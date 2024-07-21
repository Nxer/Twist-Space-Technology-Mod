package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.AppliedEnergistics.block.base;

import appeng.block.AEBaseTileBlock;
import appeng.core.features.ActivityState;
import appeng.core.features.BlockStackSrc;
import appeng.tile.AEBaseTile;
import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.api.Interfaces.ICanCreatureSpawn;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public abstract class TST_AEBaseTileBlock extends AEBaseTileBlock implements ICanCreatureSpawn {
    public static final String TST_AE_BASE_TILE_BLOCK = "TST_AEBaseTileBlock";
    public TST_AEBaseTileBlock(String unlocalizedName) {
        super(Material.iron);
        setBlockName(unlocalizedName);
        setBlockTextureName(TwistSpaceTechnology.RESOURCE_ROOT_ID+":"+TST_AE_BASE_TILE_BLOCK+"/"+unlocalizedName);
    }

    @Override
    public void setTileEntity(final Class<? extends TileEntity> clazz) {
        AEBaseTile.registerTileItem(clazz, new BlockStackSrc(this, 0, ActivityState.Enabled));
        super.setTileEntity(clazz);
    }

    @Override
    public boolean canCreatureSpawn() {
        return false;
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
}
