package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.PhotonControllerUpgradeCasing;
import com.Nxer.TwistSpaceTechnology.common.block.meta.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.GTCM_CrystallineInfinitier;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BallLightning;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaMacerator;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result.BottledChaosPoolFailure;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.github.bsideup.jabel.Desugar;

import bartworks.API.recipe.BartWorksRecipeMaps;
import goodgenerator.blocks.tileEntity.MTEPreciseAssembler;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputer;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputerPP;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GlassTier;
import gregtech.common.blocks.BlockCyclotronCoils;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer;
import gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator;
import gregtech.common.tileentities.machines.multi.MTENaquadahFuelRefinery;
import gregtech.common.tileentities.machines.multi.foundry.FoundryData;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gregtech.nei.formatter.FusionSpecialValueFormatter;
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
        int maximumOverclocks, int perfectOverclocks, int casingTier, int compressionTier, double minimumSourceDuration,
        boolean heavyCentrifuge, int plasmaTier, boolean multiStepPlasma, int startupReserveSeconds,
        long maximumSourcePower, int upkeepPeriodSeconds) {}

    /** One case per machine, with mode-specific pools handled explicitly below. */
    enum Machine {

        // spotless:off
        AUTO_CRAFTER,
        BREWERY,
        CHEMICAL_BATH,
        DISTILLATION_TOWER,
        ALLOY_BLAST,
        VACUUM_FILTER,
        MAGNETAR_SEPARATOR,
        MAGNET_SEPARATOR,
        FLOTATION,
        PHYSICAL_SWITCHER,
        FLUID_HEATER,
        LARGE_CHEMICAL,
        MEGA_CHEMICAL,
        CHEMICAL_DISTORTER,
        BENDING,
        ELECTROLYZER,
        MAGNETIC_MIXER,
        SIFTER,
        SCAVENGER,
        VACUUM_FREEZER,
        DIGESTER,
        DISSOLUTION,
        BIO_VAT,
        MEGA_ALLOY,
        BALL_LIGHTNING,
        INDISTINCT,
        PRECISE_ASSEMBLER,
        AUTOCLAVE,
        CRYSTALLINE,
        MANUFACTURING,
        BLAST_FURNACE,
        PLASMA_FORGE,
        BIOSPHERE,
        OIL_CRACKER,
        MEGA_OIL_CRACKER,
        ADVANCED_OIL_CRACKER,
        ADVANCED_DISTILLATION,
        MEGA_DISTILLATION,
        MAGNETIC_DOMAIN,
        EXTRACTOR,
        SPACE_SCALER,
        LARGE_FLUID_EXTRACTOR,
        HUMAN_POWER,
        FORGE_HAMMER,
        PRESSURE_FORMER,
        COKE_OVEN,
        LASER_ENGRAVER,
        PHOTON_MASTER,
        MACERATOR,
        MEGA_MACERATOR,
        MOLECULE,
        GIANT_DRYER,
        DEHYDRATOR,
        MIXER,
        THERMAL_CENTRIFUGE,
        THERMAL_DEVOURER,
        SOLIDIFIER,
        ISA_MILL,
        CRYOGENIC_FREEZER,
        ARC_FURNACE,
        DE_FUSION,
        INDISTINCT_PROTOTYPE,
        ADVANCED_BLAST,
        GODFORGE_SMELTING,
        GODFORGE_MOLTEN,
        STELLAR_FORGE,
        HEAT_CONVECTOR,
        HEARTH,
        SWELEGFYR,
        CHEMICAL_PLANT,
        GODFORGE_EXOTIC,
        GODFORGE_PLASMA,
        FOUNDRY,
        MULTI_FURNACE,
        HEPHAESTUS,
        CENTRIFUGE,
        SPINMATRON,
        PURIFICATION_CLARIFIER,
        PURIFICATION_DEGASSER,
        PURIFICATION_FLOCCULATION,
        PURIFICATION_OZONATION,
        PURIFICATION_PH,
        PURIFICATION_PLASMA,
        PURIFICATION_BARYONIC,
        PURIFICATION_UV,
        QUANTUM,
        FRIDGE,
        NEUTRON,
        MODULAR_NEUTRON,
        MODULAR_PLASMA_FORGE,
        NAQUADAH_REFINERY,
        FUSION;
        // spotless:on

        static Machine find(IMetaTileEntity controller) {
            return BottledChaosSupportedMachine.parameters(controller);
        }
    }

    /** Parsed once per installed module change; never contains a planning inventory or dynamic profile. */
    static final class InstalledSource {

        final Structure structure;
        final boolean debug;
        final IMetaTileEntity original;
        final Collection<RecipeMap<?>> pools;
        final Machine machine;
        final Upgrades upgrades;
        final BottledChaosComplexStructures.GodforgeProfile godforge;
        final FoundryData foundry;
        final boolean fourHeliocasts;

        InstalledSource(ItemStack controller, Structure structure) {
            this.structure = structure;
            debug = GTCMItemList.TestItem0.equal(controller);
            controller = BottledChaosComplexStructures.parameterController(controller, structure);
            original = controller != null && controller.getItem() instanceof ItemMachines
                ? ItemMachines.getMetaTileEntity(controller)
                : null;
            pools = BottledChaosSupportedMachine.recipeMaps(original);
            machine = original == null ? null : Machine.find(original);
            upgrades = new Upgrades(structure == null ? new ItemStack[0] : structure.materials());
            godforge = BottledChaosComplexStructures.godforge(structure);
            foundry = machine == Machine.FOUNDRY ? BottledChaosComplexStructures.foundry(structure) : null;
            fourHeliocasts = foundry != null && BottledChaosComplexStructures.fourHeliocasts(structure);
        }

        int poolSpeedDivisor() {
            if (original instanceof com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.WirelessEnergyMultiMachineBase
                || godforge != null) return 1;
            return Math.max(
                1,
                (int) pools.stream()
                    .distinct()
                    .count());
        }
    }

    /** References are the caller-owned, unmodified input snapshot for one planning attempt. */
    @Desugar
    public record Context(long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] fluids, double masterTime,
        double masterEU, long masterParallel) {}

    @Desugar
    record Evaluation(Profile profile, CheckRecipeResult failure) {

        static final Evaluation INVALID = new Evaluation(null, null);
    }

    static Profile defaultProfile(boolean perfect) {
        Values values = new Values();
        if (perfect) values.timeDivisor = 4;
        return values.profile();
    }

    static Evaluation evaluate(InstalledSource source, RecipeMap<?> pool, long recipeVoltage, Context context,
        BottledChaosSourceRequirements requirements) {
        long inputVoltage = context.inputVoltage();
        long inputPower = context.inputPower();
        ItemStack[] items = context.items();
        FluidStack[] fluids = context.fluids();
        double masterTime = context.masterTime();
        double masterEU = context.masterEU();
        long masterParallel = context.masterParallel();
        int requiredHeat = requirements.heat();
        int requiredGlassTier = requirements.glassTier();
        ItemStack requiredSpecialInput = requirements.specialInput() == null ? null
            : requirements.specialInput()
                .getItemStack(1);
        boolean heavyCentrifuge = requirements.heavyCentrifuge();
        long requiredSourceVoltage = requirements.requiredSourceVoltage();
        long fusionThreshold = requirements.fusionThreshold();
        if (source == null || pool == null
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
            || requiredHeat < 0) return Evaluation.INVALID;
        if (source.debug) return new Evaluation(new Values().profile(), null);
        IMetaTileEntity original = source.original;
        Machine machine = source.machine;
        Structure structure = source.structure;
        if (!source.pools.contains(pool)) return Evaluation.INVALID;
        Values value = new Values();
        if (machine == null) {
            if (!(original instanceof MTEBasicMachine basic) || basic.mTier <= 0) return Evaluation.INVALID;
            value.maximumRecipeTier = basic.mTier;
            return new Evaluation(value.profile(), null);
        }
        if (structure == null) return Evaluation.INVALID;
        Upgrades u = source.upgrades;
        int shape = structure == null ? 1 : structure.profile();
        int t = GTUtility.getTier(inputVoltage);
        int p = GTUtility.getTier(inputPower);
        int recipeTier = GTUtility.getTier(recipeVoltage);
        int c = u.coil.getTier();
        value.glassTier = u.glass;
        value.heat = structure == null ? 0 : structure.heat();
        switch (machine) {
            case FUSION -> {
                if (pool != RecipeMaps.fusionRecipes) return Evaluation.INVALID;
                int tier;
                boolean advanced;
                if (original instanceof MTELargeFusionComputer fusion) {
                    tier = fusion.tier();
                    value.parallel = (long) fusion.getMaxPara() * fusion.extraPara(fusionThreshold);
                    value.maximumSourcePower = GTValues.V[tier] * fusion.getMaxPara() * fusion.extraPara(100);
                    advanced = fusion instanceof MTELargeFusionComputerPP;
                } else {
                    tier = ((MTEFusionComputer) original).tier();
                    value.maximumSourcePower = GTValues.V[tier];
                    advanced = tier >= VoltageIndex.UHV;
                }
                value.maximumRecipeTier = tier;
                value.powerMultiplier = value.timeDivisor = advanced ? 4 : 2;
                value.maximumOverclocks = Math.max(
                    0,
                    tier - 5 - FusionSpecialValueFormatter.getFusionTier(fusionThreshold, requiredSourceVoltage));
            }
            case AUTO_CRAFTER -> value.set(2L * Math.max(1, t), 1, 1d / 3);
            case BREWERY -> value.set(4L * t, 1, 2d / 3);
            case CHEMICAL_BATH -> value.set(4L * t, 1, 0.2);
            case DISTILLATION_TOWER, DISSOLUTION, VACUUM_FREEZER -> {}
            case PLASMA_FORGE -> value.requiresHeat = true;
            case ALLOY_BLAST -> value.maximumRecipeTier = t;
            case VACUUM_FILTER -> {
                if (pool != RecipeMaps.distillationTowerRecipes && pool != RecipeMaps.distilleryRecipes)
                    return Evaluation.INVALID;
                value.set(Integer.MAX_VALUE, 0.5, 1d / (1 + p));
                if (pool == RecipeMaps.distilleryRecipes) value.timeDivisor = 4;
            }
            case MAGNETAR_SEPARATOR -> value.set(4L * t, 0.8, 0.25);
            case MAGNET_SEPARATOR -> {
                if (u.magnet < 0 || pool != RecipeMaps.electroMagneticSeparatorRecipes) return Evaluation.INVALID;
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
                if (pool != RecipeMaps.multiblockChemicalReactorRecipes) return Evaluation.INVALID;
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
            case BIO_VAT -> {}
            case MEGA_ALLOY -> {
                if (c < 0 && !u.basicAlloyCoil) return Evaluation.INVALID;
                value.set(256, Math.pow(0.95, Math.max(c + 1 - recipeTier, 0)), 1 - 0.05 * Math.max(c - 3, 0));
                value.maximumRecipeTier = u.glass;
            }
            case BALL_LIGHTNING -> {
                if (pool != RecipeMaps.arcFurnaceRecipes || shape != 1
                    || u.ballChip
                    || u.gravityLens
                    || u.fusion < 1
                    || c < HeatingCoilLevel.UMV.getTier()
                    || u.ballField < 1) return Evaluation.INVALID;
                value.set(powerOfTwo((long) u.fusion * c), 1, 1);
                value.timeDivisor = u.ballField >= 3 ? 4 : 2;
                value.maximumRecipeTier = u.glass < 12 ? u.glass : -1;
            }
            case INDISTINCT -> {
                if (pool != RecipeMaps.assemblerRecipes || u.component < 0) return Evaluation.INVALID;
                value.set(Integer.MAX_VALUE, 1, 0.25);
                value.timeDivisor = u.component >= recipeTier ? 4 : 2;
                value.maximumRecipeTier = u.component + 2;
            }
            case PRECISE_ASSEMBLER -> {
                if (pool != RecipeMaps.assemblerRecipes || u.precision < -1
                    || u.machineCasing < 0
                    || u.glass < VoltageIndex.EV) return Evaluation.INVALID;
                value.set(powerOfTwo(u.precision + 5L), 1, 0.5);
                value.maximumRecipeTier = u.machineCasing < VoltageIndex.UHV ? u.machineCasing : -1;
            }
            case AUTOCLAVE -> {
                if (u.itemPipe < 1 || u.fluidPipe < 1 || c < 0) return Evaluation.INVALID;
                value.set(12L * u.itemPipe, (12 - u.fluidPipe) / 12d, 1 / (1 + 0.25 * (c + 1)));
            }
            case CRYSTALLINE -> {
                if (pool != RecipeMaps.autoclaveRecipes && pool != RecipeMaps.chemicalBathRecipes)
                    return Evaluation.INVALID;
                if (u.field < 1 || u.glass < 1) return Evaluation.INVALID;
                value.set(
                    Math.min(Integer.MAX_VALUE, (long) u.glass * u.field * p),
                    1d / u.field,
                    pool == RecipeMaps.chemicalBathRecipes ? 0.0625 : 0.25);
                value.timeDivisor = u.field >= 3 ? 4 : 2;
            }
            case MANUFACTURING -> {
                if (u.core < 5) return Evaluation.INVALID;
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
                    if (requiredSpecialInput == null || dishes <= 0) return reject(pool, "culture", dishes, 1);
                    if (u.glass < requiredGlassTier) return reject(pool, "glass", u.glass, requiredGlassTier);
                    value.set(dishes, 1, 1 / (1.6 + 0.6 * (u.glass - requiredGlassTier)));
                }
            }
            case OIL_CRACKER -> {
                if (c < 0) return Evaluation.INVALID;
                value.euMultiplier = Math.max(0.5, 1 - 0.1 * (c + 1));
            }
            case MEGA_OIL_CRACKER -> {
                if (c < 0) return Evaluation.INVALID;
                value.set(256, Math.pow(0.9, c + 1), 1);
            }
            case ADVANCED_OIL_CRACKER -> {
                if (c < 0) return Evaluation.INVALID;
                value.set(256, 1d / (c + 1), 0.5);
                value.timeDivisor = u.coil == HeatingCoilLevel.UXV || u.coil == HeatingCoilLevel.MAX ? 4 : 2;
            }
            case ADVANCED_DISTILLATION -> {
                if (pool == RecipeMaps.distillationTowerRecipes) value.set(12, 1, 1d / 3);
                else if (pool == RecipeMaps.distilleryRecipes) value.set(2L * (shape / 3) * t, 0.15, 0.5);
                else return Evaluation.INVALID;
            }
            case MEGA_DISTILLATION -> {
                if (pool == RecipeMaps.distillationTowerRecipes) value.set(256, 0.9, 2d / 3);
                // The source increments height after checking the final middle section.
                else if (pool == RecipeMaps.distilleryRecipes) value.set(256L * (1 + (shape + 1) / 2), 0.5, 0.5);
                else return Evaluation.INVALID;
            }
            case MAGNETIC_DOMAIN -> value.set(64L * shape, 1, Math.pow(0.75, p));
            case EXTRACTOR -> {
                if (u.itemPipe < 1) return Evaluation.INVALID;
                value.set(8L * u.itemPipe, 0.85, 1d / 3);
            }
            case SPACE_SCALER -> {
                if (pool != RecipeMaps.compressorRecipes && pool != RecipeMaps.extractorRecipes
                    && pool != BartWorksRecipeMaps.electricImplosionCompressorRecipes) return Evaluation.INVALID;
                int requiredField = pool == BartWorksRecipeMaps.electricImplosionCompressorRecipes ? 11 : 1;
                if (u.field < requiredField) return reject(pool, "field", u.field, requiredField);
                if (pool == RecipeMaps.compressorRecipes)
                    value.compressionTier = u.field >= 11 ? Integer.MAX_VALUE : u.field - 1;
                value.set(Integer.MAX_VALUE, 1, u.field >= 2 ? 0.1 : 1);
                value.timeDivisor = u.field >= 2 ? 4 : 2;
            }
            case LARGE_FLUID_EXTRACTOR -> {
                if (c < 0 || u.solenoid < 0) return Evaluation.INVALID;
                value.set(Math.max(1, 8L * u.solenoid), 0.8 * Math.pow(0.9, c), 1 / (1.5 + 0.1 * c));
            }
            case HUMAN_POWER -> {
                if (c < 0) return Evaluation.INVALID;
                value.set(Integer.MAX_VALUE, 1, 1d / (c + 1));
                value.timeDivisor = 4;
            }
            case FORGE_HAMMER -> value.set(6L * Math.max(1, u.solenoid) * t, 1, 0.5);
            case PRESSURE_FORMER -> {
                if (c < 0) return Evaluation.INVALID;
                boolean extruder = pool == RecipeMaps.extruderRecipes;
                value.set(
                    Config.Parallel_MagneticDrivePressureFormer,
                    Config.EU_Multiplier_MagneticDrivePressureFormer,
                    1d / ((extruder ? Config.SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer
                        : Config.SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer)
                        * (1 + c * Config.SpeedUpMultiplier_Coil_MagneticDrivePressureFormer)));
                value.timeDivisor = !extruder
                    || c >= Config.CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer ? 4 : 2;
            }
            case COKE_OVEN -> {
                if (c < 0) return Evaluation.INVALID;
                int width = Math.max(0, shape - 1);
                if (u.cokeTier < 0) return Evaluation.INVALID;
                boolean adiabatic = u.cokeTier > 0;
                value.set(adiabatic ? 32L + 16L * width : 16L + 8L * width, Math.pow(0.98, c + 1), 1);
            }
            case LASER_ENGRAVER -> {
                if (u.laserTier < 0 || u.glass < u.laserTier || u.laserParallel <= 0) return Evaluation.INVALID;
                value.set(u.laserParallel, 0.8, 1d / 3.5);
                value.maximumRecipeTier = u.laserTier < VoltageIndex.UXV ? u.laserTier + 1 : -1;
                value.maximumOverclocks = Math.max(0, u.laserTier - recipeTier + 1);
            }
            case PHOTON_MASTER -> {
                if (pool != RecipeMaps.laserEngraverRecipes) return Evaluation.INVALID;
                value.set(256, 1, 1d / (2 * (1 + u.photonSpeed / 10000d)));
                value.timeDivisor = u.photonPerfect ? 4 : 2;
            }
            case MACERATOR -> value.set((shape > 1 ? 8L : 2L) * t, 1, shape > 1 ? 0.15625 : 0.625);
            case MEGA_MACERATOR -> {
                if (u.maceratorTier < 1) return Evaluation.INVALID;
                long parallel = u.maceratorTier >= 3 ? Integer.MAX_VALUE : u.maceratorTier == 2 ? 32768 : 128;
                value.set(parallel, 1, u.glass >= 12 || u.glass > recipeTier ? 0.125 : 1);
            }
            case MOLECULE -> {
                if (pool != RecipeMaps.centrifugeNonCellRecipes && pool != RecipeMaps.electrolyzerNonCellRecipes)
                    return Evaluation.INVALID;
                value.set(24L * shape, 1, Math.pow(0.9, p));
                value.timeDivisor = shape >= 16 ? 4 : 2;
            }
            case GIANT_DRYER -> {
                if (c < 0) return Evaluation.INVALID;
                value.set(Math.min(Integer.MAX_VALUE, (long) shape * (c + 1) * 32), 1, 2 * Math.pow(0.8, p) / (c + 1));
            }
            case DEHYDRATOR -> {
                value.set(4, 0.5, 1d / 2.2);
                value.heatDiscount = 0.95;
            }
            case MIXER -> {
                if (u.itemPipe < 1) return Evaluation.INVALID;
                value.set(8L * t, 1, 1d / (u.itemPipe + 2));
            }
            case THERMAL_CENTRIFUGE -> {
                if (c < 0 || u.solenoid < 0) return Evaluation.INVALID;
                value.set(8L * t + 2L * u.solenoid, 0.8 * Math.pow(0.95, c), 1d / (2.5 + 0.05 * c));
            }
            case THERMAL_DEVOURER -> value.set(1024, 1, 1d / (2 * (1 + p)));
            case SOLIDIFIER -> value.set(10L * t, 0.8, 1d / 3);
            case CRYOGENIC_FREEZER -> {
                value.set(16, 0.9, 1d / 3);
                value.upkeep = cryotheum(10);
            }
            case ARC_FURNACE -> {
                if (pool != RecipeMaps.arcFurnaceRecipes || u.electrode == null) return Evaluation.INVALID;
                ArcFurnaceElectrode electrode = u.electrode;
                value.set(
                    electrode == ArcFurnaceElectrode.InfinityElectrode ? Integer.MAX_VALUE : electrode.parallelLimit,
                    electrode.euModifier,
                    1d / electrode.speedModifier);
                value.timeDivisor = electrode.OCSpeedFactor;
                value.powerMultiplier = electrode.OCPowerFactor;
            }
            case INDISTINCT_PROTOTYPE, MODULAR_NEUTRON, MODULAR_PLASMA_FORGE -> {
                value.set(masterParallel, masterEU, masterTime);
                value.requiresHeat = machine == Machine.MODULAR_PLASMA_FORGE;
                if (machine == Machine.INDISTINCT_PROTOTYPE) {
                    value.timeDivisor = 1;
                    value.powerMultiplier = 1;
                }
            }
            case ADVANCED_BLAST -> {
                value.set(8, 0.9, 1d / 2.2);
                value.heatDiscount = 0.95;
                value.upkeep = pyrotheum(10);
            }
            case STELLAR_FORGE -> {
                if (pool != GTCMRecipe.StellarForgeRecipeMap && pool != GTCMRecipe.StellarForgeAlloySmelterRecipeMap)
                    return Evaluation.INVALID;
                value.euMultiplier = Math.max(
                    1,
                    pool == GTCMRecipe.StellarForgeRecipeMap ? Config.multiplierOfMiracleDoorEUCostEBFMode
                        : Config.multiplierOfMiracleDoorEUCostABSMode);
                // Native Miracle Door recipes have no coil-temperature admission limit.
                value.heat = Integer.MAX_VALUE;
                value.maximumOverclocks = 0;
                value.timeDivisor = value.powerMultiplier = 1;
            }
            case HEAT_CONVECTOR -> {
                if (pool != GTCMRecipe.RapidCoolingDownRecipeMap) return Evaluation.INVALID;
                value.maximumOverclocks = 0;
                value.timeDivisor = value.powerMultiplier = 1;
            }
            case GODFORGE_SMELTING, GODFORGE_MOLTEN, GODFORGE_EXOTIC, GODFORGE_PLASMA -> {
                var preset = source.godforge;
                if (preset == null) return Evaluation.INVALID;
                value.set(preset.parallel(), preset.eu(), preset.time());
                // This package aggregates multiple native modules, each retaining its own int parallel limit.
                value.parallelLimit = Long.MAX_VALUE;
                value.timeDivisor = preset.overclock();
                value.heat = preset.heat();
                value.plasmaTier = preset.plasmaTier();
                value.multiStepPlasma = preset.multiStepPlasma();
                value.upkeep = new FluidStack[] { Materials.MHDCSM.getMolten(preset.fuelPerFiveSeconds()) };
                value.upkeepPeriodSeconds = 5;
                if (machine == Machine.GODFORGE_SMELTING || machine == Machine.GODFORGE_MOLTEN)
                    value.heatDiscount = preset.heatDiscount();
            }
            case FOUNDRY -> {
                FoundryData foundry = source.foundry;
                if (foundry == null) return Evaluation.INVALID;
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
                    if (coolant == null) return reject(pool, "coolant", 0, 1);
                    additional = coolant == Materials.SuperCoolant ? 1 : coolant == Materials.SpaceTime ? 2 : 3;
                    value.upkeep = new FluidStack[] { coolant == Materials.SuperCoolant ? coolant.getFluid(100)
                        : coolant.getMolten(coolant == Materials.SpaceTime ? 50 : 25) };
                }
                value.maximumOverclocks = Math.max(0, t - recipeTier + additional);
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
            }
            case CHEMICAL_PLANT -> {
                if (u.fluidPipe < 1 || u.machineCasing < 0 || u.solidCasing < 0 || c < 0) return Evaluation.INVALID;
                value.set(2L * u.fluidPipe, 1, 2d / (1 + c));
                value.casingTier = u.solidCasing;
                value.maximumRecipeTier = u.machineCasing < 9 ? u.machineCasing : -1;
            }
            case MULTI_FURNACE -> {
                if (c < 0) return Evaluation.INVALID;
                value.parallel = Math.min(Integer.MAX_VALUE, 4L * powerOfTwo(u.coil.ordinal() - 1));
            }
            case HEPHAESTUS -> {
                if (pool != RecipeMaps.furnaceRecipes) return Evaluation.INVALID;
                value.parallel = Integer.MAX_VALUE;
                value.timeDivisor = 4;
            }
            case CENTRIFUGE -> value.set(8L * t, 0.9, 1d / 3);
            case SPINMATRON -> {
                if (u.rotorScore <= 0) return Evaluation.INVALID;
                boolean boost = contains(fluids, Materials.BiocatalyzedPropulsionFluid.getFluid(1));
                value.heavyCentrifuge = shape >= 3 && boost;
                if (heavyCentrifuge && !value.heavyCentrifuge) return reject(pool, "heavy_centrifuge", 0, 1);
                value.set(
                    Math.max(1, (long) Math.floor(4 * u.rotorScore * (boost ? 1.25 : 1) / (heavyCentrifuge ? 32 : 1))),
                    heavyCentrifuge ? 11.2 : 0.7,
                    !heavyCentrifuge && t - recipeTier >= 3 ? 0.25 : 1d / 3);
                value.maximumOverclocks = Math.max(0, t - recipeTier + 1);
                int amount = Math.max(1, recipeTier) * 10;
                value.upkeep = new FluidStack[] { boost ? Materials.BiocatalyzedPropulsionFluid.getFluid(amount)
                    : new FluidStack(GTPPFluids.Kerosene, amount) };
                value.startupReserveSeconds = 5;
            }
            case PURIFICATION_CLARIFIER, PURIFICATION_DEGASSER, PURIFICATION_FLOCCULATION, PURIFICATION_OZONATION, PURIFICATION_PH, PURIFICATION_PLASMA, PURIFICATION_BARYONIC, PURIFICATION_UV -> {
                value.timeDivisor = 4;
                value.powerMultiplier = 4;
            }
            case QUANTUM -> {
                long catalyst = count(items, requiredSpecialInput);
                if (requiredSpecialInput == null || catalyst <= 0) return reject(pool, "catalyst", catalyst, 1);
                if (u.quantumTier < 1) return reject(pool, "casing", u.quantumTier, 1);
                value.parallel = Math.min(Integer.MAX_VALUE, catalyst);
                value.casingTier = u.quantumTier;
            }
            case DE_FUSION -> {
                value.casingTier = u.draconicTier;
                value.perfectOverclocks = Math.max(0, u.draconicTier - 1);
            }
            case NAQUADAH_REFINERY -> {
                if (u.refineryCoil < 1) return reject(pool, "casing", u.refineryCoil, 1);
                value.parallel = 4L * u.refineryCoil;
                value.casingTier = u.refineryCoil;
                value.timeDivisor = 4;
                value.powerMultiplier = 4;
            }
            case FRIDGE -> {
                value.set(256, 1, 2d / 3);
                if (u.fridgeTier > 1) {
                    value.perfectOverclocks = 3;
                    value.upkeep = new FluidStack[] { Materials.Eternity.getMolten(20) };
                }
            }
            case NEUTRON -> {
                if (shape < 4 || shape > 249) return Evaluation.INVALID;
                value.timeMultiplier = Math.pow(0.9, shape - 4);
                value.minimumSourceDuration = 1;
                value.timeDivisor = 1;
                value.powerMultiplier = 1;
                value.maximumOverclocks = 0;
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
            var godforge = source.godforge;
            long excess = Math.max((godforge == null ? value.heat : godforge.overclockHeat()) - requiredHeat, 0);
            value.euMultiplier *= Math.pow(value.heatDiscount, excess / 900);
            value.perfectOverclocks = (int) Math.min(Integer.MAX_VALUE, excess / 1800);
        }
        // Swelegfyr's operating heat affects discounts and OC, but does not raise its recipe admission limit.
        if (machine == Machine.SWELEGFYR) value.heat = structure.heat() + 100L * Math.max(t - 2, 0);
        if (value.requiresHeat && (value.heat <= 0 || requiredHeat > value.heat))
            return reject(pool, "heat", value.heat, Math.max(1, requiredHeat));
        if (requiredGlassTier > value.glassTier) return reject(pool, "glass", value.glassTier, requiredGlassTier);
        return new Evaluation(value.profile(), null);
    }

    private static Evaluation reject(RecipeMap<?> pool, String parameter, long available, long required) {
        return new Evaluation(
            null,
            BottledChaosPoolFailure.parameter(pool.unlocalizedName, parameter, available, required));
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
        int maximumOverclocks = -1;
        int perfectOverclocks;
        int casingTier = -1;
        int compressionTier = -1;
        double minimumSourceDuration;
        boolean heavyCentrifuge;
        int plasmaTier;
        boolean multiStepPlasma;
        int startupReserveSeconds = 1;
        int upkeepPeriodSeconds = 1;
        long maximumSourcePower = Long.MAX_VALUE;
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
                maximumOverclocks,
                perfectOverclocks,
                casingTier,
                compressionTier,
                minimumSourceDuration,
                heavyCentrifuge,
                plasmaTier,
                multiStepPlasma,
                startupReserveSeconds,
                maximumSourcePower,
                upkeepPeriodSeconds);
        }
    }

    static final class Upgrades {

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
        boolean ingotMold;
        double rotorScore;
        ArcFurnaceElectrode electrode;

        Upgrades(ItemStack[] materials) {
            for (ItemStack stack : materials) {
                if (stack == null || stack.stackSize <= 0) continue;
                ingotMold |= GTCMItemList.WhiteDwarfMold_Ingot.equal(stack);
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

}
