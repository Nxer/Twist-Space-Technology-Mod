package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_TileEntity_MegaUniversalSpaceStation
    extends GT_TileEntity_MultiStructureMachine<GT_TileEntity_MegaUniversalSpaceStation> {

    public GT_TileEntity_MegaUniversalSpaceStation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        setShape();
    }

    @Override
    public StructureDefinition.Builder<GT_TileEntity_MegaUniversalSpaceStation> additionalStructureDefinition() {
        return null;
    }

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
    public void setShape() {
        shape = new String[][] {
            // TODO
        };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return null;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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

    @Override
    public IAlignment getAlignment() {
        return super.getAlignment();
    }
}
