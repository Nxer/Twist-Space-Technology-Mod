package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public interface IMachineTESR {

    // 如果您不熟悉OpenGL操作，请不要实现此接口！！！
    void render(TileEntitySpecialRenderer TESR, TileEntity TileEntity, double x, double y, double z,
        float timeSinceLastTick);
}
