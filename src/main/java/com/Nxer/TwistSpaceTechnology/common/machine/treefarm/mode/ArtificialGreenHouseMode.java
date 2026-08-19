package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.NoSeedInController;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.ArtificialGreenHouseFakeRecipe;

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
        List<CropsNHFarm.CropCache> crops = findCrops(machine);
        if (crops.isEmpty()) return EcoSphereModeResult.failure(NoSeedInController);
        long baseFertilizerCost = 0;
        for (CropsNHFarm.CropCache crop : crops) {
            if (crop.hybrid()) {
                if (machine.getModeBeaconTier() < 2) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);
                baseFertilizerCost += ArtificialGreenHouseFakeRecipe.HYBRID_SEED_FERTILIZER_PER_PARALLEL;
            } else {
                baseFertilizerCost += ArtificialGreenHouseFakeRecipe.NORMAL_SEED_FERTILIZER_PER_PARALLEL;
            }
        }

        FluidStack fertilizerInput = EcoSphereFluidCache.findFirstValidFluid(machine);
        if (fertilizerInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        Function<EcoSphereModeSupport.ParallelResult, EcoSphereModeResult> processor = parallelResult -> {
            List<ItemStack> outputs = new ArrayList<>();
            for (CropsNHFarm.CropCache crop : crops) {
                Collections.addAll(outputs, crop.getOutputStacks(parallelResult.parallel()));
            }
            if (outputs.isEmpty()) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
            return EcoSphereModeResult.standard(
                CheckRecipeResultRegistry.SUCCESSFUL,
                outputs.toArray(new ItemStack[0]),
                parallelResult.tier());
        };
        return EcoSphereModeSupport
            .processModeRecipeWithTier(machine, fertilizerInput.getFluid(), baseFertilizerCost, euTier, processor);
    }

    private static List<CropsNHFarm.CropCache> findCrops(TST_EcoSphereSimulator machine) {
        List<CropsNHFarm.CropCache> crops = new ArrayList<>();
        for (ItemStack input : machine.getModeInputs()) {
            CropsNHFarm.CropCache crop = machine.cropsNHFarm.getCropCache(input);
            if (crop != null) crops.add(crop);
        }
        return crops;
    }

}
