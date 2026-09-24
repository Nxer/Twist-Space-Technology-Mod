package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.validMTEList;
import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.LongPredicate;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.ITSTSegmentedFluidInput;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.ITSTSegmentedItemInput;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit;
import com.cleanroommc.modularui.drawable.UITexture;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;

import appeng.api.storage.data.IAEFluidStack;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;

public abstract class GTCM_MultiMachineBase<T extends GTCM_MultiMachineBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> implements IConstructable, ISurvivalConstructable, TSTTooltipCredit {

    // region Class Constructor
    public GTCM_MultiMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_MultiMachineBase(String aName) {
        super(aName);
    }

    // endregion

    // region new methods
    public void repairMachine() {
        mHardHammer = true;
        mSoftMallet = true;
        mScrewdriver = true;
        mCrowbar = true;
        mSolderingTool = true;
        mWrench = true;
    }

    // endregion

    // region Processing Logic

    /**
     * Default parameters. If these parameter is only confirmed by machine structure, they should be calculated in
     * {@link #checkMachine(IGregTechTileEntity, ItemStack, List)}.
     */
    protected boolean enablePerfectOverclock = false;
    protected int maxParallel = 1;
    protected float euModifier = 1;
    protected float speedBonus = 1;

    /** Enables long ME outputs. */
    private boolean enableMEOutput = false;

    /** One long entry per item or fluid kind. */
    protected final List<ItemStackLong> meOutputQueue = new ArrayList<>();
    protected final List<FluidStackLong> meFluidOutputQueue = new ArrayList<>();

    private static final String ME_ITEM_OUTPUTS_NBT = "tstMEItemOutputs";
    private static final String ME_FLUID_OUTPUTS_NBT = "tstMEFluidOutputs";
    private static final String ME_OUTPUT_STACK_NBT = "stack";
    private static final String ME_OUTPUT_AMOUNT_NBT = "amount";
    private static final String WAILA_ME_ITEM_LENGTH = "tstMEItemLength";
    private static final String WAILA_ME_FLUID_LENGTH = "tstMEFluidLength";
    private static final String WAILA_ME_ITEM_ICON = "tstMEItemIcon";
    private static final String WAILA_ME_ITEM_NAME = "tstMEItemName";
    private static final String WAILA_ME_ITEM_COUNT = "tstMEItemCount";
    private static final String WAILA_ME_FLUID_ICON = "tstMEFluidIcon";
    private static final String WAILA_ME_FLUID_NAME = "tstMEFluidName";
    private static final String WAILA_ME_FLUID_COUNT = "tstMEFluidCount";

    @Desugar
    public record ItemStackLong(ItemStack itemStack, long stackSize) {}

    @Desugar
    public record FluidStackLong(FluidStack fluidStack, long amount) {}

    public boolean isMEOutputEnabled() {
        if (enableMEOutput) return true;
        boolean hasMEOutput = false;
        for (MTEHatchOutputBus outputBus : validMTEList(mOutputBusses)) {
            if (!(outputBus instanceof MTEHatchOutputBusME)) return false;
            hasMEOutput = true;
        }
        for (MTEHatchOutput outputHatch : validMTEList(mOutputHatches)) {
            if (!(outputHatch instanceof MTEHatchOutputME)) return false;
            hasMEOutput = true;
        }
        return hasMEOutput;
    }

    public void setMEOutput(boolean enabled) {
        enableMEOutput = enabled;
    }

    public List<ItemStackLong> getMEItemOutputInfo() {
        return Collections.unmodifiableList(meOutputQueue);
    }

