package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_ZPM;

import com.GTNH_Community.gtnhcommunitymod.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class ChemicalReactorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map LCR = GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                Materials.Chrome.getDust(32),
                Materials.Saltpeter.getDust(64),
                Materials.Saltpeter.getDust(64),
                Materials.Saltpeter.getDust(32))
            .fluidInputs(Materials.Oxygen.getGas(96 * 1000), Materials.Water.getFluid(16 * 1000))
            .itemOutputs(
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(48))
            .fluidOutputs(Materials.NitricAcid.getFluid(32 * 1000))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(64)
            .addTo(LCR);

    }
}
