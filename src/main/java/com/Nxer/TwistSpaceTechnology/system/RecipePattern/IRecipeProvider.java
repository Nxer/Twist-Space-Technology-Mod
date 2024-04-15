package com.Nxer.TwistSpaceTechnology.system.RecipePattern;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IRecipeProvider {

    public ItemStack[] getItemInput();

    public FluidStack[] getFluidInput();

    public ItemStack[] getItemOutput();

    public FluidStack[] getFluidOutput();

    public long getRecipeProcessRounds();

    public double[] getItemOutputPriority();

    public double[] getFluidOutputPriority();

    public ItemStack[] getRealItemOutput();

    public FluidStack[] getRealFluidOutput();

    public boolean matchRecipe(Object o);

    public void initRecipe();

    public String provider();

}
