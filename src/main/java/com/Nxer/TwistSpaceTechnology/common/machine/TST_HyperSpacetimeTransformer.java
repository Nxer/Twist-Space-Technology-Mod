package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TAE;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@SkipGenerateDescription
public class TST_HyperSpacetimeTransformer extends GTCM_MultiMachineBase<TST_HyperSpacetimeTransformer> {

    // region Class Constructor
    public TST_HyperSpacetimeTransformer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.GODERIUM);
    }

    public TST_HyperSpacetimeTransformer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_HyperSpacetimeTransformer(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "mainHyperSpacetimeTransformer";
    private final int horizontalOffSet = 12;
    private final int verticalOffSet = 22;
    private final int depthOffSet = 0;
    private static IStructureDefinition<TST_HyperSpacetimeTransformer> STRUCTURE_DEFINITION = null;
    private int coolingCasingTier = -1;
    private int fusionCoilTier = -1;
    private int energyMatrixTier = -1;
    private long maxRecipeEUt = 0;

    // spotless:off
    public static final String[][] shape = new String[][] {
        { "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "                         ", "     DDD         DDD     ", "    DDDDDD     DDDDDD    ", "    DDDDD       DDDDD    ", "   DDDDD         DDDDD   ", "   DDDDD         DDDDD   ", "  DDDDD           DDDDD  ", "   DDDD           DDDD   ", "     D             D     ", "                         ", "                         ", "                         ", "                         ", "                         ", "         DDDDDDD         ", "         DDDDDDD         ", "         DDDDDDD         ", "         DDDDDDD         ", "         DDDDDDD         ", "                         ", "                         " },
        { "                         ", "                         ", "                         ", "     K             K     ", "    KKK           KKK    ", "    KKKKK       KKKKK    ", "   KKKKKKK     KKKKKKK   ", "   KKKKKK       KKKKKK   ", "  KKKKKK         KKKKKK  ", "  KKKKKK DDDDDDD KKKKKK  ", " KKKKKK  DDDDDDD  KKKKKK ", "  KKKKK  DDDDDDD  KKKKK  ", "    KK   DDDDDDD   KK    ", "         DDDDDDD         ", "         DDDDDDD         ", "         DDDDDDD         ", "                         ", "                         ", "        KKKKKKKKK        ", "        KKKKKKKKK        ", "        KKKKKKKKK        ", "        KKKKKKKKK        ", "        KKKKKKKKK        ", "        KKKKKKKKK        ", "                         " },
        { "                         ", "                         ", "                         ", "     C             C     ", "    KCC           CCK    ", "    KKCCC       CCCKK    ", "   KKKKKCC     CCKKKKK   ", "   KKKKKK       KKKKKK   ", "  KKKKKK         KKKKKK  ", "  KKKKKK DHHHHHD KKKKKK  ", " CCKKKK  HIIIIIH  KKKKCC ", "  CCCKK  HIIIIIH  KKCCC  ", "    CC   HIIIIIH   CC    ", "         HIIIIIH         ", "         HIIIIIH         ", "         DHHHHHD         ", "                         ", "                         ", "        CKKKKKKKC        ", "        CKKKKKKKC        ", "        CKKKKKKKC        ", "        CKKKKKKKC        ", "        CKKKKKKKC        ", "        CCCCCCCCC        ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "     D             D     ", "    CKKK         KKKC    ", "    KGGLLL     LLLGGK    ", "   CKGGLLKKKKKKKLLGGKC   ", "   KGGLLKKKKKKKKKLLGGK   ", "  CKGGLLKDMMMMMDKLLGGKC  ", "  KKKLLKKMLLLLLMKKLLKKK  ", "   KLLLKKMLMMMLMKKLLLK   ", "     L KKMLMMMLMKK L     ", "       KKMLMMMLMKK       ", "       KKMLLLLLMKK       ", "       KKDMMMMMDKK       ", "        KKKKKKKKK        ", "         KKKKKKK         ", "         LLLLLLL         ", "         LLLLLLL         ", "         KKKKKKK         ", "         KKKKKKK         ", "         KKKKKKK         ", "                         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "     D             D     ", "    KDD           DDK    ", "    KGGDDDDDDDDDDDGGK    ", "   KGGGLLKKKKKKKLLGGGK   ", "  KGGGLLKKKKKKKKKLLGGGK  ", "  KGGGLLKIIIIIIIKLLGGGK  ", "  DDLLLKKIHHHHHIKKLLLDD  ", "    DDDKKIHHHHHIKKDDD    ", "      DKKIHHHHHIKKD      ", "      DKKIHHHHHIKKD      ", "      DKKIHHHHHIKKD      ", "      DKKIIIIIIIKKD      ", "      DDKKKKKKKKKDD      ", "       DDKKKKKKKDD       ", "        DDLLLLLDD        ", "         DLLLLLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "    KDD           DDK    ", "   KGGGDDKKKKKKKDDGGGK   ", "   KGGLLKKKKKKKKKLLGGK   ", "  KGGGLLKKKKKKKKKLLGGGK  ", "  KGGLLKKBBBBBBBKKLLGGK  ", "  DDLLKKKB     BKKKLLDD  ", "    DDKKKB     BKKKDD    ", "      KKKB     BKKK      ", "      KKKB     BKKK      ", "      KKKB     BKKK      ", "      KKKBBBBBBBKKK      ", "      KKKKKKKKKKKKK      ", "       KKKKKKKKKKK       ", "        KKKKKKKKK        ", "         DLLLLLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "    KDD           DDK    ", "   KGGGDD       DDGGGK   ", "  KKGGLLKDBBBBBDKLLGGKK  ", "  KGGGLLDDBBBBBDDLLGGGK  ", " DKGGLLDD       DDLLGGKD ", "  DDLL BB       BB LLDD  ", "    DD BB       BB DD    ", "       BB       BB       ", "       BB       BB       ", "       BB       BB       ", "       DD       DD       ", "        DDBBBBBDD        ", "         DBBBBBD         ", "                         ", "         DLLLLLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   CCDD           DDCC   ", "   CCCCD         DCCCC   ", "  CCCCCC DHHHHHD CCCCCC  ", "  CCCCCKKKHHHHHKKKCCCCC  ", " DCCCCCDK       KDCCCCCD ", "  DDCC HH       HH CCDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "                         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   KKDD           DDKK   ", "   KAAAD         DAAAK   ", "  KKAALL DHHHHHD LLAAKK  ", "  KAAALKKKHHHHHKKKLAAAK  ", " DKAALLDK       KDLLAAKD ", "  DDLL HH       HH LLDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "                         ", "         DLKKKLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   KKDD           DDKK   ", "   KAAAD         DAAAK   ", "  KKAALL DHHHHHD LLAAKK  ", "  KAAALKKKHHHHHKKKLAAAK  ", " DKAALLDK       KDLLAAKD ", "  DDLL HH       HH LLDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "                         ", "         DLKKKLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   KKDD           DDKK   ", "   KAAAD         DAAAK   ", "  KKAALL DHHHHHD LLAAKK  ", "  KAAALKKKHHHHHKKKLAAAK  ", " DKAALLDK       KDLLAAKD ", "  DDLL HH       HH LLDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "           KKK           ", "         DLLLLLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   CCDD           DDCC   ", "   CCCCD         DCCCC   ", "  CCCCCC KKKKKKK CCCCCC  ", "  CCCCCCKKKKKKKKKCCCCCC  ", " DCCCCCKK       KKCCCCCD ", "  DDLL KK       KK LLDD  ", "    DD KK       KK DD    ", "       KK       KK       ", "       KK       KK       ", "       KK       KK       ", "       KK       KK       ", "        KKKKKKKKK        ", "         KKKKKKK         ", "           CCC           ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   KKDD           DDKK   ", "   KAAAD         DAAAK   ", "  KKAALL DHHHHHD LLAAKK  ", "  KAAALKKKHHHHHKKKLAAAK  ", " DKAALLDK       KDLLAAKD ", "  DDLL HH       HH LLDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "           KKK           ", "         DLCCCLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   KKDD           DDKK   ", "   KAAAD         DAAAK   ", "  KKAALL DHHHHHD LLAAKK  ", "  KAAALKKKHHHHHKKKLAAAK  ", " DKAALLDK       KDLLAAKD ", "  DDLL HH       HH LLDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "                         ", "         DLKKKLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   KKDD           DDKK   ", "   KAAAD         DAAAK   ", "  KKAALL DHHHHHD LLAAKK  ", "  KAAALKKKHHHHHKKKLAAAK  ", " DKAALLDK       KDLLAAKD ", "  DDLL HH       HH LLDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "                         ", "         DLKKKLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "   CCDD           DDCC   ", "   CCCCD         DCCCC   ", "  CCCCCC DHHHHHD CCCCCC  ", "  CCCCCKKKHHHHHKKKCCCCC  ", " DCCCCCDK       KDCCCCCD ", "  DDCC HH       HH CCDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "                         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "         DCCCCCD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "    KDD           DDK    ", "   KGGGDD       DDGGGK   ", "   KGGGLLDHHHHHDLLGGGK   ", "  KGGGLLKKHHHHHKKLLGGGK  ", "  KGGGLDK       KDLGGGK  ", "  DDKL HH       HH LKDD  ", "    DD HH       HH DD    ", "       HH       HH       ", "       HH       HH       ", "       HH       HH       ", "       DK       KD       ", "        KKHHHHHKK        ", "         DHHHHHD         ", "                         ", "         DLLLLLD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "     D             D     ", "    KDD           DDK    ", "    KGGDD       DDGGK    ", "   KKGGLLDBBBBBDLLGGKK   ", "   KGGGLDDBBBBBDDLGGGK   ", "  KKGGLLD       DLLGGKK  ", "  DDKK BB       BB KKDD  ", "    DD BB       BB DD    ", "       BB       BB       ", "       BB       BB       ", "       BB       BB       ", "       DD       DD       ", "        DDBBBBBDD        ", "         DBBBBBD         ", "         DLLLLLD         ", "         DKKKKKD         ", "         DKJJJKD         ", "         DKJJJKD         ", "         DKKKKKD         ", "                         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    KK             KK    ", "    KKKK         KKKK    ", "   KKKKKKKKKKKKKKKKKKK   ", "   KKKKKKKKKKKKKKKKKKK   ", "  KKKKKKKKKKKKKKKKKKKKK  ", "  KKKKKKKBBBBBBBKKKKKKK  ", " KKKKKKKKB     BKKKKKKKK ", "   KKKKKKB     BKKKKKK   ", "     KKKKB     BKKKK     ", "      KKKB     BKKK      ", "      KKKB     BKKK      ", "      KKKBBBBBBBKKK      ", "      KKKKKKKKKKKKK      ", "       KKKKKKKKKKK       ", "        KKKKKKKKK        ", "         KKKKKKK         ", "         KKJJJKK         ", "         KKJJJKK         ", "         KKKKKKK         ", "         KKKKKKK         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    DD             DD    ", "    KKDDDDDDDDDDDDDKK    ", "   KKKKKDDKKKKKDDKKKKK   ", "   KKKKKKDKKKKKDKKKKKK   ", "  KKKKKKKKKKKKKKKKKKKKK  ", "  KKKKKKKIIIIIIIKKKKKKK  ", " DDKKKKKKIHHHHHIKKKKKKDD ", "   DDKKKKIHHHHHIKKKKDD   ", "     DDKKIHHHHHIKKDD     ", "     DKKKIHHHHHIKKKD     ", "     DKKKIHHHHHIKKKD     ", "     DKKKIIIIIIIKKKD     ", "     DKKKKKKKKKKKKKD     ", "     DDKKDKKKKKDKKDD     ", "      DDKDKKKKKDKDD      ", "       DDDKKKKKDDD       ", "         DKKKKKD         ", "         DKKKKKD         ", "         DKKKKKD         ", "         DKKKKKD         ", "                         " },
        { "                         ", "                         ", "                         ", "                         ", "    KK             KK    ", "    KKKKKKKKKKKKKKKKK    ", "   KKKKKKKKKKKKKKKKKKK   ", "   KKKKKKKKKKKKKKKKKKK   ", "  KKKKKKKKKKKKKKKKKKKKK  ", "  KKKKKKKKMMMMMKKKKKKKK  ", " KKKKKKKKMLLLLLMKKKKKKKK ", "   KKKKKKMLMMMLMKKKKKK   ", "     KKKKMLMMMLMKKKK     ", "     KKKKMLMMMLMKKKK     ", "     KKKKMLLLLLMKKKK     ", "     KKKKKMMMMMKKKKK     ", "     KKKKKKKKKKKKKKK     ", "     KKKKKKKKKKKKKKK     ", "      KKKKKKKKKKKKK      ", "       KKKKKKKKKKK       ", "         KKKKKKK         ", "         KKKKKKK         ", "         KKKKKKK         ", "         KKKKKKK         ", "                         " },
        { "     DNNNNND~DNNNNND     ", "    DDDDDDDDDDDDDDDDD    ", "   DDDDDDDDDDDDDDDDDDD   ", "  DDDDDDDDDDDDDDDDDDDDD  ", " DDDDDDDDDDDDDDDDDDDDDDD ", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDEEDDDDDDDDDDDDDDDEEDDD", "DDDEEDDDDDDDDDDDDDDDEEDDD", "DDDEEDDDDDDDDDDDDDDDEEDDD", "DDDDEDDDDDDDDDDDDDDDEDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "NDDDDDDDDDDDDDDDDDDDDDDDN", "DDDDDDDDDDDDDDDDDDDDDDDDD", "NDDDDDDDDDDDDDDDDDDDDDDDN", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", " DDDDDDDDDDEEEDDDDDDDDDD ", "  DDDDDDDDDEEEDDDDDDDDD  ", "   DDDDDDDDEEEDDDDDDDD   ", "    DDDDDDDEEEDDDDDDD    ", "     DDDDDDDDDDDDDDD     " },
        { "     DDDDDDDDDDDDDDD     ", "    DFFFFFFEEEFFFFFFD    ", "   DFFFFFFFEEEFFFFFFFD   ", "  DFFFFFFFFEEEFFFFFFFFD  ", " DFFFFFFFFFEEEFFFFFFFFFD ", "DEEFFFFEEEEEEEEEEEFFFFEED", "DEEEEFEEEEEEEEEEEEEFEEEED", "DEEEEEEEEEEEEEEEEEEEEEEED", "DFFEEEEEEEEEEEEEEEEEEEFFD", "DFFFEEEEEEEEEEEEEEEEEFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFEEEEEEEEEEEEEEEFFFFD", "DFFFFFEEEEEEEEEEEEEFFFFFD", "DFFFFFFEEEEEEEEEEEFFFFFFD", " DFFFFFFFFFEEEFFFFFFFFFD ", "  DFFFFFFFFEEEFFFFFFFFD  ", "   DFFFFFFFEEEFFFFFFFD   ", "    DFFFFFFEEEFFFFFFD    ", "     DDDDDDDDDDDDDDD     " },
        { "     DDDDDDDDDDDDDDD     ", "    DDDDDDDDDDDDDDDDD    ", "   DDDDDDDDDDDDDDDDDDD   ", "  DDDDDDDDDDDDDDDDDDDDD  ", " DDDDDDDDDDDDDDDDDDDDDDD ", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDDDDDDDD", " DDDDDDDDDDDDDDDDDDDDDDD ", "  DDDDDDDDDDDDDDDDDDDDD  ", "   DDDDDDDDDDDDDDDDDDD   ", "    DDDDDDDDDDDDDDDDD    ", "     DDDDDDDDDDDDDDD     " }
    };
    // spotless:on

    @Override
    public IStructureDefinition<TST_HyperSpacetimeTransformer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_HyperSpacetimeTransformer>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement('A', ofBlock(compactFusionCoil, 0))
                .addElement(
                    'B',
                    withChannel(
                        "coolingcasing",
                        ofBlocksTiered(
                            TST_HyperSpacetimeTransformer::getCoolingCasingTier,
                            ImmutableList
                                .of(Pair.of(TstBlocks.MetaBlockCasing02, 7), Pair.of(GregTechAPI.sBlockCasings8, 14)),
                            -1,
                            (machine, tier) -> machine.coolingCasingTier = tier,
                            machine -> machine.coolingCasingTier)))
                .addElement('C', ofBlock(TstBlocks.MetaBlockCasing02, 7))
                .addElement('D', Casings.MolecularContainmentCasing.asElement())
                .addElement('E', Casings.HighVoltageCurrentCapacitor.asElement())
                .addElement('F', Casings.IntegralFrameworkUV.asElement())
                .addElement('G', Casings.MatterGenerationCoil.asElement())
                .addElement('H', Casings.QuantumGlass.asElement())
                .addElement(
                    'I',
                    withChannel(
                        "fusioncoil",
                        ofBlocksTiered(
                            TST_HyperSpacetimeTransformer::getFusionCoilTier,
                            ImmutableList.of(
                                Pair.of(compactFusionCoil, 0),
                                Pair.of(compactFusionCoil, 1),
                                Pair.of(compactFusionCoil, 2),
                                Pair.of(compactFusionCoil, 3),
                                Pair.of(compactFusionCoil, 4),
                                Pair.of(TstBlocks.MetaBlockCasing01, 2)),
                            -1,
                            (machine, tier) -> machine.fusionCoilTier = tier,
                            machine -> machine.fusionCoilTier)))
                .addElement('J', ofBlock(TstBlocks.MetaBlockCasing01, 12))
                .addElement('K', Casings.ContainmentFieldMachineCasing.asElement())
                .addElement('L', ofBlock(TstBlocks.MetaBlockCasing02, 4))
                .addElement(
                    'M',
                    withChannel(
                        "energymatrix",
                        ofBlocksTiered(
                            TST_HyperSpacetimeTransformer::getEnergyMatrixTier,
                            ImmutableList.of(
                                Pair.of(TstBlocks.EnergySustainmentMatrix, 0),
                                Pair.of(TstBlocks.EnergySustainmentMatrix, 1),
                                Pair.of(TstBlocks.EnergySustainmentMatrix, 2),
                                Pair.of(TstBlocks.EnergySustainmentMatrix, 3),
                                Pair.of(TstBlocks.EnergySustainmentMatrix, 4),
                                Pair.of(TstBlocks.EnergySustainmentMatrix, 5)),
                            -1,
                            (machine, tier) -> machine.energyMatrixTier = tier,
                            machine -> machine.energyMatrixTier)))
                .addElement(
                    'N',
                    HatchElementBuilder.<TST_HyperSpacetimeTransformer>builder()
                        .atLeast(Energy.or(ExoticEnergy), InputBus, OutputBus, InputHatch, OutputHatch)
                        .adder(TST_HyperSpacetimeTransformer::addToMachineList)
                        .hint(1)
                        .casingIndex(TAE.getIndexFromPage(3, 1))
                        .buildAndChain(ModBlocks.blockSpecialMultiCasings, 11))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        resetUpgradeTiers();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) {
            resetUpgradeTiers();
            return;
        }
        speedBonus = 1F / fusionCoilTier;
        enablePerfectOverclock = coolingCasingTier == 2;
        maxRecipeEUt = GTValues.V[VoltageIndex.UHV + energyMatrixTier - 1];
    }

    private void resetUpgradeTiers() {
        coolingCasingTier = -1;
        fusionCoilTier = -1;
        energyMatrixTier = -1;
        speedBonus = 1F;
        enablePerfectOverclock = false;
        maxRecipeEUt = 0;
    }

    private static Integer getCoolingCasingTier(Block block, int meta) {
        if (block == TstBlocks.MetaBlockCasing02 && meta == 7) return 1;
        if (block == GregTechAPI.sBlockCasings8 && meta == 14) return 2;
        return null;
    }

    private static Integer getFusionCoilTier(Block block, int meta) {
        if (block == compactFusionCoil && meta >= 0 && meta <= 4) return meta + 1;
        if (block == TstBlocks.MetaBlockCasing01 && meta == 2) return 6;
        return null;
    }

    private static Integer getEnergyMatrixTier(Block block, int meta) {
        return block == TstBlocks.EnergySustainmentMatrix && meta >= 0 && meta <= 5 ? meta + 1 : null;
    }

    // endregion

    // region Processing Logic
    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.HyperSpacetimeTransformerRecipeMap;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Collections.singletonList(getRecipeMap());
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                // A saved single-recipe lock may still refer to the retired GT recipe map.
                SingleRecipeCheck lockedRecipe = recipeLockableMachine == null ? null
                    : recipeLockableMachine.getSingleRecipeCheck();
                if (isRecipeLocked && lockedRecipe != null && lockedRecipe.getRecipeMap() != getRecipeMap()) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (maxRecipeEUt <= 0) return CheckRecipeResultRegistry.NO_RECIPE;
                // The matrix limit is calculated in checkMachine, independently of hatch power and overclocking.
                if (recipe.mEUt > maxRecipeEUt) {
                    return CheckRecipeResultRegistry
                        .insufficientMachineTier(GTUtility.getTier(recipe.mEUt) - VoltageIndex.UHV + 1);
                }
                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 256;
    }

    @Override
    protected float getEuModifier() {
        return 1.0F;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("coolingCasingTier", coolingCasingTier);
        aNBT.setInteger("fusionCoilTier", fusionCoilTier);
        aNBT.setInteger("energyMatrixTier", energyMatrixTier);
        aNBT.setLong("maxRecipeEUt", maxRecipeEUt);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = 0;
        coolingCasingTier = aNBT.getInteger("coolingCasingTier");
        fusionCoilTier = aNBT.getInteger("fusionCoilTier");
        energyMatrixTier = aNBT.getInteger("energyMatrixTier");
        maxRecipeEUt = aNBT.getLong("maxRecipeEUt");
    }

    // endregion

    // region Upgrade Information

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        if (maxRecipeEUt <= 0) return info;
        String[] upgraded = Arrays.copyOf(info, info.length + 2);
        upgraded[info.length] = TSTSharedLocalization.MachineInfo.perfectOverclockSupport(isEnablePerfectOverclock());
        // #tr tst.common.machine.HyperSpacetimeTransformer.info.max_recipe_tier
        // # {\AQUA}Maximum Supported Recipe Tier: {\GOLD}%s
        // #zh_CN {\AQUA}最高支持配方等级: {\GOLD}%s
        upgraded[info.length + 1] = TSTUtils.tr(
            "tst.common.machine.HyperSpacetimeTransformer.info.max_recipe_tier",
            GTValues.VN[GTUtility.getTier(maxRecipeEUt)]);
        return upgraded;
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
            casingTexturePages[0][TAE.getIndexFromPage(3, 1)],
            TexturesGtBlock.Overlay_Machine_Controller_Advanced,
            TexturesGtBlock.Overlay_Machine_Controller_Advanced_Glow,
            TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active,
            TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active_Glow);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.HyperSpacetimeTransformer.tooltip.machine_type
        // # {\AQUA}Hyper Spacetime Transformer
        // #zh_CN {\AQUA}时空转换仪
        tt.addMachineType(TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.tooltip.machine_type"))
            // #tr tst.common.machine.HyperSpacetimeTransformer.tooltip.info.01
            // # To change the material itself in a higher dimension.
            // #zh_CN 于更高维度改变物质本身.
            .addInfo(TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.tooltip.info.01"))
            .addInfo(" ")
            // #tr tst.common.machine.HyperSpacetimeTransformer.tooltip.info.02
            // # Maximum parallel: {\GOLD}256{\GRAY}. Upgrade the fusion coils to improve {\GOLD}processing speed{\GRAY}.
            // #zh_CN 最大并行: {\GOLD}256{\GRAY}. 升级聚变线圈可提高{\GOLD}运行速度{\GRAY}.
            .addInfo(TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.tooltip.info.02"))
            // #tr tst.common.machine.HyperSpacetimeTransformer.tooltip.info.03
            // # Energy Sustainment Matrix tier limits the {\GOLD}voltage tier of supported recipes{\GRAY}.
            // #zh_CN 能量维持矩阵等级限制可执行配方的{\GOLD}电压等级{\GRAY}.
            .addInfo(TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.tooltip.info.03"))
            // #tr tst.common.machine.HyperSpacetimeTransformer.tooltip.info.04
            // # Upgrade the central cooling casings to unlock {\GOLD}perfect overclocking{\GRAY}.
            // #zh_CN 升级中央区域的冷却方块, 解锁{\GOLD}无损超频{\GRAY}.
            .addInfo(TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.tooltip.info.04"))
            // #tr tst.common.machine.HyperSpacetimeTransformer.tooltip.controller
            // # Front center of the base. Use the blueprint for the exact position.
            // #zh_CN 底座正面中央, 具体位置参见蓝图.
            .addController(TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.tooltip.controller"))
            // #tr tst.common.machine.HyperSpacetimeTransformer.tooltip.structure.01
            // # Exterior cooling casings are fixed Cryogenic Cooling Casings and do not affect upgrades.
            // #zh_CN 外部冷却方块固定使用低温冷却机械方块, 不参与升级判定.
            .addStructureInfo(TSTUtils.tr("tst.common.machine.HyperSpacetimeTransformer.tooltip.structure.01"))
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addEnergyHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
