package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMaterialCache;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
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
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;

@IMetaTileEntity.SkipGenerateDescription
public class TST_BottledChaosRecipeTokenHatch extends TST_BottledChaosAuthorizationHatch {

    private static final String EXECUTION_ENABLED_NBT = "bottledChaosExecutionEnabled";
    private static final String WAILA_THREAD_NBT = "bottledChaosCredentialThread";
    private static final String WAILA_CACHE_NBT = "bottledChaosCachedMaterials";
    private static final String MATERIAL_CACHE_NBT = "bottledChaosMaterialCache";

    private final BottledChaosMaterialCache materialCache = new BottledChaosMaterialCache();

    private boolean executionEnabled;
    private boolean clearRunningStateRequested;
    private MM_BottledChaos controller;
    private int processingGroup = -1;
    private int displayedHatchNumber;
    private int displayedInputPeerMask;

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

    public boolean isExecutionEnabled() {
        return executionEnabled;
    }

    public BottledChaosMaterialCache getMaterialCache() {
        return materialCache;
    }

    private void setExecutionEnabled(boolean enabled) {
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

    public boolean consumeClearRunningStateRequest() {
        if (!clearRunningStateRequested) return false;
        clearRunningStateRequested = false;
        return true;
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

    private int getControllerHatchNumber() {
        return controller == null ? 0 : controller.getCredentialHatchNumber(this, processingGroup);
    }

    private int getControllerInputPeerMask() {
        return controller == null ? 0 : controller.getCredentialHatchInputPeerMask(this, processingGroup);
    }

    @SideOnly(Side.CLIENT)
    private static String getBoundInputText(int peerMask) {
        StringJoiner peers = new StringJoiner("");
        for (int slot = 0; slot < 15; slot++) {
            if ((peerMask & (1 << slot)) != 0) peers.add("#" + (slot + 1));
        }
        // #tr tst.bottledchaos.credential_hatch.bound_inputs
        // # Bound to %s
        // #zh_CN 与%s绑定
        return StatCollector
            .translateToLocalFormatted("tst.bottledchaos.credential_hatch.bound_inputs", peers.toString());
    }

    @SideOnly(Side.CLIENT)
    private String getHatchDisplayText() {
        if (displayedHatchNumber <= 0) return "#-";
        String number = "#" + displayedHatchNumber;
        return displayedInputPeerMask == 0 ? number : number + " | " + getBoundInputText(displayedInputPeerMask);
    }

    @SideOnly(Side.CLIENT)
    private String getFittedHatchDisplayText() {
        String text = getHatchDisplayText();
        FontRenderer fontRenderer = TextRenderer.getFontRenderer();
        if (fontRenderer.getStringWidth(text) <= 140) return text;
        return fontRenderer.trimStringToWidth(text, 140 - fontRenderer.getStringWidth("...")) + "...";
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
        tag.removeTag(WAILA_THREAD_NBT);
        tag.setTag(WAILA_CACHE_NBT, materialCache.getWailaSummary());
        if (controller == null) return;
        NBTTagCompound job = controller.getCredentialHatchWailaData(this, processingGroup);
        if (job == null) {
            unbindController(controller);
            return;
        }
        tag.setTag(WAILA_THREAD_NBT, job);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        boolean showCache = GuiScreen.isShiftKeyDown();
        NBTTagCompound job = tag.getCompoundTag(WAILA_THREAD_NBT);
        int duration = job.getInteger("duration");
        if (!tag.hasKey(WAILA_THREAD_NBT, 10)) {
            // #tr tst.bottledchaos.credential_hatch.disconnected
            // # Not connected to a formed Bottled Chaos controller.
            // #zh_CN 未连接到成型的瓶中混沌主机.
            currentTip.add(StatCollector.translateToLocal("tst.bottledchaos.credential_hatch.disconnected"));
            if (!showCache) return;
        } else {
            currentTip.add(
                "#" + job.getInteger("slot")
                    + " "
                    + MM_BottledChaos.getStatusColor(job.getString("status"))
                    + MM_BottledChaos.getWailaStatusText(job)
                    + EnumChatFormatting.RESET);
            int boundSlots = job.getInteger("boundSlots");
            if (boundSlots != 0) currentTip.add(getBoundInputText(boundSlots));
            long eut = job.getLong("eut");
            double inputPower = job.getDouble("inputPower");
            byte energyTier = (byte) job.getLong("energyTier");
            // #tr tst.bottledchaos.credential_hatch.power_share
            // # Total usable power share: %s%%
            // #zh_CN 占总可用功率: %s%%
            currentTip.add(
                (energyTier > 0
                    ? StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.use_with_amperage",
                        formatNumber(eut),
                        GTUtility.getAmperageForTier(eut, energyTier),
                        GTUtility.getColoredTierNameFromTier(energyTier))
                    : StatCollector.translateToLocalFormatted(
                        "GT5U.waila.energy.use",
                        formatNumber(eut),
                        GTUtility.getColoredTierNameFromVoltage(eut)))
                    + " | "
                    + StatCollector.translateToLocalFormatted(
                        "tst.bottledchaos.credential_hatch.power_share",
                        String.format(
                            Locale.ROOT,
                            "%.2f",
                            inputPower > 0 ? Math.min(100, 100.0 * eut / inputPower) : 0)));
            if (duration > 0) {
                currentTip.add(GTWaila.getMachineProgressString(duration, job.getInteger("progress")));
            }
        }
        NBTTagCompound outputs = showCache ? tag.getCompoundTag(WAILA_CACHE_NBT) : job.getCompoundTag("outputs");
        int total = outputs.getInteger("total");
        if (total <= 0 && !showCache) return;
        // #tr tst.bottledchaos.credential_hatch.expected_outputs
        // # Expected outputs (%s types):
        // #zh_CN 预计产物(%s种):

        // #tr tst.bottledchaos.credential_hatch.pending_outputs
        // # Pending outputs (%s types):
        // #zh_CN 待输出产物(%s种):

        // #tr tst.bottledchaos.credential_hatch.cached_materials
        // # Cached materials (%s types):
        // #zh_CN 缓存材料(%s种):
        currentTip.add(
            StatCollector.translateToLocalFormatted(
                showCache ? "tst.bottledchaos.credential_hatch.cached_materials"
                    : duration > 0 && job.getInteger("progress") >= duration
                        ? "tst.bottledchaos.credential_hatch.pending_outputs"
                        : "tst.bottledchaos.credential_hatch.expected_outputs",
                formatNumber(total)));
        NBTTagList entries = outputs.getTagList("entries", 10);
        int shown = 0;
        for (int i = 0; i < Math.min(10, entries.tagCount()); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            ItemStack output = entry.hasKey("item", 10) ? ItemStack.loadItemStackFromNBT(entry.getCompoundTag("item"))
                : null;
            FluidStack fluid = entry.hasKey("fluid", 10)
                ? FluidStack.loadFluidStackFromNBT(entry.getCompoundTag("fluid"))
                : null;
            if (output == null && fluid == null) continue;
            currentTip.add(
                "  " + TTRenderStack
                    .create(output != null ? output : GTUtility.getFluidDisplayStack(fluid, false), true)
                    + EnumChatFormatting.AQUA
                    + (output != null ? output.getDisplayName() : fluid.getLocalizedName())
                    + EnumChatFormatting.RESET
                    + " x "
                    + EnumChatFormatting.GOLD
                    + formatNumber(entry.getLong("amount"))
                    + (fluid == null ? "" : "L"));
            shown++;
        }
        if (total > shown) {
            currentTip.add(
                StatCollector.translateToLocalFormatted("GT5U.waila.producing.andmore", formatNumber(total - shown)));
        }
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
        materialCache.load(nbt.getCompoundTag(MATERIAL_CACHE_NBT));
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.syncValue(
            "bottledChaosHatchNumber",
            new IntSyncValue(this::getControllerHatchNumber, value -> displayedHatchNumber = value));
        syncManager.syncValue(
            "bottledChaosInputPeerMask",
            new IntSyncValue(this::getControllerInputPeerMask, value -> displayedInputPeerMask = value));
        BooleanSyncValue execution = new BooleanSyncValue(this::isExecutionEnabled);
        syncManager.syncValue("bottledChaosExecutionEnabled", execution);
        InteractionSyncHandler executionAction = new InteractionSyncHandler().setOnMousePressed(
            mouse -> { if (mouse.mouseButton == 0 && !mouse.isClient()) setExecutionEnabled(!executionEnabled); });
        syncManager.syncValue("bottledChaosToggleExecution", executionAction);
        // #tr tst.bottledchaos.credential_hatch.pause
        // # Pause this processing group.
        // #zh_CN 暂停此加工组.

        // #tr tst.bottledchaos.credential_hatch.resume
        // # Resume this processing group.
        // #zh_CN 恢复此加工组运行.

        // #tr tst.bottledchaos.credential_hatch.clear
        // # Clear this processing group's current job and cached materials.
        // #zh_CN 清空此加工组的当前运行状态和缓存材料.
        ButtonWidget<?> executionButton = new ButtonWidget<>().syncHandler(executionAction)
            .onMousePressed(mouseButton -> mouseButton == 0 && executionAction.onMousePressed(mouseButton))
            .playClickSound(true)
            .background(
                new DynamicDrawable(
                    () -> execution.getBoolValue() ? GTGuiTextures.BUTTON_STANDARD
                        : GTGuiTextures.BUTTON_STANDARD_PRESSED))
            .overlay(
                new DynamicDrawable(
                    () -> execution.getBoolValue() ? GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON
                        : GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF))
            .tooltipBuilder(
                tooltip -> tooltip.addLine(
                    IKey.lang(
                        execution.getBoolValue() ? "tst.bottledchaos.credential_hatch.pause"
                            : "tst.bottledchaos.credential_hatch.resume")))
            .tooltipAutoUpdate(true)
            .pos(52, 62)
            .size(18, 18);
        long[] clearPressedUntil = { 0 };
        InteractionSyncHandler clearAction = new InteractionSyncHandler().setOnMousePressed(mouse -> {
            if (mouse.mouseButton != 0) return;
            clearPressedUntil[0] = System.currentTimeMillis() + 120;
            if (!mouse.isClient()) {
                materialCache.clearMaterials();
                clearRunningStateRequested = true;
                markDirty();
            }
        });
        syncManager.syncValue("bottledChaosClearRunningState", clearAction);
        ButtonWidget<?> clearButton = new ButtonWidget<>().syncHandler(clearAction)
            .onMousePressed(mouseButton -> mouseButton == 0 && clearAction.onMousePressed(mouseButton))
            .playClickSound(true)
            .background(
                new DynamicDrawable(
                    () -> System.currentTimeMillis() < clearPressedUntil[0] ? GTGuiTextures.BUTTON_STANDARD_PRESSED
                        : GTGuiTextures.BUTTON_STANDARD))
            .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
            .tooltipBuilder(tooltip -> tooltip.addLine(IKey.lang("tst.bottledchaos.credential_hatch.clear")))
            .pos(106, 62)
            .size(18, 18);
        return GTGuis.mteTemplatePanelBuilder(this, guiData, syncManager, uiSettings)
            .build()
            .child(
                new ItemSlotGridBuilder(inventoryHandler, syncManager).size(1)
                    .filter(this::isAuthorizationItem)
                    .build()
                    .pos(79, 34))
            .child(executionButton)
            .child(clearButton)
            .child(
                IKey.dynamic(() -> getFittedHatchDisplayText())
                    .asWidget()
                    .color(Color.WHITE.main)
                    .textAlign(Alignment.Center)
                    .background(GTGuiTextures.BACKGROUND_TEXT_FIELD)
                    .tooltipBuilder(
                        tooltip -> tooltip.addLine(
                            displayedHatchNumber > 0 ? getHatchDisplayText()
                                : StatCollector.translateToLocal("tst.bottledchaos.credential_hatch.disconnected")))
                    .tooltipAutoUpdate(true)
                    .pos(14, 8)
                    .size(148, 18));
    }
}
