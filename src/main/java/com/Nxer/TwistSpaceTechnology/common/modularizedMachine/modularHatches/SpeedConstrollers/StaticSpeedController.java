package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

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

    // region General

    // spotless:off
    protected String[] description;

    @Override
    public String[] getDescription() {
        if (description == null || description.length == 0) {
            description =
                new String[] {
                    // #tr Tooltips.StaticSpeedController.01
                    // # Speed controller module with fixed parameters.
                    // #zh_CN 固定参数的速度控制器模块.
                    TextEnums.tr("Tooltips.StaticSpeedController.01"),
                    // #tr Tooltips.StaticSpeedController.02
                    // # Provides speed x
                    // #zh_CN 提供速度增幅
                    TextEnums.tr("Tooltips.StaticSpeedController.02") + " " + getSpeedMultiplier() + "00%",
                    TextEnums.AddByTwistSpaceTechnology.getText(),
                    TextEnums.ModularizedMachineSystem.getText(),
                };
        }
        return description;
    }
    // spotless:on

}
