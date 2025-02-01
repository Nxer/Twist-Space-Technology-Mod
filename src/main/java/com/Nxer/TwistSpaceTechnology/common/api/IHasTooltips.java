package com.Nxer.TwistSpaceTechnology.common.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Blocks and Items with meta/damage related tooltips should implement this interface.
 */
public interface IHasTooltips {

    /**
     * Set or clear the tooltips of given meta.
     *
     * @param metaValue the meta value
     * @param tooltips  the tooltips to set, {@code null} to clear.
     */
    void setTooltips(int metaValue, @Nullable String[] tooltips);

    /**
     * Get the tooltips of given meta.
     *
     * @param metaValue the meta value
     * @return the tooltips, or {@code null} if not set.
     */
    @Nullable
    String[] getTooltips(int metaValue);

    /**
     * Set or clear the tooltips of given meta.
     *
     * @param metaValue    the meta value
     * @param tooltips     the tooltips to set, {@code null} to clear.
     * @param advancedMode true if shift is pressed
     */
    default void setTooltips(int metaValue, @Nullable String[] tooltips, boolean advancedMode) {
        setTooltips(metaValue, tooltips);
    }

    /**
     * Get the tooltips of given meta.
     *
     * @param metaValue    the meta value
     * @param advancedMode true if shift is pressed
     * @return the tooltips, or {@code null} if not set.
     */
    @Nullable
    default String[] getTooltips(int metaValue, boolean advancedMode) {
        return getTooltips(metaValue);
    }

    /**
     * A variant of {@link IHasTooltips}, which it supports advanced tooltips.
     * <p>
     * Must implement {@link #setTooltips(int, String[], boolean)} and {@link #getTooltips(int, boolean)}, and not
     * implement
     * {@link #setTooltips(int, String[])} and {@link #getTooltips(int)} as they are implemented by default.
     *
     * @see IHasTooltips
     */
    interface Advanced extends IHasTooltips {

        @ApiStatus.NonExtendable
        @Override
        default void setTooltips(int metaValue, @Nullable String[] tooltips) {
            setTooltips(metaValue, tooltips, false);
        }

        @ApiStatus.NonExtendable
        @Override
        @Nullable
        default String[] getTooltips(int metaValue) {
            return getTooltips(metaValue, false);
        }

        @Override
        default void setTooltips(int metaValue, @Nullable String[] tooltips, boolean advancedMode) {
            throw new AssertionError(
                "The author must forget to implement IHasTooltips#setTooltips(int, String, boolean), report!");
        }

        @Override
        @Nullable
        default String[] getTooltips(int metaValue, boolean advancedMode) {
            throw new AssertionError(
                "The author must forget to implement IHasTooltips#getTooltips(int, String, boolean), report!");
        }
    }

}
