package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.prepareSourceRecipe;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getGCD;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MiracleDoor;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosSupportedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosSupportedMachine.Replacement;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;
import com.github.bsideup.jabel.Desugar;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import tectech.recipe.TecTechRecipeMaps;

/** Recognize complete native transformations at startup; execution only selects their cached work. */
public final class BottledChaosProcessAlternatives {

    public static final RecipeMap<?> STELLAR_FORGE = GTCMRecipe.StellarForgeRecipeMap;
    public static final RecipeMap<?> GODFORGE_MOLTEN = TecTechRecipeMaps.godforgeMoltenRecipes;
    public static final RecipeMap<?> COOLING = GTCMRecipe.RapidCoolingDownRecipeMap;

    private static final List<Replacement> RULES = java.util.Arrays.stream(BottledChaosSupportedMachine.values())
        .flatMap(
            machine -> machine.replacements()
                .stream())
        .collect(java.util.stream.Collectors.toList());
    private static final List<List<RecipeMap<?>>> CHAINS = RULES.stream()
        .map(Replacement::chain)
        .distinct()
        .sorted(
            java.util.Comparator.comparingInt((List<RecipeMap<?>> chain) -> chain.size())
                .reversed())
        .collect(java.util.stream.Collectors.toList());
    private static final List<Group> pending = new ArrayList<>();
    private static Map<Object, Long> additionalCatalysts = Collections.emptyMap();

    public static List<RecipeMap<?>> replacementPools() {
        List<RecipeMap<?>> pools = new ArrayList<>();
        for (Replacement rule : RULES) {
            if (!pools.contains(rule.pool())) pools.add(rule.pool());
            RecipeMap<?> parent = rule.chain()
                .get(0);
            if (!pools.contains(parent)) pools.add(parent);
        }
        if (!pools.contains(RecipeMaps.fluidSolidifierRecipes)) pools.add(RecipeMaps.fluidSolidifierRecipes);
        return pools;
    }

    static Map<Object, Long> additionalCatalysts() {
        return additionalCatalysts;
    }

    static final class Source {

        final Map<Object, Long> inputs;
        final Map<Object, Long> outputs;
        final Map<Object, Long> catalysts;
        final long ticks;
        private final int hash;

        Source(Map<Object, Long> inputs, Map<Object, Long> outputs, Map<Object, Long> catalysts, long ticks) {
            this.inputs = inputs;
            this.outputs = outputs;
            this.catalysts = catalysts;
            this.ticks = ticks;
            hash = Objects.hash(inputs, outputs, catalysts, ticks);
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof Source source && ticks == source.ticks
                && inputs.equals(source.inputs)
                && outputs.equals(source.outputs)
                && catalysts.equals(source.catalysts);
        }

        RecipeData data(TimeKey key) {
            return new RecipeData(inputs, outputs, catalysts, Collections.singletonMap(key, ticks));
        }
    }

    static final class Group {

        final RecipeData baseline;
        final TimeKey basis;
        final Map<Object, Long> inputs;
        final double scale;
        final List<RecipeMap<?>> chain;
        volatile Map<RecipeMap<?>, List<Version>> versions = Collections.emptyMap();

        Group(RecipeData baseline, TimeKey basis, Map<TimeKey, Long> work, Map<Object, Long> inputs,
            List<RecipeMap<?>> chain, long divisor) {
            this.baseline = baseline;
            this.chain = chain;
            this.basis = basis;
            this.inputs = inputs;
            scale = (double) work.get(basis) / baseline.times.get(basis) / divisor;
        }
    }

    @Desugar
    record Version(Map<TimeKey, Long> times, double scale, Map<Object, Long> omittedInputs, boolean ingotMold) {}

    static Source capture(RecipeMap<?> map, Map<Object, Long> inputs, Map<Object, Long> outputs,
        Map<Object, Long> catalysts, long ticks) {
        return !catalysts.isEmpty() || map == RecipeMaps.fluidSolidifierRecipes
            || RULES.stream()
                .anyMatch(
                    rule -> rule.pool() == map || rule.chain()
                        .contains(map))
            || inputs.keySet()
                .stream()
                .anyMatch(BottledChaosProcessAlternatives::protectionGas)
                    ? new Source(inputs, outputs, catalysts, ticks)
                    : null;
    }

    static void clearPending() {
        pending.clear();
        additionalCatalysts = Collections.emptyMap();
    }

