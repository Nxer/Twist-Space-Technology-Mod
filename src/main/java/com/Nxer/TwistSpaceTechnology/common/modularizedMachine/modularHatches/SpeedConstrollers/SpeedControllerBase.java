package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ModularHatchBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class SpeedControllerBase extends ModularHatchBase {

    public SpeedControllerBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, null);
    }

    public SpeedControllerBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ModularHatchTypes getType() {
        return ModularHatchTypes.SPEED_CONTROLLER;
    }

    public float getSpeedBonus() {
        return 1F / getSpeedMultiplier();
    }

    public abstract int getSpeedMultiplier();

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            // #tr Waila.SpeedControllerBase.1
            // # Speed Multiplier
            // #zh_CN 速度倍率
            EnumChatFormatting.AQUA + TextEnums.tr("Waila.SpeedControllerBase.1")
                + EnumChatFormatting.RESET
                + ": "
                + EnumChatFormatting.GOLD
                + tag.getInteger("speedMultiplier"));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setInteger("speedMultiplier", getSpeedMultiplier());
        }
    }

}
