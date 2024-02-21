package com.Nxer.TwistSpaceTechnology.util;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.Tags;

// spotless:off
/**
 * <li>It's best to write here the texts that need auto generate en_US.lang .
 * <li>Or some usually used texts.
 */
public class TextLocalization {

    // region general
    public static final String ModName = Tags.MODNAME;

    public static final String ModNameDesc = texter("Added by " + EnumChatFormatting.GREEN + "Twist Space Technology" + EnumChatFormatting.GRAY, "ModNameDesc");

    public static final String HeatCapacity = texter("Heat Capacity: ", "HeatCapacity");
    public static final String FluidCapacity = texter("Capacity: ", "FluidCapacity");
    public static final String HatchTier = texter("Hatch Tier: ", "HatchTier");
    public static final String Kelvin = texter(" K", "Kelvin");
    public static final String Text_SeparatingLine = EnumChatFormatting.GOLD + "-----------------------------------------";
    public static final String BLUE_PRINT_INFO = texter(
        "Follow the" + EnumChatFormatting.BLUE
            + " Structure"
            + EnumChatFormatting.DARK_BLUE
            + "Lib"
            + EnumChatFormatting.GRAY
            + " hologram projector to build the main structure.",
        "BLUE_PRINT_INFO");

    public static final String DSPName = texter(EnumChatFormatting.BLUE+"Dyson Sphere Program","DSPName");
    public static final String Tooltip_Details = texter(EnumChatFormatting.LIGHT_PURPLE+"Details: ","Tooltip_Details");
    public static final String Tooltips_JoinWirelessNetWithoutEnergyHatch = texter("Joining the wireless EU network when without installing an energy hatch.","Tooltips_JoinWirelessNetWithoutEnergyHatch");
    // endregion

    // region Waila
    public static final String Waila_WirelessMode = texter("Wireless Mode","Waila.General.WirelessMode");
    public static final String Waila_CurrentEuCost = texter("Current EU Cost","Waila.General.CurrentEuCost");
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

    public static final String authorName_Nxer = "Author: "+name_Nxer;
    // endregion

    // region special hatch info
    public static final String Tooltip_DoNotNeedMaintenance = texter("Do Not Need Maintenance!","Tooltip_DoNotNeedMaintenance");
    public static final String Tooltip_DoNotNeedEnergyHatch = texter("Do Not Need Energy Hatch!","Tooltip_DoNotNeedEnergyHatch");

    // endregion

    // region get info message
    public static final String infoText_CurrentStellarCoefficient = texter("Current Stellar Coefficient: ","infoText_CurrentStellarCoefficient");
    public static final String infoText_CurrentPlanetCoefficient = texter("Current Planet Coefficient: ","infoText_CurrentPlanetCoefficient");

    // endregion

    // region casing
    public static final String textCasing = texter("Casing", "textCasing");
    public static final String textUseBlueprint = texter("Use "+EnumChatFormatting.BLUE+"Blue"+EnumChatFormatting.AQUA+"print"+EnumChatFormatting.RESET+" to preview", "textUseBlueprint");
    public static final String textAnyCasing = texter("Any Casing", "textAnyCasing");
    public static final String textTopCenter = texter("Top center", "textTopCenter");
    public static final String textFrontCenter = texter("Front center", "textFrontCenter");
    public static final String textFrontBottom = texter("Front bottom", "textFrontBottom");
    public static final String textCenterOfLRSides = texter("Center area of left and right side", "textCenterOfLRSides");
    public static final String textCenterOfUDSides = texter("Center area of up and down side", "textCenterOfUDSides");
    public static final String StructureTooComplex = texter("The structure is too complex!", "StructureTooComplex");
    public static final String textCasingAdvIrPlated = texter("Advanced Iridium Plated Machine Casing", "textCasingAdvIrPlated");
    public static final String textCasingTT_0 = texter("High Power Casing", "textCasingTT_0");
    public static final String textAroundController = texter("Around the Controller", "textAroundController");

    public static final String textScrewdriverChangeMode = texter("Use screwdriver to change mode.", "textScrewdriverChangeMode");

    // endregion

    // region general tooltips
    public static final String Tooltip_GlassTierLimitEnergyHatchTier = texter("The Glass Tier limit the Energy hatch voltage Tier.","Tooltip_GlassTierLimitEnergyHatchTier");


    // endregion

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

    public static final String NameMiracleTop = texter("Miracle Top", "NameMiracleTop");

    public static final String Tooltip_MiracleTop_MachineType = texter("Circuit Assembler/Gravitation Breaker", "Tooltip_MiracleTop_MachineType");
    public static final String Tooltip_MiracleTop_00 = texter("Controller block for the Miracle Top.", "Tooltip_MiracleTop_00");
    public static final String Tooltip_MiracleTop_01 = texter(EnumChatFormatting.LIGHT_PURPLE+"I never think about the future because it will come sooner or later.", "Tooltip_MiracleTop_01");
    public static final String Tooltip_MiracleTop_02 = texter("For absolute precision and efficiency, please abandon traditional manufacturing methods.", "Tooltip_MiracleTop_02");
    public static final String Tooltip_MiracleTop_03 = texter("The machine consists of a ring section and a conveying section.", "Tooltip_MiracleTop_03");
    public static final String Tooltip_MiracleTop_04 = texter("The number of rings is variable:  Maximum 16 rings, Minimum 2 rings(the first and the last).", "Tooltip_MiracleTop_04");
    public static final String Tooltip_MiracleTop_05 = texter("Total speed multiplier is equal to 400%% x num of rings.", "Tooltip_MiracleTop_05");
    public static final String Tooltip_MiracleTop_06 = texter("Enable Perfect overclock when num of rings >= 8 .", "Tooltip_MiracleTop_06");
    public static final String Tooltip_MiracleTop_07 = texter("128x Parallel per Ring.", "Tooltip_MiracleTop_07");
    public static final String textMiracleTopHatchLocation = texter("Outermost 12 blocks on the ring (outermost 3 on each side).", "textMiracleTopHatchLocation");

    // endregion

    // region Magnetic Drive Pressure Former

    public static final String NameMagneticDrivePressureFormer = texter("Magnetic Drive Pressure Former","NameMagneticDrivePressureFormer");
    public static final String Tooltip_MagneticDrivePressureFormer_MachineType = texter("Extruder | Bending Machine | Forming Press", "Tooltip_MagneticDrivePressureFormer_MachineType");
    public static final String Tooltip_MagneticDrivePressureFormer_00 = texter("Controller block for the Magnetic Drive Pressure Former.", "Tooltip_MagneticDrivePressureFormer_00");
    public static final String Tooltip_MagneticDrivePressureFormer_01 = texter(EnumChatFormatting.AQUA + "Simple applications of Maxwell's equations.", "Tooltip_MagneticDrivePressureFormer_01");
    public static final String Tooltip_MagneticDrivePressureFormer_02 = texter("No difficulty ! No hurry !", "Tooltip_MagneticDrivePressureFormer_02");
    public static final String Tooltip_MagneticDrivePressureFormer_03 = texter(EnumChatFormatting.GOLD + "Extruder Mode:", "Tooltip_MagneticDrivePressureFormer_03");
    public static final String Tooltip_MagneticDrivePressureFormer_04 = texter("700%% faster than normal | Infinity Coil+ enable Perfect Overclock", "Tooltip_MagneticDrivePressureFormer_04");
    public static final String Tooltip_MagneticDrivePressureFormer_05 = texter(EnumChatFormatting.GOLD + "Bending and Forming Press and Forge Hammer Mode:", "Tooltip_MagneticDrivePressureFormer_05");
    public static final String Tooltip_MagneticDrivePressureFormer_06 = texter("1500%% faster than normal | Enable Perfect Overclock", "Tooltip_MagneticDrivePressureFormer_06");
    public static final String Tooltip_MagneticDrivePressureFormer_07 = texter("Extra +100%% speed multiplier per Coil Level.", "Tooltip_MagneticDrivePressureFormer_07");
    public static final String Tooltip_MagneticDrivePressureFormer_08 = texter("Need Infinity Glass to use Laser energy hatch.", "Tooltip_MagneticDrivePressureFormer_08");
    public static final String Tooltip_MagneticDrivePressureFormer_09 = texter("1024x Parallel.", "Tooltip_MagneticDrivePressureFormer_09");
    public static final String Tooltip_MagneticDrivePressureFormer_Hatches = texter("Frame location, Osmiridium Casing.", "Tooltip_MagneticDrivePressureFormer_Hatches");
    public static final String Tooltip_MagneticDrivePressureFormer_EnergyHatch = texter("The white, Iridium Casing, and the bottom center.", "Tooltip_MagneticDrivePressureFormer_EnergyHatch");

