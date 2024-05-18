package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.OverclockControllers;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class StaticOverclockController extends StaticOverclockControllerBase {

    public StaticOverclockController(int aID, String aName, String aNameRegional, int aTier, int timeReduction,
        int powerIncrease) {
        super(aID, aName, aNameRegional, aTier);
        this.timeReduction = timeReduction;
        this.powerIncrease = powerIncrease;
    }

    public StaticOverclockController(String aName, int aTier, int timeReduction, int powerIncrease,
        String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.timeReduction = timeReduction;
        this.powerIncrease = powerIncrease;
    }

    protected final int timeReduction;
    protected final int powerIncrease;

    @Override
    public int getTimeReduction() {
        return timeReduction;
    }

    @Override
    public int getPowerIncrease() {
        return powerIncrease;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new StaticOverclockController(
            this.mName,
            this.mTier,
            this.timeReduction,
            this.powerIncrease,
            this.mDescriptionArray,
            this.mTextures);
    }
}
