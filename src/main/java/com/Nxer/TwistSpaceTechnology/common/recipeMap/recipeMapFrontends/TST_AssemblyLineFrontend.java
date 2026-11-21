package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.AssemblyLineFrontend;
import gregtech.nei.RecipeDisplayInfo;

public final class TST_AssemblyLineFrontend extends AssemblyLineFrontend {

    public TST_AssemblyLineFrontend(BasicUIPropertiesBuilder ui, NEIRecipePropertiesBuilder nei) {
        super(ui, nei);
    }

    @Override
    protected void drawRecipeOwnerInfo(RecipeDisplayInfo recipeInfo) {
        TST_RecipeMapFrontend.drawOwners(recipeInfo);
    }
}
