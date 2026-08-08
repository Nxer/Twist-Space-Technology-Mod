package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures.ASPECT_SLOT_OFF;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_SkypiercerTower;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.gtnewhorizons.aspectrecipeindex.ModItems;
import com.gtnewhorizons.aspectrecipeindex.common.items.ItemAspect;

import gregtech.api.modularui2.GTGuiTextures;
import thaumcraft.api.aspects.Aspect;

public class TST_Gui_SkypiercerTower extends TST_Gui<TST_SkypiercerTower> {

    private static final int CELL_SIZE = 18;
    private static final int COLS = 10;
    private static final int TOOLTIP_DELAY = 500;

    public TST_Gui_SkypiercerTower(TST_SkypiercerTower multiblock) {
        super(multiblock);
    }

    @Override
    public Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(panel, syncManager).child(createAspectSelectionButton(syncManager, panel));
    }

    private IWidget createAspectSelectionButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler aspectPanel = syncManager.syncedPanel(
            "aspectSelectionPanel",
            true,
            (p_syncManager, syncHandler) -> createAspectSelectionPanel(p_syncManager, parent));

        return new ButtonWidget<>().size(18, 18)
            .marginLeft(4)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_WHITELIST)
            .onMousePressed(d -> {
                if (aspectPanel.isPanelOpen()) {
                    aspectPanel.closePanel();
                } else {
                    aspectPanel.openPanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("SkypiercerTower.UI.AspectSelection.name")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel createAspectSelectionPanel(PanelSyncManager syncManager, ModularPanel parent) {
        List<Aspect> aspects = TST_SkypiercerTower.getAllCompoundAspectsSorted();
        int size = aspects.size();

        List<ItemStack> icons = new ArrayList<>(size);
        for (Aspect a : aspects) {
            ItemStack stack = new ItemStack(ModItems.itemAspect, 1);
            ItemAspect.setAspect(stack, a);
            icons.add(stack);
        }

        BooleanSyncValue[] aspectSyncers = new BooleanSyncValue[size];
        for (int i = 0; i < size; i++) {
            final int index = i;
            aspectSyncers[i] = new BooleanSyncValue(
                () -> (multiblock.getAspectSelectionBits() >> index & 1L) != 0,
                val -> {
                    long bits = multiblock.getAspectSelectionBits();
                    if (val) {
                        bits |= (1L << index);
                    } else {
                        bits &= ~(1L << index);
                    }
                    multiblock.setAspectSelectionBits(bits);
                }).allowC2S();
            syncManager.syncValue("aspectToggle_" + i, aspectSyncers[i]);
        }

        BooleanSyncValue selectAllSyncer = new BooleanSyncValue(() -> {
            long bits = multiblock.getAspectSelectionBits();
            long mask = size < 63 ? (1L << size) - 1 : Long.MAX_VALUE;
            return (bits & mask) == mask;
        }, val -> {
            long mask = size < 63 ? (1L << size) - 1 : Long.MAX_VALUE;
            multiblock.setAspectSelectionBits(val ? mask : 0L);
        }).allowC2S();
        syncManager.syncValue("selectAllSyncer", selectAllSyncer);

        List<IWidget> allCells = new ArrayList<>();
        allCells.add(createSelectAllCell(selectAllSyncer));
        for (int i = 0; i < size; i++) {
            allCells.add(createAspectCell(aspects.get(i), icons.get(i), aspectSyncers[i]));
        }

        Grid grid = new Grid().gridOfWidthElements(COLS, allCells, ($x, $y, $i, element) -> element)
            .coverChildren()
            .minElementMargin(1, 1);

        return new ModularPanel("aspectSelectionPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .coverChildren()
            .padding(4)
            .child(grid);
    }

    private IWidget createSelectAllCell(BooleanSyncValue syncer) {
        return new ButtonWidget<>().size(CELL_SIZE, CELL_SIZE)
            .background(new DynamicDrawable(() -> {
                if (syncer.getValue()) {
                    return new DrawableStack(
                        GuiTextures.SLOT_ITEM,
                        new Rectangle().color(0xFFFFD700)
                            .hollow(1)
                            .asIcon()
                            .size(CELL_SIZE, CELL_SIZE));
                } else {
                    return ASPECT_SLOT_OFF;
                }
            }))
            .overlay(
                IKey.str("ALL")
                    .asIcon()
                    .size(12))
            .onMousePressed(d -> {
                syncer.setValue(!syncer.getValue());
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("SkypiercerTower.UI.AspectSelection.SelectAll")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createAspectCell(Aspect aspect, ItemStack icon, BooleanSyncValue syncer) {
        return new ButtonWidget<>().size(CELL_SIZE, CELL_SIZE)
            .background(new DynamicDrawable(() -> {
                if (syncer.getValue()) {
                    return new DrawableStack(
                        GuiTextures.SLOT_ITEM,
                        new Rectangle().color(0xFFFFD700)
                            .hollow(1)
                            .asIcon()
                            .size(CELL_SIZE, CELL_SIZE));
                } else {
                    return ASPECT_SLOT_OFF;
                }
            }))
            .overlay(
                new ItemDrawable(icon).asIcon()
                    .size(16))
            .onMousePressed(d -> {
                syncer.setValue(!syncer.getValue());
                return true;
            })
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang(aspect.getName()));
                t.addLine(IKey.lang("Tag: " + aspect.getTag()));
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
