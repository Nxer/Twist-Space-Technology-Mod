package com.Nxer.TwistSpaceTechnology.client.render;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizons.angelica.api.IBlockAccessExtended;
import com.gtnewhorizons.angelica.config.AngelicaConfig;
import com.gtnewhorizons.angelica.rendering.celeritas.world.WorldSlice;
import com.prupe.mcpatcher.ctm.CTMUtils;
import com.prupe.mcpatcher.ctm.TileOverride;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.tile.TileLoader;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jss.notfine.config.MCPatcherForgeConfig;

/** Uses main-thread vanilla cube rendering because column textures need more than a section's two-block border. */
@SideOnly(Side.CLIENT)
public final class BottledChaosCapacityRenderer implements ISimpleBlockRenderingHandler {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    public static void register() {
        // Deliberately not ThreadSafeISBRH: Angelica must defer these cubes to its main-thread path.
        RenderingRegistry.registerBlockHandler(new BottledChaosCapacityRenderer());
        if (Loader.isModLoaded("angelica")) AngelicaAccess.registerCtm();
    }

    public static IBlockAccess getColumnAccess(IBlockAccess access) {
        if (!TessellatorManager.isOnMainThread() || !Loader.isModLoaded("angelica")) return access;
        return AngelicaAccess.unwrap(access);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        // Native RenderBlocks and Angelica handle culling, lighting and compact CTM.
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

    /** Sixteen cells share one registered, natively animated 4x4 sprite. */
    public static IIcon[] splitAtlas(IIcon atlas, int cells) {
        IIcon[] icons = new IIcon[cells];
        for (int cell = 0; cell < cells; cell++) icons[cell] = new AtlasRegion(atlas, cell);
        return icons;
    }

    public static IIcon getWorldIcon(IIcon icon) {
        if (!Loader.isModLoaded("angelica") || !AngelicaAccess.isCtmEnabled()) return icon;
        AtlasRegion region = (AtlasRegion) icon;
        if (region.ctmInput == null) region.ctmInput = new AtlasRegion(region.atlas, region.cell, true);
        return region.ctmInput;
    }

    private static String regionName(String atlasName, int cell) {
        return atlasName + "/cell_" + cell;
    }

    private static final class AtlasRegion implements IIcon {

        private final IIcon atlas;
        private final int cell;
        private final boolean ctmSource;
        private IIcon ctmInput;

        private AtlasRegion(IIcon atlas, int cell) {
            this(atlas, cell, false);
        }

        private AtlasRegion(IIcon atlas, int cell, boolean ctmSource) {
            this.atlas = atlas;
            this.cell = cell;
            this.ctmSource = ctmSource;
        }

        @Override
        public int getIconWidth() {
            return atlas.getIconWidth() / 4;
        }

        @Override
        public int getIconHeight() {
            return atlas.getIconHeight() / 4;
        }

        @Override
        public float getMinU() {
            return getInterpolatedU(0);
        }

        @Override
        public float getMaxU() {
            return getInterpolatedU(16);
        }

        @Override
        public float getInterpolatedU(double coordinate) {
            return atlas.getInterpolatedU((cell % 4) * 4 + coordinate / 4);
        }

        @Override
        public float getMinV() {
            return getInterpolatedV(0);
        }

        @Override
        public float getMaxV() {
            return getInterpolatedV(16);
        }

        @Override
        public float getInterpolatedV(double coordinate) {
            return atlas.getInterpolatedV((cell / 4) * 4 + coordinate / 4);
        }

        @Override
        public String getIconName() {
            // Only CTM inputs have virtual names. Rendered regions retain the parent animation sprite's name.
            return ctmSource ? regionName(atlas.getIconName(), cell) : atlas.getIconName();
        }
    }

    /** Keep the optional Angelica type out of the renderer's class-loading path when the mod is absent. */
    private static final class AngelicaAccess {

        private static void registerCtm() {
            CTMUtils.addCtmRegistrationCallback(overrides -> {
                // A fresh loader belongs to each resource reload. It returns regions without adding atlas sprites.
                TileLoader loader = new RegionLoader();
                for (int tier = 0; tier < 5; tier++) {
                    for (int layer = 0; layer < 16; layer++) register(overrides, loader, tier, 2, layer);
                    register(overrides, loader, tier, 1, 0);
                    register(overrides, loader, tier, 0, 0);
                }
            });
        }

        private static void register(CTMUtils.Overrides overrides, TileLoader loader, int tier, int side, int layer) {
            String path = "BottledChaosCapacity/" + (tier + 1)
                + "/"
                + (side >= 2 ? "layer_" + layer : side == 1 ? "top" : "bottom")
                + "/ctm";
            PropertiesFile properties = new PropertiesFile(
                CTMUtils.logger,
                new ResourceLocation(RESOURCE_ROOT_ID, "mcpatcher/ctm/" + path + ".properties"));
            String atlas = RESOURCE_ROOT_ID + ":BottledChaosCapacity/"
                + (tier + 1)
                + "/"
                + (side >= 2 ? "side_1" : "caps");
            properties.setProperty("method", "compact");
            properties.setProperty("connect", "block");
            properties.setProperty("faces", side >= 2 ? "sides" : side == 1 ? "top" : "bottom");
            properties.setProperty("matchTiles", regionName(atlas, side >= 2 ? layer : side == 1 ? 1 : 6));
            properties.setProperty("tiles", "0-4");
            CTMUtils.registerOverrideWithoutLock(overrides, TileOverride.create(properties, loader));
        }

        private static boolean isCtmEnabled() {
            return AngelicaConfig.enableMCPatcherForgeFeatures && MCPatcherForgeConfig.ConnectedTextures.standard;
        }

        private static IBlockAccess unwrap(IBlockAccess access) {
            // Preview and virtual-world accesses must retain their own blocks and coordinates.
            return access.getClass() == WorldSlice.class ? ((IBlockAccessExtended) access).getWorld() : access;
        }

        private static final class RegionLoader extends TileLoader {

            private RegionLoader() {
                super("textures/blocks", CTMUtils.logger);
            }

            @Override
            public boolean ignoreMissingTextures() {
                return true;
            }

            @Override
            public boolean preloadTile(ResourceLocation resource, boolean alternate) {
                // These are virtual addresses for existing regions, never PNGs to register or upload.
                return true;
            }

            @Override
            public IIcon getIcon(ResourceLocation resource) {
                String[] parts = resource.getResourcePath()
                    .split("/");
                int tier = Integer.parseInt(parts[3]) - 1;
                int variant = Integer.parseInt(parts[5].substring(0, parts[5].length() - 4));
                int side = parts[4].equals("top") ? 1 : parts[4].equals("bottom") ? 0 : 2;
                int layer = side >= 2 ? Integer.parseInt(parts[4].substring(6)) : 0;
                return TstBlocks.BottledChaosCapacity.getCapacityIcon(side, tier, layer, variant);
            }
        }
    }
}
