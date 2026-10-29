package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.CARRIER_SLOT;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.MATERIAL_SLOT_START;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.OUTPUT_SLOT;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting.SECOND_OUTPUT_SLOT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineDescriptions;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result.BottledChaosResultData;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTextLayout;
import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.FluidDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.text.RichText;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
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
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ScrollingTextWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.common.gui.modularui.adapter.CheckRecipeResultAdapter;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;

public class TST_Gui_BottledChaos extends TST_Gui<MM_BottledChaos> {

    private static final String CREDENTIAL_SLOT_GROUP = "bottledChaosCredentialCrafting";
    private static final int PANEL_WIDTH = 448;
    private static final int PANEL_HEIGHT = 335;
    private static final int TABS_TOP = 8;
    private static final int TERMINAL_TOP = TABS_TOP + 20;
    private static final int TERMINAL_WIDTH = PANEL_WIDTH - 16;
    private static final int TERMINAL_HEIGHT = 188;
    private static final int LOWER_TOP = TERMINAL_TOP + TERMINAL_HEIGHT + 5;
    private static final int MODULE_LIST_WIDTH = 156;
    private static final int INVENTORY_LEFT = 18;
    private static final int INVENTORY_WIDTH = 194;
    // The native right button column ends two pixels before its row's right edge.
    private static final int INVENTORY_RIGHT = INVENTORY_LEFT + INVENTORY_WIDTH - 2;
    private static final int PACKAGING_LEFT = PANEL_WIDTH - INVENTORY_RIGHT;
    private static final int PACKAGING_WIDTH = PANEL_WIDTH - PACKAGING_LEFT - 8;
    private static final int LOWER_DIVIDER = (INVENTORY_RIGHT + PACKAGING_LEFT) / 2;
    private static final int MODULE_TEXT = 0xFFE5EEF1;
    private static final int MODULE_MUTED = 0xFFB9CBD3;
    private static final int MODULE_UNUSED = 0xFFD16C6C;
    private static final int MODULE_WARNING = 0xFFFF7777;
    private final BottledChaosGuiSkin skin = new BottledChaosGuiSkin();
    private IntSyncValue terminalPage;
    private int terminalPageIndex;

    // #tr tst.bottledchaos.gui.runtime_tab
    // # Operation
    // #zh_CN 运行信息

    // #tr tst.bottledchaos.gui.modules_tab
    // # Modules
    // #zh_CN 结构模组

    // #tr tst.bottledchaos.gui.pools_tab
    // # Recipe Pools
    // #zh_CN 配方池

    // #tr tst.bottledchaos.gui.passive_inputs
    // # Passive inputs
    // #zh_CN 被动消耗

    // #tr tst.bottledchaos.gui.per_round
    // # %s x%s / round
    // #zh_CN %s x%s / 轮

    // #tr tst.bottledchaos.gui.no_passive_inputs
    // # No passive consumption.
    // #zh_CN 当前无被动消耗.

    // #tr tst.bottledchaos.gui.recipe_category_configuration
    // # Machine Modules
    // #zh_CN 结构模组配置

    // spotless:off
    // #tr tst.bottledchaos.gui.recipe_category_slot_hint
    // # Every 8x quantity adds 100% pool speed.
    // #zh_CN 数量每增至8倍, 配方池加速100%.
    // spotless:on

    // #tr tst.bottledchaos.gui.credential_injection
    // # Credential Injection / Structure Packaging
    // #zh_CN 凭证注入 / 结构封装

    // #tr tst.bottledchaos.gui.credential_injection.frame
    // # Carrier
    // #zh_CN 载体

    // #tr tst.bottledchaos.gui.credential_injection.item_materials
    // # Materials
    // #zh_CN 材料

    // #tr tst.bottledchaos.gui.credential_injection.target
    // # Credential frame or structure module
    // #zh_CN 凭证框架或结构模组

    // #tr tst.bottledchaos.gui.credential_injection.source
    // # Route markers, credentials, structure materials or one ME item disk
    // #zh_CN 路线标志物, 凭证, 结构材料或一张ME物品磁盘

    // spotless:off
    // #tr tst.bottledchaos.gui.credential_injection.preview
    // # Automatic preview; configurations cycle every 2 seconds. Taking a result consumes its inputs.
    // #zh_CN 自动预览, 配置每2秒轮换. 取出结果时扣除材料.
    // spotless:on

    // #tr tst.bottledchaos.gui.credential_injection.ready
    // # Ready
    // #zh_CN 就绪

    // #tr tst.bottledchaos.gui.credential_injection.success
    // # Tags written.
    // #zh_CN 标签已写入.

    // #tr tst.bottledchaos.gui.credential_injection.missing_target
    // # Missing credential frame.
    // #zh_CN 缺少凭证框架.

    // #tr tst.bottledchaos.gui.credential_injection.missing_source
    // # Missing route markers or bound credentials.
    // #zh_CN 缺少路线标志物或已绑定凭证.

    // #tr tst.bottledchaos.gui.credential_injection.invalid_source
    // # Incompatible inputs.
    // #zh_CN 输入无法组合.

    // #tr tst.bottledchaos.gui.credential_injection.duplicate_tag
    // # Duplicate route tags.
    // #zh_CN 路线标签重复.

    // #tr tst.bottledchaos.gui.credential_injection.capacity_exceeded
    // # Credential capacity exceeded.
    // #zh_CN 超出凭证容量.

    // #tr tst.bottledchaos.gui.credential_injection.output_occupied
    // # Output slots occupied.
    // #zh_CN 结果槽被占用.

    // #tr tst.bottledchaos.gui.power_allocation
    // # Power Allocation
    // #zh_CN 电力分配

    // #tr tst.bottledchaos.gui.power_allocation.distributed
    // # Distributed
    // #zh_CN 均分

    // #tr tst.bottledchaos.gui.power_allocation.concentrated
    // # Concentrated
    // #zh_CN 集中

    // #tr tst.bottledchaos.gui.power_allocation.current
    // # Current: %s
    // #zh_CN 当前: %s

    // #tr tst.bottledchaos.gui.power_allocation.pending
    // # Waiting for this round to finish.
    // #zh_CN 等待本轮完成.

    // spotless:off
    // #tr tst.bottledchaos.gui.power_allocation.distributed.hint
    // # Reserve power by valid credential capacity.
    // #zh_CN 按有效凭证容量预留功率.
    // spotless:on

