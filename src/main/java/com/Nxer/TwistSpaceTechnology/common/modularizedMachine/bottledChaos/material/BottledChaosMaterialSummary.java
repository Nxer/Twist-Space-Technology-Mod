package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.util.GTUtility;

/** Bounded Waila projection; storage and material conversion do not depend on recipe authorization. */
public final class BottledChaosMaterialSummary {

    public static final int WAILA_OUTPUT_LIMIT = 20;

    public static NBTTagCompound forCache(BottledChaosMaterialCache cache, long tagMask) {
        return summarize(BottledChaosMaterialStacks.fromAmounts(cache.snapshot()), tagMask);
    }

    /** Include newly produced cache materials without making them available before the job finishes. */
    public static NBTTagCompound forJob(BottledChaosOutputBuffer outputs, BottledChaosOutputBuffer pendingCache,
        long tagMask) {
        Map<Object, Long> amounts = new LinkedHashMap<>();
        outputs.collectAmounts(amounts);
        pendingCache.collectAmounts(amounts);
        return summarize(BottledChaosMaterialStacks.fromAmounts(amounts), tagMask);
    }

    private static NBTTagCompound summarize(BottledChaosMaterialStacks materials, long tagMask) {
        var items = materials.items();
        var fluids = materials.fluids();
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("total", items.size() + fluids.size());
        NBTTagList outputs = new NBTTagList();
        Map<Object, Integer> priorities = items.isEmpty() && fluids.isEmpty() ? Collections.emptyMap()
            : BottledChaosRecipeProcessor.outputPriorities(tagMask);
        for (int priority = 0; priority < 3 && outputs.tagCount() < WAILA_OUTPUT_LIMIT; priority++) {
            for (ItemStackLong output : items) {
                if (outputs.tagCount() >= WAILA_OUTPUT_LIMIT) break;
                if (priorities.getOrDefault(TST_ItemID.create(output.itemStack()), 2) != priority) continue;
                NBTTagCompound entry = new NBTTagCompound();
                entry.setTag(
                    "item",
                    output.itemStack()
                        .writeToNBT(new NBTTagCompound()));
                entry.setLong("amount", output.stackSize());
                outputs.appendTag(entry);
            }
            for (FluidStackLong output : fluids) {
                if (outputs.tagCount() >= WAILA_OUTPUT_LIMIT) break;
                if (priorities.getOrDefault(GTUtility.FluidId.create(output.fluidStack()), 2) != priority) continue;
                NBTTagCompound entry = new NBTTagCompound();
                entry.setTag(
                    "fluid",
                    output.fluidStack()
                        .writeToNBT(new NBTTagCompound()));
                entry.setLong("amount", output.amount());
                outputs.appendTag(entry);
            }
        }
        tag.setTag("entries", outputs);
        return tag;
    }
}
