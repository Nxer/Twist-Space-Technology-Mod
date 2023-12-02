package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure;// spotless:off

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.spaceStationStructureBlockMAX;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.SpaceStationAntiGravityBlock;
import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.spaceStationStructureBlock;
import static com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry.bw_realglas2;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.GT_TileEntity_MultiStructureMachine;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.StructureLoader;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.core.block.ModBlocks;

// spotless:off
public class GT_TileEntity_MegaUniversalSpaceStation
    extends GT_TileEntity_MultiStructureMachine<GT_TileEntity_MegaUniversalSpaceStation> {

    public static IStructureDefinition<GT_TileEntity_MegaUniversalSpaceStation> structureDefinition;

    static {
        StructureLoader.setOffSet("namemegauniversalspacestation", 215, 45, 223);
    }

    public GT_TileEntity_MegaUniversalSpaceStation(int aID, String mName, String aNameRegional) {
        super(aID, mName, aNameRegional);

    }

    public GT_TileEntity_MegaUniversalSpaceStation(String mName) {
        super(mName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaUniversalSpaceStation> getStructureDefinition() {
        if (structureDefinition == null) {

            var builder = StructureDefinition.<GT_TileEntity_MegaUniversalSpaceStation>builder();
            for (int i = 0; i < StructureLoader.readStructure(mName).shape.size(); i++) {
                builder.addShape(mName + i, StructureLoader.getShape(mName, mName + i));
            }
            // Structure:
            //
            // Blocks:
            // A -> ofBlock...(BW_GlasBlocks2, 0, ...);
            // B -> ofBlock...(EMT_GTBLOCK_CASEING, 11, ...);
            // C -> ofBlock...(FRF_Casing, 0, ...);
            // D -> ofBlock...(gt.blockcasings9, 1, ...);
            // E -> ofBlock...(gt.blockcasingsBA0, 10, ...);
            // F -> ofBlock...(gt.blockcasingsBA0, 12, ...);
            // G -> ofBlock...(gt.blockcasingsSE, 1, ...);
            // H -> ofBlock...(gt.blockcasingsSE, 2, ...);
            // I -> ofBlock...(gt.blockcasingsTT, 2, ...);
            // J -> ofBlock...(gt.blockcasingsTT, 3, ...);
            // K -> ofBlock...(gt.blockmetal9, 6, ...);
            // L -> ofBlock...(gt.blockmetal9, 7, ...);
            // M -> ofBlock...(gt.spacetime_compression_field_generator, 7, ...);
            // N -> ofBlock...(gtplusplus.blockcasings.5, 10, ...);
            // O -> ofBlock...(gtplusplus.blockcasings.5, 14, ...);
            // P -> ofBlock...(gtplusplus.blockcasings.6, 0, ...);
            // Q -> ofBlock...(gtplusplus.blockspecialcasings.1, 15, ...);
            // R -> ofBlock...(miscutils.blockcasings, 9, ...);
            // S -> ofBlock...(tile.chisel.laboratoryblock, 6, ...);
            // T -> ofBlock...(tile.extrautils:angelBlock, 0, ...);
            // U -> ofBlock...(tile.snow, 0, ...);
            //
            // Tiles:
            //
            // Special Tiles:
            // V -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...); // You will probably want
            // to change it to something else
            // W -> ofSpecialTileAdder(gcewing.sg.SGRingTE, ...); // You will probably want to change it to something
            // else
            // X -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaTileEntity, ...); // You will probably want
            // to change it to something else
            //
            // Offsets:
            // 215 45 223
            structureDefinition = builder
                .addElement(
                    'A',
                    GT_HatchElementBuilder.<GT_TileEntity_MegaUniversalSpaceStation>builder()
                        .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), Maintenance)
                        .adder(GT_TileEntity_MegaUniversalSpaceStation::addToMachineList)
                        .casingIndex(176)
                        .dot(1)
                        .buildAndChain(bw_realglas2, 0))
                // .addElement('B', ofBlock(SpaceStationAntiGravityBlock, 13))
                .addElement('C', ofBlock(spaceStationStructureBlock, 0))
                .addElement('D', ofBlock(GregTech_API.sBlockCasings9, 1))
                .addElement('E', ofBlock(TT_Container_Casings.sBlockCasingsBA0, 10))
                .addElement('F', ofBlock(TT_Container_Casings.sBlockCasingsBA0, 12))
                .addElement('G', ofBlock(IGBlocks.SpaceElevatorCasing, 1))
                .addElement('H', ofBlock(IGBlocks.SpaceElevatorCasing, 2))
                .addElement('I', ofBlock(TT_Container_Casings.sBlockCasingsTT, 2))
                .addElement('J', ofBlock(TT_Container_Casings.sBlockCasingsTT, 3))
                .addElement('K', ofBlock(GregTech_API.sBlockMetal9, 6))
                .addElement('L', ofBlock(GregTech_API.sBlockMetal9, 7))
                .addElement('M', ofBlock(spaceStationStructureBlockMAX.getBlock(), 7))
                .addElement('N', ofBlock(ModBlocks.blockCasings5Misc, 10))
                .addElement('O', ofBlock(ModBlocks.blockCasings5Misc, 14))
                .addElement('P', ofBlock(ModBlocks.blockCasings6Misc, 0))
                .addElement('Q', ofBlock(ModBlocks.blockSpecialMultiCasings, 15))
                .addElement('B', ofBlock(Block.getBlockById(1), 0))
                .addElement('R', ofBlock(SpaceStationAntiGravityBlock, 13))
                // .addElement('C', ofBlock(Block.getBlockById(1),0))
                // .addElement('D', ofBlock(Block.getBlockById(1),0))
                // .addElement('E', ofBlock(Block.getBlockById(1),0))
                // .addElement('F', ofBlock(Block.getBlockById(1),0))
                // .addElement('G', ofBlock(Block.getBlockById(1),0))
                // .addElement('H', ofBlock(Block.getBlockById(1),0))
                // .addElement('I', ofBlock(Block.getBlockById(1),0))
                // .addElement('J', ofBlock(Block.getBlockById(1),0))
                // .addElement('K', ofBlock(Block.getBlockById(1),0))
                // .addElement('L', ofBlock(Block.getBlockById(1),0))
                // .addElement('M', ofBlock(Block.getBlockById(1),0))
                // .addElement('N', ofBlock(Block.getBlockById(1),0))
                // .addElement('O', ofBlock(Block.getBlockById(1),0))
                // .addElement('P', ofBlock(Block.getBlockById(1),0))
                // .addElement('Q', ofBlock(Block.getBlockById(1),0))
                .addElement('S', ofBlock(Block.getBlockById(0), 0))//
                .addElement('T', ofBlock(Block.getBlockById(0), 0))//
                .addElement('U', ofBlock(Block.getBlockById(0), 0))//
                .addElement('V', ofBlock(Block.getBlockById(0), 0))//
                .addElement('W', ofBlock(Block.getBlockById(0), 0))//
                .addElement('X', ofBlock(Block.getBlockById(0), 0))//
                .build();
        }
        return structureDefinition;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaUniversalSpaceStation(this.mName);
    }

    @Override
    public IAlignment getAlignment() {
        return super.getAlignment();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_MegaUniversalSpaceStation_MachineType)
            .addInfo(TextLocalization.Tooltip_MegaUniversalSpaceStation_00)
            .addInfo(TextLocalization.Tooltip_MegaUniversalSpaceStation_01)
            .addInfo(TextLocalization.Tooltip_MegaUniversalSpaceStation_02)
            .addInfo(TextLocalization.Tooltip_MegaUniversalSpaceStation_03)
            .addInfo(TextLocalization.Tooltip_MegaUniversalSpaceStation_04)
            .addInfo(TextLocalization.Tooltip_MegaUniversalSpaceStation_05)
            .addInfo(TextLocalization.Tooltip_MegaUniversalSpaceStation_06)
            .addInfo(TextLocalization.Tooltip_GlassTierLimitEnergyHatchTier)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(448, 256, 431, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 2)
            .addOutputHatch(TextLocalization.textUseBlueprint, 2)
            .addInputBus(TextLocalization.textUseBlueprint, 2)
            .addOutputBus(TextLocalization.textUseBlueprint, 2)
            .addMaintenanceHatch(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
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
