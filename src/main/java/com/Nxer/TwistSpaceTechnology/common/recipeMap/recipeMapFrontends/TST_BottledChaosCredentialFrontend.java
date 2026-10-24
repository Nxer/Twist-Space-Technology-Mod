package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.nei.RecipeDisplayInfo;

/** The credential examples are guidance for the host's injection page, not machine recipes. */
public final class TST_BottledChaosCredentialFrontend extends TST_GeneralFrontend {

    public TST_BottledChaosCredentialFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.progressBarPos(new Pos2d(112, 17)),
            neiPropertiesBuilder.unificateOutput(false),
            new Pos2d(151, 45));
    }

    @Override
    protected @NotNull NEIRecipePropertiesBuilder modifyNEIProperties(NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        return neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 64));
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        List<Pos2d> positions = new ArrayList<>(itemInputCount);
        if (itemInputCount <= 0) return positions;
        positions.add(new Pos2d(6, 17));
        for (int index = 1; index < itemInputCount; index++) {
            int sourceIndex = index - 1;
            positions.add(new Pos2d(30 + sourceIndex % 4 * 18, 8 + sourceIndex / 4 * 18));
        }
        return positions;
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        List<Pos2d> positions = new ArrayList<>(itemOutputCount);
        for (int index = 0; index < itemOutputCount; index++) positions.add(new Pos2d(140, 17 + index * 18));
        return positions;
    }

    @Override
    public void drawDescription(@NotNull RecipeDisplayInfo recipeInfo) {}
}
