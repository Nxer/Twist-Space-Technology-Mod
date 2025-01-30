package com.Nxer.TwistSpaceTechnology.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.MetaTileEntity;

/**
 * @deprecated see {@link TstUtils}, {@link gregtech.api.util.GTUtility} and other utility classes for better
 *             resolution.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public final class Utils {

    public static final double LOG2 = Math.log(2);
    public static final double LOG4 = Math.log(4);
    public static final BigInteger NEGATIVE_ONE = BigInteger.valueOf(-1);
    public static final BigInteger INTEGER_MAX_VALUE = BigInteger.valueOf(Integer.MAX_VALUE);
    public static final BigInteger BIG_INTEGER_100 = BigInteger.valueOf(100);
    // region about game

    // endregion

    /**
     * Used to create a size of 1 itemStack, a very light method to reduce the "new" keyword and obfuscated fields are
     * displayed。
     */
    @NotNull
    public static ItemStack createItemStack(Item item, int meta) {
        return new ItemStack(item, 1, meta);
    }

    /**
     * Used to create a size of 1 itemStack, a very light method to reduce the "new" keyword and obfuscated fields are
     * displayed。
     */
    @NotNull
    public static ItemStack createItemStack(Block block, int meta) {
        return new ItemStack(block, 1, meta);
    }
    // endregion

    // region About FluidStack

    public static boolean fluidStackEqualAbsolutely(FluidStack[] fsa1, FluidStack[] fsa2) {
        if (fsa1.length != fsa2.length) return false;
        for (int i = 0; i < fsa1.length; i++) {
            if (!fluidEqual(fsa1[i], fsa2[i])) return false;
            if (fsa1[i].amount != fsa2[i].amount) return false;
        }
        return true;
    }

    public static boolean fluidStackEqualFuzzy(FluidStack[] fsa1, FluidStack[] fsa2) {
        if (fsa1.length != fsa2.length) return false;
        for (FluidStack fluidStack1 : fsa1) {
            boolean flag = false;
            for (FluidStack fluidStack2 : fsa2) {
                if (fluidEqual(fluidStack1, fluidStack2)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) return false;
        }
        return true;
    }

    public static boolean fluidEqual(FluidStack a, FluidStack b) {
        return a.getFluid() == b.getFluid();
    }

    // endregion

    /**
     * Send a string to player's chat area directly.
     */
    public static void sendTextToPlayer(EntityPlayer player, String text) {
        if (player instanceof EntityPlayerMP && text != null) {
            player.addChatComponentMessage(new ChatComponentText(text));
        }
    }

    /**
     * Send localized message to player.
     *
     * @param player         the player
     * @param translationKey the message's localization key
     */
    public static void sendMessageToPlayerLocalized(EntityPlayer player, String translationKey) {
        if (player instanceof EntityPlayerMP && translationKey != null) {
            player.addChatComponentMessage(new ChatComponentTranslation(translationKey));
        }
    }

    /**
     * Send localized message to player.
     *
     * @param player         the player
     * @param translationKey the message's localization key
     * @param formatArgs     extra arguments
     */
    public static void sendMessageToPlayerLocalized(EntityPlayer player, String translationKey, Object... formatArgs) {
        if (player instanceof EntityPlayerMP && translationKey != null) {
            player.addChatComponentMessage(new ChatComponentTranslation(translationKey, formatArgs));
        }
    }

    // endregion

    // region Rewrites

    public static <T extends Collection<?>> T filterValidMTE(T metaTileEntities) {
        metaTileEntities.removeIf(o -> {
            if (o == null) {
                return true;
            }
            if (o instanceof MetaTileEntity mte) {
                return !mte.isValid();
            }
            return false;
        });
        return metaTileEntities;
    }

    // endregion

    // region Generals

    public static <T> T withNull(T main, T instead) {
        return null == main ? instead : main;
    }

    public static int safeInt(long number, int margin) {
        return number > Integer.MAX_VALUE - margin ? Integer.MAX_VALUE - margin : (int) number;
    }

    public static ItemStack[] sortNoNullArray(ItemStack... itemStacks) {
        if (itemStacks == null) return null;
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] == null) continue;
            list.add(itemStacks[i]);
        }
        if (list.isEmpty()) return new ItemStack[0];
        return list.toArray(new ItemStack[0]);
    }

    public static FluidStack[] sortNoNullArray(FluidStack... fluidStacks) {
        if (fluidStacks == null) return null;
        List<FluidStack> list = new ArrayList<>();
        for (int i = 0; i < fluidStacks.length; i++) {
            if (fluidStacks[i] == null) continue;
            list.add(fluidStacks[i]);
        }
        if (list.isEmpty()) return new FluidStack[0];
        return list.toArray(new FluidStack[0]);
    }

    public static Object[] sortNoNullArray(Object... objects) {
        if (objects == null) return null;
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) continue;
            list.add(objects[i]);
        }
        if (list.isEmpty()) return new Object[0];
        return list.toArray(new Object[0]);
    }

    public static <T extends Collection<E>, E extends MetaTileEntity> T filterValidMTEs(T metaTileEntities) {
        metaTileEntities.removeIf(mte -> mte == null || !mte.isValid());
        return metaTileEntities;
    }

    public static int min(int... values) {
        Arrays.sort(values);
        return values[0];
    }

    public static int max(int... values) {
        Arrays.sort(values);
        return values[values.length - 1];
    }

    public static long min(long... values) {
        Arrays.sort(values);
        return values[0];
    }

    public static long max(long... values) {
        Arrays.sort(values);
        return values[values.length - 1];
    }

    public static double calculatePowerTier(double voltage) {
        return 1 + Math.max(0, (Math.log(voltage) / LOG2) - 5) / 2;
    }
}
