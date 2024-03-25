package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import net.minecraft.block.Block;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.IMetaBlock;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.MetaItemBlockBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaItemBlockCasing extends MetaItemBlockBase {

    public MetaItemBlockCasing(Block block) {
        super(block);
    }

    @Override
    public boolean canCreatureSpawn() {
        return false;
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
