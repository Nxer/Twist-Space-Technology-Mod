package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.PhotonControllerUpgradeCasing;
import com.Nxer.TwistSpaceTechnology.common.block.meta.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BallLightning;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaMacerator;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.github.bsideup.jabel.Desugar;

import bartworks.API.recipe.BartWorksRecipeMaps;
import goodgenerator.blocks.tileEntity.MTEPreciseAssembler;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GlassTier;
import gregtech.common.blocks.BlockCyclotronCoils;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator;
import gregtech.common.tileentities.machines.multi.MTENaquadahFuelRefinery;
import gregtech.common.tileentities.machines.multi.foundry.FoundryData;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import kubatech.loaders.ArcFurnaceElectrode;
import kubatech.loaders.BlockLoader;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

/** Explicit source-machine parameters. Runtime reads only paid materials and the current input snapshot. */
public final class BottledChaosMachineParameters {

    /** Multipliers are per source recipe; quantity speed is applied separately after source duration limits. */
    @Desugar
    public record Profile(long parallel, double euMultiplier, double timeMultiplier, double timeDivisor,
        double powerMultiplier, int maximumRecipeTier, long heat, int glassTier, FluidStack[] upkeep,
        String restrictions, String special, int maximumOverclocks, int heatOverclocks, int casingTier,
        int compressionTier, double minimumSourceDuration, boolean heavyCentrifuge, int plasmaTier,
        boolean multiStepPlasma, int startupReserveSeconds) {}

    /** One case per machine, with mode-specific pools handled explicitly below. */
    private enum Machine {

        // spotless:off
        AUTO_CRAFTER("MTEAutoCrafter"), BREWERY("MTEIndustrialBrewery"), CHEMICAL_BATH("MTEIndustrialChemicalBath"),
        DISTILLATION_TOWER("MTEDistillationTower"), ALLOY_BLAST("MTEAlloyBlastSmelter"), VACUUM_FILTER("TST_VacuumFilterExtractor"),
        MAGNETAR_SEPARATOR("TST_IndustrialMagnetarSeparator"), MAGNET_SEPARATOR("MTEIndustrialElectromagneticSeparator"),
        FLOTATION("MTEFrothFlotationCell"), PHYSICAL_SWITCHER("GT_TileEntity_PhysicalFormSwitcher"),
        FLUID_HEATER("MTEIndustrialFluidHeater"), LARGE_CHEMICAL("MTELargeChemicalReactor"),
        MEGA_CHEMICAL("MTEMegaChemicalReactor"), CHEMICAL_DISTORTER("GT_TileEntity_IntensifyChemicalDistorter"),
        BENDING("MTEIndustrialBendingMachine"), ELECTROLYZER("MTEIndustrialElectrolyzer"),
        MAGNETIC_MIXER("GT_TileEntity_MagneticMixer"), SIFTER("MTEIndustrialSifter"), SCAVENGER("TST_Scavenger"),
        VACUUM_FREEZER("MTEVacuumFreezer"), DIGESTER("MTEDigester"), DISSOLUTION("MTEDissolutionTank"),
        BIO_VAT("MTEBioVat"), MEGA_ALLOY("MTEMegaAlloyBlastSmelter"), BALL_LIGHTNING("TST_BallLightning"),
        INDISTINCT("TST_IndistinctTentacle"), PRECISE_ASSEMBLER("MTEPreciseAssembler"), AUTOCLAVE("MTEMultiAutoclave"),
        CRYSTALLINE("GTCM_CrystallineInfinitier"), MANUFACTURING("TST_ManufacturingCenter"),
        BLAST_FURNACE("MTEElectricBlastFurnace"), BIOSPHERE("TST_BiosphereIII"), OIL_CRACKER("MTEOilCracker"),
        MEGA_OIL_CRACKER("MTEMegaOilCracker"), ADVANCED_OIL_CRACKER("TST_AdvancedMegaOilCracker"),
        ADVANCED_DISTILLATION("MTEAdvDistillationTower"), MEGA_DISTILLATION("MTEMegaDistillationTower"),
        MAGNETIC_DOMAIN("GT_TileEntity_MagneticDomainConstructor"), EXTRACTOR("MTEIndustrialExtractor"),
        SPACE_SCALER("GT_TileEntity_SpaceScaler"), LARGE_FLUID_EXTRACTOR("MTELargeFluidExtractor"),
        HUMAN_POWER("TST_CoreDeviceOfHumanPowerGenerationFacility"), FORGE_HAMMER("MTEIndustrialForgeHammer"),
        PRESSURE_FORMER("GT_TileEntity_MagneticDrivePressureFormer"), COKE_OVEN("MTEIndustrialCokeOven"),
        LASER_ENGRAVER("MTEIndustrialLaserEngraver"), PHOTON_MASTER("GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster"),
        MACERATOR("MTEIndustrialMacerator"), MEGA_MACERATOR("TST_MegaMacerator"),
        MOLECULE("GT_TileEntity_MoleculeDeconstructor"), GIANT_DRYER("TST_GiantVacuumDryingFurnace"),
        DEHYDRATOR("MTEIndustrialDehydrator"), MIXER("MTEIndustrialMixer"), THERMAL_CENTRIFUGE("MTEIndustrialThermalCentrifuge"),
        THERMAL_DEVOURER("TST_ThermalEnergyDevourer"), SOLIDIFIER("MTEMassSolidifier"), ISA_MILL("MTEIsaMill"),
        CRYOGENIC_FREEZER("MTECryogenicFreezer"), ARC_FURNACE("MTEIndustrialArcFurnace"), DE_FUSION("MTEDEFusionCrafter"),
        INDISTINCT_PROTOTYPE("MM_IndistinctTentaclePrototypeMK2"), ADVANCED_BLAST("MTEAdvEBF"),
        GODFORGE_SMELTING("MTESmeltingModule"), HEARTH("MTEExothermicHearth"), SWELEGFYR("TST_SwelegfyrBlastFurnace"),
        CHEMICAL_PLANT("MTEChemicalPlant"), GODFORGE_EXOTIC("MTEExoticModule"), GODFORGE_PLASMA("MTEPlasmaModule"), FOUNDRY("MTEExoFoundry"),
        MULTI_FURNACE("MTEMultiFurnace"), HEPHAESTUS("TST_HephaestusAtelier"), CENTRIFUGE("MTEIndustrialCentrifuge"),
        SPINMATRON("MTESpinmatron"), PURIFICATION_CLARIFIER("MTEPurificationUnitClarifier"),
        PURIFICATION_DEGASSER("MTEPurificationUnitDegasser"), PURIFICATION_FLOCCULATION("MTEPurificationUnitFlocculation"),
        PURIFICATION_OZONATION("MTEPurificationUnitOzonation"), PURIFICATION_PH("MTEPurificationUnitPhAdjustment"),
        PURIFICATION_PLASMA("MTEPurificationUnitPlasmaHeater"), PURIFICATION_BARYONIC("MTEPurificationUnitBaryonicPerfection"),
        PURIFICATION_UV("MTEPurificationUnitUVTreatment"), QUANTUM("MTEQuantumForceTransformer"),
        FRIDGE("MTEEndothermicFridge"), NEUTRON("MTENeutronActivator"), MODULAR_NEUTRON("MM_LargeNeutronOscillator"),
        NAQUADAH_REFINERY("MTENaquadahFuelRefinery");
        // spotless:on

        private final String type;

        Machine(String type) {
            this.type = type;
        }

        static Machine find(IMetaTileEntity controller) {
            String type = controller.getClass()
                .getSimpleName();
            for (Machine machine : values()) if (machine.type.equals(type)) return machine;
            return null;
        }
    }

