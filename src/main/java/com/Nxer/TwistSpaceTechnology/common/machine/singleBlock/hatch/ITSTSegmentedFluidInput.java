package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;

/** Supplies fluid stacks that must stay separate when GT collects ME inputs. */
public interface ITSTSegmentedFluidInput {

    /** Use TST's shared int-segment limit for the current recipe check. */
    void setTSTSegmentedInputMode();

    /** Shared ME network identity, or this hatch for an independent storage cell. */
    Object getTSTInputSource();

    List<FluidStack> getTSTStoredFluidSegments();
}
