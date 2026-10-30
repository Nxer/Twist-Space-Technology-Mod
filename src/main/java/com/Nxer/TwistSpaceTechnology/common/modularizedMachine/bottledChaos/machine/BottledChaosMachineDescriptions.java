package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineParameters.evaluate;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineParameters.Machine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineParameters.Profile;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineParameters.Upgrades;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosOverclock;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.common.tileentities.machines.multi.foundry.FoundryData;

/** Player-facing parameter descriptions, derived from the same machine profiles used for execution. */
public final class BottledChaosMachineDescriptions {

    public static List<String> replacementDescriptions(ItemStack controller, Structure structure) {
        return replacementDescriptions(controller, structure, null);
    }

    private static List<String> replacementDescriptions(ItemStack controller, Structure structure, RecipeMap<?> pool) {
        List<String> lines = new ArrayList<>();
        var source = new BottledChaosMachineParameters.InstalledSource(controller, structure);
        var machine = BottledChaosSupportedMachine.find(source.original);
        if (machine == null) return lines;
        // #tr tst.bottledchaos.machine_module.replaces_chain
        // # Replaces: %s
        // #zh_CN 可替代: %s

        // #tr tst.bottledchaos.machine_module.mold_finishing
        // # White Dwarf Ingot Mold adds ingot routes while retaining molten routes.
        // #zh_CN 白矮星锭模具新增直出锭路线, 同时保留熔融路线.
        for (var rule : machine.replacements()) {
            if (pool != null && rule.pool() != pool) continue;
            String chain = rule.chain()
                .stream()
                .map(BottledChaosMachineConfiguration::poolName)
                .collect(java.util.stream.Collectors.joining(" -> "));
            String replacement = BottledChaosMachineConfiguration.poolName(rule.pool());
            lines.add(
                text(
                    "replaces_chain",
                    chain + " => "
                        + replacement
                        + (rule.solidify()
                            ? " -> " + BottledChaosMachineConfiguration.poolName(RecipeMaps.fluidSolidifierRecipes)
                            : "")));
            if (rule.moldBypass() && source.upgrades.ingotMold) lines.add(
                text(
                    "replaces_chain",
                    chain + " => "
                        + replacement
                        + " + "
                        + com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WhiteDwarfMold_Ingot.get(1)
                            .getDisplayName()));
        }
        if (source.upgrades.ingotMold) lines.add(text("mold_finishing"));
        return lines;
    }

    public static List<String> describe(ItemStack controller, Structure structure, RecipeMap<?> pool,
        int quantitySpeedExponent, boolean compact) {
        return describe(controller, structure, pool, quantitySpeedExponent, compact, null);
    }

    /** The GUI receives numeric host-dependent values from the server, while translating locally. */
    public static List<String> describeForGui(ItemStack controller, Structure structure, RecipeMap<?> pool,
        int quantitySpeedExponent, NBTTagCompound runtime) {
        if (runtime.getBoolean("debug")) {
            return Arrays.asList(
                EnumChatFormatting.GRAY
                    + text("parallel", EnumChatFormatting.GOLD + number(runtime.getLong("parallel"))),
                EnumChatFormatting.GRAY + text("eu", EnumChatFormatting.YELLOW + number(runtime.getDouble("eu"))),
                EnumChatFormatting.GRAY + text(
                    "time",
                    EnumChatFormatting.GREEN
                        + number(runtime.getDouble("time") / (1d + Math.max(0, quantitySpeedExponent)))),
                EnumChatFormatting.GRAY + text(
                    "oc",
                    EnumChatFormatting.AQUA + clock(runtime.getDouble("power"), runtime.getDouble("divisor"))));
        }
        List<String> lines = describe(controller, structure, pool, quantitySpeedExponent, true, runtime);
        // #tr tst.bottledchaos.machine_module.source_machine
        // # Source machine: %s
        // #zh_CN 来源机器: %s
        if (controller != null) lines.add(0, text("source_machine", controller.getDisplayName()));
        lines.addAll(replacementDescriptions(controller, structure, pool));
        return lines;
    }

