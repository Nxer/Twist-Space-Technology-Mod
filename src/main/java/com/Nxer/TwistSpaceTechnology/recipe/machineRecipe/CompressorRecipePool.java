package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import ic2.api.item.IC2Items;

public class CompressorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map Compressor = GT_Recipe.GT_Recipe_Map.sCompressorRecipes;

        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.PurpleMagnoliaPetal.get(8))
            .noFluidInputs()
            .itemOutputs(IC2Items.getItem("plantBall"))
            .noFluidOutputs()
            .noOptimize()
            .eut(2)
            .duration(15 * 20)
            .addTo(Compressor);
    }
}
