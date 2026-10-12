package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatEnergy;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_DISABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_ENABLED_TOOLTIP;

import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.GTCM_LightningSpire;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;

public class TST_Gui_LightningSpire extends TST_Gui<GTCM_LightningSpire> {

    public static final UITexture POWER_PANEL_ICON = UITexture.fullImage(GregTech.ID, "gui/overlay_button/power_panel");

    public TST_Gui_LightningSpire(GTCM_LightningSpire multiblock) {
        super(multiblock);
    }

    final LongSyncValue storedEUSyncer = new LongSyncValue(null, () -> multiblock.tStoredEU);
    final LongSyncValue maxStoredEUSyncer = new LongSyncValue(null, () -> multiblock.tMaxStoredEU);

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("storedEU", storedEUSyncer);
        syncManager.syncValue("maxStoredEU", maxStoredEUSyncer);

        IntSyncValue getRodsSyncer = new IntSyncValue(multiblock::getRods);
        syncManager.syncValue("getRods", getRodsSyncer);

        BooleanSyncValue outputtingRodsButtonSyncer = new BooleanSyncValue(
            () -> multiblock.outputtingRods,
            b -> multiblock.outputtingRods = b).allowC2S();
        syncManager.syncValue("outputtingRodsButton", outputtingRodsButtonSyncer);

        BooleanSyncValue enableLightningButtonSyncer = new BooleanSyncValue(
            () -> multiblock.enable_lightning,
            b -> multiblock.enable_lightning = b).allowC2S();
        syncManager.syncValue("enableLightningButton", enableLightningButtonSyncer);
    }

    private double getEnergyRatio() {
        return (double) storedEUSyncer.getLongValue() / maxStoredEUSyncer.getLongValue();
    }

    private ProgressWidget createProgressWidget() {
        ProgressWidget progress = new ProgressWidget().texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
            .height(5)
            .value(new DoubleValue.Dynamic(this::getEnergyRatio, null))
            .expanded();
        progress.tooltipDynamic(tooltip -> {
            tooltip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.fusion.stored_eu",
                    formatEnergy(storedEUSyncer.getLongValue()),
                    formatNumber(getEnergyRatio() * 100)));
            tooltip.markDirty();
        });
        return progress;
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .paddingRight(2)
            .paddingLeft(4)
            .height(getTextBoxToInventoryGap())
            .child(createLeftPanelGapRow(parent, syncManager).paddingRight(4))
            .child(
                Flow.row()
                    .reverseLayout()
                    .expanded()
                    .child(
                        // For some reason I couldn't get it to work without this nesting
                        Flow.row()
                            .coverChildrenWidth()
                            .child(createRightPanelGapRow(parent, syncManager)))
                    .child(createProgressWidget()));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildrenWidth()
            .fullHeight()
            .child(createOutputtingRodsButton(syncManager))
            .child(createEnableLightningButtonSyncer(syncManager));
    }

    protected IWidget createEnableLightningButtonSyncer(PanelSyncManager syncManager) {
        BooleanSyncValue enableLightningButtonSyncer = syncManager
            .findSyncHandler("enableLightningButton", BooleanSyncValue.class);
        return new ToggleButton().value(enableLightningButtonSyncer)
            .overlay(POWER_PANEL_ICON)
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang("tst.gui.button.enable_lightning"));
                t.addLine(
                    IKey.lang(
                        enableLightningButtonSyncer.getValue() ? BUTTON_FEATURE_ENABLED_TOOLTIP
                            : BUTTON_FEATURE_DISABLED_TOOLTIP));
            });
        // #tr tst.gui.button.enable_lightning
        // # Lightning animation switch
        // #zh_CN 闪电动画特效开关
    }

    protected IWidget createOutputtingRodsButton(PanelSyncManager syncManager) {
        return new ToggleButton().value(syncManager.findSyncHandler("outputtingRodsButton", BooleanSyncValue.class))
            .overlay(GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
            .tooltipBuilder(t -> { t.addLine(IKey.lang("tst.gui.button.outputting_rods")); });
        // #tr tst.gui.button.outputting_rods
        // # Output all the Lightning Rods stored in machine
        // #zh_CN 输出机器内全部避雷针
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        IntSyncValue getRodsSyncValue = syncManager.findSyncHandler("getRods", IntSyncValue.class);
        // #tr TST_Gui_LightningSpire.LightningRodAmount
        // # Lightning Rod :
        // #zh_CN 避雷针数量:
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.lang("TST_Gui_LightningSpire.LightningRodAmount")
                    .color(Color.WHITE.main)
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .marginBottom(2)
                    .fullWidth())
            .child(
                IKey.dynamic(() -> String.valueOf(getRodsSyncValue.getValue()))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .marginBottom(2)
                    .fullWidth());
    }

}
