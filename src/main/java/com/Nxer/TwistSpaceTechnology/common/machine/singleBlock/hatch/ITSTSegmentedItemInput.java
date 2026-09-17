package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import java.util.List;

import net.minecraft.item.ItemStack;

/** Supplies item inputs whose equal stacks must remain as separate int-sized recipe inputs. */
public interface ITSTSegmentedItemInput {

    List<ItemStack> getTSTStoredItemSegments();
}
