package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.StringJoiner;
import java.util.function.BooleanSupplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineConfiguration;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.common.gui.modularui.adapter.CheckRecipeResultAdapter;

public class TST_Gui_BottledChaos extends TST_Gui<MM_BottledChaos> {

    private static final String RECIPE_CATEGORY_PANEL = "bottledChaosRecipeCategoryPanel";
    private static final String CREDENTIAL_INJECTION_PANEL = "bottledChaosCredentialInjectionPanel";
    private static final String CREDENTIAL_SLOT_GROUP = "bottledChaosCredentialCrafting";
    private static final int CREDENTIAL_TARGET_SLOT = 0;
    private static final int CREDENTIAL_SOURCE_SLOT_START = 1;
    private static final int CREDENTIAL_OUTPUT_SLOT = 9;
    private static final int CREDENTIAL_SECOND_OUTPUT_SLOT = 10;

    // #tr tst.bottledchaos.gui.recipe_category_configuration
    // # Machine Type Configuration
    // #zh_CN 机器类型配置

    // spotless:off
    // #tr tst.bottledchaos.gui.recipe_category_slot_hint
    // # Insert packaged machine structures. Each eightfold increase in packaged quantity adds 100% speed to the recipe pool.
    // #zh_CN 放入已封装的机器结构模组. 封装数量每增加至8倍, 配方池额外加速100%.
    // spotless:on

    // #tr tst.bottledchaos.gui.retired_machines
    // # Retired Machines
    // #zh_CN 弃用主机

    // #tr tst.bottledchaos.gui.credential_injection
    // # Credential Injection / Structure Packaging
    // #zh_CN 凭证注入 / 结构封装

    // spotless:off
    // #tr tst.bottledchaos.gui.credential_injection.hint
    // # Frame or module on the left; markers, credentials, structure materials or one ME item disk on the right.
    // #zh_CN 左侧放框架或模组; 右侧放标志物, 凭证, 结构材料或一张ME物品磁盘.
    // spotless:on

    // #tr tst.bottledchaos.gui.credential_injection.frame
    // # Carrier
    // #zh_CN 载体

    // #tr tst.bottledchaos.gui.credential_injection.item_materials
    // # Item Materials
    // #zh_CN 物品材料

    // #tr tst.bottledchaos.gui.credential_injection.target
    // # Credential frame or structure module
    // #zh_CN 凭证框架或结构模组

    // #tr tst.bottledchaos.gui.credential_injection.source
    // # Route markers, credentials, structure materials or one ME item disk
    // #zh_CN 路线标志物, 凭证, 结构材料或一张ME物品磁盘

    // spotless:off
    // #tr tst.bottledchaos.gui.credential_injection.preview
    // # Valid inputs preview the result automatically. Equivalent configurations cycle every 2 seconds. Taking the displayed result consumes the inputs.
    // #zh_CN 输入合法时自动预览结果, 多种配置每2秒轮换. 取出当前结果时扣除输入.
    // spotless:on

    // #tr tst.bottledchaos.gui.credential_injection.ready
    // # Ready
    // #zh_CN 就绪

    // #tr tst.bottledchaos.gui.credential_injection.success
    // # Credential tags injected successfully.
    // #zh_CN 凭证标签注入成功.

    // #tr tst.bottledchaos.gui.credential_injection.missing_target
    // # Insert a target credential frame.
    // #zh_CN 请放入目标凭证框架.

    // #tr tst.bottledchaos.gui.credential_injection.missing_source
    // # Insert at least one route marker or bound credential.
    // #zh_CN 请放入至少一个路线标志物或已绑定凭证.

    // #tr tst.bottledchaos.gui.credential_injection.invalid_source
    // # The sources cannot be combined in this operation.
    // #zh_CN 本次操作中的来源无法组合.

    // #tr tst.bottledchaos.gui.credential_injection.duplicate_tag
    // # A source contains a tag already present in the target or another source.
    // #zh_CN 来源含有目标或其他来源已经持有的标签.

    // #tr tst.bottledchaos.gui.credential_injection.capacity_exceeded
    // # The source credentials exceed the target frame capacity.
    // #zh_CN 来源凭证超过目标框架容量.

