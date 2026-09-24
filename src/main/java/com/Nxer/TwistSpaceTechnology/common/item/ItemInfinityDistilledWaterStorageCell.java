package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.glodblock.github.common.item.ItemInfinityWaterStorageCell;

import appeng.tile.inventory.IAEStackInventory;
import appeng.util.item.AEFluidStack;

public class ItemInfinityDistilledWaterStorageCell extends ItemInfinityWaterStorageCell {

    public ItemInfinityDistilledWaterStorageCell() {
        setUnlocalizedName("InfinityDistilledWaterStorageCell");
        setTextureName("gtnhcommunitymod:InfinityDistilledWaterStorageCell");
    }

    @Override
    public String getUnlocalizedName() {
        return "item.tst.common.infinity_distilled_water_storage_cell";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    @Override
    public IAEStackInventory getConfigAEInventory(ItemStack stack) {
        IAEStackInventory config = new IAEStackInventory(null, 1);
        config.putAEStackInSlot(0, AEFluidStack.create(FluidRegistry.getFluidStack("ic2distilledwater", 1_000)));
        return config;
    }
}
