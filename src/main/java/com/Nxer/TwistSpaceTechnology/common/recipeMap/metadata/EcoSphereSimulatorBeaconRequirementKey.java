package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class EcoSphereSimulatorBeaconRequirementKey extends RecipeMetadataKey<Integer> {

    public static final EcoSphereSimulatorBeaconRequirementKey INSTANCE = new EcoSphereSimulatorBeaconRequirementKey();

    private EcoSphereSimulatorBeaconRequirementKey() {
        super(Integer.class, "eco_sphere_simulator_beacon_requirement");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        // #tr EcoSphereSimulator.nei.beaconTier
        // # Beacon Tier: %s
        // #zh_CN 信标等级: %s
        recipeInfo
            .drawText(StatCollector.translateToLocalFormatted("EcoSphereSimulator.nei.beaconTier", cast(value, 1)));
    }
}
