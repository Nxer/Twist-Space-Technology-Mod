package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextEnums.AddByTwistSpaceTechnology;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.Author_Goderium;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_WirelessMulti;
import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT_Hatch_InfiniteWirelessMulti extends GT_MetaTileEntity_Hatch_WirelessMulti {

    private String owner_uuid;
    private String owner_name;
    public static final long[] Vst = new long[] { 8L, 32L, 128L, 512L, 2048L, 8192L, 32_768L, 131_072L, 524_288L,
        2_097_152L, 8_388_608L, 33_554_432L, 134_217_728L, 536_870_912L, 2_147_483_647L };
    long eu_transferred_per_tick = Vst[mTier] * Amperes;

    long ticks_between_energy_addition = Math.max(Long.MAX_VALUE / eu_transferred_per_tick, 2);

    public GT_Hatch_InfiniteWirelessMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier, aAmp);
        TT_Utility.setTier(aTier, this);
    }

    public GT_Hatch_InfiniteWirelessMulti(String aName, int aTier, int aAmp, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_InfiniteWirelessMulti(mName, mTier, Amperes, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            // #tr ToolTip_InfiniteWirelessMulti.1
            // # {\GRAY}Stores energy globally in a network, up to 2^(2^31) EU.
            // #zh_CN {\GRAY}将能量存储于全局网络中，上限为2^(2^31)EU.
            TextEnums.tr("ToolTip_InfiniteWirelessMulti.1"),
            // #tr ToolTip_InfiniteWirelessMulti.2
            // # {\GRAY}Does not connect to wires. This block withdraws EU from the network.
            // #zh_CN {\GRAY}不连接导线，此方块可以从网络中抽取EU.
            TextEnums.tr("ToolTip_InfiniteWirelessMulti.2"),
            // #tr ToolTip_InfiniteWirelessMulti.3
            // # {\GRAY}Be careful of energy overflow.
            // #zh_CN {\GRAY}小心能量溢出.
            TextEnums.tr("ToolTip_InfiniteWirelessMulti.3"), Author_Goderium.getText(),
            AddByTwistSpaceTechnology.getText(), GRAY + "Ampere IN: " + GREEN + String.format("%,d", Amperes) };
    }

    @Override
    public long maxEUStore() {
        return Long.MAX_VALUE;
    }

    @Override
    public long maxEUInput() {
        return Vst[mTier];
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // On first tick find the player name and attempt to add them to the map.

            // UUID and username of the owner.
            owner_uuid = aBaseMetaTileEntity.getOwnerUuid()
                .toString();
            owner_name = aBaseMetaTileEntity.getOwnerName();

            strongCheckOrAddUser(owner_uuid, owner_name);

            tryFetchingEnergy();
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            // Every ticks_between_energy_addition add eu_transferred_per_operation to internal EU storage from network.
            if (aTick % ticks_between_energy_addition == 1L) {
                tryFetchingEnergy();
            }
        }
    }

    private void tryFetchingEnergy() {
        long currentEU = getBaseMetaTileEntity().getStoredEU();
        long maxEU = maxEUStore();
        long euToTransfer = maxEU - currentEU;
        if (euToTransfer <= 0) return; // nothing to transfer
        if (!addEUToGlobalEnergyMap(owner_uuid, -euToTransfer)) return;
        setEUVar(maxEU);
    }
}
