package com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.init.TstItems;

import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.CosmicItemRenderer;

public final class MegaTreeFarmModeSymbolRenderer implements IItemRenderer {

    private static final float CONTENT_SCALE = 0.875F;
    private static final CosmicItemRenderer COSMIC_RENDERER = new CosmicItemRenderer();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item != null && item.getItem() == TstItems.MegaTreeFarmModeSymbol;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        ItemStack displayStack = getDisplayStack(item.getItemDamage());
        if (displayStack.getItem() == LudicrousItems.infinity_sword) {
            return COSMIC_RENDERER.shouldUseRenderHelper(type, displayStack, helper);
        }
        return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        renderIcon(
            type,
            item.getItem()
                .getIcon(item, 0),
            item.getItemSpriteNumber(),
            0.0F);

        int meta = item.getItemDamage();
        ItemStack displayStack = getDisplayStack(meta);
        GL11.glPushMatrix();
        applyContentOffset(type, meta);
        applyContentScale(type);
        if (displayStack.getItem() == LudicrousItems.infinity_sword) {
            COSMIC_RENDERER.renderItem(type, displayStack, data);
        } else {
            renderVanillaItem(type, displayStack);
        }
        GL11.glPopMatrix();

        renderIcon(type, TstItems.MegaTreeFarmModeSymbol.getFrameIcon(), item.getItemSpriteNumber(), 4.0F);
    }

    private static ItemStack getDisplayStack(int meta) {
        return switch (meta) {
            case 0 -> new ItemStack(Item.getItemFromBlock(Blocks.sapling), 1, 0);
            case 1 -> new ItemStack(Items.fish, 1, 0);
            case 2 -> new ItemStack(Items.wheat_seeds);
            case 3 -> new ItemStack(Items.diamond_sword);
            case 4 -> new ItemStack(LudicrousItems.infinity_sword);
            default -> new ItemStack(Items.diamond_sword);
        };
    }

    private static void applyContentOffset(ItemRenderType type, int meta) {
        if (type != ItemRenderType.INVENTORY) return;

        float horizontalOffset = switch (meta) {
            case 0 -> -1.0F;
            case 1, 2 -> -0.5F;
            default -> 0.0F;
        };
        GL11.glTranslatef(horizontalOffset, 0.0F, 0.0F);
    }

    private static void applyContentScale(ItemRenderType type) {
        switch (type) {
            case INVENTORY -> {
                float offset = 8.0F * (1.0F - CONTENT_SCALE);
                GL11.glTranslatef(offset, offset, 0.0F);
                GL11.glScalef(CONTENT_SCALE, CONTENT_SCALE, 1.0F);
            }
            case ENTITY -> GL11.glScalef(CONTENT_SCALE, CONTENT_SCALE, CONTENT_SCALE);
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> {
                float offset = (1.0F - CONTENT_SCALE) / 2.0F;
                GL11.glTranslatef(offset, offset, 0.0F);
                GL11.glScalef(CONTENT_SCALE, CONTENT_SCALE, CONTENT_SCALE);
            }
            default -> {}
        }
    }

    private static void renderVanillaItem(ItemRenderType type, ItemStack displayStack) {
        if (type == ItemRenderType.INVENTORY) {
            Minecraft minecraft = Minecraft.getMinecraft();
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glTranslatef(0.0F, 0.0F, 2.0F);
            RenderItem.getInstance()
                .renderItemIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), displayStack, 0, 0, true);
            return;
        }

        IIcon icon = displayStack.getIconIndex();
        renderIcon(type, icon, displayStack.getItemSpriteNumber(), 2.0F);
    }

    private static void renderIcon(ItemRenderType type, IIcon icon, int spriteNumber, float depthOffset) {
        if (icon == null) return;

        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(
                Minecraft.getMinecraft()
                    .getTextureManager()
                    .getResourceLocation(spriteNumber));
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslatef(0.0F, 0.0F, depthOffset);
        if (type == ItemRenderType.INVENTORY) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(0, 16, 0, icon.getMinU(), icon.getMaxV());
            tessellator.addVertexWithUV(16, 16, 0, icon.getMaxU(), icon.getMaxV());
            tessellator.addVertexWithUV(16, 0, 0, icon.getMaxU(), icon.getMinV());
            tessellator.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
            tessellator.draw();
        } else {
            ItemRenderer.renderItemIn2D(
                Tessellator.instance,
                icon.getMaxU(),
                icon.getMinV(),
                icon.getMinU(),
                icon.getMaxV(),
                icon.getIconWidth(),
                icon.getIconHeight(),
                1.0F / 16.0F);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
