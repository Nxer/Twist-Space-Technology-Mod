package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;

/** Applies GT's ME output rules while reserving and committing native AE long stacks. */
public final class TSTMEOutputTransaction<ID, T, S extends IAEStack<S>> {

    private final MTEHatchOutputMEBase<S> provider;
    private final Function<T, ID> identify;
    private final Function<T, S> toAEStack;
    private final BaseActionSource actionSource;
    private final Runnable markDirty;
    private final IMEInventoryHandler<S> simulatedCell;
    private final Map<ID, S> reservedStacks = new LinkedHashMap<>();
    private final long availableSpace;
    private final boolean checkCell;
    private final boolean allowAnyInput;
    private final boolean dynamicCapacity;
    private long remainingLongRoom;
    private long reservedAmount;
    private boolean active = true;

    private TSTMEOutputTransaction(MTEHatchOutputMEBase<S> provider, boolean recipeCheck, boolean protectOutput,
        Function<T, ID> identify, Function<T, S> toAEStack, BaseActionSource actionSource, Runnable markDirty,
        Supplier<IMEInventoryHandler<S>> cellInventory) {
        this.provider = provider;
        this.identify = identify;
        this.toAEStack = toAEStack;
        this.actionSource = actionSource;
        this.markDirty = markDirty;
        checkCell = recipeCheck && provider.shouldCheckCell();
        if (checkCell) {
            provider.flushCachedStack();
            simulatedCell = cellInventory.get();
        } else {
            simulatedCell = null;
        }
        long cached = provider.getCachedAmount();
        remainingLongRoom = cached < 0 ? 0 : Long.MAX_VALUE - cached;
        availableSpace = Math.max(0, provider.getPhysicalSpace());
        dynamicCapacity = recipeCheck && protectOutput
            && provider.getCheckMode()
            && (!provider.getCacheMode() || !provider.isDistribution())
            && !provider.canVoidOverflow();
        allowAnyInput = (!provider.getCheckMode() && availableSpace > 0)
            || (!recipeCheck && provider.getLastInputTick() == provider.getTickCounter());
    }

    public static TSTMEOutputTransaction<GTUtility.ItemId, ItemStack, IAEItemStack> forItems(MTEHatchOutputBusME bus,
        boolean recipeCheck, boolean protectOutput) {
        return new TSTMEOutputTransaction<>(
            bus.getProvider(),
            recipeCheck,
            protectOutput,
            GTUtility.ItemId::createNoCopy,
            AEItemStack::create,
            bus.getActionSource(),
            bus::markDirty,
            () -> bus.getCellStack() == null ? null
                : AEApi.instance()
                    .registries()
                    .cell()
                    .getCellInventory(
                        bus.getCellStack()
                            .copy(),
                        bus.getISaveProvider(),
                        bus.getChannel()));
    }

    public static TSTMEOutputTransaction<GTUtility.FluidId, FluidStack, IAEFluidStack> forFluids(MTEHatchOutputME hatch,
        boolean recipeCheck, boolean protectOutput) {
        return new TSTMEOutputTransaction<>(
            hatch.getProvider(),
            recipeCheck,
            protectOutput,
            (FluidStack stack) -> GTUtility.FluidId.create(stack),
            AEFluidStack::create,
            hatch.getActionSource(),
            hatch::markDirty,
            () -> hatch.getCellStack() == null ? null
                : AEApi.instance()
                    .registries()
                    .cell()
                    .getCellInventory(
                        hatch.getCellStack()
                            .copy(),
                        hatch.getISaveProvider(),
                        hatch.getChannel()));
    }

    /** Reserves the long amount in one operation; the supplied template is never modified. */
    public long reserve(T template, long amount) {
        if (!active) throw new IllegalStateException("Cannot add to a transaction after committing it");
        if (template == null || amount <= 0 || remainingLongRoom <= 0) return 0;
        S stack = toAEStack.apply(template);
        if (stack == null || !provider.canStore(stack)) return 0;
        if (!allowAnyInput && reservedAmount >= availableSpace) return 0;

        long offered = Math.min(amount, remainingLongRoom);
        long accepted;
        if (checkCell) {
            if (simulatedCell == null) return 0;
            stack.setStackSize(offered);
            S rejected = simulatedCell.injectItems(stack, Actionable.MODULATE, actionSource);
            long rejectedAmount = rejected == null ? 0 : Math.max(0, Math.min(offered, rejected.getStackSize()));
            accepted = offered - rejectedAmount;
        } else if (dynamicCapacity) {
            accepted = Math.min(offered, Math.max(0, availableSpace - reservedAmount));
        } else {
            accepted = offered;
        }
        if (accepted <= 0) return 0;

        ID id = identify.apply(template);
        S reserved = reservedStacks.get(id);
        if (reserved == null) {
            stack.setStackSize(accepted);
            reservedStacks.put(id, stack);
        } else {
            reserved.setStackSize(reserved.getStackSize() + accepted);
        }
        reservedAmount += accepted;
        remainingLongRoom -= accepted;
        return accepted;
    }

    public void commit() {
        if (!active) throw new IllegalStateException("Cannot commit a transaction more than once");
        if (!reservedStacks.isEmpty()) {
            for (S stack : reservedStacks.values()) provider.addToCache(stack);
            provider.updateLastInputTick();
            markDirty.run();
        }
        active = false;
    }
}
