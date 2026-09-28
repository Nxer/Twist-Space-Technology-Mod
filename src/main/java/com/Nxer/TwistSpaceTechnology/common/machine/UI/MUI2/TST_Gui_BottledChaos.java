package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosRecipeCategory;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;

public class TST_Gui_BottledChaos extends TST_Gui<MM_BottledChaos> {

    private static final String RECIPE_CATEGORY_PANEL = "bottledChaosRecipeCategoryPanel";

    public TST_Gui_BottledChaos(MM_BottledChaos multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createRecipeCategoryButton(syncManager, parent));
    }

    private IWidget createRecipeCategoryButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler panel = syncManager.syncedPanel(
            RECIPE_CATEGORY_PANEL,
            true,
            (panelSyncManager, syncHandler) -> createRecipeCategoryPanel(parent));
        return new ButtonWidget<>().size(18, 18)
            .marginLeft(4)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
            .onMousePressed(clickData -> {
                if (panel.isPanelOpen()) {
                    panel.closePanel();
                } else {
                    panel.openPanel();
                }
                return true;
            })
            .tooltipBuilder(tooltip -> tooltip.addLine(IKey.str("bottled_chaos.gui.recipe_category_configuration")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel createRecipeCategoryPanel(ModularPanel parent) {
        return new ModularPanel(RECIPE_CATEGORY_PANEL).relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 80)
            .child(
                Flow.column()
                    .full()
                    .padding(6)
                    .child(
                        new TextWidget<>(IKey.str("bottled_chaos.gui.recipe_category_configuration"))
                            .textAlign(Alignment.Center)
                            .fullWidth()
                            .height(18))
                    .child(
                        Flow.row()
                            .fullWidth()
                            .height(24)
                            .mainAxisAlignment(Alignment.MainAxis.CENTER)
                            .child(createRecipeCategorySlots()))
                    .child(
                        IKey.str("bottled_chaos.gui.recipe_category_slot_hint")
                            .asWidget()
                            .fullWidth()));
    }

    private Flow createRecipeCategorySlots() {
        Flow slots = Flow.row()
            .coverChildren()
            .childPadding(2);
        for (BottledChaosRecipeCategory category : BottledChaosRecipeCategory.values()) {
            slots.child(createRecipeCategorySlot(category));
        }
        return slots;
    }

    private ItemSlot createRecipeCategorySlot(BottledChaosRecipeCategory category) {
        int slot = category.getCircuitConfiguration() - 1;
        return new ItemSlot().slot(new ModularSlot(multiblock.getRecipeCategoryInventory(), slot) {

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        }.singletonSlotGroup()
            .filter(stack -> multiblock.isRecipeCategoryCredentialForSlot(slot, stack)))
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_INT_CIRCUIT)
            .tooltip(tooltip -> tooltip.addLine(IKey.str(category.getKey())));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));
    }
}
