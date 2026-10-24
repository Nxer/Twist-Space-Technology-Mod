package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosSourceParameters;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosMaterialCache;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result.BottledChaosMissingCategoriesResult;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.PreparedPlan;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

/** Folds only the cached recipe chains, against the input snapshot of one processing group. */
public final class BottledChaosProcessingLogic extends GTCM_ProcessingLogic {

    private static final int MAX_BATCH_TICKS = 200;

    private final BottledChaosRecipeProcessor.PlanningCache planningCache = new BottledChaosRecipeProcessor.PlanningCache();
    private long routeMask;
    private long pools;
    private long maximumRecipeVoltage = Long.MAX_VALUE;
    private BottledChaosMaterialCache materials;
    private PreparedPlan preparedPlan;
    private CheckRecipeResult rejectedRoute = CheckRecipeResultRegistry.NO_RECIPE;
    private final Map<Long, Double> durationLimits = new HashMap<>();
    private Function<BottledChaosRecipeProcessor.Plan, CheckRecipeResult> outputCheck;
    private BottledChaosSourceParameters machineParameters = BottledChaosSourceParameters.DEFAULT;

    public void setMaterialCache(BottledChaosMaterialCache materials,
        Function<BottledChaosRecipeProcessor.Plan, CheckRecipeResult> outputCheck) {
        this.materials = materials;
        this.outputCheck = outputCheck;
    }

    public PreparedPlan getPreparedPlan() {
        return preparedPlan;
    }

    public void setAuthorization(ItemStack credential, long poolMask) {
        routeMask = ItemBottledChaosCredential.isBound(credential) ? ItemBottledChaosCredential.getRouteMask(credential)
            : 0;
        pools = poolMask;
    }

    public long getRequiredPools() {
        return preparedPlan.requiredPools();
    }

    public long getBaseRecipeVoltage() {
        return preparedPlan.baseEUt();
    }

    public List<BottledChaosUpkeep.Demand> getUpkeep() {
        return preparedPlan == null ? Collections.emptyList() : preparedPlan.upkeep();
    }

    public void setMachineConfiguration(long maximumVoltage, BottledChaosSourceParameters parameters) {
        maximumRecipeVoltage = maximumVoltage;
        machineParameters = parameters;
    }

