package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_Solidify;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class TST_HatchGui_Solidify extends MTEHatchInputBusGui {

    public TST_HatchGui_Solidify(GT_MetaTileEntity_Hatch_Solidify hatch) {
        super(hatch);
    }

    public GT_MetaTileEntity_Hatch_Solidify getMachine() {
        return (GT_MetaTileEntity_Hatch_Solidify) machine;
    }

    @Override
    protected int getDimension() {
        return 1;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        FluidStackTank[] fluidTanks = getMachine().fluidTanks;

        int width = switch (fluidTanks.length) {
            case 2, 4 -> 2;
            case 6 -> 3;
            default -> 1;
        };
        int height = fluidTanks.length >= 4 ? 2 : 1;

        return getOverlappingEmptyContent().child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(1)
                .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_MOLD))
                .build())
            .child(
                new Grid().coverChildren()
                    .gridOfWidthHeight(
                        width,
                        height,
                        ($x, $y, index) -> new FluidSlot().syncHandler(new FluidSlotSyncHandler(fluidTanks[index])))
                    .center());
    }
}
