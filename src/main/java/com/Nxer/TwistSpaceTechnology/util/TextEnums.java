package com.Nxer.TwistSpaceTechnology.util;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

/**
 *
 */
public enum TextEnums {
    // spotless:off

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

    // #tr InstallingModuleNearControllerImproveMachine
    // # Installing module hatches near the controller block can significantly improve machine performance.
    // #zh_CN 在主机附近安装模块仓室可以显著提升机器性能.
    InstallingModuleNearControllerImproveMachine("InstallingModuleNearControllerImproveMachine"),

    // #tr ModularizedMachineSystemDescription01
    // # Install Module Hatches to enhance your machine.
    // #zh_CN 安装模块仓室强化你的机器.
    ModularizedMachineSystemDescription01("ModularizedMachineSystemDescription01"),

    // #tr ModularizedMachineSystemDescription02
    // # The installable modules are :
    // #zh_CN 可安装的模块有 :
    ModularizedMachineSystemDescription02("ModularizedMachineSystemDescription02"),

    // #tr OverclockControllerDescription
    // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}Overclock Controller Module - Set machine overclock type
    // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}超频控制器模块 - 设置机器的超频类型
    OverclockControllerDescription("OverclockControllerDescription"),

    // #tr ParallelControllerDescription
    // # Parallel Controller Module - Increase parallelism of machine
    // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}并行控制器模块 - 提高机器的并行数
    ParallelControllerDescription("ParallelControllerDescription"),

    // #tr PowerConsumptionControllerDescription
    // # {\SPACE}{\SPACE}{\SPACE}{\SPACE} Power Consumption Controller Module - Reduce machine energy consumption
    // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}耗能控制器模块 - 降低机器能耗
    PowerConsumptionControllerDescription("PowerConsumptionControllerDescription"),

    // #tr SpeedControllerDescription
    // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}Speed Controller Module - Increase processing speed of machine
    // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}速度控制器模块 - 提高机器运行速度
    SpeedControllerDescription("SpeedControllerDescription"),

    // #tr ExecutionCoreDescription
    // # {\SPACE}{\SPACE}{\SPACE}{\SPACE} Execution Core Module - Adds actual running units to the machine, true parallelism
    // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}执行核心模块 - 为机器添加实际运行单元, 真正意义上的并行
    ExecutionCoreDescription("ExecutionCoreDescription"),

    // #tr NotMultiplyInstallSameTypeModule
    // # Same type of module hatch cannot be installed repeatedly. Except for Execution Cores.
    // #zh_CN 不可重复安装同类型的模块仓室. 除了执行核心模块.
    NotMultiplyInstallSameTypeModule("NotMultiplyInstallSameTypeModule"),

    // #tr CanMultiplyInstallSameTypeModule
    // # Same type of module hatch can be installed repeatedly.
    // #zh_CN 可以重复安装同类型的模块仓室.
    CanMultiplyInstallSameTypeModule("CanMultiplyInstallSameTypeModule"),

    // #tr ModularHatch
    // # Modular Hatch
    // #zh_CN 模块仓室
    ModularHatch("ModularHatch"),
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
    BigBroArrayDesc12("BigBroArray.desc.12"),


    // end
    StructureTooComplex("StructureTooComplex");

    // spotless:on
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
