package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_LaserMeteorMiner;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class TST_Gui_LaserMeteorMiner extends MTEMultiBlockBaseGui<TST_LaserMeteorMiner> {

    private static final int DISPLAY_ROW_HEIGHT = 15;
    private static final int DISPLAY_ROW_CHAR_LIMIT = 46;

    public TST_Gui_LaserMeteorMiner(TST_LaserMeteorMiner multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("meteorReset", new ClickOnceSyncValue(multiblock::startReset).allowC2S());
        syncManager.syncValue("meteorMining", new BooleanSyncValue(multiblock::isMining));
        syncManager.syncValue("meteorRadius", new IntSyncValue(multiblock::getCurrentRadius));
        syncManager.syncValue("meteorFortune", new IntSyncValue(multiblock::getFortuneTier));
        syncManager.syncValue("meteorProgress", new DoubleSyncValue(multiblock::getProgress));
        syncManager.syncValue("meteorEta", new IntSyncValue(multiblock::getEtaSeconds));
        syncManager.syncValue("meteorBlocksPerSecond", new DoubleSyncValue(multiblock::getBlocksPerSecond));
        syncManager.syncValue(
            "meteorRecentOutputs",
            new GenericListSyncHandler<>(
                multiblock::getRecentOutputs,
                null,
                TST_Gui_LaserMeteorMiner::readOutput,
                TST_Gui_LaserMeteorMiner::writeOutput,
                (a, b) -> a.stackSize() == b.stackSize() && GTUtility.areStacksEqual(a.itemStack(), b.itemStack()),
                null));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createResetButton(syncManager));
    }

    private IWidget createResetButton(PanelSyncManager syncManager) {
        BooleanSyncValue resetSyncer = syncManager.findSyncHandler("meteorReset", BooleanSyncValue.class);
        return new ToggleButton().size(18, 18)
            .value(resetSyncer)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
            .tooltipBuilder(t -> t.addLine(IKey.lang("tst.common.machine.MeteorMiner.gui.reset")));
        // #tr tst.common.machine.MeteorMiner.gui.reset
        // # Reset machine
        // #zh_CN 重启机器
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> terminal = super.createTerminalTextWidget(syncManager, parent);

        BooleanSyncValue mining = syncManager.findSyncHandler("meteorMining", BooleanSyncValue.class);
        IntSyncValue radius = syncManager.findSyncHandler("meteorRadius", IntSyncValue.class);
        IntSyncValue fortune = syncManager.findSyncHandler("meteorFortune", IntSyncValue.class);
        DoubleSyncValue progress = syncManager.findSyncHandler("meteorProgress", DoubleSyncValue.class);
        IntSyncValue eta = syncManager.findSyncHandler("meteorEta", IntSyncValue.class);
        DoubleSyncValue blocksPerSecond = syncManager.findSyncHandler("meteorBlocksPerSecond", DoubleSyncValue.class);
        GenericListSyncHandler<ItemStackLong> recentOutputs = syncManager
            .findSyncHandler("meteorRecentOutputs", GenericListSyncHandler.class);

        // #tr tst.common.machine.MeteorMiner.gui.radius
        // # Radius: {\WHITE}%s
        // #zh_CN 当前半径: {\WHITE}%s

        // #tr tst.common.machine.MeteorMiner.gui.progress
        // # Progress: {\WHITE}%s {\GRAY}(ETA: %s)
        // #zh_CN 进度: {\WHITE}%s {\GRAY}(预计剩余: %s)

        // #tr tst.common.machine.MeteorMiner.gui.blocks_per_second
        // # Mining speed: {\WHITE}%s blocks/s
        // #zh_CN 开采速度: {\WHITE}%s 方块/秒

        // #tr tst.common.machine.MeteorMiner.gui.recent_outputs
        // # Produced in the last 5 s:
        // #zh_CN 最近5秒产出:
        terminal
            .child(
                statLine(
                    () -> StatCollector
                        .translateToLocalFormatted("tst.common.machine.MeteorMiner.gui.radius", radius.getIntValue()),
                    mining))
            .child(
                statLine(
                    () -> StatCollector.translateToLocalFormatted(
                        "tst.common.machine.MeteorMiner.gui.progress",
                        String.format("%.1f%%", progress.getDoubleValue() * 100),
                        formatEta(eta.getIntValue())),
                    mining))
            .child(
                statLine(
                    () -> StatCollector.translateToLocalFormatted(
                        "tst.common.machine.MeteorMiner.gui.blocks_per_second",
                        String.format("%.1f", blocksPerSecond.getDoubleValue())),
                    mining))
            .child(
                statLine(
                    () -> StatCollector
                        .translateToLocal("tst.common.machine.MeteorMiner.waila.fortune." + fortune.getIntValue()),
                    mining))
            .child(
                statLine(
                    () -> StatCollector.translateToLocal("tst.common.machine.MeteorMiner.gui.recent_outputs"),
                    mining));

        DynamicSyncHandler outputsHandler = new DynamicSyncHandler()
            .widgetProvider((manager, packet) -> packet == null ? new EmptyWidget() : createOutputRows(packet))
            .allowC2S();
        recentOutputs.setChangeListener(() -> outputsHandler.notifyUpdate(packet -> {
            List<ItemStackLong> outputs = recentOutputs.getValue();
            packet.writeInt(outputs.size());
            for (ItemStackLong output : outputs) writeOutput(packet, output);
        }));
        terminal.child(
            new DynamicSyncedWidget<>().widthRel(0.85f)
                .coverChildrenHeight(0)
                .syncHandler(outputsHandler)
                .setEnabledIf(widget -> mining.getBoolValue()));
        return terminal;
    }

    private static IWidget statLine(Supplier<String> text, BooleanSyncValue mining) {
        return IKey.dynamic(text::get)
            .asWidget()
            .marginBottom(2)
            .fullWidth()
            .setEnabledIf(widget -> mining.getBoolValue());
    }

    private static String formatEta(int seconds) {
        // #tr tst.common.machine.MeteorMiner.gui.eta_unknown
        // # calculating...
        // #zh_CN 计算中...
        if (seconds < 0) return StatCollector.translateToLocal("tst.common.machine.MeteorMiner.gui.eta_unknown");
        final int hours = seconds / 3600;
        final int minutes = seconds / 60 % 60;
        final int secs = seconds % 60;
        if (hours > 0) return String.format("%dh %02dm %02ds", hours, minutes, secs);
        if (minutes > 0) return String.format("%dm %02ds", minutes, secs);
        return secs + "s";
    }

    private static IWidget createOutputRows(PacketBuffer packet) {
        Flow column = Flow.column()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .coverChildren(0);
        final int count = packet.readInt();
        for (int i = 0; i < count; i++) {
            ItemStackLong output = readOutput(packet);
            if (output.itemStack() != null) column.child(createOutputRow(output));
        }
        return column;
    }

    private static IWidget createOutputRow(ItemStackLong output) {
        ItemStack stack = output.itemStack()
            .copy();
        stack.stackSize = 1;
        String amountText = EnumChatFormatting.WHITE + " x "
            + EnumChatFormatting.GOLD
            + GTUtility.formatShortenedLong(output.stackSize());
        String name = GTUtility.truncateText(
            EnumChatFormatting.AQUA + stack.getDisplayName() + EnumChatFormatting.RESET,
            DISPLAY_ROW_CHAR_LIMIT - amountText.length());
        return Flow.row()
            .fullWidth()
            .height(DISPLAY_ROW_HEIGHT)
            .child(
                new ItemDisplayWidget().disableThemeBackground(true)
                    .disableHoverThemeBackground(true)
                    .displayAmount(false)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .item(stack)
                    .size(DISPLAY_ROW_HEIGHT - 1)
                    .marginRight(2))
            .child(
                new TextWidget<>(IKey.str(name + amountText)).height(DISPLAY_ROW_HEIGHT)
                    .scale(0.75f));
    }

    private static void writeOutput(PacketBuffer packet, ItemStackLong output) {
        NetworkUtils.writeItemStack(packet, output.itemStack());
        packet.writeLong(output.stackSize());
    }

    private static ItemStackLong readOutput(PacketBuffer packet) {
        return new ItemStackLong(NetworkUtils.readItemStack(packet), packet.readLong());
    }
}
