package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static gtPlusPlus.core.material.Particle.GRAVITON;
import static gtPlusPlus.core.material.Particle.UNKNOWN;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.Particle;

public class MicroSpaceTimeFabricatorioRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // region Tesseract adv recipe
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.Eternity, 1),
                GTCMItemList.SeedsSpaceTime.get(1),
                Laser_Lens_Special.get(16),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFoil(64))
            .fluidInputs(
                MaterialsUEVplus.ExcitedDTSC.getFluid(1000),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(144 * 64))
            .itemOutputs(setStackSize(ItemList.Tesseract.get(1), 512))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(3000))
            .eut(RECIPE_UXV)
            .duration(20 * 60)
            .addTo(GTCMRecipe.MicroSpaceTimeFabricatorioRecipes);

        // endregion

        // region Quantum Anomaly adv recipe
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                setStackSize(Particle.getBaseParticle(UNKNOWN), 64),
                setStackSize(Particle.getBaseParticle(GRAVITON), 64),
                GTCMItemList.Antimatter.get(1))
            .fluidInputs(Materials.Hydrogen.getPlasma(1000))
            .itemOutputs(Laser_Lens_Special.get(64))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(500))
            .eut(RECIPE_UIV)
            .duration(20 * 60)
            .addTo(GTCMRecipe.MicroSpaceTimeFabricatorioRecipes);

        // endregion

    }
}
