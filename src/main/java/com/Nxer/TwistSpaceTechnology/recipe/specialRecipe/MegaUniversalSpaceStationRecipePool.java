package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.megaUniversalSpaceStation;
import static gregtech.api.enums.TierEU.*;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_RecipeAdder;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;

public class MegaUniversalSpaceStationRecipePool implements IRecipePool {

    final GT_Recipe.GT_Recipe_Map SpaceAssembler = IG_RecipeAdder.instance.sSpaceAssemblerRecipes;

    @Override
    public void loadRecipes() {
        loadOriginalRecipeForConstruct();
    }

    // this method loads recipe for recipe that not use MUSS but related to MUSS, eg, structure blocks and controller
    // blocks recipes.
    public void loadOriginalRecipeForConstruct() {
        GT_Values.RA.stdBuilder()
            .itemInputs()
            .fluidInputs()
            .itemOutputs(megaUniversalSpaceStation.get(1))
            .noFluidOutputs()
            .noOptimize()
            .specialValue(3)
            .eut(1024 * RECIPE_MAX)
            .duration(20 * 1000)
            .addTo(SpaceAssembler);

    }
}
