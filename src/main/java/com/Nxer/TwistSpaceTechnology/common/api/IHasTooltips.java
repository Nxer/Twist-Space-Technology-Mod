package com.Nxer.TwistSpaceTechnology.common.api;

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

}
