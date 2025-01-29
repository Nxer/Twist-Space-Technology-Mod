package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines;

import static com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EnableRenderDefaultArtificialStar;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.secondsOfArtificialStarProgressCycleTime;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.DSPName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_00;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_02_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_02_02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_02_03;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_02_04;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_02_05;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_02_06;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_03;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_04;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_05;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_06;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_07;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_08;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_Controller;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_ArtificialStar_MachineType;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_00;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_03;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_04;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_05;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_06;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_launch_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DSPInfo_launch_02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_Details;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textUseBlueprint;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static com.gtnewhorizons.gtnhintergalactic.block.IGBlocks.SpaceElevatorCasing;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static tectech.thing.casing.TTCasingsContainer.SpacetimeCompressionFieldGenerators;
import static tectech.thing.casing.TTCasingsContainer.StabilisationFieldGenerators;
import static tectech.thing.casing.TTCasingsContainer.TimeAccelerationFieldGenerator;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

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

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import galaxyspace.core.register.GSBlocks;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.block.BlockQuantumGlass;
import tectech.thing.casing.TTCasingsContainer;

public class TST_ArtificialStar extends GTCM_MultiMachineBase<TST_ArtificialStar>
    implements IWirelessEnergyHatchInformation {

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

    // region Statics
    protected static TST_ItemID Antimatter;
    protected static TST_ItemID AntimatterFuelRod;
    protected static TST_ItemID StrangeAnnihilationFuelRod;
    protected static long MaxOfAntimatter;
    protected static long MaxOfAntimatterFuelRod;
    protected static long MaxOfStrangeAnnihilationFuelRod;

    public static void initStatics() {
        Antimatter = TST_ItemID.createNoNBT(GTCMItemList.Antimatter.get(1));
        AntimatterFuelRod = TST_ItemID.createNoNBT(GTCMItemList.AntimatterFuelRod.get(1));
        StrangeAnnihilationFuelRod = TST_ItemID.createNoNBT(GTCMItemList.StrangeAnnihilationFuelRod.get(1));
        MaxOfAntimatter = Config.EUEveryAntimatter / Integer.MAX_VALUE;
        MaxOfAntimatterFuelRod = Config.EUEveryAntimatterFuelRod / Integer.MAX_VALUE;
        MaxOfStrangeAnnihilationFuelRod = Config.EUEveryStrangeAnnihilationFuelRod / Integer.MAX_VALUE;
    }

    // endregion

    // region Processing Logic
    private String ownerName;
    private UUID ownerUUID;
    private long storageEU = 0;
    private int tierDimensionField = -1;
    private int tierTimeField = -1;
    private int tierStabilisationField = -1;
    private double outputMultiplier = 1;
    private short recoveryChance = 0;
    private byte rewardContinuous = 0;
    private long currentOutputEU = 0;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.0");
    private boolean isRendering = false;
    private byte enableRender = EnableRenderDefaultArtificialStar;

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("isActive")) {
            currentTip.add(EnumChatFormatting.AQUA +
            // #tr Waila.TST_ArtificialStar.1
            // # Current Generating :
            // #zh_CN 当前发电 :
                TextEnums.tr("Waila.TST_ArtificialStar.1")
                + EnumChatFormatting.GOLD
                + tag.getLong("currentOutputEU")
                + EnumChatFormatting.RED
                + " * "
                + decimalFormat.format(tag.getDouble("outputMultiplier"))
                + EnumChatFormatting.GREEN
                + " * 2147483647"
                + EnumChatFormatting.RESET
                + " EU / "
                + secondsOfArtificialStarProgressCycleTime
                + " s");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            if (tileEntity.isActive()) {
                tag.setLong("currentOutputEU", currentOutputEU);
                tag.setDouble("outputMultiplier", (outputMultiplier * (rewardContinuous + 100) / 100));
            }
        }
    }

    @Override
    public String[] getInfoData() {
        // spotless:off
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 6];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.00
            // # Reward for continuous operation
            // #zh_CN 连续运行奖励
            TextEnums.tr("TST_ArtificialStar.getInfoData.00")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.GREEN+(rewardContinuous+100)+"%";

        ret[origin.length + 1] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.01
            // # Generating Multiplier
            // #zh_CN 发电倍率
            TextEnums.tr("TST_ArtificialStar.getInfoData.01")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.GREEN+outputMultiplier;

        ret[origin.length + 2] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.02
            // # Dimension Field Tier
            // #zh_CN 空间场等级
            TextEnums.tr("TST_ArtificialStar.getInfoData.02")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.YELLOW+tierDimensionField;

        ret[origin.length + 3] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.03
            // # Time Field Tier
            // #zh_CN 时间场等级
            TextEnums.tr("TST_ArtificialStar.getInfoData.03")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.YELLOW+tierTimeField;

        ret[origin.length + 4] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.04
            // # Stabilisation Field Tier
            // #zh_CN 稳定场等级
            TextEnums.tr("TST_ArtificialStar.getInfoData.04")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.YELLOW+tierStabilisationField;

        ret[origin.length + 5] = EnumChatFormatting.GOLD+
            // #tr TST_ArtificialStar.getInfoData.05
            // # Recover material chance
            // #zh_CN 回收原料几率
            TextEnums.tr("TST_ArtificialStar.getInfoData.05")
            +EnumChatFormatting.RESET+": "+EnumChatFormatting.AQUA+recoveryChance+EnumChatFormatting.RESET+"/"+EnumChatFormatting.AQUA+"1000";
        return ret;
       // spotless:on
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.enableRender = (byte) ((this.enableRender + 1) % 2);
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("ArtificialStar.enableRender." + this.enableRender));
            if (enableRender == 0 && isRendering) {
                destroyRenderBlock();
                isRendering = false;
            }
        }
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // iterate input bus slot
        // consume fuel and generate EU
        boolean flag = false;
        long recoveryAmount = 0;
        // * Integer.MAX_VALUE
        currentOutputEU = 0;
        for (ItemStack items : getStoredInputs()) {
            if (Antimatter.equalItemStack(items)) {
                currentOutputEU += MaxOfAntimatter * items.stackSize;
                flag = true;
            } else if (AntimatterFuelRod.equalItemStack(items)) {
                currentOutputEU += MaxOfAntimatterFuelRod * items.stackSize;
                recoveryAmount += items.stackSize;
                flag = true;
            } else if (StrangeAnnihilationFuelRod.equalItemStack(items)) {
                currentOutputEU += MaxOfStrangeAnnihilationFuelRod * items.stackSize;
                recoveryAmount += items.stackSize;
                flag = true;
            }
            // whether the item is fuel
            // void it
            items.stackSize = 0;
        }

        // flush input slots
        updateSlots();

        // if no antimatter or fuel rod input
        if (!flag) {
            // set 0 to multiplier of rewarding continuous operation
            rewardContinuous = 0;
            // stop render
            if (isRendering) {
                destroyRenderBlock();
                isRendering = false;
            }
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // add EU to the wireless EU net
        BigInteger eu = BigInteger
            .valueOf((long) (currentOutputEU * outputMultiplier * ((rewardContinuous + 100d) / 100d)))
            .multiply(Utils.INTEGER_MAX_VALUE);
        if (!addEUToGlobalEnergyMap(ownerUUID, eu)) {
            return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        // set progress time with cfg
        mMaxProgresstime = (int) (20 * secondsOfArtificialStarProgressCycleTime);
        // chance to recover FrameMaterial
        if (recoveryChance == 1000) {
            if (recoveryAmount > 0) {
                mOutputItems = getRecovers(recoveryAmount);
            }
        } else if (XSTR.XSTR_INSTANCE.nextInt(1000) < recoveryChance) {
            if (recoveryAmount > 0) {
                mOutputItems = getRecovers(recoveryAmount);
            }
        }

        // increase multiplier of rewarding continuous operation
        if (rewardContinuous < 50) rewardContinuous++;

        // start render
        if (enableRender != 0 && !isRendering) {
            createRenderBlock();
            isRendering = true;
        }
        return CheckRecipeResultRegistry.GENERATING;
    }

    protected ItemStack[] getRecovers(long amount) {
        if (amount <= Integer.MAX_VALUE) {
            return new ItemStack[] { StellarConstructionFrameMaterial.get((int) amount) };
        } else {
            int stack = (int) (amount / Integer.MAX_VALUE);
            int remainder = (int) (amount % Integer.MAX_VALUE);
            ItemStack[] r = new ItemStack[remainder > 0 ? stack + 1 : stack];
            ItemStack t = StellarConstructionFrameMaterial.get(Integer.MAX_VALUE);
            for (int i = 0; i < stack; i++) {
                r[i] = t.copy();
            }
            if (remainder > 0) r[stack] = copyAmount(remainder, t);
            return r;
        }
    }

    // Artificial Star Output multiplier
    private void calculateOutputMultiplier() {
        // tTime^0.25 * tDim^0.25 * 1.588186^(tStabilisation-2)
        // (100^0.25)*(1.588186^(10-2))) = 128.000
        // 1.588186^(-1) = 0.629
        this.outputMultiplier = Math.pow(1d * tierTimeField * tierDimensionField, 0.25d)
            * Math.pow(1.588186d, tierStabilisationField - 2);
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (isRendering && mMaxProgresstime == 0 && rewardContinuous == 0) {
            isRendering = false;
            destroyRenderBlock();
        }
        if (rewardContinuous != 0 && mMaxProgresstime == 0) rewardContinuous = 0;
    }

    @Override
    public void onBlockDestroyed() {
        if (isRendering) {
            isRendering = false;
            destroyRenderBlock();
        }
        super.onBlockDestroyed();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("storageEU", storageEU);
        aNBT.setInteger("tierDimensionField", tierDimensionField);
        aNBT.setInteger("tierTimeField", tierTimeField);
        aNBT.setInteger("tierStabilisationField", tierStabilisationField);
        aNBT.setDouble("outputMultiplier", outputMultiplier);
        aNBT.setByte("rewardContinuous", rewardContinuous);
        aNBT.setLong("currentOutputEU", currentOutputEU);
        aNBT.setBoolean("isRendering", isRendering);
        aNBT.setByte("enableRender", enableRender);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        storageEU = aNBT.getLong("storageEU");
        tierDimensionField = aNBT.getInteger("tierDimensionField");
        tierTimeField = aNBT.getInteger("tierTimeField");
        tierStabilisationField = aNBT.getInteger("tierStabilisationField");
        outputMultiplier = aNBT.getDouble("outputMultiplier");
        rewardContinuous = aNBT.getByte("rewardContinuous");
        currentOutputEU = aNBT.getLong("currentOutputEU");
        isRendering = aNBT.getBoolean("isRendering");
        enableRender = aNBT.getByte("enableRender");
    }

    // endregion

    // region Structure
    // spotless:off
    // disable crafting input bus/buffer
    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        mInputBusses.clear();
        tierDimensionField = -1;
        tierTimeField = -1;
        tierStabilisationField = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (tierDimensionField < 0 || tierTimeField < 0 || tierStabilisationField < 0) return false;
        // Only allow and must be 1 input bus
        if (this.mInputBusses.size() != 1) return false;
        calculateOutputMultiplier();
        recoveryChance = (short) (tierDimensionField * tierTimeField * tierStabilisationField);
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
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

    private static final String STRUCTURE_PIECE_MAIN = "mainArtificialStar";
    private final int horizontalOffSet = 18;
    private final int verticalOffSet = 39;
    private final int depthOffSet = 15;


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
    @Override
    public IStructureDefinition<TST_ArtificialStar> getStructureDefinition() {
        return IStructureDefinition.<TST_ArtificialStar>builder()
                   .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                   .addElement(
                       'A',
                       withChannel("tierdimensionfield",
                                   ofBlocksTiered(
                                       TST_ArtificialStar::getTierDimensionFieldBlockFromBlock,
                                       ImmutableList.of(
                                           Pair.of(GregTechAPI.sBlockCasings1, 14),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 0),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 1),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 2),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 3),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 4),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 5),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 6),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 7),
                                           Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 8)),
                                       -1,
                                       (t,m) -> t.tierDimensionField = m,
                                       t -> t.tierDimensionField
                                   )))
                   .addElement('B', ofBlock(SpaceElevatorCasing, 2))
                   .addElement('C', ofBlock(sBlockCasingsTT, 4))
                   .addElement(
                       'D',
                       withChannel("tiertimefield",
                                   ofBlocksTiered(
                                       TST_ArtificialStar::getTierTimeFieldBlockFromBlock,
                                       ImmutableList.of(
                                           Pair.of(sBlockCasingsTT, 14),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 0),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 1),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 2),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 3),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 4),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 5),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 6),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 7),
                                           Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)),
                                       -1,
                                       (t,m) -> t.tierTimeField = m,
                                       t -> t.tierTimeField)))
                   .addElement('E', ofBlock(sBlockCasingsTT, 7))
                   .addElement('F', ofBlock(sBlockCasingsTT, 8))
                   .addElement(
                       'G',
                       withChannel("tierstabilisationfield",
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
                   .addElement('K', ofBlock(BlockQuantumGlass.INSTANCE, 0))
                   .addElement('L',
                               HatchElementBuilder.<TST_ArtificialStar>builder()
                                   .atLeast(InputBus, OutputBus)
                                   .adder(TST_ArtificialStar::addInputBusOrOutputBusToMachineList)
                                   .dot(1)
                                   .casingIndex(1024+12)
                                   .buildAndChain(sBlockCasingsTT, 12))
                   .build();
    }
    public static int getTierDimensionFieldBlockFromBlock(Block block, int meta){
        if (block == GregTechAPI.sBlockCasings1 && 14 == meta) return 1;
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
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(Tooltip_ArtificialStar_MachineType)
            .addInfo(Tooltip_ArtificialStar_Controller)
            .addInfo(Tooltip_ArtificialStar_00)
            .addInfo(Tooltip_ArtificialStar_01)
            .addInfo(Tooltip_ArtificialStar_02)
            .addInfo(Tooltip_ArtificialStar_03)
            .addInfo(Tooltip_ArtificialStar_04)
            .addInfo(Tooltip_ArtificialStar_05)
            .addInfo(Tooltip_ArtificialStar_06)
            .addInfo(Tooltip_ArtificialStar_07)
            .addInfo(Tooltip_ArtificialStar_08)
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addSeparator()
            .addStructureInfo(Tooltip_Details)
            .addStructureInfo(Tooltip_ArtificialStar_02_01)
            .addStructureInfo(Tooltip_ArtificialStar_02_02)
            .addStructureInfo(Tooltip_ArtificialStar_02_03)
            .addStructureInfo(Tooltip_ArtificialStar_02_04)
            .addStructureInfo(Tooltip_ArtificialStar_02_05)
            .addStructureInfo(Tooltip_ArtificialStar_02_06)
          .addStructureInfo(EnumChatFormatting.GOLD+"-----------------------------------------")
          .addStructureInfo(DSPName + ":")
          .addStructureInfo(Tooltip_DSPInfo_launch_01)
          .addStructureInfo(Tooltip_DSPInfo_launch_02)
          .addStructureInfo(Tooltip_DSPInfo_00)
          .addStructureInfo(Tooltip_DSPInfo_01)
          .addStructureInfo(Tooltip_DSPInfo_02)
          .addStructureInfo(Tooltip_DSPInfo_03)
          .addStructureInfo(Tooltip_DSPInfo_04)
          .addStructureInfo(Tooltip_DSPInfo_05)
          .addStructureInfo(Tooltip_DSPInfo_06)
          .addStructureInfo(EnumChatFormatting.GOLD+"-----------------------------------------")
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
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
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
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

    public void createRenderBlock() {
        int x = getBaseMetaTileEntity().getXCoord();
        int y = getBaseMetaTileEntity().getYCoord();
        int z = getBaseMetaTileEntity().getZCoord();

        double xOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetX;
        double zOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetZ;
        double yOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), TstBlocks.BlockStar);
        /*
         * This wouldn't work since there are no Network System. Todo: Add a message to SERVER.
         * if (getBaseMetaTileEntity().getWorld()
         * .getTileEntity((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset)) instanceof TileStar star)
         * star.size = Math.min(currentOutputEU, 1024) / 204.8;
         */
    }

    public void destroyRenderBlock() {
        int x = getBaseMetaTileEntity().getXCoord();
        int y = getBaseMetaTileEntity().getYCoord();
        int z = getBaseMetaTileEntity().getZCoord();

        double xOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetX;
        double zOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetZ;
        double yOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY
            + 26 * getExtendedFacing().getRelativeUpInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
    }
}
