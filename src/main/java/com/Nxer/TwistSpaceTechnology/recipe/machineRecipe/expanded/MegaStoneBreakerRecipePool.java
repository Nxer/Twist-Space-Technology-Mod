package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MegaStoneBreakerRecipeMap;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;

public class MegaStoneBreakerRecipePool {

    private static final ItemStack CobbleStone = new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone1 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 0)
        : new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone2 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 1)
        : new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone3 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 2)
        : new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone4 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 3)
        : new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone5 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 4)
        : new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone6 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 5)
        : new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone7 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 6)
        : new ItemStack(Blocks.cobblestone);
    private static final ItemStack CobbleStone8 = Mods.ExtraUtilities.isModLoaded()
        ? ModItem.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 1, 7)
        : new ItemStack(Blocks.cobblestone);

    public static void loadRecipes() {
        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(CobbleStone)
            .eut(6)
            .duration(20)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .itemOutputs(CobbleStone1)
            .eut(6 * 4)
            .duration(20 * 2)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .itemOutputs(CobbleStone2)
            .eut(6 * 16)
            .duration(20 * 3)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .itemOutputs(CobbleStone3)
            .eut(6 * 64)
            .duration(20 * 4)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(5))
            .itemOutputs(CobbleStone4)
            .eut(6 * 256)
            .duration(20 * 5)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(6))
            .itemOutputs(CobbleStone5)
            .eut(6 * 1024)
            .duration(20 * 6)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(7))
            .itemOutputs(CobbleStone6)
            .eut(6 * 2048)
            .duration(20 * 7)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(8))
            .itemOutputs(CobbleStone7)
            .eut(6 * 4096)
            .duration(20 * 8)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(9))
            .itemOutputs(CobbleStone8)
            .eut(6 * 16384)
            .duration(20 * 9)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(20))
            .itemOutputs(new ItemStack(Blocks.stone))
            .eut(6)
            .duration(20)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(24),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1))
            .itemOutputs(new ItemStack(Blocks.obsidian))
            .eut(6)
            .duration(20)
            .addTo(MegaStoneBreakerRecipeMap);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1))
            .itemOutputs(new ItemStack(Blocks.netherrack))
            .eut(6)
            .duration(20)
            .addTo(MegaStoneBreakerRecipeMap);

        if (Mods.EtFuturumRequiem.isModLoaded()) {
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(22),
                    ModItem.getModItem(Mods.EtFuturumRequiem.ID, "blue_ice", 0, 0),
                    new ItemStack(Blocks.soul_sand, 0))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.Basalt, 1L))
                .eut(6)
                .duration(20)
                .addTo(MegaStoneBreakerRecipeMap);

            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(23),
                    ModItem.getModItem(Mods.EtFuturumRequiem.ID, "magma", 0, 0),
                    new ItemStack(Blocks.soul_sand, 0))
                .itemOutputs(ModItem.getModItem(Mods.EtFuturumRequiem.ID, "cobbled_deepslate", 1, 0))
                .eut(6)
                .duration(20)
                .addTo(MegaStoneBreakerRecipeMap);
        }

        if (Mods.ThaumicBases.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(ModItem.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 2))
                .inputChances(1000)
                .circuit(10)
                .itemOutputs(
                    new ItemStack(Blocks.netherrack),
                    new ItemStack(Blocks.soul_sand),
                    new ItemStack(Blocks.quartz_ore))
                .outputChances(6000, 3000, 1000)
                .duration(20)
                .eut(TierEU.RECIPE_LV)
                .addTo(MegaStoneBreakerRecipeMap);

            GTValues.RA.stdBuilder()
                .itemInputs(ModItem.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 3))
                .inputChances(1000)
                .circuit(11)
                .itemOutputs(new ItemStack(Blocks.end_stone), new ItemStack(Blocks.obsidian))
                .outputChances(9000, 1000)
                .duration(20)
                .eut(TierEU.RECIPE_LV)
                .addTo(MegaStoneBreakerRecipeMap);
        }

        loadPlanetRecipes();
    }

    private static void loadPlanetRecipes() {
        // Layers are listed from the surface down, using circuits 20, 19 and 18.
        // spotless:off
        planet("Mo", stone(Mods.GalacticraftCore, "tile.moonBlock", 4));
        planet("De", stone(Mods.GalaxySpace, "deimosblocks", 0), stone(Mods.GalaxySpace, "deimosblocks", 1));
        planet("Ma", stone(Mods.GalacticraftMars, "tile.mars", 5), stone(Mods.GalacticraftMars, "tile.mars", 6), stone(Mods.GalacticraftMars, "tile.mars", 9));
        planet("Ph", stone(Mods.GalaxySpace, "phobosblocks", 0), stone(Mods.GalaxySpace, "phobosblocks", 1), stone(Mods.GalaxySpace, "phobosblocks", 2));
        planet("As", stone(Mods.GalacticraftMars, "tile.asteroidsBlock", 0), stone(Mods.GalacticraftMars, "tile.asteroidsBlock", 1), stone(Mods.GalacticraftMars, "tile.asteroidsBlock", 2));
        planet("Ca", stone(Mods.GalaxySpace, "callistoblocks", 0), stone(Mods.GalaxySpace, "callistoblocks", 1));
        planet("Ce", stone(Mods.GalaxySpace, "ceresblocks", 0), stone(Mods.GalaxySpace, "ceresblocks", 1));
        planet("Ga", stone(Mods.GalaxySpace, "ganymedeblocks", 0), stone(Mods.GalaxySpace, "ganymedeblocks", 1));
        planet("Io", stone(Mods.GalaxySpace, "ioblocks", 0), stone(Mods.GalaxySpace, "ioblocks", 2));
        planet("Me", stone(Mods.GalaxySpace, "mercuryblocks", 0), stone(Mods.GalaxySpace, "mercuryblocks", 1), stone(Mods.GalaxySpace, "mercuryblocks", 2));
        planet("Ve", stone(Mods.GalaxySpace, "venusblocks", 0), stone(Mods.GalaxySpace, "venusblocks", 1));
        planet("En", stone(Mods.GalaxySpace, "enceladusblocks", 1));
        planet("Mi", stone(Mods.GalaxySpace, "mirandablocks", 0), stone(Mods.GalaxySpace, "mirandablocks", 1), stone(Mods.GalaxySpace, "mirandablocks", 2));
        planet("Ob", stone(Mods.GalaxySpace, "oberonblocks", 0), stone(Mods.GalaxySpace, "oberonblocks", 1), stone(Mods.GalaxySpace, "oberonblocks", 2));
        planet("Ti", stone(Mods.GalaxySpace, "titanblocks", 0), stone(Mods.GalaxySpace, "titanblocks", 1), stone(Mods.GalaxySpace, "titanblocks", 2));
        planet("Ra", stone(Mods.GalacticraftCore, "tile.moonBlock", 4));
        planet("Pr", stone(Mods.GalaxySpace, "proteusblocks", 0), stone(Mods.GalaxySpace, "proteusblocks", 1), stone(Mods.GalaxySpace, "proteusblocks", 2));
        planet("Tr", stone(Mods.GalaxySpace, "tritonblocks", 0), stone(Mods.GalaxySpace, "tritonblocks", 1), stone(Mods.GalaxySpace, "tritonblocks", 2));
        planet("Ha", stone(Mods.GalaxySpace, "haumeablocks", 0));
        planet("MM", stone(Mods.GalaxySpace, "makemakegrunt", 0), stone(Mods.GalaxySpace, "makemakegrunt", 1));
        planet("Pl", stone(Mods.GalaxySpace, "plutoblocks", 5));
        planet("BE", stone(Mods.GalaxySpace, "barnardaEgrunt", 0), stone(Mods.GalaxySpace, "barnardaEsubgrunt", 0));
        planet("BF", stone(Mods.GalaxySpace, "barnardaFgrunt", 0), stone(Mods.GalaxySpace, "barnardaFsubgrunt", 0));
        planet("CB", stone(Mods.GalaxySpace, "acentauribbgrunt", 0), stone(Mods.GalaxySpace, "acentauribbsubgrunt", 0));
        planet("TE", stone(Mods.GalaxySpace, "tcetieblocks", 0), stone(Mods.GalaxySpace, "tcetieblocks", 1), stone(Mods.GalaxySpace, "tcetieblocks", 2));
        planet("VB", stone(Mods.GalaxySpace, "vegabgrunt", 0), stone(Mods.GalaxySpace, "vegabsubgrunt", 0));
        planet("An", stone(Mods.GalacticraftAmunRa, "tile.baseBlockRock", 1));
        planet("Ho", new ItemStack(Blocks.obsidian));
        planet("Mh", stone(Mods.GalacticraftAmunRa, "tile.baseBlockRock", 1));
        planet("MB", stone(Mods.GalacticraftMars, "tile.asteroidsBlock", 0), stone(Mods.GalacticraftMars, "tile.asteroidsBlock", 1), stone(Mods.GalacticraftMars, "tile.asteroidsBlock", 2));
        // spotless:on
    }

    private static ItemStack stone(Mods mod, String name, int metadata) {
        if (!mod.isModLoaded()) return null;
        ItemStack stack = ModItem.getModItem(mod.ID, name, 1, metadata);
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return id != null && mod.ID.equals(id.modId) && name.equals(id.name) ? stack : null;
    }

    private static void planet(String dimension, ItemStack... layers) {
        ItemStack marker = ItemDimensionDisplay.getItem(dimension);
        if (marker == null) return;
        marker.stackSize = 0;
        for (int i = 0; i < layers.length; i++) {
            if (layers[i] == null) continue;
            GTValues.RA.stdBuilder()
                .itemInputs(marker.copy(), GTUtility.getIntegratedCircuit(20 - i))
                .itemOutputs(layers[i])
                .duration(20)
                .eut(6)
                .addTo(MegaStoneBreakerRecipeMap);
        }
    }
}
