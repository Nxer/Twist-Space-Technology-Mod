package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport.calculateEut;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.check.CheckRecipeResult;

@Desugar
public record EcoSphereModeResult(CheckRecipeResult result, ItemStack[] outputs, FluidStack[] fluidOutputs, long eut,
    int duration) {

    public EcoSphereModeResult(CheckRecipeResult result, ItemStack[] outputs, long eut, int duration) {
        this(result, outputs, null, eut, duration);
    }

    public static EcoSphereModeResult standard(CheckRecipeResult result, ItemStack[] outputs, int tier) {
        return new EcoSphereModeResult(result, outputs, calculateEut(tier), MODE_RECIPE_DURATION);
    }

    public static EcoSphereModeResult standard(CheckRecipeResult result, ItemStack[] outputs, FluidStack[] fluidOutputs,
        int tier) {
        return new EcoSphereModeResult(result, outputs, fluidOutputs, calculateEut(tier), MODE_RECIPE_DURATION);
    }

    public static EcoSphereModeResult failure(CheckRecipeResult result) {
        return new EcoSphereModeResult(result, null, 0, 0);
    }
}
