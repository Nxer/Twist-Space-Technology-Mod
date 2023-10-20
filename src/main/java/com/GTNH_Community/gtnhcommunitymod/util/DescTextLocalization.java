package com.GTNH_Community.gtnhcommunitymod.util;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class DescTextLocalization {

    public static final String BLUE_PRINT_INFO = "Follow the" + EnumChatFormatting.BLUE
        + " Structure"
        + EnumChatFormatting.DARK_BLUE
        + "Lib"
        + EnumChatFormatting.GRAY
        + " hologram projector to build the main structure.";

    public static String[] addText(String preFix, int length) {
        String[] text = new String[length];
        for (int i = 0; i < length; i++) {
            text[i] = StatCollector.translateToLocal(preFix + "." + i);
        }
        return text;
    }
}
