package com.Nxer.TwistSpaceTechnology.common.block;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlock01;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.PhotonControllerUpgrade;
import static com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta;
import static com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01.initMetaBlock01;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasingItemBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasing;
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

        GTCMItemList.TestMetaBlock01_0.set(ItemBlockBase01.initMetaBlock01("TestMetaBlock01_0", 0));
        GTCMItemList.PhotonControllerUpgradeLV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier LV Tier", 0));
        GTCMItemList.PhotonControllerUpgradeMV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier MV Tier", 1));
        GTCMItemList.PhotonControllerUpgradeHV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier HV Tier", 2));
        GTCMItemList.PhotonControllerUpgradeEV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier EV Tier", 3));
        GTCMItemList.PhotonControllerUpgradeIV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier IV Tier", 4));
        GTCMItemList.PhotonControllerUpgradeLuV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier LuV Tier", 5));
        GTCMItemList.PhotonControllerUpgradeZPM
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier ZPM Tier", 6));
        GTCMItemList.PhotonControllerUpgradeUV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UV Tier", 7));
        GTCMItemList.PhotonControllerUpgradeUHV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UHV Tier", 8));
        GTCMItemList.PhotonControllerUpgradeUEV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UEV Tier", 9));
        GTCMItemList.PhotonControllerUpgradeUIV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UIV Tier", 10));
        GTCMItemList.PhotonControllerUpgradeUMV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UMV Tier", 11));
        GTCMItemList.PhotonControllerUpgradeUXV
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier UXV Tier", 12));
        GTCMItemList.PhotonControllerUpgradeMAX
            .set(PhotonControllerUpgradeCasing.photonControllerUpgradeCasingMeta("Photonic Intensifier MAX Tier", 13));

    }

    public static void registry() {
        registryBlocks();
        registryBlockContainers();
    }
}
