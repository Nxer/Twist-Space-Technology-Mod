package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SPACE_ELEVATOR_BASE_CASING_INDEX;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUPerCriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.util.text.TextEnums.tr;
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
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static tectech.thing.CustomItemList.astralArrayFabricator;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataCell;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.IDSP_IO;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.Style;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Tag;
import com.Nxer.TwistSpaceTechnology.util.text.TextEnums;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.items.ItemIntegratedCircuit;
import gtPlusPlus.core.block.ModBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.block.BlockQuantumGlass;

@SkipGenerateDescription
public class TST_DSPReceiver extends GTCM_MultiMachineBase<TST_DSPReceiver>
    implements IConstructable, ISurvivalConstructable, IDSP_IO {

    // region Class Constructor
    public TST_DSPReceiver(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public TST_DSPReceiver(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_DSPReceiver(this.mName);
    }
    // endregion

    // region Structure
    protected static final String STRUCTURE_PIECE_MAIN = "mainDSPReceiver";
    protected final int horizontalOffSet = 23;
    protected final int verticalOffSet = 45;
    protected final int depthOffSet = 12;

    // spotless:off
	protected final String[][] shapeMain = new String[][]{
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       D                       ","                     DD DD                     ","                    D     D                    ","                    D  D  D                    ","                   DDDDKDDDD                   ","                    D  D  D                    ","                    D     D                    ","                     DD DD                     ","                       D                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      DDD                      ","                    DDHRHDD                    ","                    DIHIHID                    ","                   DHHIHIHHD                   ","                  CDRIHIHIRDC                  ","                   DHHIHIHHD                   ","                    DIHIHID                    ","                    DDHRHDD                    ","                      DDD                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     DDDDD                     ","                    DJJJJJD                    ","                   DJJJJJJJD                   ","                  CDJJJJJJJDC                  ","                  CDJJJJJJJDC                  ","                  CDJJJJJJJDC                  ","                   DJJJJJJJD                   ","                    DJJJJJD                    ","                     DDDDD                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      DDD                      ","                    DDJJJDD                    ","                    DJJJJJD                    ","                  CDJJJJJJJDC                  ","                  CDJJJJJJJDC                  ","                  CDJJJJJJJDC                  ","                    DJJJJJD                    ","                    DDJJJDD                    ","                      DDD                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       D                       ","                     DDJDD                     ","                    DJJJJJD                    ","                  C DJJJJJD C                  ","                   DJJJJJJJD                   ","                  C DJJJJJD C                  ","                    DJJJJJD                    ","                     DDJDD                     ","                       D                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      DDD                      ","                     DDJDD                     ","                  C DDJJJDD C                  ","                    DJJJJJD                    ","                  C DDJJJDD C                  ","                     DDJDD                     ","                      DDD                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       D                       ","                   C  DDD  C                   ","                     DDDDD                     ","                   C  DDD  C                   ","                       D                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    C     C                    ","                                               ","                    C     C                    ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                     CCCCC                     ","                     CCCCC                     ","                     CCCCC                     ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                      CCC                      ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      HHH                      ","                    HH   HH                    ","                   H       H                   ","                   H       H                   ","                  H   QCQ   H                  ","                  H   CCC   H                  ","                  H   QCQ   H                  ","                   H       H                   ","                   H       H                   ","                    HH   HH                    ","                      HHH                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      QCQ                      ","                      CCC                      ","                      QCQ                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      HHH                      ","                    HH   HH                    ","                   H       H                   ","                   H       H                   ","                  H   QCQ   H                  ","                  H   CCC   H                  ","                  H   QCQ   H                  ","                   H       H                   ","                   H       H                   ","                    HH   HH                    ","                      HHH                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      QCQ                      ","                      CCC                      ","                      QCQ                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      QCQ                      ","                      CCC                      ","                      QCQ                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      QCQ                      ","                     QCCCQ                     ","                     CCCCC                     ","                     QCCCQ                     ","                      QCQ                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     CQCQC                     ","                     QCCCQ                     ","                     CCCCC                     ","                     QCCCQ                     ","                     CQCQC                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    CQQCQQC                    ","                    Q     Q                    ","                    Q  A  Q                    ","                    C A A C                    ","                    Q  A  Q                    ","                    Q     Q                    ","                    CQQCQQC                    ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    CQQCQQC                    ","                    Q     Q                    ","                    Q  A  Q                    ","                    C A A C                    ","                    Q  A  Q                    ","                    Q     Q                    ","                    CQQCQQC                    ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   CQ QCQ QC                   ","                   Q       Q                   ","                                               ","                   Q   A   Q                   ","                   C  A A  C                   ","                   Q   A   Q                   ","                                               ","                   Q       Q                   ","                   CQ QCQ QC                   ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                   CQ QCQ QC                   ","                   Q  LLL  Q                   ","                     L   L                     ","                   QL  A  LQ                   ","                   CL A A LC                   ","                   QL  A  LQ                   ","                     L   L                     ","                   Q  LLL  Q                   ","                   CQ QCQ QC                   ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                       B                       ","                                               ","                                               ","                                               ","                                               ","                                               ","        B                             B        ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                  CQ  QCQ  QC                  ","                  Q         Q                  ","                                               ","                                               ","                  Q    A    Q                  ","  B               C   A A   C               B  ","                  Q    A    Q                  ","                                               ","                                               ","                  Q         Q                  ","                  CQ  QCQ  QC                  ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","        B                             B        ","                                               ","                                               ","                                               ","                                               ","                                               ","                       B                       ","                                               ","                                               "},
        {"                                               ","                                               ","                       B                       ","                                               ","                                               ","                                               ","                                               ","                                               ","        B                             B        ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                CQ    QCQ    QC                ","                QCQ   QCQ   QCQ                ","                 Q           Q                 ","                      LLL                      ","                    LL   LL                    ","                    L     L                    ","                QQ L   A   L QQ                ","  B             CC L  A A  L CC             B  ","                QQ L   A   L QQ                ","                    L     L                    ","                    LL   LL                    ","                      LLL                      ","                 Q           Q                 ","                QCQ   QCQ   QCQ                ","                CQ    QCQ    QC                ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","        B                             B        ","                                               ","                                               ","                                               ","                                               ","                                               ","                       B                       ","                                               ","                                               "},
        {"                                               ","                                               ","                       B                       ","                                               ","                                               ","                                               ","                                               ","                                               ","        B                             B        ","                                               ","                                               ","                                               ","                                               ","                                               ","              CQ      QCQ      QC              ","              QCQ     QCQ     QCQ              ","               Q               Q               ","                                               ","                                               ","                                               ","                                               ","                                               ","              QQ       A       QQ              ","  B           CC      A A      CC           B  ","              QQ       A       QQ              ","                                               ","                                               ","                                               ","                                               ","                                               ","               Q               Q               ","              QCQ     QCQ     QCQ              ","              CQ      QCQ      QC              ","                                               ","                                               ","                                               ","                                               ","                                               ","        B                             B        ","                                               ","                                               ","                                               ","                                               ","                                               ","                       B                       ","                                               ","                                               "},
        {"                                               ","                      CCC                      ","                      QCQ                      ","                      QCQ                      ","                      QCQ                      ","                      QCQ                      ","                      QCQ                      ","        C             QCQ             C        ","       CCQ            QCQ            QCC       ","        QCQ           QCQ           QCQ        ","         QCQ          QCQ          QCQ         ","          QCQ         QCQ         QCQ          ","           QCQ        QCQ        QCQ           ","            QCQ       QCQ       QCQ            ","             Q                   Q             ","                                               ","                                               ","                      LLL                      ","                    LL   LL                    ","                   L       L                   ","                  L         L                  ","                  L         L                  "," CQQQQQQQQQQQQ   L     A     L   QQQQQQQQQQQQC "," CCCCCCCCCCCCC   L    A A    L   CCCCCCCCCCCCC "," CQQQQQQQQQQQQ   L     A     L   QQQQQQQQQQQQC ","                  L         L                  ","                  L         L                  ","                   L       L                   ","                    LL   LL                    ","                      LLL                      ","                                               ","                                               ","             Q                   Q             ","            QCQ       QCQ       QCQ            ","           QCQ        QCQ        QCQ           ","          QCQ         QCQ         QCQ          ","         QCQ          QCQ          QCQ         ","        QCQ           QCQ           QCQ        ","       CCQ            QCQ            QCC       ","        C             QCQ             C        ","                      QCQ                      ","                      QCQ                      ","                      QCQ                      ","                      QCQ                      ","                      QCQ                      ","                      CCC                      ","                                               "},
        {"                      CCC                      ","                   CCCCCCCCC                   ","                CCC    H    CCC                ","             CCC               CCC             ","            C                     C            ","          CC                       CC          ","        CC                           CC        ","       CC                             CC       ","      CCH                             HCC      ","      C                                 C      ","     C                                   C     ","     C                                   C     ","    C                                     C    ","   C                DDDDDDD                C   ","   C              DD       DD              C   ","   C             D   OOOOO   D             C   ","  C             D  OOOOOOOOO  D             C  ","  C            D  OOOOOOOOOOO  D            C  ","  C           D  OOOOOOOOOOOOO  D           C  "," C            D OOOOOOOOOOOOOOO D            C "," C           D  OOOOOOOOOOOOOOO  D           C "," C           D OOOOOOOOOOOOOOOOO D           C ","CC           D OOOOOOOOAOOOOOOOO D           CC","CCH          D OOOOOOOA AOOOOOOO D          HCC","CC           D OOOOOOOOAOOOOOOOO D           CC"," C           D OOOOOOOOOOOOOOOOO D           C "," C           D  OOOOOOOOOOOOOOO  D           C "," C            D OOOOOOOOOOOOOOO D            C ","  C           D  OOOOOOOOOOOOO  D           C  ","  C            D  OOOOOOOOOOO  D            C  ","  C             D  OOOOOOOOO  D             C  ","   C             D   OOOOO   D             C   ","   C              DD       DD              C   ","   C                DDDDDDD                C   ","    C                                     C    ","     C                                   C     ","     C                                   C     ","      C                                 C      ","      CCH                             HCC      ","       CC                             CC       ","        CC                           CC        ","          CC                       CC          ","            C                     C            ","             CCC               CCC             ","                CCC    H    CCC                ","                   CCCCCCCCC                   ","                      CCC                      "},
        {"                      CCC                      ","                   MMMMMMMMM                   ","                MMM    H    MMM                ","             MMM               MMM             ","            M                     M            ","          MM                       MM          ","        CM                           MC        ","       CM                             MC       ","      CMH                             HMC      ","      M                                 M      ","     M                                   M     ","     M                                   M     ","    M                                     M    ","   M                                       M   ","   M                                       M   ","   M                                       M   ","  M                                         M  ","  M                                         M  ","  M                                         M  "," M                                           M "," M                                           M "," M                                           M ","CM                     A                     MC","CMH                   A A                   HMC","CM                     A                     MC"," M                                           M "," M                                           M "," M                                           M ","  M                                         M  ","  M                                         M  ","  M                                         M  ","   M                                       M   ","   M                                       M   ","   M                                       M   ","    M                                     M    ","     M                                   M     ","     M                                   M     ","      M                                 M      ","      CMH                             HMC      ","       CM                             MC       ","        CM                           MC        ","          MM                       MM          ","            M                     M            ","             MMM               MMM             ","                MMM    H    MMM                ","                   MMMMMMMMM                   ","                      CCC                      "},
        {"                                               ","                      CCC                      ","                 MMMMMMMMMMMMM                 ","               MM      H      MM               ","             MM                 MM             ","           MM                     MM           ","          M                         M          ","        CM                           MC        ","       CM                             MC       ","       M H                           H M       ","      M                                 M      ","     M                                   M     ","     M                                   M     ","    M                                     M    ","    M                                     M    ","   M                                       M   ","   M                                       M   ","  M                                         M  ","  M                                         M  ","  M                                         M  ","  M                                         M  ","  M                                         M  "," CM                    A                    MC "," CMH                  A A                  HMC "," CM                    A                    MC ","  M                                         M  ","  M                                         M  ","  M                                         M  ","  M                                         M  ","  M                                         M  ","   M                                       M   ","   M                                       M   ","    M                                     M    ","    M                                     M    ","     M                                   M     ","     M                                   M     ","      M                                 M      ","       M H                           H M       ","       CM                             MC       ","        CM                           MC        ","          M                         M          ","           MM                     MM           ","             MM                 MM             ","               MM      H      MM               ","                 MMMMMMMMMMMMM                 ","                      CCC                      ","                                               "},
        {"                                               ","                      CCC                      ","                    MMMMMMM                    ","                MMMM   H   MMMM                ","              MM               MM              ","            MM                   MM            ","           M                       M           ","        CMM                         MMC        ","       CM                             MC       ","       M H                           H M       ","       M                               M       ","      M                                 M      ","     M                                   M     ","     M                                   M     ","    M                                     M    ","    M                                     M    ","   M                                       M   ","   M                                       M   ","   M                                       M   ","   M                                       M   ","  M                                         M  ","  M                                         M  "," CM                    A                    MC "," CMH                  A A                  HMC "," CM                    A                    MC ","  M                                         M  ","  M                                         M  ","   M                                       M   ","   M                                       M   ","   M                                       M   ","   M                                       M   ","    M                                     M    ","    M                                     M    ","     M                                   M     ","     M                                   M     ","      M                                 M      ","       M                               M       ","       M H                           H M       ","       CM                             MC       ","        CMM                         MMC        ","           M                       M           ","            MM                   MM            ","              MM               MM              ","                MMMM   H   MMMM                ","                    MMMMMMM                    ","                      CCC                      ","                                               "},
        {"                                               ","                                               ","                      CCC                      ","                  MMMMMMMMMMM                  ","               MMM     H     MMM               ","             MM                 MM             ","            M                     M            ","        C MM                       MM C        ","       CCM                           MCC       ","        MH                           HM        ","       M                               M       ","       M                               M       ","      M                                 M      ","     M                                   M     ","     M                                   M     ","    M                                     M    ","    M                                     M    ","    M                                     M    ","   M                                       M   ","   M                                       M   ","   M                                       M   ","   M                                       M   ","  CM                   A                   MC  ","  CMH                 A A                 HMC  ","  CM                   A                   MC  ","   M                                       M   ","   M                                       M   ","   M                                       M   ","   M                                       M   ","    M                                     M    ","    M                                     M    ","    M                                     M    ","     M                                   M     ","     M                                   M     ","      M                                 M      ","       M                               M       ","       M                               M       ","        MH                           HM        ","       CCM                           MCC       ","        C MM                       MM C        ","            M                     M            ","             MM                 MM             ","               MMM     H     MMM               ","                  MMMMMMMMMMM                  ","                      CCC                      ","                                               ","                                               "},
        {"                                               ","                                               ","                      CCC                      ","                     MMMMM                     ","                 MMMM  H  MMMM                 ","               MM             MM               ","             MM                 MM             ","           MM                     MM           ","        CCM                         MCC        ","        CM                           MC        ","        M H                         H M        ","       M                               M       ","       M                               M       ","      M                                 M      ","      M                                 M      ","     M                                   M     ","     M                                   M     ","    M                                     M    ","    M                                     M    ","    M                                     M    ","    M                                     M    ","   M                                       M   ","  CM                   A                   MC  ","  CMH                 A A                 HMC  ","  CM                   A                   MC  ","   M                                       M   ","    M                                     M    ","    M                                     M    ","    M                                     M    ","    M                                     M    ","     M                                   M     ","     M                                   M     ","      M                                 M      ","      M                                 M      ","       M                               M       ","       M                               M       ","        M H                         H M        ","        CM                           MC        ","        CCM                         MCC        ","           MM                     MM           ","             MM                 MM             ","               MM             MM               ","                 MMMM  H  MMMM                 ","                     MMMMM                     ","                      CCC                      ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                      CCC                      ","                    MMMMMMM                    ","                MMMM   H   MMMM                ","              MM               MM              ","             M                   M             ","         C MM                     MM C         ","        CCM                         MCC        ","         MH                         HM         ","        M                             M        ","        M                             M        ","       M                               M       ","      M                                 M      ","      M                                 M      ","     M                                   M     ","     M                                   M     ","     M                                   M     ","     M                                   M     ","    M                                     M    ","    M                                     M    ","   CM                 AAA                 MC   ","   CMH                A A                HMC   ","   CM                 AAA                 MC   ","    M                                     M    ","    M                                     M    ","     M                                   M     ","     M                                   M     ","     M                                   M     ","     M                                   M     ","      M                                 M      ","      M                                 M      ","       M                               M       ","        M                             M        ","        M                             M        ","         MH                         HM         ","        CCM                         MCC        ","         C MM                     MM C         ","             M                   M             ","              MM               MM              ","                MMMM   H   MMMM                ","                    MMMMMMM                    ","                      CCC                      ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                   MMMMMMMMM                   ","                MMM    H    MMM                ","              MM               MM              ","             M                   M             ","          CMM                     MMC          ","         CMM                       MMC         ","         MMH                       HMM         ","         M                           M         ","        M                             M        ","       M                               M       ","       M                               M       ","      M                                 M      ","      M                                 M      ","      M                                 M      ","     M                                   M     ","     M                D D                M     ","     M               DAAAD               M     ","    CM              DAA AAD              MC    ","    CMH              A   A              HMC    ","    CM              DAA AAD              MC    ","     M               DAAAD               M     ","     M                D D                M     ","     M                                   M     ","      M                                 M      ","      M                                 M      ","      M                                 M      ","       M                               M       ","       M                               M       ","        M                             M        ","         M                           M         ","         MMH                       HMM         ","         CMM                       MMC         ","          CMM                     MMC          ","             M                   M             ","              MM               MM              ","                MMM    H    MMM                ","                   MMMMMMMMM                   ","                      CCC                      ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                   MMMMMMMMM                   ","                MMM    H    MMM                ","              MM               MM              ","             MM                 MM             ","          CCM                     MCC          ","          CM                       MC          ","          M H                     H M          ","         M                           M         ","        MM                           MM        ","        M                             M        ","       M                               M       ","       M                               M       ","       M                               M       ","      M               D D               M      ","      M             DAAAAAD             M      ","      M             AA   AA             M      ","     CM            DA     AD            MC     ","     CMH            A     A            HMC     ","     CM            DA     AD            MC     ","      M             AA   AA             M      ","      M             DAAAAAD             M      ","      M               D D               M      ","       M                               M       ","       M                               M       ","       M                               M       ","        M                             M        ","        MM                           MM        ","         M                           M         ","          M H                     H M          ","          CM                       MC          ","          CCM                     MCC          ","             MM                 MM             ","              MM               MM              ","                MMM    H    MMM                ","                   MMMMMMMMM                   ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                   MMMMMMMMM                   ","                MMMM   H   MMMM                ","               MM             MM               ","           C MM                 MM C           ","          CCMM                   MMCC          ","           MM                     MM           ","          MM H                   H MM          ","          M                         M          ","         M                           M         ","        MM                           MM        ","        M                             M        ","        M             D D             M        ","       MM           DAAAAAD           MM       ","       M           DA     AD           M       ","       M           A       A           M       ","      CM          DA       AD          MC      ","      CMH          A       A          HMC      ","      CM          DA       AD          MC      ","       M           A       A           M       ","       M           DA     AD           M       ","       MM           DAAAAAD           MM       ","        M             D D             M        ","        M                             M        ","        MM                           MM        ","         M                           M         ","          M                         M          ","          MM H                   H MM          ","           MM                     MM           ","          CCMM                   MMCC          ","           C MM                 MM C           ","               MM             MM               ","                MMMM   H   MMMM                ","                   MMMMMMMMM                   ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                    MMMMMMM                    ","                 MMMM  H  MMMM                 ","               MMM     H     MMM               ","            C MM               MM C            ","           CCMM                 MMCC           ","            MH                   HM            ","           MM H                 H MM           ","          MM                       MM          ","          M                         M          ","         MM           D D           MM         ","         M          D AAA D          M         ","         M          AA   AA          M         ","        MM        DA       AD        MM        ","        M          A       A          M        ","       CM        DA         AD        MC       ","       CMHH       A         A       HHMC       ","       CM        DA         AD        MC       ","        M          A       A          M        ","        MM        DA       AD        MM        ","         M          AA   AA          M         ","         M          D AAA D          M         ","         MM           D D           MM         ","          M                         M          ","          MM                       MM          ","           MM H                 H MM           ","            MH                   HM            ","           CCMM                 MMCC           ","            C MM               MM C            ","               MMM     H     MMM               ","                 MMMM  H  MMMM                 ","                    MMMMMMM                    ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                     MMMMM                     ","                  MMMMMMMMMMM                  ","                MMMM   H   MMMM                ","             C MM      H      MM C             ","            CMMM               MMMC            ","             MM                 MM             ","            MM H               H MM            ","           MM                     MM           ","           M          D D          M           ","          MM        DAAAAAD        MM          ","          MM        A     A        MM          ","          M       DA       AD       M          ","         MM       A         A       MM         ","        CMM      DA         AD      MMC        ","        CMMHH     A         A     HHMMC        ","        CMM      DA         AD      MMC        ","         MM       A         A       MM         ","          M       DA       AD       M          ","          MM        A     A        MM          ","          MM        DAAAAAD        MM          ","           M          D D          M           ","           MM                     MM           ","            MM H               H MM            ","             MM                 MM             ","            CMMM               MMMC            ","             C MM      H      MM C             ","                MMMM   H   MMMM                ","                  MMMMMMMMMMM                  ","                     MMMMM                     ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                      CCC                      ","                    MMMMMMM                    ","                 MMMMMMMMMMMMM                 ","             CC MMM    H    MMM CC             ","             CCMMM     H     MMMCC             ","              MM               MM              ","             MM H             H MM             ","            MMM  H    D D    H  MMM            ","            MM      DAAAAAD      MM            ","            M       A     A       M            ","           MM     DA       AD     MM           ","           MM     A         A     MM           ","         CCMM    DA         AD    MMCC         ","         CCMMHH   A         A   HHMMCC         ","         CCMM    DA         AD    MMCC         ","           MM     A         A     MM           ","           MM     DA       AD     MM           ","            M       A     A       M            ","            MM      DAAAAAD      MM            ","            MMM  H    D D    H  MMM            ","             MM H             H MM             ","              MM               MM              ","             CCMMM     H     MMMCC             ","             CC MMM    H    MMM CC             ","                 MMMMMMMMMMMMM                 ","                    MMMMMMM                    ","                      CCC                      ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                      CCC                      ","                   MMMMMMMMM                   ","              CC  MMMMMMMMMMM  CC              ","              CCMMMMM  H  MMMMMCC              ","               MMMM    H    MMMM               ","               MMM    DHD    MMM               ","              MMM H DAAAAAD H MMM              ","             MMM   HA     AH   MMM             ","             MMM  DA       AD  MMM             ","             MM   A         A   MM             ","           CCMM  DA         AD  MMCC           ","           CCMMHHHA         AHHHMMCC           ","           CCMM  DA         AD  MMCC           ","             MM   A         A   MM             ","             MMM  DA       AD  MMM             ","             MMM   HA     AH   MMM             ","              MMM H DAAAAAD H MMM              ","               MMM    DHD    MMM               ","               MMMM    H    MMMM               ","              CCMMMMM  H  MMMMMCC              ","              CC  MMMMMMMMMMM  CC              ","                   MMMMMMMMM                   ","                      CCC                      ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      EEE                      ","                      EEE                      ","                      EEE                      ","                C    MMMMM    C                ","               CCC MMMMMMMMM CCC               ","                CCMMMMMMMMMMMCC                ","                 MMMMMMMMMMMMM                 ","                MMMMM     MMMMM                ","                MMMM       MMMM                ","               MMMM         MMMM               ","             EEMMMM         MMMMEE             ","             EEMMMM         MMMMEE             ","             EEMMMM         MMMMEE             ","               MMMM         MMMM               ","                MMMM       MMMM                ","                MMMMM     MMMMM                ","                 MMMMMMMMMMMMM                 ","                CCMMMMMMMMMMMCC                ","               CCC MMMMMMMMM CCC               ","                C    MMMMM    C                ","                      EEE                      ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      E E                      ","                      EEE                      ","                      EEE                      ","                      EEE                      ","                    EECCCEE                    ","                  EE  CCC  EE                  ","                 ECC  CCC  CCE                 ","                 ECCCMMMMMCCCE                 ","                E  CMMMMMMMC  E                ","                E  MMMMMMMMM  E                ","             EEECCCMMMMMMMMMCCCEEE             ","             EEECCCMMMMMMMMMCCCEEE             ","             EEECCCMMMMMMMMMCCCEEE             ","                E  MMMMMMMMM  E                ","                E  CMMMMMMMC  E                ","                 ECCCMMMMMCCCE                 ","                 ECC  CCC  CCE                 ","                  EE  CCC  EE                  ","                    EECCCEE                    ","                      EEE                      ","                      EEE                      ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      E E                      ","                      EEE                      ","                      EEE                      ","                      OOO                      ","                    OO   OO                    ","                  OO       OO                  ","                 O    FFF    O                 ","                 O  FFCCCFF  O                 ","                O  FCCJJJCCF  O                ","                O  FCJJJJJCF  O                ","             EEO  FCJJJJJJJCF  OEE             ","             EEO  FCJJJJJJJCF  OEE             ","             EEO  FCJJJJJJJCF  OEE             ","                O  FCJJJJJCF  O                ","                O  FCCJJJCCF  O                ","                 O  FFCCCFF  O                 ","                 O    FFF    O                 ","                  OO       OO                  ","                    OO   OO                    ","                      OOO                      ","                      EEE                      ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      E E                      ","                      EEE                      ","                      EEE                      ","                      OOO                      ","                    OO   OO                    ","                  OO       OO                  ","                 O    LLL    O                 ","                 O  LLCCCLL  O                 ","                O  LCCJJJCCL  O                ","                O  LCJJJJJCL  O                ","             EEO  LCJJJJJJJCL  OEE             ","             EEO  LCJJJJJJJCL  OEE             ","             EEO  LCJJJJJJJCL  OEE             ","                O  LCJJJJJCL  O                ","                O  LCCJJJCCL  O                ","                 O  LLCCCLL  O                 ","                 O    LLL    O                 ","                  OO       OO                  ","                    OO   OO                    ","                      OOO                      ","                      EEE                      ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      E E                      ","                      EEE                      ","                      EEE                      ","                      OOO                      ","                    OO   OO                    ","                  OO       OO                  ","                 O    GGG    O                 ","                 O  GGCCCGG  O                 ","                O  GCCJJJCCG  O                ","                O  GCJJJJJCG  O                ","             EEO  GCJJJJJJJCG  OEE             ","             EEO  GCJJJJJJJCG  OEE             ","             EEO  GCJJJJJJJCG  OEE             ","                O  GCJJJJJCG  O                ","                O  GCCJJJCCG  O                ","                 O  GGCCCGG  O                 ","                 O    GGG    O                 ","                  OO       OO                  ","                    OO   OO                    ","                      OOO                      ","                      EEE                      ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      PPP                      ","                      EEE                      ","                      EEE                      ","                      OOO                      ","                    OO   OO                    ","                  OO       OO                  ","                 O    LLL    O                 ","                 O  LLCCCLL  O                 ","                O  LCCJJJCCL  O                ","                O  LCJJJJJCL  O                ","             EEO  LCJJJJJJJCL  OEE             ","             EEO  LCJJJJJJJCL  OEE             ","             EEO  LCJJJJJJJCL  OEE             ","                O  LCJJJJJCL  O                ","                O  LCCJJJCCL  O                ","                 O  LLCCCLL  O                 ","                 O    LLL    O                 ","                  OO       OO                  ","                    OO   OO                    ","                      OOO                      ","                      EEE                      ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      P~P                      ","                      EEE                      ","                      EEE                      ","                      EEE                      ","                    EE   EE                    ","                  EE       EE                  ","                 E    GGG    E                 ","                 E  GGCCCGG  E                 ","                E  GCCJJJCCG  E                ","                E  GCJJJJJCG  E                ","             EEE  GCJJJJJJJCG  EEE             ","             EEE  GCJJJJJJJCG  EEE             ","             EEE  GCJJJJJJJCG  EEE             ","                E  GCJJJJJCG  E                ","                E  GCCJJJCCG  E                ","                 E  GGCCCGG  E                 ","                 E    GGG    E                 ","                  EE       EE                  ","                    EE   EE                    ","                      EEE                      ","                      EEE                      ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      PPP                      ","                      EEE                      ","                    GGEEEGG                    ","                  GGFFFFFFFGG                  ","                 GFFFFFFFFFFFG                 ","                GFFFFFFFFFFFFFG                ","               GFFFFFFFFFFFFFFFG               ","               GFFFFFFFFFFFFFFFG               ","              GFFFFFFFFFFFFFFFFFG              ","              GFFFFFFFFFFFFFFFFFG              ","             EEFFFFFFFFFFFFFFFFFEE             ","             EEFFFFFFFFFFFFFFFFFEE             ","             EEFFFFFFFFFFFFFFFFFEE             ","              GFFFFFFFFFFFFFFFFFG              ","              GFFFFFFFFFFFFFFFFFG              ","               GFFFFFFFFFFFFFFFG               ","               GFFFFFFFFFFFFFFFG               ","                GFFFFFFFFFFFFFG                ","                 GFFFFFFFFFFFG                 ","                  GGFFFFFFFGG                  ","                    GGEEEGG                    ","                      EEE                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      NNN                      ","                      NNN                      ","                    FFFFFFF                    ","                  FFFFFFFFFFF                  ","                 FFFFFFFFFFFFF                 ","                FFFFFFFFFFFFFFF                ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","            NNFFFFFFFFFFFFFFFFFFFNN            ","            NNFFFFFFFFFFFFFFFFFFFNN            ","            NNFFFFFFFFFFFFFFFFFFFNN            ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","                FFFFFFFFFFFFFFF                ","                 FFFFFFFFFFFFF                 ","                  FFFFFFFFFFF                  ","                    FFFFFFF                    ","                      NNN                      ","                      NNN                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                    FFFFFFF                    ","                  FFFFFFFFFFF                  ","                 FFFFFFFFFFFFF                 ","                FFFFFFFFFFFFFFF                ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","          NNNNFFFFFFFFFFFFFFFFFFFNNNN          ","          NNNNFFFFFFFFFFFFFFFFFFFNNNN          ","          NNNNFFFFFFFFFFFFFFFFFFFNNNN          ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","                FFFFFFFFFFFFFFF                ","                 FFFFFFFFFFFFF                 ","                  FFFFFFFFFFF                  ","                    FFFFFFF                    ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                    FFFFFFF                    ","                  FFFFFFFFFFF                  ","                 FFFFFFFFFFFFF                 ","                FFFFFFFFFFFFFFF                ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","        NNNNNNFFFFFFFFFFFFFFFFFFFNNNNNN        ","        NNNNNNFFFFFFFFFFFFFFFFFFFNNNNNN        ","        NNNNNNFFFFFFFFFFFFFFFFFFFNNNNNN        ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","                FFFFFFFFFFFFFFF                ","                 FFFFFFFFFFFFF                 ","                  FFFFFFFFFFF                  ","                    FFFFFFF                    ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                    FFFFFFF                    ","                  FFFFFFFFFFF                  ","                 FFFFFFFFFFFFF                 ","                FFFFFFFFFFFFFFF                ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","      NNNNNNNNFFFFFFFFFFFFFFFFFFFNNNNNNNN      ","      NNNNNNNNFFFFFFFFFFFFFFFFFFFNNNNNNNN      ","      NNNNNNNNFFFFFFFFFFFFFFFFFFFNNNNNNNN      ","              FFFFFFFFFFFFFFFFFFF              ","              FFFFFFFFFFFFFFFFFFF              ","               FFFFFFFFFFFFFFFFF               ","               FFFFFFFFFFFFFFFFF               ","                FFFFFFFFFFFFFFF                ","                 FFFFFFFFFFFFF                 ","                  FFFFFFFFFFF                  ","                    FFFFFFF                    ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                    NNNNNNN                    ","                 NNNNNNNNNNNNN                 ","                NNNNNNNNNNNNNNN                ","              NNNNNNNNNNNNNNNNNNN              ","             NNNNNNNNNNNNNNNNNNNNN             ","             NNNNNNNNNNNNNNNNNNNNN             ","            NNNNNNNNNNNNNNNNNNNNNNN            ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","            NNNNNNNNNNNNNNNNNNNNNNN            ","             NNNNNNNNNNNNNNNNNNNNN             ","             NNNNNNNNNNNNNNNNNNNNN             ","              NNNNNNNNNNNNNNNNNNN              ","                NNNNNNNNNNNNNNN                ","                 NNNNNNNNNNNNN                 ","                    NNNNNNN                    ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                    NNNNNNN                    ","                 NNNNNNNNNNNNN                 ","                NNNNNNNNNNNNNNN                ","              NNNNNNNNNNNNNNNNNNN              ","             NNNNNNNNNNNNNNNNNNNNN             ","             NNNNNNNNNNNNNNNNNNNNN             ","            NNNNNNNNNNNNNNNNNNNNNNN            ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","            NNNNNNNNNNNNNNNNNNNNNNN            ","             NNNNNNNNNNNNNNNNNNNNN             ","             NNNNNNNNNNNNNNNNNNNNN             ","              NNNNNNNNNNNNNNNNNNN              ","                NNNNNNNNNNNNNNN                ","                 NNNNNNNNNNNNN                 ","                    NNNNNNN                    ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                    NNNNNNN                    ","                 NNNNNNNNNNNNN                 ","                NNNNNNNNNNNNNNN                ","              NNNNNNNNNNNNNNNNNNN              ","             NNNNNNNNNNNNNNNNNNNNN             ","             NNNNNNNNNNNNNNNNNNNNN             ","            NNNNNNNNNNNNNNNNNNNNNNN            ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","      NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN      ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","          NNNNNNNNNNNNNNNNNNNNNNNNNNN          ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","           NNNNNNNNNNNNNNNNNNNNNNNNN           ","            NNNNNNNNNNNNNNNNNNNNNNN            ","             NNNNNNNNNNNNNNNNNNNNN             ","             NNNNNNNNNNNNNNNNNNNNN             ","              NNNNNNNNNNNNNNNNNNN              ","                NNNNNNNNNNNNNNN                ","                 NNNNNNNNNNNNN                 ","                    NNNNNNN                    ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                      NNN                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "}
    };
    // spotless:on

    // 47,54,47
    @Override
    public IStructureDefinition<TST_DSPReceiver> getStructureDefinition() {
        return IStructureDefinition.<TST_DSPReceiver>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 14)) // A -> ofBlock...(gt.blockcasings, 14, ...);
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 8)) // B -> ofBlock...(gt.blockcasings2, 8, ...);
            .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 2)) // C -> ofBlock...(gt.blockcasings8, 2, ...);
            .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 10)) // D -> ofBlock...(gt.blockcasings8, 10, ...);
            .addElement('E', ofBlock(GregTechAPI.sBlockCasingsSE, 0)) // E -> ofBlock...(gt.blockcasingsSE, 0, ...);
            .addElement('F', ofBlock(GregTechAPI.sBlockCasingsSE, 1)) // F -> ofBlock...(gt.blockcasingsSE, 1, ...);
            .addElement('G', ofBlock(GregTechAPI.sBlockCasingsSE, 2)) // G -> ofBlock...(gt.blockcasingsSE, 2, ...);
            .addElement('H', ofBlock(sBlockCasingsTT, 0)) // H -> ofBlock...(gt.blockcasingsTT, 0, ...);
            .addElement('I', ofBlock(sBlockCasingsTT, 6)) // I -> ofBlock...(gt.blockcasingsTT, 6, ...);
            .addElement('J', ofBlock(sBlockCasingsTT, 7)) // J -> ofBlock...(gt.blockcasingsTT, 7, ...);
            .addElement('K', ofBlock(sBlockCasingsTT, 9)) // K -> ofBlock...(gt.blockcasingsTT, 9, ...);
            .addElement('L', ofBlock(ModBlocks.blockCasings4Misc, 4)) // L -> ofBlock...(gtplusplus.blockcasings.4, 4,
                                                                      // ...);
            .addElement('M', ofBlock(GregTechAPI.sBlockCasingsDyson, 1)) // M -> ofBlock...(tile.DysonSwarmPart, 1,
                                                                         // ...);
            .addElement('N', ofBlock(GregTechAPI.sBlockCasingsDyson, 9)) // N -> ofBlock...(tile.DysonSwarmPart, 9,
                                                                         // ...);
            .addElement('O', ofBlock(BlockQuantumGlass.INSTANCE, 0)) // O -> ofBlock...(tile.quantumGlass, 0, ...);
            .addElement(
                'P',
                HatchElementBuilder.<TST_DSPReceiver>builder()
                    .atLeast(InputBus, OutputBus)
                    .adder(TST_DSPReceiver::addToMachineList)
                    .casingIndex(SPACE_ELEVATOR_BASE_CASING_INDEX)
                    .hint(1)
                    .buildAndChain(GregTechAPI.sBlockCasingsSE, 0))
            .addElement('Q', ofFrame(Materials.NaquadahAlloy))
            .addElement('R', ofChain(ofBlock(sBlockCasingsTT, 0), ofBlock(GregTechAPI.sBlockCasings8, 10)))
            .build();
    }

    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasings, 13, ...);
     * B -> ofBlock...(gt.blockcasings2, 8, ...);
     * C -> ofBlock...(gt.blockcasings8, 2, ...);
     * D -> ofBlock...(gt.blockcasings8, 10, ...);
     * E -> ofBlock...(gt.blockcasingsSE, 0, ...);
     * F -> ofBlock...(gt.blockcasingsSE, 1, ...);
     * G -> ofBlock...(gt.blockcasingsSE, 2, ...);
     * H -> ofBlock...(gt.blockcasingsTT, 0, ...);
     * I -> ofBlock...(gt.blockcasingsTT, 6, ...);
     * J -> ofBlock...(gt.blockcasingsTT, 7, ...);
     * K -> ofBlock...(gt.blockcasingsTT, 9, ...);
     * L -> ofBlock...(gtplusplus.blockcasings.4, 4, ...);
     * M -> ofBlock...(tile.DysonSwarmPart, 1, ...);
     * N -> ofBlock...(tile.DysonSwarmPart, 9, ...);
     * O -> ofBlock...(tile.quantumGlass, 0, ...);
     * P -> ofBlock...(gt.blockcasingsSE, 0, ...); // Hatches
     * Q -> ofFrame...(NaquadahAlloy, ...);
     */

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
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) return;
        // wirelessMode = this.mEnergyHatches.isEmpty() && this.mExoticEnergyHatches.isEmpty();
        wirelessMode = true;
    }
    // endregion

    // region Processing Logic
    protected static double CRITICAL_PHOTON_MULTIPLE;
    protected static ItemStack ASTRAL_ARRAY_FABRICATOR;
    protected static ItemStack CRITICAL_PHOTON;
    protected String ownerName;

    // init when load world
    protected UUID ownerUUID;

    // init when load world
    protected long usedPowerPoint = 0;

    protected boolean isUsing = false;
    protected long storageEU = 0;
    protected long storageEUMAX = 0;
    protected long gravitationalLensTime = 0;
    protected boolean wirelessMode = true;
    protected int astralArrayOverloadMultiplier = 1;
    protected int dimID;

    // init when load world
    protected double stellarAndPlanetCoefficient = 0;

    protected DSP_DataCell dspDataCell;

    // init when load world
    protected byte dataSyncFlag = 0;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER };

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.DSP_ReceiverRecipes;
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Power Generation
         * 1 - Photon Collection
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // #tr TST_DSPReceiver.modeMsg.0
        // # Mode: Power Generation
        // #zh_CN 发电模式

        // #tr TST_DSPReceiver.modeMsg.1
        // # Mode: Photon Collection
        // #zh_CN 光子浓缩模式
        return StatCollector.translateToLocal("TST_DSPReceiver.modeMsg." + machineMode);
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

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        checkGravitationalLensInput();
        this.syncDSPData();

        if (machineMode == 0) {
            if (wirelessMode) {
                // Generate EU directly
                if (this.storageEU > 0 || storageEUMAX > 0) {
                    BigInteger eu = BigInteger.valueOf(storageEUMAX)
                        .multiply(TstUtils.INTEGER_MAX_VALUE)
                        .add(BigInteger.valueOf(storageEU));
                    addEUToGlobalEnergyMap(ownerUUID, eu);
                    this.storageEU = 0;
                    this.storageEUMAX = 0;
                }
            }
        } else if (machineMode == 1) {
            // Generate Photon per int.MAX EU
            if (storageEUMAX > 0 || storageEU >= EUPerCriticalPhoton) {
                long amount = storageEU / EUPerCriticalPhoton;
                storageEU -= EUPerCriticalPhoton * amount;
                double cfgMultiple = amount += (long) ((double) storageEUMAX
                    * (Integer.MAX_VALUE / EUPerCriticalPhoton));
                storageEUMAX = 0;
                if (amount > Integer.MAX_VALUE) {
                    List<ItemStack> output = new ArrayList<>();
                    while (amount > Integer.MAX_VALUE) {
                        output.add(GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, CRITICAL_PHOTON));
                        amount -= Integer.MAX_VALUE;
                    }
                    if (amount > 0) {
                        output.add(GTUtility.copyAmountUnsafe((int) amount, CRITICAL_PHOTON));
                    }
                    mOutputItems = output.toArray(new ItemStack[0]);
                } else {
                    mOutputItems = new ItemStack[] { GTUtility.copyAmountUnsafe((int) amount, CRITICAL_PHOTON) };
                }

            }
        }
        mMaxProgresstime = 128;
        return CheckRecipeResultRegistry.GENERATING;
    }

    public static void initStatics() {
        if (ASTRAL_ARRAY_FABRICATOR == null) {
            CRITICAL_PHOTON_MULTIPLE = (double) Integer.MAX_VALUE / Config.EUPerCriticalPhoton;
            ASTRAL_ARRAY_FABRICATOR = astralArrayFabricator.get(1);
            CRITICAL_PHOTON = CriticalPhoton.get(1);
        }
    }

    protected void decreaseGravitationalLensTime() {
        if (gravitationalLensTime > 0) gravitationalLensTime--;
    }

    @Override
    public String[] getInfoData() {
        List<String> ret = new ArrayList<>(Arrays.asList(super.getInfoData()));
        String space = "    ";
        ret.add(EnumChatFormatting.AQUA + "Owner Name: " + EnumChatFormatting.RESET + ownerName);
        ret.add(EnumChatFormatting.AQUA + "UUID: " + EnumChatFormatting.RESET + ownerUUID);
        ret.add(
            EnumChatFormatting.AQUA
                // spotless:off
            // #tr TST_DSPReceiver.getInfoData.01
            // # Generating EU/t:
            // #zh_CN 实际接收 EU/t:
            + TextEnums.tr("TST_DSPReceiver.getInfoData.01")
            + EnumChatFormatting.RESET + " "
            + generateTickEU());
        ret.add(EnumChatFormatting.AQUA
            // #tr TST_DSPReceiver.getInfoData.02
            // # Used Power Point:
            // #zh_CN 已占用产能点数:
            + TextEnums.tr("TST_DSPReceiver.getInfoData.02")
            + EnumChatFormatting.RESET + " "
            + usedPowerPoint);
        ret.add(EnumChatFormatting.AQUA
            // #tr TST_DSPReceiver.getInfoData.03
            // # Gravitational Lens Intensify Mode remaining time:
            // #zh_CN 引力透镜增强模式剩余时间:
            + TextEnums.tr("TST_DSPReceiver.getInfoData.03")
            + EnumChatFormatting.RESET + " "
            + (gravitationalLensTime/20) + " s");
        ret.add(EnumChatFormatting.AQUA
            // #tr TST_DSPReceiver.getInfoData.04
            // # Overload Multiplier:
            // #zh_CN 过载倍率:
            + TextEnums.tr("TST_DSPReceiver.getInfoData.04")
            + EnumChatFormatting.RESET + " "
            + astralArrayOverloadMultiplier);
        // DSPDataCell
        // Language file writer is set in TST_DSPLauncher
        ret.add(EnumChatFormatting.AQUA
            + tr("DSPDataCell.getInfoData")
            + EnumChatFormatting.RESET);
        ret.add(space
            + EnumChatFormatting.GOLD
            + tr("DSPDataCell.getDSPOwnerName")
            + EnumChatFormatting.RESET + " "
            + dspDataCell.getOwnerName());
        ret.add(space
            + EnumChatFormatting.GOLD
            + tr("DSPDataCell.getDSPGalaxy")
            + EnumChatFormatting.RESET + " "
            + dspDataCell.getGalaxy());
        ret.add(space
            + EnumChatFormatting.GOLD
            + tr("DSPDataCell.getDSPSolarSail")
            + EnumChatFormatting.RESET + " "
            + dspDataCell.getDSPSolarSail());
        ret.add(space
            + EnumChatFormatting.GOLD
            + tr("DSPDataCell.getDSPNode")
            + EnumChatFormatting.RESET + " "
            + dspDataCell.getDSPNode());
        ret.add(space
            + EnumChatFormatting.GOLD
            // #tr infoText_CurrentStellarCoefficient
            // # Current Stellar Coefficient:
            // #zh_CN 当前恒星系数:
            + TextEnums.tr("infoText_CurrentStellarCoefficient")
            + EnumChatFormatting.RESET
            + dspDataCell.getGalaxy()
            + " -> "
            + EnumChatFormatting.YELLOW
            + dspDataCell.getGalaxy().stellarCoefficient);
        ret.add(space
            + EnumChatFormatting.GOLD
            // #tr infoText_CurrentPlanetCoefficient
            // # Current Planet Coefficient:
            // #zh_CN 当前行星系数:
            + TextEnums.tr("infoText_CurrentPlanetCoefficient")
            + EnumChatFormatting.RESET
            + DSP_Planet.getPlanetFromDimID(dimID)
            + " -> "
            + EnumChatFormatting.YELLOW
            + DSP_Planet.getPlanetaryCoefficientWithDimID(dimID));
            // spotless:on
        return ret.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("isActive")) {
            currentTip.add(
                EnumChatFormatting.AQUA
                    // #tr Waila.TST_DSPReceiver.1
                    // # Energy Receiving:
                    // #zh_CN 接收能源:
                    + TextEnums.tr("Waila.TST_DSPReceiver.1")
                    + EnumChatFormatting.GOLD
                    + formatNumber(tag.getLong("TickEU"))
                    + EnumChatFormatting.RESET
                    + " EU/t");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            if (tileEntity.isActive()) {
                tag.setLong("TickEU", generateTickEU());
            }
        }
    }

    /**
     * Get how many DSP power points this machine use.
     * Limited by putting integrated circuit in controller block slot.
     *
     * @return The amount, MAX 1024 * Integer.MAX_VALUE.
     */
    protected long getPowerPoint() {
        if (dspDataCell == null) return 0;
        // Limit dsp max * circuit.meta / circuit.stackSize
        ItemStack controllerStack = getControllerSlot();
        long maxPowerPointLimit = dspDataCell.getMaxDSPPowerPoint();
        long canUse = dspDataCell.getDSPPowerPointCanUse();

        if (controllerStack == null) {
            // normal
            astralArrayOverloadMultiplier = 1;
            return Math.min(DSP_Values.maxPowerPointPerReceiver, canUse);
        }

        if (controllerStack.getItem() instanceof ItemIntegratedCircuit) {
            // use integrated circuit to limit
            double multiplier = Math
                .min(1, ((double) controllerStack.getItemDamage()) / ((double) controllerStack.stackSize));
            long limited = (long) (multiplier * maxPowerPointLimit);
            astralArrayOverloadMultiplier = 1;
            return Math.min(DSP_Values.maxPowerPointPerReceiver, Math.min(limited, canUse));
        }

        if (GTUtility.areStacksEqual(controllerStack, ASTRAL_ARRAY_FABRICATOR) && controllerStack.stackSize >= 1) {
            // use Astral Array Fabricator to overload over max input limitation
            astralArrayOverloadMultiplier = calculateAstralArrayOverloadMultiplier(controllerStack.stackSize);
            return Math.min(DSP_Values.maxPowerPointPerReceiver * astralArrayOverloadMultiplier, canUse);
        }

        // other invalid item, ignore
        astralArrayOverloadMultiplier = 1;
        return Math.min(DSP_Values.maxPowerPointPerReceiver, canUse);
    }

    protected static int calculateAstralArrayOverloadMultiplier(int astralArrayAmount) {
        if (astralArrayAmount < 1) return 1;
        return astralArrayAmount * astralArrayAmount * 2;
    }

    /**
     * Request resources and handle exceptions.
     */
    protected void startUsingDSP() {
        isUsing = true;
        dataSyncFlag = dspDataCell.getDataSyncFlag();
        usedPowerPoint = getPowerPoint();
        if (!dspDataCell.tryUsePowerPoint(usedPowerPoint)) {
            usedPowerPoint = 0;
            this.stopMachine();
            TwistSpaceTechnology.LOG.info("Error ! DSPReceiver try use DSP Power Point FAILED at " + this);
            TwistSpaceTechnology.LOG.info("Check your Dyson Sphere Program Information!");
        }
    }

    /**
     * Release resources and handle exceptions.
     */
    protected void stopUsingDSP() {
        isUsing = false;
        dataSyncFlag = 0;
        if (!dspDataCell.tryDecreaseUsedPowerPoint(usedPowerPoint)) {
            // If Solar Sail amount had been decreased, conflict value will be resolved at here.
            dspDataCell.setUsedPowerPointUnsafely(0);
        }
        usedPowerPoint = 0;
        astralArrayOverloadMultiplier = 1;
    }

    @Override
    public void onBlockDestroyed() {
        stopUsingDSP();
        super.onBlockDestroyed();
    }

    protected void syncDSPData() {
        if (this.dataSyncFlag != dspDataCell.getDataSyncFlag()) {
            this.stopUsingDSP();
            this.startUsingDSP();
            return;
        }

        final ItemStack controllerSlot = getControllerSlot();
        if (astralArrayOverloadMultiplier > 1) {
            if (controllerSlot == null || !GTUtility.areStacksEqual(controllerSlot, ASTRAL_ARRAY_FABRICATOR)
                || calculateAstralArrayOverloadMultiplier(controllerSlot.stackSize) != astralArrayOverloadMultiplier) {
                this.stopUsingDSP();
                this.startUsingDSP();
            }
        } else if (controllerSlot != null && GTUtility.areStacksEqual(controllerSlot, ASTRAL_ARRAY_FABRICATOR)) {
            this.stopUsingDSP();
            this.startUsingDSP();
        }

    }

    protected double getGLensSpeedMultiplier() {
        return gravitationalLensTime > 0 ? DSP_Values.gravitationalLensSpeedMultiplier : 1;
    }

    protected long generateTickEU() {
        return (long) (stellarAndPlanetCoefficient * getGLensSpeedMultiplier() * this.usedPowerPoint);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (machineMode == 1 || wirelessMode) {
            long gen = this.generateTickEU();
            storageEUMAX += gen / Integer.MAX_VALUE;
            storageEU += gen % Integer.MAX_VALUE;
        } else {
            addEnergyOutput(this.generateTickEU());
        }
        return true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        if (aBaseMetaTileEntity.isServerSide()) {
            this.dimID = getDimID(aBaseMetaTileEntity);
            this.ownerName = getOwnerNameAndInitMachine(aBaseMetaTileEntity);
            this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
            this.dspDataCell = getOrInitDSPData(ownerName, dimID);
            this.stellarAndPlanetCoefficient = DSP_Planet.getPlanetaryCoefficientWithDimID(dimID)
                * DSP_Galaxy.getGalaxyFromDimID(dimID).stellarCoefficient;
        }
    }

    protected void checkGravitationalLensInput() {
        ArrayList<ItemStack> storedInputs = getStoredInputs();
        if (storedInputs.isEmpty()) return;
        for (ItemStack items : storedInputs) {
            if (GTUtility.areStacksEqual(items, GravitationalLens.get(1))) {
                gravitationalLensTime += 20L * items.stackSize
                    * DSP_Values.secondsOfEveryGravitationalLensProvideToIntensifyTime;
                items.stackSize = 0;
            }
        }
        updateSlots();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // Synchronize the DSP source when run machine
            if (!isUsing && mProgresstime == 1) {
                startUsingDSP();
            }

            // Release resource when stop machine
            if (isUsing && mMaxProgresstime == 0) {
                stopUsingDSP();
            }

            decreaseGravitationalLensTime();

        }
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setLong("usedPowerPoint", usedPowerPoint);
        aNBT.setLong("storageEU", storageEU);
        aNBT.setLong("storageEUMAX", storageEUMAX);
        aNBT.setBoolean("isUsing", isUsing);
        aNBT.setLong("gravitationalLensTime", gravitationalLensTime);
        aNBT.setBoolean("wirelessMode", wirelessMode);
        aNBT.setDouble("stellarAndPlanetCoefficient", stellarAndPlanetCoefficient);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getByte("mode");
        usedPowerPoint = aNBT.getLong("usedPowerPoint");
        storageEU = aNBT.getLong("storageEU");
        storageEUMAX = aNBT.getLong("storageEUMAX");
        isUsing = aNBT.getBoolean("isUsing");
        gravitationalLensTime = aNBT.getLong("gravitationalLensTime");
        wirelessMode = aNBT.getBoolean("wirelessMode");
        stellarAndPlanetCoefficient = aNBT.getDouble("stellarAndPlanetCoefficient");
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {

            if (aActive) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(SPACE_ELEVATOR_BASE_CASING_INDEX),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_DTPF_ON)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(SPACE_ELEVATOR_BASE_CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_OFF)
                    .extFacing()
                    .build() };
        }

        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(SPACE_ELEVATOR_BASE_CASING_INDEX) };
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_DSPReceiver_MachineType
        // # Dyson Sphere Program: Ray Receiving Station
        // #zh_CN 戴森球计划: 射线接收站
        tt.addMachineType(TextEnums.tr("Tooltip_DSPReceiver_MachineType"))
            // #tr Tooltip_DSPReceiver_00
            // # Controller block for the Dyson Sphere Ray Receiving Station.
            // #zh_CN 戴森球射线接收站的控制器方块.
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_00"))
            // #tr Tooltip_DSPReceiver_01
            // # {\DARK_PURPLE}{\BOLD}You hold in your hands the true power of Master Nebula ...
            // #zh_CN {\DARK_PURPLE}{\BOLD}你的手中掌握着星云法师真正的力量 ...
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_01"))
            // #tr Tooltip_DSPReceiver_02
            // # Receive high-energy rays transmitted back from the Dyson Cloud or the Dyson Sphere.
            // #zh_CN 接收从戴森云或戴森球上传输回来的高能射线.
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_02"))
            // #tr Tooltip_DSPReceiver_03
            // # The received energy can be exported directly to the Wireless EU Net or Dynamo Hatches or stored as Critical Photons.
            // #zh_CN 可以将接收到的能量直接输出到无线EU电网或动力仓, 或者存储为临界状态的光子.
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_03"))
            // #tr Tooltip_DSPReceiver_04
            // # Ratio of the requesting from Dyson Sphere power point can be limited by putting Integrated Circuit into controller block.
            // #zh_CN 在控制器方块内放置编程电路可以限制请求功率的比率.
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_04"))
            // #tr Tooltip_DSPReceiver_05
            // # At the same time, the maximum requested power point is {\GOLD}1024{\GRAY}A Max (default).
            // #zh_CN 同时, 最大请求功率 {\GOLD}1024{\GRAY}A Max (默认).
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_05"))
            // #tr Tooltip_DSPReceiver_06
            // # Actual output power is affected by stellar and planetary coefficients.
            // #zh_CN 实际输出功率受恒星系数和行星系数影响.
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_06"))
            // #tr Tooltip_DSPReceiver_07
            // # Inputting Gravitational Lens will enable intensify mode. Increase actual output power.
            // #zh_CN 输入引力透镜将启用增强模式. 提高实际输出功率.
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_07"))
            // #tr Tooltip_DSPReceiver_08
            // # Joining the wireless EU network when without installing a dynamo hatch.
            // #zh_CN 未安装动力仓时自动进入无线电力网络模式.
            .addInfo(TextEnums.tr("Tooltip_DSPReceiver_08"))
            .addStructureInfo(Tooltip_Details)
            // #tr Tooltip_DSPReceiver_02_06
            // # Requesting ratio = Integrated Circuit Number / Stack Size
            // #zh_CN 请求比率 = 编程电路编号 / 堆叠数量
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_06"))
            // #tr Tooltip_DSPReceiver_02_01
            // # Actual Generating EU = used power point * stellar coefficient * planet coefficient * 1 or 2 in intensify mode
            // #zh_CN 实际产生EU = 已占用产能点数 * 恒星系数 * 行星系数 * 1 或 4 在增强模式时
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_01"))
            // #tr Tooltip_DSPReceiver_02_02
            // # Personal Dimension was been classified as Overworld(Earth).
            // #zh_CN 私人维度归类于主世界(地球).
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_02"))
            // #tr Tooltip_DSPReceiver_02_03
            // # Every Gravitational Lens will provide (default) 10 minutes of intensify mode.
            // #zh_CN 每个引力透镜可以提供 (默认) 10 分钟的增强模式.
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_03"))
            // #tr Tooltip_DSPReceiver_02_04
            // # Input Gravitational Lens will be consumed immediately.
            // #zh_CN 输入的引力透镜会立刻被消耗.
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_04"))
            // #tr Tooltip_DSPReceiver_02_05
            // # Converted to remaining time of intensify mode.
            // #zh_CN 转换成剩余的增强模式时间.
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_05"))
            // #tr Tooltip_DSPReceiver_02_07
            // # Put §b§l§oAstral Array Fabricator§7 into controller slot then this machine can request over 1024A Max power point.
            // #zh_CN 在控制器内放入 {\AQUA}{\BOLD}{\ITALIC}星阵{\GRAY} 可以使此机器请求超过1024A Max的能量点数.
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_07"))
            // #tr Tooltip_DSPReceiver_02_08
            // # Final requesting power point limit = Astral Array Fabricator amount^2 * 2048A Max
            // #zh_CN 最终请求能量点数上限 = 星阵数量^2 * 2048A Max
            .addStructureInfo(TextEnums.tr("Tooltip_DSPReceiver_02_08"))
            .addStructureInfo(EnumChatFormatting.GOLD + "-----------------------------------------")
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
            .addStructureInfo(EnumChatFormatting.GOLD + "-----------------------------------------")
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .addDynamoHatch(textUseBlueprint, 1)
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
