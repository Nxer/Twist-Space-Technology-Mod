package com.Nxer.TwistSpaceTechnology.common.block;

import net.minecraft.block.Block;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockBase01;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.BlockNuclearReactor;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.MetaBlockCasing01;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationAntiGravityCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationStructureCasing;

public class BasicBlocks {

    public static final Block MetaBlock01 = new BlockBase01("MetaBlock01", "MetaBlock01");
    public static final Block MetaBlockCasing01 = new MetaBlockCasing01();
    public static final Block PhotonControllerUpgrade = new PhotonControllerUpgradeCasing(
        "PhotonControllerUpgrades",
        "Photon Controller Upgrade");

    public static final Block spaceStationStructureBlock = new SpaceStationStructureCasing(
        "SpaceStationStructureBlock",
        "Space Station Structure Block");

    public static final Block SpaceStationAntiGravityBlock = new SpaceStationAntiGravityCasing(
        "SpaceStationAntiGravityBlock",
        "Space Station Anti Gravity Block");
    public static Block BlockStar;

    public static Block NuclearReactorBlock = new BlockNuclearReactor("nuclear", "Mega Nuclear Reactor");
}
