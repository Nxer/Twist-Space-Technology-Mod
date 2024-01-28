package com.Nxer.TwistSpaceTechnology.common.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class TST_VacuumFilterExtractor extends GTCM_MultiMachineBase<TST_VacuumFilterExtractor> {

    // region Class Constructor
    public TST_VacuumFilterExtractor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_VacuumFilterExtractor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_VacuumFilterExtractor(this.mName);
    }
    // endregion

    // region Processing Logic
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }
    // endregion

    // region Structure
    // spotless:off
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<TST_VacuumFilterExtractor> getStructureDefinition() {
        return null;
    }
    // spotless:on
    // endregion

    // region General
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Tooltip_MachineType")
            .addInfo("Tooltip_Controller")
            .addInfo("Tooltip_01")
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addStructureInfo(TextLocalization.Tooltip_ThermalEnergyDevourer_2_01)
            .addStructureInfo(TextLocalization.Tooltip_DoNotNeedMaintenance)
            // .beginStructureBlock(15, 37, 15, false)
            // .addController(TextLocalization.textFrontBottom)
            // .addInputHatch(TextLocalization.textUseBlueprint, 1)
            // .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            // .addInputBus(TextLocalization.textUseBlueprint, 1)
            // .addOutputBus(TextLocalization.textUseBlueprint, 1)
            // .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }
}
