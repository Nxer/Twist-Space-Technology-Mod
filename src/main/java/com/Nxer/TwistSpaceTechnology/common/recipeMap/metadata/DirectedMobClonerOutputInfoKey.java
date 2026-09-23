package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class DirectedMobClonerOutputInfoKey extends RecipeMetadataKey<Boolean> {

    public static final DirectedMobClonerOutputInfoKey INSTANCE = new DirectedMobClonerOutputInfoKey();

    private DirectedMobClonerOutputInfoKey() {
        super(Boolean.class, "directed_mob_cloner_output_info");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        // #tr EcoSphereSimulator.nei.parallel
        // # Boosted by higher voltage
        // #zh_CN 并行随电压提高

        // #tr EcoSphereSimulator.nei.losslessOverclock
        // # Runs with perfect overclock
        // #zh_CN 执行无损超频
        recipeInfo.drawText(
            TextEnums.tr(
                cast(value, false) ? "EcoSphereSimulator.nei.losslessOverclock" : "EcoSphereSimulator.nei.parallel"));
    }
}
