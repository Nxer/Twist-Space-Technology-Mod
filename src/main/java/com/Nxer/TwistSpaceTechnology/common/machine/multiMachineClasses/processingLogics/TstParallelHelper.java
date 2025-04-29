package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.util.ParallelHelper;

public class TstParallelHelper extends ParallelHelper {

    @Override
    protected void calculateItemOutputs(ItemStack[] truncatedItemOutputs) {
        if (customItemOutputCalculation != null) {
            itemOutputs = customItemOutputCalculation.apply(currentParallel);
            return;
        }
        if (truncatedItemOutputs.length == 0) return;
        ArrayList<ItemStack> itemOutputsList = new ArrayList<>();
        for (int i = 0; i < truncatedItemOutputs.length; i++) {
            if (recipe.getOutput(i) == null) continue;
            ItemStack origin = recipe.getOutput(i)
                .copy();
            final long itemStackSize = origin.stackSize;
            // TWIST MODIFICATION START
            final int outputChance = recipe.getOutputChance(i);
            if (outputChance < 100_00) {
                long fullOutputAmount = (long) currentParallel * itemStackSize * outputChance;

                long guaranteedOutputAmount = fullOutputAmount / 10000;
                addItemsLong(itemOutputsList, origin, guaranteedOutputAmount);
                long leftAmountToRandom = fullOutputAmount % 10000;
                if (leftAmountToRandom > 0 && leftAmountToRandom > XSTR_INSTANCE.nextInt(10000)) {
                    addItemsLong(itemOutputsList, origin, leftAmountToRandom);
                }
            } else {
                addItemsLong(itemOutputsList, origin, (long) currentParallel * itemStackSize);
            }
            // TWIST MODIFICATION END
        }
        itemOutputs = itemOutputsList.toArray(new ItemStack[0]);
    }

}
