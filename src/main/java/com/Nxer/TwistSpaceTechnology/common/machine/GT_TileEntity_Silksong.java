package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_PerPiece_Silksong;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedBonus_MultiplyPerVoltageTier_Silksong;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedMultiplier_CoilTier_Silksong;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.pressureResistantWalls;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
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
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings8;

public class GT_TileEntity_Silksong extends GTCM_MultiMachineBase<GT_TileEntity_Silksong> {

    // region Class Constructor
    public GT_TileEntity_Silksong(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_Silksong(String aName) {
        super(aName);
    }
    // endregion

    // region Processing Logic
    private int piece = 1;
    private HeatingCoilLevel coilLevel;

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    public int getCoilTier() {
        return TstUtils.getVoltageForCoil(coilLevel);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("piece", piece);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        piece = aNBT.getInteger("piece");
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.wiremillRecipes;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        coilLevel = HeatingCoilLevel.None;

        this.piece = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }

        while (checkPiece(STRUCTURE_PIECE_MIDDLE, horizontalOffSet, verticalOffSet, depthOffSet - piece * 2 - 2)) {
            this.piece++;
        }

        if (this.piece < 1
            || !checkPiece(STRUCTURE_PIECE_END, horizontalOffSet, verticalOffSet, depthOffSet - piece * 2 - 2)) {
            return false;
        }

        // parallel = piece * coilTier * 32
        maxParallel = (int) Math.min((long) piece * getCoilTier() * Parallel_PerPiece_Silksong, Integer.MAX_VALUE);

        // speed bonus = 0.85^voltageTier / (coilTier * 1)
        speedBonus = (float) (Math.pow(SpeedBonus_MultiplyPerVoltageTier_Silksong, getTotalPowerTier())
            / (getCoilTier() * SpeedMultiplier_CoilTier_Silksong));

        return true;
    }
    // endregion

    // region Structure
    // spotless:off

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int piece = stackSize.stackSize;
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);

        for (int pointer = 1; pointer <= piece; pointer++) {
            this.buildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                hintsOnly,
                horizontalOffSet,
                verticalOffSet,
                depthOffSet - pointer * 2);
        }

        this.buildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            hintsOnly,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet - piece * 2 - 2);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int[] built = new int[stackSize.stackSize + 2];

        built[0] = survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);

        int piece = stackSize.stackSize;

        for (int pointer = 1; pointer <= piece; pointer++) {
            built[pointer] = survivialBuildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                horizontalOffSet,
                verticalOffSet,
                depthOffSet - pointer * 2,
                elementBudget,
                env,
                false,
                true);
        }

        built[built.length - 1] = survivialBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet - piece * 2 - 2,
            elementBudget,
            env,
            false,
            true);

        return TstUtils.multiBuildPiece(built);
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainSilksong";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleSilksong";
    private static final String STRUCTURE_PIECE_END = "endSilksong";
    private final int horizontalOffSet = 3;
    private final int verticalOffSet = 5;
    private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_Silksong> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GT_TileEntity_Silksong> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_Silksong>builder()
                                                      .addShape(STRUCTURE_PIECE_MAIN, shapeMain)
                                                      .addShape(STRUCTURE_PIECE_MIDDLE, shapeMiddle)
                                                      .addShape(STRUCTURE_PIECE_END, shapeEnd)
                                                      .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 11))
                                                      .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 15))
                                                      .addElement(
                                                          'C',
                                                          withChannel("coil", ofCoil(GT_TileEntity_Silksong::setCoilLevel, GT_TileEntity_Silksong::getCoilLevel)))
                                                      .addElement(
                                                          'D',
                                                          HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                                                                                .atLeast(Energy.or(ExoticEnergy))
                                                                                .adder(GT_TileEntity_Silksong::addToMachineList)
                                                                                .dot(1)
                                                                                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                                                                                .buildAndChain(GregTechAPI.sBlockCasings8, 2))
                                                      .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 7))
                                                      .addElement('F', ofBlock(pressureResistantWalls, 0))
                                                      .addElement(
                                                          'G',
                                                          HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                                                                                .atLeast(OutputBus, OutputHatch)
                                                                                .adder(GT_TileEntity_Silksong::addToMachineList)
                                                                                .dot(2)
                                                                                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                                                                                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                                                      .addElement('H', ofFrame(Materials.Neutronium))
                                                      .addElement(
                                                          'I',
                                                          HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                                                                                .atLeast(InputBus, InputHatch)
                                                                                .adder(GT_TileEntity_Silksong::addToMachineList)
                                                                                .dot(3)
                                                                                .casingIndex(((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(11))
                                                                                .buildAndChain(GregTechAPI.sBlockCasings1, 11))
                                                      .build();
        }
        return STRUCTURE_DEFINITION;
    }

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasings, 11, ...);
     * B -> ofBlock...(gt.blockcasings2, 15, ...);
     * C -> ofBlock...(gt.blockcasings5, 0, ...); // coil
     * D -> ofBlock...(gt.blockcasings8, 2, ...); // energy maintenance
     * E -> ofBlock...(gt.blockcasings8, 7, ...);
     * F -> ofBlock...(pressureResistantWalls, 0, ...);
     * G -> ofBlock...(gt.blockcasings8, 7, ...); // output
     * H -> ofFrame...();
     * I -> ofBlock...(gt.blockcasings, 11, ...); // input
     */
    private final String[][] shapeMain = new String[][] {
        { "       ", "       ", "       ", "       ", "  DDD  ", "  D~D  ", "  DDD  " },
        { "       ", "  III  ", "  III  ", "  III  ", "  DDD  ", "  DDD  ", "  DDD  " } };

    private final String[][] shapeMiddle = new String[][] {
        { "       ", "  CCC  ", "  CBC  ", "  CCC  ", "       ", "       ", "DDDDDDD" },
        { "  AAA  ", " ACCCA ", " ACBCA ", " ACCCA ", " HAAAH ", " H   H ", "DDDDDDD" } };

    private final String[][] shapeEnd = new String[][] {
        { "  EEE  ", " EFFFE ", " EFBFE ", " EFFFE ", "  EEE  ", "       ", "DDDDDDD" },
        { "  EEE  ", " EFFFE ", " EFBFE ", " EFFFE ", "  EEE  ", "       ", "  DDD  " },
        { "  EEE  ", " EFFFE ", " EFBFE ", " EFFFE ", "  EEE  ", "       ", "  DDD  " },
        { "       ", "  GGG  ", "  GBG  ", "  GGG  ", "  DDD  ", "  DDD  ", "  DDD  " },
        { "       ", "  GGG  ", "  GGG  ", "  GGG  ", "       ", "  DDD  ", "  DDD  " } };

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    // spotless:on
    // endregion

    // region Overrides

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_Silksong_MachineType)
            .addInfo(TextLocalization.Tooltip_Silksong_00)
            .addInfo(TextLocalization.Tooltip_Silksong_01)
            .addInfo(TextLocalization.Tooltip_Silksong_02)
            .addInfo(TextLocalization.Tooltip_Silksong_03)
            .addInfo(TextLocalization.Tooltip_Silksong_04)
            .addInfo(TextLocalization.Tooltip_Silksong_05)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.textUseBlueprint, 3)
            .addOutputHatch(TextLocalization.textUseBlueprint, 3)
            .addInputBus(TextLocalization.textUseBlueprint, 3)
            .addOutputBus(TextLocalization.textUseBlueprint, 2)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_Silksong(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {

        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 2)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 2)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 2)) };
    }

    // endregion
}
