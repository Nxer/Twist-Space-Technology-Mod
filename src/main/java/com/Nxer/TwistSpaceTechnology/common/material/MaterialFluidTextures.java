package com.Nxer.TwistSpaceTechnology.common.material;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

/**
 * Fully colored textures for existing material fluids; their registration identities stay with their original owner.
 */
public final class MaterialFluidTextures {

    private static final Map<String, ResourceLocation> TEXTURES = new LinkedHashMap<>();
    private static final Map<String, ResourceLocation> TEXTURES_VIEW = Collections.unmodifiableMap(TEXTURES);

    static {
        register(
            "concentrated uu-matter",
            new ResourceLocation(TwistSpaceTechnology.RESOURCE_ROOT_ID, "fluids/concentrated_uu"));
    }

    public static void register(String fluidName, ResourceLocation texture) {
        if (TEXTURES.putIfAbsent(Objects.requireNonNull(fluidName), Objects.requireNonNull(texture)) != null) {
            throw new IllegalArgumentException("Duplicate material fluid texture: " + fluidName);
        }
    }

    public static Map<String, ResourceLocation> getTextures() {
        return TEXTURES_VIEW;
    }

    public static boolean hasFullColorTexture(Fluid fluid) {
        return fluid != null && TEXTURES.containsKey(fluid.getName());
    }
}
