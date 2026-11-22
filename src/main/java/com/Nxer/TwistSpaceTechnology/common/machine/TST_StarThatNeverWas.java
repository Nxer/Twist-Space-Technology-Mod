package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TSTUtils.tr;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.getBlueprintWithDot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_LaserMeteorMiner.MiningOverclockLogic;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_StarThatNeverWas;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarBatch;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarBeamSequence;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarControllerFrame;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarDronePorts;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarFieldTransition;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMath;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarRecipePlanner;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarStructure;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import thaumcraft.common.tiles.TileOwned;

@SkipGenerateDescription
public final class TST_StarThatNeverWas extends GTCM_MultiMachineBase<TST_StarThatNeverWas>
    implements INEIPreviewModifier {

    private static final int METEOR_INTERVAL_TICKS = 2 * 20;

    private StarBatch batch;
    private StarBatch pending;
    private final List<ItemStackLong> abortedDroneReturns = new ArrayList<>();
    private TST_LaserMeteorMiner miner;
    private final MiningOverclockLogic miningOverclock = new MiningOverclockLogic();
    private int renderStyle;
    private boolean renderEnabled = true;
    private boolean ringRenderEnabled = true, miningRenderEnabled = true, meteorRenderEnabled = true;
    private boolean meteorAnimationEnabled = true;
    private TileStarContainment ringReceiver;
    private boolean preview;
    private boolean running, advancing;
    private long nextSeed = 0x53544152L;
    private long nextMeteorStartTick;
    private boolean seedInitialized;
    private final StarFieldTransition field = new StarFieldTransition();
    private final StarBeamSequence beam = new StarBeamSequence();
    private double beamSourceDepth;
    private boolean fieldReady;
    private final StarDronePorts dronePorts = new StarDronePorts();
    private int glassTier;
    private boolean droneStructureReady;
    private final StarControllerFrame controllerFrame = new StarControllerFrame(true);
    private int starControllerState;

    public TST_StarThatNeverWas(int id, String name, String regionalName) {
        super(id, name, regionalName);
        machineMode = StarBatch.MODE_BATCH;
        setMEOutput(true);
        // #tr tst.star.preview.channel
        // # Megastructure (0-1 shaft, 2-5 body and middle ring, 6 outer ring, 7 upper rings and supports, 8 full).
        // #zh_CN Megastructure (0-1 中央矿井, 2-5 主体与中圈, 6 外圈, 7 上部环体与支撑, 8 整机).
        StructureLibAPI
            .registerChannelDescription(StarStructure.CHANNEL, "TwistSpaceTechnology", "tst.star.preview.channel");
    }

    public TST_StarThatNeverWas(String name) {
        super(name);
        machineMode = StarBatch.MODE_BATCH;
        setMEOutput(true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new TST_StarThatNeverWas(mName);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity base) {
        super.onFirstTick(base);
        if (base.isServerSide() && !seedInitialized) {
            nextSeed = base.getWorld().rand.nextLong();
            seedInitialized = true;
            markDirty();
        }
    }

    @Override
    public void onEnableWorking() {
        super.onEnableWorking();
        var base = getBaseMetaTileEntity();
        if (base != null && base.isServerSide()) {
            updateMiner();
            scheduleRecipeCheckImmediate();
        }
    }

    @Override
    public void onDisableWorking() {
        super.onDisableWorking();
        var base = getBaseMetaTileEntity();
        if (base != null && base.isServerSide()) {
            running = advancing = false;
            lEUt = 0;
            updateMiner();
        }
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new TST_Gui_StarThatNeverWas(this).withMachineModeIcons(getMachineModeIcons());
    }

    public int getStarRadius() {
        return batch == null ? 0 : batch.radius;
    }

    public int getStarFortune() {
        return batch == null ? 0 : batch.fortune;
    }

    public int getStarDrones() {
        return batch == null ? 0 : batch.drones;
    }

    public long getStarLifeEssence() {
        return batch == null ? 0 : batch.lifeEssence;
    }

    public long getStarParallel() {
        return batch == null ? 1 : batch.parallel;
    }

    public long getStarEnergyUsage() {
        return Math.max(0, -lEUt);
    }

    public int getStarPhase() {
        if (!abortedDroneReturns.isEmpty()) return 5;
        if (batch == null) return 0;
        if (batch.progress >= batch.duration) return batch.outputs.isEmpty() && !batch.droneReturns.isEmpty() ? 5 : 4;
        if (batch.isStaged() && !batch.outputs.isEmpty()) return 4;
        if (batch.progress < batch.formationTicks) return 1;
        if (batch.progress < batch.miningStartTick()) return 2;
        return 3;
    }

    public boolean getClusteredRender() {
        return renderStyle == 1;
    }

    public void setClusteredRender(boolean clustered) {
        renderStyle = clustered ? 1 : 0;
        updateRenderSettings();
    }

    public boolean isRenderEnabled() {
        return renderEnabled;
    }

    public void setRenderEnabled(boolean enabled) {
        renderEnabled = enabled;
        updateRenderSettings();
    }

    public boolean isRingRenderEnabled() {
        return ringRenderEnabled;
    }

    public void setRingRenderEnabled(boolean enabled) {
        ringRenderEnabled = enabled;
        updateRenderSettings();
    }

    public boolean isMiningRenderEnabled() {
        return miningRenderEnabled;
    }

    public void setMiningRenderEnabled(boolean enabled) {
        miningRenderEnabled = enabled;
        updateRenderSettings();
    }

    public boolean isMeteorRenderEnabled() {
        return meteorRenderEnabled;
    }

    public void setMeteorRenderEnabled(boolean enabled) {
        meteorRenderEnabled = enabled;
        updateRenderSettings();
    }

    public boolean isMeteorAnimationEnabled() {
        return meteorAnimationEnabled;
    }

    public void setMeteorAnimationEnabled(boolean enabled) {
        meteorAnimationEnabled = enabled;
        updateRenderSettings();
    }

    public boolean shouldRenderMining() {
        return renderEnabled && miningRenderEnabled;
    }

    private void updateRenderSettings() {
        markDirty();
        updateVisual();
    }

    public boolean ownsRingPosition(int x, int y, int z) {
        return !preview && ringReceiver != null && !ringReceiver.isInvalid() && ringReceiver.ownsRingPosition(x, y, z);
    }

    @Override
    public List<ItemStackLong> getMEItemOutputInfo() {
        return batch == null ? Collections.emptyList() : new ArrayList<>(batch.currentOutputs());
    }

    @Override
    public IStructureDefinition<TST_StarThatNeverWas> getStructureDefinition() {
        return StarStructure.definition();
    }

    public boolean addGeneralHatch(IGregTechTileEntity tile, int texture) {
        return tile != null && acceptsGeneralHatch(tile.getMetaTileEntity()) && addToMachineList(tile, texture);
    }

    public static boolean acceptsGeneralHatch(IMetaTileEntity hatch) {
        return hatch != null && !(hatch instanceof MTEHatchOutput)
            && (!(hatch instanceof MTEHatchOutputBus) || hatch instanceof MTEHatchOutputBusME);
    }

    public boolean addDroneInput(IGregTechTileEntity tile, int texture) {
        return dronePorts.input(tile, texture);
    }

    public boolean addDroneOutput(IGregTechTileEntity tile, int texture) {
        return dronePorts.output(tile, texture);
    }

    public boolean addDroneFluidInput(IGregTechTileEntity tile, int texture) {
        return dronePorts.fluid(tile, texture);
    }

    public int getGlassTier() {
        return glassTier;
    }

    public void setGlassTier(int glassTier) {
        this.glassTier = glassTier;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        dronePorts.clear();
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated()
            && f.isNotFlipped()
            && (ringReceiver == null || !ringReceiver.hasStoredRings() || d == getExtendedFacing().getDirection());
    }

    @Override
    public void onPreviewConstruct(@NotNull ItemStack trigger) {
        preview = true;
    }

    @Override
    public void onPreviewStructureComplete(@NotNull ItemStack trigger) {
        StarStructure.finishPreviewHatches(this);
        int selected = StarStructure.selectedPreviewSection(trigger);
        if (selected != StarStructure.UPPER_PREVIEW_SECTION && selected != StarStructure.FULL_PREVIEW_SECTION) return;
        if (tileAt(0, StarStructure.RECEIVER_Y, 0) instanceof TileStarContainment receiver) {
            receiver.formed = true;
            // Show the containment volume without a recipe meteor in NEI.
            receiver.radius = 0;
        }
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        int selected = StarStructure.selectedPreviewSection(trigger);
        boolean includeDrones = StarStructure.includesDroneStructure(trigger.stackSize);
        for (var part : StarStructure.parts()) {
            if (part.droneStructure && !includeDrones) continue;
            if ((preview || hintsOnly) && !part.isInPreviewSection(selected)) continue;
            buildPiece(part.name, trigger, hintsOnly, part.a, part.b, part.c);
        }
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int budget, ISurvivalBuildEnvironment env) {
        if (mMachine && !preview) return -1;
        int selected = StarStructure.selectedPreviewSection(trigger);
        boolean includeDrones = StarStructure.includesDroneStructure(trigger.stackSize);
        int built = 0;
        for (var part : StarStructure.parts()) {
            if (part.droneStructure && !includeDrones) continue;
            if (preview && !part.isInPreviewSection(selected)) continue;
            int result = survivalBuildPiece(
                part.name,
                trigger,
                part.a,
                part.b,
                part.c,
                budget - built,
                env,
                false,
                true);
            if (result >= 0) {
                built += result;
                if (result == 0 || built >= budget) return built;
            }
        }
        return built == 0 ? -1 : built;
    }

    @Override
    public void checkMachine(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        ringReceiver = tileAt(0, StarStructure.RECEIVER_Y, 0) instanceof TileStarContainment receiver ? receiver : null;
        repairMachine();
        glassTier = 0;
        droneStructureReady = false;
        dronePorts.clear();
        for (var part : StarStructure.parts()) {
            if (!part.droneStructure && !checkPiece(part.name, part.a, part.b, part.c, errors)) return;
        }
        if (StarStructure.includesDroneStructure(glassTier)) {
            for (var part : StarStructure.parts()) {
                if (part.droneStructure && !checkPiece(part.name, part.a, part.b, part.c, errors)) return;
            }
            droneStructureReady = true;
        }
        // During development, missing services do not block formation/rendering. Check them before paying for a batch.
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        if (!abortedDroneReturns.isEmpty()) return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        if (batch != null) return CheckRecipeResultRegistry.SUCCESSFUL;
        pending = null;
        long clock = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime();
        if (clock < nextMeteorStartTick) {
            // #tr tst.star.result.meteor_interval
            // # Waiting between meteors
            // #zh_CN 陨星生成间隔中
            return SimpleResultWithText.ofFailure("tst.star.result.meteor_interval");
        }
        if (mInputBusses.isEmpty() || mInputHatches.isEmpty()
            || mOutputBusses.stream()
                .noneMatch(bus -> bus instanceof MTEHatchOutputBusME))
            return CheckRecipeResultRegistry.NO_RECIPE;
        StarRecipePlanner plan;
        try {
            plan = StarRecipePlanner.match(getLongItemInputs().getSources(), getLongFluidInputs().getSources());
            if (plan == null) return CheckRecipeResultRegistry.NO_RECIPE;
            long availablePower = getMaxInputEu();
            setProcessingLogicPower(miningOverclock);
            OverclockCalculator reference = miningOverclock.calculate();
            long miningEut = reference.getConsumption();
            CheckRecipeResult power = plan.checkPower(miningEut, availablePower);
            if (!power.wasSuccessful()) return power;
            if (!field.isOpen(clock)) {
                // #tr tst.star.result.field_forming
                // # Containment field forming
                // #zh_CN 隔离场展开中
                return SimpleResultWithText.ofFailure("tst.star.result.field_forming");
            }
            updateMiner();
            if (miner == null || !miner.isLinkedToStar(this)) {
                return SimpleResultWithText.ofFailure("tst.star.result.miner_missing");
            }
            CheckRecipeResult planned = plan.plan(
                dronePorts,
                miner.getFortuneTier(),
                miner.getMiningTier(),
                Math.max(1, reference.getDuration()),
                miningEut,
                availablePower,
                nextSeed);
            if (!planned.wasSuccessful()) return planned;
            plan.batch.configureMode(machineMode);
        } catch (ArithmeticException overflow) {
            return SimpleResultWithText.ofFailure("tst.star.result.numeric_limit");
        } catch (IllegalArgumentException invalid) {
            return SimpleResultWithText.ofFailure("tst.star.result.invalid_meteor");
        }
        if (!plan.batch.droneReturns.isEmpty() && !dronePorts.hasReturnBus())
            return CheckRecipeResultRegistry.NO_RECIPE;
        CheckRecipeResult capacity = checkMEOutputCapacity(plan.batch.initialOutputs(), Collections.emptyList());
        if (!capacity.wasSuccessful()) return capacity;
        if (!dronePorts.canReturn(plan.batch.droneReturns)) return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        if (!plan.inputs.commit()) {
            updateSlots();
            dronePorts.markInputsDirty();
            markDirty();
            return SimpleResultWithText.ofFailure("tst.star.result.input_changed");
        }
        pending = plan.batch;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void endRecipeProcessing() {
        super.endRecipeProcessing();
        if (pending != null) {
            if (checkRecipeResult.wasSuccessful()) {
                batch = pending;
                dronePorts.heldHelium -= batch.fleet.paidHelium;
                // The previous wave may still be draining while this meteor begins forming.
                // request(true) starts a new sequence only when the next mining phase connects.
                nextSeed = new java.util.Random(nextSeed).nextLong();
                mMaxProgresstime = batch.operationDuration();
                mProgresstime = 0;
                mEfficiency = 10000;
                lEUt = -batch.formationEut;
                markDirty();
                updateVisual();
            } else {
                abortedDroneReturns.addAll(pending.droneReturns);
                markDirty();
            }
            pending = null;
            updateSlots();
            dronePorts.markInputsDirty();
        }
    }

    @Override
    protected void runMachine(IGregTechTileEntity base, long tick) {
        running = advancing = false;
        if (!abortedDroneReturns.isEmpty()) return;
        if (batch == null) {
            super.runMachine(base, tick);
            return;
        }
        // The host controls both machines. Pausing retains the paid batch and does not consume more inputs.
        if (!base.isAllowedToWork()) {
            // #tr tst.star.result.paused
            // # Paused; the paid meteor batch is retained.
            // #zh_CN 已暂停, 保留当前已扣料的陨星批次.
            pausePaidBatch("tst.star.result.paused");
            return;
        }
        if (!droneStructureReady && !batch.droneReturns.isEmpty()) {
            // #tr tst.star.result.drone_structure
            // # Restore the Infinity Glass drone structure to resume this batch.
            // #zh_CN 恢复无尽玻璃等级和无人机结构后可继续本批次.
            pausePaidBatch("tst.star.result.drone_structure");
            return;
        }
        if (batch.interrupted) {
            batch.interrupted = false;
            markDirty();
        }
        if (batch.progress < batch.duration) {
            if (!field.isOpen(
                base.getWorld()
                    .getTotalWorldTime())) {
                pausePaidBatch("tst.star.result.field_forming");
                return;
            }
            if (miner == null || !miner.isLinkedToStar(this)) {
                pausePaidBatch("tst.star.result.miner_missing");
                return;
            }
            // Paid batches keep their original yield when the miner's fixed Fortune changes.
            int requiredFortune = TST_LaserMeteorMiner.fortuneForTier(batch.miningTier, batch.fortune);
            if (miner.getMiningTier() < batch.miningTier || miner.getFortuneTier() < requiredFortune) {
                pausePaidBatch("tst.star.result.miner_tier");
                return;
            }
        }
        if (batch.legacyUnpaidMiningTicks > 0) {
            lEUt = 0;
            if (tick % 20 != 0) return;
            long due;
            try {
                due = batch.fleet.fuelForMiningTicks(batch.legacyUnpaidMiningTicks);
            } catch (ArithmeticException overflow) {
                setCheckRecipeResult(SimpleResultWithText.ofFailure("tst.star.result.numeric_limit"));
                return;
            }
            var fuel = dronePorts.fuelSource();
            if (due > 0 && (fuel == null || fuel.amount < due || fuel.extract.applyAsLong(due) != due)) {
                markDirty();
                setCheckRecipeResult(StarDronePorts.missingFuel(due));
                return;
            }
            dronePorts.heldHelium -= due;
            batch.fleet.paidHelium = due;
            batch.legacyUnpaidMiningTicks = 0;
            markDirty();
        }
        mEfficiency = 10000;
        mMaxProgresstime = batch.operationDuration();
        mProgresstime = batch.operationProgress();
        if (batch.isStaged() && !batch.outputs.isEmpty() && batch.progress < batch.duration) {
            lEUt = 0;
            if (tick % 20 == 0) deliverStarOutputs();
            batch.advanceOperation();
            mMaxProgresstime = batch.operationDuration();
            mProgresstime = batch.operationProgress();
            setCheckRecipeResult(
                batch.outputs.isEmpty() ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.ITEM_OUTPUT_FULL);
            return;
        }
        if (batch.beamWarmupTicks > 0 && batch.progress >= batch.formationTicks && batch.progress < batch.duration) {
            long clock = base.getWorld()
                .getTotalWorldTime();
            if (beam.request(true, clock)) markDirty();
            // A fault/reload can restart the beam after the batch's original warmup has already elapsed.
            // Keep the meteor visible, but freeze excavation until the new beam has actually connected.
            if (batch.progress >= batch.miningStartTick() && beam.fanExtension(clock) < 1) {
                lEUt = 0;
                running = true;
                setCheckRecipeResult(CheckRecipeResultRegistry.SUCCESSFUL);
                return;
            }
        }
        boolean justCompleted = false;
        if (batch.progress < batch.duration) {
            boolean mining = batch.progress >= batch.miningStartTick();
            lEUt = -(mining ? batch.eut : batch.formationEut);
            if (!onRunningTick(mInventory[1])) return;
            if (mining) batch.fleet.miningTick(batch);
            batch.progress++;
            advancing = true;
            if (batch.beamWarmupTicks > 0 && batch.progress >= batch.miningStartTick()) {
                beam.sampleMining(
                    starMiningProgress(),
                    starMiningDuration(),
                    batch.progress < batch.duration,
                    base.getWorld()
                        .getTotalWorldTime());
            }
            justCompleted = batch.progress == batch.duration;
            // Count the empty interval from the last mined layer, including any time waiting for output space.
            if (justCompleted) nextMeteorStartTick = base.getWorld()
                .getTotalWorldTime() + METEOR_INTERVAL_TICKS;
            mProgresstime = batch.operationProgress();
            running = true;
            setCheckRecipeResult(CheckRecipeResultRegistry.SUCCESSFUL);
            markDirty();
        }
        boolean finishing = batch.progress == batch.duration && (justCompleted || tick % 20 == 0);
        boolean operationCompleted = batch.completeMiningOperation();
        if (operationCompleted || finishing) {
            deliverStarOutputs();
            if (!batch.outputs.isEmpty()) {
                lEUt = 0;
                running = advancing = false;
                setCheckRecipeResult(CheckRecipeResultRegistry.ITEM_OUTPUT_FULL);
            }
        }
        if (finishing) {
            lEUt = 0;
            running = advancing = false;
            dronePorts.deliver(batch.droneReturns);
            // A partially accepted output stays in the paid job; a full network never deletes drones or ores.
            markDirty();
            if (batch.outputs.isEmpty() && batch.droneReturns.isEmpty()) {
                batch = null;
                mProgresstime = mMaxProgresstime = 0;
                recipesDone++;
                scheduleRecipeCheckImmediate();
                setCheckRecipeResult(CheckRecipeResultRegistry.SUCCESSFUL);
            } else {
                setCheckRecipeResult(CheckRecipeResultRegistry.ITEM_OUTPUT_FULL);
            }
        }
        if (batch != null) {
            batch.advanceOperation();
            mMaxProgresstime = batch.operationDuration();
            mProgresstime = batch.operationProgress();
        }
    }

    private void pausePaidBatch(String reason) {
        mMaxProgresstime = 0;
        lEUt = 0;
        setCheckRecipeResult(SimpleResultWithText.ofFailure(reason));
    }

    private void deliverStarOutputs() {
        StarBatch.deliverItems(batch.outputs, entry -> outputItemToMENetwork(entry.itemStack(), entry.stackSize()));
        markDirty();
    }

    public void abortCurrentTask() {
        var base = getBaseMetaTileEntity();
        if (base == null || !base.isServerSide() || batch == null) return;
        abortedDroneReturns.addAll(batch.droneReturns);
        pending = null;
        batch = null;
        running = advancing = false;
        lEUt = 0;
        mProgresstime = mMaxProgresstime = mEfficiency = mEfficiencyIncrease = 0;
        mOutputItems = null;
        mOutputFluids = null;
        nextMeteorStartTick = 0;
        beam.clear();
        base.setActive(false);
        returnAbortedDrones();
        updateVisual();
    }

    private void returnAbortedDrones() {
        if (mMachine) dronePorts.deliver(abortedDroneReturns);
        setCheckRecipeResult(
            abortedDroneReturns.isEmpty() ? CheckRecipeResultRegistry.NONE
                : CheckRecipeResultRegistry.ITEM_OUTPUT_FULL);
        markDirty();
    }

    @Override
    public boolean needsClientTick() {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long tick) {
        controllerFrame.update(base);
        if (base.isServerSide()) {
            long clock = base.getWorld()
                .getTotalWorldTime();
            if (nextMeteorStartTick > 0 && clock >= nextMeteorStartTick) {
                nextMeteorStartTick = 0;
                markDirty();
                scheduleRecipeCheckImmediate();
            }
            field.sample(clock, mMachine && isStarFieldPowered());
            boolean ready = field.isOpen(clock);
            if (ready && !fieldReady) scheduleRecipeCheckImmediate();
            fieldReady = ready;
        }
        super.onPostTick(base, tick);
        if (!base.isServerSide()) return;
        if (!abortedDroneReturns.isEmpty() && mMachine && tick % 20 == 0) returnAbortedDrones();
        if (!mMachine) running = advancing = false;
        if (tick % 20 == 0 || (miner != null && !mMachine)) {
            updateMiner();
        }
        long clock = base.getWorld()
            .getTotalWorldTime();
        if (!mMachine) beam.clear();
        else {
            // Retain the last emitter position while a stopped or removed miner's wave drains.
            if (miner != null && miner.isLinkedToStar(this) && beamSourceDepth != miner.getStarBeamDepth()) {
                beamSourceDepth = miner.getStarBeamDepth();
                markDirty();
            }
            if (batch != null && batch.beamWarmupTicks > 0 && batch.progress >= batch.miningStartTick()) {
                beam.sampleMining(starMiningProgress(), starMiningDuration(), advancing, clock);
            }
            if (beam.request(isStarMinerRunning(), clock)) markDirty();
        }
        field.sample(clock, mMachine && isStarFieldPowered());
        if (tick % 20 == 0 || ringReceiver == null
            || ringReceiver.formed != mMachine
            || ringReceiver.powered != base.isAllowedToWork()
            || !ringReceiver.field.matches(field)
            || !ringReceiver.beam.matches(beam)
            || ringReceiver.running != running
            || ringReceiver.advancing != advancing) updateVisual();
        base.setActive(running);
    }

    private boolean hasUnfinishedMeteor() {
        return batch != null && batch.progress < batch.duration;
    }

    /** An unfinished paid meteor retains containment even while its processing is suspended. */
    public boolean isStarFieldPowered() {
        var base = getBaseMetaTileEntity();
        return base != null && (base.isAllowedToWork() || hasUnfinishedMeteor()
            || beam.visible(
                base.getWorld()
                    .getTotalWorldTime()));
    }

    public boolean isStarMinerRunning() {
        return running && batch != null
            && miner != null
            && miner.isLinkedToStar(this)
            && batch.beamWarmupTicks > 0
            && batch.progress >= batch.formationTicks
            && batch.progress < batch.duration;
    }

    public int starMiningProgress() {
        return batch == null ? 0 : Math.max(0, batch.progress - batch.miningStartTick());
    }

    public int starMiningDuration() {
        return batch == null ? 0 : batch.miningDuration();
    }

    public int starMinerCycleDuration() {
        return batch != null && batch.isStaged() ? batch.operationDuration() : starMiningDuration();
    }

    public int starMinerCycleProgress() {
        return batch != null && batch.isStaged() ? batch.operationProgress() : starMiningProgress();
    }

    private void updateMiner() {
        if (!mMachine) {
            if (miner != null) miner.releaseStar(this);
            miner = null;
            return;
        }
        if (miner != null && miner.claimStar(this)) return;
        miner = null;
        for (int tier = 2; tier >= 1; tier--) {
            TileEntity tile = tileAt(0, StarStructure.MINER_FLOOR_Y + (tier == 2 ? 7 : 6), tier == 2 ? 6 : 2);
            if (tile instanceof IGregTechTileEntity base
                && base.getMetaTileEntity() instanceof TST_LaserMeteorMiner candidate
                && candidate.claimStar(this)) {
                miner = candidate;
                break;
            }
        }
    }

    public TileEntity tileAt(int x, int y, int z) {
        int[] offset = new int[3];
        getExtendedFacing()
            .getWorldOffset(new int[] { x, StarStructure.CONTROLLER_Y - y, StarStructure.CONTROLLER_Z - z }, offset);
        var base = getBaseMetaTileEntity();
        int worldX = base.getXCoord() + offset[0], worldY = base.getYCoord() + offset[1],
            worldZ = base.getZCoord() + offset[2];
        return base.getWorld()
            .blockExists(worldX, worldY, worldZ)
                ? base.getWorld()
                    .getTileEntity(worldX, worldY, worldZ)
                : null;
    }

    public boolean addWardedGlass(TileEntity tile) {
        if (!(tile instanceof TileOwned glass)) return false;
        String owner = getBaseMetaTileEntity().getOwnerName();
        if (!tile.getWorldObj().isRemote && glass.owner.isEmpty() && owner != null && !owner.isEmpty()) {
            glass.owner = owner;
            glass.markDirty();
        }
        return true;
    }

    private void updateVisual() {
        if (getBaseMetaTileEntity() == null || !getBaseMetaTileEntity().isServerSide() || preview) return;
        field.sample(
            getBaseMetaTileEntity().getWorld()
                .getTotalWorldTime(),
            mMachine && isStarFieldPowered());
        if (tileAt(0, StarStructure.RECEIVER_Y, 0) instanceof TileStarContainment receiver) {
            ringReceiver = receiver;
            int[] right = new int[3];
            getExtendedFacing().getWorldOffset(new int[] { 1, 0, 0 }, right);
            int rotation = Math.floorMod((int) Math.round(Math.atan2(-right[2], right[0]) * 2 / Math.PI), 4);
            receiver.configure(
                getBaseMetaTileEntity(),
                mMachine,
                getBaseMetaTileEntity().isAllowedToWork(),
                running,
                advancing,
                batch,
                renderStyle,
                renderEnabled && ringRenderEnabled,
                shouldRenderMining(),
                renderEnabled && meteorRenderEnabled,
                meteorAnimationEnabled,
                rotation,
                field,
                beam,
                beamSourceDepth);
        }
    }

    @Override
    public boolean isFlipChangeAllowed() {
        return !mMachine && (ringReceiver == null || !ringReceiver.hasStoredRings()) && super.isFlipChangeAllowed();
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return !mMachine && (ringReceiver == null || !ringReceiver.hasStoredRings()) && super.isRotationChangeAllowed();
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        // Native shutdown clears progress; the paid batch is deliberately independent of those transient fields.
        super.stopMachine(reason);
        // The normal work switch only prevents another batch, including when the miner is temporarily missing.
        if (batch != null && reason != ShutDownReasonRegistry.NONE) {
            batch.interrupted = true;
            markDirty();
        }
        running = advancing = false;
        long clock = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime();
        if (batch != null && batch.progress >= batch.miningStartTick()) {
            beam.sampleMining(starMiningProgress(), starMiningDuration(), false, clock);
        }
        if (beam.request(false, clock)) markDirty();
        updateVisual();
    }

    @Override
    public void onBlockDestroyed() {
        beam.clear();
        field.sample(
            getBaseMetaTileEntity().getWorld()
                .getTotalWorldTime(),
            false);
        if (miner != null) miner.releaseStar(this);
        if (tileAt(0, StarStructure.RECEIVER_Y, 0) instanceof TileStarContainment receiver) {
            receiver.configure(
                getBaseMetaTileEntity(),
                false,
                false,
                false,
                false,
                null,
                renderStyle,
                false,
                false,
                false,
                false,
                0,
                field,
                beam,
                0);
            receiver.restoreRings(true);
        }
        super.onBlockDestroyed();
    }

    @Override
    public void onRemoval() {
        if (batch != null) dropHeldDrones(batch.droneReturns);
        dropHeldDrones(abortedDroneReturns);
        if (ringReceiver != null) ringReceiver.restoreRings(true);
        if (miner != null) miner.releaseStar(this);
        super.onRemoval();
    }

    private void dropHeldDrones(List<ItemStackLong> drones) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (drones.isEmpty() || base == null || !base.isServerSide()) return;
        for (ItemStackLong entry : drones) {
            long remaining = entry.stackSize();
            while (remaining > 0) {
                ItemStack drop = entry.itemStack()
                    .copy();
                drop.stackSize = (int) Math.min(remaining, drop.getMaxStackSize());
                GTUtility
                    .dropItemToBlockPos(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord(), drop);
                remaining -= drop.stackSize;
            }
        }
        drones.clear();
        markDirty();
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        super.saveNBTData(tag);
        if (batch != null) tag.setTag("starBatch", batch.save());
        else tag.removeTag("starBatch");
        if (abortedDroneReturns.isEmpty()) tag.removeTag("starAbortedDrones");
        else tag.setTag("starAbortedDrones", StarBatch.saveOutputs(abortedDroneReturns));
        tag.setLong("starHeldHelium", dronePorts.heldHelium);
        tag.setInteger("starRenderStyle", renderStyle);
        tag.setBoolean("starRenderEnabled", renderEnabled);
        tag.setBoolean("starRingRenderEnabled", ringRenderEnabled);
        tag.setBoolean("starMiningRenderEnabled", miningRenderEnabled);
        tag.setBoolean("starMeteorRenderEnabled", meteorRenderEnabled);
        tag.setBoolean("starMeteorAnimationEnabled", meteorAnimationEnabled);
        tag.setLong("starNextSeed", nextSeed);
        tag.setLong("starNextMeteorStartTick", nextMeteorStartTick);
        field.write(tag);
        beam.write(tag);
        tag.setDouble("starBeamSourceDepth", beamSourceDepth);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        super.loadNBTData(tag);
        batch = tag.hasKey("starBatch") ? StarBatch.load(tag.getCompoundTag("starBatch")) : null;
        dronePorts.heldHelium = Math.max(0, tag.getLong("starHeldHelium"));
        abortedDroneReturns.clear();
        StarBatch.loadOutputs(tag.getTagList("starAbortedDrones", 10), abortedDroneReturns);
        machineMode = tag.hasKey("machineMode") && tag.getInteger("machineMode") == StarBatch.MODE_STAGED
            ? StarBatch.MODE_STAGED
            : StarBatch.MODE_BATCH;
        renderStyle = tag.getInteger("starRenderStyle") == 1 ? 1 : 0;
        renderEnabled = !tag.hasKey("starRenderEnabled") || tag.getBoolean("starRenderEnabled");
        ringRenderEnabled = !tag.hasKey("starRingRenderEnabled") || tag.getBoolean("starRingRenderEnabled");
        miningRenderEnabled = !tag.hasKey("starMiningRenderEnabled") || tag.getBoolean("starMiningRenderEnabled");
        meteorRenderEnabled = !tag.hasKey("starMeteorRenderEnabled") || tag.getBoolean("starMeteorRenderEnabled");
        meteorAnimationEnabled = !tag.hasKey("starMeteorAnimationEnabled")
            || tag.getBoolean("starMeteorAnimationEnabled");
        if (tag.hasKey("starNextSeed")) nextSeed = tag.getLong("starNextSeed");
        seedInitialized = tag.hasKey("starNextSeed");
        nextMeteorStartTick = Math.max(0, tag.getLong("starNextMeteorStartTick"));
        field.read(tag);
        beam.read(tag);
        beamSourceDepth = Math.max(0, Math.min(64, tag.getDouble("starBeamSourceDepth")));
        if (!Double.isFinite(beamSourceDepth)) beamSourceDepth = 0;
        mMaxProgresstime = mProgresstime = 0;
        lEUt = 0;
    }

    @Override
    public String[] getInfoData() {
        if (batch == null) return super.getInfoData();
        // #tr tst.star.info.batch
        // # Radius: %s (display: %s) | Volume: %s | Fortune: %s
        // #zh_CN 半径: %s (显示: %s) | 体积: %s | 时运: %s

        // #tr tst.star.info.drones
        // # Drones: %s | Infused: %s | Parallel: %s
        // #zh_CN 无人机: %s | 浸血: %s | 并行: %s

        // #tr tst.star.info.cost
        // # Life Essence: %s L | Progress: %s / %s ticks
        // #zh_CN 生命本源：%s L | 进度：%s / %s tick
        return new String[] {
            net.minecraft.util.StatCollector.translateToLocalFormatted(
                "tst.star.info.batch",
                batch.radius,
                StarMath.renderRadius(batch.radius),
                StarMath.volume(batch.radius),
                batch.fortune),
            net.minecraft.util.StatCollector
                .translateToLocalFormatted("tst.star.info.drones", batch.drones, batch.fleet.infused(), batch.parallel),
            net.minecraft.util.StatCollector
                .translateToLocalFormatted("tst.star.info.cost", batch.lifeEssence, batch.progress, batch.duration) };
    }

    @Override
    public boolean protectsExcessItem() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[] { GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_BENDING,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY };
    }

    @Override
    public int totalMachineMode() {
        return 2;
    }

    @Override
    public void setMachineMode(int index) {
        // Active and pending batches keep their captured mode; this selects the next batch.
        super.setMachineMode(index == StarBatch.MODE_STAGED ? StarBatch.MODE_STAGED : StarBatch.MODE_BATCH);
        markDirty();
        scheduleRecipeCheckImmediate();
    }

    @Override
    public String getMachineModeName() {
        // #tr tst.star.mode.0
        // # Stepwise mining
        // #zh_CN 分次开采

        // #tr tst.star.mode.1
        // # Batch mining
        // #zh_CN 整批开采
        return tr("tst.star.mode." + machineMode);
    }

    @Override
    public byte getUpdateData() {
        var base = getBaseMetaTileEntity();
        return (byte) (base == null || !base.isAllowedToWork() ? StarControllerFrame.OFF
            : isStarMinerRunning() ? StarControllerFrame.RUNNING : StarControllerFrame.STANDBY);
    }

    @Override
    public void onValueUpdate(byte value) {
        starControllerState = Math.max(StarControllerFrame.OFF, Math.min(StarControllerFrame.RUNNING, value));
    }

    public int getStarControllerState() {
        return starControllerState;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, ForgeDirection facing, int color,
        boolean active, boolean redstone) {
        return controllerFrame
            .getTexture(side, facing, Textures.BlockIcons.getCasingTextureForId(StarStructure.casingTextureIndex()));
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        // #tr tst.star.subtitle
        // # A Mineable Lie
        // #zh_CN 一个可开采的谎言
        TSTMultiblockTooltipBuilder tooltip = new TSTMultiblockTooltipBuilder() {

            @Override
            public String[] getInformation() {
                return new String[] { tr("tst.common.machine.StarThatNeverWas.name"), tr("tst.star.subtitle") };
            }
        };
        tooltip.addStructureHint("tst.star.preview.channel")
            // #tr tst.star.ports.controller
            // # Controller directly above the T2/T3 miner controller
            // #zh_CN 主机位于二/三级矿机控制器上方一格
            .addStructureHint("tst.star.ports.controller")
            .addInputBus("", getBlueprintWithDot(1), 1)
            .addInputHatch("", getBlueprintWithDot(1, 5), 1, 5)
            .addEnergyHatch("", getBlueprintWithDot(1, 2), 1, 2)
            .addOtherStructurePart(HatchElement.ExoticEnergy.getDisplayName(), getBlueprintWithDot(1, 2), 1, 2)
            .addOtherStructurePart(tr(StarStructure.meOutputBusNameKey()), getBlueprintWithDot(1, 4), 1, 4)
            // #tr tst.star.ports.drone_input
            // # Input Bus (physical inventory)
            // #zh_CN 输入总线 (实体库存)
            .addOtherStructurePart(tr("tst.star.ports.drone_input"), getBlueprintWithDot(3), 3)
            .addOutputBus("", getBlueprintWithDot(4), 4)
            .toolTipFinisher();
        return tooltip;
    }
}
