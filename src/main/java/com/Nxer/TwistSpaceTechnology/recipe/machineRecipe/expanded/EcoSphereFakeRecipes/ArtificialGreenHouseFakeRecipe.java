package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache.GREENHOUSE_MODE;
import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache.cacheRecipeFluids;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.CropsNHFarm;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorBeaconRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorTierRequirementKey;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.init.CropsNHFluids;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;

public final class ArtificialGreenHouseFakeRecipe {

    public static final int NORMAL_SEED_FERTILIZER_PER_PARALLEL = 20;
    public static final int HYBRID_SEED_FERTILIZER_PER_PARALLEL = 50;

    private ArtificialGreenHouseFakeRecipe() {}

    public static void loadRecipes() {
        GTCMRecipe.ArtificialGreenHouseFakeRecipes.getBackend()
            .clearRecipes();
        Set<String> registeredSeeds = new HashSet<>();
        for (ICropCard crop : CropRegistry.instance.getAllInRegistrationOrder()) {
            registerHybridSeedRecipes(crop, registeredSeeds);
            registerNormalSeedRecipes(crop, registeredSeeds);
        }
        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient()) registerUnlistedPlantableRecipes(registeredSeeds);
        cacheRecipeFluids(GREENHOUSE_MODE, GTCMRecipe.ArtificialGreenHouseFakeRecipes);
    }

    /** Registers every remaining item variant and lets the cache decide whether it is plantable. */
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    private static void registerUnlistedPlantableRecipes(Set<String> registeredSeeds) {
        CropsNHFarm farm = new CropsNHFarm();
        for (Item item : (Iterable<Item>) GameData.getItemRegistry()) {
            if (item == null) continue;
            GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(item);
            if (identifier == null) continue;

            List<ItemStack> variants = new ArrayList<>();
            try {
                item.getSubItems(item, null, variants);
            } catch (Throwable ignored) {
                // Some mod items load client-only classes while enumerating variants. Keep scanning the registry.
            }
            if (variants.isEmpty()) variants.add(new ItemStack(item, 1, 0));

            for (ItemStack listedVariant : variants) {
                if (listedVariant == null || listedVariant.getItem() != item) continue;
                ItemStack variant = listedVariant.copy();
                variant.stackSize = 1;
                String seedKey = getSeedKey(variant, identifier);
                if (!registeredSeeds.add(seedKey)) continue;
                try {
                    CropsNHFarm.CropCache crop = farm.getCropCache(variant);
                    if (crop != null && !crop.hybrid()) registerOutputs(variant, crop.cachedOutput());
                } catch (Throwable ignored) {
                    // A broken crop implementation must not prevent other registered seeds from being cached.
                }
            }
        }
    }

    private static void registerHybridSeedRecipes(ICropCard crop, Set<String> registeredSeeds) {
        ItemStack hybridSeed = crop.getSeedItem(SeedStats.DEFAULT_ANALYZED);
        if (!addSeedKey(registeredSeeds, hybridSeed)) return;
        registerOutputs(hybridSeed, crop.getDropTable(), true);
    }

    private static void registerNormalSeedRecipes(ICropCard crop, Set<String> registeredSeeds) {
        Collection<ItemStack> normalSeeds = crop.getAlternateSeeds();
        if (normalSeeds == null) return;
        for (ItemStack normalSeed : normalSeeds) {
            if (!addSeedKey(registeredSeeds, normalSeed)) continue;
            registerOutputs(normalSeed, crop.getDropTable(), false);
        }
    }

    private static boolean addSeedKey(Set<String> registeredSeeds, ItemStack seed) {
        if (seed == null || seed.getItem() == null) return false;
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(seed.getItem());
        return identifier != null && registeredSeeds.add(getSeedKey(seed, identifier));
    }

    private static String getSeedKey(ItemStack seed, GameRegistry.UniqueIdentifier identifier) {
        return identifier + ":" + seed.getItemDamage();
    }

    private static void registerOutputs(ItemStack seed, Map<ItemStack, Integer> outputs, boolean hybrid) {
        if (seed == null || seed.getItem() == null || outputs == null) return;
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

        registerRecipe(seed, products, outputChances, hybrid);
    }

    private static void registerOutputs(ItemStack seed, Pair<ItemStack, Double>[] outputs) {
        List<Map.Entry<ItemStack, Integer>> displayedOutputs = new ArrayList<>();
        for (Pair<ItemStack, Double> output : outputs) {
            if (output == null || output.getKey() == null || output.getValue() <= 0) continue;
            int stackSize = Math.max(1, (int) Math.ceil(output.getValue()));
            ItemStack product = output.getKey()
                .copy();
            product.stackSize = stackSize;
            displayedOutputs.add(
                new java.util.AbstractMap.SimpleEntry<>(
                    product,
                    Math.max(1, Math.min(10_000, (int) Math.round(output.getValue() / stackSize * 10_000)))));
        }
        if (displayedOutputs.isEmpty()) return;
        displayedOutputs.sort(Map.Entry.<ItemStack, Integer>comparingByValue(Comparator.reverseOrder()));
        displayedOutputs = displayedOutputs.stream()
            .limit(9)
            .collect(Collectors.toList());
        registerRecipe(
            seed,
            displayedOutputs.stream()
                .map(Map.Entry::getKey)
                .toArray(ItemStack[]::new),
            displayedOutputs.stream()
                .mapToInt(Map.Entry::getValue)
                .toArray(),
            false);
    }

    private static void registerRecipe(ItemStack seed, ItemStack[] products, int[] outputChances, boolean hybrid) {
        ItemStack input = copyOne(seed);
        input.stackSize = 0;

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
