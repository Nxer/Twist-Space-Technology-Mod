package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.NeutronActivatorRecipesWithEU;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.util.GTRecipe;

public class NeutronActivatorWithEURecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        for (GTRecipe recipeOrigin : neutronActivatorRecipes.getAllRecipes()) {
            GTRecipe recipeNew = recipeOrigin.copy();
            int kn = recipeNew.mSpecialValue / 10000;
            recipeNew.mEUt = kn * kn;
            NeutronActivatorRecipesWithEU.addRecipe(recipeNew);
        }

    }
}
