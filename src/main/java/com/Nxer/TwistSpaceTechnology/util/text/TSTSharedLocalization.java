package com.Nxer.TwistSpaceTechnology.util.text;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.Tags;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

/**
 * The commonly used localization texts.
 *
 * @since 0.6.4
 */
public class TSTSharedLocalization {

    public static class General {

        public static final String ModName = Tags.MODNAME;

        // #tr Machine_of_TwistSpaceTechnology
        // # {\BOLD}{\BLUE}Twist {\AQUA}Space {\YELLOW}Technology{\RESET} is honored to serve you
        // #zh_CN {\BOLD}{\BLUE}Twist {\AQUA}Space {\YELLOW}Technology{\RESET} 很荣幸为您服务!
        public static final String Machine_of_TwistSpaceTechnology = TSTUtils.tr("Machine_of_TwistSpaceTechnology");

        // #tr ModNameDesc
        // # Added by %s
        // #zh_CN 由 %s 添加
        public static final String ModNameDesc = TSTUtils.tr("ModNameDesc");

        // #tr HeatCapacity
        // # Heat Capacity:
        // #zh_CN 热容:
        public static final String HeatCapacity = TSTUtils.tr("HeatCapacity");

        // #tr FluidCapacity
        // # Capacity:
        // #zh_CN 容量:
        public static final String FluidCapacity = TSTUtils.tr("FluidCapacity");

        // #tr HatchTier
        // # Hatch Tier:
        // #zh_CN 仓室等级:
        public static final String HatchTier = TSTUtils.tr("HatchTier");

        // #tr Kelvin
        // # K
        // #zh_CN K
        public static final String Kelvin = TSTUtils.tr("Kelvin");

        // #tr AutoSeparation
        // # Automatically separate inputs
        // #zh_CN 自动隔离输入
        public static final String AutoSeparation = TSTUtils.tr("AutoSeparation");

        // #tr Text_SeparatingLine
        // # {\GOLD}-----------------------------------------
        // #zh_CN {\GOLD}-----------------------------------------
        public static final String Text_SeparatingLine = TSTUtils.tr("Text_SeparatingLine");

        // #tr Word_Parallel
        // # Parallel
        // #zh_CN 并行
        public static final String Word_Parallel = TSTUtils.tr("Word_Parallel");

        // #tr Word_Overclock
        // # Overclock
        // #zh_CN 超频
        public static final String Word_Overclock = TSTUtils.tr("Word_Overclock");
    }

    public static class MachineInfo {

        // #tr MachineInfoData.SpeedMultiplier
        // # Speed multiplier
        // #zh_CN 耗时倍率
        public static final String SpeedMultiplier = TSTUtils.tr("MachineInfoData.SpeedMultiplier");

        // #tr MachineInfoData.Parallels
        // # Parallels
        // #zh_CN 并行
        public static final String Parallels = TSTUtils.tr("MachineInfoData.Parallels");

        // #tr MachineInfoData.EuModifier
        // # EU Modifier
        // #zh_CN 耗电倍率
        public static final String EuModifier = TSTUtils.tr("MachineInfoData.EuModifier");

        // #tr MachineInfoData.HighCapacityOutput
        // # High-Capacity Output
        // #zh_CN 超大容量输出
        public static final String HighCapacityOutput = TSTUtils.tr("MachineInfoData.HighCapacityOutput");

        // #tr MachineInfoData.GlassTier
        // # {\AQUA}Glass Tier
        // #zh_CN {\AQUA}玻璃等级
        public static final String GlassTier = TSTUtils.tr("MachineInfoData.GlassTier");

        // #tr MachineInfoData.CoilTier
        // # Coil Tier
        // #zh_CN 线圈等级
        public static final String CoilTier = TSTUtils.tr("MachineInfoData.CoilTier");

        // #tr MachineInfoData.CurrentPowerConsumption
        // # Current Power Consumption
        // #zh_CN 当前耗电
        public static final String CurrentPowerConsumption = TSTUtils.tr("MachineInfoData.CurrentPowerConsumption");

        // #tr MachineInfoData.MachineMode
        // # Machine Mode
        // #zh_CN 机器模式
        public static final String MachineMode = TSTUtils.tr("MachineInfoData.MachineMode");

        // #tr MachineInfoData.MachineTier
        // # Machine Tier
        // #zh_CN 机器等级
        public static final String MachineTier = TSTUtils.tr("MachineInfoData.MachineTier");

