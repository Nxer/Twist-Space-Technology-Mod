package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_AEStorageCellHatchGui;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstSharedFormat;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class TST_AEStorageCellInputBus extends MTEHatchInputBusME implements ITSTSegmentedItemInput, ISaveProvider {

    public static final int CELL_SLOT = 1;
    private static final String SPECIAL_INPUT_NBT = "tstSpecialInput";
    private static final BaseActionSource CELL_ACTION_SOURCE = new BaseActionSource();

    private final ItemStackHandler specialInputHandler = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            justHadNewItems = true;
        }
    };
    private final CellItemEntry[] selectedContents = new CellItemEntry[TST_AEStorageCellHelper.MAX_TYPES];
    private final List<ItemStack> exposedStacks = new ArrayList<>();
    private final boolean[] displayedThisRecipe = new boolean[TST_AEStorageCellHelper.MAX_TYPES];
    private final int configuredTier;
    private boolean storageCellPresent;
    private boolean usingCellDuringRecipe;
    private boolean exposedInputsPrepared;
    private boolean longExtractionUsed;
    private boolean suppressRecipeSegments;
    private boolean showRecipeRemainder;
    private int specialInputAmountAtRecipeStart;
    private long lastGuiAmountRefreshTick = Long.MIN_VALUE;

    public TST_AEStorageCellInputBus(int id, String name, String nameRegional, int tier) {
        super(id, true, name, nameRegional);
        configuredTier = tier;
    }

    public TST_AEStorageCellInputBus(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, true, tier, description, textures);
        configuredTier = tier;
    }

    private static String[] createDescription() {
        return new String[] { TextLocalization.HatchTier + " " + TstSharedFormat.getTierName(VoltageIndex.UIV),
            // #tr Tooltip_AEStorageCellInputBus.3
            // # Advanced stocking input bus upgrade for multiblock item input
            // #zh_CN 进阶存储输入总线的升级版，为多方块机器输入物品
            TextEnums.tr("Tooltip_AEStorageCellInputBus.3"),
            // #tr Tooltip_AEStorageCellInputBus.0
            // # Retrieves up to 16 marked item types directly from the ME network
            // #zh_CN 直接从ME网络拉取至多16种已标记物品
            TextEnums.tr("Tooltip_AEStorageCellInputBus.0"),
            // #tr Tooltip_AEStorageCellInputBus.2
            // # An inserted ME storage cell supplies items instead and disconnects the ME network
            // #zh_CN 放入ME存储元件后改从元件中拉取，且无法连接ME网络
            TextEnums.tr("Tooltip_AEStorageCellInputBus.2"),
            // #tr Tooltip_AEStorageCellInputBus.1
            // # Processes up to %s x 2147483647 items in total per recipe
            // #zh_CN 单次配方合计最多处理%s × 2147483647件物品
            TextEnums.tr("Tooltip_AEStorageCellInputBus.1", Config.MaxTotalIntSegments_AEStorageCellInput),
            TextEnums.Author_Goderium.getText(), ModNameDesc };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TST_AEStorageCellInputBus(mName, configuredTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return createDescription();
    }

    @Override
    public byte getTierForStructure() {
        return (byte) VoltageIndex.UIV;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        specialInputHandler.setStackInSlot(0, GTUtility.loadItem(nbt, SPECIAL_INPUT_NBT));
        updateStorageCellState();
        updateValidGridProxySides();
        if (getProxy().getNode() != null) getProxy().getNode()
            .updateState();
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        GTUtility.saveItem(nbt, SPECIAL_INPUT_NBT, specialInputHandler.getStackInSlot(0));
    }

    public ItemStackHandler getSpecialInputHandler() {
        return specialInputHandler;
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (slot != CELL_SLOT) return;
        markDirty();
        showRecipeRemainder = false;
        lastGuiAmountRefreshTick = Long.MIN_VALUE;
        updateStorageCellState();
        updateValidGridProxySides();
        if (getProxy().getNode() != null) getProxy().getNode()
            .updateState();
        if (!processingRecipe && autoPullItemList && getBaseMetaTileEntity().isServerSide()) {
            clearSlotConfigs();
            if (hasStorageCell()) refreshItemList();
        }
        if (!processingRecipe) updateAllInformationSlots();
    }

    @Override
    protected void updateValidGridProxySides() {
        // When a cell is inserted, disconnect this bus from the ME network.
        if (hasStorageCell()) {
            getProxy().setValidSides(java.util.EnumSet.noneOf(ForgeDirection.class));
        } else {
            super.updateValidGridProxySides();
        }
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection side) {
        return hasStorageCell() ? AECableType.NONE : super.getCableConnectionType(side);
    }

    @Override
    public boolean isAllowedToWork() {
        if (processingRecipe) return cachedActivity;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || !base.isAllowedToWork()) return false;
        return hasStorageCell() || getProxy().isActive();
    }

    @Override
    public boolean isPowered() {
        return hasStorageCell() || super.isPowered();
    }

    @Override
    public boolean isActive() {
        return hasStorageCell() ? isAllowedToWork() : super.isActive();
    }

    @Override
    public void setAutoPullItemList(boolean pullItemList) {
        super.setAutoPullItemList(pullItemList);
    }

    @Override
    protected void refreshItemList() {
        if (!hasStorageCell()) {
            super.refreshItemList();
            return;
        }
        if (!isAllowedToWork()) return;
        IMEInventoryHandler<IAEItemStack> inventory = getCellInventory();
        if (inventory == null) return;

        int index = 0;
        for (IAEItemStack available : inventory.getAvailableItems(new ItemList(), 0)) {
            if (index >= slots.length) break;
            if (available.getStackSize() < minAutoPullStackSize) continue;
            ItemStack stack = available.getItemStack();
            if (stack == null) continue;
            Slot oldSlot = slots[index];
            if (oldSlot == null || !GTUtility.areStacksEqual(oldSlot.config, stack)
                || oldSlot.extractedAmount < stack.stackSize) justHadNewItems = true;
            setSlotConfig(index, GTUtility.copyAmount(1, stack));
            slots[index].extracted = stack;
            slots[index].extractedAmount = stack.stackSize;
            index++;
        }
        Arrays.fill(slots, index, slots.length, null);
        Arrays.fill(selectedContents, index, selectedContents.length, null);
    }

    @Override
    public void setSlotConfig(int index, ItemStack config) {
        Slot previous = index >= 0 && index < slots.length ? slots[index] : null;
        boolean changed = previous == null ? config != null : !GTUtility.areStacksEqual(previous.config, config);
        super.setSlotConfig(index, config);
        if (changed && index >= 0 && index < selectedContents.length) {
            selectedContents[index] = null;
            showRecipeRemainder = false;
            lastGuiAmountRefreshTick = Long.MIN_VALUE;
        }
    }

    @Override
    protected void clearSlotConfigs() {
        super.clearSlotConfigs();
        Arrays.fill(selectedContents, null);
        showRecipeRemainder = false;
        lastGuiAmountRefreshTick = Long.MIN_VALUE;
    }

    @Override
    public void updateInformationSlot(int index) throws GridAccessException {
        if (!hasStorageCell()) {
            super.updateInformationSlot(index);
            return;
        }

        Slot slot = index < 0 || index >= slots.length ? null : slots[index];
        if (slot == null) return;
        IMEInventoryHandler<IAEItemStack> inventory = getCellInventory();
        IAEItemStack result = inventory == null ? null
            : inventory.extractItems(
                AEItemStack.create(slot.config)
                    .setStackSize(Long.MAX_VALUE),
                Actionable.SIMULATE,
                CELL_ACTION_SOURCE);
        slot.extracted = result == null ? null
            : result.copy()
                .setStackSize(Math.min(result.getStackSize(), Integer.MAX_VALUE))
                .getItemStack();
        slot.extractedAmount = slot.extracted == null ? 0 : slot.extracted.stackSize;
    }

    public long getGuiAvailableItemAmount(int index) {
        if (index < 0 || index >= selectedContents.length) return 0;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.isServerSide()
            && base.getWorld() != null
            && !processingRecipe
            && !showRecipeRemainder) {
            long tick = base.getWorld()
                .getTotalWorldTime();
            if (lastGuiAmountRefreshTick == Long.MIN_VALUE || tick < lastGuiAmountRefreshTick
                || tick - lastGuiAmountRefreshTick >= 20) {
                lastGuiAmountRefreshTick = tick;
                refreshSelectedContents(hasStorageCell());
            }
        }
        CellItemEntry entry = selectedContents[index];
        return entry == null ? 0 : entry.availableAmount;
    }

    /** Reads the full amount of a marked item without the GT int segment limit. */
    public long getAvailableLongItems(ItemStack item) {
        int index = findMarkedItem(item);
        if (!canUseLongInput() || index < 0) return 0;
        boolean useCell = hasStorageCell();
        try {
            IMEInventory<IAEItemStack> inventory = getAvailableInventory(useCell);
            if (inventory == null) return 0;
            IAEItemStack found = inventory.extractItems(
                AEItemStack.create(item)
                    .setStackSize(Long.MAX_VALUE),
                Actionable.SIMULATE,
                getAvailableSource(useCell));
            if (found != null && found.getStackSize() > 0
                && processingRecipe
                && !exposedInputsPrepared
                && !displayedThisRecipe[index]) {
                selectedContents[index] = new CellItemEntry(found);
                slots[index].extracted = found.copy()
                    .setStackSize(Math.min(found.getStackSize(), Integer.MAX_VALUE))
                    .getItemStack();
                slots[index].extractedAmount = slots[index].extracted.stackSize;
                displayedThisRecipe[index] = true;
            }
            return found == null ? 0 : Math.max(0, found.getStackSize());
        } catch (GridAccessException ignored) {
            return 0;
        }
    }

    public List<ItemStack> getConfiguredItemInputs() {
        List<ItemStack> items = new ArrayList<>();
        for (Slot slot : slots) {
            if (slot != null && slot.config != null) items.add(slot.config.copy());
        }
        return items;
    }

    public IGrid getLongItemNetwork() {
        if (hasStorageCell() || !getProxy().isActive()) return null;
        try {
            return getProxy().getGrid();
        } catch (GridAccessException ignored) {
            return null;
        }
    }

    public static ArrayList<ItemStack> collectWithoutRecipeSegments(Supplier<ArrayList<ItemStack>> collect,
        List<MTEHatchInputBus> inputBusses, List<TST_AEStorageCellInputBus> longBusses, Optional<Byte> color) {
        if (longBusses.isEmpty() || !longBusses.get(0).processingRecipe) return collect.get();

        for (TST_AEStorageCellInputBus bus : longBusses) bus.suppressRecipeSegments = true;
        try {
            return filterOverlappingMEInputs(collect.get(), inputBusses, longBusses, color);
        } finally {
            for (TST_AEStorageCellInputBus bus : longBusses) bus.suppressRecipeSegments = false;
        }
    }

    private static ArrayList<ItemStack> filterOverlappingMEInputs(ArrayList<ItemStack> items,
        List<MTEHatchInputBus> inputBusses, List<TST_AEStorageCellInputBus> longBusses, Optional<Byte> color) {
        Map<GTUtility.ItemId, ItemStack> otherMEItems = new HashMap<>();
        Set<ItemStack> overlappingStacks = Collections.newSetFromMap(new IdentityHashMap<>());
        for (MTEHatchInputBus bus : GTUtility.filterValidMTEs(inputBusses)) {
            if (!(bus instanceof MTEHatchInputBusME meBus) || bus instanceof TST_AEStorageCellInputBus) continue;
            byte busColor = bus.getColor();
            if (color.isPresent() && busColor != -1 && busColor != color.get()) continue;
            for (int i = meBus.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack item = meBus.getStackInSlot(i);
                if (item == null) continue;
                if (isSuppliedByLongInput(meBus, item, longBusses)) overlappingStacks.add(item);
                else otherMEItems.put(GTUtility.ItemId.createNoCopy(item), item);
            }
        }
        // Keep GT's selected ME stack when another network still supplies the same item.
        for (ListIterator<ItemStack> iterator = items.listIterator(); iterator.hasNext();) {
            ItemStack item = iterator.next();
            if (!overlappingStacks.contains(item)) continue;
            ItemStack replacement = otherMEItems.get(GTUtility.ItemId.createNoCopy(item));
            if (replacement == null) iterator.remove();
            else iterator.set(replacement);
        }
        return items;
    }

    private static boolean isSuppliedByLongInput(MTEHatchInputBusME meBus, ItemStack item,
        List<TST_AEStorageCellInputBus> longBusses) {
        try {
            IGrid network = meBus.getProxy()
                .getGrid();
            if (network == null) return false;
            for (TST_AEStorageCellInputBus bus : longBusses) {
                if (bus.getLongItemNetwork() == network && bus.findMarkedItem(item) >= 0
                    && bus.getAvailableLongItems(item) > 0) return true;
            }
        } catch (GridAccessException ignored) {}
        return false;
    }

    /** Returns the actual amount pulled, possibly partial; do not also consume this bus's GT stacks. */
    public long extractLongItems(ItemStack item, long amount) {
        int index = findMarkedItem(item);
        if (amount <= 0 || !canUseLongInput() || index < 0) return 0;
        if (processingRecipe && !displayedThisRecipe[index]) getAvailableLongItems(item);
        boolean useCell = hasStorageCell();
        try {
            IMEInventory<IAEItemStack> inventory = getAvailableInventory(useCell);
            if (inventory == null) return 0;
            IAEItemStack request = AEItemStack.create(item)
                .setStackSize(amount);
            IAEItemStack extracted = useCell ? inventory.extractItems(request, Actionable.MODULATE, CELL_ACTION_SOURCE)
                : Platform.poweredExtraction(getProxy().getEnergy(), inventory, request, getAvailableSource(false));
            long pulled = extracted == null ? 0 : Math.max(0, Math.min(amount, extracted.getStackSize()));
            if (pulled > 0) {
                if (useCell) markDirty();
                if (processingRecipe) {
                    longExtractionUsed = true;
                    reduceDisplayedAmount(index, pulled);
                }
                lastGuiAmountRefreshTick = Long.MIN_VALUE;
            }
            return pulled;
        } catch (GridAccessException ignored) {
            return 0;
        }
    }

    private boolean canUseLongInput() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        return base != null && base.isServerSide()
            && (!processingRecipe || !exposedInputsPrepared)
            && isAllowedToWork();
    }

    private int findMarkedItem(ItemStack item) {
        if (item == null || item.stackSize <= 0) return -1;
        for (int i = 0; i < slots.length; i++) {
            Slot slot = slots[i];
            if (slot != null && slot.config != null && GTUtility.areStacksEqual(slot.config, item)) return i;
        }
        return -1;
    }

    private void reduceDisplayedAmount(int index, long amount) {
        CellItemEntry entry = selectedContents[index];
        if (entry == null) return;
        entry.availableAmount = Math.max(0, entry.availableAmount - amount);
        Slot slot = slots[index];
        if (slot != null && slot.extracted != null) {
            slot.extractedAmount = (int) Math.min(entry.availableAmount, Integer.MAX_VALUE);
            slot.extracted.stackSize = slot.extractedAmount;
        }
        showRecipeRemainder = true;
    }

    private void prepareRecipeInputsIfNeeded() {
        if (!processingRecipe || exposedInputsPrepared || longExtractionUsed || !cachedActivity) return;
        // Special machines can pull long directly without building the GT int stacks.
        exposedInputsPrepared = true;
        refreshSelectedContents(usingCellDuringRecipe);
        prepareExposedStacks();
    }

    @Override
    public int getSizeInventory() {
        // Long-input machines still see the circuit and special input, but not GT item segments.
        if (processingRecipe && suppressRecipeSegments) return 2;
        // During a recipe, show item stacks and the special input instead of the cell.
        prepareRecipeInputsIfNeeded();
        return processingRecipe ? exposedStacks.size() + 2 : super.getSizeInventory();
    }

    @Override
    public List<ItemStack> getTSTStoredItemSegments() {
        if (processingRecipe && suppressRecipeSegments) return Collections.emptyList();
        prepareRecipeInputsIfNeeded();
        return exposedStacks;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (!processingRecipe) return super.getStackInSlot(slot);
        if (suppressRecipeSegments) {
            if (slot == 0) return mInventory[getCircuitSlot()];
            return slot == 1 ? specialInputHandler.getStackInSlot(0) : null;
        }
        prepareRecipeInputsIfNeeded();
        if (slot >= 0 && slot < exposedStacks.size()) return exposedStacks.get(slot);
        if (slot == exposedStacks.size()) return mInventory[getCircuitSlot()];
        return slot == exposedStacks.size() + 1 ? specialInputHandler.getStackInSlot(0) : null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (processingRecipe) {
            prepareRecipeInputsIfNeeded();
            if (slot >= 0 && slot < exposedStacks.size()) {
                ItemStack exposed = exposedStacks.get(slot);
                if (stack == null) exposed.stackSize = 0;
                else if (GTUtility.areStacksEqual(exposed, stack))
                    exposed.stackSize = Math.min(exposed.stackSize, stack.stackSize);
            } else if (slot == exposedStacks.size()) {
                super.setInventorySlotContents(getCircuitSlot(), stack);
            } else if (slot == exposedStacks.size() + 1) {
                specialInputHandler.setStackInSlot(0, stack);
            }
        } else if (slot == CELL_SLOT && (stack == null || isItemCell(stack))) {
            super.setInventorySlotContents(slot, stack);
        } else if (slot == getCircuitSlot()) {
            super.setInventorySlotContents(slot, stack);
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (!processingRecipe) return super.decrStackSize(slot, amount);
        prepareRecipeInputsIfNeeded();
        if (slot == exposedStacks.size()) return super.decrStackSize(getCircuitSlot(), amount);
        if (slot == exposedStacks.size() + 1) return specialInputHandler.extractItem(0, amount, false);
        if (slot < 0 || slot >= exposedStacks.size() || amount <= 0) return null;
        ItemStack exposed = exposedStacks.get(slot);
        if (exposed.stackSize <= 0) return null;
        int extracted = Math.min(amount, exposed.stackSize);
        ItemStack result = exposed.copy();
        result.stackSize = extracted;
        exposed.stackSize -= extracted;
        return result;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] { CELL_SLOT };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return !processingRecipe && slot == CELL_SLOT && isItemCell(stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return !processingRecipe && slot == CELL_SLOT;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity base, int slot, ForgeDirection side, ItemStack stack) {
        return !processingRecipe && slot == CELL_SLOT && isItemCell(stack);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity base, int slot, ForgeDirection side, ItemStack stack) {
        return !processingRecipe && slot == CELL_SLOT;
    }

    @Override
    public boolean isValidSlot(int slot) {
        return slot == CELL_SLOT;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean connectsToItemPipe(ForgeDirection side) {
        return true;
    }

    @Override
    public void updateSlots() {
        ItemStack oldCell = mInventory[CELL_SLOT];
        super.updateSlots();
        if (oldCell != mInventory[CELL_SLOT]) onContentsChanged(CELL_SLOT);
        ItemStack specialInput = specialInputHandler.getStackInSlot(0);
        if (specialInput != null && specialInput.stackSize <= 0) specialInputHandler.setStackInSlot(0, null);
    }

    @Override
    public void startRecipeProcessing() {
        cachedActivity = isAllowedToWork();
        // Use the same cell or network source through the whole recipe.
        usingCellDuringRecipe = hasStorageCell();
        processingRecipe = true;
        exposedInputsPrepared = false;
        longExtractionUsed = false;
        showRecipeRemainder = false;
        Arrays.fill(displayedThisRecipe, false);
        lastGuiAmountRefreshTick = Long.MIN_VALUE;
        ItemStack specialInput = specialInputHandler.getStackInSlot(0);
        specialInputAmountAtRecipeStart = specialInput == null ? 0 : specialInput.stackSize;
        if (!cachedActivity) {
            exposedStacks.clear();
            for (CellItemEntry entry : selectedContents) {
                if (entry != null) entry.finishRecipeProcessing();
            }
            return;
        }
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        CheckRecipeResult result = CheckRecipeResultRegistry.SUCCESSFUL;
        IMEInventory<IAEItemStack> inventory = null;
        IEnergyGrid energy = null;
        try {
            if (usingCellDuringRecipe) {
                inventory = getCellInventory();
            } else {
                if (!getProxy().isReady()) getProxy().onReady();
                inventory = getProxy().getStorage()
                    .getItemInventory();
                energy = getProxy().getEnergy();
            }
        } catch (GridAccessException ignored) {}

        // Request only the items used by the recipe from the cell or network.
        for (int i = 0; i < selectedContents.length; i++) {
            CellItemEntry entry = selectedContents[i];
            if (entry == null) continue;
            long amount = entry.initialExposedAmount - entry.getRemainingExposedAmount();
            if (amount > 0) {
                IAEItemStack request = entry.prototype.copy()
                    .setStackSize(amount);
                IAEItemStack extracted = null;
                if (inventory != null) {
                    extracted = usingCellDuringRecipe
                        ? inventory.extractItems(request, Actionable.MODULATE, CELL_ACTION_SOURCE)
                        : Platform.poweredExtraction(energy, inventory, request, getRequestSource());
                }
                if (extracted == null || extracted.getStackSize() != amount) {
                    controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                    result = SimpleCheckRecipeResult.ofFailurePersistOnShutdown("stocking_bus_fail_extraction");
                }
                reduceDisplayedAmount(i, amount);
            }
            entry.finishRecipeProcessing();
        }

        ItemStack specialInput = specialInputHandler.getStackInSlot(0);
        if ((specialInput == null ? 0 : specialInput.stackSize) != specialInputAmountAtRecipeStart) markDirty();
        processingRecipe = false;
        exposedStacks.clear();
        exposedInputsPrepared = false;
        longExtractionUsed = false;
        lastGuiAmountRefreshTick = Long.MIN_VALUE;
        return result;
    }

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        markDirty();
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (!processingRecipe && !showRecipeRemainder) refreshSelectedContents(hasStorageCell());
        tag.setLong("cacheCapacity", (long) Integer.MAX_VALUE * Config.MaxTotalIntSegments_AEStorageCellInput);

        List<CellItemEntry> entries = new ArrayList<>();
        for (CellItemEntry entry : selectedContents) {
            if (entry != null && entry.availableAmount > 0) entries.add(entry);
        }
        entries.sort((a, b) -> Long.compare(b.availableAmount, a.availableAmount));

        tag.setInteger("stackCount", entries.size());
        NBTTagList stacks = new NBTTagList();
        tag.setTag("stacks", stacks);
        entries.stream()
            .limit(10)
            .forEach(entry -> {
                NBTTagCompound stack = new NBTTagCompound();
                stack.setString("Name", entry.displayStack.getDisplayName());
                stack.setLong("Amount", entry.availableAmount);
                stacks.appendTag(stack);
            });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        currentTip.add(
            translateToLocalFormatted(
                "GT5U.waila.hatch.outputme.item_cache_capacity",
                formatNumber(
                    accessor.getNBTData()
                        .getLong("cacheCapacity"))));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasWailaAdvancedBody(ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaAdvancedBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaAdvancedBody(itemStack, currentTip, accessor, config);
        MTEHatchOutputMEBase.WailaHelper.getWailaAdvancedBody("item", currentTip, accessor);
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new TST_AEStorageCellHatchGui.ItemBus(this, slots).build(guiData, syncManager, uiSettings);
    }

    public static boolean isItemCell(ItemStack stack) {
        return TST_AEStorageCellHelper.isCellForChannel(stack, StorageChannel.ITEMS);
    }

    private boolean hasStorageCell() {
        return storageCellPresent;
    }

    private void updateStorageCellState() {
        ItemStack cell = mInventory[CELL_SLOT];
        storageCellPresent = cell != null && cell.stackSize == 1 && isItemCell(cell);
    }

    private IMEInventoryHandler<IAEItemStack> getCellInventory() {
        return TST_AEStorageCellHelper.getCellInventory(mInventory[CELL_SLOT], this, StorageChannel.ITEMS);
    }

    private IMEInventory<IAEItemStack> getAvailableInventory(boolean useCell) throws GridAccessException {
        if (useCell) return getCellInventory();
        if (!getProxy().isActive()) return null;
        return getProxy().getStorage()
            .getItemInventory();
    }

    private BaseActionSource getAvailableSource(boolean useCell) {
        return useCell ? CELL_ACTION_SOURCE : getRequestSource();
    }

    private void refreshSelectedContents(boolean useCell) {
        IMEInventory<IAEItemStack> inventory;
        try {
            inventory = getAvailableInventory(useCell);
        } catch (GridAccessException ignored) {
            inventory = null;
        }
        if (inventory == null) {
            for (CellItemEntry entry : selectedContents) {
                if (entry != null) entry.availableAmount = 0;
            }
            clearExtractedStacks();
            return;
        }

        Set<GTUtility.ItemId> selectedItems = new HashSet<>();
        int selectedSlotCount = Math.min(slots.length, selectedContents.length);
        for (int i = 0; i < selectedSlotCount; i++) {
            Slot slot = slots[i];
            if (slot == null || slot.config == null) {
                selectedContents[i] = null;
                continue;
            }
            GTUtility.ItemId itemId = GTUtility.ItemId.createNoCopy(slot.config);
            if (!selectedItems.add(itemId)) {
                selectedContents[i] = null;
                slot.resetExtracted();
                continue;
            }

            IAEItemStack simulated = inventory.extractItems(
                AEItemStack.create(slot.config)
                    .setStackSize(Long.MAX_VALUE),
                Actionable.SIMULATE,
                getAvailableSource(useCell));
            if (simulated == null || simulated.getStackSize() <= 0) {
                CellItemEntry entry = selectedContents[i];
                if (entry != null && GTUtility.areStacksEqual(entry.displayStack, slot.config)) {
                    entry.availableAmount = 0;
                } else {
                    selectedContents[i] = null;
                }
                slot.resetExtracted();
                continue;
            }
            CellItemEntry entry = selectedContents[i];
            if (entry == null || !GTUtility.areStacksEqual(entry.displayStack, slot.config)) {
                selectedContents[i] = new CellItemEntry(simulated);
            } else {
                entry.availableAmount = simulated.getStackSize();
            }
            slot.extracted = simulated.copy()
                .setStackSize(Math.min(simulated.getStackSize(), Integer.MAX_VALUE))
                .getItemStack();
            slot.extractedAmount = slot.extracted.stackSize;
        }
        Arrays.fill(selectedContents, selectedSlotCount, selectedContents.length, null);
    }

    private void prepareExposedStacks() {
        exposedStacks.clear();
        long[] availableAmounts = new long[selectedContents.length];
        for (int i = 0; i < selectedContents.length; i++) {
            if (selectedContents[i] != null) availableAmounts[i] = selectedContents[i].availableAmount;
        }
        // All marked types share one limit, not one limit per type.
        int[] allocations = TST_AEStorageCellHelper
            .distributeIntSegments(availableAmounts, Config.MaxTotalIntSegments_AEStorageCellInput);
        for (int i = 0; i < selectedContents.length; i++) {
            if (selectedContents[i] != null) selectedContents[i].prepareExposedStacks(allocations[i], exposedStacks);
        }
    }

    private static final class CellItemEntry {

        private final IAEItemStack prototype;
        private final ItemStack displayStack;
        private final List<ItemStack> exposedStacks = new ArrayList<>();
        private long availableAmount;
        private long initialExposedAmount;
        private int activeStackCount;

        private CellItemEntry(IAEItemStack available) {
            prototype = available.copy()
                .setStackSize(0);
            availableAmount = available.getStackSize();
            displayStack = available.getItemStack();
        }

        private void prepareExposedStacks(int allocatedSegments, List<ItemStack> destination) {
            initialExposedAmount = Math.min(availableAmount, (long) Integer.MAX_VALUE * allocatedSegments);
            activeStackCount = initialExposedAmount == 0 ? 0
                : (int) ((initialExposedAmount - 1) / Integer.MAX_VALUE + 1);
            // Reuse these stacks so their changed sizes show how many items were used.
            while (exposedStacks.size() < activeStackCount) exposedStacks.add(displayStack.copy());

            long remaining = initialExposedAmount;
            for (int i = 0; i < activeStackCount; i++) {
                ItemStack exposed = exposedStacks.get(i);
                exposed.stackSize = (int) Math.min(remaining, Integer.MAX_VALUE);
                remaining -= exposed.stackSize;
                destination.add(exposed);
            }
        }

        private long getRemainingExposedAmount() {
            long amount = 0;
            for (int i = 0; i < activeStackCount; i++) amount += Math.max(0, exposedStacks.get(i).stackSize);
            return amount;
        }

        private void finishRecipeProcessing() {
            initialExposedAmount = 0;
            activeStackCount = 0;
        }
    }
}
