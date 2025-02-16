package com.Nxer.TwistSpaceTechnology.common.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

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

    /**
     * @return true if either left or right Shift key is down.
     */
    @SideOnly(Side.CLIENT)
    static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

}