    // endregion

    // region Physical Form Switcher

    public static final String NamePhysicalFormSwitcher = texter("Physical Form Switcher","NamePhysicalFormSwitcher");
    public static final String Tooltip_PhysicalFormSwitcher_MachineType = texter("Fluid Solidifier | Fluid Extractor | Forge Hammer", "Tooltip_PhysicalFormSwitcher_MachineType");
    public static final String Tooltip_PhysicalFormSwitcher_00 = texter("Controller block for the Physical Form Switcher", "Tooltip_PhysicalFormSwitcher_00");
    public static final String Tooltip_PhysicalFormSwitcher_01 = texter(EnumChatFormatting.YELLOW + "Forming Master !", "Tooltip_PhysicalFormSwitcher_01");
    public static final String Tooltip_PhysicalFormSwitcher_02 = texter("The ultimate method of melt operation.", "Tooltip_PhysicalFormSwitcher_02");
    public static final String Tooltip_PhysicalFormSwitcher_03 = texter("Has parallel equivalent to Perfect Overclock.", "Tooltip_PhysicalFormSwitcher_03");
    public static final String Tooltip_PhysicalFormSwitcher_04 = texter("Additional 10%% reduction in time per Voltage Tier, multiplication calculus.", "Tooltip_PhysicalFormSwitcher_04");
    public static final String Tooltip_PhysicalFormSwitcher_05 = texter("The Glass Tier limit the recipe voltage tier.", "Tooltip_PhysicalFormSwitcher_05");

    // endregion

    // region Magnetic Mixer
    public static final String NameMagneticMixer = texter("\"Mini\" Magnetic Mixer","NameMagneticMixer");
    public static final String Tooltip_MagneticMixer_MachineType = texter("Mixer", "Tooltip_MagneticMixer_MachineType");
    public static final String Tooltip_MagneticMixer_00 = texter("Controller block for the \"Mini\" Magnetic Mixer", "Tooltip_MagneticMixer_00");
    public static final String Tooltip_MagneticMixer_01 = texter(EnumChatFormatting.RED +  "Watch out for the Bumps !", "Tooltip_MagneticMixer_01");
    public static final String Tooltip_MagneticMixer_02 = texter("Looks more like a tumble washing machine.", "Tooltip_MagneticMixer_02");
    public static final String Tooltip_MagneticMixer_03 = texter("Has parallel equivalent to Perfect Overclock.", "Tooltip_MagneticMixer_03");
    public static final String Tooltip_MagneticMixer_04 = texter("Additional 20%% reduction in time per Voltage Tier, multiplication calculus.", "Tooltip_MagneticMixer_04");

    // endregion

    // region MagneticDomainConstructor
    public static final String NameMagneticDomainConstructor = texter("Magnetic Domain Constructor","NameMagneticDomainConstructor");
    public static final String Tooltip_MagneticDomainConstructor_MachineType = texter("Electromagnetic Separator | Electromagnetic Polarizer","Tooltip_MagneticDomainConstructor_MachineType");
    public static final String Tooltip_MagneticDomainConstructor_00 = texter("Controller block for the Magnetic Domain Constructor","Tooltip_MagneticDomainConstructor_00");
    public static final String Tooltip_MagneticDomainConstructor_01 = texter(EnumChatFormatting.DARK_GRAY + "Don't give up your imagination.","Tooltip_MagneticDomainConstructor_01");
    public static final String Tooltip_MagneticDomainConstructor_02 = texter("Controlling the magnetic domains inside the crystal, yes that's it.","Tooltip_MagneticDomainConstructor_02");
    public static final String Tooltip_MagneticDomainConstructor_03 = texter("8x Parallel per Ring.(Don't use a lot of blueprints when first scanning.)","Tooltip_MagneticDomainConstructor_03");
    public static final String Tooltip_MagneticDomainConstructor_04 = texter("Additional 10%% reduction in time per Voltage Tier, multiplication calculus.","Tooltip_MagneticDomainConstructor_04");

    // endregion

    // region Silksong
    public static final String NameSilksong = texter("Silksong","NameSilksong");
    public static final String Tooltip_Silksong_MachineType = texter("Wiremill","Tooltip_Silksong_MachineType");
    public static final String Tooltip_Silksong_00 = texter("Controller block for the Silksong","Tooltip_Silksong_00");
    public static final String Tooltip_Silksong_01 = texter(EnumChatFormatting.WHITE+"Maybe dreams aren't such a good thing ......","Tooltip_Silksong_01");
    public static final String Tooltip_Silksong_02 = texter("Endless cables spew from this machine.","Tooltip_Silksong_02");
    public static final String Tooltip_Silksong_03 = texter("24x Parallel per piece.","Tooltip_Silksong_03");
    public static final String Tooltip_Silksong_04 = texter("Additional 10%% reduction in time per Coil Tier, multiplication calculus.","Tooltip_Silksong_04");
    // endregion

    // region HolySeparator
    public static final String NameHolySeparator = texter("Holy Separator", "NameHolySeparator");
    public static final String Tooltip_HolySeparator_MachineType = texter("Cutter | Slicer | Lathe","Tooltip_HolySeparator_MachineType");
    public static final String Tooltip_HolySeparator_00 = texter("Controller block for the Holy Separator","Tooltip_HolySeparator_00");
    public static final String Tooltip_HolySeparator_01 = texter(EnumChatFormatting.YELLOW+"Precision "+EnumChatFormatting.GRAY+"and "+EnumChatFormatting.AQUA+"Grace.","Tooltip_HolySeparator_01");
    public static final String Tooltip_HolySeparator_02 = texter("Another form of laser engraving.","Tooltip_HolySeparator_02");
    public static final String Tooltip_HolySeparator_03 = texter("You can even slice potato chips with this.","Tooltip_HolySeparator_03");
    public static final String Tooltip_HolySeparator_04 = texter("Extra 8x Parallel per Piece. 16 Piece enable Perfect Overclock.","Tooltip_HolySeparator_04");
    public static final String Tooltip_HolySeparator_05 = texter("Additional 10%% reduction in time per Coil Tier, multiplication calculus.","Tooltip_HolySeparator_05");

    // endregion

    // region SpaceScaler
    public static final String NameSpaceScaler = texter("Space Scaler","NameSpaceScaler");
    public static final String Tooltip_SpaceScaler_MachineType = texter("Compressor | Extractor | Particle Collider","Tooltip_SpaceScaler_MachineType");
    public static final String Tooltip_SpaceScaler_00 = texter("Controller block for the Space Scaler","Tooltip_SpaceScaler_00");
    public static final String Tooltip_SpaceScaler_01 = texter(EnumChatFormatting.AQUA+" First Look Space Technology.","Tooltip_SpaceScaler_01");
    public static final String Tooltip_SpaceScaler_02 = texter("Another method to operate matter.","Tooltip_SpaceScaler_02");
    public static final String Tooltip_SpaceScaler_03 = texter("Only if the space is manageable...","Tooltip_SpaceScaler_03");
    public static final String Tooltip_SpaceScaler_04 = texter("Has parallel equivalent to Perfect Overclock.","Tooltip_SpaceScaler_04");
    public static final String Tooltip_SpaceScaler_05 = texter("If use Ultimate Containment Field Generator, enable 10x speed multiplier.","Tooltip_SpaceScaler_05");
    public static final String Tooltip_SpaceScaler_06 = texter("Crude Stabilisation Field Generator block+ allowed machine Particle Collider Mode.","Tooltip_SpaceScaler_06");
    public static final String Tooltip_SpaceScaler_07 = texter("In Particle Collider mode, higher tier has more output.","Tooltip_SpaceScaler_07");

