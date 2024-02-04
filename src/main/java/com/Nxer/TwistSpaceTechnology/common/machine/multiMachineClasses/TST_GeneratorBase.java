package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses;

import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.util.IGT_HatchAdder;

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

    protected List<GT_MetaTileEntity_Hatch_DynamoMulti> mExoticDynamoHatches = new ArrayList<>();

    @Override
    public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mExoticDynamoHatches.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addDynamoToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public enum HatchElement implements IHatchElement<TST_GeneratorBase> {

        ExoticDynamo(TST_GeneratorBase::addDynamoToMachineList, GT_MetaTileEntity_Hatch_DynamoMulti.class) {

            @Override
            public long count(TST_GeneratorBase tstGeneratorBase) {
                return tstGeneratorBase.mExoticDynamoHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<TST_GeneratorBase> adder;

        @SafeVarargs
        HatchElement(IGT_HatchAdder<TST_GeneratorBase> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.adder = adder;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGT_HatchAdder<? super TST_GeneratorBase> adder() {
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
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : filterValidMTEs(mDynamoHatches)) {
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
        for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : filterValidMTEs(mExoticDynamoHatches)) {
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
