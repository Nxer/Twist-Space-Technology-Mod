package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe.RecipeItemInput;

/** Native GT matching rules, kept with each prepared route independently of its material identity. */
final class BottledChaosItemInputs {

    static final class Requirement {

        final TST_ItemID key;
        final long amount;
        final List<RecipeItemInput> alternatives;

        Requirement(TST_ItemID key, long amount, List<RecipeItemInput> alternatives) {
            this.key = key;
            this.amount = amount;
            this.alternatives = Collections.unmodifiableList(new ArrayList<>(alternatives));
        }

        boolean matches(ItemData association, ItemStack stack) {
            for (RecipeItemInput alternative : alternatives) {
                if (alternative.matchesRecipe(association, stack)) return true;
            }
            return false;
        }

        Requirement amount(long value) {
            return new Requirement(this, value);
        }

        private Requirement(Requirement source, long amount) {
            key = source.key;
            this.amount = amount;
            alternatives = source.alternatives;
        }

        Requirement withAlternative(Requirement other) {
            if (!key.equals(other.key) || amount != other.amount)
                throw new IllegalStateException("incompatible native input alternatives");
            List<RecipeItemInput> combined = new ArrayList<>(alternatives);
            combined.addAll(other.alternatives);
            return new Requirement(key, amount, combined);
        }

