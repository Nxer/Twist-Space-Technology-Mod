package com.Nxer.TwistSpaceTechnology.util;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.Obsolete;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import com.Nxer.TwistSpaceTechnology.common.api.IHasVariantAndTooltips;
import com.Nxer.TwistSpaceTechnology.common.block.meta.AbstractTstMetaBlock;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;
import com.google.common.collect.Lists;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;

/**
 * <h1>The Twist-Space-Technology Utilities.</h1>
 * <p>
 * <h2>Naming Conventions</h2>
 * <ul>
 * <li>
 * <b>(Side Effects Prefixes)</b> Functions have side effects should be different from ones without side effects.<br>
 * For example, {@code newMyObject} can only create an instance of {@code MyObject}, but {@code registerMyObject} can do
 * other register stuffs.
 * </li>
 * <li>
 * <b>(Unsafe Suffixes)</b> Functions throw with illegal or invalid input arguments should be named with suffix
 * {@code Unsafe}.<br>
 * For example, {@code newVariantItemUnsafe} that accepts an {@code Item} but requires it to implement
 * {@code IHasVariant}.<br>
 * But since it is very unsafe to do so, we can add more functions with more exact types, like using
 * {@link AbstractTstMetaBlock} to make sure it implements {@link IHasVariantAndTooltips}.
 * </li>
 * <li>
 * <b>(Legacy Names)</b> Functions moved from other places are allowed to be named as before, like {@link #tr(String)}.
 * </li>
 * </ul>
 * <p>
 * <h2>Coding Recommendation</h2>
 * <ul>
 * <li><b>(JavaDoc)</b> Methods in this class with {@code public} modifiers should all be detailedly documented.
 * </li>
 * <li>
 * <b>(Safety Annotations)</b> And make sure to use Jetbrains Annotations ({@link org.jetbrains.annotations}) to make it
 * clear of
 * <ul>
 * <li>
 * which arguments or function returns are {@link Nullable}. <br>
 * By default, the arguments and function returns are {@link NotNull}.
 * </li>
 * <li>which function returns are {@link Unmodifiable} (or {@link UnmodifiableView}) collections.</li>
 * <li>
 * which functions are {@link Experimental}, {@link Internal}, {@link ScheduledForRemoval} and others in
 * {@link ApiStatus};<br>
 * <b>(Obsolete vs Deprecated)</b> if a function is not recommended in future usage, but allowed for legacy codes, use
 * {@link Obsolete}, otherwise use {@link Deprecated}.
 * </li>
 * </ul>
 * <a href="https://www.jetbrains.com/help/idea/annotating-source-code.html">Document for Jetbrains Annotations</a>
 * </li>
 * </ul>
 * <p>
 *
 * @since 0.6.4
 */
@SuppressWarnings("unused")
public class TstUtils {

    public static final double LOG2 = Math.log(2);
    public static final double LOG4 = Math.log(4);

    public static final BigInteger NEGATIVE_ONE = BigInteger.valueOf(-1);
    public static final BigInteger INTEGER_MAX_VALUE = BigInteger.valueOf(Integer.MAX_VALUE);

    // region Instance Creator

    /**
     * Create a new {@link ItemStack} of given Item with meta.
     *
     * @param item the Item
     * @param meta the meta value
     * @return a new ItemStack of given Item with meta
     */
    public static ItemStack newItemWithMeta(Item item, int meta) {
        return new ItemStack(item, 1, meta);
    }

    /**
     * Create a new {@link ItemStack} of given Block with meta.
     *
     * @param block the Block
     * @param meta  the meta value
     * @return a new ItemStack of given Item with meta
     */
    public static ItemStack newItemWithMeta(Block block, int meta) {
        return new ItemStack(block, 1, meta);
    }

    // endregion

    /**
     * Localize by key and given formats.
     * If the key does not exist in both of the currently used language and fallback language (English), the key itself
     * is returned.
     *
     * @param key    the localization key
     * @param format the formats
     * @return the localized text by the key and formats, or key if the key does not exist.
     * @see StatCollector#translateToLocalFormatted(String, Object...)
     */
    public static String tr(String key, Object... format) {
        return StatCollector.translateToLocalFormatted(key, format);
    }

    /**
     * Localize by the key.
     * If the key does not exist in both of the currently used language and fallback language (English), the key itself
     * is returned.
     *
     * @param key the localization key
     * @return the localized text by the key, or key if the key does not exist.
     * @see StatCollector#translateToLocal(String)
     */
    public static String tr(String key) {
        return StatCollector.translateToLocal(key);
    }

    /**
     * Build the machine info data array.
     *
     * @param builder the builder that puts info into the list
     * @return the built array of info data
     */
    public static String[] buildInfoData(Consumer<ArrayList<@NotNull String>> builder) {
        return buildInfoData(null, builder);
    }

