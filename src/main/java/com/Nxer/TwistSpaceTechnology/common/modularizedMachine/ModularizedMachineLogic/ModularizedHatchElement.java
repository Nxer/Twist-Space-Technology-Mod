package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.AdvExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.ExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.PerfectExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.OverclockControllers.StaticOverclockController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.DynamicParallelController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.StaticParallelController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.PowerConsumptionControllers.StaticPowerConsumptionController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers.DynamicSpeedController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers.StaticSpeedController;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.util.IGTHatchAdder;

public enum ModularizedHatchElement implements IHatchElement<ModularizedMachineBase> {

    ExecutionCoreModule(ModularHatchTypes.EXECUTION_CORE, ModularizedMachineBase::addExecutionCoreToMachineList,
        ExecutionCore.class, AdvExecutionCore.class, PerfectExecutionCore.class),
    ParallelController(ModularHatchTypes.PARALLEL_CONTROLLER,
        ModularizedMachineBase::addParallelControllerToMachineList, StaticParallelController.class,
        DynamicParallelController.class),
    SpeedController(ModularHatchTypes.SPEED_CONTROLLER, ModularizedMachineBase::addSpeedControllerToMachineList,
        StaticSpeedController.class, DynamicSpeedController.class),
    PowerConsumptionController(ModularHatchTypes.POWER_CONSUMPTION_CONTROLLER,
        ModularizedMachineBase::addPowerConsumptionControllerToMachineList, StaticPowerConsumptionController.class),

    OverclockController(ModularHatchTypes.OVERCLOCK_CONTROLLER,
        ModularizedMachineBase::addOverclockControllerToMachineList, StaticOverclockController.class),
    AllModule(ModularHatchTypes.ALL, ModularizedMachineBase::addAnyModularHatchToMachineList, ExecutionCore.class,
        AdvExecutionCore.class, PerfectExecutionCore.class, StaticParallelController.class,
        DynamicParallelController.class, StaticSpeedController.class, DynamicSpeedController.class,
        StaticPowerConsumptionController.class, StaticOverclockController.class) {

        @Override
        public long count(ModularizedMachineBase modularizedMachineBase) {
            return modularizedMachineBase.allModularHatches.size();
        }
    };

    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGTHatchAdder<ModularizedMachineBase> adder;
    private final ModularHatchTypes type;

    @SafeVarargs
    ModularizedHatchElement(ModularHatchTypes type, IGTHatchAdder<ModularizedMachineBase> adder,
        Class<? extends IMetaTileEntity>... mteClasses) {
        this.type = type;
        this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        this.adder = adder;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return mteClasses;
    }

    @Override
    public IGTHatchAdder<? super ModularizedMachineBase> adder() {
        return adder;
    }

    @Override
    public long count(ModularizedMachineBase modularizedMachineBase) {
        Map<ModularHatchTypes, Collection<IModularHatch>> modularHatches = modularizedMachineBase.getModularHatchMap();
        if (modularHatches == null || modularHatches.isEmpty() || !modularHatches.containsKey(this.type)) {
            return 0;
        }
        return modularHatches.get(this.type)
            .size();
    }

}
