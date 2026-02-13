package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.BlockTier1Parallel_MegaMacerator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.BlockTier2Parallel_MegaMacerator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EnablePerfectOverclock_MegaMacerator;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_MegaMacerator;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.blocks.BlockCasings8;

public class TST_MegaMacerator extends GTCM_MultiMachineBase<TST_MegaMacerator> {

    // region Class Constructor
    public TST_MegaMacerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MegaMacerator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaMacerator(this.mName);
    }

    // end region

    // region Processing Logic
    private int mBlockTier = 0;
    public int mRecipeTier = 1;
    public byte glassTier;

    @Override
    protected boolean isEnablePerfectOverclock() {
        return EnablePerfectOverclock_MegaMacerator;
    }

    @Override
    protected float getSpeedBonus() {
        return (((glassTier >= 12) || (glassTier > mRecipeTier)) ? SpeedBonus_MegaMacerator : 1);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.maceratorRecipes;
    }

    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                mRecipeTier = GTUtility.getTier(recipe.mEUt);
                setSpeedBonus(getSpeedBonus());
                if (glassTier < 12 && glassTier < mRecipeTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(mRecipeTier);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        switch (mBlockTier) {
            case (1):
                return BlockTier1Parallel_MegaMacerator;
            case (2):
                return BlockTier2Parallel_MegaMacerator;
            case (3):
                return Integer.MAX_VALUE;
            default:
                return -1;
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        mBlockTier = 0;
        glassTier = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        return true;
    }

    public static int getTierOfBlock(Block block, int meta) {
        if (block == null) {
            return -1;
        }
        if (block == GregTechAPI.sBlockMetal2 && meta == 9) {
            return 1; // Damascus Steel
        }
        if (block == GregTechAPI.sBlockMetal5 && meta == 2) {
            return 2; // Neutronium
        }
        if (block == GregTechAPI.sBlockMetal9 && meta == 8) {
            return 3; // Universium
        }
        return -1;
    }

    // region Structure

    private final int horizontalOffSet = 8;
    private final int verticalOffSet = 25;
    private final int depthOffSet = 1;
    private static final String STRUCTURE_PIECE_MAIN = "mainMegaMacerator";
    private static IStructureDefinition<TST_MegaMacerator> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
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
    public IStructureDefinition<TST_MegaMacerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaMacerator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    withChannel(
                        "glass",
                        BorosilicateGlass.ofBoroGlass(
                            (byte) 0,
                            (byte) 1,
                            Byte.MAX_VALUE,
                            (te, t) -> te.glassTier = t,
                            te -> te.glassTier)))
                .addElement(
                    'B',
                    HatchElementBuilder.<TST_MegaMacerator>builder()
                        .atLeast(InputBus, OutputBus, Maintenance)
                        .adder(TST_MegaMacerator::addToMachineList)
                        .dot(2)
                        .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                        .buildAndChain(GregTechAPI.sBlockCasings2, 0))
                .addElement('b', ofBlock(GregTechAPI.sBlockCasings2, 0))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 8))
                .addElement(
                    'D',
                    HatchElementBuilder.<TST_MegaMacerator>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(TST_MegaMacerator::addToMachineList)
                        .dot(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 3))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 10))
                // .addElement('G', ofBlock(WerkstoffLoader.BWBlocks, 10097))
                .addElement(
                    'G',
                    withChannel(
                        "structureblock",
                        ofBlocksTiered(
                            TST_MegaMacerator::getTierOfBlock,
                            ImmutableList.of(
                                Pair.of(GregTechAPI.sBlockMetal2, 9),
                                Pair.of(GregTechAPI.sBlockMetal5, 2),
                                Pair.of(GregTechAPI.sBlockMetal9, 8)),
                            -1,
                            (m, t) -> m.mBlockTier = t,
                            m -> m.mBlockTier)))

                .addElement('H', ofFrame(Materials.NaquadahAlloy))
                .addElement('I', ofFrame(Materials.CosmicNeutronium))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:off

    /*
     * Blocks:
     * A -> ofBlock...(BW_GlasBlocks2, 0, ...);
     * B -> ofBlock...(gt.blockcasings2, 0, ...);
     * C -> ofBlock...(gt.blockcasings2, 8, ...);
     * D -> ofBlock...(gt.blockcasings8, 3, ...);
     * E -> ofBlock...(gt.blockcasings8, 7, ...);
     * F -> ofBlock...(gt.blockcasings8, 10, ...);
     * G -> ofBlock...(gt.blockmetal5, 2, ...);
     * H -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...);
     * I -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...);
     */
    private final String[][] shape = new String[][]{
        {"                 ","                 ","                 ","                 ","                 ","      FFFFF      ","     FFFFFFF     ","     FFFFFFF     ","     FFFFFFF     ","     FFFFFFF     ","     FFFFFFF     ","      FFFFF      ","                 ","                 ","                 ","                 ","                 "},
        {"                 ","                 ","      FFFFF      ","    FFFFFFFFF    ","   FFFFFFFFFFF   ","   FFF     FFF   ","  FFF       FFF  ","  FFF       FFF  ","  FFF       FFF  ","  FFF       FFF  ","  FFF       FFF  ","   FFF     FFF   ","   FFFFFFFFFFF   ","    FFFFFFFFF    ","      FFFFF      ","                 ","                 "},
        {"      FFFFF      ","    FFFFFFFFF    ","   FFFCCCCCFFF   ","  FFCC     CCFF  "," FFC         CFF "," FFC         CFF ","FFC           CFF","FFC           CFF","FFC           CFF","FFC           CFF","FFC           CFF"," FFC         CFF "," FFC         CFF ","  FFCC     CCFF  ","   FFFCCCCCFFF   ","    FFFFFFFFF    ","      FFFFF      "},
        {"                 ","      DbbbD      ","    IbbbbbbbI    ","   bbb     bbb   ","  Ib         bI  ","  bb         bb  "," bb    EEE    bb "," bb   ECCCE   bb "," bb   ECCCE   bb "," bb   ECCCE   bb "," bb    EEE    bb ","  bb         bb  ","  Ib         bI  ","   bbb     bbb   ","    IbbbbbbbI    ","      DbbbD      ","                 "},
        {"      DDDDD      ","    DDDDDDDDD    ","   DDDDDDDDDDD   ","  DDDDDDDDDDDDD  "," DDDDDDDDDDDDDDD "," DDDDDDDDDDDDDDD ","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD","DDDDDDDDCDDDDDDDD","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD"," DDDDDDDDDDDDDDD "," DDDDDDDDDDDDDDD ","  DDDDDDDDDDDDD  ","   DDDDDDDDDDD   ","    DDDDDDDDD    ","      DDDDD      "},
        {"                 ","      FFFFF      ","    FF     FF    ","   F         F   ","  F           F  ","  F           F  "," F             F "," F             F "," F      C      F "," F             F "," F             F ","  F           F  ","  F           F  ","   F         F   ","    FF     FF    ","      FFFFF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H       G   H  "," F   GG   G    F "," A     G G     A "," A      C      A "," A     G G     A "," F    G   GG   F ","  H   G       H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D      G  D   ","  F       G   F  ","  H  GG   GG  H  "," F GGGGG  GG   F "," A     GGGG    A "," A     GCG     A "," A    GGGG     A "," F   GG  GGGGG F ","  H  GG   GG  H  ","  F   G       F  ","   D  G      D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH    GHF    ","   D      G  D   ","  F       GG  F  ","  H GGG  GGG  H  "," FGGGGGG GGG   F "," A   GGGGGG    A "," A     GCG     A "," A    GGGGGG   A "," F   GGG GGGGGGF ","  H  GGG  GGG H  ","  F  GG       F  ","   D  G      D   ","    FHG    HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D      G  D   ","  F       G   F  ","  H  GG   GG  H  "," F GGGGG  GG   F "," A     GGGG    A "," A     GCG     A "," A    GGGG     A "," F   GG  GGGGG F ","  H  GG   GG  H  ","  F   G       F  ","   D  G      D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H       G   H  "," F   GG   G    F "," A     G G     A "," A      C      A "," A     G G     A "," F    G   GG   F ","  H   G       H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H           H  "," F             F "," A             A "," A      C      A "," A             A "," F             F ","  H           H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H       G   H  "," F   GG   G    F "," A     G G     A "," A      C      A "," A     G G     A "," F    G   GG   F ","  H   G       H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D      G  D   ","  F       G   F  ","  H  GG   GG  H  "," F GGGGG  GG   F "," A     GGGG    A "," A     GCG     A "," A    GGGG     A "," F   GG  GGGGG F ","  H  GG   GG  H  ","  F   G       F  ","   D  G      D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH    GHF    ","   D      G  D   ","  F       GG  F  ","  H GGG  GGG  H  "," FGGGGGG GGG   F "," A   GGGGGG    A "," A     GCG     A "," A    GGGGGG   A "," F   GGG GGGGGGF ","  H  GGG  GGG H  ","  F  GG       F  ","   D  G      D   ","    FHG    HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D      G  D   ","  F       G   F  ","  H  GG   GG  H  "," F GGGGG  GG   F "," A     GGGG    A "," A     GCG     A "," A    GGGG     A "," F   GG  GGGGG F ","  H  GG   GG  H  ","  F   G       F  ","   D  G      D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H       G   H  "," F   GG   G    F "," A     G G     A "," A      C      A "," A     G G     A "," F    G   GG   F ","  H   G       H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H  C     C  H  "," F             F "," A             A "," A      C      A "," A             A "," F             F ","  H  C     C  H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   DGGG   GGGD   ","  FGGGGG GGGGGF  ","  HGGCGG GGCGGH  "," F GGGGG GGGGG F "," A  GGG   GGG  A "," A      C      A "," A  GGG   GGG  A "," F GGGGG GGGGG F ","  HGGCGG GGCGGH  ","  FGGGGG GGGGGF  ","   DGGG   GGGD   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H  C     C  H  "," F     GGG     F "," A    GGGGG    A "," A    GGCGG    A "," A    GGGGG    A "," F     GGG     F ","  H  C     C  H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   DGGG   GGGD   ","  FGGGGG GGGGGF  ","  HGGCGG GGCGGH  "," F GGGGG GGGGG F "," A  GGG   GGG  A "," A      C      A "," A  GGG   GGG  A "," F GGGGG GGGGG F ","  HGGCGG GGCGGH  ","  FGGGGG GGGGGF  ","   DGGG   GGGD   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FAAAF      ","    FH     HF    ","   D         D   ","  F           F  ","  H  C     C  H  "," F     GGG     F "," A    GGGGG    A "," A    GGCGG    A "," A    GGGGG    A "," F     GGG     F ","  H  C     C  H  ","  F           F  ","   D         D   ","    FH     HF    ","      FAAAF      ","                 "},
        {"                 ","      FFFFF      ","    FF     FF    ","   DGGG   GGGD   ","  FGGGGG GGGGGF  ","  FGGCGG GGCGGF  "," F GGGGG GGGGG F "," F  GGG   GGG  F "," F      C      F "," F  GGG   GGG  F "," F GGGGG GGGGG F ","  FGGCGG GGCGGF  ","  FGGGGG GGGGGF  ","   FGGG   GGGF   ","    FF     FF    ","      FFFFF      ","                 "},
        {"      DDDDD      ","    DDDDDDDDD    ","   DDDDDDDDDDD   ","  DDDDDDDDDDDDD  "," DDDDDDDDDDDDDDD "," DDDDCDDDDDCDDDD ","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD","DDDDDDDDCDDDDDDDD","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD"," DDDDCDDDDDCDDDD "," DDDDDDDDDDDDDDD ","  DDDDDDDDDDDDD  ","   DDDDDDDDDDD   ","    DDDDDDDDD    ","      DDDDD      "},
        {"                 ","      DBBBD      ","    HBBBBBBBH    ","   BBBEEEEEBBB   ","  HBEEEEEEEEEBH  ","  BBECEEEEECEBB  "," DBEEEEEEEEEEEBD "," BBEEEECCCEEEEBB ","  BEEEECCCEEEEB  "," BBEEEECCCEEEEBB "," DBEEEEEEEEEEEBD ","  BBECEEEEECEBB  ","  HBEEEEEEEEEBH  ","   BBBEEEEEBBB   ","    HBBBBBBBH    ","      DB BD      ","                 "},
        {"                 ","      DB~BD      ","    HBBBBBBBH    ","   BBB     BBB   ","  HBFFF   FFFBH  ","  BBFCFFFFFCFBB  "," DB FFFEEEFFF BD "," BB  FEEEEEF  BB ","  B  FEECEEF  B  "," BB  FEEEEEF  BB "," DB FFFEEEFFF BD ","  BBFCFFFFFCFBB  ","  HBFFF   FFFBH  ","   BBB     BBB   ","    HBBBBBBBH    ","      DB BD      ","                 "},
        {"                 ","      DBBBD      ","    HBBBBBBBH    ","   BBB     BBB   ","  HBFFF   FFFBH  ","  BBFCFFFFFCFBB  "," DB FFFCCCFFF BD "," BB  FCCCCCF  BB ","  B  FCCCCCF  B  "," BB  FCCCCCF  BB "," DB FFFCCCFFF BD ","  BBFCFFFFFCFBB  ","  HBFFF   FFFBH  ","   BBB     BBB   ","    HBBBBBBBH    ","      DB BD      ","                 "},
        {"      DDDDD      ","    DDDDDDDDD    ","  DDDDDDDDDDDDD  ","  DDDDDDDDDDDDD  "," DDDDDDDDDDDDDDD "," DDDDDDDDDDDDDDD ","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD","DDDDDDDDDDDDDDDDD"," DDDDDDDDDDDDDDD "," DDDDDDDDDDDDDDD ","  DDDDDDDDDDDDD  ","  DDDDDDDDDDDDD  ","    DDDDDDDDD    ","      DDDDD      "}
    };

    // spotless:on

    // endregion

    // region General

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    // Scanner Info
    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Glass Tier: " + EnumChatFormatting.GOLD + this.glassTier;
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Block Level: " + EnumChatFormatting.GOLD + this.mBlockTier;
        // ret[origin.length + 2] = EnumChatFormatting.AQUA + "Eu Tier: " + EnumChatFormatting.GOLD + this.mRecipeTier;
        return ret;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_MegaMacerator_MachineType)
            .addInfo(TextLocalization.Tooltip_MegaMacerator_Controller)
            .addInfo(TextLocalization.Tooltip_MegaMacerator_01)
            .addInfo(TextLocalization.Tooltip_MegaMacerator_02)
            .addInfo(TextLocalization.Tooltip_MegaMacerator_03)
            .addInfo(TextLocalization.Tooltip_MegaMacerator_04)
            .addInfo(TextLocalization.Tooltip_MegaMacerator_05)
            .addInfo(TextLocalization.Tooltip_MegaMacerator_06)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textFrontBottom)
            .addInputBus(textUseBlueprint, 2)
            .addOutputBus(textUseBlueprint, 2)
            .addMaintenanceHatch(textUseBlueprint, 2)
            .addEnergyHatch(textUseBlueprint, 1)
            .addStructureInfo(Text_SeparatingLine)
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)) };
    }
    // end region
}
