package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static fox.spiteful.avaritia.items.LudicrousItems.bigPearl;
import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticSolid;
import static thaumcraft.common.config.ConfigItems.itemEssence;
import static thaumcraft.common.config.ConfigItems.itemJarNode;
import static thaumcraft.common.config.ConfigItems.itemShard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.IndustrialMagicMatrixRecipeIndexKey;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import thaumcraft.common.items.ItemEssence;

public class IndustrialMagicMatrixRecipePool {

    protected static ItemStack[] itemsUnconsumed = new ItemStack[] { new ItemStack(bigPearl) };

    protected static ItemStack[] checkInputSpecial(ItemStack... itemStacks) {
        baseLoop: for (ItemStack i : itemStacks) {
            for (ItemStack u : itemsUnconsumed) {
                if (GTUtility.areStacksEqual(i, u)) {
                    i.stackSize = 0;
                    break baseLoop;
                }
            }
        }
        return itemStacks;
    }

    protected static Set<Item> skips;

    protected static boolean shouldSkip(Item item) {
        if (null == skips) {
            skips = new HashSet<>();
            skips.add(itemJarNode);
            if (Mods.ThaumicBases.isModLoaded()) {
                Item revolver = GameRegistry.findItem(Mods.ThaumicBases.ID, "revolver");
                if (null != revolver) {
                    skips.add(revolver);
                }
            }
            if (Mods.Gadomancy.isModLoaded()) {
                Item itemEtherealFamiliar = GameRegistry.findItem(Mods.Gadomancy.ID, "ItemEtherealFamiliar");
                if (null != itemEtherealFamiliar) {
                    skips.add(itemEtherealFamiliar);
                }
            }
        }

        return skips.contains(item);
    }

    public static void loadRecipes() {
        TCRecipeTools.getInfusionCraftingRecipe();

        final IRecipeMap IIM = GTCMRecipe.IndustrialMagicMatrixRecipe;
        ArrayList<TCRecipeTools.InfusionCraftingRecipe> icr = TCRecipeTools.ICR;
        for (int i = 0; i < icr.size(); i++) {
            TCRecipeTools.InfusionCraftingRecipe Recipe = icr.get(i);
            if (shouldSkip(
                Recipe.getOutput()
                    .getItem()))
                continue;

            ItemStack Essence = new ItemStack(itemEssence);
            Essence.setItemDamage(1);
            // #tr IndustrialMagicMatrixRecipeInputAspects
            // # Recipe required Essentia
            // #zh_CN 配方所需源质
            Essence.setStackDisplayName(TextEnums.tr("IndustrialMagicMatrixRecipeInputAspects"));
            new ItemEssence().setAspects(Essence, Recipe.getInputAspects());
            GTValues.RA.stdBuilder()
                .ignoreCollision()
                .clearInvalid()
                .special(Essence)
                .metadata(IndustrialMagicMatrixRecipeIndexKey.INSTANCE, i)
                .itemInputs(checkInputSpecial(Recipe.getInputItem()))
                .itemOutputs((Recipe.getOutput()))
                .fluidInputs()
                .fluidOutputs()
                .duration(200 + Recipe.getInputItem().length * 20 + Math.min(Recipe.getAspectAmount(), 600))
                .eut(RECIPE_LuV)
                .addTo(IIM);
        }

        GTValues.RA.stdBuilder()
            .clearInvalid()
            .itemInputs(new ItemStack(blockCosmeticSolid, 8, 6), new ItemStack(itemShard, 1, 6))
            .itemOutputs(new ItemStack(ModBlocksHandler.BlockCrystalDeep.getLeft(), 8))
            .fluidInputs(Materials.Thaumium.getMolten(144))
            .duration(20)
            .eut(RECIPE_LV)
            .addTo(RecipeMaps.assemblerRecipes);
    }

}
