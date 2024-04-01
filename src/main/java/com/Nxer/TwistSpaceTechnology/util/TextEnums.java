package com.Nxer.TwistSpaceTechnology.util;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

/**
 *
 */
public enum TextEnums {

    // region General

    // #tr Author_Goderium
    // # Author : {\WHITE}{\BOLD}Goderium
    // #zh_CN 作者 : {\WHITE}{\BOLD}Goderium
    Author_Goderium("Author_Goderium"),

    // #tr AddByTwistSpaceTechnology
    // # Added by {\GREEN}Twist Space Technology{\GRAY}
    // #zh_CN 由 {\GREEN}Twist Space Technology{\GRAY} 添加
    AddByTwistSpaceTechnology("AddByTwistSpaceTechnology"),
    SpeedMultiplier("MachineInfoData.SpeedMultiplier"),
    Parallels("MachineInfoData.Parallels"),
    EuModifier("MachineInfoData.EuModifier"),
    GlassTier("MachineInfoData.GlassTier"),

    // endregion

    // region Core Device of Human Power Generation Facility

    // #tr NameCoreDeviceOfHumanPowerGenerationFacility
    // # Core Device of Human Power Generation Facility
    // #zh_CN 人类能源设施的核心装置
    NameCoreDeviceOfHumanPowerGenerationFacility("NameCoreDeviceOfHumanPowerGenerationFacility")
    // endregion

    // endregion

    ;

    public static String tr(String key) {
        return translateToLocalFormatted(key);
    }

    private final String text;
    private final String key;

    TextEnums(String key) {
        this.key = key;
        this.text = tr(key);
    }

    @Override
    public String toString() {
        return text;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

}
