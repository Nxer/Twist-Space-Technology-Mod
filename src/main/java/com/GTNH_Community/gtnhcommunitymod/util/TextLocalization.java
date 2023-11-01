package com.GTNH_Community.gtnhcommunitymod.util;

import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;

import net.minecraft.util.EnumChatFormatting;

// spotless:off
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
    public static final String textCenterOfLRSides = texter("Center area of left and right side", "textCenterOfLRSides");
    public static final String textCenterOfUDSides = texter("Center area of up and down side", "textCenterOfUDSides");
    public static final String StructureTooComplex = texter("The structure is too complex!", "StructureTooComplex");
    public static final String textCasingAdvIrPlated = texter("Advanced Iridium Plated Machine Casing", "textCasingAdvIrPlated");
    public static final String textCasingTT_0 = texter("High Power Casing", "textCasingTT_0");
    public static final String textAroundController = texter("Around the Controller", "textAroundController");

    public static final String textScrewdriverChangeMode = texter("Use screwdriver to change mode.", "textScrewdriverChangeMode");

    // region Intensify Chemical Distorter text localization
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
            + "APPROOOOOACHING !!",
        "Tooltip_ICD_01");
    public static final String Tooltip_ICD_02 = texter("The most advanced base chemical reactor.", "Tooltip_ICD_02");
    public static final String Tooltip_ICD_03 = textScrewdriverChangeMode;
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

    // endregion

    // region Precise High-Energy Photonic Quantum Master text localization

    public static final String NamePreciseHighEnergyPhotonicQuantumMaster = texter(
        "Precise High-Energy Photonic Quantum Master",
        "NamePreciseHighEnergyPhotonicQuantumMaster");

    public static final String Tooltip_PhC_MachineType = texter("Photon Controller/Laser Engraver", "Tooltip_PhC_MachineType");
    public static final String Tooltip_PhC_00 = texter("Controller block for the Precise High-Energy Photonic Quantum Master", "Tooltip_PhC_00");
    public static final String Tooltip_PhC_01 = texter(EnumChatFormatting.BLUE+"Prism tank in order, sir.", "Tooltip_PhC_01");
    public static final String Tooltip_PhC_02 = texter("Control Photons on the scale of 10\u00AF\u00B9\u00B2 meters.", "Tooltip_PhC_02");
    public static final String Tooltip_PhC_03 = texter("Install Photonic Intensifier on the back side of the structure to dramatically increase production speeds.", "Tooltip_PhC_03");
    public static final String Tooltip_PhC_04 = texter("Multi upgrade modules can be stacked. Also can be uninstalled. Replace using normal Casing.", "Tooltip_PhC_04");
    public static final String Tooltip_PhC_05 = textScrewdriverChangeMode;
    public static final String Tooltip_PhC_06 = texter(EnumChatFormatting.GOLD+"Photon Controller mode:", "Tooltip_PhC_06");
    public static final String Tooltip_PhC_07 = texter("16x Parallel", "Tooltip_PhC_07");
    public static final String Tooltip_PhC_08 = texter(EnumChatFormatting.GOLD+"Laser Engraver mode:", "Tooltip_PhC_08");
    public static final String Tooltip_PhC_09 = texter("256x Parallel | Extra reduce 50%% recipe time spent", "Tooltip_PhC_09");
    public static final String textHighPowerCasingUDSides = texter("High Power Casing area of up and down side", "textHighPowerCasingUDSides");
    public static final String textUpgradeCasingAndLocation = texter("Upgrade module casing at backside area wrapped by AdvIrPlated Casing", "textUpgradeCasing");

    public static final String[] Tooltips_Upgrades_LV = new String[]{texter("Extra 1%% Speed Up !","PhotonControllerUpgradeLV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_MV = new String[]{texter("Extra 2%% Speed Up !","PhotonControllerUpgradeMV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_HV = new String[]{texter("Extra 3%% Speed Up !","PhotonControllerUpgradeHV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_EV = new String[]{texter("Extra 4%% Speed Up !","PhotonControllerUpgradeEV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_IV = new String[]{texter("Extra 5%% Speed Up !","PhotonControllerUpgradeIV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_LuV = new String[]{texter("Extra 10%% Speed Up !","PhotonControllerUpgradeLuV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_ZPM = new String[]{texter("Extra 20%% Speed Up !","PhotonControllerUpgradeZPM.tooltips.01")};
    public static final String[] Tooltips_Upgrades_UV = new String[]{texter("Extra 40%% Speed Up !","PhotonControllerUpgradeUV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_UHV = new String[]{texter("Extra 70%% Speed Up !","PhotonControllerUpgradeUHV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_UEV = new String[]{texter("Extra 100%% Speed Up !","PhotonControllerUpgradeUEV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_UIV = new String[]{texter("Extra 140%% Speed Up !","PhotonControllerUpgradeUiV.tooltips.01")};
    public static final String[] Tooltips_Upgrades_UMV = new String[]{texter("Extra 190%% Speed Up !","PhotonControllerUpgradeUMV.tooltips.01"),
            texter("Enable Perfect Overclock !","PhotonControllerUpgradeUMV.tooltips.02")};
    public static final String[] Tooltips_Upgrades_UXV = new String[]{texter("Extra 250%% Speed Up !","PhotonControllerUpgradeUXV.tooltips.01"),
            texter("Enable Perfect Overclock !","PhotonControllerUpgradeUXV.tooltips.02")};
    public static final String[] Tooltips_Upgrades_MAX = new String[]{texter("Extra 320%% Speed Up !","PhotonControllerUpgradeMAX.tooltips.01"),
            texter("Enable Perfect Overclock !","PhotonControllerUpgradeMAX.tooltips.02")};

    public static final String[][] TooltipsUpgrades = new String[][]{
            Tooltips_Upgrades_LV,
            Tooltips_Upgrades_MV,
            Tooltips_Upgrades_HV,
            Tooltips_Upgrades_EV,
            Tooltips_Upgrades_IV,
            Tooltips_Upgrades_LuV,
            Tooltips_Upgrades_ZPM,
            Tooltips_Upgrades_UV,
            Tooltips_Upgrades_UHV,
            Tooltips_Upgrades_UEV,
            Tooltips_Upgrades_UIV,
            Tooltips_Upgrades_UMV,
            Tooltips_Upgrades_UXV,
            Tooltips_Upgrades_MAX
    };


    // endregion
    
    // region MiracleTop
    
    public static final String NameMiracleTop = texter(
        "Miracle Top",
        "NameMiracleTop");

}
// spotless:on
