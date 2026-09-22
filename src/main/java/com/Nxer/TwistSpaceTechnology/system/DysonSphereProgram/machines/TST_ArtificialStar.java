package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.tiered_structure_issue;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EnableRenderDefaultArtificialStar;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.secondsOfArtificialStarProgressCycleTime;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.DSPName;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_00;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_01;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_02;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_03;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_04;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_05;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_06;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_launch_01;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DSPInfo_launch_02;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_Details;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.text.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static tectech.thing.casing.TTCasingsContainer.SpacetimeCompressionFieldGenerators;
import static tectech.thing.casing.TTCasingsContainer.StabilisationFieldGenerators;
import static tectech.thing.casing.TTCasingsContainer.TimeAccelerationFieldGenerator;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

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

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.Style;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Tag;
import com.Nxer.TwistSpaceTechnology.util.text.TextEnums;
import com.cleanroommc.modularui.drawable.UITexture;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.block.BlockQuantumGlass;
import tectech.thing.casing.TTCasingsContainer;

@SkipGenerateDescription
public class TST_ArtificialStar extends GTCM_MultiMachineBase<TST_ArtificialStar> {

    // region Class Constructor
    public TST_ArtificialStar(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public TST_ArtificialStar(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ArtificialStar(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "mainArtificialStar";
    private final int horizontalOffSet = 18;
    private final int verticalOffSet = 39;
    private final int depthOffSet = 15;

    // spotless:off
    private final String[][] shapeMain = new String[][]{
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 HHH                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","       H                     H       ","       H                     H       ","       H                     H       ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 HHH                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 H H                 ","               IIIIIII               ","             II       II             ","           II           II           ","          I               I          ","         I                 I         ","         I                 I         ","        I                   I        ","        I                   I        ","       I                     I       ","       I                     I       ","      HI                     IH      ","       I                     I       ","      HI                     IH      ","       I                     I       ","       I                     I       ","        I                   I        ","        I                   I        ","         I                 I         ","         I                 I         ","          I               I          ","           II           II           ","             II       II             ","               IIIIIII               ","                 H H                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                 H H                 ","               IIIIIII               ","                                     ","                                     ","         C                 C         ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","      I                       I      ","     HI                       IH     ","      I                       I      ","     HI                       IH     ","      I                       I      ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","         C                 C         ","                                     ","                                     ","               IIIIIII               ","                 H H                 ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","             II       II             ","                                     ","         D                 D         ","        D                   D        ","                                     ","                                     ","                                     ","      I                       I      ","      I                       I      ","                                     ","                                     ","    HI                         IH    ","     I                         I     ","    HI                         IH    ","                                     ","                                     ","      I                       I      ","      I                       I      ","                                     ","                                     ","                                     ","        D                   D        ","         D                 D         ","                                     ","             II       II             ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","           II           II           ","         C                 C         ","        D                   D        ","       C                     C       ","                                     ","      I                       I      ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","    HI                         IH    ","     I                         I     ","    HI                         IH    ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","      I                       I      ","                                     ","       C                     C       ","        D                   D        ","         C                 C         ","           II           II           ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","          I               I          ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","   HI                           IH   ","    I                           I    ","   HI                           IH   ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","          I               I          ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","         I                 I         ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","   HI                           IH   ","    I                           I    ","   HI                           IH   ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","         I                 I         ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","         I                 I         ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","  HI                             IH  ","   I                             I   ","  HI                             IH  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","         I                 I         ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     "},
        {"                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","        I                   I        ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","  HI                             IH  ","   I                             I   ","  HI                             IH  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","        I                   I        ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     "},
        {"                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","        I                   I        ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","  HI                             IH  ","   I                             I   ","  HI                             IH  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","        I                   I        ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     "},
        {"                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","                                     ","       I                     I       ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "," HI                               IH ","  I                               I  "," HI                               IH ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","       I                     I       ","                                     ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     "},
        {"                 HHH                 ","                 HIH                 ","                 III                 ","                 HHH                 ","                                     ","                 HHH                 ","       I                     I       ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","HHIH H                         H HIHH","HIIH H                         H HIIH","HHIH H                         H HIHH","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","       I                     I       ","                 HHH                 ","                                     ","                 HHH                 ","                 III                 ","                 HIH                 ","                 HHH                 "},
        {"                HCCCH                ","               HHHHHHH               ","            HHHIIIIIIIHHH            ","          HHIII HEEEH IIIHH          ","        HHII     KKK     IIHH        ","       HII      HEEEH      IIH       ","      HI         HHH         IH      ","     HI                       IH     ","    HI                         IH    ","    HI                         IH    ","   HI                           IH   ","   HI                           IH   ","  HI                             IH  ","  HI                             IH  ","  HI                             IH  "," HI                               IH ","HHIH H                         H HIHH","CHIEKEH                       HEKEIHC","CHIEKEH                       HEKEIHC","CHIEKEH                       HEKEIHC","HHIH H                         H HIHH"," HI                               IH ","  HI                             IH  ","  HI                             IH  ","  HI                             IH  ","   HI                           IH   ","   HI                           IH   ","    HI                         IH    ","    HI                         IH    ","     HI                       IH     ","      HI         HHH         IH      ","       HII      HEEEH      IIH       ","        HHII     KKK     IIHH        ","          HHIII HEEEH IIIHH          ","            HHHIIIIIIIHHH            ","               HHHHHHH               ","                HCCCH                "},
        {"                HCGCH                ","                IHFHI                ","               IIIFIII               ","            III HEFEH III            ","          II     KFK     II          ","        II      HEFEH      II        ","       I         HFH         I       ","      I           F           I      ","     I            G            I     ","     I                         I     ","    I                           I    ","    I                           I    ","   I                             I   ","   I                             I   ","   I                             I   ","  I                               I  ","HIIH H                         H HIIH","CHIEKEH                       HEKEIHC","GFFFFFFFG                   GFFFFFFFG","CHIEKEH                       HEKEIHC","HIIH H                         H HIIH","  I                               I  ","   I                             I   ","   I                             I   ","   I                             I   ","    I                           I    ","    I                           I    ","     I                         I     ","     I            G            I     ","      I           F           I      ","       I         HFH         I       ","        II      HEFEH      II        ","          II     KFK     II          ","            III HEFEH III            ","               IIIFIII               ","                IHFHI                ","                HCGCH                "},
        {"                HCCCH                ","               HHHHHHH               ","            HHHIIIIIIIHHH            ","          HHIII HEEEH IIIHH          ","        HHII     KKK     IIHH        ","       HII      HEEEH      IIH       ","      HI         HHH         IH      ","     HI                       IH     ","    HI                         IH    ","    HI                         IH    ","   HI                           IH   ","   HI                           IH   ","  HI                             IH  ","  HI                             IH  ","  HI                             IH  "," HI                               IH ","HHIH H                         H HIHH","CHIEKEH                       HEKEIHC","CHIEKEH                       HEKEIHC","CHIEKEH                       HEKEIHC","HHIH H                         H HIHH"," HI                               IH ","  HI                             IH  ","  HI                             IH  ","  HI                             IH  ","   HI                           IH   ","   HI                           IH   ","    HI                         IH    ","    HI                         IH    ","     HI                       IH     ","      HI         HHH         IH      ","       HII      HEEEH      IIH       ","        HHII     KKK     IIHH        ","          HHIII HEEEH IIIHH          ","            HHHIIIIIIIHHH            ","               HHHHHHH               ","                HCCCH                "},
        {"                 HHH                 ","                 HIH                 ","                 III                 ","                 HHH                 ","                                     ","                 HHH                 ","       I                     I       ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","HHIH H                         H HIHH","HIIH H                         H HIIH","HHIH H                         H HIHH","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","       I                     I       ","                 HHH                 ","                                     ","                 HHH                 ","                 III                 ","                 HIH                 ","                 HHH                 "},
        {"                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","                                     ","       I                     I       ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "," HI                               IH ","  I                               I  "," HI                               IH ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","       I                     I       ","                                     ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     "},
        {"                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","        I                   I        ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","  HI                             IH  ","   I                             I   ","  HI                             IH  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","        I                   I        ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     "},
        {"                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","        I                   I        ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","  HI                             IH  ","   I                             I   ","  HI                             IH  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","        I                   I        ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     "},
        {"                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","                                     ","         I                 I         ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","  HI                             IH  ","   I                             I   ","  HI                             IH  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","         I                 I         ","                                     ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","         I                 I         ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","   HI                           IH   ","    I                           I    ","   HI                           IH   ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","         I                 I         ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","                                     ","          I               I          ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","   HI                           IH   ","    I                           I    ","   HI                           IH   ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","                                     ","                                     ","                                     ","          I               I          ","                                     ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","           II           II           ","         C                 C         ","        D                   D        ","       C                     C       ","                                     ","      I                       I      ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","    HI                         IH    ","     I                         I     ","    HI                         IH    ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","      I                       I      ","                                     ","       C                     C       ","        D                   D        ","         C                 C         ","           II           II           ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","             II       II             ","                                     ","         D                 D         ","        D                   D        ","                                     ","                                     ","                                     ","      I                       I      ","      I                       I      ","                                     ","                                     ","    HI                         IH    ","     I                         I     ","    HI                         IH    ","                                     ","                                     ","      I                       I      ","      I                       I      ","                                     ","                                     ","                                     ","        D                   D        ","         D                 D         ","                                     ","             II       II             ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                 H H                 ","               IIIIIII               ","                                     ","                                     ","         C                 C         ","                                     ","                                     ","                                     ","                                     ","                                     ","      I                       I      ","      I                       I      ","     HI                       IH     ","      I                       I      ","     HI                       IH     ","      I                       I      ","      I                       I      ","                                     ","                                     ","                                     ","                                     ","                                     ","         C                 C         ","                                     ","                                     ","               IIIIIII               ","                 H H                 ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 H H                 ","               IIIIIII               ","             II       II             ","           II           II           ","          I               I          ","         I                 I         ","         I                 I         ","        I                   I        ","        I                   I        ","       I                     I       ","       I                     I       ","      HI                     IH      ","       I                     I       ","      HI                     IH      ","       I                     I       ","       I                     I       ","        I                   I        ","        I                   I        ","         I                 I         ","         I                 I         ","          I               I          ","           II           II           ","             II       II             ","               IIIIIII               ","                 H H                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 H H                 ","                 III                 ","                 III                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","       HII                 IIH       ","        II                 II        ","       HII                 IIH       ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 III                 ","                 III                 ","                 H H                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 H H                 ","                 H H                 ","                 III                 ","                 III                 ","                                     ","                                     ","                                     ","                                     ","                                     ","        HHII     AAA     IIHH        ","          II     AAA     II          ","        HHII     AAA     IIHH        ","                                     ","                                     ","                                     ","                                     ","                                     ","                 III                 ","                 III                 ","                 H H                 ","                 H H                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 H H                 ","                 HIH                 ","                 III                 ","                 III                 ","                 III                 ","                                     ","                 AAA                 ","          HHIII ADDDA IIIHH          ","           IIII ADDDA IIII           ","          HHIII ADDDA IIIHH          ","                 AAA                 ","                                     ","                 III                 ","                 III                 ","                 III                 ","                 HIH                 ","                 H H                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                 HIH                 ","                 H H                 ","                 III                 ","                 III                 ","            HHHIIIIIIIHHH            ","           III IIIAIII III           ","            HHHIIIIIIIHHH            ","                 III                 ","                 III                 ","                 H H                 ","                 HIH                 ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                 HHH                 ","                HHHHH                ","            H  HHHHHHH  H            ","           III HHHAHHH III           ","            H  HHHHHHH  H            ","                HHHHH                ","                 HHH                 ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                IIIII                ","               I     I               ","              I       I              ","             I   KKK   I             ","            HI  K   K  IH            ","           III  K A K  III           ","            HI  K   K  IH            ","             I   KKK   I             ","              I       I              ","               I     I               ","                IIIII                ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                 KKK                 ","           H    K   K    H           ","          III   K A K   III          ","           H    K   K    H           ","                 KKK                 ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                 KKK                 ","           H    K   K    H           ","          III   K A K   III          ","           H    K   K    H           ","                 KKK                 ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                BBBBB                ","              BB     BB              ","             B         B             ","             B         B             ","            B    KKK    B            ","          H B   K   K   B H          ","         IIIB   K A K   BIII         ","          H B   K   K   B H          ","            B    KKK    B            ","             B         B             ","             B         B             ","              BB     BB              ","                BBBBB                ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                 KKK                 ","          H     K   K     H          ","         III    K A K    III         ","          H     K   K     H          ","                 KKK                 ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                 KKK                 ","         H      K   K      H         ","        III     K A K     III        ","         H      K   K      H         ","                 KKK                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                BBBBB                ","              BB     BB              ","            BB         BB            ","            B           B            ","           B             B           ","           B             B           ","          B      KKK      B          ","        H B     K   K     B H        ","       IIIB     K A K     BIII       ","        H B     K   K     B H        ","          B      KKK      B          ","           B             B           ","           B             B           ","            B           B            ","            BB         BB            ","              BB     BB              ","                BBBBB                ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                  I                  ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 LLL                 ","                HHHHH                ","       H       LHHHHHL       H       ","     IIII      LHHAHHL      IIII     ","       H       LHHHHHL       H       ","                HHHHH                ","                 LLL                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                  I                  ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                                     ","                  I                  ","                  I                  ","                 HIH                 ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                 L~L                 ","                HHKHH                ","     HH        LHHAHHL        HH     ","   IIIII        KAAAK        IIIII   ","     HH        LHHAHHL        HH     ","                HHKHH                ","                 L L                 ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                 HIH                 ","                 HIH                 ","                  I                  ","                  I                  ","                                     ","                                     ","                                     "},
        {"                                     ","                                     ","                 H H                 ","                 HIH                 ","                 HIH                 ","                  I                  ","                  I                  ","               BBBBBBB               ","             BB       BB             ","           BB           BB           ","          B               B          ","         B                 B         ","         B                 B         ","        B                   B        ","        B                   B        ","       B         LLL         B       ","       B        HHHHH        B       ","  HHH  B       LHHHHHL       B  HHH  ","   IIIIB       LHHHHHL       BIIII   ","  HHH  B       LHHHHHL       B  HHH  ","       B        HHHHH        B       ","       B         LLL         B       ","        B                   B        ","        B                   B        ","         B                 B         ","         B                 B         ","          B               B          ","           BB           BB           ","             BB       BB             ","               BBBBBBB               ","                  I                  ","                  I                  ","                 HIH                 ","                 HIH                 ","                 H H                 ","                                     ","                                     "},
        {"                JJJJJ                ","               JJJJJJJ               ","               JJJJJJJ               ","               JJJJJJJ               ","               JJJJJJJ               ","              JJJJJJJJJ              ","            JJJJJJJJJJJJJ            ","           JJJJJJJJJJJJJJJ           ","         JJJJJJJJJJJJJJJJJJJ         ","        JJJJJJJJJJJJJJJJJJJJJ        ","        JJJJJJJJJJJJJJJJJJJJJ        ","       JJJJJJJJJJJJJJJJJJJJJJJ       ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","     JJJJJJJJJJJJJJJJJJJJJJJJJJJ     "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ ","     JJJJJJJJJJJJJJJJJJJJJJJJJJJ     ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","       JJJJJJJJJJJJJJJJJJJJJJJ       ","        JJJJJJJJJJJJJJJJJJJJJ        ","        JJJJJJJJJJJJJJJJJJJJJ        ","         JJJJJJJJJJJJJJJJJJJ         ","           JJJJJJJJJJJJJJJ           ","            JJJJJJJJJJJJJ            ","              JJJJJJJJJ              ","               JJJJJJJ               ","               JJJJJJJ               ","               JJJJJJJ               ","               JJJJJJJ               ","                JJJJJ                "},
        {"              JJJJJJJJJ              ","           JJJJJJJJJJJJJJJ           ","         JJJJJJJJJJJJJJJJJJJ         ","        JJJJJJJJJJJJJJJJJJJJJ        ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","     JJJJJJJJJJJJJJJJJJJJJJJJJJJ     ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","   JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ   ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  ","   JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ   ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","     JJJJJJJJJJJJJJJJJJJJJJJJJJJ     ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","        JJJJJJJJJJJJJJJJJJJJJ        ","         JJJJJJJJJJJJJJJJJJJ         ","           JJJJJJJJJJJJJJJ           ","              JJJJJJJJJ              "},
        {"              JJJJJJJJJ              ","           JJJJJJJJJJJJJJJ           ","         JJJJJJJJJJJJJJJJJJJ         ","        JJJJJJJJJJJJJJJJJJJJJ        ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","     JJJJJJJJJJJJJJJJJJJJJJJJJJJ     ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","   JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ   ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "," JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  ","  JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ  ","   JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ   ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","    JJJJJJJJJJJJJJJJJJJJJJJJJJJJJ    ","     JJJJJJJJJJJJJJJJJJJJJJJJJJJ     ","      JJJJJJJJJJJJJJJJJJJJJJJJJ      ","        JJJJJJJJJJJJJJJJJJJJJ        ","         JJJJJJJJJJJJJJJJJJJ         ","           JJJJJJJJJJJJJJJ           ","              JJJJJJJJJ              "}
    };
    // spotless:on

    /*
     * A -> ofBlock...(gt.blockcasings, 14, ...); // tierDimensionField
     * B -> ofBlock...(gt.blockcasingsSE, 2, ...);
     * C -> ofBlock...(gt.blockcasingsTT, 4, ...);
     * D -> ofBlock...(gt.blockcasingsTT, 14, ...); // tierTimeField
     * E -> ofBlock...(gt.blockcasingsTT, 7, ...);
     * F -> ofBlock...(gt.blockcasingsTT, 8, ...);
     * G -> ofBlock...(gt.blockcasingsTT, 9, ...); // tierStabilisationField
     * H -> ofBlock...(gt.blockcasingsTT, 12, ...);
     * I -> ofBlock...(gt.blockcasingsTT, 13, ...);
     * J -> ofBlock...(tile.DysonSwarmPart, 9, ...);
     * K -> ofBlock...(tile.quantumGlass, 0, ...);
     * L -> ofBlock...(gt.blockcasingsTT, 12, ...); // Hatch
     */
    @Override
    public IStructureDefinition<TST_ArtificialStar> getStructureDefinition() {
        return IStructureDefinition.<TST_ArtificialStar>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
            .addElement(
                'A',
                withChannel(
                    "tierdimensionfield",
                    ofBlocksTiered(
                        TST_ArtificialStar::getTierDimensionFieldBlockFromBlock,
                        ImmutableList.of(
                            Pair.of(GregTechAPI.sBlockCasings1, 14),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 0),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 1),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 2),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 3),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 4),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 5),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 6),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 7),
                            Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 8)),
                        -1,
                        (t, m) -> t.tierDimensionField = m,
                        t -> t.tierDimensionField)))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasingsSE, 2))
            .addElement('C', ofBlock(sBlockCasingsTT, 4))
            .addElement(
                'D',
                withChannel(
                    "tiertimefield",
                    ofBlocksTiered(
                        TST_ArtificialStar::getTierTimeFieldBlockFromBlock,
                        ImmutableList.of(
                            Pair.of(sBlockCasingsTT, 14),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 0),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 1),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 2),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 3),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 4),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 5),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 6),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 7),
                            Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)),
                        -1,
                        (t, m) -> t.tierTimeField = m,
                        t -> t.tierTimeField)))
            .addElement('E', ofBlock(sBlockCasingsTT, 7))
            .addElement('F', ofBlock(sBlockCasingsTT, 8))
            .addElement(
                'G',
                withChannel(
                    "tierstabilisationfield",
                    ofBlocksTiered(
                        TST_ArtificialStar::getTierStabilisationFieldBlockFromBlock,
                        ImmutableList.of(
                            Pair.of(sBlockCasingsTT, 9),
                            Pair.of(StabilisationFieldGenerators, 0),
                            Pair.of(StabilisationFieldGenerators, 1),
                            Pair.of(StabilisationFieldGenerators, 2),
                            Pair.of(StabilisationFieldGenerators, 3),
                            Pair.of(StabilisationFieldGenerators, 4),
                            Pair.of(StabilisationFieldGenerators, 5),
                            Pair.of(StabilisationFieldGenerators, 6),
                            Pair.of(StabilisationFieldGenerators, 7),
                            Pair.of(StabilisationFieldGenerators, 8)),
                        -1,
                        (t, m) -> t.tierStabilisationField = m,
                        t -> t.tierStabilisationField)))
            .addElement('H', ofBlock(sBlockCasingsTT, 12))
            .addElement('I', ofBlock(sBlockCasingsTT, 13))
            .addElement('J', ofBlock(GregTechAPI.sBlockCasingsDyson, 9))
            .addElement('K', ofBlock(BlockQuantumGlass.INSTANCE, 0))
            .addElement(
                'L',
                HatchElementBuilder.<TST_ArtificialStar>builder()
                    .atLeast(InputBus, OutputBus)
                    .adder(TST_ArtificialStar::addInputBusOrOutputBusToMachineList)
                    .hint(1)
                    .casingIndex(1024 + 12)
                    .buildAndChain(sBlockCasingsTT, 12))
            .build();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        mInputBusses.clear();
        tierDimensionField = -1;
        tierTimeField = -1;
        tierStabilisationField = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) return;
        if (tierDimensionField < 0 || tierTimeField < 0 || tierStabilisationField < 0) {
            errors.add(tiered_structure_issue);
            return;
        }
        // Only allow and must be 1 input bus
        checkHatchExact(errors, HatchElement.InputBus, 1);
        calculateOutputMultiplier();
        recoveryChance = (short) (tierDimensionField * tierTimeField * tierStabilisationField);
    }
    // endregion

    // region Processing Logic
    protected static TST_ItemID Antimatter;
    protected static TST_ItemID AntimatterFuelRod;
    protected static TST_ItemID StrangeAnnihilationFuelRod;
    protected static long MaxOfAntimatter;
    protected static long MaxOfAntimatterFuelRod;
    protected static long MaxOfStrangeAnnihilationFuelRod;
    private String ownerName;
    private UUID ownerUUID;
    private long storageEU = 0;
    private int tierDimensionField = -1;
    private int tierTimeField = -1;
    private int tierStabilisationField = -1;
    private double outputMultiplier = 1;
    private short recoveryChance = 0;
    private byte rewardContinuous = 0;
    private long currentOutputEU = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.0");
    private final DecimalFormat scientificFormat = new DecimalFormat("0.#E0");
    private boolean isRendering = false;
    private byte enableRender = EnableRenderDefaultArtificialStar;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.ArtificialStarGeneratingRecipes;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
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
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    // disable crafting input bus/buffer
    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // iterate input bus slot
        // consume fuel and generate EU
        boolean flag = false;
        long recoveryAmount = 0;
        // * Integer.MAX_VALUE
        currentOutputEU = 0;
        for (ItemStack items : getStoredInputs()) {
            if (Antimatter.equalItemStack(items)) {
                currentOutputEU += MaxOfAntimatter * items.stackSize;
                flag = true;
            } else if (AntimatterFuelRod.equalItemStack(items)) {
                currentOutputEU += MaxOfAntimatterFuelRod * items.stackSize;
                recoveryAmount += items.stackSize;
                flag = true;
            } else if (StrangeAnnihilationFuelRod.equalItemStack(items)) {
                currentOutputEU += MaxOfStrangeAnnihilationFuelRod * items.stackSize;
                recoveryAmount += items.stackSize;
                flag = true;
            }
            // whether the item is fuel
            // void it
            items.stackSize = 0;
        }

        // flush input slots
        updateSlots();

        // if no antimatter or fuel rod input
        if (!flag) {
            // set 0 to multiplier of rewarding continuous operation
            rewardContinuous = 0;
            // stop render
            if (isRendering) {
                destroyRenderBlock();
                isRendering = false;
            }
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // add EU to the wireless EU net
        BigInteger eu = BigInteger
            .valueOf((long) (currentOutputEU * outputMultiplier * ((rewardContinuous + 100d) / 100d)))
            .multiply(TstUtils.INTEGER_MAX_VALUE);
        if (!addEUToGlobalEnergyMap(ownerUUID, eu)) {
            return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        // set progress time with cfg
        mMaxProgresstime = (int) (20 * secondsOfArtificialStarProgressCycleTime);
        // chance to recover FrameMaterial
        if (recoveryChance == 1000) {
            if (recoveryAmount > 0) {
                mOutputItems = getRecovers(recoveryAmount);
            }
        } else if (XSTR.XSTR_INSTANCE.nextInt(1000) < recoveryChance) {
            if (recoveryAmount > 0) {
                mOutputItems = getRecovers(recoveryAmount);
            }
        }

        // increase multiplier of rewarding continuous operation
        if (rewardContinuous < 50) rewardContinuous++;

        // start render
        if (enableRender != 0 && !isRendering) {
            createRenderBlock();
            isRendering = true;
        }
        return CheckRecipeResultRegistry.GENERATING;
    }

    public static void initStatics() {
        Antimatter = TST_ItemID.createNoNBT(GTCMItemList.Antimatter.get(1));
        AntimatterFuelRod = TST_ItemID.createNoNBT(GTCMItemList.AntimatterFuelRod.get(1));
        StrangeAnnihilationFuelRod = TST_ItemID.createNoNBT(GTCMItemList.StrangeAnnihilationFuelRod.get(1));
        MaxOfAntimatter = Config.EUEveryAntimatter / Integer.MAX_VALUE;
        MaxOfAntimatterFuelRod = Config.EUEveryAntimatterFuelRod / Integer.MAX_VALUE;
        MaxOfStrangeAnnihilationFuelRod = Config.EUEveryStrangeAnnihilationFuelRod / Integer.MAX_VALUE;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("isActive")) {
            currentTip.add(EnumChatFormatting.AQUA +
            // #tr Waila.TST_ArtificialStar.1
            // # Current Generating:
            // #zh_CN 当前发电:
                TextEnums.tr("Waila.TST_ArtificialStar.1")
                + " "
                + EnumChatFormatting.GOLD
                + String.format("%,d", tag.getLong("currentOutputEU"))
                + EnumChatFormatting.RED
                + " * "
                + decimalFormat.format(tag.getDouble("outputMultiplier"))
                + EnumChatFormatting.GREEN
                + " * 2,147,483,647"
                + EnumChatFormatting.RESET
                + " EU / "
                + secondsOfArtificialStarProgressCycleTime
                + " s");

            BigDecimal currentEuOutput = BigDecimal.valueOf(tag.getLong("currentOutputEU"))
                .multiply(BigDecimal.valueOf(tag.getDouble("outputMultiplier")))
                .multiply(BigDecimal.valueOf(2147483647))
                .divide(BigDecimal.valueOf(secondsOfArtificialStarProgressCycleTime), RoundingMode.DOWN)
                .divide(BigDecimal.valueOf(20), RoundingMode.DOWN);

            String currentEuOutputScientificFormat = scientificFormat.format(currentEuOutput);
            currentTip.add(
                EnumChatFormatting.AQUA + TextEnums.tr("Waila.TST_ArtificialStar.1")
                    + " "
                    + EnumChatFormatting.GOLD
                    + currentEuOutputScientificFormat
                    + EnumChatFormatting.RESET
                    + " EU / t");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            if (tileEntity.isActive()) {
                tag.setLong("currentOutputEU", currentOutputEU);
                tag.setDouble("outputMultiplier", (outputMultiplier * (rewardContinuous + 100) / 100));
            }
        }
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 6];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.GOLD +
        // spotless:off
            // #tr TST_ArtificialStar.getInfoData.00
            // # Reward for continuous operation
            // #zh_CN 连续运行奖励
            TextEnums.tr("TST_ArtificialStar.getInfoData.00")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.GREEN+(rewardContinuous+100)+"%";

        ret[origin.length + 1] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.01
            // # Generating Multiplier
            // #zh_CN 发电倍率
            TextEnums.tr("TST_ArtificialStar.getInfoData.01")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.GREEN+outputMultiplier;

        ret[origin.length + 2] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.02
            // # Dimension Field Tier
            // #zh_CN 空间场等级
            TextEnums.tr("TST_ArtificialStar.getInfoData.02")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.YELLOW+tierDimensionField;

        ret[origin.length + 3] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.03
            // # Time Field Tier
            // #zh_CN 时间场等级
            TextEnums.tr("TST_ArtificialStar.getInfoData.03")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.YELLOW+tierTimeField;

        ret[origin.length + 4] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.04
            // # Stabilisation Field Tier
            // #zh_CN 稳定场等级
            TextEnums.tr("TST_ArtificialStar.getInfoData.04")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.YELLOW+tierStabilisationField;

        ret[origin.length + 5] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.05
            // # Recover material chance
            // #zh_CN 回收原料几率
            TextEnums.tr("TST_ArtificialStar.getInfoData.05")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.AQUA+recoveryChance+EnumChatFormatting.RESET+"/"+EnumChatFormatting.AQUA+"1000";
            // spotless:on
        return ret;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack tool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.enableRender = (byte) ((this.enableRender + 1) % 2);
            GTUtility.sendChatTrans(
                aPlayer,
                StatCollector.translateToLocal("ArtificialStar.enableRender." + this.enableRender));
            if (enableRender == 0 && isRendering) {
                destroyRenderBlock();
                isRendering = false;
            }
        }
    }

    protected ItemStack[] getRecovers(long amount) {
        if (amount <= Integer.MAX_VALUE) {
            return new ItemStack[] { StellarConstructionFrameMaterial.get((int) amount) };
        } else {
            int stack = (int) (amount / Integer.MAX_VALUE);
            int remainder = (int) (amount % Integer.MAX_VALUE);
            ItemStack[] r = new ItemStack[remainder > 0 ? stack + 1 : stack];
            ItemStack t = StellarConstructionFrameMaterial.get(Integer.MAX_VALUE);
            for (int i = 0; i < stack; i++) {
                r[i] = t.copy();
            }
            if (remainder > 0) r[stack] = GTUtility.copyAmountUnsafe(remainder, t);
            return r;
        }
    }

    // Artificial Star Output multiplier
    private void calculateOutputMultiplier() {
        // tTime^0.25 * tDim^0.25 * 1.588186^(tStabilisation-2)
        // (100^0.25)*(1.588186^(10-2))) = 128.000
        // 1.588186^(-1) = 0.629
        this.outputMultiplier = Math.pow(1d * tierTimeField * tierDimensionField, 0.25d)
            * Math.pow(1.588186d, tierStabilisationField - 2);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            this.ownerName = aBaseMetaTileEntity.getOwnerName();
            this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (isRendering && mMaxProgresstime == 0 && rewardContinuous == 0) {
            isRendering = false;
            destroyRenderBlock();
        }
        if (rewardContinuous != 0 && mMaxProgresstime == 0) rewardContinuous = 0;
    }

    @Override
    public void onBlockDestroyed() {
        if (isRendering) {
            isRendering = false;
            destroyRenderBlock();
        }
        super.onBlockDestroyed();
    }

    public static int getTierDimensionFieldBlockFromBlock(Block block, int meta) {
        if (block == GregTechAPI.sBlockCasings1 && 14 == meta) return 1;
        if (block == SpacetimeCompressionFieldGenerators) return meta + 2;
        return -1;
    }

    public static int getTierTimeFieldBlockFromBlock(Block block, int meta) {
        if (block == sBlockCasingsTT && 14 == meta) return 1;
        if (block == TimeAccelerationFieldGenerator) return meta + 2;
        return -1;
    }

    public static int getTierStabilisationFieldBlockFromBlock(Block block, int meta) {
        if (block == sBlockCasingsTT && 9 == meta) return 1;
        if (block == StabilisationFieldGenerators) return meta + 2;
        return -1;
    }

    public void createRenderBlock() {
        int x = getBaseMetaTileEntity().getXCoord();
        int y = getBaseMetaTileEntity().getYCoord();
        int z = getBaseMetaTileEntity().getZCoord();

        double xOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetX;
        double zOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetZ;
        double yOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), TstBlocks.BlockStar);
        /*
         * This wouldn't work since there are no Network System. Todo: Add a message to SERVER.
         * if (getBaseMetaTileEntity().getWorld()
         * .getTileEntity((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset)) instanceof TileStar star)
         * star.size = Math.min(currentOutputEU, 1024) / 204.8;
         */
    }

    public void destroyRenderBlock() {
        int x = getBaseMetaTileEntity().getXCoord();
        int y = getBaseMetaTileEntity().getYCoord();
        int z = getBaseMetaTileEntity().getZCoord();

        double xOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetX;
        double zOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetZ;
        double yOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("storageEU", storageEU);
        aNBT.setInteger("tierDimensionField", tierDimensionField);
        aNBT.setInteger("tierTimeField", tierTimeField);
        aNBT.setInteger("tierStabilisationField", tierStabilisationField);
        aNBT.setDouble("outputMultiplier", outputMultiplier);
        aNBT.setByte("rewardContinuous", rewardContinuous);
        aNBT.setLong("currentOutputEU", currentOutputEU);
        aNBT.setBoolean("isRendering", isRendering);
        aNBT.setByte("enableRender", enableRender);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        storageEU = aNBT.getLong("storageEU");
        tierDimensionField = aNBT.getInteger("tierDimensionField");
        tierTimeField = aNBT.getInteger("tierTimeField");
        tierStabilisationField = aNBT.getInteger("tierStabilisationField");
        outputMultiplier = aNBT.getDouble("outputMultiplier");
        rewardContinuous = aNBT.getByte("rewardContinuous");
        currentOutputEU = aNBT.getLong("currentOutputEU");
        isRendering = aNBT.getBoolean("isRendering");
        enableRender = aNBT.getByte("enableRender");
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) {
                return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][12] };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_ArtificialStar_MachineType
        // # Dyson Sphere Program: Annihilation Generator
        // #zh_CN 戴森球计划: 湮灭发电机
        tt.addMachineType(TextEnums.tr("Tooltip_ArtificialStar_MachineType"))
            // #tr Tooltip_ArtificialStar_Controller
            // # Controller block for the Artificial Star
            // #zh_CN 人造恒星的控制器方块
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_Controller"))
            // #tr Tooltip_ArtificialStar_00
            // # {\LIGHT_PURPLE}{\BOLD}All you need to do is to let the proton and antiproton beams
            // #zh_CN {\LIGHT_PURPLE}{\BOLD}你只需要让正反质子束从两端静静地穿
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_00"))
            // #tr Tooltip_ArtificialStar_01
            // # {\LIGHT_PURPLE}{\BOLD} pass silently from both ends into the annihilation constrainer. Easy peasy!
            // #zh_CN {\LIGHT_PURPLE}{\BOLD} 过磁场进入约束球就可以了, 轻松愉快!
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_01"))
            // #tr Tooltip_ArtificialStar_02
            // # It owes its simple shape to the elegance of the theory.
            // #zh_CN 它简单的外形归功于理论的优雅.
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_02"))
            // #tr Tooltip_ArtificialStar_03
            // # Actual output power is affected by {\GOLD}3{\GRAY} types tiered block.
            // #zh_CN 实际产生能量受三种等级方块影响.
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_03"))
            // #tr Tooltip_ArtificialStar_04
            // # At the same time, higher tier increase the probability of recovering materials.
            // #zh_CN 同时, 更高等级会提高回收原料的概率.
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_04"))
            // #tr Tooltip_ArtificialStar_05
            // # Continuous operation improves power generation.
            // #zh_CN 保持连续运行也会提高能量输出.
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_05"))
            // #tr Tooltip_ArtificialStar_06
            // # Only and must install {\GOLD}1{\GRAY} input bus.
            // #zh_CN 只允许且必须安装一个输入总线.
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_06"))
            // #tr Tooltip_ArtificialStar_07
            // # Energy will output to Wireless EU Net directly.
            // #zh_CN 能量将直接输出到无线EU网络.
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_07"))
            // #tr Tooltip_ArtificialStar_08
            // # Use screwdriver to enable/disable animations.
            // #zh_CN 使用螺丝刀开启/关闭动画特效.
            .addInfo(TextEnums.tr("Tooltip_ArtificialStar_08"))
            .addStructureInfo(Tooltip_Details)
            // #tr Tooltip_ArtificialStar_02_01
            // # Output multiplier = tTime^0.25 * tDim^0.25 * 1.588186^(tStabilisation - 2)
            // #zh_CN 输出系数 = 时间场等级^0.25 * 空间场等级^0.25 * 1.588186^(稳定场等级 - 2)
            .addStructureInfo(TextEnums.tr("Tooltip_ArtificialStar_02_01"))
            // #tr Tooltip_ArtificialStar_02_02
            // # Actual Generating EU = recipe value * output multiplier * Rewards for continuous operation
            // #zh_CN 实际输出EU = 配方数值 * 输出系数 * 连续运行奖励系数
            .addStructureInfo(TextEnums.tr("Tooltip_ArtificialStar_02_02"))
            // #tr Tooltip_ArtificialStar_02_03
            // # Recovering probability = tTime * tDim * tStabilisation / 1000
            // #zh_CN 原料回收概率 = 时间场等级 * 空间场等级 * 稳定场等级 / 1000
            .addStructureInfo(TextEnums.tr("Tooltip_ArtificialStar_02_03"))
            // #tr Tooltip_ArtificialStar_02_04
            // # Input fuels will be consumed at once, process 6.4s (default), and output the corresponding EU.
            // #zh_CN 输入的燃料将会一次性消耗掉, 然后运行 6.4s (默认), 并输出对应数值的 EU.
            .addStructureInfo(TextEnums.tr("Tooltip_ArtificialStar_02_04"))
            // #tr Tooltip_ArtificialStar_02_05
            // # Rewards multiplier 1%% increase per run when continuous operation.
            // #zh_CN 在连续运行时, 每次运行连续运行奖励系数提高 1%% .
            .addStructureInfo(TextEnums.tr("Tooltip_ArtificialStar_02_05"))
            // #tr Tooltip_ArtificialStar_02_06
            // # Maximum is 150%%, Minimum is 100%% when uncontinuous.
            // #zh_CN 连续运行奖励系数最大值 150%% , 中断后降到 100%% .
            .addStructureInfo(TextEnums.tr("Tooltip_ArtificialStar_02_06"))
          .addStructureInfo(EnumChatFormatting.GOLD+"-----------------------------------------")
          .addStructureInfo(DSPName + ":")
          .addStructureInfo(Tooltip_DSPInfo_launch_01)
          .addStructureInfo(Tooltip_DSPInfo_launch_02)
          .addStructureInfo(Tooltip_DSPInfo_00)
          .addStructureInfo(Tooltip_DSPInfo_01)
          .addStructureInfo(Tooltip_DSPInfo_02)
          .addStructureInfo(Tooltip_DSPInfo_03)
          .addStructureInfo(Tooltip_DSPInfo_04)
          .addStructureInfo(Tooltip_DSPInfo_05)
          .addStructureInfo(Tooltip_DSPInfo_06)
          .addStructureInfo(EnumChatFormatting.GOLD+"-----------------------------------------")
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public Style getTooltipCreditStyle() {
        return Style.DYSON_SPHERE;
    }

    @Override
    public Tag[] getTooltipCreditTags() {
        return new Tag[] { Tag.DYSON_SPHERE };
    }

    // endregion

}
