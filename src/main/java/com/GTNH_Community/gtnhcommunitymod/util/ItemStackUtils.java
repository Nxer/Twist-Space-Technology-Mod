package com.GTNH_Community.gtnhcommunitymod.util;

import net.minecraft.item.ItemStack;

public class ItemStackUtils {

    public static ItemStack get(ItemStack metaItemStack, int amount) {
        ItemStack itemStack = metaItemStack.copy();
        itemStack.stackSize = amount;
        return itemStack;
    }

    // public static ItemStack setAmount(ItemStack aItemStack, int amount){
    // aItemStack.stackSize = amount;
    // return aItemStack;
    // }
}
