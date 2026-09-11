package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.NoSeedInController;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.CropsNHFarm;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.ArtificialGreenHouseFakeRecipe;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class ArtificialGreenHouseMode implements IEcoSphereMode {

    // Applied after crop-specific growth and drop calculations, before shared upgrade and random output scaling.
    private static final double OUTPUT_SCALE = 40.0d;

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
                // Cached yields already include environmental growth progress and the non-hybrid efficiency penalty.
                Collections.addAll(
                    outputs,
                    crop.getOutputStacks(parallelResult.parallel() * (double) crop.seedCount() * OUTPUT_SCALE));
            }
            if (outputs.isEmpty()) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.INTERNAL_ERROR);
            return EcoSphereModeResult.standard(
                // #tr GT5U.gui.text.recipe_result.tst_ess_growing_crops
                // # {\GREEN}Growing Crops
                // #zh_CN {\GREEN}作物生长中
                SimpleCheckRecipeResult.ofSuccess("tst_ess_growing_crops"),
                outputs.toArray(new ItemStack[0]),
                parallelResult.tier());
        };
        return EcoSphereModeSupport
            .processModeRecipeWithTier(machine, fertilizerInput.getFluid(), baseFertilizerCost, euTier, processor);
    }

    private static List<CropsNHFarm.CropCache> findCrops(TST_EcoSphereSimulator machine) {
        List<CropsNHFarm.CropCache> crops = new ArrayList<>();
        for (ItemStack input : machine.getModeInputs()) {
            ItemStack seed = input.copy();
            seed.stackSize = 1;
            CropsNHFarm.CropCache crop = machine.cropsNHFarm.getCropCache(seed);
            if (crop != null) crops.add(crop.withSeedCount(input.stackSize));
        }
        return crops;
    }

}
