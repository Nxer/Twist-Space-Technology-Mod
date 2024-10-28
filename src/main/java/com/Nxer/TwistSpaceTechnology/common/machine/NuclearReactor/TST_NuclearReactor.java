package com.Nxer.TwistSpaceTechnology.common.machine.NuclearReactor;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_NuclearReactor extends GTCM_MultiMachineBase<TST_NuclearReactor> {

    public virtualReactorCell[][][] cells = new virtualReactorCell[15][15][15];

    public int maxHeat;

    public int heat;

    public TST_NuclearReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_NuclearReactor(String aName) {
        super(aName);
    }

    public void updateCells(boolean updateAllData) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                for (int k = 0; k < 15; k++) {
                    if (cells[i][j][k].update(this, updateAllData)) {

                    }
                }
            }
        }
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<TST_NuclearReactor> getStructureDefinition() {
        return null;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
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
