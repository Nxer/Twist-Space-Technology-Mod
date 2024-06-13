package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IStaticModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ModularHatchBase;
import com.Nxer.TwistSpaceTechnology.util.NBTUtils;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.ProcessingLogic;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class ExecutionCoreBase extends ModularHatchBase implements IExecutionCore, IStaticModularHatch {

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
    protected int maxProgressingTime;
    protected int progressedTime;
    protected int boostedTime;
    protected long eut;
    protected boolean hasBeenSetup = false;
    protected IModularizedMachine.ISupportExecutionCore mainMachine;

    // region waila

    public void processWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("hasBeenSetup")) {
            int maxProgressingTime = tag.getInteger("maxProgressingTime");
            if (maxProgressingTime > 0) {
                // spotless:off
                currentTip.add(
                    // #tr Waila.ExecutionCore.1
                    // # Total basic max progressing time
                    // #zh_CN 配方总基础耗时
                    TextEnums.tr("Waila.ExecutionCore.1") + " : "
                        + maxProgressingTime + " tick ("
                        + (maxProgressingTime / 20) + "s)");
                int progressedTime = tag.getInteger("progressedTime");
                currentTip.add(
                    // #tr Waila.ExecutionCore.2
                    // # Progressed time
                    // #zh_CN 已执行时间
                    TextEnums.tr("Waila.ExecutionCore.2") + " : "
                        + progressedTime + " tick ("
                        + (progressedTime / 20) + "s)"
                );
                int boostedTime = tag.getInteger("boostedTime");
                currentTip.add(
                    // #tr Waila.ExecutionCore.4
                    // # Boosted time
                    // #zh_CN 已加速时间
                    TextEnums.tr("Waila.ExecutionCore.4") + " : "
                        + boostedTime + " tick ("
                        + (boostedTime / 20) + "s)"
                );
                currentTip.add(
                    // #tr Waila.ExecutionCore.3
                    // # Basic power consumption
                    // #zh_CN 基础功率
                    TextEnums.tr("Waila.ExecutionCore.3") + " : "
                        + tag.getLong("usingEut") + " EU/t"
                );
                // spotless:on
            } else {
                // #tr Waila.ExecutionCore.IsIdle
                // # This {\WHITE}Execution Core{\GRAY} is idle.
                // #zh_CN 此{\WHITE}执行核心{\GRAY}处于空闲状态
                currentTip.add(TextEnums.tr("Waila.ExecutionCore.IsIdle"));
            }
        } else {
            // #tr Waila.ExecutionCore.HasNotBeenSetup
            // # This execution core has not been setup.
            // #zh_CN 此执行核心未初始化
            currentTip.add(TextEnums.tr("Waila.ExecutionCore.HasNotBeenSetup"));
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        processWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("hasBeenSetup", hasBeenSetup && mainMachine != null);
            tag.setInteger("maxProgressingTime", maxProgressingTime);
            if (maxProgressingTime > 0) {
                int outputItemStackAmount = outputItems == null ? 0 : outputItems.length;
                tag.setInteger("outputItemStackAmount", outputItemStackAmount);
                int outputFluidStackAmount = outputFluids == null ? 0 : outputFluids.length;
                tag.setInteger("outputFluidStackAmount", outputFluidStackAmount);
                tag.setInteger("progressedTime", progressedTime);
                tag.setInteger("boostedTime", boostedTime);
                tag.setLong("usingEut", eut);
            }
        }
    }
    // endregion

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        runExecutionCoreTick(aBaseMetaTileEntity, aTick);
    }

    public boolean setProcessing(ProcessingLogic processingLogic) {
        setOutputItems(processingLogic.getOutputItems());
        setOutputFluids(processingLogic.getOutputFluids());
        setMaxProgressingTime(processingLogic.getDuration());
        setEut(processingLogic.getCalculatedEut());
        return done();
    }

    public void runExecutionCoreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (hasBeenSetup && mainMachine != null) {
                if (maxProgressingTime > 0) {
                    if (progressedTime < maxProgressingTime - 1) {
                        progressedTime++;
                    } else {
                        // output and finish this work

                        if (outputItems != null && outputItems.length > 0) {
                            mainMachine.mergeOutputItems(outputItems);
                            outputItems = null;
                        }

                        if (outputFluids != null && outputFluids.length > 0) {
                            mainMachine.mergeOutputFluids(outputFluids);
                            outputFluids = null;
                        }

                        if (useMainMachinePower()) {
                            if (!mainMachine.tryDecreaseUsedEut(eut)) {
                                TwistSpaceTechnology.LOG.info(
                                    "ERROR: Execution core try decrease used EU/t failed at x"
                                        + aBaseMetaTileEntity.getXCoord()
                                        + " y"
                                        + aBaseMetaTileEntity.getYCoord()
                                        + " z"
                                        + aBaseMetaTileEntity.getZCoord());
                            }
                        }

                        resetParameters();

                        mainMachine.forceCheckProcessing();

                    }
                }
            }
        }
    }

    @Override
    public IExecutionCore boostTick(int tick) {
        progressedTime += tick;
        boostedTime += tick;
        return this;
    }

    @Override
    public int getNeedProgressingTime() {
        return maxProgressingTime - progressedTime;
    }

    @Override
    public boolean isIdle() {
        return hasBeenSetup && this.maxProgressingTime < 1;
    }

    public boolean isWorking() {
        return hasBeenSetup && this.maxProgressingTime > 0;
    }

    @Override
    public boolean setup(IModularizedMachine.ISupportExecutionCore mainMachine) {
        if (!hasBeenSetup) {
            this.mainMachine = mainMachine;
            this.hasBeenSetup = true;
            return true;
        }
        return false;
    }

    @Override
    public void shutDown() {
        outputItems = null;
        outputFluids = null;
        maxProgressingTime = 0;
        progressedTime = 0;
        eut = 0;
        setActive(false);
    }

    public void resetParameters() {
        maxProgressingTime = 0;
        progressedTime = 0;
        boostedTime = 0;
        eut = 0;
        setActive(false);
    }

    @Override
    public void reset() {
        outputItems = null;
        outputFluids = null;
        maxProgressingTime = 0;
        progressedTime = 0;
        eut = 0;
        hasBeenSetup = false;
        mainMachine = null;
        setActive(false);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("maxProgressingTime", maxProgressingTime);
        aNBT.setInteger("progressedTime", progressedTime);
        aNBT.setInteger("boostedTime", boostedTime);
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
        maxProgressingTime = aNBT.getInteger("maxProgressingTime");
        progressedTime = aNBT.getInteger("progressedTime");
        boostedTime = aNBT.getInteger("boostedTime");
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

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return VoidingMode.VOID_ALL;
    }

    @Override
    public void setVoidingMode(VoidingMode mode) {}

    @Override
    public List<ItemStack> getItemOutputSlots(ItemStack[] toOutput) {
        if (mainMachine instanceof IVoidable m) {
            return m.getItemOutputSlots(toOutput);
        }
        return Collections.emptyList();
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        if (mainMachine instanceof IVoidable m) {
            return m.getFluidOutputSlots(toOutput);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean canDumpItemToME() {
        if (mainMachine instanceof IVoidable m) {
            return m.canDumpItemToME();
        }
        return false;
    }

    @Override
    public boolean canDumpFluidToME() {
        if (mainMachine instanceof IVoidable m) {
            return m.canDumpFluidToME();
        }
        return false;
    }

    // endregion

    // region Getter and Setter

    public ItemStack[] getOutputItems() {
        return outputItems;
    }

    @Override
    public ExecutionCoreBase setOutputItems(ItemStack[] outputItems) {
        this.outputItems = outputItems;
        return this;
    }

    public FluidStack[] getOutputFluids() {
        return outputFluids;
    }

    @Override
    public ExecutionCoreBase setOutputFluids(FluidStack[] outputFluids) {
        this.outputFluids = outputFluids;
        return this;
    }

    public long getMaxProgressingTime() {
        return maxProgressingTime;
    }

    @Override
    public ExecutionCoreBase setMaxProgressingTime(int maxProgressingTime) {
        this.maxProgressingTime = maxProgressingTime;
        return this;
    }

    public long getProgressedTime() {
        return progressedTime;
    }

    public ExecutionCoreBase setProgressedTime(int progressedTime) {
        this.progressedTime = progressedTime;
        return this;
    }

    public long getEut() {
        return eut;
    }

    @Override
    public ExecutionCoreBase setEut(long eut) {
        this.eut = eut;
        return this;
    }

    @Override
    public IModularizedMachine.ISupportExecutionCore getMainMachine() {
        return mainMachine;
    }

    public ExecutionCoreBase setMainMachine(IModularizedMachine.ISupportExecutionCore mainMachine) {
        this.mainMachine = mainMachine;
        return this;
    }

    public boolean isHasBeenSetup() {
        return hasBeenSetup;
    }

    public ExecutionCoreBase setHasBeenSetup(boolean hasBeenSetup) {
        this.hasBeenSetup = hasBeenSetup;
        return this;
    }

    // endregion

    @Override
    public void onCheckMachine(ModularizedMachineBase<?> machine) {
        // do nothing
    }

}
