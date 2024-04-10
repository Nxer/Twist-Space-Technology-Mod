package com.Nxer.TwistSpaceTechnology.common.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockPowerChair;

public class TilePowerChair extends TileEntity {

    public EnumFacing Facing;
    public int facing;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    public int getMeta() {
        final Block b = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        final int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        if (b instanceof BlockPowerChair) {
            return meta;
        }
        return 0;

    }

}
