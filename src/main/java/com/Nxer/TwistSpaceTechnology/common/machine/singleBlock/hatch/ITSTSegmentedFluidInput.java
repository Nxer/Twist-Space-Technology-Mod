package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;

/** Supplies fluid stacks that must stay separate when GT collects ME inputs. */
public interface ITSTSegmentedFluidInput {

    List<FluidStack> getTSTStoredFluidSegments();
}
