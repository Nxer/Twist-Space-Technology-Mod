package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;


import java.util.LinkedList;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class BOPRecipePool extends RecipeElvenTrade{


//    private ItemStack inputItem;
	public BOPRecipePool(ItemStack input, ItemStack output) {
		super(input,output);
//		inputItem = input;
	}

	// @Override
	// public boolean matches(List<ItemStack> stacks, boolean removeMatched) {
	// 	List<ItemStack> matchedStacks = new LinkedList<ItemStack>();
	// 	boolean found = false;
		
	// 	for (ItemStack inStack : stacks) {
	// 		if (inStack == null) {
	// 			continue;
	// 		}
			
	// 		if (inStack==inputItem) {
	// 			matchedStacks.add(inStack);
	// 			found = true;
	// 			break;
	// 		}
	// 	}

	// 	if (removeMatched) {
	// 		for (ItemStack stack : matchedStacks) {
	// 			stacks.remove(stack);
	// 		}
	// 	}
		
	// 	return found;
	// }
}