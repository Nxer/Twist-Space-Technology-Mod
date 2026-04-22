package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import goodgenerator.util.CrackRecipeAdder;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

/**
 * 可使用的激发流体/冷却液与大型硅岩反应堆一致
 * 最大并行可在配置文件中调整
 * 持续运行降低激发流体/冷却液消耗（机制类似于超维度等离子锻炉）。
 */
public class TST_MegaNqReactor extends TT_MultiMachineBase_EM implements IConstructable, ISurvivalConstructable {

    // region Constants & tier caches

    protected static final int LIQUID_AIR_PER_SECOND = 2400;
    protected static final int TICKS_PER_SECOND = 20;
    protected static final int[] COOLANT_EFFICIENCY = { 500, 275, 150, 105 };
    protected static final int[] EXCITED_LIQUID_COEFF = { 64, 16, 4, 3, 2 };

    protected static final long TICKS_TO_MAX_DISCOUNT = 24L * 60 * 60 * TICKS_PER_SECOND;
    protected static final long DECAY_PER_IDLE_TICK = 20L;
    protected static final int MAX_DISCOUNT_PERCENT = 50;
    protected static final int DEFAULT_COOLANT_EFFICIENCY = 100;

    protected static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();
    protected static final Set<String> MISSING_CLASSES = ConcurrentHashMap.newKeySet();
    protected static final Map<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();
    protected static final Set<String> MISSING_FIELDS = ConcurrentHashMap.newKeySet();
    protected static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();
    protected static final Set<String> MISSING_METHODS = ConcurrentHashMap.newKeySet();

    protected static List<Pair<FluidStack, Integer>> excitedTiers;

    protected static List<Pair<FluidStack, Integer>> coolantTiers;

    // endregion

    // region Material & fluid resolution

    @Nullable
    protected static FluidStack fluidStackFromMaterial(@Nullable Materials mat, int amount) {
        if (mat == null) {
            return null;
        }
        FluidStack fs = mat.getFluid(amount);
        if (fs != null) {
            return fs.copy();
        }
        fs = mat.getMolten(amount);
        return fs == null ? null : fs.copy();
    }

    @Nullable
    protected static FluidStack optionalMaterialsFluid(String materialsFieldName, int amount) {
        FluidStack fs = optionalMaterialsField(Materials.class, materialsFieldName, amount);
        if (fs != null) {
            return fs;
        }
        fs = optionalMaterialsField("gregtech.api.enums.MaterialsUEVplus", materialsFieldName, amount);
        if (fs != null) {
            return fs;
        }
        return optionalMaterialsFluidRegistryFallback(materialsFieldName, amount);
    }

    @Nullable
    protected static FluidStack optionalMaterialsField(Class<?> holder, String fieldName, int amount) {
        try {
            Object raw = getStaticFieldValueCached(holder, fieldName);
            return fluidStackFromMaterialLike(raw, amount);
        } catch (ReflectiveOperationException | ClassCastException ignored) {
            return null;
        }
    }

    @Nullable
    protected static FluidStack optionalMaterialsField(String className, String fieldName, int amount) {
        Class<?> holder = getOptionalClassCached(className);
        if (holder == null) {
            return null;
        }
        return optionalMaterialsField(holder, fieldName, amount);
    }

    @Nullable
    protected static FluidStack optionalMaterialsFluidRegistryFallback(String fieldName, int amount) {
        String key = fieldName.toLowerCase(Locale.ROOT);
        String[] names = { "molten." + key, key };
        for (String name : names) {
            if (FluidRegistry.getFluid(name) != null) {
                return new FluidStack(FluidRegistry.getFluid(name), amount);
            }
        }
        return null;
    }

    @Nullable
    protected static FluidStack fluidStackFromMaterialLike(@Nullable Object mat, int amount) {
        if (mat == null) {
            return null;
        }
        if (mat instanceof Materials) {
            return fluidStackFromMaterial((Materials) mat, amount);
        }
        return fluidStackFromReflectiveMaterial(mat, amount);
    }