    @Override
    public @NotNull CheckRecipeResult process() {
        long diagnosticStart = System.nanoTime();
        preparedPlan = null;
        rejectedRoute = CheckRecipeResultRegistry.NO_RECIPE;
        durationLimits.clear();
        if (routeMask == 0) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.unbound");
        }
        if (!BottledChaosRecipeProcessor.isReady()) {
            // #tr tst.bottledchaos.result.recipe_cache_unavailable
            // # Bottled Chaos processing recipes are unavailable. Check the startup log.
            // #zh_CN 瓶中混沌处理配方不可用. 请检查启动日志.
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.recipe_cache_unavailable");
        }
        CheckRecipeResult failure = CheckRecipeResultRegistry.NO_RECIPE;
        var planner = BottledChaosRecipeProcessor
            .executionPlanner(routeMask, inputItems, inputFluids, machineParameters, materials, planningCache);
        planner.setLimits((voltage, requiredPools) -> {
            CheckRecipeResult result = validateLimits(voltage, requiredPools);
            if (!result.wasSuccessful()) rejectedRoute = result;
            return result.wasSuccessful();
        }, this::batchDurationLimit);
        long diagnosticSetup = System.nanoTime();
        // 1. Try the last successful family first. Failures may select another entry/family without any consumption.
        for (int entry : BottledChaosRecipeProcessor.candidateEntries(routeMask, materials)) {
            for (int attempt = 0; attempt < 2; attempt++) {
                long diagnosticPrepareStart = System.nanoTime();
                PreparedPlan baseline = attempt == 0 ? planner.prepareEntry(entry) : planner.retryEntry(entry);
                long diagnosticPrepared = System.nanoTime();
                if (baseline == null) break;
                CheckRecipeResult result = validatePlan(baseline);
                if (!result.wasSuccessful()) {
                    failure = result;
                    break;
                }
                // 2. Extend selected goals from present stock once, then estimate/halve complete-line targets.
                PreparedPlan fallback = baseline;
                Predicate<PreparedPlan> fitsWork = candidate -> validatePlan(candidate).wasSuccessful()
                    && candidate.durationTicks() <= batchDurationLimit(candidate);
                PreparedPlan chosen = planner.fitDownstream(baseline, batchDurationLimit(baseline), fitsWork);
                if (chosen.durationTicks()
                    <= batchDurationLimit(chosen) * BottledChaosOverclock.MIN_OVERCLOCK_TICKS / MAX_BATCH_TICKS) {
                    chosen = planner.refineShortPlan(chosen, fitsWork);
                }
                long diagnosticFitted = System.nanoTime();
                result = validatePlan(chosen);
                if (!result.wasSuccessful()) {
                    failure = result;
                    break;
                }
                // 3. Reserve outputs before changing even the caller's input copies; a smaller plan may still fit.
                PreparedPlan mainPlan = chosen;
                PreparedPlan recovered = planner.recoverAvailable(chosen, batchDurationLimit(chosen));
                if (fitsWork.test(recovered)) chosen = recovered;
                long diagnosticRecovered = System.nanoTime();
                result = prepareAndCheckOutputs(planner, chosen);
                if (!result.wasSuccessful() && chosen != mainPlan) {
                    chosen = mainPlan;
                    result = prepareAndCheckOutputs(planner, chosen);
                }
                if (!result.wasSuccessful() && chosen != fallback) {
                    // Output blockage only shrinks the selected targets; do not repeat the growth estimate.
                    chosen = planner.reduceDownstream(
                        fallback,
                        chosen,
                        candidate -> fitsWork.test(candidate)
                            && prepareAndCheckOutputs(planner, candidate).wasSuccessful());
                    result = prepareAndCheckOutputs(planner, chosen);
                }
                if (!result.wasSuccessful() && chosen != fallback) {
                    chosen = fallback;
                    result = prepareAndCheckOutputs(planner, chosen);
                }
                if (!result.wasSuccessful()) {
                    if (attempt == 0 && result == CheckRecipeResultRegistry.NO_RECIPE) continue;
                    failure = result;
                    break;
                }
                long diagnosticChecked = System.nanoTime();
                BottledChaosOverclock.Result clock = overclock(chosen);
                result = validateClock(clock);
                if (!result.wasSuccessful()) {
                    failure = result;
                    break;
                }
                chosen.consumeInputCopies(inputItems, inputFluids);
                preparedPlan = chosen;
                calculatedEut = clock.eut();
                duration = Math.max(1, clock.duration());
                BottledChaosRecipeProcessor.Plan outputs = chosen.outputPlan();
                calculatedParallels = outputs.executions();
                setLongOutputs(outputs.itemOutputs(), outputs.fluidOutputs());
                long diagnosticEnd = System.nanoTime();
                logRound(
                    entry,
                    planner,
                    diagnosticStart,
                    diagnosticSetup,
                    diagnosticPrepareStart,
                    diagnosticPrepared,
                    diagnosticFitted,
                    diagnosticRecovered,
                    diagnosticChecked,
                    diagnosticEnd);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }
        if (failure == CheckRecipeResultRegistry.NO_RECIPE && rejectedRoute != CheckRecipeResultRegistry.NO_RECIPE)
            return rejectedRoute;
        if (failure == CheckRecipeResultRegistry.NO_RECIPE) return machineParameters.sourceFailure();
        return failure;
    }

    /** Formatting stays outside planning; all timestamps are sampled at the original stage boundaries. */
    private void logRound(int entry, BottledChaosRecipeProcessor.ExecutionPlanner planner, long diagnosticStart,
        long diagnosticSetup, long diagnosticPrepareStart, long diagnosticPrepared, long diagnosticFitted,
        long diagnosticRecovered, long diagnosticChecked, long diagnosticEnd) {
        com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG.info(
            "BC_ROUND entry=" + entry
                + " total="
                + (diagnosticEnd - diagnosticStart)
                + " setup="
                + (diagnosticSetup - diagnosticStart)
                + " prepare="
                + (diagnosticPrepared - diagnosticPrepareStart)
                + " fit="
                + (diagnosticFitted - diagnosticPrepared)
                + " recover="
                + (diagnosticRecovered - diagnosticFitted)
                + " output="
                + (diagnosticChecked - diagnosticRecovered)
                + " finish="
                + (diagnosticEnd - diagnosticChecked)
                + " probes="
                + planner.diagnosticProbes
                + " probeNs="
                + planner.diagnosticProbeNs
                + " targetPasses="
                + planner.diagnosticTargetPasses
                + " targetExtensions="
                + planner.diagnosticTargetExtensions
                + " targetPassNs="
                + planner.diagnosticTargetPassNs
                + " shortProbes="
                + planner.diagnosticShortProbes
                + " shortExtensions="
                + planner.diagnosticShortExtensions
                + " shortBefore="
                + planner.diagnosticShortBefore
                + " shortAfter="
                + planner.diagnosticShortAfter
                + " sources="
                + planner.diagnosticSourceCalls
                + " sourceNs="
                + planner.diagnosticSourceNs
                + " inputNs="
                + planner.diagnosticInputNs
                + " matchNs="
                + planner.diagnosticMatchNs
                + " ticks="
                + duration
                + " eut="
                + calculatedEut);
    }

