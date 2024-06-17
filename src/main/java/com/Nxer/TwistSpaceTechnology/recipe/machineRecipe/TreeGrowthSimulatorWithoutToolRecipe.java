package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.Mods.Minecraft;
import static gregtech.api.util.GT_ModHandler.getModItem;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;

public class TreeGrowthSimulatorWithoutToolRecipe implements IRecipePool {

    @Override
    public void loadRecipes() {
        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Minecraft.ID, "sapling", 0, 0))
            .itemOutputs(
                getModItem(Minecraft.ID, "log", 5, 0),
                getModItem(Minecraft.ID, "sapling", 1, 0),
                getModItem(Minecraft.ID, "leaves", 2, 0),
                getModItem(Minecraft.ID, "apple", 1, 0))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(20 * 10)
            .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolRecipes);

    }
}
