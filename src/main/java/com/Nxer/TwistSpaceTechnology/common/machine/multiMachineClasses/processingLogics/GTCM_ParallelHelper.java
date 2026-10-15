package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;

import gregtech.api.enums.GTValues;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.VoidProtectionHelper;

public class GTCM_ParallelHelper extends ParallelHelper {

    private boolean calculateOutputsAsLong;

    private final List<ItemStackLong> longItemOutputs = new ArrayList<>();

    private final List<FluidStackLong> longFluidOutputs = new ArrayList<>();

    // endregion
    public GTCM_ParallelHelper() {}

    public GTCM_ParallelHelper enableLongOutputCalculation() {
        calculateOutputsAsLong = true;
        return this;
    }

    public boolean isCalculatingOutputsAsLong() {
        return calculateOutputsAsLong;
    }

    public List<ItemStackLong> getLongItemOutputs() {
        if (!built || !calculateOutputsAsLong) {
            throw new IllegalStateException("Tried to get long item outputs when long output calculation is disabled");
        }
        return longItemOutputs;
    }

    public List<FluidStackLong> getLongFluidOutputs() {
        if (!built || !calculateOutputsAsLong) {
            throw new IllegalStateException("Tried to get long fluid outputs when long output calculation is disabled");
        }
        return longFluidOutputs;
    }

