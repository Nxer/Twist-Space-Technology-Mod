package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses;

import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.util.IGTHatchAdder;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;

public abstract class TST_GeneratorBase<T extends TST_GeneratorBase<T>> extends GTCM_MultiMachineBase<T> {

    // region Class Constructor
    public TST_GeneratorBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_GeneratorBase(String aName) {
        super(aName);
    }

    // endregion

    // region Logic

    protected List<MTEHatchDynamoMulti> mExoticDynamoHatches = new ArrayList<>();

    @Override
    public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchDynamo) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDynamoHatches.add((MTEHatchDynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchDynamoMulti) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mExoticDynamoHatches.add((MTEHatchDynamoMulti) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addDynamoToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public enum HatchElement implements IHatchElement<TST_GeneratorBase> {

        ExoticDynamo(TST_GeneratorBase::addDynamoToMachineList, MTEHatchDynamoMulti.class) {

            @Override
            public long count(TST_GeneratorBase tstGeneratorBase) {
                return tstGeneratorBase.mExoticDynamoHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<TST_GeneratorBase> adder;

        @SafeVarargs
        HatchElement(IGTHatchAdder<TST_GeneratorBase> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.adder = adder;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super TST_GeneratorBase> adder() {
            return adder;
        }

    }

    @Override
    public boolean addEnergyOutputMultipleDynamos(long totalEU, boolean aAllowMixedVoltageDynamos) {
        // check positive
        if (totalEU < 0) totalEU = -totalEU;
        // to store free capacity of dynamo hatch
        long freeCapacity;
        // check normal dynamo hatches
        for (MTEHatchDynamo tHatch : filterValidMTEs(mDynamoHatches)) {
            freeCapacity = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            if (freeCapacity > 0) {
                if (totalEU > freeCapacity) {
                    tHatch.setEUVar(tHatch.maxEUStore());
                    totalEU -= freeCapacity;
                } else {
                    tHatch.setEUVar(
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + totalEU);
                    return true;
                }
            }
        }
        // check multi dynamo hatches
        for (MTEHatchDynamoMulti tHatch : filterValidMTEs(mExoticDynamoHatches)) {
            freeCapacity = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            if (freeCapacity > 0) {
                if (totalEU > freeCapacity) {
                    tHatch.setEUVar(tHatch.maxEUStore());
                    totalEU -= freeCapacity;
                } else {
                    tHatch.setEUVar(
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + totalEU);
                    return true;
                }
            }
        }
        // final
        return false;
    }
    // endregion

}
