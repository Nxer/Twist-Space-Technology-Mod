package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.util.RecipeMathUtils.roundUpToMultiple;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTRecipe;

public class RapidHeatExchangeRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        for (GTRecipe aRecipe : GoodGeneratorRecipeMaps.extremeHeatExchangerFuels.getAllRecipes()) {
            if (aRecipe == null) continue;
            FluidStack HotFluid = aRecipe.mFluidInputs[0].copy();
            FluidStack ColdFluid = aRecipe.mFluidOutputs[2].copy();
            FluidStack Water = aRecipe.mFluidInputs[1].copy();
            FluidStack Steam = aRecipe.mFluidOutputs[1].copy();
            if (HotFluid == null || ColdFluid == null || Water == null || Steam == null) continue;

            // The power generation of hot fluid is directly determined by distilled water consumption.
            // Establishing their relationship enables derivation of all other parameters.

            boolean isDense = Steam.isFluidEqual(Materials.DenseSupercriticalSteam.getGas(1));
            int hotFluidAmount = HotFluid.amount;
            int waterAmount = Water.amount;

            // Calculate the scaling value of distilled water
            double scaledDW = (double) waterAmount / hotFluidAmount;

            if (isDense) {
                // Adjust to the nearest multiple of 25 (rounded up)
                Water.amount = roundUpToMultiple(25, (int) scaledDW);
                // Calculate the amount of steam (distilled water x 4/25, the result must be an integer)
                Steam.amount = Water.amount * 4 / 25;
            } else {
                double decimalNum = scaledDW - Math.floor(scaledDW);
                if (decimalNum == 0) decimalNum = 1;
                int Multiplier = (int) (1.0 / decimalNum);
                Water.amount = Water.amount * Multiplier / HotFluid.amount;
                Steam.amount = Water.amount * 160;

                if (Steam.amount / 4000 > 0) {
                    Steam = Materials.DenseSupercriticalSteam.getGas(roundUpToMultiple(4000, Steam.amount) / 1000);
                }
            }

            HotFluid.amount = 1;
            ColdFluid.amount = 1;

            TST_RecipeBuilder.builder()
                .fluidInputs(HotFluid, Water)
                .fluidOutputs(Steam, ColdFluid)
                .duration(20)
                .addTo(GTCMRecipe.RapidHeatExchangeRecipes);

            TST_RecipeBuilder.builder()
                .fluidInputs(HotFluid)
                .fluidOutputs(ColdFluid)
                .duration(20)
                .addTo(GTCMRecipe.RapidCoolingDownRecipes);
        }
    }

}
