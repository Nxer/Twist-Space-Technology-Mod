package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class MiracleDoorRecipePool implements IRecipePool {

    public static final HashMap<Fluid, ItemStack> MoltenToIngot = new HashMap<>();

    public void initData() {

        // generate molten fluid to ingot map
        for (GTRecipe recipeSolidifier : fluidSolidifierRecipes.getAllRecipes()) {
            if (recipeSolidifier.mInputs == null || recipeSolidifier.mInputs.length < 1
                || recipeSolidifier.mOutputs == null
                || recipeSolidifier.mOutputs.length < 1) continue;
            if (metaItemEqual(
                GTModHandler.getModItem(Mods.GregTech.ID, "gt.metaitem.01", 1, 32306),
                recipeSolidifier.mInputs[0])) {
                MoltenToIngot.put(recipeSolidifier.mFluidInputs[0].getFluid(), recipeSolidifier.mOutputs[0]);
            }
        }
    }

    public void prepareABSRecipes() {

        for (GTRecipe recipe : GTPPRecipeMaps.alloyBlastSmelterRecipes.getAllRecipes()) {
            int minOutputFluidAmount = 144;
            // if there is more than one output fluid, find the fewest
            for (FluidStack aOutputFluid : recipe.mFluidOutputs) {
                // if (aOutputFluid.amount % 144 == 0) continue;
                minOutputFluidAmount = Math.min(minOutputFluidAmount, aOutputFluid.amount);
            }
            ArrayList<ItemStack> inputItemList = new ArrayList<>();
            ArrayList<ItemStack> outputItemList = new ArrayList<>();
            ArrayList<FluidStack> inputFluidList = new ArrayList<>();
            ArrayList<FluidStack> outputFluidList = new ArrayList<>();

            int RecipeMultiplier = 1;
            if (minOutputFluidAmount < 144) {
                int CorrectFluidAmount = minOutputFluidAmount;
                while (CorrectFluidAmount % 144 != 0) {
                    CorrectFluidAmount += minOutputFluidAmount;
                }
                RecipeMultiplier = CorrectFluidAmount / minOutputFluidAmount;
            }

            if (recipe.mInputs != null) for (ItemStack aItemStack : recipe.mInputs) {
                ItemStack aStackCopy = aItemStack.copy();
                aStackCopy.stackSize *= RecipeMultiplier;
                inputItemList.add(aStackCopy);
            }

            if (recipe.mFluidInputs != null) for (FluidStack aFluidStack : recipe.mFluidInputs) {
                FluidStack aFluidCopy = aFluidStack.copy();
                aFluidCopy.amount *= RecipeMultiplier;
                inputFluidList.add(aFluidCopy);
            }

            if (recipe.mOutputs != null) for (ItemStack aItemStack : recipe.mOutputs) {
                if (aItemStack.getUnlocalizedName()
                    .contains("ingot")
                    || aItemStack.getUnlocalizedName()
                        .contains("Ingot")
                    || aItemStack.getUnlocalizedName()
                        .contains("INGOT")) {
                    outputFluidList.add(StellarForgeRecipePool.getMoltenFluids(aItemStack, aItemStack.stackSize));
                } else {
                    ItemStack aStackCopy = aItemStack.copy();
                    aStackCopy.stackSize *= RecipeMultiplier;
                    outputItemList.add(aStackCopy);
                }

            }

            if (recipe.mOutputs != null) for (FluidStack aFluidStack : recipe.mFluidOutputs) {
                FluidStack aFluidCopy = aFluidStack.copy();
                aFluidCopy.amount *= RecipeMultiplier;
                outputFluidList.add(aFluidCopy);
            }

            StellarForgeRecipePool.addToMiracleDoorRecipes(
                inputItemList.toArray(new ItemStack[0]),
                inputFluidList.toArray(new FluidStack[0]),
                outputItemList.toArray(new ItemStack[0]),
                outputFluidList.toArray(new FluidStack[0]),
                recipe.mEUt,
                recipe.mDuration,
                GTCMRecipe.MiracleDoorRecipes);
        }

    }

    @Override
    public void loadRecipes() {
        initData();
    }

    public void loadOnServerStarted() {
        prepareABSRecipes();
    }
}
