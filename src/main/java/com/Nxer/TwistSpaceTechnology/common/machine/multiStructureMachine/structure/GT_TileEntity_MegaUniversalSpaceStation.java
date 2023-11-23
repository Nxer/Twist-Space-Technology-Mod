package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure;//spotless:off

import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.GT_TileEntity_MultiStructureMachine;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

// spotless:off
public class GT_TileEntity_MegaUniversalSpaceStation
    extends GT_TileEntity_MultiStructureMachine<GT_TileEntity_MegaUniversalSpaceStation> {
    public GT_TileEntity_MegaUniversalSpaceStation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaUniversalSpaceStation(String mName) {
        super(mName);
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaUniversalSpaceStation> getStructureDefinition() {
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaUniversalSpaceStation(this.mName);
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
//        Structure:
//
//        Blocks:
//        A -> ofBlock...(BW_GlasBlocks2, 0, ...);
//        B -> ofBlock...(EMT_GTBLOCK_CASEING, 11, ...);
//        C -> ofBlock...(FRF_Casing, 0, ...);
//        D -> ofBlock...(gt.blockcasings9, 1, ...);
//        E -> ofBlock...(gt.blockcasingsBA0, 10, ...);
//        F -> ofBlock...(gt.blockcasingsTT, 2, ...);
//        G -> ofBlock...(gt.blockcasingsTT, 3, ...);
//        H -> ofBlock...(gt.spacetime_compression_field_generator, 7, ...);
//        I -> ofBlock...(gtplusplus.blockcasings.5, 10, ...);
//        J -> ofBlock...(gtplusplus.blockcasings.5, 14, ...);
//        K -> ofBlock...(gtplusplus.blockcasings.6, 0, ...);
//        L -> ofBlock...(gtplusplus.blockspecialcasings.1, 15, ...);
//        M -> ofBlock...(miscutils.blockcasings, 9, ...);
//        N -> ofBlock...(tile.chisel.laboratoryblock, 6, ...);
//        O -> ofBlock...(tile.snow, 0, ...);
//
//        Tiles:
//
//        Special Tiles:
//        P -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...); // You will probably want to change it to something else
//        Q -> ofSpecialTileAdder(gcewing.sg.SGRingTE, ...); // You will probably want to change it to something else
//
//        Offsets:
//        214 115 -1
