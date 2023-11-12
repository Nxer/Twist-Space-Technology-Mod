package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.recipe.check.RecipeValidator;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

// spotless:off
public class GTCM_ProcessingLogic extends ProcessingLogic {
	
	protected IVoidable machine;
	protected IRecipeLockable recipeLockableMachine;
	protected Supplier<GT_Recipe.GT_Recipe_Map> recipeMapSupplier;
	protected GT_Recipe lastRecipe;
	protected GT_Recipe.GT_Recipe_Map lastRecipeMap;
	protected ItemStack specialSlotItem;
	protected ItemStack[] inputItems;
	protected ItemStack[] outputItems;
	protected ItemStack[] currentOutputItems;
	protected FluidStack[] inputFluids;
	protected FluidStack[] outputFluids;
	protected FluidStack[] currentOutputFluids;
	protected long calculatedEut;
	protected int duration;
	protected long availableVoltage;
	protected long availableAmperage;
	protected int overClockTimeReduction = 1;
	protected int overClockPowerIncrease = 2;
	protected boolean protectItems;
	protected boolean protectFluids;
	protected boolean isRecipeLocked;
	protected int maxParallel = 1;
	protected int calculatedParallels = 0;
	protected Supplier<Integer> maxParallelSupplier;
	protected int batchSize = 1;
	protected float euModifier = 1.0f;
	protected float speedBoost = 1.0f;
	protected boolean amperageOC = true;
	
	public GTCM_ProcessingLogic() {}
	
	// region Setters
	
	public ProcessingLogic setInputItems(ItemStack... itemInputs) {
		this.inputItems = itemInputs;
		return this;
	}
	
	public ProcessingLogic setInputItems(List<ItemStack> itemOutputs) {
		this.inputItems = itemOutputs.toArray(new ItemStack[0]);
		return this;
	}
	
	public ProcessingLogic setInputFluids(FluidStack... fluidInputs) {
		this.inputFluids = fluidInputs;
		return this;
	}
	
	public ProcessingLogic setInputFluids(List<FluidStack> fluidInputs) {
		this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
		return this;
	}
	
	public ProcessingLogic setSpecialSlotItem(ItemStack specialSlotItem) {
		this.specialSlotItem = specialSlotItem;
		return this;
	}
	
	/**
	 * Overwrites item output result of the calculation.
	 */
	public ProcessingLogic setOutputItems(ItemStack... itemOutputs) {
		this.outputItems = itemOutputs;
		return this;
	}
	
	/**
	 * Overwrites fluid output result of the calculation.
	 */
	public ProcessingLogic setOutputFluids(FluidStack... fluidOutputs) {
		this.outputFluids = fluidOutputs;
		return this;
	}
	
	public ProcessingLogic setCurrentOutputItems(ItemStack... currentOutputItems) {
		this.currentOutputItems = currentOutputItems;
		return this;
	}
	
	public ProcessingLogic setCurrentOutputFluids(FluidStack... currentOutputFluids) {
		this.currentOutputFluids = currentOutputFluids;
		return this;
	}
	
	/**
	 * Enables single recipe locking mode.
	 */
	public ProcessingLogic setRecipeLocking(IRecipeLockable recipeLockableMachine, boolean isRecipeLocked) {
		this.recipeLockableMachine = recipeLockableMachine;
		this.isRecipeLocked = isRecipeLocked;
		return this;
	}
	
	/**
	 * Sets max amount of parallel.
	 */
	public ProcessingLogic setMaxParallel(int maxParallel) {
		this.maxParallel = maxParallel;
		return this;
	}
	
	/**
	 * Sets method to get max amount of parallel.
	 */
	public ProcessingLogic setMaxParallelSupplier(Supplier<Integer> supplier) {
		this.maxParallelSupplier = supplier;
		return this;
	}
	
	/**
	 * Sets batch size for batch mode.
	 */
	public ProcessingLogic setBatchSize(int size) {
		this.batchSize = size;
		return this;
	}
	
	public ProcessingLogic setRecipeMap(GT_Recipe.GT_Recipe_Map recipeMap) {
		return setRecipeMapSupplier(() -> recipeMap);
	}
	
	public ProcessingLogic setRecipeMapSupplier(Supplier<GT_Recipe.GT_Recipe_Map> supplier) {
		this.recipeMapSupplier = supplier;
		return this;
	}
	
	public ProcessingLogic setEuModifier(float modifier) {
		this.euModifier = modifier;
		return this;
	}
	
	public ProcessingLogic setSpeedBonus(float speedModifier) {
		this.speedBoost = speedModifier;
		return this;
	}
	
	/**
	 * Sets machine used for void protection logic.
	 */
	public ProcessingLogic setMachine(IVoidable machine) {
		this.machine = machine;
		return this;
	}
	
	/**
	 * Overwrites duration result of the calculation.
	 */
	public ProcessingLogic setDuration(int duration) {
		this.duration = duration;
		return this;
	}
	
	/**
	 * Overwrites EU/t result of the calculation.
	 */
	public ProcessingLogic setCalculatedEut(long calculatedEut) {
		this.calculatedEut = calculatedEut;
		return this;
	}
	
