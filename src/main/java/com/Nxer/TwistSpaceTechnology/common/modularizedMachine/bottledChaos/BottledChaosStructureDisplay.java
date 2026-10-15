package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureCatalog.Rule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructureCatalog.Structure;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;

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
        return rules.isEmpty() ? Integer.MAX_VALUE
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
        var data = ItemBottledChaosMachineModule.assembly(frame.outputs()[0]);
        return BottledChaosStructureCatalog.resolve(
            data.getString("machine"),
            data.getInteger("profile"),
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

    public Frame frame(int cycle) {
        if (staticFrame != null) return staticFrame;
        int profileIndex = Math.floorMod(cycle, rules.size());
        Rule rule = rules.get(profileIndex);
        int variant = Math.floorDiv(cycle, rules.size());
        var structure = rule.example(variant);
        if (structure == null) {
            variant = 0;
            structure = rule.example();
        }
        if (structure == null) throw new IllegalStateException("Invalid structure display: " + rule.machineKey());
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
            orderVariableMaterials(rule, variant, variable),
            GTCMItemList.BottledChaosMachineModule.get(1),
            new ItemStack[] { ItemBottledChaosMachineModule.createDisplay(structure) },
            rule.profile(),
            structure);
    }

    /** Keep upgrade groups in stable slots even when a replacement has a different registry item ID. */
    private static ItemStack[] orderVariableMaterials(Rule rule, int variant, List<ItemStack> variable) {
        Map<TST_ItemID, ItemStack> remaining = new LinkedHashMap<>();
        for (ItemStack material : variable) remaining.put(TST_ItemID.create(material), material);
        List<ItemStack> ordered = new ArrayList<>();
        for (int group = 1; group <= rule.groups(); group++) {
            List<BottledChaosStructureCatalog.Part> alternatives = new ArrayList<>();
            for (var part : rule.parts()) if (part.group() == group) alternatives.add(part);
            ItemStack material = remaining.remove(
                TST_ItemID.create(
                    alternatives.get(Math.floorMod(variant, alternatives.size()))
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
