package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosMaterialStacks;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosOutputBuffer;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;

/** A logical server-tick job, not a Java worker thread. */
public final class BottledChaosThread {

    public final BottledChaosOutputBuffer outputs = new BottledChaosOutputBuffer();
    public final BottledChaosOutputBuffer pendingCache = new BottledChaosOutputBuffer();
    public String processingFamily = "";
    public List<BottledChaosUpkeep.Demand> upkeep = Collections.emptyList();
    public ItemStack credential;
    public long requiredPools;
    public long recipeVoltage;
    public long eut;
    public int duration;
    public int progress;
    public int parallels;
    public long nextCheckTick;
    public String status = "GT5U.waila.machine.idle";
    private CheckRecipeResult result = CheckRecipeResultRegistry.SUCCESSFUL;

    public CheckRecipeResult result() {
        return result;
    }

    /** Invalidate a search failure without changing a paid job or its displayed status. */
    public void resetResult() {
        result = CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /** Called only after the host's input transaction has succeeded. */
    public void begin(ItemStack authorization, BottledChaosProcessingLogic logic) {
        credential = authorization.copy();
        requiredPools = logic.getRequiredPools();
        upkeep = logic.getUpkeep();
        recipeVoltage = logic.getBaseRecipeVoltage();
        eut = logic.getCalculatedEut();
        duration = Math.max(1, logic.getDuration());
        progress = 0;
        parallels = logic.getCurrentParallels();
        outputs.set(logic.getLongItemOutputs(), logic.getLongFluidOutputs());
    }

    public void setPendingCache(String family, BottledChaosMaterialStacks pending) {
        processingFamily = family;
        pendingCache.set(pending.items(), pending.fluids());
    }

    public void searched(CheckRecipeResult searchResult, long retryTick) {
        result = searchResult;
        status = result.wasSuccessful() ? "GT5U.gui.text.status.active"
            : result == CheckRecipeResultRegistry.NO_RECIPE ? "GT5U.waila.machine.idle" : result.getDisplayString();
        nextCheckTick = retryTick;
    }

    public void activate() {
        status = "GT5U.gui.text.status.active";
        resetResult();
    }

    public void advance() {
        progress++;
        activate();
    }

    /** Reuse the rendered failure until it changes; do not format a message every tick. */
    public void fail(CheckRecipeResult failure) {
        if (!failure.equals(result) || "tst.bottledchaos.paused".equals(status)) status = failure.getDisplayString();
        result = failure;
    }

    public void waitForOutput(CheckRecipeResult failure, long retryTick) {
        result = failure;
        status = failure.getDisplayString();
        nextCheckTick = retryTick;
    }

    /** Pausing preserves the paid job, pending cache and previous failure. */
    public boolean pause() {
        if ("tst.bottledchaos.paused".equals(status)) return false;
        status = "tst.bottledchaos.paused";
        return true;
    }

    public boolean resumeIdle() {
        if (isBusy() || !"tst.bottledchaos.paused".equals(status)) return false;
        status = "GT5U.waila.machine.idle";
        return true;
    }

    public boolean isBusy() {
        return duration > 0;
    }

    public void complete() {
        outputs.clear();
        pendingCache.clear();
        processingFamily = "";
        upkeep = Collections.emptyList();
        credential = null;
        duration = progress = parallels = 0;
        eut = recipeVoltage = requiredPools = nextCheckTick = 0;
        status = "GT5U.waila.machine.idle";
        resetResult();
    }

    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        GTUtility.saveItem(tag, "credential", credential);
        tag.setString("recipePools", BottledChaosMachineConfiguration.poolKeys(requiredPools));
        tag.setLong("recipeVoltage", recipeVoltage);
        tag.setLong("eut", eut);
        tag.setInteger("duration", duration);
        tag.setInteger("progress", progress);
        tag.setInteger("parallels", parallels);
        tag.setTag("outputs", outputs.save());
        tag.setString("processingFamily", processingFamily);
        tag.setTag("pendingCache", pendingCache.save());
        tag.setTag("upkeep", BottledChaosUpkeep.saveDemands(upkeep));
        return tag;
    }

    public void load(NBTTagCompound tag) {
        credential = GTUtility.loadItem(tag, "credential");
        requiredPools = BottledChaosMachineConfiguration.maskFromKeys(tag.getString("recipePools"));
        recipeVoltage = tag.getLong("recipeVoltage");
        eut = Math.max(0, tag.getLong("eut"));
        duration = Math.max(0, tag.getInteger("duration"));
        progress = Math.max(0, Math.min(duration, tag.getInteger("progress")));
        parallels = Math.max(0, tag.getInteger("parallels"));
        outputs.load(tag.getCompoundTag("outputs"));
        processingFamily = tag.getString("processingFamily");
        pendingCache.load(tag.getCompoundTag("pendingCache"));
        upkeep = BottledChaosUpkeep.loadDemands(tag.getTagList("upkeep", 10));
        nextCheckTick = 0;
        resetResult();
    }
}
