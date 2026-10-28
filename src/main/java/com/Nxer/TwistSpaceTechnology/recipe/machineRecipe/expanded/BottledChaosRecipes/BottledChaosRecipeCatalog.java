package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Furnace;

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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosSourceResolver.NativeSelector;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool.BottledChaosReagentRecipes;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;

/** Startup-only registration, native recipe resolution and exact route composition. */
public final class BottledChaosRecipeCatalog {

    private static final int MAX_GRAPH_PASSES = 256;

    // Flat step storage: each chain owns a contiguous range, without nested recipe lists.
    private final List<StepRef> chainSteps = new ArrayList<>();
    private final List<StepRef> sequenceSteps = new ArrayList<>();
    private final List<RecipeSequence> recipeSequences = new ArrayList<>();
    private final List<RecipeChain> chains = new ArrayList<>(Collections.nCopies(CredentialModel.ROUTE_COUNT, null));
    private final List<ChainLink> links = new ArrayList<>();
    private final List<Expansion> expansions = new ArrayList<>();
    private final List<ReagentRecycling> reagentRecycling = new ArrayList<>();
    private final Set<Object> deferredReagents = new LinkedHashSet<>();
    private final Set<StepRef> marks = new LinkedHashSet<>();
    private final Map<StepRef, InputSubstitution[]> substitutedInputs = new LinkedHashMap<>();
    private final Map<StepRef, Integer> roundedOutputChanceBatches = new LinkedHashMap<>();
    private final Map<Integer, Integer> substitutedChains = new LinkedHashMap<>();
    private final Map<Integer, TST_ItemID> selectorCircuits = new LinkedHashMap<>();
    private final Set<Integer> availableInputChains = new LinkedHashSet<>();
    private final Map<Integer, Object[]> outputMarkers = new LinkedHashMap<>();
    private final Map<Integer, Object[]> displayOutputs = new LinkedHashMap<>();
    private final Set<Object> keyOutputs = new LinkedHashSet<>();
    private final Map<Integer, Object[]> planningTargets = new LinkedHashMap<>();
    private final Map<String, List<Object>> displayRoots = new LinkedHashMap<>();
    private final Map<Integer, ItemStack[][]> displayInputAlternatives = new LinkedHashMap<>();
    private final Set<Integer> hiddenDisplayEntries = new LinkedHashSet<>();
    private final Set<Object> registeredReagentSyntheses = new LinkedHashSet<>();
    private final Set<Object> registeredReagentDecompositions = new LinkedHashSet<>();
    private final Set<Object> externalReagents = new LinkedHashSet<>();
    private final Map<StepRef, RecipeData> resolved = new LinkedHashMap<>();

    /** Register by stable credential tag; each route consumes its balance root and retains its protected outputs. */
    public Entry registerChain(String tag, int capacityCost, long baseEUt, Object balanceRoot,
        Object[] protectedOutputs, StepRef... steps) {
        return registerChain(tag, capacityCost, baseEUt, balanceRoot, protectedOutputs, null, steps);
    }

    /** Preserve explicit resource boundaries during source composition; key markers determine runtime links. */
    public Entry registerChain(String tag, int capacityCost, long baseEUt, Object balanceRoot,
        Object[] protectedOutputs, Object[] boundaryMaterials, StepRef... steps) {
        return registerChain(tag, capacityCost, baseEUt, balanceRoot, protectedOutputs, boundaryMaterials, 0, steps);
    }

    /** Register a primary entry that also requires another processing tag. */
    public Entry registerAuthorizedChain(String tag, String additionalTag, int capacityCost, long baseEUt,
        Object balanceRoot, Object[] protectedOutputs, Object[] boundaryMaterials, StepRef... steps) {
        int additionalIndex = CredentialModel.indexForTag(additionalTag);
        if (additionalIndex < 0) throw new IllegalArgumentException("unknown additional tag: " + additionalTag);
        return registerChain(
            tag,
            capacityCost,
            baseEUt,
            balanceRoot,
            protectedOutputs,
            boundaryMaterials,
            1L << additionalIndex,
            steps);
    }

