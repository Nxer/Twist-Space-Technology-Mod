package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.DynamicParallelController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.StaticParallelController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.PowerConsumptionControllers.StaticPowerConsumptionController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers.DynamicSpeedController;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers.StaticSpeedController;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.util.IGT_HatchAdder;

public enum ModularizedHatchElement implements IHatchElement<ModularizedMachineBase> {

    ParallelController(ModularHatchTypes.PARALLEL_CONTROLLER,
        ModularizedMachineBase::addParallelControllerToMachineList, StaticParallelController.class,
        DynamicParallelController.class),
    SpeedController(ModularHatchTypes.SPEED_CONTROLLER, ModularizedMachineBase::addSpeedControllerToMachineList,
        StaticSpeedController.class, DynamicSpeedController.class),
    PowerConsumptionController(ModularHatchTypes.POWER_CONSUMPTION_CONTROLLER,
        ModularizedMachineBase::addPowerConsumptionControllerToMachineList, StaticPowerConsumptionController.class),

    ;

    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGT_HatchAdder<ModularizedMachineBase> adder;
    private final ModularHatchTypes type;

    @SafeVarargs
    ModularizedHatchElement(ModularHatchTypes type, IGT_HatchAdder<ModularizedMachineBase> adder,
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
    public IGT_HatchAdder<? super ModularizedMachineBase> adder() {
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
