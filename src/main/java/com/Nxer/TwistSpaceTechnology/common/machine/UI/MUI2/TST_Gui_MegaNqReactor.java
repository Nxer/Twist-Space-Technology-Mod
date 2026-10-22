package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis.TST_MegaNqReactor;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

public class TST_Gui_MegaNqReactor extends TST_Gui<TST_MegaNqReactor> {

    public TST_Gui_MegaNqReactor(TST_MegaNqReactor multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "megaNqOutputUnit",
            new IntSyncValue(multiblock::getOutputUnitIndex, multiblock::setOutputUnitIndex).allowC2S());
        syncManager.syncValue("megaNqBasicOutput", new IntSyncValue(multiblock::getBasicOutput));
        syncManager.syncValue("megaNqCoolantEfficiency", new IntSyncValue(multiblock::getCachedCoolantEfficiency));
        syncManager.syncValue("megaNqTimeMultiplier", new IntSyncValue(multiblock::getCachedTimeMultiplier));
        syncManager.syncValue("megaNqParallel", new IntSyncValue(multiblock::getParallel));
        syncManager.syncValue("megaNqOutput", new LongSyncValue(null, multiblock::getCurrentOutput));
        syncManager.syncValue("megaNqFuelName", new StringSyncValue(multiblock::getCachedFuelName));
        syncManager.syncValue("megaNqFuelAmount", new LongSyncValue(null, multiblock::getCachedFuelAmount));
        syncManager.syncValue("megaNqDiscount", new IntSyncValue(multiblock::getConsumptionDiscount));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        IntSyncValue unitSyncer = syncManager.findSyncHandler("megaNqOutputUnit", IntSyncValue.class);
        return super.createLeftPanelGapRow(parent, syncManager).child(
            new ButtonWidget<>().size(18, 18)
                .marginLeft(4)
                .overlay(GuiTextures.GRAPH)
                .onMousePressed(mouseButton -> {
                    int index = unitSyncer.getIntValue();
                    if (mouseButton == 0) {
                        unitSyncer.setValue(Math.min(index + 1, TST_MegaNqReactor.OUTPUT_UNIT_NAMES.length - 1));
                        return true;
                    }
                    if (mouseButton == 1) {
                        unitSyncer.setValue(Math.max(index - 1, 0));
                        return true;
                    }
                    return false;
                })
                .tooltipBuilder(t -> {
                    t.addLine(
                        IKey.lang("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.voltage_unit_left_click"));
                    t.addLine(
                        IKey.lang("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.voltage_unit_right_click"));
                    t.addLine(IKey.dynamic(() -> TST_MegaNqReactor.OUTPUT_UNIT_NAMES[unitSyncer.getIntValue()]));
                })
                .tooltipShowUpTimer(TOOLTIP_DELAY));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue unitSyncer = syncManager.findSyncHandler("megaNqOutputUnit", IntSyncValue.class);
        IntSyncValue basicSyncer = syncManager.findSyncHandler("megaNqBasicOutput", IntSyncValue.class);
        IntSyncValue efficiencySyncer = syncManager.findSyncHandler("megaNqCoolantEfficiency", IntSyncValue.class);
        IntSyncValue multiplierSyncer = syncManager.findSyncHandler("megaNqTimeMultiplier", IntSyncValue.class);
        IntSyncValue parallelSyncer = syncManager.findSyncHandler("megaNqParallel", IntSyncValue.class);
        LongSyncValue outputSyncer = syncManager.findSyncHandler("megaNqOutput", LongSyncValue.class);
        StringSyncValue fuelNameSyncer = syncManager.findSyncHandler("megaNqFuelName", StringSyncValue.class);
        LongSyncValue fuelAmountSyncer = syncManager.findSyncHandler("megaNqFuelAmount", LongSyncValue.class);
        IntSyncValue discountSyncer = syncManager.findSyncHandler("megaNqDiscount", IntSyncValue.class);

