package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public final class Utils {

    public static boolean metaItemEqual(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage();
    }

    public static boolean fluidEqual(FluidStack a, FluidStack b) {
        return a.getFluid() == b.getFluid();
    }

    public static ItemStack copyAmount(int aAmount, ItemStack aStack) {
        ItemStack rStack = aStack.copy();
        if (isStackInvalid(rStack)) return null;
        if (aAmount > 64) aAmount = 64;
        else if (aAmount == -1) aAmount = 111;
        else if (aAmount < 0) aAmount = 0;
        rStack.stackSize = aAmount;
        return rStack;
    }

    public static boolean isStackInvalid(ItemStack aStack) {
        return aStack == null || aStack.getItem() == null || aStack.stackSize < 0;
    }

    public static ItemStack setStackSize(ItemStack itemStack, int amount) {
        if (itemStack == null) return null;
        if (amount < 0) {
            TwistSpaceTechnology.LOG
                .info("Error! Trying to set a item stack size lower than zero! " + itemStack + " to amount " + amount);
            return itemStack;
        }
        itemStack.stackSize = amount;
        return itemStack;
    }
}