	/**
	 * Sets voltage of the machine. It doesn't need to be actual voltage (excluding amperage) of the machine;
	 * For example, most of the multiblock machines set maximum possible input power (including amperage) as voltage
	 * and 1 as amperage. That way recipemap search will be executed with overclocked voltage.
	 */
	public ProcessingLogic setAvailableVoltage(long voltage) {
		availableVoltage = voltage;
		return this;
	}
	
	/**
	 * Sets amperage of the machine. This amperage doesn't involve in EU/t when searching recipemap.
	 * Useful for preventing tier skip but still considering amperage for parallel.
	 */
	public ProcessingLogic setAvailableAmperage(long amperage) {
		availableAmperage = amperage;
		return this;
	}
	
	public ProcessingLogic setVoidProtection(boolean protectItems, boolean protectFluids) {
		this.protectItems = protectItems;
		this.protectFluids = protectFluids;
		return this;
	}
	
	/**
	 * Sets custom overclock ratio. 2/4 by default.
	 * Parameters represent number of bit shift, so 1 -> 2x, 2 -> 4x.
	 */
	public ProcessingLogic setOverclock(int timeReduction, int powerIncrease) {
		this.overClockTimeReduction = timeReduction;
		this.overClockPowerIncrease = powerIncrease;
		return this;
	}
	
	/**
	 * Sets overclock ratio to 4/4.
	 */
	public ProcessingLogic enablePerfectOverclock() {
		return this.setOverclock(2, 2);
	}
	
	/**
	 * Sets wether the multi should use amperage to OC or not
	 */
	public ProcessingLogic setAmperageOC(boolean amperageOC) {
		this.amperageOC = amperageOC;
		return this;
	}
	
	/**
	 * Clears calculated results and provided machine inputs to prepare for the next machine operation.
	 */
	public ProcessingLogic clear() {
		this.inputItems = null;
		this.inputFluids = null;
		this.specialSlotItem = null;
		this.outputItems = null;
		this.outputFluids = null;
		this.calculatedEut = 0;
		this.duration = 0;
		this.calculatedParallels = 0;
		return this;
	}
	
	// endregion
	
	// region Logic
	
	/**
	 * Executes the recipe check: Find recipe from recipemap, Calculate parallel, overclock and outputs.
	 */
	@Nonnull
	public CheckRecipeResult process() {
		GT_Recipe.GT_Recipe_Map recipeMap;
		if (recipeMapSupplier == null) {
			recipeMap = null;
		} else {
			recipeMap = recipeMapSupplier.get();
		}
		if (lastRecipeMap != recipeMap) {
			lastRecipe = null;
			lastRecipeMap = recipeMap;
		}
		
		if (maxParallelSupplier != null) {
			maxParallel = maxParallelSupplier.get();
		}
		
		if (isRecipeLocked && recipeLockableMachine != null && recipeLockableMachine.getSingleRecipeCheck() != null) {
			// Recipe checker is already built, we'll use it
			SingleRecipeCheck singleRecipeCheck = recipeLockableMachine.getSingleRecipeCheck();
			// Validate recipe here, otherwise machine will show "not enough output space"
			// even if recipe cannot be found
			if (singleRecipeCheck.checkRecipeInputs(false, 1, inputItems, inputFluids) == 0) {
				return CheckRecipeResultRegistry.NO_RECIPE;
			}
			
			return processRecipe(
				recipeLockableMachine.getSingleRecipeCheck()
				                     .getRecipe());
		}
		
		FindRecipeResult findRecipeResult = findRecipe(recipeMap);
		// If processRecipe is not overridden, advanced recipe validation logic is used, and we can reuse calculations.
		if (findRecipeResult.hasRecipeValidator()) {
			RecipeValidator recipeValidator = findRecipeResult.getRecipeValidator();
			
			// There are two cases:
			// 1 - there are actually no matching recipes
			// 2 - there are some matching recipes, but we rejected it due to our advanced validation (e.g. OUTPUT_FULL)
			if (findRecipeResult.getState() == FindRecipeResult.State.NOT_FOUND
				    && recipeValidator.getFirstCheckResult() != null) {
				// Here we're handling case 2
				// If there are matching recipes but our validation rejected them,
				// we should return a first one to display a proper error in the machine GUI
				return recipeValidator.getFirstCheckResult();
			}
			
			// If everything is ok, reuse our calculations
			if (recipeValidator.isExecutedAtLeastOnce() && findRecipeResult.isSuccessful()) {
				return applyRecipe(
					findRecipeResult.getRecipeNonNull(),
					recipeValidator.getLastParallelHelper(),
					recipeValidator.getLastOverclockCalculator(),
					recipeValidator.getLastCheckResult());
			}
		}
		
		if (!findRecipeResult.isSuccessful()) {
			return CheckRecipeResultRegistry.NO_RECIPE;
		}
		
		return processRecipe(findRecipeResult.getRecipeNonNull());
	}
	
