package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.ArrayList;
import java.util.List;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

/** Explicit supported controllers and their complete pieces; no GUI-time machine discovery or construction. */
public enum BottledChaosSupportedMachine {

    // spotless:off
    MTEBIO_VAT("bartworks.common.tileentities.multis.MTEBioVat", "main"),
    GTCM_CRYSTALLINE_INFINITIER("com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier", "mainCrystallineInfinitier"),
    GT_TILE_ENTITY_INTENSIFY_CHEMICAL_DISTORTER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IntensifyChemicalDistorter", "main"),
    GT_TILE_ENTITY_MAGNETIC_DOMAIN_CONSTRUCTOR("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDomainConstructor", "mainMagneticDomainConstructor"),
    GT_TILE_ENTITY_MAGNETIC_DRIVE_PRESSURE_FORMER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDrivePressureFormer", "main"),
    GT_TILE_ENTITY_MAGNETIC_MIXER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticMixer", "main"),
    GT_TILE_ENTITY_MOLECULE_DECONSTRUCTOR("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MoleculeDeconstructor", "mainMoleculeDeconstructor"),
    GT_TILE_ENTITY_PHYSICAL_FORM_SWITCHER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PhysicalFormSwitcher", "main"),
    GT_TILE_ENTITY_PRECISE_HIGH_ENERGY_PHOTONIC_QUANTUM_MASTER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster", "main"),
    GT_TILE_ENTITY_SPACE_SCALER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_SpaceScaler", "mainSpaceScaler"),
    TST_ADVANCED_MEGA_OIL_CRACKER("com.Nxer.TwistSpaceTechnology.common.machine.TST_AdvancedMegaOilCracker", "mainAdvancedMegaOilCracker"),
    TST_BALL_LIGHTNING("com.Nxer.TwistSpaceTechnology.common.machine.TST_BallLightning", "BallLightningMK1"),
    TST_BIOSPHERE_III("com.Nxer.TwistSpaceTechnology.common.machine.TST_BiosphereIII", "mainBiosphereIII"),
    TST_CORE_DEVICE_OF_HUMAN_POWER_GENERATION_FACILITY("com.Nxer.TwistSpaceTechnology.common.machine.TST_CoreDeviceOfHumanPowerGenerationFacility", "mainCoreDeviceOfHumanPowerGenerationFacility"),
    TST_GIANT_VACUUM_DRYING_FURNACE("com.Nxer.TwistSpaceTechnology.common.machine.TST_GiantVacuumDryingFurnace", "VacuumPump"),
    TST_HEPHAESTUS_ATELIER("com.Nxer.TwistSpaceTechnology.common.machine.TST_HephaestusAtelier", "mainHephaestusAtelier"),
    TST_INDISTINCT_TENTACLE("com.Nxer.TwistSpaceTechnology.common.machine.TST_IndistinctTentacle", "mainIndistinctTentacle"),
    TST_INDUSTRIAL_MAGNETAR_SEPARATOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_IndustrialMagnetarSeparator", "STRUCTURE_PIECE_MAIN"),
    TST_MANUFACTURING_CENTER("com.Nxer.TwistSpaceTechnology.common.machine.TST_ManufacturingCenter", "main"),
    TST_MEGA_MACERATOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaMacerator", "mainMegaMacerator"),
    TST_SCAVENGER("com.Nxer.TwistSpaceTechnology.common.machine.TST_Scavenger", "mainScavenger"),
    TST_SWELEGFYR_BLAST_FURNACE("com.Nxer.TwistSpaceTechnology.common.machine.TST_SwelegfyrBlastFurnace", "$"),
    TST_THERMAL_ENERGY_DEVOURER("com.Nxer.TwistSpaceTechnology.common.machine.TST_ThermalEnergyDevourer", "mainThermalEnergyDevourer"),
    TST_VACUUM_FILTER_EXTRACTOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_VacuumFilterExtractor", "mainVacuumFilterExtractor"),
    MM_INDISTINCT_TENTACLE_PROTOTYPE_MK2("com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_IndistinctTentaclePrototypeMK2", "mainIndistinctTentaclePrototypeMK2"),
    MM_LARGE_NEUTRON_OSCILLATOR("com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_LargeNeutronOscillator", "main"),
    MTENEUTRON_ACTIVATOR("goodgenerator.blocks.tileEntity.MTENeutronActivator", "$"),
    MTEPRECISE_ASSEMBLER("goodgenerator.blocks.tileEntity.MTEPreciseAssembler", "$"),
    MTECRYOGENIC_FREEZER("gregtech.common.tileentities.machines.multi.MTECryogenicFreezer", "main"),
    MTEDISTILLATION_TOWER("gregtech.common.tileentities.machines.multi.MTEDistillationTower", "base"),
    MTEELECTRIC_BLAST_FURNACE("gregtech.common.tileentities.machines.multi.MTEElectricBlastFurnace", "main"),
    MTEENDOTHERMIC_FRIDGE("gregtech.common.tileentities.machines.multi.MTEEndothermicFridge", "main"),
    MTEEXOTHERMIC_HEARTH("gregtech.common.tileentities.machines.multi.MTEExothermicHearth", "main"),
    MTEFROTH_FLOTATION_CELL("gregtech.common.tileentities.machines.multi.MTEFrothFlotationCell", "main"),
    MTEINDUSTRIAL_BENDING_MACHINE("gregtech.common.tileentities.machines.multi.MTEIndustrialBendingMachine", "main"),
    MTEINDUSTRIAL_BREWERY("gregtech.common.tileentities.machines.multi.MTEIndustrialBrewery", "main"),
    MTEINDUSTRIAL_CENTRIFUGE("gregtech.common.tileentities.machines.multi.MTEIndustrialCentrifuge", "main"),
    MTEINDUSTRIAL_CHEMICAL_BATH("gregtech.common.tileentities.machines.multi.MTEIndustrialChemicalBath", "main"),
    MTEINDUSTRIAL_COKE_OVEN("gregtech.common.tileentities.machines.multi.MTEIndustrialCokeOven", "main"),
    MTEINDUSTRIAL_ELECTROLYZER("gregtech.common.tileentities.machines.multi.MTEIndustrialElectrolyzer", "main"),
    MTEINDUSTRIAL_ELECTROMAGNETIC_SEPARATOR("gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator", "main"),
    MTEINDUSTRIAL_EXTRACTOR("gregtech.common.tileentities.machines.multi.MTEIndustrialExtractor", "main"),
    MTEINDUSTRIAL_FORGE_HAMMER("gregtech.common.tileentities.machines.multi.MTEIndustrialForgeHammer", "main"),
    MTEINDUSTRIAL_LASER_ENGRAVER("gregtech.common.tileentities.machines.multi.MTEIndustrialLaserEngraver", "main"),
    MTEINDUSTRIAL_MACERATOR("gregtech.common.tileentities.machines.multi.MTEIndustrialMacerator", "$"),
    MTEINDUSTRIAL_MIXER("gregtech.common.tileentities.machines.multi.MTEIndustrialMixer", "main"),
    MTEINDUSTRIAL_SIFTER("gregtech.common.tileentities.machines.multi.MTEIndustrialSifter", "main"),
    MTEINDUSTRIAL_THERMAL_CENTRIFUGE("gregtech.common.tileentities.machines.multi.MTEIndustrialThermalCentrifuge", "main"),
    MTELARGE_CHEMICAL_REACTOR("gregtech.common.tileentities.machines.multi.MTELargeChemicalReactor", "main"),
    MTELARGE_FLUID_EXTRACTOR("gregtech.common.tileentities.machines.multi.MTELargeFluidExtractor", "main"),
    MTEMASS_SOLIDIFIER("gregtech.common.tileentities.machines.multi.MTEMassSolidifier", "main"),
    MTEMEGA_CHEMICAL_REACTOR("gregtech.common.tileentities.machines.multi.MTEMegaChemicalReactor", "main"),
    MTEMEGA_DISTILLATION_TOWER("gregtech.common.tileentities.machines.multi.MTEMegaDistillationTower", "base"),
    MTEMEGA_OIL_CRACKER("gregtech.common.tileentities.machines.multi.MTEMegaOilCracker", "main"),
    MTEMULTI_AUTOCLAVE("gregtech.common.tileentities.machines.multi.MTEMultiAutoclave", "main"),
    MTEMULTI_FURNACE("gregtech.common.tileentities.machines.multi.MTEMultiFurnace", "main"),
    MTEOIL_CRACKER("gregtech.common.tileentities.machines.multi.MTEOilCracker", "main"),
    MTESPINMATRON("gregtech.common.tileentities.machines.multi.MTESpinmatron", "t1"),
    MTEVACUUM_FREEZER("gregtech.common.tileentities.machines.multi.MTEVacuumFreezer", "$"),
    MTEEXO_FOUNDRY("gregtech.common.tileentities.machines.multi.foundry.MTEExoFoundry", "main"),
    MTEFORGE_OF_GODS("tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods", "main"),
    MTESMELTING_MODULE("tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule", "main"),
    MTEPLASMA_MODULE("tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule", "main"),
    MTEEXOTIC_MODULE("tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule", "main"),
    MTEPURIFICATION_UNIT_BARYONIC_PERFECTION("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBaryonicPerfection", "main"),
    MTEPURIFICATION_UNIT_CLARIFIER("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitClarifier", "main"),
    MTEPURIFICATION_UNIT_DEGASSER("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitDegasser", "main"),
    MTEPURIFICATION_UNIT_FLOCCULATION("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitFlocculation", "main"),
    MTEPURIFICATION_UNIT_OZONATION("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitOzonation", "main"),
    MTEPURIFICATION_UNIT_PH_ADJUSTMENT("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitPhAdjustment", "main"),
    MTEPURIFICATION_UNIT_PLASMA_HEATER("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitPlasmaHeater", "main"),
    MTEPURIFICATION_UNIT_UVTREATMENT("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitUVTreatment", "main"),
    MTEINDUSTRIAL_DEHYDRATOR("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialDehydrator", "$"),
    MTEINDUSTRIAL_FLUID_HEATER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialFluidHeater", "$"),
    MTEISA_MILL("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIsaMill", "$"),
    MTEADV_DISTILLATION_TOWER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvDistillationTower", "base"),
    MTEADV_EBF("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvEBF", "$"),
    MTEALLOY_BLAST_SMELTER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEAlloyBlastSmelter", "$"),
    MTEAUTO_CRAFTER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEAutoCrafter", "$"),
    MTEQUANTUM_FORCE_TRANSFORMER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEQuantumForceTransformer", "main"),
    MTECHEMICAL_PLANT("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant", "$"),
    MTEMEGA_ALLOY_BLAST_SMELTER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.mega.MTEMegaAlloyBlastSmelter", "main"),
    MTEDIGESTER("gtnhlanth.common.tileentity.MTEDigester", "$"),
    MTEDISSOLUTION_TANK("gtnhlanth.common.tileentity.MTEDissolutionTank", "$"),
    MTEINDUSTRIAL_ARC_FURNACE("kubatech.tileentity.gregtech.multiblock.MTEIndustrialArcFurnace", "main"),
    MTEDEFUSION_CRAFTER("kubatech.tileentity.gregtech.multiblock.MTEDEFusionCrafter", "main");
    // spotless:on

