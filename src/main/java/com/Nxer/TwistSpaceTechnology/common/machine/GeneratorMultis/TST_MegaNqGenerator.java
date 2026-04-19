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
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
 * 巨型硅岩反应堆：可使用的激发流体/冷却液与大型硅岩反应堆一致
 * 最大并行可在配置文件中调整
 * 调整持续运行降低激发流体/冷却液消耗（机制类似于超维度等离子锻炉）。
 */
public class TST_MegaNqGenerator extends TT_MultiMachineBase_EM implements IConstructable, ISurvivalConstructable {

    // region Constants & tier caches

    private static final int LIQUID_AIR_PER_SECOND = 2400;
    private static final int TICKS_PER_SECOND = 20;
    private static final int[] COOLANT_EFFICIENCY = { 500, 275, 150, 105 };
    private static final int[] EXCITED_LIQUID_COEFF = { 64, 16, 4, 3, 2 };

    private static final long TICKS_TO_MAX_DISCOUNT = 24L * 60 * 60 * TICKS_PER_SECOND;
    private static final long DECAY_PER_IDLE_TICK = 20L;
    private static final int MAX_DISCOUNT_PERCENT = 50;
    private static final int DEFAULT_COOLANT_EFFICIENCY = 100;

    private static List<Pair<FluidStack, Integer>> excitedTiers;

    private static List<Pair<FluidStack, Integer>> coolantTiers;

    // endregion

    // region Material & fluid resolution