    public static Profile evaluate(ItemStack controller, Structure structure, RecipeMap<?> pool, long recipeVoltage,
        long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] fluids, double masterTime, double masterEU,
        long masterParallel, int requiredHeat) {
        return evaluate(
            controller,
            structure,
            pool,
            recipeVoltage,
            inputVoltage,
            inputPower,
            items,
            fluids,
            masterTime,
            masterEU,
            masterParallel,
            requiredHeat,
            0,
            null);
    }

    public static Profile evaluate(ItemStack controller, Structure structure, RecipeMap<?> pool, long recipeVoltage,
        long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] fluids, double masterTime, double masterEU,
        long masterParallel, int requiredHeat, int requiredGlassTier, ItemStack requiredSpecialInput) {
        return evaluate(
            controller,
            structure,
            pool,
            recipeVoltage,
            inputVoltage,
            inputPower,
            items,
            fluids,
            masterTime,
            masterEU,
            masterParallel,
            requiredHeat,
            requiredGlassTier,
            requiredSpecialInput,
            false);
    }

    public static Profile evaluate(ItemStack controller, Structure structure, RecipeMap<?> pool, long recipeVoltage,
        long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] fluids, double masterTime, double masterEU,
        long masterParallel, int requiredHeat, int requiredGlassTier, ItemStack requiredSpecialInput,
        boolean heavyCentrifuge) {
        return evaluate(
            controller,
            structure,
            pool,
            recipeVoltage,
            inputVoltage,
            inputPower,
            items,
            fluids,
            masterTime,
            masterEU,
            masterParallel,
            requiredHeat,
            requiredGlassTier,
            requiredSpecialInput,
            heavyCentrifuge,
            ignored -> {});
    }

    public static Profile evaluate(ItemStack controller, Structure structure, RecipeMap<?> pool, long recipeVoltage,
        long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] fluids, double masterTime, double masterEU,
        long masterParallel, int requiredHeat, int requiredGlassTier, ItemStack requiredSpecialInput,
        boolean heavyCentrifuge, Consumer<CheckRecipeResult> failure) {
        if (controller == null || pool == null
            || recipeVoltage <= 0
            || inputVoltage <= 0
            || inputPower <= 0
            || items == null
            || fluids == null
            || !Double.isFinite(masterTime)
            || masterTime <= 0
            || !Double.isFinite(masterEU)
            || masterEU <= 0
            || masterParallel <= 0
            || requiredHeat < 0) return null;
        if (GTCMItemList.TestItem0.equal(controller)) return new Values().profile();
        controller = BottledChaosComplexStructures.parameterController(controller, structure);
        if (!(controller.getItem() instanceof ItemMachines)) return null;
        IMetaTileEntity original = ItemMachines.getMetaTileEntity(controller);
        if (!(original instanceof RecipeMapWorkable workable)) return null;
        Machine machine = Machine.find(original);
        if (!workable.getAvailableRecipeMaps()
            .contains(pool) && !(machine == Machine.NEUTRON && pool == GTCMRecipe.NeutronActivatorWithEURecipeMap))
            return null;
        Values value = new Values();
        if (machine == null) {
            if (!(original instanceof MTEBasicMachine basic) || basic.mTier <= 0) return null;
            value.maximumRecipeTier = basic.mTier;
            return value.profile();
        }
        if (structure == null) return null;
        Upgrades u = new Upgrades(structure == null ? new ItemStack[0] : structure.materials());
        int shape = structure == null ? 1 : structure.profile();
        int t = GTUtility.getTier(inputVoltage);
        int p = GTUtility.getTier(inputPower);
        int recipeTier = GTUtility.getTier(recipeVoltage);
        int c = u.coil.getTier();
        value.glassTier = u.glass;
        value.heat = structure == null ? 0 : structure.heat();
        switch (machine) {
            case AUTO_CRAFTER -> value.set(2L * Math.max(1, t), 1, 1d / 3);
            case BREWERY -> value.set(4L * t, 1, 2d / 3);
            case CHEMICAL_BATH -> value.set(4L * t, 1, 0.2);
            case DISTILLATION_TOWER, DISSOLUTION, VACUUM_FREEZER -> {}
            case ALLOY_BLAST -> value.maximumRecipeTier = t;
            case VACUUM_FILTER -> {
                if (pool != RecipeMaps.distillationTowerRecipes && pool != RecipeMaps.distilleryRecipes) return null;
                value.set(Integer.MAX_VALUE, 0.5, 1d / (1 + p));
                if (pool == RecipeMaps.distilleryRecipes) value.timeDivisor = 4;
            }
            case MAGNETAR_SEPARATOR -> value.set(4L * t, 0.8, 0.25);
            case MAGNET_SEPARATOR -> {
                if (u.magnet < 0 || pool != RecipeMaps.electroMagneticSeparatorRecipes) return null;
                value.set(
                    new int[] { 8, 24, 48, 96, 256 }[u.magnet],
                    new double[] { 0.8, 0.75, 0.7, 0.6, 0.5 }[u.magnet],
                    new double[] { 1d / 1.1, 0.8, 2d / 3, 0.5, 0.4 }[u.magnet]);
            }
            case FLOTATION, ISA_MILL -> value.timeDivisor = 4;
            case PHYSICAL_SWITCHER -> value.set(Integer.MAX_VALUE, 1, Math.pow(0.9, p));
            case FLUID_HEATER -> value.set(8L * t, 0.9, 1d / 2.2);
            case LARGE_CHEMICAL -> value.timeDivisor = 4;
            case MEGA_CHEMICAL -> {
                value.parallel = 256;
                value.timeDivisor = 4;
            }
            case CHEMICAL_DISTORTER -> {
                if (pool != RecipeMaps.multiblockChemicalReactorRecipes) return null;
                value.set(1024, 1, 0.1);
                value.timeDivisor = 4;
            }
            case BENDING -> value.set(6L * t, 1, 1d / 6);
            case ELECTROLYZER -> value.set(4L * t, 0.9, 1d / 2.8);
            case MAGNETIC_MIXER -> value.set(Integer.MAX_VALUE, 1, Math.pow(0.8, p));
            case SIFTER -> value.set(4L * t, 0.75, 0.2);
            case SCAVENGER -> value.set(Integer.MAX_VALUE, 0.6, Math.pow(0.8, p));
            case DIGESTER -> {
                value.timeDivisor = 4;
                value.requiresHeat = true;
            }
            case BIO_VAT -> value.restrictions = text("culture");
            case MEGA_ALLOY -> {
                if (c < 0 && !u.basicAlloyCoil) return null;
                value.set(256, Math.pow(0.95, Math.max(c + 1 - recipeTier, 0)), 1 - 0.05 * Math.max(c - 3, 0));
                value.maximumRecipeTier = u.glass;
            }
            case BALL_LIGHTNING -> {
                if (pool != RecipeMaps.arcFurnaceRecipes || shape != 1
                    || u.ballChip
                    || u.gravityLens
                    || u.fusion < 1
                    || c < HeatingCoilLevel.UMV.getTier()
                    || u.ballField < 1) return null;
                value.set(powerOfTwo((long) u.fusion * c), 1, 1);
                value.timeDivisor = u.ballField >= 3 ? 4 : 2;
                value.maximumRecipeTier = u.glass < 12 ? u.glass : -1;
            }
            case INDISTINCT -> {
                if (pool != RecipeMaps.assemblerRecipes || u.component < 0) return null;
                value.set(Integer.MAX_VALUE, 1, 0.25);
                value.timeDivisor = u.component >= recipeTier ? 4 : 2;
                value.maximumRecipeTier = u.component + 2;
            }
            case PRECISE_ASSEMBLER -> {
                if (pool != RecipeMaps.assemblerRecipes || u.precision < -1
                    || u.machineCasing < 0
                    || u.glass < VoltageIndex.EV) return null;
                value.set(powerOfTwo(u.precision + 5L), 1, 0.5);
                value.maximumRecipeTier = u.machineCasing < VoltageIndex.UHV ? u.machineCasing : -1;
            }
            case AUTOCLAVE -> {
                if (u.itemPipe < 1 || u.fluidPipe < 1 || c < 0) return null;
                value.set(12L * u.itemPipe, (12 - u.fluidPipe) / 12d, 1 / (1 + 0.25 * (c + 1)));
            }
            case CRYSTALLINE -> {
                if (pool != RecipeMaps.autoclaveRecipes && pool != RecipeMaps.chemicalBathRecipes) return null;
                if (u.field < 1 || u.glass < 1) return null;
                value.set(
                    Math.min(Integer.MAX_VALUE, (long) u.glass * u.field * p),
                    1d / u.field,
                    pool == RecipeMaps.chemicalBathRecipes ? 0.0625 : 0.25);
                value.timeDivisor = u.field >= 3 ? 4 : 2;
            }
            case MANUFACTURING -> {
                if (!manufacturingPool(pool) || u.core < 5) return null;
                value.set(2L * t, 1 - 0.15 * Math.max(u.core - 5, 0), 1 / (1.2 + 0.5 * (u.core - 5)));
                value.maximumRecipeTier = u.core;
                value.compressionTier = 0;
            }
            case BLAST_FURNACE -> {
                value.heat += 100L * Math.max(t - 2, 0);
                value.heatDiscount = 0.95;
            }
            case BIOSPHERE -> {
                if (pool == RecipeMaps.brewingRecipes || pool == RecipeMaps.fermentingRecipes)
                    value.set(powerOfTwo(Math.max(2L * u.glass - 6, 0)), 1, 0.25);
                else {
                    long dishes = count(items, requiredSpecialInput);
                    if (requiredSpecialInput == null || dishes <= 0) return reject(pool, "culture", dishes, 1, failure);
                    if (u.glass < requiredGlassTier) return reject(pool, "glass", u.glass, requiredGlassTier, failure);
                    value.set(dishes, 1, 1 / (1.6 + 0.6 * (u.glass - requiredGlassTier)));
                    value.restrictions = text("culture");
                }
            }
            case OIL_CRACKER -> {
                if (c < 0) return null;
                value.euMultiplier = Math.max(0.5, 1 - 0.1 * (c + 1));
            }
            case MEGA_OIL_CRACKER -> {
                if (c < 0) return null;
                value.set(256, Math.pow(0.9, c + 1), 1);
            }
            case ADVANCED_OIL_CRACKER -> {
                if (c < 0) return null;
                value.set(256, 1d / (c + 1), 0.5);
                value.timeDivisor = u.coil == HeatingCoilLevel.UXV || u.coil == HeatingCoilLevel.MAX ? 4 : 2;
            }
            case ADVANCED_DISTILLATION -> {
                if (pool == RecipeMaps.distillationTowerRecipes) value.set(12, 1, 1d / 3);
                else if (pool == RecipeMaps.distilleryRecipes) value.set(2L * (shape / 3) * t, 0.15, 0.5);
                else return null;
            }
            case MEGA_DISTILLATION -> {
                if (pool == RecipeMaps.distillationTowerRecipes) value.set(256, 0.9, 2d / 3);
                // The source increments height after checking the final middle section.
                else if (pool == RecipeMaps.distilleryRecipes) value.set(256L * (1 + (shape + 1) / 2), 0.5, 0.5);
                else return null;
            }
            case MAGNETIC_DOMAIN -> value.set(64L * shape, 1, Math.pow(0.75, p));
            case EXTRACTOR -> {
                if (u.itemPipe < 1) return null;
                value.set(8L * u.itemPipe, 0.85, 1d / 3);
            }
            case SPACE_SCALER -> {
                if (pool == BartWorksRecipeMaps.electricImplosionCompressorRecipes) {
                    if (u.field < 11) return null;
                } else if (pool != RecipeMaps.extractorRecipes || u.field < 1) return null;
                value.set(Integer.MAX_VALUE, 1, u.field >= 2 ? 0.1 : 1);
                value.timeDivisor = u.field >= 2 ? 4 : 2;
            }
            case LARGE_FLUID_EXTRACTOR -> {
                if (c < 0 || u.solenoid < 0) return null;
                value.set(Math.max(1, 8L * u.solenoid), 0.8 * Math.pow(0.9, c), 1 / (1.5 + 0.1 * c));
            }
            case HUMAN_POWER -> {
                if (c < 0) return null;
                value.set(Integer.MAX_VALUE, 1, 1d / (c + 1));
                value.timeDivisor = 4;
            }
            case FORGE_HAMMER -> value.set(6L * Math.max(1, u.solenoid) * t, 1, 0.5);
            case PRESSURE_FORMER -> {
                if (pool != RecipeMaps.hammerRecipes && pool != RecipeMaps.benderRecipes || c < 0) return null;
                value.set(1024, 0.75, 1d / (16 * (c + 1)));
                value.timeDivisor = 4;
            }
            case COKE_OVEN -> {
                if (c < 0) return null;
                int width = Math.max(0, shape - 1);
                if (u.cokeTier < 0) return null;
                boolean adiabatic = u.cokeTier > 0;
                value.set(adiabatic ? 32L + 16L * width : 16L + 8L * width, Math.pow(0.98, c + 1), 1);
            }
            case LASER_ENGRAVER -> {
                if (u.laserTier < 0 || u.glass < u.laserTier || u.laserParallel <= 0) return null;
                value.set(u.laserParallel, 0.8, 1d / 3.5);
                value.maximumRecipeTier = u.laserTier < VoltageIndex.UXV ? u.laserTier + 1 : -1;
                value.maximumOverclocks = Math.max(0, u.laserTier - recipeTier + 1);
            }
            case PHOTON_MASTER -> {
                if (pool != RecipeMaps.laserEngraverRecipes) return null;
                value.set(256, 1, 1d / (2 * (1 + u.photonSpeed / 10000d)));
                value.timeDivisor = u.photonPerfect ? 4 : 2;
            }
            case MACERATOR -> value.set((shape > 1 ? 8L : 2L) * t, 1, shape > 1 ? 0.15625 : 0.625);
            case MEGA_MACERATOR -> {
                if (u.maceratorTier < 1) return null;
                long parallel = u.maceratorTier >= 3 ? Integer.MAX_VALUE : u.maceratorTier == 2 ? 32768 : 128;
                value.set(parallel, 1, u.glass >= 12 || u.glass > recipeTier ? 0.125 : 1);
            }
            case MOLECULE -> {
                if (pool != RecipeMaps.centrifugeNonCellRecipes && pool != RecipeMaps.electrolyzerNonCellRecipes)
                    return null;
                value.set(24L * shape, 1, Math.pow(0.9, p));
                value.timeDivisor = shape >= 16 ? 4 : 2;
            }
            case GIANT_DRYER -> {
                if (c < 0) return null;
                value.set(Math.min(Integer.MAX_VALUE, (long) shape * (c + 1) * 32), 1, 2 * Math.pow(0.8, p) / (c + 1));
            }
            case DEHYDRATOR -> {
                value.set(4, 0.5, 1d / 2.2);
                value.heatDiscount = 0.95;
            }
            case MIXER -> {
                if (u.itemPipe < 1) return null;
                value.set(8L * t, 1, 1d / (u.itemPipe + 2));
            }
            case THERMAL_CENTRIFUGE -> {
                if (c < 0 || u.solenoid < 0) return null;
                value.set(8L * t + 2L * u.solenoid, 0.8 * Math.pow(0.95, c), 1d / (2.5 + 0.05 * c));
            }
            case THERMAL_DEVOURER -> value.set(1024, 1, 1d / (2 * (1 + p)));
            case SOLIDIFIER -> value.set(10L * t, 0.8, 1d / 3);
            case CRYOGENIC_FREEZER -> {
                value.set(16, 0.9, 1d / 3);
                value.upkeep = cryotheum(10);
            }
            case ARC_FURNACE -> {
                if (pool != RecipeMaps.arcFurnaceRecipes || u.electrode == null) return null;
                ArcFurnaceElectrode electrode = u.electrode;
                value.set(
                    electrode == ArcFurnaceElectrode.InfinityElectrode ? Integer.MAX_VALUE : electrode.parallelLimit,
                    electrode.euModifier,
                    1d / electrode.speedModifier);
                value.timeDivisor = electrode.OCSpeedFactor;
                value.powerMultiplier = electrode.OCPowerFactor;
                value.special = text("electrode");
            }
            case INDISTINCT_PROTOTYPE, MODULAR_NEUTRON -> {
                value.set(masterParallel, masterEU, masterTime);
                if (machine == Machine.INDISTINCT_PROTOTYPE) {
                    value.timeDivisor = 1;
                    value.powerMultiplier = 1;
                }
                value.special = text("master");
            }
            case ADVANCED_BLAST -> {
                value.set(8, 0.9, 1d / 2.2);
                value.heatDiscount = 0.95;
                value.upkeep = pyrotheum(10);
            }
            case GODFORGE_SMELTING, GODFORGE_EXOTIC, GODFORGE_PLASMA -> {
                var preset = BottledChaosComplexStructures.godforge(structure);
                if (preset == null) return null;
                value.set(preset.parallel(), preset.eu(), preset.time());
                // This package aggregates multiple native modules, each retaining its own int parallel limit.
                value.parallelLimit = Long.MAX_VALUE;
                value.timeDivisor = preset.overclock();
                value.heat = preset.heat();
                value.plasmaTier = preset.plasmaTier();
                value.multiStepPlasma = preset.multiStepPlasma();
                value.special = text(
                    "godforge_configuration",
                    preset.rings(),
                    BottledChaosComplexStructures.godforgeUpgradeName(preset));
                if (machine == Machine.GODFORGE_SMELTING) value.heatDiscount = preset.heatDiscount();
            }
            case FOUNDRY -> {
                FoundryData foundry = BottledChaosComplexStructures.foundry(structure);
                if (foundry == null) return null;
                value.set(
                    (long) Math.floor(foundry.parallelScaleAdj) * Math.max(1, t),
                    foundry.euEffAdj,
                    1d / foundry.speedModifierAdj);
                value.timeDivisor = foundry.ocFactorBase + foundry.ocFactorAdditive;
                value.maximumRecipeTier = foundry.UIVRecipesEnabled ? -1 : VoltageIndex.UEV;
                int additional = 0;
                if (foundry.hypercoolerPresent) {
                    Materials coolant = contains(fluids, Materials.SuperCoolant.getFluid(1)) ? Materials.SuperCoolant
                        : contains(fluids, Materials.SpaceTime.getMolten(1)) ? Materials.SpaceTime
                            : foundry.allowEternity && contains(fluids, Materials.Eternity.getMolten(1))
                                ? Materials.Eternity
                                : null;
                    if (coolant == null) return reject(pool, "coolant", 0, 1, failure);
                    additional = coolant == Materials.SuperCoolant ? 1 : coolant == Materials.SpaceTime ? 2 : 3;
                    value.upkeep = new FluidStack[] { coolant == Materials.SuperCoolant ? coolant.getFluid(100)
                        : coolant.getMolten(coolant == Materials.SpaceTime ? 50 : 25) };
                }
                value.maximumOverclocks = Math.max(0, t - recipeTier + additional);
                value.special = text(
                    "foundry_configuration",
                    shape + 1,
                    BottledChaosComplexStructures.foundryUpgradeName(structure));
                if (foundry.isHRPairPresent) value.special = append(value.special, text("foundry_random"));
            }
            case HEARTH -> {
                value.parallel = 512;
                value.heatDiscount = 0.95;
                value.upkeep = pyrotheum(500);
            }
            case SWELEGFYR -> {
                value.set(shape > 1 ? 256 : 4096, 1, 1d / 3.9);
                value.heat = shape > 1 ? (long) (Math.floor(Math.pow(value.heat, 1.08) / 100) * 100 + 1) : value.heat;
                value.heatDiscount = 0.9;
                value.upkeep = pyrotheum(shape > 1 ? (int) Math.min(Integer.MAX_VALUE, value.heat / 5) : 1000);
                value.special = StatCollector.translateToLocalFormatted("GT5U.tooltip.fluid.temperature", value.heat);
            }
            case CHEMICAL_PLANT -> {
                if (u.fluidPipe < 1 || u.machineCasing < 0 || u.solidCasing < 0 || c < 0) return null;
                value.set(2L * u.fluidPipe, 1, 2d / (1 + c));
                value.casingTier = u.solidCasing;
                value.maximumRecipeTier = u.machineCasing < 9 ? u.machineCasing : -1;
            }
            case MULTI_FURNACE -> {
                if (c < 0) return null;
                value.parallel = Math.min(Integer.MAX_VALUE, 4L * powerOfTwo(u.coil.ordinal() - 1));
            }
            case HEPHAESTUS -> {
                if (pool != RecipeMaps.furnaceRecipes) return null;
                value.parallel = Integer.MAX_VALUE;
                value.timeDivisor = 4;
            }
            case CENTRIFUGE -> value.set(8L * t, 0.9, 1d / 3);
            case SPINMATRON -> {
                if (u.rotorScore <= 0) return null;
                boolean boost = contains(fluids, Materials.BiocatalyzedPropulsionFluid.getFluid(1));
                value.heavyCentrifuge = shape >= 3 && boost;
                if (heavyCentrifuge && !value.heavyCentrifuge) return reject(pool, "heavy_centrifuge", 0, 1, failure);
                value.set(
                    Math.max(1, (long) Math.floor(4 * u.rotorScore * (boost ? 1.25 : 1) / (heavyCentrifuge ? 32 : 1))),
                    heavyCentrifuge ? 11.2 : 0.7,
                    !heavyCentrifuge && t - recipeTier >= 3 ? 0.25 : 1d / 3);
                value.maximumOverclocks = Math.max(0, t - recipeTier + 1);
                int amount = Math.max(1, recipeTier) * 10;
                value.upkeep = new FluidStack[] { boost ? Materials.BiocatalyzedPropulsionFluid.getFluid(amount)
                    : new FluidStack(GTPPFluids.Kerosene, amount) };
                value.startupReserveSeconds = 5;
                value.restrictions = text("rotor");
            }
            case PURIFICATION_CLARIFIER, PURIFICATION_DEGASSER, PURIFICATION_FLOCCULATION, PURIFICATION_OZONATION, PURIFICATION_PH, PURIFICATION_PLASMA, PURIFICATION_BARYONIC, PURIFICATION_UV -> {
                value.timeDivisor = 4;
                value.powerMultiplier = 4;
                value.special = text("purification");
            }
            case QUANTUM -> {
                long catalyst = count(items, requiredSpecialInput);
                if (requiredSpecialInput == null || catalyst <= 0)
                    return reject(pool, "catalyst", catalyst, 1, failure);
                if (u.quantumTier < 1) return reject(pool, "casing", u.quantumTier, 1, failure);
                value.parallel = Math.min(Integer.MAX_VALUE, catalyst);
                value.casingTier = u.quantumTier;
                value.restrictions = text("catalyst");
            }
            case DE_FUSION -> {
                value.casingTier = u.draconicTier;
                value.heatOverclocks = Math.max(0, u.draconicTier - 1);
            }
            case NAQUADAH_REFINERY -> {
                if (u.refineryCoil < 1) return reject(pool, "casing", u.refineryCoil, 1, failure);
                value.parallel = 4L * u.refineryCoil;
                value.casingTier = u.refineryCoil;
                value.timeDivisor = 4;
                value.powerMultiplier = 4;
            }
            case FRIDGE -> {
                value.set(256, 1, 2d / 3);
                if (u.fridgeTier > 1) {
                    value.heatOverclocks = 3;
                    value.upkeep = new FluidStack[] { Materials.Eternity.getMolten(20) };
                }
            }
            case NEUTRON -> {
                if (shape < 4 || shape > 249) return null;
                value.timeMultiplier = Math.pow(0.9, shape - 4);
                value.minimumSourceDuration = 1;
                value.timeDivisor = 1;
                value.powerMultiplier = 1;
                value.maximumOverclocks = 0;
                value.special = text("neutron");
            }
        }
        // Only source machines whose glass limits voltage inherit a recipe voltage cap.
        switch (machine) {
            case MEGA_CHEMICAL, MEGA_OIL_CRACKER, HEARTH, LARGE_FLUID_EXTRACTOR, SOLIDIFIER -> value.maximumRecipeTier = u.glass;
            case BALL_LIGHTNING, PHYSICAL_SWITCHER, MEGA_MACERATOR, SWELEGFYR, MOLECULE, CRYSTALLINE -> value.maximumRecipeTier = u.glass
                < VoltageIndex.UMV ? u.glass : -1;
            default -> {}
        }
        if (value.heatDiscount > 0) {
            value.requiresHeat = true;
            var godforge = BottledChaosComplexStructures.godforge(structure);
            long excess = Math.max((godforge == null ? value.heat : godforge.overclockHeat()) - requiredHeat, 0);
            value.euMultiplier *= Math.pow(value.heatDiscount, excess / 900);
            value.heatOverclocks = (int) Math.min(Integer.MAX_VALUE, excess / 1800);
        }
        // Swelegfyr's operating heat affects discounts and OC, but does not raise its recipe admission limit.
        if (machine == Machine.SWELEGFYR) value.heat = structure.heat() + 100L * Math.max(t - 2, 0);
        if (value.requiresHeat && (value.heat <= 0 || requiredHeat > value.heat))
            return reject(pool, "heat", value.heat, Math.max(1, requiredHeat), failure);
        if (requiredGlassTier > value.glassTier)
            return reject(pool, "glass", value.glassTier, requiredGlassTier, failure);
        if (value.maximumRecipeTier >= 0 && recipeTier > value.maximumRecipeTier)
            return reject(pool, "voltage", GTValues.V[value.maximumRecipeTier], recipeVoltage, failure);
        return value.profile();
    }

    private static Profile reject(RecipeMap<?> pool, String parameter, long available, long required,
        Consumer<CheckRecipeResult> failure) {
        failure.accept(BottledChaosPoolFailure.parameter(pool.unlocalizedName, parameter, available, required));
        return null;
    }

    /** Cross-upgrade requirements shared by packaging, saved modules and NEI examples. */
    static boolean isValidStructure(ItemStack controller, ItemStack[] materials) {
        Machine machine = Machine.find(ItemMachines.getMetaTileEntity(controller));
        if (machine != Machine.BALL_LIGHTNING && machine != Machine.PRECISE_ASSEMBLER
            && machine != Machine.LASER_ENGRAVER
            && machine != Machine.DE_FUSION) return true;
        Upgrades u = new Upgrades(materials);
        return switch (machine) {
            case BALL_LIGHTNING -> u.coil.getTier() >= HeatingCoilLevel.UMV.getTier();
            case PRECISE_ASSEMBLER -> u.precision >= -1 && u.machineCasing >= 0 && u.glass >= VoltageIndex.EV;
            case LASER_ENGRAVER -> u.laserTier >= 0 && u.glass >= u.laserTier;
            case DE_FUSION -> u.draconicTier >= 1 && (u.draconicTier <= 3 || u.draconicFusionTier >= 2);
            default -> true;
        };
    }

    private static boolean manufacturingPool(RecipeMap<?> pool) {
        return pool == RecipeMaps.autoclaveRecipes || pool == RecipeMaps.extractorRecipes
            || pool == RecipeMaps.fermentingRecipes
            || pool == RecipeMaps.fluidExtractionRecipes
            || pool == RecipeMaps.fluidSolidifierRecipes
            || pool == RecipeMaps.laserEngraverRecipes;
    }

    private static long powerOfTwo(long power) {
        return power >= 31 ? Integer.MAX_VALUE : power < 0 ? 0 : 1L << (int) power;
    }

    private static long count(ItemStack[] items, ItemStack required) {
        if (required == null) return 0;
        long count = 0;
        for (ItemStack item : items) if (item != null && item.stackSize > 0 && GTUtility.areStacksEqual(item, required))
            count = Math.min(Integer.MAX_VALUE, count + item.stackSize);
        return count;
    }

    private static boolean contains(FluidStack[] fluids, FluidStack required) {
        if (required == null) return false;
        for (FluidStack fluid : fluids)
            if (fluid != null && fluid.amount > 0 && fluid.isFluidEqual(required)) return true;
        return false;
    }

    private static FluidStack[] pyrotheum(int amount) {
        return new FluidStack[] { new FluidStack(TFFluids.fluidPyrotheum, amount) };
    }

    private static FluidStack[] cryotheum(int amount) {
        return new FluidStack[] { new FluidStack(TFFluids.fluidCryotheum, amount) };
    }

    private static final class Values {

        long parallel = 1;
        long parallelLimit = Integer.MAX_VALUE;
        double euMultiplier = 1;
        double timeMultiplier = 1;
        double timeDivisor = 2;
        double powerMultiplier = 4;
        int maximumRecipeTier = -1;
        long heat;
        int glassTier;
        FluidStack[] upkeep = new FluidStack[0];
        String restrictions = "";
        String special = "";
        int maximumOverclocks = -1;
        int heatOverclocks;
        int casingTier = -1;
        int compressionTier = -1;
        double minimumSourceDuration;
        boolean heavyCentrifuge;
        int plasmaTier;
        boolean multiStepPlasma;
        int startupReserveSeconds = 1;
        double heatDiscount;
        boolean requiresHeat;

        void set(long parallel, double eu, double time) {
            this.parallel = parallel;
            euMultiplier = eu;
            timeMultiplier = time;
        }

        Profile profile() {
            if (parallel <= 0 || !Double.isFinite(euMultiplier)
                || euMultiplier <= 0
                || !Double.isFinite(timeMultiplier)
                || timeMultiplier <= 0) return null;
            return new Profile(
                Math.min(parallelLimit, parallel),
                euMultiplier,
                timeMultiplier,
                timeDivisor,
                powerMultiplier,
                maximumRecipeTier,
                heat,
                glassTier,
                upkeep,
                restrictions,
                special,
                maximumOverclocks,
                heatOverclocks,
                casingTier,
                compressionTier,
                minimumSourceDuration,
                heavyCentrifuge,
                plasmaTier,
                multiStepPlasma,
                startupReserveSeconds);
        }
    }

    private static final class Upgrades {

        HeatingCoilLevel coil = HeatingCoilLevel.None;
        int glass;
        int itemPipe = -1;
        int fluidPipe = -1;
        int solenoid = -1;
        int field = -1;
        int ballField = -1;
        int fusion = -1;
        int core = -1;
        int component = -1;
        int precision = -2;
        int maceratorTier = -1;
        int machineCasing = -1;
        int solidCasing = -1;
        int cokeTier = -1;
        int fridgeTier = -1;
        int laserTier = -1;
        int laserParallel;
        int quantumTier = -1;
        int focusingTier = -1;
        int draconicTier;
        int draconicFusionTier;
        int refineryCoil;
        int magnet = -1;
        long photonSpeed;
        boolean photonPerfect;
        boolean basicAlloyCoil;
        boolean ballChip;
        boolean gravityLens;
        double rotorScore;
        ArcFurnaceElectrode electrode;

        Upgrades(ItemStack[] materials) {
            for (ItemStack stack : materials) {
                if (stack == null || stack.stackSize <= 0) continue;
                Block block = Block.getBlockFromItem(stack.getItem());
                int meta = stack.getItemDamage();
                if (block instanceof IHeatingCoil heating) coil = heating.getCoilHeat(meta);
                Integer glassTier = GlassTier.getGlassBlockTier(block, meta);
                if (glassTier != null) glass = Math.max(glass, glassTier);
                if (block == GregTechAPI.sBlockCasings11 && meta >= 0 && meta < 8) itemPipe = meta + 1;
                if (block == GregTechAPI.sBlockCasings2 && meta >= 12 && meta <= 15) fluidPipe = meta - 11;
                if (block == GregTechAPI.sBlockCasings1 && meta >= 0 && meta <= 9) machineCasing = meta;
                if (block == ModBlocks.blockCustomMachineCasings && meta >= 0 && meta <= 3)
                    solidCasing = new int[] { 0, 2, 6, 7 }[meta];
                if (block == GregTechAPI.sBlockCasings2 && meta == 0) solidCasing = 1;
                if (block == GregTechAPI.sBlockCasings4 && meta >= 0 && meta <= 2)
                    solidCasing = new int[] { 5, 3, 4 }[meta];
                if (block instanceof BlockCyclotronCoils solenoidCoil) solenoid = solenoidCoil.getVoltageTier(meta);
                Integer fieldTier = GTCM_CrystallineInfinitier.getBlockFieldGeneratorTier(block, meta);
                if (fieldTier != null) field = Math.max(field, fieldTier);
                if (block == TTCasingsContainer.sBlockCasingsTT && (meta == 8 || meta == 9)) ballField = meta - 7;
                if (block == TTCasingsContainer.StabilisationFieldGenerators && meta >= 0 && meta <= 8)
                    ballField = meta + 3;
                Integer fusionTier = TST_BallLightning.getcompactFusionCoilTier(block, meta);
                if (fusionTier != null) fusion = fusionTier;
                if (block instanceof BlockMultiUseCore) core = meta;
                if (block == Loaders.componentAssemblylineCasing) component = meta;
                Integer precisionTier = MTEPreciseAssembler.getCasingBlockTier(block, meta);
                if (precisionTier != null) precision = Math.max(precision, precisionTier);
                Integer blockTier = TST_MegaMacerator.getTierOfBlock(block, meta);
                if (blockTier != null) maceratorTier = Math.max(maceratorTier, blockTier);
                if (block == TstBlocks.PhotonControllerUpgrade && meta >= 0
                    && meta < PhotonControllerUpgradeCasing.speedIncrement.length) {
                    photonSpeed += (long) PhotonControllerUpgradeCasing.speedIncrement[meta] * stack.stackSize;
                    photonPerfect |= meta >= 11;
                }
                if (block == gtPlusPlus.core.block.ModBlocks.blockCasings5Misc && meta >= 7 && meta <= 10)
                    quantumTier = meta - 6;
                if (block == ModBlocks.blockCasings5Misc && meta >= 11 && meta <= 14) focusingTier = meta - 10;
                if (block == BlockLoader.defcCasingBlock && meta >= 8 && meta <= 12) draconicTier = meta - 7;
                if (block == GregTechAPI.sBlockCasings4 && (meta == 6 || meta == 8))
                    draconicFusionTier = meta == 6 ? 1 : 2;
                Integer refineryTier = MTENaquadahFuelRefinery.fieldCoilTierConverter()
                    .convert(block, meta);
                if (refineryTier != null) refineryCoil = refineryTier;
                if (block == ModBlocks.blockCasingsMisc && (meta == 2 || meta == 3)) cokeTier = meta - 2;
                if (block == ModBlocks.blockCasingsMisc && meta == 14) basicAlloyCoil = true;
                if (block == GregTechAPI.sBlockCasings2 && meta == 1) fridgeTier = Math.max(fridgeTier, 1);
                if (block == GregTechAPI.sBlockCasings8 && meta == 14) fridgeTier = 2;
                if (MTEIndustrialElectromagneticSeparator.isValidElectromagnet(stack)) magnet = meta - 32345;
                ArcFurnaceElectrode found = ArcFurnaceElectrode.getByStack(stack);
                if (found != null) electrode = found;
                if (GTCMItemList.BallLightningUpgradeChip.equal(stack)) ballChip = true;
                if (GTCMItemList.GravitationalLens.equal(stack)) gravityLens = true;
                IMetaTileEntity hatch = stack.getItem() instanceof ItemMachines ? ItemMachines.getMetaTileEntity(stack)
                    : null;
                if (hatch instanceof MTEHatchDynamoTunnel laser) {
                    laserTier = Math.max(laserTier, (int) laser.getOutputTier());
                    laserParallel = Math.max(laserParallel, (int) Math.cbrt(laser.maxAmperesOut()));
                }
                if (stack.getItem() instanceof MetaGeneratedTool tool) {
                    IToolStats stats = tool.getToolStats(stack);
                    int harvest = tool.getHarvestLevel(stack, "test");
                    double factor = stats instanceof ToolTurbineHuge ? 1
                        : stats instanceof ToolTurbineLarge ? 0.75
                            : stats instanceof ToolTurbineNormal ? 0.5 : stats instanceof ToolTurbineSmall ? 0.25 : 0;
                    rotorScore += Math.floor(harvest * factor) * stack.stackSize;
                }
            }
        }
    }

    public static List<String> describe(ItemStack controller, Structure structure, RecipeMap<?> pool,
        int quantitySpeedExponent, boolean compact) {
        List<String> lines = new ArrayList<>();
        if (controller == null || pool == null) return lines;
        controller = BottledChaosComplexStructures.parameterController(controller, structure);
        IMetaTileEntity original = controller.getItem() instanceof ItemMachines
            ? ItemMachines.getMetaTileEntity(controller)
            : null;
        Machine machine = original == null ? null : Machine.find(original);
        Profile base = evaluate(
            controller,
            structure,
            pool,
            1,
            32,
            32,
            new ItemStack[0],
            new FluidStack[0],
            1,
            1,
            1,
            0);
        double quantitySpeed = 1d + Math.max(0, quantitySpeedExponent);
        String parallel = base == null ? "-" : number(base.parallel());
        String eu = base == null ? "-" : number(base.euMultiplier());
        String time = base == null ? "-" : number(base.timeMultiplier() / quantitySpeed);
        String oc = base == null ? "-" : clock(base.powerMultiplier(), base.timeDivisor());
        String restrictions = base == null
            ? StatCollector.translateToLocal("GT5U.gui.multimachine.incomplete_structure")
            : base.restrictions();
        String special = base == null ? "" : base.special();
        Upgrades u = new Upgrades(structure == null ? new ItemStack[0] : structure.materials());
        int shape = structure == null ? 1 : structure.profile();
        boolean recipeHeat = BottledChaosStructureCatalog.requiresRecipeHeat(pool);
        boolean variableTime = false;
        if (machine != null) switch (machine) {
            case AUTO_CRAFTER -> parallel = "2 x max(1, T)";
            case BREWERY, CHEMICAL_BATH, MAGNETAR_SEPARATOR, ELECTROLYZER, SIFTER -> parallel = "4T";
            case FLUID_HEATER, MIXER -> parallel = "8T";
            case BENDING -> parallel = "6T";
            case VACUUM_FILTER -> {
                time = "1/(1 + P)";
                variableTime = true;
            }
            case PHYSICAL_SWITCHER, MOLECULE -> {
                time = "0.9^P";
                variableTime = true;
            }
            case MAGNETIC_MIXER, SCAVENGER -> {
                time = "0.8^P";
                variableTime = true;
            }
            case ALLOY_BLAST -> restrictions = text("input_voltage_limit");
            case MEGA_ALLOY -> {
                parallel = "256";
                if (u.coil.getTier() >= 0) {
                    eu = "0.95^max(" + (u.coil.getTier() + 1) + " - " + text("recipe_tier") + ", 0)";
                    time = number((1 - 0.05 * Math.max(u.coil.getTier() - 3, 0)) / quantitySpeed);
                } else if (u.basicAlloyCoil) {
                    eu = "1";
                    time = number(1d / quantitySpeed);
                }
                oc = clock(4, 2);
                restrictions = base == null && structure != null ? tierName(u.glass) : "";
            }
            case INDISTINCT -> oc = text("conditional_oc", tierName(u.component));
            case CRYSTALLINE -> parallel = ((long) u.glass * u.field) + "P";
            case MANUFACTURING -> parallel = "2T";
            case BLAST_FURNACE -> {
                eu = text("thermal_eu", "0.95");
                special = text("voltage_heat", structure == null ? 0 : structure.heat());
            }
            case BIOSPHERE -> {
                if (pool != RecipeMaps.brewingRecipes && pool != RecipeMaps.fermentingRecipes) {
                    parallel = text("dish_count");
                    eu = "1";
                    time = text("culture_time");
                    variableTime = true;
                    oc = clock(4, 2);
                    restrictions = text("culture");
                    special = text("automatic_culture");
                }
            }
            case ADVANCED_DISTILLATION -> {
                if (pool == RecipeMaps.distilleryRecipes) parallel = (2L * (shape / 3)) + "T";
            }
            case MAGNETIC_DOMAIN -> {
                time = "0.75^P";
                variableTime = true;
            }
            case FORGE_HAMMER -> parallel = (6L * Math.max(1, u.solenoid)) + "T";
            case LASER_ENGRAVER -> {
                restrictions = text("laser_glass", tierName(u.glass), tierName(u.laserTier));
            }
            case MACERATOR -> parallel = (shape > 1 ? "8" : "2") + "T";
            case MEGA_MACERATOR -> time = u.glass >= VoltageIndex.UMV ? number(0.125 / quantitySpeed)
                : text("macerator_time", tierName(u.glass), number(0.125 / quantitySpeed), number(1d / quantitySpeed));
            case GIANT_DRYER -> time = number(2d / ((u.coil.getTier() + 1) * quantitySpeed)) + " x 0.8^P";
            case DEHYDRATOR -> {
                if (recipeHeat) eu = "0.5 x " + text("thermal_eu", "0.95");
            }
            case THERMAL_CENTRIFUGE -> parallel = u.solenoid <= 0 ? "8T" : "8T + " + (2 * u.solenoid);
            case THERMAL_DEVOURER -> time = number(0.5 / quantitySpeed) + "/(1 + P)";
            case SOLIDIFIER -> parallel = "10T";
            case INDISTINCT_PROTOTYPE, MODULAR_NEUTRON -> {
                parallel = text("master_parallel");
                eu = text("master_eu");
                time = text("master_time");
                variableTime = true;
                special = text("master");
            }
            case ADVANCED_BLAST -> {
                if (recipeHeat) eu = "0.9 x " + text("thermal_eu", "0.95");
            }
            case GODFORGE_SMELTING -> {
                var preset = BottledChaosComplexStructures.godforge(structure);
                if (preset != null && recipeHeat) eu = (preset.eu() == 1 ? "" : number(preset.eu()) + " x ")
                    + text("thermal_eu", number(preset.heatDiscount()));
            }
            case FOUNDRY -> {
                FoundryData foundry = BottledChaosComplexStructures.foundry(structure);
                if (foundry != null) {
                    parallel = number(Math.floor(foundry.parallelScaleAdj)) + "T";
                    eu = number(foundry.euEffAdj);
                    time = number(1d / (foundry.speedModifierAdj * quantitySpeed));
                    oc = clock(4, foundry.ocFactorBase + foundry.ocFactorAdditive);
                    restrictions = base == null && !foundry.UIVRecipesEnabled ? tierName(VoltageIndex.UEV) : "";
                    special = text(
                        "foundry_configuration",
                        shape + 1,
                        BottledChaosComplexStructures.foundryUpgradeName(structure));
                    if (foundry.hypercoolerPresent) special = append(
                        special,
                        text("foundry_coolant", foundry.allowEternity ? " / " + text("foundry_eternity") : ""));
                    if (foundry.isHRPairPresent) special = append(special, text("foundry_random"));
                }
            }
            case HEARTH -> {
                if (recipeHeat) eu = text("thermal_eu", "0.95");
            }
            case SWELEGFYR -> {
                if (recipeHeat) eu = text("thermal_eu", "0.9");
            }
            case CENTRIFUGE -> parallel = "8T";
            case SPINMATRON -> {
                parallel = text(
                    "rotor_parallel",
                    number(Math.min(Integer.MAX_VALUE, Math.max(1, Math.floor(4 * u.rotorScore)))),
                    number(Math.min(Integer.MAX_VALUE, Math.max(1, Math.floor(5 * u.rotorScore)))));
                if (shape >= 3) parallel = append(
                    parallel,
                    text(
                        "rotor_heavy",
                        number(Math.min(Integer.MAX_VALUE, Math.max(1, Math.floor(5 * u.rotorScore / 32))))));
                eu = shape >= 3 ? text("rotor_eu") : "0.7";
                time = text("rotor_time", number(0.25 / quantitySpeed), number(1d / (3 * quantitySpeed)));
                oc = clock(4, 2);
                restrictions = shape >= 3 ? text("rotor_limits") : "";
                special = text("rotor") + "; " + text("rotor_upkeep") + "; " + text("rotor_oc");
            }
            case QUANTUM -> {
                parallel = text("catalyst_count");
                eu = "1";
                time = number(1d / quantitySpeed);
                oc = clock(4, 2);
                restrictions = text("catalyst");
            }
            case NEUTRON -> {
                parallel = "1";
                eu = "1";
                time = structure == null ? "0.9^(L - 4)" : number(Math.pow(0.9, shape - 4) / quantitySpeed);
                variableTime = structure == null;
                oc = clock(1, 1);
                special = text("neutron");
                restrictions = text("neutron_layers", shape);
            }
            case ARC_FURNACE -> {
                if (special.isEmpty()) special = text("electrode");
            }
            default -> {}
        }
        // #tr tst.bottledchaos.machine_module.parallel
        // # Parallel: %s
        // #zh_CN 并行: %s
        lines.add(EnumChatFormatting.GRAY + text("parallel", EnumChatFormatting.GOLD + parallel));
        // #tr tst.bottledchaos.machine_module.eu
        // # Power multiplier: x%s
        // #zh_CN 耗电倍率: x%s
        lines.add(EnumChatFormatting.GRAY + text("eu", EnumChatFormatting.YELLOW + eu));
        // #tr tst.bottledchaos.machine_module.time
        // # Duration multiplier: x%s
        // #zh_CN 耗时倍率: x%s
        if (variableTime && quantitySpeed > 1) time = "(" + time + ")/" + number(quantitySpeed);
        lines.add(EnumChatFormatting.GRAY + text("time", EnumChatFormatting.GREEN + time));
        // #tr tst.bottledchaos.machine_module.oc
        // # Overclock: %s
        // #zh_CN 超频系数: %s
        lines.add(EnumChatFormatting.GRAY + text("oc", EnumChatFormatting.AQUA + oc));
        int voltageLimit = base != null && machine != Machine.ALLOY_BLAST ? base.maximumRecipeTier() : -1;
        if (voltageLimit >= 0)
            restrictions = append(restrictions, text("recipe_voltage_limit", tierName(voltageLimit)));
        if (base != null && base.casingTier() >= 0)
            restrictions = append(restrictions, text("casing_limit", base.casingTier()));
        if (base != null && base.compressionTier() >= 0)
            restrictions = append(restrictions, text("compression_limit", base.compressionTier()));
        if (recipeHeat && base != null && base.heat() > 0) restrictions = append(
            restrictions,
            text(
                "heat_limit",
                machine == Machine.BLAST_FURNACE || machine == Machine.SWELEGFYR
                    ? (structure == null ? 0 : structure.heat()) + " + 100 x max(T - 2, 0)"
                    : base.heat()));
        // #tr tst.bottledchaos.machine_module.limits
        // # Recipe limits: %s
        // #zh_CN 配方限制: %s
        if (!compact || !restrictions.isEmpty()) lines.add(
            EnumChatFormatting.GRAY + text(
                "limits",
                EnumChatFormatting.YELLOW
                    + (restrictions.isEmpty() ? StatCollector.translateToLocal("GT5U.gui.text.nil") : restrictions)));
        if (!compact && recipeHeat
            && base != null
            && base.heat() > 0
            && machine != Machine.BLAST_FURNACE
            && machine != Machine.SWELEGFYR)
            special = append(
                special,
                StatCollector.translateToLocalFormatted("GT5U.tooltip.fluid.temperature", base.heat()));
        if (machine == Machine.BIO_VAT || machine == Machine.BIOSPHERE && pool != RecipeMaps.brewingRecipes
            && pool != RecipeMaps.fermentingRecipes) special = append(special, text("radiation"));
        if (recipeHeat && (machine == Machine.DEHYDRATOR || machine == Machine.ADVANCED_BLAST
            || machine == Machine.GODFORGE_SMELTING
            || machine == Machine.HEARTH
            || machine == Machine.SWELEGFYR)) special = append(special, text("thermal_oc"));
        if (base != null && base.upkeep().length > 0 && machine != Machine.SPINMATRON)
            for (FluidStack fluid : base.upkeep())
                special += (special.isEmpty() ? "" : "; ") + text("upkeep", fluid.getLocalizedName(), fluid.amount);
        if (base != null && base.heatOverclocks() > 0 && machine == Machine.FRIDGE)
            special += "; " + text("perfect_stages", base.heatOverclocks());
        if (base != null && base.heatOverclocks() > 0 && machine == Machine.DE_FUSION)
            special = append(special, text("perfect_stages", base.heatOverclocks()));
        if (machine == Machine.QUANTUM && u.quantumTier >= 1)
            special = append(special, text("quantum_tiers", u.quantumTier, u.focusingTier));
        if (base != null && base.maximumOverclocks() >= 0
            && machine != Machine.FOUNDRY
            && machine != Machine.NEUTRON
            && machine != Machine.SPINMATRON
            && machine != Machine.LASER_ENGRAVER) special = append(special, text("oc_limit", base.maximumOverclocks()));
        if (machine == Machine.LASER_ENGRAVER)
            special = append(special, text("laser_oc_limit", tierName(u.laserTier + 1)));
        if (parallel.contains("T") || parallel.contains("P")
            || time.contains("P")
            || machine == Machine.BLAST_FURNACE
            || machine == Machine.SWELEGFYR) special = append(special, text("host_tiers"));
        if (!special.isEmpty()) {
            // #tr tst.bottledchaos.machine_module.special
            // # Special: %s
            // #zh_CN 特殊: %s
            lines.add(EnumChatFormatting.GRAY + text("special", EnumChatFormatting.WHITE + special));
        }
        return lines;
    }

    private static String append(String value, String addition) {
        return value.isEmpty() ? addition : value + "; " + addition;
    }

    private static String number(double value) {
        if (value == Integer.MAX_VALUE) return "int";
        if (value >= Long.MAX_VALUE) return "long";
        return value == Math.rint(value) ? Long.toString((long) value)
            : String.format(Locale.ROOT, "%.6g", value)
                .replaceFirst("(\\.\\d*?)0+(?=e|$)", "$1")
                .replaceFirst("\\.(?=e|$)", "");
    }

    private static String clock(double power, double divisor) {
        // #tr tst.bottledchaos.machine_module.clock
        // # EU/t x%s, duration /%s
        // #zh_CN EU/t x%s, 耗时/%s
        return text("clock", number(power), number(divisor));
    }

    private static String tierName(int tier) {
        return tier >= 0 && tier < GTValues.VN.length ? GTValues.VN[tier] : "-";
    }

    // #tr tst.bottledchaos.machine_module.godforge_configuration
    // # Rings: %s; upgrade configuration: %s; bonuses included
    // #zh_CN 环数: %s; 升级配置: %s; 包含增益

    // #tr tst.bottledchaos.machine_module.foundry_configuration
    // # Modules: %s; upgrade configuration: %s; bonuses included
    // #zh_CN 模块数: %s; 升级配置: %s; 包含增益

    // #tr tst.bottledchaos.machine_module.foundry_random
    // # Random bonuses use expected values
    // #zh_CN 随机增益取期望值

    // #tr tst.bottledchaos.machine_module.foundry_coolant
    // # Coolant: Super Coolant 100 L/s / Space-Time 50 L/s%s
    // #zh_CN 冷却液: 超级冷却液100 L/s / 时空50 L/s%s

    // #tr tst.bottledchaos.machine_module.foundry_eternity
    // # Eternity 25 L/s
    // #zh_CN 永恒25 L/s

    // #tr tst.bottledchaos.machine_module.recipe_tier
    // # recipe tier
    // #zh_CN 配方等级

    // #tr tst.bottledchaos.machine_module.conditional_oc
    // # EU/t x4; recipes up to %s: duration /4; others: /2
    // #zh_CN EU/t x4; 配方<=%s: 耗时/4; 其他: /2

    // #tr tst.bottledchaos.machine_module.culture
    // # Matching culture and sufficient glass tier are required
    // #zh_CN 需要匹配的培养物及足够的玻璃等级

    // #tr tst.bottledchaos.machine_module.automatic_culture
    // # Automatic culture mode
    // #zh_CN 自动培养模式

    // #tr tst.bottledchaos.machine_module.radiation
    // # Packaged radiation hatch is adjustable to the source recipe requirement
    // #zh_CN 已封装辐射仓可调节至原配方所需辐射

    // #tr tst.bottledchaos.machine_module.dish_count
    // # Matching culture dish count
    // #zh_CN 匹配培养皿数量

    // #tr tst.bottledchaos.machine_module.culture_time
    // # 1/[1.6 + 0.6 x (glass tier - required glass tier)]
    // #zh_CN 1/[1.6 + 0.6 x (玻璃等级 - 所需玻璃等级)]

    // #tr tst.bottledchaos.machine_module.electrode
    // # Packaged electrode; no startup or durability consumption
    // #zh_CN 已封装电极; 无启动或耐久消耗

    // #tr tst.bottledchaos.machine_module.master
    // # Module duration and power bonuses stack with the main controller's bonuses
    // #zh_CN 模组耗时和耗电增益与主机增益叠加

    // #tr tst.bottledchaos.machine_module.master_parallel
    // # Main controller parallel limit
    // #zh_CN 主机并行上限

    // #tr tst.bottledchaos.machine_module.master_eu
    // # Main controller power multiplier
    // #zh_CN 主机耗电倍率

    // #tr tst.bottledchaos.machine_module.master_time
    // # Main controller duration multiplier
    // #zh_CN 主机耗时倍率

    // #tr tst.bottledchaos.machine_module.thermal_eu
    // # %s^floor(excess heat/900K)
    // #zh_CN %s^floor(余热/900K)

    // #tr tst.bottledchaos.machine_module.voltage_heat
    // # Coil heat %s K + 100 x max(T - 2, 0) K; one perfect OC per 1800 K excess heat
    // #zh_CN 线圈炉温%s K + 100 x max(T - 2, 0) K; 每1800 K余热增加一档无损超频

    // #tr tst.bottledchaos.machine_module.thermal_oc
    // # Excess heat = temperature - required heat; one perfect OC per 1800 K excess heat
    // #zh_CN 余热 = 炉温 - 配方所需热量; 每1800 K余热增加一档无损超频

    // #tr tst.bottledchaos.machine_module.heat_limit
    // # Maximum required recipe heat: %s K
    // #zh_CN 最高配方所需热量: %s K

    // #tr tst.bottledchaos.machine_module.casing_limit
    // # Maximum required casing tier: %s
    // #zh_CN 最高配方所需机壳等级: %s

    // #tr tst.bottledchaos.machine_module.compression_limit
    // # Maximum required compression tier: %s
    // #zh_CN 最高配方所需压缩等级: %s

    // #tr tst.bottledchaos.machine_module.recipe_voltage_limit
    // # Maximum recipe voltage tier: %s
    // #zh_CN 最高配方电压等级: %s

    // #tr tst.bottledchaos.machine_module.input_voltage_limit
    // # Main controller input voltage
    // #zh_CN 主机输入电压

    // #tr tst.bottledchaos.machine_module.host_tiers
    // # T = main controller voltage tier; P = main controller available power tier
    // #zh_CN T = 主机电压等级; P = 主机可用功率等级

    // #tr tst.bottledchaos.machine_module.oc_limit
    // # Maximum source overclocks: %s
    // #zh_CN 原配方最大超频次数: %s

    // #tr tst.bottledchaos.machine_module.laser_oc_limit
    // # Maximum source tier after overclocking: %s
    // #zh_CN 原配方超频后最高等级: %s

    // #tr tst.bottledchaos.machine_module.laser_glass
    // # Glass: %s; laser source: %s
    // #zh_CN 玻璃: %s; 激光源: %s

    // #tr tst.bottledchaos.machine_module.macerator_time
    // # Recipes below %s: %s; others: %s
    // #zh_CN 配方<%s: %s; 其他: %s

    // #tr tst.bottledchaos.machine_module.catalyst
    // # Matching catalyst and sufficient crafting structure tier are required
    // #zh_CN 需要匹配的催化剂及足够的制造结构等级

    // #tr tst.bottledchaos.machine_module.quantum_tiers
    // # Crafting tier: %s; focusing tier: %s
    // #zh_CN 制造等级: %s; 聚焦等级: %s

    // #tr tst.bottledchaos.machine_module.catalyst_count
    // # Matching catalyst count
    // #zh_CN 匹配催化剂数量

    // #tr tst.bottledchaos.machine_module.rotor
    // # Packaged mixed rotors; protection fluid is requested during operation
    // #zh_CN 已封装混用转子; 运行时请求保护流体

    // #tr tst.bottledchaos.machine_module.rotor_parallel
    // # %s; propulsion fluid: %s
    // #zh_CN %s; 推进液: %s

    // #tr tst.bottledchaos.machine_module.rotor_heavy
    // # Heavy mode: %s
    // #zh_CN 重模式: %s

    // #tr tst.bottledchaos.machine_module.rotor_eu
    // # 0.7; heavy recipes: 11.2
    // #zh_CN 0.7; 重模式配方: 11.2

    // #tr tst.bottledchaos.machine_module.rotor_time
    // # Non-heavy recipes at least 3 tiers below input voltage: %s; others: %s
    // #zh_CN 非重模式配方比输入电压低至少3级: %s; 其他: %s

    // #tr tst.bottledchaos.machine_module.rotor_limits
    // # Heavy mode: T3+ and propulsion fluid
    // #zh_CN 重模式: T3+结构及推进液

    // #tr tst.bottledchaos.machine_module.rotor_oc
    // # One additional source overclock
    // #zh_CN 原配方额外超频: 1

    // #tr tst.bottledchaos.machine_module.rotor_upkeep
    // # Protection fluid: max(1, recipe tier) x 10 L/s; kerosene or supplied propulsion fluid
    // #zh_CN 保护流体: max(1, 配方等级) x 10 L/s; 煤油或输入的推进液

    // #tr tst.bottledchaos.machine_module.purification
    // # Purification unit quantity must not exceed main plant quantity
    // #zh_CN 净化水机器堆叠组数不能超过主净水厂堆叠组数

    // #tr tst.bottledchaos.machine_module.neutron
    // # Source duration is at least 1 tick before quantity speed is applied
    // #zh_CN 数量增速前的原配方耗时最低为1 tick

    // #tr tst.bottledchaos.machine_module.neutron_layers
    // # Layers: %s
    // #zh_CN 层数: %s

    // #tr tst.bottledchaos.machine_module.upkeep
    // # %s: %s L/s per complete structure
    // #zh_CN %s: 每组完整结构%s L/s

    // #tr tst.bottledchaos.machine_module.perfect_stages
    // # %s perfect overclock stages
    // #zh_CN %s档无损超频
    private static String text(String suffix, Object... args) {
        String key = "tst.bottledchaos.machine_module." + suffix;
        return args.length == 0 ? StatCollector.translateToLocal(key)
            : StatCollector.translateToLocalFormatted(key, args);
    }
}
