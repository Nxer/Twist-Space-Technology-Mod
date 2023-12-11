package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_MegaBrickedBlastFurnace extends GTCM_MultiMachineBase<GT_TileEntity_MegaBrickedBlastFurnace>
    implements ISurvivalConstructable {

    // coke coal
    private static ItemStack cokeCoal;
    // irons
    private static ItemStack iron;
    private static ItemStack wroughtIron;
    // steel
    private static ItemStack steel;
    // ash
    private static ItemStack ash;

    public static void initStatics() {
        cokeCoal = GT_ModHandler.getModItem("Railcraft", "fuel.coke", 1);
        if (cokeCoal == null) cokeCoal = Materials.Coal.getGems(1);
        iron = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L);
        wroughtIron = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.WroughtIron, 1L);
        steel = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L);
        ash = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L);
    }

    private static final int max_input_bus = 6;
    private static final int max_output_bus = 6;

    private static final ITexture[] FACING_SIDE = { TextureFactory.of(BlockIcons.MACHINE_CASING_DENSEBRICKS) };
    private static final ITexture[] FACING_FRONT = {
        TextureFactory.of(BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_INACTIVE) };
    private static final ITexture[] FACING_ACTIVE = {
        TextureFactory.of(BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE), TextureFactory.builder()
            .addIcon(BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
            .glow()
            .build() };
    @SuppressWarnings("SpellCheckingInspection")
    private static final String[][] structure_string = new String[][] { { "                                 ",
        "         N   N     N   N         ", "         N   N     N   N         ", "         N   N     N   N         ",
        "                                 ", "                                 ", "                                 ",
        "         N   N     N   N         ", "         N   N     N   N         ", " NNN   NNN   N     N   NNN   NNN ",
        "                                 ", "                                 ", "                                 ",
        " NNN   NNN             NNN   NNN ", "                                 ", "                                 ",
        "                                 ", "                                 ", "                                 ",
        " NNN   NNN             NNN   NNN ", "                                 ", "                                 ",
        "                                 ", " NNN   NNN             NNN   NNN " },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    N N    NbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "      NNNbbbbbNNsNNbbbbbNNN      ",
            "    ss   bCCCb     bCCCb   ss    ", "   s     N   N     N   N     s   ",
            "   s                         s   ", "  N      N   N     N   N      N  ",
            "  N      bCCCb     bCCCb      N  ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ",
            "         bCCCb     bCCCb         ", "  s      NCCCN     NCCCN      s  ",
            "  s      NCCCN     NCCCN      s  ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "                                 ", "         N   N     N   N         ", "   s     N   N     N   N     s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "                                 ", "                                 ", "   s                         s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N                 N   N   ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N       NbN       N   N   ", },
        { "                                 ", "         N   N     N   N         ", "  N      N   N     N   N      N  ",
            "         NCCCN     NCCCN         ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ",
            "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ",
            "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "  s     sbbbbbNNsNNbbbbbs     s  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { " NNN   NNN   N     N   NNN   NNN ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NNNN   NNNCCCb     bCCCNNN   NNNN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    NbN    NbbbN NbbbN", },
        { "                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCC   CCC   N     N   CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N     NsNsN     N     N  ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N    NbbbbbN    N     N  ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                N                ",
            " NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ", },
        { "                                 ", "                                 ", "  s     s               s     s  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  s     s               s     s  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                ~                ", "               NNN               ",
            "  NbbbbbNbbbbNbbbbbNbbbbNbbbbbN  ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                N                ",
            " NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N    NbbbbbN    N     N  ", },
        { " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N     NsNsN     N     N  ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { "                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCC   CCC   N     N   CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { " NNN   NNN   N     N   NNN   NNN ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NNNN   NNNCCCb     bCCCNNN   NNNN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    NbN    NbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "  s     sbbbbbNNsNNbbbbbs     s  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ",
            "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ",
            "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "                                 ", "         N   N     N   N         ", "  N      N   N     N   N      N  ",
            "         NCCCN     NCCCN         ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "                                 ", "                                 ", "   s                         s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N                 N   N   ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N       NbN       N   N   ", },
        { "                                 ", "         N   N     N   N         ", "   s     N   N     N   N     s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ",
            "         bCCCb     bCCCb         ", "  s      NCCCN     NCCCN      s  ",
            "  s      NCCCN     NCCCN      s  ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "      NNNbbbbbNNsNNbbbbbNNN      ",
            "    ss   bCCCb     bCCCb   ss    ", "   s     N   N     N   N     s   ",
            "   s                         s   ", "  N      N   N     N   N      N  ",
            "  N      bCCCb     bCCCb      N  ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    N N    NbbbN NbbbN", },
        { "                                 ", "         N   N     N   N         ", "         N   N     N   N         ",
            "         N   N     N   N         ", "                                 ",
            "                                 ", "                                 ",
            "         N   N     N   N         ", "         N   N     N   N         ",
            " NNN   NNN   N     N   NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", } };

    private static final int BRONZE_PLATED_BRICKS_INDEX = 10;
    private static final int FIREBRICK_METAID = 15;
    private boolean isMultiChunkloaded = true;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_MegaBrickedBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_TileEntity_MegaBrickedBlastFurnace>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure_string)
        .addElement('C', ofBlock(Blocks.dirt, 0))
        .addElement(
            'b',
            buildHatchAdder(GT_TileEntity_MegaBrickedBlastFurnace.class).atLeast(InputBus, OutputBus)
                .casingIndex(BRONZE_PLATED_BRICKS_INDEX)
                .dot(1)
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings1, BRONZE_PLATED_BRICKS_INDEX)))
        .addElement('N', ofBlock(GregTech_API.sBlockCasings4, FIREBRICK_METAID))
        .addElement('s', ofBlock(Blocks.brick_block, 0))
        .build();

    public GT_TileEntity_MegaBrickedBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaBrickedBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaBrickedBlastFurnace(mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_MegaBrickedBlastFurnace_MachineType)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_Controller)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_00)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_01)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_02)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_03)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_04)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_05)
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addSeparator()
            .addInputBus(TextLocalization.textMegaBrickedBlastFurnaceLocation, 1)
            .addOutputBus(TextLocalization.textMegaBrickedBlastFurnaceLocation, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            return aActive ? FACING_ACTIVE : FACING_FRONT;
        }
        return FACING_SIDE;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaBrickedBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (tInputList.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int coalAmount = 0;
        int ironAmount = 0;
        int wroughtIronAmount = 0;
        double originalDuration = 240d * 20d;

        for (ItemStack item : tInputList) {
            if (item != null) {
                if (item.isItemEqual(cokeCoal)) {
                    coalAmount += item.stackSize;
                } else if (item.isItemEqual(iron)) {
                    ironAmount += item.stackSize;
                } else if (item.isItemEqual(wroughtIron)) {
                    wroughtIronAmount += item.stackSize;
                }
            }
        }

        final int consumeTotalIron = Math.max((ironAmount + wroughtIronAmount) / 2, 1);
        int consumeCoal = calculateConsumeCoal(coalAmount, consumeTotalIron);

        if (coalAmount < consumeCoal) return CheckRecipeResultRegistry.NO_RECIPE;

        double WroughtIronRatio = (double) wroughtIronAmount / (ironAmount + wroughtIronAmount);

        int consumeIron = (int) (consumeTotalIron * (1 - WroughtIronRatio));
        int consumeWroughtIron = consumeTotalIron - consumeIron;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mOutputItems = calculateOutputs(consumeTotalIron, consumeCoal);
        mMaxProgresstime = calculateDuration(originalDuration, WroughtIronRatio, consumeTotalIron, coalAmount);
        consumeInputs(consumeIron, consumeWroughtIron, consumeCoal, tInputList);

        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    protected ItemStack[] calculateOutputs(int consumeTotalIron, int consumeCoal) {
        ItemStack outputSteel = steel.copy();
        outputSteel.stackSize = consumeTotalIron;
        ItemStack outputAsh = ash.copy();
        outputAsh.stackSize = consumeCoal / 9;
        double remain = (1.0 / consumeCoal % 9) * 10000;
        if (getBaseMetaTileEntity().getRandomNumber(10000) < remain) {
            outputAsh.stackSize += 1;
        }
        return new ItemStack[] { outputSteel, outputAsh };
    }

    protected int calculateDuration(double originalDuration, double wroughtIronRatio, int consumeTotalIron,
        int coalAmount) {
        return (int) (originalDuration * consumeTotalIron / ((1 + 3 * wroughtIronRatio) * Math.sqrt(coalAmount)));
    }

    protected void consumeInputs(int consumeIron, int consumeWroughtIron, int consumeCoal,
        ArrayList<ItemStack> tInputList) {
        int[] consumeAmounts = new int[] { consumeIron, consumeWroughtIron, consumeCoal };
        int i;
        for (ItemStack item : tInputList) {
            if (item != null) {
                if (item.isItemEqual(iron)) i = 0;
                else if (item.isItemEqual(wroughtIron)) i = 1;
                else if (item.isItemEqual(cokeCoal)) i = 2;
                else continue;

                if (consumeAmounts[i] >= item.stackSize) {
                    consumeAmounts[i] -= item.stackSize;
                    item.stackSize = 0;
                } else {
                    item.stackSize -= consumeAmounts[i];
                    consumeAmounts[i] = 0;
                }
            }
        }
    }

    protected int calculateConsumeCoal(int coalAmount, int consumeTotalIron) {
        return Math.max(coalAmount, consumeTotalIron * 2);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        // Check the main structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 16, 21, 16)) return false;

        // Item input bus check.
        if (mInputBusses.size() > max_input_bus) return false;

        // Item output bus check.
        if (mOutputBusses.size() > max_output_bus) return false;

        // All structure checks passed, return true.
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
            + EnumChatFormatting.GREEN
            + GT_Utility.formatNumbers(mProgresstime)
            + EnumChatFormatting.RESET
            + "t / "
            + EnumChatFormatting.YELLOW
            + GT_Utility.formatNumbers(mMaxProgresstime)
            + EnumChatFormatting.RESET
            + "t" };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // If machine has stopped, stop chunkloading.
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            isMultiChunkloaded = false;
        } else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !isMultiChunkloaded) {
            // Load a 3x3 area centered on controller when machine is running.
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);

            int ControllerXCoordinate = ((TileEntity) aBaseMetaTileEntity).xCoord;
            int ControllerZCoordinate = ((TileEntity) aBaseMetaTileEntity).zCoord;

            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate + 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate - 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate + 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate - 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate + 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate - 16));

            isMultiChunkloaded = true;
        }

        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 16, 21, 16);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 16, 21, 16, realBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }
}
