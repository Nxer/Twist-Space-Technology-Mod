package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_BlazeSet;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_HoldingHeat_Forbidden;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_HoldingHeat_Off;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_HoldingHeat_On;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_RapidHeating_Forbidden;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_RapidHeating_Off;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_RapidHeating_On;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_SwelegfyrBlastFurnace;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

public class TST_Gui_SwelegfyrBlastFurnace extends TST_Gui<TST_SwelegfyrBlastFurnace> {

    public TST_Gui_SwelegfyrBlastFurnace(TST_SwelegfyrBlastFurnace multiblock) {
        super(multiblock);
    }

    @Override
    public Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(panel, syncManager).child(createBlazeStatusButton())
            .child(createRapidHeatingButton(syncManager))
            .child(createHoldingHeatButton(syncManager));
    }

    public IWidget createBlazeStatusButton() {
        return new ToggleButton().size(18, 18)
            .syncHandler("blazeStatusButtonSyncer")
            .overlay(SBF_BlazeSet)
            .tooltip(new RichTooltip().add(StatCollector.translateToLocal("SBF.Msg.setOrClearBlaze")));

        // #tr SBF.Msg.setOrClearBlaze
        // # Place / Clear Pyrotheum
        // #zh_CN 填充/清除炽焱
    }

    public IWidget createRapidHeatingButton(PanelSyncManager syncManager) {
        IntSyncValue machineModeSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("machineMode:0");
        BooleanSyncValue rapidHeatingButtonSyncer = (BooleanSyncValue) syncManager
            .getSyncHandlerFromMapKey("rapidHeatingButtonSyncer:0");
        BooleanSyncValue updatedMachineGetter = (BooleanSyncValue) syncManager
            .getSyncHandlerFromMapKey("updatedMachineGetter:0");

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!updatedMachineGetter.getValue() || machineModeSyncer.getValue() != 1) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(rapidHeatingButtonSyncer)
            .overlay(new DynamicDrawable(() -> {
                TwistSpaceTechnology.LOG.info("updatedMachineGetter.getValue() = {}", updatedMachineGetter.getValue());
                if (!updatedMachineGetter.getValue() || machineModeSyncer.getValue() != 1) {
                    return SBF_RapidHeating_Forbidden;
                }

                return rapidHeatingButtonSyncer.getValue() ? SBF_RapidHeating_On : SBF_RapidHeating_Off;
            }))
            .tooltipBuilder(a -> a.add(StatCollector.translateToLocal("SBF.Msg.enableRapidHeating")));
        // #tr SBF.Msg.enableRapidHeating
        // # Rapid Thermal Boost
        // #zh_CN 快速热增强模式
    }

    public IWidget createHoldingHeatButton(PanelSyncManager syncManager) {
        IntSyncValue machineModeSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("machineMode:0");
        BooleanSyncValue updatedMachineGetter = (BooleanSyncValue) syncManager
            .getSyncHandlerFromMapKey("updatedMachineGetter:0");
        BooleanSyncValue holdingHeatButtonSyncer = (BooleanSyncValue) syncManager
            .getSyncHandlerFromMapKey("holdingHeatButtonSyncer:0");

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!updatedMachineGetter.getValue() || machineModeSyncer.getValue() != 1) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(holdingHeatButtonSyncer)
            .overlay(new DynamicDrawable(() -> {
                if (!updatedMachineGetter.getValue() || machineModeSyncer.getValue() != 1) {
                    return SBF_HoldingHeat_Forbidden;
                }

                return holdingHeatButtonSyncer.getValue() ? SBF_HoldingHeat_On : SBF_HoldingHeat_Off;
            }))
            .tooltipBuilder(a -> a.add(StatCollector.translateToLocal("SBF.Msg.enableHoldingHeat")));
        // #tr SBF.Msg.enableHoldingHeat
        // # Thermal Retention Standby
        // #zh_CN 热保持待机模式
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        ClickOnceSyncValue b = new ClickOnceSyncValue(multiblock::clickBlazeStatusButton);

        BooleanSyncValue blazeStatusButtonSyncer = new BooleanSyncValue(
            () -> false,
            val -> { multiblock.clickBlazeStatusButton(); }).allowC2S();
        syncManager.syncValue("blazeStatusButtonSyncer", blazeStatusButtonSyncer);

        BooleanSyncValue updatedMachineGetter = new BooleanSyncValue(() -> multiblock.controllerTier == 2).allowC2S();
        syncManager.syncValue("updatedMachineGetter", updatedMachineGetter);

        //
        BooleanSyncValue rapidHeatingButtonSyncer = new BooleanSyncValue(
            multiblock::getRapidHeating,
            multiblock::setRapidHeating).allowC2S();
        syncManager.syncValue("rapidHeatingButtonSyncer", rapidHeatingButtonSyncer);

        BooleanSyncValue holdingHeatButtonSyncer = new BooleanSyncValue(
            multiblock::getHoldingHeat,
            multiblock::setHoldingHeat).allowC2S();
        syncManager.syncValue("holdingHeatButtonSyncer", holdingHeatButtonSyncer);

    }

}
