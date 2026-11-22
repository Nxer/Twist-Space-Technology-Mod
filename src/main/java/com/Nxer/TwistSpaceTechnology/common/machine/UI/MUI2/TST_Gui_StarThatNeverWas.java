package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_StarThatNeverWas;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;

public final class TST_Gui_StarThatNeverWas extends TST_Gui<TST_StarThatNeverWas> {

    public TST_Gui_StarThatNeverWas(TST_StarThatNeverWas machine) {
        super(machine);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager manager) {
        super.registerSyncValues(manager);
        manager.syncValue("starPhase", new IntSyncValue(multiblock::getStarPhase));
        manager.syncValue("starEUt", new LongSyncValue(multiblock::getStarEnergyUsage));
        manager.syncValue("starRadius", new IntSyncValue(multiblock::getStarRadius));
        manager.syncValue("starFortune", new IntSyncValue(multiblock::getStarFortune));
        manager.syncValue("starDrones", new IntSyncValue(multiblock::getStarDrones));
        manager.syncValue("starLP", new LongSyncValue(multiblock::getStarLifeEssence));
        manager.syncValue("starParallel", new LongSyncValue(multiblock::getStarParallel));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected IWidget createStructureErrorWidget(PanelSyncManager manager) {
        GenericListSyncHandler<StructureError> errors = manager
            .findSyncHandler("structureErrors", GenericListSyncHandler.class);
        DynamicSyncHandler handler = new DynamicSyncHandler().widgetProvider((sync, packet) -> {
            Flow column = Flow.column()
                .coverChildrenHeight(0)
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .childPadding(2);
            // The GT default hides details until shutdown. Keep native error text/highlighting visible during assembly.
            for (StructureError error : errors.getValue()) column.child(error.createWidget(this));
            return column;
        })
            .allowC2S();
        errors.setChangeListener(() -> handler.notifyUpdate(packet -> {}));
        return new DynamicSyncedWidget<>().widthRel(0.95f)
            .coverChildrenHeight(0)
            .syncHandler(handler);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager manager) {
        // The native column lays out bottom to top, so the extra button sits above structure checking.
        return super.createButtonColumn(panel, manager).child(createRenderSettingsButton(manager));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager manager) {
        InteractionSyncHandler abort = new InteractionSyncHandler().setOnMouseReleased(
            mouse -> { if (mouse.mouseButton == 0 && !mouse.isClient()) multiblock.abortCurrentTask(); });
        manager.syncValue("starAbortTask", abort);
        // #tr tst.star.gui.abort
        // # Abort current task and return all drones
        // #zh_CN 终止当前任务并返还全部无人机

        // #tr tst.star.gui.abort_cost
        // # Spent inputs and undelivered ores are lost.
        // #zh_CN 已扣原料不退还, 未交付的矿物将清除.
        return super.createLeftPanelGapRow(panel, manager).child(
            new ButtonWidget<>().size(18, 18)
                .syncHandler(abort)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                .onMouseReleased(mouse -> mouse == 0 && abort.onMouseReleased(mouse))
                .tooltipBuilder(t -> {
                    t.addLine(IKey.lang("tst.star.gui.abort"));
                    t.addLine(IKey.lang("tst.star.gui.abort_cost"));
                }));
    }

    @Override
    protected void createModeSwitchTooltip(RichTooltip tooltip) {
        super.createModeSwitchTooltip(tooltip);
        // #tr tst.star.mode.next_recipe
        // # Mode changes take effect when the next recipe starts.
        // #zh_CN 模式切换将在下次配方开始时生效.
        tooltip.addLine(IKey.lang("tst.star.mode.next_recipe"));
    }

    private IWidget createRenderSettingsButton(PanelSyncManager manager) {
        IPanelHandler settings = manager
            .syncedPanel("starRenderSettings", true, (sync, handler) -> createRenderSettingsPanel(sync));
        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_SHOW_WORK_AREA)
            .onMousePressed(mouseButton -> {
                if (mouseButton != 0) return false;
                if (settings.isPanelOpen()) settings.closePanel();
                else settings.openPanel();
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("tst.star.render.settings")));
    }

