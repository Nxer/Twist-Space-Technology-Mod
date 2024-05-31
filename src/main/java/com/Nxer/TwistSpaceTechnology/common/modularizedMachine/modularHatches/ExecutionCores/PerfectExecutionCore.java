package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class PerfectExecutionCore extends ExecutionCoreBase implements IGlobalWirelessEnergy {

    public PerfectExecutionCore(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public PerfectExecutionCore(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new PerfectExecutionCore(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    private UUID ownerUUID;
    protected String costEU = "";

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public void resetParameters() {
        super.resetParameters();
        costEU = "";
    }

    @Override
    public boolean done() {
        BigInteger costEU = BigInteger.valueOf(eut)
            .multiply(BigInteger.valueOf(maxProgressingTime));
        // check wireless EU at this moment
        if (!addEUToGlobalEnergyMap(ownerUUID, costEU.multiply(Utils.NEGATIVE_ONE))) {
            shutDown();
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            TwistSpaceTechnology.LOG.info(
                "Advanced Execution Core shut down because of power at x" + mte
                    .getXCoord() + " y" + mte.getYCoord() + " z" + mte.getZCoord());

            return false;
        }

        this.costEU = GT_Utility.formatNumbers(costEU);
        maxProgressingTime = 20;

        return true;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("hasBeenSetup")) {
            int maxProgressingTime = tag.getInteger("maxProgressingTime");
            if (maxProgressingTime > 0) {
                // spotless:off
                currentTip.add(
                    // #tr Waila.ExecutionCore.1
                    // # Total basic max progressing time
                    // #zh_CN 配方总基础耗时
                    TextEnums.tr("Waila.ExecutionCore.1") + " : "
                        + maxProgressingTime + " tick ("
                        + (maxProgressingTime / 20) + "s)");
                int progressedTime = tag.getInteger("progressedTime");
                currentTip.add(
                    // #tr Waila.ExecutionCore.2
                    // # Progressed time
                    // #zh_CN 已执行时间
                    TextEnums.tr("Waila.ExecutionCore.2") + " : "
                        + progressedTime + " tick ("
                        + (progressedTime / 20) + "s)"
                );
                int boostedTime = tag.getInteger("boostedTime");
                currentTip.add(
                    // #tr Waila.ExecutionCore.4
                    // # Boosted time
                    // #zh_CN 已加速时间
                    TextEnums.tr("Waila.ExecutionCore.4") + " : "
                        + boostedTime + " tick ("
                        + (boostedTime / 20) + "s)"
                );
                String costEU = tag.getString("costEU");
                if (costEU != null && !costEU.isEmpty()) {
                    currentTip.add(
                        EnumChatFormatting.AQUA + TextEnums.tr("Waila.TST_MiracleDoor.1")
                            + EnumChatFormatting.RESET
                            + ": "
                            + EnumChatFormatting.GOLD
                            + costEU
                            + EnumChatFormatting.RESET
                            + " EU");
                }
                // spotless:on
            } else {
                // #tr Waila.ExecutionCore.IsIdle
                // # This execution core is idle.
                // #zh_CN 空闲状态
                currentTip.add(TextEnums.tr("Waila.ExecutionCore.IsIdle"));
            }
        } else {
            // #tr Waila.ExecutionCore.HasNotBeenSetup
            // # This execution core has not been setup.
            // #zh_CN 此执行核心未初始化
            currentTip.add(TextEnums.tr("Waila.ExecutionCore.HasNotBeenSetup"));
        }

    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setString("costEU", costEU);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("costEU", costEU);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        costEU = aNBT.getString("costEU");
    }

    @Override
    public boolean useMainMachinePower() {
        return false;
    }

}
