package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.util.RecipeMathUtils.roundUpToMultiple;
import static com.Nxer.TwistSpaceTechnology.util.TstUtils.copyAmount;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.util.GTRecipe;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class RapidHeatExchangeRecipePool {

    public static void loadRecipes() {
        FluidStack DenseSupercriticalSteam = Materials.DenseSupercriticalSteam.getGas(1);

        for (GTRecipe aRecipe : GoodGeneratorRecipeMaps.extremeHeatExchangerFuels.getAllRecipes()) {
            if (aRecipe == null) continue;
            if (aRecipe.mFluidInputs[0] == null || aRecipe.mFluidInputs[1] == null
                || aRecipe.mFluidOutputs[1] == null
                || aRecipe.mFluidOutputs[2] == null) continue;
            FluidStack HotFluid = aRecipe.mFluidInputs[0].copy();
            FluidStack ColdFluid = aRecipe.mFluidOutputs[2].copy();
            FluidStack Water = aRecipe.mFluidInputs[1].copy();
            FluidStack Steam = aRecipe.mFluidOutputs[1].copy();

            // The power generation of hot fluid is directly determined by distilled water consumption.
            // Establishing their relationship enables derivation of all other parameters.

            boolean isDense = Steam.isFluidEqual(DenseSupercriticalSteam);
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

        // extra recipe
        {
            // Hot super coolant (space coolant)
            {
                TST_RecipeBuilder.builder()
                    .fluidInputs(WerkstoffMaterialPool.HotSuperCoolant.getFluidOrGas(1), Materials.Water.getFluid(25))
                    .fluidOutputs(copyAmount(DenseSupercriticalSteam, 4), Materials.SuperCoolant.getFluid(1))
                    .duration(20)
                    .addTo(GTCMRecipe.RapidHeatExchangeRecipes);

                TST_RecipeBuilder.builder()
                    .fluidInputs(WerkstoffMaterialPool.HotSuperCoolant.getFluidOrGas(1))
                    .fluidOutputs(Materials.SuperCoolant.getFluid(1))
                    .duration(20)
                    .addTo(GTCMRecipe.RapidCoolingDownRecipes);
            }

            // Creon plasma
            {
                TST_RecipeBuilder.builder()
                    .fluidInputs(MaterialsUEVplus.Creon.getPlasma(1), Materials.Water.getFluid(2000))
                    .fluidOutputs(copyAmount(DenseSupercriticalSteam, 320), MaterialsUEVplus.Creon.getMolten(1))
                    .duration(20)
                    .addTo(GTCMRecipe.RapidHeatExchangeRecipes);

                TST_RecipeBuilder.builder()
                    .fluidInputs(MaterialsUEVplus.Creon.getPlasma(1))
                    .fluidOutputs(MaterialsUEVplus.Creon.getMolten(1))
                    .duration(20)
                    .addTo(GTCMRecipe.RapidCoolingDownRecipes);
            }

        }

    }

}
