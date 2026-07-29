package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

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
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.IEcoSphereMode;
import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.TreeGrowthSimulatorMode;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.cleanroommc.modularui.drawable.UITexture;
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
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_MegaTreeFarm extends GTCM_MultiMachineBase<TST_MegaTreeFarm> {

    // region Class Constructor
    public TST_MegaTreeFarm(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MegaTreeFarm(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaTreeFarm(this.mName);
    }

    // region Structure

    public static final int MODE_RECIPE_DURATION = 128;
    private static final int TIER_ONE_CLEANING_DURATION = 20 * 300;
    private static final int TIER_TWO_CLEANING_DURATION = 20 * 60;

    private int controllerTier = 0;
    private int boundMode = -1;
    private int pendingMode = -1;
    private boolean modeSymbolPresent = false;
    private boolean cleaningRequested = false;
    private boolean cleaningRunActive = false;
    private int directedMobClonerDebugRecipeId = 0;
    private boolean directedMobClonerDebugActive = false;
    private boolean directedMobClonerDebugStopPending = false;
    private long availableInputPower = 0;
    boolean checkWaterFinish = false;
    boolean checkAirFinish = false;
    private static ItemStack FountOfEcology;
    private static ItemStack Offspring;

    public long fertilizerToConsume = 0;

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Tree Growth Simulator
         * 1 - Aqua Zone Simulator
         * 2 - Green House Simulator
         * 3 - Directed Mob Cloner
         */
        return 4;
    }

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID,
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT };

    public boolean isTierTwo() {
        return controllerTier > 0;
    }

    public boolean hasDirectedMobClonerInfiniteUpgrade() {
        ItemStack symbol = getControllerSlot();
        return symbol != null && symbol.stackSize > 0
            && symbol.getItem() == TstItems.MegaTreeFarmModeSymbol
            && symbol.getItemDamage() == 4;
    }

    public long getAvailableInputPower() {
        return availableInputPower;
    }

    public int beginDirectedMobClonerDebugRun() {
        if (!directedMobClonerDebugActive) {
            directedMobClonerDebugActive = true;
            directedMobClonerDebugRecipeId = 0;
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

    public int getStandardModeDuration() {
        return MODE_RECIPE_DURATION;
    }

    public boolean isOffspring(ItemStack stack) {
        return stack != null && Offspring != null && stack.isItemEqual(Offspring);
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    // @Override
    // public void setMachineModeIcons() {
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER);
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT);
    // machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT);
    // }

    @Override
    public String getMachineModeName() {
        // #tr EcoSphereSimulator.modeMsg.0
        // # Tree Growth Simulator
        // #zh_CN 原木拟生模式

        // #tr EcoSphereSimulator.modeMsg.1
        // # Aqua Zone Simulator
        // #zh_CN 水域模拟模式

        // #tr EcoSphereSimulator.modeMsg.2
        // # Artificial Green House
        // #zh_CN 人工温室模式

        // #tr EcoSphereSimulator.modeMsg.3
        // # Directed Mob Cloner
        // #zh_CN 定向克隆模式
        if (cleaningRequested || cleaningRunActive) return tr("MegaTreeFarm.mode.cleaning");
        return modeSymbolPresent && boundMode >= 0 && boundMode < MACHINE_MODES.length
            ? MACHINE_MODES[boundMode].getDisplayName()
            : tr("MegaTreeFarm.mode.waiting");
    }

    @Override
    public void setMachineMode(int index) {
        if (boundMode < 0) return;
        machineMode = boundMode;
        SetRemoveWater();
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
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) updateModeSymbolBinding();
        if (aBaseMetaTileEntity.isServerSide() && directedMobClonerDebugStopPending && mMaxProgresstime <= 0) {
            resetDirectedMobClonerDebugRun();
            aBaseMetaTileEntity.disableWorking();
        }
        if (aBaseMetaTileEntity.isServerSide() && cleaningRunActive && mMaxProgresstime <= 0) {
            cleaningRunActive = false;
            if (pendingMode >= 0) {
                boundMode = pendingMode;
                machineMode = pendingMode;
                pendingMode = -1;
                markDirty();
                SetRemoveWater();
            }
        }
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == 0 && controllerTier == 0) {
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
                if (getBaseMetaTileEntity().isServerSide()) {
                    markDirty();
                    aPlayer.inventory.markDirty();
                    mUpdated = true;
                }
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    private void updateModeSymbolBinding() {
        int requestedMode = getModeFromSymbol(getControllerSlot());
        boolean wasPresent = modeSymbolPresent;
        modeSymbolPresent = requestedMode >= 0;
        if (wasPresent != modeSymbolPresent) markDirty();
        if (!modeSymbolPresent) return;
        if (cleaningRequested || cleaningRunActive) {
            if (requestedMode != pendingMode) {
                pendingMode = requestedMode;
                markDirty();
            }
            return;
        }
        if (requestedMode == boundMode) return;
        if (boundMode < 0) {
            boundMode = requestedMode;
            machineMode = requestedMode;
        } else {
            pendingMode = requestedMode;
            cleaningRequested = true;
            mProgresstime = 0;
            mMaxProgresstime = 0;
            mOutputItems = null;
            mOutputFluids = null;
        }
        markDirty();
        SetRemoveWater();
    }

    private static int getModeFromSymbol(ItemStack stack) {
        if (stack == null || stack.stackSize <= 0 || stack.getItem() != TstItems.MegaTreeFarmModeSymbol) return -1;
        int meta = stack.getItemDamage();
        if (meta >= 0 && meta <= 2) return meta;
        return meta == 3 || meta == 4 ? 3 : -1;
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
        aNBT.setBoolean("checkWater", checkWaterFinish);
        aNBT.setBoolean("checkAir", checkAirFinish);
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
        checkWaterFinish = aNBT.getBoolean("checkWater");
        checkAirFinish = aNBT.getBoolean("checkAir");
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

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tier", controllerTier + 1);
        if (machineMode == 2) {
            tag.setLong("fertilizerToConsume", fertilizerToConsume);
        }

    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("tier")) {
            currentTip.add(
                "Tier: " + EnumChatFormatting.YELLOW + formatNumber(tag.getInteger("tier")) + EnumChatFormatting.RESET);
        }
        if (tag.hasKey("fertilizerToConsume")) {
            // #tr MegaTreeFarm.Waila.fertiConsume
            // # Now consumption of Enriched Fertilizer is :
            // #zh_CN 当前富集肥料消耗量：
            currentTip.add(
                tr("MegaTreeFarm.Waila.fertiConsume") + " " + formatNumber(tag.getLong("fertilizerToConsume")) + "L");
        }

    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack tool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if (!checkStructure(true, getBaseMetaTileEntity())) {
                GTUtility.sendChatTrans(
                    aPlayer,
                    StatCollector.translateToLocal("BallLightning.modeMsg.IncompleteStructure"));
                return;
            }
            super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, tool);
        }
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainEcoSphereSimulator0";
    private static final String STRUCTURE_PIECE_MAIN1 = "mainEcoSphereSimulator1";
    private static final String STRUCTURE_PIECE_WATER = "waterEcoSphereSimulator";
    private static IStructureDefinition<TST_MegaTreeFarm> STRUCTURE_DEFINITION = null;

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        int structureTier = stackSize.stackSize + controllerTier - 1;
        if (structureTier > 1) structureTier = 1;
        this.buildPiece("mainEcoSphereSimulator" + structureTier, stackSize, hintsOnly, 16, 38, 7);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built;
        int builtW;
        int structureTier = stackSize.stackSize + controllerTier - 1;
        if (structureTier > 1) structureTier = 1;
        built = survivalBuildPiece(
            "mainEcoSphereSimulator" + structureTier,
            stackSize,
            16,
            38,
            7,
            elementBudget,
            env,
            false,
            true);
        builtW = survivalBuildPiece(STRUCTURE_PIECE_WATER, stackSize, 0, 37, -9, elementBudget, env, false, true);
        if (built >= 0) return built;
        return built + builtW;

    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        // setDebugEnabled(true);
        if (!checkPiece("mainEcoSphereSimulator" + controllerTier, 16, 38, 7, errors)) {
            return;
        }
        if (!checkPiece(STRUCTURE_PIECE_WATER, 0, 37, -9, errors)) {
            // #tr TST_MegaTreeFarm.StructureErrors.no_water_block
            // # Place a water block at the specific location on the top of the machine structure.
            // #zh_CN 在机器结构顶部的特定位置放置水方块
            errors.add(StructureErrors.of("TST_MegaTreeFarm.StructureErrors.no_water_block"));
        }
    }

    @Override
    public IStructureDefinition<TST_MegaTreeFarm> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaTreeFarm>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addShape(STRUCTURE_PIECE_MAIN1, transpose(shape2))
                .addShape(STRUCTURE_PIECE_WATER, transpose(water))
                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(MetaBlockCasing01, 9))
                .addElement('C', ofBlock(MetaBlockCasing01, 10))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings1, 10))
                .addElement('d', ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 5))
                .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 10))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings9, 1))
                .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 15))
                .addElement('h', ofBlock(MetaBlockCasing01, 13))
                .addElement('I', ofBlock(ModBlocks.blockCasingsTieredGTPP, 8))
                .addElement(
                    'J',
                    ofBlock(ModBlocksHandler.BlockTranslucent.getLeft(), ModBlocksHandler.BlockTranslucent.getRight()))
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
                    ofBlock(ModBlocksHandler.PurpleLight.getLeft(), ModBlocksHandler.PurpleLight.getRight()))
                .addElement('P', ofBlock(BlocksItems.getFluidBlock(InternalName.fluidDistilledWater), 0))
                .addElement(
                    'Q',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .hint(1)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement(
                    'q',
                    ofChain(
                        ofBlock(MetaBlockCasing01, 13),
                        HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .hint(1)
                            .casingIndex(MetaBlockCasing01.getTextureIndex(13))
                            .build()))
                .addElement(
                    'R',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .hint(2)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .addElement(
                    'r',
                    ofChain(
                        ofBlock(MetaBlockCasing01, 13),
                        HatchElementBuilder.<TST_MegaTreeFarm>builder()
                            .atLeast(Energy.or(ExoticEnergy))
                            .adder(TST_MegaTreeFarm::addToMachineList)
                            .hint(2)
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
    A -> ofBlock...(BW_GlasBlocks, 0, ...);
    B -> ofBlock...(MetaBlockCasing01, 9, ...);
    C -> ofBlock...(MetaBlockCasing01, 10, ...);
    D -> ofBlock...(gt.blockcasings, 10, ...);
    E -> ofBlock...(gt.blockcasings8, 5, ...);
    F -> ofBlock...(gt.blockcasings8, 10, ...);
    G -> ofBlock...(gt.blockcasings9, 1, ...);
    H -> ofBlock...(gtplusplus.blockcasings.2, 15, ...);
    I -> ofBlock...(gtplusplus.blocktieredcasings.1, 8, ...);
    J -> ofBlock...(tile.blockTranslucent, 0, ...);
    K -> ofBlock...(tile.crystalBlock, 0, ...);
    L -> ofBlock...(tile.crystalBlock, 2, ...);
    M -> ofBlock...(tile.crystalBlock, 3, ...);
    N -> ofBlock...(tile.for.soil, 0, ...);
    O -> ofBlock...(tile.redstoneLight, 0, ...);
    P -> ofBlock...(tile.water, 0, ...);
    Q -> ofBlock...(tile.wood, 0, ...);
    R -> ofBlock...(tile.wood, 1, ...);
    S -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...);
    */

    private final String[][] shape = new String[][]{
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              DDDDD              ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","             DDDDDDD             ","              DDDDD              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","             GGD DGG             ","            GGGD DGGG            ","           GGGHH HHGGG           ","           GGHSH HSHGG           ","          DDDHHH HHHDDD          ","                                 ","          DDDHHH HHHDDD          ","           GGHSH HSHGG           ","           GGGHH HHGGG           ","            GGGD DGGG            ","             GGD DGG             ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             GGD DGG             ","           DGGGSDSGGGD           ","          DGG  SDS  GGD          ","          GG  HHDHH  GG          ","         GG  HIIDIIH  GG         ","         GG HIIIDIIIH GG         ","         DSSHIIIDIIIHSSD         ","          DDDDDDDDDDDDD          ","         DSSHIIIDIIIHSSD         ","         GG HIIIDIIIH GG         ","         GG  HIIDIIH  GG         ","          GG  HHDHH  GG          ","          DGG  SDS  GGD          ","           DGGGSDSGGGD           ","             GGD DGG             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","            DGGD DGGD            ","           AD  SDS  DA           ","          A     S     A          ","         A      S      A         ","        DD    DDDDD    DD        ","        G    DOOOOOD    G        ","        G   DOOOOOOOD   G        ","       DDS  DOOOOOOOD  SDD       ","         DSSDOOOOOOODSSD         ","       DDS  DOOOOOOOD  SDD       ","        G   DOOOOOOOD   G        ","        G    DOOOOOD    G        ","        DD    DDDDD    DD        ","         A      S      A         ","          A     S     A          ","           AD  SDS  DA           ","            DGGD DGGD            ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","            ADDSDSDDA            ","          AA   SDS   AA          ","         AA           AA         ","        AA             AA        ","        A               A        ","       A                 A       ","       D                 D       ","       D                 D       ","      DSS               SSD      ","       DD               DD       ","      DSS               SSD      ","       D                 D       ","       D                 D       ","       A                 A       ","        A               A        ","        AA             AA        ","         AA           AA         ","          AA   SDS   AA          ","            ADDSDSDDA            ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","            AAAD DAAA            ","          AA   SDS   AA          ","         A             A         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","      A                   A      ","      DS                 SD      ","       D                 D       ","      DS                 SD      ","      A                   A      ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         A             A         ","          AA   SDS   AA          ","            AAAD DAAA            ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","             AAD DAA             ","           AA  SDS  AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     DS                   SD     ","      D                   D      ","     DS                   SD     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA  SDS  AA           ","             AAD DAA             ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               D D               ","           AAAASDSAAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    DS                     SD    ","     D                     D     ","    DS                     SD    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAASDSAAAA           ","               D D               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            DDDD DDDD            ","          DD   S S   DD          ","        DD             DD        ","       D                 D       ","      D                   D      ","      D                   D      ","     D                     D     ","     D                     D     ","    D                       D    ","    D                       D    ","    D                       D    ","    DS                     SD    ","                                 ","    DS                     SD    ","    D                       D    ","    D                       D    ","    D                       D    ","     D                     D     ","     D                     D     ","      D                   D      ","      D                   D      ","       D                 D       ","        DD             DD        ","          DD   S S   DD          ","            DDDD DDDD            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAADDDAAA            ","          AA   S S   AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    DS                     SD    ","    D                       D    ","    DS                     SD    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA   S S   AA          ","            AAADDDAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AA AA              ","           AAA SDS AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   AS                       SA   ","    D                       D    ","   AS                       SA   ","    A                        A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA SDS AAA           ","              AA AA              ","                                 ","                                 ","                                 "},
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
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","            SSSDHDSSS            ","          SSAAAN NAAASS          ","         SAANNNN NNNNAAS         ","        SAANNNNN NNNNNAAS        ","       SAANNNNNNNNNNNNNAAS       ","       SANNNNNNNNNNNNNNNAS       ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      DNNNNNNNNNNNNNNNNNNND      ","     DH   NNNNNNNNNNNNN   HD     ","      DNNNNNNNNNNNNNNNNNNND      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","      SANNNNNNNNNNNNNNNNNAS      ","       SANNNNNNNNNNNNNNNAS       ","       SAANNNNNNNNNNNNNAAS       ","        SAANNNNN NNNNNAAS        ","         SAANNNN NNNNAAS         ","          SSAAAN NAAASS          ","            SSSDHDSSS            ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","               AHA               ","            AAAAHAAAA            ","           AANNN NNNAA           ","          ANNNNN NNNNNA          ","         ANNNNNNNNNNNNNA         ","        AANNNNNNNNNNNNNAA        ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","       AANNNNNNNNNNNNNNNAA       ","      DHH  NNNNNNNNNNN  HHD      ","       AANNNNNNNNNNNNNNNAA       ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","        AANNNNNNNNNNNNNAA        ","         ANNNNNNNNNNNNNA         ","          ANNNNN NNNNNA          ","           AANNN NNNAA           ","            AAAAHAAAA            ","               AHA               ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","                D                ","             AAAHAAA             ","           AAAAN NAAAA           ","          AAANNN NNNAAA          ","          AANNNN NNNNAA          ","         AANNNNN NNNNNAA         ","         AANNNNNNNNNNNAA         ","         ANNNNNNNNNNNNNA         ","       DDH    NNNNN    HDD       ","         ANNNNNNNNNNNNNA         ","         AANNNNNNNNNNNAA         ","         AANNNNN NNNNNAA         ","          AANNNN NNNNAA          ","          AAANNN NNNAAA          ","           AAAAN NAAAA           ","             AAAHAAA             ","                D                ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                D                ","               AHA               ","             AAAHAAA             ","            AAAAHAAAA            ","           AAANN NNAAA           ","           AANNN NNNAA           ","          AAANNN NNNAAA          ","         DHHH       HHHD         ","          AAANNN NNNAAA          ","           AANNN NNNAA           ","           AAANN NNAAA           ","            AAAAHAAAA            ","             AAAHAAA             ","               AHA               ","                D                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
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
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             D     D             ","             DSQ~QSD             ","            SDIIIIIDS            ","          SSIIIIIIIIISS          ","         SIIIIIIIIIIIIIS         ","         SIIIIIIIIIIIIIS         ","        SIIIIIEEEEEIIIIIS        ","      DDDIIIIFMMMMMFIIIIDDD      ","       SIIIIEMMBBBMMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMBBBBBMEIIIIS       ","       SIIIIEMMBBBMMEIIIIS       ","      DDDIIIIFMMMMMFIIIIDDD      ","        SIIIIIEEEEEIIIIIS        ","         SIIIIIIIIIIIIIS         ","         SIIIIIIIIIIIIIS         ","          SSIIIIIIIIISS          ","            SDIIIIIDS            ","             DSSSSSD             ","             D     D             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             DDDDDDD             ","           DDDQQQQQDDD           ","         DDQQHQQQQQHQQDD         ","        DQQQQHGHHHGHQQQQD        ","       DQQQQGHIIIIIHGQQQQD       ","      DQQQGGIIKKKKKIIGGQQQD      ","      DQQGIIKKKKKKKKKIIGQQD      ","     DQQQGIKKKKKKKKKKKIGQQQD     ","     DQQGIKKKKEEEEEKKKKIGQQD     ","    DDHHHIKKKFMMMMMFKKKIHHHDD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DQQGIKKKEMMMMMMMEKKKIGQQD    ","    DDHHHIKKKFMMMMMFKKKIHHHDD    ","     DQQGIKKKKEEEEEKKKKIGQQD     ","     DQQQGIKKKKKKKKKKKIGQQQD     ","      DQQGIIKKKKKKKKKIIGQQD      ","      DQQQGGIIKKKKKIIGGQQQD      ","       DQQQQGHIIIIIHGQQQQD       ","        DQQQQHGGGGGHQQQQD        ","         DDQQHQQQQQHQQDD         ","           DDDQQQQQDDD           ","             DDDDDDD             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","            DHHHHHHHD            ","           HDHHHDHHHDH           ","         HHHHDDDDDDDHHHH         ","        HHHDDCCCCCCCDDHHH        ","       HHDDCCCCCCCCCCCDDHH       ","      HHDCCCCCDDDDDCCCCCDHH      ","     HHDCCCCHHCCCCCHHCCCCDHH     ","     HHDCCHHCCCCCCCCCHHCCDHH     ","    HHDCCCHCCCCCCCCCCCHCCCDHH    ","   DDHDCCHCCCCEEEEECCCCHCCDHDD   ","   HHDCCCHCCCFMMMMMFCCCHCCCDHH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HDDCCDCCCEMMMMMMMECCCDCCDDH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HHDCCDCCCEMMMMMMMECCCDCCDHH   ","   HHDCCCHCCCFMMMMMFCCCHCCCDHH   ","   DDHDCCHCCCCEEEEECCCCHCCDHDD   ","    HHDCCCHCCCCCCCCCCCHCCCDHH    ","     HHDCCHHCCCCCCCCCHHCCDHH     ","     HHDCCCCHHCCCCCHHCCCCDHH     ","      HHDCCCCCDDDDDCCCCCDHH      ","       HHDDCCCCCCCCCCCDDHH       ","        HHHDDCCCCCCCDDHHH        ","         HHHHDDDDDDDHHHH         ","           HDHHHDHHHDH           ","            DHHHHHHHD            ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","            D       D            ","            SHHJJJHHS            ","           HHHHHDHHHHH           ","         JJHHCCCCCCCHHJJ         ","        HHHCCCCCCCCCCCHHH        ","       JHCCCCCCCCCCCCCCCHJ       ","      HHCCCCCCHHHHHCCCCCCHH      ","     JHCCCCCHHGGGGGHHCCCCCHJ     ","     JHCCCHHGGGGGGGGGHHCCCHJ     ","    HHCCCCHGGGGGGGGGGGHCCCCHH    ","  DSHHCCCHGGGGEEEEEGGGGHCCCHHSD  ","   HHCCCCHGGGFMMMMMFGGGHCCCCHH   ","   HHCCCHGGGEMMMMMMMEGGGHCCCHH   ","   JHCCCHGGGEMMMMMMMEGGGHCCCHJ   ","   JDCCCHGGGEMMMMMMMEGGGHCCCDJ   ","   JHCCCHGGGEMMMMMMMEGGGHCCCHJ   ","   HHCCCHGGGEMMMMMMMEGGGHCCCHH   ","   HHCCCCHGGGFMMMMMFGGGHCCCCHH   ","  DSHHCCCHGGGGEEEEEGGGGHCCCHHSD  ","    HHCCCCHGGGGGGGGGGGHCCCCHH    ","     JHCCCHHGGGGGGGGGHHCCCHJ     ","     JHCCCCCHHGGGGGHHCCCCCHJ     ","      HHCCCCCCHHHHHCCCCCCHH      ","       JHCCCCCCCCCCCCCCCHJ       ","        HHHCCCCCCCCCCCHHH        ","         JJHHCCCCCCCHHJJ         ","           HHHHHDHHHHH           ","            SHHJJJHHS            ","            D       D            ","                                 ","                                 "},
        {"                                 ","            D       D            ","            SS     SS            ","            SHRRRRRHS            ","           HHHHHDHHHHH           ","         HHHHHHHHHHHHHHH         ","        HHHRRHHHHHHHRRHHH        ","       HHRRRJRHHHHHRJRRRHH       ","      HHRRJRRRHHHHHRRRJRRHH      ","     HHRRRRRHH     HHRRRRRHH     ","     HHRJRHH         HHRJRHH     ","    HHRRRRH           HRRRRHH    "," DSSHHRJRH    EEEEE    HRJRHHSSD ","  SHHHHRRH   FMMMMMF   HRRHHHHS  ","   RHHHHH   EMMMMMMME   HHHHHR   ","   RHHHHH   EMMMMMMME   HHHHHR   ","   RDHHHH   EMMMMMMME   HHHHDR   ","   RHHHHH   EMMMMMMME   HHHHHR   ","   RHHHHH   EMMMMMMME   HHHHHR   ","  SHHHHRRH   FMMMMMF   HRRHHHHS  "," DSSHHRJRH    EEEEE    HRJRHHSSD ","    HHRRRRH           HRRRRHH    ","     HHRJRHH         HHRJRHH     ","     HHRRRRRHH     HHRRRRRHH     ","      HHRRJRRRHHHHHRRRJRRHH      ","       HHRRRJRHHHHHRJRRRHH       ","        HHHRRHHHHHHHRRHHH        ","         HHHHHHHHHHHHHHH         ","           HHHHHDHHHHH           ","            SHRRRRRHS            ","            SS     SS            ","            D       D            ","                                 "},
        {"            DDDDDDDDD            ","            DSSSSSSSD            ","           DDD     DDD           ","           DDDDDDDDDDD           ","           DDDDDDDDDDD           ","         DDDDDDDDDDDDDDD         ","        DDD  DDDDDDD  DDD        ","       DD     DDDDD     DD       ","      DD      DDDDD      DD      ","     DD     DD     DD     DD     ","     DD   DD         DD   DD     ","  DDDD    D           D    DDDD  ","DDDDDD   D    EJJJE    D   DDDDDD","DSDDDDD  D   FMMMMMF   D  DDDDDSD","DS DDDDDD   EMMMMMMME   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   JMMMMMMMJ   DDDDDD SD","DS DDDDDD   EMMMMMMME   DDDDDD SD","DSDDDDD  D   FMMMMMF   D  DDDDDSD","DDDDDD   D    EJJJE    D   DDDDDD","  DDDD    D           D    DDDD  ","     DD   DD         DD   DD     ","     DD     DD     DD     DD     ","      DD      DDDDD      DD      ","       DD     DDDDD     DD       ","        DDD  DDDDDDD  DDD        ","         DDDDDDDDDDDDDDD         ","           DDDDDDDDDDD           ","           DDDDDDDDDDD           ","           DDD     DDD           ","            DSSSSSSSD            ","            DDDDDDDDD            "},
        {"            BBBBBBBBB            ","          BBB       BBB          ","        BBBBBB     BBBBBB        ","      BB  BBBBBBBBBBBBB  BB      ","     B   BBBBBBBBBBBBBBB   B     ","    B   BBBBBBBBBBBBBBBBB   B    ","   B   BBBB  BBBBBBB  BBBB   B   ","   B  BBB     BBBBB     BBB  B   ","  B  BBB      BBBBB      BBB  B  ","  B BBB     BB     BB     BBB B  "," BBBBBB   BB         BB   BBBBBB "," BBBBB    B           B    BBBBB ","BBBBBB   B    EEEEE    B   BBBBBB","B BBBBB  B   EFFFFFE   B  BBBBB B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B BBBBB  B   EFFFFFE   B  BBBBB B","BBBBBB   B    EEEEE    B   BBBBBB"," BBBBB    B           B    BBBBB "," BBBBBB   BB         BB   BBBBBB ","  B BBB     BB     BB     BBB B  ","  B  BBB      BBBBB      BBB  B  ","   B  BBB     BBBBB     BBB  B   ","   B   BBBB  BBBBBBB  BBBB   B   ","    B   BBBBBBBBBBBBBBBBB   B    ","     B   BBBBBBBBBBBBBBB   B     ","      BB  BBBBBBBBBBBBB  BB      ","        BBBBBB     BBBBBB        ","          BBB       BBB          ","            BBBBBBBBB            "}
    };

    private final String [][] shape2 =new String[][]{
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              ddddd              ","             ddddddd             ","             ddddddd             ","             ddddddd             ","             ddddddd             ","             ddddddd             ","              ddddd              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","             GGd dGG             ","            GGGd dGGG            ","           GGGhh hhGGG           ","           GGhsh hshGG           ","          dddhhh hhhddd          ","                                 ","          dddhhh hhhddd          ","           GGhsh hshGG           ","           GGGhh hhGGG           ","            GGGd dGGG            ","             GGd dGG             ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             GGd dGG             ","           dGGGsdsGGGd           ","          dGG  sds  GGd          ","          GG  hhdhh  GG          ","         GG  hIIdIIh  GG         ","         GG hIIIdIIIh GG         ","         dsshIIIdIIIhssd         ","          ddddddddddddd          ","         dsshIIIdIIIhssd         ","         GG hIIIdIIIh GG         ","         GG  hIIdIIh  GG         ","          GG  hhdhh  GG          ","          dGG  sds  GGd          ","           dGGGsdsGGGd           ","             GGd dGG             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","            dGGd dGGd            ","           Ad  sds  dA           ","          A     s     A          ","         A      s      A         ","        dd    ddddd    dd        ","        G    dOOOOOd    G        ","        G   dOOOOOOOd   G        ","       dds  dOOOOOOOd  sdd       ","         dssdOOOOOOOdssd         ","       dds  dOOOOOOOd  sdd       ","        G   dOOOOOOOd   G        ","        G    dOOOOOd    G        ","        dd    ddddd    dd        ","         A      s      A         ","          A     s     A          ","           Ad  sds  dA           ","            dGGd dGGd            ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               d d               ","            AddsdsddA            ","          AA   sds   AA          ","         AA           AA         ","        AA             AA        ","        A               A        ","       A                 A       ","       d                 d       ","       d                 d       ","      dss               ssd      ","       dd               dd       ","      dss               ssd      ","       d                 d       ","       d                 d       ","       A                 A       ","        A               A        ","        AA             AA        ","         AA           AA         ","          AA   sds   AA          ","            AddsdsddA            ","               d d               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","            AAAd dAAA            ","          AA   sds   AA          ","         A             A         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","      A                   A      ","      ds                 sd      ","       d                 d       ","      ds                 sd      ","      A                   A      ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         A             A         ","          AA   sds   AA          ","            AAAd dAAA            ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","             AAd dAA             ","           AA  sds  AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     ds                   sd     ","      d                   d      ","     ds                   sd     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA  sds  AA           ","             AAd dAA             ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               d d               ","           AAAAsdsAAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    ds                     sd    ","     d                     d     ","    ds                     sd    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAAsdsAAAA           ","               d d               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            dddd dddd            ","          dd   s s   dd          ","        dd             dd        ","       d                 d       ","      d                   d      ","      d                   d      ","     d                     d     ","     d                     d     ","    d                       d    ","    d                       d    ","    d                       d    ","    ds                     sd    ","                                 ","    ds                     sd    ","    d                       d    ","    d                       d    ","    d                       d    ","     d                     d     ","     d                     d     ","      d                   d      ","      d                   d      ","       d                 d       ","        dd             dd        ","          dd   s s   dd          ","            dddd dddd            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAAdddAAA            ","          AA   s s   AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    ds                     sd    ","    d                       d    ","    ds                     sd    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA   s s   AA          ","            AAAdddAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AA AA              ","           AAA sds AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   As                       sA   ","    d                       d    ","   As                       sA   ","    A                        A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA sds AAA           ","              AA AA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","       AA               AA       ","      A                   A      ","      A                   A      ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","      A                   A      ","      A                   A      ","       AA               AA       ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","             AAAdAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAdAAA             ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","              AAdAA              ","           AAA     AAA           ","         AA           AA         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","   A                         A   ","   A                         A   ","   d                         d   ","   A                         A   ","   A                         A   ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         AA           AA         ","           AAA     AAA           ","              AAdAA              ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","            AAAAdAAAA            ","          AA         AA          ","        AA             AA        ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","    d                       d    ","    A                       A    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","        AA             AA        ","          AA         AA          ","            AAAAdAAAA            ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             AAAdAAA             ","          AAA       AAA          ","         A             A         ","        A               A        ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    A                       A    ","    A                       A    ","    d                       d    ","    A                       A    ","    A                       A    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","        A               A        ","         A             A         ","          AAA       AAA          ","             AAAdAAA             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","               AdA               ","           AAAA   AAAA           ","          A           A          ","        AA             AA        ","       A                 A       ","       A                 A       ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","    A                       A    ","    d                       d    ","    A                       A    ","     A                     A     ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","       A                 A       ","       A                 A       ","        AA             AA        ","          A           A          ","           AAAA   AAAA           ","               AdA               ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                d                ","             AAAhAAA             ","           AA       AA           ","         AA           AA         ","        A               A        ","       A                 A       ","       A                 A       ","      A                   A      ","      A                   A      ","     A                     A     ","     A                     A     ","     A                     A     ","    dh                     hd    ","     A                     A     ","     A                     A     ","     A                     A     ","      A                   A      ","      A                   A      ","       A                 A       ","       A                 A       ","        A               A        ","         AA           AA         ","           AA       AA           ","             AAAhAAA             ","                d                ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","            ddddddddd            ","          ddhhhhhhhhhdd          ","         dhhNNNN NNNNhhd         ","        dhNNNNNNNNNNNNNhd        ","       dhNNNNNNNNNNNNNNNhd       ","      dhNNNNNNNNNNNNNNNNNhd      ","      dhNNNNNNNNNNNNNNNNNhd      ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dh NNNNNNNNNNNNNNNNN hd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","     dhNNNNNNNNNNNNNNNNNNNhd     ","      dhNNNNNNNNNNNNNNNNNhd      ","      dhNNNNNNNNNNNNNNNNNhd      ","       dhNNNNNNNNNNNNNNNhd       ","        dhNNNNNNNNNNNNNhd        ","         dhhNNNN NNNNhhd         ","          ddhhhhhhhhhdd          ","            ddddddddd            ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","            sssdhdsss            ","          ssAAAN NAAAss          ","         sAANNNN NNNNAAs         ","        sAANNNNN NNNNNAAs        ","       sAANNNNNNNNNNNNNAAs       ","       sANNNNNNNNNNNNNNNAs       ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","      dNNNNNNNNNNNNNNNNNNNd      ","     dh   NNNNNNNNNNNNN   hd     ","      dNNNNNNNNNNNNNNNNNNNd      ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","      sANNNNNNNNNNNNNNNNNAs      ","       sANNNNNNNNNNNNNNNAs       ","       sAANNNNNNNNNNNNNAAs       ","        sAANNNNN NNNNNAAs        ","         sAANNNN NNNNAAs         ","          ssAAAN NAAAss          ","            sssdhdsss            ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","               AhA               ","            AAAAhAAAA            ","           AANNN NNNAA           ","          ANNNNN NNNNNA          ","         ANNNNNNNNNNNNNA         ","        AANNNNNNNNNNNNNAA        ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","       AANNNNNNNNNNNNNNNAA       ","      dhh  NNNNNNNNNNN  hhd      ","       AANNNNNNNNNNNNNNNAA       ","        ANNNNNNNNNNNNNNNA        ","        ANNNNNNNNNNNNNNNA        ","        AANNNNNNNNNNNNNAA        ","         ANNNNNNNNNNNNNA         ","          ANNNNN NNNNNA          ","           AANNN NNNAA           ","            AAAAhAAAA            ","               AhA               ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","                d                ","             AAAhAAA             ","           AAAAN NAAAA           ","          AAANNN NNNAAA          ","          AANNNN NNNNAA          ","         AANNNNN NNNNNAA         ","         AANNNNNNNNNNNAA         ","         ANNNNNNNNNNNNNA         ","       ddh    NNNNN    hdd       ","         ANNNNNNNNNNNNNA         ","         AANNNNNNNNNNNAA         ","         AANNNNN NNNNNAA         ","          AANNNN NNNNAA          ","          AAANNN NNNAAA          ","           AAAAN NAAAA           ","             AAAhAAA             ","                d                ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","               AhA               ","             AAAhAAA             ","            AAAAhAAAA            ","           AAANN NNAAA           ","           AANNN NNNAA           ","          AAANNN NNNAAA          ","         dhhh       hhhd         ","          AAANNN NNNAAA          ","           AANNN NNNAA           ","           AAANN NNAAA           ","            AAAAhAAAA            ","             AAAhAAA             ","               AhA               ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                d                ","                d                ","              ddddd              ","             dhhhhhd             ","            dhhhhhhhd            ","            dhhhhhhhd            ","          dddhhh hhhddd          ","            dhhhhhhhd            ","            dhhhhhhhd            ","             dhhhhhd             ","              ddddd              ","                d                ","                d                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","              ddddd              ","             ddJdJdd             ","             dJJdJJd             ","             ddd ddd             ","             dJJdJJd             ","             ddJdJdd             ","              ddddd              ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
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
        {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             d     d             ","             dsq~qsd             ","            sdIIIIIds            ","          ssIIIIIIIIIss          ","         sIIIIIIIIIIIIIs         ","         sIIIIIIIIIIIIIs         ","        sIIIIIEEEEEIIIIIs        ","      dddIIIIFMMMMMFIIIIddd      ","       sIIIIEMMBBBMMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMBBBBBMEIIIIs       ","       sIIIIEMMBBBMMEIIIIs       ","      dddIIIIFMMMMMFIIIIddd      ","        sIIIIIEEEEEIIIIIs        ","         sIIIIIIIIIIIIIs         ","         sIIIIIIIIIIIIIs         ","          ssIIIIIIIIIss          ","            sdIIIIIds            ","             dsssssd             ","             d     d             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","                                 ","             ddddddd             ","           dddqqqqqddd           ","         ddqqhqqqqqhqqdd         ","        dqqqqhGhhhGhqqqqd        ","       dqqqqGhIIIIIhGqqqqd       ","      dqqqGGIIKKKKKIIGGqqqd      ","      dqqGIIKKKKKKKKKIIGqqd      ","     dqqqGIKKKKKKKKKKKIGqqqd     ","     dqqGIKKKKEEEEEKKKKIGqqd     ","    ddhhhIKKKFMMMMMFKKKIhhhdd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    dqqGIKKKEMMMMMMMEKKKIGqqd    ","    ddhhhIKKKFMMMMMFKKKIhhhdd    ","     dqqGIKKKKEEEEEKKKKIGqqd     ","     dqqqGIKKKKKKKKKKKIGqqqd     ","      dqqGIIKKKKKKKKKIIGqqd      ","      dqqqGGIIKKKKKIIGGqqqd      ","       dqqqqGhIIIIIhGqqqqd       ","        dqqqqhGGGGGhqqqqd        ","         ddqqhqqqqqhqqdd         ","           dddqqqqqddd           ","             ddddddd             ","                                 ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","                                 ","            dhhhhhhhd            ","           hdhhhdhhhdh           ","         hhhhdddddddhhhh         ","        hhhddCCCCCCCddhhh        ","       hhddCCCCCCCCCCCddhh       ","      hhdCCCCCdddddCCCCCdhh      ","     hhdCCCChhCCCCChhCCCCdhh     ","     hhdCChhCCCCCCCCChhCCdhh     ","    hhdCCChCCCCCCCCCCChCCCdhh    ","   ddhdCChCCCCEEEEECCCChCCdhdd   ","   hhdCCChCCCFMMMMMFCCChCCCdhh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hddCCdCCCEMMMMMMMECCCdCCddh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hhdCCdCCCEMMMMMMMECCCdCCdhh   ","   hhdCCChCCCFMMMMMFCCChCCCdhh   ","   ddhdCChCCCCEEEEECCCChCCdhdd   ","    hhdCCChCCCCCCCCCCChCCCdhh    ","     hhdCChhCCCCCCCCChhCCdhh     ","     hhdCCCChhCCCCChhCCCCdhh     ","      hhdCCCCCdddddCCCCCdhh      ","       hhddCCCCCCCCCCCddhh       ","        hhhddCCCCCCCddhhh        ","         hhhhdddddddhhhh         ","           hdhhhdhhhdh           ","            dhhhhhhhd            ","                                 ","                                 ","                                 "},
        {"                                 ","                                 ","            d       d            ","            shhJJJhhs            ","           hhhhhdhhhhh           ","         JJhhCCCCCCChhJJ         ","        hhhCCCCCCCCCCChhh        ","       JhCCCCCCCCCCCCCCChJ       ","      hhCCCCCChhhhhCCCCCChh      ","     JhCCCCChhGGGGGhhCCCCChJ     ","     JhCCChhGGGGGGGGGhhCCChJ     ","    hhCCCChGGGGGGGGGGGhCCCChh    ","  dshhCCChGGGGEEEEEGGGGhCCChhsd  ","   hhCCCChGGGFMMMMMFGGGhCCCChh   ","   hhCCChGGGEMMMMMMMEGGGhCCChh   ","   JhCCChGGGEMMMMMMMEGGGhCCChJ   ","   JdCCChGGGEMMMMMMMEGGGhCCCdJ   ","   JhCCChGGGEMMMMMMMEGGGhCCChJ   ","   hhCCChGGGEMMMMMMMEGGGhCCChh   ","   hhCCCChGGGFMMMMMFGGGhCCCChh   ","  dshhCCChGGGGEEEEEGGGGhCCChhsd  ","    hhCCCChGGGGGGGGGGGhCCCChh    ","     JhCCChhGGGGGGGGGhhCCChJ     ","     JhCCCCChhGGGGGhhCCCCChJ     ","      hhCCCCCChhhhhCCCCCChh      ","       JhCCCCCCCCCCCCCCChJ       ","        hhhCCCCCCCCCCChhh        ","         JJhhCCCCCCChhJJ         ","           hhhhhdhhhhh           ","            shhJJJhhs            ","            d       d            ","                                 ","                                 "},
        {"                                 ","            d       d            ","            ss     ss            ","            shrrrrrhs            ","           hhhhhdhhhhh           ","         hhhhhhhhhhhhhhh         ","        hhhrrhhhhhhhrrhhh        ","       hhrrrJrhhhhhrJrrrhh       ","      hhrrJrrrhhhhhrrrJrrhh      ","     hhrrrrrhh     hhrrrrrhh     ","     hhrJrhh         hhrJrhh     ","    hhrrrrh           hrrrrhh    "," dsshhrJrh    EEEEE    hrJrhhssd ","  shhhhrrh   FMMMMMF   hrrhhhhs  ","   rhhhhh   EMMMMMMME   hhhhhr   ","   rhhhhh   EMMMMMMME   hhhhhr   ","   rdhhhh   EMMMMMMME   hhhhdr   ","   rhhhhh   EMMMMMMME   hhhhhr   ","   rhhhhh   EMMMMMMME   hhhhhr   ","  shhhhrrh   FMMMMMF   hrrhhhhs  "," dsshhrJrh    EEEEE    hrJrhhssd ","    hhrrrrh           hrrrrhh    ","     hhrJrhh         hhrJrhh     ","     hhrrrrrhh     hhrrrrrhh     ","      hhrrJrrrhhhhhrrrJrrhh      ","       hhrrrJrhhhhhrJrrrhh       ","        hhhrrhhhhhhhrrhhh        ","         hhhhhhhhhhhhhhh         ","           hhhhhdhhhhh           ","            shrrrrrhs            ","            ss     ss            ","            d       d            ","                                 "},
        {"            ddddddddd            ","            dsssssssd            ","           ddd     ddd           ","           ddddddddddd           ","           ddddddddddd           ","         ddddddddddddddd         ","        ddd  ddddddd  ddd        ","       dd     ddddd     dd       ","      dd      ddddd      dd      ","     dd     dd     dd     dd     ","     dd   dd         dd   dd     ","  dddd    d           d    dddd  ","dddddd   d    EJJJE    d   dddddd","dsddddd  d   FMMMMMF   d  dddddsd","ds dddddd   EMMMMMMME   dddddd sd","ds dddddd   JMMMMMMMJ   dddddd sd","ds dddddd   JMMMMMMMJ   dddddd sd","ds dddddd   JMMMMMMMJ   dddddd sd","ds dddddd   EMMMMMMME   dddddd sd","dsddddd  d   FMMMMMF   d  dddddsd","dddddd   d    EJJJE    d   dddddd","  dddd    d           d    dddd  ","     dd   dd         dd   dd     ","     dd     dd     dd     dd     ","      dd      ddddd      dd      ","       dd     ddddd     dd       ","        ddd  ddddddd  ddd        ","         ddddddddddddddd         ","           ddddddddddd           ","           ddddddddddd           ","           ddd     ddd           ","            dsssssssd            ","            ddddddddd            "},
        {"            BBBBBBBBB            ","          BBB       BBB          ","        BBBBBB     BBBBBB        ","      BB  BBBBBBBBBBBBB  BB      ","     B   BBBBBBBBBBBBBBB   B     ","    B   BBBBBBBBBBBBBBBBB   B    ","   B   BBBB  BBBBBBB  BBBB   B   ","   B  BBB     BBBBB     BBB  B   ","  B  BBB      BBBBB      BBB  B  ","  B BBB     BB     BB     BBB B  "," BBBBBB   BB         BB   BBBBBB "," BBBBB    B           B    BBBBB ","BBBBBB   B    EEEEE    B   BBBBBB","B BBBBB  B   EFFFFFE   B  BBBBB B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFMMMMMFE   BBBBBB  B","B  BBBBBB   EFFMMMFFE   BBBBBB  B","B BBBBB  B   EFFFFFE   B  BBBBB B","BBBBBB   B    EEEEE    B   BBBBBB"," BBBBB    B           B    BBBBB "," BBBBBB   BB         BB   BBBBBB ","  B BBB     BB     BB     BBB B  ","  B  BBB      BBBBB      BBB  B  ","   B  BBB     BBBBB     BBB  B   ","   B   BBBB  BBBBBBB  BBBB   B   ","    B   BBBBBBBBBBBBBBBBB   B    ","     B   BBBBBBBBBBBBBBB   B     ","      BB  BBBBBBBBBBBBB  BB      ","        BBBBBB     BBBBBB        ","          BBB       BBB          ","            BBBBBBBBB            "}
    };
    private final String[][] water = new String[][]{
        {"P"}
    };

    // Only Use for checkwater()
    private final String[][] StructureWater = new String[][]{
        {"ZZZZZZZZZPPPPPPPZZZZZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZZZZZPPPPPPPZZZZZZZZZ"},
        {"ZZZZZZZZZPPPPPPPZZZZZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZZZZZPPPPPPPZZZZZZZZZ"},
        {"ZZZZZZZZZPPPPPPPZZZZZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZZZZZPPPPPPPZZZZZZZZZ"},
        {"ZZZZZZZZZZPPPPPZZZZZZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","PPPPPPPPPPPPPPPPPPPPPPPPP","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZZZZZZPPPPPZZZZZZZZZZ"},
        {"ZZZZZZZZZZZZZZZZZZZZZZZZZ","ZZZZZZZZPPPPPPPPPZZZZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZZZZPPPPPPPPPZZZZZZZZ","ZZZZZZZZZZZZZZZZZZZZZZZZZ"},
        {"ZZZZZZZZZZZZZZZZZZZZZZZZZ","ZZZZZZZZZPPPPPPPZZZZZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZZZZZPPPPPPPZZZZZZZZZ","ZZZZZZZZZZZZZZZZZZZZZZZZZ"},
        {"ZZZZZZZZZZZZZZZZZZZZZZZZZ","ZZZZZZZZZZZPPPZZZZZZZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZPPPPPPPPPPPPPPPPPPPPPPPZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZZZPPPPPPPPPPPPPZZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZZZZZZZPPPZZZZZZZZZZZ","ZZZZZZZZZZZZZZZZZZZZZZZZZ"},
        {"ZZZZZZZZZZZZZZZZZZZZZZZZZ","ZZZZZZZZZZZZZZZZZZZZZZZZZ","ZZZZZZZZZPPPPPPPZZZZZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZPPPPPPPPPPPPPPPPPPPPPZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZPPPPPPPPPPPPPPPPPPPZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZPPPPPPPPPPPPPPPPPZZZZ","ZZZZZPPPPPPPPPPPPPPPZZZZZ","ZZZZZZZPPPPPPPPPPPZZZZZZZ","ZZZZZZZZZPPPPPPPZZZZZZZZZ","ZZZZZZZZZZZZZZZZZZZZZZZZZ","ZZZZZZZZZZZZZZZZZZZZZZZZZ"},
    };

    // spotless:on

    private void SetRemoveWater() {

        // checkType = true, check Water
        boolean checkType = machineMode != 0;
        if (checkType && checkWaterFinish) return;
        if (!checkType && checkAirFinish) return;
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        String[][] StructureDef = StructureWater;
        Block Air = Blocks.air;
        Block Water = BlocksItems.getFluidBlock(InternalName.fluidDistilledWater);
        boolean isFlipped = this.getFlip()
            .isHorizontallyFlipped();
        int OffSetX = 12;
        int OffSetY = 25;
        int OffSetZ = 3;
        if (checkType && !checkWaterFinish) {
            checkAirFinish = false;
            TstUtils
                .setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, isFlipped, "P", Water);
            checkWaterFinish = true;
        } else if (!checkType && !checkAirFinish) {
            checkWaterFinish = false;
            TstUtils
                .setStringBlockXZ(aBaseMetaTileEntity, OffSetX, OffSetY, OffSetZ, StructureDef, isFlipped, "P", Air);
            checkAirFinish = true;
        }

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
                if (inputItems == null) inputItems = new ItemStack[0];
                if (inputFluids == null) inputFluids = new FluidStack[0];

                SetRemoveWater();
                availableInputPower = availableVoltage * availableAmperage;
                EuTier = (int) Math.max(0, Math.log((double) availableInputPower / 8d) / Math.log(4d));
                updateSlots();
                if (EuTier < 1) return SimpleCheckRecipeResult.ofFailure("no_energy");
                if (cleaningRequested) {
                    cleaningRequested = false;
                    cleaningRunActive = true;
                    outputItems = new ItemStack[0];
                    outputFluids = new FluidStack[0];
                    calculatedEut = 0;
                    duration = isTierTwo() ? TIER_TWO_CLEANING_DURATION : TIER_ONE_CLEANING_DURATION;
                    return SimpleCheckRecipeResult.ofSuccess("mega_tree_farm_cleaning");
                }
                if (!modeSymbolPresent || boundMode < 0 || boundMode >= MACHINE_MODES.length) {
                    // #tr GT5U.gui.text.recipe_result.mega_tree_farm_waiting_for_mode_symbol
                    // # Waiting For Mode Symbol
                    // #zh_CN 等待模式信物
                    return SimpleCheckRecipeResult.ofFailure("mega_tree_farm_waiting_for_mode_symbol");
                }
                machineMode = boundMode;

                tierMultiplier = EcoSphereModeSupport.getTierMultiplier(EuTier);
                EcoSphereModeResult modeResult = MACHINE_MODES[machineMode]
                    .process(TST_MegaTreeFarm.this, EuTier);
                if (!modeResult.result()
                    .wasSuccessful()) return modeResult.result();
                outputItems = modeResult.outputs();
                outputFluids = modeResult.fluidOutputs();
                calculatedEut = modeResult.eut();
                duration = modeResult.duration();
                return modeResult.result();
            }
        };
    }

    public final CropsNHFarm cropsNHFarm = new CropsNHFarm();

    // #tr GT5U.gui.text.recipe_result.no_energy
    // # No power
    // #zh_CN 能源不足

    // #tr GT5U.gui.text.recipe_result.no_sapling
    // # Missing Sapling
    // #zh_CN 缺失树苗

    // #tr GT5U.gui.text.recipe_result.no_fluid
    // # Missing Fluid
    // #zh_CN 缺失流体

    // #tr GT5U.gui.text.recipe_result.no_enough_input
    // # No Enough Fluid
    // #zh_CN 输入流体不足

    // #tr GT5U.gui.text.recipe_result.no_correct_Circuit
    // # No Match Circuit
    // #zh_CN 没有匹配的电路板

    // #tr GT5U.gui.text.recipe_result.growing_trees
    // # {\GREEN}Growing Trees
    // #zh_CN {\GREEN}原木拟生中

    // #tr GT5U.gui.text.recipe_result.focus_on
    // # {\BLUE}Targeting
    // #zh_CN {\BLUE}定向中

    // #tr GT5U.gui.text.recipe_result.fishing
    // # {\BLUE}Fishing
    // #zh_CN {\BLUE}捕鱼中

    // #tr GT5U.gui.text.recipe_result.Invalid_Seed
    // # Invalid Seed
    // #zh_CN 无效种子

    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "tierMultiplier"
            + " : "
            + EnumChatFormatting.GOLD
            + (int) this.tierMultiplier;
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Eu tier" + " : " + EnumChatFormatting.GOLD + this.EuTier;
        ret[origin.length + 2] = EnumChatFormatting.AQUA + tr("MegaTreeFarm.gui.currentRecipe")
            + " : "
            + EnumChatFormatting.GOLD
            + getMachineModeName();
        return ret;
    }

    // private static class ESSFakePlayer extends FakePlayer {
    //
    // TST_EcoSphereSimulator mte;
    // ItemStack currentWeapon;
    //
    // public ESSFakePlayer(TST_EcoSphereSimulator mte) {
    // super(
    // (WorldServer) mte.getBaseMetaTileEntity()
    // .getWorld(),
    // new GameProfile(
    // UUID.nameUUIDFromBytes("[EEC Fake Player]".getBytes(StandardCharsets.UTF_8)),
    // "[EEC Fake Player]"));
    // this.mte = mte;
    // }
    //
    // @Override
    // public void renderBrokenItemStack(ItemStack p_70669_1_) {}
    //
    // @Override
    // public Random getRNG() {
    // return mte.rand;
    // }
    //
    // @Override
    // public void destroyCurrentEquippedItem() {}
    //
    // @Override
    // public ItemStack getCurrentEquippedItem() {
    // return currentWeapon;
    // }
    //
    // @Override
    // public ItemStack getHeldItem() {
    // return currentWeapon;
    // }
    // }
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_EcoSphereSimulator_MachineType
        // # Tree Farm | Aquatic Farm | Green House | Mob Cloner
        // #zh_CN 树厂 | 渔场 | 温室 | 生物克隆
        tt.addMachineType(tr("Tooltip_EcoSphereSimulator_MachineType"))
            // #tr Tooltip_EcoSphereSimulator_Controller
            // # Controller block for the Eco-Sphere Growth Simulator
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
            .beginStructureBlock(33, 45, 33, false)
            // .addStructureInfo(Text_SeparatingLine)
            .addInputHatch(textUseBlueprint, 1)
            .addOutputHatch(textUseBlueprint, 1)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 2)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(ModName);
        return tt;
    }

}
