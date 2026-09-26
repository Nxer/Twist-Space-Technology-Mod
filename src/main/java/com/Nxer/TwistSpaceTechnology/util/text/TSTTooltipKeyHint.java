package com.Nxer.TwistSpaceTechnology.util.text;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import cpw.mods.fml.common.Loader;

public class TSTTooltipKeyHint {

    // #tr tst.common.shared.tooltip.key.more
    // # Hold %s for more
    // #zh_CN 按住 %s 显示更多
    public static final String DEFAULT_MODIFIER = "tst.common.shared.tooltip.key.more";

    public static final String CHROMATIC_ALT_MODIFIER = "§!alt";
    public static final String CHROMATIC_CTRL_MODIFIER = "§!ctrl";
    public static final String CHROMATIC_SHIFT_MODIFIER = "§!shift";

    private static boolean chromaticLoaded;
    private static boolean chromaticChecked;

    public static boolean isChromaticLoaded() {
        if (!chromaticChecked) {
            chromaticLoaded = Loader.isModLoaded("chromatictooltips");
            chromaticChecked = true;
        }

        return chromaticLoaded;
    }

    public static String getDefaultKeyHint(String key) {
        return TSTUtils
            .tr(DEFAULT_MODIFIER, EnumChatFormatting.YELLOW + key + EnumChatFormatting.RESET + EnumChatFormatting.GRAY);
    }

    public static String getAltKeyHint() {
        return getShiftKeyHint(getDefaultKeyHint("Alt"));
    }

    public static String getAltKeyHint(String fallback) {
        if (isChromaticLoaded()) {
            return CHROMATIC_ALT_MODIFIER;
        }
        return fallback;
    }

    public static String getCtrlKeyHint() {
        return getShiftKeyHint(getDefaultKeyHint("Ctrl"));
    }

    public static String getCtrlKeyHint(String fallback) {
        if (isChromaticLoaded()) {
            return CHROMATIC_CTRL_MODIFIER;
        }
        return fallback;
    }

    public static String getShiftKeyHint() {
        return getShiftKeyHint(getDefaultKeyHint("Shift"));
    }

    public static String getShiftKeyHint(String fallback) {
        if (isChromaticLoaded()) {
            return CHROMATIC_SHIFT_MODIFIER;
        }
        return fallback;
    }
}
