package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMaterialStacks.appendResourceStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMaterialCache;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMaterialStacks;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosOverclock;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosSourceParameters;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosUpkeep;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosItemInputs.Requirement;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.ChainLink;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.CompiledCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.CompiledRoute;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;

/**
 * Publishes the prepared route cache and plans execution for Bottled Chaos.
 * Prepared routes retain net materials and source timing groups for execution and NEI display.
 */
public final class BottledChaosRecipeProcessor {

    private static final BottledChaosSourceParameters DEFAULT_PARAMETERS = BottledChaosSourceParameters.DEFAULT;
    private static volatile RouteCache cache;

    // Registration owns declaration/compilation and lifecycle ordering; execution owns the published snapshot.
    static void clearCache() {
        cache = null;
    }

    static void installCache(CompiledCatalog compiled) {
        CachedRoute[] routes = new CachedRoute[compiled.routes().length];
        for (int entry = 0; entry < routes.length; entry++) {
            CompiledRoute definition = compiled.routes()[entry];
            routes[entry] = new CachedRoute(
                definition,
                new SourceWork(definition.data()),
                BottledChaosMachineConfiguration.withDependencies(
                    definition.data().times.keySet()
                        .stream()
                        .mapToLong(time -> BottledChaosMachineConfiguration.mask(time.map()))
                        .reduce(0L, (left, right) -> left | right)));
        }
        cache = new RouteCache(
            routes,
            compiled.entryOrder(),
            compiled.displayOrder(),
            compiled.links(),
            compiled.keyOutputs());
    }

    /** Last successful family/entry first, then the fixed family directory, as in GT's buffered recipe lookup. */
    public static int[] candidateEntries(long tagMask, BottledChaosMaterialCache materials) {
        RouteCache snapshot = cache;
        if (snapshot == null || tagMask == 0) return new int[0];
        int[] result = new int[snapshot.routes.length];
        int size = 0;
        for (int pass = 0; pass < 2; pass++) {
            for (int family = 0; family < snapshot.familyEntries.length; family++) {
                if (snapshot.familyNames[family].equals(materials.lastFamily()) != (pass == 0)) continue;
                int preferred = materials.lastEntry();
                if (preferred >= 0 && preferred < snapshot.routes.length
                    && snapshot.familyOfEntry[preferred] == family
                    && snapshot.routes[preferred].isAuthorized(tagMask)) result[size++] = preferred;
                for (int entry : snapshot.familyEntries[family]) {
                    if (entry != preferred && snapshot.routes[entry].isAuthorized(tagMask)) result[size++] = entry;
                }
            }
        }
        return Arrays.copyOf(result, size);
    }

    /** Waila groups final targets before other key outputs, then unmarked coproducts. */
    public static Map<Object, Integer> outputPriorities(long tagMask) {
        RouteCache snapshot = cache;
        if (snapshot == null || tagMask == 0) return Collections.emptyMap();
        Map<Object, Integer> priorities = new LinkedHashMap<>();
        for (int entry : snapshot.entryOrder) {
            CachedRoute route = snapshot.routes[entry];
            if (!route.isAuthorized(tagMask)) continue;
            for (Object key : route.definition.data().outputs.keySet())
                if (snapshot.keyOutputs.contains(key)) priorities.putIfAbsent(key, 1);
            for (Object key : route.definition.planningTargets()) {
                if (!snapshot.hasAuthorizedConsumer(entry, tagMask, key)) priorities.put(key, 0);
            }
        }
        return priorities;
    }

    /** One current authorization graph per processing group; quantities always belong to a fresh planner. */
    public static final class PlanningCache {

        private RouteCache snapshot;
        private long tagMask;
        private AuthorizedGraph graph;
        private BottledChaosItemInputs.Matching matching;

        private AuthorizedGraph get(RouteCache current, long mask) {
            if (current != snapshot || graph == null || mask != tagMask) {
                snapshot = current;
                tagMask = mask;
                graph = current == null ? null : new AuthorizedGraph(current, mask);
                matching = null;
            }
            return graph;
        }

        private BottledChaosItemInputs.Matching matching(ItemStack[] items) {
            matching = new BottledChaosItemInputs.Matching(items, matching);
            return matching;
        }
    }

    private static final class AuthorizedGraph {

        final boolean[] authorized;
        final List<Map<Object, int[]>> suppliers = new ArrayList<>();
        final List<Set<Object>> retainedOutputs = new ArrayList<>();
        final List<Set<Integer>> downstream = new ArrayList<>();
        final Set<Object> cachedInputs = new LinkedHashSet<>();
        final List<Requirement> itemRequirements = new ArrayList<>();
        final Set<Object> requiredInputs = new LinkedHashSet<>();
        final int[][] orders;
        final long[][] completeTargets;
        final boolean[] terminals;

