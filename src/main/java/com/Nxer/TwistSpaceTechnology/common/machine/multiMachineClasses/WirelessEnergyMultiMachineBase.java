package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses;

import static com.Nxer.TwistSpaceTechnology.config.Config.DefaultCycleNum_WirelessEnergyMultiMachineBase;
import static com.Nxer.TwistSpaceTechnology.util.Utils.NEGATIVE_ONE;
import static com.Nxer.TwistSpaceTechnology.util.Utils.mergeArray;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class WirelessEnergyMultiMachineBase<T extends WirelessEnergyMultiMachineBase<T>>
    extends GTCM_MultiMachineBase<T> {

    public WirelessEnergyMultiMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public WirelessEnergyMultiMachineBase(String aName) {
        super(aName);
    }

    public static final String ZERO_STRING = "0";

    protected UUID ownerUUID;
    protected boolean isRecipeProcessing = false;
    protected boolean wirelessMode = getDefaultWirelessMode();
    protected BigInteger costingEU = BigInteger.ZERO;
    protected String costingEUText = ZERO_STRING;
    protected int cycleNum = DefaultCycleNum_WirelessEnergyMultiMachineBase;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("wirelessMode", wirelessMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        wirelessMode = aNBT.getBoolean("wirelessMode");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("wirelessMode")) {
            currentTip.add(EnumChatFormatting.LIGHT_PURPLE + TextLocalization.Waila_WirelessMode);
            currentTip.add(
                EnumChatFormatting.AQUA + TextLocalization.Waila_CurrentEuCost
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.GOLD
                    + tag.getString("costingEUText")
                    + EnumChatFormatting.RESET
                    + " EU");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("wirelessMode", wirelessMode);
            if (wirelessMode) tag.setString("costingEUText", costingEUText);
        }
    }

    @Override
    protected void startRecipeProcessing() {
        isRecipeProcessing = true;
        super.startRecipeProcessing();
    }

    @Override
    protected void endRecipeProcessing() {
        super.endRecipeProcessing();
        isRecipeProcessing = false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);

                return super.process();
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return wirelessMode ? OverclockCalculator.ofNoOverclock(recipe)
                    : super.createOverclockCalculator(recipe);
            }
        }.setMaxParallelSupplier(this::getLimitedMaxParallel);
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        costingEU = BigInteger.ZERO;
        costingEUText = ZERO_STRING;
        prepareProcessing();
        if (!wirelessMode) return super.checkProcessing();

        boolean succeeded = false;
        CheckRecipeResult finalResult = CheckRecipeResultRegistry.SUCCESSFUL;
        for (int i = 0; i < cycleNum; i++) {
            CheckRecipeResult r = wirelessModeProcessOnce();
            if (!r.wasSuccessful()) {
                finalResult = r;
                break;
            }
            succeeded = true;
        }

        updateSlots();
        if (!succeeded) return finalResult;
        costingEUText = GTUtility.formatNumbers(costingEU);

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = getWirelessModeProcessingTime();

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public CheckRecipeResult wirelessModeProcessOnce() {
        if (!isRecipeProcessing) startRecipeProcessing();
        setupProcessingLogic(processingLogic);
        setupWirelessProcessingPowerLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        if (!result.wasSuccessful()) {
            return result;
        }

        BigInteger costEU = BigInteger.valueOf(processingLogic.getCalculatedEut())
            .multiply(BigInteger.valueOf(processingLogic.getDuration()));

        int m = getExtraEUCostMultiplier();
        if (m > 1) {
            costEU = costEU.multiply(BigInteger.valueOf(m));
        }

        if (!addEUToGlobalEnergyMap(ownerUUID, costEU.multiply(NEGATIVE_ONE))) {
            return CheckRecipeResultRegistry.insufficientPower(costEU.longValue());
        }

        costingEU = costingEU.add(costEU);

        mOutputItems = mergeArray(mOutputItems, processingLogic.getOutputItems());
        mOutputFluids = mergeArray(mOutputFluids, processingLogic.getOutputFluids());

        endRecipeProcessing();
        return result;
    }

    protected void prepareProcessing() {}

    protected void setupWirelessProcessingPowerLogic(ProcessingLogic logic) {
        // wireless mode ignore voltage limit
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
    }

    public int getExtraEUCostMultiplier() {
        return 1;
    }

    public abstract int getWirelessModeProcessingTime();

    public boolean getDefaultWirelessMode() {
        return false;
    }

}
