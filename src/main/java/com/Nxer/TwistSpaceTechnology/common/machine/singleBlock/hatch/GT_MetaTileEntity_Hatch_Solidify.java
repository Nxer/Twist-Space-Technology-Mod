package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.AutoSeparation;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.FluidCapacity;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUFFER;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import ggfab.GGItemList;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;

public class GT_MetaTileEntity_Hatch_Solidify extends MTEHatchInputBus implements IAddUIWidgets, IDualInputHatch {

    public static final HashSet<TST_ItemID> solidifierMolds = new HashSet<>();
    static {
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Bottle.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Plate.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Ingot.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Casing.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Gear.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Gear_Small.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Credit.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Nugget.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Block.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Ball.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Cylinder.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Anvil.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Arrow.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Rod.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Bolt.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Round.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Screw.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Ring.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Rod_Long.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Rotor.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Turbine_Blade.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Pipe_Tiny.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Pipe_Small.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Pipe_Medium.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Pipe_Large.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_Pipe_Huge.get(1));
        TST_ItemID.createNoNBT(ItemList.Shape_Mold_ToolHeadDrill.get(1));

        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolFile.get(1));
        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolWrench.get(1));
        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolCrowbar.get(1));
        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolWireCutter.get(1));
        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolHardHammer.get(1));
        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolSoftHammer.get(1));
        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolScrewdriver.get(1));
        TST_ItemID.createNoNBT(GGItemList.Shape_One_Use_craftingToolSaw.get(1));
    }
    private final FluidStack[] mStoredFluid;
    private final FluidStackTank[] fluidTanks;
    public final int mCapacityPer;
    private static final int ITEM_SLOT_AMOUNT = 1;
    private static final int MOLD_SLOT = 0;

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

    GT_MetaTileEntity_Hatch_Solidify.Inventory inventory;

    public GT_MetaTileEntity_Hatch_Solidify(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            ITEM_SLOT_AMOUNT,
            // #tr ToolTip_SolidifyHatch_1
            // # {\RESET}Fluid Input with Mold for {\GOLD}Fluid Solidifier{\RESET}
            // #zh_CN {\RESET}为{\GOLD}流体固化机{\RESET}带模具输入流体
            new String[] { TextEnums.tr("ToolTip_SolidifyHatch_1"),
                FluidCapacity + " " + getCapacityPerTank(aTier) + " L x " + getFluidSlotsAmount(aTier), AutoSeparation,
                ModNameDesc });
        mStoredFluid = new FluidStack[getFluidSlotsAmount(aTier)];
        fluidTanks = new FluidStackTank[getFluidSlotsAmount(aTier)];
        mCapacityPer = getCapacityPerTank(aTier);
        inventory = new GT_MetaTileEntity_Hatch_Solidify.Inventory(mInventory, mStoredFluid);
    }

    public GT_MetaTileEntity_Hatch_Solidify(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
        inventory = new GT_MetaTileEntity_Hatch_Solidify.Inventory(mInventory, mStoredFluid);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Solidify(mName, mTier, mDescriptionArray, mTextures);
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
    public boolean displaysStackSize() {
        return true;
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
        return super.allowPullStack(aBaseMetaTileEntity, aIndex, side, aStack) && aIndex != MOLD_SLOT;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (solidifierMolds.contains(TST_ItemID.createNoNBT(aStack))) return true;
        return false;
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
        return Arrays.stream(new GT_MetaTileEntity_Hatch_Solidify.Inventory[] { inventory })
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
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
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
            case 5 -> 1;
            case 6, 7 -> 2;
            case 8 -> 4;
            case 9 -> 6;
            default -> 0;
        };
    }

    public static int getCapacityPerTank(int aTier) {
        return switch (aTier) {
            case 5 -> 256_000;
            case 6 -> 512_000;
            case 7 -> 1024_000;
            case 8 -> 2048_000;
            case 9 -> 4096_000;
            default -> 0;
        };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final int SLOT_NUMBER = fluidTanks.length;
        final Pos2d[] positions = switch (SLOT_NUMBER) {
            case 1 -> new Pos2d[] { new Pos2d(79, 34) };
            case 2 -> new Pos2d[] { new Pos2d(70, 34), new Pos2d(88, 34) };
            case 4 -> new Pos2d[] { new Pos2d(70, 25), new Pos2d(88, 25), new Pos2d(70, 43), new Pos2d(88, 43) };
            case 6 -> new Pos2d[] { new Pos2d(61, 25), new Pos2d(79, 25), new Pos2d(97, 25), new Pos2d(61, 43),
                new Pos2d(79, 43), new Pos2d(97, 43) };
            default -> new Pos2d[] {};
        };

        for (int i = 0; i < SLOT_NUMBER; i++) {
            builder.widget(
                new FluidSlotWidget(fluidTanks[i]).setBackground(ModularUITextures.FLUID_SLOT)
                    .setPos(positions[i]));
        }
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 2)
                .startFromSlot(MOLD_SLOT)
                .endAtSlot(MOLD_SLOT)
                .background(ModularUITextures.ITEM_SLOT, GTUITextures.OVERLAY_SLOT_MOLD)
                .build()
                .setPos(7, 7));
    }
}