    final String className;
    final String mainPiece;

    BottledChaosSupportedMachine(String className, String mainPiece) {
        this.className = className;
        this.mainPiece = mainPiece;
    }

    static BottledChaosSupportedMachine find(IMetaTileEntity machine) {
        for (var supported : values()) if (supported.className.equals(
            machine.getClass()
                .getName()))
            return supported;
        return null;
    }

    /** Only real finite bounds are expanded. Unbounded structures publish representative complete sizes. */
    int[] displayProfiles() {
        return switch (this) {
            case MTEDISTILLATION_TOWER -> new int[] { 12 };
            case MTEADV_DISTILLATION_TOWER -> range(3, 12);
            case MTEMEGA_DISTILLATION_TOWER -> range(1, 5);
            case MTENEUTRON_ACTIVATOR -> range(4, 249);
            case MTESPINMATRON -> range(1, 4);
            case MTEINDUSTRIAL_MACERATOR, TST_SWELEGFYR_BLAST_FURNACE -> range(1, 2);
            case MTEINDUSTRIAL_COKE_OVEN -> new int[] { 1, 2, 16 };
            case GT_TILE_ENTITY_MAGNETIC_DOMAIN_CONSTRUCTOR, GT_TILE_ENTITY_MOLECULE_DECONSTRUCTOR, TST_GIANT_VACUUM_DRYING_FURNACE -> new int[] {
                1, 2 };
            case MTEEXO_FOUNDRY -> range(1, 3);
            default -> new int[] { 1 };
        };
    }

