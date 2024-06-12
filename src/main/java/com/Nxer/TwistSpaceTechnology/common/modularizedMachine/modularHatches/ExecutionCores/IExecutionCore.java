package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.ProcessingLogic;

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
     * @return If true this execution core module is working.
     */
    boolean isWorking();

    /**
     * Get the main machine object.
     *
     * @return The main machine object.
     */
    IModularizedMachine.ISupportExecutionCore getMainMachine();

    void runExecutionCoreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick);

    /**
     * Set this execution core processing. Get parameters from the main machine's processing logic object after check
     * recipe and calculate overclock.
     *
     * @param processingLogic Calculated parameters stored in.
     * @return Success to set processing
     */
    boolean setProcessing(ProcessingLogic processingLogic);

    /**
     * @return The tick amount of maxProgressingTime - progressedTime
     */
    int getNeedProgressingTime();

    /**
     * @param tick Boosted tick amount.
     * @return This execution core.
     */
    IExecutionCore boostTick(int tick);

    // region Getters and Setters
    IExecutionCore setOutputItems(ItemStack[] outputItems);

    IExecutionCore setOutputFluids(FluidStack[] outputFluids);

    IExecutionCore setMaxProgressingTime(int maxProgressingTime);

    IExecutionCore setEut(long eut);

    /**
     * Finish parameter setting.
     *
     * @return Success.
     */
    boolean done();

    /**
     * @return If true this execution core use main machine power system, or special handle power things.
     */
    boolean useMainMachinePower();

    long getEut();

}
