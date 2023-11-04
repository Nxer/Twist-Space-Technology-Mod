package com.GTNH_Community.gtnhcommunitymod.loader;

import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameIntensifyChemicalDistorter;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameMagneticDrivePressureFormer;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameMiracleTop;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NamePhysicalFormSwitcher;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NamePreciseHighEnergyPhotonicQuantumMaster;

import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_IntensifyChemicalDistorter;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_MagneticDrivePressureFormer;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_MiracleTop;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_PhysicalFormSwitcher;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster;

public class MachineLoader {

    public static ItemStack IntensifyChemicalDistorter; // INTENSIFY_CHEMICAL_DISTORTER
    public static ItemStack PreciseHighEnergyPhotonicQuantumMaster; // INTENSIFY_CHEMICAL_DISTORTER
    public static ItemStack MiracleTop;
    public static ItemStack MagneticDrivePressureFormer;
    public static ItemStack PhysicalFormSwitcher;

    public static void loadMachines() {

        //
        IntensifyChemicalDistorter = new GT_TileEntity_IntensifyChemicalDistorter(
            19001,
            NameIntensifyChemicalDistorter,
            NameIntensifyChemicalDistorter).getStackForm(1);
        GTCMItemList.IntensifyChemicalDistorter.set(IntensifyChemicalDistorter);

        //
        PreciseHighEnergyPhotonicQuantumMaster = new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(
            19002,
            NamePreciseHighEnergyPhotonicQuantumMaster,
            NamePreciseHighEnergyPhotonicQuantumMaster).getStackForm(1);
        GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.set(PreciseHighEnergyPhotonicQuantumMaster);

        //
        MiracleTop = new GT_TileEntity_MiracleTop(19003, NameMiracleTop, NameMiracleTop).getStackForm(1);
        GTCMItemList.MiracleTop.set(MiracleTop);

        //
        MagneticDrivePressureFormer = new GT_TileEntity_MagneticDrivePressureFormer(
            19004,
            NameMagneticDrivePressureFormer,
            NameMagneticDrivePressureFormer).getStackForm(1);
        GTCMItemList.MagneticDrivePressureFormer.set(MagneticDrivePressureFormer);

        //
        PhysicalFormSwitcher = new GT_TileEntity_PhysicalFormSwitcher(
            19005,
            NamePhysicalFormSwitcher,
            NamePhysicalFormSwitcher).getStackForm(1);
        GTCMItemList.PhysicalFormSwitcher.set(PhysicalFormSwitcher);
    }
}
