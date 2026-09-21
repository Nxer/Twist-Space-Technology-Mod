package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellInputBus;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_AEStorageCellInputHatch;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusMEGui;
import gregtech.common.gui.modularui.hatch.MTEHatchInputMEGui;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;

public final class TST_AEStorageCellHatchGui {

    private static final float SCIENTIFIC_SLOT_AMOUNT_MAX_SCALE = 0.55f;

    private TST_AEStorageCellHatchGui() {}

    private static String formatSlotAmount(long amount, String unit, int width) {
        String label = NumberFormat.format(amount, NumberFormat.AMOUNT_TEXT) + unit;
        // Keep the normal scaled label for smaller amounts; show long-scale stock the same way in both slots.
        if (amount < 1_000_000_000_000_000_000L
            && (label.length() <= 5 || Minecraft.getMinecraft().fontRenderer.getStringWidth(label) * 0.6f <= width))
            return label;
        String digits = Long.toString(amount);
        return digits.charAt(0) + "E" + (digits.length() - 1) + unit;
    }

    private static void drawSlotAmount(long amount, String unit, int x, int width, int height) {
        String label = formatSlotAmount(amount, unit, width);
        GuiDraw.drawScaledAlignedTextInBox(
            label,
            x,
            0,
            width,
            height,
            Alignment.BottomRight,
            label.length() > 2 && label.charAt(1) == 'E' ? SCIENTIFIC_SLOT_AMOUNT_MAX_SCALE : 1f);
    }

    private static IKey getCellSlotName() {
        // #tr AEStorageCellInput.CellSlot
        // # ME storage cell
        // #zh_CN ME存储元件
        return IKey.lang("AEStorageCellInput.CellSlot");
    }

    private static ItemSlot createCellSlot(MTEHatch machine, int slotIndex, Predicate<ItemStack> cellFilter) {
        return new ItemSlot() {

            @Override
            public @Nullable IDrawable getCurrentBackground(WidgetThemeEntry<?> widgetTheme) {
                return IDrawable.of(super.getCurrentBackground(widgetTheme), UITextures.ME_STORAGE_CELL);
            }
        }.slot(
            new ModularSlot(machine.inventoryHandler, slotIndex).singletonSlotGroup()
                .filter(cellFilter))
            .tooltip(tooltip -> tooltip.addLine(getCellSlotName()));
    }

    private static boolean bindSpecialInputSlot(IWidget widget, TST_AEStorageCellInputBus bus) {
        if (widget instanceof ParentWidget<?>parent) {
            for (IWidget child : parent.getChildren()) {
                if (child instanceof ItemSlot itemSlot && itemSlot.getSlot()
                    .getItemHandler() == bus.inventoryHandler
                    && itemSlot.getSlot()
                        .getSlotIndex() == bus.getManualSlot()) {
                    // Keep GT's middle column and tooltip; only its backing slot moves.
                    itemSlot.slot(new ModularSlot(bus.getSpecialInputHandler(), 0).slotGroup("item_inv"));
                    return true;
                }
                if (bindSpecialInputSlot(child, bus)) return true;
            }
        }
        return false;
    }

    public static final class ItemBus extends MTEHatchInputBusMEGui {

        private final MTEHatchInputBusME.Slot[] slots;

        public ItemBus(TST_AEStorageCellInputBus bus, MTEHatchInputBusME.Slot[] slots) {
            super(bus, slots);
            this.slots = slots;
        }

        @Override
        protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
            ParentWidget<?> content = super.createContentSection(panel, syncManager);
            bindSpecialInputSlot(content, (TST_AEStorageCellInputBus) machine);
            if (!content.getChildren()
                .isEmpty()
                && content.getChildren()
                    .get(0) instanceof Flow row
                && row.getChildren()
                    .size() == 3
                && row.getChildren()
                    .get(2) instanceof Grid) {
                row.remove(2);
                row.child(createLongStockSlots(syncManager));
            }
            return content;
        }

