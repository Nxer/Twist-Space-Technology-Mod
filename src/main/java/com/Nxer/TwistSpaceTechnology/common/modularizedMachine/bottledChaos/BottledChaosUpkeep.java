package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;

/** Charges whole seconds before running, including the first partial second. */
public final class BottledChaosUpkeep {

    private final Map<RateKey, Bill> bills = new LinkedHashMap<>();
    private final List<Bill> activeBills = new ArrayList<>();
    private CheckRecipeResult failure = CheckRecipeResultRegistry.SUCCESSFUL;

    /** A type template and an unbounded integer per-second amount; no int-sized stack expansion. */
    @Desugar
    public record Demand(String pool, FluidStack fluid, BigInteger perSecond, int startupReserveSeconds) {

        public Demand(String pool, FluidStack fluid, BigInteger perSecond) {
            this(pool, fluid, perSecond, 1);
        }

        public Demand {
            fluid = GTUtility.copyAmount(1, fluid);
            startupReserveSeconds = Math.max(1, startupReserveSeconds);
        }
    }

    @Desugar
    private record RateKey(String pool, GTUtility.FluidId fluid) {}

    private static final class Bill {

        private final RateKey key;
        private BigInteger perSecond;
        private int remainingTicks;

        private Bill(RateKey key, BigInteger perSecond) {
            this.key = key;
            this.perSecond = perSecond;
        }
    }

    private static GTUtility.FluidId fluidId(FluidStack fluid) {
        return GTUtility.FluidId.createNoCopy(fluid.getFluid(), null, fluid.tag);
    }

    /** Rebuilt only when advancing jobs change. Multiple users of a pool share its maximum rate. */
    public void setActiveDemands(List<Demand> active) {
        Map<RateKey, BigInteger> rates = new LinkedHashMap<>();
        for (Demand demand : active) {
            rates.merge(new RateKey(demand.pool(), fluidId(demand.fluid())), demand.perSecond(), BigInteger::max);
        }
        activeBills.clear();
        rates.forEach((key, rate) -> {
            Bill bill = bills.computeIfAbsent(key, ignored -> new Bill(key, rate));
            if (!bill.perSecond.equals(rate)) {
                // A new configuration starts a new whole second at its own rate.
                bill.perSecond = rate;
                bill.remainingTicks = 0;
            }
            activeBills.add(bill);
        });
    }

    public boolean needsPayment() {
        for (Bill bill : activeBills) if (bill.remainingTicks == 0) return true;
        return false;
    }

    /** Only successful running ticks use the prepaid second. Pauses and output waits do not. */
    public void advance() {
        for (Bill bill : activeBills) bill.remainingTicks--;
    }

