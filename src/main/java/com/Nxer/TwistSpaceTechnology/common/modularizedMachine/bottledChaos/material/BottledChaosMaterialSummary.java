package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.util.GTUtility;

/** Output roles affect presentation only; cache and byproduct previews remain bounded. */
public final class BottledChaosMaterialSummary {

    public static final int WAILA_OUTPUT_LIMIT = 20;
    public static final int BYPRODUCT_OUTPUT_LIMIT = 10;
    public static final int GROUP_OUTPUT_LIMIT = 3;

    public static NBTTagCompound forCache(BottledChaosMaterialCache cache, long tagMask) {
        Map<Object, Long> amounts = cache.snapshot();
        return summarize(amounts, outputPriorities(amounts, tagMask, false), WAILA_OUTPUT_LIMIT, -1);
    }

    /** Actual exports and pending carry are separate presentation sources; never merge carry into final products. */
    public static NBTTagCompound forJob(BottledChaosOutputBuffer outputs, BottledChaosOutputBuffer pendingCache,
        long tagMask) {
        var amounts = outputAmounts(outputs);
        var priorities = outputPriorities(amounts, tagMask, false);
        NBTTagCompound summary = summarize(amounts, priorities, Integer.MAX_VALUE, 0);
        var intermediates = outputAmounts(pendingCache);
        amounts.forEach(
            (key, amount) -> {
                if (priorities.getOrDefault(key, 2) == 1) intermediates.merge(key, amount, Math::addExact);
            });
        summary.setTag(
            "intermediates",
            summarize(intermediates, outputPriorities(intermediates, tagMask, true), Integer.MAX_VALUE, -1));
        summary.setTag("byproducts", summarize(amounts, priorities, BYPRODUCT_OUTPUT_LIMIT, 2));
        return summary;
    }

    /** Only terminal targets of the authorized chain, never intermediate materials or unmarked coproducts. */
    public static NBTTagCompound forFinalJob(BottledChaosOutputBuffer outputs, long tagMask) {
        var amounts = outputAmounts(outputs);
        return summarize(amounts, outputPriorities(amounts, tagMask, false), Integer.MAX_VALUE, 0);
    }

    private static Map<Object, Long> outputAmounts(BottledChaosOutputBuffer outputs) {
        Map<Object, Long> amounts = new LinkedHashMap<>();
        outputs.collectAmounts(amounts);
        return amounts;
    }

    private static Map<Object, Integer> outputPriorities(Map<Object, Long> amounts, long tagMask,
        boolean intermediateOnly) {
        return amounts.isEmpty() ? Collections.emptyMap()
            : BottledChaosRecipeProcessor.outputPriorities(tagMask, intermediateOnly);
    }

    private static NBTTagCompound summarize(Map<Object, Long> amounts, Map<Object, Integer> priorities, int limit,
        int category) {
        Map<Object, Long> ordered = new LinkedHashMap<>();
        for (Object key : priorities.keySet()) {
            Long amount = amounts.get(key);
            if (amount != null) ordered.put(key, amount);
        }
        amounts.forEach(ordered::putIfAbsent);
        List<Map.Entry<Object, Long>> entries = new ArrayList<>(ordered.entrySet());
        NBTTagCompound tag = new NBTTagCompound();
        int total = 0;
        NBTTagList outputs = new NBTTagList();
        for (int priority = 0; priority < 3; priority++) {
            if (category >= 0 && category != priority) continue;
            // Stable partition: keep tag/recipe order within items and fluids; final products retain their order.
            if (priority > 0)
                entries.sort(Comparator.comparingInt(entry -> entry.getKey() instanceof TST_ItemID ? 0 : 1));
            for (var output : entries) {
                Object key = output.getKey();
                if (priorities.getOrDefault(key, 2) != priority) continue;
                total++;
                if (outputs.tagCount() >= limit) continue;
                NBTTagCompound entry = new NBTTagCompound();
                if (key instanceof TST_ItemID item) {
                    entry.setTag(
                        "item",
                        item.getItemStack(1)
                            .writeToNBT(new NBTTagCompound()));
                } else {
                    entry.setTag(
                        "fluid",
                        ((GTUtility.FluidId) key).getFluidStack(1)
                            .writeToNBT(new NBTTagCompound()));
                }
                entry.setLong("amount", output.getValue());
                outputs.appendTag(entry);
            }
        }
        tag.setInteger("total", total);
        tag.setTag("entries", outputs);
        return tag;
    }
}
