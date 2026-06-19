package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import net.minecraft.util.StatCollector;

public class TST_Gui_BloodyHell extends TST_Gui<TST_BloodyHell> {
    public TST_Gui_BloodyHell(TST_BloodyHell multiblock) {
        super(multiblock);
    }

    @Override
    public Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createMainColumn(panel, syncManager)
                    .child(createBloodStatusButton(syncManager));
    }

    public IWidget createBloodStatusButton(PanelSyncManager syncManager) {
        InteractionSyncHandler clickOnceSyncer = (InteractionSyncHandler) syncManager.getSyncHandlerFromMapKey("clickOnceSyncer:0");
        BooleanSyncValue bloodClearedStatusGetter = (BooleanSyncValue) syncManager.getSyncHandlerFromMapKey("bloodClearedStatusGetter:0");
        return new ButtonWidget<>()
                   .overlay(bloodClearedStatusGetter.getBoolValue() ? UITextures.SBF_BlazeSet : UITextures.SBF_BlazeClear)
                   .onMousePressed(
                       mouseButton -> clickOnceSyncer.onMousePressed(mouseButton)
                   )
                   .tooltipBuilder(
                       // #tr BloodyHell_setOrClearBlood
                       // # Place / Clear Blood
                       // #zh_CN 填充/清除血液
                       t -> t.addLine(StatCollector.translateToLocal("BloodyHell_setOrClearBlood"))
                   );
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        InteractionSyncHandler clickOnceSyncer = new InteractionSyncHandler()
                                                     .setOnKeyPressed(
                                                         mouse -> {
                                                             if (multiblock.checkStructure(true, multiblock.getBaseMetaTileEntity()) && !multiblock.getBaseMetaTileEntity()
                                                                                                                       .isActive()) if (multiblock.checkBlood(true)) {
                                                                 multiblock.isBloodClear = !multiblock.isBloodClear;
                                                                 multiblock.isBloodChecked = !multiblock.isBloodClear;
                                                             }
                                                         }
                                                     );
        syncManager.syncValue("clickOnceSyncer", clickOnceSyncer);

        BooleanSyncValue bloodClearedStatusGetter = new BooleanSyncValue(
            () -> multiblock.isBloodClear
        );
        syncManager.syncValue("bloodClearedStatusGetter", bloodClearedStatusGetter);

    }

}
