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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.util.recipes.RecipeMaterialUtils;
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
    private final List<RecipeMark> chainSteps = new ArrayList<>();
    private final List<RecipeMark> sequenceSteps = new ArrayList<>();
    private final List<RecipeSequence> recipeSequences = new ArrayList<>();
    private final List<RecipeChain> chains = new ArrayList<>(Collections.nCopies(CredentialModel.ROUTE_COUNT, null));
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
    private final Map<Integer, Object[]> outputMarkers = new LinkedHashMap<>();
    private final Set<Object> keyOutputs = new LinkedHashSet<>();
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
    public void registerAuthorizedChain(String tag, String additionalTag, int capacityCost, long baseEUt, Object root,
        Object[] terminals, Object[] chainableOutputs, RecipeMark... steps) {
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
                    .map(BottledChaosRecipeCatalog::resourceKey)
                    .toArray(),
                start,
                chainSteps.size(),
                capacityCost,
                baseEUt,
                routeIndex,
                null,
                chainableOutputs == null ? null
                    : Arrays.stream(chainableOutputs)
                        .map(BottledChaosRecipeCatalog::resourceKey)
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
    public void registerAugmentedChain(String tag, String additionalTag, long baseEUt, Object root, Object[] terminals,
        Object[] chainableOutputs, RecipeMark... steps) {
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
                    .map(BottledChaosRecipeCatalog::resourceKey)
                    .toArray(),
                start,
                chainSteps.size(),
                0,
                baseEUt,
                tagIndex,
                null,
                chainableOutputs == null ? null
                    : Arrays.stream(chainableOutputs)
                        .map(BottledChaosRecipeCatalog::resourceKey)
                        .toArray(),
                0,
                (1L << tagIndex) | additionalTags));
    }

    /** Override key outputs for this tag's existing entries; their identities allow chaining across all tags. */
    public void setKeyOutputs(String tag, Object... outputs) {
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
                .terminals());
    }

    /** Select main products for demand planning without changing protected coproducts or NEI markers. */
    public void setExecutionTargets(String tag, Object... outputs) {
        if (outputs.length == 0) throw new IllegalArgumentException("empty execution targets: " + tag);
        executionTargets.put(
            latestChainIndex(tag),
            Arrays.stream(outputs)
                .map(BottledChaosRecipeCatalog::resourceKey)
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
        selectorCircuits.put(latestChainIndex(tag), TST_ItemID.create(GTUtility.getIntegratedCircuit(configuration)));
    }

    /** Recycle existing feed without recursively synthesizing more of that feed. */
    public void useAvailableInputsOnly(String tag) {
        availableInputChains.add(latestChainIndex(tag));
    }

    /** Use one branch's synthesis only for the latest same-tag entry; shared paths remain unchanged. */
    public void registerChainInputSynthesis(String tag, RecipeMap<?> map, Object target, Object inputs, int circuit) {
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
    public void registerRootSubstitution(String tag, String sourceTag, long baseEUt, Object root, Object replacedRoot,
        Object[] terminals, RecipeMark source) {
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
        RecipeMark mark = markRecipe(map, originals.length == 1 ? originals[0] : originals.clone(), output, circuit);
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
    public RecipeMark markTemporaryRecipe(RecipeMap<?> map, Object input, Object output, int circuit, GTRecipe recipe) {
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

    public RecipeMark markPreparedRecipe(RecipeMap<?> map, Object input, Object output, int circuit, RecipeData data) {
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
        expansions
            .add(new Expansion(registerRecipeMark(map, inputs, outputs, circuit, true), resourceKey(target), false));
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
            routes[i] = expandInputsAndOutputs(routes[i], chain.terminals(), true, i).netInputsAndOutputs(chain.root())
                .reduceBatch();
            TST_ItemID selector = selectorCircuits.get(i);
            if (selector != null) routes[i] = routes[i].requireCatalyst(selector);
        }
        // 4. Key output identities may supply any actual input or catalyst, independently of its tag.
        for (int entry = 0; entry < chains.size(); entry++) Collections.addAll(keyOutputs, keyOutputs(entry));
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
                if (intermediate != null) throw new IllegalStateException("ambiguous internal connection for " + step);
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
    private RecipeData expandInputsAndOutputs(RecipeData route, Object[] terminals, boolean finalReagents, int entry) {
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

    /** Resolve all declarations once; runtime receives only prepared routes and their immutable directory. */
    CompiledCatalog compile() {
        validateRegisteredChains();
        publishCapacityCosts();
        scanMarkedRecipes();
        RecipeData[] prepared = composeRecipeChains();
        CompiledRoute[] routes = new CompiledRoute[prepared.length];
        for (int entry = 0; entry < prepared.length; entry++) {
            RecipeData data = BottledChaosProcessAlternatives.group(prepared[entry]);
            RecipeChain chain = chains.get(entry);
            Object[] targets = executionTargets.getOrDefault(entry, chain.terminals());
            for (Object target : targets) {
                if (!data.outputs.containsKey(target))
                    throw new IllegalStateException("execution target is not a route output: " + target);
            }
            routes[entry] = new CompiledRoute(
                data,
                chain.root(),
                targets,
                chain.baseEUt(),
                chain.tagIndex(),
                chain.inputMultiplier(),
                chain.requiredTagMask(),
                availableInputChains.contains(entry),
                displayInputAlternatives.get(entry),
                hiddenDisplayEntries.contains(entry));
        }
        return new CompiledCatalog(
            routes,
            entryOrder(),
            displayOrder(),
            links.toArray(new ChainLink[0]),
            Collections.unmodifiableSet(keyOutputs),
            resolved.size());
    }

    /** Prepared materials and route metadata; source recipes and registration instructions stay in the catalog. */
    @Desugar
    record CompiledRoute(RecipeData data, Object root, Object[] targetOutputs, long baseEUt, int tagIndex,
        int inputMultiplier, long requiredTagMask, boolean availableInputsOnly, ItemStack[][] inputAlternatives,
        boolean hiddenDisplay) {}

    @Desugar
    record CompiledCatalog(CompiledRoute[] routes, int[] entryOrder, int[] displayOrder, ChainLink[] links,
        Set<Object> keyOutputs, int resolvedStepCount) {}

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
    record ChainLink(int source, int target, Object key) {}

}
