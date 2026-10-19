package com.Nxer.TwistSpaceTechnology.nei;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.Tags;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

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
        BottledChaosNEIHandler.register();
        BottledChaosStructureNEIHandler.register();

        // NEI loads its ordering file before loading plugins; these entries only change the in-memory order.
        // spotless:off
        String[] pages = {
            "blockrenderer6343.integration.gregtech.GTNEIMultiblockHandler",
            GTCMRecipe.BottledChaosCredentialRecipes.unlocalizedName,
            GTCMRecipe.BottledChaosStructureRecipes.unlocalizedName,
            GTCMRecipe.BottledChaosTitaniumCategory.unlocalizedName,
            GTCMRecipe.BottledChaosTungstenCategory.unlocalizedName,
            GTCMRecipe.BottledChaosIndiumCategory.unlocalizedName,
            GTCMRecipe.BottledChaosPlatinumCategory.unlocalizedName,
            GTCMRecipe.BottledChaosLanthanideCategory.unlocalizedName,
            GTCMRecipe.BottledChaosNaquadahCategory.unlocalizedName,
            GTCMRecipe.BottledChaosNetheriteCategory.unlocalizedName,
            GTCMRecipe.BottledChaosIsaMillCategory.unlocalizedName,
            GTCMRecipe.BottledChaosPrismaticCategory.unlocalizedName,
            GTCMRecipe.BottledChaosPurifiedWaterCategory.unlocalizedName,
            GTCMRecipe.BottledChaosKevlarCategory.unlocalizedName,
            GTCMRecipe.BottledChaosRadoxCategory.unlocalizedName,
            GTCMRecipe.BottledChaosBiologicalCategory.unlocalizedName,
            GTCMRecipe.BottledChaosRecipes.unlocalizedName
        };
        // spotless:on
        for (int index = 0; index < pages.length; index++) {
            NEIClientConfig.handlerOrdering.put(pages[index], -1000 + index);
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
