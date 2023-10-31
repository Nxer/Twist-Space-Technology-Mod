package com.GTNH_Community.gtnhcommunitymod.common.block;

import static com.GTNH_Community.gtnhcommunitymod.common.block.BasicBlocks.MetaBlock01;
import static com.GTNH_Community.gtnhcommunitymod.common.block.BasicBlocks.PhotonControllerUpgrade;
import static com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta;
import static com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.ItemBlockBase01.initMetaBlock01;

import com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList;
import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasingItemBlock;
import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.ItemBlockBase01;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRegister {

    public static void registryBlocks() {

        GameRegistry.registerBlock(MetaBlock01, ItemBlockBase01.class, MetaBlock01.getUnlocalizedName());
        GameRegistry.registerBlock(
            PhotonControllerUpgrade,
            PhotonControllerUpgradeCasingItemBlock.class,
            PhotonControllerUpgrade.getUnlocalizedName());

    }

    public static void registryBlockContainers() {

        GTCMItemList.TestMetaBlock01_0.set(initMetaBlock01("TestMetaBlock01_0", 0));
        GTCMItemList.PhotonControllerUpgradeLV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier LV Tier", 0));
        GTCMItemList.PhotonControllerUpgradeMV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier MV Tier", 1));
        GTCMItemList.PhotonControllerUpgradeHV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier HV Tier", 2));
        GTCMItemList.PhotonControllerUpgradeEV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier EV Tier", 3));
        GTCMItemList.PhotonControllerUpgradeIV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier IV Tier", 4));
        GTCMItemList.PhotonControllerUpgradeLuV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier LuV Tier", 5));
        GTCMItemList.PhotonControllerUpgradeZPM
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier ZPM Tier", 6));
        GTCMItemList.PhotonControllerUpgradeUV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier UV Tier", 7));
        GTCMItemList.PhotonControllerUpgradeUHV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier UHV Tier", 8));
        GTCMItemList.PhotonControllerUpgradeUEV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier UEV Tier", 9));
        GTCMItemList.PhotonControllerUpgradeUIV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier UIV Tier", 10));
        GTCMItemList.PhotonControllerUpgradeUMV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier UMV Tier", 11));
        GTCMItemList.PhotonControllerUpgradeUXV
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier UXV Tier", 12));
        GTCMItemList.PhotonControllerUpgradeMAX
            .set(photonControllerUpgradeCasingMeta("Photonic Intensifier MAX Tier", 13));

    }

    public static void registry() {
        registryBlocks();
        registryBlockContainers();
    }
}
