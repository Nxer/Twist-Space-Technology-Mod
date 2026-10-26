package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineParameters.Machine;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.github.bsideup.jabel.Desugar;

import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;

/** One registration per controller: structure reader, recipe-pool policy and runtime parameter implementation. */
public enum BottledChaosSupportedMachine {

    // spotless:off
    MTEBIO_VAT("bartworks.common.tileentities.multis.MTEBioVat", "main", Machine.BIO_VAT),
    GTCM_CRYSTALLINE_INFINITIER("com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier", "mainCrystallineInfinitier", Machine.CRYSTALLINE),
    GT_TILE_ENTITY_INTENSIFY_CHEMICAL_DISTORTER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IntensifyChemicalDistorter", "main", Machine.CHEMICAL_DISTORTER),
    GT_TILE_ENTITY_MAGNETIC_DOMAIN_CONSTRUCTOR("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDomainConstructor", "mainMagneticDomainConstructor", Machine.MAGNETIC_DOMAIN, StructureKind.REPEATING),
    GT_TILE_ENTITY_MAGNETIC_DRIVE_PRESSURE_FORMER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticDrivePressureFormer", "main", Machine.PRESSURE_FORMER),
    GT_TILE_ENTITY_MAGNETIC_MIXER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MagneticMixer", "main", Machine.MAGNETIC_MIXER),
    GT_TILE_ENTITY_MOLECULE_DECONSTRUCTOR("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_MoleculeDeconstructor", "mainMoleculeDeconstructor", Machine.MOLECULE, StructureKind.REPEATING),
    GT_TILE_ENTITY_PHYSICAL_FORM_SWITCHER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PhysicalFormSwitcher", "main", Machine.PHYSICAL_SWITCHER),
    GT_TILE_ENTITY_PRECISE_HIGH_ENERGY_PHOTONIC_QUANTUM_MASTER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster", "main", Machine.PHOTON_MASTER),
    GT_TILE_ENTITY_SPACE_SCALER("com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_SpaceScaler", "mainSpaceScaler", Machine.SPACE_SCALER),
    TST_ADVANCED_MEGA_OIL_CRACKER("com.Nxer.TwistSpaceTechnology.common.machine.TST_AdvancedMegaOilCracker", "mainAdvancedMegaOilCracker", Machine.ADVANCED_OIL_CRACKER),
    TST_BALL_LIGHTNING("com.Nxer.TwistSpaceTechnology.common.machine.TST_BallLightning", "BallLightningMK1", Machine.BALL_LIGHTNING),
    TST_BIOSPHERE_III("com.Nxer.TwistSpaceTechnology.common.machine.TST_BiosphereIII", "mainBiosphereIII", Machine.BIOSPHERE),
    TST_CORE_DEVICE_OF_HUMAN_POWER_GENERATION_FACILITY("com.Nxer.TwistSpaceTechnology.common.machine.TST_CoreDeviceOfHumanPowerGenerationFacility", "mainCoreDeviceOfHumanPowerGenerationFacility", Machine.HUMAN_POWER),
    TST_GIANT_VACUUM_DRYING_FURNACE("com.Nxer.TwistSpaceTechnology.common.machine.TST_GiantVacuumDryingFurnace", "VacuumPump", Machine.GIANT_DRYER, StructureKind.REPEATING),
    TST_HEPHAESTUS_ATELIER("com.Nxer.TwistSpaceTechnology.common.machine.TST_HephaestusAtelier", "mainHephaestusAtelier", Machine.HEPHAESTUS),
    TST_INDISTINCT_TENTACLE("com.Nxer.TwistSpaceTechnology.common.machine.TST_IndistinctTentacle", "mainIndistinctTentacle", Machine.INDISTINCT),
    TST_INDUSTRIAL_MAGNETAR_SEPARATOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_IndustrialMagnetarSeparator", "STRUCTURE_PIECE_MAIN", Machine.MAGNETAR_SEPARATOR),
    TST_MANUFACTURING_CENTER("com.Nxer.TwistSpaceTechnology.common.machine.TST_ManufacturingCenter", "main", Machine.MANUFACTURING),
    TST_MIRACLE_DOOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_MiracleDoor", "mainMiracleDoor", Machine.STELLAR_FORGE, PoolPolicy.STELLAR_FORGE),
    TST_HYPER_THERMAL_CONVECTOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_HyperThermalConvector", "main", Machine.HEAT_CONVECTOR),
    TST_MEGA_MACERATOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaMacerator", "mainMegaMacerator", Machine.MEGA_MACERATOR),
    TST_SCAVENGER("com.Nxer.TwistSpaceTechnology.common.machine.TST_Scavenger", "mainScavenger", Machine.SCAVENGER),
    TST_SWELEGFYR_BLAST_FURNACE("com.Nxer.TwistSpaceTechnology.common.machine.TST_SwelegfyrBlastFurnace", "$", Machine.SWELEGFYR),
    TST_THERMAL_ENERGY_DEVOURER("com.Nxer.TwistSpaceTechnology.common.machine.TST_ThermalEnergyDevourer", "mainThermalEnergyDevourer", Machine.THERMAL_DEVOURER),
    TST_VACUUM_FILTER_EXTRACTOR("com.Nxer.TwistSpaceTechnology.common.machine.TST_VacuumFilterExtractor", "mainVacuumFilterExtractor", Machine.VACUUM_FILTER),
    MM_INDISTINCT_TENTACLE_PROTOTYPE_MK2("com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_IndistinctTentaclePrototypeMK2", "mainIndistinctTentaclePrototypeMK2", Machine.INDISTINCT_PROTOTYPE),
    MM_LARGE_NEUTRON_OSCILLATOR("com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_LargeNeutronOscillator", "main", Machine.MODULAR_NEUTRON),
    MTENEUTRON_ACTIVATOR("goodgenerator.blocks.tileEntity.MTENeutronActivator", "$", Machine.NEUTRON, StructureKind.REPEATING, PoolPolicy.NEUTRON),
    MTEPRECISE_ASSEMBLER("goodgenerator.blocks.tileEntity.MTEPreciseAssembler", "$", Machine.PRECISE_ASSEMBLER),
    MTECRYOGENIC_FREEZER("gregtech.common.tileentities.machines.multi.MTECryogenicFreezer", "main", Machine.CRYOGENIC_FREEZER),
    MTEFUSION_COMPUTER_1("gregtech.common.tileentities.machines.multi.MTEFusionComputer1", "main", Machine.FUSION),
    MTEFUSION_COMPUTER_2("gregtech.common.tileentities.machines.multi.MTEFusionComputer2", "main", Machine.FUSION),
    MTEFUSION_COMPUTER_3("gregtech.common.tileentities.machines.multi.MTEFusionComputer3", "main", Machine.FUSION),
    MTEADV_FUSION_MK4("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvFusionMk4", "main", Machine.FUSION),
    MTEADV_FUSION_MK5("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvFusionMk5", "main", Machine.FUSION),
    MTELARGE_FUSION_COMPUTER_1("goodgenerator.blocks.tileEntity.MTELargeFusionComputer1", "largeFusion", Machine.FUSION),
    MTELARGE_FUSION_COMPUTER_2("goodgenerator.blocks.tileEntity.MTELargeFusionComputer2", "largeFusion", Machine.FUSION),
    MTELARGE_FUSION_COMPUTER_3("goodgenerator.blocks.tileEntity.MTELargeFusionComputer3", "largeFusion", Machine.FUSION),
    MTELARGE_FUSION_COMPUTER_4("goodgenerator.blocks.tileEntity.MTELargeFusionComputer4", "largeFusion", Machine.FUSION),
    MTELARGE_FUSION_COMPUTER_5("goodgenerator.blocks.tileEntity.MTELargeFusionComputer5", "largeFusion", Machine.FUSION),
    MTEDISTILLATION_TOWER("gregtech.common.tileentities.machines.multi.MTEDistillationTower", "base", Machine.DISTILLATION_TOWER),
    MTEELECTRIC_BLAST_FURNACE("gregtech.common.tileentities.machines.multi.MTEElectricBlastFurnace", "main", Machine.BLAST_FURNACE),
    MTEENDOTHERMIC_FRIDGE("gregtech.common.tileentities.machines.multi.MTEEndothermicFridge", "main", Machine.FRIDGE),
    MTEEXOTHERMIC_HEARTH("gregtech.common.tileentities.machines.multi.MTEExothermicHearth", "main", Machine.HEARTH),
    MTEFROTH_FLOTATION_CELL("gregtech.common.tileentities.machines.multi.MTEFrothFlotationCell", "main", Machine.FLOTATION),
    MTEINDUSTRIAL_BENDING_MACHINE("gregtech.common.tileentities.machines.multi.MTEIndustrialBendingMachine", "main", Machine.BENDING),
    MTEINDUSTRIAL_BREWERY("gregtech.common.tileentities.machines.multi.MTEIndustrialBrewery", "main", Machine.BREWERY),
    MTEINDUSTRIAL_CENTRIFUGE("gregtech.common.tileentities.machines.multi.MTEIndustrialCentrifuge", "main", Machine.CENTRIFUGE),
    MTEINDUSTRIAL_CHEMICAL_BATH("gregtech.common.tileentities.machines.multi.MTEIndustrialChemicalBath", "main", Machine.CHEMICAL_BATH),
    MTEINDUSTRIAL_COKE_OVEN("gregtech.common.tileentities.machines.multi.MTEIndustrialCokeOven", "main", Machine.COKE_OVEN, StructureKind.REPEATING),
    MTEINDUSTRIAL_ELECTROLYZER("gregtech.common.tileentities.machines.multi.MTEIndustrialElectrolyzer", "main", Machine.ELECTROLYZER),
    MTEINDUSTRIAL_ELECTROMAGNETIC_SEPARATOR("gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator", "main", Machine.MAGNET_SEPARATOR),
    MTEINDUSTRIAL_EXTRACTOR("gregtech.common.tileentities.machines.multi.MTEIndustrialExtractor", "main", Machine.EXTRACTOR),
    MTEINDUSTRIAL_FORGE_HAMMER("gregtech.common.tileentities.machines.multi.MTEIndustrialForgeHammer", "main", Machine.FORGE_HAMMER),
    MTEINDUSTRIAL_LASER_ENGRAVER("gregtech.common.tileentities.machines.multi.MTEIndustrialLaserEngraver", "main", Machine.LASER_ENGRAVER),
    MTEINDUSTRIAL_MACERATOR("gregtech.common.tileentities.machines.multi.MTEIndustrialMacerator", "$", Machine.MACERATOR),
    MTEINDUSTRIAL_MIXER("gregtech.common.tileentities.machines.multi.MTEIndustrialMixer", "main", Machine.MIXER),
    MTEINDUSTRIAL_SIFTER("gregtech.common.tileentities.machines.multi.MTEIndustrialSifter", "main", Machine.SIFTER),
    MTEINDUSTRIAL_THERMAL_CENTRIFUGE("gregtech.common.tileentities.machines.multi.MTEIndustrialThermalCentrifuge", "main", Machine.THERMAL_CENTRIFUGE),
    MTELARGE_CHEMICAL_REACTOR("gregtech.common.tileentities.machines.multi.MTELargeChemicalReactor", "main", Machine.LARGE_CHEMICAL),
    MTELARGE_FLUID_EXTRACTOR("gregtech.common.tileentities.machines.multi.MTELargeFluidExtractor", "main", Machine.LARGE_FLUID_EXTRACTOR),
    MTEMASS_SOLIDIFIER("gregtech.common.tileentities.machines.multi.MTEMassSolidifier", "main", Machine.SOLIDIFIER),
    MTEMEGA_CHEMICAL_REACTOR("gregtech.common.tileentities.machines.multi.MTEMegaChemicalReactor", "main", Machine.MEGA_CHEMICAL),
    MTEMEGA_DISTILLATION_TOWER("gregtech.common.tileentities.machines.multi.MTEMegaDistillationTower", "base", Machine.MEGA_DISTILLATION),
    MTEMEGA_OIL_CRACKER("gregtech.common.tileentities.machines.multi.MTEMegaOilCracker", "main", Machine.MEGA_OIL_CRACKER),
    MTEMULTI_AUTOCLAVE("gregtech.common.tileentities.machines.multi.MTEMultiAutoclave", "main", Machine.AUTOCLAVE),
    MTEMULTI_FURNACE("gregtech.common.tileentities.machines.multi.MTEMultiFurnace", "main", Machine.MULTI_FURNACE),
    MTEOIL_CRACKER("gregtech.common.tileentities.machines.multi.MTEOilCracker", "main", Machine.OIL_CRACKER),
    MTENAQUADAH_FUEL_REFINERY("gregtech.common.tileentities.machines.multi.MTENaquadahFuelRefinery", "$", Machine.NAQUADAH_REFINERY),
    MTESPINMATRON("gregtech.common.tileentities.machines.multi.MTESpinmatron", "t1", Machine.SPINMATRON),
    MTEVACUUM_FREEZER("gregtech.common.tileentities.machines.multi.MTEVacuumFreezer", "$", Machine.VACUUM_FREEZER),
    MTEEXO_FOUNDRY("gregtech.common.tileentities.machines.multi.foundry.MTEExoFoundry", "main", Machine.FOUNDRY, StructureKind.FOUNDRY),
    MTEFORGE_OF_GODS("tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods", "main", null),
    MTESMELTING_MODULE("tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule", "main", Machine.GODFORGE_SMELTING, StructureKind.GODFORGE),
    MTEMOLTEN_MODULE("tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule", "main", Machine.GODFORGE_MOLTEN, StructureKind.GODFORGE),
    MTEPLASMA_MODULE("tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule", "main", Machine.GODFORGE_PLASMA, StructureKind.GODFORGE),
    MTEEXOTIC_MODULE("tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule", "main", Machine.GODFORGE_EXOTIC, StructureKind.GODFORGE),
    MTEPURIFICATION_UNIT_BARYONIC_PERFECTION("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBaryonicPerfection", "main", Machine.PURIFICATION_BARYONIC),
    MTEPURIFICATION_UNIT_CLARIFIER("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitClarifier", "main", Machine.PURIFICATION_CLARIFIER),
    MTEPURIFICATION_UNIT_DEGASSER("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitDegasser", "main", Machine.PURIFICATION_DEGASSER),
    MTEPURIFICATION_UNIT_FLOCCULATION("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitFlocculation", "main", Machine.PURIFICATION_FLOCCULATION),
    MTEPURIFICATION_UNIT_OZONATION("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitOzonation", "main", Machine.PURIFICATION_OZONATION),
    MTEPURIFICATION_UNIT_PH_ADJUSTMENT("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitPhAdjustment", "main", Machine.PURIFICATION_PH),
    MTEPURIFICATION_UNIT_PLASMA_HEATER("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitPlasmaHeater", "main", Machine.PURIFICATION_PLASMA),
    MTEPURIFICATION_UNIT_UVTREATMENT("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitUVTreatment", "main", Machine.PURIFICATION_UV),
    MTEINDUSTRIAL_DEHYDRATOR("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialDehydrator", "$", Machine.DEHYDRATOR),
    MTEINDUSTRIAL_FLUID_HEATER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialFluidHeater", "$", Machine.FLUID_HEATER),
    MTEISA_MILL("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIsaMill", "$", Machine.ISA_MILL),
    MTEADV_DISTILLATION_TOWER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvDistillationTower", "base", Machine.ADVANCED_DISTILLATION),
    MTEADV_EBF("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvEBF", "$", Machine.ADVANCED_BLAST),
    MTEALLOY_BLAST_SMELTER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEAlloyBlastSmelter", "$", Machine.ALLOY_BLAST),
    MTEAUTO_CRAFTER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEAutoCrafter", "$", Machine.AUTO_CRAFTER),
    MTEQUANTUM_FORCE_TRANSFORMER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEQuantumForceTransformer", "main", Machine.QUANTUM),
    MTECHEMICAL_PLANT("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant", "$", Machine.CHEMICAL_PLANT),
    MTEMEGA_ALLOY_BLAST_SMELTER("gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.mega.MTEMegaAlloyBlastSmelter", "main", Machine.MEGA_ALLOY),
    MTEDIGESTER("gtnhlanth.common.tileentity.MTEDigester", "$", Machine.DIGESTER),
    MTEDISSOLUTION_TANK("gtnhlanth.common.tileentity.MTEDissolutionTank", "$", Machine.DISSOLUTION),
    MTEINDUSTRIAL_ARC_FURNACE("kubatech.tileentity.gregtech.multiblock.MTEIndustrialArcFurnace", "main", Machine.ARC_FURNACE, PoolPolicy.ARC_ONLY),
    MTEDEFUSION_CRAFTER("kubatech.tileentity.gregtech.multiblock.MTEDEFusionCrafter", "main", Machine.DE_FUSION),
    MTEPURIFICATION_PLANT("gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant", null, null, StructureKind.PURIFICATION_PLANT),
    SINGLE_BLOCK(null, null, null, StructureKind.SINGLE_BLOCK);
    // spotless:on