    private Entry registerChain(String tag, int capacityCost, long baseEUt, Object balanceRoot,
        Object[] protectedOutputs, Object[] boundaryMaterials, long additionalTags, StepRef... steps) {
        int tagIndex = CredentialModel.indexForTag(tag);
        if (tagIndex < 0 || tagIndex >= Long.SIZE
            || chains.get(tagIndex) != null
            || capacityCost <= 0
            || baseEUt <= 0
            || steps.length == 0) throw new IllegalArgumentException("invalid or duplicate recipe chain: " + tag);
        int start = chainSteps.size();
        Collections.addAll(chainSteps, steps);
        chains.set(
            tagIndex,
            new RecipeChain(
                resourceKey(balanceRoot),
                Arrays.stream(protectedOutputs)
                    .map(BottledChaosRecipeCatalog::resourceKey)
                    .toArray(),
                start,
                chainSteps.size(),
                capacityCost,
                baseEUt,
                tagIndex,
                null,
                boundaryMaterials == null ? null
                    : Arrays.stream(boundaryMaterials)
                        .map(BottledChaosRecipeCatalog::resourceKey)
                        .toArray(),
                0,
                (1L << tagIndex) | additionalTags));
        return new Entry(tagIndex);
    }

    /** Another input for an existing credential; its capacity cost is paid only once. */
    public Entry registerAlternativeChain(String tag, long baseEUt, Object balanceRoot, Object[] protectedOutputs,
        StepRef... steps) {
        return registerAlternativeChain(tag, baseEUt, balanceRoot, protectedOutputs, null, steps);
    }

    public Entry registerAlternativeChain(String tag, long baseEUt, Object balanceRoot, Object[] protectedOutputs,
        Object[] boundaryMaterials, StepRef... steps) {
        return registerAlternativeChain(tag, baseEUt, balanceRoot, protectedOutputs, boundaryMaterials, 0, steps);
    }

    /** Additional credential tags authorize this precomputed yield-boost variant. */
    public Entry registerAugmentedChain(String tag, String additionalTag, long baseEUt, Object balanceRoot,
        Object[] protectedOutputs, Object[] boundaryMaterials, StepRef... steps) {
        int additionalIndex = CredentialModel.indexForTag(additionalTag);
        if (additionalIndex < 0) throw new IllegalArgumentException("unknown additional tag: " + additionalTag);
        return registerAlternativeChain(
            tag,
            baseEUt,
            balanceRoot,
            protectedOutputs,
            boundaryMaterials,
            1L << additionalIndex,
            steps);
    }

    private Entry registerAlternativeChain(String tag, long baseEUt, Object balanceRoot, Object[] protectedOutputs,
        Object[] boundaryMaterials, long additionalTags, StepRef... steps) {
        int tagIndex = CredentialModel.indexForTag(tag);
        if (tagIndex < 0 || chains.get(tagIndex) == null || baseEUt <= 0 || steps.length == 0)
            throw new IllegalArgumentException("invalid alternative recipe chain: " + tag);
        int start = chainSteps.size();
        Collections.addAll(chainSteps, steps);
        chains.add(
            new RecipeChain(
                resourceKey(balanceRoot),
                Arrays.stream(protectedOutputs)
                    .map(BottledChaosRecipeCatalog::resourceKey)
                    .toArray(),
                start,
                chainSteps.size(),
                0,
                baseEUt,
                tagIndex,
                null,
                boundaryMaterials == null ? null
                    : Arrays.stream(boundaryMaterials)
                        .map(BottledChaosRecipeCatalog::resourceKey)
                        .toArray(),
                0,
                (1L << tagIndex) | additionalTags));
        return new Entry(chains.size() - 1);
    }

    /** Override key outputs for this tag's existing entries; their identities allow chaining across all tags. */
    public void setExistingEntriesKeyOutputs(String tag, Object... outputs) {
        int tagIndex = CredentialModel.indexForTag(tag);
        if (tagIndex < 0) throw new IllegalArgumentException("unknown key output tag: " + tag);
        Object[] outputKeys = Arrays.stream(outputs)
            .map(BottledChaosRecipeCatalog::resourceKey)
            .toArray();
        for (int entry = 0; entry < chains.size(); entry++) {
            RecipeChain chain = chains.get(entry);
            if (chain == null || chain.tagIndex() != tagIndex) continue;
            outputMarkers.put(entry, outputKeys);
        }
    }

