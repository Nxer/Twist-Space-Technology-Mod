package com.GTNH_Community.gtnhcommunitymod.loader;

import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameIntensifyChemicalDistorter;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NamePreciseHighEnergyPhotonicQuantumMaster;

import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_IntensifyChemicalDistorter;
import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster;

public class MachineLoader {

    public static ItemStack IntensifyChemicalDistorter; // INTENSIFY_CHEMICAL_DISTORTER
    public static ItemStack PreciseHighEnergyPhotonicQuantumMaster; // INTENSIFY_CHEMICAL_DISTORTER

    public static void loadMachines() {

        IntensifyChemicalDistorter = new GT_TileEntity_IntensifyChemicalDistorter(
            19001,
            NameIntensifyChemicalDistorter,
            NameIntensifyChemicalDistorter).getStackForm(1);

        PreciseHighEnergyPhotonicQuantumMaster = new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(
            19002,
            NamePreciseHighEnergyPhotonicQuantumMaster,
            NamePreciseHighEnergyPhotonicQuantumMaster).getStackForm(1);

    }
}
