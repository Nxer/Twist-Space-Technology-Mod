package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport.addSplitStack;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport.getItemStackString;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereFluidCache;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class AquaticZoneSimulatorMode implements IEcoSphereMode {

    private static final int DISTILLED_WATER_RECIPE = 1;
    private static final int UNKNOWN_WATER_RECIPE = 2;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.AquaticZoneSimulatorFakeRecipes;
    }

    @Override
    public String getDisplayName() {
        return StatCollector.translateToLocal("EcoSphereSimulator.modeMsg.1");
    }

    @Override
    public boolean displaysFluidArea() {
        return true;
    }

    @Override
    public EcoSphereModeResult process(TST_EcoSphereSimulator machine, int euTier) {
        FluidStack fluidInput = EcoSphereFluidCache.findFirstValidFluid(machine);
        if (fluidInput == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        AquaticRecipe recipe = findRecipe(fluidInput.getFluid());
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        if (recipe.recipeType() == UNKNOWN_WATER_RECIPE && machine.getModeBeaconTier() < 2)
            return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        // Both distilled-water and unknown-water recipes support focusing on one output.
        TargetingSelection targeting = findTargetingSelection(machine, recipe.recipeType());
        Function<EcoSphereModeSupport.ParallelResult, EcoSphereModeResult> processor = parallelResult -> {
            List<ItemStack> outputs;
            if (recipe.recipeType() == UNKNOWN_WATER_RECIPE) {
                outputs = processUnknownWaterOutputs(parallelResult, targeting);
            } else {
                outputs = processStandardOutputs(parallelResult, targeting);
            }
            ItemStack focusStack = null;
            if (targeting != null) focusStack = targeting.stack();
            return EcoSphereModeResult.standard(
                getRunningResult(recipe.recipeType(), focusStack),
                outputs.toArray(new ItemStack[0]),
                parallelResult.tier());
        };
        FluidStack recipeFluid = recipe.fluidInput();
        return EcoSphereModeSupport
            .processModeRecipeWithTier(machine, recipeFluid.getFluid(), recipeFluid.amount, euTier, processor);
    }

    private static AquaticRecipe findRecipe(Fluid fluid) {
        FluidStack distilledWater = AquaticZoneSimulatorFakeRecipe.DISTILLED_WATER_STACK;
        if (distilledWater != null && fluid == distilledWater.getFluid())
            return new AquaticRecipe(distilledWater, DISTILLED_WATER_RECIPE);
        FluidStack unknownWater = AquaticZoneSimulatorFakeRecipe.UNKNOWN_WATER_STACK;
        if (unknownWater != null && fluid == unknownWater.getFluid())
            return new AquaticRecipe(unknownWater, UNKNOWN_WATER_RECIPE);
        return null;
    }

    private static CheckRecipeResult getRunningResult(int recipeType, ItemStack focusStack) {
        if (focusStack == null) {
            // #tr GT5U.gui.text.recipe_result.growing_algae
            // # {\GREEN}Growing Algae
            // #zh_CN {\GREEN}藻类生长中

            // #tr GT5U.gui.text.recipe_result.fishing
            // # {\BLUE}Fishing
            // #zh_CN {\BLUE}捕鱼中
            return SimpleCheckRecipeResult.ofSuccess(recipeType == UNKNOWN_WATER_RECIPE ? "growing_algae" : "fishing");
        }
        return SimpleResultWithText.ofSuccessText(
            // #tr GT5U.gui.text.recipe_result.focus_on
            // # {\BLUE}Targeting
            // #zh_CN {\BLUE}定向中

            StatCollector.translateToLocal("GT5U.gui.text.recipe_result.focus_on") + "\n"
            // #tr EcoSphereSimulator.gui.focusOn
            // # On :
            // #zh_CN 目标 :
                + StatCollector.translateToLocal("EcoSphereSimulator.gui.focusOn")
                + " "
                + focusStack.getDisplayName());
    }

    @Desugar
    private record AquaticRecipe(FluidStack fluidInput, int recipeType) {}

    @Desugar
    private record TargetingSelection(ItemStack stack, int multiplier) {}

    private static List<ItemStack> processUnknownWaterOutputs(EcoSphereModeSupport.ParallelResult parallelResult,
        TargetingSelection targeting) {
        boolean focusMode = targeting != null;
        List<ItemStack> outputs = new ArrayList<>();
        double tierChance = Math.log(parallelResult.tier() + 2) / Math.log(2);
        for (ItemStack template : AquaticZoneSimulatorFakeRecipe.UnknownWaterOutputs) {
            int chance = AquaticZoneSimulatorFakeRecipe.UnknownWaterChances.get(getItemStackString(template));
            if (focusMode) {
                if (template.isItemEqual(targeting.stack())) {
                    chance *= targeting.multiplier();
                } else {
                    chance = Math.max(chance / targeting.multiplier(), 1);
                }
            }
            addRandomOutput(
                outputs,
                template,
                chance,
                XSTR.XSTR_INSTANCE.nextInt(AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE),
                tierChance,
                parallelResult.parallel());
        }
        return outputs;
    }

    private static List<ItemStack> processStandardOutputs(EcoSphereModeSupport.ParallelResult parallelResult,
        TargetingSelection targeting) {
        boolean focusMode = targeting != null;
        List<ItemStack> outputs = new ArrayList<>();
        double tierChance = Math.log(parallelResult.tier() + 2) / Math.log(2);
        for (ItemStack recipeStack : AquaticZoneSimulatorFakeRecipe.WatersOutputs) {
            ItemStack output = recipeStack.copy();
            int chance = AquaticZoneSimulatorFakeRecipe.WatersChances.get(getItemStackString(output));
            if (focusMode) {
                if (output.isItemEqual(targeting.stack())) {
                    chance *= targeting.multiplier();
                } else {
                    chance = Math.max(chance / targeting.multiplier(), 1);
                }
            }
            int random = XSTR.XSTR_INSTANCE.nextInt(AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE);
            if (output.isItemEqual(AquaticZoneSimulatorFakeRecipe.OFFSPRING)) {
                int offspringChance = calculateOffspringChance(parallelResult.tier(), chance, tierChance);
                if (random >= offspringChance) continue;
                addSplitStack(outputs, output, 1);
                continue;
            }
            addRandomOutput(outputs, output, chance, random, tierChance, parallelResult.parallel());
        }
        return outputs;
    }

    private static int calculateOffspringChance(int voltageTier, int baseChance, double tierChance) {
        int maxVoltageTier = (int) Math.floor(TstUtils.calculateVoltageTier((double) Integer.MAX_VALUE + 1));
        if (voltageTier < maxVoltageTier) return 0;
        long maximumInputPower = (long) Integer.MAX_VALUE * Integer.MAX_VALUE;
        int maximumInputTier = (int) Math.floor(TstUtils.calculateVoltageTier(maximumInputPower));
        double maxTierChance = Math.log(maxVoltageTier + 2) / Math.log(2);
        double probability = 0.00025 * baseChance * tierChance / maxTierChance;

        double voltageProgress = Math
            .max(0, Math.min(1, (double) (voltageTier - maxVoltageTier) / (maximumInputTier - maxVoltageTier)));
        // Voltage gain rises slowly near MAX and reaches 1.6x at the maximum input power.
        double voltageMultiplier = 1 + 0.6 * voltageProgress * voltageProgress;
        return (int) Math.min(
            AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE,
            Math.round(probability * voltageMultiplier * AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE));
    }

    private static void addRandomOutput(List<ItemStack> outputs, ItemStack template, int chance, int random,
        double tierChance, double parallel) {
        if (random > chance * tierChance) return;
        double amountScale = (double) AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE
            * AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE
            / 100;
        long amount = (long) (template.stackSize * parallel * chance * random / amountScale);
        addSplitStack(outputs, template, amount);
    }

    private static TargetingSelection findTargetingSelection(TST_EcoSphereSimulator machine, int recipeType) {
        Map<String, Integer> availableOutputs;
        if (recipeType == UNKNOWN_WATER_RECIPE) {
            availableOutputs = AquaticZoneSimulatorFakeRecipe.UnknownWaterChances;
        } else {
            availableOutputs = AquaticZoneSimulatorFakeRecipe.WatersChances;
        }
        for (ItemStack input : machine.getModeInputs()) {
            if (input == null || input.getItem() == null) continue;
            if (availableOutputs.containsKey(getItemStackString(input))) {
                int multiplier = machine.getAquaticTargetingMultiplier();
                if (multiplier <= 0) return null;
                return new TargetingSelection(input, multiplier);
            }
        }
        return null;
    }
}