    // endregion

    // region MoleculeDeconstructor
    public static final String NameMoleculeDeconstructor = texter("Molecule Deconstructor","NameMoleculeDeconstructor");
    public static final String Tooltip_MoleculeDeconstructor_MachineType = texter("Electrolyzer | Centrifuge","Tooltip_MoleculeDeconstructor_MachineType");
    public static final String Tooltip_MoleculeDeconstructor_00 = texter("Controller block for the Molecule Deconstructor","Tooltip_MoleculeDeconstructor_00");
    public static final String Tooltip_MoleculeDeconstructor_01 = texter(EnumChatFormatting.AQUA+"The lightning seemed to roll down a ladder.","Tooltip_MoleculeDeconstructor_01");
    public static final String Tooltip_MoleculeDeconstructor_02 = texter("Separate the molecules one by one with tweezers.","Tooltip_MoleculeDeconstructor_02");
    public static final String Tooltip_MoleculeDeconstructor_03 = texter("Extra 24x Parallel per Piece. 16 Piece enable Perfect Overclock.","Tooltip_MoleculeDeconstructor_03");
    public static final String Tooltip_MoleculeDeconstructor_04 = texter("Additional 10%% reduction in time per Voltage Tier, multiplication calculus.","Tooltip_MoleculeDeconstructor_04");
    public static final String Tooltip_MoleculeDeconstructor_05 = texter("The Glass Tier limit the Energy hatch voltage Tier.","Tooltip_MoleculeDeconstructor_05");

    // endregion

    // region CrystallineInfinitier
    public static final String NameCrystallineInfinitier = texter("Crystalline Infinitier","NameCrystallineInfinitier");
    public static final String Tooltip_CrystallineInfinitier_MachineType = texter("Autoclave | Crystalline Infinitier","Tooltip_CrystallineInfinitier_MachineType");
    public static final String Tooltip_CrystallineInfinitier_00 = texter("Controller block for the Crystalline Infinitier","Tooltip_CrystallineInfinitier_00");
    public static final String Tooltip_CrystallineInfinitier_01 = texter(EnumChatFormatting.GREEN + "They're here. Grow and multiply without end.","Tooltip_CrystallineInfinitier_01");
    public static final String Tooltip_CrystallineInfinitier_02 = texter("With Gravitation Tech as a medium, we can control growth of crystalline cells more conveniently.","Tooltip_CrystallineInfinitier_02");
    public static final String Tooltip_CrystallineInfinitier_03 = texter("Higher glass tier, higher field generator tier, higher voltage tier means higher value of parallel.","Tooltip_CrystallineInfinitier_03");
    public static final String Tooltip_CrystallineInfinitier_04 = texter("And higher field generator tier means lower Energy cost.","Tooltip_CrystallineInfinitier_04");
    public static final String Tooltip_CrystallineInfinitier_05 = texter("Crude Stabilisation Field Generator enable Perfect Overclock.","Tooltip_CrystallineInfinitier_05");
    public static final String Tooltip_CrystallineInfinitier_06 = texter("Extra +300%% speed in Autoclave mode.","Tooltip_CrystallineInfinitier_06");

    // endregion

    // region DSPLauncher
    public static final String NameDSPLauncher = texter("Dyson Sphere Module Launch Site","NameDSPLauncher");
    public static final String Tooltip_DSPLauncher_MachineType = texter("Dyson Sphere Program: Launch Site","Tooltip_DSPLauncher_MachineType");
    public static final String Tooltip_DSPLauncher_00 = texter("Controller block for the Dyson Sphere Module Launch Site","Tooltip_DSPLauncher_00");
    public static final String Tooltip_DSPLauncher_01 = texter(EnumChatFormatting.BLUE+"\"Low altitude flight...\"","Tooltip_DSPLauncher_01");
    public static final String Tooltip_DSPLauncher_02 = texter("Launching Dyson Sphere components into Dyson Sphere orbit to form a Dyson Sphere.","Tooltip_DSPLauncher_02");
    public static final String Tooltip_DSPLauncher_03 = texter("No overclock and no extra parallel.","Tooltip_DSPLauncher_03");
    public static final String Tooltip_DSPLauncher_04 = texter("Higher tier of Elevator Module means faster launching.","Tooltip_DSPLauncher_04");
    public static final String Tooltip_DSPLauncher_05 = texter("Inputting Space Warper will enable overlord mode. Reduce launch intervals.","Tooltip_DSPLauncher_05");
    public static final String Tooltip_DSPLauncher_06 = texter("Joining the wireless EU network when without installing an energy hatch.","Tooltip_DSPLauncher_06");
    public static final String Tooltip_DSPLauncher_2_01 = texter("Final progress time = recipe time / ( module tier * 1 or 30 in overload mode )","Tooltip_DSPLauncher_2_01");
    public static final String Tooltip_DSPLauncher_2_02 = texter("Every Space Warper will provide (default) 15 minutes of overload mode.","Tooltip_DSPLauncher_2_02");
    public static final String Tooltip_DSPLauncher_2_03 = texter("Input Space Warper will be consumed immediately.","Tooltip_DSPLauncher_2_03");
    public static final String Tooltip_DSPLauncher_2_04 = texter("Converted to remaining time of overload mode.","Tooltip_DSPLauncher_2_04");

    // endregion

    // region DSPReceiver
    public static final String NameDSPReceiver = texter("Dyson Sphere Ray Receiving Station","NameDSPReceiver");
    public static final String Tooltip_DSPReceiver_MachineType = texter("Dyson Sphere Program: Ray Receiving Station","Tooltip_DSPReceiver_MachineType");
    public static final String Tooltip_DSPReceiver_00 = texter("Controller block for the Dyson Sphere Ray Receiving Station.","Tooltip_DSPReceiver_00");
    public static final String Tooltip_DSPReceiver_01 = texter(EnumChatFormatting.DARK_PURPLE+""+EnumChatFormatting.BOLD+"You hold in your hands the true power of Master Nebula ...","Tooltip_DSPReceiver_01");
    public static final String Tooltip_DSPReceiver_02 = texter("Receive high-energy rays transmitted back from the Dyson Cloud or the Dyson Sphere.","Tooltip_DSPReceiver_02");
    public static final String Tooltip_DSPReceiver_03 = texter("The received energy can be exported directly to the Wireless EU Net or Dynamo Hatches or stored as Critical Photons.","Tooltip_DSPReceiver_03");
    public static final String Tooltip_DSPReceiver_04 = texter("Ratio of the requesting from Dyson Sphere power point can be limited by putting Integrated Circuit into controller block.","Tooltip_DSPReceiver_04");
    public static final String Tooltip_DSPReceiver_05 = texter("At the same time, the maximum requested power point is 1024A Max (default).","Tooltip_DSPReceiver_05");
    public static final String Tooltip_DSPReceiver_06 = texter("Actual output power is affected by stellar and planetary coefficients.","Tooltip_DSPReceiver_06");
    public static final String Tooltip_DSPReceiver_07 = texter("Inputting Gravitational Lens will enable intensify mode. Increase actual output power.","Tooltip_DSPReceiver_07");
    public static final String Tooltip_DSPReceiver_08 = texter("Joining the wireless EU network when without installing a dynamo hatch.","Tooltip_DSPReceiver_08");
    public static final String Tooltip_DSPReceiver_02_01 = texter("Actual Generating EU = used power point * stellar coefficient * planet coefficient * 1 or 2 in intensify mode ","Tooltip_DSPReceiver_02_01");
    public static final String Tooltip_DSPReceiver_02_02 = texter("Personal Dimension was been classified as Overworld(Earth).","Tooltip_DSPReceiver_02_02");
    public static final String Tooltip_DSPReceiver_02_03 = texter("Every Gravitational Lens will provide (default) 10 minutes of intensify mode.","Tooltip_DSPReceiver_02_03");
    public static final String Tooltip_DSPReceiver_02_04 = texter("Input Gravitational Lens will be consumed immediately.","Tooltip_DSPReceiver_02_04");
    public static final String Tooltip_DSPReceiver_02_05 = texter("Converted to remaining time of intensify mode.","Tooltip_DSPReceiver_02_05");
    public static final String Tooltip_DSPReceiver_02_06 = texter("Requesting ratio = Integrated Circuit Number / Stack Size","Tooltip_DSPReceiver_02_06");

