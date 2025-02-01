package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_GiantVacuumDryingFurnace;
import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_IndustrialAlchemyTower;
import static com.Nxer.TwistSpaceTechnology.config.Config.Enable_MegaStoneBreaker;
import static com.Nxer.TwistSpaceTechnology.config.Config.ParallelOfParallelController;
import static com.Nxer.TwistSpaceTechnology.config.Config.PowerConsumptionMultiplierOfPowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedMultiplierOfSpeedController;

import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;

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
public class MachineLoader {

    public static ItemStack IntensifyChemicalDistorter; // INTENSIFY_CHEMICAL_DISTORTER
    public static ItemStack PreciseHighEnergyPhotonicQuantumMaster;
    public static ItemStack MiracleTop;
    public static ItemStack MagneticDrivePressureFormer;
    public static ItemStack PhysicalFormSwitcher;
    public static ItemStack MagneticMixer;
    public static ItemStack MagneticDomainConstructor;
    public static ItemStack Silksong;
    public static ItemStack HolySeparator;
    public static ItemStack SpaceScaler;
    public static ItemStack MoleculeDeconstructor;
    public static ItemStack CrystallineInfinitier;
    public static ItemStack DSPLauncher;
    public static ItemStack DSPReceiver;
    public static ItemStack ArtificialStar;
    public static ItemStack MiracleDoor;
    public static ItemStack OreProcessingFactory;
    public static ItemStack megaUniversalSpaceStation;
    public static ItemStack stellarMaterialSiphon;
    public static ItemStack CircuitConverter;
    public static ItemStack ElvenWorkshop;
    public static ItemStack HyperSpacetimeTransformer;
    public static ItemStack MegaBrickedBlastFurnace;
    public static ItemStack LargeIndustrialCokingFactory;
    public static ItemStack Scavenger;
    public static ItemStack superCleanRoom;
    public static ItemStack MegaEggGenerator;
    public static ItemStack BiosphereIII;
    public static ItemStack AdvancedMegaOilCracker;
    public static ItemStack IndistinctTentacle;
    public static ItemStack ThermalEnergyDevourer;
    public static ItemStack NuclearReactor;
    public static ItemStack AstralComputingArray;
    public static ItemStack VacuumFilterExtractor;
    public static ItemStack LargeSteamForgeHammer;
    public static ItemStack LargeSteamAlloySmelter;
    public static ItemStack IndustrialMagicMatrix;
    public static ItemStack IndustrialAlchemyTower;
    public static ItemStack EyeOfWood;
    public static ItemStack BeeEngineer;
    public static ItemStack MegaMacerator;
    public static ItemStack HephaestusAtelier;
    public static ItemStack DeployedNanoCore;
    public static ItemStack CoreDeviceOfHumanPowerGenerationFacility;
    public static ItemStack BallLightning;
    public static ItemStack StarcoreMiner;
    public static ItemStack Disassembler;

    public static ItemStack BigBroArray;
    public static ItemStack SpaceApiaryT1;
    public static ItemStack SpaceApiaryT2;
    public static ItemStack SpaceApiaryT3;
    public static ItemStack SpaceApiaryT4;
    public static ItemStack LargeCanner;
    public static ItemStack IndustrialMagnetarSeparator;
    public static ItemStack MegaTreeFarm;
    public static ItemStack LightningSpire;

    public static ItemStack ExtremeCraftCenter;
    public static ItemStack DimensionallyTranscendentMatterPlasmaForgePrototypeMK2;
    public static ItemStack LargeNeutronOscillator;
    public static ItemStack IndistinctTentaclePrototypeMK2;
    public static ItemStack MassFabricatorGenesis;
    public static ItemStack IncompactCyclotron;
    public static ItemStack StrangeMatterAggregator;
    public static ItemStack MicroSpaceTimeFabricatorio;
    public static ItemStack BloodyHell;
    public static ItemStack MegaStoneBreaer;
    public static ItemStack ManufacturingCenter;
    public static ItemStack GiantVacuumDryingFurnace;
    public static ItemStack ProcessingArray;
    // Single Block
    public static ItemStack InfiniteAirHatch;
    public static ItemStack ManaHatch;
    public static ItemStack InfiniteWirelessDynamoHatch;
    public static ItemStack DualInputBuffer_IV;
    public static ItemStack DualInputBuffer_LuV;
    public static ItemStack DualInputBuffer_ZPM;
    public static ItemStack DualInputBuffer_UV;

    public static ItemStack BufferedEnergyHatchLV;
    public static ItemStack BufferedEnergyHatchMV;
    public static ItemStack BufferedEnergyHatchHV;
    public static ItemStack BufferedEnergyHatchEV;
    public static ItemStack BufferedEnergyHatchIV;
    public static ItemStack BufferedEnergyHatchLuV;
    public static ItemStack BufferedEnergyHatchZPM;
    public static ItemStack BufferedEnergyHatchUV;
    public static ItemStack BufferedEnergyHatchUHV;
    public static ItemStack BufferedEnergyHatchUEV;
    public static ItemStack BufferedEnergyHatchUIV;
    public static ItemStack BufferedEnergyHatchUMV;
    public static ItemStack BufferedEnergyHatchUXV;
    public static ItemStack BufferedEnergyHatchMAX;
    public static ItemStack DebugUncertaintyHatch;
    public static ItemStack LaserSmartNode;

    public static ItemStack FackRackHatch;
    public static ItemStack RealRackHatch;

    public static ItemStack WirelessDataInputHatch;
    public static ItemStack WirelessDataOutputHatch;

    public static ItemStack LegendaryWirelessEnergyHatch;
    public static ItemStack HarmoniousWirelessEnergyHatch;

    // region Modularized Stuff
    public static ItemStack DynamicParallelControllerT1;
    public static ItemStack DynamicParallelControllerT2;
    public static ItemStack DynamicParallelControllerT3;
    public static ItemStack DynamicParallelControllerT4;
    public static ItemStack DynamicParallelControllerT5;
    public static ItemStack DynamicParallelControllerT6;
    public static ItemStack DynamicParallelControllerT7;
    public static ItemStack DynamicParallelControllerT8;
    public static ItemStack StaticParallelControllerT1;
    public static ItemStack StaticParallelControllerT2;
    public static ItemStack StaticParallelControllerT3;
    public static ItemStack StaticParallelControllerT4;
    public static ItemStack StaticParallelControllerT5;
    public static ItemStack StaticParallelControllerT6;
    public static ItemStack StaticParallelControllerT7;
    public static ItemStack StaticParallelControllerT8;
    public static ItemStack DynamicSpeedControllerT1;
    public static ItemStack DynamicSpeedControllerT2;
    public static ItemStack DynamicSpeedControllerT3;
    public static ItemStack DynamicSpeedControllerT4;
    public static ItemStack DynamicSpeedControllerT5;
    public static ItemStack DynamicSpeedControllerT6;
    public static ItemStack DynamicSpeedControllerT7;
    public static ItemStack DynamicSpeedControllerT8;
    public static ItemStack StaticSpeedControllerT1;
    public static ItemStack StaticSpeedControllerT2;
    public static ItemStack StaticSpeedControllerT3;
    public static ItemStack StaticSpeedControllerT4;
    public static ItemStack StaticSpeedControllerT5;
    public static ItemStack StaticSpeedControllerT6;
    public static ItemStack StaticSpeedControllerT7;
    public static ItemStack StaticSpeedControllerT8;
    public static ItemStack StaticPowerConsumptionControllerT1;
    public static ItemStack StaticPowerConsumptionControllerT2;
    public static ItemStack StaticPowerConsumptionControllerT3;
    public static ItemStack StaticPowerConsumptionControllerT4;
    public static ItemStack StaticPowerConsumptionControllerT5;
    public static ItemStack StaticPowerConsumptionControllerT6;
    public static ItemStack StaticPowerConsumptionControllerT7;
    public static ItemStack StaticPowerConsumptionControllerT8;
    public static ItemStack LowSpeedPerfectOverclockController;
    public static ItemStack PerfectOverclockController;
    public static ItemStack SingularityPerfectOverclockController;
    public static ItemStack ExecutionCore;
    public static ItemStack AdvancedExecutionCore;
    public static ItemStack PerfectExecutionCore;

