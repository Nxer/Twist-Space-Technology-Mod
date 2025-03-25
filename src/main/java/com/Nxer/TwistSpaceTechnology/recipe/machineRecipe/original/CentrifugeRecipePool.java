package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class CentrifugeRecipePool {

    public static void loadRecipes() {
        final IRecipeMap centrifuge = GTPPRecipeMaps.centrifugeNonCellRecipes;
        final IRecipeMap centrifugeSingleBlock = RecipeMaps.centrifugeRecipes;

        // gun powder
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1), Materials.Gunpowder.getDust(6))
            .itemOutputs(Materials.Sulfur.getDust(1), Materials.Coal.getDust(3), Materials.Saltpeter.getDust(2))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(70)
            .addTo(centrifuge);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2), Materials.Gunpowder.getDust(6))
            .itemOutputs(Materials.Sulfur.getDust(1), Materials.Carbon.getDust(3), Materials.Saltpeter.getDust(2))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(70)
            .addTo(centrifuge);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3), Materials.Gunpowder.getDust(6))
            .itemOutputs(Materials.Sulfur.getDust(1), Materials.Charcoal.getDust(3), Materials.Saltpeter.getDust(2))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(70)
            .addTo(centrifuge);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                Materials.Gunpowder.getDust(64),
                Materials.Gunpowder.getDust(64),
                Materials.Gunpowder.getDust(64))
            .itemOutputs(
                Materials.Sulfur.getDust(32),
                Materials.Coal.getDust(64),
                Materials.Coal.getDust(32),
                Materials.Saltpeter.getDust(64))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(30)
            .addTo(centrifuge);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(12),
                Materials.Gunpowder.getDust(64),
                Materials.Gunpowder.getDust(64),
                Materials.Gunpowder.getDust(64))
            .itemOutputs(
                Materials.Sulfur.getDust(32),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(32),
                Materials.Saltpeter.getDust(64))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(30)
            .addTo(centrifuge);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(13),
                Materials.Gunpowder.getDust(64),
                Materials.Gunpowder.getDust(64),
                Materials.Gunpowder.getDust(64))
            .itemOutputs(
                Materials.Sulfur.getDust(32),
                Materials.Charcoal.getDust(64),
                Materials.Charcoal.getDust(32),
                Materials.Saltpeter.getDust(64))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(30)
            .addTo(centrifuge);

    }
}
