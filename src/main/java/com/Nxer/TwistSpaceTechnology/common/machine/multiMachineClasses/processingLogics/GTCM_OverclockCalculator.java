package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;

// spotless:off
public class GTCM_OverclockCalculator extends GT_OverclockCalculator {
	
	private static final double LOG2 = Math.log(2);
	
	/**
	 * Voltage the recipe will run at
	 */
	private long recipeVoltage = 0;
	/*
	 * The amount of amps the recipe needs
	 */
	private long recipeAmperage = 1;
	/**
	 * Voltage of the machine
	 */
	private long machineVoltage = 0;
	/**
	 * Amperage of the machine
	 */
	private long machineAmperage = 1;
	/**
	 * Duration of the recipe
	 */
	private int duration = 0;
	/**
	 * The parallel the machine has when trying to overclock
	 */
	private int parallel = 1;
	
	/**
	 * The min heat required for the recipe
	 */
	private int recipeHeat = 0;
	/**
	 * The heat the machine has when starting the recipe
	 */
	private int machineHeat = 0;
	/**
	 * How much the bits should be moved to the right for each 1800 above recipe heat (Used for duration)
	 */
	private int durationDecreasePerHeatOC = 2;
	/**
	 * Whether to enable overclocking with heat like the EBF every 1800 heat difference
	 */
	private boolean heatOC;
	/**
	 * Whether to enable heat discounts every 900 heat difference
	 */
	private boolean heatDiscount;
	/**
	 * The value used for discount final eut per 900 heat
	 */
	private double heatDiscountExponent = 0.95;
	
	/**
	 * Discount for EUt at the beginning of calculating overclocks, like GT++ machines
	 */
	private double eutDiscount = 1;
	/**
	 * Speeding/Slowing up/down the duration of a recipe at the beginning of calculating overclocks, like
	 * GT++ machines
	 */
	private double speedBoost = 1;
	
	/**
	 * How much the bits should be moved to the left when it is overclocking (Going up, 2 meaning it is multiplied with
	 * 4x)
	 */
	private int eutIncreasePerOC = 2;
	/**
	 * How much the bits should be moved to the right when its overclocking (Going down, 1 meaning it is halved)
	 */
	private int durationDecreasePerOC = 1;
	/**
	 * Whether to give EUt Discount when the duration goes below one tick
	 */
	private boolean oneTickDiscount;
	/**
	 * Whether the multi should use amperage to overclock with an exponent. Incompatible with amperageOC
	 */
	private boolean laserOC;
	/**
	 * Laser OC's penalty for using high amp lasers for overclocking. Like what the Adv. Assline is doing
	 */
	private double laserOCPenalty = 0.3;
	/**
	 * Whether the multi should use amperage to overclock normally. Incompatible with laserOC
	 */
	private boolean amperageOC;
	/**
	 * If the OC calculator should only do a given amount of overclocks. Mainly used in fusion reactors
	 */
	private boolean limitOverclocks;
	/**
	 * Maximum amount of overclocks to perform, when limitOverclocks = true
	 */
	private int maxOverclocks;
	/**
	 * How many overclocks have been performed
	 */
	private int overclockCount;
	/**
	 * How many overclocks were performed with heat out of the overclocks we had
	 */
	private int heatOverclockCount;
	/**
	 * A supplier, which is used for machines which have a custom way of calculating duration, like Neutron Activator
	 */
	private Supplier<Double> durationUnderOneTickSupplier;
	/**
	 * Should we actually try to calculate overclocking
	 */
	private boolean noOverclock;
	/**
	 * variable to check whether the overclocks have been calculated
	 */
	private boolean calculated;
	
	private static final int HEAT_DISCOUNT_THRESHOLD = 900;
	private static final int HEAT_PERFECT_OVERCLOCK_THRESHOLD = 1800;
	
	/**
	 * Creates calculator that doesn't do OC at all. Will use recipe duration.
	 */
	public static GT_OverclockCalculator ofNoOverclock(@Nonnull GT_Recipe recipe) {
		return ofNoOverclock(recipe.mEUt, recipe.mDuration);
	}
	
	/**
	 * Creates calculator that doesn't do OC at all, with set duration.
	 */
	public static GT_OverclockCalculator ofNoOverclock(long eut, int duration) {
		return new GT_OverclockCalculator().setRecipeEUt(eut)
		                                   .setDuration(duration)
		                                   .setEUt(eut)
		                                   .setNoOverclock(true);
	}
	
