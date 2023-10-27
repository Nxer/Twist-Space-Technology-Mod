package com.GTNH_Community.gtnhcommunitymod.common.item.items;

import com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders.ItemAdder01;
import net.minecraft.item.ItemStack;

import static com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders.ItemAdder01.initItem01;

public class ItemList01 implements Runnable {

    public static final int IDOffset = 0;

    // region ItemStack List ob MetaItem01

    // spotless:off
    public static final ItemStack TestItem0 = initItem01("Test Item 0",IDOffset);



    // spotless:on

    // endregion

    @Override
    public void run() {
        ItemAdder01.init();
    }

}
