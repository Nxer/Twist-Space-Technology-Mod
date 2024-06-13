package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import static com.Nxer.TwistSpaceTechnology.util.Utils.filterValidMTE;
import static com.Nxer.TwistSpaceTechnology.util.Utils.mergeArray;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.AdvExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.ExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.IExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.PerfectExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNHVersion;
import com.Nxer.TwistSpaceTechnology.util.NBTUtils;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * The base class of GregTech machine which can use multi Execution Cores to handle multi recipe at same time.
 */
public abstract class MultiExecutionCoreMachineBase<T extends MultiExecutionCoreMachineBase<T>>
    extends ModularizedMachineBase<T> implements IModularizedMachine.ISupportExecutionCore, IExecutionCore {

    public MultiExecutionCoreMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MultiExecutionCoreMachineBase(String aName) {
        super(aName);
    }

    // region Modular and preparing

    /**
     * A collector contains all perfect execution cores.
     */
    protected final Collection<PerfectExecutionCore> perfectExecutionCores = new ArrayList<>();

    /**
     * A collector contains all advanced execution cores.
     */
    protected final Collection<AdvExecutionCore> advExecutionCores = new ArrayList<>();

    /**
     * A collector contains all normal execution cores.
     */
    protected final Collection<ExecutionCore> executionCores = new ArrayList<>();

    /**
     * A collector contains all storage input buses and hatches (ME) to conveniently handle ME network.
     */
    protected final Collection<IRecipeProcessingAwareHatch> MEInputHatches = new ArrayList<>();

    @Override
    public void resetModularHatchCollections() {
        super.resetModularHatchCollections();
        perfectExecutionCores.clear();
        advExecutionCores.clear();
        executionCores.clear();
        MEInputHatches.clear();
    }

    /**
     * Quickly process by using local collectors to get ME input hatches.
     */
    @Override
    protected void startRecipeProcessing() {
        startedRecipeProcessing = true;
        if (MEInputHatches.isEmpty()) return;
        for (IRecipeProcessingAwareHatch hatch : filterValidMTE(MEInputHatches)) {
            hatch.startRecipeProcessing();
        }
    }

    /**
     * Quickly process by using local collectors to get ME input hatches.
     */
    @Override
    protected void endRecipeProcessing() {
        switch (GTNHVersion.version) {
            case GTNH261, GTNH260 -> {
                startedRecipeProcessing = false;
                if (MEInputHatches.isEmpty()) return;
                for (IRecipeProcessingAwareHatch hatch : filterValidMTE(MEInputHatches)) {
                    setResultIfFailure(hatch.endRecipeProcessing(this));
                }
            }
            case GTNH251 -> super.endRecipeProcessing();
            default -> {}
        }

    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;

        // 2.5% for wire loss
        maxEutCanUse = (long) (0.975d * getMaxInputEu());

        // collect ME input hatches to easier flush
        for (GT_MetaTileEntity_Hatch_InputBus hatch : filterValidMTEs(mInputBusses)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                MEInputHatches.add(aware);
            }
        }
        for (GT_MetaTileEntity_Hatch_Input hatch : filterValidMTEs(mInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                MEInputHatches.add(aware);
            }
        }

        // collect execution core module hatches and setup
        Collection<IModularHatch> allExecutionCores = modularHatches.get(ModularHatchTypes.EXECUTION_CORE);
        if (allExecutionCores != null && !allExecutionCores.isEmpty()) {
            for (IModularHatch hatch : allExecutionCores) {
                if (hatch == null) continue;
                if (hatch instanceof PerfectExecutionCore perfectExecutionCore) {
                    perfectExecutionCores.add(perfectExecutionCore);
                    perfectExecutionCore.setup(this);
                } else if (hatch instanceof AdvExecutionCore advExecutionCore) {
                    advExecutionCores.add(advExecutionCore);
                    advExecutionCore.setup(this);
                } else if (hatch instanceof ExecutionCore executionCore) {
                    executionCores.add(executionCore);
                    executionCore.setup(this);
                }
            }
        }

        return true;
    }

    @Override
    public Collection<IExecutionCore> getIdleNormalExecutionCores() {
        Collection<IExecutionCore> cores = new ArrayList<>();
        for (ExecutionCore executionCore : executionCores) {
            if (executionCore != null && executionCore.isIdle()) {
                cores.add(executionCore);
            }
        }
        return cores;
    }

    @Override
    public Collection<IExecutionCore> getIdleAdvancedExecutionCores() {
        Collection<IExecutionCore> cores = new ArrayList<>();
        for (AdvExecutionCore executionCore : advExecutionCores) {
            if (executionCore != null && executionCore.isIdle()) {
                cores.add(executionCore);
            }
        }
        return cores;
    }

    @Override
    public Collection<IExecutionCore> getIdlePerfectExecutionCores() {
        Collection<IExecutionCore> cores = new ArrayList<>();
        for (PerfectExecutionCore executionCore : perfectExecutionCores) {
            if (executionCore != null && executionCore.isIdle()) {
                cores.add(executionCore);
            }
        }
        return cores;
    }

    @Override
    public Collection<IExecutionCore> getAllWorkingExecutionCoresToBoost() {
        Collection<IExecutionCore> cores = new ArrayList<>();
        if (this.isWorking()) {
            cores.add(this);
        }
        for (ExecutionCore executionCore : executionCores) {
            if (executionCore.isWorking()) {
                cores.add(executionCore);
            }
        }
        for (AdvExecutionCore executionCore : advExecutionCores) {
            if (executionCore.isWorking()) {
                cores.add(executionCore);
            }
        }
        return cores;
    }

    @Override
    public int getParallelOfEveryNormalExecutionCore() {
        if (executionCores.isEmpty()) return getMaxParallelRecipes();
        int coreAmount = executionCores.size() + 1;
        int totalParallel = getMaxParallelRecipes();
        if (coreAmount >= totalParallel) return 1;
        return 1 + totalParallel / coreAmount;
    }

    @Override
    public IModularizedMachine.ISupportExecutionCore getMainMachine() {
        return this;
    }

    // endregion

    // region Of an Execution core

    protected ItemStack[] eOutputItems;
    protected FluidStack[] eOutputFluids;
    protected int eMaxProgressingTime;
    protected int eProgressedTime;
    protected int eBoostedTime;
    protected long eEut;

    @Override
    public boolean setProcessing(ProcessingLogic processingLogic) {
        setOutputItems(processingLogic.getOutputItems());
        setOutputFluids(processingLogic.getOutputFluids());
        setMaxProgressingTime(processingLogic.getDuration());
        setEut(processingLogic.getCalculatedEut());
        return done();
    }

    @Override
    public long getEut() {
        return eEut;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            runExecutionCoreTick(aBaseMetaTileEntity, aTick);

            if (aBaseMetaTileEntity.isAllowedToWork() && needToCheckRecipe) doCheckRecipeForExecutionCores();

        }
    }

    public void runExecutionCoreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (eMaxProgressingTime > 0) {
            if (eProgressedTime < eMaxProgressingTime) {
                eProgressedTime++;
            } else {
                // output and finish this work

                if (eOutputItems != null && eOutputItems.length > 0) {
                    this.mergeOutputItems(eOutputItems);
                    eOutputItems = null;
                }

                if (eOutputFluids != null && eOutputFluids.length > 0) {
                    this.mergeOutputFluids(eOutputFluids);
                    eOutputFluids = null;
                }

                if (useMainMachinePower()) {
                    if (!this.tryDecreaseUsedEut(eEut)) {
                        TwistSpaceTechnology.LOG.info(
                            "ERROR: Execution core try decrease used EU/t failed at x" + aBaseMetaTileEntity.getXCoord()
                                + " y"
                                + aBaseMetaTileEntity.getYCoord()
                                + " z"
                                + aBaseMetaTileEntity.getZCoord());
                    }
                }

                eMaxProgressingTime = 0;
                eProgressedTime = 0;
                eBoostedTime = 0;
                eEut = 0;

                this.forceCheckProcessing();

            }
        }
    }

    @Override
    public IExecutionCore boostTick(int tick) {
        eProgressedTime += tick;
        eBoostedTime += tick;
        return this;
    }

    @Override
    public int getNeedProgressingTime() {
        return eMaxProgressingTime - eProgressedTime;
    }

    @Override
    public boolean done() {
        // do nothing
        return true;
    }

    @Override
    public boolean useMainMachinePower() {
        return true;
    }

    // region waila

    public void processWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {

        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("isActive")) {
            currentTip.add(EnumChatFormatting.AQUA +
            // # Power for boosting
            // #zh_CN 已用于加速的功率
                TextEnums.tr("Waila.ExecutionCore.5")
                + EnumChatFormatting.GRAY
                + " : "
                + tag.getLong("eutForBoostLastTick")
                + " EU/t");
        }

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
        } else {
            // #tr Waila.ExecutionCore.IsIdle
            // # This {\WHITE}Execution Core{\GRAY} is idle.
            // #zh_CN 此{\WHITE}执行核心{\GRAY}处于空闲状态
            currentTip.add(TextEnums.tr("Waila.ExecutionCore.IsIdle"));
        }
        // spotless:on
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
            tag.setInteger("maxProgressingTime", eMaxProgressingTime);
            if (eMaxProgressingTime > 0) {
                int outputItemStackAmount = eOutputItems == null ? 0 : eOutputItems.length;
                tag.setInteger("outputItemStackAmount", outputItemStackAmount);
                int outputFluidStackAmount = eOutputFluids == null ? 0 : eOutputFluids.length;
                tag.setInteger("outputFluidStackAmount", outputFluidStackAmount);
                tag.setInteger("progressedTime", eProgressedTime);
                tag.setInteger("boostedTime", eBoostedTime);
                tag.setLong("usingEut", eEut);
                tag.setLong("eutForBoostLastTick", eutForBoostLastTick);
            }
        }
    }
    // endregion

    protected void saveNBTDataItemStacks(NBTTagCompound aNBT) {
        if (eOutputItems != null && eOutputItems.length > 0) {
            aNBT.setInteger("eOutputItemsLength", eOutputItems.length);
            for (int i = 0; i < eOutputItems.length; i++) {
                NBTUtils.saveItem(aNBT, "eOutputItems" + i, eOutputItems[i]);
            }
        }
    }

    protected void saveNBTDataFluidStacks(NBTTagCompound aNBT) {
        if (eOutputFluids != null && eOutputFluids.length > 0) {
            aNBT.setInteger("eOutputFluidsLength", eOutputFluids.length);
            for (int i = 0; i < eOutputFluids.length; i++) {
                NBTUtils.saveFluid(aNBT, "eOutputFluids" + i, eOutputFluids[i]);
            }
        }
    }

    protected void loadNBTDataItemStacks(NBTTagCompound aNBT) {
        int length = aNBT.getInteger("eOutputItemsLength");
        if (length > 0) {
            eOutputItems = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                eOutputItems[i] = NBTUtils.loadItem(aNBT, "eOutputItems" + i);
            }
        }
    }

    protected void loadNBTDataFluidStacks(NBTTagCompound aNBT) {
        int length = aNBT.getInteger("eOutputFluidsLength");
        if (length > 0) {
            eOutputFluids = new FluidStack[length];
            for (int i = 0; i < length; i++) {
                eOutputFluids[i] = NBTUtils.loadFluid(aNBT, "eOutputFluids" + i);
            }
        }
    }

    @Override
    public boolean setup(ISupportExecutionCore mainMachine) {
        return true;
    }

    @Override
    public void reset() {
        eOutputItems = null;
        eOutputFluids = null;
        eMaxProgressingTime = 0;
        eProgressedTime = 0;
        eBoostedTime = 0;
        eEut = 0;
    }

    @Override
    public void shutDown() {
        eOutputItems = null;
        eOutputFluids = null;
        eMaxProgressingTime = 0;
        eProgressedTime = 0;
        eBoostedTime = 0;
        eEut = 0;
    }

    @Override
    public boolean isIdle() {
        return this.eMaxProgressingTime < 1;
    }

    @Override
    public boolean isWorking() {
        return this.eMaxProgressingTime > 0;
    }

    @Override
    public IExecutionCore setOutputItems(ItemStack[] outputItems) {
        this.eOutputItems = outputItems;
        return this;
    }

    @Override
    public IExecutionCore setOutputFluids(FluidStack[] outputFluids) {
        this.eOutputFluids = outputFluids;
        return this;
    }

    @Override
    public IExecutionCore setMaxProgressingTime(int maxProgressingTime) {
        this.eMaxProgressingTime = maxProgressingTime;
        return this;
    }

    @Override
    public IExecutionCore setEut(long eut) {
        this.eEut = eut;
        return this;
    }

    // endregion

    // region Logic

    protected static final int[] progressingTick = { 1, 5, 10, 20, 32, 64, 128, 192, 256, 512 };
    protected byte progressingTickIndex = 5;
    protected boolean startedRecipeProcessing = false;
    protected boolean needToCheckRecipe = true;
    protected long maxEutCanUse = 0;
    protected long eutForBoostLastTick = 0;
    protected boolean isNoOverclockCalculator = false;
    protected CheckRecipeResult lastCheck = CheckRecipeResultRegistry.NO_RECIPE;

    protected abstract OverclockType getOverclockType();

    protected int getBaseProgressingTick() {
        return progressingTick[progressingTickIndex];
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        return GTNHVersion.version == GTNHVersion.Version.GTNH251
            ? onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, null)
            : super.onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.progressingTickIndex = (byte) ((this.progressingTickIndex + 1) % 10);
            // #tr MultiExecutionCoreMachineBase.progressingTickIndex
            // # The base run cycle time is set to{\SPACE}
            // #zh_CN 基础运行循环时间设置为{\SPACE}
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("MultiExecutionCoreMachineBase.progressingTickIndex")
                    + getBaseProgressingTick()
                    + " tick");

            return true;
        }
        return false;
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);

        ret[origin.length] = getOverclockType().getDescription();

        return ret;
    }

    public void mergeOutputItems(ItemStack... outputs) {
        mOutputItems = mergeArray(mOutputItems, outputs);
    }

    public void mergeOutputFluids(FluidStack... outputs) {
        mOutputFluids = mergeArray(mOutputFluids, outputs);
    }

    public boolean tryUseEut(long eut) {
        long canUse = getEutCanUse();
        if (canUse >= eut) {
            lEUt -= eut;
            return true;
        }
        return false;
    }

    public boolean tryDecreaseUsedEut(long eut) {
        if ((-lEUt) >= eut) {
            lEUt += eut;
            return true;
        }
        return false;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {

        // TODO boost execution core progressing
        long eutForBoostLastTick = boostExecutionCoreProcessing();

        // EU costing
        if (this.lEUt < 0) {
            if (!drainEnergyInput(getActualEnergyUsage())) {
                shutDownAllExecutionCore();
                if (GTNHVersion.version == GTNHVersion.Version.GTNH251) stopMachine();
                else stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                return false;
            }
        }

        tryDecreaseUsedEut(eutForBoostLastTick);
        return true;
    }

    @Override
    public void stopMachine() {
        eutForBoostLastTick = 0;
        this.stopMachine();
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        eutForBoostLastTick = 0;
        super.stopMachine(reason);
    }

    /**
     * Use idle EUt to boost execution cores working.
     *
     * @return The value of EUt used for boosting.
     */
    protected long boostExecutionCoreProcessing() {

        eutForBoostLastTick = 0;
        Collection<IExecutionCore> workingCores = getAllWorkingExecutionCoresToBoost();
        if (workingCores.isEmpty()) {
            return 0;
        }

        int workingCoreWaitBoostAmount = workingCores.size();
        long maxEUtCanUse = getEutCanUse();
        if (maxEUtCanUse < workingCoreWaitBoostAmount) return 0;

        boolean perfectOverclock = getOverclockType().isPerfectOverclock();

        for (IExecutionCore executionCore : workingCores) {
            long maxAverageEUtCanUse = maxEUtCanUse / workingCoreWaitBoostAmount;
            workingCoreWaitBoostAmount--;
            int maxTickCanBoost = executionCore.getNeedProgressingTime();
            if (maxTickCanBoost < 1) continue;

            long thisCoreEUt = executionCore.getEut();
            long thisCoreUsed = 0;

            long eutExtraMultiplier = maxAverageEUtCanUse / thisCoreEUt;

            int boostedTick;
            if (perfectOverclock) {
                if (eutExtraMultiplier > maxTickCanBoost) {
                    boostedTick = maxTickCanBoost;
                } else {
                    boostedTick = (int) eutExtraMultiplier;
                }
                thisCoreUsed = boostedTick * thisCoreEUt;

            } else {
                long eutMultiplier = eutExtraMultiplier + 1;
                int canOverclockTimes = (int) Math
                    .min(Math.log(eutMultiplier) / Utils.LOG4, Math.log(maxTickCanBoost) / Utils.LOG2);
                boostedTick = (int) Math.pow(2, canOverclockTimes) - 1;
                thisCoreUsed = (long) ((Math.pow(4, canOverclockTimes) - 1) * thisCoreEUt);
            }

            maxEUtCanUse -= thisCoreUsed;
            eutForBoostLastTick += thisCoreUsed;
            tryUseEut(thisCoreUsed);
            executionCore.boostTick(boostedTick);
        }

        return eutForBoostLastTick;
    }

    protected void shutDownAllExecutionCore() {
        this.shutDown();
        for (IExecutionCore executionCore : executionCores) {
            executionCore.shutDown();
        }
        for (IExecutionCore executionCore : advExecutionCores) {
            executionCore.shutDown();
        }
    }

    protected void resetAllExecutionCore() {
        this.reset();
        for (IExecutionCore executionCore : executionCores) {
            executionCore.reset();
        }
        for (IExecutionCore executionCore : advExecutionCores) {
            executionCore.reset();
        }
        for (IExecutionCore executionCore : perfectExecutionCores) {
            executionCore.reset();
        }
        resetModularHatchCollections();
    }

    @Override
    public void onBlockDestroyed() {
        resetAllExecutionCore();
        super.onBlockDestroyed();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("progressingTickIndex", progressingTickIndex);
        aNBT.setBoolean("startedRecipeProcessing", startedRecipeProcessing);
        aNBT.setLong("maxEutCanUse", maxEutCanUse);
        aNBT.setLong("eutForBoostLastTick", eutForBoostLastTick);
        aNBT.setBoolean("isNoOverclockCalculator", isNoOverclockCalculator);
        aNBT.setInteger("eMaxProgressingTime", eMaxProgressingTime);
        aNBT.setInteger("eProgressedTime", eProgressedTime);
        aNBT.setInteger("eBoostedTime", eBoostedTime);
        aNBT.setLong("eEut", eEut);
        saveNBTDataItemStacks(aNBT);
        saveNBTDataFluidStacks(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        progressingTickIndex = aNBT.getByte("progressingTickIndex");
        startedRecipeProcessing = aNBT.getBoolean("startedRecipeProcessing");
        maxEutCanUse = aNBT.getLong("maxEutCanUse");
        eutForBoostLastTick = aNBT.getLong("eutForBoostLastTick");
        isNoOverclockCalculator = aNBT.getBoolean("isNoOverclockCalculator");
        eMaxProgressingTime = aNBT.getInteger("eMaxProgressingTime");
        eProgressedTime = aNBT.getInteger("eProgressedTime");
        eBoostedTime = aNBT.getInteger("eBoostedTime");
        eEut = aNBT.getLong("eEut");
        loadNBTDataItemStacks(aNBT);
        loadNBTDataFluidStacks(aNBT);
    }

    public long getEutCanUse() {
        return maxEutCanUse + lEUt;
    }

    public long getEutCanUse(int coreAmount) {
        return (maxEutCanUse + lEUt) / coreAmount;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new MultiExecutionProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclock(getOverclockType().timeReduction, getOverclockType().powerIncrease);
                return super.process();
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                if (isNoOverclockCalculator) {
                    return GT_OverclockCalculator.ofNoOverclock(recipe);
                } else {
                    return super.createOverclockCalculator(recipe);
                }
            }

        };
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        logic.clear();
        logic.setMachine(this);
        logic.setRecipeMapSupplier(this::getRecipeMap);
        logic.setVoidProtection(false, false);
        logic.setBatchSize(isBatchModeEnabled() ? getMaxBatchSize() : 1);
        logic.setRecipeLocking(this, false);
        logic.setAvailableVoltage(getEutCanUse());
        logic.setAvailableAmperage(1);
        logic.setMaxParallel(getParallelOfEveryNormalExecutionCore());
    }

    protected void setupProcessingLogicWirelessEU(ProcessingLogic logic) {
        logic.clear();
        logic.setMachine(this);
        logic.setRecipeMapSupplier(this::getRecipeMap);
        logic.setVoidProtection(false, false);
        logic.setBatchSize(isBatchModeEnabled() ? getMaxBatchSize() : 1);
        logic.setRecipeLocking(this, false);
        logic.setAvailableVoltage(getEutCanUse());
        logic.setAvailableAmperage(1);
        logic.setMaxParallel(Integer.MAX_VALUE);
    }

    @Nonnull
    protected CheckRecipeResult doCheckRecipe() {
        return super.doCheckRecipe();
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessingMM() {
        doCheckRecipeForExecutionCores();

        mMaxProgresstime = getBaseProgressingTick();

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void forceCheckProcessing() {
        needToCheckRecipe = true;
    }

    public void doCheckRecipeForExecutionCores() {
        needToCheckRecipe = false;
        if (checkProcessingForPerfectExecutionCore() == CheckRecipeResults.SetProcessingFailed) {
            disableWorking();
            if (GTNHVersion.version != GTNHVersion.Version.GTNH251)
                this.setResultIfFailure(CheckRecipeResults.SetProcessingFailed);
            return;
        }
        if (checkProcessingForAdvancedExecutionCore() == CheckRecipeResults.SetProcessingFailed) {
            disableWorking();
            if (GTNHVersion.version != GTNHVersion.Version.GTNH251)
                this.setResultIfFailure(CheckRecipeResults.SetProcessingFailed);
            return;
        }
        lastCheck = checkProcessingForNormalExecutionCore();
    }

    public @NotNull CheckRecipeResult checkProcessingForNormalExecutionCore() {
        Collection<IExecutionCore> idleExecutionCores = getIdleNormalExecutionCores();
        // main machine is also a normal execution core.
        if (this.isIdle()) idleExecutionCores.add(this);
        if (idleExecutionCores.isEmpty()) {
            return CheckRecipeResults.NoIdleExecutionCore;
        }

        // set overclock open
        this.isNoOverclockCalculator = false;
        setupProcessingLogic(processingLogic);
        int idleAmount = idleExecutionCores.size();
        boolean flag = false;
        for (IExecutionCore executionCore : idleExecutionCores) {
            processingLogic.setAvailableVoltage(getEutCanUse(idleAmount));
            idleAmount--;
            CheckRecipeResult result = checkExecutionCoreProcessing(executionCore);
            if (result == CheckRecipeResults.SetProcessingFailed) return result;
            if (!result.wasSuccessful()) {
                break;
            } else {
                flag = true;
            }
        }

        return flag ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NO_RECIPE;
    }

    /**
     * Use main machine power parameters to check recipe and setup advanced execution cores.
     * But the real power consumption is handled by advanced execution core internal.
     *
     * @return If there is a new processing has been set. Or some processing set failed.
     */
    public @NotNull CheckRecipeResult checkProcessingForAdvancedExecutionCore() {
        Collection<IExecutionCore> idleExecutionCores = getIdleAdvancedExecutionCores();
        if (idleExecutionCores.isEmpty()) {
            return CheckRecipeResults.NoIdleExecutionCore;
        }

        // set overclock open
        this.isNoOverclockCalculator = false;
        setupProcessingLogic(processingLogic);
        boolean flag = false;
        for (IExecutionCore executionCore : idleExecutionCores) {
            CheckRecipeResult result = checkExecutionCoreProcessing(executionCore);
            if (result == CheckRecipeResults.SetProcessingFailed) return result;
            if (!result.wasSuccessful()) {
                break;
            } else {
                flag = true;
            }
        }

        return flag ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NO_RECIPE;
    }

    /**
     * Make Perfect Execution Cores as a real Wireless EU net joining machine to find and process recipe.
     *
     * @return If there is a new processing has been set. Or some processing set failed.
     */
    public CheckRecipeResult checkProcessingForPerfectExecutionCore() {
        Collection<IExecutionCore> idleExecutionCores = getIdlePerfectExecutionCores();
        if (idleExecutionCores.isEmpty()) {
            return CheckRecipeResults.NoIdleExecutionCore;
        }

        // set overclock close
        this.isNoOverclockCalculator = true;
        setupProcessingLogicWirelessEU(processingLogic);
        // set wireless EU net mode to find recipe
        processingLogic.setAvailableVoltage(Long.MAX_VALUE);
        // flag to show has do a new recipe processing
        boolean flag = false;
        for (IExecutionCore executionCore : idleExecutionCores) {
            CheckRecipeResult result = checkExecutionCoreProcessing(executionCore);
            if (result == CheckRecipeResults.SetProcessingFailed) return result;
            if (!result.wasSuccessful()) {
                break;
            } else {
                flag = true;
            }
        }

        return flag ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NO_RECIPE;
    }

    /**
     * Others were all prepared enough, then check recipe for every execution core.
     *
     * @param executionCore Check recipe for this execution core.
     */
    protected CheckRecipeResult checkExecutionCoreProcessing(IExecutionCore executionCore) {
        if (!startedRecipeProcessing) startRecipeProcessing();
        CheckRecipeResult result = doCheckRecipe();
        if (result.wasSuccessful()) {
            // set basic parameters to execution core
            boolean success = executionCore.setProcessing(processingLogic);
            if (!success) {
                updateSlots();
                endRecipeProcessing();
                return CheckRecipeResults.SetProcessingFailed;
            }

            // set power parameters of this controller
            if (executionCore.useMainMachinePower()) {
                tryUseEut(processingLogic.getCalculatedEut());
            }
        }
        updateSlots();
        endRecipeProcessing();
        return result;
    }

    /**
     * Multi Execution Core Machine will all not support void protection.
     */
    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    // endregion

}
