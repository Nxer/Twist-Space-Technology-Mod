package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.AllModule;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.MultiExecutionCoreMachineSupportAllModuleBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class Test_ModularizedMachine extends MultiExecutionCoreMachineSupportAllModuleBase<Test_ModularizedMachine> {

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

    // TODO delete this method
    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.wiremillRecipes;
    }

    @Override
    public boolean checkMachineMM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final String[][] shape = new String[][] { { "AAA", "AAA", "AAA" }, { "A~A", "AAA", "AAA" },
        { "AAA", "AAA", "AAA" } };
    private static final int horizontalOffSet = 1;
    private static final int verticalOffSet = 1;
    private static final int depthOffSet = 0;
    private static IStructureDefinition<Test_ModularizedMachine> STRUCTURE_DEFINITION;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(
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
    public IStructureDefinition<Test_ModularizedMachine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<Test_ModularizedMachine>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    GT_HatchElementBuilder.<Test_ModularizedMachine>builder()
                        .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), AllModule)
                        .adder(Test_ModularizedMachine::addToMachineList)
                        .casingIndex(BasicBlocks.MetaBlockCasing01.getTextureIndex(0))
                        .dot(1)
                        .buildAndChain(BasicBlocks.MetaBlockCasing01, 0))
                .build();
        }

        return STRUCTURE_DEFINITION;
    }
    // endregion

    // region General

    private static GT_Multiblock_Tooltip_Builder tooltip;

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        if (tooltip == null) {
            tooltip = new GT_Multiblock_Tooltip_Builder();
            tooltip.addMachineType("test")
                .addInfo("testing")
                .addSeparator()
                .addInfo(TextLocalization.StructureTooComplex)
                .addInfo(TextLocalization.BLUE_PRINT_INFO)
                .beginStructureBlock(3, 3, 3, false)
                .addInputHatch(TextLocalization.textUseBlueprint, 1)
                .addOutputHatch(TextLocalization.textUseBlueprint, 1)
                .addInputBus(TextLocalization.textUseBlueprint, 2)
                .addOutputBus(TextLocalization.textUseBlueprint, 2)
                .addEnergyHatch(TextLocalization.textUseBlueprint, 3)
                .toolTipFinisher(TextLocalization.ModName);

        }
        return tooltip;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

}
