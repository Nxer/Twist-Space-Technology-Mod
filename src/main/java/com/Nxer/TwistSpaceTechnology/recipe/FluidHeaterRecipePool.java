package com.Nxer.TwistSpaceTechnology.recipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.DRAGON_METAL;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.config.Config;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Utility;

public class FluidHeaterRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        if (Config.Registry_DragonBlood_ExtraRecipe) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(getFluidStack("potion.dragonblood", 1000))
                .fluidOutputs(DRAGON_METAL.getFluidStack(144 * 6))
                .noOptimize()
                .eut(RECIPE_UXV)
                .duration(20 * 6)
                .addTo(RecipeMaps.fluidHeaterRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.dragon_egg, 0, 0))
                .fluidInputs(getFluidStack("fieryblood", 1000))
                .fluidOutputs(DRAGON_METAL.getFluidStack(144 * 6))
                .noOptimize()
                .eut(RECIPE_UXV)
                .duration(20 * 6)
                .addTo(RecipeMaps.fluidHeaterRecipes);
        }

    }
}
