package com.GTNH_Community.gtnhcommunitymod.loader;

import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.NameIntensifyChemicalDistorter;

import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_IntensifyChemicalDistorter;

public class MachineLoader {

    public static ItemStack IntensifyChemicalDistorter; // INTENSIFY_CHEMICAL_DISTORTER

    public static void loadMachines() {

        IntensifyChemicalDistorter = new GT_TileEntity_IntensifyChemicalDistorter(
            19001,
            NameIntensifyChemicalDistorter,
            NameIntensifyChemicalDistorter).getStackForm(1);

    }
}
