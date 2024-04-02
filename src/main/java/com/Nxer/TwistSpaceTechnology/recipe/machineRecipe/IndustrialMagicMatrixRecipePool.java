package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static makeo.gadomancy.common.registration.RegisteredItems.itemEtherealFamiliar;
import static tb.init.TBItems.revolver;
import static thaumcraft.common.config.ConfigItems.*;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IRecipeMap;
import thaumcraft.common.items.ItemEssence;

public class IndustrialMagicMatrixRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        TCRecipeTools.getInfusionCraftingRecipe();

        final IRecipeMap IIM = GTCMRecipe.IndustrialMagicMatrixRecipe;
        for (TCRecipeTools.InfusionCraftingRecipe Recipe : TCRecipeTools.ICR) {
            if (Recipe.getOutput()
                .getItem() == revolver
                || Recipe.getOutput()
                    .getItem() == itemJarNode
                || Recipe.getOutput()
                    .getItem() == itemEtherealFamiliar) {
                continue;
            }
            ItemStack Essence = new ItemStack(itemEssence);
            Essence.setItemDamage(1);
            Essence.setStackDisplayName(TextLocalization.IndustrialMagicMatrixRecipeInputAspects);
            new ItemEssence().setAspects(Essence, Recipe.getInputAspects());
            GT_Values.RA.stdBuilder()
                .clearInvalid()
                .specialItem(Essence)
                .itemInputsUnified(Recipe.getInputItem())
                .itemOutputs((Recipe.getOutput()))
                .fluidInputs()
                .fluidOutputs()
                .noOptimize()
                .duration(200 + Recipe.getInputItem().length * 20 + Math.min(Recipe.getAspectAmount(), 600))
                .eut(RECIPE_LuV)
                .addTo(IIM);
        }
    }
}
