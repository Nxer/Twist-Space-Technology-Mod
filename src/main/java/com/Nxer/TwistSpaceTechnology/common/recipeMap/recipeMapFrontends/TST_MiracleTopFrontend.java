package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;

public class TST_MiracleTopFrontend extends RecipeMapFrontend {

    private static final int SLOT_COLUMNS = 4;
    private static final int INPUT_Y = 8;
    private static final int OUTPUT_Y = 134;

    public TST_MiracleTopFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.logoPos(new Pos2d(79, 7)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 154)));
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 6, INPUT_Y, SLOT_COLUMNS);
    }

    @Override
    public @NotNull List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 98, INPUT_Y, SLOT_COLUMNS);
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 6, OUTPUT_Y, SLOT_COLUMNS);
    }

    @Override
    public @NotNull List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 98, OUTPUT_Y, SLOT_COLUMNS);
    }
}
