package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

/** Integer, demand-driven planning on the startup-resolved material graph. Never touches real storage. */
final class BottledChaosChainPlanner {

    private static final int MAX_SUPPLY_ATTEMPTS = 2048;

    static final class Stage {

        final Map<Object, Long> inputs;
        final Map<Object, Long> outputs;
        final Map<Object, Long> catalysts;
        final List<?> targets;
        final Object balanceRoot;
        final long voltage;
        final boolean availableInputsOnly;

        Stage(Map<Object, Long> inputs, Map<Object, Long> outputs, Map<Object, Long> catalysts, long voltage,
            boolean availableInputsOnly, List<?> targets, Object balanceRoot) {
            this.inputs = inputs;
            this.outputs = outputs;
            this.catalysts = catalysts;
            this.targets = targets;
            this.balanceRoot = balanceRoot;
            this.voltage = voltage;
            this.availableInputsOnly = availableInputsOnly;
        }
    }

    interface Conditions {

        CheckRecipeResult check(int stage, long voltage);

        long retainedOutput(int stage, Object key, long amount);

        default boolean automaticRecovery(int stage) {
            return false;
        }

        default double cost(int stage, long voltage) {
            return 0;
        }

        default long pools(int stage, long voltage) {
            return 0;
        }

        default Map<Object, Long> catalysts(int stage, long voltage) {
            return null;
        }

        default Map<Object, Long> inputs(int stage, long voltage) {
            return null;
        }
    }

    static final class Result {

        final Map<Object, Long> external;
        final Map<Object, Long> remaining;
        final Map<Object, Long> produced;
        final Map<Object, Long> consumed;
        final Map<Object, Long> outputs;
        final long[] batches;
        final long[] targets;
        final boolean[] active;
        long voltage;
        long pools;
        double duration;

        Result(int stages, Map<Object, Long> external, Map<Object, Long> remaining, Map<Object, Long> outputs) {
            this.external = new LinkedHashMap<>(external);
            this.remaining = new LinkedHashMap<>(remaining);
            this.outputs = new LinkedHashMap<>(outputs);
            produced = new LinkedHashMap<>();
            consumed = new LinkedHashMap<>();
            batches = new long[stages];
            targets = new long[stages];
            active = new boolean[stages];
        }

        Result(Result other) {
            external = new LinkedHashMap<>(other.external);
            remaining = new LinkedHashMap<>(other.remaining);
            produced = new LinkedHashMap<>(other.produced);
            consumed = new LinkedHashMap<>(other.consumed);
            outputs = new LinkedHashMap<>(other.outputs);
            batches = other.batches.clone();
            targets = other.targets.clone();
            active = other.active.clone();
            voltage = other.voltage;
            pools = other.pools;
            duration = other.duration;
        }
    }

    /** Split carry only after chaining. Check every output sum before changing any candidate inventory. */
    static void releaseExcess(long limit, Map<Object, Long> remaining, Map<Object, Long> produced,
        Map<Object, Long> outputs) {
        Set<Object> keys = new LinkedHashSet<>(remaining.keySet());
        keys.addAll(produced.keySet());
        Map<Object, Long> excessOutputs = new LinkedHashMap<>();
        for (Object key : keys) {
            long stored = remaining.getOrDefault(key, 0L);
            long excess = Math.addExact(
                Math.max(0, stored - limit),
                Math.max(0, produced.getOrDefault(key, 0L) - Math.max(0, limit - stored)));
            if (excess > 0) excessOutputs.put(key, Math.addExact(outputs.getOrDefault(key, 0L), excess));
        }
        produced.replaceAll((key, amount) -> Math.min(amount, Math.max(0, limit - remaining.getOrDefault(key, 0L))));
        remaining.replaceAll((key, amount) -> Math.min(amount, limit));
        produced.values()
            .removeIf(amount -> amount == 0);
        remaining.values()
            .removeIf(amount -> amount == 0);
        outputs.putAll(excessOutputs);
    }

    private final Stage[] stages;
    private final List<Map<Object, int[]>> suppliers;
    private final Conditions conditions;
    private final Set<?> virtualItems;
    private final long batchLimit;
    private int attempts;
    private Result state;
    private final Map<List<?>, CheckRecipeResult> rejectedTargets = new LinkedHashMap<>();

    BottledChaosChainPlanner(Stage[] stages, List<Map<Object, int[]>> suppliers, Conditions conditions,
        Set<?> virtualItems, long batchLimit) {
        this.stages = stages;
        this.suppliers = suppliers;
        this.conditions = conditions;
        this.virtualItems = virtualItems;
        this.batchLimit = batchLimit;
    }

