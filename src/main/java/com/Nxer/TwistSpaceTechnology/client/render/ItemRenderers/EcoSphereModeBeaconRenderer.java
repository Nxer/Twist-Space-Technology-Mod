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

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.gtnewhorizon.cropsnh.api.CropsNHItemList;

import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.CosmicItemRenderer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.util.GTModHandler;
import gregtech.common.render.items.InfinityRenderer;

public final class EcoSphereModeBeaconRenderer implements IItemRenderer {

    private static final float CONTENT_SCALE = 0.875F;
    private static final float ITEM_THICKNESS = 1.0F / 16.0F;
    /** Shrinks GT's 36px halo down to the 16px frame area. */
    private static final float HALO_SCALE = 16.0F / 36.0F;
    private static final CosmicItemRenderer COSMIC_RENDERER = new CosmicItemRenderer();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item != null && item.getItem() == TstItems.EcoSphereModeBeacon;
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
        int meta = item.getItemDamage();
        boolean upgrade = meta >= 8;
        IIcon background = upgrade ? TstItems.EcoSphereModeBeacon.getUpgradeBackgroundIcon()
            : TstItems.EcoSphereModeBeacon.getBackgroundIcon();
        IIcon frame = upgrade ? TstItems.EcoSphereModeBeacon.getUpgradeFrameIcon()
            : TstItems.EcoSphereModeBeacon.getFrameIcon();
        // Draw the background first so every beacon has the same solid base.
        renderBase(type, background, frame, item.getItemSpriteNumber());

        // Pick the item shown in the center from the beacon metadata.
        ItemStack displayStack = getDisplayStack(meta);

        // Keep all center-item changes inside this matrix.
        GL11.glPushMatrix();
        // Center and resize the item so it stays inside the frame.
        applyContentTransform(type, meta);
        if (displayStack.getItem() == LudicrousItems.infinity_sword) {
            // Keep the original cosmic effect for the Infinity Sword.
            applyLayerDepth(type, -1.0F);
            if (type == ItemRenderType.ENTITY) {
                // Skip Avaritia's extra ground-item shift so the sword stays centered.
                COSMIC_RENDERER.processLightLevel(type, displayStack, data);
                COSMIC_RENDERER.render(displayStack, null);
            } else {
                COSMIC_RENDERER.renderItem(type, displayStack, data);
            }
            restoreLayerDepth(type);
        } else if (meta == 12) {
            // Auto-Pulverize upgrade: the center Infinity dust reuses GT's Infinity effect,
            // but only inside the frame; the frame itself stays on top and the base below.
            renderInfinityItem(type, displayStack);
        } else {
            // Draw normal center items as flat front and back layers.
            renderVanillaItem(type, displayStack);
        }
        GL11.glPopMatrix();

