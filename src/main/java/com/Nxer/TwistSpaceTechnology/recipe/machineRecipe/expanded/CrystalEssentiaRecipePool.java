package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.util.AspectLevelCalculator.BASE_DURATION;
import static com.Nxer.TwistSpaceTechnology.util.AspectLevelCalculator.computeAspectSynthesisTime;
import static com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper.createCrystal;
import static gregtech.api.enums.TierEU.RECIPE_EV;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IRecipeMap;
import thaumcraft.api.aspects.Aspect;

public class CrystalEssentiaRecipePool {

    public static void loadRecipes() {
        final IRecipeMap tower = GTCMRecipe.SkypiercerTower;

        for (Aspect aspect : Aspect.aspects.values()) {
            Aspect[] comps = aspect.getComponents();
            if (comps != null && comps.length == 2) {
                registerCrystallizedRecipe(tower, comps[0], comps[1], aspect);
            }
        }
    }

    private static void registerCrystallizedRecipe(IRecipeMap map, Aspect compA, Aspect compB, Aspect result) {
        ItemStack inputA = createCrystal(compA).copy();
        ItemStack inputB = createCrystal(compB).copy();
        ItemStack output = createCrystal(result).copy();

        GTValues.RA.stdBuilder()
            .ignoreCollision()
            .itemInputs(inputA, inputB)
            .itemOutputs(output)
            .duration(10 * BASE_DURATION * computeAspectSynthesisTime(result))
            .eut(RECIPE_EV)
            .addTo(map);

    }

}
