package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import ic2.api.item.IC2Items;

public class CompressorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap Compressor = RecipeMaps.compressorRecipes;

        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.PurpleMagnoliaPetal.get(8))
            .itemOutputs(IC2Items.getItem("plantBall"))
            .noOptimize()
            .eut(2)
            .duration(15 * 20)
            .addTo(Compressor);
    }
}
