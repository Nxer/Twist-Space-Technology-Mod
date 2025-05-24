package com.Nxer.TwistSpaceTechnology.common.tile;

import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_SWEAT_MODEL;
import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_SWEAT_TEXTURE;
import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_THINKING_MODEL;
import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_THINKING_TEXTURE;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import com.Nxer.TwistSpaceTechnology.config.Config;

public class TileEyeOfWoodRender extends TileEntity {

    public double size = 0;
    public double targetSize = 4.5;
    public double initialSize = 0;
    public int ticks = 0;
    public int duration = 100;

    public IModelCustom model;
    public ResourceLocation texture;

    public TileEyeOfWoodRender() {
        switch (Config.RenderModelDefault_EyeOfWood) {
            case 1 -> {
                this.model = WOOD_THINKING_MODEL;
                this.texture = WOOD_THINKING_TEXTURE;
            }
            default -> {
                this.model = WOOD_SWEAT_MODEL;
                this.texture = WOOD_SWEAT_TEXTURE;
            }
        }
    }

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

    public IModelCustom getModel() {
        return model;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public double cubicEaseOut(double t) {
        return 1 - Math.pow(1 - t, 3);
    }
}