    /** Startup reserves are checked, never consumed here. Already prepaid pools need no running reserve. */
    public CheckRecipeResult checkStart(List<Demand> active, List<Demand> starting, FluidStack[] inputs) {
        Map<RateKey, BigInteger> needed = new LinkedHashMap<>();
        for (Demand demand : active) {
            RateKey key = new RateKey(demand.pool(), fluidId(demand.fluid()));
            Bill bill = bills.get(key);
            if (bill == null || bill.remainingTicks == 0 || !bill.perSecond.equals(demand.perSecond()))
                needed.merge(key, demand.perSecond(), BigInteger::max);
        }
        for (Demand demand : starting) {
            needed.merge(
                new RateKey(demand.pool(), fluidId(demand.fluid())),
                demand.perSecond()
                    .multiply(BigInteger.valueOf(demand.startupReserveSeconds())),
                BigInteger::max);
        }
        Map<GTUtility.FluidId, BigInteger> amounts = new LinkedHashMap<>();
        needed.forEach((key, amount) -> amounts.merge(key.fluid(), amount, BigInteger::add));
        IdentityHashMap<FluidStack, Boolean> seen = new IdentityHashMap<>();
        for (FluidStack input : inputs) {
            if (input == null || input.amount <= 0 || seen.put(input, Boolean.TRUE) != null) continue;
            amounts
                .computeIfPresent(fluidId(input), (key, amount) -> amount.subtract(BigInteger.valueOf(input.amount)));
        }
        for (var entry : amounts.entrySet()) {
            if (entry.getValue()
                .signum() > 0) return missingFluid(needed, entry.getKey(), entry.getValue());
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private static CheckRecipeResult missingFluid(Map<RateKey, BigInteger> needed, GTUtility.FluidId fluid,
        BigInteger amount) {
        StringJoiner pools = new StringJoiner(";");
        for (var entry : needed.entrySet()) {
            if (entry.getValue()
                .signum() > 0 && entry.getKey()
                    .fluid()
                    .equals(fluid))
                pools.add(
                    entry.getKey()
                        .pool());
        }
        return BottledChaosPoolFailure.missingFluid(pools.toString(), fluid.getFluidStack(), amount);
    }

    public CheckRecipeResult getFailure() {
        return failure;
    }

    /** Precheck every due pool before consuming any fluid. No fractional or deferred charges. */
    public Transaction prepare(FluidStack[] inputs) {
        failure = CheckRecipeResultRegistry.SUCCESSFUL;
        Map<RateKey, BigInteger> perPool = new LinkedHashMap<>();
        Map<GTUtility.FluidId, BigInteger> amounts = new LinkedHashMap<>();
        List<Bill> due = new ArrayList<>();
        for (Bill bill : activeBills) {
            if (bill.remainingTicks != 0) continue;
            due.add(bill);
            perPool.put(bill.key, bill.perSecond);
            amounts.merge(bill.key.fluid(), bill.perSecond, BigInteger::add);
        }
        IdentityHashMap<FluidStack, Integer> remaining = new IdentityHashMap<>();
        for (FluidStack input : inputs) {
            if (input != null && input.amount > 0) remaining.putIfAbsent(input, input.amount);
        }
        for (var entry : amounts.entrySet()) {
            BigInteger needed = entry.getValue();
            for (FluidStack input : inputs) {
                if (needed.signum() == 0) break;
                if (input == null || !entry.getKey()
                    .matches(input)) continue;
                int available = remaining.getOrDefault(input, 0);
                int taken = needed.min(BigInteger.valueOf(available))
                    .intValue();
                remaining.put(input, available - taken);
                needed = needed.subtract(BigInteger.valueOf(taken));
            }
            if (needed.signum() > 0) {
                failure = missingFluid(perPool, entry.getKey(), needed);
                return null;
            }
        }
        return new Transaction(remaining, due);
    }

    public final class Transaction {

        private final IdentityHashMap<FluidStack, Integer> remaining;
        private final List<Bill> due;

        private Transaction(IdentityHashMap<FluidStack, Integer> remaining, List<Bill> due) {
            this.remaining = remaining;
            this.due = due;
        }

        public void commitInputs() {
            remaining.forEach((fluid, amount) -> fluid.amount = amount);
        }

        public void commitAccounting() {
            for (Bill bill : due) bill.remainingTicks = 20;
        }
    }

    public NBTTagList save() {
        NBTTagList list = new NBTTagList();
        for (Bill bill : bills.values()) {
            if (bill.remainingTicks == 0) continue;
            NBTTagCompound tag = bill.key.fluid()
                .getFluidStack()
                .writeToNBT(new NBTTagCompound());
            tag.setString("pool", bill.key.pool());
            tag.setByteArray("rate", bill.perSecond.toByteArray());
            tag.setInteger("remainingTicks", bill.remainingTicks);
            list.appendTag(tag);
        }
        return list;
    }

    public void load(NBTTagList list) {
        bills.clear();
        activeBills.clear();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag);
            byte[] rate = tag.getByteArray("rate");
            if (fluid == null || rate.length == 0) continue;
            RateKey key = new RateKey(tag.getString("pool"), fluidId(fluid));
            Bill bill = new Bill(key, new BigInteger(rate));
            bill.remainingTicks = tag.getInteger("remainingTicks");
            bills.put(key, bill);
        }
    }

    public static NBTTagList saveDemands(List<Demand> demands) {
        NBTTagList list = new NBTTagList();
        for (Demand demand : demands) {
            NBTTagCompound tag = demand.fluid()
                .writeToNBT(new NBTTagCompound());
            tag.setString("pool", demand.pool());
            tag.setInteger("startupReserveSeconds", demand.startupReserveSeconds());
            tag.setByteArray(
                "rate",
                demand.perSecond()
                    .toByteArray());
            list.appendTag(tag);
        }
        return list;
    }

    public static List<Demand> loadDemands(NBTTagList list) {
        List<Demand> demands = new ArrayList<>();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag);
            byte[] rate = tag.getByteArray("rate");
            if (fluid != null && rate.length > 0) demands.add(
                new Demand(
                    tag.getString("pool"),
                    fluid,
                    new BigInteger(1, rate),
                    tag.getInteger("startupReserveSeconds")));
        }
        return Collections.unmodifiableList(demands);
    }
}
