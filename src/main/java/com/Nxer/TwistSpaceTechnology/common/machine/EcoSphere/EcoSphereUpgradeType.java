package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.init.TstItems;

public enum EcoSphereUpgradeType {

    FLUID_REDUCTION(0, 0b1111, null),
    CAPACITY(1, 0b1111, null),
    OUTPUT(2, 0b1111, null),
    SPEED(3, 0b1111, null),
    BLOOD_ORB(4, 0b1000, EcoSphereSpecialUpgrade.BLOOD_ORB),
    PERFECT_GENETICS(5, 0b0101, EcoSphereSpecialUpgrade.PERFECT_GENETICS),
    OUTPUT_PULVERIZATION(6, 0b1000, EcoSphereSpecialUpgrade.OUTPUT_PULVERIZATION);

    private final int metadata;
    // Bit n authorizes this upgrade's mode-specific effect in machine mode n.
    private final int allowedModes;
    private final EcoSphereSpecialUpgrade specialUpgrade;

    EcoSphereUpgradeType(int metadata, int allowedModes, EcoSphereSpecialUpgrade specialUpgrade) {
        this.metadata = metadata;
        this.allowedModes = allowedModes;
        this.specialUpgrade = specialUpgrade;
    }

    public int getMetadata() {
        return metadata;
    }

    public EcoSphereSpecialUpgrade getSpecialUpgrade() {
        return specialUpgrade;
    }

    public boolean isAllowedForMode(int mode) {
        return mode >= 0 && mode < 4 && (allowedModes & 1 << mode) != 0;
    }

    public boolean matches(ItemStack stack) {
        return stack != null && stack.getItem() == TstItems.EcoSphereUpgrade && stack.getItemDamage() == metadata;
    }

    public static EcoSphereUpgradeType fromStack(ItemStack stack) {
        for (EcoSphereUpgradeType type : values()) {
            if (type.matches(stack)) return type;
        }
        return null;
    }
}
