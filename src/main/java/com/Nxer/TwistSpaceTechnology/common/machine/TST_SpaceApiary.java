package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryCycleTime;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryDNACost_T1;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryDNACost_T2;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryDNACost_T3;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryDNACost_T4;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryEnableDisplayInfo;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryMaxParallels_T1;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryMaxParallels_T2;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryMaxParallels_T3;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpaceApiaryMaxParallels_T4;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.enableDNAConsuming;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.apiculture.genetics.Bee;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleBase;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;

public abstract class TST_SpaceApiary extends TileEntityModuleBase {

    // region Class Constructor
    public TST_SpaceApiary(int aID, String aName, String aNameRegional, int tTier, int tModuleTier, int tMinMotorTier,
        int bufferSizeMultiplier) {
        super(aID, aName, aNameRegional, tTier, tModuleTier, tMinMotorTier, bufferSizeMultiplier);
    }

    public TST_SpaceApiary(String aName, int tTier, int tModuleTier, int tMinMotorTier, int bufferSizeMultiplier) {
        super(aName, tTier, tModuleTier, tMinMotorTier, bufferSizeMultiplier);
    }

    // endregion

    // region Statics
    private static final INameFunction<TST_SpaceApiary> PARALLEL_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.project.ig.pump.cfgi.2") + " " + (p.hatchId() / 2 + 1); // Parallels

