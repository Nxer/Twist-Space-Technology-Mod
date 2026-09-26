package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_LaserMeteorMiner;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class TST_Gui_LaserMeteorMiner extends MTEMultiBlockBaseGui<TST_LaserMeteorMiner> {

    public TST_Gui_LaserMeteorMiner(TST_LaserMeteorMiner multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("meteorReset", new ClickOnceSyncValue(multiblock::startReset).allowC2S());
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
}
