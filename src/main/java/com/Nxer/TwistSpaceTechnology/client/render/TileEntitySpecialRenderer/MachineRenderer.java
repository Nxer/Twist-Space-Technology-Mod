package com.Nxer.TwistSpaceTechnology.client.render.TileEntitySpecialRenderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import com.Nxer.TwistSpaceTechnology.client.render.IMachineTESR;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.api.metatileentity.BaseMetaTileEntity;

public class MachineRenderer extends TileEntitySpecialRenderer {

    public MachineRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(BaseMetaTileEntity.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity TileEntity, double x, double y, double z, float timeSinceLastTick) {
        if (TileEntity instanceof BaseMetaTileEntity T) {
            if (T.getMetaTileEntity() instanceof IMachineTESR TESR) {
                TESR.render(this, TileEntity, x, y, z, timeSinceLastTick);
            }
        }
    }
}
