package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.util.EnumChatFormatting;

import org.intellij.lang.annotations.MagicConstant;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.util.GTUtility;

public class TextWithColor {

    private static EnumChatFormatting DefaultColor = EnumChatFormatting.RESET;

    public static EnumChatFormatting getDefaultColor() {
        return DefaultColor;
    }

    public static void setDefaultColor(EnumChatFormatting defaultColor) {
        DefaultColor = defaultColor;
    }

    /**
     * @see GTUtility#getColoredTierNameFromTier(byte)
     */
    public static String getTierName(@MagicConstant(valuesFromClass = VoltageIndex.class) int tier) {
        return GTValues.TIER_COLORS[tier] + GTValues.VN[tier] + getDefaultColor();
    }

    /**
     * @see GTUtility#getColoredTierNameFromVoltage(long)
     */
    public static String getTierNameAtVoltage(long voltage) {
        // noinspection MagicConstant getTier always returns valid values
        return getTierName(GTUtility.getTier(voltage));
    }
}
