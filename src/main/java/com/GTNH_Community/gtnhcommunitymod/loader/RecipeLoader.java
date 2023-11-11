package com.GTNH_Community.gtnhcommunitymod.loader;

import com.GTNH_Community.gtnhcommunitymod.recipe.IRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.ChemicalReactorRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.CircuitAssemblerRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.CrystallineInfinitierRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.FluidSolidifierRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.GTCMMachineRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.IntensifyChemicalDistorterRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.MiracleTopRecipePool;
import com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipePool;

public class RecipeLoader {

    public static void loadRecipes() {
        IRecipePool[] recipePools = new IRecipePool[] { new GTCMMachineRecipePool(),
            new IntensifyChemicalDistorterRecipePool(), new ChemicalReactorRecipePool(),
            new PreciseHighEnergyPhotonicQuantumMasterRecipePool(), new CircuitAssemblerRecipePool(),
            new MiracleTopRecipePool(), new FluidSolidifierRecipePool(), new CrystallineInfinitierRecipePool() };

        for (IRecipePool recipePool : recipePools) {
            recipePool.loadRecipes();
        }
    }
}
