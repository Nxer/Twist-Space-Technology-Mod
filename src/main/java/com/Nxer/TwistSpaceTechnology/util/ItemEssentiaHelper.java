package com.Nxer.TwistSpaceTechnology.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.ItemCrystalEssence;
import thaumicenergistics.common.fluids.GaseousEssentia;
import thaumicenergistics.common.integration.tc.EssentiaConversionHelper;

public class ItemEssentiaHelper {

    @Nullable
    public static IAEFluidStack getAeFluidStack(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemCrystalEssence)) {
            return null;
        }
        AspectList aspects = null;
        try {
            aspects = ((ItemCrystalEssence) stack.getItem()).getAspects(stack);
        } catch (Throwable t) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) return null;
            aspects = new AspectList();
            aspects.readFromNBT(tag);
        }
        if (aspects == null || aspects.size() == 0) {
            return null;
        }
        Aspect asp = aspects.getAspects()[0];
        GaseousEssentia gas = GaseousEssentia.getGasFromAspect(asp);
        if (gas == null) return null;

        int fluidPerEssentia = (int) EssentiaConversionHelper.INSTANCE.convertEssentiaAmountToFluidAmount(1);
        if (fluidPerEssentia <= 0) return null;
        long totalFluidLong = (long) fluidPerEssentia * (long) stack.stackSize;
        int totalFluid = totalFluidLong > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalFluidLong;

        return AEFluidStack.create(new net.minecraftforge.fluids.FluidStack(gas, totalFluid));
    }

    @Nullable
    public static IAEItemStack newAeStack(IAEFluidStack fluid) {
        if (fluid == null || fluid.getFluid() == null || !(fluid.getFluid() instanceof GaseousEssentia)) {
            return null;
        }

        GaseousEssentia gas = (GaseousEssentia) fluid.getFluid();
        Aspect asp = gas.getAspect();

        ItemStack crystal = new ItemStack(thaumcraft.common.config.ConfigItems.itemCrystalEssence, 1);
        AspectList list = new AspectList().add(asp, 1);
        NBTTagCompound tag = new NBTTagCompound();
        list.writeToNBT(tag);
        // tag.setString("AspectTag", asp.getTag());
        crystal.setTagCompound(tag);

        int fluidPerEssentia = (int) EssentiaConversionHelper.INSTANCE.convertEssentiaAmountToFluidAmount(1);
        if (fluidPerEssentia <= 0) {
            IAEItemStack aeStackFallback = AEItemStack.create(crystal);
            aeStackFallback.setStackSize(1);
            return aeStackFallback;
        }

        if (fluid.getStackSize() >= Integer.MAX_VALUE || fluid.getStackSize() <= 0) {
            IAEItemStack aeStack = AEItemStack.create(crystal);
            aeStack.setStackSize(Integer.MAX_VALUE);
            return aeStack;
        }
        int stackSize = (int) Math.max(1, fluid.getStackSize() / fluidPerEssentia);

        IAEItemStack aeStack = AEItemStack.create(crystal);
        aeStack.setStackSize(stackSize);
        return aeStack;
    }

    @Nullable
    public static IAEFluidStack getAeFluidStack(@Nullable IAEItemStack stack) {
        if (stack == null) {
            return null;
        }

        ItemStack mcStack = stack.getItemStack();
        if (mcStack == null || !(mcStack.getItem() instanceof ItemCrystalEssence)) {
            return null;
        }
        AspectList aspects = null;
        try {
            aspects = ((ItemCrystalEssence) mcStack.getItem()).getAspects(mcStack);
        } catch (Throwable t) {
            NBTTagCompound tag = mcStack.getTagCompound();
            if (tag == null) return null;
            aspects = new AspectList();
            aspects.readFromNBT(tag);
        }

        if (aspects == null || aspects.size() == 0) {
            return null;
        }
        Aspect asp = aspects.getAspects()[0];
        GaseousEssentia gas = GaseousEssentia.getGasFromAspect(asp);
        if (gas == null) return null;
        int fluidPerEssentia = (int) EssentiaConversionHelper.INSTANCE.convertEssentiaAmountToFluidAmount(1);
        if (fluidPerEssentia <= 0) return null;

        long totalFluidLong = (long) fluidPerEssentia * (long) stack.getStackSize();
        int totalFluid = totalFluidLong > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalFluidLong;

        return AEFluidStack.create(new net.minecraftforge.fluids.FluidStack(gas, totalFluid));
    }

    @Nullable
    public static Aspect readAspectFromCrystal(@Nullable ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) {
            return null;
        }

        if (stack.getItem() instanceof thaumcraft.common.items.ItemCrystalEssence) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                AspectList aspects = new AspectList();
                aspects.readFromNBT(tag);
                if (aspects.size() > 0) {
                    return aspects.getAspects()[0];
                }
            }
        }

        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag.hasKey("aspect")) {
                Aspect aspect = Aspect.getAspect(tag.getString("aspect"));
                if (aspect != null) return aspect;
            }
            if (tag.hasKey("Aspects")) {
                AspectList list = new AspectList();
                list.readFromNBT(tag.getCompoundTag("Aspects"));
                if (list.size() > 0) return list.getAspects()[0];
            }
        }

        return null;
    }

    public static ItemStack createCrystal(Aspect asp) {
        ItemStack stack = new ItemStack(thaumcraft.common.config.ConfigItems.itemCrystalEssence, 1);
        AspectList aspects = new AspectList().add(asp, 1);
        NBTTagCompound tag = new NBTTagCompound();
        aspects.writeToNBT(tag);
        stack.setTagCompound(tag);
        return stack;
    }

    public static ItemStack createCrystal(Aspect asp, int count) {
        ItemStack stack = createCrystal(asp);
        stack.stackSize = count;
        return stack;
    }

    /**
     * Based on two input Aspect, return the target Aspect that can be synthesized (if it exists).
     * 
     * @param aspectA The first input factor
     * @param aspectB The second input factor
     * @return The synthesized result ; if they do not exist, return null
     */
    @Nullable
    public static Aspect findCombinedAspect(Aspect aspectA, Aspect aspectB) {
        for (Aspect candidate : Aspect.aspects.values()) {
            Aspect[] comps = candidate.getComponents();
            if (comps != null && comps.length == 2) {
                if ((comps[0] == aspectA && comps[1] == aspectB) || (comps[0] == aspectB && comps[1] == aspectA)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    public static final Map<Aspect, Map<Aspect, Aspect>> COMBO_CACHE = new HashMap<>();

    // Use cache acceleration for searching, but I'm not sure if it's worth it.
    @Nullable
    public static Aspect findCombinedAspectCached(Aspect a, Aspect b) {
        Map<Aspect, Aspect> inner = COMBO_CACHE.get(a);
        if (inner != null && inner.containsKey(b)) {
            return inner.get(b);
        }
        inner = COMBO_CACHE.get(b);
        if (inner != null && inner.containsKey(a)) {
            return inner.get(a);
        }
        Aspect result = findCombinedAspect(a, b);
        if (result != null) {
            COMBO_CACHE.computeIfAbsent(a, k -> new HashMap<>())
                .put(b, result);
            COMBO_CACHE.computeIfAbsent(b, k -> new HashMap<>())
                .put(a, result);
        }

        return result;
    }

}