    CheckRecipeResult preparationFailure() {
        for (CheckRecipeResult failure : rejectedTargets.values()) {
            if (failure != CheckRecipeResultRegistry.NO_RECIPE) return failure;
        }
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    Result prepare(int[] goalOrder, long[] targets, Map<Object, Long> external, Map<Object, Long> remaining,
        Map<Object, Long> released, boolean skipUnavailable) {
        rejectedTargets.clear();
        state = new Result(stages.length, external, remaining, released);
        for (int goal : goalOrder) {
            if (targets[goal] == 0) continue;
            Result before = skipUnavailable ? new Result(state) : null;
            attempts = 0;
            CheckRecipeResult result = trySupplier(goal, Math.max(0, targets[goal] - state.batches[goal]));
            if (result.wasSuccessful()) {
                state.targets[goal] = targets[goal];
                // A working alternative satisfies this target, regardless of the order of attempts.
                rejectedTargets.put(stages[goal].targets, CheckRecipeResultRegistry.NO_RECIPE);
                continue;
            }
            if (!stages[goal].availableInputsOnly) {
                // A verified material-only alternative still permits preparation of the same target.
                rejectedTargets.merge(
                    stages[goal].targets,
                    result,
                    (left, right) -> right == CheckRecipeResultRegistry.NO_RECIPE ? right : left);
            }
            if (!skipUnavailable) return null;
            state = before;
        }
        for (long target : state.targets) if (target != 0) {
            recoverProduced();
            return state;
        }
        return null;
    }

    /** Include recycling of this round's byproducts in the complete-line time estimate. */
    private void recoverProduced() {
        for (int index = 0; index < stages.length; index++) {
            Stage stage = stages[index];
            if (!stage.availableInputsOnly || !conditions.automaticRecovery(index)) continue;
            long count = 0;
            for (var input : inputs(index, Math.max(state.voltage, stage.voltage)).entrySet()) {
                count = Math.max(count, state.produced.getOrDefault(input.getKey(), 0L) / input.getValue());
            }
            count = suppliedInputLimit(index, Math.min(count, batchLimit - state.batches[index]));
            if (count <= 0) continue;
            Result before = new Result(state);
            attempts = 0;
            if (!trySupplier(index, count).wasSuccessful()) state = before;
        }
    }

    private CheckRecipeResult execute(int index, long count) {
        if (count == 0) return CheckRecipeResultRegistry.SUCCESSFUL;
        if (++attempts > MAX_SUPPLY_ATTEMPTS || state.active[index] || count > batchLimit - state.batches[index])
            return CheckRecipeResultRegistry.NO_RECIPE;
        Stage stage = stages[index];
        long voltage = Math.max(state.voltage, stage.voltage);
        CheckRecipeResult result = conditions.check(index, voltage);
        if (!result.wasSuccessful()) return result;
        if (voltage > state.voltage) {
            for (int previous = 0; previous < stages.length; previous++) {
                if (state.batches[previous] == 0 && !state.active[previous]) continue;
                result = conditions.check(previous, voltage);
                if (!result.wasSuccessful()) return result;
            }
        }
        if (voltage != state.voltage) {
            state.duration = 0;
            state.pools = 0;
            for (int previous = 0; previous < stages.length; previous++) {
                if (state.batches[previous] != 0) {
                    state.duration += conditions.cost(previous, voltage) * state.batches[previous];
                    state.pools |= conditions.pools(previous, voltage);
                }
            }
        }
        state.voltage = voltage;
        state.active[index] = true;
        Map<Object, Long> inputs = inputs(index, voltage);
        Map<Object, Long> missing = new LinkedHashMap<>();
        // Reserve every available input before making any supplier run. Shared inputs belong to this goal first.
        for (var input : inputs.entrySet()) {
            long needed = Math.multiplyExact(input.getValue(), count);
            long taken = takeInput(index, input.getKey(), needed);
            if (taken < needed) missing.put(input.getKey(), needed - taken);
        }
        // Recycling may use paid carry or supplied feed, but must not manufacture new feed for itself.
        if (stage.availableInputsOnly && !missing.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;
        for (var input : missing.entrySet()) {
            result = supply(index, input.getKey(), input.getValue());
            if (!result.wasSuccessful()) return result;
            if (takeInput(index, input.getKey(), input.getValue()) != input.getValue())
                return CheckRecipeResultRegistry.NO_RECIPE;
        }
        for (var catalyst : catalysts(index, state.voltage).entrySet()) {
            Object key = catalyst.getKey();
            long needed = Math.max(0, catalyst.getValue() - Math.multiplyExact(inputs.getOrDefault(key, 0L), count));
            if (stage.availableInputsOnly && available(index, key) < needed
                && !(needed == 1 && virtualItems.contains(key))) return CheckRecipeResultRegistry.NO_RECIPE;
            if (!(needed == 1 && virtualItems.contains(key))) {
                result = supply(index, key, needed);
                if (!result.wasSuccessful()) return result;
            }
        }
        for (var output : stage.outputs.entrySet()) {
            Object key = output.getKey();
            long amount = Math.multiplyExact(output.getValue(), count);
            long retained = conditions.retainedOutput(index, key, amount);
            if (retained > 0) {
                Math.addExact(
                    state.remaining.getOrDefault(key, 0L),
                    Math.addExact(state.produced.getOrDefault(key, 0L), retained));
                state.produced.merge(key, retained, Math::addExact);
            }
            if (amount > retained) state.outputs.merge(key, amount - retained, Math::addExact);
        }
        state.batches[index] += count;
        state.pools |= conditions.pools(index, state.voltage);
        state.duration += conditions.cost(index, state.voltage) * count;
        if (!Double.isFinite(state.duration)) throw new ArithmeticException("duration overflow");
        state.active[index] = false;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private CheckRecipeResult supply(int consumer, Object key, long needed) {
        if (available(consumer, key) >= needed) return CheckRecipeResultRegistry.SUCCESSFUL;
        int[] sources = suppliers.get(consumer)
            .get(key);
        if (sources == null) return CheckRecipeResultRegistry.NO_RECIPE;
        boolean materialShortage = false;
        CheckRecipeResult failure = CheckRecipeResultRegistry.NO_RECIPE;
        for (int sourceIndex = 0; sourceIndex < sources.length; sourceIndex++) {
            int source = sources[sourceIndex];
            // A return cannot pay for its own unfinished consumer. Existing stock may break cycles.
            if (state.active[source]) continue;
            CheckRecipeResult result = conditions.check(source, Math.max(state.voltage, stages[source].voltage));
            if (!result.wasSuccessful()) {
                failure = result;
                continue;
            }
            long missing = needed - available(consumer, key);
            long output = stages[source].outputs.get(key);
            long count = Math
                .min(batchLimit - state.batches[source], missing / output + (missing % output == 0 ? 0 : 1));
            count = suppliedInputLimit(source, count);
            if (count <= 0) {
                materialShortage = true;
                continue;
            }
            // Only an alternative supplier needs a local rollback. A failed last supplier rejects its caller.
            Result before = sourceIndex + 1 < sources.length ? new Result(state) : null;
            result = trySupplier(source, count);
            if (result.wasSuccessful()) {
                if (available(consumer, key) >= needed) return result;
                materialShortage = true;
                continue;
            }
            materialShortage |= result == CheckRecipeResultRegistry.NO_RECIPE;
            failure = result;
            if (before == null) break;
            state = before;
        }
        // Only a usable alternative waiting for materials may permit precursor preparation.
        return materialShortage ? CheckRecipeResultRegistry.NO_RECIPE : failure;
    }

    /** Bound leaf inputs directly; recursive suppliers are checked once by execute, never binary-searched. */
    private long suppliedInputLimit(int index, long count) {
        Stage stage = stages[index];
        for (var input : inputs(index, Math.max(state.voltage, stage.voltage)).entrySet()) {
            int[] sources = suppliers.get(index)
                .get(input.getKey());
            boolean canSupply = false;
            if (!stage.availableInputsOnly && sources != null) {
                for (int source : sources) if (!state.active[source] && source != index) {
                    canSupply = true;
                    break;
                }
            }
            if (!canSupply) count = Math.min(count, available(index, input.getKey()) / input.getValue());
        }
        return count;
    }

    /** Drain paid feed downstream first, including stages bypassed by the initial plan. */
    Result appendAvailableTargets(Result planned, int[] order, double durationLimit) {
        state = planned;
        for (int pass = 0; pass < stages.length; pass++) {
            boolean progressed = false;
            for (int position = order.length - 1; position >= 0; position--) {
                int index = order[position];
                Stage stage = stages[index];
                if (stage.availableInputsOnly || stage.voltage > state.voltage) continue;
                long stored = saturatedAdd(
                    state.remaining.getOrDefault(stage.balanceRoot, 0L),
                    state.produced.getOrDefault(stage.balanceRoot, 0L));
                if (stored == 0) continue;
                Map<Object, Long> inputs = inputs(index, state.voltage);
                long rootInput = inputs.getOrDefault(stage.balanceRoot, 0L);
                if (rootInput <= 0) continue;
                long count = Math.min(batchLimit - state.batches[index], stored / rootInput);
                for (var input : inputs.entrySet()) {
                    count = Math.min(count, available(index, input.getKey()) / input.getValue());
                }
                if (count <= 0) continue;
                // A cached stage may be new to this plan; keep its ordinary machine and voltage checks.
                double cost = conditions.cost(index, state.voltage);
                if (cost > 0) count = Math.min(count, (long) Math.max(0, (durationLimit - state.duration) / cost));
                if (count <= 0) continue;
                boolean catalystsAvailable = true;
                for (var catalyst : catalysts(index, state.voltage).entrySet()) {
                    Object key = catalyst.getKey();
                    long consumed = Math.multiplyExact(inputs.getOrDefault(key, 0L), count);
                    long needed = Math.max(0, catalyst.getValue() - consumed);
                    if (available(index, key) - consumed < needed && !(needed == 1 && virtualItems.contains(key))) {
                        catalystsAvailable = false;
                        break;
                    }
                }
                if (!catalystsAvailable) continue;
                Result before = state;
                state = new Result(state);
                attempts = 0;
                if (trySupplier(index, count).wasSuccessful()) {
                    state.targets[index] = state.batches[index];
                    progressed = true;
                } else state = before;
            }
            if (!progressed) break;
        }
        return state;
    }

    /** Use spare work budget for automatic recycling, without replaying the main chain or producing new feed. */
    Result recover(Result planned, int[] order, double budget, double[] costs) {
        state = new Result(planned);
        for (int index : order) {
            Stage stage = stages[index];
            if (!stage.availableInputsOnly || stage.voltage > state.voltage || costs[index] <= 0) continue;
            long count = suppliedInputLimit(
                index,
                Math.min(batchLimit - state.batches[index], (long) Math.floor(budget / costs[index])));
            if (count <= 0) continue;
            Result before = new Result(state);
            attempts = 0;
            if (trySupplier(index, count).wasSuccessful()) {
                state.targets[index] = state.batches[index];
                budget = Math.max(0, budget - costs[index] * count);
            } else state = before;
        }
        return state;
    }

    private CheckRecipeResult trySupplier(int source, long count) {
        try {
            return execute(source, count);
        } catch (ArithmeticException overflow) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    private Map<Object, Long> catalysts(int index, long voltage) {
        Map<Object, Long> selected = conditions.catalysts(index, voltage);
        return selected == null ? stages[index].catalysts : selected;
    }

    private Map<Object, Long> inputs(int index, long voltage) {
        Map<Object, Long> inputs = conditions.inputs(index, voltage);
        return inputs == null ? stages[index].inputs : inputs;
    }

    private long available(int consumer, Object key) {
        long amount = state.external.getOrDefault(key, 0L);
        if (suppliers.get(consumer)
            .containsKey(key)) {
            amount = saturatedAdd(amount, state.remaining.getOrDefault(key, 0L));
            amount = saturatedAdd(amount, state.produced.getOrDefault(key, 0L));
        }
        return amount;
    }

    private long takeInput(int consumer, Object key, long needed) {
        long taken = 0;
        if (suppliers.get(consumer)
            .containsKey(key)) {
            taken = take(state.remaining, key, needed);
            taken += take(state.produced, key, needed - taken);
        }
        long external = take(state.external, key, needed - taken);
        if (external != 0) {
            state.consumed.merge(key, external, Math::addExact);
            if (virtualItems.contains(key)) state.external.putIfAbsent(key, 0L);
        }
        return taken + external;
    }

    private static long take(Map<Object, Long> amounts, Object key, long needed) {
        long stored = amounts.getOrDefault(key, 0L);
        long taken = Math.min(stored, needed);
        if (taken != 0) {
            if (taken == stored) amounts.remove(key);
            else amounts.put(key, stored - taken);
        }
        return taken;
    }

    private static long saturatedAdd(long left, long right) {
        return left > Long.MAX_VALUE - right ? Long.MAX_VALUE : left + right;
    }
}
