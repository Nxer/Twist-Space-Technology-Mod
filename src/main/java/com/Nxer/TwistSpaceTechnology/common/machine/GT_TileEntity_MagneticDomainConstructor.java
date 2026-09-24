package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_PerRing_MagneticDomainConstructor;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_MagneticDomainConstructor;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
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
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;

@SkipGenerateDescription
public class GT_TileEntity_MagneticDomainConstructor
    extends GTCM_MultiMachineBase<GT_TileEntity_MagneticDomainConstructor> {

    // region Class Constructor
    public GT_TileEntity_MagneticDomainConstructor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_MagneticDomainConstructor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MagneticDomainConstructor(this.mName);
    }
    // endregion

    // region Structure
    private final int baseHorizontalOffSet = 7;
    private final int baseVerticalOffSet = 15;
    private final int baseDepthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainMagneticDomainConstructor";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleMagneticDomainConstructor";
    private static final String STRUCTURE_PIECE_END = "endMagneticDomainConstructor";
    private static IStructureDefinition<GT_TileEntity_MagneticDomainConstructor> STRUCTURE_DEFINITION = null;

    // spotless:off
    /**
     * The first piece of Structure
     */
    private final String[][] shapeMain = new String[][]{
        {"               ","               ","      BBB      ","               "},
        {"               ","      BBB      ","    BBAAABB    ","      BBB      "},
        {"               ","    BB   BB    ","   BAAGGGAAB   ","    BB   BB    "},
        {"               ","   B       B   ","  BAGG   GGAB  ","   B       B   "},
        {"               ","  B         B  "," BAG       GAB ","  B         B  "},
        {"      DDD      ","  B         B  "," BAG       GAB ","  B         B  "},
        {"     DEEED     "," B     C     B ","BAG    C    GAB"," B     C     B "},
        {"     DEEED     "," B    CCC    B ","BAG   CCC   GAB"," B    CCC    B "},
        {"     DEEED     "," B     C     B ","BAG    C    GAB"," B     C     B "},
        {"      DDD      ","  B         B  "," BAG       GAB ","  B         B  "},
        {"      FDF      ","  B         B  "," BAG       GAB ","  B         B  "},
        {"      FDF      ","   B       B   ","  BAGG   GGAB  ","   B       B   "},
        {"      FDF      ","   FBB   BBF   ","   BAAGGGAAB   ","   FBB   BBF   "},
        {"      FDF      ","  F   BBB   F  ","    BBAAABB    ","  F   BBB   F  "},
        {"      FDF      "," F    DDD    F ","      BBB      "," F    DDD    F "},
        {"      D~D      ","F     DDD     F","DDDDDDDDDDDDDDD","F     DDD     F"},
        {"     DDDDD     ","DDDDDDDDDDDDDDD","DDDDDDDDDDDDDDD","DDDDDDDDDDDDDDD"}
    };

    /**
     * The middle of Structure
     */
    private final String[][] shapeMiddle = new String[][]{
        {"               ","               ","      BBB      ","               "},
        {"               ","      BBB      ","    BBAAABB    ","      BBB      "},
        {"               ","    BB   BB    ","   BAAGGGAAB   ","    BB   BB    "},
        {"               ","   B       B   ","  BAGG   GGAB  ","   B       B   "},
        {"               ","  B         B  "," BAG       GAB ","  B         B  "},
        {"               ","  B         B  "," BAG       GAB ","  B         B  "},
        {"       C       "," B     C     B ","BAG    C    GAB"," B     C     B "},
        {"      CCC      "," B    CCC    B ","BAG   CCC   GAB"," B    CCC    B "},
        {"       C       "," B     C     B ","BAG    C    GAB"," B     C     B "},
        {"               ","  B         B  "," BAG       GAB ","  B         B  "},
        {"               ","  B         B  "," BAG       GAB ","  B         B  "},
        {"               ","   B       B   ","  BAGG   GGAB  ","   B       B   "},
        {"               ","   FBB   BBF   ","   BAAGGGAAB   ","   FBB   BBF   "},
        {"               ","  F   BBB   F  ","    BBAAABB    ","  F   BBB   F  "},
        {"      DDD      "," F    DDD    F ","      BBB      "," F    DDD    F "},
        {"      DDD      ","F     DDD     F","DDDDDDDDDDDDDDD","F     DDD     F"},
        {"     DDDDD     ","DDDDDDDDDDDDDDD","DDDDDDDDDDDDDDD","DDDDDDDDDDDDDDD"}
    };

    /**
     * The end of Structure
     */
    private final String[][] shapeEnd = new String[][]{
        {"               "},
        {"               "},
        {"               "},
        {"               "},
        {"               "},
        {"      DDD      "},
        {"     DOOOD     "},
        {"     DOOOD     "},
        {"     DOOOD     "},
        {"      DDD      "},
        {"      FDF      "},
        {"      FDF      "},
        {"      FDF      "},
        {"      FDF      "},
        {"      FDF      "},
        {"      DDD      "},
        {"     DDDDD     "}
    };
    // spotless:on

    @Override
    public IStructureDefinition<GT_TileEntity_MagneticDomainConstructor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MagneticDomainConstructor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addShape(STRUCTURE_PIECE_MIDDLE, transpose(shapeMiddle))
                .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
                .addElement('A', ofBlock(compactFusionCoil, 0))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 8))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'D', // Energy Hatch, Maintenance
                    HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                        .hint(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 10))
                .addElement(
                    'E',
                    HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                        .atLeast(InputBus, InputHatch)
                        .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                        .hint(2)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'O',
                    HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                        .atLeast(OutputBus, OutputHatch)
                        .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                        .hint(3)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(7))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement('F', ofFrame(Materials.NaquadahAlloy))
                .addElement('G', ofFrame(Materials.TengamAttuned))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

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

        built[0] = survivalBuildPiece(
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
                built[pointer] = survivalBuildPiece(
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

        built[ring + 1] = survivalBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - ring * 4,
            elementBudget,
            env,
            false,
            true);

        return TSTUtils.multiBuildPiece(built);

    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();

        this.rings = 1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet, errors)) {
            return;
        }

        while (checkPiece(
            STRUCTURE_PIECE_MIDDLE,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - this.rings * 4,
            errors)) {

            this.rings++;
        }

        errors.clear();

        if (!checkPiece(
            STRUCTURE_PIECE_END,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - this.rings * 4,
            errors)) {

            return;
        }

        maxParallel = (int) Math.min((long) rings * Parallel_PerRing_MagneticDomainConstructor, Integer.MAX_VALUE);
        speedBonus = (float) Math.pow(SpeedBonus_MultiplyPerTier_MagneticDomainConstructor, getTotalPowerTier());

    }
    // endregion

    // region Processing Logic
    private int rings = 1;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_POLARIZER };

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
    public int totalMachineMode() {
        /*
         * 0 - Separator
         * 1 - Polarizer
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // #tr tst.common.machine.MagneticDomainConstructor.mode.0
        // # Mode: Electromagnetic Separator
        // #zh_CN 电磁离析机模式

        // #tr tst.common.machine.MagneticDomainConstructor.mode.1
        // # Mode: Electromagnetic Polarizer
        // #zh_CN 磁化机模式
        return StatCollector.translateToLocal("tst.common.machine.MagneticDomainConstructor.mode." + machineMode);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
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
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Rings: " + EnumChatFormatting.GOLD + this.rings;
        return ret;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setInteger("rings", rings);

    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getByte("mode");
        rings = aNBT.getInteger("rings");
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            sideDirection,
            facingDirection,
            active,
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
            OVERLAY_FRONT_ASSEMBLY_LINE,
            OVERLAY_FRONT_ASSEMBLY_LINE_GLOW,
            OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE,
            OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MagneticDomainConstructor.tooltip.machine_type
        // # Electromagnetic Separator | Electromagnetic Polarizer
        // #zh_CN 电磁离析机 | 磁化机
        tt.addMachineType(TSTUtils.tr("tst.common.machine.MagneticDomainConstructor.tooltip.machine_type"))
            // #tr tst.common.machine.MagneticDomainConstructor.tooltip.info.01
            // # Controller block for the Magnetic Domain Constructor
            // #zh_CN 磁畴构建器的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticDomainConstructor.tooltip.info.01"))
            // #tr tst.common.machine.MagneticDomainConstructor.tooltip.info.02
            // # {\DARK_GRAY}Don't give up your imagination.
            // #zh_CN {\DARK_GRAY}不要放弃你的幻想.
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticDomainConstructor.tooltip.info.02"))
            // #tr tst.common.machine.MagneticDomainConstructor.tooltip.info.03
            // # Controlling the magnetic domains inside the crystal, yes that's it.
            // #zh_CN 操控晶体内部的磁畴子, 就是这样.
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticDomainConstructor.tooltip.info.03"))
            // #tr tst.common.machine.MagneticDomainConstructor.tooltip.info.04
            // # {\AQUA}64x{\GRAY} Parallel per Ring.(Don't use a lot of blueprints when first scanning.)
            // #zh_CN 每环增加{\AQUA}64x{\GRAY}并行.(不要一开始就用很多{\BLUE}蓝{\AQUA}图{\GRAY}去扫描.)
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticDomainConstructor.tooltip.info.04"))
            // #tr tst.common.machine.MagneticDomainConstructor.tooltip.info.05
            // # Additional {\RED}25%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
            // #zh_CN 电压每提高1级, 额外降低{\RED}25%{\GRAY}配方耗时, 叠乘计算.
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticDomainConstructor.tooltip.info.05"))
            .addInfo(TSTSharedLocalization.MachineTooltip.textScrewdriverChangeMode)
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .addOutputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 3)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint, 3)
            .addEnergyHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
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
