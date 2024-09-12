package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.MaterialsElements;

public class FusionReactorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // // debug Recipe
        // GTValues.RA.stdBuilder()
        // .fluidInputs(Materials.Water.getFluid(1), Materials.Lava.getFluid(1))
        // .fluidOutputs(Materials.Water.getGas(1))
        // .specialValue(2000000000)
        // .eut(RECIPE_UMV)
        // .duration(256)
        // .addTo(fusionRecipes);

        // Chromium + Oxygen = Germanium
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Chrome.getPlasma(144), Materials.Oxygen.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().GERMANIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);

        // Gadolinium + Sodium= Rhenium
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gadolinium.getPlasma(144), Materials.Sodium.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().RHENIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);

        // Dysprosium + Phosphorus= Thallium
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Dysprosium.getPlasma(144), Materials.Phosphorus.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().THALLIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);
    }

}
