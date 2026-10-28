package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Part;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Requirement;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Rule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.tileentities.machines.multi.foundry.FoundryData;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModule;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;

/** Complete hosts and their attached modules. Only native public structures and parameter APIs are used. */
public final class BottledChaosComplexStructures {

    private static final Map<String, Map<Integer, GodforgeProfile>> godforgeProfiles = new LinkedHashMap<>();
    private static final Map<Integer, ItemStack[][]> foundryCosts = new LinkedHashMap<>();
    private static final Map<Integer, List<int[]>> foundryChoices = new LinkedHashMap<>();

    /** Complete host, paid upgrade path and its loading-time native parameters at the selected operating point. */
    @Desugar
    public record GodforgeProfile(ItemStack module, int rings, int modules, long parallel, long heat,
        long overclockHeat, double eu, double time, double overclock, double heatDiscount, int fuelFactor, long battery,
        Set<ForgeOfGodsUpgrade> upgrades, int plasmaTier, boolean multiStepPlasma, int fuelPerFiveSeconds) {}

    static void clear() {
        godforgeProfiles.clear();
        foundryCosts.clear();
        foundryChoices.clear();
    }

    static void registerFoundry(IMetaTileEntity machine, BottledChaosSupportedMachine supported, long pools,
        List<Rule> rules) {
        for (int tier = 1; tier <= 3; tier++) {
            Rule tower = selectFoundryChassis(
                BottledChaosStructureCatalog.readRule(machine, supported, tier, pools),
                tier);
            ItemStack[][] costs = new ItemStack[FoundryModule.size()][];
            for (FoundryModule module : FoundryModule.values()) {
                if (module == FoundryModule.UNSET) continue;
                Rule section = selectFoundryChassis(
                    BottledChaosStructureCatalog.readRule(
                        machine,
                        supported,
                        tier,
                        pools,
                        Collections.singletonList(new BottledChaosSupportedMachine.Piece(module.structureID, 0, 0, 0))),
                    tier);
                Map<TST_ItemID, ItemStack> materials = fixedMaterials(section);
                remove(materials, tower.controller(), 1);
                costs[module.ordinal()] = materials.values()
                    .toArray(new ItemStack[0]);
            }
            foundryCosts.put(tier, costs);
            List<int[]> choices = new ArrayList<>();
            enumerateFoundryChoices(tier + 1, 1, new int[FoundryModule.size()], choices);
            foundryChoices.put(tier, choices);
            rules.add(tower);
        }
    }

    private static Rule selectFoundryChassis(Rule source, int tier) {
        List<Part> parts = new ArrayList<>();
        for (Part part : source.parts()) {
            ItemStack item = part.material();
            if (item.getItem() == net.minecraft.item.Item.getItemFromBlock(GregTechAPI.sBlockCasingsFoundry)
                && item.getItemDamage() >= 1
                && item.getItemDamage() <= 3) {
                if (item.getItemDamage() == tier) parts.add(new Part(0, item.copy()));
            } else {
                if (part.group() != 0) throw new IllegalArgumentException("Unexpected foundry structure variant");
                parts.add(new Part(0, item.copy()));
            }
        }
        return new Rule(
            source.machineKey(),
            tier,
            source.poolMask(),
            source.controller(),
            parts.toArray(new Part[0]),
            0,
            new Requirement[0]);
    }

    /** Enumerate small module-count vectors for the one animated page, never expanded structure assets. */
    private static void enumerateFoundryChoices(int remaining, int next, int[] counts, List<int[]> choices) {
        if (remaining == 0) {
            choices.add(counts.clone());
            return;
        }
        for (int i = next; i < counts.length; i++) {
            FoundryModule module = FoundryModule.values()[i];
            if (uniqueModule(module) && counts[i] != 0) continue;
            counts[i]++;
            enumerateFoundryChoices(remaining - 1, i, counts, choices);
            counts[i]--;
        }
    }

    private static boolean uniqueModule(FoundryModule module) {
        return module == FoundryModule.UNIVERSAL_COLLAPSER || module == FoundryModule.HYPERCOOLER
            || module == FoundryModule.EFFICIENT_OC;
    }

