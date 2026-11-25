package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;

/** Structure-specific state and rules; production state belongs to the shared controller. */
public abstract class BottledChaosStructure {

    protected final MM_BottledChaos machine;

    protected BottledChaosStructure(MM_BottledChaos machine) {
        this.machine = machine;
    }

    public void reset() {}

    public abstract int getProcessingGroupSlot(int relativeLeft, int relativeBack);

    public abstract int getProcessingGroupLimit();

    public abstract int getCredentialHatchLimit(int parallelTier);

    public abstract boolean checkStructurePieces(List<StructureError> errors);

    public abstract StructureError getInvalidCapacityRegionError();

    public abstract int getCapacityRegionCount();

    public abstract int getCapacityRegionBlockCount();

    public abstract int getCapacityLevelCount();

    public abstract int getCapacityRegion(int x, int y, int z);

    public abstract boolean isWirelessMode();

    public abstract long getMaximumRecipeVoltage();

    public abstract void construct(ItemStack stack, boolean hintsOnly);

    public abstract int survivalConstruct(ItemStack stack, int budget, ISurvivalBuildEnvironment environment);

    public abstract IStructureDefinition<MM_BottledChaos> getStructureDefinition();

    public abstract MultiblockTooltipBuilder getTooltip();

    protected boolean checkPiece(String piece, int x, int y, int z, List<StructureError> errors) {
        return machine.checkStructurePiece(piece, x, y, z, errors);
    }

    protected void buildPiece(String piece, ItemStack stack, boolean hintsOnly, int x, int y, int z) {
        machine.buildStructurePiece(piece, stack, hintsOnly, x, y, z);
    }

    protected int survivalBuildPiece(String piece, ItemStack stack, int x, int y, int z, int budget,
        ISurvivalBuildEnvironment environment, boolean checkPlaced, boolean checkMachine) {
        return machine
            .survivalBuildStructurePiece(piece, stack, x, y, z, budget, environment, checkPlaced, checkMachine);
    }
}