    /** Longest complete chain first; exported/shared intermediate work is never stolen by a replacement. */
    static RecipeData groupAndReduce(RecipeData route) {
        long divisor = route.batchDivisor();
        Map<Object, Long> inputs = new LinkedHashMap<>();
        route.inputs.forEach((key, amount) -> inputs.put(key, amount / divisor));
        Map<TimeKey, Long> remaining = new LinkedHashMap<>(route.times);
        Map<TimeKey, Long> grouped = new LinkedHashMap<>();
        for (List<RecipeMap<?>> chain : CHAINS) for (TimeKey first : new ArrayList<>(remaining.keySet())) {
            if (first.map() != chain.get(0) || first.source() == null) continue;
            if (first.map() == RecipeMaps.vacuumFreezerRecipes && !plasmaCooling(first.source())) continue;
            RecipeData baseline = first.source()
                .data(first);
            boolean complete = true;
            for (int step = 1; step < chain.size(); step++) {
                TimeKey next = null;
                Object intermediate = null;
                for (Object output : baseline.outputs.keySet()) {
                    if (route.inputs.containsKey(output) || route.outputs.containsKey(output)) continue;
                    next = next(remaining, chain.get(step), output);
                    if (next != null) {
                        intermediate = output;
                        break;
                    }
                }
                if (next == null) {
                    complete = false;
                    break;
                }
                baseline = baseline.appendBalancedRecipe(
                    next.source()
                        .data(next),
                    intermediate);
            }
            if (!complete
                || !proportional(baseline.times, remaining, remaining.get(first), baseline.times.get(first), false))
                continue;
            mark(grouped, remaining, new Group(baseline, first, remaining, inputs, chain, divisor));
        }
        grouped.putAll(remaining);
        return route.withTimes(grouped)
            .reduceBatch(divisor);
    }

    private static boolean plasmaCooling(Source source) {
        return source.inputs.size() == 1 && source.outputs.size() == 1
            && source.inputs.keySet()
                .iterator()
                .next() instanceof FluidId fluid
            && fluid.getFluidStack(1)
                .getFluid()
                .getName()
                .startsWith("plasma.");
    }

    private static void mark(Map<TimeKey, Long> grouped, Map<TimeKey, Long> remaining, Group group) {
        for (TimeKey key : group.baseline.times.keySet()) grouped.put(
            new TimeKey(key.map(), key.eut(), key.recipeTicks(), key.requirements(), key.source(), group),
            remaining.remove(key));
        pending.add(group);
    }

    private static TimeKey next(Map<TimeKey, Long> times, RecipeMap<?> pool, Object input) {
        for (TimeKey key : times.keySet()) {
            Source source = key.source();
            if (key.map() == pool && source != null && source.inputs.containsKey(input)) return key;
        }
        return null;
    }

    /** Resolve native recipes once, after Miracle Door publishes its generated pools. */
    static void resolve() {
        Map<RecipeMap<?>, List<GTRecipe>> nativeRecipes = new LinkedHashMap<>();
        for (Replacement rule : RULES)
            nativeRecipes.computeIfAbsent(rule.pool(), pool -> new ArrayList<>(pool.getAllRecipes()));
        Map<FluidId, List<RecipeData>> solidifiers = new LinkedHashMap<>();
        for (GTRecipe recipe : RecipeMaps.fluidSolidifierRecipes.getAllRecipes()) {
            if (recipe.mFluidInputs.length != 1 || recipe.mOutputs.length != 1
                || recipe.mFluidOutputs.length != 0
                || recipe.mInputs.length != 1
                || recipe.mInputs[0].stackSize != 0
                || !GTUtility.areStacksEqual(recipe.mInputs[0], ItemList.Shape_Mold_Ingot.get(1))) continue;
            solidifiers.computeIfAbsent(FluidId.create(recipe.mFluidInputs[0]), ignored -> new ArrayList<>())
                .add(prepareSourceRecipe(RecipeMaps.fluidSolidifierRecipes, recipe));
        }
        Map<Object, Long> catalysts = new LinkedHashMap<>();
        int count = 0;
        for (Group group : pending) {
            Map<RecipeMap<?>, List<Version>> versions = new LinkedHashMap<>();
            for (Replacement rule : RULES) {
                if (!rule.chain()
                    .equals(group.chain)) continue;
                for (GTRecipe recipe : nativeRecipes.get(rule.pool())) {
                    if (recipe.mDuration <= 0 || recipe.mEUt < 0) continue;
                    RecipeData nativeData;
                    if (rule.pool() == COOLING) {
                        if (recipe.mFluidInputs.length != 1 || recipe.mFluidOutputs.length != 1
                            || recipe.mInputs.length != 0
                            || recipe.mOutputs.length != 0) continue;
                        nativeData = new RecipeData(
                            Collections.singletonMap(
                                FluidId.create(recipe.mFluidInputs[0]),
                                (long) recipe.mFluidInputs[0].amount),
                            Collections.singletonMap(
                                FluidId.create(recipe.mFluidOutputs[0]),
                                (long) recipe.mFluidOutputs[0].amount),
                            Collections.emptyMap(),
                            Collections.singletonMap(new TimeKey(COOLING, 0), 1L));
                    } else {
                        if (recipe.mEUt == 0) continue;
                        nativeData = prepareSourceRecipe(rule.pool(), recipe);
                    }
                    // Some furnace recipes already preserve their outputs without solidification.
                    addVersion(group, rule, nativeData, false, versions, catalysts);
                    if (rule.solidify()) {
                        for (Object output : nativeData.outputs.keySet()) if (output instanceof FluidId fluid)
                            for (RecipeData finish : solidifiers.getOrDefault(fluid, Collections.emptyList()))
                                addVersion(
                                    group,
                                    rule,
                                    nativeData.appendBalancedRecipe(finish, fluid),
                                    false,
                                    versions,
                                    catalysts);
                        if (rule.moldBypass()) addVersion(
                            group,
                            rule,
                            prepareSourceRecipe(rule.pool(), TST_MiracleDoor.turnToIngotRecipe(recipe)),
                            true,
                            versions,
                            catalysts);
                    }
                }
            }
            group.versions = Collections.unmodifiableMap(versions);
            count += versions.values()
                .stream()
                .mapToInt(List::size)
                .sum();
        }
        additionalCatalysts = Collections.unmodifiableMap(catalysts);
        TwistSpaceTechnology.LOG
            .info("Bottled Chaos: {} alternatives for {} complete source chains.", count, pending.size());
        pending.clear();
    }