    /**
     * Build the machine info data array.
     *
     * @param superInfoData the info data array from parent
     * @param builder       the builder that puts info into the list
     * @return the built array of info data
     * @see TST_BloodyHell#getInfoData() example
     */
    public static String[] buildInfoData(@NotNull String @Nullable [] superInfoData,
        Consumer<ArrayList<@NotNull String>> builder) {
        ArrayList<String> ret = superInfoData != null ? Lists.newArrayList(superInfoData) : new ArrayList<>();
        builder.accept(ret);
        return ret.toArray(new String[0]);
    }

    /**
     * Register the texture to GregTech {@link gregtech.api.enums.Textures.BlockIcons}.
     *
     * @param page    the texture pages index
     * @param index   the texture index of the page
     * @param texture the texture
     */
    public static void registerTexture(int page, int index, ITexture texture) {
        GTUtility.addTexturePage((byte) page);
        Textures.BlockIcons.setCasingTexture((byte) page, (byte) index, texture);
    }

    /**
     * Get the global texture index of given page and index.
     *
     * @param page  the page index where the texture is
     * @param index the index of the page where the texture is
     * @return the calculated global texture index
     */
    public static int getTextureIndex(int page, int index) {
        return 128 * page + index;
    }

    /**
     * Return the voltage tier of given Coil.
     * <p>
     * LV = 1, MAX = 14.
     *
     * @return the voltage index of the coil.
     */
    @MagicConstant(valuesFromClass = VoltageIndex.class)
    public static int getVoltageForCoil(HeatingCoilLevel coilLevel) {
        // noinspection MagicConstant
        return coilLevel.getTier() + 1;
    }

    /**
     * One method to handle multi survivialBuildPiece at once.
     *
     * @param buildPieces All result of `survivialBuildPiece`.
     * @return If all result is -1, return -1. Otherwise, return the sum of all non-negative values.
     * @deprecated replace with {@link #getBuiltBlockCount(int...)}
     */
    @Deprecated
    public static int multiBuildPiece(int... buildPieces) {
        int out = 0x80000000;
        for (int v : buildPieces) {
            out &= (v & 0x80000000) | 0x7fffffff;
            if (v != -1) out += v;
        }
        return out < 0 ? -1 : out;
    }

    /**
     * Calculate the total built block count.
     *
     * @param builtBlockCountEachPart the built block count on each part
     * @return the total built block count
     */
    public static int getBuiltBlockCount(int... builtBlockCountEachPart) {
        var count = Arrays.stream(builtBlockCountEachPart)
            .filter(x -> x > 0)
            .sum();
        return count <= 0 ? -1 : count;
    }

    /**
     * Check if all ItemStacks in the given array are valid.
     *
     * @param stacks the array of stacks
     * @return `true` if all ItemStacks are valid
     */
    public static boolean areItemsValid(ItemStack... stacks) {
        if (stacks == null || stacks.length < 1) return false;
        return Arrays.stream(stacks)
            .allMatch(GTUtility::isStackValid);
    }

    /**
     * Return a shallow copy list of the array where the {@code null} elements are removed.
     *
     * @param array the array
     * @param <T>   the type of elements
     * @return the array copy where the {@code null} elements are removed.
     */
    public static <T> List<T> toNonNullList(@Nullable T @NotNull [] array) {
        return Arrays.stream(array)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * Return a shallow copy of the array where the {@code null} elements are removed.
     * <p>
     * Recommend to use {@link #toNonNullList(Object[])} if possible.
     *
     * @param array the array
     * @param <T>   the type of elements
     * @return the array copy where the {@code null} elements are removed.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toNonNullArray(@Nullable T @NotNull [] array, Class<T> tClass) {
        return Arrays.stream(array)
            .filter(Objects::nonNull)
            .toArray(size -> (T[]) Array.newInstance(tClass, size));
    }

    /**
     * Return a shallow copy of the array where the {@code null} elements are removed.
     *
     * @param array the array
     * @return the array copy where the {@code null} elements are removed.
     */
    @Contract("null -> null; !null -> !null")
    public static ItemStack[] toNonNullItemStackArray(@Nullable ItemStack @Nullable [] array) {
        return array != null ? toNonNullArray(array, ItemStack.class) : null;
    }

    /**
     * Return a shallow copy of the array where the {@code null} elements are removed.
     *
     * @param array the array
     * @return the array copy where the {@code null} elements are removed.
     */
    @Contract("null -> null; !null -> !null")
    public static FluidStack[] toNonNullFluidStackArray(@Nullable FluidStack @Nullable [] array) {
        return array != null ? toNonNullArray(array, FluidStack.class) : null;
    }

    /**
     * Calculate the voltage tier in double (not Integer!)
     * For example, {@code 8190} EU/t will return a double around {@code 4.9}.
     *
     * @param voltage the voltage
     * @return the voltage tier in double
     */
    public static double calculateVoltageTier(double voltage) {
        return 1 + Math.max(0, (Math.log(voltage) / LOG2) - 5) / 2;
    }

    /**
     * Set the stacksize of the given ItemStack, and return the given stack.
     * The 64 limit is unchecked, be aware.
     *
     * @param itemStack the given stack
     * @param size      the size to set
     * @return the given stack
     */
    public static ItemStack setStackSize(@NotNull ItemStack itemStack, int size) {
        itemStack.stackSize = size;
        return itemStack;
    }

    /**
     * Set the stacksize of the given FluidStack, and return the given stack.
     *
     * @param fluidStack the given stack
     * @param size       the size to set
     * @return the given stack
     */
    public static FluidStack setStackSize(@NotNull FluidStack fluidStack, int size) {
        fluidStack.amount = size;
        return fluidStack;
    }

    /**
     * Copy the itemstack array and filter Integrated Circuits out.
     *
     * @param itemStacks the itemstack array
     * @return the copy of the given array without Integrated Circuits.
     */
    public static ItemStack[] removeIntegratedCircuitFromStacks(ItemStack @Nullable [] itemStacks) {
        if (itemStacks == null) return new ItemStack[0];
        ArrayList<ItemStack> newStack = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack.getItem() != ItemList.Circuit_Integrated.getItem()) {
                newStack.add(itemStack);
            }
        }
        return newStack.toArray(new ItemStack[0]);
    }

