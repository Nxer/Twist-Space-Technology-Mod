package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.AllModule;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.MultiExecutionCoreMachineSupportAllModuleBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.blocks.GT_Block_Casings4;
import gregtech.common.blocks.GT_Block_Casings8;

public class MM_LargeNeutronOscillator
    extends MultiExecutionCoreMachineSupportAllModuleBase<MM_LargeNeutronOscillator> {

    // region Class Constructor
    public MM_LargeNeutronOscillator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MM_LargeNeutronOscillator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MM_LargeNeutronOscillator(this.mName);
    }

    // endregion

    // region Logic

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.NeutronActivatorRecipesWithEU;
    }

    @Override
    protected boolean canMultiplyModularHatchType() {
        return false;
    }

    @Override
    public boolean checkMachineMM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    // endregion

    // region Structure

    private static final int horizontalOffSet = 3;
    private static final int verticalOffSet = 36;
    private static final int depthOffSet = 1;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MM_LargeNeutronOscillator> STRUCTURE_DEFINITION = null;

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
    public IStructureDefinition<MM_LargeNeutronOscillator> getStructureDefinition() {
        if (null == STRUCTURE_DEFINITION) {
            STRUCTURE_DEFINITION = StructureDefinition.<MM_LargeNeutronOscillator>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        // spotless:off
                        new String[][] {
                            { "                       ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", "            G       G  ", "           GFFFFFFFFFG ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", " CCCCC      G       G  ", " CCCCC     GFFFFFFFFFG ", " CCCCC      FEFEFEFEF  ", " CCCCC      FFFFFFFFF  ", " CCCCC      FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", " CAAAC      G       G  ", " CDDDC     GFFFFFFFFFG ", " CDDDC      FEFEFEFEF  ", " CDDDC      FFFFFFFFF  ", " CCCCC      FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", " CA~AC      G       G  ", " CDDDC     GFFFFFFFFFG ", " CDDDC      FEFEFEFEF  ", " CDDDC      FFFFFFFFF  ", " CCCCC      FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", " CAAAC      G       G  ", " CDDDC     GFFFFFFFFFG ", " CDDDC      FEFEFEFEF  ", " CDDDC      FFFFFFFFF  ", " CCCCC      FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "            FFFFFFFFF  ", "            FEFEFEFEF  ", "           GFFFFFFFFFG ", "            G       G  ", "                       " },
                            { "                       ", " CCCCC     CCCCCCCCCCC ", " CCCCCCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCCCCCC ", " CCCCC     CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "           CCCCCCCCCCC ", "                       " },
                            { "BBBBBBB   BBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBBBBBB", "BBBBBBB   BBBBBBBBBBBBB", "          BBBBBBBBBBBBB", "          BBBBBBBBBBBBB", "          BBBBBBBBBBBBB", "          BBBBBBBBBBBBB", "          BBBBBBBBBBBBB", "          BBBBBBBBBBBBB" } }))
                // spotless:on
                .addElement(
                    'A',
                    GT_HatchElementBuilder.<MM_LargeNeutronOscillator>builder()
                        .atLeast(AllModule)
                        .adder(MM_LargeNeutronOscillator::addModularHatchToMachineList)
                        .dot(1)
                        .casingIndex(((GT_Block_Casings4) GregTech_API.sBlockCasings4).getTextureIndex(1))
                        .buildAndChain(GregTech_API.sBlockCasings4, 1))
                .addElement(
                    'B',
                    GT_HatchElementBuilder.<MM_LargeNeutronOscillator>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(MM_LargeNeutronOscillator::addNormalHatchToMachineList)
                        .dot(2)
                        .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTech_API.sBlockCasings8, 7))
                .addElement(
                    'C',
                    GT_HatchElementBuilder.<MM_LargeNeutronOscillator>builder()
                        .atLeast(InputHatch, OutputHatch, InputBus, OutputBus)
                        .adder(MM_LargeNeutronOscillator::addNormalHatchToMachineList)
                        .dot(3)
                        .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10))
                        .buildAndChain(GregTech_API.sBlockCasings8, 10))
                .addElement('D', ofBlock(sBlockCasingsTT, 0))
                .addElement('E', ofBlock(Loaders.speedingPipe, 0))
                .addElement('F', Glasses.chainAllGlasses())
                .addElement('G', ofFrame(Materials.NaquadahAlloy))
                .build();
            /*
             * A -> ofBlock...(gt.blockcasings4, 1, ...); module hatch
             * B -> ofBlock...(gt.blockcasings8, 7, ...); energy hatch
             * C -> ofBlock...(gt.blockcasings8, 10, ...); io hatch
             * D -> ofBlock...(gt.blockcasingsTT, 0, ...);
             * E -> ofBlock...(speedingPipe, 0, ...);
             * F -> ofBlock...(tile.glass, 0, ...);
             * G -> ofFrame...(Materials.NaquadahAlloy);
             */
        }
        return STRUCTURE_DEFINITION;
    }

    // endregion

    // region General
    private static GT_Multiblock_Tooltip_Builder tooltip;

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        // spotless:off
        if (tooltip == null) {
            tooltip = new GT_Multiblock_Tooltip_Builder();
            // #tr Tooltip_LargeNeutronOscillator_MachineType
            // # {\WHITE}Modularized Machine {\GRAY}- {\YELLOW}Neutron Activator
            // #zh_CN {\WHITE}模块化机械 {\GRAY}- {\YELLOW}中子活化器
            tooltip
                .addMachineType(
                    TextEnums.tr("Tooltip_LargeNeutronOscillator_MachineType"))
                // #tr Tooltip_LargeNeutronOscillator_01
                // # {\AQUA}{\UNDERLINE}The more physics you know the less engineering you need.
                // #zh_CN {\AQUA}{\UNDERLINE}掌握的物理学越多， 需要的工程学越少。
                .addInfo(TextEnums.tr("Tooltip_LargeNeutronOscillator_01"))

                // #tr Tooltip_LargeNeutronOscillator_02
                // # It consumes a lot of electricity to produce large quantities quickly.
                // #zh_CN 通过消耗大量电力来进行快速大批量生产.
                .addInfo(TextEnums.tr("Tooltip_LargeNeutronOscillator_02"))

                // #tr Tooltip_LargeNeutronOscillator_03
                // # Installing module hatches near the controller block can significantly improve machine performance.
                // #zh_CN 在主机附近安装模块仓室可以显著提升机器性能.
                .addInfo(TextEnums.tr("Tooltip_LargeNeutronOscillator_03"))
                .addInfo(TextEnums.ModularizedMachineSystem.getText())
                .addSeparator()
                .addInfo(TextLocalization.StructureTooComplex)
                .addInfo(TextLocalization.BLUE_PRINT_INFO)
                .addStructureInfo(TextEnums.ModularizedMachineSystem.getText())
                .addStructureInfo(TextEnums.ModularizedMachineSystemDescription01.getText())
                .addStructureInfo(TextEnums.ModularizedMachineSystemDescription02.getText())
                .addStructureInfo(TextEnums.OverclockControllerDescription.getText())
                .addStructureInfo(TextEnums.ParallelControllerDescription.getText())
                .addStructureInfo(TextEnums.PowerConsumptionControllerDescription.getText())
                .addStructureInfo(TextEnums.SpeedControllerDescription.getText())
                .addStructureInfo(TextEnums.ExecutionCoreDescription.getText())
                .addStructureInfo(TextEnums.NotMultiplyInstallSameTypeModule.getText())
                .addStructureInfo(TextLocalization.Text_SeparatingLine)
                .beginStructureBlock(23, 40, 13, false)
                .addStructureInfo("    " + TextEnums.ModularHatch + ": " + TextLocalization.textUseBlueprint)
                .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
                .addInputHatch(TextLocalization.textUseBlueprint, 3)
                .addOutputHatch(TextLocalization.textUseBlueprint, 3)
                .addInputBus(TextLocalization.textUseBlueprint, 3)
                .addOutputBus(TextLocalization.textUseBlueprint, 3)
                .addStructureHint(TextEnums.ModularHatch.getKey(), 1)
                .toolTipFinisher(TextLocalization.ModName);
            // spotless:on
        }
        return tooltip;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_ON)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FUSION1_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49) };
    }
}
