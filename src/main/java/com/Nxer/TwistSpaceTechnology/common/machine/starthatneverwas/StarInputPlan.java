package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellHelper.InputSource;

import gregtech.api.util.GTUtility;

/** Reservations against the current GT recipe-processing snapshot; no mutation during planning. */
public final class StarInputPlan {

    private final List<InputSource> sources;
    private final Map<InputSource, Long> debits = new LinkedHashMap<>();
    private final Map<Object, Map<Object, InputSource>> owners = new IdentityHashMap<>();
    private boolean commitAttempted;

    public StarInputPlan(List<InputSource> items, List<InputSource> fluids) {
        List<InputSource> inputs = new ArrayList<>(items);
        inputs.addAll(fluids);
        sources = InputSource.unique(inputs);
        for (InputSource source : sources) source.register(owners);
    }

    public List<ItemStack> itemTypes() {
        Map<Object, ItemStack> types = new LinkedHashMap<>();
        for (InputSource source : sources) {
            if (source.amount > 0 && source.template instanceof ItemStack stack) {
                types.putIfAbsent(source.type, stack);
            }
        }
        return new ArrayList<>(types.values());
    }

    public long available(ItemStack template) {
        return template == null ? 0 : available(GTUtility.ItemId.create(template));
    }

    public long available(FluidStack template) {
        return template == null ? 0 : available(GTUtility.FluidId.create(template));
    }

    /** Read-only trial against the snapshot. Equal item/meta/NBT requirements share one budget. */
    public int firstMissingItem(ItemStackLong... costs) {
        Map<Object, Long> required = new LinkedHashMap<>();
        for (int i = 0; i < costs.length; i++) {
            ItemStackLong cost = costs[i];
            long amount = cost.stackSize();
            if (amount == 0) continue;
            if (commitAttempted || amount < 0 || cost.itemStack() == null) return i;
            Object type = GTUtility.ItemId.create(cost.itemStack());
            long used = required.getOrDefault(type, 0L);
            if (amount > available(type) - used) return i;
            required.put(type, used + amount);
        }
        return -1;
    }

    private long available(Object type) {
        long total = 0;
        for (InputSource source : sources) {
            if (!source.type.equals(type)) continue;
            long remaining = Math.max(0, source.amount - debits.getOrDefault(source, 0L));
            // Availability may saturate; each actual source reservation and extraction stays exact.
            if (remaining > Long.MAX_VALUE - total) return Long.MAX_VALUE;
            total += remaining;
        }
        return total;
    }

    public boolean item(ItemStack template, long amount) {
        if (amount == 0) return true;
        return template != null && reserve(GTUtility.ItemId.create(template), amount);
    }

    public boolean fluid(FluidStack template, long amount) {
        if (amount == 0) return true;
        return template != null && reserve(GTUtility.FluidId.create(template), amount);
    }

    private boolean reserve(Object type, long amount) {
        if (commitAttempted || amount < 0 || available(type) < amount) return false;
        for (InputSource source : sources) {
            if (!source.type.equals(type)) continue;
            long used = debits.getOrDefault(source, 0L);
            long take = Math.min(amount, Math.max(0, source.amount - used));
            if (take > 0) debits.put(source, used + take);
            amount -= take;
            if (amount == 0) return true;
        }
        return false;
    }

    /** Reserve a dedicated port without making its contents available to general ingredients. */
    public boolean reserveFrom(InputSource port, long amount) {
        if (amount == 0) return true;
        if (port == null || commitAttempted || amount < 0 || amount > port.amount) return false;
        InputSource source = port.register(owners);
        if (source == null) return false;
        long used = debits.getOrDefault(source, 0L);
        if (amount > source.amount - used) return false;
        debits.put(source, used + amount);
        return true;
    }

    public boolean canCommit() {
        if (commitAttempted) return false;
        for (Map.Entry<InputSource, Long> debit : debits.entrySet()) {
            if (debit.getKey().available.getAsLong() < debit.getValue()) return false;
        }
        return true;
    }

    public boolean commit() {
        if (!canCommit()) return false;
        commitAttempted = true;
        // As with the shared GT/ME input path, a short real extraction cannot be rolled back or retried.
        for (Map.Entry<InputSource, Long> debit : debits.entrySet()) {
            if (debit.getKey().extract.applyAsLong(debit.getValue()) != debit.getValue()) {
                debits.clear();
                return false;
            }
        }
        debits.clear();
        return true;
    }
}
