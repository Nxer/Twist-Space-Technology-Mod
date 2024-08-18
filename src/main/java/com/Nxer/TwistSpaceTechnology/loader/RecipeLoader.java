package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MegaBrickedBlastFurnace;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.commonRecipe.ShapedCraftRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.commonRecipe.SimpleFurnaceFuelPool;
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
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.FluidHeaterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.FluidSolidifierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.FusionReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.GTCMMachineRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.HyperSpacetimeTransformerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.IndustrialMagicMatrixRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.IntensifyChemicalDistorterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.MassFabricatorGenesisRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.MiracleTopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.MixerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ModularHatchesRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.NanoForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.ParticleColliderRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.QFTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.RuneEngraverRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.SpaceAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StarKernelForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarMaterialSiphonRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.LanthanidesRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.NeutronActivatorWithEURecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.BOTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.CosmicProcessorCircuitRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.DSPRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.DragonBloodRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
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
            new ShapedCraftRecipePool(), new MixerRecipePool(), new QFTRecipePool(), new NanoForgeRecipePool(),
            new FluidHeaterRecipePool(), new ParticleColliderRecipePool(), new DragonBloodRecipe(),
            new FusionReactorRecipePool(), new ModularHatchesRecipePool(), new MassFabricatorGenesisRecipePool(),
            new SpaceAssemblerRecipePool(), new CosmicProcessorCircuitRecipePool() };

        new SimpleFurnaceFuelPool().loadRecipes();
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
        if (Config.Enable_BallLightning) {
            new StarKernelForgeRecipePool().loadRecipes();
        }
        if (Config.EnableModularizedMachineSystem) {
            if (Config.EnableLargeNeutronOscillator) {
                new NeutronActivatorWithEURecipePool().loadRecipes();
            }
        }
        if (Config.Enable_IndustrialMagicMatrix) {
            new IndustrialMagicMatrixRecipePool().loadRecipes();
        }
        if (Config.Enable_MegaTreeFarm) {
            new AquaticZoneSimulatorFakeRecipe().loadRecipes();
        }
    }

    public static void loadRecipesPostInit() {
        new IntensifyChemicalDistorterRecipePool().loadRecipePostInit();
        new IndustrialMagicMatrixRecipePool().loadRecipes();
    }

    public static void loadRecipesServerStarted() {
        new StellarForgeRecipePool().loadOnServerStarted();
    }

    public static void loadRecipeMixINGTPP() {
        new TreeGrowthSimulatorWithoutToolFakeRecipe().loadRecipes();
    }
}
