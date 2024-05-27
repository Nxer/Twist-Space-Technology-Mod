package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import java.math.BigInteger;
import java.util.UUID;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

// TODO Wireless EU costings
public class AdvExecutionCore extends ExecutionCore implements IGlobalWirelessEnergy {

    public AdvExecutionCore(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public AdvExecutionCore(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AdvExecutionCore(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    private UUID ownerUUID;

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public void done() {
        // check wireless EU at this moment
        if (!addEUToGlobalEnergyMap(
            ownerUUID,
            BigInteger.valueOf(eut)
                .multiply(
                    BigInteger.valueOf(maxProgressingTime)
                        .multiply(Utils.NEGATIVE_ONE)))) {
            shutDown();
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            TwistSpaceTechnology.LOG.info(
                "Advanced Execution Core shut down because of power at x" + mte
                    .getXCoord() + " y" + mte.getYCoord() + " z" + mte.getZCoord());
        }

    }

    @Override
    public boolean useMainMachinePower() {
        return false;
    }
}
