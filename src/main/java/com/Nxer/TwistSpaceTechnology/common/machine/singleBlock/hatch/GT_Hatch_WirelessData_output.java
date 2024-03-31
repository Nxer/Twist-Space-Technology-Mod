package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.system.WirelessDataNetWork.WirelessDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT_Hatch_WirelessData_output extends GT_MetaTileEntity_Hatch_OutputData {

    public GT_Hatch_WirelessData_output(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);

    }

    public GT_Hatch_WirelessData_output(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);

    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_WirelessData_output(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return false;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        return;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && q != null) {
            WirelessDataPacket.uploadData(aBaseMetaTileEntity.getOwnerName(), q.getContent());
            q = null;
        }
    }

}
