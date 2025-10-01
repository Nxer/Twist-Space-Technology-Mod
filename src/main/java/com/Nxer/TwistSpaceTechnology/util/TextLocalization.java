package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.Tags;

import gregtech.api.util.GTLanguageManager;

// spotless:off

/**
 * Use #tr comments and {@link TstUtils#tr(String)}.
 * <p>See <a href="https://github.com/Nxer/Twist-Space-Technology-Mod/pull/284">This PR: Use a preprocessor to help complete the creation of language files</a>
 *
 * @deprecated You should put your localization texts inside the class where it is used, except the commonly used ones, see {@link TextEnums} and {@link TstSharedLocalization} for them.
 */
@Deprecated
public class TextLocalization {

    // region general
    public static final String ModName = Tags.MODNAME;
    // #tr ModNameDesc
    // # Added by {\GREEN}Twist Space Technology{\GRAY}
    // #zh_CN 由 {\GREEN}Twist Space Technology{\GRAY} 添加{\RESET}
    public static final String ModNameDesc = TextEnums.tr("ModNameDesc");

    // #tr HeatCapacity
    // # Heat Capacity:
    // #zh_CN 热容:
    public static final String HeatCapacity = TextEnums.tr("HeatCapacity");

    // #tr FluidCapacity
    // # Capacity:
    // #zh_CN 容量:
    public static final String FluidCapacity = TextEnums.tr("FluidCapacity");

    // #tr HatchTier
    // # Hatch Tier:
    // #zh_CN 仓室等级:
    public static final String HatchTier = TextEnums.tr("HatchTier");

    // #tr Kelvin
    // #  K
    // #zh_CN  K
    public static final String Kelvin = TextEnums.tr("Kelvin");

    // #tr AutoSeparation
    // # Automatically separate inputs
    // #zh_CN 自动隔离输入
    public static final String AutoSeparation= TextEnums.tr("AutoSeparation");

    // #tr Text_SeparatingLine
    // # {\GOLD}-----------------------------------------
    // #zh_CN {\GOLD}-----------------------------------------
    public static final String Text_SeparatingLine = TextEnums.tr("Text_SeparatingLine");

    // #tr BLUE_PRINT_INFO
    // # Follow the {\BLUE} Structure{\DARK_BLUE}Lib{\GRAY} hologram projector to build the main structure.
    // #zh_CN 请参考{\BLUE}Structure{\DARK_BLUE}Lib{\GRAY}全息投影，构建主体结构
    public static final String BLUE_PRINT_INFO = TextEnums.tr("BLUE_PRINT_INFO");

    // #tr DSPName
    // # {\BLUE}Dyson Sphere Program
    // #zh_CN {\BLUE}戴森球计划
    public static final String DSPName = TextEnums.tr("DSPName");

    // #tr Tooltip_Details
    // # {\LIGHT_PURPLE}Details:
    // #zh_CN {\LIGHT_PURPLE}具体细节:
    public static final String Tooltip_Details = TextEnums.tr("Tooltip_Details");

    // #tr Tooltips_JoinWirelessNetWithoutEnergyHatch
    // # Joining the wireless EU network when without installing an energy hatch.
    // #zh_CN 未安装能源仓时自动进入无线电力网络模式.
    public static final String Tooltips_JoinWirelessNetWithoutEnergyHatch = TextEnums.tr("Tooltips_JoinWirelessNetWithoutEnergyHatch");

    public static final String mNoMobsToolTip = GTLanguageManager
        .addStringLocalization("gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block");
    public static final String mNoTileEntityToolTip = GTLanguageManager
        .addStringLocalization("gt.notileentityinthisblock", "This is NOT a TileEntity!");
    // endregion

    // region Waila

    // #tr Waila.General.WirelessMode
    // # Wireless Mode
    // #zh_CN 无线模式
    public static final String Waila_WirelessMode = TextEnums.tr("Waila.General.WirelessMode");

    // #tr Waila.General.CurrentEuCost
    // # Current EU Cost
    // #zh_CN 当前EU消耗
    public static final String Waila_CurrentEuCost = TextEnums.tr("Waila.General.CurrentEuCost");

    // endregion

    // region getInfoData

    // #tr General.getInfoData.Wireless_mode_enabled
    // # Wireless mode enabled
    // #zh_CN 已启用无线模式
    public static final String Info_Wireless_mode_enabled = TextEnums.tr("General.getInfoData.Wireless_mode_enabled");

    // endregion

    // region Names
    public static final String name_Nxer = "" + EnumChatFormatting.RED
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "N"
        + EnumChatFormatting.GREEN
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "x"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "e"
        + EnumChatFormatting.BLUE
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "r";

    public static final String authorName_Nxer = "Author: " + name_Nxer;
    // endregion

    // region special hatch info

    // #tr Tooltip_DoNotNeedMaintenance
    // # Do Not Need Maintenance!
    // #zh_CN 不需要维护仓!
    public static final String Tooltip_DoNotNeedMaintenance = TextEnums.tr("Tooltip_DoNotNeedMaintenance");

    // #tr Tooltip_DoNotNeedEnergyHatch
    // # Do Not Need Energy Hatch!
    // #zh_CN 不需要能源仓!
    public static final String Tooltip_DoNotNeedEnergyHatch = TextEnums.tr("Tooltip_DoNotNeedEnergyHatch");

    // #tr Mark_TwistSpaceTechnology_TecTech
    // # {\AQUA}{\BOLD}Twist Space Technology : {\RESET}Tech
    // #zh_CN {\AQUA}{\BOLD}Twist Space Technology : {\RESET}{\BLUE}Tec{\DARK_BLUE}Tech
    public static final String Mark_TwistSpaceTechnology_TecTech = TextEnums.tr("Mark_TwistSpaceTechnology_TecTech");

    // endregion

    // region get info message
    // #tr infoText_CurrentStellarCoefficient
    // # Current Stellar Coefficient:
    // #zh_CN 当前恒星系数:
    public static final String infoText_CurrentStellarCoefficient = TextEnums.tr("infoText_CurrentStellarCoefficient");

    // #tr infoText_CurrentPlanetCoefficient
    // # Current Planet Coefficient:
    // #zh_CN 当前行星系数:
    public static final String infoText_CurrentPlanetCoefficient = TextEnums.tr("infoText_CurrentPlanetCoefficient");

    // endregion

    // region casing

    // #tr textCasing
    // # Casing
    // #zh_CN Casing
    public static final String textCasing = TextEnums.tr("textCasing");

    // #tr textUseBlueprint
    // # Use {\BLUE}Blue{\AQUA}print{\RESET} to preview
    // #zh_CN 用{\BLUE}蓝{\AQUA}图{\RESET}预览
    public static final String textUseBlueprint = TextEnums.tr("textUseBlueprint");

    // #tr textColon
    // # :{\SPACE}
    // #zh_CN ：{\SPACE}
    public static final String textColon = TextEnums.tr("textColon");
    public static String getBlueprintWithDot(int dot){
        // #tr textDot
        // # Dot
        // #zh_CN 提示方块
        return textUseBlueprint + EnumChatFormatting.WHITE + " " + TextEnums.tr("textDot") + " : " + EnumChatFormatting.AQUA+dot;
    }

    // #tr textSpace
    // # {\SPACE}
    // #zh_CN {\SPACE}
    public static final String textSpace = TextEnums.tr("textSpace");

    // #tr textAnyCasing
    // # Any Casing
    // #zh_CN 任意机械方块
    public static final String textAnyCasing = TextEnums.tr("textAnyCasing");

    // #tr textTopCenter
    // # Top center
    // #zh_CN 顶层中央
    public static final String textTopCenter = TextEnums.tr("textTopCenter");

    // #tr textFrontCenter
    // # Front center
    // #zh_CN 正面中央
    public static final String textFrontCenter = TextEnums.tr("textFrontCenter");

    // #tr textFrontBottom
    // # Front bottom
    // #zh_CN 正面底中
    public static final String textFrontBottom = TextEnums.tr("textFrontBottom");

    // #tr textCenterOfLRSides
    // # Center area of left and right side
    // #zh_CN 左右两侧的中央区域
    public static final String textCenterOfLRSides = TextEnums.tr("textCenterOfLRSides");

    // #tr textCenterOfUDSides
    // # Center area of up and down side
    // #zh_CN 上下侧的中央区域
    public static final String textCenterOfUDSides = TextEnums.tr("textCenterOfUDSides");

    // #tr textEndSides
    // # Machine end
    // #zh_CN 机器末端
    public static final String textEndSides = TextEnums.tr("textEndSides");

    // #tr StructureTooComplex
    // # The structure is too complex!
    // #zh_CN 结构太复杂了！
    public static final String StructureTooComplex = TextEnums.tr("StructureTooComplex");

    // #tr textCasingAdvIrPlated
    // # Advanced Iridium Plated Machine Casing
    // #zh_CN 强化镀铱机械方块
    public static final String textCasingAdvIrPlated = TextEnums.tr("textCasingAdvIrPlated");

    // #tr textCasingTT_0
    // # High Power Casing
    // #zh_CN 超能机械方块
    public static final String textCasingTT_0 = TextEnums.tr("textCasingTT_0");

    // #tr textAroundController
    // # Around the Controller
    // #zh_CN 控制器方块周围
    public static final String textAroundController = TextEnums.tr("textAroundController");

    // #tr textScrewdriverChangeMode
    // # Use screwdriver to change mode.
    // #zh_CN 使用螺丝刀切换模式.
    public static final String textScrewdriverChangeMode = TextEnums.tr("textScrewdriverChangeMode");

    // endregion

    // region general tooltips

    // #tr Tooltip_GlassTierLimitEnergyHatchTier
    // # The Glass Tier limit the Energy hatch voltage Tier.
    // #zh_CN 玻璃等级限制能源仓等级.
    public static final String Tooltip_GlassTierLimitEnergyHatchTier = TextEnums.tr("Tooltip_GlassTierLimitEnergyHatchTier");


    // endregion

    // region Intensify Chemical Distorter text localization

    // #tr NameIntensifyChemicalDistorter
    // # Intensify Chemical Distorter
    // #zh_CN 深度化学扭曲仪
    public static final String NameIntensifyChemicalDistorter = TextEnums.tr("NameIntensifyChemicalDistorter");

    // #tr Tooltip_ICD_MachineType
    // # Intensify Chemical Distorter/Chemical Reactor
    // #zh_CN 深度化学扭曲仪/化学反应釜
    public static final String Tooltip_ICD_MachineType = TextEnums.tr("Tooltip_ICD_MachineType");

    // #tr Tooltip_ICD_00
    // # Controller block for the Intensify Chemical Distorter
    // #zh_CN 深度化学扭曲仪的控制器方块
    public static final String Tooltip_ICD_00 = TextEnums.tr("Tooltip_ICD_00");

    // #tr Tooltip_ICD_01
    // # {\AQUA}I! {\BLUE}AM! {\AQUA}THE! {\BLUE}CHEM! {\AQUA}THAT! {\BLUE}IS! {\AQUA}APPROOOOOACHING !!
    // #zh_CN {\AQUA}I! {\BLUE}AM! {\AQUA}THE! {\BLUE}CHEM! {\AQUA}THAT! {\BLUE}IS! {\AQUA}APPROOOOOACHING !!
    public static final String Tooltip_ICD_01 = TextEnums.tr("Tooltip_ICD_01");

    // #tr Tooltip_ICD_02
    // # The most advanced base chemical reactor.
    // #zh_CN 最先进的基础化学反应设备
    public static final String Tooltip_ICD_02 = TextEnums.tr("Tooltip_ICD_02");

    // #tr Tooltip_ICD_03
    // # Use screwdriver to change mode.
    // #zh_CN 使用螺丝刀切换模式.
    public static final String Tooltip_ICD_03 = textScrewdriverChangeMode;

    // #tr Tooltip_ICD_04
    // # {\GOLD}Intensify Chemical Distorter mode:
    // #zh_CN {\GOLD}深度化学扭曲模式:
    public static final String Tooltip_ICD_04 = TextEnums.tr("Tooltip_ICD_04");

    // #tr Tooltip_ICD_05
    // # Focus on processing the most complex chemical reaction - {\AQUA}16x {\GRAY}Parallel.
    // #zh_CN 专注于处理更复杂的化学反应 - {\AQUA}16x {\GRAY}并行
    public static final String Tooltip_ICD_05 = TextEnums.tr("Tooltip_ICD_05");

    // #tr Tooltip_ICD_06
    // # {\GOLD}Chemical Reactor mode:
    // #zh_CN {\GOLD}化学反应釜模式:
    public static final String Tooltip_ICD_06 = TextEnums.tr("Tooltip_ICD_06");

    // #tr Tooltip_ICD_07
    // # {\AQUA}1024x {\GRAY}Parallel and {\RED}900% {\GRAY}faster than using LCR of the same voltage.
    // #zh_CN 拥有 {\AQUA}1024x{\GRAY} 并行并且比相同电压的大型化学反应釜快 {\RED}900%{\GRAY}
    public static final String Tooltip_ICD_07 = TextEnums.tr("Tooltip_ICD_07");


    // endregion

    // region Precise High-Energy Photonic Quantum Master text localization

    // #tr NamePreciseHighEnergyPhotonicQuantumMaster
    // # Precise High-Energy Photonic Quantum Master
    // #zh_CN 精密高能光量子掌控者
    public static final String NamePreciseHighEnergyPhotonicQuantumMaster = TextEnums.tr("NamePreciseHighEnergyPhotonicQuantumMaster");


    // #tr Tooltip_PhC_MachineType
    // # Photon Controller/Laser Engraver
    // #zh_CN 光子掌控者/激光蚀刻机
    public static final String Tooltip_PhC_MachineType = TextEnums.tr("Tooltip_PhC_MachineType");

    // #tr Tooltip_PhC_00
    // # Controller block for the Precise High-Energy Photonic Quantum Master
    // #zh_CN 精密高能光量子掌控者的控制器方块
    public static final String Tooltip_PhC_00 = TextEnums.tr("Tooltip_PhC_00");

    // #tr Tooltip_PhC_01
    // # {\BLUE}Prism tank in order, sir.
    // #zh_CN {\BLUE}Prism tank in order, sir.
    public static final String Tooltip_PhC_01 = TextEnums.tr("Tooltip_PhC_01");

    // #tr Tooltip_PhC_02
    // # Control Photons on the scale of 10⁻² meters.
    // #zh_CN 在10¯¹² m 尺度上掌控光子.
    public static final String Tooltip_PhC_02 = TextEnums.tr("Tooltip_PhC_02");

    // #tr Tooltip_PhC_03
    // # Install Photonic Intensifier on the back side of the structure to dramatically increase production speeds.
    // #zh_CN 可以在机器背面对应位置安装光量子增幅器,大幅提高处理速度.
    public static final String Tooltip_PhC_03 = TextEnums.tr("Tooltip_PhC_03");

    // #tr Tooltip_PhC_04
    // # Multi upgrade modules can be stacked. Also can be uninstalled. Replace using normal Casing.
    // #zh_CN 多个光量子增幅器效果可以叠加,当然也可以不安装,使用强化镀铱机械方块占位.
    public static final String Tooltip_PhC_04 = TextEnums.tr("Tooltip_PhC_04");

    // #tr Tooltip_PhC_05
    // # Use screwdriver to change mode.
    // #zh_CN 使用螺丝刀切换模式.
    public static final String Tooltip_PhC_05 = textScrewdriverChangeMode;

    // #tr Tooltip_PhC_06
    // # {\GOLD}Photon Controller mode:
    // #zh_CN {\GOLD}光子掌控者模式:
    public static final String Tooltip_PhC_06 = TextEnums.tr("Tooltip_PhC_06");

    // #tr Tooltip_PhC_07
    // # {\AQUA}16x{\GRAY} Parallel
    // #zh_CN {\AQUA}16x{\GRAY} 并行
    public static final String Tooltip_PhC_07 = TextEnums.tr("Tooltip_PhC_07");

    // #tr Tooltip_PhC_08
    // # {\GOLD}Laser Engraver mode:
    // #zh_CN {\GOLD}激光蚀刻机模式:
    public static final String Tooltip_PhC_08 = TextEnums.tr("Tooltip_PhC_08");

    // #tr Tooltip_PhC_09
    // # {\AQUA}256x{\GRAY} Parallel | Extra reduce {\RED}50%{\GRAY} recipe time spent
    // #zh_CN {\AQUA}256x{\GRAY} 并行 | 额外降低{\RED}50%{\GRAY}耗时
    public static final String Tooltip_PhC_09 = TextEnums.tr("Tooltip_PhC_09");

    // #tr textHighPowerCasingUDSides
    // # High Power Casing area of up and down side
    // #zh_CN 上下两侧的超能机械方块区域
    public static final String textHighPowerCasingUDSides = TextEnums.tr("textHighPowerCasingUDSides");

    // #tr textUpgradeCasingAndLocation
    // # Upgrade module casing at backside area wrapped by AdvIrPlated Casing
    // #zh_CN Upgrade module casing at backside area wrapped by AdvIrPlated Casing
    public static final String textUpgradeCasingAndLocation = TextEnums.tr("textUpgradeCasing");

    // #tr PhotonControllerUpgradeLV.tooltips.01
    // # Extra {\RED}1%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}1%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_LV = new String[]{TextEnums.tr("PhotonControllerUpgradeLV.tooltips.01")};

    // #tr PhotonControllerUpgradeMV.tooltips.01
    // # Extra {\RED}2%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}2%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_MV = new String[]{TextEnums.tr("PhotonControllerUpgradeMV.tooltips.01")};

    // #tr PhotonControllerUpgradeHV.tooltips.01
    // # Extra {\RED}3%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}3%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_HV = new String[]{TextEnums.tr("PhotonControllerUpgradeHV.tooltips.01")};

    // #tr PhotonControllerUpgradeEV.tooltips.01
    // # Extra {\RED}4%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}4%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_EV = new String[]{TextEnums.tr("PhotonControllerUpgradeEV.tooltips.01")};

    // #tr PhotonControllerUpgradeIV.tooltips.01
    // # Extra {\RED}5%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}5%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_IV = new String[]{TextEnums.tr("PhotonControllerUpgradeIV.tooltips.01")};

    // #tr PhotonControllerUpgradeLuV.tooltips.01
    // # Extra {\RED}10%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}10%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_LuV = new String[]{TextEnums.tr("PhotonControllerUpgradeLuV.tooltips.01")};

    // #tr PhotonControllerUpgradeZPM.tooltips.01
    // # Extra {\RED}20%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}20%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_ZPM = new String[]{TextEnums.tr("PhotonControllerUpgradeZPM.tooltips.01")};

    // #tr PhotonControllerUpgradeUV.tooltips.01
    // # Extra {\RED}40%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}40%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_UV = new String[]{TextEnums.tr("PhotonControllerUpgradeUV.tooltips.01")};

    // #tr PhotonControllerUpgradeUHV.tooltips.01
    // # Extra {\RED}70%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}70%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_UHV = new String[]{TextEnums.tr("PhotonControllerUpgradeUHV.tooltips.01")};

    // #tr PhotonControllerUpgradeUEV.tooltips.01
    // # Extra {\RED}100%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}100%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_UEV = new String[]{TextEnums.tr("PhotonControllerUpgradeUEV.tooltips.01")};

