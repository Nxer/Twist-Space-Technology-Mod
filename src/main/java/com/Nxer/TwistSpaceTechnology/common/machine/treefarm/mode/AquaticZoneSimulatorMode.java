package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.addSplitStack;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.calculateEut;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.getItemStackString;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe.WatersChances;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe.WatersOutputs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;
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
    public EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier, double tierMultiplier) {
        net.minecraftforge.fluids.Fluid requiredFluid = machine.isTierTwo() ? FluidRegistry.WATER
            : FluidRegistry.getFluid("ic2distilledwater");
        EcoSphereModeSupport.ParallelResult parallelResult = EcoSphereModeSupport
            .consumeFluidForParallel(machine, requiredFluid, 10000L, euTier);
        if (parallelResult == null)
            return EcoSphereModeResult.failure(SimpleCheckRecipeResult.ofFailure("no_enough_input"));

        ItemStack controllerStack = machine.getControllerSlot();
        boolean focusMode = controllerStack != null && WatersChances.containsKey(getItemStackString(controllerStack));
        machine.setFocusMode(focusMode);
        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack recipeStack : WatersOutputs) {
            ItemStack output = recipeStack.copy();
            int chance = WatersChances.get(getItemStackString(output));
            int random = XSTR.XSTR_INSTANCE.nextInt(10000);
            double tierChance = Math.log(parallelResult.tier() + 2) / Math.log(2);
            if (machine.isOffspring(output)) {
                if (machine.getAvailableInputPower() <= Integer.MAX_VALUE) continue;
                int offspringChance = machine.isTierTwo() ? chance * 5 : chance;
                if (random > offspringChance * tierChance) continue;
                addSplitStack(outputs, output, 1);
                continue;
            }
            if (focusMode) chance = output.isItemEqual(controllerStack) ? chance * 50 : Math.max(chance / 50, 1);
            if (random > chance * tierChance) continue;
            long amount = (long) (output.stackSize * parallelResult.multiplier() * chance * random / 1_000_000d);
            addSplitStack(outputs, output, amount);
        }
        return new EcoSphereModeResult(
            SimpleCheckRecipeResult.ofSuccess(focusMode ? "focus_on" : "fishing"),
            outputs.toArray(new ItemStack[0]),
            calculateEut(parallelResult.tier()),
            machine.getStandardModeDuration());
    }
}
