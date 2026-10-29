package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_BottledChaosRecipeTokenHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosStatus;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTextLayout;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

/** Fixed 5 by 3 hatch overview. All positions are relative to the host terminal. */
final class BottledChaosHatchPage {

    // #tr tst.bottledchaos.gui.hatches_tab
    // # Credential Management
    // #zh_CN 凭证管理

    // #tr tst.bottledchaos.gui.hatch_number
    // # Credential Hatch #%s
    // #zh_CN 凭证仓 #%s

    // #tr tst.bottledchaos.gui.cache_count
    // # Cache: %s types
    // #zh_CN 缓存: %s种

    // #tr tst.bottledchaos.gui.hatch_running_state
    // # Operating status: %s
    // #zh_CN 运行状态: %s

    // #tr tst.bottledchaos.gui.cache_empty
    // # Cache empty
    // #zh_CN 缓存为空

    // #tr tst.bottledchaos.gui.loading
    // # Loading...
    // #zh_CN 正在同步...

    private static NBTTagCompound hatch(NBTTagCompound summary, int slot) {
        return summary == null ? new NBTTagCompound()
            : summary.getTagList("hatches", 10)
                .getCompoundTagAt(slot);
    }

    static ParentWidget<?> create(MM_BottledChaos machine, ModularPanel parent, PanelSyncManager manager,
        BooleanSupplier open, BottledChaosGuiSkin skin) {
        int[] selectedSlot = { 0 };
        IntSyncValue selected = new IntSyncValue(
            () -> selectedSlot[0],
            value -> selectedSlot[0] = Math.max(0, Math.min(MM_BottledChaos.THREAD_COUNT - 1, value))).allowC2S();
        manager.syncValue("bottledChaosSelectedHatch", selected);
        boolean[] receivedSummary = { false };
        GenericSyncValue<NBTTagCompound, ?> summary = GenericSyncValue.builder(NBTTagCompound.class)
            .getter(machine::getCredentialHatchSummary)
            .setter(tag -> receivedSummary[0] = true)
            .adapter(ByteBufAdapters.NBT)
            .copy(tag -> (NBTTagCompound) tag.copy())
            .build();
        manager.syncValue("bottledChaosHatchSummary", summary);
        int[] hoveredSlot = { -1 };
        IntSyncValue hovered = new IntSyncValue(
            () -> hoveredSlot[0],
            value -> hoveredSlot[0] = value >= 0 && value < MM_BottledChaos.THREAD_COUNT ? value : -1).allowC2S();
        manager.syncValue("bottledChaosHoveredCache", hovered);
        GenericSyncValue<NBTTagCompound, ?> details = GenericSyncValue.builder(NBTTagCompound.class)
            .getter(
                () -> open.getAsBoolean() ? machine.getCredentialHatchDetails(hovered.getIntValue())
                    : new NBTTagCompound())
            .adapter(ByteBufAdapters.NBT)
            .copy(tag -> (NBTTagCompound) tag.copy())
            .build();
        manager.syncValue("bottledChaosHoveredCacheDetails", details);
        Supplier<NBTTagCompound> current = () -> hatch(summary.getValue(), selected.getIntValue());
        Supplier<TST_BottledChaosRecipeTokenHatch> target = () -> open.getAsBoolean()
            ? machine.getManagedCredentialHatch(selected.getIntValue())
            : null;
        ParentWidget<?> page = new ParentWidget<>().size(432, 183);
        for (int slot = 0; slot < MM_BottledChaos.THREAD_COUNT; slot++) {
            int index = slot;
            Supplier<NBTTagCompound> data = () -> hatch(summary.getValue(), index);
            Runnable select = () -> selected.setIntValue(index, true, true);
            ParentWidget<?> content = new ParentWidget<>().size(80, 31)
                .child(
                    label(() -> "#" + (index + 1), 30).color(skin::buttonText)
                        .pos(5, 4))
                .child(
                    indicator(
                        () -> operatingColor(data.get()),
                        () -> Collections.singletonList(
                            StatCollector.translateToLocalFormatted(
                                "tst.bottledchaos.gui.hatch_running_state",
                                status(data.get()))),
                        select).pos(55, 4))
                .child(
                    indicator(
                        () -> !data.get()
                            .getBoolean("installed") ? 0xFF52616A
                                : data.get()
                                    .getInteger("cacheTypes") > 0 ? 0xFF39DCEC : 0xFF52616A,
                        () -> cacheLines(data.get(), details.getValue(), index),
                        select).pos(67, 4)
                            .onUpdateListener(widget -> {
                                if (open.getAsBoolean() && widget.isHovering()) {
                                    if (hovered.getIntValue() != index) hovered.setIntValue(index, true, true);
                                } else if (hovered.getIntValue() == index) hovered.setIntValue(-1, true, true);
                            }))
                .child(
                    label(() -> fit(family(data.get()), 70), 70).color(skin::buttonText)
                        .pos(5, 19));
            page.child(
                new ButtonWidget<>().pos(6 + slot % 5 * 84, 7 + slot / 5 * 35)
                    .size(80, 31)
                    .background(
                        new DynamicDrawable(() -> selected.getIntValue() == index ? skin.SELECTED : skin.BUTTON))
                    .disableHoverBackground()
                    .onMousePressed(mouse -> {
                        if (mouse != 0) return false;
                        select.run();
                        return true;
                    })
                    .tooltipBuilder(tooltip -> tooltip.addLine(family(data.get())))
                    .tooltipAutoUpdate(true)
                    .child(content));
        }
        page.child(
            new Rectangle().color(0xFF3E6570)
                .asWidget()
                .pos(6, 114)
                .size(419, 1));
        page.child(
            label(
                () -> StatCollector
                    .translateToLocalFormatted("tst.bottledchaos.gui.hatch_number", selected.getIntValue() + 1),
                150).pos(7, 124));
        List<ButtonWidget<?>> controls = BottledChaosHatchControls.create(
            parent,
            manager,
            "bottledChaosManagedHatch",
            target,
            () -> current.get()
                .getBoolean("enabled"),
            true,
            BottledChaosHatchControls.Action.values());
        for (int i = 0; i < controls.size(); i++) {
            BottledChaosHatchControls.Action action = BottledChaosHatchControls.Action.values()[i];
            page.child(
                controls.get(i)
                    .pos(164 + i * 53, 120)
                    .size(48, 18)
                    .background(skin.BUTTON)
                    .hoverBackground(skin.HOVER)
                    .overlay(
                        IKey.dynamic(
                            () -> StatCollector.translateToLocal(
                                action.label(
                                    current.get()
                                        .getBoolean("enabled"))))
                            .color(skin::buttonText))
                    .setEnabledIf(
                        widget -> current.get()
                            .getBoolean("installed")
                            && (action != BottledChaosHatchControls.Action.TAKE || current.get()
                                .getBoolean("hasCredential"))));
        }
        page.child(label(() -> fit(family(current.get()), 268), 268).pos(7, 146));
        page.child(label(() -> {
            String share = MM_BottledChaos.getProcessingGroupShareValue(current.get());
            int peers = current.get()
                .getInteger("boundSlots");
            return fit(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted("tst.bottledchaos.input_share", share)
                    + (peers == 0 ? ""
                        : EnumChatFormatting.GRAY + " | "
                            + StatCollector.translateToLocalFormatted(
                                "tst.bottledchaos.credential_hatch.bound_inputs",
                                MM_BottledChaos.formatProcessingGroups(peers))),
                418);
        }, 418).pos(7, 161)
            .setEnabledIf(
                widget -> current.get()
                    .getBoolean("installed")));
        // The host already synchronizes this bounded, short-lived result for the runtime page.
        GenericSyncValue<CheckRecipeResult, ?> export = manager
            .findSyncHandler("bottledChaosCacheExportResult", GenericSyncValue.class);
        IntSyncValue exportSlot = new IntSyncValue(machine::getCacheExportSlot);
        manager.syncValue("bottledChaosCacheExportSlot", exportSlot);
        page.child(
            label(
                () -> (export.getValue()
                    .wasSuccessful() ? EnumChatFormatting.AQUA : EnumChatFormatting.RED) + fit(
                        export.getValue()
                            .getDisplayString(),
                        418),
                418).pos(7, 173)
                    .setEnabledIf(
                        widget -> selected.getIntValue() == exportSlot.getIntValue()
                            && !CheckRecipeResultRegistry.NONE.equals(export.getValue())));
        page.setEnabled(false);
        return new ParentWidget<>().size(432, 183)
            .child(page.setEnabledIf(widget -> receivedSummary[0]))
            .child(
                label(() -> StatCollector.translateToLocal("tst.bottledchaos.gui.loading"), 418).pos(7, 7)
                    .setEnabledIf(widget -> !receivedSummary[0]));
    }

