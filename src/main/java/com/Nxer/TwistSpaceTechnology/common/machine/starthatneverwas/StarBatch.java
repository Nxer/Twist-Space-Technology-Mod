package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToLongFunction;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.github.bsideup.jabel.Desugar;

/** The already-paid job. Changes to input inventories never reinterpret this snapshot. */
public final class StarBatch {

    public static final int MODE_STAGED = 0, MODE_BATCH = 1;
    public int mode = MODE_BATCH;
    public long completedWork;
    public int radius;
    public int duration;
    public int progress;
    public int formationTicks;
    public int beamWarmupTicks;
    public boolean interrupted;
    public int fortune;
    public int miningTier = 1;
    public long parallel = 1;
    public long eut;
    public long formationEut;
    public long lifeEssence;
    public int legacyUnpaidMiningTicks;
    public long seed;
    public int drones;
    public final StarDroneFleet fleet = new StarDroneFleet();
    public final List<ItemStackLong> outputs = new ArrayList<>();
    public final List<ItemStackLong> droneReturns = new ArrayList<>();

    @Desugar
    public record SurfaceMaterial(ItemStack stack, double weight, boolean filler) {}

    public final List<SurfaceMaterial> surface = new ArrayList<>();
    private final List<ItemStackLong> miningOutputs = new ArrayList<>();
    private List<ItemStackLong> operationOutputPreview;
    private StarMeteorExcavation excavation;
    private int operationStart, operationEnd;

    public int miningStartTick() {
        return formationTicks + beamWarmupTicks;
    }

    public int miningDuration() {
        return duration - miningStartTick();
    }

    public void configureMode(int mode) {
        if (progress != 0 || !miningOutputs.isEmpty())
            throw new IllegalStateException("Cannot change the output schedule of a paid batch");
        this.mode = mode == MODE_STAGED ? MODE_STAGED : MODE_BATCH;
        if (this.mode == MODE_STAGED) {
            miningOutputs.addAll(outputs);
            outputs.clear();
            operationEnd = miningStartTick();
        }
    }

    public boolean isStaged() {
        return mode == MODE_STAGED;
    }

    private StarMeteorExcavation excavation() {
        if (excavation == null) excavation = new StarMeteorExcavation(StarMath.renderRadius(radius));
        return excavation;
    }

    private long animatedWork(int miningProgress) {
        int miningTicks = miningDuration();
        return StarMiningCycle.completedWork(
            excavation(),
            miningTier,
            StarMiningCycle.completed(miningProgress, miningTicks),
            miningTicks);
    }

    private List<ItemStackLong> operationOutputs(long from, long to) {
        StarMeteorExcavation cursor = excavation();
        long previous = cursor.removed(miningTier, from), removed = cursor.removed(miningTier, to);
        long volume = cursor.workUnits(1);
        List<ItemStackLong> result = new ArrayList<>();
        for (ItemStackLong output : miningOutputs) {
            long amount = StarMath.proportionalOutput(output.stackSize(), removed, volume)
                - StarMath.proportionalOutput(output.stackSize(), previous, volume);
            if (amount > 0) result.add(new ItemStackLong(output.itemStack(), amount));
        }
        return result;
    }

    public List<ItemStackLong> initialOutputs() {
        if (!isStaged()) return outputs;
        int firstCut = StarMiningCycle.nextCutTick(excavation(), miningTier, 0, miningDuration());
        return operationOutputs(0, animatedWork(firstCut));
    }

    /** Preview the current mining operation without publishing any deliverable output. */
    public List<ItemStackLong> currentOutputs() {
        if (!isStaged() || !outputs.isEmpty() || operationEnd <= miningStartTick() || progress >= operationEnd)
            return outputs;
        if (operationOutputPreview == null)
            operationOutputPreview = operationOutputs(completedWork, animatedWork(operationEnd - miningStartTick()));
        return operationOutputPreview;
    }

