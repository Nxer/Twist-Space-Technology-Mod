package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_AEStorageCellHatchGui;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstSharedFormat;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.util.Platform;
import appeng.util.item.AEFluidStack;
import appeng.util.item.FluidList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class TST_AEStorageCellInputHatch extends MTEHatchInputME implements ITSTSegmentedFluidInput, ISaveProvider {

    private static final int CELL_SLOT = 0;
    private static final BaseActionSource CELL_ACTION_SOURCE = new BaseActionSource();
    private static final FluidTankInfo[] EMPTY_TANK_INFO = new FluidTankInfo[0];

    private final CellFluidEntry[] selectedContents = new CellFluidEntry[TST_AEStorageCellHelper.MAX_TYPES];
    private final List<FluidStack> exposedFluids = new ArrayList<>();
    private final long[] pullableAmounts = new long[TST_AEStorageCellHelper.MAX_TYPES];
    private final boolean[] displayedThisRecipe = new boolean[TST_AEStorageCellHelper.MAX_TYPES];
    private int[] segmentAllocations = new int[TST_AEStorageCellHelper.MAX_TYPES];
    private final int configuredTier;
    private boolean storageCellPresent;
    private boolean usingCellDuringRecipe;
    private boolean exposedInputsPrepared;
    private boolean longExtractionUsed;
    private boolean suppressRecipeSegments;
    private boolean showRecipeRemainder;
    private long lastGuiAmountRefreshTick = Long.MIN_VALUE;

    public TST_AEStorageCellInputHatch(int id, String name, String nameRegional, int tier) {
        super(id, true, name, nameRegional);
        configuredTier = tier;
    }

    public TST_AEStorageCellInputHatch(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, true, tier, description, textures);
        configuredTier = tier;
    }

    private static String[] createDescription() {
        return new String[] { TextLocalization.HatchTier + " " + TstSharedFormat.getTierName(VoltageIndex.UIV),
            // #tr Tooltip_AEStorageCellInputHatch.3
            // # Advanced stocking input hatch upgrade for multiblock fluid input
            // #zh_CN 进阶存储输入仓的升级版，为多方块机器输入流体
            TextEnums.tr("Tooltip_AEStorageCellInputHatch.3"),
            // #tr Tooltip_AEStorageCellInputHatch.0
            // # Retrieves up to 16 marked fluid types directly from the ME network
            // #zh_CN 直接从ME网络拉取至多16种已标记流体
            TextEnums.tr("Tooltip_AEStorageCellInputHatch.0"),
            // #tr Tooltip_AEStorageCellInputHatch.2
            // # An inserted ME fluid storage cell supplies fluids instead and disconnects the ME network
            // #zh_CN 放入ME流体存储元件后改从元件中拉取，且无法连接ME网络
            TextEnums.tr("Tooltip_AEStorageCellInputHatch.2"),
            // #tr Tooltip_AEStorageCellInputHatch.1
            // # Processes up to %s x 2147483647 L of fluid in total per recipe
            // #zh_CN 单次配方合计最多处理%s × 2147483647 L流体
            TextEnums.tr("Tooltip_AEStorageCellInputHatch.1", Config.MaxTotalIntSegments_AEStorageCellInput),
            TextEnums.Author_Goderium.getText(), ModNameDesc };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TST_AEStorageCellInputHatch(mName, configuredTier, mDescriptionArray, mTextures);
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
        updateStorageCellState();
        updateCellAwareProxySides();
        if (getProxy().getNode() != null) getProxy().getNode()
            .updateState();
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (slot != CELL_SLOT) return;
        showRecipeRemainder = false;
        markDirty();
        updateStorageCellState();
        updateCellAwareProxySides();
        if (getProxy().getNode() != null) getProxy().getNode()
            .updateState();
        if (!processingRecipe && autoPullFluidList && getBaseMetaTileEntity().isServerSide()) {
            clearSlotConfigs();
            if (hasStorageCell()) refreshCellFluidList();
        }
        if (!processingRecipe) updateAllInformationSlots();
    }

    private void updateCellAwareProxySides() {
        // When a cell is inserted, disconnect this hatch from the ME network.
        if (hasStorageCell()) {
            getProxy().setValidSides(java.util.EnumSet.noneOf(ForgeDirection.class));
        } else {
            super.onFacingChange();
        }
    }

    @Override
    public void onFacingChange() {
        updateCellAwareProxySides();
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer player,
        float x, float y, float z, ItemStack tool) {
        return !hasStorageCell() && super.onWireCutterRightClick(side, wrenchingSide, player, x, y, z, tool);
    }

    @Override
    public void setConnectsToAllSides(boolean connects) {
        super.setConnectsToAllSides(connects);
        updateCellAwareProxySides();
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        boolean pasted = super.pasteCopiedData(player, nbt);
        if (!pasted) return false;
        updateCellAwareProxySides();
        if (getProxy().getNode() != null) getProxy().getNode()
            .updateState();
        return true;
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
    public void setAutoPullFluidList(boolean pullFluidList) {
        if (!hasStorageCell()) {
            super.setAutoPullFluidList(pullFluidList);
        } else if (autoPullAvailable && getBaseMetaTileEntity().isServerSide() && autoPullFluidList != pullFluidList) {
            autoPullFluidList = pullFluidList;
            clearSlotConfigs();
            if (autoPullFluidList) refreshCellFluidList();
            updateAllInformationSlots();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long timer) {
        // The parent reads the network for auto-pull; read the cell instead when inserted.
        boolean cellAutoPull = base.isServerSide() && hasStorageCell() && autoPullFluidList;
        if (cellAutoPull) autoPullFluidList = false;
        try {
            super.onPostTick(base, timer);
        } finally {
            if (cellAutoPull) autoPullFluidList = true;
        }
        if (cellAutoPull && timer % getAutoPullRefreshTime() == 0) refreshCellFluidList();
    }

    private void refreshCellFluidList() {
        if (!isAllowedToWork()) return;
        IMEInventoryHandler<IAEFluidStack> inventory = getCellInventory();
        if (inventory == null) return;

        int index = 0;
        for (IAEFluidStack available : inventory.getAvailableItems(new FluidList(), 0)) {
            if (index >= slots.length) break;
            if (available.getStackSize() < minAutoPullAmount) continue;
            FluidStack stack = available.getFluidStack();
            if (stack == null) continue;
            setSlotConfig(index, GTUtility.copyAmount(1, stack));
            slots[index].extracted = stack;
            slots[index].extractedAmount = stack.amount;
            index++;
        }
        Arrays.fill(slots, index, slots.length, null);
        Arrays.fill(selectedContents, index, selectedContents.length, null);
    }

    @Override
    public void setSlotConfig(int index, FluidStack config) {
        Slot previous = index >= 0 && index < slots.length ? slots[index] : null;
        boolean changed = previous == null ? config != null : !GTUtility.areFluidsEqual(previous.config, config);
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
        Arrays.fill(pullableAmounts, 0);
        Arrays.fill(segmentAllocations, 0);
        showRecipeRemainder = false;
        lastGuiAmountRefreshTick = Long.MIN_VALUE;
    }

    public long getPullableFluidAmount(int index) {
        if (index < 0 || index >= pullableAmounts.length) return 0;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        // Refresh all 16 GUI slots together, at most once every 20 ticks.
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
        return pullableAmounts[index];
    }

    /** Create one view during checkProcessing, after GT starts recipe processing for the hatches. */
    public static final class LongFluidInputs {

        private final List<FluidStack> ordinaryInputs;
        private final Map<Fluid, List<FluidSource>> longInputs = new HashMap<>();
        private final Set<Fluid> fluidTypes = new LinkedHashSet<>();
        private final Map<Fluid, Long> availableAmounts = new HashMap<>();

        public static LongFluidInputs of(MTEMultiBlockBase machine) {
            return new LongFluidInputs(machine, Optional.empty());
        }

        public static LongFluidInputs forColor(MTEMultiBlockBase machine, byte color) {
            return new LongFluidInputs(machine, Optional.of(color));
        }

        private LongFluidInputs(MTEMultiBlockBase machine, Optional<Byte> color) {
            List<TST_AEStorageCellInputHatch> longHatches = new ArrayList<>();
            for (MTEHatchInput hatch : GTUtility.filterValidMTEs(machine.mInputHatches)) {
                if (!(hatch instanceof TST_AEStorageCellInputHatch longHatch)) continue;
                byte hatchColor = hatch.getColor();
                if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
                if (!longHatch.processingRecipe) throw new IllegalStateException("Recipe processing has not started");
                longHatches.add(longHatch);
            }
            if (longHatches.isEmpty()) {
                ordinaryInputs = machine.getStoredFluidsForColor(color);
            } else {
                // Hide this hatch's int segments while GT gathers the other fluid inputs.
                for (TST_AEStorageCellInputHatch hatch : longHatches) hatch.suppressRecipeSegments = true;
                try {
                    ordinaryInputs = machine.getStoredFluidsForColor(color);
                    filterOverlappingMEInputs(ordinaryInputs, machine.mInputHatches, longHatches, color);
                } finally {
                    for (TST_AEStorageCellInputHatch hatch : longHatches) hatch.suppressRecipeSegments = false;
                }
            }
            for (FluidStack fluid : ordinaryInputs) {
                if (fluid != null && fluid.amount > 0 && fluid.getFluid() != null) fluidTypes.add(fluid.getFluid());
            }
            for (TST_AEStorageCellInputHatch hatch : longHatches) {
                Set<Fluid> marked = Collections.newSetFromMap(new IdentityHashMap<>());
                for (Slot slot : hatch.slots) {
                    FluidStack fluid = slot == null ? null : slot.config;
                    if (fluid == null || fluid.getFluid() == null || !marked.add(fluid.getFluid())) continue;
                    FluidStack config = fluid.copy();
                    config.amount = 1;
                    longInputs.computeIfAbsent(fluid.getFluid(), ignored -> new ArrayList<>())
                        .add(new FluidSource(hatch, config));
                    fluidTypes.add(fluid.getFluid());
                }
            }
        }

        public List<Fluid> getFluidTypes() {
            List<Fluid> types = new ArrayList<>();
            for (Fluid fluid : fluidTypes) {
                if (getAmount(fluid) > 0) types.add(fluid);
            }
            return types;
        }

        public Fluid getFirstAvailableFluid(Set<Fluid> allowedFluids) {
            for (Fluid fluid : fluidTypes) {
                if (allowedFluids.contains(fluid) && getAmount(fluid) > 0) return fluid;
            }
            return null;
        }

        public long getAmount(Fluid fluid) {
            if (fluid == null) return 0;
            return availableAmounts.computeIfAbsent(fluid, this::queryAmount);
        }

        private long queryAmount(Fluid fluid) {
            long available = 0;
            for (FluidStack stack : ordinaryInputs) {
                if (stack != null && stack.getFluid() == fluid && stack.amount > 0) {
                    available = addSaturated(available, stack.amount);
                }
            }
            // Hatches on the same ME network see the same fluid; storage cells are separate sources.
            Set<IGrid> countedNetworks = Collections.newSetFromMap(new IdentityHashMap<>());
            for (FluidSource source : longInputs.getOrDefault(fluid, Collections.emptyList())) {
                IGrid network = networkOf(source.hatch);
                if (network != null && countedNetworks.contains(network)) continue;
                long amount = simulateLong(source.hatch, source.config);
                if (amount <= 0) continue;
                if (network != null) countedNetworks.add(network);
                available = addSaturated(available, amount);
            }
            return available;
        }

        /** Returns the actual amount taken; the network may change after getAmount. */
        public long extract(Fluid fluid, long amount) {
            if (fluid == null || amount <= 0) return 0;
            long remaining = amount;
            for (FluidStack stack : ordinaryInputs) {
                if (remaining == 0) break;
                if (stack == null || stack.getFluid() != fluid || stack.amount <= 0) continue;
                int taken = (int) Math.min(remaining, stack.amount);
                stack.amount -= taken;
                remaining -= taken;
            }
            for (FluidSource source : longInputs.getOrDefault(fluid, Collections.emptyList())) {
                if (remaining == 0) break;
                remaining -= extractLong(source.hatch, source.config, remaining);
            }
            long taken = amount - remaining;
            if (availableAmounts.containsKey(fluid)) {
                availableAmounts.put(fluid, Math.max(0, availableAmounts.get(fluid) - taken));
            }
            return taken;
        }

        private static void filterOverlappingMEInputs(List<FluidStack> fluids, List<MTEHatchInput> inputHatches,
            List<TST_AEStorageCellInputHatch> longHatches, Optional<Byte> color) {
            Map<Fluid, FluidStack> otherMEFluids = new HashMap<>();
            Set<FluidStack> overlappingStacks = Collections.newSetFromMap(new IdentityHashMap<>());
            for (MTEHatchInput hatch : GTUtility.filterValidMTEs(inputHatches)) {
                if (!(hatch instanceof MTEHatchInputME meHatch) || hatch instanceof TST_AEStorageCellInputHatch)
                    continue;
                byte hatchColor = hatch.getColor();
                if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
                for (FluidStack fluid : meHatch.getStoredFluids()) {
                    if (fluid == null) continue;
                    if (isSuppliedByLongInput(meHatch, fluid, longHatches)) overlappingStacks.add(fluid);
                    else otherMEFluids.put(fluid.getFluid(), fluid);
                }
            }
            // Keep GT's selected ME stacks unless one shares the long input's network.
            for (ListIterator<FluidStack> iterator = fluids.listIterator(); iterator.hasNext();) {
                FluidStack fluid = iterator.next();
                if (!overlappingStacks.contains(fluid)) continue;
                FluidStack replacement = otherMEFluids.get(fluid.getFluid());
                if (replacement == null) iterator.remove();
                else iterator.set(replacement);
            }
        }

        private static boolean isSuppliedByLongInput(MTEHatchInputME meHatch, FluidStack fluid,
            List<TST_AEStorageCellInputHatch> longHatches) {
            try {
                IGrid network = meHatch.getProxy()
                    .getGrid();
                if (network == null) return false;
                for (TST_AEStorageCellInputHatch hatch : longHatches) {
                    if (networkOf(hatch) == network && simulateLong(hatch, fluid) > 0) return true;
                }
            } catch (GridAccessException ignored) {}
            return false;
        }

        private static IGrid networkOf(TST_AEStorageCellInputHatch hatch) {
            if (hatch.hasStorageCell() || !hatch.getProxy()
                .isActive()) return null;
            try {
                return hatch.getProxy()
                    .getGrid();
            } catch (GridAccessException ignored) {
                return null;
            }
        }

        private static long simulateLong(TST_AEStorageCellInputHatch hatch, FluidStack fluid) {
            int index = findSlot(hatch, fluid);
            if (!canUse(hatch) || index < 0) return 0;
            boolean useCell = hatch.hasStorageCell();
            try {
                IMEInventory<IAEFluidStack> inventory = hatch.getAvailableInventory(useCell);
                if (inventory == null) return 0;
                IAEFluidStack found = inventory.extractItems(
                    AEFluidStack.create(fluid)
                        .setStackSize(Long.MAX_VALUE),
                    Actionable.SIMULATE,
                    hatch.getAvailableSource(useCell));
                if (found != null && found.getStackSize() > 0
                    && hatch.processingRecipe
                    && !hatch.exposedInputsPrepared
                    && !hatch.displayedThisRecipe[index]) {
                    hatch.selectedContents[index] = new CellFluidEntry(found);
                    hatch.pullableAmounts[index] = found.getStackSize();
                    hatch.slots[index].extracted = found.copy()
                        .setStackSize(Math.min(found.getStackSize(), Integer.MAX_VALUE))
                        .getFluidStack();
                    hatch.slots[index].extractedAmount = hatch.slots[index].extracted.amount;
                    hatch.displayedThisRecipe[index] = true;
                }
                return found == null ? 0 : Math.max(0, found.getStackSize());
            } catch (GridAccessException ignored) {
                return 0;
            }
        }

        private static long extractLong(TST_AEStorageCellInputHatch hatch, FluidStack fluid, long amount) {
            int index = findSlot(hatch, fluid);
            if (amount <= 0 || !canUse(hatch) || index < 0) return 0;
            if (hatch.processingRecipe && !hatch.exposedInputsPrepared && !hatch.displayedThisRecipe[index]) {
                simulateLong(hatch, fluid);
            }
            boolean useCell = hatch.hasStorageCell();
            try {
                IMEInventory<IAEFluidStack> inventory = hatch.getAvailableInventory(useCell);
                if (inventory == null) return 0;
                IAEFluidStack request = AEFluidStack.create(fluid)
                    .setStackSize(amount);
                IAEFluidStack extracted = useCell
                    ? inventory.extractItems(request, Actionable.MODULATE, CELL_ACTION_SOURCE)
                    : Platform.poweredExtraction(
                        hatch.getProxy()
                            .getEnergy(),
                        inventory,
                        request,
                        hatch.getAvailableSource(false));
                long pulled = extracted == null ? 0 : Math.max(0, Math.min(amount, extracted.getStackSize()));
                if (pulled > 0) {
                    if (useCell) hatch.markDirty();
                    if (hatch.processingRecipe) {
                        hatch.longExtractionUsed = true;
                        hatch.reduceDisplayedAmount(index, pulled);
                    }
                    hatch.lastGuiAmountRefreshTick = Long.MIN_VALUE;
                }
                return pulled;
            } catch (GridAccessException ignored) {
                return 0;
            }
        }

        private static boolean canUse(TST_AEStorageCellInputHatch hatch) {
            IGregTechTileEntity base = hatch.getBaseMetaTileEntity();
            return base != null && base.isServerSide() && hatch.isAllowedToWork();
        }

        private static int findSlot(TST_AEStorageCellInputHatch hatch, FluidStack fluid) {
            if (fluid == null || fluid.amount <= 0) return -1;
            for (int i = 0; i < hatch.slots.length; i++) {
                Slot slot = hatch.slots[i];
                if (slot != null && slot.config != null && GTUtility.areFluidsEqual(slot.config, fluid)) return i;
            }
            return -1;
        }

        private static long addSaturated(long first, long second) {
            return first > Long.MAX_VALUE - second ? Long.MAX_VALUE : first + second;
        }

        private static final class FluidSource {

            private final TST_AEStorageCellInputHatch hatch;
            private final FluidStack config;

            private FluidSource(TST_AEStorageCellInputHatch hatch, FluidStack config) {
                this.hatch = hatch;
                this.config = config;
            }
        }
    }

    private void reduceDisplayedAmount(int index, long amount) {
        CellFluidEntry entry = selectedContents[index];
        if (entry == null) return;
        entry.availableAmount = Math.max(0, entry.availableAmount - amount);
        pullableAmounts[index] = entry.availableAmount;
        Slot slot = slots[index];
        if (slot != null && slot.extracted != null) {
            slot.extractedAmount = (int) Math.min(entry.availableAmount, Integer.MAX_VALUE);
            slot.extracted.amount = slot.extractedAmount;
        }
        showRecipeRemainder = true;
    }

    private void prepareRecipeInputsIfNeeded() {
        if (!processingRecipe || exposedInputsPrepared || longExtractionUsed || !cachedActivity) return;
        // Special machines can pull long directly without building the GT int stacks.
        exposedInputsPrepared = true;
        refreshSelectedContents(usingCellDuringRecipe);
        prepareExposedFluids();
    }

    @Override
    public void updateInformationSlot(int index) throws GridAccessException {
        if (!hasStorageCell()) {
            super.updateInformationSlot(index);
            return;
        }

        Slot slot = index < 0 || index >= slots.length ? null : slots[index];
        if (slot == null) return;
        IMEInventoryHandler<IAEFluidStack> inventory = getCellInventory();
        IAEFluidStack result = inventory == null ? null
            : inventory.extractItems(
                AEFluidStack.create(slot.config)
                    .setStackSize(Long.MAX_VALUE),
                Actionable.SIMULATE,
                CELL_ACTION_SOURCE);
        slot.extracted = result == null ? null
            : result.copy()
                .setStackSize(Math.min(result.getStackSize(), Integer.MAX_VALUE))
                .getFluidStack();
        slot.extractedAmount = slot.extracted == null ? 0 : slot.extracted.amount;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == CELL_SLOT && !processingRecipe ? mInventory[CELL_SLOT] : null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (!processingRecipe && slot == CELL_SLOT && (stack == null || isFluidCell(stack))) {
            super.setInventorySlotContents(slot, stack);
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return slot == CELL_SLOT && !processingRecipe ? super.decrStackSize(slot, amount) : null;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] { CELL_SLOT };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return !processingRecipe && slot == CELL_SLOT && isFluidCell(stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return !processingRecipe && slot == CELL_SLOT;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity base, int slot, ForgeDirection side, ItemStack stack) {
        return !processingRecipe && slot == CELL_SLOT && isFluidCell(stack);
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
        if (mInventory[CELL_SLOT] != null && mInventory[CELL_SLOT].stackSize <= 0) {
            mInventory[CELL_SLOT] = null;
            onContentsChanged(CELL_SLOT);
        }
    }

    @Override
    public FluidStack[] getStoredFluids() {
        if (processingRecipe && suppressRecipeSegments) return new FluidStack[0];
        // Use the recipe stacks only while a recipe is running.
        prepareRecipeInputsIfNeeded();
        return processingRecipe ? exposedFluids.toArray(new FluidStack[0]) : super.getStoredFluids();
    }

    @Override
    public List<FluidStack> getTSTStoredFluidSegments() {
        if (processingRecipe && suppressRecipeSegments) return Collections.emptyList();
        prepareRecipeInputsIfNeeded();
        return exposedFluids;
    }

    @Override
    public FluidStack getFluid() {
        if (!processingRecipe) return null;
        prepareRecipeInputsIfNeeded();
        for (FluidStack exposed : exposedFluids) {
            if (exposed.amount > 0) return exposed;
        }
        return null;
    }

    @Override
    public int getFluidAmount() {
        FluidStack fluid = getFluid();
        return fluid == null ? 0 : fluid.amount;
    }

    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return processingRecipe;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack fluid) {
        return false;
    }

    @Override
    public int fill(FluidStack fluid, boolean doFill) {
        return 0;
    }

    @Override
    public int fill(ForgeDirection side, FluidStack fluid, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (!processingRecipe || maxDrain <= 0) return null;
        prepareRecipeInputsIfNeeded();
        for (CellFluidEntry entry : selectedContents) {
            if (entry != null && entry.getRemainingExposedAmount() > 0) return drainEntry(entry, maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack fluid, boolean doDrain) {
        return drain(side, fluid, fluid == null ? 0 : fluid.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack fluid, int amount, boolean doDrain) {
        // The recipe uses UNKNOWN; fluid pipes on a side cannot drain these stacks.
        if (!processingRecipe || side != ForgeDirection.UNKNOWN || fluid == null || amount <= 0) return null;
        prepareRecipeInputsIfNeeded();
        CellFluidEntry entry = getMatchingEntry(fluid);
        return entry == null ? null : drainEntry(entry, amount, doDrain);
    }

    @Override
    public boolean canDrain(ForgeDirection side, Fluid fluid) {
        if (processingRecipe && side == ForgeDirection.UNKNOWN && fluid != null) prepareRecipeInputsIfNeeded();
        return processingRecipe && side == ForgeDirection.UNKNOWN
            && fluid != null
            && hasExposedFluid(new FluidStack(fluid, 1));
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (processingRecipe && side == ForgeDirection.UNKNOWN) prepareRecipeInputsIfNeeded();
        if (!processingRecipe || side != ForgeDirection.UNKNOWN || exposedFluids.isEmpty()) return EMPTY_TANK_INFO;
        FluidTankInfo[] result = new FluidTankInfo[exposedFluids.size()];
        for (int i = 0; i < exposedFluids.size(); i++) {
            result[i] = new FluidTankInfo(exposedFluids.get(i), Integer.MAX_VALUE);
        }
        return result;
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
        if (!cachedActivity) {
            exposedFluids.clear();
            for (CellFluidEntry entry : selectedContents) {
                if (entry != null) entry.finishRecipeProcessing();
            }
            return;
        }
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        CheckRecipeResult result = CheckRecipeResultRegistry.SUCCESSFUL;
        IMEInventory<IAEFluidStack> inventory = null;
        IEnergyGrid energy = null;
        try {
            if (usingCellDuringRecipe) {
                inventory = getCellInventory();
            } else {
                if (!getProxy().isReady()) getProxy().onReady();
                inventory = getProxy().getStorage()
                    .getFluidInventory();
                energy = getProxy().getEnergy();
            }
        } catch (GridAccessException ignored) {}

        // Request only the fluid used by the recipe from the cell or network.
        for (int i = 0; i < selectedContents.length; i++) {
            CellFluidEntry entry = selectedContents[i];
            if (entry == null) continue;
            long amount = entry.initialExposedAmount - entry.getRemainingExposedAmount();
            if (amount > 0) {
                IAEFluidStack request = entry.prototype.copy()
                    .setStackSize(amount);
                IAEFluidStack extracted = null;
                if (inventory != null && (usingCellDuringRecipe || energy != null)) {
                    extracted = usingCellDuringRecipe
                        ? inventory.extractItems(request, Actionable.MODULATE, CELL_ACTION_SOURCE)
                        : Platform.poweredExtraction(energy, inventory, request, getAvailableSource(false));
                }
                if (extracted == null || extracted.getStackSize() != amount) {
                    controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                    result = SimpleCheckRecipeResult.ofFailurePersistOnShutdown("stocking_hatch_fail_extraction");
                }
                reduceDisplayedAmount(i, amount);
            }
            entry.finishRecipeProcessing();
        }

        processingRecipe = false;
        exposedFluids.clear();
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

        List<CellFluidEntry> entries = new ArrayList<>();
        for (CellFluidEntry entry : selectedContents) {
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
                stack.setString("Name", entry.displayStack.getLocalizedName());
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
                "GT5U.waila.hatch.outputme.fluid_cache_capacity",
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
        MTEHatchOutputMEBase.WailaHelper.getWailaAdvancedBody("fluid", currentTip, accessor);
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new TST_AEStorageCellHatchGui.FluidHatch(this, slots).build(guiData, syncManager, uiSettings);
    }

    public static boolean isFluidCell(ItemStack stack) {
        return TST_AEStorageCellHelper.isCellForChannel(stack, StorageChannel.FLUIDS);
    }

    private boolean hasStorageCell() {
        return storageCellPresent;
    }

    private void updateStorageCellState() {
        ItemStack cell = mInventory[CELL_SLOT];
        storageCellPresent = cell != null && cell.stackSize == 1 && isFluidCell(cell);
    }

    private IMEInventoryHandler<IAEFluidStack> getCellInventory() {
        return TST_AEStorageCellHelper.getCellInventory(mInventory[CELL_SLOT], this, StorageChannel.FLUIDS);
    }

    private IMEInventory<IAEFluidStack> getAvailableInventory(boolean useCell) throws GridAccessException {
        if (useCell) return getCellInventory();
        if (!getProxy().isActive()) return null;
        return getProxy().getStorage()
            .getFluidInventory();
    }

    private BaseActionSource getAvailableSource(boolean useCell) {
        if (useCell) return CELL_ACTION_SOURCE;
        if (requestSource == null) requestSource = new appeng.api.networking.security.MachineSource(
            (appeng.api.networking.security.IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    private void refreshSelectedContents(boolean useCell) {
        IMEInventory<IAEFluidStack> inventory;
        try {
            inventory = getAvailableInventory(useCell);
        } catch (GridAccessException ignored) {
            inventory = null;
        }
        if (inventory == null) {
            for (CellFluidEntry entry : selectedContents) {
                if (entry != null) entry.availableAmount = 0;
            }
            clearExtractedStacks();
            Arrays.fill(pullableAmounts, 0);
            Arrays.fill(segmentAllocations, 0);
            return;
        }

        int selectedSlotCount = Math.min(slots.length, selectedContents.length);
        for (int i = 0; i < selectedSlotCount; i++) {
            Slot slot = slots[i];
            if (slot == null || slot.config == null) {
                selectedContents[i] = null;
                continue;
            }
            if (containsSelectedFluidBefore(i, slot.config)) {
                selectedContents[i] = null;
                slot.resetExtracted();
                continue;
            }
            IAEFluidStack simulated = inventory.extractItems(
                AEFluidStack.create(slot.config)
                    .setStackSize(Long.MAX_VALUE),
                Actionable.SIMULATE,
                getAvailableSource(useCell));
            if (simulated == null || simulated.getStackSize() <= 0) {
                CellFluidEntry entry = selectedContents[i];
                if (entry != null && GTUtility.areFluidsEqual(entry.displayStack, slot.config)) {
                    entry.availableAmount = 0;
                } else {
                    selectedContents[i] = null;
                }
                slot.resetExtracted();
                continue;
            }
            CellFluidEntry entry = selectedContents[i];
            if (entry == null || !GTUtility.areFluidsEqual(entry.displayStack, slot.config)) {
                selectedContents[i] = new CellFluidEntry(simulated);
            } else {
                entry.availableAmount = simulated.getStackSize();
            }
            slot.extracted = simulated.copy()
                .setStackSize(Math.min(simulated.getStackSize(), Integer.MAX_VALUE))
                .getFluidStack();
            slot.extractedAmount = slot.extracted.amount;
        }
        Arrays.fill(selectedContents, selectedSlotCount, selectedContents.length, null);
        updatePullableAmounts();
    }

    private void updatePullableAmounts() {
        long[] availableAmounts = new long[selectedContents.length];
        for (int i = 0; i < selectedContents.length; i++) {
            if (selectedContents[i] != null) availableAmounts[i] = selectedContents[i].availableAmount;
        }
        // GT recipes still share the int segment limit; the GUI shows the full long amount.
        segmentAllocations = TST_AEStorageCellHelper
            .distributeIntSegments(availableAmounts, Config.MaxTotalIntSegments_AEStorageCellInput);
        for (int i = 0; i < pullableAmounts.length; i++) {
            pullableAmounts[i] = availableAmounts[i];
        }
    }

    private boolean containsSelectedFluidBefore(int index, FluidStack fluid) {
        for (int i = 0; i < index; i++) {
            Slot slot = slots[i];
            if (slot != null && GTUtility.areFluidsEqual(slot.config, fluid)) return true;
        }
        return false;
    }

    private void prepareExposedFluids() {
        exposedFluids.clear();
        for (int i = 0; i < selectedContents.length; i++) {
            if (selectedContents[i] != null)
                selectedContents[i].prepareExposedFluids(segmentAllocations[i], exposedFluids);
        }
    }

    private FluidStack drainEntry(CellFluidEntry entry, int amount, boolean doDrain) {
        int toDrain = (int) Math.min(amount, entry.getRemainingExposedAmount());
        if (toDrain <= 0) return null;
        FluidStack result = entry.displayStack.copy();
        result.amount = toDrain;
        if (doDrain) {
            int remaining = toDrain;
            for (int i = 0; i < entry.activeStackCount && remaining > 0; i++) {
                FluidStack exposed = entry.exposedFluids.get(i);
                int drained = Math.min(remaining, exposed.amount);
                exposed.amount -= drained;
                remaining -= drained;
            }
        }
        return result;
    }

    private CellFluidEntry getMatchingEntry(FluidStack fluid) {
        if (fluid == null) return null;
        for (CellFluidEntry entry : selectedContents) {
            if (entry != null && entry.displayStack.isFluidEqual(fluid)) return entry;
        }
        return null;
    }

    private boolean hasExposedFluid(FluidStack fluid) {
        CellFluidEntry entry = getMatchingEntry(fluid);
        return entry != null && entry.getRemainingExposedAmount() > 0;
    }

    private static final class CellFluidEntry {

        private final IAEFluidStack prototype;
        private final FluidStack displayStack;
        private final List<FluidStack> exposedFluids = new ArrayList<>();
        private long availableAmount;
        private long initialExposedAmount;
        private int activeStackCount;

        private CellFluidEntry(IAEFluidStack available) {
            prototype = available.copy()
                .setStackSize(0);
            availableAmount = available.getStackSize();
            displayStack = available.getFluidStack();
        }

        private void prepareExposedFluids(int allocatedSegments, List<FluidStack> destination) {
            initialExposedAmount = Math.min(availableAmount, (long) Integer.MAX_VALUE * allocatedSegments);
            activeStackCount = initialExposedAmount == 0 ? 0
                : (int) ((initialExposedAmount - 1) / Integer.MAX_VALUE + 1);
            // Reuse these stacks so their changed amounts show how much fluid was used.
            while (exposedFluids.size() < activeStackCount) exposedFluids.add(displayStack.copy());

            long remaining = initialExposedAmount;
            for (int i = 0; i < activeStackCount; i++) {
                FluidStack exposed = exposedFluids.get(i);
                exposed.amount = (int) Math.min(remaining, Integer.MAX_VALUE);
                remaining -= exposed.amount;
                destination.add(exposed);
            }
        }

        private long getRemainingExposedAmount() {
            long amount = 0;
            for (int i = 0; i < activeStackCount; i++) amount += Math.max(0, exposedFluids.get(i).amount);
            return amount;
        }

        private void finishRecipeProcessing() {
            initialExposedAmount = 0;
            activeStackCount = 0;
        }
    }
}