    private static TextWidget<?> label(Supplier<String> text, int width) {
        return IKey.dynamic(text)
            .asWidget()
            .size(width, 9)
            .textAlign(Alignment.TopLeft)
            .shadow(false)
            .color(0xFFE5EEF1);
    }

    private static String family(NBTTagCompound data) {
        return MM_BottledChaos.localizeKeyList(
            data.getBoolean("installed") ? data.getString("family") : "tst.bottledchaos.gui.module_missing");
    }

    @SideOnly(Side.CLIENT)
    private static List<String> cacheLines(NBTTagCompound data, NBTTagCompound details, int slot) {
        if (!data.getBoolean("installed"))
            return Collections.singletonList(StatCollector.translateToLocal("tst.bottledchaos.gui.module_missing"));
        int count = data.getInteger("cacheTypes");
        if (count == 0)
            return Collections.singletonList(StatCollector.translateToLocal("tst.bottledchaos.gui.cache_empty"));
        List<String> lines = new ArrayList<>();
        lines.add(StatCollector.translateToLocalFormatted("tst.bottledchaos.gui.cache_count", count));
        if (details != null && details.getBoolean("connected") && details.getInteger("slot") == slot + 1) {
            lines.addAll(MM_BottledChaos.getMaterialSummaryLines(details.getCompoundTag("cache"), false));
        } else lines.add(StatCollector.translateToLocal("tst.bottledchaos.gui.loading"));
        return lines;
    }

