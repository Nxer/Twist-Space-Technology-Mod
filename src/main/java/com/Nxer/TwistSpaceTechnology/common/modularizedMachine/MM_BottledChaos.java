package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.OverclockController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.ParallelController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.PowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.SpeedController;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.defer;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_BottledChaosRecipeTokenHatch;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineSupportAllModuleBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosOutputBuffer;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosPowerAllocator;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosThread;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosUpkeep;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ModularHatchBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementCheckOnly;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.structure.error.TranslatableText;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MM_BottledChaos extends ModularizedMachineSupportAllModuleBase<MM_BottledChaos> {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int HORIZONTAL_OFFSET = 14;
    private static final int VERTICAL_OFFSET = 35;
    private static final int DEPTH_OFFSET = 12;
    private static final int CAPACITY_REGION_COUNT = 4;
    private static final int CAPACITY_REGION_BLOCK_COUNT = 64;
    private static final int CAPACITY_LEVEL_COUNT = 5;
    private static final int OUTPUT_RETRY_TICKS = 20;
    private static final int CREDENTIAL_INJECTION_TARGET_SLOT = 0;
    private static final int CREDENTIAL_INJECTION_SOURCE_SLOT_START = 1;
    private static final int CREDENTIAL_INJECTION_OUTPUT_SLOT = 9;
    private static final int CREDENTIAL_INJECTION_ITEM_SLOT_COUNT = 11;
    private static final String CREDENTIAL_INJECTION_ITEM_NBT = "bottledChaosCredentialInjectionItem";
    private static final String WAILA_CAPACITY = "bottledChaosCapacity";
    private static final String WAILA_USED_CAPACITY = "bottledChaosUsedCapacity";
    private static final String WAILA_CREDENTIAL_HATCHES = "bottledChaosCredentialHatches";
    private static final String WAILA_CREDENTIAL_HATCH_LIMIT = "bottledChaosCredentialHatchLimit";
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

    // #tr tst.bottledchaos.result.credential_changed
    // # The credential for this processing group changed.
    // #zh_CN 此加工组的凭证已变更.

    // #tr tst.bottledchaos.result.insufficient_power_share
    // # This processing group does not have enough allocated power.
    // #zh_CN 此加工组分配到的电力不足.

    // #tr tst.bottledchaos.result.capacity_exceeded
    // # Installed credentials exceed the machine capacity.
    // #zh_CN 已安装凭证超过主机容量.

    // #tr tst.bottledchaos.result.unbound
    // # This processing group has no valid credential.
    // #zh_CN 此加工组没有有效凭证.

    // #tr tst.bottledchaos.structure.invalid_capacity_region
    // # Each capacity region must contain only non-capacity blocks (including air), or 64 same-tier capacity blocks.
    // #zh_CN 每个容量区必须全为非容量方块(含空气), 或完整安装64个同级容量方块.

    // #tr tst.bottledchaos.structure.multiple_parallel_controllers
    // # Install at most one parallel controller module.
    // #zh_CN 最多只能安装一个并行控制器模块.

    // #tr tst.bottledchaos.structure.incomplete_processing_group
    // # Dedicated input positions require a credential hatch; both inputs are optional and accept buses or hatches.
    // #zh_CN 专用输入位需要对应的凭证仓; 两个输入位均可留空, 或安装物品输入总线或流体输入仓.

    // #tr tst.bottledchaos.capacity_usage
    // # Capacity: %s / %s
    // #zh_CN 容量: %s / %s

    // #tr tst.bottledchaos.credential_hatches
    // # Credential hatches: %s / %s
    // #zh_CN 凭证仓: %s / %s

    // #tr tst.bottledchaos.allowed_recipe_categories
    // # Installed machine types: %s
    // #zh_CN 已安装机器类型: %s

    // #tr tst.bottledchaos.allowed_recipe_families
    // # Installed credential families: %s
    // #zh_CN 已安装凭证类型: %s

    // #tr tst.bottledchaos.input_share
    // # Power share: %s
    // #zh_CN 电力份额: %s

    // #tr tst.bottledchaos.paused
    // # Paused
    // #zh_CN 已暂停

    // #tr tst.bottledchaos.capacity_exceeded
    // # Capacity exceeded
    // #zh_CN 容量超限

    // #tr tst.bottledchaos.unbound
    // # No credential
    // #zh_CN 无凭证

    // #tr tst.bottledchaos.tooltip.machine_type
    // # Production Chain Processor
    // #zh_CN 生产链处理器

    // #tr tst.bottledchaos.tooltip.info.01
    // # Runs multiple processing chains with independent progress.
    // #zh_CN 以独立进度同时运行多条生产链.

    // #tr tst.bottledchaos.tooltip.info.02
    // # Shared power is drained once per tick; insufficient power pauses every processing group.
    // #zh_CN 每tick统一扣除总耗电; 电力不足时全部加工组暂停.

    // #tr tst.bottledchaos.structure.body
    // # The main body uses the blocks shown in the structure projection.
    // #zh_CN 主体使用结构投影中显示的方块.

    // #tr tst.bottledchaos.structure.capacity_columns
    // # Each capacity region provides 1 with non-capacity blocks (including air), or 4/16/64/256/1024 with 64 same-tier capacity blocks.
    // #zh_CN 每个容量区全为非容量方块(含空气)时提供1容量; 完整安装64个同级容量方块时提供4/16/64/256/1024容量.

    // #tr tst.bottledchaos.structure.voltage_upgrades
    // # Matching field and energy generators limit recipes to UHV, UEV, UIV, UMV, or UXV/MAX.
    // #zh_CN 同级力场与能量发生器分别限制配方为UHV, UEV, UIV, UMV或UXV/MAX.

    // #tr tst.bottledchaos.structure.upgrade_glass
    // # Upgraded controller glass and immediate capacity-column glass must match the generator voltage.
    // #zh_CN 升级后的主轴玻璃及紧贴容量柱的玻璃必须与发生器电压一致.

    // #tr tst.bottledchaos.structure.processing_groups
    // # Up to 15 processing groups may be installed in any group positions allowed by the parallel module.
    // #zh_CN 最多可在任意加工组位置安装15组设备, 实际数量由并行模块限制.

    // #tr tst.bottledchaos.structure.shared_inputs
    // # Input buses and hatches in the central chamber are shared by every installed processing group.
    // #zh_CN 中心仓室中的输入总线和输入仓由所有已安装加工组共享.

    // #tr tst.bottledchaos.structure.parameter_modules
    // # Install at most one parallel module; speed, power, and overclock modules affect final processing.
    // #zh_CN 最多安装一个并行模块; 速度, 耗电和超频模块作用于最终加工.

    // a marks the immediate capacity enclosures and controller-shaft glass; A remains outer glass.
    private static final String[][] SHAPE = {
        {"                             ","                             ","                             ","     JJJJJJ       JJJJJJ     ","    J                   J    ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","   J                     J   ","    J                   J    ","     JJJJJJ       JJJJJJ     ","                             ","                             ","                             "},
        {"                             ","                             ","          J       J          ","     DDDDDDDDDDDDDDDDDDD     ","    D     J       J     D    ","   D      J       J      D   ","   D      J       J      D   ","   D      J       J      D   ","   D     J         J     D   ","   D    J           J    D   ","  JDJJJJ             JJJJDJ  ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","   D                     D   ","  JDJJJJ             JJJJDJ  ","   D    J           J    D   ","   D     J         J     D   ","   D      J       J      D   ","   D      J       J      D   ","   D      J       J      D   ","    D     J       J     D    ","     DDDDDDDDDDDDDDDDDDD     ","          J       J          ","                             ","                             "},
        {"                             ","          JJJJJJJJJ          ","     EEEEEEEEEEEEEEEEEEE     ","    DCCCCCCDDDDDDDCCCCCCD    ","   DCFFFFFCDDDDDDDCFFFFFCD   ","  ECFFFFFFCFFFFFFFCFFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFCDDAAAAADDCFFFFFCE  ","  ECFFFFCDAAAAAAAAADCFFFFCE  "," JECCCCCDAAAAAAAAAAADCCCCCEJ "," JEDDFDDDAAAAAAAAAAADDDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDAAAAAAAAAAAAADDFDDEJ "," JEDDFDDDAAAAAAAAAAADDDFDDEJ "," JECCCCCDAAAAAAAAAAADCCCCCEJ ","  ECFFFFCDAAAAAAAAADCFFFFCE  ","  ECFFFFFCDDAAAAADDCFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFFCDDDDDDDCFFFFFFCE  ","  ECFFFFFFCFFFFFFFCFFFFFFCE  ","   DCFFFFFCDDDDDDDCFFFFFCD   ","    DCCCCCCDDDDDDDCCCCCCD    ","     EEEEEEEEEEEEEEEEEEE     ","          JJJJJJJJJ          ","                             "},
        {"                             ","          DDDDDDDDD          ","    EDDDDDDEEEEEEEDDDDDDE    ","   ECRQQQQQCCCCCCCQQQQQRCE   ","  ECRQ    QI     IQ    QRCE  ","  DRQ     Q       Q     QRD  ","  DQ      Q       Q      QD  ","  DQ      Q       Q      QD  ","  DQ     Q         Q     QD  ","  DQ    Q           Q    QD  "," DDQQQQQ             QQQQQDD "," DECI                   ICED "," DEC                     CED "," DEC                     CED "," DEC                     CED "," DEC                     CED "," DEC                     CED "," DECI                   ICED "," DDQQQQQ             QQQQQDD ","  DQ    Q           Q    QD  ","  DQ     Q         Q     QD  ","  DQ      Q       Q      QD  ","  DQ      Q       Q      QD  ","  DRQ     Q       Q     QRD  ","  ECRQ    QI     IQ    QRCE  ","   ECRQQQQQCCCCCCCQQQQQRCE   ","    EDDDDDDEEEEEEEDDDDDDE    ","          DDDDDDDDD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDEEEEEEEDDDDDDDE   ","  ECRRQQQQQCDDDDDCQQQQQRRCE  ","  DRRQ    QI     IQ    QRRD  ","  DRQ     Q       Q     QRD  ","  DQ      Q       Q      QD  ","  DQ      Q       Q      QD  ","  DQ     Q         Q     QD  ","  DQ    Q           Q    QD  "," DDQQQQQ             QQQQQDD "," AECI                   ICEA "," AED                     DEA "," AED                     DEA "," AED                     DEA "," AED                     DEA "," AED                     DEA "," AECI                   ICEA "," DDQQQQQ             QQQQQDD ","  DQ    Q           Q    QD  ","  DQ     Q         Q     QD  ","  DQ      Q       Q      QD  ","  DQ      Q       Q      QD  ","  DRQ     Q       Q     QRD  ","  DRRQ    QI     IQ    QRRD  ","  ECRRQQQQQCDDDDDCQQQQQRRCE  ","   EDDDDDDDEEEEEEEDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQJJQQCJJJJJCQQJJQRRCE  ","  DRRQ    QI     IQ    QRRD  ","  DRQ     J       J     QRD  ","  DQ      J       J      QD  ","  DJ      J       J      JD  ","  DJ     J         J     JD  ","  DQ    J           J    QD  "," DDQQJJJ             JJJQQDD "," AKCI                   ICKA "," A J                     J A "," A J                     J A "," A J                     J A "," A J                     J A "," A J                     J A "," AKCI                   ICKA "," DDQQJJJ             JJJQQDD ","  DQ    J           J    QD  ","  DJ     J         J     JD  ","  DJ      J       J      JD  ","  DQ      J       J      QD  ","  DRQ     J       J     QRD  ","  DRRQ    QI     IQ    QRRD  ","  ECRRQJJQQCJJJJJCQQJJQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQCDDDDDCQQQQQRRCE  ","  DRRQ    QI     IQ    QRRD  ","  DRQ     Q       Q     QRD  ","  DQ      Q       Q      QD  ","  DQ      Q       Q      QD  ","  DQ     Q         Q     QD  ","  DQ    Q           Q    QD  "," DDQQQQQ             QQQQQDD "," AKCI                   ICKA "," A D                     D A "," A D                     D A "," A D                     D A "," A D                     D A "," A D                     D A "," AKCI                   ICKA "," DDQQQQQ             QQQQQDD ","  DQ    Q           Q    QD  ","  DQ     Q         Q     QD  ","  DQ      Q       Q      QD  ","  DQ      Q       Q      QD  ","  DRQ     Q       Q     QRD  ","  DRRQ    QI     IQ    QRRD  ","  ECRRQQQQQCDDDDDCQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQCSSSSSCQQQQQRRCE  ","  DRRQ    QI     IQ    QRRD  ","  DRQ JJJ Q       Q JJJ QRD  ","  DQ JEEJ Q       Q JEEJ QD  ","  DQ JEEJ Q       Q JEEJ QD  ","  DQ JJJ Q         Q JJJ QD  ","  DQ    Q           Q    QD  "," DDQQQQQ             QQQQQDD "," AKCI                   ICKA "," A S                     S A "," A S                     S A "," A S                     S A "," A S                     S A "," A S                     S A "," AKCI                   ICKA "," DDQQQQQ             QQQQQDD ","  DQ    Q           Q    QD  ","  DQ JJJ Q         Q JJJ QD  ","  DQ JEEJ Q       Q JEEJ QD  ","  DQ JEEJ Q       Q JEEJ QD  ","  DRQ JJJ Q       Q JJJ QRD  ","  DRRQ    QI     IQ    QRRD  ","  ECRRQQQQQCSSSSSCQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRREEEEQCEEEEECQEEEERRCE  ","  DRRE    EE     EE    ERRD  ","  DRE EEE E       E EEE ERD  ","  DE ECCE E       E ECCE ED  ","  DE ECCE E       E ECCE ED  ","  DE EEE E         E EEE ED  ","  DE    E           E    ED  "," DDQEEEE             EEEEQDD "," AKCE                   ECKA "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," AKCE                   ECKA "," DDQEEEE             EEEEQDD ","  DE    E           E    ED  ","  DE EEE E         E EEE ED  ","  DE ECCE E       E ECCE ED  ","  DE ECCE E       E ECCE ED  ","  DRE EEE E       E EEE ERD  ","  DRRE    EE     EE    ERRD  ","  ECRREEEEQCEEEEECQEEEERRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRCCCEQCCCCCCCQECCCRRCE  ","  DRRC    EC     CE    CRRD  ","  DRC aaa C       C aaa CRD  ","  DC aGGa C       C aGGa CD  ","  DC aGGa C       C aGGa CD  ","  DC aaa C         C aaa CD  ","  DE    C           C    ED  "," DDQECCC             CCCEQDD "," AKCC                   CCKA "," A C                     C A "," A C                     C A "," A C                     C A "," A C                     C A "," A C                     C A "," AKCC                   CCKA "," DDQECCC             CCCEQDD ","  DE    C           C    ED  ","  DC aaa C         C aaa CD  ","  DC aGGa C       C aGGa CD  ","  DC aGGa C       C aGGa CD  ","  DRC aaa C       C aaa CRD  ","  DRRC    EC     CE    CRRD  ","  ECRRCCCEQCCCCCCCQECCCRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRREEEEQCEEEEECQEEEERRCE  ","  DRRE    EE     EE    ERRD  ","  DRE aaa E       E aaa ERD  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DE aaa E         E aaa ED  ","  DE    E           E    ED  "," DDQEEEE             EEEEQDD "," AKCE                   ECKA "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," A E                     E A "," AKCE                   ECKA "," DDQEEEE             EEEEQDD ","  DE    E           E    ED  ","  DE aaa E         E aaa ED  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DRE aaa E       E aaa ERD  ","  DRRE    EE     EE    ERRD  ","  ECRREEEEQCEEEEECQEEEERRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDK     KDDDDDDDE   ","  ECRRQQQQQC     CQQQQQRRCE  ","  DRRQ    AI     IA    QRRD  ","  DRQ aaa A       A aaa QRD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aaa A         A aaa QD  ","  DQ    A           A    QD  "," DDQAAAA             AAAAQDD "," AKCI                   ICKA "," A                         A "," A                         A "," A                         A "," A                         A "," A                         A "," AKCI                   ICKA "," DDQAAAA             AAAAQDD ","  DQ    A           A    QD  ","  DQ aaa A         A aaa QD  ","  DQ aGGa A       A aGGa QD  ","  DQ aGGa A       A aGGa QD  ","  DRQ aaa A       A aaa QRD  ","  DRRQ    AI     IA    QRRD  ","  ECRRQQQQQC     CQQQQQRRCE  ","   EDDDDDDDK     KDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DAAAAAAAD          ","   EDDDDDDDEEEEEEEDDDDDDDE   ","  ECRREEEEQC     CQEEEERRCE  ","  DRRE    ED     DE    ERRD  ","  DRE aaa E       E aaa ERD  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DE aaa E         E aaa ED  ","  DE    E           E    ED  "," DDQEEEE             EEEEQDD "," AECD                   DCEA "," AE                       EA "," AE                       EA "," AE                       EA "," AE                       EA "," AE                       EA "," AECD                   DCEA "," DDQEEEE             EEEEQDD ","  DE    E           E    ED  ","  DE aaa E         E aaa ED  ","  DE aGGa E       E aGGa ED  ","  DE aGGa E       E aGGa ED  ","  DRE aaa E       E aaa ERD  ","  DRRE    ED     DE    ERRD  ","  ECRREEEEQC     CQEEEERRCE  ","   EDDDDDDDEEEEEEEDDDDDDDE   ","          DAAAAAAAD          ","                             "},
        {"                             ","          DDDDDDDDD          ","    EDDDDDDEEEEEEEDDDDDDE    ","   ECREEEEQCDDDDDCQEEEERCE   ","  ECREJJJJED     DEJJJJERCE  ","  DREJRRRJE       EJRRRJERD  ","  DEJRPPRJE       EJRPPRJED  ","  DEJRPPRJE       EJRPPRJED  ","  DEJRRRJE         EJRRRJED  ","  DEJJJJE           EJJJJED  "," DDQEEEE             EEEEQDD "," DECD                   DCED "," DED                     DED "," DED                     DED "," DED                     DED "," DED                     DED "," DED                     DED "," DECD                   DCED "," DDQEEEE             EEEEQDD ","  DEJJJJE           EJJJJED  ","  DEJRRRJE         EJRRRJED  ","  DEJRPPRJE       EJRPPRJED  ","  DEJRPPRJE       EJRPPRJED  ","  DREJRRRJE       EJRRRJERD  ","  ECREJJJJED     DEJJJJERCE  ","   ECREEEEQCDDDDDCQEEEERCE   ","    EDDDDDDEEEEEEEDDDDDDE    ","          DDDDDDDDD          ","                             "},
        {"                             ","          D       D          ","     EEEEEEEEEEEEEEEEEEE     ","    ECCCCCCSSSSSSSCCCCCCE    ","   ESSSSSSSSSSSSSSSSSSSSSE   ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","  ECSSPPSSSSSSSSSSSSSPPSSCE  ","  ECSSPPSSSSSSSSSSSSSPPSSCE  ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","  ECSSSSSSSSAAAAASSSSSSSSCE  "," DECSSSSSSSAAAAAAASSSSSSSCED ","  ESSSSSSSAAAAAAAAASSSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSSAAAAAAAAASSSSSSSE  "," DECSSSSSSSAAAAAAASSSSSSSCED ","  ECSSSSSSSSAAAAASSSSSSSSCE  ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","  ECSSPPSSSSSSSSSSSSSPPSSCE  ","  ECSSPPSSSSSSSSSSSSSPPSSCE  ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","   ESSSSSSSSSSSSSSSSSSSSSE   ","    ECCCCCCSSSSSSSCCCCCCE    ","     EEEEEEEEEEEEEEEEEEE     ","          D       D          ","                             "},
        {"                             ","          D       D          ","          DDDDDDDDD          ","         DD       DD         ","       DD           DD       ","     DDO             ODD     ","     DOBO           OBOD     ","    DOBBO           OBBOD    ","    D OO             OO D    ","   D                     D   "," DDD                     DDD ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  "," DDD                     DDD ","   D                     D   ","    D OO             OO D    ","    DOBBO           OBBOD    ","     DOBO           OBOD     ","     DDO             ODD     ","       DD           DD       ","         DD       DD         ","          DDDDDDDDD          ","          D       D          ","                             "},
        {"                             ","                             ","           DDDDDDD           ","         DD       DD         ","       DD           DD       ","     DDO             ODD     ","     DOBO           OBOD     ","    DOBBO           OBBOD    ","    D OO             OO D    ","   D                     D   ","   D                     D   ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","  D                       D  ","   D                     D   ","   D                     D   ","    D OO             OO D    ","    DOBBO           OBBOD    ","     DOBO           OBOD     ","     DDO             ODD     ","       DD           DD       ","         DD       DD         ","           DDDDDDD           ","                             ","                             "},
        {"                             ","                             ","           DDDDDDD           ","         DD       DD         ","       DD           DD       ","     DDO             ODD     ","     DOBO           OBOD     ","    DOBBO           OBBOD    ","    D OOO           OOO D    ","   D                     D   ","   D                     D   ","  D                       D  ","  D                       D  ","  D          QQQ          D  ","  D          QQQ          D  ","  D          QQQ          D  ","  D                       D  ","  D                       D  ","   D                     D   ","   D                     D   ","    D OOO           OOO D    ","    DOBBO           OBBOD    ","     DOBO           OBOD     ","     DDO             ODD     ","       DD           DD       ","         DD       DD         ","           DDDDDDD           ","                             ","                             "},
        {"                             ","                             ","                             ","           EEEEEEE           ","         EE       EE         ","       EE           EE       ","      EOO           OOE      ","     EOBBO         OBBOE     ","     EOBBO         OBBOE     ","    E  OO           OO  E    ","    E                   E    ","   EE                   EE   ","   E         QQQ         E   ","   E        Q   Q        E   ","   E        Q L Q        E   ","   E        Q   Q        E   ","   E         QQQ         E   ","   EE                   EE   ","    E                   E    ","    E  OO           OO  E    ","     EOBBO         OBBOE     ","     EOBBO         OBBOE     ","      EOO           OOE      ","       EE           EE       ","         EEE     EEE         ","           EEEEEEE           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           SE   ES           ","         SS       SS         ","       SS           SS       ","      SOO           OOS      ","     SOBBO         OBBOS     ","     SOBBO         OBBOS     ","    S  OO           OO  S    ","    S                   S    ","   SE                   ES   ","   SE        aaa        ES   ","   SE       a   a       ES   ","   SE       a L a       ES   ","   SE       a   a       ES   ","   SE        aaa        ES   ","   SE                   ES   ","    S                   S    ","    S  OO           OO  S    ","     SOBBO         OBBOS     ","     SOBBO         OBBOS     ","      SOO           OOS      ","       SS           SS       ","         SSEEEEEEESS         ","           SSSSSSS           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           SE   ES           ","         SS       SS         ","       SS           SS       ","      SOO           OOS      ","     SOBBO         OBBOS     ","     SOBBO         OBBOS     ","    S  OO           OO  S    ","    S                   S    ","   SE                   ES   ","   SV        aaa        VS   ","   SV       a   a       VS   ","   SV       a U a       VS   ","   SV       a   a       VS   ","   SV        aaa        VS   ","   SE                   ES   ","    S                   S    ","    S  OO           OO  S    ","     SOBBO         OBBOS     ","     SOBBO         OBBOS     ","      SOO           OOS      ","       SS           SS       ","         SSEVVVVVESS         ","           SSSSSSS           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           SE   ES           ","         SS       SS         ","       SS           SS       ","      SOO           OOS      ","     SOBBO         OBBOS     ","     SOBBO         OBBOS     ","    S  OO           OO  S    ","    S                   S    ","   SE                   ES   ","   SW        aaa        WS   ","   SW       a   a       WS   ","   SW       a L a       WS   ","   SW       a   a       WS   ","   SW        aaa        WS   ","   SE                   ES   ","    S                   S    ","    S  OO           OO  S    ","     SOBBO         OBBOS     ","     SOBBO         OBBOS     ","      SOO           OOS      ","       SS           SS       ","         SSEWWWWWESS         ","           SSSSSSS           ","                             ","                             ","                             "},
        {"                             ","                             ","                             ","           EE   EE           ","         EE       EE         ","       EE           EE       ","      EOO           OOE      ","     EOBBO         OBBOE     ","     EOBBO         OBBOE     ","    E  OO           OO  E    ","    E                   E    ","   EE                   EE   ","   EX        QQQ        XE   ","   EX       Q   Q       XE   ","   EX       Q L Q       XE   ","   EX       Q   Q       XE   ","   EX        QQQ        XE   ","   EE                   EE   ","    E                   E    ","    E  OO           OO  E    ","     EOBBO         OBBOE     ","     EOBBO         OBBOE     ","      EOO           OOE      ","       EE           EE       ","         EEEXXXXXEEE         ","           EEEEEEE           ","                             ","                             ","                             "},
        {"                             ","                             ","           DDDDDDD           ","         DDCCEEECCDD         ","       DDCCDDDDDDDCCDD       ","     DDCCDDD     DDDCCDD     ","     DCOODDJ     JDDOOCD     ","    DCOBBOJ       JOBBOCD    ","    DCOBBOJ       JOBBOCD    ","   DCDDOOJ         JOODDCD   ","   DCDDJJ           JJDDCD   ","  DCDDJ               JDDCD  ","  DCL       YY~YY       LCD  ","  DCL       YQQQY       LCD  ","  DCL       YQLQY       LCD  ","  DCL       YQQQY       LCD  ","  DCL       YYYYY       LCD  ","  DCDDJ               JDDCD  ","   DCDDJJ           JJDDCD   ","   DCDDOOJ         JOODDCD   ","    DCOBBOJ       JOBBOCD    ","    DCOBBOJ       JOBBOCD    ","     DCOODDJ     JDDOOCD     ","     DDCCDDD     DDDCCDD     ","       DDCCDLLLLLDCCDD       ","         DDCCCCCCCDD         ","           DDDDDDD           ","                             ","                             "},
        {"          M       M          ","   MMMMMMMMMMMMMMMMMMMMMMM   ","  MMMMMMMMMMMMMMMMMMMMMMMMM  "," MMMMMMMMMMSSSSSSSMMMMMMMMMM "," MMMMMMMMSCHDDDDDHSSMMMMMMMM "," MMMMMMSSHHHDDDDDHHHSSMMMMMM "," MMMMMSOOHHEJDDDJEHHOOSMMMMM "," MMMMSOBBOE JDDDJ EOBBOSMMMM "," MMMMSOBBOE JDDDJ EOBBOSMMMM "," MMMSHHOOO  JDDDJ  OOOHHSMMM ","MMMMSHHEE  JDDDDDJ  EEHHCMMMM"," MMSHHE   JDDHHHDDJ   EHHSMM "," MMSHDJJJJDDHHHHHDDJJJJDHSMM "," MMSHDDDDDDHHHHHHHDDDDDDHSMM "," MMSHHHHHHHHHHNHHHHHHHHHHSMM "," MMSHDDDDDDHHHHHHHDDDDDDHSMM "," MMSHDJJJJDDHHHHHDDJJJJDHSMM "," MMSHHE   JDDHHHDDJ   EHHSMM ","MMMMCHHEE  JDDHDDJ  EEHHSMMMM"," MMMSHHOOO  JDHDJ  OOOHHSMMM "," MMMMSOBBOE JDHDJ EOBBOSMMMM "," MMMMSOBBOE JDHDJ EOBBOSMMMM "," MMMMMSOOHHEJDHDJEHHOOSMMMMM "," MMMMMMSSHHHDDHDDHHHSSMMMMMM "," MMMMMMMMSSHHHHHHHCSMMMMMMMM "," MMMMMMMMMMSSSSSSSMMMMMMMMMM ","  MMMMMMMMMMMMMMMMMMMMMMMMM  ","   MMMMMMMMMMMMMMMMMMMMMMM   ","          M       M          "},
        {"          M       M          ","   MMMMMMMMMMMMMMMMMMMMMMM   ","  MMMMMMMMMMMMMMMMMMMMMMMMM  "," MMMMMMMMMMSSSSSSSMMMMMMMMMM "," MMMMMMMMSSMMMMMMMSSMMMMMMMM "," MMMMMMSSMMMMMMMMMMMSSMMMMMM "," MMMMMSOOMMDDDDDDDMMOOSMMMMM "," MMMMSOBBODDDDDDDDDOBBOSMMMM "," MMMMSOBBODDDDDDDDDOBBOSMMMM "," MMMSMMOOOODDDDDDDDOOOMMSMMM ","MMMMSMMDDDOOOCCCCDOODDMMSMMMM"," MMSMMDDDDDCOCCCCCODDDDMMSMM "," MMSMMDODDCCOOOCOOODDODMMSMM "," MMSMMDODDCCCCOCOCCDDODMMSMM "," MMSMMDODDCCOONOOCCDDODMMSMM "," MMSMMDODDCCOCOCCCCDDODMMSMM "," MMSMMDODDOOOCOOOCCDDODMMSMM "," MMSMMDDDDOCCCCCOCDDDDDMMSMM ","MMMMSMMDDOODCCCCOOODDDMMSMMMM"," MMMSMMOOODDDDDDDDOOOOMMSMMM "," MMMMSOBBODDDDDDDDDOBBOSMMMM "," MMMMSOBBODDOOOOODDOBBOSMMMM "," MMMMMSOOMMDDDDDDDMMOOSMMMMM "," MMMMMMSSMMMMMMMMMMMSSMMMMMM "," MMMMMMMMSSMMMMMMMSSMMMMMMMM "," MMMMMMMMMMSSSSSSSMMMMMMMMMM ","  MMMMMMMMMMMMMMMMMMMMMMMMM  ","   MMMMMMMMMMMMMMMMMMMMMMM   ","          M       M          "},
        {"   MMMMMMMMMMMMMMMMMMMMMMM   ","  MMMMMMMMMMMMMMMMMMMMMMMMM  "," MMMMMMMMMMMMMMMMMMMMMMMMMMM ","MMMMMMMMMMMSSSSSSSMMMMMMMMMMM","MMMMMMMMMSSMMMMMMMSSMMMMMMMMM","MMMMMMMSSMMMMMMMMMMMSSMMMMMMM","MMMMMMSOOMMMMMMMMMMMOOSMMMMMM","MMMMMSOBBOMMMMMMMMMOBBOSMMMMM","MMMMMSOBBOMMMMMMMMOBBBOSMMMMM","MMMMSMMOBBBOOMMMMMOBOOMMSMMMM","MMMMSMMMOOBBBOCCCOBBMMMMSMMMM","MMMSMMMMMMOOBOOCOOBOMMMMMSMMM","MMMSMMMMMMCOBBBOBBBOMMMMMSMMM","MMMSMMMMMMCCOOBOBOOMMMMMMSMMM","MMMSMMMMMMCOBBTBBOCMMMMMMSMMM","MMMSMMMMMMOOBOBOOCCMMMMMMSMMM","MMMSMMMMMOBBBOBBBOCMMMMMMSMMM","MMMSMMMMMOBOOCOOBOOMMMMMMSMMM","MMMMSMMMMBBOCCCOBBBOOMMMSMMMM","MMMMSMMOOBOMMMMMOOBBBOMMSMMMM","MMMMMSOBBBOMMMMMMMMOBBOSMMMMM","MMMMMSOBBOMMMMMMMMMOBBOSMMMMM","MMMMMMSOOMMMMMMMMMMMOOSMMMMMM","MMMMMMMSSMMMMMMMMMMMSSMMMMMMM","MMMMMMMMMSSMMMMMMMSSMMMMMMMMM","MMMMMMMMMMMSSSSSSSMMMMMMMMMMM"," MMMMMMMMMMMMMMMMMMMMMMMMMMM ","  MMMMMMMMMMMMMMMMMMMMMMMMM  ","   MMMMMMMMMMMMMMMMMMMMMMM   "},
        {"   MMMMMMMMMMMMMMMMMMMMMMM   ","  MMMMMMMMMMMMMMMMMMMMMMMMM  "," MMMMMMMMMMMMMMMMMMMMMMMMMMM ","MMMMMMMMMMMSSSSSSSMMMMMMMMMMM","MMMMMMMMMSSSSSSSSSSSMMMMMMMMM","MMMMMMMSSSSSSSSSSSSSSSMMMMMMM","MMMMMMSSSSSSSSSSSSSSSSSMMMMMM","MMMMMSSOOSSSSSSSSSSSOOSSMMMMM","MMMMMSSOOSSSSSSSSSSOOOSSMMMMM","MMMMSSSSOOOSSSSSSSSOSSSSSMMMM","MMMMSSSSSSOOOCCCCSOOSSSSSMMMM","MMMSSSSSSSSCOCCCCCOSSSSSSSMMM","MMMSSSSSSSCCOOOCOOOSSSSSSSMMM","MMMSSSSSSSCCCCOCOCCSSSSSSSMMM","MMMSSSSSSSCCOODOOCCSSSSSSSMMM","MMMSSSSSSSCCOCOCCCCSSSSSSSMMM","MMMSSSSSSSOOOCOOOCCSSSSSSSMMM","MMMSSSSSSSOCCCCCOCSSSSSSSSMMM","MMMMSSSSSOOSCCCCOOOSSSSSSMMMM","MMMMSSSSSOSSSSSSSSOOOSSSSMMMM","MMMMMSSOOOSSSSSSSSSSOOSSMMMMM","MMMMMSSOOSSSSSSSSSSSOOSSMMMMM","MMMMMMSSSSSSSSSSSSSSSSSMMMMMM","MMMMMMMSSSSSSSSSSSSSSSMMMMMMM","MMMMMMMMMSSSSSSSSSSSMMMMMMMMM","MMMMMMMMMMMSSSSSSSMMMMMMMMMMM"," MMMMMMMMMMMMMMMMMMMMMMMMMMM ","  MMMMMMMMMMMMMMMMMMMMMMMMM  ","   MMMMMMMMMMMMMMMMMMMMMMM   "},
        {"   MMMMMMMMMMMMMMMMMMMMMMM   ","  MMMMMMMMMMMMMMMMMMMMMMMMM  "," MMMMMMMMMMMMMMMMMMMMMMMMMMM ","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMCCCCCMMMMMMMMMMMM","MMMMMMMMMMMCCZZZCCMMMMMMMMMMM","MMMMMMMMMMMCZZZZZCMMMMMMMMMMM","MMMMMMMMMMMCZZZZZCMMMMMMMMMMM","MMMMMMMMMMMCZZZZZCMMMMMMMMMMM","MMMMMMMMMMMCCZZZCCMMMMMMMMMMM","MMMMMMMMMMMMCCCCCMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM","MMMMMMMMMMMMMMMMMMMMMMMMMMMMM"," MMMMMMMMMMMMMMMMMMMMMMMMMMM ","  MMMMMMMMMMMMMMMMMMMMMMMMM  ","   MMMMMMMMMMMMMMMMMMMMMMM   "},
    };
    // spotless:on

    private static IStructureDefinition<MM_BottledChaos> structureDefinition;
    private static MultiblockTooltipBuilder tooltip;

    private final int[][] capacityTierCounts = new int[CAPACITY_REGION_COUNT][CAPACITY_LEVEL_COUNT];
    private final BottledChaosMachineConfiguration machineConfiguration = new BottledChaosMachineConfiguration(() -> {
        authorizationDirty = true;
        markDirty();
    });
    private final ItemStackHandler credentialInjectionInventory = new ItemStackHandler(
        CREDENTIAL_INJECTION_ITEM_SLOT_COUNT) {

        @Override
        public int getSlotLimit(int slot) {
            return slot >= CREDENTIAL_INJECTION_SOURCE_SLOT_START && slot < CREDENTIAL_INJECTION_OUTPUT_SLOT ? 64 : 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == CREDENTIAL_INJECTION_TARGET_SLOT) {
                return ItemBottledChaosCredential.getCapacity(stack) > 0
                    || ItemBottledChaosMachineModule.isModule(stack);
            }
            return slot < CREDENTIAL_INJECTION_OUTPUT_SLOT;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (credentialCrafting != null) credentialCrafting.invalidate();
            markDirty();
        }
    };
    private final BottledChaosCrafting credentialCrafting = new BottledChaosCrafting(
        credentialInjectionInventory,
        this::markDirty);
    private final TST_BottledChaosRecipeTokenHatch[] credentialHatches = new TST_BottledChaosRecipeTokenHatch[THREAD_COUNT];
    // Two optional, type-independent input positions per processing group.
    private final MTEHatch[] threadInputs = new MTEHatch[THREAD_COUNT * 2];
    private final BottledChaosThread[] threads = new BottledChaosThread[THREAD_COUNT];
    private final BottledChaosProcessingLogic[] threadLogics = new BottledChaosProcessingLogic[THREAD_COUNT];
    private final ItemStack[] installedCredentials = new ItemStack[THREAD_COUNT];
    private final int[] inputPeerMasks = new int[THREAD_COUNT];
    private final long[] credentialCosts = new long[THREAD_COUNT];
    private final CheckRecipeResult[] threadResults = new CheckRecipeResult[THREAD_COUNT];
    private CheckRecipeResult cacheExportResult = CheckRecipeResultRegistry.NONE;
    private long cacheExportResultExpiresAt;
    private int processingGroupMask;
    private final long[] powerBudgets = new long[THREAD_COUNT];
    private final boolean[] advancing = new boolean[THREAD_COUNT];
    private final BottledChaosUpkeep structureUpkeep = new BottledChaosUpkeep();
    private long[] allocatedShares = new long[THREAD_COUNT];
    private int generatorLevel = -1;
    private int minimumUpgradeGlassTier;
    private int maximumUpgradeGlassTier;
    private long capacity;
    private long allocatedCapacity = -1;
    private long inputPower = -1;
    private long poolMask;
    private long schedulerTick;
    private boolean authorizationDirty = true;
    private boolean inputGroupingDirty = true;
    private int validInputGroupMask;
    private int requestedExecutionSlot = -1;
    private boolean overCapacity;
    private boolean concentratedPower;
    private boolean activeConcentratedPower;
    private int powerRoundMask;
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
        batchMode = true;
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new BottledChaosThread();
            threadLogics[i] = new BottledChaosProcessingLogic();
            threadResults[i] = CheckRecipeResultRegistry.SUCCESSFUL;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        machineConfiguration.save(nbt);
        nbt.setTag("bottledChaosUpkeepRemainders", structureUpkeep.save());
        for (int slot = 0; slot < CREDENTIAL_INJECTION_ITEM_SLOT_COUNT; slot++) {
            GTUtility
                .saveItem(nbt, CREDENTIAL_INJECTION_ITEM_NBT + slot, credentialInjectionInventory.getStackInSlot(slot));
        }
        NBTTagList jobs = new NBTTagList();
        for (BottledChaosThread thread : threads) jobs.appendTag(thread.save());
        nbt.setTag(THREAD_NBT, jobs);
        nbt.setBoolean("bottledChaosConcentratedPower", concentratedPower);
        nbt.setBoolean("bottledChaosActiveConcentratedPower", activeConcentratedPower);
        nbt.setInteger("bottledChaosPowerRound", powerRoundMask);
        NBTTagCompound budgets = new NBTTagCompound();
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            budgets.setLong(Integer.toString(slot), powerBudgets[slot]);
            budgets.setLong("share" + slot, allocatedShares[slot]);
        }
        nbt.setTag("bottledChaosRoundBudgets", budgets);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        batchMode = true;
        machineConfiguration.load(nbt);
        structureUpkeep.load(nbt.getTagList("bottledChaosUpkeepRemainders", 10));
        for (int slot = 0; slot < CREDENTIAL_INJECTION_ITEM_SLOT_COUNT; slot++) {
            credentialInjectionInventory
                .setStackInSlot(slot, GTUtility.loadItem(nbt, CREDENTIAL_INJECTION_ITEM_NBT + slot));
        }
        NBTTagList jobs = nbt.getTagList(THREAD_NBT, 10);
        for (int i = 0; i < THREAD_COUNT; i++) threads[i].load(jobs.getCompoundTagAt(i));
        concentratedPower = nbt.getBoolean("bottledChaosConcentratedPower");
        activeConcentratedPower = nbt.getBoolean("bottledChaosActiveConcentratedPower");
        powerRoundMask = nbt.getInteger("bottledChaosPowerRound") & ((1 << THREAD_COUNT) - 1);
        NBTTagCompound budgets = nbt.getCompoundTag("bottledChaosRoundBudgets");
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            powerBudgets[slot] = Math.max(0, budgets.getLong(Integer.toString(slot)));
            allocatedShares[slot] = Math.max(0, budgets.getLong("share" + slot));
        }
        authorizationDirty = true;
        inputGroupingDirty = true;
        Arrays.fill(inputPeerMasks, 0);
        Arrays.fill(credentialCosts, 0);
        Arrays.fill(threadResults, CheckRecipeResultRegistry.SUCCESSFUL);
        allocatedCapacity = -1;
        inputPower = -1;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MM_BottledChaos(mName);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (direction, rotation, flip) -> direction.offsetY == 0
            && (direction.offsetX != 0 || direction.offsetZ != 0)
            && rotation.isNotRotated()
            && flip.isNotFlipped();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.BottledChaosRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return ImmutableList.of(
            GTCMRecipe.BottledChaosRecipes,
            GTCMRecipe.BottledChaosExpandedRecipes,
            GTCMRecipe.BottledChaosCredentialRecipes,
            GTCMRecipe.BottledChaosStructureRecipes);
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
    public boolean supportsBatchMode() {
        // Batch mode is fixed on; report no toggle support so GUI and wire-cutter controls stay disabled.
        return false;
    }

    @Override
    public boolean getDefaultBatchMode() {
        return true;
    }

    @Override
    public boolean isBatchModeEnabled() {
        return true;
    }

    @Override
    public void setBatchMode(boolean enabled) {
        batchMode = true;
        scheduleRecipeCheckImmediate();
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

    public BottledChaosMachineConfiguration getMachineConfiguration() {
        return machineConfiguration;
    }

    public ItemStackHandler getCredentialInjectionInventory() {
        return credentialInjectionInventory;
    }

    public BottledChaosCrafting getCredentialCrafting() {
        return credentialCrafting;
    }

    private boolean addThreadHatch(int kind, IGregTechTileEntity tile, int texture) {
        if (tile == null) return false;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        int dx = tile.getXCoord() - base.getXCoord();
        int dz = tile.getZCoord() - base.getZCoord();
        ForgeDirection left = getExtendedFacing().getRelativeLeftInWorld();
        ForgeDirection back = getExtendedFacing().getRelativeBackInWorld();
        int a = dx * left.offsetX + dz * left.offsetZ;
        int c = dx * back.offsetX + dz * back.offsetZ;
        // Top view, controller front upwards: right #1..5, rear #6..10, left #11..15.
        int slot;
        if (a == -10 && c >= 0 && c <= 4) slot = c;
        else if (c == 12 && a >= -2 && a <= 2) slot = a + 7;
        else if (a == 10 && c >= 0 && c <= 4) slot = 14 - c;
        else return false;
        if (kind == 0 && tile.getMetaTileEntity() instanceof TST_BottledChaosRecipeTokenHatch hatch) {
            hatch.updateTexture(texture);
            hatch.updateCraftingIcon(getMachineCraftingIcon());
            credentialHatches[slot] = hatch;
            return true;
        }
        if (kind > 0 && tile.getMetaTileEntity() instanceof MTEHatchInputBus bus
            && addInputBusToMachineList(tile, texture)) {
            threadInputs[slot * 2 + kind - 1] = bus;
            return true;
        }
        if (kind > 0 && tile.getMetaTileEntity() instanceof MTEHatchInput hatch
            && addInputHatchToMachineList(tile, texture)) {
            threadInputs[slot * 2 + kind - 1] = hatch;
            return true;
        }
        return false;
    }

    private void clearCredentialHatchBindings() {
        for (TST_BottledChaosRecipeTokenHatch hatch : credentialHatches) {
            if (hatch != null) hatch.unbindController(this);
        }
    }

    @Override
    public boolean checkMachineMM(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        clearCredentialHatchBindings();
        inputGroupingDirty = true;
        Arrays.fill(inputPeerMasks, 0);
        Arrays.fill(credentialCosts, 0);
        capacity = 0;
        generatorLevel = -1;
        minimumUpgradeGlassTier = Integer.MAX_VALUE;
        maximumUpgradeGlassTier = 0;
        credentialHatchLimit = 0;
        allocatedCapacity = -1;
        Arrays.fill(credentialHatches, null);
        Arrays.fill(threadInputs, null);
        for (int[] tierCounts : capacityTierCounts) Arrays.fill(tierCounts, 0);
        if (base.getFrontFacing().offsetY != 0 || base.getFrontFacing() == ForgeDirection.UNKNOWN
            || !getAlignmentLimits().isNewExtendedFacingValid(getExtendedFacing())) {
            // #tr tst.bottledchaos.structure.invalid_alignment
            // # Bottled Chaos requires a horizontal facing without rotation or mirroring.
            // #zh_CN 瓶中混沌只能水平朝向, 不能旋转或镜像.
            errors.add(StructureErrors.of("tst.bottledchaos.structure.invalid_alignment"));
            return false;
        }
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)) {
            if (errors.isEmpty()) errors.add(StructureErrors.of("GT5U.gui.multimachine.incomplete_structure"));
            return false;
        }
        if (generatorLevel > 0 && (minimumUpgradeGlassTier != VoltageIndex.UHV + generatorLevel
            || maximumUpgradeGlassTier != minimumUpgradeGlassTier)) {
            // #tr tst.bottledchaos.structure.upgrade_glass_mismatch
            // # Internal upgrade glass must match the generator voltage tier.
            // #zh_CN 内部升级玻璃必须与发生器的电压等级一致.
            errors.add(StructureErrors.of("tst.bottledchaos.structure.upgrade_glass_mismatch"));
            return false;
        }
        if (!calculateCapacity()) {
            errors.add(StructureErrors.of("tst.bottledchaos.structure.invalid_capacity_region"));
            return false;
        }
        Collection<IModularHatch> parallelControllers = modularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER);
        int parallelControllerCount = parallelControllers == null ? 0 : parallelControllers.size();
        if (parallelControllerCount > 1) {
            errors.add(StructureErrors.of("tst.bottledchaos.structure.multiple_parallel_controllers"));
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
            if (credentialHatches[i] == null && (threadInputs[i * 2] != null || threadInputs[i * 2 + 1] != null)) {
                errors.add(StructureErrors.of("tst.bottledchaos.structure.incomplete_processing_group"));
                return false;
            }
        }
        if (installedCredentialHatches > credentialHatchLimit) {
            errors.add(
                StructureErrors.hatchCount(
                    ErrorType.TOO_MANY,
                    TranslatableText.lang("tst.bottledchaos.credential_hatch.name"),
                    installedCredentialHatches,
                    credentialHatchLimit));
            return false;
        }
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if (credentialHatches[slot] != null) credentialHatches[slot].bindController(this, slot);
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
            calculatedCapacity += 1L << ((selectedTier + 1) * 2);
        }
        capacity = calculatedCapacity;
        return true;
    }

    public long getBottledChaosCapacity() {
        return capacity;
    }

    public long getBottledChaosUsedCapacity() {
        long used = 0;
        for (long cost : credentialCosts) used += cost;
        return used;
    }

    public int getInstalledCredentialHatchCount() {
        int installed = 0;
        for (TST_BottledChaosRecipeTokenHatch hatch : credentialHatches) {
            if (hatch != null && hatch.isValid()) installed++;
        }
        return installed;
    }

    public int getCredentialHatchLimit() {
        return credentialHatchLimit;
    }

    public boolean isConcentratedPowerEnabled() {
        return concentratedPower;
    }

    public boolean isConcentratedPowerActive() {
        return activeConcentratedPower;
    }

    public void setConcentratedPowerEnabled(boolean enabled) {
        if (concentratedPower == enabled) return;
        concentratedPower = enabled;
        authorizationDirty = true;
        scheduleRecipeCheckImmediate();
        markDirty();
    }

    private boolean hasAuthorizedWork(int slot) {
        BottledChaosThread thread = threads[slot];
        return thread.isBusy() && thread.progress < thread.duration
            && !isThreadExecutionDisabled(slot)
            && validGroup(slot)
            && ItemStack.areItemStacksEqual(thread.credential, installedCredentials[slot])
            && thread.recipeVoltage <= getMaximumRecipeVoltage()
            && (poolMask & thread.requiredPools) == thread.requiredPools;
    }

    private void refreshPowerRound() {
        if (activeConcentratedPower) {
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                if ((powerRoundMask & (1 << slot)) != 0 && !hasAuthorizedWork(slot)) {
                    powerRoundMask &= ~(1 << slot);
                    powerBudgets[slot] = allocatedShares[slot] = 0;
                    markDirty();
                }
            }
        }
        if (concentratedPower == activeConcentratedPower) return;
        if (activeConcentratedPower) {
            if (powerRoundMask != 0) return;
        } else {
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                if (hasAuthorizedWork(slot)) return;
            }
        }
        activeConcentratedPower = concentratedPower;
        powerRoundMask = 0;
        Arrays.fill(powerBudgets, 0);
        Arrays.fill(allocatedShares, 0);
        authorizationDirty = true;
        refreshAllocation();
        markDirty();
    }

    private boolean validGroup(int slot) {
        return credentialHatches[slot] != null && credentialHatches[slot].isValid()
            && (threadInputs[slot * 2] == null || threadInputs[slot * 2].isValid())
            && (threadInputs[slot * 2 + 1] == null || threadInputs[slot * 2 + 1].isValid());
    }

    private boolean isThreadExecutionDisabled(int slot) {
        TST_BottledChaosRecipeTokenHatch hatch = credentialHatches[slot];
        return hatch != null && hatch.isValid() && !hatch.isExecutionEnabled();
    }

    private void applyCredentialHatchControls() {
        boolean changed = false;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            TST_BottledChaosRecipeTokenHatch hatch = credentialHatches[slot];
            if (hatch == null || !hatch.isValid()) continue;
            BottledChaosThread thread = threads[slot];
            if (hatch.consumeClearRunningStateRequest()) {
                hatch.getMaterialCache()
                    .clearMaterials();
                hatch.markDirty();
                if (thread.isBusy()) {
                    thread.complete();
                    threadLogics[slot] = new BottledChaosProcessingLogic();
                }
                thread.status = hatch.isExecutionEnabled() ? "GT5U.waila.machine.idle" : "tst.bottledchaos.paused";
                threadResults[slot] = CheckRecipeResultRegistry.SUCCESSFUL;
                thread.nextCheckTick = schedulerTick + 20;
                changed = true;
            }
            if (!hatch.isExecutionEnabled()) {
                if (!"tst.bottledchaos.paused".equals(thread.status)) {
                    thread.status = "tst.bottledchaos.paused";
                    changed = true;
                }
            } else if (!thread.isBusy() && "tst.bottledchaos.paused".equals(thread.status)) {
                thread.status = "GT5U.waila.machine.idle";
                changed = true;
            }
        }
        if (changed) markDirty();
    }

    private void refreshAllocation() {
        boolean changed = authorizationDirty || allocatedCapacity != capacity;
        boolean inputGroupsChanged = inputGroupingDirty;
        int newValidInputGroupMask = 0;
        authorizationDirty = false;
        for (int i = 0; i < THREAD_COUNT; i++) {
            TST_BottledChaosRecipeTokenHatch hatch = credentialHatches[i];
            boolean validHatch = hatch != null && hatch.isValid();
            if (validGroup(i)) newValidInputGroupMask |= 1 << i;
            ItemStack credential = validHatch ? hatch.getAuthorizationStack() : null;
            if (!ItemStack.areItemStacksEqual(credential, installedCredentials[i])) {
                installedCredentials[i] = credential == null ? null : credential.copy();
                // Credential changes also discard recipe-search caches, never already-paid jobs.
                threadLogics[i] = new BottledChaosProcessingLogic();
                if (!threads[i].isBusy()) threadResults[i] = CheckRecipeResultRegistry.SUCCESSFUL;
                inputGroupsChanged = true;
                changed = true;
            }
        }
        if (changed) {
            long installedTags = 0;
            for (ItemStack credential : installedCredentials)
                installedTags |= ItemBottledChaosCredential.getRouteMask(credential);
            machineConfiguration.updateRequiredPools(BottledChaosRecipeProcessor.requiredPoolMask(installedTags));
        }
        long newMask = machineConfiguration.installedPoolMask();
        changed |= poolMask != newMask;
        poolMask = newMask;
        if (inputGroupsChanged || validInputGroupMask != newValidInputGroupMask) {
            refreshInputPeerMasks(newValidInputGroupMask);
            changed = true;
        }
        changed |= normalizeInputGroupExecution();
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            long cost = validGroup(slot) && !isThreadExecutionDisabled(slot)
                && ItemBottledChaosCredential.isBound(installedCredentials[slot])
                    ? (long) ItemBottledChaosCredential
                        .getRequiredWeight(ItemBottledChaosCredential.getRouteMask(installedCredentials[slot]))
                        * (Integer.bitCount(getInputPeerMask(slot)) + 1)
                    : 0;
            if (credentialCosts[slot] != cost) {
                credentialCosts[slot] = cost;
                changed = true;
            }
        }
        long power = changed || schedulerTick % 20 == 0 ? Math.max(0, getMaxInputEu()) : inputPower;
        changed |= power != inputPower;
        if (!changed) return;
        inputPower = power;
        allocatedCapacity = capacity;
        long[] shares = BottledChaosPowerAllocator.allocate(capacity, credentialCosts);
        overCapacity = shares == null;
        if (activeConcentratedPower || concentratedPower != activeConcentratedPower) {
            for (BottledChaosThread thread : threads) thread.nextCheckTick = 0;
            return;
        }
        allocatedShares = overCapacity ? new long[THREAD_COUNT] : shares;
        for (int i = 0; i < THREAD_COUNT; i++) {
            powerBudgets[i] = BottledChaosPowerAllocator.powerForShare(inputPower, allocatedShares[i], capacity);
            threads[i].nextCheckTick = 0;
        }
    }

    public void onCredentialHatchExecutionChanged(TST_BottledChaosRecipeTokenHatch hatch, int slot, boolean enabled) {
        if (!isCredentialHatchConnected(hatch, slot)) return;
        requestedExecutionSlot = enabled ? slot : -1;
        authorizationDirty = true;
        refreshAllocation();
        scheduleRecipeCheckImmediate();
        markDirty();
    }

    private boolean normalizeInputGroupExecution() {
        boolean changed = false;
        int visited = 0;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if (!validGroup(slot) || (visited & (1 << slot)) != 0) continue;
            int group = (1 << slot) | getInputPeerMask(slot);
            visited |= group;
            int selected = -1;
            if (requestedExecutionSlot >= 0 && (group & (1 << requestedExecutionSlot)) != 0
                && !isThreadExecutionDisabled(requestedExecutionSlot)) selected = requestedExecutionSlot;
            if (selected < 0) {
                for (int member = 0; member < THREAD_COUNT; member++) {
                    if ((group & (1 << member)) != 0 && !isThreadExecutionDisabled(member)) {
                        selected = member;
                        break;
                    }
                }
            }
            for (int member = 0; member < THREAD_COUNT; member++) {
                if ((group & (1 << member)) != 0 && member != selected && !isThreadExecutionDisabled(member)) {
                    credentialHatches[member].setExecutionEnabledFromController(false);
                    changed = true;
                }
            }
        }
        requestedExecutionSlot = -1;
        return changed;
    }

    private void refreshInputPeerMasks(int newValidInputGroupMask) {
        int[] peers = new int[THREAD_COUNT];
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if ((newValidInputGroupMask & (1 << slot)) == 0
                || !ItemBottledChaosCredential.isBound(installedCredentials[slot])) continue;
            for (int peer = slot + 1; peer < THREAD_COUNT; peer++) {
                if ((newValidInputGroupMask & (1 << peer)) != 0
                    && ItemBottledChaosCredential.isBound(installedCredentials[peer])
                    && installedCredentials[slot].isItemEqual(installedCredentials[peer])
                    && ItemStack.areItemStackTagsEqual(installedCredentials[slot], installedCredentials[peer])) {
                    peers[slot] |= 1 << peer;
                    peers[peer] |= 1 << slot;
                }
            }
        }
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if (inputGroupingDirty || inputPeerMasks[slot] != peers[slot]
                || ((validInputGroupMask ^ newValidInputGroupMask) & (1 << slot)) != 0) {
                // Input membership invalidates searches, never a thread's already-consumed job.
                threadLogics[slot] = new BottledChaosProcessingLogic();
                if (!threads[slot].isBusy()) threadResults[slot] = CheckRecipeResultRegistry.SUCCESSFUL;
                threads[slot].nextCheckTick = 0;
            }
            inputPeerMasks[slot] = peers[slot];
        }
        validInputGroupMask = newValidInputGroupMask;
        inputGroupingDirty = false;
    }

    private int getInputPeerMask(int slot) {
        if (!validGroup(slot)) return 0;
        int peers = inputPeerMasks[slot];
        for (int peer = 0; peer < THREAD_COUNT; peer++) {
            if ((peers & (1 << peer)) != 0 && !validGroup(peer)) peers &= ~(1 << peer);
        }
        return peers;
    }

    private CheckRecipeResult canRunThread(int slot) {
        BottledChaosThread thread = threads[slot];
        if (!validGroup(slot) || !ItemStack.areItemStacksEqual(thread.credential, installedCredentials[slot])) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.credential_changed");
        }
        if (thread.recipeVoltage > getMaximumRecipeVoltage())
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.recipe_voltage_limit");
        if ((poolMask & thread.requiredPools) != thread.requiredPools) {
            return BottledChaosProcessingLogic.missingPools(thread.requiredPools, poolMask);
        }
        if (thread.progress < thread.duration && thread.eut > powerBudgets[slot]) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.insufficient_power_share");
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessingMM() {
        if (!mMachine) return SimpleResultWithText.ofFailure("GT5U.gui.multimachine.incomplete_structure");
        refreshAllocation();
        refreshPowerRound();
        if (overCapacity) return SimpleResultWithText.ofFailure("tst.bottledchaos.result.capacity_exceeded");
        // Finished jobs retry delivery only in runMachine; recipe searches reuse their last result.
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            CheckRecipeResult result = threadResults[slot];
            if (threads[slot].isBusy() && threads[slot].progress >= threads[slot].duration
                && (result == CheckRecipeResultRegistry.ITEM_OUTPUT_FULL
                    || result == CheckRecipeResultRegistry.FLUID_OUTPUT_FULL))
                return result;
        }
        if (activeConcentratedPower) return checkConcentratedProcessing();
        if (concentratedPower != activeConcentratedPower) return CheckRecipeResultRegistry.SUCCESSFUL;
        CheckRecipeResult failure = null;
        boolean hasWork = false;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            BottledChaosThread thread = threads[slot];
            if (isThreadExecutionDisabled(slot)) {
                thread.status = "tst.bottledchaos.paused";
                continue;
            }
            hasWork |= thread.isBusy();
            if (thread.isBusy() || installedCredentials[slot] == null || schedulerTick < thread.nextCheckTick) continue;
            thread.nextCheckTick = schedulerTick + 20;
            CheckRecipeResult candidate = startThread(slot);
            setThreadSearchResult(slot, candidate);
            hasWork |= candidate.wasSuccessful();
            if (!candidate.wasSuccessful() && candidate != CheckRecipeResultRegistry.NO_RECIPE) failure = candidate;
        }
        return failure != null ? failure
            : hasWork ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NO_RECIPE;
    }

    private CheckRecipeResult checkConcentratedProcessing() {
        if (powerRoundMask != 0) return CheckRecipeResultRegistry.SUCCESSFUL;
        int candidates = 0;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            BottledChaosThread thread = threads[slot];
            if (isThreadExecutionDisabled(slot)) {
                thread.status = "tst.bottledchaos.paused";
                continue;
            }
            if (schedulerTick < thread.nextCheckTick || thread.isBusy() && !hasAuthorizedWork(slot)) continue;
            if (!validGroup(slot) || !ItemBottledChaosCredential.isBound(installedCredentials[slot])) continue;
            candidates |= 1 << slot;
        }
        if (candidates == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        ArrayList<MTEHatchInputBus> allBuses = mInputBusses;
        ArrayList<MTEHatchInputBus> roundBuses = new ArrayList<>(allBuses);
        for (IDualInputHatch dual : mDualInputHatches) {
            if (dual instanceof MTEHatchInputBus bus && !roundBuses.contains(bus)) roundBuses.add(bus);
        }
        mInputBusses = roundBuses;
        InputTransaction inputs = null;
        long[] budgets = null;
        CheckRecipeResult failure = CheckRecipeResultRegistry.NO_RECIPE;
        try {
            startRecipeProcessing();
            // A full-power probe discovers work without changing any real or shared input.
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                if ((candidates & (1 << slot)) == 0) continue;
                CheckRecipeResult result = threads[slot].isBusy()
                    ? threads[slot].eut <= inputPower ? CheckRecipeResultRegistry.SUCCESSFUL
                        : CheckRecipeResultRegistry.insufficientPower(threads[slot].eut)
                    : prepareScopedThread(slot, inputPower, new InputTransaction(true));
                if (!result.wasSuccessful()) {
                    candidates &= ~(1 << slot);
                    failure = result;
                    setThreadSearchResult(slot, result);
                }
            }
            // At most one candidate is removed per attempt. Rebuild all reservations after removal.
            while (candidates != 0) {
                long[] weights = new long[THREAD_COUNT];
                for (int slot = 0; slot < THREAD_COUNT; slot++) {
                    if ((candidates & (1 << slot)) != 0) weights[slot] = credentialCosts[slot];
                }
                budgets = BottledChaosPowerAllocator.concentratedBudgets(inputPower, weights);
                inputs = new InputTransaction();
                int failed = -1;
                for (int slot = 0; slot < THREAD_COUNT; slot++) {
                    if ((candidates & (1 << slot)) == 0) continue;
                    CheckRecipeResult result = threads[slot].isBusy()
                        ? threads[slot].eut <= budgets[slot] ? CheckRecipeResultRegistry.SUCCESSFUL
                            : CheckRecipeResultRegistry.insufficientPower(threads[slot].eut)
                        : prepareScopedThread(slot, budgets[slot], inputs);
                    if (!result.wasSuccessful()) {
                        failed = slot;
                        failure = result;
                        // Prefer a feasible higher-priority subset when simultaneous base power does not fit.
                        if ("insufficient_power".equals(result.getID())) {
                            for (int candidate = THREAD_COUNT - 1; candidate >= 0; candidate--) {
                                if ((candidates & (1 << candidate)) != 0) {
                                    failed = candidate;
                                    break;
                                }
                            }
                        }
                        setThreadSearchResult(failed, result);
                        break;
                    }
                    if (!threads[slot].isBusy()) inputs.prepared[slot] = true;
                }
                if (failed < 0) break;
                candidates &= ~(1 << failed);
                inputs = null;
            }
            if (candidates != 0) {
                inputs.commit();
                updateSlots();
            }
            setCheckRecipeResult(candidates == 0 ? failure : CheckRecipeResultRegistry.SUCCESSFUL);
        } finally {
            try {
                endRecipeProcessing();
            } finally {
                mInputBusses = allBuses;
            }
        }
        // Match the existing GT/ME failure-consumption behavior: failed extraction awards no new job outputs.
        if (!checkRecipeResult.wasSuccessful()) {
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                if ((candidates & (1 << slot)) != 0) setThreadSearchResult(slot, checkRecipeResult);
            }
            return checkRecipeResult;
        }
        if (candidates == 0) return failure;
        powerRoundMask = candidates;
        System.arraycopy(budgets, 0, powerBudgets, 0, THREAD_COUNT);
        long weightSum = 0;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if ((candidates & (1 << slot)) != 0) weightSum += credentialCosts[slot];
        }
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            allocatedShares[slot] = (candidates & (1 << slot)) == 0 ? 0
                : BottledChaosPowerAllocator.powerForShare(capacity, credentialCosts[slot], weightSum);
            if ((candidates & (1 << slot)) == 0) continue;
            if (inputs.prepared[slot]) commitPreparedThread(slot);
            threads[slot].status = "GT5U.gui.text.status.active";
            threadResults[slot] = CheckRecipeResultRegistry.SUCCESSFUL;
        }
        markDirty();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void setThreadSearchResult(int slot, CheckRecipeResult result) {
        threadResults[slot] = result;
        threads[slot].status = result.wasSuccessful() ? "GT5U.gui.text.status.active"
            : result == CheckRecipeResultRegistry.NO_RECIPE ? "GT5U.waila.machine.idle" : result.getDisplayString();
        threads[slot].nextCheckTick = schedulerTick + 20;
    }

    private CheckRecipeResult prepareScopedThread(int slot, long budget, InputTransaction inputs) {
        ArrayList<MTEHatchInputBus> allBuses = mInputBusses;
        ArrayList<MTEHatchInput> allHatches = mInputHatches;
        ArrayList<IDualInputHatch> allDualHatches = mDualInputHatches;
        mDualInputHatches = getScopedDualInputHatches(slot, allDualHatches);
        mInputBusses = getScopedInputBuses(slot, allBuses);
        mInputHatches = getScopedInputHatches(slot, allHatches);
        try {
            return prepareThread(slot, budget, inputs);
        } finally {
            mInputBusses = allBuses;
            mInputHatches = allHatches;
            mDualInputHatches = allDualHatches;
        }
    }

    private CheckRecipeResult startThread(int slot) {
        if (!validGroup(slot)) return SimpleResultWithText.ofFailure("GT5U.gui.multimachine.incomplete_structure");
        if (!ItemBottledChaosCredential.isBound(installedCredentials[slot])) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.unbound");
        }
        if (powerBudgets[slot] <= 0)
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.insufficient_power_share");

        // Scope GT's normal/ME/segmented inputs to this credential's peers and the central inputs.
        ArrayList<MTEHatchInputBus> allBuses = mInputBusses;
        ArrayList<MTEHatchInput> allHatches = mInputHatches;
        ArrayList<IDualInputHatch> allDualHatches = mDualInputHatches;
        mDualInputHatches = getScopedDualInputHatches(slot, allDualHatches);
        mInputBusses = getScopedInputBuses(slot, allBuses);
        mInputHatches = getScopedInputHatches(slot, allHatches);
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
                mDualInputHatches = allDualHatches;
            }
        }
        // ME extraction failures follow GT's failure-consumption semantics: never award outputs.
        if (!checkRecipeResult.wasSuccessful()) return checkRecipeResult;
        if (!result.wasSuccessful()) return result;
        commitPreparedThread(slot);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void commitPreparedThread(int slot) {
        BottledChaosProcessingLogic logic = threadLogics[slot];
        BottledChaosThread thread = threads[slot];
        thread.credential = installedCredentials[slot].copy();
        thread.requiredPools = logic.getRequiredPools();
        thread.upkeep = logic.getUpkeep();
        thread.recipeVoltage = logic.getBaseRecipeVoltage();
        thread.eut = logic.getCalculatedEut();
        thread.duration = Math.max(1, logic.getDuration());
        thread.progress = 0;
        thread.parallels = logic.getCurrentParallels();
        thread.outputs.set(logic.getLongItemOutputs(), logic.getLongFluidOutputs());
        BottledChaosRecipeProcessor.PreparedPlan prepared = logic.getPreparedPlan();
        prepared.commitCache(credentialHatches[slot].getMaterialCache());
        thread.processingFamily = prepared.family();
        BottledChaosRecipeProcessor.Plan pending = prepared.pendingCache();
        thread.pendingCache.set(pending.itemOutputs(), pending.fluidOutputs());
        credentialHatches[slot].markDirty();
        markDirty();
    }

    private ArrayList<MTEHatchInputBus> getScopedInputBuses(int slot, List<MTEHatchInputBus> allBuses) {
        ArrayList<MTEHatchInputBus> scoped = new ArrayList<>();
        int groupMask = (1 << slot) | getInputPeerMask(slot);
        for (int input = 0; input < threadInputs.length; input++) {
            int peer = input / 2;
            if ((groupMask & (1 << peer)) != 0 && validGroup(peer)
                && threadInputs[input] instanceof MTEHatchInputBus bus) addInputReference(scoped, bus);
        }
        for (MTEHatchInputBus bus : allBuses) {
            if (!isDedicatedInput(bus)) addInputReference(scoped, bus);
        }
        // GT registers crafting buffers only in mDualInputHatches. Include their bus lifecycle as well.
        for (IDualInputHatch dual : mDualInputHatches) {
            if (dual instanceof MTEHatchInputBus bus) addInputReference(scoped, bus);
        }
        return scoped;
    }

    private ArrayList<IDualInputHatch> getScopedDualInputHatches(int slot, List<IDualInputHatch> allHatches) {
        ArrayList<IDualInputHatch> scoped = new ArrayList<>();
        int groupMask = (1 << slot) | getInputPeerMask(slot);
        for (IDualInputHatch hatch : allHatches) {
            if (!isDedicatedInput(hatch)) {
                addInputReference(scoped, hatch);
                continue;
            }
            for (int peer = 0; peer < THREAD_COUNT; peer++) {
                if ((groupMask & (1 << peer)) != 0 && validGroup(peer)
                    && (hatch == threadInputs[peer * 2] || hatch == threadInputs[peer * 2 + 1])) {
                    addInputReference(scoped, hatch);
                    break;
                }
            }
        }
        return scoped;
    }

    private ArrayList<MTEHatchInput> getScopedInputHatches(int slot, List<MTEHatchInput> allHatches) {
        ArrayList<MTEHatchInput> scoped = new ArrayList<>();
        int groupMask = (1 << slot) | getInputPeerMask(slot);
        for (int input = 0; input < threadInputs.length; input++) {
            int peer = input / 2;
            if ((groupMask & (1 << peer)) != 0 && validGroup(peer)
                && threadInputs[input] instanceof MTEHatchInput hatch) addInputReference(scoped, hatch);
        }
        for (MTEHatchInput hatch : allHatches) {
            if (!isDedicatedInput(hatch)) addInputReference(scoped, hatch);
        }
        return scoped;
    }

    private static <T> void addInputReference(List<T> scoped, T input) {
        if (input == null) return;
        for (T existing : scoped) {
            if (existing == input) return;
        }
        scoped.add(input);
    }

    private boolean isDedicatedInput(Object hatch) {
        for (MTEHatch dedicated : threadInputs) {
            if (dedicated == hatch) return true;
        }
        return false;
    }

    private CheckRecipeResult prepareThread(int slot) {
        return prepareThread(slot, powerBudgets[slot], null);
    }

    private CheckRecipeResult prepareThread(int slot, long budget, InputTransaction inputs) {
        ArrayList<ItemStack> commonItems = getStoredNonDualInputs();
        ArrayList<FluidStack> commonFluids = getStoredFluids();
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;
        // Every crafting-buffer pattern is checked separately with this group's ordinary and shared inputs.
        for (IDualInputHatch dual : mDualInputHatches) {
            for (var iterator = dual.inventories(); iterator.hasNext();) {
                var inventory = iterator.next();
                if (inventory.isEmpty()) continue;
                ArrayList<ItemStack> items = new ArrayList<>(commonItems);
                ItemStack[] sharedItems = dual.getSharedItems();
                if (sharedItems != null) items.addAll(Arrays.asList(sharedItems));
                ItemStack[] patternItems = inventory.getItemInputs();
                if (patternItems != null) items.addAll(Arrays.asList(patternItems));
                ArrayList<FluidStack> fluids = new ArrayList<>(commonFluids);
                FluidStack[] patternFluids = inventory.getFluidInputs();
                if (patternFluids != null) fluids.addAll(Arrays.asList(patternFluids));
                CheckRecipeResult candidate = prepareThread(slot, items, fluids, budget, inputs);
                if (candidate.wasSuccessful()) return candidate;
                if (candidate != CheckRecipeResultRegistry.NO_RECIPE) result = candidate;
            }
        }
        CheckRecipeResult commonResult = prepareThread(slot, commonItems, commonFluids, budget, inputs);
        return commonResult != CheckRecipeResultRegistry.NO_RECIPE ? commonResult : result;
    }

    private ArrayList<ItemStack> getStoredNonDualInputs() {
        ArrayList<MTEHatchInputBus> scopedBuses = mInputBusses;
        ArrayList<MTEHatchInputBus> ordinaryBuses = new ArrayList<>();
        for (MTEHatchInputBus bus : scopedBuses) {
            if (!(bus instanceof IDualInputHatch)) ordinaryBuses.add(bus);
        }
        ArrayList<ItemStack> storedItems;
        mInputBusses = ordinaryBuses;
        try {
            storedItems = getStoredInputs();
        } finally {
            mInputBusses = scopedBuses;
        }
        // The controller and category GUI slots are not additional material inputs.
        storedItems.removeIf(stack -> stack == mInventory[1]);
        return storedItems;
    }

    private CheckRecipeResult prepareThread(int slot, List<ItemStack> storedItems, List<FluidStack> storedFluids,
        long budget, InputTransaction inputs) {
        processingGroupMask = 1 << slot;
        ItemStack[] originals = uniqueItemReferences(storedItems);
        FluidStack[] originalFluids = uniqueFluidReferences(storedFluids);
        ItemStack[] itemCopies = Arrays.stream(originals)
            .map(stack -> inputs == null ? stack.copy() : inputs.copyItem(stack))
            .toArray(ItemStack[]::new);
        FluidStack[] fluidCopies = Arrays.stream(originalFluids)
            .map(stack -> inputs == null ? stack.copy() : inputs.copyFluid(stack))
            .toArray(FluidStack[]::new);
        // Shared recipe fluids and upkeep use the same pending copies, including earlier concentrated candidates.
        IdentityHashMap<FluidStack, FluidStack> workingFluids = new IdentityHashMap<>();
        for (int i = 0; i < originalFluids.length; i++) workingFluids.put(originalFluids[i], fluidCopies[i]);
        FluidStack[] supply = getCentralSupplyFluids();
        for (int i = 0; i < supply.length; i++) {
            FluidStack working = workingFluids.get(supply[i]);
            supply[i] = working != null ? working : inputs == null ? supply[i].copy() : inputs.copyFluid(supply[i]);
        }

        BottledChaosProcessingLogic logic = threadLogics[slot];
        logic.clear();
        logic.clearLongOutputs();
        logic.setMachine(this);
        logic.setRecipeMapSupplier(this::getRecipeMap);
        logic.setRecipeLocking(null, false);
        logic.setAuthorization(installedCredentials[slot], poolMask);
        logic.setMachineConfiguration(
            getMaximumRecipeVoltage(),
            machineConfiguration.runtime(
                getMaxInputVoltage(),
                Math.max(0, inputPower),
                itemCopies,
                supply,
                getSpeedBonus(),
                getEuModifier(),
                getMaxParallelRecipes()));
        logic.setMaterialCache(credentialHatches[slot].getMaterialCache(), plan -> reserveThreadOutputs(plan, inputs));
        logic.setInputItems(itemCopies);
        logic.setInputFluids(fluidCopies);
        logic.setMaxParallel(1);
        logic.setBatchSize(getMaxBatchSize());
        logic.setEuModifier(getEuModifier());
        logic.setSpeedBonus(getSpeedBonus());
        logic.setOverclockType(overclockType);
        logic.setAvailableVoltage(budget);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(true);
        logic.setVoidProtection(protectsExcessItem(), protectsExcessFluid());
        CheckRecipeResult result = logic.process();
        if (!result.wasSuccessful()) return result;
        if (logic.getCalculatedEut() < 0 || logic.getCalculatedEut() > budget) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.insufficient_power_share");
        }
        List<BottledChaosUpkeep.Demand> active = new ArrayList<>();
        List<BottledChaosUpkeep.Demand> starting = new ArrayList<>(logic.getUpkeep());
        for (int other = 0; other < THREAD_COUNT; other++) {
            if (hasAuthorizedWork(other)) active.addAll(threads[other].upkeep);
            if (inputs != null && inputs.prepared[other]) starting.addAll(threadLogics[other].getUpkeep());
        }
        result = structureUpkeep.checkStart(active, starting, supply);
        if (!result.wasSuccessful()) return result;
        if (inputs != null && inputs.probeOnly) return CheckRecipeResultRegistry.SUCCESSFUL;

        if (inputs != null) {
            for (int i = 0; i < originals.length; i++) inputs.items.put(originals[i], itemCopies[i]);
            for (int i = 0; i < originalFluids.length; i++) inputs.fluids.put(originalFluids[i], fluidCopies[i]);
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        // Only now commit the prechecked input deltas. No real input was touched by ProcessingLogic.
        for (int i = 0; i < originals.length; i++) originals[i].stackSize = itemCopies[i].stackSize;
        for (int i = 0; i < originalFluids.length; i++) originalFluids[i].amount = fluidCopies[i].amount;
        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /** Shared output reservation used by every candidate, including family fallback and concentrated probes. */
    private CheckRecipeResult reserveThreadOutputs(BottledChaosRecipeProcessor.Plan plan, InputTransaction inputs) {
        BottledChaosOutputBuffer.Transaction reservation = new BottledChaosOutputBuffer.Transaction(this, true);
        CheckRecipeResult result = reserveRunningOutputs(reservation);
        if (!result.wasSuccessful()) return result;
        BottledChaosOutputBuffer candidate = new BottledChaosOutputBuffer();
        if (inputs != null) {
            for (int i = 0; i < THREAD_COUNT; i++) {
                if (!inputs.prepared[i]) continue;
                candidate.set(threadLogics[i].getLongItemOutputs(), threadLogics[i].getLongFluidOutputs());
                result = reservation.reserve(candidate);
                if (!result.wasSuccessful()) return result;
            }
        }
        candidate.set(plan.itemOutputs(), plan.fluidOutputs());
        return reservation.reserve(candidate);
    }

    private CheckRecipeResult reserveRunningOutputs(BottledChaosOutputBuffer.Transaction reservation) {
        for (BottledChaosThread thread : threads) {
            if (!thread.isBusy()) continue;
            CheckRecipeResult result = reservation.reserve(thread.outputs);
            if (!result.wasSuccessful()) return result;
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /** Export available carry, including paused hatches, without voiding or touching unfinished job outputs. */
    public void exportMaterialCaches() {
        cacheExportResultExpiresAt = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime() + 60;
        boolean empty = true;
        for (TST_BottledChaosRecipeTokenHatch hatch : credentialHatches) {
            if (hatch != null && hatch.isValid() && !hatch.getMaterialCache().isEmpty()) empty = false;
        }
        if (empty) {
            // #tr tst.bottledchaos.gui.cache_empty
            // # No materials to export.
            // #zh_CN 无可导出的材料.
            cacheExportResult = SimpleResultWithText.ofSuccess("tst.bottledchaos.gui.cache_empty");
            return;
        }
        BottledChaosOutputBuffer.Transaction check = new BottledChaosOutputBuffer.Transaction(this, true, true);
        cacheExportResult = reserveRunningOutputs(check);
        if (!cacheExportResult.wasSuccessful()) return;
        for (TST_BottledChaosRecipeTokenHatch hatch : credentialHatches) {
            if (hatch == null || !hatch.isValid()) continue;
            cacheExportResult = hatch.getMaterialCache().reserveExport(check);
            if (!cacheExportResult.wasSuccessful()) return;
        }
        BottledChaosOutputBuffer.Transaction output = new BottledChaosOutputBuffer.Transaction(this, false, true);
        for (TST_BottledChaosRecipeTokenHatch hatch : credentialHatches) {
            if (hatch == null || !hatch.isValid()) continue;
            cacheExportResult = hatch.getMaterialCache().reserveExport(output);
            if (!cacheExportResult.wasSuccessful()) return;
        }
        output.commit();
        for (TST_BottledChaosRecipeTokenHatch hatch : credentialHatches) {
            if (hatch == null || !hatch.isValid()) continue;
            hatch.getMaterialCache()
                .clearMaterials();
            hatch.markDirty();
        }
        // #tr tst.bottledchaos.gui.cache_exported
        // # Complete.
        // #zh_CN 完成.
        cacheExportResult = SimpleResultWithText.ofSuccess("tst.bottledchaos.gui.cache_exported");
        markDirty();
    }

    public CheckRecipeResult getCacheExportResult() {
        return getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime() < cacheExportResultExpiresAt ? cacheExportResult : CheckRecipeResultRegistry.NONE;
    }

    private static final class InputTransaction {

        private final IdentityHashMap<ItemStack, ItemStack> items = new IdentityHashMap<>();
        private final IdentityHashMap<FluidStack, FluidStack> fluids = new IdentityHashMap<>();
        private final boolean[] prepared = new boolean[THREAD_COUNT];
        private final boolean probeOnly;

        private InputTransaction() {
            this(false);
        }

        private InputTransaction(boolean probeOnly) {
            this.probeOnly = probeOnly;
        }

        private ItemStack copyItem(ItemStack original) {
            return items.getOrDefault(original, original)
                .copy();
        }

        private FluidStack copyFluid(FluidStack original) {
            return fluids.getOrDefault(original, original)
                .copy();
        }

        private void commit() {
            items.forEach((original, copy) -> original.stackSize = copy.stackSize);
            fluids.forEach((original, copy) -> original.amount = copy.amount);
        }
    }

    private static ItemStack[] uniqueItemReferences(List<ItemStack> stacks) {
        IdentityHashMap<ItemStack, Boolean> seen = new IdentityHashMap<>();
        ArrayList<ItemStack> unique = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null && seen.put(stack, Boolean.TRUE) == null) unique.add(stack);
        }
        return unique.toArray(new ItemStack[0]);
    }

    private static FluidStack[] uniqueFluidReferences(List<FluidStack> stacks) {
        IdentityHashMap<FluidStack, Boolean> seen = new IdentityHashMap<>();
        ArrayList<FluidStack> unique = new ArrayList<>();
        for (FluidStack stack : stacks) {
            if (stack != null && seen.put(stack, Boolean.TRUE) == null) unique.add(stack);
        }
        return unique.toArray(new FluidStack[0]);
    }

    @Override
    protected void runMachine(IGregTechTileEntity base, long tick) {
        if (base.isAllowedToWork()) {
            processingGroupMask = 0;
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                if (!isThreadExecutionDisabled(slot) && (installedCredentials[slot] != null || threads[slot].isBusy()))
                    processingGroupMask |= 1 << slot;
            }
        }
        schedulerTick = tick;
        applyCredentialHatchControls();
        refreshAllocation();
        refreshPowerRound();
        mMaxProgresstime = mProgresstime = 0;
        lEUt = 0;
        if (overCapacity) {
            setCheckRecipeResult(SimpleResultWithText.ofFailure("tst.bottledchaos.result.capacity_exceeded"));
            return;
        }
        if (!base.isAllowedToWork()) return;

        long usage = 0;
        Arrays.fill(advancing, false);
        CheckRecipeResult failure = null;
        for (int i = 0; i < THREAD_COUNT; i++) {
            BottledChaosThread thread = threads[i];
            if (!thread.isBusy()) continue;
            if (isThreadExecutionDisabled(i)) {
                thread.status = "tst.bottledchaos.paused";
                continue;
            }
            CheckRecipeResult result = canRunThread(i);
            if (!result.wasSuccessful()) {
                threadResults[i] = result;
                thread.status = result.getDisplayString();
                failure = result;
                continue;
            }
            if (thread.progress < thread.duration) {
                threadResults[i] = CheckRecipeResultRegistry.SUCCESSFUL;
                advancing[i] = true;
                usage = Math.addExact(usage, thread.eut); // Each EU/t is bounded by disjoint allocated shares.
            }
        }
        lEUt = -usage;
        if ((activeConcentratedPower || concentratedPower != activeConcentratedPower) && usage > inputPower) {
            lEUt = 0;
            recordRunningFailure(CheckRecipeResultRegistry.insufficientPower(usage));
            return;
        }
        if (usage > 0 && !payRunningCosts(usage)) return;
        boolean completed = false;
        for (int i = 0; i < THREAD_COUNT; i++) {
            BottledChaosThread thread = threads[i];
            if (!thread.isBusy()) continue;
            if (isThreadExecutionDisabled(i)) continue;
            if (advancing[i]) {
                thread.progress++;
                thread.status = "GT5U.gui.text.status.active";
                markDirty();
            }
            if (thread.progress >= thread.duration && canRunThread(i).wasSuccessful()
                && (advancing[i] || tick >= thread.nextCheckTick)) {
                processingGroupMask = 1 << i;
                // Recheck current capacity: another producer may have occupied the reserved space.
                BottledChaosOutputBuffer.Transaction output = new BottledChaosOutputBuffer.Transaction(this, true);
                CheckRecipeResult result = output.reserve(thread.outputs);
                threadResults[i] = result;
                if (result.wasSuccessful()) {
                    output.commit();
                    recipesDone += thread.parallels;
                    credentialHatches[i].getMaterialCache()
                        .add(thread.processingFamily, thread.pendingCache);
                    credentialHatches[i].markDirty();
                    thread.complete();
                    completed = true;
                    markDirty();
                } else {
                    thread.status = result.getDisplayString();
                    thread.nextCheckTick = tick + OUTPUT_RETRY_TICKS;
                    failure = result;
                }
            }
            if (thread.isBusy() && thread.progress >= thread.duration && !threadResults[i].wasSuccessful()) {
                // Keep the output failure visible between the bounded delivery retries.
                failure = threadResults[i];
            }
            if (thread.isBusy() && (advancing[i] || thread.progress >= thread.duration)
                && thread.duration > mMaxProgresstime) {
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
                if (thread.isBusy() && thread.status.equals("GT5U.gui.text.status.active")
                    && thread.duration > mMaxProgresstime) {
                    mMaxProgresstime = thread.duration;
                    mProgresstime = thread.progress;
                }
            }
        }
        if (failure != null) setCheckRecipeResult(failure);
        mEfficiency = mEfficiencyIncrease = 10000;
    }

    /** Precheck all upkeep fluids, then pay energy and commit through GT's normal ME input lifecycle. */
    private boolean payRunningCosts(long usage) {
        processingGroupMask = 0;
        List<BottledChaosUpkeep.Demand> demands = new ArrayList<>();
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if (advancing[slot]) {
                processingGroupMask |= 1 << slot;
                demands.addAll(threads[slot].upkeep);
            }
        }
        if (demands.isEmpty()) {
            if (drainEnergyInput(usage)) return true;
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return false;
        }
        return withCentralInputs(() -> payRunningCosts(usage, demands));
    }

    private FluidStack[] getCentralSupplyFluids() {
        return withCentralInputs(() -> uniqueFluidReferences(getStoredFluidsWithDualInput()));
    }

    /** Source-machine upkeep belongs to the controller, not the credential's private input positions. */
    private <T> T withCentralInputs(Supplier<T> action) {
        ArrayList<MTEHatchInputBus> allBuses = mInputBusses;
        ArrayList<MTEHatchInput> allHatches = mInputHatches;
        ArrayList<IDualInputHatch> allDualHatches = mDualInputHatches;
        mInputBusses = new ArrayList<>();
        for (MTEHatchInputBus bus : allBuses) if (!isDedicatedInput(bus)) mInputBusses.add(bus);
        mInputHatches = new ArrayList<>();
        for (MTEHatchInput hatch : allHatches) if (!isDedicatedInput(hatch)) mInputHatches.add(hatch);
        mDualInputHatches = new ArrayList<>();
        for (IDualInputHatch hatch : allDualHatches) if (!isDedicatedInput(hatch)) mDualInputHatches.add(hatch);
        try {
            return action.get();
        } finally {
            mInputBusses = allBuses;
            mInputHatches = allHatches;
            mDualInputHatches = allDualHatches;
        }
    }

    private boolean payRunningCosts(long usage, List<BottledChaosUpkeep.Demand> demands) {
        startRecipeProcessing();
        BottledChaosUpkeep.Transaction payment = structureUpkeep
            .prepare(demands, uniqueFluidReferences(getStoredFluidsWithDualInput()));
        if (payment == null) {
            endRecipeProcessing();
            lEUt = 0;
            recordRunningFailure(structureUpkeep.getFailure());
            return false;
        }
        if (!drainEnergyInput(usage)) {
            endRecipeProcessing();
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return false;
        }
        payment.commitInputs();
        setCheckRecipeResult(CheckRecipeResultRegistry.SUCCESSFUL);
        endRecipeProcessing();
        // Extraction failures follow GT's failure-consumption rule; the jobs do not advance.
        if (!checkRecipeResult.wasSuccessful()) {
            lEUt = 0;
            recordRunningFailure(checkRecipeResult);
            return false;
        }
        payment.commitAccounting();
        updateSlots();
        return true;
    }

    private void recordRunningFailure(CheckRecipeResult result) {
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if (!advancing[slot]) continue;
            threadResults[slot] = result;
            threads[slot].status = result.getDisplayString();
        }
        setCheckRecipeResult(result);
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        // Keep already-consumed jobs and pending outputs for a safe resume.
        super.stopMachine(reason);
        for (BottledChaosThread thread : threads) {
            if (thread.isBusy()) thread.status = "tst.bottledchaos.paused";
        }
        markDirty();
    }

    @Override
    public void onRemoval() {
        clearCredentialHatchBindings();
        super.onRemoval();
    }

    @Override
    public void onUnload() {
        clearCredentialHatchBindings();
        super.onUnload();
    }

    @Override
    public void onBlockDestroyed() {
        clearCredentialHatchBindings();
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.isServerSide()) {
            for (ItemStackHandler inventory : new ItemStackHandler[] { machineConfiguration.machines(),
                machineConfiguration.retired() }) {
                for (int slot = 0; slot < inventory.getSlots(); slot++) {
                    ItemStack stack = inventory.getStackInSlot(slot);
                    if (stack == null) continue;
                    inventory.setStackInSlot(slot, null);
                    while (stack.stackSize > 0) {
                        ItemStack drop = stack.copy();
                        drop.stackSize = Math.min(stack.stackSize, stack.getMaxStackSize());
                        stack.stackSize -= drop.stackSize;
                        GTUtility.dropItemToBlockPos(
                            base.getWorld(),
                            base.getXCoord(),
                            base.getYCoord(),
                            base.getZCoord(),
                            drop);
                    }
                }
            }
            for (int slot = 0; slot < CREDENTIAL_INJECTION_ITEM_SLOT_COUNT; slot++) {
                ItemStack stack = credentialInjectionInventory.getStackInSlot(slot);
                if (stack == null) continue;
                credentialInjectionInventory.setStackInSlot(slot, null);
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
        // Preview placement must update hatch counts and textures without an incomplete full check clearing them.
        boolean preview = GregTechAPI.isDummyWorld(getBaseMetaTileEntity().getWorld());
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            environment,
            preview,
            !preview);
    }

    @Override
    public IStructureDefinition<MM_BottledChaos> getStructureDefinition() {
        if (structureDefinition == null) {
            structureDefinition = StructureDefinition.<MM_BottledChaos>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE))
                .addElement('A', Casings.BorosilicateGlassAny.asElement())
                .addElement('a', chainAllGlasses(-1, (machine, tier) -> {
                    machine.minimumUpgradeGlassTier = Math.min(machine.minimumUpgradeGlassTier, tier);
                    machine.maximumUpgradeGlassTier = Math.max(machine.maximumUpgradeGlassTier, tier);
                },
                    // Collect all tiers; checkMachine enforces matching glass after generator upgrades.
                    machine -> -1))
                .addElement('B', Casings.SuperconductingCoilBlock.asElement())
                .addElement('C', Casings.ContainmentFieldMachineCasing.asElement())
                .addElement('D', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
                .addElement('E', Casings.RadiantNaquadahAlloyCasing.asElement())
                .addElement('F', Casings.AdvancedFilterCasing.asElement())
                .addElement('G', GTStructureChannels.SE_MOTOR.use(capacityElement()))
                .addElement('H', Casings.HighPowerCasing.asElement())
                .addElement('I', Casings.IronFence.asElement())
                .addElement('J', ofFrame(Materials.Neutronium))
                .addElement('K', ofFrame(Materials.NaquadahAlloy))
                .addElement('L', ofBlock(TstBlocks.MetaBlockCasing02, 9))
                .addElement('M', ofBlock(GregTechAPI.sBlockCasingsDyson, 9))
                .addElement('N', Casings.QuantumGlass.asElement())
                .addElement('O', ofBlock(TstBlocks.MetaBlockCasing01, 1))
                .addElement('P', ofBlock(TstBlocks.MetaBlockCasing01, 12))
                .addElement('Q', ofBlock(TstBlocks.MetaBlockCasing02, 2))
                .addElement('R', ofBlock(TstBlocks.MetaBlockCasing02, 7))
                .addElement('S', ofBlock(TstBlocks.MetaBlockCasing02, 8))
                .addElement('T', withChannel("energymatrix", generatorElement(TstBlocks.EnergySustainmentMatrix)))
                .addElement(
                    'U',
                    withChannel("fieldgeneratortier", generatorElement(TstBlocks.StabilisationFieldGenerator)))
                .addElement(
                    'Y',
                    hatchElement(
                        HatchElementBuilder.<MM_BottledChaos>builder()
                            .atLeast(
                                InputBus,
                                InputHatch,
                                OutputBus,
                                OutputHatch,
                                Energy.or(ExoticEnergy),
                                ParallelController,
                                SpeedController,
                                PowerConsumptionController,
                                OverclockController),
                        1))
                .addElement(
                    'Z',
                    hatchElement(
                        HatchElementBuilder.<MM_BottledChaos>builder()
                            .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy)),
                        5))
                .addElement('V', threadElement(0))
                .addElement('W', threadElement(1))
                .addElement('X', threadElement(2))
                .build();
        }
        return structureDefinition;
    }

    private static IStructureElement<MM_BottledChaos> hatchElement(HatchElementBuilder<MM_BottledChaos> builder,
        int hint) {
        return builder.casingIndex(TstBlocks.MetaBlockCasing02.getTextureIndex(2))
            .hint(hint)
            .buildAndChain(TstBlocks.MetaBlockCasing02, 2);
    }

    private static IStructureElement<MM_BottledChaos> threadElement(int kind) {
        IHatchElement<MM_BottledChaos> element;
        if (kind == 0) {
            element = InputBus.<MM_BottledChaos>withCount(
                machine -> Arrays.stream(machine.credentialHatches)
                    .filter(Objects::nonNull)
                    .count())
                .withMteClass(TST_BottledChaosRecipeTokenHatch.class)
                .withDisplayName(StatCollector.translateToLocal("tst.bottledchaos.credential_hatch.name"));
        } else {
            element = InputBus.or(InputHatch)
                .<MM_BottledChaos>withCount(machine -> {
                    long count = 0;
                    for (int input = kind - 1; input < machine.threadInputs.length; input += 2) {
                        if (machine.threadInputs[input] != null) count++;
                    }
                    return count;
                });
        }
        HatchElementBuilder<MM_BottledChaos> builder = HatchElementBuilder.<MM_BottledChaos>builder()
            .atLeast(element)
            .adder((machine, tile, texture) -> machine.addThreadHatch(kind, tile, texture));
        if (kind == 0) {
            // GT's withMteClass retains InputBus.matchesHatch; override the placement and skip filters explicitly.
            builder.hatchClass(TST_BottledChaosRecipeTokenHatch.class)
                .description(() -> "tst.bottledchaos.credential_hatch.name");
        }
        return hatchElement(builder, 2 + kind);
    }

    public long getMaximumRecipeVoltage() {
        return GTValues.V[generatorLevel >= 4 ? VoltageIndex.MAX : VoltageIndex.UHV + Math.max(0, generatorLevel)];
    }

    private static IStructureElement<MM_BottledChaos> generatorElement(Block block) {
        return ofBlocksTiered(
            (candidate, meta) -> candidate == block && meta >= 0 && meta <= 4 ? meta : null,
            ImmutableList
                .of(Pair.of(block, 0), Pair.of(block, 1), Pair.of(block, 2), Pair.of(block, 3), Pair.of(block, 4)),
            -1,
            (machine, level) -> machine.generatorLevel = level,
            machine -> machine.generatorLevel);
    }

    private static IStructureElement<MM_BottledChaos> capacityElement() {
        List<IStructureElement<MM_BottledChaos>> levels = new ArrayList<>();
        for (int tier = 0; tier < CAPACITY_LEVEL_COUNT; tier++) {
            levels.add(ofBlock(TstBlocks.BottledChaosCapacity, tier));
        }
        IStructureElementCheckOnly<MM_BottledChaos> check = (machine, world, x, y, z) -> {
            if (world.getBlock(x, y, z) != TstBlocks.BottledChaosCapacity) return true;
            int tier = world.getBlockMetadata(x, y, z);
            if (tier >= CAPACITY_LEVEL_COUNT) return false;
            IGregTechTileEntity base = machine.getBaseMetaTileEntity();
            int dx = x - base.getXCoord();
            int dz = z - base.getZCoord();
            ForgeDirection left = machine.getExtendedFacing()
                .getRelativeLeftInWorld();
            ForgeDirection back = machine.getExtendedFacing()
                .getRelativeBackInWorld();
            // The four fixed columns remain independently tiered after sharing one structure symbol.
            int region = (dx * left.offsetX + dz * left.offsetZ > 0 ? 1 : 0)
                + (dx * back.offsetX + dz * back.offsetZ > 0 ? 2 : 0);
            machine.capacityTierCounts[region][tier]++;
            return true;
        };
        return defer(
            machine -> check,
            (machine, trigger) -> levels.get(Math.min(CAPACITY_LEVEL_COUNT - 1, Math.max(0, trigger.stackSize - 1))));
    }

    private boolean isCredentialHatchConnected(TST_BottledChaosRecipeTokenHatch hatch, int slot) {
        if (slot < 0 || slot >= THREAD_COUNT
            || !isValid()
            || hatch == null
            || !hatch.isValid()
            || credentialHatches[slot] != hatch
            || !mMachine) return false;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        World world = base.getWorld();
        if (world != hatch.getBaseMetaTileEntity()
            .getWorld() || !world.blockExists(base.getXCoord(), base.getYCoord(), base.getZCoord())
            || world.getTileEntity(base.getXCoord(), base.getYCoord(), base.getZCoord()) != base) return false;
        return true;
    }

    public int getCredentialHatchNumber(TST_BottledChaosRecipeTokenHatch hatch, int slot) {
        return isCredentialHatchConnected(hatch, slot) ? slot + 1 : 0;
    }

    public int getCredentialHatchInputPeerMask(TST_BottledChaosRecipeTokenHatch hatch, int slot) {
        if (!isCredentialHatchConnected(hatch, slot)) return 0;
        return getInputPeerMask(slot);
    }

    public NBTTagCompound getCredentialHatchWailaData(TST_BottledChaosRecipeTokenHatch hatch, int slot) {
        if (!isCredentialHatchConnected(hatch, slot)) return null;
        BottledChaosThread thread = threads[slot];
        NBTTagCompound job = new NBTTagCompound();
        job.setInteger("slot", slot + 1);
        job.setInteger("boundSlots", getCredentialHatchInputPeerMask(hatch, slot));
        job.setInteger("progress", thread.progress);
        job.setInteger("duration", thread.duration);
        writeThreadWailaStatus(job, slot);
        job.setLong(
            "eut",
            lEUt < 0 && thread.progress < thread.duration
                && "GT5U.gui.text.status.active".equals(job.getString("status")) ? thread.eut : 0);
        // Normalize the display against GT's practical recipe power, excluding its 1/16 cable-loss headroom.
        job.setDouble("inputPower", Math.max(0, inputPower) * 15.0 / 16.0);
        job.setLong("energyTier", getInputVoltageTier());
        job.setTag("outputs", thread.outputs.getWailaSummary());
        return job;
    }

    private String getThreadWailaStatus(int slot) {
        if (isThreadExecutionDisabled(slot)) return "tst.bottledchaos.paused";
        if (overCapacity) return "tst.bottledchaos.capacity_exceeded";
        if (validGroup(slot) && !ItemBottledChaosCredential.isBound(installedCredentials[slot]))
            return "tst.bottledchaos.unbound";
        if (!mMachine) return "tst.bottledchaos.paused";
        CheckRecipeResult result = getThreadDisplayResult(slot);
        if (isFailure(result)) return "";
        if (!getBaseMetaTileEntity().isAllowedToWork()) return "tst.bottledchaos.paused";
        return threads[slot].status;
    }

    private void writeThreadWailaStatus(NBTTagCompound job, int slot) {
        String status = getThreadWailaStatus(slot);
        job.setString("status", status);
        if (status.isEmpty()) {
            CheckRecipeResult result = getThreadDisplayResult(slot);
            job.setString("resultId", result.getID());
            job.setTag("result", result.writeToNBT(new NBTTagCompound()));
        }
    }

    public static String getWailaStatusText(NBTTagCompound job) {
        if (!job.hasKey("resultId", 8)) return StatCollector.translateToLocal(job.getString("status"));
        CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(job.getString("resultId"))
            .newInstance();
        result.readFromNBT(job.getCompoundTag("result"));
        return result.getDisplayString();
    }

    private static boolean isFailure(CheckRecipeResult result) {
        return !result.wasSuccessful() && !CheckRecipeResultRegistry.NO_RECIPE.equals(result)
            && !CheckRecipeResultRegistry.NONE.equals(result);
    }

    private CheckRecipeResult getGlobalFailure() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || base.isAllowedToWork()) return CheckRecipeResultRegistry.NONE;
        if (isFailure(checkRecipeResult) && checkRecipeResult.persistsOnShutdown()) return checkRecipeResult;
        if (base.wasShutdown()) {
            NBTTagCompound reason = base.getLastShutDownReason()
                .writeToNBT(new NBTTagCompound());
            if (reason.hasKey("key", 8) && !"none".equals(reason.getString("key")))
                return SimpleResultWithText.ofFailure("GT5U.gui.text.shutdown_reason." + reason.getString("key"));
        }
        return CheckRecipeResultRegistry.NONE;
    }

    private CheckRecipeResult getThreadDisplayResult(int slot) {
        CheckRecipeResult global = getGlobalFailure();
        return isFailure(global) && (processingGroupMask & (1 << slot)) != 0 ? global : threadResults[slot];
    }

    public static String formatProcessingGroups(int mask) {
        StringJoiner groups = new StringJoiner("");
        for (int slot = 0; slot < THREAD_COUNT; slot++) if ((mask & (1 << slot)) != 0) groups.add("#" + (slot + 1));
        return groups.toString();
    }

    public int getRecipePoolUsers(int poolSlot) {
        int users = 0;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            long required = BottledChaosRecipeProcessor
                .requiredPoolMask(ItemBottledChaosCredential.getRouteMask(installedCredentials[slot]));
            if (threads[slot].isBusy()) required |= threads[slot].requiredPools;
            if ((BottledChaosMachineConfiguration.withDependencies(required) & (1L << poolSlot)) != 0) users |= 1 << slot;
        }
        return users;
    }

    public NBTTagCompound getBottledChaosStatusSummary() {
        NBTTagCompound summary = new NBTTagCompound();
        NBTTagList entries = new NBTTagList();
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (!mMachine)
            addSummaryResult(entries, SimpleResultWithText.ofFailure("GT5U.gui.multimachine.incomplete_structure"), 0);
        if (overCapacity)
            addSummaryResult(entries, SimpleResultWithText.ofFailure("tst.bottledchaos.result.capacity_exceeded"), 0);
        if (shouldCheckMaintenance() && getRepairStatus() < getIdealStatus()) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString("key", "GT5U.turbine.maintenance.true");
            entries.appendTag(entry);
        }
        CheckRecipeResult global = getGlobalFailure();
        if (mMachine && isFailure(global)) addSummaryResult(entries, global, processingGroupMask);
        long usage = 0;
        int usageGroups = 0;
        if (mMachine) {
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                TST_BottledChaosRecipeTokenHatch hatch = credentialHatches[slot];
                if (hatch == null || !hatch.isValid() || isThreadExecutionDisabled(slot)) continue;
                CheckRecipeResult result;
                if (!validGroup(slot)) {
                    result = SimpleResultWithText.ofFailure("GT5U.gui.multimachine.incomplete_structure");
                } else if (!ItemBottledChaosCredential.isBound(hatch.getAuthorizationStack())) {
                    result = SimpleResultWithText.ofFailure("tst.bottledchaos.result.unbound");
                } else if (threads[slot].isBusy()) {
                    result = canRunThread(slot);
                    if (result.wasSuccessful()) {
                        if (threads[slot].progress < threads[slot].duration) {
                            usage += threads[slot].eut;
                            usageGroups |= 1 << slot;
                        }
                        result = getThreadDisplayResult(slot);
                    }
                } else {
                    result = getThreadDisplayResult(slot);
                }
                if (isFailure(result))
                    addSummaryResult(entries, result, 1 << slot);
            }
        }
        if (base != null && base.isAllowedToWork()
            && (activeConcentratedPower || concentratedPower != activeConcentratedPower)
            && usage > inputPower)
            addSummaryResult(entries, CheckRecipeResultRegistry.insufficientPower(usage), usageGroups);
        summary.setBoolean("errors", entries.tagCount() > 0);
        if (entries.tagCount() == 0) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString(
                "key",
                base != null && !base.isAllowedToWork() ? "tst.bottledchaos.paused" : "GT5U.gui.hoverable.runningfine");
            entries.appendTag(entry);
        }
        summary.setTag("entries", entries);
        return summary;
    }

    private static void addSummaryResult(NBTTagList entries, CheckRecipeResult result, int slots) {
        NBTTagCompound data = result.writeToNBT(new NBTTagCompound());
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            if (result.getID()
                .equals(entry.getString("resultId")) && data.equals(entry.getCompoundTag("result"))) {
                entry.setInteger("slots", entry.getInteger("slots") | slots);
                return;
            }
        }
        NBTTagCompound entry = new NBTTagCompound();
        entry.setString("resultId", result.getID());
        entry.setTag("result", data);
        entry.setInteger("slots", slots);
        entries.appendTag(entry);
    }

    @Override
    protected boolean showParallelInInfo() {
        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.removeTag("maxParallelRecipes");
        tag.removeTag("powerPanelMaxParallel");
        // Replace absolute EU/t with per-credential allocation percentages.
        tag.removeTag("energyUsage");
        tag.setLong(WAILA_CAPACITY, capacity);
        tag.setLong(WAILA_USED_CAPACITY, getBottledChaosUsedCapacity());
        tag.setInteger(WAILA_CREDENTIAL_HATCHES, getInstalledCredentialHatchCount());
        tag.setInteger(WAILA_CREDENTIAL_HATCH_LIMIT, credentialHatchLimit);
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
            job.setString("family", family == null ? "tst.bottledchaos.unbound" : getCredentialRouteKeyList(family));
            writeThreadWailaStatus(job, i);
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
        if (GuiScreen.isShiftKeyDown()) {
            currentTip.add(
                EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.allowed_recipe_categories",
                    localizePoolKeyList(tag.getString(WAILA_RECIPE_CATEGORIES))));
            currentTip.add(
                EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.allowed_recipe_families",
                    localizeKeyList(tag.getString(WAILA_RECIPE_TOKENS))));
            return;
        }
        currentTip.add(
            EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                "tst.bottledchaos.capacity_usage",
                tag.getLong(WAILA_USED_CAPACITY),
                tag.getLong(WAILA_CAPACITY)));
        currentTip.add(
            EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                "tst.bottledchaos.credential_hatches",
                tag.getInteger(WAILA_CREDENTIAL_HATCHES),
                tag.getInteger(WAILA_CREDENTIAL_HATCH_LIMIT)));
        if (tag.getBoolean("bottledChaosOverCapacity")) {
            currentTip
                .add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("tst.bottledchaos.capacity_exceeded"));
        }
        NBTTagList jobs = tag.getTagList(THREAD_NBT, 10);
        for (int i = 0; i < Math.min(THREAD_COUNT, jobs.tagCount()); i++) {
            NBTTagCompound job = jobs.getCompoundTagAt(i);
            double percent = tag.getLong(WAILA_CAPACITY) > 0
                ? job.getLong("share") * 100.0 / tag.getLong(WAILA_CAPACITY)
                : 0;
            currentTip.add(
                "#" + job.getInteger("slot")
                    + " "
                    + localizeKeyList(job.getString("family"))
                    + EnumChatFormatting.WHITE
                    + " | "
                    + StatCollector.translateToLocalFormatted(
                        "tst.bottledchaos.input_share",
                        String.format(Locale.ROOT, "%.2f%%", percent))
                    + " | "
                    + getStatusColor(job.getString("status"))
                    + getWailaStatusText(job)
                    + EnumChatFormatting.RESET);
            if (job.getInteger("duration") > 0) {
                currentTip
                    .add(GTWaila.getMachineProgressString(job.getInteger("duration"), job.getInteger("progress")));
            }
        }
    }

    private String getRecipeCategoryKeyList() {
        long mask = machineConfiguration.installedPoolMask();
        StringJoiner joiner = new StringJoiner(", ");
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++) {
            if ((mask & (1L << slot)) != 0) joiner.add(BottledChaosMachineConfiguration.poolKey(slot));
        }
        return joiner.length() == 0 ? "GT5U.gui.text.nil" : joiner.toString();
    }

    private String getRecipeTokenKeyList() {
        StringJoiner joiner = new StringJoiner(", ");
        for (ItemStack credential : installedCredentials) {
            ItemStack token = ItemBottledChaosCredential.getRecipeFamily(credential);
            if (token != null) {
                for (String key : ItemBottledChaosCredential.getRouteKeyList(token)) joiner.add(key);
            }
        }
        return joiner.length() == 0 ? "GT5U.gui.text.nil" : joiner.toString();
    }

    private static String getCredentialRouteKeyList(ItemStack credential) {
        StringJoiner joiner = new StringJoiner(", ");
        for (String key : ItemBottledChaosCredential.getRouteKeyList(credential)) joiner.add(key);
        return joiner.length() == 0 ? "tst.bottledchaos.unbound" : joiner.toString();
    }

    private static String localizeKeyList(String keys) {
        StringJoiner localized = new StringJoiner(", ");
        for (String key : keys.split(", ")) localized.add(StatCollector.translateToLocal(key));
        return localized.toString();
    }

    private static String localizePoolKeyList(String keys) {
        StringJoiner localized = new StringJoiner(", ");
        for (String key : keys.split(", ")) localized.add(BottledChaosMachineConfiguration.poolName(key));
        return localized.toString();
    }

    public static EnumChatFormatting getStatusColor(String status) {
        if ("GT5U.gui.text.status.active".equals(status)) return EnumChatFormatting.GOLD;
        if ("GT5U.waila.machine.idle".equals(status)) return EnumChatFormatting.AQUA;
        return EnumChatFormatting.WHITE;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        if (tooltip == null) {
            tooltip = new MultiblockTooltipBuilder()
                .addMachineType(TSTUtils.tr("tst.bottledchaos.tooltip.machine_type"))
                .addInfo(TSTUtils.tr("tst.bottledchaos.tooltip.info.01"))
                .addInfo(TSTUtils.tr("tst.bottledchaos.tooltip.info.02"))
                .addSeparator()
                .beginStructureBlock(29, 41, 29, false)
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.body"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.capacity_columns"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.voltage_upgrades"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.upgrade_glass"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.processing_groups"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.shared_inputs"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.parameter_modules"))
                .toolTipFinisher(TSTSharedLocalization.General.ModName);
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
