package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.core.material.MaterialsElements;

public class FusionReactorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // Chromium + Oxygen = Germanium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Chrome.getPlasma(144), Materials.Oxygen.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().GERMANIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);

        // Gadolinium + Sodium = Rhenium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gadolinium.getPlasma(144), Materials.Sodium.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().RHENIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);

        // Dysprosium + Phosphorus = Thallium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Dysprosium.getPlasma(144), Materials.Phosphorus.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().THALLIUM.getPlasma(), 144))
            .specialValue(2000000000)
            .eut(RECIPE_UIV)
            .duration(128)
            .addTo(fusionRecipes);

        // AxonisAlloy + Protomatter = Axonium

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsTST.AxonisAlloy.getMolten(144), MaterialsUEVplus.Protomatter.getFluid(1000))
            .fluidOutputs(MaterialsTST.Axonium.getMolten(144))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 20)
            .specialValue(2_000_000_000)
            .addTo(RecipeMaps.fusionRecipes);

        // Californium + Calcium = Dubnium

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().CALIFORNIUM.getPlasma(), 72),
                Materials.Calcium.getPlasma(72))
            .fluidOutputs(MaterialsTST.Dubnium.getMolten(72))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 12)
            .specialValue(1_200_000_000)
            .addTo(RecipeMaps.fusionRecipes);

        // Concentrated UU Matter

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.UUMatter.getFluid(1000000), MaterialsTST.Axonium.getMolten(36))
            .fluidOutputs(MaterialPool.ConcentratedUUMatter.getFluidOrGas(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 60)
            .specialValue(2_000_000_000)
            .addTo(RecipeMaps.fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.UUMatter.getFluid(1000000), MaterialsTST.Axonium.getPlasma(9))
            .fluidOutputs(MaterialPool.ConcentratedUUMatter.getFluidOrGas(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 10)
            .specialValue(2_000_000_000)
            .addTo(RecipeMaps.fusionRecipes);
    }

}
