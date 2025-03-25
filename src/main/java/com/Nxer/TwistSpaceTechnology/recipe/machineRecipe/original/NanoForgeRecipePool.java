package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static gregtech.api.recipe.RecipeMaps.nanoForgeRecipes;
import static gregtech.api.util.GTRecipeConstants.NANO_FORGE_TIER;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NanoForgeRecipePool {

    public static void loadRecipes() {

        // Cosmic Neutronium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedEntropy, 0, false),
                GregtechItemList.Laser_Lens_Special.get(0),
                Materials.Neutronium.getNanite(1),

                Materials.CosmicNeutronium.getBlocks(16),
                CustomItemList.PicoWafer.get(8),
                ItemRefer.HiC_T5.get(1))
            .fluidInputs(Materials.UUMatter.getFluid(1000000), MaterialsUEVplus.Protomatter.getFluid(2000))
            .itemOutputs(Materials.CosmicNeutronium.getNanite(1))
            .metadata(NANO_FORGE_TIER, 3)
            .eut(RECIPE_MAX)
            .duration(20 * 400)
            .addTo(nanoForgeRecipes);

        // Axonium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.EnergyFluctuationSelfHarmonizer.get(0),
                GTCMItemList.CoreElement.get(0),
                MaterialsTST.Axonium.getBlocks(8),
                GTCMItemList.AnnihilationConstrainer.get(1),
                GTCMItemList.PerfectEngravedEnergyChip.get(4),
                GTCMItemList.InformationHorizonInterventionShell.get(16))
            .fluidInputs(
                Materials.UUMatter.getFluid(2000000),
                MaterialsUEVplus.PhononMedium.getFluid(4000),
                Materials.Infinity.getPlasma(8000))
            .itemOutputs(MaterialsTST.Axonium.getNanite(2))
            .metadata(NANO_FORGE_TIER, 3)
            .eut(RECIPE_MAX)
            .duration(20 * 750)
            .addTo(nanoForgeRecipes);
    }
}