	/**
	 * Checks if supplied recipe is valid for process.
	 * If so, additionally performs input consumption, output calculation with parallel, and overclock calculation.
	 *
	 * @param recipe The recipe which will be checked and processed
	 */
	@Nonnull
	protected CheckRecipeResult processRecipe(@Nonnull GT_Recipe recipe) {
		CheckRecipeResult result = validateRecipe(recipe);
		if (!result.wasSuccessful()) {
			return result;
		}
		
		GT_ParallelHelper helper = createParallelHelper(recipe);
		GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
		helper.setCalculator(calculator);
		helper.build();
		
		return applyRecipe(recipe, helper, calculator, result);
	}
	
	/**
	 * Applies the recipe and calculated parameters
	 */
	protected CheckRecipeResult applyRecipe(@NotNull GT_Recipe recipe, GT_ParallelHelper helper,
		GT_OverclockCalculator calculator, CheckRecipeResult result) {
		if (!helper.getResult()
		           .wasSuccessful()) {
			return helper.getResult();
		}
		
		if (recipe.mCanBeBuffered) {
			lastRecipe = recipe;
		} else {
			lastRecipe = null;
		}
		calculatedParallels = helper.getCurrentParallel();
		
		if (calculator.getConsumption() == Long.MAX_VALUE) {
			return CheckRecipeResultRegistry.POWER_OVERFLOW;
		}
		if (calculator.getDuration() == Integer.MAX_VALUE) {
			return CheckRecipeResultRegistry.DURATION_OVERFLOW;
		}
		
		calculatedEut = calculator.getConsumption();
		
		double finalDuration = calculateDuration(recipe, helper, calculator);
		if (finalDuration >= Integer.MAX_VALUE) {
			return CheckRecipeResultRegistry.DURATION_OVERFLOW;
		}
		duration = (int) finalDuration;
		
		outputItems = helper.getItemOutputs();
		outputFluids = helper.getFluidOutputs();
		
		return result;
	}
	
	/**
	 * Override to tweak final duration that will be set as a result of this logic class.
	 */
	protected double calculateDuration(@Nonnull GT_Recipe recipe, @Nonnull GT_ParallelHelper helper,
		@Nonnull GT_OverclockCalculator calculator) {
		return calculator.getDuration() * helper.getDurationMultiplierDouble();
	}
	
	/**
	 * Override if you don't work with regular gt recipe maps
	 */
	@Nonnull
	protected FindRecipeResult findRecipe(@Nullable GT_Recipe.GT_Recipe_Map map) {
		if (map == null) return FindRecipeResult.NOT_FOUND;
		
		RecipeValidator recipeValidator = new RecipeValidator(
			this::validateRecipe,
			this::createParallelHelper,
			this::createOverclockCalculator);
		
		FindRecipeResult findRecipeResult = map.findRecipeWithResult(
			lastRecipe,
			recipeValidator,
			false,
			false,
			amperageOC ? availableVoltage * availableAmperage : availableVoltage,
			inputFluids,
			specialSlotItem,
			inputItems);
		
		findRecipeResult.setRecipeValidator(recipeValidator);
		
		return findRecipeResult;
	}
	
	/**
	 * Override to tweak parallel logic if needed.
	 */
	@Nonnull
	protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
		return new GTCM_ParallelHelper().setRecipe(recipe)
		                              .setItemInputs(inputItems)
		                              .setFluidInputs(inputFluids)
		                              .setAvailableEUt(availableVoltage * availableAmperage)
		                              .setMachine(machine, protectItems, protectFluids)
		                              .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
		                              .setMaxParallel(maxParallel)
		                              .setEUtModifier(euModifier)
		                              .enableBatchMode(batchSize)
		                              .setConsumption(true)
		                              .setOutputCalculation(true);
	}
	
	/**
	 * Override to do additional check for finding recipe if needed, mainly for special value of the recipe.
	 */
	@Nonnull
	protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
		return CheckRecipeResultRegistry.SUCCESSFUL;
	}
	
	/**
	 * Use {@link #createOverclockCalculator(GT_Recipe)}
	 */
	@Nonnull
	@Deprecated
	protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe,
		@Nullable GT_ParallelHelper helper) {
		return createOverclockCalculator(recipe);
	}
	
	/**
	 * Override to tweak overclock logic if needed.
	 */
	@Nonnull
	protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
		return new GTCM_OverclockCalculator().setRecipeEUt(recipe.mEUt)
		                                   .setAmperage(availableAmperage)
		                                   .setEUt(availableVoltage)
		                                   .setDuration(recipe.mDuration)
		                                   .setSpeedBoost(speedBoost)
		                                   .setEUtDiscount(euModifier)
		                                   .setAmperageOC(amperageOC)
		                                   .setDurationDecreasePerOC(overClockTimeReduction)
		                                   .setEUtIncreasePerOC(overClockPowerIncrease);
	}
	
	// endregion
	
	// region Getters
	
	public ItemStack[] getOutputItems() {
		return outputItems;
	}
	
	public FluidStack[] getOutputFluids() {
		return outputFluids;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public long getCalculatedEut() {
		return calculatedEut;
	}
	
	public int getCurrentParallels() {
		return calculatedParallels;
	}
	
	// endregion
}
// spotless:on
