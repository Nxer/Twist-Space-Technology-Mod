package com.Nxer.TwistSpaceTechnology.util;

import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

public final class NBTUtils {

    public static ItemStack loadItem(NBTTagCompound aNBT, String aTagName) {
        return loadItem(aNBT.getCompoundTag(aTagName));
    }

    public static FluidStack loadFluid(NBTTagCompound aNBT, String aTagName) {
        return loadFluid(aNBT.getCompoundTag(aTagName));
    }

    public static FluidStack loadFluid(NBTTagCompound aNBT) {
        if (aNBT == null) return null;
        return FluidStack.loadFluidStackFromNBT(aNBT);
    }

    public static ItemStack loadItem(NBTTagCompound aNBT) {
        if (aNBT == null) return null;
        ItemStack tRawStack = ItemStack.loadItemStackFromNBT(aNBT);
        int tRealStackSize = 0;
        if (tRawStack != null && aNBT.hasKey("Count", Constants.NBT.TAG_INT)) {
            tRealStackSize = aNBT.getInteger("Count");
            tRawStack.stackSize = tRealStackSize;
        } else if (tRawStack != null) {
            tRealStackSize = tRawStack.stackSize;
        }
        ItemStack tRet = GT_OreDictUnificator.get(true, tRawStack);
        if (tRet != null) tRet.stackSize = tRealStackSize;
        return tRet;
    }

    public static void saveItem(NBTTagCompound aParentTag, String aTagName, ItemStack aStack) {
        if (aStack != null) aParentTag.setTag(aTagName, saveItem(aStack));
    }

    public static NBTTagCompound saveItem(ItemStack aStack) {
        if (aStack == null) return new NBTTagCompound();
        NBTTagCompound t = new NBTTagCompound();
        aStack.writeToNBT(t);
        if (aStack.stackSize > Byte.MAX_VALUE) t.setInteger("Count", aStack.stackSize);
        return t;
    }

    public static void saveFluid(NBTTagCompound aParentTag, String aTagName, FluidStack fluidStack) {
        if (fluidStack != null) {
            aParentTag.setTag(aTagName, saveFluid(fluidStack));
        }
    }

    public static NBTTagCompound saveFluid(FluidStack fluidStack) {
        NBTTagCompound t = new NBTTagCompound();
        if (fluidStack == null) return t;
        fluidStack.writeToNBT(t);
        return t;
    }

}