    private static List<String> describe(ItemStack controller, Structure structure, RecipeMap<?> pool,
        int quantitySpeedExponent, boolean compact, NBTTagCompound runtime) {
        List<String> lines = new ArrayList<>();
        if (controller == null || pool == null) return lines;
        var source = new BottledChaosMachineParameters.InstalledSource(controller, structure);
        Machine machine = source.machine;
        var evaluation = evaluate(
            source,
            pool,
            1,
            new BottledChaosMachineParameters.Context(
                32,
                32,
                new ItemStack[0],
                new FluidStack[0],
                1,
                1,
                1,
                OverclockType.NormalOverclock,
                false,
                false),
            BottledChaosSourceRequirements.NONE);
        Profile base = evaluation.profile();
        String unavailable = evaluation.failure() == null
            ? StatCollector.translateToLocal("tst.bottledchaos.result.invalid_source_configuration")
            : evaluation.failure()
                .getDisplayString();
        // Recipe-dependent profiles are described symbolically below; unavailable modes have no numeric parameters.
        if (base == null && machine != Machine.BIOSPHERE && machine != Machine.QUANTUM && machine != Machine.FOUNDRY) {
            lines.add(EnumChatFormatting.YELLOW + unavailable);
            return lines;
        }
        if (machine == Machine.STELLAR_FORGE || machine == Machine.HEAT_CONVECTOR) {
            if (machine == Machine.STELLAR_FORGE) {
                // #tr tst.bottledchaos.machine_module.energy_duration
                // # Duration is normalized from total recipe energy.
                // #zh_CN 按配方总能耗折算耗时.
                String forgeEU = runtime != null && runtime.getBoolean("numeric") ? number(runtime.getDouble("eu"))
                    : base == null ? "-" : number(base.euMultiplier());
                lines.add(text("eu", forgeEU));
                lines.add(text("energy_duration"));
                // #tr tst.bottledchaos.machine_module.critical_photon
                // # Critical Photon: 1 per packaged machine per round.
                // #zh_CN 临界光子: 每台封装机器每轮1个.
                if (runtime == null) lines.add(text("critical_photon"));
                else {
                    // #tr tst.bottledchaos.machine_module.energy_duration_scaled
                    // # Duration: total recipe energy x%s / recipe voltage / %s.
                    // #zh_CN 耗时: 配方总能耗 x%s / 配方电压 / %s.
                    lines.set(1, text("energy_duration_scaled", forgeEU, 1 + Math.max(0, quantitySpeedExponent)));
                    // #tr tst.bottledchaos.machine_module.photon_demand
                    // # Critical Photons per round: %s
                    // #zh_CN 每轮临界光子: %s
                    lines.add(text("photon_demand", runtime.getLong("quantity")));
                }
            } else {
                // #tr tst.bottledchaos.machine_module.free_cooling
                // # Plasma cooling: 0 EU, 0 ticks of processing work.
                // #zh_CN 等离子冷却: 0 EU, 0 tick处理耗时.
                lines.add(text("free_cooling"));
            }
            return lines;
        }
        double quantitySpeed = runtime != null && runtime.hasKey("speed") ? runtime.getDouble("speed")
            : (1d + Math.max(0, quantitySpeedExponent)) / source.poolSpeedDivisor();
        String parallel = base == null ? "-" : number(base.parallel());
        String eu = base == null ? "-" : number(base.euMultiplier());
        String time = base == null ? "-" : number(base.timeMultiplier() / quantitySpeed);
        String oc = base == null ? "-" : clock(base.powerMultiplier(), base.timeDivisor());
        String restrictions = base == null ? unavailable : machine == Machine.BIO_VAT ? text("culture") : "";
        String special = "";
        Upgrades u = source.upgrades;
        int shape = structure == null ? 1 : structure.profile();
        if (base != null && source.godforge != null) special = text(
            "godforge_configuration",
            source.godforge.rings(),
            BottledChaosComplexStructures.godforgeUpgradeName(source.godforge));
        if (runtime == null && base != null && BottledChaosMachineConfiguration.isPurificationPool(pool))
            special = text("purification");
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
            }
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
            case INDISTINCT_PROTOTYPE, MODULAR_NEUTRON, MODULAR_PLASMA_FORGE -> {
                parallel = text("master_parallel");
                eu = text("master_eu");
                time = text("master_time");
                variableTime = true;
                special = text("master");
            }
            case ADVANCED_BLAST -> {
                if (recipeHeat) eu = "0.9 x " + text("thermal_eu", "0.95");
            }
            case GODFORGE_SMELTING, GODFORGE_MOLTEN -> {
                var preset = source.godforge;
                if (preset != null && recipeHeat) eu = (preset.eu() == 1 ? "" : number(preset.eu()) + " x ")
                    + text("thermal_eu", number(preset.heatDiscount()));
            }
            case FOUNDRY -> {
                FoundryData foundry = source.foundry;
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
                if (structure != null)
                    lines.add(StatCollector.translateToLocalFormatted("GT5U.machines.tier.fmt", shape));
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
        // #tr tst.bottledchaos.machine_module.master_oc
        // # Host overclock parameters
        // #zh_CN 主机超频参数
        if (source.inheritsMasterOverclock) oc = text("master_oc");
        if (runtime != null && runtime.getBoolean("numeric")) {
            // Recipe-dependent expressions remain expressions; host voltage/power and installed upgrades are numeric.
            if (machine != Machine.QUANTUM && machine != Machine.SPINMATRON
                && machine != Machine.FUSION
                && machine != Machine.BIO_VAT
                && machine != Machine.BIOSPHERE) parallel = number(runtime.getLong("parallel"));
            if (machine != Machine.SPINMATRON && machine != Machine.BIO_VAT && machine != Machine.BIOSPHERE) {
                time = number(runtime.getDouble("time") / quantitySpeed);
                variableTime = false;
            }
            if (!recipeHeat && machine != Machine.MEGA_ALLOY && machine != Machine.SPINMATRON)
                eu = number(runtime.getDouble("eu"));
            if (machine != Machine.INDISTINCT) oc = clock(runtime.getDouble("power"), runtime.getDouble("divisor"));
            if (machine == Machine.BLAST_FURNACE) special = text("thermal_oc");
        }
        // #tr tst.bottledchaos.machine_module.parallel
        // # Duration normalization basis: %s
        // #zh_CN 耗时折算基数: %s
        lines.add(EnumChatFormatting.GRAY + text("parallel", EnumChatFormatting.GOLD + parallel));
        // #tr tst.bottledchaos.machine_module.eu
        // # Power multiplier: x%s
        // #zh_CN 耗电倍率: x%s
        lines.add(EnumChatFormatting.GRAY + text("eu", EnumChatFormatting.YELLOW + eu));
        // #tr tst.bottledchaos.machine_module.time
        // # Duration multiplier: x%s
        // #zh_CN 耗时倍率: x%s
        if (variableTime && quantitySpeed != 1)
            time = time.startsWith("1/") ? number(1 / quantitySpeed) + time.substring(1)
                : number(1 / quantitySpeed) + " x (" + time + ")";
        lines.add(EnumChatFormatting.GRAY + text("time", EnumChatFormatting.GREEN + time));
        // #tr tst.bottledchaos.machine_module.oc
        // # Per overclock: %s
        // #zh_CN 每档超频: %s
        if (machine == Machine.INDISTINCT) {
            OverclockType sourceOverclock = runtime != null && runtime.getBoolean("numeric")
                && runtime.hasKey("sourceOverclock") ? OverclockType.getFromID(runtime.getInteger("sourceOverclock"))
                    : OverclockType.NONE;
            lines.add(EnumChatFormatting.GRAY + text("oc", ""));
            // #tr tst.bottledchaos.machine_module.oc_below_tier
            // # Recipe <= %s: %s
            // #zh_CN 配方<=%s: %s
            lines.add(
                EnumChatFormatting.GRAY + text("oc_below_tier", tierName(u.component), clock(4, 4, sourceOverclock)));
            // #tr tst.bottledchaos.machine_module.oc_above_tier
            // # Recipe > %s: %s
            // #zh_CN 配方>%s: %s
            lines.add(
                EnumChatFormatting.GRAY + text("oc_above_tier", tierName(u.component), clock(4, 2, sourceOverclock)));
        } else lines.add(EnumChatFormatting.GRAY + text("oc", EnumChatFormatting.AQUA + oc));
        int voltageLimit = base != null && machine != Machine.ALLOY_BLAST ? base.maximumRecipeTier() : -1;
        if (voltageLimit >= 0)
            restrictions = append(restrictions, text("recipe_voltage_limit", tierName(voltageLimit)));
        if (machine == Machine.BIO_VAT || machine == Machine.BIOSPHERE && pool != RecipeMaps.brewingRecipes
            && pool != RecipeMaps.fermentingRecipes) {
            // #tr tst.bottledchaos.machine_module.recipe_glass_limit
            // # Maximum culture recipe glass requirement: %s
            // #zh_CN 培养配方所需玻璃等级最高: %s
            restrictions = append(restrictions, text("recipe_glass_limit", tierName(u.glass)));
        }
        if (base != null && base.casingTier() >= 0)
            restrictions = append(restrictions, text("casing_limit", base.casingTier()));
        if (base != null && pool == RecipeMaps.compressorRecipes
            && base.compressionTier() >= 0
            && base.compressionTier() < Integer.MAX_VALUE)
            restrictions = append(restrictions, text("compression_limit", base.compressionTier()));
        if (recipeHeat && base != null && base.heat() > 0) {
            String limit = runtime != null && runtime.getBoolean("numeric") ? formatNumber(runtime.getLong("heat"))
                : machine == Machine.BLAST_FURNACE || machine == Machine.SWELEGFYR
                    ? formatNumber(structure == null ? 0 : structure.heat()) + " + 100 x max(T - 2, 0)"
                    : formatNumber(base.heat());
            String heat = machine == Machine.SWELEGFYR && structure != null ? formatNumber(
                shape > 1 ? (long) (Math.floor(Math.pow(structure.heat(), 1.08) / 100) * 100 + 1) : structure.heat())
                : limit;
            if (!heat.equals(limit))
                lines.add(EnumChatFormatting.YELLOW + text("heat_limit", EnumChatFormatting.WHITE + limit));
            lines.add(StatCollector.translateToLocalFormatted("GT5U.multiblock.heat", heat));
        }
        // #tr tst.bottledchaos.machine_module.limits
        // # Recipe limits: %s
        // #zh_CN 配方限制: %s
        if (!compact || !restrictions.isEmpty()) lines.add(
            EnumChatFormatting.GRAY + text(
                "limits",
                EnumChatFormatting.YELLOW
                    + (restrictions.isEmpty() ? StatCollector.translateToLocal("GT5U.gui.text.nil") : restrictions)));
        if (machine == Machine.BIO_VAT || machine == Machine.BIOSPHERE && pool != RecipeMaps.brewingRecipes
            && pool != RecipeMaps.fermentingRecipes) special = append(special, text("radiation"));
        if (recipeHeat && (machine == Machine.DEHYDRATOR || machine == Machine.ADVANCED_BLAST
            || machine == Machine.GODFORGE_SMELTING
            || machine == Machine.GODFORGE_MOLTEN
            || machine == Machine.HEARTH
            || machine == Machine.SWELEGFYR)) special = append(special, text("thermal_oc"));
        if (runtime == null && base != null && base.upkeep().length > 0 && machine != Machine.SPINMATRON)
            for (FluidStack fluid : base.upkeep()) special += (special.isEmpty() ? "" : "; ") + text(
                "upkeep",
                fluid.getLocalizedName(),
                BigDecimal.valueOf(fluid.amount)
                    .divide(BigDecimal.valueOf(base.upkeepPeriodSeconds()))
                    .stripTrailingZeros()
                    .toPlainString());
        if (base != null && base.perfectOverclocks() > 0 && machine == Machine.FRIDGE)
            special = append(special, text("perfect_stages", base.perfectOverclocks()));
        if (base != null && base.perfectOverclocks() > 0 && machine == Machine.DE_FUSION)
            special = append(special, text("perfect_stages", base.perfectOverclocks()));
        if (machine == Machine.QUANTUM && u.quantumTier >= 1)
            special = append(special, text("quantum_tiers", u.quantumTier, u.focusingTier));
        if (base != null && base.maximumOverclocks() >= 0
            && machine != Machine.FOUNDRY
            && machine != Machine.NEUTRON
            && machine != Machine.FUSION
            && machine != Machine.SPINMATRON
            && machine != Machine.LASER_ENGRAVER) special = append(special, text("oc_limit", base.maximumOverclocks()));
        if (machine == Machine.LASER_ENGRAVER)
            special = append(special, text("laser_oc_limit", tierName(u.laserTier + 1)));
        if (machine == Machine.FUSION) special = append(special, text("fusion_oc_limit"));
        if (parallel.contains("T") || parallel.contains("P")
            || time.contains("P")
            || (runtime == null || !runtime.getBoolean("numeric"))
                && (machine == Machine.BLAST_FURNACE || machine == Machine.SWELEGFYR))
            special = append(special, text("host_tiers"));
        for (String line : special.split(";\\s*")) {
            if (!line.isEmpty()) lines.add(EnumChatFormatting.WHITE + line);
        }
        return lines;
    }

