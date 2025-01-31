package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_PerPiece_GiantVacuumDryingFurnace;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedBonus_MultiplyPerVoltageTier_GiantVacuumDryingFurnace;
import static com.Nxer.TwistSpaceTechnology.config.Config.SpeedMultiplier_CoilTier_GiantVacuumDryingFurnace;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontCenter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_GiantVacuumDryingFurnace extends GTCM_MultiMachineBase<TST_GiantVacuumDryingFurnace> {

    public int machineMode = 0;
    private static final int MACHINEMODE_VACUUMFURNACE = 0;
    private static final int MACHINEMODE_DEHYDRATOR = 1;
    private int piece = 1;
    private int parallel = 1;
    private float speedBonus = 1;
    private HeatingCoilLevel coilLevel = HeatingCoilLevel.None;

    // region constructor
    public TST_GiantVacuumDryingFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_GiantVacuumDryingFurnace(String aName) {
        super(aName);
    }
    // endregion

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return parallel;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return false;
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.GTPP_MULTI_INDUSTRIAL_DEHYDRATOR.mode." + machineMode);
    }

    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.machineMode = (this.machineMode + 1) % 2;
        PlayerUtils.messagePlayer(
            aPlayer,
            String.format(StatCollector.translateToLocal("GT5U.MULTI_MACHINE_CHANGE"), getMachineModeName()));
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        onModeChangeByScrewdriver(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return (machineMode == MACHINEMODE_VACUUMFURNACE) ? GTPPRecipeMaps.vacuumFurnaceRecipes
            : GTPPRecipeMaps.chemicalDehydratorNonCellRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTPPRecipeMaps.chemicalDehydratorNonCellRecipes, GTPPRecipeMaps.vacuumFurnaceRecipes);
    }

    // region Processing Logic

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("piece", piece);
        aNBT.setInteger("parallel", parallel);
        aNBT.setFloat("speedBonus", speedBonus);
        // Migrates old NBT tag to the new one
        if (aNBT.hasKey("mDehydratorMode")) {
            machineMode = aNBT.getBoolean("mDehydratorMode") ? MACHINEMODE_DEHYDRATOR : MACHINEMODE_VACUUMFURNACE;
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        piece = aNBT.getInteger("piece");
        parallel = aNBT.getInteger("parallel");
        speedBonus = aNBT.getFloat("speedBonus");
        // Migrates old NBT tag to the new one
        if (aNBT.hasKey("mDehydratorMode")) {
            machineMode = aNBT.getBoolean("mDehydratorMode") ? MACHINEMODE_DEHYDRATOR : MACHINEMODE_VACUUMFURNACE;
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("mode", machineMode);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal(
                    "GT5U.GTPP_MULTI_INDUSTRIAL_DEHYDRATOR.mode."
                        + (tag.getBoolean("mode") ? MACHINEMODE_DEHYDRATOR : MACHINEMODE_VACUUMFURNACE))
                + EnumChatFormatting.RESET);
    }

    private static IStructureDefinition<TST_GiantVacuumDryingFurnace> STRUCTURE_DEFINITION = null;

    // region Structure

    // 'S' = Stainless casing
    // 'A' = Glasses
    // 'C' = Coil
    // 'V' = Chemically inert casing
    // 'E' = Ir casing
    // 'F' = Frozen casing
    // 'G' = PTFE
    // 'I' = Hot proof machine casing
    // 'J' = Vacuum casing
    // 'H' = Invar Frame

    // VacuumPump height = 8 , length = width = 9 , and x-OffSet = 10 , y-OffSet = 4 ;
    private static final String[][] shapeVacuumPump = new String[][] {
        { "         ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ",
            "         " },
        { "         ", " SAAAAAS ", " S     S ", " S     S ", " S     S ", " S     S ", " S     S ", " SAAAAAS ",
            "         " },
        { "         ", " SAAAAAS ", " S     S ", " S     S ", " S     S ", " S     S ", " S     S ", " SAAAAAS ",
            "         " },
        { "         ", " SAAAAAS ", " S     S ", " S     S ", " S     S ", " S     S ", " S     S ", " SAAAAAS ",
            "         " },
        { "         ", " SAAAAAS ", " S     S ", " S     S ", " S     S ", " S     S ", " S     S ", " SAAAAAS ",
            "         " },
        { "         ", " SAAAAAS ", " S     S ", " S     S ", " S     S ", " S     S ", " S     S ", " SAAAAAS ",
            "         " },
        { "         ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ", " SSSSSSS ",
            "         " },
        { "VVVVVVVVV", "VVVVVVVVV", "VVVVVVVVV", "VVVVVVVVV", "VVVVVVVVV", "VVVVVVVVV", "VVVVVVVVV", "VVVVVVVVV",
            "VVVVVVVVV" } };

    // mainFrame height = 7 , length = 9 , width = 3 ,and x-OffSet = 1 , y-OffSet = 3 ;
    private static final String[][] shapeMainFrame = new String[][] {
        { "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE" },
        { "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE" },
        { "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE" },
        { "E~E", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE" },
        { "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE" },
        { "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE" },
        { "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE", "EEE" } };

    // dryingTowerBase height = 5 , length = width = 11 , x-os = -2 , y-os = 1;
    private static final String[][] shapeDryingTowerBase = new String[][] {
        { "           ", " IIIIIIIII ", " IJJJAJJJI ", " IJJJAJJJI ", " IJJJAJJJI ", " AAAAGAAAA ", " IJJJAJJJI ",
            " IJJJAJJJI ", " IJJJAJJJI ", " IIIIIIIII ", "           " },
        { "           ", " H       H ", "           ", "  FFFFFFF  ", "     V     ", "    VGV    ", "     V     ",
            "  FFFFFFF  ", "           ", " H       H ", "           " },
        { "           ", " H       H ", "  FFFFFFF  ", " FGGGGGGGF ", "  FFFFFFF  ", "           ", "  FFFFFFF  ",
            " FGGGGGGGF ", "  FFFFFFF  ", " H       H ", "           " },
        { "  HHHHHHH  ", " H       H ", "H         H", "H FFFFFFF H", "H         H", "H         H", "H         H",
            "H FFFFFFF H", " H       H ", " H       H ", "  HHHHHHH  " },
        { " VVVVVVVVV ", "VVVVVVVVVVV", "VVVVVVVVVVV", "VVVVVVVVVVV", "VVVVVVVVVVV", "VVVVVVVVVVV", "VVVVVVVVVVV",
            "VVVVVVVVVVV", "VVVVVVVVVVV", "VVVVVVVVVVV", " VVVVVVVVV " } };
    // dryingTowerMiddle height = 7 , length = width = 11 , x-os = -2 , y-os = 1+7n ; n is the number of this part
    private static final String[][] shapeDryingTowerMiddle = new String[][] {
        { "           ", "   I   I   ", "  JJJAJJJ  ", " IJJJAJJJI ", "  JJJAJJJ  ", " AAAAGAAAA ", "  JJJAJJJ  ",
            " IJJJAJJJI ", "  JJJAJJJ  ", "   I   I   ", "           " },
        { "           ", "   I   I   ", "           ", " I C   C I ", "           ", "     G     ", "           ",
            " I C   C I ", "           ", "   I   I   ", "           " },
        { "           ", "   I   I   ", "   C   C   ", " IC C C CI ", "   C   C   ", "     G     ", "   C   C   ",
            " IC C C CI ", "   C   C   ", "   I   I   ", "           " },
        { "           ", "   I   I   ", "  CCC CCC  ", " IC C C CI ", "  CCC CCC  ", "     G     ", "  CCC CCC  ",
            " IC C C CI ", "  CCC CCC  ", "   I   I   ", "           " },
        { "           ", "   I   I   ", "  CCC CCC  ", " IC C C CI ", "  CCC CCC  ", "     G     ", "  CCC CCC  ",
            " IC C C CI ", "  CCC CCC  ", "   I   I   ", "           " },
        { "           ", "   I   I   ", "   C   C   ", " IC C C CI ", "   C   C   ", "     G     ", "   C   C   ",
            " IC C C CI ", "   C   C   ", "   I   I   ", "           " },
        { "           ", "   I   I   ", "           ", " I C   C I ", "           ", "     G     ", "           ",
            " I C   C I ", "           ", "   I   I   ", "           " } };
    // dryingTowerMiddle height = 3 , length = width = 11 , x-os = -2 , y-os = 4+7n;
    private static final String[][] shapeDryingTowerHat = new String[][] {
        { "           ", "           ", "           ", "    VVV    ", "   VGGGV   ", "   VGGGV   ", "   VGGGV   ",
            "    VVV    ", "           ", "           ", "           " },
        { "           ", "           ", "   I   I   ", "  IV   VI  ", "     V     ", "    VGV    ", "     V     ",
            "  IV   VI  ", "   I   I   ", "           ", "           " },
        { "           ", "           ", "   I   I   ", "  I     I  ", "           ", "     G     ", "           ",
            "  I     I  ", "   I   I   ", "           ", "           " } };

    private final int VP_horizontalOffSet = 10;
    private final int VP_verticalOffSet = 4;
    private final int VP_depthOffSet = 0;

    // MF
    private final int MF_horizontalOffSet = 1;
    private final int MF_verticalOffSet = 3;
    private final int MF_depthOffSet = 0;

    // DT
    private final int DT_horizontalOffSet = -2;
    private final int DT_verticalOffSet = 1;
    private final int DT_depthOffSet = 0;

    private static final String STRUCTURE_PIECE_VP = "VacuumPump";
    private static final String STRUCTURE_PIECE_MF = "MainFrame";
    private static final String STRUCTURE_PIECE_DTB = "DryingTowerBase";
    private static final String STRUCTURE_PIECE_DTM = "DryingTowerMiddle";
    private static final String STRUCTURE_PIECE_DTH = "DryingTowerHat";

    @Override
    public IStructureDefinition<TST_GiantVacuumDryingFurnace> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_GiantVacuumDryingFurnace>builder()
                .addShape(STRUCTURE_PIECE_VP, transpose(shapeVacuumPump))
                .addShape(STRUCTURE_PIECE_MF, transpose(shapeMainFrame))
                .addShape(STRUCTURE_PIECE_DTB, transpose(shapeDryingTowerBase))
                .addShape(STRUCTURE_PIECE_DTM, transpose(shapeDryingTowerMiddle))
                .addShape(STRUCTURE_PIECE_DTH, transpose(shapeDryingTowerHat))

                .addElement('A', Glasses.chainAllGlasses())
                .addElement('S', ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement('V', ofBlock(GregTechAPI.sBlockCasings8, 0))
                .addElement('I', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings2, 1))
                .addElement('H', ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement('J', ofBlock(ModBlocks.blockCasings4Misc, 10))

                .addElement(
                    'C',
                    withChannel(
                        "coil",
                        ofCoil(TST_GiantVacuumDryingFurnace::setCoilLevel, TST_GiantVacuumDryingFurnace::getCoilLevel))) // Coil

                .addElement(
                    'E',
                    HatchElementBuilder.<TST_GiantVacuumDryingFurnace>builder()
                        .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy), InputHatch, OutputBus)
                        .adder(TST_GiantVacuumDryingFurnace::addToMachineList)
                        .dot(1)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int piece = stackSize.stackSize;

        // Build Vacuum Pump (VP) structure
        this.buildPiece(
            STRUCTURE_PIECE_VP,
            stackSize,
            hintsOnly,
            VP_horizontalOffSet,
            VP_verticalOffSet,
            VP_depthOffSet);

        // Build Main Frame (MF) structure
        this.buildPiece(
            STRUCTURE_PIECE_MF,
            stackSize,
            hintsOnly,
            MF_horizontalOffSet,
            MF_verticalOffSet,
            MF_depthOffSet);

        // Build Drying Tower Base (DTB) structure
        this.buildPiece(
            STRUCTURE_PIECE_DTB,
            stackSize,
            hintsOnly,
            DT_horizontalOffSet,
            DT_verticalOffSet,
            DT_depthOffSet);

        // Build Drying Tower Middle (DTM) structures
        for (int pointer = 0; pointer < piece; pointer++) {
            this.buildPiece(
                STRUCTURE_PIECE_DTM,
                stackSize,
                hintsOnly,
                DT_horizontalOffSet,
                DT_verticalOffSet + 7 * pointer + 7,
                DT_depthOffSet);
        }

        // Build Drying Tower Hat (DTH) structure
        this.buildPiece(
            STRUCTURE_PIECE_DTH,
            stackSize,
            hintsOnly,
            DT_horizontalOffSet,
            DT_verticalOffSet + 7 * piece + 3,
            DT_depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;

        int[] built = new int[stackSize.stackSize + 5];
        int piece = stackSize.stackSize;

        // Survival build Vacuum Pump (VP) structure
        built[0] = survivialBuildPiece(
            STRUCTURE_PIECE_VP,
            stackSize,
            VP_horizontalOffSet,
            VP_verticalOffSet,
            VP_depthOffSet,
            elementBudget,
            env,
            false,
            true);

        // Survival build Main Frame (MF) structure
        built[1] = survivialBuildPiece(
            STRUCTURE_PIECE_MF,
            stackSize,
            MF_horizontalOffSet,
            MF_verticalOffSet,
            MF_depthOffSet,
            elementBudget,
            env,
            false,
            true);

        // Survival build Drying Tower Base (DTB) structure
        built[2] = survivialBuildPiece(
            STRUCTURE_PIECE_DTB,
            stackSize,
            DT_horizontalOffSet,
            DT_verticalOffSet,
            DT_depthOffSet,
            elementBudget,
            env,
            false,
            true);

        // Survival build Drying Tower Middle (DTM) structures
        for (int pointer = 0; pointer < piece; pointer++) {
            built[3 + pointer] = survivialBuildPiece(
                STRUCTURE_PIECE_DTM,
                stackSize,
                DT_horizontalOffSet,
                DT_verticalOffSet + 7 * pointer + 7,
                DT_depthOffSet,
                elementBudget,
                env,
                false,
                true);
        }

        // Survival build Drying Tower Hat (DTH) structure
        built[built.length - 1] = survivialBuildPiece(
            STRUCTURE_PIECE_DTH,
            stackSize,
            DT_horizontalOffSet,
            DT_verticalOffSet + 7 * piece + 3,
            DT_depthOffSet,
            elementBudget,
            env,
            false,
            true);

        return TstUtils.multiBuildPiece(built);
    }

    // endregion

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.coilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public int getCoilTier() {
        return TstUtils.getVoltageForCoil(coilLevel);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_IndustrialAlchemyTower_MachineType
        // # Vacuum Furnace | Dehydrator
        // #zh_CN 真空干燥炉 | 化学脱水机
        tt.addMachineType(TextEnums.tr("Tooltip_GVDF_MachineType"))
            // #tr Tooltip_GVDF_00
            // # Controller block for the Giant Vacuum Drying Furnace
            // #zh_CN 巨型真空干燥炉的控制器方块
            .addInfo(TextEnums.tr("Tooltip_GVDF_00"))
            // #tr Tooltip_GVDF_01
            // # §9Harness the power of vacuum technology for industrial drying!!!
            // #zh_CN §9利用真空技术的力量进行工业干燥!!!
            .addInfo(TextEnums.tr("Tooltip_GVDF_01"))
            // #tr Tooltip_GVDF_02
            // # This machine consists of vacuum pump, main structure, drying tower three parts.
            // #zh_CN 该设备由真空泵、主体结构和干燥塔三部分组成
            .addInfo(TextEnums.tr("Tooltip_GVDF_02"))
            // #tr Tooltip_GVDF_03
            // # parallel = coil * piece * 32
            // #zh_CN 并行 = 线圈等级 * 层数 * 32
            .addInfo(TextEnums.tr("Tooltip_GVDF_03"))
            // #tr Tooltip_GVDF_04
            // # Every additional voltage step reduces the time to 80%% of the original and The processing speed
            // increases by 50%% for each step of the coil
            // #zh_CN 每超出一级电压,所需时间为原来的80%%,线圈每升高一级处理速度增加50%%
            .addInfo(TextEnums.tr("Tooltip_GVDF_04"))
            // #tr Tooltip_GVDF_05
            // # Switch modes using a screwdriver
            // #zh_CN 由螺丝刀切换模式
            .addInfo(TextEnums.tr("Tooltip_GVDF_05"))
            // #tr Tooltip_GVDF_06
            // # Every additional voltage step reduces the time to 80%% of the original
            // #zh_CN 注意你的电量小伙子,巨型真空干燥塔耗能200%%
            .addInfo(TextEnums.tr("Tooltip_GVDF_06"))
            // #tr Tooltip_GVDF_07
            // # Would anyone really need this machine to handle space ice cream?
            // #zh_CN 真的会有人需要这台机器来处理太空冰淇淋吗？
            .addInfo(TextEnums.tr("Tooltip_GVDF_07"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(11, 10, 23, true)
            .addController(textFrontCenter)

            // #tr Tooltip_GVDF_HatchBusInfo
            // # The input and output hatches/buses must be placed in the main frame.
            // #zh_CN 输入和输出舱口必须放置在主框架内
            .addInputHatch(TextEnums.tr("Tooltip_GVDF_HatchBusInfo"))
            .addOutputHatch(TextEnums.tr("Tooltip_GVDF_HatchBusInfo"))
            .addInputBus(TextEnums.tr("Tooltip_GVDF_HatchBusInfo"))
            .addOutputBus(TextEnums.tr("Tooltip_GVDF_HatchBusInfo"))
            // #tr Tooltip_GVDF_HatchBusInfo
            // # The input and output hatches/buses must be placed in the main frame.
            // #zh_CN 能量舱口必须放置在主框架内
            .addEnergyHatch(TextEnums.tr("Tooltip_GVDF_EnergyHatch"))

            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        coilLevel = HeatingCoilLevel.None;

        this.piece = 0;

        if (!checkPiece(STRUCTURE_PIECE_VP, VP_horizontalOffSet, VP_verticalOffSet, VP_depthOffSet)) {
            return false;
        }
        if (!checkPiece(STRUCTURE_PIECE_MF, MF_horizontalOffSet, MF_verticalOffSet, MF_depthOffSet)) {
            return false;
        }
        if (!checkPiece(STRUCTURE_PIECE_DTB, DT_horizontalOffSet, DT_verticalOffSet, DT_depthOffSet)) {
            return false;
        }

        while (checkPiece(
            STRUCTURE_PIECE_DTM,
            DT_horizontalOffSet,
            DT_verticalOffSet + 7 * piece + 7,
            DT_depthOffSet)) {
            this.piece++;
        }

        if (this.piece < 1 || !checkPiece(
            STRUCTURE_PIECE_DTH,
            DT_horizontalOffSet,
            DT_verticalOffSet + 7 * piece + 3,
            DT_depthOffSet)) {
            return false;
        }
        // parallel = piece * 32
        parallel = (int) Math
            .min((long) piece * getCoilTier() * Parallel_PerPiece_GiantVacuumDryingFurnace, Integer.MAX_VALUE);

        // speed bonus = 0.8^voltageTier
        speedBonus = (float) (Math
            .pow(SpeedBonus_MultiplyPerVoltageTier_GiantVacuumDryingFurnace, GTUtility.getTier(this.getMaxInputEu())))
            / (getCoilTier() * SpeedMultiplier_CoilTier_GiantVacuumDryingFurnace);

        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_GiantVacuumDryingFurnace(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {

        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 2)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 2)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 2)) };
    }

}
