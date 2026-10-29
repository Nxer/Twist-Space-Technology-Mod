package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result.BottledChaosPoolFailure;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;

/** Prepays whole billing periods; only successful running ticks use the prepaid time. */
public final class BottledChaosUpkeep {

    private final Map<RateKey, Bill> bills = new LinkedHashMap<>();
    private final List<Bill> activeBills = new ArrayList<>();

    /** A type template and an unbounded integer amount per billing period. */
    @Desugar
    public record Demand(String pool, FluidStack fluid, BigInteger amount, int startupReserveSeconds, int periodSeconds,
        int installation) {

        public Demand(String pool, FluidStack fluid, BigInteger amount, int startupReserveSeconds, int periodSeconds) {
            this(pool, fluid, amount, startupReserveSeconds, periodSeconds, -1);
        }

        public Demand(String pool, FluidStack fluid, BigInteger perSecond) {
            this(pool, fluid, perSecond, 1, 1);
        }

        public Demand(String pool, FluidStack fluid, BigInteger perSecond, int startupReserveSeconds) {
            this(pool, fluid, perSecond, startupReserveSeconds, 1);
        }

        public Demand {
            fluid = GTUtility.copyAmount(1, fluid);
            startupReserveSeconds = Math.max(1, startupReserveSeconds);
            if (periodSeconds != 1 && periodSeconds != 5)
                throw new IllegalArgumentException("Unsupported upkeep period");
        }

        public BigDecimal perSecond() {
            return new BigDecimal(amount).divide(BigDecimal.valueOf(periodSeconds));
        }
    }

    /** Charged once when a new job starts, independently of its duration or batch count. */
    @Desugar
    public record ItemDemand(String pool, ItemStack item, long amount) {

        public ItemDemand {
            item = GTUtility.copyAmount(1, item);
        }
    }

    /** Only mutate the caller's working copies after every pool's startup items are available. */
    public static CheckRecipeResult consumeRoundItems(List<ItemDemand> demands, ItemStack[] inputs) {
        IdentityHashMap<ItemStack, Integer> remaining = new IdentityHashMap<>();
        for (ItemStack input : inputs) {
            if (input != null && input.stackSize > 0) remaining.putIfAbsent(input, input.stackSize);
        }
        for (ItemDemand demand : demands) {
            long needed = demand.amount();
            for (ItemStack input : inputs) {
                if (needed == 0) break;
                if (input == null || !GTUtility.areStacksEqual(input, demand.item())) continue;
                int taken = (int) Math.min(needed, remaining.getOrDefault(input, 0));
                remaining.put(input, remaining.getOrDefault(input, 0) - taken);
                needed -= taken;
            }
            if (needed > 0) return BottledChaosPoolFailure.missingItem(demand.pool(), demand.item(), needed);
        }
        remaining.forEach((item, amount) -> item.stackSize = amount);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Desugar
    private record RateKey(String pool, GTUtility.FluidId fluid, int periodSeconds, int installation) {

        @Override
        public boolean equals(Object other) {
            return other instanceof RateKey key && installation == key.installation
                && (installation >= 0 || pool.equals(key.pool))
                && fluid.equals(key.fluid)
                && periodSeconds == key.periodSeconds;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(installation >= 0 ? installation : pool, fluid, periodSeconds);
        }
    }

    private static RateKey key(Demand demand) {
        return new RateKey(demand.pool(), fluidId(demand.fluid()), demand.periodSeconds(), demand.installation());
    }

    private static final class Bill {

        private final RateKey key;
        private BigInteger amount;
        private int remainingTicks;

        private Bill(RateKey key, BigInteger amount) {
            this.key = key;
            this.amount = amount;
        }
    }

    private static GTUtility.FluidId fluidId(FluidStack fluid) {
        return GTUtility.FluidId.createNoCopy(fluid.getFluid(), null, fluid.tag);
    }

    /** Rebuilt only when advancing jobs change. Multiple users of a pool share its maximum rate. */
    public void setActiveDemands(List<Demand> active) {
        Map<RateKey, BigInteger> rates = new LinkedHashMap<>();
        for (Demand demand : active) {
            rates.merge(key(demand), demand.amount(), BigInteger::max);
        }
        activeBills.clear();
        rates.forEach((key, rate) -> {
            Bill bill = bills.computeIfAbsent(key, ignored -> new Bill(key, rate));
            if (!bill.amount.equals(rate)) {
                // A new configuration starts a new billing period at its own rate.
                bill.amount = rate;
                bill.remainingTicks = 0;
            }
            activeBills.add(bill);
        });
    }

    public boolean needsPayment() {
        for (Bill bill : activeBills) if (bill.remainingTicks == 0) return true;
        return false;
    }

    public String activeFluidText() {
        Map<GTUtility.FluidId, BigDecimal> rates = new LinkedHashMap<>();
        for (Bill bill : activeBills) rates.merge(
            bill.key.fluid(),
            new BigDecimal(bill.amount).divide(BigDecimal.valueOf(bill.key.periodSeconds())),
            BigDecimal::add);
        StringJoiner lines = new StringJoiner("\n");
        rates.forEach(
            (fluid, rate) -> lines.add(
                fluid.getFluidStack()
                    .getLocalizedName() + ": "
                    + rate.stripTrailingZeros()
                        .toPlainString()
                    + " L/s"));
        return lines.toString();
    }

    public static NBTTagList saveItems(List<ItemDemand> items) {
        NBTTagList list = new NBTTagList();
        for (ItemDemand item : items) {
            NBTTagCompound tag = item.item()
                .writeToNBT(new NBTTagCompound());
            tag.setString("pool", item.pool());
            tag.setLong("amount", item.amount());
            list.appendTag(tag);
        }
        return list;
    }

    public static List<ItemDemand> loadItems(NBTTagList list) {
        List<ItemDemand> items = new ArrayList<>();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            ItemStack item = ItemStack.loadItemStackFromNBT(tag);
            if (item != null && tag.getLong("amount") > 0)
                items.add(new ItemDemand(tag.getString("pool"), item, tag.getLong("amount")));
        }
        return Collections.unmodifiableList(items);
    }

