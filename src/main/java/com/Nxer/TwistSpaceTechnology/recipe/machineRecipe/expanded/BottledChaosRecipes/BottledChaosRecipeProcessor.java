package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Furnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMaterialCache;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosOverclock;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosUpkeep;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosItemInputs.Requirement;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;
import com.Nxer.TwistSpaceTechnology.util.recipes.RecipeMaterialUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;

/**
 * Shared startup preprocessing, route cache and execution planning for Bottled Chaos.
 * Prepared routes retain net materials and source timing groups for execution and NEI display.
 */
public final class BottledChaosRecipeProcessor {

    private static final int MAX_GRAPH_PASSES = 256;
    private static final MachineParameters DEFAULT_PARAMETERS = new MachineParameters() {};
    private static volatile RouteCache cache;

    // Core entry points: startup publication, execution preparation and NEI display.
    public static synchronized void initializeRecipeCache(Consumer<RecipeCatalog> registerChains) {
        cache = null;
        try {
            // 1. Register the route definitions, recipe markers and reagent expansions.
            RecipeCatalog catalog = new RecipeCatalog();
            registerChains.accept(catalog);
            // 2. Validate registrations and publish the credential capacity costs.
            catalog.validateRegisteredChains();
            catalog.publishCapacityCosts();
            // 3. Scan the selected source pools and prepare exact recipe batches.
            catalog.scanMarkedRecipes();
            // 4. Balance, expand and validate the routes, preserving their registered entry indexes.
            RecipeData[] preparedRoutes = catalog.composeRecipeChains();
            Set<RecipeMap<?>> selectedPools = new LinkedHashSet<>();
            selectedPools.add(RecipeMaps.fusionRecipes);
            for (RecipeData route : preparedRoutes)
                for (TimeKey time : route.times.keySet()) selectedPools.add(time.map());
            BottledChaosMachineConfiguration.initialize(selectedPools.toArray(new RecipeMap<?>[0]));
            CachedRoute[] routes = new CachedRoute[preparedRoutes.length];
            for (int i = 0; i < routes.length; i++) {
                RecipeChain chain = catalog.chains.get(i);
                Object[] targets = catalog.executionTargets.getOrDefault(i, chain.terminals());
                for (Object target : targets) {
                    if (!preparedRoutes[i].outputs.containsKey(target))
                        throw new IllegalStateException("execution target is not a route output: " + target);
                }
                routes[i] = new CachedRoute(
                    preparedRoutes[i],
                    chain.root(),
                    catalog.keyInputs(i),
                    catalog.keyOutputs(i),
                    targets,
                    chain.baseEUt(),
                    chain.tagIndex(),
                    chain.inputMultiplier(),
                    chain.requiredTagMask(),
                    catalog.availableInputChains.contains(i),
                    catalog.displayInputAlternatives.get(i),
                    catalog.hiddenDisplayEntries.contains(i),
                    BottledChaosMachineConfiguration.withDependencies(
                        preparedRoutes[i].times.keySet()
                            .stream()
                            .mapToLong(time -> BottledChaosMachineConfiguration.mask(time.map()))
                            .reduce(0L, (left, right) -> left | right)));
            }
            // 5. Publish complete routes and immutable input checkers; no scanned source recipe survives startup.
            cache = new RouteCache(
                routes,
                catalog.entryOrder(),
                catalog.displayOrder(),
                catalog.links.toArray(new ChainLink[0]));
            TwistSpaceTechnology.LOG.info("Bottled Chaos resolved {} processing steps.", catalog.resolved.size());
        } catch (RuntimeException exception) {
            TwistSpaceTechnology.LOG
                .error("Bottled Chaos recipe resolution failed; processing routes are disabled.", exception);
        }
    }

    public static PreparedPlan prepareExecution(long routeMask, ItemStack[] itemInputs, FluidStack[] fluidInputs) {
        return prepareExecution(routeMask, itemInputs, fluidInputs, DEFAULT_PARAMETERS);
    }

    public static PreparedPlan prepareExecution(long routeMask, ItemStack[] itemInputs, FluidStack[] fluidInputs,
        MachineParameters parameters) {
        BottledChaosMaterialCache materials = new BottledChaosMaterialCache();
        for (int entry : candidateEntries(routeMask, materials)) {
            PreparedPlan prepared = prepareExecution(
                routeMask,
                entry,
                1,
                itemInputs,
                fluidInputs,
                parameters,
                materials);
            if (prepared != null) return prepared;
        }
        return null;
    }

    /** Last successful family/entry first, then the fixed family directory, as in GT's buffered recipe lookup. */
    public static int[] candidateEntries(long routeMask, BottledChaosMaterialCache materials) {
        RouteCache snapshot = cache;
        if (snapshot == null || routeMask == 0) return new int[0];
        int[] result = new int[snapshot.routes.length];
        int size = 0;
        for (int pass = 0; pass < 2; pass++) {
            for (int family = 0; family < snapshot.familyEntries.length; family++) {
                if (snapshot.familyNames[family].equals(materials.lastFamily()) != (pass == 0)) continue;
                int preferred = materials.lastEntry();
                if (preferred >= 0 && preferred < snapshot.routes.length
                    && snapshot.familyOfEntry[preferred] == family
                    && snapshot.routes[preferred].isAuthorized(routeMask)) result[size++] = preferred;
                for (int entry : snapshot.familyEntries[family]) {
                    if (entry != preferred && snapshot.routes[entry].isAuthorized(routeMask)) result[size++] = entry;
                }
            }
        }
        return Arrays.copyOf(result, size);
    }

    /** Waila groups final targets before other key outputs, then unmarked coproducts. */
    public static Map<Object, Integer> outputPriorities(long routeMask) {
        RouteCache snapshot = cache;
        if (snapshot == null || routeMask == 0) return Collections.emptyMap();
        Map<Object, Integer> priorities = new LinkedHashMap<>();
        for (int entry : snapshot.entryOrder) {
            CachedRoute route = snapshot.routes[entry];
            if (!route.isAuthorized(routeMask)) continue;
            for (Object key : route.lookupOutputs) priorities.putIfAbsent(key, 1);
            for (Object key : route.targetOutputs) {
                if (!snapshot.hasAuthorizedConsumer(entry, routeMask, key)) priorities.put(key, 0);
            }
        }
        return priorities;
    }

    /** Integer final-product targets determine upstream supply; graph cycles require already available seed. */
    public static PreparedPlan prepareExecution(long routeMask, int entry, int externalBatches, ItemStack[] items,
        FluidStack[] fluids, MachineParameters parameters, BottledChaosMaterialCache materials) {
        return executionPlanner(routeMask, entry, items, fluids, parameters, materials).prepare(externalBatches);
    }

    public static ExecutionPlanner executionPlanner(long routeMask, int entry, ItemStack[] items, FluidStack[] fluids,
        MachineParameters parameters, BottledChaosMaterialCache materials) {
        return executionPlanner(routeMask, entry, items, fluids, parameters, materials, new PlanningCache());
    }

    /** One current authorization graph per processing group; quantities always belong to a fresh planner. */
    public static final class PlanningCache {

        private RouteCache snapshot;
        private long routeMask;
        private AuthorizedGraph graph;
        private BottledChaosItemInputs.Matching matching;

