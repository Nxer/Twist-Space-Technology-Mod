package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ChemicalReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CircuitAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CrystallineInfinitierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.FluidSolidifierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.GTCMMachineRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.IntensifyChemicalDistorterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.MiracleTopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarMaterialSiphonRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.DSPRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.MegaUniversalSpaceStationRecipePool;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic.StaticMiscs;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_NormalProcessing;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values;

public class RecipeLoader {

    public static void loadRecipes() {
        IRecipePool[] recipePools = new IRecipePool[] { new GTCMMachineRecipePool(),
            new IntensifyChemicalDistorterRecipePool(), new ChemicalReactorRecipePool(),
            new PreciseHighEnergyPhotonicQuantumMasterRecipePool(), new CircuitAssemblerRecipePool(),
            new MiracleTopRecipePool(), new FluidSolidifierRecipePool(), new CrystallineInfinitierRecipePool(),
            new DSPRecipePool(), new MegaUniversalSpaceStationRecipePool(), new StellarMaterialSiphonRecipePool() };

        for (IRecipePool recipePool : recipePools) {
            recipePool.loadRecipes();
        }

        StaticMiscs.init();

        OP_NormalProcessing.instance.enumOreProcessingRecipes();
        OP_Values.initOreRecipesInputs();
    }

    public static void loadRecipesPostInit() {
        new IntensifyChemicalDistorterRecipePool().loadRecipePostInit();
    }
}
