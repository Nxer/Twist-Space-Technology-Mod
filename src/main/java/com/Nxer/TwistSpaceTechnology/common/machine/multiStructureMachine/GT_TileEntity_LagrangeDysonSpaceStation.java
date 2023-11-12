package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import com.Nxer.TwistSpaceTechnology.common.machine.GT_TileEntity_IntensifyChemicalDistorter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_TileEntity_LagrangeDysonSpaceStation
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_IntensifyChemicalDistorter>
    implements IConstructable, ISurvivalConstructable {

    // nodes types: 0:Maintain nodes, 1:compute nodes, 2:wireless energy output nodes, 3:advanced dyson nodes, 4:dyson
    // nodes

    // region Member Variables
    private final Integer stationIndex;

    // the max number of Big dyson Nodes, include all kinds of big nodes.
    public static final Integer bigNodeCapacity = 1024;

    // the max number of small dyson Nodes in each big nodes, include all kinds of small nodes.
    public static final Integer smallNodeCapacity = 1024;

    public static final Double[] computePointPerNodes = { 0.0005, 0.0, 0.0003, 0.0005, 0.0001 };
    public static final Double[] maintainPointPerNodes = { -1.0, 0.1, 0.05, 0.02, 0.05 };

    // the number of big nodes
    public Integer[] bigNodeNumber = { 0, 0, 0, 0, 0 };

    public Integer[] smallNodeNumber = { 0, 0, 0, 0, 0 };

    // endregion
    // region Construction
    protected GT_TileEntity_LagrangeDysonSpaceStation(int aID, String aName, String aNameRegional,
        Integer stationIndex) {
        super(aID, aName, aNameRegional);
        this.stationIndex = stationIndex;
    }
    // endregion

    // regin logic
    public double computePointInNeed() {
        double result = 0.0;
        for (int i = 0; i < 5; i++) {
            result += 1000.0 * bigNodeNumber[i] * computePointPerNodes[i]
                + smallNodeNumber[i] * computePointPerNodes[i];
        }
        return Math.max(1.0, result);
    }

    public double efficiency() {
        return Math.log10(1.0 + (1024.0 * bigNodeNumber[1] + smallNodeNumber[1]) / computePointInNeed());
    }

    public void loseNodes() {
        double bigNodeLose = 0.0;
        int bigNodeTotal = 0;
        double smallNodeLose = 0.0;
        int smallNodeTotal = 0;
        // calculate the maintain needs
        for (int i = 0; i < 5; i++) {
            bigNodeLose += bigNodeNumber[i] * maintainPointPerNodes[i];
            bigNodeTotal += bigNodeNumber[i];
        }
        bigNodeLose = Math.max(bigNodeLose, 0.001 * bigNodeTotal);
        // delete all the unfixable big nodes first
        for (int i = 4; i >= 0; i--) {
            if (bigNodeLose >= 1.0) {
                int lose = (int) Math.min(bigNodeNumber[i], Math.floor(bigNodeLose));
                bigNodeLose -= lose;
                bigNodeNumber[i] -= lose;
            }
        }
        // delete all the small nodes more than big nodes can struct
        for (int i = 4; i >= 0; i--) {
            if (bigNodeTotal * smallNodeCapacity < smallNodeCapacity) {
                int lose = Math.min(smallNodeNumber[i], smallNodeTotal - bigNodeTotal * smallNodeCapacity);
                smallNodeNumber[i] -= lose;
                smallNodeTotal -= lose;
            }
            smallNodeLose += smallNodeNumber[i] * maintainPointPerNodes[i];
            smallNodeTotal += smallNodeTotal;
        }
        // delete all the unfixable small nodes at last
        smallNodeLose = Math.max(smallNodeLose, 0.001 * smallNodeTotal);
        for (int i = 4; i >= 0; i--) {
            if (smallNodeLose >= 1.0) {
                int lose = (int) Math.min(smallNodeNumber[i], Math.floor(bigNodeLose));
                smallNodeLose -= lose;
                smallNodeNumber[i] -= lose;
            }
        }

    }

    // endregion

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<GT_TileEntity_IntensifyChemicalDistorter> getStructureDefinition() {
        return null;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return null;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }
}
