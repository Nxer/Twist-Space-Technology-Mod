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
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosUpkeep;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosItemInputs.Requirement;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;
import com.Nxer.TwistSpaceTechnology.util.recipes.RecipeMaterialUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
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
                    catalog.queryInputs.getOrDefault(i, new Object[] { chain.root() }),
                    catalog.queryOutputs.getOrDefault(i, chain.terminals()),
                    targets,
                    chain.baseEUt(),
                    chain.tagIndex(),
                    chain.inputMultiplier(),
                    chain.requiredTagMask(),
                    BottledChaosMachineConfiguration.withDependencies(
                        preparedRoutes[i].times.keySet()
                            .stream()
                            .mapToLong(time -> BottledChaosMachineConfiguration.mask(time.map()))
                            .reduce(0L, (left, right) -> left | right)));
            }
            // 5. Publish complete routes and immutable input checkers; no scanned source recipe survives startup.
            cache = new RouteCache(routes, catalog.entryOrder(), catalog.links.toArray(new ChainLink[0]));
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

    /** Integer final-product targets determine upstream supply; graph cycles require already available seed. */
    public static PreparedPlan prepareExecution(long routeMask, int entry, int externalBatches, ItemStack[] items,
        FluidStack[] fluids, MachineParameters parameters, BottledChaosMaterialCache materials) {
        return executionPlanner(routeMask, entry, items, fluids, parameters, materials).prepare(externalBatches);
    }

    public static ExecutionPlanner executionPlanner(long routeMask, int entry, ItemStack[] items, FluidStack[] fluids,
        MachineParameters parameters, BottledChaosMaterialCache materials) {
        return new ExecutionPlanner(cache, routeMask, entry, items, fluids, parameters, materials);
    }

    /** One input/structure snapshot, sharing source checks and normalized costs across bounded batch probes. */
    public static final class ExecutionPlanner {

        private final RouteCache snapshot;
        private final long routeMask;
        private final int entry;
        private final ItemStack[] items;
        private final FluidStack[] fluids;
        private final MachineParameters parameters;
        private final BottledChaosMaterialCache materials;
        private final Map<Long, SourceChecks> sourceChecks = new LinkedHashMap<>();
        private final Set<TST_ItemID> virtualItems = new LinkedHashSet<>();
        private Map<Object, Long> external;
        private Map<Object, Long> remaining;
        private Map<Object, Long> released;
        private int[] goalOrder;
        private long[] targets;

        private ExecutionPlanner(RouteCache snapshot, long routeMask, int entry, ItemStack[] items, FluidStack[] fluids,
            MachineParameters parameters, BottledChaosMaterialCache materials) {
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
            SourceChecks checks = sourceChecks(voltage);
            if (!checks.checked[index]) {
                checks.checked[index] = true;
                RecipeData data = snapshot.routes[index].data;
                checks.allowed[index] = canProcessSources(data, parameters, voltage);
                if (checks.allowed[index]) {
                    for (var time : data.times.entrySet()) {
                        checks.costs[index] = BottledChaosRecipeSupport.addDuration(
                            checks.costs[index],
                            calculateTimePart(time.getKey(), time.getValue(), parameters, voltage),
                            1);
                    }
                }
            }
            return checks.allowed[index];
        }

        @Desugar
        private record SourceChecks(double[] costs, boolean[] checked, boolean[] allowed) {}

        /** Prefer complete terminal products; only an unavailable full line falls back to paid intermediate carry. */
        public PreparedPlan prepare(int targetBatches) {
            if (snapshot == null || !snapshot.routes[entry].isAuthorized(routeMask)) return null;
            if (external == null) {
                int family = snapshot.familyOfEntry[entry];
                external = snapshot.collectAvailableInputs(family, items, fluids, virtualItems);
                remaining = materials.snapshot(snapshot.familyNames[family]);
                released = new LinkedHashMap<>();
                for (var iterator = remaining.entrySet()
                    .iterator(); iterator.hasNext();) {
                    var carry = iterator.next();
                    if (snapshot.hasAuthorizedCacheConsumer(family, routeMask, carry.getKey())) continue;
                    released.put(carry.getKey(), carry.getValue());
                    iterator.remove();
                }
                boolean[] reachable = new boolean[snapshot.routes.length];
                reachable[entry] = true;
                boolean changed;
                do {
                    changed = false;
                    for (ChainLink link : snapshot.links) {
                        if (reachable[link.source] && !reachable[link.target]
                            && snapshot.routes[link.target].isAuthorized(routeMask)) {
                            reachable[link.target] = true;
                            changed = true;
                        }
                    }
                } while (changed);
                int[] entries = snapshot.familyEntries[family];
                goalOrder = new int[entries.length];
                int size = 0;
                long[] complete = new long[snapshot.routes.length];
                // Deeper branches get first claim on shared material. Returned byproducts do not hide a terminal.
                for (int i = entries.length - 1; i >= 0; i--) {
                    int index = entries[i];
                    if (!reachable[index]) continue;
                    goalOrder[size++] = index;
                    for (Object terminal : snapshot.routes[index].targetOutputs) {
                        if (!snapshot.hasAuthorizedConsumer(index, routeMask, terminal)) {
                            complete[index] = 1;
                            break;
                        }
                    }
                }
                goalOrder = Arrays.copyOf(goalOrder, size);
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

        /** Increase final-product targets, then rebuild exact upstream demand within the time and source limits. */
        public PreparedPlan fitDownstream(PreparedPlan full, Predicate<PreparedPlan> fits) {
            if (!fits.test(full)) return full; // One complete integer chain is indivisible.
            PreparedPlan chosen = full;
            long[] requested = full.targets.clone();
            for (int index : goalOrder) {
                if (requested[index] == 0) continue;
                long lower = requested[index];
                long upper = lower;
                while (upper < Integer.MAX_VALUE) {
                    upper = Math.min(Integer.MAX_VALUE, upper * 2);
                    requested[index] = upper;
                    PreparedPlan candidate = prepareTargets(requested, false);
                    if (candidate == null || !fits.test(candidate)) break;
                    chosen = candidate;
                    lower = upper;
                }
                if (lower < upper) {
                    long high = upper - 1;
                    long low = lower + 1;
                    while (low <= high) {
                        long middle = low + (high - low) / 2;
                        requested[index] = middle;
                        PreparedPlan candidate = prepareTargets(requested, false);
                        if (candidate != null && fits.test(candidate)) {
                            chosen = candidate;
                            low = middle + 1;
                        } else high = middle - 1;
                    }
                }
                requested = chosen.targets.clone();
            }
            return chosen;
        }

        private PreparedPlan prepareTargets(long[] requested, boolean skipUnavailable) {
            parameters.resetSourceUsage();
            long scale = Arrays.stream(requested)
                .max()
                .orElse(1);
            long batchLimit = Math.min(
                Integer.MAX_VALUE,
                Math.max(1, Math.min(10000, Config.BottledChaosMaxChainedRecipeBatches)) * Math.max(1, scale));
            BottledChaosChainPlanner planner = new BottledChaosChainPlanner(
                snapshot.stages,
                snapshot.suppliers,
                new BottledChaosChainPlanner.Conditions() {

                    @Override
                    public boolean canProcess(int index, long voltage) {
                        return snapshot.routes[index].isAuthorized(routeMask) && checkSources(index, voltage);
                    }

                    @Override
                    public long retainedOutput(int index, Object key, long amount) {
                        return snapshot.hasAuthorizedConsumer(index, routeMask, key) ? amount : 0;
                    }
                },
                virtualItems,
                batchLimit);
            BottledChaosChainPlanner.Result result = planner
                .prepare(goalOrder, requested, external, remaining, released, skipUnavailable);
            if (result == null) return null;
            BottledChaosItemInputs.Consumption itemConsumption;
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
                itemConsumption = BottledChaosItemInputs
                    .allocate(requirements, result.consumed, internalPresence, items);
            } catch (ArithmeticException overflow) {
                return null;
            }
            if (itemConsumption == null) return null;
            double duration = 0;
            long requiredPools = 0;
            int family = snapshot.familyOfEntry[entry];
            for (int index : snapshot.familyEntries[family]) {
                if (result.batches[index] == 0) continue;
                if (!checkSources(index, result.voltage)) return null;
                duration = BottledChaosRecipeSupport
                    .addDuration(duration, sourceChecks(result.voltage).costs[index], result.batches[index]);
                requiredPools |= snapshot.routes[index].poolMask;
                for (var time : snapshot.routes[index].data.times.entrySet()) {
                    configureSource(time.getKey(), parameters, result.voltage);
                    parameters.recordSourceUsage(
                        time.getKey()
                            .map());
                }
            }
            return new PreparedPlan(
                snapshot.familyNames[family],
                entry,
                result.voltage,
                duration,
                parameters.upkeepDemands(),
                requiredPools,
                result.consumed,
                result.outputs,
                result.remaining,
                result.produced,
                (int) Math.max(1, result.batches[entry]),
                result.targets,
                itemConsumption);
        }
    }

    /** NEI reads the same net materials and timing groups directly, without replaying the runtime planner. */
    public static List<DisplayRecipe> createDisplayRecipes() {
        RouteCache snapshot = cache;
        if (snapshot == null) return Collections.emptyList();
        List<DisplayRecipe> result = new ArrayList<>(snapshot.routes.length);
        // 1. Read net materials in entry order; folded input variants share their primary display entry.
        for (int routeIndex : snapshot.entryOrder) {
            CachedRoute cachedRoute = snapshot.routes[routeIndex];
            if (cachedRoute.inputMultiplier > 0) continue;
            ItemStack[] inputAlternatives = null;
            if (routeIndex == cachedRoute.tagIndex) for (CachedRoute alternative : snapshot.routes) {
                if (alternative.tagIndex != cachedRoute.tagIndex || alternative.inputMultiplier == 0) continue;
                inputAlternatives = new ItemStack[] { ((TST_ItemID) cachedRoute.root).getItemStack(1)
                    .copy(),
                    ((TST_ItemID) alternative.root).getItemStack(alternative.inputMultiplier)
                        .copy() };
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
        private final Map<RecipeMark, TST_ItemID[]> substitutedInputs = new LinkedHashMap<>();
        private final Map<RecipeMark, Integer> roundedOutputChanceBatches = new LinkedHashMap<>();
        private final Map<Integer, Integer> substitutedChains = new LinkedHashMap<>();
        private final Map<Integer, TST_ItemID> selectorCircuits = new LinkedHashMap<>();
        private final Map<Integer, Object[]> queryInputs = new LinkedHashMap<>();
        private final Map<Integer, Object[]> queryOutputs = new LinkedHashMap<>();
        private final Map<Integer, Object[]> executionTargets = new LinkedHashMap<>();
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

        /** Explicit outputs may feed later authorized routes; null retains the legacy later-root behavior. */
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

        /** Override currently registered entries of this tag; null keeps defaults, an empty array disables a side. */
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

        /** Select main products for demand planning without changing protected coproducts or NEI markers. */
        public void setExecutionTargets(String tag, Object... outputs) {
            if (outputs.length == 0) throw new IllegalArgumentException("empty execution targets: " + tag);
            executionTargets.put(
                latestChainIndex(tag),
                Arrays.stream(outputs)
                    .map(BottledChaosRecipeProcessor::resourceKey)
                    .toArray());
        }

        /** Require a non-consumed programming circuit for the latest registered entry of this tag. */
        public void requireCircuit(String tag, int configuration) {
            int tagIndex = CredentialModel.indexForTag(tag);
            if (tagIndex < 0 || configuration < 1 || configuration > 24)
                throw new IllegalArgumentException("invalid route circuit: " + tag);
            selectorCircuits
                .put(latestChainIndex(tag), TST_ItemID.create(GTUtility.getIntegratedCircuit(configuration)));
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
            substitutedInputs.put(mark, replacements.clone());
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
            if (data == null || data.inputs.isEmpty() || data.outputs.isEmpty() || data.times.isEmpty())
                throw new IllegalArgumentException("invalid prepared recipe");
            internalResources.add(resourceKey(input));
            internalResources.add(resourceKey(output));
            RecipeMark mark = new RecipeMark(map, input, output, -1, false);
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
                        TST_ItemID[] replacements = substitutedInputs.get(mark);
                        if (replacements != null) {
                            Object[] originals = mark.input() instanceof Object[]markedInputs ? markedInputs
                                : new Object[] { mark.input() };
                            for (int index = 0; index < replacements.length; index++) {
                                prepared = Objects.requireNonNull(
                                    prepared.withAlternativeInput(originals[index], replacements[index], 1),
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

        // Store routes by stable tag index, then resolve forward links in display/execution order.
        private RecipeData[] composeRecipeChains() {
            Set<Object> roots = new LinkedHashSet<>();
            for (RecipeChain chain : chains) roots.add(chain.root());
            Set<Object> declaredOutputs = new LinkedHashSet<>();
            for (RecipeChain chain : chains)
                if (chain.chainableOutputs() != null) Collections.addAll(declaredOutputs, chain.chainableOutputs());
            for (RecipeChain chain : chains) for (Object terminal : chain.terminals()) {
                // Intermediate terminals still feed later routes; only final products leave the internal set.
                if (!roots.contains(terminal) && !declaredOutputs.contains(terminal))
                    internalResources.remove(terminal);
            }
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
            // 4. Connect declared resources within a family, including reverse links for bounded runtime recycling.
            int[] executionOrder = entryOrder();
            for (int sourcePosition = 0; sourcePosition < executionOrder.length; sourcePosition++) {
                int source = executionOrder[sourcePosition];
                Object[] declared = chains.get(source)
                    .chainableOutputs();
                if (declared == null) {
                    for (int targetPosition = 0; targetPosition < executionOrder.length; targetPosition++) {
                        int target = executionOrder[targetPosition];
                        if (source == target || !canConnectChains(source, target)) continue;
                        Object key = chains.get(target)
                            .root();
                        if (routes[source].outputs.containsKey(key)) links.add(new ChainLink(source, target, key));
                    }
                    continue;
                }
                Set<Object> unique = new LinkedHashSet<>(Arrays.asList(declared));
                for (Object key : unique) {
                    if (!routes[source].outputs.containsKey(key))
                        throw new IllegalStateException("chainable output is not a final route output: " + key);
                    for (int targetPosition = 0; targetPosition < executionOrder.length; targetPosition++) {
                        int target = executionOrder[targetPosition];
                        if (source == target || !canConnectChains(source, target)) continue;
                        if (routes[target].inputs.containsKey(key) || routes[target].catalysts.containsKey(key))
                            links.add(new ChainLink(source, target, key));
                    }
                }
            }
            // 5. Remove explicit final products from the internal set, then reject unresolved connections.
            for (Object key : declaredOutputs) {
                boolean hasConsumer = false;
                for (ChainLink link : links) if (link.key()
                    .equals(key)) hasConsumer = true;
                if (!roots.contains(key) && !hasConsumer) internalResources.remove(key);
            }
            validateRouteConnections(routes);
            return routes;
        }

        /** Recycled intermediates may enter another route of the same family; execution folds their voltage. */
        private boolean canConnectChains(int source, int target) {
            RecipeChain from = chains.get(source);
            RecipeChain to = chains.get(target);
            return Objects.equals(
                CredentialModel.processingFamily(from.requiredTagMask()),
                CredentialModel.processingFamily(to.requiredTagMask()));
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
            for (int tag : CredentialModel.displayOrder()) {
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

        // Internal inputs need the route root or an incoming link; outputs need a link or explicit final status.
        private void validateRouteConnections(RecipeData[] routes) {
            for (int i = 0; i < routes.length; i++) {
                RecipeData route = routes[i];
                RecipeChain chain = chains.get(i);
                RecipeChain markedChain = chain.inputMultiplier() > 0 ? chains.get(chain.tagIndex()) : chain;
                Set<Object> connections = collectMarkedResources(chainSteps, markedChain.start(), markedChain.end());
                connections.retainAll(internalResources);
                for (Object key : connections) {
                    boolean incoming = false;
                    boolean outgoing = false;
                    for (ChainLink link : links) {
                        if (!link.key()
                            .equals(key)) continue;
                        incoming |= link.target() == i;
                        outgoing |= link.source() == i;
                        if (incoming && outgoing) break;
                    }
                    if (!key.equals(chain.root()) && !externalInputs.contains(key)
                        && (route.inputs.containsKey(key) || route.catalysts.containsKey(key))
                        && !incoming)
                        throw new IllegalStateException("unresolved intermediate input in route " + i + ": " + key);
                    if (!route.outputs.containsKey(key)) continue;
                    boolean explicitOutput = chain.chainableOutputs() != null
                        && (Arrays.asList(chain.chainableOutputs())
                            .contains(key)
                            || Arrays.asList(chain.terminals())
                                .contains(key));
                    if (!outgoing && !explicitOutput)
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
        Object[] targetOutputs, long baseEUt, int tagIndex, int inputMultiplier, long requiredTagMask, long poolMask) {

        boolean isAuthorized(long routeMask) {
            return (routeMask & requiredTagMask) == requiredTagMask;
        }
    }

    // Runtime directory: rows are processing families, columns retain their registered entry order.
    private static final class RouteCache {

        final CachedRoute[] routes;
        final int[] entryOrder;
        final ChainLink[] links;
        final int[][] familyEntries;
        final String[] familyNames;
        final int[] familyOfEntry;
        final BottledChaosChainPlanner.Stage[] stages;
        final List<Map<Object, int[]>> suppliers = new ArrayList<>();

        RouteCache(CachedRoute[] routes, int[] entryOrder, ChainLink[] links) {
            this.routes = routes;
            this.entryOrder = entryOrder;
            this.links = links;
            stages = new BottledChaosChainPlanner.Stage[routes.length];
            for (int entry = 0; entry < routes.length; entry++) {
                RecipeData data = routes[entry].data;
                stages[entry] = new BottledChaosChainPlanner.Stage(
                    data.inputs,
                    data.outputs,
                    data.catalysts,
                    routes[entry].baseEUt);
                Map<Object, List<Integer>> incoming = new LinkedHashMap<>();
                for (ChainLink link : links) {
                    if (link.target == entry) incoming.computeIfAbsent(link.key, ignored -> new ArrayList<>())
                        .add(link.source);
                }
                Map<Object, int[]> sources = new LinkedHashMap<>();
                for (var input : incoming.entrySet()) sources.put(
                    input.getKey(),
                    input.getValue()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .toArray());
                suppliers.add(sources);
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

        private Map<Object, Long> collectAvailableInputs(int family, ItemStack[] items, FluidStack[] fluids,
            Set<TST_ItemID> virtualItems) {
            Map<Object, Long> available = new LinkedHashMap<>();
            if (items != null) for (ItemStack stack : items) {
                if (stack == null || stack.stackSize < 0) continue;
                ItemData association = GTOreDictUnificator.getAssociation(stack);
                Set<TST_ItemID> matching = new LinkedHashSet<>();
                for (int entry : familyEntries[family]) {
                    for (Requirement input : routes[entry].data.itemRequirements)
                        if (input.matches(association, stack)) matching.add(input.key);
                }
                // Candidate amounts may overlap; the final native allocation reserves each actual slot only once.
                for (TST_ItemID key : matching) {
                    available.merge(key, (long) stack.stackSize, Math::addExact);
                    if (stack.stackSize == 0) virtualItems.add(key);
                }
            }
            if (fluids != null) for (FluidStack stack : fluids) {
                if (stack == null || stack.amount <= 0) continue;
                FluidId key = FluidId.create(stack);
                if (isRequiredInput(family, key)) available.merge(key, (long) stack.amount, Math::addExact);
            }
            return available;
        }

        private boolean isRequiredInput(int family, Object key) {
            for (int entry : familyEntries[family]) {
                RecipeData data = routes[entry].data;
                if (data.inputs.containsKey(key) || data.catalysts.containsKey(key)) return true;
            }
            return false;
        }

        private boolean hasAuthorizedConsumer(int source, long mask, Object key) {
            for (ChainLink link : links) {
                if (link.source == source && link.key.equals(key) && routes[link.target].isAuthorized(mask))
                    return true;
            }
            return false;
        }

        private boolean hasAuthorizedCacheConsumer(int family, long mask, Object key) {
            for (ChainLink link : links) {
                if (familyOfEntry[link.target] == family && link.key.equals(key)
                    && routes[link.target].isAuthorized(mask)) return true;
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

    private static double calculateTimePart(TimeKey key, long rawTicks, MachineParameters parameters, long baseEUt) {
        configureSource(key, parameters, baseEUt);
        double sourceSpeed = parameters.speedMultiplier(key.map());
        double sourceDuration = Math.max(parameters.minimumSourceDuration(key.map()), key.recipeTicks() / sourceSpeed);
        return BottledChaosRecipeSupport.calculateNormalizedDuration(
            rawTicks,
            key.eut(),
            baseEUt,
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
            parameters.maximumOverclocks(key.map()),
            parameters.heatOverclocks(
                key.map(),
                key.requirements()
                    .heat()),
            parameters.overclockLimitAlternatives(key.map()));
    }

    private static boolean canProcessSources(RecipeData data, MachineParameters parameters, long mergedRecipeVoltage) {
        for (TimeKey key : data.times.keySet()) {
            configureSource(key, parameters, mergedRecipeVoltage);
            var requirements = key.requirements();
            if (!parameters.canProcess(
                key.map(),
                mergedRecipeVoltage,
                requirements.heat(),
                requirements.glassTier(),
                requirements.casingTier(),
                requirements.compressionTier(),
                requirements.heavyCentrifuge(),
                requirements.plasmaTier(),
                requirements.multiStepPlasma())) return false;
        }
        return true;
    }

    private static void configureSource(TimeKey key, MachineParameters parameters, long mergedRecipeVoltage) {
        var requirements = key.requirements();
        parameters.configureSource(
            key.map(),
            mergedRecipeVoltage,
            requirements.heat(),
            requirements.glassTier(),
            requirements.specialInput() == null ? null
                : requirements.specialInput()
                    .getItemStack(1)
                    .copy(),
            requirements.heavyCentrifuge());
    }

    /** Source-machine hooks are evaluated at runtime without rebuilding material routes. */
    public interface MachineParameters {

        default void resetSourceUsage() {}

        default void recordSourceUsage(RecipeMap<?> map) {}

        default List<BottledChaosUpkeep.Demand> upkeepDemands() {
            return Collections.emptyList();
        }

        default void configureSource(RecipeMap<?> map, long mergedRecipeVoltage, int requiredHeat,
            int requiredGlassTier, ItemStack requiredSpecialInput, boolean heavyCentrifuge) {}

        default boolean canProcess(RecipeMap<?> map, long mergedRecipeVoltage, int requiredHeat, int requiredGlassTier,
            int requiredCasingTier, int requiredCompressionTier, boolean heavyCentrifuge, int requiredPlasmaTier,
            boolean multiStepPlasma) {
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

        /** Optional equiprobable native limits; ordinary machines use the single limit without allocation. */
        default int[] overclockLimitAlternatives(RecipeMap<?> map) {
            return null;
        }

        default int heatOverclocks(RecipeMap<?> map, int requiredHeat) {
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

    /** One immutable candidate: exact external consumption, final outputs and available/pending carry deltas. */
    public static final class PreparedPlan {

        private final String family;
        private final int entry;
        private final long baseEUt;
        private final double duration;
        private final List<BottledChaosUpkeep.Demand> upkeep;
        private final long requiredPools;
        private final Map<Object, Long> consumed;
        private final Map<Object, Long> outputs;
        private final Map<Object, Long> remainingCache;
        private final Map<Object, Long> pendingCache;
        private final int executions;
        private final long[] targets;
        private final BottledChaosItemInputs.Consumption itemConsumption;

        private PreparedPlan(String family, int entry, long baseEUt, double duration,
            List<BottledChaosUpkeep.Demand> upkeep, long requiredPools, Map<Object, Long> consumed,
            Map<Object, Long> outputs, Map<Object, Long> remainingCache, Map<Object, Long> pendingCache, int executions,
            long[] targets, BottledChaosItemInputs.Consumption itemConsumption) {
            this.family = family;
            this.entry = entry;
            this.baseEUt = baseEUt;
            this.duration = duration;
            this.upkeep = Collections.unmodifiableList(new ArrayList<>(upkeep));
            this.requiredPools = requiredPools;
            this.consumed = consumed;
            this.outputs = outputs;
            this.remainingCache = remainingCache;
            this.pendingCache = pendingCache;
            this.executions = executions;
            this.targets = targets;
            this.itemConsumption = itemConsumption;
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
            return createOutputPlan(outputs, duration, executions);
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
     * NEI row data: authorization, optional root alternatives, searchable entry/endpoints, complete long quantities
     * and grouped processing times. Lookup identities do not remove any displayed material.
     */
    @Desugar
    public record DisplayRecipe(int routeIndex, long requiredTagMask, ItemStack[] inputAlternatives,
        Object[] lookupInputs, Object[] lookupOutputs, long baseEUt, List<ItemStackLong> itemInputs,
        List<FluidStackLong> fluidInputs, List<RecipeMapDuration> processingTimes, Plan plan) {}

}
