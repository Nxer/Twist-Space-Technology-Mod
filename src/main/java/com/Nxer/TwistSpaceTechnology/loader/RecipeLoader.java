package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ChemicalReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CircuitAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CompressorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CrystallineInfinitierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.DistillationRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ExtractorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.FluidSolidifierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.GTCMMachineRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.IntensifyChemicalDistorterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.MiracleTopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.BOTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.DSPRecipePool;
public class RecipeLoader {

    public static void loadRecipes() {
        IRecipePool[] recipePools = new IRecipePool[] { new GTCMMachineRecipePool(),
            new IntensifyChemicalDistorterRecipePool(), new ChemicalReactorRecipePool(),
            new PreciseHighEnergyPhotonicQuantumMasterRecipePool(), new CircuitAssemblerRecipePool(),
            new MiracleTopRecipePool(), new FluidSolidifierRecipePool(), new CrystallineInfinitierRecipePool(),
            new DSPRecipePool(), new DistillationRecipePool(), new ExtractorRecipePool(),new CompressorRecipePool(),new BOTRecipePool()};

        for (IRecipePool recipePool : recipePools) {
            recipePool.loadRecipes();
        }
        
    }
}
