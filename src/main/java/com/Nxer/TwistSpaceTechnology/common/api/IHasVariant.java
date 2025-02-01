package com.Nxer.TwistSpaceTechnology.common.api;

import java.util.Set;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Unmodifiable;

public interface IHasVariant {

    /**
     * Create a copy of this with given meta value.
     *
     * @param meta the meta value
     * @return the copy of this with given meta value
     * @throws IllegalArgumentException if the meta is invalid.
     */
    ItemStack getVariant(int meta) throws IllegalArgumentException;

    /**
     * Create an array of copies of this with different meta values.
     *
     * @return the copies of this with different meta values
     */
    ItemStack[] getVariants();

    /**
     * Register a variant of this with given meta value, and return the instance of registered variant item.
     *
     * @param meta the meta value
     * @return the instance of the registered variant item
     * @throws IllegalArgumentException if the meta value is taken.
     */
    ItemStack registerVariant(int meta) throws IllegalArgumentException;

    /**
     * Create a copy of allowed meta IDs.
     *
     * @return a copy of allowed meta IDs.
     */
    @Unmodifiable
    Set<Integer> getVariantIds();

    /**
     * Check if the meta is a valid variant.
     *
     * @param meta the meta value
     * @return {@code true} if valid
     */
    default boolean isValidVariant(int meta) {
        return getVariantIds().contains(meta);
    }

}
