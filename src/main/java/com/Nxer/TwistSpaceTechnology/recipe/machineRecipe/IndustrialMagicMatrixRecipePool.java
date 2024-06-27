package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.kentington.thaumichorizons.common.ThaumicHorizons.blockCrystalDeep;
import static fox.spiteful.avaritia.items.LudicrousItems.bigPearl;
import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static makeo.gadomancy.common.registration.RegisteredItems.itemEtherealFamiliar;
import static tb.init.TBItems.revolver;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticSolid;
import static thaumcraft.common.config.ConfigItems.*;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import thaumcraft.common.items.ItemEssence;

public class IndustrialMagicMatrixRecipePool implements IRecipePool {

    protected ItemStack[] itemsUnconsumed = new ItemStack[] { new ItemStack(bigPearl) };

    protected ItemStack[] checkInputSpecial(ItemStack... itemStacks) {
        baseLoop: for (ItemStack i : itemStacks) {
            for (ItemStack u : itemsUnconsumed) {
                if (Utils.metaItemEqual(i, u)) {
                    i.stackSize = 0;
                    break baseLoop;
                }
            }
        }
        return itemStacks;
    }

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
            // #tr IndustrialMagicMatrixRecipeInputAspects
            // # Recipe required Essentia
            // #zh_CN 配方所需源质
            Essence.setStackDisplayName(TextEnums.tr("IndustrialMagicMatrixRecipeInputAspects"));
            new ItemEssence().setAspects(Essence, Recipe.getInputAspects());
            GT_Values.RA.stdBuilder()
                .ignoreCollision()
                .clearInvalid()
                .specialItem(Essence)
                .itemInputsUnified(checkInputSpecial(Recipe.getInputItem()))
                .itemOutputs((Recipe.getOutput()))
                .fluidInputs()
                .fluidOutputs()
                .noOptimize()
                .duration(200 + Recipe.getInputItem().length * 20 + Math.min(Recipe.getAspectAmount(), 600))
                .eut(RECIPE_LuV)
                .addTo(IIM);
        }

        GT_Values.RA.stdBuilder()
            .clearInvalid()
            .itemInputs(new ItemStack(blockCosmeticSolid, 8, 6), new ItemStack(itemShard, 1, 6))
            .itemOutputs(new ItemStack(blockCrystalDeep, 8))
            .fluidInputs(Materials.Thaumium.getMolten(144))
            .noOptimize()
            .duration(20)
            .eut(RECIPE_LV)
            .addTo(RecipeMaps.assemblerRecipes);
    }

}