    public static boolean isGodforgeHost(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemMachines
            && ItemMachines.getMetaTileEntity(stack) instanceof MTEForgeOfGods;
    }

    static void registerGodforge(long[] machinePools, List<Rule> rules) {
        IMetaTileEntity host = null;
        for (IMetaTileEntity machine : GregTechAPI.METATILEENTITIES)
            if (machine instanceof MTEForgeOfGods) host = machine;
        if (host == null) return;
        // Every ring count uses its maximum legal upgrade path. Split branches remain distinct configurations.
        Map<Integer, EnumSet<ForgeOfGodsUpgrade>> maximumPaths = new LinkedHashMap<>();
        maximumPaths.put(11, maximumPath(1, EnumSet.of(ForgeOfGodsUpgrade.TCT, ForgeOfGodsUpgrade.GGEBE)));
        maximumPaths.put(12, maximumPath(1, EnumSet.of(ForgeOfGodsUpgrade.SEFCP, ForgeOfGodsUpgrade.GGEBE)));
        maximumPaths.put(13, maximumPath(1, EnumSet.of(ForgeOfGodsUpgrade.SEFCP, ForgeOfGodsUpgrade.TCT)));
        maximumPaths.put(21, maximumPath(2, EnumSet.of(ForgeOfGodsUpgrade.SEFCP)));
        maximumPaths.put(22, maximumPath(2, EnumSet.of(ForgeOfGodsUpgrade.TCT)));
        maximumPaths.put(23, maximumPath(2, EnumSet.of(ForgeOfGodsUpgrade.GGEBE)));
        maximumPaths.put(3, maximumPath(3, EnumSet.noneOf(ForgeOfGodsUpgrade.class)));
        Map<Integer, Rule> towers = new LinkedHashMap<>();
        for (int id = 0; id < machinePools.length; id++) {
            if (machinePools[id] == 0) continue;
            IMetaTileEntity module = GregTechAPI.METATILEENTITIES[id];
            BottledChaosSupportedMachine supported = module == null ? null : BottledChaosSupportedMachine.find(module);
            ForgeOfGodsUpgrade target = supported == BottledChaosSupportedMachine.MTESMELTING_MODULE
                ? ForgeOfGodsUpgrade.START
                : supported == BottledChaosSupportedMachine.MTEMOLTEN_MODULE ? ForgeOfGodsUpgrade.FDIM
                    : supported == BottledChaosSupportedMachine.MTEPLASMA_MODULE ? ForgeOfGodsUpgrade.GPCI
                        // This pool includes magnetic matter, so its minimum complete host must also unlock that mode.
                        : supported == BottledChaosSupportedMachine.MTEEXOTIC_MODULE ? ForgeOfGodsUpgrade.EE : null;
            if (target == null) continue;
            try {
                Rule section = BottledChaosStructureCatalog.readRule(module, supported, 1, machinePools[id]);
                for (var entry : maximumPaths.entrySet()) {
                    EnumSet<ForgeOfGodsUpgrade> path = entry.getValue();
                    if (!path.contains(target)) continue;
                    int rings = 1 + (path.contains(ForgeOfGodsUpgrade.CD) ? 1 : 0)
                        + (path.contains(ForgeOfGodsUpgrade.END) ? 1 : 0);
                    Rule tower = towers.get(rings);
                    if (tower == null) {
                        tower = BottledChaosStructureCatalog
                            .readRule(host, BottledChaosSupportedMachine.MTEFORGE_OF_GODS, rings, 0);
                        towers.put(rings, tower);
                    }
                    registerGodforgeProfile(tower, section, module, path, entry.getKey(), machinePools[id], rules);
                }
            } catch (RuntimeException exception) {
                TwistSpaceTechnology.LOG.warn("Cannot cache complete Godforge module {}.", supported, exception);
            }
        }
    }

