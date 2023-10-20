package com.GTNH_Community.gtnhcommunitymod.loader;

import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.machine.GT_TileEntity_IntensifyChemicalDistorter;

public class MachineLoader {

    public static ItemStack IntensifyChemicalDistorter; // INTENSIFY_CHEMICAL_DISTORTER

    public static void loadMachineBlock() {

        IntensifyChemicalDistorter = new GT_TileEntity_IntensifyChemicalDistorter(
            19001,
            "Intensify Chemical Distorter",
            "Intensify Chemical Distorter").getStackForm(1);

    }
}
