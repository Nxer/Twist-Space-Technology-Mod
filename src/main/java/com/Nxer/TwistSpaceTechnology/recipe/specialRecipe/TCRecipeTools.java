package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

public class TCRecipeTools {

    public static ArrayList<InfusionCraftingRecipe> ICR = new ArrayList<>();// InfusionCraftingRecipeList

    public TCRecipeTools() {}

    public static void getInfusionCraftingRecipe() {
        for (Object r : ThaumcraftApi.getCraftingRecipes()) {
            if (r instanceof InfusionRecipe && ((InfusionRecipe) r).getRecipeOutput() instanceof ItemStack
                && ((InfusionRecipe) r).getRecipeInput() != null) // GetInfusionCraftingRecipe
            {
                InfusionCraftingRecipe y = new InfusionCraftingRecipe(
                    ((InfusionRecipe) r).getRecipeInput(), // getItemInput
                    ((InfusionRecipe) r).getRecipeOutput(), // getItemOutput
                    ((InfusionRecipe) r).getComponents(), // getRecipeItemInput
                    ((InfusionRecipe) r).getAspects(), // getAspects
                    ((InfusionRecipe) r).getResearch());// getResearch
                ICR.add(y);
            }
        }

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
}
