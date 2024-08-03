package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

public class TCRecipeTools {

    public static ArrayList<InfusionCraftingRecipe> ICR = new ArrayList<>();// InfusionCraftingRecipeList
    public static ArrayList<TCCrucibleRecipe> CR = new ArrayList<>();

    public TCRecipeTools() {}

    public static void getCrucibleRecipe() {
        for (Object r : ThaumcraftApi.getCraftingRecipes()) {
            if ((r instanceof CrucibleRecipe recipe && recipe.getRecipeOutput() != null)) {
                TCCrucibleRecipe c = new TCCrucibleRecipe(
                    recipe.key,
                    recipe.getRecipeOutput(),
                    recipe.catalyst,
                    recipe.aspects);
                CR.add(c);
            }
        }
    }

    public static void getInfusionCraftingRecipe() {
        for (Object r : ThaumcraftApi.getCraftingRecipes()) {
            if (!(r instanceof InfusionRecipe recipe)) {
                continue;
            }

            if ((recipe.getRecipeOutput() instanceof ItemStack
                && ((ItemStack) recipe.getRecipeOutput()).getItem() != null
                && recipe.getRecipeInput() != null)) {
                InfusionCraftingRecipe y = new InfusionCraftingRecipe(
                    recipe.getRecipeInput(), // getItemInput
                    recipe.getRecipeOutput(), // getItemOutput
                    recipe.getComponents(), // getRecipeItemInput
                    recipe.getAspects(), // getAspects
                    recipe.getResearch());// getResearch
                ICR.add(y);
            }
        }

    }

    public static void setAspects(ItemStack itemstack, AspectList aspects) {
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }

        aspects.writeToNBT(itemstack.getTagCompound());
    }

    public static class InfusionCraftingRecipe {

        private final ItemStack InputItem;
        private final ItemStack OutputItem;
        private final AspectList InputAspects;
        private final String Research;
        private final ItemStack[] Components;

        public InfusionCraftingRecipe(ItemStack InputItem, Object OutputItem, ItemStack[] Components,
            AspectList InputAspects, String research) {
            this.InputItem = InputItem;
            this.OutputItem = (ItemStack) OutputItem;
            this.InputAspects = InputAspects;
            this.Research = research;
            this.Components = Components;
        }

        public ItemStack[] getComponents() {
            return Components;
        }

        public ItemStack[] getInputItem() {
            ItemStack[] Input = new ItemStack[Components.length + 1];
            Input[0] = InputItem;
            int index = 1;
            for (ItemStack itemStack : Components) {
                Input[index] = itemStack;
                index++;
            }
            return Input;
        }

        public ItemStack getOutput() {
            return OutputItem;
        }

        public AspectList getInputAspects() {
            return InputAspects;
        }

        public String getResearch() {
            return Research;
        }

        public int getAspectAmount(Aspect aspect) {
            return this.InputAspects.getAmount(aspect);
        }

        public int getAspectAmount() {
            int i = 0;
            for (Aspect aspect : this.InputAspects.getAspects()) {
                i += this.InputAspects.getAmount(aspect);
            }
            return i;
        }

    }

    public static class TCCrucibleRecipe {

        private final ItemStack recipeOutput;
        private Object catalyst;
        private final AspectList aspects;
        private final String key;

        public TCCrucibleRecipe(String researchKey, ItemStack result, Object cat, AspectList tags) {
            this.recipeOutput = result;
            this.aspects = tags;
            this.key = researchKey;
            this.catalyst = cat;
            if (cat instanceof String) {
                this.catalyst = OreDictionary.getOres((String) cat);
            }
        }

        public Object getCatalyst() {
            return catalyst;
        }

        public AspectList getAspects() {
            return aspects;
        }

        public ItemStack getRecipeOutput() {
            return recipeOutput;
        }

        public String getKey() {
            return key;
        }
    }
}
