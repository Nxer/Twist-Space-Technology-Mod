package com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters;

import java.util.ArrayList;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class ArtificialStar_SpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final ArtificialStar_SpecialValueFormatter INSTANCE = new ArtificialStar_SpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        List<String> msgs = new ArrayList<>();
        msgs.add(
            // #tr NEI.ArtificialStarGeneratingRecipes.specialValue.pre
            // # Generate :
            // #zh_CN 产生 :
            TextEnums.tr("NEI.ArtificialStarGeneratingRecipes.specialValue.pre") + recipeInfo.recipe.mSpecialValue
                + " × 2,147,483,647 EU");
        return msgs;
    }
}
