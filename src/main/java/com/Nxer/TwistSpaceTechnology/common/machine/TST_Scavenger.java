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
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.blocks.BlockCasings8;

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
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.sifterRecipes;
    }

    // endregion

    // region Structure

    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 8;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainScavenger";
    private static IStructureDefinition<TST_Scavenger> STRUCTURE_DEFINITION = null;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        this.speedBonus = (float) Math
            .pow(SpeedBonus_MultiplyPerTier_Scavenger, GTUtility.getTier(this.getMaxInputEu()));
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public IStructureDefinition<TST_Scavenger> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_Scavenger>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'B',
                    HatchElementBuilder.<TST_Scavenger>builder()
                        .atLeast(OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_Scavenger::addToMachineList)
                        .dot(2)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'C',
                    HatchElementBuilder.<TST_Scavenger>builder()
                        .atLeast(InputBus, InputHatch)
                        .adder(TST_Scavenger::addToMachineList)
                        .dot(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(BorosilicateGlass.ofBoroGlassAnyTier(), ofBlock(GregTechAPI.sBlockCasings4, 14)))
                .addElement(
                    'D',
                    HatchElementBuilder.<TST_Scavenger>builder()
                        .atLeast(InputBus, InputHatch)
                        .adder(TST_Scavenger::addToMachineList)
                        .dot(1)
                        .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(14))
                        .buildAndChain(GregTechAPI.sBlockCasings4, 14))
                .addElement('E', ofFrame(Materials.Osmiridium))
                .build();
        }
        return STRUCTURE_DEFINITION;
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
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
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
