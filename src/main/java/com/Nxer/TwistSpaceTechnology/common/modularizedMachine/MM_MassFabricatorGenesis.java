package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineSupportAllModuleBase;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class MM_MassFabricatorGenesis extends ModularizedMachineSupportAllModuleBase<MM_MassFabricatorGenesis> {

    // region Class Constructor
    public MM_MassFabricatorGenesis(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MM_MassFabricatorGenesis(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MM_MassFabricatorGenesis(mName);
    }

    // endregion

    // region Logics

    @Override
    protected boolean canMultiplyModularHatchType() {
        return false;
    }

    // endregion

    // region Structure
    @Override
    public boolean checkMachineMM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<MM_MassFabricatorGenesis> getStructureDefinition() {
        return null;
    }

    // endregion

    // region General

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return null;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }
}
