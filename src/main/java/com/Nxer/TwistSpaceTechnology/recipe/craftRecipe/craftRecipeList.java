package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.itemRecipeList.LapotronChipLineRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.itemRecipeList.SingleItemRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList.DualInputBufferRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList.LegendLaserHatchRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList.PreciseHighEnergyPhotonicQuantumMasterRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList.SingleBlockRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList.SingleMachineRecipes;

public class craftRecipeList {

    public static IRecipePool[] craftRecipe = {
        // Item Recipes
        new SingleItemRecipes(), new LapotronChipLineRecipes(),
        // Machine Recipes
        new SingleBlockRecipes(), new SingleMachineRecipes(), new PreciseHighEnergyPhotonicQuantumMasterRecipes(),
        new DualInputBufferRecipes(), new LegendLaserHatchRecipes()};

}
