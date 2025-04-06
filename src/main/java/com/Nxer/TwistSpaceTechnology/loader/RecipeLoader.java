package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MegaBrickedBlastFurnace;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.commonRecipe.ShapedCraftRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.commonRecipe.SimpleFurnaceFuelPool;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.CardiganRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.CosmicProcessorCircuitRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.LapotronChipRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.SingleBlockRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item.SingleItemRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.GTCMMachineRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.ModularHatchesRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.SingleMachineRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.TSTBufferedEnergyHatchRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine.TSTSolidifierHatchRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.AssemblyLineWithoutResearchRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BOTRecipe.BOTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BOTRecipe.RuneEngraverRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BloodyHellRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.CircuitAssemblyLineWithoutImprintRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.CokingFactoryRecipePool;
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
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.NeutronActivatorWithEURecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.PreciseHighEnergyPhotonicQuantumMasterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.RapidHeatExchangeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StarKernelForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StellarForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StellarMaterialSiphonRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CentrifugeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ChemicalReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CircuitAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CompressorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.DTPFRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.DistillationRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ExtractorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.FluidHeaterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.FluidSolidifierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.FusionReactorRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.MixerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.NanoForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ParticleColliderRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.QFTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.SpaceAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.DragonBloodRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.LanthanidesRecipePool;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic.StaticMiscs;
import com.Nxer.TwistSpaceTechnology.system.ExtremeCrafting.ExtremeCraftRecipeHandler;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_NormalProcessing;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCResearches;

public class RecipeLoader {

    public static void loadRecipes() {

        // TODO Split GTCMMachineRecipes
        // Item Recipes
        SingleItemRecipes.loadRecipes();
        SingleBlockRecipes.loadRecipes();
        CosmicProcessorCircuitRecipes.loadRecipes();
        LapotronChipRecipes.loadRecipes();

        // Machine Recipes
        SingleMachineRecipes.loadRecipes();
        TSTBufferedEnergyHatchRecipes.loadRecipes();
        ModularHatchesRecipes.loadRecipes();

        TSTSolidifierHatchRecipes.loadRecipes();
        GTCMMachineRecipes.loadRecipes();
        CardiganRecipes.loadRecipes();

        // Original GTNH RecipeMap
        ChemicalReactorRecipePool.loadRecipes();
        CircuitAssemblerRecipePool.loadRecipes();
        DistillationRecipePool.loadRecipes();
        ExtractorRecipePool.loadRecipes();
        CompressorRecipePool.loadRecipes();
        LanthanidesRecipePool.loadRecipes();
        CentrifugeRecipePool.loadRecipes();
        ShapedCraftRecipePool.loadRecipes();
        MixerRecipePool.loadRecipes();
        QFTRecipePool.loadRecipes();
        NanoForgeRecipePool.loadRecipes();
        FluidHeaterRecipePool.loadRecipes();
        ParticleColliderRecipePool.loadRecipes();
        FusionReactorRecipePool.loadRecipes();
        SpaceAssemblerRecipePool.loadRecipes();
        TCRecipePool.loadRecipes();
        FluidSolidifierRecipePool.loadRecipes();
        DragonBloodRecipe.loadRecipes();
        DTPFRecipePool.loadRecipes();
        // TST Recipe Map
        IntensifyChemicalDistorterRecipePool.loadRecipes();
        PreciseHighEnergyPhotonicQuantumMasterRecipePool.loadRecipes();
        MiracleTopRecipePool.loadRecipes();
        CrystallineInfinitierRecipePool.loadRecipes();
        DSPRecipePool.loadRecipes();
        MegaUniversalSpaceStationRecipePool.loadRecipes();
        StellarMaterialSiphonRecipePool.loadRecipes();
        AssemblyLineWithoutResearchRecipePool.loadRecipes();
        BOTRecipePool.loadRecipes();
        StarKernelForgeRecipePool.loadRecipes();
        ElvenWorkshopRecipePool.loadRecipes();
        RuneEngraverRecipePool.loadRecipes();
        CokingFactoryRecipePool.loadRecipes();
        StellarForgeRecipePool.loadRecipes();
        HyperSpacetimeTransformerRecipePool.loadRecipes();
        AquaticZoneSimulatorFakeRecipe.loadRecipes();
        NeutronActivatorWithEURecipePool.loadRecipes();
        MassFabricatorGenesisRecipePool.loadRecipes();
        MicroSpaceTimeFabricatorioRecipePool.loadRecipes();
        BloodyHellRecipePool.loadRecipes();
        MegaStoneBreakerRecipePool.loadRecipes();
        IndustrialAlchemyTowerRecipePool.loadRecipes();
        CircuitAssemblyLineWithoutImprintRecipePool.loadRecipes();
        RapidHeatExchangeRecipePool.loadRecipes();
        DeployedNanoCoreRecipePool.loadRecipes();
        SimpleFurnaceFuelPool.loadRecipes();
        TCResearches.register();

        StaticMiscs.init();
        GT_TileEntity_MegaBrickedBlastFurnace.initStatics();

        OP_NormalProcessing.enumOreProcessingRecipes();

        if (Config.Enable_MegaCraftingCenter) {
            new ExtremeCraftRecipeHandler().initECRecipe();
        }

    }

    public static void loadRecipesPostInit() {
        IntensifyChemicalDistorterRecipePool.loadRecipePostInit();
    }

    public static boolean hasLoadedRecipesServerStarted = false;

    public static void loadRecipesServerStarted() {
        if (hasLoadedRecipesServerStarted) {
            TwistSpaceTechnology.LOG.info("Has loaded recipes server started.");
            return;
        }
        hasLoadedRecipesServerStarted = true;

        StellarForgeRecipePool.loadOnServerStarted();
        TreeGrowthSimulatorWithoutToolFakeRecipe.loadRecipes();
        if (Config.Enable_IndustrialMagicMatrix) {
            IndustrialMagicMatrixRecipePool.loadRecipes();
        }
    }

    public static void loadRecipemixin() {
        // new Mode3SimulatorFakeRecipe().loadRecipes();
    }
}
