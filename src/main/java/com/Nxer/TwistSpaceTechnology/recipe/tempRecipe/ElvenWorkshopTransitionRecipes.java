package com.Nxer.TwistSpaceTechnology.recipe.tempRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import cpw.mods.fml.common.registry.GameRegistry;

public class ElvenWorkshopTransitionRecipes {

    public static void loadRecipes() {
        // TODO: Remove these transitional recipes together with the legacy controller in the next version.
        GameRegistry.addShapelessRecipe(GTCMItemList.ElvenWorkshopLegacy.get(1), GTCMItemList.ElvenWorkshop.get(1));
        GameRegistry.addShapelessRecipe(GTCMItemList.ElvenWorkshop.get(1), GTCMItemList.ElvenWorkshopLegacy.get(1));
    }
}
