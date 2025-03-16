package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaCraftingCenter;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.google.common.collect.ImmutableList;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.ICellContainer;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IItemList;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public class TST_PatternAccessHatch extends MTEHatch
    implements IGridProxyable, ICellContainer, IMEInventoryHandler<AEItemStack>, IPowerChannelState {

    private TST_MegaCraftingCenter controller;
    private AENetworkProxy gridProxy;
    private static final String[] DESC = new String[] {
        // #tr Tooltip_PatternAccessHatch_1
        // # Access Hatch for Extreme Crafting Center
        // #zh_CN 梦魇工业合成中心的访问仓
        TextEnums.tr("Tooltip_PatternAccessHatch_1"),
        // #tr Tooltip_PatternAccessHatch_2
        // # Connect to ME net to access patterns stored in Extreme Crafting Center.
        // #zh_CN 连接ME网络以访问梦魇工业合成中心存储的样板
        TextEnums.tr("Tooltip_PatternAccessHatch_2"),
        // #tr Tooltip_PatternAccessHatch_3
        // # Extreme Crafting Center can only accept 1 Pattern Access Hatch at maximum.
        // #zh_CN 梦魇工业合成中心最多接受一个样板访问仓
        TextEnums.tr("Tooltip_PatternAccessHatch_3"),
        // #tr Tooltip_PatternAccessHatch_4
        // # Invalid items or duplicated patterns will be rejected.
        // #zh_CN 错误或重复的样板不会由此输入
        TextEnums.tr("Tooltip_PatternAccessHatch_4"),

        TextEnums.Mod_TwistSpaceTechnology.getText() };

    public TST_PatternAccessHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
    }

    public TST_PatternAccessHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public synchronized String[] getDescription() {
        return DESC;
    }

    public void bind(TST_MegaCraftingCenter controller) {
        this.controller = controller;
        try {
            this.getProxy()
                .getGrid()
                .postEvent(new MENetworkCellArrayUpdate());
        } catch (GridAccessException e) {}
        postChanges();
    }

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", GTCMItemList.PatternAccessHatch.get(1), true);
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            updateValidGridProxySides();
            if (getBaseMetaTileEntity().getWorld() != null) {
                gridProxy.setOwner(
                    getBaseMetaTileEntity().getWorld()
                        .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }

        }
        return this.gridProxy;
    }

    private boolean additionalConnection;

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        additionalConnection = !additionalConnection;
        updateValidGridProxySides();
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation("GT5U.hatch.additionalConnection." + additionalConnection));
        return true;
    }

    private void updateValidGridProxySides() {
        if (additionalConnection) {
            getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        } else {
            getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {

        if (getBaseMetaTileEntity().isValidFacing(wrenchingSide)) {
            getBaseMetaTileEntity().setFrontFacing(wrenchingSide);
            return true;
        }
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {

        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {

        return true;
    }

    boolean facingJustChanged;

    @Override
    public void onFacingChange() {
        updateValidGridProxySides();
        // postpone to next tick to update cache
        // update here will not work
        facingJustChanged = true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        getProxy().onReady();
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_PatternAccessHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {

        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN),
            TextureFactory.of(BlockIcons.OVERLAY_ME_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {

        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN),
            TextureFactory.of(BlockIcons.OVERLAY_ME_HATCH) };
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {

        return getProxy().getNode();
    }

    @Override
    public void securityBreak() {

    }

    @Override
    public DimensionalCoord getLocation() {

        return new DimensionalCoord((TileEntity) this.getBaseMetaTileEntity());
    }

    @Override
    public IGridNode getActionableNode() {

        return getProxy().getNode();
    }

    @Override
    public List<IMEInventoryHandler> getCellArray(StorageChannel channel) {
        if (getController() == null) {
            return ImmutableList.of();
        }
        List<IMEInventoryHandler> list = new ArrayList<>();
        if (channel == StorageChannel.ITEMS) {
            list.add(this);
        }
        return list;
    }

    @Override
    public int getPriority() {

        return 0;
    }

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        // This is handled by host itself.
    }

    @Override
    public AEItemStack injectItems(AEItemStack in, Actionable mode, BaseActionSource src) {
        if (getController() == null) return in;

        if (mode == Actionable.SIMULATE) {
            return null;
        }

        ItemStack rest = getController().tryInjectPattern(in.getItemStack());
        return AEItemStack.create(rest);
    }

    @Override
    public AEItemStack extractItems(AEItemStack request, Actionable mode, BaseActionSource src) {
        if (getController() == null) return null;
        if (getController().mMaxProgresstime > 0) return null;
        if (request.getStackSize() > 1) {
            request = (AEItemStack) request.copy()
                .setStackSize(1);
        }
        if (mode == Actionable.SIMULATE) {
            return request;
        }
        return AEItemStack.create(getController().extractPattern(request));
    }

    @Override
    public StorageChannel getChannel() {

        return StorageChannel.ITEMS;
    }

    @Override
    public AccessRestriction getAccess() {

        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(AEItemStack input) {

        return false;
    }

    @Override
    public boolean canAccept(AEItemStack input) {
        if (getController() == null) return false;
        return input.getItem() instanceof ICraftingPatternItem;
    }

    @Override
    public int getSlot() {

        return 0;
    }

    @Override
    public IItemList getAvailableItems(IItemList<AEItemStack> out, int iteration) {
        IItemList<AEItemStack> list = out;
        if (getController() == null) return list;
        for (ItemStack stack : getController().getInternalPatterns()) {
            list.add(AEItemStack.create(stack));
        }
        return list;
    }

    @Override
    public boolean validForPass(int i) {

        return true;
    }

    boolean wasActive;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        if (aBaseMetaTileEntity.isServerSide()) {
            if (facingJustChanged) {
                facingJustChanged = false;
                try {
                    this.getProxy()
                        .getGrid()
                        .postEvent(new MENetworkCellArrayUpdate());
                } catch (GridAccessException e) {}
                postChanges();

            }
            if (aTick % 20 == 0) {
                getController();// check controller every 1 second
            }
            boolean active = this.getProxy()
                .isActive();

            if (!aBaseMetaTileEntity.getWorld().isRemote) {
                getBaseMetaTileEntity().setActive(active);
                if (wasActive != active) {
                    wasActive = active;
                    try {
                        this.getProxy()
                            .getGrid()
                            .postEvent(new MENetworkCellArrayUpdate());
                    } catch (GridAccessException e) {}
                    postChanges();
                }
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    public void onChange() {
        postChanges();

    }

    List<AEItemStack> lastPattern;

    private void postChanges() {

        MachineSource src = new MachineSource(this);
        if (lastPattern != null) {
            // remove all previous patterns from AE cache

            try {
                lastPattern.forEach(s -> s.setStackSize(-s.getStackSize()));
                this.getProxy()
                    .getStorage()
                    .postAlterationOfStoredItems(StorageChannel.ITEMS, lastPattern, src);
            } catch (GridAccessException e) {}
        }

        lastPattern = new ArrayList<>();

        TST_MegaCraftingCenter ctr = getController();
        if (ctr != null) ctr.getInternalPatterns()
            .stream()
            .map(AEItemStack::create)
            .forEach(lastPattern::add);

        try {

            // add all current patterns to AE cache
            this.getProxy()
                .getStorage()
                .postAlterationOfStoredItems(StorageChannel.ITEMS, lastPattern, src);

        } catch (GridAccessException e) {}

    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    public TST_MegaCraftingCenter getController() {
        if (controller != null) {
            if ((!controller.isValid()) || (!controller.mPatternAccessHatch.contains(this))) {
                controller = null;

                try {
                    this.getProxy()
                        .getGrid()
                        .postEvent(new MENetworkCellArrayUpdate());
                } catch (GridAccessException e) {}
                postChanges();
            }
        }

        return controller;
    }

    @Override
    public boolean isPowered() {
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        getProxy().writeToNBT(aNBT);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        getProxy().readFromNBT(aNBT);
        additionalConnection = aNBT.getBoolean("additionalConnection");
        super.loadNBTData(aNBT);
    }
}