    private Object[] keyOutputs(int entry) {
        return outputMarkers.getOrDefault(
            entry,
            chains.get(entry)
                .protectedOutputs());
    }

    /** Order a family's independent NEI stages without changing credential bits or runtime priorities. */
    public void orderDisplayRecipes(String family, Object... roots) {
        List<Object> keys = new ArrayList<>(roots.length);
        for (Object balanceRoot : roots) keys.add(resourceKey(balanceRoot));
        displayRoots.put(family, keys);
    }

    /** Explicit entry handles keep local rules independent of later registration order. */
    public final class Entry {

        private final int index;

        private Entry(int index) {
            this.index = index;
        }

        /** Presentation only: composition boundaries and execution targets keep their existing meanings. */
        public void setDisplayOutputs(Object... outputs) {
            displayOutputs.put(
                index,
                Arrays.stream(outputs)
                    .map(BottledChaosRecipeCatalog::resourceKey)
                    .toArray());
        }

        public void setPlanningTargets(Object... outputs) {
            if (outputs.length == 0) throw new IllegalArgumentException("empty planning targets");
            planningTargets.put(
                index,
                Arrays.stream(outputs)
                    .map(BottledChaosRecipeCatalog::resourceKey)
                    .toArray());
        }

        public void requireCircuit(int configuration) {
            if (configuration < 1 || configuration > 24) throw new IllegalArgumentException("invalid route circuit");
            selectorCircuits.put(index, TST_ItemID.create(GTUtility.getIntegratedCircuit(configuration)));
        }

        public void useAvailableInputsOnly() {
            availableInputChains.add(index);
        }

        public void includeSynthesisStep(RecipeMap<?> map, Object target, Object inputs, int circuit) {
            includeSynthesisStep(target, registerNativeStep(map, inputs, target, circuit, false));
        }

        public void includeSynthesisStep(Object target, StepRef recipe) {
            expansions.add(new Expansion(recipe, resourceKey(target), true, index));
        }

        public void includeDecompositionStep(RecipeMap<?> map, Object target, Object outputs, int circuit) {
            expansions.add(
                new Expansion(
                    registerNativeStep(map, target, outputs, circuit, true),
                    resourceKey(target),
                    false,
                    index));
        }
    }

    /** The first explicitly selected variant owns the folded NEI row. Runtime entries stay independent. */
    public void groupDisplayRecipes(List<Entry> entries, ItemStack[]... inputs) {
        if (entries.isEmpty()) throw new IllegalArgumentException("empty display recipe group");
        int first = entries.get(0).index;
        for (Entry entry : entries) {
            if (chains.get(entry.index)
                .tagIndex()
                != chains.get(first)
                    .tagIndex())
                throw new IllegalArgumentException("mixed display recipe tags");
            if (entry.index != first) hiddenDisplayEntries.add(entry.index);
        }
        displayInputAlternatives.put(first, inputs);
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
        if (!(primary.balanceRoot() instanceof TST_ItemID) || primary.balanceRoot()
            .equals(alternative))
            throw new IllegalArgumentException("input alternatives must use distinct item roots: " + tag);
        chains.add(
            new RecipeChain(
                alternative,
                primary.protectedOutputs(),
                chainSteps.size(),
                chainSteps.size(),
                0,
                primary.baseEUt(),
                tagIndex,
                primary.balanceRoot(),
                primary.boundaryMaterials(),
                multiplier,
                1L << tagIndex));
    }

