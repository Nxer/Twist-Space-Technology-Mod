package com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture;

import gregtech.api.interfaces.IIconContainer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.ModBlocks;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ElvenWorkshopTexture implements IIconContainer{
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
        // TODO Auto-generated method stub
        return new ResourceLocation("Machines.ElvenWorkshop");
    }
    
}
