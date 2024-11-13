package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MegaStoneBreakerRecipes;
import static com.Nxer.TwistSpaceTechnology.config.Config.EuModifier_MegaStoneBreaker;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedBonus_MegaStoneBreaker;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.Nxer.TwistSpaceTechnology.util.Utils.fluidEqual;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    int MaxParallel = 1;
    private MTEHatchInput mLavaHatch;
    private MTEHatchInput mWaterHatch;
    boolean isEnablePerfectOverclock = false;

    @Override
    protected float getEuModifier() {
        return EuModifier_MegaStoneBreaker;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return isEnablePerfectOverclock;
    }

    @Override
    protected float getSpeedBonus() {
        return SpeedBonus_MegaStoneBreaker;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return MaxParallel;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return MegaStoneBreakerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setEuModifier(getEuModifier());
                setOverclock(isEnablePerfectOverclock() ? 2 : 1, 2);
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                FluidStack WaterStack = null;
                FluidStack LavaStack = null;
                for (FluidStack aStack : getStoredFluids()) {
                    if (fluidEqual(aStack, Materials.Lava.getFluid(1))) LavaStack = aStack.copy();
                    else if (fluidEqual(aStack, Materials.Water.getFluid(1))) WaterStack = aStack.copy();
                    else return CheckRecipeResultRegistry.NO_RECIPE;
                }
                if (WaterStack != null && LavaStack != null) {
                    isEnablePerfectOverclock = drain(mWaterHatch, new FluidStack(Materials.Water.mFluid, 1), false)
                        && drain(mLavaHatch, new FluidStack(Materials.Lava.mFluid, 1), false)
                        && LavaStack.amount == WaterStack.amount;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);

    }

    private byte runningTick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (runningTick % 20 == 0) {
            if (isEnablePerfectOverclock) {
                if (!drain(mWaterHatch, new FluidStack(Materials.Water.mFluid, 1000), true)
                    || !drain(mLavaHatch, new FluidStack(Materials.Lava.mFluid, 1000), true)) {
                    isEnablePerfectOverclock = false;
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
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("MaxParallel", MaxParallel);
        aNBT.setBoolean("isEnablePerfectOverclock", isEnablePerfectOverclock);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        MaxParallel = aNBT.getInteger("MaxParallel");
        isEnablePerfectOverclock = aNBT.getBoolean("isEnablePerfectOverclock");
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
            // # Controller block for the Mega Stone Breaker
            // #zh_CN 巨型碎石机的控制方块
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker_Controller"))
            // #tr Tooltip_MegaStoneBreaker.1
            // # {\WHITE}Hey, I heard you come from a sky island?
            // #zh_CN {\WHITE}嘿,听说你来自一片空岛?
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker.1"))
            // #tr Tooltip_MegaStoneBreaker.2
            // # Basic parallel is 16
            // #zh_CN 基础并行为16
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker.2"))
            // #tr Tooltip_MegaStoneBreaker.3
            // # Parallel multiplier is equivalent to imperfect overclock
            // #zh_CN 拥有等同于有损超频的并行加成
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker.3"))
            // #tr Tooltip_MegaStoneBreaker.4
            // # When equal amounts of water and lava are input from the side input hatch, Enable perfect overclock
            // #zh_CN 当侧面输入仓输入等量的水和岩浆时执行无损超频
            .addInfo(TextEnums.tr("Tooltip_MegaStoneBreaker.4"))
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
            // #zh_CN {\WHITE}岩浆输入仓:{\GRAY}结构的{\GREEN}右{\GRAY}侧
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
