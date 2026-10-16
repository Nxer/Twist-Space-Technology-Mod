package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CosmicNeutroniumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IronDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeMark;
import com.Nxer.TwistSpaceTechnology.util.recipes.RecipeMaterialUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import tectech.loader.recipe.Godforge;
import tectech.recipe.TecTechRecipeMaps;

/** Nominal weighted averages of the native Godforge challenges at actualParallel = 1. */
public final class BottledChaosExoticMatterRecipes {

    private static final int QGP_CYCLES = 1_000;

    public static void registerRecipeChains(RecipeCatalog catalog) {
        Set<Object> inputs = new LinkedHashSet<>();
        for (ItemStack stack : Godforge.exoticModulePlasmaItemMap.keySet()) inputs.add(unifiedDustId(stack));
        for (ItemStack stack : Godforge.exoticModuleMagmatterItemMap.keySet()) inputs.add(unifiedDustId(stack));
        for (FluidStack stack : Godforge.exoticModulePlasmaFluidMap.keySet()) inputs.add(requireFluid(stack.copy()));
        Map<Object, PlasmaSource> sources = collectPlasmaSources(inputs);
        Map<PlasmaSource, RecipeMark> preparations = new HashMap<>();
        for (PlasmaSource source : sources.values()) {
            catalog.registerExternalInputs(source.input());
            preparations.put(
                source,
                catalog.markTemporaryRecipe(
                    TecTechRecipeMaps.godforgePlasmaRecipes,
                    source.input(),
                    source.plasma(),
                    -1,
                    source.recipe()));
        }

        registerQuarkGluonPlasma(
            catalog,
            solidCandidates(Godforge.exoticModulePlasmaItemMap, sources),
            fluidCandidates(Godforge.exoticModulePlasmaFluidMap, sources),
            preparations);
        registerMagmatter(catalog, solidCandidates(Godforge.exoticModuleMagmatterItemMap, sources), preparations);
        catalog.setQueryMarkers("magmatter", new Object[0], null);
        catalog.setQueryMarkers("quark_gluon_plasma", new Object[0], null);
    }

    private static void registerQuarkGluonPlasma(RecipeCatalog catalog, List<SolidCandidate> solids,
        List<FluidCandidate> fluids, Map<PlasmaSource, RecipeMark> preparations) {
        int[] solidWeights = new int[solids.size()];
        for (int index = 0; index < solids.size(); index++) solidWeights[index] = solids.get(index)
            .weight();
        int[] fluidWeights = new int[fluids.size()];
        for (int index = 0; index < fluids.size(); index++) fluidWeights[index] = fluids.get(index)
            .weight();

        // Native exclusive bounds: 1..7 solid types, 0..6 fluid types, 3.5 dust and 32 mB per selected type.
        int[] solidGifts = apportionGifts(inclusionProbabilities(solidWeights, 1, 7), 3.5 * QGP_CYCLES, 14_000);
        int[] fluidGifts = apportionGifts(inclusionProbabilities(fluidWeights, 0, 6), 32.0 * QGP_CYCLES, 96_000);
        ItemStack[] returnedDusts = new ItemStack[solids.size()];
        FluidStack[] plasmaInputs = new FluidStack[solids.size() + fluids.size()];
        FluidStack[] fluidOutputs = new FluidStack[fluids.size() + 1];
        for (int index = 0; index < solids.size(); index++) {
            SolidCandidate candidate = solids.get(index);
            returnedDusts[index] = candidate.input()
                .getItemStack(solidGifts[index]);
            plasmaInputs[index] = new FluidStack(candidate.plasma(), Math.multiplyExact(144 * 9, solidGifts[index]));
        }
        for (int index = 0; index < fluids.size(); index++) {
            FluidCandidate candidate = fluids.get(index);
            plasmaInputs[solids.size() + index] = new FluidStack(
                candidate.plasma(),
                Math.multiplyExact(1_000, fluidGifts[index]));
            fluidOutputs[index] = new FluidStack(candidate.input(), fluidGifts[index]);
        }
        Fluid product = requireFluid(Materials.QuarkGluonPlasma.getFluid(1));
        fluidOutputs[fluids.size()] = new FluidStack(product, Math.multiplyExact(1_000, QGP_CYCLES));
        GTRecipe exotic = new GTRecipe(
            false,
            null,
            returnedDusts,
            null,
            null,
            null,
            null,
            null,
            plasmaInputs,
            fluidOutputs,
            Math.multiplyExact(200, QGP_CYCLES),
            (int) TierEU.RECIPE_MAX,
            0);
        catalog.registerChain(
            "quark_gluon_plasma",
            1,
            TierEU.RECIPE_MAX,
            candidateRoot(solids, IronDust),
            new Object[] { product },
            new Object[] {},
            catalog.markTemporaryRecipe(
                TecTechRecipeMaps.godforgeExoticMatterRecipes,
                plasmaInputs[0].getFluid(),
                product,
                -1,
                exotic));
        for (SolidCandidate candidate : solids) catalog.registerChainInputSynthesis(
            "quark_gluon_plasma",
            candidate.plasma(),
            preparations.get(candidate.source()));
        for (FluidCandidate candidate : fluids) catalog.registerChainInputSynthesis(
            "quark_gluon_plasma",
            candidate.plasma(),
            preparations.get(candidate.source()));
        // Whole gifts give exact macro totals: 112000 dust + 95904000 mB -> 1000000 mB QGP, 10400000 ticks.
        // Individual mean errors are <0.008 dust or <0.999 mB per 1000 mB QGP; there is no sampling at runtime.
    }

