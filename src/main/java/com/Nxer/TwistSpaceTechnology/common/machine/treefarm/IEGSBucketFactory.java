package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;

public interface IEGSBucketFactory {

    String getNBTIdentifier();

    EGSArtificialGreenHouseOutputBucket tryCreateBucket(TST_MegaTreeFarm Greenhouse);

    EGSArtificialGreenHouseOutputBucket restore(NBTTagCompound nbt);
}
