package com.GTNH_Community.gtnhcommunitymod.common.block;

import static com.GTNH_Community.gtnhcommunitymod.common.block.blockList01.PhotonControllerUpgrade;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.GTNHCommunityMod;
import com.GTNH_Community.gtnhcommunitymod.util.Utils;

public final class BlockEnum {

    // region Statics

    public static Map<String, String[]> TooltipsMap = new HashMap<>();

    // endregion

    // region Enum

    // spotless:off

    public static BlockEnum PhotonControllerUpgradeLV = new BlockEnum(PhotonControllerUpgrade,0);
    public static BlockEnum PhotonControllerUpgradeMV = new BlockEnum(PhotonControllerUpgrade,1);
    public static BlockEnum PhotonControllerUpgradeHV = new BlockEnum(PhotonControllerUpgrade,2);
    public static BlockEnum PhotonControllerUpgradeEV = new BlockEnum(PhotonControllerUpgrade,3);
    public static BlockEnum PhotonControllerUpgradeIV = new BlockEnum(PhotonControllerUpgrade,4);
    public static BlockEnum PhotonControllerUpgradeLuV = new BlockEnum(PhotonControllerUpgrade,5);
    public static BlockEnum PhotonControllerUpgradeZPM = new BlockEnum(PhotonControllerUpgrade,6);
    public static BlockEnum PhotonControllerUpgradeUV = new BlockEnum(PhotonControllerUpgrade,7);
    public static BlockEnum PhotonControllerUpgradeUHV = new BlockEnum(PhotonControllerUpgrade,8);
    public static BlockEnum PhotonControllerUpgradeUEV = new BlockEnum(PhotonControllerUpgrade,9);
    public static BlockEnum PhotonControllerUpgradeUIV = new BlockEnum(PhotonControllerUpgrade,10);
    public static BlockEnum PhotonControllerUpgradeUMV = new BlockEnum(PhotonControllerUpgrade,11);
    public static BlockEnum PhotonControllerUpgradeUXV = new BlockEnum(PhotonControllerUpgrade,12);
    public static BlockEnum PhotonControllerUpgradeMAX = new BlockEnum(PhotonControllerUpgrade,13);


    // spotless:on

    // endregion

    // region Constructors

    public BlockEnum() {}

    // public BlockEnum(Block basedBlock, int meta, ItemStack basedItemStack){
    // this.basedBlock=basedBlock;
    // this.meta=meta;
    // this.basedItemStack=basedItemStack;
    // }

    public BlockEnum(Block basedBlock, int meta) {
        this.basedBlock = basedBlock;
        this.meta = meta;
    }

    public BlockEnum(Block basedBlock, int meta, String[] tooltips) {
        this.basedBlock = basedBlock;
        this.meta = meta;
        TooltipsMap.put(Utils.getKeyWithBlockMeta(basedBlock, meta), tooltips);
    }

    // public BlockEnum(Item basedItem, int meta, ItemStack basedItemStack){
    // this.basedItem=basedItem;
    // this.meta=meta;
    // this.basedItemStack=basedItemStack;
    // }

    // endregion

    // region Member Variables

    private Block basedBlock;

    // private Item basedItem;

    private int meta;

    private ItemStack basedItemStack;

    // endregion

    // region Getters and Setters

    public Block getBasedBlock() {
        return basedBlock;
    }

    // public void setBasedBlock(Block basedBlock) {
    // this.basedBlock = basedBlock;
    // }

    public int getMeta() {
        return meta;
    }

    // public void setMeta(int meta) {
    // this.meta = meta;
    // }

    public ItemStack getBasedItemStack() {
        return basedItemStack;
    }

    public void setBasedItemStack(ItemStack basedItemStack) {
        this.basedItemStack = basedItemStack;
    }

    // endregion

    // region method

    public ItemStack get(int amount) {
        if (null == this.basedBlock) {
            GTNHCommunityMod.LOG
                .info("BlockEnum catch a issue: " + this + " at get this.basedBlock NULL, return the BlockStone.");
            return new ItemStack(new BlockStone(), 1, 0);
        }
        return new ItemStack(this.basedBlock, amount, this.meta);
    }

    // endregion

}
