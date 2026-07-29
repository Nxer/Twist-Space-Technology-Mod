package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.addSplitStack;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMap;
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
        return net.minecraft.util.StatCollector.translateToLocal("EcoSphereSimulator.modeMsg.0");
    }

    @Override
    public EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier) {
        EnumMap<Mode, ItemStack> outputPerMode = findTreeProduct(machine);
        if (outputPerMode == null) return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_sapling"));

        Fluid requiredFluid = FluidRegistry.WATER;
        long fluidPerParallel = 2000L;
        if (machine.isTierTwo()) {
            SpecialTreeRecipe specialRecipe = findSpecialRecipe(machine);
            if (specialRecipe != null) {
                requiredFluid = specialRecipe.fluid;
                fluidPerParallel = specialRecipe.amount;
                outputPerMode = specialRecipe.outputs;
            }
        }
        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, requiredFluid, fluidPerParallel, euTier);
        if (parallelResult == null)
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_enough_input"));

        int requestedOutputs = 0;
        int availableOutputs = 0;
        for (Mode mode : Mode.values()) {
            if (getModeOutput(machine, mode) > 0) requestedOutputs++;
            if (outputPerMode.get(mode) != null) availableOutputs++;
        }
        if (requestedOutputs == 0)
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_correct_Circuit"));
        float focusBonus = requestedOutputs < availableOutputs
            ? 1 + (float) (availableOutputs - requestedOutputs) / requestedOutputs / 3
            : 1;
        List<ItemStack> outputs = new ArrayList<>();
        for (Mode mode : Mode.values()) {
            ItemStack output = outputPerMode.get(mode);
            if (output == null || getModeOutput(machine, mode) < 0) continue;
            long amount = (long) (output.stackSize * getModeMultiplier(mode)
                * parallelResult.multiplier()
                * focusBonus);
            addSplitStack(outputs, output, amount);
        }
        if (outputs.isEmpty())
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_correct_Circuit"));
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

    private static SpecialTreeRecipe findSpecialRecipe(TST_MegaTreeFarm machine) {
        Fluid temporalFluid = FluidRegistry.getFluid("temporalfluid");
        Fluid uuMatter = FluidRegistry.getFluid("ic2uumatter");
        for (net.minecraftforge.fluids.FluidStack fluidStack : machine.getStoredFluids()) {
            if (fluidStack == null) continue;
            Fluid fluid = fluidStack.getFluid();
            if (Mods.TwilightForest.isModLoaded() && temporalFluid != null && fluid == temporalFluid) {
                ItemStack specialSapling = GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5);
                EnumMap<Mode, ItemStack> outputs = specialSapling == null ? null : queryTimeTreeProduct(specialSapling);
                if (outputs != null) return new SpecialTreeRecipe(fluid, 100, outputs);
            }
            if (uuMatter != null && fluid == uuMatter && allProducts != null) {
                Random random = new Random();
                EnumMap<Mode, ItemStack> randomOutputs = new EnumMap<>(Mode.class);
                for (Mode mode : Mode.values()) {
                    ItemStack[] candidates = allProducts[mode.ordinal()];
                    if (candidates != null && candidates.length > 0) {
                        randomOutputs.put(mode, candidates[random.nextInt(candidates.length)]);
                    }
                }
                if (!randomOutputs.isEmpty()) return new SpecialTreeRecipe(fluid, 500, randomOutputs);
            }
        }
        return null;
    }

    private static final class SpecialTreeRecipe {

        private final Fluid fluid;
        private final long amount;
        private final EnumMap<Mode, ItemStack> outputs;

        private SpecialTreeRecipe(Fluid fluid, long amount, EnumMap<Mode, ItemStack> outputs) {
            this.fluid = fluid;
            this.amount = amount;
            this.outputs = outputs;
        }
    }

    public static int getModeMultiplier(Mode mode) {
        return switch (mode) {
            case LOG -> 20;
            case SAPLING -> 3;
            case LEAVES -> 8;
            case FRUIT -> 1;
        };
    }

    private static int getModeOutput(TST_MegaTreeFarm machine, Mode mode) {
        for (ItemStack stack : machine.getStoredInputs()) {
            if (stack.getItem() instanceof gregtech.common.items.ItemIntegratedCircuit
                && stack.getItemDamage() == mode.ordinal() + 1) return 1;
        }
        return -1;
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
