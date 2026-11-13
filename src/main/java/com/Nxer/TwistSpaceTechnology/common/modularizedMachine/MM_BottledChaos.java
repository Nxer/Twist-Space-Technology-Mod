package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosPrototypeStructure;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStandardStructure;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosStructure;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

/** One registered controller; each structure owns its layout, tiers and construction rules. */
@IMetaTileEntity.SkipGenerateDescription
public class MM_BottledChaos extends BottledChaosMachineBase<MM_BottledChaos> implements INEIPreviewModifier {

    // #tr tst.machine.BottledChaosPrototype.name
    // # Recipe Chain Validation Prototype
    // #zh_CN 配方链验证原型机
    public static final String PROTOTYPE_NAME_KEY = "tst.machine.BottledChaosPrototype.name";

    private static final String PROTOTYPE_NBT = "bottledChaosPrototype";
    private BottledChaosStructure structure;

    public MM_BottledChaos(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MM_BottledChaos(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MM_BottledChaos(mName);
    }

    public BottledChaosStructure getMachineStructure() {
        if (structure == null) structure = new BottledChaosStandardStructure(this);
        return structure;
    }

    public boolean isPrototype() {
        return getMachineStructure() instanceof BottledChaosPrototypeStructure;
    }

    public static boolean isPrototype(ItemStack stack) {
        return stack != null && stack.hasTagCompound()
            && stack.getTagCompound()
                .getBoolean(PROTOTYPE_NBT)
            && GTCMItemList.BottledChaos.hasBeenSet()
            && GTUtility.areStacksEqual(stack, GTCMItemList.BottledChaos.get(1), true);
    }

    private void selectStructure(boolean prototype) {
        if (prototype == isPrototype()) return;
        structure = prototype ? new BottledChaosPrototypeStructure(this) : new BottledChaosStandardStructure(this);
    }

    @Override
    public void onPreviewConstruct(ItemStack trigger) {
        // GuideNH imports controllers by meta ID; select the NBT variant only in its preview world.
        if (ChannelDataAccessor.hasSubChannel(trigger, BottledChaosPrototypeStructure.PREVIEW_CHANNEL)
            && getBaseMetaTileEntity() != null
            && GregTechAPI.isDummyWorld(getBaseMetaTileEntity().getWorld())) {
            selectStructure(true);
        }
    }

    public boolean convertToPrototype(EntityPlayer player) {
        if (isPrototype()) return false;
        if (hasPendingProcessing()) {
            // #tr tst.machine.BottledChaosPrototype.conversion_busy
            // # Finish or clear all processing groups before converting the controller.
            // #zh_CN 转换前请完成或清空全部加工组任务.
            GTUtility.sendChatToPlayer(player, TSTUtils.tr("tst.machine.BottledChaosPrototype.conversion_busy"));
            return false;
        }
        stopMachine(ShutDownReasonRegistry.NONE);
        selectStructure(true);
        mMachine = false;
        onMachineBlockUpdate();
        getBaseMetaTileEntity().issueTileUpdate();
        markDirty();
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity base, EntityPlayer player) {
        ItemStack held = player.getHeldItem();
        if (GTUtility.areStacksEqual(held, GTCMItemList.BottledChaosEarlyAccess.get(1), true)) {
            if (base.isServerSide() && base.isUseableByPlayer(player)
                && convertToPrototype(player)
                && !player.capabilities.isCreativeMode) {
                held.stackSize--;
            }
            return true;
        }
        return super.onRightclick(base, player);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setBoolean(PROTOTYPE_NBT, isPrototype());
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        selectStructure(nbt.getBoolean(PROTOTYPE_NBT));
        super.loadNBTData(nbt);
    }

    @Override
    public void initDefaultModes(NBTTagCompound nbt) {
        super.initDefaultModes(nbt);
        selectStructure(nbt != null && nbt.getBoolean(PROTOTYPE_NBT));
    }

    @Override
    public void setItemNBT(NBTTagCompound nbt) {
        super.setItemNBT(nbt);
        if (!isPrototype()) return;
        nbt.setBoolean(PROTOTYPE_NBT, true);
    }

    @Override
    public ItemStack getStackForm(long amount) {
        ItemStack stack = super.getStackForm(amount);
        if (isPrototype()) {
            NBTTagCompound nbt = new NBTTagCompound();
            setItemNBT(nbt);
            stack.setTagCompound(nbt);
        }
        return stack;
    }

    @Override
    public byte getUpdateData() {
        return (byte) (isPrototype() ? 1 : 0);
    }

    @Override
    public void onValueUpdate(byte value) {
        selectStructure((value & 1) != 0);
    }

    @Override
    public String getLocalNameKey() {
        return isPrototype() ? PROTOTYPE_NAME_KEY : super.getLocalNameKey();
    }

    @Override
    public String getInventoryName() {
        return getLocalNameKey();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        // GT caches this by MetaID; the item-specific hook below supplies the prototype description.
        return new BottledChaosStandardStructure(this).getTooltip();
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);
        if (!isPrototype(stack)) return;
        if (!stack.hasDisplayName() && !tooltip.isEmpty()) {
            String name = stack.getDisplayName();
            String title = tooltip.get(0);
            if (title.startsWith(name)) {
                tooltip.set(0, TSTUtils.tr(PROTOTYPE_NAME_KEY) + title.substring(name.length()));
            }
        }
        int descriptionLength = getDescription().length;
        tooltip.subList(tooltip.size() - descriptionLength, tooltip.size())
            .clear();
        MultiblockTooltipBuilder prototype = new BottledChaosPrototypeStructure(this).getTooltip();
        Collections.addAll(
            tooltip,
            isDisplaySecondaryDescription() ? prototype.getStructureInformation() : prototype.getInformation());
    }

