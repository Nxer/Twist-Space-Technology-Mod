package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.IIconContainer;

public class SimpleIconContainer implements IIconContainer {

    private final String texturePath;
    private IIcon icon;

    public SimpleIconContainer(String texturePath) {
        this.texturePath = texturePath;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {
        if (icon == null) {
            System.err.println("Icon not registered for path: " + texturePath);
        }
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon() {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTextureFile() {
        return new ResourceLocation(texturePath);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcon(IIconRegister iconRegister) {
        String cleanPath = texturePath.substring(texturePath.indexOf(":") + 1);
        System.out.println("Attempting to register icon: " + cleanPath);
        this.icon = iconRegister.registerIcon(cleanPath);

        if (this.icon == null) {
            System.err.println("Failed to register icon: " + cleanPath);
        } else {
            System.out.println("Successfully registered icon: " + cleanPath);
        }
    }

}
