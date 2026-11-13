package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTRecipe;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;

public class TST_SkypiercerTowerFrontend extends TST_EssentiaFrontend {

    public TST_SkypiercerTowerFrontend(BasicUIPropertiesBuilder ui, NEIRecipePropertiesBuilder nei) {
        super(ui, nei);
    }

    @Override
    protected List<Pos2d> getInputPositions(int count) {
        List<Pos2d> positions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            positions.add(new Pos2d(28 + i * 44, 66));
        }
        return positions;
    }

    @Override
    protected List<Pos2d> getOutputPositions(int count) {
        return singletonList(new Pos2d(128, 66));
    }

    @Override
    protected int getAspectsY(GTRecipe recipe) {
        return 100;
    }

    @Override
    public void drawNEIBackground(GTRecipe recipe) {
        GuiDraw.drawStringC(TSTUtils.tr("tst.machine.SkypiercerTower.mode.1"), 83, 5, 0x404040, false);
        GuiDraw.drawStringC("+", 58, 38, 0x404040, false);
        GuiDraw.drawStringC("=", 108, 38, 0x404040, false);
        for (int i = 0; i < recipe.mInputs.length; i++) {
            drawAspect(recipe.mInputs[i], 24 + i * 44);
        }
        if (recipe.mOutputs.length > 0) drawAspect(recipe.mOutputs[0], 124);
    }

    private static void drawAspect(ItemStack crystal, int x) {
        Aspect aspect = ItemEssentiaHelper.readAspectFromCrystal(crystal);
        if (aspect == null) return;
        GL11.glPushMatrix();
        GL11.glTranslatef(x, 30, 0);
        GL11.glScalef(1.5f, 1.5f, 1);
        UtilsFX.drawTag(0, 0, aspect);
        GL11.glPopMatrix();
    }
}
