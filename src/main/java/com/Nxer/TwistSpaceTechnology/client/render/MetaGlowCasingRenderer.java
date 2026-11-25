package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.TstMetaBlockGlowCasing;
import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;
import com.gtnewhorizons.angelica.config.AngelicaConfig;
import com.prupe.mcpatcher.ctm.CTMUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Stateless cube layers; only visible conductor faces receive fullbright vertices. */
@SideOnly(Side.CLIENT)
@ThreadSafeISBRH(perThread = false)
public final class MetaGlowCasingRenderer implements ISimpleBlockRenderingHandler {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    private static final int FULL_BRIGHT = 0xF000F0;

    public static void register() {
        RenderingRegistry.registerBlockHandler(new MetaGlowCasingRenderer());
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        boolean rendered = renderer.renderStandardBlock(block, x, y, z);
        // Breaking overlays and StructureLib hints must retain their override texture.
        if (renderer.hasOverrideBlockTexture()) return rendered;
        TstMetaBlockGlowCasing casing = (TstMetaBlockGlowCasing) block;
        int meta = world.getBlockMetadata(x, y, z);
        boolean ambientOcclusion = renderer.enableAO;
        Tessellator tessellator = Tessellator.instance;
        try {
            renderer.enableAO = false;
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                IIcon icon = casing.getGlowIcon(side.ordinal(), meta);
                if (icon == null || (!renderer.renderAllFaces && !block
                    .shouldSideBeRendered(world, x + side.offsetX, y + side.offsetY, z + side.offsetZ, side.ordinal())))
                    continue;
                if (Loader.isModLoaded("angelica")) {
                    icon = AngelicaAccess.connectedIcon(icon, block, world, x, y, z, side.ordinal());
                }
                tessellator.setBrightness(FULL_BRIGHT);
                tessellator.setColorOpaque_F(1, 1, 1);
                renderFace(renderer, block, side, x, y, z, icon);
                rendered = true;
            }
        } finally {
            renderer.enableAO = ambientOcclusion;
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            tessellator.setColorOpaque_F(1, 1, 1);
        }
        return rendered;
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {
        TstMetaBlockGlowCasing casing = (TstMetaBlockGlowCasing) block;
        boolean ambientOcclusion = renderer.enableAO;
        renderer.enableAO = false;
        renderer.setRenderBoundsFromBlock(block);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_CURRENT_BIT);
        GL11.glPushMatrix();
        try {
            GL11.glRotatef(90, 0, 1, 0);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            renderInventoryLayer(casing, meta, renderer, false);
            GL11.glDisable(GL11.GL_LIGHTING);
            renderInventoryLayer(casing, meta, renderer, true);
        } finally {
            renderer.enableAO = ambientOcclusion;
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    private static void renderInventoryLayer(TstMetaBlockGlowCasing block, int meta, RenderBlocks renderer,
        boolean glowing) {
        Tessellator tessellator = Tessellator.instance;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            IIcon icon = glowing ? block.getGlowIcon(side.ordinal(), meta) : block.getBaseIcon(side.ordinal(), meta);
            if (icon == null) continue;
            tessellator.startDrawingQuads();
            tessellator.setNormal(side.offsetX, side.offsetY, side.offsetZ);
            tessellator.setColorOpaque_F(1, 1, 1);
            tessellator.setBrightness(FULL_BRIGHT);
            renderFace(renderer, block, side, 0, 0, 0, icon);
            tessellator.draw();
        }
    }

    private static void renderFace(RenderBlocks renderer, Block block, ForgeDirection side, int x, int y, int z,
        IIcon icon) {
        switch (side) {
            case DOWN -> renderer.renderFaceYNeg(block, x, y, z, icon);
            case UP -> renderer.renderFaceYPos(block, x, y, z, icon);
            case NORTH -> renderer.renderFaceZNeg(block, x, y, z, icon);
            case SOUTH -> renderer.renderFaceZPos(block, x, y, z, icon);
            case WEST -> renderer.renderFaceXNeg(block, x, y, z, icon);
            case EAST -> renderer.renderFaceXPos(block, x, y, z, icon);
            default -> {}
        }
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RENDER_ID;
    }

    /** Keep optional CTM classes out of execution when Angelica is absent or its CTM feature is disabled. */
    private static final class AngelicaAccess {

        private static IIcon connectedIcon(IIcon icon, Block block, IBlockAccess world, int x, int y, int z, int side) {
            return AngelicaConfig.enableMCPatcherForgeFeatures
                ? CTMUtils.getBlockIcon(icon, block, world, x, y, z, side)
                : icon;
        }
    }
}