	/**
	 * An Overclock helper for calculating overclocks in many different situations
	 */
	public GTCM_OverclockCalculator() {}
	
	/**
	 * @param recipeEUt Sets the Recipe's starting voltage
	 */
	public GT_OverclockCalculator setRecipeEUt(long recipeEUt) {
		this.recipeVoltage = recipeEUt;
		return this;
	}
	
	/**
	 * @param machineVoltage Sets the EUt that the machine can use. This is the voltage of the machine
	 */
	public GT_OverclockCalculator setEUt(long machineVoltage) {
		this.machineVoltage = machineVoltage;
		return this;
	}
	
	/**
	 * @param duration Sets the duration of the recipe
	 */
	public GT_OverclockCalculator setDuration(int duration) {
		this.duration = duration;
		return this;
	}
	
	/**
	 * @param machineAmperage Sets the Amperage that the machine can support
	 */
	public GT_OverclockCalculator setAmperage(long machineAmperage) {
		this.machineAmperage = machineAmperage;
		return this;
	}
	
	/**
	 * @param recipeAmperage Sets the Amperage of the recipe
	 */
	public GT_OverclockCalculator setRecipeAmperage(long recipeAmperage) {
		this.recipeAmperage = recipeAmperage;
		return this;
	}
	
	/**
	 * Enables Perfect OC in calculation
	 */
	public GT_OverclockCalculator enablePerfectOC() {
		this.durationDecreasePerOC = 2;
		return this;
	}
	
	/**
	 * Use {@link #setHeatOC(boolean)}
	 */
	@Deprecated
	public GT_OverclockCalculator enableHeatOC() {
		return setHeatOC(true);
	}
	
	/**
	 * Set if we should be calculating overclocking using EBF's perfectOC
	 */
	public GT_OverclockCalculator setHeatOC(boolean heatOC) {
		this.heatOC = heatOC;
		return this;
	}
	
	/**
	 * Use {@link #setHeatDiscount(boolean)}
	 */
	@Deprecated
	public GT_OverclockCalculator enableHeatDiscount() {
		return setHeatDiscount(true);
	}
	
	/**
	 * Sets if we should add a heat discount at the end of calculating an overclock, just like the EBF
	 */
	public GT_OverclockCalculator setHeatDiscount(boolean heatDiscount) {
		this.heatDiscount = heatDiscount;
		return this;
	}
	
	/**
	 * Sets the starting heat of the recipe
	 */
	public GT_OverclockCalculator setRecipeHeat(int recipeHeat) {
		this.recipeHeat = recipeHeat;
		return this;
	}
	
	/**
	 * Use {@link #setMachineHeat(int)}
	 */
	@Deprecated
	public GT_OverclockCalculator setMultiHeat(int machineHeat) {
		return setMachineHeat(machineHeat);
	}
	
	/**
	 * Sets the heat of the coils on the machine
	 */
	public GT_OverclockCalculator setMachineHeat(int machineHeat) {
		this.machineHeat = machineHeat;
		return this;
	}
	
	/**
	 * Sets an EUtDiscount. 0.9 is 10% less energy. 1.1 is 10% more energy
	 */
	public GT_OverclockCalculator setEUtDiscount(float aEUtDiscount) {
		this.eutDiscount = aEUtDiscount;
		return this;
	}
	
	/**
	 * Sets a Speed Boost for the multiblock. 0.9 is 10% faster. 1.1 is 10% slower
	 */
	public GT_OverclockCalculator setSpeedBoost(float aSpeedBoost) {
		this.speedBoost = aSpeedBoost;
		return this;
	}
	
	/**
	 * Sets the parallel that the multiblock uses
	 */
	public GT_OverclockCalculator setParallel(int aParallel) {
		this.parallel = aParallel;
		return this;
	}
	
	/**
	 * Sets the heat discount during OC calculation if HeatOC is used. Default: 0.95 = 5% discount Used like a EU/t
	 * Discount
	 */
	public GT_OverclockCalculator setHeatDiscountMultiplier(float heatDiscountExponent) {
		this.heatDiscountExponent = heatDiscountExponent;
		return this;
	}
	