    // #tr tst.bottledchaos.gui.credential_injection.output_occupied
    // # Remove both existing results before the next operation.
    // #zh_CN 请先取出已有的全部结果, 再进行下一次操作.

    // #tr tst.bottledchaos.gui.power_allocation
    // # Power Allocation
    // #zh_CN 功率分配

    // #tr tst.bottledchaos.gui.power_allocation.distributed
    // # Distributed
    // #zh_CN 均分

    // #tr tst.bottledchaos.gui.power_allocation.concentrated
    // # Concentrated
    // #zh_CN 集中

    // #tr tst.bottledchaos.gui.power_allocation.pending
    // # Applies after the current round finishes.
    // #zh_CN 当前轮完成后生效.

    // spotless:off
    // #tr tst.bottledchaos.gui.power_allocation.distributed.hint
    // # Distributed mode allocates power to each processing group according to its allocated capacity, reserving shares for idle groups.
    // #zh_CN 均分模式按分配容量向各加工组分配功率, 待机组仍保留功率份额.
    // spotless:on

    // #tr tst.bottledchaos.gui.power_allocation.concentrated.hint
    // # Concentrated mode shares power only among executable groups, in proportion to used credential capacity.
    // #zh_CN 集中模式仅按实际凭证容量消耗比例向可执行的加工组分配功率.