    /**
     * Called by build(). Determines the parallels and everything else that needs to be done at build time
     */
    protected void determineParallel() {

        if (maxParallel <= 0) {
            return;
        }

        if (itemInputs == null) {
            itemInputs = GTValues.emptyItemStackArray;
        }
        if (fluidInputs == null) {
            fluidInputs = GTValues.emptyFluidStackArray;
        }

        if (!consume) {
            copyInputs();
        }

        if (calculator == null) {
            calculator = new OverclockCalculator().setEUt(availableEUt)
                .setRecipeEUt(recipe.mEUt)
                .setDuration(recipe.mDuration)
                .setEUtDiscount(eutModifier);
        }

        double heatDiscountMultiplier = calculator.calculateHeatDiscountMultiplier();

        final long tRecipeEUt = (long) Math.ceil(recipe.mEUt * eutModifier * heatDiscountMultiplier);
        if (availableEUt < tRecipeEUt) {
            result = CheckRecipeResultRegistry.insufficientPower(tRecipeEUt);
            return;
        }

        // Save the original max parallel before calculating our overclocking under 1 tick
        int originalMaxParallel = maxParallel;
        calculator.setParallel(originalMaxParallel);

        // If the machine has custom supplier, use old method for giving parallels for overclocking too much, otherwise
        // use multiplier
        if (calculator.hasDurationUnderOneTickSupplier()) {
            if (calculator.getDurationUnderOneTickSupplier() < 1) {
                maxParallel = GTUtility.safeInt((long) (maxParallel / calculator.getDurationUnderOneTickSupplier()), 0);
            }
        } else {
            maxParallel = GTUtility.safeInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()), 0);
        }

        int maxParallelBeforeBatchMode = maxParallel;
        if (batchMode) {
            maxParallel = GTUtility.safeInt((long) maxParallel * batchModifier, 1);
        }

        final ItemStack[] truncatedItemOutputs = recipe.mOutputs != null
            ? Arrays.copyOfRange(recipe.mOutputs, 0, Math.min(machine.getItemOutputLimit(), recipe.mOutputs.length))
            : new ItemStack[0];
        final FluidStack[] truncatedFluidOutputs = recipe.mFluidOutputs != null ? Arrays
            .copyOfRange(recipe.mFluidOutputs, 0, Math.min(machine.getFluidOutputLimit(), recipe.mFluidOutputs.length))
            : new FluidStack[0];

        SingleRecipeCheck recipeCheck = null;
        SingleRecipeCheck.Builder tSingleRecipeCheckBuilder = null;
        if (isRecipeLocked && singleRecipeMachine != null) {
            recipeCheck = singleRecipeMachine.getSingleRecipeCheck();
            if (recipeCheck == null) {
                // Machine is configured to lock to a single recipe, but haven't built the recipe checker yet.
                // Build the checker on next successful recipe.
                RecipeMap<?> recipeMap = singleRecipeMachine.getRecipeMap();
                if (recipeMap != null) {
                    tSingleRecipeCheckBuilder = SingleRecipeCheck.builder(recipeMap)
                        .setBefore(itemInputs, fluidInputs);
                }
            }
        }

        // Let's look at how many parallels we can get with void protection
        if (protectExcessItem || protectExcessFluid) {
            if (machine == null) {
                throw new IllegalStateException("Tried to calculate void protection, but machine is not set");
            }
            VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
            voidProtectionHelper.setMachine(machine)
                .setItemOutputs(truncatedItemOutputs)
                .setFluidOutputs(truncatedFluidOutputs)
                .setOutputChanceGetter(recipe::getOutputChance)
                .setOutputChanceMultiplier(chanceMultiplier)
                .setMaxParallel(maxParallel)
                .build();
            maxParallel = Math.min(voidProtectionHelper.getMaxParallel(), maxParallel);
            if (voidProtectionHelper.isItemFull()) {
                result = CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                return;
            }
            if (voidProtectionHelper.isFluidFull()) {
                result = CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
                return;
            }
        }

        maxParallelBeforeBatchMode = Math.min(maxParallel, maxParallelBeforeBatchMode);

        // determine normal parallel
        int actualMaxParallel = tRecipeEUt > 0 ? (int) Math.min(maxParallelBeforeBatchMode, availableEUt / tRecipeEUt)
            : maxParallelBeforeBatchMode;
        if (recipeCheck != null) {
            currentParallel = recipeCheck.checkRecipeInputs(true, actualMaxParallel, itemInputs, fluidInputs);
        } else {
            currentParallel = (int) maxParallelCalculator.calculate(recipe, actualMaxParallel, fluidInputs, itemInputs);
            if (currentParallel > 0) {
                if (tSingleRecipeCheckBuilder != null) {
                    // If recipe checker is not built yet, build and set it
                    inputConsumer.consume(recipe, 1, fluidInputs, itemInputs);
                    SingleRecipeCheck builtCheck = tSingleRecipeCheckBuilder.setAfter(itemInputs, fluidInputs)
                        .setRecipe(recipe)
                        .build();
                    singleRecipeMachine.setSingleRecipeCheck(builtCheck);
                    inputConsumer.consume(recipe, currentParallel - 1, fluidInputs, itemInputs);
                } else {
                    inputConsumer.consume(recipe, currentParallel, fluidInputs, itemInputs);
                }
            }
        }

        if (currentParallel <= 0) {
            result = CheckRecipeResultRegistry.INTERNAL_ERROR;
            return;
        }

        calculator.setParallel(Math.min(currentParallel, originalMaxParallel))
            .calculate();

        // If Batch Mode is enabled determine how many extra parallels we can get
        if (batchMode && currentParallel > 0 && calculator.getDuration() < MAX_BATCH_MODE_TICK_TIME) {
            int tExtraParallels;
            double batchMultiplierMax = MAX_BATCH_MODE_TICK_TIME / calculator.getDuration();
            final int maxExtraParallels = (int) Math.floor(
                Math.min(
                    currentParallel * Math.min(batchMultiplierMax - 1, batchModifier - 1),
                    maxParallel - currentParallel));
            if (recipeCheck != null) {
                tExtraParallels = recipeCheck.checkRecipeInputs(true, maxExtraParallels, itemInputs, fluidInputs);
            } else {
                tExtraParallels = (int) maxParallelCalculator
                    .calculate(recipe, maxExtraParallels, fluidInputs, itemInputs);
                inputConsumer.consume(recipe, tExtraParallels, fluidInputs, itemInputs);
            }
            durationMultiplier = 1.0f + (float) tExtraParallels / currentParallel;
            currentParallel += tExtraParallels;
        }

        // If we want to calculate outputs we do it here
        if (calculateOutputs && currentParallel > 0) {
            if (calculateOutputsAsLong) {
                calculateLongOutputs();
            } else if (recipe.mOutputs != null) {
                calculateItemOutputs(truncatedItemOutputs);
            }
            if (!calculateOutputsAsLong && recipe.mFluidOutputs != null) {
                calculateFluidOutputs(truncatedFluidOutputs);
            }
        }
        result = CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void calculateLongOutputs() {
        itemOutputs = GTValues.emptyItemStackArray;
        fluidOutputs = GTValues.emptyFluidStackArray;
        longItemOutputs.clear();
        longFluidOutputs.clear();

        if (customItemOutputCalculation != null) {
            mergeLongItemOutputs(customItemOutputCalculation.apply(currentParallel));
        } else if (recipe.mOutputs != null) {
            for (int i = 0; i < recipe.mOutputs.length; i++) {
                ItemStack origin = recipe.getOutput(i)
                    .copy();
                int outputChance = recipe.getOutputChance(i);
                long outputParallels;
                if (outputChance < 10000) {
                    outputParallels = (long) currentParallel * outputChance / 10000;
                    long remainder = (long) currentParallel * outputChance % 10000;
                    if (remainder > 0 && remainder > XSTR.XSTR_INSTANCE.nextInt(10000)) outputParallels++;
                } else {
                    outputParallels = currentParallel;
                }
                mergeLongItemOutput(origin, multiplySaturated(outputParallels, origin.stackSize));
            }
        }

        if (customFluidOutputCalculation != null) {
            mergeLongFluidOutputs(customFluidOutputCalculation.apply(currentParallel));
        } else if (recipe.mFluidOutputs != null) {
            for (FluidStack recipeOutput : recipe.mFluidOutputs) {
                if (recipeOutput == null) continue;
                mergeLongFluidOutput(recipeOutput, multiplySaturated(recipeOutput.amount, currentParallel));
            }
        }
    }

    private void mergeLongItemOutputs(ItemStack[] outputs) {
        if (outputs == null) return;
        for (ItemStack output : outputs) {
            if (output != null) mergeLongItemOutput(output, output.stackSize);
        }
    }

    private void mergeLongItemOutput(ItemStack stack, long amount) {
        if (stack == null || amount <= 0) return;
        for (int i = 0; i < longItemOutputs.size(); i++) {
            ItemStackLong output = longItemOutputs.get(i);
            if (GTUtility.areStacksEqual(output.itemStack(), stack)) {
                long merged = output.stackSize() > Long.MAX_VALUE - amount ? Long.MAX_VALUE
                    : output.stackSize() + amount;
                longItemOutputs.set(i, new ItemStackLong(output.itemStack(), merged));
                return;
            }
        }
        longItemOutputs.add(new ItemStackLong(GTUtility.copyAmountUnsafe(1, stack), amount));
    }

    private void mergeLongFluidOutputs(FluidStack[] outputs) {
        if (outputs == null) return;
        for (FluidStack output : outputs) {
            if (output != null) mergeLongFluidOutput(output, output.amount);
        }
    }

    private void mergeLongFluidOutput(FluidStack stack, long amount) {
        if (stack == null || amount <= 0) return;
        for (int i = 0; i < longFluidOutputs.size(); i++) {
            FluidStackLong output = longFluidOutputs.get(i);
            if (output.fluidStack()
                .isFluidEqual(stack)) {
                long merged = output.amount() > Long.MAX_VALUE - amount ? Long.MAX_VALUE : output.amount() + amount;
                longFluidOutputs.set(i, new FluidStackLong(output.fluidStack(), merged));
                return;
            }
        }
        FluidStack template = stack.copy();
        template.amount = 1;
        longFluidOutputs.add(new FluidStackLong(template, amount));
    }

    private static long multiplySaturated(long first, long second) {
        if (first <= 0 || second <= 0) return 0;
        return first > Long.MAX_VALUE / second ? Long.MAX_VALUE : first * second;
    }

    @Override
    protected void calculateItemOutputs(ItemStack[] truncatedItemOutputs) {
        if (customItemOutputCalculation != null) {
            itemOutputs = customItemOutputCalculation.apply(currentParallel);
            return;
        }
        ArrayList<ItemStack> toOutput = new ArrayList<>();
        for (int i = 0; i < recipe.mOutputs.length; i++) {
            int outputChance = recipe.getOutputChance(i);
            ItemStack origin = recipe.getOutput(i)
                .copy();
            if (outputChance < 10000) {
                // parameter of this item final amount
                long outputsParallel = (long) currentParallel * outputChance / 10000;
                long remain = (long) currentParallel * outputChance % 10000;

                if (remain > 0) {
                    if (remain > XSTR.XSTR_INSTANCE.nextInt(10000)) {
                        outputsParallel++;
                    }
                }

                long outputs = outputsParallel * origin.stackSize;

                while (outputs >= Integer.MAX_VALUE) {
                    toOutput.add(GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, origin));
                    outputs -= Integer.MAX_VALUE;
                }

                if (outputs > 0) {
                    toOutput.add(GTUtility.copyAmountUnsafe((int) outputs, origin));
                }

            } else {
                long outputs = (long) currentParallel * origin.stackSize;
                while (outputs > Integer.MAX_VALUE) {
                    toOutput.add(GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, origin));
                    outputs -= Integer.MAX_VALUE;
                }

                if (outputs > 0) {
                    toOutput.add(GTUtility.copyAmountUnsafe((int) outputs, origin));
                }
            }

        }

        itemOutputs = toOutput.toArray(new ItemStack[0]);
    }

    @Override
    protected void calculateFluidOutputs(FluidStack[] truncatedFluidOutputs) {
        if (customFluidOutputCalculation != null) {
            fluidOutputs = customFluidOutputCalculation.apply(currentParallel);
            return;
        }
        ArrayList<FluidStack> toOutput = new ArrayList<>();
        fluidOutputs = new FluidStack[recipe.mFluidOutputs.length];
        for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
            if (recipe.getFluidOutput(i) != null) {
                FluidStack tFluid = recipe.getFluidOutput(i)
                    .copy();
                long amount = (long) tFluid.amount * currentParallel;
                while (amount >= Integer.MAX_VALUE) {
                    FluidStack fluid = tFluid.copy();
                    fluid.amount = Integer.MAX_VALUE;
                    toOutput.add(fluid);
                    amount -= Integer.MAX_VALUE;
                }
                FluidStack fluid = tFluid.copy();
                fluid.amount = (int) amount;
                toOutput.add(fluid);
            }
        }
        fluidOutputs = toOutput.toArray(new FluidStack[0]);
    }

}
