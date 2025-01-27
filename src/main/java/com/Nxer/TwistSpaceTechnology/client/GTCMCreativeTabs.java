package com.Nxer.TwistSpaceTechnology.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GTCMCreativeTabs {

    // #tr itemGroup.TSTItems1
    // # TST Items 1
    // #zh_CN TST 物品 1
    public static final CreativeTabs tabMetaItem01 = new CreativeTabs(TextEnums.tr("itemGroup.TSTItems1")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

    // #tr itemGroup.TSTGears
    // # TST Gears
    // #zh_CN TST Gears
    public static final CreativeTabs tabGears = new CreativeTabs(TextEnums.tr("itemGroup.TSTGears")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

    // #tr itemGroup.TSTBlocks1
    // # TST Blocks 1
    // #zh_CN TST 方块 1
    public static final CreativeTabs TAB_META_BLOCKS = new CreativeTabs(TextEnums.tr("itemGroup.TSTBlocks1")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

    // #tr itemGroup.TST
    // # Twist Space Technology
    // #zh_CN 扭曲空间科技
    public static final CreativeTabs tabGTCMGeneralTab = new CreativeTabs(TextEnums.tr("itemGroup.TST")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

    // #tr itemGroup.TSTMultiStructures
    // # TST Multi Structures
    // #zh_CN TST 多方块结构
    public static final CreativeTabs tabMultiStructures = new CreativeTabs(
        TextEnums.tr("itemGroup.TSTMultiStructures")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

}
