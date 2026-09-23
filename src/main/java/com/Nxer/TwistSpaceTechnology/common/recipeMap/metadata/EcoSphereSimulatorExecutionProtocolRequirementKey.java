package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class EcoSphereSimulatorExecutionProtocolRequirementKey extends RecipeMetadataKey<Integer> {

    public static final EcoSphereSimulatorExecutionProtocolRequirementKey INSTANCE = new EcoSphereSimulatorExecutionProtocolRequirementKey();

    private EcoSphereSimulatorExecutionProtocolRequirementKey() {
        super(Integer.class, "eco_sphere_simulator_execution_protocol_requirement");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        // #tr EcoSphereSimulator.nei.executionProtocolTier
        // # Required Execution Protocol Tier: %s
        // #zh_CN 所需执行协议等级: %s
        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted("EcoSphereSimulator.nei.executionProtocolTier", cast(value, 1)));
    }
}