	/**
	 * Sets the Overclock that should be calculated when one. This uses BitShifting! Default is 2, which is a 4x
	 * decrease
	 */
	public GT_OverclockCalculator setHeatPerfectOC(int heatPerfectOC) {
		this.durationDecreasePerHeatOC = heatPerfectOC;
		return this;
	}
	
	/**
	 * Sets the amount that the EUt increases per overclock. This uses BitShifting! Default is 2, which is a 4x increase
	 */
	public GT_OverclockCalculator setEUtIncreasePerOC(int aEUtIncreasePerOC) {
		this.eutIncreasePerOC = aEUtIncreasePerOC;
		return this;
	}
	
	/**
	 * Sets the amount that the duration decreases per overclock. This uses BitShifting! Default is 1, which halves the
	 * duration
	 */
	public GT_OverclockCalculator setDurationDecreasePerOC(int durationDecreasePerOC) {
		this.durationDecreasePerOC = durationDecreasePerOC;
		return this;
	}
	
	/**
	 * Use {@link #setOneTickDiscount(boolean)}
	 */
	@Deprecated
	public GT_OverclockCalculator enableOneTickDiscount() {
		return setOneTickDiscount(true);
	}
	
	/**
	 * Set One Tick Discount on EUt based on Duration Decrease Per Overclock. This functions the same as single
	 * blocks.
	 */
	public GT_OverclockCalculator setOneTickDiscount(boolean oneTickDiscount) {
		this.oneTickDiscount = oneTickDiscount;
		return this;
	}
	
	/**
	 * Limit the amount of overclocks that can be performed, regardless of how much power is available. Mainly used for
	 * fusion reactors.
	 */
	public GT_OverclockCalculator limitOverclockCount(int maxOverclocks) {
		this.limitOverclocks = true;
		this.maxOverclocks = maxOverclocks;
		return this;
	}
	
	public GT_OverclockCalculator setLaserOC(boolean laserOC) {
		this.laserOC = laserOC;
		return this;
	}
	
	public GT_OverclockCalculator setAmperageOC(boolean amperageOC) {
		this.amperageOC = amperageOC;
		return this;
	}
	
	public GT_OverclockCalculator setLaserOCPenalty(double laserOCPenalty) {
		this.laserOCPenalty = laserOCPenalty;
		return this;
	}
	
	/**
	 * Set a supplier for calculating custom duration for when its needed under one tick
	 */
	public GT_OverclockCalculator setDurationUnderOneTickSupplier(Supplier<Double> supplier) {
		this.durationUnderOneTickSupplier = supplier;
		return this;
	}
	
	/**
	 * Sets if we should do overclocking or not
	 */
	public GT_OverclockCalculator setNoOverclock(boolean noOverclock) {
		this.noOverclock = noOverclock;
		return this;
	}
	
	/**
	 * Call this when all values have been put it.
	 */
	public GT_OverclockCalculator calculate() {
		if (calculated) {
			throw new IllegalStateException("Tried to calculate overclocks twice");
		}
		calculateOverclock();
		calculated = true;
		return this;
	}
	
	private void calculateOverclock() {
		duration = (int) Math.ceil(duration * speedBoost);
		if (noOverclock) {
			recipeVoltage = calculateFinalRecipeEUt(calculateHeatDiscountMultiplier());
			return;
		}
		if (laserOC && amperageOC) {
			throw new IllegalStateException("Tried to calculate overclock with both laser and amperage overclocking");
		}
		double heatDiscountMultiplier = calculateHeatDiscountMultiplier();
		if (heatOC) {
			heatOverclockCount = calculateAmountOfHeatOverclocks();
		}
		
		double recipePowerTier = calculateRecipePowerTier(heatDiscountMultiplier);
		double machinePowerTier = calculateMachinePowerTier();
		
		overclockCount = calculateAmountOfNeededOverclocks(machinePowerTier, recipePowerTier);
		if (recipeVoltage <= GT_Values.V[0]) {
			overclockCount = Math.min(overclockCount, calculateRecipeToMachineVoltageDifference());
		}
		if (overclockCount < 0) {
			recipeVoltage = Long.MAX_VALUE;
			duration = Integer.MAX_VALUE;
			return;
		}
		
		overclockCount = limitOverclocks ? Math.min(maxOverclocks, overclockCount) : overclockCount;
		heatOverclockCount = Math.min(heatOverclockCount, overclockCount);
		recipeVoltage <<= eutIncreasePerOC * overclockCount;
		duration >>= durationDecreasePerOC * (overclockCount - heatOverclockCount);
		duration >>= durationDecreasePerHeatOC * heatOverclockCount;
		if (oneTickDiscount) {
			recipeVoltage >>= durationDecreasePerOC * ((int) (machinePowerTier - recipePowerTier - overclockCount));
			if (recipeVoltage < 1) {
				recipeVoltage = 1;
			}
		}
		
		if (laserOC) {
			calculateLaserOC();
		}
		
		if (duration < 1) {
			duration = 1;
		}
		
		recipeVoltage = calculateFinalRecipeEUt(heatDiscountMultiplier);
	}
	
