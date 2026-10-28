package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.takeAmount;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosSourceParameters;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosOverclock;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.check.CheckRecipeResult;

/** Startup-partitioned native sources, their material alternatives and normalized execution cost. */
final class BottledChaosSourceWork {

    static double normalizedDuration(TimeKey key, long rawTicks, BottledChaosSourceParameters.Evaluation source,
        long recipeVoltage) {
        if (key.eut() == 0) return 0;
        var profile = source.profile();
        if (source.energyOnly()) return rawTicks * (double) key.eut()
            * profile.euMultiplier()
            / source.quantitySpeedMultiplier()
            / recipeVoltage;
        double sourceSpeed = 1 / profile.timeMultiplier();
        double sourceDuration = Math.max(profile.minimumSourceDuration(), key.recipeTicks() / sourceSpeed);
        return BottledChaosOverclock.calculateNormalizedDuration(
            rawTicks,
            key.eut(),
            Math.min(recipeVoltage, profile.maximumSourcePower()),
            profile.parallel(),
            key.recipeTicks() / sourceDuration * source.quantitySpeedMultiplier(),
            profile.euMultiplier(),
            profile.timeDivisor(),
            profile.powerMultiplier(),
            source.maximumOverclocks(),
            source.perfectOverclocks(),
            source.overclockLimitAlternatives());
    }

    @Desugar
    private record SelectedSource(TimeKey time, BottledChaosSourceParameters.Evaluation parameters) {}

    @Desugar
    record Selection(double duration, long pools, List<SelectedSource> sources, Map<Object, Long> omittedInputs,
        Map<Object, Long> inputs, CheckRecipeResult failure) {

        Selection rejected(CheckRecipeResult failure) {
            return new Selection(duration, pools, sources, omittedInputs, inputs, failure);
        }

        void recordUsage(BottledChaosSourceParameters parameters) {
            for (SelectedSource source : sources) parameters.recordSourceUsage(source.time.map(), source.parameters);
        }
    }

    private final RecipeData data;
    private final Map<TimeKey, Long> ordinary = new LinkedHashMap<>();
    private final Map<BottledChaosProcessAlternatives.Group, Map<TimeKey, Long>> groups = new LinkedHashMap<>();

    BottledChaosSourceWork(RecipeData data) {
        this.data = data;
        for (var entry : data.times.entrySet()) {
            var group = entry.getKey()
                .alternatives();
            if (group == null) ordinary.put(entry.getKey(), entry.getValue());
            else groups.computeIfAbsent(group, ignored -> new LinkedHashMap<>())
                .put(entry.getKey(), entry.getValue());
        }
    }

    boolean hasAlternatives() {
        return !groups.isEmpty();
    }

    /** Select a cached native-pool version once per plan/voltage; unrelated pool work remains mandatory. */
    Selection select(BottledChaosSourceParameters parameters, long voltage) {
        Selection result = evaluateSources(ordinary, parameters, voltage, 1);
        if (result.failure != null) return result;
        for (var segment : groups.entrySet()) {
            var group = segment.getKey();
            boolean installed = segment.getValue()
                .keySet()
                .stream()
                .allMatch(key -> parameters.isSourceAvailable(key.map()));
            // Installing Miracle Door selects its native material contract as well as its timing.
            // Its gas-free inputs stay the same when the chain's common voltage rises.
            boolean stellarForge = parameters.isSourceAvailable(BottledChaosProcessAlternatives.STELLAR_FORGE)
                && group.versions.containsKey(BottledChaosProcessAlternatives.STELLAR_FORGE);
            Selection best = installed && !stellarForge ? evaluateSources(segment.getValue(), parameters, voltage, 1)
                : null;
            for (var pool : group.versions.entrySet()) {
                if (stellarForge && pool.getKey() != BottledChaosProcessAlternatives.STELLAR_FORGE) continue;
                if (!parameters.isSourceAvailable(pool.getKey())) continue;
                for (var version : pool.getValue()) {
                    Selection candidate = evaluateSources(version.times(), parameters, voltage, version.scale());
                    if (best != null
                        && (candidate.failure != null || best.failure == null && candidate.duration >= best.duration))
                        continue;
                    candidate.omittedInputs.putAll(version.omittedInputs());
                    best = candidate;
                }
            }
            // Preserve the ordinary pool failures when no installed native version can run.
            if (best == null && !installed && !stellarForge)
                best = evaluateSources(segment.getValue(), parameters, voltage, 1);
            if (best.failure != null) return best;
            result.sources.addAll(best.sources);
            for (var omitted : best.omittedInputs.entrySet())
                result.omittedInputs.merge(omitted.getKey(), omitted.getValue(), Math::addExact);
            result = new Selection(
                BottledChaosRecipeSupport.addDuration(result.duration, best.duration, 1),
                result.pools | best.pools,
                result.sources,
                result.omittedInputs,
                null,
                null);
        }
        // A fully substituted gas demand disappears, including any source tick rounding difference.
        for (var omitted : result.omittedInputs.entrySet()) {
            boolean stillConsumed = false;
            for (SelectedSource source : result.sources)
                if (source.time.source() != null && source.time.source().inputs.containsKey(omitted.getKey())) {
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
        return new Selection(result.duration, result.pools, result.sources, result.omittedInputs, inputs, null);
    }

    private static Selection evaluateSources(Map<TimeKey, Long> work, BottledChaosSourceParameters parameters,
        long voltage, double scale) {
        double duration = 0;
        long pools = 0;
        List<SelectedSource> sources = new ArrayList<>(work.size());
        for (var entry : work.entrySet()) {
            TimeKey key = entry.getKey();
            var source = parameters.evaluateSource(key.map(), voltage, key.requirements());
            if (!source.successful())
                return new Selection(0, 0, sources, new LinkedHashMap<>(), null, source.failure());
            duration = BottledChaosRecipeSupport
                .addDuration(duration, normalizedDuration(key, entry.getValue(), source, voltage) * scale, 1);
            pools |= BottledChaosMachineConfiguration.mask(key.map());
            sources.add(new SelectedSource(key, source));
        }
        return new Selection(
            duration,
            BottledChaosMachineConfiguration.withDependencies(pools),
            sources,
            new LinkedHashMap<>(),
            null,
            null);
    }

}
