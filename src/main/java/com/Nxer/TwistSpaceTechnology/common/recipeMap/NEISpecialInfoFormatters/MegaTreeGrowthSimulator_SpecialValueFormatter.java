package com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters;

import java.util.ArrayList;
import java.util.List;

import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class MegaTreeGrowthSimulator_SpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final MegaTreeGrowthSimulator_SpecialValueFormatter INSTANCE = new MegaTreeGrowthSimulator_SpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        List<String> msgs = new ArrayList<>();
        msgs.add("Output is further boosted");
        msgs.add("by machine energy tier");
        return msgs;
    }
}
