package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_GiantVacuumDryingFurnace;
import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_IndustrialAlchemyTower;
import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_MegaStoneBreaker;
import static com.Nxer.TwistSpaceTechnology.config.Config.ParallelOfParallelController;
import static com.Nxer.TwistSpaceTechnology.config.Config.PowerConsumptionMultiplierOfPowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedMultiplierOfSpeedController;

import net.minecraft.entity.EntityList;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_ElvenWorkshop;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_HyperSpacetimeTransformer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_HolySeparator;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IndustrialMagicMatrix;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IntensifyChemicalDistorter;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDomainConstructor;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDrivePressureFormer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticMixer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MegaBrickedBlastFurnace;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MiracleTop;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MoleculeDeconstructor;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PhysicalFormSwitcher;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_Silksong;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_SpaceScaler;
import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.GTCM_LightningSpire;
import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.GT_TileEntity_MegaEggGenerator;
import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.TST_LargeSolarBoiler;
import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.TST_MegaNqReactor;
import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.TST_SteamBasicGenerator;
import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.TST_UniversalGenerator;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_AdvCircuitAssemblyLine;
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
import com.Nxer.TwistSpaceTechnology.common.machine.TST_HyperThermalConvector;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IncompactCyclotron;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IndistinctTentacle;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IndustrialAlchemyTower;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IndustrialMagnetarSeparator;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_InfusionMaterialDispenser;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IntegratedAssemblyMatrix;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeCanner;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeIndustrialCokingFactory;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeSteamAlloySmelter;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeSteamForgeHammer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LaserMeteorMiner;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_ManufacturingCenter;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaCraftingCenter;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaMacerator;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaSolarPanelFactory;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaStoneBreaker;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MicroSpaceTimeFabricatorio;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MiracleDoor;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_NetherInterface;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_PrimordialDisjunctus;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_ProcessingArray;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_Scavenger;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_SkypiercerTower;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_SpaceApiary;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_StarcoreMiner;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_SuperWaterPurifier;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_SwelegfyrBlastFurnace;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_ThermalEnergyDevourer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_VacuumFilterExtractor;
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
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Pipe_EnergySmart_Focusing;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellInputBus;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellInputHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_BloodOrbHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_CircuitImprintHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_ManaHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_PatternAccessHatch;
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
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

public final class MachineLoader {

    // meta id 19029 has been assigned for AstralComputingArray
    // NuclearReactor = new TST_NuclearReactor(19029, "nucleareactor", "nuclear reactor").getStackForm(1);
    // GTCMItemList.NuclearReactor.set(IndistinctTentacle); // emm 应该是之前测试IndistinctTentacle用的