    private CheckRecipeResult prepareAndCheckOutputs(BottledChaosRecipeProcessor.ExecutionPlanner planner,
        PreparedPlan plan) {
        if (planner.completePlan(plan) == null) return CheckRecipeResultRegistry.NO_RECIPE;
        BottledChaosRecipeProcessor.Plan outputs;
        try {
            plan.prepareOutputs();
            outputs = plan.outputPlan();
        } catch (ArithmeticException overflow) {
            // #tr tst.bottledchaos.result.output_overflow
            // # Output quantity exceeds the limit.
            // #zh_CN 输出数量超过上限.
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.output_overflow");
        }
        return outputCheck.apply(outputs);
    }

    private CheckRecipeResult validatePlan(PreparedPlan plan) {
        long baseRecipeVoltage = plan.baseEUt();
        long requiredPools = plan.requiredPools();
        CheckRecipeResult limits = validateLimits(baseRecipeVoltage, requiredPools);
        if (!limits.wasSuccessful()) return limits;
        double power = baseRecipeVoltage * euModifier;
        if (!Double.isFinite(power) || power <= 0 || power >= Long.MAX_VALUE)
            return CheckRecipeResultRegistry.POWER_OVERFLOW;
        double time = plan.durationTicks() * speedBoost;
        if (!Double.isFinite(time) || time <= 0) return CheckRecipeResultRegistry.DURATION_OVERFLOW;
        if (Math.ceil(power) > availableVoltage)
            return CheckRecipeResultRegistry.insufficientPower((long) Math.ceil(power));
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private CheckRecipeResult validateLimits(long baseRecipeVoltage, long requiredPools) {
        if (baseRecipeVoltage > maximumRecipeVoltage) {
            // #tr tst.bottledchaos.result.recipe_voltage_limit
            // # Recipe voltage exceeds the structure upgrade limit.
            // #zh_CN 配方电压超过结构升级上限.
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.recipe_voltage_limit");
        }
        if ((pools & requiredPools) != requiredPools) return missingPools(requiredPools, pools);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private double batchDurationLimit(PreparedPlan plan) {
        return batchDurationLimit(plan.baseEUt());
    }

    private double batchDurationLimit(long voltage) {
        return durationLimits.computeIfAbsent(
            voltage,
            key -> Math.min(
                Double.MAX_VALUE,
                MAX_BATCH_TICKS * BottledChaosOverclock
                    .maximumSpeed(key * euModifier, availableVoltage, overClockTimeReduction, overClockPowerIncrease)
                    / speedBoost));
    }

    private BottledChaosOverclock.Result overclock(PreparedPlan plan) {
        return BottledChaosOverclock.calculate(
            plan.baseEUt() * euModifier,
            plan.durationTicks() * speedBoost,
            availableVoltage,
            overClockTimeReduction,
            overClockPowerIncrease);
    }

    private CheckRecipeResult validateClock(BottledChaosOverclock.Result clock) {
        if (clock.eut() == Long.MAX_VALUE) return CheckRecipeResultRegistry.POWER_OVERFLOW;
        if (clock.eut() > availableVoltage) return CheckRecipeResultRegistry.insufficientPower(clock.eut());
        if (clock.duration() == Integer.MAX_VALUE) return CheckRecipeResultRegistry.DURATION_OVERFLOW;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public static CheckRecipeResult missingPools(long required, long available) {
        return new BottledChaosMissingCategoriesResult(required & ~available);
    }
}
