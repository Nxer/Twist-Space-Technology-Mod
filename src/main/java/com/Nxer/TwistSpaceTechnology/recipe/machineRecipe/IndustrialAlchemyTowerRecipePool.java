package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static thaumcraft.common.config.ConfigItems.itemEssence;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GTUtility;
import thaumcraft.common.items.ItemEssence;

public class IndustrialAlchemyTowerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        TCRecipeTools.getCrucibleCraftingRecipe();
        final IRecipeMap IIA = GTCMRecipe.IndustrialAlchemyTowerRecipe;
        for (Map.Entry<String, ArrayList<TCRecipeTools.CrucibleCraftingRecipe>> entry : TCRecipeTools.CCR.entrySet()) {
            ArrayList<TCRecipeTools.CrucibleCraftingRecipe> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                TCRecipeTools.CrucibleCraftingRecipe recipe = value.get(i);
                ItemStack Essence = new ItemStack(itemEssence);
                Essence.setItemDamage(1);
                Essence.setStackDisplayName(TextEnums.tr("IndustrialAlchemyTowerRecipeInputAspects"));
                new ItemEssence().setAspects(Essence, recipe.getInputAspects());
                Object inputItem = recipe.getInputItem();
                Object[] combined = new Object[] { inputItem, GTUtility.getIntegratedCircuit(i + 1) };
                GTValues.RA.stdBuilder()
                    .ignoreCollision()
                    .clearInvalid()
                    .special(Essence)
                    .itemInputs(combined)
                    .itemOutputs(recipe.getOutputItem())
                    .noOptimize()
                    .duration(30 * SECONDS + Math.min(recipe.getAspectAmount() * SECONDS, 600))
                    .eut(RECIPE_IV)
                    .addTo(IIA);
            }
        }
    }
}