    @Nullable
    private static FluidStack fluidStackFromMaterial(@Nullable Materials mat, int amount) {
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
    private static FluidStack optionalMaterialsFluid(String materialsFieldName, int amount) {
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
    private static FluidStack optionalMaterialsField(Class<?> holder, String fieldName, int amount) {
        try {
            Object raw = holder.getField(fieldName)
                .get(null);
            return fluidStackFromMaterialLike(raw, amount);
        } catch (ReflectiveOperationException | ClassCastException ignored) {
            return null;
        }
    }

    @Nullable
    private static FluidStack optionalMaterialsField(String className, String fieldName, int amount) {
        try {
            return optionalMaterialsField(Class.forName(className), fieldName, amount);
        } catch (ClassNotFoundException | LinkageError ignored) {
            return null;
        }
    }

    @Nullable
    private static FluidStack optionalMaterialsFluidRegistryFallback(String fieldName, int amount) {
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
    private static FluidStack fluidStackFromMaterialLike(@Nullable Object mat, int amount) {
        if (mat == null) {
            return null;
        }
        if (mat instanceof Materials) {
            return fluidStackFromMaterial((Materials) mat, amount);
        }
        return fluidStackFromReflectiveMaterial(mat, amount);
    }

    @Nullable
    private static FluidStack fluidStackFromReflectiveMaterial(Object mat, int amount) {
        long asLong = amount;
        String[] methodNames = { "getFluid", "getMolten" };
        Class<?>[] numericTypes = { long.class, int.class };
        for (String methodName : methodNames) {
            for (Class<?> numType : numericTypes) {
                try {
                    Method m = mat.getClass().getMethod(methodName, numType);
                    Object arg = numType == long.class ? asLong : amount;
                    Object r = m.invoke(mat, arg);
                    if (r instanceof FluidStack) {
                        FluidStack fs = (FluidStack) r;
                        if (fs.getFluid() != null) {
                            return fs.copy();
                        }
                    }
                } catch (NoSuchMethodException ignored) {
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    private static void ensureTierLists() {
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

    private static ArrayList<FluidStack> mergeFluidStacks(List<FluidStack> raw) {
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

    public TST_MegaNqGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public TST_MegaNqGenerator(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaNqGenerator(this.mName);
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
            this.lockedFluid = new FluidStack(FluidRegistry.getFluid(lockedName), aNBT.getInteger("mLockedFluidAmount"));
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
        int maxParallel = Config.Parallel_MegaNqGenerator;
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
        lockedFluid = excitedInfo == null ? null : excitedInfo.getKey()
            .copy();
        mMaxProgresstime = tRecipe.mDuration;
        this.lEUt = 0;
        this.setPowerFlow(0);
        this.trueEff = 0;
        this.trueOutput = 0;
        return CheckRecipeResultRegistry.GENERATING;
    }

    private void updateRunTimeDiscountState(boolean isRunning) {
        if (isRunning) {
            runTimeTicks = Math.min(runTimeTicks + 1, TICKS_TO_MAX_DISCOUNT);
        } else if (wasRunning) {
            runTimeTicks = Math.max(0, runTimeTicks - DECAY_PER_IDLE_TICK);
        }
        wasRunning = isRunning;
    }

    private boolean tickGenerationSecond() {
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

    private void clearPowerState() {
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

    private int consumeCoolantDiscounted(FluidStack[] input, int count) {
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

    private static boolean canAcceptPower(MTEHatchDynamoMulti hatch, long outputPower) {
        return (long) hatch.maxEUOutput() * hatch.maxAmperesOut() >= outputPower;
    }

    private static boolean canAcceptPower(MTEHatchDynamo hatch, long outputPower) {
        return (long) hatch.maxEUOutput() * hatch.maxAmperesOut() >= outputPower;
    }

    private static void chargeDynamoMulti(MTEHatchDynamoMulti hatch, long outputPower) {
        long cap = Math.min(hatch.maxEUStore(), hatch.getBaseMetaTileEntity()
            .getStoredEU() + outputPower);
        hatch.setEUVar(cap);
    }

    private static void chargeDynamo(MTEHatchDynamo hatch, long outputPower) {
        long cap = Math.min(hatch.maxEUStore(), hatch.getBaseMetaTileEntity()
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

    private static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_MNG";
    private final int hOffset = 3, vOffset = 7, dOffset = 0;
    private static IStructureDefinition<TST_MegaNqGenerator> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_MegaNqGenerator> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaNqGenerator>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] {
                            { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "AAAAAAA", "A     A", "A CCC A", "A CPC A", "A CCC A", "A     A", "AAAAAAA" },
                            { "ANNNNNA", "N     N", "N CCC N", "N CPC N", "N CCC N", "N     N", "ANNNNNA" },
                            { "XXX~XXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX" }, }))
                .addElement(
                    'X',
                    ofChain(
                        buildHatchAdder(TST_MegaNqGenerator.class)
                            .atLeast(
                                TTMultiblockBase.HatchElement.DynamoMulti.or(Dynamo),
                                TTMultiblockBase.HatchElement.EnergyMulti.or(gregtech.api.enums.HatchElement.Energy),
                                InputHatch,
                                OutputHatch,
                                gregtech.api.enums.HatchElement.Maintenance)
                            .casingIndex(44)
                            .dot(1)
                            .build(),
                        ofBlock(GregTechAPI.sBlockCasings3, 12)))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings3, 12))
                .addElement('N', ofBlock(Loaders.radiationProtectionSteelFrame, 0))
                .addElement('C', ofBlock(Loaders.MAR_Casing, 0))
                .addElement('P', ofBlock(GregTechAPI.sBlockCasings2, 15))
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
        if (side == facing) {
            if (aActive) {
                return new ITexture[] {
                    casingTexturePages[0][44],
                    TextureFactory.builder()
                        .addIcon(NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
            return new ITexture[] {
                casingTexturePages[0][44],
                TextureFactory.builder()
                    .addIcon(NAQUADAH_REACTOR_SOLID_FRONT)
                    .extFacing()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][44] };
    }

    // endregion

    // region Overrides (tooltip, UI, misc)

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(tr("Tooltip_MegaNqGenerator_MachineType"))
            .addInfo(tr("Tooltip_MegaNqGenerator_01"))
            .addInfo(tr("Tooltip_MegaNqGenerator_02"))
            .addInfo(tr("Tooltip_MegaNqGenerator_03"))
            .addInfo(tr("Tooltip_MegaNqGenerator_04"))
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
        // #tr GUI.MegaNqGenerator.RunningTime
        // # Running Time:
        // #zh_CN 持续运行时间:
        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(() -> tr("GUI.MegaNqGenerator.RunningTime") + formatRunTime(runTimeTicks))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> runTimeTicks, val -> runTimeTicks = val))
            .widget(
                new TextWidget()
                    // #tr GUI.MegaNqGenerator.ConsumptionDiscount
                    // # Consumption Discount:
                    // #zh_CN 消耗减免:
                    .setStringSupplier(() -> tr("GUI.MegaNqGenerator.ConsumptionDiscount") + getConsumptionDiscount() + "%")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(
                new TextWidget()
                    // #tr GUI.MegaNqGenerator.CurrentOutput
                    // # Current Output:
                    // #zh_CN 当前输出:
                    .setStringSupplier(
                        () -> tr("GUI.MegaNqGenerator.CurrentOutput") + GTUtility.formatNumbers(trueOutput) + " EU/t")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(() -> trueOutput, val -> trueOutput = val));
    }

    private static String formatRunTime(long ticks) {
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
