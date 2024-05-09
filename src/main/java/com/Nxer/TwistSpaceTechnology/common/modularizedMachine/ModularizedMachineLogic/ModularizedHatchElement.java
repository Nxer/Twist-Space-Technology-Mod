package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.DynamicParallelController;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.util.IGT_HatchAdder;

public enum ModularizedHatchElement implements IHatchElement<ModularizedMachineBase> {

    ParallelController(ModularizedMachineBase::addParallelControllerToMachineList, DynamicParallelController.class) {

        @Override
        public long count(ModularizedMachineBase modularizedMachineBase) {
            Map<ModularHatchTypes, Collection<IModularHatch>> modularHatches = modularizedMachineBase
                .getModularHatchMap();
            if (modularHatches == null || modularHatches.isEmpty()
                || !modularHatches.containsKey(ModularHatchTypes.PARALLEL_CONTROLLER)) {
                return 0;
            }
            return modularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER)
                .size();
        }
    };

    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGT_HatchAdder<ModularizedMachineBase> adder;

    @SafeVarargs
    ModularizedHatchElement(IGT_HatchAdder<ModularizedMachineBase> adder,
        Class<? extends IMetaTileEntity>... mteClasses) {
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

}
