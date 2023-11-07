package com.GTNH_Community.gtnhcommunitymod.loader;

import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameHolySeparator;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameInfiniteAirHatch;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameIntensifyChemicalDistorter;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameMagneticDomainConstructor;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameMagneticDrivePressureFormer;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameMagneticMixer;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameMiracleTop;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameMoleculeDeconstructor;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NamePhysicalFormSwitcher;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NamePreciseHighEnergyPhotonicQuantumMaster;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameSilksong;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameSpaceScaler;

import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_HolySeparator;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_IntensifyChemicalDistorter;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_MagneticDomainConstructor;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_MagneticDrivePressureFormer;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_MagneticMixer;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_MiracleTop;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_MoleculeDeconstructor;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_PhysicalFormSwitcher;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_Silksong;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_SpaceScaler;
import com.GTNH_Community.gtnhcommunitymod.common.machine.singleBlock.GT_MetaTileEntity_Hatch_Air;

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

    // Single Block
    public static ItemStack InfiniteAirHatch;

    public static void loadMachines() {

        // region multi Machine controller

        //
        IntensifyChemicalDistorter = new GT_TileEntity_IntensifyChemicalDistorter(
            19001,
            "NameIntensifyChemicalDistorter",
            NameIntensifyChemicalDistorter).getStackForm(1);
        GTCMItemList.IntensifyChemicalDistorter.set(IntensifyChemicalDistorter);

        //
        PreciseHighEnergyPhotonicQuantumMaster = new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(
            19002,
            "NamePreciseHighEnergyPhotonicQuantumMaster",
            NamePreciseHighEnergyPhotonicQuantumMaster).getStackForm(1);
        GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.set(PreciseHighEnergyPhotonicQuantumMaster);

        //
        MiracleTop = new GT_TileEntity_MiracleTop(19003, NameMiracleTop, NameMiracleTop).getStackForm(1);
        GTCMItemList.MiracleTop.set(MiracleTop);

        //
        MagneticDrivePressureFormer = new GT_TileEntity_MagneticDrivePressureFormer(
            19004,
            "NameMagneticDrivePressureFormer",
            NameMagneticDrivePressureFormer).getStackForm(1);
        GTCMItemList.MagneticDrivePressureFormer.set(MagneticDrivePressureFormer);

        //
        PhysicalFormSwitcher = new GT_TileEntity_PhysicalFormSwitcher(
            19005,
            "NamePhysicalFormSwitcher",
            NamePhysicalFormSwitcher).getStackForm(1);
        GTCMItemList.PhysicalFormSwitcher.set(PhysicalFormSwitcher);

        //
        MagneticMixer = new GT_TileEntity_MagneticMixer(19006, "NameMagneticMixer", NameMagneticMixer).getStackForm(1);
        GTCMItemList.MagneticMixer.set(MagneticMixer);

        //
        MagneticDomainConstructor = new GT_TileEntity_MagneticDomainConstructor(
            19007,
            "NameMagneticDomainConstructor",
            NameMagneticDomainConstructor).getStackForm(1);
        GTCMItemList.MagneticDomainConstructor.set(MagneticDomainConstructor);

        //
        Silksong = new GT_TileEntity_Silksong(19008, "NameSilksong", NameSilksong).getStackForm(1);
        GTCMItemList.Silksong.set(Silksong);

        //
        HolySeparator = new GT_TileEntity_HolySeparator(19009, "NameHolySeparator", NameHolySeparator).getStackForm(1);
        GTCMItemList.HolySeparator.set(HolySeparator);

        //
        SpaceScaler = new GT_TileEntity_SpaceScaler(19010, "NameSpaceScaler", NameSpaceScaler).getStackForm(1);
        GTCMItemList.SpaceScaler.set(SpaceScaler);

        //
        MoleculeDeconstructor = new GT_TileEntity_MoleculeDeconstructor(
            19011,
            "NameMoleculeDeconstructor",
            NameMoleculeDeconstructor).getStackForm(1);
        GTCMItemList.MoleculeDeconstructor.set(MoleculeDeconstructor);

        // endregion

        // region Single block Machine

        //
        InfiniteAirHatch = new GT_MetaTileEntity_Hatch_Air(18999, "NameInfiniteAirHatch", NameInfiniteAirHatch, 9)
            .getStackForm(1);
        GTCMItemList.InfiniteAirHatch.set(InfiniteAirHatch);
    }
}