        boolean sameRules(Requirement other) {
            for (RecipeItemInput left : alternatives) {
                boolean found = false;
                for (RecipeItemInput right : other.alternatives) {
                    // Ore slots also carry private static alternatives; only the same native matcher proves equality.
                    if (left == right || left.oreDictId < 0 && right.oreDictId < 0
                        && left.oreDictId == right.oreDictId
                        && left.usesNbtMatching == right.usesNbtMatching
                        && TST_ItemID.create(left.unifiedStack)
                            .equals(TST_ItemID.create(right.unifiedStack))) {
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
            return true;
        }
    }

    /** Reuse native rule matches while slot identities stay equal; every round keeps its own quantities. */
    static final class Matching {

        final ItemStack[] items;
        private final ItemData[] associations;
        private final TST_ItemID[] identities;
        private final boolean[] valid;
        private final Map<List<RecipeItemInput>, boolean[]> slots;
        private final Map<List<RecipeItemInput>, Boolean> canonical;
        private List<Requirement> combinedRequirements;
        private Map<TST_ItemID, boolean[]> combinedSlots;

        Matching(ItemStack[] provided) {
            this(provided, null);
        }

        Matching(ItemStack[] provided, Matching previous) {
            items = provided == null ? new ItemStack[0] : provided;
            if (previous != null && previous.sameSlots(items)) {
                associations = previous.associations;
                identities = previous.identities;
                valid = previous.valid;
                slots = previous.slots;
                canonical = previous.canonical;
                combinedRequirements = previous.combinedRequirements;
                combinedSlots = previous.combinedSlots;
            } else {
                associations = new ItemData[items.length];
                identities = new TST_ItemID[items.length];
                valid = new boolean[items.length];
                slots = new IdentityHashMap<>();
                canonical = new IdentityHashMap<>();
                for (int i = 0; i < items.length; i++) {
                    if (items[i] == null) continue;
                    identities[i] = TST_ItemID.create(items[i].copy());
                    associations[i] = GTOreDictUnificator.getAssociation(items[i]);
                    valid[i] = items[i].stackSize >= 0;
                }
            }
        }

        private boolean sameSlots(ItemStack[] provided) {
            if (provided.length != identities.length) return false;
            for (int i = 0; i < provided.length; i++) {
                if (provided[i] == null) {
                    if (identities[i] != null) return false;
                } else if (identities[i] == null || valid[i] != (provided[i].stackSize >= 0)
                    || !identities[i].equals(TST_ItemID.create(provided[i]))) return false;
            }
            return true;
        }

        boolean[] slots(Requirement input) {
            return slots.computeIfAbsent(input.alternatives, ignored -> {
                boolean[] matches = new boolean[items.length];
                for (int i = 0; i < items.length; i++) {
                    matches[i] = items[i] != null && items[i].stackSize >= 0
                        && input.matches(associations[i], items[i]);
                }
                return matches;
            });
        }

        boolean canonical(Requirement input) {
            return canonical.computeIfAbsent(input.alternatives, ignored -> {
                ItemStack template = input.key.getItemStack();
                return input.matches(GTOreDictUnificator.getAssociation(template), template);
            });
        }

        Map<TST_ItemID, boolean[]> slotsByKey(List<Requirement> requirements) {
            if (combinedRequirements != requirements) {
                combinedRequirements = requirements;
                combinedSlots = new LinkedHashMap<>();
                for (Requirement input : requirements) {
                    boolean[] accepted = slots(input);
                    boolean[] combined = combinedSlots.computeIfAbsent(input.key, ignored -> new boolean[items.length]);
                    for (int slot = 0; slot < accepted.length; slot++) combined[slot] |= accepted[slot];
                }
            }
            return combinedSlots;
        }
    }

    static Requirement exact(TST_ItemID key, long amount) {
        return new Requirement(key, amount, Collections.singletonList(new RecipeItemInput(key.getItemStack(), true)));
    }

    static List<Requirement> defaults(Map<Object, Long> inputs, Map<Object, Long> catalysts) {
        List<Requirement> result = new ArrayList<>();
        inputs.forEach((key, amount) -> { if (key instanceof TST_ItemID item) result.add(exact(item, amount)); });
        catalysts.forEach((key, amount) -> { if (key instanceof TST_ItemID item) result.add(exact(item, 0)); });
        return result;
    }

    /** Identical regular GT rules share one quantity, so composition does not unnecessarily enlarge the batch. */
    static List<Requirement> compact(List<Requirement> inputs) {
        List<Requirement> result = new ArrayList<>();
        for (Requirement input : inputs) {
            int selected = -1;
            for (int i = 0; i < result.size(); i++) {
                Requirement previous = result.get(i);
                if (!previous.key.equals(input.key) || (previous.amount == 0) != (input.amount == 0)
                    || previous.alternatives.size() != input.alternatives.size()) continue;
                boolean same = true;
                for (int j = 0; j < input.alternatives.size(); j++) {
                    RecipeItemInput left = previous.alternatives.get(j);
                    RecipeItemInput right = input.alternatives.get(j);
                    if (left != right && (left.oreDictId >= 0 || right.oreDictId >= 0
                        || left.usesNbtMatching != right.usesNbtMatching
                        || !TST_ItemID.create(left.unifiedStack)
                            .equals(TST_ItemID.create(right.unifiedStack)))) {
                        same = false;
                        break;
                    }
                }
                if (same) {
                    selected = i;
                    break;
                }
            }
            if (selected < 0) result.add(input);
            else result.set(
                selected,
                result.get(selected)
                    .amount(Math.addExact(result.get(selected).amount, input.amount)));
        }
        return result;
    }

    static List<Requirement> scale(List<Requirement> inputs, long numerator, long denominator, Object excluded) {
        List<Requirement> result = new ArrayList<>();
        for (Requirement input : inputs) {
            if (input.amount == 0) result.add(input);
            else if (!input.key.equals(excluded)) {
                long amount = Math.multiplyExact(input.amount, numerator);
                if (amount % denominator != 0) throw new IllegalStateException("fractional native input requirement");
                result.add(input.amount(amount / denominator));
            }
        }
        return result;
    }

    /** Cancel only requirements that the canonical returned material can actually satisfy. */
    static List<Requirement> retain(List<Requirement> inputs, Map<Object, Long> amounts) {
        Map<Object, Long> removed = new LinkedHashMap<>();
        for (Requirement input : inputs) if (input.amount > 0) removed.merge(input.key, input.amount, Math::addExact);
        removed.replaceAll((key, total) -> total - amounts.getOrDefault(key, 0L));
        List<Requirement> result = new ArrayList<>();
        for (Requirement input : inputs) {
            if (input.amount == 0) {
                result.add(input);
                continue;
            }
            long cancelled = removed.getOrDefault(input.key, 0L);
            if (cancelled > 0) {
                ItemStack returned = input.key.getItemStack();
                if (input.matches(GTOreDictUnificator.getAssociation(returned), returned)) {
                    long taken = Math.min(cancelled, input.amount);
                    removed.put(input.key, cancelled - taken);
                    if (taken < input.amount) result.add(input.amount(input.amount - taken));
                    continue;
                }
            }
            result.add(input);
        }
        for (long amount : removed.values()) {
            if (amount != 0) throw new IllegalStateException("incompatible canonical input cancellation");
        }
        return result;
    }

    /** A concrete allocation is retained so validation and later consumption cannot choose different substitutes. */
    static final class Consumption {

        private final TST_ItemID[] identities;
        private final int[] amounts;

        Consumption(ItemStack[] items, int[] amounts) {
            identities = new TST_ItemID[amounts.length];
            this.amounts = amounts;
            for (int i = 0; i < amounts.length; i++) {
                if (amounts[i] > 0) identities[i] = TST_ItemID.create(items[i].copy());
            }
        }

        void validate(ItemStack[] items) {
            if (items == null && amounts.length != 0 || items != null && items.length != amounts.length)
                throw new IllegalStateException("input inventory changed after allocation");
            for (int i = 0; i < amounts.length; i++) {
                if (amounts[i] > 0 && (items[i] == null || items[i].stackSize < amounts[i]
                    || !identities[i].equals(TST_ItemID.create(items[i]))))
                    throw new IllegalStateException("input stack changed after allocation");
            }
        }

        void consume(ItemStack[] items) {
            for (int i = 0; i < amounts.length; i++) if (amounts[i] > 0) items[i].stackSize -= amounts[i];
        }
    }

    /**
     * Canonical supply covers exactly the internally supplied amount; every remaining unit comes from a real slot.
     * Residual paths can move a broad match aside for a narrow match, without counting any slot twice.
     */
    static Consumption allocate(List<Requirement> requirements, Map<Object, Long> consumed,
        Map<Object, Long> internalPresence, ItemStack[] provided) {
        return allocate(requirements, consumed, internalPresence, new Matching(provided));
    }

    static Consumption allocate(List<Requirement> requirements, Map<Object, Long> consumed,
        Map<Object, Long> internalPresence, Matching matching) {
        ItemStack[] items = matching.items;
        List<Requirement> demands = new ArrayList<>();
        Map<TST_ItemID, Long> internal = new LinkedHashMap<>();
        for (Requirement input : requirements) {
            if (input.amount == 0) {
                boolean found = false;
                for (int i = 0; i < items.length; i++) {
                    if (matching.slots(input)[i]) {
                        found = true;
                        break;
                    }
                }
                if (!found && !(internalPresence.getOrDefault(input.key, 0L) > 0 && matching.canonical(input)))
                    return null;
            } else {
                demands.add(input);
                internal.merge(input.key, input.amount, Math::addExact);
            }
        }
        internal.replaceAll((key, amount) -> amount - consumed.getOrDefault(key, 0L));
        for (long amount : internal.values()) if (amount < 0) return null;
        int[] direct = allocateDirect(demands, internal, matching);
        if (direct != null) return new Consumption(items, direct);
        List<TST_ItemID> keys = new ArrayList<>(internal.keySet());
        int source = 0;
        int firstSupply = 1;
        int firstDemand = firstSupply + keys.size() + items.length;
        int sink = firstDemand + demands.size();
        Flow flow = new Flow(sink + 1);
        List<Edge> internalEdges = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            TST_ItemID key = keys.get(i);
            long amount = internal.get(key);
            internalEdges.add(flow.add(source, firstSupply + i, amount));
            for (int j = 0; j < demands.size(); j++) {
                Requirement demand = demands.get(j);
                if (key.equals(demand.key) && matching.canonical(demand))
                    flow.add(firstSupply + i, firstDemand + j, demand.amount);
            }
        }
        List<Edge> slots = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            int node = firstSupply + keys.size() + i;
            slots.add(flow.add(source, node, 0));
            if (items[i] == null || items[i].stackSize <= 0) continue;
            for (int j = 0; j < demands.size(); j++) {
                if (matching.slots(demands.get(j))[i]) flow.add(node, firstDemand + j, items[i].stackSize);
            }
        }
        List<Edge> remaining = new ArrayList<>();
        for (int i = 0; i < demands.size(); i++) remaining.add(flow.add(firstDemand + i, sink, demands.get(i).amount));
        // Seed the exact internal contribution first; later residual paths may reassign it, but never remove it.
        flow.fill(source, sink);
        for (Edge edge : internalEdges) if (edge.capacity != 0) return null;
        for (int i = 0; i < slots.size(); i++) {
            if (items[i] != null && items[i].stackSize > 0) slots.get(i).capacity = items[i].stackSize;
        }
        flow.fill(source, sink);
        for (Edge edge : remaining) if (edge.capacity != 0) return null;
        int[] amounts = new int[items.length];
        for (int i = 0; i < amounts.length; i++) {
            amounts[i] = items[i] == null || items[i].stackSize <= 0 ? 0
                : (int) (items[i].stackSize - slots.get(i).capacity);
        }
        return new Consumption(items, amounts);
    }

