package com.Nxer.TwistSpaceTechnology.compatibility.GTNH251.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_ThermalEnergyDevourer;

import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class TST_ThermalEnergyDevourer_251 extends TST_ThermalEnergyDevourer implements IGlobalWirelessEnergy {

    public TST_ThermalEnergyDevourer_251(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private TST_ThermalEnergyDevourer_251(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ThermalEnergyDevourer_251(this.mName);
    }
}
