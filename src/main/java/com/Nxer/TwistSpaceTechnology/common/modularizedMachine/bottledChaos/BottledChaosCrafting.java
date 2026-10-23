package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;

/** Shared workbench transaction: cached previews are detached from the persistent inventory. */
public final class BottledChaosCrafting {

    public static final int CARRIER_SLOT = 0;
    public static final int MATERIAL_SLOT_START = 1;
    public static final int MATERIAL_SLOT_COUNT = 8;
    public static final int OUTPUT_SLOT = 9;
    public static final int SECOND_OUTPUT_SLOT = 10;
    public static final int SLOT_COUNT = SECOND_OUTPUT_SLOT + 1;

    private final ItemStackHandler inventory;
    private final Runnable changed;
    private Preview preview;
    private long revision;
    private boolean dirty = true;
    private boolean committing;
    private boolean unloading;
    private String status = "tst.bottledchaos.gui.credential_injection.ready";

    public BottledChaosCrafting(ItemStackHandler inventory, Runnable changed) {
        this.inventory = inventory;
        this.changed = changed;
    }

    public void invalidate() {
        if (committing) return;
        dirty = true;
        revision++;
    }

    public boolean isUnloading() {
        return unloading;
    }

    public void setUnloading(boolean unloading) {
        if (this.unloading == unloading) return;
        this.unloading = unloading;
        invalidate();
    }

    public String status() {
        preview();
        return status;
    }

    /** Re-evaluate only after an input or mode change, including at most one copied-disk transaction. */
    public Preview preview() {
        if (!dirty) return preview;
        dirty = false;
        preview = null;
        ItemStack[] before = snapshot();
        if (before[OUTPUT_SLOT] != null || before[SECOND_OUTPUT_SLOT] != null) {
            status = "tst.bottledchaos.gui.credential_injection.output_occupied";
            return null;
        }
        ItemStackHandler after = new ItemStackHandler(inventory.getSlots());
        for (int slot = 0; slot < before.length; slot++) after.setStackInSlot(slot, copy(before[slot]));
        List<ItemStack> outputs = new ArrayList<>();
        if (ItemBottledChaosMachineModule.isModule(before[CARRIER_SLOT])) {
            status = BottledChaosStructurePackaging.execute(after, unloading, outputs);
        } else status = inject(after);
        if (!status.endsWith(".success")) return null;
        if (outputs.isEmpty()) outputs.add(after.getStackInSlot(OUTPUT_SLOT));
        preview = new Preview(revision, before, after, outputs);
        status = "tst.bottledchaos.gui.credential_injection.preview";
        return preview;
    }

    public boolean isCurrent(Preview candidate) {
        return candidate != null && candidate == preview() && candidate.revision == revision;
    }

