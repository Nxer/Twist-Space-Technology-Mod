package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport.addSplitStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;

import gregtech.api.enums.Mods;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTModHandler;
import gregtech.common.tileentities.machines.multi.MTETreeFarm.Mode;

public final class DebugMode {

    private static final long DEBUG_PARALLEL = 100_000;
    private static final Map<Integer, List<DebugOutput>> OUTPUT_CACHE = new HashMap<>();
    private static final Map<TST_EcoSphereSimulator, DebugState> MACHINE_STATES = new WeakHashMap<>();

    private DebugMode() {}

    public static EcoSphereModeResult process(TST_EcoSphereSimulator machine, int mode, int beaconTier) {
        int tier = beaconTier >= 2 ? 2 : 1;
        int cacheKey = mode * 2 + tier - 1;
        List<DebugOutput> cachedOutputs = OUTPUT_CACHE.computeIfAbsent(cacheKey, ignored -> collectOutputs(mode, tier));
        DebugState state = MACHINE_STATES.computeIfAbsent(machine, ignored -> new DebugState(mode, tier));
        if (state.mode != mode || state.beaconTier != tier) {
            state = new DebugState(mode, tier);
            MACHINE_STATES.put(machine, state);
        }
        if (state.outputIndex >= cachedOutputs.size()) {
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        }

        DebugOutput output = cachedOutputs.get(state.outputIndex);
        List<ItemStack> outputs = new ArrayList<>();
        addSplitStack(
            outputs,
            output.item()
                .getItemStackWithNBT(),
            output.amount());
        state.outputIndex++;
        return new EcoSphereModeResult(
            SimpleResultWithText.ofSuccessText("debugRUN"),
            outputs.toArray(new ItemStack[0]),
            new FluidStack[0],
            0,
            1);
    }

    public static void reset(TST_EcoSphereSimulator machine) {
        MACHINE_STATES.remove(machine);
    }

    private static List<DebugOutput> collectOutputs(int mode, int beaconTier) {
        // Merge identical outputs before applying the fixed debug parallel count.
        Map<TST_ItemID, Long> outputAmounts = new LinkedHashMap<>();
        switch (mode) {
            case 0 -> collectTreeOutputs(outputAmounts, beaconTier);
            case 1 -> collectAquaticOutputs(outputAmounts, beaconTier);
            case 2 -> collectGreenhouseOutputs(outputAmounts, beaconTier);
            case 3 -> collectClonerOutputs(outputAmounts, beaconTier);
            default -> {}
        }

        List<DebugOutput> outputs = new ArrayList<>(outputAmounts.size());
        for (Map.Entry<TST_ItemID, Long> output : outputAmounts.entrySet()) {
            outputs.add(new DebugOutput(output.getKey(), output.getValue()));
        }
        return outputs;
    }

    private static void collectTreeOutputs(Map<TST_ItemID, Long> outputs, int beaconTier) {
        ItemStack[][] registeredProducts = TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts;
        if (registeredProducts != null) {
            for (ItemStack[] products : registeredProducts) collect(outputs, products);
        }
        if (beaconTier < 2) return;

        ItemStack timeTree = GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5);
        if (timeTree != null) collectTreeRecipe(outputs, TreeGrowthSimulatorMode.queryTimeTreeProduct(timeTree));
        ItemStack taintedTree = GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintSapling", 1, 0);
        if (taintedTree != null) collectTreeRecipe(outputs, TreeGrowthSimulatorMode.queryTreeProduct(taintedTree));
        ItemStack barnardaCTree = GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 1, 0);
        if (barnardaCTree != null) collectTreeRecipe(outputs, TreeGrowthSimulatorMode.queryTreeProduct(barnardaCTree));
    }

    private static void collectTreeRecipe(Map<TST_ItemID, Long> outputs, EnumMap<Mode, ItemStack> products) {
        if (products == null) return;
        for (Map.Entry<Mode, ItemStack> product : products.entrySet()) {
            ItemStack stack = product.getValue();
            if (stack == null) continue;
            collect(
                outputs,
                stack,
                (long) stack.stackSize * TreeGrowthSimulatorMode.getModeMultiplier(product.getKey()));
        }
    }

    private static void collectAquaticOutputs(Map<TST_ItemID, Long> outputs, int beaconTier) {
        collect(outputs, AquaticZoneSimulatorFakeRecipe.WatersOutputs);
        if (beaconTier >= 2) collect(outputs, AquaticZoneSimulatorFakeRecipe.UnknownWaterOutputs);
    }

    private static void collectGreenhouseOutputs(Map<TST_ItemID, Long> outputs, int beaconTier) {
        for (ICropCard crop : CropRegistry.instance.getAllInRegistrationOrder()) {
            Collection<ItemStack> alternateSeeds = crop.getAlternateSeeds();
            // Tier I can only reach crops that have a normal seed registration.
            if (beaconTier < 2 && (alternateSeeds == null || alternateSeeds.isEmpty())) continue;
            Map<ItemStack, Integer> dropTable = crop.getDropTable();
            if (dropTable != null) collect(outputs, dropTable.keySet());
        }
    }

    private static void collectClonerOutputs(Map<TST_ItemID, Long> outputs, int beaconTier) {
        for (DirectedMobClonerRecipeCache.CachedRecipe recipe : DirectedMobClonerRecipeCache.getDebugRecipes()) {
            // The secondary cloning beacon adds boss recipes to the same numbered pool.
            if (recipe.boss() && beaconTier < 2) continue;
            for (DirectedMobClonerRecipeCache.CachedDrop drop : recipe.drops()) {
                ItemStack dropStack = drop.stack();
                if (dropStack != null) collect(outputs, dropStack, dropStack.stackSize);
                for (DirectedMobClonerDropConversion.ConvertedOutput converted : drop.convertedOutputs()) {
                    ItemStack convertedStack = converted.stack();
                    if (convertedStack != null) collect(outputs, convertedStack, convertedStack.stackSize);
                }
            }
        }
    }

    private static void collect(Map<TST_ItemID, Long> outputs, Iterable<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (stack != null) collect(outputs, stack, stack.stackSize);
        }
    }

    private static void collect(Map<TST_ItemID, Long> outputs, ItemStack[] stacks) {
        if (stacks == null) return;
        for (ItemStack stack : stacks) {
            if (stack != null) collect(outputs, stack, stack.stackSize);
        }
    }

    private static void collect(Map<TST_ItemID, Long> outputs, ItemStack stack, long baseAmount) {
        if (stack == null || stack.getItem() == null || baseAmount <= 0) return;
        long amount = baseAmount > Long.MAX_VALUE / DEBUG_PARALLEL ? Long.MAX_VALUE : baseAmount * DEBUG_PARALLEL;
        outputs.merge(TST_ItemID.create(stack), amount, DebugMode::addSaturated);
    }

    private static long addSaturated(long left, long right) {
        return left > Long.MAX_VALUE - right ? Long.MAX_VALUE : left + right;
    }

    @Desugar
    private record DebugOutput(TST_ItemID item, long amount) {}

    private static final class DebugState {

        private final int mode;
        private final int beaconTier;
        private int outputIndex;

        private DebugState(int mode, int beaconTier) {
            this.mode = mode;
            this.beaconTier = beaconTier;
        }
    }
}
