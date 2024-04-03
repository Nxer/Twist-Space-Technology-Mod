package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;

public class TST_IndustrialMagicMatrixFrontend extends RecipeMapFrontend {

    private static final int xDirMaxCount = 5;
    private static final int yOrigin = 8;
    private final int itemRowCount;

    public TST_IndustrialMagicMatrixFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
        this.itemRowCount = getItemRowCount();
        neiProperties.recipeBackgroundSize = new Size(170, 10 + (itemRowCount * 18));
    }

    @Override
    public void addProgressBar(ModularWindow.@NotNull Builder builder, @NotNull Supplier<Float> progressSupplier,
        @NotNull Pos2d windowOffset) {
        super.addProgressBar(builder, progressSupplier, new Pos2d(15, 10));
    }

    private int getItemRowCount() {
        return (Math.max(uiProperties.maxItemInputs, uiProperties.maxItemOutputs) - 1) / xDirMaxCount + 1;
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 6, yOrigin, xDirMaxCount);
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 125, 45, xDirMaxCount);
    }

    @Override
    public @NotNull Pos2d getSpecialItemPosition() {
        return new Pos2d(125, 75);
    }
}