    /** Check the complete snapshot at take time; no periodic equality scan of large ME disk NBT is needed. */
    public boolean canTake(Preview candidate) {
        if (!isCurrent(candidate)) return false;
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            if (!ItemStack.areItemStacksEqual(candidate.before[slot], inventory.getStackInSlot(slot))) {
                invalidate();
                return false;
            }
        }
        return true;
    }

    /** Commit both results once. The untaken result becomes an ordinary, persistent inventory item. */
    public boolean take(Preview candidate, int variant, int outputSlot) {
        if (!canTake(candidate) || variant < 0
            || variant >= candidate.outputs.size()
            || outputSlot < OUTPUT_SLOT
            || outputSlot > SECOND_OUTPUT_SLOT
            || candidate.output(outputSlot, variant) == null) return false;
        committing = true;
        try {
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                ItemStack stack = slot == OUTPUT_SLOT ? candidate.outputs.get(variant)
                    : candidate.after.getStackInSlot(slot);
                inventory.setStackInSlot(slot, slot == outputSlot ? null : copy(stack));
            }
        } finally {
            committing = false;
            invalidate();
        }
        changed.run();
        return true;
    }

    public ItemStack result(int outputSlot) {
        return displayStack(inventory.getStackInSlot(outputSlot));
    }

    public void takeResult(int outputSlot) {
        inventory.setStackInSlot(outputSlot, null);
    }

    private ItemStack[] snapshot() {
        ItemStack[] stacks = new ItemStack[inventory.getSlots()];
        for (int slot = 0; slot < stacks.length; slot++) stacks[slot] = copy(inventory.getStackInSlot(slot));
        return stacks;
    }

    private static ItemStack copy(ItemStack stack) {
        return stack == null ? null : stack.copy();
    }

    /** Detach the mutable count for native slot clicks; disk and module NBT remain read-only during preview. */
    public static ItemStack displayStack(ItemStack stack) {
        if (stack == null) return null;
        ItemStack display = new ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage());
        display.setTagCompound(stack.getTagCompound());
        return display;
    }

    /** One immutable input snapshot and deduction plan, with interchangeable primary results. */
    public static final class Preview {

        private final long revision;
        private final ItemStack[] before;
        private final ItemStackHandler after;
        private final List<ItemStack> outputs;

        private Preview(long revision, ItemStack[] before, ItemStackHandler after, List<ItemStack> outputs) {
            this.revision = revision;
            this.before = before;
            this.after = after;
            this.outputs = outputs;
        }

        public long revision() {
            return revision;
        }

        public int variants() {
            return outputs.size();
        }

        public ItemStack output(int slot, int variant) {
            return displayStack(slot == OUTPUT_SLOT ? outputs.get(variant) : after.getStackInSlot(slot));
        }
    }

    private static String inject(ItemStackHandler inventory) {
        ItemStack target = inventory.getStackInSlot(CARRIER_SLOT);
        long targetCapacity = ItemBottledChaosCredential.getCapacity(target);
        if (targetCapacity <= 0) {
            return "tst.bottledchaos.gui.credential_injection.missing_target";
        }
        if (inventory.getStackInSlot(OUTPUT_SLOT) != null || inventory.getStackInSlot(SECOND_OUTPUT_SLOT) != null) {
            return "tst.bottledchaos.gui.credential_injection.output_occupied";
        }

        String splitStatus = split(inventory, target, targetCapacity);
        if (splitStatus != null) return splitStatus;

        long routeMask = ItemBottledChaosCredential.getRouteMask(target);
        long targetRouteMask = routeMask;
        int occupiedWeight = ItemBottledChaosCredential.getOccupiedWeight(target);
        if (targetRouteMask != 0 && !ItemBottledChaosCredential.isBound(target)) {
            return "tst.bottledchaos.gui.credential_injection.invalid_source";
        }
        if (targetRouteMask == 0) occupiedWeight = 0;
        boolean hasMarker = false;
        boolean hasCredential = false;
        boolean hasSource = false;
        for (int slot = MATERIAL_SLOT_START; slot < MATERIAL_SLOT_START + MATERIAL_SLOT_COUNT; slot++) {
            ItemStack source = inventory.getStackInSlot(slot);
            if (source == null) continue;
            hasSource = true;
            long sourceCapacity = ItemBottledChaosCredential.getCapacity(source);
            long sourceMask;
            int sourceWeight;
            if (sourceCapacity > 0) {
                sourceMask = ItemBottledChaosCredential.getRouteMask(source);
                sourceWeight = ItemBottledChaosCredential.getOccupiedWeight(source);
                if (!ItemBottledChaosCredential.isBound(source) || hasMarker) {
                    return "tst.bottledchaos.gui.credential_injection.invalid_source";
                }
                hasCredential = true;
            } else {
                sourceMask = BottledChaosRecipeResources.getRouteMaskForMarker(source);
                sourceWeight = 0;
                if (sourceMask == 0 || targetRouteMask != 0 || hasCredential) {
                    return "tst.bottledchaos.gui.credential_injection.invalid_source";
                }
                hasMarker = true;
            }
            if ((routeMask & sourceMask) != 0) {
                return "tst.bottledchaos.gui.credential_injection.duplicate_tag";
            }
            routeMask |= sourceMask;
            if (sourceWeight > targetCapacity - occupiedWeight) {
                return "tst.bottledchaos.gui.credential_injection.capacity_exceeded";
            }
            occupiedWeight += sourceWeight;
        }
        if (!hasSource || routeMask == 0) {
            return "tst.bottledchaos.gui.credential_injection.missing_source";
        }
        if (hasMarker) occupiedWeight = ItemBottledChaosCredential.getRequiredWeight(routeMask);
        if (occupiedWeight > targetCapacity) {
            return "tst.bottledchaos.gui.credential_injection.capacity_exceeded";
        }

        ItemStack result = target.copy();
        if (!ItemBottledChaosCredential.setBinding(result, routeMask, occupiedWeight)) {
            return "tst.bottledchaos.gui.credential_injection.invalid_source";
        }
        inventory.setStackInSlot(OUTPUT_SLOT, result);
        inventory.setStackInSlot(CARRIER_SLOT, null);
        for (int slot = MATERIAL_SLOT_START; slot < MATERIAL_SLOT_START + MATERIAL_SLOT_COUNT; slot++) {
            if (inventory.getStackInSlot(slot) != null) {
                inventory.extractItem(slot, 1, false);
            }
        }
        return "tst.bottledchaos.gui.credential_injection.success";
    }

    private static String split(ItemStackHandler inventory, ItemStack target, long targetCapacity) {
        if (ItemBottledChaosCredential.getRouteMask(target) != 0) return null;

        int sourceSlot = -1;
        ItemStack source = null;
        for (int slot = MATERIAL_SLOT_START; slot < MATERIAL_SLOT_START + MATERIAL_SLOT_COUNT; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack == null) continue;
            if (source != null) return null;
            sourceSlot = slot;
            source = stack;
        }
        if (!ItemBottledChaosCredential.isBound(source)) return null;

        long sourceMask = ItemBottledChaosCredential.getRouteMask(source);
        List<Integer> routeIndices = new ArrayList<>();
        long individualCostSum = 0;
        for (int index = 0; index < CredentialModel.ROUTE_COUNT; index++) {
            if ((sourceMask & (1L << index)) == 0) continue;
            routeIndices.add(index);
            individualCostSum += ItemBottledChaosCredential.getRequiredWeight(1L << index);
        }
        if (individualCostSum <= targetCapacity) return null;

        routeIndices.sort(
            (left, right) -> Integer.compare(
                ItemBottledChaosCredential.getRequiredWeight(1L << right),
                ItemBottledChaosCredential.getRequiredWeight(1L << left)));
        long transferredMask = 0;
        int transferredWeight = 0;
        for (int index : routeIndices) {
            int weight = ItemBottledChaosCredential.getRequiredWeight(1L << index);
            if (weight > targetCapacity - transferredWeight) continue;
            transferredMask |= 1L << index;
            transferredWeight += weight;
        }
        if (transferredMask == 0) {
            return "tst.bottledchaos.gui.credential_injection.capacity_exceeded";
        }

        long remainingMask = sourceMask & ~transferredMask;
        ItemStack remaining = source.copy();
        ItemStack transferred = target.copy();
        int remainingWeight = Math.max(
            ItemBottledChaosCredential.getRequiredWeight(remainingMask),
            ItemBottledChaosCredential.getOccupiedWeight(source) - transferredWeight);
        if (!ItemBottledChaosCredential.setBinding(remaining, remainingMask, remainingWeight)
            || !ItemBottledChaosCredential.setBinding(transferred, transferredMask, transferredWeight)) {
            return "tst.bottledchaos.gui.credential_injection.invalid_source";
        }

        inventory.setStackInSlot(OUTPUT_SLOT, remaining);
        inventory.setStackInSlot(SECOND_OUTPUT_SLOT, transferred);
        inventory.setStackInSlot(CARRIER_SLOT, null);
        inventory.setStackInSlot(sourceSlot, null);
        return "tst.bottledchaos.gui.credential_injection.success";
    }

}
