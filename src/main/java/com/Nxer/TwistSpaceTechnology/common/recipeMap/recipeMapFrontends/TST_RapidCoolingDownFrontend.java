package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.Arrays;
import java.util.List;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;

public class TST_RapidCoolingDownFrontend extends RecipeMapFrontend {

    public TST_RapidCoolingDownFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return Arrays.asList(new Pos2d(26, 25));
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return Arrays.asList(new Pos2d(128, 25));
    }

}
