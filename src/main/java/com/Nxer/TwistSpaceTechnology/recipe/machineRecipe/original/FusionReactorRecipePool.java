package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.core.material.MaterialsElements;

public class FusionReactorRecipePool {

    public static void loadRecipes() {

        // Chromium + Oxygen = Germanium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Chrome.getPlasma(144), Materials.Oxygen.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().GERMANIUM.getPlasma(), 144))
            .eut(RECIPE_UIV)
            .duration(128)
            .metadata(FUSION_THRESHOLD, 1_200_000_000L)
            .addTo(fusionRecipes);

        // Gadolinium + Sodium = Rhenium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gadolinium.getPlasma(144), Materials.Sodium.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().RHENIUM.getPlasma(), 144))
            .eut(RECIPE_UIV)
            .duration(128)
            .metadata(FUSION_THRESHOLD, 1_200_000_000L)
            .addTo(fusionRecipes);

        // Dysprosium + Phosphorus = Thallium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Dysprosium.getPlasma(144), Materials.Phosphorus.getPlasma(1000))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().THALLIUM.getPlasma(), 144))
            .eut(RECIPE_UIV)
            .duration(128)
            .metadata(FUSION_THRESHOLD, 1_200_000_000L)
            .addTo(fusionRecipes);

        // AxonisAlloy + Protomatter = Axonium

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsTST.AxonisAlloy.getMolten(144), Materials.Protomatter.getFluid(1000))
            .fluidOutputs(MaterialsTST.Axonium.getMolten(144))
            .eut(TierEU.RECIPE_UEV)
            .duration(64)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(RecipeMaps.fusionRecipes);

        // Californium + Calcium = Dubnium

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().CALIFORNIUM.getPlasma(), 72),
                Materials.Calcium.getPlasma(72))
            .fluidOutputs(MaterialsTST.Dubnium.getMolten(72))
            .eut(TierEU.RECIPE_UHV)
            .duration(20 * 6)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(RecipeMaps.fusionRecipes);

        // Concentrated UU Matter

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.UUMatter.getFluid(1000000), MaterialsTST.Axonium.getMolten(36))
            .fluidOutputs(MaterialPool.ConcentratedUUMatter.getFluidOrGas(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 30)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(RecipeMaps.fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.UUMatter.getFluid(1000000), MaterialsTST.Axonium.getPlasma(9))
            .fluidOutputs(MaterialPool.ConcentratedUUMatter.getFluidOrGas(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 5)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(RecipeMaps.fusionRecipes);

    }

}
