package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import appeng.api.networking.GridFlags;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;

import javax.annotation.Nullable;

public class TST_WirelessOutputBusME extends GT_MetaTileEntity_Hatch_OutputBus_ME {

    // region Class Constructor
    public TST_WirelessOutputBusME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_WirelessOutputBusME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_WirelessOutputBusME(mName, mTier, mDescriptionArray, mTextures);
    }

    // endregion

    // region Logic

    // x,y,z
    protected int[] connectorLocation = new int[3];
    protected @Nullable AENetworkProxy gridProxy = null;

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                    (IGridProxyable) getBaseMetaTileEntity(),
                    "proxy",
                    ItemList.Hatch_Output_Bus_ME.get(1),
                    true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                updateValidGridProxySides();
                if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                    getBaseMetaTileEntity().getWorld()
                                           .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }
        }
        return this.gridProxy;
    }

    // endregion

}
