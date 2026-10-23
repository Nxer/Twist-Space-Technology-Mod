package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineParameters.evaluate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineParameters.Machine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineParameters.Profile;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineParameters.Upgrades;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureCatalog.Structure;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.common.tileentities.machines.multi.foundry.FoundryData;

/** Player-facing parameter descriptions, derived from the same machine profiles used for execution. */
public final class BottledChaosMachineDescriptions {

    public static List<String> describe(ItemStack controller, Structure structure, RecipeMap<?> pool,
        int quantitySpeedExponent, boolean compact) {
        List<String> lines = new ArrayList<>();
        if (controller == null || pool == null) return lines;
        var source = new BottledChaosMachineParameters.InstalledSource(controller, structure);
        Machine machine = source.machine;
        Profile base = evaluate(
            source,
            pool,
            1,
            32,
            32,
            new ItemStack[0],
            new FluidStack[0],
            1,
            1,
            1,
            0,
            0,
            null,
            false,
            ignored -> {},
            0,
            0);
        if (machine == Machine.STELLAR_FORGE || machine == Machine.HEAT_CONVECTOR) {
            if (machine == Machine.STELLAR_FORGE) {
                // #tr tst.bottledchaos.machine_module.energy_duration
                // # Duration is normalized from total recipe energy.
                // #zh_CN 按配方总能耗折算耗时.
                lines.add(text("eu", base == null ? "-" : number(base.euMultiplier())));
                lines.add(text("energy_duration"));
                // #tr tst.bottledchaos.machine_module.critical_photon
                // # Critical Photon: 1 per module stack per round.
                // #zh_CN 临界光子: 每堆每轮1个.
                lines.add(text("critical_photon"));
            } else {
                // #tr tst.bottledchaos.machine_module.free_cooling
                // # Plasma cooling: 0 EU, 0 ticks of processing work.
                // #zh_CN 等离子冷却: 0 EU, 0 tick处理耗时.
                lines.add(text("free_cooling"));
            }
            return lines;
        }
        double quantitySpeed = 1d + Math.max(0, quantitySpeedExponent);
        String parallel = base == null ? "-" : number(base.parallel());
        String eu = base == null ? "-" : number(base.euMultiplier());
        String time = base == null ? "-" : number(base.timeMultiplier() / quantitySpeed);
        String oc = base == null ? "-" : clock(base.powerMultiplier(), base.timeDivisor());
        String restrictions = base == null
            ? StatCollector.translateToLocal("GT5U.gui.multimachine.incomplete_structure")
            : machine == Machine.BIO_VAT ? text("culture") : "";
        String special = "";
        Upgrades u = source.upgrades;
        int shape = structure == null ? 1 : structure.profile();
        if (base != null && source.godforge != null) special = text(
            "godforge_configuration",
            source.godforge.rings(),
            BottledChaosComplexStructures.godforgeUpgradeName(source.godforge));
        if (base != null && BottledChaosMachineConfiguration.isPurificationPool(pool)) special = text("purification");
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
                if (base != null) special = StatCollector.translateToLocalFormatted(
                    "GT5U.tooltip.fluid.temperature",
                    shape > 1 ? (long) (Math.floor(Math.pow(structure.heat(), 1.08) / 100) * 100 + 1)
                        : structure.heat());
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
            || machine == Machine.GODFORGE_MOLTEN
            || machine == Machine.HEARTH
            || machine == Machine.SWELEGFYR)) special = append(special, text("thermal_oc"));
        if (base != null && base.upkeep().length > 0 && machine != Machine.SPINMATRON)
            for (FluidStack fluid : base.upkeep())
                special += (special.isEmpty() ? "" : "; ") + text("upkeep", fluid.getLocalizedName(), fluid.amount);
        if (base != null && base.perfectOverclocks() > 0 && machine == Machine.FRIDGE)
            special += "; " + text("perfect_stages", base.perfectOverclocks());
        if (base != null && base.perfectOverclocks() > 0 && machine == Machine.DE_FUSION)
            special = append(special, text("perfect_stages", base.perfectOverclocks()));
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

    static String append(String value, String addition) {
        return value.isEmpty() ? addition : value + "; " + addition;
    }

    static String text(String suffix, Object... args) {
        String key = "tst.bottledchaos.machine_module." + suffix;
        return args.length == 0 ? StatCollector.translateToLocal(key)
            : StatCollector.translateToLocalFormatted(key, args);
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
}
