package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MegaStoneBreakerRecipeMap;
import static com.Nxer.TwistSpaceTechnology.util.TSTUtils.calculateVoltageTier;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.General.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.MachineTooltip.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textUseBlueprint;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
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
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;

@SkipGenerateDescription
public class TST_MegaStoneBreaker extends GTCM_MultiMachineBase<TST_MegaStoneBreaker> {

    // region Class Constructor
    public TST_MegaStoneBreaker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.GODERIUM);
    }

    public TST_MegaStoneBreaker(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaStoneBreaker(this.mName);
    }
    // endregion

    // region Structure
    private final int horizontalOffSet = 10;
    private final int verticalOffSet = 7;
    private final int depthOffSet = 2;
    private static final String STRUCTURE_PIECE_MAIN = "mainMegaStoneBreaker";
    private static IStructureDefinition<TST_MegaStoneBreaker> STRUCTURE_DEFINITION = null;

    // spotless:off
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
    // spotless:on

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
                        .hint(1)
                        .casingIndex(TAE.getIndexFromPage(1, 0))
                        .buildAndChain(ModBlocks.blockCasings2Misc, 0))
                .addElement(
                    'L',
                    buildHatchAdder(TST_MegaStoneBreaker.class).hatchClass(MTEHatchInput.class)
                        .adder(TST_MegaStoneBreaker::addLavaHatch)
                        .casingIndex(TAE.getIndexFromPage(1, 0))
                        .hint(2)
                        .buildAndChain(ModBlocks.blockCasings2Misc, 0))
                .addElement(
                    'W',
                    buildHatchAdder(TST_MegaStoneBreaker.class).hatchClass(MTEHatchInput.class)
                        .adder(TST_MegaStoneBreaker::addWaterHatch)
                        .casingIndex(TAE.getIndexFromPage(1, 0))
                        .hint(3)
                        .buildAndChain(ModBlocks.blockCasings2Misc, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors);
    }
    // endregion

    // region Processing Logic
    private MTEHatchInput mLavaHatch;
    private MTEHatchInput mWaterHatch;
    boolean isOutputMultiply = false;
    private byte runningTick = 0;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return MegaStoneBreakerRecipeMap;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public int getMaxParallelRecipes() {
        return (int) Math.min(Integer.MAX_VALUE, getStoneBreakerParallelLimit());
    }

    private long getStoneBreakerParallelLimit() {
        int tier = (int) calculateVoltageTier(getMaxInputEu());
        return 1L << Math.min(62, Math.max(0, tier) + 2);
    }

    @Override
    protected float getEuModifier() {
        return 1;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            private String selectedDimension() {
                String selected = null;
                for (ItemStack input : inputItems) {
                    if (input == null || input.stackSize <= 0) continue;
                    String dimension = ItemDimensionDisplay.getDimension(input);
                    if (dimension == null) continue;
                    if (selected != null && !selected.equals(dimension)) return "";
                    selected = dimension;
                }
                return selected;
            }

            private boolean matchesDimension(GTRecipe recipe) {
                String dimension = null;
                for (ItemStack input : recipe.mInputs) {
                    if (input == null) continue;
                    dimension = ItemDimensionDisplay.getDimension(input);
                    if (dimension != null) break;
                }
                return Objects.equals(selectedDimension(), dimension);
            }

            @Nonnull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                return super.findRecipeMatches(map).filter(this::matchesDimension);
            }

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                return matchesDimension(recipe) ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.NO_RECIPE;
            }

            @Nonnull
            @Override
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                isOutputMultiply = drain(mWaterHatch, new FluidStack(Materials.Water.mFluid, 1000), false)
                    && drain(mLavaHatch, new FluidStack(Materials.Lava.mFluid, 1000), false);
                int outputBonus = isOutputMultiply ? 1024 : 4;
                return new StoneBreakerParallelHelper().setRecipe(recipe)
                    .setItemInputs(inputItems)
                    .setFluidInputs(inputFluids)
                    .setAvailableEUt(availableVoltage * availableAmperage)
                    .setMachine(machine, protectItems, protectFluids)
                    .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
                    .setMaxParallel(maxParallel)
                    .setChanceMultiplier(outputBonus)
                    .enableBatchMode(batchSize)
                    .setConsumption(true)
                    .setOutputCalculation(true);
            }

            @Nonnull
            @Override
            protected CheckRecipeResult applyRecipe(@Nonnull GTRecipe recipe, @Nonnull ParallelHelper helper,
                @Nonnull OverclockCalculator calculator, @Nonnull CheckRecipeResult result) {
                CheckRecipeResult applied = super.applyRecipe(recipe, helper, calculator, result);
                if (applied.wasSuccessful() && isMEOutputEnabled()) {
                    setLongOutputs(((StoneBreakerParallelHelper) helper).outputs, Collections.emptyList());
                }
                return applied;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private final class StoneBreakerParallelHelper extends ParallelHelper {

        private final List<ItemStackLong> outputs = new ArrayList<>();

        @Override
        protected void calculateItemOutputs(ItemStack[] truncatedItemOutputs) {
            outputs.clear();
            ArrayList<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < truncatedItemOutputs.length; i++) {
                ItemStack output = truncatedItemOutputs[i];
                if (output == null) continue;
                long successes = calculateIntegralChancedOutputMultiplier(
                    (int) (recipe.getOutputChance(i) * chanceMultiplier),
                    currentParallel);
                long amount = Math.multiplyExact(successes, output.stackSize);
                if (amount <= 0) continue;
                if (isMEOutputEnabled()) {
                    outputs.add(new ItemStackLong(output, amount));
                } else {
                    addItemsLong(stacks, output, amount);
                }
            }
            itemOutputs = stacks.toArray(new ItemStack[0]);
            fluidOutputs = new FluidStack[0];
        }
    }

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

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            side,
            aFacing,
            aActive,
            Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(16)),
            TexturesGtBlock.Overlay_Machine_Controller_Advanced,
            TexturesGtBlock.Overlay_Machine_Controller_Advanced_Glow,
            TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active,
            TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active_Glow);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final TSTMultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.machine.MegaStoneBreaker.tooltip.machine_type
        // # Stone Breaker
        // #zh_CN 碎石机
        tt.addMachineType(TSTUtils.tr("tst.machine.MegaStoneBreaker.tooltip.machine_type"))
            // #tr tst.machine.MegaStoneBreaker.tooltip.controller
            // # Controller block for the Silicon Rock Synthesizer
            // #zh_CN 硅岩制造机的控制方块
            .addInfo(TSTUtils.tr("tst.machine.MegaStoneBreaker.tooltip.controller"))
            // #tr tst.machine.MegaStoneBreaker.tooltip.info.01
            // # {\WHITE}Hey, I heard you come from a sky island?
            // #zh_CN {\WHITE}嘿,听说你来自一片空岛?
            .addInfo(TSTUtils.tr("tst.machine.MegaStoneBreaker.tooltip.info.01"))
            // #tr tst.machine.MegaStoneBreaker.tooltip.info.02
            // # Basic parallel is 4, and the multiplier is equivalent to imperfect overclock
            // #zh_CN 基础并行为4, 拥有等同于有损超频的并行加成
            .addInfo(TSTUtils.tr("tst.machine.MegaStoneBreaker.tooltip.info.02"))
            // #tr tst.machine.MegaStoneBreaker.tooltip.info.03
            // # Basic increase in output by 4x, When water and lava are input from the side input hatch, Increase to 1024x of output
            // #zh_CN 基础增产4倍, 当侧面输入仓输入水和岩浆时增产1024倍
            .addInfo(TSTUtils.tr("tst.machine.MegaStoneBreaker.tooltip.info.03"))
            .beginStructureBlock(shapeMain)
            .addController(textFrontBottom)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 1)
            // #tr tst.machine.MegaStoneBreaker.tooltip.structure.01
            // # {\WHITE}Lava Hatch:{GREEN\} Left{\GRAY} side of Structure
            // #zh_CN {\WHITE}岩浆输入仓:{\GRAY}结构的{\GREEN}左{\GRAY}侧
            .addStructureInfo(TSTUtils.tr("tst.machine.MegaStoneBreaker.tooltip.structure.01"))
            // #tr tst.machine.MegaStoneBreaker.tooltip.structure.02
            // # {\WHITE}Water Hatch:{GREEN\} Right{\GRAY} side of Structure
            // #zh_CN {\WHITE}水输入仓:{\GRAY}结构的{\GREEN}右{\GRAY}侧
            .addStructureInfo(TSTUtils.tr("tst.machine.MegaStoneBreaker.tooltip.structure.02"))
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addStructureInfo(Text_SeparatingLine)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

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

    // endregion

}
