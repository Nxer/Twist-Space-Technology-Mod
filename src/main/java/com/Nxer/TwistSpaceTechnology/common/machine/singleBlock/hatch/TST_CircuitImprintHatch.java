package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DATA_ACCESS;
import static gregtech.api.util.GTUtility.areStacksEqual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.enums.Mods;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class TST_CircuitImprintHatch extends MTEHatch implements IAddUIWidgets {

    private int timeout = 4;
    public HashSet<NBTTagCompound> circuitType = new HashSet<>();

    public TST_CircuitImprintHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            16,
            new String[] { "Data Access for Multiblocks",
                "Adds " + (aTier == 4 ? 4 : 16) + " extra slots for Data Sticks" });
    }

    public TST_CircuitImprintHatch(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier == 4 ? 4 : 16, aDescription, aTextures);
    }

    public TST_CircuitImprintHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier == 4 ? 4 : 16, aDescription, aTextures);
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
    public boolean isSimpleMachine() {
        return true;
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
        // if (aNBT.getByte("mCircuitUpdated") != 1) {
        // for (int i = 0; i < getSizeInventory(); i++) {
        // processCircuitImprint(getStackInSlot(i));
        // }
        // }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        // aNBT.setByte("mCircuitUpdated", (byte) 1);
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        super.setInventorySlotContents(aIndex, aStack);
        // processCircuitImprint(aStack);
    }

    public List<ItemStack> getStoredCircuitImprints() {
        return getInventoryItems(
            stack -> stack != null
                && stack.getItem() == GTModHandler.getModItem(Mods.BartWorks.ID, "gt.bwMetaGeneratedItem0", 1)
                    .getItem());
    }

    public void processCircuitImprint(ItemStack aStack) {
        if (!GTUtility.isStackValid(aStack)) return;
        if (areStacksEqual(aStack, GTModHandler.getModItem(Mods.BartWorks.ID, "gt.bwMetaGeneratedItem0", 1))) {
            circuitType.add(aStack.stackTagCompound);
        }

    }

    public List<ItemStack> getInventoryItems(Predicate<ItemStack> filter) {
        ArrayList<ItemStack> items = new ArrayList<>();
        IGregTechTileEntity te = getBaseMetaTileEntity();
        for (int i = 0; i < te.getSizeInventory(); ++i) {
            ItemStack slot = te.getStackInSlot(i);
            if (slot != null) {
                if (filter != null && filter.test(slot)) {
                    items.add(slot);
                }
            }
        }
        return items;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (mTier == 4) {
            getBaseMetaTileEntity()
                .add2by2Slots(builder, getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_CIRCUIT);
        } else {
            getBaseMetaTileEntity()
                .add4by4Slots(builder, getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_CIRCUIT);
        }
    }
}
