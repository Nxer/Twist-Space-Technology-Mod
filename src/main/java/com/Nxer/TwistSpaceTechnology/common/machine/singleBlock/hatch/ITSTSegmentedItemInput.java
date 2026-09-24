package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import java.util.List;

import net.minecraft.item.ItemStack;

/** Supplies item inputs whose equal stacks must remain as separate int-sized recipe inputs. */
public interface ITSTSegmentedItemInput {

    /** Use TST's shared int-segment limit for the current recipe check. */
    void setTSTSegmentedInputMode();

    /** Shared ME network identity, or this bus for an independent storage cell. */
    Object getTSTInputSource();

    List<ItemStack> getTSTStoredItemSegments();
}
