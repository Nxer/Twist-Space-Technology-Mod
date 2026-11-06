package com.Nxer.TwistSpaceTechnology.recipe.tempRecipe;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class SoulFragmentRecipes {

    public static void loadRecipes() {
        // TODO: Temporary recipe because soul fragment crafting is broken in the current version. Remove once fixed.
        if (Mods.ExtraUtilities.isModLoaded() && Mods.DraconicEvolution.isModLoaded() && Mods.EnderIO.isModLoaded()) {
            GTModHandler.addCraftingRecipe(
                getModItem(Mods.ExtraUtilities.ID, "mini-soul", 1, 0),
                new Object[] { "SHV", 'S', getModItem(Mods.ExtraUtilities.ID, "lawSword", 1, 0), 'H',
                    getModItem(Mods.DraconicEvolution.ID, "dragonHeart", 1, 0), 'V',
                    getModItem(Mods.EnderIO.ID, "itemSoulVessel", 1, 0) });
        }
    }
}
