package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.core.block.ModBlocks;

public class TST_LargeIndustrialCokingFactory extends GTCM_MultiMachineBase<TST_LargeIndustrialCokingFactory> {

    // region Class Constructor
    public TST_LargeIndustrialCokingFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_LargeIndustrialCokingFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LargeIndustrialCokingFactory(this.mName);
    }

    // endregion

    // region Processing Logic
    private float speedBonus = 1;

    public HeatingCoilLevel coilLevel;

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.CokingFactoryRecipes;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        this.speedBonus = 1F / (coilLevel.getTier() + 1);
        return true;
    }
    // endregion

    // region Structure
    // spotless:off

    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 13;
    private final int depthOffSet = 0;
    private final String STRUCTURE_PIECE_MAIN = "mainLargeIndustrialCokingFactory";
    private final String[][] shapeMain = new String[][]{
        {"      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      ","      CCC      "},
        {"      CCC      ","               ","      III      ","               ","               ","      III      ","               ","               ","      III      ","               ","               ","      III      ","               ","               ","      III      ","               ","               ","      III      ","               ","               ","      III      ","               ","               ","      III      ","               ","               ","      III      ","               ","      CCC      "},
        {"      CCC      ","      FFF      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      EEE      ","      EEE      ","    IIEEEII    ","      FFF      ","      CCC      "},
        {"      CCC      ","    FFFFFFF    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    EE   EE    ","    EE   EE    ","   IEE   EEI   ","    FFFFFFF    ","      CCC      "},
        {"      CCC      ","   FFFFFFFFF   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   E       E   ","   E       E   ","  IE       EI  ","   FFFFFFFFF   ","      CCC      "},
        {"      CCC      ","  FFFFFFFFFFF  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  FFFFFFFFFFF  ","      CCC      "},
        {"      CCC      ","  FFFFFFFFFFF  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  E         E  ","  E         E  "," IE         EI ","  FFFFFFFFFFF  ","      CCC      "},
        {"      CCC      "," FFFFFFFFFFFFF ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," FFFFFFFFFFFFF ","      CCC      "},
        {"     CCCCC     "," FFFFFFFFFFFFF ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," FFFFFFFFFFFFF ","     CCCCC     "},
        {"    CCCCCCC    "," FFFFFFFFFFFFF ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," FFFFFFFFFFFFF ","    CCCCCCC    "},
        {"  CCCCCCCCCCC  "," FFFFFFFFFFFFF ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," E           E "," E           E ","IE           EI"," FFFFFFFFFFFFF ","  CCCCCCCCCCC  "},
        {" CCBCBCBCBCBCC "," FFAFAFAFAFAFF ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," GHHHHHHHHHHHG "," GHHHHHHHHHHHG ","IEHHHHHHHHHHHEI"," FFAFAFAFAFAFF "," CCBCBCBCBCBCC "},
        {"CCCCCCCCCCCCCCC"," FFFFFFFFFFFFF ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," EDDDDDDDDDDDE "," EDDDDDDDDDDDE ","IEDDDDDDDDDDDEI"," FFFFFFFFFFFFF ","CCCCCCCCCCCCCCC"},
        {"CCCCCCC~CCCCCCC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CFFFFFFFFFFFFFC","CCCCCCCCCCCCCCC"}
    };
    private static IStructureDefinition<TST_LargeIndustrialCokingFactory> STRUCTURE_DEFINITION = null;



    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
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

    /*
Blocks:
A -> ofBlock...(gt.blockcasings2, 15, ...);
B -> ofBlock...(gt.blockcasings3, 15, ...);
C -> ofBlock...(gt.blockcasings4, 0, ...); // IOs
D -> ofBlock...(gt.blockcasings5, 0, ...); // Coils
E -> ofBlock...(gt.blockcasingsNH, 2, ...);
F -> ofBlock...(miscutils.blockcasings, 1, ...);
G -> ofBlock...(miscutils.blockcasings, 3, ...);
H -> ofFrame...(Materials.BlackSteel, ...);
I -> ofFrame...(Materials.Steel, ...);
     */
    @Override
    public IStructureDefinition<TST_LargeIndustrialCokingFactory> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition
                                       .<TST_LargeIndustrialCokingFactory>builder()
                                       .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                                       .addElement('A', ofBlock(GregTech_API.sBlockCasings2, 15))
                                       .addElement('B', ofBlock(GregTech_API.sBlockCasings3, 15))
                                       .addElement(
                                           'C',
                                           GT_HatchElementBuilder
                                               .<TST_LargeIndustrialCokingFactory>builder()
                                               .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                                               .adder(TST_LargeIndustrialCokingFactory::addToMachineList)
                                               .dot(1)
                                               .casingIndex(48)
                                               .buildAndChain(GregTech_API.sBlockCasings4, 0))
                                       .addElement(
                                           'D',
                                           withChannel(
                                               "coil",
                                               ofCoil(
                                                   TST_LargeIndustrialCokingFactory::setCoilLevel,
                                                   TST_LargeIndustrialCokingFactory::getCoilLevel)))
                                       .addElement('E', ofBlockUnlocalizedName("dreamcraft", "gt.blockcasingsNH", 2))
                                       .addElement('F', ofBlock(ModBlocks.blockCasingsMisc, 1))
                                       .addElement('G', ofBlock(ModBlocks.blockCasingsMisc, 3))
                                       .addElement('H', ofFrame(Materials.BlackSteel))
                                       .addElement('I', ofFrame(Materials.Steel))
                                       .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:on
    // endregion

    // region Info
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_LargeIndustrialCokingFactory_MachineType)
            .addInfo(TextLocalization.Tooltip_LargeIndustrialCokingFactory_Controller)
            .addInfo(TextLocalization.Tooltip_LargeIndustrialCokingFactory_01)
            .addInfo(TextLocalization.Tooltip_LargeIndustrialCokingFactory_02)
            .addInfo(TextLocalization.Tooltip_LargeIndustrialCokingFactory_03)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addController(textFrontBottom)
            .addInputHatch(textUseBlueprint, 1)
            .addOutputHatch(textUseBlueprint, 1)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    // endregion

    // region General

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48] };
    }
}
