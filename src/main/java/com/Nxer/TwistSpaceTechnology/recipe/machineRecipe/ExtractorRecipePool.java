package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_LV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import magicbees.item.types.CombType;
import magicbees.main.Config;

public class ExtractorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map Extractor = GT_Recipe.GT_Recipe_Map.sExtractorRecipes;

        GT_Values.RA.stdBuilder()
            .itemInputs(Config.combs.getStackForType(CombType.OTHERWORLDLY))
            .noFluidInputs()
            .noItemOutputs()
            .fluidOutputs(MaterialPool.LiquidMana.getFluidOrGas(10))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(30 * 20)
            .addTo(Extractor);
        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.PurpleMagnoliaPetal.get(1))
            .noFluidInputs()
            .noItemOutputs()
            .fluidOutputs(MaterialPool.LiquidMana.getFluidOrGas(5))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(10 * 20)
            .addTo(Extractor);
    }
}
