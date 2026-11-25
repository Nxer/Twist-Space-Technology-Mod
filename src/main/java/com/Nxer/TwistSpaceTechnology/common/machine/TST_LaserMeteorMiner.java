package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.LaserBeaconRender;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.mixed_energy_hatches;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.special_block_structure_issue;
import static com.Nxer.TwistSpaceTechnology.config.Config.OverclockPowerMultiplier_LaserMeteorMiner;
import static com.Nxer.TwistSpaceTechnology.config.Config.OverclockTimeDivisor_LaserMeteorMiner;
import static com.Nxer.TwistSpaceTechnology.config.Config.StandardRecipeDuration_Second_LaserMeteorMiner;
import static com.Nxer.TwistSpaceTechnology.util.TSTUtils.tr;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.entity.TileEntityLaserBeacon;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_LaserMeteorMiner;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarControllerFrame;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarStructure;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GlassTier;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gtPlusPlus.core.block.ModBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@SkipGenerateDescription
public class TST_LaserMeteorMiner extends MTEExtendedPowerMultiBlockBase<TST_LaserMeteorMiner>
    implements ISurvivalConstructable, TSTTooltipCredit {

    // region Class Constructor
    public TST_LaserMeteorMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.TOTTO);
    }

    public TST_LaserMeteorMiner(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LaserMeteorMiner(mName);
    }

    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int horizontalOffSet_T1 = 9;
    protected static final int verticalOffSet_T1 = 13;
    protected static final int depthOffSet_T1 = 7;
    private static final String STRUCTURE_PIECE_TIER2 = "tier2";
    private static final String STRUCTURE_PIECE_TIER3 = "tier3";
    protected static final int horizontalOffSet_T2 = 9;
    protected static final int verticalOffSet_T2 = 15;
    protected static final int depthOffSet_T2 = 3;
    private static IStructureDefinition<TST_LaserMeteorMiner> STRUCTURE_DEFINITION;
    private final StarControllerFrame controllerFrame = new StarControllerFrame(false);
    private static final int STAR_CONTROLLER_LINKED = 4;
    private boolean starControllerLinked;

    // spotless:off
    protected static final String[][] shape_T1 = new String[][]{
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         J         ","        J J        ","         J         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         J         ","        J J        ","       J   J       ","        J J        ","         J         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         J         ","       J   J       ","                   ","      J     J      ","                   ","       J   J       ","         J         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         J         ","      J     J      ","                   ","                   ","     J   Z   J     ","                   ","                   ","      J     J      ","         J         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","         J         ","     J       J     ","                   ","                   ","    J              ","    J    B    J    ","                   ","                   ","                   ","     J       J     ","         J         ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","         J         ","    J         J    ","                   ","                   ","                   ","         I         ","   J    IBI    J   ","         I         ","                   ","                   ","                   ","    J         J    ","         J         ","                   ","                   ","                   "},
        {"                   ","                   ","    JJJJJJJJJJJ    ","   JJLLLLLLLLLJJ   ","  JJLL       LLJJ  ","  JLL         LLJ  ","  JL           LJ  ","  JL           LJ  ","  JL     I     LJ  ","  JL    IBI    LJ  ","  JL     I     LJ  ","  JL           LJ  ","  JL           LJ  ","  JLL         LLJ  ","  JJLL       LLJJ  ","   JJLLLLLLLLLJJ   ","    JJJJJJJJJJJ    ","                   ","                   "},
        {"                   ","                   ","                   ","         J         ","      LLLLLLL      ","     LL     LL     ","    LL       LL    ","    L         L    ","    L    I    L    ","   JL   IBI   LJ   ","    L    I    L    ","    L         L    ","    LL       LL    ","     LL     LL     ","      LLLLLLL      ","         J         ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","         J         ","       LLLLL       ","      LL   LL      ","     LL  I  LL     ","     L  III  L     ","    JL IIBII LJ    ","     L  III  L     ","     LL  I  LL     ","      LL   LL      ","       LLLLL       ","         J         ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         J         ","        LLL        ","       LLLLL       ","      LLLLLLL      ","     JLLLBLLLJ     ","      LLLLLLL      ","       LLLLL       ","        LLL        ","         J         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         J         ","        JJJ        ","       JKAKJ       ","      JJABAJJ      ","       JKAKJ       ","        JJJ        ","         J         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","        KAK        ","        ABA        ","        KAK        ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","        KAK        ","        ABA        ","        KAK        ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","        W~W        ","       WKKKW       ","       WKBKW       ","       WKKKW       ","        WWW        ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","        KYK        ","       KKKKK       ","      KKBBBKK      ","      KKBBBKK      ","      KKBBBKK      ","       KKKKK       ","        KKK        ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","        K K        ","       K   K       ","      A     A      ","     K       K     ","    K         K    ","                   ","    K         K    ","     K       K     ","      A     A      ","       K   K       ","        K K        ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","        K K        ","                   ","       K   K       ","      A     A      ","     K       K     ","   K           K   ","                   ","   K           K   ","     K       K     ","      A     A      ","       K   K       ","                   ","        K K        ","                   ","                   ","                   "},
        {"                   ","                   ","        K K        ","                   ","                   ","       K   K       ","      A     A      ","     K       K     ","  K             K  ","                   ","  K             K  ","     K       K     ","      A     A      ","       K   K       ","                   ","                   ","        K K        ","                   ","                   "},
        {"         K         ","        K K        ","       K   K       ","       K   K       ","       K   K       ","      K     K      ","     KK     KK     ","  KKK         KKK  "," K               K ","K                 K"," K               K ","  KKK         KKK  ","     KK     KK     ","      K     K      ","       K   K       ","       K   K       ","       K   K       ","        K K        ","         K         "}
    };

    protected static final String[][] shape_T2 = new String[][]{
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         Z         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         B         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         B         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        EBE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        EBE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        EBE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        ECE        ","       ECBCE       ","        ECE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        ECE        ","       ECBCE       ","        ECE        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        CCC        ","       ECBCE       ","        CCC        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         E         ","         E         ","        C C        ","      EE B EE      ","        C C        ","         E         ","         E         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","         E         ","        HEH        ","       HC CH       ","      EE B EE      ","       HC CH       ","        HEH        ","         E         ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","                   ","        HEH        ","       H C H       ","      H C C H      ","      EC B CE      ","      H C C H      ","       H C H       ","        HEH        ","                   ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         E         ","        AEA        ","       E C E       ","      A     A      ","     EEC B CEE     ","      A     A      ","       E C E       ","        AEA        ","         E         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","                   ","         E         ","        A A        ","       E C E       ","      A     A      ","     E C B C E     ","      A     A      ","       E C E       ","        A A        ","         E         ","                   ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","                   ","         E         ","         E         ","       EA AE       ","      EE   EE      ","      A     A      ","    EE       EE    ","      A     A      ","      EE   EE      ","       EA AE       ","         E         ","         E         ","                   ","                   ","                   ","                   "},
        {"                   ","                   ","                   ","         ~         ","       DD DD       ","      D     D      ","     D  FFF  D     ","    D  F   F  D    ","    D F     F D    ","   E  F     F  E   ","    D F     F D    ","    D  F   F  D    ","     D  FFF  D     ","      D     D      ","       DD DD       ","         E         ","                   ","                   ","                   "},
        {"                   ","                   ","         X         ","        E E        ","                   ","                   ","                   ","                   ","   E           E   ","  X             X  ","   E           E   ","                   ","                   ","                   ","                   ","        E E        ","         X         ","                   ","                   "},
        {"                   ","         X         ","        X X        ","      GGG GGG      ","     GG     GG     ","    GG       GG    ","   GG         GG   ","   G           G   ","  XG           GX  "," X               X ","  XG           GX  ","   G           G   ","   GG         GG   ","    GG       GG    ","     GG     GG     ","      GGG GGG      ","        X X        ","         X         ","                   "},
        {"         X         ","        X X        ","       X   X       ","                   ","                   ","                   ","                   ","  X             X  "," X               X ","X                 X"," X               X ","  X             X  ","                   ","                   ","                   ","                   ","       X   X       ","        X X        ","         X         "},
        {"         X         ","        X X        ","       X   X       ","                   ","                   ","                   ","                   ","  X             X  "," X               X ","X                 X"," X               X ","  X             X  ","                   ","                   ","                   ","                   ","       X   X       ","        X X        ","         X         "},
        {"         X         ","        X X        ","                   ","                   ","                   ","                   ","                   ","                   "," X               X ","X                 X"," X               X ","                   ","                   ","                   ","                   ","                   ","                   ","        X X        ","         X         "},
        {"         X         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","X                 X","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         X         "}
    };

    protected static final String[][] shape_T3 = new String[][]{
        {"                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "         Z         ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "        MMM        ", "      MMMMMMM      ", "     MQM   MQM     ", "    MM       MM    ", "    MM       MM    ", "   MM         MM   ", "   MQ    B    QM   ", "   MM         MM   ", "    MM       MM    ", "    MM       MM    ", "     MQM   MQM     ", "      MMMMMMM      ", "        MMM        ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "        EEE        ", "      ESEEESE      ", "     ESSEEESSE     ", "    EEE     EEE    ", "    EE       EE    ", "   ESE       ESE   ", "   ESE   B   ESE   ", "   ESE       ESE   ", "    EE       EE    ", "    EEE     EEE    ", "     ESSEEESSE     ", "      ESEEESE      ", "        EEE        ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "         E         ", "      ESEEESE      ", "     EESE ESEE     ", "     EE     EE     ", "     EE  E  EE     ", "    ES  EBE  SE    ", "     EE  E  EE     ", "     EE     EE     ", "     EESE ESEE     ", "      ESEEESE      ", "         E         ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "         M         ", "       MMMMM       ", "      ME   EM      ", "      M  E  M      ", "     MM EBE MM     ", "      M  E  M      ", "      ME   EM      ", "       MMMMM       ", "         M         ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "      EEE EEE      ", "      ENE ENE      ", "      EEEEEEE      ", "        EBE        ", "      EEEEEEE      ", "      ENE ENE      ", "      EEE EEE      ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "      EME EME      ", "      MNEEENM      ", "      EEEPEEE      ", "       EPBPE       ", "      EEEPEEE      ", "      MNEEENM      ", "      EME EME      ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "      ERE ERE      ", "      RREEERR      ", "      EEEPEEE      ", "       EPBPE       ", "      EEEPEEE      ", "      RREEERR      ", "      ERE ERE      ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "      ERE ERE      ", "      RNEEENR      ", "      EEPPPEE      ", "       EPBPE       ", "      EEPPPEE      ", "      RNEEENR      ", "      ERE ERE      ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "      ERE ERE      ", "      RREEERR      ", "      RNEEENR      ", "      EEP PEE      ", "      EE B EE      ", "      EEP PEE      ", "      RNEEENR      ", "      RREEERR      ", "      ERE ERE      ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "     EMME EMME     ", "     MMUEEEUMM     ", "     EEEHEHEEE     ", "       HP PH       ", "      EE B EE      ", "       HP PH       ", "     EEEHEHEEE     ", "     MMUEEEUMM     ", "     EMME EMME     ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "                   ", "     EEE   EEE     ", "     ENUHEHUNE     ", "     EEH P HEE     ", "      H P P H      ", "      EP B PE      ", "      H P P H      ", "     EEH P HEE     ", "     ENUHEHUNE     ", "     EEE   EEE     ", "                   ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "         N         ", "     EEENENEEE     ", "     ENUOEOUNE     ", "     EEENPNEEE     ", "     NOEE EEON     ", "    NEEP B PEEN    ", "     NOEE EEON     ", "     EEENPNEEE     ", "     ENUOEOUNE     ", "     EEENENEEE     ", "         N         ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "         U         ", "     UUUUUUUUU     ", "     UNEO OENU     ", "     UEE P EEU     ", "     UO     OU     ", "    UU P B P UU    ", "     UO     OU     ", "     UEE P EEU     ", "     UNEO OENU     ", "     UUUUUUUUU     ", "         U         ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "                   ", "       UUUUU       ", "      UUUUUUU      ", "     UUEO OEUU     ", "     UUE   EUU     ", "    UUO     OUU    ", "    UU       UU    ", "    UUO     OUU    ", "     UUE   EUU     ", "     UUEO OEUU     ", "      UUUUUUU      ", "        UUU        ", "                   ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "                   ", "        U~U        ", "      UUUUUUU      ", "     ENUEEEUNE     ", "    ENUEFFFEUNE    ", "    NUUF   FUUN    ", "   UNUF     FUNU   ", "   UUUF     FUUU   ", "   UNUF     FUNU   ", "    NUUF   FUUN    ", "    ENUEFFFEUNE    ", "     ENUEEEUNE     ", "      ENNUNNE      ", "        UUU        ", "                   ", "                   ", "                   "},
        {"                   ", "                   ", "         U         ", "       UU UU       ", "       UUUUU       ", "      NUUUUUN      ", "     N       N     ", "    NU       UN    ", "   UUU       UUU   ", "  VUUU       UUUV  ", "   UUU       UUU   ", "    NU       UN    ", "     N       N     ", "      NUUUUUN      ", "       NUUUN       ", "        UUU        ", "         V         ", "                   ", "                   "},
        {"                   ", "        EVE        ", "      UFVUVFU      ", "     EGGGUGGGE     ", "    EGGUEEEUGGE    ", "   EGG EEEEE GGE   ", "  EGG         GGE  ", "  EGE         EGE  ", "  VGE         EGV  ", " VEEE         EEEV ", "  VGE         EGV  ", "  EGE         EGE  ", "  EGG         GGE  ", "   EGG       GGE   ", "    EGGEEEEEGGE    ", "     EGGGUGGGE     ", "      EEVUVEE      ", "         V         ", "                   "},
        {"         V         ", "        VEV        ", "       VUUUV       ", "      NUUEUUN      ", "       UEEEU       ", "       UEEEU       ", "   N           N   ", "  VE           EV  ", " VEE           EEV ", "VEEE           EEEV", " VEE           EEV ", "  VE           EV  ", "   N           N   ", "                   ", "                   ", "      NUUEUUN      ", "       VUUUV       ", "        VEV        ", "         V         "},
        {"         V         ", "        VEV        ", "       VEEEV       ", "      EEEEEEE      ", "                   ", "                   ", "   E           E   ", "  VE           EV  ", " VEE           EEV ", "VEEE           EEEV", " VEE           EEV ", "  VE           EV  ", "   E           E   ", "                   ", "                   ", "      EEEEEEE      ", "       VEEEV       ", "        VEV        ", "         V         "},
        {"         V         ", "        VEV        ", "       EEEEE       ", "      EEEEEEE      ", "                   ", "                   ", "   E           E   ", "  EE           EE  ", " VEE           EEV ", "VEEE           EEEV", " VEE           EEV ", "  EE           EE  ", "   E           E   ", "                   ", "                   ", "      EEEEEEE      ", "       EEEEE       ", "        VEV        ", "         V         "},
        {"         V         ", "        EEE        ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", " E               E ", "VE               EV", " E               E ", "                   ", "                   ", "                   ", "                   ", "                   ", "                   ", "        EEE        ", "         V         "}
    };
    // spotless:on

    @Override
    public IStructureDefinition<TST_LaserMeteorMiner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_LaserMeteorMiner>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape_T1))
                .addShape(STRUCTURE_PIECE_TIER2, transpose(shape_T2))
                .addShape(STRUCTURE_PIECE_TIER3, transpose(shape_T3))
                .addElement('A', ofAnyGlass())
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 15)) // Superconducting Coil
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings4, 7)) // Fusion Coil Block
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 2)) // Mining Neutronium Casing
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 3)) // Mining Black Plutonium Casing
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings9, 11)) // Heat-Resistant Trinium Plated Casing
                .addElement('G', ofFrame(Materials.Neutronium)) // Neutronium Frame
                .addElement('H', ofFrame(Materials.BlackPlutonium)) // Black Plutonium Frame
                .addElement('I', ofBlock(GregTechAPI.sBlockCasings5, 5)) // Naquadah Coil
                .addElement('J', ofFrame(Materials.StainlessSteel))
                .addElement('K', ofBlock(ModBlocks.blockSpecialMultiCasings, 6)) // Structural Solar Casings
                .addElement('L', ofBlock(ModBlocks.blockSpecialMultiCasings, 8)) // Thermally Insulated Casing
                .addElement('M', ofBlock(TstBlocks.MetaBlockCasing02, 2)) // Iridium-Reinforced Neutronium Casing
                .addElement('N', ofBlock(TstBlocks.MetaBlockCasing02, 8)) // Neutronium Support Casing
                .addElement('O', ofBlockUnlocalizedName("bartworks", "BW_TieredGlass", 6, false))
                .addElement('P', ofBlockUnlocalizedName("GoodGenerator", "compactFusionCoil", 2, false))
                .addElement('Q', ofBlockUnlocalizedName("Automagy", "blockTranslucent", 0, false))
                .addElement('R', ofBlockUnlocalizedName("dreamcraft", "BloodyIchorium", 0, false))
                .addElement('S', ofBlockUnlocalizedName("dreamcraft", "BloodyThaumium", 0, false))
                .addElement('U', StarStructure.controllerCasing())
                .addElement(
                    'V',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(OutputBus, Energy.or(ExoticEnergy), Maintenance)
                        .casingIndex(TstBlocks.MetaBlockCasing02.getTextureIndex(8))
                        .hint(4)
                        .buildAndChain(ofBlock(TstBlocks.MetaBlockCasing02, 8)))
                .addElement(
                    'W',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(OutputBus, Energy.or(ExoticEnergy), Maintenance)
                        .casingIndex(TAE.getIndexFromPage(3, 9))
                        .hint(1)
                        .buildAndChain(ofBlock(ModBlocks.blockSpecialMultiCasings, 6)))
                .addElement(
                    'Y',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(InputBus)
                        .adder(TST_LaserMeteorMiner::addInjector)
                        .casingIndex(TAE.getIndexFromPage(3, 9))
                        .hint(2)
                        .buildAndChain(ofBlock(ModBlocks.blockSpecialMultiCasings, 6)))
                .addElement(
                    'X',
                    buildHatchAdder(TST_LaserMeteorMiner.class).atLeast(OutputBus, Energy.or(ExoticEnergy), Maintenance)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                        .hint(3)
                        .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 2)))
                .addElement('Z', ofBlock(LaserBeaconRender, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    /**
     * Any glass, like {@code chainAllGlasses()} but without its glass tier channel: in NEI that channel raises the tier
     * slider to the number of glass tiers and adds a "Glass" slider, while this multi accepts every glass anyway.
     */
    private static IStructureElement<TST_LaserMeteorMiner> ofAnyGlass() {
        return lazy(() -> {
            List<IStructureElement<TST_LaserMeteorMiner>> glasses = new ArrayList<>();
            for (Pair<Block, Integer> glass : GlassTier.getGlassList()) {
                glasses.add(ofBlock(glass.getLeft(), glass.getRight()));
            }
            return ofChain(glasses);
        });
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (GregTechAPI.isDummyWorld(getBaseMetaTileEntity().getWorld())) {
            multiTier = Math.max(1, Math.min(3, stackSize.stackSize));
        }
        if (stackSize.stackSize > 1) {
            buildPiece(
                stackSize.stackSize >= 3 ? STRUCTURE_PIECE_TIER3 : STRUCTURE_PIECE_TIER2,
                stackSize,
                hintsOnly,
                horizontalOffSet_T2,
                verticalOffSet_T2,
                depthOffSet_T2);
        } else {
            buildPiece(
                STRUCTURE_PIECE_MAIN,
                stackSize,
                hintsOnly,
                horizontalOffSet_T1,
                verticalOffSet_T1,
                depthOffSet_T1);
        }
        // Only for the hologram projector's hints: not part of the structure, of auto-build or of the NEI preview
        if (hintsOnly) hintRitualLayout(Math.min(3, stackSize.stackSize));
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return stackSize.stackSize < 2
            ? survivalBuildPiece(
                STRUCTURE_PIECE_MAIN,
                stackSize,
                horizontalOffSet_T1,
                verticalOffSet_T1,
                depthOffSet_T1,
                elementBudget,
                env,
                false,
                true)
            : survivalBuildPiece(
                stackSize.stackSize >= 3 ? STRUCTURE_PIECE_TIER3 : STRUCTURE_PIECE_TIER2,
                stackSize,
                horizontalOffSet_T2,
                verticalOffSet_T2,
                depthOffSet_T2,
                elementBudget,
                env,
                false,
                true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.multiTier = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet_T1, verticalOffSet_T1, depthOffSet_T1, errors)) {
            multiTier = 1;
        } else {
            clearHatches();
            errors.clear();
            if (checkPiece(STRUCTURE_PIECE_TIER2, horizontalOffSet_T2, verticalOffSet_T2, depthOffSet_T2, errors)) {
                multiTier = 2;
            } else {
                clearHatches();
                errors.clear();
                if (!checkPiece(STRUCTURE_PIECE_TIER3, horizontalOffSet_T2, verticalOffSet_T2, depthOffSet_T2, errors))
                    return;
                multiTier = 3;
            }
        }

        setStartCoords();
        if (!findLaserRenderer()) {
            errors.add(special_block_structure_issue);
            return;
        }

        checkEnergyHatches(errors);

    }

    /**
     * At least one energy hatch: any number of normal ones, or TecTech multi Amp / laser ones, but not mixed.
     */
    protected void checkEnergyHatches(List<StructureError> errors) {
        if (hasStarStructureContext() && multiTier == structureStarTier) return;
        if (mEnergyHatches.isEmpty() && mExoticEnergyHatches.isEmpty()) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, Energy, 0, 1));
        } else if (!mEnergyHatches.isEmpty() && !mExoticEnergyHatches.isEmpty()) {
            errors.add(mixed_energy_hatches);
        }
    }
    // endregion

    // region Processing Logic
    private static final int distanceFromMeteor = 48;
    private static final int MAX_RADIUS = 40;
    private static final int METEOR_POLL_INTERVAL_TICKS = 200;
    /** Max air blocks the tier 1 drill skips in a single cycle, to avoid lag spikes. */
    private static final int MAX_AIR_CHECKS_PER_CYCLE = 4096;
    protected TileEntityLaserBeacon renderer;
    private int currentRadius = MAX_RADIUS;
    private int xDrill, yDrill, zDrill;
    private int xStart, yStart, zStart;
    private int fortuneTier = 0;
    private boolean isStartInitialized = false;
    private boolean hasFinished = true;
    private boolean isWaiting = false;
    private boolean isResetting = false;
    private final List<ItemStack> res = new ArrayList<>();
    private final MiningOverclockLogic miningOverclock = new MiningOverclockLogic();
    protected int multiTier = 0;
    protected boolean stopAllRendering = false;

    /** Native output cycles must finish before another mode may use the controller. */
    protected final boolean canYieldWorldMining() {
        return mMaxProgresstime <= 0 && res.isEmpty() && (mOutputItems == null || mOutputItems.length == 0);
    }

    protected final boolean canSelectMiningMode() {
        return (hasFinished || !isStartInitialized) && canYieldWorldMining();
    }

    /** Window of the stats shown in the GUI: 5 seconds. */
    private static final int RECENT_WINDOW_TICKS = 5 * 20;

    /** A mining cycle, kept for the mining speed of the last {@link #RECENT_WINDOW_TICKS} ticks (not saved). */
    @Desugar
    private record MiningCycle(long tick, int blocksMined) {}

    /** An item produced from the current meteor, with its total amount. */
    private static final class MinedAmount {

        final ItemStack stack; // stack size 1
        long amount;

        MinedAmount(ItemStack stack, long amount) {
            this.stack = stack;
            this.amount = amount;
        }
    }

    /** Everything produced since the drill started on the current meteor, merged (saved). */
    private final List<MinedAmount> meteorOutputs = new ArrayList<>();

    private final ArrayDeque<MiningCycle> recentCycles = new ArrayDeque<>();
    private int blocksMinedThisCycle = 0;
    /** Cache of {@link #sphereWork} for the whole meteor, which only depends on radius and tier. */
    private long totalWork = 0;
    private int totalWorkRadius = -1, totalWorkTier = -1;

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    protected void runMachine(IGregTechTileEntity base, long tick) {
        if (!starMode) tryLinkToStarHost();
        if (!isMiningUnit()) {
            super.runMachine(base, tick);
            return;
        }
        lEUt = 0;
        boolean active = linkedStar != null && isLinkedToStar(linkedStar) && linkedStar.isStarMinerRunning();
        mMaxProgresstime = active ? Math.max(1, linkedStar.starMinerCycleDuration()) : 0;
        mProgresstime = active ? linkedStar.starMinerCycleProgress() : 0;
        setCheckRecipeResult(miningUnitResult());
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        if (!starMode) tryLinkToStarHost();
        if (isMiningUnit()) return SimpleCheckRecipeResult.ofFailure("star_linked");
        return checkWorldMining();
    }

    @NotNull
    private CheckRecipeResult checkWorldMining() {
        if (this.multiTier != this.getMultiTier(mInventory[1])) {
            // spotless:off
            // #tr GT5U.gui.text.recipe_result.missing_schematic
            // # {\LIGHT_PURPLE}Missing Schematic.
            // #zh_CN {\LIGHT_PURPLE}缺少设计图.
            return SimpleCheckRecipeResult.ofFailure("missing_schematic");
        }
        if (isResetting) {
            this.reset();
            // #tr GT5U.gui.text.recipe_result.meteor_reset
            // # {\LIGHT_PURPLE}Reset completed!
            // #zh_CN {\LIGHT_PURPLE}重置完成!
            return SimpleCheckRecipeResult.ofSuccess("meteor_reset");
        }

        setElectricityStats();
        if (!isEnergyEnough()) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("not_enough_energy");
        }

        if (!isStartInitialized) {
            this.setStartCoords();
            if (!isMeteorAreaLoaded(MAX_RADIUS)) return meteorAreaNotLoaded();
            this.findBestRadius();
            this.initializeDrillPos();
        }

        if (!hasFinished) {
            if (!isMeteorAreaLoaded(currentRadius)) return meteorAreaNotLoaded();
            updateLaser(true, this.currentRadius + distanceFromMeteor + 0.5 + this.getLaserToEndHeight());
            this.setFortuneTier();
            this.startMining(this.multiTier);
            final List<ItemStack> outputs = mergeStacks(res);
            mOutputItems = toOutputArray(outputs);
            res.clear();
            addToMeteorOutputs(outputs);
            recordCycle();
        } else {
            updateLaser(false, 0);
            this.isWaiting = true;
            this.setElectricityStats();
            // Finding the new radius reads blocks up to the max radius
            if (!isMeteorAreaLoaded(MAX_RADIUS)) return meteorAreaNotLoaded();
            boolean isReady = checkCenter();
            if (isReady) {
                this.isWaiting = false;
                this.setElectricityStats();
                this.setReady();
                this.hasFinished = false;
            } else {
                // #tr GT5U.gui.text.recipe_result.meteor_waiting
                // # {\LIGHT_PURPLE}Waiting for a Meteor...
                // #zh_CN {\LIGHT_PURPLE}等待陨星中...
                return SimpleCheckRecipeResult.ofSuccess("meteor_waiting");
            }
        }

        // #tr GT5U.gui.text.recipe_result.meteor_mining
        // # {\LIGHT_PURPLE}Currently Mining!
        // #zh_CN {\LIGHT_PURPLE}正在开采!
        return SimpleCheckRecipeResult.ofSuccess("meteor_mining");
            // spotless:on
    }

    @Override
    public void onDisableWorking() {
        updateLaser(false, 0);
        super.onDisableWorking();
    }

    @Override
    public void onBlockDestroyed() {
        updateLaser(false, 0);
        super.onBlockDestroyed();
    }

    /**
     * Updates the laser only when something changed: every setter of the laser tile entity sends a block update to the
     * clients.
     *
     * @param mining whether the miner is mining; the laser is shown only if the screwdriver didn't turn rendering off
     * @param range  length of the laser, used only while it is shown
     */
    protected void updateLaser(boolean mining, double range) {
        if (renderer == null) return;
        // Linked effects belong to the star receiver, even after removal of the miner or its beacon.
        boolean shouldRender = !isMiningUnit() && mining && !stopAllRendering;
        if (renderer.getShouldRender() != shouldRender) renderer.setShouldRender(shouldRender);
        if (shouldRender && renderer.getRange() != range) renderer.setRange(range);
    }

    /**
     * Whether all the chunks of the cube mined around the meteor center are loaded, so that mining never loads chunks
     * by itself.
     */
    private boolean isMeteorAreaLoaded(int radius) {
        return getBaseMetaTileEntity().getWorld()
            .checkChunksExist(
                xStart - radius,
                yStart - radius,
                zStart - radius,
                xStart + radius + 1,
                yStart + radius + 1,
                zStart + radius + 1);
    }

    private static CheckRecipeResult meteorAreaNotLoaded() {
        // #tr GT5U.gui.text.recipe_result.meteor_area_not_loaded
        // # {\LIGHT_PURPLE}Meteor area not loaded, waiting...
        // #zh_CN {\LIGHT_PURPLE}陨星区域未加载, 等待中...
        return SimpleCheckRecipeResult.ofFailure("meteor_area_not_loaded");
    }

    protected boolean findLaserRenderer() {
        ChunkCoordinates center = getMeteorCenter(multiTier);

        if (getBaseMetaTileEntity().getWorld()
            .getTileEntity(
                center.posX,
                getBaseMetaTileEntity().getYCoord() + (this.multiTier == 1 ? 10 : 15),
                center.posZ) instanceof TileEntityLaserBeacon laser) {
            renderer = laser;
            renderer.setRotationFields(ExtendedFacing.of(getDirection(), getRotation(), getFlip()));
            renderer.setColors(1, 0, 0);
            return true;
        }
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack tool) {
        if (aPlayer.isSneaking()) {
            if (automaticStarLink) {
                resolveStarHost();
                if (automaticStarLink) return;
            }
            if (linkedStar != null && isLinkedToStar(linkedStar)) return;
            if (!starMode && !canSelectMiningMode()) return;
            starMode = !starMode;
            linkedStar = null;
            updateLaser(false, 0);
            markDirty();
            // #tr tst.star.miner.mode.world
            // # Meteor miner: world mining mode
            // #zh_CN 坠星矿机: 世界开采模式

            // #tr tst.star.miner.mode.link
            // # Meteor miner: extraction unit mode
            // #zh_CN 坠星矿机: 开采单元模式
            GTUtility
                .sendChatToPlayer(aPlayer, tr(starMode ? "tst.star.miner.mode.link" : "tst.star.miner.mode.world"));
            return;
        }
        stopAllRendering = !stopAllRendering;
        // #tr tst.machine.MeteorMiner.message.render.off
        // # Rendering off
        // #zh_CN 渲染特效关闭

        // #tr tst.machine.MeteorMiner.message.render.on
        // # Rendering on
        // #zh_CN 渲染特效开启
        if (stopAllRendering) {
            TSTUtils.sendMessageKeyToPlayer(aPlayer, "tst.machine.MeteorMiner.message.render.off");
            updateLaser(false, 0);
        } else {
            // the laser comes back on the next mining cycle
            TSTUtils.sendMessageKeyToPlayer(aPlayer, "tst.machine.MeteorMiner.message.render.on");
        }
    }

    protected int getMultiTier(ItemStack inventory) {
        if (inventory == null || inventory.stackSize < 1) return 0;
        return GTCMItemList.MeteorMinerSchematic3.equal(inventory) ? 3
            : GTCMItemList.MeteorMinerSchematic2.equal(inventory) ? 2
                : GTCMItemList.MeteorMinerSchematic1.equal(inventory) ? 1 : 0;
    }

    private int getLaserToEndHeight() {
        return (this.multiTier == 1 ? 3 : 0);
    }

    protected void setFortuneTier() {
        int toolFortune = 0;
        if (multiTier == 1 && !mInputBusses.isEmpty()) {
            toolFortune = getFortuneTier(
                mInputBusses.get(0)
                    .getInventoryHandler()
                    .getStackInSlot(0));
        }
        fortuneTier = fortuneForTier(multiTier, toolFortune);
    }

    static int fortuneForTier(int miningTier, int toolFortune) {
        return switch (miningTier) {
            case 1 -> toolFortune;
            case 2 -> 3;
            case 3 -> 4;
            default -> 0;
        };
    }

    private static int getFortuneTier(ItemStack itemStack) {
        if (itemStack == null || itemStack.stackSize < 1) return 0;
        Item t = itemStack.getItem();
        if (MiscHelper.PickaxeOfTheCore.equals(t)) return 1;
        if (MiscHelper.BoundPickaxe.equals(t)) return 2;
        if (MiscHelper.TerraShatterer.equals(t)) return 3;
        return 0;
    }

    private void reset() {
        this.isResetting = false;
        this.hasFinished = true;
        this.isWaiting = false;
        currentRadius = MAX_RADIUS;
        this.initializeDrillPos();
    }

    public void startReset() {
        if (isMiningUnit()) return;
        this.isResetting = true;
        stopMachine(ShutDownReasonRegistry.NONE);
        enableWorking();
    }

    private void startMining(int tier) {
        switch (tier) {
            case 1 -> this.mineSingleBlock();
            case 2 -> this.mineRow();
            case 3 -> this.mineLayer();
            default -> throw new IllegalArgumentException("Invalid Multiblock Tier");
        }
    }

    private void mineSingleBlock() {
        final World world = getBaseMetaTileEntity().getWorld();
        int airChecks = 0;
        while (true) {
            if (this.zDrill == this.zStart - currentRadius && world.isAirBlock(this.xDrill, this.yDrill, this.zStart)) {
                // Meteors are symmetric: if the center of the row is air, the whole row is air
                this.moveToNextColumn();
            } else if (world.isAirBlock(this.xDrill, this.yDrill, this.zDrill)) {
                this.moveToNextBlock();
            } else break;
            if (this.hasFinished || ++airChecks >= MAX_AIR_CHECKS_PER_CYCLE) return;
        }
        this.mineBlock(this.xDrill, this.yDrill, this.zDrill);
        this.moveToNextBlock();
    }

    private void mineRow() {
        int currentX = this.xDrill;
        int currentY = this.yDrill;
        while (getBaseMetaTileEntity().getWorld() // Skips empty rows
            .isAirBlock(currentX, currentY, this.zStart)) {
            this.moveToNextColumn();
            if (this.hasFinished) return;
            currentX = this.xDrill;
            currentY = this.yDrill;
        }

        int opposite = 0;
        for (int z = -currentRadius; z <= (currentRadius - opposite); z++) {
            int currentZ = this.zStart + z;
            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(this.xDrill, this.yDrill, currentZ)) {
                this.mineBlock(this.xDrill, this.yDrill, currentZ);
            } else opposite++;
        }
        this.moveToNextColumn();
    }

    /** One horizontal slice per native cycle, with at most (2 * MAX_RADIUS + 1)^2 block visits. */
    private void mineLayer() {
        World world = getBaseMetaTileEntity().getWorld();
        // The same spherical centre-line assumption as T1/T2, bounded to the remaining height.
        while (yDrill <= yStart + currentRadius && world.isAirBlock(xStart, yDrill, zStart)) yDrill++;
        if (yDrill > yStart + currentRadius) {
            hasFinished = true;
            return;
        }
        for (int x = xStart - currentRadius; x <= xStart + currentRadius; x++) {
            for (int z = zStart - currentRadius; z <= zStart + currentRadius; z++) {
                if (!world.isAirBlock(x, yDrill, z)) mineBlock(x, yDrill, z);
            }
        }
        yDrill++;
        xDrill = xStart - currentRadius;
        zDrill = zStart - currentRadius;
        if (yDrill > yStart + currentRadius) hasFinished = true;
    }

    private void mineBlock(int currentX, int currentY, int currentZ) {
        Block target = getBaseMetaTileEntity().getBlock(currentX, currentY, currentZ);
        // Negative hardness means unbreakable (e.g. bedrock); zero is valid (e.g. Et Futurum's honey block)
        if (target.getBlockHardness(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ) >= 0) {
            final int targetMeta = getBaseMetaTileEntity().getMetaID(currentX, currentY, currentZ);
            Collection<ItemStack> drops = target
                .getDrops(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ, targetMeta, 0);
            if (GTUtility.isOre(target, targetMeta)) {
                res.addAll(getOutputByDrops(drops));
            } else res.addAll(drops);
            getBaseMetaTileEntity().getWorld()
                .setBlockToAir(currentX, currentY, currentZ);
            blocksMinedThisCycle++;
        }
    }

    private List<ItemStack> getOutputByDrops(Collection<ItemStack> oreBlockDrops) {
        long voltage = getMaxInputVoltage();
        List<ItemStack> outputItems = new ArrayList<>();
        oreBlockDrops.forEach(currentItem -> {
            if (!doUseMaceratorRecipe(currentItem)) {
                outputItems.add(multiplyStackSize(currentItem));
                return;
            }
            GTRecipe tRecipe = RecipeMaps.maceratorRecipes.findRecipeQuery()
                .items(currentItem)
                .voltage(voltage)
                .find();
            if (tRecipe == null) {
                outputItems.add(currentItem);
                return;
            }
            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                if (tRecipe.mOutputs[i] == null) continue;
                // Chanced outputs (e.g. byproducts) only come out when the roll succeeds
                if (getBaseMetaTileEntity().getRandomNumber(10000) >= tRecipe.getOutputChance(i)) continue;
                outputItems.add(multiplyStackSize(tRecipe.mOutputs[i].copy()));
            }
        });
        return outputItems;
    }

    /**
     * Merges equal stacks (same item, meta and NBT). The resulting stacks can be bigger than their max stack size.
     */
    private static List<ItemStack> mergeStacks(List<ItemStack> stacks) {
        List<ItemStack> merged = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack == null || stack.stackSize <= 0) continue;
            ItemStack same = null;
            for (ItemStack candidate : merged) {
                if (GTUtility.areStacksEqual(candidate, stack)) {
                    same = candidate;
                    break;
                }
            }
            if (same == null) merged.add(stack.copy());
            else same.stackSize += stack.stackSize;
        }
        return merged;
    }

    /**
     * Splits merged stacks into stacks of at most their max stack size.
     */
    private static ItemStack[] toOutputArray(List<ItemStack> mergedStacks) {
        List<ItemStack> outputs = new ArrayList<>();
        for (ItemStack stack : mergedStacks) {
            final int maxSize = Math.max(1, stack.getMaxStackSize());
            for (int left = stack.stackSize; left > 0; left -= maxSize) {
                outputs.add(GTUtility.copyAmountUnsafe(Math.min(maxSize, left), stack));
            }
        }
        return outputs.toArray(new ItemStack[0]);
    }

    private ItemStack multiplyStackSize(ItemStack itemStack) {
        itemStack.stackSize *= getBaseMetaTileEntity().getRandomNumber(this.fortuneTier + 1) + 1;
        return itemStack;
    }

    private boolean doUseMaceratorRecipe(ItemStack currentItem) {
        ItemData itemData = GTOreDictUnificator.getItemData(currentItem);
        return itemData == null || itemData.mPrefix != OrePrefixes.crushed && itemData.mPrefix != OrePrefixes.dustImpure
            && itemData.mPrefix != OrePrefixes.dust
            && itemData.mPrefix != OrePrefixes.gem
            && itemData.mPrefix != OrePrefixes.gemChipped
            && itemData.mPrefix != OrePrefixes.gemExquisite
            && itemData.mPrefix != OrePrefixes.gemFlawed
            && itemData.mPrefix != OrePrefixes.gemFlawless
            && itemData.mMaterial.mMaterial != Materials.Oilsands;
    }

    private void moveToNextBlock() {
        if (this.zDrill <= this.zStart + currentRadius) {
            this.zDrill++;
        } else {
            this.zDrill = this.zStart - currentRadius;
            this.moveToNextColumn();
        }
    }

    private void moveToNextColumn() {
        if (this.xDrill <= this.xStart + currentRadius) {
            this.xDrill++;
        } else if (this.yDrill <= this.yStart + currentRadius) {
            this.xDrill = this.xStart - currentRadius;
            this.yDrill++;
        } else {
            this.hasFinished = true;
        }
    }

    /**
     * Sets the coordinates of the center to the max range meteor center
     *
     */
    private void setStartCoords() {
        final ChunkCoordinates center = getMeteorCenter(this.multiTier);
        xStart = center.posX;
        yStart = center.posY;
        zStart = center.posZ;
    }

    /**
     * Center of the meteor for the given tier: right above the laser beacon, {@code distanceFromMeteor} blocks above
     * the highest block of the multi. This is where players put the Warded Glass that stops the meteor.
     */
    protected ChunkCoordinates getMeteorCenter(int tier) {
        final IGregTechTileEntity base = getBaseMetaTileEntity();
        final int backDistance = tier == 1 ? 2 : 6;
        int x = base.getXCoord();
        int z = base.getZCoord();
        ForgeDirection facing = base.getBackFacing();
        if (facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH) {
            z += backDistance * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        } else {
            x += backDistance * getExtendedFacing().getRelativeBackInWorld().offsetX;
        }
        return new ChunkCoordinates(x, distanceFromMeteor + (tier == 1 ? 13 : 15) + base.getYCoord(), z);
    }

    /**
     * Position of Blood Magic's Master Ritual Stone of the ritual the multi is built around: in the same column as the
     * laser and the meteor center, below the Superconducting Coils (tier 1: below the 3x3 with 3 blocks of air in
     * between; tier 2: below the column with 5 blocks of air in between).
     */
    private ChunkCoordinates getMasterRitualStonePosition(int tier) {
        final ChunkCoordinates center = getMeteorCenter(tier);
        return new ChunkCoordinates(
            center.posX,
            getBaseMetaTileEntity().getYCoord() - (tier == 1 ? 5 : 4),
            center.posZ);
    }

    /**
     * Shows where the Warded Glass (meteor center) and the Master Ritual Stone go, together with the hologram
     * projector's structure hints.
     */
    private void hintRitualLayout(int tier) {
        hintBlock(getMeteorCenter(tier), Mods.Thaumcraft.ID, "blockCosmeticOpaque", 2); // Warded Glass
        hintBlock(getMasterRitualStonePosition(tier), Mods.BloodMagic.ID, "masterStone", 0);
    }

    /**
     * Hint particle of the given block, or of StructureLib's generic hint block if the mod isn't loaded.
     */
    private void hintBlock(ChunkCoordinates position, String modId, String blockName, int meta) {
        final World world = getBaseMetaTileEntity().getWorld();
        final Block block = GameRegistry.findBlock(modId, blockName);
        if (block != null) {
            StructureLibAPI.hintParticle(world, position.posX, position.posY, position.posZ, block, meta);
        } else {
            StructureLibAPI
                .hintParticle(world, position.posX, position.posY, position.posZ, StructureLibAPI.getBlockHint(), 0);
        }
    }

    private void setReady() {
        this.findBestRadius();
        this.initializeDrillPos();
    }

    private void initializeDrillPos() {
        this.xDrill = this.xStart - currentRadius;
        this.yDrill = this.yStart - currentRadius;
        this.zDrill = this.zStart - currentRadius;

        this.isStartInitialized = true;
        this.hasFinished = false;
        // a new meteor (or a reset): the stats of the previous one don't apply
        recentCycles.clear();
        blocksMinedThisCycle = 0;
        meteorOutputs.clear();
    }

    private boolean checkCenter() {
        return !getBaseMetaTileEntity().getWorld()
            .isAirBlock(xStart, yStart + 1, zStart);
    }

    private void findBestRadius() {
        currentRadius = MAX_RADIUS;
        int delta = 0;
        for (int zCoord = zStart - currentRadius; delta < MAX_RADIUS - 1; zCoord++) {
            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(xStart, yStart, zCoord)) {
                break;
            }
            delta++;
        }
        currentRadius -= delta;
    }

    /** Receives the inherited power settings, including the single-hatch and debug-energy rules. */
    public static final class MiningOverclockLogic extends ProcessingLogic {

        public OverclockCalculator calculate() {
            return calculateMiningOverclock(availableVoltage, availableAmperage, amperageOC);
        }
    }

    /** Shared configurable OC, rounded up to whole ticks and capped at the first one-tick cycle. */
    public static OverclockCalculator calculateMiningOverclock(long voltage, long amperage, boolean amperageOC) {
        int duration = StandardRecipeDuration_Second_LaserMeteorMiner * 20;
        double powerMultiplier = OverclockPowerMultiplier_LaserMeteorMiner;
        double timeDivisor = OverclockTimeDivisor_LaserMeteorMiner;
        long overclockVoltage = voltage / 16;
        double availablePower = (double) overclockVoltage * (amperageOC ? amperage : Math.min(amperage, 1));
        int maxOverclocks = 0;
        while (maxOverclocks < Long.SIZE && miningDurationTicks(duration, maxOverclocks, timeDivisor) > 1
            && RECIPE_MV * Math.pow(powerMultiplier, maxOverclocks + 1) <= availablePower) maxOverclocks++;
        return new OverclockCalculator() {

            @Override
            protected void calculateOverclock() {
                super.calculateOverclock();
                calculatedDuration = miningDurationTicks(duration, overclocks, timeDivisor);
            }
        }.setEUt(overclockVoltage)
            .setAmperage(amperage)
            .setRecipeEUt(RECIPE_MV)
            .setDuration(duration)
            .setAmperageOC(amperageOC)
            .setEUtIncreasePerOC(powerMultiplier)
            .setDurationDecreasePerOC(timeDivisor)
            .setMaxOverclocks(maxOverclocks)
            .calculate();
    }

    private static int miningDurationTicks(int baseTicks, int overclocks, double timeDivisor) {
        // Pair OCs into exact powers of two before rounding, without any fractional-tick state.
        double ticks;
        if (timeDivisor == Math.sqrt(2)) {
            ticks = Math.scalb((double) baseTicks, -(overclocks / 2));
            if ((overclocks & 1) != 0) ticks /= Math.sqrt(2);
        } else {
            ticks = baseTicks / Math.pow(timeDivisor, overclocks);
        }
        return Math.max(1, (int) Math.ceil(ticks));
    }

    protected void setElectricityStats() {
        this.mOutputItems = new ItemStack[0];

        this.mEfficiency = 10000;
        this.mEfficiencyIncrease = 10000;

        setProcessingLogicPower(miningOverclock);
        OverclockCalculator calculator = miningOverclock.calculate();
        this.mMaxProgresstime = isWaiting ? METEOR_POLL_INTERVAL_TICKS : calculator.getDuration();
        this.lEUt = isWaiting ? 0 : -calculator.getConsumption();
    }

    private boolean isEnergyEnough() {
        long requiredEnergy = 512 + getMaxInputVoltage() * 4;
        for (MTEHatch energyHatch : getExoticAndNormalEnergyHatchList()) {
            requiredEnergy -= energyHatch.getEUVar();
            if (requiredEnergy <= 0) return true;
        }
        return false;
    }

    /**
     * The GUI shows the outputs of the last seconds instead of the outputs and progress of the single (short) cycle.
     */
    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    // region GUI stats (server side, read by TST_Gui_LaserMeteorMiner's sync values)

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new TST_Gui_LaserMeteorMiner(this);
    }

    private void recordCycle() {
        final long now = getWorldTime();
        recentCycles.addLast(new MiningCycle(now, blocksMinedThisCycle));
        blocksMinedThisCycle = 0;
        pruneRecentCycles(now);
    }

    /**
     * Drops the cycles older than the window, but keeps the last two to compute rates when cycles are slow.
     */
    private void pruneRecentCycles(long now) {
        while (recentCycles.size() > 2 && now - recentCycles.peekFirst()
            .tick() > RECENT_WINDOW_TICKS) {
            recentCycles.pollFirst();
        }
    }

    protected long getWorldTime() {
        return getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime();
    }

    public boolean isMining() {
        return isMiningUnit() ? linkedStar != null && isLinkedToStar(linkedStar) && linkedStar.isStarMinerRunning()
            : getBaseMetaTileEntity().isActive() && isStartInitialized && !hasFinished && !isWaiting;
    }

    public int getCurrentRadius() {
        return currentRadius;
    }

    public int getFortuneTier() {
        return fortuneTier;
    }

    public int getMiningTier() {
        return multiTier;
    }

    /**
     * Mining cycles needed for the meteor, a sphere of the given radius around its center, from the given drill
     * position (relative to the center) to the end. The drill scans y, then x, then (tier 1 only) z, each from
     * {@code -radius} to {@code radius + 1}. Tier 1 mines one block per cycle and tier 2 one row per cycle, while empty
     * space is skipped within a cycle, so it costs nothing. Tier 3 counts one cycle per horizontal layer.
     */
    protected static long sphereWork(int radius, int tier, int dxFrom, int dyFrom, int dzFrom) {
        if (tier == 3) return Math.max(0, radius - Math.max(-radius, dyFrom) + 1L);
        // Blood Magic uses (radius + 0.5)^2; integer coordinates give this inclusive limit.
        final long radiusLimit = (long) radius * (radius + 1);
        long work = 0;
        for (int dy = dyFrom; dy <= radius + 1; dy++) {
            for (int dx = dy == dyFrom ? dxFrom : -radius; dx <= radius + 1; dx++) {
                final long left = radiusLimit - (long) dx * dx - (long) dy * dy;
                if (left < 0) continue; // the row misses the sphere
                if (tier != 1) {
                    work++;
                    continue;
                }
                final int halfLength = (int) Math.sqrt(left); // blocks of the row at |dz| <= halfLength
                final int firstDz = dy == dyFrom && dx == dxFrom ? Math.max(dzFrom, -halfLength) : -halfLength;
                if (firstDz <= halfLength) work += halfLength - firstDz + 1;
            }
        }
        return work;
    }

    private long getRemainingWork() {
        return sphereWork(currentRadius, multiTier, xDrill - xStart, yDrill - yStart, zDrill - zStart);
    }

    private long getTotalWork() {
        if (totalWorkRadius != currentRadius || totalWorkTier != multiTier) {
            totalWork = sphereWork(currentRadius, multiTier, -currentRadius, -currentRadius, -currentRadius);
            totalWorkRadius = currentRadius;
            totalWorkTier = multiTier;
        }
        return totalWork;
    }

    /**
     * Fraction (0-1) of the meteor already mined, counted in mining cycles: it grows at a steady pace.
     */
    public double getProgress() {
        final long total = getTotalWork();
        if (total <= 0) return 0;
        return Math.max(0, Math.min(1, 1 - (double) getRemainingWork() / total));
    }

    /**
     * Blocks mined per second over the recent cycles.
     */
    public double getBlocksPerSecond() {
        pruneRecentCycles(getWorldTime());
        if (recentCycles.size() < 2) return 0;
        final long ticks = recentCycles.peekLast()
            .tick()
            - recentCycles.peekFirst()
                .tick();
        if (ticks <= 0) return 0;
        int blocks = -recentCycles.peekFirst()
            .blocksMined(); // the first cycle happened before the measured interval
        for (MiningCycle cycle : recentCycles) blocks += cycle.blocksMined();
        return blocks * 20.0 / ticks;
    }

    /**
     * Seconds until the meteor is mined: the remaining mining cycles times the current cycle duration; -1 if unknown.
     */
    public int getEtaSeconds() {
        if (mMaxProgresstime <= 0) return -1;
        final long ticks = getRemainingWork() * mMaxProgresstime;
        return (int) Math.min(Integer.MAX_VALUE, (ticks + 19) / 20);
    }

    private void addToMeteorOutputs(List<ItemStack> outputs) {
        for (ItemStack output : outputs) {
            MinedAmount same = null;
            for (MinedAmount mined : meteorOutputs) {
                if (GTUtility.areStacksEqual(mined.stack, output)) {
                    same = mined;
                    break;
                }
            }
            if (same == null)
                meteorOutputs.add(new MinedAmount(GTUtility.copyAmountUnsafe(1, output), output.stackSize));
            else same.amount += output.stackSize;
        }
    }

    /**
     * Everything produced from the current meteor (or the last one, while waiting), biggest amounts first.
     */
    public List<ItemStackLong> getMeteorOutputs() {
        final List<ItemStackLong> result = new ArrayList<>(meteorOutputs.size());
        for (MinedAmount mined : meteorOutputs) result.add(new ItemStackLong(mined.stack, mined.amount));
        result.sort(
            Comparator.comparingLong(ItemStackLong::stackSize)
                .reversed());
        return result;
    }

    // endregion

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("fortune", this.fortuneTier);
        tag.setInteger("tier", this.multiTier);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        // spotless:off
        // #tr tst.machine.MeteorMiner.waila.tier.0
        // # Incomplete Structure
        // #zh_CN 结构未成型

        // #tr tst.machine.MeteorMiner.waila.tier.1
        // # Current Tier: {\WHITE}1
        // #zh_CN 当前等级: {\WHITE}1

        // #tr tst.machine.MeteorMiner.waila.tier.2
        // # Current Tier: {\WHITE}2
        // #zh_CN 当前等级: {\WHITE}2

        // #tr tst.machine.MeteorMiner.waila.fortune.0
        // # Augment: {\WHITE}No Augment Applied
        // #zh_CN 增强效果: 无增强

        // #tr tst.machine.MeteorMiner.waila.fortune.1
        // # Augment: {\WHITE}Fortune I
        // #zh_CN 增强效果: 时运I

        // #tr tst.machine.MeteorMiner.waila.fortune.2
        // # Augment: {\WHITE}Fortune II
        // #zh_CN 增强效果: 时运II

        // #tr tst.machine.MeteorMiner.waila.fortune.3
        // # Augment: {\WHITE}Fortune III
        // #zh_CN 增强效果: 时运III

        // #tr tst.machine.MeteorMiner.waila.fortune.4
        // # Augment: {\WHITE}Fortune IV
        // #zh_CN 增强效果: 时运IV
        currentTip.add(tr("tst.machine.MeteorMiner.waila.tier." + tag.getInteger("tier")) + EnumChatFormatting.RESET);
        // spotless:on
        currentTip
            .add(tr("tst.machine.MeteorMiner.waila.fortune." + tag.getInteger("fortune")) + EnumChatFormatting.RESET);
    }

    // endregion

    // region Star Mining Unit
    private boolean starMode;
    private boolean automaticStarLink;
    private int starHostX, starHostY, starHostZ;
    private TST_StarThatNeverWas linkedStar;
    private long starLeaseUntil;
    private double starBeamRange;
    private TST_StarThatNeverWas structureStar;
    private int structureStarTier;
    private long structureStarLeaseUntil;

    /** Claim a complete miner between native output cycles, with its beacon below the host's receiver. */
    public boolean claimStar(TST_StarThatNeverWas host) {
        var base = getBaseMetaTileEntity();
        if (base == null || !isValid()) return false;
        var hostBase = host.getBaseMetaTileEntity();
        if (hostBase == null || !host.isValid() || !host.mMachine) return false;
        if (automaticStarLink && (starHostX != hostBase.getXCoord() || starHostY != hostBase.getYCoord()
            || starHostZ != hostBase.getZCoord())) return false;
        if (linkedStar != null && linkedStar != host && isLinkedToStar(linkedStar)) return false;
        if (!automaticStarLink && !canYieldWorldMining()) return false;
        int mountedTier = 0;
        for (int tier = 2; tier >= 1; tier--) {
            if (host.tileAt(0, StarStructure.MINER_FLOOR_Y + (tier == 2 ? 7 : 6), tier == 2 ? 6 : 2) == base) {
                mountedTier = tier == 2 && (multiTier == 3 || getMultiTier(mInventory[1]) == 3) ? 3 : tier;
                break;
            }
        }
        if (mountedTier == 0) return false;
        TileEntity receiver = host.tileAt(0, StarStructure.RECEIVER_Y, 0);
        if (!(receiver instanceof TileStarContainment)) return false;
        ChunkCoordinates center = getMeteorCenter(mountedTier);
        int laserY = base.getYCoord() + (mountedTier == 1 ? 10 : 15);
        if (center.posX != receiver.xCoord || center.posZ != receiver.zCoord || receiver.yCoord <= laserY) return false;
        // Establish the mounted structure context before requiring formation. Native geometry and the laser beacon
        // are still checked; only the host supplies the otherwise independent energy/maintenance infrastructure.
        if (structureStar != host || structureStarTier != mountedTier) {
            structureStar = host;
            structureStarTier = mountedTier;
            onStructureChange();
            mUpdate = 1;
        }
        structureStarLeaseUntil = base.getWorld()
            .getTotalWorldTime() + 60;
        if (!mMachine || multiTier != mountedTier || multiTier != getMultiTier(mInventory[1])) return false;
        if (!automaticStarLink) {
            starMode = true;
            automaticStarLink = true;
            starHostX = hostBase.getXCoord();
            starHostY = hostBase.getYCoord();
            starHostZ = hostBase.getZCoord();
            mMaxProgresstime = mProgresstime = 0;
            lEUt = 0;
            markDirty();
        }
        linkedStar = host;
        starLeaseUntil = base.getWorld()
            .getTotalWorldTime() + 60;
        // Only a validated mounted miner follows this host; a paused link retains its owner and structure lease.
        if (base.isAllowedToWork() != hostBase.isAllowedToWork()) {
            if (hostBase.isAllowedToWork()) base.enableWorking();
            else base.disableWorking();
        }
        starBeamRange = receiver.yCoord - laserY + 0.5;
        setFortuneTier();
        if (renderer == null || renderer.isInvalid()) findLaserRenderer();
        updateLaser(false, 0);
        return true;
    }

    private boolean hasStarStructureContext() {
        var base = getBaseMetaTileEntity();
        if (structureStar == null || base == null || !structureStar.isValid() || !structureStar.mMachine) return false;
        var hostBase = structureStar.getBaseMetaTileEntity();
        World world = base.getWorld();
        return hostBase != null && hostBase.getWorld() == world
            && world.blockExists(hostBase.getXCoord(), hostBase.getYCoord(), hostBase.getZCoord())
            && world.getTotalWorldTime() <= structureStarLeaseUntil;
    }

    @Override
    public int getRepairStatus() {
        return hasStarStructureContext() && multiTier == structureStarTier ? getIdealStatus() : super.getRepairStatus();
    }

    public boolean isLinkedToStar(TST_StarThatNeverWas host) {
        return hasStarLink(host) && getBaseMetaTileEntity().isAllowedToWork();
    }

    /** A validated claim survives the work switch, including the connected panel's red-light state. */
    public boolean hasStarLink(TST_StarThatNeverWas host) {
        var base = getBaseMetaTileEntity();
        return host != null && starMode
            && linkedStar == host
            && isValid()
            && mMachine
            && base != null
            && base.getWorld()
                .blockExists(starHostX, starHostY, starHostZ)
            && base.getWorld()
                .getTotalWorldTime() <= starLeaseUntil
            && host.isValid()
            && host.mMachine;
    }

    public double getStarBeamDepth() {
        return Math.max(0, starBeamRange - .5);
    }

    public void releaseStar(TST_StarThatNeverWas host) {
        if (linkedStar != host) return;
        clearAutomaticStarLink();
    }

    private void clearAutomaticStarLink() {
        starMode = automaticStarLink = false;
        linkedStar = null;
        clearStarStructureContext();
        starLeaseUntil = 0;
        mMaxProgresstime = mProgresstime = 0;
        lEUt = 0;
        updateLaser(false, 0);
        // Returning to world mining must be an explicit player decision, never a side effect of removing the host.
        getBaseMetaTileEntity().disableWorking();
        getBaseMetaTileEntity().setActive(false);
        setCheckRecipeResult(CheckRecipeResultRegistry.NONE);
        markDirty();
        scheduleRecipeCheckImmediate();
    }

    private void clearStarStructureContext() {
        structureStar = null;
        structureStarTier = 0;
        structureStarLeaseUntil = 0;
        onStructureChange();
        mUpdate = 1;
    }

    /** Probe one known controller position before native mining; never scan or force-load nearby chunks. */
    private void tryLinkToStarHost() {
        var base = getBaseMetaTileEntity();
        if (starMode || base == null
            || !mMachine
            || !base.isAllowedToWork()
            || multiTier < 1
            || multiTier > 3
            || mMaxProgresstime > 0) return;
        ChunkCoordinates center = getMeteorCenter(multiTier);
        ForgeDirection back = getExtendedFacing().getRelativeBackInWorld();
        int x = center.posX - back.offsetX * StarStructure.CONTROLLER_Z;
        int y = base.getYCoord() + StarStructure.CONTROLLER_Y - StarStructure.MINER_FLOOR_Y - (multiTier >= 2 ? 7 : 6);
        int z = center.posZ - back.offsetZ * StarStructure.CONTROLLER_Z;
        World world = base.getWorld();
        if (!world.blockExists(x, y, z)) return;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IGregTechTileEntity hostBase
            && hostBase.getMetaTileEntity() instanceof TST_StarThatNeverWas host
            && host.tileAt(0, StarStructure.MINER_FLOOR_Y + (multiTier >= 2 ? 7 : 6), multiTier >= 2 ? 6 : 2) == base)
            claimStar(host);
    }

    /** An unloaded owner is unresolved, never permission to start digging the world. */
    private void resolveStarHost() {
        if (!automaticStarLink) return;
        World world = getBaseMetaTileEntity().getWorld();
        if (!world.blockExists(starHostX, starHostY, starHostZ)) return;
        TileEntity tile = world.getTileEntity(starHostX, starHostY, starHostZ);
        if (tile instanceof IGregTechTileEntity base && base.getMetaTileEntity() instanceof TST_StarThatNeverWas host
            && host.isValid()
            && (host.mMachine || host.getmStartUpCheck() >= 0)) {
            linkedStar = host;
            return;
        }
        clearAutomaticStarLink();
    }

    @Override
    public boolean needsClientTick() {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long tick) {
        controllerFrame.update(base);
        if (base.isServerSide() && structureStar != null && !hasStarStructureContext()) {
            if (automaticStarLink && !base.getWorld()
                .blockExists(starHostX, starHostY, starHostZ)) {
                // Keep the saved owner while its chunk is unavailable, but revoke the temporary infrastructure.
                clearStarStructureContext();
                base.disableWorking();
                mMaxProgresstime = mProgresstime = 0;
                lEUt = 0;
                updateLaser(false, 0);
            } else clearAutomaticStarLink();
        }
        if (base.isServerSide() && automaticStarLink && tick % 20 == 0) resolveStarHost();
        super.onPostTick(base, tick);
        // Do not alter the native tool flags: they must retain their original state when the unit is detached.
        if (base.isServerSide() && hasStarStructureContext()) setErrorDisplayID(getErrorDisplayID() & ~63);
        if (base.isServerSide() && starMode) {
            base.setActive(linkedStar != null && isLinkedToStar(linkedStar) && linkedStar.isStarMinerRunning());
            if (linkedStar == null || !isLinkedToStar(linkedStar)) updateLaser(false, 0);
        }
    }

    public boolean isMiningUnit() {
        return starMode || hasStarStructureContext();
    }

    private CheckRecipeResult miningUnitResult() {
        if (hasStarStructureContext() && multiTier != getMultiTier(mInventory[1])) {
            return SimpleCheckRecipeResult.ofFailure("missing_schematic");
        }
        // #tr GT5U.gui.text.recipe_result.star_linked
        // # Standing by
        // #zh_CN 待命

        // #tr GT5U.gui.text.recipe_result.star_mining
        // # Extracting
        // #zh_CN 开采中
        return SimpleCheckRecipeResult.ofSuccess(isMining() ? "star_mining" : "star_linked");
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("currentRadius", currentRadius);
        aNBT.setInteger("xDrill", xDrill);
        aNBT.setInteger("yDrill", yDrill);
        aNBT.setInteger("zDrill", zDrill);
        aNBT.setInteger("xStart", xStart);
        aNBT.setInteger("yStart", yStart);
        aNBT.setInteger("zStart", zStart);
        aNBT.setBoolean("isStartInitialized", isStartInitialized);
        aNBT.setBoolean("hasFinished", hasFinished);
        aNBT.setBoolean("isWaiting", isWaiting);
        aNBT.setBoolean("stopAllRendering", stopAllRendering);
        aNBT.setInteger("multiTier", multiTier);
        aNBT.setInteger("fortuneTier", fortuneTier);
        NBTTagList outputsTag = new NBTTagList();
        for (MinedAmount mined : meteorOutputs) {
            NBTTagCompound tag = new NBTTagCompound();
            mined.stack.writeToNBT(tag);
            tag.setLong("amount", mined.amount); // ItemStack's own count is a byte
            outputsTag.appendTag(tag);
        }
        aNBT.setTag("meteorOutputs", outputsTag);

        aNBT.setBoolean("starLinkMode", starMode);
        aNBT.setBoolean("automaticStarLink", automaticStarLink);
        aNBT.setInteger("starHostX", starHostX);
        aNBT.setInteger("starHostY", starHostY);
        aNBT.setInteger("starHostZ", starHostZ);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        currentRadius = aNBT.getInteger("currentRadius");
        xDrill = aNBT.getInteger("xDrill");
        yDrill = aNBT.getInteger("yDrill");
        zDrill = aNBT.getInteger("zDrill");
        xStart = aNBT.getInteger("xStart");
        yStart = aNBT.getInteger("yStart");
        zStart = aNBT.getInteger("zStart");
        isStartInitialized = aNBT.getBoolean("isStartInitialized");
        hasFinished = aNBT.getBoolean("hasFinished");
        isWaiting = aNBT.getBoolean("isWaiting");
        stopAllRendering = aNBT.getBoolean("stopAllRendering");
        multiTier = aNBT.getInteger("multiTier");
        fortuneTier = fortuneForTier(multiTier, aNBT.getInteger("fortuneTier"));
        meteorOutputs.clear();
        NBTTagList outputsTag = aNBT.getTagList("meteorOutputs", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < outputsTag.tagCount(); i++) {
            NBTTagCompound tag = outputsTag.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
            if (stack != null) meteorOutputs.add(new MinedAmount(stack, tag.getLong("amount")));
        }

        starMode = aNBT.getBoolean("starLinkMode");
        automaticStarLink = aNBT.getBoolean("automaticStarLink");
        if (automaticStarLink) starMode = true;
        starHostX = aNBT.getInteger("starHostX");
        starHostY = aNBT.getInteger("starHostY");
        starHostZ = aNBT.getInteger("starHostZ");
    }

    // endregion

    // region Textures
    @Override
    public byte getUpdateData() {
        return (byte) (multiTier & 3
            | (controllerFrame.hasLinkedNeighbour(getBaseMetaTileEntity()) ? STAR_CONTROLLER_LINKED : 0));
    }

    @Override
    public void onValueUpdate(byte value) {
        multiTier = value & 3;
        starControllerLinked = (value & STAR_CONTROLLER_LINKED) != 0;
    }

    public boolean isStarControllerLinked() {
        return starControllerLinked;
    }

    public static IIconContainer OVERLAY_FRONT_METEOR_MINER = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER");

    public static IIconContainer OVERLAY_FRONT_METEOR_MINER_ACTIVE = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_ACTIVE");

    public static IIconContainer OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW");

    public static IIconContainer OVERLAY_FRONT_METEOR_MINER_GLOW = Textures.BlockIcons
        .custom("gtnhcommunitymod:iconSets/OVERLAY_FRONT_METEOR_MINER_GLOW");

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        int casingIndex = multiTier >= 3 ? StarStructure.casingTextureIndex()
            : multiTier == 2 ? ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3)
                : TAE.getIndexFromPage(3, 9);
        ITexture casing = Textures.BlockIcons.getCasingTextureForId(casingIndex);
        ITexture[] connected = controllerFrame.getTexture(side, aFacing, casing);
        if (connected != null) return connected;
        return TSTControllerTextures.getTexture(
            side,
            aFacing,
            aActive,
            casing,
            OVERLAY_FRONT_METEOR_MINER,
            OVERLAY_FRONT_METEOR_MINER_GLOW,
            OVERLAY_FRONT_METEOR_MINER_ACTIVE,
            OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        TSTMultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.machine.MeteorMiner.tooltip.machine_type
        // # Meteor Miner
        // #zh_CN 陨星采矿机
        tt.addMachineType(tr("tst.machine.MeteorMiner.tooltip.machine_type"))
            // #tr tst.machine.MeteorMiner.tooltip.info.08
            // # Controller Block for the Laser Meteor Miner!
            // #zh_CN 激光陨星采矿场的控制器方块！
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.08"))
            // #tr tst.machine.MeteorMiner.tooltip.info.09
            // # To work properly the center of the meteor has to be 48 blocks above the highest block of the multi.
            // #zh_CN 陨星的中心必须位于机器最高点上方48个方块的位置, 机器方可正常工作.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.09"))
            // #tr tst.machine.MeteorMiner.tooltip.info.10
            // # The laser will mine in a radius of up to 40 blocks in each direction from the center of the meteor.
            // #zh_CN 激光将在陨星中心半径40方块内进行采矿作业.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.10"))
            // #tr tst.machine.MeteorMiner.tooltip.info.11
            // # All the chunks involved must be chunkloaded.
            // #zh_CN 所涉及区块都必须保证加载.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.11"))
            // #tr tst.machine.MeteorMiner.tooltip.info.12
            // # The laser will automatically set its radius based on the meteorite,
            // #zh_CN 激光将根据陨石自动设置其运行半径.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.12"))
            // #tr tst.machine.MeteorMiner.tooltip.info.13
            // # if it doesn't find any it will wait for a meteor to spawn,
            // #zh_CN 没有找到陨星时机器会等待陨星生成.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.13"))
            // #tr tst.machine.MeteorMiner.tooltip.info.14
            // # considering the block right above the center of the meteor (like Warded Glass).
            // #zh_CN 顾及了中心正上方的方块(比如守卫者玻璃).
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.14"))
            // #tr tst.machine.MeteorMiner.tooltip.info.hologram
            // # Use the Hologram Projector on the controller to see where the Warded Glass (center of the meteor) and the Master Ritual Stone go.
            // #zh_CN 对控制器使用全息投影仪, 可显示守卫者玻璃(陨星中心)与主仪式石的放置位置.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.hologram"))
            // #tr tst.machine.MeteorMiner.tooltip.info.15
            // # The reset button will restart the machine without optimizing the radius.
            // #zh_CN 点击重启按钮将重启机器, 并且不进行半径适配优化.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.15"))
            // #tr tst.machine.MeteorMiner.tooltip.info.16
            // # Insert the matching schematic in the controller slot. See the schematic for details.
            // #zh_CN 将对应等级的设计图放入控制器槽. 详细信息见图纸.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.16"))
            // #tr tst.machine.MeteorMiner.tooltip.info.energy_hatches
            // # Accepts normal energy hatches or TecTech multi Amp / laser energy hatches, but not mixed.
            // #zh_CN 可使用普通能源仓, 或TecTech高电流/激光能源仓, 但不可混用.
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.energy_hatches"))
            // #tr tst.machine.MeteorMiner.tooltip.info.28
            // # {\BLUE}{\BOLD}Finally some good Meteors!
            // #zh_CN {\BLUE}{\BOLD}终是好陨星! (Finally some good Meteors!)
            .addInfo(tr("tst.machine.MeteorMiner.tooltip.info.28"))
            .beginStructureBlock(shape_T1, shape_T2, shape_T3)
            // #tr tst.machine.MeteorMiner.tooltip.structure.01
            // # {\GOLD}{\BOLD}TIER I
            // #zh_CN {\GOLD}{\BOLD}等级 I
            .addStructureInfo(tr("tst.machine.MeteorMiner.tooltip.structure.01"))
            // #tr tst.machine.MeteorMiner.tooltip.structure.02
            // # Center of the second layer above the ritual
            // #zh_CN 仪式上方第二层的中心
            .addController(tr("tst.machine.MeteorMiner.tooltip.structure.02"))
            // #tr tst.machine.MeteorMiner.tooltip.structure.03
            // # Any Structural Solar Casing around the controller
            // #zh_CN 控制器周边的太阳能塔机械方块
            .addOutputBus(tr("tst.machine.MeteorMiner.tooltip.structure.03"), 1)
            .addEnergyHatch(tr("tst.machine.MeteorMiner.tooltip.structure.03"), 1)
            .addMaintenanceHatch(tr("tst.machine.MeteorMiner.tooltip.structure.03"), 1)
            // #tr tst.machine.MeteorMiner.tooltip.structure.04
            // # ULV only, below the controller
            // #zh_CN 仅限ULV, 控制器下侧
            .addInputBus(tr("tst.machine.MeteorMiner.tooltip.structure.04"), 2)
            // #tr tst.machine.MeteorMiner.tooltip.structure.05
            // # {\GOLD}{\BOLD}TIER II
            // #zh_CN {\GOLD}{\BOLD}等级 II
            .addStructureInfo(tr("tst.machine.MeteorMiner.tooltip.structure.05"))
            // #tr tst.machine.MeteorMiner.tooltip.structure.06
            // # Highest layer of the ritual
            // #zh_CN 仪式最上层
            .addController(tr("tst.machine.MeteorMiner.tooltip.structure.06"))
            // #tr tst.machine.MeteorMiner.tooltip.structure.07
            // # Any Neutronium Casing below the controller
            // #zh_CN 控制器下方的中子采矿机械方块
            .addOutputBus(tr("tst.machine.MeteorMiner.tooltip.structure.07"), 3)
            .addEnergyHatch(tr("tst.machine.MeteorMiner.tooltip.structure.07"), 3)
            .addMaintenanceHatch(tr("tst.machine.MeteorMiner.tooltip.structure.07"), 3)
            // #tr tst.machine.MeteorMiner.tooltip.structure.08
            // # {\GOLD}{\BOLD}TIER III
            // #zh_CN {\GOLD}{\BOLD}等级 III
            .addStructureInfo(tr("tst.machine.MeteorMiner.tooltip.structure.08"))
            // #tr tst.machine.MeteorMiner.tooltip.structure.09
            // # Lower Neutronium Support Casings
            // #zh_CN 下部的中子承载机械方块
            .addOutputBus(tr("tst.machine.MeteorMiner.tooltip.structure.09"), 4)
            .addEnergyHatch(tr("tst.machine.MeteorMiner.tooltip.structure.09"), 4)
            .addMaintenanceHatch(tr("tst.machine.MeteorMiner.tooltip.structure.09"), 4)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    private boolean addInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        // Only an ULV input bus (a single slot) for the fortune pickaxe
        if (!(aMetaTileEntity instanceof MTEHatchInputBus bus) || bus.mTier != 0) return false;
        bus.updateTexture(aBaseCasingIndex);
        addIfSmartInput(bus);
        return mInputBusses.add(bus);
    }

    // endregion

}
