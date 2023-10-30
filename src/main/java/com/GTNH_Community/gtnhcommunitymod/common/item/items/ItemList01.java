package com.GTNH_Community.gtnhcommunitymod.common.item.items;

import static com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders.ItemAdder01.initItem01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders.ItemAdder01;

public class ItemList01 implements Runnable {

    public static final int IDOffset = 0;

    // region ItemStack List ob MetaItem01

    // spotless:off
    public static final ItemStack TestItem0 = initItem01("Test Item 0",IDOffset, new String[]{texter("A test item, no use.","tooltips.TestItem0.line1")});
    public static final ItemStack SpaceWarper = initItem01("Space Warper",IDOffset+1, new String[]{texter(EnumChatFormatting.DARK_BLUE + "Power of gravitation !","tooltips.SpaceWarper.line1")});



    // spotless:on

    // endregion

    @Override
    public void run() {
        ItemAdder01.init();
    }

}