    public static void loadMachines() {

        if (Config.activateMegaSpaceStation) EntityList.addMapping(Ship.class, "Ship", 114);

        // test
        // new Test_ModularizedMachine(19000, "TestMachine", "TestMachine");

        // region multi Machine controller

        // #tr NameIntensifyChemicalDistorter
        // # Intensify Chemical Distorter
        // #zh_CN 深度化学扭曲仪
        GTCMItemList.IntensifyChemicalDistorter.set(
            new GT_TileEntity_IntensifyChemicalDistorter(
                19001,
                "NameIntensifyChemicalDistorter",
                TSTUtils.tr("NameIntensifyChemicalDistorter")));

        // #tr NamePreciseHighEnergyPhotonicQuantumMaster
        // # Precise High-Energy Photonic Quantum Master
        // #zh_CN 精密高能光量子掌控者
        GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.set(
            new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(
                19002,
                "NamePreciseHighEnergyPhotonicQuantumMaster",
                TSTUtils.tr("NamePreciseHighEnergyPhotonicQuantumMaster")));

        // #tr NameMiracleTop
        // # Miracle Top
        // #zh_CN 奇迹顶点
        GTCMItemList.MiracleTop
            .set(new GT_TileEntity_MiracleTop(19003, TSTUtils.tr("NameMiracleTop"), TSTUtils.tr("NameMiracleTop")));

        // #tr NameMagneticDrivePressureFormer
        // # Magnetic Drive Pressure Former
        // #zh_CN 磁驱压力成型机
        GTCMItemList.MagneticDrivePressureFormer.set(
            new GT_TileEntity_MagneticDrivePressureFormer(
                19004,
                "NameMagneticDrivePressureFormer",
                TSTUtils.tr("NameMagneticDrivePressureFormer")));

        // #tr NamePhysicalFormSwitcher
        // # Physical Form Switcher
        // #zh_CN 物质形态转换器
        GTCMItemList.PhysicalFormSwitcher.set(
            new GT_TileEntity_PhysicalFormSwitcher(
                19005,
                "NamePhysicalFormSwitcher",
                TSTUtils.tr("NamePhysicalFormSwitcher")));

        // #tr NameMagneticMixer
        // # "Mini" Magnetic Mixer
        // #zh_CN "小型"磁力搅拌机
        GTCMItemList.MagneticMixer
            .set(new GT_TileEntity_MagneticMixer(19006, "NameMagneticMixer", TSTUtils.tr("NameMagneticMixer")));

        // #tr NameMagneticDomainConstructor
        // # Magnetic Domain Constructor
        // #zh_CN 磁畴构建器
        GTCMItemList.MagneticDomainConstructor.set(
            new GT_TileEntity_MagneticDomainConstructor(
                19007,
                "NameMagneticDomainConstructor",
                TSTUtils.tr("NameMagneticDomainConstructor")));

        // #tr NameSilksong
        // # Silksong
        // #zh_CN 丝之歌
        GTCMItemList.Silksong.set(new GT_TileEntity_Silksong(19008, "NameSilksong", TSTUtils.tr("NameSilksong")));

        // #tr NameHolySeparator
        // # Holy Separator
        // #zh_CN 神圣分离者
        GTCMItemList.HolySeparator
            .set(new GT_TileEntity_HolySeparator(19009, "NameHolySeparator", TSTUtils.tr("NameHolySeparator")));

        // #tr NameSpaceScaler
        // # Space Scaler
        // #zh_CN 空间缩放仪
        GTCMItemList.SpaceScaler
            .set(new GT_TileEntity_SpaceScaler(19010, "NameSpaceScaler", TSTUtils.tr("NameSpaceScaler")));

        // #tr NameMoleculeDeconstructor
        // # Molecule Deconstructor
        // #zh_CN 分子解构器
        GTCMItemList.MoleculeDeconstructor.set(
            new GT_TileEntity_MoleculeDeconstructor(
                19011,
                "NameMoleculeDeconstructor",
                TSTUtils.tr("NameMoleculeDeconstructor")));

        // #tr NameCrystallineInfinitier
        // # Crystalline Infinitier
        // #zh_CN 无限晶胞
        GTCMItemList.CrystallineInfinitier.set(
            new GTCM_CrystallineInfinitier(
                19012,
                "NameCrystallineInfinitier",
                TSTUtils.tr("NameCrystallineInfinitier")));

        // #tr NameDSPLauncher
        // # Dyson Sphere Module Launch Site
        // #zh_CN 戴森球模块发射场
        GTCMItemList.DSPLauncher.set(new TST_DSPLauncher(19013, "NameDSPLauncher", TSTUtils.tr("NameDSPLauncher")));

        // #tr NameDSPReceiver
        // # Dyson Sphere Ray Receiving Station
        // #zh_CN 戴森球射线接收站
        GTCMItemList.DSPReceiver.set(new TST_DSPReceiver(19014, "NameDSPReceiver", TSTUtils.tr("NameDSPReceiver")));

        // #tr NameArtificialStar
        // # Artificial Star
        // #zh_CN 人造恒星
        GTCMItemList.ArtificialStar
            .set(new TST_ArtificialStar(19015, "NameArtificialStar", TSTUtils.tr("NameArtificialStar")));

        // #tr NameMiracleDoor
        // # Miracle Door
        // #zh_CN 奇迹之门
        GTCMItemList.MiracleDoor.set(new TST_MiracleDoor(19016, "NameMiracleDoor", TSTUtils.tr("NameMiracleDoor")));

        // #tr NameOreProcessingFactory
        // # General Ore Processing Factory TST
        // #zh_CN 通用矿物处理厂TST
        GTCMItemList.OreProcessingFactory.set(
            new TST_OreProcessingFactory(19017, "NameOreProcessingFactory", TSTUtils.tr("NameOreProcessingFactory")));

        // Space Station Systems
        // #tr NameMegaUniversalSpaceStation
        // # Mega Universal Space Station
        // #zh_CN {\RED}寰 {\AQUA}宇 {\GOLD}空 {\BLUE}间 {\DARK_GRAY}站

        // #tr NameStellarMaterialSiphon
        // # Stellar Material Siphon
        // #zh_CN Stellar Material Siphon
        // spotless:off
        /*
        if (Config.activateMegaSpaceStation) {
            GTCMItemList.megaUniversalSpaceStation.set(
                new TST_MegaUniversalSpaceStation(
                    19018,
                    "NameMegaUniversalSpaceStation",
                    TstUtils.tr("NameMegaUniversalSpaceStation")));
            GTCMItemList.StellarMaterialSiphon.set(
                new GT_TileEntity_StellarMaterialSiphon(
                    19019,
                    "NameStellarMaterialSiphon",
                    TstUtils.tr("NameStellarMaterialSiphon")));
        }
         */
        // spotless:on

        // #tr NameCircuitConverter
        // # General Circuit Converter
        // #zh_CN 通用电路板转换器
        GTCMItemList.CircuitConverter
            .set(new TST_CircuitConverter(19020, "NameCircuitConverter", TSTUtils.tr("NameCircuitConverter")));

        // #tr NameLargeIndustrialCokingFactory
        // # Large Industrial Coking Factory
        // #zh_CN 大型工业炼焦厂
        GTCMItemList.LargeIndustrialCokingFactory.set(
            new TST_LargeIndustrialCokingFactory(
                19021,
                "NameLargeIndustrialCokingFactory",
                TSTUtils.tr("NameLargeIndustrialCokingFactory")));

        // #tr NameElvenWorkshop
        // # ElvenWorkshop
        // #zh_CN 精灵工坊
        GTCMItemList.ElvenWorkshop
            .set(new GTCM_ElvenWorkshop(19500, "NameElvenWorkshop", TSTUtils.tr("NameElvenWorkshop")));

        // #tr NameHyperSpacetimeTransformer
        // # HyperSpacetimeTransformer
        // #zh_CN 极限时空转换仪
        GTCMItemList.HyperSpacetimeTransformer.set(
            new GTCM_HyperSpacetimeTransformer(
                19501,
                "NameHyperSpacetimeTransformer",
                TSTUtils.tr("NameHyperSpacetimeTransformer")));

        // #tr NameMegaBrickedBlastFurnace
        // # Mega Bricked Blast Furnace
        // #zh_CN 巨型砖高炉
        GTCMItemList.MegaBrickedBlastFurnace.set(
            new GT_TileEntity_MegaBrickedBlastFurnace(
                19022,
                "NameMegaBrickedBlastFurnace",
                TSTUtils.tr("NameMegaBrickedBlastFurnace")));

        // #tr NameScavenger
        // # Scavenger
        // #zh_CN 拾荒者
        GTCMItemList.Scavenger.set(new TST_Scavenger(19023, "NameScavenger", TSTUtils.tr("NameScavenger")));

        // #tr NamesuperCleanRoom
        // # CleanRoom
        // #zh_CN TST超净间
        GTCMItemList.superCleanRoom
            .set(new TST_CleanRoom(19024, "NameTSTcleanroom", TSTUtils.tr("NamesuperCleanRoom")));

        // #tr NameBiosphereIII
        // # Biosphere III
        // #zh_CN 生物圈III号
        GTCMItemList.BiosphereIII.set(new TST_BiosphereIII(19025, "nameBiosphereIII", TSTUtils.tr("NameBiosphereIII")));

        // #tr NameMegaEggGenerator
        // # Tower of Abstraction
        // #zh_CN 抽象之塔
        GTCMItemList.MegaEggGenerator.set(
            new GT_TileEntity_MegaEggGenerator(19026, "NameMegaEggGenerator", TSTUtils.tr("NameMegaEggGenerator")));

        // #tr NameAdvancedMegaOilCracker
        // # Advanced Mega Oil Cracker
        // #zh_CN 进阶巨型石油裂化机
        GTCMItemList.AdvancedMegaOilCracker.set(
            new TST_AdvancedMegaOilCracker(
                19027,
                "NameAdvancedMegaOilCracker",
                TSTUtils.tr("NameAdvancedMegaOilCracker")));

        // #tr NameIndistinctTentacle
        // # {\BOLD}{\DARK_GRAY}Indistinct Tentacle
        // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}
        GTCMItemList.IndistinctTentacle
            .set(new TST_IndistinctTentacle(19028, "NameIndistinctTentacle", TSTUtils.tr("NameIndistinctTentacle")));

        // #tr NameAstralComputingArray
        // # Astral Computing Array
        // #zh_CN 星规阵列
        GTCMItemList.AstralComputingArray
            .set(new TST_Computer(19029, "NameAstralComputingArray", TSTUtils.tr("NameAstralComputingArray")));

        // #tr NameThermalEnergyDevourer
        // # Thermal Energy Devourer
        // #zh_CN 热能饕餮
        GTCMItemList.ThermalEnergyDevourer.set(
            new TST_ThermalEnergyDevourer(
                19030,
                "NameThermalEnergyDevourer",
                TSTUtils.tr("NameThermalEnergyDevourer")));

        // #tr NameVacuumFilterExtractor
        // # Vacuum Filter Extractor
        // #zh_CN 真空抽滤器
        GTCMItemList.VacuumFilterExtractor.set(
            new TST_VacuumFilterExtractor(
                19031,
                "NameVacuumFilterExtractor",
                TSTUtils.tr("NameVacuumFilterExtractor")));

        // #tr NameLargeSteamForgeHammer
        // # Large Steam Forge Hammer
        // #zh_CN 大型蒸汽锻造锤
        GTCMItemList.LargeSteamForgeHammer.set(
            new TST_LargeSteamForgeHammer(
                19032,
                "NameLargeSteamForgeHammer",
                TSTUtils.tr("NameLargeSteamForgeHammer")));

        // #tr NameLargeSteamAlloySmelter
        // # Large Steam Alloy Smelter
        // #zh_CN 大型蒸汽合金炉
        GTCMItemList.LargeSteamAlloySmelter.set(
            new TST_LargeSteamAlloySmelter(
                19033,
                "NameLargeSteamAlloySmelter",
                TSTUtils.tr("NameLargeSteamAlloySmelter")));

        // #tr NameEyeOfWood
        // # Eye of Wood
        // #zh_CN 武德之眼
        GTCMItemList.EyeOfWood.set(new TST_EyeOfWood(19034, "NameEyeOfWood", TSTUtils.tr("NameEyeOfWood")));

        // #tr NameBeeEngineer
        // # Bee Engineer (Prototype)
        // #zh_CN 蜜蜂操纵者 (Prototype)
        GTCMItemList.BeeEngineer.set(new TST_BeeEngineer(19035, "NameBeeEngineer", TSTUtils.tr("NameBeeEngineer")));

        // #tr NameMegaMacerator
        // # "Mini" Household Cell Fragmentizer
        // #zh_CN "小型"家用破壁机
        GTCMItemList.MegaMacerator
            .set(new TST_MegaMacerator(19036, "NameMegaMacerator", TSTUtils.tr("NameMegaMacerator")));

        // #tr NameHephaestusAtelier
        // # Hephaestus' Atelier
        // #zh_CN 赫菲斯托斯的工坊
        GTCMItemList.HephaestusAtelier
            .set(new TST_HephaestusAtelier(19037, "NameHephaestusAtelier", TSTUtils.tr("NameHephaestusAtelier")));

        if (Config.Enable_DeployedNanoCore) {
            // #tr NameDeployedNanoCore
            // # Deployed Nano Core
            // #zh_CN 展开的纳米核心
            GTCMItemList.DeployedNanoCore
                .set(new TST_DeployedNanoCore(19038, "NameDeployedNanoCore", TSTUtils.tr("NameDeployedNanoCore")));
        }

        if (Config.Enable_CoreDeviceOfHumanPowerGenerationFacility) {
            // #tr NameCoreDeviceOfHumanPowerGenerationFacility
            // # Core Device of Human Power Generation Facility
            // #zh_CN 人类能源设施的核心装置
            GTCMItemList.CoreDeviceOfHumanPowerGenerationFacility.set(
                new TST_CoreDeviceOfHumanPowerGenerationFacility(
                    19039,
                    "NameCoreDeviceOfHumanPowerGenerationFacility",
                    TSTUtils.tr("NameCoreDeviceOfHumanPowerGenerationFacility")));
        }

        if (Config.Enable_StarcoreMiner) {
            GTCMItemList.StarcoreMiner.set(
                new TST_StarcoreMiner(
                    19040,
                    "NameStarcoreMiner",
                    // #tr NameStarcoreMiner
                    // # Starcore Miner
                    // #zh_CN 星核钻机
                    TSTUtils.tr("NameStarcoreMiner")));
        }

        if (Config.Enable_Disassembler) {
            GTCMItemList.Disassembler.set(
                new TST_Disassembler(
                    19041,
                    // #tr NameTSTDisassembler
                    // # TST Large Disassembler
                    // #zh_CN TST大型拆解机
                    "NameTSTDisassembler",
                    TSTUtils.tr("NameTSTDisassembler")));
        }

        if (Config.Enable_BallLightning) {
            // #tr NameBallLightning
            // # BallLightning
            // #zh_CN 球状闪电
            GTCMItemList.BallLightning
                .set(new TST_BallLightning(19046, "NameBallLightning", TSTUtils.tr("NameBallLightning")));
        }

        if (Config.Enable_IndustrialMagicMatrix) {
            GTCMItemList.IndustrialMagicMatrix.set(
                new GT_TileEntity_IndustrialMagicMatrix(
                    19047,
                    "IndustrialMagicMatrix",
                    // #tr NameIndustrialMagicMatrix
                    // # Industrial Magic Matrix
                    // #zh_CN §0工业注魔矩阵
                    TSTUtils.tr("NameIndustrialMagicMatrix")));
        }

        if (Config.Enable_LargeCanner) {
            GTCMItemList.LargeCanner.set(
                new TST_LargeCanner(
                    19048,
                    "NameLargeCanner",
                    // #tr NameLargeCanner
                    // # Large Canner
                    // #zh_CN 大型装罐机
                    TSTUtils.tr("NameLargeCanner")));
        }

        // #tr BigBroArray.name
        // # MegaArray
        // #zh_CN 大哥阵列
        GTCMItemList.BigBroArray.set(new TST_BigBroArray(19049, "BigBroArray.name", TSTUtils.tr("BigBroArray.name")));

        if (Config.Enable_IndustrialMagnetarSeparator) {
            GTCMItemList.IndustrialMagnetarSeparator.set(
                new TST_IndustrialMagnetarSeparator(
                    19050,
                    "NameIndustrialMagnetarSeparator",
                    // #tr NameIndustrialMagnetarSeparator
                    // # Industrial Magnetar Separator
                    // #zh_CN 工业电磁离析机
                    TSTUtils.tr("NameIndustrialMagnetarSeparator")));
        }

        if (Config.Enable_MegaTreeFarm) {
            GTCMItemList.MegaTreeFarm.set(
                new TST_MegaTreeFarm(
                    19051,
                    "NameMegaTreeFarm",
                    // #tr NameMegaTreeFarm
                    // # Eco-Sphere Growth Simulator
                    // #zh_CN 拟似生态圈
                    TSTUtils.tr("NameMegaTreeFarm")));
        }

        // #tr NameExtremeCraftCenter
        // # Extreme Crafting Center
        // #zh_CN 梦魇工业合成中心
        GTCMItemList.ExtremeCraftCenter
            .set(new TST_MegaCraftingCenter(19052, "NameExtremeCraftCenter", TSTUtils.tr("NameExtremeCraftCenter")));

        // #tr NamePatternAccessHatch
        // # Pattern Access Hatch
        // #zh_CN 样板访问仓
        GTCMItemList.PatternAccessHatch
            .set(new TST_PatternAccessHatch(18847, "NamePatternAccessHatch", TSTUtils.tr("NamePatternAccessHatch"), 9));

        if (Config.Enable_LightningSpire) {
            GTCMItemList.LightningSpire.set(
                new GTCM_LightningSpire(
                    19053,
                    "NameLightningSpire",
                    // #tr NameLightningSpire
                    // # Lightning Spire
                    // #zh_CN 闪电尖塔
                    TSTUtils.tr("NameLightningSpire")));
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
                        TSTUtils.tr("NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2")));
            }

            if (Config.EnableLargeNeutronOscillator) {
                // #tr NameLargeNeutronOscillator
                // # Large Neutron Oscillator
                // #zh_CN 大型中子振荡器
                GTCMItemList.LargeNeutronOscillator.set(
                    new MM_LargeNeutronOscillator(
                        19055,
                        "NameLargeNeutronOscillator",
                        TSTUtils.tr("NameLargeNeutronOscillator")));
            }

            if (Config.EnableRecipeRegistry_IndistinctTentacle) {
                // #tr NameIndistinctTentaclePrototypeMK2
                // # {\DARK_GRAY}{\BOLD}Indistinct Tentacle {\RESET}Prototype MK-II
                // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}原型机MK-II
                GTCMItemList.IndistinctTentaclePrototypeMK2.set(
                    new MM_IndistinctTentaclePrototypeMK2(
                        19056,
                        "NameIndistinctTentaclePrototypeMK2",
                        TSTUtils.tr("NameIndistinctTentaclePrototypeMK2")));
            }

            // #tr NameMassFabricatorGenesis
            // # Mass Fabricator : Genesis
            // #zh_CN 质量发生器 : 创世纪
            GTCMItemList.MassFabricatorGenesis.set(
                new MM_MassFabricatorGenesis(
                    19057,
                    "NameMassFabricatorGenesis",
                    TSTUtils.tr("NameMassFabricatorGenesis")));

        }

        if (Config.Enable_IncompactCyclotron) {
            // #tr NameIncompactCyclotron
            // # PULSAR - Incompact Cyclotron
            // #zh_CN PULSAR - 非紧凑式回旋加速器
            GTCMItemList.IncompactCyclotron.set(
                new TST_IncompactCyclotron(19058, "NameIncompactCyclotron", TSTUtils.tr("NameIncompactCyclotron")));
        }

        if (Config.EnableModularizedMachineSystem) {
            // #tr NameStrangeMatterAggregator
            // # Strange Matter Aggregator
            // #zh_CN 奇异物质聚合器
            GTCMItemList.StrangeMatterAggregator.set(
                new TST_StrangeMatterAggregator(
                    19059,
                    "NameStrangeMatterAggregator",
                    TSTUtils.tr("NameStrangeMatterAggregator")));

        }

        // #tr NameMicroSpaceTimeFabricatorio
        // # Micro SpaceTime Fabricatorio
        // #zh_CN 微型时空发生器
        GTCMItemList.MicroSpaceTimeFabricatorio.set(
            new TST_MicroSpaceTimeFabricatorio(
                19060,
                "NameMicroSpaceTimeFabricatorio",
                TSTUtils.tr("NameMicroSpaceTimeFabricatorio")));

        if (Config.Enable_BloodHell) {
            // #tr NameBloodyHell
            // # Bloody Hell
            // #zh_CN 血狱
            GTCMItemList.BloodyHell.set(new TST_BloodyHell(19061, "NameBloodyHell", TSTUtils.tr("NameBloodyHell")));

            if (Config.Enable_BloodHatch) {
                // #tr NameBloodOrbHatch
                // # Blood Hatch
                // #zh_CN 血液仓
                GTCMItemList.BloodOrbHatch
                    .set(new TST_BloodOrbHatch(18846, "NameBloodOrbHatch", TSTUtils.tr("NameBloodOrbHatch"), 4));

            }
        }

        if (Enable_MegaStoneBreaker) {
            // #tr NameMegaStoneBreaker
            // # Silicon Rock Synthesizer
            // #zh_CN 硅岩制造机
            GTCMItemList.MegaStoneBreaker
                .set(new TST_MegaStoneBreaker(19062, "NameMegaStoneBreaker", TSTUtils.tr("NameMegaStoneBreaker")));
        }

        // #tr NameManufacturingCenter
        // # Manufacturing Center
        // #zh_CN 加工中心
        GTCMItemList.ManufacturingCenter
            .set(new TST_ManufacturingCenter(19063, "NameManufacturingCenter", TSTUtils.tr("NameManufacturingCenter")));

        if (Enable_IndustrialAlchemyTower) {
            GTCMItemList.IndustrialAlchemyTower.set(
                new TST_IndustrialAlchemyTower(
                    // #tr NameIndustrialAlchemyTower
                    // # Industrial Alchemy Tower
                    // #zh_CN 工业炼金塔
                    19064,
                    "IndustrialAlchemyTower",
                    TSTUtils.tr("NameIndustrialAlchemyTower")));
        }

        // #tr NameGiantVacuumDryingFurnace
        // # Giant Vacuum Drying Furnace
        // #zh_CN 巨型真空干燥炉
        if (Enable_GiantVacuumDryingFurnace) {
            GTCMItemList.GiantVacuumDryingFurnace.set(
                new TST_GiantVacuumDryingFurnace(
                    19065,
                    "GiantVacuumDryingFurnace",
                    TSTUtils.tr("NameGiantVacuumDryingFurnace")));
        }

        if (Config.Enable_ProcessingArray) {
            // #tr NameProcessingArray
            // # TST Processing Array
            // #zh_CN TST处理阵列
            GTCMItemList.ProcessingArray
                .set(new TST_ProcessingArray(19066, "NameProcessingArray", TSTUtils.tr("NameProcessingArray")));
        }

        if (Config.Enable_AdvCircuitAssemblyLine) {
            // #tr NameAdvCircuitAssemblyLine
            // # Advanced Circuit Assembly Line
            // #zh_CN 进阶电路装配线
            GTCMItemList.AdvCircuitAssemblyLine.set(
                new TST_AdvCircuitAssemblyLine(
                    19067,
                    "NameAdvCircuitAssemblyLine",
                    TSTUtils.tr("NameAdvCircuitAssemblyLine")));
        }

        if (Config.Enable_SwelegfyrBlastFurnace) {
            // #tr NameSwelegfyrBlastFurnace
            // # Swelegfyr Blast Furnace
            // #zh_CN 熯焱高炉
            GTCMItemList.SwelegfyrBlastFurnace.set(
                new TST_SwelegfyrBlastFurnace(
                    19068,
                    "NameSwelegfyrBlastFurnace",
                    TSTUtils.tr("NameSwelegfyrBlastFurnace")));
        }

        if (Config.Enable_HyperThermalConvector) {
            // #tr NameHyperThermalConvector
            // # Hyper Thermal Convector
            // #zh_CN 高能态热对流器
            GTCMItemList.HyperThermalConvector.set(
                new TST_HyperThermalConvector(
                    19069,
                    "NameHyperThermalConvector",
                    TSTUtils.tr("NameHyperThermalConvector")));
        }

        if (Config.Enable_PrimordialDisjunctus) {
            // #tr NamePrimordialDisjunctus
            // # Primordial Disjunctus
            // #zh_CN 初源解离机
            GTCMItemList.PrimordialDisjunctus.set(
                new TST_PrimordialDisjunctus(19070, "PrimordialDisjunctus", TSTUtils.tr("NamePrimordialDisjunctus")));
        }

        if (Config.Enable_SkypiercerTower) {
            // #tr NameSkypiercerTower
            // # Skypiercer Tower
            // #zh_CN 穿云尖塔
            GTCMItemList.SkypiercerTower
                .set(new TST_SkypiercerTower(19071, "NameSkypiercerTower", TSTUtils.tr("NameSkypiercerTower")));
        }

        if (Config.Enable_LaserMeteorMiner) {
            // #tr NameMeteorMiner
            // # Laser Meteor Miner
            // #zh_CN 激光陨星采矿场
            GTCMItemList.MeteorMiner
                .set(new TST_LaserMeteorMiner(19072, "NameMeteorMiner", TSTUtils.tr("NameMeteorMiner")));
        }

        // #tr NameSteamBasicGenerator
        // # Basic Steam Generator
        // #zh_CN 基础蒸汽发电机
        GTCMItemList.SteamBasicGenerator
            .set(new TST_SteamBasicGenerator(19073, "NameSteamBasicGenerator", TSTUtils.tr("NameSteamBasicGenerator")));

        // #tr NameUniversalGenerator
        // # Universal Generator
        // #zh_CN 通用发电机
        GTCMItemList.UniversalGenerator
            .set(new TST_UniversalGenerator(19074, "NameUniversalGenerator", TSTUtils.tr("NameUniversalGenerator")));

        if (Config.Enable_InfusionMaterialDispenser) {
            // #tr NameInfusionMaterialDispenser
            // # Infusion Material Dispenser
            // #zh_CN 注魔分配器
            GTCMItemList.InfusionMaterialDispenser.set(
                new TST_InfusionMaterialDispenser(
                    19075,
                    "NameInfusionMaterialDispenser",
                    TSTUtils.tr("NameInfusionMaterialDispenser")));
        }

        // #tr NameLargeSolarBoiler
        // # Large Solar Boiler
        // #zh_CN 大型太阳能锅炉
        GTCMItemList.LargeSolarBoiler
            .set(new TST_LargeSolarBoiler(19076, "NameLargeSolarBoiler", TSTUtils.tr("NameLargeSolarBoiler")));

        // #tr NameNetherInterface
        // # Nether Interface
        // #zh_CN 地狱接口
        GTCMItemList.NetherInterface
            .set(new TST_NetherInterface(19077, "NameNetherInterface", TSTUtils.tr("NameNetherInterface")));

        // #tr NameSuperWaterPurifier
        // # Super Water Purifier
        // #zh_CN 超净水生成器
        GTCMItemList.SuperWaterPurifier
            .set(new TST_SuperWaterPurifier(19078, "NameSuperWaterPurifier", TSTUtils.tr("NameSuperWaterPurifier")));

        // #tr NameIntegratedAssemblyMatrix
        // # Integrated Assembly Matrix
        // #zh_CN 集成装配矩阵
        GTCMItemList.IntegratedAssemblyMatrix.set(
            new TST_IntegratedAssemblyMatrix(
                19079,
                "NameIntegratedAssemblyMatrix",
                TSTUtils.tr("NameIntegratedAssemblyMatrix")));

        // #tr NameMegaSolarPanelFactory
        // # Mega Solar Panel Factory
        // #zh_CN 巨型太阳能板工厂
        GTCMItemList.MegaSolarPanelFactory.set(
            new TST_MegaSolarPanelFactory(
                19080,
                "NameMegaSolarPanelFactory",
                TSTUtils.tr("NameMegaSolarPanelFactory")));

        // #tr NameMegaNqReactor
        // # Mega Naquadah Reactor
        // #zh_CN 巨型硅岩反应堆
        GTCMItemList.MegaNqReactor
            .set(new TST_MegaNqReactor(19081, "NameMegaNqReactor", TSTUtils.tr("NameMegaNqReactor")));

        // endregion

        // region Single block Machine

        // #tr NameInfiniteAirHatch
        // # Infinite Air Hatch
        // #zh_CN 无限进气仓
        GTCMItemList.InfiniteAirHatch.set(
            new GT_MetaTileEntity_Hatch_Air(18999, "NameInfiniteAirHatch", TSTUtils.tr("NameInfiniteAirHatch"), 9));

        // #tr NameInfiniteWirelessDynamoHatch
        // # Infinite Wireless Dynamo Hatch
        // #zh_CN 无限无线动力仓
        GTCMItemList.InfiniteWirelessDynamoHatch.set(
            new GT_Hatch_InfiniteWirelessDynamoHatch(
                18998,
                "NameInfiniteWirelessDynamoHatch",
                TSTUtils.tr("NameInfiniteWirelessDynamoHatch"),
                14));

        // #tr NameManaHatch
        // # Mana Hatch
        // #zh_CN Mana Hatch
        GTCMItemList.ManaHatch.set(new TST_ManaHatch(18979, "NameManaHatch", TSTUtils.tr("NameManaHatch"), 9));

        // #tr NameAEStorageCellInputBus
        // # Super Stocking Input Bus (ME)
        // #zh_CN 超级存储输入总线(ME)
        GTCMItemList.AEStorageCellInputBus.set(
            new TST_AEStorageCellInputBus(
                18851,
                "NameAEStorageCellInputBus",
                TSTUtils.tr("NameAEStorageCellInputBus"),
                9));

        // #tr NameAEStorageCellInputHatch
        // # Super Stocking Input Hatch (ME)
        // #zh_CN 超级存储输入仓(ME)
        GTCMItemList.AEStorageCellInputHatch.set(
            new TST_AEStorageCellInputHatch(
                18852,
                "NameAEStorageCellInputHatch",
                TSTUtils.tr("NameAEStorageCellInputHatch"),
                9));

        // region Dual Input Buffer
        // #tr NameDualInputBuffer_IV
        // # Dual Input Buffer (IV)
        // #zh_CN 输入总成 (IV)
        GTCMItemList.DualInputBuffer_IV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18980,
                "NameDualInputBuffer_IV",
                TSTUtils.tr("NameDualInputBuffer_IV"),
                5));

        // #tr NameDualInputBuffer_LuV
        // # Dual Input Buffer (LuV)
        // #zh_CN 输入总成 (LuV)
        GTCMItemList.DualInputBuffer_LuV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18981,
                "NameDualInputBuffer_LuV",
                TSTUtils.tr("NameDualInputBuffer_LuV"),
                6));

        // #tr NameDualInputBuffer_ZPM
        // # Dual Input Buffer (ZPM)
        // #zh_CN 输入总成 (ZPM)
        GTCMItemList.DualInputBuffer_ZPM.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18982,
                "NameDualInputBuffer_ZPM",
                TSTUtils.tr("NameDualInputBuffer_ZPM"),
                7));

        // #tr NameDualInputBuffer_UV
        // # Dual Input Buffer (UV)
        // #zh_CN 输入总成 (UV)
        GTCMItemList.DualInputBuffer_UV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18983,
                "NameDualInputBuffer_UV",
                TSTUtils.tr("NameDualInputBuffer_UV"),
                8));

        // region buffered energy hatch

        // #tr NameBufferedEnergyHatchLV
        // # Buffered Energy Hatch LV
        // #zh_CN 缓存能源仓LV

        // #tr NameBufferedEnergyHatchMV
        // # Buffered Energy Hatch MV
        // #zh_CN 缓存能源仓MV

        // #tr NameBufferedEnergyHatchHV
        // # Buffered Energy Hatch HV
        // #zh_CN 缓存能源仓HV

        // #tr NameBufferedEnergyHatchEV
        // # Buffered Energy Hatch EV
        // #zh_CN 缓存能源仓EV

        // #tr NameBufferedEnergyHatchIV
        // # Buffered Energy Hatch IV
        // #zh_CN 缓存能源仓IV

        // #tr NameBufferedEnergyHatchLuV
        // # Buffered Energy Hatch LuV
        // #zh_CN 缓存能源仓LuV

        // #tr NameBufferedEnergyHatchZPM
        // # Buffered Energy Hatch ZPM
        // #zh_CN 缓存能源仓ZPM

        // #tr NameBufferedEnergyHatchUV
        // # Buffered Energy Hatch UV
        // #zh_CN 缓存能源仓UV

        // #tr NameBufferedEnergyHatchUHV
        // # Buffered Energy Hatch UHV
        // #zh_CN 缓存能源仓UHV

        // #tr NameBufferedEnergyHatchUEV
        // # Buffered Energy Hatch UEV
        // #zh_CN 缓存能源仓UEV

        // #tr NameBufferedEnergyHatchUIV
        // # Buffered Energy Hatch UIV
        // #zh_CN 缓存能源仓UIV

        // #tr NameBufferedEnergyHatchUMV
        // # Buffered Energy Hatch UMV
        // #zh_CN 缓存能源仓UMV

        // #tr NameBufferedEnergyHatchUXV
        // # Buffered Energy Hatch UXV
        // #zh_CN 缓存能源仓UXV

        // #tr NameBufferedEnergyHatchMAX
        // # Buffered Energy Hatch MAX
        // #zh_CN 缓存能源仓MAX
        GTCMItemList.BufferedEnergyHatchLV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18984,
                "NameBufferedEnergyHatchLV",
                TSTUtils.tr("NameBufferedEnergyHatchLV"),
                1,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchMV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18985,
                "NameBufferedEnergyHatchMV",
                TSTUtils.tr("NameBufferedEnergyHatchMV"),
                2,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchHV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18986,
                "NameBufferedEnergyHatchHV",
                TSTUtils.tr("NameBufferedEnergyHatchHV"),
                3,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchEV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18987,
                "NameBufferedEnergyHatchEV",
                TSTUtils.tr("NameBufferedEnergyHatchEV"),
                4,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchIV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18988,
                "NameBufferedEnergyHatchIV",
                TSTUtils.tr("NameBufferedEnergyHatchIV"),
                5,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchLuV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18989,
                "NameBufferedEnergyHatchLuV",
                TSTUtils.tr("NameBufferedEnergyHatchLuV"),
                6,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchZPM.set(
            new GT_Hatch_BufferedEnergyHatch(
                18990,
                "NameBufferedEnergyHatchZPM",
                TSTUtils.tr("NameBufferedEnergyHatchZPM"),
                7,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18991,
                "NameBufferedEnergyHatchUV",
                TSTUtils.tr("NameBufferedEnergyHatchUV"),
                8,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUHV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18992,
                "NameBufferedEnergyHatchUHV",
                TSTUtils.tr("NameBufferedEnergyHatchUHV"),
                9,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUEV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18993,
                "NameBufferedEnergyHatchUEV",
                TSTUtils.tr("NameBufferedEnergyHatchUEV"),
                10,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUIV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18994,
                "NameBufferedEnergyHatchUIV",
                TSTUtils.tr("NameBufferedEnergyHatchUIV"),
                11,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUMV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18995,
                "NameBufferedEnergyHatchUMV",
                TSTUtils.tr("NameBufferedEnergyHatchUMV"),
                12,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUXV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18996,
                "NameBufferedEnergyHatchUXV",
                TSTUtils.tr("NameBufferedEnergyHatchUXV"),
                13,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchMAX.set(
            new GT_Hatch_BufferedEnergyHatch(
                18997,
                "NameBufferedEnergyHatchMAX",
                TSTUtils.tr("NameBufferedEnergyHatchMAX"),
                14,
                16,
                null));

        // #tr NameDebugUncertaintyHatch
        // # Debug Uncertainty Hatch
        // #zh_CN Debug未定元解析器
        GTCMItemList.DebugUncertaintyHatch.set(
            new GT_MetaTileEntity_Hatch_UncertaintyDebug(
                18978,
                "NameDebugUncertaintyHatch",
                TSTUtils.tr("NameDebugUncertaintyHatch"),
                12));

        // #tr NameLaserSmartNode
        // # Laser Smart Node
        // #zh_CN 激光智能节点
        GTCMItemList.LaserSmartNode.set(
            new GT_MetaTileEntity_Pipe_EnergySmart(18960, "NameLaserSmartNode", TSTUtils.tr("NameLaserSmartNode")));

        // #tr NameLaserFocusedSmartNode
        // # Laser-Focused Smart Node
        // #zh_CN 激光聚焦智能节点
        GTCMItemList.LaserFocusedSmartNode.set(
            new GT_MetaTileEntity_Pipe_EnergySmart_Focusing(
                18961,
                "NameLaserFocusedSmartNode",
                TSTUtils.tr("NameLaserFocusedSmartNode")));
        // endregion

        // #tr NameFackRackHatch
        // # rack simulation hack
        // #zh_CN 机箱模拟器
        GTCMItemList.FackRackHatch.set(
            new GT_Hatch_RackComputationMonitor(
                18959,
                "NameFackRackHatch",
                TSTUtils.tr("NameFackRackHatch"),
                12,
                false));

        // #tr NameRealRackHatch
        // # rack simulation controller hack
        // #zh_CN 机箱控制器
        GTCMItemList.RealRackHatch.set(
            new GT_Hatch_RackComputationMonitor(
                18958,
                "NameRealRackHatch",
                TSTUtils.tr("NameRealRackHatch"),
                12,
                true));

        // #tr NameWirelessDataInputHatch
        // # Wireless Optical Slave Connector
        // #zh_CN 无线副光学接口
        GTCMItemList.WirelessDataInputHatch.set(
            new GT_Hatch_WirelessData_input(
                18957,
                "NameWirelessDataInputHatch",
                TSTUtils.tr("NameWirelessDataInputHatch"),
                12));

        // #tr NameWirelessDataOutputHatch
        // # Wireless Optical Master Connector
        // #zh_CN 无线主光学接口
        GTCMItemList.WirelessDataOutputHatch.set(
            new GT_Hatch_WirelessData_output(
                18956,
                "NameWirelessDataOutputHatch",
                TSTUtils.tr("NameWirelessDataOutputHatch"),
                12));

        // #tr NameLegendaryWirelessEnergyHatch
        // # Legendary Wireless Energy Hatch
        // #zh_CN 传奇无线能源仓
        GTCMItemList.LegendaryWirelessEnergyHatch.set(
            new GT_Hatch_InfiniteWirelessMulti(
                18798,
                "NameLegendaryWirelessEnergyHatch",
                TSTUtils.tr("NameLegendaryWirelessEnergyHatch"),
                13,
                536870912));

        // #tr NameHarmoniousWirelessEnergyHatch
        // # Harmonious Wireless Energy Hatch
        // #zh_CN 鸿蒙无线能源仓
        GTCMItemList.HarmoniousWirelessEnergyHatch.set(
            new GT_Hatch_InfiniteWirelessMulti(
                18799,
                "NameHarmoniousWirelessEnergyHatch",
                TSTUtils.tr("NameHarmoniousWirelessEnergyHatch"),
                14,
                2147483647));

        // #tr NameSolidifyHatchUHV
        // # Solidifier Hatch(UHV)
        // #zh_CN 固化仓(UHV)
        GTCMItemList.SolidifyHatch_UHV.set(
            new GT_MetaTileEntity_Hatch_Solidify(
                18797,
                "NameSolidifyHatchUHV",
                TSTUtils.tr("NameSolidifyHatchUHV"),
                9));

        // #tr NameSolidifyHatchUV
        // # Solidifier Hatch(UV)
        // #zh_CN 固化仓(UV)
        GTCMItemList.SolidifyHatch_UV.set(
            new GT_MetaTileEntity_Hatch_Solidify(18796, "NameSolidifyHatchUV", TSTUtils.tr("NameSolidifyHatchUV"), 8));

        // #tr NameSolidifyHatchZPM
        // # Solidifier Hatch(ZPM)
        // #zh_CN 固化仓(ZPM)
        GTCMItemList.SolidifyHatch_ZPM.set(
            new GT_MetaTileEntity_Hatch_Solidify(
                18795,
                "NameSolidifyHatchZPM",
                TSTUtils.tr("NameSolidifyHatchZPM"),
                7));

        // #tr NameSolidifyHatchLuV
        // # Solidifier Hatch(LuV)
        // #zh_CN 固化仓(LuV)
        GTCMItemList.SolidifyHatch_LuV.set(
            new GT_MetaTileEntity_Hatch_Solidify(
                18794,
                "NameSolidifyHatchLuV",
                TSTUtils.tr("NameSolidifyHatchLuV"),
                6));

        // #tr NameSolidifyHatchIV
        // # Solidifier Hatch(IV)
        // #zh_CN 固化仓(IV)
        GTCMItemList.SolidifyHatch_IV.set(
            new GT_MetaTileEntity_Hatch_Solidify(18793, "NameSolidifyHatchIV", TSTUtils.tr("NameSolidifyHatchIV"), 5));

        // #tr NameCircuitImprintHatchT2
        // # Imprint Circuit Hatch T2
        // #zh_CN 压印电路仓T2
        GTCMItemList.CircuitImprintHatchT2.set(
            new TST_CircuitImprintHatch(
                18792,
                "NameCircuitImprintHatchT2",
                TSTUtils.tr("NameCircuitImprintHatchT2"),
                8));

        // #tr NameCircuitImprintHatchT1
        // # Imprint Circuit Hatch T1
        // #zh_CN 压印电路仓T1
        GTCMItemList.CircuitImprintHatchT1.set(
            new TST_CircuitImprintHatch(
                18791,
                "NameCircuitImprintHatchT1",
                TSTUtils.tr("NameCircuitImprintHatchT1"),
                5));

        // region Modularized Stuff

        if (Config.EnableModularizedMachineSystem) {

            // #tr NameDynamicParallelControllerT1
            // # Dynamic Parallel Controller Module T1
            // #zh_CN 动态并行控制器模块T1
            GTCMItemList.DynamicParallelControllerT1.set(
                new DynamicParallelController(
                    18800,
                    "NameDynamicParallelControllerT1",
                    TSTUtils.tr("NameDynamicParallelControllerT1"),
                    7,
                    ParallelOfParallelController[0]));

            // #tr NameDynamicParallelControllerT2
            // # Dynamic Parallel Controller Module T2
            // #zh_CN 动态并行控制器模块T2
            GTCMItemList.DynamicParallelControllerT2.set(
                new DynamicParallelController(
                    18801,
                    "NameDynamicParallelControllerT2",
                    TSTUtils.tr("NameDynamicParallelControllerT2"),
                    8,
                    ParallelOfParallelController[1]));

            // #tr NameDynamicParallelControllerT3
            // # Dynamic Parallel Controller Module T3
            // #zh_CN 动态并行控制器模块T3
            GTCMItemList.DynamicParallelControllerT3.set(
                new DynamicParallelController(
                    18802,
                    "NameDynamicParallelControllerT3",
                    TSTUtils.tr("NameDynamicParallelControllerT3"),
                    9,
                    ParallelOfParallelController[2]));

            // #tr NameDynamicParallelControllerT4
            // # Dynamic Parallel Controller Module T4
            // #zh_CN 动态并行控制器模块T4
            GTCMItemList.DynamicParallelControllerT4.set(
                new DynamicParallelController(
                    18803,
                    "NameDynamicParallelControllerT4",
                    TSTUtils.tr("NameDynamicParallelControllerT4"),
                    10,
                    ParallelOfParallelController[3]));

            // #tr NameDynamicParallelControllerT5
            // # Dynamic Parallel Controller Module T5
            // #zh_CN 动态并行控制器模块T5
            GTCMItemList.DynamicParallelControllerT5.set(
                new DynamicParallelController(
                    18804,
                    "NameDynamicParallelControllerT5",
                    TSTUtils.tr("NameDynamicParallelControllerT5"),
                    11,
                    ParallelOfParallelController[4]));

            // #tr NameDynamicParallelControllerT6
            // # Dynamic Parallel Controller Module T6
            // #zh_CN 动态并行控制器模块T6
            GTCMItemList.DynamicParallelControllerT6.set(
                new DynamicParallelController(
                    18805,
                    "NameDynamicParallelControllerT6",
                    TSTUtils.tr("NameDynamicParallelControllerT6"),
                    12,
                    ParallelOfParallelController[5]));

            // #tr NameDynamicParallelControllerT7
            // # Dynamic Parallel Controller Module T7
            // #zh_CN 动态并行控制器模块T7
            GTCMItemList.DynamicParallelControllerT7.set(
                new DynamicParallelController(
                    18806,
                    "NameDynamicParallelControllerT7",
                    TSTUtils.tr("NameDynamicParallelControllerT7"),
                    13,
                    ParallelOfParallelController[6]));

            // #tr NameDynamicParallelControllerT8
            // # Dynamic Parallel Controller Module T8
            // #zh_CN 动态并行控制器模块T8
            GTCMItemList.DynamicParallelControllerT8.set(
                new DynamicParallelController(
                    18807,
                    "NameDynamicParallelControllerT8",
                    TSTUtils.tr("NameDynamicParallelControllerT8"),
                    14,
                    ParallelOfParallelController[7]));

            // #tr NameStaticParallelControllerT1
            // # Static Parallel Controller Module T1
            // #zh_CN 静态并行控制器模块T1
            GTCMItemList.StaticParallelControllerT1.set(
                new StaticParallelController(
                    18808,
                    "NameStaticParallelControllerT1",
                    TSTUtils.tr("NameStaticParallelControllerT1"),
                    7,
                    ParallelOfParallelController[0]));

            // #tr NameStaticParallelControllerT2
            // # Static Parallel Controller Module T2
            // #zh_CN 静态并行控制器模块T2
            GTCMItemList.StaticParallelControllerT2.set(
                new StaticParallelController(
                    18809,
                    "NameStaticParallelControllerT2",
                    TSTUtils.tr("NameStaticParallelControllerT2"),
                    8,
                    ParallelOfParallelController[1]));

            // #tr NameStaticParallelControllerT3
            // # Static Parallel Controller Module T3
            // #zh_CN 静态并行控制器模块T3
            GTCMItemList.StaticParallelControllerT3.set(
                new StaticParallelController(
                    18810,
                    "NameStaticParallelControllerT3",
                    TSTUtils.tr("NameStaticParallelControllerT3"),
                    9,
                    ParallelOfParallelController[2]));

            // #tr NameStaticParallelControllerT4
            // # Static Parallel Controller Module T4
            // #zh_CN 静态并行控制器模块T4
            GTCMItemList.StaticParallelControllerT4.set(
                new StaticParallelController(
                    18811,
                    "NameStaticParallelControllerT4",
                    TSTUtils.tr("NameStaticParallelControllerT4"),
                    10,
                    ParallelOfParallelController[3]));

            // #tr NameStaticParallelControllerT5
            // # Static Parallel Controller Module T5
            // #zh_CN 静态并行控制器模块T5
            GTCMItemList.StaticParallelControllerT5.set(
                new StaticParallelController(
                    18812,
                    "NameStaticParallelControllerT5",
                    TSTUtils.tr("NameStaticParallelControllerT5"),
                    11,
                    ParallelOfParallelController[4]));

            // #tr NameStaticParallelControllerT6
            // # Static Parallel Controller Module T6
            // #zh_CN 静态并行控制器模块T6
            GTCMItemList.StaticParallelControllerT6.set(
                new StaticParallelController(
                    18813,
                    "NameStaticParallelControllerT6",
                    TSTUtils.tr("NameStaticParallelControllerT6"),
                    12,
                    ParallelOfParallelController[5]));

            // #tr NameStaticParallelControllerT7
            // # Static Parallel Controller Module T7
            // #zh_CN 静态并行控制器模块T7
            GTCMItemList.StaticParallelControllerT7.set(
                new StaticParallelController(
                    18814,
                    "NameStaticParallelControllerT7",
                    TSTUtils.tr("NameStaticParallelControllerT7"),
                    13,
                    ParallelOfParallelController[6]));

            // #tr NameStaticParallelControllerT8
            // # Static Parallel Controller Module T8
            // #zh_CN 静态并行控制器模块T8
            GTCMItemList.StaticParallelControllerT8.set(
                new StaticParallelController(
                    18815,
                    "NameStaticParallelControllerT8",
                    TSTUtils.tr("NameStaticParallelControllerT8"),
                    14,
                    ParallelOfParallelController[7]));

            // #tr NameDynamicSpeedControllerT1
            // # Dynamic Speed Controller Module T1
            // #zh_CN 动态速度控制器模块T1
            GTCMItemList.DynamicSpeedControllerT1.set(
                new DynamicSpeedController(
                    18816,
                    "NameDynamicSpeedControllerT1",
                    TSTUtils.tr("NameDynamicSpeedControllerT1"),
                    7,
                    SpeedMultiplierOfSpeedController[0]));

            // #tr NameDynamicSpeedControllerT2
            // # Dynamic Speed Controller Module T2
            // #zh_CN 动态速度控制器模块T2
            GTCMItemList.DynamicSpeedControllerT2.set(
                new DynamicSpeedController(
                    18817,
                    "NameDynamicSpeedControllerT2",
                    TSTUtils.tr("NameDynamicSpeedControllerT2"),
                    8,
                    SpeedMultiplierOfSpeedController[1]));

            // #tr NameDynamicSpeedControllerT3
            // # Dynamic Speed Controller Module T3
            // #zh_CN 动态速度控制器模块T3
            GTCMItemList.DynamicSpeedControllerT3.set(
                new DynamicSpeedController(
                    18818,
                    "NameDynamicSpeedControllerT3",
                    TSTUtils.tr("NameDynamicSpeedControllerT3"),
                    9,
                    SpeedMultiplierOfSpeedController[2]));

            // #tr NameDynamicSpeedControllerT4
            // # Dynamic Speed Controller Module T4
            // #zh_CN 动态速度控制器模块T4
            GTCMItemList.DynamicSpeedControllerT4.set(
                new DynamicSpeedController(
                    18819,
                    "NameDynamicSpeedControllerT4",
                    TSTUtils.tr("NameDynamicSpeedControllerT4"),
                    10,
                    SpeedMultiplierOfSpeedController[3]));

            // #tr NameDynamicSpeedControllerT5
            // # Dynamic Speed Controller Module T5
            // #zh_CN 动态速度控制器模块T5
            GTCMItemList.DynamicSpeedControllerT5.set(
                new DynamicSpeedController(
                    18820,
                    "NameDynamicSpeedControllerT5",
                    TSTUtils.tr("NameDynamicSpeedControllerT5"),
                    11,
                    SpeedMultiplierOfSpeedController[4]));

            // #tr NameDynamicSpeedControllerT6
            // # Dynamic Speed Controller Module T6
            // #zh_CN 动态速度控制器模块T6
            GTCMItemList.DynamicSpeedControllerT6.set(
                new DynamicSpeedController(
                    18821,
                    "NameDynamicSpeedControllerT6",
                    TSTUtils.tr("NameDynamicSpeedControllerT6"),
                    12,
                    SpeedMultiplierOfSpeedController[5]));

            // #tr NameDynamicSpeedControllerT7
            // # Dynamic Speed Controller Module T7
            // #zh_CN 动态速度控制器模块T7
            GTCMItemList.DynamicSpeedControllerT7.set(
                new DynamicSpeedController(
                    18822,
                    "NameDynamicSpeedControllerT7",
                    TSTUtils.tr("NameDynamicSpeedControllerT7"),
                    13,
                    SpeedMultiplierOfSpeedController[6]));

            // #tr NameDynamicSpeedControllerT8
            // # Dynamic Speed Controller Module T8
            // #zh_CN 动态速度控制器模块T8
            GTCMItemList.DynamicSpeedControllerT8.set(
                new DynamicSpeedController(
                    18823,
                    "NameDynamicSpeedControllerT8",
                    TSTUtils.tr("NameDynamicSpeedControllerT8"),
                    14,
                    SpeedMultiplierOfSpeedController[7]));

            // #tr NameStaticSpeedControllerT1
            // # Static Speed Controller Module T1
            // #zh_CN 静态速度控制器模块T1
            GTCMItemList.StaticSpeedControllerT1.set(
                new StaticSpeedController(
                    18824,
                    "NameStaticSpeedControllerT1",
                    TSTUtils.tr("NameStaticSpeedControllerT1"),
                    7,
                    SpeedMultiplierOfSpeedController[0]));

            // #tr NameStaticSpeedControllerT2
            // # Static Speed Controller Module T2
            // #zh_CN 静态速度控制器模块T2
            GTCMItemList.StaticSpeedControllerT2.set(
                new StaticSpeedController(
                    18825,
                    "NameStaticSpeedControllerT2",
                    TSTUtils.tr("NameStaticSpeedControllerT2"),
                    8,
                    SpeedMultiplierOfSpeedController[1]));

            // #tr NameStaticSpeedControllerT3
            // # Static Speed Controller Module T3
            // #zh_CN 静态速度控制器模块T3
            GTCMItemList.StaticSpeedControllerT3.set(
                new StaticSpeedController(
                    18826,
                    "NameStaticSpeedControllerT3",
                    TSTUtils.tr("NameStaticSpeedControllerT3"),
                    9,
                    SpeedMultiplierOfSpeedController[2]));

            // #tr NameStaticSpeedControllerT4
            // # Static Speed Controller Module T4
            // #zh_CN 静态速度控制器模块T4
            GTCMItemList.StaticSpeedControllerT4.set(
                new StaticSpeedController(
                    18827,
                    "NameStaticSpeedControllerT4",
                    TSTUtils.tr("NameStaticSpeedControllerT4"),
                    10,
                    SpeedMultiplierOfSpeedController[3]));

            // #tr NameStaticSpeedControllerT5
            // # Static Speed Controller Module T5
            // #zh_CN 静态速度控制器模块T5
            GTCMItemList.StaticSpeedControllerT5.set(
                new StaticSpeedController(
                    18828,
                    "NameStaticSpeedControllerT5",
                    TSTUtils.tr("NameStaticSpeedControllerT5"),
                    11,
                    SpeedMultiplierOfSpeedController[4]));

            // #tr NameStaticSpeedControllerT6
            // # Static Speed Controller Module T6
            // #zh_CN 静态速度控制器模块T6
            GTCMItemList.StaticSpeedControllerT6.set(
                new StaticSpeedController(
                    18829,
                    "NameStaticSpeedControllerT6",
                    TSTUtils.tr("NameStaticSpeedControllerT6"),
                    12,
                    SpeedMultiplierOfSpeedController[5]));

            // #tr NameStaticSpeedControllerT7
            // # Static Speed Controller Module T7
            // #zh_CN 静态速度控制器模块T7
            GTCMItemList.StaticSpeedControllerT7.set(
                new StaticSpeedController(
                    18830,
                    "NameStaticSpeedControllerT7",
                    TSTUtils.tr("NameStaticSpeedControllerT7"),
                    13,
                    SpeedMultiplierOfSpeedController[6]));

            // #tr NameStaticSpeedControllerT8
            // # Static Speed Controller Module T8
            // #zh_CN 静态速度控制器模块T8
            GTCMItemList.StaticSpeedControllerT8.set(
                new StaticSpeedController(
                    18831,
                    "NameStaticSpeedControllerT8",
                    TSTUtils.tr("NameStaticSpeedControllerT8"),
                    14,
                    SpeedMultiplierOfSpeedController[7]));

            // #tr NameStaticPowerConsumptionControllerT1
            // # Static Power Consumption Controller Module T1
            // #zh_CN 静态耗能控制器模块T1
            GTCMItemList.StaticPowerConsumptionControllerT1.set(
                new StaticPowerConsumptionController(
                    18832,
                    "NameStaticPowerConsumptionControllerT1",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT1"),
                    7,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[0]));

            // #tr NameStaticPowerConsumptionControllerT2
            // # Static Power Consumption Controller Module T2
            // #zh_CN 静态耗能控制器模块T2
            GTCMItemList.StaticPowerConsumptionControllerT2.set(
                new StaticPowerConsumptionController(
                    18833,
                    "NameStaticPowerConsumptionControllerT2",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT2"),
                    8,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[1]));

            // #tr NameStaticPowerConsumptionControllerT3
            // # Static Power Consumption Controller Module T3
            // #zh_CN 静态耗能控制器模块T3
            GTCMItemList.StaticPowerConsumptionControllerT3.set(
                new StaticPowerConsumptionController(
                    18834,
                    "NameStaticPowerConsumptionControllerT3",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT3"),
                    9,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[2]));

            // #tr NameStaticPowerConsumptionControllerT4
            // # Static Power Consumption Controller Module T4
            // #zh_CN 静态耗能控制器模块T4
            GTCMItemList.StaticPowerConsumptionControllerT4.set(
                new StaticPowerConsumptionController(
                    18835,
                    "NameStaticPowerConsumptionControllerT4",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT4"),
                    10,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[3]));

            // #tr NameStaticPowerConsumptionControllerT5
            // # Static Power Consumption Controller Module T5
            // #zh_CN 静态耗能控制器模块T5
            GTCMItemList.StaticPowerConsumptionControllerT5.set(
                new StaticPowerConsumptionController(
                    18836,
                    "NameStaticPowerConsumptionControllerT5",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT5"),
                    11,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[4]));

            // #tr NameStaticPowerConsumptionControllerT6
            // # Static Power Consumption Controller Module T6
            // #zh_CN 静态耗能控制器模块T6
            GTCMItemList.StaticPowerConsumptionControllerT6.set(
                new StaticPowerConsumptionController(
                    18837,
                    "NameStaticPowerConsumptionControllerT6",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT6"),
                    12,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[5]));

            // #tr NameStaticPowerConsumptionControllerT7
            // # Static Power Consumption Controller Module T7
            // #zh_CN 静态耗能控制器模块T7
            GTCMItemList.StaticPowerConsumptionControllerT7.set(
                new StaticPowerConsumptionController(
                    18838,
                    "NameStaticPowerConsumptionControllerT7",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT7"),
                    13,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[6]));

            // #tr NameStaticPowerConsumptionControllerT8
            // # Static Power Consumption Controller Module T8
            // #zh_CN 静态耗能控制器模块T8
            GTCMItemList.StaticPowerConsumptionControllerT8.set(
                new StaticPowerConsumptionController(
                    18839,
                    "NameStaticPowerConsumptionControllerT8",
                    TSTUtils.tr("NameStaticPowerConsumptionControllerT8"),
                    14,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[7]));

            // #tr NameLowSpeedPerfectOverclockController
            // # Low Speed Perfect Overclock Controller Module
            // #zh_CN 低速无损超频控制器模块
            GTCMItemList.LowSpeedPerfectOverclockController.set(
                new StaticOverclockController(
                    18840,
                    "NameLowSpeedPerfectOverclockController",
                    TSTUtils.tr("NameLowSpeedPerfectOverclockController"),
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
                    TSTUtils.tr("NamePerfectOverclockController"),
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
                    TSTUtils.tr("NameSingularityPerfectOverclockController"),
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
                .set(new ExecutionCore(18843, "NameExecutionCore", TSTUtils.tr("NameExecutionCore"), 12));

            // #tr NameAdvancedExecutionCore
            // # Advanced Execution Core Module
            // #zh_CN 高级执行核心模块
            GTCMItemList.AdvancedExecutionCore.set(
                new AdvExecutionCore(18844, "NameAdvancedExecutionCore", TSTUtils.tr("NameAdvancedExecutionCore"), 13));

            // #tr NamePerfectExecutionCore
            // # Perfect Execution Core Module
            // #zh_CN 完美执行核心模块
            GTCMItemList.PerfectExecutionCore.set(
                new PerfectExecutionCore(
                    18845,
                    "NamePerfectExecutionCore",
                    TSTUtils.tr("NamePerfectExecutionCore"),
                    14));
        }

        // endregion

        if (Config.Enable_BloodHell && Config.Enable_BloodHatch) {
            // #tr NameBloodOrbHatchDebug
            // # Debug Blood Hatch
            // #zh_CN Debug血液仓
            GTCMItemList.BloodOrbHatchDebug.set(
                new TST_BloodOrbHatch.TST_Debug_BloodHatch(
                    18848,
                    "NameBloodOrbHatchDebug",
                    TSTUtils.tr("NameBloodOrbHatchDebug"),
                    4));

        }

    }

    public static void loadMachinePostInit() {
        if (Config.EnableSpaceApiaryModule) {
            // #tr NameSpaceApiaryT1
            // # Space Apiray Module MK-I
            // #zh_CN 太空蜂箱模块 MK-I
            GTCMItemList.SpaceApiaryT1.set(
                new TST_SpaceApiary.TST_SpaceApiaryT1(19042, "NameSpaceApiaryT1", TSTUtils.tr("NameSpaceApiaryT1")));

            // #tr NameSpaceApiaryT2
            // # Space Apiray Module MK-II
            // #zh_CN 太空蜂箱模块 MK-II
            GTCMItemList.SpaceApiaryT2.set(
                new TST_SpaceApiary.TST_SpaceApiaryT2(19043, "NameSpaceApiaryT2", TSTUtils.tr("NameSpaceApiaryT2")));

            // #tr NameSpaceApiaryT3
            // # Space Apiray Module MK-III
            // #zh_CN 太空蜂箱模块 MK-III
            GTCMItemList.SpaceApiaryT3.set(
                new TST_SpaceApiary.TST_SpaceApiaryT3(19044, "NameSpaceApiaryT3", TSTUtils.tr("NameSpaceApiaryT3")));

            // #tr NameSpaceApiaryT4
            // # Space Apiray Module MK-IV
            // #zh_CN 太空蜂箱模块 MK-IV
            GTCMItemList.SpaceApiaryT4.set(
                new TST_SpaceApiary.TST_SpaceApiaryT4(19045, "NameSpaceApiaryT4", TSTUtils.tr("NameSpaceApiaryT4")));
        }

    }
}
