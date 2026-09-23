package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_PerPiece_Silksong;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedBonus_MultiplyPerVoltageTier_Silksong;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedMultiplier_CoilTier_Silksong;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
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

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.WirelessEnergyMultiMachineBase;
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
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings8;

@SkipGenerateDescription
public class GT_TileEntity_Silksong extends WirelessEnergyMultiMachineBase<GT_TileEntity_Silksong> {

    // region Class Constructor
    public GT_TileEntity_Silksong(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_Silksong(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_Silksong(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "mainSilksong";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleSilksong";
    private static final String STRUCTURE_PIECE_END = "endSilksong";
    private final int horizontalOffSet = 3;
    private final int verticalOffSet = 5;
    private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_Silksong> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] shapeMain = new String[][]{
        {"       ","       "},
        {"       ","  III  "},
        {"       ","  III  "},
        {"       ","  III  "},
        {"  DDD  ","  DDD  "},
        {"  D~D  ","  DDD  "},
        {"  DDD  ","  DDD  "}
    };

    private final String[][] shapeMiddle = new String[][]{
        {"       ","  AAA  "},
        {"  CCC  "," ACCCA "},
        {"  CBC  "," ACBCA "},
        {"  CCC  "," ACCCA "},
        {"       "," HAAAH "},
        {"       "," H   H "},
        {"DDDDDDD","DDDDDDD"}
    };

    private final String[][] shapeEnd = new String[][]{
        {"  EEE  ","  EEE  ","  EEE  ","       ","       "},
        {" EFFFE "," EFFFE "," EFFFE ","  GGG  ","  GGG  "},
        {" EFBFE "," EFBFE "," EFBFE ","  GBG  ","  GGG  "},
        {" EFFFE "," EFFFE "," EFFFE ","  GGG  ","  GGG  "},
        {"  EEE  ","  EEE  ","  EEE  ","  DDD  ","       "},
        {"       ","       ","       ","  DDD  ","  DDD  "},
        {"DDDDDDD","  DDD  ","  DDD  ","  DDD  ","  DDD  "}
    };
    // spotless:on

    @Override
    public IStructureDefinition<GT_TileEntity_Silksong> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_Silksong>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
                .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 11))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 15))
                .addElement(
                    'C',
                    withChannel(
                        "coil",
                        ofCoil(GT_TileEntity_Silksong::setCoilLevel, GT_TileEntity_Silksong::getCoilLevel)))
                .addElement(
                    'D',
                    HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_Silksong::addToMachineList)
                        .hint(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 2))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement('F', ofBlock(pressureResistantWalls, 0))
                .addElement(
                    'G',
                    HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                        .atLeast(OutputBus, OutputHatch)
                        .adder(GT_TileEntity_Silksong::addToMachineList)
                        .hint(2)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement('H', ofFrame(Materials.Neutronium))
                .addElement(
                    'I',
                    HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                        .atLeast(InputBus, InputHatch)
                        .adder(GT_TileEntity_Silksong::addToMachineList)
                        .hint(3)
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

        built[0] = survivalBuildPiece(
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
            built[pointer] = survivalBuildPiece(
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

        built[built.length - 1] = survivalBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet - piece * 2 - 2,
            elementBudget,
            env,
            false,
            true);

        return TSTUtils.multiBuildPiece(built);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        coilLevel = HeatingCoilLevel.None;

        this.piece = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) {
            return;
        }

        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet - piece * 2 - 2,
            errors)) {
            this.piece++;
        }

        if (piece < 1) return;

        errors.clear();

        if (!checkPiece(STRUCTURE_PIECE_END, horizontalOffSet, verticalOffSet, depthOffSet - piece * 2 - 2, errors)) {
            return;
        }

        // parallel = piece * coilTier * 32
        maxParallel = (int) Math.min((long) piece * getCoilTier() * Parallel_PerPiece_Silksong, Integer.MAX_VALUE);

        // speed bonus = 0.85^voltageTier / (coilTier * 1)
        speedBonus = (float) (Math.pow(SpeedBonus_MultiplyPerVoltageTier_Silksong, getTotalPowerTier())
            / (getCoilTier() * SpeedMultiplier_CoilTier_Silksong));

        // allow machine use perfect overclock when piece reach 94, and enter wireless mode if no energy hatch
        if (piece >= 94) {
            enablePerfectOverclock = true;
            wirelessMode = mEnergyHatches.isEmpty() && mExoticEnergyHatches.isEmpty();
        } else {
            enablePerfectOverclock = false;
            wirelessMode = false;
        }

    }
    // endregion

    // region Processing Logic
    private int piece = 1;
    private HeatingCoilLevel coilLevel;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.wiremillRecipes;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    public int getCoilTier() {
        return TSTUtils.getVoltageForCoil(coilLevel);
    }

    @Override
    public int getWirelessModeProcessingTime() {
        return 188;
    }

    // endregion

    // region NBT

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

    // endregion

    // region Textures

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

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.Silksong.tooltip.machine_type
        // # Wiremill
        // #zh_CN 线材轧机
        tt.addMachineType(TSTUtils.tr("tst.common.machine.Silksong.tooltip.machine_type"))
            // #tr tst.common.machine.Silksong.tooltip.info.01
            // # Controller block for the Silksong
            // #zh_CN 丝之歌的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.Silksong.tooltip.info.01"))
            // #tr tst.common.machine.Silksong.tooltip.info.02
            // # {\WHITE}Maybe dreams aren't such a good thing ......
            // #zh_CN {\WHITE}也许梦想并不是那么好的东西 ......
            .addInfo(TSTUtils.tr("tst.common.machine.Silksong.tooltip.info.02"))
            // #tr tst.common.machine.Silksong.tooltip.info.03
            // # Endless cables spew from this machine.
            // #zh_CN 无穷无尽的导线从这里喷薄而出.
            .addInfo(TSTUtils.tr("tst.common.machine.Silksong.tooltip.info.03"))
            // #tr tst.common.machine.Silksong.tooltip.info.04
            // # Parallel = {\AQUA}32 × piece × coil tier{\GRAY}.
            // #zh_CN 每16个线圈为1层. 并行数 = 层数 × 线圈等级
            .addInfo(TSTUtils.tr("tst.common.machine.Silksong.tooltip.info.04"))
            // #tr tst.common.machine.Silksong.tooltip.info.05
            // # Each level of coil increases the speed by {\RED}100%{\GRAY}.
            // #zh_CN 线圈每提高1级额外加速{\RED}100%{\GRAY}.
            .addInfo(TSTUtils.tr("tst.common.machine.Silksong.tooltip.info.05"))
            // #tr tst.common.machine.Silksong.tooltip.info.06
            // # Additional {\RED}15%{\GRAY} reduction in time per Coil Tier, multiplication calculus.
            // #zh_CN 电压每提高1级, 额外降低{\RED}15%{\GRAY}配方耗时, 叠乘计算.
            .addInfo(TSTUtils.tr("tst.common.machine.Silksong.tooltip.info.06"))
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 3)
            .addOutputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 3)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint, 3)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .addEnergyHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            // #tr tst.common.machine.Silksong.tooltip.structure.01
            // # {\BLACK}Something special when piece reaches 94.
            // #zh_CN {\BLACK}层数达到94层后有一些特别的东西.
            .addStructureInfo(TSTUtils.tr("tst.common.machine.Silksong.tooltip.structure.01"))
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
