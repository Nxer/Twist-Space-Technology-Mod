package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.util.GTUtility.FluidId;

/** Copied material templates with exact long quantities, independent of recipe timing or batch counts. */
@Desugar
public record BottledChaosMaterialStacks(List<ItemStackLong> items, List<FluidStackLong> fluids) {

    public static BottledChaosMaterialStacks fromAmounts(Map<Object, Long> amounts) {
        List<ItemStackLong> items = new ArrayList<>();
        List<FluidStackLong> fluids = new ArrayList<>();
        amounts.forEach((key, amount) -> appendResourceStack(key, amount, items, fluids));
        return new BottledChaosMaterialStacks(
            Collections.unmodifiableList(items),
            Collections.unmodifiableList(fluids));
    }

    /** Append in source order, including zero-quantity catalysts used by recipe displays. */
    public static void appendResourceStack(Object key, long amount, List<ItemStackLong> items,
        List<FluidStackLong> fluids) {
        // Copy templates so neither runtime outputs nor NEI can mutate cached identities through NBT.
        if (key instanceof TST_ItemID item) items.add(
            new ItemStackLong(
                item.getItemStack()
                    .copy(),
                amount));
        else fluids.add(
            new FluidStackLong(
                ((FluidId) key).getFluidStack(1)
                    .copy(),
                amount));
    }
}