    /** Try the ordinary disjoint-input case first; overlapping native rules still fall back to residual paths. */
    private static int[] allocateDirect(List<Requirement> demands, Map<TST_ItemID, Long> internal, Matching matching) {
        ItemStack[] items = matching.items;
        Map<TST_ItemID, Long> remainingInternal = new LinkedHashMap<>(internal);
        long[] needed = new long[demands.size()];
        for (int i = 0; i < demands.size(); i++) {
            Requirement demand = demands.get(i);
            needed[i] = demand.amount;
            long available = remainingInternal.getOrDefault(demand.key, 0L);
            if (available <= 0) continue;
            if (!matching.canonical(demand)) continue;
            long taken = Math.min(available, needed[i]);
            remainingInternal.put(demand.key, available - taken);
            needed[i] -= taken;
        }
        for (long amount : remainingInternal.values()) if (amount != 0) return null;
        int[] amounts = new int[items.length];
        for (int i = 0; i < demands.size(); i++) {
            for (int slot = 0; needed[i] > 0 && slot < items.length; slot++) {
                if (items[slot] == null || items[slot].stackSize <= amounts[slot]
                    || !matching.slots(demands.get(i))[slot]) continue;
                int taken = (int) Math.min(needed[i], items[slot].stackSize - amounts[slot]);
                amounts[slot] += taken;
                needed[i] -= taken;
            }
            if (needed[i] != 0) return null;
        }
        return amounts;
    }

