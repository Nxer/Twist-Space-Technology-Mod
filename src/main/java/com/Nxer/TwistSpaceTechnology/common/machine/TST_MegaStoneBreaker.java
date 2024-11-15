package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MegaStoneBreakerRecipes;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.Nxer.TwistSpaceTechnology.util.Utils.calculatePowerTier;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.ParallelHelper.addItemsLong;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class TST_MegaStoneBreaker extends GTCM_MultiMachineBase<TST_MegaStoneBreaker> {

    // region Class Constructor
    public TST_MegaStoneBreaker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MegaStoneBreaker(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaStoneBreaker(this.mName);
    }

    // endregion

    // region Processing Logic
    private MTEHatchInput mLavaHatch;
    private MTEHatchInput mWaterHatch;
    boolean isOutputMultiply = false;

    @Override
    protected float getEuModifier() {
        return 1;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        int EuTier = (int) calculatePowerTier(getMaxInputEu());
        return EuTier < 29 ? (int) (Math.pow(2, EuTier) * 4) : Integer.MAX_VALUE;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return MegaStoneBreakerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                isOutputMultiply = drain(mWaterHatch, new FluidStack(Materials.Water.mFluid, 1000), false)
                    && drain(mLavaHatch, new FluidStack(Materials.Lava.mFluid, 1000), false);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                return super.createParallelHelper(recipe).setCustomItemOutputCalculation(parallel -> {
                    ArrayList<ItemStack> outputItemList = new ArrayList<>();
                    int OutputBonus = isOutputMultiply ? 1024 : 4;

                    for (ItemStack recipeItemStack : recipe.mOutputs) {
                        if (recipeItemStack != null) addItemsLong(
                            outputItemList,
                            recipeItemStack,
                            (long) parallel * recipeItemStack.stackSize * OutputBonus);
                    }
                    return outputItemList.toArray(new ItemStack[0]);
                });
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);

    }

    private byte runningTick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (runningTick % 20 == 0) {
            if (isOutputMultiply) {
                if (!drain(mWaterHatch, new FluidStack(Materials.Water.mFluid, 1000), true)
                    || !drain(mLavaHatch, new FluidStack(Materials.Lava.mFluid, 1000), true)) {
                    isOutputMultiply = false;
                    return false;
                }
            }
            runningTick = 1;
        } else {
            runningTick++;
        }
        return super.onRunningTick(aStack);
    }

    public boolean addWaterHatch(IGregTechTileEntity aTileEntity, short aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mWaterHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addLavaHatch(IGregTechTileEntity aTileEntity, short aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mLavaHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    // region Structure

    private final int horizontalOffSet = 10;
    private final int verticalOffSet = 7;
    private final int depthOffSet = 2;
    private static final String STRUCTURE_PIECE_MAIN = "mainMegaStoneBreaker";
    private static IStructureDefinition<TST_MegaStoneBreaker> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
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
    public IStructureDefinition<TST_MegaStoneBreaker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaStoneBreaker>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings11, 7))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 15))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 1))
                .addElement('E', ofFrame(Materials.CosmicNeutronium))
                .addElement('F', ofBlock(ModBlocks.blockCasings2Misc, 0))
                .addElement('G', ofBlock(ModBlocks.blockCasings2Misc, 11))
                .addElement('H', ofBlock(ModBlocks.blockCasingsMisc, 14))
                .addElement('I', ofBlock(Loaders.pressureResistantWalls, 0))
                .addElement(
                    'J',
                    HatchElementBuilder.<TST_MegaStoneBreaker>builder()
                        .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                        .adder(TST_MegaStoneBreaker::addToMachineList)
                        .dot(1)
                        .casingIndex(TAE.getIndexFromPage(1, 0))
                        .buildAndChain(ModBlocks.blockCasings2Misc, 0))
                .addElement(
                    'L',
                    buildHatchAdder(TST_MegaStoneBreaker.class).hatchClass(MTEHatchInput.class)
                        .adder(TST_MegaStoneBreaker::addLavaHatch)
                        .casingIndex(TAE.getIndexFromPage(1, 0))
                        .dot(2)
                        .buildAndChain(ModBlocks.blockCasings2Misc, 0))
                .addElement(
                    'W',
                    buildHatchAdder(TST_MegaStoneBreaker.class).hatchClass(MTEHatchInput.class)
                        .adder(TST_MegaStoneBreaker::addWaterHatch)
                        .casingIndex(TAE.getIndexFromPage(1, 0))
                        .dot(3)
                        .buildAndChain(ModBlocks.blockCasings2Misc, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:off

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasings11, 7, ...);
     * B -> ofBlock...(gt.blockcasings2, 15, ...);
     * C -> ofBlock...(gt.blockcasings8, 7, ...);
     * D -> ofBlock...(gt.blockcasings9, 1, ...);
     * E -> ofBlock...(gt.blockframes, 302, ...);
     * F -> ofBlock...(gtplusplus.blockcasings.2, 0, ...);
     * G -> ofBlock...(gtplusplus.blockcasings.2, 11, ...);
     * H -> ofBlock...(miscutils.blockcasings, 14, ...);
     * I -> ofBlock...(pressureResistantWalls, 0, ...);
     */

    private final String[][] shapeMain = new String[][]{
        {"                     ","  GGGGG       GGGGG  "," GGDCDGG     GGDCDGG "," GDDCDDGGGGGGGDDCDDG "," GCCCCCCCCCCCCCCCCCG "," GDDCDDGGGGGGGDDCDDG "," GGDCDGG     GGDCDGG ","  GGGGG       GGGGG  ","                     "},
        {"  FFFFF       FFFFF  "," FGEEEGF     FGEEEGF ","FGBBBBBGFFFFFGBBBBBGF","FEBIIIBEGEEEGEBIIIBEF","FEBIBIBEGEEEGEBIBIBEF","FEBIIIBEGEEEGEBIIIBEF","FGBBBBBGFFFFFGBBBBBGF"," FGEEEGF     FGEEEGF ","  FFFFF       FFFFF  "},
        {"  GGGGG       GGGGG  "," GEEEEEG     GEEEEEG ","GEBBBBBEGGGGGEBBBBBEG","GEBIIIBEGIIIGEBIIIBEG","GEBIBIBEGGGGGEBIBIBEG","GEBIIIBEGIIIGEBIIIBEG","GEBBBBBEGGGGGEBBBBBEG"," GEEEEEG     GEEEEEG ","  GGGGG       GGGGG  "},
        {"  GHHHG       GHHHG  "," GIIIIIG     GIIIIIG ","GIBBBBBIGFGFGIBBBBBIG","HIBIIIBIIIIIIIBIIIBIH","HIBIBIBIAAAAAIBIBIBIH","HIBIIIBIIIIIIIBIIIBIH","GIBBBBBIGGGGGIBBBBBIG"," GIIIIIG     GIIIIIG ","  GHHHG       GHHHG  "},
        {"  GHHHG       GHHHG  "," GIIIIIG     GIIIIIG ","GIBBBBBIGFFFGIBBBBBIG","HIBIIIBIIIIIIIBIIIBIH","HIBIBIBIAAAAAIBIBIBIH","HIBIIIBIIIIIIIBIIIBIH","GIBBBBBIGGGGGIBBBBBIG"," GIIIIIG     GIIIIIG ","  GHHHG       GHHHG  "},
        {"  GHHHG       GHHHG  "," GIIIIIG     GIIIIIG ","GIBBBBBIGFGFGIBBBBBIG","HIBIIIBIIIIIIIBIIIBIH","HIBIBIBIAAAAAIBIBIBIH","HIBIIIBIIIIIIIBIIIBIH","GIBBBBBIGGGGGIBBBBBIG"," GIIIIIG     GIIIIIG ","  GHHHG       GHHHG  "},
        {"  GGGGG       GGGGG  "," GEEGEEG     GEEGEEG ","GEEEGEEEGGGGGEEEGEEEG","GEEIIIEEGIIIGEEIIIEEG","GGGIBIGGGGGGGGGIBIGGG","GEEIIIEEGIIIGEEIIIEEG","GEEEGEEEGGGGGEEEGEEEG"," GEEGEEG     GEEGEEG ","  GGGGG       GGGGG  "},
        {"  FFLFF       FFWFF  "," FGGGGGF     FGGGGGF ","FGGGGGGGJJ~JJGGGGGGGF","FGGGGGGGGGGGGGGGGGGGF","FGGGGGGGGGGGGGGGGGGGF","FGGGGGGGGGGGGGGGGGGGF","FGGGGGGGFFFFFGGGGGGGF"," FGGGGGF     FGGGGGF ","  FFFFF       FFFFF  "}
    };

    // endregion

    // region General

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_MegaStoneBreaker_MachineType
        // # Stone Breaker
        // #zh_CN 碎石机
        tt.addMachineType(TextEnums.tr("Tooltip_MegaStoneBreaker_MachineType"))
            // #tr Tooltip_MegaStoneBreaker_Controller
            // # Controller block for the Silicon Rock Synthesizer
            // #zh_CN 硅岩制造机的控制方块
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker_Controller"))
            // #tr Tooltip_MegaStoneBreaker.1
            // # {\WHITE}Hey, I heard you come from a sky island?
            // #zh_CN {\WHITE}嘿,听说你来自一片空岛?
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker.1"))
            // #tr Tooltip_MegaStoneBreaker.2
            // # Basic parallel is 4, and the multiplier is equivalent to imperfect overclock
            // #zh_CN 基础并行为4, 拥有等同于有损超频的并行加成
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker.2"))
            // #tr Tooltip_MegaStoneBreaker.3
            // # Basic increase in output by 4x, When water and lava are input from the side input hatch, Increase to 1024x of output
            // #zh_CN 基础增产4倍, 当侧面输入仓输入水和岩浆时增产1024倍
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker.3"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textFrontBottom)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 1)
            // #tr Tooltip_MegaStoneBreaker_Hatch_0
            // # {\WHITE}Lava Hatch:{GREEN\} Left{\GRAY} side of Structure
            // #zh_CN {\WHITE}岩浆输入仓:{\GRAY}结构的{\GREEN}左{\GRAY}侧
            .addStructureInfo(TextEnums.tr("Tooltip_MegaStoneBreaker_Hatch_0"))
            // #tr Tooltip_MegaStoneBreaker_Hatch_1
            // # {\WHITE}Water Hatch:{GREEN\} Right{\GRAY} side of Structure
            // #zh_CN {\WHITE}水输入仓:{\GRAY}结构的{\GREEN}右{\GRAY}侧
            .addStructureInfo(TextEnums.tr("Tooltip_MegaStoneBreaker_Hatch_1"))
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addStructureInfo(Text_SeparatingLine)
            .toolTipFinisher(ModName);
        return tt;
    }

    // spotless:on

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(16)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(16)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build() };
        }

        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(16)) };
    }
}
