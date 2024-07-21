package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import com.Nxer.TwistSpaceTechnology.common.api.Interfaces.ICanCreatureSpawn;
import net.minecraft.block.Block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

public class MetaItemBlock extends MetaItemBlockBase {

    protected final boolean canCreatureSpawn;

    public MetaItemBlock(Block block) {
        super(block);
        if (block instanceof ICanCreatureSpawn b) {
            canCreatureSpawn = b.canCreatureSpawn();
        } else {
            canCreatureSpawn = true;
        }
    }

    @Override
    public boolean canCreatureSpawn() {
        return canCreatureSpawn;
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