    public static ItemStack BloodOrbHatch;

    // endregion

    // test
    public static ItemStack TestMachine;

    public static void loadMachines() {

        if (Config.activateMegaSpaceStation) {
            EntityList.addMapping(Ship.class, "Ship", 114);
        }
        // test
        // TestMachine = new Test_ModularizedMachine(19000, "TestMachine", "TestMachine").getStackForm(1);

        // region multi Machine controller

        //
        IntensifyChemicalDistorter = new GT_TileEntity_IntensifyChemicalDistorter(
            19001,
            "NameIntensifyChemicalDistorter",
            TextLocalization.NameIntensifyChemicalDistorter).getStackForm(1);
        GTCMItemList.IntensifyChemicalDistorter.set(IntensifyChemicalDistorter);

        //
        PreciseHighEnergyPhotonicQuantumMaster = new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(
            19002,
            "NamePreciseHighEnergyPhotonicQuantumMaster",
            TextLocalization.NamePreciseHighEnergyPhotonicQuantumMaster).getStackForm(1);
        GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.set(PreciseHighEnergyPhotonicQuantumMaster);

        //
        MiracleTop = new GT_TileEntity_MiracleTop(
            19003,
            TextLocalization.NameMiracleTop,
            TextLocalization.NameMiracleTop).getStackForm(1);
        GTCMItemList.MiracleTop.set(MiracleTop);

        //
        MagneticDrivePressureFormer = new GT_TileEntity_MagneticDrivePressureFormer(
            19004,
            "NameMagneticDrivePressureFormer",
            TextLocalization.NameMagneticDrivePressureFormer).getStackForm(1);
        GTCMItemList.MagneticDrivePressureFormer.set(MagneticDrivePressureFormer);

        //
        PhysicalFormSwitcher = new GT_TileEntity_PhysicalFormSwitcher(
            19005,
            "NamePhysicalFormSwitcher",
            TextLocalization.NamePhysicalFormSwitcher).getStackForm(1);
        GTCMItemList.PhysicalFormSwitcher.set(PhysicalFormSwitcher);

        //
        MagneticMixer = new GT_TileEntity_MagneticMixer(19006, "NameMagneticMixer", TextLocalization.NameMagneticMixer)
            .getStackForm(1);
        GTCMItemList.MagneticMixer.set(MagneticMixer);

        //
        MagneticDomainConstructor = new GT_TileEntity_MagneticDomainConstructor(
            19007,
            "NameMagneticDomainConstructor",
            TextLocalization.NameMagneticDomainConstructor).getStackForm(1);
        GTCMItemList.MagneticDomainConstructor.set(MagneticDomainConstructor);

        //
        Silksong = new GT_TileEntity_Silksong(19008, "NameSilksong", TextLocalization.NameSilksong).getStackForm(1);
        GTCMItemList.Silksong.set(Silksong);

        //
        HolySeparator = new GT_TileEntity_HolySeparator(19009, "NameHolySeparator", TextLocalization.NameHolySeparator)
            .getStackForm(1);
        GTCMItemList.HolySeparator.set(HolySeparator);

        //
        SpaceScaler = new GT_TileEntity_SpaceScaler(19010, "NameSpaceScaler", TextLocalization.NameSpaceScaler)
            .getStackForm(1);
        GTCMItemList.SpaceScaler.set(SpaceScaler);

        //
        MoleculeDeconstructor = new GT_TileEntity_MoleculeDeconstructor(
            19011,
            "NameMoleculeDeconstructor",
            TextLocalization.NameMoleculeDeconstructor).getStackForm(1);
        GTCMItemList.MoleculeDeconstructor.set(MoleculeDeconstructor);

        //
        CrystallineInfinitier = new GTCM_CrystallineInfinitier(
            19012,
            "NameCrystallineInfinitier",
            TextLocalization.NameCrystallineInfinitier).getStackForm(1);
        GTCMItemList.CrystallineInfinitier.set(CrystallineInfinitier);

        //
        DSPLauncher = new TST_DSPLauncher(19013, "NameDSPLauncher", TextLocalization.NameDSPLauncher).getStackForm(1);
        GTCMItemList.DSPLauncher.set(DSPLauncher);

        //
        DSPReceiver = new TST_DSPReceiver(19014, "NameDSPReceiver", TextLocalization.NameDSPReceiver).getStackForm(1);
        GTCMItemList.DSPReceiver.set(DSPReceiver);

        //
        ArtificialStar = new TST_ArtificialStar(19015, "NameArtificialStar", TextLocalization.NameArtificialStar)
            .getStackForm(1);
        GTCMItemList.ArtificialStar.set(ArtificialStar);

        //
        MiracleDoor = new TST_MiracleDoor(19016, "NameMiracleDoor", TextLocalization.NameMiracleDoor).getStackForm(1);
        GTCMItemList.MiracleDoor.set(MiracleDoor);

        //
        OreProcessingFactory = new TST_OreProcessingFactory(
            19017,
            "NameOreProcessingFactory",
            TextLocalization.NameOreProcessingFactory).getStackForm(1);
        GTCMItemList.OreProcessingFactory.set(OreProcessingFactory);

        // Space Station Systems
        if (Config.activateMegaSpaceStation) {
            megaUniversalSpaceStation = new TST_MegaUniversalSpaceStation(
                19018,
                "NameMegaUniversalSpaceStation",
                TextLocalization.NameMegaUniversalSpaceStation).getStackForm(1);
            GTCMItemList.megaUniversalSpaceStation.set(megaUniversalSpaceStation);
            stellarMaterialSiphon = new GT_TileEntity_StellarMaterialSiphon(
                19019,
                "NameStellarMaterialSiphon",
                TextLocalization.NameStellarMaterialSiphon).getStackForm(1);
            GTCMItemList.StellarMaterialSiphon.set(stellarMaterialSiphon);
        }

        //
        CircuitConverter = new TST_CircuitConverter(
            19020,
            "NameCircuitConverter",
            TextLocalization.NameCircuitConverter).getStackForm(1);
        GTCMItemList.CircuitConverter.set(CircuitConverter);

        //
        LargeIndustrialCokingFactory = new TST_LargeIndustrialCokingFactory(
            19021,
            "NameLargeIndustrialCokingFactory",
            TextLocalization.NameLargeIndustrialCokingFactory).getStackForm(1);
        GTCMItemList.LargeIndustrialCokingFactory.set(LargeIndustrialCokingFactory);

        //
        ElvenWorkshop = new GTCM_ElvenWorkshop(19500, "NameElvenWorkshop", TextLocalization.NameElvenWorkshop)
            .getStackForm(1);
        GTCMItemList.ElvenWorkshop.set(ElvenWorkshop);

        //
        HyperSpacetimeTransformer = new GTCM_HyperSpacetimeTransformer(
            19501,
            "NameHyperSpacetimeTransformer",
            TextLocalization.NameHyperSpacetimeTransformer).getStackForm(1);
        GTCMItemList.HyperSpacetimeTransformer.set(HyperSpacetimeTransformer);
        //
        MegaBrickedBlastFurnace = new GT_TileEntity_MegaBrickedBlastFurnace(
            19022,
            "NameMegaBrickedBlastFurnace",
            TextLocalization.NameMegaBrickedBlastFurnace).getStackForm(1);
        GTCMItemList.MegaBrickedBlastFurnace.set(MegaBrickedBlastFurnace);

        Scavenger = new TST_Scavenger(19023, "NameScavenger", TextLocalization.NameScavenger).getStackForm(1);

        GTCMItemList.Scavenger.set(Scavenger);

        superCleanRoom = new TST_CleanRoom(19024, "NameTSTcleanroom", TextLocalization.NamesuperCleanRoom)
            .getStackForm(1);
        // ItemList.Machine_Multi_Cleanroom.set(superCleanRoom);
        GTCMItemList.superCleanRoom.set(superCleanRoom);

        //
        BiosphereIII = new TST_BiosphereIII(19025, "nameBiosphereIII", TextLocalization.NameBiosphereIII)
            .getStackForm(1);
        GTCMItemList.BiosphereIII.set(BiosphereIII);

        MegaEggGenerator = new GT_TileEntity_MegaEggGenerator(
            19026,
            "NameMegaEggGenerator",
            TextLocalization.NameMegaEggGenerator).getStackForm(1);
        GTCMItemList.MegaEggGenerator.set(MegaEggGenerator);

        //
        AdvancedMegaOilCracker = new TST_AdvancedMegaOilCracker(
            19027,
            "NameAdvancedMegaOilCracker",
            TextLocalization.NameAdvancedMegaOilCracker).getStackForm(1);
        GTCMItemList.AdvancedMegaOilCracker.set(AdvancedMegaOilCracker);

        //
        IndistinctTentacle = new TST_IndistinctTentacle(
            19028,
            "NameIndistinctTentacle",
            TextLocalization.NameIndistinctTentacle).getStackForm(1);
        GTCMItemList.IndistinctTentacle.set(IndistinctTentacle);
        // NuclearReactor = new TST_NuclearReactor(19029, "nuclea reactor", "nuclear reactor").getStackForm(1);
        // GTCMItemList.NuclearReactor.set(IndistinctTentacle);

        //
        AstralComputingArray = new TST_Computer(
            19029,
            "NameAstralComputingArray",
            TextLocalization.NameAstralComputingArray).getStackForm(1);
        GTCMItemList.AstralComputingArray.set(AstralComputingArray);

        //
        ThermalEnergyDevourer = new TST_ThermalEnergyDevourer(
            19030,
            "NameThermalEnergyDevourer",
            TextLocalization.NameThermalEnergyDevourer).getStackForm(1);
        GTCMItemList.ThermalEnergyDevourer.set(ThermalEnergyDevourer);

        //
        VacuumFilterExtractor = new TST_VacuumFilterExtractor(
            19031,
            "NameVacuumFilterExtractor",
            TextLocalization.NameVacuumFilterExtractor).getStackForm(1);
        GTCMItemList.VacuumFilterExtractor.set(VacuumFilterExtractor);

        //
        LargeSteamForgeHammer = new TST_LargeSteamForgeHammer(
            19032,
            "NameLargeSteamForgeHammer",
            TextLocalization.NameLargeSteamForgeHammer).getStackForm(1);
        GTCMItemList.LargeSteamForgeHammer.set(LargeSteamForgeHammer);

        //
        LargeSteamAlloySmelter = new TST_LargeSteamAlloySmelter(
            19033,
            "NameLargeSteamAlloySmelter",
            TextLocalization.NameLargeSteamAlloySmelter).getStackForm(1);
        GTCMItemList.LargeSteamAlloySmelter.set(LargeSteamAlloySmelter);

        //
        EyeOfWood = new TST_EyeOfWood(19034, "NameEyeOfWood", TextLocalization.NameEyeOfWood).getStackForm(1);
        GTCMItemList.EyeOfWood.set(EyeOfWood);

        //
        BeeEngineer = new TST_BeeEngineer(19035, "NameBeeEngineer", TextLocalization.NameBeeEngineer).getStackForm(1);
        GTCMItemList.BeeEngineer.set(BeeEngineer);

        //
        MegaMacerator = new TST_MegaMacerator(19036, "NameMegaMacerator", TextLocalization.NameMegaMacerator)
            .getStackForm(1);
        GTCMItemList.MegaMacerator.set(MegaMacerator);

        //
        HephaestusAtelier = new TST_HephaestusAtelier(
            19037,
            "NameHephaestusAtelier",
            TextLocalization.NameHephaestusAtelier).getStackForm(1);
        GTCMItemList.HephaestusAtelier.set(HephaestusAtelier);

        //
        if (Config.Enable_DeployedNanoCore) {
            DeployedNanoCore = new TST_DeployedNanoCore(
                19038,
                "NameDeployedNanoCore",
                TextLocalization.NameDeployedNanoCore).getStackForm(1);
            GTCMItemList.DeployedNanoCore.set(DeployedNanoCore);
        }

        //
        if (Config.Enable_CoreDeviceOfHumanPowerGenerationFacility) {
            CoreDeviceOfHumanPowerGenerationFacility = new TST_CoreDeviceOfHumanPowerGenerationFacility(
                19039,
                "NameCoreDeviceOfHumanPowerGenerationFacility",
                TextEnums.NameCoreDeviceOfHumanPowerGenerationFacility.toString()).getStackForm(1);
            GTCMItemList.CoreDeviceOfHumanPowerGenerationFacility.set(CoreDeviceOfHumanPowerGenerationFacility);
        }

        //
        if (Config.Enable_StarcoreMiner) {
            StarcoreMiner = new TST_StarcoreMiner(
                19040,
                "NameStarcoreMiner",
                // #tr NameStarcoreMiner
                // # Starcore Miner
                // #zh_CN 星核钻机
                TextEnums.tr("NameStarcoreMiner")).getStackForm(1);
            GTCMItemList.StarcoreMiner.set(StarcoreMiner);
        }

        //
        if (Config.Enable_Disassembler) {
            Disassembler = new TST_Disassembler(
                19041,
                // #tr NameTSTDisassembler
                // # TST Large Disassembler
                // #zh_CN TST大型拆解机
                "NameTSTDisassembler",
                TextEnums.tr("NameTSTDisassembler")).getStackForm(1);
            GTCMItemList.Disassembler.set(Disassembler);
        }

        if (Config.Enable_BallLightning) {
            BallLightning = new TST_BallLightning(19046, "NameBallLightning", TextLocalization.NameBallLightning)
                .getStackForm(1);
            GTCMItemList.BallLightning.set(BallLightning);
        }

        //
        if (Config.Enable_IndustrialMagicMatrix) {
            IndustrialMagicMatrix = new GT_TileEntity_IndustrialMagicMatrix(
                19047,
                "IndustrialMagicMatrix",
                // #tr NameIndustrialMagicMatrix
                // # Industrial Magic Matrix
                // #zh_CN §0工业注魔矩阵
                TextEnums.tr("NameIndustrialMagicMatrix")).getStackForm(1);
            GTCMItemList.IndustrialMagicMatrix.set(IndustrialMagicMatrix);
        }

        //
        if (Config.Enable_LargeCanner) {
            LargeCanner = new TST_LargeCanner(
                19048,
                "NameLargeCanner",
                // #tr NameLargeCanner
                // # Large Canner
                // #zh_CN 大型装罐机
                TextEnums.tr("NameLargeCanner")).getStackForm(1);
            GTCMItemList.LargeCanner.set(LargeCanner);
        }

        BigBroArray = new TST_BigBroArray(19049, "BigBroArray.name", TextEnums.tr("BigBroArray.name")).getStackForm(1);
        GTCMItemList.BigBroArray.set(BigBroArray);

        if (Config.Enable_IndustrialMagnetarSeparator) {
            IndustrialMagnetarSeparator = new TST_IndustrialMagnetarSeparator(
                19050,
                "NameIndustrialMagnetarSeparator",
                // #tr NameIndustrialMagnetarSeparator
                // # Industrial Magnetar Separator
                // #zh_CN 工业电磁离析机
                TextEnums.tr("NameIndustrialMagnetarSeparator")).getStackForm(1);
            GTCMItemList.IndustrialMagnetarSeparator.set(IndustrialMagnetarSeparator);
        }

        if (Config.Enable_MegaTreeFarm) {
            MegaTreeFarm = new TST_MegaTreeFarm(
                19051,
                "NameMegaTreeFarm",
                // #tr NameMegaTreeFarm
                // # Eco-Sphere Growth Simulator
                // #zh_CN 拟似生态圈
                TextEnums.tr("NameMegaTreeFarm")).getStackForm(1);
            GTCMItemList.MegaTreeFarm.set(MegaTreeFarm);
        }

        ExtremeCraftCenter = new TST_MegaCraftingCenter(
            19052,
            "NameExtremeCraftCenter",
            TextEnums.tr("NameExtremeCraftCenter")).getStackForm(1);
        GTCMItemList.ExtremeCraftCenter.set(ExtremeCraftCenter);

        if (Config.Enable_LightningSpire) {
            LightningSpire = new GTCM_LightningSpire(
                19053,
                "NameLightningSpire",
                // #tr NameLightningSpire
                // # Lightning Spire
                // #zh_CN 闪电尖塔
                TextEnums.tr("NameLightningSpire")).getStackForm(1);
            GTCMItemList.LightningSpire.set(LightningSpire);
        }

        if (Config.EnableModularizedMachineSystem) {

            if (Config.EnableDimensionallyTranscendentMatterPlasmaForgePrototypeMK2) {
                // #tr NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2
                // # Dimensionally Transcendent Matter Plasma Forge Prototype MK-II
                // #zh_CN 超维度物质等离子锻炉原型机MK-II
                DimensionallyTranscendentMatterPlasmaForgePrototypeMK2 = new MM_DimensionallyTranscendentMatterPlasmaForgePrototypeMK2(
                    19054,
                    "NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2",
                    TextEnums.tr("NameDimensionallyTranscendentMatterPlasmaForgePrototypeMK2")).getStackForm(1);
                GTCMItemList.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2
                    .set(DimensionallyTranscendentMatterPlasmaForgePrototypeMK2);
            }

            if (Config.EnableLargeNeutronOscillator) {

                // #tr NameLargeNeutronOscillator
                // # Large Neutron Oscillator
                // #zh_CN 大型中子振荡器
                LargeNeutronOscillator = new MM_LargeNeutronOscillator(
                    19055,
                    "NameLargeNeutronOscillator",
                    TextEnums.tr("NameLargeNeutronOscillator")).getStackForm(1);
                GTCMItemList.LargeNeutronOscillator.set(LargeNeutronOscillator);
            }

            if (Config.EnableRecipeRegistry_IndistinctTentacle) {

                // #tr NameIndistinctTentaclePrototypeMK2
                // # {\DARK_GRAY}{\BOLD}Indistinct Tentacle {\RESET}Prototype MK-II
                // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}原型机MK-II
                IndistinctTentaclePrototypeMK2 = new MM_IndistinctTentaclePrototypeMK2(
                    19056,
                    "NameIndistinctTentaclePrototypeMK2",
                    TextEnums.tr("NameIndistinctTentaclePrototypeMK2")).getStackForm(1);
                GTCMItemList.IndistinctTentaclePrototypeMK2.set(IndistinctTentaclePrototypeMK2);
            }

            // #tr NameMassFabricatorGenesis
            // # Mass Fabricator : Genesis
            // #zh_CN 质量发生器 : 创世纪
            MassFabricatorGenesis = new MM_MassFabricatorGenesis(
                19057,
                "NameMassFabricatorGenesis",
                TextEnums.tr("NameMassFabricatorGenesis")).getStackForm(1);
            GTCMItemList.MassFabricatorGenesis.set(MassFabricatorGenesis);

        }

        if (Config.Enable_IncompactCyclotron) {
            // #tr NameIncompactCyclotron
            // # PULSAR - Incompact Cyclotron
            // #zh_CN PULSAR - 非紧凑式回旋加速器
            IncompactCyclotron = new TST_IncompactCyclotron(
                19058,
                "NameIncompactCyclotron",
                TextEnums.tr("NameIncompactCyclotron")).getStackForm(1);
            GTCMItemList.IncompactCyclotron.set(IncompactCyclotron);
        }

        if (Config.EnableModularizedMachineSystem) {

            // #tr NameStrangeMatterAggregator
            // # Strange Matter Aggregator
            // #zh_CN 奇异物质聚合器
            StrangeMatterAggregator = new TST_StrangeMatterAggregator(
                19059,
                "NameStrangeMatterAggregator",
                TextEnums.tr("NameStrangeMatterAggregator")).getStackForm(1);
            GTCMItemList.StrangeMatterAggregator.set(StrangeMatterAggregator);

        }

        // #tr NameMicroSpaceTimeFabricatorio
        // # Micro SpaceTime Fabricatorio
        // #zh_CN 微型时空发生器
        MicroSpaceTimeFabricatorio = new TST_MicroSpaceTimeFabricatorio(
            19060,
            "NameMicroSpaceTimeFabricatorio",
            TextEnums.tr("NameMicroSpaceTimeFabricatorio")).getStackForm(1);
        GTCMItemList.MicroSpaceTimeFabricatorio.set(MicroSpaceTimeFabricatorio);

        if (Config.Enable_BloodHell) {
            // #tr NameBloodyHell
            // # Bloody Hell
            // #zh_CN 血狱
            BloodyHell = new TST_BloodyHell(19061, "NameBloodyHell", TextEnums.tr("NameBloodyHell")).getStackForm(1);
            GTCMItemList.BloodyHell.set(BloodyHell);

            if (Config.Enable_BloodHatch) {
                // #tr NameBloodOrbHatch
                // # Blood Hatch
                // #zh_CN 血液仓
                BloodOrbHatch = new TST_BloodOrbHatch(18846, "NameBloodOrbHatch", TextEnums.tr("NameBloodOrbHatch"), 4)
                    .getStackForm(1);
                GTCMItemList.BloodOrbHatch.set(BloodOrbHatch);
            }
        }

        if (Enable_MegaStoneBreaker) {
            // #tr NameMegaStoneBreaker
            // # Silicon Rock Synthesizer
            // #zh_CN 硅岩制造机
            MegaStoneBreaer = new TST_MegaStoneBreaker(
                19062,
                "NameMegaStoneBreaker",
                TextEnums.tr("NameMegaStoneBreaker")).getStackForm(1);
            GTCMItemList.MegaStoneBreaker.set(MegaStoneBreaer);
        }

        // #tr NameManufacturingCenter
        // # Manufacturing Center
        // #zh_CN 加工中心
        ManufacturingCenter = new TST_ManufacturingCenter(
            19063,
            "NameManufacturingCenter",
            TextEnums.tr("NameManufacturingCenter")).getStackForm(1);
        GTCMItemList.ManufacturingCenter.set(ManufacturingCenter);

        if (Enable_IndustrialAlchemyTower) {
            IndustrialAlchemyTower = new TST_IndustrialAlchemyTower(
                // #tr NameIndustrialAlchemyTower
                // # Industrial Alchemy Tower
                // #zh_CN 工业炼金塔
                19064,
                "IndustrialAlchemyTower",
                TextEnums.tr("NameIndustrialAlchemyTower")).getStackForm(1);
            GTCMItemList.IndustrialAlchemyTower.set(IndustrialAlchemyTower);
        }

        // #tr NameGiantVacuumDryingFurnace
        // # Giant Vacuum Drying Furnace
        // #zh_CN 巨型真空干燥炉
        if (Enable_GiantVacuumDryingFurnace) {
            GiantVacuumDryingFurnace = new TST_GiantVacuumDryingFurnace(
                19065,
                "GiantVacuumDryingFurnace",
                TextEnums.tr("NameGiantVacuumDryingFurnace")).getStackForm(1);
            GTCMItemList.GiantVacuumDryingFurnace.set(GiantVacuumDryingFurnace);
        }

        if (Config.Enable_ProcessingArray) {
            // #tr NameProcessingArray
            // # TST Processing Array
            // #zh_CN TST处理阵列
            ProcessingArray = new TST_ProcessingArray(19066, "NameProcessingArray", TextEnums.tr("NameProcessingArray"))
                .getStackForm(1);
            GTCMItemList.ProcessingArray.set(ProcessingArray);
        }
        // endregion

        // region Single block Machine

        //
        InfiniteAirHatch = new GT_MetaTileEntity_Hatch_Air(
            18999,
            "NameInfiniteAirHatch",
            TextLocalization.NameInfiniteAirHatch,
            9).getStackForm(1);
        GTCMItemList.InfiniteAirHatch.set(InfiniteAirHatch);

        //
        InfiniteWirelessDynamoHatch = new GT_Hatch_InfiniteWirelessDynamoHatch(
            18998,
            "NameInfiniteWirelessDynamoHatch",
            TextLocalization.NameInfiniteWirelessDynamoHatch,
            14).getStackForm(1);
        GTCMItemList.InfiniteWirelessDynamoHatch.set(InfiniteWirelessDynamoHatch);

        //
        ManaHatch = new TST_ManaHatch(18979, "NameManaHatch", TextLocalization.NameManaHatch, 9).getStackForm(1);
        GTCMItemList.ManaHatch.set(ManaHatch);

        // region Dual Input Buffer
        DualInputBuffer_IV = new GT_MetaTileEntity_Hatch_DualInput(
            18980,
            "NameDualInputBuffer_IV",
            TextLocalization.NameDualInputBuffer_IV,
            5).getStackForm(1);
        GTCMItemList.DualInputBuffer_IV.set(DualInputBuffer_IV);

        DualInputBuffer_LuV = new GT_MetaTileEntity_Hatch_DualInput(
            18981,
            "NameDualInputBuffer_LuV",
            TextLocalization.NameDualInputBuffer_LuV,
            6).getStackForm(1);
        GTCMItemList.DualInputBuffer_LuV.set(DualInputBuffer_LuV);

        DualInputBuffer_ZPM = new GT_MetaTileEntity_Hatch_DualInput(
            18982,
            "NameDualInputBuffer_ZPM",
            TextLocalization.NameDualInputBuffer_ZPM,
            7).getStackForm(1);
        GTCMItemList.DualInputBuffer_ZPM.set(DualInputBuffer_ZPM);

        DualInputBuffer_UV = new GT_MetaTileEntity_Hatch_DualInput(
            18983,
            "NameDualInputBuffer_UV",
            TextLocalization.NameDualInputBuffer_UV,
            8).getStackForm(1);
        GTCMItemList.DualInputBuffer_UV.set(DualInputBuffer_UV);

        // region buffered energy hatch

        BufferedEnergyHatchLV = new GT_Hatch_BufferedEnergyHatch(
            18984,
            "NameBufferedEnergyHatchLV",
            TstUtils.tr("NameBufferedEnergyHatchLV"),
            1,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchMV = new GT_Hatch_BufferedEnergyHatch(
            18985,
            "NameBufferedEnergyHatchMV",
            TstUtils.tr("NameBufferedEnergyHatchMV"),
            2,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchHV = new GT_Hatch_BufferedEnergyHatch(
            18986,
            "NameBufferedEnergyHatchHV",
            TstUtils.tr("NameBufferedEnergyHatchHV"),
            3,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchEV = new GT_Hatch_BufferedEnergyHatch(
            18987,
            "NameBufferedEnergyHatchEV",
            TstUtils.tr("NameBufferedEnergyHatchEV"),
            4,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchIV = new GT_Hatch_BufferedEnergyHatch(
            18988,
            "NameBufferedEnergyHatchIV",
            TstUtils.tr("NameBufferedEnergyHatchIV"),
            5,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchLuV = new GT_Hatch_BufferedEnergyHatch(
            18989,
            "NameBufferedEnergyHatchLuV",
            TstUtils.tr("NameBufferedEnergyHatchLuV"),
            6,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchZPM = new GT_Hatch_BufferedEnergyHatch(
            18990,
            "NameBufferedEnergyHatchZPM",
            TstUtils.tr("NameBufferedEnergyHatchZPM"),
            7,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUV = new GT_Hatch_BufferedEnergyHatch(
            18991,
            "NameBufferedEnergyHatchUV",
            TstUtils.tr("NameBufferedEnergyHatchUV"),
            8,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUHV = new GT_Hatch_BufferedEnergyHatch(
            18992,
            "NameBufferedEnergyHatchUHV",
            TstUtils.tr("NameBufferedEnergyHatchUHV"),
            9,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUEV = new GT_Hatch_BufferedEnergyHatch(
            18993,
            "NameBufferedEnergyHatchUEV",
            TstUtils.tr("NameBufferedEnergyHatchUEV"),
            10,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUIV = new GT_Hatch_BufferedEnergyHatch(
            18994,
            "NameBufferedEnergyHatchUIV",
            TstUtils.tr("NameBufferedEnergyHatchUIV"),
            11,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUMV = new GT_Hatch_BufferedEnergyHatch(
            18995,
            "NameBufferedEnergyHatchUMV",
            TstUtils.tr("NameBufferedEnergyHatchUMV"),
            12,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUXV = new GT_Hatch_BufferedEnergyHatch(
            18996,
            "NameBufferedEnergyHatchUXV",
            TstUtils.tr("NameBufferedEnergyHatchUXV"),
            13,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchMAX = new GT_Hatch_BufferedEnergyHatch(
            18997,
            "NameBufferedEnergyHatchMAX",
            TstUtils.tr("NameBufferedEnergyHatchMAX"),
            14,
            16,
            null).getStackForm(1);
        GTCMItemList.BufferedEnergyHatchLV.set(BufferedEnergyHatchLV);
        GTCMItemList.BufferedEnergyHatchMV.set(BufferedEnergyHatchMV);
        GTCMItemList.BufferedEnergyHatchHV.set(BufferedEnergyHatchHV);
        GTCMItemList.BufferedEnergyHatchEV.set(BufferedEnergyHatchEV);
        GTCMItemList.BufferedEnergyHatchIV.set(BufferedEnergyHatchIV);
        GTCMItemList.BufferedEnergyHatchLuV.set(BufferedEnergyHatchLuV);
        GTCMItemList.BufferedEnergyHatchZPM.set(BufferedEnergyHatchZPM);
        GTCMItemList.BufferedEnergyHatchUV.set(BufferedEnergyHatchUV);
        GTCMItemList.BufferedEnergyHatchUHV.set(BufferedEnergyHatchUHV);
        GTCMItemList.BufferedEnergyHatchUEV.set(BufferedEnergyHatchUEV);
        GTCMItemList.BufferedEnergyHatchUIV.set(BufferedEnergyHatchUIV);
        GTCMItemList.BufferedEnergyHatchUMV.set(BufferedEnergyHatchUMV);
        GTCMItemList.BufferedEnergyHatchUXV.set(BufferedEnergyHatchUXV);
        GTCMItemList.BufferedEnergyHatchMAX.set(BufferedEnergyHatchMAX);

        //
        DebugUncertaintyHatch = new GT_MetaTileEntity_Hatch_UncertaintyDebug(
            18978,
            "NameDebugUncertaintyHatch",
            TextLocalization.NameDebugUncertaintyHatch,
            12).getStackForm(1);
        GTCMItemList.DebugUncertaintyHatch.set(DebugUncertaintyHatch);

        //
        LaserSmartNode = new GT_MetaTileEntity_Pipe_EnergySmart(
            18960,
            "NameLaserSmartNode",
            TextLocalization.NameLaserSmartNode).getStackForm(1);
        GTCMItemList.LaserSmartNode.set(LaserSmartNode);
        // endregion
        FackRackHatch = new GT_Hatch_RackComputationMonitor(
            18959,
            "NameFackRackHatch",
            TextLocalization.NameFackRackHatch,
            12,
            false).getStackForm(1);
        GTCMItemList.FackRackHatch.set(FackRackHatch);

        RealRackHatch = new GT_Hatch_RackComputationMonitor(
            18958,
            "NameRealRackHatch",
            TextLocalization.NameRealRackHatch,
            12,
            true).getStackForm(1);
        GTCMItemList.RealRackHatch.set(RealRackHatch);

        WirelessDataInputHatch = new GT_Hatch_WirelessData_input(
            18957,
            "NameWirelessDataInputHatch",
            TextLocalization.NameWirelessDataInputHatch,
            12).getStackForm(1);
        GTCMItemList.WirelessDataInputHatch.set(WirelessDataInputHatch);

        WirelessDataOutputHatch = new GT_Hatch_WirelessData_output(
            18956,
            "NameWirelessDataOutputHatch",
            TextLocalization.NameWirelessDataOutputHatch,
            12).getStackForm(1);
        GTCMItemList.WirelessDataOutputHatch.set(WirelessDataOutputHatch);

        // #tr NameLegendaryWirelessEnergyHatch
        // # Legendary Wireless Energy Hatch
        // #zh_CN 传奇无线能源仓
        LegendaryWirelessEnergyHatch = new GT_Hatch_InfiniteWirelessMulti(
            18798,
            "NameLegendaryWirelessEnergyHatch",
            TextEnums.tr("NameLegendaryWirelessEnergyHatch"),
            13,
            536870912).getStackForm(1);
        GTCMItemList.LegendaryWirelessEnergyHatch.set(LegendaryWirelessEnergyHatch);

        // #tr NameHarmoniousWirelessEnergyHatch
        // # Harmonious Wireless Energy Hatch
        // #zh_CN 鸿蒙无线能源仓
        HarmoniousWirelessEnergyHatch = new GT_Hatch_InfiniteWirelessMulti(
            18799,
            "NameHarmoniousWirelessEnergyHatch",
            TextEnums.tr("NameHarmoniousWirelessEnergyHatch"),
            14,
            2147483647).getStackForm(1);
        GTCMItemList.HarmoniousWirelessEnergyHatch.set(HarmoniousWirelessEnergyHatch);

        // region Modularized Stuff

        if (Config.EnableModularizedMachineSystem) {

            // #tr NameDynamicParallelControllerT1
            // # Dynamic Parallel Controller Module T1
            // #zh_CN 动态并行控制器模块T1
            DynamicParallelControllerT1 = new DynamicParallelController(
                18800,
                "NameDynamicParallelControllerT1",
                TextEnums.tr("NameDynamicParallelControllerT1"),
                7,
                ParallelOfParallelController[0]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT1.set(DynamicParallelControllerT1);

            // #tr NameDynamicParallelControllerT2
            // # Dynamic Parallel Controller Module T2
            // #zh_CN 动态并行控制器模块T2
            DynamicParallelControllerT2 = new DynamicParallelController(
                18801,
                "NameDynamicParallelControllerT2",
                TextEnums.tr("NameDynamicParallelControllerT2"),
                8,
                ParallelOfParallelController[1]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT2.set(DynamicParallelControllerT2);

            // #tr NameDynamicParallelControllerT3
            // # Dynamic Parallel Controller Module T3
            // #zh_CN 动态并行控制器模块T3
            DynamicParallelControllerT3 = new DynamicParallelController(
                18802,
                "NameDynamicParallelControllerT3",
                TextEnums.tr("NameDynamicParallelControllerT3"),
                9,
                ParallelOfParallelController[2]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT3.set(DynamicParallelControllerT3);

            // #tr NameDynamicParallelControllerT4
            // # Dynamic Parallel Controller Module T4
            // #zh_CN 动态并行控制器模块T4
            DynamicParallelControllerT4 = new DynamicParallelController(
                18803,
                "NameDynamicParallelControllerT4",
                TextEnums.tr("NameDynamicParallelControllerT4"),
                10,
                ParallelOfParallelController[3]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT4.set(DynamicParallelControllerT4);

            // #tr NameDynamicParallelControllerT5
            // # Dynamic Parallel Controller Module T5
            // #zh_CN 动态并行控制器模块T5
            DynamicParallelControllerT5 = new DynamicParallelController(
                18804,
                "NameDynamicParallelControllerT5",
                TextEnums.tr("NameDynamicParallelControllerT5"),
                11,
                ParallelOfParallelController[4]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT5.set(DynamicParallelControllerT5);

            // #tr NameDynamicParallelControllerT6
            // # Dynamic Parallel Controller Module T6
            // #zh_CN 动态并行控制器模块T6
            DynamicParallelControllerT6 = new DynamicParallelController(
                18805,
                "NameDynamicParallelControllerT6",
                TextEnums.tr("NameDynamicParallelControllerT6"),
                12,
                ParallelOfParallelController[5]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT6.set(DynamicParallelControllerT6);

            // #tr NameDynamicParallelControllerT7
            // # Dynamic Parallel Controller Module T7
            // #zh_CN 动态并行控制器模块T7
            DynamicParallelControllerT7 = new DynamicParallelController(
                18806,
                "NameDynamicParallelControllerT7",
                TextEnums.tr("NameDynamicParallelControllerT7"),
                13,
                ParallelOfParallelController[6]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT7.set(DynamicParallelControllerT7);

            // #tr NameDynamicParallelControllerT8
            // # Dynamic Parallel Controller Module T8
            // #zh_CN 动态并行控制器模块T8
            DynamicParallelControllerT8 = new DynamicParallelController(
                18807,
                "NameDynamicParallelControllerT8",
                TextEnums.tr("NameDynamicParallelControllerT8"),
                14,
                ParallelOfParallelController[7]).getStackForm(1);
            GTCMItemList.DynamicParallelControllerT8.set(DynamicParallelControllerT8);

            // #tr NameStaticParallelControllerT1
            // # Static Parallel Controller Module T1
            // #zh_CN 静态并行控制器模块T1
            StaticParallelControllerT1 = new StaticParallelController(
                18808,
                "NameStaticParallelControllerT1",
                TextEnums.tr("NameStaticParallelControllerT1"),
                7,
                ParallelOfParallelController[0]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT1.set(StaticParallelControllerT1);

            // #tr NameStaticParallelControllerT2
            // # Static Parallel Controller Module T2
            // #zh_CN 静态并行控制器模块T2
            StaticParallelControllerT2 = new StaticParallelController(
                18809,
                "NameStaticParallelControllerT2",
                TextEnums.tr("NameStaticParallelControllerT2"),
                8,
                ParallelOfParallelController[1]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT2.set(StaticParallelControllerT2);

            // #tr NameStaticParallelControllerT3
            // # Static Parallel Controller Module T3
            // #zh_CN 静态并行控制器模块T3
            StaticParallelControllerT3 = new StaticParallelController(
                18810,
                "NameStaticParallelControllerT3",
                TextEnums.tr("NameStaticParallelControllerT3"),
                9,
                ParallelOfParallelController[2]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT3.set(StaticParallelControllerT3);

            // #tr NameStaticParallelControllerT4
            // # Static Parallel Controller Module T4
            // #zh_CN 静态并行控制器模块T4
            StaticParallelControllerT4 = new StaticParallelController(
                18811,
                "NameStaticParallelControllerT4",
                TextEnums.tr("NameStaticParallelControllerT4"),
                10,
                ParallelOfParallelController[3]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT4.set(StaticParallelControllerT4);

            // #tr NameStaticParallelControllerT5
            // # Static Parallel Controller Module T5
            // #zh_CN 静态并行控制器模块T5
            StaticParallelControllerT5 = new StaticParallelController(
                18812,
                "NameStaticParallelControllerT5",
                TextEnums.tr("NameStaticParallelControllerT5"),
                11,
                ParallelOfParallelController[4]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT5.set(StaticParallelControllerT5);

            // #tr NameStaticParallelControllerT6
            // # Static Parallel Controller Module T6
            // #zh_CN 静态并行控制器模块T6
            StaticParallelControllerT6 = new StaticParallelController(
                18813,
                "NameStaticParallelControllerT6",
                TextEnums.tr("NameStaticParallelControllerT6"),
                12,
                ParallelOfParallelController[5]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT6.set(StaticParallelControllerT6);

            // #tr NameStaticParallelControllerT7
            // # Static Parallel Controller Module T7
            // #zh_CN 静态并行控制器模块T7
            StaticParallelControllerT7 = new StaticParallelController(
                18814,
                "NameStaticParallelControllerT7",
                TextEnums.tr("NameStaticParallelControllerT7"),
                13,
                ParallelOfParallelController[6]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT7.set(StaticParallelControllerT7);

            // #tr NameStaticParallelControllerT8
            // # Static Parallel Controller Module T8
            // #zh_CN 静态并行控制器模块T8
            StaticParallelControllerT8 = new StaticParallelController(
                18815,
                "NameStaticParallelControllerT8",
                TextEnums.tr("NameStaticParallelControllerT8"),
                14,
                ParallelOfParallelController[7]).getStackForm(1);
            GTCMItemList.StaticParallelControllerT8.set(StaticParallelControllerT8);

            // #tr NameDynamicSpeedControllerT1
            // # Dynamic Speed Controller Module T1
            // #zh_CN 动态速度控制器模块T1
            DynamicSpeedControllerT1 = new DynamicSpeedController(
                18816,
                "NameDynamicSpeedControllerT1",
                TextEnums.tr("NameDynamicSpeedControllerT1"),
                7,
                SpeedMultiplierOfSpeedController[0]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT1.set(DynamicSpeedControllerT1);

            // #tr NameDynamicSpeedControllerT2
            // # Dynamic Speed Controller Module T2
            // #zh_CN 动态速度控制器模块T2
            DynamicSpeedControllerT2 = new DynamicSpeedController(
                18817,
                "NameDynamicSpeedControllerT2",
                TextEnums.tr("NameDynamicSpeedControllerT2"),
                8,
                SpeedMultiplierOfSpeedController[1]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT2.set(DynamicSpeedControllerT2);

            // #tr NameDynamicSpeedControllerT3
            // # Dynamic Speed Controller Module T3
            // #zh_CN 动态速度控制器模块T3
            DynamicSpeedControllerT3 = new DynamicSpeedController(
                18818,
                "NameDynamicSpeedControllerT3",
                TextEnums.tr("NameDynamicSpeedControllerT3"),
                9,
                SpeedMultiplierOfSpeedController[2]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT3.set(DynamicSpeedControllerT3);

            // #tr NameDynamicSpeedControllerT4
            // # Dynamic Speed Controller Module T4
            // #zh_CN 动态速度控制器模块T4
            DynamicSpeedControllerT4 = new DynamicSpeedController(
                18819,
                "NameDynamicSpeedControllerT4",
                TextEnums.tr("NameDynamicSpeedControllerT4"),
                10,
                SpeedMultiplierOfSpeedController[3]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT4.set(DynamicSpeedControllerT4);

            // #tr NameDynamicSpeedControllerT5
            // # Dynamic Speed Controller Module T5
            // #zh_CN 动态速度控制器模块T5
            DynamicSpeedControllerT5 = new DynamicSpeedController(
                18820,
                "NameDynamicSpeedControllerT5",
                TextEnums.tr("NameDynamicSpeedControllerT5"),
                11,
                SpeedMultiplierOfSpeedController[4]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT5.set(DynamicSpeedControllerT5);

            // #tr NameDynamicSpeedControllerT6
            // # Dynamic Speed Controller Module T6
            // #zh_CN 动态速度控制器模块T6
            DynamicSpeedControllerT6 = new DynamicSpeedController(
                18821,
                "NameDynamicSpeedControllerT6",
                TextEnums.tr("NameDynamicSpeedControllerT6"),
                12,
                SpeedMultiplierOfSpeedController[5]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT6.set(DynamicSpeedControllerT6);

            // #tr NameDynamicSpeedControllerT7
            // # Dynamic Speed Controller Module T7
            // #zh_CN 动态速度控制器模块T7
            DynamicSpeedControllerT7 = new DynamicSpeedController(
                18822,
                "NameDynamicSpeedControllerT7",
                TextEnums.tr("NameDynamicSpeedControllerT7"),
                13,
                SpeedMultiplierOfSpeedController[6]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT7.set(DynamicSpeedControllerT7);

            // #tr NameDynamicSpeedControllerT8
            // # Dynamic Speed Controller Module T8
            // #zh_CN 动态速度控制器模块T8
            DynamicSpeedControllerT8 = new DynamicSpeedController(
                18823,
                "NameDynamicSpeedControllerT8",
                TextEnums.tr("NameDynamicSpeedControllerT8"),
                14,
                SpeedMultiplierOfSpeedController[7]).getStackForm(1);
            GTCMItemList.DynamicSpeedControllerT8.set(DynamicSpeedControllerT8);

            // #tr NameStaticSpeedControllerT1
            // # Static Speed Controller Module T1
            // #zh_CN 静态速度控制器模块T1
            StaticSpeedControllerT1 = new StaticSpeedController(
                18824,
                "NameStaticSpeedControllerT1",
                TextEnums.tr("NameStaticSpeedControllerT1"),
                7,
                SpeedMultiplierOfSpeedController[0]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT1.set(StaticSpeedControllerT1);

            // #tr NameStaticSpeedControllerT2
            // # Static Speed Controller Module T2
            // #zh_CN 静态速度控制器模块T2
            StaticSpeedControllerT2 = new StaticSpeedController(
                18825,
                "NameStaticSpeedControllerT2",
                TextEnums.tr("NameStaticSpeedControllerT2"),
                8,
                SpeedMultiplierOfSpeedController[1]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT2.set(StaticSpeedControllerT2);

            // #tr NameStaticSpeedControllerT3
            // # Static Speed Controller Module T3
            // #zh_CN 静态速度控制器模块T3
            StaticSpeedControllerT3 = new StaticSpeedController(
                18826,
                "NameStaticSpeedControllerT3",
                TextEnums.tr("NameStaticSpeedControllerT3"),
                9,
                SpeedMultiplierOfSpeedController[2]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT3.set(StaticSpeedControllerT3);

            // #tr NameStaticSpeedControllerT4
            // # Static Speed Controller Module T4
            // #zh_CN 静态速度控制器模块T4
            StaticSpeedControllerT4 = new StaticSpeedController(
                18827,
                "NameStaticSpeedControllerT4",
                TextEnums.tr("NameStaticSpeedControllerT4"),
                10,
                SpeedMultiplierOfSpeedController[3]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT4.set(StaticSpeedControllerT4);

            // #tr NameStaticSpeedControllerT5
            // # Static Speed Controller Module T5
            // #zh_CN 静态速度控制器模块T5
            StaticSpeedControllerT5 = new StaticSpeedController(
                18828,
                "NameStaticSpeedControllerT5",
                TextEnums.tr("NameStaticSpeedControllerT5"),
                11,
                SpeedMultiplierOfSpeedController[4]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT5.set(StaticSpeedControllerT5);

            // #tr NameStaticSpeedControllerT6
            // # Static Speed Controller Module T6
            // #zh_CN 静态速度控制器模块T6
            StaticSpeedControllerT6 = new StaticSpeedController(
                18829,
                "NameStaticSpeedControllerT6",
                TextEnums.tr("NameStaticSpeedControllerT6"),
                12,
                SpeedMultiplierOfSpeedController[5]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT6.set(StaticSpeedControllerT6);

            // #tr NameStaticSpeedControllerT7
            // # Static Speed Controller Module T7
            // #zh_CN 静态速度控制器模块T7
            StaticSpeedControllerT7 = new StaticSpeedController(
                18830,
                "NameStaticSpeedControllerT7",
                TextEnums.tr("NameStaticSpeedControllerT7"),
                13,
                SpeedMultiplierOfSpeedController[6]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT7.set(StaticSpeedControllerT7);

            // #tr NameStaticSpeedControllerT8
            // # Static Speed Controller Module T8
            // #zh_CN 静态速度控制器模块T8
            StaticSpeedControllerT8 = new StaticSpeedController(
                18831,
                "NameStaticSpeedControllerT8",
                TextEnums.tr("NameStaticSpeedControllerT8"),
                14,
                SpeedMultiplierOfSpeedController[7]).getStackForm(1);
            GTCMItemList.StaticSpeedControllerT8.set(StaticSpeedControllerT8);

            // #tr NameStaticPowerConsumptionControllerT1
            // # Static Power Consumption Controller Module T1
            // #zh_CN 静态耗能控制器模块T1
            StaticPowerConsumptionControllerT1 = new StaticPowerConsumptionController(
                18832,
                "NameStaticPowerConsumptionControllerT1",
                TextEnums.tr("NameStaticPowerConsumptionControllerT1"),
                7,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[0]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT1.set(StaticPowerConsumptionControllerT1);

            // #tr NameStaticPowerConsumptionControllerT2
            // # Static Power Consumption Controller Module T2
            // #zh_CN 静态耗能控制器模块T2
            StaticPowerConsumptionControllerT2 = new StaticPowerConsumptionController(
                18833,
                "NameStaticPowerConsumptionControllerT2",
                TextEnums.tr("NameStaticPowerConsumptionControllerT2"),
                8,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[1]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT2.set(StaticPowerConsumptionControllerT2);

            // #tr NameStaticPowerConsumptionControllerT3
            // # Static Power Consumption Controller Module T3
            // #zh_CN 静态耗能控制器模块T3
            StaticPowerConsumptionControllerT3 = new StaticPowerConsumptionController(
                18834,
                "NameStaticPowerConsumptionControllerT3",
                TextEnums.tr("NameStaticPowerConsumptionControllerT3"),
                9,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[2]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT3.set(StaticPowerConsumptionControllerT3);

            // #tr NameStaticPowerConsumptionControllerT4
            // # Static Power Consumption Controller Module T4
            // #zh_CN 静态耗能控制器模块T4
            StaticPowerConsumptionControllerT4 = new StaticPowerConsumptionController(
                18835,
                "NameStaticPowerConsumptionControllerT4",
                TextEnums.tr("NameStaticPowerConsumptionControllerT4"),
                10,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[3]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT4.set(StaticPowerConsumptionControllerT4);

            // #tr NameStaticPowerConsumptionControllerT5
            // # Static Power Consumption Controller Module T5
            // #zh_CN 静态耗能控制器模块T5
            StaticPowerConsumptionControllerT5 = new StaticPowerConsumptionController(
                18836,
                "NameStaticPowerConsumptionControllerT5",
                TextEnums.tr("NameStaticPowerConsumptionControllerT5"),
                11,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[4]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT5.set(StaticPowerConsumptionControllerT5);

            // #tr NameStaticPowerConsumptionControllerT6
            // # Static Power Consumption Controller Module T6
            // #zh_CN 静态耗能控制器模块T6
            StaticPowerConsumptionControllerT6 = new StaticPowerConsumptionController(
                18837,
                "NameStaticPowerConsumptionControllerT6",
                TextEnums.tr("NameStaticPowerConsumptionControllerT6"),
                12,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[5]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT6.set(StaticPowerConsumptionControllerT6);

            // #tr NameStaticPowerConsumptionControllerT7
            // # Static Power Consumption Controller Module T7
            // #zh_CN 静态耗能控制器模块T7
            StaticPowerConsumptionControllerT7 = new StaticPowerConsumptionController(
                18838,
                "NameStaticPowerConsumptionControllerT7",
                TextEnums.tr("NameStaticPowerConsumptionControllerT7"),
                13,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[6]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT7.set(StaticPowerConsumptionControllerT7);

            // #tr NameStaticPowerConsumptionControllerT8
            // # Static Power Consumption Controller Module T8
            // #zh_CN 静态耗能控制器模块T8
            StaticPowerConsumptionControllerT8 = new StaticPowerConsumptionController(
                18839,
                "NameStaticPowerConsumptionControllerT8",
                TextEnums.tr("NameStaticPowerConsumptionControllerT8"),
                14,
                (float) PowerConsumptionMultiplierOfPowerConsumptionController[7]).getStackForm(1);
            GTCMItemList.StaticPowerConsumptionControllerT8.set(StaticPowerConsumptionControllerT8);

            // #tr NameLowSpeedPerfectOverclockController
            // # Low Speed Perfect Overclock Controller Module
            // #zh_CN 低速无损超频控制器模块
            LowSpeedPerfectOverclockController = new StaticOverclockController(
                18840,
                "NameLowSpeedPerfectOverclockController",
                TextEnums.tr("NameLowSpeedPerfectOverclockController"),
                12,
                2,
                2).getStackForm(1);
            GTCMItemList.LowSpeedPerfectOverclockController.set(LowSpeedPerfectOverclockController);

            // #tr NamePerfectOverclockController
            // # Perfect Overclock Controller Module
            // #zh_CN 无损超频控制器模块
            PerfectOverclockController = new StaticOverclockController(
                18841,
                "NamePerfectOverclockController",
                TextEnums.tr("NamePerfectOverclockController"),
                13,
                4,
                4).getStackForm(1);
            GTCMItemList.PerfectOverclockController.set(PerfectOverclockController);

            // #tr NameSingularityPerfectOverclockController
            // # Singularity Perfect Overclock Controller Module
            // #zh_CN 奇点无损超频控制器模块
            SingularityPerfectOverclockController = new StaticOverclockController(
                18842,
                "NameSingularityPerfectOverclockController",
                TextEnums.tr("NameSingularityPerfectOverclockController"),
                14,
                8,
                4).getStackForm(1);
            GTCMItemList.SingularityPerfectOverclockController.set(SingularityPerfectOverclockController);

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
            ExecutionCore = new ExecutionCore(18843, "NameExecutionCore", TextEnums.tr("NameExecutionCore"), 12)
                .getStackForm(1);
            GTCMItemList.ExecutionCore.set(ExecutionCore);

            // #tr NameAdvancedExecutionCore
            // # Advanced Execution Core Module
            // #zh_CN 高级执行核心模块
            AdvancedExecutionCore = new AdvExecutionCore(
                18844,
                "NameAdvancedExecutionCore",
                TextEnums.tr("NameAdvancedExecutionCore"),
                13).getStackForm(1);
            GTCMItemList.AdvancedExecutionCore.set(AdvancedExecutionCore);

            // #tr NamePerfectExecutionCore
            // # Perfect Execution Core Module
            // #zh_CN 完美执行核心模块
            PerfectExecutionCore = new PerfectExecutionCore(
                18845,
                "NamePerfectExecutionCore",
                TextEnums.tr("NamePerfectExecutionCore"),
                14).getStackForm(1);
            GTCMItemList.PerfectExecutionCore.set(PerfectExecutionCore);
        }

        // endregion

    }

    public static void loadMachinePostInit() {
        //
        if (Config.EnableSpaceApiaryModule) {
            SpaceApiaryT1 = new TST_SpaceApiary.TST_SpaceApiaryT1(
                19042,
                "NameSpaceApiaryT1",
                TextLocalization.NameSpaceApiaryT1).getStackForm(1);
            GTCMItemList.SpaceApiaryT1.set(SpaceApiaryT1);

            SpaceApiaryT2 = new TST_SpaceApiary.TST_SpaceApiaryT2(
                19043,
                "NameSpaceApiaryT2",
                TextLocalization.NameSpaceApiaryT2).getStackForm(1);
            GTCMItemList.SpaceApiaryT2.set(SpaceApiaryT2);

            SpaceApiaryT3 = new TST_SpaceApiary.TST_SpaceApiaryT3(
                19044,
                "NameSpaceApiaryT3",
                TextLocalization.NameSpaceApiaryT3).getStackForm(1);
            GTCMItemList.SpaceApiaryT3.set(SpaceApiaryT3);

            SpaceApiaryT4 = new TST_SpaceApiary.TST_SpaceApiaryT4(
                19045,
                "NameSpaceApiaryT4",
                TextLocalization.NameSpaceApiaryT4).getStackForm(1);
            GTCMItemList.SpaceApiaryT4.set(SpaceApiaryT4);
        }

    }
}
