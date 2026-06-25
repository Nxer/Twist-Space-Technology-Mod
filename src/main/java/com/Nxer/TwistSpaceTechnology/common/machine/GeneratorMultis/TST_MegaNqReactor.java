package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.client.effect.MegaNqReactorParticleBatch;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TST_GeneratorBase;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import goodgenerator.util.CrackRecipeAdder;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.render.IMTERenderer;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * 可使用的激发流体/冷却液与大型硅岩反应堆一致
 * 最大并行可在配置文件中调整。
 * 消耗减免按配方开始时刻的累计运行时间计算。
 */
public class TST_MegaNqReactor extends TST_GeneratorBase<TST_MegaNqReactor>
    implements IConstructable, ISurvivalConstructable, IMTERenderer {

    // region Constants & tier caches

    protected static final int LIQUID_AIR_PER_SECOND = 2400;
    protected static final int TICKS_PER_SECOND = 20;
    protected static final int[] COOLANT_EFFICIENCY = { 500, 275, 150, 105 };
    protected static final int[] EXCITED_LIQUID_COEFF = { 64, 16, 4, 3, 2 };

    protected static final long TICKS_TO_MAX_DISCOUNT = 24L * 60 * 60 * TICKS_PER_SECOND;
    protected static final long DECAY_PER_IDLE_TICK = 20L;
    protected static final int MAX_DISCOUNT_PERCENT = 50;
    protected static final int DEFAULT_COOLANT_EFFICIENCY = 100;

    protected static final List<Pair<FluidStack, Integer>> EXCITED_TIERS = Arrays.asList(
        Pair.of(Materials.Space.getMolten(20L), EXCITED_LIQUID_COEFF[0]),
        Pair.of(GGMaterial.atomicSeparationCatalyst.getMolten(20), EXCITED_LIQUID_COEFF[1]),
        Pair.of(Materials.Naquadah.getMolten(20L), EXCITED_LIQUID_COEFF[2]),
        Pair.of(Materials.Uranium235.getMolten(180L), EXCITED_LIQUID_COEFF[3]),
        Pair.of(Materials.Caesium.getMolten(180L), EXCITED_LIQUID_COEFF[4]));

    protected static final List<Pair<FluidStack, Integer>> COOLANT_TIERS = Arrays.asList(
        Pair.of(Materials.Time.getMolten(20L), COOLANT_EFFICIENCY[0]),
        Pair.of(new FluidStack(TFFluids.fluidCryotheum, 1_000), COOLANT_EFFICIENCY[1]),
        Pair.of(Materials.SuperCoolant.getFluid(1_000), COOLANT_EFFICIENCY[2]),
        Pair.of(GTModHandler.getIC2Coolant(1_000), COOLANT_EFFICIENCY[3]));

    // endregion

    // region Fluid accounting

    protected static FluidInventoryView createFluidInventoryView(List<FluidStack> storedFluids) {
        FluidInventoryView view = new FluidInventoryView();
        for (FluidStack storedFluid : storedFluids) {
            view.add(storedFluid);
        }
        return view;
    }

    @Nullable
    protected static FluidStack copyFluid(@Nullable FluidStack fluidStack, int amount) {
        if (fluidStack == null || fluidStack.getFluid() == null || amount <= 0) {
            return null;
        }
        return CrackRecipeAdder.copyFluidWithAmount(fluidStack, amount);
    }

    protected static FluidStack[] copyFluidOutputs(@Nullable FluidStack[] outputs, int multiplier) {
        if (outputs == null || outputs.length == 0 || multiplier <= 0) {
            return null;
        }

        int totalSize = 0;
        for (FluidStack output : outputs) {
            if (output == null) {
                continue;
            }

            long totalAmount = (long) output.amount * multiplier;

            totalSize += (int) ((totalAmount + Integer.MAX_VALUE - 1L) / Integer.MAX_VALUE);
        }

        FluidStack[] result = new FluidStack[totalSize];

        int index = 0;

        for (FluidStack output : outputs) {
            if (output == null) {
                continue;
            }

            long totalAmount = (long) output.amount * multiplier;

            while (totalAmount > 0) {
                int splitAmount = (int) Math.min(Integer.MAX_VALUE, totalAmount);

                result[index++] = copyFluid(output, splitAmount);

                totalAmount -= splitAmount;
            }
        }

        return result;
    }

    protected boolean consumeFluidStacks(@Nullable FluidStack template, int requiredAmount) {
        return consumeFluidStacks(getStoredFluids(), template, requiredAmount);
    }

    protected boolean consumeFluidStacks(List<FluidStack> storedFluids, @Nullable FluidStack template,
        int requiredAmount) {
        FluidStack requiredFluid = copyFluid(template, requiredAmount);
        if (requiredFluid == null) {
            return false;
        }
        ArrayList<FluidStack> requirements = new ArrayList<>(1);
        requirements.add(requiredFluid);
        return consumeFluidRequirements(storedFluids, requirements);
    }

    protected boolean consumeFluidRequirements(List<FluidStack> storedFluids, List<FluidStack> requirements) {
        List<FluidStack> mergedRequirements = mergeFluidRequirements(requirements);
        if (mergedRequirements.isEmpty()) {
            return true;
        }

        FluidInventoryView fluidView = createFluidInventoryView(storedFluids);
        for (FluidStack requirement : mergedRequirements) {
            if (!fluidView.consume(requirement, requirement.amount)) {
                return false;
            }
        }
        for (FluidStack requirement : mergedRequirements) {
            consumeFluidDirect(storedFluids, requirement);
        }
        updateInputHatchFluids();
        return true;
    }

    protected static List<FluidStack> mergeFluidRequirements(List<FluidStack> requirements) {
        Object2ObjectOpenHashMap<FluidId, FluidStack> mergedById = new Object2ObjectOpenHashMap<>();
        ArrayList<FluidStack> mergedRequirements = new ArrayList<>(requirements.size());
        for (FluidStack requirement : requirements) {
            if (requirement == null || requirement.amount <= 0 || requirement.getFluid() == null) {
                continue;
            }
            FluidId id = FluidId.create(requirement);
            FluidStack mergedRequirement = mergedById.get(id);
            if (mergedRequirement == null) {
                mergedRequirement = copyFluid(requirement, requirement.amount);
                if (mergedRequirement == null) {
                    continue;
                }
                mergedById.put(id, mergedRequirement);
                mergedRequirements.add(mergedRequirement);
            } else {
                mergedRequirement.amount += requirement.amount;
            }
        }
        return mergedRequirements;
    }

    protected boolean consumeFluidDirect(List<FluidStack> storedFluids, FluidStack requirement) {
        int remainingAmount = requirement.amount;
        for (FluidStack storedFluid : storedFluids) {
            if (!isSameFluid(storedFluid, requirement)) {
                continue;
            }
            int drainedAmount = Math.min(storedFluid.amount, remainingAmount);
            storedFluid.amount -= drainedAmount;
            remainingAmount -= drainedAmount;
            if (remainingAmount <= 0) {
                return true;
            }
        }
        return false;
    }

    protected void updateInputHatchFluids() {
        for (MTEHatchInput inputHatch : GTUtility.validMTEList(mInputHatches)) {
            inputHatch.updateSlots();
        }
    }

    protected static boolean isSameFluid(@Nullable FluidStack left, @Nullable FluidStack right) {
        return left != null && GTUtility.areFluidsEqual(left, right, true);
    }

    protected static int recipeSecondsFromDurationTicks(int durationTicks) {
        if (durationTicks <= 0) {
            return 0;
        }
        return (durationTicks + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND;
    }

    protected static int safeMulFluidAmount(long a, long b) {
        long p = a * b;
        if (p <= 0L || p > (long) Integer.MAX_VALUE) {
            return -1;
        }
        return (int) p;
    }

    // endregion

    // region Instance state

    protected FluidStack lockedFluid = null;
    protected int times = 1;
    protected int basicOutput;
    protected int parallel = 1;
    protected long runTimeTicks = 0;
    protected int cachedCoolantEfficiency = DEFAULT_COOLANT_EFFICIENCY;
    protected int cachedTimeMultiplier = 1;

    protected double coreFxX;
    protected double coreFxY;
    protected double coreFxZ;
    protected long lastCoreParticleBatchTick = -1L;
    protected boolean isRenderActive = false;

    public static class FluidInventoryView {

        private final Object2IntOpenHashMap<FluidId> amounts = new Object2IntOpenHashMap<>();
        private final Object2ObjectOpenHashMap<FluidId, FluidStack> mergedById = new Object2ObjectOpenHashMap<>();
        private final List<FluidStack> mergedFluids = new ArrayList<>();

        public FluidInventoryView() {
            amounts.defaultReturnValue(0);
        }

        public void add(@Nullable FluidStack stack) {
            if (stack == null || stack.amount <= 0 || stack.getFluid() == null) {
                return;
            }
            FluidId id = FluidId.create(stack);
            int previousAmount = amounts.addTo(id, stack.amount);
            FluidStack mergedFluid = mergedById.get(id);
            if (mergedFluid == null) {
                mergedFluid = copyFluid(stack, stack.amount);
                mergedById.put(id, mergedFluid);
                mergedFluids.add(mergedFluid);
            } else {
                mergedFluid.amount = previousAmount + stack.amount;
            }
        }

        public FluidStack[] toFluidArray() {
            return mergedFluids.toArray(new FluidStack[0]);
        }

        public List<FluidStack> getMergedFluids() {
            return mergedFluids;
        }

        public int getAmount(@Nullable FluidStack template) {
            if (template == null || template.getFluid() == null) {
                return 0;
            }
            return amounts.getInt(FluidId.create(template));
        }

        public boolean hasAtLeast(@Nullable FluidStack template, int requiredAmount) {
            return requiredAmount <= 0 || getAmount(template) >= requiredAmount;
        }

        public boolean consume(@Nullable FluidStack template, int requiredAmount) {
            if (template == null || template.getFluid() == null || requiredAmount <= 0) {
                return false;
            }
            FluidId id = FluidId.create(template);
            int currentAmount = amounts.getInt(id);
            if (currentAmount < requiredAmount) {
                return false;
            }
            int remainingAmount = currentAmount - requiredAmount;
            if (remainingAmount > 0) {
                amounts.put(id, remainingAmount);
            } else {
                amounts.removeInt(id);
            }
            updateMergedAmount(id, remainingAmount);
            return true;
        }

        public void updateMergedAmount(FluidId id, int newAmount) {
            FluidStack mergedFluid = mergedById.get(id);
            if (mergedFluid == null) {
                return;
            }
            if (newAmount > 0) {
                mergedFluid.amount = newAmount;
                return;
            }
            mergedById.remove(id);
            for (int i = 0; i < mergedFluids.size(); i++) {
                if (mergedFluids.get(i) == mergedFluid) {
                    mergedFluids.remove(i);
                    return;
                }
            }
        }
    }

    // endregion

    // region Class Constructor

    public TST_MegaNqReactor(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public TST_MegaNqReactor(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaNqReactor(this.mName);
    }

    // endregion

    // region Persistence & consumption discount

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.times = aNBT.getInteger("mTimes");
        this.basicOutput = aNBT.getInteger("mbasicOutput");
        this.parallel = aNBT.getInteger("mParallel");
        this.runTimeTicks = aNBT.getLong("mRunTimeTicks");
        String lockedName = aNBT.getString("mLockedFluidName");
        if (FluidRegistry.getFluid(lockedName) != null) {
            this.lockedFluid = new FluidStack(
                FluidRegistry.getFluid(lockedName),
                aNBT.getInteger("mLockedFluidAmount"));
        } else {
            this.lockedFluid = null;
        }
        this.cachedCoolantEfficiency = aNBT.hasKey("mCachedCoolantEff") ? aNBT.getInteger("mCachedCoolantEff")
            : DEFAULT_COOLANT_EFFICIENCY;
        this.cachedTimeMultiplier = aNBT.hasKey("mCachedTimeMult") ? aNBT.getInteger("mCachedTimeMult") : 1;
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mTimes", this.times);
        aNBT.setInteger("mbasicOutput", this.basicOutput);
        aNBT.setInteger("mParallel", this.parallel);
        aNBT.setLong("mRunTimeTicks", this.runTimeTicks);
        if (lockedFluid != null) {
            aNBT.setString("mLockedFluidName", FluidRegistry.getFluidName(this.lockedFluid));
            aNBT.setInteger("mLockedFluidAmount", this.lockedFluid.amount);
        } else {
            aNBT.removeTag("mLockedFluidName");
            aNBT.removeTag("mLockedFluidAmount");
        }
        aNBT.setInteger("mCachedCoolantEff", this.cachedCoolantEfficiency);
        aNBT.setInteger("mCachedTimeMult", this.cachedTimeMultiplier);
        super.saveNBTData(aNBT);
    }

    public int getConsumptionDiscount() {
        if (runTimeTicks >= TICKS_TO_MAX_DISCOUNT) {
            return MAX_DISCOUNT_PERCENT;
        }
        return (int) (runTimeTicks * MAX_DISCOUNT_PERCENT / TICKS_TO_MAX_DISCOUNT);
    }

    public int getDiscountedAmount(int originalAmount) {
        int discount = getConsumptionDiscount();
        if (discount <= 0) {
            return originalAmount;
        }
        return Math.max(1, originalAmount * (100 - discount) / 100);
    }

    // endregion

    // region Processing logic

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.naquadahReactorFuels;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        FluidInventoryView fluidView = createFluidInventoryView(getStoredFluids());
        FluidStack[] fluidArray = fluidView.toFluidArray();

        GTRecipe tRecipe = GoodGeneratorRecipeMaps.naquadahReactorFuels.findRecipeQuery()
            .fluids(fluidArray)
            .find();
        if (tRecipe == null) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        Pair<FluidStack, Integer> excitedInfo = getExcited(fluidView);
        int coefficient = excitedInfo == null ? 1 : excitedInfo.getValue();
        FluidStack fuelInput = tRecipe.mFluidInputs[0];
        int perParallelFuel = fuelInput.amount;
        if (perParallelFuel <= 0) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        int fuelAmount = fluidView.getAmount(fuelInput);
        int intLimitCap = Integer.MAX_VALUE / perParallelFuel;
        int maxParallel = Math.min(Math.min(Config.Parallel_MegaNqReactor, fuelAmount / perParallelFuel), intLimitCap);
        if (maxParallel <= 0) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        int consumedFuelAmount = perParallelFuel * maxParallel;
        if (!fluidView.hasAtLeast(fuelInput, consumedFuelAmount)) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        int recipeTicks = tRecipe.mDuration;
        int recipeSeconds = recipeSecondsFromDurationTicks(recipeTicks);
        int efficiencyForRun = DEFAULT_COOLANT_EFFICIENCY;
        int timeMultForRun = 1;

        ArrayList<FluidStack> requirements = new ArrayList<>(6);
        FluidStack fuelReq = copyFluid(fuelInput, consumedFuelAmount);
        if (fuelReq == null) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        requirements.add(fuelReq);

        if (recipeSeconds > 0 && maxParallel > 0) {
            long airTotalLong = (long) LIQUID_AIR_PER_SECOND * maxParallel * recipeSeconds;
            if (airTotalLong > Integer.MAX_VALUE) {
                return CheckRecipeResultRegistry.NO_FUEL_FOUND;
            }
            int airAmount = (int) airTotalLong;
            if (airAmount > 0) {
                FluidStack liquidAir = Materials.LiquidAir.getFluid(airAmount);
                if (!fluidView.hasAtLeast(liquidAir, airAmount)) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                requirements.add(liquidAir);
            }
        }

        Pair<FluidStack, Integer> coolantSelection = selectCoolant(fluidView, maxParallel);
        if (coolantSelection != null) {
            efficiencyForRun = coolantSelection.getValue();
            if (recipeSeconds > 0) {
                FluidStack oneSecondCoolant = coolantSelection.getKey();
                int totalCoolantAmt = safeMulFluidAmount((long) oneSecondCoolant.amount, recipeSeconds);
                if (totalCoolantAmt < 0) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                FluidStack fullCoolant = copyFluid(oneSecondCoolant, totalCoolantAmt);
                if (fullCoolant == null) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                if (!fluidView.hasAtLeast(fullCoolant, totalCoolantAmt)) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                requirements.add(fullCoolant);
            }
        }

        FluidStack lockedForRun = null;
        if (excitedInfo != null) {
            lockedForRun = excitedInfo.getKey()
                .copy();
            timeMultForRun = coefficient;
            if (recipeSeconds > 0) {
                long perSecondExcited = (long) getDiscountedAmount(lockedForRun.amount) * maxParallel;
                int totalExcitedAmt = safeMulFluidAmount(perSecondExcited, recipeSeconds);
                if (totalExcitedAmt < 0) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                FluidStack excitedReq = copyFluid(lockedForRun, totalExcitedAmt);
                if (excitedReq == null) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                if (!fluidView.hasAtLeast(excitedReq, totalExcitedAmt)) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                requirements.add(excitedReq);
            }
        }

        startRecipeProcessing();
        if (!consumeFluidRequirements(getStoredFluids(), requirements)) {
            endRecipeProcessing();
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        endRecipeProcessing();

        basicOutput = tRecipe.mSpecialValue;
        times = coefficient;
        parallel = maxParallel;
        lockedFluid = lockedForRun;
        mMaxProgresstime = recipeTicks;
        mEfficiencyIncrease = 10000;
        mOutputFluids = copyFluidOutputs(tRecipe.mFluidOutputs, maxParallel);
        cachedCoolantEfficiency = efficiencyForRun;
        cachedTimeMultiplier = timeMultForRun;
        lEUt = (long) basicOutput * cachedCoolantEfficiency * cachedTimeMultiplier / 100L * maxParallel;
        return CheckRecipeResultRegistry.GENERATING;
    }

    protected void updateRunTimeDiscountState(boolean isRunning) {
        if (isRunning) {
            runTimeTicks = Math.min(runTimeTicks + 1, TICKS_TO_MAX_DISCOUNT);
        } else {
            runTimeTicks = Math.max(0, runTimeTicks - DECAY_PER_IDLE_TICK);
        }
    }

    @Override
    public void onValueUpdate(byte aValue) {
        isRenderActive = (aValue & 0x01) != 0;
    }

    @Override
    public byte getUpdateData() {
        byte data = 0;
        if (getBaseMetaTileEntity().isActive()) {
            data |= 0x01;
        }
        return data;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        boolean isRunning = mMaxProgresstime != 0;
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            updateRunTimeDiscountState(isRunning);
        }
        return super.onRunningTick(stack);
    }

    @Override
    public boolean addEnergyOutput(long EU) {
        boolean result = super.addEnergyOutput(EU);
        if (!result) {
            stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
        }
        return result;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        lEUt = 0;
        cachedCoolantEfficiency = DEFAULT_COOLANT_EFFICIENCY;
        cachedTimeMultiplier = 1;
        super.stopMachine(reason);
    }

    protected void updateCoreFxCenter() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        ExtendedFacing facing = getExtendedFacing();
        ForgeDirection back = facing.getRelativeBackInWorld();
        ForgeDirection up = facing.getRelativeUpInWorld();
        coreFxX = base.getXCoord() + 0.5D
            + CORE_CENTER_OFFSET_X * back.offsetX
            + CORE_CENTER_OFFSET_Y * up.offsetX
            + CORE_CENTER_OFFSET_Z * back.offsetX;
        coreFxY = base.getYCoord() + 0.5D
            + CORE_CENTER_OFFSET_X * back.offsetY
            + CORE_CENTER_OFFSET_Y * up.offsetY
            + CORE_CENTER_OFFSET_Z * back.offsetY;
        coreFxZ = base.getZCoord() + 0.5D
            + CORE_CENTER_OFFSET_X * back.offsetZ
            + CORE_CENTER_OFFSET_Y * up.offsetZ
            + CORE_CENTER_OFFSET_Z * back.offsetZ;
    }

    /**
     * 运行时在客户端按 {@link #CORE_PARTICLE_SPAWN_INTERVAL_TICKS} 追加粒子批次；
     * 间隔约为单粒子寿命的三分之一，通常可见 2～3 批叠加；新批次不强制结束上一批。
     */
    @SideOnly(Side.CLIENT)
    protected void scheduleCoreParticleBatch() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        if (world == null || !world.isRemote) {
            return;
        }
        if (world.getTotalWorldTime() % CORE_PARTICLE_SPAWN_INTERVAL_TICKS != 0L) {
            return;
        }
        if (lastCoreParticleBatchTick == world.getTotalWorldTime()) {
            return;
        }
        lastCoreParticleBatchTick = world.getTotalWorldTime();
        updateCoreFxCenter();
        MegaNqReactorParticleBatch.spawn(world, coreFxX, coreFxY, coreFxZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        if (!isRenderActive) {
            return;
        }
        scheduleCoreParticleBatch();
    }

    public Pair<FluidStack, Integer> getExcited(FluidInventoryView fluidView) {
        for (Pair<FluidStack, Integer> tier : EXCITED_TIERS) {
            FluidStack template = tier.getKey();
            if (fluidView.hasAtLeast(template, template.amount)) {
                return tier;
            }
        }
        return null;
    }

    @Nullable
    protected Pair<FluidStack, Integer> selectCoolant(FluidInventoryView fluidView, int count) {
        for (Pair<FluidStack, Integer> tier : COOLANT_TIERS) {
            FluidStack template = tier.getKey();
            int requiredAmount = getDiscountedAmount(template.amount) * count;
            FluidStack requiredFluid = copyFluid(template, requiredAmount);
            if (requiredFluid == null || !fluidView.hasAtLeast(requiredFluid, requiredAmount)) {
                continue;
            }
            return Pair.of(requiredFluid, tier.getValue());
        }
        return null;
    }

    // endregion

    // region Structure

    protected static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_MNG";
    protected final int hOffset = 15, vOffset = 25, dOffset = 1;

    protected static final double CORE_CENTER_OFFSET_X = 15.0D;
    protected static final double CORE_CENTER_OFFSET_Y = 12.0D;
    protected static final double CORE_CENTER_OFFSET_Z = 0.0D;
    /**
     * 与 {@code MegaNqReactorParticle.LIFETIME_TICKS}（200）配合：约每 1/3 寿命一批，平均近 3 层叠加。
     */
    protected static final int CORE_PARTICLE_SPAWN_INTERVAL_TICKS = 67;
    protected static IStructureDefinition<TST_MegaNqReactor> STRUCTURE_DEFINITION = null;

    // spotless:off
    // structure by Tuna
    protected static final String[][] SHAPE_MAIN = new String[][]{{
        "                               ",
        "    FFFFFFF         FFFFFFF    ",
        "   FDDDDDDDF       FDDDDDDDF   ",
        "  FDDDDDDDDDFF   FFDDDDDDDDDF  ",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        " FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        " FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        "  FDDDDDDDDDFF   FFDDDDDDDDDF  ",
        "   FDDDDDDDF       FDDDDDDDF   ",
        "    FFFFFFF         FFFFFFF    ",
    }, {
        "                               ",
        "                               ",
        "  FDBBBBBBB         BBBBBBBDF  ",
        " FDDDDDDDDDB       BDDDDDDDDDF ",
        " DDDDDDDDDDDBB   BBDDDDDDDDDDD ",
        " BDDDDDDDDDDDDBBBDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        "  BDDDDDDDDDDDDDDDDDDDDDDDDDB  ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "    BDDDDDDDDDDDDDDDDDDDDDB    ",
        "    BDDDDDDDDDDDDDDDDDDDDDB    ",
        "    BDDDDDDDDDDDDDDDDDDDDDB    ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "  BDDDDDDDDDDDDDDDDDDDDDDDDDB  ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDBBBDDDDDDDDDDDDB ",
        " DDDDDDDDDDDBB   BBDDDDDDDDDDD ",
        " FDDDDDDDDDB       BDDDDDDDDDF ",
        "  FDBBBBBBB         BBBBBBBDF  ",
        "                               ",
    }, {
        "                               ",
        "    FFFFFFF         FFFFFFF    ",
        " FDFDDDDDDDF       FDDDDDDDFDF ",
        " DDDDDDDDDDDFF   FFDDDDDDDDDDD ",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        "FDDDFF                   FFDDDF",
        "FDDDFED                 DEFDDDF",
        "FDDD DED               DED DDDF",
        "FDDD  DED             DED  DDDF",
        "FDDD   DED           DED   DDDF",
        "FDDD    DED         DED    DDDF",
        "FDDD     DED       DED     DDDF",
        " FDD      DED     DED      DDF ",
        "  FD       DED   DED       DF  ",
        "  FD        DED DED        DF  ",
        "   F         DDFDD         F   ",
        "   F          FCF          F   ",
        "   F         DDFDD         F   ",
        "  FD        DED DED        DF  ",
        "  FD       DED   DED       DF  ",
        " FDD      DED     DED      DDF ",
        "FDDD     DED       DED     DDDF",
        "FDDD    DED         DED    DDDF",
        "FDDD   DED           DED   DDDF",
        "FDDD  DED             DED  DDDF",
        "FDDD DED               DED DDDF",
        "FDDDFED                 DEFDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        " DDDDDDDDDDDFF   FFDDDDDDDDDDD ",
        " FDFDDDDDDDF       FDDDDDDDFDF ",
        "    FFFFFFF         FFFFFFF    ",
    }, {
        "                               ",
        "                               ",
        " FF FF                   FF FF ",
        " FDFFFF                 FFFFDF ",
        "  FFFFF                 FFFFF  ",
        " FFFB                     BFFF ",
        " FFF A                   A FFF ",
        "  FF  A                 A  FF  ",
        "       A               A       ",
        "        A             A        ",
        "         A           A         ",
        "          A         A          ",
        "           A       A           ",
        "            A     A            ",
        "             A   A             ",
        "              DFD              ",
        "              FCF              ",
        "              DFD              ",
        "             A   A             ",
        "            A     A            ",
        "           A       A           ",
        "          A         A          ",
        "         A           A         ",
        "        A             A        ",
        "       A               A       ",
        "  FF  A                 A  FF  ",
        " FFF A                   A FFF ",
        " FFFFF                   FFFFF ",
        "  FFFFF                 FFFFF  ",
        " FDFFFF                 FFFFDF ",
        " FF FF                   FF FF ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        " FD                         DF ",
        " DDFFFF                 FFFFDD ",
        "  FEBB                   BBEF  ",
        "  FBB                     BBF  ",
        "  FB                       BF  ",
        "  F                         F  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "              DFD              ",
        "              FCF              ",
        "              DFD              ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  F                         F  ",
        "  FB                       BF  ",
        "  FBB                     BBF  ",
        "  FEBB                   BBEF  ",
        " DDFFFF                 FFFFDD ",
        " FD                         DF ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FFFF                   FFFF  ",
        "  FEB                     BEF  ",
        "  FB                       BF  ",
        "  F                         F  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "              DFD              ",
        "              FCF              ",
        "              DFD              ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  F                         F  ",
        "  FB                       BF  ",
        "  FEB                     BEF  ",
        "  FFFF                   FFFF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FFF                     FFF  ",
        "  FE                       EF  ",
        "  F                         F  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "              AAA              ",
        "             AAAAA             ",
        "             AAAAA             ",
        "             AAAAA             ",
        "              AAA              ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  F                         F  ",
        "  FE                       EF  ",
        "  FFF                     FFF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "             AAAAA             ",
        "            AAAAAAA            ",
        "           AAA   AAA           ",
        "           AA     AA           ",
        "           AA     AA           ",
        "           AA     AA           ",
        "           AAA   AAA           ",
        "            AAAAAAA            ",
        "             AAAAA             ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "             AAAAA             ",
        "            A     A            ",
        "           A       A           ",
        "          A         A          ",
        "          A         A          ",
        "          A         A          ",
        "          A         A          ",
        "          A         A          ",
        "           A       A           ",
        "            A     A            ",
        "             AAAAA             ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "             AAAAA             ",
        "            A     A            ",
        "           A       A           ",
        "          A         A          ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "          A         A          ",
        "           A       A           ",
        "            A     A            ",
        "             AAAAA             ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "            AAAAAAA            ",
        "           A       A           ",
        "          A         A          ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "          A         A          ",
        "           A       A           ",
        "            AAAAAAA            ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "              AAA              ",
        "           AAA   AAA           ",
        "          A         A          ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "        A             A        ",
        "        A             A        ",
        "        A             A        ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "          A         A          ",
        "           AAA   AAA           ",
        "              AAA              ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "            DDDDDDD            ",
        "          DD       DD          ",
        "         D   AAAAA   D         ",
        "        D  AA     AA  D        ",
        "       D  A         A  D       ",
        "       D A           A D       ",
        "      D  A           A  D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D  A           A  D      ",
        "       D A           A D       ",
        "       D  A         A  D       ",
        "        D  AA     AA  D        ",
        "         D   AAAAA   D         ",
        "          DD       DD          ",
        "            DDDDDDD            ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "            DDDDDDD            ",
        "          DDBBBBBBBDD          ",
        "         DBB       BBD         ",
        "        DB   AAAAA   BD        ",
        "       DB  AA     AA  BD       ",
        "      DB  A         A  BD      ",
        "      DB A           A BD      ",
        "     DB  A           A  BD     ",
        "     DB A             A BD     ",
        "     DB A             A BD     ",
        "     DB A             A BD     ",
        "     DB A             A BD     ",
        "     DB A             A BD     ",
        "     DB  A           A  BD     ",
        "      DB A           A BD      ",
        "      DB  A         A  BD      ",
        "       DB  AA     AA  BD       ",
        "        DB   AAAAA   BD        ",
        "         DBB       BBD         ",
        "          DDBBBBBBBDD          ",
        "            DDDDDDD            ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "            DDDDDDD            ",
        "          DD       DD          ",
        "         D   AAAAA   D         ",
        "        D  AA     AA  D        ",
        "       D  A         A  D       ",
        "       D A           A D       ",
        "      D  A           A  D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D A             A D      ",
        "      D  A           A  D      ",
        "       D A           A D       ",
        "       D  A         A  D       ",
        "        D  AA     AA  D        ",
        "         D   AAAAA   D         ",
        "          DD       DD          ",
        "            DDDDDDD            ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "              AAA              ",
        "           AAA   AAA           ",
        "          A         A          ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "        A             A        ",
        "        A             A        ",
        "        A             A        ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "          A         A          ",
        "           AAA   AAA           ",
        "              AAA              ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "            AAAAAAA            ",
        "           A       A           ",
        "          A         A          ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "          A         A          ",
        "           A       A           ",
        "            AAAAAAA            ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "             AAAAA             ",
        "            A     A            ",
        "           A       A           ",
        "          A         A          ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "         A           A         ",
        "          A         A          ",
        "           A       A           ",
        "            A     A            ",
        "             AAAAA             ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "             AAAAA             ",
        "            A     A            ",
        "           A       A           ",
        "          A         A          ",
        "          A         A          ",
        "          A         A          ",
        "          A         A          ",
        "          A         A          ",
        "           A       A           ",
        "            A     A            ",
        "             AAAAA             ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FF                       FF  ",
        "  FE                       EF  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "             AAAAA             ",
        "            AAAAAAA            ",
        "           AAA   AAA           ",
        "           AA     AA           ",
        "           AA     AA           ",
        "           AA     AA           ",
        "           AAA   AAA           ",
        "            AAAAAAA            ",
        "             AAAAA             ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  FE                       EF  ",
        "  FF                       FF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FFF                     FFF  ",
        "  FE                       EF  ",
        "  F                         F  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "              AAA              ",
        "             AAAAA             ",
        "             AAAAA             ",
        "             AAAAA             ",
        "              AAA              ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  F                         F  ",
        "  FE                       EF  ",
        "  FFF                     FFF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "                               ",
        "  FFFF                   FFFF  ",
        "  FEB                     BEF  ",
        "  FB                       BF  ",
        "  F                         F  ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "              DFD              ",
        "              FCF              ",
        "              DFD              ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "                               ",
        "  F                         F  ",
        "  FB                       BF  ",
        "  FEB                     BEF  ",
        "  FFFF                   FFFF  ",
        "                               ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "  F                         F  ",
        " FDFFFF                 FFFFDF ",
        "  FEBB                   BBEF  ",
        "  FBB                     BBF  ",
        "  FB A                   A BF  ",
        "  F   A                 A   F  ",
        "       A               A       ",
        "        A             A        ",
        "         A           A         ",
        "          A         A          ",
        "           A       A           ",
        "            A     A            ",
        "             A   A             ",
        "              DFD              ",
        "              FCF              ",
        "              DFD              ",
        "             A   A             ",
        "            A     A            ",
        "           A       A           ",
        "          A         A          ",
        "         A           A         ",
        "        A             A        ",
        "       A               A       ",
        "  F   A                 A   F  ",
        "  FB A                   A BF  ",
        "  FBB                     BBF  ",
        "  FEBB                   BBEF  ",
        " FDFFFF                 FFFFDF ",
        "  F                         F  ",
        "                               ",
    }, {
        "                               ",
        "                               ",
        "  F FF                   FF F  ",
        " FDFFFF                 FFFFDF ",
        "  FFFFF                 FFFFF  ",
        " FFFFF                   FFFFF ",
        " FFFFED                 DEFFFF ",
        "  FF DED               DED FF  ",
        "      DED             DEDD     ",
        "       DED           DED       ",
        "        DED         DED        ",
        "         DED       DED         ",
        "          DED     DED          ",
        "           DED   DED           ",
        "            DED DED            ",
        "             DDFDD             ",
        "              FCF              ",
        "             DDFDD             ",
        "            DED DED            ",
        "           DED   DED           ",
        "          DED     DED          ",
        "         DED       DED         ",
        "        DED         DED        ",
        "       DED           DED       ",
        "      DED             DED      ",
        "  FF DED               DED FF  ",
        " FFFFED                 DEFFFF ",
        " FFFFF                   FFFFF ",
        "  FFFFF                 FFFFF  ",
        " FDFFFF                 FFFFDF ",
        "  F FF                   FF F  ",
        "                               ",
    }, {
        "                               ",
        "    FFFFFFF         FFFFFFF    ",
        "  FFDDDDDDDF       FDDDDDDDFF  ",
        " FDDDDDDDDDDFF   FFDDDDDDDDDDF ",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        " FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "   FDDDDDDDDDDDCDDDDDDDDDDDF   ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        " FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        " FDDDDDDDDDDFF   FFDDDDDDDDDDF ",
        "  FFDDDDDDDF       FDDDDDDDFF  ",
        "    FFFFFFF         FFFFFFF    ",
    }, {
        "                               ",
        "               ~               ",
        "  FDBBBBBBB    C    BBBBBBBDF  ",
        " FDDDDDDDDDB   C   BDDDDDDDDDF ",
        " DDDDDDDDDDDBB C BBDDDDDDDDDDD ",
        " BDDDDDDDDDDDDBBBDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        "  BDDDDDDDDDDDDDDDDDDDDDDDDDB  ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "    BDDDDDDDDDDDDDDDDDDDDDB    ",
        "    BDDDDDDDDDDDDDDDDDDDDDB    ",
        "    BDDDDDDDDDDDDDDDDDDDDDB    ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "   BDDDDDDDDDDDDDDDDDDDDDDDB   ",
        "  BDDDDDDDDDDDDDDDDDDDDDDDDDB  ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDDDDDDDDDDDDDDDDB ",
        " BDDDDDDDDDDDDBBBDDDDDDDDDDDDB ",
        "  DDDDDDDDDDBB   BBDDDDDDDDDDD ",
        " FDDDDDDDDDB       BDDDDDDDDDF ",
        "  FDBBBBBBB         BBBBBBBDF  ",
        "                               ",
    }, {
        "              DDD              ",
        "    FFFFFFF   DDD   FFFFFFF    ",
        "   FDDDDDDDF  DDD  FDDDDDDDF   ",
        "  FDDDDDDDDDFFDDDFFDDDDDDDDDF  ",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        " FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "   FDDDDDDDDDDDDDDDDDDDDDDDF   ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        "  FDDDDDDDDDDDDDDDDDDDDDDDDDF  ",
        " FDDDDDDDDDDDDDDDDDDDDDDDDDDDF ",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        "FDDDDDDDDDDDDDDDDDDDDDDDDDDDDDF",
        " FDDDDDDDDDDDDFFFDDDDDDDDDDDDF ",
        "  FDDDDDDDDDFF   FFDDDDDDDDDF  ",
        "   FDDDDDDDF       FDDDDDDDF   ",
        "    FFFFFFF         FFFFFFF    "
    }};
    // spotless:on

    @Override
    public IStructureDefinition<TST_MegaNqReactor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaNqReactor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE_MAIN))
                .addElement(
                    'D',
                    GTStructureUtility.buildHatchAdder(TST_MegaNqReactor.class)
                        .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 3))
                        .hint(1)
                        .atLeast(ExoticDynamo.or(Dynamo), ExoticEnergy.or(Energy), InputHatch, OutputHatch, Maintenance)
                        .buildAndChain(GregTechAPI.sBlockCasings8, 3))
                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(Loaders.MAR_Casing, 0))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings1, 14))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings9, 14))
                .addElement('F', ofBlock(TstBlocks.MetaBlockCasing02, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        boolean formed = checkPiece(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset, errors);
        if (formed) {
            updateCoreFxCenter();
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, hOffset, vOffset, dOffset);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) {
            return -1;
        }
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hOffset,
            vOffset,
            dOffset,
            elementBudget,
            env,
            false,
            true);
    }

    // endregion

    // region Texture

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        final ITexture baseCasingTexture = getCasingTextureForId(
            GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 3));
        if (side == facing) {
            if (aActive) {
                return new ITexture[] { baseCasingTexture, TextureFactory.builder()
                    .addIcon(NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
            return new ITexture[] { baseCasingTexture, TextureFactory.builder()
                .addIcon(NAQUADAH_REACTOR_SOLID_FRONT)
                .extFacing()
                .build() };
        }
        return new ITexture[] { baseCasingTexture };
    }

    // endregion

    // region Overrides (tooltip, UI, misc)

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(tr("Tooltip_MegaNqReactor_MachineType"))
            .addInfo(tr("Tooltip_MegaNqReactor_01"))
            .addInfo(tr("Tooltip_MegaNqReactor_02"))
            .addInfo(tr("Tooltip_MegaNqReactor_03"))
            .addInfo(tr("Tooltip_MegaNqReactor_04"))
            .addInfo(tr("Tooltip_MegaNqReactor_05"))
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        // #tr GUI.MegaNqReactor.RunningTime
        // # Running Time:
        // #zh_CN 持续运行时间:
        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(() -> tr("GUI.MegaNqReactor.RunningTime") + formatRunTime(runTimeTicks))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> runTimeTicks, val -> runTimeTicks = val))
            .widget(
                new TextWidget()
                    // #tr GUI.MegaNqReactor.ConsumptionDiscount
                    // # Consumption Discount:
                    // #zh_CN 消耗减免:
                    .setStringSupplier(
                        () -> tr("GUI.MegaNqReactor.ConsumptionDiscount") + getConsumptionDiscount() + "%")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(
                new TextWidget()
                    // #tr GUI.MegaNqReactor.CurrentOutput
                    // # Current Output:
                    // #zh_CN 当前输出:
                    .setStringSupplier(() -> tr("GUI.MegaNqReactor.CurrentOutput") + formatNumber(lEUt) + " EU/t")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> lEUt, val -> lEUt = val))
            .widget(new FakeSyncWidget.LongSyncer(() -> runTimeTicks, val -> runTimeTicks = val));
    }

    protected static String formatRunTime(long ticks) {
        long seconds = ticks / TICKS_PER_SECOND;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    // endregion
}
