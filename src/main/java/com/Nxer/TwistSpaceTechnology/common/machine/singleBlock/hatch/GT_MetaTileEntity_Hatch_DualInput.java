package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.AutoSeparation;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.FluidCapacity;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUFFER;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;

public class GT_MetaTileEntity_Hatch_DualInput extends MTEHatchInputBus implements IAddUIWidgets, IDualInputHatch {

    private final FluidStack[] mStoredFluid;
    private final FluidStackTank[] fluidTanks;
    public final int mCapacityPer;
    private static final int ITEM_SLOT_AMOUNT = 19;
    private static final int CATALYST_SLOT_1 = 16;
    private static final int CATALYST_SLOT_2 = 17;

    public static class Inventory implements IDualInputInventory {

        private final ItemStack[] itemInventory;
        private final FluidStack[] fluidInventory;

        public Inventory(ItemStack[] items, FluidStack[] fluid) {
            itemInventory = items;
            fluidInventory = fluid;
        }

        @Override
        public ItemStack[] getItemInputs() {
            if (isEmpty()) return new ItemStack[0];
            return Arrays.stream(itemInventory)
                .filter(Objects::nonNull)
                .toArray(ItemStack[]::new);
        }

        @Override
        public FluidStack[] getFluidInputs() {
            if (isEmpty()) return new FluidStack[0];
            return Arrays.stream(fluidInventory)
                .filter(Objects::nonNull)
                .toArray(FluidStack[]::new);
        }

        public boolean isEmpty() {
            if (itemInventory != null) {
                // Circuit slot is the last slot; catalyst slots are the second & third to last slots. Will not check
                // them
                for (int i = 0; i < itemInventory.length - 3; i++) {
                    if (itemInventory[i] != null && itemInventory[i].stackSize > 0) {
                        return false;
                    }
                }
            }
            if (fluidInventory != null) {
                for (FluidStack fluid : fluidInventory) {
                    if (fluid != null && fluid.amount > 0) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    Inventory inventory;

    public GT_MetaTileEntity_Hatch_DualInput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            ITEM_SLOT_AMOUNT,
            // #tr ToolTip_DualInputHatch_1
            // # Advanced input for Multiblocks
            // #zh_CN 多方块的进阶输入
            // #tr ToolTip_DualInputHatch_2
            // # Can hold
            // #zh_CN 能容纳
            // #tr ToolTip_DualInputHatch_3
            // # types of item and
            // #zh_CN 种物品与
            // #tr ToolTip_DualInputHatch_4
            // # types of fluid
            // #zh_CN 种流体
            new String[] { TextEnums.tr("ToolTip_DualInputHatch_1"),
                FluidCapacity + " " + getCapacityPerTank(aTier) + " L",
                TextEnums.tr("ToolTip_DualInputHatch_2") + " "
                    + getSlots(aTier)
                    + " "
                    + TextEnums.tr("ToolTip_DualInputHatch_3")
                    + " "
                    + getFluidSlotsAmount(aTier)
                    + " "
                    + TextEnums.tr("ToolTip_DualInputHatch_4"),
                AutoSeparation, ModNameDesc });
        mStoredFluid = new FluidStack[getFluidSlotsAmount(aTier)];
        fluidTanks = new FluidStackTank[getFluidSlotsAmount(aTier)];
        mCapacityPer = getCapacityPerTank(aTier);
        inventory = new Inventory(mInventory, mStoredFluid);
    }

    public GT_MetaTileEntity_Hatch_DualInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, ITEM_SLOT_AMOUNT, aDescription, aTextures);
        mStoredFluid = new FluidStack[getFluidSlotsAmount(aTier)];
        fluidTanks = new FluidStackTank[getFluidSlotsAmount(aTier)];
        mCapacityPer = getCapacityPerTank(aTier);
        for (int i = 0; i < getFluidSlotsAmount(aTier); i++) {
            final int index = i;
            fluidTanks[i] = new FluidStackTank(
                () -> mStoredFluid[index],
                fluid -> mStoredFluid[index] = fluid,
                mCapacityPer);
        }
        inventory = new Inventory(mInventory, mStoredFluid);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_DualInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (mStoredFluid != null) {
            for (int i = 0; i < getMaxType(); i++) {
                if (mStoredFluid[i] != null)
                    aNBT.setTag("mFluid" + i, mStoredFluid[i].writeToNBT(new NBTTagCompound()));
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (mStoredFluid != null) {
            for (int i = 0; i < getMaxType(); i++) {
                if (aNBT.hasKey("mFluid" + i)) {
                    mStoredFluid[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid" + i));
                }
            }
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_CRAFTING_INPUT_BUFFER) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_CRAFTING_INPUT_BUFFER) };
    }

