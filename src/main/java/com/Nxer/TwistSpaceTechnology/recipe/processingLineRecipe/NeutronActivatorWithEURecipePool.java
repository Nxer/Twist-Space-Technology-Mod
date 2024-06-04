package com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.NeutronActivatorRecipesWithEU;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.util.GT_Recipe;

public class NeutronActivatorWithEURecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        for (GT_Recipe recipeOrigin : neutronActivatorRecipes.getAllRecipes()) {
            GT_Recipe recipeNew = recipeOrigin.copy();
            int kn = recipeNew.mSpecialValue / 10000;
            recipeNew.mEUt = kn * kn;
            NeutronActivatorRecipesWithEU.addRecipe(recipeNew);
        }

    }
}
