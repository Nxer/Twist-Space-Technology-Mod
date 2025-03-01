package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_PerRing_MagneticDomainConstructor;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_MagneticDomainConstructor;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static goodgenerator.loader.Loaders.compactFusionCoil;
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
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;

public class GT_TileEntity_MagneticDomainConstructor
    extends GTCM_MultiMachineBase<GT_TileEntity_MagneticDomainConstructor> {

    // region Class Constructor
    public GT_TileEntity_MagneticDomainConstructor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MagneticDomainConstructor(String aName) {
        super(aName);
    }
    // endregion

    // region Processing Logic
    private int rings = 1;
    private int parallel = 1;
    private float speedBonus = 1;

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Separator
         * 1 - Polarizer
         */
        return 2;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_POLARIZER);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("MagneticDomainConstructor.modeMsg." + mode);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setInteger("rings", rings);
        aNBT.setInteger("parallel", parallel);
        aNBT.setFloat("speedBonus", speedBonus);

    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getByte("mode");
        rings = aNBT.getInteger("rings");
        parallel = aNBT.getInteger("parallel");
        speedBonus = aNBT.getFloat("speedBonus");
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    public int getMaxParallelRecipes() {
        return parallel;
    }

    public float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return machineMode == 1 ? RecipeMaps.polarizerRecipes : RecipeMaps.electroMagneticSeparatorRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.polarizerRecipes, RecipeMaps.electroMagneticSeparatorRecipes);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();

        this.rings = 1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet)) {
            return false;
        }

        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - this.rings * 4)) {

            this.rings++;
        }

        if (!checkPiece(
            STRUCTURE_PIECE_END,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - this.rings * 4)) {

            return false;
        }

        parallel = (int) Math.min((long) rings * Parallel_PerRing_MagneticDomainConstructor, Integer.MAX_VALUE);
        speedBonus = (float) Math
            .pow(SpeedBonus_MultiplyPerTier_MagneticDomainConstructor, TstUtils.calculateVoltageTier(getMaxInputEu()));

        return true;
    }

    // endregion

    // region Structure
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int Ring = stackSize.stackSize;
        this.buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet);

        if (Ring > 1) {
            for (int pointer = 1; pointer < Ring; pointer++) {
                this.buildPiece(
                    STRUCTURE_PIECE_MIDDLE,
                    stackSize,
                    hintsOnly,
                    baseHorizontalOffSet,
                    baseVerticalOffSet,
                    baseDepthOffSet - pointer * 4);
            }
        }

        this.buildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - Ring * 4);

    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;

        int[] built = new int[stackSize.stackSize + 2];

        built[0] = survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet,
            elementBudget,
            env,
            false,
            true);

        int ring = stackSize.stackSize;

        if (ring > 1) {
            int pointer = 1;
            while (pointer < ring) {
                built[pointer] = survivialBuildPiece(
                    STRUCTURE_PIECE_MIDDLE,
                    stackSize,
                    baseHorizontalOffSet,
                    baseVerticalOffSet,
                    baseDepthOffSet - pointer * 4,
                    elementBudget,
                    env,
                    false,
                    true);
                pointer++;
            }
        }

        built[ring + 1] = survivialBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - ring * 4,
            elementBudget,
            env,
            false,
            true);

        return TstUtils.multiBuildPiece(built);

    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    /*
     * Blocks:
     * A -> ofBlock...(compactFusionCoil, 0, ...);
     * B -> ofBlock...(gt.blockcasings2, 8, ...);
     * C -> ofBlock...(gt.blockcasings8, 7, ...);
     * D -> ofBlock...(gt.blockcasings8, 10, ...); // Energy Hatch, Maintenance
     * E -> ofBlock...(gt.blockcasings8, 7, ...); // IO Hatch
     * F -> ofFrame...(NaquadahAlloy);
     * F -> ofFrame...(Tengam);
     */
    @Override
    public IStructureDefinition<GT_TileEntity_MagneticDomainConstructor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MagneticDomainConstructor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, shapeMain)
                .addShape(STRUCTURE_PIECE_MIDDLE, shapeMiddle)
                .addShape(STRUCTURE_PIECE_END, shapeEnd)
                .addElement('A', ofBlock(compactFusionCoil, 0))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 8))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'D', // Energy Hatch, Maintenance
                    HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                        .dot(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 10))
                .addElement(
                    'E',
                    HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                        .atLeast(InputBus, InputHatch)
                        .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                        .dot(2)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'O',
                    HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                        .atLeast(OutputBus, OutputHatch)
                        .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                        .dot(3)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement('F', ofFrame(Materials.NaquadahAlloy))
                .addElement('G', ofFrame(Materials.TengamAttuned))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private final int baseHorizontalOffSet = 7;
    private final int baseVerticalOffSet = 15;
    private final int baseDepthOffSet = 0;

    private static final String STRUCTURE_PIECE_MAIN = "mainMagneticDomainConstructor";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleMagneticDomainConstructor";
    private static final String STRUCTURE_PIECE_END = "endMagneticDomainConstructor";

    private static IStructureDefinition<GT_TileEntity_MagneticDomainConstructor> STRUCTURE_DEFINITION = null;

    /**
     * The first piece of Structure
     */
    private final String[][] shapeMain = new String[][] {
        { "               ", "               ", "               ", "               ", "               ",
            "      DDD      ", "     DEEED     ", "     DEEED     ", "     DEEED     ", "      DDD      ",
            "      FDF      ", "      FDF      ", "      FDF      ", "      FDF      ", "      FDF      ",
            "      D~D      ", "     DDDDD     " },
        { "               ", "      BBB      ", "    BB   BB    ", "   B       B   ", "  B         B  ",
            "  B         B  ", " B     C     B ", " B    CCC    B ", " B     C     B ", "  B         B  ",
            "  B         B  ", "   B       B   ", "   FBB   BBF   ", "  F   BBB   F  ", " F    DDD    F ",
            "F     DDD     F", "DDDDDDDDDDDDDDD" },
        { "      BBB      ", "    BBAAABB    ", "   BAAGGGAAB   ", "  BAGG   GGAB  ", " BAG       GAB ",
            " BAG       GAB ", "BAG    C    GAB", "BAG   CCC   GAB", "BAG    C    GAB", " BAG       GAB ",
            " BAG       GAB ", "  BAGG   GGAB  ", "   BAAGGGAAB   ", "    BBAAABB    ", "      BBB      ",
            "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "    BB   BB    ", "   B       B   ", "  B         B  ",
            "  B         B  ", " B     C     B ", " B    CCC    B ", " B     C     B ", "  B         B  ",
            "  B         B  ", "   B       B   ", "   FBB   BBF   ", "  F   BBB   F  ", " F    DDD    F ",
            "F     DDD     F", "DDDDDDDDDDDDDDD" } };
    /**
     * The middle of Structure
     */
    private final String[][] shapeMiddle = new String[][] {
        { "               ", "               ", "               ", "               ", "               ",
            "               ", "       C       ", "      CCC      ", "       C       ", "               ",
            "               ", "               ", "               ", "               ", "      DDD      ",
            "      DDD      ", "     DDDDD     " },
        { "               ", "      BBB      ", "    BB   BB    ", "   B       B   ", "  B         B  ",
            "  B         B  ", " B     C     B ", " B    CCC    B ", " B     C     B ", "  B         B  ",
            "  B         B  ", "   B       B   ", "   FBB   BBF   ", "  F   BBB   F  ", " F    DDD    F ",
            "F     DDD     F", "DDDDDDDDDDDDDDD" },
        { "      BBB      ", "    BBAAABB    ", "   BAAGGGAAB   ", "  BAGG   GGAB  ", " BAG       GAB ",
            " BAG       GAB ", "BAG    C    GAB", "BAG   CCC   GAB", "BAG    C    GAB", " BAG       GAB ",
            " BAG       GAB ", "  BAGG   GGAB  ", "   BAAGGGAAB   ", "    BBAAABB    ", "      BBB      ",
            "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD" },
        { "               ", "      BBB      ", "    BB   BB    ", "   B       B   ", "  B         B  ",
            "  B         B  ", " B     C     B ", " B    CCC    B ", " B     C     B ", "  B         B  ",
            "  B         B  ", "   B       B   ", "   FBB   BBF   ", "  F   BBB   F  ", " F    DDD    F ",
            "F     DDD     F", "DDDDDDDDDDDDDDD" } };
    /**
     * The end of Structure
     */
    private final String[][] shapeEnd = new String[][] { { "               ", "               ", "               ",
        "               ", "               ", "      DDD      ", "     DOOOD     ", "     DOOOD     ",
        "     DOOOD     ", "      DDD      ", "      FDF      ", "      FDF      ", "      FDF      ",
        "      FDF      ", "      FDF      ", "      DDD      ", "     DDDDD     " } };

    // endregion

    // region Overrides

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Rings: " + EnumChatFormatting.GOLD + this.rings;
        return ret;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_MagneticDomainConstructor_MachineType)
            .addInfo(TextLocalization.Tooltip_MagneticDomainConstructor_00)
            .addInfo(TextLocalization.Tooltip_MagneticDomainConstructor_01)
            .addInfo(TextLocalization.Tooltip_MagneticDomainConstructor_02)
            .addInfo(TextLocalization.Tooltip_MagneticDomainConstructor_03)
            .addInfo(TextLocalization.Tooltip_MagneticDomainConstructor_04)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.textUseBlueprint, 2)
            .addOutputHatch(TextLocalization.textUseBlueprint, 3)
            .addInputBus(TextLocalization.textUseBlueprint, 2)
            .addOutputBus(TextLocalization.textUseBlueprint, 3)
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
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MagneticDomainConstructor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
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
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
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
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)) };
    }

    // endregion

}
