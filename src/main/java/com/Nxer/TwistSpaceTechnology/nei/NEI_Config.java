package com.Nxer.TwistSpaceTechnology.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.Tags;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipePage;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.IUsageHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
        if (Loader.isModLoaded("blockrenderer6343")) BottledChaosPrototypePreview.register();

        // NEI loads its ordering file before loading plugins; these entries only change the in-memory order.
        // Only order TST pages; external handlers retain their configured priorities.
        List<String> pages = new ArrayList<>();
        for (int tag : CredentialModel.displayOrder()) {
            BottledChaosRecipePage page = CredentialModel.displayPage(tag);
            if (page == BottledChaosRecipePage.DEFAULT) continue;
            String name = page.category().unlocalizedName;
            if (!pages.contains(name)) pages.add(name);
        }
        pages.add(GTCMRecipe.BottledChaosRecipes.unlocalizedName);
        for (int index = 0; index < pages.size(); index++) {
            NEIClientConfig.handlerOrdering.put(pages.get(index), -997 + index);
        }
        // GTNH puts normal processing at <= 30 and recycling at 40.
        NEIClientConfig.handlerOrdering.put(GTCMRecipe.BottledChaosCredentialRecipes.unlocalizedName, 35);
        NEIClientConfig.handlerOrdering.put(GTCMRecipe.BottledChaosStructureRecipes.unlocalizedName, 36);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!(event.gui instanceof GuiUsageRecipe gui)) return;
        boolean controllerQuery = gui.currenthandlers.stream()
            .anyMatch(
                handler -> handler instanceof BottledChaosStructureNEIHandler structure
                    && structure.isControllerQuery());
        if (!controllerQuery) return;

        // GuiOpenEvent precedes NEI's initial tab selection. Only this controller query is reordered;
        // handler identities, global priorities and other open/history screens remain unchanged.
        moveFirst(gui.currenthandlers, GTCMRecipe.BottledChaosStructureRecipes.unlocalizedName);
        moveFirst(gui.currenthandlers, GTCMRecipe.BottledChaosCredentialRecipes.unlocalizedName);
    }

    private static void moveFirst(List<IUsageHandler> handlers, String overlayId) {
        for (int index = 0; index < handlers.size(); index++) {
            if (overlayId.equals(
                handlers.get(index)
                    .getOverlayIdentifier())) {
                handlers.add(0, handlers.remove(index));
                return;
            }
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
