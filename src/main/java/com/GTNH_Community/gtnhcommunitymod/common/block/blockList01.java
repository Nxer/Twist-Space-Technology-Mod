package com.GTNH_Community.gtnhcommunitymod.common.block;

import static com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta;
import static com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.ItemBlockBase01.metaBlock01;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.BlockBase01;
import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasing;
import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasingItemBlock;
import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.ItemBlockBase01;

import cpw.mods.fml.common.registry.GameRegistry;

public class blockList01 implements Runnable {

    // region Basic Blocks
    public static final Block MetaBlock01 = new BlockBase01("MetaBlock01", "MetaBlock01");
    public static final Block PhotonControllerUpgrade = new PhotonControllerUpgradeCasing(
        "PhotonControllerUpgrade",
        "Photon Controller Upgrade");

    // endregion
    // -----------------------
    // region Meta Blocks
    public static ItemStack TestMetaBlock01_0 = metaBlock01("TestMetaBlock01_0", 0);

    public static void initMetaBlocks() {

    }

    // endregion
    // -----------------------
    // region Photon Controller Upgrades
    public static ItemStack PhotonControllerUpgradeLV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier LV Tier",
        0);
    public static ItemStack PhotonControllerUpgradeMV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier MV Tier",
        1);
    public static ItemStack PhotonControllerUpgradeHV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier HV Tier",
        2);
    public static ItemStack PhotonControllerUpgradeEV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier EV Tier",
        3);
    public static ItemStack PhotonControllerUpgradeIV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier IV Tier",
        4);
    public static ItemStack PhotonControllerUpgradeLuV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier LuV Tier",
        5);
    public static ItemStack PhotonControllerUpgradeZPM = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier ZPM Tier",
        6);
    public static ItemStack PhotonControllerUpgradeUV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UV Tier",
        7);
    public static ItemStack PhotonControllerUpgradeUHV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UHV Tier",
        8);
    public static ItemStack PhotonControllerUpgradeUEV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UEV Tier",
        9);
    public static ItemStack PhotonControllerUpgradeUIV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UIV Tier",
        10);
    public static ItemStack PhotonControllerUpgradeUMV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UMV Tier",
        11);
    public static ItemStack PhotonControllerUpgradeUXV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UXV Tier",
        12);
    public static ItemStack PhotonControllerUpgradeMAX = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier MAX Tier",
        13);

    // endregion
    // -----------------------
    public static ItemStack get(ItemStack metaItem, int amount) {
        return new ItemStack(metaItem.getItem(), amount, metaItem.getItemDamage());
    }

    @Override
    public void run() {
        // register basic blocks
        GameRegistry.registerBlock(MetaBlock01, ItemBlockBase01.class, MetaBlock01.getUnlocalizedName());
        GameRegistry.registerBlock(
            PhotonControllerUpgrade,
            PhotonControllerUpgradeCasingItemBlock.class,
            "PhotonControllerUpgrades");

        // init Meta information
        initMetaBlocks();
    }
}