    private static void registerGodforgeProfile(Rule tower, Rule section, IMetaTileEntity module,
        EnumSet<ForgeOfGodsUpgrade> path, int profile, long pools, List<Rule> rules) {
        int rings = tower.profile();
        int modules = 8 + 4 * (rings - 1);
        Map<TST_ItemID, ItemStack> materials = fixedMaterials(tower);
        // Each installed module controller replaces exactly one host module-attachment casing.
        remove(materials, new ItemStack(TTCasingsContainer.GodforgeCasings, 1, 0), modules);
        for (Part part : section.parts())
            add(materials, part.material(), Math.multiplyExact(modules, part.material().stackSize));
        int shards = 0;
        for (ForgeOfGodsUpgrade upgrade : path) {
            shards = Math.addExact(shards, upgrade.getShardCost());
            if (upgrade == ForgeOfGodsUpgrade.START && !upgrade.hasExtraCost())
                throw new IllegalArgumentException("Godforge upgrade costs have not been registered");
            for (ItemStack cost : upgrade.getExtraCostNoNulls()) add(materials, cost, cost.stackSize);
        }
        if (shards > 0) add(materials, Materials.GravitonShard.getGems(1), shards);
        String key = tower.machineKey() + ":"
            + BottledChaosSupportedMachine.find(module)
                .name();
        List<Part> parts = new ArrayList<>();
        for (ItemStack item : materials.values()) parts.add(new Part(0, item));
        godforgeProfiles.computeIfAbsent(key, unused -> new LinkedHashMap<>())
            .put(profile, nativeGodforgeProfile(module, path, modules, rings));
        rules.add(new Rule(key, profile, pools, tower.controller(), parts.toArray(new Part[0]), 0, new Requirement[0]));
    }

    /** Native prerequisite/split checks select reachable upgrades without exceeding the host's ring count. */
    private static EnumSet<ForgeOfGodsUpgrade> maximumPath(int rings, Set<ForgeOfGodsUpgrade> excluded) {
        EnumSet<ForgeOfGodsUpgrade> path = EnumSet.noneOf(ForgeOfGodsUpgrade.class);
        ForgeOfGodsData data = new ForgeOfGodsData();
        boolean changed;
        do {
            changed = false;
            for (ForgeOfGodsUpgrade upgrade : ForgeOfGodsUpgrade.VALUES) {
                if (path.contains(upgrade) || excluded.contains(upgrade)
                    || upgrade == ForgeOfGodsUpgrade.CD && rings < 2
                    || upgrade == ForgeOfGodsUpgrade.END && rings < 3) continue;
                int availableBranches = 1 + (path.contains(ForgeOfGodsUpgrade.CD) ? 1 : 0)
                    + (path.contains(ForgeOfGodsUpgrade.END) ? 1 : 0);
                if (!data.getUpgrades()
                    .checkPrerequisites(upgrade)
                    || !data.getUpgrades()
                        .checkSplit(upgrade, availableBranches))
                    continue;
                data.getUpgrades()
                    .unlockUpgrade(upgrade);
                path.add(upgrade);
                changed = true;
            }
        } while (changed);
        if (!path.contains(
            rings == 3 ? ForgeOfGodsUpgrade.END : rings == 2 ? ForgeOfGodsUpgrade.CD : ForgeOfGodsUpgrade.START))
            throw new IllegalArgumentException("Incomplete Godforge ring upgrade path");
        return path;
    }