    static String append(String value, String addition) {
        return value.isEmpty() ? addition : value + "; " + addition;
    }

    static String text(String suffix, Object... args) {
        String key = "tst.bottledchaos.machine_module." + suffix;
        return args.length == 0 ? StatCollector.translateToLocal(key)
            : StatCollector.translateToLocalFormatted(key, args);
    }

    private static String number(double value) {
        if (value == Integer.MAX_VALUE) return formatNumber(Integer.MAX_VALUE);
        if (value >= Long.MAX_VALUE) return formatNumber(Long.MAX_VALUE);
        return value == Math.rint(value) ? Long.toString((long) value)
            : String.format(Locale.ROOT, "%.6g", value)
                .replaceFirst("(\\.\\d*?)0+(?=e|$)", "$1")
                .replaceFirst("\\.(?=e|$)", "");
    }

    private static String clock(double power, double divisor, OverclockType sourceOverclock) {
        if (BottledChaosOverclock.isBetterSourceOverclock(sourceOverclock, divisor, power)) {
            power = sourceOverclock.powerIncrease;
            divisor = sourceOverclock.timeReduction;
        }
        return EnumChatFormatting.AQUA + clock(power, divisor);
    }

    private static String clock(double power, double divisor) {
        if (power == 1 && divisor == 1) return StatCollector.translateToLocal("GT5U.gui.text.nil");
        // #tr tst.bottledchaos.machine_module.clock_fixed_duration
        // # Power x%s, duration unchanged
        // #zh_CN 耗电 x%s, 耗时不变
        if (divisor == 1) return text("clock_fixed_duration", number(power));
        // #tr tst.bottledchaos.machine_module.clock
        // # Power x%s, duration /%s
        // #zh_CN 耗电 x%s, 耗时/%s
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

    // #tr tst.bottledchaos.machine_module.culture
    // # Matching culture and sufficient glass tier are required
    // #zh_CN 需要匹配的培养物及足够的玻璃等级

    // #tr tst.bottledchaos.machine_module.automatic_culture
    // # Automatic culture mode
    // #zh_CN 自动培养模式

    // #tr tst.bottledchaos.machine_module.radiation
    // # Ignores radiation requirements
    // #zh_CN 忽略辐射要求

    // #tr tst.bottledchaos.machine_module.dish_count
    // # Matching culture dish count
    // #zh_CN 匹配培养皿数量

    // #tr tst.bottledchaos.machine_module.culture_time
    // # 1/[1.6 + 0.6 x (glass tier - required glass tier)]
    // #zh_CN 1/[1.6 + 0.6 x (玻璃等级 - 所需玻璃等级)]

    // #tr tst.bottledchaos.machine_module.electrode
    // # Ignores electrode startup and durability consumption
    // #zh_CN 忽略电极启动及耐久消耗

    // #tr tst.bottledchaos.machine_module.master
    // # Module duration and power bonuses stack with the main controller's bonuses
    // #zh_CN 模组耗时和耗电增益与主机增益叠加

    // #tr tst.bottledchaos.machine_module.master_parallel
    // # Main controller duration normalization basis
    // #zh_CN 主机耗时折算基数

    // #tr tst.bottledchaos.machine_module.master_eu
    // # Main controller power multiplier
    // #zh_CN 主机耗电倍率

    // #tr tst.bottledchaos.machine_module.master_time
    // # Main controller duration multiplier
    // #zh_CN 主机耗时倍率

    // #tr tst.bottledchaos.machine_module.thermal_eu
    // # %s^floor(excess furnace temperature/900K)
    // #zh_CN %s^floor(炉温差/900K)

    // #tr tst.bottledchaos.machine_module.voltage_heat
    // # Coil furnace temperature %s K + 100 x max(T - 2, 0) K; one perfect OC per 1800 K excess furnace temperature
    // #zh_CN 线圈炉温%s K + 100 x max(T - 2, 0) K; 每1800 K炉温差增加一档无损超频

    // #tr tst.bottledchaos.machine_module.thermal_oc
    // # Excess heat = heat - recipe heat; one perfect overclock for every 1800K above the recipe requirement
    // #zh_CN 炉温差 = 炉温 - 配方所需炉温; 炉温每高出配方要求1800K, 增加一档无损超频

    // #tr tst.bottledchaos.machine_module.heat_limit
    // # Recipe heat limit: %sK
    // #zh_CN 配方炉温限制: %sK

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
    // # Maximum overclock stages: %s
    // #zh_CN 最多超频档数: %s

    // #tr tst.bottledchaos.machine_module.laser_oc_limit
    // # Overclock voltage limit: %s
    // #zh_CN 超频电压上限: %s

    // #tr tst.bottledchaos.machine_module.fusion_oc_limit
    // # Overclock stages depend on the recipe's fusion tier
    // #zh_CN 超频档数受配方聚变等级限制

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
    // #zh_CN 净化水机器堆叠组数不能超过主净化厂堆叠组数

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
}