    @Nullable
    protected static FluidStack fluidStackFromReflectiveMaterial(Object mat, int amount) {
        long asLong = amount;
        String[] methodNames = { "getFluid", "getMolten" };
        Class<?>[] numericTypes = { long.class, int.class };
        Class<?> materialClass = mat.getClass();
        for (String methodName : methodNames) {
            for (Class<?> numType : numericTypes) {
                Method m = getMethodCached(materialClass, methodName, numType);
                if (m == null) {
                    continue;
                }
                try {
                    Object arg = numType == long.class ? asLong : amount;
                    Object r = m.invoke(mat, arg);
                    if (r instanceof FluidStack) {
                        FluidStack fs = (FluidStack) r;
                        if (fs.getFluid() != null) {
                            return fs.copy();
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    @Nullable
    protected static Class<?> getOptionalClassCached(String className) {
        Class<?> cached = CLASS_CACHE.get(className);
        if (cached != null) {
            return cached;
        }
        if (MISSING_CLASSES.contains(className)) {
            return null;
        }
        try {
            Class<?> resolved = Class.forName(className);
            CLASS_CACHE.put(className, resolved);
            return resolved;
        } catch (ClassNotFoundException | LinkageError ignored) {
            MISSING_CLASSES.add(className);
            return null;
        }
    }

    protected static Object getStaticFieldValueCached(Class<?> holder, String fieldName)
        throws ReflectiveOperationException {
        String key = holder.getName() + '#' + fieldName;
        Field field = FIELD_CACHE.get(key);
        if (field != null) {
            return field.get(null);
        }
        if (MISSING_FIELDS.contains(key)) {
            throw new NoSuchFieldException(fieldName);
        }
        try {
            Field resolved = holder.getField(fieldName);
            FIELD_CACHE.put(key, resolved);
            return resolved.get(null);
        } catch (NoSuchFieldException e) {
            MISSING_FIELDS.add(key);
            throw e;
        }
    }

    @Nullable
    protected static Method getMethodCached(Class<?> holder, String methodName, Class<?> argType) {
        String key = holder.getName() + '#' + methodName + '(' + argType.getName() + ')';
        Method method = METHOD_CACHE.get(key);
        if (method != null) {
            return method;
        }
        if (MISSING_METHODS.contains(key)) {
            return null;
        }
        try {
            Method resolved = holder.getMethod(methodName, argType);
            METHOD_CACHE.put(key, resolved);
            return resolved;
        } catch (NoSuchMethodException ignored) {
            MISSING_METHODS.add(key);
            return null;
        }
    }

    protected static void ensureTierLists() {
        if (excitedTiers != null) {
            return;
        }
        ArrayList<Pair<FluidStack, Integer>> excited = new ArrayList<>();
        FluidStack space = optionalMaterialsFluid("Space", 20);
        if (space != null) {
            excited.add(Pair.of(space, EXCITED_LIQUID_COEFF[0]));
        }
        excited.add(Pair.of(GGMaterial.atomicSeparationCatalyst.getMolten(20), EXCITED_LIQUID_COEFF[1]));
        excited.add(Pair.of(Materials.Naquadah.getMolten(20L), EXCITED_LIQUID_COEFF[2]));
        excited.add(Pair.of(Materials.Uranium235.getMolten(180L), EXCITED_LIQUID_COEFF[3]));
        excited.add(Pair.of(Materials.Caesium.getMolten(180L), EXCITED_LIQUID_COEFF[4]));

        ArrayList<Pair<FluidStack, Integer>> cool = new ArrayList<>();
        FluidStack timeFluid = optionalMaterialsFluid("Time", 20);
        if (timeFluid != null) {
            cool.add(Pair.of(timeFluid, COOLANT_EFFICIENCY[0]));
        }
        cool.add(Pair.of(new FluidStack(TFFluids.fluidCryotheum, 1_000), COOLANT_EFFICIENCY[1]));
        cool.add(Pair.of(Materials.SuperCoolant.getFluid(1_000), COOLANT_EFFICIENCY[2]));
        cool.add(Pair.of(GTModHandler.getIC2Coolant(1_000), COOLANT_EFFICIENCY[3]));

        excitedTiers = Collections.unmodifiableList(excited);
        coolantTiers = Collections.unmodifiableList(cool);
    }

    protected static ArrayList<FluidStack> mergeFluidStacks(List<FluidStack> raw) {
        ArrayList<FluidStack> merged = new ArrayList<>();
        for (FluidStack stack : raw) {
            if (stack == null || stack.amount <= 0) {
                continue;
            }
            boolean absorbed = false;
            for (FluidStack existing : merged) {
                if (GTUtility.areFluidsEqual(existing, stack, true)) {
                    existing.amount += stack.amount;
                    absorbed = true;
                    break;
                }
            }
            if (!absorbed) {
                merged.add(stack.copy());
            }
        }
        return merged;
    }

    // endregion

    // region Instance state

    protected long trueOutput = 0;
    protected int trueEff = 0;
    protected FluidStack lockedFluid = null;
    protected int times = 1;
    protected int basicOutput;
    protected int parallel = 1;
    protected long runTimeTicks = 0;
    protected boolean wasRunning = false;

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
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        ArrayList<FluidStack> mergedFluids = mergeFluidStacks(getStoredFluids());
        FluidStack[] fluidArray = mergedFluids.toArray(new FluidStack[0]);

        GTRecipe tRecipe = GoodGeneratorRecipeMaps.naquadahReactorFuels.findRecipeQuery()
            .fluids(fluidArray)
            .find();
        if (tRecipe == null) {
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        Pair<FluidStack, Integer> excitedInfo = getExcited(fluidArray, false);
        int coefficient = excitedInfo == null ? 1 : excitedInfo.getValue();
        FluidStack fuelInput = tRecipe.mFluidInputs[0];
        int maxParallel = Config.Parallel_MegaNqReactor;
        for (FluidStack fs : mergedFluids) {
            if (fs != null && fs.isFluidEqual(fuelInput)) {
                maxParallel = Math.min(maxParallel, fs.amount / fuelInput.amount);
            }
        }
        if (maxParallel < 1) {
            maxParallel = 1;
        }
        basicOutput = tRecipe.mSpecialValue;
        times = coefficient;
        parallel = maxParallel;
        lockedFluid = excitedInfo == null ? null
            : excitedInfo.getKey()
                .copy();
        mMaxProgresstime = tRecipe.mDuration;
        this.lEUt = 0;
        this.setPowerFlow(0);
        this.trueEff = 0;
        this.trueOutput = 0;
        return CheckRecipeResultRegistry.GENERATING;
    }

    protected void updateRunTimeDiscountState(boolean isRunning) {
        if (isRunning) {
            runTimeTicks = Math.min(runTimeTicks + 1, TICKS_TO_MAX_DISCOUNT);
        } else if (wasRunning) {
            runTimeTicks = Math.max(0, runTimeTicks - DECAY_PER_IDLE_TICK);
        }
        wasRunning = isRunning;
    }

    protected boolean tickGenerationSecond() {
        startRecipeProcessing();
        FluidStack[] input = getStoredFluids().toArray(new FluidStack[0]);
        int timeMultiplier = 1;
        int airRequired = LIQUID_AIR_PER_SECOND * parallel;
        if (airRequired != 0 && !consumeFuel(Materials.LiquidAir.getFluid(airRequired), input)) {
            clearPowerState();
            endRecipeProcessing();
            return false;
        }
        int eff = consumeCoolantDiscounted(input, parallel);
        if (lockedFluid != null) {
            int discountedAmount = getDiscountedAmount(lockedFluid.amount) * parallel;
            if (consumeFuel(CrackRecipeAdder.copyFluidWithAmount(this.lockedFluid, discountedAmount), input)) {
                timeMultiplier = times;
            }
        }
        long totalOutput = (long) basicOutput * eff * timeMultiplier / 100L * parallel;
        this.lEUt = totalOutput;
        this.setPowerFlow(totalOutput);
        this.mEUt = (int) Math.min(Integer.MAX_VALUE, totalOutput);
        this.trueEff = eff;
        this.trueOutput = totalOutput;
        endRecipeProcessing();
        return true;
    }

    protected void clearPowerState() {
        this.lEUt = 0;
        this.setPowerFlow(0);
        this.trueEff = 0;
        this.trueOutput = 0;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            boolean isRunning = mMaxProgresstime != 0;
            updateRunTimeDiscountState(isRunning);

            if (isRunning && mProgresstime % TICKS_PER_SECOND == 0) {
                if (!tickGenerationSecond()) {
                    return true;
                }
            }
            addAutoEnergy(this.lEUt);
        }
        return true;
    }

    public boolean consumeFuel(@Nullable FluidStack target, FluidStack[] input) {
        if (target == null || target.amount <= 0) {
            return false;
        }
        int need = target.amount;
        int available = 0;
        for (FluidStack inFluid : input) {
            if (inFluid != null && GTUtility.areFluidsEqual(inFluid, target, true)) {
                available += inFluid.amount;
            }
        }
        if (available < need) {
            return false;
        }
        int remaining = need;
        for (FluidStack inFluid : input) {
            if (inFluid != null && GTUtility.areFluidsEqual(inFluid, target, true)) {
                int take = Math.min(inFluid.amount, remaining);
                inFluid.amount -= take;
                remaining -= take;
                if (remaining <= 0) {
                    break;
                }
            }
        }
        return true;
    }

    public Pair<FluidStack, Integer> getExcited(FluidStack[] input, boolean isConsume) {
        ensureTierLists();
        for (Pair<FluidStack, Integer> tier : excitedTiers) {
            FluidStack template = tier.getKey();
            for (FluidStack inFluid : input) {
                if (inFluid != null && inFluid.amount >= template.amount
                    && GTUtility.areFluidsEqual(inFluid, template, true)) {
                    if (isConsume) {
                        inFluid.amount -= template.amount;
                    }
                    return tier;
                }
            }
        }
        return null;
    }

    protected int consumeCoolantDiscounted(FluidStack[] input, int count) {
        ensureTierLists();
        for (Pair<FluidStack, Integer> tier : coolantTiers) {
            FluidStack template = tier.getKey();
            int required = getDiscountedAmount(template.amount) * count;
            int available = 0;
            for (FluidStack inFluid : input) {
                if (inFluid != null && GTUtility.areFluidsEqual(inFluid, template, true)) {
                    available += inFluid.amount;
                }
            }
            if (available < required) {
                continue;
            }
            int remaining = required;
            for (FluidStack inFluid : input) {
                if (inFluid != null && GTUtility.areFluidsEqual(inFluid, template, true)) {
                    int take = Math.min(inFluid.amount, remaining);
                    inFluid.amount -= take;
                    remaining -= take;
                    if (remaining <= 0) {
                        return tier.getValue();
                    }
                }
            }
        }
        return DEFAULT_COOLANT_EFFICIENCY;
    }

    // endregion

    // region Power output (dynamo)

    protected static boolean canAcceptPower(MTEHatchDynamoMulti hatch, long outputPower) {
        return (long) hatch.maxEUOutput() * hatch.maxAmperesOut() >= outputPower;
    }

    protected static boolean canAcceptPower(MTEHatchDynamo hatch, long outputPower) {
        return (long) hatch.maxEUOutput() * hatch.maxAmperesOut() >= outputPower;
    }

    protected static void chargeDynamoMulti(MTEHatchDynamoMulti hatch, long outputPower) {
        long cap = Math.min(
            hatch.maxEUStore(),
            hatch.getBaseMetaTileEntity()
                .getStoredEU() + outputPower);
        hatch.setEUVar(cap);
    }

    protected static void chargeDynamo(MTEHatchDynamo hatch, long outputPower) {
        long cap = Math.min(
            hatch.maxEUStore(),
            hatch.getBaseMetaTileEntity()
                .getStoredEU() + outputPower);
        hatch.setEUVar(cap);
    }

    public void addAutoEnergy(long outputPower) {
        if (!this.eDynamoMulti.isEmpty()) {
            MTEHatchDynamoMulti tHatch = this.eDynamoMulti.get(0);
            if (canAcceptPower(tHatch, outputPower)) {
                chargeDynamoMulti(tHatch, outputPower);
            } else {
                stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
            }
        }
        if (!this.mDynamoHatches.isEmpty()) {
            MTEHatchDynamo tHatch = this.mDynamoHatches.get(0);
            if (canAcceptPower(tHatch, outputPower)) {
                chargeDynamo(tHatch, outputPower);
            } else {
                stopMachine(ShutDownReasonRegistry.INSUFFICIENT_DYNAMO);
            }
        }
    }

    // endregion

    // region Structure

    protected static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_MNG";
    protected final int hOffset = 15, vOffset = 25, dOffset = 1;
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
},{
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
    public IStructureDefinition<TST_MegaNqReactor> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaNqReactor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE_MAIN))
                .addElement(
                    'D',
                    ofChain(
                        buildHatchAdder(TST_MegaNqReactor.class)
                            .atLeast(
                                TTMultiblockBase.HatchElement.DynamoMulti.or(Dynamo),
                                TTMultiblockBase.HatchElement.EnergyMulti.or(gregtech.api.enums.HatchElement.Energy),
                                InputHatch,
                                OutputHatch,
                                gregtech.api.enums.HatchElement.Maintenance)
                            .casingIndex(
                                ((gregtech.common.blocks.BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3))
                            .dot(1)
                            .build(),
                        ofBlock(GregTechAPI.sBlockCasings8, 3)))
                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(Loaders.MAR_Casing, 0))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings1, 14))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings9, 14))
                .addElement('F', ofBlock(com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing02, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
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
        final ITexture baseCasingTexture = gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId(
            ((gregtech.common.blocks.BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3));
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
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public boolean isPowerPassButtonEnabled() {
        return false;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
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
                    .setStringSupplier(
                        () -> tr("GUI.MegaNqReactor.CurrentOutput") + GTUtility.formatNumbers(trueOutput) + " EU/t")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> trueOutput, val -> trueOutput = val));
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
