package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

/** Coarse authorization groups. One circuit unlocks every processing type assigned to its group. */
public enum BottledChaosRecipeCategory {

    GENERAL_PROCESSING(1, "bottled_chaos.recipe_category.general_processing"),
    CHEMICAL(2, "bottled_chaos.recipe_category.chemical"),
    ASSEMBLY(3, "bottled_chaos.recipe_category.assembly"),
    SMELTING_AND_HEAT_TREATMENT(4, "bottled_chaos.recipe_category.smelting_and_heat_treatment"),
    SPECIAL(5, "bottled_chaos.recipe_category.special");

    private final int circuitConfiguration;
    private final long mask;
    private final String key;

    BottledChaosRecipeCategory(int circuitConfiguration, String key) {
        this.circuitConfiguration = circuitConfiguration;
        this.mask = 1L << (circuitConfiguration - 1);
        this.key = key;
    }

    public int getCircuitConfiguration() {
        return circuitConfiguration;
    }

    public long getMask() {
        return mask;
    }

    public String getKey() {
        return key;
    }

    public static BottledChaosRecipeCategory fromCircuit(ItemStack stack) {
        if (!GTUtility.isAnyIntegratedCircuit(stack)) return null;
        int configuration = stack.getItemDamage();
        for (BottledChaosRecipeCategory category : values()) {
            if (category.circuitConfiguration == configuration) return category;
        }
        return null;
    }

    public static long maskOf(BottledChaosRecipeCategory... categories) {
        long result = 0;
        for (BottledChaosRecipeCategory category : categories) {
            if (category != null) result |= category.mask;
        }
        return result;
    }
}
