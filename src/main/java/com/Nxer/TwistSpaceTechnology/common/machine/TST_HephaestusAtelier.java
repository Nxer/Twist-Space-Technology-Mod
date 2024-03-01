package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ConsumeDuration_HephaestusAtelier;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ConsumeEuPerSmelting_HephaestusAtelier;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ConsumeEutPerParallel_HephaestusAtelier;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.Utils.NEGATIVE_ONE;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;

public class TST_HephaestusAtelier extends GTCM_MultiMachineBase<TST_HephaestusAtelier>
    implements IGlobalWirelessEnergy {

    // region Class Constructor
    public TST_HephaestusAtelier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_HephaestusAtelier(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_HephaestusAtelier(this.mName);
    }

    // endregion

    // region Processing Logic
    private static final BigInteger CONSUME_EU_PER_SMELTING = BigInteger
        .valueOf(ConsumeEuPerSmelting_HephaestusAtelier);
    private int coilTier = 0;
    private int maxProcessNormalMode = 0;
    private UUID ownerUUID;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("coilTier", coilTier);
        aNBT.setInteger("maxProcessNormalMode", maxProcessNormalMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        coilTier = aNBT.getInteger("coilTier");
        maxProcessNormalMode = aNBT.getInteger("maxProcessNormalMode");
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret;
        if (coilTier > 1) {
            ret = new String[origin.length + 2];
            System.arraycopy(origin, 0, ret, 0, origin.length);
            ret[origin.length - 1] = EnumChatFormatting.AQUA
                + texter("Coil Tier", "HephaestusAtelier.getInfoData.Coil_Tier")
                + ": "
                + EnumChatFormatting.RESET
                + coilTier;
            ret[origin.length] = "" + EnumChatFormatting.RED
                + EnumChatFormatting.BOLD
                + texter("Wireless mode enabled", "HephaestusAtelier.getInfoData.Wireless_mode_enabled");
        } else {
            ret = new String[origin.length + 1];
            System.arraycopy(origin, 0, ret, 0, origin.length);
            ret[origin.length] = EnumChatFormatting.AQUA
                + texter("Coil Tier", "HephaestusAtelier.getInfoData.Coil_Tier")
                + ": "
                + EnumChatFormatting.RESET
                + coilTier;
        }

        return ret;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> inputItems = getStoredInputsNoSeparation();
        if (inputItems.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        if (coilTier > 1) {
            // wireless
            return wirelessProcessing(inputItems);
        } else {
            // normal
            return normalProcessing(inputItems);
        }
    }

    public CheckRecipeResult wirelessProcessing(ArrayList<ItemStack> inputItems) {
        ArrayList<ItemStack> outputs = new ArrayList<>();
        long smeltedAmount = 0;
        for (ItemStack items : inputItems) {
            ItemStack smeltedOutput = GT_ModHandler.getSmeltingOutput(items, false, null);
            if (smeltedOutput == null) {
                // move to outputs
                outputs.add(items.copy());
                items.stackSize = 0;
            } else {
                outputs.add(Utils.setStackSize(smeltedOutput, items.stackSize));
                smeltedAmount += items.stackSize;
                items.stackSize = 0;
            }
        }

        mOutputItems = outputs.toArray(new ItemStack[0]);
        updateSlots();

        // no smelting, just move garbage
        if (smeltedAmount == 0) {
            mMaxProgresstime = 20;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        // consume EU
        BigInteger consumeEU = BigInteger.valueOf(smeltedAmount)
            .multiply(CONSUME_EU_PER_SMELTING);
        if (!addEUToGlobalEnergyMap(ownerUUID, consumeEU.multiply(NEGATIVE_ONE))) {
            return CheckRecipeResultRegistry.insufficientPower(consumeEU.longValue());
        }

        // set processing time
        if (coilTier > 2) {
            // t3 coil
            mMaxProgresstime = DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier;
        } else {
            // t2 coil
            mMaxProgresstime = DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier;
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public CheckRecipeResult normalProcessing(ArrayList<ItemStack> inputItems) {
        int canProcess = maxProcessNormalMode;
        ArrayList<ItemStack> outputs = new ArrayList<>();
        for (ItemStack items : inputItems) {
            if (canProcess <= 0) break;

            ItemStack smeltedOutput = GT_ModHandler.getSmeltingOutput(items, false, null);
            if (smeltedOutput == null) {
                // move to outputs
                outputs.add(items.copy());
                items.stackSize = 0;
            } else {
                if (canProcess >= items.stackSize) {
                    outputs.add(Utils.setStackSize(smeltedOutput, items.stackSize));
                    canProcess -= items.stackSize;
                    items.stackSize = 0;
                } else {
                    outputs.add(Utils.setStackSize(smeltedOutput, canProcess));
                    items.stackSize -= canProcess;
                    canProcess = 0;
                    break;
                }
            }
        }

        mOutputItems = outputs.toArray(new ItemStack[0]);
        updateSlots();
        // no smelting, just move garbage
        if (canProcess == maxProcessNormalMode) {
            mMaxProgresstime = 20;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        long maxEut = getMaxInputEu();

        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
            .setRecipeEUt((long) ConsumeEutPerParallel_HephaestusAtelier * (maxProcessNormalMode - canProcess))
            .setEUt(maxEut)
            .setDuration(ConsumeDuration_HephaestusAtelier)
            .setDurationDecreasePerOC(1)
            .calculate();

        lEUt = -calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    protected long getActualEnergyUsage() {
        return -this.lEUt;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.furnaceRecipes;
    }

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
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        coilTier = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (coilTier == 0) return false;
        if (coilTier == 1 && mEnergyHatches.isEmpty() && mExoticEnergyHatches.isEmpty()) return false;
        maxProcessNormalMode = (int) Math
            .min(Integer.MAX_VALUE, (15d / 16 * getMaxInputEu() / ConsumeEutPerParallel_HephaestusAtelier));
        return true;
    }
    // endregion

    // region Structure
    // spotless:off
    private final int horizontalOffSet = 16;
    private final int verticalOffSet = 5;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainHephaestusAtelier";
    private static IStructureDefinition<TST_HephaestusAtelier> STRUCTURE_DEFINITION = null;
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
    public IStructureDefinition<TST_HephaestusAtelier> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION =
                StructureDefinition
                    .<TST_HephaestusAtelier>builder()
                    .addShape(
                        STRUCTURE_PIECE_MAIN,
                        transpose(
                            new String[][]{
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               DDD               ","              DDDDD              ","              DDDDD              ","              DDDDD              ","               DDD               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             DD B DD             ","             D BBB D             ","             DBBBBBD             ","             D BBB D             ","             DD B DD             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             D BBB D             ","            D BB BB D            ","            DBBCCCBBD            ","            DB CCC BD            ","            DBBCCCBBD            ","            D BB BB D            ","             D BBB D             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               DDD               ","             DD B DD             ","            D BB BB D            ","            DBCCCCCBD            ","           D BC   CB D           ","           DB C   C BD           ","           D BC   CB D           ","            DBCCCCCBD            ","            D BB BB D            ","             DD B DD             ","               DDD               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                                {"                                 ","             AAAAAAA             ","          AAA       AAA          ","        AA             AA        ","       A                 A       ","      A                   A      ","     A                     A     ","    A                       A    ","   A                         A   ","   A                         A   ","  A                           A  ","  A           DDDDD           A  ","  A          D BBB D          A  "," A          DBBCCCBBD          A "," A         D BC   CB D         A "," A         DBC     CBD         A "," A         DBC     CBD         A "," A         DBC     CBD         A "," A         D BC   CB D         A "," A          DBBCCCBBD          A ","  A          D BBB D          A  ","  A           DDDDD           A  ","  A                           A  ","   A                         A   ","   A                         A   ","    A                       A    ","     A                     A     ","      A                   A      ","       A                 A       ","        AA             AA        ","          AAA       AAA          ","             AAAAAAA             ","                                 "},
                                {"             AAA~AAA             ","          AAAEEEEEEEAAA          ","        AAEEE       EEEAA        ","       AEE             EEA       ","      AE                 EA      ","     AE                   EA     ","    AE                     EA    ","   AE                       EA   ","  AE                         EA  ","  AE                         EA  "," AE                           EA "," AE           DDDDD           EA "," AE          DBBBBBD          EA ","AE          DB CCC BD          EA","AE         DB C   C BD         EA","AE         DBC     CBD         EA","AE         DBC     CBD         EA","AE         DBC     CBD         EA","AE         DB C   C BD         EA","AE          DB CCC BD          EA"," AE          DBBBBBD          EA "," AE           DDDDD           EA "," AE                           EA ","  AE                         EA  ","  AE                         EA  ","   AE                       EA   ","    AE                     EA    ","     AE                   EA     ","      AE                 EA      ","       AEE             EEA       ","        AAEEE       EEEAA        ","          AAAEEEEEEEAAA          ","             AAAAAAA             "},
                                {"                                 ","             AAAAAAA             ","          AAA       AAA          ","        AA             AA        ","       A                 A       ","      A                   A      ","     A                     A     ","    A                       A    ","   A                         A   ","   A                         A   ","  A                           A  ","  A           DDDDD           A  ","  A          D BBB D          A  "," A          DBBCCCBBD          A "," A         D BC   CB D         A "," A         DBC     CBD         A "," A         DBC     CBD         A "," A         DBC     CBD         A "," A         D BC   CB D         A "," A          DBBCCCBBD          A ","  A          D BBB D          A  ","  A           DDDDD           A  ","  A                           A  ","   A                         A   ","   A                         A   ","    A                       A    ","     A                     A     ","      A                   A      ","       A                 A       ","        AA             AA        ","          AAA       AAA          ","             AAAAAAA             ","                                 "},
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               DDD               ","             DD B DD             ","            D BB BB D            ","            DBCCCCCBD            ","           D BC   CB D           ","           DB C   C BD           ","           D BC   CB D           ","            DBCCCCCBD            ","            D BB BB D            ","             DD B DD             ","               DDD               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             D BBB D             ","            D BB BB D            ","            DBBCCCBBD            ","            DB CCC BD            ","            DBBCCCBBD            ","            D BB BB D            ","             D BBB D             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             DD B DD             ","             D BBB D             ","             DBBBBBD             ","             D BBB D             ","             DD B DD             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                                {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               DDD               ","              DDDDD              ","              DDDDD              ","              DDDDD              ","               DDD               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "}
                            }
                        )
                    )
                    .addElement(
                        'A',
                        GT_HatchElementBuilder
                            .<TST_HephaestusAtelier>builder()
                            .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy) )
                            .adder(TST_HephaestusAtelier::addToMachineList)
                            .dot(1)
                            .casingIndex(11)
                            .buildAndChain(GregTech_API.sBlockCasings1, 11))
                    .addElement('B', ofBlock(sBlockCasingsTT, 4))
                    .addElement('C', ofBlock(sBlockCasingsTT, 7))
                    .addElement('D', ofBlock(QuantumGlassBlock.INSTANCE, 0))
                    .addElement(
                        'E',
                        withChannel(
                            "fieldcoil",
                            ofBlocksTiered(
                                TST_HephaestusAtelier::getBlockTier,
                                ImmutableList.of(
                                    Pair.of(sBlockCasingsTT, 7),
                                    Pair.of(sBlockCasingsTT, 14),
                                    Pair.of(sBlockCasingsTT, 10)
                                ),
                                0,
                                (m, t) -> m.coilTier = t,
                                m -> m.coilTier
                            )
                        ))
                    .build();
//            Blocks:
//            A -> ofBlock...(gt.blockcasings, 11, ...); // hatch
//            B -> ofBlock...(gt.blockcasingsTT, 4, ...);
//            C -> ofBlock...(gt.blockcasingsTT, 7, ...);
//            D -> ofBlock...(tile.quantumGlass, 0, ...);
//            E -> ofBlock...(tile.stonebricksmooth, 0, ...); // tiered coils
        }
        return STRUCTURE_DEFINITION;
    }

    private static int getBlockTier(Block block, int meta) {
        if (sBlockCasingsTT == block) {
            return switch (meta) {
                case 7 -> 1;
                case 14 -> 2;
                case 10 -> 3;
                default -> 0;
            };
        } else {
            return 0;
        }
    }

    // spotless:on
    // endregion

    // region General

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_HephaestusAtelier_MachineType)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_Controller)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_01)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_02)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_03)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_04)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_05)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_06)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_07)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_08)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_09)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_10)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_11)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_12)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_13)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_14)
            .addInfo(TextLocalization.Tooltip_HephaestusAtelier_15)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addStructureInfo(TextLocalization.Tooltip_HephaestusAtelier_2_01)
            .addStructureInfo(TextLocalization.Tooltip_DoNotNeedMaintenance)
            .beginStructureBlock(33, 11, 33, false)
            .addController(TextLocalization.textFrontCenter)
            .addInputBus(TextLocalization.textAnyCasing, 1)
            .addOutputBus(TextLocalization.textAnyCasing, 1)
            .addEnergyHatch(TextLocalization.textAnyCasing, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][11], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][11], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][11] };
    }
}
