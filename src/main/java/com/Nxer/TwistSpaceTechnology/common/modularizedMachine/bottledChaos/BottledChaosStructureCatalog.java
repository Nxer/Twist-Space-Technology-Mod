package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.Mods;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GlassTier;
import gregtech.common.GTDummyWorld;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import kubatech.loaders.ArcFurnaceElectrode;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

/** Loading-only rules. Runtime matching never constructs a machine or scans its world. */
public final class BottledChaosStructureCatalog {

    private static final List<Rule> rules = new ArrayList<>();
    private static final List<Repeat> repeats = new ArrayList<>();
    private static final Set<RecipeMap<?>> heatedPools = new HashSet<>();

    /** Paid configuration: one controller, exact one-set materials and fixed base coil temperature. */
    @Desugar
    public record Structure(String machineKey, int profile, long poolMask, ItemStack controller, ItemStack[] materials,
        long heat) {

        public NBTTagList materialTag() {
            NBTTagList list = new NBTTagList();
            for (ItemStack material : materials) {
                ItemStack template = material.copy();
                template.stackSize = 1;
                NBTTagCompound tag = template.writeToNBT(new NBTTagCompound());
                tag.setInteger("amount", material.stackSize);
                list.appendTag(tag);
            }
            return list;
        }

        public boolean sameConfiguration(Structure other) {
            return other != null && machineKey.equals(other.machineKey)
                && profile == other.profile
                && materialTag().equals(other.materialTag());
        }
    }

    /** Flat choices: group 0 is fixed; each positive group selects exactly one homogeneous material variant. */
    @Desugar
    public record Part(int group, ItemStack material) {}

    public enum MaterialKind {
        EXACT,
        ROTOR,
        ELECTRODE
    }

    /** A real inventory item or dedicated hatch, with an explicit quantity and legal casing substitutions. */
    @Desugar
    public record Requirement(String name, ItemStack[] alternatives, int minimum, int maximum, boolean mixed,
        ItemStack[] replacements, MaterialKind kind) {

        boolean accepts(ItemStack material) {
            if (kind == MaterialKind.ROTOR) {
                if (!(material.getItem() instanceof MetaGeneratedTool01 tool) || material.getItemDamage() < 170
                    || material.getItemDamage() > 179) return false;
                var stats = tool.getToolStats(material);
                Materials primary = MetaGeneratedTool.getPrimaryMaterial(material);
                return stats != null && stats.getSpeedMultiplier() > 0 && primary != null && primary.mToolSpeed > 0;
            }
            if (kind == MaterialKind.ELECTRODE) return ArcFurnaceElectrode.getByStack(material) != null;
            for (ItemStack alternative : alternatives) if (sameItem(material, alternative)) return true;
            return false;
        }

        boolean replaces(ItemStack material) {
            for (ItemStack replacement : replacements) if (sameItem(material, replacement)) return true;
            return false;
        }
    }

    /** One complete size. Independent upgrades and mixed inventories never create a Cartesian-product cache. */
    @Desugar
    public record Rule(String machineKey, int profile, long poolMask, ItemStack controller, Part[] parts, int groups,
        Requirement[] requirements) {

        public Structure example() {
            return example(0);
        }

        /** Number of steps needed to show every choice at least once. */
        public int exampleCount() {
            if (BottledChaosComplexStructures.isFoundry(this))
                return BottledChaosComplexStructures.foundryExampleCount(profile);
            int count = 1;
            for (int group = 1; group <= groups; group++) {
                int alternatives = 0;
                for (Part part : parts) if (part.group == group) alternatives++;
                count = Math.max(count, alternatives);
            }
            for (Requirement requirement : requirements) {
                count = Math.max(count, requirement.alternatives.length);
            }
            return count;
        }

        public Structure example(int variant) {
            if (BottledChaosComplexStructures.isFoundry(this))
                return BottledChaosComplexStructures.foundryExample(this, variant);
            Map<TST_ItemID, ItemStack> materials = new LinkedHashMap<>();
            for (Part part : parts) if (part.group == 0) addMaterial(materials, part.material, part.material.stackSize);
            // Every independent group advances on the same clock, with its own period.
            for (int group = 1; group <= groups; group++) {
                List<Part> alternatives = new ArrayList<>();
                for (Part part : parts) if (part.group == group) alternatives.add(part);
                Part selected = alternatives.get(Math.floorMod(variant, alternatives.size()));
                addMaterial(materials, selected.material, selected.material.stackSize);
            }
            for (int i = 0; i < requirements.length; i++) {
                Requirement requirement = requirements[i];
                int amount = requirement.minimum;
                if (amount == 0) continue;
                if (requirement.alternatives.length == 0) return null;
                addMaterial(
                    materials,
                    requirement.alternatives[Math.floorMod(variant, requirement.alternatives.length)],
                    amount);
                if (requirement.replacements.length > 0) {
                    ItemStack replaced = null;
                    for (ItemStack replacement : requirement.replacements) {
                        ItemStack available = materials.get(TST_ItemID.create(replacement));
                        if (available != null && available.stackSize >= amount) {
                            replaced = available.copy();
                            replaced.stackSize = amount;
                            break;
                        }
                    }
                    if (replaced == null || !removeMaterial(materials, replaced)) return null;
                }
            }
            // A source upgrade also needs matching glass; examples must show a legal complete combination.
            if (BottledChaosSupportedMachine.find(ItemMachines.getMetaTileEntity(controller))
                == BottledChaosSupportedMachine.MTEINDUSTRIAL_LASER_ENGRAVER) matchLaserGlass(this, materials);
            return resolve(
                this,
                materials.values()
                    .toArray(new ItemStack[0]));
        }

        /** Amount shown for a casing choice, after the minimum mandatory hatch count replaces its positions. */
        public int displayAmount(Part part) {
            int amount = part.material.stackSize;
            for (Requirement requirement : requirements)
                if (requirement.replaces(part.material)) amount -= requirement.minimum;
            return amount;
        }
    }

    /** Repeated-piece arithmetic is cached once; runtime resolution uses only flat material counts. */
    @Desugar
    private record Repeat(Rule base, Part[] increments, int maximum) {

