package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;

public class NanoForgeRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.AnnihilationConstrainer.get(0),
                MaterialsUEVplus.TranscendentMetal.getBlocks(64),
                GTUtility.copyAmountUnsafe(128, ItemList.Circuit_Wafer_SoC2.get(1)))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 16))
            .itemOutputs(MaterialsUEVplus.TranscendentMetal.getNanite(12))
            .specialValue(3)
            .eut(2000000000)
            .duration(20 * 500)
            .addTo(RecipeMaps.nanoForgeRecipes);
    }
}
