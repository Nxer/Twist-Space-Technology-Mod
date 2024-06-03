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
    NameCoreDeviceOfHumanPowerGenerationFacility("NameCoreDeviceOfHumanPowerGenerationFacility"),
    // endregion

    // region Single World

    // #tr World_Parallel
    // # Parallel
    // #zh_CN 并行
    World_Parallel("World_Parallel"),

    // #tr World_Overclock
    // # Overclock
    // #zh_CN 超频
    World_Overclock("World_Overclock"),

    // endregion

    // region Modularized Machine System

    // #tr ModularizedMachineSystem
    // # {\BLUE}{\BOLD}Modularized Machine System
    // #zh_CN {\BLUE}{\BOLD}模块化机械系统
    ModularizedMachineSystem("ModularizedMachineSystem"),
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
    BigBroArrayDesc8("BigBroArray.desc.8"),

    BigBroArrayDesc9("BigBroArray.desc.9"),

    BigBroArrayDesc10("BigBroArray.desc.10"),
    BigBroArrayDesc11("BigBroArray.desc.11"),

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
