package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.RecipeDisplayInfo;

/** Keep full recipe-owner names within TST's NEI pages, using GT's captured owner information. */
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
            String text = EnumChatFormatting.ITALIC + StatCollector.translateToLocalFormatted(
                key,
                recipeInfo.recipe.owners.get(i)
                    .getName());
            recipeInfo
                .drawTextMultipleLines(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, 160));
        }
    }
}
