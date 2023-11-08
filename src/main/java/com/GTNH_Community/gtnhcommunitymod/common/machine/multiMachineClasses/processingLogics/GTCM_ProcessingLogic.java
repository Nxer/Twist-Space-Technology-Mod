package com.GTNH_Community.gtnhcommunitymod.common.machine.multiMachineClasses.processingLogics;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

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
	public GTCM_ProcessingLogic(){}
	
	/**
	 * Executes the recipe check: Find recipe from recipemap, Calculate parallel, overclock and outputs.
	 */
	@Nonnull
	public CheckRecipeResult process() {
		/*
		 * Init variables
		 */
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
		
		/*
		 * check single recipe mode
		 */
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
		
		/*
		 * Check : can use cache recipe again ?
		 */
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
		
		/*
		 *
		 */
		if (!findRecipeResult.isSuccessful()) {
			return CheckRecipeResultRegistry.NO_RECIPE;
		}
		
		/*
		 * To generate a new process IO
		 */
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
	private CheckRecipeResult applyRecipe(@NotNull GT_Recipe recipe, GT_ParallelHelper helper,
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
}
// spotless:on
