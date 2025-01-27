package com.Nxer.TwistSpaceTechnology.util;

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
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.MetaBlockCasingBase;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.MetaBlockBase;

import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;

/**
 * The Twist-Space-Technology Utilities.
 * <p>
 * Methods in this class with {@code public} modifiers should all be detailedly documented.
 * <p>
 * And make sure to use Jetbrains Annotations ({@link org.jetbrains.annotations}) to make it clear of
 * <ul>
 * <li>
 * which arguments or function returns are {@link Nullable}. <br>
 * By default, the arguments and function returns are {@link NotNull}.
 * </li>
 * <li>which function returns are {@link Unmodifiable} (or {@link UnmodifiableView}) collections.</li>
 * <li>
 * which functions are {@link Experimental}, {@link Internal}, {@link ScheduledForRemoval} and others in
 * {@link ApiStatus};<br>
 * <i>(Obsolete vs Deprecated)</i> if a function is not recommended in future usage, but allowed for legacy codes, use
 * {@link Obsolete}, otherwise use {@link Deprecated}.
 * </li>
 * </ul>
 * <p>
 * <a href="https://www.jetbrains.com/help/idea/annotating-source-code.html">Document for Jetbrains Annotations</a>
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
    public static ItemStack withMeta(Item item, int meta) {
        return new ItemStack(item, meta);
    }

    /**
     * Create a new {@link ItemStack} of given Block with meta.
     *
     * @param block the Block
     * @param meta  the meta value
     * @return a new ItemStack of given Item with meta
     */
    public static ItemStack withMeta(Block block, int meta) {
        return new ItemStack(block, meta);
    }

    /**
     * Create a copy of given ItemStack, and set the meta.
     *
     * @param stack the ItemStack
     * @param meta  the meta value
     * @return a copy of ItemStack of given ItemStack with meta
     */
    public static ItemStack withMeta(ItemStack stack, int meta) {
        var copy = stack.copy();
        copy.setItemDamage(meta);
        return copy;
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
            throw new IllegalArgumentException(
                "Block " + blockMeta.getUnlocalizedName()
                    + " ("
                    + blockMeta.getClass()
                        .getSimpleName()
                    + ") doesn't support IHasTooltips.");
        }
        return withMeta(blockMeta, meta);
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
    public static ItemStack newCasingBlockItemStack(MetaBlockCasingBase blockCasing, int meta) {
        return newCasingBlockItemStack(blockCasing, meta, null);
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
    public static ItemStack newCasingBlockItemStack(MetaBlockCasingBase blockCasing, int meta,
        @Nullable String[] tooltips) {
        Textures.BlockIcons
            .setCasingTextureForId(blockCasing.getTextureIndex(meta), TextureFactory.of(blockCasing, meta));
        return newMetaBlockItemStack(blockCasing, meta, tooltips);
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

}
