package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.addSplitStack;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.getItemStackString;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe.WatersChances;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe.WatersOutputs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;

public final class AquaticZoneSimulatorMode implements IEcoSphereMode {

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.AquaticZoneSimulatorFakeRecipes;
    }

    @Override
    public String getDisplayName() {
        return net.minecraft.util.StatCollector.translateToLocal("EcoSphereSimulator.modeMsg.1");
    }

    @Override
    public EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier) {
        ItemStack focusStack = findFocusStack(machine);
        boolean focusMode = focusStack != null;
        net.minecraftforge.fluids.Fluid requiredFluid = FluidRegistry.getFluid("ic2distilledwater");
        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, requiredFluid, 10000L, euTier);
        if (parallelResult == null)
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_enough_input"));

        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack recipeStack : WatersOutputs) {
            ItemStack output = recipeStack.copy();
            int chance = WatersChances.get(getItemStackString(output));
            int random = XSTR.XSTR_INSTANCE.nextInt(10000);
            double tierChance = Math.log(parallelResult.tier() + 2) / Math.log(2);
            if (machine.isOffspring(output)) {
                if (machine.getAvailableInputPower() <= Integer.MAX_VALUE) continue;
                int offspringChance = machine.isTierTwo() ? chance * 41 : chance;
                if (random > offspringChance * tierChance) continue;
                addSplitStack(outputs, output, 1);
                continue;
            }
            if (focusMode) chance = output.isItemEqual(focusStack) ? chance * 50 : Math.max(chance / 50, 1);
            if (random > chance * tierChance) continue;
            long amount = (long) (output.stackSize * parallelResult.multiplier() * chance * random / 1_000_000d);
            addSplitStack(outputs, output, amount);
        }
        return EcoSphereModeResult.standard(
            focusMode ? SimpleResultWithText.ofSuccessText(
                StatCollector.translateToLocal("GT5U.gui.text.recipe_result.focus_on") + "\n"
                    + StatCollector.translateToLocal("MegaTreeFarm.gui.focusOn")
                    + ": "
                    + focusStack.getDisplayName())
                : SimpleCheckRecipeResult.ofSuccess("fishing"),
            outputs.toArray(new ItemStack[0]),
            parallelResult.tier());
    }

    private static ItemStack findFocusStack(TST_MegaTreeFarm machine) {
        for (ItemStack input : machine.getStoredInputs()) {
            if (input != null && input.getItem() != null && WatersChances.containsKey(getItemStackString(input))) {
                return input;
            }
        }
        return null;
    }
}
