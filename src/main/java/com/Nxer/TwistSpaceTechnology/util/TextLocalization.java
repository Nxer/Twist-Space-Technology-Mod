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
    // endregion

    // region special hatch info
    public static final String Tooltip_DoNotNeedMaintenance = texter("Do Not Need Maintenance!","Tooltip_DoNotNeedMaintenance");

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
    public static final String Tooltip_MiracleTop_07 = texter("256x Parallel.", "Tooltip_MiracleTop_07");
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
    public static final String Tooltip_Silksong_03 = texter("8x Parallel per piece.","Tooltip_Silksong_03");
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

    // endregion

    // region MoleculeDeconstructor
    public static final String NameMoleculeDeconstructor = texter("Molecule Deconstructor","NameMoleculeDeconstructor");
    public static final String Tooltip_MoleculeDeconstructor_MachineType = texter("Electrolyzer | Centrifuge","Tooltip_MoleculeDeconstructor_MachineType");
    public static final String Tooltip_MoleculeDeconstructor_00 = texter("Controller block for the Molecule Deconstructor","Tooltip_MoleculeDeconstructor_00");
    public static final String Tooltip_MoleculeDeconstructor_01 = texter(EnumChatFormatting.AQUA+"The lightning seemed to roll down a ladder.","Tooltip_MoleculeDeconstructor_01");
    public static final String Tooltip_MoleculeDeconstructor_02 = texter("Separate the molecules one by one with tweezers.","Tooltip_MoleculeDeconstructor_02");
    public static final String Tooltip_MoleculeDeconstructor_03 = texter("Extra 24x Parallel per Piece. 16 Piece enable Perfect Overclock.","Tooltip_MoleculeDeconstructor_03");
    public static final String Tooltip_MoleculeDeconstructor_04 = texter("Additional 10%% reduction in time per Coil Tier, multiplication calculus.","Tooltip_MoleculeDeconstructor_04");
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
    public static final String Tooltip_DSPLauncher_2_01 = texter("Final progress time = recipe time / ( module tier * 1 or 60 in overload mode )","Tooltip_DSPLauncher_2_01");
    public static final String Tooltip_DSPLauncher_2_02 = texter("Every Space Warper will provide (default) 15 minutes of overload mode.","Tooltip_DSPLauncher_2_02");
    public static final String Tooltip_DSPLauncher_2_03 = texter("Input Space Warper will be consumed immediately.","Tooltip_DSPLauncher_2_03");
    public static final String Tooltip_DSPLauncher_2_04 = texter("Converted to remaining time of overload mode.","Tooltip_DSPLauncher_2_04");

    // endregion

    // region DSPReceiver
    public static final String NameDSPReceiver = texter("Dyson Sphere Ray Receiving Station","NameDSPReceiver");

    // endregion

    // region ArtificialStar
    public static final String NameArtificialStar = texter("Artificial Star","NameArtificialStar");


    // endregion


    // region InfiniteAirHatch
    public static final String NameInfiniteAirHatch = texter("Infinite Air Hatch", "NameInfiniteAirHatch");

    // endregion
}
// spotless:on
