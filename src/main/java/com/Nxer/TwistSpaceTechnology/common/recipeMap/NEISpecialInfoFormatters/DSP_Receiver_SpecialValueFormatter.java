package com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.ArrayList;
import java.util.List;

import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class DSP_Receiver_SpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final DSP_Receiver_SpecialValueFormatter INSTANCE = new DSP_Receiver_SpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        List<String> msgs = new ArrayList<>();
        msgs.add(
            texter("Equivalence value of EU : ", "NEI.DSP_ReceiverRecipes.specialValue.pre")
                + recipeInfo.recipe.mSpecialValue);
        return msgs;
    }
}
