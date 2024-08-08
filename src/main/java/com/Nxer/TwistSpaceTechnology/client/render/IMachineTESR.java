package com.Nxer.TwistSpaceTechnology.client.render;

import static net.minecraft.tileentity.TileEntity.INFINITE_EXTENT_AABB;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import gregtech.api.metatileentity.BaseMetaTileEntity;

public interface IMachineTESR {

    // 如果您不熟悉OpenGL操作，请不要实现此接口！！！
    void render(TileEntitySpecialRenderer TESR, TileEntity TileEntity, double x, double y, double z,
        float timeSinceLastTick);

    /**
     * Return an {@link AxisAlignedBB} that controls the visible scope of a {@link TileEntitySpecialRenderer} associated
     * with this {@link TileEntity}
     * Defaults to the collision bounding box {@link Block#getCollisionBoundingBoxFromPool(World, int, int, int)}
     * associated with the block
     * at this location.
     *
     * @return an appropriately size {@link AxisAlignedBB} for the {@link TileEntity}
     */

    default AxisAlignedBB getRenderBoundingBox(BaseMetaTileEntity baseMetaTile) {
        return INFINITE_EXTENT_AABB;
    }

    // 最大渲染距离
    default double getMaxRenderDistanceSquared() {
        return 4096.0D;
    }
}
