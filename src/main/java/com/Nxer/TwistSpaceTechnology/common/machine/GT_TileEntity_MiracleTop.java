package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_PerRing_MiracleTop;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.RingsAmount_EnablePerfectOverclock_MiracleTop;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedUpMultiplier_PerRing_MiracleTop;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.text.TextLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import tectech.thing.block.BlockQuantumGlass;

@SkipGenerateDescription
public class GT_TileEntity_MiracleTop extends GTCM_MultiMachineBase<GT_TileEntity_MiracleTop> {

    // region Class Constructor
    public GT_TileEntity_MiracleTop(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_MiracleTop(String aName) {
        super(aName);
    }

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MiracleTop(this.mName);
    }
    // endregion

    // region Structure
    private final int baseHorizontalOffSet = 10;
    private final int baseVerticalOffSet = 10;
    private final int baseDepthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainMiracleTop";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleMiracleTop";
    private static final String STRUCTURE_PIECE_END = "endMiracleTop";
    private static IStructureDefinition<GT_TileEntity_MiracleTop> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] shapeMain = new String[][]{
        {"                     ","         HHH         ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"         AAA         ","       AADDDAA       ","         HHH         ","                     ","                     ","                     ","                     ","                     "},
        {"       AA   AA       ","      ADDEEEDDA      ","       AA   AA       ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","       EE   EE       ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","  A               A  ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"  A               A  "," ADE             EDA ","  A               A  ","                     ","                     ","                     ","                     ","                     "},
        {"  A      AAA      A  "," ADE     EEE     EDA ","  A      AAA      A  ","                     ","                     ","                     ","                     ","                     "},
        {" A      AMMMA      A ","HDE     EBBBE     EDH"," H      AAAAA      H ","         EEE         ","         EEE         ","         EEE         ","         EEE         ","         EEE         "},
        {" A      AM~MA      A ","HDE     EBCBE     EDH"," H      AACAA      H ","         ECE         ","         ECE         ","         ECE         ","         ECE         ","         ECE         "},
        {" A      AMMMA      A ","HDE     EBBBE     EDH"," H      AAAAA      H ","         EEE         ","         EEE         ","         EEE         ","         EEE         ","         EEE         "},
        {"  A      AAA      A  "," ADE     EEE     EDA ","  A      AAA      A  ","                     ","                     ","                     ","                     ","                     "},
        {"  A               A  "," ADE             EDA ","  A               A  ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","  A               A  ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","       EE   EE       ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"       AA   AA       ","      ADDEEEDDA      ","       AA   AA       ","                     ","                     ","                     ","                     ","                     "},
        {"         AAA         ","       AADDDAA       ","         AAA         ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","         HHH         ","                     ","                     ","                     ","                     ","                     ","                     "}
    };

    /*
     * A -> ofBlock...(gt.blockcasingsTT, 4, ...);
     * B -> ofBlock...(gt.blockcasingsTT, 7, ...);
     * C -> ofBlock...(gt.blockcasingsTT, 9, ...);
     * D -> ofBlock...(gtplusplus.blockcasings.4, 4, ...);
     * E -> ofBlock...(tile.quantumGlass, 0, ...);
     */

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasingsTT, 4, ...);
     * B -> ofBlock...(gt.blockcasingsTT, 7, ...);
     * C -> ofBlock...(gt.blockcasingsTT, 9, ...);
     * D -> ofBlock...(gtplusplus.blockcasings.4, 4, ...);
     * E -> ofBlock...(tile.quantumGlass, 0, ...);
     */

    private final String[][] shapeMiddle = new String[][]{
        {"                     ","         HHH         ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"         AAA         ","       AADDDAA       ","         AAA         ","                     ","                     ","                     ","                     ","                     "},
        {"       AA   AA       ","      ADDEEEDDA      ","       AA   AA       ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","       EE   EE       ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","  A               A  ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"  A               A  "," ADE             EDA ","  A               A  ","                     ","                     ","                     ","                     ","                     "},
        {"  A               A  "," ADE             EDA ","  A               A  ","                     ","                     ","                     ","                     ","                     "},
        {" A       EEE       A ","HDE      EEE      EDH"," A       EEE       A ","         EEE         ","         EEE         ","         EEE         ","         EEE         ","         EEE         "},
        {" A       ECE       A ","HDE      ECE      EDH"," A       ECE       A ","         ECE         ","         ECE         ","         ECE         ","         ECE         ","         ECE         "},
        {" A       EEE       A ","HDE      EEE      EDH"," A       EEE       A ","         EEE         ","         EEE         ","         EEE         ","         EEE         ","         EEE         "},
        {"  A               A  "," ADE             EDA ","  A               A  ","                     ","                     ","                     ","                     ","                     "},
        {"  A               A  "," ADE             EDA ","  A               A  ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","  A               A  ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","                     ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","       EE   EE       ","                     ","                     ","                     ","                     ","                     ","                     "},
        {"       AA   AA       ","      ADDEEEDDA      ","       AA   AA       ","                     ","                     ","                     ","                     ","                     "},
        {"         AAA         ","       AADDDAA       ","         AAA         ","                     ","                     ","                     ","                     ","                     "},
        {"                     ","         HHH         ","                     ","                     ","                     ","                     ","                     ","                     "}
    };

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasingsTT, 4, ...);
     * B -> ofBlock...(gt.blockcasingsTT, 7, ...);
     * C -> ofBlock...(gt.blockcasingsTT, 9, ...);
     * D -> ofBlock...(gtplusplus.blockcasings.4, 4, ...);
     * E -> ofBlock...(tile.quantumGlass, 0, ...);
     */

    private final String[][] shapeEnd = new String[][]{
        {"                     ","         HHH         ","                     "},
        {"         AAA         ","       AADDDAA       ","         AAA         "},
        {"       AA   AA       ","      ADDEEEDDA      ","       AA   AA       "},
        {"                     ","       EE   EE       ","                     "},
        {"                     ","                     ","                     "},
        {"                     ","                     ","                     "},
        {"                     ","  A               A  ","                     "},
        {"  A               A  "," ADE             EDA ","  A               A  "},
        {"  A      AAA      A  "," ADE     EEE     EDA ","  A      AAA      A  "},
        {" A      AAAAA      A ","HDE     EBBBE     EDH"," A      AHHHA      A "},
        {" A      AACAA      A ","HDE     EBCBE     EDH"," A      AHHHA      A "},
        {" A      AAAAA      A ","HDE     EBBBE     EDH"," A      AHHHA      A "},
        {"  A      AAA      A  "," ADE     EEE     EDA ","  A      AAA      A  "},
        {"  A               A  "," ADE             EDA ","  A               A  "},
        {"                     ","  A               A  ","                     "},
        {"                     ","                     ","                     "},
        {"                     ","                     ","                     "},
        {"                     ","       EE   EE       ","                     "},
        {"       AA   AA       ","      ADDEEEDDA      ","       AA   AA       "},
        {"         AAA         ","       AADDDAA       ","         AAA         "},
        {"                     ","         HHH         ","                     "}
    };
    // spotless:on

    /*
     * A -> ofBlock...(gt.blockcasingsTT, 4, ...);
     * B -> ofBlock...(gt.blockcasingsTT, 7, ...);
     * C -> ofBlock...(gt.blockcasingsTT, 9, ...);
     * D -> ofBlock...(gtplusplus.blockcasings.4, 4, ...);
     * E -> ofBlock...(tile.quantumGlass, 0, ...);
     * H -> Hatches;
     * M -> Maintenance Hatch;
     */
    @Override
    public IStructureDefinition<GT_TileEntity_MiracleTop> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MiracleTop>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
                .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
                .addElement('A', ofBlock(sBlockCasingsTT, 4))
                .addElement('B', ofBlock(sBlockCasingsTT, 7))
                .addElement('C', ofBlock(sBlockCasingsTT, 9))
                .addElement('D', ofBlock(ModBlocks.blockCasings4Misc, 4))
                .addElement('E', ofBlock(BlockQuantumGlass.INSTANCE, 0))
                .addElement(
                    'M',
                    GTStructureUtility.buildHatchAdder(GT_TileEntity_MiracleTop.class)
                        .atLeast(Maintenance)
                        .hint(1)
                        .casingIndex(1028)
                        .buildAndChain(sBlockCasingsTT, 4))
                .addElement(
                    'H',
                    GTStructureUtility.buildHatchAdder(GT_TileEntity_MiracleTop.class)
                        .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                        .hint(2)
                        .casingIndex(1028)
                        .buildAndChain(sBlockCasingsTT, 4))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet);
        int pointer = 1;
        for (; pointer < Math.min(15, stackSize.stackSize); pointer++) {

            buildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                hintsOnly,
                baseHorizontalOffSet,
                baseVerticalOffSet,
                baseDepthOffSet - pointer * 8);
        }
        buildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - pointer * 8);
    }

    /**
     * Called when try auto construct in survival mode.
     *
     * @param stackSize     The StructureLib Blueprint item stack.
     * @param elementBudget The server configured element budget. The implementor can choose to tune this up a bit if
     *                      the structure is too big, but generally should not be a 4 digits number to not overwhelm the
     *                      server.
     */
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {

        if (this.mMachine) return -1;

        int built = 0;

        built = survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet,
            elementBudget,
            env,
            false,
            true);

        if (built >= 0) return built;

        int rings = Math.min(14, stackSize.stackSize - 1);
        int pointer = 1;
        while (pointer <= rings) {
            built += survivalBuildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                baseHorizontalOffSet,
                baseVerticalOffSet,
                baseDepthOffSet - pointer * 8,
                elementBudget,
                env,
                false,
                true);
            pointer++;
            if (built >= 0) return built;
        }

        built += survivalBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - pointer * 8,
            elementBudget,
            env,
            false,
            true);

        return built;

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

        // init the pointer, also the Properties.
        this.amountRings = 1;

        // check the Top layer.
        if (!checkPiece(STRUCTURE_PIECE_MAIN, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet, errors)) {
            return;
        }

        // check middle layer, increase speedBoost per layer.
        // 8 blocks depth per layer
        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - this.amountRings * 8,
            errors)) {
            this.amountRings++;
            if (amountRings > 15) {
                return;
            }
        }

        errors.clear();

        // check the end layer
        if (!checkPiece(
            STRUCTURE_PIECE_END,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - this.amountRings * 8,
            errors)) {
            return;
        }

        // basic two layers: the top and the end, means amountRings default is 2 .
        this.amountRings++;

        // calculate parameters
        enablePerfectOverclock = amountRings >= RingsAmount_EnablePerfectOverclock_MiracleTop;
        speedBonus = 1.0F / (amountRings * SpeedUpMultiplier_PerRing_MiracleTop);
        maxParallel = amountRings * Parallel_PerRing_MiracleTop;

    }
    // endregion

    // region Processing Logic
    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_COMPRESSING, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_BENDING };

    public int amountRings = 1;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return machineMode == 0 ? GTCMRecipe.MiracleTopRecipes : GTCMRecipe.QuantumInversionRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTCMRecipe.MiracleTopRecipes, GTCMRecipe.QuantumInversionRecipes);
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Miracle Top Circuit Assembler
         * 1 - Gravitation Inversion
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    // @Override
    // public void setMachineModeIcons() {
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_COMPRESSING);
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_BENDING);
    // }
    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("MiracleTop.modeMsg." + machineMode);
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = "Speed up multiplier: " + this.amountRings * SpeedUpMultiplier_PerRing_MiracleTop;
        return ret;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setInteger("amountRings", amountRings);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getByte("mode");
        amountRings = aNBT.getInteger("amountRings");
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {

            if (aActive) {
                return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][12] };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_MiracleTop_MachineType
        // # Circuit Assembler/Gravitation Breaker
        // #zh_CN 电路组装机/引力驱使核心
        tt.addMachineType(TextEnums.tr("Tooltip_MiracleTop_MachineType"))
            // #tr Tooltip_MiracleTop_00
            // # Controller block for the Miracle Top.
            // #zh_CN 奇迹顶点的控制器方块.
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_00"))
            // #tr Tooltip_MiracleTop_01
            // # {\LIGHT_PURPLE}I never think about the future because it will come sooner or later.
            // #zh_CN {\LIGHT_PURPLE}我从不思考未来，因为未来迟早会来.
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_01"))
            // #tr Tooltip_MiracleTop_02
            // # For absolute precision and efficiency, please abandon traditional manufacturing methods.
            // #zh_CN 为了绝对的精准和高效，请放弃传统的制造思路.
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_02"))
            // #tr Tooltip_MiracleTop_03
            // # The machine consists of a ring section and a conveying section.
            // #zh_CN 整个机器由环部分和传输部分组成.
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_03"))
            // #tr Tooltip_MiracleTop_04
            // # The number of rings is variable:{\SPACE}{\SPACE}Maximum {\GOLD}16{\GRAY} rings, Minimum {\GOLD}2{\GRAY} rings(the first and the last).
            // #zh_CN 环的数量是可变的:{\SPACE}{\SPACE}最多{\GOLD}16{\GRAY}环, 最少{\GOLD}2{\GRAY}环(第一个环和最后一个环).
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_04"))
            // #tr Tooltip_MiracleTop_05
            // # Total speed multiplier is equal to {\RED}400%{\GRAY} x num of rings.
            // #zh_CN 速度倍率 = 环数 x {\RED}400%{\GRAY}.
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_05"))
            // #tr Tooltip_MiracleTop_06
            // # Enable Perfect overclock when num of rings >= {\GOLD}8{\GRAY}.
            // #zh_CN 环数大于等于{\RED}8{\GRAY}时开启无损超频.
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_06"))
            // #tr Tooltip_MiracleTop_07
            // # {\AQUA}128x{\GRAY} Parallel per Ring.
            // #zh_CN 每环 {\AQUA}128x{\GRAY} 并行.
            .addInfo(TextEnums.tr("Tooltip_MiracleTop_07"))
            .addController(TextLocalization.textFrontCenter)
            // #tr textMiracleTopHatchLocation
            // # Outermost 12 blocks on the ring (outermost 3 on each side).
            // #zh_CN 环上最外侧的12个方块(每侧最外边3个).
            .addInputHatch(TextEnums.tr("textMiracleTopHatchLocation"), 2)
            .addOutputHatch(TextEnums.tr("textMiracleTopHatchLocation"), 2)
            .addInputBus(TextEnums.tr("textMiracleTopHatchLocation"), 2)
            .addOutputBus(TextEnums.tr("textMiracleTopHatchLocation"), 2)
            .addEnergyHatch(TextEnums.tr("textMiracleTopHatchLocation"), 2)
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
