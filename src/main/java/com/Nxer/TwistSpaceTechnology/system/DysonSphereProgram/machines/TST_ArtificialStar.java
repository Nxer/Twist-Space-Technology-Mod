package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Antimatter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatter;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.secondsOfArtificialStarProgressCycleTime;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.SpacetimeCompressionFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.StabilisationFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.TimeAccelerationFieldGenerator;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static com.gtnewhorizons.gtnhintergalactic.block.IGBlocks.SpaceElevatorCasing;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import galaxyspace.core.register.GSBlocks;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class TST_ArtificialStar extends GTCM_MultiMachineBase<TST_ArtificialStar> implements IGlobalWirelessEnergy {

    // region Class Constructor
    public TST_ArtificialStar(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_ArtificialStar(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ArtificialStar(this.mName);
    }

    // endregion

    // region Processing Logic
    private String ownerName;
    private UUID ownerUUID;
    private long storageEU = 0;
    private int tierDimensionField = -1;
    private int tierTimeField = -1;
    private int tierStabilisationField = -1;

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // iterate input bus slot
        // consume fuel and generate EU
        // if no input, still progress
        boolean flag = false;
        for (ItemStack items : getStoredInputs()) {
            if (metaItemEqual(items, Antimatter.get(1))) {
                consumeAntimatter(items);
                flag = true;
            } else if (metaItemEqual(items, AntimatterFuelRod.get(1))) {
                consumeAntimatterFuelRod(items);
                flag = true;
            }
        }

        if (!flag) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // flush input slots
        updateSlots();
        // set progress time with cfg
        mMaxProgresstime = (int) (20 * secondsOfArtificialStarProgressCycleTime);
        return CheckRecipeResultRegistry.GENERATING;
        // return super.checkProcessing();
    }

    private void consumeAntimatter(ItemStack antimatter) {
        if (Long.MAX_VALUE / EUEveryAntimatter < antimatter.stackSize) {
            addEUToGlobalEnergyMap(
                ownerUUID,
                BigInteger.valueOf(EUEveryAntimatter)
                    .multiply(BigInteger.valueOf(antimatter.stackSize)));
        } else {
            addEUToGlobalEnergyMap(ownerUUID, antimatter.stackSize * EUEveryAntimatter);
        }
        antimatter.stackSize = 0;
    }

    private void consumeAntimatterFuelRod(ItemStack antimatterFuelRod) {
        if (Long.MAX_VALUE / EUEveryAntimatterFuelRod < antimatterFuelRod.stackSize) {
            addEUToGlobalEnergyMap(
                ownerUUID,
                BigInteger.valueOf(EUEveryAntimatterFuelRod)
                    .multiply(BigInteger.valueOf(antimatterFuelRod.stackSize)));
        } else {
            addEUToGlobalEnergyMap(ownerUUID, antimatterFuelRod.stackSize * EUEveryAntimatterFuelRod);
        }
        antimatterFuelRod.stackSize = 0;
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
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("storageEU", storageEU);
        aNBT.setInteger("tierDimensionField", tierDimensionField);
        aNBT.setInteger("tierTimeField", tierTimeField);
        aNBT.setInteger("tierStabilisationField", tierStabilisationField);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        storageEU = aNBT.getLong("storageEU");
        tierDimensionField = aNBT.getInteger("tierDimensionField");
        tierTimeField = aNBT.getInteger("tierTimeField");
        tierStabilisationField = aNBT.getInteger("tierStabilisationField");
    }

    // endregion

    // region Structure
    // spotless:off
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tierDimensionField = -1;
        tierTimeField = -1;
        tierStabilisationField = -1;
        repairMachine();
        boolean flag = checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
        if (tierDimensionField < 0 || tierTimeField < 0 || tierStabilisationField < 0) return false;
        // Only allow and must be 1 input bus
        if (this.mInputBusses.size() != 1) return false;
        return flag;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainArtificialStar";
    private final int horizontalOffSet = 18;
    private final int verticalOffSet = 39;
    private final int depthOffSet = 15;
    @Override
    public IStructureDefinition<TST_ArtificialStar> getStructureDefinition() {
        return IStructureDefinition.<TST_ArtificialStar>builder()
                   .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                                   /*
A -> ofBlock...(gt.blockcasings, 14, ...);      // tierDimensionField
B -> ofBlock...(gt.blockcasingsSE, 2, ...);
C -> ofBlock...(gt.blockcasingsTT, 4, ...);
D -> ofBlock...(gt.blockcasingsTT, 14, ...);    // tierTimeField
E -> ofBlock...(gt.blockcasingsTT, 7, ...);
F -> ofBlock...(gt.blockcasingsTT, 8, ...);
G -> ofBlock...(gt.blockcasingsTT, 9, ...);     // tierStabilisationField
H -> ofBlock...(gt.blockcasingsTT, 12, ...);
I -> ofBlock...(gt.blockcasingsTT, 13, ...);
J -> ofBlock...(tile.DysonSwarmPart, 9, ...);
K -> ofBlock...(tile.quantumGlass, 0, ...);
L -> ofBlock...(gt.blockcasingsTT, 12, ...); // Hatch
                                    */
                   .addElement(
                       'A',
                       withChannel("tierDimensionField",
                                   ofBlocksTiered(
                                       TST_ArtificialStar::getTierDimensionFieldBlockFromBlock,
                                       ImmutableList.of(
                                           Pair.of(GregTech_API.sBlockCasings1, 14),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 0),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 1),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 2),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 3),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 4),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 5),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 6),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 7),
                                           Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 8)),
                                       -1,
                                       (t,m) -> t.tierDimensionField = m,
                                       t -> t.tierDimensionField
                                   )))
                   .addElement('B', ofBlock(SpaceElevatorCasing, 2))
                   .addElement('C', ofBlock(sBlockCasingsTT, 4))
                   .addElement(
                       'D',
                       withChannel("tierTimeField",
                                   ofBlocksTiered(
                                       TST_ArtificialStar::getTierTimeFieldBlockFromBlock,
                                       ImmutableList.of(
                                           Pair.of(sBlockCasingsTT, 14),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 0),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 1),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 2),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 3),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 4),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 5),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 6),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 7),
                                           Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 8)),
                                       -1,
                                       (t,m) -> t.tierTimeField = m,
                                       t -> t.tierTimeField)))
                   .addElement('E', ofBlock(sBlockCasingsTT, 7))
                   .addElement('F', ofBlock(sBlockCasingsTT, 8))
                   .addElement(
                       'G',
                       withChannel("tierStabilisationField",
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
                   .addElement('J', ofBlock(GSBlocks.DysonSwarmBlocks, 9))
                   .addElement('K', ofBlock(QuantumGlassBlock.INSTANCE, 0))
                   .addElement('L',
                               GT_HatchElementBuilder.<TST_ArtificialStar>builder()
                                   .atLeast(InputBus)
                                   .adder(TST_ArtificialStar::addInputBusToMachineList)
                                   .dot(1)
                                   .casingIndex(1024+12)
                                   .buildAndChain(sBlockCasingsTT, 12))
                   .build();
    }
    public static int getTierDimensionFieldBlockFromBlock(Block block, int meta){
        if (block == GregTech_API.sBlockCasings1 && 14 == meta) return 1;
        if (block == SpacetimeCompressionFieldGenerators) return meta + 2;
        return -1;
    }
    public static int getTierTimeFieldBlockFromBlock(Block block, int meta){
        if (block == sBlockCasingsTT && 14 == meta) return 1;
        if (block == TimeAccelerationFieldGenerator) return meta + 2;
        return -1;
    }
    public static int getTierStabilisationFieldBlockFromBlock(Block block, int meta){
        if (block == sBlockCasingsTT && 9 == meta) return 1;
        if (block == StabilisationFieldGenerators) return meta + 2;
        return -1;
    }
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Test")
          .addInfo(TextLocalization.StructureTooComplex)
          .addInfo(TextLocalization.BLUE_PRINT_INFO)
          .addSeparator()
            .addInputBus(textUseBlueprint, 1)
          //          .addStructureInfo("")
          .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }
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
        {"                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                  I                  ","                  I                  ","                 HIH                 ","                IIIII                ","               I     I               ","              I       I              ","             I   KKK   I             ","            HI  K   K  IH            ","           III  K A K  III           ","            HI  K   K  IH            ","             I   KKK   I             ","              I       I              ","               I     I               ","                IIIII                ","                 HIH                 ","                  I                  ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     ","                                     "},
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
    // endregion

    // region Overrides

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
    }

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
}
