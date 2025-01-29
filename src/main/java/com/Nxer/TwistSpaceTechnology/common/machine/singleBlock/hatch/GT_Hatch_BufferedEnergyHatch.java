package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static gregtech.api.enums.GTValues.V;

import java.util.List;

import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import ic2.api.item.IElectricItem;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_Hatch_BufferedEnergyHatch extends MTEHatchEnergy {

    public boolean mCharge = false, mDecharge = false;
    public int mBatteryCount = 0, mChargeableCount = 0;
    private long count = 0;
    private long mStored = 0;
    private long mMax = 0;

    public GT_Hatch_BufferedEnergyHatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            aInvSlotCount,
            new String[] { TstUtils.tr("BufferedEnergyHatch.Tooltips.01"), TstUtils.tr("BufferedEnergyHatch.Tooltips.02"),
                TextLocalization.ModNameDesc },
            aTextures);
    }

    public GT_Hatch_BufferedEnergyHatch(String aName, int aTier, int aslot, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aslot, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[mDescriptionArray.length + 1];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        // #tr Slots
        // #en_US Slots
        // #zh_CN 格
        desc[mDescriptionArray.length] = mInventory.length + " " + TstUtils.tr("Slots");
        return desc;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 16L * mInventory.length;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 64L * mInventory.length;
    }

    @Override
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesOut() {
        return 2;
    }

    @Override
    public int rechargerSlotCount() {
        return mCharge ? mInventory.length : 0;
    }

    @Override
    public int dechargerSlotCount() {
        return mDecharge ? mInventory.length : 0;
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            mCharge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3;
            mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3;
            mBatteryCount = 0;
            mChargeableCount = 0;
            for (ItemStack tStack : mInventory) if (GTModHandler.isElectricItem(tStack, mTier)) {
                if (GTModHandler.isChargerItem(tStack)) mBatteryCount++;
                mChargeableCount++;
            }
        }
        count++;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GTModHandler.isElectricItem(aStack) && aStack.getUnlocalizedName()
            .startsWith("gt.metaitem.01.")) {
            String name = aStack.getUnlocalizedName();
            if (name.equals("gt.metaitem.01.32510") || name.equals("gt.metaitem.01.32511")
                || name.equals("gt.metaitem.01.32520")
                || name.equals("gt.metaitem.01.32521")
                || name.equals("gt.metaitem.01.32530")
                || name.equals("gt.metaitem.01.32531")) {
                return ic2.api.item.ElectricItem.manager.getCharge(aStack) == 0;
            }
        }
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!GTUtility.isStackValid(aStack)) {
            return false;
        }
        return mInventory[aIndex] == null && GTModHandler.isElectricItem(aStack, this.mTier);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public long[] getStoredEnergy() {
        boolean scaleOverflow = false;
        boolean storedOverflow = false;
        long tScale = getBaseMetaTileEntity().getEUCapacity();
        long tStored = getBaseMetaTileEntity().getStoredEU();
        long tStep = 0;
        if (mInventory != null) {
            for (ItemStack aStack : mInventory) {
                if (GTModHandler.isElectricItem(aStack)) {

                    if (aStack.getItem() instanceof MetaBaseItem) {
                        Long[] stats = ((MetaBaseItem) aStack.getItem()).getElectricStats(aStack);
                        if (stats != null) {
                            if (stats[0] > Long.MAX_VALUE / 2) {
                                scaleOverflow = true;
                            }
                            tScale = tScale + stats[0];
                            tStep = ((MetaBaseItem) aStack.getItem()).getRealCharge(aStack);
                            if (tStep > Long.MAX_VALUE / 2) {
                                storedOverflow = true;
                            }
                            tStored = tStored + tStep;
                        }
                    } else if (aStack.getItem() instanceof IElectricItem) {
                        tStored = tStored + (long) ic2.api.item.ElectricItem.manager.getCharge(aStack);
                        tScale = tScale + (long) ((IElectricItem) aStack.getItem()).getMaxCharge(aStack);
                    }
                }
            }
        }
        if (scaleOverflow) {
            tScale = Long.MAX_VALUE;
        }
        if (storedOverflow) {
            tStored = Long.MAX_VALUE;
        }
        return new long[] { tStored, tScale };
    }

    @Override
    public String[] getInfoData() {
        updateStorageInfo();

        return new String[] { EnumChatFormatting.BLUE + getLocalName() + EnumChatFormatting.RESET, "Stored Items:",
            EnumChatFormatting.GREEN + GTUtility.formatNumbers(mStored)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMax)
                + EnumChatFormatting.RESET
                + " EU",
            "Average input:", GTUtility.formatNumbers(getBaseMetaTileEntity().getAverageElectricInput()) + " EU/t",
            "Average output:", GTUtility.formatNumbers(getBaseMetaTileEntity().getAverageElectricOutput()) + " EU/t" };
    }

    private void updateStorageInfo() {
        if (mMax == 0 || (count > 20)) {
            long[] tmp = getStoredEnergy();
            mStored = tmp[0];
            mMax = tmp[1];
            count = 0;
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.energy.stored",
                GTUtility.formatNumbers(tag.getLong("mStored")),
                GTUtility.formatNumbers(tag.getLong("mMax"))));
        long avgIn = tag.getLong("AvgIn");
        long avgOut = tag.getLong("AvgOut");
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.energy.avg_in_with_amperage",
                GTUtility.formatNumbers(avgIn),
                GTUtility.getAmperageForTier(avgIn, (byte) getInputTier()),
                GTUtility.getColoredTierNameFromTier((byte) getInputTier())));
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.energy.avg_out_with_amperage",
                GTUtility.formatNumbers(avgOut),
                GTUtility.getAmperageForTier(avgOut, (byte) getOutputTier()),
                GTUtility.getColoredTierNameFromTier((byte) getOutputTier())));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        updateStorageInfo();
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("mStored", mStored);
        tag.setLong("mMax", mMax);
        tag.setLong("AvgIn", getBaseMetaTileEntity().getAverageElectricInput());
        tag.setLong("AvgOut", getBaseMetaTileEntity().getAverageElectricOutput());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        switch (mInventory.length) {
            case 4 -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 2)
                    .startFromSlot(0)
                    .endAtSlot(3)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(70, 25));
            case 9 -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 3)
                    .startFromSlot(0)
                    .endAtSlot(8)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(61, 16));
            case 16 -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 4)
                    .startFromSlot(0)
                    .endAtSlot(15)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(52, 7));
            default -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 1)
                    .startFromSlot(0)
                    .endAtSlot(0)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(79, 34));
        }
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_BufferedEnergyHatch(mName, mTier, mInventory.length, mDescriptionArray, mTextures);
    }

}
