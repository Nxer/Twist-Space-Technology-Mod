package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_ULV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import net.minecraft.item.ItemStack;
import ic2.api.item.IC2Items;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

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
            .eut(RECIPE_ULV)
            .duration(15*20)
            .addTo(Compressor);
    }
}