        private Grid createLongStockSlots(PanelSyncManager syncManager) {
            IItemHandlerModifiable stock = new IItemHandlerModifiable() {

                @Override
                public int getSlots() {
                    return slots.length;
                }

                @Override
                public ItemStack getStackInSlot(int index) {
                    MTEHatchInputBusME.Slot slot = slots[index];
                    if (slot == null || slot.extracted == null || slot.extractedAmount <= 0) return null;
                    ItemStack display = slot.extracted.copy();
                    display.stackSize = 1;
                    return display;
                }

                @Override
                public ItemStack insertItem(int index, ItemStack stack, boolean simulate) {
                    return stack;
                }

                @Override
                public ItemStack extractItem(int index, int amount, boolean simulate) {
                    return null;
                }

                @Override
                public int getSlotLimit(int index) {
                    return 1;
                }

                @Override
                public void setStackInSlot(int index, ItemStack stack) {}
            };

            return new Grid().coverChildren()
                .gridOfWidthHeight(4, 4, (x, y, index) -> {
                    LongSyncValue amount = new LongSyncValue(
                        () -> ((TST_AEStorageCellInputBus) machine).getGuiAvailableItemAmount(index));
                    syncManager.syncValue("availableItem" + index, amount);
                    return new ItemSlot() {

                        @Override
                        protected void drawSlotAmountText(int ignored, String format) {
                            if (amount.getLongValue() <= 0) return;
                            drawSlotAmount(amount.getLongValue(), "", 1, getArea().width - 1, getArea().height);
                        }

                        @Override
                        public void buildTooltip(ItemStack stack, RichTooltip tooltip) {
                            super.buildTooltip(stack, tooltip);
                            tooltip.addLine(
                                // #tr AEStorageCellInput.AvailableAmount
                                // # Available: %s
                                // #zh_CN 可用数量：%s
                                IKey.lang("AEStorageCellInput.AvailableAmount", formatNumber(amount.getLongValue())));
                        }
                    }.slot(
                        new ModularSlot(stock, index).slotGroup("stock_inv")
                            .accessibility(false, false))
                        .backgroundOverlay(GTGuiTextures.SLOT_ITEM_DARK);
                });
        }

        @Override
        protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
            return super.createBottomRightCornerFlow(panel, syncManager).child(
                createCellSlot(machine, TST_AEStorageCellInputBus.CELL_SLOT, TST_AEStorageCellInputBus::isItemCell));
        }
    }

    public static final class FluidHatch extends MTEHatchInputMEGui {

        public FluidHatch(TST_AEStorageCellInputHatch hatch, MTEHatchInputME.Slot[] slots) {
            super(hatch, slots);
        }

        @Override
        protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
            ParentWidget<?> content = super.createContentSection(panel, syncManager);
            // GT's fluid grid shows int amounts; show the full long stock instead.
            if (!content.getChildren()
                .isEmpty()
                && content.getChildren()
                    .get(0) instanceof Flow row
                && row.getChildren()
                    .size() == 3
                && row.getChildren()
                    .get(2) instanceof Grid) {
                row.remove(2);
                row.child(createPullableStockSlots(syncManager));
            }
            return content;
        }

        private Grid createPullableStockSlots(PanelSyncManager syncManager) {
            return new Grid().coverChildren()
                .gridOfWidthHeight(4, 4, (x, y, index) -> {
                    LongSyncValue amount = new LongSyncValue(
                        () -> ((TST_AEStorageCellInputHatch) machine).getPullableFluidAmount(index));
                    syncManager.syncValue("pullableFluid" + index, amount);
                    return new FluidSlot() {

                        @Override
                        protected boolean displayAmountText() {
                            return false;
                        }

                        @Override
                        public void drawOverlay(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
                            super.drawOverlay(context, widgetTheme);
                            if (getFluidStack() != null && amount.getLongValue() > 0) {
                                drawSlotAmount(
                                    amount.getLongValue(),
                                    getBaseUnit(),
                                    getContentPadding().getLeft(),
                                    getArea().width - getContentPadding().getLeft(),
                                    getArea().height);
                            }
                        }

                        @Override
                        protected void addToolTip(RichTooltip tooltip) {
                            FluidStack fluid = getFluidStack();
                            if (fluid == null) {
                                tooltip.addLine(IKey.lang("modularui2.fluid.empty"));
                                return;
                            }
                            tooltip.addFromFluid(fluid);
                            tooltip.addLine(
                                IKey.lang(
                                    "modularui2.fluid.phantom.amount",
                                    formatNumber(amount.getLongValue()),
                                    getBaseUnit()));
                            addAdditionalFluidInfo(tooltip, fluid);
                            if (!Interactable.hasShiftDown()) tooltip.addLine(IKey.lang("modularui2.tooltip.shift"));
                        }
                    }.syncHandler(new FluidSlotSyncHandler(new ExtractedFluidTank(index)) {

                        @Override
                        protected void tryClickPhantom(MouseData mouseData, ItemStack cursorStack) {}

                        @Override
                        public void tryScrollPhantom(MouseData mouseData) {}
                    }.phantom(true))
                        .backgroundOverlay(GTGuiTextures.SLOT_ITEM_DARK);
                });
        }

        @Override
        protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
            return super.createBottomRightCornerFlow(panel, syncManager)
                .child(createCellSlot(machine, 0, TST_AEStorageCellInputHatch::isFluidCell));
        }
    }
}
