package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularBlockTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collection;
import java.util.List;

public class Test_ModularizedMachine extends ModularizedMachineBase<Test_ModularizedMachine> {

    // region Class Constructor
    public Test_ModularizedMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public Test_ModularizedMachine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new Test_ModularizedMachine(this.mName);
    }

    // endregion

    // region Processing Logic

    // endregion

    // region Structure

    // endregion

    // region General
    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    public Collection<ModularHatchTypes> getSupportedModularHatchTypes() {
        return null;//TODO NULL
    }

    @Override
    public Collection<ModularBlockTypes> getSupportedModularBlockTypes() {
        return null;//TODO NULL
    }

    @Override
    public void resetModularStaticSettings() {

    }

    @Override
    public void applyModularStaticSettings() {

    }

    @Override
    public void resetModularDynamicParameters() {

    }

    @Override
    public void applyModularDynamicParameters() {

    }

    @Override
    public Collection<IModularHatch> getAllModularHatches() {
        return null;//TODO NULL
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<Test_ModularizedMachine> getStructureDefinition() {
        return null;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }


    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }
}