    // endregion

    // region ArtificialStar
    public static final String NameArtificialStar = texter("Artificial Star","NameArtificialStar");
    public static final String Tooltip_ArtificialStar_MachineType = texter("Dyson Sphere Program: Annihilation Generator","Tooltip_ArtificialStar_MachineType");
    public static final String Tooltip_ArtificialStar_Controller = texter("Controller block for the Artificial Star","Tooltip_ArtificialStar_Controller");
    public static final String Tooltip_ArtificialStar_00 = texter(EnumChatFormatting.LIGHT_PURPLE+""+EnumChatFormatting.BOLD+"All you need to do is to let the proton and antiproton beams","Tooltip_ArtificialStar_00");
    public static final String Tooltip_ArtificialStar_01 = texter(EnumChatFormatting.LIGHT_PURPLE+""+EnumChatFormatting.BOLD+" pass silently from both ends into the annihilation constrainer. Easy peasy!","Tooltip_ArtificialStar_01");
    public static final String Tooltip_ArtificialStar_02 = texter("It owes its simple shape to the elegance of the theory.","Tooltip_ArtificialStar_02");
    public static final String Tooltip_ArtificialStar_03 = texter("Actual output power is affected by 3 types tiered block.","Tooltip_ArtificialStar_03");
    public static final String Tooltip_ArtificialStar_04 = texter("At the same time, higher tier increase the probability of recovering materials.","Tooltip_ArtificialStar_04");
    public static final String Tooltip_ArtificialStar_05 = texter("Continuous operation improves power generation.","Tooltip_ArtificialStar_05");
    public static final String Tooltip_ArtificialStar_06 = texter("Only and must install 1 input bus.","Tooltip_ArtificialStar_06");
    public static final String Tooltip_ArtificialStar_07 = texter("Energy will output to Wireless EU Net directly.","Tooltip_ArtificialStar_07");
    public static final String Tooltip_ArtificialStar_08 = texter("Use screwdriver to enable/disable animations.","Tooltip_ArtificialStar_08");
    public static final String Tooltip_ArtificialStar_02_01 = texter("Output multiplier = tTime^0.25 * tDim^0.25 * 1.588186^(tStabilisation - 2)","Tooltip_ArtificialStar_02_01");
    public static final String Tooltip_ArtificialStar_02_02 = texter("Actual Generating EU = recipe value * output multiplier * Rewards for continuous operation","Tooltip_ArtificialStar_02_02");
    public static final String Tooltip_ArtificialStar_02_03 = texter("Recovering probability = tTime * tDim * tStabilisation / 1000","Tooltip_ArtificialStar_02_03");
    public static final String Tooltip_ArtificialStar_02_04 = texter("Input fuels will be consumed at once, process 6.4s (default), and output the corresponding EU.","Tooltip_ArtificialStar_02_04");
    public static final String Tooltip_ArtificialStar_02_05 = texter("Rewards multiplier 1%% increase per run when continuous operation.","Tooltip_ArtificialStar_02_05");
    public static final String Tooltip_ArtificialStar_02_06 = texter("Maximum is 150%% , Minimum is 100%% when uncontinuous.","Tooltip_ArtificialStar_02_06");

    // endregion

    // region Dyson Sphere Program Information
    public static final String Tooltip_DSPInfo_launch_01 = texter("Launching Solar Sail increase Solar Sail amount of current Galaxy's Dyson Sphere.","Tooltip_DSPInfo_launch_01");
    public static final String Tooltip_DSPInfo_launch_02 = texter("Launching Small Launch Vehicle increase Node amount of current Galaxy's Dyson Sphere.","Tooltip_DSPInfo_launch_02");
    public static final String Tooltip_DSPInfo_00 = texter("DSP Power Point = Solar Sail Power Point (default 524288) * Solar Sail amount * (Node + 1)^0.5","Tooltip_DSPInfo_00");
    public static final String Tooltip_DSPInfo_01 = texter("Every Node can absorb (default) 256 Solar Sails.","Tooltip_DSPInfo_01");
    public static final String Tooltip_DSPInfo_02 = texter("If unabsorbed solar sails amount is larger than 2048,","Tooltip_DSPInfo_02");
    public static final String Tooltip_DSPInfo_03 = texter(" the excess may be destroyed.","Tooltip_DSPInfo_03");
    public static final String Tooltip_DSPInfo_04 = texter("Every 30 minutes has 1/5000 chance to decrease Solar Sail amount.","Tooltip_DSPInfo_04");
    public static final String Tooltip_DSPInfo_05 = texter("In decreasing, every Dyson Sphere has 1/4 chance to destroyed Solar Sail amount.","Tooltip_DSPInfo_05");
    public static final String Tooltip_DSPInfo_06 = texter("The amount of destroyed is the excess' 1/2 .","Tooltip_DSPInfo_06");

    // endregion

    // region MiracleDoor
    public static final String NameMiracleDoor = texter("Miracle Door", "NameMiracleDoor");
    public static final String Tooltip_MiracleDoor_MachineType = texter("Stellar Forge | Fluid Alloy Cooker", "Tooltip_MiracleDoor_MachineType");
    public static final String Tooltip_MiracleDoor_Controller = texter("Controller block for the Miracle Door", "Tooltip_MiracleDoor_Controller");
    public static final String Tooltip_MiracleDoor_00 = texter(EnumChatFormatting.GOLD+""+EnumChatFormatting.BOLD+"Mere mortals can't even begin to understand the progress we've made.", "Tooltip_MiracleDoor_00");
    public static final String Tooltip_MiracleDoor_01 = texter("Enslaving Stellaris to work for us.", "Tooltip_MiracleDoor_01");
    public static final String Tooltip_MiracleDoor_02 = texter("No matter how large the workload, it can be done in one time.", "Tooltip_MiracleDoor_02");
    public static final String Tooltip_MiracleDoor_03 = texter("No matter how large the workload, it need one Critical Photon to start.", "Tooltip_MiracleDoor_03");
    public static final String Tooltip_MiracleDoor_04 = texter("Power consumption: Fluid Alloy Cooker §c1600%% | Stellar Forge §c6400%%", "Tooltip_MiracleDoor_04");
    public static final String Tooltip_MiracleDoor_05 = texter("Directly get EU from the Wireless EU Net.", "Tooltip_MiracleDoor_05");
    public static final String Tooltip_MiracleDoor_06 = texter("Warning! If trying to start machine when Wireless EU Net has not enough EU,", "Tooltip_MiracleDoor_06");
    public static final String Tooltip_MiracleDoor_07 = texter(" the materials will fade.", "Tooltip_MiracleDoor_07");
    public static final String Tooltip_MiracleDoor_08 = texter("Put Integrated Circuit into Controller block to decrease process time interval.", "Tooltip_MiracleDoor_08");
    public static final String Tooltip_MiracleDoor_2_01 = texter("Each run takes the same amount of time, (ABS) 25.6s | (EBF) 128s default.", "Tooltip_MiracleDoor_2_01");
    public static final String Tooltip_MiracleDoor_2_02 = texter("If putting Integrated Circuit into Controller block slot,", "Tooltip_MiracleDoor_2_02");
    public static final String Tooltip_MiracleDoor_2_03 = texter(" actual progress time = default / (Integrated Circuit Number * Stack Size)", "Tooltip_MiracleDoor_2_03");
    public static final String Tooltip_MiracleDoor_2_04 = texter("Actual cost EU = recipe value * 16 * (Integrated Circuit Number * Stack Size)", "Tooltip_MiracleDoor_2_04");
    public static final String Tooltip_MiracleDoor_2_05 = texter("ABS mode each run cost 1 Critical Photon to start.", "Tooltip_MiracleDoor_2_05");

    // endregion

