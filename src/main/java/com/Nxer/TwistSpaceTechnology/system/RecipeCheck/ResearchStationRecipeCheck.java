package com.Nxer.TwistSpaceTechnology.system.RecipeCheck;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.MODID;
import static tectech.recipe.TecTechRecipeMaps.researchStationFakeRecipes;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.ModContainer;
import gregtech.api.util.GTRecipe;

public class ResearchStationRecipeCheck {

    public static void checkDuplicates() {
        Collection<GTRecipe> allRecipes = researchStationFakeRecipes.getAllRecipes();

        HashSet<String> otherRecipeSet = new HashSet<>();
        HashSet<String> selfRecipeSet = new HashSet<>();

        for (GTRecipe recipe : allRecipes) {
            ItemStack[] mInputs = recipe.mInputs;
            if (mInputs.length == 0) {
                continue;
            }

            ItemStack input = mInputs[0];

            String item = input.getItem()
                .getUnlocalizedName() + " "
                + input.getItemDamage();

            boolean owned = false;
            if (recipe.owners != null) {
                for (ModContainer owner : recipe.owners) {
                    if (owner.getModId()
                        .equals(MODID)) {
                        if (selfRecipeSet.contains(item)) {
                            LOG.warn("duplicate research station recipe found: {}", item);
                        } else {
                            selfRecipeSet.add(item);
                        }
                        owned = true;
                    }
                }
            }

            if (!owned) {
                otherRecipeSet.add(item);
            }
        }

        selfRecipeSet.retainAll(otherRecipeSet);

        for (String item : selfRecipeSet) {
            LOG.warn("duplicate research station recipe found: {}", item);
        }
    }
}
