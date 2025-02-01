package com.Nxer.TwistSpaceTechnology.common.block.meta;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;

import com.Nxer.TwistSpaceTechnology.common.block.IHasMoreBlockInfo;

/**
 * A {@link AbstractTstMetaBlock} with predefined settings like
 * {@link #canCreatureSpawn(EnumCreatureType, IBlockAccess, int, int, int)},
 * {@link #canBeReplacedByLeaves(IBlockAccess, int, int, int)}, etc.
 */
public class TstMetaBlock extends AbstractTstMetaBlock implements IHasMoreBlockInfo {

    public TstMetaBlock(String unlocalizedName) {
        super(Material.iron, unlocalizedName);
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isNoMobSpawn() {
        return true;
    }

    @Override
    public boolean isNotTileEntity() {
        return true;
    }

}