    // region OreProcessingFactory
    public static final String NameOreProcessingFactory = texter("General Ore Processing Factory TST","NameOreProcessingFactory");
    public static final String Tooltip_OreProcessingFactory_MachineType = texter("Ore Processor","Tooltip_OreProcessingFactory_MachineType");
    public static final String Tooltip_OreProcessingFactory_Controller = texter("Controller block for the General Ore Processing Factory TST","Tooltip_OreProcessingFactory_Controller");
    public static final String Tooltip_OreProcessingFactory_01 = texter(EnumChatFormatting.WHITE+"Engineering is the art of directing the great sources of power in nature for the use and convenience of man.","Tooltip_OreProcessingFactory_01");
    public static final String Tooltip_OreProcessingFactory_02 = texter("The ores will line up and go in through the entrance and out through the exit.","Tooltip_OreProcessingFactory_02");
    public static final String Tooltip_OreProcessingFactory_03 = texter("This machine will not do overclock. Progress time is always 6.4s (default) .","Tooltip_OreProcessingFactory_03");
    public static final String Tooltip_OreProcessingFactory_04 = texter("It will process as many inputs as possible at once, if power allow.","Tooltip_OreProcessingFactory_04");
    public static final String Tooltip_OreProcessingFactory_05 = texter("Consume 3200L Lubricant every 12.8s (default).","Tooltip_OreProcessingFactory_05");
    public static final String Tooltip_OreProcessingFactory_06 = texter("Non-ore inputs will be transferred to the output bus.","Tooltip_OreProcessingFactory_06");

    // endregion

    // region CircuitConverter
    public static final String NameCircuitConverter = texter("General Circuit Converter","NameCircuitConverter");
    public static final String Tooltip_CircuitConverter_MachineType = texter("Circuit Converter","Tooltip_CircuitConverter_MachineType");
    public static final String Tooltip_CircuitConverter_Controller = texter("Controller block for the General Circuit Converter","Tooltip_CircuitConverter_Controller");
    public static final String Tooltip_CircuitConverter_01 = texter("Transform input circuits to Any Circuit.","Tooltip_CircuitConverter_01");
    public static final String Tooltip_CircuitConverter_2_01 = texter("Maximum 8 In/Output Buses.","Tooltip_CircuitConverter_2_01");
    // endregion

    // region LargeIndustrialCokingFactory
    public static final String NameLargeIndustrialCokingFactory = texter("Large Industrial Coking Factory","NameLargeIndustrialCokingFactory");
    public static final String Tooltip_LargeIndustrialCokingFactory_MachineType = texter("Coke Oven","Tooltip_LargeIndustrialCokingFactory_MachineType");
    public static final String Tooltip_LargeIndustrialCokingFactory_Controller = texter("Controller block for the Large Industrial Coking Factory","Tooltip_LargeIndustrialCokingFactory_Controller");
    public static final String Tooltip_LargeIndustrialCokingFactory_01 = texter(""+EnumChatFormatting.DARK_AQUA+EnumChatFormatting.BOLD+"Seizing like a ravenous beast, lavishing like a breezing east.", "Tooltip_LargeIndustrialCokingFactory_01");
    public static final String Tooltip_LargeIndustrialCokingFactory_02 = texter("Process endless inputs in one time.","Tooltip_LargeIndustrialCokingFactory_02");
    public static final String Tooltip_LargeIndustrialCokingFactory_03 = texter("Higher tier of coil make machine more faster.","Tooltip_LargeIndustrialCokingFactory_03");
    // endregion

    // region Elvenworkshop
    public static final String NameElvenWorkshop = texter("ElvenWorkshop","NameElvenWorkshop");
    public static final String Tooltip_ElvenWorkshop_MachineType = texter("Mana Infuser/Rune Engraver","Tooltip_ElvenWorkshop_MachineType");
    // endregion

    // region HyperSpacetimeTransformer
    public static final String NameHyperSpacetimeTransformer = texter("HyperSpacetimeTransformer","NameHyperSpacetimeTransformer");
    public static final String Tooltip_HyperSpacetimeTransformer_MachineType = texter("HyperSpacetimeTransformer","Tooltip_HyperSpacetimeTransformer_MachineType");
    public static final String Tooltip_HyperSpacetimeTransformer_00= texter("To change the material itself in a higher dimension.","Tooltip_HyperSpacetimeTransformer_00");
    public static final String Tooltip_HyperSpacetimeTransformer_01= texter("Molecular Transformer Mode","Tooltip_HyperSpacetimeTransformer_01");
    public static final String Tooltip_HyperSpacetimeTransformer_02= texter("Parallel:Product of three types of field generators.","Tooltip_HyperSpacetimeTransformer_02");
    public static final String Tooltip_HyperSpacetimeTransformer_03= texter("Tier 9 field generator will decrease energy use by 25%, max 75%.","Tooltip_HyperSpacetimeTransformer_03");
    public static final String Tooltip_HyperSpacetimeTransformer_04= texter("","Tooltip_HyperSpacetimeTransformer_04");
    public static final String Tooltip_HyperSpacetimeTransformer_05= texter("","Tooltip_HyperSpacetimeTransformer_05");
    public static final String Tooltip_HyperSpacetimeTransformer_06= texter("","Tooltip_HyperSpacetimeTransformer_06");

    // region Scavenger
    public static final String NameScavenger = texter("Scavenger","NameScavenger");
    public static final String Tooltip_Scavenger_MachineType = texter("Sifter","Tooltip_Scavenger_MachineType");
    public static final String Tooltip_Scavenger_Controller = texter("Controller block for the Scavenger","Tooltip_Scavenger_Controller");
    public static final String Tooltip_Scavenger_01 = texter(EnumChatFormatting.BOLD+"I like pigs. Dogs look up to us. Cats look down on us. Pigs treat us as equals.","Tooltip_Scavenger_01");
    public static final String Tooltip_Scavenger_02 = texter("Has parallel equivalent to Perfect Overclock.","Tooltip_Scavenger_02");
    public static final String Tooltip_Scavenger_03 = texter("Only uses 60%% of the EU/t normally required.","Tooltip_Scavenger_03");
    public static final String Tooltip_Scavenger_04 = texter("Additional 20%% reduction in time per Voltage Tier, multiplication calculus.","Tooltip_Scavenger_04");
    // endregion

    // region AdvancedMegaOilCracker
    public static final String NameAdvancedMegaOilCracker = texter("Advanced Mega Oil Cracker","NameAdvancedMegaOilCracker");
    public static final String Tooltips_AdvancedMegaOilCracker_MachineType = texter("Cracker","Tooltips_AdvancedMegaOilCracker_MachineType");
    public static final String Tooltips_AdvancedMegaOilCracker_Controller = texter("Controller block for the Advanced Mega Oil Cracker","Tooltips_AdvancedMegaOilCracker_Controller");
    public static final String Tooltips_AdvancedMegaOilCracker_01 = texter(EnumChatFormatting.ITALIC+"Freedom as a basis for self-government.","Tooltips_AdvancedMegaOilCracker_01");
    public static final String Tooltips_AdvancedMegaOilCracker_02 = texter("As the Mega Oil Cracker do.","Tooltips_AdvancedMegaOilCracker_02");
    // endregion

    // region InfiniteAirHatch
    public static final String NameInfiniteAirHatch = texter("Infinite Air Hatch", "NameInfiniteAirHatch");
    public static final String NameManaHatch = texter("Mana Hatch", "NameManaHatch");
    public static final String NameInfiniteWirelessDynamoHatch = texter("Infinite Wireless Dynamo Hatch", "NameInfiniteWirelessDynamoHatch");
    public static final String NameDualInputBuffer_IV = texter("Dual Input Buffer (IV)", "NameDualInputBuffer_IV");
    public static final String NameDualInputBuffer_LuV = texter("Dual Input Buffer (LuV)", "NameDualInputBuffer_LuV");
    public static final String NameDualInputBuffer_ZPM = texter("Dual Input Buffer (ZPM)", "NameDualInputBuffer_ZPM");
    public static final String NameDualInputBuffer_UV = texter("Dual Input Buffer (UV)",   "NameDualInputBuffer_UV");

    // endregion

