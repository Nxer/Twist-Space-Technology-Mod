package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class StaticParallelController extends StaticParallelControllerBase {

    public StaticParallelController(int aID, String aName, String aNameRegional, int aTier, int parallel) {
        super(aID, aName, aNameRegional, aTier);
        this.parallel = parallel;
    }

    public StaticParallelController(String aName, int aTier, int parallel, ITexture[][][] aTextures) {
        super(aName, aTier, aTextures);
        this.parallel = parallel;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new StaticParallelController(this.mName, this.mTier, this.parallel, this.mTextures);
    }

    protected final int parallel;

    @Override
    public int getParallel() {
        return parallel;
    }

}
