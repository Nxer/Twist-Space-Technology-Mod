package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AceticAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AmmoniaGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AmmoniumNitrateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AmmoniumNitrateSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BoricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciumFluorideFluid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DilutedHydrochloricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DilutedSulfuricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.FermentationBase;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.FormicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HexafluorosilicicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrochloricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrofluoricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LiquidNitrogen;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MagnesiumChlorideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NaphthenicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Nitrobenzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitrogenDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitrogenMonoxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxalicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhosphoricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhthalicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PotassiumNitrateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumChlorideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumNitrateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfurDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfurTrioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfuricAcid;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerRecipeCache;
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
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosAdvancedMaterialRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosBiologicalRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosExoticMatterRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosFakeRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosIndiumRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosIsaMillRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosKevlarRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosLanthanideRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosNaquadahRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosNetheriteRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosPlatinumRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosPrismaticRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosPurifiedWaterRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRadoxRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosStructureRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosTitaniumRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosTungstenRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.CircuitAssemblyLineWithoutImprintRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.CokingFactoryRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.CrystalEssentiaRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.CrystallineInfinitierRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.DSPRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.DeployedNanoCoreRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.ArtificialGreenHouseFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.ElvenWorkshopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.HyperSpacetimeTransformerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.IndustrialAlchemyTowerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.IndustrialMagicMatrixRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.IntensifyChemicalDistorterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MassFabricatorGenesisRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MegaSolarPanelFactoryRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MegaStoneBreakerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MegaUniversalSpaceStationRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MicroSpaceTimeFabricatorioRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.NeutronActivatorWithEURecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.PreciseHighEnergyPhotonicQuantumMasterRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.RapidHeatExchangeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StarKernelForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StellarForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.StellarMaterialSiphonRecipePool;
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
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.NACRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.NanoForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.ParticleColliderRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.QFTRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.SpaceAssemblerRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.DragonBloodRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe.LanthanidesRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipes.CropsMutationRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.tempRecipe.ElvenWorkshopTransitionRecipes;
import com.Nxer.TwistSpaceTechnology.recipe.tempRecipe.SwelegfyrTransitionRecipes;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic.StaticMiscs;
import com.Nxer.TwistSpaceTechnology.system.ExtremeCrafting.ExtremeCraftRecipeHandler;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Logic;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipePool;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCResearches;

