package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;
import gtPlusPlus.core.block.ModBlocks;

public class GT_TileEntity_MagneticMixer
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_MagneticMixer>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MagneticMixer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MagneticMixer(String aName) {
        super(aName);
    }
    // endregion

    // region Processing Logic
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

    public int getMaxParallelRecipes() {
        return (int) Math
            .min(ValueEnum.MAX_PARALLEL_LIMIT, Math.pow(2, GT_Utility.getTier(this.getAverageInputVoltage())));
    }

    public float getSpeedBonus() {
        return (float) Math.pow(0.8, GT_Utility.getTier(this.getAverageInputVoltage()));
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // endregion

    // region Structure

    // spotless:off
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }

    private final int horizontalOffSet = 9;
    private final int verticalOffSet = 19;
    private final int depthOffSet = 0;

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasings2, 8, ...);
     * B -> ofBlock...(gt.blockcasings8, 2, ...); // IO Hatch
     * C -> ofBlock...(gt.blockcasings8, 3, ...); // Energy Hatch
     * D -> ofBlock...(gt.blockcasings8, 10, ...); // Maintenance Hatch
     * E -> ofBlock...(gtplusplus.blockcasings.3, 11, ...);
     */
    @Override
    public IStructureDefinition<GT_TileEntity_MagneticMixer> getStructureDefinition() {
        return StructureDefinition.<GT_TileEntity_MagneticMixer>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addElement('A', ofBlock(GregTech_API.sBlockCasings2, 8))
            .addElement(
                'B',
                GT_HatchElementBuilder.<GT_TileEntity_MagneticMixer>builder()
                    .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                    .adder(GT_TileEntity_MagneticMixer::addToMachineList)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(2))
                    .buildAndChain(GregTech_API.sBlockCasings8, 2))
            .addElement(
                'C',
                GT_HatchElementBuilder.<GT_TileEntity_MagneticMixer>builder()
                    .atLeast(Energy.or(ExoticEnergy))
                    .adder(GT_TileEntity_MagneticMixer::addToMachineList)
                    .dot(2)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(3))
                    .buildAndChain(GregTech_API.sBlockCasings8, 3))
            .addElement(
                'D',
                GT_HatchElementBuilder.<GT_TileEntity_MagneticMixer>builder()
                    .atLeast(Maintenance)
                    .adder(GT_TileEntity_MagneticMixer::addToMachineList)
                    .dot(3)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10))
                    .buildAndChain(GregTech_API.sBlockCasings8, 10))
            .addElement('E', ofBlock(ModBlocks.blockCasings3Misc, 11))
            .build();
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
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
    // endregion

    // region Overrides

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 1] = EnumChatFormatting.AQUA + "Parallel: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        ret[origin.length] = EnumChatFormatting.AQUA + "Recipe Time multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        return ret;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_MagneticMixer_MachineType)
            .addInfo(TextLocalization.Tooltip_MagneticMixer_00)
            .addInfo(TextLocalization.Tooltip_MagneticMixer_01)
            .addInfo(TextLocalization.Tooltip_MagneticMixer_02)
            .addInfo(TextLocalization.Tooltip_MagneticMixer_03)
            .addInfo(TextLocalization.Tooltip_MagneticMixer_04)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(19, 19, 21, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addMaintenanceHatch(TextLocalization.textUseBlueprint, 3)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
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
        return new GT_TileEntity_MagneticMixer(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
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
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)),
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
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10)) };
    }

    // endregion
}
