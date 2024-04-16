package com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;

public class YamatoRenderer implements IItemRenderer {

    protected IModelCustom models;
    protected ResourceLocation textures;

    public YamatoRenderer(final IModelCustom models, final ResourceLocation textures) {
        this.models = models;
        this.textures = textures;
    }

    /**
     * Checks if this renderer should handle a specific item's render type
     *
     * @param item The item we are trying to render
     * @param type A render type to check if this renderer handles
     * @return true if this renderer should handle the given render type,
     *         otherwise false
     */
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (item.getItem() != BasicItems.Yamato) {
            return false;
        }
        return switch (type) {
            case ENTITY, EQUIPPED, EQUIPPED_FIRST_PERSON, INVENTORY -> true;
            default -> false;
        };
    }

    /**
     * Checks if certain helper functionality should be executed for this renderer.
     * See ItemRendererHelper for more info
     *
     * @param type   The render type
     * @param item   The ItemStack being rendered
     * @param helper The type of helper functionality to be ran
     * @return True to run the helper functionality, false to not.
     */
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    /**
     * Called to do the actual rendering, see ItemRenderType for details on when specific
     * types are run, and what extra data is passed into the data parameter.
     *
     * @param type The render type
     * @param item The ItemStack being rendered
     * @param data Extra Type specific data
     */
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item.getItem() == BasicItems.Yamato) {
            final ResourceLocation texture = this.textures;
            final IModelCustom model = this.models;
            switch (type) {
                case ENTITY -> {
                    GL11.glScaled(0.01, 0.01, 0.01);
                    GL11.glTranslated(0.0, 1.0, 1.0);
                }
                case EQUIPPED -> {
                    GL11.glScaled(0.015, 0.015, 0.015);
                    GL11.glTranslated(50.0, 35.0, 50.0);
                    GL11.glRotated(-45, 0.0, 1.0, 0.0);
                    GL11.glRotated(180, 1.0, 0.0, 0.0);
                    GL11.glRotated(45, 0.0, 0.0, 1.0);
                }
                case EQUIPPED_FIRST_PERSON -> {
                    GL11.glScaled(0.01, 0.01, 0.01);
                    GL11.glTranslated(50.0, 45.0, 50.0);
                    GL11.glRotated(-145, 0.0, 1.0, 0.0);
                    GL11.glRotated(180, 1.0, 0.0, 0.0);
                    GL11.glRotated(80, 0.0, 0.0, 1.0);

                }
                case INVENTORY -> {
                    GL11.glTranslated(-0.6, -0.75, 0);
                    GL11.glScaled(0.01, 0.01, 0.01);
                    GL11.glRotated(-45, 0, 0, 1);
                    GL11.glRotated(-70, 0, 1, 0);
                    GL11.glRotated(90, 1, 0, 0);
                }
                default -> {}
            }
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            if (item.getItemDamage() == 1) {
                model.renderAllExcept("daoqiao");
            } else model.renderAll();
            GL11.glPopMatrix();
        }
    }
}
