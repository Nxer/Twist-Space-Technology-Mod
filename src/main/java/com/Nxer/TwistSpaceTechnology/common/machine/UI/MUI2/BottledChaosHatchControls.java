package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Mouse;

import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_BottledChaosRecipeTokenHatch;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;

/** Both GUIs use the same action order, labels, limit editor and server-side hatch operations. */
public final class BottledChaosHatchControls {

    // #tr tst.bottledchaos.credential_hatch.pause_button
    // # Pause
    // #zh_CN 暂停

    // #tr tst.bottledchaos.credential_hatch.resume_button
    // # Resume
    // #zh_CN 恢复

    // #tr tst.bottledchaos.credential_hatch.export_button
    // # Export cache
    // #zh_CN 排出缓存

    // #tr tst.bottledchaos.credential_hatch.clear_button
    // # Clear
    // #zh_CN 清空

    // #tr tst.bottledchaos.credential_hatch.pause
    // # Pause this processing group.
    // #zh_CN 暂停此加工组.

    // #tr tst.bottledchaos.credential_hatch.resume
    // # Resume this processing group.
    // #zh_CN 恢复此加工组运行.

    // #tr tst.bottledchaos.credential_hatch.clear
    // # Clear this processing group's current job and cached materials.
    // #zh_CN 清空此加工组的当前运行状态和缓存材料.

    // #tr tst.bottledchaos.gui.export_material_cache
    // # Export this hatch's cache to controller outputs. Blocked outputs preserve the cache.
    // #zh_CN 将本仓缓存排入主机输出. 输出空间不足时保留缓存.

    public enum Action {

        TAKE,
        EXECUTION,
        LIMIT,
        EXPORT,
        CLEAR;

        public String label(boolean enabled) {
            return switch (this) {
                // #tr tst.bottledchaos.credential_hatch.take_button
                // # Take
                // #zh_CN 取出凭证
                case TAKE -> "tst.bottledchaos.credential_hatch.take_button";
                case EXECUTION -> "tst.bottledchaos.credential_hatch." + (enabled ? "pause_button" : "resume_button");
                case LIMIT -> "tst.bottledchaos.credential_hatch.cache_limit";
                case EXPORT -> "tst.bottledchaos.credential_hatch.export_button";
                case CLEAR -> "tst.bottledchaos.credential_hatch.clear_button";
            };
        }

        private String tooltip(boolean enabled) {
            return switch (this) {
                // #tr tst.bottledchaos.credential_hatch.take
                // # Move this credential to your inventory.
                // #zh_CN 将本仓凭证取到玩家背包.
                case TAKE -> "tst.bottledchaos.credential_hatch.take";
                case EXECUTION -> "tst.bottledchaos.credential_hatch." + (enabled ? "pause" : "resume");
                case LIMIT -> label(enabled);
                case EXPORT -> "tst.bottledchaos.gui.export_material_cache";
                case CLEAR -> "tst.bottledchaos.credential_hatch.clear";
            };
        }

        private IDrawable icon(boolean enabled) {
            return switch (this) {
                case TAKE -> GTGuiTextures.OVERLAY_BUTTON_EXPORT;
                case EXECUTION -> enabled ? GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON
                    : GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF;
                case LIMIT -> GTGuiTextures.OVERLAY_BUTTON_CYCLIC;
                case EXPORT -> GTGuiTextures.OVERLAY_BUTTON_EXPORT;
                case CLEAR -> GTGuiTextures.OVERLAY_BUTTON_CROSS;
            };
        }

        private void execute(TST_BottledChaosRecipeTokenHatch hatch, EntityPlayer player) {
            switch (this) {
                case TAKE -> hatch.takeCredential(player);
                case EXECUTION -> hatch.setExecutionEnabled(!hatch.isExecutionEnabled());
                case EXPORT -> hatch.exportMaterialCache();
                case CLEAR -> hatch.clearRunningState();
                case LIMIT -> {}
            }
        }
    }

