package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Recipe;

public class AssemblyLineWithoutResearchRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        for (GT_Recipe recipe : RecipeMaps.assemblylineVisualRecipes.getAllRecipes()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(recipe.mInputs)
                .itemOutputs(recipe.mOutputs)
                .fluidInputs(recipe.mFluidInputs)
                .noOptimize()
                .eut(recipe.mEUt)
                .duration(recipe.mDuration)
                .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
        }
    }

}
