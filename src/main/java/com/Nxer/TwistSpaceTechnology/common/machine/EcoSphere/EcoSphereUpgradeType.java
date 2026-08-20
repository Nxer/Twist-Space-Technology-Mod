package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.init.TstItems;

import lombok.Getter;

public enum EcoSphereUpgradeType {

    FLUID_EFFICIENCY(8, 0b1111, null),
    OUTPUT_BOOST(9, 0b1111, null),
    SPEED(10, 0b1111, null),
    CAPACITY(11, 0b1110, null),
    AUTO_PULVERIZE_EQUIPMENT(12, 0b1000, EcoSphereSpecialUpgrade.AUTO_PULVERIZE_EQUIPMENT);

    @Getter
    private final int metadata;
    private final int allowedModes;
    @Getter
    private final EcoSphereSpecialUpgrade specialUpgrade;

    EcoSphereUpgradeType(int metadata, int allowedModes, EcoSphereSpecialUpgrade specialUpgrade) {
        this.metadata = metadata;
        this.allowedModes = allowedModes;
        this.specialUpgrade = specialUpgrade;
    }

    public boolean isAllowedForMode(int mode) {
        return mode >= 0 && mode < 4 && (allowedModes & 1 << mode) != 0;
    }

    public boolean matches(ItemStack stack) {
        return stack != null && stack.getItem() == TstItems.EcoSphereModeBeacon && stack.getItemDamage() == metadata;
    }

    public static EcoSphereUpgradeType fromStack(ItemStack stack) {
        for (EcoSphereUpgradeType type : values()) {
            if (type.matches(stack)) return type;
        }
        return null;
    }
}
