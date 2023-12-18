package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import gregtech.api.recipe.RecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.VoidProtectionHelper;

// spotless:off
public class GTCM_ParallelHelper extends GT_ParallelHelper {

    // region Variables
    private static final double MAX_BATCH_MODE_TICK_TIME = 128;
    /**
     * Machine used for calculation
     */
    private IVoidable machine;
    /**
     * Machine used for single recipe locking calculation
     */
    private IRecipeLockable singleRecipeMachine;
    /**
     * Is locked to a single recipe?
     */
    private boolean isRecipeLocked;
    /**
     * Recipe used when trying to calculate parallels
     */
    private GT_Recipe recipe;
    /**
     * EUt available to the multiblock (This should be the total eut available)
     */
    private long availableEUt;
    /**
     * The current parallel possible for the multiblock
     */
    private int currentParallel = 0;
    /**
     * The maximum possible parallel possible for the multiblock
     */
    private int maxParallel = 1;
    /**
     * The Batch Modifier applied when batch mode is enabled. 1 does nothing. 2 doubles max possible
     * parallel, but also duration
     */
    private int batchModifier = 1;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    private ItemStack[] itemInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    private ItemStack[] itemOutputs;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    private FluidStack[] fluidInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    private FluidStack[] fluidOutputs;
    /**
     * Does the multi have void protection enabled for items
     */
    private boolean protectExcessItem;
    /**
     * Does the multi have void protection enabled for fluids
     */
    private boolean protectExcessFluid;
    /**
     * Should the Parallel Helper automatically consume for the multi
     */
    private boolean consume;
    /**
     * Is batch mode turned on?
     */
    private boolean batchMode;
    /**
     * Should the Parallel Helper automatically calculate the outputs of the recipe with current parallel?
     */
    private boolean calculateOutputs;
    /**
     * Has the Parallel Helper been built?
     */
    private boolean built;
    /**
     * What is the duration multiplier with batch mode enabled
     */
    private double durationMultiplier;
    /**
     * Modifier which is applied on the recipe eut. Useful for GT++ machines
     */
    private float eutModifier = 1;
    /**
     * Method for calculating max parallel from given inputs.
     */
    private MaxParallelCalculator maxParallelCalculator = GT_Recipe::maxParallelCalculatedByInputs;
    /**
     * Method for consuming inputs after determining how many parallels it can execute.
     */
    private InputConsumer inputConsumer = GT_Recipe::consumeInput;

    /**
     * Calculator to use for overclocking
     */
    private GT_OverclockCalculator calculator;

    private CheckRecipeResult result = CheckRecipeResultRegistry.NONE;

    private Function<Integer, ItemStack[]> customItemOutputCalculation;

    private Function<Integer, FluidStack[]> customFluidOutputCalculation;

    // endregion
    public GTCM_ParallelHelper() {
    }

    // region Setters

