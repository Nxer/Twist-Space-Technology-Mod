package com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers;

import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_SWEAT_MODEL;
import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_SWEAT_TEXTURE;
import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_THINKING_MODEL;
import static com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender.WOOD_THINKING_TEXTURE;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.BlockEyeOfWoodRender;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.config.Config;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EyeOfWoodRender implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (item.getItem() != Item.getItemFromBlock(BlockEyeOfWoodRender)) {
            return false;
        }
        return switch (type) {
            case ENTITY, EQUIPPED, EQUIPPED_FIRST_PERSON, INVENTORY -> true;
            default -> false;
        };
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item.getItem() != Item.getItemFromBlock(BlockEyeOfWoodRender)) return;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        switch (type) {
            case ENTITY:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(5, 1, 1, 1);
                GL11.glRotated(90, 0, 1, 0);
            case EQUIPPED:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(5, 0, 1, 1);
                GL11.glRotated(90, 0, 1, 0);
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(5, 0, 1, 1);
                GL11.glRotated(180, 0, 1, 0);
            case INVENTORY:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glScaled(0.4, 0.4, 0.4);
                GL11.glRotated(5, 1, 1, 1);
        }
        switch (Config.RenderModelDefault_EyeOfWood) {
            case 1 -> {
                Minecraft.getMinecraft().renderEngine.bindTexture(WOOD_THINKING_TEXTURE);
                WOOD_THINKING_MODEL.renderAll();
            }
            default -> {
                Minecraft.getMinecraft().renderEngine.bindTexture(WOOD_SWEAT_TEXTURE);
                WOOD_SWEAT_MODEL.renderAll();
            }
        }
        GL11.glPopMatrix();

    }
}
