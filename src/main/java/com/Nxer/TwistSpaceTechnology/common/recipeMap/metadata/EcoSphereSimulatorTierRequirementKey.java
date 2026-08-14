package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class EcoSphereSimulatorTierRequirementKey extends RecipeMetadataKey<Integer> {

    public static final EcoSphereSimulatorTierRequirementKey INSTANCE = new EcoSphereSimulatorTierRequirementKey();

    private EcoSphereSimulatorTierRequirementKey() {
        super(Integer.class, "eco_sphere_simulator_tier_requirement");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        // #tr EcoSphereSimulator.nei.structureTier
        // # Structure Tier: %s
        // #zh_CN 结构等级: %s
        recipeInfo.drawText(StatCollector.translateToLocalFormatted("EcoSphereSimulator.nei.structureTier", tier));
    }
}
