package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.authorName_Nxer;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEWirelessDynamo;

public class GT_Hatch_InfiniteWirelessDynamoHatch extends MTEWirelessDynamo {

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
    private static final long LongMaxDivide4 = Long.MAX_VALUE / 4;
    private static final long LongMaxDecreaseInt = Long.MAX_VALUE - Integer.MAX_VALUE;

    @Override
    public long getMinimumStoredEU() {
        return 512;
    }

    @Override
    public long maxEUOutput() {
        return LongMaxDivide4;
    }

    @Override
    public long maxEUStore() {
        return LongMaxDecreaseInt;
    }

    @Override
    public long maxAmperesOut() {
        return 1;
    }

    // endregion

    // region General
    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Stores energy globally in a network, up to 2^(2^31) EU.",
            EnumChatFormatting.GRAY + "Does not connect to wires. This block accepts EU into the network.",
            // #tr Description.InfiniteWirelessDynamoHatch.1
            // # Infinite output voltage limit.
            // #zh_CN 无限输出电压限制.
            EnumChatFormatting.WHITE + TextEnums.tr("Description.InfiniteWirelessDynamoHatch.1"), ModNameDesc,
            authorName_Nxer };
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