    public List<FluidStackLong> getMEFluidOutputInfo() {
        return Collections.unmodifiableList(meFluidOutputQueue);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("enablePerfectOverclock", enablePerfectOverclock);
        aNBT.setInteger("maxParallel", maxParallel);
        aNBT.setFloat("euModifier", euModifier);
        aNBT.setFloat("speedBonus", speedBonus);
        saveMEOutputQueues(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        enablePerfectOverclock = aNBT.getBoolean("enablePerfectOverclock");
        maxParallel = Math.max(aNBT.getInteger("maxParallel"), 1);
        euModifier = aNBT.getFloat("euModifier");
        if (euModifier <= 0) {
            euModifier = 1;
        }
        speedBonus = aNBT.getFloat("speedBonus");
        if (speedBonus <= 0) {
            speedBonus = 1;
        }
        loadMEOutputQueues(aNBT);
    }

    private void saveMEOutputQueues(NBTTagCompound nbt) {
        NBTTagList itemOutputs = new NBTTagList();
        for (ItemStackLong entry : meOutputQueue) {
            if (entry.itemStack() == null || entry.stackSize() <= 0) continue;
            NBTTagCompound output = new NBTTagCompound();
            output.setTag(
                ME_OUTPUT_STACK_NBT,
                entry.itemStack()
                    .writeToNBT(new NBTTagCompound()));
            output.setLong(ME_OUTPUT_AMOUNT_NBT, entry.stackSize());
            itemOutputs.appendTag(output);
        }
        nbt.setTag(ME_ITEM_OUTPUTS_NBT, itemOutputs);

        NBTTagList fluidOutputs = new NBTTagList();
        for (FluidStackLong entry : meFluidOutputQueue) {
            if (entry.fluidStack() == null || entry.amount() <= 0) continue;
            NBTTagCompound output = new NBTTagCompound();
            output.setTag(
                ME_OUTPUT_STACK_NBT,
                entry.fluidStack()
                    .writeToNBT(new NBTTagCompound()));
            output.setLong(ME_OUTPUT_AMOUNT_NBT, entry.amount());
            fluidOutputs.appendTag(output);
        }
        nbt.setTag(ME_FLUID_OUTPUTS_NBT, fluidOutputs);
    }

    private void loadMEOutputQueues(NBTTagCompound nbt) {
        meOutputQueue.clear();
        NBTTagList itemOutputs = nbt.getTagList(ME_ITEM_OUTPUTS_NBT, TAG_COMPOUND);
        for (int i = 0; i < itemOutputs.tagCount(); i++) {
            NBTTagCompound output = itemOutputs.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(output.getCompoundTag(ME_OUTPUT_STACK_NBT));
            long amount = output.getLong(ME_OUTPUT_AMOUNT_NBT);
            if (stack != null && amount > 0) meOutputQueue.add(new ItemStackLong(stack, amount));
        }

        meFluidOutputQueue.clear();
        NBTTagList fluidOutputs = nbt.getTagList(ME_FLUID_OUTPUTS_NBT, TAG_COMPOUND);
        for (int i = 0; i < fluidOutputs.tagCount(); i++) {
            NBTTagCompound output = fluidOutputs.getCompoundTagAt(i);
            FluidStack stack = FluidStack.loadFluidStackFromNBT(output.getCompoundTag(ME_OUTPUT_STACK_NBT));
            long amount = output.getLong(ME_OUTPUT_AMOUNT_NBT);
            if (stack != null && amount > 0) meFluidOutputQueue.add(new FluidStackLong(stack, amount));
        }
    }

    /**
     * Creates logic to run recipe check based on recipemap. This runs only once, on class instantiation.
     * <p>
     * If this machine doesn't use recipemap or does some complex things, override {@link #checkProcessing()}.
     */
    @ApiStatus.OverrideOnly
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    /**
     * Proxy Perfect Overclock Supplier.
     *
     * @return If true, enable Perfect Overclock.
     */
    @ApiStatus.OverrideOnly
    protected boolean isEnablePerfectOverclock() {
        return enablePerfectOverclock;
    }

    /**
     * Proxy Standard Eu Modifier Supplier.
     *
     * @return The value (or a method to get the value) of Eu Modifier (dynamically) .
     */
    @ApiStatus.OverrideOnly
    protected float getEuModifier() {
        return euModifier;
    }

    /**
     * Proxy Standard Speed Multiplier Supplier.
     *
     * @return The value (or a method to get the value) of Speed Multiplier (dynamically) .
     */
    @ApiStatus.OverrideOnly
    protected float getSpeedBonus() {
        return speedBonus;
    }

    /**
     * Proxy Standard Parallel Supplier.
     *
     * @return The value (or a method to get the value) of Max Parallel (dynamically) .
     */
    @ApiStatus.OverrideOnly
    public int getMaxParallelRecipes() {
        return maxParallel;
    }

    /**
     *
     * @return The voltage tier should be in of this machine total EU/t, allow over MAX calculation.
     */
    public int getTotalPowerTier() {
        return TSTUtils.getMachineTotalPowerTier(this);
    }

    /**
     * Gets the voltage tier of the energy hatch that provides the most available EU/t. This includes normal and exotic
     * energy hatches. If multiple hatches provide the same EU/t, the hatch with the higher input voltage is selected.
     *
     * @return The dominant energy hatch voltage tier, or 0 when no valid energy hatch is installed.
     */
    public int getDominantInputVoltageTier() {
        long dominantInputPower = 0;
        long dominantInputVoltage = 0;
        int dominantInputTier = 0;
        for (MTEHatch energyHatch : validMTEList(getExoticAndNormalEnergyHatchList())) {
            long inputVoltage = energyHatch.getBaseMetaTileEntity()
                .getInputVoltage();
            long inputAmperage = energyHatch.maxWorkingAmperesIn();
            if (inputVoltage <= 0 || inputAmperage <= 0) continue;

            long inputPower = inputVoltage > Long.MAX_VALUE / inputAmperage ? Long.MAX_VALUE
                : inputVoltage * inputAmperage;
            if (inputPower > dominantInputPower
                || (inputPower == dominantInputPower && inputVoltage > dominantInputVoltage)) {
                dominantInputPower = inputPower;
                dominantInputVoltage = inputVoltage;
                dominantInputTier = (int) energyHatch.getInputTier();
            }
        }
        return dominantInputTier;
    }

    /**
     * Remove machine efficiency.
     *
     * @return Eu consumption per tick.
     */
    @Override
    protected long getActualEnergyUsage() {
        return -this.lEUt;
    }

    /**
     * Checks recipe and setup machine if it's successful.
     * <p>
     * For generic machine working with recipemap, use {@link #createProcessingLogic()} to make use of shared codebase.
     */
    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        // If no logic is found, try legacy checkRecipe
        if (processingLogic == null) {
            return checkRecipe(mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }

        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) {
            return result;
        }

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        if (isMEOutputEnabled()) {
            if (processingLogic instanceof GTCM_ProcessingLogic tstLogic && tstLogic.hasLongOutputs()) {
                replaceMEOutputQueues(tstLogic.getLongItemOutputs(), tstLogic.getLongFluidOutputs());
            } else {
                replaceMEOutputQueues(processingLogic.getOutputItems(), processingLogic.getOutputFluids());
            }
            // Avoid syncing split int stacks through GT's normal output arrays.
            mOutputItems = null;
            mOutputFluids = null;
        } else {
            mOutputItems = processingLogic.getOutputItems();
            mOutputFluids = processingLogic.getOutputFluids();
        }

        return result;
    }

