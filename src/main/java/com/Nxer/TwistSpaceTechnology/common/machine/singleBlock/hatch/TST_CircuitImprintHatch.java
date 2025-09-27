package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DATA_ACCESS;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import bartworks.system.material.CircuitGeneration.BWMetaItems;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public class TST_CircuitImprintHatch extends MTEHatch implements IAddUIWidgets {

    private int timeout = 4;
    public HashSet<NBTTagCompound> circuitType = new HashSet<>();

    public TST_CircuitImprintHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            aTier > 5 ? 16 : 4,
            // spotless:off
            // #tr Tooltips.CircuitImprintHatch.01
            // # Extra imprint circuit input for TST Advanced Circuit Assembly Line
            // #zh_CN TST进阶电路装配线的额外压印电路输入
            // #tr Tooltips.CircuitImprintHatch.02
            // # Adds
            // #zh_CN 为压印电路增加
            // #tr Tooltips.CircuitImprintHatch.03
            // # extra slots for imprint circuits
            // #zh_CN 个额外插槽
            // spotless:on
            new String[] { TextEnums.tr("Tooltips.CircuitImprintHatch.01"),
                TextEnums.tr("Tooltips.CircuitImprintHatch.02") + " " + (aTier > 5 ? 16 : 4) + " "
                    + TextEnums.tr("Tooltips.CircuitImprintHatch.03"),
                ModNameDesc });
    }

    public TST_CircuitImprintHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier > 5 ? 16 : 4, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_CircuitImprintHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return mTier >= 8 && !aBaseMetaTileEntity.isActive();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return mTier >= 8 && !aBaseMetaTileEntity.isActive();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isActive()) {
            timeout--;
            if (timeout <= 0) {
                aBaseMetaTileEntity.setActive(false);
            }
        }
    }

    public void setActive(boolean mActive) {
        getBaseMetaTileEntity().setActive(mActive);
        timeout = mActive ? 4 : 0;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.getByte("mCircuitUpdated") != 1) {
            refreshImprint();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mCircuitUpdated", (byte) 1);
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        super.setInventorySlotContents(aIndex, aStack);
        refreshImprint();
    }

    public HashSet<NBTTagCompound> getStoredCircuitImprints() {
        return circuitType;
    }

    public void refreshImprint() {
        circuitType = new HashSet<>();
        IGregTechTileEntity te = getBaseMetaTileEntity();
        for (int i = 0; i < te.getSizeInventory(); ++i) {
            ItemStack slot = te.getStackInSlot(i);
            if (slot != null && slot.isItemEqual(
                BWMetaItems.getCircuitParts()
                    .getStack(0, 0))) {
                circuitType.add(slot.stackTagCompound);
            }
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (mTier > 5) {
            getBaseMetaTileEntity()
                .add4by4Slots(builder, getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_CIRCUIT);
        } else {
            getBaseMetaTileEntity()
                .add2by2Slots(builder, getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_CIRCUIT);
        }
    }
}
