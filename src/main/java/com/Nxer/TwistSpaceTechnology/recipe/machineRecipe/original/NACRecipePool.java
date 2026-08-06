package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.TierEU.RECIPE_UIV;

import com.Nxer.TwistSpaceTechnology.common.item.NACComponentRegistry;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.NanochipAssemblyMatrixTierKey;
import gregtech.api.util.GTRecipeConstants;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleRecipeInfo;
import gtPlusPlus.core.material.MaterialMisc;

/**
 * Registers custom NAC recipes during the normal recipe-loading phase.
 *
 * <p>
 * Register custom components first in {@link NACComponentRegistry}, then use
 * {@link CircuitComponent#getFakeStack(int)} for NAC component inputs and outputs.
 * </p>
 */
public final class NACRecipePool {

    public static void loadRecipes() {
        // Accessing a built-in component initializes the enum and registers the custom components.
        CircuitComponent opticalProcessor = CircuitComponent.OpticalProcessor;

        // Package optical SOC
        GTValues.RA.stdBuilder()
            .itemInputs(NACComponentRegistry.opticalSOC.getFakeStack(1))
            .itemOutputs(NACComponentRegistry.processedOpticalSOC.getFakeStack(1))
            .duration(30 * 20)
            .eut(ModuleRecipeInfo.ExtremeTier.recipeEUt)
            .addTo(RecipeMaps.nanochipOpticalOrganizer);

        // Package infinity bolt
        GTValues.RA.stdBuilder()
            .itemInputs(NACComponentRegistry.boltInfinity.getFakeStack(1))
            .fluidInputs(Materials.Lubricant.getFluid(20))
            .itemOutputs(NACComponentRegistry.processedBoltInfinity.getFakeStack(1))
            .duration(5 * 20)
            .eut(ModuleRecipeInfo.LowTier.recipeEUt)
            .addTo(RecipeMaps.nanochipCuttingChamber);

        // Optical process with soc
        GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyMatrixTierKey.INSTANCE, VoltageIndex.UHV)
            .metadata(GTRecipeConstants.CIRCUIT_CALIBRATION_TYPE, opticalProcessor.circuitType)
            .itemInputs(
                CircuitComponent.ProcessedBoardOptical.getFakeStack(1),
                NACComponentRegistry.processedOpticalSOC.getFakeStack(1),
                CircuitComponent.ProcessedCableOpticalFiber.getFakeStack(1),
                NACComponentRegistry.processedBoltInfinity.getFakeStack(3))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(18))
            .itemOutputs(opticalProcessor.getFakeStack(1))
            .duration(3 * 20)
            .eut(RECIPE_UIV)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    }
}
