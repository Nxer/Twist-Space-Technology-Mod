package com.Nxer.TwistSpaceTechnology.util.enums;

public class TierName {

    public static final String[] VoltageName = { "ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV",
        "UIV", "UMV", "UXV", "MAX", "ERROR" };

    public static String getVoltageName(int tier) {
        if (tier < 0) throw new IllegalArgumentException("tier < 0");
        if (tier >= VoltageName.length) return "ERROR";
        return VoltageName[tier];
    }
}
