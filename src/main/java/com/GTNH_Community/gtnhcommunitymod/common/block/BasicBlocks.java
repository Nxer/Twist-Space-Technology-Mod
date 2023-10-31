package com.GTNH_Community.gtnhcommunitymod.common.block;

import net.minecraft.block.Block;

import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.BlockBase01;
import com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasing;

public class BasicBlocks {

    public static final Block MetaBlock01 = new BlockBase01("MetaBlock01", "MetaBlock01");
    public static final Block PhotonControllerUpgrade = new PhotonControllerUpgradeCasing(
        "PhotonControllerUpgrades",
        "Photon Controller Upgrade");
}
