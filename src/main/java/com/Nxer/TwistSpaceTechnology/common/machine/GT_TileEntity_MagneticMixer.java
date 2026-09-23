package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MultiplyPerTier_MagneticMixer;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;

@SkipGenerateDescription
public class GT_TileEntity_MagneticMixer extends GTCM_MultiMachineBase<GT_TileEntity_MagneticMixer> {

    // region Class Constructor
    public GT_TileEntity_MagneticMixer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_MagneticMixer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MagneticMixer(this.mName);
    }
    // endregion

    // region Structure
    private final int horizontalOffSet = 9;
    private final int verticalOffSet = 19;
    private final int depthOffSet = 0;

    private static IStructureDefinition<GT_TileEntity_MagneticMixer> STRUCTURE_DEFINITION = null;

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasings2, 8, ...);
     * B -> ofBlock...(gt.blockcasings8, 2, ...); // IO Hatch
     * C -> ofBlock...(gt.blockcasings8, 3, ...); // Energy Hatch
     * D -> ofBlock...(gt.blockcasings8, 10, ...); // Maintenance Hatch
     * E -> ofBlock...(gtplusplus.blockcasings.3, 11, ...);
     */

    private static final String STRUCTURE_PIECE_MAIN = "main";

    // spotless:off
    private final String[][] shape = new String[][] {
        { "                   ", "                   ", "        CCC        ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "        CCC        ", "                   ", "                   " },
        { "                   ", "                   ", "      CCCCCCC      ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "      CCCCCCC      ", "                   ", "                   " },
        { "                   ", "        DDD        ", "     CCCCCCCCC     ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "     CCCCCCCCC     ", "        DDD        ", "                   " },
        { "                   ", "      DD   DD      ", "    CCCCCCCCCCC    ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "    CCCCCCCCCCC    ", "      DD   DD      ", "                   " },
        { "                   ", "     D  EEE  D     ", "   CCCCCCCCCCCCC   ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "   CCCCCCCCCCCCC   ", "     D  EEE  D     ", "                   " },
        { "                   ", "     D E   E D     ", "   CCCCCCCCCCCCC   ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "   CCCCCCCCCCCCC   ", "     D E   E D     ", "                   " },
        { "                   ", "    D E       D    ", "  CCCCCCCCCCCCCCC  ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "  CCCCCCCCCCCCCCC  ", "    D       E D    ", "                   " },
        { "                   ", "    D E  EEEE D    ", "  CCCCCCCCCCCCCCC  ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "   BBBB  B  BBBB   ", "  CCCCCCCCCCCCCCC  ", "    D EEEE  E D    ", "                   " },
        { "                   ", "    D E    E  D    ", "  CCCCCCCCCCCCCCC  ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "   B           B   ", "  CCCCCCCCCCCCCCC  ", "    D  E    E D    ", "                   " },
        { "                   ", "     D E   E D     ", "   CCCCCCCCCCCCC   ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "    B  B   B  B    ", "   CCCCCCCCCCCCC   ", "     D E   E D     ", "                   " },
        { "                   ", "     D  EEE  D     ", "   CCCCCCCCCCCCC   ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "    B B  B  B B    ", "   CCCCCCCCCCCCC   ", "     D  EEE  D     ", "                   " },
        { "                   ", "      DD   DD      ", "    CCCCCCCCCCC    ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "     B   B   B     ", "    CCCCCCCCCCC    ", "      DD   DD      ", "                   " },
        { "                   ", "        DDD        ", "     CCCCCCCCC     ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "      BB B BB      ", "     CCCCCCCCC     ", "        DDD        ", "                   " },
        { "                   ", "                   ", "      CCCCCCC      ", "        BBB        ","        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "        BBB        ", "      CCCCCCC      ", "                   ", "                   " },
        { "   C           C   ", "   C           C   ", "   C    CCC    C   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "   C    CCC    C   ", "   C           C   ", "   C           C   " },
        { "   C           C   ", "   C           C   ", "   C           C   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "   C           C   ", "   C           C   ", "   C           C   " },
        { "  CDC         CDC  ", "  CDC         CDC  ", "  CDC         CDC  ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "   D           D   ", "  CDC         CDC  ", "  CDC         CDC  ", "  CDC         CDC  " },
        { "  CDDCC     CCDDC  ", "  CDDCC     CCDDC  ", "  CDDCC     CCDDC  ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "   DA         AD   ", "  CDDCC     CCDDC  ", "  CDDCC     CCDDC  ", "  CDDCC     CCDDC  " },
        { " CDDDDDCCCCCDDDDDC ", " CDDDDDCCCCCDDDDDC ", " CDDDDDCCCCCDDDDDC ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", "  DDDAD     DADDD  ", " CDDDDDCCCCCDDDDDC ", " CDDDDDCCCCCDDDDDC ", " CDDDDDCCCCCDDDDDC " },
        { " CDDDDDDD~DDDDDDDC ", " CDDDDDDDDDDDDDDDC ", " CDDDDDDDDDDDDDDDC ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", "  DDDDDDDDDDDDDDD  ", " CDDDDDDDDDDDDDDDC ", " CDDDDDDDDDDDDDDDC ", " CDDDDDDDDDDDDDDDC " },
        { "CCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCC", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", " CCCCCCCCCCCCCCCCC ", "CCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCC" } };
    // spotless:on

    @Override
    public IStructureDefinition<GT_TileEntity_MagneticMixer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MagneticMixer>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings2, 8))
                .addElement(
                    'B',
                    HatchElementBuilder.<GT_TileEntity_MagneticMixer>builder()
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                        .adder(GT_TileEntity_MagneticMixer::addToMachineList)
                        .hint(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 2))
                .addElement(
                    'C',
                    HatchElementBuilder.<GT_TileEntity_MagneticMixer>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(GT_TileEntity_MagneticMixer::addToMachineList)
                        .hint(2)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 3))
                .addElement(
                    'D',
                    HatchElementBuilder.<GT_TileEntity_MagneticMixer>builder()
                        .atLeast(Maintenance)
                        .adder(GT_TileEntity_MagneticMixer::addToMachineList)
                        .hint(3)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 10))
                .addElement('E', ofBlock(ModBlocks.blockCasings3Misc, 11))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
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
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) return;
        speedBonus = (float) Math.pow(SpeedBonus_MultiplyPerTier_MagneticMixer, getTotalPowerTier());
    }
    // endregion

    // region Processing Logic

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.mixerNonCellRecipes;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    public int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
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
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Parallel: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Recipe Time multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        return ret;
    }

    // endregion

    // region Textures

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

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MagneticMixer.tooltip.machine_type
        // # Mixer
        // #zh_CN 搅拌机
        tt.addMachineType(TSTUtils.tr("tst.common.machine.MagneticMixer.tooltip.machine_type"))
            // #tr tst.common.machine.MagneticMixer.tooltip.info.01
            // # Controller block for the "Mini" Magnetic Mixer
            // #zh_CN "小型"磁力搅拌机的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticMixer.tooltip.info.01"))
            // #tr tst.common.machine.MagneticMixer.tooltip.info.02
            // # {\RED}Watch out for the Bumps !
            // #zh_CN {\RED}我叫磁力棒！
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticMixer.tooltip.info.02"))
            // #tr tst.common.machine.MagneticMixer.tooltip.info.03
            // # Looks more like a tumble washing machine.
            // #zh_CN 看起来更像一个滚筒洗衣机.
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticMixer.tooltip.info.03"))
            // #tr tst.common.machine.MagneticMixer.tooltip.info.04
            // # Has parallel equivalent to Perfect Overclock.
            // #zh_CN 拥有与无损超频等效的并行(但有损超频).
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticMixer.tooltip.info.04"))
            // #tr tst.common.machine.MagneticMixer.tooltip.info.05
            // # Additional {\RED}20%{\GRAY} reduction in time per Voltage Tier, multiplication calculus.
            // #zh_CN 电压每提高1级, 额外降低{\RED}20%{\GRAY}配方耗时, 叠乘计算.
            .addInfo(TSTUtils.tr("tst.common.machine.MagneticMixer.tooltip.info.05"))
            .beginStructureBlock(19, 19, 21, false)
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addEnergyHatch(TSTSharedLocalization.Structure.textUseBlueprint, 2)
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
