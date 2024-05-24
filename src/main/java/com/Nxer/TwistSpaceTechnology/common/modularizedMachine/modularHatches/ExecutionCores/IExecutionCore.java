package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;

import gregtech.api.interfaces.tileentity.IVoidable;

public interface IExecutionCore extends IVoidable {

    /**
     * Init this execution core module when main machine checkMachine.
     *
     * @param mainMachine The main machine object.
     * @return Success to init this execution core module.
     */
    boolean setup(IModularizedMachine.ISupportExecutionCore mainMachine);

    /**
     * Reset this execution core module when main machine was destroyed.
     */
    void reset();

    /**
     * Shut down this execution core module when maine machine was shut down (always by drain power).
     */
    void shutDown();

    /**
     * @return If true this execution core module is idle,new recipe detection can be performed.
     */
    boolean isIdle();

    /**
     * Get the main machine object.
     *
     * @return The main machine object.
     */
    IModularizedMachine.ISupportExecutionCore getMainMachine();

    // region Setters
    IExecutionCore setOutputItems(ItemStack[] outputItems);

    IExecutionCore setOutputFluids(FluidStack[] outputFluids);

    IExecutionCore setMaxProgressingTime(int maxProgressingTime);

    IExecutionCore setEut(long eut);

}