    // region megaUniverseSpaceStation
    public static final String NameMegaUniversalSpaceStation = texter("Mega Universal Space Station","NameMegaUniversalSpaceStation");
    public static final String Tooltip_MegaUniversalSpaceStation_MachineType = texter("space station","Tooltip_MegaUniversalSpaceStation_MachineType");
    public static final String Tooltip_MegaUniversalSpaceStation_00 = texter("Use auto build item to build instead of build your self","Tooltip_MegaUniversalSpaceStation_00");
    public static final String Tooltip_MegaUniversalSpaceStation_01 = texter("Auto-SpaceStation build core","Tooltip_MegaUniversalSpaceStation_01");
    public static final String Tooltip_MegaUniversalSpaceStation_02 = texter("If your station broke, you can put fix block inside the input hatch to fix it","Tooltip_MegaUniversalSpaceStation_02");
    public static final String Tooltip_MegaUniversalSpaceStation_03 = texter("temp","Tooltip_MegaUniversalSpaceStation_03");
    public static final String Tooltip_MegaUniversalSpaceStation_04 = texter("temp","Tooltip_MegaUniversalSpaceStation_04");
    public static final String Tooltip_MegaUniversalSpaceStation_05 = texter("temp","Tooltip_MegaUniversalSpaceStation_05");
    public static final String Tooltip_MegaUniversalSpaceStation_06 = texter("temp","Tooltip_MegaUniversalSpaceStation_06");

    public static final String NameStellarMaterialSiphon = texter("Stellar Material Siphon","NameStellarMaterialSiphon");
    public static final String Tooltip_NameStellarMaterialSiphon = texter("Siphon","Tooltip_MegaUniversalSpaceStation_MachineType");

    //endregion

    //region MegaPrimitiveBlastFurnace
    public static final String NameMegaBrickedBlastFurnace = texter("Mega Bricked Blast Furnace","NameMegaBrickedBlastFurnace");
    public static final String Tooltip_MegaBrickedBlastFurnace_MachineType = texter("Blast Furnace","Tooltip_MegaBrickedBlastFurnace_MachineType");
    public static final String Tooltip_MegaBrickedBlastFurnace_Controller = texter("Controller block for the Mega Bricked Blast Furnace","Tooltip_MegaBrickedBlastFurnace_Controller");
    public static final String Tooltip_MegaBrickedBlastFurnace_00 = texter(EnumChatFormatting.WHITE+"Who could ever imagine the power of the Steam Age?","Tooltip_MegaBrickedBlastFurnace_00");
    public static final String Tooltip_MegaBrickedBlastFurnace_01 = texter("consume iron/wrought iron ingots and coke coals (blocks) to produce steel (and ash byproduct)","Tooltip_MegaBrickedBlastFurnace_01");
    public static final String Tooltip_MegaBrickedBlastFurnace_02 = texter("Default recipe time is 240s. More wrought iron and coal input will reduce process time.","Tooltip_MegaBrickedBlastFurnace_02");
    public static final String Tooltip_MegaBrickedBlastFurnace_03 = texter("actual progress time = default x parallels /((1 + 4 x Ratio of wrought iron input) x sqrt(Coke coal input))","Tooltip_MegaBrickedBlastFurnace_03");
    public static final String Tooltip_MegaBrickedBlastFurnace_04 = texter("process 50% of (wrought) iron input and consume all coke coal input at once.","Tooltip_MegaBrickedBlastFurnace_04");
    public static final String Tooltip_MegaBrickedBlastFurnace_05 = texter("minimum coke coal requirement:2 x (wrought) iron processed","Tooltip_MegaBrickedBlastFurnace_05");
    public static final String Tooltip_MegaBrickedBlastFurnace_06 = texter("Takes "+EnumChatFormatting.RED + "8" +EnumChatFormatting.GRAY+" hours of continuous run time to achieve maximum efficiency.","Tooltip_MegaBrickedBlastFurnace_06");
    public static final String Tooltip_MegaBrickedBlastFurnace_07 = texter("This improve coal efficiency by up to "+EnumChatFormatting.RED+"800%"+EnumChatFormatting.GRAY+". Reduce minimum coal requirement and calculate in actual progress time.","Tooltip_MegaBrickedBlastFurnace_07");
    public static final String Tooltip_MegaBrickedBlastFurnace_08 = texter(EnumChatFormatting.YELLOW+"It is recommended not to force yourself to build it until you have enough resources.","Tooltip_MegaBrickedBlastFurnace_08");
    public static final String textMegaBrickedBlastFurnaceTips = texter(EnumChatFormatting.YELLOW+"Dirt must be Horizontal dirt in Chisel Mod!","textMegaBrickedBlastFurnaceTips");
    public static final String textMegaBrickedBlastFurnaceLocation = texter("any Bronze Plated Bricks, 0-6x","textMegaBrickedBlastFurnaceLocation");

    //region BiosphereIII
    public static final String NameBiosphereIII = texter("Biosphere III","NameBiosphereIII");
    public static final String Tooltip_BiosphereIII_MachineType = texter("Bacterial Vat | Brewing Machine | Fermenter","Tooltip_BiosphereIII_MachineType");
    public static final String Tooltip_BiosphereIII_Controller = texter("Controller block for Biosphere III","Tooltip_BiosphereIII_Controller");
    public static final String Tooltip_BiosphereIII_00 = texter(""+EnumChatFormatting.AQUA+EnumChatFormatting.BOLD+"Control the thoughts of those microorganisms...","Tooltip_BiosphereIII_00");
    public static final String BiosphereIII_Mode_00 = texter(EnumChatFormatting.GOLD+"----- Bacterial Vat Mode -----","BiosphereIII_Mode_00");
    public static final String Tooltip_BiosphereIII_Mode0_00 = texter("Need Petri Dish in controller slot","Tooltip_BiosphereIII_Mode0_00");
    public static final String Tooltip_BiosphereIII_Mode0_01 = texter("Every Petri Dish provides 4 parallel","Tooltip_BiosphereIII_Mode0_01");
    public static final String Tooltip_BiosphereIII_Mode0_02 = texter("Keep the Output Hatch always half filled for maximum efficiency","Tooltip_BiosphereIII_Mode0_02");
    public static final String BiosphereIII_Mode_01 = texter(EnumChatFormatting.GOLD+"----- Bacterial Vat Automation Mode -----","BiosphereIII_Mode_01");
    public static final String Tooltip_BiosphereIII_Mode1_00 = texter("Need Petri Dish in controller slot","Tooltip_BiosphereIII_Mode1_00");
    public static final String Tooltip_BiosphereIII_Mode1_01 = texter("Every Petri Dish provides 1 parallel","Tooltip_BiosphereIII_Mode1_01");
    public static final String Tooltip_BiosphereIII_Mode1_02 = texter("Advanced artificial intelligence controls the breeding of bacteria","Tooltip_BiosphereIII_Mode1_02");
    public static final String Tooltip_BiosphereIII_Mode1_03 = texter("Original efficiency of control is 40% of the maximum. Each glass tier over recipe requirement improve the efficiency by 15%.","Tooltip_BiosphereIII_Mode1_03");
    public static final String BiosphereIII_Mode_02 = texter(EnumChatFormatting.GOLD+"----- Brewing Machine Mode -----","BiosphereIII_Mode_02");
    public static final String BiosphereIII_Mode_03 = texter(EnumChatFormatting.GOLD+"----- Fermenter Mode -----","BiosphereIII_Mode_03");
    public static final String Tooltip_BiosphereIII_Mode2n3_00 = texter("Don't need Petri Dish","Tooltip_BiosphereIII_Mode2n3_00");
    public static final String Tooltip_BiosphereIII_Mode2n3_01 = texter("Each glass tier over HV provides 4 times parallel","Tooltip_BiosphereIII_Mode2n3_01");
    public static final String Tooltip_BiosphereIII_Mode2n3_02 = texter("EV glass provides 4 parallel, IV glass provides 16 parallel, etc.","Tooltip_BiosphereIII_Mode2n3_02");
    public static final String textBiosphereIIIRadioHatch = texter("Radiation Hatch: 0-1x","textBiosphereIIIRadioHatch");
    public static final String textBiosphereIIIHatchLocation = texter("Any Bottom Clean Stainless Steel Machine Casing","textBiosphereIIIHatchLocation");
    public static final String BiosphereIIIEfficiency = texter("Efficiency: ","BiosphereIIIEfficiency");
    //endregion