        // #tr MachineInfoData.FieldGeneratorTier
        // # Field Generator Tier
        // #zh_CN 力场发生器等级
        public static final String FieldGeneratorTier = TSTUtils.tr("MachineInfoData.FieldGeneratorTier");

        // #tr MachineInfoData.FusionCoilTier
        // # Fusion Coil Tier
        // #zh_CN 聚变线圈等级
        public static final String FusionCoilTier = TSTUtils.tr("MachineInfoData.FusionCoilTier");

        // #tr MachineInfoData.CompactFusionCoilTier
        // # Compact Fusion Coil Tier
        // #zh_CN 压缩聚变线圈等级
        public static final String CompactFusionCoilTier = TSTUtils.tr("MachineInfoData.CompactFusionCoilTier");

        // #tr General.getInfoData.Wireless_mode_enabled
        // # Wireless mode enabled
        // #zh_CN 已启用无线模式
        public static final String Info_Wireless_mode_enabled = TSTUtils
            .tr("General.getInfoData.Wireless_mode_enabled");

        public static String glassTier(int glassTier) {
            // #tr tst.shared.machineInfo.glassTier
            // # {\AQUA}Glass Tier: {\GOLD}%s
            // #zh_CN {\AQUA}玻璃等级: {\GOLD}%s
            return TSTUtils.tr("tst.shared.machineInfo.glassTier", glassTier);
        }

        public static String coilTier(int coilTier) {
            // #tr tst.shared.machineInfo.coilTier
            // # {\AQUA}Coil Tier: {\GOLD}%s
            // #zh_CN {\AQUA}线圈等级: {\GOLD}%s
            return TSTUtils.tr("tst.shared.machineInfo.coilTier", coilTier);
        }

        public static String componentTier(int componentTier) {
            // #tr tst.shared.machineInfo.componentTier
            // # {\AQUA}Component Tier: {\GOLD}%s
            // #zh_CN {\AQUA}部件等级: {\GOLD}%s
            return TSTUtils.tr("tst.shared.machineInfo.componentTier", componentTier);
        }
    }

    public static class Waila {

        // #tr Waila.General.WirelessMode
        // # Wireless Mode
        // #zh_CN 无线模式
        public static final String Waila_WirelessMode = TSTUtils.tr("Waila.General.WirelessMode");

        // #tr Waila.General.CurrentEuCost
        // # Current EU Cost
        // #zh_CN 当前EU消耗
        public static final String Waila_CurrentEuCost = TSTUtils.tr("Waila.General.CurrentEuCost");
    }

    public static class Structure {

        // #tr textCasing
        // # Casing
        // #zh_CN Casing
        public static final String textCasing = TSTUtils.tr("textCasing");

        // #tr textUseBlueprint
        // # Use {\BLUE}Blue{\AQUA}print{\RESET} to preview
        // #zh_CN 用{\BLUE}蓝{\AQUA}图{\RESET}预览
        public static final String textUseBlueprint = TSTUtils.tr("textUseBlueprint");

        // #tr textColon
        // # :{\SPACE}
        // #zh_CN ：{\SPACE}
        public static final String textColon = TSTUtils.tr("textColon");

        // #tr textDot
        // # Dot
        // #zh_CN 提示方块
        public static final String textDot = TSTUtils.tr("textDot");

        public static String getBlueprintWithDot(int dot) {
            return textUseBlueprint + EnumChatFormatting.WHITE + " " + textDot + " : " + EnumChatFormatting.AQUA + dot;
        }

        // #tr textSpace
        // # {\SPACE}
        // #zh_CN {\SPACE}
        public static final String textSpace = TSTUtils.tr("textSpace");

        // #tr textAnyCasing
        // # Any Casing
        // #zh_CN 任意机械方块
        public static final String textAnyCasing = TSTUtils.tr("textAnyCasing");

        // #tr textTopCenter
        // # Top center
        // #zh_CN 顶层中央
        public static final String textTopCenter = TSTUtils.tr("textTopCenter");

        // #tr textFrontCenter
        // # Front center
        // #zh_CN 正面中央
        public static final String textFrontCenter = TSTUtils.tr("textFrontCenter");

        // #tr textFrontBottom
        // # Front bottom
        // #zh_CN 正面底中
        public static final String textFrontBottom = TSTUtils.tr("textFrontBottom");

