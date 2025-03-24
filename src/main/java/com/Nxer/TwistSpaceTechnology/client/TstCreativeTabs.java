package com.Nxer.TwistSpaceTechnology.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TstCreativeTabs {

    // #tr itemGroup.TST
    // # Twist Space Technology
    // #zh_CN 扭曲空间科技
    public static final CreativeTabs TabGeneral = new CreativeTabs("TST") {

        @Override
        public Item getTabIconItem() {
            return null; // don't care about it, we have override below!
        }

        @Override
        public ItemStack getIconItemStack() {
            return GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1);
        }
    };

    // #tr itemGroup.TSTItems1
    // # TST Items 1
    // #zh_CN TST 物品 1
    public static final CreativeTabs TabMetaItems = new CreativeTabs("TSTItems1") {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return TstItems.ProofOfGods;
        }
    };

    // #tr itemGroup.TSTBlocks1
    // # TST Blocks 1
    // #zh_CN TST 方块 1
    public static final CreativeTabs TabMetaBlocks = new CreativeTabs("TSTBlocks1") {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(TstBlocks.BlockPowerChair);
        }
    };

    private static final List<ItemStack> TstMachines = new ArrayList<>();

    public static void registerMachineToCreativeTab(ItemStack stack) {
        TstMachines.add(stack);
    }

    // #tr itemGroup.TSTMachines
    // # TST Machines
    // #zh_CN TST 机器
    @SuppressWarnings("unused")
    public static final CreativeTabs TabMachines = new CreativeTabs("TSTMachines") {

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(TstBlocks.MultiUseCore);
        }

        @Override
        public void displayAllReleventItems(List<ItemStack> stackList) {
            // no super! because we don't need other things, no!
            stackList.addAll(TstMachines);
        }
    };

    // #tr itemGroup.TSTMultiStructures
    // # TST Multi Structures
    // #zh_CN TST 多方块结构
    public static final CreativeTabs TabMultiStructures = new CreativeTabs("TSTMultiStructures") {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return TstItems.MetaItem01;
        }
    };

    // #tr itemGroup.TSTGears
    // # TST Gears
    // #zh_CN TST Gears
    public static final CreativeTabs TabGears = new CreativeTabs("TSTGears") {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return TstItems.MetaItem01;
        }
    };

}
