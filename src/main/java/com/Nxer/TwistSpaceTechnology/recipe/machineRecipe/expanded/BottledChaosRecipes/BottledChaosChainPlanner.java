package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Integer, demand-driven planning on the startup-resolved material graph. Never touches real storage. */
final class BottledChaosChainPlanner {

    private static final int MAX_SUPPLY_ATTEMPTS = 2048;

    static final class Stage {

        final Map<Object, Long> inputs;
        final Map<Object, Long> outputs;
        final Map<Object, Long> catalysts;
        final long voltage;

        Stage(Map<Object, Long> inputs, Map<Object, Long> outputs, Map<Object, Long> catalysts, long voltage) {
            this.inputs = inputs;
            this.outputs = outputs;
            this.catalysts = catalysts;
            this.voltage = voltage;
        }
    }

    interface Conditions {

        boolean canProcess(int stage, long voltage);

        boolean checkInputs(int stage, long count, Map<Object, Long> allocated);

        long retainedOutput(int stage, Object key, long amount);
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
        }
    }

    private final Stage[] stages;
    private final List<Map<Object, int[]>> suppliers;
    private final Conditions conditions;
    private final Set<?> virtualItems;
    private final long batchLimit;
    private int attempts;
    private Result state;

    BottledChaosChainPlanner(Stage[] stages, List<Map<Object, int[]>> suppliers, Conditions conditions,
        Set<?> virtualItems, long batchLimit) {
        this.stages = stages;
        this.suppliers = suppliers;
        this.conditions = conditions;
        this.virtualItems = virtualItems;
        this.batchLimit = batchLimit;
    }

    Result prepare(int[] goalOrder, long[] targets, Map<Object, Long> external, Map<Object, Long> remaining,
        Map<Object, Long> released, boolean skipUnavailable) {
        state = new Result(stages.length, external, remaining, released);
        for (int goal : goalOrder) {
            if (targets[goal] == 0) continue;
            Result before = new Result(state);
            attempts = 0;
            try {
                if (execute(goal, Math.max(0, targets[goal] - state.batches[goal]))) {
                    state.targets[goal] = targets[goal];
                    continue;
                }
            } catch (ArithmeticException overflow) {
                // An unrepresentable probe is discarded without committing any of its material deltas.
            }
            state = before;
            if (!skipUnavailable) return null;
        }
        for (long target : state.targets) if (target != 0) return state;
        return null;
    }

    private boolean execute(int index, long count) {
        if (count == 0) return true;
        if (++attempts > MAX_SUPPLY_ATTEMPTS || state.active[index] || count > batchLimit - state.batches[index])
            return false;
        Stage stage = stages[index];
        long voltage = Math.max(state.voltage, stage.voltage);
        if (!conditions.canProcess(index, voltage)) return false;
        if (voltage > state.voltage) {
            for (int previous = 0; previous < stages.length; previous++) {
                if ((state.batches[previous] != 0 || state.active[previous])
                    && !conditions.canProcess(previous, voltage)) return false;
            }
        }
        state.voltage = voltage;
        state.active[index] = true;
        Map<Object, Long> allocated = new LinkedHashMap<>();
        Map<Object, Long> missing = new LinkedHashMap<>();
        // Reserve every available input before making any supplier run. Shared inputs belong to this goal first.
        for (var input : stage.inputs.entrySet()) {
            long needed = Math.multiplyExact(input.getValue(), count);
            long taken = takeInput(index, input.getKey(), needed);
            allocated.put(input.getKey(), needed);
            if (taken < needed) missing.put(input.getKey(), needed - taken);
        }
        for (var input : missing.entrySet()) {
            if (!supply(index, input.getKey(), input.getValue())) return false;
            if (takeInput(index, input.getKey(), input.getValue()) != input.getValue()) return false;
        }
        for (var catalyst : stage.catalysts.entrySet()) {
            Object key = catalyst.getKey();
            long needed = Math.max(0, catalyst.getValue() - allocated.getOrDefault(key, 0L));
            if (!(needed == 1 && virtualItems.contains(key)) && !supply(index, key, needed)) return false;
            allocated.merge(key, needed, Math::addExact);
        }
        if (!conditions.checkInputs(index, count, allocated)) return false;
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
        state.active[index] = false;
        return true;
    }

    private boolean supply(int consumer, Object key, long needed) {
        if (available(consumer, key) >= needed) return true;
        int[] sources = suppliers.get(consumer)
            .get(key);
        if (sources == null) return false;
        for (int source : sources) {
            // A return cannot pay for its own unfinished consumer. Existing carry or external seed may break cycles.
            if (state.active[source]) continue;
            long missing = needed - available(consumer, key);
            long output = stages[source].outputs.get(key);
            long count = Math
                .min(batchLimit - state.batches[source], missing / output + (missing % output == 0 ? 0 : 1));
            if (count <= 0) continue;
            Result before = new Result(state);
            if (trySupplier(source, count)) {
                if (available(consumer, key) >= needed) return true;
                continue;
            }
            state = before;
            // Different authorized suppliers may jointly cover the shortage. Probe integer counts, not units.
            long lower = 1;
            long upper = count - 1;
            Result best = before;
            while (lower <= upper && attempts <= MAX_SUPPLY_ATTEMPTS) {
                long middle = lower + (upper - lower) / 2;
                state = new Result(before);
                if (trySupplier(source, middle)) {
                    best = state;
                    lower = middle + 1;
                } else upper = middle - 1;
            }
            state = best;
            if (available(consumer, key) >= needed) return true;
        }
        return false;
    }

    private boolean trySupplier(int source, long count) {
        try {
            return execute(source, count);
        } catch (ArithmeticException overflow) {
            return false;
        }
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