    /** Compose a fixed source with the already prepared route it supplies. */
    public void registerRootSubstitution(String tag, String sourceTag, long baseEUt, Object balanceRoot,
        Object replacedRoot, Object[] protectedOutputs, StepRef source) {
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
                resourceKey(balanceRoot),
                Arrays.stream(protectedOutputs)
                    .map(BottledChaosRecipeCatalog::resourceKey)
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

    public StepRef markRecipe(RecipeMap<?> map, Object input, Object output, int circuit) {
        return registerNativeStep(map, input, output, circuit, false);
    }

    /** Scan the native input, then replace its material form once in the startup cache, keeping all quantities. */
    public StepRef markRecipeWithSubstitutedInput(RecipeMap<?> map, TST_ItemID original, TST_ItemID replacement,
        Object output, int circuit) {
        return markRecipeWithSubstitutedInputs(
            map,
            new TST_ItemID[] { original },
            new TST_ItemID[] { replacement },
            output,
            circuit);
    }

    /** Convert a source material form using its exact whole-item ratio, preserving native processing work. */
    public StepRef markRecipeWithSubstitutedInput(RecipeMap<?> map, TST_ItemID original, TST_ItemID replacement,
        int multiplier, Object output, int circuit) {
        if (multiplier <= 0) throw new IllegalArgumentException("input substitution requires a positive ratio");
        StepRef mark = markRecipeWithSubstitutedInput(map, original, replacement, output, circuit);
        substitutedInputs.put(mark, new InputSubstitution[] { new InputSubstitution(replacement, multiplier) });
        return mark;
    }

    /** Replace multiple source item forms in one startup step; matching still uses all native identities. */
    public StepRef markRecipeWithSubstitutedInputs(RecipeMap<?> map, TST_ItemID[] originals, TST_ItemID[] replacements,
        Object output, int circuit) {
        if (originals.length == 0 || originals.length != replacements.length)
            throw new IllegalArgumentException("input substitutions require matching nonempty arrays");
        StepRef mark = markRecipe(map, originals.length == 1 ? originals[0] : originals.clone(), output, circuit);
        substitutedInputs.put(
            mark,
            Arrays.stream(replacements)
                .map(item -> new InputSubstitution(item, 1))
                .toArray(InputSubstitution[]::new));
        return mark;
    }

    /** Balance a finite source sequence and cancel its returned input once during startup. */
    public StepRef markRecipeSequence(RecipeMap<?> map, Object input, Object output, StepRef... steps) {
        if (steps.length == 0) throw new IllegalArgumentException("empty recipe sequence");
        StepRef mark = new StepRef(map, input, output);
        int start = sequenceSteps.size();
        Collections.addAll(sequenceSteps, steps);
        recipeSequences
            .add(new RecipeSequence(mark, resourceKey(input), resourceKey(output), start, sequenceSteps.size()));
        return mark;
    }

    /** Round chance outputs over the specified source batch, without changing the source pool recipe. */
    public StepRef markRecipeWithRoundedOutputChances(RecipeMap<?> map, Object input, Object output, int circuit,
        int batches) {
        if (batches <= 0) throw new IllegalArgumentException("invalid output chance batch");
        StepRef mark = markRecipe(map, input, output, circuit);
        roundedOutputChanceBatches.put(mark, batches);
        return mark;
    }

    /** Prepare a fixed source recipe without registering it in any GT recipe pool. */
    public StepRef markFixedRecipe(RecipeMap<?> map, Object input, Object output, GTRecipe recipe) {
        if (recipe == null || recipe.mEUt <= 0) throw new IllegalArgumentException("invalid fixed recipe");
        StepRef mark = new StepRef(map, input, output);
        resolved.put(mark, BottledChaosRecipeSupport.prepareSourceRecipe(map, recipe));
        return mark;
    }

    /** Cache a dynamic source's deterministic long-quantity equivalent during startup. */
    public StepRef markPreparedRecipe(RecipeMap<?> map, Object input, Object output, RecipeData data) {
        if (data == null || data.inputs.isEmpty() || data.outputs.isEmpty() || data.times.isEmpty())
            throw new IllegalArgumentException("invalid prepared recipe");
        StepRef mark = new StepRef(map, input, output);
        resolved.put(mark, data);
        return mark;
    }

    /** Register named common reagents and their stored upstream syntheses once, during startup. */
    public void includeSynthesisPath(Object... targets) {
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
                includeSynthesisStep(recipe.pool(), target, recipe.inputs(), recipe.circuit());
            if (recipe.inputs() instanceof Object[]inputs) Collections.addAll(pending, inputs);
            else pending.add(recipe.inputs());
        }
    }

    /** Register one stored synthesis step, retaining the caller's existing upstream input boundary. */
    public void includeSynthesisStep(Object target) {
        var recipe = BottledChaosReagentRecipes.getInputSynthesis(target);
        if (recipe == null) throw new IllegalArgumentException("unknown reagent synthesis: " + target);
        if (registeredReagentSyntheses.add(target))
            includeSynthesisStep(recipe.pool(), target, recipe.inputs(), recipe.circuit());
    }

