package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import org.jetbrains.annotations.NotNull;

public class TST_StellarForgeFrontend extends RecipeMapFrontend {

    public TST_StellarForgeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
                                               NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public @NotNull Pos2d getSpecialItemPosition() {
        return new Pos2d(79, 44);
    }
}
