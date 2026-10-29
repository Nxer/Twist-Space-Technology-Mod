package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.util.Collections;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosUpkeep;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;

/** Source-machine hooks use the final serial power without rebuilding material routes. */
public interface BottledChaosSourceParameters {

    BottledChaosSourceParameters DEFAULT = new BottledChaosSourceParameters() {};

    default boolean isSourceAvailable(RecipeMap<?> pool) {
        return true;
    }

    /** Complete result for one source/voltage/requirements key; arrays and profiles are read-only. */
    @Desugar
    record Evaluation(BottledChaosMachineParameters.Profile profile, double quantitySpeedMultiplier, boolean energyOnly,
        int maximumOverclocks, int perfectOverclocks, int[] overclockLimitAlternatives, CheckRecipeResult failure,
        int installation, boolean ingotMold) {

        public Evaluation(BottledChaosMachineParameters.Profile profile, double quantitySpeedMultiplier,
            boolean energyOnly, int maximumOverclocks, int perfectOverclocks, int[] overclockLimitAlternatives,
            CheckRecipeResult failure) {
            this(
                profile,
                quantitySpeedMultiplier,
                energyOnly,
                maximumOverclocks,
                perfectOverclocks,
                overclockLimitAlternatives,
                failure,
                -1,
                false);
        }

        private static final Evaluation NORMAL = new Evaluation(
            BottledChaosMachineParameters.defaultProfile(false),
            1,
            false,
            -1,
            0,
            null,
            null);
        private static final Evaluation PERFECT = new Evaluation(
            BottledChaosMachineParameters.defaultProfile(true),
            1,
            false,
            -1,
            0,
            null,
            null);

        static Evaluation defaults(RecipeMap<?> pool, double quantitySpeed) {
            Evaluation base = BottledChaosMachineConfiguration.isPurificationPool(pool) ? PERFECT : NORMAL;
            return quantitySpeed == 1 ? base : new Evaluation(base.profile, quantitySpeed, false, -1, 0, null, null);
        }

        public static Evaluation rejected(CheckRecipeResult failure) {
            return new Evaluation(null, 1, false, -1, 0, null, failure);
        }

        public boolean successful() {
            return failure == null;
        }
    }

    default Evaluation evaluateSource(RecipeMap<?> pool, long recipeVoltage,
        BottledChaosSourceRequirements requirements) {
        return Evaluation.defaults(pool, 1);
    }

    default void resetSourceUsage() {}

    default List<Evaluation> evaluateCandidates(RecipeMap<?> pool, long recipeVoltage,
        BottledChaosSourceRequirements requirements) {
        return Collections.singletonList(evaluateSource(pool, recipeVoltage, requirements));
    }

    default void recordSourceUsage(RecipeMap<?> map, Evaluation source) {}

    default List<BottledChaosUpkeep.Demand> upkeepDemands() {
        return Collections.emptyList();
    }

    default List<BottledChaosUpkeep.ItemDemand> roundItemDemands() {
        return Collections.emptyList();
    }

}