        private AuthorizedGraph(RouteCache snapshot, long tagMask) {
            authorized = new boolean[snapshot.routes.length];
            List<Map<Object, List<Integer>>> incoming = new ArrayList<>();
            for (int index = 0; index < authorized.length; index++) {
                authorized[index] = snapshot.routes[index].isAuthorized(tagMask);
                incoming.add(new LinkedHashMap<>());
                retainedOutputs.add(new LinkedHashSet<>());
                downstream.add(new LinkedHashSet<>());
            }
            for (ChainLink link : snapshot.links) {
                if (!authorized[link.target()]) continue;
                cachedInputs.add(link.key());
                // An already-paid input stays usable even if none of its suppliers is currently authorized.
                List<Integer> sources = incoming.get(link.target())
                    .computeIfAbsent(link.key(), ignored -> new ArrayList<>());
                if (!authorized[link.source()]) continue;
                sources.add(link.source());
                retainedOutputs.get(link.source())
                    .add(link.key());
                downstream.get(link.source())
                    .add(link.target());
            }
            for (Map<Object, List<Integer>> inputs : incoming) {
                Map<Object, int[]> sources = new LinkedHashMap<>();
                inputs.forEach(
                    (key, entries) -> sources.put(
                        key,
                        entries.stream()
                            .mapToInt(Integer::intValue)
                            .toArray()));
                suppliers.add(sources);
            }
            for (int index : snapshot.entryOrder) {
                if (!authorized[index]) continue;
                RecipeData data = snapshot.routes[index].definition.data();
                itemRequirements.addAll(data.itemRequirements);
                requiredInputs.addAll(data.inputs.keySet());
                requiredInputs.addAll(data.catalysts.keySet());
            }
            orders = new int[snapshot.routes.length][];
            completeTargets = new long[snapshot.routes.length][];
            terminals = new boolean[snapshot.routes.length];
            for (int index = 0; index < terminals.length; index++) {
                for (Object output : snapshot.routes[index].definition.planningTargets()) {
                    if (!retainedOutputs.get(index)
                        .contains(output)) terminals[index] = true;
                }
            }
        }

    }

    public static ExecutionPlanner executionPlanner(long tagMask, ItemStack[] items, FluidStack[] fluids,
        BottledChaosSourceParameters parameters, BottledChaosMaterialCache materials, PlanningCache planningCache) {
        return new ExecutionPlanner(
            cache,
            tagMask,
            items,
            fluids,
            parameters,
            materials,
            planningCache.get(cache, tagMask),
            planningCache.matching(items));
    }

    @FunctionalInterface
    public interface RouteFilter {

        boolean allows(long voltage, long pools);
    }

    /** One input/structure snapshot, sharing source checks and normalized costs across bounded batch probes. */
    public static final class ExecutionPlanner {

        public long diagnosticProbeNs, diagnosticSourceNs, diagnosticMatchNs, diagnosticInputNs, diagnosticTargetPassNs;
        public int diagnosticProbes, diagnosticSourceCalls, diagnosticTargetPasses, diagnosticTargetExtensions;
        public int diagnosticShortProbes, diagnosticShortExtensions;
        public double diagnosticShortBefore, diagnosticShortAfter;

        private final RouteCache snapshot;
        private final long tagMask;
        private int entry = -1;
        private final ItemStack[] items;
        private final FluidStack[] fluids;
        private final BottledChaosSourceParameters parameters;
        private final BottledChaosMaterialCache materials;
        private final Map<Long, SourceChecks> sourceChecks = new LinkedHashMap<>();
        private final Set<TST_ItemID> virtualItems = new LinkedHashSet<>();
        private final AuthorizedGraph graph;
        private final BottledChaosItemInputs.Matching matching;
        private final List<Map<Object, int[]>> suppliers;
        private final List<Set<Object>> retainedOutputs;
        private final List<Set<Integer>> downstream;
        private final boolean[] authorized;
        private Map<Object, Long> external;
        private Map<Object, Long> remaining;
        private Map<Object, Long> released;
        private int[] goalOrder;
        private long[] targets;
        private boolean deferCompletion;
        private boolean targetExtensionAttempted;
        private boolean shortCorrectionAttempted;
        private RouteFilter routeFilter = (voltage, pools) -> true;
        private java.util.function.LongToDoubleFunction durationLimit = voltage -> Double.MAX_VALUE;

        public void setLimits(RouteFilter routeFilter, java.util.function.LongToDoubleFunction durationLimit) {
            this.routeFilter = routeFilter;
            this.durationLimit = durationLimit;
        }

        private ExecutionPlanner(RouteCache snapshot, long tagMask, ItemStack[] items, FluidStack[] fluids,
            BottledChaosSourceParameters parameters, BottledChaosMaterialCache materials, AuthorizedGraph graph,
            BottledChaosItemInputs.Matching matching) {
            this.graph = graph;
            this.matching = matching;
            suppliers = graph == null ? null : graph.suppliers;
            retainedOutputs = graph == null ? null : graph.retainedOutputs;
            downstream = graph == null ? null : graph.downstream;
            authorized = graph == null ? null : graph.authorized;
            this.snapshot = snapshot;
            this.tagMask = tagMask;
            this.items = items;
            this.fluids = fluids;
            this.parameters = parameters;
            this.materials = materials;
        }

        private SourceChecks sourceChecks(long voltage) {
            return sourceChecks.computeIfAbsent(
                voltage,
                ignored -> new SourceChecks(
                    new double[snapshot.routes.length],
                    new boolean[snapshot.routes.length],
                    new boolean[snapshot.routes.length],
                    new SourceSelection[snapshot.routes.length]));
        }

        private boolean checkSources(int index, long voltage) {
            SourceChecks checks = sourceChecks(voltage);
            if (!checks.checked[index]) {
                long diagnosticSourceStart = System.nanoTime();
                diagnosticSourceCalls++;
                checks.checked[index] = true;
                SourceSelection selected = selectSources(snapshot.routes[index].work, parameters, voltage);
                checks.selections[index] = selected;
                checks.allowed[index] = selected != null && routeFilter.allows(voltage, selected.pools);
                if (selected != null) checks.costs[index] = selected.duration;
                diagnosticSourceNs += System.nanoTime() - diagnosticSourceStart;
            }
            return checks.allowed[index];
        }

        @Desugar
        private record SourceChecks(double[] costs, boolean[] checked, boolean[] allowed,
            SourceSelection[] selections) {}

        /** Entry fallbacks share the same input, authorization and source-cost snapshot. */
        public PreparedPlan prepareEntry(int entry) {
            return prepareEntry(entry, true);
        }

