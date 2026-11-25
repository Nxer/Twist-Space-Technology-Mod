package com.Nxer.TwistSpaceTechnology.client.render;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.TstMetaBlockGlowCasing;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarRings;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarRings.Material;
import com.gtnewhorizons.angelica.config.AngelicaConfig;
import com.prupe.mcpatcher.ctm.CTMUtils;

import cpw.mods.fml.common.Loader;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import tectech.util.TextureUpdateRequester;

/** Exposed, CTM-connected block faces cached once per ring. Only the emissive layer is fullbright. */
final class StarRingMesh {

    private static final float[] SHADE = { .5f, 1, .8f, .8f, .6f, .6f };
    private final List<StarRings.Cell> cells;
    private final RingAccess access = new RingAccess();
    private final RenderBlocks renderer = new RenderBlocks(access);
    private final List<Integer> baseLists = new ArrayList<>(), glowLists = new ArrayList<>();
    private final TextureUpdateRequester textures = new TextureUpdateRequester();
    private int nextCell;

    StarRingMesh(int ring) {
        cells = StarRings.ring(ring);
        EnumSet<Material> registered = EnumSet.noneOf(Material.class);
        for (StarRings.Cell cell : cells) {
            access.cells.put(StarRings.key(cell.x, cell.y, cell.z), cell);
            if (registered.add(cell.material)) textures.add(cell.material.block(), cell.material.meta);
        }
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.enableAO = false;
    }

    long build(long budget) {
        if (budget <= 0 || ready()) return 0;
        long start = System.nanoTime();
        do {
            int end = Math.min(nextCell + 128, cells.size());
            int base = compile(nextCell, end, false);
            if (base == 0) break;
            int glow = compile(nextCell, end, true);
            if (glow == 0) {
                GL11.glDeleteLists(base, 1);
                break;
            }
            baseLists.add(base);
            glowLists.add(glow);
            nextCell = end;
        } while (!ready() && System.nanoTime() - start < budget);
        return System.nanoTime() - start;
    }

    boolean ready() {
        return nextCell == cells.size();
    }

