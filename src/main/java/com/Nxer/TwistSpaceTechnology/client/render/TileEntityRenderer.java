package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;

import cpw.mods.fml.client.registry.ClientRegistry;

public class TileEntityRenderer extends TileEntitySpecialRenderer {

    private final IModelCustom Renderer;
    private final ResourceLocation textures;

    public TileEntityRenderer(final IModelCustom Renderer, final ResourceLocation textures) {
        this.Renderer = Renderer;
        this.textures = textures;
        ClientRegistry.bindTileEntitySpecialRenderer(TilePowerChair.class, this);
    }

    private void getTileEntityFacing(TilePowerChair tile, double x, double y, double z) {
        switch (tile.getMeta()) {
            case 1 -> {
                GL11.glTranslated(x + 0.8, y, z + 0.5);
                GL11.glRotated(-90, 0, 1, 0);
            }
            case 2 -> {
                GL11.glTranslated(x + 0.5, y, z + 0.2);
                GL11.glRotated(0, 0, 1, 0);
            }
            case 3 -> {
                GL11.glTranslated(x + 0.5, y, z + 0.8);
                GL11.glRotated(180, 0, 1, 0);
            }
            case 0 -> {
                GL11.glTranslated(x + 0.2, y, z + 0.5);
                GL11.glRotated(90, 0, 1, 0);
            }
        }

    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TilePowerChair)) return;
        GL11.glPushMatrix();
        getTileEntityFacing((TilePowerChair) tile, x, y, z);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glScaled(0.07, 0.07, 0.07);
        this.bindTexture(textures);
        Renderer.renderAll();
        GL11.glPopMatrix();
    }
}
