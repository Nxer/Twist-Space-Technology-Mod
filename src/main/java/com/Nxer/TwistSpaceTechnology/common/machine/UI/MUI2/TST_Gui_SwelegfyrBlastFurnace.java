package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_BlazeClear;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_BlazeSet;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_Blaze_Forbidden;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_HoldingHeat_Forbidden;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_HoldingHeat_Off;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_HoldingHeat_On;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_RapidHeating_Forbidden;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_RapidHeating_Off;
import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.SBF_RapidHeating_On;

import java.util.function.BooleanSupplier;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_SwelegfyrBlastFurnace;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
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
        return super.createLeftPanelGapRow(panel, syncManager).child(createBlazeStatusButton(syncManager))
            .child(createRapidHeatingButton(syncManager))
            .child(createHoldingHeatButton(syncManager));
    }

    public IWidget createBlazeStatusButton(PanelSyncManager syncManager) {
        BooleanSyncValue blazeStatusButtonSyncer = syncManager
            .findSyncHandler("blazeStatusButtonSyncer", BooleanSyncValue.class);
        BooleanSyncValue machineActiveGetter = syncManager
            .findSyncHandler("machineActiveGetter", BooleanSyncValue.class);

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (machineActiveGetter.getValue()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(blazeStatusButtonSyncer)
            .overlay(new DynamicDrawable(() -> {
                if (machineActiveGetter.getValue()) return SBF_Blaze_Forbidden;
                return blazeStatusButtonSyncer.getValue() ? SBF_BlazeClear : SBF_BlazeSet;
            }))
            .tooltipBuilder(
                tooltip -> tooltip.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocal(
                            blazeStatusButtonSyncer.getValue()
                                ? "tst.common.machine.SwelegfyrBlastFurnace.message.clear_blaze"
                                : "tst.common.machine.SwelegfyrBlastFurnace.message.fill_blaze"))));

        // #tr tst.common.machine.SwelegfyrBlastFurnace.message.fill_blaze
        // # Fill Pyrotheum
        // #zh_CN 填充炽焱

        // #tr tst.common.machine.SwelegfyrBlastFurnace.message.clear_blaze
        // # Clear Pyrotheum
        // #zh_CN 清除炽焱
    }

    public IWidget createRapidHeatingButton(PanelSyncManager syncManager) {
        return createPassiveModeButton(
            syncManager,
            "rapidHeatingButtonSyncer",
            SBF_RapidHeating_Off,
            SBF_RapidHeating_On,
            SBF_RapidHeating_Forbidden,
            "tst.common.machine.SwelegfyrBlastFurnace.message.enable_rapid_heating");

        // #tr tst.common.machine.SwelegfyrBlastFurnace.message.enable_rapid_heating
        // # Rapid Thermal Boost
        // #zh_CN 快速热增强模式
    }

    public IWidget createHoldingHeatButton(PanelSyncManager syncManager) {
        return createPassiveModeButton(
            syncManager,
            "holdingHeatButtonSyncer",
            SBF_HoldingHeat_Off,
            SBF_HoldingHeat_On,
            SBF_HoldingHeat_Forbidden,
            "tst.common.machine.SwelegfyrBlastFurnace.message.enable_holding_heat");

        // #tr tst.common.machine.SwelegfyrBlastFurnace.message.enable_holding_heat
        // # Thermal Retention Standby
        // #zh_CN 热保持待机模式
    }

    private IWidget createPassiveModeButton(PanelSyncManager syncManager, String syncKey, UITexture offTexture,
        UITexture onTexture, UITexture forbiddenTexture, String tooltipKey) {
        IntSyncValue machineModeSyncer = syncManager.findSyncHandler("machineMode", IntSyncValue.class);
        BooleanSyncValue buttonSyncer = syncManager.findSyncHandler(syncKey, BooleanSyncValue.class);
        BooleanSyncValue updatedMachineGetter = syncManager
            .findSyncHandler("updatedMachineGetter", BooleanSyncValue.class);
        // Both secondary modes are available only after the tier II upgrade and while passive mode is selected.
        BooleanSupplier isEnabled = () -> updatedMachineGetter.getValue() && machineModeSyncer.getValue() == 1;

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!isEnabled.getAsBoolean()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(buttonSyncer)
            .overlay(new DynamicDrawable(() -> {
                if (!isEnabled.getAsBoolean()) return forbiddenTexture;
                return buttonSyncer.getValue() ? onTexture : offTexture;
            }))
            .tooltipBuilder(tooltip -> tooltip.add(StatCollector.translateToLocal(tooltipKey)));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        BooleanSyncValue blazeStatusButtonSyncer = new BooleanSyncValue(
            multiblock::isBlazeFilled,
            val -> { multiblock.clickBlazeStatusButton(); }).allowC2S();
        syncManager.syncValue("blazeStatusButtonSyncer", blazeStatusButtonSyncer);

        BooleanSyncValue machineActiveGetter = new BooleanSyncValue(
            () -> multiblock.getBaseMetaTileEntity() != null && multiblock.getBaseMetaTileEntity()
                .isActive());
        syncManager.syncValue("machineActiveGetter", machineActiveGetter);

        BooleanSyncValue updatedMachineGetter = new BooleanSyncValue(() -> multiblock.controllerTier == 2).allowC2S();
        syncManager.syncValue("updatedMachineGetter", updatedMachineGetter);

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