    private static GodforgeProfile nativeGodforgeProfile(IMetaTileEntity prototype, EnumSet<ForgeOfGodsUpgrade> path,
        int modules, int rings) {
        MTEBaseModule module = (MTEBaseModule) prototype.newMetaEntity(null);
        ForgeOfGodsData data = new ForgeOfGodsData();
        for (ForgeOfGodsUpgrade upgrade : path) data.getUpgrades()
            .unlockUpgrade(upgrade);
        data.setRingAmount(rings);
        data.setSelectedFuelType(2);
        data.setFuelConsumptionFactor(GodforgeMath.calculateMaxFuelFactor(data));
        data.setMaxBatteryCharge(Long.MAX_VALUE);
        data.setInternalBattery(data.getMaxBatteryCharge() / 2);
        GodforgeMath.calculateMaxHeatForModules(module, data);
        GodforgeMath.calculateMaxParallelForModules(module, data);
        GodforgeMath.calculateSpeedBonusForModules(module, data);
        GodforgeMath.calculateEnergyDiscountForModules(module, data);
        GodforgeMath.setMiscModuleParameters(module, data);
        return new GodforgeProfile(
            prototype.getStackForm(1),
            rings,
            modules,
            Math.multiplyExact((long) modules, module.getCalculatedMaxParallel()),
            module.getHeat(),
            module.getHeatForOC(),
            module.getEnergyDiscount(),
            module.getSpeedBonus(),
            module.getOverclockTimeFactor(),
            module.getHeatEnergyDiscount(),
            data.getFuelConsumptionFactor(),
            data.getMaxBatteryCharge(),
            Collections.unmodifiableSet(EnumSet.copyOf(path)),
            module.getPlasmaTier(),
            module.isMultiStepPlasma(),
            // The packaged host operates at half battery, without charging. Round before scaling host quantity.
            Math.toIntExact((long) Math.max(GodforgeMath.calculateFuelConsumption(data) * 5, 1)));
    }

    public static GodforgeProfile godforge(Structure structure) {
        if (structure == null) return null;
        Map<Integer, GodforgeProfile> profiles = godforgeProfiles.get(structure.machineKey());
        return profiles == null ? null : profiles.get(structure.profile());
    }

    static ItemStack parameterController(ItemStack controller, Structure structure) {
        GodforgeProfile preset = godforge(structure);
        return preset == null ? controller : preset.module();
    }

    static boolean isFoundry(Rule rule) {
        return BottledChaosSupportedMachine.find(ItemMachines.getMetaTileEntity(rule.controller()))
            == BottledChaosSupportedMachine.MTEEXO_FOUNDRY;
    }

    public static boolean isFoundry(Structure structure) {
        return structure != null
            && BottledChaosSupportedMachine.find(ItemMachines.getMetaTileEntity(structure.controller()))
                == BottledChaosSupportedMachine.MTEEXO_FOUNDRY;
    }

    static int foundryExampleCount(int tier) {
        return foundryChoices.get(tier)
            .size();
    }

    static Structure foundryExample(Rule rule, int variant) {
        Map<TST_ItemID, ItemStack> materials = fixedMaterials(rule);
        int[] counts = foundryChoices.get(rule.profile())
            .get(Math.floorMod(variant, foundryExampleCount(rule.profile())));
        addFoundryModules(materials, rule.profile(), counts);
        return resolveFoundry(
            rule,
            materials.values()
                .toArray(new ItemStack[0]));
    }

    static Structure resolveFoundry(Rule rule, ItemStack[] materials) {
        int[] counts = foundryModuleCounts(rule.profile(), materials);
        if (counts == null) return null;
        Map<TST_ItemID, ItemStack> expected = fixedMaterials(rule);
        addFoundryModules(expected, rule.profile(), counts);
        if (expected.size() != materials.length) return null;
        // The registered tower/module order is independent of the player's input order.
        ItemStack[] canonical = expected.values()
            .toArray(new ItemStack[0]);
        for (ItemStack item : materials) {
            if (item == null || item.stackSize <= 0) return null;
            ItemStack needed = expected.remove(TST_ItemID.create(item));
            if (needed == null || needed.stackSize != item.stackSize) return null;
        }
        if (!expected.isEmpty()) return null;
        return new Structure(rule.machineKey(), rule.profile(), rule.poolMask(), rule.controller(), canonical, 0);
    }

    private static int[] foundryModuleCounts(int tier, ItemStack[] materials) {
        ItemStack[][] costs = foundryCosts.get(tier);
        if (costs == null) return null;
        int[] counts = new int[FoundryModule.size()];
        int total = 0;
        for (FoundryModule module : FoundryModule.values()) {
            if (module == FoundryModule.UNSET) continue;
            ItemStack marker = module.getItemIcon();
            int perModule = 0;
            for (ItemStack cost : costs[module.ordinal()]) if (sameItem(cost, marker)) perModule = cost.stackSize;
            if (perModule <= 0) throw new IllegalStateException("No foundry module signature: " + module);
            long actual = 0;
            for (ItemStack item : materials) if (item != null && sameItem(item, marker)) actual += item.stackSize;
            if (actual < 0 || actual % perModule != 0 || actual / perModule > tier + 1) return null;
            int count = (int) (actual / perModule);
            if (uniqueModule(module) && count > 1) return null;
            counts[module.ordinal()] = count;
            total += count;
        }
        return total == tier + 1 ? counts : null;
    }