    /**
     * Sets MetaTE controller, with current configuration for void protection mode.
     *
     * @deprecated Use {@link #setMachine(IVoidable)}
     */
    @Deprecated
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta) {
        return setMachine(machineMeta, machineMeta.protectsExcessItem(), machineMeta.protectsExcessFluid());
    }

    /**
     * Sets MetaTE controller, with void protection mode forcibly.
     *
     * @deprecated Use {@link #setMachine(IVoidable, boolean, boolean)}
     */
    @Deprecated
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta, boolean protectExcessItem,
                                           boolean protectExcessFluid) {
        return setMachine(machineMeta, protectExcessItem, protectExcessFluid);
    }

    /**
     * Sets machine, with current configuration for void protection mode.
     */
    public GT_ParallelHelper setMachine(IVoidable machine) {
        return setMachine(machine, machine.protectsExcessItem(), machine.protectsExcessFluid());
    }

    /**
     * Sets machine, with void protection mode forcibly.
     */
    public GT_ParallelHelper setMachine(IVoidable machine, boolean protectExcessItem, boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machine = machine;
        return this;
    }

    /**
     * Sets the recipe, which will be used for the parallel calculation
     */
    public GT_ParallelHelper setRecipe(@Nonnull GT_Recipe aRecipe) {
        recipe = Objects.requireNonNull(aRecipe);
        return this;
    }

    public GT_ParallelHelper setRecipeLocked(IRecipeLockable singleRecipeMachine, boolean isRecipeLocked) {
        this.singleRecipeMachine = singleRecipeMachine;
        this.isRecipeLocked = isRecipeLocked;
        return this;
    }

    /**
     * Sets the items available for the recipe check
     */
    public GT_ParallelHelper setItemInputs(ItemStack... aItemInputs) {
        this.itemInputs = aItemInputs;
        return this;
    }

    /**
     * Sets the fluid inputs available for the recipe check
     */
    public GT_ParallelHelper setFluidInputs(FluidStack... aFluidInputs) {
        this.fluidInputs = aFluidInputs;
        return this;
    }

    /**
     * Sets the available eut when trying for more parallels
     */
    public GT_ParallelHelper setAvailableEUt(long aAvailableEUt) {
        this.availableEUt = aAvailableEUt;
        return this;
    }

    /**
     * Sets the modifier for recipe eut. 1 does nothing 0.9 is 10% less. 1.1 is 10% more
     */
    public GT_ParallelHelper setEUtModifier(float aEUtModifier) {
        this.eutModifier = aEUtModifier;
        return this;
    }

    public GT_ParallelHelper setCalculator(GT_OverclockCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    /**
     * Use {@link #setConsumption(boolean)}
     */
    @Deprecated
    public GT_ParallelHelper enableConsumption() {
        return setConsumption(true);
    }

    /**
     * Set if we should consume inputs or not when trying for parallels
     *
     * @param consume Should we consume inputs
     */
    public GT_ParallelHelper setConsumption(boolean consume) {
        this.consume = consume;
        return this;
    }

    /**
     * Sets the MaxParallel a multi can handle
     */
    public GT_ParallelHelper setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return this;
    }

    /**
     * Enables Batch mode. Can do up to an additional processed recipes of mCurrentParallel * mBatchModifier A batch
     * modifier of 1 does nothing
     */
    public GT_ParallelHelper enableBatchMode(int batchModifier) {
        this.batchMode = batchModifier > 1;
        this.batchModifier = batchModifier;
        return this;
    }

    /**
     * Use {@link #setOutputCalculation(boolean)}
     */
    @Deprecated
    public GT_ParallelHelper enableOutputCalculation() {
        return setOutputCalculation(true);
    }

    /**
     * Sets if we should calculate outputs with the parallels we found or not
     *
     * @param calculateOutputs Should we calculate outputs with the helper or not
     */
    public GT_ParallelHelper setOutputCalculation(boolean calculateOutputs) {
        this.calculateOutputs = calculateOutputs;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return an ItemStack
     * array
     */
    public GT_ParallelHelper setCustomItemOutputCalculation(Function<Integer, ItemStack[]> custom) {
        customItemOutputCalculation = custom;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return a FluidStack
     * array
     */
    public GT_ParallelHelper setCustomFluidOutputCalculation(Function<Integer, FluidStack[]> custom) {
        customFluidOutputCalculation = custom;
        return this;
    }

    // endregion

    /**
     * Finishes the GT_ParallelHelper. Anything changed after this will not effect anything
     */
    public GT_ParallelHelper build() {
        if (built) {
            throw new IllegalStateException("Tried to build twice");
        }
        if (recipe == null) {
            throw new IllegalStateException("Recipe is not set");
        }
        built = true;
        determineParallel();
        return this;
    }

    /**
     * @return The current parallels possible by the multiblock
     */
    public int getCurrentParallel() {
        if (!built) {
            throw new IllegalStateException("Tried to get parallels before building");
        }
        return currentParallel;
    }

    /**
     * @return The duration multiplier if batch mode was enabled for the multiblock
     */
    public double getDurationMultiplierDouble() {
        if (!built) {
            throw new IllegalStateException("Tried to get duration multiplier before building");
        }
        if (batchMode && durationMultiplier > 0) {
            return durationMultiplier;
        }
        return 1;
    }

    /**
     * @deprecated Use {@link #getDurationMultiplierDouble()}
     */
    @Deprecated
    public float getDurationMultiplier() {
        return (float) getDurationMultiplierDouble();
    }

    /**
     * @return The ItemOutputs from the recipe
     */
    @Nonnull
    public ItemStack[] getItemOutputs() {
        if (!built || !calculateOutputs) {
            throw new IllegalStateException(
                "Tried to get item outputs before building or without enabling calculation of outputs");
        }
        return itemOutputs;
    }

    /**
     * @return The FluidOutputs from the recipe
     */
    @Nonnull
    public FluidStack[] getFluidOutputs() {
        if (!built || !calculateOutputs) {
            throw new IllegalStateException(
                "Tried to get fluid outputs before building or without enabling calculation of outputs");
        }
        return fluidOutputs;
    }

    /**
     * @return The result of why a recipe could've failed or succeeded
     */
    @Nonnull
    public CheckRecipeResult getResult() {
        if (!built) {
            throw new IllegalStateException("Tried to get recipe result before building");
        }
        return result;
    }

    /**
     * Use {@link #tryConsumeRecipeInputs(GT_Recipe, FluidStack[], ItemStack[], int)}
     */
    @Deprecated
    protected boolean tryConsumeRecipeInputs(GT_Recipe recipe, FluidStack[] fluids, ItemStack[] items) {
        return tryConsumeRecipeInputs(recipe, fluids, items, 1);
    }

    /**
     * Try to consume the inputs of the recipe
     *
     * @param recipe      Processed recipe
     * @param fluids      fluid inputs that will be consumed
     * @param items       item inputs that will be consumed
     * @param minParallel minimum amount of parallels to do with this check
     * @return True if recipe was satisfied, else false
     */
    protected boolean tryConsumeRecipeInputs(GT_Recipe recipe, FluidStack[] fluids, ItemStack[] items,
                                             int minParallel) {
        return recipe.isRecipeInputEqual(true, false, minParallel, fluids, items);
    }


    /**
     * Called by build(). Determines the parallels and everything else that needs to be done at build time
     */
    protected void determineParallel() {
        if (itemInputs == null) {
            itemInputs = new ItemStack[0];
        }
        if (fluidInputs == null) {
            fluidInputs = new FluidStack[0];
        }

        if (!consume) {
            copyInputs();
        }

        if (calculator == null) {
            calculator = new GT_OverclockCalculator().setEUt(availableEUt)
                                                     .setRecipeEUt(recipe.mEUt)
                                                     .setDuration(recipe.mDuration)
                                                     .setEUtDiscount(eutModifier);
        }

        final int tRecipeEUt = (int) Math.ceil(recipe.mEUt * eutModifier);
        if (availableEUt < tRecipeEUt) {
            result = CheckRecipeResultRegistry.insufficientPower(tRecipeEUt);
            return;
        }

        // Save the original max parallel before calculating our overclocking under 1 tick
        int originalMaxParallel = maxParallel;
        double tickTimeAfterOC = calculator.setParallel(originalMaxParallel)
                                           .calculateDurationUnderOneTick();
        if (tickTimeAfterOC < 1) {
            maxParallel = (int) (maxParallel / tickTimeAfterOC);
        }

        int maxParallelBeforeBatchMode = maxParallel;
        if (batchMode) {
            maxParallel *= batchModifier;
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
                                .setMaxParallel(maxParallel)
                                .build();
            maxParallel = Math.min(voidProtectionHelper.getMaxParallel(), maxParallel);
            if (maxParallel <= 0) {
                result = CheckRecipeResultRegistry.OUTPUT_FULL;
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

        long eutUseAfterOC = calculator.calculateEUtConsumptionUnderOneTick(originalMaxParallel, currentParallel);
        calculator.setParallel(Math.min(currentParallel, originalMaxParallel))
                  .calculate();
        if (currentParallel > originalMaxParallel) {
            calculator.setRecipeEUt(eutUseAfterOC);
        }
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
            if (recipe.mOutputs != null) {
                calculateItemOutputs();
            }
            if (recipe.mFluidOutputs != null) {
                calculateFluidOutputs();
            }
        }
        result = CheckRecipeResultRegistry.SUCCESSFUL;
    }

    protected void copyInputs() {
        ItemStack[] itemInputsToUse;
        FluidStack[] fluidInputsToUse;
        itemInputsToUse = new ItemStack[itemInputs.length];
        for (int i = 0; i < itemInputs.length; i++) {
            itemInputsToUse[i] = itemInputs[i].copy();
        }
        fluidInputsToUse = new FluidStack[fluidInputs.length];
        for (int i = 0; i < fluidInputs.length; i++) {
            fluidInputsToUse[i] = fluidInputs[i].copy();
        }
        itemInputs = itemInputsToUse;
        fluidInputs = fluidInputsToUse;
    }

    protected void calculateItemOutputs() {
        if (customItemOutputCalculation != null) {
            itemOutputs = customItemOutputCalculation.apply(currentParallel);
            return;
        }
        ArrayList<ItemStack> tempItemStack = new ArrayList<>();
        //        itemOutputs = new ItemStack[recipe.mOutputs.length];
        for (int i = 0; i < recipe.mOutputs.length; i++) {
            long items = 0;
            long remain = 0;
            int itemStackSize = recipe.getOutput(i).stackSize;
            items = (long) currentParallel * itemStackSize * recipe.getOutputChance(i) / 10000;
            remain = (long) currentParallel * itemStackSize * recipe.getOutputChance(i) % 10000;
            if (remain > XSTR.XSTR_INSTANCE.nextInt(10000)) {
                items += itemStackSize;
            }
            ItemStack origin = recipe.getOutput(i).copy();
            while (items >= Integer.MAX_VALUE) {
                ItemStack itemstack = origin.copy();
                itemstack.stackSize = Integer.MAX_VALUE;
                tempItemStack.add(itemstack);
                items -= Integer.MAX_VALUE;
            }
            ItemStack item = origin.copy();
            item.stackSize = (int) items;
            tempItemStack.add(item);
        }

        itemOutputs = tempItemStack.toArray(new ItemStack[0]);
    }

    protected void calculateFluidOutputs() {
        if (customFluidOutputCalculation != null) {
            fluidOutputs = customFluidOutputCalculation.apply(currentParallel);
            return;
        }
        ArrayList<FluidStack> tempFluidStack = new ArrayList<>();
        fluidOutputs = new FluidStack[recipe.mFluidOutputs.length];
        for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
            if (recipe.getFluidOutput(i) != null) {
                FluidStack tFluid = recipe.getFluidOutput(i)
                    .copy();
                long amount = (long) tFluid.amount * currentParallel;
                while (amount >= Integer.MAX_VALUE) {
                    FluidStack fluid = tFluid.copy();
                    fluid.amount = Integer.MAX_VALUE;
                    tempFluidStack.add(fluid);
                    amount -= Integer.MAX_VALUE;
                }
                FluidStack fluid = tFluid.copy();
                fluid.amount = (int) amount;
                tempFluidStack.add(fluid);
            }
        }
        fluidOutputs = tempFluidStack.toArray(new FluidStack[0]);
    }
}
// spotless:on