        // #tr textCenterOfLRSides
        // # Center area of left and right side
        // #zh_CN 左右两侧的中央区域
        public static final String textCenterOfLRSides = TSTUtils.tr("textCenterOfLRSides");

        // #tr textCenterOfUDSides
        // # Center area of up and down side
        // #zh_CN 上下侧的中央区域
        public static final String textCenterOfUDSides = TSTUtils.tr("textCenterOfUDSides");

        // #tr textEndSides
        // # Machine end
        // #zh_CN 机器末端
        public static final String textEndSides = TSTUtils.tr("textEndSides");

        // #tr textCasingAdvIrPlated
        // # Advanced Iridium Plated Machine Casing
        // #zh_CN 强化镀铱机械方块
        public static final String textCasingAdvIrPlated = TSTUtils.tr("textCasingAdvIrPlated");

        // #tr textCasingTT_0
        // # High Power Casing
        // #zh_CN 超能机械方块
        public static final String textCasingTT_0 = TSTUtils.tr("textCasingTT_0");

        // #tr textAroundController
        // # Around the Controller
        // #zh_CN 控制器方块周围
        public static final String textAroundController = TSTUtils.tr("textAroundController");
    }

    public static class MachineTooltip {

        // spotless:off
        // #tr MoreInfoCheckingInScanner
        // # {\WHITE}Use scanner check controller block to get more information.
        // #zh_CN {\WHITE}使用三录仪查看主机获取更多信息.
        public static final String MoreInfoCheckingInScanner = TSTUtils.tr("MoreInfoCheckingInScanner");

        // #tr Tooltip_Details
        // # {\LIGHT_PURPLE}Details:
        // #zh_CN {\LIGHT_PURPLE}具体细节:
        public static final String Tooltip_Details = TSTUtils.tr("Tooltip_Details");

        // #tr Tooltips_JoinWirelessNetWithoutEnergyHatch
        // # Joining the wireless EU network when without installing an energy hatch.
        // #zh_CN 未安装能源仓时自动进入无线电力网络模式.
        public static final String Tooltips_JoinWirelessNetWithoutEnergyHatch = TSTUtils.tr("Tooltips_JoinWirelessNetWithoutEnergyHatch");

        // #tr Tooltip_DoNotNeedMaintenance
        // # Do Not Need Maintenance!
        // #zh_CN 不需要维护仓!
        public static final String Tooltip_DoNotNeedMaintenance = TSTUtils.tr("Tooltip_DoNotNeedMaintenance");

        // #tr Tooltip_DoNotNeedEnergyHatch
        // # Do Not Need Energy Hatch!
        // #zh_CN 不需要能源仓!
        public static final String Tooltip_DoNotNeedEnergyHatch = TSTUtils.tr("Tooltip_DoNotNeedEnergyHatch");

        // #tr Mark_TwistSpaceTechnology_TecTech
        // # {\AQUA}{\BOLD}Twist Space Technology : {\RESET}Tech
        // #zh_CN {\AQUA}{\BOLD}Twist Space Technology : {\RESET}{\BLUE}Tec{\DARK_BLUE}Tech
        public static final String Mark_TwistSpaceTechnology_TecTech = TSTUtils.tr("Mark_TwistSpaceTechnology_TecTech");

        // #tr textScrewdriverChangeMode
        // # Use screwdriver to change mode.
        // #zh_CN 使用螺丝刀切换模式.
        public static final String textScrewdriverChangeMode = TSTUtils.tr("textScrewdriverChangeMode");

        // #tr Tooltip_GlassTierLimitEnergyHatchTier
        // # The Glass Tier limit the Energy hatch voltage Tier.
        // #zh_CN 玻璃等级限制能源仓等级.
        public static final String Tooltip_GlassTierLimitEnergyHatchTier = TSTUtils.tr("Tooltip_GlassTierLimitEnergyHatchTier");

        // #tr OutOfMaintenance
        // # {\RED}{\BOLD} OUT OF MAINTENANCE !
        // #zh_CN {\RED}{\BOLD}不再维护！
        public static final String OutOfMaintenance = TSTUtils.tr("OutOfMaintenance");
        // spotless:on

        public static String tooComplex() {
            return TSTUtils.tr("GT5U.MBTT.Structure.Complex");
        }

