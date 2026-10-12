package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
    public String status = "tst.modular.machine.BottledChaos.idle";

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
        status = "tst.modular.machine.BottledChaos.idle";
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
    }
}
