package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static fox.spiteful.avaritia.items.LudicrousItems.bigPearl;
import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static thaumcraft.common.config.ConfigBlocks.blockCosmeticSolid;
import static thaumcraft.common.config.ConfigItems.itemEssence;
import static thaumcraft.common.config.ConfigItems.itemJarNode;
import static thaumcraft.common.config.ConfigItems.itemShard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.IndustrialMagicMatrixRecipeIndexKey;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TSTArrayUtils;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import thaumcraft.api.aspects.AspectList;

public class IndustrialMagicMatrixRecipePool {

    protected Collection<TST_ItemID> itemsUnconsumed = new HashSet<>();
    protected Map<TST_ItemID, Integer> recipeSeparationMap = new HashMap<>();

    protected void prepare() {
        itemsUnconsumed.add(TST_ItemID.create(new ItemStack(bigPearl)));

        recipeSeparationMap.put(TST_ItemID.create(blockCosmeticSolid), 11);
    }

    /**
     * Turn input item list to correct items for IMM recipe.
     *
     * @param origin Itemstacks from TC Infusion Matrix recipe.
     * @return Items for IMM recipes.
     */
    protected ItemStack[] checkInputSpecial(ItemStack... origin) {
        for (int i = 0; i < origin.length; i++) {

            // check something should be set a unconsumed tag
            if (itemsUnconsumed.contains(TST_ItemID.create(origin[i]))) {
                origin[i].stackSize = 0;
            }

            // check gt materials
            ItemStack g = GTOreDictUnificator.get(origin[i]);
            if (g != null) {
                origin[i] = g;
            }

        }
        return origin;
    }

    protected Set<Item> skips;

    protected boolean shouldSkip(Item item) {
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

    public void loadRecipes() {
        TCRecipeTools.getInfusionCraftingRecipe();
        prepare();

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
            setAspects(Essence, Recipe.getInputAspects());

            GTValues.RA.stdBuilder()
                .ignoreCollision()
                .clearInvalid()
                .special(Essence)
                .metadata(IndustrialMagicMatrixRecipeIndexKey.INSTANCE, i)
                .itemInputs(lateCheck(TST_ItemID.create(Recipe.getOutput()), checkInputSpecial(Recipe.getInputItem())))
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

    public ItemStack[] lateCheck(TST_ItemID output, ItemStack... inputs) {
        if (recipeSeparationMap.containsKey(output)) {
            int tag = recipeSeparationMap.get(output);

            return TSTArrayUtils.concatToLast(ItemStack.class, inputs, GTUtility.getIntegratedCircuit(tag));

        }

        return inputs;
    }

    public void setAspects(ItemStack itemstack, AspectList aspects) {
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }

        aspects.writeToNBT(itemstack.getTagCompound());
    }

}
