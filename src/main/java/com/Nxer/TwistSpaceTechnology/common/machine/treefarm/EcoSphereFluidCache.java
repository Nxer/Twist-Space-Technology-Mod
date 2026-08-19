package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;

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
        int mode = TST_EcoSphereSimulator.getModeFromBeacon(machine.getControllerSlot());
        if (mode < 0 || mode >= MODE_FLUIDS.length) return null;
        Set<Fluid> validFluids = MODE_FLUIDS[mode];
        if (validFluids == null || validFluids.isEmpty()) return null;
        for (FluidStack input : machine.getStoredFluids()) {
            if (input != null && input.amount > 0 && validFluids.contains(input.getFluid())) return input;
        }
        return null;
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