        ListWidget<IWidget, ?> terminal = super.createTerminalTextWidget(syncManager, parent);
        // 插到终端文本最上方，避免结构错误/停机原因过长时被挤出可视区域
        IWidget outputLine = createInfoLine(
            IKey.dynamic(
                () -> TSTUtils.tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output") + " "
                    + EnumChatFormatting.GOLD
                    + formatOutput(outputSyncer.getLongValue(), unitSyncer.getIntValue())
                    + EnumChatFormatting.RESET));
        IWidget breakdownLine = createInfoLine(
            IKey.dynamic(
                () -> EnumChatFormatting.GRAY
                    + formatBreakdown(
                        basicSyncer.getIntValue(),
                        efficiencySyncer.getIntValue(),
                        multiplierSyncer.getIntValue(),
                        parallelSyncer.getIntValue(),
                        unitSyncer.getIntValue())
                    + EnumChatFormatting.RESET));
        IWidget fuelLine = createInfoLine(
            IKey.dynamic(
                () -> TSTUtils.tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.fuel_consumption") + " "
                    + EnumChatFormatting.GOLD
                    + fuelNameSyncer.getStringValue()
                    + " "
                    + formatNumber(fuelAmountSyncer.getLongValue())
                    + " mB"
                    + EnumChatFormatting.RESET));
        IWidget discountLine = createInfoLine(
            IKey.dynamic(
                () -> TSTUtils.tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.consumption_discount") + " "
                    + EnumChatFormatting.GOLD
                    + discountSyncer.getIntValue()
                    + "%"
                    + EnumChatFormatting.RESET));
        terminal.addChild(outputLine, 0);
        terminal.addChild(breakdownLine, 1);
        terminal.addChild(fuelLine, 2);
        terminal.addChild(discountLine, 3);
        return terminal;
    }

    private static IWidget createInfoLine(IKey key) {
        return key.color(Color.WHITE.main)
            .asWidget()
            .textAlign(Alignment.CenterLeft)
            .marginBottom(2)
            .fullWidth();
    }

    private static String formatOutput(long euPerTick, int unitIndex) {
        long unit = TST_MegaNqReactor.OUTPUT_UNIT_VALUES[unitIndex];
        double value = (double) euPerTick / unit;
        String amount = value == Math.floor(value) ? formatNumber((long) value) : formatNumber(value);
        return amount + " A " + TST_MegaNqReactor.OUTPUT_UNIT_NAMES[unitIndex];
    }

    private static String formatBreakdown(int basicOutput, int efficiency, int multiplier, int parallel,
        int unitIndex) {
        return "= " + formatNumber(basicOutput)
            + " EU/t ("
            + TSTUtils.tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_base")
            + ") * "
            + efficiency
            + "% ("
            + TSTUtils.tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_efficiency")
            + ") * "
            + multiplier
            + " ("
            + TSTUtils.tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_coefficient")
            + ") * "
            + parallel
            + " ("
            + TSTUtils.tr("tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_parallel")
            + ") / "
            + formatNumber(TST_MegaNqReactor.OUTPUT_UNIT_VALUES[unitIndex])
            + " EU/t";
    }

    // spotless:off
    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output
    // # Output:
    // #zh_CN 发电量:

    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_base
    // # Base
    // #zh_CN 基础

    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_efficiency
    // # Efficiency
    // #zh_CN 效率

    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_coefficient
    // # Coefficient
    // #zh_CN 系数

    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.output_parallel
    // # Parallel
    // #zh_CN 并行

    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.fuel_consumption
    // # Fuel:
    // #zh_CN 燃料消耗:

    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.voltage_unit_left_click
    // # Left Click to increase voltage unit
    // #zh_CN 左键提高电压单位

    // #tr tst.common.machine.MegaNqReactor.gui.mega_nq_reactor.voltage_unit_right_click
    // # Right Click to decrease voltage unit
    // #zh_CN 右键降低电压单位
    // spotless:on
}