    enum StructureKind {
        STANDARD,
        REPEATING,
        SINGLE_BLOCK,
        PURIFICATION_PLANT,
        FOUNDRY,
        GODFORGE
    }

    private enum PoolPolicy {
        NATIVE,
        NEUTRON,
        STELLAR_FORGE,
        ARC_ONLY
    }

    final String className;
    final String mainPiece;
    final Machine parameters;
    final StructureKind structureKind;
    private final PoolPolicy poolPolicy;

    BottledChaosSupportedMachine(String className, String mainPiece, Machine parameters) {
        this(className, mainPiece, parameters, StructureKind.STANDARD, PoolPolicy.NATIVE);
    }

    BottledChaosSupportedMachine(String className, String mainPiece, Machine parameters, StructureKind structureKind) {
        this(className, mainPiece, parameters, structureKind, PoolPolicy.NATIVE);
    }

    BottledChaosSupportedMachine(String className, String mainPiece, Machine parameters, PoolPolicy poolPolicy) {
        this(className, mainPiece, parameters, StructureKind.STANDARD, poolPolicy);
    }

    BottledChaosSupportedMachine(String className, String mainPiece, Machine parameters, StructureKind structureKind,
        PoolPolicy poolPolicy) {
        this.className = className;
        this.mainPiece = mainPiece;
        this.parameters = parameters;
        this.structureKind = structureKind;
        this.poolPolicy = poolPolicy;
    }

