package com.GTNH_Community.gtnhcommunitymod.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import cpw.mods.fml.common.registry.GameRegistry;

// Demo
public final class VanillaRegistries {

    public static void vanillaRegItem(Item item, String unlocalizedName) {
        GameRegistry.registerItem(item, unlocalizedName);
    }

    public static void vanillaRegBlock(Block block, Class<? extends ItemBlock> itemBlockClass, String unlocalizedName) {
        GameRegistry.registerBlock(block, itemBlockClass, unlocalizedName);
    }

}
