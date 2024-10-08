package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EuModifier_VacuumFilterExtractor;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Mode_Default_VacuumFilterExtractor;
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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.blocks.BlockCasings8;
import tectech.thing.block.BlockQuantumGlass;

// 真空抽滤器
public class TST_VacuumFilterExtractor extends GTCM_MultiMachineBase<TST_VacuumFilterExtractor> {

    // region Class Constructor
    public TST_VacuumFilterExtractor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_VacuumFilterExtractor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_VacuumFilterExtractor(this.mName);
    }
    // endregion

    // region Processing Logic
    /**
     * 0 = distillation tower ; 1 = distillery
     */
    private byte mode = Mode_Default_VacuumFilterExtractor;
    /**
     * coefficient = input voltage tier
     */
    private int coefficientMultiplier = 1;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", mode);
        aNBT.setInteger("coefficientMultiplier", coefficientMultiplier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = aNBT.getByte("mode");
        coefficientMultiplier = aNBT.getInteger("coefficientMultiplier");
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("VacuumFilterExtractor.modeMsg." + this.mode));
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (mode == 1) {
            return RecipeMaps.distilleryRecipes;
        }
        return RecipeMaps.distillationTowerRecipes;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        // distillery has perfect overclock
        return mode == 1;
    }

    @Override
    protected float getSpeedBonus() {
        return 1F / coefficientMultiplier;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected float getEuModifier() {
        return EuModifier_VacuumFilterExtractor;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        coefficientMultiplier = 1 + (int) Utils.calculatePowerTier(getMaxInputEu());
        return true;
    }
    // endregion

    // region Structure
    // spotless:off
    private final int horizontalOffSet = 6;
    private final int verticalOffSet = 20;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainVacuumFilterExtractor";
    private static IStructureDefinition<TST_VacuumFilterExtractor> STRUCTURE_DEFINITION = null;
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<TST_VacuumFilterExtractor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition
                                       .<TST_VacuumFilterExtractor>builder()
                                       .addShape(STRUCTURE_PIECE_MAIN, transpose(SHAPE))
                                       .addElement('A', ofBlock(GregTechAPI.sBlockCasings2, 8))
                                       .addElement(
                                           'B',
                                           HatchElementBuilder
                                               .<TST_VacuumFilterExtractor>builder()
                                               .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                                               .adder(TST_VacuumFilterExtractor::addToMachineList)
                                               .dot(1)
                                               .casingIndex(((BlockCasings4)GregTechAPI.sBlockCasings4).getTextureIndex(10))
                                               .buildAndChain(GregTechAPI.sBlockCasings4, 10))
                                       .addElement(
                                           'C',
                                           HatchElementBuilder
                                               .<TST_VacuumFilterExtractor>builder()
                                               .atLeast(Energy.or(ExoticEnergy))
                                               .adder(TST_VacuumFilterExtractor::addToMachineList)
                                               .dot(2)
                                               .casingIndex(((BlockCasings8)GregTechAPI.sBlockCasings8).getTextureIndex(3))
                                               .buildAndChain(GregTechAPI.sBlockCasings8, 3))
                                       .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 0))
                                       .addElement('E', ofBlock(sBlockCasingsTT, 8))
                                       .addElement('F', ofBlock(BlockQuantumGlass.INSTANCE, 0))
                                       .addElement('G', ofFrame(Materials.Neutronium))
                                       .build();
        }
        return STRUCTURE_DEFINITION;
    }

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
    // endregion

    // region General
    @Override
    public boolean getDefaultInputSeparationMode() {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_VacuumFilterExtractor_MachineType)
            .addInfo(TextLocalization.Tooltip_VacuumFilterExtractor_Controller)
            .addInfo(TextLocalization.Tooltip_VacuumFilterExtractor_01)
            .addInfo(TextLocalization.Tooltip_VacuumFilterExtractor_02)
            .addInfo(TextLocalization.Tooltip_VacuumFilterExtractor_03)
            .addInfo(TextLocalization.Tooltip_VacuumFilterExtractor_04)
            .addInfo(TextLocalization.Tooltip_VacuumFilterExtractor_05)
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addInfo(TextLocalization.Tooltip_VacuumFilterExtractor_06)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addStructureInfo(TextLocalization.Tooltip_DoNotNeedMaintenance)
            .beginStructureBlock(13, 22, 14, false)
            .addController(TextLocalization.textUseBlueprint)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

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
}
