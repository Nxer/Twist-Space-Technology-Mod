package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EuModifier_VacuumFilterExtractor;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.text.TextLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.blocks.BlockCasings8;
import tectech.thing.block.BlockQuantumGlass;

// 真空抽滤器
@SkipGenerateDescription
public class TST_VacuumFilterExtractor extends GTCM_MultiMachineBase<TST_VacuumFilterExtractor> {

    // region Class Constructor
    public TST_VacuumFilterExtractor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public TST_VacuumFilterExtractor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_VacuumFilterExtractor(this.mName);
    }
    // endregion

    // region Structure
    private final int horizontalOffSet = 6;
    private final int verticalOffSet = 20;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainVacuumFilterExtractor";
    private static IStructureDefinition<TST_VacuumFilterExtractor> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] SHAPE = new String[][]{
        {"             ","   CCCCCCC   ","  CCCCCCCCC  "," CCCCCCCCCCC ","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC"," CCCCCCCCCCC ","  CCCCCCCCC  ","   CCCCCCC   "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G         G ","      F      ","     FEF     ","      F      "," G         G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G         G ","      A      ","     AEA     ","      A      "," G         G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G   AAA   G ","    AA AA    ","    A   A    ","    AA AA    "," G   AAA   G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","      A      "," G  AA AA  G ","    A   A    ","   A     A   ","    A   A    "," G  AA AA  G ","      A      ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","     AAA     "," G  A   A  G ","   A     A   ","   A     A   ","   A     A   "," G  A   A  G ","     AAA     ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","      A      "," G  AA AA  G ","    A   A    ","   A     A   ","    A   A    "," G  AA AA  G ","      A      ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G   AAA   G ","    AA AA    ","    A   A    ","    AA AA    "," G   AAA   G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G         G ","      A      ","     AEA     ","      A      "," G         G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G         G ","      F      ","     FEF     ","      F      "," G         G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"             ","             ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"     CCC     ","     CCC     ","    G   G    ","             ","             "," G    B    G ","     BDB     ","    BDEDB    ","     BDB     "," G    B    G ","             ","             ","    G   G    ","             "},
        {"     C~C     ","   CCCCCCC   ","  CCCCCCCCC  "," CCCCCCCCCCC ","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC"," CCCCCCCCCCC ","  CCCCCCCCC  ","   CCCCCCC   "},
        {"     CCC     ","   CCCCCCC   ","  CCCCCCCCC  "," CCCCCCCCCCC ","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC","CCCCCCCCCCCCC"," CCCCCCCCCCC ","  CCCCCCCCC  ","   CCCCCCC   "}
    };

    /*
    Blocks:
A -> ofBlock...(gt.blockcasings2, 8, ...);
B -> ofBlock...(gt.blockcasings4, 10, ...); // IO hatch
C -> ofBlock...(gt.blockcasings8, 3, ...);  // energy hatch
D -> ofBlock...(gt.blockcasings9, 0, ...);
E -> ofBlock...(gt.blockcasingsTT, 8, ...);
F -> ofBlock...(tile.quantumGlass, 0, ...);
G -> ofFrame...(Materials.Neutronium);
     */
    // spotless:on

    @Override
    public IStructureDefinition<TST_VacuumFilterExtractor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_VacuumFilterExtractor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings2, 8))
                .addElement(
                    'B',
                    HatchElementBuilder.<TST_VacuumFilterExtractor>builder()
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                        .adder(TST_VacuumFilterExtractor::addToMachineList)
                        .hint(1)
                        .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(10))
                        .buildAndChain(GregTechAPI.sBlockCasings4, 10))
                .addElement(
                    'C',
                    HatchElementBuilder.<TST_VacuumFilterExtractor>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(TST_VacuumFilterExtractor::addToMachineList)
                        .hint(2)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3))
                        .buildAndChain(GregTechAPI.sBlockCasings8, 3))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 0))
                .addElement('E', ofBlock(sBlockCasingsTT, 8))
                .addElement('F', ofBlock(BlockQuantumGlass.INSTANCE, 0))
                .addElement('G', ofFrame(Materials.Neutronium))
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
        coefficientMultiplier = 1 + getTotalPowerTier();
        speedBonus = 1F / coefficientMultiplier;
    }
    // endregion

    // region Processing Logic
    /**
     * coefficient = input voltage tier
     */
    private int coefficientMultiplier = 1;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_STEAM, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID };

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (machineMode == 1) {
            return RecipeMaps.distilleryRecipes;
        }
        return RecipeMaps.distillationTowerRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.distillationTowerRecipes, RecipeMaps.distilleryRecipes);
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Distillation Tower
         * 1 - Distillery
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    // @Override
    // public void setMachineModeIcons() {
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_STEAM);
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    // }
    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("VacuumFilterExtractor.modeMsg." + machineMode);
    }

    @Override
    public int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected float getEuModifier() {
        return EuModifier_VacuumFilterExtractor;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        // distillery has perfect overclock
        return machineMode == 1;
    }

    @Override
    public boolean getDefaultInputSeparationMode() {
        return false;
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
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { getCasingTextureForId(179), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_DRILL_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ORE_DRILL_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { getCasingTextureForId(179), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_DRILL)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ORE_DRILL_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { getCasingTextureForId(179) };
        }
        return rTexture;
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_VacuumFilterExtractor_MachineType
        // # Distillation Tower | Distillery
        // #zh_CN 蒸馏塔 | 蒸馏室
        tt.addMachineType(TextEnums.tr("Tooltip_VacuumFilterExtractor_MachineType"))
            // #tr Tooltip_VacuumFilterExtractor_Controller
            // # Controller block for the Vacuum Filter Extractor
            // #zh_CN 真空抽滤器的控制器方块
            .addInfo(TextEnums.tr("Tooltip_VacuumFilterExtractor_Controller"))
            // #tr Tooltip_VacuumFilterExtractor_01
            // # {\ITALIC}Engineers think something isn't broken because it has too few features.
            // #zh_CN {\ITALIC}工程师认为东西没坏是它功能太少.
            .addInfo(TextEnums.tr("Tooltip_VacuumFilterExtractor_01"))
            // #tr Tooltip_VacuumFilterExtractor_02
            // # By manipulating space in order to achieve separation of matter
            // #zh_CN 通过操控空间以实现分离物质,
            .addInfo(TextEnums.tr("Tooltip_VacuumFilterExtractor_02"))
            // #tr Tooltip_VacuumFilterExtractor_03
            // # rather than direct manipulation of matter.
            // #zh_CN 而非直接操控物质.
            .addInfo(TextEnums.tr("Tooltip_VacuumFilterExtractor_03"))
            // #tr Tooltip_VacuumFilterExtractor_04
            // # Recipe voltage is only {\RED}50%{\GRAY} of normal.
            // #zh_CN 只需要正常配方电压的{\RED}50%{\GRAY}.
            .addInfo(TextEnums.tr("Tooltip_VacuumFilterExtractor_04"))
            // #tr Tooltip_VacuumFilterExtractor_05
            // # Increasing the energy input will result in more speed boosts.
            // #zh_CN 提高能量输入将提供更多的速度提升.
            .addInfo(TextEnums.tr("Tooltip_VacuumFilterExtractor_05"))
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            // #tr Tooltip_VacuumFilterExtractor_06
            // # In distillery mode, machine will enable {\AQUA}Perfect Overclock{\GRAY}.
            // #zh_CN 蒸馏室模式将启用{\AQUA}无损超频{\GRAY}.
            .addInfo(TextEnums.tr("Tooltip_VacuumFilterExtractor_06"))
            .addStructureInfo(TextLocalization.Tooltip_DoNotNeedMaintenance)
            .beginStructureBlock(13, 22, 14, false)
            .addController(TextLocalization.textUseBlueprint)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