    private static void registerMagmatter(RecipeCatalog catalog, List<SolidCandidate> solids,
        Map<PlasmaSource, RecipeMark> preparations) {
        int weight = solids.get(0)
            .weight();
        ItemStack[] returnedDusts = new ItemStack[solids.size()];
        FluidStack[] plasmaInputs = new FluidStack[solids.size()];
        for (int index = 0; index < solids.size(); index++) {
            SolidCandidate candidate = solids.get(index);
            if (candidate.weight() != weight) throw new IllegalStateException("unequal native magmatter weights");
            returnedDusts[index] = candidate.input()
                .getItemStack(1);
            plasmaInputs[index] = new FluidStack(candidate.plasma(), 144 * 50);
        }
        Fluid product = requireFluid(Materials.MagMatter.getMolten(1));
        // Time and Space are gifted and consumed unchanged in the native challenge, so their net cost is zero.
        GTRecipe exotic = new GTRecipe(
            false,
            null,
            returnedDusts,
            null,
            null,
            null,
            null,
            null,
            plasmaInputs,
            new FluidStack[] { new FluidStack(product, Math.multiplyExact(576, solids.size())) },
            Math.multiplyExact(200, solids.size()),
            (int) TierEU.RECIPE_MAX,
            0);
        catalog.registerChain(
            "magmatter",
            1,
            TierEU.RECIPE_MAX,
            candidateRoot(solids, CosmicNeutroniumDust),
            new Object[] { product },
            new Object[] {},
            catalog.markTemporaryRecipe(
                TecTechRecipeMaps.godforgeExoticMatterRecipes,
                plasmaInputs[0].getFluid(),
                product,
                -1,
                exotic));
        for (SolidCandidate candidate : solids)
            catalog.registerChainInputSynthesis("magmatter", candidate.plasma(), preparations.get(candidate.source()));
        // The current 14-type pool nets 49 dust each -> 8064 mB, with 454000 preparation + 2800 exotic ticks.
    }

    private static TST_ItemID candidateRoot(List<SolidCandidate> candidates, TST_ItemID preferred) {
        for (SolidCandidate candidate : candidates) if (candidate.input()
            .equals(preferred)) return candidate.input();
        throw new IllegalStateException("missing preferred Godforge input root: " + preferred);
    }