    boolean hasRepeatingPiece() {
        return switch (this) {
            case GT_TILE_ENTITY_MAGNETIC_DOMAIN_CONSTRUCTOR, GT_TILE_ENTITY_MOLECULE_DECONSTRUCTOR, TST_GIANT_VACUUM_DRYING_FURNACE, MTEINDUSTRIAL_COKE_OVEN, MTENEUTRON_ACTIVATOR -> true;
            default -> false;
        };
    }

    boolean isPurificationUnit() {
        return className.startsWith("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnit");
    }

    private static int[] range(int minimum, int maximum) {
        int[] profiles = new int[maximum - minimum + 1];
        for (int i = 0; i < profiles.length; i++) profiles[i] = minimum + i;
        return profiles;
    }

    /** Position offsets are used only to count overlapping pieces once while loading the catalogue. */
    @Desugar
    record Piece(String name, int a, int b, int c) {}

    List<Piece> pieces(IMetaTileEntity machine, int size) {
        List<Piece> pieces = new ArrayList<>();
        switch (this) {
            case GT_TILE_ENTITY_MAGNETIC_DOMAIN_CONSTRUCTOR, GT_TILE_ENTITY_MOLECULE_DECONSTRUCTOR -> {
                String suffix = this == GT_TILE_ENTITY_MAGNETIC_DOMAIN_CONSTRUCTOR ? "MagneticDomainConstructor"
                    : "MoleculeDeconstructor";
                int y = this == GT_TILE_ENTITY_MAGNETIC_DOMAIN_CONSTRUCTOR ? 15 : 9;
                pieces.add(new Piece("main" + suffix, 7, y, 0));
                for (int i = 1; i < size; i++) pieces.add(new Piece("middle" + suffix, 7, y, -4 * i));
                pieces.add(new Piece("end" + suffix, 7, y, -4 * size));
            }
            case TST_GIANT_VACUUM_DRYING_FURNACE -> {
                pieces.add(new Piece("VacuumPump", 10, 4, 0));
                pieces.add(new Piece("MainFrame", 1, 3, 0));
                pieces.add(new Piece("DryingTowerBase", -2, 1, 0));
                for (int i = 0; i < size; i++) pieces.add(new Piece("DryingTowerMiddle", -2, 8 + 7 * i, 0));
                pieces.add(new Piece("DryingTowerHat", -2, 4 + 7 * size, 0));
            }
            case TST_BALL_LIGHTNING -> pieces.add(new Piece("BallLightningMK1", 12, 20, 7));
            case MTEDISTILLATION_TOWER, MTEADV_DISTILLATION_TOWER -> {
                int height = size;
                pieces.add(new Piece("base", 1, 0, 0));
                for (int i = 1; i < height - 1; i++) pieces.add(new Piece("layerHint", 1, i, 0));
                pieces.add(new Piece("topHint", 1, height - 1, 0));
            }
            case MTEMEGA_DISTILLATION_TOWER -> {
                int height = size;
                pieces.add(new Piece("base", 6, 9, 0));
                for (int i = 1; i <= height; i++) pieces.add(new Piece("layer", 6, 9 + 6 * i, 0));
                pieces.add(new Piece("top", 6, 21 + 6 * height, 0));
            }
            case MTENEUTRON_ACTIVATOR -> {
                String name = machine.getStackForm(1)
                    .getUnlocalizedName()
                    .replace("gt.blockmachines.", "");
                pieces.add(new Piece(name + "buttom", 2, 0, 0));
                for (int i = 1; i <= size; i++) pieces.add(new Piece(name + "mid", 2, i, 0));
                pieces.add(new Piece(name + "top", 2, size + 1, 0));
            }
            case MTEINDUSTRIAL_COKE_OVEN -> {
                pieces.add(new Piece("main", 1, 5, 1));
                for (int i = 1; i < size; i++) pieces.add(new Piece("slice", -1 - 2 * i, 5, 1));
            }
            case MTEINDUSTRIAL_MACERATOR -> pieces.add(new Piece(size == 1 ? "main_t1" : "main_t2", 0, 0, 0));
            case MTESPINMATRON -> pieces.add(new Piece("t" + size, 0, 0, 0));
            case TST_SWELEGFYR_BLAST_FURNACE -> pieces.add(new Piece("mainT" + size, 0, 0, 0));
            case MTEEXO_FOUNDRY -> pieces.add(new Piece("main", 7, 53, 0));
            case MTEFORGE_OF_GODS -> {
                pieces.add(new Piece("main", 63, 14, 1));
                if (size >= 2) pieces.add(new Piece("second_ring", 55, 11, -67));
                if (size >= 3) pieces.add(new Piece("third_ring", 47, 13, -76));
            }
            default -> pieces.add(
                new Piece(
                    mainPiece.equals("$") ? machine.getStackForm(1)
                        .getUnlocalizedName()
                        .replace("gt.blockmachines.", "") : mainPiece,
                    0,
                    0,
                    0));
        }
        return pieces;
    }
}
