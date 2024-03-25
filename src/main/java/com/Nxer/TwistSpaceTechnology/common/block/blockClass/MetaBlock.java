package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;

public class MetaBlock extends MetaBlockBase {

    public MetaBlock(String unlocalizedName) {
        super(unlocalizedName);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return true;
    }

}