        /** Native matching conflicts retain the original, fully checked entry/quantity fallback. */
        public PreparedPlan retryEntry(int entry) {
            return prepareEntry(entry, false);
        }

        private PreparedPlan prepareEntry(int entry, boolean deferCompletion) {
            this.deferCompletion = deferCompletion;
            this.entry = entry;
            goalOrder = null;
            targets = null;
            return prepare(1);
        }

        /** Prefer complete terminal products; only an unavailable full line falls back to paid intermediate carry. */
        private PreparedPlan prepare(int targetBatches) {
            if (snapshot == null || !snapshot.routes[entry].isAuthorized(tagMask)) return null;
            if (external == null) {
                Set<Object> cachedInputs = graph.cachedInputs;
                long diagnosticInputStart = System.nanoTime();
                external = snapshot.collectAvailableInputs(graph, matching, fluids, virtualItems);
                diagnosticInputNs += System.nanoTime() - diagnosticInputStart;
                remaining = materials.snapshot();
                released = new LinkedHashMap<>();
                for (var iterator = remaining.entrySet()
                    .iterator(); iterator.hasNext();) {
                    var carry = iterator.next();
                    if (cachedInputs.contains(carry.getKey())) continue;
                    released.put(carry.getKey(), carry.getValue());
                    iterator.remove();
                }
            }
            if (goalOrder == null) {
                if (graph.orders[entry] == null) {
                    boolean[] reachable = new boolean[snapshot.routes.length];
                    reachable[entry] = true;
                    int[] queue = new int[snapshot.routes.length];
                    queue[0] = entry;
                    int tail = 1;
                    for (int head = 0; head < tail; head++) {
                        for (int target : downstream.get(queue[head])) {
                            if (reachable[target]) continue;
                            reachable[target] = true;
                            queue[tail++] = target;
                        }
                    }
                    int[] entries = snapshot.entryOrder;
                    goalOrder = new int[entries.length];
                    int size = 0;
                    long[] complete = new long[snapshot.routes.length];
                    // Deeper branches get first claim on shared material. Returned byproducts do not hide a terminal.
                    for (int i = entries.length - 1; i >= 0; i--) {
                        int index = entries[i];
                        if (!reachable[index]) continue;
                        goalOrder[size++] = index;
                        for (Object terminal : snapshot.routes[index].definition.planningTargets()) {
                            if (!retainedOutputs.get(index)
                                .contains(terminal)) {
                                complete[index] = 1;
                                break;
                            }
                        }
                    }
                    goalOrder = Arrays.copyOf(goalOrder, size);
                    graph.orders[entry] = goalOrder;
                    graph.completeTargets[entry] = complete;
                }
                goalOrder = graph.orders[entry];
                long[] complete = graph.completeTargets[entry];
                PreparedPlan first = prepareTargets(complete, true);
                if (first == null) {
                    for (int index : goalOrder) {
                        long[] partial = new long[snapshot.routes.length];
                        partial[index] = 1;
                        first = prepareTargets(partial, false);
                        if (first != null) break;
                    }
                }
                if (first == null) return null;
                targets = first.targets.clone();
                if (targetBatches <= 1) return first;
            }
            if (targets == null) return null;
            long[] requested = targets.clone();
            for (int index : goalOrder) if (requested[index] != 0) requested[index] = Math.max(1, targetBatches);
            return prepareTargets(requested, false);
        }

        /** Estimate whole-line batches once, then shrink until a complete candidate fits. */
        public PreparedPlan fitDownstream(PreparedPlan full, double durationLimit, Predicate<PreparedPlan> fits) {
            if (!fits.test(full)) return full; // One complete integer chain is indivisible.
            if (!targetExtensionAttempted) {
                targetExtensionAttempted = true;
                full = appendAvailableTargets(full, durationLimit, fits);
            }
            return fitTargets(full, (long) Math.min(Integer.MAX_VALUE, durationLimit / full.duration), fits);
        }

        /** Correct an underfilled estimate at most twice; ordinary rounds never enter this path. */
        public PreparedPlan refineShortPlan(PreparedPlan chosen, Predicate<PreparedPlan> fits) {
            if (shortCorrectionAttempted) return chosen;
            shortCorrectionAttempted = true;
            double share = chosen.duration / durationLimit.applyAsDouble(chosen.baseEUt);
            diagnosticShortBefore = share;
            diagnosticShortAfter = share;
            double scale = 1 / share;
            for (long target : chosen.targets) {
                if (target != 0) scale = Math.min(scale, (double) Integer.MAX_VALUE / target);
            }
            for (int attempt = 0; attempt < 2 && scale > 1; attempt++) {
                long[] requested = chosen.targets.clone();
                for (int index : goalOrder) requested[index] = (long) Math.floor(requested[index] * scale);
                if (Arrays.equals(requested, chosen.targets)) break;
                diagnosticShortProbes++;
                PreparedPlan candidate = probeTargets(requested, false);
                if (candidate != null && fits.test(candidate)) {
                    candidate = finishCandidate(candidate);
                    if (candidate != null) {
                        diagnosticShortExtensions++;
                        diagnosticShortAfter = candidate.duration / durationLimit.applyAsDouble(candidate.baseEUt);
                        return candidate;
                    }
                }
                double rejectedShare = candidate == null ? 0
                    : candidate.duration / durationLimit.applyAsDouble(candidate.baseEUt);
                // A measured time rejection brackets the target. Missing input only permits a smaller estimate.
                scale = rejectedShare > 1 ? 1 + (scale - 1) * (1 - share) / (rejectedShare - share)
                    : 1 + (scale - 1) / 2;
            }
            return chosen;
        }

