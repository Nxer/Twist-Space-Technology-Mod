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
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.*;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.google.common.collect.ImmutableList;
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
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings2;
import gregtech.common.blocks.GT_Block_Casings8;

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
    private float speedBonus = 1;
    public byte glassTier;

    @Override
    protected boolean isEnablePerfectOverclock() {
        return EnablePerfectOverclock_MegaMacerator;
    }

    @Override
    protected float getSpeedBonus() {
        // int mEuTier = GT_Utility.getTier(this.getMaxInputEu()) - 1;
        // return (float) (Math.log(100 + exp(4 * 13 - mBlockTier * mEuTier))
        // / Math.log(100 + exp(3 * 11 - mBlockTier * mEuTier))
        // * Math.pow(1 / BlockspeedMultiplier_MegaMacerator, mBlockTier)
        // * Math.pow(EuModifier_MegaMacerator, mEuTier));
        return speedBonus;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.maceratorRecipes;
    }

    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                return super.process();
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (glassTier < 12 && glassTier < GT_Utility.getTier(recipe.mEUt)) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(GT_Utility.getTier(recipe.mEUt));
                }
                if (glassTier > 11 || glassTier > GT_Utility.getTier(recipe.mEUt)) {
                    speedBonus = SpeedBonus_MegaMacerator;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected int getMaxParallelRecipes() {
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
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (glassTier < 12) {
            for (GT_MetaTileEntity_Hatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int getTierOfBlock(Block block, int meta) {
        if (block == null) {
            return -1;
        }
        if (block == GregTech_API.sBlockMetal2 && meta == 9) {
            return 1; // Damascus Steel
        }
        if (block == GregTech_API.sBlockMetal5 && meta == 2) {
            return 2; // Neutronium
        }
        if (block == GregTech_API.sBlockMetal9 && meta == 8) {
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
                    GT_HatchElementBuilder.<TST_MegaMacerator>builder()
                        .atLeast(InputBus, OutputBus, Maintenance)
                        .adder(TST_MegaMacerator::addToMachineList)
                        .dot(2)
                        .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                        .buildAndChain(GregTech_API.sBlockCasings2, 0))
                .addElement('b', ofBlock(GregTech_API.sBlockCasings2, 0))
                .addElement('C', ofBlock(GregTech_API.sBlockCasings2, 8))
                .addElement(
                    'D',
                    GT_HatchElementBuilder.<TST_MegaMacerator>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(TST_MegaMacerator::addToMachineList)
                        .dot(1)
                        .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(3))
                        .buildAndChain(GregTech_API.sBlockCasings8, 3))
                .addElement('E', ofBlock(GregTech_API.sBlockCasings8, 7))
                .addElement('F', ofBlock(GregTech_API.sBlockCasings8, 10))
                // .addElement('G', ofBlock(WerkstoffLoader.BWBlocks, 10097))
                .addElement(
                    'G',
                    withChannel(
                        "structureblock",
                        ofBlocksTiered(
                            TST_MegaMacerator::getTierOfBlock,
                            ImmutableList.of(
                                Pair.of(GregTech_API.sBlockMetal2, 9),
                                Pair.of(GregTech_API.sBlockMetal5, 2),
                                Pair.of(GregTech_API.sBlockMetal9, 8)),
                            0,
                            (m, t) -> m.mBlockTier = t,
                            m -> m.mBlockTier)))

                .addElement('H', ofFrame(Materials.NaquadahAlloy))
                .addElement('I', ofFrame(Materials.CosmicNeutronium))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

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
    private final String[][] shape = new String[][] {
        { "                 ", "                 ", "                 ", "                 ", "                 ",
            "      FFFFF      ", "     FFFFFFF     ", "     FFFFFFF     ", "     FFFFFFF     ", "     FFFFFFF     ",
            "     FFFFFFF     ", "      FFFFF      ", "                 ", "                 ", "                 ",
            "                 ", "                 " },
        { "                 ", "                 ", "      FFFFF      ", "    FFFFFFFFF    ", "   FFFFFFFFFFF   ",
            "   FFF     FFF   ", "  FFF       FFF  ", "  FFF       FFF  ", "  FFF       FFF  ", "  FFF       FFF  ",
            "  FFF       FFF  ", "   FFF     FFF   ", "   FFFFFFFFFFF   ", "    FFFFFFFFF    ", "      FFFFF      ",
            "                 ", "                 " },
        { "      FFFFF      ", "    FFFFFFFFF    ", "   FFFCCCCCFFF   ", "  FFCC     CCFF  ", " FFC         CFF ",
            " FFC         CFF ", "FFC           CFF", "FFC           CFF", "FFC           CFF", "FFC           CFF",
            "FFC           CFF", " FFC         CFF ", " FFC         CFF ", "  FFCC     CCFF  ", "   FFFCCCCCFFF   ",
            "    FFFFFFFFF    ", "      FFFFF      " },
        { "                 ", "      DbbbD      ", "    IbbbbbbbI    ", "   bbb     bbb   ", "  Ib         bI  ",
            "  bb         bb  ", " bb    EEE    bb ", " bb   ECCCE   bb ", " bb   ECCCE   bb ", " bb   ECCCE   bb ",
            " bb    EEE    bb ", "  bb         bb  ", "  Ib         bI  ", "   bbb     bbb   ", "    IbbbbbbbI    ",
            "      DbbbD      ", "                 " },
        { "      DDDDD      ", "    DDDDDDDDD    ", "   DDDDDDDDDDD   ", "  DDDDDDDDDDDDD  ", " DDDDDDDDDDDDDDD ",
            " DDDDDDDDDDDDDDD ", "DDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDD", "DDDDDDDDCDDDDDDDD", "DDDDDDDDDDDDDDDDD",
            "DDDDDDDDDDDDDDDDD", " DDDDDDDDDDDDDDD ", " DDDDDDDDDDDDDDD ", "  DDDDDDDDDDDDD  ", "   DDDDDDDDDDD   ",
            "    DDDDDDDDD    ", "      DDDDD      " },
        { "                 ", "      FFFFF      ", "    FF     FF    ", "   F         F   ", "  F           F  ",
            "  F           F  ", " F             F ", " F             F ", " F      C      F ", " F             F ",
            " F             F ", "  F           F  ", "  F           F  ", "   F         F   ", "    FF     FF    ",
            "      FFFFF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H       G   H  ", " F   GG   G    F ", " A     G G     A ", " A      C      A ", " A     G G     A ",
            " F    G   GG   F ", "  H   G       H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D      G  D   ", "  F       G   F  ",
            "  H  GG   GG  H  ", " F GGGGG  GG   F ", " A     GGGG    A ", " A     GCG     A ", " A    GGGG     A ",
            " F   GG  GGGGG F ", "  H  GG   GG  H  ", "  F   G       F  ", "   D  G      D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH    GHF    ", "   D      G  D   ", "  F       GG  F  ",
            "  H GGG  GGG  H  ", " FGGGGGG GGG   F ", " A   GGGGGG    A ", " A     GCG     A ", " A    GGGGGG   A ",
            " F   GGG GGGGGGF ", "  H  GGG  GGG H  ", "  F  GG       F  ", "   D  G      D   ", "    FHG    HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D      G  D   ", "  F       G   F  ",
            "  H  GG   GG  H  ", " F GGGGG  GG   F ", " A     GGGG    A ", " A     GCG     A ", " A    GGGG     A ",
            " F   GG  GGGGG F ", "  H  GG   GG  H  ", "  F   G       F  ", "   D  G      D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H       G   H  ", " F   GG   G    F ", " A     G G     A ", " A      C      A ", " A     G G     A ",
            " F    G   GG   F ", "  H   G       H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H           H  ", " F             F ", " A             A ", " A      C      A ", " A             A ",
            " F             F ", "  H           H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H       G   H  ", " F   GG   G    F ", " A     G G     A ", " A      C      A ", " A     G G     A ",
            " F    G   GG   F ", "  H   G       H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D      G  D   ", "  F       G   F  ",
            "  H  GG   GG  H  ", " F GGGGG  GG   F ", " A     GGGG    A ", " A     GCG     A ", " A    GGGG     A ",
            " F   GG  GGGGG F ", "  H  GG   GG  H  ", "  F   G       F  ", "   D  G      D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH    GHF    ", "   D      G  D   ", "  F       GG  F  ",
            "  H GGG  GGG  H  ", " FGGGGGG GGG   F ", " A   GGGGGG    A ", " A     GCG     A ", " A    GGGGGG   A ",
            " F   GGG GGGGGGF ", "  H  GGG  GGG H  ", "  F  GG       F  ", "   D  G      D   ", "    FHG    HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D      G  D   ", "  F       G   F  ",
            "  H  GG   GG  H  ", " F GGGGG  GG   F ", " A     GGGG    A ", " A     GCG     A ", " A    GGGG     A ",
            " F   GG  GGGGG F ", "  H  GG   GG  H  ", "  F   G       F  ", "   D  G      D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H       G   H  ", " F   GG   G    F ", " A     G G     A ", " A      C      A ", " A     G G     A ",
            " F    G   GG   F ", "  H   G       H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H  C     C  H  ", " F             F ", " A             A ", " A      C      A ", " A             A ",
            " F             F ", "  H  C     C  H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   DGGG   GGGD   ", "  FGGGGG GGGGGF  ",
            "  HGGCGG GGCGGH  ", " F GGGGG GGGGG F ", " A  GGG   GGG  A ", " A      C      A ", " A  GGG   GGG  A ",
            " F GGGGG GGGGG F ", "  HGGCGG GGCGGH  ", "  FGGGGG GGGGGF  ", "   DGGG   GGGD   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H  C     C  H  ", " F     GGG     F ", " A    GGGGG    A ", " A    GGCGG    A ", " A    GGGGG    A ",
            " F     GGG     F ", "  H  C     C  H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   DGGG   GGGD   ", "  FGGGGG GGGGGF  ",
            "  HGGCGG GGCGGH  ", " F GGGGG GGGGG F ", " A  GGG   GGG  A ", " A      C      A ", " A  GGG   GGG  A ",
            " F GGGGG GGGGG F ", "  HGGCGG GGCGGH  ", "  FGGGGG GGGGGF  ", "   DGGG   GGGD   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FAAAF      ", "    FH     HF    ", "   D         D   ", "  F           F  ",
            "  H  C     C  H  ", " F     GGG     F ", " A    GGGGG    A ", " A    GGCGG    A ", " A    GGGGG    A ",
            " F     GGG     F ", "  H  C     C  H  ", "  F           F  ", "   D         D   ", "    FH     HF    ",
            "      FAAAF      ", "                 " },
        { "                 ", "      FFFFF      ", "    FF     FF    ", "   DGGG   GGGD   ", "  FGGGGG GGGGGF  ",
            "  FGGCGG GGCGGF  ", " F GGGGG GGGGG F ", " F  GGG   GGG  F ", " F      C      F ", " F  GGG   GGG  F ",
            " F GGGGG GGGGG F ", "  FGGCGG GGCGGF  ", "  FGGGGG GGGGGF  ", "   FGGG   GGGF   ", "    FF     FF    ",
            "      FFFFF      ", "                 " },
        { "      DDDDD      ", "    DDDDDDDDD    ", "   DDDDDDDDDDD   ", "  DDDDDDDDDDDDD  ", " DDDDDDDDDDDDDDD ",
            " DDDDCDDDDDCDDDD ", "DDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDD", "DDDDDDDDCDDDDDDDD", "DDDDDDDDDDDDDDDDD",
            "DDDDDDDDDDDDDDDDD", " DDDDCDDDDDCDDDD ", " DDDDDDDDDDDDDDD ", "  DDDDDDDDDDDDD  ", "   DDDDDDDDDDDD  ",
            "    DDDDDDDDD    ", "      DDDDD      " },
        { "                 ", "      DBBBD      ", "    HBBBBBBBH    ", "   BBBEEEEEBBB   ", "  HBEEEEEEEEEBH  ",
            "  BBECEEEEECEBB  ", " DBEEEEEEEEEEEBD ", " BBEEEECCCEEEEBB ", "  BEEEECCCEEEEB  ", " BBEEEECCCEEEEBB ",
            " DBEEEEEEEEEEEBD ", "  BBECEEEEECEBB  ", "  HBEEEEEEEEEBH  ", "   BBBEEEEEBBB   ", "    HBBBBBBBH    ",
            "      DB BD      ", "                 " },
        { "                 ", "      DB~BD      ", "    HBBBBBBBH    ", "   BBB     BBB   ", "  HBFFF   FFFBH  ",
            "  BBFCFFFFFCFBB  ", " DB FFFEEEFFF BD ", " BB  FEEEEEF  BB ", "  B  FEECEEF  B  ", " BB  FEEEEEF  BB ",
            " DB FFFEEEFFF BD ", "  BBFCFFFFFCFBB  ", "  HBFFF   FFFBH  ", "   BBB     BBB   ", "    HBBBBBBBH    ",
            "      DB BD      ", "                 " },
        { "                 ", "      DBBBD      ", "    HBBBBBBBH    ", "   BBB     BBB   ", "  HBFFF   FFFBH  ",
            "  BBFCFFFFFCFBB  ", " DB FFFCCCFFF BD ", " BB  FCCCCCF  BB ", "  B  FCCCCCF  B  ", " BB  FCCCCCF  BB ",
            " DB FFFCCCFFF BD ", "  BBFCFFFFFCFBB  ", "  HBFFF   FFFBH  ", "   BBB     BBB   ", "    HBBBBBBBH    ",
            "      DB BD      ", "                 " },
        { "      DDDDD      ", "    DDDDDDDDD    ", "  DDDDDDDDDDDDD  ", "  DDDDDDDDDDDDD  ", " DDDDDDDDDDDDDDD ",
            " DDDDDDDDDDDDDDD ", "DDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDD",
            "DDDDDDDDDDDDDDDDD", " DDDDDDDDDDDDDDD ", " DDDDDDDDDDDDDDD ", "  DDDDDDDDDDDDD  ", "  DDDDDDDDDDDDD  ",
            "    DDDDDDDDD    ", "      DDDDD      " } };

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
        return ret;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)) };
    }
    // end region
}