    private static final IStatusFunction<TST_SpaceApiary> PARALLEL_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getMaxParallels());

    // endregion
    Parameters.Group.ParameterIn[] parallelSettings;
    private final Fluid liquddna = FluidRegistry.getFluid("liquiddna") != null ? FluidRegistry.getFluid("liquiddna")
        : FluidRegistry.getFluid("water");

    protected World world;
    protected float voltageTierExact;

    public double getVoltageTierExact() {
        return Math.log((double) GTValues.V[6] / 8d) / Math.log(4d) + 1e-8d;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick_EM(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            world = getBaseMetaTileEntity().getWorld();
            voltageTierExact = (float) getVoltageTierExact();
        }
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        ItemStack controllerStack = getControllerSlot();
        if (controllerStack == null || beeRoot.getType(controllerStack) != EnumBeeType.QUEEN) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int mParallel = getCurrentParallel();
        if (RECIPE_LuV * mParallel > getEUVar()) {
            return CheckRecipeResultRegistry.insufficientPower(RECIPE_LuV * mParallel);
        }

        if (enableDNAConsuming) {
            if (!consumeLiquidDNA()) {
                return CheckRecipeResultRegistry.NO_FUEL_FOUND;
            }
        }

        updateSlots();

        List<ItemStack> outputs = new ArrayList<>();
        TST_SpaceApiary.BeeSimulator beeSimulator = new TST_SpaceApiary.BeeSimulator(
            controllerStack,
            world,
            voltageTierExact);

        for (ItemStack outItems : beeSimulator.getDrops(this, 6400)) {
            if (outItems == null || outItems.stackSize < 1) continue;
            long amount = (long) outItems.stackSize * mParallel;
            if (amount <= Integer.MAX_VALUE) {
                outputs.add(GTUtility.copyAmountUnsafe((int) amount, outItems));
            } else {
                while (amount >= Integer.MAX_VALUE) {
                    outputs.add(GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, outItems));
                    amount -= Integer.MAX_VALUE;
                }
                if (amount > 0) {
                    outputs.add(GTUtility.copyAmountUnsafe((int) amount, outItems));
                }
            }
        }

        this.lEUt = -RECIPE_LuV * mParallel;
        this.mEfficiency = 10000;
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = SpaceApiaryCycleTime;
        this.mOutputItems = outputs.toArray(new ItemStack[0]);

        return CheckRecipeResultRegistry.SUCCESSFUL;

    }

    protected abstract int getLiquidDnaConsumingAmount();

    private boolean consumeLiquidDNA() {
        if (getStoredFluids().isEmpty()) return false;
        int consumeAmount = getCurrentParallel() * getLiquidDnaConsumingAmount();
        for (FluidStack fluid : getStoredFluids()) {
            if (fluid.getFluid() == liquddna) {
                if (fluid.amount >= consumeAmount) {
                    fluid.amount -= consumeAmount;
                    return true;
                }
            }
        }
        return false;
    }

    protected int getCurrentParallel() {
        return Math.min((int) parallelSettings[0].get(), getMaxParallels());
    }

    protected int getMaxParallels() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void parametersInstantiation_EM() {
        super.parametersInstantiation_EM();
        parallelSettings = new Parameters.Group.ParameterIn[1];
        parallelSettings[0] = parametrization.getGroup(0, false)
            .makeInParameter(0, 1, PARALLEL_SETTING_NAME, PARALLEL_STATUS);
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        if (SpaceApiaryEnableDisplayInfo) {
            screenElements.widget(
                TextWidget.dynamicString(this::generateRecipeInfo)
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> mMachine && mOutputItems != null && mOutputItems.length > 0))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> mProgresstime, val -> mProgresstime = val))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> mMaxProgresstime, val -> mMaxProgresstime = val))
                .widget(
                    new FakeSyncWidget.ListSyncer<>(
                        () -> mOutputItems != null ? Arrays.asList(mOutputItems) : Collections.emptyList(),
                        val -> mOutputItems = val.toArray(new ItemStack[0]),
                        NetworkUtils::writeItemStack,
                        NetworkUtils::readItemStack));
        }
    }

    private String generateRecipeInfo() {
        StringBuilder ret = new StringBuilder("Progress: ").append(String.format("%,.2f", (double) mProgresstime / 20))
            .append("s / ")
            .append(String.format("%,.2f", (double) mMaxProgresstime / 20))
            .append("s (")
            .append(String.format("%,.1f", (double) mProgresstime / mMaxProgresstime * 100))
            .append("%)\n");
        if (mOutputItems != null) {
            for (ItemStack outputItem : mOutputItems) {
                String ItemDisplayName = outputItem.getDisplayName();
                ret.append(" - ")
                    .append(ItemDisplayName)
                    .append(String.format(" x %d", outputItem.stackSize))
                    .append("\n");
            }
        }
        return ret.toString();
    }

    public static class TST_SpaceApiaryT1 extends TST_SpaceApiary {

        protected static final int MODULE_VOLTAGE_TIER = 10;

        protected static final int MODULE_TIER = 1;

        protected static final int MINIMUM_MOTOR_TIER = 1;

        protected static final int MAX_PARALLELS = SpaceApiaryMaxParallels_T1;

        @Override
        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) GTValues.V[8] / 8d) / Math.log(4d) + 1e-8d; // UV Tier
        }

        @Override
        protected int getLiquidDnaConsumingAmount() {
            return SpaceApiaryDNACost_T1;
        }

        public TST_SpaceApiaryT1(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        public TST_SpaceApiaryT1(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT1(mName);
        }

        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t1_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t1_desc3)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t1_desc4)
                .addInfo(translateToLocal("gt.blockmachines.multimachine.project.ig.motorT1"))
                .addSeparator()
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(translateToLocal("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addOutputBus(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher(TextLocalization.ModName);
            return tt;
        }
    }

    public static class TST_SpaceApiaryT2 extends TST_SpaceApiary {

        protected static final int MODULE_VOLTAGE_TIER = 14;

        protected static final int MODULE_TIER = 2;

        protected static final int MINIMUM_MOTOR_TIER = 2;

        protected static final int MAX_PARALLELS = SpaceApiaryMaxParallels_T2;

        @Override
        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        @Override
        protected int getLiquidDnaConsumingAmount() {
            return SpaceApiaryDNACost_T2;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) GTValues.V[9] / 8d) / Math.log(4d) + 1e-8d; // UHV Tier
        }

        public TST_SpaceApiaryT2(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        public TST_SpaceApiaryT2(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT2(mName);
        }

        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t2_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t2_desc3)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t2_desc4)
                .addInfo(translateToLocal("gt.blockmachines.multimachine.project.ig.motorT2"))
                .addSeparator()
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(translateToLocal("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addOutputBus(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher(TextLocalization.ModName);
            return tt;
        }
    }

    public static class TST_SpaceApiaryT3 extends TST_SpaceApiary {

        protected static final int MODULE_VOLTAGE_TIER = 18;

        protected static final int MODULE_TIER = 3;

        protected static final int MINIMUM_MOTOR_TIER = 3;

        protected static final int MAX_PARALLELS = SpaceApiaryMaxParallels_T3;

        @Override
        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        @Override
        protected int getLiquidDnaConsumingAmount() {
            return SpaceApiaryDNACost_T3;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) GTValues.V[10] / 8d) / Math.log(4d) + 1e-8d; // UEV Tier
        }

        public TST_SpaceApiaryT3(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        public TST_SpaceApiaryT3(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT3(mName);
        }

        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t3_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t3_desc3)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t3_desc4)
                .addInfo(translateToLocal("gt.blockmachines.multimachine.project.ig.motorT3"))
                .addSeparator()
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(translateToLocal("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addOutputBus(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher(TextLocalization.ModName);
            return tt;
        }
    }

    public static class TST_SpaceApiaryT4 extends TST_SpaceApiary {

        protected static final int MODULE_VOLTAGE_TIER = 25;

        protected static final int MODULE_TIER = 4;

        protected static final int MINIMUM_MOTOR_TIER = 4;

        protected static final int MAX_PARALLELS = SpaceApiaryMaxParallels_T4;

        @Override
        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        @Override
        protected int getLiquidDnaConsumingAmount() {
            return SpaceApiaryDNACost_T4;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) Integer.MAX_VALUE / 8d) / Math.log(4d) + 1e-8d; // MAX Tier
        }

        public TST_SpaceApiaryT4(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        public TST_SpaceApiaryT4(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT4(mName);
        }

        @Override
        protected MultiblockTooltipBuilder createTooltip() {
            final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t4_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t4_desc3)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t4_desc4)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_t4_desc5)
                .addInfo(translateToLocal("gt.blockmachines.multimachine.project.ig.motorT4"))
                .addSeparator()
                .beginStructureBlock(1, 5, 2, false)
                .addCasingInfoRange(translateToLocal("gt.blockcasings.ig.0.name"), 0, 9, false)
                .addOutputBus(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .addInputHatch(translateToLocal("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher(TextLocalization.ModName);
            return tt;
        }
    }

    final HashMap<TST_ItemID, Double> dropProgress = new HashMap<>();

    // from kubatech, modified
    private static class BeeSimulator {

        final ItemStack queenStack;
        boolean isValid;
        List<TST_SpaceApiary.BeeSimulator.BeeDrop> drops = new ArrayList<>();
        List<TST_SpaceApiary.BeeSimulator.BeeDrop> specialDrops = new ArrayList<>();
        float beeSpeed;

        float maxBeeCycles;
        private static IBeekeepingMode mode;

        public BeeSimulator(ItemStack queenStack, World world, float t) {
            isValid = false;
            this.queenStack = queenStack.copy();
            this.queenStack.stackSize = 1;
            generate(world, t);
            isValid = true;
            // queenStack.stackSize--;
        }

        public void generate(World world, float t) {
            if (mode == null) mode = beeRoot.getBeekeepingMode(world);
            drops.clear();
            specialDrops.clear();
            if (beeRoot.getType(this.queenStack) != EnumBeeType.QUEEN) return;
            IBee queen = beeRoot.getMember(this.queenStack);
            IBeeModifier beeModifier = mode.getBeeModifier();
            float mod = beeModifier.getLifespanModifier(null, null, 1.f);
            int h = queen.getMaxHealth();
            maxBeeCycles = (float) h / (1.f / mod);
            IBeeGenome genome = queen.getGenome();
            IAlleleBeeSpecies primary = genome.getPrimary();
            beeSpeed = genome.getSpeed();
            genome.getPrimary()
                .getProductChances()
                .forEach((key, value) -> drops.add(new TST_SpaceApiary.BeeSimulator.BeeDrop(key, value, beeSpeed, t)));
            genome.getSecondary()
                .getProductChances()
                .forEach(
                    (key, value) -> drops.add(new TST_SpaceApiary.BeeSimulator.BeeDrop(key, value / 2.f, beeSpeed, t)));
            primary.getSpecialtyChances()
                .forEach(
                    (key, value) -> specialDrops
                        .add(new TST_SpaceApiary.BeeSimulator.BeeDrop(key, value, beeSpeed, t)));
        }

        static final Map<TST_ItemID, ItemStack> dropstacks = new HashMap<>();

        public List<ItemStack> getDrops(final TST_SpaceApiary MTE, final double timePassed) {
            drops.forEach(d -> {
                MTE.dropProgress.merge(d.id, d.getAmount(timePassed / 550d), Double::sum);
                if (!dropstacks.containsKey(d.id)) dropstacks.put(d.id, d.stack);
            });
            specialDrops.forEach(d -> {
                MTE.dropProgress.merge(d.id, d.getAmount(timePassed / 550d), Double::sum);
                if (!dropstacks.containsKey(d.id)) dropstacks.put(d.id, d.stack);
            });
            List<ItemStack> currentDrops = new ArrayList<>();
            MTE.dropProgress.entrySet()
                .forEach(e -> {
                    double v = e.getValue();
                    long size = (long) v;
                    ItemStack stack;
                    while (size > 0) {
                        if (size > Integer.MAX_VALUE) {
                            stack = dropstacks.get(e.getKey())
                                .copy();
                            stack.stackSize = Integer.MAX_VALUE;
                            currentDrops.add(stack);
                            size -= Integer.MAX_VALUE;
                            v -= Integer.MAX_VALUE;
                        } else {
                            stack = dropstacks.get(e.getKey())
                                .copy();
                            stack.stackSize = (int) size;
                            currentDrops.add(stack);
                            v -= size;
                            size = 0;
                        }
                    }
                    e.setValue(v);
                });
            return currentDrops;
        }

        private static class BeeDrop {

            private static final float MAX_PRODUCTION_MODIFIER_FROM_UPGRADES = 17.19926784f; // 4*1.2^8
            final ItemStack stack;
            double amount;
            final TST_ItemID id;

            final float chance;
            final float beeSpeed;
            float t;

            public BeeDrop(ItemStack stack, float chance, float beeSpeed, float t) {
                this.stack = stack;
                this.chance = chance;
                this.beeSpeed = beeSpeed;
                this.t = t;
                id = TST_ItemID.create(this.stack);
                evaluate();
            }

            public void evaluate() {

                this.amount = Bee.getFinalChance(
                    chance,
                    beeSpeed,
                    MAX_PRODUCTION_MODIFIER_FROM_UPGRADES + mode.getBeeModifier()
                        .getProductionModifier(null, MAX_PRODUCTION_MODIFIER_FROM_UPGRADES),
                    t);
            }

            public double getAmount(double speedModifier) {
                return amount * speedModifier;
            }

            @Override
            public int hashCode() {
                return id.hashCode();
            }
        }
    }

}
