package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport.addSplitStack;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.MissingSaplingInput;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.MissingTreeOutputSelection;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.common.tileentities.machines.multi.MTETreeFarm.Mode;

public final class TreeGrowthSimulatorMode implements IEcoSphereMode {

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
        // Every valid input sapling is processed in one operation, so different saplings
        // produce their own tree products together. Duplicate saplings are counted once.
        EnumSet<Mode> selectedInputs = machine.getSelectedTreeOutputs();
        List<SaplingProducts> saplings = new ArrayList<>();
        Set<TST_ItemID> seenSaplings = new HashSet<>();
        for (ItemStack input : machine.getModeInputs()) {
            if (!seenSaplings.add(TST_ItemID.create(input))) continue;
            EnumMap<Mode, ItemStack> products = queryTreeProduct(input);
            if (products != null) saplings.add(new SaplingProducts(input, products));
        }
        if (saplings.isEmpty()) return EcoSphereModeResult.failure(MissingSaplingInput);

        FluidStack fluidInput = EcoSphereFluidCache.findFirstValidFluid(machine);
        if (fluidInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);

        TreeFluidRecipe recipe = findRecipe(fluidInput);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        if (machine.getModeBeaconTier() < recipe.requiredBeaconTier())
            return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        List<EnumMap<Mode, ItemStack>> productSets = new ArrayList<>();
        List<Integer> saplingCounts = new ArrayList<>();
        if (recipe.uuMatter()) {
            // UU matter ignores saplings and picks one random product per mode.
            productSets.add(buildUuProducts());
            saplingCounts.add(1);
        } else {
            for (SaplingProducts sapling : saplings) {
                productSets.add(recipe.timeFluid() ? queryTimeTreeProduct(sapling.sapling()) : sapling.products());
                saplingCounts.add(sapling.sapling().stackSize);
            }
        }

