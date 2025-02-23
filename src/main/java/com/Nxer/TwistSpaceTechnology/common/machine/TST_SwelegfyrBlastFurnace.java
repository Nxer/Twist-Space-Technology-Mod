package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textEndSides;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class TST_SwelegfyrBlastFurnace extends GTCM_MultiMachineBase<TST_SwelegfyrBlastFurnace> {

    // region Class Constructor
    public TST_SwelegfyrBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_SwelegfyrBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_SwelegfyrBlastFurnace(this.mName);
    }
    // end region

    // region Structure
    private static final int baseHorizontalOffSet = 5;
    private static final int baseVerticalOffSet = 17;
    private static final int baseDepthOffSet = 1;
    private static final int flameHorizontalOffSet = 3;
    private static final int flameVerticalOffSet = 10;
    private static final int flameDepthOffSet = -1;
    private static final String STRUCTURE_PIECE_MAIN_T1 = "mainT1";
    private static final String STRUCTURE_PIECE_MAIN_T2 = "mainT2";
    private static final String STRUCTURE_PIECE_FLAME_T1 = "flameT1";
    private static final String STRUCTURE_PIECE_FLAME_T2 = "flameT2";
    // spotless:off
    private static final String[][] shapeMainT1 =new String[][]{
        {"           ","           ","   NNNNN   ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  NHHHHHN  "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN ","  NHHHHHN  ","   NNNNN   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFJJJFMF ","FMFEEEEEFMF","FMJEEEEEJMF","FMJEECEEJMF","FMJEEEEEJMF","FMFEEEEEFMF"," FMFJJJFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"           ","   J   J   ","   FAAAF   "," JFEEEEEFJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JFEEEEEFJ ","   FAAAF   ","   J   J   ","           "},
        {"           ","   J   J   ","   FAAAF   "," JFEEEEEFJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JFEEEEEFJ ","   FAAAF   ","   J   J   ","           "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFJJJFMF ","FMFEEEEEFMF","FMJEEEEEJMF","FMJEECEEJMF","FMJEEEEEJMF","FMFEEEEEFMF"," FMFJJJFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   JJ JJ   ","  JNNNNNJ  "," JNDDDDDNJ ","JJNEEEEENJ ","  NDECEDN  ","JJNEEEEENJ "," JNDDDDDNJ ","  JNNNNNJ  ","   JJ JJ   ","           "},
        {"           ","    J J    ","   NHNHN   ","  NEEEEEN  "," JHEEEEEHJ ","  NDECEDN  "," JHEEEEEHJ ","  NEEEEEN  ","   NHNHN   ","    J J    ","           "},
        {"           ","    J J    ","   NHNHN   ","  NEEEEEN  "," JHEEEEEHJ ","  NDECEDN  "," JHEEEEEHJ ","  NEEEEEN  ","   NHNHN   ","    J J    ","           "},
        {"           ","           ","   NHNHN   ","  NEEEEEN  ","  HEEEEEH  ","  NDECEDN  ","  HEEEEEH  ","  NEEEEEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  NEEEEEN  ","  HEEEEEH  ","  NDECEDN  ","  HEEEEEH  ","  NEEEEEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  NEEEEEN  ","  HEEEEEH  ","  NDECEDN  ","  HEEEEEH  ","  NEEEEEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NNNNN   ","  NEEEEEN  ","  NEEEEEN  ","  NDECEDN  ","  NEEEEEN  ","  NEEEEEN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  N     N  "," N  DDD  N "," N DEEED N "," N DECED N "," N DEEED N "," N  DDD  N ","  N     N  ","   NNNNN   ","           "},
        {"           ","   MOOOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   MO~OM   ","  A     A  "," A  KKK  A "," A KEEEK A "," A KECEK A "," A KEEEK A "," A  KKK  A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   MOOOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   PPPPP   ","  PDDDDDP  "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP ","  PDDDDDP  ","   PPPPP   ","           "}
    };

    private static final String[][] shapeMainT2 =new String[][]{
        {"                         ","                         ","   NNNNN                 ","  NNNNNNN                ","  NNNNNNN                ","  NNNNNNN                ","  NNNNNNN                ","  NNNNNNN                ","   NNNNN                 ","                         ","                         "},
        {"                         ","   NNNNN                 ","  NHHHHHN                "," NHDDDDDHN               "," NHDDDDDHN               "," NHDDDDDHN               "," NHDDDDDHN               "," NHDDDDDHN               ","  NHHHHHN                ","   NNNNN                 ","                         "},
        {"   FFFFF                 ","  FMMMMMF                "," FMFJJJFMF       JJJ     ","FMFEEEEEFMF     J   J    ","FMJEEEEEJMFJJJJJ     J   ","FMJEECEEJMF          J   ","FMJEEEEEJMFJJJJJ     J   ","FMFEEEEEFMF     J   J    "," FMFJJJFMF       JJJ     ","  FMMMMMF                ","   FFFFF                 "},
        {"                         ","   HHHHH                 ","  HFFFFFH        HHH     "," HFEEEEEFH      HNNNH    "," HFEEEEEFHHHHHHHNNNNNH   "," HFEECEEFNNNNNNNNNNNNH   "," HFEEEEEFHHHHHHHNNNNNH   "," HFEEEEEFH      HNNNH    ","  HFFFFFH        HHH     ","   HHHHH                 ","                         "},
        {"                         ","   J   J                 ","   FAAAF                 "," JFEEEEEFJ      FAAAF    ","  AEEEEEHAAAAAAAA   A    ","  AEECEEN         L A    ","  AEEEEEHAAAAAAAA   A    "," JFEEEEEFJ      FAAAF    ","   FAAAF                 ","   J   J                 ","                         "},
        {"                         ","   J   J                 ","   FAAAF                 "," JFEEEEEFJ      FAAAF    ","  AEEEEEHAAAAAAAA   A    ","  AEECEEN         L A    ","  AEEEEEHAAAAAAAA   A    "," JFEEEEEFJ      FAAAF    ","   FAAAF                 ","   J   J                 ","                         "},
        {"                         ","   HHHHH                 ","  HFFFFFH        HHH     "," HFEEEEEFH      HNNNH    "," HFEEEEEFHHHHHHHN   NH   "," HFEECEEFNNNNNNNN L NH   "," HFEEEEEFHHHHHHHN   NH   "," HFEEEEEFH      HNNNH    ","  HFFFFFH        HHH     ","   HHHHH                 ","                         "},
        {"   FFFFF                 ","  FMMMMMF                "," FMFJJJFMF       JJJ     ","FMFEEEEEFMF     JAAAJ    ","FMJEEEEEJMFJJJJJA   AJ   ","FMJEECEEJMF     N L NJH  ","FMJEEEEEJMFJJJJJA   AJ   ","FMFEEEEEFMF     JAAAJ    "," FMFJJJFMF       JJJ     ","  FMMMMMF                ","   FFFFF                 "},
        {"                         ","   JJ JJ                 ","  JNNNNNJ                "," JNDDDDDNJ       AAA     ","JJNEEEEENJJ     A   A    ","  NDECEDN       N L NJH  ","JJNEEEEENJJ     A   A    "," JNDDDDDNJ       AAA     ","  JNNNNNJ                ","   JJ JJ                 ","                         "},
        {"                         ","    J J                  ","   NHNHN                 ","  NEEEEEN        AAA     "," JHEEEEEHJ      A   A    ","  NDECEDN       N L NJHH "," JHEEEEEHJ      A   A    ","  NEEEEEN        AAA     ","   NHNHN                 ","    J J                  ","                         "},
        {"                         ","    J J                  ","   NHNHN                 ","  NEEEEEN        AAA     "," JHEEEEEHJ      A   A    ","  NDECEDN       N L NJHH "," JHEEEEEHJ      A   A    ","  NEEEEEN        AAA     ","   NHNHN                 ","    J J                  ","                         "},
        {"                         ","                         ","   NHNHN                 ","  NEEEEEN        AAA     ","  HEEEEEH       A   A    ","  NDECEDN       N L NJHH ","  HEEEEEH       A   A    ","  NEEEEEN        AAA     ","   NHNHN                 ","                         ","                         "},
        {"                         ","                         ","   NHNHN                 ","  NEEEEEN        AAA     ","  HEEEEEH       A   A    ","  NDECEDN       N L NJHH ","  HEEEEEH       A   A    ","  NEEEEEN        AAA     ","   NHNHN                 ","                         ","                         "},
        {"                         ","                         ","   NHNHN                 ","  NEEEEEN        AAA     ","  HEEEEEH       A   AJJJ ","  NDECEDN       N L NJHHH","  HEEEEEH       A   AJJJ ","  NEEEEEN        AAA     ","   NHNHN                 ","                         ","                         "},
        {"                         ","                         ","   NNNNN                 ","  NEEEEEN        AAA     ","  NEEEEEN       A   AJHJ ","  NDECEDN       N L NJHHH","  NEEEEEN       A   AJHJ ","  NEEEEEN        AAA     ","   NNNNN                 ","                         ","                         "},
        {"                         ","   NNNNN                 ","  N     N       GGGGG    "," N  DDD  N     GGNNNGG   "," N DEEED N     GN   NGHJ "," N DECED N     GN L NGUHH"," N DEEED N     GN   NGHJ "," N  DDD  N     GGNNNGG   ","  N     N       GGGGG    ","   NNNNN                 ","                         "},
        {"                         ","   MOOOM                 ","  M     M       BGIGB    "," M  MMM  M     BNDIDNB   "," M MEEEM M     GD   DGHJ "," M MECEM M     II L IIUHH"," M MEEEM M     GD   DGHJ "," M  MMM  M     BNDIDNB   ","  M     M       BGIGB    ","   MMMMM                 ","                         "},
        {"                         ","   MO~OM            HHH  ","  A     A       BQQQBHHH "," A  KKK  A     BNDDDNBHH "," A KEEEK A     QD   DQHHH"," A KECEK A     QD L DQUUH"," A KEEEK A     QD   DQHHH"," A  KKK  A     BNDDDNBHH ","  A     A       BQQQBHHH ","   AAAAA            HHH  ","                         "},
        {"                         ","   MOOOM            HJJ  ","  M     M       BGIGBJJJ "," M  MMM  M     BNDIDNBJJ "," M MEEEM M     GD   DGHHH"," M MECEM M     II L IIUUH"," M MEEEM M     GD   DGHHH"," M  MMM  M     BNDIDNBJJ ","  M     M       BGIGBJJJ ","   MMMMM            HJJ  ","                         "},
        {"                         ","   PPPPP            HHH  ","  PDDDDDP       GGGGGHHH "," PDDDDDDDP     GGNNNGGHH "," PDDDDDDDP     GNNNNNGHHH"," PDDDDDDDP     GNNNNNGHHH"," PDDDDDDDP     GNNNNNGHHH"," PDDDDDDDP     GGNNNGGHH ","  PDDDDDP       GGGGGHHH ","   PPPPP            HHH  ","                         "}
    };

    private static final String[][] shapeFlameT1 =new String[][]{
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {"       ","       ","       ","       ","       ","       ","       "},
        {" ZZZZZ ","ZZ   ZZ","Z     Z","Z     Z","Z     Z","ZZ   ZZ"," ZZZZZ "},
        {" ZZZZZ ","ZZ   ZZ","Z     Z","Z     Z","Z     Z","ZZ   ZZ"," ZZZZZ "},
        {" ZZZZZ ","ZZ   ZZ","Z     Z","Z     Z","Z     Z","ZZ   ZZ"," ZZZZZ "}
    };

    private static final String[][] shapeFlameT2 =new String[][]{
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {"                  ","                  ","               ZZZ","               Z Z","               ZZZ","                  ","                  "},
        {" ZZZZZ            ","ZZ   ZZ           ","Z     Z        ZZZ","Z     Z        Z Z","Z     Z        ZZZ","ZZ   ZZ           "," ZZZZZ            "},
        {" ZZZZZ            ","ZZ   ZZ           ","Z     Z        ZZZ","Z     Z        Z Z","Z     Z        ZZZ","ZZ   ZZ           "," ZZZZZ            "},
        {" ZZZZZ            ","ZZ   ZZ           ","Z     Z        ZZZ","Z     Z        Z Z","Z     Z        ZZZ","ZZ   ZZ           "," ZZZZZ            "}
    };
    // spotless:on

    private static final IStructureDefinition<TST_SwelegfyrBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<TST_SwelegfyrBlastFurnace>builder()
        .addShape(STRUCTURE_PIECE_MAIN_T1, transpose(shapeMainT1))
        .addShape(STRUCTURE_PIECE_MAIN_T2, transpose(shapeMainT2))
        .addShape(STRUCTURE_PIECE_FLAME_T1, transpose(shapeFlameT1))
        .addShape(STRUCTURE_PIECE_FLAME_T2, transpose(shapeFlameT2))
        .addElement(
            'A',
            withChannel(
                "glass",
                BorosilicateGlass
                    .ofBoroGlass((byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glassTier = t, te -> te.glassTier)))
        .addElement('B', ofBlock(GameRegistry.findBlock(Mods.IndustrialCraft2.ID, "blockFenceIron"), 0))
        .addElement('C', ofBlock(compactFusionCoil, 0))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings1, 11))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings5, 8))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 5))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 2))
        .addElement('H', ofBlock(GregTechAPI.sBlockCasings8, 10))
        .addElement('I', ofFrame(Materials.Neutronium))
        .addElement('J', ofFrame(Materials.NaquadahAlloy))
        .addElement('K', ofFrame(Materials.CosmicNeutronium))
        .addElement('L', ofBlock(ModBlocks.blockCustomMachineCasings, 3))
        .addElement('M', ofBlock(ModBlocks.blockCasingsMisc, 14))
        .addElement('N', ofBlock(TstBlocks.MetaBlockCasing01, 15))
        .addElement(
            'O',
            HatchElementBuilder.<TST_SwelegfyrBlastFurnace>builder()
                .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                .adder(TST_SwelegfyrBlastFurnace::addToMachineList)
                .dot(1)
                .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
        .addElement(
            'P',
            HatchElementBuilder.<TST_SwelegfyrBlastFurnace>builder()
                .atLeast(Energy.or(ExoticEnergy))
                .adder(TST_SwelegfyrBlastFurnace::addToMachineList)
                .dot(2)
                .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
        .addElement(
            'Q',
            buildHatchAdder(TST_SwelegfyrBlastFurnace.class).hatchClass(MTEHatchInput.class)
                .adder(TST_SwelegfyrBlastFurnace::addFlameHatch)
                .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                .dot(3)
                .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
        .addElement('U', ofBlock(GregTechAPI.sBlockCasings10, 15))
        .build();

    @Override
    public IStructureDefinition<TST_SwelegfyrBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        int structureTier = stackSize.stackSize;
        if (structureTier > 1) structureTier = 2;
        this.buildPiece(
            "mainT" + structureTier,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseVerticalOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built;
        int builtW;
        int structureTier = stackSize.stackSize;
        if (structureTier > 1) structureTier = 2;
        built = survivialBuildPiece(
            "mainT" + structureTier,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet,
            elementBudget,
            env,
            false,
            true);
        builtW = survivialBuildPiece(
            "flameT" + structureTier,
            stackSize,
            flameHorizontalOffSet,
            flameVerticalOffSet,
            flameDepthOffSet,
            elementBudget,
            env,
            false,
            true);
        if (built >= 0) return built;
        return built + builtW;

    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        glassTier = 0;
        controllerTier = 0;
        return checkPiece("mainT" + controllerTier, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet)
            & checkPiece("flameT" + controllerTier, flameHorizontalOffSet, flameVerticalOffSet, flameDepthOffSet);
    }

    // region Processing Logic
    byte glassTier = 0;
    byte controllerTier = 0;
    private MTEHatchInput mFlameHatch;

    public boolean addFlameHatch(IGregTechTileEntity aTileEntity, short aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mFlameHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture base = casingTexturePages[115][MetaBlockCasing01.getTextureIndexInPage(1)];
        if (side == facing) {
            if (active) return new ITexture[] { base, TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAAdvancedEBF)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAAdvancedEBFActive)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { base, TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAAdvancedEBF)
                .extFacing()
                .glow()
                .build() };
        }
        return new ITexture[] { base };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine_MachineType"))
            .addInfo(TextEnums.tr("Tooltip_AdvCircuitAssemblyLine_Controller"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addEnergyHatch(textUseBlueprint, 1)
            .addInputBus(textUseBlueprint, 2)
            .addInputHatch(textUseBlueprint, 3)
            .addOutputBus(textEndSides, 2)
            .addStructureInfo(Text_SeparatingLine)
            .toolTipFinisher(ModName);
        return tt;
    }
}
