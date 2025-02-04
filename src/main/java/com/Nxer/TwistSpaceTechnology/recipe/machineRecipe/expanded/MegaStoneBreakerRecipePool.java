package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MegaStoneBreakerRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class MegaStoneBreakerRecipePool implements IRecipePool {

    ItemStack CobbleStone = new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone1 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 0)
        : new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone2 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 1)
        : new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone3 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 2)
        : new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone4 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 3)
        : new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone5 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 4)
        : new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone6 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 5)
        : new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone7 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 6)
        : new ItemStack(Blocks.cobblestone);
    ItemStack CobbleStone8 = Mods.ExtraUtilities.isModLoaded()
        ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 7)
        : new ItemStack(Blocks.cobblestone);

    @Override
    public void loadRecipes() {
        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(CobbleStone)
            .eut(6)
            .duration(20)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .itemOutputs(CobbleStone1)
            .eut(6 * 4)
            .duration(20 * 2)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .itemOutputs(CobbleStone2)
            .eut(6 * 16)
            .duration(20 * 3)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .itemOutputs(CobbleStone3)
            .eut(6 * 64)
            .duration(20 * 4)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .itemOutputs(CobbleStone4)
            .eut(6 * 256)
            .duration(20 * 5)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(6))
            .itemOutputs(CobbleStone5)
            .eut(6 * 1024)
            .duration(20 * 6)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(7))
            .itemOutputs(CobbleStone6)
            .eut(6 * 2048)
            .duration(20 * 7)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(8))
            .itemOutputs(CobbleStone7)
            .eut(6 * 4096)
            .duration(20 * 8)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(9))
            .itemOutputs(CobbleStone8)
            .eut(6 * 16384)
            .duration(20 * 9)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(20))
            .itemOutputs(new ItemStack(Blocks.stone))
            .eut(6)
            .duration(20)
            .addTo(MegaStoneBreakerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1))
            .itemOutputs(new ItemStack(Blocks.obsidian))
            .eut(6)
            .duration(20)
            .addTo(MegaStoneBreakerRecipes);
    }
}