    private void replaceMEOutputQueues(List<ItemStackLong> itemOutputs, List<FluidStackLong> fluidOutputs) {
        clearMEOutputQueues();
        for (ItemStackLong output : itemOutputs) {
            mergeItemIntoMEOutputQueue(output.itemStack(), output.stackSize());
        }
        for (FluidStackLong output : fluidOutputs) {
            mergeFluidIntoMEOutputQueue(output.fluidStack(), output.amount());
        }
    }

    private void replaceMEOutputQueues(ItemStack[] itemOutputs, FluidStack[] fluidOutputs) {
        clearMEOutputQueues();
        if (itemOutputs != null) {
            for (ItemStack stack : itemOutputs) {
                if (stack == null || stack.stackSize <= 0) continue;
                mergeItemIntoMEOutputQueue(stack);
            }
        }
        if (fluidOutputs != null) {
            for (FluidStack fluid : fluidOutputs) {
                if (fluid == null || fluid.amount <= 0) continue;
                mergeFluidIntoMEOutputQueue(fluid);
            }
        }
    }

    protected void mergeItemIntoMEOutputQueue(ItemStack stack) {
        if (stack == null) return;
        mergeItemIntoMEOutputQueue(stack, stack.stackSize);
    }

    protected void mergeItemIntoMEOutputQueue(ItemStack stack, long amount) {
        if (stack == null || amount <= 0) return;
        for (int i = 0; i < meOutputQueue.size(); i++) {
            ItemStackLong entry = meOutputQueue.get(i);
            if (GTUtility.areStacksEqual(entry.itemStack(), stack)) {
                long merged = entry.stackSize() > Long.MAX_VALUE - amount ? Long.MAX_VALUE : entry.stackSize() + amount;
                meOutputQueue.set(i, new ItemStackLong(entry.itemStack(), merged));
                return;
            }
        }
        meOutputQueue.add(new ItemStackLong(GTUtility.copyAmountUnsafe(1, stack), amount));
    }

    protected void mergeFluidIntoMEOutputQueue(FluidStack fluid) {
        if (fluid == null) return;
        mergeFluidIntoMEOutputQueue(fluid, fluid.amount);
    }

    protected void mergeFluidIntoMEOutputQueue(FluidStack fluid, long amount) {
        if (fluid == null || amount <= 0) return;
        for (int i = 0; i < meFluidOutputQueue.size(); i++) {
            FluidStackLong entry = meFluidOutputQueue.get(i);
            if (entry.fluidStack()
                .isFluidEqual(fluid)) {
                long merged = entry.amount() > Long.MAX_VALUE - amount ? Long.MAX_VALUE : entry.amount() + amount;
                meFluidOutputQueue.set(i, new FluidStackLong(entry.fluidStack(), merged));
                return;
            }
        }
        FluidStack template = fluid.copy();
        template.amount = 1;
        meFluidOutputQueue.add(new FluidStackLong(template, amount));
    }

    protected void clearMEOutputQueues() {
        meOutputQueue.clear();
        meFluidOutputQueue.clear();
    }

    protected CheckRecipeResult checkMEOutputCapacity(List<ItemStackLong> itemOutputs,
        List<FluidStackLong> fluidOutputs) {
        if (protectsExcessItem() && !canFitMEItemOutputs(itemOutputs)) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }
        if (protectsExcessFluid() && !canFitMEFluidOutputs(fluidOutputs)) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private boolean canFitMEItemOutputs(List<ItemStackLong> outputs) {
        if (outputs == null || outputs.isEmpty()) return true;
        List<MTEHatchOutputBusME> meBusses = getMEOutputBusses();
        Map<MTEHatchOutputBusME, Long> reserved = new HashMap<>();
        for (ItemStackLong output : outputs) {
            if (output.itemStack() == null || output.stackSize() <= 0) continue;
            long remaining = output.stackSize();
            for (MTEHatchOutputBusME meBus : meBusses) {
                var provider = meBus.getProvider();
                long room = getLongCacheRoom(provider.getCachedAmount(), reserved.getOrDefault(meBus, 0L));
                if (room <= 0) continue;
                long accepted = Math.min(remaining, room);
                if (provider.shouldCheckCell()) {
                    accepted = findLargestAcceptedAmount(
                        accepted,
                        value -> provider.canStore(output.itemStack(), value));
                } else if (!provider.hasAvailableSpace() || !provider.getFilter()
                    .isAllowed(output.itemStack())) {
                        continue;
                    }
                if (accepted <= 0) continue;
                reserved.merge(meBus, accepted, Long::sum);
                remaining -= accepted;
                if (remaining <= 0) break;
            }
            if (remaining > 0) return false;
        }
        return true;
    }

