package com.GTNH_Community.gtnhcommunitymod.common.item.items;

import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders.ItemAdder01;

public class ItemList01 implements Runnable {

    // region ItemStack List ob MetaItem01

    // spotless:off
    public static final ItemStack TestItem0 = ItemAdder01.initItem01("Test Item 0");
    public static final ItemStack TestItem1 = ItemAdder01.initItem01("Test Item 1");


    // spotless:on

    // endregion

    @Override
    public void run() {
        ItemAdder01.init();
    }

}
