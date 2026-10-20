package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

/**
 * Per-hatch, paid intermediate inventory shared by every authorized route. Family labels only identify saved buckets.
 */
public final class BottledChaosMaterialCache {

    private String[] families = new String[0];
    private BottledChaosOutputBuffer[] inventories = new BottledChaosOutputBuffer[0];
    private String lastFamily = "";
    private int lastEntry = -1;

    public String lastFamily() {
        return lastFamily;
    }

    public int lastEntry() {
        return lastEntry;
    }

    public void remember(String family, int entry) {
        lastFamily = family;
        lastEntry = entry;
    }

    public Map<Object, Long> snapshot() {
        Map<Object, Long> amounts = new LinkedHashMap<>();
        for (BottledChaosOutputBuffer inventory : inventories) inventory.collectAmounts(amounts);
        return amounts;
    }

    /** Merge all families for a bounded Waila summary without changing their stored inventories. */
    public NBTTagCompound getWailaSummary(long routeMask) {
        BottledChaosRecipeProcessor.Plan materials = BottledChaosRecipeProcessor.materialPlan(snapshot());
        BottledChaosOutputBuffer summary = new BottledChaosOutputBuffer();
        summary.set(materials.itemOutputs(), materials.fluidOutputs());
        return summary.getWailaSummary(routeMask);
    }

    /** Called once after real inputs and output reservations have committed. */
    public void replace(String family, BottledChaosRecipeProcessor.Plan remaining) {
        int index = indexOf(family);
        if (index < 0) {
            if (remaining.itemOutputs()
                .isEmpty()
                && remaining.fluidOutputs()
                    .isEmpty()) {
                clearMaterials();
                return;
            }
            index = families.length;
            families = Arrays.copyOf(families, index + 1);
            inventories = Arrays.copyOf(inventories, index + 1);
            families[index] = family;
            inventories[index] = new BottledChaosOutputBuffer();
        }
        clearMaterials();
        inventories[index].set(remaining.itemOutputs(), remaining.fluidOutputs());
    }

    /** Newly produced intermediates become available only after the paid job finishes. */
    public void add(String family, BottledChaosOutputBuffer produced) {
        if (produced.isEmpty()) return;
        Map<Object, Long> amounts = snapshot();
        produced.collectAmounts(amounts);
        replace(family, BottledChaosRecipeProcessor.materialPlan(amounts));
    }

    /** Reserve every family before exporting anything; a blocked output leaves the cache untouched. */
    public CheckRecipeResult reserveExport(BottledChaosOutputBuffer.Transaction transaction) {
        for (BottledChaosOutputBuffer inventory : inventories) {
            CheckRecipeResult result = transaction.reserve(inventory);
            if (!result.wasSuccessful()) return result;
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public boolean isEmpty() {
        for (BottledChaosOutputBuffer inventory : inventories) if (!inventory.isEmpty()) return false;
        return true;
    }

    public void clearMaterials() {
        for (BottledChaosOutputBuffer inventory : inventories) inventory.clear();
    }

    private int indexOf(String family) {
        for (int i = 0; i < families.length; i++) if (families[i].equals(family)) return i;
        return -1;
    }

    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("lastFamily", lastFamily);
        NBTTagList entries = new NBTTagList();
        for (int i = 0; i < families.length; i++) {
            if (inventories[i].isEmpty()) continue;
            NBTTagCompound entry = inventories[i].save();
            entry.setString("family", families[i]);
            entries.appendTag(entry);
        }
        tag.setTag("families", entries);
        return tag;
    }

    public void load(NBTTagCompound tag) {
        lastFamily = tag.getString("lastFamily");
        lastEntry = -1; // Entry indexes are startup-local; material identities and family names are persistent.
        NBTTagList entries = tag.getTagList("families", 10);
        families = new String[entries.tagCount()];
        inventories = new BottledChaosOutputBuffer[entries.tagCount()];
        for (int i = 0; i < families.length; i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            families[i] = entry.getString("family");
            inventories[i] = new BottledChaosOutputBuffer();
            inventories[i].load(entry);
        }
    }
}
