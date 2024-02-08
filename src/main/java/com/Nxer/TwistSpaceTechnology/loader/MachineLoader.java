package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.NameElvenWorkshop;

import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_ElvenWorkshop;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_HyperSpacetimeTransformer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_HolySeparator;
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
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BiosphereIII;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_CleanRoom;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_Computer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_IndistinctTentacle;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeIndustrialCokingFactory;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_LargeSteamForgeHammer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MiracleDoor;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_Scavenger;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_ThermalEnergyDevourer;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_VacuumFilterExtractor;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure.spaceStationModular.TST_MegaUniversalSpaceStation;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_BufferedEnergyHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_InfiniteWirelessDynamoHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_Air;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_DualInput;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_Mana;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_UncertaintyDebug;
import com.Nxer.TwistSpaceTechnology.common.ship.Ship;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.machines.TST_CircuitConverter;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_ArtificialStar;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPLauncher;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPReceiver;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.machines.TST_OreProcessingFactory;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

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

    // Single Block
    public static ItemStack InfiniteAirHatch;
    public static ItemStack ManaHatch;
    public static ItemStack InfiniteWirelessDynamoHatch;
    public static ItemStack DualInputBuffer_LuV;
    public static ItemStack DualInputBuffer_ZPM;
    public static ItemStack DualInputBuffer_UV;
    public static ItemStack DualInputBuffer_UHV;

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

    // test
    // public static ItemStack TestMachine;

    public static void loadMachines() {

        EntityList.addMapping(Ship.class, "Ship", 114);
        // test
        // TestMachine = new GTCM_TestMultiMachine(19000, "TestMachine", "TestMachine").getStackForm(1);

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
        ElvenWorkshop = new GTCM_ElvenWorkshop(19500, "NameElvenWorkshop", NameElvenWorkshop).getStackForm(1);
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

        superCleanRoom = new TST_CleanRoom(19024, "multimachine.cleanroom", "Cleanroom Controller").getStackForm(1);
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
        AstralComputingArray = new TST_Computer(19029, "NameAstralComputingArray", "Astral Computing Array")
            .getStackForm(1);
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
        ManaHatch = new GT_MetaTileEntity_Hatch_Mana(18979, "NameManaHatch", TextLocalization.NameManaHatch, 9)
            .getStackForm(1);
        GTCMItemList.ManaHatch.set(ManaHatch);

        //
        DualInputBuffer_LuV = new GT_MetaTileEntity_Hatch_DualInput(
            18980,
            "NameDualInputBuffer_LuV",
            TextLocalization.NameDualInputBuffer_LuV,
            6).getStackForm(1);
        GTCMItemList.DualInputBuffer_LuV.set(DualInputBuffer_LuV);

        //
        DualInputBuffer_ZPM = new GT_MetaTileEntity_Hatch_DualInput(
            18981,
            "NameDualInputBuffer_ZPM",
            TextLocalization.NameDualInputBuffer_ZPM,
            7).getStackForm(1);
        GTCMItemList.DualInputBuffer_ZPM.set(DualInputBuffer_ZPM);

        //
        DualInputBuffer_UV = new GT_MetaTileEntity_Hatch_DualInput(
            18982,
            "NameDualInputBuffer_UV",
            TextLocalization.NameDualInputBuffer_UV,
            8).getStackForm(1);
        GTCMItemList.DualInputBuffer_UV.set(DualInputBuffer_UV);

        // region buffered energy hatch
        DualInputBuffer_UHV = new GT_MetaTileEntity_Hatch_DualInput(
            18983,
            "NameDualInputBuffer_UHV",
            TextLocalization.NameDualInputBuffer_UHV,
            9).getStackForm(1);
        GTCMItemList.DualInputBuffer_UHV.set(DualInputBuffer_UHV);

        BufferedEnergyHatchLV = new GT_Hatch_BufferedEnergyHatch(
            18984,
            "NameBufferedEnergyHatchLV",
            "Buffered Energy Hatch LV",
            1,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchMV = new GT_Hatch_BufferedEnergyHatch(
            18985,
            "NameBufferedEnergyHatchMV",
            "Buffered Energy Hatch MV",
            2,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchHV = new GT_Hatch_BufferedEnergyHatch(
            18986,
            "NameBufferedEnergyHatchHV",
            "Buffered Energy Hatch HV",
            3,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchEV = new GT_Hatch_BufferedEnergyHatch(
            18987,
            "NameBufferedEnergyHatchEV",
            "Buffered Energy Hatch EV",
            4,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchIV = new GT_Hatch_BufferedEnergyHatch(
            18988,
            "NameBufferedEnergyHatchIV",
            "Buffered Energy Hatch IV",
            5,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchLuV = new GT_Hatch_BufferedEnergyHatch(
            18989,
            "NameBufferedEnergyHatchLuV",
            "Buffered Energy Hatch LuV",
            6,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchZPM = new GT_Hatch_BufferedEnergyHatch(
            18990,
            "NameBufferedEnergyHatchZPM",
            "Buffered Energy Hatch ZPM",
            7,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUV = new GT_Hatch_BufferedEnergyHatch(
            18991,
            "NameBufferedEnergyHatchUV",
            "Buffered Energy Hatch UV",
            8,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUHV = new GT_Hatch_BufferedEnergyHatch(
            18992,
            "NameBufferedEnergyHatchUHV",
            "Buffered Energy Hatch UHV",
            9,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUEV = new GT_Hatch_BufferedEnergyHatch(
            18993,
            "NameBufferedEnergyHatchUEV",
            "Buffered Energy Hatch UEV",
            10,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUIV = new GT_Hatch_BufferedEnergyHatch(
            18994,
            "NameBufferedEnergyHatchUIV",
            "Buffered Energy Hatch UIV",
            11,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUMV = new GT_Hatch_BufferedEnergyHatch(
            18995,
            "NameBufferedEnergyHatchUMV",
            "Buffered Energy Hatch UMV",
            12,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchUXV = new GT_Hatch_BufferedEnergyHatch(
            18996,
            "NameBufferedEnergyHatchUXV",
            "Buffered Energy Hatch UXV",
            13,
            16,
            null).getStackForm(1);
        BufferedEnergyHatchMAX = new GT_Hatch_BufferedEnergyHatch(
            18997,
            "NameBufferedEnergyHatchMAX",
            "Buffered Energy Hatch MAX",
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
        // endregion

    }
}