        public static String temporaryController() {
            // #tr tst.shared.machineTooltip.temporaryController
            // # {\RED}Temporary controller; will be removed in the next update!
            // #zh_CN {\RED}临时控制器, 将在下个版本移除!
            return TSTUtils.tr("tst.shared.machineTooltip.temporaryController");
        }

        public static String replacementController() {
            // #tr tst.shared.machineTooltip.replacementController
            // # {\RED}See NEI for the replacement controller and conversion recipes.
            // #zh_CN {\RED}请在NEI查看替代控制器及转换配方.
            return TSTUtils.tr("tst.shared.machineTooltip.replacementController");
        }

    }

    public static class DysonSphere {

        // spotless:off
        // #tr RiseOfTheDarkFog
        // # {\BLACK}{\BOLD}Rise of the Dark Fog
        // #zh_CN {\BLACK}{\BOLD}黑雾崛起
        public static final String RiseOfDarkFog = TSTUtils.tr("RiseOfTheDarkFog");

        // #tr DSPName
        // # {\BLUE}Dyson Sphere Program
        // #zh_CN {\BLUE}戴森球计划
        public static final String DSPName = TSTUtils.tr("DSPName");

        // #tr Tooltip_DSPInfo_launch_01
        // # Launching Solar Sail increase Solar Sail amount of current Galaxy's Dyson Sphere.
        // #zh_CN 发射太阳帆增加当前星系戴森球的太阳帆数量.
        public static final String Tooltip_DSPInfo_launch_01 = TSTUtils.tr("Tooltip_DSPInfo_launch_01");

        // #tr Tooltip_DSPInfo_launch_02
        // # Launching Small Launch Vehicle increase Node amount of current Galaxy's Dyson Sphere.
        // #zh_CN 发射小型运载火箭增加当前星系戴森球的节点数量.
        public static final String Tooltip_DSPInfo_launch_02 = TSTUtils.tr("Tooltip_DSPInfo_launch_02");

        // #tr Tooltip_DSPInfo_00
        // # Dyson Sphere Energy Credit = Solar Sail Output (default 524288) * Solar Sail amount * (Node + 1)^0.8
        // #zh_CN 戴森球产能点数 = 每太阳帆产出 (默认 524288) * 太阳帆数量 * (节点数量 + 1)^0.8
        public static final String Tooltip_DSPInfo_00 = TSTUtils.tr("Tooltip_DSPInfo_00");

        // #tr Tooltip_DSPInfo_01
        // # Every Node can absorb (default) 256 Solar Sails.
        // #zh_CN 每个节点可以吸附 (默认) 256 太阳帆.
        public static final String Tooltip_DSPInfo_01 = TSTUtils.tr("Tooltip_DSPInfo_01");

        // #tr Tooltip_DSPInfo_02
        // # If unabsorbed solar sails amount is larger than 2048,
        // #zh_CN 如果未吸附的太阳帆数量超过2048,
        public static final String Tooltip_DSPInfo_02 = TSTUtils.tr("Tooltip_DSPInfo_02");

        // #tr Tooltip_DSPInfo_03
        // # the excess may be destroyed.
        // #zh_CN  超过的部分可能会损毁.
        public static final String Tooltip_DSPInfo_03 = TSTUtils.tr("Tooltip_DSPInfo_03");

        // #tr Tooltip_DSPInfo_04
        // # Every 30 minutes has 1/5000 chance to decrease Solar Sail amount.
        // #zh_CN 每30 分钟有 1/5000 几率触发太阳帆损毁事件.
        public static final String Tooltip_DSPInfo_04 = TSTUtils.tr("Tooltip_DSPInfo_04");

        // #tr Tooltip_DSPInfo_05
        // # In decreasing, every Dyson Sphere has 1/4 chance to destroyed Solar Sail amount.
        // #zh_CN 在损毁事件里, 每个戴森球都有 1/4 几率损毁太阳帆.
        public static final String Tooltip_DSPInfo_05 = TSTUtils.tr("Tooltip_DSPInfo_05");

        // #tr Tooltip_DSPInfo_06
        // # The amount of destroyed is the excess' 1/2.
        // #zh_CN 损毁的数量是超过部分的 1/2 .
        public static final String Tooltip_DSPInfo_06 = TSTUtils.tr("Tooltip_DSPInfo_06");
        // spotless:on
    }

    public static class ModularizedMachine {

