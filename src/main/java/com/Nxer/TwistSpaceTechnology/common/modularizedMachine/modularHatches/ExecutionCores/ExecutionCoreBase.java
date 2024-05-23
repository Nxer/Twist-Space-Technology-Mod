package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ModularHatchBase;
import com.Nxer.TwistSpaceTechnology.util.NBTUtils;

import gregtech.api.interfaces.ITexture;

public abstract class ExecutionCoreBase extends ModularHatchBase {

    public ExecutionCoreBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, null);
    }

    public ExecutionCoreBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ModularHatchTypes getType() {
        return ModularHatchTypes.EXECUTION_CORE;
    }

    // region Logic

    protected ItemStack[] outputItems;
    protected FluidStack[] outputFluids;
    protected long maxProgressingTime;
    protected long progressedTime;
    protected long eut;
    protected IModularizedMachine.ISupportExecutionCore mainMachine;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("maxProgressingTime", maxProgressingTime);
        aNBT.setLong("progressedTime", progressedTime);
        aNBT.setLong("eut", eut);
        saveNBTDataItemStacks(aNBT);
        saveNBTDataFluidStacks(aNBT);
    }

    protected void saveNBTDataItemStacks(NBTTagCompound aNBT) {
        if (outputItems != null && outputItems.length > 0) {
            aNBT.setInteger("outputItemsLength", outputItems.length);
            for (int i = 0; i < outputItems.length; i++) {
                NBTUtils.saveItem(aNBT, "outputItems" + i, outputItems[i]);
            }
        }
    }

    protected void saveNBTDataFluidStacks(NBTTagCompound aNBT) {
        if (outputFluids != null && outputFluids.length > 0) {
            aNBT.setInteger("outputFluidsLength", outputFluids.length);
            for (int i = 0; i < outputFluids.length; i++) {
                NBTUtils.saveFluid(aNBT, "outputFluids" + i, outputFluids[i]);
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        maxProgressingTime = aNBT.getLong("maxProgressingTime");
        progressedTime = aNBT.getLong("progressedTime");
        eut = aNBT.getLong("eut");
        loadNBTDataItemStacks(aNBT);
        loadNBTDataFluidStacks(aNBT);
    }

    protected void loadNBTDataItemStacks(NBTTagCompound aNBT) {
        int length = aNBT.getInteger("outputItemsLength");
        if (length > 0) {
            outputItems = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                outputItems[i] = NBTUtils.loadItem(aNBT, "outputItems" + i);
            }
        }
    }

    protected void loadNBTDataFluidStacks(NBTTagCompound aNBT) {
        int length = aNBT.getInteger("outputFluidsLength");
        if (length > 0) {
            outputFluids = new FluidStack[length];
            for (int i = 0; i < length; i++) {
                outputFluids[i] = NBTUtils.loadFluid(aNBT, "outputFluids" + i);
            }
        }
    }

    // endregion

    // region Getter and Setter

    public ItemStack[] getOutputItems() {
        return outputItems;
    }

    public ExecutionCoreBase setOutputItems(ItemStack[] outputItems) {
        this.outputItems = outputItems;
        return this;
    }

    public FluidStack[] getOutputFluids() {
        return outputFluids;
    }

    public ExecutionCoreBase setOutputFluids(FluidStack[] outputFluids) {
        this.outputFluids = outputFluids;
        return this;
    }

    public long getMaxProgressingTime() {
        return maxProgressingTime;
    }

    public ExecutionCoreBase setMaxProgressingTime(long maxProgressingTime) {
        this.maxProgressingTime = maxProgressingTime;
        return this;
    }

    public long getProgressedTime() {
        return progressedTime;
    }

    public ExecutionCoreBase setProgressedTime(long progressedTime) {
        this.progressedTime = progressedTime;
        return this;
    }

    public long getEut() {
        return eut;
    }

    public ExecutionCoreBase setEut(long eut) {
        this.eut = eut;
        return this;
    }

    public IModularizedMachine.ISupportExecutionCore getMainMachine() {
        return mainMachine;
    }

    public ExecutionCoreBase setMainMachine(IModularizedMachine.ISupportExecutionCore mainMachine) {
        this.mainMachine = mainMachine;
        return this;
    }

    // endregion

}