    /** Index only the actual preparation pool, once, preserving the selected source's work and voltage. */
    private static Map<Object, PlasmaSource> collectPlasmaSources(Set<Object> candidates) {
        Map<Object, PlasmaSource> sources = new HashMap<>();
        for (GTRecipe recipe : TecTechRecipeMaps.godforgePlasmaRecipes.getAllRecipes()) {
            if (!recipe.mEnabled || recipe.mFakeRecipe
                || recipe.mDuration <= 0
                || recipe.mEUt <= 0
                || recipe.mFluidOutputs == null
                || recipe.mFluidOutputs.length != 1
                || recipe.mFluidOutputs[0] == null
                || recipe.mOutputs != null && recipe.mOutputs.length != 0) continue;
            Object match;
            Object input;
            if (recipe.mInputs != null && recipe.mInputs.length == 1
                && recipe.mInputs[0] != null
                && recipe.mInputs[0].stackSize == 1
                && !RecipeMaterialUtils.isFractionalDust(recipe.mInputs[0])
                && (recipe.mFluidInputs == null || recipe.mFluidInputs.length == 0)
                && recipe.mFluidOutputs[0].amount == 144) {
                match = unifiedDustId(recipe.mInputs[0]);
                input = recipeItemId(recipe.mInputs[0].copy());
            } else if ((recipe.mInputs == null || recipe.mInputs.length == 0) && recipe.mFluidInputs != null
                && recipe.mFluidInputs.length == 1
                && recipe.mFluidInputs[0] != null
                && recipe.mFluidInputs[0].amount == 500
                && recipe.mFluidOutputs[0].amount == 500) {
                    match = requireFluid(recipe.mFluidInputs[0]);
                    input = match;
                } else continue;
            if (!candidates.contains(match)) continue;
            PlasmaSource source = new PlasmaSource(input, requireFluid(recipe.mFluidOutputs[0]), recipe);
            PlasmaSource previous = sources.putIfAbsent(match, source);
            if (previous != null && (!previous.input()
                .equals(source.input()) || previous.plasma() != source.plasma()
                || previous.recipe().mDuration != recipe.mDuration
                || previous.recipe().mEUt != recipe.mEUt))
                throw new IllegalStateException("ambiguous Godforge plasma source: " + match);
        }
        for (Object candidate : candidates) {
            if (!sources.containsKey(candidate))
                throw new IllegalStateException("missing Godforge plasma source: " + candidate);
        }
        return sources;
    }

    private static List<SolidCandidate> solidCandidates(Map<ItemStack, Integer> pool,
        Map<Object, PlasmaSource> sources) {
        if (pool.isEmpty()) throw new IllegalStateException("empty Godforge solid candidate pool");
        List<SolidCandidate> candidates = new ArrayList<>();
        Set<TST_ItemID> identities = new LinkedHashSet<>();
        for (Map.Entry<ItemStack, Integer> entry : pool.entrySet()) {
            PlasmaSource source = sources.get(
                unifiedDustId(
                    entry.getKey()
                        .copy()));
            TST_ItemID input = (TST_ItemID) source.input();
            if (!identities.add(input)) throw new IllegalStateException("duplicate Godforge solid candidate: " + input);
            candidates.add(new SolidCandidate(input, requireWeight(entry.getValue()), source.plasma(), source));
        }
        candidates.sort(
            Comparator.comparing(
                (SolidCandidate candidate) -> Item.itemRegistry.getNameForObject(
                    candidate.input()
                        .item()))
                .thenComparingInt(
                    candidate -> candidate.input()
                        .metaData()));
        return candidates;
    }

    private static List<FluidCandidate> fluidCandidates(Map<FluidStack, Integer> pool,
        Map<Object, PlasmaSource> sources) {
        if (pool.isEmpty()) throw new IllegalStateException("empty Godforge fluid candidate pool");
        List<FluidCandidate> candidates = new ArrayList<>();
        Set<Fluid> identities = new LinkedHashSet<>();
        for (Map.Entry<FluidStack, Integer> entry : pool.entrySet()) {
            Fluid input = requireFluid(
                entry.getKey()
                    .copy());
            if (!identities.add(input))
                throw new IllegalStateException("duplicate Godforge fluid candidate: " + input.getName());
            PlasmaSource source = sources.get(input);
            candidates.add(new FluidCandidate(input, requireWeight(entry.getValue()), source.plasma(), source));
        }
        candidates.sort(
            Comparator.comparing(
                candidate -> candidate.input()
                    .getName()));
        return candidates;
    }

    private static TST_ItemID unifiedDustId(ItemStack stack) {
        if (stack == null || stack.getItem() == null || RecipeMaterialUtils.isFractionalDust(stack))
            throw new IllegalStateException("invalid Godforge full dust candidate");
        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        // The native challenge's Werkstoff iodine and the preparation's GT++ iodine share dustIodine.
        return recipeItemId(Objects.requireNonNull(GTOreDictUnificator.get(false, copy), "unified Godforge dust"));
    }

    private static Fluid requireFluid(FluidStack stack) {
        if (stack == null || stack.getFluid() == null) throw new IllegalStateException("missing Godforge fluid");
        return stack.getFluid();
    }

