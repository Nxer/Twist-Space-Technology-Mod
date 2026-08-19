package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public final class EcoSphereModeSupport {

    private EcoSphereModeSupport() {}

    private static int calculateTierOneParallel(int tier) {
        return (int) Math
            .floor(2 * Math.pow(2, 0.1 * (tier - 1) * (8 + Math.log(25 + Math.exp(25 - tier)) / Math.log(5))));
    }

    public static long calculateEut(int tier) {
        // The machine numbers LV as tier 1, so n in 2 * 4^n * 15 / 16 is tier + 1.
        return (long) (2d * Math.pow(4, tier + 1) * 15 / 16);
    }

    public static long getParallelFromEUt(int tier) {
        return calculateTierOneParallel(tier);
    }

    public static long getPerfectOverclockParallelFromEUt(int tier) {
        return powerOfFour(tier - 1);
    }

    public static CheckRecipeResult missingFluid(TST_EcoSphereSimulator machine, Fluid requiredFluid, long amount) {
        if (requiredFluid == null) return CheckRecipeResultRegistry.INTERNAL_ERROR;
        if (getAvailableFluid(machine, requiredFluid) <= 0) return CheckRecipeResultRegistry.NO_RECIPE;
        return SimpleResultWithText
            .outOfFluid(new FluidStack(requiredFluid, (int) Math.min(Integer.MAX_VALUE, amount)));
    }

    public static EcoSphereModeResult processModeRecipeWithTier(TST_EcoSphereSimulator machine, Fluid requiredFluid,
        long baseFluidPerParallel, int powerTier, Function<ParallelResult, EcoSphereModeResult> processor) {
        long parallelFromEUt;
        if (machine.isTierTwo()) {
            parallelFromEUt = getPerfectOverclockParallelFromEUt(powerTier);
        } else {
            parallelFromEUt = getParallelFromEUt(powerTier);
        }
        return processRecipeWithParallelLimit(
            machine,
            requiredFluid,
            baseFluidPerParallel,
            powerTier,
            parallelFromEUt,
            processor);
    }

    public static EcoSphereModeResult processRecipeWithParallelLimit(TST_EcoSphereSimulator machine,
        Fluid requiredFluid, long baseFluidPerOperation, int powerTier, long parallelFromEUt,
        Function<ParallelResult, EcoSphereModeResult> processor) {
        // Apply fluid-efficiency upgrades before determining the fluid-limited parallel count.
        long fluidPerOperation = machine.applyFluidDiscount(baseFluidPerOperation);
        long parallel = consumeFluidForParallel(machine, requiredFluid, fluidPerOperation, parallelFromEUt);
        if (parallel <= 0) return EcoSphereModeResult.failure(missingFluid(machine, requiredFluid, fluidPerOperation));
        return processor.apply(new ParallelResult(powerTier, parallel));
    }

    private static long consumeFluidForParallel(TST_EcoSphereSimulator machine, Fluid requiredFluid,
        long fluidPerOperation, long parallelFromEUt) {
        if (requiredFluid == null || fluidPerOperation <= 0 || parallelFromEUt < 1) return 0;
        long availableFluid = getAvailableFluid(machine, requiredFluid);
        if (availableFluid <= 0 || !machine.prepareFluidAreaForConsumption(requiredFluid)) return 0;
        long parallelFromFluid = availableFluid / fluidPerOperation;
        if (parallelFromFluid < 1) return 0;
        long parallel = Math.min(parallelFromEUt, parallelFromFluid);
        long fluidCost;
        try {
            fluidCost = Math.multiplyExact(fluidPerOperation, parallel);
        } catch (ArithmeticException ignored) {
            return 0;
        }
        if (!drainFluid(machine, requiredFluid, fluidCost)) return 0;
        return parallel;
    }

    public static long powerOfFour(int exponent) {
        // 4^n equals 2^(2n); cap large exponents at 2^62 to avoid overflowing a signed long.
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

    private static long getAvailableFluid(TST_EcoSphereSimulator machine, Fluid requiredFluid) {
        long available = 0;
        for (FluidStack fluid : machine.getStoredFluids()) {
            if (fluid != null && fluid.getFluid() == requiredFluid) available += fluid.amount;
        }
        return available;
    }

    public static boolean drainFluid(TST_EcoSphereSimulator machine, Fluid requiredFluid, long amount) {
        List<FluidStack> matching = new ArrayList<>();
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
    public record ParallelResult(int tier, long parallel) {}
}
