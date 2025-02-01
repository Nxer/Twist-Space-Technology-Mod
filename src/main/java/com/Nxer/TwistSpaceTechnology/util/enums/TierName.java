package com.Nxer.TwistSpaceTechnology.util.enums;

import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated use GregTech ones.
 *
 * @see gregtech.api.enums.GTValues#VN
 * @see gregtech.api.util.GTUtility#getColoredTierNameFromTier(byte)
 * @see gregtech.api.util.GTUtility#getColoredTierNameFromVoltage(long)
 * @see gregtech.api.util.GTUtility#getTierNameWithParentheses(long)
 */
@ApiStatus.Obsolete
public class TierName {

    public static final String[] VoltageName = { "ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV",
        "UIV", "UMV", "UXV", "MAX", "ERROR" };

    public static String getVoltageName(int tier) {
        if (tier < 0) throw new IllegalArgumentException("tier < 0");
        if (tier >= VoltageName.length) return "ERROR";
        return VoltageName[tier];
    }
}
