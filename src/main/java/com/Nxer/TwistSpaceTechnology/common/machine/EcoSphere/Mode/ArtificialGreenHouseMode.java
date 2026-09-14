package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.NoSeedInController;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereSpecialUpgrade;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.CropsNHFarm;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.ArtificialGreenHouseFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.api.ISeedStats;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.utility.CropsNHUtils;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class ArtificialGreenHouseMode implements IEcoSphereMode {

    // Applied after crop-specific growth and drop calculations, before shared upgrade and random output scaling.
    private static final double OUTPUT_SCALE = 40.0d;
    private static final int PERFECT_SEED_STAT = 31;

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
        // Each retained seed expands its crop parallel and pays the discounted per-seed fertilizer cost.
        long fertilizerCostPerParallel = 0;
        int inputParallelMultiplier = 0;
        for (CropsNHFarm.CropCache crop : crops) {
            int fertilizerPerSeed;
            if (crop.hybrid()) {
                if (machine.getModeBeaconTier() < 2) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);
                fertilizerPerSeed = ArtificialGreenHouseFakeRecipe.HYBRID_SEED_FERTILIZER_PER_PARALLEL;
            } else {
                fertilizerPerSeed = ArtificialGreenHouseFakeRecipe.NORMAL_SEED_FERTILIZER_PER_PARALLEL;
            }
            fertilizerCostPerParallel += machine.applyFluidDiscount(fertilizerPerSeed) * crop.seedCount();
            inputParallelMultiplier += crop.seedCount();
        }

        FluidStack fertilizerInput = EcoSphereFluidCache.findFirstValidFluid(machine);
        if (fertilizerInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        Function<EcoSphereModeSupport.ParallelResult, EcoSphereModeResult> processor = parallelResult -> {
            List<ItemStack> outputs = new ArrayList<>();
            for (CropsNHFarm.CropCache crop : crops) {
                // Cached yields already include environmental growth progress and the non-hybrid efficiency penalty.
                long seedParallel = EcoSphereModeSupport.multiplyParallel(parallelResult.parallel(), crop.seedCount());
                Collections.addAll(outputs, crop.getOutputStacks(seedParallel * OUTPUT_SCALE));
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
        return EcoSphereModeSupport.processModeRecipeWithTierAndFluidCost(
            machine,
            fertilizerInput.getFluid(),
            fertilizerCostPerParallel,
            inputParallelMultiplier,
            euTier,
            processor);
    }

    private static List<CropsNHFarm.CropCache> findCrops(TST_EcoSphereSimulator machine) {
        Map<Object, CropSelection> selectedCrops = new LinkedHashMap<>();
        boolean maximizeGenetics = machine.hasSpecialUpgrade(EcoSphereSpecialUpgrade.PERFECT_GENETICS);
        for (ItemStack input : machine.getModeInputs()) {
            ItemStack seed = input.copy();
            seed.stackSize = 1;

            // CropsNH stores different stats in NBT, so seeds of one registered crop share one selection slot.
            ISeedData seedData = CropsNHUtils.getAnalyzedSeedData(seed);
            ICropCard cropType = seedData != null ? seedData.getCrop() : CropRegistry.instance.fromAlternateSeed(seed);
            Object typeKey = cropType != null ? cropType : TST_ItemID.create(seed);
            int statTotal = 0;
            if (seedData != null) {
                ISeedStats stats = seedData.getStats();
                statTotal = stats.getGrowth() + stats.getGain() + stats.getResistance();
            }
            int simulatedSeedStat = maximizeGenetics && seedData != null ? PERFECT_SEED_STAT : 0;
            CropsNHFarm.CropCache crop = machine.cropsNHFarm.getCropCache(seed, simulatedSeedStat);
            if (crop == null) continue;

            int seedCount = Math.min(input.stackSize, 64);
            CropSelection current = selectedCrops.get(typeKey);
            if (current != null) {
                if (current.crop()
                    .seedCount() > seedCount) continue;
                if (current.crop()
                    .seedCount() == seedCount && current.statTotal() >= statTotal) continue;
            }
            selectedCrops.put(typeKey, new CropSelection(crop.withSeedCount(seedCount), statTotal));
        }
        List<CropsNHFarm.CropCache> crops = new ArrayList<>(selectedCrops.size());
        for (CropSelection selection : selectedCrops.values()) crops.add(selection.crop());
        return crops;
    }

    @Desugar
    private record CropSelection(CropsNHFarm.CropCache crop, int statTotal) {}

}
