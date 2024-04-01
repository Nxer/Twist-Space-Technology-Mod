package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.enableDNAConsuming;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
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
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleBase;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import forestry.api.apiculture.*;
import forestry.apiculture.genetics.Bee;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

public abstract class TST_SpaceApiary extends TileEntityModuleBase {

    public static final long ENERGY_CONSUMPTION = (int) GT_Values.VP[6];// 1A Luv
    Parameters.Group.ParameterIn[] parallelSettings;
    private final Fluid liquddna = FluidRegistry.getFluid("liquiddna");

    private static final INameFunction<TST_SpaceApiary> PARALLEL_SETTING_NAME = (base,
        p) -> GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.cfgi.2") + " "
            + (p.hatchId() / 2 + 1); // Parallels

    private static final IStatusFunction<TST_SpaceApiary> PARALLEL_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getParallels());

    public TST_SpaceApiary(int aID, String aName, String aNameRegional, int tTier, int tModuleTier, int tMinMotorTier) {
        super(aID, aName, aNameRegional, tTier, tModuleTier, tMinMotorTier);
    }

    public TST_SpaceApiary(String aName, int tTier, int tModuleTier, int tMinMotorTier) {
        super(aName, tTier, tModuleTier, tMinMotorTier);
    }

    public double getVoltageTierExact() {
        return Math.log((double) GT_Values.V[6] / 8d) / Math.log(4d) + 1e-8d;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        ItemStack controllerStack = getControllerSlot();

        int mparallel = Math.min((int) parallelSettings[0].get(), getParallels());
        if (ENERGY_CONSUMPTION * mparallel > getEUVar()) {
            return CheckRecipeResultRegistry.insufficientPower(ENERGY_CONSUMPTION * mparallel);
        }

        if (controllerStack != null && beeRoot.getType(controllerStack) == EnumBeeType.QUEEN) {
            double boosted = 1d;

            if (enableDNAConsuming) {
                if (!consumeLiquddna()) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                updateSlots();
            }

            World w = getBaseMetaTileEntity().getWorld();
            float t = (float) getVoltageTierExact();
            List<ItemStack> stacks = new ArrayList<>();
            TST_SpaceApiary.BeeSimulator beeSimulator = new TST_SpaceApiary.BeeSimulator(controllerStack, w, t);
            stacks.addAll(beeSimulator.getDrops(this, 64_00d * boosted * mparallel));

            this.lEUt = -(int) ((double) GT_Values.V[6] * mparallel * 0.99d);
            this.mEfficiency = 10000;
            this.mEfficiencyIncrease = 10000;
            this.mMaxProgresstime = 100;
            this.mOutputItems = stacks.toArray(new ItemStack[0]);

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        return CheckRecipeResultRegistry.NO_RECIPE;

    }

    private boolean consumeLiquddna() {
        if (getStoredFluids().isEmpty()) return false;
        int mparallel = Math.min((int) parallelSettings[0].get(), getParallels());
        for (FluidStack fluid : getStoredFluids()) {
            if (fluid.getFluid() == liquddna) {
                if (fluid.amount >= 100 * mparallel) {
                    fluid.amount -= 100 * mparallel;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return super.checkMachine_EM(aBaseMetaTileEntity, aStack);
    }

    protected int getParallels() {
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
    }

    public static class TST_SpaceApiaryT1 extends TST_SpaceApiary {

        protected static final int MODULE_VOLTAGE_TIER = 10;

        protected static final int MODULE_TIER = 1;

        protected static final int MINIMUM_MOTOR_TIER = 1;

        protected static final int MAX_PARALLELS = 256;

        @Override
        protected int getParallels() {
            return MAX_PARALLELS;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) GT_Values.V[8] / 8d) / Math.log(4d) + 1e-8d; // UV Tier
        }

        public TST_SpaceApiaryT1(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        public TST_SpaceApiaryT1(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT1(mName);
        }

        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t1_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc3)
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

        protected static final int MAX_PARALLELS = 4096;

        @Override
        protected int getParallels() {
            return MAX_PARALLELS;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) GT_Values.V[9] / 8d) / Math.log(4d) + 1e-8d; // UHV Tier
        }

        public TST_SpaceApiaryT2(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        public TST_SpaceApiaryT2(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT2(mName);
        }

        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t2_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc3)
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

        protected static final int MAX_PARALLELS = 32768;

        @Override
        protected int getParallels() {
            return MAX_PARALLELS;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) GT_Values.V[10] / 8d) / Math.log(4d) + 1e-8d; // UEV Tier
        }

        public TST_SpaceApiaryT3(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        public TST_SpaceApiaryT3(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT3(mName);
        }

        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t3_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc3)
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

        protected static final int MAX_PARALLELS = Integer.MAX_VALUE;

        @Override
        protected int getParallels() {
            return MAX_PARALLELS;
        }

        @Override
        public double getVoltageTierExact() {
            return Math.log((double) Integer.MAX_VALUE / 8d) / Math.log(4d) + 1e-8d; // MAX Tier
        }

        public TST_SpaceApiaryT4(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        public TST_SpaceApiaryT4(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new TST_SpaceApiaryT4(mName);
        }

        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addInfo(TextLocalization.Tooltip_SpaceApiary_desc0)
                .addInfo(
                    EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + TextLocalization.Tooltip_SpaceApiary_t4_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc1)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc2)
                .addInfo(TextLocalization.Tooltip_SpaceApiary_desc3)
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
