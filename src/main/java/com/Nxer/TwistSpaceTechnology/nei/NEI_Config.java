package com.Nxer.TwistSpaceTechnology.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.Tags;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipePage;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_Config implements IConfigureNEI {

    public static boolean isAdded = true;

    public static void hide(Block aBlock) {
        API.hideItem(new ItemStack(aBlock, 1));
    }

    public static void hide(Item aItem) {
        API.hideItem(new ItemStack(aItem, 1));
    }

    @Override
    public void loadConfig() {
        isAdded = true;

        hide(TstBlocks.BlockLargeSolarBoilerRender);
        NEICollapsibleItems.load();
        EssentiaNEIHandler.register();
        BottledChaosNEIHandler.register();
        BottledChaosStructureNEIHandler.register();

        // NEI loads its ordering file before loading plugins; these entries only change the in-memory order.
        List<String> pages = new ArrayList<>();
        pages.add("blockrenderer6343.integration.gregtech.GTNEIMultiblockHandler");
        pages.add(GTCMRecipe.BottledChaosCredentialRecipes.unlocalizedName);
        pages.add(GTCMRecipe.BottledChaosStructureRecipes.unlocalizedName);
        for (int tag : CredentialModel.displayOrder()) {
            BottledChaosRecipePage page = CredentialModel.displayPage(tag);
            if (page == BottledChaosRecipePage.DEFAULT) continue;
            String name = page.category().unlocalizedName;
            if (!pages.contains(name)) pages.add(name);
        }
        pages.add(GTCMRecipe.BottledChaosRecipes.unlocalizedName);
        for (int index = 0; index < pages.size(); index++) {
            NEIClientConfig.handlerOrdering.put(pages.get(index), -1000 + index);
        }
    }

    @Override
    public String getName() {
        return Tags.MODNAME + " NEI Plugin";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }
}
