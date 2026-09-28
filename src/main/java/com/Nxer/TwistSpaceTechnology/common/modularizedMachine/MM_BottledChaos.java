package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.OverclockController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.ParallelController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.PowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.SpeedController;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_BottledChaosRecipeTokenHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineSupportAllModuleBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosOutputBuffer;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosPowerAllocator;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosRecipeCategory;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosThread;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ModularHatchBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.IDualInputHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MM_BottledChaos extends ModularizedMachineSupportAllModuleBase<MM_BottledChaos> {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int HORIZONTAL_OFFSET = 14;
    private static final int VERTICAL_OFFSET = 34;
    private static final int DEPTH_OFFSET = 12;
    private static final int CAPACITY_REGION_COUNT = 4;
    private static final int CAPACITY_REGION_BLOCK_COUNT = 64;
    private static final int CAPACITY_LEVEL_COUNT = 5;
    private static final int RECIPE_CATEGORY_SLOT_COUNT = BottledChaosRecipeCategory.values().length;
    private static final String RECIPE_CATEGORY_SLOT_NBT = "bottledChaosRecipeCategorySlot";
    private static final String WAILA_CAPACITY = "bottledChaosCapacity";
    private static final String WAILA_RECIPE_CATEGORIES = "bottledChaosRecipeCategories";
    private static final String WAILA_RECIPE_TOKENS = "bottledChaosRecipeTokens";
    private static final int THREAD_COUNT = 15;
    private static final String THREAD_NBT = "bottledChaosThreads";
    private static final Collection<ModularHatchTypes> SUPPORTED_MODULE_TYPES = ImmutableList.of(
        ModularHatchTypes.PARALLEL_CONTROLLER,
        ModularHatchTypes.SPEED_CONTROLLER,
        ModularHatchTypes.POWER_CONSUMPTION_CONTROLLER,
        ModularHatchTypes.OVERCLOCK_CONTROLLER);

    // spotless:off
    private static final String[][] SHAPE = {
        {"                             ","                             ","                             ","     MMMMMM       MMMMMM     ","    M                   M    ","   M                     M   ","   M                     M   ","   M                     M   ","   M                     M   ","   M                     M   ","   M                     M   ","                             ","                             ","                             ","                             ","                             ","                             ","   M                         ","   M                     M   ","   M                     M   ","   M                     M   ","   M                     M   ","   M                     M   ","   M                     M   ","    M                   M    ","     MMMMMM       MMMMMM     ","                             ","                             ","                             "},
        {"                             ","                             ","          M       M          ","     GGGGGGGGGGGGGGGGGGG     ","    G     M       M     G    ","   G      M       M      G   ","   G      M       M      G   ","   G      M       M      G   ","   G     M         M     G   ","   G    M           M    G   ","  MGMMMM             MMMMGM  ","   G                     G   ","   G                     G   ","   G                     G   ","   G                     G   ","   G                     G   ","   G                     G   ","   G                     G   ","  MGMMMM             MMMMGM  ","   G    M           M    G   ","   G     M         M     G   ","   G      M       M      G   ","   G      M       M      G   ","   G      M       M      G   ","    G     M       M     G    ","     GGGGGGGGGGGGGGGGGGG     ","          M       M          ","                             ","                             "},
        {"                             ","          MMMMMMMMM          ","     HHHHHHHHHHHHHHHHHHH     ","    GFFFFFFGGGGGGGFFFFFFG    ","   GFIIIIIFGGGGGGGFIIIIIFG   ","  HFIIIIIIFIIIIIIIFIIIIIIFH  ","  HFIIIIIIFGGGGGGGFIIIIIIFH  ","  HFIIIIIIFGGGGGGGFIIIIIIFH  ","  HFIIIIIFGGAAAAAGGFIIIIIFH  ","  HFIIIIFGAAAAAAAAAGFIIIIFH  "," MHFFFFFGAAAAAAAAAAAGFFFFFHM "," MHGGIGGGAAAAAAAAAAAGGGIGGHM "," MHGGIGGAAAAAAAAAAAAAGGIGGHM "," MHGGIGGAAAAAAAAAAAAAGGIGGHM "," MHGGIGGAAAAAAAAAAAAAGGIGGHM "," MHGGIGGAAAAAAAAAAAAAGGIGGHM "," MHGGIGGAAAAAAAAAAAAAGGIGGHM "," MHGGIGGGAAAAAAAAAAAGGGIGGHM "," MHFFFFFGAAAAAAAAAAAGFFFFFHM ","  HFIIIIFGAAAAAAAAAGFIIIIFH  ","  HFIIIIIFGGAAAAAGGFIIIIIFH  ","  HFIIIIIIFGGGGGGGFIIIIIIFH  ","  HFIIIIIIFGGGGGGGFIIIIIIFH  ","  HFIIIIIIFIIIIIIIFIIIIIIFH  ","   GFIIIIIFGGGGGGGFIIIIIFG   ","    GFFFFFFGGGGGGGFFFFFFG    ","     HHHHHHHHHHHHHHHHHHH     ","          MMMMMMMMM          ","                             "},
        {"                             ","          GGGGGGGGG          ","    HGGGGGGHHHHHHHGGGGGGH    ","   HFEGGGGGFFFFFFFGGGGGEFH   ","  HFEG    GL     LG    GEFH  ","  GEG     G       G     GEG  ","  GG      G       G      GG  ","  GG      G       G      GG  ","  GG     G         G     GG  ","  GG    G           G    GG  "," GGGGGGG             GGGGGGG "," GHFL                   LFHG "," GHF                     FHG "," GHF                     FHG "," GHF                     FHG "," GHF                     FHG "," GHF                     FHG "," GHFL                   LFHG "," GGGGGGG             GGGGGGG ","  GG    G           G    GG  ","  GG     G         G     GG  ","  GG      G       G      GG  ","  GG      G       G      GG  ","  GEG     G       G     GEG  ","  HFEG    GL     LG    GEFH  ","   HFEGGGGGFFFFFFFGGGGGEFH   ","    HGGGGGGHHHHHHHGGGGGGH    ","          GGGGGGGGG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGHHHHHHHGGGGGGGH   ","  HFEEGGGGGFGGGGGFGGGGGEEFH  ","  GEEG    GL     LG    GEEG  ","  GEG     G       G     GEG  ","  GG      G       G      GG  ","  GG      G       G      GG  ","  GG     G         G     GG  ","  GG    G           G    GG  "," GGGGGGG             GGGGGGG "," AHFL                   LFHA "," AHG                     GHA "," AHG                     GHA "," AHG                     GHA "," AHG                     GHA "," AHG                     GHA "," AHFL                   LFHA "," GGGGGGG             GGGGGGG ","  GG    G           G    GG  ","  GG     G         G     GG  ","  GG      G       G      GG  ","  GG      G       G      GG  ","  GEG     G       G     GEG  ","  GEEG    GL     LG    GEEG  ","  HFEEGGGGGFGGGGGFGGGGGEEFH  ","   HGGGGGGGHHHHHHHGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGMMGGFMMMMMFGGMMGEEFH  ","  GEEG    GL     LG    GEEG  ","  GEG     M       M     GEG  ","  GG      M       M      GG  ","  GM      M       M      MG  ","  GM     M         M     MG  ","  GG    M           M    GG  "," GGGGMMM             MMMGGGG "," ANFL                   LFNA "," A M                     M A "," A M                     M A "," A M                     M A "," A M                     M A "," A M                     M A "," ANFL                   LFNA "," GGGGMMM             MMMGGGG ","  GG    M           M    GG  ","  GM     M         M     MG  ","  GM      M       M      MG  ","  GG      M       M      GG  ","  GEG     M       M     GEG  ","  GEEG    GL     LG    GEEG  ","  HFEEGMMGGFMMMMMFGGMMGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGFGGGGGFGGGGGEEFH  ","  GEEG    GL     LG    GEEG  ","  GEG     G       G     GEG  ","  GG      G       G      GG  ","  GG      G       G      GG  ","  GG     G         G     GG  ","  GG    G           G    GG  "," GGGGGGG             GGGGGGG "," ANFL                   LFNA "," A G                     G A "," A G                     G A "," A G                     G A "," A G                     G A "," A G                     G A "," ANFL                   LFNA "," GGGGGGG             GGGGGGG ","  GG    G           G    GG  ","  GG     G         G     GG  ","  GG      G       G      GG  ","  GG      G       G      GG  ","  GEG     G       G     GEG  ","  GEEG    GL     LG    GEEG  ","  HFEEGGGGGFGGGGGFGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGFFFFFFFGGGGGEEFH  ","  GEEG    GL     LG    GEEG  ","  GEG MMM G       G MMM GEG  ","  GG MHHM G       G MHHM GG  ","  GG MHHM G       G MHHM GG  ","  GG MMM G         G MMM GG  ","  GG    G           G    GG  "," GGGGGGG             GGGGGGG "," ANFL                   LFNA "," A F                     F A "," A F                     F A "," A F                     F A "," A F                     F A "," A F                     F A "," ANFL                   LFNA "," GGGGGGG             GGGGGGG ","  GG    G           G    GG  ","  GG MMM G         G MMM GG  ","  GG MHHM G       G MHHM GG  ","  GG MHHM G       G MHHM GG  ","  GEG MMM G       G MMM GEG  ","  GEEG    GL     LG    GEEG  ","  HFEEGGGGGFFFFFFFGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEHHHHGFHHHHHFGHHHHEEFH  ","  GEEH    HH     HH    HEEG  ","  GEH HHH H       H HHH HEG  ","  GH HFFH H       H HFFH HG  ","  GH HFFH H       H HFFH HG  ","  GH HHH H         H HHH HG  ","  GH    H           H    HG  "," GGGHHHH             HHHHGGG "," ANFH                   HFNA "," A H                     H A "," A H                     H A "," A H                     H A "," A H                     H A "," A H                     H A "," ANFH                   HFNA "," GGGHHHH             HHHHGGG ","  GH    H           H    HG  ","  GH HHH H         H HHH HG  ","  GH HFFH H       H HFFH HG  ","  GH HFFH H       H HFFH HG  ","  GEH HHH H       H HHH HEG  ","  GEEH    HH     HH    HEEG  ","  HFEEHHHHGFHHHHHFGHHHHEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEFFFHGFFFFFFFGHFFFEEFH  ","  GEEF    HF     FH    FEEG  ","  GEF AAA F       F AAA FEG  ","  GF AaaA F       F AbbA FG  ","  GF AaaA F       F AbbA FG  ","  GF AAA F         F AAA FG  ","  GH    F           F    HG  "," GGGHFFF             FFFHGGG "," ANFF                   FFNA "," A F                     F A "," A F                     F A "," A F                     F A "," A F                     F A "," A F                     F A "," ANFF                   FFNA "," GGGHFFF             FFFHGGG ","  GH    F           F    HG  ","  GF AAA F         F AAA FG  ","  GF AccA F       F AddA FG  ","  GF AccA F       F AddA FG  ","  GEF AAA F       F AAA FEG  ","  GEEF    HF     FH    FEEG  ","  HFEEFFFHGFFFFFFFGHFFFEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEHHHHGFHHHHHFGHHHHEEFH  ","  GEEH    HH     HH    HEEG  ","  GEH AAA H       H AAA HEG  ","  GH AaaA H       H AbbA HG  ","  GH AaaA H       H AbbA HG  ","  GH AAA H         H AAA HG  ","  GH    H           H    HG  "," GGGHHHH             HHHHGGG "," ANFH                   HFNA "," A H                     H A "," A H                     H A "," A H                     H A "," A H                     H A "," A H                     H A "," ANFH                   HFNA "," GGGHHHH             HHHHGGG ","  GH    H           H    HG  ","  GH AAA H         H AAA HG  ","  GH AccA H       H AddA HG  ","  GH AccA H       H AddA HG  ","  GEH AAA H       H AAA HEG  ","  GEEH    HH     HH    HEEG  ","  HFEEHHHHGFHHHHHFGHHHHEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGN     NGGGGGGGH   ","  HFEEGGGGGF     FGGGGGEEFH  ","  GEEG    AL     LA    GEEG  ","  GEG AAA A       A AAA GEG  ","  GG AaaA A       A AbbA GG  ","  GG AaaA A       A AbbA GG  ","  GG AAA A         A AAA GG  ","  GG    A           A    GG  "," GGGAAAA             AAAAGGG "," ANFL                   LFNA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," ANFL                   LFNA "," GGGAAAA             AAAAGGG ","  GG    A           A    GG  ","  GG AAA A         A AAA GG  ","  GG AccA A       A AddA GG  ","  GG AccA A       A AddA GG  ","  GEG AAA A       A AAA GEG  ","  GEEG    AL     LA    GEEG  ","  HFEEGGGGGF     FGGGGGEEFH  ","   HGGGGGGGN     NGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GAAAAAAAG          ","   HGGGGGGGHHHHHHHGGGGGGGH   ","  HFEEHHHHGF     FGHHHHEEFH  ","  GEEH    HG     GH    HEEG  ","  GEH AAA H       H AAA HEG  ","  GH AaaA H       H AbbA HG  ","  GH AaaA H       H AbbA HG  ","  GH AAA H         H AAA HG  ","  GH    H           H    HG  "," GGGHHHH             HHHHGGG "," AHFG                   GFHA "," AH                       HA "," AH                       HA "," AH                       HA "," AH                       HA "," AH                       HA "," AHFG                   GFHA "," GGGHHHH             HHHHGGG ","  GH    H           H    HG  ","  GH AAA H         H AAA HG  ","  GH AccA H       H AddA HG  ","  GH AccA H       H AddA HG  ","  GEH AAA H       H AAA HEG  ","  GEEH    HG     GH    HEEG  ","  HFEEHHHHGF     FGHHHHEEFH  ","   HGGGGGGGHHHHHHHGGGGGGGH   ","          GAAAAAAAG          ","                             "},
        {"                             ","          GGGGGGGGG          ","    HGGGGGGHHHHHHHGGGGGGH    ","   HF     GFGGGGGFG     FH   ","  HFEHMMMMHG     GHMMMMHEFH  ","  G HMFFFMH       HMFFFMH G  ","  G MFKKFMH       HMFKKFM G  ","  G MFKKFMH       HMFKKFM G  ","  G MFFFMH         HMFFFM G  ","  G MMMMH           HMMMM G  "," GGGHHHH             HHHHGGG "," GHFG                   GFHG "," GHG                     GHG "," GHG                     GHG "," GHG                     GHG "," GHG                     GHG "," GHG                     GHG "," GHFG                   GFHG "," GGGHHHH             HHHHGGG ","  G MMMMH           HMMMMHG  ","  G MFFFMH         HMFFFMHG  ","  G MFKKFMH       HMFKKFMHG  ","  G MFKKFMH       HMFKKFMHG  ","  G HMFFFMH       HMFFFMHEG  ","  HFEHMMMMHG     GHMMMMHEFH  ","   HF     GFGGGGGFGHHHHEFH   ","    HGGGGGGHHHHHHHGGGGGGH    ","          GGGGGGGGG          ","                             "},
        {"                             ","          G       G          ","     HHHHHHHHHHHHHHHHHHH     ","    HFFFFFFFFFFFFFFFFFFFH    ","   HFFFFFFFFFFFFFFFFFFFFFH   ","  HFFFFFFFFFFFFFFFFFFFFFFFH  ","  HFFFKKFFFFFFFFFFFFFKKFFFH  ","  HFFFKKFFFFFFFFFFFFFKKFFFH  ","  HFFFFFFFFFFFFFFFFFFFFFFFH  ","  HFFFFFFFFFAAAAAFFFFFFFFFH  "," GHFFFFFFFFAAAAAAAFFFFFFFFHG ","  HFFFFFFFAAAAAAAAAFFFFFFFH  ","  HFFFFFFAAAAAAAAAAAFFFFFFH  ","  HFFFFFFAAAAAAAAAAAFFFFFFH  ","  HFFFFFFAAAAAAAAAAAFFFFFFH  ","  HFFFFFFAAAAAAAAAAAFFFFFFH  ","  HFFFFFFAAAAAAAAAAAFFFFFFH  ","  HFFFFFFFAAAAAAAAAFFFFFFFH  "," GHFFFFFFFFAAAAAAAFFFFFFFFHG ","  HFFFFFFFFFAAAAAFFFFFFFFFH  ","  HFFFFFFFFFFFFFFFFFFFFFFFH  ","  HFFFKKFFFFFFFFFFFFFKKFFFH  ","  HFFFKKFFFFFFFFFFFFFKKFFFH  ","  HFFFFFFFFFFFFFFFFFFFFFFFH  ","   HFFFFFFFFFFFFFFFFFFFFFH   ","    HFFFFFFFFFFFFFFFFFFFH    ","     HHHHHHHHHHHHHHHHHHH     ","          G       G          ","                             "},
        {"                             ","          G       G          ","          GGGGGGGGG          ","         GG       GG         ","       GG           GG       ","     GGS             SGG     ","     GSDS           SDSG     ","    GSDDS           SDDSG    ","    G SS             SS G    ","   G                     G   "," GGG                     GGG ","  G                       G  ","  G                       G  ","  G                       G  ","  G                       G  ","  G                       G  ","  G                       G  ","  G                       G  "," GGG                     GGG ","   G                     G   ","    G SS             SS G    ","    GSDDS           SDDSG    ","     GSDS           SDSG     ","     GGS             SGG     ","       GG           GG       ","         GG       GG         ","          GGGGGGGGG          ","          G       G          ","                             "},
        {"                             ","                             ","           GGGGGGG           ","         GG       GG         ","       GG           GG       ","     GGS             SGG     ","     GSDS           SDSG     ","    GSDDS           SDDSG    ","    G SS             SS G    ","   G                     G   ","   G                     G   ","  G                       G  ","  G                       G  ","  G          TTT          G  ","  G          TTT          G  ","  G          TTT          G  ","  G                       G  ","  G                       G  ","   G                     G   ","   G                     G   ","    G SS             SS G    ","    GSDDS           SDDSG    ","     GSDS           SDSG     ","     GGS             SGG     ","       GG           GG       ","         GG       GG         ","           GGGGGGG           ","                             ","                             "},
        {"                             ","                             ","           GGGGGGG           ","         GG       GG         ","       GG           GG       ","     GGS             SGG     ","     GSDS           SDSG     ","    GSDDS           SDDSG    ","    G SSS           SSS G    ","   G                     G   ","   G                     G   ","  G                       G  ","  G          TTT          G  ","  G         T   T         G  ","  G         T P T         G  ","  G         T   T         G  ","  G          TTT          G  ","  G                       G  ","   G                     G   ","   G                     G   ","    G SSS           SSS G    ","    GSDDS           SDDSG    ","     GSDS           SDSG     ","     GGS             SGG     ","       GG           GG       ","         GG       GG         ","           GGGGGGG           ","                             ","                             "},
        {"                             ","                             ","                             ","           HHHHHHH           ","         HH       HH         ","       HH           HH       ","      HSS           SSH      ","     HSDDS         SDDSH     ","     HSDDS         SDDSH     ","    H  SS           SS  H    ","    H                   H    ","   HH                   HH   ","   HH        AAA        HH   ","   HH       A   A       HH   ","   HH       A P A       HH   ","   HH       A   A       HH   ","   HH        AAA        HH   ","   HH                   HH   ","    H                   H    ","    H  SS           SS  H    ","     HSDDS         SDDSH     ","     HSDDS         SDDSH     ","      HSS           SSH      ","       HH           HH       ","         HHHHHHHHHHH         ","           HHHHHHH           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           FH   HF           ","         FF       FF         ","       FF           FF       ","      FSS           SSF      ","     FSDDS         SDDSF     ","     FSDDS         SDDSF     ","    F  SS           SS  F    ","    F                   F    ","   FH                   HF   ","   FU        AAA        UF   ","   FU       A   A       UF   ","   FU       A O A       UF   ","   FU       A   A       UF   ","   FU        AAA        UF   ","   FH                   HF   ","    F                   F    ","    F  SS           SS  F    ","     FSDDS         SDDSF     ","     FSDDS         SDDSF     ","      FSS           SSF      ","       FF           FF       ","         FFHUUUUUHFF         ","           FFFFFFF           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           FH   HF           ","         FF       FF         ","       FF           FF       ","      FSS           SSF      ","     FSDDS         SDDSF     ","     FSDDS         SDDSF     ","    F  SS           SS  F    ","    F                   F    ","   FH                   HF   ","   FV        AAA        VF   ","   FV       A   A       VF   ","   FV       A P A       VF   ","   FV       A   A       VF   ","   FV        AAA        VF   ","   FH                   HF   ","    F                   F    ","    F  SS           SS  F    ","     FSDDS         SDDSF     ","     FSDDS         SDDSF     ","      FSS           SSF      ","       FF           FF       ","         FFHVVVVVHFF         ","           FFFFFFF           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           FH   HF           ","         FF       FF         ","       FF           FF       ","      FSS           SSF      ","     FSDDS         SDDSF     ","     FSDDS         SDDSF     ","    F  SS           SS  F    ","    F                   F    ","   FH                   HF   ","   FW        TTT        WF   ","   FW       T   T       WF   ","   FW       T P T       WF   ","   FW       T   T       WF   ","   FW        TTT        WF   ","   FH                   HF   ","    F                   F    ","    F  SS           SS  F    ","     FSDDS         SDDSF     ","     FSDDS         SDDSF     ","      FSS           SSF      ","       FF           FF       ","         FFHWWWWWHFF         ","           FFFFFFF           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           HHHHHHH           ","         HH       HH         ","       HH           HH       ","      HSS           SSH      ","     HSDDS         SDDSH     ","     HSDDS         SDDSH     ","    H  SS           SS  H    ","    H                   H    ","   HH                   HH   ","   HP       XX~XX       PH   ","   HP        TTT        PH   ","   HP        TPT        PH   ","   HP        TTT        PH   ","   HP                   PH   ","   HH                   HH   ","    H                   H    ","    H  SS           SS  H    ","     HSDDS         SDDSH     ","     HSDDS         SDDSH     ","      HSS           SSH      ","       HH           HH       ","         HHHPPPPPHHH         ","           HHHHHHH           ","                             ","                             ","                             "},
        {"                             ","                             ","           GGGGGGG           ","         GGFFFFFFFGG         ","       GGFFGGGGGGGFFGG       ","     GGFFGGGGGGGGGGGFFGG     ","     GFSSGGMMGGGMMGGSSFG     ","    GFSDDSM MGGGM MSDDSFG    ","    GFSDDSM MGGGM MSDDSFG    ","   GFGGSSM  MGGGM  MSSGGFG   ","   GFGGMM   MGGGM   MMGGFG   ","  GFGGM     MGGGM     MGGFG  ","  GFBGMMMMMMGBBBGMMMMMMGBFG  ","  GFBGGGGGGGBBBBBGGGGGGGBFG  ","  GFBBBBBBBBBBRBBBBBBBBBBFG  ","  GFBGGGGGGGBBBBBGGGGGGGBFG  ","  GFBGMMMMMMGBBBGMMMMMMGBFG  ","  GFGGM     MGBGM     MGGFG  ","   GFGGMM   MGBGM   MMGGFG   ","   GFGGSSM  MGBGM  MSSGGFG   ","    GFSDDSM MGBGM MSDDSFG    ","    GFSDDSM MGBGM MSDDSFG    ","     GFSSGGMMGBGMMGGSSFG     ","     GGFFGGGGGBGGGGGFFGG     ","       GGFFGBBBBBGFFGG       ","         GGFFFFFFFGG         ","           GGGGGGG           ","                             ","                             "},
        {"          Q       Q          ","   QQQQQQQQQQQQQQQQQQQQQQQ   ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  "," QQQQQQQQQQFFFFFFFQQQQQQQQQQ "," QQQQQQQQFFQQQQQQQFFQQQQQQQQ "," QQQQQQFFQQQQQQQQQQQFFQQQQQQ "," QQQQQFSSQQGGGGGGGQQSSFQQQQQ "," QQQQFSDDSGGSSSSSGGSDDSFQQQQ "," QQQQFSDDSGGGGGGGGGSDDSFQQQQ "," QQQFQQSSSSGGGGGGGGSSSQQFQQQ ","QQQQFQQGGGSSSGGGGGSSGGQQFQQQQ"," QQFQQGGGGGGSGGGGGSGGGGGGFQQ "," QQFQQGSGGGGSSSGSSSGGSGGGFQQ "," QQFQQGSGGGGGGSGSGGGGSGGGFQQ "," QQFQQGSGGGGSSCSSGGGGSGGGFQQ "," QQFQQGSGGGGSGSGGGGGGSGGGFQQ "," QQFQQGSGGSSSGSSSGGGGSGGGFQQ "," QQFQQGGGGSGGGGGSGGGGGGGGFQQ ","QQQQFQQGGSSGGGGGSSSGGGQQFQQQQ"," QQQFQQSSSGGGGGGGGSSSSQQFQQQ "," QQQQFSDDSGGGGGGGGGSDDSFQQQQ "," QQQQFSDDSGGSSSSSGGSDDSFQQQQ "," QQQQQFSSQQGGGGGGGQQSSFQQQQQ "," QQQQQQFFQQQQQQQQQQQFFQQQQQQ "," QQQQQQQQFFQQQQQQQFFQQQQQQQQ "," QQQQQQQQQQFFFFFFFQQQQQQQQQQ ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  ","   QQQQQQQQQQQQQQQQQQQQQQQ   ","          Q       Q          "},
        {"          Q       Q          ","   QQQQQQQQQQQQQQQQQQQQQQQ   ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  "," QQQQQQQQQQFFFFFFFQQQQQQQQQQ "," QQQQQQQQFFQQQQQQQFFQQQQQQQQ "," QQQQQQFFQQQQQQQQQQQFFQQQQQQ "," QQQQQFSSQQQQQQQQQQQSSFQQQQQ "," QQQQFSDDSQQQQQQQQQSDDSFQQQQ "," QQQQFSDDSQQQQQQQQSDDDSFQQQQ "," QQQFQQSDDDSSQQQQQSDSSQQFQQQ ","QQQQFQQQSSDDDSQQQSDDQQQQFQQQQ"," QQFQQQQQQSSDSSQSSDSQQQQQFQQ "," QQFQQQQQQQSDDDSDDDSQQQQQFQQ "," QQFQQQQQQQQSSDSDSSQQQQQQFQQ "," QQFQQQQQQQSDDCDDSQQQQQQQFQQ "," QQFQQQQQQSSDSDSSQQQQQQQQFQQ "," QQFQQQQQSDDDSDDDSQQQQQQQFQQ "," QQFQQQQQSDSSQSSDSSQQQQQQFQQ ","QQQQFQQQQDDSQQQSDDDSSQQQFQQQQ"," QQQFQQSSDSQQQQQSSDDDSQQFQQQ "," QQQQFSDDDSQQQQQQQQSDDSFQQQQ "," QQQQFSDDSQQQQQQQQQSDDSFQQQQ "," QQQQQFSSQQQQQQQQQQQSSFQQQQQ "," QQQQQQFFQQQQQQQQQQQFFQQQQQQ "," QQQQQQQQFFQQQQQQQFFQQQQQQQQ "," QQQQQQQQQQFFFFFFFQQQQQQQQQQ ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  ","   QQQQQQQQQQQQQQQQQQQQQQQ   ","          Q       Q          "},
        {"   QQQQQQQQQQQQQQQQQQQQQQQ   ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  "," QQQQQQQQQQQQQQQQQQQQQQQQQQQ ","QQQQQQQQQQQFFFFFFFQQQQQQQQQQQ","QQQQQQQQQFFFFFFFFFFFQQQQQQQQQ","QQQQQQQFFFFFFFFFFFFFFFQQQQQQQ","QQQQQQFFFFFFFFFFFFFFFFFQQQQQQ","QQQQQFFSSFFFFFFFFFFFSSFFQQQQQ","QQQQQFFSSFFFFFFFFFFSSSFFQQQQQ","QQQQFFFFSSSFFFFFFFFSFFFFFQQQQ","QQQQFFFFFFSSSFFFFFSSFFFFFQQQQ","QQQFFFFFFFFFSFFFFFSFFFFFFFQQQ","QQQFFFFFFFFFSSSFSSSFFFFFFFQQQ","QQQFFFFFFFFFFFSFSFFFFFFFFFQQQ","QQQFFFFFFFFFSSFSSFFFFFFFFFQQQ","QQQFFFFFFFFFSFSFFFFFFFFFFFQQQ","QQQFFFFFFFSSSFSSSFFFFFFFFFQQQ","QQQFFFFFFFSFFFFFSFFFFFFFFFQQQ","QQQQFFFFFSSFFFFFSSSFFFFFFQQQQ","QQQQFFFFFSFFFFFFFFSSSFFFFQQQQ","QQQQQFFSSSFFFFFFFFFFSSFFQQQQQ","QQQQQFFSSFFFFFFFFFFFSSFFQQQQQ","QQQQQQFFFFFFFFFFFFFFFFFQQQQQQ","QQQQQQQFFFFFFFFFFFFFFFQQQQQQQ","QQQQQQQQQFFFFFFFFFFFQQQQQQQQQ","QQQQQQQQQQQFFFFFFFQQQQQQQQQQQ"," QQQQQQQQQQQQQQQQQQQQQQQQQQQ ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  ","   QQQQQQQQQQQQQQQQQQQQQQQ   "},
        {"   QQQQQQQQQQQQQQQQQQQQQQQ   ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  "," QQQQQQQQQQQQQQQQQQQQQQQQQQQ ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ"," QQQQQQQQQQQQQQQQQQQQQQQQQQQ ","  QQQQQQQQQQQQQQQQQQQQQQQQQ  ","   QQQQQQQQQQQQQQQQQQQQQQQ   "},
    };
    // spotless:on

    private static IStructureDefinition<MM_BottledChaos> structureDefinition;
    private static MultiblockTooltipBuilder tooltip;

    private final int[][] capacityTierCounts = new int[CAPACITY_REGION_COUNT][CAPACITY_LEVEL_COUNT];
    private final ItemStackHandler recipeCategoryInventory = new ItemStackHandler(RECIPE_CATEGORY_SLOT_COUNT) {

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return isRecipeCategoryCredentialForSlot(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            authorizationDirty = true;
            markDirty();
        }
    };
    private final TST_BottledChaosRecipeTokenHatch[] credentialHatches = new TST_BottledChaosRecipeTokenHatch[THREAD_COUNT];
    private final MTEHatchInputBus[] threadInputBuses = new MTEHatchInputBus[THREAD_COUNT];
    private final MTEHatchInput[] threadInputHatches = new MTEHatchInput[THREAD_COUNT];
    private final BottledChaosThread[] threads = new BottledChaosThread[THREAD_COUNT];
    private final BottledChaosProcessingLogic[] threadLogics = new BottledChaosProcessingLogic[THREAD_COUNT];
    private final ItemStack[] installedCredentials = new ItemStack[THREAD_COUNT];
    private final long[] powerBudgets = new long[THREAD_COUNT];
    private final boolean[] advancing = new boolean[THREAD_COUNT];
    private long[] allocatedShares = new long[THREAD_COUNT];
    private long capacity;
    private long allocatedCapacity = -1;
    private long inputPower = -1;
    private long categoryMask;
    private long schedulerTick;
    private boolean authorizationDirty = true;
    private boolean overCapacity;
    private int credentialHatchLimit;

    public MM_BottledChaos(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        initializeThreads();
    }

    public MM_BottledChaos(String name) {
        super(name);
        initializeThreads();
    }

    private void initializeThreads() {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new BottledChaosThread();
            threadLogics[i] = new BottledChaosProcessingLogic();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        for (int slot = 0; slot < RECIPE_CATEGORY_SLOT_COUNT; slot++) {
            GTUtility.saveItem(nbt, RECIPE_CATEGORY_SLOT_NBT + slot, recipeCategoryInventory.getStackInSlot(slot));
        }
        NBTTagList jobs = new NBTTagList();
        for (BottledChaosThread thread : threads) jobs.appendTag(thread.save());
        nbt.setTag(THREAD_NBT, jobs);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        for (int slot = 0; slot < RECIPE_CATEGORY_SLOT_COUNT; slot++) {
            recipeCategoryInventory.setStackInSlot(slot, GTUtility.loadItem(nbt, RECIPE_CATEGORY_SLOT_NBT + slot));
        }
        NBTTagList jobs = nbt.getTagList(THREAD_NBT, 10);
        for (int i = 0; i < THREAD_COUNT; i++) threads[i].load(jobs.getCompoundTagAt(i));
        authorizationDirty = true;
        allocatedCapacity = -1;
        inputPower = -1;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MM_BottledChaos(mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.BottledChaosRecipes;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new TST_Gui_BottledChaos(this);
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public Collection<ModularHatchTypes> getSupportedModularHatchTypes() {
        return SUPPORTED_MODULE_TYPES;
    }

    @Override
    protected boolean canMultiplyModularHatchType() {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        // The controller must never fall back to a shared-input recipe or a shared recipe cache.
        return null;
    }

    public long getInstalledRecipeCategoryMask() {
        long mask = 0;
        for (int slot = 0; slot < RECIPE_CATEGORY_SLOT_COUNT; slot++) {
            BottledChaosRecipeCategory category = BottledChaosRecipeCategory
                .fromCircuit(recipeCategoryInventory.getStackInSlot(slot));
            if (category != null) mask |= category.getMask();
        }
        return mask;
    }

    public boolean isRecipeCategoryCredentialForSlot(int slot, ItemStack stack) {
        BottledChaosRecipeCategory category = BottledChaosRecipeCategory.fromCircuit(stack);
        return category != null && category.getCircuitConfiguration() == slot + 1;
    }

    public ItemStackHandler getRecipeCategoryInventory() {
        return recipeCategoryInventory;
    }

    private boolean addThreadHatch(int slot, int kind, IGregTechTileEntity tile, int texture) {
        if (tile == null) return false;
        if (kind == 0 && tile.getMetaTileEntity() instanceof TST_BottledChaosRecipeTokenHatch hatch) {
            hatch.updateTexture(texture);
            hatch.updateCraftingIcon(getMachineCraftingIcon());
            credentialHatches[slot] = hatch;
            return true;
        }
        if (kind == 1 && tile.getMetaTileEntity() instanceof MTEHatchInputBus bus
            && addInputBusToMachineList(tile, texture)) {
            threadInputBuses[slot] = bus;
            return true;
        }
        if (kind == 2 && tile.getMetaTileEntity() instanceof MTEHatchInput hatch
            && addInputHatchToMachineList(tile, texture)) {
            threadInputHatches[slot] = hatch;
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMachineMM(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        capacity = 0;
        credentialHatchLimit = 0;
        allocatedCapacity = -1;
        Arrays.fill(credentialHatches, null);
        Arrays.fill(threadInputBuses, null);
        Arrays.fill(threadInputHatches, null);
        for (int[] tierCounts : capacityTierCounts) Arrays.fill(tierCounts, 0);
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)) {
            if (errors.isEmpty()) errors.add(StructureErrors.of("bottled_chaos.structure_incomplete"));
            return false;
        }
        if (!calculateCapacity()) {
            errors.add(StructureErrors.of("bottled_chaos.structure.invalid_capacity_region"));
            return false;
        }
        Collection<IModularHatch> parallelControllers = modularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER);
        int parallelControllerCount = parallelControllers == null ? 0 : parallelControllers.size();
        if (parallelControllerCount > 1) {
            errors.add(StructureErrors.of("bottled_chaos.structure.multiple_parallel_controllers"));
            return false;
        }
        if (parallelControllerCount == 1) {
            IModularHatch controller = parallelControllers.iterator()
                .next();
            if (controller instanceof ModularHatchBase hatch) {
                credentialHatchLimit = Math
                    .min(THREAD_COUNT, Math.max(0, (hatch.getTierForStructure() - VoltageIndex.ZPM + 1) * 2 - 1));
            }
        }
        int installedCredentialHatches = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            if (credentialHatches[i] != null) installedCredentialHatches++;
            int installed = (credentialHatches[i] == null ? 0 : 1) + (threadInputBuses[i] == null ? 0 : 1)
                + (threadInputHatches[i] == null ? 0 : 1);
            if (installed != 0 && installed != 3) {
                errors.add(StructureErrors.of("bottled_chaos.structure.incomplete_processing_group"));
                return false;
            }
        }
        if (installedCredentialHatches > credentialHatchLimit) {
            errors.add(StructureErrors.of("bottled_chaos.structure.credential_hatch_limit"));
            return false;
        }
        return true;
    }

    private boolean calculateCapacity() {
        long calculatedCapacity = 0;
        for (int region = 0; region < CAPACITY_REGION_COUNT; region++) {
            int selectedTier = -1;
            for (int tier = 0; tier < CAPACITY_LEVEL_COUNT; tier++) {
                int count = capacityTierCounts[region][tier];
                if (count == 0) continue;
                if (selectedTier >= 0 || count != CAPACITY_REGION_BLOCK_COUNT) return false;
                selectedTier = tier;
            }
            if (selectedTier < 0) return false;
            calculatedCapacity += 1L << (selectedTier * 2);
        }
        capacity = calculatedCapacity;
        return true;
    }

    public long getBottledChaosCapacity() {
        return capacity;
    }

    private boolean validGroup(int slot) {
        return credentialHatches[slot] != null && credentialHatches[slot].isValid()
            && threadInputBuses[slot] != null
            && threadInputBuses[slot].isValid()
            && threadInputHatches[slot] != null
            && threadInputHatches[slot].isValid();
    }

    private void refreshAllocation() {
        boolean changed = authorizationDirty || allocatedCapacity != capacity;
        long newMask = getInstalledRecipeCategoryMask();
        changed |= categoryMask != newMask;
        categoryMask = newMask;
        authorizationDirty = false;
        for (int i = 0; i < THREAD_COUNT; i++) {
            ItemStack credential = credentialHatches[i] != null && credentialHatches[i].isValid()
                ? credentialHatches[i].getAuthorizationStack()
                : null;
            if (!ItemStack.areItemStacksEqual(credential, installedCredentials[i])) {
                installedCredentials[i] = credential == null ? null : credential.copy();
                // Credential changes also discard recipe-search caches, never already-paid jobs.
                threadLogics[i] = new BottledChaosProcessingLogic();
                changed = true;
            }
        }
        long power = changed || schedulerTick % 20 == 0 ? Math.max(0, getMaxInputEu()) : inputPower;
        changed |= power != inputPower;
        if (!changed) return;
        inputPower = power;
        allocatedCapacity = capacity;
        long[] capacities = new long[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++)
            capacities[i] = ItemBottledChaosCredential.getCapacity(installedCredentials[i]);
        long[] shares = BottledChaosPowerAllocator.allocate(capacity, capacities);
        overCapacity = shares == null;
        allocatedShares = overCapacity ? new long[THREAD_COUNT] : shares;
        for (int i = 0; i < THREAD_COUNT; i++) {
            powerBudgets[i] = BottledChaosPowerAllocator.powerForShare(inputPower, allocatedShares[i], capacity);
            threads[i].nextCheckTick = 0;
        }
    }

    private CheckRecipeResult canRunThread(int slot) {
        BottledChaosThread thread = threads[slot];
        if (!validGroup(slot) || !ItemStack.areItemStacksEqual(thread.credential, installedCredentials[slot])) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.credential_changed");
        }
        if ((categoryMask & thread.requiredCategories) != thread.requiredCategories) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.missing_recipe_categories");
        }
        if (thread.eut > powerBudgets[slot]) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.insufficient_power_share");
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessingMM() {
        if (!mMachine) return SimpleCheckRecipeResult.ofFailure("bottled_chaos.structure_incomplete");
        refreshAllocation();
        if (overCapacity) return SimpleCheckRecipeResult.ofFailure("bottled_chaos.capacity_exceeded");
        CheckRecipeResult failure = null;
        boolean hasWork = false;
        for (int i = 0; i < THREAD_COUNT; i++) {
            BottledChaosThread thread = threads[i];
            hasWork |= thread.isBusy();
            if (thread.isBusy() || installedCredentials[i] == null || schedulerTick < thread.nextCheckTick) continue;
            thread.nextCheckTick = schedulerTick + 20;
            CheckRecipeResult candidate = startThread(i);
            thread.status = candidate.wasSuccessful() ? "bottled_chaos.running"
                : candidate == CheckRecipeResultRegistry.NO_RECIPE ? "bottled_chaos.idle"
                    : candidate.getDisplayString();
            hasWork |= candidate.wasSuccessful();
            if (!candidate.wasSuccessful() && candidate != CheckRecipeResultRegistry.NO_RECIPE) failure = candidate;
        }
        return failure != null ? failure
            : hasWork ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NO_RECIPE;
    }

    private CheckRecipeResult startThread(int slot) {
        if (!validGroup(slot)) return SimpleCheckRecipeResult.ofFailure("bottled_chaos.structure_incomplete");
        if (ItemBottledChaosCredential.getRecipeFamily(installedCredentials[slot]) == null) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.unbound");
        }
        if (powerBudgets[slot] <= 0) return SimpleCheckRecipeResult.ofFailure("bottled_chaos.insufficient_power_share");

        // Scope GT's normal/ME/segmented input lifecycle to this group, then restore the structural lists.
        ArrayList<MTEHatchInputBus> allBuses = mInputBusses;
        ArrayList<MTEHatchInput> allHatches = mInputHatches;
        mInputBusses = new ArrayList<>(Collections.singletonList(threadInputBuses[slot]));
        mInputHatches = new ArrayList<>(Collections.singletonList(threadInputHatches[slot]));
        CheckRecipeResult result;
        try {
            startRecipeProcessing();
            result = prepareThread(slot);
            setCheckRecipeResult(result);
        } finally {
            try {
                endRecipeProcessing();
            } finally {
                mInputBusses = allBuses;
                mInputHatches = allHatches;
            }
        }
        // ME extraction failures follow GT's failure-consumption semantics: never award outputs.
        if (!checkRecipeResult.wasSuccessful()) return checkRecipeResult;
        if (!result.wasSuccessful()) return result;
        BottledChaosProcessingLogic logic = threadLogics[slot];
        BottledChaosThread thread = threads[slot];
        thread.credential = installedCredentials[slot].copy();
        thread.requiredCategories = logic.getRequiredCategories();
        thread.eut = logic.getCalculatedEut();
        thread.duration = Math.max(1, logic.getDuration());
        thread.progress = 0;
        thread.parallels = logic.getCurrentParallels();
        thread.outputs.set(logic.getLongItemOutputs(), logic.getLongFluidOutputs());
        markDirty();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private CheckRecipeResult prepareThread(int slot) {
        if (threadInputBuses[slot] instanceof IDualInputHatch dual) {
            CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;
            // Keep crafting-buffer patterns separate, and never inspect another group's buffer.
            for (var iterator = dual.inventories(); iterator.hasNext();) {
                var inventory = iterator.next();
                if (inventory.isEmpty()) continue;
                ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(dual.getSharedItems()));
                items.addAll(Arrays.asList(inventory.getItemInputs()));
                ArrayList<FluidStack> fluids = getStoredFluids();
                fluids.addAll(Arrays.asList(inventory.getFluidInputs()));
                CheckRecipeResult candidate = prepareThread(slot, items, fluids);
                if (candidate.wasSuccessful()) return candidate;
                if (candidate != CheckRecipeResultRegistry.NO_RECIPE) result = candidate;
            }
            return result;
        }
        ArrayList<ItemStack> storedItems = getStoredInputs();
        // The controller and category GUI slots are not additional material inputs.
        storedItems.removeIf(stack -> stack == mInventory[1]);
        return prepareThread(slot, storedItems, getStoredFluids());
    }

    private CheckRecipeResult prepareThread(int slot, List<ItemStack> storedItems, List<FluidStack> storedFluids) {
        ItemStack[] originals = storedItems.stream()
            .filter(stack -> stack != null)
            .toArray(ItemStack[]::new);
        FluidStack[] originalFluids = storedFluids.stream()
            .filter(stack -> stack != null)
            .toArray(FluidStack[]::new);
        ItemStack[] itemCopies = Arrays.stream(originals)
            .map(ItemStack::copy)
            .toArray(ItemStack[]::new);
        FluidStack[] fluidCopies = Arrays.stream(originalFluids)
            .map(FluidStack::copy)
            .toArray(FluidStack[]::new);

        BottledChaosProcessingLogic logic = threadLogics[slot];
        logic.clear();
        logic.clearLongOutputs();
        logic.setMachine(this);
        logic.setRecipeMapSupplier(this::getRecipeMap);
        logic.setRecipeLocking(null, false);
        logic.setAuthorization(installedCredentials[slot], categoryMask);
        logic.setInputItems(itemCopies);
        logic.setInputFluids(fluidCopies);
        logic.setMaxParallel(getTrueParallel());
        logic.setBatchSize(isBatchModeEnabled() ? getMaxBatchSize() : 1);
        logic.setEuModifier(getEuModifier());
        logic.setSpeedBonus(getSpeedBonus());
        logic.setOverclockType(overclockType);
        logic.setAvailableVoltage(powerBudgets[slot]);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(true);
        logic.setVoidProtection(protectsExcessItem(), protectsExcessFluid());
        CheckRecipeResult result = logic.process();
        if (!result.wasSuccessful()) return result;
        if (logic.getCalculatedEut() < 0 || logic.getCalculatedEut() > powerBudgets[slot]) {
            return SimpleCheckRecipeResult.ofFailure("bottled_chaos.insufficient_power_share");
        }

        BottledChaosOutputBuffer candidate = new BottledChaosOutputBuffer();
        candidate.set(logic.getLongItemOutputs(), logic.getLongFluidOutputs());
        BottledChaosOutputBuffer.Transaction reservation = new BottledChaosOutputBuffer.Transaction(this, true);
        for (BottledChaosThread thread : threads) {
            if (!thread.isBusy()) continue;
            result = reservation.reserve(thread.outputs);
            if (!result.wasSuccessful()) return result;
        }
        result = reservation.reserve(candidate);
        if (!result.wasSuccessful()) return result;
        // Only now commit the prechecked input deltas. No real input was touched by ProcessingLogic.
        for (int i = 0; i < originals.length; i++) originals[i].stackSize = itemCopies[i].stackSize;
        for (int i = 0; i < originalFluids.length; i++) originalFluids[i].amount = fluidCopies[i].amount;
        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    protected void runMachine(IGregTechTileEntity base, long tick) {
        schedulerTick = tick;
        refreshAllocation();
        mMaxProgresstime = mProgresstime = 0;
        lEUt = 0;
        if (overCapacity) {
            setCheckRecipeResult(SimpleCheckRecipeResult.ofFailure("bottled_chaos.capacity_exceeded"));
            return;
        }
        if (!base.isAllowedToWork()) return;

        long usage = 0;
        Arrays.fill(advancing, false);
        CheckRecipeResult failure = null;
        for (int i = 0; i < THREAD_COUNT; i++) {
            BottledChaosThread thread = threads[i];
            if (!thread.isBusy()) continue;
            CheckRecipeResult result = canRunThread(i);
            if (!result.wasSuccessful()) {
                thread.status = result.getDisplayString();
                failure = result;
                continue;
            }
            if (thread.progress < thread.duration) {
                advancing[i] = true;
                usage = Math.addExact(usage, thread.eut); // Each EU/t is bounded by disjoint allocated shares.
            }
        }
        lEUt = -usage;
        if (usage > 0 && !drainEnergyInput(usage)) {
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return;
        }
        boolean completed = false;
        for (int i = 0; i < THREAD_COUNT; i++) {
            BottledChaosThread thread = threads[i];
            if (!thread.isBusy()) continue;
            if (advancing[i]) {
                thread.progress++;
                thread.status = "bottled_chaos.running";
                markDirty();
            }
            if (thread.progress >= thread.duration && canRunThread(i).wasSuccessful()
                && (advancing[i] || tick >= thread.nextCheckTick)) {
                BottledChaosOutputBuffer.Transaction output = new BottledChaosOutputBuffer.Transaction(this, false);
                CheckRecipeResult result = output.reserve(thread.outputs);
                if (result.wasSuccessful()) {
                    output.commit();
                    recipesDone += thread.parallels;
                    thread.complete();
                    completed = true;
                    markDirty();
                } else {
                    thread.status = result.getDisplayString();
                    thread.nextCheckTick = tick + 20;
                    failure = result;
                }
            }
            if (thread.isBusy() && advancing[i] && thread.duration > mMaxProgresstime) {
                mMaxProgresstime = thread.duration;
                mProgresstime = thread.progress;
            }
        }
        // Failed/empty slots retry at most once a second. Successful slots may start their next job immediately.
        boolean shouldSearch = completed || tick % 20 == 0 || base.hasWorkJustBeenEnabled();
        if (shouldSearch) {
            CheckRecipeResult result = checkProcessing();
            if (failure == null) setCheckRecipeResult(result);
            for (BottledChaosThread thread : threads) {
                if (thread.isBusy() && thread.status.equals("bottled_chaos.running")
                    && thread.duration > mMaxProgresstime) {
                    mMaxProgresstime = thread.duration;
                    mProgresstime = thread.progress;
                }
            }
        }
        if (failure != null) setCheckRecipeResult(failure);
        mEfficiency = mEfficiencyIncrease = 10000;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        // Keep already-consumed jobs and pending outputs for a safe resume.
        super.stopMachine(reason);
        for (BottledChaosThread thread : threads) {
            if (thread.isBusy()) thread.status = "bottled_chaos.paused";
        }
        markDirty();
    }

    @Override
    public void onBlockDestroyed() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.isServerSide()) {
            for (int slot = 0; slot < RECIPE_CATEGORY_SLOT_COUNT; slot++) {
                ItemStack stack = recipeCategoryInventory.getStackInSlot(slot);
                if (stack == null) continue;
                recipeCategoryInventory.setStackInSlot(slot, null);
                GTUtility
                    .dropItemToBlockPos(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord(), stack);
            }
        }
        super.onBlockDestroyed();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment environment) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            environment,
            false,
            true);
    }

    private static char threadSymbol(int slot, int kind) {
        return (char) (0x100 + slot * 3 + kind);
    }

    private static String[][] threadShape() {
        String[][] shape = new String[SHAPE.length][];
        for (int y = 0; y < SHAPE.length; y++) shape[y] = SHAPE[y].clone();
        int slot = 0;
        for (int z = 0; z < 29; z++) {
            for (int x = 0; x < 29; x++) {
                if (SHAPE[31][z].charAt(x) != 'U') continue;
                for (int kind = 0; kind < 3; kind++) {
                    char[] row = shape[31 + kind][z].toCharArray();
                    row[x] = threadSymbol(slot, kind);
                    shape[31 + kind][z] = new String(row);
                }
                slot++;
            }
        }
        if (slot != THREAD_COUNT) throw new IllegalStateException("Invalid Bottled Chaos processing groups");
        return shape;
    }

    @Override
    public IStructureDefinition<MM_BottledChaos> getStructureDefinition() {
        if (structureDefinition == null) {
            var builder = StructureDefinition.<MM_BottledChaos>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(threadShape()))
                .addElement('A', ofBlock(BorosilicateGlass.getGlassBlock(), 0))
                .addElement('B', Casings.DimensionalInjectionCasing.asElement())
                .addElement('C', Casings.DimensionalBridge.asElement())
                .addElement('D', Casings.SuperconductingCoilBlock.asElement())
                .addElement('E', Casings.BackgroundRadiationAbsorbentCasing.asElement())
                .addElement('F', Casings.MiningNeutroniumCasing.asElement())
                .addElement('G', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
                .addElement('H', Casings.RadiantNaquadahAlloyCasing.asElement())
                .addElement('I', Casings.AdvancedFilterCasing.asElement())
                .addElement('a', capacityElement(0))
                .addElement('b', capacityElement(1))
                .addElement('c', capacityElement(2))
                .addElement('d', capacityElement(3))
                .addElement('K', Casings.MolecularCoil.asElement())
                .addElement('L', Casings.IronFence.asElement())
                .addElement('M', ofFrame(Materials.Neutronium))
                .addElement('N', ofFrame(Materials.NaquadahAlloy))
                .addElement('O', Casings.TiplerStabilisationFieldGenerator.asElement())
                .addElement('P', Casings.ForceFieldGlass.asElement())
                .addElement('Q', ofBlock(GregTechAPI.sBlockCasingsDyson, 9))
                .addElement('R', Casings.QuantumGlass.asElement())
                .addElement('S', ofBlock(TstBlocks.MetaBlockCasing01, 1))
                .addElement(
                    'T',
                    HatchElementBuilder.<MM_BottledChaos>builder()
                        .anyOf(OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                        .casingIndex(TstBlocks.MetaBlockCasing02.getTextureIndex(2))
                        .hint(1)
                        .buildAndChain(TstBlocks.MetaBlockCasing02, 2))
                .addElement(
                    'X',
                    HatchElementBuilder.<MM_BottledChaos>builder()
                        .anyOf(ParallelController, SpeedController, PowerConsumptionController, OverclockController)
                        .casingIndex(TstBlocks.MetaBlockCasing02.getTextureIndex(2))
                        .hint(5)
                        .buildAndChain(TstBlocks.MetaBlockCasing02, 2));
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                for (int kind = 0; kind < 3; kind++) {
                    builder.addElement(threadSymbol(slot, kind), threadElement(slot, kind));
                }
            }
            structureDefinition = builder.build();
        }
        return structureDefinition;
    }

    private static IStructureElement<MM_BottledChaos> threadElement(int slot, int kind) {
        HatchElementBuilder<MM_BottledChaos> builder = HatchElementBuilder.<MM_BottledChaos>builder();
        if (kind == 0) {
            builder.hatchClass(TST_BottledChaosRecipeTokenHatch.class)
                .description(() -> "bottled_chaos.hatch.recipe_token");
        } else if (kind == 1) {
            builder.anyOf(InputBus);
        } else {
            builder.anyOf(InputHatch);
        }
        return builder.adder((machine, tile, texture) -> machine.addThreadHatch(slot, kind, tile, texture))
            .casingIndex(TstBlocks.MetaBlockCasing02.getTextureIndex(2))
            .hint(2 + kind)
            .buildAndChain(TstBlocks.MetaBlockCasing02, 2);
    }

    private static IStructureElement<MM_BottledChaos> capacityElement(int region) {
        return ofChain(
            onElementPass(
                machine -> machine.capacityTierCounts[region][4]++,
                ofBlockUnlocalizedName("gtnhintergalactic", "gt.blockcasingsSEMotor", 4)),
            onElementPass(machine -> machine.capacityTierCounts[region][0]++, isAir()));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        // Replace absolute EU/t with per-credential allocation percentages.
        tag.removeTag("energyUsage");
        tag.setLong(WAILA_CAPACITY, capacity);
        tag.setString(WAILA_RECIPE_CATEGORIES, getRecipeCategoryKeyList());
        tag.setString(WAILA_RECIPE_TOKENS, getRecipeTokenKeyList());
        tag.setBoolean("bottledChaosOverCapacity", overCapacity);
        NBTTagList jobs = new NBTTagList();
        for (int i = 0; i < THREAD_COUNT; i++) {
            if (installedCredentials[i] == null && !threads[i].isBusy()) continue;
            BottledChaosThread thread = threads[i];
            NBTTagCompound job = new NBTTagCompound();
            job.setInteger("slot", i + 1);
            job.setLong("share", allocatedShares[i]);
            job.setInteger("progress", thread.progress);
            job.setInteger("duration", thread.duration);
            ItemStack family = ItemBottledChaosCredential.getRecipeFamily(installedCredentials[i]);
            job.setString(
                "family",
                family == null ? "bottled_chaos.unbound" : family.getUnlocalizedName() + "@" + family.getItemDamage());
            job.setString(
                "status",
                overCapacity ? "bottled_chaos.capacity_exceeded"
                    : !mMachine || !getBaseMetaTileEntity().isAllowedToWork() ? "bottled_chaos.paused" : thread.status);
            jobs.appendTag(job);
        }
        tag.setTag(THREAD_NBT, jobs);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currentTip.remove(
            GTWaila.getMachineProgressString(
                tag.getBoolean("isActive"),
                tag.getBoolean("isAllowedToWork"),
                tag.getInteger("maxProgress"),
                tag.getInteger("progress")));
        currentTip.add("bottled_chaos.capacity: " + tag.getLong(WAILA_CAPACITY));
        currentTip.add("bottled_chaos.allowed_recipe_categories: " + tag.getString(WAILA_RECIPE_CATEGORIES));
        currentTip.add("bottled_chaos.allowed_recipe_families: " + tag.getString(WAILA_RECIPE_TOKENS));
        if (tag.getBoolean("bottledChaosOverCapacity")) currentTip.add("bottled_chaos.capacity_exceeded");
        NBTTagList jobs = tag.getTagList(THREAD_NBT, 10);
        for (int i = 0; i < Math.min(THREAD_COUNT, jobs.tagCount()); i++) {
            NBTTagCompound job = jobs.getCompoundTagAt(i);
            double percent = tag.getLong(WAILA_CAPACITY) > 0
                ? job.getLong("share") * 100.0 / tag.getLong(WAILA_CAPACITY)
                : 0;
            currentTip.add(
                "#" + job.getInteger("slot")
                    + " "
                    + job.getString("family")
                    + " | bottled_chaos.input_share: "
                    + String.format(Locale.ROOT, "%.2f%%", percent)
                    + " | "
                    + job.getString("status"));
            if (job.getInteger("duration") > 0) {
                currentTip
                    .add(GTWaila.getMachineProgressString(job.getInteger("duration"), job.getInteger("progress")));
            }
        }
    }

    private String getRecipeCategoryKeyList() {
        long mask = getInstalledRecipeCategoryMask();
        StringJoiner joiner = new StringJoiner(", ");
        for (BottledChaosRecipeCategory category : BottledChaosRecipeCategory.values()) {
            if ((mask & category.getMask()) != 0) joiner.add(category.getKey());
        }
        return joiner.length() == 0 ? "bottled_chaos.none" : joiner.toString();
    }

    private String getRecipeTokenKeyList() {
        StringJoiner joiner = new StringJoiner(", ");
        for (ItemStack credential : installedCredentials) {
            ItemStack token = ItemBottledChaosCredential.getRecipeFamily(credential);
            if (token != null) joiner.add(token.getUnlocalizedName() + "@" + token.getItemDamage());
        }
        return joiner.length() == 0 ? "bottled_chaos.none" : joiner.toString();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        if (tooltip == null) {
            tooltip = new MultiblockTooltipBuilder().addMachineType("bottled_chaos.machine_type")
                .addInfo("bottled_chaos.tooltip.0")
                .addInfo("bottled_chaos.tooltip.1")
                .addSeparator()
                .beginStructureBlock(29, 40, 29, false)
                .addStructureInfo("bottled_chaos.structure.body")
                .addStructureInfo("bottled_chaos.structure.capacity_columns")
                .addStructureInfo("bottled_chaos.structure.processing_groups")
                .addStructureInfo("bottled_chaos.structure.parameter_modules")
                .toolTipFinisher("bottled_chaos.mod_name");
        }
        return tooltip;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstone) {
        ITexture baseTexture = Textures.BlockIcons
            .getCasingTextureForId(TstBlocks.MetaBlockCasing02.getTextureIndex(2));
        if (side == facing) {
            if (active) return new ITexture[] { baseTexture, TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_ON)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FUSION1_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { baseTexture, TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }
        return new ITexture[] { baseTexture };
    }
}