    private static final class Edge {

        final int target;
        final int reverse;
        long capacity;

        Edge(int target, int reverse, long capacity) {
            this.target = target;
            this.reverse = reverse;
            this.capacity = capacity;
        }
    }

    private static final class Flow {

        final List<List<Edge>> edges = new ArrayList<>();

        Flow(int size) {
            for (int i = 0; i < size; i++) edges.add(new ArrayList<>());
        }

        Edge add(int source, int target, long capacity) {
            Edge forward = new Edge(
                target,
                edges.get(target)
                    .size(),
                capacity);
            Edge reverse = new Edge(
                source,
                edges.get(source)
                    .size(),
                0);
            edges.get(source)
                .add(forward);
            edges.get(target)
                .add(reverse);
            return forward;
        }

        void fill(int source, int sink) {
            int[] previous = new int[edges.size()];
            int[] selected = new int[edges.size()];
            int[] queue = new int[edges.size()];
            while (true) {
                Arrays.fill(previous, -1);
                previous[source] = source;
                queue[0] = source;
                int size = 1;
                for (int head = 0; head < size && previous[sink] < 0; head++) {
                    int node = queue[head];
                    for (int index = 0; index < edges.get(node)
                        .size(); index++) {
                        Edge edge = edges.get(node)
                            .get(index);
                        if (edge.capacity <= 0 || previous[edge.target] >= 0) continue;
                        previous[edge.target] = node;
                        selected[edge.target] = index;
                        queue[size++] = edge.target;
                    }
                }
                if (previous[sink] < 0) return;
                long amount = Long.MAX_VALUE;
                for (int node = sink; node != source; node = previous[node]) amount = Math.min(
                    amount,
                    edges.get(previous[node])
                        .get(selected[node]).capacity);
                for (int node = sink; node != source; node = previous[node]) {
                    Edge edge = edges.get(previous[node])
                        .get(selected[node]);
                    edge.capacity -= amount;
                    edges.get(node)
                        .get(edge.reverse).capacity += amount;
                }
            }
        }
    }
}