        private PreparedPlan appendAvailableTargets(PreparedPlan full, double durationLimit,
            Predicate<PreparedPlan> fits) {
            long diagnosticStart = System.nanoTime();
            diagnosticTargetPasses++;
            try {
                var expanded = chainPlanner(Integer.MAX_VALUE)
                    .appendAvailableTargets(full.chain, goalOrder, durationLimit);
                if (Arrays.equals(expanded.targets, full.targets)) return full;
                PreparedPlan candidate = prepareResult(expanded);
                if (!fits.test(candidate)) return full;
                candidate = finishCandidate(candidate);
                if (candidate == null) return full;
                diagnosticTargetExtensions++;
                return candidate;
            } finally {
                diagnosticTargetPassNs += System.nanoTime() - diagnosticStart;
            }
        }

        /** Output blockage only shrinks the selected targets; it never repeats the initial growth estimate. */
        public PreparedPlan reduceDownstream(PreparedPlan full, PreparedPlan blocked, Predicate<PreparedPlan> fits) {
            long multiplier = Integer.MAX_VALUE;
            for (int index : goalOrder) {
                if (full.targets[index] != 0)
                    multiplier = Math.min(multiplier, blocked.targets[index] / full.targets[index]);
            }
            return fitTargets(full, multiplier / 2, fits);
        }

        private PreparedPlan fitTargets(PreparedPlan full, long multiplier, Predicate<PreparedPlan> fits) {
            for (long target : full.targets) {
                if (target != 0) multiplier = Math.min(multiplier, Integer.MAX_VALUE / target);
            }
            for (; multiplier > 1; multiplier /= 2) {
                PreparedPlan candidate = prepareScaledTargets(full, multiplier);
                if (candidate == null || !fits.test(candidate)) continue;
                candidate = finishCandidate(candidate);
                if (candidate != null) return candidate;
            }
            return full;
        }

        private PreparedPlan prepareScaledTargets(PreparedPlan full, long multiplier) {
            long[] requested = full.targets.clone();
            for (int index : goalOrder) requested[index] *= multiplier;
            return probeTargets(requested, false);
        }

        /** Terminal recycling consumes existing leftovers within the remaining work budget. */
        private boolean terminalRecovery(int index) {
            if (!snapshot.stages[index].availableInputsOnly) return false;
            for (Object output : snapshot.routes[index].definition.planningTargets()) {
                if (!retainedOutputs.get(index)
                    .contains(output)) return true;
            }
            return false;
        }

        public PreparedPlan recoverAvailable(PreparedPlan chosen, double durationLimit) {
            double budget = durationLimit - chosen.duration;
            if (budget <= 0) return chosen;
            int[] recovery = new int[goalOrder.length];
            int size = 0;
            for (int index : goalOrder) {
                if (terminalRecovery(index) && checkSources(index, chosen.baseEUt)) recovery[size++] = index;
            }
            if (size == 0) return chosen;
            var recovered = chainPlanner(Integer.MAX_VALUE)
                .recover(chosen.chain, Arrays.copyOf(recovery, size), budget, sourceChecks(chosen.baseEUt).costs);
            if (Arrays.equals(recovered.batches, chosen.chain.batches)) return chosen;
            PreparedPlan result = finishCandidate(prepareResult(recovered));
            return result == null ? chosen : result;
        }

        private PreparedPlan prepareTargets(long[] requested, boolean skipUnavailable) {
            return finishCandidate(probeTargets(requested, skipUnavailable));
        }

        private PreparedPlan probeTargets(long[] requested, boolean skipUnavailable) {
            long diagnosticStart = System.nanoTime();
            diagnosticProbes++;
            try {

                long scale = Arrays.stream(requested)
                    .max()
                    .orElse(1);
                long batchLimit = Math.min(
                    Integer.MAX_VALUE,
                    Math.max(1, Math.min(10000, Config.BottledChaosMaxChainedRecipeBatches)) * Math.max(1, scale));
                BottledChaosChainPlanner.Result result = chainPlanner(batchLimit)
                    .prepare(goalOrder, requested, external, remaining, released, skipUnavailable);
                return result == null ? null : prepareResult(result);

            } finally {
                diagnosticProbeNs += System.nanoTime() - diagnosticStart;
            }
        }

        private BottledChaosChainPlanner chainPlanner(long batchLimit) {
            return new BottledChaosChainPlanner(snapshot.stages, suppliers, new BottledChaosChainPlanner.Conditions() {

                @Override
                public boolean canProcess(int index, long voltage) {
                    return authorized[index] && checkSources(index, voltage);
                }

                @Override
                public double cost(int index, long voltage) {
                    return checkSources(index, voltage) ? sourceChecks(voltage).costs[index] : Double.POSITIVE_INFINITY;
                }

                @Override
                public long pools(int index, long voltage) {
                    return sourceChecks(voltage).selections[index].pools;
                }

                @Override
                public Map<Object, Long> inputs(int index, long voltage) {
                    return snapshot.routes[index].work.groups.isEmpty() || !checkSources(index, voltage) ? null
                        : sourceChecks(voltage).selections[index].inputs;
                }

                @Override
                public long retainedOutput(int index, Object key, long amount) {
                    return retainedOutputs.get(index)
                        .contains(key) ? amount : 0;
                }

                @Override
                public boolean terminalRecovery(int index) {
                    return ExecutionPlanner.this.terminalRecovery(index);
                }
            }, virtualItems, batchLimit);
        }

