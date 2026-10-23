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
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableList;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility.FluidId;
import tectech.recipe.TecTechRecipeMaps;

/** Recognize complete native transformations at startup; execution only selects their cached work. */
public final class BottledChaosProcessAlternatives {

    public static final RecipeMap<?> STELLAR_FORGE = GTCMRecipe.StellarForgeRecipeMap;
    public static final RecipeMap<?> GODFORGE_MOLTEN = TecTechRecipeMaps.godforgeMoltenRecipes;
    public static final RecipeMap<?> COOLING = GTCMRecipe.RapidCoolingDownRecipeMap;
    private static final DisplayGroup DISPLAY_GROUP = new DisplayGroup(
        ImmutableList
            .of(RecipeMaps.blastFurnaceRecipes, RecipeMaps.vacuumFreezerRecipes, RecipeMaps.fluidExtractionRecipes),
        ImmutableList.of(STELLAR_FORGE, GODFORGE_MOLTEN, COOLING));
    private static final List<Group> pending = new ArrayList<>();

    /** Pool-list membership and original-column order only; valid substitutions still come from cached versions. */
    @Desugar
    public record DisplayGroup(List<RecipeMap<?>> originals, List<RecipeMap<?>> alternatives) {}

    public static DisplayGroup displayGroup() {
        return DISPLAY_GROUP;
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
        volatile Map<RecipeMap<?>, List<Version>> versions = Collections.emptyMap();

        Group(RecipeData baseline, TimeKey basis, Map<TimeKey, Long> work, Map<Object, Long> inputs) {
            this.baseline = baseline;
            this.basis = basis;
            this.inputs = inputs;
            scale = (double) work.get(basis) / baseline.times.get(basis);
        }
    }

    @Desugar
    record Version(Map<TimeKey, Long> times, double scale, Map<Object, Long> omittedInputs) {}

    static Source capture(RecipeMap<?> map, Map<Object, Long> inputs, Map<Object, Long> outputs,
        Map<Object, Long> catalysts, long ticks) {
        return map == RecipeMaps.blastFurnaceRecipes || map == RecipeMaps.vacuumFreezerRecipes
            || map == RecipeMaps.fluidExtractionRecipes
            || inputs.keySet()
                .stream()
                .anyMatch(BottledChaosProcessAlternatives::protectionGas)
                    ? new Source(inputs, outputs, catalysts, ticks)
                    : null;
    }

    static void clearPending() {
        pending.clear();
    }

    /** Preserve net materials and NEI work; mark only complete segments whose intermediates are fully internal. */
    static RecipeData group(RecipeData route) {
        Map<TimeKey, Long> remaining = new LinkedHashMap<>(route.times);
        Map<TimeKey, Long> grouped = new LinkedHashMap<>();
        for (TimeKey furnace : route.times.keySet()) {
            if (furnace.map() != RecipeMaps.blastFurnaceRecipes || !remaining.containsKey(furnace)) continue;
            if (furnace.source() == null) continue;
            Object ingot = singleItem(furnace.source().outputs);
            if (ingot == null || route.outputs.containsKey(ingot) || route.inputs.containsKey(ingot)) continue;
            RecipeData baseline = furnace.source()
                .data(furnace);
            TimeKey extraction = next(remaining, RecipeMaps.fluidExtractionRecipes, ingot);
            if (extraction == null) {
                TimeKey freezer = next(remaining, RecipeMaps.vacuumFreezerRecipes, ingot);
                if (freezer == null) continue;
                Object cooled = singleItem(freezer.source().outputs);
                if (cooled == null || route.outputs.containsKey(cooled) || route.inputs.containsKey(cooled)) continue;
                extraction = next(remaining, RecipeMaps.fluidExtractionRecipes, cooled);
                if (extraction == null) continue;
                baseline = baseline.appendBalancedRecipe(
                    freezer.source()
                        .data(freezer),
                    ingot);
                ingot = cooled;
            }
            if (extraction.source().outputs.size() != 1 || !(extraction.source().outputs.keySet()
                .iterator()
                .next() instanceof FluidId)) continue;
            baseline = baseline.appendBalancedRecipe(
                extraction.source()
                    .data(extraction),
                ingot);
            // Partial conversion cannot substitute work belonging to exported ingots or another downstream use.
            long numerator = remaining.get(furnace);
            long denominator = baseline.times.get(furnace);
            if (!proportional(baseline.times, remaining, numerator, denominator, false)) continue;
            mark(grouped, remaining, new Group(baseline, furnace, remaining, route.inputs));
        }
        for (TimeKey freezer : new ArrayList<>(remaining.keySet())) {
            Source source = freezer.source();
            if (freezer.map() != RecipeMaps.vacuumFreezerRecipes || source == null
                || source.inputs.size() != 1
                || source.outputs.size() != 1
                || !(source.inputs.keySet()
                    .iterator()
                    .next() instanceof FluidId fluid)
                || !fluid.getFluidStack(1)
                    .getFluid()
                    .getName()
                    .startsWith("plasma."))
                continue;
            mark(grouped, remaining, new Group(source.data(freezer), freezer, remaining, route.inputs));
        }
        grouped.putAll(remaining);
        return route.withTimes(grouped);
    }