    private boolean canFitMEFluidOutputs(List<FluidStackLong> outputs) {
        if (outputs == null || outputs.isEmpty()) return true;
        List<MTEHatchOutputME> meHatches = getMEOutputHatches();
        Map<MTEHatchOutputME, Long> reserved = new HashMap<>();
        for (FluidStackLong output : outputs) {
            if (output.fluidStack() == null || output.amount() <= 0) continue;
            long remaining = output.amount();
            for (MTEHatchOutputME meHatch : meHatches) {
                MTEHatchOutputMEBase<IAEFluidStack> provider = meHatch.getProvider();
                long room = getLongCacheRoom(provider.getCachedAmount(), reserved.getOrDefault(meHatch, 0L));
                if (room <= 0) continue;
                long accepted = Math.min(remaining, room);
                if (provider.shouldCheckCell()) {
                    accepted = findLargestAcceptedAmount(
                        accepted,
                        value -> provider.canStore(output.fluidStack(), value));
                } else if (!provider.canAcceptAnyInput() || !provider.getFilter()
                    .isAllowed(output.fluidStack())) {
                        continue;
                    }
                if (accepted <= 0) continue;
                reserved.merge(meHatch, accepted, Long::sum);
                remaining -= accepted;
                if (remaining <= 0) break;
            }
            if (remaining > 0) return false;
        }
        return true;
    }

