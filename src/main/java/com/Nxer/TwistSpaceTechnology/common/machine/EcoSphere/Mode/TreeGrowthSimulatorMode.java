package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport.addItemOutput;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ExecutionProtocolInputMismatch;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.MissingSaplingInput;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.MissingTreeOutputSelection;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereSpecialUpgrade;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.common.tileentities.machines.multi.MTETreeFarm.Mode;

public final class TreeGrowthSimulatorMode implements IEcoSphereMode {

    // Perfect genetics uses virtual traits beyond Forestry's registered allele limits.
    private static final double PERFECT_TREE_HEIGHT = 4.0d;
    private static final int PERFECT_TREE_GIRTH = 16;
    private static final double PERFECT_TREE_FERTILITY = 0.6d;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes;
    }

    @Override
    public String getDisplayName() {
        return translateToLocal("EcoSphereSimulator.modeMsg.0");
    }

    @Override
    public EcoSphereModeResult process(TST_EcoSphereSimulator machine, int euTier) {
        // Every valid input sapling is processed in one operation. Duplicate saplings use only the largest stack.
        EnumSet<Mode> selectedInputs = machine.getSelectedTreeOutputs();
        Map<TST_ItemID, SaplingProducts> selectedSaplings = new LinkedHashMap<>();
        for (ItemStack input : machine.getModeInputs()) {
            TST_ItemID saplingId = TST_ItemID.create(input);
            SaplingProducts current = selectedSaplings.get(saplingId);
            if (current != null && current.sapling().stackSize >= input.stackSize) continue;
            EnumMap<Mode, ItemStack> products = queryTreeProduct(input, false);
            if (products != null) selectedSaplings.put(saplingId, new SaplingProducts(input, products));
        }
        List<SaplingProducts> saplings = new ArrayList<>(selectedSaplings.values());
        if (saplings.isEmpty()) return EcoSphereModeResult.failure(MissingSaplingInput);

        FluidStack fluidInput = EcoSphereFluidCache.findFirstValidFluid(machine);
        if (fluidInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);

        TreeFluidRecipe recipe = findRecipe(fluidInput);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        if (machine.getExecutionProtocolTier() < recipe.requiredExecutionProtocolTier())
            return EcoSphereModeResult.failure(ExecutionProtocolInputMismatch);

        List<EnumMap<Mode, ItemStack>> productSets = new ArrayList<>();
        List<Integer> saplingCounts = new ArrayList<>();
        boolean maximizeForestry = recipe.normalWater()
            && machine.hasSpecialUpgrade(EcoSphereSpecialUpgrade.PERFECT_GENETICS);
        if (recipe.uuMatter()) {
            // UU matter ignores saplings and picks one random product per mode.
            productSets.add(buildUuProducts());
            saplingCounts.add(1);
        } else {
            for (SaplingProducts sapling : saplings) {
                EnumMap<Mode, ItemStack> products = sapling.products();
                if (recipe.timeFluid()) {
                    products = queryTimeTreeProduct(sapling.sapling());
                } else if (maximizeForestry) {
                    products = queryTreeProduct(sapling.sapling(), true);
                }
                productSets.add(products);
                saplingCounts.add(Math.min(sapling.sapling().stackSize, 64));
            }
        }

        // Each retained sapling expands mode parallel and pays the discounted per-sapling fluid cost.
        long fluidPerSapling = machine.applyFluidDiscount(recipe.fluid().amount);
        long fluidCostPerParallel = 0;
        int inputParallelMultiplier = 0;
        for (int saplingCount : saplingCounts) {
            fluidCostPerParallel += fluidPerSapling * saplingCount;
            inputParallelMultiplier += saplingCount;
        }
        return EcoSphereModeSupport.processModeRecipeWithTierAndFluidCost(
            machine,
            recipe.fluid()
                .getFluid(),
            fluidCostPerParallel,
            inputParallelMultiplier,
            euTier,
            parallelResult -> processOutputs(productSets, saplingCounts, selectedInputs, saplings, parallelResult));
    }

    @Desugar
    private record SaplingProducts(ItemStack sapling, EnumMap<Mode, ItemStack> products) {}

    private static EcoSphereModeResult processOutputs(List<EnumMap<Mode, ItemStack>> productSets,
        List<Integer> saplingCounts, EnumSet<Mode> selectedInputs, List<SaplingProducts> saplings,
        EcoSphereModeSupport.ParallelResult parallelResult) {
        List<ItemStackLong> outputs = new ArrayList<>();
        for (int index = 0; index < productSets.size(); index++) {
            EnumMap<Mode, ItemStack> products = productSets.get(index);
            int saplingCount = saplingCounts.get(index);
            EnumSet<Mode> selected = EnumSet.noneOf(Mode.class);
            for (Mode mode : selectedInputs) {
                if (products.get(mode) != null) selected.add(mode);
            }
            if (selected.isEmpty()) continue;

            int availableOutputs = 0;
            for (ItemStack output : products.values()) {
                if (output != null) availableOutputs++;
            }
            final float focusBonus;
            if (selected.size() < availableOutputs) {
                focusBonus = 1 + (float) (availableOutputs - selected.size()) / selected.size() / 3;
            } else {
                focusBonus = 1;
            }
            for (Mode mode : selected) {
                ItemStack output = products.get(mode);
                long saplingParallel = EcoSphereModeSupport.multiplyParallel(parallelResult.parallel(), saplingCount);
                double outputAmount = output.stackSize * (double) getModeMultiplier(mode)
                    * saplingParallel
                    * focusBonus;
                long amount = outputAmount >= Long.MAX_VALUE ? Long.MAX_VALUE : (long) outputAmount;
                addItemOutput(outputs, output, amount);
            }
        }
        if (outputs.isEmpty()) return EcoSphereModeResult.failure(MissingTreeOutputSelection);
        List<String> saplingNames = new ArrayList<>(saplings.size());
        for (SaplingProducts sapling : saplings) saplingNames.add(
            sapling.sapling()
                .getDisplayName());
        // #tr EcoSphereSimulator.gui.runningSaplings
        // # Saplings
        // #zh_CN 树苗
        return EcoSphereModeResult.standard(
            // #tr GT5U.gui.text.recipe_result.tst_ess_growing_trees
            // # {\GREEN}Growing Trees
            // #zh_CN {\GREEN}树木生长中
            SimpleResultWithText.ofSuccessText(
                translateToLocal("GT5U.gui.text.recipe_result.tst_ess_growing_trees") + "\n"
                    + EcoSphereModeSupport
                        .formatRunningInputs(translateToLocal("EcoSphereSimulator.gui.runningSaplings"), saplingNames)),
            outputs,
            parallelResult.tier());
    }

    private static TreeFluidRecipe findRecipe(FluidStack fluid) {
        FluidStack water = TreeGrowthSimulatorWithoutToolFakeRecipe.WATER_STACK;
        if (water != null && fluid.getFluid() == water.getFluid())
            return new TreeFluidRecipe(water, 1, true, false, false);
        FluidStack temporalFluid = TreeGrowthSimulatorWithoutToolFakeRecipe.TEMPORAL_FLUID_STACK;
        if (temporalFluid != null && fluid.getFluid() == temporalFluid.getFluid())
            return new TreeFluidRecipe(temporalFluid, 2, false, true, false);
        FluidStack deathWater = TreeGrowthSimulatorWithoutToolFakeRecipe.DEATH_WATER_STACK;
        if (deathWater != null && fluid.getFluid() == deathWater.getFluid())
            return new TreeFluidRecipe(deathWater, 2, false, false, false);
        FluidStack unknownWater = TreeGrowthSimulatorWithoutToolFakeRecipe.UNKNOWN_WATER_STACK;
        if (unknownWater != null && fluid.getFluid() == unknownWater.getFluid())
            return new TreeFluidRecipe(unknownWater, 2, false, false, false);
        FluidStack uuMatter = TreeGrowthSimulatorWithoutToolFakeRecipe.UU_MATTER_STACK;
        if (uuMatter != null && fluid.getFluid() == uuMatter.getFluid()
            && TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts != null)
            return new TreeFluidRecipe(uuMatter, 2, false, false, true);
        return null;
    }

    private static EnumMap<Mode, ItemStack> buildUuProducts() {
        Random random = new Random();
        EnumMap<Mode, ItemStack> randomOutputs = new EnumMap<>(Mode.class);
        for (Mode mode : Mode.values()) {
            ItemStack[] candidates = TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts[mode.ordinal()];
            if (candidates != null && candidates.length > 0) {
                randomOutputs.put(mode, candidates[random.nextInt(candidates.length)]);
            }
        }
        return randomOutputs;
    }

    @Desugar
    private record TreeFluidRecipe(FluidStack fluid, int requiredExecutionProtocolTier, boolean normalWater,
        boolean timeFluid, boolean uuMatter) {}

    public static int getModeMultiplier(Mode mode) {
        return switch (mode) {
            case LOG -> 20;
            case SAPLING -> 3;
            case LEAVES -> 8;
            case FRUIT -> 1;
        };
    }

    public static EnumMap<Mode, ItemStack> queryTreeProduct(ItemStack sapling, boolean maximizeForestry) {
        String key = EcoSphereModeSupport.getItemStackString(sapling);
        EnumMap<Mode, ItemStack> productMap = gregtech.common.tileentities.machines.multi.MTETreeFarm.treeProductsMap
            .get(key);
        return productMap != null ? productMap : getOutputsForForestrySapling(sapling, maximizeForestry);
    }

    public static EnumMap<Mode, ItemStack> queryTimeTreeProduct(ItemStack sapling) {
        EnumMap<Mode, ItemStack> productMap = queryTreeProduct(sapling, false);
        if (productMap == null) return null;
        EnumMap<Mode, ItemStack> adjustedMap = new EnumMap<>(productMap);
        ItemStack timewoodClock = gregtech.api.util.GTModHandler
            .getModItem(gregtech.api.enums.Mods.TwilightForest.ID, "tile.TFMagicLogSpecial", 1, 0);
        if (timewoodClock != null) adjustedMap.put(Mode.FRUIT, timewoodClock);
        return adjustedMap;
    }

    private static EnumMap<Mode, ItemStack> getOutputsForForestrySapling(ItemStack sapling, boolean maximizeForestry) {
        forestry.api.arboriculture.ITree tree = forestry.api.arboriculture.TreeManager.treeRoot.getMember(sapling);
        if (tree == null) return null;
        EnumMap<Mode, ItemStack> defaultMap = gregtech.common.tileentities.machines.multi.MTETreeFarm.treeProductsMap
            .get("Forestry:sapling:" + tree.getIdent());
        if (defaultMap == null) return null;
        EnumMap<Mode, ItemStack> adjustedMap = new EnumMap<>(Mode.class);
        ItemStack log = defaultMap.get(Mode.LOG);
        if (log != null) {
            double treeHeight = maximizeForestry ? PERFECT_TREE_HEIGHT
                : tree.getGenome()
                    .getHeight();
            int treeGirth = maximizeForestry ? PERFECT_TREE_GIRTH
                : tree.getGenome()
                    .getGirth();
            double height = Math.max(3 * (treeHeight - 1), 0) + 1;
            log = log.copy();
            log.stackSize = (int) (log.stackSize * height * treeGirth);
            adjustedMap.put(Mode.LOG, log);
        }
        ItemStack saplingOut = defaultMap.get(Mode.SAPLING);
        if (saplingOut != null) {
            saplingOut = sapling.copy();
            double fertility = maximizeForestry ? PERFECT_TREE_FERTILITY
                : tree.getGenome()
                    .getFertility();
            saplingOut.stackSize = Math.max(1, (int) (defaultMap.get(Mode.SAPLING).stackSize * fertility * 10));
            adjustedMap.put(Mode.SAPLING, saplingOut);
        }
        ItemStack leaves = defaultMap.get(Mode.LEAVES);
        if (leaves != null) adjustedMap.put(Mode.LEAVES, leaves.copy());
        ItemStack fruit = defaultMap.get(Mode.FRUIT);
        if (fruit != null) {
            fruit = fruit.copy();
            double yield = maximizeForestry ? 0.4
                : tree.getGenome()
                    .getYield();
            fruit.stackSize = (int) (fruit.stackSize * yield * 10);
            adjustedMap.put(Mode.FRUIT, fruit);
        }
        return adjustedMap;
    }
}
