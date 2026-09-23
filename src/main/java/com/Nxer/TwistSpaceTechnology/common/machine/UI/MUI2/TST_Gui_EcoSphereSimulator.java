package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;

public class TST_Gui_EcoSphereSimulator extends TST_Gui<TST_EcoSphereSimulator> {

    public TST_Gui_EcoSphereSimulator(TST_EcoSphereSimulator multiblock) {
        super(multiblock);
    }

    @Override
    public Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(panel, syncManager).child(createFluidAreaClearingButton(syncManager));
    }

    private IWidget createFluidAreaClearingButton(PanelSyncManager syncManager) {
        InteractionSyncHandler clearingSyncer = (InteractionSyncHandler) syncManager
            .getSyncHandlerFromMapKey("fluidAreaClearingSyncer:0");
        long[] pressedUntil = { 0 };
        return new ButtonWidget<>().syncHandler(clearingSyncer)
            .background(
                new DynamicDrawable(
                    () -> System.currentTimeMillis() < pressedUntil[0] ? GTGuiTextures.BUTTON_STANDARD_PRESSED
                        : GTGuiTextures.BUTTON_STANDARD))
            .overlay(GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT)
            .onMousePressed(mouseButton -> {
                pressedUntil[0] = System.currentTimeMillis() + 100;
                return clearingSyncer.onMousePressed(mouseButton);
            })
            .playClickSound(true)
            .tooltip(
                // #tr tst.ecosphere.machine.EcoSphereSimulator.gui.clear_fluid_area
                // # Clear and reset the fluid area
                // #zh_CN 清除并重置水域
                tooltip -> tooltip
                    .addLine(TSTUtils.tr("tst.ecosphere.machine.EcoSphereSimulator.gui.clear_fluid_area")));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "fluidAreaClearingSyncer",
            new InteractionSyncHandler().setOnMousePressed(mouseButton -> multiblock.onClickFluidAreaClearingButton()));
    }
}