        private AuthorizedGraph get(RouteCache current, long mask) {
            if (current != snapshot || graph == null || mask != routeMask) {
                snapshot = current;
                routeMask = mask;
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

        private AuthorizedGraph(RouteCache snapshot, long routeMask) {
            authorized = new boolean[snapshot.routes.length];
            List<Map<Object, List<Integer>>> incoming = new ArrayList<>();
            for (int index = 0; index < authorized.length; index++) {
                authorized[index] = snapshot.routes[index].isAuthorized(routeMask);
                incoming.add(new LinkedHashMap<>());
                retainedOutputs.add(new LinkedHashSet<>());
                downstream.add(new LinkedHashSet<>());
            }
            for (ChainLink link : snapshot.links) {
                if (!authorized[link.target]) continue;
                cachedInputs.add(link.key);
                // An already-paid input stays usable even if none of its suppliers is currently authorized.
                List<Integer> sources = incoming.get(link.target)
                    .computeIfAbsent(link.key, ignored -> new ArrayList<>());
                if (!authorized[link.source]) continue;
                sources.add(link.source);
                retainedOutputs.get(link.source)
                    .add(link.key);
                downstream.get(link.source)
                    .add(link.target);
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
                RecipeData data = snapshot.routes[index].data;
                itemRequirements.addAll(data.itemRequirements);
                requiredInputs.addAll(data.inputs.keySet());
                requiredInputs.addAll(data.catalysts.keySet());
            }
            orders = new int[snapshot.routes.length][];
            completeTargets = new long[snapshot.routes.length][];
            terminals = new boolean[snapshot.routes.length];
            for (int index = 0; index < terminals.length; index++) {
                for (Object output : snapshot.routes[index].targetOutputs) {
                    if (!retainedOutputs.get(index)
                        .contains(output)) terminals[index] = true;
                }
            }
        }

    }

    public static ExecutionPlanner executionPlanner(long routeMask, int entry, ItemStack[] items, FluidStack[] fluids,
        MachineParameters parameters, BottledChaosMaterialCache materials, PlanningCache planningCache) {
        return new ExecutionPlanner(
            cache,
            routeMask,
            entry,
            items,
            fluids,
            parameters,
            materials,
            planningCache.get(cache, routeMask),
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
        private final long routeMask;
        private int entry;
        private final ItemStack[] items;
        private final FluidStack[] fluids;
        private final MachineParameters parameters;
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

        private ExecutionPlanner(RouteCache snapshot, long routeMask, int entry, ItemStack[] items, FluidStack[] fluids,
            MachineParameters parameters, BottledChaosMaterialCache materials, AuthorizedGraph graph,
            BottledChaosItemInputs.Matching matching) {
            this.graph = graph;
            this.matching = matching;
            suppliers = graph == null ? null : graph.suppliers;
            retainedOutputs = graph == null ? null : graph.retainedOutputs;
            downstream = graph == null ? null : graph.downstream;
            authorized = graph == null ? null : graph.authorized;
            this.snapshot = snapshot;
            this.routeMask = routeMask;
            this.entry = entry;
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
                    new boolean[snapshot.routes.length]));
        }

        private boolean checkSources(int index, long voltage) {
            if (!routeFilter.allows(voltage, snapshot.routes[index].poolMask)) return false;
            SourceChecks checks = sourceChecks(voltage);
            if (!checks.checked[index]) {
                long diagnosticSourceStart = System.nanoTime();
                diagnosticSourceCalls++;
                checks.checked[index] = true;
                checks.allowed[index] = true;
                for (var time : snapshot.routes[index].data.times.entrySet()) {
                    configureSource(time.getKey(), parameters, voltage);
                    if (!canProcessSource(time.getKey(), parameters, voltage)) {
                        checks.allowed[index] = false;
                        break;
                    }
                    checks.costs[index] = BottledChaosRecipeSupport.addDuration(
                        checks.costs[index],
                        calculateTimePart(time.getKey(), time.getValue(), parameters, voltage),
                        1);
                }
                diagnosticSourceNs += System.nanoTime() - diagnosticSourceStart;
            }
            return checks.allowed[index];
        }

        @Desugar
        private record SourceChecks(double[] costs, boolean[] checked, boolean[] allowed) {}

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
        public PreparedPlan prepare(int targetBatches) {
            if (snapshot == null || !snapshot.routes[entry].isAuthorized(routeMask)) return null;
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
                        for (Object terminal : snapshot.routes[index].targetOutputs) {
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
            for (Object output : snapshot.routes[index].targetOutputs) {
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
                public long pools(int index) {
                    return snapshot.routes[index].poolMask;
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
                result.duration,
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
                        BottledChaosItemInputs
                            .scale(snapshot.routes[index].data.itemRequirements, result.batches[index], 1, null));
                    snapshot.routes[index].data.outputs.forEach((key, amount) -> internalPresence.put(key, 1L));
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
                CachedRoute route = snapshot.routes[index];
                for (var time : route.data.times.entrySet()) {
                    configureSource(time.getKey(), parameters, result.voltage);
                    parameters.recordSourceUsage(
                        time.getKey()
                            .map());
                }
            }
            plan.upkeep = Collections.unmodifiableList(new ArrayList<>(parameters.upkeepDemands()));
            return plan;
        }
    }

    /** NEI reads the same net materials and timing groups directly, without replaying the runtime planner. */
    public static List<DisplayRecipe> createDisplayRecipes() {
        RouteCache snapshot = cache;
        if (snapshot == null) return Collections.emptyList();
        List<DisplayRecipe> result = new ArrayList<>(snapshot.routes.length);
        // 1. Read net materials in production order; folded input variants share their primary display entry.
        for (int displayOrder = 0; displayOrder < snapshot.displayOrder.length; displayOrder++) {
            int routeIndex = snapshot.displayOrder[displayOrder];
            CachedRoute cachedRoute = snapshot.routes[routeIndex];
            if (cachedRoute.inputMultiplier > 0 || cachedRoute.hiddenDisplay) continue;
            ItemStack[][] inputAlternatives = cachedRoute.displayInputAlternatives;
            if (routeIndex == cachedRoute.tagIndex) for (CachedRoute alternative : snapshot.routes) {
                if (alternative.tagIndex != cachedRoute.tagIndex || alternative.inputMultiplier == 0) continue;
                inputAlternatives = new ItemStack[][] { { ((TST_ItemID) cachedRoute.root).getItemStack(1)
                    .copy(),
                    ((TST_ItemID) alternative.root).getItemStack(alternative.inputMultiplier)
                        .copy() } };
                break;
            }
            RecipeData route = cachedRoute.data;
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
                    cachedRoute.baseEUt);
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
                    cachedRoute.tagIndex,
                    displayOrder,
                    cachedRoute.requiredTagMask,
                    inputAlternatives,
                    cachedRoute.lookupInputs.clone(),
                    cachedRoute.lookupOutputs.clone(),
                    cachedRoute.baseEUt,
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
        if (snapshot != null)
            for (CachedRoute route : snapshot.routes) if (route.isAuthorized(tagMask)) pools |= route.poolMask;
        return pools;
    }

    // Cache availability.
    public static boolean isReady() {
        return cache != null;
    }

    // Startup registrations, source preprocessing and route construction.
    public static final class RecipeCatalog {

        // Flat step storage: each chain owns a contiguous range, without nested recipe lists.
        private final List<RecipeMark> chainSteps = new ArrayList<>();
        private final List<RecipeMark> sequenceSteps = new ArrayList<>();
        private final List<RecipeSequence> recipeSequences = new ArrayList<>();
        private final List<RecipeChain> chains = new ArrayList<>(
            Collections.nCopies(CredentialModel.ROUTE_COUNT, null));
        private final List<ChainLink> links = new ArrayList<>();
        private final List<Expansion> expansions = new ArrayList<>();
        private final List<ReagentRecycling> reagentRecycling = new ArrayList<>();
        private final Set<Object> deferredReagents = new LinkedHashSet<>();
        private final Set<RecipeMark> marks = new LinkedHashSet<>();
        private final Map<RecipeMark, InputSubstitution[]> substitutedInputs = new LinkedHashMap<>();
        private final Map<RecipeMark, Integer> roundedOutputChanceBatches = new LinkedHashMap<>();
        private final Map<Integer, Integer> substitutedChains = new LinkedHashMap<>();
        private final Map<Integer, TST_ItemID> selectorCircuits = new LinkedHashMap<>();
        private final Set<Integer> availableInputChains = new LinkedHashSet<>();
        private final Map<Integer, Object[]> queryInputs = new LinkedHashMap<>();
        private final Map<Integer, Object[]> queryOutputs = new LinkedHashMap<>();
        private final Map<Integer, Object[]> executionTargets = new LinkedHashMap<>();
        private final Map<String, List<Object>> displayRoots = new LinkedHashMap<>();
        private final Map<Integer, ItemStack[][]> displayInputAlternatives = new LinkedHashMap<>();
        private final Set<Integer> hiddenDisplayEntries = new LinkedHashSet<>();
        private final Set<Object> registeredReagentSyntheses = new LinkedHashSet<>();
        private final Set<Object> registeredReagentDecompositions = new LinkedHashSet<>();
        private final Set<RecipeMap<?>> pools = new LinkedHashSet<>();
        private final Set<Object> internalResources = new LinkedHashSet<>();
        private final Set<Object> externalInputs = new LinkedHashSet<>();
        private final Map<RecipeMark, RecipeData> resolved = new LinkedHashMap<>();

        /** Register by stable credential tag; each route consumes its root and must reach its terminals. */
        public void registerChain(String tag, int capacityCost, long baseEUt, Object root, Object[] terminals,
            RecipeMark... steps) {
            registerChain(tag, capacityCost, baseEUt, root, terminals, null, steps);
        }

        /** Preserve explicit resource boundaries during source composition; key markers determine runtime links. */
        public void registerChain(String tag, int capacityCost, long baseEUt, Object root, Object[] terminals,
            Object[] chainableOutputs, RecipeMark... steps) {
            registerChain(tag, capacityCost, baseEUt, root, terminals, chainableOutputs, 0, steps);
        }

        /** Register a primary entry that also requires another processing tag. */
        public void registerAuthorizedChain(String tag, String additionalTag, int capacityCost, long baseEUt,
            Object root, Object[] terminals, Object[] chainableOutputs, RecipeMark... steps) {
            int additionalIndex = CredentialModel.indexForTag(additionalTag);
            if (additionalIndex < 0) throw new IllegalArgumentException("unknown additional tag: " + additionalTag);
            registerChain(tag, capacityCost, baseEUt, root, terminals, chainableOutputs, 1L << additionalIndex, steps);
        }

        private void registerChain(String tag, int capacityCost, long baseEUt, Object root, Object[] terminals,
            Object[] chainableOutputs, long additionalTags, RecipeMark... steps) {
            int routeIndex = CredentialModel.indexForTag(tag);
            if (routeIndex < 0 || routeIndex >= Long.SIZE
                || chains.get(routeIndex) != null
                || capacityCost <= 0
                || baseEUt <= 0
                || steps.length == 0) throw new IllegalArgumentException("invalid or duplicate recipe chain: " + tag);
            int start = chainSteps.size();
            Collections.addAll(chainSteps, steps);
            chains.set(
                routeIndex,
                new RecipeChain(
                    resourceKey(root),
                    Arrays.stream(terminals)
                        .map(BottledChaosRecipeProcessor::resourceKey)
                        .toArray(),
                    start,
                    chainSteps.size(),
                    capacityCost,
                    baseEUt,
                    routeIndex,
                    null,
                    chainableOutputs == null ? null
                        : Arrays.stream(chainableOutputs)
                            .map(BottledChaosRecipeProcessor::resourceKey)
                            .toArray(),
                    0,
                    (1L << routeIndex) | additionalTags));
        }

        /** Another input for an existing credential; its capacity cost is paid only once. */
        public void registerAlternativeChain(String tag, long baseEUt, Object root, Object[] terminals,
            RecipeMark... steps) {
            registerAlternativeChain(tag, baseEUt, root, terminals, null, steps);
        }

        public void registerAlternativeChain(String tag, long baseEUt, Object root, Object[] terminals,
            Object[] chainableOutputs, RecipeMark... steps) {
            registerAlternativeChain(tag, baseEUt, root, terminals, chainableOutputs, 0, steps);
        }

        /** Additional credential tags authorize this precomputed yield-boost variant. */
        public void registerAugmentedChain(String tag, String additionalTag, long baseEUt, Object root,
            Object[] terminals, Object[] chainableOutputs, RecipeMark... steps) {
            int additionalIndex = CredentialModel.indexForTag(additionalTag);
            if (additionalIndex < 0) throw new IllegalArgumentException("unknown additional tag: " + additionalTag);
            registerAlternativeChain(tag, baseEUt, root, terminals, chainableOutputs, 1L << additionalIndex, steps);
        }

        private void registerAlternativeChain(String tag, long baseEUt, Object root, Object[] terminals,
            Object[] chainableOutputs, long additionalTags, RecipeMark... steps) {
            int tagIndex = CredentialModel.indexForTag(tag);
            if (tagIndex < 0 || chains.get(tagIndex) == null || baseEUt <= 0 || steps.length == 0)
                throw new IllegalArgumentException("invalid alternative recipe chain: " + tag);
            int start = chainSteps.size();
            Collections.addAll(chainSteps, steps);
            chains.add(
                new RecipeChain(
                    resourceKey(root),
                    Arrays.stream(terminals)
                        .map(BottledChaosRecipeProcessor::resourceKey)
                        .toArray(),
                    start,
                    chainSteps.size(),
                    0,
                    baseEUt,
                    tagIndex,
                    null,
                    chainableOutputs == null ? null
                        : Arrays.stream(chainableOutputs)
                            .map(BottledChaosRecipeProcessor::resourceKey)
                            .toArray(),
                    0,
                    (1L << tagIndex) | additionalTags));
        }

        /**
         * Override NEI lookup keys for currently registered entries of this tag; input keys also accept chained
         * supply. Null keeps defaults, an empty array disables a side.
         */
        public void setQueryMarkers(String tag, Object[] inputs, Object[] outputs) {
            int tagIndex = CredentialModel.indexForTag(tag);
            if (tagIndex < 0) throw new IllegalArgumentException("unknown query tag: " + tag);
            Object[] inputKeys = inputs == null ? null
                : Arrays.stream(inputs)
                    .map(BottledChaosRecipeProcessor::resourceKey)
                    .toArray();
            Object[] outputKeys = outputs == null ? null
                : Arrays.stream(outputs)
                    .map(BottledChaosRecipeProcessor::resourceKey)
                    .toArray();
            for (int entry = 0; entry < chains.size(); entry++) {
                RecipeChain chain = chains.get(entry);
                if (chain == null || chain.tagIndex() != tagIndex) continue;
                if (inputKeys != null) queryInputs.put(entry, inputKeys);
                if (outputKeys != null) queryOutputs.put(entry, outputKeys);
            }
        }

        /** Override only the latest entry's key inputs; no inputs disables lookup and incoming recipe links. */
        public void setLatestQueryInputs(String tag, Object... inputs) {
            queryInputs.put(
                latestChainIndex(tag),
                Arrays.stream(inputs)
                    .map(BottledChaosRecipeProcessor::resourceKey)
                    .toArray());
        }

        private Object[] keyInputs(int entry) {
            return queryInputs.getOrDefault(
                entry,
                new Object[] { chains.get(entry)
                    .root() });
        }

        private Object[] keyOutputs(int entry) {
            return queryOutputs.getOrDefault(
                entry,
                chains.get(entry)
                    .terminals());
        }

        /** Select main products for demand planning without changing protected coproducts or NEI markers. */
        public void setExecutionTargets(String tag, Object... outputs) {
            if (outputs.length == 0) throw new IllegalArgumentException("empty execution targets: " + tag);
            executionTargets.put(
                latestChainIndex(tag),
                Arrays.stream(outputs)
                    .map(BottledChaosRecipeProcessor::resourceKey)
                    .toArray());
        }

        /** Order a family's independent NEI stages without changing credential bits or runtime priorities. */
        public void orderDisplayRecipes(String family, Object... roots) {
            List<Object> keys = new ArrayList<>(roots.length);
            for (Object root : roots) keys.add(resourceKey(root));
            displayRoots.put(family, keys);
        }

        /** Fold explicitly registered independent combinations into one NEI row; runtime entries stay separate. */
        public void groupLatestDisplayRecipes(String tag, int variants, ItemStack[]... inputs) {
            int last = latestChainIndex(tag);
            int first = last - variants + 1;
            for (int entry = first; entry <= last; entry++) {
                if (entry < 0 || chains.get(entry) == null
                    || chains.get(entry)
                        .tagIndex()
                        != chains.get(last)
                            .tagIndex())
                    throw new IllegalArgumentException("invalid display recipe group: " + tag);
                if (entry != first) hiddenDisplayEntries.add(entry);
            }
            displayInputAlternatives.put(first, inputs);
        }

        /** Require a non-consumed programming circuit for the latest registered entry of this tag. */
        public void requireCircuit(String tag, int configuration) {
            int tagIndex = CredentialModel.indexForTag(tag);
            if (tagIndex < 0 || configuration < 1 || configuration > 24)
                throw new IllegalArgumentException("invalid route circuit: " + tag);
            selectorCircuits
                .put(latestChainIndex(tag), TST_ItemID.create(GTUtility.getIntegratedCircuit(configuration)));
        }

        /** Recycle existing feed without recursively synthesizing more of that feed. */
        public void useAvailableInputsOnly(String tag) {
            availableInputChains.add(latestChainIndex(tag));
        }

        /** Use one branch's synthesis only for the latest same-tag entry; shared paths remain unchanged. */
        public void registerChainInputSynthesis(String tag, RecipeMap<?> map, Object target, Object inputs,
            int circuit) {
            registerChainInputSynthesis(tag, target, registerRecipeMark(map, inputs, target, circuit, false));
        }

        /** Reuse an already prepared source only within the latest entry of this tag. */
        public void registerChainInputSynthesis(String tag, Object target, RecipeMark recipe) {
            expansions.add(new Expansion(recipe, resourceKey(target), true, latestChainIndex(tag)));
        }

        private int latestChainIndex(String tag) {
            int tagIndex = CredentialModel.indexForTag(tag);
            for (int entry = chains.size() - 1; entry >= 0; entry--) {
                RecipeChain chain = chains.get(entry);
                if (chain != null && chain.tagIndex() == tagIndex) return entry;
            }
            throw new IllegalArgumentException("missing recipe chain: " + tag);
        }

        /**
         * Fold one alternate item input into the primary NEI row, without adding work; runtime entries stay separate.
         */
        public void registerInputAlternative(String tag, TST_ItemID alternative, int multiplier) {
            int tagIndex = CredentialModel.indexForTag(tag);
            if (tagIndex < 0 || chains.get(tagIndex) == null || alternative == null || multiplier <= 0)
                throw new IllegalArgumentException("invalid input alternative: " + tag);
            for (RecipeChain chain : chains) {
                if (chain != null && chain.tagIndex() == tagIndex && chain.inputMultiplier() > 0)
                    throw new IllegalArgumentException("duplicate folded input alternative: " + tag);
            }
            RecipeChain primary = chains.get(tagIndex);
            if (!(primary.root() instanceof TST_ItemID) || primary.root()
                .equals(alternative))
                throw new IllegalArgumentException("input alternatives must use distinct item roots: " + tag);
            chains.add(
                new RecipeChain(
                    alternative,
                    primary.terminals(),
                    chainSteps.size(),
                    chainSteps.size(),
                    0,
                    primary.baseEUt(),
                    tagIndex,
                    primary.root(),
                    primary.chainableOutputs(),
                    multiplier,
                    1L << tagIndex));
        }

        /** Compose a fixed source with the already prepared route it supplies. */
        public void registerRootSubstitution(String tag, String sourceTag, long baseEUt, Object root,
            Object replacedRoot, Object[] terminals, RecipeMark source) {
            int tagIndex = CredentialModel.indexForTag(tag);
            int sourceIndex = CredentialModel.indexForTag(sourceTag);
            if (tagIndex < 0 || sourceIndex < 0
                || chains.get(tagIndex) == null
                || chains.get(sourceIndex) == null
                || baseEUt <= 0) throw new IllegalArgumentException("invalid root substitution: " + tag);
            int start = chainSteps.size();
            chainSteps.add(source);
            substitutedChains.put(chains.size(), sourceIndex);
            chains.add(
                new RecipeChain(
                    resourceKey(root),
                    Arrays.stream(terminals)
                        .map(BottledChaosRecipeProcessor::resourceKey)
                        .toArray(),
                    start,
                    chainSteps.size(),
                    0,
                    baseEUt,
                    tagIndex,
                    resourceKey(replacedRoot),
                    new Object[] {},
                    0,
                    (1L << tagIndex) | (1L << sourceIndex)));
        }

        public RecipeMark markRecipe(RecipeMap<?> map, Object input, Object output, int circuit) {
            internalResources.add(resourceKey(input));
            internalResources.add(resourceKey(output));
            return registerRecipeMark(map, input, output, circuit, false);
        }

        /** Scan the native input, then replace its material form once in the startup cache, keeping all quantities. */
        public RecipeMark markRecipeWithSubstitutedInput(RecipeMap<?> map, TST_ItemID original, TST_ItemID replacement,
            Object output, int circuit) {
            return markRecipeWithSubstitutedInputs(
                map,
                new TST_ItemID[] { original },
                new TST_ItemID[] { replacement },
                output,
                circuit);
        }

        /** Convert a source material form using its exact whole-item ratio, preserving native processing work. */
        public RecipeMark markRecipeWithSubstitutedInput(RecipeMap<?> map, TST_ItemID original, TST_ItemID replacement,
            int multiplier, Object output, int circuit) {
            if (multiplier <= 0) throw new IllegalArgumentException("input substitution requires a positive ratio");
            RecipeMark mark = markRecipeWithSubstitutedInput(map, original, replacement, output, circuit);
            substitutedInputs.put(mark, new InputSubstitution[] { new InputSubstitution(replacement, multiplier) });
            return mark;
        }

        /** Replace multiple source item forms in one startup step; matching still uses all native identities. */
        public RecipeMark markRecipeWithSubstitutedInputs(RecipeMap<?> map, TST_ItemID[] originals,
            TST_ItemID[] replacements, Object output, int circuit) {
            if (originals.length == 0 || originals.length != replacements.length)
                throw new IllegalArgumentException("input substitutions require matching nonempty arrays");
            RecipeMark mark = markRecipe(
                map,
                originals.length == 1 ? originals[0] : originals.clone(),
                output,
                circuit);
            Collections.addAll(internalResources, replacements);
            substitutedInputs.put(
                mark,
                Arrays.stream(replacements)
                    .map(item -> new InputSubstitution(item, 1))
                    .toArray(InputSubstitution[]::new));
            return mark;
        }

        /** Balance a finite source sequence and cancel its returned input once during startup. */
        public RecipeMark markRecipeSequence(RecipeMap<?> map, Object input, Object output, RecipeMark... steps) {
            if (steps.length == 0) throw new IllegalArgumentException("empty recipe sequence");
            RecipeMark mark = new RecipeMark(map, input, output, -1, false);
            int start = sequenceSteps.size();
            Collections.addAll(sequenceSteps, steps);
            recipeSequences
                .add(new RecipeSequence(mark, resourceKey(input), resourceKey(output), start, sequenceSteps.size()));
            return mark;
        }

        /** Round chance outputs over the specified source batch, without changing the source pool recipe. */
        public RecipeMark markRecipeWithRoundedOutputChances(RecipeMap<?> map, Object input, Object output, int circuit,
            int batches) {
            if (batches <= 0) throw new IllegalArgumentException("invalid output chance batch");
            RecipeMark mark = markRecipe(map, input, output, circuit);
            roundedOutputChanceBatches.put(mark, batches);
            return mark;
        }

        /** Prepare a fixed source recipe without registering it in any GT recipe pool. */
        public RecipeMark markTemporaryRecipe(RecipeMap<?> map, Object input, Object output, int circuit,
            GTRecipe recipe) {
            if (recipe == null || recipe.mEUt <= 0) throw new IllegalArgumentException("invalid temporary recipe");
            internalResources.add(resourceKey(input));
            internalResources.add(resourceKey(output));
            RecipeMark mark = new RecipeMark(map, input, output, circuit, false);
            if (resolved.putIfAbsent(mark, BottledChaosRecipeSupport.prepareSourceRecipe(map, recipe)) != null)
                throw new IllegalArgumentException("duplicate temporary recipe mark " + mark);
            return mark;
        }

        /** Cache a dynamic source's deterministic long-quantity equivalent during startup. */
        public RecipeMark markPreparedRecipe(RecipeMap<?> map, Object input, Object output, RecipeData data) {
            return markPreparedRecipe(map, input, output, -1, data);
        }

        public RecipeMark markPreparedRecipe(RecipeMap<?> map, Object input, Object output, int circuit,
            RecipeData data) {
            if (data == null || data.inputs.isEmpty() || data.outputs.isEmpty() || data.times.isEmpty())
                throw new IllegalArgumentException("invalid prepared recipe");
            internalResources.add(resourceKey(input));
            internalResources.add(resourceKey(output));
            RecipeMark mark = new RecipeMark(map, input, output, circuit, false);
            if (resolved.putIfAbsent(mark, data) != null)
                throw new IllegalArgumentException("duplicate prepared recipe mark " + mark);
            return mark;
        }

        /** Register named common reagents and their stored upstream syntheses once, during startup. */
        public void registerInputSyntheses(Object... targets) {
            Set<Object> pending = new LinkedHashSet<>(Arrays.asList(targets));
            Set<Object> visited = new LinkedHashSet<>();
            while (!pending.isEmpty()) {
                Object target = pending.iterator()
                    .next();
                pending.remove(target);
                if (!visited.add(target)) continue;
                var recipe = BottledChaosReagentRecipes.getInputSynthesis(target);
                if (recipe == null) continue;
                if (registeredReagentSyntheses.add(target))
                    registerInputSynthesis(recipe.pool(), target, recipe.inputs(), recipe.circuit());
                if (recipe.inputs() instanceof Object[]inputs) Collections.addAll(pending, inputs);
                else pending.add(recipe.inputs());
            }
        }

        /** Register one stored synthesis step, retaining the caller's existing upstream input boundary. */
        public void registerInputSynthesis(Object target) {
            var recipe = BottledChaosReagentRecipes.getInputSynthesis(target);
            if (recipe == null) throw new IllegalArgumentException("unknown reagent synthesis: " + target);
            if (registeredReagentSyntheses.add(target))
                registerInputSynthesis(recipe.pool(), target, recipe.inputs(), recipe.circuit());
        }

        public void registerInputSynthesis(RecipeMap<?> map, Object target, Object inputs, int circuit) {
            // Furnace and fluid-container fallback recipes are generated on lookup, rather than listed in
            // getAllRecipes(). Keep native container returns and processing work in the composed route.
            RecipeMark mark = map == Furnace || map == RecipeMaps.cannerRecipes ? markTemporaryRecipe(
                map,
                inputs,
                target,
                circuit,
                map.findRecipeQuery()
                    .items(((TST_ItemID) inputs).getItemStack(1))
                    .find())
                : registerRecipeMark(map, inputs, target, circuit, true);
            expansions.add(new Expansion(mark, resourceKey(target), true));
        }

        /** Register the stored downstream decomposition path once, during startup. */
        public void registerOutputDecompositions(Object... targets) {
            Set<Object> pending = new LinkedHashSet<>(Arrays.asList(targets));
            while (!pending.isEmpty()) {
                Object target = pending.iterator()
                    .next();
                pending.remove(target);
                if (!registeredReagentDecompositions.add(target)) continue;
                var recipe = BottledChaosReagentRecipes.getOutputDecomposition(target);
                if (recipe == null) continue;
                registerOutputDecomposition(recipe.pool(), target, recipe.inputs(), recipe.outputs(), recipe.circuit());
                if (recipe.outputs() instanceof Object[]outputs) Collections.addAll(pending, outputs);
                else pending.add(recipe.outputs());
            }
        }

        public void registerOutputDecomposition(RecipeMap<?> map, Object target, Object outputs, int circuit) {
            registerOutputDecomposition(map, target, target, outputs, circuit);
        }

        /** Match specific input identities while balancing only the output branch's target resource. */
        public void registerOutputDecomposition(RecipeMap<?> map, Object target, Object inputs, Object outputs,
            int circuit) {
            expansions.add(
                new Expansion(registerRecipeMark(map, inputs, outputs, circuit, true), resourceKey(target), false));
        }

        /** Decompose one coproduct only in the latest entry of this tag; other families retain their boundaries. */
        public void registerChainOutputDecomposition(String tag, RecipeMap<?> map, Object target, Object outputs,
            int circuit) {
            expansions.add(
                new Expansion(
                    registerRecipeMark(map, target, outputs, circuit, true),
                    resourceKey(target),
                    false,
                    latestChainIndex(tag)));
        }

        /** Reuse one coproduct for another reagent before synthesizing or decomposing their net remainders. */
        public void registerReagentRecycling(RecipeMap<?> map, Object returned, Object needed, int circuit) {
            reagentRecycling.add(
                new ReagentRecycling(
                    registerRecipeMark(map, returned, needed, circuit, true),
                    resourceKey(returned),
                    resourceKey(needed)));
            deferredReagents.add(resourceKey(returned));
            deferredReagents.add(resourceKey(needed));
        }

        /** Keep reusable reagents intact until every main line, branch and returned input has been combined. */
        public void registerDeferredReagents(Object... resources) {
            for (Object resource : resources) deferredReagents.add(resourceKey(resource));
        }

        /** Explicit auxiliary feedstocks may also be entry markers of another registered route. */
        public void registerExternalInputs(Object... resources) {
            for (Object resource : resources) externalInputs.add(resourceKey(resource));
        }

        /** Set the final input boundary after registering all paths; stored upstream definitions remain reusable. */
        public void registerExternalReagents(Object... resources) {
            Set<Object> supplied = new LinkedHashSet<>();
            for (Object resource : resources) supplied.add(resourceKey(resource));
            externalInputs.addAll(supplied);
            expansions.removeIf(expansion -> expansion.synthesis() && supplied.contains(expansion.key()));
        }

        private RecipeMark registerRecipeMark(RecipeMap<?> map, Object input, Object output, int circuit,
            boolean optional) {
            if (input == null && output == null)
                throw new IllegalArgumentException("recipe mark must identify an input or output: " + map);
            RecipeMark mark = new RecipeMark(map, input, output, circuit, optional);
            marks.add(mark);
            pools.add(map);
            return mark;
        }

        private void validateRegisteredChains() {
            for (int index = 0; index < chains.size(); index++) {
                if (chains.get(index) == null)
                    throw new IllegalStateException("missing recipe chain for tag " + CredentialModel.tag(index));
            }
        }

        private void publishCapacityCosts() {
            int[] costs = new int[CredentialModel.ROUTE_COUNT];
            for (int index = 0; index < costs.length; index++) costs[index] = chains.get(index)
                .capacityCost();
            CredentialModel.publishCapacityCosts(costs);
        }

        // Scan each selected pool once and resolve the flat marker directory.
        private void scanMarkedRecipes() {
            for (RecipeMap<?> pool : pools) {
                for (GTRecipe recipe : pool.getAllRecipes()) {
                    if (!recipe.mEnabled || recipe.mFakeRecipe
                        || recipe.mEUt <= 0
                        || containsFractionalPowder(recipe.mInputs)
                        || containsFractionalPowder(recipe.mOutputs)) continue;
                    Set<Integer> circuits = new LinkedHashSet<>();
                    Set<Object> inputs = collectRecipeIdentities(recipe.mInputs, recipe.mFluidInputs, circuits);
                    Set<Object> outputs = collectRecipeIdentities(recipe.mOutputs, recipe.mFluidOutputs, null);
                    for (RecipeMark mark : marks) {
                        if (mark.map != pool || !matchesIdentities(inputs, mark.input)
                            || !matchesIdentities(outputs, mark.output)
                            || mark.circuit >= 0 && !circuits.contains(mark.circuit)) continue;
                        var prepared = BottledChaosRecipeSupport
                            .prepareSourceRecipe(pool, recipe, roundedOutputChanceBatches.getOrDefault(mark, 0));
                        InputSubstitution[] replacements = substitutedInputs.get(mark);
                        if (replacements != null) {
                            Object[] originals = mark.input() instanceof Object[]markedInputs ? markedInputs
                                : new Object[] { mark.input() };
                            for (int index = 0; index < replacements.length; index++) {
                                prepared = Objects.requireNonNull(
                                    prepared.withAlternativeInput(
                                        originals[index],
                                        replacements[index].item(),
                                        replacements[index].multiplier()),
                                    "missing source input for substitution: " + mark);
                            }
                        }
                        var previous = resolved.putIfAbsent(mark, prepared);
                        // GT may register both production and recycling copies of the same unified recipe.
                        if (previous != null
                            && (!previous.inputs.equals(prepared.inputs) || !previous.outputs.equals(prepared.outputs)
                                || !previous.catalysts.equals(prepared.catalysts)
                                || !previous.times.equals(prepared.times)))
                            throw new IllegalStateException("ambiguous recipe mark " + mark);
                        if (previous != null) resolved.put(mark, previous.withInputAlternatives(prepared));
                    }
                }
            }
            for (RecipeMark mark : marks) {
                if (resolved.containsKey(mark)) continue;
                if (!mark.optional) throw new IllegalStateException("missing recipe mark " + mark);
                TwistSpaceTechnology.LOG.warn("Bottled Chaos optional recipe is unavailable: {}", mark);
            }
            for (RecipeSequence sequence : recipeSequences) {
                RecipeData prepared = composeMarkedSteps(
                    sequenceSteps,
                    sequence.start(),
                    sequence.end(),
                    new Object[] { sequence.output() }).netInputsAndOutputs(sequence.input());
                if (resolved.putIfAbsent(sequence.mark(), prepared) != null)
                    throw new IllegalArgumentException("duplicate prepared recipe sequence");
            }
        }

        private Set<Object> collectRecipeIdentities(ItemStack[] items, FluidStack[] fluids, Set<Integer> circuits) {
            Set<Object> result = new LinkedHashSet<>();
            if (items != null) for (ItemStack stack : items) {
                if (stack == null || stack.getItem() == null) continue;
                if (circuits != null && GTUtility.isAnyIntegratedCircuit(stack)) circuits.add(stack.getItemDamage());
                else result.add(recipeItemId(stack));
            }
            if (fluids != null) for (FluidStack stack : fluids) {
                if (stack != null && stack.getFluid() != null) result.add(stack.getFluid());
            }
            return result;
        }

        private static boolean matchesIdentities(Set<Object> values, Object condition) {
            if (condition == null) return true;
            if (condition instanceof Object[]conditions) {
                for (Object value : conditions) if (!values.contains(value)) return false;
                return true;
            }
            return values.contains(condition);
        }

        // Store routes by stable tag index, then resolve key-material links in display/execution order.
        private RecipeData[] composeRecipeChains() {
            RecipeData[] routes = new RecipeData[chains.size()];
            for (int i = 0; i < routes.length; i++) {
                RecipeChain chain = chains.get(i);
                // 1. Reuse the complete primary route for aliases, or balance and expand marked main-line steps.
                RecipeData route;
                if (chain.inputMultiplier() > 0) {
                    route = Objects.requireNonNull(
                        routes[chain.tagIndex()]
                            .withAlternativeInput(chain.substitutedRoot(), chain.root(), chain.inputMultiplier()),
                        "unrepresentable input alternative");
                } else {
                    route = composeChainSteps(chain);
                    route = expandInputsAndOutputs(route, chain.terminals(), false, i);
                    // Attach fixed sources before canceling the net materials.
                    if (chain.substitutedRoot() != null)
                        route = route.appendBalancedRecipe(routes[substitutedChains.get(i)], chain.substitutedRoot());
                    // Deferred reagents may still hide the final root; validate it after their expansion below.
                    route = route.netInputsAndOutputs();
                }
                // 2. Check the terminal products of both marked routes and derived input variants.
                for (Object terminal : chain.terminals()) {
                    if (!route.outputs.containsKey(terminal)) throw new IllegalStateException(
                        "incomplete prepared route " + i
                            + " (tag="
                            + CredentialModel.tag(chain.tagIndex())
                            + ", missing output="
                            + (terminal instanceof TST_ItemID item ? item.getItemStack()
                                .getDisplayName()
                                : ((FluidId) terminal).getFluidStack()
                                    .getFluid()
                                    .getName())
                            + ")");
                }
                routes[i] = route;
            }
            // 3. Cancel complete routes before expanding net reagents, then reduce materials and source work together.
            for (int i = 0; i < routes.length; i++) {
                RecipeChain chain = chains.get(i);
                routes[i] = expandInputsAndOutputs(routes[i], chain.terminals(), true, i)
                    .netInputsAndOutputs(chain.root())
                    .reduceBatch();
                TST_ItemID selector = selectorCircuits.get(i);
                if (selector != null) routes[i] = routes[i].requireCatalyst(selector);
            }
            // 4. Any actual output may supply a marked key input; output markers only control NEI lookup.
            int[] executionOrder = entryOrder();
            for (int sourcePosition = 0; sourcePosition < executionOrder.length; sourcePosition++) {
                int source = executionOrder[sourcePosition];
                for (Object key : routes[source].outputs.keySet()) {
                    for (int targetPosition = 0; targetPosition < executionOrder.length; targetPosition++) {
                        int target = executionOrder[targetPosition];
                        if (source == target || !Arrays.asList(keyInputs(target))
                            .contains(key)) continue;
                        if (routes[target].inputs.containsKey(key) || routes[target].catalysts.containsKey(key))
                            links.add(new ChainLink(source, target, key));
                    }
                }
            }
            // 5. Reject unfinished local intermediates; a standalone route may use boundary materials directly.
            validateRouteConnections(routes);
            return routes;
        }

        // Chain each marked main line through its declared internal intermediates.
        private RecipeData composeChainSteps(RecipeChain chain) {
            return composeMarkedSteps(chainSteps, chain.start(), chain.end(), chain.terminals());
        }

        private RecipeData composeMarkedSteps(List<RecipeMark> steps, int start, int end, Object[] terminals) {
            Set<Object> connections = collectMarkedResources(steps, start, end);
            RecipeMark first = steps.get(start);
            var result = Objects.requireNonNull(resolved.get(first), first.toString());
            for (int index = start + 1; index < end; index++) {
                RecipeMark step = steps.get(index);
                var next = Objects.requireNonNull(resolved.get(step), step.toString());
                Object intermediate = null;
                for (Object key : next.inputs.keySet()) {
                    // A fraction retained by another branch may still be this step's declared connection.
                    if (!result.outputs.containsKey(key)
                        || !connections.contains(key) && !key.equals(resourceKey(step.input()))
                            && !Arrays.asList(terminals)
                                .contains(key))
                        continue;
                    if (intermediate != null)
                        throw new IllegalStateException("ambiguous internal connection for " + step);
                    intermediate = key;
                }
                if (intermediate == null) throw new IllegalStateException("missing internal connection for " + step);
                result = result.appendBalancedRecipe(next, intermediate);
            }
            return result;
        }

        /** Keep main-line connections local; another route may use a common reagent as its entry marker. */
        private Set<Object> collectMarkedResources(List<RecipeMark> steps, int start, int end) {
            Set<Object> resources = new LinkedHashSet<>();
            for (int index = start; index < end; index++) {
                RecipeMark step = steps.get(index);
                for (Object marker : new Object[] { step.input(), step.output() }) {
                    if (marker instanceof Object[]alternatives) {
                        for (Object resource : alternatives) resources.add(resourceKey(resource));
                    } else if (marker != null) resources.add(resourceKey(marker));
                }
            }
            return resources;
        }

        // Prepend input syntheses and append output decompositions until the route is complete.
        private RecipeData expandInputsAndOutputs(RecipeData route, Object[] terminals, boolean finalReagents,
            int entry) {
            var finalOutputs = Arrays.asList(terminals);
            Set<Object> overridden = new LinkedHashSet<>();
            for (Expansion expansion : expansions) if (expansion.entry() == entry) overridden.add(expansion.key());
            for (int pass = 0; pass < MAX_GRAPH_PASSES; pass++) {
                boolean changed = false;
                for (var expansion : expansions) {
                    Object key = expansion.key();
                    if (expansion.entry() >= 0 && expansion.entry() != entry
                        || expansion.entry() < 0 && expansion.synthesis() && overridden.contains(key)) continue;
                    if (!finalReagents && deferredReagents.contains(key)) continue;
                    route = route.cancelReturnedResource(key);
                    if (!expansion.synthesis() && finalOutputs.contains(key)) continue;
                    var side = expansion.synthesis() ? route.inputs : route.outputs;
                    if (!side.containsKey(key)) continue;
                    var process = resolved.get(expansion.recipe());
                    if (process == null) continue;
                    route = expansion.synthesis() ? process.appendBalancedRecipe(route, key)
                        : route.appendBalancedRecipe(process, key);
                    changed = true;
                }
                if (!changed) {
                    // First close coproduct conversions; only their remaining net resources use direct recipes.
                    for (var recycling : reagentRecycling) {
                        RecipeData conversion = resolved.get(recycling.recipe());
                        if (conversion != null)
                            route = route.recycleReagent(conversion, recycling.returned(), recycling.needed());
                    }
                    return route;
                }
            }
            throw new IllegalStateException("cyclic reagent synthesis/decomposition in recipe composition");
        }

        private int[] entryOrder() {
            int[] order = new int[chains.size()];
            int next = 0;
            // Runtime priorities retain the stable registration order, independently of display progression.
            for (int tag = 0; tag < CredentialModel.ROUTE_COUNT; tag++) {
                // Preparations follow registration order; insert the primary before its same-root variants.
                boolean primaryAdded = false;
                for (int entry = CredentialModel.ROUTE_COUNT; entry < chains.size(); entry++) {
                    RecipeChain chain = chains.get(entry);
                    if (chain.tagIndex() != tag) continue;
                    if (!primaryAdded && chains.get(tag)
                        .root()
                        .equals(chain.root())) {
                        order[next++] = tag;
                        primaryAdded = true;
                    }
                    order[next++] = entry;
                }
                if (!primaryAdded) order[next++] = tag;
            }
            return order;
        }

        private int[] displayOrder() {
            List<Integer> order = new ArrayList<>(chains.size());
            for (int entry = 0; entry < chains.size(); entry++) order.add(entry);
            int honeycomb = CredentialModel.indexForTag("honeycomb");
            long honeycombMask = honeycomb < 0 ? 0 : 1L << honeycomb;
            order.sort(
                Comparator.comparingInt(
                    (Integer entry) -> CredentialModel.displayIndex(
                        chains.get(entry)
                            .requiredTagMask()))
                    .thenComparingInt(
                        entry -> (chains.get(entry)
                            .requiredTagMask() & honeycombMask) != 0 ? 1 : 0));
            Map<String, Map<Object, List<Integer>>> families = new LinkedHashMap<>();
            for (int entry : order) {
                RecipeChain chain = chains.get(entry);
                String family = CredentialModel.processingFamily(chain.requiredTagMask());
                var roots = families.computeIfAbsent(family, key -> {
                    Map<Object, List<Integer>> stages = new LinkedHashMap<>();
                    for (Object root : displayRoots.getOrDefault(key, Collections.emptyList()))
                        stages.put(root, new ArrayList<>());
                    return stages;
                });
                Object root = chain.root();
                // Standalone comb sources follow their production tag's primary when no ordinary root matches.
                if ((chain.requiredTagMask() & honeycombMask) != 0 && !roots.containsKey(root))
                    root = chains.get(Long.numberOfTrailingZeros(chain.requiredTagMask() & ~honeycombMask))
                        .root();
                roots.computeIfAbsent(root, ignored -> new ArrayList<>())
                    .add(entry);
            }
            int[] result = new int[order.size()];
            int next = 0;
            for (var roots : families.values())
                for (var variants : roots.values()) for (int entry : variants) result[next++] = entry;
            return result;
        }

        // Material boundaries remain valid external inputs/outputs even when key markers forbid chaining them.
        private void validateRouteConnections(RecipeData[] routes) {
            Set<Object> boundaries = new LinkedHashSet<>(externalInputs);
            for (int entry = 0; entry < chains.size(); entry++) {
                RecipeChain chain = chains.get(entry);
                boundaries.add(chain.root());
                Collections.addAll(boundaries, chain.terminals());
                Collections.addAll(boundaries, keyInputs(entry));
                Collections.addAll(boundaries, keyOutputs(entry));
                if (chain.chainableOutputs() != null) Collections.addAll(boundaries, chain.chainableOutputs());
            }
            for (int i = 0; i < routes.length; i++) {
                RecipeData route = routes[i];
                RecipeChain chain = chains.get(i);
                RecipeChain markedChain = chain.inputMultiplier() > 0 ? chains.get(chain.tagIndex()) : chain;
                Set<Object> connections = collectMarkedResources(chainSteps, markedChain.start(), markedChain.end());
                connections.retainAll(internalResources);
                connections.removeAll(boundaries);
                for (Object key : connections) {
                    if (route.inputs.containsKey(key) || route.catalysts.containsKey(key))
                        throw new IllegalStateException("unresolved intermediate input in route " + i + ": " + key);
                    if (route.outputs.containsKey(key))
                        throw new IllegalStateException("unfinished or backward output in route " + i + ": " + key);
                }
            }
        }

    }

    private static Object resourceKey(Object resource) {
        return resource instanceof Fluid fluid ? FluidId.create(fluid) : resource;
    }

    private static boolean containsFractionalPowder(ItemStack[] stacks) {
        if (stacks == null) return false;
        for (ItemStack stack : stacks) {
            if (RecipeMaterialUtils.isFractionalDust(stack)) return true;
        }
        return false;
    }

    /**
     * Startup route declaration: a flat step range, terminal/connection rules, credential cost, base power and
     * all credential tags required to select this entry.
     * Positive inputMultiplier derives an input-only variant from its primary tag, without extra work.
     */
    @Desugar
    private record RecipeChain(Object root, Object[] terminals, int start, int end, int capacityCost, long baseEUt,
        int tagIndex, Object substitutedRoot, Object[] chainableOutputs, int inputMultiplier, long requiredTagMask) {}

    /**
     * Source-recipe lookup identity used by marker sets and the resolved-data cache; optional permits missing
     * expansions.
     */
    @Desugar
    public record RecipeMark(RecipeMap<?> map, Object input, Object output, int circuit, boolean optional) {}

    @Desugar
    private record InputSubstitution(TST_ItemID item, int multiplier) {}

    /** A startup-only contiguous source range, folded into one reusable cached processing step. */
    @Desugar
    private record RecipeSequence(RecipeMark mark, Object input, Object output, int start, int end) {}

    /** Startup reagent instruction: apply the marked recipe to synthesize an input or decompose an output resource. */
    @Desugar
    private record Expansion(RecipeMark recipe, Object key, boolean synthesis, int entry) {

        Expansion(RecipeMark recipe, Object key, boolean synthesis) {
            this(recipe, key, synthesis, -1);
        }
    }

    /** Startup-only coproduct conversion, with direct handling deferred for both reagent identities. */
    @Desugar
    private record ReagentRecycling(RecipeMark recipe, Object returned, Object needed) {}

    /** Authorized resource flow between actual entry indexes; indexes may differ from credential tag indexes. */
    @Desugar
    private record ChainLink(int source, int target, Object key) {}

    /**
     * Complete net recipe with its runtime root, NEI endpoint identities, voltage, owning tag and authorization mask;
     * positive inputMultiplier folds its NEI row into the primary entry.
     */
    @Desugar
    private record CachedRoute(RecipeData data, Object root, Object[] lookupInputs, Object[] lookupOutputs,
        Object[] targetOutputs, long baseEUt, int tagIndex, int inputMultiplier, long requiredTagMask,
        boolean availableInputsOnly, ItemStack[][] displayInputAlternatives, boolean hiddenDisplay, long poolMask) {

        boolean isAuthorized(long routeMask) {
            return (routeMask & requiredTagMask) == requiredTagMask;
        }
    }

    // Families prioritize entry lookup and label saved materials; they do not constrain the execution graph.
    private static final class RouteCache {

        final CachedRoute[] routes;
        final int[] entryOrder;
        final int[] displayOrder;
        final ChainLink[] links;
        final int[][] familyEntries;
        final String[] familyNames;
        final int[] familyOfEntry;
        final BottledChaosChainPlanner.Stage[] stages;

        RouteCache(CachedRoute[] routes, int[] entryOrder, int[] displayOrder, ChainLink[] links) {
            this.routes = routes;
            this.entryOrder = entryOrder;
            this.displayOrder = displayOrder;
            this.links = links;
            stages = new BottledChaosChainPlanner.Stage[routes.length];
            for (int entry = 0; entry < routes.length; entry++) {
                RecipeData data = routes[entry].data;
                stages[entry] = new BottledChaosChainPlanner.Stage(
                    data.inputs,
                    data.outputs,
                    data.catalysts,
                    routes[entry].baseEUt,
                    routes[entry].availableInputsOnly);
            }
            Map<String, Integer> families = new LinkedHashMap<>();
            familyOfEntry = new int[routes.length];
            int[] counts = new int[routes.length];
            for (int entry : entryOrder) {
                String name = CredentialModel.processingFamily(routes[entry].requiredTagMask);
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
                if (link.source == source && link.key.equals(key) && routes[link.target].isAuthorized(mask))
                    return true;
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

    private static double calculateTimePart(TimeKey key, long rawTicks, MachineParameters parameters,
        long recipeVoltage) {
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

    private static boolean canProcessSource(TimeKey key, MachineParameters parameters, long recipeVoltage) {
        var requirements = key.requirements();
        return parameters.canProcess(
            key.map(),
            recipeVoltage,
            requirements.heat(),
            requirements.glassTier(),
            requirements.casingTier(),
            requirements.compressionTier(),
            requirements.heavyCentrifuge(),
            requirements.plasmaTier(),
            requirements.multiStepPlasma(),
            requirements.requiredSourceVoltage());
    }

    private static void configureSource(TimeKey key, MachineParameters parameters, long recipeVoltage) {
        var requirements = key.requirements();
        parameters.configureSource(
            key.map(),
            recipeVoltage,
            requirements.heat(),
            requirements.glassTier(),
            requirements.specialInput() == null ? null
                : requirements.specialInput()
                    .getItemStack(1)
                    .copy(),
            requirements.heavyCentrifuge(),
            requirements.requiredSourceVoltage(),
            requirements.fusionThreshold());
    }

    /** Source-machine hooks use the final serial power without rebuilding material routes. */
    public interface MachineParameters {

        default void resetSourceUsage() {}

        default void recordSourceUsage(RecipeMap<?> map) {}

        default List<BottledChaosUpkeep.Demand> upkeepDemands() {
            return Collections.emptyList();
        }

        default void configureSource(RecipeMap<?> map, long recipeVoltage, int requiredHeat, int requiredGlassTier,
            ItemStack requiredSpecialInput, boolean heavyCentrifuge, long requiredSourceVoltage,
            long fusionThreshold) {}

        default long maximumSourcePower(RecipeMap<?> map) {
            return Long.MAX_VALUE;
        }

        default boolean canProcess(RecipeMap<?> map, long recipeVoltage, int requiredHeat, int requiredGlassTier,
            int requiredCasingTier, int requiredCompressionTier, boolean heavyCentrifuge, int requiredPlasmaTier,
            boolean multiStepPlasma, long requiredSourceVoltage) {
            return true;
        }

        default long parallel(RecipeMap<?> map) {
            return 1;
        }

        default double speedMultiplier(RecipeMap<?> map) {
            return 1;
        }

        default double euMultiplier(RecipeMap<?> map) {
            return 1;
        }

        default double euMultiplier(RecipeMap<?> map, int requiredHeat) {
            return euMultiplier(map);
        }

        default double quantitySpeedMultiplier(RecipeMap<?> map) {
            return 1;
        }

        default double minimumSourceDuration(RecipeMap<?> map) {
            return 0;
        }

        default double timeDivisor(RecipeMap<?> map, int requiredHeat) {
            return overclock(map).timeReduction;
        }

        default double powerMultiplier(RecipeMap<?> map, int requiredHeat) {
            return overclock(map).powerIncrease;
        }

        default int maximumOverclocks(RecipeMap<?> map) {
            return -1;
        }

        default int maximumOverclocks(RecipeMap<?> map, int requiredCasingTier) {
            return maximumOverclocks(map);
        }

        /** Optional equiprobable native limits; ordinary machines use the single limit without allocation. */
        default int[] overclockLimitAlternatives(RecipeMap<?> map) {
            return null;
        }

        default int perfectOverclocks(RecipeMap<?> map, int requiredHeat, int requiredCasingTier) {
            return 0;
        }

        default OverclockType overclock(RecipeMap<?> map) {
            return defaultOverclockType(map);
        }
    }

    private static OverclockType defaultOverclockType(RecipeMap<?> map) {
        if (BottledChaosMachineConfiguration.isPurificationPool(map)) return OverclockType.PerfectOverclock;
        return OverclockType.NormalOverclock;
    }

    /** One candidate: exact input consumption and carry, with outputs finalized only when checking capacity. */
    public static final class PreparedPlan {

        private final String family;
        private final int entry;
        private final long baseEUt;
        private final double duration;
        private List<BottledChaosUpkeep.Demand> upkeep;
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
            this.consumed = consumed;
            this.outputs = outputs;
            this.remainingCache = remainingCache;
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

        public Plan outputPlan() {
            if (outputPlan == null) {
                BottledChaosChainPlanner.releaseExcess(cacheLimit, remainingCache, pendingCache, outputs);
                outputPlan = createOutputPlan(outputs, duration, executions);
            }
            return outputPlan;
        }

        public Plan pendingCache() {
            return materialPlan(pendingCache);
        }

        public void commitCache(BottledChaosMaterialCache materials) {
            materials.replace(family, materialPlan(remainingCache));
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

    /** Reuse the long output representation and NBT codec for material cache snapshots. */
    public static Plan materialPlan(Map<Object, Long> amounts) {
        return createOutputPlan(amounts, 0, 0);
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

    private static void appendResourceStack(Object key, long amount, List<ItemStackLong> items,
        List<FluidStackLong> fluids) {
        // Copy templates so neither runtime outputs nor NEI can mutate cached identities through NBT.
        if (key instanceof TST_ItemID item) items.add(
            new ItemStackLong(
                item.getItemStack()
                    .copy(),
                amount));
        else fluids.add(
            new FluidStackLong(
                ((FluidId) key).getFluidStack(1)
                    .copy(),
                amount));
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
     * NEI row data: authorization, independent item alternatives, searchable entry/endpoints, complete long quantities
     * and grouped processing times. Lookup identities do not remove any displayed material.
     */
    @Desugar
    public record DisplayRecipe(int routeIndex, int displayOrder, long requiredTagMask, ItemStack[][] inputAlternatives,
        Object[] lookupInputs, Object[] lookupOutputs, long baseEUt, List<ItemStackLong> itemInputs,
        List<FluidStackLong> fluidInputs, List<RecipeMapDuration> processingTimes, Plan plan) {}

}
