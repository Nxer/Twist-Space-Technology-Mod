package com.GTNH_Community.gtnhcommunitymod.loader;

import com.GTNH_Community.gtnhcommunitymod.recipe.RecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.ChemicalReactorRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.CircuitAssemblerRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.GTCMMachineRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.IntensifyChemicalDistorterRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.MiracleTopRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipePool;

public class RecipeLoader {

    public static void loadRecipes() {
        RecipePool[] recipePools = new RecipePool[] { new GTCMMachineRecipePool(),
            new IntensifyChemicalDistorterRecipePool(), new ChemicalReactorRecipePool(),
            new PreciseHighEnergyPhotonicQuantumMasterRecipePool(), new CircuitAssemblerRecipePool(),
            new MiracleTopRecipePool() };

        for (RecipePool recipePool : recipePools) {
            recipePool.loadRecipes();
        }
    }
}
