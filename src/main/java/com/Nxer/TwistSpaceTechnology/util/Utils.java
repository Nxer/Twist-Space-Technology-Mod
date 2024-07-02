package com.Nxer.TwistSpaceTechnology.util;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.isInDevMode;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.metatileentity.MetaTileEntity;
import scala.actors.migration.pattern;

public final class Utils {

    public static final double LOG2 = Math.log(2);
    public static final double LOG4 = Math.log(4);
    public static final BigInteger NEGATIVE_ONE = BigInteger.valueOf(-1);

    // region about game

    /**
     * LV = 1, MAX = 14
     */
    public static int getCoilTier(HeatingCoilLevel coilLevel) {
        return coilLevel.getTier() + 1;
    }

    public static int multiBuildPiece(int... buildPieces) {
        int out = 0x80000000;
        for (int v : buildPieces) {
            out &= (v & 0x80000000) | 0x7fffffff;
            if (v != -1) out += v;
        }
        return out < 0 ? -1 : out;
    }

    // endregion

    // region about ItemStack
    public static boolean metaItemEqual(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (a == b) return true;
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage();
    }

    public static ItemStack[] copyItemStackArray(ItemStack... array) {
        ItemStack[] result = new ItemStack[array.length];
        for (int i = 0; i < result.length; i++) {
            if (array[i] == null) continue;
            result[i] = array[i].copy();
        }
        return result;
    }

    public static ItemStack[] mergeItemStackArray(ItemStack[] array1, ItemStack[] array2) {
        if (array1 == null || array1.length < 1) {
            return array2;
        }
        if (array2 == null || array2.length < 1) {
            return array1;
        }
        ItemStack[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static ItemStack[] mergeItemStackArrays(ItemStack[]... itemStacks) {
        return Arrays.stream(itemStacks)
            .filter(Objects::nonNull)
            .flatMap(Arrays::stream)
            .toArray(ItemStack[]::new);
    }

    public static <T> T[] mergeArray(T[] array1, T[] array2) {
        if (array1 == null || array1.length < 1) {
            return array2;
        }
        if (array2 == null || array2.length < 1) {
            return array1;
        }
        T[] newArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }

    public static <T> T[] mergeArrayss(/* @NotNull IntFunction<T[]> generator, */T[]... arrays) {
        IntFunction<T[]> generator = null;
        for (T[] array : arrays) {
            if (array == null) continue;
            generator = size -> (T[]) Array.newInstance(
                array.getClass()
                    .getComponentType(),
                size);
            break;
        }
        if (generator == null) return null;

        return Arrays.stream(arrays)
            .filter(a -> a != null && a.length > 0)
            .flatMap(Arrays::stream)
            .toArray(generator);
    }

    public static <T> T[] mergeArrays(T[]... arrays) {
        int totalLength = 0;
        T[] pattern = null;
        int indexFirstNotNull = -1;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] == null || arrays[i].length < 1) continue;
            totalLength += arrays[i].length;
            if (pattern == null) {
                pattern = arrays[i];
                indexFirstNotNull = i;
            }
        }

        if (pattern == null) return null;

        T[] output = Arrays.copyOf(pattern, totalLength);
        int offset = pattern.length;
        for (int i = indexFirstNotNull; i < arrays.length; i++) {
            if (arrays[i] == null || arrays[i].length < 1) continue;
            if (arrays[i] != pattern) {
                System.arraycopy(arrays[i], 0, output, offset, arrays[i].length);
                offset += arrays[i].length;
            }
        }
        return output;
    }

    /**
     *
     * @param isa1 The ItemStack Array 1.
     * @param isa2 The ItemStack Array 2.
     * @return The elements of these two arrays are identical and in the same order.
     */
    public static boolean itemStackArrayEqualAbsolutely(ItemStack[] isa1, ItemStack[] isa2) {
        if (isa1.length != isa2.length) return false;
        for (int i = 0; i < isa1.length; i++) {
            if (!metaItemEqual(isa1[i], isa2[i])) return false;
            if (isa1[i].stackSize != isa2[i].stackSize) return false;
        }
        return true;
    }

    public static boolean itemStackArrayEqualFuzzy(ItemStack[] isa1, ItemStack[] isa2) {
        if (isa1.length != isa2.length) return false;
        for (ItemStack itemStack1 : isa1) {
            boolean flag = false;
            for (ItemStack itemStack2 : isa2) {
                if (metaItemEqual(itemStack1, itemStack2)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) return false;
        }
        return true;
    }

    public static ItemStack copyAmount(int aAmount, ItemStack aStack) {
        ItemStack rStack = aStack.copy();
        if (isStackInvalid(rStack)) return null;
        // if (aAmount > 64) aAmount = 64;
        else if (aAmount == -1) aAmount = 111;
        else if (aAmount < 0) aAmount = 0;
        rStack.stackSize = aAmount;
        return rStack;
    }

    public static boolean isStackValid(ItemStack aStack) {
        return (aStack != null) && aStack.getItem() != null && aStack.stackSize >= 0;
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

    public static FluidStack setStackSize(FluidStack fluidStack, int amount) {
        if (fluidStack == null) return null;
        if (amount < 0) {
            TwistSpaceTechnology.LOG
                .info("Error! Trying to set a item stack size lower than zero! " + fluidStack + " to amount " + amount);
            return fluidStack;
        }
        fluidStack.amount = amount;
        return fluidStack;
    }

    // endregion

    // region About Text
    public static String i18n(String key) {
        return translateToLocalFormatted(key);
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

    public static void debugLogInfo(String... strings) {
        if (isInDevMode) {
            for (String msg : strings) {
                TwistSpaceTechnology.LOG.info(msg);
            }
        }
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
