package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.queryTreeProduct;
import static gregtech.api.enums.Mods.Minecraft;
import static gregtech.api.util.GT_ModHandler.getModItem;

import java.util.EnumMap;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.Mode;

public class TreeGrowthSimulatorWithoutToolFakeRecipe implements IRecipePool {

    static FluidStack WaterStack = Materials.Water.getFluid(1000);

    @Override
    public void loadRecipes() {

        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.TestItem0.get(1))
            .itemOutputs(GTCMItemList.TestItem0.get(1))
            .fluidInputs(WaterStack)
            .fluidOutputs()
            .eut(0)
            .duration(20)
            .noOptimize()
            .specialItem(new ItemStack(Blocks.sapling, 0, 0))
            .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);

        // debug
        ItemStack[] InputItem = { GT_Utility.getIntegratedCircuit(1), GT_Utility.getIntegratedCircuit(2),
            GT_Utility.getIntegratedCircuit(3), GT_Utility.getIntegratedCircuit(4) };

        ItemStack[] OutputItem = { getModItem(Minecraft.ID, "log", 5, 0), getModItem(Minecraft.ID, "sapling", 1, 0),
            getModItem(Minecraft.ID, "leaves", 2, 0), getModItem(Minecraft.ID, "apple", 1, 0) };

        // ItemStack[] SaplingStack = {
        // // Oak
        // new ItemStack(Blocks.sapling, 1, 0),
        // // Spruce
        // new ItemStack(Blocks.sapling, 1, 1),
        // // Birch
        // new ItemStack(Blocks.sapling, 1, 2),
        // // Jungle
        // new ItemStack(Blocks.sapling, 1, 3),
        // // Acacia
        // new ItemStack(Blocks.sapling, 1, 4),
        // // Dark Oak
        // new ItemStack(Blocks.sapling, 1, 5),
        // // Brown Mushroom
        // new ItemStack(Blocks.brown_mushroom, 1, 0),
        // // Red Mushroom
        // new ItemStack(Blocks.red_mushroom, 1, 0) };

        // for (ItemStack SaplingIn : SaplingStack) {
        // addRecipe2(SaplingIn);
        // }
    }

    public void addRecipe2(ItemStack sapling) {

        ItemStack specialStack = sapling.copy();
        specialStack.stackSize = 0;
        // String key = Item.itemRegistry.getNameForObject(sapling.getItem()) + ":" + sapling.getItemDamage();
        EnumMap<Mode, ItemStack> ProductMap = queryTreeProduct(sapling);
        ItemStack[] inputStacks = new ItemStack[Mode.values().length];
        ItemStack[] outputStacks = new ItemStack[Mode.values().length];
        int count = 0;
        if (ProductMap != null) {
            for (Mode mode : Mode.values()) {
                if (ProductMap.get(mode) != null) {
                    inputStacks[count] = GT_Utility.getIntegratedCircuit(count + 1)
                        .copy();
                    outputStacks[count] = ProductMap.get(mode)
                        .copy();
                }
                count++;
            }
            // inputStacks[count] = GTCMItemList.TestItem0.get(1);
            // outputStacks[count] = GTCMItemList.TestItem0.get(1);
            GT_Values.RA.stdBuilder()
                .itemInputs(inputStacks)
                .itemOutputs(outputStacks)
                .fluidInputs(WaterStack)
                .fluidOutputs()
                .eut(0)
                .duration(20)
                .specialItem(specialStack)
                .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);
        } else {
            GT_Values.RA.stdBuilder()
                .itemInputs(sapling)
                .itemOutputs(GTCMItemList.TestItem0.get(1))
                .fluidInputs(WaterStack)
                .fluidOutputs()
                .eut(0)
                .duration(20)
                .specialItem(specialStack)
                .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);
        }

    }
}
