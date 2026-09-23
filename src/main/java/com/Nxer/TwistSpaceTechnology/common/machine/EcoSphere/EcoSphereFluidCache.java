package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellInputHatch.LongFluidInputs;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;

public final class EcoSphereFluidCache {

    public static final int TREE_MODE = 0;
    public static final int AQUATIC_MODE = 1;
    public static final int GREENHOUSE_MODE = 2;
    public static final int CLONER_MODE = 3;

    @SuppressWarnings("unchecked")
    private static final Set<Fluid>[] MODE_FLUIDS = new Set[4];

    private EcoSphereFluidCache() {}

    public static FluidStack findFirstValidFluid(TST_EcoSphereSimulator machine) {
        return findFirstValidFluid(machine, null);
    }

    public static FluidStack findFirstValidFluid(TST_EcoSphereSimulator machine, Fluid fallbackFluid) {
        int mode = TST_EcoSphereSimulator.getModeFromExecutionProtocol(machine.getControllerSlot());
        if (mode < 0 || mode >= MODE_FLUIDS.length) return null;
        Set<Fluid> validFluids = MODE_FLUIDS[mode];
        if (validFluids == null || validFluids.isEmpty()) return null;
        LongFluidInputs inputs = LongFluidInputs.of(machine);
        Fluid fluid = null;
        if (fallbackFluid == null) {
            fluid = inputs.getFirstAvailableFluid(validFluids);
        } else {
            for (Fluid candidate : inputs.getFluidTypes()) {
                if (!validFluids.contains(candidate)) continue;
                fluid = candidate;
                if (candidate != fallbackFluid) break;
            }
        }
        return fluid == null ? null : new FluidStack(fluid, 1);
    }

    // Build this once when a mode finishes registering its fake recipes.
    public static void cacheRecipeFluids(int mode, RecipeMap<?> recipeMap) {
        if (mode < 0 || mode >= MODE_FLUIDS.length) return;
        Set<Fluid> fluids = new HashSet<>();
        for (GTRecipe recipe : recipeMap.getAllRecipes()) {
            if (recipe.mFluidInputs == null) continue;
            for (FluidStack input : recipe.mFluidInputs) {
                if (input.getFluid() != null) fluids.add(input.getFluid());
            }
        }
        MODE_FLUIDS[mode] = fluids;
    }
}