    public static List<ButtonWidget<?>> create(ModularPanel parent, PanelSyncManager manager, String id,
        Supplier<TST_BottledChaosRecipeTokenHatch> target, BooleanSupplier enabled, Action... actions) {
        IPanelHandler settings = manager.syncedPanel(id + "Limit", true, (sync, handler) -> {
            // Freeze the selected hatch when opening: later selection/removal cannot redirect an edit.
            TST_BottledChaosRecipeTokenHatch opened = target.get();
            return createLimitPanel(
                parent,
                () -> opened != null && opened.isValid() && target.get() == opened ? opened : null);
        });
        List<ButtonWidget<?>> buttons = new ArrayList<>();
        for (Action action : actions) {
            InteractionSyncHandler interaction = new InteractionSyncHandler().setOnMouseReleased(mouse -> {
                if (mouse.mouseButton != 0 || mouse.isClient()) return;
                TST_BottledChaosRecipeTokenHatch hatch = target.get();
                if (hatch != null && hatch.isValid()) action.execute(hatch, manager.getPlayer());
            });
            manager.syncValue(id + action.name(), interaction);
            buttons.add(
                new ActionButton().size(18, 18)
                    .syncHandler(interaction)
                    .onMouseReleased(mouse -> {
                        if (mouse != 0) return false;
                        if (action != Action.LIMIT) return interaction.onMouseReleased(mouse);
                        if (settings.isPanelOpen()) settings.closePanel();
                        else {
                            // MUI caches closed panels; rebuild this slot-free editor for the newly selected hatch.
                            settings.deleteCachedPanel();
                            settings.openPanel();
                        }
                        return true;
                    })
                    .playClickSound(true)
                    .background(
                        new DynamicDrawable(
                            () -> action == Action.EXECUTION && !enabled.getAsBoolean()
                                ? GTGuiTextures.BUTTON_STANDARD_PRESSED
                                : GTGuiTextures.BUTTON_STANDARD))
                    .overlay(new DynamicDrawable(() -> action.icon(enabled.getAsBoolean())))
                    .tooltipBuilder(tooltip -> tooltip.addLine(IKey.lang(action.tooltip(enabled.getAsBoolean()))))
                    .tooltipAutoUpdate(true));
        }
        return buttons;
    }

    private static final class ActionButton extends ButtonWidget<ActionButton> {

        private boolean pressed;

        @Override
        public Result onMousePressed(int mouseButton) {
            if (mouseButton != 0) return Result.IGNORE;
            pressed = true;
            playClickSound();
            return Result.SUCCESS;
        }

        @Override
        public boolean onMouseRelease(int mouseButton) {
            if (mouseButton != 0 || !pressed) return false;
            pressed = false;
            return isHovering() && super.onMouseRelease(mouseButton);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IDrawable getCurrentBackground(WidgetThemeEntry<?> theme) {
            // A release outside the button need not be dispatched to it by MUI.
            if (!isHovering() || !Mouse.isButtonDown(0)) pressed = false;
            return pressed ? GTGuiTextures.BUTTON_STANDARD_PRESSED : super.getCurrentBackground(theme);
        }
    }

    private static ModularPanel createLimitPanel(ModularPanel parent,
        Supplier<TST_BottledChaosRecipeTokenHatch> target) {
        LongSyncValue limit = new LongSyncValue(
            () -> target.get() == null ? 0
                : target.get()
                    .getMaterialCache()
                    .amountLimit(),
            value -> {
                if (target.get() != null) target.get()
                    .setCacheLimit(value);
            }).allowC2S();
        return GTGuis.createPopUpPanel("bottledChaosCacheSettings")
            .relative(parent)
            .leftRel(0.5f)
            .topRel(0.5f)
            .size(164, 78)
            .child(
                // #tr tst.bottledchaos.credential_hatch.cache_limit
                // # Cache limit
                // #zh_CN 缓存上限
                IKey.lang("tst.bottledchaos.credential_hatch.cache_limit")
                    .asWidget()
                    .pos(8, 18)
                    .size(148, 14))
            .child(
                new TextFieldWidget().value(limit)
                    .setPattern(Pattern.compile("[0-9]*"))
                    .setMaxLength(19)
                    .setValidator(text -> {
                        try {
                            return Long.toString(Math.max(0, Long.parseLong(text)));
                        } catch (NumberFormatException invalid) {
                            return Long.toString(limit.getLongValue());
                        }
                    })
                    .setTextAlignment(Alignment.Center)
                    .tooltipBuilder(
                        tooltip -> tooltip
                            // #tr tst.bottledchaos.credential_hatch.cache_limit.range
                            // # Range: %s - %s
                            // #zh_CN 范围: %s - %s
                            .addLine(
                                IKey.lang("tst.bottledchaos.credential_hatch.cache_limit.range", 0, Long.MAX_VALUE))
                            // #tr tst.bottledchaos.credential_hatch.cache_limit.info
                            // # Each item or fluid has this limit. Excess is sent to normal outputs.
                            // #zh_CN 每种物品或流体分别使用此上限, 超出部分正常输出.
                            .addLine(IKey.lang("tst.bottledchaos.credential_hatch.cache_limit.info")))
                    .pos(8, 40)
                    .size(148, 18));
    }
}
