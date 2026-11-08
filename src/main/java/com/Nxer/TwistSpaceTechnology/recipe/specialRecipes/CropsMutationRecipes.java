package com.Nxer.TwistSpaceTechnology.recipe.specialRecipes;

import static com.Nxer.TwistSpaceTechnology.common.cropsNH.Crops.PurpleMagnolia;
import static com.gtnewhorizon.cropsnh.api.CropsNHCrops.ManaBean;
import static com.gtnewhorizon.cropsnh.api.CropsNHCrops.Shimmerleaf;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original.CropsNHRecipePool;
import com.gtnewhorizon.cropsnh.farming.mutation.CropMutation;

import gregtech.api.enums.Mods;

public class CropsMutationRecipes {

    public static void loadRecipes() {
        if (Mods.Thaumcraft.isModLoaded() && Mods.Botania.isModLoaded()) {
            // Cross Mana Bean and Shimmerleaf to obtain Purple Magnolia.
            CropMutation mutation = new CropMutation(PurpleMagnolia, ManaBean, Shimmerleaf);
            mutation.register();
            CropsNHRecipePool.loadPurpleMagnoliaRecipes(mutation);
        }
    }
}
