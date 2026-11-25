package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.MathHelper;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTRecipe;

public class TST_IndustrialMagicMatrixFrontend extends TST_EssentiaFrontend {

    private static final int CENTER_X = 83;
    private static final int ITEMS_PER_RING = 12;
    private static final int MAX_RING_COMPONENTS = 30;

    public TST_IndustrialMagicMatrixFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected List<Pos2d> getInputPositions(int itemInputCount) {
        List<Pos2d> positions = new ArrayList<>();
        int components = Math.max(0, itemInputCount - 1);
        for (int i = 0; i < itemInputCount; i++) {
            positions.add(inputPosition(i, components));
        }
        return positions;
    }

    @Override
    protected List<Pos2d> getOutputPositions(int itemOutputCount) {
        return singletonList(new Pos2d(CENTER_X - 8, 5));
    }

    @Override
    public void drawNEIBackground(GTRecipe recipe) {
        RESULT.draw(68.775f, -1.05f, 28, 29.75f);
        MATRIX.draw(46.375f, getCenterY(recipe) - 36, 73.5f, 73.5f);
    }

    public static int getCenterY(GTRecipe recipe) {
        return centerY(Math.max(0, recipe.mInputs.length - 1));
    }

    @Override
    protected int getAspectsY(GTRecipe recipe) {
        int components = Math.max(0, recipe.mInputs.length - 1);
        int overflowRows = (Math.max(0, components - MAX_RING_COMPONENTS) + 7) / 8;
        return centerY(components) + ringRadius(components) + 19 + overflowRows * 20;
    }

    private static Pos2d inputPosition(int index, int components) {
        int centerY = centerY(components);
        if (index == 0) return new Pos2d(CENTER_X - 8, centerY - 8);
        if (index > MAX_RING_COMPONENTS) {
            int overflow = index - MAX_RING_COMPONENTS - 1;
            return new Pos2d(5 + overflow % 8 * 20, centerY + ringRadius(components) + 19 + overflow / 8 * 20);
        }
        boolean nativeRing = components <= 16;
        int ring = nativeRing || index <= ITEMS_PER_RING ? 0 : 1;
        int count = nativeRing ? components
            : ring == 0 ? ITEMS_PER_RING : Math.min(MAX_RING_COMPONENTS, components) - ITEMS_PER_RING;
        int ringIndex = index - 1 - ring * ITEMS_PER_RING;
        float angle = -90.0F;
        for (int i = 0; i < ringIndex; i++) {
            angle += 360.0F / count;
        }
        float radians = (float) (angle / 180.0F * Math.PI);
        int radius = nativeRing ? 40 : 44 + ring * 24;
        return new Pos2d(
            CENTER_X - 8 + (int) (MathHelper.cos(radians) * radius),
            centerY - 8 + (int) (MathHelper.sin(radians) * radius));
    }

    private static int centerY(int components) {
        return 71 + ringRadius(components) - 40;
    }

    private static int ringRadius(int components) {
        return components <= 16 ? 40 : 68;
    }

}