    private static void addFoundryModules(Map<TST_ItemID, ItemStack> materials, int tier, int[] counts) {
        ItemStack[][] costs = foundryCosts.get(tier);
        for (int i = 1; i < counts.length; i++) if (counts[i] > 0)
            for (ItemStack cost : costs[i]) add(materials, cost, Math.multiplyExact(counts[i], cost.stackSize));
    }

    /** Reuse native module/pairing parameters. Three/four HR modules use deterministic coefficient expectations. */
    public static FoundryData foundry(Structure structure) {
        if (!isFoundry(structure)) return null;
        int[] counts = foundryModuleCounts(structure.profile(), structure.materials());
        if (counts == null) return null;
        FoundryData data = new FoundryData();
        data.tier = structure.profile();
        int index = 0;
        for (int i = 1; i < counts.length; i++)
            for (int n = 0; n < counts[i]; n++) data.modules[index++] = FoundryModule.values()[i];
        int helio = counts[FoundryModule.HELIOCAST_REINFORCEMENT.ordinal()];
        if (helio < 3) data.checkSolidifierModules();
        else {
            // HR's own effect is boolean; removing HR #3/#4 prevents the native method from drawing random numbers.
            int retained = 0;
            for (int i = 0; i < data.modules.length; i++)
                if (data.modules[i] == FoundryModule.HELIOCAST_REINFORCEMENT && ++retained > 2)
                    data.modules[i] = FoundryModule.UNSET;
            data.checkSolidifierModules();
            double mean = helio / 2d;
            data.parallelScaleAdditive += (int) (8 * mean);
            data.speedAdditive += (float) mean;
            data.euEffAdditive -= (float) (0.15 * mean);
            data.ocFactorAdditive += (float) (0.05 * mean);
            data.parallelScaleAdj = (data.parallelScaleBase + data.parallelScaleAdditive)
                * data.parallelScaleMultiplier;
            data.speedModifierAdj = (data.speedModifierBase + data.speedAdditive) * data.speedMultiplier;
            data.euEffAdj = (data.euEffBase + data.euEffAdditive) * data.euEffMultiplier;
            data.isHRPairPresent = true;
        }
        return data;
    }

    public static boolean fourHeliocasts(Structure structure) {
        int[] counts = isFoundry(structure) ? foundryModuleCounts(structure.profile(), structure.materials()) : null;
        return counts != null && counts[FoundryModule.HELIOCAST_REINFORCEMENT.ordinal()] == 4;
    }

    static boolean foundryFits(Rule rule, int slots) {
        for (int[] counts : foundryChoices.get(rule.profile())) {
            Map<TST_ItemID, ItemStack> materials = new LinkedHashMap<>();
            addFoundryModules(materials, rule.profile(), counts);
            if (materials.size() > slots) return false;
        }
        return true;
    }

    static boolean hasGodforgeModule(Rule rule, ItemStack controller) {
        Map<Integer, GodforgeProfile> profiles = godforgeProfiles.get(rule.machineKey());
        GodforgeProfile preset = profiles == null ? null : profiles.get(rule.profile());
        return preset != null && sameItem(preset.module(), controller);
    }