    @Override
    public FluidStack getFluid() {
        for (FluidStack tFluid : mStoredFluid) {
            if (tFluid != null && tFluid.amount > 0) return tFluid;
        }
        return null;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPullStack(aBaseMetaTileEntity, aIndex, side, aStack) && aIndex != CATALYST_SLOT_1
            && aIndex != CATALYST_SLOT_2;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex != CATALYST_SLOT_1 && aIndex != CATALYST_SLOT_2
            && super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack);
    }

    public FluidStack getFluid(int aSlot) {
        if (mStoredFluid == null || aSlot < 0 || aSlot >= getMaxType()) return null;
        return mStoredFluid[aSlot];
    }

    @Override
    public int getFluidAmount() {
        if (getFluid() != null) {
            return getFluid().amount;
        }
        return 0;
    }

    @Override
    public int getCapacity() {
        return mCapacityPer;
    }

    public int getFirstEmptySlot() {
        for (int i = 0; i < getMaxType(); i++) {
            if (mStoredFluid[i] == null) return i;
        }
        return -1;
    }

    public boolean hasFluid(FluidStack aFluid) {
        if (aFluid == null) return false;
        for (FluidStack tFluid : mStoredFluid) {
            if (aFluid.isFluidEqual(tFluid)) return true;
        }
        return false;
    }

    public int getFluidSlot(FluidStack tFluid) {
        if (tFluid == null) return -1;
        for (int i = 0; i < getMaxType(); i++) {
            if (tFluid.isFluidEqual(mStoredFluid[i])) return i;
        }
        return -1;
    }

    public int getFluidAmount(FluidStack tFluid) {
        int tSlot = getFluidSlot(tFluid);
        if (tSlot != -1) {
            return mStoredFluid[tSlot].amount;
        }
        return 0;
    }

    public void setFluid(FluidStack aFluid, int aSlot) {
        if (aSlot < 0 || aSlot >= getMaxType()) return;
        mStoredFluid[aSlot] = aFluid;
    }

