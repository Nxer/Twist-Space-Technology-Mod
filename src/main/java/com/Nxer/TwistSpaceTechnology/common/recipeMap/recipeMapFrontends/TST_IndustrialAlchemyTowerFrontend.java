package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTRecipe;

public class TST_IndustrialAlchemyTowerFrontend extends TST_EssentiaFrontend {

    private static final UITexture CRUCIBLE = UITexture.partly(OVERLAY, 256, 256, 2, 20, 54, 67);
    private static final UITexture INPUT_ARROW = UITexture.partly(OVERLAY, 256, 256, 100, 84, 110, 97);

    public TST_IndustrialAlchemyTowerFrontend(BasicUIPropertiesBuilder ui, NEIRecipePropertiesBuilder nei) {
        super(ui, nei);
    }

    @Override
    protected List<Pos2d> getInputPositions(int count) {
        List<Pos2d> positions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            positions.add(new Pos2d(i == 0 ? 54 : 110 + (i - 1) * 18, 27));
        }
        return positions;
    }

    @Override
    protected List<Pos2d> getOutputPositions(int count) {
        return singletonList(new Pos2d(75, 5));
    }

    @Override
    protected int getAspectsPerRow() {
        return 3;
    }

    @Override
    protected int getAspectsY(GTRecipe recipe) {
        int rows = (getAspects(recipe).size() + 2) / 3;
        return 86 - Math.min(rows, 3) * 10;
    }

    @Override
    public int getContentHeight(GTRecipe recipe) {
        return Math.max(132, super.getContentHeight(recipe));
    }

    @Override
    public void drawNEIBackground(GTRecipe recipe) {
        RESULT.draw(68.775f, -1.05f, 28, 29.75f);
        CRUCIBLE.draw(36.75f, 45.5f, 91, 82.25f);
        INPUT_ARROW.draw(70, 31.5f, 17.5f, 22.75f);
    }
}
