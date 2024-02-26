package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Mods.Chisel;
import static gregtech.api.enums.Textures.BlockIcons;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.io.File;
import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.Sets;
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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.*;
import gregtech.common.GT_Pollution;

public class GT_TileEntity_MegaBrickedBlastFurnace extends GTCM_MultiMachineBase<GT_TileEntity_MegaBrickedBlastFurnace>
    implements ISurvivalConstructable {

    // 3600 seconds in an hour, 8 hours, 20 ticks in a second.
    private static final double max_efficiency_time_in_ticks = 3600d * 8d * 20d;
    private static final double maximum_fuelEfficiency = 8d;

    // Current efficiency
    private double fuelEfficiency = 1;
    private long running_time = 0;

    // coke coal
    private static ItemStack cokeCoal;
    private static ItemStack cokeCoalBlock;

    private boolean usePrimitiveRecipes = false;

    // needed to calculate fuel/material ratio
    private static Set<ItemWithDamage> fuels;

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
        cokeCoalBlock = GT_ModHandler.getModItem("Railcraft", "cube", 1);
        if (cokeCoalBlock == null) cokeCoalBlock = Materials.Coal.getBlocks(1);

        ItemStack charCoal = Materials.Charcoal.getGems(1);
        ItemStack charCoalBlock = Materials.Charcoal.getBlocks(1);
        ItemStack dustCoal = Materials.Coal.getDust(1);
        ItemStack dustCharCoal = Materials.Charcoal.getDust(1);
        ItemStack cactusCoke = GT_ModHandler.getModItem("miscutils", "itemCactusCoke", 1);
        ItemStack cactusCharCoal = GT_ModHandler.getModItem("miscutils", "itemCactusCharcoal", 1);
        ItemStack sugarCharCoal = GT_ModHandler.getModItem("miscutils", "itemSugarCharcoal", 1);
        ItemStack sugarCoke = GT_ModHandler.getModItem("miscutils", "itemSugarCoke", 1);

        fuels = Sets.newHashSet(
            new ItemWithDamage(cokeCoal.getItem(), cokeCoal.getItemDamage()),
            new ItemWithDamage(cokeCoalBlock.getItem(), cokeCoal.getItemDamage()),
            new ItemWithDamage(cokeCoal.getItem(), cokeCoal.getItemDamage()),
            new ItemWithDamage(dustCoal.getItem(), dustCoal.getItemDamage()),
            new ItemWithDamage(charCoal.getItem(), charCoal.getItemDamage()),
            new ItemWithDamage(charCoalBlock.getItem(), charCoalBlock.getItemDamage()),
            new ItemWithDamage(dustCharCoal.getItem(), dustCharCoal.getItemDamage()),
            new ItemWithDamage(cactusCoke.getItem(), cactusCoke.getItemDamage()),
            new ItemWithDamage(cactusCharCoal.getItem(), cactusCharCoal.getItemDamage()),
            new ItemWithDamage(sugarCharCoal.getItem(), sugarCharCoal.getItemDamage()),
            new ItemWithDamage(sugarCoke.getItem(), sugarCoke.getItemDamage()));

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
    private static final int HORIZONTAL_DIRT_METAID = 9;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_MegaBrickedBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_TileEntity_MegaBrickedBlastFurnace>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure_string)
        .addElement(
            'C',
            (Chisel.isModLoaded() && Block.getBlockFromName(Chisel.ID + ":dirt") != null)
                ? ofBlock(Block.getBlockFromName(Chisel.ID + ":dirt"), HORIZONTAL_DIRT_METAID)
                : ofBlock(Blocks.dirt, 0))
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
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_06)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_07)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_08)
            .addInfo(TextLocalization.Tooltip_MegaBrickedBlastFurnace_09)
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addSeparator()
            .addStructureInfo(TextLocalization.textMegaBrickedBlastFurnaceTips)
            .addInputBus(TextLocalization.textMegaBrickedBlastFurnaceLocation, 1)
            .addOutputBus(TextLocalization.textMegaBrickedBlastFurnaceLocation, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        usePrimitiveRecipes = !usePrimitiveRecipes;
        GT_Utility.sendChatToPlayer(
            aPlayer,
            usePrimitiveRecipes ? "Now Bricked DTPF accepts primitive blast furnace recipes"
                : "Now Bricked DTPF only accepts iron/wrought iron and charcoal");
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
        return 30000;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaBrickedBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private GT_Recipe findRecipe(ArrayList<ItemStack> inputList) {
        RecipeMap<RecipeMapBackend> primitiveBlastRecipes = RecipeMaps.primitiveBlastRecipes;
        ItemStack[] inputArr = inputList.toArray(new ItemStack[inputList.size()]);
        return primitiveBlastRecipes.findRecipe(null, false, 0, null, inputArr);
    }

    /*
     * calculate parallelism, material/fuel ratio
     * if there're multiple materials, use that of the largest amount
     */
    static MaterialConsumption calculateMaterialConsumption(GT_Recipe recipe, List<ItemStack> inputList) {
        // merge stacks
        MaterialConsumption result = new MaterialConsumption();
        Map<ItemWithDamage, Integer> itemCountInput = new HashMap<>();
        Map<ItemWithDamage, Integer> recipeItems = new HashMap<>();

        int recipefuelAmount = 0;
        ItemWithDamage fuelItem = null;
        for (ItemStack ingredient : recipe.mInputs) {
            if (ingredient != null) {
                ItemWithDamage itemWithDamage = new ItemWithDamage(ingredient.getItem(), ingredient.getItemDamage());
                recipeItems.put(itemWithDamage, ingredient.stackSize);
                if (fuels.contains(itemWithDamage)) {
                    recipefuelAmount = ingredient.stackSize;
                    fuelItem = itemWithDamage;
                }
            }
        }

        for (ItemStack ingredient : recipe.mInputs) {
            if (ingredient != null) {
                ItemWithDamage itemWithDamage = new ItemWithDamage(ingredient.getItem(), ingredient.getItemDamage());
                if (!fuels.contains(itemWithDamage)) {
                    result.originalRatio.put(itemWithDamage, ingredient.stackSize / (double) recipefuelAmount);
                }
            }
        }

        for (ItemStack itemStack : inputList) {
            ItemWithDamage itemWithDamage = new ItemWithDamage(itemStack.getItem(), itemStack.getItemDamage());
            int itemCount = itemCountInput.getOrDefault(itemWithDamage, 0) + itemStack.stackSize;
            itemCountInput.put(itemWithDamage, itemCount);
        }

        // get parallelism
        int fuelAmount = itemCountInput.get(fuelItem);
        int parallelism = Integer.MAX_VALUE;
        for (ItemWithDamage item : recipeItems.keySet()) {
            if (item != null) {
                parallelism = Math.min(itemCountInput.get(item) / recipeItems.get(item), parallelism);
                if (!fuels.contains(item)) {
                    result.actualRatio.put(item, itemCountInput.get(item) / (double) fuelAmount);
                }
            }
        }
        result.parallelism = parallelism;
        result.fuelToBeConsumed = new ItemStack(fuelItem.item, fuelAmount, fuelItem.damage);

        for (ItemStack ingredient : recipe.mInputs) {
            if (ingredient != null) {
                ItemWithDamage itemWithDamage = new ItemWithDamage(ingredient.getItem(), ingredient.getItemDamage());
                if (!fuels.contains(itemWithDamage)) {
                    result.materialToBeConsumed
                        .add(new ItemStack(ingredient.getItem(), parallelism * ingredient.stackSize));
                }
            }
        }
        return result;
    }

    public ItemStack[] getPrimitiveOutputs(GT_Recipe recipe, int parallelism) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack output : recipe.mOutputs) {
            if (output != null) {
                int count = output.stackSize * parallelism;
                while (count > 0) {
                    ItemStack copy = output.copy();
                    copy.stackSize = Math.min(64, count);
                    result.add(copy);
                    count -= 64;
                }
            }
        }
        return result.toArray(new ItemStack[0]);
    }

    public void consumePrimitiveInput(MaterialConsumption materialConsumption, List<ItemStack> inputList) {
        for (int i = 0; i < inputList.size(); i++) {
            ItemStack input = inputList.get(i);
            if (input != null) {
                if (input.getItem() == materialConsumption.fuelToBeConsumed.getItem()
                    && input.getItemDamage() == materialConsumption.fuelToBeConsumed.getItemDamage()) {
                    input.stackSize = 0;
                }
            }
        }
        for (ItemStack toBeConsumed : materialConsumption.materialToBeConsumed) {
            int consumeSize = toBeConsumed.stackSize;
            while (consumeSize > 0) {
                int consumeThisTime = Math.min(64, consumeSize);
                for (int i = 0; i < inputList.size(); i++) {
                    ItemStack input = inputList.get(i);
                    if (input != null && input.getItem() == toBeConsumed.getItem()
                        && input.getItemDamage() == toBeConsumed.getItemDamage()) {
                        input.stackSize -= consumeThisTime;
                        if (input.stackSize <= 0) {
                            input.stackSize = 0;
                        }
                    }
                }
                consumeSize -= 64;
            }
        }
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (tInputList.isEmpty()) {
            resetEfficiency();
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        if (usePrimitiveRecipes) {
            GT_Recipe recipe = findRecipe(tInputList);
            if (recipe == null) return CheckRecipeResultRegistry.NO_RECIPE;
            MaterialConsumption materialConsumption = calculateMaterialConsumption(recipe, tInputList);
            double time_percentage = running_time / max_efficiency_time_in_ticks;
            time_percentage = Math.min(time_percentage, 1.0d);
            fuelEfficiency = 1 + time_percentage * 7;
            fuelEfficiency = Math.min(maximum_fuelEfficiency, fuelEfficiency);

            ItemStack fuelToBeConsumed = materialConsumption.fuelToBeConsumed;

            for (ItemWithDamage item : materialConsumption.actualRatio.keySet()) {
                double originalRatio = materialConsumption.originalRatio.get(item);
                double actualRatio = materialConsumption.actualRatio.get(item);
                if (actualRatio > originalRatio) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
            }

            int consumeTotalMaterial = 0;
            for (ItemStack itemStack : materialConsumption.materialToBeConsumed) {
                consumeTotalMaterial += itemStack.stackSize;
            }

            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            mOutputItems = getPrimitiveOutputs(recipe, materialConsumption.parallelism);
            mMaxProgresstime = calculateDuration(
                recipe.mDuration,
                0,
                consumeTotalMaterial,
                (int) (fuelToBeConsumed.stackSize * fuelEfficiency));
            consumePrimitiveInput(materialConsumption, tInputList);

            updateSlots();
            running_time += mMaxProgresstime;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        } else {
            int coalAmount = 0;
            int ironAmount = 0;
            int wroughtIronAmount = 0;
            double originalDuration = 240d * 20d;

            for (ItemStack item : tInputList) {
                if (item != null) {
                    if (item.isItemEqual(cokeCoal)) {
                        coalAmount += item.stackSize;
                    } else if (item.isItemEqual(cokeCoalBlock)) {
                        // Every coal block is considered as 10 coal
                        coalAmount += item.stackSize * 10;
                    } else if (item.isItemEqual(iron)) {
                        ironAmount += item.stackSize;
                    } else if (item.isItemEqual(wroughtIron)) {
                        wroughtIronAmount += item.stackSize;
                    }
                }
            }
            if (ironAmount == 0 && wroughtIronAmount == 0) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
            // Calculate fuel efficiency here.
            //
            // coal amount is considered as (original amount * fuelEfficiency)
            // for recipe check and duration calculation, but it doesn't affect ash output.
            //
            // If running for max_efficiency_time_in_ticks then fuelEfficiency is at maximum.
            double time_percentage = running_time / max_efficiency_time_in_ticks;
            time_percentage = Math.min(time_percentage, 1.0d);
            fuelEfficiency = 1 + time_percentage * 7;
            fuelEfficiency = Math.min(maximum_fuelEfficiency, fuelEfficiency);

            final int consumeTotalIron = Math.max((ironAmount + wroughtIronAmount) / 2, 1);
            int consumeCoal = calculateConsumeCoal(coalAmount, consumeTotalIron);

            if (coalAmount < consumeCoal) {
                resetEfficiency();
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            double WroughtIronRatio = (double) wroughtIronAmount / (ironAmount + wroughtIronAmount);

            int consumeIron = (int) (consumeTotalIron * (1 - WroughtIronRatio));
            int consumeWroughtIron = Math.min(consumeTotalIron - consumeIron, wroughtIronAmount);
            consumeIron = consumeTotalIron - consumeWroughtIron;

            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            mOutputItems = calculateOutputs(consumeTotalIron, consumeCoal);
            mMaxProgresstime = calculateDuration(
                originalDuration,
                WroughtIronRatio,
                consumeTotalIron,
                (int) (consumeCoal * fuelEfficiency));
            consumeInputs(consumeIron, consumeWroughtIron, tInputList);

            updateSlots();
            running_time += mMaxProgresstime;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
    }

    protected ItemStack[] calculateOutputs(int consumeTotalIron, int consumeCoal) {
        ItemStack outputSteel = steel.copy();
        outputSteel.stackSize = consumeTotalIron;
        ItemStack outputAsh = ash.copy();
        outputAsh.stackSize = consumeCoal / 9;
        double remain = (1.0 / (consumeCoal % 9)) * 10000;
        if (getBaseMetaTileEntity().getRandomNumber(10000) < remain) {
            outputAsh.stackSize += 1;
        }
        return new ItemStack[] { outputSteel, outputAsh };
    }

    protected int calculateDuration(double originalDuration, double wroughtIronRatio, int consumeTotalIron,
        int coalAmount) {
        return (int) (originalDuration * consumeTotalIron / ((1 + 4 * wroughtIronRatio) * Math.sqrt(coalAmount)));
    }

    protected void consumeInputs(int consumeIron, int consumeWroughtIron, ArrayList<ItemStack> tInputList) {
        int[] consumeAmounts = new int[] { consumeIron, consumeWroughtIron };
        int i;
        for (ItemStack item : tInputList) {
            if (item != null) {
                // consume all coke coal (block)
                if (item.isItemEqual(cokeCoal) || item.isItemEqual(cokeCoalBlock)) {
                    item.stackSize = 0;
                    continue;
                }
                if (item.isItemEqual(iron)) i = 0;
                else if (item.isItemEqual(wroughtIron)) i = 1;
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
        return (int) Math.max(coalAmount, consumeTotalIron * 2 / fuelEfficiency);
    }

    protected void resetEfficiency() {
        running_time = 0;
        fuelEfficiency = 1;
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
        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime)
                + EnumChatFormatting.RESET
                + "t / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime)
                + EnumChatFormatting.RESET
                + "t",
            "Ticks run: " + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(running_time)
                + EnumChatFormatting.RESET
                + ", Fuel Efficiency: "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(100 * fuelEfficiency)
                + EnumChatFormatting.RESET
                + "%" };
    }

    @Override
    public void onWorldSave(File aSaveDirectory) {
        super.onWorldSave(aSaveDirectory);
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

    // No muffler hatch needed.
    @Override
    public boolean polluteEnvironment(int aPollutionLevel) {
        GT_Pollution.addPollution(getBaseMetaTileEntity(), getPollutionPerTick(null));
        return true;
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
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("eRunningTime", running_time);
        aNBT.setDouble("eLongEfficiencyValue", fuelEfficiency);
        aNBT.setBoolean("usePrimitiveRecipes", usePrimitiveRecipes);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        running_time = aNBT.getLong("eRunningTime");
        fuelEfficiency = aNBT.getDouble("eLongEfficiencyValue");
        usePrimitiveRecipes = aNBT.getBoolean("usePrimitiveRecipes");
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    static class MaterialConsumption {

        public int parallelism = 1;
        Map<ItemWithDamage, Double> originalRatio = new HashMap<>();
        Map<ItemWithDamage, Double> actualRatio = new HashMap<>();
        List<ItemStack> materialToBeConsumed = new ArrayList<>();
        ItemStack fuelToBeConsumed;
    }

    /*
     * helper class used for hashing with items
     */
    static class ItemWithDamage {

        public Item item;
        public int damage;

        public ItemWithDamage(Item item, int damage) {
            this.item = item;
            this.damage = damage;
        }

        @Override
        public int hashCode() {
            return 100 * damage + item.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            if (!(o instanceof ItemWithDamage)) {
                return false;
            }
            ItemWithDamage o1 = (ItemWithDamage) o;
            return damage == o1.damage && item == o1.item;
        }
    }
}
