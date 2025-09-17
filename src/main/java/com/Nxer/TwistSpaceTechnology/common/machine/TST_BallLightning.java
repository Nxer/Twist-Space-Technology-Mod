package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.config.Config.WirelessModeExtraEuCost_BallLightning;
import static com.Nxer.TwistSpaceTechnology.config.Config.WirelessModeTickEveryProcess_BallLightning;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.getCasingTextureIndex;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static tectech.thing.casing.TTCasingsContainer.StabilisationFieldGenerators;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import galaxyspace.core.register.GSBlocks;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gtPlusPlus.core.block.ModBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_BallLightning extends GTCM_MultiMachineBase<TST_BallLightning>
    implements IWirelessEnergyHatchInformation {

    // region Class Constructor
    public TST_BallLightning(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_BallLightning(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BallLightning(this.mName);
    }

    // end region

    // region default value

    public int mRecipeTierModeFusion = 1;
    public byte glassTier;
    private int fieldGeneratorTier = 0;
    private int compactFusionCoilTier = 0;
    private int dimensionBlockTier = 0;
    private UUID ownerUUID;
    public byte mMachineTier = 1;
    private byte mStructureTier = 0;
    private int overclockParameter = 1;
    public long FusionMaxEut = 0;
    private boolean isWirelessMode = false;
    private String costingWirelessEU = "0";
    private int extraEuCostMultiplier = 1;

    private static final BigInteger NEGATIVE_ONE = BigInteger.valueOf(-1);
    // end region

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
        if (GravitationalLens == null) GravitationalLens = GTCMItemList.GravitationalLens.get(1);
        if (BallLightningUpgradeChip == null) BallLightningUpgradeChip = GTCMItemList.BallLightningUpgradeChip.get(1);
    }

    public HeatingCoilLevel coilLevel;

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    // region Processing Logic

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Arc Furnace
         * 1 - Plasma Arc Furnace
         * 2 - Fusion
         * 3 - Ball Lightning
         */
        return 4;
    }

    @Override
    public int nextMachineMode() {
        if (machineMode + 1 >= mMachineTier) {
            return 0;
        }
        return machineMode + 1;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_STEAM);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_BENDING);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("BallLightning.modeMsg." + mode);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return switch (machineMode) {
            case 1 -> RecipeMaps.plasmaArcFurnaceRecipes;
            case 2 -> RecipeMaps.fusionRecipes;
            case 3 -> GTCMRecipe.BallLightningRecipes;
            default -> RecipeMaps.arcFurnaceRecipes;
        };
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            RecipeMaps.arcFurnaceRecipes,
            RecipeMaps.plasmaArcFurnaceRecipes,
            RecipeMaps.fusionRecipes,
            GTCMRecipe.BallLightningRecipes);
    }

    @Override
    protected int getMaxParallelRecipes() {
        if (isWirelessMode || machineMode == 3) return Integer.MAX_VALUE;
        else if (machineMode == 2) return 65536;
        else return (int) Math.pow(2, compactFusionCoilTier * (coilLevel.getTier() - 10));
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return switch (machineMode) {
            case (2) -> true;
            case (3) -> false;
            default -> fieldGeneratorTier > 2;
        };
    }

    public static int getBlockFieldGeneratorTier(Block block, int meta) {
        if (block == sBlockCasingsTT) {
            return meta - 7;
        }
        if (block == StabilisationFieldGenerators) {
            return meta + 3;
        }
        return 0;
    }

    public static int getcompactFusionCoilTier(Block block, int meta) {
        if (block == compactFusionCoil) {
            return meta + 1;
        }
        if (block == MetaBlockCasing01 && meta == 2) return 6;
        return 0;
    }

    public static int getDimensionBlockTier(Block block, int meta) {
        if (block == sBlockCasingsTT && meta == 10) {
            return 2;
        }
        if (block == GregTechAPI.sBlockCasings1 && meta == 14) {
            return 1;
        }
        return 0;
    }

    public static int getFusionRecipeTier(int StartUp, int RecipeTier) {
        if (StartUp < 160000000) return 1;
        if (StartUp < 320000000) return 2;
        if (StartUp < 640000000) return 3;
        if (StartUp > 640000000 && RecipeTier < 10) return 4;
        if (StartUp > 640000000 && RecipeTier == 10) return 5;
        if (RecipeTier > 10) return 6;
        return 10;
    }

    public void checkBonus() {
        if (mMachineTier - machineMode > 1) speedBonus = (float) Math.pow(0.25, mMachineTier - machineMode - 1);
        else speedBonus = 1;
        euModifier = machineMode != 3 ? 1 : (float) (1 - 0.099 * (fieldGeneratorTier - 1));
    }

    public void checkWireless() {
        isWirelessMode = this.mEnergyHatches.isEmpty() && this.mExoticEnergyHatches.isEmpty() && mMachineTier == 4;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide() && mMachineTier != 0) {
            if (!checkStructure(true)) {
                // #tr BallLightning.modeMsg.IncompleteStructure
                // # INCOMPLETE STRUCTURE!
                // #zh_CN 结构不完整!
                GTUtility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("BallLightning.modeMsg.IncompleteStructure"));
                return;
            }
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    private void flushOverclockParameter() {
        ItemStack controllerStack = getControllerSlot();
        if (GTUtility.areStacksEqual(controllerStack, GTCMItemList.BallLightningUpgradeChip.get(1))
            && controllerStack.stackSize >= 1) this.overclockParameter = controllerStack.stackSize;
        else this.overclockParameter = 1;

    }

    private int getOverclockEUCostMultiplier() {
        return this.overclockParameter;
    }

    private int getProgressTime(int basicTickCost) {
        return (int) Math.max(1, Math.ceil((float) basicTickCost / this.overclockParameter));
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setEuModifier(getEuModifier());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (glassTier < 12 && glassTier < GTUtility.getTier(recipe.mEUt)) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(GTUtility.getTier(recipe.mEUt));
                }
                if (machineMode == 2) {
                    mRecipeTierModeFusion = getFusionRecipeTier(recipe.mSpecialValue, GTUtility.getTier(recipe.mEUt));
                    if (mRecipeTierModeFusion > compactFusionCoilTier)
                        return CheckRecipeResultRegistry.insufficientMachineTier(mRecipeTierModeFusion);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                if (isWirelessMode) {
                    return OverclockCalculator.ofNoOverclock(recipe);
                } else {
                    return super.createOverclockCalculator(recipe);
                }
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        if (machineMode != 2) {
            if (isWirelessMode) {
                // wireless mode ignore voltage limit
                logic.setAvailableVoltage(Long.MAX_VALUE);
                logic.setAvailableAmperage(1);
                logic.setAmperageOC(false);
            } else {
                super.setProcessingLogicPower(logic);
            }
        } else {
            FusionMaxEut = (long) (RECIPE_MAX
                * (Math.pow(4, (compactFusionCoilTier - 2)) * Math.pow(1.8, fieldGeneratorTier - 1)));
            if (isWirelessMode || FusionMaxEut < getMaxInputEu()) {
                logic.setAvailableVoltage(FusionMaxEut);
                logic.setAvailableAmperage(1);
                logic.setAmperageOC(false);
            } else {
                super.setProcessingLogicPower(logic);
            }
        }
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        checkTier();
        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);

        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        if (isWirelessMode) {
            lEUt = 0;
            flushOverclockParameter();
            mMaxProgresstime = getProgressTime(WirelessModeTickEveryProcess_BallLightning);
            extraEuCostMultiplier = (int) (WirelessModeExtraEuCost_BallLightning
                * Math.pow(getOverclockEUCostMultiplier(), 2));

            BigInteger costingWirelessEUTemp = BigInteger.valueOf(processingLogic.getCalculatedEut())
                .multiply(BigInteger.valueOf(processingLogic.getDuration()))
                .multiply(BigInteger.valueOf(Math.round((extraEuCostMultiplier * speedBonus))))
                .divide(BigInteger.valueOf((long) (1 / euModifier)));
            costingWirelessEU = GTUtility.formatNumbers(costingWirelessEUTemp);
            if (!addEUToGlobalEnergyMap(ownerUUID, costingWirelessEUTemp.multiply(NEGATIVE_ONE))) {
                return CheckRecipeResultRegistry.insufficientPower(costingWirelessEUTemp.longValue());
            }
        } else {
            mMaxProgresstime = processingLogic.getDuration();
            setEnergyUsage(processingLogic);
        }

        mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        return result;
    }

    // region Structure

    private static final String STRUCTURE_PIECE_MK1 = "BallLightningMK1";
    private static final String STRUCTURE_PIECE_MK2 = "BallLightningMK2";
    private static IStructureDefinition<TST_BallLightning> STRUCTURE_DEFINITION = null;
    private static ItemStack GravitationalLens;
    private static ItemStack BallLightningUpgradeChip;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.glassTier = 0;
        this.fieldGeneratorTier = 0;
        this.compactFusionCoilTier = 0;
        this.dimensionBlockTier = 0;
        this.coilLevel = HeatingCoilLevel.None;
        this.mStructureTier = 0;

        if (checkPiece(STRUCTURE_PIECE_MK1, 12, 20, 7) && coilLevel.getTier() > 10) {
            if (checkPiece(STRUCTURE_PIECE_MK2, 38, 51, 7)) {
                mStructureTier = 2;
            } else {
                mStructureTier = 1;
            }
        } else return false;

        checkTier();
        checkWireless();
        return mStructureTier > 0;

    }

    protected void checkTier() {
        checkTier: {
            if (mStructureTier < 1) {
                mMachineTier = 0;
                break checkTier;
            }

            ItemStack controllerSlot = getControllerSlot();
            if (controllerSlot == null) {
                mMachineTier = 1;
                break checkTier;
            }

            if (mStructureTier == 2 && controllerSlot.isItemEqual(BallLightningUpgradeChip)) {
                if (compactFusionCoilTier == 6 && dimensionBlockTier == 2) {
                    mMachineTier = 4;
                } else {
                    mMachineTier = 3;
                }
            } else if (controllerSlot.isItemEqual(GravitationalLens)) {
                mMachineTier = 2;
            } else {
                mMachineTier = 1;
            }
        }
        checkWireless();
        checkBonus();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MK1, stackSize, hintsOnly, 12, 20, 7);
        if (stackSize.stackSize > 1) this.buildPiece(STRUCTURE_PIECE_MK2, stackSize, hintsOnly, 38, 51, 7);
    }
    // end region

    // region Structure

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine && stackSize.stackSize < 2) return -1;
        int builtMain = survivialBuildPiece(STRUCTURE_PIECE_MK1, stackSize, 12, 20, 7, elementBudget, env, false, true);
        if (stackSize.stackSize < 2) {
            return builtMain;
        }
        int builtAdv = survivialBuildPiece(STRUCTURE_PIECE_MK2, stackSize, 38, 51, 7, elementBudget, env, false, true);
        if (builtMain == -1 && builtAdv == -1) return -1;
        if (builtMain == -1 && builtAdv > -1) return builtAdv;
        if (builtMain > -1 && builtAdv == -1) return builtMain;
        return builtMain + builtAdv;
    }

    /*
     * A -> ofBlock...(BW_GlasBlocks, 0, ...);
     * B -> ofBlock...(compactFusionCoil, 1, ...);
     * C -> ofBlock...(gt.blockcasings, 14, ...);
     * D -> ofBlock...(gt.blockcasings5, 12, ...);
     * E -> ofBlock...(gt.blockcasings8, 5, ...);
     * F -> ofBlock...(gt.stabilisation_field_generator, 8, ...);
     * G -> ofBlock...(gtplusplus.blockcasings.4, 3, ...);
     * H -> ofBlock...(gtplusplus.blocktieredcasings.1, 9, ...);
     * I -> ofBlock...(tile.DysonSwarmPart, 1, ...);
     * J -> ofBlock...(tile.DysonSwarmPart, 8, ...);
     * K -> ofBlock...(tile.DysonSwarmPart, 0, ...);
     * L -> ofBlock...(tile.DysonSwarmPart, 5, ...);
     * M -> ofBlock...(gt.blockcasingsTT, 10, ...);
     * N -> ofBlock...(tile.blockDiamond, 0, ...);
     * O -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseOetaPipeEntity, ...);
     */
    @Override
    public IStructureDefinition<TST_BallLightning> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_BallLightning>builder()
                .addShape(STRUCTURE_PIECE_MK1, transpose(shapeMK1))
                .addShape(STRUCTURE_PIECE_MK2, transpose(shapeMK2))
                .addElement(
                    'A',
                    withChannel(
                        "glass",
                        BorosilicateGlass.ofBoroGlass(
                            (byte) 0,
                            (byte) 1,
                            Byte.MAX_VALUE,
                            (te, t) -> te.glassTier = t,
                            te -> te.glassTier)))
                .addElement(
                    'B',
                    withChannel(
                        "compactcoiltier",
                        ofBlocksTiered(
                            TST_BallLightning::getcompactFusionCoilTier,
                            ImmutableList.of(
                                Pair.of(compactFusionCoil, 0),
                                Pair.of(compactFusionCoil, 1),
                                Pair.of(compactFusionCoil, 2),
                                Pair.of(compactFusionCoil, 3),
                                Pair.of(compactFusionCoil, 4),
                                Pair.of(MetaBlockCasing01, 2)),
                            0,
                            (me, m) -> me.compactFusionCoilTier = m,
                            me -> me.compactFusionCoilTier)))
                .addElement(
                    'C',
                    withChannel(
                        "dimensionblocktier",
                        ofBlocksTiered(
                            TST_BallLightning::getDimensionBlockTier,
                            ImmutableList.of(Pair.of(GregTechAPI.sBlockCasings1, 14), Pair.of(sBlockCasingsTT, 10)),
                            0,
                            (ne, n) -> ne.dimensionBlockTier = n,
                            ne -> ne.dimensionBlockTier)))
                .addElement(
                    'D',
                    withChannel("coil", ofCoil(TST_BallLightning::setCoilLevel, TST_BallLightning::getCoilLevel)))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 5))
                .addElement(
                    'F',
                    withChannel(
                        "fieldgeneratortier",
                        ofBlocksTiered(
                            TST_BallLightning::getBlockFieldGeneratorTier,
                            ImmutableList.of(
                                Pair.of(sBlockCasingsTT, 8),
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
                            0,
                            (ne, n) -> ne.fieldGeneratorTier = n,
                            ne -> ne.fieldGeneratorTier)))
                .addElement('G', ofBlock(MetaBlockCasing01, 1))
                .addElement('H', ofBlock(ModBlocks.blockCasingsTieredGTPP, 9))
                .addElement('I', ofBlock(GSBlocks.DysonSwarmBlocks, 1))
                .addElement('J', ofBlock(GSBlocks.DysonSwarmBlocks, 8))
                .addElement('K', ofBlock(GSBlocks.DysonSwarmBlocks, 0))
                .addElement('L', ofBlock(GSBlocks.DysonSwarmBlocks, 5))
                .addElement('N', ofFrame(Materials.SuperconductorUIVBase))
                .addElement('O', ofFrame(Materials.Neutronium))
                .addElement(
                    'Y',
                    HatchElementBuilder.<TST_BallLightning>builder()
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_BallLightning::addToMachineList)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(1))
                        .dot(1)
                        .buildAndChain(TstBlocks.MetaBlockCasing01, 1))
                .addElement(
                    'Z',
                    HatchElementBuilder.<TST_BallLightning>builder()
                        .atLeast(Energy.or(ExoticEnergy))
                        .adder(TST_BallLightning::addToMachineList)
                        .casingIndex(getCasingTextureIndex(GregTechAPI.sBlockCasings8, 5))
                        .dot(2)
                        .buildAndChain(GregTechAPI.sBlockCasings8, 5))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // region General

    @Override
    public boolean getDefaultInputSeparationMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    // Info
    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getInteger("mode") == 2) {
            currentTip.add(
                // #tr Waila.TST_BallLightning.1
                // # Max Fusion Eu Cost
                // #zh_CN 聚变功耗上限
                (EnumChatFormatting.AQUA + TextEnums.tr("Waila.TST_BallLightning.1")
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.GOLD
                    + tag.getString("FusionMaxEut")
                    + EnumChatFormatting.RESET
                    + " EU/t"));
        }
        if (tag.getBoolean("isWirelessMode")) {
            // #tr Waila.TST_IndistinctTentacle.1
            // # Wireless Mode
            // #zh_CN 无线模式
            currentTip.add(EnumChatFormatting.LIGHT_PURPLE + TextEnums.tr("Waila.TST_IndistinctTentacle.1"));
            currentTip.add(
                // #tr Waila.TST_MiracleDoor.1
                // # Current EU cost
                // #zh_CN 当前EU消耗
                EnumChatFormatting.AQUA + TextEnums.tr("Waila.TST_MiracleDoor.1")
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.GOLD
                    + tag.getString("costingWirelessEU")
                    + EnumChatFormatting.RESET
                    + " EU");

            if (1 != tag.getInteger("EuCostMultiplier")) {
                currentTip.add("" + EnumChatFormatting.BLUE + EnumChatFormatting.BOLD
                // #tr Waila.TST_BallLightning.2
                // # Extra EU cost multiplier
                // #zh_CN 额外EU消耗倍率
                    + TextEnums.tr("Waila.TST_BallLightning.2")
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.GOLD
                    + EnumChatFormatting.BOLD
                    + tag.getInteger("extraEuCostMultiplier")
                    + EnumChatFormatting.RESET);
            }
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setString("FusionMaxEut", GTUtility.formatNumbers(FusionMaxEut));
            tag.setBoolean("isWirelessMode", isWirelessMode);
            tag.setString("costingWirelessEU", costingWirelessEU);
            tag.setInteger("extraEuCostMultiplier", Math.round(extraEuCostMultiplier * speedBonus));
        }
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 6];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + TextEnums.MachineMode.getText()
            + " : "
            + EnumChatFormatting.GOLD
            + (this.machineMode + 1);
        ret[origin.length + 1] = EnumChatFormatting.AQUA + TextEnums.MachineTier.getText()
            + " : "
            + EnumChatFormatting.GOLD
            + this.mMachineTier;
        ret[origin.length + 2] = EnumChatFormatting.AQUA + TextEnums.FieldGeneratorTier.getText()
            + " : "
            + EnumChatFormatting.GOLD
            + this.fieldGeneratorTier;
        ret[origin.length + 3] = EnumChatFormatting.AQUA + TextEnums.CompactFusionCoilTier.getText()
            + " : "
            + EnumChatFormatting.GOLD
            + this.compactFusionCoilTier;
        ret[origin.length + 4] = EnumChatFormatting.AQUA + TextEnums.GlassTier.getText()
            + " : "
            + EnumChatFormatting.GOLD
            + this.glassTier;
        ret[origin.length + 5] = EnumChatFormatting.AQUA + TextEnums.CoilTier.getText()
            + " : "
            + EnumChatFormatting.GOLD
            + (this.coilLevel != null ? this.coilLevel.getTier() : 0);
        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setByte(" mMachineTier", mMachineTier);
        aNBT.setByte(" mStructureTier", mStructureTier);
        aNBT.setBoolean("isWirelessMode", isWirelessMode);
        aNBT.setFloat("speedBonus", speedBonus);
        aNBT.setInteger("fieldGeneratorTier", fieldGeneratorTier);
        aNBT.setInteger("compactFusionCoilTier", compactFusionCoilTier);
        aNBT.setByte("glassTier", glassTier);
        aNBT.setString("costingWirelessEU", costingWirelessEU);
        aNBT.setInteger("extraEuCostMultiplier", extraEuCostMultiplier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getByte("mode");
        mMachineTier = aNBT.getByte("mMachineTier");
        mStructureTier = aNBT.getByte("mStructureTier");
        isWirelessMode = aNBT.getBoolean("isWirelessMode");
        speedBonus = aNBT.getFloat("speedBonus");
        fieldGeneratorTier = aNBT.getInteger("fieldGeneratorTier");
        compactFusionCoilTier = aNBT.getInteger("compactFusionCoilTier");
        glassTier = aNBT.getByte("glassTier");
        costingWirelessEU = aNBT.getString("costingWirelessEU");
        extraEuCostMultiplier = aNBT.getInteger("extraEuCostMultiplier");
    }

    // end region

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        ITexture base = casingTexturePages[115][MetaBlockCasing01.getTextureIndexInPage(1)];
        if (side == facing) {
            if (aActive) return new ITexture[] { base, TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_ON)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FUSION1_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { base, TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .glow()
                .build() };
        }
        return new ITexture[] { base };
    }

    // end region
    // spotless:off
    private final String[][] shapeMK1 = new String[][]{
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","    GI             IG    ","    GI             IG    ","    GI             IG    ","    GI             IG    ","    GI             IG    ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GGI             IGG   ","   GDJ             JDG   ","   GDJ             JDG   ","  GGDJ             JDGG  ","   GDJ             JDG   ","   GDJ             JDG   ","   GGI             IGG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  G DJ             JD G  ","  GDBD     AAA     DBDG  ","  GDBD    AAAAA    DBDG  ","  GDBD    AAAAA    DBDG  ","  GDBD    AAAAA    DBDG  ","  GDBD     AAA     DBDG  ","  G DJ             JD G  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  GDBD             DBDG  ","  GDBD    AAAAA    DBDG  "," GO D    AA   AA    D OG "," GO D    A     A    D OG "," GO D    A     A    D OG "," GO D    A     A    D OG "," GO D    AA   AA    D OG ","  GDBD    AAAAA    DBDG  ","  GDBD             DBDG  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","   GGI             IGG   ","  G DJ             JD G  ","  GDBD    AAAAA    DBDG  "," GO D    A     A    D OG "," G      A       A      G "," G      A       A      G "," G      A       A      G "," G      A       A      G "," G      A       A      G "," GO D    A     A    D OG ","  GDBD    AAAAA    DBDG  ","  G DJ             JD G  ","   GGI             IGG   ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  GDBD     AAA     DBDG  "," GO D    AA   AA    D OG "," G      A       A      G "," G      A       A      G ","GGD D DADO     ODAD D DGG","GGD D DADO     ODAD D DGG","GGD D DADO     ODAD D DGG"," G      A       A      G "," G      A       A      G "," GO D    AA   AA    D OG ","  GDBD     AAA     DBDG  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  GDBD    AAAAA    DBDG  "," GO D    A     A    D OG "," G      A       A      G ","GGD D DADO     ODAD D DGG","GG     A C  C  C A     GG","IOOOOOOOOC CFC COOOOOOOOI","GG     A C  C  C A     GG","GGD D DADO     ODAD D DGG"," G      A       A      G "," GO D    A     A    D OG ","  GDBD    AAAAA    DBDG  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","    GI             IG    ","  GGDJ             JDGG  ","  GDBD    AAAAA    DBDG  "," GO D    A     A    D OG "," G      A       A      G ","GGD D DADO     ODAD D DGG","IOOOOOOOO  CFC  OOOOOOOOI","IBBBBBBBBF F F FBBBBBBBBI","IOOOOOOOO  CFC  OOOOOOOOI","GGD D DADO     ODAD D DGG"," G      A       A      G "," GO D    A     A    D OG ","  GDBD    AAAAA    DBDG  ","  GGDJ             JDGG  ","    GI             IG    ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  GDBD    AAAAA    DBDG  "," GO D    A     A    D OG "," G      A       A      G ","GGD D DADO     ODAD D DGG","GG     A C  C  C A     GG","IOOOOOOOOC CFC COOOOOOOOI","GG     A C  C  C A     GG","GGD D DADO     ODAD D DGG"," G      A       A      G "," GO D    A     A    D OG ","  GDBD    AAAAA    DBDG  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  GDBD     AAA     DBDG  "," GO D    AA   AA    D OG "," G      A       A      G "," G      A       A      G ","GGD D DADO     ODAD D DGG","GGD D DADO     ODAD D DGG","GGD D DADO     ODAD D DGG"," G      A       A      G "," G      A       A      G "," GO D    AA   AA    D OG ","  GDBD     AAA     DBDG  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","   GGI             IGG   ","  G DJ             JD G  ","  GDBD    AAAAA    DBDG  "," GO D    A     A    D OG "," G      A       A      G "," G      A       A      G "," G      A       A      G "," G      A       A      G "," G      A       A      G "," GO D    A     A    D OG ","  GDBD    AAAAA    DBDG  ","  G DJ             JD G  ","   GGI             IGG   ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  GDBD             DBDG  ","  GDBD    AAAAA    DBDG  "," GO D    AA   AA    D OG "," GO D    A     A    D OG "," GO D    A     A    D OG "," GO D    A     A    D OG "," GO D    AA   AA    D OG ","  GDBD    AAAAA    DBDG  ","  GDBD             DBDG  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GDJ             JDG   ","  G DJ             JD G  ","  GDBD     AAA     DBDG  ","  GDBD    AAAAA    DBDG  ","  GDBD    AAAAA    DBDG  ","  GDBD    AAAAA    DBDG  ","  GDBD     AAA     DBDG  ","  G DJ             JD G  ","   GDJ             JDG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","    GI             IG    ","   GGI             IGG   ","   GDJ             JDG   ","   GDJ             JDG   ","  GGDJ             JDGG  ","   GDJ             JDG   ","   GDJ             JDG   ","   GGI             IGG   ","    GI             IG    ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","    GI     III     IG    ","    GI    IJJJI    IG    ","    GI    IJJJI    IG    ","    GI    IJJJI    IG    ","    GI     III     IG    ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","           JOJ           ","           OBO           ","           JOJ           ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","           EEE           ","          E O E          ","         E JOJ E         ","         EOOBOOE         ","         E JOJ E         ","          E O E          ","           EEE           ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","           JOJ           ","           OBO           ","           JOJ           ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","          EEEEE          ","         E  O  E         ","        E   O   E        ","        E  JOJ  E        ","        EOOOBOOOE        ","        E  JOJ  E        ","        E   O   E        ","         E  O  E         ","          EEEEE          ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","           YYY           ","           O O           ","         O     O         ","                         ","        O  JOJ  O        ","           OBO           ","        O  JOJ  O        ","                         ","         O     O         ","           O O           ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","                         ","                         ","                         ","                         ","          YY~YY          ","          GGGGG          ","         GHHGHHG         ","        GH  G  HG        ","        GH JGJ HG        ","        GGGGBGGGG        ","        GH JGJ HG        ","        GH  G  HG        ","         GHHGHHG         ","          GGGGG          ","                         ","                         ","                         ","                         ","                         ","                         ","                         ","                         "},
        {"                         ","                         ","                         ","         GGGGGGG         ","       GGOOOOOOOGG       ","      GOOO     OOOG      ","     GOO         OOG     ","    GOO   YYYYY   OOG    ","    GO   GHHHHHG   OG    ","   GOO  GHHHHHHHG  OOG   ","   GO  GHHHHHHHHHG  OG   ","   GO  GHHHHHHHHHG  OG   ","   GO  GHHHHHHHHHG  OG   ","   GO  GHHHHHHHHHG  OG   ","   GO  GHHHHHHHHHG  OG   ","   GOO  GHHHHHHHG  OOG   ","    GO   GHHHHHG   OG    ","    GOO   GGGGG   OOG    ","     GOO         OOG     ","      GOOO     OOOG      ","       GGOOOOOOOGG       ","         GGGGGGG         ","                         ","                         ","                         "},
        {"                         ","         GGGGGGG         ","       GGEEEEEEEGG       ","     GGEEGGGGGGGEEGG     ","    GGEGGGGGGGGGGGEGG    ","   GGEGGGGJJJJJGGGGEGG   ","   GEGGGJJJJJJJJJGGGEG   ","  GEGGGJJJGGGGGJJJGGGEG  ","  GEGGJJJGGGGGGGJJJGGEG  "," GEGGGJJGGGGGGGGGJJGGGEG "," GEGGJJGGGGGGGGGGGJJGGEG "," GEGGJJGGGGGGGGGGGJJGGEG "," GEGGJJGGGGGGGGGGGJJGGEG "," GEGGJJGGGGGGGGGGGJJGGEG "," GEGGJJGGGGGGGGGGGJJGGEG "," GEGGGJJGGGGGGGGGJJGGGEG ","  GEGGJJJGGGGGGGJJJGGEG  ","  GEGGGJJJGGGGGJJJGGGEG  ","   GEGGGJJJJJJJJJGGGEG   ","   GGEGGGGJJJJJGGGGEGG   ","    GGEGGGGGGGGGGGEGG    ","     GGEEGGGGGGGEEGG     ","       GGEEEEEEEGG       ","         GGGGGGG         ","                         "},
        {"         GGGGGGG         ","       GGZZZZZZZGG       ","     GGZZZZZZZZZZZGG     ","    GZZZZZZZZZZZZZZZG    ","   GZZZZZZZZZZZZZZZZZG   ","  GZZZZZZZZZZZZZZZZZZZG  ","  GZZZZZZZZZZZZZZZZZZZG  "," GZZZZZZZZZZZZZZZZZZZZZG "," GZZZZZZZZZZZZZZZZZZZZZG ","GZZZZZZZZZZZZZZZZZZZZZZZG","GZZZZZZZZZZZZZZZZZZZZZZZG","GZZZZZZZZZZZZZZZZZZZZZZZG","GZZZZZZZZZZZZZZZZZZZZZZZG","GZZZZZZZZZZZZZZZZZZZZZZZG","GZZZZZZZZZZZZZZZZZZZZZZZG","GZZZZZZZZZZZZZZZZZZZZZZZG"," GZZZZZZZZZZZZZZZZZZZZZG "," GZZZZZZZZZZZZZZZZZZZZZG ","  GZZZZZZZZZZZZZZZZZZZG  ","  GZZZZZZZZZZZZZZZZZZZG  ","   GZZZZZZZZZZZZZZZZZG   ","    GZZZZZZZZZZZZZZZG    ","     GGZZZZZZZZZZZGG     ","       GGZZZZZZZGG       ","         GGGGGGG         "}
    };
    private final String[][] shapeMK2 = new String[][]{
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                GGGGGGGGGGGGG                                ","                                ZZZZZZZZZZZZZ                                ","                                GGGGGGGGGGGGG                                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                GGGGGGGGGGGGG                                ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                            ZZZZBBBBBBBBBBBBBZZZZ                            ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                                GGGGGGGGGGGGG                                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                GGGGGGGGGGGGG                                ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                         ZZZBBBBBBBBBBBBBBBBBBBBBZZZ                         ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                                GGGGGGGGGGGGG                                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                            GGGGIIIIIIIIIIIIIGGGG                            ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                      ZZZBBBBBBBBBBBBBBBBBBBBBBBBBBBZZZ                      ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                            GGGGIIIIIIIIIIIIIGGGG                            ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                         GGGIIIIIIIIIIIIIIIIIIIIIGGG                         ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                    GGBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBGG                    ","                    ZZBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBZZ                    ","                    GGBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBGG                    ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                         GGGIIIIIIIIIIIIIIIIIIIIIGGG                         ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                       GGIIIIIIIIIIIIIIIIIIIIIIIIIIIGG                       ","                    GGGBBBBBBBBBBIIIJLLLJIIIBBBBBBBBBBGGG                    ","                   GBBBBBBBBBBBBBIIJLCCCLJIIBBBBBBBBBBBBBG                   ","                   ZBBBBBBBBBBBBBIIJLCCCLJIIBBBBBBBBBBBBBZ                   ","                   GBBBBBBBBBBBBBIIJLCCCLJIIBBBBBBBBBBBBBG                   ","                    GGGBBBBBBBBBBIIIJLLLJIIIBBBBBBBBBBGGG                    ","                       GGIIIIIIIIIIIIIIIIIIIIIIIIIIIGG                       ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                     GGIIIIIIIIIIGGGGGGGGGGGIIIIIIIIIIGG                     ","                   GGBBBBBBB     GGJICCCIJGG     BBBBBBBGG                   ","                 GGBBBBBBBBB     GJICCCCCIJG     BBBBBBBBBGG                 ","                 ZZBBBBBBBBB     GJICCCCCIJG     BBBBBBBBBZZ                 ","                 GGBBBBBBBBB     GJICCCCCIJG     BBBBBBBBBGG                 ","                   GGBBBBBBB     GGJICCCIJGG     BBBBBBBGG                   ","                     GGIIIIIIIIIIGGGGGGGGGGGIIIIIIIIIIGG                     ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                   GGIIIIIIIIGGGG   OOOOO   GGGGIIIIIIIIGG                   ","                 GGBBBBBBB   AAAG  OGGGGGO  GAAA   BBBBBBBGG                 ","                GBBBBBBBBB   AAAG OGGCCCGGO GAAA   BBBBBBBBBG                ","                ZBBBBBBBBB   AAAG OGICCCIGO GAAA   BBBBBBBBBZ                ","                GBBBBBBBBB   AAAG OGGCCCGGO GAAA   BBBBBBBBBG                ","                 GGBBBBBBB   AAAG  OGGGGGO  GAAA   BBBBBBBGG                 ","                   GGIIIIIIIIGGGG   OOOOO   GGGGIIIIIIIIGG                   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                  GIIIIIIIGGG        GGG        GGGIIIIIIIG                  ","                GGBBBBB   AAAGGG    GIIIG    GGGAAA   BBBBBGG                ","              GGBBBBBBB   AAA      GIOOOIG      AAA   BBBBBBBGG              ","              ZZBBBBBBB   AAA      GIOOOIG      AAA   BBBBBBBZZ              ","              GGBBBBBBB   AAA      GIOOOIG      AAA   BBBBBBBGG              ","                GGBBBBB   AAAGGG    GIIIG    GGGAAA   BBBBBGG                ","                  GIIIIIIIGGG        GGG        GGGIIIIIIIG                  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                GGIIIIIIGG                         GGIIIIIIGG                ","              GGBBBBB   AAGGG                   GGGAA   BBBBBGG              ","             GBBBBBBB   AA                         AA   BBBBBBBG             ","             ZBBBBBBB   AA                         AA   BBBBBBBZ             ","             GBBBBBBB   AA                         AA   BBBBBBBG             ","              GGBBBBB   AAGGG                   GGGAA   BBBBBGG              ","                GGIIIIIIGG                         GGIIIIIIGG                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","               GIIIIIIGG                             GGIIIIIIG               ","             GGBBBBB  AAGG                         GGAA  BBBBBGG             ","            GBBBBBBB  AA                             AA  BBBBBBBG            ","            ZBBBBBBB  AA                             AA  BBBBBBBZ            ","            GBBBBBBB  AA                             AA  BBBBBBBG            ","             GGBBBBB  AAGG                         GGAA  BBBBBGG             ","               GIIIIIIGG                             GGIIIIIIG               ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","              GIIIIIGG                                 GGIIIIIG              ","            GGBBBB  AAGG                             GGAA  BBBBGG            ","           GBBBBBB  AA                                 AA  BBBBBBG           ","           ZBBBBBB  AA                                 AA  BBBBBBZ           ","           GBBBBBB  AA                                 AA  BBBBBBG           ","            GGBBBB  AAGG                             GGAA  BBBBGG            ","              GIIIIIGG                                 GGIIIIIG              ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                      O                                      ","                                    OOOOO                                    ","                                  OOGGGGGOO                                  ","             GIIIIIG              OGGGGGGGO              GIIIIIG             ","           GGBBBB  AGG           OGGGJJJGGGO           GGA  BBBBGG           ","          GBBBBBB  A             OGGJOOOJGGO             A  BBBBBBG          ","          ZBBBBBB  A            OOGGJOOOJGGOO            A  BBBBBBZ          ","          GBBBBBB  A             OGGJOOOJGGO             A  BBBBBBG          ","           GGBBBB  AGG           OGGGJJJGGGO           GGA  BBBBGG           ","             GIIIIIG              OGGGGGGGO              GIIIIIG             ","                                  OOGGGGGOO                                  ","                                    OOOOO                                    ","                                      O                                      ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                    GEOEG                                    ","                                  GGEEEEEGG                                  ","                                 GEEGGGGGEEG                                 ","                                GEGGBBBBBGGEG                                ","            GIIIIIG             GEGBNNNNNBGEG             GIIIIIG            ","          GGBBBB  AG           GEGBNGKKKGNBGEG           GA  BBBBGG          ","         GBBBBBB  A            GEGBNKCCCKNBGEG            A  BBBBBBG         ","         ZBBBBBB  A            OEGBNKCCCKNBGEO            A  BBBBBBZ         ","         GBBBBBB  A            GEGBNKCCCKNBGEG            A  BBBBBBG         ","          GGBBBB  AG           GEGBNGKKKGNBGEG           GA  BBBBGG          ","            GIIIIIG             GEGBNNNNNBGEG             GIIIIIG            ","                                GEGGBBBBBGGEG                                ","                                 GEEGGGGGEEG                                 ","                                  GGEEEEEGG                                  ","                                    GEOEG                                    ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                   GGEGEGG                                   ","                                 GGGBBGBBGGG                                 ","                                GGBBBBBBBBBGG                                ","                               GGBBBNNNNNBBBGG                               ","                               GBBBNBBBBBNBBBG                               ","           GIIIIIG            GGBBNBNNNNNBNBBGG            GIIIIIG           ","         GGBBBB  AG           GBBNBNGKKKGNBNBBG           GA  BBBBGG         ","        GBBBBBB  A            GBBNBNKKKKKNBNBBG            A  BBBBBBG        ","        ZBBBBBB  A            EEBNBNKKKKKNBNBEE            A  BBBBBBZ        ","        GBBBBBB  A            GBBNBNKKKKKNBNBBG            A  BBBBBBG        ","         GGBBBB  AG           GBBNBNGKKKGNBNBBG           GA  BBBBGG         ","           GIIIIIG            GGBBNBNNNNNBNBBGG            GIIIIIG           ","                               GBBBNBBBBBNBBBG                               ","                               GGBBBNNNNNBBBGG                               ","                                GGBBBBBBBBBGG                                ","                                 GGGBBGBBGGG                                 ","                                   GGEGEGG                                   ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   GGEGEGG                                   ","                                 GGNNNNNNNGG                                 ","                                GNNNBBBBBNNNG                                ","                               GNNBBBBBBBBBNNG                               ","                              GNNBBBNNNNNBBBNNG                              ","                              GNBBBNBBBBBNBBBNG                              ","          GIIIIIG            GNNBBNBNNNNNBNBBNNG            GIIIIIG          ","         GBBBB  AG           GNBBNBNGKKKGNBNBBNG           GA  BBBBG         ","        GBBBBB  A            GNBBNBNKKKKKNBNBBNG            A  BBBBBG        ","        ZBBBBB  A            ENBBNBNKKKKKNBNBBNE            A  BBBBBZ        ","        GBBBBB  A            GNBBNBNKKKKKNBNBBNG            A  BBBBBG        ","         GBBBB  AG           GNBBNBNGKKKGNBNBBNG           GA  BBBBG         ","          GIIIIIG            GNNBBNBNNNNNBNBBNNG            GIIIIIG          ","                              GNBBBNBBBBBNBBBNG                              ","                              GNNBBBNNNNNBBBNNG                              ","                               GNNBBBBBBBBBNNG                               ","                                GNNNBBBBBNNNG                                ","                                 GGNNNNNNNGG                                 ","                                   GGEGEGG                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   EEEEEEE                                   ","                                 EEGGGGGGGEE                                 ","                                EGGGIIIIIGGGE                                ","                               EGGIIIIIIIIIGGE                               ","                              EGGIIIGGGGGIIIGGE                              ","                              EGIIIGGGGGGGIIIGE                              ","         GIIIIIG             EGGIIGGGGGGGGGIIGGE             GIIIIIG         ","        GBBBB  AG            EGIIGGGGGGGGGGGIIGE            GA  BBBBG        ","       GBBBBB  A             EGIIGGGGGGGGGGGIIGE             A  BBBBBG       ","       ZBBBBB  A             EGIIGGGGGGGGGGGIIGE             A  BBBBBZ       ","       GBBBBB  A             EGIIGGGGGGGGGGGIIGE             A  BBBBBG       ","        GBBBB  AG            EGIIGGGGGGGGGGGIIGE            GA  BBBBG        ","         GIIIIIG             EGGIIGGGGGGGGGIIGGE             GIIIIIG         ","                              EGIIIGGGGGGGIIIGE                              ","                              EGGIIIGGGGGIIIGGE                              ","                               EGGIIIIIIIIIGGE                               ","                                EGGGIIIIIGGGE                                ","                                 EEGGGGGGGEE                                 ","                                   EEEEEEE                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   GGGGGGG                                   ","                                 GGOOOOOOOGG                                 ","                                GOOO     OOOG                                ","                               GOO         OOG                               ","                              GOO   GGGGG   OOG                              ","                              GO   GJJJJJG   OG                              ","         GIIIIG              GOO  GJGGGGGJG  OOG              GIIIIG         ","       GGBBB  AG             GO  GJGGJJJGGJG  OG             GA  BBBGG       ","      GBBBBB  A              GO  GJGJGGGJGJG  OG              A  BBBBBG      ","      ZBBBBB  A              GO  GJGJGGGJGJG  OG              A  BBBBBZ      ","      GBBBBB  A              GO  GJGJGGGJGJG  OG              A  BBBBBG      ","       GGBBB  AG             GO  GJGGJJJGGJG  OG             GA  BBBGG       ","         GIIIIG              GOO  GJGGGGGJG  OOG              GIIIIG         ","                              GO   GJJJJJG   OG                              ","                              GOO   GGGGG   OOG                              ","                               GOO         OOG                               ","                                GOOO     OOOG                                ","                                 GGOOOOOOOGG                                 ","                                   GGGGGGG                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","        GIIIIG                                                 GIIIIG        ","       GBBB  AG                                               GA  BBBG       ","      GBBBB  A                                                 A  BBBBG      ","      ZBBBB  A                                                 A  BBBBZ      ","      GBBBB  A                                                 A  BBBBG      ","       GBBB  AG                                               GA  BBBG       ","        GIIIIG                                                 GIIIIG        ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","       GIIIIG                                                   GIIIIG       ","      GBBBB AG                                                 GA BBBBG      ","     GBBBBB A                                                   A BBBBBG     ","     ZBBBBB A                                                   A BBBBBZ     ","     GBBBBB A                                                   A BBBBBG     ","      GBBBB AG                                                 GA BBBBG      ","       GIIIIG                                                   GIIIIG       ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   GGGGGGG                                   ","                                 GGOOOOOOOGG                                 ","                                GOOO     OOOG                                ","                               GOO         OOG                               ","                              GOO   GGGGG   OOG                              ","                              GO   GIIIIIG   OG                              ","       GIIIG                 GOO  GIGGGGGIG  OOG                 GIIIG       ","     GGBBB AG                GO  GIGGIIIGGIG  OG                GA BBBGG     ","    GBBBBB A                 GO  GIGIGGGIGIG  OG                 A BBBBBG    ","    ZBBBBB A                 GO  GIGIGGGIGIG  OG                 A BBBBBZ    ","    GBBBBB A                 GO  GIGIGGGIGIG  OG                 A BBBBBG    ","     GGBBB AG                GO  GIGGIIIGGIG  OG                GA BBBGG     ","       GIIIG                 GOO  GIGGGGGIG  OOG                 GIIIG       ","                              GO   GIIIIIG   OG                              ","                              GOO   GGGGG   OOG                              ","                               GOO         OOG                               ","                                GOOO     OOOG                                ","                                 GGOOOOOOOGG                                 ","                                   GGGGGGG                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                   GGGGGGG                                   ","                                 GGEEEEEEEGG                                 ","                               GGEEGGGGGGGEEGG                               ","                              GGEGGGGGGGGGGGEGG                              ","                             GGEGGGGJJJJJGGGGEGG                             ","                             GEGGGJJJJJJJJJGGGEG                             ","                            GEGGGJJJGGGGGJJJGGGEG                            ","                            GEGGJJJGGGGGGGJJJGGEG                            ","      GIIIIG               GEGGGJJGGGGGGGGGJJGGGEG               GIIIIG      ","     GBBB  AG              GEGGJJGGGGGGGGGGGJJGGEG              GA  BBBG     ","    GBBBB  A               GEGGJJGGGGGGGGGGGJJGGEG               A  BBBBG    ","    ZBBBB  A               GEGGJJGGGGGGGGGGGJJGGEG               A  BBBBZ    ","    GBBBB  A               GEGGJJGGGGGGGGGGGJJGGEG               A  BBBBG    ","     GBBB  AG              GEGGJJGGGGGGGGGGGJJGGEG              GA  BBBG     ","      GIIIIG               GEGGGJJGGGGGGGGGJJGGGEG               GIIIIG      ","                            GEGGJJJGGGGGGGJJJGGEG                            ","                            GEGGGJJJGGGGGJJJGGGEG                            ","                             GEGGGJJJJJJJJJGGGEG                             ","                             GGEGGGGJJJJJGGGGEGG                             ","                              GGEGGGGGGGGGGGEGG                              ","                               GGEEGGGGGGGEEGG                               ","                                 GGEEEEEEEGG                                 ","                                   GGGGGGG                                   ","                                                                             "},
        {"                                   GGGGGGG                                   ","                                 GGEEEEEEEGG                                 ","                               GGEEEEEEEEEEEGG                               ","                              GEEEEEEEEEEEEEEEG                              ","                             GEEEEEEEEEEEEEEEEEG                             ","                            GEEEEEEEEEEEEEEEEEEEG                            ","                            GEEEEEEEEEEEEEEEEEEEG                            ","                           GEEEEEEEEEEEEEEEEEEEEEG                           ","                           GEEEEEEEEEEEEEEEEEEEEEG                           ","      GIIIG               GEEEEEEEEEEEEEEEEEEEEEEEG               GIIIG      ","    GGBBB AG              GEEEEEEEEEEEEEEEEEEEEEEEG              GA BBBGG    ","   GBBBBB A               GEEEEEEEEEEEEEEEEEEEEEEEG               A BBBBBG   ","   ZBBBBB A               GEEEEEEEEEEEEEEEEEEEEEEEG               A BBBBBZ   ","   GBBBBB A               GEEEEEEEEEEEEEEEEEEEEEEEG               A BBBBBG   ","    GGBBB AG              GEEEEEEEEEEEEEEEEEEEEEEEG              GA BBBGG    ","      GIIIG               GEEEEEEEEEEEEEEEEEEEEEEEG               GIIIG      ","                           GEEEEEEEEEEEEEEEEEEEEEG                           ","                           GEEEEEEEEEEEEEEEEEEEEEG                           ","                            GEEEEEEEEEEEEEEEEEEEG                            ","                            GEEEEEEEEEEEEEEEEEEEG                            ","                             GEEEEEEEEEEEEEEEEEG                             ","                              GEEEEEEEEEEEEEEEG                              ","                               GGEEEEEEEEEEEGG                               ","                                 GGEEEEEEEGG                                 ","                                   GGGGGGG                                   "},
        {"                                                                             ","                                   GGGGGGG                                   ","                                 GGEEEEEEEGG                                 ","                               GGEEGGGGGGGEEGG                               ","                              GGEGGGGGGGGGGGEGG                              ","                             GGEGGGGJJJJJGGGGEGG                             ","                             GEGGGJJJJJJJJJGGGEG                             ","                            GEGGGJJJGGGGGJJJGGGEG                            ","                            GEGGJJJGGGGGGGJJJGGEG                            ","     GIIIIG                GEGGGJJGGGGGGGGGJJGGGEG                GIIIIG     ","    GBBB  AG               GEGGJJGGGGGGGGGGGJJGGEG               GA  BBBG    ","   GBBBB  A                GEGGJJGGGGGGGGGGGJJGGEG                A  BBBBG   ","   ZBBBB  A                GEGGJJGGGGGGGGGGGJJGGEG                A  BBBBZ   ","   GBBBB  A                GEGGJJGGGGGGGGGGGJJGGEG                A  BBBBG   ","    GBBB  AG               GEGGJJGGGGGGGGGGGJJGGEG               GA  BBBG    ","     GIIIIG                GEGGGJJGGGGGGGGGJJGGGEG                GIIIIG     ","                            GEGGJJJGGGGGGGJJJGGEG                            ","                            GEGGGJJJGGGGGJJJGGGEG                            ","                             GEGGGJJJJJJJJJGGGEG                             ","                             GGEGGGGJJJJJGGGGEGG                             ","                              GGEGGGGGGGGGGGEGG                              ","                               GGEEGGGGGGGEEGG                               ","                                 GGEEEEEEEGG                                 ","                                   GGGGGGG                                   ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   GGGGGGG                                   ","                                 GGOOOOOOOGG                                 ","                                GOOO     OOOG                                ","                               GOO         OOG                               ","                              GOO   GGGGG   OOG                              ","                              GO   GHHHHHG   OG                              ","     GIIIG                   GOO  GHHHHHHHG  OOG                   GIIIG     ","    GBBB AG                  GO  GHHHHHHHHHG  OG                  GA BBBG    ","   GBBBB A                   GO  GHHHHHHHHHG  OG                   A BBBBG   ","   ZBBBB A                   GO  GHHHHHHHHHG  OG                   A BBBBZ   ","   GBBBB A                   GO  GHHHHHHHHHG  OG                   A BBBBG   ","    GBBB AG                  GO  GHHHHHHHHHG  OG                  GA BBBG    ","     GIIIG                   GOO  GHHHHHHHG  OOG                   GIIIG     ","                              GO   GHHHHHG   OG                              ","                              GOO   GGGGG   OOG                              ","                               GOO         OOG                               ","                                GOOO     OOOG                                ","                                 GGOOOOOOOGG                                 ","                                   GGGGGGG                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                    GGGGG                                    ","    GIIIIG                         GHHGHHG                         GIIIIG    ","   GBBBB AG                       GH  G  HG                       GA BBBBG   ","  GBBBBB A                        GH JGJ HG                        A BBBBBG  ","  ZBBBBB A                        GGGGBGGGG                        A BBBBBZ  ","  GBBBBB A                        GH JGJ HG                        A BBBBBG  ","   GBBBB AG                       GH  G  HG                       GA BBBBG   ","    GIIIIG                         GHHGHHG                         GIIIIG    ","                                    GGGGG                                    ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                     O O                                     ","    GIIIG                          O     O                          GIIIG    ","   GBBB AG                                                         GA BBBG   ","  GBBBB A                         O  JOJ  O                         A BBBBG  ","  ZBBBB A                            OBO                            A BBBBZ  ","  GBBBB A                         O  JOJ  O                         A BBBBG  ","   GBBB AG                                                         GA BBBG   ","    GIIIG                          O     O                          GIIIG    ","                                     O O                                     ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                    EEEEE                                    ","    GIIIG                          E  O  E                          GIIIG    ","   GBBB AG                        E   O   E                        GA BBBG   ","  GBBBB A                         E  JOJ  E                         A BBBBG  ","  ZBBBB A                         EOOOBOOOE                         A BBBBZ  ","  GBBBB A                         E  JOJ  E                         A BBBBG  ","   GBBB AG                        E   O   E                        GA BBBG   ","    GIIIG                          E  O  E                          GIIIG    ","                                    EEEEE                                    ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIIG                                                           GIIIIG   ","  GBBB  AG                                                         GA  BBBG  "," GBBBB  A                            JOJ                            A  BBBBG "," ZBBBB  A                            OBO                            A  BBBBZ "," GBBBB  A                            JOJ                            A  BBBBG ","  GBBB  AG                                                         GA  BBBG  ","   GIIIIG                                                           GIIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIG                             EEE                             GIIIG   ","  GBBB AG                           E O E                           GA BBBG  "," GBBBB A                           E JOJ E                           A BBBBG "," ZBBBB A                           EOOBOOE                           A BBBBZ "," GBBBB A                           E JOJ E                           A BBBBG ","  GBBB AG                           E O E                           GA BBBG  ","   GIIIG                             EEE                             GIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIG                                                             GIIIG   ","  GBBB AG                                                           GA BBBG  "," GBBBB A                             JOJ                             A BBBBG "," ZBBBB A                             OBO                             A BBBBZ "," GBBBB A                             JOJ                             A BBBBG ","  GBBB AG                                                           GA BBBG  ","   GIIIG                                                             GIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIG                                                             GIIIG   ","  GBBB AG                            III                            GA BBBG  "," GBBBB A                            IJJJI                            A BBBBG "," ZBBBB A                            IJJJI                            A BBBBZ "," GBBBB A                            IJJJI                            A BBBBG ","  GBBB AG                            III                            GA BBBG  ","   GIIIG                                                             GIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GIIIIG                                                             GIIIIG  "," GBBBB AG                                                           GA BBBBG ","GBBBBB A                                                             A BBBBBG","ZBBBBB A                                                             A BBBBBZ","GBBBBB A                                                             A BBBBBG"," GBBBB AG                                                           GA BBBBG ","  GIIIIG                                                             GIIIIG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GIIIG                                                               GIIIG  "," GBBB AG                                                             GA BBBG ","GBBBB A                                                               A BBBBG","ZBBBB A                                                               A BBBBZ","GBBBB A                                                               A BBBBG"," GBBB AG                                                             GA BBBG ","  GIIIG                                                               GIIIG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GIIIG                                                               GIIIG  "," GGGGGGG                                                             GGGGGGG ","GGGAAGGG                                                             GGGAAGGG","ZGGAAGGG                                                             GGGAAGGZ","GGGAAGGG                                                             GGGAAGGG"," GGGGGGG                                                             GGGGGGG ","  GIIIG                                                               GIIIG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GGGGGG                                                             GGGGGG  "," GHHHJA                                                               AJHHHG ","GGJ  JA                                                               AJ  JGG","ZGJ  JA                                                               AJ  JGZ","GGJ  JA                                                               AJ  JGG"," GHHHJA                                                               AJHHHG ","  GGGGGG                                                             GGGGGG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GGGGGG                                                             GGGGGG  "," GHHHJA                                                               AJHHHG "," GLLLJA                                                               AJLLLG ","GKKCCGA                                                               AGCCKKG","GKKCCGA                                                               AGCCKKG","GKKCCGA                                                               AGCCKKG"," GLLLJA                                                               AJLLLG "," GHHHJA                                                               AJHHHG ","  GGGGGG                                                             GGGGGG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GAAGGG                                                             GGGAAG  "," GJ  JA                                                               AJ  JG ","GKKCCGA                                                               AGCCKKG","GLBCO A                                                               A OCBLG","GLBCO A                                                               A OCBLG","GLBCO A                                                               A OCBLG","GKKCCGA                                                               AGCCKKG"," GJ  JA                                                               AJ  JG ","  GAAGGG                                                             GGGAAG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GAAGGG                                                             GGGAAG  "," GJ  JA                                                               AJ  JG ","GKKCCGA                                                               AGCCKKG","GLBCO A                                                               A OCBLG","GLBCO A                                                               A OCBLG","GLBCO A                                                               A OCBLG","GKKCCGA                                                               AGCCKKG"," GJ  JA                                                               AJ  JG ","  GAAGGG                                                             GGGAAG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GAAGGG                                                             GGGAAG  "," GJ  JA                                                               AJ  JG ","GKKCCGA                                                               AGCCKKG","GLBCO A                                                               A OCBLG","GLBCO A                                                               A OCBLG","GLBCO A                                                               A OCBLG","GKKCCGA                                                               AGCCKKG"," GJ  JA                                                               AJ  JG ","  GAAGGG                                                             GGGAAG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GGGGGG                                                             GGGGGG  "," GHHHJA                                                               AJHHHG "," GLLLJA                                                               AJLLLG ","GKKCCGA                                                               AGCCKKG","GKKCCGA                                                               AGCCKKG","GKKCCGA                                                               AGCCKKG"," GLLLJA                                                               AJLLLG "," GHHHJA                                                               AJHHHG ","  GGGGGG                                                             GGGGGG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GGGGGG                                                             GGGGGG  "," GHHHJA                                                               AJHHHG ","GGJ  JA                                                               AJ  JGG","ZGJ  JA                                                               AJ  JGZ","GGJ  JA                                                               AJ  JGG"," GHHHJA                                                               AJHHHG ","  GGGGGG                                                             GGGGGG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GIIIG                                                               GIIIG  "," GGGGGGG                                                             GGGGGGG ","GGGAAGGG                                                             GGGAAGGG","ZGGAAGGG                                                             GGGAAGGZ","GGGAAGGG                                                             GGGAAGGG"," GGGGGGG                                                             GGGGGGG ","  GIIIG                                                               GIIIG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GIIIG                                                               GIIIG  "," GBBB AG                                                             GA BBBG ","GBBBB A                                                               A BBBBG","ZBBBB A                                                               A BBBBZ","GBBBB A                                                               A BBBBG"," GBBB AG                                                             GA BBBG ","  GIIIG                                                               GIIIG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","  GIIIIG                                                             GIIIIG  "," GBBBB AG                                                           GA BBBBG ","GBBBBB A                                                             A BBBBBG","ZBBBBB A                                                             A BBBBBZ","GBBBBB A                                                             A BBBBBG"," GBBBB AG                                                           GA BBBBG ","  GIIIIG                                                             GIIIIG  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIG                                                             GIIIG   ","  GBBB AG                                                           GA BBBG  "," GBBBB A                                                             A BBBBG "," ZBBBB A                                                             A BBBBZ "," GBBBB A                                                             A BBBBG ","  GBBB AG                                                           GA BBBG  ","   GIIIG                                                             GIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIG                                                             GIIIG   ","  GBBB AG                                                           GA BBBG  "," GBBBB A                                                             A BBBBG "," ZBBBB A                                                             A BBBBZ "," GBBBB A                                                             A BBBBG ","  GBBB AG                                                           GA BBBG  ","   GIIIG                                                             GIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIG                                                             GIIIG   ","  GBBB AG                                                           GA BBBG  "," GBBBB A                                                             A BBBBG "," ZBBBB A                                                             A BBBBZ "," GBBBB A                                                             A BBBBG ","  GBBB AG                                                           GA BBBG  ","   GIIIG                                                             GIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","   GIIIIG                                                           GIIIIG   ","  GBBB  AG                                                         GA  BBBG  "," GBBBB  A                                                           A  BBBBG "," ZBBBB  A                                                           A  BBBBZ "," GBBBB  A                                                           A  BBBBG ","  GBBB  AG                                                         GA  BBBG  ","   GIIIIG                                                           GIIIIG   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","    GIIIG                                                           GIIIG    ","   GBBB AG                                                         GA BBBG   ","  GBBBB A                                                           A BBBBG  ","  ZBBBB A                                                           A BBBBZ  ","  GBBBB A                                                           A BBBBG  ","   GBBB AG                                                         GA BBBG   ","    GIIIG                                                           GIIIG    ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","    GIIIG                                                           GIIIG    ","   GBBB AG                                                         GA BBBG   ","  GBBBB A                                                           A BBBBG  ","  ZBBBB A                                                           A BBBBZ  ","  GBBBB A                                                           A BBBBG  ","   GBBB AG                                                         GA BBBG   ","    GIIIG                                                           GIIIG    ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","    GIIIIG                                                         GIIIIG    ","   GBBBB AG                                                       GA BBBBG   ","  GBBBBB A                                                         A BBBBBG  ","  ZBBBBB A                                                         A BBBBBZ  ","  GBBBBB A                                                         A BBBBBG  ","   GBBBB AG                                                       GA BBBBG   ","    GIIIIG                                                         GIIIIG    ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","     GIIIG                                                         GIIIG     ","    GBBB AG                                                       GA BBBG    ","   GBBBB A                                                         A BBBBG   ","   ZBBBB A                                                         A BBBBZ   ","   GBBBB A                                                         A BBBBG   ","    GBBB AG                                                       GA BBBG    ","     GIIIG                                                         GIIIG     ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","     GIIIIG                                                       GIIIIG     ","    GBBB  AG                                                     GA  BBBG    ","   GBBBB  A                                                       A  BBBBG   ","   ZBBBB  A                                                       A  BBBBZ   ","   GBBBB  A                                                       A  BBBBG   ","    GBBB  AG                                                     GA  BBBG    ","     GIIIIG                                                       GIIIIG     ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","      GIIIG                                                       GIIIG      ","    GGBBB AG                                                     GA BBBGG    ","   GBBBBB A                                                       A BBBBBG   ","   ZBBBBB A                                                       A BBBBBZ   ","   GBBBBB A                                                       A BBBBBG   ","    GGBBB AG                                                     GA BBBGG    ","      GIIIG                                                       GIIIG      ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                   GGGGGGG                                   ","                                 GGEEEEEEEGG                                 ","                               GGEEGGGGGGGEEGG                               ","                              GGEGGGGGGGGGGGEGG                              ","                             GGEGGGGJJJJJGGGGEGG                             ","                             GEGGGJJJJJJJJJGGGEG                             ","                            GEGGGJJJGGGGGJJJGGGEG                            ","                            GEGGJJJGGGGGGGJJJGGEG                            ","      GIIIIG               GEGGGJJGGGGGGGGGJJGGGEG               GIIIIG      ","     GBBB  AG              GEGGJJGGGGGGGGGGGJJGGEG              GA  BBBG     ","    GBBBB  A               GEGGJJGGGGGGGGGGGJJGGEG               A  BBBBG    ","    ZBBBB  A               GEGGJJGGGGGGGGGGGJJGGEG               A  BBBBZ    ","    GBBBB  A               GEGGJJGGGGGGGGGGGJJGGEG               A  BBBBG    ","     GBBB  AG              GEGGJJGGGGGGGGGGGJJGGEG              GA  BBBG     ","      GIIIIG               GEGGGJJGGGGGGGGGJJGGGEG               GIIIIG      ","                            GEGGJJJGGGGGGGJJJGGEG                            ","                            GEGGGJJJGGGGGJJJGGGEG                            ","                             GEGGGJJJJJJJJJGGGEG                             ","                             GGEGGGGJJJJJGGGGEGG                             ","                              GGEGGGGGGGGGGGEGG                              ","                               GGEEGGGGGGGEEGG                               ","                                 GGEEEEEEEGG                                 ","                                   GGGGGGG                                   ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   GGGGGGG                                   ","                                 GGOOOOOOOGG                                 ","                                GOOO     OOOG                                ","                               GOO         OOG                               ","                              GOO   GGGGG   OOG                              ","                              GO   GIIIIIG   OG                              ","       GIIIG                 GOO  GIGGGGGIG  OOG                 GIIIG       ","     GGBBB AG                GO  GIGGIIIGGIG  OG                GA BBBGG     ","    GBBBBB A                 GO  GIGIGGGIGIG  OG                 A BBBBBG    ","    ZBBBBB A                 GO  GIGIGGGIGIG  OG                 A BBBBBZ    ","    GBBBBB A                 GO  GIGIGGGIGIG  OG                 A BBBBBG    ","     GGBBB AG                GO  GIGGIIIGGIG  OG                GA BBBGG     ","       GIIIG                 GOO  GIGGGGGIG  OOG                 GIIIG       ","                              GO   GIIIIIG   OG                              ","                              GOO   GGGGG   OOG                              ","                               GOO         OOG                               ","                                GOOO     OOOG                                ","                                 GGOOOOOOOGG                                 ","                                   GGGGGGG                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","       GIIIIG                                                   GIIIIG       ","      GBBBB AG                                                 GA BBBBG      ","     GBBBBB A                                                   A BBBBBG     ","     ZBBBBB A                                                   A BBBBBZ     ","     GBBBBB A                                                   A BBBBBG     ","      GBBBB AG                                                 GA BBBBG      ","       GIIIIG                                                   GIIIIG       ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","        GIIIIG                                                 GIIIIG        ","       GBBB  AG                                               GA  BBBG       ","      GBBBB  A                                                 A  BBBBG      ","      ZBBBB  A                                                 A  BBBBZ      ","      GBBBB  A                                                 A  BBBBG      ","       GBBB  AG                                               GA  BBBG       ","        GIIIIG                                                 GIIIIG        ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   GGGGGGG                                   ","                                 GGOOOOOOOGG                                 ","                                GOOO     OOOG                                ","                               GOO         OOG                               ","                              GOO   GGGGG   OOG                              ","                              GO   GJJJJJG   OG                              ","         GIIIIG              GOO  GJGGGGGJG  OOG              GIIIIG         ","       GGBBB  AG             GO  GJGGJJJGGJG  OG             GA  BBBGG       ","      GBBBBB  A              GO  GJGJGGGJGJG  OG              A  BBBBBG      ","      ZBBBBB  A              GO  GJGJGGGJGJG  OG              A  BBBBBZ      ","      GBBBBB  A              GO  GJGJGGGJGJG  OG              A  BBBBBG      ","       GGBBB  AG             GO  GJGGJJJGGJG  OG             GA  BBBGG       ","         GIIIIG              GOO  GJGGGGGJG  OOG              GIIIIG         ","                              GO   GJJJJJG   OG                              ","                              GOO   GGGGG   OOG                              ","                               GOO         OOG                               ","                                GOOO     OOOG                                ","                                 GGOOOOOOOGG                                 ","                                   GGGGGGG                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   EEEEEEE                                   ","                                 EEGGGGGGGEE                                 ","                                EGGGIIIIIGGGE                                ","                               EGGIIIIIIIIIGGE                               ","                              EGGIIIGGGGGIIIGGE                              ","                              EGIIIGGGGGGGIIIGE                              ","         GIIIIIG             EGGIIGGGGGGGGGIIGGE             GIIIIIG         ","        GBBBB  AG            EGIIGGGGGGGGGGGIIGE            GA  BBBBG        ","       GBBBBB  A             EGIIGGGGGGGGGGGIIGE             A  BBBBBG       ","       ZBBBBB  A             EGIIGGGGGGGGGGGIIGE             A  BBBBBZ       ","       GBBBBB  A             EGIIGGGGGGGGGGGIIGE             A  BBBBBG       ","        GBBBB  AG            EGIIGGGGGGGGGGGIIGE            GA  BBBBG        ","         GIIIIIG             EGGIIGGGGGGGGGIIGGE             GIIIIIG         ","                              EGIIIGGGGGGGIIIGE                              ","                              EGGIIIGGGGGIIIGGE                              ","                               EGGIIIIIIIIIGGE                               ","                                EGGGIIIIIGGGE                                ","                                 EEGGGGGGGEE                                 ","                                   EEEEEEE                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                   GGEGEGG                                   ","                                 GGNNNNNNNGG                                 ","                                GNNNBBBBBNNNG                                ","                               GNNBBBBBBBBBNNG                               ","                              GNNBBBNNNNNBBBNNG                              ","                              GNBBBNBBBBBNBBBNG                              ","          GIIIIIG            GNNBBNBNNNNNBNBBNNG            GIIIIIG          ","         GBBBB  AG           GNBBNBNGKKKGNBNBBNG           GA  BBBBG         ","        GBBBBB  A            GNBBNBNKKKKKNBNBBNG            A  BBBBBG        ","        ZBBBBB  A            ENBBNBNKKKKKNBNBBNE            A  BBBBBZ        ","        GBBBBB  A            GNBBNBNKKKKKNBNBBNG            A  BBBBBG        ","         GBBBB  AG           GNBBNBNGKKKGNBNBBNG           GA  BBBBG         ","          GIIIIIG            GNNBBNBNNNNNBNBBNNG            GIIIIIG          ","                              GNBBBNBBBBBNBBBNG                              ","                              GNNBBBNNNNNBBBNNG                              ","                               GNNBBBBBBBBBNNG                               ","                                GNNNBBBBBNNNG                                ","                                 GGNNNNNNNGG                                 ","                                   GGEGEGG                                   ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                   GGEGEGG                                   ","                                 GGGBBGBBGGG                                 ","                                GGBBBBBBBBBGG                                ","                               GGBBBNNNNNBBBGG                               ","                               GBBBNBBBBBNBBBG                               ","           GIIIIIG            GGBBNBNNNNNBNBBGG            GIIIIIG           ","         GGBBBB  AG           GBBNBNGKKKGNBNBBG           GA  BBBBGG         ","        GBBBBBB  A            GBBNBNKKKKKNBNBBG            A  BBBBBBG        ","        ZBBBBBB  A            EEBNBNKKKKKNBNBEE            A  BBBBBBZ        ","        GBBBBBB  A            GBBNBNKKKKKNBNBBG            A  BBBBBBG        ","         GGBBBB  AG           GBBNBNGKKKGNBNBBG           GA  BBBBGG         ","           GIIIIIG            GGBBNBNNNNNBNBBGG            GIIIIIG           ","                               GBBBNBBBBBNBBBG                               ","                               GGBBBNNNNNBBBGG                               ","                                GGBBBBBBBBBGG                                ","                                 GGGBBGBBGGG                                 ","                                   GGEGEGG                                   ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                    GEOEG                                    ","                                  GGEEEEEGG                                  ","                                 GEEGGGGGEEG                                 ","                                GEGGBBBBBGGEG                                ","            GIIIIIG             GEGBNNNNNBGEG             GIIIIIG            ","          GGBBBB  AG           GEGBNGKKKGNBGEG           GA  BBBBGG          ","         GBBBBBB  A            GEGBNKCCCKNBGEG            A  BBBBBBG         ","         ZBBBBBB  A            OEGBNKCCCKNBGEO            A  BBBBBBZ         ","         GBBBBBB  A            GEGBNKCCCKNBGEG            A  BBBBBBG         ","          GGBBBB  AG           GEGBNGKKKGNBGEG           GA  BBBBGG          ","            GIIIIIG             GEGBNNNNNBGEG             GIIIIIG            ","                                GEGGBBBBBGGEG                                ","                                 GEEGGGGGEEG                                 ","                                  GGEEEEEGG                                  ","                                    GEOEG                                    ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                      O                                      ","                                    OOOOO                                    ","                                  OOGGGGGOO                                  ","             GIIIIIG              OGGGGGGGO              GIIIIIG             ","           GGBBBB  AGG           OGGGJJJGGGO           GGA  BBBBGG           ","          GBBBBBB  A             OGGJOOOJGGO             A  BBBBBBG          ","          ZBBBBBB  A            OOGGJOOOJGGOO            A  BBBBBBZ          ","          GBBBBBB  A             OGGJOOOJGGO             A  BBBBBBG          ","           GGBBBB  AGG           OGGGJJJGGGO           GGA  BBBBGG           ","             GIIIIIG              OGGGGGGGO              GIIIIIG             ","                                  OOGGGGGOO                                  ","                                    OOOOO                                    ","                                      O                                      ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","              GIIIIIGG                                 GGIIIIIG              ","            GGBBBB  AAGG                             GGAA  BBBBGG            ","           GBBBBBB  AA                                 AA  BBBBBBG           ","           ZBBBBBB  AA                                 AA  BBBBBBZ           ","           GBBBBBB  AA                                 AA  BBBBBBG           ","            GGBBBB  AAGG                             GGAA  BBBBGG            ","              GIIIIIGG                                 GGIIIIIG              ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","               GIIIIIIGG                             GGIIIIIIG               ","             GGBBBBB  AAGG                         GGAA  BBBBBGG             ","            GBBBBBBB  AA                             AA  BBBBBBBG            ","            ZBBBBBBB  AA                             AA  BBBBBBBZ            ","            GBBBBBBB  AA                             AA  BBBBBBBG            ","             GGBBBBB  AAGG                         GGAA  BBBBBGG             ","               GIIIIIIGG                             GGIIIIIIG               ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                GGIIIIIIGG                         GGIIIIIIGG                ","              GGBBBBB   AAGGG                   GGGAA   BBBBBGG              ","             GBBBBBBB   AA                         AA   BBBBBBBG             ","             ZBBBBBBB   AA                         AA   BBBBBBBZ             ","             GBBBBBBB   AA                         AA   BBBBBBBG             ","              GGBBBBB   AAGGG                   GGGAA   BBBBBGG              ","                GGIIIIIIGG                         GGIIIIIIGG                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                  GIIIIIIIGGG        GGG        GGGIIIIIIIG                  ","                GGBBBBB   AAAGGG    GIIIG    GGGAAA   BBBBBGG                ","              GGBBBBBBB   AAA      GIOOOIG      AAA   BBBBBBBGG              ","              ZZBBBBBBB   AAA      GIOOOIG      AAA   BBBBBBBZZ              ","              GGBBBBBBB   AAA      GIOOOIG      AAA   BBBBBBBGG              ","                GGBBBBB   AAAGGG    GIIIG    GGGAAA   BBBBBGG                ","                  GIIIIIIIGGG        GGG        GGGIIIIIIIG                  ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                   GGIIIIIIIIGGGG   OOOOO   GGGGIIIIIIIIGG                   ","                 GGBBBBBBB   AAAG  OGGGGGO  GAAA   BBBBBBBGG                 ","                GBBBBBBBBB   AAAG OGGCCCGGO GAAA   BBBBBBBBBG                ","                ZBBBBBBBBB   AAAG OGICCCIGO GAAA   BBBBBBBBBZ                ","                GBBBBBBBBB   AAAG OGGCCCGGO GAAA   BBBBBBBBBG                ","                 GGBBBBBBB   AAAG  OGGGGGO  GAAA   BBBBBBBGG                 ","                   GGIIIIIIIIGGGG   OOOOO   GGGGIIIIIIIIGG                   ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                     GGIIIIIIIIIIGGGGGGGGGGGIIIIIIIIIIGG                     ","                   GGBBBBBBB     GGJICCCIJGG     BBBBBBBGG                   ","                 GGBBBBBBBBB     GJICCCCCIJG     BBBBBBBBBGG                 ","                 ZZBBBBBBBBB     GJICCCCCIJG     BBBBBBBBBZZ                 ","                 GGBBBBBBBBB     GJICCCCCIJG     BBBBBBBBBGG                 ","                   GGBBBBBBB     GGJICCCIJGG     BBBBBBBGG                   ","                     GGIIIIIIIIIIGGGGGGGGGGGIIIIIIIIIIGG                     ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                       GGIIIIIIIIIIIIIIIIIIIIIIIIIIIGG                       ","                    GGGBBBBBBBBBBIIIJLLLJIIIBBBBBBBBBBGGG                    ","                   GBBBBBBBBBBBBBIIJLCCCLJIIBBBBBBBBBBBBBG                   ","                   ZBBBBBBBBBBBBBIIJLCCCLJIIBBBBBBBBBBBBBZ                   ","                   GBBBBBBBBBBBBBIIJLCCCLJIIBBBBBBBBBBBBBG                   ","                    GGGBBBBBBBBBBIIIJLLLJIIIBBBBBBBBBBGGG                    ","                       GGIIIIIIIIIIIIIIIIIIIIIIIIIIIGG                       ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                         GGGIIIIIIIIIIIIIIIIIIIIIGGG                         ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                    GGBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBGG                    ","                    ZZBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBZZ                    ","                    GGBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBGG                    ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                         GGGIIIIIIIIIIIIIIIIIIIIIGGG                         ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                            GGGGIIIIIIIIIIIIIGGGG                            ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                      ZZZBBBBBBBBBBBBBBBBBBBBBBBBBBBZZZ                      ","                      GGGBBBBBBBBBBBBBBBBBBBBBBBBBBBGGG                      ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                            GGGGIIIIIIIIIIIIIGGGG                            ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                GGGGGGGGGGGGG                                ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                         ZZZBBBBBBBBBBBBBBBBBBBBBZZZ                         ","                         GGGBBBBBBBBBBBBBBBBBBBBBGGG                         ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                                GGGGGGGGGGGGG                                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                GGGGGGGGGGGGG                                ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                            ZZZZBBBBBBBBBBBBBZZZZ                            ","                            GGGGBBBBBBBBBBBBBGGGG                            ","                                GGGGGGGGGGGGG                                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "},
        {"                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                GGGGGGGGGGGGG                                ","                                ZZZZZZZZZZZZZ                                ","                                GGGGGGGGGGGGG                                ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             ","                                                                             "}
    };
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_BallLightning_MachineType
        // # (Plasma / Electric) Arc Furnace / Fusion Reactor / Star Kernel Generator
        // #zh_CN 电弧炉 | 等离子电弧炉 | 聚变反应堆 | 星核发生器
        tt.addMachineType(TextEnums.tr("Tooltip_BallLightning_MachineType"))
            // #tr Tooltip_BallLightning_Controller
            // # Controller block for the Ball Lightning
            // #zh_CN 球状闪电的的控制方块
            .addInfo(TextEnums.tr("Tooltip_BallLightning_Controller"))
            // #tr Tooltip_BallLightning.0.01
            // # {\SPACE}
            // #zh_CN {\SPACE}
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.01"))
            // #tr Tooltip_BallLightning.0.02
            // # {\ITALIC} " I closed my eyes and opened them again. "
            // #zh_CN {\ITALIC} " 我闭上眼睛又睁开. "
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.02"))
            // #tr Tooltip_BallLightning.0.03
            // # {\ITALIC} " The rose didn't reappear, "
            // #zh_CN {\ITALIC} " 玫瑰没有再出现, "
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.03"))
            // #tr Tooltip_BallLightning.0.04
            // # {\ITALIC} " but I knew it was there,"
            // #zh_CN {\ITALIC} " 但我知道它就在那里, "
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.04"))
            // #tr Tooltip_BallLightning.0.05
            // # {\ITALIC} " nestled in the amethyst vase."
            // #zh_CN {\ITALIC} " 就插在紫水晶花瓶上. "
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.05"))
            // #tr Tooltip_BallLightning.0.06
            // # {\SPACE}
            // #zh_CN {\SPACE}
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.06"))
            // #tr Tooltip_BallLightning.0.07
            // # {\ITALIC}{\WHITE}The most elemental and ultimate utilization of energy.
            // #zh_CN {\ITALIC}{\WHITE}对能源最基础也是最终极的运用.
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.07"))
            // #tr Tooltip_BallLightning.0.08
            // # The structure requires at least Infinity Coil.
            // #zh_CN 至少需要无尽线圈才可成型
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.08"))
            // #tr Tooltip_BallLightning.0.09
            // # Comprises four machine levels, each unlocking a different mode.
            // #zh_CN 机器拥有4个等级, 依次解锁4种模式,
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.09"))
            // #tr Tooltip_BallLightning.0.10
            // # With each machine tier upgrade, the lower-tier modes benefit from a 4x speed multiplier.
            // #zh_CN 每升级一次机器等级, 更低级的机器模式获得4倍速.
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.10"))
            // #tr Tooltip_BallLightning.0.11
            // # {\GOLD}=== Machine Tier ===
            // #zh_CN {\GOLD}=== 机器等级 ===
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.11"))
            // #tr Tooltip_BallLightning.0.12
            // # The base structure is Tier 1
            // #zh_CN 基础结构为等级1
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.12"))
            // #tr Tooltip_BallLightning.0.13
            // # Gravitational Lens in the control slot unlocks Tier 2
            // #zh_CN 在主机内放入引力透镜解锁等级2
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.13"))
            // #tr Tooltip_BallLightning.0.14
            // # Upgrade Chip in the control slot and utilizing Tier 2 structure unlocks Tier 3
            // #zh_CN 在主机内放入升级芯片且使用2级结构解锁等级3
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.14"))
            // #tr Tooltip_BallLightning.0.15
            // # Utilizing Advanced High Power Coil and then utilizing Teleportation Casing replacing Dimensional Bridge unlock Tier 4
            // #zh_CN 使用进阶高能线圈, 并使用传输机械方块替换维度桥接方块解锁等级4
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.15"))
            // #tr Tooltip_BallLightning.0.16
            // # {\GOLD}=== Machine Mode ===
            // #zh_CN {\GOLD}=== 机器模式 ===
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.16"))
            // #tr Tooltip_BallLightning.0.17
            // # {\YELLOW} (Plasma / Electric) Arc Furnace
            // #zh_CN {\YELLOW} 电弧炉 | 等离子电弧炉
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.17"))
            // #tr Tooltip_BallLightning.0.18
            // # Parallel number = 2 ^ (Compact Fusion Coil Tier * (Coil Tier - 10))
            // #zh_CN 并行数 = 2 ^ (聚变线圈等级 * (线圈等级 - 10))
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.18"))
            // #tr Tooltip_BallLightning.0.19
            // # Tiers above Crude Stabilisation Field Generator block enables perfect overclocks
            // #zh_CN 粗制稳定力场发生器等级+ 解锁无损超频
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.19"))
            // #tr Tooltip_BallLightning.0.20
            // # {\YELLOW} Fusion Reactor
            // #zh_CN {\YELLOW} 聚变反应堆
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.20"))
            // #tr Tooltip_BallLightning.0.21
            // # The maximum Eu consumption is limited at 4 ^ (Compact Fusion Coil Tier - 2) * 1.8 ^ (Field Generator Tier - 1) MAX/t
            // #zh_CN 最高运行功耗为 4 ^ (聚变线圈等级 - 2) * 1.8 ^ (力场发生器等级 - 1) A MAX
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.21"))
            // #tr Tooltip_BallLightning.0.22
            // # 65536x parallel | Perfect overclocks
            // #zh_CN 65536 并行, 无损超频
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.22"))
            // #tr Tooltip_BallLightning.0.23
            // # The max recipe tier is limited by the Compact Fusion Coil Tier
            // #zh_CN 聚变线圈等级决定配方等级
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.23"))
            // #tr Tooltip_BallLightning.0.24
            // # {\YELLOW} Star Kernel Generator
            // #zh_CN {\YELLOW} 星核发生器
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.24"))
            // #tr Tooltip_BallLightning.0.25
            // # Almost infinite parallel | Upgrade the Field Generator for faster speeds
            // #zh_CN 几乎无限的并行, 升级力场发生器以获得更高的速度
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.25"))
            // #tr Tooltip_BallLightning.0.26
            // # Eu Modifier = 1 - 9.9%% * (Field Generator Tier - 1)
            // #zh_CN 每升级一次力场发生器, 降低9.9%%功耗
            .addInfo(TextEnums.tr("Tooltip_BallLightning.0.26"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addInfo(TextEnums.Author_Goderium.getText())
            // #tr Tooltip_BallLightning.0.27
            // # {\LIGHT_PURPLE}Wireless Mode :
            // #zh_CN {\LIGHT_PURPLE}无线模式 :
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.0.27"))
            // #tr Tooltip_BallLightning.0.28
            // # Joining the wireless EU network when machine tier is 4 AND when no energy hatch is installed
            // #zh_CN 机器等级为4且未安装能源仓时进入无线模式
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.0.28"))
            // #tr Tooltip_BallLightning.0.29
            // # The Progressing Time will be fixed at 3.2s, and EU cost increase to {\RED}64{\GRAY}x
            // #zh_CN 处理时间固定为3.2s, 同时耗能提高到{\RED}64{\GRAY}倍
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.0.29"))
            // #tr Tooltip_BallLightning.0.30
            // # Put More Upgrade Chip into the controller block to decrease processing time interval and increase Eu Cost
            // #zh_CN 在控制器方块内安装更多升级芯片以减少处理时间间隔, 同时耗能也会增加
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.0.30"))
            // #tr Tooltip_BallLightning.0.31
            // #  Actual processing time = default / Upgrade Chip Stack Size
            // #zh_CN 实际处理时间 = 默认耗时 / 升级芯片数量
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.0.31"))
            // #tr Tooltip_BallLightning.0.32
            // # Actual EU cost = recipe value * Upgrade Chip Stack Size ^ 2 * 64
            // #zh_CN 实际消耗EU = 默认耗能 * 升级芯片数量 ^ 2 * 64
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.0.32"))
            .addStructureInfo(Text_SeparatingLine)
            // #tr Tooltip_BallLightning.1.01
            // # {\BLUE}Base Multi (Tier{\DARK_PURPLE}1{\BLUE}):
            // #zh_CN {\BLUE}基础结构({\DARK_PURPLE}1{\BLUE}级):
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.01"))
            // #tr Tooltip_BallLightning.1.02
            // # {\GOLD}NEI {\GRAY}preview for details
            // #zh_CN {\GRAY}详见{\GOLD}NEI{\GRAY}预览
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.02"))
            // #tr Tooltip_BallLightning.1.03
            // # {\BLUE}Tier §52 {\BLUE}(Adds To §51{\BLUE}):
            // #zh_CN {\DARK_PURPLE}2{\BLUE}级(在{\DARK_PURPLE}1{\BLUE}级基础上添加):
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.03"))
            // #tr Tooltip_BallLightning.1.04
            // # {\GOLD}818 {\GRAY}xBorosilicate Glass
            // #zh_CN {\GOLD}818 {\GRAY}x硼玻璃
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.04"))
            // #tr Tooltip_BallLightning.1.05
            // # {\GOLD}4144 {\GRAY}xCompact Fusion Coil
            // #zh_CN {\GOLD}4144 {\GRAY}x压缩聚变线圈方块
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.05"))
            // #tr Tooltip_BallLightning.1.06
            // # {\GOLD}1047 {\GRAY}xEuropium Reinforced Radiation Proof Machine Casing
            // #zh_CN {\GOLD}1047 {\GRAY}x铕强化防辐射机械方块
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.06"))
            // #tr Tooltip_BallLightning.1.07
            // # {\GOLD}4631 {\GRAY}xHigh Power Radiation Proof Casing
            // #zh_CN {\GOLD}4631 {\GRAY}x高能防辐射机械方块
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.07"))
            // #tr Tooltip_BallLightning.1.08
            // # {\GOLD}133 {\GRAY}xIntergral Framework V
            // #zh_CN {\GOLD}133 {\GRAY}x基本外壳V
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.08"))
            // #tr Tooltip_BallLightning.1.09
            // # {\GOLD}1616 {\GRAY}xDyson Swarm Energy Receiver Dish Block
            // #zh_CN {\GOLD}1616 {\GRAY}x戴森球能量接收天线方块
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.09"))
            // #tr Tooltip_BallLightning.1.10
            // # {\GOLD}481 {\GRAY}xDyson Swarm Control Center Toroid Casing
            // #zh_CN {\GOLD}481 {\GRAY}x戴森球控制中心环形机械方块
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.10"))
            // #tr Tooltip_BallLightning.1.11
            // # {\GOLD}156 {\GRAY}xDyson Swarm Energy Receiver Base Casing
            // #zh_CN {\GOLD}156 {\GRAY}x戴森球能量接收基座机械方块
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.11"))
            // #tr Tooltip_BallLightning.1.12
            // # {\GOLD}66 {\GRAY}xDyson Swarm Control Center Base Casing
            // #zh_CN {\GOLD}66 {\GRAY}x戴森球控制中心基座机械方块
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.12"))
            // #tr Tooltip_BallLightning.1.13
            // # {\GOLD}162 {\GRAY}xTeleportation Casing (T4) or Dimensional Bridge (T3)
            // #zh_CN {\GOLD}162 {\GRAY}x传输机械方块(T4)或维度桥接方块(T3)
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.13"))
            // #tr Tooltip_BallLightning.1.14
            // # {\GOLD}360 {\GRAY}xSuperconductor Base UIV Frame Box
            // #zh_CN {\GOLD}360 {\GRAY}xUIV超导粗胚框架
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.14"))
            // #tr Tooltip_BallLightning.1.15
            // # {\GOLD}538 {\GRAY}xNeutronium Frame Box
            // #zh_CN {\GOLD}538 {\GRAY}x中子框架
            .addStructureInfo(TextEnums.tr("Tooltip_BallLightning.1.15"))
            .addStructureInfo(Text_SeparatingLine)
            .addStructureInfo(TextLocalization.Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(ModName);
        return tt;
    }
    // spotless:on
}
