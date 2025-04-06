package com.Nxer.TwistSpaceTechnology.system.ExtremeCrafting;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.api.AlternativeItemStack;
import com.Nxer.TwistSpaceTechnology.common.api.IAlternativeItem;
import com.Nxer.TwistSpaceTechnology.common.api.OreDictItem;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

public class ExtremeCraftRecipe {

    public ItemStack outputItem = new ItemStack(Blocks.fire);
    public Map<TST_ItemID, Integer> inputItemsGeneral;
    public Map<IAlternativeItem, Integer> inputItemsAlts;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtremeCraftRecipe that)) {
            return false;
        }
        return Objects.equals(outputItem, that.outputItem) && Objects.equals(inputItemsGeneral, that.inputItemsGeneral)
            && Objects.equals(inputItemsAlts, that.inputItemsAlts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputItem, inputItemsGeneral, inputItemsAlts);
    }

    public int getInputTypeAmount() {
        int t = 0;
        if (inputItemsGeneral != null) t += inputItemsGeneral.size();
        if (inputItemsAlts != null) t += inputItemsAlts.size();
        return t;
    }

    public boolean useAlternativeItem() {
        return inputItemsAlts != null && !inputItemsAlts.isEmpty();
    }

    public ExtremeCraftRecipe itemInputs(ItemStack... itemStacks) {
        inputItemsGeneral = new HashMap<>();
        for (ItemStack itemStack : itemStacks) {
            inputItemsGeneral.merge(TST_ItemID.create(itemStack), itemStack.stackSize, Integer::sum);
        }
        return this;
    }

    public ExtremeCraftRecipe itemInputs(Object... inputs) {
        inputItemsGeneral = new HashMap<>();
        inputItemsAlts = new HashMap<>();
        for (Object o : inputs) {
            if (o == null) continue;
            if (o instanceof ItemStack itemStack) {
                inputItemsGeneral.merge(TST_ItemID.create(itemStack), itemStack.stackSize, Integer::sum);
            } else if (o instanceof AlternativeItemStack alternativeItemStack) {
                inputItemsAlts
                    .merge(alternativeItemStack.alternativeItem, alternativeItemStack.stackSize, Integer::sum);
            } else if (o instanceof Object[]os) {
                if (os.length != 2) throw new IllegalArgumentException();
                if (os[0] instanceof IAlternativeItem alternativeItem) {
                    inputItemsAlts.merge(alternativeItem, ((Number) os[1]).intValue(), Integer::sum);
                } else if (os[0] instanceof String oreDictName) {
                    inputItemsAlts
                        .merge(OreDictItem.getOreDictItems(oreDictName), ((Number) os[1]).intValue(), Integer::sum);
                } else throw new IllegalArgumentException();
            }
        }
        return this;
    }

    public ExtremeCraftRecipe itemOutputs(ItemStack itemStack) {
        if (itemStack != null) {
            outputItem = itemStack;
        }
        return this;
    }

}
