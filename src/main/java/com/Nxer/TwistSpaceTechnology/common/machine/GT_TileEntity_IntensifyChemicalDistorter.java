/*
 * Copyright (c) 2022 Nxer
 */
package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Mode_Default_IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_ICDMode_IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_LCRMode_IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter;
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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

// MTEEnhancedMultiBlockBase called failed to use 64A energy hatch
// GT_TileEntity_MegaMultiBlockBase then use megaClass to success to apply 64A hatch
// Why ?
//
// GT_TileEntity_IntensifyChemicalDistorter
public class GT_TileEntity_IntensifyChemicalDistorter
    extends GTCM_MultiMachineBase<GT_TileEntity_IntensifyChemicalDistorter> {

    // region Class Constructor

    public GT_TileEntity_IntensifyChemicalDistorter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_IntensifyChemicalDistorter(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic

    protected int mode = Mode_Default_IntensifyChemicalDistorter;// 0 means IntensifyChemicalDistorter; 1 means LCR adv
    private HeatingCoilLevel coilLevel;

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (mode == 0) return GTCMRecipe.IntensifyChemicalDistorterRecipes;
        return RecipeMaps.multiblockChemicalReactorRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTCMRecipe.IntensifyChemicalDistorterRecipes, RecipeMaps.multiblockChemicalReactorRecipes);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclock(isEnablePerfectOverclock() ? 2 : 1, 2);
                return super.process();
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(GTRecipe recipe) {
                return recipe.mSpecialValue <= coilLevel.getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

        }.enablePerfectOverclock()
            .setMaxParallelSupplier(this::getMaxParallelRecipes);

    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected float getSpeedBonus() {
        return mode == 0 ? 1F / SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter
            : 1F / SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return this.mode == 0 ? Parallel_ICDMode_IntensifyChemicalDistorter
            : Parallel_LCRMode_IntensifyChemicalDistorter;
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
    private final String[][] shape = new String[][] {
        { "    sss    ", "  ssvvvss  ", " svvvvvvvs ", " svvvvvvvs ", "svvvvvvvvvs", "svvvvevvvvs", "svvvvvvvvvs",
            " svvvvvvvs ", " svvvvvvvs ", "  ssvvvss  ", "    sss    " },
        { "           ", "     h     ", "     p     ", "     p     ", "     p     ", " hpppcppph ", "     p     ",
            "     p     ", "     p     ", "     h     ", "           " },
        { "           ", "    h      ", "           ", "           ", "     p   h ", "    pcp    ", " h   p     ",
            "           ", "           ", "      h    ", "           " },
        { "           ", "           ", "   h       ", "        h  ", "     p     ", "    pcp    ", "     p     ",
            "  h        ", "       h   ", "           ", "           " },
        { "           ", "           ", "       h   ", "  h        ", "     p     ", "    pcp    ", "     p     ",
            "        h  ", "   h       ", "           ", "           " },
        { "           ", "      h    ", "           ", "           ", " h   p     ", "    pcp    ", "     p   h ",
            "           ", "           ", "    h      ", "           " },
        { "           ", "     h     ", "           ", "           ", "     p     ", " h  pcp  h ", "     p     ",
            "           ", "           ", "     h     ", "           " },
        { "           ", "    h      ", "           ", "           ", "     p   h ", "    pcp    ", " h   p     ",
            "           ", "           ", "      h    ", "           " },
        { "           ", "           ", "   h       ", "        h  ", "     p     ", "    pcp    ", "     p     ",
            "  h        ", "       h   ", "           ", "           " },
        { "           ", "           ", "       h   ", "  h        ", "     p     ", "    pcp    ", "     p     ",
            "        h  ", "   h       ", "           ", "           " },
        { "           ", "      h    ", "           ", "           ", " h   p     ", "    pcp    ", "     p   h ",
            "           ", "           ", "    h      ", "           " },
        { "           ", "     h     ", "     p     ", "     p     ", "     p     ", " hpppcppph ", "     p     ",
            "     p     ", "     p     ", "     h     ", "           " },
        { "    b~b    ", "  ssvvvss  ", " svvvvvvvs ", " svvvvvvvs ", "bvvvvvvvvvb", "bvvvvevvvvb", "bvvvvvvvvvb",
            " svvvvvvvs ", " svvvvvvvs ", "  ssvvvss  ", "    bbb    " } };

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
                        .dot(1)/* preview channel of blueprint */
                        .buildAndChain(GregTechAPI.sBlockCasings8, 0))
                .addElement(
                    'b',
                    HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                        .atLeast(InputBus, OutputBus)
                        .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                        .casingIndex(49)/* index of chem inert casing */
                        .dot(2)/* preview channel of blueprint */
                        .buildAndChain(GregTechAPI.sBlockCasings4, 1))
                .addElement(
                    'e',
                    HatchElementBuilder.<GT_TileEntity_IntensifyChemicalDistorter>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_IntensifyChemicalDistorter::addToMachineList)
                        .casingIndex(11)
                        .dot(3)
                        .buildAndChain(GregTechAPI.sBlockCasings1, 11))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.coilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.coilLevel;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

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
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("IntensifyChemicalDistorter.mode." + this.mode));
        }
    }

    //

    /**
     * Checks if this is a Correct Machine Part for this kind of Machine (Turbine Rotor for example)
     *
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     *
     * @param aBaseMetaTileEntity
     * @param aStack
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        coilLevel = HeatingCoilLevel.None;
        // this.casingAmountActual = 0; // re-init counter
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    /**
     * Gets the maximum Efficiency that spare Part can get (0 - 10000)
     *
     * @param aStack
     */
    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    /**
     * Gets the damage to the ItemStack, usually 0 or 1.
     *
     * @param aStack
     */
    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    /**
     * If it explodes when the Component has to be replaced.
     *
     * @param aStack
     */
    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getInteger("mode");
    }

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_IntensifyChemicalDistorter(this.mName);
    }

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

    // Tooltips
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_ICD_MachineType)
            .addInfo(TextLocalization.Tooltip_ICD_00)
            .addInfo(TextLocalization.Tooltip_ICD_01)
            .addInfo(TextLocalization.Tooltip_ICD_02)
            .addInfo(TextLocalization.Tooltip_ICD_03)
            .addInfo(TextLocalization.Tooltip_ICD_04)
            .addInfo(TextLocalization.Tooltip_ICD_05)
            .addInfo(TextLocalization.Tooltip_ICD_06)
            .addInfo(TextLocalization.Tooltip_ICD_07)
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(11, 13, 11, false)
            .addController(TextLocalization.textFrontBottom)
            .addCasingInfoRange(TextLocalization.textCasing, 8, 26, false)
            .addInputHatch(TextLocalization.textAnyCasing, 1)
            .addOutputHatch(TextLocalization.textAnyCasing, 1)
            .addInputBus(TextLocalization.textAnyCasing, 2)
            .addOutputBus(TextLocalization.textAnyCasing, 2)
            .addEnergyHatch(TextLocalization.textAnyCasing, 3)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

}
