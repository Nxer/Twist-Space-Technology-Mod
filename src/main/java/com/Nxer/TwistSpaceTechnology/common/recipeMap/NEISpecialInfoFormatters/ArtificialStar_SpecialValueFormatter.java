package com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.ArrayList;
import java.util.List;

import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class ArtificialStar_SpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final ArtificialStar_SpecialValueFormatter INSTANCE = new ArtificialStar_SpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        List<String> msgs = new ArrayList<>();
        msgs.add(
            texter("Generate : ", "NEI.ArtificialStarGeneratingRecipes.specialValue.pre")
                + recipeInfo.recipe.mSpecialValue
                + " × 2,147,483,647 EU");
        return msgs;
    }
}
