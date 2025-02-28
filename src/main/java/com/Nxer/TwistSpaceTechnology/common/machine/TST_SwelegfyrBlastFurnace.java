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
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
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
                .addElement('Z', ofBlock(TFFluids.fluidPyrotheum.getBlock(), 0))
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
        this.mHeatingCapacity = 0;
        this.glassTier = 0;
        this.mFlameHatch = null;
        this.setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece("mainT" + controllerTier, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet))
            return false;

        return mFlameHatch != null;
    }

    // region Processing Logic
    byte glassTier = 0;
    byte controllerTier = 1;
    boolean setFlameFinish = false;
    boolean clearFlameFinish = false;
    boolean isPassiveMode = false;
    boolean isRapidHeating = false;
    boolean isHoldingHeat =false;
    ItemStack UpgradeItem = null;
    private MTEHatchInput mFlameHatch;
    public HeatingCoilLevel coilLevel;
    private int mHeatingCapacity;

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        clearFlameFinish = true;
        if (UpgradeItem == null) UpgradeItem = GTCMItemList.TestItem0.get(1);
    }

    private boolean setRemoveFlame() {

        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        String[][] StructureDef = controllerTier > 1 ? shapeFlameT2 : shapeFlameT1;
        Block Air = Blocks.air;
        Block Flame = TFFluids.fluidPyrotheum.getBlock();
        int flameAmount = 0;
        int OffSetX = flameHorizontalOffSet;
        int OffSetY = flameVerticalOffSet;
        int OffSetZ = flameDepthOffSet;
        if (clearFlameFinish) {
            // if (!drain(mFlameHatch, new FluidStack(TFFluids.fluidPyrotheum, flameAmount), false)) return false;
            drain(mFlameHatch, new FluidStack(TFFluids.fluidPyrotheum, flameAmount), true);
            clearFlameFinish = false;
            TstUtils.setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, "Z", Flame);
            setFlameFinish = true;
            return true;
        } else if (setFlameFinish) {
            // clear will not return existing pyrotheum
            setFlameFinish = false;
            TstUtils.setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, "Z", Air);
            clearFlameFinish = true;
            return true;
        }
        return false;
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
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // only can face to X, Z direction
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
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
        return (float) (1 / 4.82);
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int totalMachineMode() {
        return 2;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(UITexture.fullImage(GregTech.ID, "gui/overlay_slot/furnace_bronze"));
        machineModeIcons.add(UITexture.fullImage(GregTech.ID, "gui/overlay_slot/furnace_steel"));
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("Swelegfyr.modeMsg." + mode);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat(TST_SwelegfyrBlastFurnace.this.mHeatingCapacity)
                    .setHeatOC(true)
                    .setHeatDiscount(true);
            }

            @Override
            @Nonnull
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                int mRecipeTier = GTUtility.getTier(recipe.mEUt);
                if (glassTier < 12 && glassTier < mRecipeTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(mRecipeTier);
                }

                if (clearFlameFinish || !setFlameFinish) return SimpleCheckRecipeResult.ofFailure("no_flame");

                return recipe.mSpecialValue <= mHeatingCapacity ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

        }.setMaxParallelSupplier(this::getMaxParallelRecipes);

    }

    private long runningTick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
            if (runningTick % 200 == 0) {
                // Updates every 10 sec
                if (!isPassiveMode) {
                    if (!drain(mFlameHatch, new FluidStack(TFFluids.fluidPyrotheum, 1), true)) {
                        return false;
                    }
                } else if (isPassiveMode && !isRapidHeating) {
                    if (!drain(mFlameHatch, new FluidStack(TFFluids.fluidPyrotheum, 1), true)) {
                        return false;
                    }
                } else if (isPassiveMode && isRapidHeating) {
                    if (!drain(mFlameHatch, new FluidStack(TFFluids.fluidPyrotheum, 1), true)) {
                        return false;
                    }
                } else if (!isPassiveMode && isRapidHeating) {
                    return false;
                }
                runningTick = 1;
            } else {
                runningTick++;
            }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            // Updates every 10 sec
            if (mUpdate <= -150){
                mUpdate = 50;
                if(!aBaseMetaTileEntity.isActive()&&isPassiveMode&&!isRapidHeating&&isHoldingHeat){
                    if (drain(mFlameHatch, new FluidStack(TFFluids.fluidPyrotheum, 1), true)) {
                        mHeatingCapacity=1000;
                    }else stopMachine(SimpleShutDownReason.ofNormal("missing_flame"));
                }
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        runningTick = 0;
        super.stopMachine(reason);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if (!checkStructure(true)) {
                GTUtility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("BallLightning.modeMsg.IncompleteStructure"));
                return;
            } else {
                if (setRemoveFlame()) {
                    // #tr SBF.Msg.setFlame
                    // # Pyrotheum placed
                    // #zh_CN 炽焱填充完毕
                    if (setFlameFinish)
                        GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("SBF.Msg.setFlame"));
                    // #tr SBF.Msg.clearFlame
                    // # Pyrotheum cleared
                    // #zh_CN 炽焱清除完毕
                    if (clearFlameFinish)
                        GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("SBF.Msg.clearFlame"));

                }
            }
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public void setMachineMode(int index) {
        super.setMachineMode(index);
    }


    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
                                float aX, float aY, float aZ) {
        if (controllerTier == 1 && !aPlayer.isSneaking()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (GTUtility.areStacksEqual(GTCMItemList.TestItem0.get(1), heldItem)) {
                controllerTier = 2;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem));
                if (getBaseMetaTileEntity().isServerSide()) {
                    markDirty();
                    aPlayer.inventory.markDirty();
                    // schedule a structure check
                    mUpdated = true;
                }
                if (setFlameFinish) setRemoveFlame();
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        controllerTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return controllerTier;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mTier", controllerTier);
        aNBT.setByte("mGlass", glassTier);
        aNBT.setByte("mMode", (byte) machineMode);
        aNBT.setInteger("mHeatingCapacity", mHeatingCapacity);
        aNBT.setBoolean("setFlameFinish", setFlameFinish);
        aNBT.setBoolean("clearFlameFinish", clearFlameFinish);
        aNBT.setLong("runningTick",runningTick);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        controllerTier = aNBT.getByte("mTier");
        glassTier = aNBT.getByte("mGlass");
        machineMode = aNBT.getByte("mMode");
        mHeatingCapacity = aNBT.getInteger("mHeatingCapacity");
        setFlameFinish = aNBT.getBoolean("setFlameFinish");
        clearFlameFinish = aNBT.getBoolean("clearFlameFinish");
        runningTick=aNBT.getLong("runningTick");
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
