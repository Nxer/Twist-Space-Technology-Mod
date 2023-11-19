package com.Nxer.TwistSpaceTechnology.client;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GTCMCreativeTabs {

    /**
     * Creative Tab for MetaItem01
     */
    public static final CreativeTabs tabMetaItem01 = new CreativeTabs(
        texter("GTCM Meta Items 1", "itemGroup.GTCM Meta Items 1")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

    /**
     * Creative Tab for MetaBlock01
     */
    public static final CreativeTabs tabMetaBlock01 = new CreativeTabs(
        texter("GTCM Meta Blocks 1", "itemGroup.GTCM Meta Blocks 1")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

    /**
     * Creative Tab for MetaBlock01
     */
    public static final CreativeTabs tabGTCMGeneralTab = new CreativeTabs(texter("GTCM", "itemGroup.GTCM")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };
    public static final CreativeTabs tabMultiStructures = new CreativeTabs(
        texter("MultiStructures", "itemGroup.MultiStructures")) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return BasicItems.MetaItem01;
        }
    };

}
