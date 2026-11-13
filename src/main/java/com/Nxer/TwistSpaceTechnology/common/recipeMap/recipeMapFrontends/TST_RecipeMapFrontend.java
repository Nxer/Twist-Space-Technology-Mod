package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import cpw.mods.fml.common.ModContainer;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.RecipeDisplayInfo;

/** Use TST's short name in recipe-owner information while retaining GT's single-line layout. */
public class TST_RecipeMapFrontend extends RecipeMapFrontend {

    public TST_RecipeMapFrontend(BasicUIPropertiesBuilder ui, NEIRecipePropertiesBuilder nei) {
        super(ui, nei);
    }

    @Override
    protected void drawRecipeOwnerInfo(RecipeDisplayInfo recipeInfo) {
        drawOwners(recipeInfo);
    }

    static void drawOwners(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.recipe.owners == null) return;
        for (int i = 0; i < recipeInfo.recipe.owners.size(); i++) {
            String key = recipeInfo.recipe.owners.size() == 1 ? "GT5U.recipes.recipe_by"
                : i == 0 ? "GT5U.recipes.recipe_by.original" : "GT5U.recipes.recipe_by.modified";
            ModContainer owner = recipeInfo.recipe.owners.get(i);
            String name = TwistSpaceTechnology.MODID.equals(owner.getModId()) ? "TST" : owner.getName();
            recipeInfo.drawText(EnumChatFormatting.ITALIC + StatCollector.translateToLocalFormatted(key, name));
        }
    }
}