        private PreparedPlan prepareResult(BottledChaosChainPlanner.Result result) {
            return new PreparedPlan(
                snapshot.familyNames[snapshot.familyOfEntry[entry]],
                entry,
                result.voltage,
                Math.max(1, result.duration),
                result.pools,
                result.consumed,
                result.outputs,
                result.remaining,
                result.produced,
                materials.amountLimit(),
                (int) Math.max(1, result.batches[entry]),
                result);
        }

        private PreparedPlan finishCandidate(PreparedPlan plan) {
            return deferCompletion ? plan : completePlan(plan);
        }

        public PreparedPlan completePlan(PreparedPlan plan) {
            if (plan == null || plan.itemConsumption != null) return plan;
            BottledChaosChainPlanner.Result result = plan.chain;
            try {
                List<Requirement> requirements = new ArrayList<>();
                Map<Object, Long> internalPresence = new LinkedHashMap<>(remaining);
                for (int index = 0; index < result.batches.length; index++) {
                    if (result.batches[index] == 0) continue;
                    requirements.addAll(
                        BottledChaosItemInputs.scale(
                            snapshot.routes[index].definition.data().itemRequirements,
                            result.batches[index],
                            1,
                            null));
                    snapshot.routes[index].definition.data().outputs
                        .forEach((key, amount) -> internalPresence.put(key, 1L));
                }
                long diagnosticMatchStart = System.nanoTime();
                plan.itemConsumption = BottledChaosItemInputs
                    .allocate(requirements, result.consumed, internalPresence, matching);
                diagnosticMatchNs += System.nanoTime() - diagnosticMatchStart;
            } catch (ArithmeticException overflow) {
                return null;
            }
            if (plan.itemConsumption == null) return null;
            parameters.resetSourceUsage();
            for (int index : snapshot.entryOrder) {
                if (result.batches[index] == 0) continue;
                SourceSelection selected = sourceChecks(result.voltage).selections[index];
                for (TimeKey time : selected.sources) {
                    parameters.configureSource(time.map(), result.voltage, time.requirements());
                    parameters.recordSourceUsage(time.map());
                }
            }
            plan.upkeep = Collections.unmodifiableList(new ArrayList<>(parameters.upkeepDemands()));
            plan.roundItems = Collections.unmodifiableList(new ArrayList<>(parameters.roundItemDemands()));
            return plan;
        }
    }

    /** NEI reads the same net materials and timing groups directly, without replaying the runtime planner. */
    public static List<DisplayRecipe> displayRecipes() {
        RouteCache snapshot = cache;
        return snapshot == null ? Collections.emptyList() : snapshot.displays;
    }

    public static int maxDisplaySourcePools() {
        RouteCache snapshot = cache;
        return snapshot == null ? 12 : snapshot.maxDisplaySourcePools;
    }

    private static List<DisplayRecipe> buildDisplayRecipes(RouteCache snapshot) {
        List<DisplayRecipe> result = new ArrayList<>(snapshot.routes.length);
        // 1. Read net materials in production order; folded input variants share their primary display entry.
        for (int displayOrder = 0; displayOrder < snapshot.displayOrder.length; displayOrder++) {
            int entryIndex = snapshot.displayOrder[displayOrder];
            CachedRoute cachedRoute = snapshot.routes[entryIndex];
            if (cachedRoute.definition.inputMultiplier() > 0 || cachedRoute.definition.hiddenDisplay()) continue;
            ItemStack[][] inputAlternatives = cachedRoute.definition.inputAlternatives();
            if (entryIndex == cachedRoute.definition.tagIndex()) for (CachedRoute alternative : snapshot.routes) {
                if (alternative.definition.tagIndex() != cachedRoute.definition.tagIndex()
                    || alternative.definition.inputMultiplier() == 0) continue;
                inputAlternatives = new ItemStack[][] { {
                    ((TST_ItemID) cachedRoute.definition.balanceRoot()).getItemStack(1)
                        .copy(),
                    ((TST_ItemID) alternative.definition.balanceRoot())
                        .getItemStack(alternative.definition.inputMultiplier())
                        .copy() } };
                break;
            }
            RecipeData route = cachedRoute.definition.data();
            List<ItemStackLong> items = new ArrayList<>();
            List<FluidStackLong> fluids = new ArrayList<>();
            route.inputs.forEach((key, amount) -> appendResourceStack(key, amount, items, fluids));
            route.catalysts.forEach(
                (key, amount) -> { if (!route.inputs.containsKey(key)) appendResourceStack(key, 0, items, fluids); });
            // Keep consumed materials first, then catalysts and selector circuits; preserve each group's order.
            items.sort(
                Comparator.comparingInt(
                    input -> GTUtility.isAnyIntegratedCircuit(input.itemStack()) ? 2 : input.stackSize() == 0 ? 1 : 0));
            // 2. Normalize this entry at its own base power using the default source-machine parameters.
            Map<RecipeMap<?>, Double> timeByMap = new LinkedHashMap<>();
            double totalDuration = 0;
            for (var time : route.times.entrySet()) {
                double part = calculateTimePart(
                    time.getKey(),
                    time.getValue(),
                    DEFAULT_PARAMETERS,
                    cachedRoute.definition.baseEUt());
                totalDuration = BottledChaosRecipeSupport.addDuration(totalDuration, part, 1);
                RecipeMap<?> map = time.getKey()
                    .map();
                timeByMap.put(map, BottledChaosRecipeSupport.addDuration(timeByMap.getOrDefault(map, 0.0), part, 1));
            }
            List<RecipeMapDuration> processingTimes = new ArrayList<>(timeByMap.size());
            timeByMap.forEach((map, ticks) -> processingTimes.add(new RecipeMapDuration(map, ticks)));
            // 3. Build copied display templates with the exact long quantities and grouped source times.
            result.add(
                new DisplayRecipe(
                    cachedRoute.definition.tagIndex(),
                    displayOrder,
                    cachedRoute.definition.requiredTagMask(),
                    inputAlternatives,
                    snapshot.keyOutputs,
                    cachedRoute.definition.baseEUt(),
                    Collections.unmodifiableList(items),
                    Collections.unmodifiableList(fluids),
                    Collections.unmodifiableList(processingTimes),
                    createOutputPlan(route.outputs, totalDuration, 1)));
        }
        return Collections.unmodifiableList(result);
    }