        Rule at(int profile) {
            if (profile < base.profile || profile > maximum) return null;
            Part[] parts = new Part[base.parts.length];
            try {
                for (int i = 0; i < parts.length; i++) {
                    ItemStack material = base.parts[i].material.copy();
                    material.stackSize = Math.addExact(
                        material.stackSize,
                        Math.multiplyExact(profile - base.profile, increments[i].material.stackSize));
                    parts[i] = new Part(base.parts[i].group, material);
                }
            } catch (ArithmeticException exception) {
                return null;
            }
            return new Rule(
                base.machineKey,
                profile,
                base.poolMask,
                base.controller,
                parts,
                base.groups,
                base.requirements);
        }

        int infer(ItemStack[] materials) {
            for (int i = 0; i < increments.length; i++) {
                Part increment = increments[i];
                if (increment.group != 0 || increment.material.stackSize <= 0) continue;
                boolean substitutable = false;
                for (Requirement requirement : base.requirements)
                    if (requirement.replaces(increment.material)) substitutable = true;
                if (substitutable) continue;
                for (ItemStack material : materials) if (sameItem(material, increment.material)) {
                    int extra = material.stackSize - base.parts[i].material.stackSize;
                    if (extra >= 0 && extra % increment.material.stackSize == 0)
                        return base.profile + extra / increment.material.stackSize;
                }
            }
            return -1;
        }
    }

