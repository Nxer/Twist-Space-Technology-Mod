package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EnablePerfectOverclock_Scavenger;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EuModifier_Scavenger;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_Scavenger;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings4;
import gregtech.common.blocks.GT_Block_Casings8;

public class TST_Scavenger extends GTCM_MultiMachineBase<TST_Scavenger> {

    // region Class Constructor
    public TST_Scavenger(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_Scavenger(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_Scavenger(this.mName);
    }

    // endregion

    // region Processing Logic

    private float speedBonus = 1;

    @Override
    protected float getEuModifier() {
        return EuModifier_Scavenger;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return EnablePerfectOverclock_Scavenger;
    }

    @Override
    protected float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sSifterRecipes;
    }

    // endregion

    // region Structure

    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 8;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainScavenger";

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        this.speedBonus = (float) Math.pow(SpeedBonus_MultiplyPerTier_Scavenger, GT_Utility.getTier(this.getMaxInputEu()));
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }

    @Override
    public IStructureDefinition<TST_Scavenger> getStructureDefinition() {
        return StructureDefinition.<TST_Scavenger>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
            .addElement('A', ofBlock(GregTech_API.sBlockCasings8, 7))
            .addElement(
                'B',
                GT_HatchElementBuilder.<TST_Scavenger>builder()
                    .atLeast(OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                    .adder(TST_Scavenger::addToMachineList)
                    .dot(2)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(7))
                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
            .addElement(
                'C',
                GT_HatchElementBuilder.<TST_Scavenger>builder()
                    .atLeast(InputBus, InputHatch)
                    .adder(TST_Scavenger::addToMachineList)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(7))
                    .buildAndChain(BorosilicateGlass.ofBoroGlassAnyTier(), ofBlock(GregTech_API.sBlockCasings4, 14)))
            .addElement(
                'D',
                GT_HatchElementBuilder.<TST_Scavenger>builder()
                    .atLeast(InputBus, InputHatch)
                    .adder(TST_Scavenger::addToMachineList)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings4) GregTech_API.sBlockCasings4).getTextureIndex(14))
                    .buildAndChain(GregTech_API.sBlockCasings4, 14))
            .addElement('E', ofFrame(Materials.Osmiridium))
            .build();
    }

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasings8, 7, ...);
     * B -> ofBlock...(gt.blockcasings8, 7, ...); // output IO, other hatches
     * C -> ofBlock...(tile.stonebrick, 0, ...); // in
     * D -> ofBlock...(gt.blockcasings4, 14, ...); // in , glass
     * E -> ofFrame...(Materials.Osmiridium);
     */
    private final String[][] shapeMain = new String[][] {
        { "               ", "     DDDDD     ", "   DDDCCCDDD   ", "  DDCCCCCCCDD  ", "  DCCCCCCCCCD  ",
            " DDCCCCCCCCCDD ", " DCCCCCCCCCCCD ", " DCCCCCCCCCCCD ", " DCCCCCCCCCCCD ", " DDCCCCCCCCCDD ",
            "  DCCCCCCCCCD  ", "  DDCCCCCCCDD  ", "   DDDCCCDDD   ", "     DDDDD     ", "               " },
        { "               ", "     AAAAA     ", "   AAEEEEEAA   ", "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ",
            " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ",
            "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ", "   AAEEEEEAA   ", "     AAAAA     ", "               " },
        { "               ", "     AAAAA     ", "   AAEEEEEAA   ", "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ",
            " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ",
            "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ", "   AAEEEEEAA   ", "     AAAAA     ", "               " },
        { "               ", "     AAAAA     ", "   AAEEEEEAA   ", "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ",
            " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ",
            "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ", "   AAEEEEEAA   ", "     AAAAA     ", "               " },
        { "               ", "     AAAAA     ", "   AAEEEEEAA   ", "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ",
            " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ",
            "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ", "   AAEEEEEAA   ", "     AAAAA     ", "               " },
        { "               ", "     AAAAA     ", "   AAEEEEEAA   ", "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ",
            " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ",
            "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ", "   AAEEEEEAA   ", "     AAAAA     ", "               " },
        { "               ", "     AAAAA     ", "   AAEEEEEAA   ", "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ",
            " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ",
            "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ", "   AAEEEEEAA   ", "     AAAAA     ", "               " },
        { "               ", "     AAAAA     ", "   AAEEEEEAA   ", "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ",
            " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ", " AEEEEEEEEEEEA ",
            "  AEEEEEEEEEA  ", "  AEEEEEEEEEA  ", "   AAEEEEEAA   ", "     AAAAA     ", "               " },
        { "     BB~BB     ", "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBBBBBBBBBBBB ", " BBBBBBBBBBBBB ",
            "BBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBB",
            " BBBBBBBBBBBBB ", " BBBBBBBBBBBBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   ", "     BBBBB     " } };

    // endregion

    // region General

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_Scavenger_MachineType)
            .addInfo(TextLocalization.Tooltip_Scavenger_Controller)
            .addInfo(TextLocalization.Tooltip_Scavenger_01)
            .addInfo(TextLocalization.Tooltip_Scavenger_02)
            .addInfo(TextLocalization.Tooltip_Scavenger_03)
            .addInfo(TextLocalization.Tooltip_Scavenger_04)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textFrontBottom)
            .addInputHatch(textUseBlueprint, 1)
            .addOutputHatch(textUseBlueprint, 2)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 2)
            .addEnergyHatch(textUseBlueprint, 2)
            .addStructureInfo(Text_SeparatingLine)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(ModName);
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
