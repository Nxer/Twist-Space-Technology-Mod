package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.item.ItemStack;

public final class Utils {

    public static boolean metaItemEqual(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage();
    }
}
