package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static goodgenerator.loader.Loaders.compactFusionCoil;
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
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
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
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;

public class GT_TileEntity_MagneticDomainConstructor
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_MagneticDomainConstructor>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MagneticDomainConstructor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MagneticDomainConstructor(String aName) {
        super(aName);
    }
    // endregion

    // region Processing Logic
    private byte mode = 0;
    private int rings = 1;

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
        return this.rings * 8;
    }

    public float getSpeedBonus() {
        return (float) Math.pow(0.90, GT_Utility.getTier(this.getAverageInputVoltage()));
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        switch (mode) {
            case 1:
                return GT_Recipe.GT_Recipe_Map.sPolarizerRecipes;
            default:
                return GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes;
        }
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);

            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("MagneticDomainConstructor.modeMsg." + this.mode));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;

        int built = 0;

        built += survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet,
            elementBudget,
            source,
            actor,
            false,
            true);

        int ring = stackSize.stackSize;

        if (ring > 1) {
            int pointer = 1;
            while (pointer < ring) {
                built += survivialBuildPiece(
                    STRUCTURE_PIECE_MIDDLE,
                    stackSize,
                    baseHorizontalOffSet,
                    baseVerticalOffSet,
                    baseDepthOffSet - pointer * 4,
                    elementBudget,
                    source,
                    actor,
                    false,
                    true);
                pointer++;
            }
        }

        built += survivialBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet - ring * 4,
            elementBudget,
            source,
            actor,
            false,
            true);

        return built;

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
        return StructureDefinition.<GT_TileEntity_MagneticDomainConstructor>builder()
            .addShape(STRUCTURE_PIECE_MAIN, shapeMain)
            .addShape(STRUCTURE_PIECE_MIDDLE, shapeMiddle)
            .addShape(STRUCTURE_PIECE_END, shapeEnd)
            .addElement('A', ofBlock(compactFusionCoil, 0))
            .addElement('B', ofBlock(GregTech_API.sBlockCasings2, 8))
            .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 7))
            .addElement(
                'D', // Energy Hatch, Maintenance
                GT_HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                    .atLeast(Energy.or(ExoticEnergy), Maintenance)
                    .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10))
                    .buildAndChain(GregTech_API.sBlockCasings8, 10))
            .addElement(
                'E',
                GT_HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                    .atLeast(InputBus, InputHatch)
                    .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                    .dot(2)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(7))
                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
            .addElement(
                'O',
                GT_HatchElementBuilder.<GT_TileEntity_MagneticDomainConstructor>builder()
                    .atLeast(OutputBus, OutputHatch)
                    .adder(GT_TileEntity_MagneticDomainConstructor::addToMachineList)
                    .dot(3)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(7))
                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
            .addElement('F', ofFrame(Materials.NaquadahAlloy))
            .addElement('G', ofFrame(Materials.TengamAttuned))
            .build();
    }

    private final int baseHorizontalOffSet = 7;
    private final int baseVerticalOffSet = 15;
    private final int baseDepthOffSet = 0;

    private static final String STRUCTURE_PIECE_MAIN = "mainMagneticDomainConstructor";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleMagneticDomainConstructor";
    private static final String STRUCTURE_PIECE_END = "endMagneticDomainConstructor";

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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
            .addMaintenanceHatch(TextLocalization.textUseBlueprint, 1)
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

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", mode);
        aNBT.setInteger("rings", rings);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = aNBT.getByte("mode");
        rings = aNBT.getInteger("rings");
    }
    // endregion

}