    // #tr PhotonControllerUpgradeUiV.tooltips.01
    // # Extra {\RED}140%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}140%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_UIV = new String[]{TextEnums.tr("PhotonControllerUpgradeUiV.tooltips.01")};

    // #tr PhotonControllerUpgradeUMV.tooltips.01
    // # Extra {\RED}190%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}190%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_UMV = new String[]{TextEnums.tr("PhotonControllerUpgradeUMV.tooltips.01"),
        TextEnums.tr("PhotonControllerUpgradeUMV.tooltips.02")};

    // #tr PhotonControllerUpgradeUXV.tooltips.01
    // # Extra {\RED}250%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}250%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_UXV = new String[]{TextEnums.tr("PhotonControllerUpgradeUXV.tooltips.01"),
        TextEnums.tr("PhotonControllerUpgradeUXV.tooltips.02")};

    // #tr PhotonControllerUpgradeMAX.tooltips.01
    // # Extra {\RED}320%{\GRAY} Speed Up !
    // #zh_CN 额外{\RED}320%{\GRAY}的速度提升！
    public static final String[] Tooltips_Upgrades_MAX = new String[]{TextEnums.tr("PhotonControllerUpgradeMAX.tooltips.01"),
        TextEnums.tr("PhotonControllerUpgradeMAX.tooltips.02")};


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
    public static final String[] Tooltips_HighPowerRadiationProofCasing = new String[]{
        // #tr Tooltips_HighPowerRadiationProofCasing.01
        // # Constrained the overflowing energy
        // #zh_CN {\GREEN}约束住了外溢的能量{\GRAY}.
        TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.01"),

        // #tr Tooltips_HighPowerRadiationProofCasing.02
        // # The power levels are still rising!!!
        // #zh_CN {\BOLD}{\AQUA}能量仍在上升!!!
        TextEnums.tr("Tooltips_HighPowerRadiationProofCasing.02"),
    };

    public static  final String[] Tooltips_AdvancedHighPowerCoil = new String[]{
        // #tr Tooltips_AdvancedHighPowerCoil.01
        // # Constrained the overflowing energy
        // #zh_CN {\BLUE}电流涌动于其中.
        TextEnums.tr("Tooltips_AdvancedHighPowerCoil.01"),

        // #tr Tooltips_AdvancedHighPowerCoil.02
        // # The power levels are still rising!!!
        // #zh_CN {\AQUA}目中闪烁雷霆.
        TextEnums.tr("Tooltips_AdvancedHighPowerCoil.02"),
    };



    // endregion

    // region MiracleTop

    // #tr NameMiracleTop
    // # Miracle Top
    // #zh_CN 奇迹顶点
    public static final String NameMiracleTop = TextEnums.tr("NameMiracleTop");

    // #tr Tooltip_MiracleTop_MachineType
    // # Circuit Assembler/Gravitation Breaker
    // #zh_CN 电路组装机/引力驱使核心
    public static final String Tooltip_MiracleTop_MachineType = TextEnums.tr("Tooltip_MiracleTop_MachineType");

    // #tr Tooltip_MiracleTop_00
    // # Controller block for the Miracle Top.
    // #zh_CN 奇迹顶点的控制器方块.
    public static final String Tooltip_MiracleTop_00 = TextEnums.tr("Tooltip_MiracleTop_00");

    // #tr Tooltip_MiracleTop_01
    // # {\LIGHT_PURPLE}I never think about the future because it will come sooner or later.
    // #zh_CN {\LIGHT_PURPLE}我从不思考未来，因为未来迟早会来.
    public static final String Tooltip_MiracleTop_01 = TextEnums.tr("Tooltip_MiracleTop_01");

    // #tr Tooltip_MiracleTop_02
    // # For absolute precision and efficiency, please abandon traditional manufacturing methods.
    // #zh_CN 为了绝对的精准和高效，请放弃传统的制造思路.
    public static final String Tooltip_MiracleTop_02 = TextEnums.tr("Tooltip_MiracleTop_02");

    // #tr Tooltip_MiracleTop_03
    // # The machine consists of a ring section and a conveying section.
    // #zh_CN 整个机器由环部分和传输部分组成.
    public static final String Tooltip_MiracleTop_03 = TextEnums.tr("Tooltip_MiracleTop_03");

    // #tr Tooltip_MiracleTop_04
    // # The number of rings is variable:  Maximum {\GOLD}16{\GRAY} rings, Minimum {\GOLD}2{\GRAY} rings(the first and the last).
    // #zh_CN 环的数量是可变的:  最多{\GOLD}16{\GRAY}环, 最少{\GOLD}2{\GRAY}环(第一个环和最后一个环).
    public static final String Tooltip_MiracleTop_04 = TextEnums.tr("Tooltip_MiracleTop_04");

    // #tr Tooltip_MiracleTop_05
    // # Total speed multiplier is equal to {\RED}400%{\GRAY} x num of rings.
    // #zh_CN 速度倍率 = 环数 x {\RED}400%{\GRAY}.
    public static final String Tooltip_MiracleTop_05 = TextEnums.tr("Tooltip_MiracleTop_05");

    // #tr Tooltip_MiracleTop_06
    // # Enable Perfect overclock when num of rings >= {\GOLD}8{\GRAY}.
    // #zh_CN 环数大于等于{\RED}8{\GRAY}时开启无损超频.
    public static final String Tooltip_MiracleTop_06 = TextEnums.tr("Tooltip_MiracleTop_06");

    // #tr Tooltip_MiracleTop_07
    // # {\AQUA}128x{\GRAY} Parallel per Ring.
    // #zh_CN 每环 {\AQUA}128x{\GRAY} 并行.
    public static final String Tooltip_MiracleTop_07 = TextEnums.tr("Tooltip_MiracleTop_07");

    // #tr textMiracleTopHatchLocation
    // # Outermost 12 blocks on the ring (outermost 3 on each side).
    // #zh_CN 环上最外侧的12个方块(每侧最外边3个).
    public static final String textMiracleTopHatchLocation = TextEnums.tr("textMiracleTopHatchLocation");

    // endregion

    // region Magnetic Drive Pressure Former

    // #tr NameMagneticDrivePressureFormer
    // # Magnetic Drive Pressure Former
    // #zh_CN 磁驱压力成型机
    public static final String NameMagneticDrivePressureFormer = TextEnums.tr("NameMagneticDrivePressureFormer");

    // #tr Tooltip_MagneticDrivePressureFormer_MachineType
    // # Extruder | Bending Machine | Forming Press | Forge Hammer
    // #zh_CN 压模机 | 卷板机 | 冲压机床 | 锻造锤
    public static final String Tooltip_MagneticDrivePressureFormer_MachineType = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_MachineType");

    // #tr Tooltip_MagneticDrivePressureFormer_00
    // # Controller block for the Magnetic Drive Pressure Former.
    // #zh_CN 磁驱压力成型机的控制器方块
    public static final String Tooltip_MagneticDrivePressureFormer_00 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_00");

    // #tr Tooltip_MagneticDrivePressureFormer_01
    // # {\AQUA}Simple applications of Maxwell's equations.
    // #zh_CN {\AQUA}麦克斯韦方程的简单应用.
    public static final String Tooltip_MagneticDrivePressureFormer_01 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_01");

    // #tr Tooltip_MagneticDrivePressureFormer_02
    // # No difficulty ! No hurry !
    // #zh_CN 轻而易举, 从容不迫！
    public static final String Tooltip_MagneticDrivePressureFormer_02 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_02");

    // #tr Tooltip_MagneticDrivePressureFormer_03
    // # {\GOLD}Extruder Mode:
    // #zh_CN {\GOLD}压模机模式:
    public static final String Tooltip_MagneticDrivePressureFormer_03 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_03");

    // #tr Tooltip_MagneticDrivePressureFormer_04
    // # {\RED}700%{\GRAY} faster than normal | Infinity Coil+ enable Perfect Overclock
    // #zh_CN 8倍速 | 无尽线圈解锁无损超频
    public static final String Tooltip_MagneticDrivePressureFormer_04 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_04");

    // #tr Tooltip_MagneticDrivePressureFormer_05
    // # {\GOLD}Bending and Forming Press and Forge Hammer Mode:
    // #zh_CN {\GOLD}卷板机|冲压机床|锻造锤模式:
    public static final String Tooltip_MagneticDrivePressureFormer_05 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_05");

    // #tr Tooltip_MagneticDrivePressureFormer_06
    // # {\RED}1500%{\GRAY} faster than normal | Enable Perfect Overclock
    // #zh_CN 16倍速 | 无损超频
    public static final String Tooltip_MagneticDrivePressureFormer_06 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_06");

    // #tr Tooltip_MagneticDrivePressureFormer_07
    // # Extra {\RED}+100%{\GRAY} speed multiplier per Coil Level.
    // #zh_CN 线圈等级每提高1级, 额外加速{\RED}100%{\GRAY}, 线圈加速部分独立计算.
    public static final String Tooltip_MagneticDrivePressureFormer_07 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_07");

    // #tr Tooltip_MagneticDrivePressureFormer_08
    // # Need Infinity Glass to use Laser energy hatch.
    // #zh_CN 无尽强化硼玻璃解锁激光仓.
    public static final String Tooltip_MagneticDrivePressureFormer_08 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_08");

    // #tr Tooltip_MagneticDrivePressureFormer_09
    // # {\AQUA}1024x{\GRAY} Parallel.
    // #zh_CN {\AQUA}1024x{\GRAY} 并行.
    public static final String Tooltip_MagneticDrivePressureFormer_09 = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_09");

    // #tr Tooltip_MagneticDrivePressureFormer_Hatches
    // # Frame location, Osmiridium Casing.
    // #zh_CN 框架位置, 铱锇机械方块.
    public static final String Tooltip_MagneticDrivePressureFormer_Hatches = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_Hatches");

    // #tr Tooltip_MagneticDrivePressureFormer_EnergyHatch
    // # The white, Iridium Casing, and the bottom center.
    // #zh_CN 铱强化机械方块, 和机器底层中心.
    public static final String Tooltip_MagneticDrivePressureFormer_EnergyHatch = TextEnums.tr("Tooltip_MagneticDrivePressureFormer_EnergyHatch");

    // endregion

    // region Physical Form Switcher

    // #tr NamePhysicalFormSwitcher
    // # Physical Form Switcher
    // #zh_CN 物质形态转换器
    public static final String NamePhysicalFormSwitcher = TextEnums.tr("NamePhysicalFormSwitcher");

    // #tr Tooltip_PhysicalFormSwitcher_MachineType
    // # Fluid Solidifier | Fluid Extractor
    // #zh_CN 流体固化器 | 流体提取机
    public static final String Tooltip_PhysicalFormSwitcher_MachineType = TextEnums.tr("Tooltip_PhysicalFormSwitcher_MachineType");

    // #tr Tooltip_PhysicalFormSwitcher_00
    // # Controller block for the Physical Form Switcher
    // #zh_CN 物质形态转换器的控制器方块
    public static final String Tooltip_PhysicalFormSwitcher_00 = TextEnums.tr("Tooltip_PhysicalFormSwitcher_00");

    // #tr Tooltip_PhysicalFormSwitcher_01
    // # {\YELLOW}Forming Master !
    // #zh_CN {\YELLOW}体态多端！
    public static final String Tooltip_PhysicalFormSwitcher_01 = TextEnums.tr("Tooltip_PhysicalFormSwitcher_01");

    // #tr Tooltip_PhysicalFormSwitcher_02
    // # The ultimate method of melt operation.
    // #zh_CN 熔体操作的最终手段.
    public static final String Tooltip_PhysicalFormSwitcher_02 = TextEnums.tr("Tooltip_PhysicalFormSwitcher_02");

    // #tr Tooltip_PhysicalFormSwitcher_03
    // # Has parallel equivalent to Perfect Overclock.
    // #zh_CN 拥有与无损超频等效的并行(但有损超频).
    public static final String Tooltip_PhysicalFormSwitcher_03 = TextEnums.tr("Tooltip_PhysicalFormSwitcher_03");

    // #tr Tooltip_PhysicalFormSwitcher_04
    // # Additional {\RED}10%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
    // #zh_CN 电压每提高1级, 额外降低{\RED}10%{\GRAY}配方耗时, 叠乘计算.
    public static final String Tooltip_PhysicalFormSwitcher_04 = TextEnums.tr("Tooltip_PhysicalFormSwitcher_04");

    // #tr Tooltip_PhysicalFormSwitcher_05
    // # The Glass Tier limit the recipe voltage tier.
    // #zh_CN 玻璃等级限制可执行配方等级.
    public static final String Tooltip_PhysicalFormSwitcher_05 = TextEnums.tr("Tooltip_PhysicalFormSwitcher_05");


    // endregion

    // region Magnetic Mixer

    // #tr NameMagneticMixer
    // # "Mini" Magnetic Mixer
    // #zh_CN "小型"磁力搅拌机
    public static final String NameMagneticMixer = TextEnums.tr("NameMagneticMixer");

    // #tr Tooltip_MagneticMixer_MachineType
    // # Mixer
    // #zh_CN 搅拌机
    public static final String Tooltip_MagneticMixer_MachineType = TextEnums.tr("Tooltip_MagneticMixer_MachineType");

    // #tr Tooltip_MagneticMixer_00
    // # Controller block for the "Mini" Magnetic Mixer
    // #zh_CN "小型"磁力搅拌机的控制器方块
    public static final String Tooltip_MagneticMixer_00 = TextEnums.tr("Tooltip_MagneticMixer_00");

    // #tr Tooltip_MagneticMixer_01
    // # {\RED}Watch out for the Bumps !
    // #zh_CN {\RED}我叫磁力棒！
    public static final String Tooltip_MagneticMixer_01 = TextEnums.tr("Tooltip_MagneticMixer_01");

    // #tr Tooltip_MagneticMixer_02
    // # Looks more like a tumble washing machine.
    // #zh_CN 看起来更像一个滚筒洗衣机.
    public static final String Tooltip_MagneticMixer_02 = TextEnums.tr("Tooltip_MagneticMixer_02");

    // #tr Tooltip_MagneticMixer_03
    // # Has parallel equivalent to Perfect Overclock.
    // #zh_CN 拥有与无损超频等效的并行(但有损超频).
    public static final String Tooltip_MagneticMixer_03 = TextEnums.tr("Tooltip_MagneticMixer_03");

    // #tr Tooltip_MagneticMixer_04
    // # Additional {\RED}20%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
    // #zh_CN 电压每提高1级, 额外降低{\RED}20%{\GRAY}配方耗时, 叠乘计算.
    public static final String Tooltip_MagneticMixer_04 = TextEnums.tr("Tooltip_MagneticMixer_04");

    // endregion

    // region MagneticDomainConstructor

    // #tr NameMagneticDomainConstructor
    // # Magnetic Domain Constructor
    // #zh_CN 磁畴构建器
    public static final String NameMagneticDomainConstructor = TextEnums.tr("NameMagneticDomainConstructor");

    // #tr Tooltip_MagneticDomainConstructor_MachineType
    // # Electromagnetic Separator | Electromagnetic Polarizer
    // #zh_CN 电磁离析机 | 磁化机
    public static final String Tooltip_MagneticDomainConstructor_MachineType = TextEnums.tr("Tooltip_MagneticDomainConstructor_MachineType");

    // #tr Tooltip_MagneticDomainConstructor_00
    // # Controller block for the Magnetic Domain Constructor
    // #zh_CN 磁畴构建器的控制器方块
    public static final String Tooltip_MagneticDomainConstructor_00 = TextEnums.tr("Tooltip_MagneticDomainConstructor_00");

    // #tr Tooltip_MagneticDomainConstructor_01
    // # {\DARK_GRAY}Don't give up your imagination.
    // #zh_CN {\DARK_GRAY}不要放弃你的幻想.
    public static final String Tooltip_MagneticDomainConstructor_01 = TextEnums.tr("Tooltip_MagneticDomainConstructor_01");

    // #tr Tooltip_MagneticDomainConstructor_02
    // # Controlling the magnetic domains inside the crystal, yes that's it.
    // #zh_CN 操控晶体内部的磁畴子, 就是这样.
    public static final String Tooltip_MagneticDomainConstructor_02 = TextEnums.tr("Tooltip_MagneticDomainConstructor_02");

    // #tr Tooltip_MagneticDomainConstructor_03
    // # {\AQUA}64x{\GRAY} Parallel per Ring.(Don't use a lot of blueprints when first scanning.)
    // #zh_CN 每环增加{\AQUA}64x{\GRAY}并行.(不要一开始就用很多{\BLUE}蓝{\AQUA}图{\GRAY}去扫描.)
    public static final String Tooltip_MagneticDomainConstructor_03 = TextEnums.tr("Tooltip_MagneticDomainConstructor_03");

    // #tr Tooltip_MagneticDomainConstructor_04
    // # Additional {\RED}25%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
    // #zh_CN 电压每提高1级, 额外降低{\RED}25%{\GRAY}配方耗时, 叠乘计算.
    public static final String Tooltip_MagneticDomainConstructor_04 = TextEnums.tr("Tooltip_MagneticDomainConstructor_04");

    // endregion

    // region Silksong

    // #tr NameSilksong
    // # Silksong
    // #zh_CN 丝之歌
    public static final String NameSilksong = TextEnums.tr("NameSilksong");

    // #tr Tooltip_Silksong_MachineType
    // # Wiremill
    // #zh_CN 线材轧机
    public static final String Tooltip_Silksong_MachineType = TextEnums.tr("Tooltip_Silksong_MachineType");

    // #tr Tooltip_Silksong_00
    // # Controller block for the Silksong
    // #zh_CN 丝之歌的控制器方块
    public static final String Tooltip_Silksong_00 = TextEnums.tr("Tooltip_Silksong_00");

    // #tr Tooltip_Silksong_01
    // # {\WHITE}Maybe dreams aren't such a good thing ......
    // #zh_CN {\WHITE}也许梦想并不是那么好的东西 ......
    public static final String Tooltip_Silksong_01 = TextEnums.tr("Tooltip_Silksong_01");

    // #tr Tooltip_Silksong_02
    // # Endless cables spew from this machine.
    // #zh_CN 无穷无尽的导线从这里喷薄而出.
    public static final String Tooltip_Silksong_02 = TextEnums.tr("Tooltip_Silksong_02");

    // #tr Tooltip_Silksong_03
    // # Parallel = {\AQUA}32 × piece × coil tier{\GRAY}.
    // #zh_CN 每16个线圈为1层. 并行数 = 层数 × 线圈等级
    public static final String Tooltip_Silksong_03 = TextEnums.tr("Tooltip_Silksong_03");

    // #tr Tooltip_Silksong_04
    // # Each level of coil increases the speed by {\RED}100%{\GRAY}.
    // #zh_CN 线圈每提高1级额外加速{\RED}100%{\GRAY}.
    public static final String Tooltip_Silksong_04 = TextEnums.tr("Tooltip_Silksong_04");

    // #tr Tooltip_Silksong_05
    // # Additional {\RED}15%{\GRAY} reduction in time per Coil Tier, multiplication calculus.
    // #zh_CN 电压每提高1级, 额外降低{\RED}15%{\GRAY}配方耗时, 叠乘计算.
    public static final String Tooltip_Silksong_05 = TextEnums.tr("Tooltip_Silksong_05");

    // endregion

    // region HolySeparator

    // #tr NameHolySeparator
    // # Holy Separator
    // #zh_CN 神圣分离者
    public static final String NameHolySeparator = TextEnums.tr("NameHolySeparator");

    // #tr Tooltip_HolySeparator_MachineType
    // # Cutter | Slicer | Lathe
    // #zh_CN 切割机 | 切片机 | 车床
    public static final String Tooltip_HolySeparator_MachineType = TextEnums.tr("Tooltip_HolySeparator_MachineType");

    // #tr Tooltip_HolySeparator_00
    // # Controller block for the Holy Separator
    // #zh_CN 神圣分离者的控制器方块
    public static final String Tooltip_HolySeparator_00 = TextEnums.tr("Tooltip_HolySeparator_00");

    // #tr Tooltip_HolySeparator_01
    // # {\YELLOW}Precision {\GRAY}and {\AQUA}Grace.
    // #zh_CN {\YELLOW}精准{\GRAY}而{\AQUA}优雅.
    public static final String Tooltip_HolySeparator_01 = TextEnums.tr("Tooltip_HolySeparator_01");

    // #tr Tooltip_HolySeparator_02
    // # Another form of laser engraving.
    // #zh_CN 激光蚀刻的另一个形式.
    public static final String Tooltip_HolySeparator_02 = TextEnums.tr("Tooltip_HolySeparator_02");

    // #tr Tooltip_HolySeparator_03
    // # You can even slice potato chips with this.
    // #zh_CN 你甚至可以用这机器切薯片.
    public static final String Tooltip_HolySeparator_03 = TextEnums.tr("Tooltip_HolySeparator_03");

    // #tr Tooltip_HolySeparator_04
    // # Extra {\AQUA}8x{\GRAY} Parallel per Piece. {\GOLD}16{\GRAY} Piece enable Perfect Overclock.
    // #zh_CN 每层提供{\AQUA}8x{\GRAY}并行. {\GOLD}16{\GRAY}层启用无损超频.
    public static final String Tooltip_HolySeparator_04 = TextEnums.tr("Tooltip_HolySeparator_04");

    // #tr Tooltip_HolySeparator_05
    // # Additional {\RED}10%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
    // #zh_CN 电压每提高1级, 额外降低{\RED}10%{\GRAY}配方耗时, 叠乘计算.
    public static final String Tooltip_HolySeparator_05 = TextEnums.tr("Tooltip_HolySeparator_05");

    // endregion

    // region SpaceScaler

    // #tr NameSpaceScaler
    // # Space Scaler
    // #zh_CN 空间缩放仪
    public static final String NameSpaceScaler = TextEnums.tr("NameSpaceScaler");

    // #tr Tooltip_SpaceScaler_MachineType
    // # Compressor | Extractor | Particle Collider | Electric Implosion Compressor | Neutronium Compressor
    // #zh_CN 压缩机 | 提取机 | 粒子对撞机 | 电动聚爆压缩机 | 中子态素压缩机
    public static final String Tooltip_SpaceScaler_MachineType = TextEnums.tr("Tooltip_SpaceScaler_MachineType");

    // #tr Tooltip_SpaceScaler_00
    // # Controller block for the Space Scaler
    // #zh_CN 空间缩放仪的控制器方块
    public static final String Tooltip_SpaceScaler_00 = TextEnums.tr("Tooltip_SpaceScaler_00");

    // #tr Tooltip_SpaceScaler_01
    // # {\AQUA} First Look Space Technology.
    // #zh_CN {\AQUA}初见空间科技.
    public static final String Tooltip_SpaceScaler_01 = TextEnums.tr("Tooltip_SpaceScaler_01");

    // #tr Tooltip_SpaceScaler_02
    // # Another method to operate matter.
    // #zh_CN 操作物质的另一种方式.
    public static final String Tooltip_SpaceScaler_02 = TextEnums.tr("Tooltip_SpaceScaler_02");

    // #tr Tooltip_SpaceScaler_03
    // # Only if the space is manageable...
    // #zh_CN 前提是空间是可控的 ...
    public static final String Tooltip_SpaceScaler_03 = TextEnums.tr("Tooltip_SpaceScaler_03");

    // #tr Tooltip_SpaceScaler_04
    // # Has parallel equivalent to Perfect Overclock.
    // #zh_CN 拥有与无损超频等效的并行(但有损超频).
    public static final String Tooltip_SpaceScaler_04 = TextEnums.tr("Tooltip_SpaceScaler_04");

    // #tr Tooltip_SpaceScaler_05
    // # If use Ultimate Containment Field Generator, enable {\RED}10x{\GRAY} speed multiplier.
    // #zh_CN 如果换装终极遏制场发生器, 获得{\RED}10x{\GRAY}倍速.
    public static final String Tooltip_SpaceScaler_05 = TextEnums.tr("Tooltip_SpaceScaler_05");

    // #tr Tooltip_SpaceScaler_06
    // # Crude Stabilisation Field Generator block+ allowed machine Particle Collider Mode.
    // #zh_CN 粗制稳定力场发生器等级+允许使用粒子对撞机模式.
    public static final String Tooltip_SpaceScaler_06 = TextEnums.tr("Tooltip_SpaceScaler_06");

    // #tr Tooltip_SpaceScaler_08
    // # In Particle Collider mode, higher tier has more output.
    // #zh_CN 粒子对撞机模式下, 高级方块带来额外产出.
    public static final String Tooltip_SpaceScaler_08 = TextEnums.tr("Tooltip_SpaceScaler_08");

    // endregion

    // region MoleculeDeconstructor

    // #tr NameMoleculeDeconstructor
    // # Molecule Deconstructor
    // #zh_CN 分子解构器
    public static final String NameMoleculeDeconstructor = TextEnums.tr("NameMoleculeDeconstructor");

    // #tr Tooltip_MoleculeDeconstructor_MachineType
    // # Electrolyzer | Centrifuge
    // #zh_CN 电解机 | 离心机
    public static final String Tooltip_MoleculeDeconstructor_MachineType = TextEnums.tr("Tooltip_MoleculeDeconstructor_MachineType");

    // #tr Tooltip_MoleculeDeconstructor_00
    // # Controller block for the Molecule Deconstructor
    // #zh_CN 分子解构器的控制器方块
    public static final String Tooltip_MoleculeDeconstructor_00 = TextEnums.tr("Tooltip_MoleculeDeconstructor_00");

    // #tr Tooltip_MoleculeDeconstructor_01
    // # {\AQUA}The lightning seemed to roll down a ladder.
    // #zh_CN {\AQUA}雷电好像从一架梯子上滚下来.
    public static final String Tooltip_MoleculeDeconstructor_01 = TextEnums.tr("Tooltip_MoleculeDeconstructor_01");

    // #tr Tooltip_MoleculeDeconstructor_02
    // # Separate the molecules one by one with tweezers.
    // #zh_CN 用镊子将分子一个一个一个分开.
    public static final String Tooltip_MoleculeDeconstructor_02 = TextEnums.tr("Tooltip_MoleculeDeconstructor_02");

    // #tr Tooltip_MoleculeDeconstructor_03
    // # Extra {\AQUA}24x{\GRAY} Parallel per Piece. {\GOLD}16{\GRAY} Piece enable Perfect Overclock.
    // #zh_CN 每层提供{\AQUA}24x{\GRAY}并行. {\GOLD}16{\GRAY}层启用无损超频.
    public static final String Tooltip_MoleculeDeconstructor_03 = TextEnums.tr("Tooltip_MoleculeDeconstructor_03");

    // #tr Tooltip_MoleculeDeconstructor_04
    // # Additional {\RED}10%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
    // #zh_CN 电压每提高1级, 额外降低{\RED}10%{\GRAY}配方耗时, 叠乘计算.
    public static final String Tooltip_MoleculeDeconstructor_04 = TextEnums.tr("Tooltip_MoleculeDeconstructor_04");

    // #tr Tooltip_MoleculeDeconstructor_05
    // # The Glass Tier limit the Energy hatch voltage Tier.
    // #zh_CN 玻璃等级限制能源仓等级.
    public static final String Tooltip_MoleculeDeconstructor_05 = TextEnums.tr("Tooltip_MoleculeDeconstructor_05");

    // endregion

    // region CrystallineInfinitier

    // #tr NameCrystallineInfinitier
    // # Crystalline Infinitier
    // #zh_CN 无限晶胞
    public static final String NameCrystallineInfinitier = TextEnums.tr("NameCrystallineInfinitier");

    // #tr Tooltip_CrystallineInfinitier_MachineType
    // # Autoclave | Crystalline Infinitier | Chemical Bath
    // #zh_CN 高压釜 | 晶胞铸造器 | 化学浸洗机
    public static final String Tooltip_CrystallineInfinitier_MachineType = TextEnums.tr("Tooltip_CrystallineInfinitier_MachineType");

    // #tr Tooltip_CrystallineInfinitier_00
    // # Controller block for the Crystalline Infinitier
    // #zh_CN 无限晶胞的控制器方块
    public static final String Tooltip_CrystallineInfinitier_00 = TextEnums.tr("Tooltip_CrystallineInfinitier_00");

    // #tr Tooltip_CrystallineInfinitier_01
    // # {\GREEN}They're here. Grow and multiply without end.
    // #zh_CN {\GREEN}它在这里. 生生不息.
    public static final String Tooltip_CrystallineInfinitier_01 = TextEnums.tr("Tooltip_CrystallineInfinitier_01");

    // #tr Tooltip_CrystallineInfinitier_02
    // # With Gravitation Tech as a medium, we can control growth of crystalline cells more conveniently.
    // #zh_CN 有了引力科技作为媒介, 我们可以更方便的控制晶胞的生长.
    public static final String Tooltip_CrystallineInfinitier_02 = TextEnums.tr("Tooltip_CrystallineInfinitier_02");

    // #tr Tooltip_CrystallineInfinitier_03
    // # Higher glass tier, higher field generator tier, higher voltage tier means higher value of parallel.
    // #zh_CN 更高的玻璃等级, 力场发生器等级, 电压等级意味着更多的并行数.
    public static final String Tooltip_CrystallineInfinitier_03 = TextEnums.tr("Tooltip_CrystallineInfinitier_03");

    // #tr Tooltip_CrystallineInfinitier_04
    // # And higher field generator tier means lower Energy cost.
    // #zh_CN 同时更高的力场发生器等级让耗电变得更低.
    public static final String Tooltip_CrystallineInfinitier_04 = TextEnums.tr("Tooltip_CrystallineInfinitier_04");

    // #tr Tooltip_CrystallineInfinitier_05
    // # Crude Stabilisation Field Generator enable Perfect Overclock.
    // #zh_CN 粗制稳定力场发生器等级+启用无损超频.
    public static final String Tooltip_CrystallineInfinitier_05 = TextEnums.tr("Tooltip_CrystallineInfinitier_05");

    // #tr Tooltip_CrystallineInfinitier_06
    // # Extra {\RED}+300%{\GRAY} speed in Autoclave mode. Extra {\RED}+1500%{\GRAY} speed in Chemical Bath mode.
    // #zh_CN 高压釜模式额外加速{\RED}300%{\GRAY}. 化学浸洗机模式额外加速{\RED}1500%{\GRAY}.
    public static final String Tooltip_CrystallineInfinitier_06 = TextEnums.tr("Tooltip_CrystallineInfinitier_06");

    // endregion

    // region DSPLauncher
    // #tr NameDSPLauncher
    // # Dyson Sphere Module Launch Site
    // #zh_CN 戴森球模块发射场
    public static final String NameDSPLauncher = TextEnums.tr("NameDSPLauncher");

    // #tr Tooltip_DSPLauncher_MachineType
    // # Dyson Sphere Program: Launch Site
    // #zh_CN 戴森球计划: 垂直发射井
    public static final String Tooltip_DSPLauncher_MachineType = TextEnums.tr("Tooltip_DSPLauncher_MachineType");

    // #tr Tooltip_DSPLauncher_00
    // # Controller block for the Dyson Sphere Module Launch Site
    // #zh_CN 戴森球模块发射场的控制器方块
    public static final String Tooltip_DSPLauncher_00 = TextEnums.tr("Tooltip_DSPLauncher_00");

    // #tr Tooltip_DSPLauncher_01
    // # {\BLUE}"Low altitude flight..."
    // #zh_CN {\BLUE}"低空飞行 ..."
    public static final String Tooltip_DSPLauncher_01 = TextEnums.tr("Tooltip_DSPLauncher_01");

    // #tr Tooltip_DSPLauncher_02
    // # Launching Dyson Sphere components into Dyson Sphere orbit to form a Dyson Sphere.
    // #zh_CN 发射装载有戴森球组件的小型运载火箭到戴森球轨道上组建戴森球.
    public static final String Tooltip_DSPLauncher_02 = TextEnums.tr("Tooltip_DSPLauncher_02");

    // #tr Tooltip_DSPLauncher_03
    // # No overclock and no extra parallel.
    // #zh_CN 不会超频且没有额外并行.
    public static final String Tooltip_DSPLauncher_03 = TextEnums.tr("Tooltip_DSPLauncher_03");

    // #tr Tooltip_DSPLauncher_04
    // # Higher tier of Elevator motor Module means faster launching.
    // #zh_CN 更高级的加速轨道可以减少发射耗时.
    public static final String Tooltip_DSPLauncher_04 = TextEnums.tr("Tooltip_DSPLauncher_04");

    // #tr Tooltip_DSPLauncher_05
    // # Inputting Space Warper will enable overlord mode. Reduce launch intervals.
    // #zh_CN 输入空间翘曲器可以进入过载模式. 减少发射时间间隔.
    public static final String Tooltip_DSPLauncher_05 = TextEnums.tr("Tooltip_DSPLauncher_05");

    // #tr Tooltip_DSPLauncher_06
    // # Joining the wireless EU network when without installing an energy hatch.
    // #zh_CN 未安装能源仓时自动进入无线电力网络模式.
    public static final String Tooltip_DSPLauncher_06 = TextEnums.tr("Tooltip_DSPLauncher_06");

    // #tr Tooltip_DSPLauncher_2_01
    // # Final progress time = recipe time / ( module tier * overload mode parameter )
    // #zh_CN 最终处理时间 = 配方时间 /( 加速轨道等级 * 过载模式参数 )
    public static final String Tooltip_DSPLauncher_2_01 = TextEnums.tr("Tooltip_DSPLauncher_2_01");

    // #tr Tooltip_DSPLauncher_2_02
    // # Every Space Warper will provide (default) 15 minutes of overload mode.
    // #zh_CN 每个空间翘曲器提供(默认)15分钟的过载模式.
    public static final String Tooltip_DSPLauncher_2_02 = TextEnums.tr("Tooltip_DSPLauncher_2_02");

    // #tr Tooltip_DSPLauncher_2_03
    // # Input Space Warper will be consumed immediately.
    // #zh_CN 输入的空间翘曲器会立刻被消耗.
    public static final String Tooltip_DSPLauncher_2_03 = TextEnums.tr("Tooltip_DSPLauncher_2_03");

    // #tr Tooltip_DSPLauncher_2_04
    // # Converted to remaining time of overload mode.
    // #zh_CN 转换成剩余的过载模式时间.
    public static final String Tooltip_DSPLauncher_2_04 = TextEnums.tr("Tooltip_DSPLauncher_2_04");

    // endregion

    // region DSPReceiver

    // #tr NameDSPReceiver
    // # Dyson Sphere Ray Receiving Station
    // #zh_CN 戴森球射线接收站
    public static final String NameDSPReceiver = TextEnums.tr("NameDSPReceiver");

    // #tr Tooltip_DSPReceiver_MachineType
    // # Dyson Sphere Program: Ray Receiving Station
    // #zh_CN 戴森球计划: 射线接收站
    public static final String Tooltip_DSPReceiver_MachineType = TextEnums.tr("Tooltip_DSPReceiver_MachineType");

    // #tr Tooltip_DSPReceiver_00
    // # Controller block for the Dyson Sphere Ray Receiving Station.
    // #zh_CN 戴森球射线接收站的控制器方块.
    public static final String Tooltip_DSPReceiver_00 = TextEnums.tr("Tooltip_DSPReceiver_00");

    // #tr Tooltip_DSPReceiver_01
    // # {\DARK_PURPLE}{\BOLD}You hold in your hands the true power of Master Nebula ...
    // #zh_CN {\DARK_PURPLE}{\BOLD}你的手中掌握着星云法师真正的力量 ...
    public static final String Tooltip_DSPReceiver_01 = TextEnums.tr("Tooltip_DSPReceiver_01");

    // #tr Tooltip_DSPReceiver_02
    // # Receive high-energy rays transmitted back from the Dyson Cloud or the Dyson Sphere.
    // #zh_CN 接收从戴森云或戴森球上传输回来的高能射线.
    public static final String Tooltip_DSPReceiver_02 = TextEnums.tr("Tooltip_DSPReceiver_02");

    // #tr Tooltip_DSPReceiver_03
    // # The received energy can be exported directly to the Wireless EU Net or Dynamo Hatches or stored as Critical Photons.
    // #zh_CN 可以将接收到的能量直接输出到无线EU电网或动力仓, 或者存储为临界状态的光子.
    public static final String Tooltip_DSPReceiver_03 = TextEnums.tr("Tooltip_DSPReceiver_03");

    // #tr Tooltip_DSPReceiver_04
    // # Ratio of the requesting from Dyson Sphere power point can be limited by putting Integrated Circuit into controller block.
    // #zh_CN 在控制器方块内放置编程电路可以限制请求功率的比率.
    public static final String Tooltip_DSPReceiver_04 = TextEnums.tr("Tooltip_DSPReceiver_04");

    // #tr Tooltip_DSPReceiver_05
    // # At the same time, the maximum requested power point is {\GOLD}1024{\GRAY}A Max (default).
    // #zh_CN 同时, 最大请求功率 {\GOLD}1024{\GRAY}A Max (默认).
    public static final String Tooltip_DSPReceiver_05 = TextEnums.tr("Tooltip_DSPReceiver_05");

    // #tr Tooltip_DSPReceiver_06
    // # Actual output power is affected by stellar and planetary coefficients.
    // #zh_CN 实际输出功率受恒星系数和行星系数影响.
    public static final String Tooltip_DSPReceiver_06 = TextEnums.tr("Tooltip_DSPReceiver_06");

    // #tr Tooltip_DSPReceiver_07
    // # Inputting Gravitational Lens will enable intensify mode. Increase actual output power.
    // #zh_CN 输入引力透镜将启用增强模式. 提高实际输出功率.
    public static final String Tooltip_DSPReceiver_07 = TextEnums.tr("Tooltip_DSPReceiver_07");

    // #tr Tooltip_DSPReceiver_08
    // # Joining the wireless EU network when without installing a dynamo hatch.
    // #zh_CN 未安装动力仓时自动进入无线电力网络模式.
    public static final String Tooltip_DSPReceiver_08 = TextEnums.tr("Tooltip_DSPReceiver_08");

    // #tr Tooltip_DSPReceiver_02_01
    // # Actual Generating EU = used power point * stellar coefficient * planet coefficient * 1 or 2 in intensify mode
    // #zh_CN 实际产生EU = 已占用产能点数 * 恒星系数 * 行星系数 * 1 或 4 在增强模式时
    public static final String Tooltip_DSPReceiver_02_01 = TextEnums.tr("Tooltip_DSPReceiver_02_01");

    // #tr Tooltip_DSPReceiver_02_02
    // # Personal Dimension was been classified as Overworld(Earth).
    // #zh_CN 私人维度归类于主世界(地球).
    public static final String Tooltip_DSPReceiver_02_02 = TextEnums.tr("Tooltip_DSPReceiver_02_02");

    // #tr Tooltip_DSPReceiver_02_03
    // # Every Gravitational Lens will provide (default) 10 minutes of intensify mode.
    // #zh_CN 每个引力透镜可以提供 (默认) 10 分钟的增强模式.
    public static final String Tooltip_DSPReceiver_02_03 = TextEnums.tr("Tooltip_DSPReceiver_02_03");

    // #tr Tooltip_DSPReceiver_02_04
    // # Input Gravitational Lens will be consumed immediately.
    // #zh_CN 输入的引力透镜会立刻被消耗.
    public static final String Tooltip_DSPReceiver_02_04 = TextEnums.tr("Tooltip_DSPReceiver_02_04");

    // #tr Tooltip_DSPReceiver_02_05
    // # Converted to remaining time of intensify mode.
    // #zh_CN 转换成剩余的增强模式时间.
    public static final String Tooltip_DSPReceiver_02_05 = TextEnums.tr("Tooltip_DSPReceiver_02_05");

    // #tr Tooltip_DSPReceiver_02_06
    // # Requesting ratio = Integrated Circuit Number / Stack Size
    // #zh_CN 请求比率 = 编程电路编号 / 堆叠数量
    public static final String Tooltip_DSPReceiver_02_06 = TextEnums.tr("Tooltip_DSPReceiver_02_06");

    // #tr Tooltip_DSPReceiver_02_07
    // # Put §b§l§oAstral Array Fabricator§7 into controller slot then this machine can request over 1024A Max power point.
    // #zh_CN 在控制器内放入 {\AQUA}{\BOLD}{\ITALIC}星阵{\GRAY} 可以使此机器请求超过1024A Max的能量点数.
    public static final String Tooltip_DSPReceiver_02_07 = TextEnums.tr("Tooltip_DSPReceiver_02_07");

    // #tr Tooltip_DSPReceiver_02_08
    // # Final requesting power point limit = Astral Array Fabricator amount^2 * 2048A Max
    // #zh_CN 最终请求能量点数上限 = 星阵数量^2 * 2048A Max
    public static final String Tooltip_DSPReceiver_02_08 = TextEnums.tr("Tooltip_DSPReceiver_02_08");

    // endregion

    // region ArtificialStar

    // #tr NameArtificialStar
    // # Artificial Star
    // #zh_CN 人造恒星
    public static final String NameArtificialStar = TextEnums.tr("NameArtificialStar");

    // #tr Tooltip_ArtificialStar_MachineType
    // # Dyson Sphere Program: Annihilation Generator
    // #zh_CN 戴森球计划: 湮灭发电机
    public static final String Tooltip_ArtificialStar_MachineType = TextEnums.tr("Tooltip_ArtificialStar_MachineType");

    // #tr Tooltip_ArtificialStar_Controller
    // # Controller block for the Artificial Star
    // #zh_CN 人造恒星的控制器方块
    public static final String Tooltip_ArtificialStar_Controller = TextEnums.tr("Tooltip_ArtificialStar_Controller");

    // #tr Tooltip_ArtificialStar_00
    // # {\LIGHT_PURPLE}{\BOLD}All you need to do is to let the proton and antiproton beams
    // #zh_CN {\LIGHT_PURPLE}{\BOLD}你只需要让正反质子束从两端静静地穿
    public static final String Tooltip_ArtificialStar_00 = TextEnums.tr("Tooltip_ArtificialStar_00");

    // #tr Tooltip_ArtificialStar_01
    // # {\LIGHT_PURPLE}{\BOLD} pass silently from both ends into the annihilation constrainer. Easy peasy!
    // #zh_CN {\LIGHT_PURPLE}{\BOLD} 过磁场进入约束球就可以了, 轻松愉快!
    public static final String Tooltip_ArtificialStar_01 = TextEnums.tr("Tooltip_ArtificialStar_01");

    // #tr Tooltip_ArtificialStar_02
    // # It owes its simple shape to the elegance of the theory.
    // #zh_CN 它简单的外形归功于理论的优雅.
    public static final String Tooltip_ArtificialStar_02 = TextEnums.tr("Tooltip_ArtificialStar_02");

    // #tr Tooltip_ArtificialStar_03
    // # Actual output power is affected by {\GOLD}3{\GRAY} types tiered block.
    // #zh_CN 实际产生能量受三种等级方块影响.
    public static final String Tooltip_ArtificialStar_03 = TextEnums.tr("Tooltip_ArtificialStar_03");

    // #tr Tooltip_ArtificialStar_04
    // # At the same time, higher tier increase the probability of recovering materials.
    // #zh_CN 同时, 更高等级会提高回收原料的概率.
    public static final String Tooltip_ArtificialStar_04 = TextEnums.tr("Tooltip_ArtificialStar_04");

    // #tr Tooltip_ArtificialStar_05
    // # Continuous operation improves power generation.
    // #zh_CN 保持连续运行也会提高能量输出.
    public static final String Tooltip_ArtificialStar_05 = TextEnums.tr("Tooltip_ArtificialStar_05");

    // #tr Tooltip_ArtificialStar_06
    // # Only and must install {\GOLD}1{\GRAY} input bus.
    // #zh_CN 只允许且必须安装一个输入总线.
    public static final String Tooltip_ArtificialStar_06 = TextEnums.tr("Tooltip_ArtificialStar_06");

    // #tr Tooltip_ArtificialStar_07
    // # Energy will output to Wireless EU Net directly.
    // #zh_CN 能量将直接输出到无线EU网络.
    public static final String Tooltip_ArtificialStar_07 = TextEnums.tr("Tooltip_ArtificialStar_07");

    // #tr Tooltip_ArtificialStar_08
    // # Use screwdriver to enable/disable animations.
    // #zh_CN 使用螺丝刀开启/关闭动画特效.
    public static final String Tooltip_ArtificialStar_08 = TextEnums.tr("Tooltip_ArtificialStar_08");

    // #tr Tooltip_ArtificialStar_02_01
    // # Output multiplier = tTime^0.25 * tDim^0.25 * 1.588186^(tStabilisation - 2)
    // #zh_CN 输出系数 = 时间场等级^0.25 * 空间场等级^0.25 * 1.588186^(稳定场等级 - 2)
    public static final String Tooltip_ArtificialStar_02_01 = TextEnums.tr("Tooltip_ArtificialStar_02_01");

    // #tr Tooltip_ArtificialStar_02_02
    // # Actual Generating EU = recipe value * output multiplier * Rewards for continuous operation
    // #zh_CN 实际输出EU = 配方数值 * 输出系数 * 连续运行奖励系数
    public static final String Tooltip_ArtificialStar_02_02 = TextEnums.tr("Tooltip_ArtificialStar_02_02");

    // #tr Tooltip_ArtificialStar_02_03
    // # Recovering probability = tTime * tDim * tStabilisation / 1000
    // #zh_CN 原料回收概率 = 时间场等级 * 空间场等级 * 稳定场等级 / 1000
    public static final String Tooltip_ArtificialStar_02_03 = TextEnums.tr("Tooltip_ArtificialStar_02_03");

    // #tr Tooltip_ArtificialStar_02_04
    // # Input fuels will be consumed at once, process 6.4s (default), and output the corresponding EU.
    // #zh_CN 输入的燃料将会一次性消耗掉, 然后运行 6.4s (默认), 并输出对应数值的 EU.
    public static final String Tooltip_ArtificialStar_02_04 = TextEnums.tr("Tooltip_ArtificialStar_02_04");

    // #tr Tooltip_ArtificialStar_02_05
    // # Rewards multiplier 1%% increase per run when continuous operation.
    // #zh_CN 在连续运行时, 每次运行连续运行奖励系数提高 1%% .
    public static final String Tooltip_ArtificialStar_02_05 = TextEnums.tr("Tooltip_ArtificialStar_02_05");

    // #tr Tooltip_ArtificialStar_02_06
    // # Maximum is 150%%, Minimum is 100%% when uncontinuous.
    // #zh_CN 连续运行奖励系数最大值 150%% , 中断后降到 100%% .
    public static final String Tooltip_ArtificialStar_02_06 = TextEnums.tr("Tooltip_ArtificialStar_02_06");

    // endregion

    // region Dyson Sphere Program Information

    // #tr Tooltip_DSPInfo_launch_01
    // # Launching Solar Sail increase Solar Sail amount of current Galaxy's Dyson Sphere.
    // #zh_CN 发射太阳帆增加当前星系戴森球的太阳帆数量.
    public static final String Tooltip_DSPInfo_launch_01 = TextEnums.tr("Tooltip_DSPInfo_launch_01");

    // #tr Tooltip_DSPInfo_launch_02
    // # Launching Small Launch Vehicle increase Node amount of current Galaxy's Dyson Sphere.
    // #zh_CN 发射小型运载火箭增加当前星系戴森球的节点数量.
    public static final String Tooltip_DSPInfo_launch_02 = TextEnums.tr("Tooltip_DSPInfo_launch_02");

    // #tr Tooltip_DSPInfo_00
    // # Dyson Sphere Energy Credit = Solar Sail Output (default 524288) * Solar Sail amount * (Node + 1)^0.8
    // #zh_CN 戴森球产能点数 = 每太阳帆产出 (默认 524288) * 太阳帆数量 * (节点数量 + 1)^0.8
    public static final String Tooltip_DSPInfo_00 = TextEnums.tr("Tooltip_DSPInfo_00");

    // #tr Tooltip_DSPInfo_01
    // # Every Node can absorb (default) 256 Solar Sails.
    // #zh_CN 每个节点可以吸附 (默认) 256 太阳帆.
    public static final String Tooltip_DSPInfo_01 = TextEnums.tr("Tooltip_DSPInfo_01");

    // #tr Tooltip_DSPInfo_02
    // # If unabsorbed solar sails amount is larger than 2048,
    // #zh_CN 如果未吸附的太阳帆数量超过2048,
    public static final String Tooltip_DSPInfo_02 = TextEnums.tr("Tooltip_DSPInfo_02");

    // #tr Tooltip_DSPInfo_03
    // # the excess may be destroyed.
    // #zh_CN  超过的部分可能会损毁.
    public static final String Tooltip_DSPInfo_03 = TextEnums.tr("Tooltip_DSPInfo_03");

    // #tr Tooltip_DSPInfo_04
    // # Every 30 minutes has 1/5000 chance to decrease Solar Sail amount.
    // #zh_CN 每30 分钟有 1/5000 几率触发太阳帆损毁事件.
    public static final String Tooltip_DSPInfo_04 = TextEnums.tr("Tooltip_DSPInfo_04");

    // #tr Tooltip_DSPInfo_05
    // # In decreasing, every Dyson Sphere has 1/4 chance to destroyed Solar Sail amount.
    // #zh_CN 在损毁事件里, 每个戴森球都有 1/4 几率损毁太阳帆.
    public static final String Tooltip_DSPInfo_05 = TextEnums.tr("Tooltip_DSPInfo_05");

    // #tr Tooltip_DSPInfo_06
    // # The amount of destroyed is the excess' 1/2.
    // #zh_CN 损毁的数量是超过部分的 1/2 .
    public static final String Tooltip_DSPInfo_06 = TextEnums.tr("Tooltip_DSPInfo_06");

    // endregion

    // region MiracleDoor

    // #tr NameMiracleDoor
    // # Miracle Door
    // #zh_CN 奇迹之门
    public static final String NameMiracleDoor = TextEnums.tr("NameMiracleDoor");

    // #tr Tooltip_MiracleDoor_MachineType
    // # Stellar Forge | Stellar Forge : Alloy Smelter
    // #zh_CN 恒星锻炉 | 恒星锻炉:合金冶炼
    public static final String Tooltip_MiracleDoor_MachineType = TextEnums.tr("Tooltip_MiracleDoor_MachineType");

    // #tr Tooltip_MiracleDoor_Controller
    // # Controller block for the Miracle Door
    // #zh_CN 奇迹之门的控制器方块
    public static final String Tooltip_MiracleDoor_Controller = TextEnums.tr("Tooltip_MiracleDoor_Controller");

    // #tr Tooltip_MiracleDoor_00
    // # {\GOLD}{\BOLD}Mere mortals can't even begin to understand the progress we've made.
    // #zh_CN {\GOLD}{\BOLD}凡夫俗子根本无法理解我们的进步.
    public static final String Tooltip_MiracleDoor_00 = TextEnums.tr("Tooltip_MiracleDoor_00");

    // #tr Tooltip_MiracleDoor_01
    // # Enslaving Stellaris to work for us.
    // #zh_CN 奴役群星为我们工作.
    public static final String Tooltip_MiracleDoor_01 = TextEnums.tr("Tooltip_MiracleDoor_01");

    // #tr Tooltip_MiracleDoor_02
    // # No matter how large the workload, it can be done in one time.
    // #zh_CN 无论多大的工作量, 都能一次完成.
    public static final String Tooltip_MiracleDoor_02 = TextEnums.tr("Tooltip_MiracleDoor_02");

    // #tr Tooltip_MiracleDoor_03
    // # No matter how large the workload, it need one Critical Photon to start.
    // #zh_CN 无论多大的工作量, 都需要临界光子启动.
    public static final String Tooltip_MiracleDoor_03 = TextEnums.tr("Tooltip_MiracleDoor_03");

    // #tr Tooltip_MiracleDoor_04
    // # Power consumption: Alloy Smelter {\RED}100%{\GRAY} | Stellar Forge {\RED}200%{\GRAY}
    // #zh_CN 能量消耗: {\RESET}合金冶炼模式 {\RED}{\BOLD}100% {\GRAY}| {\RESET}恒星锻炉模式 {\RED}{\BOLD}200%
    public static final String Tooltip_MiracleDoor_04 = TextEnums.tr("Tooltip_MiracleDoor_04");

    // #tr Tooltip_MiracleDoor_05
    // # Directly get EU from the Wireless EU Net.
    // #zh_CN 直接从无线EU网络获取能量.
    public static final String Tooltip_MiracleDoor_05 = TextEnums.tr("Tooltip_MiracleDoor_05");

    // #tr Tooltip_MiracleDoor_06
    // # Warning! If trying to start machine when Wireless EU Net has not enough EU,
    // #zh_CN 警告! 如果尝试在网络内能量不足时启动机器,
    public static final String Tooltip_MiracleDoor_06 = TextEnums.tr("Tooltip_MiracleDoor_06");

    // #tr Tooltip_MiracleDoor_07
    // # the materials will fade.
    // #zh_CN  输入的原料将直接寂灭.
    public static final String Tooltip_MiracleDoor_07 = TextEnums.tr("Tooltip_MiracleDoor_07");

    // #tr Tooltip_MiracleDoor_08
    // # Put Integrated Circuit into Controller block to decrease process time interval.
    // #zh_CN 在控制器方块内放置编程电路以减少处理时间间隔.
    public static final String Tooltip_MiracleDoor_08 = TextEnums.tr("Tooltip_MiracleDoor_08");

    // #tr Tooltip_MiracleDoor_2_01
    // # Each run takes the same amount of time, (ABS) 25.6s | (EBF) 64s default.
    // #zh_CN 每次运行消耗相同的时间, 默认 (恒星锻炉) 64s | (合金冶炼) 25.6s .
    public static final String Tooltip_MiracleDoor_2_01 = TextEnums.tr("Tooltip_MiracleDoor_2_01");

    // #tr Tooltip_MiracleDoor_2_02
    // # If putting Integrated Circuit into Controller block slot,
    // #zh_CN 如果在控制器方块输入槽放置编程电路,
    public static final String Tooltip_MiracleDoor_2_02 = TextEnums.tr("Tooltip_MiracleDoor_2_02");

    // #tr Tooltip_MiracleDoor_2_03
    // # actual progress time = default / (Integrated Circuit Number * Stack Size)
    // #zh_CN 实际处理时间 = 默认耗时 / (编号 * 物品数量)
    public static final String Tooltip_MiracleDoor_2_03 = TextEnums.tr("Tooltip_MiracleDoor_2_03");

    // #tr Tooltip_MiracleDoor_2_04
    // # Actual cost EU = recipe value * 16 * (Integrated Circuit Number * Stack Size)
    // #zh_CN 实际消耗 EU = 配方数值 * (编号 * 物品数量) * (恒星锻炉) 2 (合金冶炼) 1
    public static final String Tooltip_MiracleDoor_2_04 = TextEnums.tr("Tooltip_MiracleDoor_2_04");

    // #tr Tooltip_MiracleDoor_2_05
    // # Each run cost number of OverClock Times Critical Photon to start(Default 1).
    // #zh_CN 每次运行需要消耗超频次数颗临界光子(默认为1).
    public static final String Tooltip_MiracleDoor_2_05 = TextEnums.tr("Tooltip_MiracleDoor_2_05");

    // endregion

    // region OreProcessingFactory

    // #tr NameOreProcessingFactory
    // # General Ore Processing Factory TST
    // #zh_CN 通用矿物处理厂TST
    public static final String NameOreProcessingFactory = TextEnums.tr("NameOreProcessingFactory");

    // #tr Tooltip_OreProcessingFactory_MachineType
    // # Ore Processor
    // #zh_CN 矿石处理厂
    public static final String Tooltip_OreProcessingFactory_MachineType = TextEnums.tr("Tooltip_OreProcessingFactory_MachineType");

    // #tr Tooltip_OreProcessingFactory_Controller
    // # Controller block for the General Ore Processing Factory TST
    // #zh_CN 通用矿物处理厂TST的控制器方块
    public static final String Tooltip_OreProcessingFactory_Controller = TextEnums.tr("Tooltip_OreProcessingFactory_Controller");

    // #tr Tooltip_OreProcessingFactory_01
    // # {\WHITE}Engineering is the art of directing the great sources of power in nature for the use and convenience of man.
    // #zh_CN {\WHITE}所谓工程, 就是一门将大自然中绝佳的能量源用于惠世济民的艺术.
    public static final String Tooltip_OreProcessingFactory_01 = TextEnums.tr("Tooltip_OreProcessingFactory_01");

    // #tr Tooltip_OreProcessingFactory_02
    // # The ores will line up and go in through the entrance and out through the exit.
    // #zh_CN 矿石们将排好队从入口进去, 再从出口出来.
    public static final String Tooltip_OreProcessingFactory_02 = TextEnums.tr("Tooltip_OreProcessingFactory_02");

    // #tr Tooltip_OreProcessingFactory_03
    // # This machine will not do overclock. Progress time is always {\GOLD}6.4s{\GRAY} (default).
    // #zh_CN 机器不会进行超频. 处理时间固定为 {\GOLD}6.4s{\GRAY} (默认) .
    public static final String Tooltip_OreProcessingFactory_03 = TextEnums.tr("Tooltip_OreProcessingFactory_03");

    // #tr Tooltip_OreProcessingFactory_04
    // # It will process as many inputs as possible at once, if power allow.
    // #zh_CN 将尽可能一次处理全部输入的原料, 供电允许的话.
    public static final String Tooltip_OreProcessingFactory_04 = TextEnums.tr("Tooltip_OreProcessingFactory_04");

    // #tr Tooltip_OreProcessingFactory_05
    // # Consume {\GOLD}3200L{\GRAY} Lubricant every {\GOLD}12.8s{\GRAY} (default).
    // #zh_CN 每{\GOLD}12.8s{\GRAY} 消耗 {\GOLD}3200L{\GRAY} 润滑油(默认) .
    public static final String Tooltip_OreProcessingFactory_05 = TextEnums.tr("Tooltip_OreProcessingFactory_05");

    // #tr Tooltip_OreProcessingFactory_06
    // # Non-ore inputs will be transferred to the output bus.
    // #zh_CN 非矿石输入物将被转移到输出总线.
    public static final String Tooltip_OreProcessingFactory_06 = TextEnums.tr("Tooltip_OreProcessingFactory_06");

    // endregion

    // region CircuitConverter

    // #tr NameCircuitConverter
    // # General Circuit Converter
    // #zh_CN 通用电路板转换器
    public static final String NameCircuitConverter = TextEnums.tr("NameCircuitConverter");

    // #tr Tooltip_CircuitConverter_MachineType
    // # Circuit Converter
    // #zh_CN 电路板转换器
    public static final String Tooltip_CircuitConverter_MachineType = TextEnums.tr("Tooltip_CircuitConverter_MachineType");

    // #tr Tooltip_CircuitConverter_Controller
    // # Controller block for the General Circuit Converter
    // #zh_CN 通用电路板转换器的控制器方块
    public static final String Tooltip_CircuitConverter_Controller = TextEnums.tr("Tooltip_CircuitConverter_Controller");

    // #tr Tooltip_CircuitConverter_01
    // # Transform input circuits to Any Circuit.
    // #zh_CN 将输入的电路板转换成通用电路板.
    public static final String Tooltip_CircuitConverter_01 = TextEnums.tr("Tooltip_CircuitConverter_01");

    // #tr Tooltip_CircuitConverter_2_01
    // # Maximum 8 In/Output Buses.
    // #zh_CN 最多 8 个输入总线或输出总线.
    public static final String Tooltip_CircuitConverter_2_01 = TextEnums.tr("Tooltip_CircuitConverter_2_01");

    // endregion

    // region LargeIndustrialCokingFactory

    // #tr NameLargeIndustrialCokingFactory
    // # Large Industrial Coking Factory
    // #zh_CN 大型工业炼焦厂
    public static final String NameLargeIndustrialCokingFactory = TextEnums.tr("NameLargeIndustrialCokingFactory");

    // #tr Tooltip_LargeIndustrialCokingFactory_MachineType
    // # Coke Oven
    // #zh_CN 焦炉
    public static final String Tooltip_LargeIndustrialCokingFactory_MachineType = TextEnums.tr("Tooltip_LargeIndustrialCokingFactory_MachineType");

    // #tr Tooltip_LargeIndustrialCokingFactory_Controller
    // # Controller block for the Large Industrial Coking Factory
    // #zh_CN 大型工业炼焦厂的控制器方块
    public static final String Tooltip_LargeIndustrialCokingFactory_Controller = TextEnums.tr("Tooltip_LargeIndustrialCokingFactory_Controller");

    // #tr Tooltip_LargeIndustrialCokingFactory_01
    // # {\DARK_AQUA}{\BOLD}Seizing like a ravenous beast, lavishing like a breezing east.
    // #zh_CN {\DARK_AQUA}{\BOLD}取之尽锱铢, 用之如泥沙.
    public static final String Tooltip_LargeIndustrialCokingFactory_01 = TextEnums.tr("Tooltip_LargeIndustrialCokingFactory_01");

    // #tr Tooltip_LargeIndustrialCokingFactory_02
    // # Process endless inputs in one time.
    // #zh_CN 无尽的原料一次加工完成.
    public static final String Tooltip_LargeIndustrialCokingFactory_02 = TextEnums.tr("Tooltip_LargeIndustrialCokingFactory_02");

    // #tr Tooltip_LargeIndustrialCokingFactory_03
    // # Higher tier of coil make machine more faster.
    // #zh_CN 更高级的线圈可以让机器更快.
    public static final String Tooltip_LargeIndustrialCokingFactory_03 = TextEnums.tr("Tooltip_LargeIndustrialCokingFactory_03");

    // endregion

    // region Elvenworkshop

    // #tr NameElvenWorkshop
    // # ElvenWorkshop
    // #zh_CN 精灵工坊
    public static final String NameElvenWorkshop = TextEnums.tr("NameElvenWorkshop");

    // #tr Tooltip_ElvenWorkshop_MachineType
    // # Mana Infuser/Rune Engraver
    // #zh_CN 魔力灌注/符文雕刻
    public static final String Tooltip_ElvenWorkshop_MachineType = TextEnums.tr("Tooltip_ElvenWorkshop_MachineType");

    // endregion

    // region HyperSpacetimeTransformer

    // #tr NameHyperSpacetimeTransformer
    // # HyperSpacetimeTransformer
    // #zh_CN 极限时空转换仪
    public static final String NameHyperSpacetimeTransformer = TextEnums.tr("NameHyperSpacetimeTransformer");

    // #tr Tooltip_HyperSpacetimeTransformer_MachineType
    // # HyperSpacetimeTransformer
    // #zh_CN 分子重组仪/{\DARK_BLUE}时空转换仪
    public static final String Tooltip_HyperSpacetimeTransformer_MachineType = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_MachineType");

    // #tr Tooltip_HyperSpacetimeTransformer_00
    // # To change the material itself in a higher dimension.
    // #zh_CN 于更高维度改变物质本身。
    public static final String Tooltip_HyperSpacetimeTransformer_00 = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_00");

    // #tr Tooltip_HyperSpacetimeTransformer_01
    // # Molecular Transformer Mode
    // #zh_CN {\YELLOW}分子重组仪模式
    public static final String Tooltip_HyperSpacetimeTransformer_01 = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_01");

    // #tr Tooltip_HyperSpacetimeTransformer_02
    // # Parallel:Product of three types of field generators.
    // #zh_CN 并行：时空场发生器等级的乘积，最多512并行。
    public static final String Tooltip_HyperSpacetimeTransformer_02 = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_02");

    // #tr Tooltip_HyperSpacetimeTransformer_03
    // # Tier 9 field generator will decrease energy use by {\RED}25%{\GRAY}, max {\RED}75%{\GRAY}.
    // #zh_CN 9级时空场会带来额外的能耗降低，最多降低{\RED}75%{\GRAY}。
    public static final String Tooltip_HyperSpacetimeTransformer_03 = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_03");

    // #tr Tooltip_HyperSpacetimeTransformer_04
    // #zh_CN {\DARK_BLUE}时空转换仪模式
    // #
    public static final String Tooltip_HyperSpacetimeTransformer_04 = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_04");

    // #tr Tooltip_HyperSpacetimeTransformer_05
    // #zh_CN 屏蔽核心等级与脉冲控制等级决定了该机器并行，最高16并行。
    // #
    public static final String Tooltip_HyperSpacetimeTransformer_05 = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_05");

    // #tr Tooltip_HyperSpacetimeTransformer_06
    // #zh_CN 升级时空场以降低能耗，最多降低87.5%%EU消耗。
    // #
    public static final String Tooltip_HyperSpacetimeTransformer_06 = TextEnums.tr("Tooltip_HyperSpacetimeTransformer_06");


    // region Scavenger

    // #tr NameScavenger
    // # Scavenger
    // #zh_CN 拾荒者
    public static final String NameScavenger = TextEnums.tr("NameScavenger");

    // #tr Tooltip_Scavenger_MachineType
    // # Sifter
    // #zh_CN 筛选机
    public static final String Tooltip_Scavenger_MachineType = TextEnums.tr("Tooltip_Scavenger_MachineType");

    // #tr Tooltip_Scavenger_Controller
    // # Controller block for the Scavenger
    // #zh_CN 拾荒者的控制器方块
    public static final String Tooltip_Scavenger_Controller = TextEnums.tr("Tooltip_Scavenger_Controller");

    // #tr Tooltip_Scavenger_01
    // # {\BOLD}I like pigs. Dogs look up to us. Cats look down on us. Pigs treat us as equals.
    // #zh_CN {\BOLD}我喜欢猪. 狗崇拜人类. 猫鄙视人类. 猪对我们一视同仁.
    public static final String Tooltip_Scavenger_01 = TextEnums.tr("Tooltip_Scavenger_01");

    // #tr Tooltip_Scavenger_02
    // # Has parallel equivalent to Perfect Overclock.
    // #zh_CN 拥有与无损超频等效的并行(但有损超频).
    public static final String Tooltip_Scavenger_02 = TextEnums.tr("Tooltip_Scavenger_02");

    // #tr Tooltip_Scavenger_03
    // # Only uses {\RED}60%{\GRAY} of the EU/t normally required.
    // #zh_CN 只需要使用配方要求功率的{\RED}60%{\GRAY} .
    public static final String Tooltip_Scavenger_03 = TextEnums.tr("Tooltip_Scavenger_03");

    // #tr Tooltip_Scavenger_04
    // # Additional {\RED}20%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
    // #zh_CN 电压每提高1级, 额外降低{\RED}20%{\GRAY}配方耗时, 叠乘计算.
    public static final String Tooltip_Scavenger_04 = TextEnums.tr("Tooltip_Scavenger_04");

    // endregion

    // region AdvancedMegaOilCracker

    // #tr NameAdvancedMegaOilCracker
    // # Advanced Mega Oil Cracker
    // #zh_CN 进阶巨型石油裂化机
    public static final String NameAdvancedMegaOilCracker = TextEnums.tr("NameAdvancedMegaOilCracker");

    // #tr Tooltips_AdvancedMegaOilCracker_MachineType
    // # Cracker
    // #zh_CN 石油裂化机
    public static final String Tooltips_AdvancedMegaOilCracker_MachineType = TextEnums.tr("Tooltips_AdvancedMegaOilCracker_MachineType");

    // #tr Tooltips_AdvancedMegaOilCracker_Controller
    // # Controller block for the Advanced Mega Oil Cracker
    // #zh_CN 进阶巨型石油裂化机的控制器方块
    public static final String Tooltips_AdvancedMegaOilCracker_Controller = TextEnums.tr("Tooltips_AdvancedMegaOilCracker_Controller");

    // #tr Tooltips_AdvancedMegaOilCracker_01
    // # {\ITALIC}Freedom as a basis for self-government.
    // #zh_CN {\ITALIC}自由是自治之基础.
    public static final String Tooltips_AdvancedMegaOilCracker_01 = TextEnums.tr("Tooltips_AdvancedMegaOilCracker_01");

    // #tr Tooltips_AdvancedMegaOilCracker_02
    // # As the Mega Oil Cracker do.
    // #zh_CN 就像巨型石油裂化机那样.
    public static final String Tooltips_AdvancedMegaOilCracker_02 = TextEnums.tr("Tooltips_AdvancedMegaOilCracker_02");

    // endregion

    // region InfiniteAirHatch

    // #tr NameInfiniteAirHatch
    // # Infinite Air Hatch
    // #zh_CN 无限进气仓
    public static final String NameInfiniteAirHatch = TextEnums.tr("NameInfiniteAirHatch");

    // #tr NameManaHatch
    // # Mana Hatch
    // #zh_CN Mana Hatch
    public static final String NameManaHatch = TextEnums.tr("NameManaHatch");

    // #tr NameInfiniteWirelessDynamoHatch
    // # Infinite Wireless Dynamo Hatch
    // #zh_CN 无限无线动力仓
    public static final String NameInfiniteWirelessDynamoHatch = TextEnums.tr("NameInfiniteWirelessDynamoHatch");

    // #tr NameDualInputBuffer_IV
    // # Dual Input Buffer (IV)
    // #zh_CN 输入总成 (IV)
    public static final String NameDualInputBuffer_IV = TextEnums.tr("NameDualInputBuffer_IV");

    // #tr NameDualInputBuffer_LuV
    // # Dual Input Buffer (LuV)
    // #zh_CN 输入总成 (LuV)
    public static final String NameDualInputBuffer_LuV = TextEnums.tr("NameDualInputBuffer_LuV");

    // #tr NameDualInputBuffer_ZPM
    // # Dual Input Buffer (ZPM)
    // #zh_CN 输入总成 (ZPM)
    public static final String NameDualInputBuffer_ZPM = TextEnums.tr("NameDualInputBuffer_ZPM");

    // #tr NameDualInputBuffer_UV
    // # Dual Input Buffer (UV)
    // #zh_CN 输入总成 (UV)
    public static final String NameDualInputBuffer_UV = TextEnums.tr("NameDualInputBuffer_UV");

    // endregion

    // region megaUniverseSpaceStation

    // #tr NameMegaUniversalSpaceStation
    // # Mega Universal Space Station
    // #zh_CN {\RED}寰 {\AQUA}宇 {\GOLD}空 {\BLUE}间 {\DARK_GRAY}站
    public static final String NameMegaUniversalSpaceStation = TextEnums.tr("NameMegaUniversalSpaceStation");

    // #tr Tooltip_MegaUniversalSpaceStation_MachineType
    // # space station
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_MachineType = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_MachineType");

    // #tr Tooltip_MegaUniversalSpaceStation_00
    // # Use auto build item to build instead of build your self
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_00 = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_00");

    // #tr Tooltip_MegaUniversalSpaceStation_01
    // # Auto-SpaceStation build core
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_01 = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_01");

    // #tr Tooltip_MegaUniversalSpaceStation_02
    // # If your station broke, you can put fix block inside the input hatch to fix it
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_02 = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_02");

    // #tr Tooltip_MegaUniversalSpaceStation_03
    // # temp
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_03 = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_03");

    // #tr Tooltip_MegaUniversalSpaceStation_04
    // # temp
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_04 = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_04");

    // #tr Tooltip_MegaUniversalSpaceStation_05
    // # temp
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_05 = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_05");

    // #tr Tooltip_MegaUniversalSpaceStation_06
    // # temp
    // #zh_CN temp
    public static final String Tooltip_MegaUniversalSpaceStation_06 = TextEnums.tr("Tooltip_MegaUniversalSpaceStation_06");

    // #tr NameStellarMaterialSiphon
    // # Stellar Material Siphon
    // #zh_CN Stellar Material Siphon
    public static final String NameStellarMaterialSiphon = TextEnums.tr("NameStellarMaterialSiphon");

    // #tr Tooltip_NameStellarMaterialSiphon
    // # Siphon
    // #zh_CN Siphon
    public static final String Tooltip_NameStellarMaterialSiphon = TextEnums.tr("Tooltip_NameStellarMaterialSiphon");

    //endregion

    //region MegaPrimitiveBlastFurnace

    // #tr NameMegaBrickedBlastFurnace
    // # Mega Bricked Blast Furnace
    // #zh_CN 巨型砖高炉
    public static final String NameMegaBrickedBlastFurnace = TextEnums.tr("NameMegaBrickedBlastFurnace");

    // #tr Tooltip_MegaBrickedBlastFurnace_MachineType
    // # Blast Furnace
    // #zh_CN 高炉
    public static final String Tooltip_MegaBrickedBlastFurnace_MachineType = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_MachineType");

    // #tr Tooltip_MegaBrickedBlastFurnace_Controller
    // # Controller block for the Mega Bricked Blast Furnace
    // #zh_CN 巨型砖高炉的控制器方块
    public static final String Tooltip_MegaBrickedBlastFurnace_Controller = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_Controller");

    // #tr Tooltip_MegaBrickedBlastFurnace_00
    // # {\WHITE}Who could ever imagine the power of the Steam Age?
    // #zh_CN {\WHITE}谁能想象出蒸汽时代之伟力?
    public static final String Tooltip_MegaBrickedBlastFurnace_00 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_00");

    // #tr Tooltip_MegaBrickedBlastFurnace_01
    // # consume iron/wrought iron ingots and coke coals (blocks) to produce steel (and ash byproduct)
    // #zh_CN 消耗铁/锻铁锭与焦煤/焦煤块炼钢(与灰烬副产物).
    public static final String Tooltip_MegaBrickedBlastFurnace_01 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_01");

    // #tr Tooltip_MegaBrickedBlastFurnace_02
    // # Default recipe time is {\GOLD}240s{\GRAY}. More wrought iron and coal input will reduce process time.
    // #zh_CN 初始配方时间为{\GOLD}240s{\GRAY}. 输入更多锻铁与焦煤以减少处理时间.
    public static final String Tooltip_MegaBrickedBlastFurnace_02 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_02");

    // #tr Tooltip_MegaBrickedBlastFurnace_03
    // # actual progress time = default x parallels /((1 + 4 x Ratio of wrought iron input) x sqrt(Coke coal input))
    // #zh_CN 实际处理时间 = 初始值 x 并行 / ((1 + 4 x 输入锻铁比例) x sqrt(输入焦煤))
    public static final String Tooltip_MegaBrickedBlastFurnace_03 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_03");

    // #tr Tooltip_MegaBrickedBlastFurnace_04
    // # process {\RED}50%{\GRAY} of (wrought) iron input and consume all coke coal input at once.
    // #zh_CN 一次性消耗输入的(锻)铁锭的{\RED}50%{\GRAY}与输入的全部焦煤.
    public static final String Tooltip_MegaBrickedBlastFurnace_04 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_04");

    // #tr Tooltip_MegaBrickedBlastFurnace_05
    // # minimum coke coal requirement:2 x (wrought) iron processed
    // #zh_CN 焦煤的最低需求量: 2 x 处理的(锻)铁锭的量
    public static final String Tooltip_MegaBrickedBlastFurnace_05 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_05");

    // #tr Tooltip_MegaBrickedBlastFurnace_06
    // # Takes {\RED}8{\GRAY} hours of continuous run time to achieve maximum efficiency.
    // #zh_CN 需要连续运行{\RED}8{\GRAY}小时来达到最大效率.
    public static final String Tooltip_MegaBrickedBlastFurnace_06 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_06");

    // #tr Tooltip_MegaBrickedBlastFurnace_07
    // # This improve coal efficiency by up to {\RED}800%{\GRAY}. Reduce minimum coal requirement and calculate in actual progress time.
    // #zh_CN 最多可使焦煤的使用效率提高至{\RED}800%{\GRAY},降低焦煤最低需求量并计入处理时间计算
    public static final String Tooltip_MegaBrickedBlastFurnace_07 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_07");

    // #tr Tooltip_MegaBrickedBlastFurnace_08
    // # {\YELLOW}It is recommended not to force yourself to build it until you have enough resources.
    // #zh_CN {\YELLOW}建议在你有充足的资源之前不要强迫自己建造它!
    public static final String Tooltip_MegaBrickedBlastFurnace_08 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_08");

    // #tr Tooltip_MegaBrickedBlastFurnace_09
    // # {\AQUA}Use a screwdriver to switch to primitive mode so you can process all primitive recipes here, but you cannot use wrought iron anymore
    // #zh_CN 使用螺丝刀切换到土高模式以处理原本的土高炉配方，但不再能通过锻铁加速
    public static final String Tooltip_MegaBrickedBlastFurnace_09 = TextEnums.tr("Tooltip_MegaBrickedBlastFurnace_09");

    // #tr textMegaBrickedBlastFurnaceTips
    // # {\YELLOW}Dirt must be Horizontal dirt in Chisel Mod!
    // #zh_CN {\YELLOW}泥土必须为Chisel模组中的水平花纹泥土!
    public static final String textMegaBrickedBlastFurnaceTips = TextEnums.tr("textMegaBrickedBlastFurnaceTips");

    // #tr textMegaBrickedBlastFurnaceLocation
    // # any Bronze Plated Bricks, 0-6x
    // #zh_CN 任意镀铜机械方块, 0-6x
    public static final String textMegaBrickedBlastFurnaceLocation = TextEnums.tr("textMegaBrickedBlastFurnaceLocation");

    //region BiosphereIII

    // #tr NameBiosphereIII
    // # Biosphere III
    // #zh_CN 生物圈III号
    public static final String NameBiosphereIII = TextEnums.tr("NameBiosphereIII");

    // #tr Tooltip_BiosphereIII_MachineType
    // # Bacterial Vat | Brewing Machine | Fermenter
    // #zh_CN 细菌培养缸 | 酿造室 | 发酵槽
    public static final String Tooltip_BiosphereIII_MachineType = TextEnums.tr("Tooltip_BiosphereIII_MachineType");

    // #tr Tooltip_BiosphereIII_Controller
    // # Controller block for Biosphere III
    // #zh_CN 生物圈III号的控制器方块
    public static final String Tooltip_BiosphereIII_Controller = TextEnums.tr("Tooltip_BiosphereIII_Controller");

    // #tr Tooltip_BiosphereIII_00
    // # {\AQUA}{\BOLD}Control the thoughts of those microorganisms...
    // #zh_CN {\BLUE}{\BOLD}操控微生物们的思想……
    public static final String Tooltip_BiosphereIII_00 = TextEnums.tr("Tooltip_BiosphereIII_00");

    // #tr BiosphereIII_Mode_00
    // # {\GOLD}----- Bacterial Vat Mode -----
    // #zh_CN {\GOLD}----- 细菌培养缸模式 -----
    public static final String BiosphereIII_Mode_00 = TextEnums.tr("BiosphereIII_Mode_00");

    // #tr Tooltip_BiosphereIII_Mode0_00
    // # Need Petri Dish in controller slot
    // #zh_CN 需要在主机中放入培养皿.加速{\RED}100%{\GOLD}.
    public static final String Tooltip_BiosphereIII_Mode0_00 = TextEnums.tr("Tooltip_BiosphereIII_Mode0_00");

    // #tr Tooltip_BiosphereIII_Mode0_01
    // # Every Petri Dish provides {\AQUA}4x{\GRAY} parallel
    // #zh_CN 每个培养皿提供{\AQUA}4x{\GRAY}并行
    public static final String Tooltip_BiosphereIII_Mode0_01 = TextEnums.tr("Tooltip_BiosphereIII_Mode0_01");

    // #tr Tooltip_BiosphereIII_Mode0_02
    // # Keep the Output Hatch always half filled for maximum efficiency
    // #zh_CN 保持输出仓半满以达到最高效率
    public static final String Tooltip_BiosphereIII_Mode0_02 = TextEnums.tr("Tooltip_BiosphereIII_Mode0_02");

    // #tr BiosphereIII_Mode_01
    // # {\GOLD}----- Bacterial Vat Automation Mode -----
    // #zh_CN {\GOLD}----- 细菌培养缸自动化模式 -----
    public static final String BiosphereIII_Mode_01 = TextEnums.tr("BiosphereIII_Mode_01");

    // #tr Tooltip_BiosphereIII_Mode1_00
    // # Need Petri Dish in controller slot
    // #zh_CN 需要在主机中放入培养皿
    public static final String Tooltip_BiosphereIII_Mode1_00 = TextEnums.tr("Tooltip_BiosphereIII_Mode1_00");

    // #tr Tooltip_BiosphereIII_Mode1_01
    // # Every Petri Dish provides {\AQUA}1x{\GRAY} parallel
    // #zh_CN 每个培养皿提供{\AQUA}1x{\GRAY}并行
    public static final String Tooltip_BiosphereIII_Mode1_01 = TextEnums.tr("Tooltip_BiosphereIII_Mode1_01");

    // #tr Tooltip_BiosphereIII_Mode1_02
    // # Advanced artificial intelligence controls the breeding of bacteria
    // #zh_CN 使用先进的人工智能控制细菌的繁殖
    public static final String Tooltip_BiosphereIII_Mode1_02 = TextEnums.tr("Tooltip_BiosphereIII_Mode1_02");

    // #tr Tooltip_BiosphereIII_Mode1_03
    // # Original efficiency of control is {\RED}40%{\GRAY} of the maximum. Each glass tier over recipe requirement improve the efficiency by {\RED}15%{\GRAY}.
    // #zh_CN 初始控制效率为最高效率的{\RED}160%{\GRAY}.玻璃等级每超出配方要求1级,将控制效率提高{\RED}60%{\GRAY}.
    public static final String Tooltip_BiosphereIII_Mode1_03 = TextEnums.tr("Tooltip_BiosphereIII_Mode1_03");

    // #tr BiosphereIII_Mode_02
    // # {\GOLD}----- Brewing Machine Mode -----
    // #zh_CN {\GOLD}----- 酿造室模式 -----
    public static final String BiosphereIII_Mode_02 = TextEnums.tr("BiosphereIII_Mode_02");

    // #tr BiosphereIII_Mode_03
    // # {\GOLD}----- Fermenter Mode -----
    // #zh_CN {\GOLD}----- 发酵槽模式 -----
    public static final String BiosphereIII_Mode_03 = TextEnums.tr("BiosphereIII_Mode_03");

    // #tr Tooltip_BiosphereIII_Mode2n3_00
    // # Don't need Petri Dish
    // #zh_CN 不需要培养皿.加速300%%.
    public static final String Tooltip_BiosphereIII_Mode2n3_00 = TextEnums.tr("Tooltip_BiosphereIII_Mode2n3_00");

    // #tr Tooltip_BiosphereIII_Mode2n3_01
    // # Each glass tier over HV provides 4 times parallel
    // #zh_CN 玻璃等级每超出HV一级,提供4倍并行
    public static final String Tooltip_BiosphereIII_Mode2n3_01 = TextEnums.tr("Tooltip_BiosphereIII_Mode2n3_01");

    // #tr Tooltip_BiosphereIII_Mode2n3_02
    // # EV glass provides {\AQUA}4x{\GRAY} parallel, IV glass provides {\AQUA}16x{\GRAY} parallel, etc.
    // #zh_CN EV玻璃提供{\AQUA}4x{\GRAY}并行,IV玻璃提供{\AQUA}16x{\GRAY}并行,以此类推
    public static final String Tooltip_BiosphereIII_Mode2n3_02 = TextEnums.tr("Tooltip_BiosphereIII_Mode2n3_02");

    // #tr textBiosphereIIIRadioHatch
    // # Radiation Hatch: 0-1x
    // #zh_CN 放射仓: 0-1x
    public static final String textBiosphereIIIRadioHatch = TextEnums.tr("textBiosphereIIIRadioHatch");

    // #tr textBiosphereIIIHatchLocation
    // # Any Bottom Clean Stainless Steel Machine Casing
    // #zh_CN 任意底层洁净不锈钢机械方块
    public static final String textBiosphereIIIHatchLocation = TextEnums.tr("textBiosphereIIIHatchLocation");

    // #tr BiosphereIIIEfficiency
    // # Efficiency:
    // #zh_CN 效率:
    public static final String BiosphereIIIEfficiency = TextEnums.tr("BiosphereIIIEfficiency");

    //endregion

    // region Egg Generator

    // #tr NameMegaEggGenerator
    // # Tower of Abstraction
    // #zh_CN 抽象之塔
    public static final String NameMegaEggGenerator = TextEnums.tr("NameMegaEggGenerator");

    // #tr Tooltip_MegaEggGenerator_MachineType
    // # Magical Energy Absorber
    // #zh_CN 魔法能源吸收者
    public static final String Tooltip_MegaEggGenerator_MachineType = TextEnums.tr("Tooltip_MegaEggGenerator_MachineType");

    // #tr Tooltip_MegaEggGenerator_Controller
    // # Controller block for the Tower of Abstraction
    // #zh_CN 抽象之塔的控制器
    public static final String Tooltip_MegaEggGenerator_Controller = TextEnums.tr("Tooltip_MegaEggGenerator_Controller");

    // #tr Tooltip_MegaEggGenerator_00
    // # This is where the {\RED}ulti{\AQUA}mate{\GOLD} des{\BLUE}tiny{\GRAY} of the Dragon's Children lies.
    // #zh_CN 这是龙之子嗣的{\RED}终{\AQUA}极{\GOLD}宿{\BLUE}命{\GRAY}所在.
    public static final String Tooltip_MegaEggGenerator_00 = TextEnums.tr("Tooltip_MegaEggGenerator_00");

    // #tr Tooltip_MegaEggGenerator_01
    // # With the help of the Magic Egg, it draws in endless magical power as effortlessly as a soul-sucking sorcerer.
    // #zh_CN 借助魔法之卵，汲取无尽魔力，如同{\GOLD}{\ITALIC}{\BOLD}吸魂巫师{\GRAY}般不费吹灰之力。
    public static final String Tooltip_MegaEggGenerator_01 = TextEnums.tr("Tooltip_MegaEggGenerator_01");

    // #tr Tooltip_MegaEggGenerator_02
    // # Every dragon egg generates {\GOLD}1A EV{\GRAY} & every creeper's generates {\GOLD}1A HV{\GRAY}.
    // #zh_CN 每个龙蛋将产生{\GOLD}1A EV{\GRAY},而爬行者蛋产生{\GOLD}1A HV{\GRAY}.
    public static final String Tooltip_MegaEggGenerator_02 = TextEnums.tr("Tooltip_MegaEggGenerator_02");

    // #tr Tooltip_MegaEggGenerator_03
    // # Infinity egg generates {\GOLD}2A IV{\GRAY}, but you can put only one for one each piece you add.
    // #zh_CN 每个无限之蛋产生{\GOLD}2A IV{\GRAY},但无限之蛋的总数不得多于层数.
    public static final String Tooltip_MegaEggGenerator_03 = TextEnums.tr("Tooltip_MegaEggGenerator_03");

    // #tr Tooltip_MegaEggGenerator_04
    // # But quantitative change leads to qualitative change,
    // #zh_CN 数量积累至一定阶段，便能引发{\BOLD}{\ITALIC}{\GOLD}质的飞跃,
    public static final String Tooltip_MegaEggGenerator_04 = TextEnums.tr("Tooltip_MegaEggGenerator_04");

    // #tr Tooltip_MegaEggGenerator_05
    // # Every 2^n pieces give {\RED}2%{\GRAY} max efficiency bonus, and every infinity egg gives {\RED}1%{\GRAY}.
    // #zh_CN 每2^n层提供{\RED}2%{\GRAY}的最大效率加成,而每个无限之蛋提高{\RED}1%{\GRAY}.
    public static final String Tooltip_MegaEggGenerator_05 = TextEnums.tr("Tooltip_MegaEggGenerator_05");

    // #tr Tooltip_MegaEggGenerator_06
    // # You can also put nothing on the egg pos, but every empty pos decreases {\RED}5%{\GRAY} max efficiency.
    // #zh_CN 在这能量的殿堂里，每一个未被诸蛋之力占据的空缺，都将令整体的效率减损{\RED}5%{\GRAY}。
    public static final String Tooltip_MegaEggGenerator_06 = TextEnums.tr("Tooltip_MegaEggGenerator_06");

    // #tr Tooltip_MegaEggGenerator_07
    // # Warm-up process for 0.01%% per second as base, each pair of Dragon Eggs add 0.01%% but limit for 50 pairs, each Infinity Egg add {\RED}1%{\GRAY} with no limits.
    // #zh_CN 基础预热速度0.01%%每秒，一对龙蛋增加0.01%%但最多只计算50对，无限之蛋增加{\RED}1%{\GRAY}且无加速上限.
    public static final String Tooltip_MegaEggGenerator_07 = TextEnums.tr("Tooltip_MegaEggGenerator_07");

    // #tr Tooltip_MegaEggGenerator_08
    // # The process will take about {\GOLD}3{\GRAY} hours if put Creeper Eggs only, but who really care about?
    // #zh_CN 如果仅有爬行者蛋，预热将需要大约三小时，只是谁会真的在乎这个?
    public static final String Tooltip_MegaEggGenerator_08 = TextEnums.tr("Tooltip_MegaEggGenerator_08");

    // #tr Tooltip_MegaEggGenerator_09
    // # Whether it is the {\DARK_PURPLE} Dragon Egg, {\DARK_GREEN} Creeper Egg, or the {\GOLD}Egg of Infinity, {\GRAY}only their presence can drive the full circulation of power.
    // #zh_CN 无论是{\DARK_PURPLE}龙蛋{\RESET}、{\GREEN}爬行者蛋{\GRAY}，抑或是{\ITALIC}{\GOLD}无尽之蛋{\GRAY}，唯有它们的存在，方能驱动力量的完满流转。
    public static final String Tooltip_MegaEggGenerator_09 = TextEnums.tr("Tooltip_MegaEggGenerator_09");

    // #tr Tooltip_MegaEggGenerator_D
    // # Dynamo or TT Dynamo, one only
    // #zh_CN 动力仓或TecTech动力仓,1个
    public static final String Tooltip_MegaEggGenerator_D = TextEnums.tr("Tooltip_MegaEggGenerator_D");

    // #tr Tooltip_MegaEggGenerator_M
    // # No need for maintenance hatch.
    // #zh_CN 不需要维护仓.
    public static final String Tooltip_MegaEggGenerator_M = TextEnums.tr("Tooltip_MegaEggGenerator_M");

    // #tr Tooltip_MegaEggGenerator_L
    // # Lasers unlock at >=16 pieces.
    // #zh_CN 激光在16层及以上解锁.
    public static final String Tooltip_MegaEggGenerator_L = TextEnums.tr("Tooltip_MegaEggGenerator_L");

    // #tr Tooltip_MegaEggGenerator_C
    // # Any Magical Casing.
    // #zh_CN 任何魔法机械外壳
    public static final String Tooltip_MegaEggGenerator_C = TextEnums.tr("Tooltip_MegaEggGenerator_C");

    // endregion

    // region IndistinctTentacle

    // #tr NameIndistinctTentacle
    // # {\BOLD}{\DARK_GRAY}Indistinct Tentacle
    // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}
    public static final String NameIndistinctTentacle = TextEnums.tr("NameIndistinctTentacle");

    // #tr Tooltip_IndistinctTentacle_MachineType
    // # Assembly Line | Component Assembly Line | Assembler | Precise Assembler
    // #zh_CN 装配线 | 部件装配线 | 组装机 | 精密组装机
    public static final String Tooltip_IndistinctTentacle_MachineType = TextEnums.tr("Tooltip_IndistinctTentacle_MachineType");

    // #tr Tooltip_IndistinctTentacle_Controller
    // # Controller block for the Indistinct Tentacle
    // #zh_CN 不可视之触的控制器方块
    public static final String Tooltip_IndistinctTentacle_Controller = TextEnums.tr("Tooltip_IndistinctTentacle_Controller");

    // #tr Tooltip_IndistinctTentacle_01
    // # {\BOLD}{\ITALIC}In the midst of this sea and endless solitude there appears a dim road, a road without human footprints.
    // #zh_CN {\BOLD}{\ITALIC}在这片海和无尽的孤独中出现了一条昏暗的道路，一条没有人类足迹的道路。
    public static final String Tooltip_IndistinctTentacle_01 = TextEnums.tr("Tooltip_IndistinctTentacle_01");

    // #tr Tooltip_IndistinctTentacle_02
    // # {\BOLD}{\ITALIC}No man has ever passed this place; no ship has ever sailed here.
    // #zh_CN {\BOLD}{\ITALIC}没有人曾经过此地；亦没有船只曾在此航行。
    public static final String Tooltip_IndistinctTentacle_02 = TextEnums.tr("Tooltip_IndistinctTentacle_02");

    // #tr Tooltip_IndistinctTentacle_03
    // # Made everything in its where should be.
    // #zh_CN 让所有事情都各得其所.
    public static final String Tooltip_IndistinctTentacle_03 = TextEnums.tr("Tooltip_IndistinctTentacle_03");

    // #tr Tooltip_IndistinctTentacle_04
    // # Glass tier limit energy hatch, laser hatch need UV glass.
    // #zh_CN 玻璃限制能源仓等级, 激光仓需要UV玻璃.
    public static final String Tooltip_IndistinctTentacle_04 = TextEnums.tr("Tooltip_IndistinctTentacle_04");

    // #tr Tooltip_IndistinctTentacle_05
    // # Component Casing tier limit recipe can process. Casing tier require at least the recipe voltage level -1.
    // #zh_CN 部件装配外壳等级限制可执行配方等级. 外壳等级最少需要配方电压等级-1.
    public static final String Tooltip_IndistinctTentacle_05 = TextEnums.tr("Tooltip_IndistinctTentacle_05");

    // #tr Tooltip_IndistinctTentacle_06
    // # If Component Casing tier is higher than recipe voltage, enable §cPerfect Overclock§7.
    // #zh_CN 如果部件装配外壳等级高于配方等级则启用{\RED}无损超频{\GRAY}.
    public static final String Tooltip_IndistinctTentacle_06 = TextEnums.tr("Tooltip_IndistinctTentacle_06");

    // #tr Tooltip_IndistinctTentacle_07
    // # UMV+ glass and Component Casing allow Wireless mode by placing no energy hatch.
    // #zh_CN {\LIGHT_PURPLE}{\BOLD}UMV{\GRAY}+ 玻璃与部件装配外壳允许使用无线电网模式(无能源仓自动进入).
    public static final String Tooltip_IndistinctTentacle_07 = TextEnums.tr("Tooltip_IndistinctTentacle_07");

    // #tr Tooltip_IndistinctTentacle_08
    // # Progressing time is fixed in Wireless mode.
    // #zh_CN 无线电网模式下处理时间是固定的.
    public static final String Tooltip_IndistinctTentacle_08 = TextEnums.tr("Tooltip_IndistinctTentacle_08");

    // #tr Tooltip_IndistinctTentacle_09
    // # Watch out your Global energy storage if use wireless mode, you should not want to see the power drain's landscape.
    // #zh_CN 注意你的无线电网电量, 你应该不会想看到跳电的风景对吧.
    public static final String Tooltip_IndistinctTentacle_09 = TextEnums.tr("Tooltip_IndistinctTentacle_09");

    // #tr Tooltip_IndistinctTentacle_2_01
    // # Speed (default) of mode:
    // #zh_CN 模式下速度乘数(默认) :
    public static final String Tooltip_IndistinctTentacle_2_01 = TextEnums.tr("Tooltip_IndistinctTentacle_2_01");

    // #tr Tooltip_IndistinctTentacle_2_02
    // #     Assembly Line = 100%%
    // #zh_CN     装配线 = 100%%
    public static final String Tooltip_IndistinctTentacle_2_02 = TextEnums.tr("Tooltip_IndistinctTentacle_2_02");

    // #tr Tooltip_IndistinctTentacle_2_03
    // #     Component Assembly Line = 200%%;
    // #zh_CN     部件装配线 = 200%%
    public static final String Tooltip_IndistinctTentacle_2_03 = TextEnums.tr("Tooltip_IndistinctTentacle_2_03");

    // #tr Tooltip_IndistinctTentacle_2_04
    // #     Assembler = 400%%
    // #zh_CN     组装机 = 400%%
    public static final String Tooltip_IndistinctTentacle_2_04 = TextEnums.tr("Tooltip_IndistinctTentacle_2_04");

    // #tr Tooltip_IndistinctTentacle_2_05
    // #     Precise Assembler = 400%%
    // #zh_CN     精密组装机 = 400%%
    public static final String Tooltip_IndistinctTentacle_2_05 = TextEnums.tr("Tooltip_IndistinctTentacle_2_05");

    // #tr Tooltip_IndistinctTentacle_2_06
    // # Default power mode parallel 256.
    // #zh_CN 默认能源仓模式下256并行.
    public static final String Tooltip_IndistinctTentacle_2_06 = TextEnums.tr("Tooltip_IndistinctTentacle_2_06");

    // #tr Tooltip_IndistinctTentacle_2_07
    // # Progressing time is fixed at 25.6s in Wireless mode.
    // #zh_CN 无线电网模式下处理时间固定为25.6s.
    public static final String Tooltip_IndistinctTentacle_2_07 = TextEnums.tr("Tooltip_IndistinctTentacle_2_07");

    // #tr Tooltip_IndistinctTentacle_2_08
    // # Put an Astral Array Fabricator into controller slot,
    // #zh_CN 在控制器方块放入 {\AQUA}{\BOLD}{\ITALIC}星阵{\GRAY} ,
    public static final String Tooltip_IndistinctTentacle_2_08 = TextEnums.tr("Tooltip_IndistinctTentacle_2_08");

    // #tr Tooltip_IndistinctTentacle_2_09
    // #   the Progressing time will be fixed at 1.0s, and EU cost increase to 64 times.
    // #zh_CN   则处理时间固定为1.0s, 同时耗能提高到{\RED}64{\GRAY}倍.
    public static final String Tooltip_IndistinctTentacle_2_09 = TextEnums.tr("Tooltip_IndistinctTentacle_2_09");

    // endregion

    // region ThermalEnergyDevourer

    // #tr NameThermalEnergyDevourer
    // # Thermal Energy Devourer
    // #zh_CN 热能饕餮
    public static final String NameThermalEnergyDevourer = TextEnums.tr("NameThermalEnergyDevourer");

    // #tr Tooltip_ThermalEnergyDevourer_MachineType
    // # Vacuum Freezer
    // #zh_CN 真空冷冻机
    public static final String Tooltip_ThermalEnergyDevourer_MachineType = TextEnums.tr("Tooltip_ThermalEnergyDevourer_MachineType");

    // #tr Tooltip_ThermalEnergyDevourer_Controller
    // # Controller block for the Thermal Energy Devourer
    // #zh_CN 热能饕餮的控制器方块
    public static final String Tooltip_ThermalEnergyDevourer_Controller = TextEnums.tr("Tooltip_ThermalEnergyDevourer_Controller");

    // #tr Tooltip_ThermalEnergyDevourer_01
    // # {\WHITE}{\BOLD}{\ITALIC} It's White Album season again.
    // #zh_CN {\WHITE}{\BOLD}{\ITALIC} 又到了白色相簿的季节.
    public static final String Tooltip_ThermalEnergyDevourer_01 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_01");

    // #tr Tooltip_ThermalEnergyDevourer_02
    // # The thermal energy it greedily devours.
    // #zh_CN 它贪婪地吞噬着热量.
    public static final String Tooltip_ThermalEnergyDevourer_02 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_02");

    // #tr Tooltip_ThermalEnergyDevourer_03
    // # But overall it's still a Freezer, and works well.
    // #zh_CN 但总的来说仍是一台冷冻机, 而且工作得不错.
    public static final String Tooltip_ThermalEnergyDevourer_03 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_03");

    // #tr Tooltip_ThermalEnergyDevourer_04
    // # Increasing the input power will also increase the operating efficiency.
    // #zh_CN 提高输入功率的同时会提高运行效率.
    public static final String Tooltip_ThermalEnergyDevourer_04 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_04");

    // #tr Tooltip_ThermalEnergyDevourer_05
    // # {\GOLD} ----- High Speed Mode -----
    // #zh_CN {\GOLD} ----- 高速模式 -----
    public static final String Tooltip_ThermalEnergyDevourer_05 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_05");

    // #tr Tooltip_ThermalEnergyDevourer_06
    // # Operating efficiency decrease the progress time directly.
    // #zh_CN 运行效率直接降低处理耗时.
    public static final String Tooltip_ThermalEnergyDevourer_06 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_06");

    // #tr Tooltip_ThermalEnergyDevourer_07
    // # {\AQUA}1024x{\GRAY} parallel.
    // #zh_CN {\AQUA}1024x{\GRAY} 并行.
    public static final String Tooltip_ThermalEnergyDevourer_07 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_07");

    // #tr Tooltip_ThermalEnergyDevourer_08
    // # {\GOLD} ----- High Parallel Mode -----
    // #zh_CN {\GOLD} ----- 饕餮模式 -----
    public static final String Tooltip_ThermalEnergyDevourer_08 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_08");

    // #tr Tooltip_ThermalEnergyDevourer_09
    // # Operating efficiency decrease the EU cost.
    // #zh_CN 运行效率降低EU消耗.
    public static final String Tooltip_ThermalEnergyDevourer_09 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_09");

    // #tr Tooltip_ThermalEnergyDevourer_10
    // # Almost {\AQUA}infinite parallel{\GRAY}.
    // #zh_CN 几乎无限的并行.
    public static final String Tooltip_ThermalEnergyDevourer_10 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_10");

    // #tr Tooltip_ThermalEnergyDevourer_11
    // # {\GOLD} ----- Wireless Mode -----
    // #zh_CN {\GOLD} ----- 无线模式 -----
    public static final String Tooltip_ThermalEnergyDevourer_11 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_11");

    // #tr Tooltip_ThermalEnergyDevourer_12
    // # Put {\AQUA}Energised Tesseract{\GRAY} into controller slot to turn into Wireless mode.
    // #zh_CN 在控制器内放入{\AQUA}充能超立方体{\GRAY}以切换至无线模式.
    public static final String Tooltip_ThermalEnergyDevourer_12 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_12");

    // #tr Tooltip_ThermalEnergyDevourer_13
    // # Directly consume required EU from wireless EU network.
    // #zh_CN 直接在无线EU网络中消耗所需的能量.
    public static final String Tooltip_ThermalEnergyDevourer_13 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_13");

    // #tr Tooltip_ThermalEnergyDevourer_14
    // # Progress time is fixed at {\GOLD}6.4s{\GRAY}.
    // #zh_CN 处理时间固定为{\GOLD}6.4s{\GRAY}.
    public static final String Tooltip_ThermalEnergyDevourer_14 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_14");

    // #tr Tooltip_ThermalEnergyDevourer_2_01
    // # Check whether turn into Wireless mode when checking structure.
    // #zh_CN 检查结构时检测是否进入无线模式.
    public static final String Tooltip_ThermalEnergyDevourer_2_01 = TextEnums.tr("Tooltip_ThermalEnergyDevourer_2_01");

    // endregion

    // region Debug Uncertainty Hatch

    // #tr NameDebugUncertaintyHatch
    // # Debug Uncertainty Hatch
    // #zh_CN Debug未定元解析器
    public static final String NameDebugUncertaintyHatch = TextEnums.tr("NameDebugUncertaintyHatch");

    // endregion

    // #tr NameLaserSmartNode
    // # Laser Smart Node
    // #zh_CN 激光智能节点
    public static final String NameLaserSmartNode = TextEnums.tr("NameLaserSmartNode");

    // #tr NameLaserFocusedSmartNode
    // # Laser-Focused Smart Node
    // #zh_CN 激光聚焦智能节点
    public static final String NameLaserFocusedSmartNode = TextEnums.tr("NameLaserFocusedSmartNode");

    // region Vacuum Filter Extractor

    // #tr NameVacuumFilterExtractor
    // # Vacuum Filter Extractor
    // #zh_CN 真空抽滤器
    public static final String NameVacuumFilterExtractor = TextEnums.tr("NameVacuumFilterExtractor");

    // #tr Tooltip_VacuumFilterExtractor_MachineType
    // # Distillation Tower | Distillery
    // #zh_CN 蒸馏塔 | 蒸馏室
    public static final String Tooltip_VacuumFilterExtractor_MachineType = TextEnums.tr("Tooltip_VacuumFilterExtractor_MachineType");

    // #tr Tooltip_VacuumFilterExtractor_Controller
    // # Controller block for the Vacuum Filter Extractor
    // #zh_CN 真空抽滤器的控制器方块
    public static final String Tooltip_VacuumFilterExtractor_Controller = TextEnums.tr("Tooltip_VacuumFilterExtractor_Controller");

    // #tr Tooltip_VacuumFilterExtractor_01
    // # {\ITALIC}Engineers think something isn't broken because it has too few features.
    // #zh_CN {\ITALIC}工程师认为东西没坏是它功能太少.
    public static final String Tooltip_VacuumFilterExtractor_01 = TextEnums.tr("Tooltip_VacuumFilterExtractor_01");

    // #tr Tooltip_VacuumFilterExtractor_02
    // # By manipulating space in order to achieve separation of matter
    // #zh_CN 通过操控空间以实现分离物质,
    public static final String Tooltip_VacuumFilterExtractor_02 = TextEnums.tr("Tooltip_VacuumFilterExtractor_02");

    // #tr Tooltip_VacuumFilterExtractor_03
    // #   rather than direct manipulation of matter.
    // #zh_CN   而非直接操控物质.
    public static final String Tooltip_VacuumFilterExtractor_03 = TextEnums.tr("Tooltip_VacuumFilterExtractor_03");

    // #tr Tooltip_VacuumFilterExtractor_04
    // # Recipe voltage is only {\RED}50%{\GRAY} of normal.
    // #zh_CN 只需要正常配方电压的{\RED}50%{\GRAY}.
    public static final String Tooltip_VacuumFilterExtractor_04 = TextEnums.tr("Tooltip_VacuumFilterExtractor_04");

    // #tr Tooltip_VacuumFilterExtractor_05
    // # Increasing the energy input will result in more speed boosts.
    // #zh_CN 提高能量输入将提供更多的速度提升.
    public static final String Tooltip_VacuumFilterExtractor_05 = TextEnums.tr("Tooltip_VacuumFilterExtractor_05");

    // #tr Tooltip_VacuumFilterExtractor_06
    // # In distillery mode, machine will enable {\AQUA}Perfect Overclock{\GRAY}.
    // #zh_CN 蒸馏室模式将启用{\AQUA}无损超频{\GRAY}.
    public static final String Tooltip_VacuumFilterExtractor_06 = TextEnums.tr("Tooltip_VacuumFilterExtractor_06");

    // endregion

    // region Large Steam Forge Hammer

    // #tr NameLargeSteamForgeHammer
    // # Large Steam Forge Hammer
    // #zh_CN 大型蒸汽锻造锤
    public static final String NameLargeSteamForgeHammer = TextEnums.tr("NameLargeSteamForgeHammer");

    // #tr Tooltip_LargeSteamForgeHammer_MachineType
    // # Forge Hammer
    // #zh_CN 锻造锤
    public static final String Tooltip_LargeSteamForgeHammer_MachineType = TextEnums.tr("Tooltip_LargeSteamForgeHammer_MachineType");

    // #tr Tooltip_LargeSteamForgeHammer_Controller
    // # Controller block for the Large Steam Forge Hammer
    // #zh_CN 大型蒸汽锻造锤的控制器方块
    public static final String Tooltip_LargeSteamForgeHammer_Controller = TextEnums.tr("Tooltip_LargeSteamForgeHammer_Controller");

    // #tr Tooltip_LargeSteamForgeHammer_01
    // # He has a hammer. Who has the Sickle?
    // #zh_CN 他有一柄锤子. 谁有镰刀?
    public static final String Tooltip_LargeSteamForgeHammer_01 = TextEnums.tr("Tooltip_LargeSteamForgeHammer_01");

    // endregion

    // region Large Steam Alloy Smelter

    // #tr NameLargeSteamAlloySmelter
    // # Large Steam Alloy Smelter
    // #zh_CN 大型蒸汽合金炉
    public static final String NameLargeSteamAlloySmelter = TextEnums.tr("NameLargeSteamAlloySmelter");

    // #tr Tooltip_LargeSteamAlloySmelter_MachineType
    // # Alloy Smelter
    // #zh_CN 合金炉
    public static final String Tooltip_LargeSteamAlloySmelter_MachineType = TextEnums.tr("Tooltip_LargeSteamAlloySmelter_MachineType");

    // #tr Tooltip_LargeSteamAlloySmelter_Controller
    // # Controller block for the Large Steam Alloy Smelter
    // #zh_CN 大型蒸汽合金炉的控制器方块
    public static final String Tooltip_LargeSteamAlloySmelter_Controller = TextEnums.tr("Tooltip_LargeSteamAlloySmelter_Controller");

    // #tr Tooltip_LargeSteamAlloySmelter_01
    // # Steam Tech Operational
    // #zh_CN 蒸汽科技，启动！
    public static final String Tooltip_LargeSteamAlloySmelter_01 = TextEnums.tr("Tooltip_LargeSteamAlloySmelter_01");

    // endregion

    // region Eye Of Wood

    // #tr NameEyeOfWood
    // # Eye of Wood
    // #zh_CN 武德之眼
    public static final String NameEyeOfWood = TextEnums.tr("NameEyeOfWood");

    // #tr Tooltip_EyeOfWood_MachineType
    // # Happiness Master
    // #zh_CN Happiness Master
    public static final String Tooltip_EyeOfWood_MachineType = TextEnums.tr("Tooltip_EyeOfWood_MachineType");

    // #tr Tooltip_EyeOfWood_Controller
    // # Controller block for the Eye of Wood
    // #zh_CN 武德之眼的控制器方块
    public static final String Tooltip_EyeOfWood_Controller = TextEnums.tr("Tooltip_EyeOfWood_Controller");

    // #tr Tooltip_EyeOfWood_01
    // # You'd better to do a sanity check, if you are looking at this.
    // #zh_CN 如果你在看这个东西的话, 你最好去检查一下你的san值.
    public static final String Tooltip_EyeOfWood_01 = TextEnums.tr("Tooltip_EyeOfWood_01");

    // endregion

    // region Bee Engineer

    // #tr NameBeeEngineer
    // # Bee Engineer (Prototype)
    // #zh_CN 蜜蜂操纵者 (Prototype)
    public static final String NameBeeEngineer = TextEnums.tr("NameBeeEngineer");

    // #tr Tooltip_BeeEngineer_Type
    // # Bee Engineer
    // #zh_CN 蜜蜂操纵者
    public static final String Tooltip_BeeEngineer_Type = TextEnums.tr("Tooltip_BeeEngineer_Type");

    // #tr Tooltip_BeeEngineer_Controller
    // # Controller of the Bee Engineer
    // #zh_CN 蜜蜂操纵者的控制器
    public static final String Tooltip_BeeEngineer_Controller = TextEnums.tr("Tooltip_BeeEngineer_Controller");

    // #tr Tooltip_BeeEngineer_01
    // # Still in test.
    // #zh_CN 其实还在测试中.
    public static final String Tooltip_BeeEngineer_01 = TextEnums.tr("Tooltip_BeeEngineer_01");

    // #tr Tooltip_BeeEngineer_02
    // # Transforming drones into princesses.
    // #zh_CN 将雄蜂转化为公主蜂.
    public static final String Tooltip_BeeEngineer_02 = TextEnums.tr("Tooltip_BeeEngineer_02");

    // #tr Tooltip_BeeEngineer_03
    // # Who knows how many drones became stepping stones for the Last Queen?
    // #zh_CN 谁知道有多少雄蜂成为了蜂后的垫脚石？
    public static final String Tooltip_BeeEngineer_03 = TextEnums.tr("Tooltip_BeeEngineer_03");

    // #tr Tooltip_BeeEngineer_04
    // # Cost {\GOLD}128kL{\GRAY} Honey to transform a drone into princess, but with {\RED}40%{\GRAY} failing chance.
    // #zh_CN 消耗{\GOLD}128k{\GRAY}L蜂蜜, 将雄蜂转化为公主蜂，但失败几率为{\RED}40%{\GRAY}.
    public static final String Tooltip_BeeEngineer_04 = TextEnums.tr("Tooltip_BeeEngineer_04");

    // #tr Tooltip_BeeEngineer_05
    // # Will try to consume {\GOLD}32kL{\GRAY} UUM (if exists) to increase success rate to {\RED}80%{\GRAY}.
    // #zh_CN 将尝试消耗{\GOLD}32kL{\GRAY}的UU物质(如果存在)以将成功率提高到{\RED}80%{\GRAY}.
    public static final String Tooltip_BeeEngineer_05 = TextEnums.tr("Tooltip_BeeEngineer_05");

    // #tr Tooltip_BeeEngineer_06
    // # In case of failure, all consumed ingredients will not be returned.
    // #zh_CN 在失败的情况下, 所有投入的原料都不会返还.
    public static final String Tooltip_BeeEngineer_06 = TextEnums.tr("Tooltip_BeeEngineer_06");

    // #tr Tooltip_BeeEngineer_07
    // # Don't put too many drones in at once, that will result in a long run time!
    // #zh_CN 不要一次性放入太多雄蜂, 那会导致运行时间过长!
    public static final String Tooltip_BeeEngineer_07 = TextEnums.tr("Tooltip_BeeEngineer_07");

    // region Mega Macerator

    // #tr NameMegaMacerator
    // # "Mini" Household Cell Fragmentizer
    // #zh_CN "小型"家用破壁机
    public static final String NameMegaMacerator = TextEnums.tr("NameMegaMacerator");

    // #tr Tooltip_MegaMacerator_MachineType
    // # Macerator
    // #zh_CN Macerator
    public static final String Tooltip_MegaMacerator_MachineType = TextEnums.tr("Tooltip_MegaMacerator_Type");

    // #tr Tooltip_MegaMacerator_Controller
    // # Controller block for the "Mini" Household Cell Fragmentizer
    // #zh_CN "小型"家用破壁机的控制方块
    public static final String Tooltip_MegaMacerator_Controller = TextEnums.tr("Tooltip_MegaMacerator_Controller");

    // #tr Tooltip_MegaMacerator_01
    // # Squeezed Collision of Material.
    // #zh_CN {\GOLD}物质的挤压碰撞
    public static final String Tooltip_MegaMacerator_01 = TextEnums.tr("Tooltip_MegaMacerator_01");

    // #tr Tooltip_MegaMacerator_02
    // # This is way better than a forge hammer.
    // #zh_CN 这玩意可比锻造锤好用多了
    public static final String Tooltip_MegaMacerator_02 = TextEnums.tr("Tooltip_MegaMacerator_02");

    // #tr Tooltip_MegaMacerator_03
    // # Can parallel up to {\AQUA}2 ^ (2 ^ (Tier + 2) - 1){\GRAY}.
    // #zh_CN 最大并行：{\AQUA}2 ^ (2 ^ (等级 + 2) - 1){\GRAY}
    public static final String Tooltip_MegaMacerator_03 = TextEnums.tr("Tooltip_MegaMacerator_03");

    // #tr Tooltip_MegaMacerator_04
    // # Tier is determined by cotainment block: Damascus Steel, Neutronium, Universium.
    // #zh_CN 取决于可选方块:大马士革钢、中子、宇宙素
    public static final String Tooltip_MegaMacerator_04 = TextEnums.tr("Tooltip_MegaMacerator_04");

    // #tr Tooltip_MegaMacerator_05
    // # The max voltage tier is limited by the glass tier.
    // #zh_CN 玻璃等级限制可执行配方等级
    public static final String Tooltip_MegaMacerator_05 = TextEnums.tr("Tooltip_MegaMacerator_05");

    // #tr Tooltip_MegaMacerator_06
    // # Enable {\RED}8x{\GRAY} speed multiplier when glass tier > recipe tier.
    // #zh_CN 当玻璃等级高于配方等级时获得{\RED}8x{\GRAY}倍速
    public static final String Tooltip_MegaMacerator_06 = TextEnums.tr("Tooltip_MegaMacerator_06");

    // endregion

    // region HephaestusAtelier

    // #tr NameHephaestusAtelier
    // # Hephaestus' Atelier
    // #zh_CN 赫菲斯托斯的工坊
    public static final String NameHephaestusAtelier = TextEnums.tr("NameHephaestusAtelier");

    // #tr Tooltip_HephaestusAtelier_MachineType
    // # Furnace | Alloy Smelter
    // #zh_CN 熔炉 | 合金炉
    public static final String Tooltip_HephaestusAtelier_MachineType = TextEnums.tr("Tooltip_HephaestusAtelier_MachineType");

    // #tr Tooltip_HephaestusAtelier_Controller
    // # Controller block for the Hephaestus' Atelier
    // #zh_CN 赫菲斯托斯的工坊的控制器方块
    public static final String Tooltip_HephaestusAtelier_Controller = TextEnums.tr("Tooltip_HephaestusAtelier_Controller");

    // #tr Tooltip_HephaestusAtelier_01
    // # {\DARK_RED}{\ITALIC}“And first Hephaestus makes a great and massive shield ...
    // #zh_CN {\DARK_RED}{\ITALIC}“最先，火神赫菲斯托斯做了一个超级厉害的巨大盾牌 ...
    public static final String Tooltip_HephaestusAtelier_01 = TextEnums.tr("Tooltip_HephaestusAtelier_01");

    // #tr Tooltip_HephaestusAtelier_02
    // # {\DARK_RED}{\ITALIC}    ... And he forged on the shield two noble cities.”
    // #zh_CN {\DARK_RED}{\ITALIC}    ... 他在盾牌上锻造了两座宏伟城市。”
    public static final String Tooltip_HephaestusAtelier_02 = TextEnums.tr("Tooltip_HephaestusAtelier_02");

    // #tr Tooltip_HephaestusAtelier_03
    // # Even in the future, the most primitive means of smelting will be needed.
    // #zh_CN 即使是在未来, 这最原始的冶炼手段也是有需要的.
    public static final String Tooltip_HephaestusAtelier_03 = TextEnums.tr("Tooltip_HephaestusAtelier_03");

    // #tr Tooltip_HephaestusAtelier_04
    // # Parallelism across recipes is possible, even using Crafting Input hatches.
    // #zh_CN 可以跨配方并行, 即便使用样板输入仓室.
    public static final String Tooltip_HephaestusAtelier_04 = TextEnums.tr("Tooltip_HephaestusAtelier_04");

    // #tr Tooltip_HephaestusAtelier_05
    // # The machine will adjust its operation according to the installed coil level.
    // #zh_CN 机器将根据线圈等级调整自身运行状态.
    public static final String Tooltip_HephaestusAtelier_05 = TextEnums.tr("Tooltip_HephaestusAtelier_05");

    // #tr Tooltip_HephaestusAtelier_06
    // # {\GOLD} ----- T1 Coil : Molecular Coil -----
    // #zh_CN {\GOLD} ----- T1 线圈 : 分子线圈 -----
    public static final String Tooltip_HephaestusAtelier_06 = TextEnums.tr("Tooltip_HephaestusAtelier_06");

    // #tr Tooltip_HephaestusAtelier_07
    // # Like other normal machine, use energy from energy hatches and do overclock.
    // #zh_CN 和其他普通机器一样, 使用能源仓获取能源, 进行超频.
    public static final String Tooltip_HephaestusAtelier_07 = TextEnums.tr("Tooltip_HephaestusAtelier_07");

    // #tr Tooltip_HephaestusAtelier_08
    // # {\AQUA}2.1G{\GRAY} parallel. Furnace mode every item smelting consume {\GOLD}7 EU/t{\GRAY} and {\GOLD}25.6s{\GRAY}.
    // #zh_CN {\AQUA}2.1G{\GRAY} 并行. 熔炉模式每冶炼一个物品消耗 {\GOLD}7 EU/t{\GRAY} 和耗时 {\GOLD}25.6s{\GRAY}.
    public static final String Tooltip_HephaestusAtelier_08 = TextEnums.tr("Tooltip_HephaestusAtelier_08");

    // #tr Tooltip_HephaestusAtelier_09
    // # {\GOLD} ----- T2 Coil : Ultimate Containment Field Generator -----
    // #zh_CN {\GOLD} ----- T2 线圈 : 终极遏制场发生器 -----
    public static final String Tooltip_HephaestusAtelier_09 = TextEnums.tr("Tooltip_HephaestusAtelier_09");

    // #tr Tooltip_HephaestusAtelier_10
    // # Directly consume energy from wireless EU net.
    // #zh_CN 直接从无线EU网络获取能量.(无线模式).
    public static final String Tooltip_HephaestusAtelier_10 = TextEnums.tr("Tooltip_HephaestusAtelier_10");

    // #tr Tooltip_HephaestusAtelier_11
    // # {\AQUA}Infinite parallel{\GRAY}. Furnace mode every item smelting consume 2048 EU.
    // #zh_CN 无限并行. 熔炉模式每冶炼一个物品消耗 {\GOLD}2048 EU{\GRAY}.
    public static final String Tooltip_HephaestusAtelier_11 = TextEnums.tr("Tooltip_HephaestusAtelier_11");

    // #tr Tooltip_HephaestusAtelier_12
    // # Processing time is fixed at {\GOLD}12.8s{\GRAY}.
    // #zh_CN 每次运行时间固定为 {\GOLD}12.8s{\GRAY}.
    public static final String Tooltip_HephaestusAtelier_12 = TextEnums.tr("Tooltip_HephaestusAtelier_12");

    // #tr Tooltip_HephaestusAtelier_13
    // # {\GOLD} ----- T3 Coil : Teleportation Casing -----
    // #zh_CN {\GOLD} ----- T3 线圈 : 传输机械方块 -----
    public static final String Tooltip_HephaestusAtelier_13 = TextEnums.tr("Tooltip_HephaestusAtelier_13");

    // #tr Tooltip_HephaestusAtelier_14
    // # Processing time {\GOLD}1s{\GRAY}.
    // #zh_CN 每次运行时间固定为 {\GOLD}1s{\GRAY}.
    public static final String Tooltip_HephaestusAtelier_14 = TextEnums.tr("Tooltip_HephaestusAtelier_14");

    // #tr Tooltip_HephaestusAtelier_15
    // # Otherwise same as T2.
    // #zh_CN 其他方面与 T2 相同.
    public static final String Tooltip_HephaestusAtelier_15 = TextEnums.tr("Tooltip_HephaestusAtelier_15");

    // #tr Tooltip_HephaestusAtelier_2_01
    // # Must install energy hatch when in T1.
    // #zh_CN T1等级线圈(普通模式)时必须安装能源仓.
    public static final String Tooltip_HephaestusAtelier_2_01 = TextEnums.tr("Tooltip_HephaestusAtelier_2_01");

    // #tr NameFackRackHatch
    // # rack simulation hack
    // #zh_CN 机箱模拟器
    public static final String NameFackRackHatch = TextEnums.tr("NameFackRackHatch");

    // #tr NameRealRackHatch
    // # rack simulation controller hack
    // #zh_CN 机箱控制器
    public static final String NameRealRackHatch = TextEnums.tr("NameRealRackHatch");

    // #tr NameAstralComputingArray
    // # Astral Computing Array
    // #zh_CN 星规阵列
    public static final String NameAstralComputingArray = TextEnums.tr("NameAstralComputingArray");

    // #tr NameWirelessDataInputHatch
    // # Wireless Optical Slave Connector
    // #zh_CN 无线副光学接口
    public static final String NameWirelessDataInputHatch = TextEnums.tr("NameWirelessDataInputHatch");

    // #tr NameWirelessDataOutputHatch
    // # Wireless Optical Master Connector
    // #zh_CN 无线主光学接口
    public static final String NameWirelessDataOutputHatch = TextEnums.tr("NameWirelessDataOutputHatch");

    // endregion

    // region Ball Lightning

    // #tr NameBallLightning
    // # BallLightning
    // #zh_CN 球状闪电
    public static final String NameBallLightning = TextEnums.tr("NameBallLightning");

    // endregion

    // region Deployed Nano Core

    // #tr NameDeployedNanoCore
    // # Deployed Nano Core
    // #zh_CN 展开的纳米核心
    public static final String NameDeployedNanoCore = TextEnums.tr("NameDeployedNanoCore");

    // #tr Tooltip_DeployedNanoCore_MachineType
    // # Nano Forge
    // #zh_CN 纳米锻炉
    public static final String Tooltip_DeployedNanoCore_MachineType = TextEnums.tr("Tooltip_DeployedNanoCore_MachineType");

    // #tr Tooltip_DeployedNanoCore_Controller
    // # Controller block for the Deployed Nano Core
    // #zh_CN 展开的纳米核心的控制器方块
    public static final String Tooltip_DeployedNanoCore_Controller = TextEnums.tr("Tooltip_DeployedNanoCore_Controller");

    // #tr Tooltip_DeployedNanoCore_01
    // # {\WHITE}{\ITALIC}If you shed tears when you miss the sun, you also miss the stars.
    // #zh_CN {\WHITE}{\ITALIC}如果你因错过太阳而流泪, 你也会错过繁星.
    public static final String Tooltip_DeployedNanoCore_01 = TextEnums.tr("Tooltip_DeployedNanoCore_01");

    // #tr Tooltip_DeployedNanoCore_02
    // # It'll take care of itself.
    // #zh_CN 它们会打理好的.
    public static final String Tooltip_DeployedNanoCore_02 = TextEnums.tr("Tooltip_DeployedNanoCore_02");

    // #tr Tooltip_DeployedNanoCore_03
    // # With perfect overclock and {\AQUA}infinite parallel{\GRAY}.
    // #zh_CN 以无损超频和无限并行.
    public static final String Tooltip_DeployedNanoCore_03 = TextEnums.tr("Tooltip_DeployedNanoCore_03");

    // #tr Tooltip_DeployedNanoCore_04
    // # What are you worried about?
    // #zh_CN 你在担心什么?
    public static final String Tooltip_DeployedNanoCore_04 = TextEnums.tr("Tooltip_DeployedNanoCore_04");

    // endregion

    //region Space Apiary

    // #tr NameSpaceApiaryT1
    // # Space Apiray Module MK-I
    // #zh_CN 太空蜂箱模块 MK-I
    public static final String NameSpaceApiaryT1 = TextEnums.tr("NameSpaceApiaryT1");

    // #tr Tooltip_SpaceApiary_desc0
    // # Module that adds Space Apiary Operations to the Space Elevator
    // #zh_CN 将太空蜂箱功能添加到太空电梯
    public static final String Tooltip_SpaceApiary_desc0 = TextEnums.tr("Tooltip_SpaceApiary_desc0");

    // #tr Tooltip_SpaceApiary_t1_desc1
    // # Time to let the lazy bees absorb some cosmic rays.
    // #zh_CN 该让懒惰的蜜蜂们晒晒太阳了
    public static final String Tooltip_SpaceApiary_t1_desc1 = TextEnums.tr("Tooltip_SpaceApiary_t1_desc1");

    // #tr Tooltip_SpaceApiary_desc1
    // # Accurately distort bee working progress in low-gravity environments.
    // #zh_CN 在低重力环境中精准扭曲蜜蜂工作时间.
    public static final String Tooltip_SpaceApiary_desc1 = TextEnums.tr("Tooltip_SpaceApiary_desc1");

    // #tr Tooltip_SpaceApiary_desc2
    // # Need queen bee in controller slot.
    // #zh_CN 需要在控制器中放入蜂后.
    public static final String Tooltip_SpaceApiary_desc2 = TextEnums.tr("Tooltip_SpaceApiary_desc2");

    // #tr Tooltip_SpaceApiary_t1_desc3
    // # Consumes {\GOLD}1A-Luv{\GRAY} per parallel, while consuming {\GOLD}100L{\GRAY} liquid DNA/parallel per run.
    // #zh_CN Consumes {\GOLD}1A{\GRAY}-Luv per parallel, while consuming {\GOLD}100L{\GRAY} liquid DNA/parallel per run.
    public static final String Tooltip_SpaceApiary_t1_desc3 = TextEnums.tr("Tooltip_SpaceApiary_desc3");

    // #tr Tooltip_SpaceApiary_t2_desc3
    // # Consumes {\GOLD}1A-Luv{\GRAY} per parallel, while consuming {\GOLD}25L{\GRAY} liquid DNA/parallel per run.
    // #zh_CN 每并行消耗 {\GOLD}1A-Luv{\GRAY} ，同时每次运行消耗 {\GOLD}25L{\GRAY} 液态DNA/并行.
    public static final String Tooltip_SpaceApiary_t2_desc3 = TextEnums.tr("Tooltip_SpaceApiary_t2_desc3");

    // #tr Tooltip_SpaceApiary_t3_desc3
    // # Consumes {\GOLD}1A-Luv{\GRAY} per parallel, while consuming {\GOLD}5L{\GRAY} liquid DNA/parallel per run.
    // #zh_CN 每并行消耗 {\GOLD}1A-Luv{\GRAY} ，同时每次运行消耗 {\GOLD}5L{\GRAY} 液态DNA/并行.
    public static final String Tooltip_SpaceApiary_t3_desc3 = TextEnums.tr("Tooltip_SpaceApiary_t3_desc3");

    // #tr Tooltip_SpaceApiary_t4_desc3
    // # Consumes {\GOLD}1A-Luv{\GRAY} per parallel, while consuming {\GOLD}1L{\GRAY} liquid DNA/parallel per run.
    // #zh_CN 每并行消耗 {\GOLD}1A-Luv{\GRAY} ，同时每次运行消耗 {\GOLD}1L{\GRAY} 液态DNA/并行.
    public static final String Tooltip_SpaceApiary_t4_desc3 = TextEnums.tr("Tooltip_SpaceApiary_t4_desc3");

    // #tr Tooltip_SpaceApiary_t1_desc4
    // # Max parallels: {\AQUA}256{\GRAY}
    // #zh_CN 最大并行：{\AQUA}256{\GRAY}
    public static final String Tooltip_SpaceApiary_t1_desc4 = TextEnums.tr("Tooltip_SpaceApiary_t1_desc4");

    // #tr NameSpaceApiaryT2
    // # Space Apiray Module MK-II
    // #zh_CN 太空蜂箱模块 MK-II
    public static final String NameSpaceApiaryT2 = TextEnums.tr("NameSpaceApiaryT2");

    // #tr Tooltip_SpaceApiary_t2_desc1
    // # Surrender to the hive mind..
    // #zh_CN 臣服于蜂巢思维..
    public static final String Tooltip_SpaceApiary_t2_desc1 = TextEnums.tr("Tooltip_SpaceApiary_t2_desc1");

    // #tr Tooltip_SpaceApiary_t2_desc4
    // # Max parallels: {\AQUA}4096{\GRAY}
    // #zh_CN 最大并行：{\AQUA}4096{\GRAY}
    public static final String Tooltip_SpaceApiary_t2_desc4 = TextEnums.tr("Tooltip_SpaceApiary_t2_desc4");

    // #tr NameSpaceApiaryT3
    // # Space Apiray Module MK-III
    // #zh_CN 太空蜂箱模块 MK-III
    public static final String NameSpaceApiaryT3 = TextEnums.tr("NameSpaceApiaryT3");

    // #tr Tooltip_SpaceApiary_t3_desc1
    // # The Great Creator of the Void!
    // #zh_CN 伟大的虚空造物主!
    public static final String Tooltip_SpaceApiary_t3_desc1 = TextEnums.tr("Tooltip_SpaceApiary_t3_desc1");

    // #tr Tooltip_SpaceApiary_t3_desc4
    // # Max parallels: {\AQUA}32768{\GRAY}
    // #zh_CN 最大并行：{\AQUA}32768{\GRAY}
    public static final String Tooltip_SpaceApiary_t3_desc4 = TextEnums.tr("Tooltip_SpaceApiary_t3_desc4");

    // #tr NameSpaceApiaryT4
    // # Space Apiray Module MK-IV
    // #zh_CN 太空蜂箱模块 MK-IV
    public static final String NameSpaceApiaryT4 = TextEnums.tr("NameSpaceApiaryT4");

    // #tr Tooltip_SpaceApiary_t4_desc1
    // # Bees give birth to all things to nourish humanity.
    // #zh_CN Bee生万物以养人
    public static final String Tooltip_SpaceApiary_t4_desc1 = TextEnums.tr("Tooltip_SpaceApiary_t4_desc1");

    // #tr Tooltip_SpaceApiary_t4_desc4
    // # Max parallels: {\AQUA}2147483647{\GRAY}
    // #zh_CN 最大并行：{\AQUA}2147483647{\GRAY}
    public static final String Tooltip_SpaceApiary_t4_desc4 = TextEnums.tr("Tooltip_SpaceApiary_t4_desc4");

    // #tr Tooltip_SpaceApiary_t4_desc5
    // # But humans have nothing to repay the bees.
    // #zh_CN 人无一物以报Bee.
    public static final String Tooltip_SpaceApiary_t4_desc5 = TextEnums.tr("Tooltip_SpaceApiary_t4_desc5");

    // endregion

    // region TST Cleanroom

    // #tr NamesuperCleanRoom
    // # CleanRoom
    // #zh_CN TST超净间
    public static final String NamesuperCleanRoom = TextEnums.tr("NamesuperCleanRoom");

    // endregion

    // region TST_SteamBasicGenerator
    public static final String Tooltip_SteamBasicGenerator_MachineType = TextEnums.tr("Tooltip_SteamBasicGenerator_MachineType");
    public static final String Tooltip_SteamBasicGenerator_00 = TextEnums.tr("Tooltip_SteamBasicGenerator_00");
    public static final String Tooltip_SteamBasicGenerator_01 = TextEnums.tr("Tooltip_SteamBasicGenerator_01");
    public static final String Tooltip_SteamBasicGenerator_02 = TextEnums.tr("Tooltip_SteamBasicGenerator_02");
    public static final String Tooltip_SteamBasicGenerator_03 = TextEnums.tr("Tooltip_SteamBasicGenerator_03");
    // endregion

    // region TST_SteamBasicGenerator
    public static final String Tooltip_UniversalGenerator_MachineType = TextEnums.tr("Tooltip_UniversalGenerator_MachineType");
    public static final String Tooltip_UniversalGenerator_00 = TextEnums.tr("Tooltip_UniversalGenerator_00");
    public static final String Tooltip_UniversalGenerator_01 = TextEnums.tr("Tooltip_UniversalGenerator_01");
    public static final String Tooltip_UniversalGenerator_02 = TextEnums.tr("Tooltip_UniversalGenerator_02");
    public static final String Tooltip_UniversalGenerator_03 = TextEnums.tr("Tooltip_UniversalGenerator_03");
    // endregion


}
// spotless:on
