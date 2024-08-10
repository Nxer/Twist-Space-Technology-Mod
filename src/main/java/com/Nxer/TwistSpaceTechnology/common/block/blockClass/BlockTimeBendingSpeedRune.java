package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import WayofTime.alchemicalWizardry.common.block.SpeedRune;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockTimeBendingSpeedRune extends SpeedRune {

    public BlockTimeBendingSpeedRune() {
        super();
        // #tr tile.timeBendingSpeedRune.name
        // # Time-bending Speed Rune
        // #zh_CN 时间扭曲速度符文
        setBlockName("timeBendingSpeedRune");
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("gtnhcommunitymod:TimeBendingSpeedRune");
    }

    @Override
    public int getRuneEffect(int metaData) {
        return super.getRuneEffect(metaData);
    }
}
