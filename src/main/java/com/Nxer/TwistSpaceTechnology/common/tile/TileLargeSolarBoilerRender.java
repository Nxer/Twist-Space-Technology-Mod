package com.Nxer.TwistSpaceTechnology.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;

public class TileLargeSolarBoilerRender extends TileEntity {

    public int xBackOffset = 0;
    public int zBackOffset = 0;
    public int xRightOffset = 0;
    public int zRightOffset = 0;

    public TileLargeSolarBoilerRender() {}

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("xBackOffset", xBackOffset);
        compound.setInteger("zBackOffset", zBackOffset);
        compound.setInteger("xRightOffset", xRightOffset);
        compound.setInteger("zRightOffset", zRightOffset);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        xBackOffset = compound.getInteger("xBackOffset");
        zBackOffset = compound.getInteger("zBackOffset");
        xRightOffset = compound.getInteger("xRightOffset");
        zRightOffset = compound.getInteger("zRightOffset");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