    static BottledChaosSupportedMachine find(IMetaTileEntity machine) {
        if (machine == null) return null;
        if (machine instanceof MTEPurificationPlant) return MTEPURIFICATION_PLANT;
        if (machine instanceof MTEBasicMachine basic && basic.isElectric()) return SINGLE_BLOCK;
        String className = machine.getClass()
            .getName();
        for (var supported : values()) if (className.equals(supported.className)) return supported;
        return null;
    }

    static Machine parameters(IMetaTileEntity machine) {
        var supported = find(machine);
        if (supported != null) return supported.parameters;
        // Fusion variants share the same parameter calculation, including subclasses supplied by addons.
        return machine instanceof MTEFusionComputer || machine instanceof MTELargeFusionComputer ? Machine.FUSION
            : null;
    }

    /** Shared by registration and execution so additional or restricted pools cannot diverge. */
    static Collection<RecipeMap<?>> recipeMaps(IMetaTileEntity machine) {
        if (!(machine instanceof RecipeMapWorkable workable)) return Collections.emptyList();
        Collection<RecipeMap<?>> nativePools = workable.getAvailableRecipeMaps();
        var supported = find(machine);
        if (supported == null || supported.poolPolicy == PoolPolicy.NATIVE) return nativePools;
        if (supported.poolPolicy == PoolPolicy.ARC_ONLY) return nativePools.contains(RecipeMaps.arcFurnaceRecipes)
            ? Collections.singletonList(RecipeMaps.arcFurnaceRecipes)
            : Collections.emptyList();
        Collection<RecipeMap<?>> result = new LinkedHashSet<>(nativePools);
        result.add(
            supported.poolPolicy == PoolPolicy.NEUTRON ? GTCMRecipe.NeutronActivatorWithEURecipeMap
                : RecipeMaps.alloyBlastSmelterRecipes);
        return result;
    }

    long poolMask(IMetaTileEntity machine) {
        if (structureKind == StructureKind.PURIFICATION_PLANT)
            return BottledChaosMachineConfiguration.purificationPlantMask();
        long mask = 0;
        for (RecipeMap<?> pool : recipeMaps(machine)) mask |= BottledChaosMachineConfiguration.mask(pool);
        return mask;
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
        return structureKind == StructureKind.REPEATING;
    }

    boolean isPurificationUnit() {
        return className != null
            && className.startsWith("gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnit");
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
