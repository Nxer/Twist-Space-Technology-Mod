package com.Nxer.TwistSpaceTechnology.common.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;
import com.glodblock.github.crossmod.thaumcraft.AspectUtil;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.events.MENetworkStorageEvent;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IBaseMonitor;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.ICellContainer;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.helpers.IPriorityHost;
import appeng.me.GridAccessException;
import appeng.me.cache.CraftingGridCache;
import appeng.me.storage.MEInventoryHandler;
import appeng.tile.grid.AENetworkTile;

public class TileEssentiaDiscretizer extends AENetworkTile implements IPriorityHost, ICellContainer {

    private final BaseActionSource ownActionSource = new MachineSource(this);
    private final EssentiaDiscretizingInventory essentiaInv = new EssentiaDiscretizingInventory();

    // Without this section, the CPU cannot correctly identify the source quality of the returned essentials.
    // The fluidCraftInv part is from ae2t .thanks for asdflj.
    private final FluidCraftingInventory fluidCraftInv = new FluidCraftingInventory();

    private boolean prevActiveState = false;

    public TileEssentiaDiscretizer() {
        super();
        getProxy().setIdlePowerUsage(3D);
        getProxy().setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    @Override
    public boolean canBeRotated() {
        return false;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<IMEInventoryHandler> getCellArray(StorageChannel channel) {
        if (getProxy().isActive()) {
            if (channel == StorageChannel.ITEMS) {
                return Collections.singletonList(essentiaInv.invHandler);
            } else if (channel == StorageChannel.FLUIDS) {
                return Collections.singletonList(fluidCraftInv.invHandler);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setPriority(int newValue) {
        // no-op
    }

    @Override
    public void gridChanged() {
        IMEMonitor<IAEFluidStack> fluidGrid = getFluidGrid();
        if (fluidGrid != null) {
            fluidGrid.addListener(essentiaInv, fluidGrid);
        }
        essentiaInv.itemCache = null;
        try {
            getProxy().getGrid()
                .postEvent(new MENetworkCellArrayUpdate());
        } catch (GridAccessException ignored) {}
    }

    private IMEMonitor<IAEFluidStack> getFluidGrid() {
        try {
            return getProxy().getGrid()
                .<IStorageGrid>getCache(IStorageGrid.class)
                .getFluidInventory();
        } catch (GridAccessException e) {
            return null;
        }
    }

    private IEnergyGrid getEnergyGrid() {
        try {
            return getProxy().getGrid()
                .getCache(IEnergyGrid.class);
        } catch (GridAccessException e) {
            return null;
        }
    }

    private void updateState() {
        boolean isActive = getProxy().isActive();
        if (isActive != prevActiveState) {
            prevActiveState = isActive;
            try {
                getProxy().getGrid()
                    .postEvent(new MENetworkCellArrayUpdate());
            } catch (GridAccessException e) {}
        }
    }

    @MENetworkEventSubscribe
    public void onPowerUpdate(MENetworkPowerStatusChange event) {
        updateState();
    }

    @MENetworkEventSubscribe
    public void onChannelUpdate(MENetworkChannelsChanged event) {
        updateState();
    }

    @MENetworkEventSubscribe
    public void onStorageUpdate(MENetworkStorageEvent event) {
        updateState();
    }

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        markDirty();
    }

    private class EssentiaDiscretizingInventory
        implements IMEInventory<IAEItemStack>, IMEMonitorHandlerReceiver<IAEFluidStack> {

        private final MEInventoryHandler<IAEItemStack> invHandler = new MEInventoryHandler<>(
            this,
            StorageChannel.ITEMS);

        private IItemList<IAEItemStack> itemCache = null;

        EssentiaDiscretizingInventory() {
            invHandler.setPriority(Integer.MAX_VALUE);
        }

        @Override
        public IAEItemStack injectItems(IAEItemStack request, Actionable type, BaseActionSource src) {
            if (request == null) return null;

            IAEFluidStack essentiaFluid = ItemEssentiaHelper.getAeFluidStack(request);
            if (essentiaFluid == null) return request;

            IMEMonitor<IAEFluidStack> fluidGrid = getFluidGrid();
            if (fluidGrid == null) return request;

            IEnergyGrid energyGrid = getEnergyGrid();
            if (energyGrid == null) return request;

            if (type == Actionable.SIMULATE) {
                return ItemEssentiaHelper
                    .newAeStack(fluidGrid.injectItems(essentiaFluid.copy(), Actionable.SIMULATE, src));
            } else {
                IAEFluidStack rem = fluidGrid.injectItems(essentiaFluid.copy(), Actionable.MODULATE, src);
                itemCache = null;
                return ItemEssentiaHelper.newAeStack(rem);
            }
        }

        @Override
        public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
            if (request == null) return null;
            IAEFluidStack fluidRequest = ItemEssentiaHelper.getAeFluidStack(request);
            if (fluidRequest == null) return null;

            IMEMonitor<IAEFluidStack> fluidGrid = getFluidGrid();
            if (fluidGrid == null) return null;

            IEnergyGrid energyGrid = getEnergyGrid();
            if (energyGrid == null) return null;

            if (mode == Actionable.SIMULATE) {
                return ItemEssentiaHelper
                    .newAeStack(fluidGrid.extractItems(fluidRequest.copy(), Actionable.SIMULATE, src));
            } else {
                IAEFluidStack extracted = fluidGrid.extractItems(fluidRequest.copy(), Actionable.MODULATE, src);
                itemCache = null;
                return ItemEssentiaHelper.newAeStack(extracted);
            }
        }

        @Override
        public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> out, int iteration) {
            itemCache = AEApi.instance()
                .storage()
                .createItemList();
            IMEMonitor<IAEFluidStack> fluidGrid = getFluidGrid();
            if (fluidGrid != null) {
                for (IAEFluidStack fluid : fluidGrid.getStorageList()) {
                    IAEItemStack stack = ItemEssentiaHelper.newAeStack(fluid);
                    if (stack != null) itemCache.add(stack);
                }
            }
            for (IAEItemStack stack : itemCache) {
                out.addStorage(stack);
            }
            return out;
        }

        @Override
        public IAEItemStack getAvailableItem(@Nonnull IAEItemStack request, int iteration) {
            IMEMonitor<IAEFluidStack> fluidGrid = getFluidGrid();
            if (fluidGrid == null) return null;
            IAEFluidStack fluidReq = ItemEssentiaHelper.getAeFluidStack(request);
            if (fluidReq == null) return null;
            IAEFluidStack available = fluidGrid.getAvailableItem(fluidReq, iteration);
            return ItemEssentiaHelper.newAeStack(available);
        }

        @Override
        public StorageChannel getChannel() {
            return StorageChannel.ITEMS;
        }

        @Override
        public boolean isValid(Object verificationToken) {
            IMEMonitor<IAEFluidStack> fluidGrid = getFluidGrid();
            return fluidGrid != null && fluidGrid == verificationToken;
        }

        @Override
        public void postChange(IBaseMonitor<IAEFluidStack> monitor, Iterable<IAEFluidStack> change,
            BaseActionSource actionSource) {
            itemCache = null;
            try {
                List<IAEItemStack> mappedChanges = new ArrayList<>();
                for (IAEFluidStack fluidStack : change) {
                    IAEItemStack itemStack = ItemEssentiaHelper.newAeStack(fluidStack);
                    if (itemStack != null) mappedChanges.add(itemStack);
                }
                getProxy().getGrid()
                    .<IStorageGrid>getCache(IStorageGrid.class)
                    .postAlterationOfStoredItems(getChannel(), mappedChanges, ownActionSource);
            } catch (GridAccessException ignored) {}
        }

        @Override
        public void onListUpdate() {
            itemCache = null;
        }
    }

    private class FluidCraftingInventory implements IMEInventory<IAEFluidStack> {

        private final MEInventoryHandler<IAEFluidStack> invHandler = new MEInventoryHandler<>(
            this,
            StorageChannel.FLUIDS);

        FluidCraftingInventory() {
            invHandler.setPriority(Integer.MAX_VALUE);
        }

        @Override
        @SuppressWarnings("rawtypes")
        public IAEFluidStack injectItems(IAEFluidStack input, Actionable type, BaseActionSource src) {
            if (input == null) return null;

            ICraftingGrid craftingGrid;
            try {
                craftingGrid = (ICraftingGrid) getProxy().getGrid()
                    .getCache(ICraftingGrid.class);
            } catch (GridAccessException e) {
                return input;
            }

            try {
                if (craftingGrid instanceof CraftingGridCache) {
                    if (AspectUtil.isEssentiaGas(input.getFluidStack())) {
                        IAEItemStack asItem = ItemEssentiaHelper.newAeStack(input);
                        if (asItem == null) return input;

                        IAEStack remaining = ((CraftingGridCache) craftingGrid)
                            .injectItems(asItem, type, ownActionSource);
                        if (remaining instanceof IAEItemStack) {
                            return ItemEssentiaHelper.getAeFluidStack((IAEItemStack) remaining);
                        } else if (remaining == null) {
                            return null;
                        }
                    }
                }
            } catch (Throwable t) {
                // FMLLog.warning("FluidCraftingInventory.injectItems error: %s", t.getMessage());
                return input;
            }
            return input;
        }

        @Override
        public IAEFluidStack extractItems(IAEFluidStack request, Actionable mode, BaseActionSource src) {
            return null;
        }

        @Override
        public IItemList<IAEFluidStack> getAvailableItems(IItemList<IAEFluidStack> out, int iteration) {
            return out;
        }

        @Override
        public IAEFluidStack getAvailableItem(@Nonnull IAEFluidStack request, int iteration) {
            return null;
        }

        @Override
        public StorageChannel getChannel() {
            return StorageChannel.FLUIDS;
        }
    }
}
