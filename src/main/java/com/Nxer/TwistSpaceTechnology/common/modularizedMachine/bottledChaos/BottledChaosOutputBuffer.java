package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.TSTMEOutputTransaction;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.FluidEjectionHelper;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;

/** Long quantities stay compact in memory and NBT, including while outputs are blocked. */
public final class BottledChaosOutputBuffer {

    private final List<ItemStackLong> items = new ArrayList<>();
    private final List<FluidStackLong> fluids = new ArrayList<>();

    public List<ItemStackLong> itemOutputs() {
        return Collections.unmodifiableList(items);
    }

    public List<FluidStackLong> fluidOutputs() {
        return Collections.unmodifiableList(fluids);
    }

    public boolean isEmpty() {
        return items.isEmpty() && fluids.isEmpty();
    }

    public void clear() {
        items.clear();
        fluids.clear();
    }

    public void set(List<ItemStackLong> itemOutputs, List<FluidStackLong> fluidOutputs) {
        clear();
        for (ItemStackLong output : itemOutputs) {
            items.add(new ItemStackLong(GTUtility.copyAmountUnsafe(1, output.itemStack()), output.stackSize()));
        }
        for (FluidStackLong output : fluidOutputs) {
            fluids.add(new FluidStackLong(GTUtility.copyAmount(1, output.fluidStack()), output.amount()));
        }
    }

    void collectAmounts(Map<Object, Long> amounts) {
        for (ItemStackLong output : items) {
            amounts.merge(TST_ItemID.create(output.itemStack()), output.stackSize(), Math::addExact);
        }
        for (FluidStackLong output : fluids) {
            amounts.merge(GTUtility.FluidId.create(output.fluidStack()), output.amount(), Math::addExact);
        }
    }

    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList itemTags = new NBTTagList();
        for (ItemStackLong output : items) {
            NBTTagCompound entry = output.itemStack()
                .writeToNBT(new NBTTagCompound());
            entry.setLong("amount", output.stackSize());
            itemTags.appendTag(entry);
        }
        tag.setTag("items", itemTags);
        NBTTagList fluidTags = new NBTTagList();
        for (FluidStackLong output : fluids) {
            NBTTagCompound entry = output.fluidStack()
                .writeToNBT(new NBTTagCompound());
            entry.setLong("longAmount", output.amount());
            fluidTags.appendTag(entry);
        }
        tag.setTag("fluids", fluidTags);
        return tag;
    }

    public void load(NBTTagCompound tag) {
        clear();
        NBTTagList itemTags = tag.getTagList("items", 10);
        for (int i = 0; i < itemTags.tagCount(); i++) {
            NBTTagCompound entry = itemTags.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(entry);
            if (stack != null && entry.getLong("amount") > 0) {
                items.add(new ItemStackLong(GTUtility.copyAmountUnsafe(1, stack), entry.getLong("amount")));
            }
        }
        NBTTagList fluidTags = tag.getTagList("fluids", 10);
        for (int i = 0; i < fluidTags.tagCount(); i++) {
            NBTTagCompound entry = fluidTags.getCompoundTagAt(i);
            FluidStack stack = FluidStack.loadFluidStackFromNBT(entry);
            if (stack != null && entry.getLong("longAmount") > 0) {
                fluids.add(new FluidStackLong(GTUtility.copyAmount(1, stack), entry.getLong("longAmount")));
            }
        }
    }

    /** One-tick transaction shared by all reservations. Never commit a failed reservation. */
    public static final class Transaction {

        // Bound work at legacy int-only outputs. Large native ME transfers remain a single operation.
        private static final int MAX_NORMAL_CHUNKS = 64;
        private final List<TSTMEOutputTransaction<GTUtility.ItemId, ItemStack, IAEItemStack>> meItems = new ArrayList<>();
        private final List<TSTMEOutputTransaction<GTUtility.FluidId, FluidStack, IAEFluidStack>> meFluids = new ArrayList<>();
        private final ItemEjectionHelper normalItems;
        private final FluidEjectionHelper normalFluids;
        private final boolean protectItems;
        private final boolean protectFluids;

        public Transaction(IVoidable machine, boolean recipeCheck) {
            this(machine, recipeCheck, false);
        }

        /** Cache exports always protect paid materials, independently of the controller's voiding mode. */
        public Transaction(IVoidable machine, boolean recipeCheck, boolean forceProtect) {
            protectItems = forceProtect || machine.protectsExcessItem();
            protectFluids = forceProtect || machine.protectsExcessFluid();
            List<IOutputBus> buses = new ArrayList<>();
            for (IOutputBus bus : machine.getOutputBusses()) {
                if (bus instanceof MTEHatchOutputBusME me) {
                    meItems.add(TSTMEOutputTransaction.forItems(me, recipeCheck, protectItems));
                } else buses.add(bus);
            }
            List<IOutputHatch> hatches = new ArrayList<>();
            for (IOutputHatch hatch : machine.getOutputHatches()) {
                if (hatch instanceof MTEHatchOutputME me) {
                    meFluids.add(TSTMEOutputTransaction.forFluids(me, recipeCheck, protectFluids));
                } else hatches.add(hatch);
            }
            // Always count actual insertions; voiding is applied only to the remaining amount below.
            normalItems = new ItemEjectionHelper(buses, true, recipeCheck);
            normalFluids = new FluidEjectionHelper(hatches, true, recipeCheck);
        }

        public CheckRecipeResult reserve(BottledChaosOutputBuffer buffer) {
            for (ItemStackLong entry : buffer.items) {
                long remaining = entry.stackSize();
                for (var transaction : meItems) {
                    remaining -= transaction.reserve(entry.itemStack(), remaining);
                    if (remaining == 0) break;
                }
                for (int chunk = 0; remaining > 0 && chunk < MAX_NORMAL_CHUNKS; chunk++) {
                    int offered = (int) Math.min(Integer.MAX_VALUE, remaining);
                    int inserted = normalItems.ejectItems(Collections.singletonList(entry.itemStack()), offered);
                    remaining -= inserted;
                    if (inserted < offered) break;
                }
                if (remaining > 0 && protectItems) return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
            }
            for (FluidStackLong entry : buffer.fluids) {
                long remaining = entry.amount();
                for (var transaction : meFluids) {
                    remaining -= transaction.reserve(entry.fluidStack(), remaining);
                    if (remaining == 0) break;
                }
                for (int chunk = 0; remaining > 0 && chunk < MAX_NORMAL_CHUNKS; chunk++) {
                    int offered = (int) Math.min(Integer.MAX_VALUE, remaining);
                    int inserted = normalFluids.ejectFluids(Collections.singletonList(entry.fluidStack()), offered);
                    remaining -= inserted;
                    if (inserted < offered) break;
                }
                if (remaining > 0 && protectFluids) return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
            }
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        public void commit() {
            meItems.forEach(TSTMEOutputTransaction::commit);
            meFluids.forEach(TSTMEOutputTransaction::commit);
            normalItems.commit();
            normalFluids.commit();
        }
    }
}