    /** Only successful running ticks use the prepaid period. Pauses and output waits do not. */
    public void advance() {
        for (Bill bill : activeBills) bill.remainingTicks--;
    }

    /** Startup reserves are checked, never consumed here. Already prepaid pools need no running reserve. */
    public CheckRecipeResult checkStart(List<Demand> active, List<Demand> starting, FluidStack[] inputs) {
        Map<RateKey, BigInteger> needed = new LinkedHashMap<>();
        for (Demand demand : active) {
            RateKey key = key(demand);
            Bill bill = bills.get(key);
            if (bill == null || bill.remainingTicks == 0 || !bill.amount.equals(demand.amount()))
                needed.merge(key, demand.amount(), BigInteger::max);
        }
        for (Demand demand : starting) {
            needed.merge(
                key(demand),
                demand.amount()
                    .multiply(BigInteger.valueOf((demand.startupReserveSeconds() - 1L) / demand.periodSeconds() + 1)),
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

    /** Precheck every due pool before consuming any fluid. No fractional or deferred charges. */
    public Transaction prepare(FluidStack[] inputs) {
        Map<RateKey, BigInteger> perPool = new LinkedHashMap<>();
        Map<GTUtility.FluidId, BigInteger> amounts = new LinkedHashMap<>();
        List<Bill> due = new ArrayList<>();
        for (Bill bill : activeBills) {
            if (bill.remainingTicks != 0) continue;
            due.add(bill);
            perPool.put(bill.key, bill.amount);
            amounts.merge(bill.key.fluid(), bill.amount, BigInteger::add);
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
                return new Transaction(missingFluid(perPool, entry.getKey(), needed), null, null);
            }
        }
        return new Transaction(CheckRecipeResultRegistry.SUCCESSFUL, remaining, due);
    }

    /** One preparation result. Only a successful result has input and accounting changes to commit. */
    public static final class Transaction {

        private final CheckRecipeResult result;
        private final IdentityHashMap<FluidStack, Integer> remaining;
        private final List<Bill> due;

        private Transaction(CheckRecipeResult result, IdentityHashMap<FluidStack, Integer> remaining, List<Bill> due) {
            this.result = result;
            this.remaining = remaining;
            this.due = due;
        }

        public CheckRecipeResult result() {
            return result;
        }

        public void commitInputs() {
            remaining.forEach((fluid, amount) -> fluid.amount = amount);
        }

        public void commitAccounting() {
            for (Bill bill : due) bill.remainingTicks = 20 * bill.key.periodSeconds();
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
            tag.setInteger("installation", bill.key.installation());
            tag.setInteger("periodSeconds", bill.key.periodSeconds());
            tag.setByteArray("rate", bill.amount.toByteArray());
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
            int periodSeconds = tag.getInteger("periodSeconds");
            if (fluid == null || rate.length == 0 || (periodSeconds != 1 && periodSeconds != 5)) continue;
            RateKey key = new RateKey(
                tag.getString("pool"),
                fluidId(fluid),
                periodSeconds,
                tag.getInteger("installation"));
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
            tag.setInteger("installation", demand.installation());
            tag.setInteger("startupReserveSeconds", demand.startupReserveSeconds());
            tag.setInteger("periodSeconds", demand.periodSeconds());
            tag.setByteArray(
                "rate",
                demand.amount()
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
            int periodSeconds = tag.getInteger("periodSeconds");
            if (fluid != null && rate.length > 0 && (periodSeconds == 1 || periodSeconds == 5)) demands.add(
                new Demand(
                    tag.getString("pool"),
                    fluid,
                    new BigInteger(1, rate),
                    tag.getInteger("startupReserveSeconds"),
                    periodSeconds,
                    tag.getInteger("installation")));
        }
        return Collections.unmodifiableList(demands);
    }

}
