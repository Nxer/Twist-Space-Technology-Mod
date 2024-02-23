package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.authorName_Nxer;

import java.util.UUID;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Wireless_Dynamo;

public class GT_Hatch_InfiniteWirelessDynamoHatch extends GT_MetaTileEntity_Wireless_Dynamo {

    // region Class Constructor
    public GT_Hatch_InfiniteWirelessDynamoHatch(String aName, byte aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_Hatch_InfiniteWirelessDynamoHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_InfiniteWirelessDynamoHatch(mName, (byte) 14, new String[] { "" }, mTextures);
    }

    // endregion

    // region IO info
    private String owner_uuid;
    private String owner_name;

    @Override
    public long getMinimumStoredEU() {
        return 512;
    }

    @Override
    public long maxEUOutput() {
        return Long.MAX_VALUE;
    }

    @Override
    public long maxEUStore() {
        return Long.MAX_VALUE;
    }

    @Override
    public long maxAmperesOut() {
        return 1;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            // On first tick find the player name and attempt to add them to the map.
            // UUID and username of the owner.
            this.owner_uuid = aBaseMetaTileEntity.getOwnerUuid()
                .toString();
            owner_name = aBaseMetaTileEntity.getOwnerName();

            strongCheckOrAddUser(owner_uuid, owner_name);
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            // Every ticks_between_energy_addition ticks change the energy content of the machine.
            if (aTick % ticks_between_energy_addition == 0L) {
                addEUToGlobalEnergyMap(owner_uuid, getEUVar());
                setEUVar(0L);
            }
        }
    }

    // endregion

    // region General
    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Stores energy globally in a network, up to 2^(2^31) EU.",
            EnumChatFormatting.GRAY + "Does not connect to wires. This block accepts EU into the network.",
            EnumChatFormatting.WHITE
                + texter("Infinite output voltage limit.", "Description.InfiniteWirelessDynamoHatch.1"),
            ModNameDesc, authorName_Nxer };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[1] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[1] };
    }
}
