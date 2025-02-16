package com.Nxer.TwistSpaceTechnology.common.api;

import org.jetbrains.annotations.Nullable;

/**
 * Blocks and Items with meta/damage related tooltips should implement this interface.
 */
public interface IHasTooltips {

    /**
     * Set or clear the tooltips of given meta.
     *
     * @param metaValue    the meta value
     * @param tooltips     the tooltips to set, {@code null} to clear.
     * @param advancedMode true if shift is pressed
     */
    void setTooltips(int metaValue, @Nullable String[] tooltips, boolean advancedMode);

    /**
     * Get the tooltips of given meta.
     *
     * @param metaValue    the meta value
     * @param advancedMode true if shift is pressed
     * @return the tooltips, or {@code null} if not set.
     */
    @Nullable
    String[] getTooltips(int metaValue, boolean advancedMode);

}
