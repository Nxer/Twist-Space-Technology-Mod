package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;

// spotless:off
public class HyperSpacetimeTransformerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map HST=GTCMRecipe.instance.HyperSpacetimeTransformerRecipe;
        //terrastrial recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.wheat,1))
            .fluidInputs(Materials.Water.getFluid(1000))
            .itemOutputs(ItemList.Food_Baked_Bun.get(1))
            .noFluidOutputs()
            .eut(2)
            .duration(20 * 114514)
            .addTo(HST);
        
    }
}
// spotless:on
