package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.addSplitStack;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTModHandler;
import gregtech.common.items.ItemIntegratedCircuit;
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
    public EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier) {
        // A valid sapling is required before selecting a tree recipe.
        EnumMap<Mode, ItemStack> outputPerMode = findTreeProduct(machine);
        if (outputPerMode == null) return EcoSphereModeResult.failure(MissingSaplingInput);

        // Start with the discounted water cost used by normal recipes and missing-fluid errors.
        long fluidPerParallel = machine
            .applyStructureFluidDiscount(TreeGrowthSimulatorWithoutToolFakeRecipe.WATER_PER_PARALLEL);

        // The first available fluid decides whether this is a normal or special recipe.
        FluidStack inputFluid = findInputFluid(machine);

        // The lowest power tier runs two parallels, so this and later startup checks require twice the fluid.
        if (inputFluid == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);

        Fluid requiredFluid = inputFluid.getFluid();
        if (requiredFluid != FluidRegistry.WATER) {
            SpecialTreeRecipe specialRecipe = findSpecialRecipe(requiredFluid);
            if (specialRecipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
            // Special trees require the upgraded tree beacon.
            if (machine.getModeBeaconTier() < 2) return EcoSphereModeResult.failure(ModeBeaconInputMismatch);
            fluidPerParallel = machine.applyStructureFluidDiscount(specialRecipe.amount);
            outputPerMode = specialRecipe.outputs;
        }
        // Read output circuits once and reuse the selected tree parts below.
        EnumSet<Mode> selectedModes = findSelectedModes(machine);
        if (selectedModes.isEmpty()) return EcoSphereModeResult.failure(MissingTreeOutputSelection);
        int availableOutputs = 0;
        for (ItemStack output : outputPerMode.values()) {
            if (output != null) availableOutputs++;
        }

        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, requiredFluid, fluidPerParallel, euTier);
        if (parallelResult == null) return EcoSphereModeResult
            .failure(EcoSphereModeSupport.missingFluid(machine, requiredFluid, fluidPerParallel * 2));
        float focusBonus = selectedModes.size() < availableOutputs
            ? 1 + (float) (availableOutputs - selectedModes.size()) / selectedModes.size() / 3
            : 1;
        List<ItemStack> outputs = new ArrayList<>();
        for (Mode mode : selectedModes) {
            ItemStack output = outputPerMode.get(mode);
            if (output == null) continue;
            long amount = (long) (output.stackSize * getModeMultiplier(mode)
                * parallelResult.multiplier()
                * focusBonus);
            addSplitStack(outputs, output, amount);
        }
        if (outputs.isEmpty()) return EcoSphereModeResult.failure(MissingTreeOutputSelection);
        return EcoSphereModeResult.standard(
            SimpleCheckRecipeResult.ofSuccess("growing_trees"),
            outputs.toArray(new ItemStack[0]),
            parallelResult.tier());
    }

    private static EnumMap<Mode, ItemStack> findTreeProduct(TST_MegaTreeFarm machine) {
        for (ItemStack input : machine.getStoredInputs()) {
            if (input == null || input.getItem() == null) continue;
            EnumMap<Mode, ItemStack> outputs = queryTreeProduct(input);
            if (outputs != null) return outputs;
        }
        return null;
    }

    private static FluidStack findInputFluid(TST_MegaTreeFarm machine) {
        for (FluidStack fluidStack : machine.getStoredFluids()) {
            if (fluidStack != null && fluidStack.getFluid() != null && fluidStack.amount > 0) return fluidStack;
        }
        return null;
    }

    private static SpecialTreeRecipe findSpecialRecipe(Fluid fluid) {
        Fluid temporalFluid = FluidRegistry.getFluid("temporalfluid");
        Fluid deathWater = FluidRegistry.getFluid("fluiddeath");
        Fluid unknownWater = FluidRegistry.getFluid("unknowwater");
        Fluid uuMatter = FluidRegistry.getFluid("ic2uumatter");
        if (temporalFluid != null && fluid == temporalFluid) {
            ItemStack specialSapling = GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5);
            EnumMap<Mode, ItemStack> outputs = specialSapling == null ? null : queryTimeTreeProduct(specialSapling);
            if (outputs != null) return new SpecialTreeRecipe(
                TreeGrowthSimulatorWithoutToolFakeRecipe.TEMPORAL_FLUID_PER_PARALLEL,
                outputs);
        }
        if (deathWater != null && fluid == deathWater) {
            ItemStack taintedSapling = GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintSapling", 1, 0);
            EnumMap<Mode, ItemStack> outputs = taintedSapling == null ? null : queryTreeProduct(taintedSapling);
            if (outputs != null) return new SpecialTreeRecipe(
                TreeGrowthSimulatorWithoutToolFakeRecipe.DEATH_WATER_PER_PARALLEL,
                outputs);
        }
        if (unknownWater != null && fluid == unknownWater) {
            ItemStack barnardaCSapling = GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 1, 0);
            EnumMap<Mode, ItemStack> outputs = barnardaCSapling == null ? null : queryTreeProduct(barnardaCSapling);
            if (outputs != null) return new SpecialTreeRecipe(
                TreeGrowthSimulatorWithoutToolFakeRecipe.UNKNOWN_WATER_PER_PARALLEL,
                outputs);
        }
        if (uuMatter != null && fluid == uuMatter && TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts != null) {
            Random random = new Random();
            EnumMap<Mode, ItemStack> randomOutputs = new EnumMap<>(Mode.class);
            for (Mode mode : Mode.values()) {
                ItemStack[] candidates = TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts[mode.ordinal()];
                if (candidates != null && candidates.length > 0) {
                    randomOutputs.put(mode, candidates[random.nextInt(candidates.length)]);
                }
            }
            if (!randomOutputs.isEmpty()) return new SpecialTreeRecipe(
                TreeGrowthSimulatorWithoutToolFakeRecipe.UU_MATTER_PER_PARALLEL,
                randomOutputs);
        }
        return null;
    }

    @Desugar
    private record SpecialTreeRecipe(long amount, EnumMap<Mode, ItemStack> outputs) {

    }

    public static int getModeMultiplier(Mode mode) {
        return switch (mode) {
            case LOG -> 20;
            case SAPLING -> 3;
            case LEAVES -> 8;
            case FRUIT -> 1;
        };
    }

    private static EnumSet<Mode> findSelectedModes(TST_MegaTreeFarm machine) {
        Mode[] modes = Mode.values();
        EnumSet<Mode> selectedModes = EnumSet.noneOf(Mode.class);
        for (ItemStack input : machine.getStoredInputs()) {
            if (input == null || !(input.getItem() instanceof ItemIntegratedCircuit)) continue;
            int configuration = input.getItemDamage();
            if (configuration > 0 && configuration <= modes.length) selectedModes.add(modes[configuration - 1]);
        }
        return selectedModes;
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
