package com.Nxer.TwistSpaceTechnology.common.egs;

import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;

public interface IEGSBucketFactory {

    String getNBTIdentifier();

    EGSBucket tryCreateBucket(TST_MegaTreeFarm Greenhouse);

    EGSBucket restore(NBTTagCompound nbt);
}
