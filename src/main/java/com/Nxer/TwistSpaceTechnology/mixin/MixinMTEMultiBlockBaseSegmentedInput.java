package com.Nxer.TwistSpaceTechnology.mixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.ITSTSegmentedFluidInput;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.ITSTSegmentedItemInput;

import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtility;

/** Adds back extra int-sized stacks after GT keeps only one stack per ME type. */
@Mixin(value = MTEMultiBlockBase.class, remap = false)
public abstract class MixinMTEMultiBlockBaseSegmentedInput {

    @Shadow
    public ArrayList<MTEHatchInput> mInputHatches;

    @Shadow
    public ArrayList<MTEHatchInputBus> mInputBusses;

    @Inject(method = "getStoredInputsForColor", at = @At("RETURN"), require = 1)
    private void tst$appendSegmentedItemsForColor(Optional<Byte> color,
        CallbackInfoReturnable<ArrayList<ItemStack>> callback) {
        ArrayList<ItemStack> result = callback.getReturnValue();
        // Skip the same stack already added by GT, but keep other stacks of the same item.
        Set<ItemStack> segmentsAlreadyPresent = Collections.newSetFromMap(new IdentityHashMap<>());
        segmentsAlreadyPresent.addAll(result);
        for (MTEHatchInputBus bus : GTUtility.validMTEList(mInputBusses)) {
            if (!(bus instanceof ITSTSegmentedItemInput segmentedInput)) continue;
            byte busColor = bus.getColor();
            if (color.isPresent() && busColor != -1 && busColor != color.get()) continue;
            appendAdditionalItemSegments(result, segmentedInput, segmentsAlreadyPresent);
        }
    }

    @Inject(method = "getAllStoredInputs", at = @At("RETURN"), require = 1)
    private void tst$appendAllSegmentedItems(CallbackInfoReturnable<ArrayList<ItemStack>> callback) {
        ArrayList<ItemStack> result = callback.getReturnValue();
        Set<ItemStack> segmentsAlreadyPresent = Collections.newSetFromMap(new IdentityHashMap<>());
        segmentsAlreadyPresent.addAll(result);
        for (MTEHatchInputBus bus : GTUtility.validMTEList(mInputBusses)) {
            if (bus instanceof ITSTSegmentedItemInput segmentedInput) {
                appendAdditionalItemSegments(result, segmentedInput, segmentsAlreadyPresent);
            }
        }
    }

    @Inject(method = "getStoredFluidsForColor", at = @At("RETURN"), require = 1)
    private void tst$appendSegmentedFluids(Optional<Byte> color,
        CallbackInfoReturnable<ArrayList<FluidStack>> callback) {
        ArrayList<FluidStack> result = callback.getReturnValue();
        // Skip the same stack already added by GT, but keep other stacks of the same fluid.
        Set<FluidStack> segmentsAlreadyPresent = Collections.newSetFromMap(new IdentityHashMap<>());
        segmentsAlreadyPresent.addAll(result);
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            if (!(hatch instanceof ITSTSegmentedFluidInput segmentedInput)) continue;
            byte hatchColor = hatch.getColor();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            for (FluidStack fluid : segmentedInput.getTSTStoredFluidSegments()) {
                if (fluid != null && !segmentsAlreadyPresent.remove(fluid)) result.add(fluid);
            }
        }
    }

    private static void appendAdditionalItemSegments(ArrayList<ItemStack> result, ITSTSegmentedItemInput segmentedInput,
        Set<ItemStack> segmentsAlreadyPresent) {
        for (ItemStack stack : segmentedInput.getTSTStoredItemSegments()) {
            if (stack != null && !segmentsAlreadyPresent.remove(stack)) result.add(stack);
        }
    }
}
