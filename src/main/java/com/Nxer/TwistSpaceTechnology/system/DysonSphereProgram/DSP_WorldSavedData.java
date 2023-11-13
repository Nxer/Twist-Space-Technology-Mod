package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class DSP_WorldSavedData extends WorldSavedData {

    public static DSP_WorldSavedData INSTANCE;
    private static final String DATA_NAME = "TST_DysonSphereProgramWorldSavedData";

    public DSP_WorldSavedData() {
        super(DATA_NAME);
    }

    public DSP_WorldSavedData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_76184_1_) {

    }

    @Override
    public void writeToNBT(NBTTagCompound p_76187_1_) {

    }
}