    private static void addVersion(Group group, Replacement rule, RecipeData candidate, boolean mold,
        Map<RecipeMap<?>, List<Version>> versions, Map<Object, Long> catalysts) {
        Map<Object, Long> inputs = new LinkedHashMap<>(group.baseline.inputs);
        Set<Object> gases = new java.util.LinkedHashSet<>();
        if (rule.removeGas()) inputs.keySet()
            .removeIf(key -> {
                if (!protectionGas(key)) return false;
                gases.add(key);
                return true;
            });
        if (candidate.outputs.isEmpty() || !inputs.keySet()
            .equals(candidate.inputs.keySet())
            || !group.baseline.outputs.keySet()
                .equals(candidate.outputs.keySet()))
            return;
        Object output = candidate.outputs.keySet()
            .iterator()
            .next();
        long original = group.baseline.outputs.get(output), replacement = candidate.outputs.get(output);
        long gcd = getGCD(original, replacement), numerator = original / gcd, denominator = replacement / gcd;
        if (!proportional(candidate.inputs, inputs, numerator, denominator, true)
            || !proportional(candidate.outputs, group.baseline.outputs, numerator, denominator, true)) return;
        Map<Object, Long> omitted = new LinkedHashMap<>();
        for (Object gas : gases) omitted.put(
            gas,
            Math.min(
                group.inputs.getOrDefault(gas, 0L),
                (long) Math.floor(group.baseline.inputs.get(gas) * group.scale)));
        versions.computeIfAbsent(rule.pool(), ignored -> new ArrayList<>())
            .add(
                new Version(
                    candidate.times,
                    group.scale * numerator / denominator,
                    Collections.unmodifiableMap(omitted),
                    mold));
        candidate.catalysts.forEach((key, amount) -> catalysts.merge(key, amount, Math::max));
    }

    private static <K> boolean proportional(Map<K, Long> source, Map<K, Long> target, long numerator, long denominator,
        boolean exactKeys) {
        if (exactKeys && !source.keySet()
            .equals(target.keySet())) return false;
        for (var entry : source.entrySet()) {
            Long amount = target.get(entry.getKey());
            if (amount == null || !BigInteger.valueOf(entry.getValue())
                .multiply(BigInteger.valueOf(numerator))
                .equals(
                    BigInteger.valueOf(amount)
                        .multiply(BigInteger.valueOf(denominator))))
                return false;
        }
        return true;
    }

    private static boolean protectionGas(Object key) {
        if (!(key instanceof FluidId id)) return false;
        var fluid = id.getFluidStack(1)
            .getFluid();
        return fluid == Materials.Hydrogen.mGas || fluid == Materials.Oxygen.mGas
            || fluid == Materials.Nitrogen.mGas
            || fluid == Materials.Radon.mGas
            || fluid == Materials.Argon.mGas
            || fluid == Materials.Helium.mGas
            || fluid == WerkstoffLoader.Xenon.getFluidOrGas(1)
                .getFluid()
            || fluid == WerkstoffLoader.Oganesson.getFluidOrGas(1)
                .getFluid()
            || fluid == WerkstoffLoader.Krypton.getFluidOrGas(1)
                .getFluid()
            || fluid == WerkstoffLoader.Neon.getFluidOrGas(1)
                .getFluid();
    }

}
