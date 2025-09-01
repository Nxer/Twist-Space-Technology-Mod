package com.Nxer.TwistSpaceTechnology.client.render;

import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.tile.TileLargeSolarBoilerRender;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LargeSolarBoilerRender extends TileEntitySpecialRenderer {

    public LargeSolarBoilerRender() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileLargeSolarBoilerRender.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float timeSinceLastTick) {
        TileLargeSolarBoilerRender tile = (TileLargeSolarBoilerRender) te;

        int xRightOffset = tile.xRightOffset;
        int zRightOffset = tile.zRightOffset;

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glDisable(GL11.GL_LIGHTING);

        Fluid water = FluidUtils.getWater(1).getFluid();
        Fluid steam = FluidUtils.getSteam(1).getFluid();

        GL11.glPushMatrix();
        GL11.glTranslated(x-1, y, z-1);
        renderFluidCuboid(water,
            0f, 0f, 0f,
            3f, 1f, 3f);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(x + xRightOffset * 5, y, z + zRightOffset * 5);
        renderFluidCuboid(steam,
            0f, 0f, 0f,
            1f, 1f, 1f);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(x + xRightOffset * -5, y, z + zRightOffset * -5);
        renderFluidCuboid(water,
            0f, 0f, 0f,
            1f, 1f, 1f);
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

    }

    public static void renderFluidCuboid(Fluid fluid, float minX, float minY, float minZ,
                                         float maxX, float maxY, float maxZ) {
        if (fluid == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tess = Tessellator.instance;

        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        IIcon icon = fluid.getStillIcon();
        if (icon == null) return;

        minX += 0.001f;
        minY += 0.001f;
        minZ += 0.001f;
        maxX -= 0.001f;
        maxY -= 0.001f;
        maxZ -= 0.001f;

        tess.startDrawingQuads();

        double u0 = icon.getInterpolatedU(0);
        double u16 = icon.getInterpolatedU(16);
        double v0 = icon.getInterpolatedV(0);
        double v16 = icon.getInterpolatedV(16);

        for (int bx = 0; bx < (int) Math.ceil(maxX); bx++) {
            for (int bz = 0; bz < (int) Math.ceil(maxZ); bz++) {
                tess.addVertexWithUV(bx, maxY, bz + 1, u0, v16);
                tess.addVertexWithUV(bx + 1, maxY, bz + 1, u16, v16);
                tess.addVertexWithUV(bx + 1, maxY, bz, u16, v0);
                tess.addVertexWithUV(bx, maxY, bz, u0, v0);

                tess.addVertexWithUV(bx, minY, bz, u0, v16);
                tess.addVertexWithUV(bx + 1, minY, bz, u16, v16);
                tess.addVertexWithUV(bx + 1, minY, bz + 1, u16, v0);
                tess.addVertexWithUV(bx, minY, bz + 1, u0, v0);
            }
        }

        for (int bx = 0; bx < (int) Math.ceil(maxX); bx++) {
            for (int by = 0; by < (int) Math.ceil(maxY); by++) {
                tess.addVertexWithUV(bx, by, maxZ, u0, v16);
                tess.addVertexWithUV(bx + 1, by, maxZ, u16, v16);
                tess.addVertexWithUV(bx + 1, by + 1, maxZ, u16, v0);
                tess.addVertexWithUV(bx, by + 1, maxZ, u0, v0);

                tess.addVertexWithUV(bx + 1, by, minZ, u0, v16);
                tess.addVertexWithUV(bx, by, minZ, u16, v16);
                tess.addVertexWithUV(bx, by + 1, minZ, u16, v0);
                tess.addVertexWithUV(bx + 1, by + 1, minZ, u0, v0);
            }
        }

        for (int bz = 0; bz < (int) Math.ceil(maxZ); bz++) {
            for (int by = 0; by < (int) Math.ceil(maxY); by++) {
                tess.addVertexWithUV(minX, by, bz, u0, v16);
                tess.addVertexWithUV(minX, by, bz + 1, u16, v16);
                tess.addVertexWithUV(minX, by + 1, bz + 1, u16, v0);
                tess.addVertexWithUV(minX, by + 1, bz, u0, v0);

                tess.addVertexWithUV(maxX, by, bz + 1, u0, v16);
                tess.addVertexWithUV(maxX, by, bz, u16, v16);
                tess.addVertexWithUV(maxX, by + 1, bz, u16, v0);
                tess.addVertexWithUV(maxX, by + 1, bz + 1, u0, v0);
            }
        }

        tess.draw();
    }
}
