package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.PowerConsumptionControllers;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class StaticPowerConsumptionController extends StaticPowerConsumptionControllerBase {

    public StaticPowerConsumptionController(int aID, String aName, String aNameRegional, int aTier,
        float powerConsumptionMultiplier) {
        super(aID, aName, aNameRegional, aTier);
        this.powerConsumptionMultiplier = powerConsumptionMultiplier;
    }

    public StaticPowerConsumptionController(String aName, int aTier, float powerConsumptionMultiplier,
        String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.powerConsumptionMultiplier = powerConsumptionMultiplier;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new StaticPowerConsumptionController(
            this.mName,
            this.mTier,
            this.powerConsumptionMultiplier,
            this.mDescriptionArray,
            this.mTextures);
    }

    protected final float powerConsumptionMultiplier;

    @Override
    public float getPowerConsumptionMultiplier() {
        return powerConsumptionMultiplier;
    }

}
