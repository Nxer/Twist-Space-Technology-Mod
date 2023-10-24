package com.GTNH_Community.gtnhcommunitymod.util;

import net.minecraft.util.StatCollector;

public class DescTextLocalization {

    // public static final String HeatCapacity = StatCollector.translateToLocal(TextLocalization.HeatCapacity);

    public static String[] addText(String preFix, int length) {
        String[] text = new String[length];
        for (int i = 0; i < length; i++) {
            text[i] = StatCollector.translateToLocal(preFix + "." + i);
        }
        return text;
    }
}
