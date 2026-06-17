package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import org.joml.Math;

public class TST_IndustrialMagicMatrixFrontend extends RecipeMapFrontend {

    private static final int xDirMaxCount = 5;
    private static final int yOrigin = 8;
    private final int itemRowCount;

    public TST_IndustrialMagicMatrixFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
        this.itemRowCount = getItemRowCount();
        // neiProperties.recipeBackgroundSize = new Size(170, 10 + (itemRowCount * 18));
    }

    @Override
    protected @NotNull NEIRecipePropertiesBuilder modifyNEIProperties(NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        int itemRowCount = getItemRowCount();
        return neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 10 + (itemRowCount) * 18));
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, GTNEIDefaultHandler.NEITemplateContext ctx) {
        // new Pos2d(15, 10);
        assert uiProperties.progressBarTexture != null;
        builder.widget(
            new ProgressBar().setTexture(uiProperties.progressBarTexture.get(), 20)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(ctx.progressSupplier)
                .setSynced(false, false)
                .setPos(new Pos2d(78+15, 24+10).add(ctx.windowOffset))
                .setSize(uiProperties.progressBarSize));
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
