package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MegaBrickedBlastFurnace;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.commonRecipe.ShapedCraftRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.commonRecipe.SimpleFurnaceFuelPool;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.CosmicProcessorCircuitRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.LapotronChipRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.MoldSingularityRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.SingleBlockRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.SingleItemRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.AstralArrayRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.DSPRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.DualInputBufferRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.LegendLaserHatchRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.ModularHatchesRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.ModularizedMachineSystemRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.PreciseHighEnergyPhotonicQuantumMasterRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.SingleMachineRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.TSTBufferedEnergyHatchRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.AssemblyLineWithoutResearchRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BOTRecipe.BOTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BOTRecipe.RuneEngraverRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BloodyHellRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.CrystallineInfinitierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.DSPRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.DeployedNanoCoreRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.ElvenWorkshopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.HyperSpacetimeTransformerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.IndustrialAlchemyTowerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.IndustrialMagicMatrixRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.IntensifyChemicalDistorterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MassFabricatorGenesisRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MegaStoneBreakerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MegaUniversalSpaceStationRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MicroSpaceTimeFabricatorioRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.PreciseHighEnergyPhotonicQuantumMasterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StarKernelForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StellarForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StellarMaterialSiphonRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CentrifugeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ChemicalBathRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ChemicalReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CircuitAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CokingFactoryRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CompressorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.DistillationRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ElectricImplosionCompressorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ExtractorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.FluidHeaterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.FusionReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.MixerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.NanoForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ParticleColliderRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.QFTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.SpaceAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.LanthanidesRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.NeutronActivatorWithEURecipePool;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic.StaticMiscs;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_NormalProcessing;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCResearches;

public class RecipeLoader {

    public static void loadRecipes() {
        TwistSpaceTechnology.LOG.info("GTCMRecipePool loading recipes.");

        /**
         * For the new recipe classification rules
         *
         * @see com.Nxer.TwistSpaceTechnology.recipe.commonRecipe
         *      Crafting table recipes
         * @see com.Nxer.TwistSpaceTechnology.recipe.craftRecipe
         *      Recipes of TST machines, items in assembler, assemblyLine and so on. Among them, machine related
         *      upgrade blocks and items with a large number of upgrades are separately separated from Single ones.
         *      When using Config, do not include everything and only activate the controller for Config.
         * @see com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.SingleBlockRecipes
         *      Recipes of blocks borrowed from GT, TT or block added by TST that have weak correlation with the main
         *      machine host recipes.
         * @see com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original
         *      Recipes added by TST in the original GTNH recipe pool
         * @see com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded
         *      Recipes added by TST in the TST machine recipe pool
         * @see com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe
         *      Recipes like platinum treated line
         */

        IRecipePool[] craftRecipePool = {
            // Item Recipes
            new SingleItemRecipes(), new SingleBlockRecipes(), new CosmicProcessorCircuitRecipes(),
            new LapotronChipRecipes(), new MoldSingularityRecipes(),
            // Machine Recipes
            new SingleMachineRecipes(), new PreciseHighEnergyPhotonicQuantumMasterRecipes(),
            new DualInputBufferRecipes(), new TSTBufferedEnergyHatchRecipes(), new DSPRecipes(),
            new LegendLaserHatchRecipes(), new AstralArrayRecipes(), new ModularizedMachineSystemRecipes(),
            new ModularHatchesRecipes() };

        for (IRecipePool recipePool : craftRecipePool) {
            recipePool.loadRecipes();
        }

        IRecipePool[] machineRecipePools = new IRecipePool[] {
            // Original GTNH RecipeMap
            new ChemicalReactorRecipePool(), new CircuitAssemblerRecipePool(), new DistillationRecipePool(),
            new ExtractorRecipePool(), new CompressorRecipePool(), new LanthanidesRecipePool(),
            new CentrifugeRecipePool(), new ShapedCraftRecipePool(), new MixerRecipePool(), new QFTRecipePool(),
            new NanoForgeRecipePool(), new FluidHeaterRecipePool(), new ParticleColliderRecipePool(),
            new FusionReactorRecipePool(), new SpaceAssemblerRecipePool(), new TCRecipePool(),
            new ElectricImplosionCompressorRecipePool(), new ChemicalBathRecipePool(),
            // TST Recipe Map
            new IntensifyChemicalDistorterRecipePool(), new PreciseHighEnergyPhotonicQuantumMasterRecipePool(),
            new MiracleTopRecipePool(), new CrystallineInfinitierRecipePool(), new DSPRecipePool(),
            new MegaUniversalSpaceStationRecipePool(), new StellarMaterialSiphonRecipePool(),
            new AssemblyLineWithoutResearchRecipePool(), new BOTRecipePool(), new StarKernelForgeRecipePool(),
            new ElvenWorkshopRecipePool(), new RuneEngraverRecipePool(), new CokingFactoryRecipePool(),
            new StellarForgeRecipePool(), new HyperSpacetimeTransformerRecipePool(),
            new AquaticZoneSimulatorFakeRecipe(), new NeutronActivatorWithEURecipePool(),
            new MassFabricatorGenesisRecipePool(), new MicroSpaceTimeFabricatorioRecipePool(),
            new BloodyHellRecipePool(), new MegaStoneBreakerRecipePool(), new DeployedNanoCoreRecipePool(),
            new IndustrialAlchemyTowerRecipePool() };

        for (IRecipePool recipePool : machineRecipePools) {
            recipePool.loadRecipes();
        }

        new SimpleFurnaceFuelPool().loadRecipes();
        new TCResearches().register();
        StaticMiscs.init();
        GT_TileEntity_MegaBrickedBlastFurnace.initStatics();
        new OP_NormalProcessing().enumOreProcessingRecipes();

    }

    public static void loadRecipesPostInit() {
        new IntensifyChemicalDistorterRecipePool().loadRecipePostInit();
    }

    public static boolean hasLoadedRecipesServerStarted = false;

    public static void loadRecipesServerStarted() {
        if (hasLoadedRecipesServerStarted) {
            TwistSpaceTechnology.LOG.info("Has loaded recipes server started.");
            return;
        }
        hasLoadedRecipesServerStarted = true;

        new StellarForgeRecipePool().loadOnServerStarted();
        new TreeGrowthSimulatorWithoutToolFakeRecipe().loadRecipes();
        if (Config.Enable_IndustrialMagicMatrix) {
            new IndustrialMagicMatrixRecipePool().loadRecipes();
        }
    }

    public static void loadRecipemixin() {
        // new Mode3SimulatorFakeRecipe().loadRecipes();
    }
}
