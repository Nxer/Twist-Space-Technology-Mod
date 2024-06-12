package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.Collection;
import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.google.common.collect.ImmutableList;

public abstract class MultiExecutionCoreMachineSupportAllModuleBase<T extends MultiExecutionCoreMachineSupportAllModuleBase<T>>
    extends MultiExecutionCoreMachineBase<T> implements IModularizedMachine.ISupportAllModularHatches {

    public MultiExecutionCoreMachineSupportAllModuleBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MultiExecutionCoreMachineSupportAllModuleBase(String aName) {
        super(aName);
    }

    // region Modular Settings

    public OverclockType overclockType = OverclockType.NormalOverclock;
    public int staticParallel = 0;
    public int dynamicParallel = 0;
    public float staticPowerConsumptionMultiplier = 1;
    public float staticSpeedBonus = 1;
    public float dynamicSpeedBonus = 1;

    @Override
    public void resetModularStaticSettings() {
        overclockType = OverclockType.NormalOverclock;
        staticParallel = 0;
        staticPowerConsumptionMultiplier = 1;
        staticSpeedBonus = 1;
    }

    @Override
    public void resetModularDynamicParameters() {
        dynamicParallel = 0;
        dynamicSpeedBonus = 1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("OverclockTypeTimeReduction", overclockType.timeReduction);
        aNBT.setInteger("OverclockTypePowerIncrease", overclockType.timeReduction);
        aNBT.setInteger("staticParallel", staticParallel);
        aNBT.setInteger("dynamicParallel", dynamicParallel);
        aNBT.setFloat("staticPowerConsumptionMultiplier", staticPowerConsumptionMultiplier);
        aNBT.setFloat("staticSpeedBonus", staticSpeedBonus);
        aNBT.setFloat("dynamicSpeedBonus", dynamicSpeedBonus);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        overclockType = OverclockType.checkOverclockType(
            aNBT.getInteger("OverclockTypeTimeReduction"),
            aNBT.getInteger("OverclockTypePowerIncrease"));
        staticParallel = aNBT.getInteger("staticParallel");
        dynamicParallel = aNBT.getInteger("dynamicParallel");
        staticPowerConsumptionMultiplier = aNBT.getFloat("staticPowerConsumptionMultiplier");
        staticSpeedBonus = aNBT.getFloat("staticSpeedBonus");
        dynamicSpeedBonus = aNBT.getFloat("dynamicSpeedBonus");
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return overclockType.isPerfectOverclock();
    }

    @Override
    protected float getSpeedBonus() {
        return staticSpeedBonus * dynamicSpeedBonus;
    }

    @Override
    protected int getMaxParallelRecipes() {
        if (dynamicParallel == Integer.MAX_VALUE || staticParallel == Integer.MAX_VALUE
            || dynamicParallel >= Integer.MAX_VALUE - 1 - staticParallel) {
            return Integer.MAX_VALUE;
        }
        return dynamicParallel + staticParallel + 1;
    }

    @Override
    protected OverclockType getOverclockType() {
        return overclockType;
    }

    @Override
    public void setOverclockType(OverclockType type) {
        this.overclockType = type;
    }

    @Override
    public int getStaticParallelParameterValue() {
        return staticParallel;
    }

    @Override
    public void setStaticParallelParameter(int value) {
        this.staticParallel = value;
    }

    @Override
    public int getDynamicParallelParameterValue() {
        return dynamicParallel;
    }

    @Override
    public void setDynamicParallelParameter(int value) {
        this.dynamicParallel = value;
    }

    @Override
    public float getStaticPowerConsumptionParameterValue() {
        return staticPowerConsumptionMultiplier;
    }

    @Override
    public void setStaticPowerConsumptionParameterValue(float value) {
        this.staticPowerConsumptionMultiplier = value;
    }

    @Override
    public float getStaticSpeedParameterValue() {
        return staticSpeedBonus;
    }

    @Override
    public void setStaticSpeedParameterValue(float value) {
        this.staticSpeedBonus = value;
    }

    @Override
    public float getDynamicSpeedParameterValue() {
        return dynamicSpeedBonus;
    }

    @Override
    public void setDynamicSpeedParameterValue(float value) {
        this.dynamicSpeedBonus = value;
    }

    private static final Collection<ModularHatchTypes> supportedModularHatchTypes = ImmutableList
        .of(ModularHatchTypes.ALL);

    @Override
    public Collection<ModularHatchTypes> getSupportedModularHatchTypes() {
        return supportedModularHatchTypes;
    }

    @Override
    public Collection<ModularBlockTypes> getSupportedModularBlockTypes() {
        return Collections.emptyList();
    }

    // endregion

}