    public void includeSynthesisStep(RecipeMap<?> map, Object target, Object inputs, int circuit) {
        // Furnace and fluid-container fallback recipes are generated on lookup, rather than listed in
        // getAllRecipes(). Keep native container returns and processing work in the composed route.
        StepRef mark = map == Furnace || map == RecipeMaps.cannerRecipes
            ? markFixedRecipe(
                map,
                inputs,
                target,
                BottledChaosSourceResolver.require(map, new ItemStack[] { ((TST_ItemID) inputs).getItemStack(1) }))
            : registerNativeStep(map, inputs, target, circuit, true);
        expansions.add(new Expansion(mark, resourceKey(target), true));
    }

    /** Register the stored downstream decomposition path once, during startup. */
    public void includeDecompositionPath(Object... targets) {
        Set<Object> pending = new LinkedHashSet<>(Arrays.asList(targets));
        while (!pending.isEmpty()) {
            Object target = pending.iterator()
                .next();
            pending.remove(target);
            if (!registeredReagentDecompositions.add(target)) continue;
            var recipe = BottledChaosReagentRecipes.getOutputDecomposition(target);
            if (recipe == null) continue;
            includeDecompositionStep(recipe.pool(), target, recipe.inputs(), recipe.outputs(), recipe.circuit());
            if (recipe.outputs() instanceof Object[]outputs) Collections.addAll(pending, outputs);
            else pending.add(recipe.outputs());
        }
    }

    public void includeDecompositionStep(RecipeMap<?> map, Object target, Object outputs, int circuit) {
        includeDecompositionStep(map, target, target, outputs, circuit);
    }

    /** Match specific input identities while balancing only the output branch's target resource. */
    public void includeDecompositionStep(RecipeMap<?> map, Object target, Object inputs, Object outputs, int circuit) {
        expansions
            .add(new Expansion(registerNativeStep(map, inputs, outputs, circuit, true), resourceKey(target), false));
    }

    /** Reuse one coproduct for another reagent before synthesizing or decomposing their net remainders. */
    public void registerReagentRecycling(RecipeMap<?> map, Object returned, Object needed, int circuit) {
        reagentRecycling.add(
            new ReagentRecycling(
                registerNativeStep(map, returned, needed, circuit, true),
                resourceKey(returned),
                resourceKey(needed)));
        deferredReagents.add(resourceKey(returned));
        deferredReagents.add(resourceKey(needed));
    }

    /** Keep reusable reagents intact until every main line, branch and returned input has been combined. */
    public void registerDeferredReagents(Object... resources) {
        for (Object resource : resources) deferredReagents.add(resourceKey(resource));
    }

    /** Declare external supply; compilation applies this boundary after collecting all reagent paths. */
    public void registerExternalReagents(Object... resources) {
        for (Object resource : resources) externalReagents.add(resourceKey(resource));
    }

    private StepRef registerNativeStep(RecipeMap<?> map, Object input, Object output, int circuit, boolean optional) {
        if (input == null && output == null)
            throw new IllegalArgumentException("recipe mark must identify an input or output: " + map);
        StepRef mark = new StepRef(map, input, output, new NativeSelector(map, input, output, circuit), optional);
        marks.add(mark);
        return mark;
    }

    private void validateRegisteredChains() {
        for (int index = 0; index < chains.size(); index++) {
            if (chains.get(index) == null)
                throw new IllegalStateException("missing recipe chain for tag " + CredentialModel.tag(index));
        }
    }

    private int[] capacityCosts() {
        int[] costs = new int[CredentialModel.ROUTE_COUNT];
        for (int index = 0; index < costs.length; index++) costs[index] = chains.get(index)
            .capacityCost();
        return costs;
    }