        return EcoSphereModeSupport.processModeRecipeWithTier(
            machine,
            recipe.fluid()
                .getFluid(),
            recipe.fluid().amount,
            euTier,
            parallelResult -> processOutputs(productSets, saplingCounts, selectedInputs, parallelResult));
    }

    @Desugar
    private record SaplingProducts(ItemStack sapling, EnumMap<Mode, ItemStack> products) {}

    private static EcoSphereModeResult processOutputs(List<EnumMap<Mode, ItemStack>> productSets,
        List<Integer> saplingCounts, EnumSet<Mode> selectedInputs, EcoSphereModeSupport.ParallelResult parallelResult) {
        List<ItemStack> outputs = new ArrayList<>();
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
                long amount = (long) (output.stackSize * saplingCount
                    * getModeMultiplier(mode)
                    * parallelResult.parallel()
                    * focusBonus);
                addSplitStack(outputs, output, amount);
            }
        }
        if (outputs.isEmpty()) return EcoSphereModeResult.failure(MissingTreeOutputSelection);
        return EcoSphereModeResult.standard(
            // #tr GT5U.gui.text.recipe_result.tst_ess_growing_trees
            // # {\GREEN}Growing Trees
            // #zh_CN {\GREEN}树木生长中
            SimpleCheckRecipeResult.ofSuccess("tst_ess_growing_trees"),
            outputs.toArray(new ItemStack[0]),
            parallelResult.tier());
    }

    private static TreeFluidRecipe findRecipe(FluidStack fluid) {
        FluidStack water = TreeGrowthSimulatorWithoutToolFakeRecipe.WATER_STACK;
        if (water != null && fluid.getFluid() == water.getFluid()) return new TreeFluidRecipe(water, 1, false, false);
        FluidStack temporalFluid = TreeGrowthSimulatorWithoutToolFakeRecipe.TEMPORAL_FLUID_STACK;
        if (temporalFluid != null && fluid.getFluid() == temporalFluid.getFluid())
            return new TreeFluidRecipe(temporalFluid, 2, true, false);
        FluidStack deathWater = TreeGrowthSimulatorWithoutToolFakeRecipe.DEATH_WATER_STACK;
        if (deathWater != null && fluid.getFluid() == deathWater.getFluid())
            return new TreeFluidRecipe(deathWater, 2, false, false);
        FluidStack unknownWater = TreeGrowthSimulatorWithoutToolFakeRecipe.UNKNOWN_WATER_STACK;
        if (unknownWater != null && fluid.getFluid() == unknownWater.getFluid())
            return new TreeFluidRecipe(unknownWater, 2, false, false);
        FluidStack uuMatter = TreeGrowthSimulatorWithoutToolFakeRecipe.UU_MATTER_STACK;
        if (uuMatter != null && fluid.getFluid() == uuMatter.getFluid()
            && TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts != null)
            return new TreeFluidRecipe(uuMatter, 2, false, true);
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
    private record TreeFluidRecipe(FluidStack fluid, int requiredBeaconTier, boolean timeFluid, boolean uuMatter) {}

    public static int getModeMultiplier(Mode mode) {
        return switch (mode) {
            case LOG -> 20;
            case SAPLING -> 3;
            case LEAVES -> 8;
            case FRUIT -> 1;
        };
    }

    public static EnumMap<Mode, ItemStack> queryTreeProduct(ItemStack sapling) {
        String key = EcoSphereModeSupport.getItemStackString(sapling);
        EnumMap<Mode, ItemStack> productMap = gregtech.common.tileentities.machines.multi.MTETreeFarm.treeProductsMap
            .get(key);
        return productMap != null ? productMap : getOutputsForForestrySapling(sapling);
    }

    public static EnumMap<Mode, ItemStack> queryTimeTreeProduct(ItemStack sapling) {
        EnumMap<Mode, ItemStack> productMap = queryTreeProduct(sapling);
        if (productMap == null) return null;
        EnumMap<Mode, ItemStack> adjustedMap = new EnumMap<>(productMap);
        ItemStack timewoodClock = gregtech.api.util.GTModHandler
            .getModItem(gregtech.api.enums.Mods.TwilightForest.ID, "tile.TFMagicLogSpecial", 1, 0);
        if (timewoodClock != null) adjustedMap.put(Mode.FRUIT, timewoodClock);
        return adjustedMap;
    }

    private static EnumMap<Mode, ItemStack> getOutputsForForestrySapling(ItemStack sapling) {
        forestry.api.arboriculture.ITree tree = forestry.api.arboriculture.TreeManager.treeRoot.getMember(sapling);
        if (tree == null) return null;
        EnumMap<Mode, ItemStack> defaultMap = gregtech.common.tileentities.machines.multi.MTETreeFarm.treeProductsMap
            .get("Forestry:sapling:" + tree.getIdent());
        if (defaultMap == null) return null;
        EnumMap<Mode, ItemStack> adjustedMap = new EnumMap<>(Mode.class);
        ItemStack log = defaultMap.get(Mode.LOG);
        if (log != null) {
            double height = Math.max(
                3 * (tree.getGenome()
                    .getHeight() - 1),
                0) + 1;
            log = log.copy();
            log.stackSize = (int) (log.stackSize * height
                * tree.getGenome()
                    .getGirth());
            adjustedMap.put(Mode.LOG, log);
        }
        ItemStack saplingOut = defaultMap.get(Mode.SAPLING);
        if (saplingOut != null) {
            saplingOut = sapling.copy();
            saplingOut.stackSize = Math.max(
                1,
                (int) (defaultMap.get(Mode.SAPLING).stackSize * tree.getGenome()
                    .getFertility() * 10));
            adjustedMap.put(Mode.SAPLING, saplingOut);
        }
        ItemStack leaves = defaultMap.get(Mode.LEAVES);
        if (leaves != null) adjustedMap.put(Mode.LEAVES, leaves.copy());
        ItemStack fruit = defaultMap.get(Mode.FRUIT);
        if (fruit != null) {
            fruit = fruit.copy();
            fruit.stackSize = (int) (fruit.stackSize * tree.getGenome()
                .getYield() * 10);
            adjustedMap.put(Mode.FRUIT, fruit);
        }
        return adjustedMap;
    }
}
