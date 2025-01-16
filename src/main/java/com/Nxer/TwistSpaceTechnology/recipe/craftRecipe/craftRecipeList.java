package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.itemRecipeList.LapotronChipLineRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList.PreciseHighEnergyPhotonicQuantumMasterRecipeList;

public class craftRecipeList {

    public static IRecipePool[] craftRecipe = {
        // Item Recipes
        new LapotronChipLineRecipes(),
        // Machine Recipes
        new PreciseHighEnergyPhotonicQuantumMasterRecipeList() };

}
