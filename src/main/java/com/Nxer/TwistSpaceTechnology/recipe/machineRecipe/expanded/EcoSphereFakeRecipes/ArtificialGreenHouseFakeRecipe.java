package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.MODE_RECIPE_DURATION;

import java.util.Collection;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmTierRequirementKey;
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
        for (ItemStack output : outputs.keySet()) {
            if (output == null || output.getItem() == null) continue;
            ItemStack product = copyOne(output);
            FluidStack requiredFluid = new FluidStack(
                CropsNHFluids.enrichedFertilizer,
                hybrid ? HYBRID_SEED_FERTILIZER_PER_PARALLEL : NORMAL_SEED_FERTILIZER_PER_PARALLEL);
            if (requiredFluid.getFluid() == null) continue;
            GTValues.RA.stdBuilder()
                .itemInputs(input.copy())
                .itemOutputs(product)
                .fluidInputs(requiredFluid)
                .metadata(MegaTreeFarmTierRequirementKey.INSTANCE, hybrid ? 2 : 1)
                .duration(MODE_RECIPE_DURATION)
                .eut(0)
                .fake()
                .addTo(GTCMRecipe.ArtificialGreenHouseFakeRecipes);
        }
    }

    private static ItemStack copyOne(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        return copy;
    }
}
