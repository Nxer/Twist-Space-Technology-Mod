package com.Nxer.TwistSpaceTechnology.common.block;

import net.minecraft.block.Block;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockArcaneHole;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockBase01;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockTimeBendingSpeedRune;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.MetaBlockCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.PhotonControllerUpgradeCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.nuclear.BlockNuclearReactor;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationAntiGravityCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.spaceStation.SpaceStationStructureCasing;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.MetaStructureCasing;

public class BasicBlocks {

    public static final Block MetaBlock01 = new BlockBase01();

    /**
     * UnlocalizedName: {@code MetaBlockCasing01}
     */
    public static final MetaBlockCasing MetaBlockCasing01 = new MetaBlockCasing("MetaBlockCasing01", (byte) 0);

    /**
     * UnlocalizedName: {@code MetaBlockCasing02}
     */
    public static final MetaBlockCasing MetaBlockCasing02 = new MetaBlockCasing("MetaBlockCasing02", (byte) 16);

    public static final PhotonControllerUpgradeCasing PhotonControllerUpgrade = new PhotonControllerUpgradeCasing();

    public static final MetaStructureCasing SpaceTimeOscillator = new MetaStructureCasing("SpaceTimeOscillator");
    public static final MetaStructureCasing SpaceTimeConstraintor = new MetaStructureCasing("SpaceTimeConstraintor");
    public static final MetaStructureCasing SpaceTimeMerger = new MetaStructureCasing("SpaceTimeMerger");

    public static final SpaceStationStructureCasing SpaceStationStructureBlock = new SpaceStationStructureCasing();

    public static final SpaceStationAntiGravityCasing SpaceStationAntiGravityBlock = new SpaceStationAntiGravityCasing();
    public static Block BlockStar;
    public static Block NuclearReactorBlock = new BlockNuclearReactor();

    public static Block BlockPowerChair = new BlockPowerChair();
    public static Block BlockArcaneHole = new BlockArcaneHole();

    public static BlockTimeBendingSpeedRune timeBendingSpeedRune = new BlockTimeBendingSpeedRune();

    public static BlockMultiUseCore multiUseCore = new BlockMultiUseCore();
}
