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

    // #tr MachineInfoData.CoilTier
    // # Coil Tier
    // #zh_CN 线圈等级
    CoilTier("MachineInfoData.CoilTier"),

    // #tr MachineInfoData.MachineMode
    // # Machine Mode
    // #zh_CN 机器模式
    MachineMode("MachineInfoData.MachineMode"),

    // #tr MachineInfoData.MachineTier
    // # Machine Tier
    // #zh_CN 机器等级
    MachineTier("MachineInfoData.MachineTier"),

    // #tr MachineInfoData.FieldGeneratorTier
    // # Field Generator Tier
    // #zh_CN 力场发生器等级
    FieldGeneratorTier("MachineInfoData.FieldGeneratorTier"),

    // #tr MachineInfoData.FusionCoilTier
    // # Fusion Coil Tier
    // #zh_CN 聚变线圈等级
    FusionCoilTier("MachineInfoData.FusionCoilTier"),

    // #tr MachineInfoData.CompactFusionCoilTier
    // # Compact Fusion Coil Tier
    // #zh_CN 压缩聚变线圈等级
    CompactFusionCoilTier("MachineInfoData.CompactFusionCoilTier"),

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
