package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gregtech.api.enums.Mods;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class AuraCoreRecipes {

    public static void loadRecipes() {
        if (!Mods.Gadomancy.isModLoaded()) return;

        // Thaumcraft aspect registration order; Gadomancy core metadata uses a different order.
        Aspect[] aspects = { Aspect.AIR, Aspect.EARTH, Aspect.FIRE, Aspect.WATER, Aspect.ORDER, Aspect.ENTROPY };
        int[] coreMetadata = { 1, 4, 2, 3, 5, 6 };
        String researchKey = "GADOMANCY.CLEAN_AURA_CORE";
        List<CrucibleRecipe> duplicationRecipes = new ArrayList<>();
        List<CrucibleRecipe> conversionRecipes = new ArrayList<>();
        for (int target = 0; target < aspects.length; target++) {
            duplicationRecipes.add(
                ThaumcraftApi.addCrucibleRecipe(
                    researchKey,
                    getModItem(Mods.Gadomancy.ID, "ItemAuraCore", 2, coreMetadata[target]),
                    getModItem(Mods.Gadomancy.ID, "ItemAuraCore", 1, coreMetadata[target]),
                    new AspectList().add(aspects[target], 128)
                        .add(Aspect.MAGIC, 64)
                        .add(Aspect.AURA, 32)
                        .add(Aspect.LIFE, 32)));
            conversionRecipes.add(
                ThaumcraftApi.addCrucibleRecipe(
                    researchKey,
                    getModItem(Mods.Gadomancy.ID, "ItemAuraCore", 1, coreMetadata[target]),
                    getModItem(
                        Mods.Gadomancy.ID,
                        "ItemAuraCore",
                        1,
                        coreMetadata[(target + aspects.length - 1) % aspects.length]),
                    new AspectList().add(aspects[target], 256)
                        .add(Aspect.EXCHANGE, 32)
                        .add(Aspect.MAGIC, 12)
                        .add(Aspect.HEAL, 18)));
        }

        ResearchItem research = ResearchCategories.getResearch(researchKey);
        if (research != null) {
            List<ResearchPage> pages = new ArrayList<>(Arrays.asList(research.getPages()));
            pages.add(new ResearchPage(duplicationRecipes.toArray(new CrucibleRecipe[0])));
            pages.add(new ResearchPage(conversionRecipes.toArray(new CrucibleRecipe[0])));
            research.setPages(pages.toArray(new ResearchPage[0]));
        }
    }
}
