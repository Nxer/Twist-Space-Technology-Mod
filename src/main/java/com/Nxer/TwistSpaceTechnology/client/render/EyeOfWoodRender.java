package com.Nxer.TwistSpaceTechnology.client.render;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.tile.TileEyeOfWoodRender;
import com.Nxer.TwistSpaceTechnology.config.Config;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EyeOfWoodRender extends TileEntitySpecialRenderer {

    public static final ResourceLocation WOOD_SWEAT_TEXTURE = new ResourceLocation(
        RESOURCE_ROOT_ID + ":" + "model/Sweat.png");
    public static final IModelCustom WOOD_SWEAT_MODEL = AdvancedModelLoader
        .loadModel(new ResourceLocation(RESOURCE_ROOT_ID + ":" + "model/Sweat.obj"));

    public static final ResourceLocation WOOD_THINKING_TEXTURE = new ResourceLocation(
        RESOURCE_ROOT_ID + ":" + "model/Thinking.png");
    public static final IModelCustom WOOD_THINKING_MODEL = AdvancedModelLoader
        .loadModel(new ResourceLocation(RESOURCE_ROOT_ID + ":" + "model/Thinking.obj"));

    public EyeOfWoodRender() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEyeOfWoodRender.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEyeOfWoodRender star)) return;
        double teX = tile.xCoord;
        double teY = tile.yCoord;
        double teZ = tile.zCoord;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        double playerX = player.posX;
        double playerY = player.posY;
        double playerZ = player.posZ;

        double dx = playerX - (teX + 0.5);
        double dy = playerY - (teY + 0.5);
        double dz = playerZ - (teZ + 0.5);

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        double yaw = -Math.atan2(dz, dx) * (180.0 / Math.PI);
        double pitch = Math.atan2(dy, distanceXZ) * (180.0 / Math.PI);

        final double size = star.size;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        GL11.glRotated(yaw + 90, 0, 1, 0);
        GL11.glRotated(-pitch, 1, 0, 0);

        renderStar(size);
        GL11.glPopMatrix();
    }

    public void renderStar(double size) {
        ResourceLocation texture;
        IModelCustom model;
        switch (Config.RenderModelDefault_EyeOfWood) {
            case 1 -> {
                model = WOOD_THINKING_MODEL;
                texture = WOOD_THINKING_TEXTURE;
            }
            default -> {
                model = WOOD_SWEAT_MODEL;
                texture = WOOD_SWEAT_TEXTURE;
            }
        }
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(texture);
        GL11.glScaled(size, size, size);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
