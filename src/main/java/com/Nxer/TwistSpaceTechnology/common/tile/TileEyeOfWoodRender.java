package com.Nxer.TwistSpaceTechnology.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEyeOfWoodRender extends TileEntity {

    public double size = 0;
    public double targetSize = 4.5;
    public double initialSize = 0;
    public int ticks = 0;
    public int duration = 100;

    public TileEyeOfWoodRender() {}

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("size", size);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (ticks < duration) {
            double t = (double) ticks / duration;
            double easedT = cubicEaseOut(t);
            size = initialSize + (targetSize - initialSize) * easedT;
            ticks++;
        } else {
            size = targetSize;
        }
    }

    public double cubicEaseOut(double t) {
        return 1 - Math.pow(1 - t, 3);
    }
}
