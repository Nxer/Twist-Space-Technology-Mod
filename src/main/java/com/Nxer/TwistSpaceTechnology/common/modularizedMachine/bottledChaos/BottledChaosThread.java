package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.util.GTUtility;

/** A logical server-tick job, not a Java worker thread. */
public final class BottledChaosThread {

    public final BottledChaosOutputBuffer outputs = new BottledChaosOutputBuffer();
    public ItemStack credential;
    public long requiredCategories;
    public long eut;
    public int duration;
    public int progress;
    public int parallels;
    public long nextCheckTick;
    public String status = "bottled_chaos.idle";

    public boolean isBusy() {
        return duration > 0;
    }

    public void complete() {
        outputs.clear();
        credential = null;
        duration = progress = parallels = 0;
        eut = requiredCategories = nextCheckTick = 0;
        status = "bottled_chaos.idle";
    }

    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        GTUtility.saveItem(tag, "credential", credential);
        tag.setLong("categories", requiredCategories);
        tag.setLong("eut", eut);
        tag.setInteger("duration", duration);
        tag.setInteger("progress", progress);
        tag.setInteger("parallels", parallels);
        tag.setTag("outputs", outputs.save());
        return tag;
    }

    public void load(NBTTagCompound tag) {
        credential = GTUtility.loadItem(tag, "credential");
        requiredCategories = tag.getLong("categories");
        eut = Math.max(0, tag.getLong("eut"));
        duration = Math.max(0, tag.getInteger("duration"));
        progress = Math.max(0, Math.min(duration, tag.getInteger("progress")));
        parallels = Math.max(0, tag.getInteger("parallels"));
        outputs.load(tag.getCompoundTag("outputs"));
        nextCheckTick = 0;
    }
}
