package com.Nxer.TwistSpaceTechnology.util;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

/**
 *
 */
public enum TextEnums {

    // region General

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
    NameCoreDeviceOfHumanPowerGenerationFacility("NameCoreDeviceOfHumanPowerGenerationFacility"),
    // endregion

    // endregion

    // start
    BigBroArrayName("BigBroArray.name"),
    BigBroArrayType("BigBroArray.type"),
    BigBroArrayDesc1("BigBroArray.desc.1"),
    BigBroArrayDesc2("BigBroArray.desc.2"),
    BigBroArrayDesc3("BigBroArray.desc.3"),
    BigBroArrayDesc4("BigBroArray.desc.4"),
    BigBroArrayDesc5("BigBroArray.desc.5"),
    BigBroArrayDesc6("BigBroArray.desc.6"),
    BigBroArrayDesc7("BigBroArray.desc.7"),

    // end
    StructureTooComplex("StructureTooComplex");

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
