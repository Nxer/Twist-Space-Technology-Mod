package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class Utils {

    public static boolean metaItemEqual(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage();
    }

    public static boolean fluidEqual(FluidStack a, FluidStack b) {
        return a.getFluid() == b.getFluid();
    }

}