    private ModularPanel createRenderSettingsPanel(PanelSyncManager manager) {
        // #tr tst.star.render.settings
        // # Render settings
        // #zh_CN 渲染设置

        // #tr tst.star.render.master
        // # All rendering
        // #zh_CN 渲染总开关

        // #tr tst.star.render.rings
        // # Ring bands
        // #zh_CN 环带

        // #tr tst.star.render.mining
        // # Miner and containment sphere
        // #zh_CN 矿机与隔离球

        // #tr tst.star.render.meteor
        // # Show meteor
        // #zh_CN 陨星显示

        // #tr tst.star.render.meteor_animation
        // # Meteor animation
        // #zh_CN 陨星动画

        // #tr tst.star.render.clustered
        // # Meteor style: clustered ores
        // #zh_CN 陨星样式: 群聚矿石

        // #tr tst.star.render.surface
        // # Meteor style: scattered ores
        // #zh_CN 陨星样式: 散布矿石
        BooleanSyncValue style = new BooleanSyncValue(multiblock::getClusteredRender, multiblock::setClusteredRender)
            .allowC2S();
        manager.syncValue("starRenderStyle", style);
        Flow settings = Flow.column()
            .width(208)
            .coverChildrenHeight()
            .top(24)
            .left(8)
            .childPadding(4)
            .child(
                createRenderToggle(
                    manager,
                    "starAnimations",
                    "tst.star.render.master",
                    multiblock::isRenderEnabled,
                    multiblock::setRenderEnabled))
            .child(
                createRenderToggle(
                    manager,
                    "starRingAnimations",
                    "tst.star.render.rings",
                    multiblock::isRingRenderEnabled,
                    multiblock::setRingRenderEnabled))
            .child(
                createRenderToggle(
                    manager,
                    "starMiningAnimations",
                    "tst.star.render.mining",
                    multiblock::isMiningRenderEnabled,
                    multiblock::setMiningRenderEnabled))
            .child(
                createRenderToggle(
                    manager,
                    "starMeteorVisibility",
                    "tst.star.render.meteor",
                    multiblock::isMeteorRenderEnabled,
                    multiblock::setMeteorRenderEnabled))
            .child(
                createRenderToggle(
                    manager,
                    "starMeteorAnimations",
                    "tst.star.render.meteor_animation",
                    multiblock::isMeteorAnimationEnabled,
                    multiblock::setMeteorAnimationEnabled))
            .child(
                new ToggleButton().size(208, 20)
                    .value(style)
                    .overlay(
                        IKey.dynamic(
                            () -> StatCollector.translateToLocal(
                                style.getBoolValue() ? "tst.star.render.clustered" : "tst.star.render.surface"))));
        return new ModularPanel("starRenderSettings").size(224, 166)
            .center()
            .child(
                new TextWidget<>(IKey.lang("tst.star.render.settings")).top(8)
                    .left(8))
            .child(ButtonWidget.panelCloseButton())
            .child(settings);
    }

    private Flow createRenderToggle(PanelSyncManager manager, String id, String label, BooleanSupplier getter,
        Consumer<Boolean> setter) {
        BooleanSyncValue value = new BooleanSyncValue(getter, setter::accept).allowC2S();
        manager.syncValue(id, value);
        return Flow.row()
            .width(208)
            .height(18)
            .childPadding(5)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(
                new ToggleButton().size(18, 18)
                    .value(value)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                    .tooltipBuilder(t -> t.addLine(IKey.lang(label))))
            .child(new TextWidget<>(IKey.lang(label)).width(185));
    }

    @Override
    protected void addMachineStatus(ListWidget<IWidget, ?> terminal, PanelSyncManager manager) {
        IntSyncValue phase = manager.findSyncHandler("starPhase", IntSyncValue.class);
        LongSyncValue eut = manager.findSyncHandler("starEUt", LongSyncValue.class);
        IntSyncValue radius = manager.findSyncHandler("starRadius", IntSyncValue.class);
        IntSyncValue fortune = manager.findSyncHandler("starFortune", IntSyncValue.class);
        IntSyncValue drones = manager.findSyncHandler("starDrones", IntSyncValue.class);
        LongSyncValue lp = manager.findSyncHandler("starLP", LongSyncValue.class);
        LongSyncValue parallel = manager.findSyncHandler("starParallel", LongSyncValue.class);

        // #tr tst.star.gui.phase.0
        // # Idle
        // #zh_CN 待机

        // #tr tst.star.gui.phase.1
        // # Meteor formation
        // #zh_CN 陨星凝聚

        // #tr tst.star.gui.phase.2
        // # Beam connection
        // #zh_CN 光束连接

        // #tr tst.star.gui.phase.3
        // # Mining
        // #zh_CN 采掘

        // #tr tst.star.gui.phase.4
        // # Delivering outputs
        // #zh_CN 产出交付

        // #tr tst.star.gui.phase.5
        // # Waiting to return drones
        // #zh_CN 等待返还无人机

        // #tr tst.star.gui.operation
        // # %s | %s EU/t
        // #zh_CN %s | %s EU/t

        // #tr tst.star.gui.mining
        // # Radius: %s | Fortune: %s
        // #zh_CN 半径: %s | 时运: %s

        // #tr tst.star.gui.parallel
        // # Parallel: %s | Active drones: %s
        // #zh_CN 并行: %s | 参与无人机: %s

        // #tr tst.star.gui.lp
        // # Life Essence consumed: %s L
        // #zh_CN 本批消耗生命本源：%s L

        terminal.child(
            new TextWidget<>(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "tst.star.gui.operation",
                        StatCollector.translateToLocal("tst.star.gui.phase." + phase.getIntValue()),
                        GTUtility.formatShortenedLong(eut.getLongValue())))).fullWidth());
        terminal.child(
            new TextWidget<>(
                IKey.dynamic(
                    () -> StatCollector
                        .translateToLocalFormatted("tst.star.gui.mining", radius.getIntValue(), fortune.getIntValue())))
                            .fullWidth()
                            .setEnabledIf(w -> radius.getIntValue() > 0)
                            .tooltipBuilder(
                                t -> t.addLine(
                                    IKey.dynamic(
                                        () -> StatCollector.translateToLocalFormatted(
                                            "tst.star.gui.lp",
                                            formatNumber(lp.getLongValue()))))));
        terminal.child(
            new TextWidget<>(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "tst.star.gui.parallel",
                        GTUtility.formatShortenedLong(parallel.getLongValue()),
                        drones.getIntValue()))).fullWidth()
                            .setEnabledIf(w -> radius.getIntValue() > 0));
    }
}
