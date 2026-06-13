package com.Nxer.TwistSpaceTechnology.common.entity;

import gregtech.common.tileentities.render.RenderingTileEntityLaser;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityLaserBeacon extends RenderingTileEntityLaser {

    private double range;

    public TileEntityLaserBeacon() {
        super();
    }

    public double getRange() {
        return this.range;
    }

    public void setRange(double r) {
        this.range = r;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setDouble("range", range);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        range = compound.getDouble("range");
    }
}
