package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2.BottledChaosHatchControls;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.material.BottledChaosMaterialCache;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTextLayout;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class TST_BottledChaosRecipeTokenHatch extends TST_BottledChaosAuthorizationHatch {

    private static final String EXECUTION_ENABLED_NBT = "bottledChaosExecutionEnabled";
    private static final String INFORMATION_NBT = "bottledChaosHatchInformation";
    private static final String MATERIAL_CACHE_NBT = "bottledChaosMaterialCache";

    private final BottledChaosMaterialCache materialCache = new BottledChaosMaterialCache();

    private boolean executionEnabled;
    private boolean clearRunningStateRequested;
    private MM_BottledChaos controller;
    private int processingGroup = -1;

    // #tr tst.bottledchaos.credential_hatch.tooltip.info.01
    // # Holds one Bottled Chaos credential for one processing group.
    // #zh_CN 为一个加工组容纳一个瓶中混沌凭证.

    // #tr tst.bottledchaos.credential_hatch.tooltip.info.02
    // # The parallel controller module determines how many credential hatches may be installed.
    // #zh_CN 并行控制器模块决定可安装的凭证仓数量.
    private static final String[] DESCRIPTION = { TSTUtils.tr("tst.bottledchaos.credential_hatch.tooltip.info.01"),
        TSTUtils.tr("tst.bottledchaos.credential_hatch.tooltip.info.02") };

    public TST_BottledChaosRecipeTokenHatch(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, DESCRIPTION);
    }

    public TST_BottledChaosRecipeTokenHatch(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, description, textures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TST_BottledChaosRecipeTokenHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected boolean isAuthorizationItem(ItemStack stack) {
        return ItemBottledChaosCredential.getCapacity(stack) > 0;
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (slot == 0 && controller != null
            && getBaseMetaTileEntity() != null
            && getBaseMetaTileEntity().isServerSide()) {
            controller.onCredentialHatchContentsChanged(this, processingGroup);
        }
    }

    public boolean isExecutionEnabled() {
        return executionEnabled;
    }

    public BottledChaosMaterialCache getMaterialCache() {
        return materialCache;
    }

    public void setExecutionEnabled(boolean enabled) {
        if (executionEnabled == enabled) return;
        setExecutionEnabledFromController(enabled);
        if (controller != null && getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isServerSide()) {
            controller.onCredentialHatchExecutionChanged(this, processingGroup, enabled);
        }
    }

    public void setExecutionEnabledFromController(boolean enabled) {
        if (executionEnabled == enabled) return;
        executionEnabled = enabled;
        if (getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isServerSide()) markDirty();
    }

    public void clearRunningState() {
        if (!isValid() || !getBaseMetaTileEntity().isServerSide()) return;
        materialCache.clearMaterials();
        clearRunningStateRequested = true;
        markDirty();
    }

    public boolean consumeClearRunningStateRequest() {
        if (!clearRunningStateRequested) return false;
        clearRunningStateRequested = false;
        return true;
    }

    public void setCacheLimit(long limit) {
        materialCache.setAmountLimit(limit);
        if (getBaseMetaTileEntity().isServerSide()) markDirty();
    }

    public void exportMaterialCache() {
        if (controller != null) controller.exportMaterialCache(this, processingGroup);
    }

    /** Called by the managed hatch action on the server; never drops an item if the inventory is full. */
    public void takeCredential(EntityPlayer player) {
        if (!isValid() || !getBaseMetaTileEntity().isServerSide()) return;
        ItemStack credential = getStackInSlot(0);
        if (ItemBottledChaosCredential.getCapacity(credential) == 0) return;
        if (player.inventory.getFirstEmptyStack() < 0) {
            // #tr tst.bottledchaos.credential_hatch.inventory_full
            // # No free inventory slot. The credential remains in its hatch.
            // #zh_CN 背包没有空位, 凭证保留在仓内.
            player.addChatMessage(new ChatComponentTranslation("tst.bottledchaos.credential_hatch.inventory_full"));
            return;
        }
        if (!player.inventory.addItemStackToInventory(credential.copy())) return;
        setInventorySlotContents(0, null);
        onContentsChanged(0);
        markDirty();
        player.inventory.markDirty();
    }

    public void bindController(MM_BottledChaos controller, int slot) {
        this.controller = controller;
        processingGroup = slot;
    }

    public void unbindController(MM_BottledChaos controller) {
        if (this.controller != controller) return;
        this.controller = null;
        processingGroup = -1;
    }

    public NBTTagCompound getInformation(boolean details) {
        return MM_BottledChaos.getCredentialHatchInformation(this, controller, processingGroup, details);
    }

    @SideOnly(Side.CLIENT)
    private static String getFittedHatchDisplayText(NBTTagCompound data) {
        return TSTTextLayout.ellipsize(
            MM_BottledChaos.getCredentialHatchTitle(data),
            140,
            TextRenderer.getFontRenderer()::getStringWidth);
    }

    @Override
    public void onRemoval() {
        unbindController(controller);
        super.onRemoval();
    }

    @Override
    public void onUnload() {
        unbindController(controller);
        super.onUnload();
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setTag(INFORMATION_NBT, getInformation(true));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        MM_BottledChaos.appendCredentialHatchWaila(
            currentTip,
            accessor.getNBTData()
                .getCompoundTag(INFORMATION_NBT),
            GuiScreen.isShiftKeyDown());
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setBoolean(EXECUTION_ENABLED_NBT, executionEnabled);
        nbt.setTag(MATERIAL_CACHE_NBT, materialCache.save());
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        executionEnabled = nbt.getBoolean(EXECUTION_ENABLED_NBT);
        materialCache.load(nbt.getCompoundTag(MATERIAL_CACHE_NBT));
    }

    @Override
    public void setItemNBT(NBTTagCompound nbt) {
        super.setItemNBT(nbt);
        nbt.setBoolean(EXECUTION_ENABLED_NBT, executionEnabled);
        nbt.setTag(MATERIAL_CACHE_NBT, materialCache.save());
    }

    @Override
    public void initDefaultModes(NBTTagCompound nbt) {
        super.initDefaultModes(nbt);
        if (nbt == null) return;
        executionEnabled = nbt.getBoolean(EXECUTION_ENABLED_NBT);
        if (nbt.hasKey(MATERIAL_CACHE_NBT, 10)) materialCache.load(nbt.getCompoundTag(MATERIAL_CACHE_NBT));
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        GenericSyncValue<NBTTagCompound, ?> information = GenericSyncValue.builder(NBTTagCompound.class)
            .getter(() -> getInformation(false))
            .adapter(ByteBufAdapters.NBT)
            .copy(tag -> (NBTTagCompound) tag.copy())
            .build();
        syncManager.syncValue("bottledChaosHatchInformation", information);
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, guiData, syncManager, uiSettings)
            .build();
        List<ButtonWidget<?>> controls = BottledChaosHatchControls.create(
            panel,
            syncManager,
            "bottledChaosHatch",
            () -> this,
            () -> information.getValue() != null && information.getValue()
                .getBoolean("enabled"),
            null,
            null,
            BottledChaosHatchControls.Action.EXECUTION,
            BottledChaosHatchControls.Action.LIMIT,
            BottledChaosHatchControls.Action.EXPORT,
            BottledChaosHatchControls.Action.CLEAR);
        int[] positions = { 36, 62, 88, 114 };
        for (int i = 0; i < controls.size(); i++) panel.child(
            controls.get(i)
                .pos(positions[i], 62));
        return panel.child(
            new ItemSlotGridBuilder(inventoryHandler, syncManager).size(1)
                .filter(this::isAuthorizationItem)
                .build()
                .pos(79, 34))
            .child(
                IKey.dynamic(() -> getFittedHatchDisplayText(information.getValue()))
                    .asWidget()
                    .color(Color.WHITE.main)
                    .textAlign(Alignment.Center)
                    .background(GTGuiTextures.BACKGROUND_TEXT_FIELD)
                    .tooltipBuilder(
                        tooltip -> tooltip.addLine(
                            information.getValue() != null && information.getValue()
                                .getBoolean("connected")
                                    ? MM_BottledChaos.getCredentialHatchTitle(information.getValue())
                                    : StatCollector.translateToLocal("tst.bottledchaos.credential_hatch.disconnected")))
                    .tooltipAutoUpdate(true)
                    .pos(14, 8)
                    .size(148, 18));
    }
}
