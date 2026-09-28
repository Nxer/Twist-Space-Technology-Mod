package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ParallelHelper;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ParallelHelper;

/** Each slot owns a recipe cache. Authorization is checked even for cached recipes. */
public final class BottledChaosProcessingLogic extends GTCM_ProcessingLogic {

    private ItemStack family;
    private long categories;
    private long requiredCategories;

    public void setAuthorization(ItemStack credential, long categoryMask) {
        family = ItemBottledChaosCredential.getRecipeFamily(credential);
        categories = categoryMask;
        requiredCategories = 0;
    }

    public long getRequiredCategories() {
        return requiredCategories;
    }

    @Override
    protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        Long mask = recipe.getMetadata(BottledChaosRecipeMetadata.REQUIRED_CATEGORY_MASK);
        ItemStack token = recipe.getMetadata(BottledChaosRecipeMetadata.REQUIRED_RECIPE_TOKEN);
        if (mask == null || mask == 0 || token == null) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.invalid_authorization_metadata");
        }
        if (family == null || !GTUtility.areStacksEqual(family, token, false)) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.missing_recipe_token");
        }
        if ((categories & mask) != mask) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.missing_recipe_categories");
        }
        requiredCategories = mask;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    protected @NotNull ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
        // Never expand large output quantities into thousands of int-sized stacks.
        return ((GTCM_ParallelHelper) super.createParallelHelper(recipe)).enableLongOutputCalculation();
    }
}
