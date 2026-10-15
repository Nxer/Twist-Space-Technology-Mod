package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizons.angelica.api.IBlockAccessExtended;
import com.gtnewhorizons.angelica.rendering.celeritas.world.WorldSlice;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Uses main-thread vanilla cube rendering because column textures need more than a section's two-block border. */
@SideOnly(Side.CLIENT)
public final class BottledChaosCapacityRenderer implements ISimpleBlockRenderingHandler {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    public static void register() {
        // Deliberately not ThreadSafeISBRH: Angelica must defer these cubes to its main-thread path.
        RenderingRegistry.registerBlockHandler(new BottledChaosCapacityRenderer());
    }

    public static IBlockAccess getColumnAccess(IBlockAccess access) {
        if (!TessellatorManager.isOnMainThread() || !Loader.isModLoaded("angelica")) return access;
        return AngelicaAccess.unwrap(access);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        // Keep the supplied access for native face culling, lighting and CTM; only the height scan unwraps it.
        return renderer.renderStandardBlock(block, x, y, z);
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {
        boolean ambientOcclusion = renderer.enableAO;
        renderer.enableAO = false;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glPushMatrix();
        try {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            Tessellator tessellator = Tessellator.instance;
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                IIcon icon = block.getIcon(side.ordinal(), meta);
                tessellator.startDrawingQuads();
                tessellator.setNormal(side.offsetX, side.offsetY, side.offsetZ);
                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                switch (side) {
                    case DOWN -> renderer.renderFaceYNeg(block, 0, 0, 0, icon);
                    case UP -> renderer.renderFaceYPos(block, 0, 0, 0, icon);
                    case NORTH -> renderer.renderFaceZNeg(block, 0, 0, 0, icon);
                    case SOUTH -> renderer.renderFaceZPos(block, 0, 0, 0, icon);
                    case WEST -> renderer.renderFaceXNeg(block, 0, 0, 0, icon);
                    case EAST -> renderer.renderFaceXPos(block, 0, 0, 0, icon);
                    default -> {}
                }
                tessellator.draw();
            }
        } finally {
            renderer.enableAO = ambientOcclusion;
            GL11.glPopMatrix();
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

    /** Keep the optional Angelica type out of the renderer's class-loading path when the mod is absent. */
    private static final class AngelicaAccess {

        private static IBlockAccess unwrap(IBlockAccess access) {
            // Preview and virtual-world accesses must retain their own blocks and coordinates.
            return access.getClass() == WorldSlice.class ? ((IBlockAccessExtended) access).getWorld() : access;
        }
    }
}