    // Scan each selected pool once and resolve the flat marker directory.
    private void scanMarkedRecipes() {
        Set<NativeSelector> selectors = new LinkedHashSet<>();
        for (StepRef step : marks) selectors.add(step.selector);
        var sources = new BottledChaosSourceResolver(selectors);
        for (StepRef mark : marks) {
            for (RecipeData source : sources
                .preparedSources(mark.selector, roundedOutputChanceBatches.getOrDefault(mark, 0))) {
                var prepared = source;
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
        for (StepRef mark : marks) {
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
            validateConnections(
                prepared,
                collectIntermediateResources(sequenceSteps, sequence.start(), sequence.end()),
                new LinkedHashSet<>(Arrays.asList(sequence.input(), sequence.output())),
                "sequence " + sequence.mark());
            resolved.put(sequence.mark(), prepared);
        }
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
                        .withAlternativeInput(chain.substitutedRoot(), chain.balanceRoot(), chain.inputMultiplier()),
                    "unrepresentable input alternative");
            } else {
                route = composeChainSteps(chain);
                route = expandInputsAndOutputs(route, chain.protectedOutputs(), false, i);
                // Attach fixed sources before canceling the net materials.
                if (chain.substitutedRoot() != null)
                    route = route.appendBalancedRecipe(routes[substitutedChains.get(i)], chain.substitutedRoot());
                // Deferred reagents may still hide the final balance root; validate it after their expansion below.
                route = route.netInputsAndOutputs();
            }
            // 2. Check the terminal products of both marked routes and derived input variants.
            for (Object terminal : chain.protectedOutputs()) {
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
            routes[i] = expandInputsAndOutputs(routes[i], chain.protectedOutputs(), true, i)
                .netInputsAndOutputs(chain.balanceRoot())
                .reduceBatch();
            TST_ItemID selector = selectorCircuits.get(i);
            if (selector != null) routes[i] = routes[i].requireCatalyst(selector);
        }
        // 4. Key output identities may supply any actual input or catalyst, independently of its tag.
        for (int entry = 0; entry < chains.size(); entry++) Collections.addAll(keyOutputs, keyOutputs(entry));
        // Recovery feed is a chain boundary even when it is not a displayed key product.
        for (int entry : availableInputChains) keyOutputs.add(
            chains.get(entry)
                .balanceRoot());
        int[] executionOrder = entryOrder();
        for (int sourcePosition = 0; sourcePosition < executionOrder.length; sourcePosition++) {
            int source = executionOrder[sourcePosition];
            for (Object key : routes[source].outputs.keySet()) {
                if (!keyOutputs.contains(key)) continue;
                for (int targetPosition = 0; targetPosition < executionOrder.length; targetPosition++) {
                    int target = executionOrder[targetPosition];
                    if (source == target) continue;
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
        return composeMarkedSteps(chainSteps, chain.start(), chain.end(), chain.protectedOutputs());
    }

    private RecipeData composeMarkedSteps(List<StepRef> steps, int start, int end, Object[] protectedOutputs) {
        Set<Object> connections = collectMarkedResources(steps, start, end);
        StepRef first = steps.get(start);
        var result = Objects.requireNonNull(resolved.get(first), first.toString());
        for (int index = start + 1; index < end; index++) {
            StepRef step = steps.get(index);
            var next = Objects.requireNonNull(resolved.get(step), step.toString());
            Object intermediate = null;
            for (Object key : next.inputs.keySet()) {
                // A fraction retained by another branch may still be this step's declared connection.
                if (!result.outputs.containsKey(key)
                    || !connections.contains(key) && !key.equals(resourceKey(step.input()))
                        && !Arrays.asList(protectedOutputs)
                            .contains(key))
                    continue;
                if (intermediate != null) throw new IllegalStateException("ambiguous internal connection for " + step);
                intermediate = key;
            }
            if (intermediate == null) throw new IllegalStateException("missing internal connection for " + step);
            result = result.appendBalancedRecipe(next, intermediate);
        }
        return result;
    }

    /** Keep main-line connections local; another route may use a common reagent as its entry marker. */
    private Set<Object> collectMarkedResources(List<StepRef> steps, int start, int end) {
        Set<Object> resources = new LinkedHashSet<>();
        for (int index = start; index < end; index++) {
            StepRef step = steps.get(index);
            for (Object marker : new Object[] { step.input(), step.output() }) {
                if (marker instanceof Object[]alternatives) {
                    for (Object resource : alternatives) resources.add(resourceKey(resource));
                } else if (marker != null) resources.add(resourceKey(marker));
            }
        }
        return resources;
    }

    // Prepend input syntheses and append output decompositions until the route is complete.
    private RecipeData expandInputsAndOutputs(RecipeData route, Object[] protectedOutputs, boolean finalReagents,
        int entry) {
        var finalOutputs = Arrays.asList(protectedOutputs);
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
                    .balanceRoot()
                    .equals(chain.balanceRoot())) {
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
                for (Object balanceRoot : displayRoots.getOrDefault(key, Collections.emptyList()))
                    stages.put(balanceRoot, new ArrayList<>());
                return stages;
            });
            Object balanceRoot = chain.balanceRoot();
            // Standalone comb sources follow their production tag's primary when no ordinary root matches.
            if ((chain.requiredTagMask() & honeycombMask) != 0 && !roots.containsKey(balanceRoot))
                balanceRoot = chains.get(CredentialModel.ownerTagIndex(chain.requiredTagMask()))
                    .balanceRoot();
            roots.computeIfAbsent(balanceRoot, ignored -> new ArrayList<>())
                .add(entry);
        }
        int[] result = new int[order.size()];
        int next = 0;
        for (var roots : families.values())
            for (var variants : roots.values()) for (int entry : variants) result[next++] = entry;
        return result;
    }

