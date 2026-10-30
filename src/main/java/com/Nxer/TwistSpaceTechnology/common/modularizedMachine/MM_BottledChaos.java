package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.OverclockController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.ParallelController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.PowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.SpeedController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.CARRIER_SLOT;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.MATERIAL_SLOT_START;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.OUTPUT_SLOT;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.SLOT_COUNT;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.defer;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
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
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

import com.Nxer.TwistSpaceTechnology.common.block.meta.BottledChaosCapacityBlock;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.TST_Gui_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellHelper.InputSource;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_BottledChaosRecipeTokenHatch;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineSupportAllModuleBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineParameters;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosMaterialSummary;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosOutputBuffer;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosPowerAllocator;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosStatus;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosThread;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosUpkeep;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result.BottledChaosResultData;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ModularHatchBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
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
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
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
import gregtech.common.tileentities.machines.IDualInputHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;

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
    private static final String CREDENTIAL_INJECTION_ITEM_NBT = "bottledChaosCredentialInjectionItem";
    private static final String WAILA_CAPACITY = "bottledChaosCapacity";
    private static final String WAILA_USED_CAPACITY = "bottledChaosUsedCapacity";
    private static final String WAILA_CREDENTIAL_HATCHES = "bottledChaosCredentialHatches";
    private static final String WAILA_CREDENTIAL_HATCH_LIMIT = "bottledChaosCredentialHatchLimit";
    private static final String WAILA_RECIPE_CATEGORIES = "bottledChaosRecipeCategories";
    private static final String WAILA_RECIPE_TOKENS = "bottledChaosRecipeTokens";
    public static final int THREAD_COUNT = 15;
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

    // #tr tst.bottledchaos.tooltip.wireless
    // # At UXV, omitting energy hatches enables wireless power. Each group prepays its normalized energy and runs for 2s without further overclocking.
    // #zh_CN UXV等级未安装能源仓时启用无线模式. 每组预付折算后的总电量, 固定运行2s, 不额外超频.

    // #tr tst.bottledchaos.wireless_mode
    // # Wireless mode
    // #zh_CN 无线模式

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
        {"                             ","          D       D          ","     EEEEEEEEEEEEEEEEEEE     ","    ECCCCCCSSSSSSSCCCCCCE    ","   ESSSSSSSSSSSSSSSSSSSSSE   ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","  ECSSRRSSSSSSSSSSSSSRRSSCE  ","  ECSSRRSSSSSSSSSSSSSRRSSCE  ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","  ECSSSSSSSSAAAAASSSSSSSSCE  "," DECSSSSSSSAAAAAAASSSSSSSCED ","  ESSSSSSSAAAAAAAAASSSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSAAAAAAAAAAASSSSSSE  ","  ESSSSSSSAAAAAAAAASSSSSSSE  "," DECSSSSSSSAAAAAAASSSSSSSCED ","  ECSSSSSSSSAAAAASSSSSSSSCE  ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","  ECSSRRSSSSSSSSSSSSSRRSSCE  ","  ECSSRRSSSSSSSSSSSSSRRSSCE  ","  ECSSSSSSSSSSSSSSSSSSSSSCE  ","   ESSSSSSSSSSSSSSSSSSSSSE   ","    ECCCCCCSSSSSSSCCCCCCE    ","     EEEEEEEEEEEEEEEEEEE     ","          D       D          ","                             "},
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
        poolUsersDirty = true;
        markDirty();
    });
    private final ItemStackHandler credentialInjectionInventory = new ItemStackHandler(SLOT_COUNT) {

        @Override
        public int getSlotLimit(int slot) {
            return slot >= MATERIAL_SLOT_START && slot < OUTPUT_SLOT ? 64 : 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == CARRIER_SLOT) {
                return ItemBottledChaosCredential.getCapacity(stack) > 0
                    || ItemBottledChaosMachineModule.isModule(stack);
            }
            return slot < OUTPUT_SLOT;
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
    private final boolean[] matchingCredentials = new boolean[THREAD_COUNT];
    private final int[] inputPeerMasks = new int[THREAD_COUNT];
    private final int[] credentialCosts = new int[THREAD_COUNT];
    private final int[] powerWeights = new int[THREAD_COUNT];
    private CheckRecipeResult cacheExportResult = CheckRecipeResultRegistry.NONE;
    private int cacheExportSlot = -1;
    private long cacheExportResultExpiresAt;
    private int processingGroupMask;
    private final long[] powerBudgets = new long[THREAD_COUNT];
    private final boolean[] advancing = new boolean[THREAD_COUNT];
    private final BottledChaosUpkeep structureUpkeep = new BottledChaosUpkeep();
    private final boolean[] runnable = new boolean[THREAD_COUNT];
    private final ArrayList<MTEHatchInputBus> centralInputBuses = new ArrayList<>();
    private final ArrayList<MTEHatchInput> centralInputHatches = new ArrayList<>();
    private final int[] recipePoolUsers = new int[BottledChaosMachineConfiguration.MAX_POOLS];
    private long missingRecipePools;
    private boolean poolUsersDirty = true;
    private boolean upkeepDirty = true;
    private int upkeepGroupMask = -1;
    private long upkeepRetryTick;
    private CheckRecipeResult upkeepFailure = CheckRecipeResultRegistry.SUCCESSFUL;
    private long statusSummaryTick = Long.MIN_VALUE;
    private NBTTagCompound statusSummary;
    private int generatorLevel = -1;
    private int minimumUpgradeGlassTier;
    private int maximumUpgradeGlassTier;
    private int capacity;
    private int allocatedCapacity = -1;
    private long inputPower = -1;
    private boolean allocationWireless;
    private BigInteger runningPower = BigInteger.ZERO;
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
    private boolean powerAllocationPending = true;
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
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        machineConfiguration.save(nbt);
        nbt.setTag("bottledChaosUpkeep", structureUpkeep.save());
        for (int slot = 0; slot < SLOT_COUNT; slot++) {
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
        }
        nbt.setTag("bottledChaosRoundBudgets", budgets);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        batchMode = true;
        machineConfiguration.load(nbt);
        structureUpkeep.load(nbt.getTagList("bottledChaosUpkeep", 10));
        upkeepDirty = poolUsersDirty = true;
        upkeepRetryTick = 0;
        for (int slot = 0; slot < SLOT_COUNT; slot++) {
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
        }
        authorizationDirty = true;
        inputGroupingDirty = true;
        Arrays.fill(inputPeerMasks, 0);
        Arrays.fill(credentialCosts, 0);
        Arrays.fill(powerWeights, 0);
        powerAllocationPending = true;
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
        centralInputBuses.clear();
        centralInputHatches.clear();
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
        for (MTEHatchInputBus bus : mInputBusses) if (!isDedicatedInput(bus)) centralInputBuses.add(bus);
        for (MTEHatchInput hatch : mInputHatches) if (!isDedicatedInput(hatch)) centralInputHatches.add(hatch);
        return true;
    }

    private boolean calculateCapacity() {
        int calculatedCapacity = 0;
        for (int region = 0; region < CAPACITY_REGION_COUNT; region++) {
            int selectedTier = -1;
            for (int tier = 0; tier < CAPACITY_LEVEL_COUNT; tier++) {
                int count = capacityTierCounts[region][tier];
                if (count == 0) continue;
                if (selectedTier >= 0 || count != CAPACITY_REGION_BLOCK_COUNT) return false;
                selectedTier = tier;
            }
            calculatedCapacity += 1 << ((selectedTier + 1) * 2);
        }
        capacity = calculatedCapacity;
        return true;
    }

    public int getBottledChaosCapacity() {
        return capacity;
    }

    public int getBottledChaosUsedCapacity() {
        int used = 0;
        for (int cost : credentialCosts) used += cost;
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

    public boolean isWirelessMode() {
        return generatorLevel >= 4 && mEnergyHatches.isEmpty() && mExoticEnergyHatches.isEmpty();
    }

    private BottledChaosMachineParameters.Context machineParameterContext(long voltage, long power, ItemStack[] items,
        FluidStack[] fluids) {
        var controllers = modularHatches.get(ModularHatchTypes.OVERCLOCK_CONTROLLER);
        return new BottledChaosMachineParameters.Context(
            voltage,
            power,
            items,
            fluids,
            getSpeedBonus(),
            getEuModifier(),
            getMaxParallelRecipes(),
            overclockType,
            isWirelessMode(),
            controllers != null && !controllers.isEmpty());
    }

    private long parameterVoltage() {
        return isWirelessMode() ? GTValues.V[VoltageIndex.MAX] : getMaxInputVoltage();
    }

    public void setConcentratedPowerEnabled(boolean enabled) {
        if (isWirelessMode()) return;
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
            && matchingCredentials[slot]
            && (thread.isWireless() || thread.recipeVoltage <= getMaximumRecipeVoltage())
            && (poolMask & thread.requiredPools) == thread.requiredPools;
    }

    private void refreshPowerRound() {
        if (isWirelessMode()) return;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if ((powerRoundMask & (1 << slot)) != 0 && (!hasAuthorizedWork(slot) || threads[slot].isWireless())) {
                powerRoundMask &= ~(1 << slot);
                if (activeConcentratedPower || powerWeights[slot] == 0) powerBudgets[slot] = 0;
                markDirty();
            }
        }
        if (concentratedPower != activeConcentratedPower) {
            if (powerRoundMask != 0) return;
            activeConcentratedPower = concentratedPower;
            Arrays.fill(powerBudgets, 0);
            authorizationDirty = true;
            refreshAllocation();
            markDirty();
        }
        if (activeConcentratedPower) return;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if (!threads[slot].isWireless() && hasAuthorizedWork(slot) && (powerRoundMask & (1 << slot)) == 0)
                powerAllocationPending = true;
        }
        if (!powerAllocationPending || powerRoundMask != 0) return;
        // Paid jobs retain their original EU/t and finish in compatible rounds before new reservations apply.
        for (int slot = 0; slot < THREAD_COUNT; slot++)
            if (!threads[slot].isWireless() && hasAuthorizedWork(slot)) return;
        long[] budgets = BottledChaosPowerAllocator.proportionalBudgets(inputPower, powerWeights);
        System.arraycopy(budgets, 0, powerBudgets, 0, THREAD_COUNT);
        powerAllocationPending = false;
        for (BottledChaosThread thread : threads) thread.nextCheckTick = 0;
        markDirty();
    }

    private boolean isWaitingForPowerRound(int slot) {
        if (isWirelessMode() || threads[slot].isWireless()) return false;
        return (activeConcentratedPower || powerAllocationPending || concentratedPower != activeConcentratedPower)
            && (powerRoundMask & (1 << slot)) == 0
            && threads[slot].progress < threads[slot].duration;
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

    private void refreshAllocation() {
        int newValidInputGroupMask = 0;
        for (int slot = 0; slot < THREAD_COUNT; slot++) if (validGroup(slot)) newValidInputGroupMask |= 1 << slot;
        boolean changed = authorizationDirty || allocatedCapacity != capacity
            || inputGroupingDirty
            || validInputGroupMask != newValidInputGroupMask
            || allocationWireless != isWirelessMode();
        allocationWireless = isWirelessMode();
        authorizationDirty = false;
        if (changed) {
            poolUsersDirty = true;
            boolean inputGroupsChanged = inputGroupingDirty;
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                TST_BottledChaosRecipeTokenHatch hatch = credentialHatches[slot];
                ItemStack credential = hatch != null && hatch.isValid() ? hatch.getAuthorizationStack() : null;
                if (!ItemStack.areItemStacksEqual(credential, installedCredentials[slot])) {
                    installedCredentials[slot] = credential == null ? null : credential.copy();
                    // Credential changes also discard recipe-search caches, never already-paid jobs.
                    threadLogics[slot] = new BottledChaosProcessingLogic();
                    if (!threads[slot].isBusy()) threads[slot].resetResult();
                    inputGroupsChanged = true;
                }
                matchingCredentials[slot] = ItemStack
                    .areItemStacksEqual(threads[slot].credential, installedCredentials[slot]);
            }
            long installedTags = 0;
            for (ItemStack credential : installedCredentials)
                installedTags |= ItemBottledChaosCredential.getRouteMask(credential);
            machineConfiguration.updateRequiredPools(BottledChaosRecipeProcessor.requiredPoolMask(installedTags));
            poolMask = machineConfiguration.installedPoolMask();
            if (inputGroupsChanged || validInputGroupMask != newValidInputGroupMask) {
                refreshInputPeerMasks(newValidInputGroupMask);
            }
            normalizeInputGroupExecution();
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                credentialCosts[slot] = validGroup(slot) && !isThreadExecutionDisabled(slot)
                    && ItemBottledChaosCredential.isBound(installedCredentials[slot])
                        ? ItemBottledChaosCredential
                            .getRequiredWeight(ItemBottledChaosCredential.getRouteMask(installedCredentials[slot]))
                            * (activeConcentratedPower ? Integer.bitCount(getInputPeerMask(slot)) + 1 : 1)
                        : 0;
                powerWeights[slot] = credentialCosts[slot] > 0 && (!threads[slot].isBusy() || matchingCredentials[slot])
                    ? ItemBottledChaosCredential.getCapacity(installedCredentials[slot])
                    : 0;
            }
        }
        long power = isWirelessMode() ? GTValues.V[VoltageIndex.MAX]
            : changed || schedulerTick % 20 == 0 ? Math.max(0, getMaxInputEu()) : inputPower;
        changed |= power != inputPower;
        if (!changed) return;
        inputPower = power;
        allocatedCapacity = capacity;
        overCapacity = capacity <= 0 || getBottledChaosUsedCapacity() > capacity;
        if (isWirelessMode()) {
            powerRoundMask = 0;
            powerAllocationPending = false;
            Arrays.fill(powerBudgets, 0);
            for (BottledChaosThread thread : threads) thread.nextCheckTick = 0;
            return;
        }
        if (!activeConcentratedPower) powerAllocationPending = !Arrays
            .equals(powerBudgets, BottledChaosPowerAllocator.proportionalBudgets(inputPower, powerWeights));
        for (BottledChaosThread thread : threads) thread.nextCheckTick = 0;
    }

    public void onCredentialHatchContentsChanged(TST_BottledChaosRecipeTokenHatch hatch, int slot) {
        if (!isCredentialHatchConnected(hatch, slot)) return;
        authorizationDirty = true;
        scheduleRecipeCheckImmediate();
    }

    public void onCredentialHatchExecutionChanged(TST_BottledChaosRecipeTokenHatch hatch, int slot, boolean enabled) {
        if (!isCredentialHatchConnected(hatch, slot)) return;
        requestedExecutionSlot = enabled ? slot : -1;
        authorizationDirty = true;
        statusSummaryTick = Long.MIN_VALUE;
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
                    threads[member].pause();
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
                if (!threads[slot].isBusy()) threads[slot].resetResult();
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
        if (!validGroup(slot) || !matchingCredentials[slot]) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.credential_changed");
        }
        if (!thread.isWireless() && thread.recipeVoltage > getMaximumRecipeVoltage())
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.recipe_voltage_limit");
        if ((poolMask & thread.requiredPools) != thread.requiredPools) {
            return BottledChaosProcessingLogic.missingPools(thread.requiredPools, poolMask, true);
        }
        if (thread.isWireless() || thread.progress >= thread.duration) return CheckRecipeResultRegistry.SUCCESSFUL;
        if (isWirelessMode()) return CheckRecipeResultRegistry.insufficientPower(thread.eut);
        if (thread.eut > inputPower) return CheckRecipeResultRegistry.insufficientPower(thread.eut);
        if (!isWaitingForPowerRound(slot) && thread.eut > powerBudgets[slot]) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.insufficient_power_share");
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessingMM() {
        if (!mMachine) return SimpleResultWithText.ofFailure("GT5U.gui.multimachine.incomplete_structure");
        if (!getBaseMetaTileEntity().isAllowedToWork()) return CheckRecipeResultRegistry.NO_RECIPE;
        refreshAllocation();
        refreshPowerRound();
        if (overCapacity) return SimpleResultWithText.ofFailure("tst.bottledchaos.result.capacity_exceeded");
        // Finished jobs retry delivery only in runMachine; recipe searches reuse their last result.
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            CheckRecipeResult result = threads[slot].result();
            if (threads[slot].isBusy() && threads[slot].progress >= threads[slot].duration
                && (result == CheckRecipeResultRegistry.ITEM_OUTPUT_FULL
                    || result == CheckRecipeResultRegistry.FLUID_OUTPUT_FULL))
                return result;
        }
        if (!isWirelessMode() && (activeConcentratedPower || powerAllocationPending))
            return checkPowerRoundProcessing(!activeConcentratedPower);
        if (!isWirelessMode() && concentratedPower != activeConcentratedPower)
            return CheckRecipeResultRegistry.SUCCESSFUL;
        CheckRecipeResult failure = null;
        boolean hasWork = false;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            BottledChaosThread thread = threads[slot];
            if (isThreadExecutionDisabled(slot)) {
                thread.pause();
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

    private CheckRecipeResult checkPowerRoundProcessing(boolean resumeOnly) {
        if (powerRoundMask != 0) return CheckRecipeResultRegistry.SUCCESSFUL;
        int candidates = 0;
        int paidCandidates = 0;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            BottledChaosThread thread = threads[slot];
            if (isThreadExecutionDisabled(slot)) {
                thread.pause();
                continue;
            }
            if (thread.isWireless() || schedulerTick < thread.nextCheckTick
                || thread.isBusy() && !hasAuthorizedWork(slot)) continue;
            if (!validGroup(slot) || !ItemBottledChaosCredential.isBound(installedCredentials[slot])) continue;
            candidates |= 1 << slot;
            if (thread.isBusy()) paidCandidates |= 1 << slot;
        }
        // Resumed jobs must not compete with newly planned jobs which could repeatedly take their power.
        if (resumeOnly || paidCandidates != 0) candidates = paidCandidates;
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
            // Multiple groups need a full-power eligibility probe before sharing power. A lone group already
            // receives that same budget in the real pass, so do not plan its complete chain twice.
            boolean probeCandidates = Integer.bitCount(candidates) > 1;
            for (int slot = 0; probeCandidates && slot < THREAD_COUNT; slot++) {
                if ((candidates & (1 << slot)) == 0) continue;
                CheckRecipeResult result = threads[slot].isBusy()
                    ? threads[slot].eut <= inputPower ? CheckRecipeResultRegistry.SUCCESSFUL
                        : CheckRecipeResultRegistry.insufficientPower(threads[slot].eut)
                    : prepareScopedThread(slot, inputPower, new InputTransaction(false, true));
                if (!result.wasSuccessful()) {
                    candidates &= ~(1 << slot);
                    failure = result;
                    setThreadSearchResult(slot, result);
                }
            }
            // At most one candidate is removed per attempt. Rebuild all reservations after removal.
            while (candidates != 0) {
                int[] weights = new int[THREAD_COUNT];
                for (int slot = 0; slot < THREAD_COUNT; slot++) {
                    if ((candidates & (1 << slot)) != 0) weights[slot] = powerWeights[slot];
                }
                budgets = BottledChaosPowerAllocator.proportionalBudgets(inputPower, weights);
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
                        if ("insufficient_power".equals(result.getID()) && Integer.bitCount(candidates) > 1) {
                            setThreadSearchResult(failed, CheckRecipeResultRegistry.SUCCESSFUL);
                            threads[failed].status = BottledChaosStatus.WAITING_POWER.key();
                        } else {
                            setThreadSearchResult(failed, result);
                        }
                        break;
                    }
                    if (!threads[slot].isBusy()) inputs.prepared[slot] = true;
                }
                if (failed < 0) break;
                candidates &= ~(1 << failed);
                inputs = null;
            }
            setCheckRecipeResult(candidates == 0 ? failure : commitInputs(inputs));
            if (candidates != 0 && checkRecipeResult.wasSuccessful()) updateSlots();
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
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            if ((candidates & (1 << slot)) == 0) continue;
            if (inputs.prepared[slot]) commitPreparedThread(slot);
            threads[slot].activate();
        }
        markDirty();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void setThreadSearchResult(int slot, CheckRecipeResult result) {
        threads[slot].searched(result, schedulerTick + 20);
    }

    private CheckRecipeResult prepareScopedThread(int slot, long budget, InputTransaction inputs) {
        return withGroupInputs(slot, () -> prepareThread(slot, budget, inputs));
    }

    private <T> T withGroupInputs(int slot, Supplier<T> action) {
        ArrayList<IDualInputHatch> duals = getScopedDualInputHatches(slot, mDualInputHatches);
        return withInputs(
            getScopedInputBuses(slot, mInputBusses, duals),
            getScopedInputHatches(slot, mInputHatches),
            duals,
            action);
    }

    private CheckRecipeResult startThread(int slot) {
        if (!validGroup(slot)) return SimpleResultWithText.ofFailure("GT5U.gui.multimachine.incomplete_structure");
        if (!ItemBottledChaosCredential.isBound(installedCredentials[slot])) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.unbound");
        }
        if (!isWirelessMode() && powerBudgets[slot] <= 0)
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.insufficient_power_share");

        InputTransaction inputs = new InputTransaction(false, false);
        CheckRecipeResult result = withGroupInputs(slot, () -> {
            try {
                startRecipeProcessing();
                CheckRecipeResult prepared = prepareThread(slot, powerBudgets[slot], inputs);
                setCheckRecipeResult(prepared);
                return prepared;
            } finally {
                endRecipeProcessing();
            }
        });
        // ME extraction failures follow GT's failure-consumption semantics: never award outputs.
        if (!checkRecipeResult.wasSuccessful()) {
            if (inputs.wirelessPaid.signum() > 0)
                addEUToGlobalEnergyMap(getBaseMetaTileEntity().getOwnerUuid(), inputs.wirelessPaid);
            return checkRecipeResult;
        }
        if (!result.wasSuccessful()) return result;
        commitPreparedThread(slot);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void commitPreparedThread(int slot) {
        BottledChaosProcessingLogic logic = threadLogics[slot];
        BottledChaosThread thread = threads[slot];
        thread.begin(installedCredentials[slot], logic);
        if (!thread.isWireless()) powerRoundMask |= 1 << slot;
        matchingCredentials[slot] = true;
        upkeepDirty = poolUsersDirty = true;
        BottledChaosRecipeProcessor.PreparedPlan prepared = logic.getPreparedPlan();
        prepared.commitCache(credentialHatches[slot].getMaterialCache());
        thread.setPendingCache(prepared.family(), prepared.pendingCache());
        credentialHatches[slot].markDirty();
        markDirty();
    }

    private ArrayList<MTEHatchInputBus> getScopedInputBuses(int slot, List<MTEHatchInputBus> allBuses,
        List<IDualInputHatch> duals) {
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
        // Include the scoped crafting buffers in the bus lifecycle as well.
        for (IDualInputHatch dual : duals) {
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

    private CheckRecipeResult prepareThread(int slot, long budget, InputTransaction inputs) {
        InputTransaction.View common = withNonDualInputs(
            mInputBusses,
            mInputHatches,
            () -> captureInputs(inputs, true));
        InputTransaction.View central = withCentralInputs(() -> captureInputs(inputs, false));
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;
        // Each pattern remains isolated; only ordinary and shared inputs are common to every attempt.
        for (IDualInputHatch dual : mDualInputHatches) {
            ItemStack[] sharedItems = dual.getSharedItems();
            for (var iterator = dual.inventories(); iterator.hasNext();) {
                var inventory = iterator.next();
                if (inventory.isEmpty()) continue;
                InputTransaction.View pattern = common.copy();
                if (sharedItems != null) for (ItemStack item : sharedItems) pattern.add(item);
                ItemStack[] items = inventory.getItemInputs();
                if (items != null) for (ItemStack item : items) pattern.add(item);
                FluidStack[] fluids = inventory.getFluidInputs();
                if (fluids != null) for (FluidStack fluid : fluids) pattern.add(fluid);
                CheckRecipeResult candidate = prepareThread(slot, pattern, central, budget, inputs);
                if (candidate.wasSuccessful() || inputs.commitFailed) return candidate;
                if (candidate != CheckRecipeResultRegistry.NO_RECIPE) result = candidate;
            }
        }
        CheckRecipeResult commonResult = prepareThread(slot, common, central, budget, inputs);
        return commonResult != CheckRecipeResultRegistry.NO_RECIPE ? commonResult : result;
    }

    /** Capture the caller's ordinary-input scope; dual inventories are added only by prepareThread(). */
    private InputTransaction.View captureInputs(InputTransaction inputs, boolean includeItems) {
        InputTransaction.View view = inputs.new View();
        if (includeItems) getLongItemInputs().getSources()
            .forEach(view::add);
        getLongFluidInputs().getSources()
            .forEach(view::add);
        view.limits.keySet()
            .removeIf(source -> source.owner == mInventory[1]);
        return view;
    }

    private CheckRecipeResult prepareThread(int slot, InputTransaction.View recipe, InputTransaction.View central,
        long budget, InputTransaction inputs) {
        processingGroupMask = 1 << slot;
        InputTransaction candidate = inputs.fork();
        InputTransaction.Snapshot working = candidate.snapshot(recipe);
        InputTransaction.Snapshot supply = candidate.snapshot(central);

        BottledChaosProcessingLogic logic = threadLogics[slot];
        logic.clear();
        logic.clearLongOutputs();
        logic.setMachine(this);
        logic.setRecipeMapSupplier(this::getRecipeMap);
        logic.setRecipeLocking(null, false);
        logic.setAuthorization(installedCredentials[slot], poolMask);
        logic.setWireless(isWirelessMode());
        logic.setMachineConfiguration(
            getMaximumRecipeVoltage(),
            machineConfiguration.runtime(
                machineParameterContext(
                    parameterVoltage(),
                    Math.max(0, inputPower),
                    working.itemStacks(),
                    supply.fluidStacks())));
        logic.setMaterialCache(credentialHatches[slot].getMaterialCache(), plan -> reserveThreadOutputs(plan, inputs));
        logic.setInputs(working.getInputItems(), working.getInputFluids());
        logic.setMaxParallel(1);
        logic.setBatchSize(getMaxBatchSize());
        logic.setEuModifier(getEuModifier());
        logic.setSpeedBonus(getSpeedBonus());
        logic.setOverclockType(overclockType);
        // Wireless jobs request their total energy after planning; only wired jobs use an EU/t budget.
        if (!isWirelessMode()) logic.setAvailableVoltage(budget);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(true);
        logic.setVoidProtection(protectsExcessItem(), protectsExcessFluid());
        CheckRecipeResult result = logic.process();
        if (!result.wasSuccessful()) return result;
        if (!isWirelessMode() && (logic.getCalculatedEut() < 0 || logic.getCalculatedEut() > budget)) {
            return SimpleResultWithText.ofFailure("tst.bottledchaos.result.insufficient_power_share");
        }
        List<BottledChaosUpkeep.Demand> active = new ArrayList<>();
        List<BottledChaosUpkeep.Demand> starting = new ArrayList<>(logic.getUpkeep());
        for (int other = 0; other < THREAD_COUNT; other++) {
            if (hasAuthorizedWork(other)) active.addAll(threads[other].upkeep);
            if (inputs.isPrepared(other)) starting.addAll(threadLogics[other].getUpkeep());
        }
        candidate.accept(working);
        supply = candidate.snapshot(central);
        result = structureUpkeep.checkStart(active, starting, supply.getInputFluids());
        if (!result.wasSuccessful()) return result;
        List<BottledChaosUpkeep.ItemDemand> roundItems = logic.getPreparedPlan()
            .roundItems();
        if (!roundItems.isEmpty()) {
            supply = candidate.snapshot(withCentralInputs(() -> captureInputs(inputs, true)));
            result = BottledChaosUpkeep.consumeRoundItems(roundItems, supply.getInputItems());
            if (!result.wasSuccessful()) return result;
            candidate.accept(supply);
        }
        // Both modes accept the same checked deltas; only the transaction decides when to apply them.
        if (isWirelessMode() && !inputs.deferred() && !inputs.probeOnly) {
            var owner = getBaseMetaTileEntity().getOwnerUuid();
            strongCheckOrAddUser(owner);
            if (!addEUToGlobalEnergyMap(
                owner,
                logic.getWirelessCost()
                    .negate())) {
                // #tr tst.bottledchaos.result.insufficient_wireless_energy
                // # Insufficient wireless energy.
                // #zh_CN 无线电网电量不足.
                return SimpleResultWithText.ofFailure("tst.bottledchaos.result.insufficient_wireless_energy");
            }
        }
        if (!inputs.probeOnly) {
            inputs.accept(candidate);
            if (!inputs.deferred()) {
                if (isWirelessMode()) inputs.wirelessPaid = logic.getWirelessCost();
                result = commitInputs(inputs);
                inputs.commitFailed = !result.wasSuccessful();
                if (result.wasSuccessful()) updateSlots();
                return result;
            }
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private CheckRecipeResult commitInputs(InputTransaction inputs) {
        CheckRecipeResult result = inputs.commit();
        if (!result.wasSuccessful()) stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
        return result;
    }

    /** Shared output reservation used by every candidate, including family fallback and concentrated probes. */
    private CheckRecipeResult reserveThreadOutputs(BottledChaosRecipeProcessor.Plan plan, InputTransaction inputs) {
        BottledChaosOutputBuffer.Transaction reservation = BottledChaosOutputBuffer.Transaction.forRecipeOutputs(this);
        CheckRecipeResult result = reserveRunningOutputs(reservation);
        if (!result.wasSuccessful()) return result;
        BottledChaosOutputBuffer candidate = new BottledChaosOutputBuffer();
        if (inputs.deferred()) {
            for (int i = 0; i < THREAD_COUNT; i++) {
                if (!inputs.isPrepared(i)) continue;
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
    public void exportMaterialCache(TST_BottledChaosRecipeTokenHatch hatch, int slot) {
        if (!isCredentialHatchConnected(hatch, slot) || !getBaseMetaTileEntity().isServerSide()) return;
        cacheExportSlot = slot;
        cacheExportResultExpiresAt = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime() + 60;
        if (hatch.getMaterialCache()
            .isEmpty()) {
            cacheExportResult = SimpleResultWithText.ofSuccess("tst.bottledchaos.gui.cache_empty");
            return;
        }
        BottledChaosOutputBuffer.Transaction check = BottledChaosOutputBuffer.Transaction.forCacheExportCheck(this);
        cacheExportResult = reserveRunningOutputs(check);
        if (!cacheExportResult.wasSuccessful()) return;
        cacheExportResult = hatch.getMaterialCache()
            .reserveExport(check);
        if (!cacheExportResult.wasSuccessful()) return;
        BottledChaosOutputBuffer.Transaction output = BottledChaosOutputBuffer.Transaction.forCacheExport(this);
        cacheExportResult = hatch.getMaterialCache()
            .reserveExport(output);
        if (!cacheExportResult.wasSuccessful()) return;
        output.commit();
        hatch.getMaterialCache()
            .clearMaterials();
        hatch.markDirty();
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

    public int getCacheExportSlot() {
        return cacheExportSlot;
    }

    /** Shared reservations belong to this scheduling attempt; source access stays in the existing input adapters. */
    private static final class InputTransaction {

        private final Map<Object, Map<Object, InputSource>> sources;
        private final Map<InputSource, Long> reserved = new LinkedHashMap<>();
        private final boolean[] prepared;
        private final boolean probeOnly;
        private boolean commitFailed;
        private BigInteger wirelessPaid = BigInteger.ZERO;

        private InputTransaction() {
            this(true, false);
        }

        private InputTransaction(boolean deferred, boolean probeOnly) {
            sources = new IdentityHashMap<>();
            prepared = deferred ? new boolean[THREAD_COUNT] : null;
            this.probeOnly = probeOnly;
        }

        private InputTransaction(InputTransaction original) {
            sources = original.sources;
            prepared = original.prepared;
            probeOnly = original.probeOnly;
            reserved.putAll(original.reserved);
        }

        private boolean deferred() {
            return prepared != null;
        }

        private boolean isPrepared(int slot) {
            return deferred() && prepared[slot];
        }

        private InputTransaction fork() {
            return new InputTransaction(this);
        }

        private final class View {

            private final Map<InputSource, Long> limits = new LinkedHashMap<>();

            private View copy() {
                View copy = new View();
                copy.limits.putAll(limits);
                return copy;
            }

            private void add(ItemStack stack) {
                add(InputSource.of(stack, stack));
            }

            private void add(FluidStack stack) {
                add(InputSource.of(stack, stack));
            }

            private void add(InputSource input) {
                if (input == null) return;
                long limit = input.amount;
                InputSource source = input.register(sources);
                if (source != null) limits.merge(source, limit, Math::max);
            }
        }

        private Snapshot snapshot(View view) {
            return new Snapshot(view, reserved);
        }

        private void accept(Snapshot snapshot) {
            for (int i = 0; i < snapshot.sources.size(); i++) {
                long remaining = i < snapshot.items.length ? snapshot.items[i].stackSize()
                    : snapshot.fluids[i - snapshot.items.length].amount();
                long used = snapshot.initial[i] - remaining;
                if (used < 0 || remaining < 0) throw new IllegalStateException("Invalid input reservation");
                if (used > 0) reserved.merge(snapshot.sources.get(i), used, Math::addExact);
            }
        }

        private void accept(InputTransaction candidate) {
            reserved.clear();
            reserved.putAll(candidate.reserved);
        }

        private CheckRecipeResult commit() {
            for (var entry : reserved.entrySet()) {
                if (entry.getKey().available.getAsLong() < entry.getValue()) return extractionFailure(entry.getKey());
            }
            // A short real extraction follows GT's non-rollback semantics; never retry an already debited candidate.
            for (var entry : reserved.entrySet()) {
                if (entry.getKey().extract.applyAsLong(entry.getValue()) != entry.getValue()) {
                    reserved.clear();
                    return extractionFailure(entry.getKey());
                }
            }
            reserved.clear();
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        private static CheckRecipeResult extractionFailure(InputSource source) {
            return SimpleCheckRecipeResult.ofFailurePersistOnShutdown(
                source.template instanceof ItemStack ? "stocking_bus_fail_extraction"
                    : "stocking_hatch_fail_extraction");
        }

        private static final class Snapshot {

            private final List<InputSource> sources = new ArrayList<>();
            private final ItemStackLong[] items;
            private final FluidStackLong[] fluids;
            private final long[] initial;

            private Snapshot(View view, Map<InputSource, Long> reserved) {
                for (InputSource source : view.limits.keySet())
                    if (source.template instanceof ItemStack) sources.add(source);
                int itemCount = sources.size();
                for (InputSource source : view.limits.keySet())
                    if (source.template instanceof FluidStack) sources.add(source);
                items = new ItemStackLong[itemCount];
                fluids = new FluidStackLong[sources.size() - itemCount];
                initial = new long[sources.size()];
                for (int i = 0; i < sources.size(); i++) {
                    InputSource source = sources.get(i);
                    initial[i] = Math
                        .min(view.limits.get(source), Math.max(0, source.amount - reserved.getOrDefault(source, 0L)));
                    if (i < itemCount) items[i] = new ItemStackLong(((ItemStack) source.template).copy(), initial[i]);
                    else fluids[i - itemCount] = new FluidStackLong(((FluidStack) source.template).copy(), initial[i]);
                }
            }

            private ItemStackLong[] getInputItems() {
                return items;
            }

            private FluidStackLong[] getInputFluids() {
                return fluids;
            }

            /** Bounded copies for structure parameter checks only, never for recipe allocation. */
            private ItemStack[] itemStacks() {
                ItemStack[] result = new ItemStack[items.length];
                for (int i = 0; i < result.length; i++) result[i] = GTUtility
                    .copyAmountUnsafe((int) Math.min(Integer.MAX_VALUE, items[i].stackSize()), items[i].itemStack());
                return result;
            }

            private FluidStack[] fluidStacks() {
                FluidStack[] result = new FluidStack[fluids.length];
                for (int i = 0; i < result.length; i++) result[i] = GTUtility
                    .copyAmount((int) Math.min(Integer.MAX_VALUE, fluids[i].amount()), fluids[i].fluidStack());
                return result;
            }
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
        schedulerTick = tick;
        statusSummaryTick = Long.MIN_VALUE;
        runningPower = BigInteger.ZERO;
        if (base.isAllowedToWork()) processingGroupMask = 0;
        boolean controlsChanged = false;
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            TST_BottledChaosRecipeTokenHatch hatch = credentialHatches[slot];
            BottledChaosThread thread = threads[slot];
            if (hatch != null && hatch.isValid()) {
                if (hatch.consumeClearRunningStateRequest()) {
                    threadLogics[slot] = new BottledChaosProcessingLogic();
                    thread.complete();
                    upkeepDirty = poolUsersDirty = authorizationDirty = true;
                    controlsChanged = true;
                }
                controlsChanged |= hatch.isExecutionEnabled() ? thread.resumeIdle() : thread.pause();
            }
            if (base.isAllowedToWork() && !isThreadExecutionDisabled(slot)
                && (installedCredentials[slot] != null || thread.isBusy())) processingGroupMask |= 1 << slot;
        }
        if (controlsChanged) markDirty();
        refreshAllocation();
        refreshPowerRound();
        // Like GT, disabling work prevents new recipes; stopMachine clears this running marker on faults.
        lEUt = 0;
        if (overCapacity) {
            mMaxProgresstime = mProgresstime = 0;
            setCheckRecipeResult(SimpleResultWithText.ofFailure("tst.bottledchaos.result.capacity_exceeded"));
            return;
        }
        if (!base.isAllowedToWork() && mMaxProgresstime <= 0) return;

        long usage = 0;
        Arrays.fill(advancing, false);
        Arrays.fill(runnable, false);
        int runningMask = 0;
        CheckRecipeResult failure = null;
        for (int i = 0; i < THREAD_COUNT; i++) {
            BottledChaosThread thread = threads[i];
            if (!thread.isBusy()) continue;
            if (isThreadExecutionDisabled(i)) {
                thread.pause();
                continue;
            }
            CheckRecipeResult result = canRunThread(i);
            if (!result.wasSuccessful()) {
                thread.fail(result);
                failure = result;
                continue;
            }
            runnable[i] = true;
            if (isWaitingForPowerRound(i)) {
                thread.status = BottledChaosStatus.WAITING_POWER.key();
                thread.resetResult();
                continue;
            }
            if (thread.progress < thread.duration) {
                advancing[i] = true;
                runningMask |= 1 << i;
                usage = Math.addExact(usage, thread.eut); // Each EU/t is bounded by disjoint allocated shares.
            }
        }
        lEUt = -usage;
        if (usage > inputPower) {
            lEUt = 0;
            recordRunningFailure(CheckRecipeResultRegistry.insufficientPower(usage));
            return;
        }
        if (runningMask != 0 && !payRunningCosts(usage, runningMask)) return;
        // Keep GT's running marker through transient payment failures; rebuild it after successful processing.
        mMaxProgresstime = mProgresstime = 0;
        boolean completed = false;
        boolean progressed = false;
        for (int i = 0; i < THREAD_COUNT; i++) {
            BottledChaosThread thread = threads[i];
            if (!thread.isBusy()) continue;
            if (isThreadExecutionDisabled(i)) continue;
            if (advancing[i]) {
                thread.advance();
                progressed = true;
            }
            if (thread.progress >= thread.duration && runnable[i] && (advancing[i] || tick >= thread.nextCheckTick)) {
                processingGroupMask = 1 << i;
                // Recheck current capacity: another producer may have occupied the reserved space.
                BottledChaosOutputBuffer.Transaction output = BottledChaosOutputBuffer.Transaction
                    .forRecipeOutputs(this);
                CheckRecipeResult result = output.reserve(thread.outputs);
                if (result.wasSuccessful()) {
                    output.commit();
                    recipesDone += thread.parallels;
                    credentialHatches[i].getMaterialCache()
                        .add(thread.processingFamily, thread.pendingCache);
                    credentialHatches[i].markDirty();
                    thread.complete();
                    upkeepDirty = poolUsersDirty = true;
                    completed = true;
                } else {
                    thread.waitForOutput(result, tick + OUTPUT_RETRY_TICKS);
                    failure = result;
                }
            }
            if (thread.isBusy() && thread.progress >= thread.duration
                && !threads[i].result()
                    .wasSuccessful()) {
                // Keep the output failure visible between the bounded delivery retries.
                failure = threads[i].result();
            }
            // Paid jobs waiting for a power round remain part of shutdown completion.
            if (thread.isBusy() && runnable[i] && thread.duration > mMaxProgresstime) {
                mMaxProgresstime = thread.duration;
                mProgresstime = thread.progress;
            }
        }
        if (progressed || completed) markDirty();
        // Failed/empty slots retry at most once a second. Successful slots may start their next job immediately.
        boolean shouldSearch = completed || tick % 20 == 0 || base.hasWorkJustBeenEnabled();
        if (shouldSearch) {
            CheckRecipeResult result = base.isAllowedToWork() ? checkProcessing()
                : !isWirelessMode() && (activeConcentratedPower || powerAllocationPending)
                    ? checkPowerRoundProcessing(true)
                    : CheckRecipeResultRegistry.NO_RECIPE;
            if (failure == null) setCheckRecipeResult(result);
            for (BottledChaosThread thread : threads) {
                if (thread.isBusy() && thread.status.equals(BottledChaosStatus.ACTIVE.key())
                    && thread.duration > mMaxProgresstime) {
                    mMaxProgresstime = thread.duration;
                    mProgresstime = thread.progress;
                }
            }
        }
        if (failure != null) setCheckRecipeResult(failure);
        // Use the current jobs on both sides of the ratio, including replacements started this tick.
        for (BottledChaosThread thread : threads) {
            if (thread.progress < thread.duration && BottledChaosStatus.ACTIVE.key()
                .equals(thread.status)) runningPower = runningPower.add(thread.power());
        }
        mEfficiency = mEfficiencyIncrease = 10000;
    }

    /** Only a new prepaid second opens the central ME input transaction. */
    private boolean payRunningCosts(long usage, int runningMask) {
        processingGroupMask = runningMask;
        if (upkeepDirty || upkeepGroupMask != runningMask) {
            List<BottledChaosUpkeep.Demand> demands = new ArrayList<>();
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                if (advancing[slot]) demands.addAll(threads[slot].upkeep);
            }
            structureUpkeep.setActiveDemands(demands);
            upkeepGroupMask = runningMask;
            upkeepDirty = false;
            upkeepRetryTick = 0;
        }
        if (structureUpkeep.needsPayment()) {
            if (schedulerTick < upkeepRetryTick) {
                lEUt = 0;
                recordRunningFailure(upkeepFailure);
                return false;
            }
            if (!withCentralInputs(() -> payRunningCosts(usage))) {
                if (!checkRecipeResult.wasSuccessful()) {
                    upkeepFailure = checkRecipeResult;
                    upkeepRetryTick = schedulerTick + 20;
                }
                return false;
            }
        } else if (usage > 0 && !drainEnergyInput(usage)) {
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return false;
        }
        structureUpkeep.advance();
        return true;
    }

    private FluidStack[] getCentralSupplyFluids() {
        return withCentralInputs(() -> uniqueFluidReferences(getStoredFluids()));
    }

    private ItemStack[] getCentralSupplyItems() {
        return withCentralInputs(() -> {
            ArrayList<ItemStack> items = getStoredInputs();
            items.removeIf(stack -> stack == mInventory[1]);
            return uniqueItemReferences(items);
        });
    }

    /** Upkeep uses only central ordinary/long inputs; no private inputs or dual-inventory materials. */
    private <T> T withCentralInputs(Supplier<T> action) {
        return withNonDualInputs(centralInputBuses, centralInputHatches, action);
    }

    /** The host adds dual busses for GT's lifecycle, but must read their inventories separately. */
    private <T> T withNonDualInputs(List<MTEHatchInputBus> buses, List<MTEHatchInput> hatches, Supplier<T> action) {
        return withInputs(withoutDualInputs(buses), withoutDualInputs(hatches), new ArrayList<>(), action);
    }

    private static <T> ArrayList<T> withoutDualInputs(List<T> hatches) {
        ArrayList<T> ordinary = new ArrayList<>();
        for (T hatch : hatches) if (!(hatch instanceof IDualInputHatch)) ordinary.add(hatch);
        return ordinary;
    }

    /** Scope only the exposed inputs; the caller owns GT's start/end processing lifecycle. */
    private <T> T withInputs(ArrayList<MTEHatchInputBus> buses, ArrayList<MTEHatchInput> hatches,
        ArrayList<IDualInputHatch> duals, Supplier<T> action) {
        ArrayList<MTEHatchInputBus> allBuses = mInputBusses;
        ArrayList<MTEHatchInput> allHatches = mInputHatches;
        ArrayList<IDualInputHatch> allDualHatches = mDualInputHatches;
        mInputBusses = buses;
        mInputHatches = hatches;
        mDualInputHatches = duals;
        try {
            return action.get();
        } finally {
            mInputBusses = allBuses;
            mInputHatches = allHatches;
            mDualInputHatches = allDualHatches;
        }
    }

    private boolean payRunningCosts(long usage) {
        BottledChaosUpkeep.Transaction payment;
        startRecipeProcessing();
        try {
            InputTransaction inputs = new InputTransaction(false, false);
            InputTransaction.Snapshot supply = inputs.snapshot(captureInputs(inputs, false));
            payment = structureUpkeep.prepare(supply.getInputFluids());
            setCheckRecipeResult(payment.result());
            if (checkRecipeResult.wasSuccessful()) {
                if (usage > 0 && !drainEnergyInput(usage)) {
                    stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                    return false;
                }
                payment.commitInputs();
                inputs.accept(supply);
                setCheckRecipeResult(commitInputs(inputs));
            }
        } finally {
            endRecipeProcessing();
        }
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
            threads[slot].fail(result);
        }
        setCheckRecipeResult(result);
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        // Keep already-consumed jobs and pending outputs for a safe resume.
        super.stopMachine(reason);
        for (BottledChaosThread thread : threads) {
            if (thread.isBusy()) thread.pause();
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
            ItemStackHandler inventory = machineConfiguration.machines();
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
            for (int slot = 0; slot < SLOT_COUNT; slot++) {
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
                .addElement('G', withChannel(BottledChaosCapacityBlock.STRUCTURE_CHANNEL, capacityElement()))
                .addElement('H', Casings.HighPowerCasing.asElement())
                .addElement('I', Casings.IronFence.asElement())
                .addElement('J', ofFrame(Materials.Neutronium))
                .addElement('K', ofFrame(Materials.NaquadahAlloy))
                .addElement('L', ofBlock(TstBlocks.MetaBlockCasing02, 9))
                .addElement('M', ofBlock(GregTechAPI.sBlockCasingsDyson, 9))
                .addElement('N', Casings.QuantumGlass.asElement())
                .addElement('O', ofBlock(TstBlocks.MetaBlockCasing01, 1))
                .addElement('P', ofBlock(TstBlocks.MetaBlockCasing02, 4))
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
        levels.add(isAir());
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
        // Runtime checks accept mixed non-capacity blocks; construction and previews select an explicit tier.
        return defer(
            (machine, trigger) -> trigger == null ? check
                : levels.get(Math.min(CAPACITY_LEVEL_COUNT, Math.max(0, trigger.stackSize - 1))));
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

    public TST_BottledChaosRecipeTokenHatch getManagedCredentialHatch(int slot) {
        if (slot < 0 || slot >= THREAD_COUNT) return null;
        return isCredentialHatchConnected(credentialHatches[slot], slot) ? credentialHatches[slot] : null;
    }

    /** Always synchronized while the host GUI is open; switching pages never clears the overview. */
    public NBTTagCompound getCredentialHatchSummary() {
        NBTTagCompound summary = new NBTTagCompound();
        summary.setInteger("capacity", capacity);
        NBTTagList hatches = new NBTTagList();
        for (int slot = 0; slot < THREAD_COUNT; slot++) {
            hatches.appendTag(getCredentialHatchInformation(getManagedCredentialHatch(slot), this, slot, false));
        }
        summary.setTag("hatches", hatches);
        return summary;
    }

    /** Detailed data is requested only for the hovered hatch, not for all fifteen slots each tick. */
    public NBTTagCompound getCredentialHatchDetails(int slot) {
        return getCredentialHatchInformation(getManagedCredentialHatch(slot), this, slot, true);
    }

    /**
     * Authoritative hatch information. Detached hatches retain their physical inventory and settings,
     * but never expose a stale controller job or allocation. Both GUI and Waila use these same fields.
     */
    public static NBTTagCompound getCredentialHatchInformation(TST_BottledChaosRecipeTokenHatch hatch,
        MM_BottledChaos controller, int slot, boolean details) {
        NBTTagCompound data = new NBTTagCompound();
        boolean installed = hatch != null && hatch.isValid();
        data.setBoolean("installed", installed);
        if (!installed) return data;
        ItemStack credential = hatch.getStackInSlot(0);
        data.setBoolean("enabled", hatch.isExecutionEnabled());
        data.setBoolean("hasCredential", ItemBottledChaosCredential.getCapacity(credential) > 0);
        data.setString("family", ItemBottledChaosCredential.getDisplayRouteKeys(credential));
        data.setString("tags", String.join(", ", ItemBottledChaosCredential.getRouteKeyList(credential)));
        data.setInteger(
            "cacheTypes",
            hatch.getMaterialCache()
                .typeCount());
        data.setLong(
            "cacheLimit",
            hatch.getMaterialCache()
                .amountLimit());
        if (details) data.setTag(
            "cache",
            BottledChaosMaterialSummary
                .forCache(hatch.getMaterialCache(), ItemBottledChaosCredential.getRouteMask(credential)));
        boolean connected = controller != null && controller.isCredentialHatchConnected(hatch, slot);
        data.setBoolean("connected", connected);
        if (!connected) return data;
        controller.writeProcessingGroupInformation(data, slot);
        data.setInteger("boundSlots", controller.getInputPeerMask(slot));
        data.setInteger("capacity", controller.capacity);
        BottledChaosThread thread = controller.threads[slot];
        if (details) data.setTag(
            "outputs",
            BottledChaosMaterialSummary.forJob(
                thread.outputs,
                thread.pendingCache,
                ItemBottledChaosCredential.getRouteMask(thread.credential)));
        return data;
    }

    private void writeProcessingGroupInformation(NBTTagCompound data, int slot) {
        data.setInteger("slot", slot + 1);
        data.setInteger("progress", threads[slot].progress);
        data.setInteger("duration", threads[slot].duration);
        writeThreadWailaStatus(data, slot);
        if (threads[slot].isBusy() && isFailure(threads[slot].blockedRoute)) {
            NBTTagCompound failure = new NBTTagCompound();
            BottledChaosResultData.write(failure, threads[slot].blockedRoute);
            data.setTag("blockedRoute", failure);
        }
        boolean active = runningPower.signum() > 0 && threads[slot].progress < threads[slot].duration
            && BottledChaosStatus.ACTIVE.key()
                .equals(data.getString("status"));
        BigInteger power = active ? threads[slot].power() : BigInteger.ZERO;
        data.setByteArray("power", power.toByteArray());
        data.setLong(
            "energyTier",
            isWirelessMode() || threads[slot].isWireless() ? VoltageIndex.MAX : getInputVoltageTier());
        if (isWirelessMode() || threads[slot].isWireless()) {
            data.setDouble(
                "powerRatio",
                runningPower.signum() > 0 ? power.doubleValue() / runningPower.doubleValue() : 0);
        } else {
            data.setDouble(
                "powerRatio",
                inputPower > 0 && powerWeights[slot] > 0 && !isWaitingForPowerRound(slot)
                    ? (double) powerBudgets[slot] / inputPower
                    : 0);
        }
    }

    private void writeThreadWailaStatus(NBTTagCompound job, int slot) {
        String status;
        if (isThreadExecutionDisabled(slot)) status = BottledChaosStatus.PAUSED.key();
        else if (overCapacity) status = BottledChaosStatus.CAPACITY_EXCEEDED.key();
        else if (validGroup(slot) && !ItemBottledChaosCredential.isBound(installedCredentials[slot]))
            status = BottledChaosStatus.UNBOUND.key();
        else if (!mMachine) status = BottledChaosStatus.PAUSED.key();
        else if ((getBaseMetaTileEntity().isAllowedToWork() || mMaxProgresstime > 0) && hasAuthorizedWork(slot)
            && threads[slot].eut <= inputPower
            && isWaitingForPowerRound(slot)) status = BottledChaosStatus.WAITING_POWER.key();
        else {
            CheckRecipeResult result = getThreadDisplayResult(slot);
            status = isFailure(result) ? ""
                : !getBaseMetaTileEntity().isAllowedToWork() && (!threads[slot].isBusy() || mMaxProgresstime <= 0)
                    ? BottledChaosStatus.PAUSED.key()
                    : threads[slot].status;
            if (BottledChaosStatus.IDLE.key()
                .equals(status) && powerWeights[slot] > 0
                && powerRoundMask != 0
                && (activeConcentratedPower || powerAllocationPending
                    || concentratedPower != activeConcentratedPower)) {
                status = BottledChaosStatus.WAITING_POWER.key();
            }
            job.setString("status", status);
            if (status.isEmpty()) BottledChaosResultData.write(job, result);
            return;
        }
        job.setString("status", status);
    }

    @SideOnly(Side.CLIENT)
    public static String getCredentialHatchBoundInputs(int peers) {
        // #tr tst.bottledchaos.credential_hatch.bound_inputs
        // # Bound to %s
        // #zh_CN 与%s绑定
        return StatCollector
            .translateToLocalFormatted("tst.bottledchaos.credential_hatch.bound_inputs", formatProcessingGroups(peers));
    }

    @SideOnly(Side.CLIENT)
    public static String getCredentialHatchTitle(NBTTagCompound data) {
        if (data == null || !data.getBoolean("connected")) return "#-";
        int peers = data.getInteger("boundSlots");
        return "#" + data.getInteger("slot") + (peers == 0 ? "" : " | " + getCredentialHatchBoundInputs(peers));
    }

    @SideOnly(Side.CLIENT)
    public static void appendCredentialHatchWaila(List<String> currentTip, NBTTagCompound data, boolean showCache) {
        if (showCache) {
            String keys = data.getString("tags");
            if (!keys.isEmpty())
                for (String key : keys.split(", ")) currentTip.add(StatCollector.translateToLocal(key));
        } else {
            currentTip.add(localizeKeyList(data.getString("family")));
        }
        int duration = data.getInteger("duration");
        if (!data.getBoolean("connected")) {
            // #tr tst.bottledchaos.credential_hatch.disconnected
            // # Not connected to a formed Bottled Chaos controller.
            // #zh_CN 未连接到成型的瓶中混沌主机.
            currentTip.add(StatCollector.translateToLocal("tst.bottledchaos.credential_hatch.disconnected"));
            if (!showCache) return;
        } else {
            currentTip.add(
                "#" + data.getInteger("slot")
                    + " "
                    + getStatusColor(data.getString("status"))
                    + getWailaStatusText(data)
                    + EnumChatFormatting.RESET);
            int boundSlots = data.getInteger("boundSlots");
            String blockedRoute = getBlockedRouteText(data);
            if (!blockedRoute.isEmpty()) currentTip.add(blockedRoute);
            if (boundSlots != 0) currentTip.add(getCredentialHatchBoundInputs(boundSlots));
            BigInteger eut = processingGroupPower(data);
            byte energyTier = (byte) data.getLong("energyTier");
            // #tr tst.bottledchaos.credential_hatch.power_share
            // # Power share: %s%%
            // #zh_CN 电力份额: %s%%
            currentTip.add(
                (energyTier > 0
                    ? StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.use_with_amperage",
                        formatNumber(eut),
                        processingGroupAmperage(data),
                        GTUtility.getColoredTierNameFromTier(energyTier))
                    : StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.use",
                        formatNumber(eut),
                        GTUtility.getColoredTierNameFromVoltage(eut.longValue())))
                    + " | "
                    + StatCollector.translateToLocalFormatted(
                        "tst.bottledchaos.credential_hatch.power_share",
                        String.format(Locale.ROOT, "%.2f", data.getDouble("powerRatio") * 100)));
            if (duration > 0) {
                currentTip.add(GTWaila.getMachineProgressString(duration, data.getInteger("progress")));
            }
        }
        NBTTagCompound outputs = showCache ? data.getCompoundTag("cache") : data.getCompoundTag("outputs");
        if (showCache) {
            // #tr tst.bottledchaos.credential_hatch.cached_materials
            // # Cached materials (%s types):
            // #zh_CN 缓存材料(%s种):
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.credential_hatch.cached_materials",
                    formatNumber(outputs.getInteger("total"))));
            currentTip.addAll(getMaterialSummaryLines(outputs, true));
        } else {
            appendMaterialSummary(currentTip, getProcessingGroupOutputTitle(data), outputs);
            // #tr tst.bottledchaos.intermediate_outputs
            // # Intermediate products:
            // #zh_CN 中间产物:
            appendMaterialSummary(
                currentTip,
                StatCollector.translateToLocal("tst.bottledchaos.intermediate_outputs"),
                outputs.getCompoundTag("intermediates"));
            // #tr tst.bottledchaos.byproducts
            // # Byproducts:
            // #zh_CN 副产物:
            appendMaterialSummary(
                currentTip,
                StatCollector.translateToLocal("tst.bottledchaos.byproducts"),
                outputs.getCompoundTag("byproducts"));
        }
    }

    @SideOnly(Side.CLIENT)
    private static void appendMaterialSummary(List<String> lines, String title, NBTTagCompound materials) {
        if (materials.getInteger("total") <= 0) return;
        lines.add(title);
        lines.addAll(getMaterialSummaryLines(materials, true));
    }

    @SideOnly(Side.CLIENT)
    public static List<String> getMaterialSummaryLines(NBTTagCompound materials, boolean wailaIcons) {
        List<String> lines = new ArrayList<>();
        NBTTagList entries = materials.getTagList("entries", 10);
        int shown = 0;
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            ItemStack item = entry.hasKey("item", 10) ? ItemStack.loadItemStackFromNBT(entry.getCompoundTag("item"))
                : null;
            FluidStack fluid = entry.hasKey("fluid", 10)
                ? FluidStack.loadFluidStackFromNBT(entry.getCompoundTag("fluid"))
                : null;
            if (item == null && fluid == null) continue;
            lines.add(
                (wailaIcons
                    ? "  "
                        + TTRenderStack.create(item != null ? item : GTUtility.getFluidDisplayStack(fluid, false), true)
                    : "") + formatGroupOutput(item, fluid, entry.getLong("amount")));
            shown++;
        }
        if (materials.getInteger("total") > shown) lines.add(
            StatCollector
                .translateToLocalFormatted("GT5U.waila.producing.andmore", materials.getInteger("total") - shown));
        return lines;
    }

    public static String getWailaStatusText(NBTTagCompound job) {
        return BottledChaosResultData.displayText(job, "status");
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
        return isFailure(global) && (processingGroupMask & (1 << slot)) != 0 ? global : threads[slot].result();
    }

    public static String formatProcessingGroups(int mask) {
        StringJoiner groups = new StringJoiner(", ");
        for (int slot = 0; slot < THREAD_COUNT; slot++) if ((mask & (1 << slot)) != 0) groups.add("#" + (slot + 1));
        return groups.toString();
    }

    public int getRecipePoolUsers(int poolSlot) {
        if (poolUsersDirty) {
            Arrays.fill(recipePoolUsers, 0);
            missingRecipePools = 0;
            long installed = machineConfiguration.installedPoolMask();
            var parameters = machineConfiguration.runtime(
                machineParameterContext(
                    Math.max(32, parameterVoltage()),
                    Math.max(32, inputPower),
                    getCentralSupplyItems(),
                    getCentralSupplyFluids()));
            for (int slot = 0; slot < THREAD_COUNT; slot++) {
                long required = threads[slot].isBusy() ? threads[slot].requiredPools
                    : BottledChaosRecipeProcessor.displayPoolMask(
                        ItemBottledChaosCredential.getRouteMask(installedCredentials[slot]),
                        parameters);
                required = BottledChaosMachineConfiguration.withDependencies(required);
                for (int pool = 0; pool < BottledChaosMachineConfiguration.poolCount(); pool++) {
                    if ((required & (1L << pool)) == 0) continue;
                    int display = BottledChaosMachineConfiguration.displayPoolSlot(pool);
                    recipePoolUsers[display] |= 1 << slot;
                    if ((installed & (1L << pool)) == 0) missingRecipePools |= 1L << display;
                }
            }
            poolUsersDirty = false;
        }
        return recipePoolUsers[poolSlot];
    }

    public long getMissingRecipePoolMask() {
        getRecipePoolUsers(0);
        return missingRecipePools;
    }

    public NBTTagCompound getModuleParameters(int slot, int source) {
        List<BottledChaosUpkeep.Demand> demands = new ArrayList<>();
        for (int group = 0; group < THREAD_COUNT; group++)
            if (hasAuthorizedWork(group)) demands.addAll(threads[group].upkeep);
        return machineConfiguration.moduleParameters(
            slot,
            source,
            machineParameterContext(
                parameterVoltage(),
                Math.max(0, inputPower),
                getCentralSupplyItems(),
                getCentralSupplyFluids()),
            demands);
    }

    public String getPassiveInputText() {
        if (getBaseMetaTileEntity() == null || !getBaseMetaTileEntity().isActive())
            return StatCollector.translateToLocal("tst.bottledchaos.gui.no_passive_inputs");
        StringJoiner text = new StringJoiner("\n");
        text.add(structureUpkeep.activeFluidText());
        java.util.Map<com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID, java.math.BigInteger> items = new java.util.LinkedHashMap<>();
        for (int group = 0; group < THREAD_COUNT; group++) if (advancing[group]) {
            for (var demand : threads[group].roundItems) items.merge(
                com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID.create(demand.item()),
                java.math.BigInteger.valueOf(demand.amount()),
                java.math.BigInteger::add);
        }
        items.forEach(
            (item, amount) -> text.add(
                StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.gui.per_round",
                    item.getItemStack(1)
                        .getDisplayName(),
                    formatNumber(amount))));
        String result = text.toString()
            .trim();
        return result.isEmpty() ? StatCollector.translateToLocal("tst.bottledchaos.gui.no_passive_inputs") : result;
    }

    public NBTTagCompound getBottledChaosStatusSummary() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        long tick = base == null ? schedulerTick
            : base.getWorld()
                .getTotalWorldTime();
        if (statusSummary != null && statusSummaryTick == tick) return statusSummary;
        statusSummary = buildBottledChaosStatusSummary();
        statusSummaryTick = tick;
        return statusSummary;
    }

    private NBTTagCompound buildBottledChaosStatusSummary() {
        NBTTagCompound summary = new NBTTagCompound();
        summary.setBoolean("wireless", isWirelessMode());
        NBTTagList entries = new NBTTagList();
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (!mMachine) BottledChaosResultData
            .addGrouped(entries, SimpleResultWithText.ofFailure("GT5U.gui.multimachine.incomplete_structure"), 0);
        if (overCapacity) BottledChaosResultData
            .addGrouped(entries, SimpleResultWithText.ofFailure("tst.bottledchaos.result.capacity_exceeded"), 0);
        if (shouldCheckMaintenance() && getRepairStatus() < getIdealStatus()) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString("key", "GT5U.turbine.maintenance.true");
            entries.appendTag(entry);
        }
        CheckRecipeResult global = getGlobalFailure();
        if (mMachine && isFailure(global)) BottledChaosResultData.addGrouped(entries, global, processingGroupMask);
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
                        if (threads[slot].progress < threads[slot].duration && !isWaitingForPowerRound(slot)) {
                            usage += threads[slot].eut;
                            usageGroups |= 1 << slot;
                        }
                        result = getThreadDisplayResult(slot);
                    }
                } else {
                    result = getThreadDisplayResult(slot);
                }
                if (isFailure(result)) BottledChaosResultData.addGrouped(entries, result, 1 << slot);
            }
        }
        if (base != null && (base.isAllowedToWork() || mMaxProgresstime > 0) && usage > inputPower)
            BottledChaosResultData.addGrouped(entries, CheckRecipeResultRegistry.insufficientPower(usage), usageGroups);
        summary.setBoolean("errors", entries.tagCount() > 0);
        if (entries.tagCount() == 0) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString(
                "key",
                base != null && !base.isAllowedToWork() && mMaxProgresstime <= 0 ? BottledChaosStatus.PAUSED.key()
                    : "gt.interact.desc.mb.running");
            entries.appendTag(entry);
        }
        summary.setTag("entries", entries);
        return summary;
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
        tag.setInteger(WAILA_CAPACITY, capacity);
        tag.setInteger(WAILA_USED_CAPACITY, getBottledChaosUsedCapacity());
        tag.setInteger(WAILA_CREDENTIAL_HATCHES, getInstalledCredentialHatchCount());
        tag.setInteger(WAILA_CREDENTIAL_HATCH_LIMIT, credentialHatchLimit);
        tag.setString(WAILA_RECIPE_CATEGORIES, getRecipeCategoryKeyList());
        tag.setString(WAILA_RECIPE_TOKENS, getRecipeTokenKeyList());
        tag.setBoolean("bottledChaosOverCapacity", overCapacity);
        tag.setBoolean("bottledChaosWireless", isWirelessMode());
        tag.setTag(THREAD_NBT, getProcessingGroupSummary().getTagList(THREAD_NBT, 10));
    }

    /** Shared server snapshot for the runtime tab and controller Waila. */
    public NBTTagCompound getProcessingGroupSummary() {
        NBTTagCompound summary = new NBTTagCompound();
        summary.setInteger(WAILA_CAPACITY, capacity);
        NBTTagList jobs = new NBTTagList();
        for (int i = 0; i < THREAD_COUNT; i++) {
            if (installedCredentials[i] == null && !threads[i].isBusy()) continue;
            BottledChaosThread thread = threads[i];
            NBTTagCompound job = new NBTTagCompound();
            writeProcessingGroupInformation(job, i);
            ItemStack family = ItemBottledChaosCredential
                .getRecipeFamily(thread.isBusy() ? thread.credential : installedCredentials[i]);
            job.setString("family", ItemBottledChaosCredential.getDisplayRouteKeys(family));
            job.setTag("outputs", thread.getFinalOutputSummary());
            jobs.appendTag(job);
        }
        summary.setTag(THREAD_NBT, jobs);
        return summary;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("bottledChaosWireless"))
            currentTip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tst.bottledchaos.wireless_mode"));
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
                tag.getInteger(WAILA_USED_CAPACITY),
                tag.getInteger(WAILA_CAPACITY)));
        currentTip.add(
            EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                "tst.bottledchaos.credential_hatches",
                tag.getInteger(WAILA_CREDENTIAL_HATCHES),
                tag.getInteger(WAILA_CREDENTIAL_HATCH_LIMIT)));
        if (tag.getBoolean("bottledChaosOverCapacity")) {
            currentTip.add(
                EnumChatFormatting.WHITE + StatCollector.translateToLocal(BottledChaosStatus.CAPACITY_EXCEEDED.key()));
        }
        for (int i = 0; i < THREAD_COUNT; i++) currentTip.addAll(getProcessingGroupLines(tag, i));
    }

    public static int getProcessingGroupCount(NBTTagCompound summary) {
        return summary == null ? 0
            : Math.min(
                THREAD_COUNT,
                summary.getTagList(THREAD_NBT, 10)
                    .tagCount());
    }

    @SideOnly(Side.CLIENT)
    public static List<String> getProcessingGroupLines(NBTTagCompound summary, int index) {
        return getProcessingGroupLines(summary, index, true);
    }

    public static NBTTagCompound getProcessingGroupData(NBTTagCompound summary, int index) {
        return index < 0 || index >= getProcessingGroupCount(summary) ? new NBTTagCompound()
            : summary.getTagList(THREAD_NBT, 10)
                .getCompoundTagAt(index);
    }

    @SideOnly(Side.CLIENT)
    public static List<String> getProcessingGroupLines(NBTTagCompound summary, int index, boolean waila) {
        List<String> lines = new ArrayList<>();
        if (index < 0 || index >= getProcessingGroupCount(summary)) return lines;
        NBTTagList jobs = summary.getTagList(THREAD_NBT, 10);
        NBTTagCompound job = jobs.getCompoundTagAt(index);
        String energy = waila ? "" : getProcessingGroupEnergyText(job);
        lines.add(
            EnumChatFormatting.WHITE + "#"
                + job.getInteger("slot")
                + " "
                + localizeKeyList(job.getString("family"))
                + EnumChatFormatting.WHITE
                + " | "
                + getProcessingGroupShareText(summary, job)
                + (energy.isEmpty() ? "" : EnumChatFormatting.WHITE + " | " + energy)
                + EnumChatFormatting.WHITE
                + " | "
                + getStatusColor(job.getString("status"))
                + getWailaStatusText(job)
                + EnumChatFormatting.RESET);
        String blockedRoute = getBlockedRouteText(job);
        if (!blockedRoute.isEmpty()) lines.add(blockedRoute);
        if (waila && job.getInteger("duration") > 0) {
            lines.add(GTWaila.getMachineProgressString(job.getInteger("duration"), job.getInteger("progress")));
        }
        String outputs = getProcessingGroupOutputText(job, waila);
        if (!outputs.isEmpty()) lines.add(outputs);
        return lines;
    }

    @SideOnly(Side.CLIENT)
    public static String getProcessingGroupShareText(NBTTagCompound summary, NBTTagCompound job) {
        return EnumChatFormatting.AQUA + StatCollector
            .translateToLocalFormatted("tst.bottledchaos.input_share", getProcessingGroupShareValue(job));
    }

    @SideOnly(Side.CLIENT)
    public static String getProcessingGroupShareValue(NBTTagCompound job) {
        return String.format(Locale.ROOT, "%.2f%%", job.getDouble("powerRatio") * 100);
    }

    private static BigInteger processingGroupPower(NBTTagCompound job) {
        byte[] power = job.getByteArray("power");
        return power.length == 0 ? BigInteger.ZERO : new BigInteger(1, power);
    }

    private static BigInteger processingGroupAmperage(NBTTagCompound job) {
        BigInteger voltage = BigInteger.valueOf(GTValues.V[(int) job.getLong("energyTier")]);
        return processingGroupPower(job).add(voltage)
            .subtract(BigInteger.ONE)
            .divide(voltage);
    }

    @SideOnly(Side.CLIENT)
    public static String getProcessingGroupEnergyText(NBTTagCompound job) {
        return processingGroupPower(job).signum() <= 0 ? ""
            : getProcessingGroupPowerText(job) + " " + getProcessingGroupVoltageText(job);
    }

    @SideOnly(Side.CLIENT)
    public static String getProcessingGroupPowerText(NBTTagCompound job) {
        BigInteger eut = processingGroupPower(job);
        if (eut.signum() <= 0) return "";
        return EnumChatFormatting.YELLOW + String.format(Locale.ROOT, "%.1e", eut.doubleValue())
            .replace("e+0", "e")
            .replace("e+", "e") + EnumChatFormatting.WHITE + " EU/t";
    }

    @SideOnly(Side.CLIENT)
    public static String getProcessingGroupVoltageText(NBTTagCompound job) {
        if (processingGroupPower(job).signum() <= 0) return "";
        byte tier = (byte) job.getLong("energyTier");
        return EnumChatFormatting.WHITE + "("
            + processingGroupAmperage(job)
            + "A "
            + GTUtility.getColoredTierNameFromTier(tier)
            + EnumChatFormatting.WHITE
            + ")";
    }

    @SideOnly(Side.CLIENT)
    public static String getBlockedRouteText(NBTTagCompound job) {
        // #tr tst.bottledchaos.partial_route_blocked
        // # Some routes blocked: %s
        // #zh_CN 部分路线受阻: %s
        return job.hasKey("blockedRoute", 10)
            ? EnumChatFormatting.YELLOW
                + StatCollector
                    .translateToLocalFormatted(
                        "tst.bottledchaos.partial_route_blocked",
                        BottledChaosResultData.read(job.getCompoundTag("blockedRoute"))
                            .getDisplayString())
            : "";
    }

    @SideOnly(Side.CLIENT)
    public static String getProcessingGroupOutputTitle(NBTTagCompound job) {
        // #tr tst.bottledchaos.pending_outputs
        // # Pending outputs:
        // #zh_CN 待输出:
        return StatCollector.translateToLocal(
            job.getInteger("duration") > 0 && job.getInteger("progress") >= job.getInteger("duration")
                ? "tst.bottledchaos.pending_outputs"
                : "GT5U.waila.producing");
    }

    @SideOnly(Side.CLIENT)
    public static String getProcessingGroupOutputText(NBTTagCompound job) {
        return getProcessingGroupOutputText(job, false);
    }

    @SideOnly(Side.CLIENT)
    private static String getProcessingGroupOutputText(NBTTagCompound job, boolean waila) {
        String outputs = formatGroupOutputs(job.getCompoundTag("outputs"), waila);
        return outputs.isEmpty() ? "" : EnumChatFormatting.WHITE + getProcessingGroupOutputTitle(job) + " " + outputs;
    }

    @SideOnly(Side.CLIENT)
    private static String formatGroupOutputs(NBTTagCompound outputs, boolean waila) {
        NBTTagList entries = outputs.getTagList("entries", 10);
        StringJoiner text = new StringJoiner(EnumChatFormatting.WHITE + ", ");
        int shown = 0;
        int limit = waila ? BottledChaosMaterialSummary.GROUP_OUTPUT_LIMIT : entries.tagCount();
        for (int i = 0; i < Math.min(limit, entries.tagCount()); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            ItemStack item = entry.hasKey("item", 10) ? ItemStack.loadItemStackFromNBT(entry.getCompoundTag("item"))
                : null;
            FluidStack fluid = entry.hasKey("fluid", 10)
                ? FluidStack.loadFluidStackFromNBT(entry.getCompoundTag("fluid"))
                : null;
            if (item == null && fluid == null) continue;
            text.add(formatGroupOutput(item, fluid, entry.getLong("amount")));
            shown++;
        }
        if (outputs.getInteger("total") > shown) text.add(
            waila
                ? StatCollector
                    .translateToLocalFormatted("GT5U.waila.producing.andmore", outputs.getInteger("total") - shown)
                : "...");
        return text.toString();
    }

    @SideOnly(Side.CLIENT)
    public static String formatGroupOutput(ItemStack item, FluidStack fluid, long amount) {
        return EnumChatFormatting.AQUA + (item != null ? item.getDisplayName() : fluid.getLocalizedName())
            + EnumChatFormatting.WHITE
            + " x "
            + EnumChatFormatting.GOLD
            + formatNumber(amount)
            + (fluid == null ? "" : "L")
            + EnumChatFormatting.RESET;
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
        long mask = 0;
        for (ItemStack credential : installedCredentials) {
            mask |= ItemBottledChaosCredential.getRouteMask(credential);
        }
        for (int index : CredentialModel.displayOrder()) {
            if ((mask & (1L << index)) != 0) joiner.add(CredentialModel.translationKey(index));
        }
        return joiner.length() == 0 ? "GT5U.gui.text.nil" : joiner.toString();
    }

    public static String localizeKeyList(String keys) {
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
        return BottledChaosStatus.fromKey(status)
            .textColor();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        if (tooltip == null) {
            tooltip = new MultiblockTooltipBuilder()
                .addMachineType(TSTUtils.tr("tst.bottledchaos.tooltip.machine_type"))
                .addInfo(TSTUtils.tr("tst.bottledchaos.tooltip.info.01"))
                .addInfo(TSTUtils.tr("tst.bottledchaos.tooltip.info.02"))
                .addInfo(TSTUtils.tr("tst.bottledchaos.tooltip.wireless"))
                .addSeparator()
                .beginStructureBlock(29, 41, 29, false)
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.body"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.capacity_columns"))
                .addStructureInfo(TSTUtils.tr("tst.bottledchaos.structure.capacity_channel"))
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
