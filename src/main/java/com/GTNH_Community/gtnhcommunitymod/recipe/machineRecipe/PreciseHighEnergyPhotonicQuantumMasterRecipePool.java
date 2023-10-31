package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import static com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList.SpaceWarper;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.TierEU.RECIPE_UMV;

import com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList;
import com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap.GTCMRecipe;
import com.GTNH_Community.gtnhcommunitymod.recipe.RecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class PreciseHighEnergyPhotonicQuantumMasterRecipePool implements RecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map PhC = GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes;

        // Optical SoC
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Chip_Optical.get(1),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Hydrogen.getPlasma(16000))
            .itemOutputs(GTCMItemList.OpticalSOC.get(1), GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 1, 14))
            .fluidOutputs(Materials.Helium.getPlasma(4000))
            .outputChances(10000, 1)
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(128 * 20)
            .addTo(PhC);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                SpaceWarper.get(1),
                ItemList.Circuit_Chip_Optical.get(16),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1))
            .fluidInputs(Materials.Hydrogen.getPlasma(32000))
            .itemOutputs(
                GTCMItemList.OpticalSOC.get(64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 1, 14))
            .fluidOutputs(Materials.Helium.getPlasma(8000))
            .outputChances(10000, 1)
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(256 * 20)
            .addTo(PhC);

    }
}
