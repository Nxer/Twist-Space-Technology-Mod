package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.function.Supplier;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import gregtech.api.recipe.RecipeCategory;

/** Page identity, NEI routing and compact credential naming, without eager recipe-map initialization. */
public enum BottledChaosRecipePage {

    // spotless:off
    DEFAULT(() -> GTCMRecipe.BottledChaosRecipes.getDefaultRecipeCategory()),
    TITANIUM(() -> GTCMRecipe.BottledChaosTitaniumCategory),
    TUNGSTEN(() -> GTCMRecipe.BottledChaosTungstenCategory),
    INDIUM(() -> GTCMRecipe.BottledChaosIndiumCategory),
    PLATINUM(() -> GTCMRecipe.BottledChaosPlatinumCategory, "tst.bottledchaos.route_group.platinum"),
    LANTHANIDE(() -> GTCMRecipe.BottledChaosLanthanideCategory, "tst.bottledchaos.route_group.lanthanide"),
    NAQUADAH(() -> GTCMRecipe.BottledChaosNaquadahCategory),
    NAQUADAH_FUEL(() -> GTCMRecipe.BottledChaosNaquadahFuelCategory),
    FUSION(() -> GTCMRecipe.BottledChaosFusionCategory, true),
    NETHERITE(() -> GTCMRecipe.BottledChaosNetheriteCategory),
    ISA_MILL(() -> GTCMRecipe.BottledChaosIsaMillCategory),
    PRISMATIC(() -> GTCMRecipe.BottledChaosPrismaticCategory),
    PURIFIED_WATER(() -> GTCMRecipe.BottledChaosPurifiedWaterCategory),
    KEVLAR(() -> GTCMRecipe.BottledChaosKevlarCategory),
    RADOX(() -> GTCMRecipe.BottledChaosRadoxCategory),
    BIOLOGICAL(() -> GTCMRecipe.BottledChaosBiologicalCategory);
    // spotless:on

    private final Supplier<RecipeCategory> category;
    private final String summaryKey;
    private final boolean keepExpanded;

    BottledChaosRecipePage(Supplier<RecipeCategory> category) {
        this(category, null, false);
    }

    BottledChaosRecipePage(Supplier<RecipeCategory> category, String summaryKey) {
        this(category, summaryKey, false);
    }

    BottledChaosRecipePage(Supplier<RecipeCategory> category, boolean keepExpanded) {
        this(category, null, keepExpanded);
    }

    BottledChaosRecipePage(Supplier<RecipeCategory> category, String summaryKey, boolean keepExpanded) {
        this.category = category;
        this.summaryKey = summaryKey;
        this.keepExpanded = keepExpanded;
    }

    public RecipeCategory category() {
        return category.get();
    }

    public boolean keepExpanded() {
        return keepExpanded;
    }

    public String groupKey(String tag) {
        return this == DEFAULT ? tag : name();
    }

    public String summaryKey(String tagTranslationKey) {
        return summaryKey == null ? tagTranslationKey : summaryKey;
    }
}