        // spotless:off
        // #tr ModularizedMachineSystem
        // # {\BLUE}{\BOLD}Modularized Machine System
        // #zh_CN {\BLUE}{\BOLD}模块化机械系统
        public static final String ModularizedMachineSystem = TSTUtils.tr("ModularizedMachineSystem");

        // #tr InstallingModuleNearControllerImproveMachine
        // # Installing module hatches near the controller block can significantly improve machine performance.
        // #zh_CN 在主机附近安装模块仓室可以显著提升机器性能.
        public static final String InstallingModuleNearControllerImproveMachine = TSTUtils.tr("InstallingModuleNearControllerImproveMachine");

        // #tr ModularizedMachineSystemDescription01
        // # Install Module Hatches to enhance your machine.
        // #zh_CN 安装模块仓室强化你的机器.
        public static final String ModularizedMachineSystemDescription01 = TSTUtils.tr("ModularizedMachineSystemDescription01");

        // #tr ModularizedMachineSystemDescription02
        // # The installable modules are :
        // #zh_CN 可安装的模块有 :
        public static final String ModularizedMachineSystemDescription02 = TSTUtils.tr("ModularizedMachineSystemDescription02");

        // #tr OverclockControllerDescription
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}Overclock Controller Module - Set machine overclock type
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}超频控制器模块 - 设置机器的超频类型
        public static final String OverclockControllerDescription = TSTUtils.tr("OverclockControllerDescription");

        // #tr ParallelControllerDescription
        // # Parallel Controller Module - Increase parallelism of machine
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}并行控制器模块 - 提高机器的并行数
        public static final String ParallelControllerDescription = TSTUtils.tr("ParallelControllerDescription");

        // #tr PowerConsumptionControllerDescription
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE} Power Consumption Controller Module - Reduce machine energy consumption
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}耗能控制器模块 - 降低机器能耗
        public static final String PowerConsumptionControllerDescription = TSTUtils.tr("PowerConsumptionControllerDescription");

        // #tr SpeedControllerDescription
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}Speed Controller Module - Increase processing speed of machine
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}速度控制器模块 - 提高机器运行速度
        public static final String SpeedControllerDescription = TSTUtils.tr("SpeedControllerDescription");

        // #tr ExecutionCoreDescription
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE} Execution Core Module - Adds actual running units to the machine, true parallelism
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}执行核心模块 - 为机器添加实际运行单元, 真正意义上的并行
        public static final String ExecutionCoreDescription = TSTUtils.tr("ExecutionCoreDescription");

        // #tr NotMultiplyInstallSameTypeModule
        // # Same type of module hatch cannot be installed repeatedly. Except for Execution Cores.
        // #zh_CN 不可重复安装同类型的模块仓室. 除了执行核心模块.
        public static final String NotMultiplyInstallSameTypeModule = TSTUtils.tr("NotMultiplyInstallSameTypeModule");

        // #tr NotMultiplyInstallSameTypeModuleAll
        // # Same type of module hatch cannot be installed repeatedly.
        // #zh_CN 不可重复安装同类型的模块仓室.
        public static final String NotMultiplyInstallSameTypeModuleAll = TSTUtils.tr("NotMultiplyInstallSameTypeModuleAll");

        // #tr CanMultiplyInstallSameTypeModule
        // # Same type of module hatch can be installed repeatedly.
        // #zh_CN 可以重复安装同类型的模块仓室.
        public static final String CanMultiplyInstallSameTypeModule = TSTUtils.tr("CanMultiplyInstallSameTypeModule");

        // #tr ModularHatch
        // # Modular Hatch
        // #zh_CN 模块仓室
        public static final String ModularHatchKey = "ModularHatch";
        public static final String ModularHatch = TSTUtils.tr(ModularHatchKey);
        // spotless:on
    }

    public static class Command {

        public static ChatComponentTranslation invalidCommand() {
            // #tr TST_Command.InvalidCommand
            // # {\RED}Invalid command.
            // #zh_CN 无效指令.
            return new ChatComponentTranslation("TST_Command.InvalidCommand");
        }

        public static ChatComponentTranslation formatError() {
            // #tr TST_Command.InputFormatError
            // # {\RED}Input format error, please check your inputs.
            // #zh_CN 输入格式错误, 请检查你的输入参数.
            return new ChatComponentTranslation("TST_Command.InputFormatError");
        }
    }

}
