package com.Nxer.TwistSpaceTechnology.system.OreProcess.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class TST_OreProcessingFactory extends GTCM_MultiMachineBase<TST_OreProcessingFactory> {

    // region Class Constructor
    public TST_OreProcessingFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_OreProcessingFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_OreProcessingFactory(this.mName);
    }

    // endregion

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
    public IStructureDefinition<TST_OreProcessingFactory> getStructureDefinition() {
        return null;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("test")
            .addInfo("test")
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addMaintenanceHatch(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }
}
