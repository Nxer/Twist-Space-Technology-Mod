package com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

/**
 * Casing and front overlays using GT's texture API and renderer.
 * Create path-based textures during machine registration or registerIcons, before GT stitches its block icons.
 * Paths use "modid:path", without "textures/blocks/" or ".png".
 */
public final class TSTControllerTextures {

    private final ITexture[] sides;
    private final ITexture[] front;

    public TSTControllerTextures(String casing, String overlay) {
        this(casing, overlay(overlay));
    }

    public TSTControllerTextures(String casing, ITexture... overlays) {
        this(TextureFactory.of(icon(casing)), overlays);
    }

    /** Overlays are drawn in the supplied order, with no fixed layer limit. */
    public TSTControllerTextures(ITexture casing, ITexture... overlays) {
        sides = new ITexture[] { casing };
        front = new ITexture[overlays.length + 1];
        front[0] = casing;
        System.arraycopy(overlays, 0, front, 1, overlays.length);
    }

    public ITexture[] getTexture(ForgeDirection side, ForgeDirection facing) {
        return side == facing ? front : sides;
    }

    /** Casing and one overlay, independent of machine activity. */
    public static ITexture[] getTexture(ForgeDirection side, ForgeDirection facing, ITexture casing,
        IIconContainer overlay) {
        return getTexture(side, facing, false, casing, overlay, null, null, null);
    }

    /** Casing and one glowing overlay, independent of machine activity. */
    public static ITexture[] getGlowTexture(ForgeDirection side, ForgeDirection facing, ITexture casing,
        IIconContainer overlay) {
        return getTexture(side, facing, false, casing, null, overlay, null, null);
    }

    /** Casing, inactive overlay, active overlay. */
    public static ITexture[] getTexture(ForgeDirection side, ForgeDirection facing, boolean active, ITexture casing,
        IIconContainer inactiveOverlay, IIconContainer activeOverlay) {
        return getTexture(side, facing, active, casing, inactiveOverlay, null, activeOverlay, null);
    }

    /** Casing, inactive overlay, active overlay, active glow. */
    public static ITexture[] getTexture(ForgeDirection side, ForgeDirection facing, boolean active, ITexture casing,
        IIconContainer inactiveOverlay, IIconContainer activeOverlay, IIconContainer activeGlow) {
        return getTexture(side, facing, active, casing, inactiveOverlay, null, activeOverlay, activeGlow);
    }

    /** Casing, inactive overlay, inactive glow, active overlay, active glow. Null omits a layer. */
    public static ITexture[] getTexture(ForgeDirection side, ForgeDirection facing, boolean active, ITexture casing,
        IIconContainer inactiveOverlay, IIconContainer inactiveGlow, IIconContainer activeOverlay,
        IIconContainer activeGlow) {
        if (side != facing) return new ITexture[] { casing };
        IIconContainer overlay = active ? activeOverlay : inactiveOverlay;
        IIconContainer glow = active ? activeGlow : inactiveGlow;
        if (overlay != null && glow != null) {
            return Textures.BlockIcons.createTextureWithCasing(
                () -> casing,
                side,
                facing,
                active,
                inactiveOverlay,
                inactiveGlow,
                activeOverlay,
                activeGlow);
        }
        if (overlay != null) return new ITexture[] { casing, overlay(overlay) };
        if (glow != null) return new ITexture[] { casing, glowOverlay(glow) };
        return new ITexture[] { casing };
    }

    public static ITexture overlay(String path) {
        return overlay(icon(path));
    }

    public static ITexture glowOverlay(String path) {
        return glowOverlay(icon(path));
    }

    public static ITexture overlay(IIconContainer icon) {
        return overlay(icon, false);
    }

    public static ITexture glowOverlay(IIconContainer icon) {
        return overlay(icon, true);
    }

    private static ITexture overlay(IIconContainer icon, boolean glow) {
        return TextureFactory.builder()
            .addIcon(icon)
            .extFacing()
            .glow(glow)
            .build();
    }

    private static IIconContainer icon(String path) {
        ResourceLocation location = new ResourceLocation(path);
        return Textures.BlockIcons.custom(location.getResourceDomain(), location.getResourcePath());
    }
}
