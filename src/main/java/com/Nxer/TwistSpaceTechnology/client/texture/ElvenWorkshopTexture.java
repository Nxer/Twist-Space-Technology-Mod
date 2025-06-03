package com.Nxer.TwistSpaceTechnology.client.texture;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.interfaces.IIconContainer;
import vazkii.botania.common.block.ModBlocks;

public class ElvenWorkshopTexture implements IIconContainer {

    IIconRegister iIconRegister;

    @Override
    public IIcon getIcon() {
        return ModBlocks.manaBomb.getBlockTextureFromSide(0);
    }

    @Override
    public IIcon getOverlayIcon() {
        return null;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return new ResourceLocation("Machines.ElvenWorkshop");
    }

}
