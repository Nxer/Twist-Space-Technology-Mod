package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_MetaTileEntity_Hatch_DualInput;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.common.gui.modularui.hatch.MTEHatchInputBusGui;

public class TST_HatchGui_DualInput extends MTEHatchInputBusGui {

    public TST_HatchGui_DualInput(GT_MetaTileEntity_Hatch_DualInput hatch) {
        super(hatch);
    }

    public GT_MetaTileEntity_Hatch_DualInput getMachine() {
        return (GT_MetaTileEntity_Hatch_DualInput) machine;
    }

    @Override
    protected int getDimension() {
        return 4;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        FluidStackTank[] fluidTanks = getMachine().fluidTanks;

        int width = fluidTanks.length >= 4 ? 2 : 1;
        int height = switch (fluidTanks.length) {
            case 3, 6 -> 3;
            default -> 2;
        };

        return super.createContentSection(panel, syncManager).child(
            new Grid().coverChildren()
                .gridOfWidthHeight(width, height, ($x, $y, index) -> {

                    int i = index;
                    if (i >= fluidTanks.length) {
                        i = fluidTanks.length - 1;
                    }

                    return new FluidSlot().syncHandler(new FluidSlotSyncHandler(fluidTanks[i]));
                }));
    }

}