    private static int requireWeight(Integer weight) {
        if (weight == null || weight <= 0) throw new IllegalStateException("invalid Godforge candidate weight");
        return weight;
    }

    /** Exact grouped weighted draws without replacement; states contain counts rather than material subsets. */
    private static double[] inclusionProbabilities(int[] weights, int minimumDraws, int maximumDraws) {
        Map<Integer, Integer> groups = new TreeMap<>();
        for (int weight : weights) groups.merge(weight, 1, Math::addExact);
        if (maximumDraws > weights.length) throw new IllegalStateException("Godforge pool is too small");
        int[] groupWeights = new int[groups.size()];
        int[] groupSizes = new int[groups.size()];
        int[] strides = new int[groups.size()];
        int states = 1;
        int group = 0;
        for (Map.Entry<Integer, Integer> entry : groups.entrySet()) {
            groupWeights[group] = entry.getKey();
            groupSizes[group] = entry.getValue();
            strides[group] = states;
            states = Math.multiplyExact(states, maximumDraws + 1);
            group++;
        }
        double[] probabilities = new double[states];
        probabilities[0] = 1;
        double[] groupInclusions = new double[groups.size()];
        int[] counts = new int[groups.size()];
        for (int draw = 0; draw <= maximumDraws; draw++) {
            double[] next = draw == maximumDraws ? null : new double[states];
            for (int state = 0; state < states; state++) {
                double probability = probabilities[state];
                if (probability == 0) continue;
                long remainingWeight = 0;
                for (group = 0; group < groups.size(); group++) {
                    counts[group] = state / strides[group] % (maximumDraws + 1);
                    remainingWeight = Math.addExact(
                        remainingWeight,
                        Math.multiplyExact((long) groupSizes[group] - counts[group], groupWeights[group]));
                    if (draw >= minimumDraws) groupInclusions[group] += probability * counts[group]
                        / groupSizes[group]
                        / (maximumDraws - minimumDraws + 1);
                }
                if (next == null) continue;
                for (group = 0; group < groups.size(); group++) {
                    int remaining = groupSizes[group] - counts[group];
                    if (remaining == 0) continue;
                    next[state + strides[group]] += probability * remaining * groupWeights[group] / remainingWeight;
                }
            }
            probabilities = next;
        }
        double[] result = new double[weights.length];
        for (int index = 0; index < weights.length; index++)
            result[index] = groupInclusions[Arrays.binarySearch(groupWeights, weights[index])];
        return result;
    }

    /** Round gifts together so gross plasma preparation and the gift cancellation use one integer allocation. */
    private static int[] apportionGifts(double[] inclusions, double perInclusion, int total) {
        int[] gifts = new int[inclusions.length];
        double[] remainders = new double[inclusions.length];
        Integer[] order = new Integer[inclusions.length];
        int allocated = 0;
        double expectedTotal = 0;
        for (int index = 0; index < inclusions.length; index++) {
            double quota = inclusions[index] * perInclusion;
            gifts[index] = (int) Math.floor(quota);
            remainders[index] = quota - gifts[index];
            allocated = Math.addExact(allocated, gifts[index]);
            expectedTotal += quota;
            order[index] = index;
        }
        if (Math.abs(expectedTotal - total) > 0.000_001 || allocated > total || total - allocated > order.length)
            throw new IllegalStateException("invalid Godforge mean apportionment");
        // Candidates are sorted by registry identity, giving a stable tie breaker independent of HashMap order.
        Arrays.sort(
            order,
            Comparator.comparingDouble((Integer index) -> remainders[index])
                .reversed()
                .thenComparingInt(index -> index));
        for (int index = 0; index < total - allocated; index++) gifts[order[index]]++;
        return gifts;
    }

    /** Actual recipe data paired with its normalized consumable input and its real plasma output. */
    @Desugar
    private record PlasmaSource(Object input, Fluid plasma, GTRecipe recipe) {}

    /** One original solid-pool weight with a canonical full-dust identity and its selected source. */
    @Desugar
    private record SolidCandidate(TST_ItemID input, int weight, Fluid plasma, PlasmaSource source) {}

    /** One original fluid-pool weight with its selected source, retaining the fluid registry identity. */
    @Desugar
    private record FluidCandidate(Fluid input, int weight, Fluid plasma, PlasmaSource source) {}
}
