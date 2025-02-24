package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
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
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
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
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

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
    private static final String[][] shapeMainT1 = new String[][]{
        {"           ","           ","   NNNNN   ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  NHHHHHN  "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN ","  NHHHHHN  ","   NNNNN   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFJJJFMF ","FMFEEEEEFMF","FMJEEEEEJMF","FMJEECEEJMF","FMJEEEEEJMF","FMFEEEEEFMF"," FMFJJJFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"           ","   J   J   ","   FAAAF   "," JFEEEEEFJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JFEEEEEFJ ","   FAAAF   ","   J   J   ","           "},
        {"           ","   J   J   ","   FAAAF   "," JFEEEEEFJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JFEEEEEFJ ","   FAAAF   ","   J   J   ","           "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFJJJFMF ","FMFEEEEEFMF","FMJEEEEEJMF","FMJEECEEJMF","FMJEEEEEJMF","FMFEEEEEFMF"," FMFJJJFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   JJ JJ   ","  JNNNNNJ  "," JNDDDDDNJ ","JJNDEEEDNJ ","  NDECEDN  ","JJNDEEEDNJ "," JNDDDDDNJ ","  JNNNNNJ  ","   JJ JJ   ","           "},
        {"           ","    J J    ","   NHNHN   ","  N DDD N  "," JHDEEEDHJ ","  NDECEDN  "," JHDEEEDHJ ","  N DDD N  ","   NHNHN   ","    J J    ","           "},
        {"           ","    J J    ","   NHNHN   ","  N DDD N  "," JHDEEEDHJ ","  NDECEDN  "," JHDEEEDHJ ","  N DDD N  ","   NHNHN   ","    J J    ","           "},
        {"           ","           ","   NHNHN   ","  N DDD N  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  N DDD N  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  N DDD N  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  N DDD N  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  N DDD N  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  N DDD N  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NNNNN   ","  N DDD N  ","  NDEEEDN  ","  NDECEDN  ","  NDEEEDN  ","  N DDD N  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  N     N  "," N  DDD  N "," N DEEED N "," N DECED N "," N DEEED N "," N  DDD  N ","  N     N  ","   NNNNN   ","           "},
        {"           ","   MOOOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   MO~OM   ","  A     A  "," A  KKK  A "," A KEEEK A "," A KECEK A "," A KEEEK A "," A  KKK  A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   MOQOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   PPPPP   ","  PDDDDDP  "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP ","  PDDDDDP  ","   PPPPP   ","           "}
    };

    private static final String[][] shapeMainT2 = new String[][]{
        {"                         ","                         ","   NNNNN                 ","  NNNNNNN                ","  NNNNNNN                ","  NNNNNNN                ","  NNNNNNN                ","  NNNNNNN                ","   NNNNN                 ","                         ","                         "},
        {"                         ","   NNNNN                 ","  NHHHHHN                "," NHDDDDDHN               "," NHDDDDDHN               "," NHDDDDDHN               "," NHDDDDDHN               "," NHDDDDDHN               ","  NHHHHHN                ","   NNNNN                 ","                         "},
        {"   FFFFF                 ","  FMMMMMF                "," FMFJJJFMF       JJJ     ","FMFEEEEEFMF     J   J    ","FMJEEEEEJMFJJJJJ     J   ","FMJEECEEJMF          J   ","FMJEEEEEJMFJJJJJ     J   ","FMFEEEEEFMF     J   J    "," FMFJJJFMF       JJJ     ","  FMMMMMF                ","   FFFFF                 "},
        {"                         ","   HHHHH                 ","  HFFFFFH        HHH     "," HFEEEEEFH      HNNNH    "," HFEEEEEFHHHHHHHNNNNNH   "," HFEECEEFNNNNNNNNNNNNH   "," HFEEEEEFHHHHHHHNNNNNH   "," HFEEEEEFH      HNNNH    ","  HFFFFFH        HHH     ","   HHHHH                 ","                         "},
        {"                         ","   J   J                 ","   FAAAF                 "," JFEEEEEFJ      FAAAF    ","  AEEEEEHAAAAAAAA   A    ","  AEECEEN         L A    ","  AEEEEEHAAAAAAAA   A    "," JFEEEEEFJ      FAAAF    ","   FAAAF                 ","   J   J                 ","                         "},
        {"                         ","   J   J                 ","   FAAAF                 "," JFEEEEEFJ      FAAAF    ","  AEEEEEHAAAAAAAA   A    ","  AEECEEN         L A    ","  AEEEEEHAAAAAAAA   A    "," JFEEEEEFJ      FAAAF    ","   FAAAF                 ","   J   J                 ","                         "},
        {"                         ","   HHHHH                 ","  HFFFFFH        HHH     "," HFEEEEEFH      HNNNH    "," HFEEEEEFHHHHHHHN   NH   "," HFEECEEFNNNNNNNN L NH   "," HFEEEEEFHHHHHHHN   NH   "," HFEEEEEFH      HNNNH    ","  HFFFFFH        HHH     ","   HHHHH                 ","                         "},
        {"   FFFFF                 ","  FMMMMMF                "," FMFJJJFMF       JJJ     ","FMFEEEEEFMF     JAAAJ    ","FMJEEEEEJMFJJJJJA   AJ   ","FMJEECEEJMF     N L NJH  ","FMJEEEEEJMFJJJJJA   AJ   ","FMFEEEEEFMF     JAAAJ    "," FMFJJJFMF       JJJ     ","  FMMMMMF                ","   FFFFF                 "},
        {"                         ","   JJ JJ                 ","  JNNNNNJ                "," JNDDDDDNJ       AAA     ","JJNDEEEDNJJ     A   A    ","  NDECEDN       N L NJH  ","JJNDEEEDNJJ     A   A    "," JNDDDDDNJ       AAA     ","  JNNNNNJ                ","   JJ JJ                 ","                         "},
        {"                         ","    J J                  ","   NHNHN                 ","  N DDD N        AAA     "," JHDEEEDHJ      A   A    ","  NDECEDN       N L NJHH "," JHDEEEDHJ      A   A    ","  N DDD N        AAA     ","   NHNHN                 ","    J J                  ","                         "},
        {"                         ","    J J                  ","   NHNHN                 ","  N DDD N        AAA     "," JHDEEEDHJ      A   A    ","  NDECEDN       N L NJHH "," JHDEEEDHJ      A   A    ","  N DDD N        AAA     ","   NHNHN                 ","    J J                  ","                         "},
        {"                         ","                         ","   NHNHN                 ","  N DDD N        AAA     ","  HDEEEDH       A   A    ","  NDECEDN       N L NJHH ","  HDEEEDH       A   A    ","  N DDD N        AAA     ","   NHNHN                 ","                         ","                         "},
        {"                         ","                         ","   NHNHN                 ","  N DDD N        AAA     ","  HDEEEDH       A   A    ","  NDECEDN       N L NJHH ","  HDEEEDH       A   A    ","  N DDD N        AAA     ","   NHNHN                 ","                         ","                         "},
        {"                         ","                         ","   NHNHN                 ","  N DDD N        AAA     ","  HDEEEDH       A   AJJJ ","  NDECEDN       N L NJHHH","  HDEEEDH       A   AJJJ ","  N DDD N        AAA     ","   NHNHN                 ","                         ","                         "},
        {"                         ","                         ","   NNNNN                 ","  N DDD N        AAA     ","  NDEEEDN       A   AJHJ ","  NDECEDN       N L NJHHH","  NDEEEDN       A   AJHJ ","  N DDD N        AAA     ","   NNNNN                 ","                         ","                         "},
        {"                         ","   NNNNN                 ","  N     N       GGGGG    "," N  DDD  N     GGNNNGG   "," N DEEED N     GN   NGHJ "," N DECED N     GN L NGHHH"," N DEEED N     GN   NGHJ "," N  DDD  N     GGNNNGG   ","  N     N       GGGGG    ","   NNNNN                 ","                         "},
        {"                         ","   MOOOM                 ","  M     M       BGIGB    "," M  MMM  M     BNDIDNB   "," M MEEEM M     GD   DGHJ "," M MECEM M     II L IIHHH"," M MEEEM M     GD   DGHJ "," M  MMM  M     BNDIDNB   ","  M     M       BGIGB    ","   MMMMM                 ","                         "},
        {"                         ","   MO~OM            HHH  ","  A     A       BNQNBHHH "," A  KKK  A     BNDDDNBHH "," A KEEEK A     ND   DNHHH"," A KECEK A     ND L DNHHH"," A KEEEK A     ND   DNHHH"," A  KKK  A     BNDDDNBHH ","  A     A       BNNNBHHH ","   AAAAA            HHH  ","                         "},
        {"                         ","   MOOOM            HJJ  ","  M     M       BGIGBJJJ "," M  MMM  M     BNDIDNBJJ "," M MEEEM M     GD   DGHHH"," M MECEM M     II L IIHHH"," M MEEEM M     GD   DGHHH"," M  MMM  M     BNDIDNBJJ ","  M     M       BGIGBJJJ ","   MMMMM            HJJ  ","                         "},
        {"                         ","   PPPPP            HHH  ","  PDDDDDP       GGGGGHHH "," PDDDDDDDP     GGNNNGGHH "," PDDDDDDDP     GNNNNNGHHH"," PDDDDDDDP     GNNNNNGHHH"," PDDDDDDDP     GNNNNNGHHH"," PDDDDDDDP     GGNNNGGHH ","  PDDDDDP       GGGGGHHH ","   PPPPP            HHH  ","                         "}
    };

    private static final String[][] shapeFlameT1 = new String[][]{
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

    private static final String[][] shapeFlameT2 = new String[][]{
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

    private static IStructureDefinition<TST_SwelegfyrBlastFurnace> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_SwelegfyrBlastFurnace> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_SwelegfyrBlastFurnace>builder()
                .addShape(STRUCTURE_PIECE_MAIN_T1, transpose(shapeMainT1))
                .addShape(STRUCTURE_PIECE_MAIN_T2, transpose(shapeMainT2))
                .addShape(STRUCTURE_PIECE_FLAME_T1, transpose(shapeFlameT1))
                .addShape(STRUCTURE_PIECE_FLAME_T2, transpose(shapeFlameT2))
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
                .addElement('B', ofBlock(GameRegistry.findBlock(Mods.IndustrialCraft2.ID, "blockFenceIron"), 0))
                .addElement('C', ofBlock(compactFusionCoil, 0))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings1, 11))
                .addElement(
                    'E',
                    withChannel(
                        "coil",
                        ofCoil(TST_SwelegfyrBlastFurnace::setCoilLevel, TST_SwelegfyrBlastFurnace::getCoilLevel)))
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 5))
                .addElement('G', ofBlock(TstBlocks.MetaBlockCasing02, 2))
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
                .addElement('Z', ofBlock(TFFluids.fluidPyrotheum.getBlock(), 15))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    int getStructureTier(int aTier) {
        if (aTier > 1) return 2;
        return 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        int structureTier = getStructureTier(stackSize.stackSize);
        this.buildPiece(
            "mainT" + structureTier,
            stackSize,
            hintsOnly,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built;
        int builtW;
        int structureTier = getStructureTier(stackSize.stackSize);
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
        if (checkPiece(STRUCTURE_PIECE_MAIN_T2, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet)) {
            return checkPiece(STRUCTURE_PIECE_FLAME_T2, flameHorizontalOffSet, flameVerticalOffSet, flameDepthOffSet);
        } else if (checkPiece(STRUCTURE_PIECE_MAIN_T1, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet)) {
            return checkPiece(STRUCTURE_PIECE_FLAME_T1, flameHorizontalOffSet, flameVerticalOffSet, flameDepthOffSet);
        }
        return false;
    }

    // region Processing Logic
    byte glassTier = 0;
    byte controllerTier = 1;
    private MTEHatchInput mFlameHatch;
    public HeatingCoilLevel coilLevel;

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

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

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.blastFurnaceRecipes);
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
        ITexture base = casingTexturePages[115][MetaBlockCasing01.getTextureIndexInPage(15)];
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
        tt.addMachineType(TextLocalization.Tooltip_Scavenger_MachineType)
            .addInfo(TextLocalization.Tooltip_Scavenger_Controller)
            .addInfo(TextLocalization.Tooltip_Scavenger_01)
            .addInfo(TextLocalization.Tooltip_Scavenger_02)
            .addInfo(TextLocalization.Tooltip_Scavenger_03)
            .addInfo(TextLocalization.Tooltip_Scavenger_04)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textFrontBottom)
            .addInputHatch(textUseBlueprint, 1)
            .addOutputHatch(textUseBlueprint, 2)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 2)
            .addEnergyHatch(textUseBlueprint, 2)
            .addStructureInfo(Text_SeparatingLine)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(ModName);
        return tt;
    }
}
