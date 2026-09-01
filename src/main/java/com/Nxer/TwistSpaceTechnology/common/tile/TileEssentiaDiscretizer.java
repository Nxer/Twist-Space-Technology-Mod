package com.Nxer.TwistSpaceTechnology.common.tile;

import static appeng.util.item.AEItemStackType.ITEM_STACK_TYPE;
import static thaumicenergistics.common.storage.AEEssentiaStackType.ESSENTIA_STACK_TYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
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
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IAEStackType;
import appeng.api.storage.data.IItemList;
import appeng.me.GridAccessException;
import appeng.me.cache.CraftingGridCache;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.storage.MEInventoryHandler;
import appeng.tile.grid.AENetworkTile;
import thaumicenergistics.common.storage.AEEssentiaStack;

/**
 * Bridges CrystalEssence (AE2 ITEMS channel) and ThaumicEnergistics native essentia storage.
 * <p>
 * After the ThaumicEnergistics "Native Essentia" rewrite (PR #98) the old GaseousEssentia fluid API is gone; this tile
 * now talks to the essentia network via {@code AEEssentiaStack} / {@code ESSENTIA_STACK_TYPE}.
 * <p>
 * Extends {@link AENetworkTile} which provides the correct AE2 grid integration (AENetworkProxy lifecycle, NBT
 * persistence, IGridHost implementation) — matching how AE2's own devices work.
 */
public class TileEssentiaDiscretizer extends AENetworkTile implements ICellContainer {

    private final BaseActionSource ownActionSource = new MachineSource(this);
    private final EssentiaDiscretizingInventory essentiaInv = new EssentiaDiscretizingInventory();
    private final EssentiaCraftingInventory essentiaCraftInv = new EssentiaCraftingInventory();
    private boolean prevActiveState = false;

    public TileEssentiaDiscretizer() {
        super();
    }

    @Override
    protected AENetworkProxy createProxy() {
        AENetworkProxy proxy = super.createProxy();
        proxy.setIdlePowerUsage(3D);
        proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
        proxy.setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        return proxy;
    }

    @Override
    public boolean canBeRotated() {
        return false;
    }

    @Override
    public void securityBreak() {}

    @Override
    public void onReady() {
        super.onReady();
    }

    // ---- ICellContainer ----

