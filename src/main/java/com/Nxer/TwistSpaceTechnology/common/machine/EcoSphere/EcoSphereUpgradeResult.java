package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.DirectedMobClonerMode;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
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
            // Composite upgrades always retain their inherited base effect. Their special effect still follows the
            // mode mask below.
            if (!allowedForMode && type != EcoSphereUpgradeType.BLOOD_ORB
                && type != EcoSphereUpgradeType.PERFECT_GENETICS
                && type != EcoSphereUpgradeType.OUTPUT_PULVERIZATION) continue;
            upgradeCounts.merge(type, 1, Integer::sum);
            if (allowedForMode && type.getSpecialUpgrade() != null) {
                specialUpgrades.add(type.getSpecialUpgrade());
            }
        }
    }

    public long applyFluidDiscount(long amount) {
        // The speed upgrade is crafted around, and retains, one fluid-reduction upgrade.
        int upgrades = getCount(EcoSphereUpgradeType.FLUID_REDUCTION) + getCount(EcoSphereUpgradeType.SPEED);
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
        int capacityUpgrades = getCount(EcoSphereUpgradeType.CAPACITY) + getCount(EcoSphereUpgradeType.BLOOD_ORB);
        return Math.min(4, capacityUpgrades);
    }

    public boolean hasSpecialUpgrade(EcoSphereSpecialUpgrade upgrade) {
        return specialUpgrades.contains(upgrade);
    }

    private List<ItemStackLong> applyOutputScaling(List<ItemStackLong> outputs) {
        if (outputs.isEmpty()) return outputs;
        double outputMultiplier = getOutputMultiplier();
        List<ItemStackLong> scaledOutputs = new ArrayList<>(outputs.size());
        for (ItemStackLong entry : outputs) {
            ItemStack output = entry.itemStack();
            if (output == null) continue;
            ItemStack offspring = AquaticZoneSimulatorFakeRecipe.OFFSPRING;
            if (offspring != null && output.isItemEqual(offspring)) {
                EcoSphereModeSupport.addItemOutput(scaledOutputs, output, entry.stackSize());
                continue;
            }
            double outputScale = outputMultiplier * (0.85 + XSTR.XSTR_INSTANCE.nextDouble() * 0.15);
            EcoSphereModeSupport
                .addItemOutput(scaledOutputs, output, multiplySaturated(entry.stackSize(), outputScale));
        }
        return scaledOutputs;
    }

    private List<FluidStackLong> applyOutputScaling(List<FluidStackLong> outputs, int conversionDivisor) {
        if (outputs.isEmpty()) return outputs;
        double outputMultiplier = getOutputMultiplier();
        List<FluidStackLong> scaledOutputs = new ArrayList<>(outputs.size());
        for (FluidStackLong entry : outputs) {
            if (entry.fluidStack() == null) continue;
            double outputScale = outputMultiplier * (0.85 + XSTR.XSTR_INSTANCE.nextDouble() * 0.15);
            int groupDivisor = conversionDivisor > 1 && DirectedMobClonerMode.isLifeEssenceOutput(entry.fluidStack())
                && entry.amount() % conversionDivisor == 0 ? conversionDivisor : 1;
            long scaledAmount = multiplySaturated(entry.amount() / groupDivisor, outputScale);
            scaledAmount = Math.min(scaledAmount, Long.MAX_VALUE / groupDivisor) * groupDivisor;
            EcoSphereModeSupport.addFluidOutput(scaledOutputs, entry.fluidStack(), scaledAmount);
        }
        return scaledOutputs;
    }

    private int getCount(EcoSphereUpgradeType type) {
        return upgradeCounts.getOrDefault(type, 0);
    }

    private double getOutputMultiplier() {
        // Perfect genetics and output pulverization are crafted around, and retain, one output upgrade each.
        int upgrades = getCount(EcoSphereUpgradeType.OUTPUT) + getCount(EcoSphereUpgradeType.PERFECT_GENETICS)
            + getCount(EcoSphereUpgradeType.OUTPUT_PULVERIZATION);
        return Math.pow(1.5, upgrades);
    }

    private static long multiplySaturated(long amount, double multiplier) {
        double result = amount * multiplier;
        if (result >= Long.MAX_VALUE) return Long.MAX_VALUE;
        return Math.max(1, (long) result);
    }

}
