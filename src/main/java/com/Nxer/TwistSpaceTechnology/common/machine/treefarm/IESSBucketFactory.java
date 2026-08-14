package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;

public interface IESSBucketFactory {

    String getNBTIdentifier();

    ESSArtificialGreenHouseOutputBucket tryCreateBucket(TST_EcoSphereSimulator greenhouse);

    ESSArtificialGreenHouseOutputBucket restore(NBTTagCompound nbt);
}
