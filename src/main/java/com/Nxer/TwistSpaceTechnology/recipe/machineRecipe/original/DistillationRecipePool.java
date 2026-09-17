package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.glodblock.github.loader.ItemAndBlockHolder;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;

public class DistillationRecipePool {

    public static void loadRecipes() {
        final IRecipeMap DT = RecipeMaps.distillationTowerRecipes;

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1000))
            .itemOutputs(GTCMItemList.VoidPollen.get(1))
            .fluidOutputs(MaterialPool.PurifiedMana.getFluidOrGas(800))
            .outputChances(100)
            .eut(RECIPE_IV)
            .duration(200)
            .addTo(DT);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemAndBlockHolder.INFINITY_WATER_CELL))
            .fluidInputs(FluidRegistry.getFluidStack("ic2distilledwater", Integer.MAX_VALUE - 7))
            .itemOutputs(new ItemStack(TstItems.InfinityDistilledWaterStorageCell))
            .fluidOutputs(Materials.Water.getFluid(Integer.MAX_VALUE - 7))
            .eut(RECIPE_UMV / 2)
            .duration(69120000)
            .addTo(RecipeMaps.distilleryRecipes);
    }
}
