package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;

public class CircuitAssemblyLineWithoutImprintRecipePool {

    public static void loadRecipes() {
        for (GTRecipe originalRecipe : BartWorksRecipeMaps.circuitAssemblyLineRecipes.getAllRecipes()) {
            if (originalRecipe == null) continue;
            GTRecipeBuilder.builder()
                .itemInputs(originalRecipe.mInputs)
                .fluidInputs(originalRecipe.mFluidInputs)
                .itemOutputs(originalRecipe.mOutputs)
                .eut(originalRecipe.mEUt)
                .duration(originalRecipe.mDuration)
                .addTo(GTCMRecipe.advCircuitAssemblyLineRecipes);
        }
    }
}