    /** Union of actual source pools required by authorized entries; computed from the prepared cache. */
    public static long requiredPoolMask(long tagMask) {
        RouteCache snapshot = cache;
        long pools = 0;
        if (snapshot != null) for (CachedRoute route : snapshot.routes) if (route.isAuthorized(tagMask)) {
            pools |= route.poolMask;
            for (TimeKey key : route.definition.data().times.keySet()) {
                var group = key.alternatives();
                if (group != null) for (RecipeMap<?> pool : group.versions.keySet())
                    pools |= BottledChaosMachineConfiguration.mask(pool);
            }
        }
        return pools;
    }

    // Cache availability.
    public static boolean isReady() {
        return cache != null;
    }

    /** One immutable compiled definition, plus execution-only derived work and pool requirements. */
    @Desugar
    private record CachedRoute(CompiledRoute definition, SourceWork work, long poolMask) {

        boolean isAuthorized(long tagMask) {
            return (tagMask & definition.requiredTagMask()) == definition.requiredTagMask();
        }
    }

    // Families prioritize entry lookup and label saved materials; they do not constrain the execution graph.
    private static final class RouteCache {

        final CachedRoute[] routes;
        final int[] entryOrder;
        final int[] displayOrder;
        final ChainLink[] links;
        final Set<Object> keyOutputs;
        final int[][] familyEntries;
        final String[] familyNames;
        final int[] familyOfEntry;
        final BottledChaosChainPlanner.Stage[] stages;
        final List<DisplayRecipe> displays;
        final int maxDisplaySourcePools;

        RouteCache(CachedRoute[] routes, int[] entryOrder, int[] displayOrder, ChainLink[] links,
            Set<Object> keyOutputs) {
            this.routes = routes;
            this.entryOrder = entryOrder;
            this.displayOrder = displayOrder;
            this.links = links;
            this.keyOutputs = keyOutputs;
            stages = new BottledChaosChainPlanner.Stage[routes.length];
            for (int entry = 0; entry < routes.length; entry++) {
                RecipeData data = routes[entry].definition.data();
                stages[entry] = new BottledChaosChainPlanner.Stage(
                    data.inputs,
                    data.outputs,
                    data.catalysts,
                    routes[entry].definition.baseEUt(),
                    routes[entry].definition.availableInputsOnly());
            }
            Map<String, Integer> families = new LinkedHashMap<>();
            familyOfEntry = new int[routes.length];
            int[] counts = new int[routes.length];
            for (int entry : entryOrder) {
                String name = CredentialModel.processingFamily(routes[entry].definition.requiredTagMask());
                int family = families.computeIfAbsent(name, ignored -> families.size());
                familyOfEntry[entry] = family;
                counts[family]++;
            }
            familyNames = families.keySet()
                .toArray(new String[0]);
            familyEntries = new int[familyNames.length][];
            for (int family = 0; family < familyEntries.length; family++)
                familyEntries[family] = new int[counts[family]];
            Arrays.fill(counts, 0);
            for (int entry : entryOrder) {
                int family = familyOfEntry[entry];
                familyEntries[family][counts[family]++] = entry;
            }
            displays = buildDisplayRecipes(this);
            maxDisplaySourcePools = displays.stream()
                .mapToInt(
                    display -> display.processingTimes()
                        .size())
                .max()
                .orElse(12);
        }

        private Map<Object, Long> collectAvailableInputs(AuthorizedGraph graph,
            BottledChaosItemInputs.Matching matching, FluidStack[] fluids, Set<TST_ItemID> virtualItems) {
            Map<Object, Long> available = new LinkedHashMap<>();
            matching.slotsByKey(graph.itemRequirements)
                .forEach((key, slots) -> {
                    for (int slot = 0; slot < slots.length; slot++) {
                        if (!slots[slot]) continue;
                        int amount = matching.items[slot].stackSize;
                        available.merge(key, (long) amount, Math::addExact);
                        if (amount == 0) virtualItems.add(key);
                    }
                });
            if (fluids != null) for (FluidStack stack : fluids) {
                if (stack == null || stack.amount <= 0) continue;
                FluidId key = FluidId.create(stack);
                if (graph.requiredInputs.contains(key)) available.merge(key, (long) stack.amount, Math::addExact);
            }
            return available;
        }

        private boolean hasAuthorizedConsumer(int source, long mask, Object key) {
            for (ChainLink link : links) {
                if (link.source() == source && link.key()
                    .equals(key) && routes[link.target()].isAuthorized(mask)) return true;
            }
            return false;
        }

    }

    private static long takeAmount(Map<Object, Long> amounts, Object key, long needed) {
        long stored = amounts.getOrDefault(key, 0L);
        long taken = Math.min(stored, needed);
        if (stored == taken) amounts.remove(key);
        else amounts.put(key, stored - taken);
        return taken;
    }

