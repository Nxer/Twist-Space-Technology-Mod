package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.PreparedPlan;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

/** Folds only the cached recipe chains, against the input snapshot of one processing group. */
public final class BottledChaosProcessingLogic extends GTCM_ProcessingLogic {

    private static final int MAX_BATCH_TICKS = 200;

    private long routeMask;
    private long pools;
    private long requiredPools;
    private long baseRecipeVoltage;
    private long maximumRecipeVoltage = Long.MAX_VALUE;
    private BottledChaosMaterialCache materials;
    private PreparedPlan preparedPlan;
    private Function<BottledChaosRecipeProcessor.Plan, CheckRecipeResult> outputCheck;
    private BottledChaosRecipeProcessor.MachineParameters machineParameters = new BottledChaosRecipeProcessor.MachineParameters() {};

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
        requiredPools = 0;
    }

    public long getRequiredPools() {
        return requiredPools;
    }

    public long getBaseRecipeVoltage() {
        return baseRecipeVoltage;
    }

    public List<BottledChaosUpkeep.Demand> getUpkeep() {
        return preparedPlan == null ? Collections.emptyList() : preparedPlan.upkeep();
    }

    public void setMachineConfiguration(long maximumVoltage, BottledChaosRecipeProcessor.MachineParameters parameters) {
        maximumRecipeVoltage = maximumVoltage;
        machineParameters = parameters;
    }

    @Override
    public @NotNull CheckRecipeResult process() {
        preparedPlan = null;
        if (routeMask == 0) {
            // #tr GT5U.gui.text.recipe_result.bottled_chaos.missing_recipe_token
            // # Missing a valid Bottled Chaos credential.
            // #zh_CN 缺少有效的瓶中混沌凭证.
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.missing_recipe_token");
        }
        if (!BottledChaosRecipeProcessor.isReady()) {
            // #tr GT5U.gui.text.recipe_result.bottled_chaos.recipe_cache_unavailable
            // # Bottled Chaos processing recipes are unavailable. Check the startup log.
            // #zh_CN 瓶中混沌处理配方不可用. 请检查启动日志.
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.recipe_cache_unavailable");
        }
        CheckRecipeResult failure = CheckRecipeResultRegistry.NO_RECIPE;
        // 1. Try the last successful family first. Failures may select another entry/family without any consumption.
        for (int entry : BottledChaosRecipeProcessor.candidateEntries(routeMask, materials)) {
            var planner = BottledChaosRecipeProcessor
                .executionPlanner(routeMask, entry, inputItems, inputFluids, machineParameters, materials);
            PreparedPlan baseline = planner.prepare(1);
            if (baseline == null) continue;
            CheckRecipeResult result = validatePlan(baseline);
            if (!result.wasSuccessful()) {
                failure = result;
                continue;
            }
            // 2. First spend the 10-second work allowance on downstream batches, then on additional external feed.
            double maximumWork = Math.min(
                Double.MAX_VALUE,
                MAX_BATCH_TICKS * BottledChaosOverclock.maximumSpeed(
                    baseline.baseEUt() * euModifier,
                    availableVoltage,
                    overClockTimeReduction,
                    overClockPowerIncrease) / speedBoost);
            PreparedPlan fallback = planner.fitDownstream(baseline, maximumWork);
            PreparedPlan chosen = fallback;
            int upper = batchSize > 1 && baseline.durationTicks() <= maximumWork
                ? baseline.externalBatchLimit()
                : 1;
            long lower = 2;
            int chosenBatches = 1;
            while (lower <= upper) {
                int middle = (int) (lower + (upper - lower) / 2);
                PreparedPlan candidate = planner.prepare(middle);
                if (candidate != null && candidate.durationTicks() <= maximumWork
                    && validatePlan(candidate).wasSuccessful()) {
                    chosen = candidate;
                    chosenBatches = middle;
                    lower = (long) middle + 1;
                } else upper = middle - 1;
            }
            chosen = planner.balanceCarry(chosen, chosenBatches, maximumWork);
            result = validatePlan(chosen);
            if (!result.wasSuccessful()) {
                failure = result;
                continue;
            }
            // 3. Reserve outputs before changing even the caller's input copies; a smaller plan may still fit.
            result = outputCheck.apply(chosen.outputPlan());
            if (!result.wasSuccessful() && chosen != fallback) {
                chosen = fallback;
                result = outputCheck.apply(chosen.outputPlan());
            }
            if (!result.wasSuccessful()) {
                failure = result;
                continue;
            }
            BottledChaosOverclock.Result clock = overclock(chosen);
            result = validateClock(clock);
            if (!result.wasSuccessful()) {
                failure = result;
                continue;
            }
            chosen.consumeInputCopies(inputItems, inputFluids);
            preparedPlan = chosen;
            baseRecipeVoltage = chosen.baseEUt();
            requiredPools = chosen.requiredPools();
            calculatedEut = clock.eut();
            duration = Math.max(1, clock.duration());
            BottledChaosRecipeProcessor.Plan outputs = chosen.outputPlan();
            calculatedParallels = outputs.executions();
            setLongOutputs(outputs.itemOutputs(), outputs.fluidOutputs());
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        if (failure == CheckRecipeResultRegistry.NO_RECIPE
            && machineParameters instanceof BottledChaosMachineConfiguration.RuntimeParameters runtime
            && runtime.rejectedSource()) {
            // #tr GT5U.gui.text.recipe_result.bottled_chaos.source_machine_limit
            // # The configured source machine cannot satisfy the recipe requirements.
            // #zh_CN 已配置的来源机器无法满足配方要求.
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.source_machine_limit");
        }
        return failure;
    }

    private CheckRecipeResult validatePlan(PreparedPlan plan) {
        baseRecipeVoltage = plan.baseEUt();
        requiredPools = plan.requiredPools();
        if (baseRecipeVoltage > maximumRecipeVoltage) {
            // #tr GT5U.gui.text.recipe_result.bottled_chaos.recipe_voltage_limit
            // # Recipe voltage exceeds the structure upgrade limit.
            // #zh_CN 配方电压超过结构升级上限.
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.recipe_voltage_limit");
        }
        if ((pools & requiredPools) != requiredPools) return missingPools(requiredPools, pools);
        double power = baseRecipeVoltage * euModifier;
        if (!Double.isFinite(power) || power <= 0 || power >= Long.MAX_VALUE)
            return CheckRecipeResultRegistry.POWER_OVERFLOW;
        double time = plan.durationTicks() * speedBoost;
        if (!Double.isFinite(time) || time <= 0) return CheckRecipeResultRegistry.DURATION_OVERFLOW;
        if (Math.ceil(power) > availableVoltage)
            return CheckRecipeResultRegistry.insufficientPower((long) Math.ceil(power));
        return CheckRecipeResultRegistry.SUCCESSFUL;
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
