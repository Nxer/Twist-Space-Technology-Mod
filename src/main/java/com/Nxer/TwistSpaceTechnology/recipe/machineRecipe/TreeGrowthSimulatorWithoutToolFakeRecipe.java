package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.getModeMultiplier;
import static gregtech.api.enums.Mods.Minecraft;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.Mode;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.treeProductsMap;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm;

public class TreeGrowthSimulatorWithoutToolFakeRecipe implements IRecipePool {

    static FluidStack WaterStack = Materials.Water.getFluid(1000);

    @Override
    public void loadRecipes() {

        // debug
        ItemStack[] InputItem = { GT_Utility.getIntegratedCircuit(1), GT_Utility.getIntegratedCircuit(2),
            GT_Utility.getIntegratedCircuit(3), GT_Utility.getIntegratedCircuit(4) };

        ItemStack[] OutputItem = { getModItem(Minecraft.ID, "log", 5, 0), getModItem(Minecraft.ID, "sapling", 1, 0),
            getModItem(Minecraft.ID, "leaves", 2, 0), getModItem(Minecraft.ID, "apple", 1, 0) };
        GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes.addFakeRecipe(
            false,
            new GT_Recipe(
                false,
                InputItem,
                OutputItem,
                getModItem(Minecraft.ID, "sapling", 0, 0),
                null,
                new FluidStack[] { WaterStack },
                null,
                20,
                0,
                1),
            false);

        for (Map.Entry<String, EnumMap<Mode, ItemStack>> entry : treeProductsMap.entrySet()) {
            // String treeType = entry.getKey();
            EnumMap<Mode, ItemStack> productMap = entry.getValue();
            ItemStack sapling = productMap.get(Mode.SAPLING);
            if (sapling != null) addFakeRecipeToNEI(
                sapling,
                productMap.get(Mode.LOG),
                productMap.get(Mode.SAPLING),
                productMap.get(Mode.LEAVES),
                productMap.get(Mode.FRUIT),
                WaterStack);
        }
        // debug

        addFakeRecipeToNEI(
            getModItem(Minecraft.ID, "sapling", 1, 0),
            getModItem(Minecraft.ID, "log", 5, 0),
            getModItem(Minecraft.ID, "sapling", 1, 0),
            getModItem(Minecraft.ID, "leaves", 2, 0),
            getModItem(Minecraft.ID, "apple", 1, 0),
            WaterStack);

    }

    public static void addFakeRecipeToNEI(ItemStack saplingIn, ItemStack log, ItemStack saplingOut, ItemStack leaves,
        ItemStack fruit, FluidStack fluidStack) {

        int recipeCount = GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes.getAllRecipes()
            .size();
        // Sapling goes into the "special" slot.
        ItemStack specialStack = saplingIn.copy();
        specialStack.stackSize = 0;

        ItemStack[] inputStacks = new ItemStack[GregtechMetaTileEntityTreeFarm.Mode.values().length];
        ItemStack[] outputStacks = new ItemStack[GregtechMetaTileEntityTreeFarm.Mode.values().length];
        FluidStack[] inputfluidStacks = new FluidStack[1];
        inputfluidStacks[0] = fluidStack.copy();
        for (GregtechMetaTileEntityTreeFarm.Mode mode : GregtechMetaTileEntityTreeFarm.Mode.values()) {
            ItemStack output = switch (mode) {
                case LOG -> log;
                case SAPLING -> saplingOut;
                case LEAVES -> leaves;
                case FRUIT -> fruit;
            };
            if (output != null) {
                int ordinal = mode.ordinal();
                inputStacks[ordinal] = GT_Utility.getIntegratedCircuit(ordinal + 1);
                outputStacks[ordinal] = output.copy();
                outputStacks[ordinal].stackSize *= getModeMultiplier(mode);
            }
        }

        Logger.INFO(
            "Adding Tree Growth Simulation NEI recipe for " + specialStack.getDisplayName()
                + " -> "
                + ItemUtils.getArrayStackNames(outputStacks));

        GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes.addFakeRecipe(
            false,
            new GT_Recipe(
                false,
                inputStacks,
                outputStacks,
                specialStack,
                null,
                inputfluidStacks,
                null,
                20,
                0,
                recipeCount),
            false);
        // return GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes.getAllRecipes().size() > recipeCount;
    }

    private void loadManualRecipes() {
        // EnumMap<GregtechMetaTileEntityTreeFarm.Mode, ItemStack> SaplingIn = treeProductsMap.;
    }

    // private void loadGeneralRecipes() {
    // GT_Values.RA.stdBuilder()
    // .specialItem(getModItem(Minecraft.ID, "sapling", 0, 0))
    // .itemInputs(
    // GT_Utility.getIntegratedCircuit(1),
    // GT_Utility.getIntegratedCircuit(2),
    // GT_Utility.getIntegratedCircuit(3),
    // GT_Utility.getIntegratedCircuit(4))
    // .itemOutputs(
    // getModItem(Minecraft.ID, "log", 5, 0),
    // getModItem(Minecraft.ID, "sapling", 1, 0),
    // getModItem(Minecraft.ID, "leaves", 2, 0),
    // getModItem(Minecraft.ID, "apple", 1, 0))
    // .fluidInputs(Materials.Water.getFluid(1000))
    // .noOptimize()
    // .eut(RECIPE_LV)
    // .duration(20)
    // .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);
    // }

}
