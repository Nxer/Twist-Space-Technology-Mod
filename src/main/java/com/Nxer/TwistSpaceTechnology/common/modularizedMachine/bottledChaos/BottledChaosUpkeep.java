package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.util.GTUtility;

/** Bills active structure pools once per tick, with fractional mB carried across jobs and pauses. */
public final class BottledChaosUpkeep {

    private static final BigInteger TICKS_PER_SECOND = BigInteger.valueOf(20);
    private final Map<RateKey, Integer> remainders = new HashMap<>();

    /** A type template and an unbounded per-second amount; no int-sized stack expansion. */
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

    private static GTUtility.FluidId fluidId(FluidStack fluid) {
        return GTUtility.FluidId.createNoCopy(fluid.getFluid(), null, fluid.tag);
    }

    /** Check startup reserves and the next running tick without draining fluid or advancing fractional billing. */
    public boolean canStart(List<Demand> active, List<Demand> starting, FluidStack[] inputs) {
        Map<RateKey, BigInteger> needed = new LinkedHashMap<>();
        for (Demand demand : active) {
            RateKey key = new RateKey(demand.pool(), fluidId(demand.fluid()));
            needed.merge(
                key,
                demand.perSecond()
                    .add(BigInteger.valueOf(remainders.getOrDefault(key, 0)))
                    .divide(TICKS_PER_SECOND),
                BigInteger::max);
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
            amounts.computeIfPresent(
                fluidId(input),
                (key, amount) -> amount.subtract(BigInteger.valueOf(input.amount)));
        }
        return amounts.values()
            .stream()
            .noneMatch(amount -> amount.signum() > 0);
    }

    /** All fluids are checked before any input changes. Duplicate use of a pool does not multiply its rate. */
    public Transaction prepare(List<Demand> active, FluidStack[] inputs) {
        Map<RateKey, Demand> rates = new LinkedHashMap<>();
        for (Demand demand : active) {
            RateKey key = new RateKey(demand.pool(), fluidId(demand.fluid()));
            Demand previous = rates.get(key);
            if (previous == null || previous.perSecond()
                .compareTo(demand.perSecond()) < 0) rates.put(key, demand);
        }
        Map<RateKey, Integer> nextRemainders = new HashMap<>();
        Map<GTUtility.FluidId, BigInteger> amounts = new LinkedHashMap<>();
        for (var entry : rates.entrySet()) {
            BigInteger[] tick = entry.getValue()
                .perSecond()
                .add(BigInteger.valueOf(remainders.getOrDefault(entry.getKey(), 0)))
                .divideAndRemainder(TICKS_PER_SECOND);
            nextRemainders.put(entry.getKey(), tick[1].intValue());
            amounts.merge(
                entry.getKey()
                    .fluid(),
                tick[0],
                BigInteger::add);
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
            if (needed.signum() > 0) return null;
        }
        return new Transaction(remaining, nextRemainders);
    }

    public final class Transaction {

        private final IdentityHashMap<FluidStack, Integer> remaining;
        private final Map<RateKey, Integer> nextRemainders;

        private Transaction(IdentityHashMap<FluidStack, Integer> remaining, Map<RateKey, Integer> nextRemainders) {
            this.remaining = remaining;
            this.nextRemainders = nextRemainders;
        }

        public void commitInputs() {
            remaining.forEach((fluid, amount) -> fluid.amount = amount);
        }

        public void commitAccounting() {
            remainders.putAll(nextRemainders);
        }
    }

    public NBTTagList save() {
        NBTTagList list = new NBTTagList();
        for (var entry : remainders.entrySet()) {
            NBTTagCompound tag = entry.getKey()
                .fluid()
                .getFluidStack()
                .writeToNBT(new NBTTagCompound());
            tag.setString(
                "pool",
                entry.getKey()
                    .pool());
            tag.setInteger("remainder", entry.getValue());
            list.appendTag(tag);
        }
        return list;
    }

    public void load(NBTTagList list) {
        remainders.clear();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag);
            if (fluid != null) remainders.put(
                new RateKey(tag.getString("pool"), fluidId(fluid)),
                Math.floorMod(tag.getInteger("remainder"), 20));
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
            if (fluid != null && rate.length > 0)
                demands.add(
                    new Demand(
                        tag.getString("pool"),
                        fluid,
                        new BigInteger(1, rate),
                        tag.getInteger("startupReserveSeconds")));
        }
        return Collections.unmodifiableList(demands);
    }
}
