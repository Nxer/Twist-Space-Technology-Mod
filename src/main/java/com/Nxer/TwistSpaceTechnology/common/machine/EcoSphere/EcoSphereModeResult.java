package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport.calculateEut;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;

import java.util.Collections;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.check.CheckRecipeResult;

@Desugar
public record EcoSphereModeResult(CheckRecipeResult result, List<ItemStackLong> outputs,
    List<FluidStackLong> fluidOutputs, long eut, int duration) {

    public EcoSphereModeResult(CheckRecipeResult result, List<ItemStackLong> outputs, long eut, int duration) {
        this(result, outputs, Collections.emptyList(), eut, duration);
    }

    public static EcoSphereModeResult standard(CheckRecipeResult result, List<ItemStackLong> outputs, int tier) {
        return new EcoSphereModeResult(result, outputs, calculateEut(tier), MODE_RECIPE_DURATION);
    }

    public static EcoSphereModeResult standard(CheckRecipeResult result, List<ItemStackLong> outputs,
        List<FluidStackLong> fluidOutputs, int tier) {
        return new EcoSphereModeResult(result, outputs, fluidOutputs, calculateEut(tier), MODE_RECIPE_DURATION);
    }

    public static EcoSphereModeResult failure(CheckRecipeResult result) {
        return new EcoSphereModeResult(result, Collections.emptyList(), 0, 0);
    }
}
