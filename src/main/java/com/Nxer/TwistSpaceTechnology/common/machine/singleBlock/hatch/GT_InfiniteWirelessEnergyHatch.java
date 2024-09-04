package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import com.github.technus.tectech.util.TT_Utility;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Wireless_Hatch;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BLUE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static java.lang.Long.min;

public class GT_InfiniteWirelessEnergyHatch extends GT_MetaTileEntity_Wireless_Hatch implements IGlobalWirelessEnergy, IWirelessEnergyHatchInformation {

    public GT_InfiniteWirelessEnergyHatch(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }


    private String owner_uuid;
    private String owner_name;
    long ticks_between_energy_addition = 100L * 20L;

    @Override
    public long maxEUStore() {
        return Long.MAX_VALUE;
    }

    @Override
    public String[] getDescription() {
        return new String[] {
"aDescription" };
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
            if (aTick % ticks_between_energy_addition == 0L) {
                tryFetchingEnergy();
            }
        }
    }

    private void tryFetchingEnergy() {
        long currentEU = getBaseMetaTileEntity().getStoredEU();
        long maxEU = maxEUStore();
        long euToTransfer = min(maxEU - currentEU, eu_transferred_per_operation_long);
        if (euToTransfer <= 0) return; // nothing to transfer
        if (!addEUToGlobalEnergyMap(owner_uuid, -euToTransfer)) return;
        setEUVar(currentEU + euToTransfer);
    }
}
