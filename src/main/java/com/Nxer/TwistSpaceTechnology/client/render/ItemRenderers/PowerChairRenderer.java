package com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockPowerChair;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockPowerChair;

public class PowerChairRenderer implements IItemRenderer {

    protected IModelCustom models;
    protected ResourceLocation textures;

    public PowerChairRenderer(final IModelCustom models, final ResourceLocation textures) {
        this.models = models;
        this.textures = textures;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (item.getItem() != ItemBlockPowerChair.getItemFromBlock(BlockPowerChair)) {
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
        if (item.getItem() == ItemBlockPowerChair.getItemFromBlock(BlockPowerChair)) {
            final ResourceLocation texture = this.textures;
            final IModelCustom model = this.models;
            switch (type) {
                case ENTITY -> {
                    GL11.glScaled(0.05, 0.05, 0.05);
                    GL11.glTranslated(0.0, 1.0, 1.0);
                }
                case EQUIPPED -> {
                    GL11.glScaled(0.05, 0.05, 0.05);
                    GL11.glTranslated(1.0, 0.8, 1.5);
                    GL11.glRotated(90, 0, 1, 0);
                }
                case EQUIPPED_FIRST_PERSON -> {
                    GL11.glScaled(0.05, 0.05, 0.05);
                    GL11.glTranslated(-8.0, 8.0, 8.0);
                    GL11.glRotated(90, 0, 1, 0);
                }
                case INVENTORY -> {
                    GL11.glTranslated(-0.1, -0.8, 0);
                    GL11.glScaled(0.05, 0.05, 0.05);
                    GL11.glRotated(180, 0, 1, 0);
                }
                default -> {}
            }
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            model.renderAll();
            GL11.glPopMatrix();
        }
    }
}
