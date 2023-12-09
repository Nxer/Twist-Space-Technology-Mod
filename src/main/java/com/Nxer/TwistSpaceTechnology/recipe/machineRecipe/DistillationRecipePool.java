package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_IV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;

public class DistillationRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map DT = GT_Recipe.GT_Recipe_Map.sDistillationRecipes;

        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1000))
            .itemOutputs(GTCMItemList.VoidPollen.get(1))
            .fluidOutputs(MaterialPool.PurifiedMana.getFluidOrGas(800))
            .outputChances(100)
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(200)
            .addTo(DT);
    }
}
