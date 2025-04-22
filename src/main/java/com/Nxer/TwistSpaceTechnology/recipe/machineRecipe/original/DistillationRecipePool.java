package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.TierEU.RECIPE_IV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;

public class DistillationRecipePool {

    public static void loadRecipes() {
        final IRecipeMap DT = RecipeMaps.distillationTowerRecipes;

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1000))
            .itemOutputs(GTCMItemList.VoidPollen.get(1))
            .fluidOutputs(MaterialPool.PurifiedMana.getFluidOrGas(800))
            .outputChances(100)
            .eut(RECIPE_IV)
            .duration(200)
            .addTo(DT);
    }
}
