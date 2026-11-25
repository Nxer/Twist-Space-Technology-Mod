package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.glodblock.github.common.item.ItemInfinityWaterStorageCell;

import appeng.tile.inventory.IAEStackInventory;
import appeng.util.item.AEFluidStack;

public class ItemInfinityFluidStorageCell extends ItemInfinityWaterStorageCell {

    private final String translationKey;
    private final String fluidName;

    public ItemInfinityFluidStorageCell(String registryName, String translationKey, String fluidName) {
        this.translationKey = translationKey;
        this.fluidName = fluidName;
        setUnlocalizedName(registryName);
        setTextureName("gtnhcommunitymod:" + registryName);
    }

    @Override
    public String getUnlocalizedName() {
        return translationKey;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    @Override
    public IAEStackInventory getConfigAEInventory(ItemStack stack) {
        IAEStackInventory config = new IAEStackInventory(null, 1);
        FluidStack fluid = FluidRegistry.getFluidStack(fluidName, 1_000);
        if (fluid != null) {
            config.putAEStackInSlot(0, AEFluidStack.create(fluid));
        }
        return config;
    }
}
