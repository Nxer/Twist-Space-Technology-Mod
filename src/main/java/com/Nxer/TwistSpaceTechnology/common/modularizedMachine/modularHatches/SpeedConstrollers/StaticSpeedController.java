package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@SkipGenerateDescription
public class StaticSpeedController extends StaticSpeedControllerBase {

    public StaticSpeedController(int aID, String aName, String aNameRegional, int aTier, int speedMultiplier) {
        super(aID, aName, aNameRegional, aTier);
        registerTooltipCredits(ID.NXER);
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
                    // #tr tst.modular.machine.StaticSpeedController.tooltip.info.01
                    // # Speed controller module with fixed parameters.
                    // #zh_CN 固定参数的速度控制器模块.
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tooltip.info.01"),
                    // #tr tst.modular.machine.StaticSpeedController.tooltip.info.02
                    // # Provides speed x
                    // #zh_CN 提供速度增幅
                    TSTUtils.tr("tst.modular.machine.StaticSpeedController.tooltip.info.02") + " " + getSpeedMultiplier() + "00%",
                };
        }
        return description;
    }
    // spotless:on

}
