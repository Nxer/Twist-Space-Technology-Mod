package com.Nxer.TwistSpaceTechnology.recipe.tempRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;

import cpw.mods.fml.common.registry.GameRegistry;

public class StarThatNeverWasTransitionRecipes {

    public static void loadRecipes() {
        if (!Config.Enable_LaserMeteorMiner) return;

        // TODO: Remove these transitional recipes together with the legacy controller in the next version.
        GameRegistry
            .addShapelessRecipe(GTCMItemList.StarThatNeverWasLegacy.get(1), GTCMItemList.StarThatNeverWas.get(1));
        GameRegistry
            .addShapelessRecipe(GTCMItemList.StarThatNeverWas.get(1), GTCMItemList.StarThatNeverWasLegacy.get(1));
    }
}
