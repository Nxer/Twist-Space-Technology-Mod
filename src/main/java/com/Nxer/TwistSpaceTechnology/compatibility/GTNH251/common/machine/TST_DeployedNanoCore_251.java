package com.Nxer.TwistSpaceTechnology.compatibility.GTNH251.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_DeployedNanoCore;

import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class TST_DeployedNanoCore_251 extends TST_DeployedNanoCore implements IGlobalWirelessEnergy {

    public TST_DeployedNanoCore_251(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected TST_DeployedNanoCore_251(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_DeployedNanoCore_251(this.mName);
    }
}
