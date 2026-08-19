package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import java.util.EnumMap;
import java.util.EnumSet;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;

import gregtech.api.objects.XSTR;

public final class EcoSphereUpgradeResult {

    public static final EcoSphereUpgradeResult EMPTY = new EcoSphereUpgradeResult(new ItemStack[0], -1);

    private final EnumMap<EcoSphereUpgradeType, Integer> upgradeCounts = new EnumMap<>(EcoSphereUpgradeType.class);
    private final EnumSet<EcoSphereSpecialUpgrade> specialUpgrades = EnumSet.noneOf(EcoSphereSpecialUpgrade.class);

    public EcoSphereUpgradeResult(ItemStack[] upgrades, int mode) {
        for (ItemStack stack : upgrades) {
            EcoSphereUpgradeType type = EcoSphereUpgradeType.fromStack(stack);
            if (type == null || !type.isAllowedForMode(mode)) continue;
            upgradeCounts.merge(type, 1, Integer::sum);
            if (type.getSpecialUpgrade() != null) specialUpgrades.add(type.getSpecialUpgrade());
        }
    }

    public long applyFluidDiscount(long amount) {
        int upgrades = getCount(EcoSphereUpgradeType.FLUID_EFFICIENCY);
        for (int i = 0; i < upgrades && amount > 1; i++) amount = Math.max(1, amount / 2);
        return amount;
    }

    public EcoSphereModeResult applyTo(EcoSphereModeResult result) {
        if (result == null || !result.result()
            .wasSuccessful()) return result;
        // Apply shared output scaling only after mode-specific recipe generation has completed.
        int duration = result.duration();
        if (getCount(EcoSphereUpgradeType.SPEED) > 0) duration = 20;
        return new EcoSphereModeResult(
            result.result(),
            applyOutputScaling(result.outputs()),
            applyOutputScaling(result.fluidOutputs()),
            result.eut(),
            duration);
    }

    public int getCapacityUpgrades() {
        return Math.min(4, getCount(EcoSphereUpgradeType.CAPACITY));
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

    private FluidStack[] applyOutputScaling(FluidStack[] outputs) {
        if (outputs == null || outputs.length == 0) return outputs;
        double outputMultiplier = getOutputMultiplier();
        for (FluidStack output : outputs) {
            if (output == null) continue;
            double outputScale = outputMultiplier * (0.85 + XSTR.XSTR_INSTANCE.nextDouble() * 0.15);
            output.amount = multiplySaturated(output.amount, outputScale);
        }
        return outputs;
    }

    private int getCount(EcoSphereUpgradeType type) {
        return upgradeCounts.getOrDefault(type, 0);
    }

    private double getOutputMultiplier() {
        return Math.pow(1.5, getCount(EcoSphereUpgradeType.OUTPUT_BOOST));
    }

    private static int multiplySaturated(int amount, double multiplier) {
        double result = amount * multiplier;
        if (result >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return Math.max(1, (int) result);
    }
}