	private double calculateRecipePowerTier(double heatDiscountMultiplier) {
		return calculatePowerTier(recipeVoltage * parallel * eutDiscount * heatDiscountMultiplier * recipeAmperage);
	}
	
	private double calculateMachinePowerTier() {
		return calculatePowerTier(
			machineVoltage * (amperageOC ? machineAmperage : Math.min(machineAmperage, parallel)));
	}
	
	private int calculateRecipeToMachineVoltageDifference() {
		return (int) (Math.ceil(calculatePowerTier(machineVoltage)) - Math.ceil(calculatePowerTier(recipeVoltage)));
	}
	
	private double calculatePowerTier(double voltage) {
		return 1 + Math.max(0, (Math.log(voltage) / LOG2) - 5) / 2;
	}
	
	private long calculateFinalRecipeEUt(double heatDiscountMultiplier) {
		return (long) Math.ceil(recipeVoltage * eutDiscount * heatDiscountMultiplier * parallel * recipeAmperage);
	}
	
	private int calculateAmountOfHeatOverclocks() {
		return Math.min(
			(machineHeat - recipeHeat) / HEAT_PERFECT_OVERCLOCK_THRESHOLD,
			calculateAmountOfOverclocks(
				calculateMachinePowerTier(),
				calculateRecipePowerTier(calculateHeatDiscountMultiplier())));
	}
	
	/**
	 * Calculate maximum possible overclocks ignoring if we are going to go under 1 tick
	 */
	private int calculateAmountOfOverclocks(double machinePowerTier, double recipePowerTier) {
		return (int) (machinePowerTier - recipePowerTier);
	}
	
	/**
	 * Calculates the amount of overclocks needed to reach 1 ticking
	 *
	 * Here we limit "the tier difference overclock" amount to a number
	 * of overclocks needed to reach 1 tick duration, for example:
	 *
	 * recipe initial duration = 250 ticks (12,5 seconds LV(1))
	 * we have LCR with IV(5) energy hatch, which overclocks at 4/4 rate
	 *
	 * log_4 (250) ~ 3,98 is the number of overclocks needed to reach 1 tick
	 *
	 * to calculate log_a(b) we can use the log property:
	 * log_a(b) = log_c(b) / log_c(a)
	 * in our case we use natural log base as 'c'
	 *
	 * as a final step we apply Math.ceil(),
	 * otherwise for fractional nums like 3,98 we will never reach 1 tick
	 */
	private int calculateAmountOfNeededOverclocks(double machinePowerTier, double recipePowerTier) {
		return (int) Math.min(
			calculateAmountOfOverclocks(machinePowerTier, recipePowerTier),
			Math.ceil(Math.log(duration) / Math.log(1 << durationDecreasePerOC)));
	}
	
	private double calculateHeatDiscountMultiplier() {
		int heatDiscounts = heatDiscount ? (machineHeat - recipeHeat) / HEAT_DISCOUNT_THRESHOLD : 0;
		return Math.pow(heatDiscountExponent, heatDiscounts);
	}
	
	private void calculateLaserOC() {
		long inputEut = machineVoltage * machineAmperage;
		double currentPenalty = (1 << eutIncreasePerOC) + laserOCPenalty;
		while (inputEut > recipeVoltage * currentPenalty && recipeVoltage * currentPenalty > 0 && duration > 1) {
			duration >>= durationDecreasePerOC;
			recipeVoltage *= currentPenalty;
			currentPenalty += laserOCPenalty;
		}
	}
	
