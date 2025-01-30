package com.Nxer.TwistSpaceTechnology.system.Disassembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

public class TST_SimpleDisassemblyRecipe {

    private TST_ItemID itemToDisassemble = null;
    private int itemAmount = 0;
    private ItemStack[] outputItems = new ItemStack[0];
    private FluidStack[] outputFluids = new FluidStack[0];
    private long eut = 0;
    private byte tier = 0;

    public boolean hasOutputItems() {
        return outputItems.length > 0;
    }

    public boolean hasOutputFluids() {
        return outputFluids.length > 0;
    }

    public List<ItemStack> getOutputItems(int multiplier) {
        if (outputItems.length < 1) return Collections.emptyList();
        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack itemStack : outputItems) {
            long amount = (long) multiplier * itemStack.stackSize;
            if (amount > Integer.MAX_VALUE) {
                long t = amount;
                while (t > Integer.MAX_VALUE) {
                    outputs.add(TstUtils.copyAmountUnlimited(Integer.MAX_VALUE, itemStack));
                    t -= Integer.MAX_VALUE;
                }
                if (t > 0) {
                    outputs.add(TstUtils.copyAmountUnlimited((int) t, itemStack));
                }
            } else {
                outputs.add(TstUtils.copyAmountUnlimited((int) amount, itemStack));
            }
        }
        return outputs;
    }

    public List<FluidStack> getOutputFluids(int multiplier) {
        if (outputFluids.length < 1) return Collections.emptyList();
        List<FluidStack> outputs = new ArrayList<>();
        for (FluidStack fluidStack : outputFluids) {
            long amount = (long) multiplier * fluidStack.amount;
            if (amount > Integer.MAX_VALUE) {
                long t = amount;
                while (t > Integer.MAX_VALUE) {
                    outputs.add(Utils.setStackSize(fluidStack.copy(), Integer.MAX_VALUE));
                    t -= Integer.MAX_VALUE;
                }
                if (t > 0) {
                    outputs.add(Utils.setStackSize(fluidStack.copy(), (int) t));
                }
            } else {
                outputs.add(Utils.setStackSize(fluidStack.copy(), (int) amount));
            }
        }
        return outputs;
    }

    public TST_ItemID getItemToDisassemble() {
        return itemToDisassemble;
    }

    public TST_SimpleDisassemblyRecipe setItemToDisassemble(TST_ItemID itemToDisassemble) {
        this.itemToDisassemble = itemToDisassemble;
        return this;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public TST_SimpleDisassemblyRecipe setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
        return this;
    }

    public ItemStack[] getOutputItems() {
        return outputItems;
    }

    public TST_SimpleDisassemblyRecipe setOutputItems(ItemStack... outputItems) {
        if (outputItems != null && outputItems.length > 0) {
            this.outputItems = Stream.of(outputItems)
                .filter(is -> is != null && is.stackSize > 0)
                .toArray(ItemStack[]::new);
        }
        return this;
    }

    public FluidStack[] getOutputFluids() {
        return outputFluids;
    }

    public TST_SimpleDisassemblyRecipe setOutputFluids(FluidStack... outputFluids) {
        if (outputFluids != null && outputFluids.length > 0) {
            this.outputFluids = outputFluids;
        }
        return this;
    }

    public long getEut() {
        return eut;
    }

    public TST_SimpleDisassemblyRecipe setEut(long eut) {
        this.eut = eut;
        return this;
    }

    public byte getTier() {
        return tier;
    }

    public TST_SimpleDisassemblyRecipe setTier(byte tier) {
        this.tier = tier;
        return this;
    }
}
