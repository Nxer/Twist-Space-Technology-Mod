package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addIntegratedCircuitToRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addRecipeMT;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.convertCircuitRecipeItems;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.reduplicateRecipe;

import java.util.HashSet;

import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTRecipe;

public final class CircuitAssemblyLineRecipeGenerator {

    private CircuitAssemblyLineRecipeGenerator() {}

    public static void load() {
        HashSet<TST_ItemID> IgnoreRecipeOutputs = new HashSet<>();
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Crystalprocessor.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Crystalcomputer.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Ultimatecrystalcomputer.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Crystalmainframe.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Neuroprocessor.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Wetwarecomputer.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Wetwaresupercomputer.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Wetwaremainframe.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Bioprocessor.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Biowarecomputer.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Biowaresupercomputer.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_Biomainframe.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_OpticalProcessor.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_OpticalAssembly.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_OpticalComputer.get(1)));
        IgnoreRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Circuit_OpticalMainframe.get(1)));

        for (GTRecipe aRecipe : BartWorksRecipeMaps.circuitAssemblyLineRecipes.getAllRecipes()) {
            if (aRecipe == null || aRecipe.mOutputs == null
                || aRecipe.mOutputs.length == 0
                || IgnoreRecipeOutputs.contains(TST_ItemID.createNoNBT(aRecipe.mOutputs[0]))) continue;

            String itemName = Item.itemRegistry.getNameForObject(aRecipe.mOutputs[0].getItem());
            if (itemName == null || itemName.contains(Mods.Railcraft.ID) || itemName.contains(Mods.Forestry.ID))
                continue;

            GTRecipe generatedRecipe = convertCircuitRecipeItems(aRecipe);
            generatedRecipe = reduplicateRecipe(generatedRecipe, 3, 3, 4, 4, 1, 3);
            addRecipeMT(addIntegratedCircuitToRecipe(generatedRecipe, 16));
        }
    }

}
