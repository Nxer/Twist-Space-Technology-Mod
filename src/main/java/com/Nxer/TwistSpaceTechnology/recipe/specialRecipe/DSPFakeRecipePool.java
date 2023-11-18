package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import com.Nxer.TwistSpaceTechnology.common.machine.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;

public class DSPFakeRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .noItemOutputs()
            .noFluidInputs()
            .noFluidOutputs()
            .noOptimize()
            .eut(0)
            .duration(128)
            .addTo(GTCMRecipe.instance.DSP_FakeRecipes);

    }
}
