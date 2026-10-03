package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.util.NBTUtils;

import gregtech.api.logic.ProcessingLogic;

/** An execution core owns its outputs independently of the controller's reusable processing logic. */
public class ExecutionCoreOutputBuffer {

    private final List<ItemStackLong> itemOutputs = new ArrayList<>();
    private final List<FluidStackLong> fluidOutputs = new ArrayList<>();

    public void capture(ProcessingLogic logic) {
        clear();
        if (logic instanceof GTCM_ProcessingLogic tstLogic && tstLogic.hasLongOutputs()) {
            for (ItemStackLong output : tstLogic.getLongItemOutputs()) {
                addItemOutput(output.itemStack(), output.stackSize());
            }
            for (FluidStackLong output : tstLogic.getLongFluidOutputs()) {
                addFluidOutput(output.fluidStack(), output.amount());
            }
        } else {
            ItemStack[] items = logic.getOutputItems();
            if (items != null) {
                for (ItemStack output : items) {
                    if (output != null) addItemOutput(output, output.stackSize);
                }
            }
            FluidStack[] fluids = logic.getOutputFluids();
            if (fluids != null) {
                for (FluidStack output : fluids) {
                    if (output != null) addFluidOutput(output, output.amount);
                }
            }
        }
    }

    private void addItemOutput(ItemStack stack, long amount) {
        if (stack == null || amount <= 0) return;
        ItemStack template = stack.copy();
        template.stackSize = 1;
        itemOutputs.add(new ItemStackLong(template, amount));
    }

    private void addFluidOutput(FluidStack stack, long amount) {
        if (stack == null || amount <= 0) return;
        FluidStack template = stack.copy();
        template.amount = 1;
        fluidOutputs.add(new FluidStackLong(template, amount));
    }

    public void mergeInto(IModularizedMachine.ISupportExecutionCore machine) {
        machine.mergeOutputItems(itemOutputs);
        machine.mergeOutputFluids(fluidOutputs);
        clear();
    }

    public int getItemOutputCount() {
        return itemOutputs.size();
    }

    public int getFluidOutputCount() {
        return fluidOutputs.size();
    }

    public void clear() {
        itemOutputs.clear();
        fluidOutputs.clear();
    }

    public void saveNBTData(NBTTagCompound nbt) {
        NBTTagCompound outputs = new NBTTagCompound();
        outputs.setBoolean("unified", true);
        NBTTagList items = new NBTTagList();
        for (ItemStackLong output : itemOutputs) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag(
                "stack",
                output.itemStack()
                    .writeToNBT(new NBTTagCompound()));
            entry.setLong("amount", output.stackSize());
            items.appendTag(entry);
        }
        outputs.setTag("items", items);
        NBTTagList fluids = new NBTTagList();
        for (FluidStackLong output : fluidOutputs) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag(
                "stack",
                output.fluidStack()
                    .writeToNBT(new NBTTagCompound()));
            entry.setLong("amount", output.amount());
            fluids.appendTag(entry);
        }
        outputs.setTag("fluids", fluids);
        nbt.setTag("executionCoreLongOutputs", outputs);
    }

    public void loadNBTData(NBTTagCompound nbt, String legacyPrefix) {
        clear();
        NBTTagCompound outputs = nbt.getCompoundTag("executionCoreLongOutputs");
        NBTTagList items = outputs.getTagList("items", TAG_COMPOUND);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound entry = items.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(entry.getCompoundTag("stack"));
            long amount = entry.getLong("amount");
            addItemOutput(stack, amount);
        }
        NBTTagList fluids = outputs.getTagList("fluids", TAG_COMPOUND);
        for (int i = 0; i < fluids.tagCount(); i++) {
            NBTTagCompound entry = fluids.getCompoundTagAt(i);
            FluidStack stack = FluidStack.loadFluidStackFromNBT(entry.getCompoundTag("stack"));
            long amount = entry.getLong("amount");
            addFluidOutput(stack, amount);
        }
        if (!outputs.getBoolean("unified") && itemOutputs.isEmpty() && fluidOutputs.isEmpty()) {
            for (int i = 0; i < nbt.getInteger(legacyPrefix + "ItemsLength"); i++) {
                ItemStack stack = NBTUtils.loadItem(nbt, legacyPrefix + "Items" + i);
                if (stack != null) addItemOutput(stack, stack.stackSize);
            }
            for (int i = 0; i < nbt.getInteger(legacyPrefix + "FluidsLength"); i++) {
                FluidStack stack = NBTUtils.loadFluid(nbt, legacyPrefix + "Fluids" + i);
                if (stack != null) addFluidOutput(stack, stack.amount);
            }
        }
    }
}