    private static void mark(Map<TimeKey, Long> grouped, Map<TimeKey, Long> remaining, Group group) {
        for (TimeKey key : group.baseline.times.keySet()) grouped.put(
            new TimeKey(key.map(), key.eut(), key.recipeTicks(), key.requirements(), key.source(), group),
            remaining.remove(key));
        pending.add(group);
    }

    private static Object singleItem(Map<Object, Long> amounts) {
        Object result = null;
        for (Object key : amounts.keySet()) if (key instanceof TST_ItemID) {
            if (result != null) return null;
            result = key;
        }
        return result;
    }

    private static TimeKey next(Map<TimeKey, Long> times, RecipeMap<?> pool, Object input) {
        for (TimeKey key : times.keySet()) {
            Source source = key.source();
            if (key.map() == pool && source != null && source.inputs.size() == 1 && source.inputs.containsKey(input))
                return key;
        }
        return null;
    }

    /** Native Stellar Forge recipes are generated at server-start, after the ordinary recipe cache. */
    static void resolve() {
        int stellarForge = 0, godforge = 0, cooling = 0;
        for (Group group : pending) {
            Map<RecipeMap<?>, List<Version>> versions = new LinkedHashMap<>();
            if (group.basis.map() == RecipeMaps.blastFurnaceRecipes) {
                if (cacheVersions(group, STELLAR_FORGE, versions)) stellarForge++;
                if (cacheVersions(group, GODFORGE_MOLTEN, versions)) godforge++;
            } else if (cacheVersions(group, COOLING, versions)) cooling++;
            group.versions = Collections.unmodifiableMap(versions);
        }
        TwistSpaceTechnology.LOG.info(
            "Bottled Chaos process alternatives: {} Stellar Forge, {} Godforge Molten, {} plasma cooling ({} segments).",
            stellarForge,
            godforge,
            cooling,
            pending.size());
        pending.clear();
    }

    private static boolean cacheVersions(Group group, RecipeMap<?> pool, Map<RecipeMap<?>, List<Version>> versions) {
        List<Version> matches = match(group, pool);
        if (matches.isEmpty()) return false;
        versions.put(pool, Collections.unmodifiableList(matches));
        return true;
    }

    private static List<Version> match(Group group, RecipeMap<?> nativePool) {
        List<Version> matches = new ArrayList<>();
        Map<Object, Long> inputs = new LinkedHashMap<>(group.baseline.inputs);
        Set<Object> gases = new java.util.LinkedHashSet<>();
        if (nativePool == STELLAR_FORGE) inputs.keySet()
            .removeIf(key -> {
                if (!protectionGas(key)) return false;
                gases.add(key);
                return true;
            });
        for (GTRecipe recipe : nativePool.getAllRecipes()) {
            if (recipe.mDuration <= 0 || recipe.mEUt < 0) continue;
            RecipeData candidate;
            if (nativePool == COOLING) {
                if (recipe.mFluidInputs.length != 1 || recipe.mFluidOutputs.length != 1
                    || recipe.mInputs.length != 0
                    || recipe.mOutputs.length != 0) continue;
                candidate = new RecipeData(
                    Collections
                        .singletonMap(FluidId.create(recipe.mFluidInputs[0]), (long) recipe.mFluidInputs[0].amount),
                    Collections
                        .singletonMap(FluidId.create(recipe.mFluidOutputs[0]), (long) recipe.mFluidOutputs[0].amount),
                    Collections.emptyMap(),
                    Collections.singletonMap(new TimeKey(COOLING, 0), 1L));
            } else {
                if (recipe.mEUt == 0) continue;
                candidate = prepareSourceRecipe(nativePool, recipe);
            }
            if (!inputs.keySet()
                .equals(candidate.inputs.keySet())
                || !group.baseline.outputs.keySet()
                    .equals(candidate.outputs.keySet())
                || !group.baseline.catalysts.equals(candidate.catalysts)) continue;
            Object output = candidate.outputs.keySet()
                .iterator()
                .next();
            long original = group.baseline.outputs.get(output);
            long replacement = candidate.outputs.get(output);
            long gcd = getGCD(original, replacement);
            long numerator = original / gcd, denominator = replacement / gcd;
            if (!proportional(candidate.inputs, inputs, numerator, denominator, true)
                || !proportional(candidate.outputs, group.baseline.outputs, numerator, denominator, true)) continue;
            Map<Object, Long> omittedInputs = new LinkedHashMap<>();
            for (Object gas : gases) omittedInputs.put(
                gas,
                Math.min(
                    group.inputs.getOrDefault(gas, 0L),
                    (long) Math.floor(group.baseline.inputs.get(gas) * group.scale)));
            matches.add(
                new Version(
                    candidate.times,
                    group.scale * numerator / denominator,
                    Collections.unmodifiableMap(omittedInputs)));
        }
        return matches;
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

    public static long coolingMask() {
        return BottledChaosMachineConfiguration.mask(COOLING);
    }
}
