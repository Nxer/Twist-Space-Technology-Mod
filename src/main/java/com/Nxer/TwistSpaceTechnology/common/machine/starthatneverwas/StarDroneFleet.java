package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;

import gtnhintergalactic.item.ItemMiningDrones;

/** Startup fuel and independent infusion checks for the drones held by an already-paid batch. */
public final class StarDroneFleet {

    private int activeTier, ordinary, infused;
    private long idleOrdinary;
    public long paidHelium;

    private static int itemTier(ItemStack stack) {
        if (stack == null || stack.stackSize <= 0
            || !(stack.getItem() instanceof ItemMiningDrones) && !isInfused(stack)) return 0;
        int meta = stack.getItemDamage();
        return meta >= 0 && meta <= ItemMiningDrones.DroneTiers.MAX.ordinal() ? meta + 1 : 0;
    }

    public static int effectiveTier(ItemStack stack) {
        int tier = itemTier(stack);
        if (tier == 0 || !isInfused(stack)) return tier;
        return Math.min(tier + 1, StarMath.MAX_DRONE_TIER);
    }

    static boolean isInfused(ItemStack stack) {
        return stack.getItem() == TstItems.BloodInfusedMiningDrone;
    }

    public void initialize(int activeTier, int ordinary, int infused, long idleOrdinary) {
        this.activeTier = activeTier;
        this.ordinary = ordinary;
        this.infused = infused;
        this.idleOrdinary = idleOrdinary;
        paidHelium = 0;
    }

    public int infused() {
        return infused;
    }

    public long fuelForMiningTicks(int ticks) {
        return Math.multiplyExact(Math.multiplyExact(ordinary + idleOrdinary, 50L), ticks);
    }

    /** Called once, before incrementing batch.progress, only after a successful mining tick. */
    public void miningTick(StarBatch batch) {
        int completed = batch.progress - batch.miningStartTick() + 1;
        if (ordinary == 0 || completed % 20 != 0) return;
        // A distinct stream for each completed second also resumes without rerolling after an NBT reload.
        Random random = new Random(batch.seed ^ (0x9E3779B97F4A7C15L * (completed / 20)));
        for (int i = batch.droneReturns.size() - 1; i >= 0; i--) {
            ItemStackLong entry = batch.droneReturns.get(i);
            if (isInfused(entry.itemStack()) || itemTier(entry.itemStack()) != activeTier) continue;
            int converted = 0;
            for (long drone = 0; drone < entry.stackSize(); drone++) {
                if (random.nextInt(3600) == 0) converted++;
            }
            if (converted == 0) continue;
            long remaining = entry.stackSize() - converted;
            if (remaining == 0) batch.droneReturns.remove(i);
            else batch.droneReturns.set(i, new ItemStackLong(entry.itemStack(), remaining));
            ItemStack result = new ItemStack(
                TstItems.BloodInfusedMiningDrone,
                1,
                entry.itemStack()
                    .getItemDamage());
            if (entry.itemStack()
                .hasTagCompound()) {
                result.setTagCompound(
                    (NBTTagCompound) entry.itemStack()
                        .getTagCompound()
                        .copy());
            }
            batch.addDroneReturn(result, converted);
            ordinary -= converted;
            infused += converted;
        }
    }

    public void save(NBTTagCompound nbt) {
        nbt.setInteger("activeDroneTier", activeTier);
        nbt.setInteger("ordinaryWorkingDrones", ordinary);
        nbt.setInteger("infusedWorkingDrones", infused);
        nbt.setLong("idleOrdinaryDrones", idleOrdinary);
        nbt.setLong("paidDroneHelium", paidHelium);
    }

    public void load(NBTTagCompound nbt, List<ItemStackLong> drones) {
        // Older batches only held drones of their working tier.
        int savedTier = nbt.hasKey("activeDroneTier") ? nbt.getInteger("activeDroneTier")
            : drones.isEmpty() ? 0
                : itemTier(
                    drones.get(0)
                        .itemStack());
        initialize(
            savedTier,
            nbt.getInteger("ordinaryWorkingDrones"),
            nbt.getInteger("infusedWorkingDrones"),
            nbt.getLong("idleOrdinaryDrones"));
        if (!nbt.hasKey("idleOrdinaryDrones")) {
            boolean legacyWorkingFleet = !nbt.hasKey("ordinaryWorkingDrones");
            for (ItemStackLong entry : drones) {
                ItemStack stack = entry.itemStack();
                if (savedTier == 0 || itemTier(stack) != savedTier) {
                    if (!isInfused(stack)) idleOrdinary += entry.stackSize();
                } else if (legacyWorkingFleet) {
                    // Those batches selected by physical grade, including pre-infused drones.
                    int count = Math.toIntExact(entry.stackSize());
                    if (isInfused(stack)) infused = Math.addExact(infused, count);
                    else ordinary = Math.addExact(ordinary, count);
                }
            }
        }
        paidHelium = nbt.getLong("paidDroneHelium");
    }
}
