package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.MODE_RECIPE_DURATION;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.calculateEut;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.check.CheckRecipeResult;

public final class EcoSphereModeResult {

    private final CheckRecipeResult result;
    private final ItemStack[] outputs;
    private final FluidStack[] fluidOutputs;
    private final long eut;
    private final int duration;

    public EcoSphereModeResult(CheckRecipeResult result, ItemStack[] outputs, long eut, int duration) {
        this(result, outputs, null, eut, duration);
    }

    public EcoSphereModeResult(CheckRecipeResult result, ItemStack[] outputs, FluidStack[] fluidOutputs, long eut,
        int duration) {
        this.result = result;
        this.outputs = outputs;
        this.fluidOutputs = fluidOutputs;
        this.eut = eut;
        this.duration = duration;
    }

    public CheckRecipeResult result() {
        return result;
    }

    public ItemStack[] outputs() {
        return outputs;
    }

    public FluidStack[] fluidOutputs() {
        return fluidOutputs;
    }

    public long eut() {
        return eut;
    }

    public int duration() {
        return duration;
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
