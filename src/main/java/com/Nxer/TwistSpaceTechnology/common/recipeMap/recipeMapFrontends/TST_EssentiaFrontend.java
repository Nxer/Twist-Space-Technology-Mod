package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.nei.GTNEIDefaultHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/** Shared NEI presentation; the real item inputs remain available to NEE processing-pattern transfer. */
public abstract class TST_EssentiaFrontend extends TST_RecipeMapFrontend {

    protected static final ResourceLocation OVERLAY = new ResourceLocation(
        "thaumcraft",
        "textures/gui/gui_researchbook_overlay.png");
    protected static final UITexture RESULT = UITexture.partly(OVERLAY, 256, 256, 20, 3, 36, 20);
    protected static final UITexture MATRIX = UITexture.partly(OVERLAY, 256, 256, 207, 78, 249, 120);

    protected TST_EssentiaFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected NEIRecipePropertiesBuilder modifyNEIProperties(NEIRecipePropertiesBuilder builder) {
        return builder.recipeBackgroundSize(new Size(170, 150));
    }

    @Override
    public ModularWindow.Builder createNEITemplate(GTNEIDefaultHandler.NEITemplateContext ctx) {
        ModularWindow.Builder builder = ModularWindow.builder(neiProperties.recipeBackgroundSize);
        List<Pos2d> inputs = getItemInputPositions(uiProperties.maxItemInputs);
        for (int i = 0; i < inputs.size(); i++) {
            builder.widget(
                SlotWidget.phantom(ctx.itemInputsInventory, i)
                    .setBackground(IDrawable.EMPTY)
                    .setPos(
                        inputs.get(i)
                            .add(ctx.windowOffset))
                    .setSize(18, 18));
        }
        List<Pos2d> outputs = getItemOutputPositions(uiProperties.maxItemOutputs);
        for (int i = 0; i < outputs.size(); i++) {
            builder.widget(
                SlotWidget.phantom(ctx.itemOutputsInventory, i)
                    .setBackground(IDrawable.EMPTY)
                    .setPos(
                        outputs.get(i)
                            .add(ctx.windowOffset))
                    .setSize(18, 18));
        }
        // A special essence phial carries recipe data; its aspects are displayed individually below.
        return builder;
    }

    @Override
    public List<Pos2d> getItemInputPositions(int count) {
        return getInputPositions(count).stream()
            .map(pos -> pos.add(4, 10))
            .collect(Collectors.toList());
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int count) {
        return getOutputPositions(count).stream()
            .map(pos -> pos.add(4, 10))
            .collect(Collectors.toList());
    }

    protected abstract List<Pos2d> getInputPositions(int count);

    protected abstract List<Pos2d> getOutputPositions(int count);

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        positionStacks(recipe.mInputs, getInputPositions(recipe.mRecipe.mInputs.length));
        positionStacks(recipe.mOutputs, getOutputPositions(recipe.mRecipe.mOutputs.length));
        AspectList aspects = getAspects(recipe.mRecipe);
        Aspect[] sorted = aspects.getAspectsSortedAmount();
        int rows = (sorted.length + getAspectsPerRow() - 1) / getAspectsPerRow();
        int index = 0;
        for (int row = 0; row < rows; row++) {
            int columns = (sorted.length + rows - row - 1) / rows;
            for (int column = 0; column < columns; column++) {
                Aspect aspect = sorted[index++];
                ItemStack stack = ItemEssentiaHelper.createAspectItem(aspect, aspects.getAmount(aspect));
                if (stack == null) continue;
                stack.setItemDamage(1);
                recipe.mInputs.add(
                    new PositionedStack(
                        stack,
                        85 - columns * 10 + column * 20,
                        getAspectsY(recipe.mRecipe) + row * 20,
                        false));
            }
        }
    }

    private static void positionStacks(List<PositionedStack> stacks, List<Pos2d> positions) {
        for (int i = 0; i < positions.size(); i++) {
            stacks.get(i).relx = positions.get(i).x;
            stacks.get(i).rely = positions.get(i).y;
        }
    }

    protected static AspectList getAspects(GTRecipe recipe) {
        AspectList aspects = new AspectList();
        if (recipe.mSpecialItems instanceof ItemStack essence && essence.hasTagCompound()) {
            aspects.readFromNBT(essence.getTagCompound());
        }
        return aspects;
    }

    protected int getAspectsPerRow() {
        return 7;
    }

    protected abstract int getAspectsY(GTRecipe recipe);

    public int getContentHeight(GTRecipe recipe) {
        int rows = (getAspects(recipe).size() + getAspectsPerRow() - 1) / getAspectsPerRow();
        return getAspectsY(recipe) + rows * 20 + 4;
    }

    public boolean allowsPatternTransfer() {
        return true;
    }

    public abstract void drawNEIBackground(GTRecipe recipe);
}
