package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import net.minecraft.block.Block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaItemBlock extends MetaItemBlockBase {

    public MetaItemBlock(Block block) {
        super(block);
    }

    @Override
    public boolean canCreatureSpawn() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String[] getTooltips(int meta) {
        if (getThisBlock() instanceof IMetaBlock thisBlock) {
            return thisBlock.getTooltips(meta);
        }
        return new String[0];
    }
}
