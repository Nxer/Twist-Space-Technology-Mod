package com.GTNH_Community.gtnhcommunitymod.common.block;

import static com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta;
import static com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.ItemBlockBase01.metaBlock01;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.BlockBase01;
import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasing;
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
    public static final ItemStack TestMetaBlock01_0 = metaBlock01("TestMetaBlock01_0", 0);
    public static final ItemStack PhotonControllerUpgradeLV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier LV Tier",
        0);
    public static final ItemStack PhotonControllerUpgradeMV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier MV Tier",
        1);
    public static final ItemStack PhotonControllerUpgradeHV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier HV Tier",
        2);
    public static final ItemStack PhotonControllerUpgradeEV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier EV Tier",
        3);
    public static final ItemStack PhotonControllerUpgradeIV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier IV Tier",
        4);
    public static final ItemStack PhotonControllerUpgradeLuV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier LuV Tier",
        5);
    public static final ItemStack PhotonControllerUpgradeZPM = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier ZPM Tier",
        6);
    public static final ItemStack PhotonControllerUpgradeUV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UV Tier",
        7);
    public static final ItemStack PhotonControllerUpgradeUHV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UHV Tier",
        8);
    public static final ItemStack PhotonControllerUpgradeUEV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UEV Tier",
        9);
    public static final ItemStack PhotonControllerUpgradeUIV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UIV Tier",
        10);
    public static final ItemStack PhotonControllerUpgradeUMV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UMV Tier",
        11);
    public static final ItemStack PhotonControllerUpgradeUXV = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier UXV Tier",
        12);
    public static final ItemStack PhotonControllerUpgradeMAX = photonControllerUpgradeCasingMeta(
        "Photonic Intensifier MAX Tier",
        13);

    // endregion
    // -----------------------
    @Override
    public void run() {
        GameRegistry.registerBlock(MetaBlock01, ItemBlockBase01.class, MetaBlock01.getUnlocalizedName());
        GameRegistry.registerBlock(PhotonControllerUpgrade, ItemBlockBase01.class, "PhotonControllerUpgrades");
    }
}
