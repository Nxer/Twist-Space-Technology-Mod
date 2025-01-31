package com.Nxer.TwistSpaceTechnology.common.block.meta.casing;

import static com.Nxer.TwistSpaceTechnology.config.Config.PhotonControllerUpgradeCasingSpeedIncrement;

import com.Nxer.TwistSpaceTechnology.common.block.meta.TstMetaBlockMachine;

public class PhotonControllerUpgradeCasing extends TstMetaBlockMachine {

    public PhotonControllerUpgradeCasing() {
        // #tr tile.PhotonControllerUpgrades.name
        // # Photon Controller Upgrade
        // #zh_CN 光子掌控者升级
        super("PhotonControllerUpgrades");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
    }

    /**
     * The Speed Increment of the Upgrade Casing.
     * Total Speed Increment is the sum of each Upgrade Casing.
     * Total value 10,000 means time cost *0.5 .
     */
    public static int[] speedIncrement = PhotonControllerUpgradeCasingSpeedIncrement;

    public int getSpeedIncrement(int meta) {
        return speedIncrement[meta];
    }

    public void setSpeedIncrement(int speedIncrement, int meta) {
        PhotonControllerUpgradeCasing.speedIncrement[meta] = speedIncrement;
    }

}
