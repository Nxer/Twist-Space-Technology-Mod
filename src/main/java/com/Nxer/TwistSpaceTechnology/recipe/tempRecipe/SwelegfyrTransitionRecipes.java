package com.Nxer.TwistSpaceTechnology.recipe.tempRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;

import cpw.mods.fml.common.registry.GameRegistry;

public class SwelegfyrTransitionRecipes {

    public static void loadRecipes() {
        if (!Config.Enable_SwelegfyrBlastFurnace) return;

        // TODO: Remove these transitional recipes together with the legacy controller in the next version.
        GameRegistry.addShapelessRecipe(
            GTCMItemList.SwelegfyrBlastFurnaceLegacy.get(1),
            GTCMItemList.SwelegfyrBlastFurnace.get(1));
        GameRegistry.addShapelessRecipe(
            GTCMItemList.SwelegfyrBlastFurnace.get(1),
            GTCMItemList.SwelegfyrBlastFurnaceLegacy.get(1));
    }
}
