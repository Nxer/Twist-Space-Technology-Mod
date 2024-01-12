// package com.Nxer.TwistSpaceTechnology.common.machine.ultimateMachines;
//
// import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
// import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.UUID_Name;
// import static gregtech.api.enums.TierEU.RECIPE_UMV;
// import static gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME.fluidAEInsert;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.EnumSet;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.Map;
// import java.util.Objects;
// import java.util.Optional;
// import java.util.Queue;
// import java.util.UUID;
// import java.util.concurrent.ConcurrentLinkedQueue;
// import java.util.function.Function;
// import java.util.stream.Collectors;
//
// import javax.annotation.Nonnull;
// import javax.annotation.Nullable;
//
// import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemRecipePackagePattern;
// import net.minecraft.inventory.IInventory;
// import net.minecraft.inventory.InventoryCrafting;
// import net.minecraft.item.ItemStack;
// import net.minecraft.nbt.NBTTagCompound;
// import net.minecraft.util.EnumChatFormatting;
// import net.minecraft.world.World;
// import net.minecraftforge.common.util.ForgeDirection;
// import net.minecraftforge.fluids.FluidStack;
//
// import org.apache.commons.lang3.tuple.Pair;
// import org.jetbrains.annotations.NotNull;
//
// import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
// import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
// import com.Nxer.TwistSpaceTechnology.config.Config;
// import com.glodblock.github.common.item.ItemFluidPacket;
// import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
// import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
// import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
//
// import appeng.api.AEApi;
// import appeng.api.implementations.ICraftingPatternItem;
// import appeng.api.implementations.IPowerChannelState;
// import appeng.api.networking.GridFlags;
// import appeng.api.networking.IGridNode;
// import appeng.api.networking.crafting.ICraftingPatternDetails;
// import appeng.api.networking.crafting.ICraftingProvider;
// import appeng.api.networking.crafting.ICraftingProviderHelper;
// import appeng.api.networking.events.MENetworkCraftingPatternChange;
// import appeng.api.networking.security.BaseActionSource;
// import appeng.api.networking.security.IActionHost;
// import appeng.api.networking.security.MachineSource;
// import appeng.api.storage.IMEMonitor;
// import appeng.api.storage.data.IAEFluidStack;
// import appeng.api.storage.data.IAEItemStack;
// import appeng.api.storage.data.IItemList;
// import appeng.api.util.AECableType;
// import appeng.api.util.DimensionalCoord;
// import appeng.api.util.IInterfaceViewable;
// import appeng.me.GridAccessException;
// import appeng.me.helpers.AENetworkProxy;
// import appeng.me.helpers.IGridProxyable;
// import appeng.util.IWideReadableNumberConverter;
// import appeng.util.Platform;
// import appeng.util.ReadableNumberConverter;
// import gregtech.GT_Mod;
// import gregtech.api.enums.GT_Values;
// import gregtech.api.enums.ItemList;
// import gregtech.api.enums.MaterialsUEVplus;
// import gregtech.api.interfaces.IGlobalWirelessEnergy;
// import gregtech.api.interfaces.ITexture;
// import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
// import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
// import gregtech.api.logic.ProcessingLogic;
// import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
// import gregtech.api.objects.XSTR;
// import gregtech.api.recipe.RecipeMap;
// import gregtech.api.recipe.check.CheckRecipeResult;
// import gregtech.api.recipe.check.CheckRecipeResultRegistry;
// import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
// import gregtech.api.util.GT_OverclockCalculator;
// import gregtech.api.util.GT_ParallelHelper;
// import gregtech.api.util.GT_Recipe;
// import gregtech.api.util.GT_Utility;
// import gregtech.common.tileentities.machines.IDualInputHatch;
// import gregtech.common.tileentities.machines.IDualInputInventory;
//
// public abstract class TST_UltimateMachineBase<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<T>>
// extends GT_MetaTileEntity_EnhancedMultiBlockBase<T> implements IPowerChannelState, ICraftingProvider,
// IGridProxyable, IDualInputHatch, IConstructable, ISurvivalConstructable, IGlobalWirelessEnergy, IInterfaceViewable {
//
// protected RecipeMap<?> recipeMap;
// private static final IItemList<IAEFluidStack> fluidCache = AEApi.instance()
// .storage()
// .createFluidList();
// private static final IItemList<IAEItemStack> itemCache = AEApi.instance()
// .storage()
// .createItemList();
// protected IStructureDefinition<T> structureDefinition;
// protected Queue<PatternRecipePackage> processQueue = new ConcurrentLinkedQueue<>();
// private BaseActionSource requestSource = null;
// private @Nullable AENetworkProxy gridProxy = null;
// private final Map<ICraftingPatternDetails, PatternRecipePackage> patternDetailsPatternSlotMap = new HashMap<>();
// private ArrayList<PatternRecipePackage> patterns = new ArrayList<>();
// private boolean needPatternSync = true;
// private boolean justHadNewItems = false;
//
// private String customName = null;
// private boolean additionalConnection = false;
// private String ownerName; // init when loading world
// private UUID ownerUUID; // init when loading world
// private int dimID; // init when loading world
//
// long lastOutputTick = 0;
// long tickCounter = 0;
// boolean lastOutputFailed = false;
// boolean infiniteCache = true;
//
// public void createRecipe(T machine) {
// if (Config.activateMegaSpaceStation) {
// final RecipeMap<?> stationRecipe = GTCMRecipe.megaUniversalSpaceStationRecipePool;
// GT_Values.RA.stdBuilder()
// .itemInputs(
// ItemList.Hatch_Output_Bus_ME.get(4),
// ItemList.Hatch_Output_ME.get(4),
// ItemList.Hatch_CraftingInput_Bus_ME.get(4),
// GTCMItemList.InfiniteWirelessDynamoHatch.get(1),
// machine.getStackForm(1),
// ItemList.Hatch_AutoMaintenance.get(1))
// .fluidInputs(MaterialsUEVplus.Universium.getFluid(1440))
// .itemOutputs(this.getStackForm(1))
// .noOptimize()
// .eut(RECIPE_UMV)
// .duration(20)
// .addTo(stationRecipe);
// }
// }
//
// protected TST_UltimateMachineBase(int aID, String aName, String aNameRegional) {
// super(aID, aName, aNameRegional);
// // finish Maintenance Hatch
// disableMaintenance = true;
// }
//
// @Override
// public IStructureDefinition<T> getStructureDefinition() {
// return structureDefinition;
// }
//
// public void setStructureDefinition(IStructureDefinition<T> structureDefinition) {
// this.structureDefinition = structureDefinition;
// }
//
// @Override
// protected GT_Multiblock_Tooltip_Builder createTooltip() {
// return null;
// }
//
// @Override
// public void securityBreak() {
//
// }
//
// @Override
// public boolean isBusy() {
// return false;
// }
//
// @Override
// public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
// int colorIndex, boolean active, boolean redstoneLevel) {
// return new ITexture[0];
// }
//
// @Override
// public boolean justUpdated() {
// return false;
// }
//
// @Override
// public Iterator<? extends IDualInputInventory> inventories() {
// return null;
// }
//
// @Override
// public void updateCraftingIcon(ItemStack icon) {
//
// }
//
// @Override
// public Optional<IDualInputInventory> getFirstNonEmptyInventory() {
// return Optional.empty();
// }
//
// @Override
// public boolean supportsFluids() {
// return true;
// }
//
// @Override
// protected ProcessingLogic createProcessingLogic() {
// return new UltimatedProcessLogic();
// }
//
//
// public static class UltimatedProcessLogic extends ProcessingLogic {
//
// protected Queue<Pair<GT_Recipe, Integer>> processQueue;
//
// protected Pair<GT_Recipe, Integer> cachedProcessing = null;
// protected long staticMaxParallel = -1;
//
// /**
// * Executes the recipe check: Find recipe from recipemap, Calculate parallel, overclock and outputs.
// */
// @Nonnull
// public CheckRecipeResult process() {
// if (processQueue.isEmpty()) {
// return CheckRecipeResultRegistry.NO_RECIPE;
// }
// var processing = processQueue.poll();
// cachedProcessing = processing;
// if (processing != null) {
// Integer maxProcess = processing.getValue();
// setMaxParallel((int) Math.min(staticMaxParallel, maxProcess));
// return processRecipe(processing.getKey());
// }
// return CheckRecipeResultRegistry.NO_RECIPE;
// }
//
// /**
// * Checks if supplied recipe is valid for process.
// * If so, additionally performs input consumption, output calculation with parallel, and overclock calculation.
// *
// * @param recipe The recipe which will be checked and processed
// */
// @Nonnull
// protected CheckRecipeResult processRecipe(@Nonnull GT_Recipe recipe) {
// CheckRecipeResult result = validateRecipe(recipe);
// if (!result.wasSuccessful()) {
// return result;
// }
//
// GT_ParallelHelper helper = createParallelHelper(recipe);
// GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
// helper.setCalculator(calculator);
// helper.build();
//
// return applyRecipe(recipe, helper, calculator, result);
// }
//
// @Nonnull
// protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
// return new UltimateParallelHelper().setRecipe(recipe)
// .setAvailableEUt(availableVoltage * availableAmperage)
// .setMachine(machine, protectItems, protectFluids)
// .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
// .setMaxParallel(maxParallel)
// .setEUtModifier(euModifier)
// .enableBatchMode(batchSize)
// .setConsumption(true)
// .setOutputCalculation(true);
// }
//
// /**
// * Applies the recipe and calculated parameters
// */
// private CheckRecipeResult applyRecipe(@NotNull GT_Recipe recipe, GT_ParallelHelper helper,
// GT_OverclockCalculator calculator, CheckRecipeResult result) {
// if (!helper.getResult()
// .wasSuccessful()) {
// return helper.getResult();
// }
//
// calculatedParallels = helper.getCurrentParallel();
//
// if (calculator.getConsumption() == Long.MAX_VALUE) {
// return CheckRecipeResultRegistry.POWER_OVERFLOW;
// }
// if (calculator.getDuration() == Integer.MAX_VALUE) {
// return CheckRecipeResultRegistry.DURATION_OVERFLOW;
// }
//
// calculatedEut = calculator.getConsumption();
//
// double finalDuration = calculateDuration(recipe, helper, calculator);
// if (finalDuration >= Integer.MAX_VALUE) {
// return CheckRecipeResultRegistry.DURATION_OVERFLOW;
// }
// duration = (int) finalDuration;
//
// outputItems = helper.getItemOutputs();
// outputFluids = helper.getFluidOutputs();
// if (cachedProcessing != null) {
// int currentParallels = getCurrentParallels();
// Integer maxProcess = cachedProcessing.getValue();
// if (maxProcess > maxParallel) {
// cachedProcessing.setValue(maxProcess - maxParallel);
// }
// processQueue.add(cachedProcessing);
// }
//
// return result;
// }
//
// }
//
// public static class UltimateParallelHelper extends GT_ParallelHelper {
//
// // region Variables
// private static final double MAX_BATCH_MODE_TICK_TIME = 128;
//
// private GT_Recipe recipe;
// /**
// * The current parallel possible for the multiblock
// */
// private int currentParallel = 0;
// /**
// * The maximum possible parallel possible for the multiblock
// */
// private int maxParallel = 1;
// /**
// * The Batch Modifier applied when batch mode is enabled. 1 does nothing. 2 doubles max possible
// * parallel, but also duration
// */
// private int batchModifier = 1;
// /**
// * The outputs of the recipe with the applied parallel
// */
// private ItemStack[] itemOutputs;
// /**
// * The outputs of the recipe with the applied parallel
// */
// private FluidStack[] fluidOutputs;
//
// private boolean batchMode;
// /**
// * Should the Parallel Helper automatically calculate the outputs of the recipe with current
// * parallel
// */
// private boolean calculateOutputs;
// /**
// * Has the Parallel Helper been built?
// */
// private boolean built;
// /**
// * What is the duration multiplier with batch mode enabled
// */
// private double durationMultiplier;
//
// /**
// * Calculator to use for overclocking
// */
// private GT_OverclockCalculator calculator;
//
// private CheckRecipeResult result = CheckRecipeResultRegistry.NONE;
//
// private Function<Integer, ItemStack[]> customItemOutputCalculation;
//
// private Function<Integer, FluidStack[]> customFluidOutputCalculation;
//
// // endregion
// public UltimateParallelHelper() {
// }
//
// /**
// * Sets the recipe, which will be used for the parallel calculation
// */
// public UltimateParallelHelper setRecipe(@Nonnull GT_Recipe aRecipe) {
// recipe = Objects.requireNonNull(aRecipe);
// return this;
// }
//
// public UltimateParallelHelper setCalculator(GT_OverclockCalculator calculator) {
// this.calculator = calculator;
// return this;
// }
//
// /**
// * Sets the MaxParallel a multi can handle
// */
// public UltimateParallelHelper setMaxParallel(int maxParallel) {
// this.maxParallel = maxParallel;
// return this;
// }
//
// /**
// * Enables Batch mode. Can do up to an additional processed recipes of mCurrentParallel * mBatchModifier A batch
// * modifier of 1 does nothing
// */
// public UltimateParallelHelper enableBatchMode(int batchModifier) {
// this.batchMode = batchModifier > 1;
// this.batchModifier = batchModifier;
// return this;
// }
//
// /**
// * Sets if we should calculate outputs with the parallels we found or not
// *
// * @param calculateOutputs Should we calculate outputs with the helper or not
// */
// public UltimateParallelHelper setOutputCalculation(boolean calculateOutputs) {
// this.calculateOutputs = calculateOutputs;
// return this;
// }
//
// /**
// * Set a custom way to calculate item outputs. You are given the amount of parallels and must return an
// * ItemStack
// * array
// */
// public UltimateParallelHelper setCustomItemOutputCalculation(Function<Integer, ItemStack[]> custom) {
// customItemOutputCalculation = custom;
// return this;
// }
//
// /**
// * Set a custom way to calculate item outputs. You are given the amount of parallels and must return a
// * FluidStack
// * array
// */
// public UltimateParallelHelper setCustomFluidOutputCalculation(Function<Integer, FluidStack[]> custom) {
// customFluidOutputCalculation = custom;
// return this;
// }
//
// // endregion
//
// /**
// * Finishes the GT_ParallelHelper. Anything changed after this will not effect anything
// */
// public UltimateParallelHelper build() {
// if (built) {
// throw new IllegalStateException("Tried to build twice");
// }
// if (recipe == null) {
// throw new IllegalStateException("Recipe is not set");
// }
// built = true;
// determineParallel();
// return this;
// }
//
// /**
// * @return The current parallels possible by the multiblock
// */
// public int getCurrentParallel() {
// if (!built) {
// throw new IllegalStateException("Tried to get parallels before building");
// }
// return currentParallel;
// }
//
// /**
// * @return The duration multiplier if batch mode was enabled for the multiblock
// */
// public double getDurationMultiplierDouble() {
// if (!built) {
// throw new IllegalStateException("Tried to get duration multiplier before building");
// }
// if (batchMode && durationMultiplier > 0) {
// return durationMultiplier;
// }
// return 1;
// }
//
// /**
// * @return The ItemOutputs from the recipe
// */
// @Nonnull
// public ItemStack[] getItemOutputs() {
// if (!built || !calculateOutputs) {
// throw new IllegalStateException(
// "Tried to get item outputs before building or without enabling calculation of outputs");
// }
// return itemOutputs;
// }
//
// /**
// * @return The FluidOutputs from the recipe
// */
// @Nonnull
// public FluidStack[] getFluidOutputs() {
// if (!built || !calculateOutputs) {
// throw new IllegalStateException(
// "Tried to get fluid outputs before building or without enabling calculation of outputs");
// }
// return fluidOutputs;
// }
//
// /**
// * @return The result of why a recipe could've failed or succeeded
// */
// @Nonnull
// public CheckRecipeResult getResult() {
// if (!built) {
// throw new IllegalStateException("Tried to get recipe result before building");
// }
// return result;
// }
//
// /**
// * Called by build(). Determines the parallels and everything else that needs to be done at build time
// */
// protected void determineParallel() {
// long eutUseAfterOC = calculator.calculateEUtConsumptionUnderOneTick(currentParallel, currentParallel);
// calculator.setRecipeEUt(eutUseAfterOC);
// // If Batch Mode is enabled determine how many extra parallels we can get
// if (batchMode && currentParallel > 0 && calculator.getDuration() < MAX_BATCH_MODE_TICK_TIME) {
// double batchMultiplierMax = MAX_BATCH_MODE_TICK_TIME / calculator.getDuration();
// final int tExtraParallels = (int) Math.floor(
// Math.min(
// currentParallel * Math.min(batchMultiplierMax - 1, batchModifier - 1),
// maxParallel - currentParallel));
// durationMultiplier = 1.0f + (float) tExtraParallels / currentParallel;
// currentParallel += tExtraParallels;
// }
// // If we want to calculate outputs we do it here
// if (calculateOutputs && currentParallel > 0) {
// if (recipe.mOutputs != null) {
// calculateItemOutputs();
// }
// if (recipe.mFluidOutputs != null) {
// calculateFluidOutputs();
// }
// }
// result = CheckRecipeResultRegistry.SUCCESSFUL;
// }
//
// protected void calculateItemOutputs() {
// if (customItemOutputCalculation != null) {
// itemOutputs = customItemOutputCalculation.apply(currentParallel);
// return;
// }
// ArrayList<ItemStack> tempItemStack = new ArrayList<>();
// // itemOutputs = new ItemStack[recipe.mOutputs.length];
// for (int i = 0; i < recipe.mOutputs.length; i++) {
// long items = 0;
// long remain = 0;
// int itemStackSize = recipe.getOutput(i).stackSize;
// items = (long) currentParallel * itemStackSize * recipe.getOutputChance(i) / 10000;
// remain = (long) currentParallel * itemStackSize * recipe.getOutputChance(i) % 10000;
// if (remain > XSTR.XSTR_INSTANCE.nextInt(10000)) {
// items += itemStackSize;
// }
// ItemStack origin = recipe.getOutput(i)
// .copy();
// while (items >= Integer.MAX_VALUE) {
// ItemStack itemstack = origin.copy();
// itemstack.stackSize = Integer.MAX_VALUE;
// tempItemStack.add(itemstack);
// items -= Integer.MAX_VALUE;
// }
// ItemStack item = origin.copy();
// item.stackSize = (int) items;
// tempItemStack.add(item);
// }
// itemOutputs = new ItemStack[tempItemStack.size()];
// for (int i = 0; i < tempItemStack.size(); i++) {
// itemOutputs[i] = tempItemStack.get(i);
// }
// }
//
// protected void calculateFluidOutputs() {
// if (customFluidOutputCalculation != null) {
// fluidOutputs = customFluidOutputCalculation.apply(currentParallel);
// return;
// }
// ArrayList<FluidStack> tempFluidStack = new ArrayList<>();
// fluidOutputs = new FluidStack[recipe.mFluidOutputs.length];
// for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
// if (recipe.getFluidOutput(i) == null) {
// continue;
// } else {
// FluidStack tFluid = recipe.getFluidOutput(i)
// .copy();
// long amount = (long) tFluid.amount * currentParallel;
// while (amount >= Integer.MAX_VALUE) {
// FluidStack fluid = tFluid.copy();
// fluid.amount = Integer.MAX_VALUE;
// tempFluidStack.add(fluid);
// amount -= Integer.MAX_VALUE;
// }
// FluidStack fluid = tFluid.copy();
// fluid.amount = (int) amount;
// tempFluidStack.add(fluid);
// }
// }
// fluidOutputs = new FluidStack[tempFluidStack.size()];
// for (int i = 0; i < tempFluidStack.size(); i++) {
// fluidOutputs[i] = tempFluidStack.get(i);
// }
// }
//
// }
//
// @Override
// protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
// if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {
// if (onRunningTick(mInventory[1])) {
// markDirty();
// if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) {
// stopMachine();
// }
// if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
// mEfficiency = Math.max(
// 0,
// Math.min(
// mEfficiency + mEfficiencyIncrease,
// getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
// mOutputItems = null;
// mProgresstime = 0;
// mMaxProgresstime = 0;
// mEfficiencyIncrease = 0;
// if (aBaseMetaTileEntity.isAllowedToWork()) {
// checkRecipe();
// }
// }
// }
// } else {
// if (!processQueue.isEmpty() || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
// || aBaseMetaTileEntity.hasInventoryBeenModified()) {
// if (aBaseMetaTileEntity.isAllowedToWork()) {
// if (checkRecipe()) {
// markDirty();
// }
// }
// if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
// }
// }
// }
//
// @Override
// public boolean depleteInput(FluidStack aLiquid, boolean simulate) {
// return true;
// }
//
// @Override
// public boolean depleteInput(ItemStack stack) {
// return true;
// }
//
// // region AE
// private void updateValidGridProxySides() {
// if (additionalConnection) {
// getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
// } else {
// getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
// }
// }
//
// @Override
// public AENetworkProxy getProxy() {
// if (gridProxy == null) {
// gridProxy = new AENetworkProxy(this, "proxy", ItemList.Hatch_CraftingInput_Bus_ME.get(1), true);
// gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
// updateValidGridProxySides();
// if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
// getBaseMetaTileEntity().getWorld()
// .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
// }
//
// return this.gridProxy;
// }
//
// @Override
// public boolean isActive() {
// return getProxy() != null && getProxy().isActive();
// }
//
// @Override
// public boolean addOutput(ItemStack Stack) {
// if (GT_Utility.isStackInvalid(Stack)) return false;
// itemCache.add(
// AEApi.instance()
// .storage()
// .createItemStack(Stack));
// return true;
// }
//
// @Override
// public boolean addOutput(FluidStack Stack) {
// if (Stack == null) return false;
// fluidCache.add(
// AEApi.instance()
// .storage()
// .createFluidStack(Stack));
// return Stack.amount == 0;
// }
//
// private void flushCachedStack() {
// lastOutputFailed = true;
// AENetworkProxy proxy = getProxy();
// if (proxy == null) {
// lastOutputFailed = true;
// return;
// }
// if (!fluidCache.isEmpty()) {
// try {
// IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
// .getFluidInventory();
// for (IAEFluidStack s : fluidCache) {
// if (s.getStackSize() == 0) continue;
// IAEFluidStack rest = fluidAEInsert(proxy.getEnergy(), sg, s, getRequest());
// if (rest != null && rest.getStackSize() > 0) {
// s.setStackSize(rest.getStackSize());
// continue;
// }
// lastOutputFailed = false;
// s.setStackSize(0);
// }
// } catch (final GridAccessException ignored) {
// lastOutputFailed = true;
// }
// }
// if (!itemCache.isEmpty()) {
// try {
// IMEMonitor<IAEItemStack> sg = proxy.getStorage()
// .getItemInventory();
// for (IAEItemStack s : itemCache) {
// if (s.getStackSize() == 0) continue;
// IAEItemStack rest = Platform.poweredInsert(proxy.getEnergy(), sg, s, getRequest());
// if (rest != null && rest.getStackSize() > 0) {
// lastOutputFailed = true;
// s.setStackSize(rest.getStackSize());
// break;
// }
// s.setStackSize(0);
// }
// } catch (final GridAccessException ignored) {
// lastOutputFailed = true;
// }
// }
// lastOutputTick = tickCounter;
// }
//
// @Override
// public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
// super.onPostTick(aBaseMetaTileEntity, aTimer);
//
// if (getBaseMetaTileEntity().isServerSide()) {
// if (needPatternSync && aTimer % 17 == 0) {
// needPatternSync = !postMEPatternChange();
// }
// if (aTimer % 17 == 0) {
// getBaseMetaTileEntity().setActive(isActive());
// }
// tickCounter = aTimer;
// if (tickCounter > (lastOutputTick + 23)) flushCachedStack();
// }
// }
//
// @Override
// public IGridNode getGridNode(ForgeDirection dir) {
// return getProxy().getNode();
// }
//
// @Override
// public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
// return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
// }
//
// @Override
// public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
// super.onFirstTick(aBaseMetaTileEntity);
// if (aBaseMetaTileEntity.isServerSide()) {
// this.dimID = getDimID(aBaseMetaTileEntity);
// this.ownerName = getOwnerNameAndInitMachine(aBaseMetaTileEntity);
// this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
// }
// getProxy().onReady();
// }
//
// int getDimID(@NotNull IHasWorldObjectAndCoords aBaseMetaTileEntity) {
// return aBaseMetaTileEntity.getWorld().provider.dimensionId;
// }
//
// String getOwnerNameAndInitMachine(@NotNull IGregTechTileEntity aBaseMetaTileEntity) {
// final String ownerName = aBaseMetaTileEntity.getOwnerName();
// final String ownerUUID = aBaseMetaTileEntity.getOwnerUuid()
// .toString();
// if (UUID_Name.isEmpty() || !UUID_Name.containsKey(ownerName) || !UUID_Name.containsKey(ownerUUID)) {
// UUID_Name.put(ownerName, ownerUUID);
// UUID_Name.put(ownerUUID, ownerName);
// markDirty();
// }
// return ownerName;
// }
//
// private boolean postMEPatternChange() {
// // don't post until it's active
// if (!getProxy().isActive()) return false;
// try {
// getProxy().getGrid()
// .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
// } catch (GridAccessException ignored) {
// return false;
// }
// return true;
// }
//
// @Override
// public void onFacingChange() {
// updateValidGridProxySides();
// }
//
// @Override
// public DimensionalCoord getLocation() {
// return new DimensionalCoord(
// getBaseMetaTileEntity().getWorld(),
// getBaseMetaTileEntity().getXCoord(),
// getBaseMetaTileEntity().getYCoord(),
// getBaseMetaTileEntity().getZCoord());
// }
//
// @Override
// public boolean shouldDisplay() {
// return false;
// }
//
// @Override
// public void gridChanged() {
// needPatternSync = true;
// }
//
// @Override
// public boolean isPowered() {
// return getProxy() != null && getProxy().isPowered();
// }
//
// @Override
// public IInventory getPatterns() {
// return this;
// }
//
// private String describePattern(ICraftingPatternDetails patternDetails) {
// return Arrays.stream(patternDetails.getCondensedOutputs())
// .map(
// aeItemStack -> aeItemStack.getItem()
// .getItemStackDisplayName(aeItemStack.getItemStack()))
// .collect(Collectors.joining(", "));
// }
//
// @Override
// public String[] getInfoData() {
// var ret = new ArrayList<String>();
// ret.add(
// "The bus is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
// : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET);
// ret.add("Internal Inventory: ");
// var i = 0;
// for (var slot : patternDetailsPatternSlotMap.values()) {
// if (slot == null) continue;
// IWideReadableNumberConverter nc = ReadableNumberConverter.INSTANCE;
//
// i += 1;
// ret.add(
// "Slot " + i
// + " "
// + EnumChatFormatting.BLUE
// + describePattern(slot.getPatternDetails())
// + EnumChatFormatting.RESET);
// for (var item : slot.cachedRecipe.mInputs) {
// if (item == null || item.stackSize == 0) continue;
// ret.add(
// item.getItem()
// .getItemStackDisplayName(item) + ": "
// + EnumChatFormatting.GOLD
// + nc.toWideReadableForm((long) item.stackSize * slot.recipeCount)
// + EnumChatFormatting.RESET);
// }
// for (var fluid : slot.cachedRecipe.mFluidInputs) {
// if (fluid == null || fluid.amount == 0) continue;
// ret.add(
// fluid.getLocalizedName() + ": "
// + EnumChatFormatting.AQUA
// + nc.toWideReadableForm((long) fluid.amount * slot.recipeCount)
// + EnumChatFormatting.RESET);
// }
// }
// return ret.toArray(new String[0]);
// }
//
// private BaseActionSource getRequest() {
// if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
// return requestSource;
// }
//
// private void onPatternRemove(ItemRecipePackagePattern patternSlot) {
// if (!getBaseMetaTileEntity().isServerSide() || patternSlot == null) return;
// var world = getBaseMetaTileEntity().getWorld();
// // remove old if applicable
// try {
// patternSlot.pattern.refund(getProxy(), getRequest());
//
// } catch (GridAccessException ignored) {
// }
// needPatternSync = true;
// patternDetailsPatternSlotMap.remove(patternSlot.pattern.getPatternDetails());
// //patternSlot.clearPattern();
// }
//
//
//
// @Override
// public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
// if (aStack.getItem() instanceof ItemRecipePackagePattern pattern && pattern.isValidForInsert(this)) {
// ICraftingPatternDetails details = pattern.details;
// var newPackage = new PatternRecipePackage(getRecipeMap(), details, details.getPattern());
// if (newPackage.isValid) {
// patternDetailsPatternSlotMap.put(details, newPackage);
// ((ItemRecipePackagePattern) aStack.getItem()).initPattern(this, newPackage);
// return true;
// }
// return super.canInsertItem(aIndex, aStack, ordinalSide);
// }
// return false;
// }
//
//
// @Override
// public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
// if (!isActive()) return false;
// if (!patternDetailsPatternSlotMap.get(patternDetails)
// .insertItemsAndFluids(table)) {
// return false;
// }
// justHadNewItems = true;
// return true;
// }
//
//
// @Override
// public void provideCrafting(ICraftingProviderHelper craftingTracker) {
// if (!isActive()) return;
//
// for (PatternRecipePackage slot : patternDetailsPatternSlotMap.values()) {
// if (slot == null) continue;
// ICraftingPatternDetails details = slot.getPatternDetails();
// if (details == null) {
// GT_Mod.GT_FML_LOGGER.warn(
// "Found an invalid pattern at " + getBaseMetaTileEntity().getCoords()
// + " in dim "
// + getBaseMetaTileEntity().getWorld().provider.dimensionId);
// continue;
// }
// craftingTracker.addCraftingOption(this, details);
// }
// }
//
// // endregion
//
// }
//// public static class IPatternSlot implements IDualInputInventory {
////
////
//// @Override
//// public ItemStack[] getItemInputs() {
//// return null;
//// }
////
//// @Override
//// public FluidStack[] getFluidInputs() {
//// return null;
//// }
////
//// public interface SharedItemGetter {
////
//// ItemStack[] getSharedItem();
//// }
////
//// public final ItemStack pattern;
//// public final PatternRecipePackage patternDetails;
//// public final SharedItemGetter sharedItemGetter;
////
//// public final GT_Recipe recipe;
//// public int recipeCount;
//// public ItemStack firstItem;
//// public FluidStack firstFluid;
////
//// public IPatternSlot(TST_UltimateMachineBase<?> machine, ItemStack pattern, World world, SharedItemGetter getter,
// GT_Recipe recipe) {
//// this.pattern = pattern;
//// this.recipe = recipe;
//// this.patternDetails = new PatternRecipePackage(
//// machine.getRecipeMap(),
//// machine,
//// ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem())).getPatternForItem(pattern, world),
//// pattern);
//// this.sharedItemGetter = getter;
//// }
////
////// public IPatternSlot(ItemStack pattern, NBTTagCompound nbt, World world, SharedItemGetter getter,
////// GT_Recipe recipe) {
////// this.pattern = pattern;
////// this.recipe = recipe;
////// this.patternDetails = ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem()))
////// .getPatternForItem(pattern, world);
////// this.sharedItemGetter = getter;
//////
////// }
////
//// public boolean hasChanged(ItemStack newPattern, World world) {
//// return newPattern == null
//// || (!ItemStack.areItemStacksEqual(pattern, newPattern) && !this.patternDetails.equals(
//// ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem()))
//// .getPatternForItem(pattern, world)));
//// }
////
//// public ICraftingPatternDetails getPatternDetails() {
//// return patternDetails;
//// }
////
//// public void refund(AENetworkProxy proxy, BaseActionSource src) throws GridAccessException {
//// IMEMonitor<IAEItemStack> sg = proxy.getStorage()
//// .getItemInventory();
//// for (ItemStack itemStack : recipe.mInputs) {
//// if (itemStack == null || itemStack.stackSize == 0) continue;
//// itemStack.stackSize *= recipeCount;
//// IAEItemStack rest = Platform.poweredInsert(
//// proxy.getEnergy(),
//// sg,
//// AEApi.instance()
//// .storage()
//// .createItemStack(itemStack),
//// src);
//// itemStack.stackSize = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
//// }
//// IMEMonitor<IAEFluidStack> fsg = proxy.getStorage()
//// .getFluidInventory();
//// for (FluidStack fluidStack : recipe.mFluidInputs) {
//// if (fluidStack == null || fluidStack.amount == 0) continue;
//// fluidStack.amount *= recipeCount;
//// IAEFluidStack rest = Platform.poweredInsert(
//// proxy.getEnergy(),
//// fsg,
//// AEApi.instance()
//// .storage()
//// .createFluidStack(fluidStack),
//// src);
//// fluidStack.amount = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
//// }
//// recipeCount = 0;
//// }
////
//// public boolean insertItemsAndFluids(InventoryCrafting inventoryCrafting) {
//// if (firstItem == null && firstFluid == null) {
//// ItemStack itemStack = inventoryCrafting.getStackInSlot(0);
//// if (itemStack.getItem() instanceof ItemFluidPacket) {
//// var fluidStack = ItemFluidPacket.getFluidStack(itemStack);
//// for (var fluid : recipe.mFluidInputs) {
//// if (fluid != null && fluid.isFluidEqual(fluidStack)) {
//// firstFluid = fluid;
//// break;
//// }
//// }
//// } else {
//// for (var item : recipe.mInputs) {
//// if (item != null && item.isItemEqual(itemStack)) {
//// firstItem = item;
//// break;
//// }
//// }
//// }
//// }
//// int processCount = 0;
//// if (firstItem == null && firstFluid != null) {
//// var slotItem = ItemFluidPacket.getFluidStack(inventoryCrafting.getStackInSlot(0));
//// if (slotItem != null) {
//// processCount = slotItem.amount / firstFluid.amount;
//// }
//// } else if (firstItem != null && firstFluid == null) {
//// processCount = inventoryCrafting.getStackInSlot(0).stackSize / firstItem.stackSize;
//// } else {
//// LOG.info("unexpected recipe get while processing crafting");
//// return false;
//// }
//// if (Integer.MAX_VALUE - recipeCount < processCount) {
//// return false;
//// }
//// recipeCount += processCount;
//// return processCount == 0;
//// }
////
//// public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
//// nbt.setTag("pattern", pattern.writeToNBT(new NBTTagCompound()));
////
//// return nbt;
//// }
//// }