    private int compile(int from, int to, boolean glowing) {
        int list = GL11.glGenLists(1);
        if (list == 0) return 0;
        GL11.glNewList(list, GL11.GL_COMPILE);
        Tessellator tess = Tessellator.instance;
        try {
            tess.startDrawingQuads();
            // Intentionally omit per-vertex brightness. Each cached layer gets a live lightmap value at draw time.
            for (int i = from; i < to; i++) {
                StarRings.Cell cell = cells.get(i);
                Block block = cell.material.block();
                int meta = cell.material.meta;
                TstMetaBlockGlowCasing layered = block instanceof TstMetaBlockGlowCasing casing ? casing : null;
                boolean fullbright = cell.material == Material.GREENSCREEN || cell.material == Material.TRANSLUCENT
                    || (layered == null && block.getLightValue(access, cell.x, cell.y, cell.z) == 15);
                int color = block.getRenderColor(meta);
                for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
                    if (access.getBlock(cell.x + face.offsetX, cell.y + face.offsetY, cell.z + face.offsetZ)
                        .isOpaqueCube()) continue;
                    int side = face.ordinal();
                    IIcon icon;
                    if (layered != null)
                        icon = glowing ? layered.getGlowIcon(side, meta) : layered.getBaseIcon(side, meta);
                    else if (glowing != fullbright) icon = null;
                    else if (cell.material == Material.ARCANE_TILE_3 || cell.material == Material.ARCANE_TILE_8
                        || cell.material == Material.ARCANE_TILE_4)
                        icon = block.getIcon(access, cell.x, cell.y, cell.z, side);
                    else icon = block.getIcon(side, meta);
                    if (icon == null) continue;
                    if (Loader.isModLoaded("angelica")) icon = ConnectedTexture.icon(icon, block, access, cell, side);
                    float shade = glowing ? 1 : SHADE[side];
                    int tint = glowing && layered != null ? 0xFFFFFF : color;
                    tess.setColorOpaque_F(
                        (tint >> 16 & 255) / 255f * shade,
                        (tint >> 8 & 255) / 255f * shade,
                        (tint & 255) / 255f * shade);
                    tess.setNormal(face.offsetX, face.offsetY, face.offsetZ);
                    double x = cell.x - .5, y = cell.y - StarRings.CENTER_Y - .5, z = cell.z - .5;
                    switch (face) {
                        case DOWN -> renderer.renderFaceYNeg(block, x, y, z, icon);
                        case UP -> renderer.renderFaceYPos(block, x, y, z, icon);
                        case NORTH -> renderer.renderFaceZNeg(block, x, y, z, icon);
                        case SOUTH -> renderer.renderFaceZPos(block, x, y, z, icon);
                        case WEST -> renderer.renderFaceXNeg(block, x, y, z, icon);
                        case EAST -> renderer.renderFaceXPos(block, x, y, z, icon);
                        default -> {}
                    }
                }
            }
            tess.draw();
        } finally {
            GL11.glEndList();
        }
        return list;
    }

    void draw(int light) {
        if (!ready()) return;
        textures.requestUpdate();
        // Extra Utilities' inventory renderer enables fixed-function lighting while waking its texture.
        GL11.glDisable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light & 65535, light >>> 16);
        for (int list : baseLists) GL11.glCallList(list);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        for (int list : glowLists) GL11.glCallList(list);
    }

    void dispose() {
        for (int list : baseLists) GL11.glDeleteLists(list, 1);
        for (int list : glowLists) GL11.glDeleteLists(list, 1);
        baseLists.clear();
        glowLists.clear();
    }

    private static final class ConnectedTexture {

        static IIcon icon(IIcon icon, Block block, IBlockAccess access, StarRings.Cell cell, int side) {
            return AngelicaConfig.enableMCPatcherForgeFeatures
                ? CTMUtils.getBlockIcon(icon, block, access, cell.x, cell.y, cell.z, side)
                : icon;
        }
    }

    /** Neighbours belong to this ring alone, so moving rings never borrow each other's CTM connections. */
    private static final class RingAccess implements IBlockAccess {

        final Int2ObjectOpenHashMap<StarRings.Cell> cells = new Int2ObjectOpenHashMap<>();

        private StarRings.Cell cell(int x, int y, int z) {
            return x < -84 || x > 84 || y < 16 || y > 184 || z < -84 || z > 84 ? null
                : cells.get(StarRings.key(x, y, z));
        }

        @Override
        public Block getBlock(int x, int y, int z) {
            StarRings.Cell cell = cell(x, y, z);
            return cell == null ? Blocks.air : cell.material.block();
        }

        @Override
        public int getBlockMetadata(int x, int y, int z) {
            StarRings.Cell cell = cell(x, y, z);
            return cell == null ? 0 : cell.material.meta;
        }

        @Override
        public TileEntity getTileEntity(int x, int y, int z) {
            return null;
        }

        @Override
        public int getLightBrightnessForSkyBlocks(int x, int y, int z, int light) {
            return 0xF000F0;
        }

        @Override
        public boolean isAirBlock(int x, int y, int z) {
            return cell(x, y, z) == null;
        }

        @Override
        public BiomeGenBase getBiomeGenForCoords(int x, int z) {
            return BiomeGenBase.plains;
        }

        @Override
        public int getHeight() {
            return 256;
        }

        @Override
        public boolean extendedLevelsInChunkCache() {
            return false;
        }

        @Override
        public int isBlockProvidingPowerTo(int x, int y, int z, int side) {
            return 0;
        }

        @Override
        public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean defaultValue) {
            return getBlock(x, y, z).isSideSolid(this, x, y, z, side);
        }
    }
}