    public TST_Gui_BottledChaos(MM_BottledChaos multiblock) {
        super(multiblock);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        uiSettings.customContainer(BottledChaosCraftingOutput.CraftingContainer::new);
        return super.build(guiData, syncManager, uiSettings);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("bottledChaosCapacity", new LongSyncValue(multiblock::getBottledChaosCapacity));
        syncManager.syncValue("bottledChaosUsedCapacity", new LongSyncValue(multiblock::getBottledChaosUsedCapacity));
        syncManager
            .syncValue("bottledChaosCredentialHatches", new IntSyncValue(multiblock::getInstalledCredentialHatchCount));
        syncManager
            .syncValue("bottledChaosCredentialHatchLimit", new IntSyncValue(multiblock::getCredentialHatchLimit));
        syncManager.syncValue(
            "bottledChaosStatusSummary",
            GenericSyncValue.builder(NBTTagCompound.class)
                .getter(multiblock::getBottledChaosStatusSummary)
                .adapter(ByteBufAdapters.NBT)
                .copy(tag -> (NBTTagCompound) tag.copy())
                .build());
        syncManager.syncValue(
            "bottledChaosCacheExportResult",
            GenericSyncValue.builder(CheckRecipeResult.class)
                .getter(multiblock::getCacheExportResult)
                .adapter(new CheckRecipeResultAdapter())
                .build());
        syncManager.syncValue(
            "bottledChaosConcentratedPower",
            new BooleanSyncValue(multiblock::isConcentratedPowerEnabled, multiblock::setConcentratedPowerEnabled)
                .allowC2S());
        syncManager.syncValue(
            "bottledChaosActiveConcentratedPower",
            new BooleanSyncValue(multiblock::isConcentratedPowerActive));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> terminal = new ListWidget<>().fullWidth()
            .crossAxisAlignment(Alignment.CrossAxis.START);
        LongSyncValue capacity = syncManager.findSyncHandler("bottledChaosCapacity", LongSyncValue.class);
        LongSyncValue usedCapacity = syncManager.findSyncHandler("bottledChaosUsedCapacity", LongSyncValue.class);
        IntSyncValue hatches = syncManager.findSyncHandler("bottledChaosCredentialHatches", IntSyncValue.class);
        IntSyncValue hatchLimit = syncManager.findSyncHandler("bottledChaosCredentialHatchLimit", IntSyncValue.class);
        GenericSyncValue<NBTTagCompound, ?> status = syncManager
            .findSyncHandler("bottledChaosStatusSummary", GenericSyncValue.class);

        terminal.child(
            IKey.dynamic(() -> getStatusSummaryText(status.getValue()))
                .asWidget()
                .fullWidth()
                .marginBottom(2));
        terminal.child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.capacity_usage",
                    usedCapacity.getLongValue(),
                    capacity.getLongValue()))
                .asWidget()
                .fullWidth()
                .marginBottom(2));
        terminal.child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.credential_hatches",
                    hatches.getIntValue(),
                    hatchLimit.getIntValue()))
                .asWidget()
                .fullWidth()
                .marginBottom(2));
        GenericSyncValue<CheckRecipeResult, ?> cacheExport = syncManager
            .findSyncHandler("bottledChaosCacheExportResult", GenericSyncValue.class);
        // #tr tst.bottledchaos.gui.cache_export_result
        // # Cache export: %s
        // #zh_CN 缓存导出: %s
        terminal.child(
            IKey.dynamic(
                () -> (cacheExport.getValue()
                    .wasSuccessful() ? EnumChatFormatting.AQUA : EnumChatFormatting.RED)
                    + StatCollector.translateToLocalFormatted(
                        "tst.bottledchaos.gui.cache_export_result",
                        cacheExport.getValue()
                            .getDisplayString()))
                .asWidget()
                .fullWidth()
                .setEnabledIf(widget -> !CheckRecipeResultRegistry.NONE.equals(cacheExport.getValue())));
        return terminal;
    }

    private static String getStatusSummaryText(NBTTagCompound summary) {
        if (summary == null) return "";
        StringJoiner lines = new StringJoiner("\n");
        NBTTagList entries = summary.getTagList("entries", 10);
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            String slots = MM_BottledChaos.formatProcessingGroups(entry.getInteger("slots"));
            String text;
            if (entry.hasKey("resultId", 8)) {
                CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(entry.getString("resultId"))
                    .newInstance();
                result.readFromNBT(entry.getCompoundTag("result"));
                text = result.getDisplayString();
            } else {
                text = StatCollector.translateToLocal(entry.getString("key"));
            }
            lines.add(
                (summary.getBoolean("errors") ? EnumChatFormatting.RED : EnumChatFormatting.AQUA)
                    + (slots.length() == 0 ? "" : slots + " ")
                    + text
                    + EnumChatFormatting.RESET);
        }
        return lines.toString();
    }

    @Override
    protected void initPanelMap(ModularPanel parent, PanelSyncManager syncManager) {
        super.initPanelMap(parent, syncManager);
        panelMap.put(
            CREDENTIAL_INJECTION_PANEL,
            syncManager.syncedPanel(
                CREDENTIAL_INJECTION_PANEL,
                true,
                (panelSyncManager, handler) -> createCredentialInjectionPanel(panelSyncManager, parent, handler)));
        panelMap.put(
            RECIPE_CATEGORY_PANEL,
            syncManager.syncedPanel(
                RECIPE_CATEGORY_PANEL,
                true,
                (panelSyncManager, handler) -> createRecipeCategoryPanel(panelSyncManager, parent, handler)));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager)
            .child(createCredentialInjectionButton(syncManager, parent))
            .child(createRecipeCategoryButton(syncManager, parent))
            .child(createCacheExportButton(syncManager));
    }

    private IWidget createCacheExportButton(PanelSyncManager syncManager) {
        InteractionSyncHandler export = new InteractionSyncHandler().setOnMousePressed(
            mouse -> {
                if (multiblock.getBaseMetaTileEntity()
                    .isServerSide()) multiblock.exportMaterialCaches();
            });
        syncManager.syncValue("bottledChaosCacheExport", export);
        // #tr tst.bottledchaos.gui.export_material_cache
        // # Export available intermediates to controller outputs. Blocked outputs preserve the cache.
        // #zh_CN 将可用中间产物排入主机输出. 输出空间不足时保留缓存.
        return new ButtonWidget<>().size(18, 18)
            .syncHandler(export)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT)
            .onMousePressed(export::onMousePressed)
            .playClickSound(true)
            .tooltipBuilder(tooltip -> tooltip.addLine(IKey.lang("tst.bottledchaos.gui.export_material_cache")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private void toggleConfigurationPanel(String panelName) {
        IPanelHandler panel = panelMap.get(panelName);
        if (panel.isPanelOpen()) {
            panel.closePanel();
            return;
        }
        for (String otherName : new String[] { CREDENTIAL_INJECTION_PANEL, RECIPE_CATEGORY_PANEL }) {
            IPanelHandler other = panelMap.get(otherName);
            if (!otherName.equals(panelName) && other.isPanelOpen()) other.closePanel();
        }
        panel.openPanel();
    }

    private IWidget createCredentialInjectionButton(PanelSyncManager syncManager, ModularPanel parent) {
        return new ButtonWidget<>().size(18, 18)
            .marginLeft(18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_IMPORT)
            .onMousePressed(clickData -> {
                toggleConfigurationPanel(CREDENTIAL_INJECTION_PANEL);
                return true;
            })
            .tooltipBuilder(tooltip -> tooltip.addLine(IKey.lang("tst.bottledchaos.gui.credential_injection")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel createCredentialInjectionPanel(PanelSyncManager syncManager, ModularPanel parent,
        IPanelHandler panelHandler) {
        BooleanSupplier panelOpen = () -> panelHandler.isPanelOpen() && syncManager.getModularSyncManager()
            .isOpen();
        syncManager.registerSlotGroup(new SlotGroup(CREDENTIAL_SLOT_GROUP, 4).setAllowSorting(false));
        var outputs = new BottledChaosCraftingOutput(multiblock, panelOpen);
        BooleanSyncValue unload = new BooleanSyncValue(
            () -> multiblock.getCredentialCrafting()
                .isUnloading(),
            value -> {
                if (panelOpen.getAsBoolean()) multiblock.getCredentialCrafting()
                    .setUnloading(value);
            }).allowC2S();
        syncManager.syncValue("bottledChaosStructureUnload", unload);
        StringSyncValue status = new StringSyncValue(
            () -> multiblock.getCredentialCrafting()
                .status());
        syncManager.syncValue("bottledChaosCredentialInjectionStatus", status);
        // Close immediately so the previous page's slots cannot remain active during a close animation.
        return new ModularPanel(CREDENTIAL_INJECTION_PANEL) {

            @Override
            public boolean shouldAnimate() {
                return false;
            }
        }.relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(190, 160)
            .child(
                Flow.column()
                    .full()
                    .padding(6)
                    .child(
                        new TextWidget<>(IKey.lang("tst.bottledchaos.gui.credential_injection"))
                            .textAlign(Alignment.Center)
                            .fullWidth()
                            .height(16))
                    .child(
                        IKey.lang("tst.bottledchaos.gui.credential_injection.hint")
                            .asWidget()
                            .fullWidth()
                            .height(25))
                    .child(createCredentialItemArea(panelOpen, outputs))
                    .child(
                        Flow.row()
                            .fullWidth()
                            .height(22)
                            .mainAxisAlignment(Alignment.MainAxis.CENTER)
                            .child(
                                IKey.dynamic(
                                    () -> StatCollector.translateToLocal(
                                        unload.getBoolValue() ? "tst.bottledchaos.gui.structure_packaging.unloading"
                                            : "tst.bottledchaos.gui.structure_packaging.automatic"))
                                    .asWidget()
                                    .width(144))
                            .child(
                                new ButtonWidget<>().size(18, 18)
                                    .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT)
                                    .onMousePressed(mouse -> {
                                        unload.setBoolValue(!unload.getBoolValue(), true, true);
                                        return true;
                                    })
                                    .tooltipBuilder(
                                        tooltip -> tooltip
                                            .addLine(IKey.lang("tst.bottledchaos.gui.structure_packaging.unload")))))
                    .child(
                        Flow.row()
                            .fullWidth()
                            .height(20)
                            .mainAxisAlignment(Alignment.MainAxis.CENTER)
                            .child(
                                IKey.dynamic(() -> StatCollector.translateToLocal(status.getStringValue()))
                                    .asWidget()
                                    .fullWidth()
                                    .textAlign(Alignment.Center))));
    }

    // #tr tst.bottledchaos.gui.structure_packaging.automatic
    // # Preview packaging results; take to confirm
    // #zh_CN 预览封装结果, 取出后确认

    // spotless:off
    // #tr tst.bottledchaos.gui.structure_packaging.unload
    // # Switch between packaging and unloading previews. Taking a result confirms the operation. Insufficient disk capacity prevents unloading.
    // #zh_CN 切换封装或卸载预览, 取出结果后执行. 磁盘容量不足时无法卸载.
    // spotless:on

    // #tr tst.bottledchaos.gui.structure_packaging.unloading
    // # Preview unloading results; take to confirm
    // #zh_CN 预览卸载结果, 取出后确认
    private IWidget createCredentialItemArea(BooleanSupplier panelOpen, BottledChaosCraftingOutput outputs) {
        return Flow.row()
            .fullWidth()
            .height(48)
            .childPadding(4)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .child(
                createCredentialSingleSlotArea(
                    "tst.bottledchaos.gui.credential_injection.frame",
                    CREDENTIAL_TARGET_SLOT,
                    "tst.bottledchaos.gui.credential_injection.target",
                    true,
                    panelOpen))
            .child(
                Flow.column()
                    .size(80, 48)
                    .padding(2)
                    .background(GTGuiTextures.BACKGROUND_GRAY_BORDER)
                    .crossAxisAlignment(Alignment.CrossAxis.CENTER)
                    .child(
                        new TextWidget<>(IKey.lang("tst.bottledchaos.gui.credential_injection.item_materials"))
                            .textAlign(Alignment.Center)
                            .fullWidth()
                            .height(8))
                    .child(createCredentialSourceSlots(panelOpen)))
            .child(createCredentialOutputArea(outputs));
    }

    private IWidget createCredentialOutputArea(BottledChaosCraftingOutput outputs) {
        return Flow.column()
            .size(42, 48)
            .padding(3)
            .background(GTGuiTextures.BACKGROUND_GRAY_BORDER)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(
                new TextWidget<>(IKey.lang("GTPP.nei.result")).textAlign(Alignment.Center)
                    .fullWidth()
                    .height(10))
            .child(
                Flow.row()
                    .size(36, 18)
                    .child(createCredentialOutputSlot(CREDENTIAL_OUTPUT_SLOT, outputs))
                    .child(createCredentialOutputSlot(CREDENTIAL_SECOND_OUTPUT_SLOT, outputs)));
    }

    private IWidget createCredentialOutputSlot(int slot, BottledChaosCraftingOutput outputs) {
        return new ItemSlot().size(18, 18)
            .syncHandler(outputs.slot(slot, CREDENTIAL_SLOT_GROUP))
            .tooltip(tooltip -> tooltip.addLine(IKey.lang("tst.bottledchaos.gui.credential_injection.preview")));
    }

    private Flow createCredentialSingleSlotArea(String labelKey, int slot, String tooltipKey, boolean canPut,
        BooleanSupplier panelOpen) {
        return Flow.column()
            .size(42, 48)
            .padding(3)
            .background(GTGuiTextures.BACKGROUND_GRAY_BORDER)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(
                new TextWidget<>(IKey.lang(labelKey)).textAlign(Alignment.Center)
                    .fullWidth()
                    .height(10))
            .child(
                new ItemSlot().size(18, 18)
                    .slot(
                        createConfigurationSlot(multiblock.getCredentialInjectionInventory(), slot, panelOpen)
                            .slotGroup(CREDENTIAL_SLOT_GROUP)
                            .accessibility(canPut, true))
                    .tooltip(tooltip -> tooltip.addLine(IKey.lang(tooltipKey))));
    }

    private Grid createCredentialSourceSlots(BooleanSupplier panelOpen) {
        return new Grid().coverChildren()
            .gridOfWidthHeight(
                4,
                2,
                (x, y, index) -> new ItemSlot()
                    .slot(
                        createConfigurationSlot(
                            multiblock.getCredentialInjectionInventory(),
                            CREDENTIAL_SOURCE_SLOT_START + index,
                            panelOpen).slotGroup(CREDENTIAL_SLOT_GROUP))
                    .tooltip(
                        tooltip -> tooltip.addLine(IKey.lang("tst.bottledchaos.gui.credential_injection.source"))));
    }

    private IWidget createRecipeCategoryButton(PanelSyncManager syncManager, ModularPanel parent) {
        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
            .onMousePressed(clickData -> {
                toggleConfigurationPanel(RECIPE_CATEGORY_PANEL);
                return true;
            })
            .tooltipBuilder(tooltip -> tooltip.addLine(IKey.lang("tst.bottledchaos.gui.recipe_category_configuration")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel createRecipeCategoryPanel(PanelSyncManager syncManager, ModularPanel parent,
        IPanelHandler panelHandler) {
        BooleanSupplier panelOpen = () -> panelHandler.isPanelOpen() && syncManager.getModularSyncManager()
            .isOpen();
        LongSyncValue required = new LongSyncValue(
            () -> multiblock.getMachineConfiguration()
                .requiredPoolMask(),
            multiblock.getMachineConfiguration()::setRequiredPoolMask);
        syncManager.syncValue("bottledChaosRequiredPools", required);
        var rows = new ListWidget<>().fullWidth()
            .height(104);
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++) {
            final int poolSlot = slot;
            IntSyncValue users = new IntSyncValue(() -> multiblock.getRecipePoolUsers(poolSlot));
            syncManager.syncValue("bottledChaosPoolUsers" + poolSlot, users);
            BooleanSupplier visible = () -> (required.getLongValue() & (1L << poolSlot)) != 0
                || multiblock.getMachineConfiguration()
                    .machines()
                    .getStackInSlot(poolSlot) != null;
            rows.child(
                Flow.row()
                    .fullWidth()
                    .height(20)
                    .setEnabledIf(widget -> visible.getAsBoolean())
                    .child(
                        new ItemSlot()
                            .syncHandler(
                                new MachineSlotSyncHandler(
                                    createConfigurationSlot(
                                        multiblock.getMachineConfiguration()
                                            .machines(),
                                        poolSlot,
                                        () -> panelOpen.getAsBoolean() && visible.getAsBoolean())
                                            .ignoreMaxStackSize(true)
                                            .singletonSlotGroup()))
                            .tooltip(
                                tooltip -> tooltip
                                    .addLine(IKey.str(BottledChaosMachineConfiguration.poolName(poolSlot)))))
                    .child(
                        new TextWidget<>(IKey.str(BottledChaosMachineConfiguration.poolName(poolSlot))).width(168)
                            .height(18)
                            .textAlign(Alignment.CenterLeft)
                            .tooltipBuilder(tooltip -> {
                                // #tr tst.bottledchaos.gui.pool_users
                                // # Processing groups: %s
                                // #zh_CN 加工组: %s
                                String groups = MM_BottledChaos.formatProcessingGroups(users.getValue());
                                tooltip.addLine(
                                    IKey.str(
                                        StatCollector.translateToLocalFormatted(
                                            "tst.bottledchaos.gui.pool_users",
                                            groups.isEmpty() ? StatCollector.translateToLocal("GT5U.gui.text.nil")
                                                : groups)));
                            })
                            .tooltipAutoUpdate(true)));
        }
        return new ModularPanel(RECIPE_CATEGORY_PANEL) {

            @Override
            public boolean shouldAnimate() {
                return false;
            }
        }.relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(208, 268)
            .child(
                Flow.column()
                    .full()
                    .padding(6)
                    .child(
                        new TextWidget<>(IKey.lang("tst.bottledchaos.gui.recipe_category_configuration"))
                            .textAlign(Alignment.Center)
                            .fullWidth()
                            .height(18))
                    .child(
                        IKey.lang("tst.bottledchaos.gui.recipe_category_slot_hint")
                            .asWidget()
                            .fullWidth()
                            .height(38))
                    .child(rows)
                    .child(
                        new TextWidget<>(IKey.lang("tst.bottledchaos.gui.retired_machines")).textAlign(Alignment.Center)
                            .fullWidth()
                            .height(18))
                    .child(
                        new ListWidget<>().fullWidth()
                            .height(72)
                            .child(
                                new Grid().coverChildren()
                                    .gridOfWidthHeight(
                                        8,
                                        8,
                                        (x, y, index) -> new ItemSlot().syncHandler(
                                            new MachineSlotSyncHandler(
                                                createConfigurationSlot(
                                                    multiblock.getMachineConfiguration()
                                                        .retired(),
                                                    index,
                                                    panelOpen).ignoreMaxStackSize(true)
                                                        .singletonSlotGroup()
                                                        .filter(stack -> false)))))));
    }

    /** Sends an integer quantity after native slot packets, only when a machine slot changes. */
    private static final class MachineSlotSyncHandler extends ItemSlotSH {

        private static final int SYNC_MACHINE_STACK = 2;
        private ItemStack lastMachineStack;

        private MachineSlotSyncHandler(ModularSlot slot) {
            super(slot);
        }

        @Override
        public void detectAndSendChanges(boolean init) {
            super.detectAndSendChanges(init);
            syncMachineStack(init);
        }

        @Override
        public void checkUpdate() {
            super.checkUpdate();
            syncMachineStack(false);
        }

        @Override
        public void forceSyncItem() {
            super.forceSyncItem();
            syncMachineStack(true);
        }

        private void syncMachineStack(boolean force) {
            if (!isValid() || getSyncManager().isClient()) return;
            ItemStack stack = getSlot().getStack();
            if (!force && ItemStack.areItemStacksEqual(lastMachineStack, stack)) return;
            lastMachineStack = stack == null ? null : stack.copy();
            ItemStack template = stack == null ? null : stack.copy();
            if (template != null) template.stackSize = 1;
            syncToClient(SYNC_MACHINE_STACK, buffer -> {
                NetworkUtils.writeItemStack(buffer, template);
                buffer.writeInt(stack == null ? 0 : stack.stackSize);
            });
        }

        @Override
        public void readOnClient(int id, PacketBuffer buffer) {
            if (id != SYNC_MACHINE_STACK) {
                super.readOnClient(id, buffer);
                return;
            }
            ItemStack stack = NetworkUtils.readItemStack(buffer);
            int count = buffer.readInt();
            if (stack != null) stack.stackSize = count;
            getSlot().putStack(stack);
            onSlotUpdate(stack, false, true, false);
        }
    }

    private ModularSlot createConfigurationSlot(IItemHandler inventory, int slot, BooleanSupplier panelOpen) {
        return new ModularSlot(inventory, slot) {

            @Override
            public boolean func_111238_b() {
                return panelOpen.getAsBoolean() && super.func_111238_b();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return panelOpen.getAsBoolean() && super.isItemValid(stack);
            }

            @Override
            public boolean canTakeStack(EntityPlayer player) {
                return panelOpen.getAsBoolean() && super.canTakeStack(player);
            }

            @Override
            public boolean canDragIntoSlot() {
                return panelOpen.getAsBoolean() && super.canDragIntoSlot();
            }

            @Override
            public void onSlotChanged() {
                if (inventory == multiblock.getCredentialInjectionInventory()) {
                    multiblock.getCredentialCrafting()
                        .invalidate();
                    multiblock.markDirty();
                }
            }
        };
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .child(createPowerSwitchButton())
            .child(createPowerAllocationButton(syncManager))
            .child(createStructureUpdateButton(syncManager));
    }

    private IWidget createPowerAllocationButton(PanelSyncManager syncManager) {
        BooleanSyncValue mode = syncManager.findSyncHandler("bottledChaosConcentratedPower", BooleanSyncValue.class);
        BooleanSyncValue activeMode = syncManager
            .findSyncHandler("bottledChaosActiveConcentratedPower", BooleanSyncValue.class);
        return createDisableableToggleButton(
            mode,
            () -> true,
            GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF,
            GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON,
            tooltip -> {
                tooltip.addLine(IKey.lang("tst.bottledchaos.gui.power_allocation"));
                tooltip.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocal(
                            mode.getBoolValue() ? "tst.bottledchaos.gui.power_allocation.concentrated"
                                : "tst.bottledchaos.gui.power_allocation.distributed")));
                tooltip.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocal(
                            mode.getBoolValue() ? "tst.bottledchaos.gui.power_allocation.concentrated.hint"
                                : "tst.bottledchaos.gui.power_allocation.distributed.hint")));
                if (mode.getBoolValue() != activeMode.getBoolValue())
                    tooltip.addLine(IKey.lang("tst.bottledchaos.gui.power_allocation.pending"));
            }).tooltipAutoUpdate(true);
    }
}
