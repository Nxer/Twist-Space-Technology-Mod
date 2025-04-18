package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.entity.TileEntityLaserBeacon;

import cpw.mods.fml.client.registry.ClientRegistry;

public class MeteorMinerRenderer extends TileEntitySpecialRenderer {

    public MeteorMinerRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserBeacon.class, this);
    }

    private void renderFakeLine(TileEntityLaserBeacon laser, double x, double y1, double z, double y2) {
        Tessellator tessellator = Tessellator.instance;
        float lineOpacity = 0.7F;
        tessellator.setColorRGBA_F(laser.getRed(), laser.getGreen(), laser.getBlue(), lineOpacity);
        double lineRadius = 0.25;
        double x1 = x - lineRadius;
        double x2 = x + lineRadius;
        double z1 = z - lineRadius;
        double z2 = z + lineRadius;

        // Bottom face
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y1, z1);
        tessellator.addVertex(x2, y1, z1);
        tessellator.addVertex(x2, y1, z2);
        tessellator.addVertex(x1, y1, z2);
        tessellator.draw();

        // Top face
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y2, z1);
        tessellator.addVertex(x2, y2, z1);
        tessellator.addVertex(x2, y2, z2);
        tessellator.addVertex(x1, y2, z2);
        tessellator.draw();

        // Side face 1
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y1, z1);
        tessellator.addVertex(x2, y1, z1);
        tessellator.addVertex(x2, y2, z1);
        tessellator.addVertex(x1, y2, z1);
        tessellator.draw();

        // Side face 2
        tessellator.startDrawingQuads();
        tessellator.addVertex(x2, y1, z1);
        tessellator.addVertex(x2, y1, z2);
        tessellator.addVertex(x2, y2, z2);
        tessellator.addVertex(x2, y2, z1);
        tessellator.draw();

        // Side face 3
        tessellator.startDrawingQuads();
        tessellator.addVertex(x2, y1, z2);
        tessellator.addVertex(x1, y1, z2);
        tessellator.addVertex(x1, y2, z2);
        tessellator.addVertex(x2, y2, z2);
        tessellator.draw();

        // Side face 4
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y1, z2);
        tessellator.addVertex(x1, y1, z1);
        tessellator.addVertex(x1, y2, z1);
        tessellator.addVertex(x1, y2, z2);
        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        final TileEntityLaserBeacon ltile = (TileEntityLaserBeacon) tile;

        if (ltile.getShouldRender()) {
            // Push GL state
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);

            // Full brightness on this thing (Emits glow with shaders)
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.f, 240.f);

            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glRotated(ltile.rotationAngle, ltile.rotAxisX, ltile.rotAxisY, ltile.rotAxisZ);
            GL11.glTranslated(-x - 0.5, -y - 0.5, -z - 0.5);

            double range = ltile.getRange();
            renderFakeLine(ltile, x + 0.5, y + range, z + 0.5, y + 0.5);

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
