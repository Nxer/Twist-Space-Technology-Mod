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
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_ElvenWorkshopLegacy;
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
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
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
import com.Nxer.TwistSpaceTechnology.common.machine.TST_SwelegfyrBlastFurnaceLegacy;
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
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_EcoSphereInputInterfaceHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_EcoSphereUpgradeInterfaceHatch;
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

// spotless:off
public final class MachineLoader {

    // meta id 19029 has been assigned for AstralComputingArray
    // NuclearReactor = new TST_NuclearReactor(19029, "nucleareactor", "nuclear reactor").getStackForm(1);
    // GTCMItemList.NuclearReactor.set(IndistinctTentacle); // emm 应该是之前测试IndistinctTentacle用的

    public static void loadMachines() {

        if (Config.activateMegaSpaceStation) EntityList.addMapping(Ship.class, "Ship", 114);

        // test
        // new Test_ModularizedMachine(19000, "TestMachine", "TestMachine");

        // region multi Machine controller

        // #tr tst.common.machine.IntensifyChemicalDistorter.name
        // # Intensify Chemical Distorter
        // #zh_CN 深度化学扭曲仪
        GTCMItemList.IntensifyChemicalDistorter.set(
            new GT_TileEntity_IntensifyChemicalDistorter(
                19001,
                "NameIntensifyChemicalDistorter",
                TSTUtils.tr("tst.common.machine.IntensifyChemicalDistorter.name")));

        // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.name
        // # Precise High-Energy Photonic Quantum Master
        // #zh_CN 精密高能光量子掌控者
        GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.set(
            new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(
                19002,
                "NamePreciseHighEnergyPhotonicQuantumMaster",
                TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.name")));

        // #tr tst.common.machine.MiracleTop.name
        // # Miracle Top
        // #zh_CN 奇迹顶点
        GTCMItemList.MiracleTop
            .set(new GT_TileEntity_MiracleTop(19003, "NameMiracleTop", TSTUtils.tr("tst.common.machine.MiracleTop.name")));

        // #tr tst.common.machine.MagneticDrivePressureFormer.name
        // # Magnetic Drive Pressure Former
        // #zh_CN 磁驱压力成型机
        GTCMItemList.MagneticDrivePressureFormer.set(
            new GT_TileEntity_MagneticDrivePressureFormer(
                19004,
                "NameMagneticDrivePressureFormer",
                TSTUtils.tr("tst.common.machine.MagneticDrivePressureFormer.name")));

        // #tr tst.common.machine.PhysicalFormSwitcher.name
        // # Physical Form Switcher
        // #zh_CN 物质形态转换器
        GTCMItemList.PhysicalFormSwitcher.set(
            new GT_TileEntity_PhysicalFormSwitcher(
                19005,
                "NamePhysicalFormSwitcher",
                TSTUtils.tr("tst.common.machine.PhysicalFormSwitcher.name")));

        // #tr tst.common.machine.MagneticMixer.name
        // # "Mini" Magnetic Mixer
        // #zh_CN "小型"磁力搅拌机
        GTCMItemList.MagneticMixer
            .set(new GT_TileEntity_MagneticMixer(19006, "NameMagneticMixer", TSTUtils.tr("tst.common.machine.MagneticMixer.name")));

        // #tr tst.common.machine.MagneticDomainConstructor.name
        // # Magnetic Domain Constructor
        // #zh_CN 磁畴构建器
        GTCMItemList.MagneticDomainConstructor.set(
            new GT_TileEntity_MagneticDomainConstructor(
                19007,
                "NameMagneticDomainConstructor",
                TSTUtils.tr("tst.common.machine.MagneticDomainConstructor.name")));

        // #tr tst.common.machine.Silksong.name
        // # Silksong
        // #zh_CN 丝之歌
        GTCMItemList.Silksong.set(new GT_TileEntity_Silksong(19008, "NameSilksong", TSTUtils.tr("tst.common.machine.Silksong.name")));

        // #tr tst.common.machine.HolySeparator.name
        // # Holy Separator
        // #zh_CN 神圣分离者
        GTCMItemList.HolySeparator
            .set(new GT_TileEntity_HolySeparator(19009, "NameHolySeparator", TSTUtils.tr("tst.common.machine.HolySeparator.name")));

        // #tr tst.common.machine.SpaceScaler.name
        // # Space Scaler
        // #zh_CN 空间缩放仪
        GTCMItemList.SpaceScaler
            .set(new GT_TileEntity_SpaceScaler(19010, "NameSpaceScaler", TSTUtils.tr("tst.common.machine.SpaceScaler.name")));

        // #tr tst.common.machine.MoleculeDeconstructor.name
        // # Molecule Deconstructor
        // #zh_CN 分子解构器
        GTCMItemList.MoleculeDeconstructor.set(
            new GT_TileEntity_MoleculeDeconstructor(
                19011,
                "NameMoleculeDeconstructor",
                TSTUtils.tr("tst.common.machine.MoleculeDeconstructor.name")));

        // #tr tst.common.machine.CrystallineInfinitier.name
        // # Crystalline Infinitier
        // #zh_CN 无限晶胞
        GTCMItemList.CrystallineInfinitier.set(
            new GTCM_CrystallineInfinitier(
                19012,
                "NameCrystallineInfinitier",
                TSTUtils.tr("tst.common.machine.CrystallineInfinitier.name")));

        // #tr tst.dyson.machine.DSPLauncher.name
        // # Dyson Sphere Module Launch Site
        // #zh_CN 戴森球模块发射场
        GTCMItemList.DSPLauncher.set(new TST_DSPLauncher(19013, "NameDSPLauncher", TSTUtils.tr("tst.dyson.machine.DSPLauncher.name")));

        // #tr tst.dyson.machine.DSPReceiver.name
        // # Dyson Sphere Ray Receiving Station
        // #zh_CN 戴森球射线接收站
        GTCMItemList.DSPReceiver.set(new TST_DSPReceiver(19014, "NameDSPReceiver", TSTUtils.tr("tst.dyson.machine.DSPReceiver.name")));

        // #tr tst.dyson.machine.ArtificialStar.name
        // # Artificial Star
        // #zh_CN 人造恒星
        GTCMItemList.ArtificialStar
            .set(new TST_ArtificialStar(19015, "NameArtificialStar", TSTUtils.tr("tst.dyson.machine.ArtificialStar.name")));

        // #tr tst.common.machine.MiracleDoor.name
        // # Miracle Door
        // #zh_CN 奇迹之门
        GTCMItemList.MiracleDoor.set(new TST_MiracleDoor(19016, "NameMiracleDoor", TSTUtils.tr("tst.common.machine.MiracleDoor.name")));

        // #tr tst.common.machine.OreProcessingFactory.name
        // # General Ore Processing Factory TST
        // #zh_CN 通用矿物处理厂TST
        GTCMItemList.OreProcessingFactory.set(
            new TST_OreProcessingFactory(19017, "NameOreProcessingFactory", TSTUtils.tr("tst.common.machine.OreProcessingFactory.name")));

        // #tr tst.common.machine.MegaUniversalSpaceStation.name
        // # Mega Universal Space Station
        // #zh_CN {\RED}寰 {\AQUA}宇 {\GOLD}空 {\BLUE}间 {\DARK_GRAY}站

        // #tr tst.common.machine.ElvenWorkshop.name
        // # ElvenWorkshop
        // #zh_CN 精灵工坊
        GTCMItemList.ElvenWorkshop
            .set(new GTCM_ElvenWorkshop(19018, "NameElvenWorkshop", TSTUtils.tr("tst.common.machine.ElvenWorkshop.name")));
        // TODO: Remove the transitional controller (19299) in the next version.
        GTCMItemList.ElvenWorkshopLegacy
            .set(new GTCM_ElvenWorkshopLegacy(19299, "NameElvenWorkshopLegacy", TSTUtils.tr("tst.common.machine.ElvenWorkshop.name")));

        // #tr tst.common.machine.HyperSpacetimeTransformer.name
        // # HyperSpacetimeTransformer
        // #zh_CN 极限时空转换仪
        GTCMItemList.HyperSpacetimeTransformer.set(
            new GTCM_HyperSpacetimeTransformer(
                19019,
                "NameHyperSpacetimeTransformer",
                TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.name")));

        // #tr tst.common.machine.CircuitConverter.name
        // # General Circuit Converter
        // #zh_CN 通用电路板转换器
        GTCMItemList.CircuitConverter
            .set(new TST_CircuitConverter(19020, "NameCircuitConverter", TSTUtils.tr("tst.common.machine.CircuitConverter.name")));

        // #tr tst.common.machine.LargeIndustrialCokingFactory.name
        // # Large Industrial Coking Factory
        // #zh_CN 大型工业炼焦厂
        GTCMItemList.LargeIndustrialCokingFactory.set(
            new TST_LargeIndustrialCokingFactory(
                19021,
                "NameLargeIndustrialCokingFactory",
                TSTUtils.tr("tst.common.machine.LargeIndustrialCokingFactory.name")));

        // #tr tst.common.machine.MegaBrickedBlastFurnace.name
        // # Mega Bricked Blast Furnace
        // #zh_CN 巨型砖高炉
        GTCMItemList.MegaBrickedBlastFurnace.set(
            new GT_TileEntity_MegaBrickedBlastFurnace(
                19022,
                "NameMegaBrickedBlastFurnace",
                TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.name")));

        // #tr tst.common.machine.Scavenger.name
        // # Scavenger
        // #zh_CN 拾荒者
        GTCMItemList.Scavenger.set(new TST_Scavenger(19023, "NameScavenger", TSTUtils.tr("tst.common.machine.Scavenger.name")));

        // #tr tst.common.machine.TSTcleanroom.name
        // # CleanRoom
        // #zh_CN TST超净间
        GTCMItemList.superCleanRoom
            .set(new TST_CleanRoom(19024, "NameTSTcleanroom", TSTUtils.tr("tst.common.machine.TSTcleanroom.name")));

        // #tr tst.common.machine.BiosphereIII.name
        // # Biosphere III
        // #zh_CN 生物圈III号
        GTCMItemList.BiosphereIII.set(new TST_BiosphereIII(19025, "nameBiosphereIII", TSTUtils.tr("tst.common.machine.BiosphereIII.name")));

        // #tr tst.common.machine.MegaEggGenerator.name
        // # Tower of Abstraction
        // #zh_CN 抽象之塔
        GTCMItemList.MegaEggGenerator.set(
            new GT_TileEntity_MegaEggGenerator(19026, "NameMegaEggGenerator", TSTUtils.tr("tst.common.machine.MegaEggGenerator.name")));

        // #tr tst.common.machine.AdvancedMegaOilCracker.name
        // # Advanced Mega Oil Cracker
        // #zh_CN 进阶巨型石油裂化机
        GTCMItemList.AdvancedMegaOilCracker.set(
            new TST_AdvancedMegaOilCracker(
                19027,
                "NameAdvancedMegaOilCracker",
                TSTUtils.tr("tst.common.machine.AdvancedMegaOilCracker.name")));

        // #tr tst.common.machine.IndistinctTentacle.name
        // # {\BOLD}{\DARK_GRAY}Indistinct Tentacle
        // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}
        GTCMItemList.IndistinctTentacle
            .set(new TST_IndistinctTentacle(19028, "NameIndistinctTentacle", TSTUtils.tr("tst.common.machine.IndistinctTentacle.name")));

        // #tr tst.common.machine.AstralComputingArray.name
        // # Astral Computing Array
        // #zh_CN 星规阵列
        GTCMItemList.AstralComputingArray
            .set(new TST_Computer(19029, "NameAstralComputingArray", TSTUtils.tr("tst.common.machine.AstralComputingArray.name")));

        // #tr tst.common.machine.ThermalEnergyDevourer.name
        // # Thermal Energy Devourer
        // #zh_CN 热能饕餮
        GTCMItemList.ThermalEnergyDevourer.set(
            new TST_ThermalEnergyDevourer(
                19030,
                "NameThermalEnergyDevourer",
                TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.name")));

        // #tr tst.common.machine.VacuumFilterExtractor.name
        // # Vacuum Filter Extractor
        // #zh_CN 真空抽滤器
        GTCMItemList.VacuumFilterExtractor.set(
            new TST_VacuumFilterExtractor(
                19031,
                "NameVacuumFilterExtractor",
                TSTUtils.tr("tst.common.machine.VacuumFilterExtractor.name")));

        // #tr tst.common.machine.LargeSteamForgeHammer.name
        // # Large Steam Forge Hammer
        // #zh_CN 大型蒸汽锻造锤
        GTCMItemList.LargeSteamForgeHammer.set(
            new TST_LargeSteamForgeHammer(
                19032,
                "NameLargeSteamForgeHammer",
                TSTUtils.tr("tst.common.machine.LargeSteamForgeHammer.name")));

        // #tr tst.common.machine.LargeSteamAlloySmelter.name
        // # Large Steam Alloy Smelter
        // #zh_CN 大型蒸汽合金炉
        GTCMItemList.LargeSteamAlloySmelter.set(
            new TST_LargeSteamAlloySmelter(
                19033,
                "NameLargeSteamAlloySmelter",
                TSTUtils.tr("tst.common.machine.LargeSteamAlloySmelter.name")));

        // #tr tst.common.machine.EyeOfWood.name
        // # Eye of Wood
        // #zh_CN 武德之眼
        GTCMItemList.EyeOfWood.set(new TST_EyeOfWood(19034, "NameEyeOfWood", TSTUtils.tr("tst.common.machine.EyeOfWood.name")));

        // #tr tst.common.machine.BeeEngineer.name
        // # Bee Engineer (Prototype)
        // #zh_CN 蜜蜂操纵者 (Prototype)
        GTCMItemList.BeeEngineer.set(new TST_BeeEngineer(19035, "NameBeeEngineer", TSTUtils.tr("tst.common.machine.BeeEngineer.name")));

        // #tr tst.common.machine.MegaMacerator.name
        // # "Mini" Household Cell Fragmentizer
        // #zh_CN "小型"家用破壁机
        GTCMItemList.MegaMacerator
            .set(new TST_MegaMacerator(19036, "NameMegaMacerator", TSTUtils.tr("tst.common.machine.MegaMacerator.name")));

        // #tr tst.common.machine.HephaestusAtelier.name
        // # Hephaestus' Atelier
        // #zh_CN 赫菲斯托斯的工坊
        GTCMItemList.HephaestusAtelier
            .set(new TST_HephaestusAtelier(19037, "NameHephaestusAtelier", TSTUtils.tr("tst.common.machine.HephaestusAtelier.name")));

        if (Config.Enable_DeployedNanoCore) {
            // #tr tst.common.machine.DeployedNanoCore.name
            // # Deployed Nano Core
            // #zh_CN 展开的纳米核心
            GTCMItemList.DeployedNanoCore
                .set(new TST_DeployedNanoCore(19038, "NameDeployedNanoCore", TSTUtils.tr("tst.common.machine.DeployedNanoCore.name")));
        }

        if (Config.Enable_CoreDeviceOfHumanPowerGenerationFacility) {
            // #tr tst.common.machine.CoreDeviceOfHumanPowerGenerationFacility.name
            // # Core Device of Human Power Generation Facility
            // #zh_CN 人类能源设施的核心装置
            GTCMItemList.CoreDeviceOfHumanPowerGenerationFacility.set(
                new TST_CoreDeviceOfHumanPowerGenerationFacility(
                    19039,
                    "NameCoreDeviceOfHumanPowerGenerationFacility",
                    TSTUtils.tr("tst.common.machine.CoreDeviceOfHumanPowerGenerationFacility.name")));
        }

        if (Config.Enable_StarcoreMiner) {
            GTCMItemList.StarcoreMiner.set(
                new TST_StarcoreMiner(
                    19040,
                    "NameStarcoreMiner",
                    // #tr tst.common.machine.StarcoreMiner.name
                    // # Starcore Miner
                    // #zh_CN 星核钻机
                    TSTUtils.tr("tst.common.machine.StarcoreMiner.name")));
        }

        if (Config.Enable_Disassembler) {
            GTCMItemList.Disassembler.set(
                new TST_Disassembler(
                    19041,
                    // #tr tst.common.machine.TSTDisassembler.name
                    // # TST Large Disassembler
                    // #zh_CN TST大型拆解机
                    "NameTSTDisassembler",
                    TSTUtils.tr("tst.common.machine.TSTDisassembler.name")));
        }

        if (Config.Enable_BallLightning) {
            // #tr tst.common.machine.BallLightning.name
            // # BallLightning
            // #zh_CN 球状闪电
            GTCMItemList.BallLightning
                .set(new TST_BallLightning(19046, "NameBallLightning", TSTUtils.tr("tst.common.machine.BallLightning.name")));
        }

        if (Config.Enable_IndustrialMagicMatrix) {
            GTCMItemList.IndustrialMagicMatrix.set(
                new GT_TileEntity_IndustrialMagicMatrix(
                    19047,
                    "IndustrialMagicMatrix",
                    // #tr tst.common.machine.IndustrialMagicMatrix.name
                    // # Industrial Magic Matrix
                    // #zh_CN §0工业注魔矩阵
                    TSTUtils.tr("tst.common.machine.IndustrialMagicMatrix.name")));
        }

        if (Config.Enable_LargeCanner) {
            GTCMItemList.LargeCanner.set(
                new TST_LargeCanner(
                    19048,
                    "NameLargeCanner",
                    // #tr tst.common.machine.LargeCanner.name
                    // # Large Canner
                    // #zh_CN 大型装罐机
                    TSTUtils.tr("tst.common.machine.LargeCanner.name")));
        }

        // #tr tst.common.machine.BigBroArray.name
        // # MegaArray
        // #zh_CN 大哥阵列
        GTCMItemList.BigBroArray.set(new TST_BigBroArray(19049, "BigBroArray.name", TSTUtils.tr("tst.common.machine.BigBroArray.name")));

        if (Config.Enable_IndustrialMagnetarSeparator) {
            GTCMItemList.IndustrialMagnetarSeparator.set(
                new TST_IndustrialMagnetarSeparator(
                    19050,
                    "NameIndustrialMagnetarSeparator",
                    // #tr tst.common.machine.IndustrialMagnetarSeparator.name
                    // # Industrial Magnetar Separator
                    // #zh_CN 工业电磁离析机
                    TSTUtils.tr("tst.common.machine.IndustrialMagnetarSeparator.name")));
        }

        if (Config.Enable_EcoSphereSimulator) {
            GTCMItemList.EcoSphereSimulator.set(
                new TST_EcoSphereSimulator(
                    19051,
                    "NameEcoSphereSimulator",
                    // #tr tst.ecosphere.machine.EcoSphereSimulator.name
                    // # Eco-Sphere Simulator
                    // #zh_CN 拟似生态圈
                    TSTUtils.tr("tst.ecosphere.machine.EcoSphereSimulator.name")));

            // #tr tst.ecosphere.machine.EcoSphereInputInterface.name
            // # Eco-Sphere Input Interface
            // #zh_CN 生态圈输入接口
            GTCMItemList.EcoSphereInputInterface.set(
                new TST_EcoSphereInputInterfaceHatch(
                    18849,
                    "NameEcoSphereInputInterface",
                    TSTUtils.tr("tst.ecosphere.machine.EcoSphereInputInterface.name"),
                    10));

            // #tr tst.ecosphere.machine.EcoSphereUpgradeInterface.name
            // # Eco-Sphere Upgrade Interface
            // #zh_CN 生态圈升级接口
            GTCMItemList.EcoSphereUpgradeInterface.set(
                new TST_EcoSphereUpgradeInterfaceHatch(
                    18850,
                    "NameEcoSphereUpgradeInterface",
                    TSTUtils.tr("tst.ecosphere.machine.EcoSphereUpgradeInterface.name"),
                    10));
        }

        // #tr tst.common.machine.ExtremeCraftCenter.name
        // # Extreme Crafting Center
        // #zh_CN 梦魇工业合成中心
        GTCMItemList.ExtremeCraftCenter
            .set(new TST_MegaCraftingCenter(19052, "NameExtremeCraftCenter", TSTUtils.tr("tst.common.machine.ExtremeCraftCenter.name")));

        // #tr tst.common.machine.PatternAccessHatch.name
        // # Pattern Access Hatch
        // #zh_CN 样板访问仓
        GTCMItemList.PatternAccessHatch
            .set(new TST_PatternAccessHatch(18847, "NamePatternAccessHatch", TSTUtils.tr("tst.common.machine.PatternAccessHatch.name"), 9));

        if (Config.Enable_LightningSpire) {
            GTCMItemList.LightningSpire.set(
                new GTCM_LightningSpire(
                    19053,
                    "NameLightningSpire",
                    // #tr tst.common.machine.LightningSpire.name
                    // # Lightning Spire
                    // #zh_CN 闪电尖塔
                    TSTUtils.tr("tst.common.machine.LightningSpire.name")));
        }

        if (Config.EnableModularizedMachineSystem) {
            if (Config.EnableDimensionallyTranscendentMatterPlasmaForgePrototypeMK2) {
                // #tr tst.modular.machine.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2.name
                // # Dimensionally Transcendent Matter Plasma Forge Prototype MK-II
                // #zh_CN 超维度物质等离子锻炉原型机MK-II
                GTCMItemList.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2.set(
                    new MM_DimensionallyTranscendentMatterPlasmaForgePrototypeMK2(
                        19054,
                        "NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2",
                        TSTUtils.tr("tst.modular.machine.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2.name")));
            }

            if (Config.EnableLargeNeutronOscillator) {
                // #tr tst.modular.machine.LargeNeutronOscillator.name
                // # Large Neutron Oscillator
                // #zh_CN 大型中子振荡器
                GTCMItemList.LargeNeutronOscillator.set(
                    new MM_LargeNeutronOscillator(
                        19055,
                        "NameLargeNeutronOscillator",
                        TSTUtils.tr("tst.modular.machine.LargeNeutronOscillator.name")));
            }

            if (Config.EnableRecipeRegistry_IndistinctTentacle) {
                // #tr tst.modular.machine.IndistinctTentaclePrototypeMK2.name
                // # {\DARK_GRAY}{\BOLD}Indistinct Tentacle {\RESET}Prototype MK-II
                // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}原型机MK-II
                GTCMItemList.IndistinctTentaclePrototypeMK2.set(
                    new MM_IndistinctTentaclePrototypeMK2(
                        19056,
                        "NameIndistinctTentaclePrototypeMK2",
                        TSTUtils.tr("tst.modular.machine.IndistinctTentaclePrototypeMK2.name")));
            }

            // #tr tst.modular.machine.MassFabricatorGenesis.name
            // # Mass Fabricator : Genesis
            // #zh_CN 质量发生器 : 创世纪
            GTCMItemList.MassFabricatorGenesis.set(
                new MM_MassFabricatorGenesis(
                    19057,
                    "NameMassFabricatorGenesis",
                    TSTUtils.tr("tst.modular.machine.MassFabricatorGenesis.name")));

        }

        if (Config.Enable_IncompactCyclotron) {
            // #tr tst.common.machine.IncompactCyclotron.name
            // # PULSAR - Incompact Cyclotron
            // #zh_CN PULSAR - 非紧凑式回旋加速器
            GTCMItemList.IncompactCyclotron.set(
                new TST_IncompactCyclotron(19058, "NameIncompactCyclotron", TSTUtils.tr("tst.common.machine.IncompactCyclotron.name")));
        }

        if (Config.EnableModularizedMachineSystem) {
            // #tr tst.dyson.machine.StrangeMatterAggregator.name
            // # Strange Matter Aggregator
            // #zh_CN 奇异物质聚合器
            GTCMItemList.StrangeMatterAggregator.set(
                new TST_StrangeMatterAggregator(
                    19059,
                    "NameStrangeMatterAggregator",
                    TSTUtils.tr("tst.dyson.machine.StrangeMatterAggregator.name")));

        }

        // #tr tst.common.machine.MicroSpaceTimeFabricatorio.name
        // # Micro SpaceTime Fabricatorio
        // #zh_CN 微型时空发生器
        GTCMItemList.MicroSpaceTimeFabricatorio.set(
            new TST_MicroSpaceTimeFabricatorio(
                19060,
                "NameMicroSpaceTimeFabricatorio",
                TSTUtils.tr("tst.common.machine.MicroSpaceTimeFabricatorio.name")));

        if (Config.Enable_BloodHell) {
            // #tr tst.common.machine.BloodyHell.name
            // # Bloody Hell
            // #zh_CN 血狱
            GTCMItemList.BloodyHell.set(new TST_BloodyHell(19061, "NameBloodyHell", TSTUtils.tr("tst.common.machine.BloodyHell.name")));

            if (Config.Enable_BloodHatch) {
                // #tr tst.common.machine.BloodOrbHatch.name
                // # Blood Hatch
                // #zh_CN 血液仓
                GTCMItemList.BloodOrbHatch
                    .set(new TST_BloodOrbHatch(18846, "NameBloodOrbHatch", TSTUtils.tr("tst.common.machine.BloodOrbHatch.name"), 4));

            }
        }

        if (Enable_MegaStoneBreaker) {
            // #tr tst.common.machine.MegaStoneBreaker.name
            // # Silicon Rock Synthesizer
            // #zh_CN 硅岩制造机
            GTCMItemList.MegaStoneBreaker
                .set(new TST_MegaStoneBreaker(19062, "NameMegaStoneBreaker", TSTUtils.tr("tst.common.machine.MegaStoneBreaker.name")));
        }

        // #tr tst.common.machine.ManufacturingCenter.name
        // # Manufacturing Center
        // #zh_CN 加工中心
        GTCMItemList.ManufacturingCenter
            .set(new TST_ManufacturingCenter(19063, "NameManufacturingCenter", TSTUtils.tr("tst.common.machine.ManufacturingCenter.name")));

        if (Enable_IndustrialAlchemyTower) {
            GTCMItemList.IndustrialAlchemyTower.set(
                new TST_IndustrialAlchemyTower(
                    // #tr tst.common.machine.IndustrialAlchemyTower.name
                    // # Industrial Alchemy Tower
                    // #zh_CN 工业炼金塔
                    19064,
                    "IndustrialAlchemyTower",
                    TSTUtils.tr("tst.common.machine.IndustrialAlchemyTower.name")));
        }

        // #tr tst.common.machine.GiantVacuumDryingFurnace.name
        // # Giant Vacuum Drying Furnace
        // #zh_CN 巨型真空干燥炉
        if (Enable_GiantVacuumDryingFurnace) {
            GTCMItemList.GiantVacuumDryingFurnace.set(
                new TST_GiantVacuumDryingFurnace(
                    19065,
                    "GiantVacuumDryingFurnace",
                    TSTUtils.tr("tst.common.machine.GiantVacuumDryingFurnace.name")));
        }

        if (Config.Enable_ProcessingArray) {
            // #tr tst.common.machine.ProcessingArray.name
            // # TST Processing Array
            // #zh_CN TST处理阵列
            GTCMItemList.ProcessingArray
                .set(new TST_ProcessingArray(19066, "NameProcessingArray", TSTUtils.tr("tst.common.machine.ProcessingArray.name")));
        }

        if (Config.Enable_AdvCircuitAssemblyLine) {
            // #tr tst.common.machine.AdvCircuitAssemblyLine.name
            // # Advanced Circuit Assembly Line
            // #zh_CN 进阶电路装配线
            GTCMItemList.AdvCircuitAssemblyLine.set(
                new TST_AdvCircuitAssemblyLine(
                    19067,
                    "NameAdvCircuitAssemblyLine",
                    TSTUtils.tr("tst.common.machine.AdvCircuitAssemblyLine.name")));
        }

        if (Config.Enable_SwelegfyrBlastFurnace) {
            // #tr tst.common.machine.SwelegfyrBlastFurnace.name
            // # Swelegfyr Blast Furnace
            // #zh_CN 熯焱高炉
            GTCMItemList.SwelegfyrBlastFurnace.set(
                new TST_SwelegfyrBlastFurnace(
                    19068,
                    "NameSwelegfyrBlastFurnace",
                    TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.name")));
            // TODO: Remove the transitional controller (19298) in the next version.
            GTCMItemList.SwelegfyrBlastFurnaceLegacy.set(
                new TST_SwelegfyrBlastFurnaceLegacy(
                    19298,
                    "NameSwelegfyrBlastFurnaceLegacy",
                    TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.name")));
        }

        if (Config.Enable_HyperThermalConvector) {
            // #tr tst.common.machine.HyperThermalConvector.name
            // # Hyper Thermal Convector
            // #zh_CN 高能态热对流器
            GTCMItemList.HyperThermalConvector.set(
                new TST_HyperThermalConvector(
                    19069,
                    "NameHyperThermalConvector",
                    TSTUtils.tr("tst.common.machine.HyperThermalConvector.name")));
        }

        if (Config.Enable_PrimordialDisjunctus) {
            // #tr tst.common.machine.PrimordialDisjunctus.name
            // # Primordial Disjunctus
            // #zh_CN 初源解离机
            GTCMItemList.PrimordialDisjunctus.set(
                new TST_PrimordialDisjunctus(19070, "PrimordialDisjunctus", TSTUtils.tr("tst.common.machine.PrimordialDisjunctus.name")));
        }

        if (Config.Enable_SkypiercerTower) {
            // #tr tst.common.machine.SkypiercerTower.name
            // # Skypiercer Tower
            // #zh_CN 穿云尖塔
            GTCMItemList.SkypiercerTower
                .set(new TST_SkypiercerTower(19071, "NameSkypiercerTower", TSTUtils.tr("tst.common.machine.SkypiercerTower.name")));
        }

        if (Config.Enable_LaserMeteorMiner) {
            // #tr tst.common.machine.MeteorMiner.name
            // # Laser Meteor Miner
            // #zh_CN 激光陨星采矿场
            GTCMItemList.MeteorMiner
                .set(new TST_LaserMeteorMiner(19072, "NameMeteorMiner", TSTUtils.tr("tst.common.machine.MeteorMiner.name")));
        }

        // #tr tst.common.machine.SteamBasicGenerator.name
        // # Basic Steam Generator
        // #zh_CN 基础蒸汽发电机
        GTCMItemList.SteamBasicGenerator
            .set(new TST_SteamBasicGenerator(19073, "NameSteamBasicGenerator", TSTUtils.tr("tst.common.machine.SteamBasicGenerator.name")));

        // #tr tst.common.machine.UniversalGenerator.name
        // # Universal Generator
        // #zh_CN 通用发电机
        GTCMItemList.UniversalGenerator
            .set(new TST_UniversalGenerator(19074, "NameUniversalGenerator", TSTUtils.tr("tst.common.machine.UniversalGenerator.name")));

        if (Config.Enable_InfusionMaterialDispenser) {
            // #tr tst.common.machine.InfusionMaterialDispenser.name
            // # Infusion Material Dispenser
            // #zh_CN 注魔分配器
            GTCMItemList.InfusionMaterialDispenser.set(
                new TST_InfusionMaterialDispenser(
                    19075,
                    "NameInfusionMaterialDispenser",
                    TSTUtils.tr("tst.common.machine.InfusionMaterialDispenser.name")));
        }

        // #tr tst.common.machine.LargeSolarBoiler.name
        // # Large Solar Boiler
        // #zh_CN 大型太阳能锅炉
        GTCMItemList.LargeSolarBoiler
            .set(new TST_LargeSolarBoiler(19076, "NameLargeSolarBoiler", TSTUtils.tr("tst.common.machine.LargeSolarBoiler.name")));

        // #tr tst.common.machine.NetherInterface.name
        // # Nether Interface
        // #zh_CN 地狱接口
        GTCMItemList.NetherInterface
            .set(new TST_NetherInterface(19077, "NameNetherInterface", TSTUtils.tr("tst.common.machine.NetherInterface.name")));

        // #tr tst.common.machine.SuperWaterPurifier.name
        // # Super Water Purifier
        // #zh_CN 超净水生成器
        GTCMItemList.SuperWaterPurifier
            .set(new TST_SuperWaterPurifier(19078, "NameSuperWaterPurifier", TSTUtils.tr("tst.common.machine.SuperWaterPurifier.name")));

        // #tr tst.common.machine.IntegratedAssemblyMatrix.name
        // # Integrated Assembly Matrix
        // #zh_CN 集成装配矩阵
        GTCMItemList.IntegratedAssemblyMatrix.set(
            new TST_IntegratedAssemblyMatrix(
                19079,
                "NameIntegratedAssemblyMatrix",
                TSTUtils.tr("tst.common.machine.IntegratedAssemblyMatrix.name")));

        // #tr tst.common.machine.MegaSolarPanelFactory.name
        // # Mega Solar Panel Factory
        // #zh_CN 巨型太阳能板工厂
        GTCMItemList.MegaSolarPanelFactory.set(
            new TST_MegaSolarPanelFactory(
                19080,
                "NameMegaSolarPanelFactory",
                TSTUtils.tr("tst.common.machine.MegaSolarPanelFactory.name")));

        // #tr tst.common.machine.MegaNqReactor.name
        // # Mega Naquadah Reactor
        // #zh_CN 巨型硅岩反应堆
        GTCMItemList.MegaNqReactor
            .set(new TST_MegaNqReactor(19081, "NameMegaNqReactor", TSTUtils.tr("tst.common.machine.MegaNqReactor.name")));

        // endregion

        // region Single block Machine

        // #tr tst.common.machine.InfiniteAirHatch.name
        // # Infinite Air Hatch
        // #zh_CN 无限进气仓
        GTCMItemList.InfiniteAirHatch.set(
            new GT_MetaTileEntity_Hatch_Air(18999, "NameInfiniteAirHatch", TSTUtils.tr("tst.common.machine.InfiniteAirHatch.name"), 9));

        // #tr tst.common.machine.InfiniteWirelessDynamoHatch.name
        // # Infinite Wireless Dynamo Hatch
        // #zh_CN 无限无线动力仓
        GTCMItemList.InfiniteWirelessDynamoHatch.set(
            new GT_Hatch_InfiniteWirelessDynamoHatch(
                18998,
                "NameInfiniteWirelessDynamoHatch",
                TSTUtils.tr("tst.common.machine.InfiniteWirelessDynamoHatch.name"),
                14));

        // #tr tst.common.machine.ManaHatch.name
        // # Mana Hatch
        // #zh_CN Mana Hatch
        GTCMItemList.ManaHatch.set(new TST_ManaHatch(18979, "NameManaHatch", TSTUtils.tr("tst.common.machine.ManaHatch.name"), 9));

        // #tr tst.common.machine.AEStorageCellInputBus.name
        // # Super Stocking Input Bus (ME)
        // #zh_CN 超级存储输入总线(ME)
        GTCMItemList.AEStorageCellInputBus.set(
            new TST_AEStorageCellInputBus(
                18851,
                "NameAEStorageCellInputBus",
                TSTUtils.tr("tst.common.machine.AEStorageCellInputBus.name"),
                9));

        // #tr tst.common.machine.AEStorageCellInputHatch.name
        // # Super Stocking Input Hatch (ME)
        // #zh_CN 超级存储输入仓(ME)
        GTCMItemList.AEStorageCellInputHatch.set(
            new TST_AEStorageCellInputHatch(
                18852,
                "NameAEStorageCellInputHatch",
                TSTUtils.tr("tst.common.machine.AEStorageCellInputHatch.name"),
                9));

        // region Dual Input Buffer
        // #tr tst.common.machine.DualInputBuffer.tier.iv.name
        // # Dual Input Buffer (IV)
        // #zh_CN 输入总成 (IV)
        GTCMItemList.DualInputBuffer_IV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18980,
                "NameDualInputBuffer_IV",
                TSTUtils.tr("tst.common.machine.DualInputBuffer.tier.iv.name"),
                5));

        // #tr tst.common.machine.DualInputBuffer.tier.luv.name
        // # Dual Input Buffer (LuV)
        // #zh_CN 输入总成 (LuV)
        GTCMItemList.DualInputBuffer_LuV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18981,
                "NameDualInputBuffer_LuV",
                TSTUtils.tr("tst.common.machine.DualInputBuffer.tier.luv.name"),
                6));

        // #tr tst.common.machine.DualInputBuffer.tier.zpm.name
        // # Dual Input Buffer (ZPM)
        // #zh_CN 输入总成 (ZPM)
        GTCMItemList.DualInputBuffer_ZPM.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18982,
                "NameDualInputBuffer_ZPM",
                TSTUtils.tr("tst.common.machine.DualInputBuffer.tier.zpm.name"),
                7));

        // #tr tst.common.machine.DualInputBuffer.tier.uv.name
        // # Dual Input Buffer (UV)
        // #zh_CN 输入总成 (UV)
        GTCMItemList.DualInputBuffer_UV.set(
            new GT_MetaTileEntity_Hatch_DualInput(
                18983,
                "NameDualInputBuffer_UV",
                TSTUtils.tr("tst.common.machine.DualInputBuffer.tier.uv.name"),
                8));

        // region buffered energy hatch

        // #tr tst.common.machine.BufferedEnergyHatch.tier.lv.name
        // # Buffered Energy Hatch LV
        // #zh_CN 缓存能源仓LV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.mv.name
        // # Buffered Energy Hatch MV
        // #zh_CN 缓存能源仓MV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.hv.name
        // # Buffered Energy Hatch HV
        // #zh_CN 缓存能源仓HV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.ev.name
        // # Buffered Energy Hatch EV
        // #zh_CN 缓存能源仓EV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.iv.name
        // # Buffered Energy Hatch IV
        // #zh_CN 缓存能源仓IV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.luv.name
        // # Buffered Energy Hatch LuV
        // #zh_CN 缓存能源仓LuV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.zpm.name
        // # Buffered Energy Hatch ZPM
        // #zh_CN 缓存能源仓ZPM

        // #tr tst.common.machine.BufferedEnergyHatch.tier.uv.name
        // # Buffered Energy Hatch UV
        // #zh_CN 缓存能源仓UV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.uhv.name
        // # Buffered Energy Hatch UHV
        // #zh_CN 缓存能源仓UHV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.uev.name
        // # Buffered Energy Hatch UEV
        // #zh_CN 缓存能源仓UEV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.uiv.name
        // # Buffered Energy Hatch UIV
        // #zh_CN 缓存能源仓UIV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.umv.name
        // # Buffered Energy Hatch UMV
        // #zh_CN 缓存能源仓UMV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.uxv.name
        // # Buffered Energy Hatch UXV
        // #zh_CN 缓存能源仓UXV

        // #tr tst.common.machine.BufferedEnergyHatch.tier.max.name
        // # Buffered Energy Hatch MAX
        // #zh_CN 缓存能源仓MAX
        GTCMItemList.BufferedEnergyHatchLV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18984,
                "NameBufferedEnergyHatchLV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.lv.name"),
                1,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchMV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18985,
                "NameBufferedEnergyHatchMV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.mv.name"),
                2,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchHV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18986,
                "NameBufferedEnergyHatchHV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.hv.name"),
                3,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchEV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18987,
                "NameBufferedEnergyHatchEV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.ev.name"),
                4,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchIV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18988,
                "NameBufferedEnergyHatchIV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.iv.name"),
                5,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchLuV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18989,
                "NameBufferedEnergyHatchLuV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.luv.name"),
                6,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchZPM.set(
            new GT_Hatch_BufferedEnergyHatch(
                18990,
                "NameBufferedEnergyHatchZPM",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.zpm.name"),
                7,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18991,
                "NameBufferedEnergyHatchUV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.uv.name"),
                8,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUHV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18992,
                "NameBufferedEnergyHatchUHV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.uhv.name"),
                9,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUEV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18993,
                "NameBufferedEnergyHatchUEV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.uev.name"),
                10,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUIV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18994,
                "NameBufferedEnergyHatchUIV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.uiv.name"),
                11,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUMV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18995,
                "NameBufferedEnergyHatchUMV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.umv.name"),
                12,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchUXV.set(
            new GT_Hatch_BufferedEnergyHatch(
                18996,
                "NameBufferedEnergyHatchUXV",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.uxv.name"),
                13,
                16,
                null));
        GTCMItemList.BufferedEnergyHatchMAX.set(
            new GT_Hatch_BufferedEnergyHatch(
                18997,
                "NameBufferedEnergyHatchMAX",
                TSTUtils.tr("tst.common.machine.BufferedEnergyHatch.tier.max.name"),
                14,
                16,
                null));

        // #tr tst.common.machine.DebugUncertaintyHatch.name
        // # Debug Uncertainty Hatch
        // #zh_CN Debug未定元解析器
        GTCMItemList.DebugUncertaintyHatch.set(
            new GT_MetaTileEntity_Hatch_UncertaintyDebug(
                18978,
                "NameDebugUncertaintyHatch",
                TSTUtils.tr("tst.common.machine.DebugUncertaintyHatch.name"),
                12));

        // #tr tst.common.machine.LaserSmartNode.name
        // # Laser Smart Node
        // #zh_CN 激光智能节点
        GTCMItemList.LaserSmartNode.set(
            new GT_MetaTileEntity_Pipe_EnergySmart(18960, "NameLaserSmartNode", TSTUtils.tr("tst.common.machine.LaserSmartNode.name")));

        // #tr tst.common.machine.LaserFocusedSmartNode.name
        // # Laser-Focused Smart Node
        // #zh_CN 激光聚焦智能节点
        GTCMItemList.LaserFocusedSmartNode.set(
            new GT_MetaTileEntity_Pipe_EnergySmart_Focusing(
                18961,
                "NameLaserFocusedSmartNode",
                TSTUtils.tr("tst.common.machine.LaserFocusedSmartNode.name")));
        // endregion

        // #tr tst.common.machine.FackRackHatch.name
        // # rack simulation hack
        // #zh_CN 机箱模拟器
        GTCMItemList.FackRackHatch.set(
            new GT_Hatch_RackComputationMonitor(
                18959,
                "NameFackRackHatch",
                TSTUtils.tr("tst.common.machine.FackRackHatch.name"),
                12,
                false));

        // #tr tst.common.machine.RealRackHatch.name
        // # rack simulation controller hack
        // #zh_CN 机箱控制器
        GTCMItemList.RealRackHatch.set(
            new GT_Hatch_RackComputationMonitor(
                18958,
                "NameRealRackHatch",
                TSTUtils.tr("tst.common.machine.RealRackHatch.name"),
                12,
                true));

        // #tr tst.common.machine.WirelessDataInputHatch.name
        // # Wireless Optical Slave Connector
        // #zh_CN 无线副光学接口
        GTCMItemList.WirelessDataInputHatch.set(
            new GT_Hatch_WirelessData_input(
                18957,
                "NameWirelessDataInputHatch",
                TSTUtils.tr("tst.common.machine.WirelessDataInputHatch.name"),
                12));

        // #tr tst.common.machine.WirelessDataOutputHatch.name
        // # Wireless Optical Master Connector
        // #zh_CN 无线主光学接口
        GTCMItemList.WirelessDataOutputHatch.set(
            new GT_Hatch_WirelessData_output(
                18956,
                "NameWirelessDataOutputHatch",
                TSTUtils.tr("tst.common.machine.WirelessDataOutputHatch.name"),
                12));

        // #tr tst.common.machine.LegendaryWirelessEnergyHatch.name
        // # Legendary Wireless Energy Hatch
        // #zh_CN 传奇无线能源仓
        GTCMItemList.LegendaryWirelessEnergyHatch.set(
            new GT_Hatch_InfiniteWirelessMulti(
                18798,
                "NameLegendaryWirelessEnergyHatch",
                TSTUtils.tr("tst.common.machine.LegendaryWirelessEnergyHatch.name"),
                13,
                536870912));

        // #tr tst.common.machine.HarmoniousWirelessEnergyHatch.name
        // # Harmonious Wireless Energy Hatch
        // #zh_CN 鸿蒙无线能源仓
        GTCMItemList.HarmoniousWirelessEnergyHatch.set(
            new GT_Hatch_InfiniteWirelessMulti(
                18799,
                "NameHarmoniousWirelessEnergyHatch",
                TSTUtils.tr("tst.common.machine.HarmoniousWirelessEnergyHatch.name"),
                14,
                2147483647));

        // #tr tst.common.machine.SolidifyHatch.tier.uhv.name
        // # Solidifier Hatch(UHV)
        // #zh_CN 固化仓(UHV)
        GTCMItemList.SolidifyHatch_UHV.set(
            new GT_MetaTileEntity_Hatch_Solidify(
                18797,
                "NameSolidifyHatchUHV",
                TSTUtils.tr("tst.common.machine.SolidifyHatch.tier.uhv.name"),
                9));

        // #tr tst.common.machine.SolidifyHatch.tier.uv.name
        // # Solidifier Hatch(UV)
        // #zh_CN 固化仓(UV)
        GTCMItemList.SolidifyHatch_UV.set(
            new GT_MetaTileEntity_Hatch_Solidify(18796, "NameSolidifyHatchUV", TSTUtils.tr("tst.common.machine.SolidifyHatch.tier.uv.name"), 8));

        // #tr tst.common.machine.SolidifyHatch.tier.zpm.name
        // # Solidifier Hatch(ZPM)
        // #zh_CN 固化仓(ZPM)
        GTCMItemList.SolidifyHatch_ZPM.set(
            new GT_MetaTileEntity_Hatch_Solidify(
                18795,
                "NameSolidifyHatchZPM",
                TSTUtils.tr("tst.common.machine.SolidifyHatch.tier.zpm.name"),
                7));

        // #tr tst.common.machine.SolidifyHatch.tier.luv.name
        // # Solidifier Hatch(LuV)
        // #zh_CN 固化仓(LuV)
        GTCMItemList.SolidifyHatch_LuV.set(
            new GT_MetaTileEntity_Hatch_Solidify(
                18794,
                "NameSolidifyHatchLuV",
                TSTUtils.tr("tst.common.machine.SolidifyHatch.tier.luv.name"),
                6));

        // #tr tst.common.machine.SolidifyHatch.tier.iv.name
        // # Solidifier Hatch(IV)
        // #zh_CN 固化仓(IV)
        GTCMItemList.SolidifyHatch_IV.set(
            new GT_MetaTileEntity_Hatch_Solidify(18793, "NameSolidifyHatchIV", TSTUtils.tr("tst.common.machine.SolidifyHatch.tier.iv.name"), 5));

        // #tr tst.common.machine.CircuitImprintHatch.tier.2.name
        // # Imprint Circuit Hatch T2
        // #zh_CN 压印电路仓T2
        GTCMItemList.CircuitImprintHatchT2.set(
            new TST_CircuitImprintHatch(
                18792,
                "NameCircuitImprintHatchT2",
                TSTUtils.tr("tst.common.machine.CircuitImprintHatch.tier.2.name"),
                8));

        // #tr tst.common.machine.CircuitImprintHatch.tier.1.name
        // # Imprint Circuit Hatch T1
        // #zh_CN 压印电路仓T1
        GTCMItemList.CircuitImprintHatchT1.set(
            new TST_CircuitImprintHatch(
                18791,
                "NameCircuitImprintHatchT1",
                TSTUtils.tr("tst.common.machine.CircuitImprintHatch.tier.1.name"),
                5));

        // region Modularized Stuff

        if (Config.EnableModularizedMachineSystem) {

            // #tr tst.modular.machine.DynamicParallelController.tier.1.name
            // # Dynamic Parallel Controller Module T1
            // #zh_CN 动态并行控制器模块T1
            GTCMItemList.DynamicParallelControllerT1.set(
                new DynamicParallelController(
                    18800,
                    "NameDynamicParallelControllerT1",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.1.name"),
                    7,
                    ParallelOfParallelController[0]));

            // #tr tst.modular.machine.DynamicParallelController.tier.2.name
            // # Dynamic Parallel Controller Module T2
            // #zh_CN 动态并行控制器模块T2
            GTCMItemList.DynamicParallelControllerT2.set(
                new DynamicParallelController(
                    18801,
                    "NameDynamicParallelControllerT2",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.2.name"),
                    8,
                    ParallelOfParallelController[1]));

            // #tr tst.modular.machine.DynamicParallelController.tier.3.name
            // # Dynamic Parallel Controller Module T3
            // #zh_CN 动态并行控制器模块T3
            GTCMItemList.DynamicParallelControllerT3.set(
                new DynamicParallelController(
                    18802,
                    "NameDynamicParallelControllerT3",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.3.name"),
                    9,
                    ParallelOfParallelController[2]));

            // #tr tst.modular.machine.DynamicParallelController.tier.4.name
            // # Dynamic Parallel Controller Module T4
            // #zh_CN 动态并行控制器模块T4
            GTCMItemList.DynamicParallelControllerT4.set(
                new DynamicParallelController(
                    18803,
                    "NameDynamicParallelControllerT4",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.4.name"),
                    10,
                    ParallelOfParallelController[3]));

            // #tr tst.modular.machine.DynamicParallelController.tier.5.name
            // # Dynamic Parallel Controller Module T5
            // #zh_CN 动态并行控制器模块T5
            GTCMItemList.DynamicParallelControllerT5.set(
                new DynamicParallelController(
                    18804,
                    "NameDynamicParallelControllerT5",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.5.name"),
                    11,
                    ParallelOfParallelController[4]));

            // #tr tst.modular.machine.DynamicParallelController.tier.6.name
            // # Dynamic Parallel Controller Module T6
            // #zh_CN 动态并行控制器模块T6
            GTCMItemList.DynamicParallelControllerT6.set(
                new DynamicParallelController(
                    18805,
                    "NameDynamicParallelControllerT6",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.6.name"),
                    12,
                    ParallelOfParallelController[5]));

            // #tr tst.modular.machine.DynamicParallelController.tier.7.name
            // # Dynamic Parallel Controller Module T7
            // #zh_CN 动态并行控制器模块T7
            GTCMItemList.DynamicParallelControllerT7.set(
                new DynamicParallelController(
                    18806,
                    "NameDynamicParallelControllerT7",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.7.name"),
                    13,
                    ParallelOfParallelController[6]));

            // #tr tst.modular.machine.DynamicParallelController.tier.8.name
            // # Dynamic Parallel Controller Module T8
            // #zh_CN 动态并行控制器模块T8
            GTCMItemList.DynamicParallelControllerT8.set(
                new DynamicParallelController(
                    18807,
                    "NameDynamicParallelControllerT8",
                    TSTUtils.tr("tst.modular.machine.DynamicParallelController.tier.8.name"),
                    14,
                    ParallelOfParallelController[7]));

            // #tr tst.modular.machine.StaticParallelController.tier.1.name
            // # Static Parallel Controller Module T1
            // #zh_CN 静态并行控制器模块T1
            GTCMItemList.StaticParallelControllerT1.set(
                new StaticParallelController(
                    18808,
                    "NameStaticParallelControllerT1",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.1.name"),
                    7,
                    ParallelOfParallelController[0]));

            // #tr tst.modular.machine.StaticParallelController.tier.2.name
            // # Static Parallel Controller Module T2
            // #zh_CN 静态并行控制器模块T2
            GTCMItemList.StaticParallelControllerT2.set(
                new StaticParallelController(
                    18809,
                    "NameStaticParallelControllerT2",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.2.name"),
                    8,
                    ParallelOfParallelController[1]));

            // #tr tst.modular.machine.StaticParallelController.tier.3.name
            // # Static Parallel Controller Module T3
            // #zh_CN 静态并行控制器模块T3
            GTCMItemList.StaticParallelControllerT3.set(
                new StaticParallelController(
                    18810,
                    "NameStaticParallelControllerT3",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.3.name"),
                    9,
                    ParallelOfParallelController[2]));

            // #tr tst.modular.machine.StaticParallelController.tier.4.name
            // # Static Parallel Controller Module T4
            // #zh_CN 静态并行控制器模块T4
            GTCMItemList.StaticParallelControllerT4.set(
                new StaticParallelController(
                    18811,
                    "NameStaticParallelControllerT4",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.4.name"),
                    10,
                    ParallelOfParallelController[3]));

            // #tr tst.modular.machine.StaticParallelController.tier.5.name
            // # Static Parallel Controller Module T5
            // #zh_CN 静态并行控制器模块T5
            GTCMItemList.StaticParallelControllerT5.set(
                new StaticParallelController(
                    18812,
                    "NameStaticParallelControllerT5",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.5.name"),
                    11,
                    ParallelOfParallelController[4]));

            // #tr tst.modular.machine.StaticParallelController.tier.6.name
            // # Static Parallel Controller Module T6
            // #zh_CN 静态并行控制器模块T6
            GTCMItemList.StaticParallelControllerT6.set(
                new StaticParallelController(
                    18813,
                    "NameStaticParallelControllerT6",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.6.name"),
                    12,
                    ParallelOfParallelController[5]));

            // #tr tst.modular.machine.StaticParallelController.tier.7.name
            // # Static Parallel Controller Module T7
            // #zh_CN 静态并行控制器模块T7
            GTCMItemList.StaticParallelControllerT7.set(
                new StaticParallelController(
                    18814,
                    "NameStaticParallelControllerT7",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.7.name"),
                    13,
                    ParallelOfParallelController[6]));

            // #tr tst.modular.machine.StaticParallelController.tier.8.name
            // # Static Parallel Controller Module T8
            // #zh_CN 静态并行控制器模块T8
            GTCMItemList.StaticParallelControllerT8.set(
                new StaticParallelController(
                    18815,
                    "NameStaticParallelControllerT8",
                    TSTUtils.tr("tst.modular.machine.StaticParallelController.tier.8.name"),
                    14,
                    ParallelOfParallelController[7]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.1.name
            // # Dynamic Speed Controller Module T1
            // #zh_CN 动态速度控制器模块T1
            GTCMItemList.DynamicSpeedControllerT1.set(
                new DynamicSpeedController(
                    18816,
                    "NameDynamicSpeedControllerT1",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.1.name"),
                    7,
                    SpeedMultiplierOfSpeedController[0]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.2.name
            // # Dynamic Speed Controller Module T2
            // #zh_CN 动态速度控制器模块T2
            GTCMItemList.DynamicSpeedControllerT2.set(
                new DynamicSpeedController(
                    18817,
                    "NameDynamicSpeedControllerT2",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.2.name"),
                    8,
                    SpeedMultiplierOfSpeedController[1]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.3.name
            // # Dynamic Speed Controller Module T3
            // #zh_CN 动态速度控制器模块T3
            GTCMItemList.DynamicSpeedControllerT3.set(
                new DynamicSpeedController(
                    18818,
                    "NameDynamicSpeedControllerT3",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.3.name"),
                    9,
                    SpeedMultiplierOfSpeedController[2]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.4.name
            // # Dynamic Speed Controller Module T4
            // #zh_CN 动态速度控制器模块T4
            GTCMItemList.DynamicSpeedControllerT4.set(
                new DynamicSpeedController(
                    18819,
                    "NameDynamicSpeedControllerT4",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.4.name"),
                    10,
                    SpeedMultiplierOfSpeedController[3]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.5.name
            // # Dynamic Speed Controller Module T5
            // #zh_CN 动态速度控制器模块T5
            GTCMItemList.DynamicSpeedControllerT5.set(
                new DynamicSpeedController(
                    18820,
                    "NameDynamicSpeedControllerT5",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.5.name"),
                    11,
                    SpeedMultiplierOfSpeedController[4]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.6.name
            // # Dynamic Speed Controller Module T6
            // #zh_CN 动态速度控制器模块T6
            GTCMItemList.DynamicSpeedControllerT6.set(
                new DynamicSpeedController(
                    18821,
                    "NameDynamicSpeedControllerT6",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.6.name"),
                    12,
                    SpeedMultiplierOfSpeedController[5]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.7.name
            // # Dynamic Speed Controller Module T7
            // #zh_CN 动态速度控制器模块T7
            GTCMItemList.DynamicSpeedControllerT7.set(
                new DynamicSpeedController(
                    18822,
                    "NameDynamicSpeedControllerT7",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.7.name"),
                    13,
                    SpeedMultiplierOfSpeedController[6]));

            // #tr tst.modular.machine.DynamicSpeedController.tier.8.name
            // # Dynamic Speed Controller Module T8
            // #zh_CN 动态速度控制器模块T8
            GTCMItemList.DynamicSpeedControllerT8.set(
                new DynamicSpeedController(
                    18823,
                    "NameDynamicSpeedControllerT8",
                    TSTUtils.tr("tst.modular.machine.DynamicSpeedController.tier.8.name"),
                    14,
                    SpeedMultiplierOfSpeedController[7]));

            // #tr tst.modular.machine.StaticSpeedController.tier.1.name
            // # Static Speed Controller Module T1
            // #zh_CN 静态速度控制器模块T1
            GTCMItemList.StaticSpeedControllerT1.set(
                new StaticSpeedController(
                    18824,
                    "NameStaticSpeedControllerT1",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.1.name"),
                    7,
                    SpeedMultiplierOfSpeedController[0]));

            // #tr tst.modular.machine.StaticSpeedController.tier.2.name
            // # Static Speed Controller Module T2
            // #zh_CN 静态速度控制器模块T2
            GTCMItemList.StaticSpeedControllerT2.set(
                new StaticSpeedController(
                    18825,
                    "NameStaticSpeedControllerT2",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.2.name"),
                    8,
                    SpeedMultiplierOfSpeedController[1]));

            // #tr tst.modular.machine.StaticSpeedController.tier.3.name
            // # Static Speed Controller Module T3
            // #zh_CN 静态速度控制器模块T3
            GTCMItemList.StaticSpeedControllerT3.set(
                new StaticSpeedController(
                    18826,
                    "NameStaticSpeedControllerT3",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.3.name"),
                    9,
                    SpeedMultiplierOfSpeedController[2]));

            // #tr tst.modular.machine.StaticSpeedController.tier.4.name
            // # Static Speed Controller Module T4
            // #zh_CN 静态速度控制器模块T4
            GTCMItemList.StaticSpeedControllerT4.set(
                new StaticSpeedController(
                    18827,
                    "NameStaticSpeedControllerT4",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.4.name"),
                    10,
                    SpeedMultiplierOfSpeedController[3]));

            // #tr tst.modular.machine.StaticSpeedController.tier.5.name
            // # Static Speed Controller Module T5
            // #zh_CN 静态速度控制器模块T5
            GTCMItemList.StaticSpeedControllerT5.set(
                new StaticSpeedController(
                    18828,
                    "NameStaticSpeedControllerT5",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.5.name"),
                    11,
                    SpeedMultiplierOfSpeedController[4]));

            // #tr tst.modular.machine.StaticSpeedController.tier.6.name
            // # Static Speed Controller Module T6
            // #zh_CN 静态速度控制器模块T6
            GTCMItemList.StaticSpeedControllerT6.set(
                new StaticSpeedController(
                    18829,
                    "NameStaticSpeedControllerT6",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.6.name"),
                    12,
                    SpeedMultiplierOfSpeedController[5]));

            // #tr tst.modular.machine.StaticSpeedController.tier.7.name
            // # Static Speed Controller Module T7
            // #zh_CN 静态速度控制器模块T7
            GTCMItemList.StaticSpeedControllerT7.set(
                new StaticSpeedController(
                    18830,
                    "NameStaticSpeedControllerT7",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.7.name"),
                    13,
                    SpeedMultiplierOfSpeedController[6]));

            // #tr tst.modular.machine.StaticSpeedController.tier.8.name
            // # Static Speed Controller Module T8
            // #zh_CN 静态速度控制器模块T8
            GTCMItemList.StaticSpeedControllerT8.set(
                new StaticSpeedController(
                    18831,
                    "NameStaticSpeedControllerT8",
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tier.8.name"),
                    14,
                    SpeedMultiplierOfSpeedController[7]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.1.name
            // # Static Power Consumption Controller Module T1
            // #zh_CN 静态耗能控制器模块T1
            GTCMItemList.StaticPowerConsumptionControllerT1.set(
                new StaticPowerConsumptionController(
                    18832,
                    "NameStaticPowerConsumptionControllerT1",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.1.name"),
                    7,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[0]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.2.name
            // # Static Power Consumption Controller Module T2
            // #zh_CN 静态耗能控制器模块T2
            GTCMItemList.StaticPowerConsumptionControllerT2.set(
                new StaticPowerConsumptionController(
                    18833,
                    "NameStaticPowerConsumptionControllerT2",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.2.name"),
                    8,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[1]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.3.name
            // # Static Power Consumption Controller Module T3
            // #zh_CN 静态耗能控制器模块T3
            GTCMItemList.StaticPowerConsumptionControllerT3.set(
                new StaticPowerConsumptionController(
                    18834,
                    "NameStaticPowerConsumptionControllerT3",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.3.name"),
                    9,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[2]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.4.name
            // # Static Power Consumption Controller Module T4
            // #zh_CN 静态耗能控制器模块T4
            GTCMItemList.StaticPowerConsumptionControllerT4.set(
                new StaticPowerConsumptionController(
                    18835,
                    "NameStaticPowerConsumptionControllerT4",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.4.name"),
                    10,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[3]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.5.name
            // # Static Power Consumption Controller Module T5
            // #zh_CN 静态耗能控制器模块T5
            GTCMItemList.StaticPowerConsumptionControllerT5.set(
                new StaticPowerConsumptionController(
                    18836,
                    "NameStaticPowerConsumptionControllerT5",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.5.name"),
                    11,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[4]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.6.name
            // # Static Power Consumption Controller Module T6
            // #zh_CN 静态耗能控制器模块T6
            GTCMItemList.StaticPowerConsumptionControllerT6.set(
                new StaticPowerConsumptionController(
                    18837,
                    "NameStaticPowerConsumptionControllerT6",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.6.name"),
                    12,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[5]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.7.name
            // # Static Power Consumption Controller Module T7
            // #zh_CN 静态耗能控制器模块T7
            GTCMItemList.StaticPowerConsumptionControllerT7.set(
                new StaticPowerConsumptionController(
                    18838,
                    "NameStaticPowerConsumptionControllerT7",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.7.name"),
                    13,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[6]));

            // #tr tst.modular.machine.StaticPowerConsumptionController.tier.8.name
            // # Static Power Consumption Controller Module T8
            // #zh_CN 静态耗能控制器模块T8
            GTCMItemList.StaticPowerConsumptionControllerT8.set(
                new StaticPowerConsumptionController(
                    18839,
                    "NameStaticPowerConsumptionControllerT8",
                    TSTUtils.tr("tst.modular.machine.StaticPowerConsumptionController.tier.8.name"),
                    14,
                    (float) PowerConsumptionMultiplierOfPowerConsumptionController[7]));

            // #tr tst.modular.machine.LowSpeedPerfectOverclockController.name
            // # Low Speed Perfect Overclock Controller Module
            // #zh_CN 低速无损超频控制器模块
            GTCMItemList.LowSpeedPerfectOverclockController.set(
                new StaticOverclockController(
                    18840,
                    "NameLowSpeedPerfectOverclockController",
                    TSTUtils.tr("tst.modular.machine.LowSpeedPerfectOverclockController.name"),
                    12,
                    2,
                    2));

            // #tr tst.modular.machine.PerfectOverclockController.name
            // # Perfect Overclock Controller Module
            // #zh_CN 无损超频控制器模块
            GTCMItemList.PerfectOverclockController.set(
                new StaticOverclockController(
                    18841,
                    "NamePerfectOverclockController",
                    TSTUtils.tr("tst.modular.machine.PerfectOverclockController.name"),
                    13,
                    4,
                    4));

            // #tr tst.modular.machine.SingularityPerfectOverclockController.name
            // # Singularity Perfect Overclock Controller Module
            // #zh_CN 奇点无损超频控制器模块
            GTCMItemList.SingularityPerfectOverclockController.set(
                new StaticOverclockController(
                    18842,
                    "NameSingularityPerfectOverclockController",
                    TSTUtils.tr("tst.modular.machine.SingularityPerfectOverclockController.name"),
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

            // #tr tst.modular.machine.ExecutionCore.name
            // # Execution Core Module
            // #zh_CN 执行核心模块
            GTCMItemList.ExecutionCore
                .set(new ExecutionCore(18843, "NameExecutionCore", TSTUtils.tr("tst.modular.machine.ExecutionCore.name"), 12));

            // #tr tst.modular.machine.AdvancedExecutionCore.name
            // # Advanced Execution Core Module
            // #zh_CN 高级执行核心模块
            GTCMItemList.AdvancedExecutionCore.set(
                new AdvExecutionCore(18844, "NameAdvancedExecutionCore", TSTUtils.tr("tst.modular.machine.AdvancedExecutionCore.name"), 13));

            // #tr tst.modular.machine.PerfectExecutionCore.name
            // # Perfect Execution Core Module
            // #zh_CN 完美执行核心模块
            GTCMItemList.PerfectExecutionCore.set(
                new PerfectExecutionCore(
                    18845,
                    "NamePerfectExecutionCore",
                    TSTUtils.tr("tst.modular.machine.PerfectExecutionCore.name"),
                    14));
        }

        // endregion

        if (Config.Enable_BloodHell && Config.Enable_BloodHatch) {
            // #tr tst.common.machine.BloodOrbHatchDebug.name
            // # Debug Blood Hatch
            // #zh_CN Debug血液仓
            GTCMItemList.BloodOrbHatchDebug.set(
                new TST_BloodOrbHatch.TST_Debug_BloodHatch(
                    18848,
                    "NameBloodOrbHatchDebug",
                    TSTUtils.tr("tst.common.machine.BloodOrbHatchDebug.name"),
                    4));

        }

    }

    public static void loadMachinePostInit() {
        if (Config.EnableSpaceApiaryModule) {
            // #tr tst.common.machine.SpaceApiary.tier.1.name
            // # Space Apiray Module MK-I
            // #zh_CN 太空蜂箱模块 MK-I
            GTCMItemList.SpaceApiaryT1.set(
                new TST_SpaceApiary.TST_SpaceApiaryT1(19042, "NameSpaceApiaryT1", TSTUtils.tr("tst.common.machine.SpaceApiary.tier.1.name")));

            // #tr tst.common.machine.SpaceApiary.tier.2.name
            // # Space Apiray Module MK-II
            // #zh_CN 太空蜂箱模块 MK-II
            GTCMItemList.SpaceApiaryT2.set(
                new TST_SpaceApiary.TST_SpaceApiaryT2(19043, "NameSpaceApiaryT2", TSTUtils.tr("tst.common.machine.SpaceApiary.tier.2.name")));

            // #tr tst.common.machine.SpaceApiary.tier.3.name
            // # Space Apiray Module MK-III
            // #zh_CN 太空蜂箱模块 MK-III
            GTCMItemList.SpaceApiaryT3.set(
                new TST_SpaceApiary.TST_SpaceApiaryT3(19044, "NameSpaceApiaryT3", TSTUtils.tr("tst.common.machine.SpaceApiary.tier.3.name")));

            // #tr tst.common.machine.SpaceApiary.tier.4.name
            // # Space Apiray Module MK-IV
            // #zh_CN 太空蜂箱模块 MK-IV
            GTCMItemList.SpaceApiaryT4.set(
                new TST_SpaceApiary.TST_SpaceApiaryT4(19045, "NameSpaceApiaryT4", TSTUtils.tr("tst.common.machine.SpaceApiary.tier.4.name")));
        }

    }
}
// spotless:on
