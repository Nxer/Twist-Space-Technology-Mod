package com.Nxer.TwistSpaceTechnology.common.block.meta.casing;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.Nxer.TwistSpaceTechnology.client.render.MetaGlowCasingRenderer;
import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Solid light-emitting casings, with optional per-variant and per-face fullbright texture layers. */
public class TstMetaBlockGlowCasing extends TstMetaBlockCasing {

    private final Map<Integer, GlowLayer[]> glowLayers = new HashMap<>();
    @SideOnly(Side.CLIENT)
    private Map<Integer, IIcon[]> baseIcons;
    @SideOnly(Side.CLIENT)
    private Map<Integer, IIcon[]> glowIcons;

    public TstMetaBlockGlowCasing(String unlocalizedName, byte textureIdOffsite) {
        super(unlocalizedName, textureIdOffsite);
        setLightLevel(1.0F);
    }

    /** Uses meta.png for the composite icon, and meta_base.png / meta_glow.png for world rendering. */
    public ItemStack registerVariantWithGlow(int meta) {
        String path = RESOURCE_ROOT_ID + ":" + unlocalizedName + "/" + meta;
        return registerVariantWithGlow(meta, new GlowLayer(path + "_base", path + "_glow"));
    }

    /**
     * One layer definition applies to all faces; six definitions use vanilla side order (down, up, north, south,
     * west, east). Plain registerVariant remains available without requiring any layer textures.
     */
    public ItemStack registerVariantWithGlow(int meta, GlowLayer... faces) {
        if (faces.length != 1 && faces.length != 6)
            throw new IllegalArgumentException("Expected one or six glow faces");
        GlowLayer[] copy = faces.clone();
        for (GlowLayer face : copy) Objects.requireNonNull(face, "Glow face");
        ItemStack variant = registerVariant(meta);
        glowLayers.put(meta, copy);
        return variant;
    }

    /** Atlas icon names, including namespace and excluding .png. A null glowTexture makes that face unlit. */
    @Desugar
    public record GlowLayer(String baseTexture, String glowTexture) {

        public GlowLayer {
            Objects.requireNonNull(baseTexture, "Base texture");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        baseIcons = new HashMap<>();
        glowIcons = new HashMap<>();
        for (var entry : glowLayers.entrySet()) {
            IIcon[] base = new IIcon[6];
            IIcon[] glow = new IIcon[6];
            GlowLayer[] faces = entry.getValue();
            for (int side = 0; side < 6; side++) {
                GlowLayer layer = faces[faces.length == 1 ? 0 : side];
                base[side] = register.registerIcon(layer.baseTexture());
                if (layer.glowTexture() != null) glow[side] = register.registerIcon(layer.glowTexture());
            }
            baseIcons.put(entry.getKey(), base);
            glowIcons.put(entry.getKey(), glow);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return MetaGlowCasingRenderer.RENDER_ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return getBaseIcon(side, world.getBlockMetadata(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getBaseIcon(int side, int meta) {
        IIcon[] icons = baseIcons == null ? null : baseIcons.get(meta);
        return icons == null ? super.getIcon(side, meta) : icons[side];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getGlowIcon(int side, int meta) {
        IIcon[] icons = glowIcons == null ? null : glowIcons.get(meta);
        return icons == null ? null : icons[side];
    }
}
