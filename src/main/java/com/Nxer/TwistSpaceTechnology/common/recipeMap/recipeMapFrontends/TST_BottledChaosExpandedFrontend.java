package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.common.gui.modularui.UIHelper;

/** Separate nine-column format for all-candidate equivalents that exceed the existing four-by-four layout. */
public final class TST_BottledChaosExpandedFrontend extends TST_BottledChaosFrontend {

    public TST_BottledChaosExpandedFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder.progressBarPos(new Pos2d(89, 194)), neiPropertiesBuilder, new Pos2d(151, 194));
    }

    @Override
    protected @NotNull NEIRecipePropertiesBuilder modifyNEIProperties(NEIRecipePropertiesBuilder builder) {
        return builder.recipeBackgroundSize(new Size(170, 300));
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int count) {
        return UIHelper.getGridPositions(count, 6, 8, 9);
    }

    @Override
    public @NotNull List<Pos2d> getFluidInputPositions(int count) {
        return UIHelper.getGridPositions(count, 6, 152, 9);
    }

    @Override
    public @NotNull Pos2d getSpecialItemPosition() {
        return new Pos2d(61, 192);
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int count) {
        return UIHelper.getGridPositions(count, 6, 220, 9);
    }

    @Override
    public @NotNull List<Pos2d> getFluidOutputPositions(int count) {
        return UIHelper.getGridPositions(count, 6, 256, 9);
    }
}
