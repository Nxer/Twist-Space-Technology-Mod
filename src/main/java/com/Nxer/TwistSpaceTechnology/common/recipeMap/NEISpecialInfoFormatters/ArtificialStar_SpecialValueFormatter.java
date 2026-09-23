package com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters;

import java.util.ArrayList;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class ArtificialStar_SpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final ArtificialStar_SpecialValueFormatter INSTANCE = new ArtificialStar_SpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        List<String> msgs = new ArrayList<>();
        msgs.add(
            // #tr tst.dyson.recipe.ArtificialStarGeneratingRecipeMap.special_value_prefix
            // # Generate :
            // #zh_CN 产生 :
            TSTUtils.tr("tst.dyson.recipe.ArtificialStarGeneratingRecipeMap.special_value_prefix")
                + recipeInfo.recipe.mSpecialValue
                + " × 2,147,483,647 EU");
        return msgs;
    }
}
