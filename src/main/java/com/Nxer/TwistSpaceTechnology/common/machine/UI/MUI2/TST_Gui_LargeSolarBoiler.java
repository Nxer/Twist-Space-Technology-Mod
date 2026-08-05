package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.TST_LargeSolarBoiler;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;

public class TST_Gui_LargeSolarBoiler extends TST_Gui<TST_LargeSolarBoiler> {

    public TST_Gui_LargeSolarBoiler(TST_LargeSolarBoiler multiblock) {
        super(multiblock);
    }

    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        DoubleSyncValue heatSyncer = new DoubleSyncValue(multiblock::getHeat);
        DoubleSyncValue calcificationSyncer = new DoubleSyncValue(multiblock::getCalcification);
        syncManager.syncValue("heatSyncer", heatSyncer);
        syncManager.syncValue("calcificationSyncer", calcificationSyncer);

        return super.createTerminalTextWidget(syncManager, parent)
            // #tr TST_LargeSolarBoiler.gui.02
            // # {\WHITE}Heat: {\GOLD}%s%%{\RESET}
            // #zh_CN {\WHITE}热量: {\GOLD}%s%%{\RESET}
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "TST_LargeSolarBoiler.gui.02",
                        formatNumber((int) (heatSyncer.getDoubleValue() * 100))))
                    .asWidget()
                    .marginBottom(2)
                    .fullWidth())
            // #tr TST_LargeSolarBoiler.gui.03
            // # {\WHITE}Calcification Level: {\GOLD}%s%%{\RESET}
            // #zh_CN {\WHITE}钙化程度: {\GOLD}%s%%{\RESET}
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "TST_LargeSolarBoiler.gui.03",
                        formatNumber((int) (calcificationSyncer.getDoubleValue() * 100))))
                    .asWidget()
                    .marginBottom(2)
                    .fullWidth());
    }

    @Override
    public Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(panel, syncManager).child(createClearingButton(syncManager));
    }

    public IWidget createClearingButton(PanelSyncManager syncManager) {
        InteractionSyncHandler clearOnceSyncer = (InteractionSyncHandler) syncManager
            .getSyncHandlerFromMapKey("clearOnceSyncer:0");

        return new ButtonWidget<>().syncHandler(clearOnceSyncer)
            .overlay(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT)
            .playClickSound(true)
            .tooltip(
                // #tr TST_LargeSolarBoiler.gui.01
                // # Press to clear the machine
                // #zh_CN 点击以清洁机器的钙化
                t -> t.addLine(TextEnums.tr("TST_LargeSolarBoiler.gui.01")));

    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        InteractionSyncHandler clearOnceSyncer = new InteractionSyncHandler()
            .setOnMousePressed(mouse -> { multiblock.onClickClearingButton(); });
        syncManager.syncValue("clearOnceSyncer", clearOnceSyncer);

    }

}