    private static double calculateTimePart(TimeKey key, long rawTicks, BottledChaosSourceParameters parameters,
        long recipeVoltage) {
        if (key.eut() == 0) return 0;
        if (parameters.energyOnly(key.map())) return rawTicks * (double) key.eut()
            * parameters.euMultiplier(
                key.map(),
                key.requirements()
                    .heat())
            / parameters.quantitySpeedMultiplier(key.map())
            / recipeVoltage;
        double sourceSpeed = parameters.speedMultiplier(key.map());
        double sourceDuration = Math.max(parameters.minimumSourceDuration(key.map()), key.recipeTicks() / sourceSpeed);
        return BottledChaosOverclock.calculateNormalizedDuration(
            rawTicks,
            key.eut(),
            Math.min(recipeVoltage, parameters.maximumSourcePower(key.map())),
            parameters.parallel(key.map()),
            key.recipeTicks() / sourceDuration * parameters.quantitySpeedMultiplier(key.map()),
            parameters.euMultiplier(
                key.map(),
                key.requirements()
                    .heat()),
            parameters.timeDivisor(
                key.map(),
                key.requirements()
                    .heat()),
            parameters.powerMultiplier(
                key.map(),
                key.requirements()
                    .heat()),
            parameters.maximumOverclocks(
                key.map(),
                key.requirements()
                    .casingTier()),
            parameters.perfectOverclocks(
                key.map(),
                key.requirements()
                    .heat(),
                key.requirements()
                    .casingTier()),
            parameters.overclockLimitAlternatives(key.map()));
    }

    @Desugar
    private record SourceSelection(double duration, long pools, List<TimeKey> sources, Map<Object, Long> omittedInputs,
        Map<Object, Long> inputs) {}

    /** Partition source work once at startup, keeping ordinary routes on the direct calculation path. */
    private static final class SourceWork {

        final RecipeData data;
        final Map<TimeKey, Long> ordinary = new LinkedHashMap<>();
        final Map<BottledChaosProcessAlternatives.Group, Map<TimeKey, Long>> groups = new LinkedHashMap<>();

        SourceWork(RecipeData data) {
            this.data = data;
            for (var entry : data.times.entrySet()) {
                var group = entry.getKey()
                    .alternatives();
                if (group == null) ordinary.put(entry.getKey(), entry.getValue());
                else groups.computeIfAbsent(group, ignored -> new LinkedHashMap<>())
                    .put(entry.getKey(), entry.getValue());
            }
        }
    }

    /** Select a cached native-pool version once per plan/voltage; unrelated pool work remains mandatory. */
    private static SourceSelection selectSources(SourceWork work, BottledChaosSourceParameters parameters,
        long voltage) {
        RecipeData data = work.data;
        SourceSelection result = evaluateSources(work.ordinary, parameters, voltage, 1);
        if (result == null) return null;
        for (var segment : work.groups.entrySet()) {
            var group = segment.getKey();
            boolean installed = segment.getValue()
                .keySet()
                .stream()
                .allMatch(key -> parameters.isSourceAvailable(key.map()));
            // Installing Miracle Door selects its native material contract as well as its timing.
            // Its gas-free inputs stay the same when the chain's common voltage rises.
            boolean stellarForge = parameters.isSourceAvailable(BottledChaosProcessAlternatives.STELLAR_FORGE)
                && group.versions.containsKey(BottledChaosProcessAlternatives.STELLAR_FORGE);
            SourceSelection best = installed && !stellarForge
                ? evaluateSources(segment.getValue(), parameters, voltage, 1)
                : null;
            for (var pool : group.versions.entrySet()) {
                if (stellarForge && pool.getKey() != BottledChaosProcessAlternatives.STELLAR_FORGE) continue;
                if (!parameters.isSourceAvailable(pool.getKey())) continue;
                for (var version : pool.getValue()) {
                    SourceSelection candidate = evaluateSources(version.times(), parameters, voltage, version.scale());
                    if (candidate == null || best != null && candidate.duration >= best.duration) continue;
                    candidate.omittedInputs.putAll(version.omittedInputs());
                    best = candidate;
                }
            }
            // Preserve the ordinary pool failures when no installed native version can run.
            if (best == null && !installed && !stellarForge)
                best = evaluateSources(segment.getValue(), parameters, voltage, 1);
            if (best == null) return null;
            result.sources.addAll(best.sources);
            for (var omitted : best.omittedInputs.entrySet())
                result.omittedInputs.merge(omitted.getKey(), omitted.getValue(), Math::addExact);
            result = new SourceSelection(
                BottledChaosRecipeSupport.addDuration(result.duration, best.duration, 1),
                result.pools | best.pools,
                result.sources,
                result.omittedInputs,
                null);
        }
        // A fully substituted gas demand disappears, including any source tick rounding difference.
        for (var omitted : result.omittedInputs.entrySet()) {
            boolean stillConsumed = false;
            for (TimeKey source : result.sources)
                if (source.source() != null && source.source().inputs.containsKey(omitted.getKey())) {
                    stillConsumed = true;
                    break;
                }
            if (!stillConsumed) omitted.setValue(data.inputs.getOrDefault(omitted.getKey(), 0L));
        }
        Map<Object, Long> inputs = data.inputs;
        if (!result.omittedInputs.isEmpty()) {
            inputs = new LinkedHashMap<>(inputs);
            for (var omitted : result.omittedInputs.entrySet())
                takeAmount(inputs, omitted.getKey(), omitted.getValue());
        }
        return new SourceSelection(result.duration, result.pools, result.sources, result.omittedInputs, inputs);
    }

    private static SourceSelection evaluateSources(Map<TimeKey, Long> work, BottledChaosSourceParameters parameters,
        long voltage, double scale) {
        double duration = 0;
        long pools = 0;
        List<TimeKey> sources = new ArrayList<>(work.size());
        for (var entry : work.entrySet()) {
            TimeKey key = entry.getKey();
            parameters.configureSource(key.map(), voltage, key.requirements());
            if (!parameters.canProcess(key.map(), voltage, key.requirements())) return null;
            duration = BottledChaosRecipeSupport
                .addDuration(duration, calculateTimePart(key, entry.getValue(), parameters, voltage) * scale, 1);
            pools |= BottledChaosMachineConfiguration.mask(key.map());
            sources.add(key);
        }
        return new SourceSelection(
            duration,
            BottledChaosMachineConfiguration.withDependencies(pools),
            sources,
            new LinkedHashMap<>(),
            null);
    }

