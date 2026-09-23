package com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters;

import java.util.ArrayList;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class DSP_Receiver_SpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final DSP_Receiver_SpecialValueFormatter INSTANCE = new DSP_Receiver_SpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        List<String> msgs = new ArrayList<>();
        msgs.add(
            // #tr NEI.DSP_ReceiverRecipes.specialValue.pre
            // # Equivalence value of EU :
            // #zh_CN 等效于EU :
            TSTUtils.tr("NEI.DSP_ReceiverRecipes.specialValue.pre") + recipeInfo.recipe.mSpecialValue);
        return msgs;
    }
}
