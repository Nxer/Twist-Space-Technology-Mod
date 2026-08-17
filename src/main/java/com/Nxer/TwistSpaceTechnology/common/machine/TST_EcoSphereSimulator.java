package com.Nxer.TwistSpaceTechnology.common.machine;

import static bartworks.common.loaders.ItemRegistry.bw_realglas;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing02;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereFluidAreaHandler.FluidArea.LOWER_SOURCE;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereFluidAreaHandler.FluidArea.MAIN_SOURCE;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereFluidAreaHandler.FluidArea.UPPER_SOURCE;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereFluidAreaHandler.MAIN_SOURCE_LAST_LAYER;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.getBlueprintWithDot;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockHint;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static java.util.stream.Collectors.toList;
import static vazkii.botania.common.block.ModBlocks.seaLamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.CropsNHFarm;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.AquaticZoneSimulatorMode;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.ArtificialGreenHouseMode;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.DirectedMobClonerMode;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereFluidAreaHandler;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.TreeGrowthSimulatorMode;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class TST_EcoSphereSimulator extends GTCM_MultiMachineBase<TST_EcoSphereSimulator> {

    // region Class Constructor
    public TST_EcoSphereSimulator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_EcoSphereSimulator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_EcoSphereSimulator(this.mName);
    }

    // region Structure

    public static final int MODE_RECIPE_DURATION = 20;
    private static final int MODE_BEACON_CHECK_INTERVAL = 20;

    private int controllerTier = 0;
    private int boundMode = -1;
    private int pendingMode = -1;
    private boolean modeBeaconPresent = false;
    private boolean cleaningRequested = false;
    private boolean cleaningRunActive = false;
    private int directedMobClonerDebugRecipeId = 0;
    private boolean directedMobClonerDebugActive = false;
    private boolean directedMobClonerDebugStopPending = false;
    private long availableInputPower = 0;
    private String fluidAreaFluidName = "";
    private boolean fluidAreaInitialized = false;
    private int fluidAreaFillDuration = 0;
    private FluidStack missingFluidAreaInput;
    private static ItemStack FountOfEcology;
    private static ItemStack Offspring;

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Arboreal Genesis
         * 1 - Aquatic Simulation
         * 2 - Green House Simulator
         * 3 - Directed Mob Cloning
         */
        return 4;
    }

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID,
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT };

    public boolean isTierTwo() {
        return getStructureTier() >= 2;
    }

    public int getStructureTier() {
        return controllerTier + 1;
    }

    public int getModeBeaconTier() {
        ItemStack beacon = getControllerSlot();
        if (getModeFromBeacon(beacon) < 0) return 0;
        return beacon.getItemDamage() % 2 + 1;
    }

    public boolean hasDirectedMobClonerInfiniteUpgrade() {
        return getModeFromBeacon(getControllerSlot()) == 3 && hasSecondaryModeBeacon();
    }

    public boolean hasSecondaryModeBeacon() {
        return getModeBeaconTier() == 2;
    }

    public long applyStructureFluidDiscount(long fluidAmount) {
        return isTierTwo() ? Math.max(1, fluidAmount / 10) : fluidAmount;
    }

    public long getAvailableInputPower() {
        return availableInputPower;
    }

    public int beginDirectedMobClonerDebugRun() {
        if (!directedMobClonerDebugActive) {
            directedMobClonerDebugActive = true;
            directedMobClonerDebugRecipeId = 1;
            directedMobClonerDebugStopPending = false;
            markDirty();
        }
        return directedMobClonerDebugRecipeId;
    }

    public void advanceDirectedMobClonerDebugRun(boolean lastRecipe) {
        if (lastRecipe) {
            directedMobClonerDebugStopPending = true;
        } else {
            directedMobClonerDebugRecipeId++;
        }
        markDirty();
    }

    public void resetDirectedMobClonerDebugRun() {
        if (!directedMobClonerDebugActive && directedMobClonerDebugRecipeId == 0 && !directedMobClonerDebugStopPending)
            return;
        directedMobClonerDebugActive = false;
        directedMobClonerDebugRecipeId = 0;
        directedMobClonerDebugStopPending = false;
        markDirty();
    }

    public boolean isOffspring(ItemStack stack) {
        return stack != null && Offspring != null && stack.isItemEqual(Offspring);
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // #tr EcoSphereSimulator.modeMsg.0
        // # Arboreal Genesis
        // #zh_CN 原木拟生

        // #tr EcoSphereSimulator.modeMsg.1
        // # Aquatic Simulation
        // #zh_CN 水域模拟

        // #tr EcoSphereSimulator.modeMsg.2
        // # Artificial Greenhouse
        // #zh_CN 人工温室

        // #tr EcoSphereSimulator.modeMsg.3
        // # Directed Mob Cloning
        // #zh_CN 定向克隆
        if (cleaningRequested || cleaningRunActive) return tr("EcoSphereSimulator.mode.cleaning");
        return modeBeaconPresent && boundMode >= 0 && boundMode < MACHINE_MODES.length
            ? MACHINE_MODES[boundMode].getDisplayName()
            : tr("EcoSphereSimulator.mode.waiting");
    }

    @Override
    public void setMachineMode(int index) {
        if (boundMode < 0) return;
        machineMode = boundMode;
        clearFluidAreaForMode();
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return false;
    }

    // @Override
    // public boolean canButtonSwitchMode() {
    // return checkStructure(true, getBaseMetaTileEntity());
    // }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // You're right, but there will be water leakage
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (FountOfEcology == null) FountOfEcology = GTCMItemList.FountOfEcology.get(1);
        if (Offspring == null) Offspring = GTCMItemList.OffSpring.get(1);
        // Sync the installed beacon when the machine loads.
        if (aBaseMetaTileEntity.isServerSide()) updateModeBeaconBinding();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        boolean serverSide = aBaseMetaTileEntity.isServerSide();
        // Check once per second while a recipe is active so beacon changes can stop it early.
        if (serverSide && mMaxProgresstime > 0 && aTick % MODE_BEACON_CHECK_INTERVAL == 0) {
            updateModeBeaconBinding();
        }
        // Run the normal machine tick after a possible mode change has stopped the old recipe.
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!serverSide) return;

        if (directedMobClonerDebugStopPending && mMaxProgresstime <= 0) {
            resetDirectedMobClonerDebugRun();
            aBaseMetaTileEntity.disableWorking();
        }
        if (aTick % 20 == 0 && controllerTier == 0) {
            ItemStack ControllerSlot = this.getControllerSlot();
            if (GTUtility.areStacksEqual(FountOfEcology, ControllerSlot)) {
                controllerTier = 1;
                mInventory[1] = ItemUtils.depleteStack(ControllerSlot, ControllerSlot.stackSize);
                markDirty();
                // schedule a structure check
                mUpdated = true;
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (!aPlayer.isSneaking()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (controllerTier == 0 && GTUtility.areStacksEqual(FountOfEcology, heldItem)) {
                controllerTier = 1;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, heldItem.stackSize));
                IGregTechTileEntity base = getBaseMetaTileEntity();
                if (base != null && base.isServerSide()) {
                    markDirty();
                    aPlayer.inventory.markDirty();
                    mUpdated = true;
                }
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    // Detect beacon changes and route every mode switch through the cleaning sequence.
    private void updateModeBeaconBinding() {
        int requestedMode = getModeFromBeacon(getControllerSlot());
        boolean wasPresent = modeBeaconPresent;
        modeBeaconPresent = requestedMode >= 0;
        if (wasPresent != modeBeaconPresent) markDirty();
        if (!modeBeaconPresent) return;
        if (cleaningRequested || cleaningRunActive) {
            if (requestedMode != pendingMode) {
                pendingMode = requestedMode;
                markDirty();
            }
            return;
        }
        if (requestedMode == boundMode) return;
        requestCleaning(requestedMode);
    }

    // Stop the current recipe and queue the requested mode for cleaning.
    private void requestCleaning(int requestedMode) {
        pendingMode = requestedMode;
        cleaningRequested = true;
        cleaningRunActive = false;
        missingFluidAreaInput = null;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        mOutputItems = null;
        mOutputFluids = null;
        markDirty();
    }

    // Map the eight beacon items to the four machine modes.
    private static int getModeFromBeacon(ItemStack stack) {
        if (stack == null || stack.stackSize <= 0 || stack.getItem() != TstItems.EcoSphereModeBeacon) return -1;
        int meta = stack.getItemDamage();
        return meta >= 0 && meta <= 7 ? meta / 2 : -1;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        controllerTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) controllerTier;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mTier", (byte) controllerTier);
        aNBT.setByte("mMode", (byte) machineMode);
        aNBT.setInteger("boundMode", boundMode);
        aNBT.setInteger("pendingMode", pendingMode);
        aNBT.setBoolean("cleaningRequested", cleaningRequested);
        aNBT.setBoolean("cleaningRunActive", cleaningRunActive);
        aNBT.setInteger("directedMobClonerDebugRecipeId", directedMobClonerDebugRecipeId);
        aNBT.setBoolean("directedMobClonerDebugActive", directedMobClonerDebugActive);
        aNBT.setBoolean("directedMobClonerDebugStopPending", directedMobClonerDebugStopPending);
        aNBT.setString("fluidAreaFluidName", fluidAreaFluidName);
        aNBT.setBoolean("fluidAreaInitialized", fluidAreaInitialized);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        controllerTier = aNBT.getByte("mTier");
        machineMode = aNBT.getByte("mMode");
        boundMode = aNBT.hasKey("boundMode") ? aNBT.getInteger("boundMode") : -1;
        pendingMode = aNBT.hasKey("pendingMode") ? aNBT.getInteger("pendingMode") : -1;
        cleaningRequested = aNBT.getBoolean("cleaningRequested");
        cleaningRunActive = aNBT.getBoolean("cleaningRunActive");
        directedMobClonerDebugRecipeId = aNBT.getInteger("directedMobClonerDebugRecipeId");
        directedMobClonerDebugActive = aNBT.getBoolean("directedMobClonerDebugActive");
        directedMobClonerDebugStopPending = aNBT.getBoolean("directedMobClonerDebugStopPending");
        fluidAreaFluidName = aNBT.getString("fluidAreaFluidName");
        fluidAreaInitialized = aNBT.hasKey("fluidAreaInitialized") && aNBT.getBoolean("fluidAreaInitialized");
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        aNBT.setByte("mTier", (byte) controllerTier);
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        super.initDefaultModes(aNBT);
        if (aNBT == null || !aNBT.hasKey("mTier")) {
            controllerTier = 0;
        } else {
            controllerTier = aNBT.getByte("mTier");
        }
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);
        NBTTagCompound aNBT = stack.getTagCompound();
        int tier;
        if (aNBT == null) {
            tier = 1;
        } else {
            tier = aNBT.getInteger("mTier") + 1;
        }
        tooltip.add(StatCollector.translateToLocalFormatted("tooltip.large_macerator.tier", tier));
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainEcoSphereSimulator0";
    private static final String STRUCTURE_PIECE_MAIN1 = "mainEcoSphereSimulator1";
    private static final String STRUCTURE_PIECE_FLUID_PREVIEW = "ecoSphereSimulatorFluidPreview";
    private static IStructureDefinition<TST_EcoSphereSimulator> STRUCTURE_DEFINITION = null;

    private static final int STRUCTURE_OFFSET_X = 16;
    private static final int STRUCTURE_OFFSET_Y = 38;
    private static final int STRUCTURE_OFFSET_Z = 7;

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        int structureTier = Math.min(stackSize.stackSize + controllerTier - 1, 1);
        this.buildPiece(
            "mainEcoSphereSimulator" + structureTier,
            stackSize,
            hintsOnly,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z);
        if (hintsOnly) {
            int[] fluidAreaOffset = EcoSphereFluidAreaHandler.STRUCTURE_FLUID_AREA_WITH_MAIN_OFFSET;
            buildPiece(
                STRUCTURE_PIECE_FLUID_PREVIEW,
                stackSize,
                true,
                fluidAreaOffset[0],
                fluidAreaOffset[1],
                fluidAreaOffset[2]);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built;
        int structureTier = Math.min(stackSize.stackSize + controllerTier - 1, 1);
        built = survivalBuildPiece(
            "mainEcoSphereSimulator" + structureTier,
            stackSize,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
        return built;

    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        // setDebugEnabled(true);
        checkPiece(
            "mainEcoSphereSimulator" + controllerTier,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z,
            errors);
    }

    @Override
    public IStructureDefinition<TST_EcoSphereSimulator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_EcoSphereSimulator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addShape(STRUCTURE_PIECE_MAIN1, transpose(shape2))
                .addShape(
                    STRUCTURE_PIECE_FLUID_PREVIEW,
                    transpose(EcoSphereFluidAreaHandler.StructureFluidAreaWithMain))
                .addElement('w', ofBlockHint(Blocks.air, 0, StructureLibAPI.getBlockHint(), 3))
                .addElement('W', ofBlockHint(Blocks.air, 0, StructureLibAPI.getBlockHint(), 3))
                .addElement('A', ofBlock(bw_realglas, 0))
                .addElement('a', ofBlock(bw_realglas, 15))
                .addElement('B', ofBlock(MetaBlockCasing01, 9))
                .addElement('C', ofBlock(MetaBlockCasing01, 10))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings1, 10))
                .addElement('d', ofBlock(MetaBlockCasing02, 6))
                .addElement('E', ofBlock(MetaBlockCasing02, 2))
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 10))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings9, 1))
                .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 15))
                .addElement('h', ofBlock(MetaBlockCasing01, 13))
                .addElement('I', ofBlock(ModBlocks.blockCasingsTieredGTPP, 8))
                .addElement(
                    'J',
                    ofBlock(ModBlocksHandler.BlockTranslucent.getLeft(), ModBlocksHandler.BlockTranslucent.getRight()))
                .addElement('j', ofBlock(seaLamp, 0))
                .addElement(
                    'K',
                    ofBlock(ModBlocksHandler.AirCrystalBlock.getLeft(), ModBlocksHandler.AirCrystalBlock.getRight()))
                .addElement(
                    'L',
                    ofBlock(
                        ModBlocksHandler.WaterCrystalBlock.getLeft(),
                        ModBlocksHandler.WaterCrystalBlock.getRight()))
                .addElement(
                    'M',
                    ofBlock(
                        ModBlocksHandler.EarthCrystalBlock.getLeft(),
                        ModBlocksHandler.EarthCrystalBlock.getRight()))
                .addElement('N', ofBlock(ModBlocksHandler.soil.getLeft(), ModBlocksHandler.soil.getRight()))
                .addElement(
                    'O',
                    withChannel(
                        "LightScreen",
                        ofBlocksTiered(
                            (block, meta) -> block == ModBlocksHandler.GreenScreen ? 0 : null,
                            IntStream.range(0, 16)
                                .mapToObj(meta -> Pair.of(ModBlocksHandler.GreenScreen, meta))
                                .collect(toList()),
                            -1,
                            (machine, tier) -> {},
                            machine -> 0)))
                .addElement(
                    'Q',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        HatchElementBuilder.<TST_EcoSphereSimulator>builder()
                            .anyOf(InputBus, OutputBus, InputHatch, OutputHatch)
                            .hint(1)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement(
                    'q',
                    ofChain(
                        ofBlock(MetaBlockCasing01, 13),
                        HatchElementBuilder.<TST_EcoSphereSimulator>builder()
                            .anyOf(InputBus, OutputBus, InputHatch, OutputHatch)
                            .hint(1)
                            .casingIndex(MetaBlockCasing01.getTextureIndex(13))
                            .build()))
                .addElement(
                    'R',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        HatchElementBuilder.<TST_EcoSphereSimulator>builder()
                            .atLeast(Energy.or(ExoticEnergy))
                            .adder(TST_EcoSphereSimulator::addToMachineList)
                            .hint(2)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement(
                    'r',
                    ofChain(
                        ofBlock(MetaBlockCasing01, 13),
                        HatchElementBuilder.<TST_EcoSphereSimulator>builder()
                            .atLeast(Energy.or(ExoticEnergy))
                            .adder(TST_EcoSphereSimulator::addToMachineList)
                            .hint(2)
                            .casingIndex(MetaBlockCasing01.getTextureIndex(13))
                            .build()))
                .addElement(
                    'T',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        HatchElementBuilder.<TST_EcoSphereSimulator>builder()
                            .anyOf(InputBus, OutputBus, InputHatch, OutputHatch)
                            .hint(3)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement(
                    't',
                    ofChain(
                        ofBlock(MetaBlockCasing01, 13),
                        HatchElementBuilder.<TST_EcoSphereSimulator>builder()
                            .anyOf(InputBus, OutputBus, InputHatch, OutputHatch)
                            .hint(3)
                            .casingIndex(MetaBlockCasing01.getTextureIndex(13))
                            .build()))
                .addElement('S', ofFrame(Materials.Mytryl))
                .addElement('s', ofFrame(Materials.AstralSilver))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) {
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(
                        controllerTier == 0 ? TAE.getIndexFromPage(1, 15) : MetaBlockCasing01.getTextureIndex(13)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(
                    controllerTier == 0 ? TAE.getIndexFromPage(1, 15) : MetaBlockCasing01.getTextureIndex(13)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build() };
        }

        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(
            controllerTier == 0 ? TAE.getIndexFromPage(1, 15) : MetaBlockCasing01.getTextureIndex(13)) };
    }

    // spotless:off

    /*
    A -> ofBlock...(BW_GlasBlocks, 0, ...); // T1 borosilicate glass
    a -> ofBlock...(BW_GlasBlocks, 15, ...); // T2 borosilicate glass
    B -> ofBlock...(MetaBlockCasing01, 9, ...);
    C -> ofBlock...(MetaBlockCasing01, 10, ...);
    D -> ofBlock...(gt.blockcasings, 10, ...);
    E -> ofBlock...(gt.blockcasings8, 5, ...);
    F -> ofBlock...(gt.blockcasings8, 10, ...);
    G -> ofBlock...(gt.blockcasings9, 1, ...);
    H -> ofBlock...(gtplusplus.blockcasings.2, 15, ...);
    I -> ofBlock...(gtplusplus.blocktieredcasings.1, 8, ...);
    J -> ofBlock...(tile.blockTranslucent, 0, ...); // T1 structure
    j -> ofBlock...(Botania:seaLamp, 0, ...); // T2 structure
    K -> ofBlock...(tile.crystalBlock, 0, ...);
    L -> ofBlock...(tile.crystalBlock, 2, ...);
    M -> ofBlock...(tile.crystalBlock, 3, ...);
    N -> ofBlock...(tile.for.soil, 0, ...);
    O -> withChannel...(ExtraUtilities:greenscreen, ...); // channel selects hint meta 0-15
    Q/q -> oak plank positions: item and fluid input/output hatches
    R/r -> birch plank positions: energy and exotic energy hatches
    T/t -> spruce plank positions: reserved special hatches, currently the same as Q/q
    The cleaning extension is removed only by the cleaning animation.
    Additional animated fluid positions are defined separately from the original main fluid area.
    The independent position is filled for every machine mode.
    All animated positions are treated as air by the structure check.
    ~ -> controller
    */

    private final String[][] shape = new String[][]{
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","             GGD DGG             ","            GGGD DGGG            ","           GGGHD DHGGG           ","           GGHSHHHSHGG           ","          DDDDHHHHHDDDD          ","              HHHHH              ","          DDDDHHHHHDDDD          ","           GGHSHHHSHGG           ","           GGGHD DHGGG           ","            GGGD DGGG            ","             GGD DGG             ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             GGD DGG             ","           DGGGSDSGGGD           ","          DGG  SDS  GGD          ","          GG  HHDHH  GG          ","         GG  HIIDIIH  GG         ","         GG HIIIIIIIH GG         ","         DSSHIIIIIIIHSSD         ","          DDDDIIIIIDDDD          ","         DSSHIIIIIIIHSSD         ","         GG HIIIIIIIH GG         ","         GG  HIIDIIH  GG         ","          GG  HHDHH  GG          ","          DGG  SDS  GGD          ","           DGGGSDSGGGD           ","             GGD DGG             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","            DGGD DGGD            ","           AD  SDS  DA           ","          A     S     A          ","         A      S      A         ","        DD    DDDDD    DD        ","        G    DOOOOOD    G        ","        G   DOOOOOOOD   G        ","       DDS  DOOOOOOOD  SDD       ","         DSSDOOOOOOODSSD         ","       DDS  DOOOOOOOD  SDD       ","        G   DOOOOOOOD   G        ","        G    DOOOOOD    G        ","        DD    DDDDD    DD        ","         A      S      A         ","          A     S     A          ","           AD  SDS  DA           ","            DGGD DGGD            ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","            ADDSDSDDA            ","          AA   SDS   AA          ","         AA           AA         ","        AA             AA        ","        A               A        ","       A                 A       ","       D                 D       ","       D                 D       ","      DSS               SSD      ","       DD               DD       ","      DSS               SSD      ","       D                 D       ","       D                 D       ","       A                 A       ","        A               A        ","        AA             AA        ","         AA           AA         ","          AA   SDS   AA          ","            ADDSDSDDA            ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","            AAAD DAAA            ","          AA   SDS   AA          ","         A             A         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","      A                   A      ","      DS                 SD      ","       D                 D       ","      DS                 SD      ","      A                   A      ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         A             A         ","          AA   SDS   AA          ","            AAAD DAAA            ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","             AAD DAA             ","           AA  SDS  AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     DS                   SD     ","      D                   D      ","     DS                   SD     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA  SDS  AA           ","             AAD DAA             ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               D D               ","           AAAASDSAAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    DS                     SD    ","     D                     D     ","    DS                     SD    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAASDSAAAA           ","               D D               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            DDDD DDDD            ","          DD   S S   DD          ","        DD             DD        ","       D                 D       ","      D                   D      ","      D                   D      ","     D                     D     ","     D                     D     ","    D                       D    ","    D                       D    ","    D                       D    ","    DS                     SD    ","                                 ","    DS                     SD    ","    D                       D    ","    D                       D    ","    D                       D    ","     D                     D     ","     D                     D     ","      D                   D      ","      D                   D      ","       D                 D       ","        DD             DD        ","          DD   S S   DD          ","            DDDD DDDD            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAADDDAAA            ","          AA   S S   AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    DS                     SD    ","    D                       D    ","    DS                     SD    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA   S S   AA          ","            AAADDDAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AA AA              ","           AAA SDS AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   AS                       SA   ","    D                       D    ","   AS                       SA   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA SDS AAA           ","              AA AA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAADAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAADAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AADAA              ","           AAA     AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   D                         D   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA     AAA           ","              AADAA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAAADAAAA            ","          AA         AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","    D                       D    ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA         AA          ","            AAAADAAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             AAADAAA             ","          AAA       AAA          ","         A             A         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    D                       D    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         A             A         ","          AAA       AAA          ","             AAADAAA             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               ADA               ","           AAAA   AAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    D                       D    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAA   AAAA           ","               ADA               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                D                ","             AAAHAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    DH                     HD    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAHAAA             ","                D                ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","            DDDDDDDDD            ","          DDHHHHHHHHHDD          ","         DHHNNNN NNNNHHD         ","        DHNNNNNNNNNNNNNHD        ","       DHNNNNNNNNNNNNNNNHD       ","      DHNNNNNNNNNNNNNNNNNHD      ","      DHNNNNNNNNNNNNNNNNNHD      ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DH NNNNNNNNNNNNNNNNN HD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","     DHNNNNNNNNNNNNNNNNNNNHD     ","      DHNNNNNNNNNNNNNNNNNHD      ","      DHNNNNNNNNNNNNNNNNNHD      ","       DHNNNNNNNNNNNNNNNHD       ","        DHNNNNNNNNNNNNNHD        ","         DHHNNNN NNNNHHD         ","          DDHHHHHHHHHDD          ","            DDDDDDDDD            ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","            SSSDHDSSS            ","          SSAAANNNAAASS          ","         SAANNNNNNNNNAAS         ","        SAANNNNNNNNNNNAAS        ","       SAANNNNNNNNNNNNNAAS       ","       SANNNNNNNNNNNNNNNAS       ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      DNNNNNNNNNNNNNNNNNNND      ","     DHNNNNNNNNNNNNNNNNNNNHD     ","      DNNNNNNNNNNNNNNNNNNND      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","       SANNNNNNNNNNNNNNNAS       ","       SAANNNNNNNNNNNNNAAS       ","        SAANNNNNNNNNNNAAS        ","         SAANNNNNNNNNAAS         ","          SSAAANNNAAASS          ","            SSSDHDSSS            ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","               AHA               ","            AAAAHAAAA            ","           AANNNNNNNAA           ","          ANNNNNNNNNNNA          ","         ANNNNNNNNNNNNNA         ","        AANNNNNNNNNNNNNAA        ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","       AANNNNNNNNNNNNNNNAA       ","      DHHNNNNNNNNNNNNNNNHHD      ","       AANNNNNNNNNNNNNNNAA       ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","        AANNNNNNNNNNNNNAA        ","         ANNNNNNNNNNNNNA         ","          ANNNNNNNNNNNA          ","           AANNNNNNNAA           ","            AAAAHAAAA            ","               AHA               ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","                D                ","             AAAHAAA             ","           AAAANNNAAAA           ","          AAANNNNNNNAAA          ","          AANNNNNNNNNAA          ","         AANNNNNNNNNNNAA         ","         AANNNNNNNNNNNAA         ","         ANNNNNNNNNNNNNA         ","       DDHNNNNNNNNNNNNNHDD       ","         ANNNNNNNNNNNNNA         ","         AANNNNNNNNNNNAA         ","         AANNNNNNNNNNNAA         ","          AANNNNNNNNNAA          ","          AAANNNNNNNAAA          ","           AAAANNNAAAA           ","             AAAHAAA             ","                D                ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","               AHA               ","             AAAHAAA             ","            AAAAHAAAA            ","           AAANNNNNAAA           ","           AANNNNNNNAA           ","          AAANNNNNNNAAA          ","         DHHHNNN NNNHHHD         ","          AAANNNNNNNAAA          ","           AANNNNNNNAA           ","           AAANNNNNAAA           ","            AAAAHAAAA            ","             AAAHAAA             ","               AHA               ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","                D                ","              DDDDD              ","             DHHHHHD             ","            DHHHHHHHD            ","            DHHHHHHHD            ","          DDDHHH HHHDDD          ","            DHHHHHHHD            ","            DHHHHHHHD            ","             DHHHHHD             ","              DDDDD              ","                D                ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             DDJDJDD             ","             DJJDJJD             ","             DDD DDD             ","             DJJDJJD             ","             DDJDJDD             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              EEEEE              ","             E     E             ","            E       E            ","            E  LLL  E            ","            E  LLL  E            ","            E  LLL  E            ","            E       E            ","             E     E             ","              EEEEE              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             DDDDDDD             ","            DDSSSSSDD            ","          DDSSGGGGGSSDD          ","          DSGGGGGGGGGSD          ","         DSGGGEEEEEGGGSD         ","        DDSGGFBBBBBFGGSDD        ","        DSGGEBB   BBEGGSD        ","        DSGGEB LLL BEGGSD        ","        DSGGEB LLL BEGGSD        ","        DSGGEB LLL BEGGSD        ","        DSGGEBB   BBEGGSD        ","        DDSGGFBBBBBFGGSDD        ","         DSGGGEEEEEGGGSD         ","          DSGGGGGGGGGSD          ","          DDSSGGGGGSSDD          ","            DDSSSSSDD            ","             DDDDDDD             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             D     D             ","             DST~TSD             ","            SDIIIIIDS            ","          SSIIIIIIIIISS          ","         SIIIIIIIIIIIIIS         ","         SIIIIIIIIIIIIIS         ","        SIIIIIEEEEEIIIIIS        ","      DDDIIIIFMMMMMFIIIIDDD      ","       SIIIIEMMBBBMMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMMBBBMMEIIIIS       ","      DDDIIIIFMMMMMFIIIIDDD      ","        SIIIIIEEEEEIIIIIS        ","         SIIIIIIIIIIIIIS         ","         SIIIIIIIIIIIIIS         ","          SSIIIIIIIIISS          ","            SDIIIIIDS            ","             DSSSSSD             ","             D     D             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             DDDDDDD             ","           DDDQQQQQDDD           ","         DDHHDQQQQQDHHDD         ","        DHHHHDGGGGGDHHHHD        ","       DHHHHGDIIIIIDGHHHHD       ","      DHHHGGIIKKKKKIIGGHHHD      ","      DHHGIIKKKKKKKKKIIGHHD      ","     DHHHGIKKKKKKKKKKKIGHHHD     ","     DHHGIKKKKEEEEEKKKKIGHHD     ","    DDDDDIKKKFMMMMMFKKKIDDDDD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DDDDDIKKKFMMMMMFKKKIDDDDD    ","     DHHGIKKKKEEEEEKKKKIGHHD     ","     DHHHGIKKKKKKKKKKKIGHHHD     ","      DHHGIIKKKKKKKKKIIGHHD      ","      DHHHGGIIKKKKKIIGGHHHD      ","       DHHHHGDIIIIIDGHHHHD       ","        DHHHHDGGGGGDHHHHD        ","         DDHHDQQQQQDHHDD         ","           DDDQQQQQDDD           ","             DDDDDDD             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","            DHHHHHHHD            ","           HDDDDDDDDDH           ","         HHHHDDDDDDDHHHH         ","        HHHHCCCCCCCCCHHHH        ","       HHHCCCCCCCCCCCCCHHH       ","      HHHCCCCCDDDDDCCCCCHHH      ","     HHHCCCCHHCCCCCHHCCCCHHH     ","     HHCCCHHCCCCCCCCCHHCCCHH     ","    HHHCCCHCCCCCCCCCCCHCCCHHH    ","   DDHCCCHCCCCEEEEECCCCHCCCHDD   ","   HDDCCCHCCCFMMMMMFCCCHCCCDDH   ","   HDDCCDCCCEMMMMMMMECCCDCCDDH   ","   HDDCCDCCCEMMMMMMMECCCDCCDDH   ","   HDDCCDCCCEMMMMMMMECCCDCCDDH   ","   HDDCCDCCCEMMMMMMMECCCDCCDDH   ","   HDDCCDCCCEMMMMMMMECCCDCCDDH   ","   HDDCCCHCCCFMMMMMFCCCHCCCDDH   ","   DDHCCCHCCCCEEEEECCCCHCCCHDD   ","    HHHCCCHCCCCCCCCCCCHCCCHHH    ","     HHCCCHHCCCCCCCCCHHCCCHH     ","     HHHCCCCHHCCCCCHHCCCCHHH     ","      HHHCCCCCDDDDDCCCCCHHH      ","       HHHCCCCCCCCCCCCCHHH       ","        HHHHCCCCCCCCCHHHH        ","         HHHHDDDDDDDHHHH         ","           HDDDDDDDDDH           ","            DHHHHHHHD            ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","            D       D            ","            SHHJJJHHS            ","           HHHHHHDDHHH           ","         JJHHCCCCCCCHHJJ         ","        HHHHCCCCCCCCCHHHH        ","       JHHCCCCCCCCCCCCCHHJ       ","      HHHCCCCCHHHHHCCCCCHHH      ","     JHHCCCCHHGGGGGHHCCCCHHJ     ","     JHCCCHHGGGGGGGGGHHCCCHJ     ","    HHHCCCHGGGGGGGGGGGHCCCHHH    ","  DSHHCCCHGGGGEEEEEGGGGHCCCHHSD  ","   HHCCCCHGGGFMMMMMFGGGHCCCCHH   ","   HDCCCHGGGEMMMMMMMEGGGHCCCHH   ","   JDCCCHGGGEMMMMMMMEGGGHCCCHJ   ","   JDCCCHGGGEMMMMMMMEGGGHCCCDJ   ","   JHCCCHGGGEMMMMMMMEGGGHCCCDJ   ","   HHCCCHGGGEMMMMMMMEGGGHCCCDH   ","   HHCCCCHGGGFMMMMMFGGGHCCCCHH   ","  DSHHCCCHGGGGEEEEEGGGGHCCCHHSD  ","    HHHCCCHGGGGGGGGGGGHCCCHHH    ","     JHCCCHHGGGGGGGGGHHCCCHJ     ","     JHHCCCCHHGGGGGHHCCCCHHJ     ","      HHHCCCCCHHHHHCCCCCHHH      ","       JHHCCCCCCCCCCCCCHHJ       ","        HHHHCCCCCCCCCHHHH        ","         JJHHCCCCCCCHHJJ         ","           HHHDDDHHHHH           ","            SHHJJJHHS            ","            D       D            ","                                 ","                                 "},
        {"                                 ","            D       D            ","            SS     SS            ","           DSHHHHHHHS            ","           HHHDDDDDHHH           ","         HHHHHHHHHHHHHHH         ","        HHHRRHHHHHHHRRHHH        ","       HHRRRJRHHHHHRJRRRHH       ","      HHRRJRRRHHHHHRRRJRRHH      ","     HHRRRRRHH     HHRRRRRHH     ","     HHRJRHH         HHRJRHH     ","    HHRRRRH           HRRRRHHD   "," DSSHHRJRH    EEEEE    HRJRHHSSD ","  SHHHHRRH   FMMMMMF   HRRHHHHS  ","   HDHHHH   EMMMMMMME   HHHHDH   ","   HDHHHH   EMMMMMMME   HHHHDH   ","   HDHHHH   EMMMMMMME   HHHHDH   ","   HDHHHH   EMMMMMMME   HHHHDH   ","   HDHHHH   EMMMMMMME   HHHHDH   ","  SHHHHRRH   FMMMMMF   HRRHHHHS  "," DSSHHRJRH    EEEEE    HRJRHHSSD ","   DHHRRRRH           HRRRRHH    ","     HHRJRHH         HHRJRHH     ","     HHRRRRRHH     HHRRRRRHH     ","      HHRRJRRRHHHHHRRRJRRHH      ","       HHRRRJRHHHHHRJRRRHH       ","        HHHRRHHHHHHHRRHHH        ","         HHHHHHHHHHHHHHH         ","           HHHDDDDDHHH           ","            SHHHHHHHSD           ","            SS     SS            ","            D       D            ","                                 "},
        {"            DDDDDDDDD            ","            DSSSSSSSD            ","           DDD     DDD           ","           DDDDDDDDDDD           ","           DDDDDDDDDDD           ","         DDDDDDDDDDDDDDD         ","        DDD  DDDDDDD  DDD        ","       DD     DDDDD     DD       ","      DD      DDDDD      DD      ","     DD     DD     DD     DD     ","     DD   DD         DD   DD     ","  DDDD    D           D    DDDD  ","DDDDDD   D    EJJJE    D   DDDDDD","DSDDDDD  D   FMMMMMF   D  DDDDDSD","DS DDDDDD   EMMMMMMME   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   EMMMMMMME   DDDDDD SD","DSDDDDD  D   FMMMMMF   D  DDDDDSD","DDDDDD   D    EJJJE    D   DDDDDD","  DDDD    D           D    DDDD  ","     DD   DD         DD   DD     ","     DD     DD     DD     DD     ","      DD      DDDDD      DD      ","       DD     DDDDD     DD       ","        DDD  DDDDDDD  DDD        ","         DDDDDDDDDDDDDDD         ","           DDDDDDDDDDD           ","           DDDDDDDDDDD           ","           DDD     DDD           ","            DSSSSSSSD            ","            DDDDDDDDD            "},
        {"            BBBBBBBBB            ","          BBB       BBB          ","        BBBBBB     BBBBBB        ","      BB  BBBBBBBBBBBBB  BB      ","     B   BBBBBBBBBBBBBBB   B     ","    B   BBBBBBBBBBBBBBBBB   B    ","   B   BBBB  BBBBBBB  BBBB   B   ","   B  BBB     BBBBB     BBB  B   ","  B  BBB      BBBBB      BBB  B  ","  B BBB     BB     BB     BBB B  "," BBBBBB   BB         BB   BBBBBB "," BBBBB    B           B    BBBBB ","BBBBBB   B    EEEEE    B   BBBBBB","B BBBBB  B   EFFFFFE   B  BBBBB B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B BBBBB  B   EFFFFFE   B  BBBBB B","BBBBBB   B    EEEEE    B   BBBBBB"," BBBBB    B           B    BBBBB "," BBBBBB   BB         BB   BBBBBB ","  B BBB     BB     BB     BBB B  ","  B  BBB      BBBBB      BBB  B  ","   B  BBB     BBBBB     BBB  B   ","   B   BBBB  BBBBBBB  BBBB   B   ","    B   BBBBBBBBBBBBBBBBB   B    ","     B   BBBBBBBBBBBBBBB   B     ","      BB  BBBBBBBBBBBBB  BB      ","        BBBBBB     BBBBBB        ","          BBB       BBB          ","            BBBBBBBBB            "}
    };

    private final String[][] shape2 = new String[][]{
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              ddddd              ","             ddddddd             ","             ddddddd             ","             ddddddd             ","             ddddddd             ","             ddddddd             ","              ddddd              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","             GGd dGG             ","            GGGd dGGG            ","           GGGhd dhGGG           ","           GGhshhhshGG           ","          ddddhhhhhdddd          ","              hhhhh              ","          ddddhhhhhdddd          ","           GGhshhhshGG           ","           GGGhd dhGGG           ","            GGGd dGGG            ","             GGd dGG             ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             GGd dGG             ","           dGGGsdsGGGd           ","          dGG  sds  GGd          ","          GG  hhdhh  GG          ","         GG  hIIdIIh  GG         ","         GG hIIIIIIIh GG         ","         dsshIIIIIIIhssd         ","          ddddIIIIIdddd          ","         dsshIIIIIIIhssd         ","         GG hIIIIIIIh GG         ","         GG  hIIdIIh  GG         ","          GG  hhdhh  GG          ","          dGG  sds  GGd          ","           dGGGsdsGGGd           ","             GGd dGG             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","            dGGd dGGd            ","           ad  sds  da           ","          a     s     a          ","         a      s      a         ","        dd    ddddd    dd        ","        G    dOOOOOd    G        ","        G   dOOOOOOOd   G        ","       dds  dOOOOOOOd  sdd       ","         dssdOOOOOOOdssd         ","       dds  dOOOOOOOd  sdd       ","        G   dOOOOOOOd   G        ","        G    dOOOOOd    G        ","        dd    ddddd    dd        ","         a      s      a         ","          a     s     a          ","           ad  sds  da           ","            dGGd dGGd            ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","            addsdsdda            ","          aa   sds   aa          ","         aa           aa         ","        aa             aa        ","        a               a        ","       a                 a       ","       d                 d       ","       d                 d       ","      dss               ssd      ","       dd               dd       ","      dss               ssd      ","       d                 d       ","       d                 d       ","       a                 a       ","        a               a        ","        aa             aa        ","         aa           aa         ","          aa   sds   aa          ","            addsdsdda            ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","            aaad daaa            ","          aa   sds   aa          ","         a             a         ","        a               a        ","       a                 a       ","       a                 a       ","      a                   a      ","      a                   a      ","      a                   a      ","      ds                 sd      ","       d                 d       ","      ds                 sd      ","      a                   a      ","      a                   a      ","      a                   a      ","       a                 a       ","       a                 a       ","        a               a        ","         a             a         ","          aa   sds   aa          ","            aaad daaa            ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","             aad daa             ","           aa  sds  aa           ","         aa           aa         ","        a               a        ","       a                 a       ","       a                 a       ","      a                   a      ","      a                   a      ","     a                     a     ","     a                     a     ","     ds                   sd     ","      d                   d      ","     ds                   sd     ","     a                     a     ","     a                     a     ","      a                   a      ","      a                   a      ","       a                 a       ","       a                 a       ","        a               a        ","         aa           aa         ","           aa  sds  aa           ","             aad daa             ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               d d               ","           aaaasdsaaaa           ","          a           a          ","        aa             aa        ","       a                 a       ","       a                 a       ","      a                   a      ","     a                     a     ","     a                     a     ","     a                     a     ","     a                     a     ","    ds                     sd    ","     d                     d     ","    ds                     sd    ","     a                     a     ","     a                     a     ","     a                     a     ","     a                     a     ","      a                   a      ","       a                 a       ","       a                 a       ","        aa             aa        ","          a           a          ","           aaaasdsaaaa           ","               d d               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            dddd dddd            ","          dd   s s   dd          ","        dd             dd        ","       d                 d       ","      d                   d      ","      d                   d      ","     d                     d     ","     d                     d     ","    d                       d    ","    d                       d    ","    d                       d    ","    ds                     sd    ","                                 ","    ds                     sd    ","    d                       d    ","    d                       d    ","    d                       d    ","     d                     d     ","     d                     d     ","      d                   d      ","      d                   d      ","       d                 d       ","        dd             dd        ","          dd   s s   dd          ","            dddd dddd            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            aaadddaaa            ","          aa   s s   aa          ","        aa             aa        ","       a                 a       ","      a                   a      ","      a                   a      ","     a                     a     ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","    ds                     sd    ","    d                       d    ","    ds                     sd    ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","     a                     a     ","      a                   a      ","      a                   a      ","       a                 a       ","        aa             aa        ","          aa   s s   aa          ","            aaadddaaa            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              aa aa              ","           aaa sds aaa           ","         aa           aa         ","        a               a        ","       a                 a       ","      a                   a      ","     a                     a     ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","   a                         a   ","   as                       sa   ","    d                       d    ","   as                       sa   ","   a                         a   ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","     a                     a     ","      a                   a      ","       a                 a       ","        a               a        ","         aa           aa         ","           aaa sds aaa           ","              aa aa              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             aaadaaa             ","           aa       aa           ","         aa           aa         ","        a               a        ","       a                 a       ","      a                   a      ","     a                     a     ","     a                     a     ","    a                       a    ","    a                       a    ","   a                         a   ","   a                         a   ","   a                         a   ","   d                         d   ","   a                         a   ","   a                         a   ","   a                         a   ","    a                       a    ","    a                       a    ","     a                     a     ","     a                     a     ","      a                   a      ","       a                 a       ","        a               a        ","         aa           aa         ","           aa       aa           ","             aaadaaa             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             aaadaaa             ","          aaa       aaa          ","         a             a         ","       aa               aa       ","      a                   a      ","      a                   a      ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","   a                         a   ","   a                         a   ","   a                         a   ","   d                         d   ","   a                         a   ","   a                         a   ","   a                         a   ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","      a                   a      ","      a                   a      ","       aa               aa       ","         a             a         ","          aaa       aaa          ","             aaadaaa             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             aaadaaa             ","          aaa       aaa          ","         a             a         ","       aa               aa       ","      a                   a      ","      a                   a      ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","   a                         a   ","   a                         a   ","   a                         a   ","   d                         d   ","   a                         a   ","   a                         a   ","   a                         a   ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","      a                   a      ","      a                   a      ","       aa               aa       ","         a             a         ","          aaa       aaa          ","             aaadaaa             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             aaadaaa             ","          aaa       aaa          ","         a             a         ","       aa               aa       ","      a                   a      ","      a                   a      ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","   a                         a   ","   a                         a   ","   a                         a   ","   d                         d   ","   a                         a   ","   a                         a   ","   a                         a   ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","      a                   a      ","      a                   a      ","       aa               aa       ","         a             a         ","          aaa       aaa          ","             aaadaaa             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             aaadaaa             ","           aa       aa           ","         aa           aa         ","        a               a        ","       a                 a       ","      a                   a      ","     a                     a     ","     a                     a     ","    a                       a    ","    a                       a    ","   a                         a   ","   a                         a   ","   a                         a   ","   d                         d   ","   a                         a   ","   a                         a   ","   a                         a   ","    a                       a    ","    a                       a    ","     a                     a     ","     a                     a     ","      a                   a      ","       a                 a       ","        a               a        ","         aa           aa         ","           aa       aa           ","             aaadaaa             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              aadaa              ","           aaa     aaa           ","         aa           aa         ","        a               a        ","       a                 a       ","      a                   a      ","     a                     a     ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","   a                         a   ","   a                         a   ","   d                         d   ","   a                         a   ","   a                         a   ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","     a                     a     ","      a                   a      ","       a                 a       ","        a               a        ","         aa           aa         ","           aaa     aaa           ","              aadaa              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            aaaadaaaa            ","          aa         aa          ","        aa             aa        ","       a                 a       ","      a                   a      ","      a                   a      ","     a                     a     ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","    a                       a    ","    d                       d    ","    a                       a    ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","     a                     a     ","      a                   a      ","      a                   a      ","       a                 a       ","        aa             aa        ","          aa         aa          ","            aaaadaaaa            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             aaadaaa             ","          aaa       aaa          ","         a             a         ","        a               a        ","       a                 a       ","      a                   a      ","     a                     a     ","     a                     a     ","     a                     a     ","    a                       a    ","    a                       a    ","    a                       a    ","    d                       d    ","    a                       a    ","    a                       a    ","    a                       a    ","     a                     a     ","     a                     a     ","     a                     a     ","      a                   a      ","       a                 a       ","        a               a        ","         a             a         ","          aaa       aaa          ","             aaadaaa             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               ada               ","           aaaa   aaaa           ","          a           a          ","        aa             aa        ","       a                 a       ","       a                 a       ","      a                   a      ","     a                     a     ","     a                     a     ","     a                     a     ","     a                     a     ","    a                       a    ","    d                       d    ","    a                       a    ","     a                     a     ","     a                     a     ","     a                     a     ","     a                     a     ","      a                   a      ","       a                 a       ","       a                 a       ","        aa             aa        ","          a           a          ","           aaaa   aaaa           ","               ada               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                d                ","             aaahaaa             ","           aa       aa           ","         aa           aa         ","        a               a        ","       a                 a       ","       a                 a       ","      a                   a      ","      a                   a      ","     a                     a     ","     a                     a     ","     a                     a     ","    dh                     hd    ","     a                     a     ","     a                     a     ","     a                     a     ","      a                   a      ","      a                   a      ","       a                 a       ","       a                 a       ","        a               a        ","         aa           aa         ","           aa       aa           ","             aaahaaa             ","                d                ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","            ddddddddd            ","          ddhhhhhhhhhdd          ","         dhhNNNN NNNNhhd         ","        dhNNNNNNNNNNNNNhd        ","       dhNNNNNNNNNNNNNNNhd       ","      dhNNNNNNNNNNNNNNNNNhd      ","      dhNNNNNNNNNNNNNNNNNhd      ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dh NNNNNNNNNNNNNNNNN hd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","      dhNNNNNNNNNNNNNNNNNhd      ","      dhNNNNNNNNNNNNNNNNNhd      ","       dhNNNNNNNNNNNNNNNhd       ","        dhNNNNNNNNNNNNNhd        ","         dhhNNNN NNNNhhd         ","          ddhhhhhhhhhdd          ","            ddddddddd            ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","            sssdhdsss            ","          ssaaaNNNaaass          ","         saaNNNNNNNNNaas         ","        saaNNNNNNNNNNNaas        ","       saaNNNNNNNNNNNNNaas       ","       saNNNNNNNNNNNNNNNas       ","      saNNNNNNNNNNNNNNNNNas      ","      saNNNNNNNNNNNNNNNNNas      ","      saNNNNNNNNNNNNNNNNNas      ","      dNNNNNNNNNNNNNNNNNNNd      ","     dhNNNNNNNNNNNNNNNNNNNhd     ","      dNNNNNNNNNNNNNNNNNNNd      ","      saNNNNNNNNNNNNNNNNNas      ","      saNNNNNNNNNNNNNNNNNas      ","      saNNNNNNNNNNNNNNNNNas      ","       saNNNNNNNNNNNNNNNas       ","       saaNNNNNNNNNNNNNaas       ","        saaNNNNNNNNNNNaas        ","         saaNNNNNNNNNaas         ","          ssaaaNNNaaass          ","            sssdhdsss            ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","               aha               ","            aaaahaaaa            ","           aaNNNNNNNaa           ","          aNNNNNNNNNNNa          ","         aNNNNNNNNNNNNNa         ","        aaNNNNNNNNNNNNNaa        ","        aNNNNNNNNNNNNNNNa        ","        aNNNNNNNNNNNNNNNa        ","       aaNNNNNNNNNNNNNNNaa       ","      dhhNNNNNNNNNNNNNNNhhd      ","       aaNNNNNNNNNNNNNNNaa       ","        aNNNNNNNNNNNNNNNa        ","        aNNNNNNNNNNNNNNNa        ","        aaNNNNNNNNNNNNNaa        ","         aNNNNNNNNNNNNNa         ","          aNNNNNNNNNNNa          ","           aaNNNNNNNaa           ","            aaaahaaaa            ","               aha               ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","                d                ","             aaahaaa             ","           aaaaNNNaaaa           ","          aaaNNNNNNNaaa          ","          aaNNNNNNNNNaa          ","         aaNNNNNNNNNNNaa         ","         aaNNNNNNNNNNNaa         ","         aNNNNNNNNNNNNNa         ","       ddhNNNNNNNNNNNNNhdd       ","         aNNNNNNNNNNNNNa         ","         aaNNNNNNNNNNNaa         ","         aaNNNNNNNNNNNaa         ","          aaNNNNNNNNNaa          ","          aaaNNNNNNNaaa          ","           aaaaNNNaaaa           ","             aaahaaa             ","                d                ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","               aha               ","             aaahaaa             ","            aaaahaaaa            ","           aaaNNNNNaaa           ","           aaNNNNNNNaa           ","          aaaNNNNNNNaaa          ","         dhhhNNN NNNhhhd         ","          aaaNNNNNNNaaa          ","           aaNNNNNNNaa           ","           aaaNNNNNaaa           ","            aaaahaaaa            ","             aaahaaa             ","               aha               ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","                d                ","              ddddd              ","             dhhhhhd             ","            dhhhhhhhd            ","            dhhhhhhhd            ","          dddhhh hhhddd          ","            dhhhhhhhd            ","            dhhhhhhhd            ","             dhhhhhd             ","              ddddd              ","                d                ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              ddddd              ","             ddjdjdd             ","             djjdjjd             ","             ddd ddd             ","             djjdjjd             ","             ddjdjdd             ","              ddddd              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                L                ","               LLL               ","                L                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              EEEEE              ","             E     E             ","            E       E            ","            E  LLL  E            ","            E  LLL  E            ","            E  LLL  E            ","            E       E            ","             E     E             ","              EEEEE              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             ddddddd             ","            ddsssssdd            ","          ddssGGGGGssdd          ","          dsGGGGGGGGGsd          ","         dsGGGEEEEEGGGsd         ","        ddsGGFBBBBBFGGsdd        ","        dsGGEBB   BBEGGsd        ","        dsGGEB LLL BEGGsd        ","        dsGGEB LLL BEGGsd        ","        dsGGEB LLL BEGGsd        ","        dsGGEBB   BBEGGsd        ","        ddsGGFBBBBBFGGsdd        ","         dsGGGEEEEEGGGsd         ","          dsGGGGGGGGGsd          ","          ddssGGGGGssdd          ","            ddsssssdd            ","             ddddddd             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             d     d             ","             dst~tsd             ","            sdIIIIIds            ","          ssIIIIIIIIIss          ","         sIIIIIIIIIIIIIs         ","         sIIIIIIIIIIIIIs         ","        sIIIIIEEEEEIIIIIs        ","      dddIIIIFMMMMMFIIIIddd      ","       sIIIIEMMBBBMMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMMBBBMMEIIIIs       ","      dddIIIIFMMMMMFIIIIddd      ","        sIIIIIEEEEEIIIIIs        ","         sIIIIIIIIIIIIIs         ","         sIIIIIIIIIIIIIs         ","          ssIIIIIIIIIss          ","            sdIIIIIds            ","             dsssssd             ","             d     d             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             ddddddd             ","           dddqqqqqddd           ","         ddhhdqqqqqdhhdd         ","        dhhhhdGGGGGdhhhhd        ","       dhhhhGdIIIIIdGhhhhd       ","      dhhhGGIIKKKKKIIGGhhhd      ","      dhhGIIKKKKKKKKKIIGhhd      ","     dhhhGIKKKKKKKKKKKIGhhhd     ","     dhhGIKKKKEEEEEKKKKIGhhd     ","    dddddIKKKFMMMMMFKKKIddddd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dddddIKKKFMMMMMFKKKIddddd    ","     dhhGIKKKKEEEEEKKKKIGhhd     ","     dhhhGIKKKKKKKKKKKIGhhhd     ","      dhhGIIKKKKKKKKKIIGhhd      ","      dhhhGGIIKKKKKIIGGhhhd      ","       dhhhhGdIIIIIdGhhhhd       ","        dhhhhdGGGGGdhhhhd        ","         ddhhdqqqqqdhhdd         ","           dddqqqqqddd           ","             ddddddd             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","            dhhhhhhhd            ","           hdddddddddh           ","         hhhhdddddddhhhh         ","        hhhhCCCCCCCCChhhh        ","       hhhCCCCCCCCCCCCChhh       ","      hhhCCCCCdddddCCCCChhh      ","     hhhCCCChhCCCCChhCCCChhh     ","     hhCCChhCCCCCCCCChhCCChh     ","    hhhCCChCCCCCCCCCCChCCChhh    ","   ddhCCChCCCCEEEEECCCChCCChdd   ","   hddCCChCCCFMMMMMFCCChCCCddh   ","   hddCCdCCCEMMMMMMMECCCdCCddh   ","   hddCCdCCCEMMMMMMMECCCdCCddh   ","   hddCCdCCCEMMMMMMMECCCdCCddh   ","   hddCCdCCCEMMMMMMMECCCdCCddh   ","   hddCCdCCCEMMMMMMMECCCdCCddh   ","   hddCCChCCCFMMMMMFCCChCCCddh   ","   ddhCCChCCCCEEEEECCCChCCChdd   ","    hhhCCChCCCCCCCCCCChCCChhh    ","     hhCCChhCCCCCCCCChhCCChh     ","     hhhCCCChhCCCCChhCCCChhh     ","      hhhCCCCCdddddCCCCChhh      ","       hhhCCCCCCCCCCCCChhh       ","        hhhhCCCCCCCCChhhh        ","         hhhhdddddddhhhh         ","           hdddddddddh           ","            dhhhhhhhd            ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","            d       d            ","            shhjjjhhs            ","           hhhhhhddhhh           ","         jjhhCCCCCCChhjj         ","        hhhhCCCCCCCCChhhh        ","       jhhCCCCCCCCCCCCChhj       ","      hhhCCCCChhhhhCCCCChhh      ","     jhhCCCChhGGGGGhhCCCChhj     ","     jhCCChhGGGGGGGGGhhCCChj     ","    hhhCCChGGGGGGGGGGGhCCChhh    ","  dshhCCChGGGGEEEEEGGGGhCCChhsd  ","   hhCCCChGGGFMMMMMFGGGhCCCChh   ","   hdCCChGGGEMMMMMMMEGGGhCCChh   ","   jdCCChGGGEMMMMMMMEGGGhCCChj   ","   jdCCChGGGEMMMMMMMEGGGhCCCdj   ","   jhCCChGGGEMMMMMMMEGGGhCCCdj   ","   hhCCChGGGEMMMMMMMEGGGhCCCdh   ","   hhCCCChGGGFMMMMMFGGGhCCCChh   ","  dshhCCChGGGGEEEEEGGGGhCCChhsd  ","    hhhCCChGGGGGGGGGGGhCCChhh    ","     jhCCChhGGGGGGGGGhhCCChj     ","     jhhCCCChhGGGGGhhCCCChhj     ","      hhhCCCCChhhhhCCCCChhh      ","       jhhCCCCCCCCCCCCChhj       ","        hhhhCCCCCCCCChhhh        ","         jjhhCCCCCCChhjj         ","           hhhdddhhhhh           ","            shhjjjhhs            ","            d       d            ","                                 ","                                 "},
        {"                                 ","            d       d            ","            ss     ss            ","           dshhhhhhhs            ","           hhhdddddhhh           ","         hhhhhhhhhhhhhhh         ","        hhhrrhhhhhhhrrhhh        ","       hhrrrjrhhhhhrjrrrhh       ","      hhrrjrrrhhhhhrrrjrrhh      ","     hhrrrrrhh     hhrrrrrhh     ","     hhrjrhh         hhrjrhh     ","    hhrrrrh           hrrrrhhd   "," dsshhrjrh    EEEEE    hrjrhhssd ","  shhhhrrh   FMMMMMF   hrrhhhhs  ","   hdhhhh   EMMMMMMME   hhhhdh   ","   hdhhhh   EMMMMMMME   hhhhdh   ","   hdhhhh   EMMMMMMME   hhhhdh   ","   hdhhhh   EMMMMMMME   hhhhdh   ","   hdhhhh   EMMMMMMME   hhhhdh   ","  shhhhrrh   FMMMMMF   hrrhhhhs  "," dsshhrjrh    EEEEE    hrjrhhssd ","   dhhrrrrh           hrrrrhh    ","     hhrjrhh         hhrjrhh     ","     hhrrrrrhh     hhrrrrrhh     ","      hhrrjrrrhhhhhrrrjrrhh      ","       hhrrrjrhhhhhrjrrrhh       ","        hhhrrhhhhhhhrrhhh        ","         hhhhhhhhhhhhhhh         ","           hhhdddddhhh           ","            shhhhhhhsd           ","            ss     ss            ","            d       d            ","                                 "},
        {"            ddddddddd            ","            dsssssssd            ","           ddd     ddd           ","           ddddddddddd           ","           ddddddddddd           ","         ddddddddddddddd         ","        ddd  ddddddd  ddd        ","       dd     ddddd     dd       ","      dd      ddddd      dd      ","     dd     dd     dd     dd     ","     dd   dd         dd   dd     ","  dddd    d           d    dddd  ","dddddd   d    EjjjE    d   dddddd","dsddddd  d   FMMMMMF   d  dddddsd","ds dddddd   EMMMMMMME   dddddd sd","ds dddddd   jMMMMMMMj   dddddd sd","ds dddddd   jMMMMMMMj   dddddd sd","ds dddddd   jMMMMMMMj   dddddd sd","ds dddddd   EMMMMMMME   dddddd sd","dsddddd  d   FMMMMMF   d  dddddsd","dddddd   d    EjjjE    d   dddddd","  dddd    d           d    dddd  ","     dd   dd         dd   dd     ","     dd     dd     dd     dd     ","      dd      ddddd      dd      ","       dd     ddddd     dd       ","        ddd  ddddddd  ddd        ","         ddddddddddddddd         ","           ddddddddddd           ","           ddddddddddd           ","           ddd     ddd           ","            dsssssssd            ","            ddddddddd            "},
        {"            BBBBBBBBB            ","          BBB       BBB          ","        BBBBBB     BBBBBB        ","      BB  BBBBBBBBBBBBB  BB      ","     B   BBBBBBBBBBBBBBB   B     ","    B   BBBBBBBBBBBBBBBBB   B    ","   B   BBBB  BBBBBBB  BBBB   B   ","   B  BBB     BBBBB     BBB  B   ","  B  BBB      BBBBB      BBB  B  ","  B BBB     BB     BB     BBB B  "," BBBBBB   BB         BB   BBBBBB "," BBBBB    B           B    BBBBB ","BBBBBB   B    EEEEE    B   BBBBBB","B BBBBB  B   EFFFFFE   B  BBBBB B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B BBBBB  B   EFFFFFE   B  BBBBB B","BBBBBB   B    EEEEE    B   BBBBBB"," BBBBB    B           B    BBBBB "," BBBBBB   BB         BB   BBBBBB ","  B BBB     BB     BB     BBB B  ","  B  BBB      BBBBB      BBB  B  ","   B  BBB     BBBBB     BBB  B   ","   B   BBBB  BBBBBBB  BBBB   B   ","    B   BBBBBBBBBBBBBBBBB   B    ","     B   BBBBBBBBBBBBBBB   B     ","      BB  BBBBBBBBBBBBB  BB      ","        BBBBBB     BBBBBB        ","          BBB       BBB          ","            BBBBBBBBB            "}
    };
    // spotless:on

    private EcoSphereFluidAreaHandler fluidAreaHandler;

    // Create one handler per controller and reuse all cached fluid-area data.
    private EcoSphereFluidAreaHandler getFluidAreaHandler() {
        if (fluidAreaHandler == null) {
            fluidAreaHandler = new EcoSphereFluidAreaHandler(this);
        }
        return fluidAreaHandler;
    }

    // Keep fluid-area transforms aligned with the mirrored multiblock structure.
    public boolean isFluidAreaHorizontallyFlipped() {
        return getFlip().isHorizontallyFlipped();
    }

    // Mark the cleaned area ready to receive the fluid selected by the next recipe.
    private void clearFluidAreaForMode() {
        // Cleaning already removed the old display blocks through the forced animation.
        fluidAreaFluidName = "";
        fluidAreaInitialized = true;
        fluidAreaFillDuration = 0;
        missingFluidAreaInput = null;
        markDirty();
        mUpdated = true;
    }

    // Select the visible fluid for the current mode before consuming recipe fluid.
    public boolean prepareFluidAreaForConsumption(Fluid consumedFluid) {
        IEcoSphereMode mode = getBoundMode();
        if (mode == null) return false;
        boolean fillMainArea = mode.displaysFluidArea();
        Fluid displayFluid = fillMainArea ? consumedFluid : FluidRegistry.WATER;
        return prepareFluidArea(displayFluid, fillMainArea);
    }

    // Return the mode that remains active until cleaning finishes a pending switch.
    private IEcoSphereMode getBoundMode() {
        return boundMode >= 0 && boundMode < MACHINE_MODES.length ? MACHINE_MODES[boundMode] : null;
    }

    // Fill one required layer per processing step and skip all checks after NBT marks completion.
    private boolean prepareFluidArea(Fluid targetFluid, boolean fillMainArea) {
        final int fluidAreaBlockCost = 1000;
        fluidAreaFillDuration = 0;
        if (targetFluid == null || targetFluid.getBlock() == null) {
            fluidAreaInitialized = false;
            missingFluidAreaInput = targetFluid == null ? null : new FluidStack(targetFluid, fluidAreaBlockCost);
            return false;
        }

        String targetName = targetFluid.getName();
        Block targetBlock = targetFluid.getBlock();
        if (!targetName.equals(fluidAreaFluidName)) {
            // A fluid change uses the same cleaning and drain animation as a beacon change.
            if (!fluidAreaFluidName.isEmpty()) {
                requestCleaning(boundMode);
                return false;
            }
            fluidAreaFluidName = targetName;
            fluidAreaInitialized = false;
        }
        // A completed matching area needs no world scan during normal recipes.
        if (fluidAreaInitialized) return true;

        EcoSphereFluidAreaHandler fluidArea = getFluidAreaHandler();
        // Reuse the fixed source layers for every filling step.
        int fluidFlowTickRate = fluidArea.getFluidFlowTickRate(targetFluid);
        int upperSourceLayer = 0;
        int lowestMainSourceLayer = MAIN_SOURCE_LAST_LAYER;
        int lowerSourceLayer = 0;
        int lowerSourceDuration = EcoSphereFluidAreaHandler
            .getLayerDuration(fluidArea.countLayerBlocks(LOWER_SOURCE, lowerSourceLayer));

        // Start the four upper streams, then wait for them to reach the main area's lowest sources.
        if (!fluidArea.isLayerPlaced(UPPER_SOURCE, upperSourceLayer, targetBlock)) {
            int flowDuration = fluidArea
                .getUpperToMainFlowDuration(upperSourceLayer, lowestMainSourceLayer, fluidFlowTickRate);
            if (!fillMainArea) {
                flowDuration += fluidArea
                    .getMainToLowerFlowDuration(lowestMainSourceLayer, lowerSourceLayer, fluidFlowTickRate);
            }
            consumeAndPlaceFluidLayer(
                UPPER_SOURCE,
                upperSourceLayer,
                targetFluid,
                targetBlock,
                fluidAreaBlockCost,
                flowDuration);
            return false;
        }

        if (fillMainArea) {
            int highestMainSourceLayer = 0;
            int lowerSourceTriggerLayer = lowestMainSourceLayer - 1;
            // Fill the lowest four blocks before continuing upward through the main area.
            if (!fluidArea.isLayerPlaced(MAIN_SOURCE, lowestMainSourceLayer, targetBlock)) {
                consumeAndPlaceFluidLayer(
                    MAIN_SOURCE,
                    lowestMainSourceLayer,
                    targetFluid,
                    targetBlock,
                    fluidAreaBlockCost,
                    EcoSphereFluidAreaHandler
                        .getLayerDuration(fluidArea.countLayerBlocks(MAIN_SOURCE, lowestMainSourceLayer)));
                return false;
            }

            // Keep the original block-count timing while filling the main area upward.
            for (int layer = lowestMainSourceLayer; layer >= highestMainSourceLayer; layer--) {
                int sourceCount = fluidArea.countLayerBlocks(MAIN_SOURCE, layer);
                if (sourceCount == 0) continue;
                if (fluidArea.isLayerPlaced(MAIN_SOURCE, layer, targetBlock)) {
                    // Place the lowest source after the layer above the first four blocks is complete.
                    if (layer == lowerSourceTriggerLayer
                        && !fluidArea.isLayerPlaced(LOWER_SOURCE, lowerSourceLayer, targetBlock)) {
                        consumeAndPlaceFluidLayer(
                            LOWER_SOURCE,
                            lowerSourceLayer,
                            targetFluid,
                            targetBlock,
                            fluidAreaBlockCost,
                            lowerSourceDuration);
                        return false;
                    }
                    continue;
                }

                consumeAndPlaceFluidLayer(
                    MAIN_SOURCE,
                    layer,
                    targetFluid,
                    targetBlock,
                    fluidAreaBlockCost,
                    EcoSphereFluidAreaHandler.getLayerDuration(sourceCount));
                return false;
            }
        }

        if (!fluidArea.isLayerPlaced(LOWER_SOURCE, lowerSourceLayer, targetBlock)) {
            consumeAndPlaceFluidLayer(
                LOWER_SOURCE,
                lowerSourceLayer,
                targetFluid,
                targetBlock,
                fluidAreaBlockCost,
                lowerSourceDuration);
            return false;
        }

        fluidAreaInitialized = true;
        missingFluidAreaInput = null;
        markDirty();
        return true;
    }

    // Charge a complete layer before placing it so partial fluid input cannot create free blocks.
    private void consumeAndPlaceFluidLayer(EcoSphereFluidAreaHandler.FluidArea area, int layer, Fluid targetFluid,
        Block targetBlock, int fluidAreaBlockCost, int animationDuration) {
        EcoSphereFluidAreaHandler fluidArea = getFluidAreaHandler();
        int blockCount = fluidArea.countLayerBlocks(area, layer);
        long fluidCost = (long) blockCount * fluidAreaBlockCost;
        if (!EcoSphereModeSupport.drainFluid(this, targetFluid, fluidCost)) {
            fluidAreaInitialized = false;
            missingFluidAreaInput = new FluidStack(targetFluid, (int) Math.min(Integer.MAX_VALUE, fluidCost));
            getBaseMetaTileEntity().disableWorking();
            markDirty();
            return;
        }
        fluidArea.setLayer(area, layer, targetBlock);
        fluidAreaFillDuration = animationDuration;
        missingFluidAreaInput = null;
        markDirty();
        mUpdated = true;
    }

    // Remove only placed fluid sources when the controller leaves the world.
    @Override
    public void onRemoval() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.isServerSide()) {
            Fluid placedFluid = FluidRegistry.getFluid(fluidAreaFluidName);
            if (placedFluid != null) getFluidAreaHandler().clearPlacedSources(placedFluid.getBlock());
            fluidAreaFluidName = "";
            fluidAreaInitialized = false;
            fluidAreaFillDuration = 0;
            missingFluidAreaInput = null;
        }
        super.onRemoval();
    }

    // region Processing Logic
    double tierMultiplier = 1;
    int EuTier = 1;

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        List<RecipeMap<?>> recipeMaps = new ArrayList<>(MACHINE_MODES.length);
        for (IEcoSphereMode mode : MACHINE_MODES) recipeMaps.add(mode.getRecipeMap());
        return recipeMaps;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    private static final IEcoSphereMode[] MACHINE_MODES = { new TreeGrowthSimulatorMode(),
        new AquaticZoneSimulatorMode(), new ArtificialGreenHouseMode(), new DirectedMobClonerMode() };

    @Override
    public GTCM_ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                // Always use the latest beacon before starting the next recipe.
                updateModeBeaconBinding();
                if (inputItems == null) inputItems = new ItemStack[0];
                if (inputFluids == null) inputFluids = new FluidStack[0];

                availableInputPower = availableVoltage * availableAmperage;
                EuTier = (int) Math.max(0, Math.log((double) availableInputPower / 8d) / Math.log(4d));
                updateSlots();
                if (EuTier < 1) return CheckRecipeResultRegistry.insufficientPower(32);
                if (cleaningRequested || cleaningRunActive) {
                    CheckRecipeResult cleaningResult = processCleaning();
                    if (cleaningResult != null) return cleaningResult;
                }
                if (!modeBeaconPresent || boundMode < 0 || boundMode >= MACHINE_MODES.length) {
                    // #tr GT5U.gui.text.recipe_result.eco_sphere_simulator_waiting_for_mode_beacon
                    // # Waiting For Mode Beacon
                    // #zh_CN 等待模式信标
                    return SimpleCheckRecipeResult.ofFailure("eco_sphere_simulator_waiting_for_mode_beacon");
                }
                machineMode = boundMode;
                tierMultiplier = EcoSphereModeSupport.getTierMultiplier(EuTier);
                EcoSphereModeResult modeResult = MACHINE_MODES[machineMode]
                    .process(TST_EcoSphereSimulator.this, EuTier);
                if (!modeResult.result()
                    .wasSuccessful()) {
                    if (cleaningRequested) {
                        CheckRecipeResult cleaningResult = processCleaning();
                        if (cleaningResult != null) return cleaningResult;
                    }
                    if (fluidAreaFillDuration > 0) {
                        // Wait for this layer before the next layer starts filling.
                        // #tr GT5U.gui.text.recipe_result.eco_sphere_simulator_filling_fluid_area
                        // # Filling Eco-Sphere fluid area
                        // #zh_CN 生态圈流体灌注中
                        return waitForAnimation(fluidAreaFillDuration, "eco_sphere_simulator_filling_fluid_area");
                    }
                    if (missingFluidAreaInput != null) return SimpleResultWithText.outOfFluid(missingFluidAreaInput);
                    return modeResult.result();
                }
                outputItems = modeResult.outputs();
                outputFluids = modeResult.fluidOutputs();
                calculatedEut = modeResult.eut();
                duration = modeResult.duration();
                return modeResult.result();
            }

            // Clear one top-down layer per processing step, then bind the queued mode.
            private CheckRecipeResult processCleaning() {
                IEcoSphereMode mode = getBoundMode();
                boolean withMainArea = mode != null && mode.displaysFluidArea();
                if (!cleaningRunActive) {
                    // Reset filling state before the first cleaning layer is removed.
                    cleaningRequested = false;
                    cleaningRunActive = true;
                    fluidAreaInitialized = false;
                    fluidAreaFillDuration = 0;
                    missingFluidAreaInput = null;
                    markDirty();
                    getFluidAreaHandler().startCleaning(withMainArea);
                }

                Fluid placedFluid = FluidRegistry.getFluid(fluidAreaFluidName);
                int cleaningDuration = getFluidAreaHandler()
                    .clearNextCleaningLayer(withMainArea, placedFluid == null ? null : placedFluid.getBlock());
                if (cleaningDuration <= 0) {
                    // Only expose the new mode after every old fluid layer is gone.
                    cleaningRunActive = false;
                    if (pendingMode >= 0) {
                        // Bind the requested mode before GT starts the next recipe automatically.
                        boundMode = pendingMode;
                        machineMode = pendingMode;
                        pendingMode = -1;
                        clearFluidAreaForMode();
                    } else {
                        markDirty();
                    }
                    return null;
                }
                // #tr GT5U.gui.text.recipe_result.eco_sphere_simulator_cleaning
                // # Eco-Sphere cleaning in progress
                // #zh_CN 生态圈清理中
                return waitForAnimation(cleaningDuration, "eco_sphere_simulator_cleaning");
            }

            // Run a zero-power wait step between visible cleaning or filling changes.
            private CheckRecipeResult waitForAnimation(int animationDuration, String resultKey) {
                outputItems = new ItemStack[0];
                outputFluids = new FluidStack[0];
                calculatedEut = 0;
                duration = animationDuration;
                return SimpleCheckRecipeResult.ofSuccess(resultKey);
            }
        };
    }

    public final CropsNHFarm cropsNHFarm = new CropsNHFarm();

    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        int extraLines = missingFluidAreaInput == null ? 3 : 4;
        String[] ret = new String[origin.length + extraLines];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "tierMultiplier"
            + " : "
            + EnumChatFormatting.GOLD
            + (int) this.tierMultiplier;
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Eu tier" + " : " + EnumChatFormatting.GOLD + this.EuTier;
        ret[origin.length + 2] = EnumChatFormatting.AQUA + tr("EcoSphereSimulator.gui.currentRecipe")
            + " : "
            + EnumChatFormatting.GOLD
            + getMachineModeName();
        if (missingFluidAreaInput != null) {
            ret[origin.length + 3] = EnumChatFormatting.RED + SimpleResultWithText.outOfFluid(missingFluidAreaInput)
                .getDisplayString();
        }
        return ret;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_EcoSphereSimulator_MachineType
        // # Tree Farm | Aquatic Farm | Green House | Mob Cloner
        // #zh_CN 树厂 | 渔场 | 温室 | 生物克隆
        tt.addMachineType(tr("Tooltip_EcoSphereSimulator_MachineType"))
            // #tr Tooltip_EcoSphereSimulator_Controller
            // # Controller block for the Eco-Sphere Simulator
            // #zh_CN 拟似生态圈的控制方块
            .addInfo(tr("Tooltip_EcoSphereSimulator_Controller"))
            // #tr Tooltip_EcoSphereSimulator.0.01
            // # {\SPACE}
            // #zh_CN {\SPACE}
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.01"))
            // #tr Tooltip_EcoSphereSimulator.0.02
            // # {\WHITE}Hark to the whispers of all creation......
            // #zh_CN {\WHITE}聆听万物之声......
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.02"))
            // #tr Tooltip_EcoSphereSimulator.0.03
            // # {\WHITE}Yet, save the bees, for they do buzz too loudly.
            // #zh_CN {\WHITE}等一下, 蜜蜂除外. 它实在是太吵了.
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.03"))
            // #tr Tooltip_EcoSphereSimulator.0.04
            // # {\SPACE}
            // #zh_CN {\SPACE}
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.04"))
            // #tr Tooltip_EcoSphereSimulator.0.05
            // # {\SPACE}
            // #zh_CN {\SPACE}
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.05"))
            // #tr Tooltip_EcoSphereSimulator.0.06
            // # {\AQUA}The thaumaturges' latest masterpiece in the Integration of Magic and Electrical Engineering
            // #zh_CN {\AQUA}神秘使在魔电一体化领域的又一力作
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.06"))
            // #tr Tooltip_EcoSphereSimulator.0.07
            // # {\AQUA}Simulate the growth and development cycle of samples using simple raw materials
            // #zh_CN {\AQUA}通过简单的原材料就可以模拟样本的生长发育周期
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.07"))
            .addSeparator()
            // #tr Tooltip_EcoSphereSimulator.0.08
            // # {\GOLD}Features a unique method of overclocking
            // #zh_CN {\GOLD}拥有独特的超频增益方式
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.08"))
            // #tr Tooltip_EcoSphereSimulator.0.09
            // # Recipe time is fixed
            // #zh_CN 配方时间被固定
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.09"))
            // #tr Tooltip_EcoSphereSimulator.0.10
            // # The product increases nonlinearly with the increase of voltage
            // #zh_CN 且产物随着电压的增加而非线性提升
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.10"))
            // #tr Tooltip_EcoSphereSimulator.0.11
            // # Use screwdriver to change mode
            // #zh_CN 使用螺丝刀切换模式
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.11"))
            // #tr Tooltip_EcoSphereSimulator.0.12
            // # Need to pour a bucket of {\AQUA}distilled water {\GRAY}at the top to drive the machine
            // #zh_CN 需要在顶端倒一桶{\AQUA}蒸馏水{\GRAY}来驱动机器
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.12"))
            // #tr Tooltip_EcoSphereSimulator.0.13
            // # Secondary recipes incomplete
            // #zh_CN 二级配方尚未完成
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.13"))
            // #tr Tooltip_EcoSphereSimulator.0.14
            // # {\GREEN}Green House Mode:
            // #zh_CN {\GREEN}工业温室模式:
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.14"))
            // #tr Tooltip_EcoSphereSimulator.0.15
            // # {\SPACE}- supports both normal & CropsNH's crops
            // #zh_CN {\SPACE}- 同时支持普通作物与CropsNH作物
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.15"))
            // #tr Tooltip_EcoSphereSimulator.0.16
            // # {\SPACE}- Can be boosted by supplying enriched fertilizer
            // #zh_CN {\SPACE}- 提供富集肥料提高产量
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.16"))
            // #tr Tooltip_EcoSphereSimulator.0.17
            // # - Has a huge consumption of water
            // #zh_CN {\SPACE}- 运行时消耗大量水
            .addInfo(tr("Tooltip_EcoSphereSimulator.0.17"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            // #tr Tooltip_EcoSphereSimulator_StructurePreview
            // # The complete blueprint includes the animated fluid area marked as air
            // #zh_CN 完整蓝图包含以空气标注的动态流体区域
            .addStructureInfo(tr("Tooltip_EcoSphereSimulator_StructurePreview"))
            .beginStructureBlock(33, 45, 33, false)
            // .addStructureInfo(Text_SeparatingLine)
            .addInputHatch(getBlueprintWithDot(1), 1)
            .addOutputHatch(getBlueprintWithDot(1), 1)
            .addInputBus(getBlueprintWithDot(1), 1)
            .addOutputBus(getBlueprintWithDot(1), 1)
            .addEnergyHatch(getBlueprintWithDot(2), 2)
            // #tr Tooltip_EcoSphereSimulator_FluidArea
            // # Animated fluid area; no blocks are required
            // #zh_CN 动态流体区域；无需放置方块
            .addOtherStructurePart(tr("Tooltip_EcoSphereSimulator_FluidArea"), getBlueprintWithDot(4), 4)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(ModName);
        return tt;
    }

}
