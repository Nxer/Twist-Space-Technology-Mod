package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

// TODO: Remove this transitional controller and its conversion recipes in the next version.
@IMetaTileEntity.SkipGenerateDescription
public class GTCM_ElvenWorkshopLegacy extends GTCM_ElvenWorkshop {

    public GTCM_ElvenWorkshopLegacy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_ElvenWorkshopLegacy(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_ElvenWorkshopLegacy(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder()
            .addDeprecatedLine(TSTSharedLocalization.MachineTooltip.temporaryController())
            .addInfo(TSTSharedLocalization.MachineTooltip.replacementController())
            .toolTipFinisher();
    }
}