    public void addFluid(FluidStack aFluid, int aSlot) {
        if (aSlot < 0 || aSlot >= getMaxType()) return;
        if (aFluid.equals(mStoredFluid[aSlot])) mStoredFluid[aSlot].amount += aFluid.amount;
        if (mStoredFluid[aSlot] == null) mStoredFluid[aSlot] = aFluid.copy();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            mFluid = getFluid();
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean justUpdated() {
        return false;
    }

    @Override
    public Iterator<? extends IDualInputInventory> inventories() {
        return Arrays.stream(new Inventory[] { inventory })
            .filter(Objects::nonNull)
            .iterator();
    }

    @Override
    public Optional<IDualInputInventory> getFirstNonEmptyInventory() {
        if (!inventory.isEmpty()) {
            return Optional.of(inventory);
        }
        return Optional.empty();
    }

    @Override
    public boolean supportsFluids() {
        return true;
    }

    @Override
    public ItemStack[] getSharedItems() {
        return new ItemStack[0];
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public int getCircuitSlot() {
        return ITEM_SLOT_AMOUNT - 1;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0 || aFluid.amount <= 0 || !canTankBeFilled() || !isFluidInputAllowed(aFluid)) return 0;
        if (!hasFluid(aFluid) && getFirstEmptySlot() != -1) {
            int tFilled = Math.min(aFluid.amount, mCapacityPer);
            if (doFill) {
                FluidStack tFluid = aFluid.copy();
                tFluid.amount = tFilled;
                addFluid(tFluid, getFirstEmptySlot());
                getBaseMetaTileEntity().markDirty();
            }
            return tFilled;
        }
        if (hasFluid(aFluid)) {
            int tLeft = mCapacityPer - getFluidAmount(aFluid);
            int tFilled = Math.min(tLeft, aFluid.amount);
            if (doFill) {
                FluidStack tFluid = aFluid.copy();
                tFluid.amount = tFilled;
                addFluid(tFluid, getFluidSlot(tFluid));
                getBaseMetaTileEntity().markDirty();
            }
            return tFilled;
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack nowFluid = getFluid();
        if (nowFluid == null) return null;
        else return drain(ForgeDirection.UNKNOWN, new FluidStack(nowFluid.getFluid(), maxDrain), doDrain);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack aFluid, boolean doDrain) {
        if (aFluid == null || !hasFluid(aFluid)) return null;
        FluidStack tStored = mStoredFluid[getFluidSlot(aFluid)];
        if (tStored.amount <= 0 && isFluidChangingAllowed()) {
            setFluid(null, getFluidSlot(tStored));
            getBaseMetaTileEntity().markDirty();
            return null;
        }
        FluidStack tRemove = tStored.copy();
        tRemove.amount = Math.min(aFluid.amount, tRemove.amount);
        if (doDrain) {
            tStored.amount -= tRemove.amount;
            getBaseMetaTileEntity().markDirty();
        }
        if (tStored.amount <= 0 && isFluidChangingAllowed()) {
            setFluid(null, getFluidSlot(tStored));
            getBaseMetaTileEntity().markDirty();
        }
        return tRemove;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide()) {
            updateSlots();
        }
    }

    public void updateSlots() {
        // items
        if (mInventory != null) {
            for (int i = 0; i < mInventory.length - 1; i++)
                if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        }
        // fluids
        if (mStoredFluid != null) {
            for (int i = 0; i < getMaxType(); i++) {
                if (mStoredFluid[i] != null && mStoredFluid[i].amount <= 0) mStoredFluid[i] = null;
            }
        }
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] FTI = new FluidTankInfo[getMaxType()];
        for (int i = 0; i < getMaxType(); i++) {
            FTI[i] = new FluidTankInfo(mStoredFluid[i], mCapacityPer);
        }
        return FTI;
    }

    public int getMaxType() {
        return mStoredFluid.length;
    }

    public static int getFluidSlotsAmount(int tier) {
        return switch (tier) {
            case 5 -> 2;
            case 6 -> 3;
            case 7 -> 4;
            case 8 -> 6;
            default -> 0;
        };
    }

    public static int getCapacityPerTank(int aTier) {
        return switch (aTier) {
            case 5 -> 20000;
            case 6 -> 60000;
            case 7 -> 180000;
            case 8 -> 540000;
            default -> 0;
        };
    }

    // @Override
    // public boolean useModularUI() {
    // return true;
    // }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final int SLOT_NUMBER = fluidTanks.length;
        final Pos2d[] positions = switch (SLOT_NUMBER) {
            case 2 -> new Pos2d[] { new Pos2d(124, 7), new Pos2d(124, 25) };
            case 3 -> new Pos2d[] { new Pos2d(124, 7), new Pos2d(124, 25), new Pos2d(124, 43) };
            case 4 -> new Pos2d[] { new Pos2d(124, 7), new Pos2d(124, 25), new Pos2d(124, 43), new Pos2d(124, 61) };
            case 6 -> new Pos2d[] { new Pos2d(124, 7), new Pos2d(124, 25), new Pos2d(124, 43), new Pos2d(142, 7),
                new Pos2d(142, 25), new Pos2d(142, 43) };
            default -> new Pos2d[] {};
        };

        for (int i = 0; i < SLOT_NUMBER; i++) {
            builder.widget(
                new FluidSlotWidget(fluidTanks[i]).setBackground(ModularUITextures.FLUID_SLOT)
                    .setPos(positions[i]));
        }
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 2)
                .startFromSlot(CATALYST_SLOT_1)
                .endAtSlot(CATALYST_SLOT_2)
                .background(ModularUITextures.ITEM_SLOT, GTUITextures.OVERLAY_SLOT_MOLECULAR_1)
                .build()
                .setPos(7, 7));
        getBaseMetaTileEntity().add4by4Slots(builder);
    }
}
