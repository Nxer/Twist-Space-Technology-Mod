package com.Nxer.TwistSpaceTechnology.loader;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_HolySeparator;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IntensifyChemicalDistorter;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDomainConstructor;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDrivePressureFormer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticMixer;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MiracleTop;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MoleculeDeconstructor;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PhysicalFormSwitcher;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_Silksong;
import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_SpaceScaler;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.GT_MetaTileEntity_Hatch_Air;
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

    public static ItemStack TestMultiStructureMainMachine;
    public static ItemStack TestMultiStructureSubMachine;

    // Single Block
    public static ItemStack InfiniteAirHatch;

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

        // endregion

        // region Single block Machine

        //
        InfiniteAirHatch = new GT_MetaTileEntity_Hatch_Air(
            18999,
            "NameInfiniteAirHatch",
            TextLocalization.NameInfiniteAirHatch,
            9).getStackForm(1);
        GTCMItemList.InfiniteAirHatch.set(InfiniteAirHatch);

        // TestMultiStructureMainMachine = new Test_MultiStructMachine(
        // 19013,
        // "NameTestMultiStructureMainMachine",
        // TextLocalization.NameTestMultiStructureMainMachine).getStackForm(1);
        // GTCMItemList.TestMultiStructureMainMachine.set(TestMultiStructureSubMachine);
        //
        // TestMultiStructureSubMachine = new Test_SubStructureMachine(
        // 19014,
        // "NameTestMultiStructureSubMachine",
        // TextLocalization.NameTestMultiStructureSubMachine).getStackForm(1);
        // GTCMItemList.TestMultiStructureSubMachine.set(TestMultiStructureMainMachine);

    }
}
