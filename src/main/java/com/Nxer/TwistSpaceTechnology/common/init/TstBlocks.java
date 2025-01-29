package com.Nxer.TwistSpaceTechnology.common.init;

import net.minecraft.block.Block;

import com.Nxer.TwistSpaceTechnology.common.block.BlockArcaneHole;
import com.Nxer.TwistSpaceTechnology.common.block.BlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.block.BlockStar;
import com.Nxer.TwistSpaceTechnology.common.block.bloodMagic.BlockTimeBendingSpeedRune;
import com.Nxer.TwistSpaceTechnology.common.block.meta.BlockNuclearReactor;
import com.Nxer.TwistSpaceTechnology.common.block.meta.TstMetaBlock;
import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.PhotonControllerUpgradeCasing;
import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.TstMetaBlockCasing;
import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.TstMetaBlockStructureCasing;
import com.Nxer.TwistSpaceTechnology.common.block.meta.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.common.block.meta.spaceStation.SpaceStationAntiGravityCasing;
import com.Nxer.TwistSpaceTechnology.common.block.meta.spaceStation.SpaceStationStructureCasing;

public class TstBlocks {

    /**
     * UnlocalizedName: {@code MetaBlock01}
     */
    public static final TstMetaBlock MetaBlock01 = new TstMetaBlock("MetaBlock01");

    /**
     * UnlocalizedName: {@code MetaBlockCasing01}
     */
    public static final TstMetaBlockCasing MetaBlockCasing01 = new TstMetaBlockCasing("MetaBlockCasing01", (byte) 0);

    /**
     * UnlocalizedName: {@code MetaBlockCasing02}
     */
    public static final TstMetaBlockCasing MetaBlockCasing02 = new TstMetaBlockCasing("MetaBlockCasing02", (byte) 16);

    public static final PhotonControllerUpgradeCasing PhotonControllerUpgrade = new PhotonControllerUpgradeCasing();

    public static final TstMetaBlockStructureCasing SpaceTimeOscillator = new TstMetaBlockStructureCasing(
        "SpaceTimeOscillator");
    public static final TstMetaBlockStructureCasing SpaceTimeConstraintor = new TstMetaBlockStructureCasing(
        "SpaceTimeConstraintor");
    public static final TstMetaBlockStructureCasing SpaceTimeMerger = new TstMetaBlockStructureCasing(
        "SpaceTimeMerger");

    public static final SpaceStationStructureCasing SpaceStationStructureBlock = new SpaceStationStructureCasing();

    public static final SpaceStationAntiGravityCasing SpaceStationAntiGravityBlock = new SpaceStationAntiGravityCasing();

    public static final Block BlockStar = new BlockStar();
    public static final Block NuclearReactorBlock = new BlockNuclearReactor();

    public static final Block BlockPowerChair = new BlockPowerChair();
    public static final Block BlockArcaneHole = new BlockArcaneHole();

    public static final BlockTimeBendingSpeedRune TimeBendingSpeedRune = new BlockTimeBendingSpeedRune();

    public static final BlockMultiUseCore MultiUseCore = new BlockMultiUseCore();
}
