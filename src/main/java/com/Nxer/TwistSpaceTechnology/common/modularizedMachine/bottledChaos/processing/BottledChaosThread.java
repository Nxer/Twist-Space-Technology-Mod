package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosMaterialStacks;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosMaterialSummary;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosOutputBuffer;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result.BottledChaosResultData;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;

/** A logical server-tick job, not a Java worker thread. */
public final class BottledChaosThread {

    public final BottledChaosOutputBuffer outputs = new BottledChaosOutputBuffer();
    public final BottledChaosOutputBuffer pendingCache = new BottledChaosOutputBuffer();
    public String processingFamily = "";
    public List<BottledChaosUpkeep.Demand> upkeep = Collections.emptyList();
    public List<BottledChaosUpkeep.ItemDemand> roundItems = Collections.emptyList();
    public ItemStack credential;
    public long requiredPools;
    public long recipeVoltage;
    public long eut;
    private BigInteger wirelessCost = BigInteger.ZERO;

    public int duration;
    public int progress;
    public int parallels;
    public long nextCheckTick;
    public String status = BottledChaosStatus.IDLE.key();
    public CheckRecipeResult blockedRoute = CheckRecipeResultRegistry.NO_RECIPE;
    private CheckRecipeResult result = CheckRecipeResultRegistry.SUCCESSFUL;
    private NBTTagCompound finalOutputSummary;

    public boolean isWireless() {
        return wirelessCost.signum() > 0;
    }

    public BigInteger power() {
        return isWireless() ? wirelessCost.add(BigInteger.valueOf(BottledChaosProcessingLogic.WIRELESS_TICKS - 1))
            .divide(BigInteger.valueOf(BottledChaosProcessingLogic.WIRELESS_TICKS)) : BigInteger.valueOf(eut);
    }

    /** Paid-job materials are stable until completion; do not traverse the recipe graph each GUI tick. */
    public NBTTagCompound getFinalOutputSummary() {
        if (finalOutputSummary == null) finalOutputSummary = BottledChaosMaterialSummary
            .forFinalJob(outputs, ItemBottledChaosCredential.getRouteMask(credential));
        return finalOutputSummary;
    }

    public CheckRecipeResult result() {
        return result;
    }

