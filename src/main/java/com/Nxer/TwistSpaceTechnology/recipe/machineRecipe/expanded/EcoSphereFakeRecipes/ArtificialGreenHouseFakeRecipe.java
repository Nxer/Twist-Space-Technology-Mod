package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache.GREENHOUSE_MODE;
import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache.cacheRecipeFluids;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorBeaconRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorTierRequirementKey;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.init.CropsNHFluids;

import gregtech.api.enums.GTValues;

public final class ArtificialGreenHouseFakeRecipe {

    public static final int NORMAL_SEED_FERTILIZER_PER_PARALLEL = 20;
    public static final int HYBRID_SEED_FERTILIZER_PER_PARALLEL = 50;

    private ArtificialGreenHouseFakeRecipe() {}

    public static void loadRecipes() {
        GTCMRecipe.ArtificialGreenHouseFakeRecipes.getBackend()
            .clearRecipes();
        for (ICropCard crop : CropRegistry.instance.getAllInRegistrationOrder()) {
            registerHybridSeedRecipes(crop);
            registerNormalSeedRecipes(crop);
        }
        cacheRecipeFluids(GREENHOUSE_MODE, GTCMRecipe.ArtificialGreenHouseFakeRecipes);
    }

    private static void registerHybridSeedRecipes(ICropCard crop) {
        ItemStack hybridSeed = crop.getSeedItem(SeedStats.DEFAULT_ANALYZED);
        registerOutputs(hybridSeed, crop.getDropTable(), true);
    }

    private static void registerNormalSeedRecipes(ICropCard crop) {
        Collection<ItemStack> normalSeeds = crop.getAlternateSeeds();
        if (normalSeeds == null) return;
        for (ItemStack normalSeed : normalSeeds) {
            registerOutputs(normalSeed, crop.getDropTable(), false);
        }
    }

    private static void registerOutputs(ItemStack seed, Map<ItemStack, Integer> outputs, boolean hybrid) {
        if (seed == null || seed.getItem() == null || outputs == null) return;
        ItemStack input = copyOne(seed);
        input.stackSize = 0;
        List<Map.Entry<ItemStack, Integer>> displayedOutputs = outputs.entrySet()
            .stream()
            .filter(
                entry -> entry.getKey() != null && entry.getKey()
                    .getItem() != null)
            // Match the CropsNH crop page: most likely drops appear first in the 3x3 grid.
            .sorted(Map.Entry.<ItemStack, Integer>comparingByValue(Comparator.reverseOrder()))
            .limit(9)
            .collect(Collectors.toList());
        if (displayedOutputs.isEmpty()) return;

        ItemStack[] products = displayedOutputs.stream()
            .map(
                entry -> entry.getKey()
                    .copy())
            .toArray(ItemStack[]::new);
        int[] outputChances = displayedOutputs.stream()
            .mapToInt(Map.Entry::getValue)
            .toArray();

        FluidStack requiredFluid = new FluidStack(
            CropsNHFluids.enrichedFertilizer,
            hybrid ? HYBRID_SEED_FERTILIZER_PER_PARALLEL : NORMAL_SEED_FERTILIZER_PER_PARALLEL);
        if (requiredFluid.getFluid() == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(products)
            .outputChances(outputChances)
            .fluidInputs(requiredFluid)
            .metadata(EcoSphereSimulatorTierRequirementKey.INSTANCE, 1)
            .metadata(EcoSphereSimulatorBeaconRequirementKey.INSTANCE, hybrid ? 2 : 1)
            .duration(MODE_RECIPE_DURATION)
            .eut(0)
            .fake()
            .addTo(GTCMRecipe.ArtificialGreenHouseFakeRecipes);
    }

    private static ItemStack copyOne(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        return copy;
    }
}
