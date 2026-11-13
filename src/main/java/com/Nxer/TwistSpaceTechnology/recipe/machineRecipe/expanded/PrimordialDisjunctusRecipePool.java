package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.PrimordialDisjunctusCycleKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.PrimordialDisjunctusCycleKey.Cycle;
import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;

import goodgenerator.loader.Loaders;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.util.OverclockCalculator;
import thaumcraft.api.aspects.Aspect;

/** A reference cycle for NEI; the machine continues to generate essentia through its own processing logic. */
public final class PrimordialDisjunctusRecipePool {

    private PrimordialDisjunctusRecipePool() {}

    public static void loadRecipes() {
        // Four F positions are occupied by diffusion cells. Mixed tiers sum their individual parallel values.
        for (int voltageTier = 4; voltageTier <= 12; voltageTier++) {
            for (int cellTier = 0; cellTier < 4; cellTier++) {
                registerCycle(cellTier, voltageTier, 0);
                if (ValueEnum.BoostGainMultiplier_PrimordialDisjunctus > 0
                    && ValueEnum.BoostMultiplier_PrimordialDisjunctus > 0) {
                    registerCycle(cellTier, voltageTier, 640);
                }
            }
        }
    }

    private static void registerCycle(int cellTier, int voltageTier, int perditioVis) {
        int parallel = 4 * (ValueEnum.DiffusionCellParallel_PrimordialDisjunctus << cellTier);
        int boost = (int) (perditioVis * 0.025 * ValueEnum.BoostGainMultiplier_PrimordialDisjunctus);
        // Match checkProcessing, including both integer truncations and the one-second output conversion.
        int amount = (int) (parallel * ValueEnum.PrimalAspectsPerParallel_PrimordialDisjunctus
            * (1.0 + boost * ValueEnum.BoostMultiplier_PrimordialDisjunctus));
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(TierEU.RECIPE_EV)
            .setEUt(GTValues.V[voltageTier])
            .setDuration(20 * ValueEnum.BaseRecipeDuration_PrimordialDisjunctus)
            .setDurationDecreasePerOC(2)
            .calculate();
        int overclockTicks = calculator.getDuration();
        if (overclockTicks < 20) amount = (int) ((20.0 / overclockTicks) * amount);
        Aspect[] primals = { Aspect.AIR, Aspect.EARTH, Aspect.FIRE, Aspect.WATER, Aspect.ORDER, Aspect.ENTROPY };
        ItemStack[] outputs = new ItemStack[primals.length];
        for (int i = 0; i < primals.length; i++) {
            outputs[i] = ItemEssentiaHelper.createAspectItem(primals[i], amount);
            outputs[i].setItemDamage(1);
        }
        ItemStack cells = new ItemStack(Loaders.essentiaCell, 4, cellTier);
        GTValues.RA.stdBuilder()
            .itemInputs(cells)
            .itemOutputs(outputs)
            .duration(Math.max(20, overclockTicks))
            .eut(calculator.getConsumption())
            .metadata(
                PrimordialDisjunctusCycleKey.INSTANCE,
                new Cycle(cellTier, voltageTier, parallel, perditioVis, boost, overclockTicks))
            .ignoreCollision()
            .fake()
            .addTo(GTCMRecipe.PrimordialDisjunctusVisualRecipeMap);
    }
}
