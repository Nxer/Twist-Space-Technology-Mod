package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

public class MegaUniversalSpaceStationRecipePool implements IRecipePool {
    @Override
    public void loadRecipes() {
        loadOriginalRecipeForConstruct();
    }
    //this method loads recipe for recipe that not use MUSS but related to MUSS, eg, structure blocks and controller blocks recipes.
    public void loadOriginalRecipeForConstruct(){

    }
}
