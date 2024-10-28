package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Mark_TwistSpaceTechnology_TecTech;

import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.system.WirelessDataNetWork.WirelessDataPacket;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;

public class GT_Hatch_WirelessData_output extends MTEHatchDataOutput {

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

    private static String[] tooltips;

    @Override
    public String[] getDescription() {
        if (tooltips == null) {
            tooltips = new String[] { Mark_TwistSpaceTechnology_TecTech,
                // #tr WirelessDataOutput.tooltips.01
                // # Wireless Quantum Data Output for Multiblocks
                // #zh_CN 多方块机器无线数据输出
                TextEnums.tr("WirelessDataOutput.tooltips.01") };
        }
        return tooltips;
    }

}
