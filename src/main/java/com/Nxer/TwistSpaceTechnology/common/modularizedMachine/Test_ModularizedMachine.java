package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.ExecutionCoreModule;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.ParallelController;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularBlockTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.MultiExecutionCoreMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class Test_ModularizedMachine extends MultiExecutionCoreMachineBase<Test_ModularizedMachine>
    implements IModularizedMachine.ISupportAllModularHatches {

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

    private int staticParallelParameter = 0;
    private int dynamicParallelParameter = 0;

    @Override
    public void resetModularStaticSettings() {
        staticParallelParameter = 0;
    }

    @Override
    public void resetModularDynamicParameters() {
        dynamicParallelParameter = 0;
    }

    @Override
    public int getStaticParallelParameterValue() {
        return staticParallelParameter;
    }

    @Override
    public void setStaticParallelParameter(int value) {
        staticParallelParameter = value;
    }

    @Override
    public int getDynamicParallelParameterValue() {
        return dynamicParallelParameter;
    }

    @Override
    public void setDynamicParallelParameter(int value) {
        dynamicParallelParameter = value;
    }

    @Override
    public float getStaticPowerConsumptionParameterValue() {
        return 0;
    }

    @Override
    public void setStaticPowerConsumptionParameterValue(float value) {

    }

    @Override
    public float getStaticSpeedParameterValue() {
        return 0;
    }

    @Override
    public void setStaticSpeedParameterValue(float value) {

    }

    @Override
    public float getDynamicSpeedParameterValue() {
        return 0;
    }

    @Override
    public void setDynamicSpeedParameterValue(float value) {

    }

    @Override
    public void setOverclockParameter(int timeReduction, int powerIncrease) {

    }

    // TODO

    @Override
    protected boolean isEnablePerfectOverclock() {
        return getOverclockType().isPerfectOverclock();
    }

    @Override
    protected OverclockType getOverclockType() {
        return OverclockType.PerfectOverclock;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        if (dynamicParallelParameter == Integer.MAX_VALUE || staticParallelParameter == Integer.MAX_VALUE
            || dynamicParallelParameter >= Integer.MAX_VALUE - 1 - staticParallelParameter) {
            return Integer.MAX_VALUE;
        }
        return dynamicParallelParameter + staticParallelParameter + 1;
    }

    protected static final Collection<ModularHatchTypes> SUPPORTED_MODULAR_HATCH_TYPES = Arrays
        .asList(ModularHatchTypes.PARALLEL_CONTROLLER, ModularHatchTypes.EXECUTION_CORE);

    @Override
    public Collection<ModularHatchTypes> getSupportedModularHatchTypes() {
        return SUPPORTED_MODULAR_HATCH_TYPES;// TODO NULL
    }

    @Override
    public Collection<ModularBlockTypes> getSupportedModularBlockTypes() {
        return Collections.emptyList();// TODO NULL
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
                        .atLeast(
                            InputHatch,
                            OutputHatch,
                            InputBus,
                            OutputBus,
                            Energy.or(ExoticEnergy),
                            ParallelController,
                            ExecutionCoreModule)
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
