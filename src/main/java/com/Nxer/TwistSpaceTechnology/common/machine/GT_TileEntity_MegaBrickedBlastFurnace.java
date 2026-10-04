package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler.HorizontalDirt;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.util.TSTStructureUtility.ofVariableBlock;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.cleanroommc.modularui.drawable.UITexture;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.common.pollution.Pollution;

@SkipGenerateDescription
public class GT_TileEntity_MegaBrickedBlastFurnace extends GTCM_MultiMachineBase<GT_TileEntity_MegaBrickedBlastFurnace>
    implements ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MegaBrickedBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.HOLEFISH);
    }

    public GT_TileEntity_MegaBrickedBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaBrickedBlastFurnace(mName);
    }
    // endregion

    // region Structure
    // spotless:off
    @SuppressWarnings("SpellCheckingInspection")
    private static final String[][] structure_string = new String[][]{
        {"                                 ","         N   N     N   N         ","         N   N     N   N         ","         N   N     N   N         ","                                 ","                                 ","                                 ","         N   N     N   N         ","         N   N     N   N         "," NNN   NNN   N     N   NNN   NNN ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 "," NNN   NNN   N     N   NNN   NNN ","         N   N     N   N         ","         N   N     N   N         ","                                 ","                                 ","                                 ","         N   N     N   N         ","         N   N     N   N         ","         N   N     N   N         ","                                 "},
        {"         N   N     N   N         ","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","         N   N     N   N         ","                                 ","         N   N     N   N         ","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","NbbbN NbbNCCCb     bCCCNbbN NbbbN"," CCC   CCC   N     N   CCC   CCC "," CCC   CCC             CCC   CCC "," CCC   CCC             CCC   CCC ","NbbbN NbbbN           NbbbN NbbbN","                                 ","                                 ","                                 ","                                 ","                                 ","NbbbN NbbbN           NbbbN NbbbN"," CCC   CCC             CCC   CCC "," CCC   CCC             CCC   CCC "," CCC   CCC   N     N   CCC   CCC ","NbbbN NbbNCCCb     bCCCNbbN NbbbN","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","         N   N     N   N         ","                                 ","         N   N     N   N         ","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","         N   N     N   N         "},
        {"         N   N     N   N         ","         bCCCb     bCCCb         ","      NNNbbbbbNNsNNbbbbbNNN      ","    ss   bCCCb     bCCCb   ss    ","   s     N   N     N   N     s   ","   s                         s   ","  N      N   N     N   N      N  ","  N      bCCCb     bCCCb      N  ","  N     sbbbbbNNsNNbbbbbs     N  ","NbbbN NbbNCCCb     bCCCNbbN NbbbN"," CbC   CbC   N     N   CbC   CbC "," CbC   CbC             CbC   CbC "," CbC   CbC             CbC   CbC ","NbbbN NbbbN           NbbbN NbbbN","  N     N               N     N  ","  N     N               N     N  ","  s     s               s     s  ","  N     N               N     N  ","  N     N               N     N  ","NbbbN NbbbN           NbbbN NbbbN"," CbC   CbC             CbC   CbC "," CbC   CbC             CbC   CbC "," CbC   CbC   N     N   CbC   CbC ","NbbbN NbbNCCCb     bCCCNbbN NbbbN","  N     sbbbbbNNsNNbbbbbs     N  ","  N      bCCCb     bCCCb      N  ","  N      N   N     N   N      N  ","   s                         s   ","   s     N   N     N   N     s   ","    ss   bCCCb     bCCCb   ss    ","      NNNbbbbbNNsNNbbbbbNNN      ","         bCCCb     bCCCb         ","         N   N     N   N         "},
        {"         N   N     N   N         ","         bCCCb     bCCCb         ","    ss   bCCCb     bCCCb   ss    ","         bCCCb     bCCCb         ","  s      NCCCN     NCCCN      s  ","  s      NCCCN     NCCCN      s  ","         NCCCN     NCCCN         ","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN"," CCCCCCCCC   N     N   CCCCCCCCC "," CCCCCCCCC             CCCCCCCCC "," CCCCCCCCC             CCCCCCCCC ","NbbbNNNbbbN           NbbbNNNbbbN","                                 ","                                 ","                                 ","                                 ","                                 ","NbbbNNNbbbN           NbbbNNNbbbN"," CCCCCCCCC             CCCCCCCCC "," CCCCCCCCC             CCCCCCCCC "," CCCCCCCCC   N     N   CCCCCCCCC ","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","         NCCCN     NCCCN         ","  s      NCCCN     NCCCN      s  ","  s      NCCCN     NCCCN      s  ","         bCCCb     bCCCb         ","    ss   bCCCb     bCCCb   ss    ","         bCCCb     bCCCb         ","         N   N     N   N         "},
        {"                                 ","         N   N     N   N         ","   s     N   N     N   N     s   ","  s      NCCCN     NCCCN      s  ","                                 ","                                 ","                                 ","         NCCCN     NCCCN         ","         N   N     N   N         "," NNN   NNN   N     N   NNN   NNN ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," NNN   NNN   N     N   NNN   NNN ","         N   N     N   N         ","         NCCCN     NCCCN         ","                                 ","                                 ","                                 ","  s      NCCCN     NCCCN      s  ","   s     N   N     N   N     s   ","         N   N     N   N         ","                                 "},
        {"                                 ","                                 ","   s                         s   ","  s      NCCCN     NCCCN      s  ","                                 ","                                 ","                                 ","         NCCCN     NCCCN         ","                                 ","   N   N                 N   N   ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   ","   N   N                 N   N   ","                                 ","                                 ","                                 ","                                 ","                                 ","   N   N                 N   N   ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   ","   N   N                 N   N   ","                                 ","         NCCCN     NCCCN         ","                                 ","                                 ","                                 ","  s      NCCCN     NCCCN      s  ","   s                         s   ","                                 ","                                 "},
        {"                                 ","         N   N     N   N         ","  N      N   N     N   N      N  ","         NCCCN     NCCCN         ","                                 ","                                 ","                                 ","         NCCCN     NCCCN         ","         N   N     N   N         "," NNN   NNN   N     N   NNN   NNN ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," NNN   NNN   N     N   NNN   NNN ","         N   N     N   N         ","         NCCCN     NCCCN         ","                                 ","                                 ","                                 ","         NCCCN     NCCCN         ","  N      N   N     N   N      N  ","         N   N     N   N         ","                                 "},
        {"         N   N     N   N         ","         bCCCb     bCCCb         ","  N      bCCCb     bCCCb      N  ","         bCCCb     bCCCb         ","         NCCCN     NCCCN         ","         NCCCN     NCCCN         ","         NCCCN     NCCCN         ","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN"," CCCCCCCCC   N     N   CCCCCCCCC "," CCCCCCCCC             CCCCCCCCC "," CCCCCCCCC             CCCCCCCCC ","NbbbNNNbbbN           NbbbNNNbbbN","                                 ","                                 ","                                 ","                                 ","                                 ","NbbbNNNbbbN           NbbbNNNbbbN"," CCCCCCCCC             CCCCCCCCC "," CCCCCCCCC             CCCCCCCCC "," CCCCCCCCC   N     N   CCCCCCCCC ","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN","         bCCCb     bCCCb         ","         bCCCb     bCCCb         ","         NCCCN     NCCCN         ","         NCCCN     NCCCN         ","         NCCCN     NCCCN         ","         bCCCb     bCCCb         ","  N      bCCCb     bCCCb      N  ","         bCCCb     bCCCb         ","         N   N     N   N         "},
        {"         N   N     N   N         ","         bCCCb     bCCCb         ","  N     sbbbbbNNsNNbbbbbs     N  ","         bCCCb     bCCCb         ","         N   N     N   N         ","                                 ","         N   N     N   N         ","         bCCCb     bCCCb         ","  s     sbbbbbNNsNNbbbbbs     s  ","NbbbN NbbNCCCb     bCCCNbbN NbbbN"," CbC   CbC   N     N   CbC   CbC "," CbC   CbC             CbC   CbC "," CbC   CbC             CbC   CbC ","NbbbN NbbbN           NbbbN NbbbN","  N     N               N     N  ","  N     N               N     N  ","  s     s               s     s  ","  N     N               N     N  ","  N     N               N     N  ","NbbbN NbbbN           NbbbN NbbbN"," CbC   CbC             CbC   CbC "," CbC   CbC             CbC   CbC "," CbC   CbC   N     N   CbC   CbC ","NbbbN NbbNCCCb     bCCCNbbN NbbbN","  s     sbbbbbNNsNNbbbbbs     s  ","         bCCCb     bCCCb         ","         N   N     N   N         ","                                 ","         N   N     N   N         ","         bCCCb     bCCCb         ","  N     sbbbbbNNsNNbbbbbs     N  ","         bCCCb     bCCCb         ","         N   N     N   N         "},
        {" NNN   NNN   N     N   NNN   NNN ","NbbbN NbbNCCCb     bCCCNbbN NbbbN","NbbbN NbbNCCCb     bCCCNbbN NbbbN","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN"," NNN   NN    N     N    NN   NNN ","   N   N                 N   N   "," NNN   NN    N     N    NN   NNN ","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN","NbbbN NbbNCCCb     bCCCNbbN NbbbN","NNNN   NNNCCCb     bCCCNNN   NNNN"," CCC   CCC   N     N   CCC   CCC "," CCC   CCC             CCC   CCC "," CCC   CCC             CCC   CCC ","NbbbN NbbbN           NbbbN NbbbN","                                 ","                                 ","                                 ","                                 ","                                 ","NbbbN NbbbN           NbbbN NbbbN"," CCC   CCC             CCC   CCC "," CCC   CCC             CCC   CCC "," CCC   CCC   N     N   CCC   CCC ","NNNN   NNNCCCb     bCCCNNN   NNNN","NbbbN NbbNCCCb     bCCCNbbN NbbbN","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN"," NNN   NN    N     N    NN   NNN ","   N   N                 N   N   "," NNN   NN    N     N    NN   NNN ","NbbbNNNbbNCCCb     bCCCNbbNNNbbbN","NbbbN NbbNCCCb     bCCCNbbN NbbbN","NbbbN NbbNCCCb     bCCCNbbN NbbbN"," NNN   NNN   N     N   NNN   NNN "},
        {"                                 "," CCC   CCC   N     N   CCC   CCC "," CbC   CbC   N     N   CbC   CbC "," CCCCCCCCC   N     N   CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC   N     N   CCCCCCCCC "," CbC   CbC   N     N   CbC   CbC "," CCC   CCC   N     N   CCC   CCC ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 "," CCC   CCC   N     N   CCC   CCC "," CbC   CbC   N     N   CbC   CbC "," CCCCCCCCC   N     N   CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC   N     N   CCCCCCCCC "," CbC   CbC   N     N   CbC   CbC "," CCC   CCC   N     N   CCC   CCC ","                                 "},
        {"                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 "},
        {"                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 "},
        {" NNN   NNN             NNN   NNN ","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbNNNbbbN           NbbbNNNbbbN"," NNN   NNN             NNN   NNN ","   N   N                 N   N   "," NNN   NNN             NNN   NNN ","NbbbNNNbbbN           NbbbNNNbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN"," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbNNNbbbN           NbbbNNNbbbN"," NNN   NNN             NNN   NNN ","   N   N                 N   N   "," NNN   NNN             NNN   NNN ","NbbbNNNbbbN           NbbbNNNbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN"," NNN   NNN             NNN   NNN "},
        {"                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 "},
        {"                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 "},
        {"                                 ","                                 ","  s     s               s     s  ","                                 ","                                 ","                                 ","                                 ","                                 ","  s     s               s     s  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  s     s               s     s  ","                                 ","                                 ","                                 ","                                 ","                                 ","  s     s               s     s  ","                                 ","                                 "},
        {"                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 "},
        {"                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 ","                                 ","                                 ","  N     N               N     N  "," NNN   NNN             NNN   NNN ","  N     N               N     N  ","                                 "},
        {" NNN   NNN             NNN   NNN ","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbNNNbbbN           NbbbNNNbbbN"," NNN   NNN             NNN   NNN ","   N   N                 N   N   "," NNN   NNN             NNN   NNN ","NbbbNNNbbbN           NbbbNNNbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN"," NNN   NNN             NNN   NNN ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," NNN   NNN             NNN   NNN ","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbNNNbbbN           NbbbNNNbbbN"," NNN   NNN             NNN   NNN ","   N   N                 N   N   "," NNN   NNN             NNN   NNN ","NbbbNNNbbbN           NbbbNNNbbbN","NbbbN NbbbN           NbbbN NbbbN","NbbbN NbbbN           NbbbN NbbbN"," NNN   NNN             NNN   NNN "},
        {"                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 "},
        {"                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                ~                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 "},
        {"                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 ","                                 ","                                 ","                                 ","                                 ","                N                ","               NNN               ","                N                ","                                 ","                                 ","                                 ","                                 ","                                 "," CCC   CCC             CCC   CCC "," CbC   CbC             CbC   CbC "," CCCCCCCCC             CCCCCCCCC ","   C   C                 C   C   ","   C   C                 C   C   ","   C   C                 C   C   "," CCCCCCCCC             CCCCCCCCC "," CbC   CbC             CbC   CbC "," CCC   CCC             CCC   CCC ","                                 "},
        {" NNN   NNN             NNN   NNN ","NbbbN NbbbN    N N    NbbbN NbbbN","NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN","NbbbNNNbbbN    NbN    NbbbNNNbbbN"," NNN   NNN     NbN     NNN   NNN ","   N   N       NbN       N   N   "," NNN   NNN     NbN     NNN   NNN ","NbbbNNNbbbN    NbN    NbbbNNNbbbN","NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN","NbbbN NbbbN    NbN    NbbbN NbbbN"," NNN   NNN     NbN     NNN   NNN ","  N     N      NbN      N     N  ","  N     N      NbN      N     N  ","  N     N     NsNsN     N     N  ","  N     N    NbbbbbN    N     N  "," NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ","  NbbbbbNbbbbNbbbbbNbbbbNbbbbbN  "," NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ","  N     N    NbbbbbN    N     N  ","  N     N     NsNsN     N     N  ","  N     N      NbN      N     N  ","  N     N      NbN      N     N  "," NNN   NNN     NbN     NNN   NNN ","NbbbN NbbbN    NbN    NbbbN NbbbN","NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN","NbbbNNNbbbN    NbN    NbbbNNNbbbN"," NNN   NNN     NbN     NNN   NNN ","   N   N       NbN       N   N   "," NNN   NNN     NbN     NNN   NNN ","NbbbNNNbbbN    NbN    NbbbNNNbbbN","NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN","NbbbN NbbbN    N N    NbbbN NbbbN"," NNN   NNN             NNN   NNN "}
    };
    // spotless:on

    private static final int BRONZE_PLATED_BRICKS_INDEX = 10;
    private static final int FIREBRICK_METAID = 15;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<GT_TileEntity_MegaBrickedBlastFurnace> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GT_TileEntity_MegaBrickedBlastFurnace> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_MegaBrickedBlastFurnace>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure_string))
                .addElement(
                    'C',
                    ofVariableBlock(
                        "chisel",
                        HorizontalDirt.getLeft(),
                        HorizontalDirt.getRight(),
                        ImmutableList.of(
                            TSTUtils.newItemWithMeta(HorizontalDirt.getLeft(), HorizontalDirt.getRight()),
                            TSTUtils.newItemWithMeta(Blocks.dirt, 0))))
                .addElement(
                    'b',
                    buildHatchAdder(GT_TileEntity_MegaBrickedBlastFurnace.class).atLeast(InputBus, OutputBus)
                        .casingIndex(BRONZE_PLATED_BRICKS_INDEX)
                        .hint(1)
                        .buildAndChain(ofBlock(GregTechAPI.sBlockCasings1, BRONZE_PLATED_BRICKS_INDEX)))
                .addElement('N', ofBlock(GregTechAPI.sBlockCasings4, FIREBRICK_METAID))
                .addElement('s', ofBlock(Blocks.brick_block, 0))
                .build();
        }

        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 16, 21, 16);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 16, 21, 16, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        // Check the main structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 16, 21, 16, errors)) return;
        // Item input bus check.
        checkHatchMax(errors, InputBus, max_input_bus);
        // Item output bus check.
        checkHatchMax(errors, OutputBus, max_output_bus);
    }
    // endregion

    // region Processing Logic
    // 3600 seconds in an hour, 8 hours, 20 ticks in a second.
    private static final double max_efficiency_time_in_ticks = 3600d * 8d * 20d;

    private static final double maximum_fuelEfficiency = 8d;

    // Current efficiency
    private double fuelEfficiency = 1;

    private long running_time = 0;

    // coke coal
    private static ItemStack cokeCoal;

    private static ItemStack cokeCoalBlock;

    // irons
    private static ItemStack iron;

    private static ItemStack wroughtIron;

    // steel
    private static ItemStack steel;

    // ash
    private static ItemStack ash;

    private static final int max_input_bus = 6;
    private static final int max_output_bus = 6;
    private boolean isMultiChunkloaded = true;

    @Override
    public int totalMachineMode() {
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[] { GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_IOF_FORGE,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_STEAM };
    }

    @Override
    public String getMachineModeName() {
        // #tr tst.common.machine.MegaBrickedBlastFurnace.mode.0
        // # Steelmaking
        // #zh_CN 炼钢模式

        // #tr tst.common.machine.MegaBrickedBlastFurnace.mode.1
        // # Primitive Blast Furnace
        // #zh_CN 原始高炉模式
        return StatCollector.translateToLocal("tst.common.machine.MegaBrickedBlastFurnace.mode." + machineMode);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 30000;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (tInputList.isEmpty()) {
            resetEfficiency();
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        // If running for max_efficiency_time_in_ticks then fuelEfficiency is at maximum.
        double time_percentage = running_time / max_efficiency_time_in_ticks;
        time_percentage = Math.min(time_percentage, 1.0d);
        if (machineMode == 1) {
            GTRecipe recipe = findRecipe(tInputList);
            if (recipe == null) return CheckRecipeResultRegistry.NO_RECIPE;
            MaterialConsumption materialConsumption = calculateMaterialConsumption(recipe, tInputList);
            if (materialConsumption == null) return CheckRecipeResultRegistry.NO_RECIPE;

            fuelEfficiency = 1 + time_percentage * 7;
            fuelEfficiency = Math.min(maximum_fuelEfficiency, fuelEfficiency);

            if (!consumePrimitiveInput(materialConsumption, tInputList)) return CheckRecipeResultRegistry.NO_RECIPE;

            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            clearProcessingOutputs();
            mergeOutputItems(getPrimitiveOutputs(recipe, materialConsumption.parallelism));

            mMaxProgresstime = calculateDuration(
                recipe.mDuration,
                0,
                materialConsumption.materialAmount / materialConsumption.materialFactor,
                materialConsumption.fuelAmount * fuelEfficiency
                    * (materialConsumption.fuelInput.unifiedStack.getItem() instanceof ItemBlock ? 10 : 1));
            // Coal block considered as 10 coals here
            updateSlots();
            running_time += mMaxProgresstime;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        } else {
            long coalAmount = 0;
            long ironAmount = 0;
            long wroughtIronAmount = 0;
            double originalDuration = 240d * 20d;

            for (ItemStack item : tInputList) {
                if (item != null && item.stackSize > 0) {
                    if (item.isItemEqual(cokeCoal)) {
                        coalAmount += item.stackSize;
                    } else if (item.isItemEqual(cokeCoalBlock)) {
                        // Every coal block is considered as 10 coal
                        coalAmount += (long) item.stackSize * 10;
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
            fuelEfficiency = 1 + time_percentage * 7;
            fuelEfficiency = Math.min(maximum_fuelEfficiency, fuelEfficiency);

            final long consumeTotalIron = Math.max((ironAmount + wroughtIronAmount) / 2, 1);
            long consumeCoal = calculateConsumeCoal(coalAmount, consumeTotalIron);

            if (coalAmount < consumeCoal) {
                resetEfficiency();
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            double WroughtIronRatio = (double) wroughtIronAmount / (ironAmount + wroughtIronAmount);

            long consumeIron = (long) (consumeTotalIron * (1 - WroughtIronRatio));
            long consumeWroughtIron = Math.min(consumeTotalIron - consumeIron, wroughtIronAmount);
            consumeIron = consumeTotalIron - consumeWroughtIron;

            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            clearProcessingOutputs();
            mergeOutputItems(calculateOutputs(consumeTotalIron, consumeCoal));
            mMaxProgresstime = calculateDuration(
                originalDuration,
                WroughtIronRatio,
                consumeTotalIron,
                consumeCoal * fuelEfficiency);
            consumeInputs(consumeIron, consumeWroughtIron, tInputList);

            updateSlots();
            running_time += mMaxProgresstime;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
    }

    public static void initStatics() {
        cokeCoal = Mods.Railcraft.isModLoaded() ? getModItem("Railcraft", "fuel.coke", 1, Materials.Coal.getGems(1))
            : Materials.Coal.getGems(1);
        cokeCoalBlock = Mods.Railcraft.isModLoaded() ? getModItem("Railcraft", "cube", 1, Materials.Coal.getBlocks(1))
            : Materials.Coal.getBlocks(1);

        iron = GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L);
        wroughtIron = GTOreDictUnificator.get(OrePrefixes.ingot, Materials.WroughtIron, 1L);
        steel = GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L);
        ash = GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L);
    }

    private GTRecipe findRecipe(ArrayList<ItemStack> inputList) {
        RecipeMap<RecipeMapBackend> primitiveBlastRecipes = RecipeMaps.primitiveBlastRecipes;
        ItemStack[] inputArr = inputList.toArray(new ItemStack[inputList.size()]);
        return primitiveBlastRecipes.findRecipeQuery()
            .items(inputArr)
            .find();
    }

    /** Use the same merged ingredients and matching rules as GT's recipe search and consumption. */
    static MaterialConsumption calculateMaterialConsumption(GTRecipe recipe, List<ItemStack> inputList) {
        if (recipe.mInputs.length == 0) return null;
        MaterialConsumption result = new MaterialConsumption();
        result.recipe = recipe;
        result.parallelism = (int) recipe
            .maxParallelCalculatedByInputs(Integer.MAX_VALUE, null, inputList.toArray(new ItemStack[0]));
        if (result.parallelism <= 0) return null;

        // The standard primitive-blast recipe emitter appends its fuel additive as the last input.
        ItemStack fuel = recipe.mInputs[recipe.mInputs.length - 1];
        if (fuel == null || fuel.stackSize <= 0) return null;
        GTRecipe.RecipeItemInput[] ingredients = recipe.getCachedCombinedItemInputs();
        for (GTRecipe.RecipeItemInput ingredient : ingredients) {
            if (ingredient.matchesType(fuel)) result.fuelInput = ingredient;
        }
        if (result.fuelInput == null) return null;
        result.fuelAmount = getProvidedAmount(result.fuelInput, inputList);
        if (result.fuelAmount <= 0) return null;

        for (GTRecipe.RecipeItemInput ingredient : ingredients) {
            if (ingredient == result.fuelInput || ingredient.inputAmount <= 0) continue;
            double actualRatio = (double) getProvidedAmount(ingredient, inputList) / result.fuelAmount;
            double originalRatio = (double) ingredient.inputAmount / result.fuelInput.inputAmount;
            if (actualRatio > originalRatio) return null;
            result.materialAmount += ingredient.inputAmount * result.parallelism;
            result.materialFactor = Math.max(result.materialFactor, ingredient.inputAmount);
        }
        if (result.materialFactor <= 0) return null;
        return result;
    }

    private static long getProvidedAmount(GTRecipe.RecipeItemInput ingredient, List<ItemStack> inputs) {
        long amount = 0;
        for (ItemStack input : inputs) {
            if (input != null && input.stackSize > 0
                && ingredient.matchesRecipe(GTOreDictUnificator.getAssociation(input), input)) {
                amount += input.stackSize;
            }
        }
        return amount;
    }

    public List<ItemStackLong> getPrimitiveOutputs(GTRecipe recipe, int parallelism) {
        List<ItemStackLong> result = new ArrayList<>();
        for (int i = 0; i < recipe.mOutputs.length; i++) {
            ItemStack output = recipe.mOutputs[i];
            if (output != null && output.stackSize > 0 && parallelism > 0) {
                long amount = (long) output.stackSize
                    * ParallelHelper.calculateIntegralChancedOutputMultiplier(recipe.getOutputChance(i), parallelism);
                if (amount > 0) result.add(new ItemStackLong(output, amount));
            }
        }
        return result;
    }

    public boolean consumePrimitiveInput(MaterialConsumption materialConsumption, List<ItemStack> inputList) {
        ItemStack[] inputs = inputList.toArray(new ItemStack[0]);
        if (!materialConsumption.recipe.isRecipeInputEqual(false, false, materialConsumption.parallelism, null, inputs))
            return false;
        materialConsumption.recipe.consumeInput(materialConsumption.parallelism, null, inputs);
        // Preserve the machine's full-fuel consumption, after all recipe inputs have passed validation.
        for (ItemStack input : inputs) {
            if (input != null && input.stackSize > 0
                && materialConsumption.fuelInput.matchesRecipe(GTOreDictUnificator.getAssociation(input), input)) {
                input.stackSize = 0;
            }
        }
        return true;
    }

    protected List<ItemStackLong> calculateOutputs(long consumeTotalIron, long consumeCoal) {
        long ashAmount = consumeCoal / 9;
        if (consumeCoal % 9 > 0 && getBaseMetaTileEntity().getRandomNumber(9) < consumeCoal % 9) {
            ashAmount++;
        }
        List<ItemStackLong> outputs = new ArrayList<>();
        outputs.add(new ItemStackLong(steel, consumeTotalIron));
        if (ashAmount > 0) outputs.add(new ItemStackLong(ash, ashAmount));
        return outputs;
    }

    protected int calculateDuration(double originalDuration, double wroughtIronRatio, long consumeTotalIron,
        double coalAmount) {
        return (int) Math.max(
            1,
            Math.min(
                Integer.MAX_VALUE,
                originalDuration * consumeTotalIron / ((1 + 4 * wroughtIronRatio) * Math.sqrt(coalAmount))));
    }

    protected void consumeInputs(long consumeIron, long consumeWroughtIron, ArrayList<ItemStack> tInputList) {
        long[] consumeAmounts = new long[] { consumeIron, consumeWroughtIron };
        int i;
        for (ItemStack item : tInputList) {
            if (item != null && item.stackSize > 0) {
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
                    item.stackSize -= (int) consumeAmounts[i];
                    consumeAmounts[i] = 0;
                }
            }
        }
    }

    protected long calculateConsumeCoal(long coalAmount, long consumeTotalIron) {
        return Math.max(coalAmount, (long) Math.ceil(consumeTotalIron * 2d / fuelEfficiency));
    }

    protected void resetEfficiency() {
        running_time = 0;
        fuelEfficiency = 1;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(mProgresstime)
                + EnumChatFormatting.RESET
                + "t / "
                + EnumChatFormatting.YELLOW
                + formatNumber(mMaxProgresstime)
                + EnumChatFormatting.RESET
                + "t",
            "Ticks run: " + EnumChatFormatting.GREEN
                + formatNumber(running_time)
                + EnumChatFormatting.RESET
                + ", Fuel Efficiency: "
                + EnumChatFormatting.RED
                + formatNumber(100 * fuelEfficiency)
                + EnumChatFormatting.RESET
                + "%" };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // If machine has stopped, stop chunkloading.
            GTChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            isMultiChunkloaded = false;
        } else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !isMultiChunkloaded) {
            // Load a 3x3 area centered on controller when machine is running.
            GTChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);

            int ControllerXCoordinate = ((TileEntity) aBaseMetaTileEntity).xCoord;
            int ControllerZCoordinate = ((TileEntity) aBaseMetaTileEntity).zCoord;

            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate + 16));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate - 16));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate + 16));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate - 16));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate + 16));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate - 16));

            isMultiChunkloaded = true;
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    // No muffler hatch needed.
    @Override
    public boolean polluteEnvironment(int aPollutionLevel) {
        Pollution.addPollution(getBaseMetaTileEntity(), getPollutionPerTick(null));
        return true;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("eRunningTime", running_time);
        aNBT.setDouble("eLongEfficiencyValue", fuelEfficiency);
        aNBT.setBoolean("usePrimitiveRecipes", machineMode == 1);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        running_time = aNBT.getLong("eRunningTime");
        fuelEfficiency = aNBT.getDouble("eLongEfficiencyValue");
        super.loadNBTData(aNBT);
        machineMode = aNBT.getBoolean("usePrimitiveRecipes") ? 1 : 0;
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return TSTControllerTextures.getTexture(
            side,
            aFacing,
            aActive,
            TextureFactory.of(BlockIcons.MACHINE_CASING_DENSEBRICKS),
            BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_INACTIVE,
            BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_INACTIVE_GLOW,
            BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE,
            BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.machine_type
        // # Blast Furnace
        // #zh_CN 高炉
        tt.addMachineType(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.machine_type"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.controller
            // # Controller block for the Mega Bricked Blast Furnace
            // #zh_CN 巨型砖高炉的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.controller"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.01
            // # {\WHITE}Who could ever imagine the power of the Steam Age?
            // #zh_CN {\WHITE}谁能想象出蒸汽时代之伟力?
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.01"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.02
            // # consume iron/wrought iron ingots and coke coals (blocks) to produce steel (and ash byproduct)
            // #zh_CN 消耗铁/锻铁锭与焦煤/焦煤块炼钢(与灰烬副产物).
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.02"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.03
            // # Default recipe time is {\GOLD}240s{\GRAY}. More wrought iron and coal input will reduce process time.
            // #zh_CN 初始配方时间为{\GOLD}240s{\GRAY}. 输入更多锻铁与焦煤以减少处理时间.
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.03"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.04
            // # actual progress time = default x parallels /((1 + 4 x Ratio of wrought iron input) x sqrt(Coke coal input))
            // #zh_CN 实际处理时间 = 初始值 x 并行 / ((1 + 4 x 输入锻铁比例) x sqrt(输入焦煤))
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.04"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.05
            // # process {\RED}50%{\GRAY} of (wrought) iron input and consume all coke coal input at once.
            // #zh_CN 一次性消耗输入的(锻)铁锭的{\RED}50%{\GRAY}与输入的全部焦煤.
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.05"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.06
            // # minimum coke coal requirement:2 x (wrought) iron processed
            // #zh_CN 焦煤的最低需求量: 2 x 处理的(锻)铁锭的量
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.06"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.07
            // # Takes {\RED}8{\GRAY} hours of continuous run time to achieve maximum efficiency.
            // #zh_CN 需要连续运行{\RED}8{\GRAY}小时来达到最大效率.
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.07"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.08
            // # This improve coal efficiency by up to {\RED}800%{\GRAY}. Reduce minimum coal requirement and calculate in actual progress time.
            // #zh_CN 最多可使焦煤的使用效率提高至{\RED}800%{\GRAY},降低焦煤最低需求量并计入处理时间计算
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.08"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.09
            // # {\YELLOW}It is recommended not to force yourself to build it until you have enough resources.
            // #zh_CN {\YELLOW}建议在你有充足的资源之前不要强迫自己建造它!
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.09"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.10
            // # {\AQUA}Use the GUI mode button or a screwdriver to switch to primitive mode and process primitive blast furnace recipes, without wrought iron acceleration
            // #zh_CN 切换到原始高炉模式可处理原始高炉配方, 不再通过锻铁加速.
            .addInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.info.10"))
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo(TSTUtils.tr("tst.common.machine.IndustrialAlchemyTower.tooltip.info.16"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.structure.01
            // # {\YELLOW}Dirt must be Horizontal dirt in Chisel Mod!
            // #zh_CN {\YELLOW}泥土必须为Chisel模组中的水平花纹泥土!
            .addStructureInfo(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.structure.01"))
            // #tr tst.common.machine.MegaBrickedBlastFurnace.tooltip.structure.02
            // # any Bronze Plated Bricks, 0-6x
            // #zh_CN 任意镀铜机械方块, 0-6x
            .addInputBus(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.structure.02"), 1)
            .addOutputBus(TSTUtils.tr("tst.common.machine.MegaBrickedBlastFurnace.tooltip.structure.02"), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Nested Classes

    static class MaterialConsumption {

        public int parallelism = 1;
        GTRecipe recipe;
        GTRecipe.RecipeItemInput fuelInput;
        long fuelAmount;
        long materialAmount;
        long materialFactor;
    }

    // endregion

}
