package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.DeployedNanoCoreRecipes;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getLCM;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;

public class DeployedNanoCoreRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        for (GTRecipe aRecipe : RecipeMaps.nanoForgeRecipes.getAllRecipes()) {
            if (aRecipe != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(aRecipe.mInputs)
                    .fluidInputs(aRecipe.mFluidInputs)
                    .itemOutputs(aRecipe.mOutputs)
                    .eut(aRecipe.mEUt)
                    .duration(aRecipe.mDuration)
                    .addTo(DeployedNanoCoreRecipes);

                for (FluidStack aFluid : aRecipe.mFluidInputs) {
                    if (aFluid.isFluidEqual(Materials.UUMatter.getFluid(1))) {
                        int Multiplier = (int) (getLCM(aFluid.amount, 1000000) / aFluid.amount);
                        if (Multiplier == 0) break;

                        ArrayList<ItemStack> InputItems = new ArrayList<>();
                        ArrayList<FluidStack> InputFluids = new ArrayList<>();
                        ArrayList<ItemStack> OutputItems = new ArrayList<>();

                        if (aRecipe.mInputs != null) for (ItemStack aStack : aRecipe.mInputs) {
                            InputItems.add(copyAmount(aStack.stackSize * Multiplier, aStack));
                        }

                        if (aRecipe.mFluidInputs != null) for (FluidStack aStack : aRecipe.mFluidInputs) {
                            FluidStack aCopy;
                            if (aStack.isFluidEqual(Materials.UUMatter.getFluid(1))) {
                                aCopy = MaterialPool.ConcentratedUUMatter
                                    .getFluidOrGas(aStack.amount * Multiplier / 1000000);
                            } else {
                                aCopy = aStack.copy();
                                aStack.amount *= Multiplier;
                            }
                            InputFluids.add(aCopy);
                        }

                        if (aRecipe.mOutputs != null) for (ItemStack aStack : aRecipe.mOutputs) {
                            OutputItems.add(copyAmount(aStack.stackSize * Multiplier, aStack));
                        }

                        GTValues.RA.stdBuilder()
                            .itemInputs(InputItems.toArray(new ItemStack[0]))
                            .fluidInputs(InputFluids.toArray(new FluidStack[0]))
                            .itemOutputs(OutputItems.toArray(new ItemStack[0]))
                            .eut(aRecipe.mEUt)
                            .duration((int) (aRecipe.mDuration * 0.8 * Multiplier))
                            .addTo(DeployedNanoCoreRecipes);
                    }
                    break;
                }
            }
        }
    }

}