    // region Egg Generator
    public static final String NameMegaEggGenerator = texter("Tower of Abstraction", "NameMegaEggGenerator");
    public static final String Tooltip_MegaEggGenerator_MachineType = texter("Magical Energy Absorber", "Tooltip_MegaEggGenerator_MachineType");
    public static final String Tooltip_MegaEggGenerator_Controller = texter("Controller block for the Tower of Abstraction", "Tooltip_MegaEggGenerator_Controller");
    public static final String Tooltip_MegaEggGenerator_00 = texter("This is where the "+EnumChatFormatting.RED+"ulti"+EnumChatFormatting.AQUA+"mate"+ EnumChatFormatting.GOLD +" des"+EnumChatFormatting.BLUE+"tiny"+EnumChatFormatting.GRAY+" of the Dragon's Children lies.", "Tooltip_MegaEggGenerator_00");
    public static final String Tooltip_MegaEggGenerator_01 = texter("With the help of the Magic Egg, it draws in endless magical power as effortlessly as a soul-sucking sorcerer.", "Tooltip_MegaEggGenerator_01");
    public static final String Tooltip_MegaEggGenerator_02 = texter("Every dragon egg generates 1A EV & every creeper's generates 1A HV.", "Tooltip_MegaEggGenerator_02");
    public static final String Tooltip_MegaEggGenerator_03 = texter("Infinity egg generates 2A IV, but you can put only one for one each piece you add.", "Tooltip_MegaEggGenerator_03");
    public static final String Tooltip_MegaEggGenerator_04 = texter("But quantitative change leads to qualitative change,", "Tooltip_MegaEggGenerator_04");
    public static final String Tooltip_MegaEggGenerator_05 = texter("Every 2^n pieces give 2% max efficiency bonus, and every infinity egg gives 1%.", "Tooltip_MegaEggGenerator_05");
    public static final String Tooltip_MegaEggGenerator_06 = texter("Note its warm-up process is quite slow, about 500 secs in need.", "Tooltip_MegaEggGenerator_06");
    public static final String Tooltip_MegaEggGenerator_07 = texter("You can also put nothing on the egg pos, but every empty pos decreases 5% max efficiency.", "Tooltip_MegaEggGenerator_07");
    public static final String Tooltip_MegaEggGenerator_08 = texter("Whether it is the"+EnumChatFormatting.DARK_PURPLE+" Dragon Egg,"+EnumChatFormatting.DARK_GREEN+" Creeper Egg, or the "+EnumChatFormatting.GOLD+"Egg of Infinity, "+EnumChatFormatting.GRAY+"only their presence can drive the full circulation of power.", "Tooltip_MegaEggGenerator_08");
    public static final String Tooltip_MegaEggGenerator_D = texter("Dynamo or TT Dynamo, one only", "Tooltip_MegaEggGenerator_D");
    public static final String Tooltip_MegaEggGenerator_M = texter("No need for maintenance hatch.", "Tooltip_MegaEggGenerator_M");
    public static final String Tooltip_MegaEggGenerator_L = texter("Lasers unlock at >=16 pieces.", "Tooltip_MegaEggGenerator_L");
    public static final String Tooltip_MegaEggGenerator_C = texter("Any Magical Casing.", "Tooltip_MegaEggGenerator_C");
    // endregion

    // region IndistinctTentacle
    public static final String NameIndistinctTentacle = texter(""+EnumChatFormatting.BOLD+EnumChatFormatting.BLACK+"Indistinct Tentacle","NameIndistinctTentacle");
    public static final String Tooltip_IndistinctTentacle_MachineType = texter("Assembly Line | Component Assembly Line | Assembler | Precise Assembler","Tooltip_IndistinctTentacle_MachineType");
    public static final String Tooltip_IndistinctTentacle_Controller = texter("Controller block for the Indistinct Tentacle","Tooltip_IndistinctTentacle_Controller");
    public static final String Tooltip_IndistinctTentacle_01 = texter(""+EnumChatFormatting.BOLD+EnumChatFormatting.ITALIC+"In the midst of this sea and endless solitude there appears a dim road, a road without human footprints.","Tooltip_IndistinctTentacle_01");
    public static final String Tooltip_IndistinctTentacle_02 = texter(""+EnumChatFormatting.BOLD+EnumChatFormatting.ITALIC+"No man has ever passed this place; no ship has ever sailed here.","Tooltip_IndistinctTentacle_02");
    public static final String Tooltip_IndistinctTentacle_03 = texter("Made everything in its where should be.","Tooltip_IndistinctTentacle_03");
    public static final String Tooltip_IndistinctTentacle_04 = texter("Glass tier limit energy hatch, laser hatch need UV glass.","Tooltip_IndistinctTentacle_04");
    public static final String Tooltip_IndistinctTentacle_05 = texter("Component Casing tier limit recipe can process. Casing tier require at least the recipe voltage level -1 .","Tooltip_IndistinctTentacle_05");
    public static final String Tooltip_IndistinctTentacle_06 = texter("If Component Casing tier is higher than recipe voltage, enable §cPerfect Overclock§7.","Tooltip_IndistinctTentacle_06");
    public static final String Tooltip_IndistinctTentacle_07 = texter("UMV+ glass and Component Casing allow Wireless mode by placing no energy hatch.","Tooltip_IndistinctTentacle_07");
    public static final String Tooltip_IndistinctTentacle_08 = texter("Progressing time is fixed in Wireless mode.","Tooltip_IndistinctTentacle_08");
    public static final String Tooltip_IndistinctTentacle_09 = texter("Watch out your Global energy storage if use wireless mode, you should not want to see the power drain's landscape.","Tooltip_IndistinctTentacle_09");
    public static final String Tooltip_IndistinctTentacle_2_01 = texter("Speed (default) of mode:","Tooltip_IndistinctTentacle_2_01");
    public static final String Tooltip_IndistinctTentacle_2_02 = texter("    Assembly Line = 100%%","Tooltip_IndistinctTentacle_2_02");
    public static final String Tooltip_IndistinctTentacle_2_03 = texter("    Component Assembly Line = 200%%;","Tooltip_IndistinctTentacle_2_03");
    public static final String Tooltip_IndistinctTentacle_2_04 = texter("    Assembler = 400%%","Tooltip_IndistinctTentacle_2_04");
    public static final String Tooltip_IndistinctTentacle_2_05 = texter("    Precise Assembler = 400%%","Tooltip_IndistinctTentacle_2_05");
    public static final String Tooltip_IndistinctTentacle_2_06 = texter("Default power mode parallel 256.","Tooltip_IndistinctTentacle_2_06");
    public static final String Tooltip_IndistinctTentacle_2_07 = texter("Progressing time is fixed at 25.6s in Wireless mode.","Tooltip_IndistinctTentacle_2_07");
    public static final String Tooltip_IndistinctTentacle_2_08 = texter("Put an Astral Array Fabricator into controller slot,","Tooltip_IndistinctTentacle_2_08");
    public static final String Tooltip_IndistinctTentacle_2_09 = texter("  the Progressing time will be fixed at 1.0s, and EU cost increase to 64 times.","Tooltip_IndistinctTentacle_2_09");

    // endregion

