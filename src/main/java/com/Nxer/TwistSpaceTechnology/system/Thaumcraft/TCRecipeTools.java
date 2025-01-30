package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import java.util.ArrayList;
import java.util.HashMap;

import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.util.GTOreDictUnificator;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

public class TCRecipeTools {

    public static ArrayList<InfusionCraftingRecipe> ICR = new ArrayList<>();// InfusionCraftingRecipeList

    public static HashMap<String, ArrayList<CrucibleCraftingRecipe>> CCR = new HashMap<>();// CrucibleCraftingRecipeMap

    public static String toStringWithoutStackSize(ItemStack itemStack) {
        return itemStack.getUnlocalizedName() + "@" + itemStack.getItemDamage();
    }

    public TCRecipeTools() {}

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

    public static void getCrucibleCraftingRecipe() {
        for (var o : ThaumcraftApi.getCraftingRecipes()) {
            if (!(o instanceof CrucibleRecipe o1)) continue;
            if (GTUtility.isStackValid(o1.getRecipeOutput())) {
                ItemStack input;
                Object cat = o1.catalyst;
                if (cat instanceof ArrayList<?>catalyst1) {
                    input = GTOreDictUnificator.get(false, (ItemStack) catalyst1.get(0), true);
                } else if (cat instanceof ItemStack itemStack) {
                    input = TstUtils.copyAmountUnlimited(1, itemStack);
                } else continue;
                String inputKey = null;
                if (input != null) {
                    inputKey = toStringWithoutStackSize(input);
                } else {
                    TwistSpaceTechnology.LOG.info("input is null when getting CrucibleCraftingRecipe");
                }
                CrucibleCraftingRecipe p = new CrucibleCraftingRecipe(
                    input,
                    o1.getRecipeOutput()
                        .copy(),
                    o1.aspects,
                    o1.key);
                CCR.computeIfAbsent(inputKey, K -> new ArrayList<>())
                    .add(p);
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

    @SuppressWarnings("unused")
    public static class CrucibleCraftingRecipe {

        private final Object InputItem;
        private final ItemStack OutputItem;
        private final AspectList InputAspects;
        private final String Research;

        public CrucibleCraftingRecipe(Object inputItem, ItemStack outputItem, AspectList inputAspects,
            String research) {
            InputItem = inputItem;
            OutputItem = outputItem;
            InputAspects = inputAspects;
            Research = research;
        }

        public Object getInputItem() {
            return InputItem;
        }

        public ItemStack getOutputItem() {
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
