package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.ELEMENT;

public class FusionReactorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // Chromium + Oxygen = Germanium
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Chrome.getPlasma(144), Materials.Oxygen.getPlasma(1000))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().GERMANIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);

        // Gadolinium + Sodium= Rhenium
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Gadolinium.getPlasma(144), Materials.Sodium.getPlasma(1000))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().RHENIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);

        // Dysprosium + Phosphorus= Thallium
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Dysprosium.getPlasma(144), Materials.Phosphorus.getPlasma(1000))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().THALLIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);
    }

}
