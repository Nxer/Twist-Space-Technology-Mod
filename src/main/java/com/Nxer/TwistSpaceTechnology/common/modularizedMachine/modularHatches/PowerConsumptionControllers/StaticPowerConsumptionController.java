package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.PowerConsumptionControllers;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

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

    // region General

    // spotless:off
    protected String[] description;

    @Override
    public String[] getDescription() {
        if (description == null || description.length == 0) {
            String value = (int) (getPowerConsumptionMultiplier() * 100) + "%";
            description =
                new String[] {
                    // #tr Tooltips.StaticPowerConsumptionController.01
                    // # Parallel controller module with a fixed parameter.
                    // #zh_CN 固定参数的耗能控制器模块.
                    TextEnums.tr("Tooltips.StaticPowerConsumptionController.01"),
                    // #tr Tooltips.StaticPowerConsumptionController.02
                    // # The actual power consumption is multiplied by
                    // #zh_CN 机器实际耗电量乘以
                    TextEnums.tr("Tooltips.StaticPowerConsumptionController.02") + value,
                    TextEnums.AddByTwistSpaceTechnology.getText(),
                    TextEnums.ModularizedMachineSystem.getText(),
                };
        }
        return description;
    }
    // spotless:on

}
