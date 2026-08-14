package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.addSplitStack;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.getItemStackString;
import static com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults.ModeBeaconInputMismatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
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
        // Read the input fluid once to select the recipe and its fluid cost.
        AquaticRecipe recipe = selectRecipe(machine);
        if (recipe == null) return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        if (recipe.growsAlgae() && machine.getModeBeaconTier() < 2)
            return EcoSphereModeResult.failure(ModeBeaconInputMismatch);

        // Both distilled-water and unknown-water recipes support focusing on one output.
        ItemStack focusStack = findFocusStack(machine, recipe.growsAlgae());
        long fluidPerParallel = machine.applyStructureFluidDiscount(recipe.fluidPerParallel());
        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, recipe.fluid(), fluidPerParallel, euTier);

        // The lowest power tier runs two parallels, so startup requires twice the per-parallel fluid.
        if (parallelResult == null) return EcoSphereModeResult
            .failure(EcoSphereModeSupport.missingFluid(machine, recipe.fluid(), fluidPerParallel * 2));

        List<ItemStack> outputs = recipe.growsAlgae() ? processUnknownWaterOutputs(parallelResult, focusStack)
            : processStandardOutputs(machine, parallelResult, focusStack);
        return EcoSphereModeResult.standard(
            getRunningResult(recipe.growsAlgae(), focusStack),
            outputs.toArray(new ItemStack[0]),
            parallelResult.tier());
    }

    private static AquaticRecipe selectRecipe(TST_EcoSphereSimulator machine) {
        Fluid distilledWater = FluidRegistry.getFluid("ic2distilledwater");
        Fluid unknownWater = FluidRegistry.getFluid("unknowwater");
        for (FluidStack input : machine.getStoredFluids()) {
            if (input == null || input.amount <= 0) continue;
            if (input.getFluid() == unknownWater) {
                return new AquaticRecipe(unknownWater, AquaticZoneSimulatorFakeRecipe.UNKNOWN_WATER_PER_PARALLEL, true);
            }
            if (input.getFluid() == distilledWater) {
                return new AquaticRecipe(
                    distilledWater,
                    AquaticZoneSimulatorFakeRecipe.DISTILLED_WATER_PER_PARALLEL,
                    false);
            }
        }
        return null;
    }

    private static CheckRecipeResult getRunningResult(boolean growsAlgae, ItemStack focusStack) {
        if (focusStack == null) {
            // #tr GT5U.gui.text.recipe_result.growing_algae
            // # {\GREEN}Growing Algae
            // #zh_CN {\GREEN}藻类生长中

            // #tr GT5U.gui.text.recipe_result.fishing
            // # {\BLUE}Fishing
            // #zh_CN {\BLUE}捕鱼中
            return SimpleCheckRecipeResult.ofSuccess(growsAlgae ? "growing_algae" : "fishing");
        }
        return SimpleResultWithText.ofSuccessText(
            // #tr GT5U.gui.text.recipe_result.focus_on
            // # {\BLUE}Targeting
            // #zh_CN {\BLUE}定向中

            StatCollector.translateToLocal("GT5U.gui.text.recipe_result.focus_on") + "\n"
            // #tr EcoSphereSimulator.gui.focusOn
            // # On :
            // #zh_CN 定向目标
                + StatCollector.translateToLocal("EcoSphereSimulator.gui.focusOn")
                + " "
                + focusStack.getDisplayName());
    }

    @Desugar
    private record AquaticRecipe(Fluid fluid, long fluidPerParallel, boolean growsAlgae) {}

    private static List<ItemStack> processUnknownWaterOutputs(EcoSphereModeSupport.ParallelResult parallelResult,
        ItemStack focusStack) {
        boolean focusMode = focusStack != null;
        List<ItemStack> outputs = new ArrayList<>();
        double tierChance = Math.log(parallelResult.tier() + 2) / Math.log(2);
        for (ItemStack template : AquaticZoneSimulatorFakeRecipe.UnknownWaterOutputs) {
            int chance = AquaticZoneSimulatorFakeRecipe.UnknownWaterChances.get(getItemStackString(template));
            if (focusMode) chance = template.isItemEqual(focusStack) ? chance * 50 : Math.max(chance / 50, 1);
            addRandomOutput(
                outputs,
                template,
                chance,
                XSTR.XSTR_INSTANCE.nextInt(AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE),
                tierChance,
                parallelResult.multiplier());
        }
        return outputs;
    }

    private static List<ItemStack> processStandardOutputs(TST_EcoSphereSimulator machine,
        EcoSphereModeSupport.ParallelResult parallelResult, ItemStack focusStack) {
        boolean focusMode = focusStack != null;
        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack recipeStack : AquaticZoneSimulatorFakeRecipe.WatersOutputs) {
            ItemStack output = recipeStack.copy();
            int chance = AquaticZoneSimulatorFakeRecipe.WatersChances.get(getItemStackString(output));
            if (focusMode) chance = output.isItemEqual(focusStack) ? chance * 50 : Math.max(chance / 50, 1);
            int random = XSTR.XSTR_INSTANCE.nextInt(AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE);
            double tierChance = Math.log(parallelResult.tier() + 2) / Math.log(2);
            if (machine.isOffspring(output)) {
                int offspringChance = calculateOffspringChance(machine, chance, tierChance);
                if (random >= offspringChance) continue;
                addSplitStack(outputs, output, 1);
                continue;
            }
            addRandomOutput(outputs, output, chance, random, tierChance, parallelResult.multiplier());
        }
        return outputs;
    }

    private static int calculateOffspringChance(TST_EcoSphereSimulator machine, int baseChance, double tierChance) {
        if (machine.getAvailableInputPower() <= Integer.MAX_VALUE) return 0;
        int maxVoltageTier = (int) Math.floor(TstUtils.calculateVoltageTier((double) Integer.MAX_VALUE + 1));
        long maximumInputPower = (long) Integer.MAX_VALUE * Integer.MAX_VALUE;
        int maximumInputTier = (int) Math.floor(TstUtils.calculateVoltageTier(maximumInputPower));
        double maxTierChance = Math.log(maxVoltageTier + 2) / Math.log(2);
        double probability = 0.00005 * baseChance * tierChance / maxTierChance;
        if (machine.isTierTwo()) probability *= AquaticZoneSimulatorFakeRecipe.OFFSPRING_TIER_TWO_MULTIPLIER;

        int voltageTier = (int) Math.floor(TstUtils.calculateVoltageTier(machine.getAvailableInputPower()));
        double voltageProgress = Math
            .max(0, Math.min(1, (double) (voltageTier - maxVoltageTier) / (maximumInputTier - maxVoltageTier)));
        // Voltage gain rises slowly near MAX and reaches 1.6x at the maximum input power.
        double voltageMultiplier = 1 + 0.6 * voltageProgress * voltageProgress;
        return (int) Math.min(
            AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE,
            Math.round(probability * voltageMultiplier * AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE));
    }

    private static void addRandomOutput(List<ItemStack> outputs, ItemStack template, int chance, int random,
        double tierChance, double multiplier) {
        if (random > chance * tierChance) return;
        double amountScale = (double) AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE
            * AquaticZoneSimulatorFakeRecipe.CHANCE_SCALE
            / 100;
        long amount = (long) (template.stackSize * multiplier * chance * random / amountScale);
        addSplitStack(outputs, template, amount);
    }

    private static ItemStack findFocusStack(TST_EcoSphereSimulator machine, boolean unknownWaterRecipe) {
        Map<String, Integer> availableOutputs = unknownWaterRecipe ? AquaticZoneSimulatorFakeRecipe.UnknownWaterChances
            : AquaticZoneSimulatorFakeRecipe.WatersChances;
        for (ItemStack input : machine.getStoredInputs()) {
            if (input == null || input.getItem() == null) continue;
            if (availableOutputs.containsKey(getItemStackString(input))) return input;
        }
        return null;
    }
}