    /** One candidate: exact input consumption and carry, with outputs finalized only when checking capacity. */
    public static final class PreparedPlan {

        private final String family;
        private final int entry;
        private final long baseEUt;
        private final double duration;
        private List<BottledChaosUpkeep.Demand> upkeep;
        private List<BottledChaosUpkeep.ItemDemand> roundItems;
        private final long requiredPools;
        private final Map<Object, Long> consumed;
        private final Map<Object, Long> outputs;
        private final Map<Object, Long> remainingCache;
        private final Map<Object, Long> pendingCache;
        private final long cacheLimit;
        private Plan outputPlan;
        private final int executions;
        private final long[] targets;
        private final BottledChaosChainPlanner.Result chain;
        private BottledChaosItemInputs.Consumption itemConsumption;

        private PreparedPlan(String family, int entry, long baseEUt, double duration, long requiredPools,
            Map<Object, Long> consumed, Map<Object, Long> outputs, Map<Object, Long> remainingCache,
            Map<Object, Long> pendingCache, long cacheLimit, int executions, BottledChaosChainPlanner.Result chain) {
            this.family = family;
            this.entry = entry;
            this.baseEUt = baseEUt;
            this.duration = duration;
            this.requiredPools = requiredPools;
            this.consumed = new LinkedHashMap<>(consumed);
            this.outputs = outputs;
            this.remainingCache = new LinkedHashMap<>(remainingCache);
            this.pendingCache = pendingCache;
            this.cacheLimit = cacheLimit;
            this.executions = executions;
            this.targets = chain.targets;
            this.chain = chain;
        }

        public String family() {
            return family;
        }

        public int entry() {
            return entry;
        }

        public double durationTicks() {
            return duration;
        }

        public long baseEUt() {
            return baseEUt;
        }

        public long requiredPools() {
            return requiredPools;
        }

        public List<BottledChaosUpkeep.Demand> upkeep() {
            return upkeep;
        }

        public List<BottledChaosUpkeep.ItemDemand> roundItems() {
            return roundItems;
        }

        public void prepareOutputs() {
            if (outputPlan == null) {
                BottledChaosChainPlanner.releaseExcess(cacheLimit, remainingCache, pendingCache, outputs);
                outputPlan = createOutputPlan(outputs, duration, executions);
            }
        }

        public Plan outputPlan() {
            return outputPlan;
        }

        public BottledChaosMaterialStacks pendingCache() {
            return BottledChaosMaterialStacks.fromAmounts(pendingCache);
        }

        public void commitCache(BottledChaosMaterialCache materials) {
            materials.replace(family, BottledChaosMaterialStacks.fromAmounts(remainingCache));
            materials.remember(family, entry);
        }

        /** Consume only the caller's working copies, after power, time and output checks have succeeded. */
        public void consumeInputCopies(ItemStack[] items, FluidStack[] fluids) {
            Map<Object, Long> needed = new LinkedHashMap<>(consumed);
            needed.keySet()
                .removeIf(key -> key instanceof TST_ItemID);
            itemConsumption.validate(items);
            Map<Object, Long> fluidCheck = new LinkedHashMap<>(needed);
            if (fluids != null) for (FluidStack stack : fluids) {
                if (stack != null && stack.amount > 0) takeAmount(fluidCheck, FluidId.create(stack), stack.amount);
            }
            if (!fluidCheck.isEmpty()) throw new IllegalStateException("input fluids changed after allocation");
            itemConsumption.consume(items);
            if (fluids != null) for (FluidStack stack : fluids) {
                if (stack != null && stack.amount > 0)
                    stack.amount -= (int) takeAmount(needed, FluidId.create(stack), stack.amount);
            }
            if (!needed.isEmpty()) throw new IllegalStateException("unrepresentable input consumption: " + needed);
        }
    }

    private static Plan createOutputPlan(Map<Object, Long> outputs, double duration, int executions) {
        List<ItemStackLong> items = new ArrayList<>();
        List<FluidStackLong> fluids = new ArrayList<>();
        outputs.forEach((key, amount) -> appendResourceStack(key, amount, items, fluids));
        return new Plan(
            duration,
            Collections.unmodifiableList(items),
            Collections.unmodifiableList(fluids),
            executions);
    }

    /**
     * Independent long outputs and actual entry batch count; runtime duration includes every executed stage.
     */
    @Desugar
    public record Plan(double durationTicks, List<ItemStackLong> itemOutputs, List<FluidStackLong> fluidOutputs,
        int executions) {}

    // NEI display result types.
    /**
     * NEI time summary for one source pool, normalized at the displayed route voltage and default machine
     * parameters.
     */
    @Desugar
    public record RecipeMapDuration(RecipeMap<?> map, double durationTicks) {}

    /**
     * NEI row data: authorization, independent item alternatives, global key outputs, complete long quantities
     * and grouped processing times. Key outputs restrict lookup without removing any displayed material.
     */
    @Desugar
    public record DisplayRecipe(int tagIndex, int displayOrder, long requiredTagMask, ItemStack[][] inputAlternatives,
        Set<Object> keyOutputs, long baseEUt, List<ItemStackLong> itemInputs, List<FluidStackLong> fluidInputs,
        List<RecipeMapDuration> processingTimes, Plan plan) {}

}
