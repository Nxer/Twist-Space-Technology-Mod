package com.GTNH_Community.gtnhcommunitymod.util;

import net.minecraft.block.Block;

public final class Utils {

    public static String getKeyWithBlockMeta(Block block, int meta) {
        return block.getUnlocalizedName() + "." + meta;
    }
}
