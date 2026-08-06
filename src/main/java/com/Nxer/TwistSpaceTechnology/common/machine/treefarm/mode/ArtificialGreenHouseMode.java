package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.NoSeedInController;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.NotEnoughWater;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.ArtificialGreenHouseFakeRecipe;
import com.gtnewhorizon.cropsnh.init.CropsNHFluids;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class ArtificialGreenHouseMode implements IEcoSphereMode {

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.ArtificialGreenHouseFakeRecipes;
    }

    @Override
    public String getDisplayName() {
        return translateToLocal("EcoSphereSimulator.modeMsg.2");
    }

    @Override
    public EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier) {
        machine.fertilizerToConsume = 0;
        if (!findSeed(machine)) return EcoSphereModeResult.failure(NoSeedInController);
        if (!machine.cropsNHFarm.isValid())
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);

        boolean hybridSeed = machine.cropsNHFarm.seedData != null;
        if (hybridSeed && !machine.isTierTwo())
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("mega_tree_farm_tier_two_required"));

        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport.consumeFluidForParallel(
            machine,
            CropsNHFluids.enrichedFertilizer,
            hybridSeed ? ArtificialGreenHouseFakeRecipe.HYBRID_SEED_FERTILIZER_PER_PARALLEL
                : ArtificialGreenHouseFakeRecipe.NORMAL_SEED_FERTILIZER_PER_PARALLEL,
            euTier);
        if (parallelResult == null) return EcoSphereModeResult.failure(NotEnoughWater);

        machine.fertilizerToConsume = parallelResult.fluidCost();
        return EcoSphereModeResult.standard(
            CheckRecipeResultRegistry.SUCCESSFUL,
            machine.cropsNHFarm.getOutputStacks(parallelResult.multiplier()),
            parallelResult.tier());
    }

    private static boolean findSeed(TST_MegaTreeFarm machine) {
        for (ItemStack input : machine.getStoredInputs()) {
            if (input != null && input.getItem() != null && machine.cropsNHFarm.createCropCache(input)) return true;
        }
        return false;
    }

}
