package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.EcoSphereFakeRecipes;

import gregtech.api.enums.Mods;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;

public class AquaticZoneSimulatorFakeRecipe implements IRecipePool {

    @Override
    public void loadRecipes() {
        ItemStack[] ExtraOutputs={
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.HugeLilyPad", 1, 0),
            GT_ModHandler.getModItem(Mods.Minecraft.ID, "waterlily", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 2),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 2),
            GT_ModHandler.getModItem(Mods.Minecraft.ID, "bone", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "cranberryItem", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "riceItem", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "seaweedItem", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "waterchestnutItem", 1, 0),
            GT_ModHandler.getModItem(Mods.Minecraft.ID, "dye", 2, 0)};
    }

    void addFakeRecipe(ItemStack[] inputStacks, ItemStack[] outputStacks, ItemStack[] specialStacks,
        FluidStack inputFluid) {
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
