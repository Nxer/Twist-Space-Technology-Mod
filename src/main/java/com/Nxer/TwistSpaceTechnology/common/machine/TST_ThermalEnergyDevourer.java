package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization.MachineTooltip.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.AUTHOR;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.loader.Loaders.MAR_Casing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.TSTControllerTextures;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.WirelessEnergyMultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.blocks.BlockCasings8;

@SkipGenerateDescription
public class TST_ThermalEnergyDevourer extends WirelessEnergyMultiMachineBase<TST_ThermalEnergyDevourer> {

    // region Class Constructor
    public TST_ThermalEnergyDevourer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(AUTHOR, ID.NXER);
    }

    public TST_ThermalEnergyDevourer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ThermalEnergyDevourer(this.mName);
    }
    // endregion

    // region Structure
    private static final int horizontalOffSet = 7;
    private static final int verticalOffSet = 36;
    private static final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainThermalEnergyDevourer";
    private static IStructureDefinition<TST_ThermalEnergyDevourer> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_ThermalEnergyDevourer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_ThermalEnergyDevourer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] {
                            { "     CCCCC     ", "   CCEEEEECC   ", "  CEEEEEEEEEC  ", " CEEEEEEEEEEEC ",
                                " CEEEEEEEEEEEC ", "CEEEEEEEEEEEEEC", "CEEEEEEEEEEEEEC", "CEEEEEEEEEEEEEC",
                                "CEEEEEEEEEEEEEC", "CEEEEEEEEEEEEEC", " CEEEEEEEEEEEC ", " CEEEEEEEEEEEC ",
                                "  CEEEEEEEEEC  ", "   CCEEEEECC   ", "     CCCCC     " },
                            { "               ", "       B       ", "       B       ", "       B       ",
                                "     FFBFF     ", "    FFADAFF    ", "    FA F AF    ", " BBBBDF FDBBBB ",
                                "    FA F AF    ", "    FFADAFF    ", "     FFBFF     ", "       B       ",
                                "       B       ", "       B       ", "               " },
                            { "               ", "               ", "       B       ", "       B       ",
                                "       B       ", "      ADA      ", "     A F A     ", "  BBBDF FDBBB  ",
                                "     A F A     ", "      ADA      ", "       B       ", "       B       ",
                                "       B       ", "               ", "               " },
                            { "               ", "               ", "               ", "       B       ",
                                "       B       ", "      ADA      ", "     A F A     ", "   BBDF FDBB   ",
                                "     A F A     ", "      ADA      ", "       B       ", "       B       ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "       B       ",
                                "       B       ", "      ADA      ", "     A F A     ", "   BBDF FDBB   ",
                                "     A F A     ", "      ADA      ", "       B       ", "       B       ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "       B       ", "      ADA      ", "     A F A     ", "    BDF FDB    ",
                                "     A F A     ", "      ADA      ", "       B       ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "       B       ", "      ADA      ", "     A F A     ", "    BDF FDB    ",
                                "     A F A     ", "      ADA      ", "       B       ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "       B       ", "      ADA      ", "     A F A     ", "    BDF FDB    ",
                                "     A F A     ", "      ADA      ", "       B       ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "       D       ", "       F       ", "     DF FD     ",
                                "       F       ", "       D       ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "               ", "       F       ", "      F F      ",
                                "       F       ", "               ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "               ", "       F       ", "      F F      ",
                                "       F       ", "               ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "               ", "      FFF      ", "      F F      ",
                                "      FFF      ", "               ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "      F F      ", "      F F      ", "    FF D FF    ", "      DDD      ",
                                "    FF D FF    ", "      F F      ", "      F F      ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "      F F      ",
                                "       D       ", "       D       ", "   F       F   ", "    DD   DD    ",
                                "   F       F   ", "       D       ", "       D       ", "      F F      ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "      F F      ", "       D       ",
                                "               ", "               ", "  F         F  ", "   D       D   ",
                                "  F         F  ", "               ", "               ", "       D       ",
                                "      F F      ", "               ", "               " },
                            { "               ", "      F F      ", "       D       ", "               ",
                                "               ", "               ", " F           F ", "  D         D  ",
                                " F           F ", "               ", "               ", "               ",
                                "       D       ", "      F F      ", "               " },
                            { "               ", "      F F      ", "       D       ", "               ",
                                "               ", "               ", " F           F ", "  D         D  ",
                                " F           F ", "               ", "               ", "               ",
                                "       D       ", "      F F      ", "               " },
                            { "      FFF      ", "    FF D FF    ", "   F       F   ", "  F         F  ",
                                " F           F ", " F           F ", "F             F", "FD           DF",
                                "F             F", " F           F ", " F           F ", "  F         F  ",
                                "   F       F   ", "    FF D FF    ", "      FFF      " },
                            { "      F F      ", "      DDD      ", "    DD   DD    ", "   D       D   ",
                                "  D         D  ", "  D         D  ", "FD           DF", " D           D ",
                                "FD           DF", "  D         D  ", "  D         D  ", "   D       D   ",
                                "    DD   DD    ", "      DDD      ", "      F F      " },
                            { "      FFF      ", "    FF D FF    ", "   F       F   ", "  F         F  ",
                                " F           F ", " F           F ", "F             F", "FD           DF",
                                "F             F", " F           F ", " F           F ", "  F         F  ",
                                "   F       F   ", "    FF D FF    ", "      FFF      " },
                            { "               ", "      F F      ", "       D       ", "               ",
                                "               ", "               ", " F           F ", "  D         D  ",
                                " F           F ", "               ", "               ", "               ",
                                "       D       ", "      F F      ", "               " },
                            { "               ", "      F F      ", "       D       ", "               ",
                                "               ", "               ", " F           F ", "  D         D  ",
                                " F           F ", "               ", "               ", "               ",
                                "       D       ", "      F F      ", "               " },
                            { "               ", "               ", "      F F      ", "       D       ",
                                "               ", "               ", "  F         F  ", "   D       D   ",
                                "  F         F  ", "               ", "               ", "       D       ",
                                "      F F      ", "               ", "               " },
                            { "               ", "               ", "               ", "      F F      ",
                                "       D       ", "       D       ", "   F       F   ", "    DD   DD    ",
                                "   F       F   ", "       D       ", "       D       ", "      F F      ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "      F F      ", "      F F      ", "    FF D FF    ", "      DDD      ",
                                "    FF D FF    ", "      F F      ", "      F F      ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "               ", "      FFF      ", "      F F      ",
                                "      FFF      ", "               ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "               ", "       F       ", "      F F      ",
                                "       F       ", "               ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "               ", "       F       ", "      F F      ",
                                "       F       ", "               ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "               ", "       D       ", "       F       ", "     DF FD     ",
                                "       F       ", "       D       ", "               ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "       B       ", "      ADA      ", "     A F A     ", "    BDF FDB    ",
                                "     A F A     ", "      ADA      ", "       B       ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "       B       ", "      ADA      ", "     A F A     ", "    BDF FDB    ",
                                "     A F A     ", "      ADA      ", "       B       ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "               ",
                                "       B       ", "      ADA      ", "     A F A     ", "    BDF FDB    ",
                                "     A F A     ", "      ADA      ", "       B       ", "               ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "       B       ",
                                "       B       ", "      ADA      ", "     A F A     ", "   BBDF FDBB   ",
                                "     A F A     ", "      ADA      ", "       B       ", "       B       ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "               ", "       B       ",
                                "       B       ", "      ADA      ", "     A F A     ", "   BBDF FDBB   ",
                                "     A F A     ", "      ADA      ", "       B       ", "       B       ",
                                "               ", "               ", "               " },
                            { "               ", "               ", "       B       ", "       B       ",
                                "       B       ", "      ADA      ", "     A F A     ", "  BBBDF FDBBB  ",
                                "     A F A     ", "      ADA      ", "       B       ", "       B       ",
                                "       B       ", "               ", "               " },
                            { "               ", "       B       ", "       B       ", "       B       ",
                                "     FFBFF     ", "    FFADAFF    ", "    FA F AF    ", " BBBBDF FDBBBB ",
                                "    FA F AF    ", "    FFADAFF    ", "     FFBFF     ", "       B       ",
                                "       B       ", "       B       ", "               " },
                            { "     CC~CC     ", "   CCEEEEECC   ", "  CEEEEEEEEEC  ", " CEEEEEEEEEEEC ",
                                " CEEEEEEEEEEEC ", "CEEEEEEEEEEEEEC", "CEEEEEEEEEEEEEC", "CEEEEEEEEEEEEEC",
                                "CEEEEEEEEEEEEEC", "CEEEEEEEEEEEEEC", " CEEEEEEEEEEEC ", " CEEEEEEEEEEEC ",
                                "  CEEEEEEEEEC  ", "   CCEEEEECC   ", "     CCCCC     " } }))
                .addElement('A', ofBlock(MAR_Casing, 0))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 11))
                .addElement(
                    'C',
                    HatchElementBuilder.<TST_ThermalEnergyDevourer>builder()
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                        .adder(TST_ThermalEnergyDevourer::addToMachineList)
                        .hint(1)
                        .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(1))
                        .buildAndChain(ofBlock(GregTechAPI.sBlockCasings2, 1)))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 8))
                .addElement(
                    'E',
                    HatchElementBuilder.<TST_ThermalEnergyDevourer>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(TST_ThermalEnergyDevourer::addToMachineList)
                        .hint(2)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3))
                        .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 3)))
                .addElement('F', ofFrame(Materials.NaquadahAlloy))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
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
        coefficientMultiplier = 1 + getExtraCoefficientMultiplierByVoltageTier();
        ItemStack controllerSlot = getControllerSlot();
        wirelessMode = controllerSlot != null && controllerSlot.stackSize > 0
            && GTUtility.areStacksEqual(controllerSlot, ItemList.EnergisedTesseract.get(1));

    }
    // endregion

    // region Processing Logic
    private int coefficientMultiplier = 1;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_BENDING };

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.vacuumFreezerRecipes;
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - High Speed
         * 1 - Devourer
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        if (wirelessMode) {
            return TSTSharedLocalization.Waila.Waila_WirelessMode;
        }
        // #tr tst.common.machine.ThermalEnergyDevourer.mode.0
        // # High speed mode
        // #zh_CN 高速模式

        // #tr tst.common.machine.ThermalEnergyDevourer.mode.1
        // # Devourer mode
        // #zh_CN 饕餮模式
        return StatCollector.translateToLocal("tst.common.machine.ThermalEnergyDevourer.mode." + machineMode);
    }

    @Override
    public int getMaxParallelRecipes() {
        if (wirelessMode) return Integer.MAX_VALUE;
        return machineMode == 1 ? ValueEnum.Parallel_HighParallelMode_ThermalEnergyDevourer
            : ValueEnum.Parallel_HighSpeedMode_ThermalEnergyDevourer;
    }

    @Override
    protected float getEuModifier() {
        if (wirelessMode) return 1;
        return machineMode == 0 ? 1 : 1F / coefficientMultiplier;
    }

    @Override
    protected float getSpeedBonus() {
        if (wirelessMode) return 1;
        return machineMode == 1 ? 1 : 0.5F / coefficientMultiplier;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    public boolean getDefaultInputSeparationMode() {
        return false;
    }

    @Override
    public ButtonWidget createModeSwitchButton(IWidgetBuilder<?> builder) {
        ButtonWidget modeSwitchButton = super.createModeSwitchButton(builder);
        modeSwitchButton
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(() -> wirelessMode, (w) -> wirelessMode = w), builder)
            .setEnabled(widget -> !wirelessMode);
        return modeSwitchButton;
    }

    @Override
    public boolean showModeInWaila() {
        return !wirelessMode;
    }

    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        // #tr tst.common.machine.ThermalEnergyDevourer.info.coefficient_multiplier
        // # {\AQUA}Coefficient Multiplier: {\GOLD}%s
        // #zh_CN {\AQUA}效率倍率: {\GOLD}%s
        ret[origin.length] = TSTUtils
            .tr("tst.common.machine.ThermalEnergyDevourer.info.coefficient_multiplier", this.coefficientMultiplier);
        return ret;
    }

    @Override
    public int getWirelessModeProcessingTime() {
        return ValueEnum.TickPerProgressing_WirelessMode_ThermalEnergyDevourer;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    public int getExtraCoefficientMultiplierByVoltageTier() {
        return (int) TSTUtils.calculateVoltageTier(getMaxInputEu());
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setInteger("coefficientMultiplier", coefficientMultiplier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getByte("mode");
        coefficientMultiplier = aNBT.getInteger("coefficientMultiplier");
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
            casingTexturePages[0][17],
            OVERLAY_FRONT_VACUUM_FREEZER,
            OVERLAY_FRONT_VACUUM_FREEZER_GLOW,
            OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE,
            OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW);
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.machine_type
        // # Vacuum Freezer
        // #zh_CN 真空冷冻机
        tt.addMachineType(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.machine_type"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.controller
            // # Controller block for the Thermal Energy Devourer
            // #zh_CN 热能饕餮的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.controller"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.01
            // # {\WHITE}{\BOLD}{\ITALIC} It's White Album season again.
            // #zh_CN {\WHITE}{\BOLD}{\ITALIC} 又到了白色相簿的季节.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.01"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.02
            // # The thermal energy it greedily devours.
            // #zh_CN 它贪婪地吞噬着热量.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.02"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.03
            // # But overall it's still a Freezer, and works well.
            // #zh_CN 但总的来说仍是一台冷冻机, 而且工作得不错.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.03"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.04
            // # Increasing the input power will also increase the operating efficiency.
            // #zh_CN 提高输入功率的同时会提高运行效率.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.04"))
            .addInfo(TSTSharedLocalization.MachineTooltip.textScrewdriverChangeMode)
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.05
            // # {\GOLD} ----- High Speed Mode -----
            // #zh_CN {\GOLD} ----- 高速模式 -----
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.05"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.06
            // # Operating efficiency decrease the progress time directly.
            // #zh_CN 运行效率直接降低处理耗时.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.06"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.07
            // # {\AQUA}1024x{\GRAY} parallel.
            // #zh_CN {\AQUA}1024x{\GRAY} 并行.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.07"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.08
            // # {\GOLD} ----- High Parallel Mode -----
            // #zh_CN {\GOLD} ----- 饕餮模式 -----
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.08"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.09
            // # Operating efficiency decrease the EU cost.
            // #zh_CN 运行效率降低EU消耗.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.09"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.10
            // # Almost {\AQUA}infinite parallel{\GRAY}.
            // #zh_CN 几乎无限的并行.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.10"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.11
            // # {\GOLD} ----- Wireless Mode -----
            // #zh_CN {\GOLD} ----- 无线模式 -----
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.11"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.12
            // # Put {\AQUA}Energised Tesseract{\GRAY} into controller slot to turn into Wireless mode.
            // #zh_CN 在控制器内放入{\AQUA}充能超立方体{\GRAY}以切换至无线模式.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.12"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.13
            // # Directly consume required EU from wireless EU network.
            // #zh_CN 直接在无线EU网络中消耗所需的能量.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.13"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.info.14
            // # Progress time is fixed at {\GOLD}6.4s{\GRAY}.
            // #zh_CN 处理时间固定为{\GOLD}6.4s{\GRAY}.
            .addInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.info.14"))
            // #tr tst.common.machine.ThermalEnergyDevourer.tooltip.structure.01
            // # Check whether turn into Wireless mode when checking structure.
            // #zh_CN 检查结构时检测是否进入无线模式.
            .addStructureInfo(TSTUtils.tr("tst.common.machine.ThermalEnergyDevourer.tooltip.structure.01"))
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .beginStructureBlock(15, 37, 15, false)
            .addController(TSTSharedLocalization.Structure.textFrontBottom)
            .addInputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputHatch(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addInputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addOutputBus(TSTSharedLocalization.Structure.textUseBlueprint, 1)
            .addEnergyHatch(TSTSharedLocalization.Structure.textUseBlueprint, 2)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
