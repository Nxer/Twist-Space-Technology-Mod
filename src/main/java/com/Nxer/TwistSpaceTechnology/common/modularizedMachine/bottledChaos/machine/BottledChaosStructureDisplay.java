package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.IntUnaryOperator;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Rule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.GlassTier;
import gregtech.common.blocks.ItemMachines;

/** One NEI entry per structure or single-block pool. Independent choices resolve to one complete configuration. */
public final class BottledChaosStructureDisplay {

    public static final RecipeMetadataKey<BottledChaosStructureDisplay> DISPLAY = SimpleRecipeMetadataKey
        .create(BottledChaosStructureDisplay.class, "bottled_chaos_structure_display");

    private final List<Rule> rules;
    private final Map<TST_ItemID, ItemStack> fixedMaterials;
    private final Frame staticFrame;

    /** Both material regions and the output describe the same validated one-set configuration. */
    @Desugar
    public record Frame(ItemStack[] fixed, ItemStack[] variable, ItemStack carrier, ItemStack[] outputs, int profile,
        Structure structure) {

        public ItemStack[] inputs() {
            ItemStack[] result = new ItemStack[fixed.length + variable.length + 1];
            System.arraycopy(fixed, 0, result, 0, fixed.length);
            System.arraycopy(variable, 0, result, fixed.length, variable.length);
            result[result.length - 1] = carrier;
            return result;
        }
    }

    public BottledChaosStructureDisplay(List<Rule> rules) {
        this.rules = Collections.unmodifiableList(new ArrayList<>(rules));
        staticFrame = null;
        fixedMaterials = fixedParts(rules.get(0));
        for (Rule rule : rules) {
            Map<TST_ItemID, ItemStack> fixed = fixedParts(rule);
            fixedMaterials.entrySet()
                .removeIf(
                    entry -> !fixed.containsKey(entry.getKey())
                        || fixed.get(entry.getKey()).stackSize != entry.getValue().stackSize);
        }
        // Even a baseline casing count changes when an optional hatch or upgrade replaces some of its positions.
        fixedMaterials.entrySet()
            .removeIf(
                entry -> rules.stream()
                    .anyMatch(rule -> isVariable(rule, entry.getValue())));
    }

    /** Disk loading and unloading use the same layout without animated structure choices. */
    public BottledChaosStructureDisplay(Frame frame) {
        rules = Collections.emptyList();
        fixedMaterials = Collections.emptyMap();
        staticFrame = frame;
    }

    public int poolOrder() {
        // Disk loading/unloading examples precede every machine pool.
        return rules.isEmpty() ? -1
            : Long.numberOfTrailingZeros(
                rules.get(0)
                    .poolMask());
    }