    @Override
    public String[] getStructureDescription(ItemStack stack) {
        return getMachineStructure().getTooltip()
            .getStructureHint();
    }

    @Override
    public boolean checkMachineMM(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        getMachineStructure().reset();
        return super.checkMachineMM(base, stack, errors);
    }

    @Override
    protected boolean checkStructurePieces(List<StructureError> errors) {
        return getMachineStructure().checkStructurePieces(errors);
    }

    @Override
    protected int getProcessingGroupSlot(int left, int back) {
        return getMachineStructure().getProcessingGroupSlot(left, back);
    }

    @Override
    public int getProcessingGroupLimit() {
        return getMachineStructure().getProcessingGroupLimit();
    }

    @Override
    protected int getCredentialHatchLimit(int parallelTier) {
        return getMachineStructure().getCredentialHatchLimit(parallelTier);
    }

    @Override
    protected StructureError getInvalidCapacityRegionError() {
        return getMachineStructure().getInvalidCapacityRegionError();
    }

    @Override
    protected int getCapacityRegionCount() {
        return getMachineStructure().getCapacityRegionCount();
    }

    @Override
    protected int getCapacityRegionBlockCount() {
        return getMachineStructure().getCapacityRegionBlockCount();
    }

    @Override
    protected int getCapacityLevelCount() {
        return getMachineStructure().getCapacityLevelCount();
    }

    @Override
    protected int getCapacityRegion(int x, int y, int z) {
        return getMachineStructure().getCapacityRegion(x, y, z);
    }

    @Override
    public boolean isWirelessMode() {
        return getMachineStructure().isWirelessMode();
    }

    @Override
    public long getMaximumRecipeVoltage() {
        return getMachineStructure().getMaximumRecipeVoltage();
    }

    @Override
    public IStructureDefinition<MM_BottledChaos> getStructureDefinition() {
        return getMachineStructure().getStructureDefinition();
    }

    @Override
    public void construct(ItemStack stack, boolean hintsOnly) {
        getMachineStructure().construct(stack, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int budget, ISurvivalBuildEnvironment environment) {
        return getMachineStructure().survivalConstruct(stack, budget, environment);
    }

    public boolean checkStructurePiece(String piece, int x, int y, int z, List<StructureError> errors) {
        return checkPiece(piece, x, y, z, errors);
    }

    public void buildStructurePiece(String piece, ItemStack stack, boolean hintsOnly, int x, int y, int z) {
        buildPiece(piece, stack, hintsOnly, x, y, z);
    }

    public int survivalBuildStructurePiece(String piece, ItemStack stack, int x, int y, int z, int budget,
        ISurvivalBuildEnvironment environment, boolean checkPlaced, boolean checkMachine) {
        return survivalBuildPiece(piece, stack, x, y, z, budget, environment, checkPlaced, checkMachine);
    }
}
