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
        if (cast(value, false)) {
            // #tr DirectedMobCloner.nei.perfectOverclock
            // # Executes with perfect overclock
            // #zh_CN 执行无损超频
            recipeInfo.drawText(TextEnums.tr("DirectedMobCloner.nei.perfectOverclock"));
            return;
        }
        recipeInfo.drawText(TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.1"));
        recipeInfo.drawText(TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.2"));
    }
}
