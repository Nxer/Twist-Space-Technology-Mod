package com.Nxer.TwistSpaceTechnology.common.tile;

import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_MODEL;
import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_TEXTURE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

public class TileEyeOfWoodRender extends TileEntity {

    // Current size
    public double size = 0;
    // Target size
    public double targetSize = 4.5;
    // Initial size
    public double initialSize = 0;
    // Ticks that have been updated
    public int ticks = 0;
    // Duration of the size increase (in ticks)
    public int duration = 100;
    // Current model index being scaled
    public int currentModelIndex = 0;
    // List of models
    public final List<IModelCustom> models = new ArrayList<>();
    // List of textures
    public final List<ResourceLocation> textures = new ArrayList<>();

    public TileEyeOfWoodRender() {
        models.add(WOOD_MODEL);
        textures.add(WOOD_TEXTURE);
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
        nbt.setInteger("currentModelIndex", currentModelIndex);
        nbt.setInteger("modelsCount", models.size());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
        currentModelIndex = nbt.getInteger("currentModelIndex");
        int modelsCount = nbt.getInteger("modelsCount");
        models.clear();
        textures.clear();
        for (int i = 0; i < modelsCount; i++) {
            models.add(WOOD_MODEL);
            textures.add(WOOD_TEXTURE);
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (currentModelIndex < models.size()) {
            if (ticks < duration) {
                double t = (double) ticks / duration;
                double easedT = cubicEaseOut(t);
                size = initialSize + (targetSize - initialSize) * easedT;
                ticks++;
            } else {
                size = targetSize;
                currentModelIndex++;
                ticks = 0;
            }
        }
    }

    public List<IModelCustom> getModels() {
        return models;
    }

    public ResourceLocation getTexture(int index) {
        return textures.get(index);
    }

    public double cubicEaseOut(double t) {
        return 1 - Math.pow(1 - t, 3);
    }
}
