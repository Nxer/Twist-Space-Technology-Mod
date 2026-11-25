package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

// TODO: Remove this transitional controller and its conversion recipes in the next version.
@IMetaTileEntity.SkipGenerateDescription
public class TST_StarThatNeverWasLegacy extends TST_StarThatNeverWas {

    public TST_StarThatNeverWasLegacy(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public TST_StarThatNeverWasLegacy(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new TST_StarThatNeverWasLegacy(mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder()
            .addDeprecatedLine(TSTSharedLocalization.MachineTooltip.temporaryController())
            .addInfo(TSTSharedLocalization.MachineTooltip.replacementController())
            .toolTipFinisher();
    }
}
