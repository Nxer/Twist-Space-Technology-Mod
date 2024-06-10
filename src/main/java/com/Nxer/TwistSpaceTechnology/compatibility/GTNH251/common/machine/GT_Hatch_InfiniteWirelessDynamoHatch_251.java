package com.Nxer.TwistSpaceTechnology.compatibility.GTNH251.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_InfiniteWirelessDynamoHatch;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNH251.Reflector;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT_Hatch_InfiniteWirelessDynamoHatch_251 extends GT_Hatch_InfiniteWirelessDynamoHatch {

    private GT_Hatch_InfiniteWirelessDynamoHatch_251(String aName, byte aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_Hatch_InfiniteWirelessDynamoHatch_251(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_InfiniteWirelessDynamoHatch_251(mName, (byte) 14, new String[] { "" }, mTextures);
    }

    private String owner_uuid;
    private String owner_name;

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            // On first tick find the player name and attempt to add them to the map.
            // UUID and username of the owner.
            this.owner_uuid = aBaseMetaTileEntity.getOwnerUuid()
                .toString();
            owner_name = aBaseMetaTileEntity.getOwnerName();

            Reflector.strongCheckOrAddUser(this, owner_uuid, owner_name);
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            // Every ticks_between_energy_addition ticks change the energy content of the machine.
            if (aTick % ticks_between_energy_addition == 0L) {
                Reflector.addEUToGlobalEnergyMap(this, owner_uuid, getEUVar());
                setEUVar(0L);
            }
        }
    }
}
