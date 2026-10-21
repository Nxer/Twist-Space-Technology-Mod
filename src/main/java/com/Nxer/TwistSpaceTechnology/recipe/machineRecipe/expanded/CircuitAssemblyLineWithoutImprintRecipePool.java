package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;

public class CircuitAssemblyLineWithoutImprintRecipePool {

    public static void loadRecipes() {
        for (GTRecipe originalRecipe : BartWorksRecipeMaps.circuitAssemblyLineRecipes.getAllRecipes()) {
            if (originalRecipe == null) continue;
            GTRecipeBuilder builder = GTRecipeBuilder.builder()
                .itemInputs(originalRecipe.mInputs);
            if (originalRecipe.mAltFluidInputs != null) {
                builder.fluidInputs(getFluidInputsWithAlt(originalRecipe));
            } else {
                builder.fluidInputs(originalRecipe.mFluidInputs);
            }
            builder.itemOutputs(originalRecipe.mOutputs)
                .eut(originalRecipe.mEUt)
                .duration(originalRecipe.mDuration)
                .addTo(GTCMRecipe.AdvCircuitAssemblyLineRecipeMap);
        }
    }

    private static Object[] getFluidInputsWithAlt(GTRecipe recipe) {
        FluidStack[] fluidInputs = recipe.mFluidInputs;
        FluidStack[][] altFluidInputs = recipe.mAltFluidInputs;
        Object[] result = new Object[fluidInputs.length];
        for (int i = 0; i < fluidInputs.length; i++) {
            FluidStack[] alternatives = i < altFluidInputs.length ? altFluidInputs[i] : null;
            result[i] = alternatives != null && alternatives.length > 1 ? new SubstituteFluidStack(alternatives)
                : fluidInputs[i];
        }
        return result;
    }
}
