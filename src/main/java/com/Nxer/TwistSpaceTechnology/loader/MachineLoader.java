package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.*;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.*;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure.GT_TileEntity_MegaUniversalSpaceStation;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_InfiniteWirelessDynamoHatch;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_Air;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_ArtificialStar;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPLauncher;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPReceiver;
import com.Nxer.TwistSpaceTechnology.system.OreProcess.machines.TST_OreProcessingFactory;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

public class MachineLoader {

    public static ItemStack IntensifyChemicalDistorter; // INTENSIFY_CHEMICAL_DISTORTER
    public static ItemStack PreciseHighEnergyPhotonicQuantumMaster; // INTENSIFY_CHEMICAL_DISTORTER
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

    // Single Block
    public static ItemStack InfiniteAirHatch;
    public static ItemStack InfiniteWirelessDynamoHatch;

    // test
    // public static ItemStack TestMachine;

    public static void loadMachines() {

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
        DSPLauncher = new TST_DSPLauncher(19013, "NameDSPLauncher", NameDSPLauncher).getStackForm(1);
        GTCMItemList.DSPLauncher.set(DSPLauncher);

        //
        DSPReceiver = new TST_DSPReceiver(19014, "NameDSPReceiver", NameDSPReceiver).getStackForm(1);
        GTCMItemList.DSPReceiver.set(DSPReceiver);

        //
        ArtificialStar = new TST_ArtificialStar(19015, "NameArtificialStar", NameArtificialStar).getStackForm(1);
        GTCMItemList.ArtificialStar.set(ArtificialStar);

        MiracleDoor = new TST_MiracleDoor(19016, "NameMiracleDoor", NameMiracleDoor).getStackForm(1);
        GTCMItemList.MiracleDoor.set(MiracleDoor);

        OreProcessingFactory = new TST_OreProcessingFactory(19017, "NameOreProcessingFactory", NameOreProcessingFactory)
            .getStackForm(1);
        GTCMItemList.OreProcessingFactory.set(OreProcessingFactory);

        megaUniversalSpaceStation = new GT_TileEntity_MegaUniversalSpaceStation(
            19018,
            "NameMegaUniversalSpaceStation",
            NameMegaUniversalSpaceStation).getStackForm(1);
        GTCMItemList.megaUniversalSpaceStation.set(megaUniversalSpaceStation);

        stellarMaterialSiphon = new GT_TileEntity_StellarMaterialSiphon(
            19019,
            "NameStellarMaterialSiphon",
            NameStellarMaterialSiphon).getStackForm(1);
        GTCMItemList.StellarMaterialSiphon.set(stellarMaterialSiphon);
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
            NameInfiniteWirelessDynamoHatch,
            14).getStackForm(1);
        GTCMItemList.InfiniteWirelessDynamoHatch.set(InfiniteWirelessDynamoHatch);

    }
}
