package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;

public class TST_Gui_BloodyHell extends TST_Gui<TST_BloodyHell> {

    public TST_Gui_BloodyHell(TST_BloodyHell multiblock) {
        super(multiblock);
    }

    @Override
    public Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(panel, syncManager).child(createBloodStatusButton(syncManager));
    }

    public IWidget createBloodStatusButton(PanelSyncManager syncManager) {
        InteractionSyncHandler clickOnceSyncer = (InteractionSyncHandler) syncManager
            .getSyncHandlerFromMapKey("clickOnceSyncer:0");
        BooleanSyncValue bloodClearedStatusGetter = (BooleanSyncValue) syncManager
            .getSyncHandlerFromMapKey("bloodClearedStatusGetter:0");
        long[] pressedUntil = { 0 };
        return new ButtonWidget<>().syncHandler(clickOnceSyncer)
            .background(
                new DynamicDrawable(
                    () -> System.currentTimeMillis() < pressedUntil[0] ? GTGuiTextures.BUTTON_STANDARD_PRESSED
                        : GTGuiTextures.BUTTON_STANDARD))
            .overlay(
                new DynamicDrawable(
                    () -> bloodClearedStatusGetter.getBoolValue() ? UITextures.SBF_BlazeSet
                        : UITextures.SBF_BlazeClear))
            .onMousePressed(mouseButton -> {
                pressedUntil[0] = System.currentTimeMillis() + 100;
                return clickOnceSyncer.onMousePressed(mouseButton);
            })
            .playClickSound(true)
            .tooltipBuilder(
                // #tr tst.common.machine.BloodyHell.gui.set_or_clear_blood
                // # Place / Clear Blood
                // #zh_CN 填充/清除血液
                t -> t.addLine(StatCollector.translateToLocal("tst.common.machine.BloodyHell.gui.set_or_clear_blood")));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        InteractionSyncHandler clickOnceSyncer = new InteractionSyncHandler().setOnMousePressed(mouse -> {
            if (!multiblock.getBaseMetaTileEntity()
                .isServerSide()) return;
            multiblock.checkStructure(true, multiblock.getBaseMetaTileEntity());
            if (multiblock.isStructureBuild /* && !multiblock.getBaseMetaTileEntity().isActive() */) {
                multiblock.checkBlood(true);
            }
        });
        syncManager.syncValue("clickOnceSyncer", clickOnceSyncer);

        BooleanSyncValue bloodClearedStatusGetter = new BooleanSyncValue(() -> multiblock.isBloodClear);
        syncManager.syncValue("bloodClearedStatusGetter", bloodClearedStatusGetter);

    }

}