    // spotless:off
    // #tr tst.bottledchaos.gui.power_allocation.concentrated.hint
    // # Allocate power by the credential capacity of running groups.
    // #zh_CN 按运行加工组的凭证容量分配功率.
    // spotless:on

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
        terminalPage = new IntSyncValue(
            () -> terminalPageIndex,
            value -> terminalPageIndex = Math.max(0, Math.min(3, value))).allowC2S();
        syncManager.syncValue("bottledChaosTerminalPage", terminalPage);
        syncManager.syncValue("bottledChaosCapacity", new IntSyncValue(multiblock::getBottledChaosCapacity));
        syncManager.syncValue("bottledChaosUsedCapacity", new IntSyncValue(multiblock::getBottledChaosUsedCapacity));
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
            "bottledChaosProcessingGroups",
            GenericSyncValue.builder(NBTTagCompound.class)
                .getter(multiblock::getProcessingGroupSummary)
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
        IntSyncValue capacity = syncManager.findSyncHandler("bottledChaosCapacity", IntSyncValue.class);
        IntSyncValue usedCapacity = syncManager.findSyncHandler("bottledChaosUsedCapacity", IntSyncValue.class);
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
                    usedCapacity.getIntValue(),
                    capacity.getIntValue()))
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
        // # Cache export result: %s
        // #zh_CN 缓存排出结果: %s
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
        GenericSyncValue<NBTTagCompound, ?> groups = syncManager
            .findSyncHandler("bottledChaosProcessingGroups", GenericSyncValue.class);
        for (int i = 0; i < MM_BottledChaos.THREAD_COUNT; i++) {
            int index = i;
            terminal.child(new Flow(GuiAxis.Y) {

                @Override
                public void onChildChangeEnabled(IWidget child, boolean enabled) {
                    super.onChildChangeEnabled(child, enabled);
                    // Local Flow layout does not remeasure its height or reposition subsequent groups.
                    terminal.scheduleResize();
                }
            }.fullWidth()
                .coverChildrenHeight(0)
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .collapseDisabledChild()
                .childPadding(2)
                .marginTop(6)
                .marginBottom(4)
                .setEnabledIf(widget -> index < MM_BottledChaos.getProcessingGroupCount(groups.getValue()))
                .child(groupHeader(groups::getValue, index))
                .child(
                    IKey.dynamic(
                        () -> MM_BottledChaos
                            .getBlockedRouteText(MM_BottledChaos.getProcessingGroupData(groups.getValue(), index)))
                        .asWidget()
                        .fullWidth()
                        .textAlign(Alignment.TopLeft)
                        .shadow(false)
                        .setEnabledIf(
                            widget -> MM_BottledChaos.getProcessingGroupData(groups.getValue(), index)
                                .hasKey("blockedRoute", 10)))
                .child(
                    new ParentWidget<>().fullWidth()
                        .height(11)
                        .background(skin.progress(() -> {
                            NBTTagCompound job = MM_BottledChaos.getProcessingGroupData(groups.getValue(), index);
                            return job.getInteger("progress") / (double) Math.max(1, job.getInteger("duration"));
                        }))
                        .disableHoverBackground()
                        .child(IKey.dynamic(() -> {
                            NBTTagCompound job = MM_BottledChaos.getProcessingGroupData(groups.getValue(), index);
                            return job.getInteger("duration") < 20
                                ? StatCollector.translateToLocalFormatted(
                                    "GT5U.waila.machine.progress_tick",
                                    job.getInteger("progress"),
                                    job.getInteger("duration"))
                                : StatCollector.translateToLocalFormatted(
                                    "GT5U.waila.machine.progress_second",
                                    String.format("%.2f", job.getInteger("progress") / 20d),
                                    String.format("%.2f", job.getInteger("duration") / 20d));
                        })
                            .asWidget()
                            .fullWidth()
                            .height(11)
                            .textAlign(Alignment.Center)
                            .color(MODULE_TEXT)
                            .shadow(false))
                        .setEnabledIf(
                            widget -> MM_BottledChaos.getProcessingGroupData(groups.getValue(), index)
                                .getInteger("duration") > 0))
                .child(
                    new ParentWidget<>().fullWidth()
                        .height(12)
                        .background(new DynamicDrawable(() -> groupOutput(groups.getValue(), index)))
                        .disableHoverBackground()
                        .setEnabledIf(
                            widget -> MM_BottledChaos.getProcessingGroupData(groups.getValue(), index)
                                .getCompoundTag("outputs")
                                .getTagList("entries", 10)
                                .tagCount() > 0)));
        }
        return terminal;
    }

    private static ParentWidget<?> groupHeader(Supplier<NBTTagCompound> summary, int index) {
        Supplier<NBTTagCompound> job = () -> MM_BottledChaos.getProcessingGroupData(summary.get(), index);
        List<Supplier<String>> columns = Arrays.asList(
            () -> EnumChatFormatting.WHITE + "#"
                + job.get()
                    .getInteger("slot")
                + " "
                + MM_BottledChaos.localizeKeyList(
                    job.get()
                        .getString("family")),
            () -> EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted("tst.bottledchaos.input_share", ""),
            () -> EnumChatFormatting.AQUA + MM_BottledChaos.getProcessingGroupShareValue(job.get()),
            () -> MM_BottledChaos.getProcessingGroupPowerText(job.get()),
            () -> MM_BottledChaos.getProcessingGroupVoltageText(job.get()),
            () -> MM_BottledChaos.getStatusColor(
                job.get()
                    .getString("status"))
                + MM_BottledChaos.getWailaStatusText(job.get()));
        // Reserve the same columns in every row, including paused groups without current power use.
        // The label ends at a fixed colon anchor; percentages grow rightward in their own column.
        int[] widths = { 104, 46, 40, 70, 92, 40 };
        ParentWidget<?> header = new ParentWidget<>().size(TERMINAL_WIDTH - 20, 10);
        int x = 0;
        for (int column = 0; column < columns.size(); column++) {
            Supplier<String> text = columns.get(column);
            int width = widths[column];
            header.child(
                IKey.dynamic(
                    () -> TSTTextLayout.ellipsize(text.get(), width, TextRenderer.getFontRenderer()::getStringWidth))
                    .asWidget()
                    .pos(x, 0)
                    .size(width, 10)
                    .textAlign(column == 0 || column == 2 ? Alignment.TopLeft : Alignment.TopRight)
                    .shadow(false)
                    .tooltipBuilder(tooltip -> {
                        String full = text.get();
                        if (TextRenderer.getFontRenderer()
                            .getStringWidth(full) > width) tooltip.addLine(full);
                    })
                    .tooltipAutoUpdate(true));
            x += width + 4;
        }
        return header;
    }

    @SideOnly(Side.CLIENT)
    private static RichText groupOutput(NBTTagCompound summary, int index) {
        NBTTagCompound job = MM_BottledChaos.getProcessingGroupData(summary, index);
        NBTTagCompound outputs = job.getCompoundTag("outputs");
        NBTTagList entries = outputs.getTagList("entries", 10);
        var font = TextRenderer.getFontRenderer();
        String title = EnumChatFormatting.WHITE + MM_BottledChaos.getProcessingGroupOutputTitle(job) + " ";
        RichText line = new RichText().alignment(Alignment.CenterLeft)
            .textColor(0xFFFFFFFF)
            .textShadow(false)
            .add(title);
        int used = font.getStringWidth(title);
        int shown = 0;
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            ItemStack item = entry.hasKey("item", 10) ? ItemStack.loadItemStackFromNBT(entry.getCompoundTag("item"))
                : null;
            FluidStack fluid = entry.hasKey("fluid", 10)
                ? FluidStack.loadFluidStackFromNBT(entry.getCompoundTag("fluid"))
                : null;
            if (item == null && fluid == null) continue;
            String separator = shown == 0 ? "" : EnumChatFormatting.WHITE + ", ";
            int remaining = TERMINAL_WIDTH - 20 - used - font.getStringWidth(separator) - 12 - font.getStringWidth(" ");
            if (remaining < font.getStringWidth("...")) {
                line.add(EnumChatFormatting.WHITE + "...");
                break;
            }
            IDrawable icon = item != null ? new ItemDrawable(item) : new FluidDrawable(fluid);
            line.add(separator)
                .add(
                    icon.asIcon()
                        .size(12))
                .add(" ");
            String text = MM_BottledChaos.formatGroupOutput(item, fluid, entry.getLong("amount"));
            boolean more = i + 1 < entries.tagCount() || outputs.getInteger("total") > entries.tagCount();
            // Reserve an ellipsis for subsequent outputs, measuring icons as well as text.
            if (font.getStringWidth(text) + (more ? font.getStringWidth("...") : 0) > remaining) {
                line.add(TSTTextLayout.ellipsize(text + (more ? "..." : ""), remaining, font::getStringWidth));
                break;
            }
            line.add(text);
            used += font.getStringWidth(separator) + 12 + font.getStringWidth(" ") + font.getStringWidth(text);
            shown++;
            if (i + 1 == entries.tagCount() && more) line.add(EnumChatFormatting.WHITE + "...");
        }
        return line;
    }

    private static String getStatusSummaryText(NBTTagCompound summary) {
        if (summary == null) return "";
        StringJoiner lines = new StringJoiner("\n");
        if (summary.getBoolean("wireless"))
            lines.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tst.bottledchaos.wireless_mode"));
        NBTTagList entries = summary.getTagList("entries", 10);
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            String slots = MM_BottledChaos.formatProcessingGroups(entry.getInteger("slots"));
            lines.add(
                (summary.getBoolean("errors") ? EnumChatFormatting.RED
                    : "gt.interact.desc.mb.running".equals(entry.getString("key")) ? EnumChatFormatting.GOLD
                        : EnumChatFormatting.AQUA)
                    + (slots.length() == 0 ? "" : slots + " ")
                    + BottledChaosResultData.displayText(entry, "key")
                    + EnumChatFormatting.RESET);
        }
        return lines.toString();
    }

    @Override
    protected ModularPanel getBasePanel(PosGuiData data, PanelSyncManager manager, UISettings settings) {
        return new GTBaseGuiBuilder(multiblock, data, manager, settings).setWidth(PANEL_WIDTH)
            .setHeight(PANEL_HEIGHT)
            .doesBindPlayerInventory(false)
            .doesAddGregTechLogo(false)
            .build()
            .background(skin.background(LOWER_DIVIDER, LOWER_TOP + 2))
            .disableHoverBackground();
    }

    @Override
    public Flow createMainColumn(ModularPanel panel, PanelSyncManager manager) {
        ParentWidget<?> terminal = new ParentWidget<>().pos(8, TERMINAL_TOP)
            .size(TERMINAL_WIDTH, TERMINAL_HEIGHT)
            .background(skin.TERMINAL)
            .disableHoverBackground()
            .child(
                createTerminalTextWidget(manager, panel).pos(6, 6)
                    .size(TERMINAL_WIDTH - 12, TERMINAL_HEIGHT - 12)
                    .showScrollShadows(false)
                    .setEnabledIf(widget -> terminalPage.getIntValue() == 0))
            .child(
                BottledChaosHatchPage.create(multiblock, panel, manager, () -> terminalPage.getIntValue() == 1, skin)
                    .setEnabledIf(widget -> terminalPage.getIntValue() == 1))
            .child(createModuleConfiguration(manager).setEnabledIf(widget -> terminalPage.getIntValue() == 2))
            .child(createModuleInventory(manager).setEnabledIf(widget -> terminalPage.getIntValue() == 3));
        ParentWidget<?> main = new ParentWidget<>().size(PANEL_WIDTH, PANEL_HEIGHT)
            .child(createTerminalTab("tst.bottledchaos.gui.runtime_tab", 0).pos(8, TABS_TOP))
            .child(createTerminalTab("tst.bottledchaos.gui.hatches_tab", 1).pos(88, TABS_TOP))
            .child(createTerminalTab("tst.bottledchaos.gui.pools_tab", 2).pos(168, TABS_TOP))
            .child(createTerminalTab("tst.bottledchaos.gui.modules_tab", 3).pos(248, TABS_TOP))
            .child(createPassiveInputs(manager))
            .child(terminal)
            .child(
                createPanelGap(panel, manager).pos(INVENTORY_LEFT, LOWER_TOP)
                    .size(INVENTORY_WIDTH, 22))
            .child(
                new ParentWidget<>().pos(INVENTORY_LEFT, LOWER_TOP + 23)
                    .size(INVENTORY_WIDTH, 76)
                    .child(createInventoryRow(panel, manager)))
            .child(createCredentialWorkspace(manager).pos(PACKAGING_LEFT, LOWER_TOP))
            .child(
                makeLogoWidget(manager, panel).marginTop(0)
                    .pos(PANEL_WIDTH - 27, PANEL_HEIGHT - 27)
                    .size(17, 17));
        if (multiblock.canBeMuffled()) main.child(
            super.createMuffleButton().right(8)
                .top(TABS_TOP)
                .size(18, 18));
        skinSlots(main);
        return Flow.column()
            .full()
            .child(main);
    }

    private ButtonWidget<?> createTerminalTab(String key, int page) {
        return new ButtonWidget<>().size(80, 16)
            .background(new DynamicDrawable(() -> terminalPage.getIntValue() == page ? skin.SELECTED : skin.BUTTON))
            .disableHoverBackground()
            .overlay(
                IKey.lang(key)
                    .color(skin::buttonText))
            .onMousePressed(mouse -> {
                if (mouse != 0) return false;
                terminalPage.setIntValue(page, true, true);
                return true;
            });
    }

    private void skinSlots(IWidget widget) {
        if (widget instanceof ItemSlot slot) {
            slot.background(skin.SLOT)
                .disableHoverBackground();
        }
        for (IWidget child : widget.getChildren()) skinSlots(child);
    }

    private ParentWidget<?> createCredentialWorkspace(PanelSyncManager manager) {
        // These slots belong to the main container, not to the currently selected terminal page.
        BooleanSupplier open = () -> manager.getModularSyncManager()
            .isOpen();
        manager.registerSlotGroup(new SlotGroup(CREDENTIAL_SLOT_GROUP, 4).setAllowSorting(false));
        var outputs = new BottledChaosCraftingOutput(multiblock, open);
        StringSyncValue status = new StringSyncValue(
            () -> multiblock.getCredentialCrafting()
                .status());
        manager.syncValue("bottledChaosCredentialInjectionStatus", status);
        return new ParentWidget<>().size(PACKAGING_WIDTH, 110)
            .child(
                new ScrollingTextWidget(IKey.lang("tst.bottledchaos.gui.credential_injection")).pos(0, 3)
                    .size(PACKAGING_WIDTH, 12)
                    .shadow(false)
                    .color(skin::workspaceText))
            .child(
                createCredentialItemArea(open, outputs).pos(0, 24)
                    .size(PACKAGING_WIDTH, 48))
            .child(
                new ScrollingTextWidget(IKey.dynamic(() -> StatCollector.translateToLocal(status.getStringValue())))
                    .pos(0, 74)
                    .size(PACKAGING_WIDTH - 30, 9)
                    .shadow(false)
                    .color(skin::workspaceMuted)
                    .setEnabledIf(
                        widget -> !"tst.bottledchaos.gui.credential_injection.missing_target"
                            .equals(status.getStringValue())));
    }

    private Flow createCredentialItemArea(BooleanSupplier panelOpen, BottledChaosCraftingOutput outputs) {
        return Flow.row()
            .fullWidth()
            .height(48)
            .childPadding(4)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .child(
                createCredentialSingleSlotArea(
                    "tst.bottledchaos.gui.credential_injection.frame",
                    CARRIER_SLOT,
                    "tst.bottledchaos.gui.credential_injection.target",
                    true,
                    panelOpen))
            .child(
                Flow.column()
                    .size(80, 48)
                    .padding(2)
                    .background(IDrawable.EMPTY)
                    .crossAxisAlignment(Alignment.CrossAxis.CENTER)
                    .child(
                        new TextWidget<>(IKey.lang("tst.bottledchaos.gui.credential_injection.item_materials"))
                            .textAlign(Alignment.Center)
                            .color(skin::workspaceText)
                            .shadow(false)
                            .fullWidth()
                            .height(8))
                    .child(createCredentialSourceSlots(panelOpen)))
            .child(createCredentialOutputArea(outputs));
    }

    private IWidget createCredentialOutputArea(BottledChaosCraftingOutput outputs) {
        return Flow.column()
            .size(42, 48)
            .padding(3)
            .background(IDrawable.EMPTY)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(
                new TextWidget<>(IKey.lang("GTPP.nei.result")).textAlign(Alignment.Center)
                    .color(skin::workspaceText)
                    .shadow(false)
                    .fullWidth()
                    .height(10))
            .child(
                Flow.row()
                    .size(36, 18)
                    .child(createCredentialOutputSlot(OUTPUT_SLOT, outputs))
                    .child(createCredentialOutputSlot(SECOND_OUTPUT_SLOT, outputs)));
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
            .background(IDrawable.EMPTY)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(
                new TextWidget<>(IKey.lang(labelKey)).textAlign(Alignment.Center)
                    .color(skin::workspaceText)
                    .shadow(false)
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
                            MATERIAL_SLOT_START + index,
                            panelOpen).slotGroup(CREDENTIAL_SLOT_GROUP))
                    .tooltip(
                        tooltip -> tooltip.addLine(IKey.lang("tst.bottledchaos.gui.credential_injection.source"))));
    }

    private ParentWidget<?> createModuleConfiguration(PanelSyncManager syncManager) {
        BooleanSupplier panelOpen = () -> syncManager.getModularSyncManager()
            .isOpen() && terminalPage.getIntValue() == 2;
        ModuleBrowser browser = new ModuleBrowser(syncManager, panelOpen);
        Flow rows = Flow.column()
            .fullWidth()
            .height(150);
        for (int index = 0; index < ModuleBrowser.PAGE_SIZE; index++) {
            final int row = index;
            rows.child(
                Flow.row()
                    .fullWidth()
                    .height(25)
                    .childPadding(2)
                    .collapseDisabledChild()
                    .child(
                        new ButtonWidget<>().expanded()
                            .height(23)
                            .background(
                                new DynamicDrawable(() -> browser.isSelected(row) ? skin.SELECTED : skin.BUTTON))
                            .disableHoverBackground()
                            .setEnabledIf(widget -> browser.row(row) >= 0)
                            .child(
                                Flow.column()
                                    .full()
                                    .padding(4, 2)
                                    .collapseDisabledChild()
                                    .mainAxisAlignment(Alignment.MainAxis.CENTER)
                                    .child(
                                        new ScrollingTextWidget(IKey.dynamic(() -> browser.rowName(row))).fullWidth()
                                            .height(10)
                                            .shadow(false)
                                            .color(skin::buttonText))
                                    .child(
                                        new ScrollingTextWidget(IKey.dynamic(() -> browser.rowState(row))).fullWidth()
                                            .height(10)
                                            .shadow(false)
                                            .color(() -> browser.rowStateColor(row))
                                            .setEnabledIf(
                                                widget -> !browser.rowState(row)
                                                    .isEmpty())))
                            .onMousePressed(click -> {
                                browser.select(browser.row(row));
                                browser.selectCurrent();
                                return true;
                            })
                            .tooltipBuilder(tooltip -> {
                                int slot = browser.row(row);
                                if (slot >= 0) {
                                    tooltip.addLine(IKey.str(BottledChaosMachineConfiguration.poolName(slot)));
                                    tooltip.addLine(IKey.str(browser.groups(slot)));
                                }
                            })
                            .tooltipAutoUpdate(true)));
        }
        Flow navigation = Flow.row()
            .fullWidth()
            .height(18)
            .childPadding(1)
            .child(
                new ButtonWidget<>().width(18)
                    .height(18)
                    .background(skin.BUTTON)
                    .hoverBackground(skin.HOVER)
                    .overlay(
                        IKey.str("<")
                            .color(skin::buttonText))
                    .onMousePressed(click -> {
                        browser.turnPage(-1);
                        return true;
                    }))
            .child(
                IKey.dynamic(() -> (browser.page + 1) + "/" + browser.pageCount())
                    .asWidget()
                    .expanded()
                    .height(18)
                    .shadow(false)
                    .color(MODULE_TEXT)
                    .textAlign(Alignment.Center))
            .child(
                new ButtonWidget<>().width(18)
                    .height(18)
                    .background(skin.BUTTON)
                    .hoverBackground(skin.HOVER)
                    .overlay(
                        IKey.str(">")
                            .color(skin::buttonText))
                    .onMousePressed(click -> {
                        browser.turnPage(1);
                        return true;
                    }));
        int detailsWidth = TERMINAL_WIDTH - MODULE_LIST_WIDTH - 20;
        Flow details = Flow.column()
            .pos(MODULE_LIST_WIDTH + 14, 6)
            .size(detailsWidth, TERMINAL_HEIGHT - 12)
            .child(
                Flow.row()
                    .fullWidth()
                    .height(40)
                    .childPadding(6)
                    .child(
                        new ButtonWidget<>().size(18, 18)
                            .background(skin.SLOT)
                            .overlay(new DynamicDrawable(() -> {
                                ItemStack stack = browser.stack(browser.selected.getIntValue());
                                return stack == null ? IDrawable.EMPTY : new ItemDrawable(stack);
                            }))
                            .onMousePressed(click -> {
                                browser.nextSource();
                                return true;
                            }))
                    .child(
                        Flow.column()
                            .width(detailsWidth - 26)
                            .height(24)
                            .childPadding(2)
                            .child(
                                new ScrollingTextWidget(IKey.dynamic(browser::heading)).fullWidth()
                                    .height(11)
                                    .shadow(false)
                                    .color(MODULE_TEXT))
                            .child(
                                new ScrollingTextWidget(
                                    IKey.dynamic(
                                        () -> browser.selected.getIntValue() < 0 ? ""
                                            : "x" + browser.quantity(browser.selected.getIntValue()))).fullWidth()
                                                .height(10)
                                                .shadow(false)
                                                .color(MODULE_MUTED))))
            .child(
                new Rectangle().color(0xFF3E6570)
                    .asWidget()
                    .fullWidth()
                    .height(1)
                    .margin(0, 4))
            .child(
                new ModuleDetails(browser, detailsWidth - 11).fullWidth()
                    .expanded()
                    .paddingRight(5)
                    .showScrollShadows(false));
        return new ParentWidget<>().size(TERMINAL_WIDTH, TERMINAL_HEIGHT)
            .child(
                Flow.column()
                    .pos(6, 6)
                    .size(MODULE_LIST_WIDTH, TERMINAL_HEIGHT - 12)
                    .childPadding(2)
                    .child(rows)
                    .child(navigation))
            .child(details);
    }

    private ParentWidget<?> createModuleInventory(PanelSyncManager manager) {
        ParentWidget<?> page = new ParentWidget<>().size(TERMINAL_WIDTH, TERMINAL_HEIGHT);
        for (int index = 0; index < BottledChaosMachineConfiguration.MODULE_SLOTS; index++) {
            final int slot = index;
            int left = 6 + (index % 6) * 70;
            int top = 6 + (index / 6) * 43;
            page.child(
                new ItemSlot().size(18, 18)
                    .pos(left + 25, top)
                    .syncHandler(
                        new MachineSlotSyncHandler(
                            createConfigurationSlot(
                                multiblock.getMachineConfiguration()
                                    .machines(),
                                slot,
                                () -> terminalPage.getIntValue() == 3).ignoreMaxStackSize(true)
                                    .singletonSlotGroup())));
            page.child(
                IKey.dynamic(
                    () -> TSTTextLayout.ellipsize(
                        BottledChaosMachineConfiguration.moduleName(
                            multiblock.getMachineConfiguration()
                                .machines()
                                .getStackInSlot(slot)),
                        64,
                        TextRenderer.getFontRenderer()::getStringWidth))
                    .asWidget()
                    .pos(left, top + 22)
                    .size(68, 13)
                    .background(skin.BUTTON)
                    .color(skin::buttonText)
                    .shadow(false)
                    .textAlign(Alignment.Center));
        }
        return page;
    }

    private IWidget createPassiveInputs(PanelSyncManager manager) {
        var inputs = new StringSyncValue(multiblock::getPassiveInputText);
        manager.syncValue("bottledChaosPassiveInputs", inputs);
        return GTGuiTextures.BUTTON_BOXED_EXCLAMATION_POINT_18x18.asWidget()
            .size(18, 18)
            .pos(PANEL_WIDTH - 48, TABS_TOP)
            .tooltipBuilder(tooltip -> {
                tooltip.addLine(IKey.lang("tst.bottledchaos.gui.passive_inputs"));
                for (String line : inputs.getStringValue()
                    .split("\n")) tooltip.addLine(IKey.str(line));
            })
            .tooltipAutoUpdate(true);
    }

    private static final class ModuleDetails extends ListWidget<IWidget, ModuleDetails> {

        private static final float TEXT_SCALE = 1f;
        private final ModuleBrowser browser;
        private int slot = -2;
        private final int width;
        private String text = "";

        private ModuleDetails(ModuleBrowser browser, int width) {
            this.browser = browser;
            this.width = width;
            crossAxisAlignment(Alignment.CrossAxis.START);
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void onUpdate() {
            super.onUpdate();
            // A fixed content width and left alignment avoid first-open scrollbar/layout shifts.
            String next = EnumChatFormatting.getTextWithoutFormattingCodes(browser.detailsText());
            int selected = browser.selected.getIntValue();
            if (slot == selected && text.equals(next)) return;
            if (slot != selected) scrollDirection(new VerticalScrollData());
            slot = selected;
            text = next;
            removeAll();
            var font = TextRenderer.getFontRenderer();
            int lineHeight = (int) Math.ceil(font.FONT_HEIGHT * TEXT_SCALE) + 2;
            int labelWidth = Math.min(58, width / 3);
            int valueWidth = width - labelWidth - 6;
            boolean upkeep = false;
            for (String line : text.split("\n")) {
                if (line.isEmpty()) continue;
                if (line.startsWith("---")) {
                    upkeep = true;
                    child(
                        new Rectangle().color(0xFF3E6570)
                            .asWidget()
                            .width(width)
                            .height(1)
                            .margin(0, 4));
                    continue;
                }
                int colon = labelEnd(line);
                boolean heat = colon >= 0 && line.substring(0, colon)
                    .equals(heatLabel());
                int labelColor = heat ? 0xFFFF5555 : MODULE_MUTED;
                int valueColor = upkeep ? 0xFF7DD1C0 : parameterColor(colon < 0 ? "" : line.substring(0, colon));
                if (colon < 0) {
                    child(
                        textColumn(
                            wrap(line, width - 2, value -> (int) Math.ceil(font.getStringWidth(value) * TEXT_SCALE)),
                            width,
                            lineHeight,
                            upkeep ? valueColor : MODULE_MUTED,
                            Alignment.TopLeft).marginBottom(6));
                } else {
                    boolean stacked = width < 150 || line.indexOf(';', colon) >= 0
                        || font.getStringWidth(
                            line.substring(colon + 1)
                                .trim())
                            * TEXT_SCALE > valueWidth - 2;
                    List<String> labels = wrap(
                        line.substring(0, colon),
                        (stacked ? width : labelWidth) - 2,
                        value -> (int) Math.ceil(font.getStringWidth(value) * TEXT_SCALE));
                    List<String> values = wrap(
                        line.substring(colon + 1)
                            .trim(),
                        (stacked ? width : valueWidth) - 2,
                        value -> (int) Math.ceil(font.getStringWidth(value) * TEXT_SCALE));
                    if (stacked) {
                        child(
                            Flow.column()
                                .width(width)
                                .height((labels.size() + values.size()) * lineHeight + 2)
                                .childPadding(2)
                                .marginBottom(7)
                                .child(textColumn(labels, width, lineHeight, labelColor, Alignment.TopLeft))
                                .child(textColumn(values, width, lineHeight, valueColor, Alignment.TopLeft)));
                        continue;
                    }
                    child(
                        Flow.row()
                            .width(width)
                            .height(Math.max(labels.size(), values.size()) * lineHeight)
                            .childPadding(6)
                            .marginBottom(5)
                            .crossAxisAlignment(Alignment.CrossAxis.START)
                            .child(textColumn(labels, labelWidth, lineHeight, labelColor, Alignment.TopLeft))
                            .child(textColumn(values, valueWidth, lineHeight, valueColor, Alignment.TopRight)));
                }
            }
            scheduleResize();
        }

        /** Break descriptions at semicolons, then hard-wrap long clauses by rendered width. */
        private static List<String> wrap(String text, int width, ToIntFunction<String> measure) {
            List<String> lines = new ArrayList<>();
            for (String paragraph : text.replace("; ", "\n")
                .replace(';', '\n')
                .split("\n", -1)) {
                if (paragraph.isEmpty()) {
                    lines.add("");
                    continue;
                }
                int start = 0;
                while (start < paragraph.length()) {
                    int end = start;
                    while (end < paragraph.length()) {
                        int next = paragraph.offsetByCodePoints(end, 1);
                        if (end > start && measure.applyAsInt(paragraph.substring(start, next)) > Math.max(1, width))
                            break;
                        end = next;
                    }
                    lines.add(paragraph.substring(start, end));
                    start = end;
                }
            }
            return lines;
        }

        private static int parameterColor(String label) {
            if (label.equals(parameterLabel("parallel"))) return 0xFF8EC6FF;
            if (label.equals(parameterLabel("eu"))) return 0xFFEDB46B;
            if (label.equals(parameterLabel("time"))) return 0xFF7BD4DD;
            if (label.equals(parameterLabel("oc"))) return 0xFFD9A4ED;
            if (label.equals(parameterLabel("limits")) || label.equals(parameterLabel("heat_limit"))) return 0xFFE4CD84;
            return MODULE_TEXT;
        }

        private static int labelEnd(String text) {
            int colon = text.indexOf(':');
            return colon >= 0 ? colon : text.indexOf('\uFF1A');
        }

        private static String heatLabel() {
            String text = EnumChatFormatting
                .getTextWithoutFormattingCodes(StatCollector.translateToLocalFormatted("GT5U.multiblock.heat", ""));
            int colon = labelEnd(text);
            return colon < 0 ? text : text.substring(0, colon);
        }

        private static String parameterLabel(String key) {
            String text = StatCollector.translateToLocal("tst.bottledchaos.machine_module." + key);
            int colon = labelEnd(text);
            return colon < 0 ? text : text.substring(0, colon);
        }

        private static Flow textColumn(List<String> lines, int width, int lineHeight, int color, Alignment alignment) {
            Flow column = Flow.column()
                .size(width, lines.size() * lineHeight);
            for (String line : lines) column.child(
                IKey.str(line)
                    .asWidget()
                    .size(width, lineHeight)
                    .scale(TEXT_SCALE)
                    .shadow(false)
                    .color(color)
                    .textAlign(alignment));
            return column;
        }
    }

    // #tr tst.bottledchaos.gui.pool_users
    // # Processing groups: %s
    // #zh_CN 加工组: %s

    // #tr tst.bottledchaos.gui.pool_unused
    // # No processing group uses this recipe pool.
    // #zh_CN 没有加工组使用此配方池.

    // #tr tst.bottledchaos.gui.module_unused
    // # Unused
    // #zh_CN 未使用

    // #tr tst.bottledchaos.gui.module_missing
    // # Not installed
    // #zh_CN 未安装

    // #tr tst.bottledchaos.gui.plant_shortage
    // # Insufficient main purification plants
    // #zh_CN 主净化厂数量不足

    // #tr tst.bottledchaos.gui.plant_required
    // # Required quantity: %s
    // #zh_CN 所需数量: %s

    // #tr tst.bottledchaos.gui.module_empty
    // # No recipe pools.
    // #zh_CN 暂无配方池.

    // #tr tst.bottledchaos.gui.upkeep
    // # Maintenance fluids / second
    // #zh_CN 每秒维护流体

    private final class ModuleBrowser {

        private static final int PAGE_SIZE = 6;
        private final List<Integer> rows = new ArrayList<>();
        private long visibleRows;
        private long missingRows;
        private long usedRows;
        private int page;
        private int selectedRow = -1;
        private final LongSyncValue required;
        private final LongSyncValue missing;
        private final IntSyncValue[] users = new IntSyncValue[BottledChaosMachineConfiguration.poolCount()];
        private final IntSyncValue selected;
        private final IntSyncValue source;
        private int selectedSource;
        private final GenericSyncValue<NBTTagCompound, ?> parameters;
        private final long waterPools;
        private final boolean client;
        private int selectedSlot = -1;
        private int plantSlot = -1;
        private long lastSnapshotTick = Long.MIN_VALUE;
        private NBTTagCompound snapshot = new NBTTagCompound();
        private ItemStack snapshotStack;
        private ItemStack describedStack;
        private NBTTagCompound describedParameters;
        private int describedSlot = -2;
        private String description = "";

        private ModuleBrowser(PanelSyncManager syncManager, BooleanSupplier panelOpen) {
            client = syncManager.isClient();
            required = new LongSyncValue(
                () -> multiblock.getMachineConfiguration()
                    .requiredPoolMask(),
                multiblock.getMachineConfiguration()::setRequiredPoolMask);
            syncManager.syncValue("bottledChaosRequiredPools", required);
            missing = new LongSyncValue(multiblock::getMissingRecipePoolMask);
            syncManager.syncValue("bottledChaosMissingPools", missing);
            selected = new IntSyncValue(() -> selectedSlot, value -> {
                if (value < -1 || value >= users.length) return;
                selectedSlot = value;
                lastSnapshotTick = Long.MIN_VALUE;
            }).allowC2S();
            syncManager.syncValue("bottledChaosSelectedPool", selected);
            source = new IntSyncValue(() -> selectedSource, value -> {
                selectedSource = Math.max(0, value);
                lastSnapshotTick = Long.MIN_VALUE;
            }).allowC2S();
            syncManager.syncValue("bottledChaosSelectedSource", source);
            parameters = GenericSyncValue.builder(NBTTagCompound.class)
                .getter(() -> {
                    if (!panelOpen.getAsBoolean()) return snapshot;
                    long tick = multiblock.getBaseMetaTileEntity()
                        .getWorld()
                        .getTotalWorldTime();
                    if (lastSnapshotTick == Long.MIN_VALUE
                        || !ItemStack.areItemStacksEqual(snapshotStack, stack(selectedSlot))
                        || tick - lastSnapshotTick >= 10) {
                        snapshot = multiblock.getModuleParameters(selectedSlot, selectedSource);
                        snapshotStack = stack(selectedSlot) == null ? null : stack(selectedSlot).copy();
                        lastSnapshotTick = tick;
                    }
                    return snapshot;
                })
                .adapter(ByteBufAdapters.NBT)
                .copy(tag -> (NBTTagCompound) tag.copy())
                .build();
            syncManager.syncValue("bottledChaosModuleParameters", parameters);
            long water = 0;
            for (int slot = 0; slot < users.length; slot++) {
                final int poolSlot = slot;
                users[slot] = new IntSyncValue(() -> multiblock.getRecipePoolUsers(poolSlot));
                syncManager.syncValue("bottledChaosPoolUsers" + slot, users[slot]);
                if (BottledChaosMachineConfiguration.isPurificationPlantSlot(slot)) plantSlot = slot;
                if (BottledChaosMachineConfiguration.isPurificationPool(BottledChaosMachineConfiguration.pool(slot)))
                    water |= 1L << slot;
            }
            waterPools = water;
        }

        private ItemStack stack(int slot) {
            return multiblock.getMachineConfiguration()
                .poolStack(slot, slot == selected.getIntValue() ? source.getIntValue() : 0);
        }

        private void nextSource() {
            int count = multiblock.getMachineConfiguration()
                .displaySourcesForPool(selected.getIntValue()).length;
            if (count > 1) source.setIntValue((source.getIntValue() + 1) % count, true, true);
        }

        private long quantity(int slot) {
            ItemStack stack = stack(slot);
            return stack == null ? 0
                : ItemBottledChaosMachineModule.isModule(stack)
                    ? Math.max(0, ItemBottledChaosMachineModule.quantity(stack))
                    : stack.stackSize;
        }

        private int row(int row) {
            long visible = 0;
            for (int slot = 0; slot < users.length; slot++)
                if (BottledChaosMachineConfiguration.displayPoolSlot(slot) == slot
                    && (stack(slot) != null || users[slot].getIntValue() != 0)) visible |= 1L << slot;
            long missing = 0, used = 0;
            for (int slot = 0; slot < users.length; slot++) {
                if (users[slot].getIntValue() != 0) {
                    if (isMissing(slot)) missing |= 1L << slot;
                    else used |= 1L << slot;
                }
            }
            updateRows(visible, missing, used);
            selectCurrent();
            return pageRow(row);
        }

        /** Preserve selection while ordering missing pools, used pools, then unused pools. */
        private void updateRows(long visible, long missing, long used) {
            if (visibleRows == visible && missingRows == missing && usedRows == used) return;
            visibleRows = visible;
            missingRows = missing;
            usedRows = used;
            rows.clear();
            for (long section : new long[] { visible & missing, visible & ~missing & used,
                visible & ~(missing | used) })
                for (int pool = 0; pool < Long.SIZE; pool++) if ((section & (1L << pool)) != 0) rows.add(pool);
            int selectedIndex = rows.indexOf(selectedRow);
            if (selectedIndex >= 0) page = selectedIndex / PAGE_SIZE;
            page = Math.min(page, pageCount() - 1);
            if (selectedIndex < 0) selectedRow = pageRow(0);
        }

        private void select(int slot) {
            if (rows.indexOf(slot) / PAGE_SIZE == page && rows.contains(slot)) selectedRow = slot;
        }

        private int pageRow(int index) {
            int offset = page * PAGE_SIZE + index;
            return offset < rows.size() ? rows.get(offset) : -1;
        }

        private int pageCount() {
            return Math.max(1, (rows.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        }

        private void selectCurrent() {
            if (client && selected.getIntValue() != selectedRow) selected.setIntValue(selectedRow, true, true);
        }

        private void turnPage(int direction) {
            row(0);
            page = Math.max(0, Math.min(pageCount() - 1, page + direction));
            selectedRow = pageRow(0);
            selectCurrent();
        }

        private String groups(int slot) {
            int mask = users[slot].getIntValue();
            return mask == 0 ? StatCollector.translateToLocal("tst.bottledchaos.gui.pool_unused")
                : StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.gui.pool_users",
                    MM_BottledChaos.formatProcessingGroups(mask));
        }

        private String rowName(int row) {
            int slot = row(row);
            if (slot < 0) return "";
            return BottledChaosMachineConfiguration.poolName(slot);
        }

        private boolean isSelected(int row) {
            int slot = row(row);
            return slot >= 0 && slot == selected.getIntValue();
        }

        private int rowStateColor(int row) {
            int slot = row(row);
            return slot >= 0 && (isMissing(slot) || users[slot].getIntValue() == 0) ? MODULE_UNUSED : MODULE_WARNING;
        }

        private boolean isMissing(int slot) {
            return (missing.getLongValue() & (1L << slot)) != 0 || quantity(slot) <= 0;
        }

        private String rowState(int row) {
            int slot = row(row);
            if (slot < 0) return "";
            if (isMissing(slot)) return StatCollector.translateToLocal("tst.bottledchaos.gui.module_missing");
            if (users[slot].getIntValue() == 0)
                return StatCollector.translateToLocal("tst.bottledchaos.gui.module_unused");
            String state = "";
            if ((waterPools & (1L << slot)) != 0 && quantity(slot) > quantity(plantSlot))
                state = StatCollector.translateToLocal("tst.bottledchaos.gui.plant_shortage");
            if (slot == plantSlot && quantity(slot) > 0) {
                for (int pool = 0; pool < users.length; pool++)
                    if ((waterPools & required.getLongValue() & (1L << pool)) != 0 && quantity(pool) > quantity(slot))
                        return StatCollector.translateToLocal("tst.bottledchaos.gui.plant_shortage");
            }
            return state;
        }

        private String heading() {
            int slot = selected.getIntValue();
            return slot < 0 ? "" : BottledChaosMachineConfiguration.poolName(slot);
        }

        private String detailsText() {
            int slot = selected.getIntValue();
            if (slot < 0) return StatCollector.translateToLocal("tst.bottledchaos.gui.module_empty");
            NBTTagCompound data = parameters.getValue();
            String result = users[slot].getIntValue() == 0 ? "" : groups(slot);
            if (data == null || data.getInteger("slot") != slot) return result;
            if (BottledChaosMachineConfiguration.isPurificationPlantSlot(slot))
                return result + (result.isEmpty() ? "" : "\n\n")
                    + StatCollector
                        .translateToLocalFormatted("tst.bottledchaos.gui.plant_required", data.getLong("needed"));
            ItemStack stack = stack(slot);
            if (describedSlot != slot || !ItemStack.areItemStacksEqual(describedStack, stack)
                || !data.equals(describedParameters)) {
                describedSlot = slot;
                describedStack = stack == null ? null : stack.copy();
                describedParameters = (NBTTagCompound) data.copy();
                description = "";
                if (ItemBottledChaosMachineModule.isModule(stack)) {
                    var structure = ItemBottledChaosMachineModule.structure(stack);
                    if (structure != null) {
                        List<String> lines = BottledChaosMachineDescriptions.describeForGui(
                            structure.controller(),
                            structure,
                            BottledChaosMachineConfiguration.pool(data.getInteger("sourcePool")),
                            ItemBottledChaosMachineModule.quantitySpeedExponent(quantity(slot)),
                            data);
                        description = String.join("\n", lines);
                    }
                } else if (GTCMItemList.TestItem0.equal(stack) && data.getBoolean("debug")) {
                    description = String.join(
                        "\n",
                        BottledChaosMachineDescriptions.describeForGui(
                            stack,
                            null,
                            BottledChaosMachineConfiguration.pool(slot),
                            ItemBottledChaosMachineModule.quantitySpeedExponent(quantity(slot)),
                            data));
                }
            }
            if (!description.isEmpty()) result += (result.isEmpty() ? "" : "\n\n") + description;
            NBTTagList upkeep = data.getTagList("upkeep", 10);
            StringJoiner rates = new StringJoiner("\n");
            for (int index = 0; index < upkeep.tagCount(); index++) {
                NBTTagCompound entry = upkeep.getCompoundTagAt(index);
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(entry);
                if (fluid != null) rates.add(fluid.getLocalizedName() + ": " + entry.getString("perSecond") + " L/s");
            }
            if (rates.length() > 0) result += "\n\n------------------------\n"
                + StatCollector.translateToLocal("tst.bottledchaos.gui.upkeep")
                + "\n"
                + rates;
            return result;
        }
    }

    /** Sends an integer quantity after native slot packets, only when a machine slot changes. */
    private static final class MachineSlotSyncHandler extends ItemSlotSH {

        private static final int SYNC_MACHINE_STACK = 2;
        private boolean machineStackChanged;

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

        @Override
        protected void onSlotUpdate(ItemStack stack, boolean onlyAmountChanged, boolean client, boolean init) {
            super.onSlotUpdate(stack, onlyAmountChanged, client, init);
            if (!client) machineStackChanged = true;
        }

        private void syncMachineStack(boolean force) {
            if (!isValid() || getSyncManager().isClient()) return;
            if (!force && !machineStackChanged) return;
            machineStackChanged = false;
            ItemStack stack = getSlot().getStack();
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

    @SuppressWarnings("unchecked")
    private IWidget createPowerAllocationButton(PanelSyncManager syncManager) {
        GenericSyncValue<NBTTagCompound, ?> status = syncManager
            .findSyncHandler("bottledChaosStatusSummary", GenericSyncValue.class);
        BooleanSupplier wireless = () -> status.getValue() != null && status.getValue()
            .getBoolean("wireless");
        BooleanSyncValue mode = syncManager.findSyncHandler("bottledChaosConcentratedPower", BooleanSyncValue.class);
        BooleanSyncValue activeMode = syncManager
            .findSyncHandler("bottledChaosActiveConcentratedPower", BooleanSyncValue.class);
        return createDisableableToggleButton(
            mode,
            () -> !wireless.getAsBoolean(),
            GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF,
            GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON,
            tooltip -> {
                if (wireless.getAsBoolean()) {
                    tooltip.addLine(IKey.lang("tst.bottledchaos.wireless_mode"));
                    return;
                }
                tooltip.addLine(IKey.lang("tst.bottledchaos.gui.power_allocation"));
                tooltip.addLine(
                    IKey.dynamic(
                        () -> EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                            "tst.bottledchaos.gui.power_allocation.current",
                            StatCollector.translateToLocal(
                                activeMode.getBoolValue() ? "tst.bottledchaos.gui.power_allocation.concentrated"
                                    : "tst.bottledchaos.gui.power_allocation.distributed"))));
                tooltip.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocal(
                            activeMode.getBoolValue() ? "tst.bottledchaos.gui.power_allocation.concentrated.hint"
                                : "tst.bottledchaos.gui.power_allocation.distributed.hint")));
                if (mode.getBoolValue() != activeMode.getBoolValue()) {
                    tooltip.addLine(
                        IKey.dynamic(
                            () -> EnumChatFormatting.YELLOW
                                + StatCollector.translateToLocal("tst.bottledchaos.gui.power_allocation.pending")));
                }
            }).tooltipAutoUpdate(true);
    }
}
