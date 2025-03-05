package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.MoreInfoCheckingInScanner;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.getBlueprintWithDot;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textColon;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontBottom;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textSpace;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
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

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

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
    private static final int BlazeHorizontalOffSet = 3;
    private static final int BlazeVerticalOffSet = 10;
    private static final int BlazeDepthOffSet = -1;
    private static final String STRUCTURE_PIECE_MAIN_T1 = "mainT1";
    private static final String STRUCTURE_PIECE_MAIN_T2 = "mainT2";
    private static final String STRUCTURE_PIECE_Blaze_T1 = "BlazeT1";
    private static final String STRUCTURE_PIECE_Blaze_T2 = "BlazeT2";
    // spotless:off
    private static final String[][] shapeMainT1 = new String[][]{
        {"           ","           ","   NNNNN   ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","  NNNNNNN  ","   NNNNN   ","           ","           "},
        {"           ","   NNNNN   ","  NHHHHHN  "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN "," NHDDDDDHN ","  NHHHHHN  ","   NNNNN   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFKKKFMF ","FMFEEEEEFMF","FMKEEEEEKMF","FMKEECEEKMF","FMKEEEEEKMF","FMFEEEEEFMF"," FMFKKKFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"           ","   J   J   ","  BFAAAFB  "," JFEEEEEFJ ","  AEEEEEH  ","  AEECEEN  ","  AEEEEEH  "," JFEEEEEFJ ","  BFAAAFB  ","   J   J   ","           "},
        {"           ","   J   J   ","  BFAAAFB  "," JFEEEEEFJ ","  AEEEEEH  ","  AEECEEN  ","  AEEEEEH  "," JFEEEEEFJ ","  BFAAAFB  ","   J   J   ","           "},
        {"           ","   HHHHH   ","  HFFFFFH  "," HFEEEEEFH "," HFEEEEEFH "," HFEECEEFH "," HFEEEEEFH "," HFEEEEEFH ","  HFFFFFH  ","   HHHHH   ","           "},
        {"   FFFFF   ","  FMMMMMF  "," FMFJJJFMF ","FMFEEEEEFMF","FMJEEEEEJMF","FMJEECEEJMF","FMJEEEEEJMF","FMFEEEEEFMF"," FMFJJJFMF ","  FMMMMMF  ","   FFFFF   "},
        {"           ","   JJ JJ   ","  JNNNNNJ  "," JNEDDDDNJ ","JJNDEEEDNJ ","  NDECEDN  ","JJNDEEEDNJ "," JNEDDDENJ ","  JNNNNNJ  ","   JJ JJ   ","           "},
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

    private static final String[][] shapeMainT2 = new String[][]{
        {"                      ","                      ","   NNNNN              ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","  NNNNNNN             ","   NNNNN              ","                      ","                      "},
        {"                      ","   NNNNN              ","  NHHHHHN             "," NHDDDDDHN       NNN  "," NHDDDDDHN      NNNNN "," NHDDDDDHN      NNNNN "," NHDDDDDHN      NNNNN "," NHDDDDDHN       NNN  ","  NHHHHHN             ","   NNNNN              ","                      "},
        {"   FFFFF              ","  FMMMMMF             "," FMFKKKFMF      GGGGG ","FMFEEEEEFMF    GGGGGGG","FMKEEEEEKMFJJJJGGGGGGG","FMKEECEEKMF    GGGGGGG","FMKEEEEEKMFJJJJGGGGGGG","FMFEEEEEFMF    GGGGGGG"," FMFKKKFMF      GGGGG ","  FMMMMMF             ","   FFFFF              "},
        {"                      ","   HHHHH              ","  HFFFFFH        HHH  "," HFEEEEEFH      HNNNH "," HFEEEEEFHHHHHHHNLLLNH"," HFEECEEFNNNNNNNNLLLNh"," HFEEEEEFHHHHHHHNLLLNH"," HFEEEEEFH      HNNNH ","  HFFFFFH        HHH  ","   HHHHH              ","                      "},
        {"                      ","   J   J              ","  BFAAAFB             "," JFEEEEEFJ      GAAAG ","  AEEEEEHAAAAAAAA   A ","  AEECEEN       - L a ","  AEEEEEHAAAAAAAA   A "," JFEEEEEFJ      GAAAG ","  BFAAAFB             ","   J   J              ","                      "},
        {"                      ","   J   J              ","  BFAAAFB             "," JFEEEEEFJ      GAAAG ","  AEEEEEHAAAAAAAA   A ","  AEECEEN       - L a ","  AEEEEEHAAAAAAAA   A "," JFEEEEEFJ      GAAAG ","  BFAAAFB             ","   J   J              ","                      "},
        {"                      ","   HHHHH              ","  HFFFFFH        HHH  "," HFEEEEEFH      HNNNH "," HFEEEEEFHHHHHHHN   NH"," HFEECEEFNNNNNNNN L Nh"," HFEEEEEFHHHHHHHN   NH"," HFEEEEEFH      HNNNH ","  HFFFFFH        HHH  ","   HHHHH              ","                      "},
        {"   FFFFF              ","  FMMMMMF             "," FMFJJJFMF       GGG  ","FMFEEEEEFMF     GAAAG ","FMJEEEEEJMFJJJJGA   AG","FMJEECEEJMF    GN L NG","FMJEEEEEJMFJJJJGA   AG","FMFEEEEEFMF     GAAAG "," FMFJJJFMF       GGG  ","  FMMMMMF             ","   FFFFF              "},
        {"                      ","   JJ JJ              ","  JNNNNNJ        JJJ  "," JNEDDDDNJ      JAAAJ ","JJNDEEEDNJJ    JA   AJ","  NDECEDN      JN L NJ","JJNDEEEDNJJ    JA   AJ"," JNEDDDENJ      JAAAJ ","  JNNNNNJ        JJJ  ","   JJ JJ              ","                      "},
        {"                      ","    J J               ","   NHNHN              ","  NEDDDEN       BAAAB "," JHDEEEDHJ      A   A ","  NDECEDN       N L N "," JHDEEEDHJ      A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","    J J               ","                      "},
        {"                      ","    J J               ","   NHNHN              ","  NEDDDEN       BAAAB "," JHDEEEDHJ      A   A ","  NDECEDN       N L N "," JHDEEEDHJ      A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","    J J               ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NHNHN              ","  NEDDDEN       BAAAB ","  HDEEEDH       A   A ","  NDECEDN       N L N ","  HDEEEDH       A   A ","  NEDDDEN       BAAAB ","   NHNHN              ","                      ","                      "},
        {"                      ","                      ","   NNNNN              ","  NNDDDNN       BAAAB ","  NDEEEDN       A   A ","  NDECEDN       N L N ","  NDEEEDN       A   A ","  NNDDDNN       BAAAB ","   NNNNN              ","                      ","                      "},
        {"                      ","   NNNNN              ","  N     N       GGGGG "," N  DDD  N     GGNNNGG"," N DEEED N     GN   NG"," N DECED N     GN L NG"," N DEEED N     GN   NG"," N  DDD  N     GGNNNGG","  N     N       GGGGG ","   NNNNN              ","                      "},
        {"                      ","   MOOOM              ","  M     M       BGIGB "," M  MMM  M     BHDIDHB"," M MEEEM M     GD   DG"," M MECEM M     II L II"," M MEEEM M     GD   DG"," M  MMM  M     BHDIDHB","  M     M       BGIGB ","   MMMMM              ","                      "},
        {"                      ","   MO~OM              ","  A     A       BNSNB "," A  KKK  A     BHDDDHB"," A KEEEK A     ND   DN"," A KECEK A     ND L DN"," A KEEEK A     ND   DN"," A  KKK  A     BHDDDHB","  A     A       BNONB ","   AAAAA              ","                      "},
        {"                      ","   MOOOM              ","  M     M       BGIGB "," M  MMM  M     BHDIDHB"," M MEEEM M     GD   DG"," M MECEM M     II L II"," M MEEEM M     GD   DG"," M  MMM  M     BHDIDHB","  M     M       BGIGB ","   MMMMM              ","                      "},
        {"                      ","   PPPPP              ","  PDDDDDP       GGGGG "," PDDDDDDDP     GGNNNGG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GNNNNNG"," PDDDDDDDP     GGNNNGG","  PDDDDDP       GGGGG ","   PPPPP              ","                      "}
    };

    private static final String[][] shapeBlazeT1 = new String[][]{
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

    private static final String[][] shapeBlazeT2 = new String[][]{
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
                .addShape(STRUCTURE_PIECE_Blaze_T1, transpose(shapeBlazeT1))
                .addShape(STRUCTURE_PIECE_Blaze_T2, transpose(shapeBlazeT2))
                .addElement('-', isAir())
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
                    'a',
                    ofChain(
                        withChannel(
                            "glass",
                            BorosilicateGlass.ofBoroGlass(
                                (byte) 0,
                                (byte) 1,
                                Byte.MAX_VALUE,
                                (te, t) -> te.glassTier = t,
                                te -> te.glassTier)),
                        isAir()))
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
                    'S',
                    buildHatchAdder(TST_SwelegfyrBlastFurnace.class).hatchClass(MTEHatchInput.class)
                        .adder(TST_SwelegfyrBlastFurnace::addBlazeHatch)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(15))
                        .dot(3)
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 15))
                .addElement('Z', ofBlock(TFFluids.fluidPyrotheum.getBlock(), 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        int structureTier = stackSize.stackSize > 1 ? 2 : 1;
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
        int structureTier = stackSize.stackSize > 1 ? 2 : 1;
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.glassTier = 0;
        this.mBlazeHatch = null;
        this.setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece("mainT" + controllerTier, baseHorizontalOffSet, baseVerticalOffSet, baseDepthOffSet))
            return false;
        if (this.mHeatingCapacity < getCoilHeat()) this.mHeatingCapacity = getCoilHeat();
        this.maxHeatingCapacity = (int) (Math.floor(Math.pow(getCoilHeat(), 1.1) / 100) * 100 + 1);

        if (glassTier < 12) {
            for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
                if (this.glassTier < mEnergyHatch.mTier) {
                    return false;
                }
            }
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
        }

        return mBlazeHatch != null;
    }

    // region Processing Logic
    byte glassTier = 0;
    byte controllerTier = 1;
    boolean isBlazeFinishSet = false;
    boolean isBlazeFinishClear = true;
    boolean isPassiveMode = false;
    boolean inPassiveMode = false;
    boolean isRapidHeating = false;
    boolean inRapidHeating = false;
    boolean isHoldingHeat = false;
    static ItemStack UpgradeItem = null;
    int previousRecipeCode = 0;
    int correctBlazeCost = 0;
    private MTEHatchInput mBlazeHatch;
    public HeatingCoilLevel coilLevel;
    private int mHeatingCapacity;
    private int maxHeatingCapacity;

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
        if (UpgradeItem == null) UpgradeItem = GTCMItemList.SwelegfgrUpgradeChip.get(1);
    }

    private boolean setRemoveBlaze() {
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
        if (isBlazeFinishClear) {
            if (!drainPyrotheumFromBlazeHatch(BlazeAmount, false)) return false;
            drainPyrotheumFromBlazeHatch(BlazeAmount, true);
            isBlazeFinishClear = false;
            TstUtils
                .setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, isFlipped, "Z", Blaze);
            isBlazeFinishSet = true;
            return true;
        } else if (isBlazeFinishSet) {
            // clear will not return existing pyrotheum
            isBlazeFinishSet = false;
            TstUtils
                .setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, isFlipped, "Z", Air);
            isBlazeFinishClear = true;
            return true;
        }
        return false;
    }

    private boolean checkBlaze() {
        // If blaze illegal return true
        if (isBlazeFinishClear || !isBlazeFinishSet) {
            return !setRemoveBlaze();
        }
        return false;
    }

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

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // only can face to X, Z direction
        // return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
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
        return isPassiveMode ? Config.SpeedBonus_PassiveMode_SwelegfyrBlastFurnace
            : Config.SpeedBonus_NormalMode_SwelegfyrBlastFurnace;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return isPassiveMode ? Config.Parallel_NormalMode_SwelegfyrBlastFurnace
            : Config.Parallel_PassiveMode_SwelegfyrBlastFurnace;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {

                // Refresh passive status
                inPassiveMode = isPassiveMode;

                // No recipe change, no heat clear, whether normal or passive
                if (!(previousRecipeCode == recipe.hashCode() || previousRecipeCode != 0)) {
                    previousRecipeCode = recipe.hashCode();
                    mHeatingCapacity = getCoilHeat();
                }

                euModifier = (float) Math.pow(0.9, Math.max(mHeatingCapacity - recipe.mSpecialValue, 0) / 1800);

                // whether recipe can be processed depends on the coil heat
                return recipe.mSpecialValue <= getCoilHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                // overclock depends on the correct heat (mHeatingCapacity)
                return super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat(TST_SwelegfyrBlastFurnace.this.mHeatingCapacity)
                    .setHeatOC(true)
                    .setHeatDiscount(true);
            }

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setEuModifier(getEuModifier());

                if (checkBlaze()) return shutDownOfMissingPyrotheum(controllerTier > 1 ? 168000 : 72000);

                if (isPassiveMode && isRapidHeating) {
                    inPassiveMode = true;
                    inRapidHeating = true;
                    return RapidHeating();
                } else {
                    inRapidHeating = false;
                    return super.process();
                }
            }

            private CheckRecipeResult RapidHeating() {
                if (mHeatingCapacity < maxHeatingCapacity) {
                    // Heating with 100 heat/s
                    int euTier = (int) Math
                        .max(0, Math.log((double) (availableVoltage * availableAmperage) / 8) / Math.log(4));
                    if (euTier < 1) stopMachine(ShutDownReasonRegistry.POWER_LOSS);

                    correctBlazeCost = mHeatingCapacity * maxHeatingCapacity / (int) Math.pow(euTier, 3);
                    if (!drainPyrotheumFromBlazeHatch(correctBlazeCost, true))
                        return shutDownOfMissingPyrotheum(correctBlazeCost);

                    calculatedEut = availableVoltage * availableAmperage * 15 / 16;
                    duration = 20;
                    mHeatingCapacity = numericalApproximation(mHeatingCapacity, maxHeatingCapacity, 100);

                    return CheckRecipeResults.RapidHeating;
                } else {
                    // Heating finish, as holding mode running
                    correctBlazeCost = mHeatingCapacity / 20;
                    if (!drainPyrotheumFromBlazeHatch(correctBlazeCost * 10, true))
                        return shutDownOfMissingPyrotheum(correctBlazeCost * 10);

                    duration = 200;
                    return CheckRecipeResults.RapidHeatFinish;
                }
            }

        }.setMaxParallelSupplier(this::getMaxParallelRecipes);

    }

    private long runningTick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (runningTick % 20 == 0) {
            // Updates every sec
            if (!isPassiveMode) {
                correctBlazeCost = 1000;
                if (!drainPyrotheumFromBlazeHatch(correctBlazeCost, true)) {
                    stopMachineOfMissingPyrotheum(correctBlazeCost);
                    return false;
                }
            } else if (inPassiveMode && !isRapidHeating) {
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
                    mInventory[1] = ItemUtils.depleteStack(ControllerSlot);
                    markDirty();
                    // schedule a structure check
                    mUpdated = true;
                }
            }

            // Updates every 10 sec
            if (aTick % 200 == 0) {
                boolean isActive = aBaseMetaTileEntity.isActive();

                // Heat holding mode
                if (!isActive && isPassiveMode && !isRapidHeating && isHoldingHeat) {
                    // If missing blaze, stop holding
                    if (checkBlaze()) {
                        mHeatingCapacity = getCoilHeat();
                        isHoldingHeat = false;
                        return;
                    }

                    correctBlazeCost = mHeatingCapacity / 20;
                    if (!drainPyrotheumFromBlazeHatch(correctBlazeCost * 10, true)) {
                        isHoldingHeat = false;
                    }
                } else if (!isPassiveMode || (!isActive && !isHoldingHeat)) {
                    // Not hold, loss heat
                    int targetHeat = getCoilHeat();
                    double lossRat = isActive && !isPassiveMode ? 0.1 : 0.2;
                    if (!isActive && !isHoldingHeat) correctBlazeCost = 0;
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
     * n1 approaches n2 with a step of n3. n3 < |n1-n2|, n1 = n2.
     *
     * @param n1 previous number
     * @param n2 target number
     * @param n3 step number, n3 > 0
     * @return modified n1
     */
    public int numericalApproximation(int n1, int n2, int n3) {
        if (n3 < 0) return n1;
        int delta = n1 - n2;
        int absDelta = Math.abs(delta);
        if (absDelta > n3) {
            n1 += (delta > 0) ? -n3 : n3;
        } else {
            n1 = n2;
        }
        return n1;
    }

    /**
     * drain Blaze Pyrotheum from mBlazeHatch
     *
     * @param amount  amount of Blaze Pyrotheum
     * @param doDrain if really drain
     * @return can it drain
     */
    private boolean drainPyrotheumFromBlazeHatch(int amount, boolean doDrain) {
        return drain(this.mBlazeHatch, new FluidStack(TFFluids.fluidPyrotheum, amount), doDrain);
    }

    private void stopMachineOfMissingPyrotheum(int amount) {
        stopMachine(ShutDownReasonRegistry.outOfFluid(new FluidStack(TFFluids.fluidPyrotheum, amount)));
    }

    private CheckRecipeResult shutDownOfMissingPyrotheum(int amount) {
        return SimpleResultWithText.outOfFluid(new FluidStack(TFFluids.fluidPyrotheum, amount));
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        runningTick = 0;
        previousRecipeCode = 0;
        mHeatingCapacity = getCoilHeat();
        super.stopMachine(reason);
    }

    @Override
    public int totalMachineMode() {
        return 2;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(UITextures.SBF_ModeBase);
        machineModeIcons.add(UITextures.SBF_ModePassive);
    }

    @Override
    public void setMachineMode(int index) {
        super.setMachineMode(index);
        isPassiveMode = index != 0;
    }

    @Override
    public String getMachineModeName(int mode) {
        // Override the origin logic, check machine mode name with the machine status
        return getMachineModeName(mode != 0, inPassiveMode, inRapidHeating);
    }

    public String getMachineModeName(boolean isPassiveMode, boolean inPassiveMode, boolean inRapidHeating) {
        // #tr Swelegfyr.modeMsg.0
        // # Normal Mode
        // #zh_CN 普通模式

        // #tr Swelegfyr.modeMsg.1
        // # Passive Mode
        // #zh_CN 被动模式

        int correctMode = 0;
        boolean isActive = this.getBaseMetaTileEntity()
            .isActive();
        String suffixKey = null;

        if (isActive) {
            // Machine active, check work status
            if (inPassiveMode) {
                correctMode = 1;
                if (inRapidHeating) {
                    suffixKey = "SBF.Msg.enableRapidHeating";
                }
            }
        } else {
            // Machine inactive, check real-time status
            if (isPassiveMode) {
                correctMode = 1;
                if (isHoldingHeat) {
                    suffixKey = "SBF.Msg.enableHoldingHeat";
                }
            }
        }

        String base = StatCollector.translateToLocal("Swelegfyr.modeMsg." + correctMode);
        if (suffixKey != null) {
            return base + "-" + StatCollector.translateToLocal(suffixKey);
        }
        return base;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(createBlazeStatusButton(builder));
        builder.widget(createRapidHeatingButton(builder));
        builder.widget(createHoldingHeatButton(builder));

    }

    public ButtonWidget createBlazeStatusButton(IWidgetBuilder<?> builder) {

        Widget button = new ButtonWidget()
            .setOnClick(
                (clickData, widget) -> {
                    if (checkStructure(true) && !this.getBaseMetaTileEntity()
                        .isActive()) setRemoveBlaze();
                })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<IDrawable> layers = new ArrayList<>();
                // Add icons per mode
                if (isBlazeFinishSet) {
                    layers.add(GTUITextures.BUTTON_STANDARD);
                    layers.add(UITextures.SBF_BlazeClear);
                } else if (isBlazeFinishClear) {
                    layers.add(GTUITextures.BUTTON_STANDARD);
                    layers.add(UITextures.SBF_BlazeSet);
                }

                return layers.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> isBlazeFinishSet, val -> isBlazeFinishSet = val),
                builder)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> isBlazeFinishClear, val -> isBlazeFinishClear = val),
                builder)
            // #tr SBF.Msg.setOrClearBlaze
            // # Place / Clear Pyrotheum
            // #zh_CN 填充/清除炽焱
            .addTooltip(StatCollector.translateToLocal("SBF.Msg.setOrClearBlaze"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(98, 91)
            .setSize(16, 16);

        return (ButtonWidget) button;
    }

    public ButtonWidget createRapidHeatingButton(IWidgetBuilder<?> builder) {
        // if controller tier = 1, not generate button
        if (controllerTier != 2) return null;

        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isPassiveMode) {
                setRapidHeating(!isRapidHeating);
                if (isRapidHeating) isHoldingHeat = false;
            }
        })
            .setPlayClickSound(isPassiveMode)
            .setBackground(() -> {
                List<IDrawable> layers = new ArrayList<>();
                // Add icons per mode
                if (isPassiveMode) {
                    if (getRapidHeating()) {
                        layers.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                        layers.add(UITextures.SBF_RapidHeating_On);
                    } else {
                        layers.add(GTUITextures.BUTTON_STANDARD);
                        layers.add(UITextures.SBF_RapidHeating_Off);
                    }
                } else {
                    layers.add(GTUITextures.BUTTON_STANDARD);
                    layers.add(UITextures.SBF_RapidHeating_Forbidden);
                }

                return layers.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::getRapidHeating, this::setRapidHeating), builder)
            // #tr SBF.Msg.enableRapidHeating
            // # Rapid Thermal Boost
            // #zh_CN 快速热增强模式
            .addTooltip(StatCollector.translateToLocal("SBF.Msg.enableRapidHeating"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(116, 91)
            .setSize(16, 16);

        return (ButtonWidget) button;
    }

    public boolean getRapidHeating() {
        return isRapidHeating;
    }

    public void setRapidHeating(boolean b) {
        isRapidHeating = b;
    }

    public ButtonWidget createHoldingHeatButton(IWidgetBuilder<?> builder) {
        // if controller tier = 1, not generate button
        if (controllerTier != 2) return null;

        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isPassiveMode) {
                setHoldingHeat(!isHoldingHeat);
                if (isHoldingHeat) isRapidHeating = false;
            }
        })
            .setPlayClickSound(isPassiveMode)
            .setBackground(() -> {
                List<IDrawable> layers = new ArrayList<>();
                // Add icons per mode
                if (isPassiveMode) {
                    if (getHoldingHeat()) {
                        layers.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                        layers.add(UITextures.SBF_HoldingHeat_On);
                    } else {
                        layers.add(GTUITextures.BUTTON_STANDARD);
                        layers.add(UITextures.SBF_HoldingHeat_Off);
                    }
                } else {
                    layers.add(GTUITextures.BUTTON_STANDARD);
                    layers.add(UITextures.SBF_HoldingHeat_Forbidden);
                }

                return layers.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::getHoldingHeat, this::setHoldingHeat), builder)
            // #tr SBF.Msg.enableHoldingHeat
            // # Thermal Retention Standby
            // #zh_CN 热保持待机模式
            .addTooltip(StatCollector.translateToLocal("SBF.Msg.enableHoldingHeat"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(134, 91)
            .setSize(16, 16);

        return (ButtonWidget) button;
    }

    public boolean getHoldingHeat() {
        return isHoldingHeat;
    }

    public void setHoldingHeat(boolean b) {
        isHoldingHeat = b;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (controllerTier == 1 && !aPlayer.isSneaking()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (GTUtility.areStacksEqual(UpgradeItem, heldItem)) {
                controllerTier = 2;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem));
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
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mTier", controllerTier);
        aNBT.setByte("mGlass", glassTier);
        aNBT.setByte("mMode", (byte) machineMode);
        aNBT.setInteger("mHeatingCapacity", mHeatingCapacity);
        aNBT.setBoolean("isBlazeFinishSet", isBlazeFinishSet);
        aNBT.setBoolean("isBlazeFinishClear", isBlazeFinishClear);
        aNBT.setBoolean("isPassiveMode", isPassiveMode);
        aNBT.setBoolean("inPassiveMode", inPassiveMode);
        aNBT.setBoolean("isRapidHeating", isRapidHeating);
        aNBT.setBoolean("inRapidHeating", inRapidHeating);
        aNBT.setBoolean("isHoldingHeat", isHoldingHeat);
        aNBT.setInteger("previousRecipeCode", previousRecipeCode);
        aNBT.setInteger("correctBlazeCost", correctBlazeCost);
        aNBT.setLong("runningTick", runningTick);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        controllerTier = aNBT.getByte("mTier");
        glassTier = aNBT.getByte("mGlass");
        machineMode = aNBT.getByte("mMode");
        mHeatingCapacity = aNBT.getInteger("mHeatingCapacity");
        isBlazeFinishSet = aNBT.getBoolean("isBlazeFinishSet");
        isBlazeFinishClear = aNBT.getBoolean("isBlazeFinishClear");
        isPassiveMode = aNBT.getBoolean("isPassiveMode");
        inPassiveMode = aNBT.getBoolean("inPassiveMode");
        isRapidHeating = aNBT.getBoolean("isRapidHeating");
        inRapidHeating = aNBT.getBoolean("inRapidHeating");
        isHoldingHeat = aNBT.getBoolean("isHoldingHeat");
        previousRecipeCode = aNBT.getInteger("previousRecipeCode");
        correctBlazeCost = aNBT.getInteger("correctBlazeCost");
        runningTick = aNBT.getLong("runningTick");
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setInteger("mHeatingCapacity", mHeatingCapacity);
            tag.setInteger("maxHeatingCapacity", maxHeatingCapacity);
            tag.setInteger("correctBlazeCost", correctBlazeCost);
            tag.setBoolean("isPassiveMode", isPassiveMode);
            tag.setBoolean("inPassiveMode", inPassiveMode);
            tag.setBoolean("inRapidHeating", inRapidHeating);
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
        boolean IsActive = this.getBaseMetaTileEntity()
            .isActive();

        if (tag.hasKey("mode")) {
            // Old one won't refresh automatically, replace it with new one
            currentTip.removeIf(s -> s.contains(StatCollector.translateToLocal("GT5U.machines.oreprocessor1")));
            currentTip
                .add(StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " " + EnumChatFormatting.WHITE
                // Status switch need special handle
                    + getMachineModeName(IsPassiveMode, InPassiveMode, InRapidHeating)
                    + EnumChatFormatting.RESET);
        }

        currentTip.add(
            // #tr Waila.SBF.1
            // # Current Heat
            // #zh_CN 当前炉温
            (EnumChatFormatting.YELLOW + TextEnums.tr("Waila.SBF.1")
                + textColon
                + EnumChatFormatting.WHITE
                + tag.getInteger("mHeatingCapacity")));
        if ((IsActive && InPassiveMode) || (!IsActive && IsPassiveMode)) {
            currentTip.add(
                // #tr Waila.SBF.2
                // # Max Heat
                // #zh_CN 最高炉温
                (EnumChatFormatting.YELLOW + TextEnums.tr("Waila.SBF.2")
                    + textColon
                    + EnumChatFormatting.WHITE
                    + tag.getInteger("maxHeatingCapacity")));
        }
        currentTip.add(
            // #tr Waila.SBF.3
            // # Current Blazing Pyrotheum Cost
            // #zh_CN 当前炽焰消耗
            (EnumChatFormatting.YELLOW + TextEnums.tr(
                "Waila.SBF.3") + textColon + EnumChatFormatting.WHITE + tag.getInteger("correctBlazeCost") + " L/s"));

    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + TextEnums.tr("Waila.SBF.1")
            + textColon
            + EnumChatFormatting.GOLD
            + mHeatingCapacity;
        return ret;
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
        // spotless:off
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_SwelegfyrBlastFurnace_MachineType
        // # Blast Furnace
        // #zh_CN 工业高炉
        tt.addMachineType(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace_MachineType"))
            // #tr Tooltip_SwelegfyrBlastFurnace_Controller
            // # Controller block for the Swelegfyr Blast Furnace
            // #zh_CN 熯焱高炉的控制方块
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace_Controller"))
            // #tr Tooltip_SwelegfyrBlastFurnace.01
            // # {\ITALIC}{\GOLD}Blaze Pyrotheum feeds celestial forges. Soulsteel wrought, flame-bound cosmic rite.
            // #zh_CN {\ITALIC}{\GOLD}炽焱为薪，焚天作工。铸形炼魄，器道同烽。
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.01"))
            // #tr Tooltip_SwelegfyrBlastFurnace.02
            // # A Volcanus blast furnace specialized in continuous processing, also capable of conventional processes.
            // #zh_CN 专注于持续加工的炽焱高炉，同时也可以进行常规处理。
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.02"))
            // #tr Tooltip_SwelegfyrBlastFurnace.03
            // # Blast furnace temp gradually increases in Passive Mode.
            // #zh_CN 当处于被动模式时炉温会缓慢升高。
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.03"))
            // #tr Tooltip_SwelegfyrBlastFurnace.04
            // # Power consumption decreases by 10% per 1800K above recipe temperature threshold.
            // #zh_CN 炉温每高出配方1800K， 耗电减少10% 。
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.04"))
            // #tr Tooltip_SwelegfyrBlastFurnace.05
            // # Glass tier restricts Energy Hatch tier.
            // #zh_CN 玻璃等级限制能源仓等级。
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.05"))
            // #tr Tooltip_SwelegfyrBlastFurnace.06
            // # Upgrade machine and build additional structure to unlock additional functions.
            // #zh_CN 升级机器并搭建附加结构以解锁更多功能。
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.06"))
            .addInfo(textSpace)
            // #tr Tooltip_SwelegfyrBlastFurnace.07
            // # {\YELLOW}Do not open the cabin door while the machine is running!
            // #zh_CN {\YELLOW}禁止在机器运行时打开舱门！
            .addInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.07"))
            .addSeparator()
            .addInfo(MoreInfoCheckingInScanner.getText())
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addStructureInfo(Text_SeparatingLine)
            // #tr Tooltip_SwelegfyrBlastFurnace.11
            // # {\GOLD}Heat{\WHITE}Upper Limit:
            // #zh_CN {\GOLD}炉温{\WHITE}上限：
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.11"))
            // #tr Tooltip_SwelegfyrBlastFurnace.12
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Normal Mode: {\AQUA}Coil Heat
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}普通模式： {\AQUA}线圈炉温
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.12"))
            // #tr Tooltip_SwelegfyrBlastFurnace.13
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Passive Mode: {\AQUA}Coil Heat {\WHITE}^ {\GOLD}1.1
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}被动模式： {\AQUA}线圈炉温 {\WHITE}^ {\GOLD}1.1
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.13"))
            // #tr Tooltip_SwelegfyrBlastFurnace.14
            // # {\GOLD}Blaze Pyrotheum {\WHITE}Consumption:
            // #zh_CN {\GOLD}炽焱{\WHITE}消耗：
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.14"))
            // #tr Tooltip_SwelegfyrBlastFurnace.15
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Normal Mode: {\GOLD}1000 {\WHITE}L/s
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}普通模式： {\GOLD}1000 {\WHITE}L/s
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.15"))
            // #tr Tooltip_SwelegfyrBlastFurnace.16
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Passive Mode: {\AQUA}Current Heat {\WHITE}/ {\GOLD}5 {\WHITE}L/s
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}被动模式： {\AQUA}当前炉温 {\WHITE}/ {\GOLD}5 {\WHITE}L/s
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.16"))
            // #tr Tooltip_SwelegfyrBlastFurnace.17
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Rapid Heating Mode: ({\AQUA}Current Heat {\WHITE}× {\AQUA}Max Heat{\WHITE}) / {\AQUA}Voltage Tier {\WHITE}^ {\GOLD}3 {\WHITE}L/s
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}升温模式： {\AQUA}当前炉温 {\WHITE}x {\AQUA}最高炉温 {\WHITE}/ {\AQUA}电压等级 {\WHITE}^ {\GOLD}3 {\WHITE}L/s
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.17"))
            // #tr Tooltip_SwelegfyrBlastFurnace.18
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Thermal Retention Mode: {\AQUA}Current Heat {\WHITE}/ {\GOLD}20 {\WHITE}L/s
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}保温模式： {\AQUA}当前炉温 {\WHITE}/ {\GOLD}20 {\WHITE}L/s
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.18"))
            // #tr Tooltip_SwelegfyrBlastFurnace.21
            // # {\GOLD}Heat Capacity {\WHITE}Change:
            // #zh_CN {\GOLD}炉温{\WHITE}改变:
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.21"))
            // #tr Tooltip_SwelegfyrBlastFurnace.22
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Passive Mode: {\GOLD}5 {\WHITE}K/s [{\RED}Increasing{\WHITE}]
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}被动模式： {\GOLD}5 {\WHITE}K/s [{\RED}升温{\WHITE}]
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.22"))
            // #tr Tooltip_SwelegfyrBlastFurnace.23
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Rapid Heating Mode: {\GOLD}100 {\WHITE}K/s [{\RED}Increasing{\WHITE}]
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}升温模式： {\GOLD}100 {\WHITE}K/s [{\RED}升温{\WHITE}]
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.23"))
            // #tr Tooltip_SwelegfyrBlastFurnace.24
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Normal Mode: {\AQUA}Current Heat {\WHITE}× {\GOLD}10% {\WHITE}K/s [{\BLUE}Decreasing{\WHITE}] (Minimum: Coil Heat)
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}普通模式： {\AQUA}当前炉温 {\WHITE}x {\GOLD}10% {\WHITE}K/s [{\BLUE}降温{\WHITE}]， 不低于线圈炉温
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.24"))
            // #tr Tooltip_SwelegfyrBlastFurnace.25
            // # {\SPACE}{\SPACE}{\SPACE}{\WHITE}Shutdown in Non-Retention Mode: {\AQUA}Current Heat {\WHITE}× {\GOLD}20% {\WHITE}K/s [{\BLUE}Decreasing{\WHITE}] (Minimum: Coil Heat)
            // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\WHITE}非保温模式关机： {\AQUA}当前炉温 {\WHITE}x {\GOLD}20% {\WHITE}K/s [{\BLUE}降温{\WHITE}]， 不低于线圈炉温
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.25"))
            .addStructureInfo(Text_SeparatingLine)
            // #tr Tooltip_SwelegfyrBlastFurnace.tooltips.structureWarn
            // # Attention: Pyrotheum input hatch location will be changed when upgrade machine.
            // #zh_CN 注意：升级结构后炽焱输入仓位置会发生变化.
            .addStructureInfo(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.tooltips.structureWarn"))
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addController(textFrontBottom)
            .addInputHatch(getBlueprintWithDot(1), 1)
            .addOutputHatch(getBlueprintWithDot(1), 1)
            .addInputBus(getBlueprintWithDot(1), 1)
            .addOutputBus(getBlueprintWithDot(1), 1)
            .addEnergyHatch(getBlueprintWithDot(2), 2)
            // #tr Tooltip_SwelegfyrBlastFurnace.31
            // # Pyrotheum input hatch
            // #zh_CN 炽焱输入仓
            .addOtherStructurePart(TextEnums.tr("Tooltip_SwelegfyrBlastFurnace.31"), getBlueprintWithDot(3), 3)
            .toolTipFinisher(ModName);
        return tt;
        // spotless:on
    }
}