    /** Invalidate a search failure without changing a paid job or its displayed status. */
    public void resetResult() {
        result = CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /** Called only after the host's input transaction has succeeded. */
    public void begin(ItemStack authorization, BottledChaosProcessingLogic logic) {
        finalOutputSummary = null;
        blockedRoute = logic.getBlockedRoute();
        credential = authorization.copy();
        requiredPools = logic.getRequiredPools();
        upkeep = logic.getUpkeep();
        roundItems = logic.getPreparedPlan()
            .roundItems();
        recipeVoltage = logic.getBaseRecipeVoltage();
        eut = logic.getCalculatedEut();
        wirelessCost = logic.getWirelessCost();
        duration = Math.max(1, logic.getDuration());
        progress = 0;
        parallels = logic.getCurrentParallels();
        outputs.set(logic.getLongItemOutputs(), logic.getLongFluidOutputs());
    }

    public void setPendingCache(String family, BottledChaosMaterialStacks pending) {
        finalOutputSummary = null;
        processingFamily = family;
        pendingCache.set(pending.items(), pending.fluids());
    }

    public void searched(CheckRecipeResult searchResult, long retryTick) {
        result = searchResult;
        status = result.wasSuccessful() ? BottledChaosStatus.ACTIVE.key()
            : result == CheckRecipeResultRegistry.NO_RECIPE ? BottledChaosStatus.IDLE.key() : result.getDisplayString();
        nextCheckTick = retryTick;
    }

    public void activate() {
        status = BottledChaosStatus.ACTIVE.key();
        resetResult();
    }

    public void advance() {
        progress++;
        activate();
    }

    /** Reuse the rendered failure until it changes; do not format a message every tick. */
    public void fail(CheckRecipeResult failure) {
        if (!failure.equals(result) || BottledChaosStatus.PAUSED.key()
            .equals(status)) status = failure.getDisplayString();
        result = failure;
    }

    public void waitForOutput(CheckRecipeResult failure, long retryTick) {
        result = failure;
        status = failure.getDisplayString();
        nextCheckTick = retryTick;
    }

    /** Pausing preserves the paid job, pending cache and previous failure. */
    public boolean pause() {
        if (BottledChaosStatus.PAUSED.key()
            .equals(status)) return false;
        status = BottledChaosStatus.PAUSED.key();
        return true;
    }

    public boolean resumeIdle() {
        if (isBusy() || !BottledChaosStatus.PAUSED.key()
            .equals(status)) return false;
        status = BottledChaosStatus.IDLE.key();
        return true;
    }

    public boolean isBusy() {
        return duration > 0;
    }

    public void complete() {
        finalOutputSummary = null;
        blockedRoute = CheckRecipeResultRegistry.NO_RECIPE;
        outputs.clear();
        pendingCache.clear();
        processingFamily = "";
        upkeep = Collections.emptyList();
        roundItems = Collections.emptyList();
        credential = null;
        duration = progress = parallels = 0;
        eut = recipeVoltage = requiredPools = nextCheckTick = 0;
        wirelessCost = BigInteger.ZERO;
        status = BottledChaosStatus.IDLE.key();
        resetResult();
    }

    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        GTUtility.saveItem(tag, "credential", credential);
        tag.setString("recipePools", BottledChaosMachineConfiguration.poolKeys(requiredPools));
        tag.setLong("recipeVoltage", recipeVoltage);
        tag.setLong("eut", eut);
        tag.setByteArray("wirelessCost", wirelessCost.toByteArray());
        tag.setInteger("duration", duration);
        tag.setInteger("progress", progress);
        tag.setInteger("parallels", parallels);
        tag.setTag("outputs", outputs.save());
        tag.setString("processingFamily", processingFamily);
        tag.setTag("pendingCache", pendingCache.save());
        tag.setTag("upkeep", BottledChaosUpkeep.saveDemands(upkeep));
        tag.setTag("roundItems", BottledChaosUpkeep.saveItems(roundItems));
        if (blockedRoute != CheckRecipeResultRegistry.NO_RECIPE) {
            NBTTagCompound failure = new NBTTagCompound();
            BottledChaosResultData.write(failure, blockedRoute);
            tag.setTag("blockedRoute", failure);
        }
        return tag;
    }

    public void load(NBTTagCompound tag) {
        finalOutputSummary = null;
        credential = GTUtility.loadItem(tag, "credential");
        requiredPools = BottledChaosMachineConfiguration.maskFromKeys(tag.getString("recipePools"));
        recipeVoltage = tag.getLong("recipeVoltage");
        eut = Math.max(0, tag.getLong("eut"));
        byte[] cost = tag.getByteArray("wirelessCost");
        wirelessCost = cost.length == 0 ? BigInteger.ZERO : new BigInteger(1, cost);
        duration = Math.max(0, tag.getInteger("duration"));
        progress = Math.max(0, Math.min(duration, tag.getInteger("progress")));
        parallels = Math.max(0, tag.getInteger("parallels"));
        outputs.load(tag.getCompoundTag("outputs"));
        processingFamily = tag.getString("processingFamily");
        pendingCache.load(tag.getCompoundTag("pendingCache"));
        upkeep = BottledChaosUpkeep.loadDemands(tag.getTagList("upkeep", 10));
        roundItems = BottledChaosUpkeep.loadItems(tag.getTagList("roundItems", 10));
        blockedRoute = tag.hasKey("blockedRoute", 10) ? BottledChaosResultData.read(tag.getCompoundTag("blockedRoute"))
            : CheckRecipeResultRegistry.NO_RECIPE;
        nextCheckTick = 0;
        resetResult();
    }
}
