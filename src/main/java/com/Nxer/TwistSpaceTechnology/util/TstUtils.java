package com.Nxer.TwistSpaceTechnology.util;

import java.util.ArrayList;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.Obsolete;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;
import com.Nxer.TwistSpaceTechnology.common.api.IHasVariant;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.MetaBlockCasingBase;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.MetaBlockBase;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdder_Basic;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;
import com.google.common.collect.Lists;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
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
 * {@link MetaBlockBase} to make sure it implements {@link IHasTooltips}.
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

    /**
     * Create a copy of given ItemStack, and set the meta.
     *
     * @param stack the ItemStack
     * @param meta  the meta value
     * @return a copy of ItemStack of given ItemStack with meta
     */
    public static ItemStack copyItemWithMeta(ItemStack stack, int meta) {
        var copy = stack.copy();
        copy.setItemDamage(meta);
        return copy;
    }

    /**
     * Make an {@link IllegalArgumentException} for the situation that an input argument (Item or Block) is expected to
     * implement an interface but not.
     *
     * @param type            the type of the argument, "Item" or "Block"
     * @param unlocalizedName the unlocalized name of the argument
     * @param objectClass     the argument class
     * @param interfaceClass  the interface class that expected to be implemented
     * @return the exception instance with nice message built up by the arguments
     */
    private static IllegalArgumentException makeNotSupportInterfaceException(String type, String unlocalizedName,
        Class<?> objectClass, Class<?> interfaceClass) {
        return new IllegalArgumentException(
            type + " "
                + tr(unlocalizedName)
                + " ("
                + unlocalizedName
                + ", class = "
                + objectClass.getSimpleName()
                + ") doesn't support "
                + interfaceClass.getSimpleName()
                + ".");
    }

    /**
     * @see #makeNotSupportInterfaceException(String, String, Class, Class)
     */
    private static IllegalArgumentException makeItemNotSupportInterfaceException(Item item, Class<?> interfaceClass) {
        return makeNotSupportInterfaceException("Item", item.unlocalizedName, item.getClass(), interfaceClass);
    }

    /**
     * @see #makeNotSupportInterfaceException(String, String, Class, Class)
     */
    private static IllegalArgumentException makeBlockNotSupportInterfaceException(Block item, Class<?> interfaceClass) {
        return makeNotSupportInterfaceException("Block", item.unlocalizedName, item.getClass(), interfaceClass);
    }

    public static ItemStack newMetaItemStackUnsafe(Item itemMeta, int meta) {
        return newMetaItemStackUnsafe(itemMeta, meta, null);
    }

    public static ItemStack newMetaItemStackUnsafe(Item itemMeta, int meta, @Nullable String[] tooltips)
        throws IllegalArgumentException {
        if (tooltips != null) {
            if (itemMeta instanceof IHasTooltips hasTooltips) {
                hasTooltips.setTooltips(meta, tooltips);
            } else {
                throw makeItemNotSupportInterfaceException(itemMeta, IHasTooltips.class);
            }
        }

        return newItemWithMeta(itemMeta, meta);
    }

    /**
     * Return a copy of the instance of the block with given meta.
     * <p>
     * <b>UNSAFE</b>: It is caller's responsibility to check if the block supports meta values.
     * <p>
     * I'd recommend you to {@code extends} {@link MetaBlockBase} to mark it as meta block.
     *
     * @param blockMeta the meta block
     * @param meta      the meta
     * @return the copy of the instance of the meta block.
     * @see #newMetaBlockItemStack(MetaBlockBase, int)
     * @see #newMetaBlockItemStack(MetaBlockBase, int, String[])
     */
    public static ItemStack newMetaBlockItemStackUnsafe(Block blockMeta, int meta) {
        return newMetaBlockItemStackUnsafe(blockMeta, meta, null);
    }

    /**
     * Register the tooltips of the {@link MetaBlockBase}, and return a copy of the instance of the block.
     * <p>
     * <b>UNSAFE</b>: It is caller's responsibility to check if the block supports meta values and tooltips.
     * <p>
     * I'd recommend you to {@code extends} {@link MetaBlockBase} to mark it as meta block.
     *
     * @param blockMeta the meta block that must implement {@link IHasTooltips}
     * @param meta      the meta
     * @param tooltips  the tooltips (optional)
     * @return the copy of the instance of the meta block.
     * @throws IllegalArgumentException if tooltips is provided, but the block doesn't implement {@link IHasTooltips}.
     * @see #newMetaBlockItemStack(MetaBlockBase, int)
     * @see #newMetaBlockItemStack(MetaBlockBase, int, String[])
     */
    public static ItemStack newMetaBlockItemStackUnsafe(Block blockMeta, int meta, @Nullable String[] tooltips)
        throws IllegalArgumentException {
        if (tooltips != null) if (blockMeta instanceof IHasTooltips hasTooltips) {
            hasTooltips.setTooltips(meta, tooltips);
        } else {
            throw makeBlockNotSupportInterfaceException(blockMeta, IHasTooltips.class);
        }
        return newItemWithMeta(blockMeta, meta);
    }

    /**
     * Return a copy of the instance of the block with given meta.
     *
     * @param blockMeta the meta block
     * @param meta      the meta
     * @return the copy of the instance of the meta block.
     */
    public static ItemStack newMetaBlockItemStack(MetaBlockBase blockMeta, int meta) {
        return newMetaBlockItemStackUnsafe(blockMeta, meta);
    }

    /**
     * Register the tooltips of the {@link MetaBlockBase}, and return a copy of the instance of the block.
     *
     * @param blockMeta the meta block
     * @param meta      the meta
     * @param tooltips  the tooltips (optional)
     * @return the copy of the instance of the meta block.
     */
    public static ItemStack newMetaBlockItemStack(MetaBlockBase blockMeta, int meta, @Nullable String[] tooltips) {
        return newMetaBlockItemStackUnsafe(blockMeta, meta, tooltips);
    }

    /**
     * Register the Texture of the {@link MetaBlockCasingBase}, and return a copy of the instance of the casing.
     *
     * @param blockCasing the casing block
     * @param meta        the meta
     * @return the copy of the instance of the casing.
     */
    public static ItemStack registerCasingBlockItemStack(MetaBlockCasingBase blockCasing, int meta) {
        return registerCasingBlockItemStack(blockCasing, meta, null);
    }

    /**
     * Register the Texture of the {@link MetaBlockCasingBase}, and add the tooltips if present.
     * Return a copy of the instance of the casing.
     *
     * @param blockCasing the casing block
     * @param meta        the meta
     * @param tooltips    the tooltips (optional)
     * @return the copy of the instance of the casing
     */
    public static ItemStack registerCasingBlockItemStack(MetaBlockCasingBase blockCasing, int meta,
        @Nullable String[] tooltips) {
        Textures.BlockIcons
            .setCasingTextureForId(blockCasing.getTextureIndex(meta), TextureFactory.of(blockCasing, meta));
        return newMetaBlockItemStack(blockCasing, meta, tooltips);
    }

    /**
     * Register tooltips with both normal and advanced.
     *
     * @param hasTooltips      the object reference that has tooltips
     * @param meta             the meta value
     * @param normalTooltips   the normal tooltips
     * @param advancedTooltips the advanced tooltips
     */
    public static void registerAdvancedTooltips(IHasTooltips hasTooltips, int meta, String[] normalTooltips,
        String[] advancedTooltips) {
        hasTooltips.setTooltips(meta, normalTooltips, false);
        hasTooltips.setTooltips(meta, advancedTooltips, true);
    }

    public static ItemStack registerVariantMetaItemStackUnsafe(Item itemMeta, int meta) {
        return registerVariantMetaItemStackUnsafe(itemMeta, meta, null);
    }

    public static ItemStack registerVariantMetaItemStackUnsafe(Item itemMeta, int meta, @Nullable String[] tooltips)
        throws IllegalArgumentException {
        ItemStack stack;
        if (itemMeta instanceof IHasVariant hasVariant) {
            stack = hasVariant.registerVariant(meta);
        } else {
            throw makeItemNotSupportInterfaceException(itemMeta, IHasVariant.class);
        }

        if (tooltips != null) {
            if (itemMeta instanceof IHasTooltips hasTooltips) {
                hasTooltips.setTooltips(meta, tooltips);
            } else {
                throw makeItemNotSupportInterfaceException(itemMeta, IHasTooltips.class);
            }
        }

        return stack;
    }

    public static ItemStack registerItemAdder(ItemAdder_Basic itemAdder, int meta) {
        return registerItemAdder(itemAdder, meta, null);
    }

    public static ItemStack registerItemAdder(ItemAdder_Basic itemAdder, int meta, @Nullable String[] tooltips) {
        return registerVariantMetaItemStackUnsafe(itemAdder, meta, tooltips);
    }

    /**
     * Register a variant of a meta item with its tooltips, and return the instance of the variant item.
     * <p>
     * <b>UNSAFE</b>: It is caller's responsibility to check if the item supports variants and tooltips.
     *
     * @param itemMeta         the meta item
     * @param meta             the meta value
     * @param tooltips         the normal tooltips
     * @param advancedTooltips the advanced tooltips
     * @return the instance of the variant item.
     * @throws IllegalArgumentException if the item doesn't implement either {@link IHasVariant} or
     *                                  {@link IHasTooltips}.
     */
    public static ItemStack registerVariantMetaItemStackWithAdvancedTooltipsUnsafe(Item itemMeta, int meta,
        String[] tooltips, String[] advancedTooltips) throws IllegalArgumentException {
        ItemStack stack;
        if (itemMeta instanceof IHasVariant hasVariant) {
            stack = hasVariant.registerVariant(meta);
        } else {
            throw makeItemNotSupportInterfaceException(itemMeta, IHasVariant.class);
        }

        if (itemMeta instanceof IHasTooltips hasTooltips) {
            registerAdvancedTooltips(hasTooltips, meta, tooltips, advancedTooltips);
        } else {
            throw makeItemNotSupportInterfaceException(itemMeta, IHasTooltips.class);
        }

        return stack;
    }

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
    public static String[] buildInfoData(Consumer<ArrayList<String>> builder) {
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
    public static String[] buildInfoData(@Nullable String[] superInfoData, Consumer<ArrayList<String>> builder) {
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
}