    public String poolNames() {
        long mask = 0;
        for (Rule rule : rules) mask |= rule.poolMask();
        if (staticFrame != null) for (ItemStack item : staticFrame.inputs()) {
            var structure = ItemBottledChaosMachineModule.structure(item);
            if (structure != null) mask |= structure.poolMask();
        }
        if (staticFrame != null) for (ItemStack item : staticFrame.outputs()) {
            var structure = ItemBottledChaosMachineModule.structure(item);
            if (structure != null) mask |= structure.poolMask();
        }
        StringJoiner names = new StringJoiner(", ");
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++)
            if ((mask & (1L << slot)) != 0) names.add(BottledChaosMachineConfiguration.poolName(slot));
        return names.toString();
    }

    /** Validate the selected upgrade combination before updating the output or exporting a pattern. */
    public BottledChaosStructureCatalog.Structure resolve(Frame frame, ItemStack[] selected) {
        if (rules.isEmpty()) return null;
        Map<TST_ItemID, ItemStack> materials = new LinkedHashMap<>();
        for (ItemStack[] region : new ItemStack[][] { frame.fixed(), selected }) for (ItemStack item : region) {
            if (item.stackSize <= 0) continue;
            ItemStack material = materials.get(TST_ItemID.create(item));
            if (material == null) materials.put(TST_ItemID.create(item), item.copy());
            else material.stackSize += item.stackSize;
        }
        ItemStack module = frame.outputs()[0];
        return BottledChaosStructureCatalog.resolve(
            ItemBottledChaosMachineModule.machineKey(module),
            ItemBottledChaosMachineModule.profile(module),
            materials.values()
                .toArray(new ItemStack[0]));
    }

    /** Lookup covers every controller in a grouped recipe, independently of the currently visible frame. */
    public boolean hasController(ItemStack controller) {
        for (Rule rule : rules) if (rule.controller()
            .isItemEqual(controller) || BottledChaosComplexStructures.hasGodforgeModule(rule, controller)) return true;
        return false;
    }

    public boolean hasMachine(String key) {
        for (Rule rule : rules) if (rule.machineKey()
            .equals(key)) return true;
        return false;
    }

    /** Bound every animated frame before registration, including optional components absent from the baseline. */
    public boolean fits(int regionSlots) {
        if (staticFrame != null)
            return staticFrame.fixed().length <= regionSlots && staticFrame.variable().length <= regionSlots;
        if (fixedMaterials.size() > regionSlots) return false;
        for (Rule rule : rules) {
            if (BottledChaosComplexStructures.isFoundry(rule)
                && !BottledChaosComplexStructures.foundryFits(rule, regionSlots - 1)) return false;
            Set<TST_ItemID> variable = new HashSet<>();
            for (var part : rule.parts()) {
                if (part.group() == 0 && !fixedMaterials.containsKey(TST_ItemID.create(part.material())))
                    variable.add(TST_ItemID.create(part.material()));
            }
            int count = variable.size() + rule.groups();
            for (var requirement : rule.requirements()) {
                if (requirement.alternatives().length > 0
                    && !fixedMaterials.containsKey(TST_ItemID.create(requirement.alternatives()[0]))) count++;
            }
            if (count > regionSlots) return false;
        }
        return true;
    }

    public Frame frame() {
        return frame(new LinkedHashMap<>());
    }

    /** A viewer owns only its choices; registered examples and other viewers remain untouched. */
    public Frame frame(Map<String, Integer> choices) {
        if (staticFrame != null) return staticFrame;
        Rule rule = selectedRule(choices);
        Structure structure = rule.example(
            group -> choices.getOrDefault("group" + group, 0),
            index -> choices.getOrDefault(rule.requirements()[index].name(), 0));
        if (structure == null) return null;
        // Native cross-tier requirements may raise glass to match a source. Show and retain the actual selection.
        for (int group = 1; group <= rule.groups(); group++) {
            int index = 0;
            for (var part : rule.parts()) if (part.group() == group) {
                for (ItemStack material : structure.materials()) if (material.isItemEqual(part.material())) {
                    choices.put("group" + group, index);
                    break;
                }
                index++;
            }
        }
        return frame(rule, structure, group -> choices.getOrDefault("group" + group, 0));
    }

    @Desugar
    public record Selection(String key, String name, int count, int offset, String detail) {

        public String text(int index) {
            return name + ": " + (index + offset);
        }
    }

    public List<Selection> selections(Map<String, Integer> choices) {
        if (staticFrame != null) return Collections.emptyList();
        Rule rule = selectedRule(choices);
        List<Selection> result = new ArrayList<>();
        // #tr tst.bottledchaos.recipe.structure.selection
        // # Tier
        // #zh_CN 等级

        // #tr tst.bottledchaos.recipe.structure.layers
        // # Layers
        // #zh_CN 层数
        if (rules.size() > 1) {
            var machine = BottledChaosSupportedMachine.find(ItemMachines.getMetaTileEntity(rule.controller()));
            result.add(
                new Selection(
                    "profile",
                    StatCollector.translateToLocal(
                        machine != null && machine.hasRepeatingPiece() ? "tst.bottledchaos.recipe.structure.layers"
                            : "tst.bottledchaos.recipe.structure.selection"),
                    rules.size(),
                    1,
                    rule.controller()
                        .getDisplayName()));
        }
        if (BottledChaosComplexStructures.isFoundry(rule)) {
            result.add(
                new Selection(
                    "group0",
                    parameterName("modules"),
                    BottledChaosComplexStructures.foundryExampleCount(rule.profile()),
                    1,
                    BottledChaosComplexStructures.foundryUpgradeName(
                        BottledChaosComplexStructures.foundryExample(rule, choices.getOrDefault("group0", 0)))));
        }
        for (int group = 1; group <= rule.groups(); group++) {
            List<ItemStack> alternatives = new ArrayList<>();
            for (var part : rule.parts()) if (part.group() == group) alternatives.add(part.material());
            if (alternatives.size() <= 1) continue;
            String key = "group" + group;
            ItemStack selected = alternatives.get(Math.floorMod(choices.getOrDefault(key, 0), alternatives.size()));
            result.add(
                new Selection(
                    key,
                    groupName(alternatives.get(0), group),
                    alternatives.size(),
                    1,
                    selected.getDisplayName()));
        }
        for (var requirement : rule.requirements()) {
            boolean optional = requirement.minimum() == 0 && requirement.maximum() == 1;
            int count = requirement.alternatives().length + (optional ? 1 : 0);
            if (count <= 1) continue;
            int selected = Math.floorMod(choices.getOrDefault(requirement.name(), 0), count) - (optional ? 1 : 0);
            String detail = selected < 0 ? "" : requirement.alternatives()[selected].getDisplayName();
            result.add(
                new Selection(requirement.name(), parameterName(requirement.name()), count, optional ? 0 : 1, detail));
        }
        return result;
    }

    private static String groupName(ItemStack material, int group) {
        Block block = Block.getBlockFromItem(material.getItem());
        if (block instanceof IHeatingCoil) return parameterName("coil");
        if (GlassTier.getGlassBlockTier(block, material.getItemDamage()) != null) return parameterName("glass");
        // Reuse the existing structural parameter reader instead of duplicating block/meta classification.
        var upgrades = new BottledChaosMachineParameters.Upgrades(new ItemStack[] { material });
        if (upgrades.field >= 0 || upgrades.ballField >= 0) return parameterName("field");
        if (upgrades.fusion >= 0) return parameterName("fusion_coil");
        if (upgrades.itemPipe >= 0) return parameterName("item_pipe");
        if (upgrades.fluidPipe >= 0) return parameterName("fluid_pipe");
        if (upgrades.solenoid >= 0) return parameterName("solenoid");
        if (upgrades.component >= 0) return parameterName("component_casing");
        if (upgrades.precision >= 0) return parameterName("precision_casing");
        if (upgrades.machineCasing >= 0) return parameterName("machine_casing");
        if (upgrades.solidCasing >= 0) return parameterName("solid_casing");
        if (upgrades.core >= 0) return parameterName("core");
        return parameterName("material") + " " + group;
    }

    private static String parameterName(String name) {
        // #tr tst.bottledchaos.recipe.structure.parameter.modules
        // # Modules
        // #zh_CN 模组

        // #tr tst.bottledchaos.recipe.structure.parameter.coil
        // # Coil
        // #zh_CN 线圈

        // #tr tst.bottledchaos.recipe.structure.parameter.glass
        // # Glass
        // #zh_CN 玻璃

        // #tr tst.bottledchaos.recipe.structure.parameter.field
        // # Field generator
        // #zh_CN 力场

        // #tr tst.bottledchaos.recipe.structure.parameter.fusion_coil
        // # Fusion coil
        // #zh_CN 聚变线圈

        // #tr tst.bottledchaos.recipe.structure.parameter.item_pipe
        // # Item pipe
        // #zh_CN 物品管道

        // #tr tst.bottledchaos.recipe.structure.parameter.fluid_pipe
        // # Fluid pipe
        // #zh_CN 流体管道

        // #tr tst.bottledchaos.recipe.structure.parameter.solenoid
        // # Solenoid
        // #zh_CN 螺线管

        // #tr tst.bottledchaos.recipe.structure.parameter.component_casing
        // # Component casing
        // #zh_CN 部件机壳

        // #tr tst.bottledchaos.recipe.structure.parameter.precision_casing
        // # Precision casing
        // #zh_CN 精密机壳

        // #tr tst.bottledchaos.recipe.structure.parameter.machine_casing
        // # Machine casing
        // #zh_CN 机器机壳

        // #tr tst.bottledchaos.recipe.structure.parameter.solid_casing
        // # Solid casing
        // #zh_CN 坚固机壳

        // #tr tst.bottledchaos.recipe.structure.parameter.core
        // # Core
        // #zh_CN 核心

        // #tr tst.bottledchaos.recipe.structure.parameter.material
        // # Material
        // #zh_CN 部件

        // #tr tst.bottledchaos.recipe.structure.parameter.sawblade
        // # Sawblade
        // #zh_CN 锯片

        // #tr tst.bottledchaos.recipe.structure.parameter.ingot_mold
        // # Ingot mold
        // #zh_CN 锭模具

        // #tr tst.bottledchaos.recipe.structure.parameter.rotors
        // # Rotor
        // #zh_CN 转子

        // #tr tst.bottledchaos.recipe.structure.parameter.electromagnet
        // # Electromagnet
        // #zh_CN 电磁铁

        // #tr tst.bottledchaos.recipe.structure.parameter.laser_source
        // # Laser source
        // #zh_CN 激光源

        // #tr tst.bottledchaos.recipe.structure.parameter.electrode
        // # Electrode
        // #zh_CN 电极

        // #tr tst.bottledchaos.recipe.structure.parameter.photon_upgrades
        // # Photon upgrade
        // #zh_CN 光子升级

        String key = "tst.bottledchaos.recipe.structure.parameter." + name;
        String text = StatCollector.translateToLocal(key);
        return text.equals(key) ? StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.parameter.material")
            : text;
    }

    private Rule selectedRule(Map<String, Integer> choices) {
        return rules.get(Math.floorMod(choices.getOrDefault("profile", 0), rules.size()));
    }

    private Frame frame(Rule rule, Structure structure, IntUnaryOperator groupChoice) {
        List<ItemStack> fixed = new ArrayList<>();
        for (ItemStack material : fixedMaterials.values()) fixed.add(material.copy());
        List<ItemStack> variable = new ArrayList<>();
        for (ItemStack material : structure.materials()) {
            ItemStack base = fixedMaterials.get(TST_ItemID.create(material));
            if (base == null) variable.add(material.copy());
            else if (BottledChaosComplexStructures.isFoundry(rule) && material.stackSize > base.stackSize) {
                fixed.removeIf(
                    item -> TST_ItemID.create(item)
                        .equals(TST_ItemID.create(material)));
                variable.add(material.copy());
            }
        }
        return new Frame(
            fixed.toArray(new ItemStack[0]),
            orderVariableMaterials(rule, groupChoice, variable),
            GTCMItemList.BottledChaosMachineModule.get(1),
            new ItemStack[] { ItemBottledChaosMachineModule.create(structure, 1) },
            rule.profile(),
            structure);
    }

    /** Keep upgrade groups in stable slots even when a replacement has a different registry item ID. */
    private static ItemStack[] orderVariableMaterials(Rule rule, IntUnaryOperator groupChoice,
        List<ItemStack> variable) {
        Map<TST_ItemID, ItemStack> remaining = new LinkedHashMap<>();
        for (ItemStack material : variable) remaining.put(TST_ItemID.create(material), material);
        List<ItemStack> ordered = new ArrayList<>();
        for (int group = 1; group <= rule.groups(); group++) {
            List<BottledChaosStructureCatalog.Part> alternatives = new ArrayList<>();
            for (var part : rule.parts()) if (part.group() == group) alternatives.add(part);
            ItemStack material = remaining.remove(
                TST_ItemID.create(
                    alternatives.get(Math.floorMod(groupChoice.applyAsInt(group), alternatives.size()))
                        .material()));
            // Cross-tier rules can raise a selected glass variant to the minimum required by its source.
            if (material == null) for (var part : alternatives) {
                material = remaining.remove(TST_ItemID.create(part.material()));
                if (material != null) break;
            }
            if (material != null) ordered.add(material);
        }
        for (var requirement : rule.requirements()) {
            var iterator = remaining.values()
                .iterator();
            while (iterator.hasNext()) {
                ItemStack material = iterator.next();
                if (!requirement.accepts(material)) continue;
                ordered.add(material);
                iterator.remove();
            }
        }
        for (var part : rule.parts()) {
            ItemStack material = remaining.remove(TST_ItemID.create(part.material()));
            if (material != null) ordered.add(material);
        }
        ordered.addAll(remaining.values());
        return ordered.toArray(new ItemStack[0]);
    }

    private static Map<TST_ItemID, ItemStack> fixedParts(Rule rule) {
        Map<TST_ItemID, ItemStack> fixed = new LinkedHashMap<>();
        if (BottledChaosComplexStructures.isFoundry(rule)) {
            for (var part : rule.parts()) fixed.put(
                TST_ItemID.create(part.material()),
                part.material()
                    .copy());
            return fixed;
        }
        for (ItemStack material : rule.example()
            .materials()) fixed.put(TST_ItemID.create(material), material.copy());
        return fixed;
    }

    private static boolean isVariable(Rule rule, ItemStack material) {
        for (var part : rule.parts()) {
            if (part.group() > 0 && TST_ItemID.create(part.material())
                .equals(TST_ItemID.create(material))) return true;
        }
        for (var requirement : rule.requirements()) {
            if ((requirement.minimum() != requirement.maximum() || requirement.alternatives().length > 1)
                && (requirement.accepts(material) || requirement.replaces(material))) return true;
        }
        return false;
    }
}
