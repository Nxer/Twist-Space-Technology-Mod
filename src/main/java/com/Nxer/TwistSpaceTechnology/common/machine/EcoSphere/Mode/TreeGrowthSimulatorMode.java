package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport.addSplitStack;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.MissingSaplingInput;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.MissingTreeOutputSelection;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTModHandler;
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
        // A valid sapling is required before selecting a tree recipe.
        EnumMap<Mode, ItemStack> normalOutputs = findTreeProduct(machine);
        if (normalOutputs == null) return EcoSphereModeResult.failure(MissingSaplingInput);

        FluidStack fluidInput = EcoSphereFluidCache.findFirstValidFluid(machine);
        if (fluidInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);

        TreeRecipe recipe = findRecipe(fluidInput.getFluid(), normalOutputs);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        if (machine.getModeBeaconTier() < recipe.requiredBeaconTier())
            return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        // Ignore selected tree parts that the resolved tree cannot produce.
        EnumSet<Mode> selectedModes = EnumSet.noneOf(Mode.class);
        for (Mode mode : machine.getSelectedTreeOutputs()) {
            if (recipe.outputs()
                .get(mode) != null) selectedModes.add(mode);
        }
        if (selectedModes.isEmpty()) return EcoSphereModeResult.failure(MissingTreeOutputSelection);
        int availableOutputs = 0;
        for (ItemStack output : recipe.outputs()
            .values()) {
            if (output != null) availableOutputs++;
        }

        final float focusBonus;
        if (selectedModes.size() < availableOutputs) {
            focusBonus = 1 + (float) (availableOutputs - selectedModes.size()) / selectedModes.size() / 3;
        } else {
            focusBonus = 1;
        }
        FluidStack recipeFluid = recipe.fluidInput();
        return EcoSphereModeSupport.processModeRecipeWithTier(
            machine,
            recipeFluid.getFluid(),
            recipeFluid.amount,
            euTier,
            parallelResult -> processOutputs(recipe, selectedModes, focusBonus, parallelResult));
    }

    private static EcoSphereModeResult processOutputs(TreeRecipe recipe, EnumSet<Mode> selectedModes, float focusBonus,
        EcoSphereModeSupport.ParallelResult parallelResult) {
        List<ItemStack> outputs = new ArrayList<>();
        for (Mode mode : selectedModes) {
            ItemStack output = recipe.outputs()
                .get(mode);
            long amount = (long) (output.stackSize * getModeMultiplier(mode) * parallelResult.parallel() * focusBonus);
            addSplitStack(outputs, output, amount);
        }
        if (outputs.isEmpty()) return EcoSphereModeResult.failure(MissingTreeOutputSelection);
        return EcoSphereModeResult.standard(
            // #tr GT5U.gui.text.recipe_result.growing_trees
            // # {\GREEN}Growing Trees
            // #zh_CN {\GREEN}原木拟生中
            SimpleCheckRecipeResult.ofSuccess("growing_trees"),
            outputs.toArray(new ItemStack[0]),
            parallelResult.tier());
    }

    private static EnumMap<Mode, ItemStack> findTreeProduct(TST_EcoSphereSimulator machine) {
        for (ItemStack input : machine.getModeInputs()) {
            if (input == null || input.getItem() == null) continue;
            EnumMap<Mode, ItemStack> outputs = queryTreeProduct(input);
            if (outputs != null) return outputs;
        }
        return null;
    }

    private static TreeRecipe findRecipe(Fluid fluid, EnumMap<Mode, ItemStack> normalOutputs) {
        FluidStack water = TreeGrowthSimulatorWithoutToolFakeRecipe.WATER_STACK;
        if (water != null && fluid == water.getFluid()) return new TreeRecipe(water, 1, normalOutputs);
        return findSpecialRecipe(fluid);
    }

    private static TreeRecipe findSpecialRecipe(Fluid fluid) {
        FluidStack temporalFluid = TreeGrowthSimulatorWithoutToolFakeRecipe.TEMPORAL_FLUID_STACK;
        if (temporalFluid != null && fluid == temporalFluid.getFluid()) {
            ItemStack specialSapling = GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5);
            EnumMap<Mode, ItemStack> outputs = specialSapling == null ? null : queryTimeTreeProduct(specialSapling);
            if (outputs != null) return new TreeRecipe(temporalFluid, 2, outputs);
        }
        FluidStack deathWater = TreeGrowthSimulatorWithoutToolFakeRecipe.DEATH_WATER_STACK;
        if (deathWater != null && fluid == deathWater.getFluid()) {
            ItemStack taintedSapling = GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintSapling", 1, 0);
            EnumMap<Mode, ItemStack> outputs = taintedSapling == null ? null : queryTreeProduct(taintedSapling);
            if (outputs != null) return new TreeRecipe(deathWater, 2, outputs);
        }
        FluidStack unknownWater = TreeGrowthSimulatorWithoutToolFakeRecipe.UNKNOWN_WATER_STACK;
        if (unknownWater != null && fluid == unknownWater.getFluid()) {
            ItemStack barnardaCSapling = GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 1, 0);
            EnumMap<Mode, ItemStack> outputs = barnardaCSapling == null ? null : queryTreeProduct(barnardaCSapling);
            if (outputs != null) return new TreeRecipe(unknownWater, 2, outputs);
        }
        FluidStack uuMatter = TreeGrowthSimulatorWithoutToolFakeRecipe.UU_MATTER_STACK;
        if (uuMatter != null && fluid == uuMatter.getFluid()
            && TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts != null) {
            Random random = new Random();
            EnumMap<Mode, ItemStack> randomOutputs = new EnumMap<>(Mode.class);
            for (Mode mode : Mode.values()) {
                ItemStack[] candidates = TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts[mode.ordinal()];
                if (candidates != null && candidates.length > 0) {
                    randomOutputs.put(mode, candidates[random.nextInt(candidates.length)]);
                }
            }
            if (!randomOutputs.isEmpty()) return new TreeRecipe(uuMatter, 2, randomOutputs);
        }
        return null;
    }

    @Desugar
    private record TreeRecipe(FluidStack fluidInput, int requiredBeaconTier, EnumMap<Mode, ItemStack> outputs) {}

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
        ItemStack timewoodClock = GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLogSpecial", 1, 0);
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
