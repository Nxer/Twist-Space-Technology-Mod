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
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.util.GTUtility;
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

    protected final ExecutionCoreOutputBuffer outputBuffer = new ExecutionCoreOutputBuffer();
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
                    // #tr tst.modular.machine.ExecutionCore.waila.execution_core.1
                    // # Total basic max progressing time
                    // #zh_CN 配方总基础耗时
                    TSTUtils.tr("tst.modular.machine.ExecutionCore.waila.execution_core.1") + " : "
                        + maxProgressingTime + " tick ("
                        + (maxProgressingTime / 20) + "s)");
                int progressedTime = tag.getInteger("progressedTime");
                currentTip.add(
                    // #tr tst.modular.machine.ExecutionCore.waila.execution_core.2
                    // # Progressed time
                    // #zh_CN 已执行时间
                    TSTUtils.tr("tst.modular.machine.ExecutionCore.waila.execution_core.2") + " : "
                        + progressedTime + " tick ("
                        + (progressedTime / 20) + "s)"
                );
                int boostedTime = tag.getInteger("boostedTime");
                currentTip.add(
                    // #tr tst.modular.machine.ExecutionCore.waila.execution_core.4
                    // # Boosted time
                    // #zh_CN 已加速时间
                    TSTUtils.tr("tst.modular.machine.ExecutionCore.waila.execution_core.4") + " : "
                        + boostedTime + " tick ("
                        + (boostedTime / 20) + "s)"
                );
                currentTip.add(
                    // #tr tst.modular.machine.ExecutionCore.waila.execution_core.3
                    // # Basic power consumption
                    // #zh_CN 基础功率
                    TSTUtils.tr("tst.modular.machine.ExecutionCore.waila.execution_core.3") + " : "
                        + tag.getLong("usingEut") + " EU/t"
                );
                // spotless:on
            } else {
                // #tr tst.modular.machine.ExecutionCore.waila.execution_core.is_idle
                // # This {\WHITE}Execution Core{\GRAY} is idle.
                // #zh_CN 此{\WHITE}执行核心{\GRAY}处于空闲状态
                currentTip.add(TSTUtils.tr("tst.modular.machine.ExecutionCore.waila.execution_core.is_idle"));
            }
        } else {
            // #tr tst.modular.machine.ExecutionCore.waila.execution_core.has_not_been_setup
            // # This execution core has not been setup.
            // #zh_CN 此执行核心未初始化
            currentTip.add(TSTUtils.tr("tst.modular.machine.ExecutionCore.waila.execution_core.has_not_been_setup"));
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
                tag.setInteger("outputItemStackAmount", outputBuffer.getItemOutputCount());
                tag.setInteger("outputFluidStackAmount", outputBuffer.getFluidOutputCount());
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
        if (aBaseMetaTileEntity.isServerSide()) {
            runExecutionCoreTick(aBaseMetaTileEntity, aTick);

            if (maxProgressingTime <= 0 && active) {
                trySetInactive();
            }

        }
    }

    @Override
    public ExecutionCoreOutputBuffer getOutputBuffer() {
        return outputBuffer;
    }

    public void runExecutionCoreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (hasBeenSetup && mainMachine != null) {
                if (maxProgressingTime > 0) {
                    if (progressedTime < maxProgressingTime - 1) {
                        progressedTime++;
                    } else {
                        // output and finish this work
                        outputBuffer.mergeInto(mainMachine);

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
        outputBuffer.clear();
        maxProgressingTime = 0;
        progressedTime = 0;
        eut = 0;
        setInactiveCritical();
    }

    public void resetParameters() {
        outputBuffer.clear();
        maxProgressingTime = 0;
        progressedTime = 0;
        boostedTime = 0;
        eut = 0;
        trySetInactive();
    }

    @Override
    public void reset() {
        outputBuffer.clear();
        maxProgressingTime = 0;
        progressedTime = 0;
        eut = 0;
        hasBeenSetup = false;
        mainMachine = null;
        setInactiveCritical();
    }

    protected boolean active = false;
    protected byte trySetInactiveTimes = 0;

    public void setActive(boolean active) {
        this.active = active;
        IGregTechTileEntity mte = getBaseMetaTileEntity();
        if (mte != null) mte.setActive(active);
    }

    public void trySetActive() {
        trySetInactiveTimes = 0;
        setActive(true);
    }

    public void trySetInactive() {
        if (trySetInactiveTimes > 2) {
            trySetInactiveTimes = 0;
            setActive(false);
        } else {
            trySetInactiveTimes++;
        }
    }

    public void setInactiveCritical() {
        trySetInactiveTimes = 0;
        setActive(false);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("MH_active", active);
        aNBT.setByte("trySetInactiveTimes", trySetInactiveTimes);
        aNBT.setInteger("maxProgressingTime", maxProgressingTime);
        aNBT.setInteger("progressedTime", progressedTime);
        aNBT.setInteger("boostedTime", boostedTime);
        aNBT.setLong("eut", eut);
        outputBuffer.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        active = aNBT.getBoolean("MH_active");
        trySetInactiveTimes = aNBT.getByte("trySetInactiveTimes");
        maxProgressingTime = aNBT.getInteger("maxProgressingTime");
        progressedTime = aNBT.getInteger("progressedTime");
        boostedTime = aNBT.getInteger("boostedTime");
        eut = aNBT.getLong("eut");
        outputBuffer.loadNBTData(aNBT, "output");
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
    public List<IOutputHatch> getOutputHatches() {
        if (mainMachine instanceof IVoidable m) {
            return m.getOutputHatches();
        }
        return Collections.emptyList();
    }

    @Override
    public List<IOutputHatch> getOutputHatches(FluidStack[] toOutput) {
        if (mainMachine instanceof IVoidable m) {
            return m.getOutputHatches(toOutput);
        }
        return Collections.emptyList();
    }

    @Override
    public List<IOutputBus> getOutputBusses() {
        if (mainMachine instanceof IVoidable m) {
            return m.getOutputBusses();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean canDumpItemToME(List<GTUtility.ItemId> outputs) {
        if (mainMachine instanceof IVoidable m) {
            return m.canDumpItemToME(outputs);
        }
        return false;
    }

    @Override
    public boolean canDumpFluidToME(List<GTUtility.FluidId> outputs) {
        if (mainMachine instanceof IVoidable m) {
            return m.canDumpFluidToME(outputs);
        }
        return false;
    }

    // endregion

    // region Getter and Setter

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