    /** Publish exactly one completed operation; retained outputs must be delivered before advancing again. */
    public boolean completeMiningOperation() {
        if (!isStaged() || !outputs.isEmpty() || progress < operationEnd) return false;
        long completed = animatedWork(Math.max(0, progress - miningStartTick()));
        if (completed <= completedWork) return false;
        outputs.addAll(operationOutputs(completedWork, completed));
        completedWork = completed;
        return true;
    }

    public void advanceOperation() {
        if (!isStaged() || !outputs.isEmpty() || progress < operationEnd || progress >= duration) return;
        if (completedWork < animatedWork(Math.max(0, progress - miningStartTick()))) return;
        operationStart = operationEnd;
        operationEnd = Math.addExact(
            miningStartTick(),
            StarMiningCycle.nextCutTick(excavation(), miningTier, completedWork, miningDuration()));
        operationOutputPreview = null;
    }

    public int operationStartTick() {
        return isStaged() ? operationStart : 0;
    }

    public int operationDuration() {
        if (!isStaged()) return duration;
        return operationEnd - operationStart;
    }

    public int operationProgress() {
        return Math.max(0, Math.min(operationDuration(), progress - operationStartTick()));
    }

    public void addOutput(ItemStack stack, long amount) {
        merge(outputs, stack, amount);
    }

    public void addDroneReturn(ItemStack stack, long amount) {
        merge(droneReturns, stack, amount);
    }

    private static void merge(List<ItemStackLong> outputs, ItemStack stack, long amount) {
        if (amount <= 0) return;
        ItemStack unit = stack.copy();
        unit.stackSize = 1;
        for (int i = 0; i < outputs.size(); i++) {
            ItemStackLong old = outputs.get(i);
            if (ItemStack.areItemStacksEqual(old.itemStack(), unit)) {
                outputs.set(i, new ItemStackLong(old.itemStack(), Math.addExact(old.stackSize(), amount)));
                return;
            }
        }
        outputs.add(new ItemStackLong(unit, amount));
    }

    /** Each destination reports the amount still owed after insertion. */
    public static void deliverItems(List<ItemStackLong> outputs, ToLongFunction<ItemStackLong> deliver) {
        for (var it = outputs.listIterator(); it.hasNext();) {
            ItemStackLong entry = it.next();
            long remaining = deliver.applyAsLong(entry);
            if (remaining == 0) it.remove();
            else if (remaining != entry.stackSize()) it.set(new ItemStackLong(entry.itemStack(), remaining));
        }
    }