    /**
     * Like structure definition, select a character from the structure definition string array as the target to place
     * blocks in the world, with the machine facing the XZ direction.
     *
     * @param aBaseMetaTileEntity the machine
     * @param OffSetX             horizontalOffSet of the machine structure definition
     * @param OffSetY             verticalOffSet of the machine structure definition
     * @param OffSetZ             depthOffSet of the machine structure definition
     * @param StructureString     the machine structure definition string array
     * @param isStructureFlipped  if the machine is flipped, use getFlip().isHorizontallyFlipped() to get it
     * @param TargetString        target character
     * @param TargetBlock         target block
     * @param TargetMeta          target block meta
     */
    public static void setStringBlockXZ(IGregTechTileEntity aBaseMetaTileEntity, int OffSetX, int OffSetY, int OffSetZ,
        String[][] StructureString, boolean isStructureFlipped, String TargetString, Block TargetBlock,
        int TargetMeta) {
        int mDirectionX = aBaseMetaTileEntity.getFrontFacing().offsetX;
        int mDirectionZ = aBaseMetaTileEntity.getFrontFacing().offsetZ;
        int xDir = 0;
        int zDir = 0;
        if (mDirectionX == 1) {
            // EAST
            xDir = 1;
            zDir = 1;
        } else if (mDirectionX == -1) {
            // WEST
            xDir = -1;
            zDir = -1;
        }
        if (mDirectionZ == 1) {
            // SOUTH
            xDir = -1;
            zDir = 1;
        } else if (mDirectionZ == -1) {
            // NORTH
            xDir = 1;
            zDir = -1;
        }
        int LengthX = StructureString[0][0].length();
        int LengthY = StructureString.length;
        int LengthZ = StructureString[0].length;
        for (int x = 0; x < LengthX; x++) {
            for (int z = 0; z < LengthZ; z++) {
                for (int y = 0; y < LengthY; y++) {
                    String ListStr = String.valueOf(StructureString[y][z].charAt(x));
                    if (!Objects.equals(ListStr, TargetString)) continue;

                    int aX = (OffSetX - x) * xDir;
                    int aY = OffSetY - y;
                    int aZ = (OffSetZ - z) * zDir;
                    if (mDirectionX == 1 || mDirectionX == -1) {
                        int temp = aX;
                        aX = aZ;
                        aZ = temp;
                    }
                    if (isStructureFlipped) aX = -aX;

                    aBaseMetaTileEntity.getWorld()
                        .setBlock(
                            aBaseMetaTileEntity.getXCoord() + aX,
                            aBaseMetaTileEntity.getYCoord() + aY,
                            aBaseMetaTileEntity.getZCoord() + aZ,
                            TargetBlock,
                            TargetMeta,
                            3);
                }
            }
        }
    }

    public static void setStringBlockXZ(IGregTechTileEntity aBaseMetaTileEntity, int OffSetX, int OffSetY, int OffSetZ,
        String[][] StructureString, boolean isStructureFlipped, String TargetString, Block TargetBlock) {
        setStringBlockXZ(
            aBaseMetaTileEntity,
            OffSetX,
            OffSetY,
            OffSetZ,
            StructureString,
            isStructureFlipped,
            TargetString,
            TargetBlock,
            0);
    }

}
