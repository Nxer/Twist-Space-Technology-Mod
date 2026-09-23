package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@SkipGenerateDescription
public class StaticParallelController extends StaticParallelControllerBase {

    public StaticParallelController(int aID, String aName, String aNameRegional, int aTier, int parallel) {
        super(aID, aName, aNameRegional, aTier);
        registerTooltipCredits(ID.NXER);
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

    // region General

    // spotless:off
    protected String[] description;

    @Override
    public String[] getDescription() {
        if (description == null || description.length == 0) {
            description =
                new String[] {
                    // #tr Tooltips.StaticParallelController.01
                    // # Parallel controller module with a fixed parameter.
                    // #zh_CN 固定参数的并行控制器模块.
                    TSTUtils.tr("Tooltips.StaticParallelController.01"),
                    // #tr Tooltips.StaticParallelController.02
                    // # Provides
                    // #zh_CN 提供
                    TSTUtils.tr("Tooltips.StaticParallelController.02") + " " + getParallel() + " " + TSTSharedLocalization.General.Word_Parallel + ".",
                };
        }
        return description;
    }
    // spotless:on

}