    public NBTTagCompound save() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("mode", mode);
        nbt.setLong("completedWork", completedWork);
        nbt.setInteger("operationStart", operationStart);
        nbt.setInteger("operationEnd", operationEnd);
        nbt.setInteger("radius", radius);
        nbt.setInteger("duration", duration);
        nbt.setInteger("progress", progress);
        nbt.setInteger("formationTicks", formationTicks);
        nbt.setInteger("beamWarmupTicks", beamWarmupTicks);
        nbt.setBoolean("interrupted", interrupted);
        nbt.setInteger("fortune", fortune);
        nbt.setInteger("miningTier", miningTier);
        nbt.setLong("parallel", parallel);
        nbt.setLong("eut", eut);
        nbt.setLong("formationEut", formationEut);
        nbt.setLong("lp", lifeEssence);
        nbt.setLong("seed", seed);
        nbt.setInteger("drones", drones);
        fleet.save(nbt);
        if (legacyUnpaidMiningTicks > 0) nbt.setInteger("legacyUnpaidMiningTicks", legacyUnpaidMiningTicks);
        nbt.setTag("outputs", saveOutputs(outputs));
        nbt.setTag("droneReturns", saveOutputs(droneReturns));
        if (isStaged()) nbt.setTag("miningOutputs", saveOutputs(miningOutputs));
        NBTTagList palette = new NBTTagList();
        for (SurfaceMaterial material : surface) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag("stack", material.stack.writeToNBT(new NBTTagCompound()));
            entry.setDouble("weight", material.weight);
            entry.setBoolean("filler", material.filler);
            palette.appendTag(entry);
        }
        nbt.setTag("surface", palette);
        return nbt;
    }

    public static NBTTagList saveOutputs(List<ItemStackLong> outputs) {
        NBTTagList result = new NBTTagList();
        for (ItemStackLong output : outputs) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag(
                "stack",
                output.itemStack()
                    .writeToNBT(new NBTTagCompound()));
            entry.setLong("amount", output.stackSize());
            result.appendTag(entry);
        }
        return result;
    }

    public static void loadOutputs(NBTTagList saved, List<ItemStackLong> outputs) {
        for (int i = 0; i < saved.tagCount(); i++) {
            NBTTagCompound entry = saved.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(entry.getCompoundTag("stack"));
            if (stack == null) throw new IllegalStateException("Missing item in paid virtual-meteor job");
            merge(outputs, stack, entry.getLong("amount"));
        }
    }

    public static StarBatch load(NBTTagCompound nbt) {
        StarBatch batch = new StarBatch();
        batch.radius = nbt.getInteger("radius");
        StarMath.volume(batch.radius);
        batch.duration = Math.max(1, nbt.getInteger("duration"));
        batch.progress = Math.max(0, Math.min(batch.duration, nbt.getInteger("progress")));
        batch.formationTicks = Math.max(0, Math.min(batch.duration - 1, nbt.getInteger("formationTicks")));
        batch.beamWarmupTicks = Math
            .max(0, Math.min(batch.duration - batch.formationTicks - 1, nbt.getInteger("beamWarmupTicks")));
        batch.interrupted = nbt.getBoolean("interrupted");
        batch.fortune = nbt.getInteger("fortune");
        batch.miningTier = Math.max(1, Math.min(3, nbt.getInteger("miningTier")));
        batch.parallel = Math.max(1, nbt.getLong("parallel"));
        batch.eut = nbt.getLong("eut");
        batch.formationEut = nbt.getLong("formationEut");
        batch.lifeEssence = nbt.getLong("lp");
        batch.seed = nbt.getLong("seed");
        batch.drones = nbt.getInteger("drones");
        batch.mode = nbt.hasKey("mode") && nbt.getInteger("mode") == MODE_STAGED ? MODE_STAGED : MODE_BATCH;
        batch.completedWork = nbt.getLong("completedWork");
        batch.operationStart = nbt.getInteger("operationStart");
        batch.operationEnd = nbt.getInteger("operationEnd");
        loadOutputs(nbt.getTagList("outputs", 10), batch.outputs);
        loadOutputs(nbt.getTagList("droneReturns", 10), batch.droneReturns);
        batch.fleet.load(nbt, batch.droneReturns);
        batch.legacyUnpaidMiningTicks = nbt.getInteger("legacyUnpaidMiningTicks");
        if (!nbt.hasKey("paidDroneHelium")) {
            int remaining = batch.duration - Math.max(batch.progress, batch.miningStartTick());
            int previousCredit = Math.max(0, Math.min(20, nbt.getInteger("droneFuelTicks")));
            batch.legacyUnpaidMiningTicks = Math.max(0, remaining - previousCredit);
        }
        if (batch.isStaged()) {
            loadOutputs(nbt.getTagList("miningOutputs", 10), batch.miningOutputs);
            long units = batch.excavation()
                .workUnits(batch.miningTier);
            if (batch.completedWork < 0 || batch.completedWork > units
                || batch.operationStart < 0
                || batch.operationEnd <= batch.operationStart
                || batch.operationEnd > batch.duration
                || batch.progress < batch.operationStart
                || batch.progress > batch.operationEnd) throw new IllegalStateException("Invalid paid mining schedule");
        }
        NBTTagList surface = nbt.getTagList("surface", 10);
        for (int i = 0; i < surface.tagCount(); i++) {
            NBTTagCompound entry = surface.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(entry.getCompoundTag("stack"));
            if (stack == null) continue;
            batch.surface.add(new SurfaceMaterial(stack, entry.getDouble("weight"), entry.getBoolean("filler")));
        }
        return batch;
    }
}