    // region ThermalEnergyDevourer
    public static final String NameThermalEnergyDevourer = texter("Thermal Energy Devourer","NameThermalEnergyDevourer");
    public static final String Tooltip_ThermalEnergyDevourer_MachineType = texter("Vacuum Freezer","Tooltip_ThermalEnergyDevourer_MachineType");
    public static final String Tooltip_ThermalEnergyDevourer_Controller = texter("Controller block for the Thermal Energy Devourer","Tooltip_ThermalEnergyDevourer_Controller");
    public static final String Tooltip_ThermalEnergyDevourer_01 = texter(""+EnumChatFormatting.WHITE+EnumChatFormatting.BOLD+EnumChatFormatting.ITALIC+" It's White Album season again.","Tooltip_ThermalEnergyDevourer_01");
    public static final String Tooltip_ThermalEnergyDevourer_02 = texter("The thermal energy it greedily devours.","Tooltip_ThermalEnergyDevourer_02");
    public static final String Tooltip_ThermalEnergyDevourer_03 = texter("But overall it's still a Freezer, and works well.","Tooltip_ThermalEnergyDevourer_03");
    public static final String Tooltip_ThermalEnergyDevourer_04 = texter("Increasing the input power will also increase the operating efficiency.","Tooltip_ThermalEnergyDevourer_04");
    public static final String Tooltip_ThermalEnergyDevourer_05 = texter(EnumChatFormatting.GOLD+" ----- High Speed Mode -----","Tooltip_ThermalEnergyDevourer_05");
    public static final String Tooltip_ThermalEnergyDevourer_06 = texter("Operating efficiency decrease the progress time directly.","Tooltip_ThermalEnergyDevourer_06");
    public static final String Tooltip_ThermalEnergyDevourer_07 = texter("1024x parallel.","Tooltip_ThermalEnergyDevourer_07");
    public static final String Tooltip_ThermalEnergyDevourer_08 = texter(EnumChatFormatting.GOLD+" ----- High Parallel Mode -----","Tooltip_ThermalEnergyDevourer_08");
    public static final String Tooltip_ThermalEnergyDevourer_09 = texter("Operating efficiency decrease the EU cost.","Tooltip_ThermalEnergyDevourer_09");
    public static final String Tooltip_ThermalEnergyDevourer_10 = texter("Almost infinite parallel.","Tooltip_ThermalEnergyDevourer_10");
    public static final String Tooltip_ThermalEnergyDevourer_11 = texter(EnumChatFormatting.GOLD+" ----- Wireless Mode -----","Tooltip_ThermalEnergyDevourer_11");
    public static final String Tooltip_ThermalEnergyDevourer_12 = texter("Put "+EnumChatFormatting.AQUA+"Energised Tesseract"+EnumChatFormatting.GRAY+" into controller slot to turn into Wireless mode.","Tooltip_ThermalEnergyDevourer_12");
    public static final String Tooltip_ThermalEnergyDevourer_13 = texter("Directly consume required EU from wireless EU network.","Tooltip_ThermalEnergyDevourer_13");
    public static final String Tooltip_ThermalEnergyDevourer_14 = texter("Progress time is fixed at 6.4s .","Tooltip_ThermalEnergyDevourer_14");
    public static final String Tooltip_ThermalEnergyDevourer_2_01 = texter("Check whether turn into Wireless mode when checking structure.","Tooltip_ThermalEnergyDevourer_2_01");
    // endregion

    // region Debug Uncertainty Hatch
    public static final String NameDebugUncertaintyHatch = texter("Debug Uncertainty Hatch","NameDebugUncertaintyHatch");
    // endregion

    public static final String NameLaserSmartNode = texter("Laser Smart Node","NameLaserSmartPipe");

    // region Vacuum Filter Extractor
    public static final String NameVacuumFilterExtractor = texter("Vacuum Filter Extractor","NameVacuumFilterExtractor");
    public static final String Tooltip_VacuumFilterExtractor_MachineType = texter("Distillation Tower | Distillery","Tooltip_VacuumFilterExtractor_MachineType");
    public static final String Tooltip_VacuumFilterExtractor_Controller = texter("Controller block for the Vacuum Filter Extractor","Tooltip_VacuumFilterExtractor_Controller");
    public static final String Tooltip_VacuumFilterExtractor_01 = texter(EnumChatFormatting.ITALIC+"Engineers think something isn't broken because it has too few features.","Tooltip_VacuumFilterExtractor_01");
    public static final String Tooltip_VacuumFilterExtractor_02 = texter("By manipulating space in order to achieve separation of matter ","Tooltip_VacuumFilterExtractor_02");
    public static final String Tooltip_VacuumFilterExtractor_03 = texter("  rather than direct manipulation of matter.","Tooltip_VacuumFilterExtractor_03");
    public static final String Tooltip_VacuumFilterExtractor_04 = texter("Recipe voltage is only 50%% of normal.","Tooltip_VacuumFilterExtractor_04");
    public static final String Tooltip_VacuumFilterExtractor_05 = texter("Increasing the energy input will result in more speed boosts.","Tooltip_VacuumFilterExtractor_05");
    public static final String Tooltip_VacuumFilterExtractor_06 = texter("In distillery mode, machine will enable "+EnumChatFormatting.AQUA+"Perfect Overclock"+EnumChatFormatting.GRAY+".","Tooltip_VacuumFilterExtractor_06");
    // endregion

    // region Large Steam Forge Hammer
    public static final String NameLargeSteamForgeHammer = texter("Large Steam Forge Hammer", "NameLargeSteamForgeHammer");
    public static final String Tooltip_LargeSteamForgeHammer_MachineType = texter("Forge Hammer", "Tooltip_LargeSteamForgeHammer_MachineType");
    public static final String Tooltip_LargeSteamForgeHammer_Controller = texter("Controller block for the Large Steam Forge Hammer", "Tooltip_LargeSteamForgeHammer_Controller");
    public static final String Tooltip_LargeSteamForgeHammer_01 = texter("He has a hammer. Who has the Sickle?", "Tooltip_LargeSteamForgeHammer_01");
    // endregion

    // region Large Steam Alloy Smelter
    public static final String NameLargeSteamAlloySmelter = texter("Large Steam Alloy Smelter", "NameLargeSteamAlloySmelter");
    public static final String Tooltip_LargeSteamAlloySmelter_MachineType = texter("Alloy Smelter","Tooltip_LargeSteamAlloySmelter_MachineType");
    public static final String Tooltip_LargeSteamAlloySmelter_Controller = texter("Controller block for the Large Steam Alloy Smelter","Tooltip_LargeSteamAlloySmelter_Controller");
    public static final String Tooltip_LargeSteamAlloySmelter_01 = texter("Steam Tech Operational","Tooltip_LargeSteamAlloySmelter_01");
    // endregion

    // region Eye Of Wood
    public static final String NameEyeOfWood = texter("Eye of Wood","NameEyeOfWood");
    public static final String Tooltip_EyeOfWood_MachineType = texter("Happiness Master", "Tooltip_EyeOfWood_MachineType");
    public static final String Tooltip_EyeOfWood_Controller = texter("Controller block for the Eye of Wood", "Tooltip_EyeOfWood_Controller");
    public static final String Tooltip_EyeOfWood_01 = texter("You'd better to do a sanity check, if you are looking at this.", "Tooltip_EyeOfWood_01");
    // endregion

    // region Bee Engineer
    public static final String NameBeeEngineer = texter("Bee Engineer (Prototype)", "NameBeeEngineer");
    public static final String Tooltip_BeeEngineer_Type = texter("Bee Engineer", "Tooltip_BeeEngineer_Type");
    public static final String Tooltip_BeeEngineer_Controller = texter("Controller of the Bee Engineer", "Tooltip_BeeEngineer_Controller");
    public static final String Tooltip_BeeEngineer_01 = texter("Still in test.", "Tooltip_BeeEngineer_01");
    public static final String Tooltip_BeeEngineer_02 = texter("Transforming drones into princesses.", "Tooltip_BeeEngineer_02");
    public static final String Tooltip_BeeEngineer_03 = texter("Who knows how many drones became stepping stones for the Last Queen?", "Tooltip_BeeEngineer_03");
    public static final String Tooltip_BeeEngineer_04 = texter("Cost 128kL Honey to transform a drone into princess, but with 40% failing chance.", "Tooltip_BeeEngineer_04");
    public static final String Tooltip_BeeEngineer_05 = texter("Will try to consume 32kL UUM (if exists) to increase success rate to 80%.", "Tooltip_BeeEngineer_05");
    public static final String Tooltip_BeeEngineer_06 = texter("In case of failure, all consumed ingredients will not be returned.", "Tooltip_BeeEngineer_06");
    public static final String Tooltip_BeeEngineer_07 = texter("Don't put too many drones in at once, that will result in a long run time!", "Tooltip_BeeEngineer_07");
    // endregion

}
// spotless:on
