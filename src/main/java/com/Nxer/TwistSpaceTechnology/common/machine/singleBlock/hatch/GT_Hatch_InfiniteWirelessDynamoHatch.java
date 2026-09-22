package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS;

import java.util.Arrays;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit;
import com.Nxer.TwistSpaceTechnology.util.text.TextEnums;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessDynamoMulti;

@SkipGenerateDescription
public class GT_Hatch_InfiniteWirelessDynamoHatch extends MTEHatchWirelessDynamoMulti implements TSTTooltipCredit {

    // region Class Constructor
    public GT_Hatch_InfiniteWirelessDynamoHatch(String aName, byte aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 65536, aDescription, aTextures);
    }

    public GT_Hatch_InfiniteWirelessDynamoHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 65536);
        registerTooltipCredits(ID.NXER);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_InfiniteWirelessDynamoHatch(mName, mTier, new String[] { "" }, mTextures);
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
        String[] gtDescription = super.getDescription();
        String[] description = Arrays.copyOf(gtDescription, gtDescription.length + 1);
        // #tr Description.InfiniteWirelessDynamoHatch.1
        // # Infinite output voltage limit.
        // #zh_CN 无限输出电压限制.
        description[gtDescription.length] = EnumChatFormatting.WHITE
            + TextEnums.tr("Description.InfiniteWirelessDynamoHatch.1");
        return description;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, OVERLAYS_ENERGY_ON_WIRELESS[1] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, OVERLAYS_ENERGY_ON_WIRELESS[1] };
    }
}
