package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.OverclockControllers;

import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class StaticOverclockController extends StaticOverclockControllerBase {

    public StaticOverclockController(int aID, String aName, String aNameRegional, int aTier, int timeReduction,
        int powerIncrease) {
        super(aID, aName, aNameRegional, aTier);
        this.overclockType = OverclockType.checkOverclockType(timeReduction, powerIncrease);
    }

    public StaticOverclockController(String aName, int aTier, OverclockType overclockType, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.overclockType = overclockType;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new StaticOverclockController(
            this.mName,
            this.mTier,
            this.overclockType,
            this.mDescriptionArray,
            this.mTextures);
    }

    protected final OverclockType overclockType;

    @Override
    public OverclockType getOverclockType() {
        return overclockType;
    }
}
