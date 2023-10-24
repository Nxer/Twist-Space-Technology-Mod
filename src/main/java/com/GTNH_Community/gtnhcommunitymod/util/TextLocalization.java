package com.GTNH_Community.gtnhcommunitymod.util;

import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;

import net.minecraft.util.EnumChatFormatting;

/**
 * <li>It's best to write here the texts that need auto generate en_US.lang .
 * <li>Or some usually used texts.
 */
public class TextLocalization {

    public static final String ModName = "GTNH Community Mod";

    public static final String HeatCapacity = texter("Heat Capacity: ", "HeatCapacity");
    public static final String Kelvin = texter(" K", "Kelvin");
    public static final String BLUE_PRINT_INFO = texter(
        "Follow the" + EnumChatFormatting.BLUE
            + " Structure"
            + EnumChatFormatting.DARK_BLUE
            + "Lib"
            + EnumChatFormatting.GRAY
            + " hologram projector to build the main structure.",
        "BLUE_PRINT_INFO");
    public static final String textCasing = texter("Casing", "textCasing");
    public static final String textAnyCasing = texter("Any Casing", "textAnyCasing");
    public static final String textFrontCenter = texter("Front center", "textFrontCenter");
    public static final String textFrontBottom = texter("Front bottom", "textFrontBottom");
    public static final String StructureTooComplex = texter("The structure is too complex!", "StructureTooComplex");

    /**
     * Intensify Chemical Distorter text localization
     */

    // --
    public static final String NameIntensifyChemicalDistorter = texter(
        "Intensify Chemical Distorter",
        "NameIntensifyChemicalDistorter");

    public static final String Tooltip_ICD_MachineType = texter(
        "Intensify Chemical Distorter/Chemical Reactor",
        "Tooltip_ICD_MachineType");
    public static final String Tooltip_ICD_00 = texter(
        "Controller block for the Intensify Chemical Distorter",
        "Tooltip_ICD_00");
    public static final String Tooltip_ICD_01 = texter(
        EnumChatFormatting.AQUA + "I! "
            + EnumChatFormatting.BLUE
            + "AM! "
            + EnumChatFormatting.AQUA
            + "THE! "
            + EnumChatFormatting.BLUE
            + "CHEM! "
            + EnumChatFormatting.AQUA
            + "THAT! "
            + EnumChatFormatting.BLUE
            + "IS! "
            + EnumChatFormatting.AQUA
            + "APROOOOOACHING !!",
        "Tooltip_ICD_01");
    public static final String Tooltip_ICD_02 = texter("The most advanced base chemical reactor.", "Tooltip_ICD_02");
    public static final String Tooltip_ICD_03 = texter("Use screwdriver to change mode.", "Tooltip_ICD_03");
    public static final String Tooltip_ICD_04 = texter(
        EnumChatFormatting.GOLD + "Intensify Chemical Distorter mode: ",
        "Tooltip_ICD_04");
    public static final String Tooltip_ICD_05 = texter(
        "Focus on processing the most complex chemical reaction - 16x Parallel.",
        "Tooltip_ICD_05");
    public static final String Tooltip_ICD_06 = texter(
        EnumChatFormatting.GOLD + "Chemical Reactor mode: ",
        "Tooltip_ICD_06");
    public static final String Tooltip_ICD_07 = texter(
        "1024 Parallel and 900% faster than using LCR of the same voltage.",
        "Tooltip_ICD_07");
    // public static final String Tooltip_ICD_08 = texter(,"Tooltip_ICD_08");
    // public static final String Tooltip_ICD_09 = texter();
    // public static final String Tooltip_ICD_10 = texter();
    // public static final String Tooltip_ICD_11 = texter();
    // public static final String Tooltip_ICD_12 = texter();

}
