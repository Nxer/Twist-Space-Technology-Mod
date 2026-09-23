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

        // spotless:off
        // #tr tst.common.shared.general.machine_of_twist_space_technology
        // # {\BOLD}{\BLUE}Twist {\AQUA}Space {\YELLOW}Technology{\RESET} is honored to serve you
        // #zh_CN {\BOLD}{\BLUE}Twist {\AQUA}Space {\YELLOW}Technology{\RESET} 很荣幸为您服务!
        public static final String Machine_of_TwistSpaceTechnology = TSTUtils.tr("tst.common.shared.general.machine_of_twist_space_technology");

        // #tr tst.common.shared.credit.mod_name
        // # Added by %s
        // #zh_CN 由 %s 添加
        public static final String ModNameDesc = TSTUtils.tr("tst.common.shared.credit.mod_name");

        // #tr tst.common.shared.general.heat_capacity
        // # Heat Capacity:
        // #zh_CN 热容:
        public static final String HeatCapacity = TSTUtils.tr("tst.common.shared.general.heat_capacity");

        // #tr tst.common.shared.general.fluid_capacity
        // # Capacity:
        // #zh_CN 容量:
        public static final String FluidCapacity = TSTUtils.tr("tst.common.shared.general.fluid_capacity");

        // #tr tst.common.shared.general.hatch_tier
        // # Hatch Tier:
        // #zh_CN 仓室等级:
        public static final String HatchTier = TSTUtils.tr("tst.common.shared.general.hatch_tier");

        // #tr tst.common.shared.general.kelvin
        // # K
        // #zh_CN K
        public static final String Kelvin = TSTUtils.tr("tst.common.shared.general.kelvin");

        // #tr tst.common.shared.general.auto_separation
        // # Automatically separate inputs
        // #zh_CN 自动隔离输入
        public static final String AutoSeparation = TSTUtils.tr("tst.common.shared.general.auto_separation");

        // #tr tst.common.shared.general.text_separating_line
        // # {\GOLD}-----------------------------------------
        // #zh_CN {\GOLD}-----------------------------------------
        public static final String Text_SeparatingLine = TSTUtils.tr("tst.common.shared.general.text_separating_line");

        // #tr tst.common.shared.general.word_parallel
        // # Parallel
        // #zh_CN 并行
        public static final String Word_Parallel = TSTUtils.tr("tst.common.shared.general.word_parallel");

        // #tr tst.common.shared.general.word_overclock
        // # Overclock
        // #zh_CN 超频
        public static final String Word_Overclock = TSTUtils.tr("tst.common.shared.general.word_overclock");
        // spotless:on
    }

    public static class MachineInfo {

        // spotless:off
        // #tr tst.common.shared.machine_info.speed_multiplier
        // # Speed multiplier
        // #zh_CN 耗时倍率
        public static final String SpeedMultiplier = TSTUtils.tr("tst.common.shared.machine_info.speed_multiplier");

        // #tr tst.common.shared.machine_info.parallels
        // # Parallels
        // #zh_CN 并行
        public static final String Parallels = TSTUtils.tr("tst.common.shared.machine_info.parallels");

        // #tr tst.common.shared.machine_info.eu_modifier
        // # EU Modifier
        // #zh_CN 耗电倍率
        public static final String EuModifier = TSTUtils.tr("tst.common.shared.machine_info.eu_modifier");

        // #tr tst.common.shared.machine_info.high_capacity_output
        // # High-Capacity Output
        // #zh_CN 超大容量输出
        public static final String HighCapacityOutput = TSTUtils.tr("tst.common.shared.machine_info.high_capacity_output");

        // #tr tst.common.shared.machine_info.glass_tier
        // # {\AQUA}Glass Tier
        // #zh_CN {\AQUA}玻璃等级
        public static final String GlassTier = TSTUtils.tr("tst.common.shared.machine_info.glass_tier");

        // #tr tst.common.shared.machine_info.coil_tier
        // # Coil Tier
        // #zh_CN 线圈等级
        public static final String CoilTier = TSTUtils.tr("tst.common.shared.machine_info.coil_tier");

        // #tr tst.common.shared.machine_info.current_power_consumption
        // # Current Power Consumption
        // #zh_CN 当前耗电
        public static final String CurrentPowerConsumption = TSTUtils.tr("tst.common.shared.machine_info.current_power_consumption");

        // #tr tst.common.shared.machine_info.machine_mode
        // # Machine Mode
        // #zh_CN 机器模式
        public static final String MachineMode = TSTUtils.tr("tst.common.shared.machine_info.machine_mode");

        // #tr tst.common.shared.machine_info.machine_tier
        // # Machine Tier
        // #zh_CN 机器等级
        public static final String MachineTier = TSTUtils.tr("tst.common.shared.machine_info.machine_tier");

        // #tr tst.common.shared.machine_info.field_generator_tier
        // # Field Generator Tier
        // #zh_CN 力场发生器等级
        public static final String FieldGeneratorTier = TSTUtils.tr("tst.common.shared.machine_info.field_generator_tier");

        // #tr tst.common.shared.machine_info.fusion_coil_tier
        // # Fusion Coil Tier
        // #zh_CN 聚变线圈等级
        public static final String FusionCoilTier = TSTUtils.tr("tst.common.shared.machine_info.fusion_coil_tier");

        // #tr tst.common.shared.machine_info.compact_fusion_coil_tier
        // # Compact Fusion Coil Tier
        // #zh_CN 压缩聚变线圈等级
        public static final String CompactFusionCoilTier = TSTUtils.tr("tst.common.shared.machine_info.compact_fusion_coil_tier");

        // #tr tst.common.shared.machine_info.wireless_mode_enabled
        // # Wireless mode enabled
        // #zh_CN 已启用无线模式
        public static final String Info_Wireless_mode_enabled = TSTUtils.tr("tst.common.shared.machine_info.wireless_mode_enabled");
        // spotless:on

        public static String glassTier(int glassTier) {
            // #tr tst.common.shared.machine_info.formatted.glass_tier
            // # {\AQUA}Glass Tier: {\GOLD}%s
            // #zh_CN {\AQUA}玻璃等级: {\GOLD}%s
            return TSTUtils.tr("tst.common.shared.machine_info.formatted.glass_tier", glassTier);
        }

        public static String coilTier(int coilTier) {
            // #tr tst.common.shared.machine_info.formatted.coil_tier
            // # {\AQUA}Coil Tier: {\GOLD}%s
            // #zh_CN {\AQUA}线圈等级: {\GOLD}%s
            return TSTUtils.tr("tst.common.shared.machine_info.formatted.coil_tier", coilTier);
        }

        public static String componentTier(int componentTier) {
            // #tr tst.common.shared.machine_info.formatted.component_tier
            // # {\AQUA}Component Tier: {\GOLD}%s
            // #zh_CN {\AQUA}部件等级: {\GOLD}%s
            return TSTUtils.tr("tst.common.shared.machine_info.formatted.component_tier", componentTier);
        }
    }

    public static class Waila {

        // #tr tst.common.shared.waila.wireless_mode
        // # Wireless Mode
        // #zh_CN 无线模式
        public static final String Waila_WirelessMode = TSTUtils.tr("tst.common.shared.waila.wireless_mode");

        // #tr tst.common.shared.waila.current_eu_cost
        // # Current EU Cost
        // #zh_CN 当前EU消耗
        public static final String Waila_CurrentEuCost = TSTUtils.tr("tst.common.shared.waila.current_eu_cost");
    }

    public static class Structure {

        // spotless:off
        // #tr tst.common.shared.structure.text_casing
        // # Casing
        // #zh_CN Casing
        public static final String textCasing = TSTUtils.tr("tst.common.shared.structure.text_casing");

        // #tr tst.common.shared.structure.text_use_blueprint
        // # Use {\BLUE}Blue{\AQUA}print{\RESET} to preview
        // #zh_CN 用{\BLUE}蓝{\AQUA}图{\RESET}预览
        public static final String textUseBlueprint = TSTUtils.tr("tst.common.shared.structure.text_use_blueprint");

        // #tr tst.common.shared.structure.text_colon
        // # :{\SPACE}
        // #zh_CN ：{\SPACE}
        public static final String textColon = TSTUtils.tr("tst.common.shared.structure.text_colon");

        // #tr tst.common.shared.structure.text_dot
        // # Dot
        // #zh_CN 提示方块
        public static final String textDot = TSTUtils.tr("tst.common.shared.structure.text_dot");

        public static String getBlueprintWithDot(int dot) {
            return textUseBlueprint + EnumChatFormatting.WHITE + " " + textDot + " : " + EnumChatFormatting.AQUA + dot;
        }

        // #tr tst.common.shared.structure.text_space
        // # {\SPACE}
        // #zh_CN {\SPACE}
        public static final String textSpace = TSTUtils.tr("tst.common.shared.structure.text_space");

        // #tr tst.common.shared.structure.text_any_casing
        // # Any Casing
        // #zh_CN 任意机械方块
        public static final String textAnyCasing = TSTUtils.tr("tst.common.shared.structure.text_any_casing");

        // #tr tst.common.shared.structure.text_top_center
        // # Top center
        // #zh_CN 顶层中央
        public static final String textTopCenter = TSTUtils.tr("tst.common.shared.structure.text_top_center");

        // #tr tst.common.shared.structure.text_front_center
        // # Front center
        // #zh_CN 正面中央
        public static final String textFrontCenter = TSTUtils.tr("tst.common.shared.structure.text_front_center");

        // #tr tst.common.shared.structure.text_front_bottom
        // # Front bottom
        // #zh_CN 正面底中
        public static final String textFrontBottom = TSTUtils.tr("tst.common.shared.structure.text_front_bottom");

        // #tr tst.common.shared.structure.text_center_of_lr_sides
        // # Center area of left and right side
        // #zh_CN 左右两侧的中央区域
        public static final String textCenterOfLRSides = TSTUtils.tr("tst.common.shared.structure.text_center_of_lr_sides");

        // #tr tst.common.shared.structure.text_center_of_ud_sides
        // # Center area of up and down side
        // #zh_CN 上下侧的中央区域
        public static final String textCenterOfUDSides = TSTUtils.tr("tst.common.shared.structure.text_center_of_ud_sides");

        // #tr tst.common.shared.structure.text_end_sides
        // # Machine end
        // #zh_CN 机器末端
        public static final String textEndSides = TSTUtils.tr("tst.common.shared.structure.text_end_sides");

        // #tr tst.common.shared.structure.text_casing_adv_ir_plated
        // # Advanced Iridium Plated Machine Casing
        // #zh_CN 强化镀铱机械方块
        public static final String textCasingAdvIrPlated = TSTUtils.tr("tst.common.shared.structure.text_casing_adv_ir_plated");

        // #tr tst.common.shared.structure.text_casing_tt_0
        // # High Power Casing
        // #zh_CN 超能机械方块
        public static final String textCasingTT_0 = TSTUtils.tr("tst.common.shared.structure.text_casing_tt_0");

        // #tr tst.common.shared.structure.text_around_controller
        // # Around the Controller
        // #zh_CN 控制器方块周围
        public static final String textAroundController = TSTUtils.tr("tst.common.shared.structure.text_around_controller");
        // spotless:on
    }

    public static class MachineTooltip {

        // spotless:off
        // #tr tst.common.shared.machine_tooltip.more_info_checking_in_scanner
        // # {\WHITE}Use scanner check controller block to get more information.
        // #zh_CN {\WHITE}使用三录仪查看主机获取更多信息.
        public static final String MoreInfoCheckingInScanner = TSTUtils.tr("tst.common.shared.machine_tooltip.more_info_checking_in_scanner");

        // #tr tst.common.shared.machine_tooltip.tooltip_details
        // # {\LIGHT_PURPLE}Details:
        // #zh_CN {\LIGHT_PURPLE}具体细节:
        public static final String Tooltip_Details = TSTUtils.tr("tst.common.shared.machine_tooltip.tooltip_details");

        // #tr tst.common.shared.machine_tooltip.tooltips_join_wireless_net_without_energy_hatch
        // # Joining the wireless EU network when without installing an energy hatch.
        // #zh_CN 未安装能源仓时自动进入无线电力网络模式.
        public static final String Tooltips_JoinWirelessNetWithoutEnergyHatch = TSTUtils.tr("tst.common.shared.machine_tooltip.tooltips_join_wireless_net_without_energy_hatch");

        // #tr tst.common.shared.machine_tooltip.tooltip_do_not_need_maintenance
        // # Do Not Need Maintenance!
        // #zh_CN 不需要维护仓!
        public static final String Tooltip_DoNotNeedMaintenance = TSTUtils.tr("tst.common.shared.machine_tooltip.tooltip_do_not_need_maintenance");

        // #tr tst.common.shared.machine_tooltip.tooltip_do_not_need_energy_hatch
        // # Do Not Need Energy Hatch!
        // #zh_CN 不需要能源仓!
        public static final String Tooltip_DoNotNeedEnergyHatch = TSTUtils.tr("tst.common.shared.machine_tooltip.tooltip_do_not_need_energy_hatch");

        // #tr tst.common.shared.machine_tooltip.mark_twist_space_technology_tec_tech
        // # {\AQUA}{\BOLD}Twist Space Technology : {\RESET}Tech
        // #zh_CN {\AQUA}{\BOLD}Twist Space Technology : {\RESET}{\BLUE}Tec{\DARK_BLUE}Tech
        public static final String Mark_TwistSpaceTechnology_TecTech = TSTUtils.tr("tst.common.shared.machine_tooltip.mark_twist_space_technology_tec_tech");

        // #tr tst.common.shared.machine_tooltip.text_screwdriver_change_mode
        // # Use screwdriver to change mode.
        // #zh_CN 使用螺丝刀切换模式.
        public static final String textScrewdriverChangeMode = TSTUtils.tr("tst.common.shared.machine_tooltip.text_screwdriver_change_mode");

        // #tr tst.common.shared.machine_tooltip.tooltip_glass_tier_limit_energy_hatch_tier
        // # The Glass Tier limit the Energy hatch voltage Tier.
        // #zh_CN 玻璃等级限制能源仓等级.
        public static final String Tooltip_GlassTierLimitEnergyHatchTier = TSTUtils.tr("tst.common.shared.machine_tooltip.tooltip_glass_tier_limit_energy_hatch_tier");

        // #tr tst.common.shared.machine_tooltip.out_of_maintenance
        // # {\RED}{\BOLD} OUT OF MAINTENANCE !
        // #zh_CN {\RED}{\BOLD}不再维护！
        public static final String OutOfMaintenance = TSTUtils.tr("tst.common.shared.machine_tooltip.out_of_maintenance");
        // spotless:on

        public static String tooComplex() {
            return TSTUtils.tr("GT5U.MBTT.Structure.Complex");
        }

        public static String temporaryController() {
            // #tr tst.common.shared.machine_tooltip.temporary_controller
            // # {\RED}Temporary controller; will be removed in the next update!
            // #zh_CN {\RED}临时控制器, 将在下个版本移除!
            return TSTUtils.tr("tst.common.shared.machine_tooltip.temporary_controller");
        }

        public static String replacementController() {
            // #tr tst.common.shared.machine_tooltip.replacement_controller
            // # {\RED}See NEI for the replacement controller and conversion recipes.
            // #zh_CN {\RED}请在NEI查看替代控制器及转换配方.
            return TSTUtils.tr("tst.common.shared.machine_tooltip.replacement_controller");
        }

    }

    public static class DysonSphere {

        // spotless:off
        // #tr tst.dyson.shared.rise_of_the_dark_fog
        // # {\BLACK}{\BOLD}Rise of the Dark Fog
        // #zh_CN {\BLACK}{\BOLD}黑雾崛起
        public static final String RiseOfDarkFog = TSTUtils.tr("tst.dyson.shared.rise_of_the_dark_fog");

        // #tr tst.dyson.shared.program_name
        // # {\BLUE}Dyson Sphere Program
        // #zh_CN {\BLUE}戴森球计划
        public static final String DSPName = TSTUtils.tr("tst.dyson.shared.program_name");

        // #tr tst.dyson.shared.tooltip.info.01
        // # Dyson Sphere Energy Credit = Solar Sail Output (default 524288) * Solar Sail amount * (Node + 1)^0.8
        // #zh_CN 戴森球产能点数 = 每太阳帆产出 (默认 524288) * 太阳帆数量 * (节点数量 + 1)^0.8
        public static final String Tooltip_DSPInfo_00 = TSTUtils.tr("tst.dyson.shared.tooltip.info.01");

        // #tr tst.dyson.shared.tooltip.info.02
        // # Every Node can absorb (default) 256 Solar Sails.
        // #zh_CN 每个节点可以吸附 (默认) 256 太阳帆.
        public static final String Tooltip_DSPInfo_01 = TSTUtils.tr("tst.dyson.shared.tooltip.info.02");

        // #tr tst.dyson.shared.tooltip.info.03
        // # If unabsorbed solar sails amount is larger than 2048,
        // #zh_CN 如果未吸附的太阳帆数量超过2048,
        public static final String Tooltip_DSPInfo_02 = TSTUtils.tr("tst.dyson.shared.tooltip.info.03");

        // #tr tst.dyson.shared.tooltip.info.04
        // # the excess may be destroyed.
        // #zh_CN  超过的部分可能会损毁.
        public static final String Tooltip_DSPInfo_03 = TSTUtils.tr("tst.dyson.shared.tooltip.info.04");

        // #tr tst.dyson.shared.tooltip.info.05
        // # Every 30 minutes has 1/5000 chance to decrease Solar Sail amount.
        // #zh_CN 每30 分钟有 1/5000 几率触发太阳帆损毁事件.
        public static final String Tooltip_DSPInfo_04 = TSTUtils.tr("tst.dyson.shared.tooltip.info.05");

        // #tr tst.dyson.shared.tooltip.info.06
        // # In decreasing, every Dyson Sphere has 1/4 chance to destroyed Solar Sail amount.
        // #zh_CN 在损毁事件里, 每个戴森球都有 1/4 几率损毁太阳帆.
        public static final String Tooltip_DSPInfo_05 = TSTUtils.tr("tst.dyson.shared.tooltip.info.06");

        // #tr tst.dyson.shared.tooltip.info.07
        // # The amount of destroyed is the excess' 1/2.
        // #zh_CN 损毁的数量是超过部分的 1/2 .
        public static final String Tooltip_DSPInfo_06 = TSTUtils.tr("tst.dyson.shared.tooltip.info.07");
        // spotless:on
    }

    public static class ModularizedMachine {

        // spotless:off
        // #tr tst.modular.shared.modularized_machine_system
        // # {\BLUE}{\BOLD}Modularized Machine System
        // #zh_CN {\BLUE}{\BOLD}模块化机械系统
        public static final String ModularizedMachineSystem = TSTUtils.tr("tst.modular.shared.modularized_machine_system");

        // #tr tst.modular.shared.installing_module_near_controller_improve_machine
        // # Installing module hatches near the controller block can significantly improve machine performance.
        // #zh_CN 在主机附近安装模块仓室可以显著提升机器性能.
        public static final String InstallingModuleNearControllerImproveMachine = TSTUtils.tr("tst.modular.shared.installing_module_near_controller_improve_machine");

        // #tr tst.modular.shared.modularized_machine_system_description_01
        // # Install Module Hatches to enhance your machine.
        // #zh_CN 安装模块仓室强化你的机器.
        public static final String ModularizedMachineSystemDescription01 = TSTUtils.tr("tst.modular.shared.modularized_machine_system_description_01");

        // #tr tst.modular.shared.modularized_machine_system_description_02
        // # The installable modules are :
        // #zh_CN 可安装的模块有 :
        public static final String ModularizedMachineSystemDescription02 = TSTUtils.tr("tst.modular.shared.modularized_machine_system_description_02");

        // #tr tst.modular.shared.overclock_controller_description
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}Overclock Controller Module - Set machine overclock type
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}超频控制器模块 - 设置机器的超频类型
        public static final String OverclockControllerDescription = TSTUtils.tr("tst.modular.shared.overclock_controller_description");

        // #tr tst.modular.shared.parallel_controller_description
        // # Parallel Controller Module - Increase parallelism of machine
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}并行控制器模块 - 提高机器的并行数
        public static final String ParallelControllerDescription = TSTUtils.tr("tst.modular.shared.parallel_controller_description");

        // #tr tst.modular.shared.power_consumption_controller_description
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE} Power Consumption Controller Module - Reduce machine energy consumption
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}耗能控制器模块 - 降低机器能耗
        public static final String PowerConsumptionControllerDescription = TSTUtils.tr("tst.modular.shared.power_consumption_controller_description");

        // #tr tst.modular.shared.speed_controller_description
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}Speed Controller Module - Increase processing speed of machine
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}速度控制器模块 - 提高机器运行速度
        public static final String SpeedControllerDescription = TSTUtils.tr("tst.modular.shared.speed_controller_description");

        // #tr tst.modular.shared.execution_core_description
        // # {\SPACE}{\SPACE}{\SPACE}{\SPACE} Execution Core Module - Adds actual running units to the machine, true parallelism
        // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}执行核心模块 - 为机器添加实际运行单元, 真正意义上的并行
        public static final String ExecutionCoreDescription = TSTUtils.tr("tst.modular.shared.execution_core_description");

        // #tr tst.modular.shared.not_multiply_install_same_type_module
        // # Same type of module hatch cannot be installed repeatedly. Except for Execution Cores.
        // #zh_CN 不可重复安装同类型的模块仓室. 除了执行核心模块.
        public static final String NotMultiplyInstallSameTypeModule = TSTUtils.tr("tst.modular.shared.not_multiply_install_same_type_module");

        // #tr tst.modular.shared.not_multiply_install_same_type_module_all
        // # Same type of module hatch cannot be installed repeatedly.
        // #zh_CN 不可重复安装同类型的模块仓室.
        public static final String NotMultiplyInstallSameTypeModuleAll = TSTUtils.tr("tst.modular.shared.not_multiply_install_same_type_module_all");

        // #tr tst.modular.shared.can_multiply_install_same_type_module
        // # Same type of module hatch can be installed repeatedly.
        // #zh_CN 可以重复安装同类型的模块仓室.
        public static final String CanMultiplyInstallSameTypeModule = TSTUtils.tr("tst.modular.shared.can_multiply_install_same_type_module");

        // #tr tst.modular.shared.modular_hatch
        // # Modular Hatch
        // #zh_CN 模块仓室
        public static final String ModularHatchKey = "tst.modular.shared.modular_hatch";
        public static final String ModularHatch = TSTUtils.tr(ModularHatchKey);
        // spotless:on
    }

    public static class Command {

        public static ChatComponentTranslation invalidCommand() {
            // #tr tst.common.shared.command.tst_command.invalid_command
            // # {\RED}Invalid command.
            // #zh_CN 无效指令.
            return new ChatComponentTranslation("tst.common.shared.command.tst_command.invalid_command");
        }

        public static ChatComponentTranslation formatError() {
            // #tr tst.common.shared.command.tst_command.input_format_error
            // # {\RED}Input format error, please check your inputs.
            // #zh_CN 输入格式错误, 请检查你的输入参数.
            return new ChatComponentTranslation("tst.common.shared.command.tst_command.input_format_error");
        }
    }

}
