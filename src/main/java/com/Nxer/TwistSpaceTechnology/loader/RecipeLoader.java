package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MegaBrickedBlastFurnace;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.commonRecipe.ShapedCraftRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.AssemblyLineWithoutResearchRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CentrifugeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ChemicalReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CircuitAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CokingFactoryRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CompressorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.CrystallineInfinitierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.DistillationRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ElvenWorkshopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ExtractorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.FluidSolidifierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.GTCMMachineRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.HyperSpacetimeTransformerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.IntensifyChemicalDistorterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.MiracleTopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.MixerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.NanoForgeRecipePoll;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.QFTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.RuneEngraverRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarMaterialSiphonRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.LanthanidesRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.BOTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.DSPRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.MegaUniversalSpaceStationRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TCRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TCResearches;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TSTBufferedEnergyHatchRecipe;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic.StaticMiscs;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_NormalProcessing;

public class RecipeLoader {

    public static void loadRecipes() {
        IRecipePool[] recipePools = new IRecipePool[] { new GTCMMachineRecipePool(),
            new IntensifyChemicalDistorterRecipePool(), new ChemicalReactorRecipePool(),
            new PreciseHighEnergyPhotonicQuantumMasterRecipePool(), new CircuitAssemblerRecipePool(),
            new MiracleTopRecipePool(), new FluidSolidifierRecipePool(), new CrystallineInfinitierRecipePool(),
            new DSPRecipePool(), new MegaUniversalSpaceStationRecipePool(), new StellarMaterialSiphonRecipePool(),
            new DistillationRecipePool(), new ExtractorRecipePool(), new CompressorRecipePool(), new BOTRecipePool(),
            new TCRecipePool(), new ElvenWorkshopRecipePool(), new RuneEngraverRecipePool(),
            new CokingFactoryRecipePool(), new StellarForgeRecipePool(), new LanthanidesRecipePool(),
            new HyperSpacetimeTransformerRecipePool(), new TSTBufferedEnergyHatchRecipe(), new CentrifugeRecipePool(),
            new ShapedCraftRecipePool(), new MixerRecipePool(), new QFTRecipePool(), new NanoForgeRecipePoll() };

        new TCResearches().loadResearches();
        for (IRecipePool recipePool : recipePools) {
            recipePool.loadRecipes();
        }

        StaticMiscs.init();
        GT_TileEntity_MegaBrickedBlastFurnace.initStatics();

        new OP_NormalProcessing().enumOreProcessingRecipes();
        if (Config.EnableRecipeRegistry_IndistinctTentacle) {
            new AssemblyLineWithoutResearchRecipePool().loadRecipes();
        }
    }

    public static void loadRecipesPostInit() {
        new IntensifyChemicalDistorterRecipePool().loadRecipePostInit();
    }

    public static void loadRecipesServerStarted() {
        new StellarForgeRecipePool().loadOnServerStarted();
    }
}