    /**
     * <p>
     * Get inputting items without DualInputHatch, and no separation mode.
     * <p>
     * Always used to get some special input items.
     *
     * @return The inputting items.
     */
    public ArrayList<ItemStack> getStoredInputsWithoutDualInputHatch() {

        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchInputBus tHatch : GTUtility.filterValidMTEs(mInputBusses)) {
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    rList.add(itemStack);
                }
            }
        }

        if (getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            rList.add(getStackInSlot(1));
        }
        return rList;
    }

    public ArrayList<ItemStack> getStoredInputItemsWithDualInputHatch() {

        if (supportsCraftingMEBuffer() && !mDualInputHatches.isEmpty()) {
            for (IDualInputHatch dualInputHatch : mDualInputHatches) {
                Iterator<? extends IDualInputInventory> inventoryIterator = dualInputHatch.inventories();
                while (inventoryIterator.hasNext()) {
                    ItemStack[] items = inventoryIterator.next()
                        .getItemInputs();
                    if (items == null || items.length == 0) {
                        continue;
                    }

                    ArrayList<ItemStack> rList = new ArrayList<>();
                    for (int i = 0; i < items.length; i++) {
                        if (items[i] != null) {
                            rList.add(items[i]);
                        }
                    }
                    return rList;
                }
            }
        }

        ArrayList<ItemStack> rList = new ArrayList<>();
        for (MTEHatchInputBus tHatch : GTUtility.filterValidMTEs(mInputBusses)) {
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    rList.add(itemStack);
                }
            }
        }

        if (getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            rList.add(getStackInSlot(1));
        }
        return rList;
    }

    /**
     * Forced get all input items, include all Dual Input Hatch slot.
     *
     * @return The items list.
     */
    public ArrayList<ItemStack> getStoredInputsNoSeparation() {
        ArrayList<ItemStack> rList = new ArrayList<>();

        if (supportsCraftingMEBuffer()) {
            for (IDualInputHatch dualInputHatch : mDualInputHatches) {
                Iterator<? extends IDualInputInventory> inventoryIterator = dualInputHatch.inventories();
                while (inventoryIterator.hasNext()) {
                    ItemStack[] items = inventoryIterator.next()
                        .getItemInputs();
                    if (items == null || items.length == 0) {
                        continue;
                    }

                    for (int i = 0; i < items.length; i++) {
                        if (items[i] != null) {
                            rList.add(items[i]);
                        }
                    }

                }
            }
        }

        Map<GTUtility.ItemId, ItemStack> inputsFromME = new HashMap<>();
        for (MTEHatchInputBus tHatch : GTUtility.filterValidMTEs(mInputBusses)) {
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            boolean isMEBus = tHatch instanceof MTEHatchInputBusME;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        rList.add(itemStack);
                    }
                }
            }
        }

        if (getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            rList.add(getStackInSlot(1));
        }
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        appendSegmentedItemInputs(rList, Optional.empty());
        return rList;
    }

    /**
     * Forced get all input fluids, include all Dual Input Hatch slot.
     *
     * @return ArrayList of all fluid stacks, contains fluid stacks in Crafting Input Hatch.
     */
    public ArrayList<FluidStack> getStoredFluidsWithDualInput() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        Map<Fluid, FluidStack> inputsFromME = new HashMap<>();
        for (MTEHatchInput tHatch : GTUtility.filterValidMTEs(mInputHatches)) {
            setHatchRecipeMap(tHatch);
            if (tHatch instanceof MTEHatchMultiInput multiInputHatch) {
                for (FluidStack tFluid : multiInputHatch.getStoredFluid()) {
                    if (tFluid != null) {
                        rList.add(tFluid);
                    }
                }
            } else if (tHatch instanceof MTEHatchInputME meHatch) {
                for (FluidStack fluidStack : meHatch.getStoredFluids()) {
                    if (fluidStack != null) {
                        // Prevent the same fluid from different ME hatches from being recognized
                        inputsFromME.put(fluidStack.getFluid(), fluidStack);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    rList.add(tHatch.getFillableStack());
                }
            }
        }

        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        appendSegmentedFluidInputs(rList, Optional.empty());

        // get all fluids from Dual input
        if (supportsCraftingMEBuffer()) {
            for (IDualInputHatch dualInputHatch : mDualInputHatches) {
                Iterator<? extends IDualInputInventory> inventoryIterator = dualInputHatch.inventories();
                while (inventoryIterator.hasNext()) {
                    FluidStack[] fluids = inventoryIterator.next()
                        .getFluidInputs();
                    if (fluids == null || fluids.length == 0) {
                        continue;
                    }

                    for (int i = 0; i < fluids.length; i++) {
                        if (fluids[i] != null && fluids[i].amount > 0) {
                            rList.add(fluids[i]);
                        }
                    }

                }
            }
        }

        return rList;
    }

    // GT keeps one stack per ME type; TST machines add the remaining int-sized segments back.
    private void appendSegmentedItemInputs(List<ItemStack> inputs, Optional<Byte> color) {
        Set<ItemStack> includedSegments = null;
        for (MTEHatchInputBus bus : GTUtility.filterValidMTEs(mInputBusses)) {
            if (!(bus instanceof ITSTSegmentedItemInput segmentedInput)) continue;
            byte busColor = bus.getColor();
            if (color.isPresent() && busColor != -1 && busColor != color.get()) continue;
            if (includedSegments == null) {
                includedSegments = Collections.newSetFromMap(new IdentityHashMap<>());
                includedSegments.addAll(inputs);
            }
            for (ItemStack segment : segmentedInput.getTSTStoredItemSegments()) {
                if (segment != null && !includedSegments.remove(segment)) inputs.add(segment);
            }
        }
    }

    private void appendSegmentedFluidInputs(List<FluidStack> inputs, Optional<Byte> color) {
        Set<FluidStack> includedSegments = null;
        for (MTEHatchInput hatch : GTUtility.filterValidMTEs(mInputHatches)) {
            if (!(hatch instanceof ITSTSegmentedFluidInput segmentedInput)) continue;
            byte hatchColor = hatch.getColor();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            if (includedSegments == null) {
                includedSegments = Collections.newSetFromMap(new IdentityHashMap<>());
                includedSegments.addAll(inputs);
            }
            for (FluidStack segment : segmentedInput.getTSTStoredFluidSegments()) {
                if (segment != null && !includedSegments.remove(segment)) inputs.add(segment);
            }
        }
    }

    // region Overrides
    @Override
    protected void outputAfterRecipe() {
        if (isMEOutputEnabled()) {
            // Keep long outputs outside GT's int-sized output arrays.
            outputMEItemQueue(meOutputQueue);
            outputMEFluidQueue(meFluidOutputQueue);
            clearMEOutputQueues();
        }
        super.outputAfterRecipe();
    }

    protected void outputMEItemQueue(List<ItemStackLong> outputs) {
        for (ItemStackLong entry : outputs) {
            outputItemToMENetwork(entry.itemStack(), entry.stackSize());
        }
    }

    protected void outputMEFluidQueue(List<FluidStackLong> outputs) {
        for (FluidStackLong entry : outputs) {
            outputFluidToMENetwork(entry.fluidStack(), entry.amount());
        }
    }

    protected long outputItemToMENetwork(ItemStack item, long amount) {
        if (item == null || amount <= 0) return amount;
        long remaining = amount;
        for (MTEHatchOutputBusME meBus : getMEOutputBusses()) {
            var provider = meBus.getProvider();
            long transfer = Math.min(remaining, getLongCacheRoom(provider.getCachedAmount(), 0));
            if (transfer <= 0) continue;
            if (provider.shouldCheckCell()) {
                transfer = findLargestAcceptedAmount(transfer, value -> provider.canStore(item, value));
            } else if (!provider.canAcceptAnyInput() || !provider.getFilter()
                .isAllowed(item)) {
                    continue;
                }
            if (transfer <= 0) continue;
            provider.storeToCache(
                provider.getFilter()
                    .fromNative(GTUtility.copyAmountUnsafe(1, item))
                    .setStackSize(transfer));
            meBus.markDirty();
            remaining -= transfer;
            if (remaining <= 0) break;
        }
        return remaining;
    }

    protected long outputFluidToMENetwork(FluidStack fluid, long amount) {
        if (fluid == null || amount <= 0) return amount;
        long remaining = amount;
        for (MTEHatchOutputME meHatch : getMEOutputHatches()) {
            var provider = meHatch.getProvider();
            long transfer = Math.min(remaining, getLongCacheRoom(provider.getCachedAmount(), 0));
            if (transfer <= 0) continue;
            if (provider.shouldCheckCell()) {
                transfer = findLargestAcceptedAmount(transfer, value -> provider.canStore(fluid, value));
            } else if (!provider.canAcceptAnyInput() || !provider.getFilter()
                .isAllowed(fluid)) {
                    continue;
                }
            if (transfer <= 0) continue;
            FluidStack template = fluid.copy();
            template.amount = 1;
            provider.storeToCache(
                provider.getFilter()
                    .fromNative(template)
                    .setStackSize(transfer));
            meHatch.markDirty();
            remaining -= transfer;
            if (remaining <= 0) break;
        }
        return remaining;
    }

    private List<MTEHatchOutputBusME> getMEOutputBusses() {
        List<MTEHatchOutputBusME> result = new ArrayList<>();
        for (MTEHatchOutputBus outputBus : validMTEList(mOutputBusses)) {
            if (outputBus instanceof MTEHatchOutputBusME meBus) result.add(meBus);
        }
        result.sort(
            Comparator.comparingInt(
                bus -> bus.getBusType()
                    .ordinal()));
        return result;
    }

    private List<MTEHatchOutputME> getMEOutputHatches() {
        List<MTEHatchOutputME> result = new ArrayList<>();
        for (MTEHatchOutput outputHatch : validMTEList(mOutputHatches)) {
            if (outputHatch instanceof MTEHatchOutputME meHatch) result.add(meHatch);
        }
        result.sort(Comparator.comparingInt(hatch -> hatch.isFluidLocked() ? 0 : 1));
        return result;
    }

    private static long getLongCacheRoom(long cached, long reserved) {
        if (cached < 0 || reserved < 0 || cached >= Long.MAX_VALUE - reserved) return 0;
        return Long.MAX_VALUE - cached - reserved;
    }

    private static long findLargestAcceptedAmount(long maximum, LongPredicate canStore) {
        if (maximum <= 0) return 0;
        if (canStore.test(maximum)) return maximum;
        long low = 0;
        long high = maximum;
        while (low < high) {
            long middle = low + ((high - low) >>> 1) + 1;
            if (canStore.test(middle)) low = middle;
            else high = middle - 1;
        }
        return low;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        clearMEOutputQueues();
        super.stopMachine(reason);
    }

    @Override
    public void startRecipeProcessing() {
        super.startRecipeProcessing();
        for (MTEHatchInputBus bus : GTUtility.filterValidMTEs(mInputBusses)) {
            if (bus instanceof ITSTSegmentedItemInput segmentedInput) segmentedInput.setTSTSegmentedInputMode();
        }
        for (MTEHatchInput hatch : GTUtility.filterValidMTEs(mInputHatches)) {
            if (hatch instanceof ITSTSegmentedFluidInput segmentedInput) segmentedInput.setTSTSegmentedInputMode();
        }
    }

    @Override
    public ArrayList<ItemStack> getStoredInputsForColor(Optional<Byte> color) {
        ArrayList<ItemStack> inputs = super.getStoredInputsForColor(color);
        appendSegmentedItemInputs(inputs, color);
        return inputs;
    }

    @Override
    public ArrayList<ItemStack> getAllStoredInputs() {
        ArrayList<ItemStack> inputs = super.getAllStoredInputs();
        appendSegmentedItemInputs(inputs, Optional.empty());
        return inputs;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluidsForColor(Optional<Byte> color) {
        ArrayList<FluidStack> inputs = super.getStoredFluidsForColor(color);
        appendSegmentedFluidInputs(inputs, color);
        return inputs;
    }

    @Override
    public String[] getInfoData() {
        String dSpeed = String.format("%.3f", this.getSpeedBonus() * 100) + "%";
        String dEUMod = String.format("%.3f", this.getEuModifier() * 100) + "%";
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 4];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + TSTSharedLocalization.MachineInfo.Parallels
            + ": "
            + EnumChatFormatting.GOLD
            + this.getTrueParallel();
        ret[origin.length + 1] = EnumChatFormatting.AQUA + TSTSharedLocalization.MachineInfo.SpeedMultiplier
            + ": "
            + EnumChatFormatting.GOLD
            + dSpeed;
        ret[origin.length + 2] = EnumChatFormatting.AQUA + TSTSharedLocalization.MachineInfo.EuModifier
            + ": "
            + EnumChatFormatting.GOLD
            + dEUMod;
        ret[origin.length + 3] = EnumChatFormatting.AQUA + TSTSharedLocalization.MachineInfo.HighCapacityOutput
            + ": "
            + EnumChatFormatting.GOLD
            + (isMEOutputEnabled() ? "On" : "Off");
        return ret;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addEnergyHatchOrExoticEnergyHatchToMachineList(IGregTechTileEntity aTileEntity,
        int aBaseCasingIndex) {
        return addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addInputBusOrOutputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addInputBusToMachineList(aTileEntity, aBaseCasingIndex)
            || addOutputBusToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addInputHatchOrOutputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addInputHatchToMachineList(aTileEntity, aBaseCasingIndex)
            || addOutputHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addFluidInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = getRecipeMap();
            addIfSmartInput(aMetaTileEntity);
            return mInputHatches.add((MTEHatchInput) aMetaTileEntity);
        } else if (aMetaTileEntity instanceof MTEHatchMuffler) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMufflerHatches.add((MTEHatchMuffler) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean addEnergyOutput(long aEU) {
        if (aEU <= 0) {
            return true;
        }
        if (!mDynamoHatches.isEmpty()) {
            return addEnergyOutputMultipleDynamos(aEU, true);
        }
        return false;
    }

    @Override
    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long totalOutput = 0;
        long aFirstVoltageFound = -1;
        boolean aFoundMixedDynamos = false;
        for (MTEHatchDynamo aDynamo : validMTEList(mDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            long aTotal = aDynamo.maxAmperesOut() * aVoltage;
            // Check against voltage to check when hatch mixing
            if (aFirstVoltageFound == -1) {
                aFirstVoltageFound = aVoltage;
            } else {
                if (aFirstVoltageFound != aVoltage) {
                    aFoundMixedDynamos = true;
                }
            }
            totalOutput += aTotal;
        }
        for (MTEHatch aDynamo : validMTEList(mExoticDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            long aTotal = aDynamo.maxAmperesOut() * aVoltage;
            // Check against voltage to check when hatch mixing
            if (aFirstVoltageFound == -1) {
                aFirstVoltageFound = aVoltage;
            } else {
                if (aFirstVoltageFound != aVoltage) {
                    aFoundMixedDynamos = true;
                }
            }
            totalOutput += aTotal;
        }

        // if (totalOutput < aEU || (aFoundMixedDynamos && !aAllowMixedVoltageDynamos)) {
        // explodeMultiblock();
        // return false;
        // }

        long leftToInject;
        long aVoltage;
        int aAmpsToInject;
        int aRemainder;
        int ampsOnCurrentHatch;
        for (MTEHatch aDynamo : validMTEList(mDynamoHatches)) {
            leftToInject = aEU - injected;
            aVoltage = aDynamo.maxEUOutput();
            aAmpsToInject = (int) (leftToInject / aVoltage);
            aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
            ampsOnCurrentHatch = (int) Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);
            for (int i = 0; i < ampsOnCurrentHatch; i++) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aVoltage, false);
            }
            injected += aVoltage * ampsOnCurrentHatch;
            if (aRemainder > 0 && ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aRemainder, false);
                injected += aRemainder;
            }
        }
        for (MTEHatch aDynamo : validMTEList(mExoticDynamoHatches)) {
            leftToInject = aEU - injected;
            aVoltage = aDynamo.maxEUOutput();
            aAmpsToInject = (int) (leftToInject / aVoltage);
            aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
            ampsOnCurrentHatch = (int) Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);
            for (int i = 0; i < ampsOnCurrentHatch; i++) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aVoltage, false);
            }
            injected += aVoltage * ampsOnCurrentHatch;
            if (aRemainder > 0 && ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aRemainder, false);
                injected += aRemainder;
            }
        }
        return injected > 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    /**
     * No more machine error
     */
    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    /**
     * No more machine error
     */
    @Override
    public void checkMaintenance() {}

    /**
     * No more machine error
     */
    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    /**
     * No more machine error
     */
    @Override
    public final boolean shouldCheckMaintenance() {
        return false;
    }

    /**
     * Gets the maximum Efficiency that spare Part can get (0 - 10000)
     *
     * @param aStack
     */
    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    /**
     * Gets the damage to the ItemStack, usually 0 or 1.
     *
     * @param aStack
     */
    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    /**
     * no longer afraid of rain
     */
    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean getDefaultBatchMode() {
        if (!supportsBatchMode()) {
            return false;
        }
        return Config.DEFAULT_BATCH_MODE;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    // endregion

    // region Machine Mode

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack tool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if (supportsMachineModeSwitch()) {
                setMachineMode(nextMachineMode());
                GTUtility.sendChatTrans(aPlayer, getMachineModeName());
            } else {
                super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, tool);
            }
        }
    }

    public boolean showModeInWaila() {
        return supportsMachineModeSwitch();
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (isMEOutputEnabled()) {
            tag.setInteger(WAILA_ME_ITEM_LENGTH, meOutputQueue.size());
            tag.setInteger(WAILA_ME_FLUID_LENGTH, meFluidOutputQueue.size());

            int itemCount = Math.min(3, meOutputQueue.size());
            for (int i = 0; i < itemCount; i++) {
                ItemStackLong output = meOutputQueue.get(i);
                tag.setString(WAILA_ME_ITEM_ICON + i, TTRenderStack.create(output.itemStack(), true));
                tag.setString(
                    WAILA_ME_ITEM_NAME + i,
                    output.itemStack()
                        .getDisplayName());
                tag.setLong(WAILA_ME_ITEM_COUNT + i, output.stackSize());
            }

            int fluidCount = Math.min(3 - itemCount, meFluidOutputQueue.size());
            for (int i = 0; i < fluidCount; i++) {
                FluidStackLong output = meFluidOutputQueue.get(i);
                tag.setString(
                    WAILA_ME_FLUID_ICON + i,
                    TTRenderStack.create(GTUtility.getFluidDisplayStack(output.fluidStack(), false), true));
                tag.setString(
                    WAILA_ME_FLUID_NAME + i,
                    output.fluidStack()
                        .getLocalizedName());
                tag.setLong(WAILA_ME_FLUID_COUNT + i, output.amount());
            }
        }
        if (showModeInWaila()) {
            tag.setInteger("modeTST", machineMode);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey(WAILA_ME_ITEM_LENGTH) && tag.getBoolean("isActive")) {
            int itemCount = tag.getInteger(WAILA_ME_ITEM_LENGTH);
            int fluidCount = tag.getInteger(WAILA_ME_FLUID_LENGTH);
            int totalOutputs = itemCount + fluidCount;
            if (totalOutputs > 0) {
                List<String> outputInfo = new ArrayList<>();
                outputInfo.add(StatCollector.translateToLocal("GT5U.waila.producing"));
                if (tag.getBoolean("isLockedToRecipe")) {
                    outputInfo.add(StatCollector.translateToLocal("GT5U.waila.multiblock.status.locked_recipe"));
                }
                int displayedItems = Math.min(3, itemCount);
                for (int i = 0; i < displayedItems; i++) {
                    outputInfo.add(
                        "  " + tag.getString(WAILA_ME_ITEM_ICON + i)
                            + EnumChatFormatting.AQUA
                            + tag.getString(WAILA_ME_ITEM_NAME + i)
                            + EnumChatFormatting.RESET
                            + " x "
                            + EnumChatFormatting.GOLD
                            + formatNumber(tag.getLong(WAILA_ME_ITEM_COUNT + i)));
                }
                int displayedFluids = Math.min(3 - displayedItems, fluidCount);
                for (int i = 0; i < displayedFluids; i++) {
                    outputInfo.add(
                        "  " + tag.getString(WAILA_ME_FLUID_ICON + i)
                            + EnumChatFormatting.AQUA
                            + tag.getString(WAILA_ME_FLUID_NAME + i)
                            + EnumChatFormatting.RESET
                            + " x "
                            + EnumChatFormatting.GOLD
                            + formatNumber(tag.getLong(WAILA_ME_FLUID_COUNT + i))
                            + "L");
                }
                if (totalOutputs > 3) {
                    outputInfo.add(
                        StatCollector
                            .translateToLocalFormatted("GT5U.waila.producing.andmore", formatNumber(totalOutputs - 3)));
                }
                String progress = GTWaila.getMachineProgressString(
                    true,
                    tag.getBoolean("isAllowedToWork"),
                    tag.getInteger("maxProgress"),
                    tag.getInteger("progress"));
                int index = currentTip.lastIndexOf(progress);
                currentTip.addAll(index < 0 ? currentTip.size() : index, outputInfo);
            }
        }
        if (tag.hasKey("modeTST")) {
            currentTip.add(EnumChatFormatting.YELLOW +
            // #tr tst.common.shared.machine_info.running_mode
            // # Running Mode :
            // #zh_CN 运行模式 :
                StatCollector.translateToLocal("tst.common.shared.machine_info.running_mode")
                + " "
                + EnumChatFormatting.WHITE
                + getMachineModeName()
                + EnumChatFormatting.RESET);
        }
    }

    public long getAllDynamoBuffer() {
        long buffer = 0;
        for (MTEHatch tHatch : validMTEList(mDynamoHatches)) {
            buffer += tHatch.getEUVar();
        }
        return buffer;
    }

    public long getAllMaxDynamoBuffer() {
        long buffer = 0;
        for (MTEHatch tHatch : validMTEList(mDynamoHatches)) {
            buffer += tHatch.maxEUStore();
        }
        return buffer;
    }

    public long getDynamoAmperage() {
        long dynamoAmperage = 0;
        for (MTEHatch tHatch : validMTEList(mDynamoHatches)) {
            dynamoAmperage += tHatch.getBaseMetaTileEntity()
                .getOutputAmperage();
        }
        return dynamoAmperage;
    }

    public boolean setDynamoTier(int tier, boolean onlyThisTier) {
        if (onlyThisTier) {
            return mDynamoHatches.stream()
                .allMatch(dynamo -> dynamo.getTierForStructure() == tier);
        }
        return mDynamoHatches.stream()
            .allMatch(dynamo -> dynamo.getTierForStructure() <= tier);
    }

    public boolean checkMixedDynamo() {
        long firstVoltage = -1;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            long aVoltage = tHatch.maxEUOutput();
            if (firstVoltage == -1) {
                firstVoltage = aVoltage;
            } else {
                if (firstVoltage != aVoltage) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCountDynamo(int countAvaliableDynamo) {
        int count = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            count++;
            if (count > countAvaliableDynamo) {
                return false;
            }
        }
        return true;
    }

    public long getTierDynamo() {
        if (!checkMixedDynamo()) {
            return mDynamoHatches.stream()
                .mapToLong(MTEHatchDynamo::maxEUOutput)
                .distinct()
                .reduce((a, b) -> 0)
                .orElse(0);
        }
        return 0;
    }

    // endregion

    // endregion

    // region UI

    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        if (supportsMachineModeSwitch()) {
            return new TST_Gui<>((T) this).withMachineModeIcons(getMachineModeIcons());
        } else {
            return new TST_Gui<>((T) this);
        }

    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return totalMachineMode() > 1;
    }

    public abstract UITexture[] getMachineModeIcons();

    /**
     * Set total mode count for the machine.
     * Also indicate whether this machine has multiple modes.
     * Use {@link #machineMode} to get current machine mode index.
     * Override {@link #getMachineModeName()} to set name for each mode.
     * Override {@link #setMachineModeIcons()} to set button icon.
     * Override {@link #setMachineMode(int)} or {@link #nextMachineMode()} to restrict mode change.
     */
    public int totalMachineMode() {
        return 1;
    }

    @Override
    public String getMachineModeName() {
        return super.getMachineModeName();
    }

    @Override
    public int nextMachineMode() {
        machineMode++;
        if (machineMode >= totalMachineMode()) {
            machineMode = 0;
        }
        return machineMode;
    }

    // endregion

}