    private Set<Object> collectIntermediateResources(List<StepRef> steps, int start, int end) {
        Set<Object> connections = collectMarkedResources(steps, start, end);
        Set<Object> inputs = new LinkedHashSet<>();
        Set<Object> outputs = new LinkedHashSet<>();
        for (int step = start; step < end; step++) {
            RecipeData source = resolved.get(steps.get(step));
            inputs.addAll(source.inputs.keySet());
            inputs.addAll(source.catalysts.keySet());
            outputs.addAll(source.outputs.keySet());
        }
        connections.retainAll(inputs);
        connections.retainAll(outputs);
        return connections;
    }

    // Local connections and declared branches must close without borrowing another route's boundaries.
    private void validateRouteConnections(RecipeData[] routes) {
        for (int i = 0; i < routes.length; i++) {
            RecipeData route = routes[i];
            RecipeChain chain = chains.get(i);
            RecipeChain markedChain = chain.inputMultiplier() > 0 ? chains.get(chain.tagIndex()) : chain;
            Set<Object> markers = collectMarkedResources(chainSteps, markedChain.start(), markedChain.end());
            Set<Object> connections = collectIntermediateResources(chainSteps, markedChain.start(), markedChain.end());
            for (Expansion expansion : expansions) {
                if ((expansion.entry() < 0 || expansion.entry() == i
                    || chain.inputMultiplier() > 0 && expansion.entry() == chain.tagIndex())
                    && markers.contains(expansion.key())) connections.add(expansion.key());
            }
            Set<Object> boundaries = new LinkedHashSet<>();
            boundaries.add(chain.balanceRoot());
            Collections.addAll(boundaries, chain.protectedOutputs());
            Collections.addAll(boundaries, keyOutputs(i));
            if (chain.boundaryMaterials() != null) Collections.addAll(boundaries, chain.boundaryMaterials());
            validateConnections(route, connections, boundaries, "route " + i);
        }
    }

    private void validateConnections(RecipeData route, Set<Object> connections, Set<Object> boundaries, String owner) {
        connections.removeAll(boundaries);
        connections.removeAll(externalReagents);
        for (Object key : connections) {
            if (route.inputs.containsKey(key) || route.catalysts.containsKey(key))
                throw new IllegalStateException("unresolved intermediate input in " + owner + ": " + key);
            if (route.outputs.containsKey(key))
                throw new IllegalStateException("unfinished or backward output in " + owner + ": " + key);
        }
    }

