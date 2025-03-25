package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.BloodyHellRecipes;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellAlchemicTierKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellTierKey;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.TSTArrayUtils;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipe;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRecipe;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;

public class BloodyHellRecipePool {

    public static void loadRecipes() {
        // total(Life Essence, L) / soakingSpeed(L/tick) = duration(tick)
        // for example, a recipe costs 1,000L of LE, it should take 10 ticks to craft
        var soakingSpeed = 10;

        // the LE cost for each binding ritual
        var bindingRecipeLECost = 30_000;

        for (AltarRecipe recipe : AltarRecipeRegistry.altarRecipes) {
            // filter empty output recipes, which these recipes are most likely charging orbs.
            if (recipe.result == null) continue;

            GTValues.RA.stdBuilder()
                .itemInputs(recipe.requiredItem, GTUtility.getIntegratedCircuit(1))
                .itemOutputs(recipe.result)
                .fluidInputs(BloodMagicHelper.getLifeEssence(recipe.liquidRequired))
                .eut(0)
                .duration(recipe.liquidRequired / soakingSpeed)
                .metadata(BloodyHellTierKey.INSTANCE, recipe.minTier)
                .addTo(BloodyHellRecipes);
        }

        for (AlchemyRecipe recipe : AlchemyRecipeRegistry.recipes) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    TSTArrayUtils.concatToLast(ItemStack.class, recipe.getRecipe(), GTUtility.getIntegratedCircuit(2)))
                .itemOutputs(recipe.getResult())
                .fluidInputs(BloodMagicHelper.getLifeEssence(recipe.getAmountNeeded() * 100))
                .eut(0)
                .duration(recipe.getAmountNeeded() * 100 / soakingSpeed)
                .metadata(BloodyHellAlchemicTierKey.INSTANCE, recipe.getOrbLevel())
                .addTo(BloodyHellRecipes);
        }

        for (BindingRecipe recipe : BindingRegistry.bindingRecipes) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    recipe.requiredItem,
                    new ItemStack(ModItems.weakBloodShard, 1),
                    GTUtility.getIntegratedCircuit(11))
                .itemOutputs(recipe.outputItem)
                .fluidInputs(BloodMagicHelper.getLifeEssence(bindingRecipeLECost))
                .eut(0)
                .duration(bindingRecipeLECost / soakingSpeed)
                .metadata(BloodyHellTierKey.INSTANCE, 1)
                .addTo(BloodyHellRecipes);
        }
    }
}
