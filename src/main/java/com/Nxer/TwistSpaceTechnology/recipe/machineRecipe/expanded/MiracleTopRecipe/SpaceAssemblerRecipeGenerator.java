package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addIntegratedCircuitToRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addRecipeMT;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.convertCircuitRecipeItems;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.packageCircuitRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.reduplicateRecipe;
import static gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;

import java.util.HashSet;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GTRecipe;

public final class SpaceAssemblerRecipeGenerator {

    private SpaceAssemblerRecipeGenerator() {}

    public static void load() {
        HashSet<TST_ItemID> generateRecipeOutputs = new HashSet<>();
        generateRecipeOutputs.add(TST_ItemID.createNoNBT(getModItem("OpenComputers", "item", 1, 39)));
        generateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Optically_Perfected_CPU.get(1)));
        generateRecipeOutputs.add(TST_ItemID.createNoNBT(ItemList.Optically_Compatible_Memory.get(1)));
        for (GTRecipe aRecipe : spaceAssemblerRecipes.getAllRecipes()) {
            if (!generateRecipeOutputs.contains(TST_ItemID.createNoNBT(aRecipe.mOutputs[0]))) continue;

            GTRecipe generatedRecipe = packageCircuitRecipe(aRecipe);
            generatedRecipe = convertCircuitRecipeItems(generatedRecipe);
            generatedRecipe = reduplicateRecipe(generatedRecipe, 3, 3, 4, 4, 1, 3);
            addRecipeMT(addIntegratedCircuitToRecipe(generatedRecipe, 15));
        }
    }

}
