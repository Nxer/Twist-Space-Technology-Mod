package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class TST_Gui<T extends GTCM_MultiMachineBase<T>> extends MTEMultiBlockBaseGui<T> {

    private static final int DISPLAY_ROW_HEIGHT = 15;
    private static final int DISPLAY_ROW_CHAR_LIMIT = 46;

    public TST_Gui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("tstMEOutputEnabled", new BooleanSyncValue(multiblock::isMEOutputEnabled));

        syncManager.syncValue(
            "tstMEItemOutput",
            new GenericListSyncHandler<>(
                multiblock::getMEItemOutputInfo,
                null,
                TST_Gui::readItemOutput,
                TST_Gui::writeItemOutput,
                (a, b) -> a.stackSize() == b.stackSize() && GTUtility.areStacksEqual(a.itemStack(), b.itemStack()),
                null));
        syncManager.syncValue(
            "tstMEFluidOutput",
            new GenericListSyncHandler<>(
                multiblock::getMEFluidOutputInfo,
                null,
                TST_Gui::readFluidOutput,
                TST_Gui::writeFluidOutput,
                (a, b) -> a.amount() == b.amount() && a.fluidStack()
                    .isFluidEqual(b.fluidStack()),
                null));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> terminal = super.createTerminalTextWidget(syncManager, parent);

        BooleanSyncValue meOutputEnabledSyncer = (BooleanSyncValue) syncManager
            .getSyncHandlerFromMapKey("tstMEOutputEnabled:0");
        GenericListSyncHandler<ItemStackLong> itemOutputSyncer = (GenericListSyncHandler<ItemStackLong>) syncManager
            .getSyncHandlerFromMapKey("tstMEItemOutput:0");
        GenericListSyncHandler<FluidStackLong> fluidOutputSyncer = (GenericListSyncHandler<FluidStackLong>) syncManager
            .getSyncHandlerFromMapKey("tstMEFluidOutput:0");

        DynamicSyncHandler recipeHandler = new DynamicSyncHandler()
            .widgetProvider(
                (manager, packet) -> packet == null ? new EmptyWidget() : createMERecipeInfo(packet, syncManager))
            .allowC2S();
        itemOutputSyncer
            .setChangeListener(() -> notifyMERecipeHandler(recipeHandler, itemOutputSyncer, fluidOutputSyncer));
        fluidOutputSyncer
            .setChangeListener(() -> notifyMERecipeHandler(recipeHandler, itemOutputSyncer, fluidOutputSyncer));

        terminal.child(
            new DynamicSyncedWidget<>().widthRel(0.85f)
                .coverChildrenHeight(0)
                .syncHandler(recipeHandler)
                .setEnabledIf(widget -> meOutputEnabledSyncer.getBoolValue()));
        return terminal;
    }

    private void notifyMERecipeHandler(DynamicSyncHandler recipeHandler,
        GenericListSyncHandler<ItemStackLong> itemOutputSyncer,
        GenericListSyncHandler<FluidStackLong> fluidOutputSyncer) {
        recipeHandler.notifyUpdate(packet -> {
            List<ItemStackLong> items = itemOutputSyncer.getValue();
            packet.writeInt(items.size());
            for (ItemStackLong item : items) writeItemOutput(packet, item);

            List<FluidStackLong> fluids = fluidOutputSyncer.getValue();
            packet.writeInt(fluids.size());
            for (FluidStackLong fluid : fluids) writeFluidOutput(packet, fluid);
        });
    }

    private IWidget createMERecipeInfo(PacketBuffer packet, PanelSyncManager syncManager) {
        Flow column = Flow.column()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .coverChildren(0);

        int itemCount = packet.readInt();
        List<ItemStackLong> itemOutputs = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            ItemStackLong output = readItemOutput(packet);
            if (output.itemStack() != null) itemOutputs.add(output);
        }
        itemOutputs.sort(
            Comparator.comparingLong(ItemStackLong::stackSize)
                .reversed());
        for (ItemStackLong output : itemOutputs) column.child(createItemOutputRow(output, syncManager));

        int fluidCount = packet.readInt();
        List<FluidStackLong> fluidOutputs = new ArrayList<>(fluidCount);
        for (int i = 0; i < fluidCount; i++) {
            FluidStackLong output = readFluidOutput(packet);
            if (output.fluidStack() != null) fluidOutputs.add(output);
        }
        fluidOutputs.sort(
            Comparator.comparingLong(FluidStackLong::amount)
                .reversed());
        for (FluidStackLong output : fluidOutputs) column.child(createFluidOutputRow(output, syncManager));
        return column;
    }

    private IWidget createItemOutputRow(ItemStackLong output, PanelSyncManager syncManager) {
        ItemStack stack = output.itemStack()
            .copy();
        stack.stackSize = 1;
        return Flow.row()
            .fullWidth()
            .height(DISPLAY_ROW_HEIGHT)
            .child(
                new ItemDisplayWidget().disableThemeBackground(true)
                    .disableHoverThemeBackground(true)
                    .displayAmount(false)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .item(stack)
                    .size(DISPLAY_ROW_HEIGHT - 1)
                    .marginRight(2))
            .child(createOutputText(stack.getDisplayName(), output.stackSize(), false, syncManager));
    }

    private IWidget createFluidOutputRow(FluidStackLong output, PanelSyncManager syncManager) {
        FluidStack stack = output.fluidStack()
            .copy();
        stack.amount = 1;
        return Flow.row()
            .fullWidth()
            .height(DISPLAY_ROW_HEIGHT)
            .child(
                new FluidDisplayWidget().disableThemeBackground(true)
                    .disableHoverThemeBackground(true)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .displayAmount(false)
                    .value(stack)
                    .size(DISPLAY_ROW_HEIGHT - 1)
                    .marginRight(2))
            .child(createOutputText(stack.getLocalizedName(), output.amount(), true, syncManager));
    }

    private IWidget createOutputText(String name, long amount, boolean fluid, PanelSyncManager syncManager) {
        IntSyncValue maxProgressSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("maxProgressTime:0");
        String coloredName = EnumChatFormatting.AQUA + name + EnumChatFormatting.RESET;
        return new TextWidget<>(IKey.dynamic(() -> {
            String amountText = EnumChatFormatting.WHITE + " x "
                + EnumChatFormatting.GOLD
                + GTUtility.formatShortenedLong(amount)
                + (fluid ? "L" : "")
                + EnumChatFormatting.WHITE
                + (showOutputRates() ? GTUtility.appendRate(fluid, amount, true, maxProgressSyncer.getIntValue()) : "");
            return GTUtility.truncateText(coloredName, DISPLAY_ROW_CHAR_LIMIT - amountText.length()) + amountText;
        })).height(DISPLAY_ROW_HEIGHT)
            .scale(0.75f)
            .tooltip(t -> {
                if (showOutputRates()) {
                    t.addLine(
                        coloredName + "\n"
                            + GTUtility.appendRate(fluid, amount, false, maxProgressSyncer.getIntValue()));
                }
            });
    }

    private static void writeItemOutput(PacketBuffer packet, ItemStackLong output) {
        NetworkUtils.writeItemStack(packet, output.itemStack());
        packet.writeLong(output.stackSize());
    }

    private static ItemStackLong readItemOutput(PacketBuffer packet) {
        return new ItemStackLong(NetworkUtils.readItemStack(packet), packet.readLong());
    }

    private static void writeFluidOutput(PacketBuffer packet, FluidStackLong output) {
        NetworkUtils.writeFluidStack(packet, output.fluidStack());
        packet.writeLong(output.amount());
    }

    private static FluidStackLong readFluidOutput(PacketBuffer packet) {
        return new FluidStackLong(NetworkUtils.readFluidStack(packet), packet.readLong());
    }

}
