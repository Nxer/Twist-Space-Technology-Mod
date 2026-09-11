package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.DirectedMobClonerMode;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;

import gregtech.api.objects.XSTR;

public final class EcoSphereUpgradeResult {

    public static final EcoSphereUpgradeResult EMPTY = new EcoSphereUpgradeResult(new ItemStack[0], -1);

    private final EnumMap<EcoSphereUpgradeType, Integer> upgradeCounts = new EnumMap<>(EcoSphereUpgradeType.class);
    private final EnumSet<EcoSphereSpecialUpgrade> specialUpgrades = EnumSet.noneOf(EcoSphereSpecialUpgrade.class);

    public EcoSphereUpgradeResult(ItemStack[] upgrades, int mode) {
        for (ItemStack stack : upgrades) {
            EcoSphereUpgradeType type = EcoSphereUpgradeType.fromStack(stack);
            if (type == null) continue;
            boolean allowedForMode = type.isAllowedForMode(mode);
            // The Blood Orb upgrade always provides its capacity effect, but only cloning enables its LP-network
            // effect.
            if (!allowedForMode && type != EcoSphereUpgradeType.BLOOD_ORB_NETWORK) continue;
            upgradeCounts.merge(type, 1, Integer::sum);
            if (allowedForMode && type.getSpecialUpgrade() != null) {
                specialUpgrades.add(type.getSpecialUpgrade());
            }
        }
    }

    public long applyFluidDiscount(long amount) {
        // The speed upgrade is crafted around, and retains, one fluid-efficiency upgrade.
        int upgrades = getCount(EcoSphereUpgradeType.FLUID_EFFICIENCY) + getCount(EcoSphereUpgradeType.SPEED);
        for (int i = 0; i < upgrades && amount > 1; i++) amount = Math.max(1, amount / 2);
        return amount;
    }

    public EcoSphereModeResult applyTo(EcoSphereModeResult result, boolean preconvertFluidOutputToLp) {
        if (result == null || !result.result()
            .wasSuccessful()) return result;
        // Apply shared output scaling only after mode-specific recipe generation has completed.
        int duration = result.duration();
        if (getCount(EcoSphereUpgradeType.SPEED) > 0) duration = 20;
        return new EcoSphereModeResult(
            result.result(),
            applyOutputScaling(result.outputs()),
            applyOutputScaling(
                result.fluidOutputs(),
                preconvertFluidOutputToLp ? DirectedMobClonerMode.LP_NETWORK_CONVERSION_DIVISOR : 1),
            result.eut(),
            duration);
    }

    public int getCapacityUpgrades() {
        int capacityUpgrades = getCount(EcoSphereUpgradeType.CAPACITY)
            + getCount(EcoSphereUpgradeType.BLOOD_ORB_NETWORK);
        return Math.min(4, capacityUpgrades);
    }

    public boolean hasSpecialUpgrade(EcoSphereSpecialUpgrade upgrade) {
        return specialUpgrades.contains(upgrade);
    }

    private ItemStack[] applyOutputScaling(ItemStack[] outputs) {
        if (outputs == null || outputs.length == 0) return outputs;
        double outputMultiplier = getOutputMultiplier();
        for (ItemStack output : outputs) {
            if (output == null) continue;
            double outputScale = outputMultiplier;
            ItemStack offspring = AquaticZoneSimulatorFakeRecipe.OFFSPRING;
            if (offspring == null || !output.isItemEqual(offspring)) {
                outputScale *= 0.85 + XSTR.XSTR_INSTANCE.nextDouble() * 0.15;
            }
            output.stackSize = multiplySaturated(output.stackSize, outputScale);
        }
        return outputs;
    }

    private FluidStack[] applyOutputScaling(FluidStack[] outputs, int conversionDivisor) {
        if (outputs == null || outputs.length == 0) return outputs;
        double outputMultiplier = getOutputMultiplier();
        List<FluidStack> scaledOutputs = new ArrayList<>(outputs.length);
        // Merge equal fluids so split stacks share one random roll and are scaled once as a long total.
        boolean[] consumed = new boolean[outputs.length];
        for (int i = 0; i < outputs.length; i++) {
            FluidStack template = outputs[i];
            if (template == null || consumed[i]) continue;
            long totalAmount = 0;
            for (int j = i; j < outputs.length; j++) {
                FluidStack candidate = outputs[j];
                if (candidate == null || consumed[j] || !template.isFluidEqual(candidate)) continue;
                totalAmount += candidate.amount;
                consumed[j] = true;
            }
            double outputScale = outputMultiplier * (0.85 + XSTR.XSTR_INSTANCE.nextDouble() * 0.15);
            // Scale recipe 0 in whole LP units so its physical remainder stays exactly convertible.
            int groupDivisor = conversionDivisor > 1 && DirectedMobClonerMode.isLifeEssenceOutput(template)
                && totalAmount % conversionDivisor == 0 ? conversionDivisor : 1;
            long scaledAmount = multiplySaturated(totalAmount / groupDivisor, outputScale);
            scaledAmount = Math.min(scaledAmount, Long.MAX_VALUE / groupDivisor) * groupDivisor;
            addSplitFluid(scaledOutputs, template, scaledAmount, groupDivisor);
        }
        return scaledOutputs.toArray(new FluidStack[0]);
    }

    private static void addSplitFluid(List<FluidStack> outputs, FluidStack template, long amount, int amountDivisor) {
        int maximumAmountPerStack = Integer.MAX_VALUE - Integer.MAX_VALUE % amountDivisor;
        while (amount > 0) {
            FluidStack split = template.copy();
            split.amount = (int) Math.min(maximumAmountPerStack, amount);
            outputs.add(split);
            amount -= split.amount;
        }
    }

    private int getCount(EcoSphereUpgradeType type) {
        return upgradeCounts.getOrDefault(type, 0);
    }

    private double getOutputMultiplier() {
        // Auto-pulverization is crafted around, and retains, one output-boost upgrade.
        int upgrades = getCount(EcoSphereUpgradeType.OUTPUT_BOOST)
            + getCount(EcoSphereUpgradeType.AUTO_PULVERIZE_EQUIPMENT);
        return Math.pow(1.5, upgrades);
    }

    private static long multiplySaturated(long amount, double multiplier) {
        double result = amount * multiplier;
        if (result >= Long.MAX_VALUE) return Long.MAX_VALUE;
        return Math.max(1, (long) result);
    }

    private static int multiplySaturated(int amount, double multiplier) {
        double result = amount * multiplier;
        if (result >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return Math.max(1, (int) result);
    }
}