    public static void initialize(long[] machinePools) {
        rules.clear();
        repeats.clear();
        heatedPools.clear();
        BottledChaosComplexStructures.clear();
        // Read heat requirements once at loading; coil-based upgrades alone do not imply recipe heat limits.
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++) {
            RecipeMap<?> pool = BottledChaosMachineConfiguration.pool(slot);
            if (pool != null && pool.getAllRecipes()
                .stream()
                .anyMatch(
                    recipe -> recipe.getMetadataOrDefault(GTRecipeConstants.COIL_HEAT, 0) > 0
                        || (pool == RecipeMaps.blastFurnaceRecipes || pool == RecipeMaps.vacuumFurnaceRecipes)
                            && recipe.mSpecialValue > 0))
                heatedPools.add(pool);
        }
        for (int id = 0; id < machinePools.length; id++) {
            IMetaTileEntity machine = GregTechAPI.METATILEENTITIES[id];
            if (machinePools[id] == 0 || machine == null) continue;
            if (machine instanceof MTEPurificationPlant) {
                rules.add(readPlant(machine, machinePools[id]));
                continue;
            }
            if (machine instanceof MTEBasicMachine basic && basic.isElectric()) {
                // Electrical single-block machines pay only their actual registered controller; its tier remains the
                // limit.
                ItemStack controller = machine.getStackForm(1);
                rules.add(
                    new Rule(
                        controller.getUnlocalizedName(),
                        1,
                        machinePools[id],
                        controller,
                        new Part[] { new Part(0, controller.copy()) },
                        0,
                        new Requirement[0]));
                continue;
            }
            var supported = BottledChaosSupportedMachine.find(machine);
            if (supported == null || !(machine instanceof MTEEnhancedMultiBlockBase<?>)) continue;
            try {
                long pools = supported == BottledChaosSupportedMachine.MTEINDUSTRIAL_ARC_FURNACE
                    ? machinePools[id]
                        & BottledChaosMachineConfiguration.mask(gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes)
                    : machinePools[id];
                if (pools == 0) continue;
                if (BottledChaosComplexStructures.register(machine, supported, pools, rules)) continue;
                int[] profiles = supported.displayProfiles();
                if (profiles.length == 0) continue;
                if (supported.hasRepeatingPiece()) {
                    Rule base = readRule(machine, supported, profiles[0], pools);
                    Rule next = readRule(machine, supported, profiles[0] + 1, pools);
                    Part[] increments = new Part[base.parts.length];
                    if (base.parts.length != next.parts.length || base.groups != next.groups)
                        throw new IllegalArgumentException("Repeated structure changes material identities");
                    for (int i = 0; i < increments.length; i++) {
                        if (base.parts[i].group != next.parts[i].group
                            || !sameItem(base.parts[i].material, next.parts[i].material))
                            throw new IllegalArgumentException("Repeated structure changes upgrade groups");
                        ItemStack increment = base.parts[i].material.copy();
                        increment.stackSize = next.parts[i].material.stackSize - increment.stackSize;
                        if (increment.stackSize < 0)
                            throw new IllegalArgumentException("Negative repeat material count");
                        increments[i] = new Part(base.parts[i].group, increment);
                    }
                    Repeat repeat = new Repeat(
                        base,
                        increments,
                        supported == BottledChaosSupportedMachine.MTENEUTRON_ACTIVATOR ? 249 : Integer.MAX_VALUE);
                    repeats.add(repeat);
                    for (int profile : profiles) rules.add(repeat.at(profile));
                } else for (int profile : profiles) rules.add(readRule(machine, supported, profile, pools));
            } catch (RuntimeException exception) {
                TwistSpaceTechnology.LOG
                    .warn("Cannot cache complete Bottled Chaos structure {}.", supported, exception);
            }
        }
        BottledChaosComplexStructures.registerGodforge(machinePools, rules);
        TwistSpaceTechnology.LOG
            .info("Cached {} Bottled Chaos structure rules with independent upgrades.", rules.size());
    }

    public static boolean isController(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemMachines)) return false;
        IMetaTileEntity machine = ItemMachines.getMetaTileEntity(stack);
        return machine instanceof MTEEnhancedMultiBlockBase<?>
            || machine instanceof MTEBasicMachine basic && basic.isElectric();
    }

    public static Collection<Rule> displayRules() {
        return Collections.unmodifiableList(rules);
    }

    public static boolean requiresRecipeHeat(RecipeMap<?> pool) {
        return heatedPools.contains(pool);
    }

    public static boolean isAuxiliaryController(ItemStack stack) {
        return isController(stack) && ItemMachines.getMetaTileEntity(stack) instanceof MTEPurificationPlant;
    }

    public static Structure match(ItemStack controller, ItemStack[] materials) {
        List<Structure> matches = matches(controller, materials);
        return matches.isEmpty() ? null : matches.get(0);
    }

    /** Identical paid materials may authorize several distinct upgrade configurations. */
    public static List<Structure> matches(ItemStack controller, ItemStack[] materials) {
        List<Structure> matches = new ArrayList<>();
        if (!isController(controller)) return matches;
        for (Rule rule : rules) if (rule.controller.isItemEqual(controller)) {
            Structure structure = resolve(rule, materials);
            if (structure != null) matches.add(structure);
        }
        for (Repeat repeat : repeats) if (repeat.base.controller.isItemEqual(controller)) {
            Rule rule = repeat.at(repeat.infer(materials));
            if (rule != null) {
                Structure structure = resolve(rule, materials);
                if (structure != null && matches.stream()
                    .noneMatch(structure::sameConfiguration)) matches.add(structure);
            }
        }
        return matches;
    }

    public static Structure resolve(String machineKey, int profile, ItemStack[] materials) {
        for (Rule rule : rules)
            if (rule.machineKey.equals(machineKey) && rule.profile == profile) return resolve(rule, materials);
        for (Repeat repeat : repeats) if (repeat.base.machineKey.equals(machineKey)) {
            Rule rule = repeat.at(profile);
            if (rule != null) return resolve(rule, materials);
        }
        return null;
    }

    private static Structure resolve(Rule rule, ItemStack[] materials) {
        if (BottledChaosComplexStructures.isFoundry(rule))
            return BottledChaosComplexStructures.resolveFoundry(rule, materials);
        Map<TST_ItemID, ItemStack> remaining = new LinkedHashMap<>();
        for (ItemStack material : materials) {
            if (material == null || material.stackSize <= 0) return null;
            if (remaining.containsKey(TST_ItemID.create(material))) return null;
            addMaterial(remaining, material, material.stackSize);
        }
        int[] replaced = new int[rule.requirements.length];
        for (int i = 0; i < rule.requirements.length; i++) {
            Requirement requirement = rule.requirements[i];
            long quantity = 0;
            int types = 0;
            Iterator<ItemStack> iterator = remaining.values()
                .iterator();
            while (iterator.hasNext()) {
                ItemStack material = iterator.next();
                if (!requirement.accepts(material)) continue;
                quantity += material.stackSize;
                types++;
                iterator.remove();
            }
            if (quantity < requirement.minimum || quantity > requirement.maximum || !requirement.mixed && types > 1)
                return null;
            if (requirement.replacements.length > 0) replaced[i] = (int) quantity;
        }
        Map<TST_ItemID, ItemStack> missing = new LinkedHashMap<>();
        for (Part part : rule.parts) if (part.group == 0) consumePart(remaining, missing, part.material);
        int substitutions = Arrays.stream(replaced)
            .sum();
        if (total(missing) > substitutions || !matchGroups(rule, 1, remaining, missing, replaced, substitutions))
            return null;
        // Preserve actual rotor/electrode NBT and quantities; ordering is stable for comparison and exact unloading.
        Map<TST_ItemID, ItemStack> ordered = new LinkedHashMap<>();
        for (Part part : rule.parts) for (ItemStack material : materials)
            if (sameItem(material, part.material)) ordered.putIfAbsent(TST_ItemID.create(material), material.copy());
        for (Requirement requirement : rule.requirements) for (ItemStack material : materials)
            if (requirement.accepts(material)) ordered.putIfAbsent(TST_ItemID.create(material), material.copy());
        long heat = 0;
        for (ItemStack material : ordered.values())
            if (Block.getBlockFromItem(material.getItem()) instanceof IHeatingCoil coil) heat = Math.max(
                heat,
                coil.getCoilHeat(material.getItemDamage())
                    .getHeat());
        if (rule.profile > 16 && BottledChaosSupportedMachine.find(ItemMachines.getMetaTileEntity(rule.controller))
            == BottledChaosSupportedMachine.MTEINDUSTRIAL_COKE_OVEN) {
            boolean eternal = false;
            for (ItemStack material : ordered.values())
                if (Block.getBlockFromItem(material.getItem()) instanceof IHeatingCoil coil
                    && coil.getCoilHeat(material.getItemDamage())
                        .getTier() > HeatingCoilLevel.MAX.getTier())
                    eternal = true;
            if (!eternal) return null;
        }
        List<ItemStack> canonical = new ArrayList<>(ordered.values());
        canonical.sort(
            Comparator.comparingInt((ItemStack item) -> Item.getIdFromItem(item.getItem()))
                .thenComparingInt(ItemStack::getItemDamage)
                .thenComparing(
                    item -> item.hasTagCompound() ? item.getTagCompound()
                        .toString() : ""));
        ItemStack[] actualMaterials = canonical.toArray(new ItemStack[0]);
        if (!BottledChaosMachineParameters.isValidStructure(rule.controller, actualMaterials)) return null;
        return new Structure(rule.machineKey, rule.profile, rule.poolMask, rule.controller, actualMaterials, heat);
    }

    private static void matchLaserGlass(Rule rule, Map<TST_ItemID, ItemStack> materials) {
        int laserTier = -1;
        for (ItemStack material : materials.values()) {
            IMetaTileEntity hatch = material.getItem() instanceof ItemMachines
                ? ItemMachines.getMetaTileEntity(material)
                : null;
            if (hatch instanceof MTEHatchDynamoTunnel laser) laserTier = (int) laser.getOutputTier();
        }
        for (ItemStack material : materials.values()) {
            Integer tier = GlassTier
                .getGlassBlockTier(Block.getBlockFromItem(material.getItem()), material.getItemDamage());
            if (tier != null && tier >= laserTier) return;
        }
        for (Part part : rule.parts) {
            Integer glassTier = GlassTier
                .getGlassBlockTier(Block.getBlockFromItem(part.material.getItem()), part.material.getItemDamage());
            if (glassTier == null || glassTier < laserTier) continue;
            for (Part current : rule.parts) {
                if (current.group != part.group || !materials.containsKey(TST_ItemID.create(current.material)))
                    continue;
                materials.remove(TST_ItemID.create(current.material));
                addMaterial(materials, part.material, part.material.stackSize);
                return;
            }
        }
    }

    private static void consumePart(Map<TST_ItemID, ItemStack> remaining, Map<TST_ItemID, ItemStack> missing,
        ItemStack needed) {
        TST_ItemID id = TST_ItemID.create(needed);
        ItemStack available = remaining.get(id);
        int taken = available == null ? 0 : Math.min(available.stackSize, needed.stackSize);
        if (taken > 0) {
            available.stackSize -= taken;
            if (available.stackSize == 0) remaining.remove(id);
        }
        if (taken < needed.stackSize) addMaterial(missing, needed, needed.stackSize - taken);
    }

    private static long total(Map<TST_ItemID, ItemStack> materials) {
        long total = 0;
        for (ItemStack material : materials.values()) total += material.stackSize;
        return total;
    }

    /** Missing casing positions must be exactly covered by the hatches which are legal in those positions. */
    private static boolean matchGroups(Rule rule, int group, Map<TST_ItemID, ItemStack> remaining,
        Map<TST_ItemID, ItemStack> missing, int[] replaced, int substitutions) {
        if (group > rule.groups) return remaining.isEmpty() && total(missing) == substitutions
            && matchSubstitutions(rule, missing, replaced);
        for (Part part : rule.parts) {
            if (part.group != group) continue;
            Map<TST_ItemID, ItemStack> next = copyMaterials(remaining), gaps = copyMaterials(missing);
            consumePart(next, gaps, part.material);
            if (total(gaps) <= substitutions && matchGroups(rule, group + 1, next, gaps, replaced, substitutions))
                return true;
        }
        return false;
    }

    /** Small bipartite capacity graph, including modules which may use either of two different casing areas. */
    private static boolean matchSubstitutions(Rule rule, Map<TST_ItemID, ItemStack> missing, int[] replaced) {
        List<ItemStack> gaps = new ArrayList<>(missing.values());
        for (ItemStack gap : gaps) {
            int legalPositions = 0;
            for (Requirement requirement : rule.requirements) for (ItemStack replacement : requirement.replacements)
                if (sameItem(gap, replacement)) legalPositions = Math.max(legalPositions, replacement.stackSize);
            if (gap.stackSize > legalPositions) return false;
        }
        int requirementCount = replaced.length, sink = requirementCount + gaps.size() + 1;
        int[][] capacity = new int[sink + 1][sink + 1];
        for (int i = 0; i < requirementCount; i++) {
            capacity[0][i + 1] = replaced[i];
            for (int j = 0; j < gaps.size(); j++)
                if (rule.requirements[i].replaces(gaps.get(j))) capacity[i + 1][requirementCount + j + 1] = replaced[i];
        }
        for (int j = 0; j < gaps.size(); j++) capacity[requirementCount + j + 1][sink] = gaps.get(j).stackSize;
        int flow = 0;
        while (true) {
            int[] previous = new int[sink + 1];
            Arrays.fill(previous, -1);
            previous[0] = 0;
            int[] queue = new int[sink + 1];
            int end = 1;
            for (int head = 0; head < end && previous[sink] < 0; head++) {
                int from = queue[head];
                for (int to = 1; to <= sink; to++) if (previous[to] < 0 && capacity[from][to] > 0) {
                    previous[to] = from;
                    queue[end++] = to;
                }
            }
            if (previous[sink] < 0) return flow == total(missing);
            int amount = Integer.MAX_VALUE;
            for (int to = sink; to != 0; to = previous[to]) amount = Math.min(amount, capacity[previous[to]][to]);
            for (int to = sink; to != 0; to = previous[to]) {
                capacity[previous[to]][to] -= amount;
                capacity[to][previous[to]] += amount;
            }
            flow = Math.addExact(flow, amount);
        }
    }

    private static Map<TST_ItemID, ItemStack> copyMaterials(Map<TST_ItemID, ItemStack> materials) {
        Map<TST_ItemID, ItemStack> copy = new LinkedHashMap<>();
        for (var entry : materials.entrySet()) copy.put(
            entry.getKey(),
            entry.getValue()
                .copy());
        return copy;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static Rule readRule(IMetaTileEntity machine, BottledChaosSupportedMachine supported, int shape, long pools) {
        return readRule(machine, supported, shape, pools, supported.pieces(machine, shape));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static Rule readRule(IMetaTileEntity machine, BottledChaosSupportedMachine supported, int shape, long pools,
        List<BottledChaosSupportedMachine.Piece> pieces) {
        ItemStack controller = machine.getStackForm(1);
        MaterialWorld world = new MaterialWorld();
        BaseMetaTileEntity base = new BaseMetaTileEntity();
        base.setWorldObj(world);
        base.yCoord = 128;
        MTEEnhancedMultiBlockBase context = (MTEEnhancedMultiBlockBase) machine.newMetaEntity(base);
        context.setBaseMetaTileEntity(base);
        base.setMetaTileEntity(context);
        base.setMetaTileID((short) controller.getItemDamage());
        Map<Position, IStructureElement> positions = new LinkedHashMap<>();
        for (var piece : pieces) {
            int a = -piece.a(), b = -piece.b(), c = -piece.c();
            for (IStructureElement element : (IStructureElement[]) context.getStructureDefinition()
                .getStructureFor(piece.name())) {
                if (element.isNavigating()) {
                    a = (element.resetA() ? -piece.a() : a) + element.getStepA();
                    b = (element.resetB() ? -piece.b() : b) + element.getStepB();
                    c = (element.resetC() ? -piece.c() : c) + element.getStepC();
                } else positions.put(new Position(a++, b, c), element);
            }
        }
        Map<IStructureElement, Integer> counts = new LinkedHashMap<>();
        for (IStructureElement element : positions.values()) {
            if (element != StructureUtility.isAir()) counts.merge(element, 1, Integer::sum);
        }
        List<Part> parts = new ArrayList<>();
        parts.add(new Part(0, controller.copy()));
        int groups = 0;
        if (supported == BottledChaosSupportedMachine.MTELARGE_CHEMICAL_REACTOR) {
            // Five eligible positions in the original shape, but exactly one coil per complete LCR.
            parts.add(new Part(0, new ItemStack(GregTechAPI.sBlockCasings8, 24, 0)));
            parts.add(new Part(0, new ItemStack(GregTechAPI.sBlockCasings8, 1, 1)));
            List<ItemStack> coils = new ArrayList<>();
            for (IStructureElement element : counts.keySet())
                for (ItemStack choice : choices(context, world, element, controller, supported))
                    if (Block.getBlockFromItem(choice.getItem()) instanceof IHeatingCoil) addChoice(coils, choice);
            if (coils.isEmpty()) throw new IllegalArgumentException("No LCR coil variants");
            groups = appendParts(parts, coils, 1, groups);
        } else for (var entry : counts.entrySet()) {
            List<ItemStack> alternatives = choices(context, world, entry.getKey(), controller, supported);
            if (supported == BottledChaosSupportedMachine.MTEINDUSTRIAL_MACERATOR) {
                Casings selected = shape == 2 ? Casings.MacerationStackCasing : Casings.StableTitaniumMachineCasing;
                Casings other = shape == 2 ? Casings.StableTitaniumMachineCasing : Casings.MacerationStackCasing;
                alternatives.removeIf(item -> sameItem(item, casing(other, 1)));
                if (alternatives.stream()
                    .anyMatch(item -> sameItem(item, casing(selected, 1))) && alternatives.size() != 1)
                    throw new IllegalArgumentException("Mixed macerator casing tiers");
            }
            groups = appendParts(parts, alternatives, entry.getValue(), groups);
        }
        if (supported == BottledChaosSupportedMachine.MTEDISTILLATION_TOWER) groups = appendParts(
            parts,
            Collections.singletonList(new ItemStack(GregTechAPI.sBlockCasings4, 1, 1)),
            1,
            groups);
        // Pure ordinary hatch positions have no casing fallback in the original element; pay their casing here.
        switch (supported) {
            case MTEPURIFICATION_UNIT_PH_ADJUSTMENT -> groups = appendParts(
                parts,
                Collections.singletonList(new ItemStack(GregTechAPI.sBlockCasings9, 1, 8)),
                2,
                groups);
            case MTEPURIFICATION_UNIT_PLASMA_HEATER -> {
                groups = appendParts(
                    parts,
                    Collections.singletonList(new ItemStack(GregTechAPI.sBlockCasings9, 1, 5)),
                    1,
                    groups);
                groups = appendParts(
                    parts,
                    Collections.singletonList(new ItemStack(GregTechAPI.sBlockCasings9, 1, 11)),
                    1,
                    groups);
            }
            case MTEELECTRIC_BLAST_FURNACE -> groups = appendParts(
                parts,
                Collections.singletonList(casing(Casings.HeatProofMachineCasing, 1)),
                1,
                groups);
            case MTEMULTI_FURNACE -> groups = appendParts(
                parts,
                Collections.singletonList(new ItemStack(GregTechAPI.sBlockCasings1, 1, 11)),
                1,
                groups);
            case MTEMEGA_ALLOY_BLAST_SMELTER -> groups = appendParts(
                parts,
                Collections.singletonList(new ItemStack(ModBlocks.blockCasingsMisc, 1, 15)),
                1,
                groups);
            case MTEEXOTHERMIC_HEARTH -> groups = appendParts(
                parts,
                Collections.singletonList(casing(Casings.StructuralCokeOvenCasing, 1)),
                1,
                groups);
            default -> {}
        }
        List<Requirement> requirements = dedicatedMaterials(supported, shape);
        groups = moveMixedPhotonUpgrades(supported, parts, requirements, groups);
        if (parts.size() <= 1) throw new IllegalArgumentException("No structure materials");
        return new Rule(
            controller.getUnlocalizedName(),
            shape,
            pools,
            controller,
            parts.toArray(new Part[0]),
            groups,
            requirements.toArray(new Requirement[0]));
    }

    /** Readable per-machine additions to the material counts obtained from their public structure definitions. */
    private static List<Requirement> dedicatedMaterials(BottledChaosSupportedMachine supported, int profile) {
        List<Requirement> requirements = new ArrayList<>();
        switch (supported) {
            case MTESPINMATRON -> {
                // All four tiers have eight real Rotor Assemblies. Controller inventory has 2/4/6/8 rotor slots.
                require(
                    requirements,
                    "rotor_assemblies",
                    8,
                    8,
                    false,
                    new ItemStack[] { GregtechItemList.Hatch_Turbine_Rotor.get(1) });
                requirements.add(
                    new Requirement(
                        "rotors",
                        rotorExamples(),
                        profile * 2,
                        profile * 2,
                        true,
                        new ItemStack[0],
                        MaterialKind.ROTOR));
            }
            case MTEINDUSTRIAL_ELECTROMAGNETIC_SEPARATOR -> {
                // One exclusive housing, and exactly one of the five registered electromagnets inside it.
                require(
                    requirements,
                    "electromagnet_housing",
                    1,
                    1,
                    false,
                    new ItemStack[] { ItemList.Hatch_Electromagnet.get(1) });
                require(
                    requirements,
                    "electromagnet",
                    1,
                    1,
                    false,
                    items(
                        ItemList.Electromagnet_Iron,
                        ItemList.Electromagnet_Steel,
                        ItemList.Electromagnet_Neodymium,
                        ItemList.Electromagnet_Samarium,
                        ItemList.Electromagnet_Tengam));
            }
            case MTEINDUSTRIAL_LASER_ENGRAVER -> {
                // Laser source is a dedicated structural upgrade, with only the highest current at each voltage tier.
                require(requirements, "laser_source", 1, 1, false, laserSources());
            }
            case MTEISA_MILL -> {
                // Exactly one Ball Housing substitutes one of the 50 milling casing positions.
                require(
                    requirements,
                    "ball_housing",
                    1,
                    1,
                    false,
                    new ItemStack[] { GregtechItemList.Bus_Milling_Balls.get(1) },
                    new ItemStack(ModBlocks.blockCasings5Misc, 1, 0));
            }
            case MTECRYOGENIC_FREEZER -> {
                // One fixed cooling hatch substitutes an advanced cryogenic casing position.
                require(
                    requirements,
                    "cryotheum_hatch",
                    1,
                    1,
                    false,
                    new ItemStack[] { GregtechItemList.Hatch_Input_Cryotheum.get(1) },
                    casing(Casings.AdvancedCryogenicCasing, 12));
            }
            case MTEADV_EBF -> {
                // One fixed heating hatch substitutes a Volcanus casing position.
                require(
                    requirements,
                    "pyrotheum_hatch",
                    1,
                    1,
                    false,
                    new ItemStack[] { GregtechItemList.Hatch_Input_Pyrotheum.get(1) },
                    new ItemStack(ModBlocks.blockCasings3Misc, 6, 11));
            }
            case MTEQUANTUM_FORCE_TRANSFORMER -> {
                // The lower 36 positions hold inputs, power and catalyst housings; recipe catalysts stay dynamic
                // inputs.
                require(
                    requirements,
                    "bulk_catalyst_housing",
                    1,
                    1,
                    false,
                    new ItemStack[] { ItemList.Hatch_Catalyst_Bulk.get(1) },
                    new ItemStack(ModBlocks.blockCasings2Misc, 33, 12));
            }
            case MTECHEMICAL_PLANT -> {
                // One fixed Catalyst Housing substitutes a homogeneous solid casing position.
                require(
                    requirements,
                    "chemical_catalyst_housing",
                    1,
                    1,
                    false,
                    new ItemStack[] { GregtechItemList.Bus_Catalysts.get(1) },
                    solidChemicalCasings());
            }
            case MTEINDUSTRIAL_ARC_FURNACE -> {
                // Only the arc-furnace recipe pool is authorized. Electrode and detector housings occupy steel casings.
                require(
                    requirements,
                    "electrode_hatch",
                    1,
                    1,
                    false,
                    new ItemStack[] { kubatech.api.enums.ItemList.ElectrodeHatch.get(1) },
                    casing(Casings.SolidSteelMachineCasing, 162));
                require(
                    requirements,
                    "electrode_detector",
                    1,
                    1,
                    false,
                    new ItemStack[] { kubatech.api.enums.ItemList.ElectrodeDetectorHatch.get(1) },
                    casing(Casings.SolidSteelMachineCasing, 162));
                List<ItemStack> electrodes = new ArrayList<>();
                for (ArcFurnaceElectrode electrode : ArcFurnaceElectrode.values())
                    addActualChoice(electrodes, electrode.getElectrodeItem(1));
                requirements.add(
                    new Requirement(
                        "electrode",
                        electrodes.toArray(new ItemStack[0]),
                        1,
                        1,
                        false,
                        new ItemStack[0],
                        MaterialKind.ELECTRODE));
            }
            case MTEBIO_VAT, TST_BIOSPHERE_III -> {
                // One radiation hatch and one fixed Naquadria Singularity. This dedicated source is adjustable.
                require(
                    requirements,
                    "radiation_hatch",
                    1,
                    1,
                    false,
                    new ItemStack[] { registeredMachine(MetaTileEntityIDs.RadioHatch_HV) },
                    new ItemStack(GregTechAPI.sBlockCasings4, 1, 1));
                require(requirements, "naquadria_singularity", 1, 1, false, new ItemStack[] { naquadriaSingularity() });
            }
            case MTEPURIFICATION_UNIT_DEGASSER -> {
                // Exactly one Degasifier Control Hatch; it substitutes a heater casing.
                require(
                    requirements,
                    "degasifier_control",
                    1,
                    1,
                    false,
                    new ItemStack[] { ItemList.Hatch_DegasifierControl.get(1) },
                    new ItemStack(GregTechAPI.sBlockCasings9, 1, 11));
            }
            case MTEPURIFICATION_UNIT_UVTREATMENT -> {
                // The lens housing is a pure bus position; the indicator replaces one main casing.
                require(
                    requirements,
                    "lens_housing",
                    1,
                    1,
                    false,
                    new ItemStack[] { ItemList.Hatch_LensHousing.get(1) });
                require(
                    requirements,
                    "lens_indicator",
                    1,
                    1,
                    false,
                    new ItemStack[] { ItemList.Hatch_LensIndicator.get(1) },
                    new ItemStack(GregTechAPI.sBlockCasings9, 1, 12));
            }
            case MTEPURIFICATION_UNIT_PH_ADJUSTMENT -> {
                // Ordinary acid/alkali inputs pay casings; the dedicated pH sensor is a fixed input.
                require(
                    requirements,
                    "ph_sensor",
                    1,
                    1,
                    false,
                    new ItemStack[] { ItemList.Hatch_pHSensor.get(1) },
                    new ItemStack(GregTechAPI.sBlockCasings9, 2, 7));
            }
            case MTENEUTRON_ACTIVATOR -> {
                // Preserve the ordinary activator alongside the electric TST oscillator. Bottom edge has 15 positions.
                require(
                    requirements,
                    "neutron_accelerator",
                    1,
                    1,
                    false,
                    new ItemStack[] { goodgenerator.loader.Loaders.NeutronAccelerators[VoltageIndex.UMV] },
                    new ItemStack(GregTechAPI.sBlockCasings4, 13, 1));
                require(
                    requirements,
                    "neutron_sensor",
                    1,
                    1,
                    false,
                    new ItemStack[] { goodgenerator.loader.Loaders.NS },
                    new ItemStack(GregTechAPI.sBlockCasings4, 13, 1));
            }
            case MTEMASS_SOLIDIFIER -> {
                // Fixed highest-tier solidifier housing substitutes one shared casing position.
                require(
                    requirements,
                    "solidifier_hatches",
                    1,
                    1,
                    false,
                    new ItemStack[] { GregtechItemList.Hatch_Solidifier_IV.get(1) },
                    new ItemStack(GregTechAPI.sBlockCasings10, 49, 13));
            }
            case TST_SWELEGFYR_BLAST_FURNACE -> {
                // The blaze input is an ordinary hatch and is already replaced by its casing fallback.
                if (profile == 2) require(
                    requirements,
                    "tier_two_chip",
                    1,
                    1,
                    false,
                    new ItemStack[] { GTCMItemList.SwelegfyrUpgradeChip.get(1) });
            }
            case MTEINDUSTRIAL_MACERATOR -> {
                if (profile == 2) require(
                    requirements,
                    "maceration_chip",
                    1,
                    1,
                    false,
                    new ItemStack[] { GregtechItemList.Maceration_Upgrade_Chip.get(1) });
            }
            default -> {}
        }
        return requirements;
    }

    private static boolean hasPureHatch(BottledChaosSupportedMachine supported) {
        return switch (supported) {
            case MTESPINMATRON, MTEINDUSTRIAL_ELECTROMAGNETIC_SEPARATOR, MTEINDUSTRIAL_LASER_ENGRAVER, MTEPURIFICATION_UNIT_PH_ADJUSTMENT, MTEPURIFICATION_UNIT_PLASMA_HEATER, MTEPURIFICATION_UNIT_UVTREATMENT, MTEELECTRIC_BLAST_FURNACE, MTEEXOTHERMIC_HEARTH, MTEMULTI_FURNACE, MTEMEGA_ALLOY_BLAST_SMELTER -> true;
            default -> false;
        };
    }

    /** The central plant is fixed and small; list its complete blocks explicitly instead of another discovery scan. */
    private static Rule readPlant(IMetaTileEntity plant, long pools) {
        if (!(plant instanceof MTEPurificationPlant))
            throw new IllegalArgumentException("Missing purification plant controller");
        List<Part> parts = new ArrayList<>();
        parts.add(new Part(0, ItemList.Machine_Multi_PurificationPlant.get(1)));
        parts.add(new Part(0, new ItemStack(GregTechAPI.sBlockCasings9, 56, 3))); // Concrete
        parts.add(new Part(0, new ItemStack(GregTechAPI.sBlockCasings9, 74, 4))); // 71 sterile + three shared service
                                                                                  // positions
        parts.add(new Part(0, new ItemStack(GregTechAPI.sBlockCasings9, 77, 5))); // Reinforced sterile
        parts.add(
            new Part(
                0,
                gregtech.api.util.GTOreDictUnificator
                    .get(gregtech.api.enums.OrePrefixes.frameGt, Materials.Tungsten, 30)));
        for (int color = 0; color < 16; color++)
            parts.add(new Part(1, new ItemStack(GregTechAPI.sBlockTintedGlass, 6, color)));
        // Four water/air cells carry no item material. Standard service positions use the host's ordinary hatch
        // services.
        return new Rule(
            plant.getStackForm(1)
                .getUnlocalizedName(),
            1,
            pools,
            plant.getStackForm(1),
            parts.toArray(new Part[0]),
            1,
            new Requirement[0]);
    }

    private static int moveMixedPhotonUpgrades(BottledChaosSupportedMachine supported, List<Part> parts,
        List<Requirement> requirements, int groups) {
        if (supported != BottledChaosSupportedMachine.GT_TILE_ENTITY_PRECISE_HIGH_ENERGY_PHOTONIC_QUANTUM_MASTER)
            return groups;
        // The original machine permits different Photon Controller Upgrades at each position, including meta 11.
        List<ItemStack> variants = new ArrayList<>();
        int removedGroup = 0, amount = 0;
        for (Part part : parts)
            if (Block.getBlockFromItem(part.material.getItem()) == TstBlocks.PhotonControllerUpgrade) {
                addActualChoice(variants, part.material);
                removedGroup = part.group;
                amount = part.material.stackSize;
            }
        if (variants.isEmpty() || removedGroup == 0) return groups;
        final int group = removedGroup;
        parts.removeIf(part -> part.group == group);
        for (int i = 0; i < parts.size(); i++) {
            Part part = parts.get(i);
            if (part.group > group) parts.set(i, new Part(part.group - 1, part.material));
        }
        require(requirements, "photon_upgrades", amount, amount, true, variants.toArray(new ItemStack[0]));
        return groups - 1;
    }

    private static void require(List<Requirement> requirements, String name, int minimum, int maximum, boolean mixed,
        ItemStack[] alternatives, ItemStack... replacements) {
        List<ItemStack> valid = new ArrayList<>();
        for (ItemStack alternative : alternatives) addActualChoice(valid, alternative);
        if (minimum > 0 && valid.isEmpty()) throw new IllegalArgumentException("Missing dedicated material " + name);
        requirements.add(
            new Requirement(
                name,
                valid.toArray(new ItemStack[0]),
                minimum,
                maximum,
                mixed,
                replacements,
                MaterialKind.EXACT));
    }

    private static void addActualChoice(List<ItemStack> choices, ItemStack stack) {
        if (stack == null || stack.getItem() == null) return;
        for (ItemStack choice : choices) if (sameItem(choice, stack)) return;
        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        choices.add(copy);
    }

    private static ItemStack casing(Casings casing, int positions) {
        return new ItemStack(casing.getBlock(), positions, casing.getBlockMeta());
    }

    private static ItemStack[] items(ItemList... entries) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemList entry : entries) if (entry.hasBeenSet()) addActualChoice(items, entry.get(1));
        return items.toArray(new ItemStack[0]);
    }

    private static ItemStack[] laserSources() {
        // Legendary and 16,777,216A sources share UXV; only Legendary is the maximum-current choice.
        CustomItemList[] sources = { CustomItemList.eM_dynamoTunnel1_IV, CustomItemList.eM_dynamoTunnel2_LuV,
            CustomItemList.eM_dynamoTunnel3_ZPM, CustomItemList.eM_dynamoTunnel4_UV,
            CustomItemList.eM_dynamoTunnel5_UHV, CustomItemList.eM_dynamoTunnel6_UEV,
            CustomItemList.eM_dynamoTunnel7_UIV, CustomItemList.eM_dynamoTunnel8_UMV,
            CustomItemList.eM_dynamoTunnel9001 };
        List<ItemStack> items = new ArrayList<>();
        for (CustomItemList source : sources) if (source.hasBeenSet()) addActualChoice(items, source.get(1));
        return items.toArray(new ItemStack[0]);
    }

    private static ItemStack[] rotorExamples() {
        return new ItemStack[] {
            MetaGeneratedTool01.INSTANCE
                .getToolWithStats(IDMetaTool01.TURBINE_HUGE.ID, 1, Materials.Steel, Materials.Steel, null),
            MetaGeneratedTool01.INSTANCE
                .getToolWithStats(IDMetaTool01.TURBINE_LARGE.ID, 1, Materials.Steel, Materials.Steel, null),
            MetaGeneratedTool01.INSTANCE
                .getToolWithStats(IDMetaTool01.TURBINE.ID, 1, Materials.Steel, Materials.Steel, null),
            MetaGeneratedTool01.INSTANCE
                .getToolWithStats(IDMetaTool01.TURBINE_SMALL.ID, 1, Materials.Steel, Materials.Steel, null) };
    }

    private static ItemStack[] solidChemicalCasings() {
        // GregtechAlgaeContent registers these eight casing tiers in this exact order, 0..7.
        return new ItemStack[] { new ItemStack(ModBlocks.blockCustomMachineCasings, 1, 0),
            new ItemStack(GregTechAPI.sBlockCasings2, 1, 0), new ItemStack(ModBlocks.blockCustomMachineCasings, 1, 1),
            new ItemStack(GregTechAPI.sBlockCasings4, 1, 1), new ItemStack(GregTechAPI.sBlockCasings4, 1, 2),
            new ItemStack(GregTechAPI.sBlockCasings4, 1, 0), new ItemStack(ModBlocks.blockCustomMachineCasings, 1, 2),
            new ItemStack(ModBlocks.blockCustomMachineCasings, 1, 3) };
    }

    private static ItemStack registeredMachine(MetaTileEntityIDs id) {
        IMetaTileEntity registered = GregTechAPI.METATILEENTITIES[id.ID];
        if (registered == null) throw new IllegalArgumentException("Missing dedicated machine " + id);
        return registered.getStackForm(1);
    }

    private static ItemStack naquadriaSingularity() {
        return ModItem.getModItem(Mods.UniversalSingularities.ID, "universal.general.singularity", 1, 26);
    }

    /** Identical alternatives share one homogeneous group; different upgrade types stay independent. */
    private static int appendParts(List<Part> parts, List<ItemStack> choices, int amount, int groups) {
        if (choices.isEmpty()) return groups;
        if (choices.size() == 1) {
            for (Part part : parts) if (part.group == 0 && sameItem(part.material, choices.get(0))) {
                part.material.stackSize = Math.addExact(part.material.stackSize, amount);
                return groups;
            }
        } else for (int group = 1; group <= groups; group++) {
            int matched = 0, count = 0;
            for (Part part : parts) if (part.group == group) {
                count++;
                for (ItemStack choice : choices) if (sameItem(part.material, choice)) matched++;
            }
            if (matched != choices.size() || count != choices.size()) continue;
            for (Part part : parts)
                if (part.group == group) part.material.stackSize = Math.addExact(part.material.stackSize, amount);
            return groups;
        }
        int group = choices.size() == 1 ? 0 : ++groups;
        for (ItemStack choice : choices) {
            choice.stackSize = amount;
            parts.add(new Part(group, choice));
        }
        return groups;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<ItemStack> choices(Object context, MaterialWorld world, IStructureElement element,
        ItemStack controller, BottledChaosSupportedMachine supported) {
        List<ItemStack> choices = new ArrayList<>();
        world.block = null;
        if (element.check(context, world, 0, 128, 0)) return choices;
        ItemStack trigger = controller.copy();
        boolean fluidPosition = false;
        for (int tier = 1; tier <= 16; tier++) {
            trigger.stackSize = tier;
            var candidates = element.getBlocksToPlace(context, world, 0, 128, 0, trigger, null);
            if (candidates != null && candidates.getStacks() != null)
                for (Object candidate : candidates.getStacks()) addChoice(choices, (ItemStack) candidate);
            world.block = null;
            element.placeBlock(context, world, 0, 128, 0, trigger);
            if (world.block != null && Block.getBlockFromItem(world.block.getItem())
                .getMaterial()
                .isLiquid()) fluidPosition = true;
            addChoice(choices, world.block);
        }
        choices.removeIf(stack -> {
            Block block = Block.getBlockFromItem(stack.getItem());
            if (supported == BottledChaosSupportedMachine.TST_BALL_LIGHTNING && block instanceof IHeatingCoil coil)
                return coil.getCoilHeat(stack.getItemDamage())
                    .getTier() < HeatingCoilLevel.UMV.getTier();
            Integer glassTier = GlassTier.getGlassBlockTier(block, stack.getItemDamage());
            return supported == BottledChaosSupportedMachine.MTEPRECISE_ASSEMBLER && glassTier != null
                && glassTier < VoltageIndex.EV;
        });
        if (choices.isEmpty() && !fluidPosition && !hasPureHatch(supported)) throw new IllegalArgumentException(
            "No explicit material for " + element.getClass()
                .getName());
        return choices;
    }

    private static void addChoice(List<ItemStack> choices, ItemStack stack) {
        if (stack == null || stack.getItem() == null
            || stack.getItem() instanceof ItemMachines
            || Block.getBlockFromItem(stack.getItem()) == Blocks.air
            || Block.getBlockFromItem(stack.getItem()) != null && Block.getBlockFromItem(stack.getItem())
                .getMaterial()
                .isLiquid())
            return;
        for (ItemStack choice : choices) if (sameItem(choice, stack)) return;
        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        choices.add(copy);
    }

    private static boolean sameItem(ItemStack first, ItemStack second) {
        return first.isItemEqual(second) && ItemStack.areItemStackTagsEqual(first, second);
    }

    private static boolean removeMaterial(Map<TST_ItemID, ItemStack> materials, ItemStack needed) {
        TST_ItemID id = TST_ItemID.create(needed);
        ItemStack available = materials.get(id);
        if (available == null || available.stackSize < needed.stackSize) return false;
        available.stackSize -= needed.stackSize;
        if (available.stackSize == 0) materials.remove(id);
        return true;
    }

    private static void addMaterial(Map<TST_ItemID, ItemStack> materials, ItemStack stack, int amount) {
        TST_ItemID id = TST_ItemID.create(stack);
        ItemStack existing = materials.get(id);
        if (existing == null) {
            existing = stack.copy();
            existing.stackSize = 0;
            materials.put(id, existing);
        }
        existing.stackSize = Math.addExact(existing.stackSize, amount);
    }

    /** Loading-only coordinate deduplication, discarded after publishing the flat material rule. */
    @Desugar
    private record Position(int a, int b, int c) {}

    /** One scratch block for public material accessors; no machine construction, chunks or events. */
    private static final class MaterialWorld extends GTDummyWorld {

        ItemStack block;

        @Override
        public boolean blockExists(int x, int y, int z) {
            return true;
        }

        @Override
        public boolean setBlock(int x, int y, int z, Block value, int meta, int flags) {
            block = value == Blocks.air ? null : new ItemStack(value, 1, meta);
            return true;
        }

        @Override
        public Block getBlock(int x, int y, int z) {
            return block == null ? Blocks.air : Block.getBlockFromItem(block.getItem());
        }

        @Override
        public int getBlockMetadata(int x, int y, int z) {
            return block == null ? 0 : block.getItemDamage();
        }
    }
}
