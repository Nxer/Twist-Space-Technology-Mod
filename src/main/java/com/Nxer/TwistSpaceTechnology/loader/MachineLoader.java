package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_GiantVacuumDryingFurnace;
import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_IndustrialAlchemyTower;
import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_MegaStoneBreaker;
import static com.Nxer.TwistSpaceTechnology.config.Config.ParallelOfParallelController;
import static com.Nxer.TwistSpaceTechnology.config.Config.PowerConsumptionMultiplierOfPowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedMultiplierOfSpeedController;

import net.minecraft.entity.EntityList;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_ElvenWorkshop;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_HyperSpacetimeTransformer;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_LightningSpire;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_HolySeparator;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IndustrialMagicMatrix;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IntensifyChemicalDistorter;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDomainConstructor;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDrivePressureFormer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticMixer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MegaBrickedBlastFurnace;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MegaEggGenerator;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MiracleTop;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MoleculeDeconstructor;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PhysicalFormSwitcher;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_Silksong;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_SpaceScaler;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_StellarMaterialSiphon;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_AdvancedMegaOilCracker;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BallLightning;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BeeEngineer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BiosphereIII;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_CleanRoom;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_Computer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_CoreDeviceOfHumanPowerGenerationFacility;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_DeployedNanoCore;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EyeOfWood;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_GiantVacuumDryingFurnace;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_HephaestusAtelier;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IncompactCyclotron;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IndistinctTentacle;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IndustrialAlchemyTower;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IndustrialMagnetarSeparator;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeCanner;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeIndustrialCokingFactory;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeSteamAlloySmelter;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeSteamForgeHammer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_ManufacturingCenter;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaCraftingCenter;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaMacerator;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaStoneBreaker;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MicroSpaceTimeFabricatorio;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MiracleDoor;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_ProcessingArray;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_Scavenger;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_SpaceApiary;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_StarcoreMiner;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_ThermalEnergyDevourer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_VacuumFilterExtractor;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure.spaceStationModular.TST_MegaUniversalSpaceStation;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_BufferedEnergyHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_InfiniteWirelessDynamoHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_InfiniteWirelessMulti;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_RackComputationMonitor;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_WirelessData_input;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_WirelessData_output;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_Air;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_DualInput;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_Solidify;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_UncertaintyDebug;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Pipe_EnergySmart;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_BloodOrbHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_ManaHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_DimensionallyTranscendentMatterPlasmaForgePrototypeMK2;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_IndistinctTentaclePrototypeMK2;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_LargeNeutronOscillator;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_MassFabricatorGenesis;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.AdvExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.ExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.PerfectExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.OverclockControllers.StaticOverclockController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.DynamicParallelController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.StaticParallelController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.PowerConsumptionControllers.StaticPowerConsumptionController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers.DynamicSpeedController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers.StaticSpeedController;
import com.Nxer.TwistSpaceTechnology.common.ship.Ship;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.machines.TST_CircuitConverter;
import com.Nxer.TwistSpaceTechnology.system.Disassembler.TST_Disassembler;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_ArtificialStar;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPLauncher;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPReceiver;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_StrangeMatterAggregator;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.machines.TST_OreProcessingFactory;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

@SuppressWarnings("deprecation") // ignore deprecation for TextLocalization
public final class MachineLoader {

    // meta id 19029 has been assigned for AstralComputingArray
    // NuclearReactor = new TST_NuclearReactor(19029, "nucleareactor", "nuclear reactor").getStackForm(1);
    // GTCMItemList.NuclearReactor.set(IndistinctTentacle); // emm 应该是之前测试IndistinctTentacle用的