    private static int operatingColor(NBTTagCompound data) {
        return (data.getBoolean("installed") ? BottledChaosStatus.fromKey(data.getString("status"))
            : BottledChaosStatus.UNINSTALLED).indicatorColor();
    }

    private static ButtonWidget<?> indicator(IntSupplier color, Supplier<List<String>> text, Runnable select) {
        return new ButtonWidget<>().size(8, 8)
            .background(BottledChaosGuiSkin.indicator(color))
            .disableHoverBackground()
            .tooltipBuilder(
                tooltip -> text.get()
                    .forEach(tooltip::addLine))
            .tooltipAutoUpdate(true)
            .onMousePressed(mouse -> {
                if (mouse != 0) return false;
                select.run();
                return true;
            });
    }

    private static String status(NBTTagCompound data) {
        String blockedRoute = MM_BottledChaos.getBlockedRouteText(data);
        return data.getBoolean("installed")
            ? MM_BottledChaos.getStatusColor(data.getString("status")) + MM_BottledChaos.getWailaStatusText(data)
                + (blockedRoute.isEmpty() ? "" : "\n" + blockedRoute)
            : BottledChaosStatus.UNINSTALLED.textColor()
                + StatCollector.translateToLocal(BottledChaosStatus.UNINSTALLED.key());
    }

    @SideOnly(Side.CLIENT)
    private static String fit(String text, int width) {
        return TSTTextLayout.ellipsize(text, width, TextRenderer.getFontRenderer()::getStringWidth);
    }
}