import gregtech.api.enums.Mods;

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
        ElvenWorkshopTransitionRecipes.loadRecipes();
        SwelegfyrTransitionRecipes.loadRecipes();

        // Original GTNH RecipeMap
        ChemicalReactorRecipePool.loadRecipes();
        CircuitAssemblerRecipePool.loadRecipes();
        DistillationRecipePool.loadRecipes();
        ExtractorRecipePool.loadRecipes();
        CompressorRecipePool.loadRecipes();
        LanthanidesRecipePool.loadRecipes();
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
        NACRecipePool.loadRecipes();
        DragonBloodRecipe.loadRecipes();
        DTPFRecipePool.loadRecipes();
        // TST Recipe Map
        IntensifyChemicalDistorterRecipePool.loadRecipes();
        PreciseHighEnergyPhotonicQuantumMasterRecipePool.loadRecipes();
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
        ArtificialGreenHouseFakeRecipe.loadRecipes();
        NeutronActivatorWithEURecipePool.loadRecipes();
        MassFabricatorGenesisRecipePool.loadRecipes();
        MicroSpaceTimeFabricatorioRecipePool.loadRecipes();
        BloodyHellRecipePool.loadRecipes();
        MegaStoneBreakerRecipePool.loadRecipes();
        IndustrialAlchemyTowerRecipePool.loadRecipes();
        RapidHeatExchangeRecipePool.loadRecipes();
        DeployedNanoCoreRecipePool.loadRecipes();
        SimpleFurnaceFuelPool.loadRecipes();
        TCResearches.register();
        CrystalEssentiaRecipePool.loadRecipes();
        MegaSolarPanelFactoryRecipePool.loadRecipes();

        StaticMiscs.init();
        GT_TileEntity_MegaBrickedBlastFurnace.initStatics();

        OP_Logic.initialize();

        if (Config.Enable_MegaCraftingCenter) {
            new ExtremeCraftRecipeHandler().initECRecipe();
        }

        CropsMutationRecipes.loadRecipes();

        BottledChaosRecipeProcessor.initializeRecipeCache(catalog -> {
            BottledChaosTitaniumRecipes.registerRecipeChains(catalog);
            BottledChaosTungstenRecipes.registerRecipeChains(catalog);
            BottledChaosIndiumRecipes.registerRecipeChains(catalog);
            BottledChaosPlatinumRecipes.registerRecipeChains(catalog);
            BottledChaosNetheriteRecipes.registerRecipeChains(catalog);
            BottledChaosPrismaticRecipes.registerRecipeChains(catalog);
            BottledChaosLanthanideRecipes.registerRecipeChains(catalog);
            BottledChaosNaquadahRecipes.registerRecipeChains(catalog);
            BottledChaosBiologicalRecipes.registerRecipeChains(catalog);
            BottledChaosRadoxRecipes.registerRecipeChains(catalog);
            BottledChaosKevlarRecipes.registerRecipeChains(catalog);
            BottledChaosPurifiedWaterRecipes.registerRecipeChains(catalog);
            BottledChaosExoticMatterRecipes.registerRecipeChains(catalog);
            BottledChaosAdvancedMaterialRecipes.registerRecipeChains(catalog);
            BottledChaosIndiumRecipes.registerCombRecipeChains(catalog);
            BottledChaosIsaMillRecipes.registerRecipeChains(catalog);
            BottledChaosPlatinumRecipes.registerCombRecipeChains(catalog);
            catalog.registerDeferredReagents(AmmoniaGas);
            catalog.registerInputSyntheses(
                Nitrobenzene,
                LiquidNitrogen,
                SodiumNitrateDust,
                AmmoniumNitrateDust,
                AmmoniumNitrateSolution,
                AmmoniaGas,
                OxalicAcid,
                HexafluorosilicicAcid,
                AceticAcid,
                BoricAcid,
                FormicAcid,
                FermentationBase);
            catalog.registerOutputDecompositions(
                MagnesiumChlorideDust,
                AmmoniaGas,
                NitrogenMonoxideGas,
                NitrogenDioxideGas,
                SulfurDioxideGas,
                SulfurTrioxideGas,
                CalciumFluorideFluid);
            // Apply reagent boundaries last so every family retains these inputs and recovered acids can cancel.
            catalog.registerExternalReagents(
                NitricAcid,
                HydrofluoricAcid,
                HydrochloricAcid,
                SulfuricAcid,
                PhosphoricAcid,
                PhthalicAcid,
                NaphthenicAcid,
                DilutedSulfuricAcid,
                DilutedHydrochloricAcid,
                SodiumChlorideDust,
                PotassiumNitrateDust);
        });
        BottledChaosFakeRecipes.registerRecipes();
        BottledChaosFakeRecipes.registerCredentialRecipes();
        BottledChaosStructureRecipes.registerRecipes();

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

        CircuitAssemblyLineWithoutImprintRecipePool.loadRecipes();
        StellarForgeRecipePool.loadOnServerStarted();
        TreeGrowthSimulatorWithoutToolFakeRecipe.loadRecipes();
        MiracleTopRecipePool.loadRecipes();
        if (Config.Enable_IndustrialMagicMatrix) {
            new IndustrialMagicMatrixRecipePool().loadRecipes();
        }
        if (Mods.MobsInfo.isModLoaded()) DirectedMobClonerRecipeCache.registerFakeRecipes();
    }

    public static void loadRecipemixin() {}
}