    /** Resolve all declarations once; runtime receives only prepared routes and their immutable directory. */
    CompiledCatalog compile() {
        validateRegisteredChains();
        expansions.removeIf(expansion -> expansion.synthesis() && externalReagents.contains(expansion.key()));
        scanMarkedRecipes();
        RecipeData[] prepared = composeRecipeChains();
        CompiledRoute[] routes = new CompiledRoute[prepared.length];
        for (int entry = 0; entry < prepared.length; entry++) {
            RecipeData data = BottledChaosProcessAlternatives.group(prepared[entry]);
            RecipeChain chain = chains.get(entry);
            Object[] targets = planningTargets.getOrDefault(entry, chain.protectedOutputs());
            for (Object target : targets) {
                if (!data.outputs.containsKey(target))
                    throw new IllegalStateException("execution target is not a route output: " + target);
            }
            Set<Object> markers = new LinkedHashSet<>(
                Arrays.asList(displayOutputs.getOrDefault(entry, keyOutputs(entry))));
            markers.retainAll(data.outputs.keySet());
            routes[entry] = new CompiledRoute(
                data,
                chain.balanceRoot(),
                targets,
                chain.baseEUt(),
                chain.tagIndex(),
                chain.inputMultiplier(),
                chain.requiredTagMask(),
                availableInputChains.contains(entry),
                displayInputAlternatives.get(entry),
                hiddenDisplayEntries.contains(entry),
                Collections.unmodifiableSet(markers));
        }
        return new CompiledCatalog(
            routes,
            entryOrder(),
            displayOrder(),
            links.toArray(new ChainLink[0]),
            resolved.size(),
            capacityCosts());
    }

    /** Prepared materials and route metadata; source recipes and registration instructions stay in the catalog. */
    @Desugar
    record CompiledRoute(RecipeData data, Object balanceRoot, Object[] planningTargets, long baseEUt, int tagIndex,
        int inputMultiplier, long requiredTagMask, boolean availableInputsOnly, ItemStack[][] inputAlternatives,
        boolean hiddenDisplay, Set<Object> displayOutputs) {}

    @Desugar
    record CompiledCatalog(CompiledRoute[] routes, int[] entryOrder, int[] displayOrder, ChainLink[] links,
        int resolvedStepCount, int[] capacityCosts) {}

    private static Object resourceKey(Object resource) {
        return resource instanceof Fluid fluid ? FluidId.create(fluid) : resource;
    }

    /**
     * Startup route declaration: a flat step range, terminal/connection rules, credential cost, base power and
     * all credential tags required to select this entry.
     * Positive inputMultiplier derives an input-only variant from its primary tag, without extra work.
     */
    @Desugar
    private record RecipeChain(Object balanceRoot, Object[] protectedOutputs, int start, int end, int capacityCost,
        long baseEUt, int tagIndex, Object substitutedRoot, Object[] boundaryMaterials, int inputMultiplier,
        long requiredTagMask) {}

    /** A single declared step. Identity never merges transformation or prepared-data variants. */
    public static final class StepRef {

        private final RecipeMap<?> map;
        private final Object input;
        private final Object output;
        private final NativeSelector selector;
        private final boolean optional;

        private StepRef(RecipeMap<?> map, Object input, Object output) {
            this(map, input, output, null, false);
        }

        private StepRef(RecipeMap<?> map, Object input, Object output, NativeSelector selector, boolean optional) {
            this.map = map;
            this.input = input;
            this.output = output;
            this.selector = selector;
            this.optional = optional;
        }

        Object input() {
            return input;
        }

        Object output() {
            return output;
        }

        @Override
        public String toString() {
            return selector == null ? map.unlocalizedName + " [prepared step]" : selector.toString();
        }
    }

    @Desugar
    private record InputSubstitution(TST_ItemID item, int multiplier) {}

    /** A startup-only contiguous source range, folded into one reusable cached processing step. */
    @Desugar
    private record RecipeSequence(StepRef mark, Object input, Object output, int start, int end) {}

    /** Startup reagent instruction: apply the marked recipe to synthesize an input or decompose an output resource. */
    @Desugar
    private record Expansion(StepRef recipe, Object key, boolean synthesis, int entry) {

        Expansion(StepRef recipe, Object key, boolean synthesis) {
            this(recipe, key, synthesis, -1);
        }
    }

    /** Startup-only coproduct conversion, with direct handling deferred for both reagent identities. */
    @Desugar
    private record ReagentRecycling(StepRef recipe, Object returned, Object needed) {}

    /** Authorized resource flow between actual entry indexes; indexes may differ from credential tag indexes. */
    @Desugar
    record ChainLink(int source, int target, Object key) {}

}
