package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class DysonSphereProgramWorldSavedData extends WorldSavedData {

    public static DysonSphereProgramWorldSavedData INSTANCE;
    private static final String DATA_NAME = "TST_DysonSphereProgramWorldSavedData";

    public DysonSphereProgramWorldSavedData() {
        super(DATA_NAME);
    }

    public DysonSphereProgramWorldSavedData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_76184_1_) {

    }

    @Override
    public void writeToNBT(NBTTagCompound p_76187_1_) {

    }
}
