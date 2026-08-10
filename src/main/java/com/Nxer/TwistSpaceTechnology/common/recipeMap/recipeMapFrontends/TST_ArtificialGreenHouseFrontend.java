package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public final class TST_ArtificialGreenHouseFrontend extends RecipeMapFrontend {

    private static final int SLOT_SIZE = 18;
    private static final int INPUT_X = 45;
    private static final int ITEM_INPUT_Y = 27;
    private static final int FLUID_INPUT_Y = ITEM_INPUT_Y + SLOT_SIZE;
    private static final int PROGRESS_X = 72;
    private static final int PROGRESS_Y = 36;
    private static final int OUTPUT_X = 98;
    private static final int OUTPUT_Y = 18;

    public TST_ArtificialGreenHouseFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.addNEITransferRect(new Rectangle(PROGRESS_X, PROGRESS_Y, 20, SLOT_SIZE))
                .progressBarPos(new Pos2d(PROGRESS_X, PROGRESS_Y)),
            neiPropertiesBuilder.neiSpecialInfoFormatter(OutputBoostFormatter.INSTANCE));
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawDurationInfo(recipeInfo);
        drawMetadataInfo(recipeInfo);
        drawSpecialInfo(recipeInfo);
        drawRecipeOwnerInfo(recipeInfo);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, INPUT_X, ITEM_INPUT_Y, 1);
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, OUTPUT_X, OUTPUT_Y, 3);
    }

    @Override
    public @NotNull List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, INPUT_X, FLUID_INPUT_Y, 1);
    }

    private static final class OutputBoostFormatter implements INEISpecialInfoFormatter {

        private static final OutputBoostFormatter INSTANCE = new OutputBoostFormatter();

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            return Arrays.asList(
                TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.1"),
                TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.2"));
        }
    }
}