	/**
	 * @return The consumption after overclock has been calculated
	 */
	public long getConsumption() {
		if (!calculated) {
			throw new IllegalStateException("Tried to get consumption before calculating");
		}
		return recipeVoltage;
	}
	
	/**
	 * @return The duration of the recipe after overclock has been calculated
	 */
	public int getDuration() {
		if (!calculated) {
			throw new IllegalStateException("Tried to get duration before calculating");
		}
		return duration;
	}
	
	/**
	 * @return Number of performed overclocks
	 */
	public int getPerformedOverclocks() {
		if (!calculated) {
			throw new IllegalStateException("Tried to get performed overclocks before calculating");
		}
		return overclockCount;
	}
	
	/**
	 * Returns duration as a double to show how much it is overclocking too much to determine extra parallel.
	 * This doesn't count as calculating
	 */
	public double calculateDurationUnderOneTick() {
		if (durationUnderOneTickSupplier != null) return durationUnderOneTickSupplier.get();
		if (noOverclock) return duration;
		int normalOverclocks = calculateAmountOfOverclocks(
			calculateMachinePowerTier(),
			calculateRecipePowerTier(calculateHeatDiscountMultiplier()));
		normalOverclocks = limitOverclocks ? Math.min(normalOverclocks, maxOverclocks) : normalOverclocks;
		int heatOverclocks = Math.min(calculateAmountOfHeatOverclocks(), normalOverclocks);
		return (duration * speedBoost) / (Math.pow(1 << durationDecreasePerOC, normalOverclocks - heatOverclocks)
			                                  * Math.pow(1 << durationDecreasePerHeatOC, heatOverclocks));
	}
	
	/**
	 * Returns the EUt consumption one would get from overclocking under 1 tick
	 * This Doesn't count as calculating
	 *
	 * @param originalMaxParallel Parallels which are of the actual machine before the overclocking extra ones
	 */
	public long calculateEUtConsumptionUnderOneTick(int originalMaxParallel, int currentParallel) {
		if (noOverclock) return recipeVoltage;
		double heatDiscountMultiplier = calculateHeatDiscountMultiplier();
		// So what we need to do here is as follows:
		// - First we need to figure out what out parallel multiplier for getting to that OC was
		// - Second we need to find how many of those were from heat overclocks
		// - Third we need to find how many were from normal overclocking.
		// = For that we need to find how much better heat overclocks are compared to normal ones
		// = Then remove that many from our normal overclocks
		// - Fourth we find how many total overclocks we have
		// - Fifth we find how many of those are needed to one tick
		// - Finally we calculate the formula
		// = The energy increase from our overclocks for parallel
		// = The energy increase from our overclock to reach maximum under one tick potential
		// =- NOTE: This will always cause machine to use full power no matter what. Otherwise it creates many
		// anomalies.
		// = Everything else for recipe voltage is also calculated here.
		
		double parallelMultiplierFromOverclocks = (double) currentParallel / originalMaxParallel;
		double amountOfParallelHeatOverclocks = Math.min(
			Math.log(parallelMultiplierFromOverclocks) / Math.log(1 << durationDecreasePerHeatOC),
			calculateAmountOfHeatOverclocks());
		double amountOfParallelOverclocks = Math.log(parallelMultiplierFromOverclocks)
			                                    / Math.log(1 << durationDecreasePerOC)
			                                    - amountOfParallelHeatOverclocks * (1 << durationDecreasePerHeatOC - durationDecreasePerOC);
		double machineTier = calculateMachinePowerTier();
		double recipeTier = calculateRecipePowerTier(heatDiscountMultiplier);
		double amountOfTotalOverclocks = calculateAmountOfOverclocks(machineTier, recipeTier);
		if (recipeVoltage <= GT_Values.V[0]) {
			amountOfTotalOverclocks = Math.min(amountOfTotalOverclocks, calculateRecipeToMachineVoltageDifference());
		}
		return (long) Math.ceil(
			recipeVoltage * Math.pow(1 << eutIncreasePerOC, amountOfParallelOverclocks + amountOfParallelHeatOverclocks)
				* Math.pow(
				1 << eutIncreasePerOC,
				amountOfTotalOverclocks - (amountOfParallelOverclocks + amountOfParallelHeatOverclocks))
				* originalMaxParallel
				* eutDiscount
				* recipeAmperage
				* heatDiscountMultiplier);
	}
}
// spotless:on
