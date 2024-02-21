package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import com.github.technus.tectech.mechanics.pipe.PipeActivityMessage;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import com.github.technus.tectech.util.CommonValues;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.common.GT_Client;

public class GT_MetaTileEntity_Pipe_EnergySmart extends GT_MetaTileEntity_Pipe_Energy {

    public GT_MetaTileEntity_Pipe_EnergySmart(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Pipe_EnergySmart(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_EnergySmart(mName);
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM,
            texter("Reflect after precise calculation.", "LaserSmartPipe.getDescription.01"),
            texter(
                EnumChatFormatting.AQUA + "Connects "
                    + EnumChatFormatting.BOLD
                    + "TWO"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.AQUA
                    + " devices in different directions.",
                "LaserSmartPipe.getDescription.02"),
            texter(EnumChatFormatting.AQUA + "Cannot connect to the third device.", "LaserSmartPipe.getDescription.03"),
            texter(
                EnumChatFormatting.AQUA + "Cannot connect to the other Smart Nodes.",
                "LaserSmartPipe.getDescription.04"),
            TextLocalization.ModNameDesc

        };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick & 31) == 31) {
                if (TecTech.RANDOM.nextInt(15) == 0) {
                    NetworkDispatcher.INSTANCE.sendToAllAround(
                        new PipeActivityMessage.PipeActivityData(this),
                        aBaseMetaTileEntity.getWorld().provider.dimensionId,
                        aBaseMetaTileEntity.getXCoord(),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getZCoord(),
                        256);
                }
                if (getActive()) {
                    setActive(false);
                }
                mConnections = 0;
                connectionCount = 0;
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    final ForgeDirection oppositeSide = side.getOpposite();
                    TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
                    if (tTileEntity instanceof IColoredTileEntity) {
                        byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                        if (tColor != aBaseMetaTileEntity.getColorization()) {
                            continue;
                        }
                        // }
                    }
                    if (tTileEntity instanceof PowerLogicHost) {
                        PowerLogic logic = ((PowerLogicHost) tTileEntity).getPowerLogic(oppositeSide);
                        if (logic.canUseLaser() && connectionCount < 2) {
                            mConnections |= 1 << side.ordinal();
                            connectionCount++;
                            continue;
                        }
                    }
                    if (tTileEntity instanceof IConnectsToEnergyTunnel
                        && ((IConnectsToEnergyTunnel) tTileEntity).canConnect(oppositeSide)
                        && connectionCount < 2) {
                        mConnections |= 1 << side.ordinal();
                        connectionCount++;
                    } else if (tTileEntity instanceof IGregTechTileEntity gtTE
                        && gtTE.getMetaTileEntity() instanceof IConnectsToEnergyTunnel CtET
                        && !(CtET instanceof GT_MetaTileEntity_Pipe_EnergySmart)) {
                            if (CtET.canConnect(oppositeSide) && connectionCount < 2) {
                                mConnections |= 1 << side.ordinal();
                                connectionCount++;
                            }
                        }
                }
            }

        } else if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }
}
