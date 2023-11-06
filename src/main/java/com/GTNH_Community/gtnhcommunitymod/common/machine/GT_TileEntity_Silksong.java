package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.BLUE_PRINT_INFO;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.ModName;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.StructureTooComplex;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_Silksong_00;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_Silksong_01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_Silksong_02;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_Silksong_03;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_Silksong_04;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_Silksong_MachineType;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.pressureResistantWalls;
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
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
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
import gregtech.common.blocks.GT_Block_Casings1;
import gregtech.common.blocks.GT_Block_Casings8;

public class GT_TileEntity_Silksong extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_Silksong>
    implements IConstructable, ISurvivalConstructable {

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

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private int getMaxParallelRecipes() {
        return this.piece * 8;
    }

    private float getSpeedBonus() {
        return (float) Math.pow(0.9, this.coilLevel.getLevel());
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sWiremillRecipes;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
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

        return true;
    }
    // endregion

    // region Structure
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int built = 0;

        built += survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            source,
            actor,
            false,
            true);

        int piece = stackSize.stackSize;

        for (int pointer = 1; pointer <= piece; pointer++) {
            built += survivialBuildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                horizontalOffSet,
                verticalOffSet,
                depthOffSet - pointer * 2,
                elementBudget,
                source,
                actor,
                false,
                true);
        }

        built += survivialBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet - piece * 2 - 2,
            elementBudget,
            source,
            actor,
            false,
            true);

        return built;
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainSilksong";
    private static final String STRUCTURE_PIECE_MIDDLE = "middleSilksong";
    private static final String STRUCTURE_PIECE_END = "endSilksong";
    private final int horizontalOffSet = 3;
    private final int verticalOffSet = 5;
    private final int depthOffSet = 0;

    @Override
    public IStructureDefinition<GT_TileEntity_Silksong> getStructureDefinition() {
        return StructureDefinition.<GT_TileEntity_Silksong>builder()
            .addShape(STRUCTURE_PIECE_MAIN, shapeMain)
            .addShape(STRUCTURE_PIECE_MIDDLE, shapeMiddle)
            .addShape(STRUCTURE_PIECE_END, shapeEnd)
            .addElement('A', ofBlock(GregTech_API.sBlockCasings1, 11))
            .addElement('B', ofBlock(GregTech_API.sBlockCasings2, 15))
            .addElement(
                'C',
                withChannel("coil", ofCoil(GT_TileEntity_Silksong::setCoilLevel, GT_TileEntity_Silksong::getCoilLevel)))
            .addElement(
                'D',
                GT_HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                    .atLeast(Maintenance, Energy.or(ExoticEnergy))
                    .adder(GT_TileEntity_Silksong::addToMachineList)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(2))
                    .buildAndChain(GregTech_API.sBlockCasings8, 2))
            .addElement('E', ofBlock(GregTech_API.sBlockCasings8, 7))
            .addElement('F', ofBlock(pressureResistantWalls, 0))
            .addElement(
                'G',
                GT_HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                    .atLeast(OutputBus, OutputHatch)
                    .adder(GT_TileEntity_Silksong::addToMachineList)
                    .dot(2)
                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(7))
                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
            .addElement('H', ofFrame(Materials.Neutronium))
            .addElement(
                'I',
                GT_HatchElementBuilder.<GT_TileEntity_Silksong>builder()
                    .atLeast(InputBus, InputHatch)
                    .adder(GT_TileEntity_Silksong::addToMachineList)
                    .dot(3)
                    .casingIndex(((GT_Block_Casings1) GregTech_API.sBlockCasings1).getTextureIndex(11))
                    .buildAndChain(GregTech_API.sBlockCasings1, 11))
            .build();
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

    // endregion

    // region Overrides

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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(Tooltip_Silksong_MachineType)
            .addInfo(Tooltip_Silksong_00)
            .addInfo(Tooltip_Silksong_01)
            .addInfo(Tooltip_Silksong_02)
            .addInfo(Tooltip_Silksong_03)
            .addInfo(Tooltip_Silksong_04)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addInputHatch(textUseBlueprint, 3)
            .addOutputHatch(textUseBlueprint, 3)
            .addInputBus(textUseBlueprint, 3)
            .addOutputBus(textUseBlueprint, 2)
            .addMaintenanceHatch(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 1)
            .toolTipFinisher(ModName);
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
        return new GT_TileEntity_Silksong(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {

        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 2)),
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
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 2)),
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
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 2)) };
    }

    // endregion
}
