package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.NoSeedInController;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.ArtificialGreenHouseFakeRecipe;
import com.gtnewhorizon.cropsnh.init.CropsNHFluids;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

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
    public EcoSphereModeResult process(TST_EcoSphereSimulator machine, int euTier) {
        // Build the crop cache once from the first valid seed in the input buses.
        if (!findSeed(machine)) return EcoSphereModeResult.failure(NoSeedInController);
        if (!machine.cropsNHFarm.isValid())
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);

        boolean hybridSeed = machine.cropsNHFarm.seedData != null;
        if (hybridSeed && machine.getModeBeaconTier() < 2) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        long baseFertilizerCost = hybridSeed ? ArtificialGreenHouseFakeRecipe.HYBRID_SEED_FERTILIZER_PER_PARALLEL
            : ArtificialGreenHouseFakeRecipe.NORMAL_SEED_FERTILIZER_PER_PARALLEL;
        long fertilizerPerParallel = machine.applyStructureFluidDiscount(baseFertilizerCost);
        FluidStack fertilizerInput = EcoSphereModeSupport
            .findFirstValidFluid(machine, fluid -> fluid == CropsNHFluids.enrichedFertilizer);
        if (fertilizerInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, fertilizerInput.getFluid(), fertilizerPerParallel, euTier);
        // The lowest power tier runs two parallels, so startup requires twice the per-parallel fertilizer.
        if (parallelResult == null) return EcoSphereModeResult.failure(
            EcoSphereModeSupport.missingFluid(machine, CropsNHFluids.enrichedFertilizer, fertilizerPerParallel * 2));

        return EcoSphereModeResult.standard(
            CheckRecipeResultRegistry.SUCCESSFUL,
            machine.cropsNHFarm.getOutputStacks(parallelResult.multiplier()),
            parallelResult.tier());
    }

    private static boolean findSeed(TST_EcoSphereSimulator machine) {
        for (ItemStack input : machine.getStoredInputs()) {
            if (input != null && input.getItem() != null && machine.cropsNHFarm.createCropCache(input)) return true;
        }
        return false;
    }

}
