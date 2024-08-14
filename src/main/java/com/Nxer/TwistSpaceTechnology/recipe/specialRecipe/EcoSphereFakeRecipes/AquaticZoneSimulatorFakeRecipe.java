package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.EcoSphereFakeRecipes;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.recipe.common.CI;

public class AquaticZoneSimulatorFakeRecipe implements IRecipePool {

    static FluidStack WaterStack = Materials.Water.getFluid(1000);
    ArrayList<ItemStack> WatersOutputs = new ArrayList<>();
    ItemStack[] ExtraOutputs = { GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.HugeLilyPad", 1, 0),
        GT_ModHandler.getModItem(Mods.Minecraft.ID, "waterlily", 1, 0),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 0),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 1),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 2),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 2),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "cranberryItem", 1, 0),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "riceItem", 1, 0),
        GT_ModHandler.getModItem(Mods.Minecraft.ID, "bone", 2, 0),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "seaweedItem", 3, 0),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "waterchestnutItem", 3, 0),
        GT_ModHandler.getModItem(Mods.Minecraft.ID, "dye", 3, 0) };

    @Override
    public void loadRecipes() {
        initStatics();
        loadAquaticZoneRecipes();
    }

    void initStatics() {
        Collections.addAll(WatersOutputs, ExtraOutputs);

        // generate fish recipes
        for (GT_Recipe aRecipe : GTPPRecipeMaps.fishPondRecipes.getAllRecipes()) {
            if (aRecipe.mInputs[0].isItemEqual(CI.getNumberedCircuit(14))) {
                ItemStack aStack = aRecipe.mOutputs[0].copy();
                aStack.stackSize = 4;
                WatersOutputs.add(aStack);
            }
        }
    }

    void loadAquaticZoneRecipes() {
        for (ItemStack aStack : WatersOutputs) {
            addFakeRecipe(GTCMItemList.TestItem0.get(1), aStack, WaterStack);
        }
    }

    void addFakeRecipe(ItemStack inputStacks, ItemStack outputStacks, FluidStack inputFluid) {
        GT_Values.RA.stdBuilder()
            .itemInputs(inputStacks)
            .itemOutputs(outputStacks)
            .fluidInputs(inputFluid)
            .noOptimize()
            .fake()
            .duration(20)
            .eut(0)
            .addTo(GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }
}
