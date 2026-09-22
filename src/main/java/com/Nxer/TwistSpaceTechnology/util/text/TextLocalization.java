package com.Nxer.TwistSpaceTechnology.util.text;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.Tags;
import com.Nxer.TwistSpaceTechnology.util.TstSharedLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

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
    // # Added by %s
    // #zh_CN 由 %s 添加
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

    // Mobs cannot Spawn on this Block
    public static final String mNoMobsToolTip = GTLanguageManager.getTranslation("gt.nomobspawnsonthisblock");
    // This is NOT a TileEntity!
    public static final String mNoTileEntityToolTip = GTLanguageManager.getTranslation("gt.notileentityinthisblock");
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

    // endregion

    // region Precise High-Energy Photonic Quantum Master text localization

    // #tr NamePreciseHighEnergyPhotonicQuantumMaster
    // # Precise High-Energy Photonic Quantum Master
    // #zh_CN 精密高能光量子掌控者
    public static final String NamePreciseHighEnergyPhotonicQuantumMaster = TextEnums.tr("NamePreciseHighEnergyPhotonicQuantumMaster");

    // endregion

    // region MiracleTop

    // #tr NameMiracleTop
    // # Miracle Top
    // #zh_CN 奇迹顶点
    public static final String NameMiracleTop = TextEnums.tr("NameMiracleTop");

    // endregion

    // region Magnetic Drive Pressure Former

    // #tr NameMagneticDrivePressureFormer
    // # Magnetic Drive Pressure Former
    // #zh_CN 磁驱压力成型机
    public static final String NameMagneticDrivePressureFormer = TextEnums.tr("NameMagneticDrivePressureFormer");

    // endregion

    // region Physical Form Switcher

    // #tr NamePhysicalFormSwitcher
    // # Physical Form Switcher
    // #zh_CN 物质形态转换器
    public static final String NamePhysicalFormSwitcher = TextEnums.tr("NamePhysicalFormSwitcher");

    // endregion

    // region Magnetic Mixer

    // #tr NameMagneticMixer
    // # "Mini" Magnetic Mixer
    // #zh_CN "小型"磁力搅拌机
    public static final String NameMagneticMixer = TextEnums.tr("NameMagneticMixer");

    // endregion

    // region MagneticDomainConstructor

    // #tr NameMagneticDomainConstructor
    // # Magnetic Domain Constructor
    // #zh_CN 磁畴构建器
    public static final String NameMagneticDomainConstructor = TextEnums.tr("NameMagneticDomainConstructor");

    // endregion

    // region Silksong

    // #tr NameSilksong
    // # Silksong
    // #zh_CN 丝之歌
    public static final String NameSilksong = TextEnums.tr("NameSilksong");

    // endregion

    // region HolySeparator

    // #tr NameHolySeparator
    // # Holy Separator
    // #zh_CN 神圣分离者
    public static final String NameHolySeparator = TextEnums.tr("NameHolySeparator");

    // endregion

    // region SpaceScaler

    // #tr NameSpaceScaler
    // # Space Scaler
    // #zh_CN 空间缩放仪
    public static final String NameSpaceScaler = TextEnums.tr("NameSpaceScaler");

    // endregion

    // region MoleculeDeconstructor

    // #tr NameMoleculeDeconstructor
    // # Molecule Deconstructor
    // #zh_CN 分子解构器
    public static final String NameMoleculeDeconstructor = TextEnums.tr("NameMoleculeDeconstructor");

    // endregion

    // region CrystallineInfinitier

    // #tr NameCrystallineInfinitier
    // # Crystalline Infinitier
    // #zh_CN 无限晶胞
    public static final String NameCrystallineInfinitier = TextEnums.tr("NameCrystallineInfinitier");

    // endregion

    // region DSPLauncher
    // #tr NameDSPLauncher
    // # Dyson Sphere Module Launch Site
    // #zh_CN 戴森球模块发射场
    public static final String NameDSPLauncher = TextEnums.tr("NameDSPLauncher");

    // endregion

    // region DSPReceiver

    // #tr NameDSPReceiver
    // # Dyson Sphere Ray Receiving Station
    // #zh_CN 戴森球射线接收站
    public static final String NameDSPReceiver = TextEnums.tr("NameDSPReceiver");

    // endregion

    // region ArtificialStar

    // #tr NameArtificialStar
    // # Artificial Star
    // #zh_CN 人造恒星
    public static final String NameArtificialStar = TextEnums.tr("NameArtificialStar");

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

    // endregion

    // region OreProcessingFactory

    // #tr NameOreProcessingFactory
    // # General Ore Processing Factory TST
    // #zh_CN 通用矿物处理厂TST
    public static final String NameOreProcessingFactory = TextEnums.tr("NameOreProcessingFactory");

    // endregion

    // region CircuitConverter

    // #tr NameCircuitConverter
    // # General Circuit Converter
    // #zh_CN 通用电路板转换器
    public static final String NameCircuitConverter = TextEnums.tr("NameCircuitConverter");

    // endregion

    // region LargeIndustrialCokingFactory

    // #tr NameLargeIndustrialCokingFactory
    // # Large Industrial Coking Factory
    // #zh_CN 大型工业炼焦厂
    public static final String NameLargeIndustrialCokingFactory = TextEnums.tr("NameLargeIndustrialCokingFactory");

    // endregion

    // region Elvenworkshop

    // #tr NameElvenWorkshop
    // # ElvenWorkshop
    // #zh_CN 精灵工坊
    public static final String NameElvenWorkshop = TextEnums.tr("NameElvenWorkshop");

    // endregion

    // region HyperSpacetimeTransformer

    // #tr NameHyperSpacetimeTransformer
    // # HyperSpacetimeTransformer
    // #zh_CN 极限时空转换仪
    public static final String NameHyperSpacetimeTransformer = TextEnums.tr("NameHyperSpacetimeTransformer");

    // endregion

    // region Scavenger

    // #tr NameScavenger
    // # Scavenger
    // #zh_CN 拾荒者
    public static final String NameScavenger = TextEnums.tr("NameScavenger");

    // endregion

    // region AdvancedMegaOilCracker

    // #tr NameAdvancedMegaOilCracker
    // # Advanced Mega Oil Cracker
    // #zh_CN 进阶巨型石油裂化机
    public static final String NameAdvancedMegaOilCracker = TextEnums.tr("NameAdvancedMegaOilCracker");

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

    // region Space machines

    // #tr NameMegaUniversalSpaceStation
    // # Mega Universal Space Station
    // #zh_CN {\RED}寰 {\AQUA}宇 {\GOLD}空 {\BLUE}间 {\DARK_GRAY}站
    public static final String NameMegaUniversalSpaceStation = TextEnums.tr("NameMegaUniversalSpaceStation");

    // #tr NameStellarMaterialSiphon
    // # Stellar Material Siphon
    // #zh_CN Stellar Material Siphon
    public static final String NameStellarMaterialSiphon = TextEnums.tr("NameStellarMaterialSiphon");

    // endregion

    // region MegaPrimitiveBlastFurnace

    // #tr NameMegaBrickedBlastFurnace
    // # Mega Bricked Blast Furnace
    // #zh_CN 巨型砖高炉
    public static final String NameMegaBrickedBlastFurnace = TextEnums.tr("NameMegaBrickedBlastFurnace");

    // endregion

    // region BiosphereIII

    // #tr NameBiosphereIII
    // # Biosphere III
    // #zh_CN 生物圈III号
    public static final String NameBiosphereIII = TextEnums.tr("NameBiosphereIII");

    // endregion

    // region Egg Generator

    // #tr NameMegaEggGenerator
    // # Tower of Abstraction
    // #zh_CN 抽象之塔
    public static final String NameMegaEggGenerator = TextEnums.tr("NameMegaEggGenerator");

    // endregion

    // region IndistinctTentacle

    // #tr NameIndistinctTentacle
    // # {\BOLD}{\DARK_GRAY}Indistinct Tentacle
    // #zh_CN {\DARK_GRAY}{\BOLD}不可视之触{\RESET}
    public static final String NameIndistinctTentacle = TextEnums.tr("NameIndistinctTentacle");

    // endregion

    // region ThermalEnergyDevourer

    // #tr NameThermalEnergyDevourer
    // # Thermal Energy Devourer
    // #zh_CN 热能饕餮
    public static final String NameThermalEnergyDevourer = TextEnums.tr("NameThermalEnergyDevourer");

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

    // endregion

    // region Large Steam Forge Hammer

    // #tr NameLargeSteamForgeHammer
    // # Large Steam Forge Hammer
    // #zh_CN 大型蒸汽锻造锤
    public static final String NameLargeSteamForgeHammer = TextEnums.tr("NameLargeSteamForgeHammer");

    // endregion

    // region Large Steam Alloy Smelter

    // #tr NameLargeSteamAlloySmelter
    // # Large Steam Alloy Smelter
    // #zh_CN 大型蒸汽合金炉
    public static final String NameLargeSteamAlloySmelter = TextEnums.tr("NameLargeSteamAlloySmelter");

    // endregion

    // region Eye Of Wood

    // #tr NameEyeOfWood
    // # Eye of Wood
    // #zh_CN 武德之眼
    public static final String NameEyeOfWood = TextEnums.tr("NameEyeOfWood");

    // endregion

    // region Bee Engineer

    // #tr NameBeeEngineer
    // # Bee Engineer (Prototype)
    // #zh_CN 蜜蜂操纵者 (Prototype)
    public static final String NameBeeEngineer = TextEnums.tr("NameBeeEngineer");

    // endregion

    // region Mega Macerator

    // #tr NameMegaMacerator
    // # "Mini" Household Cell Fragmentizer
    // #zh_CN "小型"家用破壁机
    public static final String NameMegaMacerator = TextEnums.tr("NameMegaMacerator");

    // endregion

    // region HephaestusAtelier

    // #tr NameHephaestusAtelier
    // # Hephaestus' Atelier
    // #zh_CN 赫菲斯托斯的工坊
    public static final String NameHephaestusAtelier = TextEnums.tr("NameHephaestusAtelier");

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

    // endregion

    // region Space Apiary

    // #tr NameSpaceApiaryT1
    // # Space Apiray Module MK-I
    // #zh_CN 太空蜂箱模块 MK-I
    public static final String NameSpaceApiaryT1 = TextEnums.tr("NameSpaceApiaryT1");

    // #tr NameSpaceApiaryT2
    // # Space Apiray Module MK-II
    // #zh_CN 太空蜂箱模块 MK-II
    public static final String NameSpaceApiaryT2 = TextEnums.tr("NameSpaceApiaryT2");

    // #tr NameSpaceApiaryT3
    // # Space Apiray Module MK-III
    // #zh_CN 太空蜂箱模块 MK-III
    public static final String NameSpaceApiaryT3 = TextEnums.tr("NameSpaceApiaryT3");

    // #tr NameSpaceApiaryT4
    // # Space Apiray Module MK-IV
    // #zh_CN 太空蜂箱模块 MK-IV
    public static final String NameSpaceApiaryT4 = TextEnums.tr("NameSpaceApiaryT4");

    // endregion

    // region TST Cleanroom

    // #tr NamesuperCleanRoom
    // # CleanRoom
    // #zh_CN TST超净间
    public static final String NamesuperCleanRoom = TextEnums.tr("NamesuperCleanRoom");

    // endregion

}
// spotless:on
