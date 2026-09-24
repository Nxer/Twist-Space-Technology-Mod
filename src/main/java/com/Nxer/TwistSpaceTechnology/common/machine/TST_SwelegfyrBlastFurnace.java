package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.special_hatch_amount_wrong;
import static com.Nxer.TwistSpaceTechnology.util.RecipeMathUtils.numericalApproximation;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.General.Kelvin;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.General.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.MachineTooltip.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.getBlueprintWithDot;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textColon;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.Structure.textSpace;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_SwelegfyrBlastFurnace;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@SkipGenerateDescription
public class TST_SwelegfyrBlastFurnace extends GTCM_MultiMachineBase<TST_SwelegfyrBlastFurnace> {

    // region Class Constructor
    public TST_SwelegfyrBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.GODERIUM);
    }

    public TST_SwelegfyrBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_SwelegfyrBlastFurnace(this.mName);
    }
    // endregion

    // region Structure
    protected static final int baseHorizontalOffSet = 5;
    protected static final int baseVerticalOffSet = 17;
    protected static final int baseDepthOffSet = 1;
    protected static final int BlazeHorizontalOffSet = 3;
    protected static final int BlazeVerticalOffSet = 10;
    protected static final int BlazeDepthOffSet = -1;
    protected static final String STRUCTURE_PIECE_MAIN_T1 = "mainT1";
    protected static final String STRUCTURE_PIECE_MAIN_T2 = "mainT2";
    protected static final String STRUCTURE_PIECE_Blaze_T1 = "BlazeT1";
    protected static final String STRUCTURE_PIECE_Blaze_T2 = "BlazeT2";

    // spotless:off
    protected static final String[][] shapeMainT1 = new String[][]{
        {"           ","           ","   NNNNN   ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  NHHHHHN  "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN ","  NHHHHHN  ","   NNNNN   ","           "},
        {"   GGGGG   ","  GMMMMMG  "," GMGKKKGMG ","GMGEEEEEGMG","GMKEEEEEKMG","GMKEECEEKMG","GMKEEEEEKMG","GMGEEEEEGMG"," GMGKKKGMG ","  GMMMMMG  ","   GGGGG   "},
        {"           ","   HHHHH   ","  HGGGGGH  "," HGEEEEEGH "," HGEEEEEGH "," HGEECEEGH "," HGEEEEEGH "," HGEEEEEGH ","  HGGGGGH  ","   HHHHH   ","           "},
        {"           ","   J   J   ","  BGAAAGB  "," JGEEEEEGJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JGEEEEEGJ ","  BGAAAGB  ","   J   J   ","           "},
        {"           ","   J   J   ","  BGAAAGB  "," JGEEEEEGJ ","  AEEEEEA  ","  AEECEEA  ","  AEEEEEA  "," JGEEEEEGJ ","  BGAAAGB  ","   J   J   ","           "},
        {"           ","   HHHHH   ","  HGGGGGH  "," HGEEEEEGH "," HGEEEEEGH "," HGEECEEGH "," HGEEEEEGH "," HGEEEEEGH ","  HGGGGGH  ","   HHHHH   ","           "},
        {"   GGGGG   ","  GMMMMMG  "," GMGJJJGMG ","GMGEEEEEGMG","GMJEEEEEJMG","GMJEECEEJMG","GMJEEEEEJMG","GMGEEEEEGMG"," GMGJJJGMG ","  GMMMMMG  ","   GGGGG   "},
        {"           ","   JJ JJ   ","  JNNNNNJ  "," JNEDDDENJ ","JJNDEEEDNJ ","  NDECEDN  ","JJNDEEEDNJ "," JNEDDDENJ ","  JNNNNNJ  ","   JJ JJ   ","           "},
        {"           ","    J J    ","   NHNHN   ","  NEDDDEN  "," JHDEEEDHJ ","  NDECEDN  "," JHDEEEDHJ ","  NEDDDEN  ","   NHNHN   ","    J J    ","           "},
        {"           ","    J J    ","   NHNHN   ","  NEDDDEN  "," JHDEEEDHJ ","  NDECEDN  "," JHDEEEDHJ ","  NEDDDEN  ","   NHNHN   ","    J J    ","           "},
        {"           ","           ","   NHNHN   ","  NEDDDEN  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  NEDDDEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  NEDDDEN  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  NEDDDEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NHNHN   ","  NEDDDEN  ","  HDEEEDH  ","  NDECEDN  ","  HDEEEDH  ","  NEDDDEN  ","   NHNHN   ","           ","           "},
        {"           ","           ","   NNNNN   ","  NNDDDNN  ","  NDEEEDN  ","  NDECEDN  ","  NDEEEDN  ","  NNDDDNN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  N     N  "," N  DDD  N "," N DEEED N "," N DECED N "," N DEEED N "," N  DDD  N ","  N     N  ","   NNNNN   ","           "},
        {"           ","   MOOOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   MO~OM   ","  A     A  "," A  KKK  A "," A KEEEK A "," A KECEK A "," A KEEEK A "," A  KKK  A ","  A     A  ","   AAAAA   ","           "},
        {"           ","   MOSOM   ","  M     M  "," M  MMM  M "," M MEEEM M "," M MECEM M "," M MEEEM M "," M  MMM  M ","  M     M  ","   MMMMM   ","           "},
        {"           ","   PPPPP   ","  PDDDDDP  "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP "," PDDDDDDDP ","  PDDDDDP  ","   PPPPP   ","           "}
    };

    protected static final String[][] shapeMainT2 = new String[][]{
        {"                      ","                      ","   NNNNN              ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","   NNNNN              ","                      ","                      "},
        {"                      ","   NNNNN              ","  NHHHHHN             "," NHDDDDDHN       NNN  "," NHDDDDDHN      NNNNN "," NHDDDDDHN      NNNNN "," NHDDDDDHN      NNNNN "," NHDDDDDHN       NNN  ","  NHHHHHN             ","   NNNNN              ","                      "},
        {"   GGGGG              ","  GMMMMMG             "," GMGKKKGMG      GGGGG ","GMGEEEEEGMG    GGGGGGG","GMKEEEEEKMGJJJJGGGGGGG","GMKEECEEKMG    GGGGGGG","GMKEEEEEKMGJJJJGGGGGGG","GMGEEEEEGMG    GGGGGGG"," GMGKKKGMG      GGGGG ","  GMMMMMG             ","   GGGGG              "},
        {"                      ","   HHHHH              ","  HGGGGGH        HHH  "," HGEEEEEGH      HNNNH "," HGEEEEEGHHHHHHHNFFFNH"," HGEECEEGNNNNNNNNFFFNH"," HGEEEEEGHHHHHHHNFFFNH"," HGEEEEEGH      HNNNH ","  HGGGGGH        HHH  ","   HHHHH              ","                      "},
        {"                      ","   J   J              ","  BGAAAGB             "," JGEEEEEGJ      GAAAG ","  AEEEEEHAAAAAAAA   A ","  AEECEEN         L A ","  AEEEEEHAAAAAAAA   A "," JGEEEEEGJ      GAAAG ","  BGAAAGB             ","   J   J              ","                      "},
        {"                      ","   J   J              ","  BGAAAGB             "," JGEEEEEGJ      GAAAG ","  AEEEEEHAAAAAAAA   A ","  AEECEEN         L A ","  AEEEEEHAAAAAAAA   A "," JGEEEEEGJ      GAAAG ","  BGAAAGB             ","   J   J              ","                      "},
        {"                      ","   HHHHH              ","  HGGGGGH        HHH  "," HGEEEEEGH      HNNNH "," HGEEEEEGHHHHHHHN   NH"," HGEECEEGNNNNNNNN L NH"," HGEEEEEGHHHHHHHN   NH"," HGEEEEEGH      HNNNH ","  HGGGGGH        HHH  ","   HHHHH              ","                      "},
        {"   GGGGG              ","  GMMMMMG             "," GMGJJJGMG       GGG  ","GMGEEEEEGMG     GAAAG ","GMJEEEEEJMGJJJJGA   AG","GMJEECEEJMG    GN L NG","GMJEEEEEJMGJJJJGA   AG","GMGEEEEEGMG     GAAAG "," GMGJJJGMG       GGG  ","  GMMMMMG             ","   GGGGG              "},
        {"                      ","   JJ JJ              ","  JNNNNNJ        JJJ  "," JNEDDDENJ      JAAAJ ","JJNDEEEDNJJ    JA   AJ","  NDECEDN      JN L NJ","JJNDEEEDNJJ    JA   AJ"," JNEDDDENJ      JAAAJ ","  JNNNNNJ        JJJ  ","   JJ JJ              ","                      "},
        {"                      ","    J J               ","   NHNHN              ","  NEDDDEN       BAAAB "," JHDEEEDHJ      A   A ","  NDECEDN       N L N "," JHDEEEDHJ      A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","    J J               ","                      "},
        {"                      ","    J J               ","   NHNHN              ","  NEDDDEN       BAAAB "," JHDEEEDHJ      A   A ","  NDECEDN       N L N "," JHDEEEDHJ      A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","    J J               ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NNNNN              ","  NNDDDNN       BAAAB ","  NDEEEDN       A   A ","  NDECEDN       N L N ","  NDEEEDN       A   A ","  NNDDDNN       BAAAB ","   NNNNN              ","                      ","                      "},
        {"                      ","   NNNNN              ","  N     N       GGGGG "," N  DDD  N     GGNNNGG"," N DEEED N     GN   NG"," N DECED N     GN L NG"," N DEEED N     GN   NG"," N  DDD  N     GGNNNGG","  N     N       GGGGG ","   NNNNN              ","                      "},
        {"                      ","   MOOOM              ","  M     M       BGIGB "," M  MMM  M     BHFIFHB"," M MEEEM M     GF   FG"," M MECEM M     II L II"," M MEEEM M     GF   FG"," M  MMM  M     BHFIFHB","  M     M       BGIGB ","   MMMMM              ","                      "},
        {"                      ","   MO~OM              ","  A     A       BNSNB "," A  KKK  A     BHFFFHB"," A KEEEK A     NF   FN"," A KECEK A     NF L FN"," A KEEEK A     NF   FN"," A  KKK  A     BHFFFHB","  A     A       BNONB ","   AAAAA              ","                      "},
        {"                      ","   MOOOM              ","  M     M       BGIGB "," M  MMM  M     BHFIFHB"," M MEEEM M     GF   FG"," M MECEM M     II L II"," M MEEEM M     GF   FG"," M  MMM  M     BHFIFHB","  M     M       BGIGB ","   MMMMM              ","                      "},
        {"                      ","   PPPPP              ","  PDDDDDP       GGGGG "," PDDDDDDDP     GGNNNGG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GGNNNGG","  PDDDDDP       GGGGG ","   PPPPP              ","                      "}
    };

    protected static final String[][] shapeBlazeT1 = new String[][]{
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

    protected static final String[][] shapeBlazeT2 = new String[][]{
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

    protected static IStructureDefinition<TST_SwelegfyrBlastFurnace> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_SwelegfyrBlastFurnace> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_SwelegfyrBlastFurnace>builder()
                .addShape(STRUCTURE_PIECE_MAIN_T1, transpose(shapeMainT1))
                .addShape(STRUCTURE_PIECE_MAIN_T2, transpose(shapeMainT2))
                .addShape(STRUCTURE_PIECE_Blaze_T1, transpose(shapeBlazeT1))
                .addShape(STRUCTURE_PIECE_Blaze_T2, transpose(shapeBlazeT2))
                .addElement('-', isAir())
                .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement('a', ofChain(chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier), isAir()))
                .addElement('B', ofBlock(ItemList.FenceIron.getBlock(), 0))
                .addElement('C', ofBlock(compactFusionCoil, 0))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings1, 11))
                .addElement(
                    'E',
                    withChannel(
                        "coil",
                        ofCoil(TST_SwelegfyrBlastFurnace::setCoilLevel, TST_SwelegfyrBlastFurnace::getCoilLevel)))
                .addElement('F', ofBlock(TstBlocks.MetaBlockCasing02, 6))
                .addElement('G', ofBlock(TstBlocks.MetaBlockCasing02, 2))
                .addElement('H', ofBlock(GregTechAPI.sBlockCasings8, 10))
                .addElement(
                    'h',
                    ofChain(ofBlock(GregTechAPI.sBlockCasings8, 10), ofBlock(TstBlocks.MetaBlockCasing01, 15)))
                .addElement('I', ofFrame(Materials.Neutronium))
                .addElement('J', ofFrame(Materials.NaquadahAlloy))
                .addElement('K', ofFrame(Materials.CosmicNeutronium))
                .addElement('L', ofBlock(ModBlocks.blockCustomMachineCasings, 3))
                .addElement('M', ofBlock(ModBlocks.blockCasingsMisc, 14))
                .addElement('N', ofBlock(TstBlocks.MetaBlockCasing01, 15))
                .addElement(
                    'O',
                    HatchElementBuilder.<TST_SwelegfyrBlastFurnace>builder()
                        .atLeast(InputBus, OutputBus, InputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_SwelegfyrBlastFurnace::addToMachineList)
                        .hint(1)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
                .addElement(
                    'P',
                    HatchElementBuilder.<TST_SwelegfyrBlastFurnace>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(TST_SwelegfyrBlastFurnace::addToMachineList)
                        .hint(2)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
                .addElement(
                    'S',
                    buildHatchAdder(TST_SwelegfyrBlastFurnace.class).hatchClass(MTEHatchInput.class)
                        .adder(TST_SwelegfyrBlastFurnace::addBlazeHatch)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                        .hint(3)
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
                .addElement('Z', ofBlock(TFFluids.fluidPyrotheum.getBlock(), 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        this.buildPiece(
            "mainT" + controllerTier,
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
        int structureTier = stackSize.stackSize > 1 ? 2 : 1;
        built = survivalBuildPiece(
            "mainT" + structureTier,
            stackSize,
            baseHorizontalOffSet,
            baseVerticalOffSet,
            baseDepthOffSet,
            elementBudget,
            env,
            false,
            true);
        builtW = survivalBuildPiece(
            "BlazeT" + structureTier,
            stackSize,
            BlazeHorizontalOffSet,
            BlazeVerticalOffSet,
            BlazeDepthOffSet,
            elementBudget,
            env,
            false,
            true);
        if (built >= 0) return built;
        return built + builtW;

    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        recipeHeatLimitation = 0;
        glassTier = -1;

        // Check all tier to render properly in nei
        if (!checkPiece(STRUCTURE_PIECE_MAIN_T2, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet, errors)) {
            clearHatches();
            errors.clear();
            if (!checkPiece(STRUCTURE_PIECE_MAIN_T1, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet, errors)
                || controllerTier > 1) return;
        }

        if (this.mHeatingCapacity < getCoilHeat()) this.mHeatingCapacity = getCoilHeat();
        this.maxHeatingCapacity = (int) (Math.floor(Math.pow(getCoilHeat(), 1.08) / 100) * 100 + 1);

        if (glassTier < 12) {
            for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
                if (this.glassTier < mEnergyHatch.mTier) {
                    errors.add(StructureErrorRegistry.ENERGY_TIER_EXCEED_GLASS);
                    return;
                }
            }
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    errors.add(StructureErrorRegistry.ENERGY_TIER_EXCEED_GLASS);
                    return;
                }
            }
        }

        if (mBlazeHatch == null) {
            errors.add(special_hatch_amount_wrong);
            return;
        }

        // set recipe heat limitation
        recipeHeatLimitation = (int) getCoilLevel().getHeat() + 100 * Math.max(getDominantInputVoltageTier() - 2, 0);

    }
    // endregion

    // region Processing Logic
    public int glassTier = -1;
    public byte controllerTier = 1;
    public boolean isBlazeFinishSet = false;
    public boolean isBlazeFinishClear = true;
    public boolean isPassiveMode = false;
    public boolean inPassiveMode = false;
    public boolean isRapidHeating = false;
    public boolean inRapidHeating = false;
    private int pendingRapidHeatingStep = 0;
    public boolean isHoldingHeat = false;
    private int holdingHeatTicks = 0;
    public static ItemStack UpgradeItem = null;
    public int previousRecipeCode = 0;
    private GTRecipe previousRecipe;
    private int previousRecipeVoltageTier = -1;
    public int correctBlazeCost = 0;
    public MTEHatchInput mBlazeHatch;
    public HeatingCoilLevel coilLevel = HeatingCoilLevel.None;
    public int mHeatingCapacity;
    public int maxHeatingCapacity;
    public int recipeHeatLimitation;
    protected long runningTick = 0;

    private static int getRecipeCode(GTRecipe recipe) {
        int result = 1;
        result = 31 * result + recipe.mEUt;
        result = 31 * result + recipe.mDuration;
        result = 31 * result + recipe.mSpecialValue;
        for (ItemStack input : recipe.mInputs) {
            result = 31 * result + GTUtility.persistentHash(input, true, true);
        }
        for (FluidStack input : recipe.mFluidInputs) {
            result = 31 * result + GTUtility.persistentHash(input, true, true);
        }
        for (ItemStack output : recipe.mOutputs) {
            result = 31 * result + GTUtility.persistentHash(output, true, true);
        }
        for (FluidStack output : recipe.mFluidOutputs) {
            result = 31 * result + GTUtility.persistentHash(output, true, true);
        }
        result = 31 * result + Arrays.hashCode(recipe.mInputChances);
        result = 31 * result + Arrays.hashCode(recipe.mOutputChances);
        result = 31 * result + Arrays.hashCode(recipe.mFluidInputChances);
        result = 31 * result + Arrays.hashCode(recipe.mFluidOutputChances);
        return result == 0 ? 1 : result;
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
    protected boolean useMui2() {
        return super.useMui2();
    }

    @Override
    public int totalMachineMode() {
        return 2;
    }

    @Override
    public void setMachineMode(int index) {
        super.setMachineMode(index);
        isPassiveMode = index != 0;
    }

    public static final UITexture[] tMachineModeIcons = new UITexture[] { UITextures.SBF_ModeBase,
        UITextures.SBF_ModePassive };

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // An unbound meta tile entity has no running state; use its selected mode until it is placed.
        IGregTechTileEntity base = getBaseMetaTileEntity();
        return getMachineModeName(
            base != null && base.isActive(),
            machineMode != 0,
            inPassiveMode,
            inRapidHeating,
            isHoldingHeat);
    }

    private String getMachineModeName(boolean isActive, boolean isPassiveMode, boolean inPassiveMode,
        boolean inRapidHeating, boolean isHoldingHeat) {
        // #tr tst.common.machine.SwelegfyrBlastFurnace.mode.0
        // # Normal Mode
        // #zh_CN 普通模式

        // #tr tst.common.machine.SwelegfyrBlastFurnace.mode.1
        // # Passive Mode
        // #zh_CN 被动模式
        boolean passive = isActive ? inPassiveMode : isPassiveMode;
        String suffixKey = null;
        if (passive) {
            if (isActive && inRapidHeating)
                suffixKey = "tst.common.machine.SwelegfyrBlastFurnace.message.enable_rapid_heating";
            else if (!isActive && isHoldingHeat)
                suffixKey = "tst.common.machine.SwelegfyrBlastFurnace.message.enable_holding_heat";
        }

        String base = StatCollector
            .translateToLocal("tst.common.machine.SwelegfyrBlastFurnace.mode." + (passive ? 1 : 0));
        if (suffixKey != null) {
            return base + "-" + StatCollector.translateToLocal(suffixKey);
        }
        return base;
    }

    @Override
    public int getMaxParallelRecipes() {
        return isPassiveMode ? Config.Parallel_PassiveMode_SwelegfyrBlastFurnace
            : Config.Parallel_NormalMode_SwelegfyrBlastFurnace;
    }

    @Override
    protected float getSpeedBonus() {
        return 1f / (isPassiveMode ? Config.SpeedMultiplier_PassiveMode_SwelegfyrBlastFurnace
            : Config.SpeedMultiplier_NormalMode_SwelegfyrBlastFurnace);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // only can face to X, Z direction
        // return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            private boolean heatBeforeRecipe;
            private int rapidHeatingVoltageTierDifference;
            private int rapidHeatingInitialMissingHeat;
            private GTRecipe previousRecipeAtCheckStart;
            private int previousRecipeCodeAtCheckStart;
            private int previousRecipeVoltageTierAtCheckStart;
            private int heatingCapacityAtCheckStart;
            private int rapidHeatingInitialMissingHeatAtCheckStart;
            private GTRecipe previousRecipeCandidateDuringCheck;

            private int selectRecipeCandidate(GTRecipe recipe) {
                int recipeCode = recipe == previousRecipeAtCheckStart ? previousRecipeCodeAtCheckStart
                    : getRecipeCode(recipe);
                int recipeVoltageTier = GTUtility.getTier(recipe.mEUt);
                if (previousRecipeAtCheckStart == recipe
                    || (previousRecipeCodeAtCheckStart != 0 && previousRecipeCodeAtCheckStart == recipeCode)) {
                    previousRecipeCandidateDuringCheck = recipe;
                }
                // Compare every candidate against the same pre-check state so fallback candidates cannot repeatedly
                // apply heat retention during one recipe search.
                boolean recipeChanged = previousRecipeAtCheckStart == null
                    ? previousRecipeCodeAtCheckStart != 0 && previousRecipeCodeAtCheckStart != recipeCode
                    : previousRecipeAtCheckStart != recipe && previousRecipeCodeAtCheckStart != recipeCode;
                if (recipeChanged) {
                    int coilHeat = getCoilHeat();
                    double retainedHeatRatio = controllerTier > 1
                        ? 0.5 + 0.25 * Math.tanh((previousRecipeVoltageTierAtCheckStart - recipeVoltageTier) / 3.0)
                        : 0;
                    mHeatingCapacity = coilHeat
                        + (int) Math.round(Math.max(heatingCapacityAtCheckStart - coilHeat, 0) * retainedHeatRatio);
                    rapidHeatingInitialMissingHeat = 0;
                } else {
                    mHeatingCapacity = heatingCapacityAtCheckStart;
                    rapidHeatingInitialMissingHeat = rapidHeatingInitialMissingHeatAtCheckStart;
                }
                previousRecipe = recipe;
                previousRecipeCode = recipeCode;
                previousRecipeVoltageTier = recipeVoltageTier;
                return recipeVoltageTier;
            }

            private void restorePreviousRecipeAfterFailedSearch() {
                mHeatingCapacity = heatingCapacityAtCheckStart;
                rapidHeatingInitialMissingHeat = rapidHeatingInitialMissingHeatAtCheckStart;
                previousRecipe = previousRecipeCandidateDuringCheck;
                previousRecipeCode = previousRecipeCodeAtCheckStart;
                previousRecipeVoltageTier = previousRecipeVoltageTierAtCheckStart;
            }

            @Override
            @Nonnull
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {

                // Refresh passive status
                inPassiveMode = isPassiveMode;

                if (heatBeforeRecipe) return CheckRecipeResultRegistry.NO_RECIPE;

                // Select before validation: insufficient heat or power still counts as changing the recipe.
                int recipeVoltageTier = selectRecipeCandidate(recipe);

                if (recipe.mSpecialValue > recipeHeatLimitation)
                    return CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);

                rapidHeatingVoltageTierDifference = getDominantInputVoltageTier() - recipeVoltageTier;
                int minimumRecipeEUtAtMaxHeat = (int) Math
                    .ceil(recipe.mEUt * Math.pow(0.9, Math.max(maxHeatingCapacity - recipe.mSpecialValue, 0) / 900));
                if (availableVoltage * availableAmperage < minimumRecipeEUtAtMaxHeat) {
                    rapidHeatingInitialMissingHeat = 0;
                    return CheckRecipeResultRegistry.insufficientPower(minimumRecipeEUtAtMaxHeat);
                }
                heatBeforeRecipe = isPassiveMode && isRapidHeating && mHeatingCapacity < maxHeatingCapacity;
                if (!heatBeforeRecipe) rapidHeatingInitialMissingHeat = 0;

                // Heat the new recipe before consuming its inputs.
                if (heatBeforeRecipe) {
                    // Rapid heating is a fake recipe, so remember its target before applyRecipe can update the cache.
                    lastRecipe = recipe.mCanBeBuffered ? recipe : null;
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                TST_SwelegfyrBlastFurnace.this.euModifier = (float) Math
                    .pow(0.9, Math.max(mHeatingCapacity - recipe.mSpecialValue, 0) / 900);
                setEuModifier(TST_SwelegfyrBlastFurnace.this.getEuModifier());

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                // overclock depends on the correct heat (mHeatingCapacity)
                return super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat(TST_SwelegfyrBlastFurnace.this.mHeatingCapacity)
                    .setHeatOC(true);
            }

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                heatBeforeRecipe = false;
                rapidHeatingVoltageTierDifference = 0;
                setSpeedBonus(getSpeedBonus());
                setEuModifier(getEuModifier());

                if (checkBlaze()) return shutDownOfMissingPyrotheum(controllerTier > 1 ? 168000 : 72000);

                previousRecipeAtCheckStart = previousRecipe;
                previousRecipeCodeAtCheckStart = previousRecipeCode;
                previousRecipeVoltageTierAtCheckStart = previousRecipeVoltageTier;
                heatingCapacityAtCheckStart = mHeatingCapacity;
                rapidHeatingInitialMissingHeatAtCheckStart = rapidHeatingInitialMissingHeat;
                previousRecipeCandidateDuringCheck = null;
                inRapidHeating = false;
                CheckRecipeResult result = super.process();
                if (heatBeforeRecipe) {
                    inRapidHeating = true;
                    return RapidHeating();
                }
                if (!result.wasSuccessful() && previousRecipeCandidateDuringCheck != null) {
                    // If the old target still matches the current inputs, other failed fallback candidates do not
                    // represent a player recipe change and must not overwrite its heat-retention history.
                    restorePreviousRecipeAfterFailedSearch();
                }
                return result;
            }

            private CheckRecipeResult RapidHeating() {
                int euTier = getDominantInputVoltageTier();
                if (euTier < 1) {
                    stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                    return CheckRecipeResultRegistry.insufficientPower(32);
                }

                int rapidHeatingTierSteps = Math.max(rapidHeatingVoltageTierDifference, 0);
                int missingHeat = maxHeatingCapacity - mHeatingCapacity;
                if (rapidHeatingInitialMissingHeat < missingHeat) rapidHeatingInitialMissingHeat = missingHeat;
                // Voltage above the recipe tier accelerates heating; lower voltage approaches half the base rate.
                double heatingRatio = rapidHeatingVoltageTierDifference >= 0
                    ? 0.2 + 0.25 * (1 - Math.exp(-rapidHeatingTierSteps / 2.0))
                    : 0.2 * (0.5 + 0.5 * Math.exp(rapidHeatingVoltageTierDifference / 3.0));
                int heatingStep = Math.min(missingHeat, Math.max(200, (int) Math.ceil(missingHeat * heatingRatio)));

                // Keep a 10x passive baseline, then decay the front-loaded surcharge over the warm-up.
                long baseBlazeCost = (long) mHeatingCapacity * maxHeatingCapacity / (long) Math.pow(euTier, 3);
                double heatFactor = Math.log1p(heatingStep / 200.0) / Math.log(2);
                double tierFactor = 1 + 0.5 * (1 - Math.exp(-rapidHeatingTierSteps / 3.0));
                double remainingHeatRatio = (double) missingHeat / rapidHeatingInitialMissingHeat;
                double passiveBlazeCost = mHeatingCapacity / 5.0;
                double blazeCost = passiveBlazeCost * 10
                    + baseBlazeCost * heatFactor * tierFactor * 0.25 * remainingHeatRatio * remainingHeatRatio;
                correctBlazeCost = blazeCost >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) Math.ceil(blazeCost);
                if (!drainPyrotheumFromBlazeHatch(correctBlazeCost, true))
                    return shutDownOfMissingPyrotheum(correctBlazeCost);

                calculatedEut = availableVoltage * availableAmperage * 15 / 16;
                duration = 20;
                pendingRapidHeatingStep = heatingStep;

                return CheckRecipeResults.RapidHeating;
            }

        }.setMaxParallelSupplier(this::getTrueParallel);

    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        this.glassTier = -1;
        this.mBlazeHatch = null;
        this.setCoilLevel(HeatingCoilLevel.None);
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public int getCoilHeat() {
        return (int) getCoilLevel().getHeat();
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (UpgradeItem == null) UpgradeItem = GTCMItemList.SwelegfyrUpgradeChip.get(1);
    }

    protected boolean setRemoveBlaze() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        String[][] StructureDef = controllerTier > 1 ? shapeBlazeT2 : shapeBlazeT1;
        Block Air = Blocks.air;
        Block Blaze = TFFluids.fluidPyrotheum.getBlock();
        boolean isFlipped = this.getFlip()
            .isHorizontallyFlipped();
        int BlazeAmount = controllerTier > 1 ? 168000 : 72000;
        int OffSetX = BlazeHorizontalOffSet;
        int OffSetY = BlazeVerticalOffSet;
        int OffSetZ = BlazeDepthOffSet;
        // if (!checkStructure(true)) return false;
        if (!isBlazeFinishSet) {
            if (!drainPyrotheumFromBlazeHatch(BlazeAmount, false)) return false;
            drainPyrotheumFromBlazeHatch(BlazeAmount, true);
            isBlazeFinishClear = false;
            TSTUtils
                .setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, isFlipped, "Z", Blaze);
            isBlazeFinishSet = true;
            return true;
        } else if (!isBlazeFinishClear) {
            // clear will not return existing pyrotheum
            isBlazeFinishSet = false;
            TSTUtils
                .setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, isFlipped, "Z", Air);
            isBlazeFinishClear = true;
            return true;
        }
        return false;
    }

    protected boolean checkBlaze() {
        // If blaze illegal return true
        if (isBlazeFinishClear || !isBlazeFinishSet) {
            return !setRemoveBlaze();
        }
        return false;
    }

    @Override
    protected void outputAfterRecipe() {
        if (inRapidHeating) {
            // Grant heat only after the fake recipe finishes, so interrupted warm-up cycles give no free heat.
            mHeatingCapacity = numericalApproximation(mHeatingCapacity, maxHeatingCapacity, pendingRapidHeatingStep);
            pendingRapidHeatingStep = 0;
            inRapidHeating = false;
        }
        super.outputAfterRecipe();
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (runningTick % 20 == 0) {
            // Updates every sec
            if (!inPassiveMode) {
                correctBlazeCost = 1000;
                if (!drainPyrotheumFromBlazeHatch(correctBlazeCost, true)) {
                    stopMachineOfMissingPyrotheum(correctBlazeCost);
                    return false;
                }
            } else if (!inRapidHeating) {
                correctBlazeCost = mHeatingCapacity / 5;
                if (!drainPyrotheumFromBlazeHatch(correctBlazeCost, true)) {
                    stopMachineOfMissingPyrotheum(correctBlazeCost);
                    return false;
                }
                mHeatingCapacity = numericalApproximation(mHeatingCapacity, maxHeatingCapacity, 5);
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
            // Update controller
            if (controllerTier == 1 && aTick % 20 == 0) {
                ItemStack ControllerSlot = this.getControllerSlot();
                if (GTUtility.areStacksEqual(UpgradeItem, ControllerSlot)) {
                    controllerTier = 2;
                    mInventory[1] = ItemUtils.depleteStack(ControllerSlot, 1);
                    markDirty();
                    // schedule a structure check
                    mUpdated = true;
                }
            }

            boolean isActive = aBaseMetaTileEntity.isActive();
            boolean isCurrentlyPassive = isActive ? inPassiveMode : isPassiveMode;

            // Heat holding mode
            if (!inRapidHeating && !isActive && isCurrentlyPassive && isHoldingHeat) {
                if (holdingHeatTicks > 0) holdingHeatTicks--;
                if (holdingHeatTicks == 0) {
                    // Holding cost is paid once per second; the mode state itself is still evaluated every tick.
                    // If missing blaze, stop holding
                    if (checkBlaze()) {
                        isHoldingHeat = false;
                        correctBlazeCost = 0;
                        return;
                    }

                    correctBlazeCost = mHeatingCapacity / 20;
                    if (!drainPyrotheumFromBlazeHatch(correctBlazeCost, true)) {
                        isHoldingHeat = false;
                        correctBlazeCost = 0;
                        return;
                    }
                    holdingHeatTicks = 20;
                }
            } else {
                holdingHeatTicks = 0;
                if (!isActive && !isHoldingHeat) correctBlazeCost = 0;
            }

            // Updates every 10 sec
            if (aTick % 200 == 0) {
                if (!inRapidHeating && (!isCurrentlyPassive || (!isActive && !isHoldingHeat))) {
                    // Not hold, loss heat
                    int targetHeat = getCoilHeat();
                    double lossRat = isActive && !isCurrentlyPassive ? 0.1 : 0.2;
                    // Normal mode inactive not cost Blaze

                    if (mHeatingCapacity != targetHeat) {
                        int delta = (int) (Math.abs(mHeatingCapacity - targetHeat) * lossRat);
                        mHeatingCapacity = numericalApproximation(mHeatingCapacity, targetHeat, delta > 0 ? delta : 1);
                    }
                }
            }
        }
    }

    /**
     * drain Blaze Pyrotheum from mBlazeHatch
     *
     * @param amount  amount of Blaze Pyrotheum
     * @param doDrain if really drain
     * @return can it drain
     */
    protected boolean drainPyrotheumFromBlazeHatch(int amount, boolean doDrain) {
        return drain(this.mBlazeHatch, new FluidStack(TFFluids.fluidPyrotheum, amount), doDrain);
    }

    protected void stopMachineOfMissingPyrotheum(int amount) {
        stopMachine(ShutDownReasonRegistry.outOfFluid(new FluidStack(TFFluids.fluidPyrotheum, amount)));
    }

    protected CheckRecipeResult shutDownOfMissingPyrotheum(int amount) {
        return SimpleResultWithText.outOfFluid(new FluidStack(TFFluids.fluidPyrotheum, amount));
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        runningTick = 0;
        inRapidHeating = false;
        pendingRapidHeatingStep = 0;
        super.stopMachine(reason);
    }

    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new TST_Gui_SwelegfyrBlastFurnace(this).withMachineModeIcons(getMachineModeIcons());
    }

    public void clickBlazeStatusButton() {
        if (checkStructure(true, getBaseMetaTileEntity()) && this.getBaseMetaTileEntity() != null
            && !this.getBaseMetaTileEntity()
                .isActive()) {
            setRemoveBlaze();
        }
    }

    public boolean isBlazeFilled() {
        return isBlazeFinishSet && !isBlazeFinishClear;
    }

    public boolean getRapidHeating() {
        return isRapidHeating;
    }

    public void setRapidHeating(boolean b) {
        isRapidHeating = b;
    }

    public boolean getHoldingHeat() {
        return isHoldingHeat;
    }

    public void setHoldingHeat(boolean b) {
        if (isHoldingHeat == b) return;
        isHoldingHeat = b;
        holdingHeatTicks = 0;
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null && !baseMetaTileEntity.isActive()) {
            correctBlazeCost = b && isPassiveMode ? mHeatingCapacity / 20 : 0;
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (controllerTier == 1 && !aPlayer.isSneaking()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (GTUtility.areStacksEqual(UpgradeItem, heldItem)) {
                controllerTier = 2;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, 1));
                if (getBaseMetaTileEntity().isServerSide()) {
                    markDirty();
                    aPlayer.inventory.markDirty();
                    // schedule a structure check
                    mUpdated = true;
                }
                if (isBlazeFinishSet) setRemoveBlaze();
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
    public void initDefaultModes(NBTTagCompound aNBT) {
        super.initDefaultModes(aNBT);
        if (aNBT == null || !aNBT.hasKey("mTier")) {
            controllerTier = 1;
        } else {
            controllerTier = aNBT.getByte("mTier");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setInteger("recipeHeatLimitation", recipeHeatLimitation);
            tag.setInteger("mHeatingCapacity", mHeatingCapacity);
            tag.setInteger("maxHeatingCapacity", maxHeatingCapacity);
            tag.setInteger("coilHeat", getCoilHeat());
            tag.setInteger("correctBlazeCost", correctBlazeCost);
            tag.setInteger("pendingRapidHeatingStep", pendingRapidHeatingStep);
            tag.setBoolean("isPassiveMode", isPassiveMode);
            tag.setBoolean("inPassiveMode", inPassiveMode);
            tag.setBoolean("inRapidHeating", inRapidHeating);
            tag.setBoolean("isHoldingHeat", isHoldingHeat);
            tag.setBoolean("machineUpdated", controllerTier == 2);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();

        boolean IsPassiveMode = tag.getBoolean("isPassiveMode");
        boolean InPassiveMode = tag.getBoolean("inPassiveMode");
        boolean InRapidHeating = tag.getBoolean("inRapidHeating");
        boolean IsHoldingHeat = tag.getBoolean("isHoldingHeat");
        boolean IsActive = tag.getBoolean("isActive");
        boolean updated = tag.getBoolean("machineUpdated");
        int rapidHeatingStep = tag.getInteger("pendingRapidHeatingStep");
        String heatChangeSuffix = getHeatChangeSuffix(
            IsActive,
            IsPassiveMode,
            InPassiveMode,
            InRapidHeating,
            IsHoldingHeat,
            tag.getInteger("mHeatingCapacity"),
            tag.getInteger("maxHeatingCapacity"),
            tag.getInteger("coilHeat"),
            rapidHeatingStep);

        // The inherited mode line uses unsynchronized client-side fields, replace it with server Waila data.
        String runningModeLabel = StatCollector.translateToLocal("tst.common.shared.machine_info.running_mode");
        currentTip.removeIf(s -> s.contains(runningModeLabel));
        currentTip.add(
            runningModeLabel + " "
                + EnumChatFormatting.WHITE
                + getMachineModeName(IsActive, IsPassiveMode, InPassiveMode, InRapidHeating, IsHoldingHeat)
                + EnumChatFormatting.RESET);

        currentTip.add(
            // spotless:off
            // #tr tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.0
            // # Recipe Heat
            // #zh_CN 配方炉温限制
            (EnumChatFormatting.YELLOW + TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.0")
                + textColon
                + EnumChatFormatting.WHITE
                + tag.getInteger("recipeHeatLimitation")) + Kelvin);
        currentTip.add(
            // #tr tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.1
            // # Current Heat
            // #zh_CN 当前炉温
            (EnumChatFormatting.YELLOW + TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.1")
                + textColon
                + EnumChatFormatting.WHITE
                + tag.getInteger("mHeatingCapacity")
                + Kelvin
                + heatChangeSuffix));
        if ((IsActive && InPassiveMode) || (!IsActive && IsPassiveMode)) {
            currentTip.add(
                // #tr tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.2
                // # Max Heat
                // #zh_CN 最高炉温
                (EnumChatFormatting.YELLOW + TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.2")
                    + textColon
                    + EnumChatFormatting.WHITE
                    + tag.getInteger("maxHeatingCapacity")) + Kelvin);
        }
        currentTip.add(
            // #tr tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.3
            // # Current Blazing Pyrotheum Cost
            // #zh_CN 当前炽焰消耗
            (EnumChatFormatting.YELLOW + TSTUtils.tr(
                "tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.3") + textColon + EnumChatFormatting.WHITE + tag.getInteger("correctBlazeCost") + " L/s"));

        if (updated) {
            // #tr tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.4
            // # {\GOLD}Machine Updated
            // #zh_CN {\GOLD}已升级至二级
            currentTip.add(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.4"));
            // spotless:on
        }

    }

    private static String getHeatChangeSuffix(boolean isActive, boolean isPassiveMode, boolean inPassiveMode,
        boolean inRapidHeating, boolean isHoldingHeat, int currentHeat, int maxHeat, int coilHeat,
        int rapidHeatingStep) {
        boolean isCurrentlyPassive = isActive ? inPassiveMode : isPassiveMode;

        if (inRapidHeating && rapidHeatingStep > 0) {
            return formatHeatChange(true, rapidHeatingStep);
        }
        if (isActive && isCurrentlyPassive) {
            int passiveHeatingStep = Math.min(5, Math.max(maxHeat - currentHeat, 0));
            return passiveHeatingStep > 0 ? formatHeatChange(true, passiveHeatingStep) : "";
        }
        if (!isActive && isCurrentlyPassive && isHoldingHeat) {
            return formatHeatChange(false, 0);
        }
        if (!isCurrentlyPassive || (!isActive && !isHoldingHeat)) {
            int heatDifference = currentHeat - coilHeat;
            if (heatDifference == 0) return "";
            double lossRatio = isActive && !isCurrentlyPassive ? 0.1 : 0.2;
            return formatHeatChange(heatDifference < 0, Math.max((int) (Math.abs(heatDifference) * lossRatio), 1));
        }
        return "";
    }

    private static String formatHeatChange(boolean increasing, int heatChange) {
        return EnumChatFormatting.WHITE + " "
            + (increasing ? "+" : "-")
            + " ["
            + (increasing ? EnumChatFormatting.RED : EnumChatFormatting.BLUE)
            + heatChange
            + Kelvin
            + EnumChatFormatting.WHITE
            + "]"
            + EnumChatFormatting.RESET;
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA
            + TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.0")
            + textColon
            + EnumChatFormatting.GOLD
            + recipeHeatLimitation
            + Kelvin;
        ret[origin.length + 1] = EnumChatFormatting.AQUA
            + TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.waila.sbf.1")
            + textColon
            + EnumChatFormatting.GOLD
            + mHeatingCapacity
            + Kelvin
            + getHeatChangeSuffix(
                getBaseMetaTileEntity().isActive(),
                isPassiveMode,
                inPassiveMode,
                inRapidHeating,
                isHoldingHeat,
                mHeatingCapacity,
                maxHeatingCapacity,
                getCoilHeat(),
                pendingRapidHeatingStep);
        return ret;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mTier", controllerTier);
        aNBT.setInteger("mGlass", glassTier);
        aNBT.setByte("mMode", (byte) machineMode);
        aNBT.setInteger("mHeatingCapacity", mHeatingCapacity);
        aNBT.setBoolean("isBlazeFinishSet", isBlazeFinishSet);
        aNBT.setBoolean("isBlazeFinishClear", isBlazeFinishClear);
        aNBT.setBoolean("isPassiveMode", isPassiveMode);
        aNBT.setBoolean("inPassiveMode", inPassiveMode);
        aNBT.setBoolean("isRapidHeating", isRapidHeating);
        aNBT.setBoolean("inRapidHeating", inRapidHeating);
        aNBT.setInteger("pendingRapidHeatingStep", pendingRapidHeatingStep);
        aNBT.setBoolean("isHoldingHeat", isHoldingHeat);
        aNBT.setInteger("previousRecipeCode", previousRecipeCode);
        aNBT.setInteger("previousRecipeVoltageTier", previousRecipeVoltageTier);
        aNBT.setInteger("correctBlazeCost", correctBlazeCost);
        aNBT.setInteger("recipeHeatLimitation", recipeHeatLimitation);
        aNBT.setLong("runningTick", runningTick);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        previousRecipe = null;
        controllerTier = aNBT.getByte("mTier");
        glassTier = aNBT.getInteger("mGlass");
        machineMode = aNBT.getByte("mMode");
        mHeatingCapacity = aNBT.getInteger("mHeatingCapacity");
        isBlazeFinishSet = aNBT.getBoolean("isBlazeFinishSet");
        isBlazeFinishClear = aNBT.getBoolean("isBlazeFinishClear");
        isPassiveMode = aNBT.getBoolean("isPassiveMode");
        inPassiveMode = aNBT.getBoolean("inPassiveMode");
        isRapidHeating = aNBT.getBoolean("isRapidHeating");
        inRapidHeating = aNBT.getBoolean("inRapidHeating");
        pendingRapidHeatingStep = aNBT.getInteger("pendingRapidHeatingStep");
        isHoldingHeat = aNBT.getBoolean("isHoldingHeat");
        previousRecipeCode = aNBT.getInteger("previousRecipeCode");
        previousRecipeVoltageTier = aNBT.getInteger("previousRecipeVoltageTier");
        correctBlazeCost = aNBT.getInteger("correctBlazeCost");
        recipeHeatLimitation = aNBT.getInteger("recipeHeatLimitation");
        runningTick = aNBT.getLong("runningTick");
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (controllerTier > 1) aNBT.setByte("mTier", controllerTier);
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            side,
            facing,
            active,
            casingTexturePages[115][MetaBlockCasing01.getTextureIndexInPage(15)],
            TexturesGtBlock.oMCAAdvancedEBF,
            TexturesGtBlock.oMCAAdvancedEBFGlow,
            TexturesGtBlock.oMCAAdvancedEBFActive,
            TexturesGtBlock.oMCAAdvancedEBFActiveGlow);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.machine_type
        // # Blast Furnace
        // #zh_CN 工业高炉
        tt.addMachineType(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.machine_type"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.controller
            // # Controller block for the Swelegfyr Blast Furnace
            // #zh_CN 熯焱高炉的控制方块
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.controller"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.01
            // # {\ITALIC}{\GOLD}Blaze Pyrotheum feeds celestial forges. Soulsteel wrought, flame-bound cosmic rite.
            // #zh_CN {\ITALIC}{\GOLD}炽焱为薪, 焚天作工. 铸形炼魄, 器道同烽.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.01"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.02
            // # Excels at continuous processing, but also handles conventional recipes.
            // #zh_CN 专精持续加工, 也可处理常规配方.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.02"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.03
            // # Blast furnace temp gradually increases in Passive Mode.
            // #zh_CN 当处于被动模式时炉温会缓慢升高.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.03"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.04
            // # Recipe changes recalculate extra heat based on structure tier.
            // #zh_CN 切换配方会按结构等级重算额外炉温.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.04"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.05
            // # Power consumption decreases by 10% per 900K above recipe temperature threshold.
            // #zh_CN 炉温每高出配方900K, 耗电减少10%.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.05"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.06
            // # Max parallels: 4096x in Normal Mode; 256x in Passive Mode.
            // #zh_CN 最大并行: 普通模式4096x, 被动模式256x.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.06"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.07
            // # Processes recipes at 390% speed; Glass tier restricts Energy Hatch tier.
            // #zh_CN 配方处理速度为390%, 玻璃等级限制能源仓等级.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.07"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.08
            // # Upgrade machine and build additional structure to unlock additional functions.
            // #zh_CN 升级机器并搭建附加结构以解锁更多功能.
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.08"))
            .addInfo(textSpace)
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.09
            // # {\YELLOW}Do not open the cabin door while the machine is running!
            // #zh_CN {\YELLOW}禁止在机器运行时打开舱门!
            .addInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.info.09"))
            .addStructureInfo(Text_SeparatingLine)
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.01
            // # {\GOLD}Heat {\WHITE}Upper Limit:
            // #zh_CN {\GOLD}炉温{\WHITE}上限:
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.01"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.02
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Normal Mode: {\AQUA}Coil Heat
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}普通模式: {\AQUA}线圈炉温
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.02"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.03
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Passive Mode: {\AQUA}Coil Heat {\WHITE}^ {\GOLD}1.08
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}被动模式: {\AQUA}线圈炉温 {\WHITE}^ {\GOLD}1.08
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.03"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.04
            // # {\GOLD}Blaze Pyrotheum {\WHITE}Consumption:
            // #zh_CN {\GOLD}炽焱{\WHITE}消耗:
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.04"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.05
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Normal Mode: {\GOLD}1000 {\WHITE}L/s
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}普通模式: {\GOLD}1000 {\WHITE}L/s
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.05"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.06
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Passive Mode: {\AQUA}Current Heat {\WHITE}/ {\GOLD}5 {\WHITE}L/s
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}被动模式: {\AQUA}当前炉温 {\WHITE}/ {\GOLD}5 {\WHITE}L/s
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.06"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.07
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Rapid Heating Mode: {\AQUA}Current Heat {\WHITE}x {\AQUA}Max Heat {\WHITE}/ {\AQUA}Input Voltage Tier {\WHITE}^ {\GOLD}3 {\WHITE}(affected by remaining heat)
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}升温模式: {\AQUA}当前炉温 {\WHITE}x {\AQUA}最高炉温 {\WHITE}/ {\AQUA}输入电压等级 {\WHITE}^ {\GOLD}3 {\WHITE}(受剩余温差影响)
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.07"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.08
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Thermal Retention Mode: {\AQUA}Current Heat {\WHITE}/ {\GOLD}20 {\WHITE}L/s
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}保温模式: {\AQUA}当前炉温 {\WHITE}/ {\GOLD}20 {\WHITE}L/s
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.08"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.09
            // # {\GOLD}Heat Capacity {\WHITE}Change:
            // #zh_CN {\GOLD}炉温{\WHITE}改变:
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.09"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.10
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Passive Mode: {\GOLD}5 {\WHITE}K/s [{\RED}Increasing{\WHITE}]
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}被动模式: {\GOLD}5 {\WHITE}K/s [{\RED}升温{\WHITE}]
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.10"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.11
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Rapid Heating Mode: (Max Heat - Current Heat) x 20% K/s (affected by input voltage) [{\RED}Increasing{\WHITE}]
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}升温模式: (最高炉温 - 当前炉温) x 20% K/s (受输入电压影响) [{\RED}升温{\WHITE}]
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.11"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.12
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Normal Mode: {\AQUA}Current Heat {\WHITE}x {\GOLD}10% {\WHITE}K/s [{\BLUE}Decreasing{\WHITE}] (Minimum: Coil Heat)
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}普通模式: {\AQUA}当前炉温 {\WHITE}x {\GOLD}10% {\WHITE}K/s [{\BLUE}降温{\WHITE}], 不低于线圈炉温
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.12"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.13
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Shutdown: {\AQUA}Current Heat {\WHITE}x {\GOLD}20% {\WHITE}K/s [{\BLUE}Decreasing{\WHITE}] (Minimum: Coil Heat)
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}关机状态: {\AQUA}当前炉温 {\WHITE}x {\GOLD}20% {\WHITE}K/s [{\BLUE}降温{\WHITE}], 不低于线圈炉温
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.13"))
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.14
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Recipe Change: Tier I {\GOLD}0%{\WHITE}, Tier II {\GOLD}25%-75% {\WHITE}(scaled by recipe voltage) [{\BLUE}Decreasing{\WHITE}]
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}切换配方: 一级结构 {\GOLD}0%{\WHITE}, 二级结构 {\GOLD}25%-75% {\WHITE}(按配方电压折算) [{\BLUE}降温{\WHITE}]
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.14"))
            .addStructureInfo(Text_SeparatingLine)
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.15
            // # Attention: Pyrotheum's dedicated input hatch location will be changed when upgrade machine.
            // #zh_CN 注意: 升级结构后炽焱专用的输入仓位置会发生变化.
            .addStructureInfo(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.15"))
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addController(textFrontBottom)
            .addInputHatch(getBlueprintWithDot(1), 1)
            .addOutputHatch(getBlueprintWithDot(1), 1)
            .addInputBus(getBlueprintWithDot(1), 1)
            .addOutputBus(getBlueprintWithDot(1), 1)
            .addEnergyHatch(getBlueprintWithDot(2), 2)
            // #tr tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.16
            // # Pyrotheum's dedicated input hatch
            // #zh_CN 炽焱专用的输入仓
            .addOtherStructurePart(TSTUtils.tr("tst.common.machine.SwelegfyrBlastFurnace.tooltip.structure.16"), getBlueprintWithDot(3), 3)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    public boolean addBlazeHatch(IGregTechTileEntity aTileEntity, short aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mBlazeHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    // endregion

}
