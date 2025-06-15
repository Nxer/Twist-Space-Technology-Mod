package com.Nxer.TwistSpaceTechnology.client.texture;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;

public enum TstMachineTextures implements IIconContainer {

    OVERLAY_FRONT_PROCESSING_ARRAY("blocks/iconSets/OVERLAY_FRONT_PROCESSING_ARRAY"),
    OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE("blocks/iconSets/OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE"),
    OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW("blocks/iconSets/OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW"),
    OVERLAY_FRONT_PROCESSING_ARRAY_GLOW("blocks/iconSets/OVERLAY_FRONT_PROCESSING_ARRAY_GLOW"),

    oMCAIndustrialMultiMachine("blocks/iconSets/gtpp/industrialMultiMachine"),
    oMCAIndustrialMultiMachineActive("blocks/iconSets/gtpp/industrialMultiMachineActive"),;

    @Nullable
    public IIcon icon;

    TstMachineTextures(Supplier<IIcon> iconSupplier) {
        GregTechAPI.sGTBlockIconload.add(new Runnable() {

            @Override
            public void run() {
                icon = Objects.requireNonNull(
                    iconSupplier.get(),
                    "Failed to load icon for " + name() + " because the supplier returned null.");
            }

            @Override
            public String toString() {
                return "TstTextures{" + "name=" + name() + ", " + "iconSupplier=" + iconSupplier.toString() + '}';
            }
        });
    }

    TstMachineTextures(String path) {
        this(() -> GregTechAPI.sBlockIcons.registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":" + path));
    }

    public static void init() {
        // no-op, just to ensure the enum is loaded
    }

    @Override
    public @Nullable IIcon getIcon() {
        return icon;
    }

    @Override
    public IIcon getOverlayIcon() {
        return null;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return TextureMap.locationBlocksTexture;
    }
}
