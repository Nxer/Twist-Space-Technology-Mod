/*
 * Copyright (c) 2022 Nxer
 */
package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Mode_Default_IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_ICDMode_IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_LCRMode_IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;

// MTEEnhancedMultiBlockBase called failed to use 64A energy hatch
// GT_TileEntity_MegaMultiBlockBase then use megaClass to success to apply 64A hatch
// Why ?
//
// GT_TileEntity_IntensifyChemicalDistorter
@SkipGenerateDescription
public class GT_TileEntity_IntensifyChemicalDistorter
    extends GTCM_MultiMachineBase<GT_TileEntity_IntensifyChemicalDistorter> {

    // region Class Constructor
    public GT_TileEntity_IntensifyChemicalDistorter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        machineMode = Mode_Default_IntensifyChemicalDistorter;
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_IntensifyChemicalDistorter(String aName) {
        super(aName);
        machineMode = Mode_Default_IntensifyChemicalDistorter;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_IntensifyChemicalDistorter(this.mName);
    }
    // endregion

    // region Structure
    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private final int horizontalOffSet = 5;
    private final int verticalOffSet = 12;
    private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_IntensifyChemicalDistorter> STRUCTURE_DEFINITION = null;

    // spotless:off
    /**
     * <li>'s' = Stainless casing ;
     * <li>'v' = Chemically inert casing ;
     * <li>'h' = input Hatch ;
     * <li>'p' = PTFE Pipe casing ;
     * <li>'c' = coil ;
     * <li>'b' = input Bus ;
     * <li>'~' = machine ;
     * <li>'e' = Energy hatch ;
     */
    private final String[][] shape = new String[][]{
        {"    sss    ","  ssvvvss  "," svvvvvvvs "," svvvvvvvs ","svvvvvvvvvs","svvvvevvvvs","svvvvvvvvvs"," svvvvvvvs "," svvvvvvvs ","  ssvvvss  ","    sss    "},
        {"           ","     h     ","     p     ","     p     ","     p     "," hpppcppph ","     p     ","     p     ","     p     ","     h     ","           "},
        {"           ","    h      ","           ","           ","     p   h ","    pcp    "," h   p     ","           ","           ","      h    ","           "},
        {"           ","           ","   h       ","        h  ","     p     ","    pcp    ","     p     ","  h        ","       h   ","           ","           "},
        {"           ","           ","       h   ","  h        ","     p     ","    pcp    ","     p     ","        h  ","   h       ","           ","           "},
        {"           ","      h    ","           ","           "," h   p     ","    pcp    ","     p   h ","           ","           ","    h      ","           "},
        {"           ","     h     ","           ","           ","     p     "," h  pcp  h ","     p     ","           ","           ","     h     ","           "},
        {"           ","    h      ","           ","           ","     p   h ","    pcp    "," h   p     ","           ","           ","      h    ","           "},
        {"           ","           ","   h       ","        h  ","     p     ","    pcp    ","     p     ","  h        ","       h   ","           ","           "},
        {"           ","           ","       h   ","  h        ","     p     ","    pcp    ","     p     ","        h  ","   h       ","           ","           "},
        {"           ","      h    ","           ","           "," h   p     ","    pcp    ","     p   h ","           ","           ","    h      ","           "},
        {"           ","     h     ","     p     ","     p     ","     p     "," hpppcppph ","     p     ","     p     ","     p     ","     h     ","           "},
        {"    b~b    ","  ssvvvss  "," svvvvvvvs "," svvvvvvvs ","bvvvvvvvvvb","bvvvvevvvvb","bvvvvvvvvvb"," svvvvvvvs "," svvvvvvvs ","  ssvvvss  ","    bbb    "}
    };
    // spotless:on

    /**
     * <li>√ 's' = Stainless casing ;
     * <li>√ 'v' = Chemically inert casing ;
     * <li>√ 'h' = input Hatch & output Hatch;
     * <li>√ 'p' = PTFE Pipe casing ;
     * <li>√ 'c' = coil ;
     * <li>√ 'b' = input Bus or output Bus or Maintenance;
     * <li>√ '~' = machine ;
     * <li>√ 'e' = Energy hatch ;
     */
    @Override
    public IStructureDefinition<GT_TileEntity_IntensifyChemicalDistorter> getStructureDefinition() {
        // Structure def
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement('s', ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement('v', ofBlock(GregTechAPI.sBlockCasings8, 0))
                .addElement('p', ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement(
                    'c',
                    withChannel(
                        "coil",
                        ofCoil(
                            GT_TileEntity_IntensifyChemicalDistorter::setCoilLevel,
                            GT_TileEntity_IntensifyChemicalDistorter::getCoilLevel)))
                .addElement(
                    'h',
                    HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                        .atLeast(InputHatch, OutputHatch)
                        .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                        .casingIndex(176)/* index of stainless steal casing */
                        .hint(1)/* preview channel of blueprint */
                        .buildAndChain(GregTechAPI.sBlockCasings8, 0))
                .addElement(
                    'b',
                    HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                        .atLeast(InputBus, OutputBus)
                        .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                        .casingIndex(49)/* index of chem inert casing */
                        .hint(2)/* preview channel of blueprint */
                        .buildAndChain(GregTechAPI.sBlockCasings4, 1))
                .addElement(
                    'e',
                    HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                        .casingIndex(11)
                        .hint(3)
                        .buildAndChain(GregTechAPI.sBlockCasings1, 11))
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

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     *
     * @param aBaseMetaTileEntity
     * @param aStack
     */
    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        coilLevel = HeatingCoilLevel.None;
        // this.casingAmountActual = 0; // re-init counter
        checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors);
    }
    // endregion

    // region Processing Logic
    private HeatingCoilLevel coilLevel = HeatingCoilLevel.None;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH,
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT };

    @Override
    public RecipeMap<?> getRecipeMap() {
        return switch (machineMode) {
            case 1 -> RecipeMaps.multiblockChemicalReactorRecipes;
            case 2 -> RecipeMaps.chemicalPlantRecipes;
            default -> GTCMRecipe.IntensifyChemicalDistorterRecipeMap;
        };
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            GTCMRecipe.IntensifyChemicalDistorterRecipeMap,
            RecipeMaps.multiblockChemicalReactorRecipes,
            RecipeMaps.chemicalPlantRecipes);
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Intense Chemical Distorter
         * 1 - Chemical Reactor
         * 2 - Chemical Plant
         */
        return 3;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // #tr tst.machine.IntensifyChemicalDistorter.mode.0
        // # Mode: Intense Chemical Distorter
        // #zh_CN 深度化学扭曲模式

        // #tr tst.machine.IntensifyChemicalDistorter.mode.1
        // # Mode: Chemical Reactor
        // #zh_CN 化学反应釜模式

        // #tr tst.machine.IntensifyChemicalDistorter.mode.2
        // # Mode: Chemical Plant
        // #zh_CN 化工厂模式
        return StatCollector.translateToLocal("tst.machine.IntensifyChemicalDistorter.mode." + machineMode);
    }

    @Override
    public int getMaxParallelRecipes() {
        return machineMode == 0 ? Parallel_ICDMode_IntensifyChemicalDistorter
            : Parallel_LCRMode_IntensifyChemicalDistorter;
    }

    @Override
    protected float getSpeedBonus() {
        return machineMode == 0 ? 1F / SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter
            : 1F / Math.max(1, coilLevel.getTier() + 1);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    /**
     * Checks if this is a Correct Machine Part for this kind of Machine (Turbine Rotor for example)
     *
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(GTRecipe recipe) {
                if (machineMode == 2) {
                    // Chemical plant special values are casing tiers, not heat requirements.
                    // Native zero-size catalyst inputs remain required and do not wear at this coil tier.
                    return coilLevel.getTier() >= HeatingCoilLevel.UMV.getTier() ? CheckRecipeResultRegistry.SUCCESSFUL
                        : CheckRecipeResultRegistry.insufficientHeat((int) HeatingCoilLevel.UMV.getHeat());
                }
                return recipe.mSpecialValue <= coilLevel.getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(GTRecipe recipe) {
                OverclockCalculator calculator = super.createOverclockCalculator(recipe);
                if (machineMode == 0) {
                    // Each coil tier adds 900 K.
                    int excessCoilTiers = (int) Math.max(0, (coilLevel.getHeat() - recipe.mSpecialValue) / 900);
                    // Recalculate for each matched recipe, including cached recipes and mode changes.
                    calculator.setDurationModifier(speedBoost / (1 + excessCoilTiers));
                }
                return calculator;
            }

        }.enablePerfectOverclock()
            .setMaxParallelSupplier(this::getMaxParallelRecipes);

    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.coilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.coilLevel;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("mode", machineMode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        machineMode = aNBT.getInteger("mode");
    }

    // endregion

    // region Textures

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param aBaseMetaTileEntity
     * @param side                is the Side of the Block
     * @param facing              is the direction the Block is facing
     * @param aColorIndex         The Minecraft Color the Block is having
     * @param aActive             if the Machine is currently active (use this instead of calling
     *                            {@code mBaseMetaTileEntity.mActive)}. Note: In case of Pipes this means if this Side
     *                            is
     *                            connected to something or not.
     * @param aRedstone           if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                            {@code mBaseMetaTileEntity.mRedstone} !!!)
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        return TSTControllerTextures.getTexture(
            side,
            facing,
            aActive,
            casingTexturePages[1][48],
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    // Tooltips
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final TSTMultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.machine.IntensifyChemicalDistorter.tooltip.machine_type
        // # Intensify Chemical Distorter/Chemical Reactor/Chemical Plant
        // #zh_CN 深度化学扭曲仪/化学反应釜/化工厂
        tt.addMachineType(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.machine_type"))
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.1
            // # Controller block for the Intensify Chemical Distorter
            // #zh_CN 深度化学扭曲仪的控制器方块
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.1"))
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.2
            // # {\AQUA}I! {\BLUE}AM! {\AQUA}THE! {\BLUE}CHEM! {\AQUA}THAT! {\BLUE}IS! {\AQUA}APPROOOOOACHING !!
            // #zh_CN {\AQUA}I! {\BLUE}AM! {\AQUA}THE! {\BLUE}CHEM! {\AQUA}THAT! {\BLUE}IS! {\AQUA}APPROOOOOACHING !!
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.2"))
            .addInfo("")
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.3
            // # {\GOLD}Intensify Chemical Distorter mode:
            // #zh_CN {\GOLD}深度化学扭曲模式:
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.3"))
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.4
            // # {\AQUA}%dx {\GRAY}Parallel.
            // #zh_CN {\AQUA}%dx {\GRAY}并行.
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.4", Parallel_ICDMode_IntensifyChemicalDistorter))
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.5
            // # Each coil tier above the recipe requirement adds {\AQUA}100% {\GRAY}processing speed.
            // #zh_CN 线圈每高于配方要求一级, 处理速度增加 {\AQUA}100%{\GRAY}.
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.5"))
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.6
            // # {\GOLD}Chemical Reactor | Chemical Plant modes:
            // #zh_CN {\GOLD}化学反应釜|化工厂模式:
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.6"))
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.7
            // # {\AQUA}%dx {\GRAY}Parallel | Higher-tier coils increase processing speed.
            // #zh_CN {\AQUA}%dx {\GRAY}并行 | 升级线圈可提高处理速度.
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.7", Parallel_LCRMode_IntensifyChemicalDistorter))
            // #tr tst.machine.IntensifyChemicalDistorter.tooltip.info.8
            // # Infinity coils unlock Chemical Plant mode.
            // #zh_CN 无尽线圈解锁化工厂模式.
            .addInfo(TSTUtils.tr("tst.machine.IntensifyChemicalDistorter.tooltip.info.8"))
            .addSeparator()
            .addPerfectOCInfo();
        tt.beginStructureBlock(shape)
            .addController(TSTSharedLocalization.Structure.textFrontBottom)
            .addCasingInfoRange(TSTSharedLocalization.Structure.textCasing, 8, 26, false)
            .addInputHatch(TSTSharedLocalization.Structure.textAnyCasing, 1)
            .addOutputHatch(TSTSharedLocalization.Structure.textAnyCasing, 1)
            .addInputBus(TSTSharedLocalization.Structure.textAnyCasing, 2)
            .addOutputBus(TSTSharedLocalization.Structure.textAnyCasing, 2)
            .addEnergyHatch(TSTSharedLocalization.Structure.textAnyCasing, 3)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    // endregion

}
