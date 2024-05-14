package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class StaticSpeedController extends StaticSpeedControllerBase {

    public StaticSpeedController(int aID, String aName, String aNameRegional, int aTier, int speedMultiplier) {
        super(aID, aName, aNameRegional, aTier);
        this.speedMultiplier = speedMultiplier;
    }

    public StaticSpeedController(String aName, int aTier, int speedMultiplier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new StaticSpeedController(
            this.mName,
            this.mTier,
            this.speedMultiplier,
            this.mDescriptionArray,
            this.mTextures);
    }

    protected final int speedMultiplier;

    @Override
    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

}
