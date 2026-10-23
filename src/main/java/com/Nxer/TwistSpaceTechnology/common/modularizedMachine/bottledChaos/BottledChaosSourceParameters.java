package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.Collections;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

/** Source-machine hooks use the final serial power without rebuilding material routes. */
public interface BottledChaosSourceParameters {

    BottledChaosSourceParameters DEFAULT = new BottledChaosSourceParameters() {};

    default CheckRecipeResult sourceFailure() {
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    default boolean isSourceAvailable(RecipeMap<?> pool) {
        return true;
    }

    default boolean energyOnly(RecipeMap<?> pool) {
        return false;
    }

    default void resetSourceUsage() {}

    default void recordSourceUsage(RecipeMap<?> map) {}

    default List<BottledChaosUpkeep.Demand> upkeepDemands() {
        return Collections.emptyList();
    }

    default List<BottledChaosUpkeep.ItemDemand> roundItemDemands() {
        return Collections.emptyList();
    }

    default void configureSource(RecipeMap<?> map, long recipeVoltage, BottledChaosSourceRequirements requirements) {}

    default long maximumSourcePower(RecipeMap<?> map) {
        return Long.MAX_VALUE;
    }

    default boolean canProcess(RecipeMap<?> map, long recipeVoltage, BottledChaosSourceRequirements requirements) {
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

    default int maximumOverclocks(RecipeMap<?> map, int requiredCasingTier) {
        return maximumOverclocks(map);
    }

    /** Optional equiprobable native limits; ordinary machines use the single limit without allocation. */
    default int[] overclockLimitAlternatives(RecipeMap<?> map) {
        return null;
    }

    default int perfectOverclocks(RecipeMap<?> map, int requiredHeat, int requiredCasingTier) {
        return 0;
    }

    default OverclockType overclock(RecipeMap<?> map) {
        return BottledChaosMachineConfiguration.isPurificationPool(map) ? OverclockType.PerfectOverclock
            : OverclockType.NormalOverclock;
    }
}
