package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import ic2.api.item.IC2Items;

public class CompressorRecipePool {

    public static void loadRecipes() {
        final IRecipeMap Compressor = RecipeMaps.compressorRecipes;

        GTValues.RA.stdBuilder()
            .itemInputs(GTCMItemList.PurpleMagnoliaPetal.get(8))
            .itemOutputs(IC2Items.getItem("plantBall"))
            .noOptimize()
            .eut(2)
            .duration(15 * 20)
            .addTo(Compressor);
    }
}