        // Draw the frame last so it always stays above the center item.
        renderLayerIcon(type, frame, item.getItemSpriteNumber(), 2);
    }

    private static void applyLayerDepth(ItemRenderType type, float offset) {
        // Separate flat layers without moving them far apart.
        if (type == ItemRenderType.INVENTORY) return;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_POLYGON_BIT);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(offset, offset);
    }

    private static void restoreLayerDepth(ItemRenderType type) {
        if (type != ItemRenderType.INVENTORY) {
            GL11.glPopAttrib();
        }
    }

    private static ItemStack getDisplayStack(int meta) {
        // Each metadata value uses one familiar item as its mode icon.
        return switch (meta) {
            case 0 -> new ItemStack(Item.getItemFromBlock(Blocks.sapling), 1, 0);
            case 1 -> getTimewoodSapling();
            case 2 -> new ItemStack(Items.fish, 1, 0);
            case 3 -> getUnknownLiquidBucket();
            case 4 -> new ItemStack(Items.wheat_seeds);
            case 5 -> getGaiaWart();
            case 6 -> new ItemStack(Items.diamond_sword);
            case 7 -> new ItemStack(LudicrousItems.infinity_sword);
            case 8 -> ItemList.Cell_Empty.get(1);
            case 9 -> new ItemStack(Items.wheat);
            case 10 -> getNodeUpgrade();
            case 11 -> getModuleOutputUpgrade();
            case 12 -> Materials.Infinity.getDust(1);
            default -> GTCMItemList.TestItem0.get(1);
        };
    }

    private static ItemStack getNodeUpgrade() {
        ItemStack upgrade = Mods.ExtraUtilities.isModLoaded()
            ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "nodeUpgrade", 1, 0)
            : null;
        return upgrade == null ? new ItemStack(Items.sugar) : upgrade;
    }

    private static ItemStack getTimewoodSapling() {
        ItemStack sapling = Mods.TwilightForest.isModLoaded()
            ? GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5)
            : null;
        return sapling == null ? new ItemStack(Items.clock) : sapling;
    }

    private static ItemStack getUnknownLiquidBucket() {
        ItemStack bucket = Mods.GalaxySpace.isModLoaded()
            ? GTModHandler.getModItem(Mods.GalaxySpace.ID, "item.UnknowWaterBucket", 1)
            : null;
        return bucket == null ? new ItemStack(Items.water_bucket) : bucket;
    }

    private static ItemStack getGaiaWart() {
        ItemStack gaiaWart = CropsNHItemList.gaiaWart.get(1);
        return gaiaWart == null ? new ItemStack(Items.nether_wart) : gaiaWart;
    }

    private static ItemStack getModuleOutputUpgrade() {
        ItemStack upgrade = Mods.StevesCarts2.isModLoaded()
            ? GTModHandler.getModItem(Mods.StevesCarts2.ID, "upgrade", 1, 8)
            : null;
        return upgrade == null ? new ItemStack(Blocks.chest) : upgrade;
    }

    // spotless:off
    private static final float[][] CONTENT_OFFSETS = {
        { -0.83F, 0.83F, -0.83F },
        { 0.415F, -0.415F, -0.415F },
        { -0.415F, 0.415F, -0.415F },
        { 0.0F, 0.0F, 0.0F },
        { -0.415F, 0.415F, -0.415F },
        { 0.0F, 0.0F, 0.0F },
        { 0.0F, 0.0F, 0.0F },
        { 0.0F, 0.0F, 0.0F } };
    //spotless:on

    private static void applyContentTransform(ItemRenderType type, int meta) {
        int state = switch (type) {
            case INVENTORY -> 0;
            case ENTITY -> 1;
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> 2;
            default -> -1;
        };
        if (state < 0) return;

        float horizontalOffsetPixels = meta >= 0 && meta < CONTENT_OFFSETS.length ? CONTENT_OFFSETS[meta][state] : 0.0F;
        float centerOffset = type == ItemRenderType.INVENTORY ? 8.0F * (1.0F - CONTENT_SCALE)
            : (1.0F - CONTENT_SCALE) / 2.0F;
        GL11.glTranslatef(centerOffset, centerOffset, 0.0F);
        GL11.glScalef(CONTENT_SCALE, CONTENT_SCALE, 1.0F);
        float coordinateScale = type == ItemRenderType.INVENTORY ? 1.0F : 16.0F;
        GL11.glTranslatef(horizontalOffsetPixels / coordinateScale / CONTENT_SCALE, 0.0F, 0.0F);
    }

    private static void renderVanillaItem(ItemRenderType type, ItemStack displayStack) {
        if (type == ItemRenderType.INVENTORY) {
            // Use Minecraft's normal GUI renderer to keep item colors and lighting.
            Minecraft minecraft = Minecraft.getMinecraft();
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glTranslatef(0.0F, 0.0F, 2.0F);
            RenderItem.getInstance()
                .renderItemIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), displayStack, 0, 0, true);
            return;
        }

        IIcon icon = displayStack.getIconIndex();
        renderLayerIcon(type, icon, displayStack.getItemSpriteNumber(), 1);
    }

    private static void renderInfinityItem(ItemRenderType type, ItemStack displayStack) {
        if (type != ItemRenderType.INVENTORY) {
            // The Infinity effect is inventory-only; held and dropped items stay as normal center items.
            renderVanillaItem(type, displayStack);
            return;
        }

        IIcon overlay = null;
        IIcon icon = null;
        if (displayStack.getItem() instanceof IGT_ItemWithMaterialRenderer aMaterialItem) {
            int meta = displayStack.getItemDamage();
            overlay = aMaterialItem.getOverlayIcon(meta, 0);
            icon = aMaterialItem.getIcon(meta, 0);
        }

        // GT's sequence for inventories: halo, pulse, item. The halo is shrunk to the frame area.
        // The item body uses RenderItem.renderItemIntoGUI, which binds the right texture atlas.
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glPushMatrix();
        GL11.glTranslatef(8.0F, 8.0F, 0.0F);
        GL11.glScalef(HALO_SCALE, HALO_SCALE, 1.0F);
        GL11.glTranslatef(-8.0F, -8.0F, 0.0F);
        InfinityRenderer.renderHalo();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        InfinityRenderer.renderPulse(overlay, icon);
        renderVanillaItem(type, displayStack);
    }

    private static void renderBase(ItemRenderType type, IIcon background, IIcon frame, int spriteNumber) {
        if (type == ItemRenderType.INVENTORY) {
            // The inventory only needs the flat background image.
            renderIcon(type, background, spriteNumber, 0.0F);
            return;
        }
        if (background == null || frame == null) return;

        // The background supplies the front and back faces.
        renderLayerIcon(type, background, spriteNumber, 0);
        // The frame texture supplies clean outer edges for the held model.
        renderFrameSides(frame, spriteNumber);
    }

    private static void renderFrameSides(IIcon frame, int spriteNumber) {
        // Build four simple sides so the item looks solid from an angle.
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(
                Minecraft.getMinecraft()
                    .getTextureManager()
                    .getResourceLocation(spriteNumber));

        float pixelU = (frame.getMaxU() - frame.getMinU()) / frame.getIconWidth();
        float pixelV = (frame.getMaxV() - frame.getMinV()) / frame.getIconHeight();
        float leftU = frame.getMinU() + pixelU * 0.5F;
        float rightU = frame.getMaxU() - pixelU * 0.5F;
        float topV = frame.getMinV() + pixelV * 0.5F;
        float bottomV = frame.getMaxV() - pixelV * 0.5F;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV(0, 0, -ITEM_THICKNESS, leftU, frame.getMaxV());
        tessellator.addVertexWithUV(0, 0, 0, leftU, frame.getMaxV());
        tessellator.addVertexWithUV(0, 1, 0, leftU, frame.getMinV());
        tessellator.addVertexWithUV(0, 1, -ITEM_THICKNESS, leftU, frame.getMinV());

        tessellator.addVertexWithUV(1, 1, -ITEM_THICKNESS, rightU, frame.getMinV());
        tessellator.addVertexWithUV(1, 1, 0, rightU, frame.getMinV());
        tessellator.addVertexWithUV(1, 0, 0, rightU, frame.getMaxV());
        tessellator.addVertexWithUV(1, 0, -ITEM_THICKNESS, rightU, frame.getMaxV());

        tessellator.addVertexWithUV(0, 1, -ITEM_THICKNESS, frame.getMinU(), topV);
        tessellator.addVertexWithUV(0, 1, 0, frame.getMinU(), topV);
        tessellator.addVertexWithUV(1, 1, 0, frame.getMaxU(), topV);
        tessellator.addVertexWithUV(1, 1, -ITEM_THICKNESS, frame.getMaxU(), topV);

        tessellator.addVertexWithUV(1, 0, -ITEM_THICKNESS, frame.getMaxU(), bottomV);
        tessellator.addVertexWithUV(1, 0, 0, frame.getMaxU(), bottomV);
        tessellator.addVertexWithUV(0, 0, 0, frame.getMinU(), bottomV);
        tessellator.addVertexWithUV(0, 0, -ITEM_THICKNESS, frame.getMinU(), bottomV);

        tessellator.draw();
        GL11.glPopAttrib();
    }

    private static void renderLayerIcon(ItemRenderType type, IIcon icon, int spriteNumber, int layer) {
        if (type == ItemRenderType.INVENTORY) {
            // GUI layers use simple depth values and do not need model thickness.
            renderIcon(type, icon, spriteNumber, layer * 2.0F);
            return;
        }
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
        applyLayerDepth(type, -layer);

        Tessellator tessellator = Tessellator.instance;
        // Draw the front face.
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0, 0, 0.0F, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, 0, 0.0F, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, 1, 0.0F, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(0, 1, 0.0F, icon.getMaxU(), icon.getMinV());
        tessellator.draw();

        // Draw the same icon on the back so both sides look complete.
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        float backDepth = -ITEM_THICKNESS;
        tessellator.addVertexWithUV(0, 1, backDepth, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 1, backDepth, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 0, backDepth, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(0, 0, backDepth, icon.getMaxU(), icon.getMaxV());
        tessellator.draw();

        restoreLayerDepth(type);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private static void renderIcon(ItemRenderType type, IIcon icon, int spriteNumber, float depthOffset) {
        // This helper draws either a flat GUI icon or a normal thick item model.
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