    public static void loadMachines() {

        if (Config.activateMegaSpaceStation) EntityList.addMapping(Ship.class, "Ship", 114);

        // test
        // new Test_ModularizedMachine(19000, "TestMachine", "TestMachine");

        // region multi Machine controller

        GTCMItemList.IntensifyChemicalDistorter.set(
            new GT_TileEntity_IntensifyChemicalDistorter(
                19001,
                "NameIntensifyChemicalDistorter",
                TextLocalization.NameIntensifyChemicalDistorter));

        GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.set(
            new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(
                19002,
                "NamePreciseHighEnergyPhotonicQuantumMaster",
                TextLocalization.NamePreciseHighEnergyPhotonicQuantumMaster));

        GTCMItemList.MiracleTop
            .set(new GT_TileEntity_MiracleTop(19003, TextLocalization.NameMiracleTop, TextLocalization.NameMiracleTop));

        GTCMItemList.MagneticDrivePressureFormer.set(
            new GT_TileEntity_MagneticDrivePressureFormer(
                19004,
                "NameMagneticDrivePressureFormer",
                TextLocalization.NameMagneticDrivePressureFormer));

        GTCMItemList.PhysicalFormSwitcher.set(
            new GT_TileEntity_PhysicalFormSwitcher(
                19005,
                "NamePhysicalFormSwitcher",
                TextLocalization.NamePhysicalFormSwitcher));

        GTCMItemList.MagneticMixer
            .set(new GT_TileEntity_MagneticMixer(19006, "NameMagneticMixer", TextLocalization.NameMagneticMixer));

        GTCMItemList.MagneticDomainConstructor.set(
            new GT_TileEntity_MagneticDomainConstructor(
                19007,
                "NameMagneticDomainConstructor",
                TextLocalization.NameMagneticDomainConstructor));

        GTCMItemList.Silksong.set(new GT_TileEntity_Silksong(19008, "NameSilksong", TextLocalization.NameSilksong));

        GTCMItemList.HolySeparator
            .set(new GT_TileEntity_HolySeparator(19009, "NameHolySeparator", TextLocalization.NameHolySeparator));

        GTCMItemList.SpaceScaler
            .set(new GT_TileEntity_SpaceScaler(19010, "NameSpaceScaler", TextLocalization.NameSpaceScaler));

        GTCMItemList.MoleculeDeconstructor.set(
            new GT_TileEntity_MoleculeDeconstructor(
                19011,
                "NameMoleculeDeconstructor",
                TextLocalization.NameMoleculeDeconstructor));

        GTCMItemList.CrystallineInfinitier.set(
            new GTCM_CrystallineInfinitier(
                19012,
                "NameCrystallineInfinitier",
                TextLocalization.NameCrystallineInfinitier));

        GTCMItemList.DSPLauncher.set(new TST_DSPLauncher(19013, "NameDSPLauncher", TextLocalization.NameDSPLauncher));

        GTCMItemList.DSPReceiver.set(new TST_DSPReceiver(19014, "NameDSPReceiver", TextLocalization.NameDSPReceiver));

        GTCMItemList.ArtificialStar
            .set(new TST_ArtificialStar(19015, "NameArtificialStar", TextLocalization.NameArtificialStar));

        GTCMItemList.MiracleDoor.set(new TST_MiracleDoor(19016, "NameMiracleDoor", TextLocalization.NameMiracleDoor));

        GTCMItemList.OreProcessingFactory.set(
            new TST_OreProcessingFactory(19017, "NameOreProcessingFactory", TextLocalization.NameOreProcessingFactory));

        // Space Station Systems
        if (Config.activateMegaSpaceStation) {
            GTCMItemList.megaUniversalSpaceStation.set(
                new TST_MegaUniversalSpaceStation(
                    19018,
                    "NameMegaUniversalSpaceStation",
                    TextLocalization.NameMegaUniversalSpaceStation));
            GTCMItemList.StellarMaterialSiphon.set(
                new GT_TileEntity_StellarMaterialSiphon(
                    19019,
                    "NameStellarMaterialSiphon",
                    TextLocalization.NameStellarMaterialSiphon));
        }

        GTCMItemList.CircuitConverter
            .set(new TST_CircuitConverter(19020, "NameCircuitConverter", TextLocalization.NameCircuitConverter));

        GTCMItemList.LargeIndustrialCokingFactory.set(
            new TST_LargeIndustrialCokingFactory(
                19021,
                "NameLargeIndustrialCokingFactory",
                TextLocalization.NameLargeIndustrialCokingFactory));

        GTCMItemList.ElvenWorkshop
            .set(new GTCM_ElvenWorkshop(19500, "NameElvenWorkshop", TextLocalization.NameElvenWorkshop));

        GTCMItemList.HyperSpacetimeTransformer.set(
            new GTCM_HyperSpacetimeTransformer(
                19501,
                "NameHyperSpacetimeTransformer",
                TextLocalization.NameHyperSpacetimeTransformer));

        GTCMItemList.MegaBrickedBlastFurnace.set(
            new GT_TileEntity_MegaBrickedBlastFurnace(
                19022,
                "NameMegaBrickedBlastFurnace",
                TextLocalization.NameMegaBrickedBlastFurnace));

        GTCMItemList.Scavenger.set(new TST_Scavenger(19023, "NameScavenger", TextLocalization.NameScavenger));

        GTCMItemList.superCleanRoom
            .set(new TST_CleanRoom(19024, "NameTSTcleanroom", TextLocalization.NamesuperCleanRoom));

        GTCMItemList.BiosphereIII
            .set(new TST_BiosphereIII(19025, "nameBiosphereIII", TextLocalization.NameBiosphereIII));

        GTCMItemList.MegaEggGenerator.set(
            new GT_TileEntity_MegaEggGenerator(19026, "NameMegaEggGenerator", TextLocalization.NameMegaEggGenerator));

        GTCMItemList.AdvancedMegaOilCracker.set(
            new TST_AdvancedMegaOilCracker(
                19027,
                "NameAdvancedMegaOilCracker",
                TextLocalization.NameAdvancedMegaOilCracker));

        GTCMItemList.IndistinctTentacle
            .set(new TST_IndistinctTentacle(19028, "NameIndistinctTentacle", TextLocalization.NameIndistinctTentacle));

        GTCMItemList.AstralComputingArray
            .set(new TST_Computer(19029, "NameAstralComputingArray", TextLocalization.NameAstralComputingArray));

        GTCMItemList.ThermalEnergyDevourer.set(
            new TST_ThermalEnergyDevourer(
                19030,
                "NameThermalEnergyDevourer",
                TextLocalization.NameThermalEnergyDevourer));

        GTCMItemList.VacuumFilterExtractor.set(
            new TST_VacuumFilterExtractor(
                19031,
                "NameVacuumFilterExtractor",
                TextLocalization.NameVacuumFilterExtractor));

        GTCMItemList.LargeSteamForgeHammer.set(
            new TST_LargeSteamForgeHammer(
                19032,
                "NameLargeSteamForgeHammer",
                TextLocalization.NameLargeSteamForgeHammer));

        GTCMItemList.LargeSteamAlloySmelter.set(
            new TST_LargeSteamAlloySmelter(
                19033,
                "NameLargeSteamAlloySmelter",
                TextLocalization.NameLargeSteamAlloySmelter));

        GTCMItemList.EyeOfWood.set(new TST_EyeOfWood(19034, "NameEyeOfWood", TextLocalization.NameEyeOfWood));

        GTCMItemList.BeeEngineer.set(new TST_BeeEngineer(19035, "NameBeeEngineer", TextLocalization.NameBeeEngineer));

        GTCMItemList.MegaMacerator
            .set(new TST_MegaMacerator(19036, "NameMegaMacerator", TextLocalization.NameMegaMacerator));

        GTCMItemList.HephaestusAtelier
            .set(new TST_HephaestusAtelier(19037, "NameHephaestusAtelier", TextLocalization.NameHephaestusAtelier));

        if (Config.Enable_DeployedNanoCore) {
            GTCMItemList.DeployedNanoCore
                .set(new TST_DeployedNanoCore(19038, "NameDeployedNanoCore", TextLocalization.NameDeployedNanoCore));
        }

        if (Config.Enable_CoreDeviceOfHumanPowerGenerationFacility) {
            GTCMItemList.CoreDeviceOfHumanPowerGenerationFacility.set(
                new TST_CoreDeviceOfHumanPowerGenerationFacility(
                    19039,
                    "NameCoreDeviceOfHumanPowerGenerationFacility",
                    TextEnums.NameCoreDeviceOfHumanPowerGenerationFacility.toString()));
        }

        if (Config.Enable_StarcoreMiner) {
            GTCMItemList.StarcoreMiner.set(
                new TST_StarcoreMiner(
                    19040,
                    "NameStarcoreMiner",
                    // #tr NameStarcoreMiner
                    // # Starcore Miner
                    // #zh_CN 星核钻机
                    TextEnums.tr("NameStarcoreMiner")));
        }

        if (Config.Enable_Disassembler) {
            GTCMItemList.Disassembler.set(
                new TST_Disassembler(
                    19041,
                    // #tr NameTSTDisassembler
                    // # TST Large Disassembler
                    // #zh_CN TST大型拆解机
                    "NameTSTDisassembler",
                    TextEnums.tr("NameTSTDisassembler")));
        }

        if (Config.Enable_BallLightning) {
            GTCMItemList.BallLightning
                .set(new TST_BallLightning(19046, "NameBallLightning", TextLocalization.NameBallLightning));
        }

        if (Config.Enable_IndustrialMagicMatrix) {
            GTCMItemList.IndustrialMagicMatrix.set(
                new GT_TileEntity_IndustrialMagicMatrix(
                    19047,
                    "IndustrialMagicMatrix",
                    // #tr NameIndustrialMagicMatrix
                    // # Industrial Magic Matrix
                    // #zh_CN §0工业注魔矩阵
                    TextEnums.tr("NameIndustrialMagicMatrix")));
        }

        if (Config.Enable_LargeCanner) {
            GTCMItemList.LargeCanner.set(
                new TST_LargeCanner(
                    19048,
                    "NameLargeCanner",
                    // #tr NameLargeCanner
                    // # Large Canner
                    // #zh_CN 大型装罐机
                    TextEnums.tr("NameLargeCanner")));
        }

        GTCMItemList.BigBroArray.set(new TST_BigBroArray(19049, "BigBroArray.name", TextEnums.tr("BigBroArray.name")));

        if (Config.Enable_IndustrialMagnetarSeparator) {
            GTCMItemList.IndustrialMagnetarSeparator.set(
                new TST_IndustrialMagnetarSeparator(
                    19050,
                    "NameIndustrialMagnetarSeparator",
                    // #tr NameIndustrialMagnetarSeparator
                    // # Industrial Magnetar Separator
                    // #zh_CN 工业电磁离析机
                    TextEnums.tr("NameIndustrialMagnetarSeparator")));
        }

        if (Config.Enable_MegaTreeFarm) {
            GTCMItemList.MegaTreeFarm.set(
                new TST_MegaTreeFarm(
                    19051,
                    "NameMegaTreeFarm",
                    // #tr NameMegaTreeFarm
                    // # Eco-Sphere Growth Simulator
                    // #zh_CN 拟似生态圈
                    TextEnums.tr("NameMegaTreeFarm")));
        }

        GTCMItemList.ExtremeCraftCenter
            .set(new TST_MegaCraftingCenter(19052, "NameExtremeCraftCenter", TextEnums.tr("NameExtremeCraftCenter")));

        if (Config.Enable_LightningSpire) {
            GTCMItemList.LightningSpire.set(
                new GTCM_LightningSpire(
                    19053,
                    "NameLightningSpire",
                    // #tr NameLightningSpire
                    // # Lightning Spire
                    // #zh_CN 闪电尖塔
                    TextEnums.tr("NameLightningSpire")));
        }

        if (Config.EnableModularizedMachineSystem) {
            if (Config.EnableDimensionallyTranscendentMatterPlasmaForgePrototypeMK2) {
                // #tr NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2
                // # Dimensionally Transcendent Matter Plasma Forge Prototype MK-II
                // #zh_CN 超维度物质等离子锻炉原型机MK-II
                GTCMItemList.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2.set(
                    new MM_DimensionallyTranscendentMatterPlasmaForgePrototypeMK2(
                        19054,
                        "NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2",
                        TextEnums.tr("NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2")));
            }

            if (Config.EnableLargeNeutronOscillator) {
                // #tr NameLargeNeutronOscillator
                // # Large Neutron Oscillator
                // #zh_CN 大型中子振荡器
                GTCMItemList.LargeNeutronOscillator.set(
                    new MM_LargeNeutronOscillator(
                        19055,
                        "NameLargeNeutronOscillator",
                        TextEnums.tr("NameLargeNeutronOscillator")));
            }

            if (Config.EnableRecipeRegistry_IndistinctTentacle) {
                // #tr NameIndistinctTentaclePrototypeMK2
                // # {\DARK_GRAY}{\BOLD}Indistinct Tentacle {\RESET}Prototype MK-II
                // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}原型机MK-II
                GTCMItemList.IndistinctTentaclePrototypeMK2.set(
                    new MM_IndistinctTentaclePrototypeMK2(
                        19056,
                        "NameIndistinctTentaclePrototypeMK2",
                        TextEnums.tr("NameIndistinctTentaclePrototypeMK2")));
            }

            // #tr NameMassFabricatorGenesis
            // # Mass Fabricator : Genesis
            // #zh_CN 质量发生器 : 创世纪
            GTCMItemList.MassFabricatorGenesis.set(
                new MM_MassFabricatorGenesis(
                    19057,
                    "NameMassFabricatorGenesis",
                    TextEnums.tr("NameMassFabricatorGenesis")));

        }

        if (Config.Enable_IncompactCyclotron) {
            // #tr NameIncompactCyclotron
            // # PULSAR - Incompact Cyclotron
            // #zh_CN PULSAR - 非紧凑式回旋加速器
            GTCMItemList.IncompactCyclotron.set(
                new TST_IncompactCyclotron(19058, "NameIncompactCyclotron", TextEnums.tr("NameIncompactCyclotron")));
        }

        if (Config.EnableModularizedMachineSystem) {
            // #tr NameStrangeMatterAggregator
            // # Strange Matter Aggregator
            // #zh_CN 奇异物质聚合器
            GTCMItemList.StrangeMatterAggregator.set(
                new TST_StrangeMatterAggregator(
                    19059,
                    "NameStrangeMatterAggregator",
                    TextEnums.tr("NameStrangeMatterAggregator")));

        }

        // #tr NameMicroSpaceTimeFabricatorio
        // # Micro SpaceTime Fabricatorio
        // #zh_CN 微型时空发生器
        GTCMItemList.MicroSpaceTimeFabricatorio.set(
            new TST_MicroSpaceTimeFabricatorio(
                19060,
                "NameMicroSpaceTimeFabricatorio",
                TextEnums.tr("NameMicroSpaceTimeFabricatorio")));

        if (Config.Enable_BloodHell) {
            // #tr NameBloodyHell
            // # Bloody Hell
            // #zh_CN 血狱
            GTCMItemList.BloodyHell.set(new TST_BloodyHell(19061, "NameBloodyHell", TextEnums.tr("NameBloodyHell")));

            if (Config.Enable_BloodHatch) {
                // #tr NameBloodOrbHatch
                // # Blood Hatch
                // #zh_CN 血液仓
                GTCMItemList.BloodOrbHatch
                    .set(new TST_BloodOrbHatch(18846, "NameBloodOrbHatch", TextEnums.tr("NameBloodOrbHatch"), 4));
            }
        }

        if (Enable_MegaStoneBreaker) {
            // #tr NameMegaStoneBreaker
            // # Silicon Rock Synthesizer
            // #zh_CN 硅岩制造机
            GTCMItemList.MegaStoneBreaker
                .set(new TST_MegaStoneBreaker(19062, "NameMegaStoneBreaker", TextEnums.tr("NameMegaStoneBreaker")));
        }

        // #tr NameManufacturingCenter
        // # Manufacturing Center
        // #zh_CN 加工中心
        GTCMItemList.ManufacturingCenter.set(
            new TST_ManufacturingCenter(19063, "NameManufacturingCenter", TextEnums.tr("NameManufacturingCenter")));

        if (Enable_IndustrialAlchemyTower) {
            GTCMItemList.IndustrialAlchemyTower.set(
                new TST_IndustrialAlchemyTower(
                    // #tr NameIndustrialAlchemyTower
                    // # Industrial Alchemy Tower
                    // #zh_CN 工业炼金塔
                    19064,
                    "IndustrialAlchemyTower",
                    TextEnums.tr("NameIndustrialAlchemyTower")));
        }

        // #tr NameGiantVacuumDryingFurnace
        // # Giant Vacuum Drying Furnace
        // #zh_CN 巨型真空干燥炉
        if (Enable_GiantVacuumDryingFurnace) {
            GTCMItemList.GiantVacuumDryingFurnace.set(
                new TST_GiantVacuumDryingFurnace(
                    19065,
                    "GiantVacuumDryingFurnace",
                    TextEnums.tr("NameGiantVacuumDryingFurnace")));
        }

        if (Config.Enable_ProcessingArray) {
            // #tr NameProcessingArray
            // # TST Processing Array
            // #zh_CN TST处理阵列
            GTCMItemList.ProcessingArray
                .set(new TST_ProcessingArray(19066, "NameProcessingArray", TextEnums.tr("NameProcessingArray")));
        }
        // endregion

        // region Single block Machine

        GTCMItemList.InfiniteAirHatch.set(
            new GT_MetaTileEntity_Hatch_Air(18999, "NameInfiniteAirHatch", TextLocalization.NameInfiniteAirHatch, 9));

        GTCMItemList.InfiniteWirelessDynamoHatch.set(
            new GT_Hatch_InfiniteWirelessDynamoHatch(
                18998,
                "NameInfiniteWirelessDynamoHatch",
                TextLocalization.NameInfiniteWirelessDynamoHatch,
                14));

        GTCMItemList.ManaHatch.set(new TST_ManaHatch(18979, "NameManaHatch", TextLocalization.NameManaHatch, 9));

        // region Dual Input Buffer
        GTCMItemList.DualInputBuffer_IV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18980,
                "NameDualInputBuffer_IV",
                TextLocalization.NameDualInputBuffer_IV,
                5));

        GTCMItemList.DualInputBuffer_LuV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18981,
                "NameDualInputBuffer_LuV",
                TextLocalization.NameDualInputBuffer_LuV,
                6));

        GTCMItemList.DualInputBuffer_ZPM.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18982,
                "NameDualInputBuffer_ZPM",
                TextLocalization.NameDualInputBuffer_ZPM,
                7));

        GTCMItemList.DualInputBuffer_UV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18983,
                "NameDualInputBuffer_UV",
                TextLocalization.NameDualInputBuffer_UV,
                8));

        // region buffered energy hatch

        GTCMItemList.BufferedEnergyHatchLV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18984,
                "NameBufferedEnergyHatchLV",
                TstUtils.tr("NameBufferedEnergyHatchLV"),
                1,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchMV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18985,
                "NameBufferedEnergyHatchMV",
                TstUtils.tr("NameBufferedEnergyHatchMV"),
                2,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchHV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18986,
                "NameBufferedEnergyHatchHV",
                TstUtils.tr("NameBufferedEnergyHatchHV"),
                3,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchEV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18987,
                "NameBufferedEnergyHatchEV",
                TstUtils.tr("NameBufferedEnergyHatchEV"),
                4,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchIV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18988,
                "NameBufferedEnergyHatchIV",
                TstUtils.tr("NameBufferedEnergyHatchIV"),
                5,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchLuV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18989,
                "NameBufferedEnergyHatchLuV",
                TstUtils.tr("NameBufferedEnergyHatchLuV"),
                6,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchZPM.set(
            new GT_Hatch_BufferedEnergyHatch(
                18990,
                "NameBufferedEnergyHatchZPM",
                TstUtils.tr("NameBufferedEnergyHatchZPM"),
                7,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18991,
                "NameBufferedEnergyHatchUV",
                TstUtils.tr("NameBufferedEnergyHatchUV"),
                8,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUHV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18992,
                "NameBufferedEnergyHatchUHV",
                TstUtils.tr("NameBufferedEnergyHatchUHV"),
                9,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUEV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18993,
                "NameBufferedEnergyHatchUEV",
                TstUtils.tr("NameBufferedEnergyHatchUEV"),
                10,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUIV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18994,
                "NameBufferedEnergyHatchUIV",
                TstUtils.tr("NameBufferedEnergyHatchUIV"),
                11,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUMV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18995,
                "NameBufferedEnergyHatchUMV",
                TstUtils.tr("NameBufferedEnergyHatchUMV"),
                12,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUXV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18996,
                "NameBufferedEnergyHatchUXV",
                TstUtils.tr("NameBufferedEnergyHatchUXV"),
                13,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchMAX.set(
            new GT_Hatch_BufferedEnergyHatch(
                18997,
                "NameBufferedEnergyHatchMAX",
                TstUtils.tr("NameBufferedEnergyHatchMAX"),
                14,
                16,
                null));

        GTCMItemList.DebugUncertaintyHatch.set(
            new GT_MetaTileEntity_Hatch_UncertaintyDebug(
                18978,
                "NameDebugUncertaintyHatch",
                TextLocalization.NameDebugUncertaintyHatch,
                12));

        GTCMItemList.LaserSmartNode.set(
            new GT_MetaTileEntity_Pipe_EnergySmart(18960, "NameLaserSmartNode", TextLocalization.NameLaserSmartNode));
        // endregion

        GTCMItemList.FackRackHatch.set(
            new GT_Hatch_RackComputationMonitor(
                18959,
                "NameFackRackHatch",
                TextLocalization.NameFackRackHatch,
                12,
                false));

        GTCMItemList.RealRackHatch.set(
            new GT_Hatch_RackComputationMonitor(
                18958,
                "NameRealRackHatch",
                TextLocalization.NameRealRackHatch,
                12,
                true));

        GTCMItemList.WirelessDataInputHatch.set(
            new GT_Hatch_WirelessData_input(
                18957,
                "NameWirelessDataInputHatch",
                TextLocalization.NameWirelessDataInputHatch,
                12));

        GTCMItemList.WirelessDataOutputHatch.set(
            new GT_Hatch_WirelessData_output(
                18956,
                "NameWirelessDataOutputHatch",
                TextLocalization.NameWirelessDataOutputHatch,
                12));

        // #tr NameLegendaryWirelessEnergyHatch
        // # Legendary Wireless Energy Hatch
        // #zh_CN 传奇无线能源仓
        GTCMItemList.LegendaryWirelessEnergyHatch.set(
            new GT_Hatch_InfiniteWirelessMulti(
                18798,
                "NameLegendaryWirelessEnergyHatch",
                TextEnums.tr("NameLegendaryWirelessEnergyHatch"),
                13,
                536870912));

        // #tr NameHarmoniousWirelessEnergyHatch
        // # Harmonious Wireless Energy Hatch
        // #zh_CN 鸿蒙无线能源仓
        GTCMItemList.HarmoniousWirelessEnergyHatch.set(
            new GT_Hatch_InfiniteWirelessMulti(
                18799,
                "NameHarmoniousWirelessEnergyHatch",
                TextEnums.tr("NameHarmoniousWirelessEnergyHatch"),
                14,
                2147483647));

        // #tr NameSolidifyHatchUHV
        // # Solidifier Hatch(UHV)
        // #zh_CN 固化仓(UHV)
        SolidifyHatch_UHV = new GT_MetaTileEntity_Hatch_Solidify(
            18797,
            "NameSolidifyHatchUHV",
            TextEnums.tr("NameSolidifyHatchUHV"),
            9).getStackForm(1);
        GTCMItemList.SolidifyHatch_UHV.set(SolidifyHatch_UHV);

        // #tr NameSolidifyHatchUV
        // # Solidifier Hatch(UV)
        // #zh_CN 固化仓(UV)
        SolidifyHatch_UV = new GT_MetaTileEntity_Hatch_Solidify(
            18796,
            "NameSolidifyHatchUV",
            TextEnums.tr("NameSolidifyHatchUV"),
            8).getStackForm(1);
        GTCMItemList.SolidifyHatch_UV.set(SolidifyHatch_UV);

        // #tr NameSolidifyHatchZPM
        // # Solidifier Hatch(ZPM)
        // #zh_CN 固化仓(ZPM)
        SolidifyHatch_ZPM = new GT_MetaTileEntity_Hatch_Solidify(
            18795,
            "NameSolidifyHatchZPM",
            TextEnums.tr("NameSolidifyHatchZPM"),
            7).getStackForm(1);
        GTCMItemList.SolidifyHatch_ZPM.set(SolidifyHatch_ZPM);

        // #tr NameSolidifyHatchLuV
        // # Solidifier Hatch(LuV)
        // #zh_CN 固化仓(LuV)
        SolidifyHatch_LuV = new GT_MetaTileEntity_Hatch_Solidify(
            18794,
            "NameSolidifyHatchLuV",
            TextEnums.tr("NameSolidifyHatchLuV"),
            6).getStackForm(1);
        GTCMItemList.SolidifyHatch_LuV.set(SolidifyHatch_LuV);

        // #tr NameSolidifyHatchIV
        // # Solidifier Hatch(IV)
        // #zh_CN 固化仓(IV)
        SolidifyHatch_IV = new GT_MetaTileEntity_Hatch_Solidify(
            18793,
            "NameSolidifyHatchIV",
            TextEnums.tr("NameSolidifyHatchIV"),
            5).getStackForm(1);
        GTCMItemList.SolidifyHatch_IV.set(SolidifyHatch_IV);

        // region Modularized Stuff

        if (Config.EnableModularizedMachineSystem) {

            // #tr NameDynamicParallelControllerT1
            // # Dynamic Parallel Controller Module T1
            // #zh_CN 动态并行控制器模块T1
            GTCMItemList.DynamicParallelControllerT1.set(
                new DynamicParallelController(
                    18800,
                    "NameDynamicParallelControllerT1",
                    TextEnums.tr("NameDynamicParallelControllerT1"),
                    7,
                    ParallelOfParallelController[0]));

            // #tr NameDynamicParallelControllerT2
            // # Dynamic Parallel Controller Module T2
            // #zh_CN 动态并行控制器模块T2
            GTCMItemList.DynamicParallelControllerT2.set(
                new DynamicParallelController(
                    18801,
                    "NameDynamicParallelControllerT2",
                    TextEnums.tr("NameDynamicParallelControllerT2"),
                    8,
                    ParallelOfParallelController[1]));

            // #tr NameDynamicParallelControllerT3
            // # Dynamic Parallel Controller Module T3
            // #zh_CN 动态并行控制器模块T3
            GTCMItemList.DynamicParallelControllerT3.set(
                new DynamicParallelController(
                    18802,
                    "NameDynamicParallelControllerT3",
                    TextEnums.tr("NameDynamicParallelControllerT3"),
                    9,
                    ParallelOfParallelController[2]));

            // #tr NameDynamicParallelControllerT4
            // # Dynamic Parallel Controller Module T4
            // #zh_CN 动态并行控制器模块T4
            GTCMItemList.DynamicParallelControllerT4.set(
                new DynamicParallelController(
                    18803,
                    "NameDynamicParallelControllerT4",
                    TextEnums.tr("NameDynamicParallelControllerT4"),
                    10,
                    ParallelOfParallelController[3]));

            // #tr NameDynamicParallelControllerT5
            // # Dynamic Parallel Controller Module T5
            // #zh_CN 动态并行控制器模块T5
            GTCMItemList.DynamicParallelControllerT5.set(
                new DynamicParallelController(
                    18804,
                    "NameDynamicParallelControllerT5",
                    TextEnums.tr("NameDynamicParallelControllerT5"),
                    11,
                    ParallelOfParallelController[4]));

            // #tr NameDynamicParallelControllerT6
            // # Dynamic Parallel Controller Module T6
            // #zh_CN 动态并行控制器模块T6
            GTCMItemList.DynamicParallelControllerT6.set(
                new DynamicParallelController(
                    18805,
                    "NameDynamicParallelControllerT6",
                    TextEnums.tr("NameDynamicParallelControllerT6"),
                    12,
                    ParallelOfParallelController[5]));

            // #tr NameDynamicParallelControllerT7
            // # Dynamic Parallel Controller Module T7
            // #zh_CN 动态并行控制器模块T7
            GTCMItemList.DynamicParallelControllerT7.set(
                new DynamicParallelController(
                    18806,
                    "NameDynamicParallelControllerT7",
                    TextEnums.tr("NameDynamicParallelControllerT7"),
                    13,
                    ParallelOfParallelController[6]));

            // #tr NameDynamicParallelControllerT8
            // # Dynamic Parallel Controller Module T8
            // #zh_CN 动态并行控制器模块T8
            GTCMItemList.DynamicParallelControllerT8.set(
                new DynamicParallelController(
                    18807,
                    "NameDynamicParallelControllerT8",
                    TextEnums.tr("NameDynamicParallelControllerT8"),
                    14,
                    ParallelOfParallelController[7]));

            // #tr NameStaticParallelControllerT1
            // # Static Parallel Controller Module T1
            // #zh_CN 静态并行控制器模块T1
            GTCMItemList.StaticParallelControllerT1.set(
                new StaticParallelController(
                    18808,
                    "NameStaticParallelControllerT1",
                    TextEnums.tr("NameStaticParallelControllerT1"),
                    7,
                    ParallelOfParallelController[0]));

            // #tr NameStaticParallelControllerT2
            // # Static Parallel Controller Module T2
            // #zh_CN 静态并行控制器模块T2
            GTCMItemList.StaticParallelControllerT2.set(
                new StaticParallelController(
                    18809,
                    "NameStaticParallelControllerT2",
                    TextEnums.tr("NameStaticParallelControllerT2"),
                    8,
                    ParallelOfParallelController[1]));

            // #tr NameStaticParallelControllerT3
            // # Static Parallel Controller Module T3
            // #zh_CN 静态并行控制器模块T3
            GTCMItemList.StaticParallelControllerT3.set(
                new StaticParallelController(
                    18810,
                    "NameStaticParallelControllerT3",
                    TextEnums.tr("NameStaticParallelControllerT3"),
                    9,
                    ParallelOfParallelController[2]));

            // #tr NameStaticParallelControllerT4
            // # Static Parallel Controller Module T4
            // #zh_CN 静态并行控制器模块T4
            GTCMItemList.StaticParallelControllerT4.set(
                new StaticParallelController(
                    18811,
                    "NameStaticParallelControllerT4",
                    TextEnums.tr("NameStaticParallelControllerT4"),
                    10,
                    ParallelOfParallelController[3]));

            // #tr NameStaticParallelControllerT5
            // # Static Parallel Controller Module T5
            // #zh_CN 静态并行控制器模块T5
            GTCMItemList.StaticParallelControllerT5.set(
                new StaticParallelController(
                    18812,
                    "NameStaticParallelControllerT5",
                    TextEnums.tr("NameStaticParallelControllerT5"),
                    11,
                    ParallelOfParallelController[4]));

            // #tr NameStaticParallelControllerT6
            // # Static Parallel Controller Module T6
            // #zh_CN 静态并行控制器模块T6
            GTCMItemList.StaticParallelControllerT6.set(
                new StaticParallelController(
                    18813,
                    "NameStaticParallelControllerT6",
                    TextEnums.tr("NameStaticParallelControllerT6"),
                    12,
                    ParallelOfParallelController[5]));

            // #tr NameStaticParallelControllerT7
            // # Static Parallel Controller Module T7
            // #zh_CN 静态并行控制器模块T7
            GTCMItemList.StaticParallelControllerT7.set(
                new StaticParallelController(
                    18814,
                    "NameStaticParallelControllerT7",
                    TextEnums.tr("NameStaticParallelControllerT7"),
                    13,
                    ParallelOfParallelController[6]));

            // #tr NameStaticParallelControllerT8
            // # Static Parallel Controller Module T8
            // #zh_CN 静态并行控制器模块T8
            GTCMItemList.StaticParallelControllerT8.set(
                new StaticParallelController(
                    18815,
                    "NameStaticParallelControllerT8",
                    TextEnums.tr("NameStaticParallelControllerT8"),
                    14,
                    ParallelOfParallelController[7]));

            // #tr NameDynamicSpeedControllerT1
            // # Dynamic Speed Controller Module T1
            // #zh_CN 动态速度控制器模块T1
            GTCMItemList.DynamicSpeedControllerT1.set(
                new DynamicSpeedController(
                    18816,
                    "NameDynamicSpeedControllerT1",
                    TextEnums.tr("NameDynamicSpeedControllerT1"),
                    7,
                    SpeedMultiplierOfSpeedController[0]));

            // #tr NameDynamicSpeedControllerT2
            // # Dynamic Speed Controller Module T2
            // #zh_CN 动态速度控制器模块T2
            GTCMItemList.DynamicSpeedControllerT2.set(
                new DynamicSpeedController(
                    18817,
                    "NameDynamicSpeedControllerT2",
                    TextEnums.tr("NameDynamicSpeedControllerT2"),
                    8,
                    SpeedMultiplierOfSpeedController[1]));

            // #tr NameDynamicSpeedControllerT3
            // # Dynamic Speed Controller Module T3
            // #zh_CN 动态速度控制器模块T3
            GTCMItemList.DynamicSpeedControllerT3.set(
                new DynamicSpeedController(
                    18818,
                    "NameDynamicSpeedControllerT3",
                    TextEnums.tr("NameDynamicSpeedControllerT3"),
                    9,
                    SpeedMultiplierOfSpeedController[2]));

            // #tr NameDynamicSpeedControllerT4
            // # Dynamic Speed Controller Module T4
            // #zh_CN 动态速度控制器模块T4
            GTCMItemList.DynamicSpeedControllerT4.set(
                new DynamicSpeedController(
                    18819,
                    "NameDynamicSpeedControllerT4",
                    TextEnums.tr("NameDynamicSpeedControllerT4"),
                    10,
                    SpeedMultiplierOfSpeedController[3]));

            // #tr NameDynamicSpeedControllerT5
            // # Dynamic Speed Controller Module T5
            // #zh_CN 动态速度控制器模块T5
            GTCMItemList.DynamicSpeedControllerT5.set(
                new DynamicSpeedController(
                    18820,
                    "NameDynamicSpeedControllerT5",
                    TextEnums.tr("NameDynamicSpeedControllerT5"),
                    11,
                    SpeedMultiplierOfSpeedController[4]));

            // #tr NameDynamicSpeedControllerT6
            // # Dynamic Speed Controller Module T6
            // #zh_CN 动态速度控制器模块T6
            GTCMItemList.DynamicSpeedControllerT6.set(
                new DynamicSpeedController(
                    18821,
                    "NameDynamicSpeedControllerT6",
                    TextEnums.tr("NameDynamicSpeedControllerT6"),
                    12,
                    SpeedMultiplierOfSpeedController[5]));

            // #tr NameDynamicSpeedControllerT7
            // # Dynamic Speed Controller Module T7
            // #zh_CN 动态速度控制器模块T7
            GTCMItemList.DynamicSpeedControllerT7.set(
                new DynamicSpeedController(
                    18822,
                    "NameDynamicSpeedControllerT7",
                    TextEnums.tr("NameDynamicSpeedControllerT7"),
                    13,
                    SpeedMultiplierOfSpeedController[6]));

            // #tr NameDynamicSpeedControllerT8
            // # Dynamic Speed Controller Module T8
            // #zh_CN 动态速度控制器模块T8
            GTCMItemList.DynamicSpeedControllerT8.set(
                new DynamicSpeedController(
                    18823,
                    "NameDynamicSpeedControllerT8",
                    TextEnums.tr("NameDynamicSpeedControllerT8"),
                    14,
                    SpeedMultiplierOfSpeedController[7]));

            // #tr NameStaticSpeedControllerT1
            // # Static Speed Controller Module T1
            // #zh_CN 静态速度控制器模块T1
            GTCMItemList.StaticSpeedControllerT1.set(
                new StaticSpeedController(
                    18824,
                    "NameStaticSpeedControllerT1",
                    TextEnums.tr("NameStaticSpeedControllerT1"),
                    7,
                    SpeedMultiplierOfSpeedController[0]));

            // #tr NameStaticSpeedControllerT2
            // # Static Speed Controller Module T2
            // #zh_CN 静态速度控制器模块T2
            GTCMItemList.StaticSpeedControllerT2.set(
                new StaticSpeedController(
                    18825,
                    "NameStaticSpeedControllerT2",
                    TextEnums.tr("NameStaticSpeedControllerT2"),
                    8,
                    SpeedMultiplierOfSpeedController[1]));

            // #tr NameStaticSpeedControllerT3
            // # Static Speed Controller Module T3
            // #zh_CN 静态速度控制器模块T3
            GTCMItemList.StaticSpeedControllerT3.set(
                new StaticSpeedController(
                    18826,
                    "NameStaticSpeedControllerT3",
                    TextEnums.tr("NameStaticSpeedControllerT3"),
                    9,
                    SpeedMultiplierOfSpeedController[2]));

            // #tr NameStaticSpeedControllerT4
            // # Static Speed Controller Module T4
            // #zh_CN 静态速度控制器模块T4
            GTCMItemList.StaticSpeedControllerT4.set(
                new StaticSpeedController(
                    18827,
                    "NameStaticSpeedControllerT4",
                    TextEnums.tr("NameStaticSpeedControllerT4"),
                    10,
                    SpeedMultiplierOfSpeedController[3]));

            // #tr NameStaticSpeedControllerT5
            // # Static Speed Controller Module T5
            // #zh_CN 静态速度控制器模块T5
            GTCMItemList.StaticSpeedControllerT5.set(
                new StaticSpeedController(
                    18828,
                    "NameStaticSpeedControllerT5",
                    TextEnums.tr("NameStaticSpeedControllerT5"),
                    11,
                    SpeedMultiplierOfSpeedController[4]));

            // #tr NameStaticSpeedControllerT6
            // # Static Speed Controller Module T6
            // #zh_CN 静态速度控制器模块T6
            GTCMItemList.StaticSpeedControllerT6.set(
                new StaticSpeedController(
                    18829,
                    "NameStaticSpeedControllerT6",
                    TextEnums.tr("NameStaticSpeedControllerT6"),
                    12,
                    SpeedMultiplierOfSpeedController[5]));

            // #tr NameStaticSpeedControllerT7
            // # Static Speed Controller Module T7
            // #zh_CN 静态速度控制器模块T7
            GTCMItemList.StaticSpeedControllerT7.set(
                new StaticSpeedController(
                    18830,
                    "NameStaticSpeedControllerT7",
                    TextEnums.tr("NameStaticSpeedControllerT7"),
                    13,
                    SpeedMultiplierOfSpeedController[6]));

            // #tr NameStaticSpeedControllerT8
            // # Static Speed Controller Module T8
            // #zh_CN 静态速度控制器模块T8
            GTCMItemList.StaticSpeedControllerT8.set(
                new StaticSpeedController(
                    18831,
                    "NameStaticSpeedControllerT8",
                    TextEnums.tr("NameStaticSpeedControllerT8"),
                    14,
                    SpeedMultiplierOfSpeedController[7]));

            // #tr NameStaticPowerConsumptionControllerT1
            // # Static Power Consumption Controller Module T1
            // #zh_CN 静态耗能控制器模块T1
            GTCMItemList.StaticPowerConsumptionControllerT1.set(
                new StaticPowerConsumptionController(
                    18832,
                    "NameStaticPowerConsumptionControllerT1",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT1"),
                    7,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[0]));

            // #tr NameStaticPowerConsumptionControllerT2
            // # Static Power Consumption Controller Module T2
            // #zh_CN 静态耗能控制器模块T2
            GTCMItemList.StaticPowerConsumptionControllerT2.set(
                new StaticPowerConsumptionController(
                    18833,
                    "NameStaticPowerConsumptionControllerT2",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT2"),
                    8,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[1]));

            // #tr NameStaticPowerConsumptionControllerT3
            // # Static Power Consumption Controller Module T3
            // #zh_CN 静态耗能控制器模块T3
            GTCMItemList.StaticPowerConsumptionControllerT3.set(
                new StaticPowerConsumptionController(
                    18834,
                    "NameStaticPowerConsumptionControllerT3",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT3"),
                    9,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[2]));

            // #tr NameStaticPowerConsumptionControllerT4
            // # Static Power Consumption Controller Module T4
            // #zh_CN 静态耗能控制器模块T4
            GTCMItemList.StaticPowerConsumptionControllerT4.set(
                new StaticPowerConsumptionController(
                    18835,
                    "NameStaticPowerConsumptionControllerT4",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT4"),
                    10,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[3]));

            // #tr NameStaticPowerConsumptionControllerT5
            // # Static Power Consumption Controller Module T5
            // #zh_CN 静态耗能控制器模块T5
            GTCMItemList.StaticPowerConsumptionControllerT5.set(
                new StaticPowerConsumptionController(
                    18836,
                    "NameStaticPowerConsumptionControllerT5",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT5"),
                    11,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[4]));

            // #tr NameStaticPowerConsumptionControllerT6
            // # Static Power Consumption Controller Module T6
            // #zh_CN 静态耗能控制器模块T6
            GTCMItemList.StaticPowerConsumptionControllerT6.set(
                new StaticPowerConsumptionController(
                    18837,
                    "NameStaticPowerConsumptionControllerT6",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT6"),
                    12,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[5]));

            // #tr NameStaticPowerConsumptionControllerT7
            // # Static Power Consumption Controller Module T7
            // #zh_CN 静态耗能控制器模块T7
            GTCMItemList.StaticPowerConsumptionControllerT7.set(
                new StaticPowerConsumptionController(
                    18838,
                    "NameStaticPowerConsumptionControllerT7",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT7"),
                    13,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[6]));

            // #tr NameStaticPowerConsumptionControllerT8
            // # Static Power Consumption Controller Module T8
            // #zh_CN 静态耗能控制器模块T8
            GTCMItemList.StaticPowerConsumptionControllerT8.set(
                new StaticPowerConsumptionController(
                    18839,
                    "NameStaticPowerConsumptionControllerT8",
                    TextEnums.tr("NameStaticPowerConsumptionControllerT8"),
                    14,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[7]));

            // #tr NameLowSpeedPerfectOverclockController
            // # Low Speed Perfect Overclock Controller Module
            // #zh_CN 低速无损超频控制器模块
            GTCMItemList.LowSpeedPerfectOverclockController.set(
                new StaticOverclockController(
                    18840,
                    "NameLowSpeedPerfectOverclockController",
                    TextEnums.tr("NameLowSpeedPerfectOverclockController"),
                    12,
                    2,
                    2));

            // #tr NamePerfectOverclockController
            // # Perfect Overclock Controller Module
            // #zh_CN 无损超频控制器模块
            GTCMItemList.PerfectOverclockController.set(
                new StaticOverclockController(
                    18841,
                    "NamePerfectOverclockController",
                    TextEnums.tr("NamePerfectOverclockController"),
                    13,
                    4,
                    4));

            // #tr NameSingularityPerfectOverclockController
            // # Singularity Perfect Overclock Controller Module
            // #zh_CN 奇点无损超频控制器模块
            GTCMItemList.SingularityPerfectOverclockController.set(
                new StaticOverclockController(
                    18842,
                    "NameSingularityPerfectOverclockController",
                    TextEnums.tr("NameSingularityPerfectOverclockController"),
                    14,
                    8,
                    4));

            /*
             * LV=1
             * MV=2
             * HV=3
             * EV=4
             * IV=5
             * LuV=6
             * ZPM=7 8
             * UV=8 128
             * UHV=9 2,048
             * UEV=10 32,768
             * UIV=11 524,288
             * UMV=12 8,388,608
             * UXV=13 134,217,728
             * MAX=14 2,147,483,648
             */

            // #tr NameExecutionCore
            // # Execution Core Module
            // #zh_CN 执行核心模块
            GTCMItemList.ExecutionCore
                .set(new ExecutionCore(18843, "NameExecutionCore", TextEnums.tr("NameExecutionCore"), 12));

            // #tr NameAdvancedExecutionCore
            // # Advanced Execution Core Module
            // #zh_CN 高级执行核心模块
            GTCMItemList.AdvancedExecutionCore.set(
                new AdvExecutionCore(
                    18844,
                    "NameAdvancedExecutionCore",
                    TextEnums.tr("NameAdvancedExecutionCore"),
                    13));

            // #tr NamePerfectExecutionCore
            // # Perfect Execution Core Module
            // #zh_CN 完美执行核心模块
            GTCMItemList.PerfectExecutionCore.set(
                new PerfectExecutionCore(
                    18845,
                    "NamePerfectExecutionCore",
                    TextEnums.tr("NamePerfectExecutionCore"),
                    14));
        }

        // endregion

    }

    public static void loadMachinePostInit() {
        if (Config.EnableSpaceApiaryModule) {
            GTCMItemList.SpaceApiaryT1.set(
                new TST_SpaceApiary.TST_SpaceApiaryT1(19042, "NameSpaceApiaryT1", TextLocalization.NameSpaceApiaryT1));

            GTCMItemList.SpaceApiaryT2.set(
                new TST_SpaceApiary.TST_SpaceApiaryT2(19043, "NameSpaceApiaryT2", TextLocalization.NameSpaceApiaryT2));

            GTCMItemList.SpaceApiaryT3.set(
                new TST_SpaceApiary.TST_SpaceApiaryT3(19044, "NameSpaceApiaryT3", TextLocalization.NameSpaceApiaryT3));

            GTCMItemList.SpaceApiaryT4.set(
                new TST_SpaceApiary.TST_SpaceApiaryT4(19045, "NameSpaceApiaryT4", TextLocalization.NameSpaceApiaryT4));
        }

    }
}