    public static String godforgeUpgradeName(GodforgeProfile profile) {
        StringJoiner branches = new StringJoiner(" + ");
        // #tr tst.bottledchaos.recipe.structure.godforge_heat
        // # Heat
        // #zh_CN 热量
        if (profile.upgrades()
            .contains(ForgeOfGodsUpgrade.SEFCP))
            branches.add(StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.godforge_heat"));
        if (profile.upgrades()
            .contains(ForgeOfGodsUpgrade.TCT))
            branches.add(StatCollector.translateToLocal("tst.common.shared.general.word_parallel"));
        if (profile.upgrades()
            .contains(ForgeOfGodsUpgrade.GGEBE))
            branches.add(StatCollector.translateToLocal("tst.common.shared.general.word_overclock"));
        return branches.toString();
    }

    public static String foundryUpgradeName(Structure structure) {
        int[] counts = foundryModuleCounts(structure.profile(), structure.materials());
        StringJoiner modules = new StringJoiner(" + ");
        for (FoundryModule module : FoundryModule.values())
            if (counts[module.ordinal()] > 0) modules.add(module.shorthand + " x" + counts[module.ordinal()]);
        return modules.toString();
    }

    public static List<String> notes(Structure structure) {
        List<String> lines = new ArrayList<>();
        GodforgeProfile godforge = godforge(structure);
        if (godforge != null) {
            // #tr tst.bottledchaos.recipe.structure.godforge_modules
            // # Rings: %s
            // #zh_CN 环数: %s
            lines.add(
                StatCollector
                    .translateToLocalFormatted("tst.bottledchaos.recipe.structure.godforge_modules", godforge.rings()));
            // #tr tst.bottledchaos.recipe.structure.godforge_upgrades
            // # Upgrade configuration: %s; all available bonuses included
            // #zh_CN 升级配置: %s; 包含全部可用增益
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.recipe.structure.godforge_upgrades",
                    godforgeUpgradeName(godforge)));
            // #tr tst.bottledchaos.recipe.structure.godforge_maximum_operation
            // # Fuel: MHDCSM; factor: %s; battery: long, half-full
            // #zh_CN 燃料: MHDCSM; 系数: %s; 电池: long, 半满
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.recipe.structure.godforge_maximum_operation",
                    godforge.fuelFactor() == Integer.MAX_VALUE ? "int" : Integer.toString(godforge.fuelFactor())));
        }
        if (isFoundry(structure)) {
            // #tr tst.bottledchaos.recipe.structure.foundry_modules
            // # Modules: %s; upgrade configuration: %s
            // #zh_CN 模块数: %s; 升级配置: %s
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.recipe.structure.foundry_modules",
                    structure.profile() + 1,
                    foundryUpgradeName(structure)));
            // #tr tst.bottledchaos.recipe.structure.foundry_expectation
            // # Module and pairing bonuses included; random bonuses use expected values
            // #zh_CN 包含模块和组合增益; 随机增益取期望值
            lines.add(StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.foundry_expectation"));
        }
        return lines;
    }

    private static Map<TST_ItemID, ItemStack> fixedMaterials(Rule rule) {
        Map<TST_ItemID, ItemStack> materials = new LinkedHashMap<>();
        for (Part part : rule.parts()) {
            if (part.group() != 0) throw new IllegalArgumentException("Complex host contains an unspecified upgrade");
            add(materials, part.material(), part.material().stackSize);
        }
        return materials;
    }

    private static void add(Map<TST_ItemID, ItemStack> materials, ItemStack item, int quantity) {
        if (item == null || quantity <= 0) throw new IllegalArgumentException("Missing complete host material");
        ItemStack stored = materials.get(TST_ItemID.create(item));
        if (stored == null) {
            stored = item.copy();
            stored.stackSize = quantity;
            materials.put(TST_ItemID.create(item), stored);
        } else stored.stackSize = Math.addExact(stored.stackSize, quantity);
    }

    private static void remove(Map<TST_ItemID, ItemStack> materials, ItemStack item, int quantity) {
        ItemStack stored = materials.get(TST_ItemID.create(item));
        if (stored == null || stored.stackSize < quantity)
            throw new IllegalArgumentException("Missing attachment position");
        stored.stackSize -= quantity;
        if (stored.stackSize == 0) materials.remove(TST_ItemID.create(item));
    }

    private static boolean sameItem(ItemStack first, ItemStack second) {
        return first.isItemEqual(second) && ItemStack.areItemStackTagsEqual(first, second);
    }
}
