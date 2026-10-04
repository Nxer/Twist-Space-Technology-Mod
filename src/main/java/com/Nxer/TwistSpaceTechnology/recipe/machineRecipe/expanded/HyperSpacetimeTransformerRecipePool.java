package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;

// spotless:off
public class HyperSpacetimeTransformerRecipePool  {

    public static void loadRecipes() {
        loadMolecularTransformerRecipes();

        addMolecularRecipe(Materials.Naquadah.getDust(1), Materials.NaquadahEnriched.getDust(1), 20 * SECONDS, TierEU.RECIPE_UEV);
        addMolecularRecipe(Materials.NaquadahEnriched.getDust(1), Materials.Naquadria.getDust(1), 20 * SECONDS, TierEU.RECIPE_UEV);
        addMolecularRecipe(Materials.Naquadria.getDust(1), GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1), 20 * SECONDS, TierEU.RECIPE_UIV);
    }

    private static void loadMolecularTransformerRecipes() {
        // GTNH 2.4.0 / GT++ 1.9.85 imported these ASP recipes at ceil(total EU / 7680) ticks.
        addMolecularRecipe(new ItemStack(Items.redstone, 1), Materials.Ruby.getDust(1), 326, 7680);
        addMolecularRecipe(Materials.CertusQuartz.getDust(1), Materials.NetherQuartz.getDust(1), 33, 7680);
        addMolecularRecipe(Materials.NetherQuartz.getDust(1), Materials.CertusQuartz.getDust(1), 33, 7680);
        addMolecularRecipe(Materials.Copper.getDust(1), Materials.Nickel.getDust(1), 326, 7680);
        addMolecularRecipe(Materials.Tin.getDust(1), Materials.Silver.getDust(1), 326, 7680);
        addMolecularRecipe(Materials.Silver.getDust(1), Materials.Gold.getDust(1), 652, 7680);
        addMolecularRecipe(Materials.Gold.getDust(1), Materials.Platinum.getDust(1), 5209, 7680);
        addMolecularRecipe(Materials.GarnetRed.getDust(1), Materials.GarnetYellow.getDust(1), 326, 7680);
        addMolecularRecipe(Materials.GarnetYellow.getDust(1), Materials.GarnetRed.getDust(1), 326, 7680);
        addMolecularRecipe(Materials.Carbon.getDust(1), Materials.Graphene.getDust(1), 652, 7680);

        if (Mods.GalacticraftCore.isModLoaded()) {
            addMolecularRecipe(
                ItemList.Food_Cheese.get(1),
                getModItem(Mods.GalacticraftCore.ID, "item.cheeseCurd", 1),
                326,
                7680);
            if (Mods.PamsHarvestCraft.isModLoaded()) {
                addMolecularRecipe(
                    getModItem(Mods.PamsHarvestCraft.ID, "cheeseItem", 1),
                    getModItem(Mods.GalacticraftCore.ID, "item.cheeseCurd", 1),
                    326,
                    7680);
            }
        }

        if (Mods.AdvancedSolarPanel.isModLoaded() && Mods.GalaxySpace.isModLoaded()) {
            ItemStack sunnarium = getModItem(Mods.AdvancedSolarPanel.ID, "asp_crafting_items", 1, 9);
            addMolecularRecipe(Materials.Glowstone.getDust(1), sunnarium, 600, 1920);
            addMolecularRecipe(getModItem(Mods.GalaxySpace.ID, "item.GlowstoneDusts", 1, 0), sunnarium, 150, 7680);
            addMolecularRecipe(getModItem(Mods.GalaxySpace.ID, "item.GlowstoneDusts", 1, 1), sunnarium, 38, 30720);
            addMolecularRecipe(getModItem(Mods.GalaxySpace.ID, "item.GlowstoneDusts", 1, 2), sunnarium, 10, 122880);
            addMolecularRecipe(getModItem(Mods.GalaxySpace.ID, "item.GlowstoneDusts", 1, 3), sunnarium, 3, 491520);
            // Preserve the old loader's literal EU/t, which differs from the modern UHV recipe voltage.
            addMolecularRecipe(getModItem(Mods.GalaxySpace.ID, "item.GlowstoneDusts", 1, 4), sunnarium, 1, 1996080);
        }
    }

    private static void addMolecularRecipe(ItemStack input, ItemStack output, int duration, long eut) {
        if (input == null || output == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(input.copy())
            .itemOutputs(output.copy())
            .duration(duration)
            .eut(eut)
            .addTo(GTCMRecipe.HyperSpacetimeTransformerRecipeMap);
    }
}
// spotless:on