    @Override
    @SuppressWarnings("rawtypes")
    public List<IMEInventoryHandler> getCellArray(IAEStackType<?> type) {
        if (getProxy().isActive()) {
            if (type == ESSENTIA_STACK_TYPE) {
                return Collections.singletonList(essentiaCraftInv.invHandler);
            }
            if (type == ITEM_STACK_TYPE) {
                return Collections.singletonList(essentiaInv.invHandler);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void gridChanged() {
        IMEMonitor<AEEssentiaStack> essentiaMonitor = getEssentiaMonitor();
        if (essentiaMonitor != null) {
            essentiaMonitor.addListener(essentiaInv, essentiaMonitor);
        }
        essentiaInv.itemCache = null;
        try {
            getProxy().getGrid()
                .postEvent(new MENetworkCellArrayUpdate());
        } catch (GridAccessException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private IMEMonitor<AEEssentiaStack> getEssentiaMonitor() {
        try {
            return (IMEMonitor<AEEssentiaStack>) getProxy().getGrid()
                .<IStorageGrid>getCache(IStorageGrid.class)
                .getMEMonitor(ESSENTIA_STACK_TYPE);
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

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        markDirty();
    }

    /**
     * Presents the essentia network as a CrystalEssence item inventory on the ITEMS channel. Inject/extract of
     * CrystalEssence is forwarded to the native essentia monitor, and essentia changes are mirrored back as item
     * changes so the ME network stays in sync.
     */
    private class EssentiaDiscretizingInventory
        implements IMEInventory<IAEItemStack>, IMEMonitorHandlerReceiver<AEEssentiaStack> {

        private final MEInventoryHandler<IAEItemStack> invHandler = new MEInventoryHandler<>(this, ITEM_STACK_TYPE);
        private IItemList<IAEItemStack> itemCache = null;

        EssentiaDiscretizingInventory() {
            invHandler.setPriority(Integer.MAX_VALUE);
        }

        @Override
        public IAEItemStack injectItems(IAEItemStack request, Actionable type, BaseActionSource src) {
            if (request == null) return null;

            ItemStack itemStack = request.getItemStack();
            if (!(itemStack.getItem() instanceof thaumcraft.common.items.ItemCrystalEssence)) {
                return request;
            }

            AEEssentiaStack essentia = ItemEssentiaHelper.getAeEssentiaStack(request);
            if (essentia == null) return request;
            IMEMonitor<AEEssentiaStack> essentiaMonitor = getEssentiaMonitor();
            if (essentiaMonitor == null) return request;
            if (type == Actionable.SIMULATE) {
                return ItemEssentiaHelper
                    .newAeItemStack(essentiaMonitor.injectItems(essentia.copy(), Actionable.SIMULATE, src));
            } else {
                AEEssentiaStack rem = essentiaMonitor.injectItems(essentia.copy(), Actionable.MODULATE, src);
                itemCache = null;
                return ItemEssentiaHelper.newAeItemStack(rem);
            }
        }

        @Override
        public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
            if (request == null) return null;
            AEEssentiaStack essentiaRequest = ItemEssentiaHelper.getAeEssentiaStack(request);
            if (essentiaRequest == null) return null;
            IMEMonitor<AEEssentiaStack> essentiaMonitor = getEssentiaMonitor();
            if (essentiaMonitor == null) return null;
            if (mode == Actionable.SIMULATE) {
                return ItemEssentiaHelper
                    .newAeItemStack(essentiaMonitor.extractItems(essentiaRequest.copy(), Actionable.SIMULATE, src));
            } else {
                AEEssentiaStack extracted = essentiaMonitor
                    .extractItems(essentiaRequest.copy(), Actionable.MODULATE, src);
                itemCache = null;
                return ItemEssentiaHelper.newAeItemStack(extracted);
            }
        }

        @Override
        public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> out, int iteration) {
            itemCache = AEApi.instance()
                .storage()
                .createItemList();
            IMEMonitor<AEEssentiaStack> essentiaMonitor = getEssentiaMonitor();
            if (essentiaMonitor != null) {
                for (AEEssentiaStack essentia : essentiaMonitor.getStorageList()) {
                    IAEItemStack stack = ItemEssentiaHelper.newAeItemStack(essentia);
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
            IMEMonitor<AEEssentiaStack> essentiaMonitor = getEssentiaMonitor();
            if (essentiaMonitor == null) return null;
            AEEssentiaStack essentiaReq = ItemEssentiaHelper.getAeEssentiaStack(request);
            if (essentiaReq == null) return null;
            AEEssentiaStack available = essentiaMonitor.getAvailableItem(essentiaReq, iteration);
            return ItemEssentiaHelper.newAeItemStack(available);
        }

        @Override
        public StorageChannel getChannel() {
            return StorageChannel.ITEMS;
        }

        @Override
        public boolean isValid(Object verificationToken) {
            IMEMonitor<AEEssentiaStack> essentiaMonitor = getEssentiaMonitor();
            return essentiaMonitor != null && essentiaMonitor == verificationToken;
        }

        @Override
        public void postChange(IBaseMonitor<AEEssentiaStack> monitor, Iterable<AEEssentiaStack> change,
            BaseActionSource actionSource) {
            itemCache = null;
            try {
                List<IAEItemStack> mappedChanges = new ArrayList<>();
                for (AEEssentiaStack essentia : change) {
                    IAEItemStack itemStack = ItemEssentiaHelper.newAeItemStack(essentia);
                    if (itemStack != null) mappedChanges.add(itemStack);
                }
                getProxy().getGrid()
                    .<IStorageGrid>getCache(IStorageGrid.class)
                    .postAlterationOfStoredItems(ITEM_STACK_TYPE, mappedChanges, ownActionSource);
            } catch (GridAccessException ignored) {}
        }

        @Override
        public void onListUpdate() {
            itemCache = null;
        }
    }

    /**
     * Accepts native essentia that the crafting CPU pushes back (e.g. leftover from a crafting job) and reroutes it
     * into the crafting grid as CrystalEssence items. Exposed on the essentia channel via
     * {@link #getCellArray(IAEStackType)}.
     */
    private class EssentiaCraftingInventory implements IMEInventory<AEEssentiaStack> {

        private final MEInventoryHandler<AEEssentiaStack> invHandler = new MEInventoryHandler<>(
            this,
            ESSENTIA_STACK_TYPE);

        EssentiaCraftingInventory() {
            invHandler.setPriority(Integer.MAX_VALUE);
        }

        @Override
        public AEEssentiaStack injectItems(AEEssentiaStack input, Actionable type, BaseActionSource src) {
            if (input == null) return null;
            CraftingGridCache craftingGrid;
            try {
                craftingGrid = getProxy().getGrid()
                    .getCache(CraftingGridCache.class);
            } catch (GridAccessException e) {
                return input;
            }
            try {
                IAEItemStack asItem = ItemEssentiaHelper.newAeItemStack(input);
                if (asItem == null) return input;
                IAEStack remaining = craftingGrid.injectItems(asItem, type, ownActionSource);
                if (remaining instanceof IAEItemStack) {
                    return ItemEssentiaHelper.getAeEssentiaStack((IAEItemStack) remaining);
                } else if (remaining == null) {
                    return null;
                }
            } catch (Throwable t) {
                return input;
            }
            return input;
        }

        @Override
        public AEEssentiaStack extractItems(AEEssentiaStack request, Actionable mode, BaseActionSource src) {
            return null;
        }

        @Override
        public IItemList<AEEssentiaStack> getAvailableItems(IItemList<AEEssentiaStack> out, int iteration) {
            return out;
        }

        @Override
        public AEEssentiaStack getAvailableItem(@Nonnull AEEssentiaStack request, int iteration) {
            return null;
        }

        @Override
        @Nonnull
        public IAEStackType<AEEssentiaStack> getStackType() {
            return ESSENTIA_STACK_TYPE;
        }

        @Override
        @Deprecated
        public StorageChannel getChannel() {
            // Essentia has no StorageChannel; the default would NPE switching on this, so return null explicitly.
            return null;
        }
    }
}
