package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public final class EcoSphereModeSupport {

    private EcoSphereModeSupport() {}

    public static int getTierMultiplier(int tier) {
        return (int) Math
            .floor(3 * Math.pow(2, 0.1 * (tier - 1) * (8 + Math.log(25 + Math.exp(25 - tier)) / Math.log(5))));
    }

    public static long calculateEut(int tier) {
        return (long) (8d * Math.pow(4, tier) * 15 / 16);
    }

    public static CheckRecipeResult missingFluid(Fluid requiredFluid, long amount) {
        if (requiredFluid == null) return CheckRecipeResultRegistry.INTERNAL_ERROR;
        return SimpleResultWithText
            .outOfFluid(new FluidStack(requiredFluid, (int) Math.min(Integer.MAX_VALUE, amount)));
    }

    public static ParallelResult consumeFluidForParallel(TST_MegaTreeFarm machine, Fluid requiredFluid,
        long fluidPerParallel, int powerTier) {
        if (requiredFluid == null || fluidPerParallel <= 0 || powerTier < 1) return null;
        if (!machine.prepareFluidAreaForConsumption(requiredFluid)) return null;
        long availableFluid = getAvailableFluid(machine, requiredFluid);
        long fluidParallel = availableFluid / fluidPerParallel;
        if (fluidParallel < 2) return null;
        int fluidTier = 63 - Long.numberOfLeadingZeros(fluidParallel);
        int effectiveTier = Math.min(powerTier, fluidTier);
        long parallel = 1L << Math.min(effectiveTier, 62);
        long fluidCost;
        try {
            fluidCost = Math.multiplyExact(fluidPerParallel, parallel);
        } catch (ArithmeticException ignored) {
            return null;
        }
        if (!drainFluid(machine, requiredFluid, fluidCost)) return null;
        return new ParallelResult(effectiveTier, parallel, getTierMultiplier(effectiveTier), fluidCost);
    }

    public static PerfectOverclockResult consumeFluidForPerfectOverclock(TST_MegaTreeFarm machine, Fluid requiredFluid,
        long fluidPerOperation, int maximumOverclocks) {
        if (requiredFluid == null || fluidPerOperation <= 0 || maximumOverclocks < 0) return null;
        if (!machine.prepareFluidAreaForConsumption(requiredFluid)) return null;
        long availableFluid = getAvailableFluid(machine, requiredFluid);
        long fluidOperations = availableFluid / fluidPerOperation;
        if (fluidOperations < 1) return null;
        int fluidOverclocks = 0;
        long fluidMultiplier = 1;
        while (fluidOverclocks < maximumOverclocks && fluidMultiplier <= fluidOperations / 4) {
            fluidMultiplier *= 4;
            fluidOverclocks++;
        }
        int overclocks = Math.min(maximumOverclocks, fluidOverclocks);
        long multiplier = powerOfFour(overclocks);
        long fluidCost;
        try {
            fluidCost = Math.multiplyExact(fluidPerOperation, multiplier);
        } catch (ArithmeticException ignored) {
            return null;
        }
        if (!drainFluid(machine, requiredFluid, fluidCost)) return null;
        return new PerfectOverclockResult(overclocks, multiplier, fluidCost);
    }

    public static long powerOfFour(int exponent) {
        if (exponent <= 0) return 1;
        if (exponent >= 31) return 1L << 62;
        return 1L << (exponent * 2);
    }

    public static void addSplitStack(List<ItemStack> outputs, ItemStack template, long amount) {
        while (amount > Integer.MAX_VALUE) {
            ItemStack split = template.copy();
            split.stackSize = Integer.MAX_VALUE;
            outputs.add(split);
            amount -= Integer.MAX_VALUE;
        }
        if (amount <= 0) return;
        ItemStack split = template.copy();
        split.stackSize = (int) amount;
        outputs.add(split);
    }

    private static long getAvailableFluid(TST_MegaTreeFarm machine, Fluid requiredFluid) {
        long available = 0;
        for (FluidStack fluid : machine.getStoredFluids()) {
            if (fluid != null && fluid.getFluid() == requiredFluid) available += fluid.amount;
        }
        return available;
    }

    public static boolean drainFluid(TST_MegaTreeFarm machine, Fluid requiredFluid, long amount) {
        List<FluidStack> matching = new java.util.ArrayList<>();
        long available = 0;
        for (FluidStack fluid : machine.getStoredFluids()) {
            if (fluid != null && fluid.getFluid() == requiredFluid) {
                matching.add(fluid);
                available += fluid.amount;
            }
        }
        return available >= amount && drainFluidStacks(matching, amount);
    }

    public static boolean drainFluidStacks(List<FluidStack> fluids, long amount) {
        for (FluidStack fluid : fluids) {
            int drained = (int) Math.min(amount, fluid.amount);
            fluid.amount -= drained;
            amount -= drained;
            if (amount == 0) return true;
        }
        return amount == 0;
    }

    public static String getItemStackString(ItemStack stack) {
        return Item.itemRegistry.getNameForObject(stack.getItem()) + ":" + stack.getItemDamage();
    }

    @Desugar
    public record PerfectOverclockResult(int tier, long multiplier, long fluidCost) {}

    @Desugar
    public record ParallelResult(int tier, long parallel, double multiplier, long fluidCost) {}
}
