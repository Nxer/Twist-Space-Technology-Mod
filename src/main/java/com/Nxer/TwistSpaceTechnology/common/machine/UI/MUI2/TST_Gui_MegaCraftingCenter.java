package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaCraftingCenter;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;

public class TST_Gui_MegaCraftingCenter extends TST_Gui<TST_MegaCraftingCenter> {

    public TST_Gui_MegaCraftingCenter(TST_MegaCraftingCenter multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createParallelismButton(syncManager, parent));

    }

    public IWidget createParallelismButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler magnificationPanel = syncManager.syncedPanel(
            "magnificationPanel",
            true,
            (p_syncManager, syncHandler) -> openMagnificationPanel(p_syncManager, parent));
        return new ButtonWidget<>().size(18, 18)
            .marginLeft(4)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
            .onMousePressed(d -> {
                if (!magnificationPanel.isPanelOpen()) {
                    magnificationPanel.openPanel();
                } else {
                    magnificationPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("MegaCraftingCenter.UI.MagnificationInfoMenuButton.name")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
        // #tr MegaCraftingCenter.UI.MagnificationInfoMenuButton.name
        // # Pattern Magnification Configuration Menu
        // #zh_CN 样板倍率配置菜单
    }

    public ModularPanel openMagnificationPanel(PanelSyncManager syncManager, ModularPanel parent) {

        return new ModularPanel("magnificationPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 130)
            .child(
                Flow.column()
                    .full()
                    .padding(3)
                    .child(
                        new TextWidget<>(
                            EnumChatFormatting.UNDERLINE + StatCollector
                                .translateToLocal("MegaCraftingCenter.UI.MagnificationInfoMenuButton.name"))
                                    .textAlign(Alignment.Center)
                                    .size(120, 18)
                                    .marginBottom(4))
                    .childIf(
                        // spotless:off
                                                         // #tr MegaCraftingCenter.UI.Magnification.ConfigurationDescription.text
                                                         // # Set actual pattern magnification, actual input/output numbers of patterns will be multiplied by this number.
                                                         // #zh_CN 设置样板实际运行倍率, 实际合成输入输出等于样板数值乘以此参数.
                                                         // spotless:on
                        showMaxParallelRow(),
                        () -> IKey.lang("MegaCraftingCenter.UI.Magnification.ConfigurationDescription.text")
                            .asWidget()
                            .marginBottom(4))
                    .child(makeParallelSetter(syncManager)));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

    }

    public IWidget makeParallelSetter(PanelSyncManager syncManager) {

        IntSyncValue magnificationPanelSyncer = new IntSyncValue(
            multiblock::getPowerPanelMaxParallel,
            multiblock::setPowerPanelMaxParallel).allowC2S();
        syncManager.syncValue("magnificationPanelSyncer", magnificationPanelSyncer);

        return Flow.row()
            .fullWidth()
            .marginBottom(4)
            .height(18)
            .paddingLeft(3)
            .paddingRight(3)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .child(makeParallelConfiguratorTextFieldWidget(magnificationPanelSyncer));
    }

    protected IWidget makeParallelConfiguratorTextFieldWidget(IntSyncValue magnificationPanelSyncer) {
        return new TextFieldWidget().value(magnificationPanelSyncer)
            .setTextAlignment(Alignment.Center)
            .numbersInt(1, Integer.MAX_VALUE)
            .tooltipBuilder(
                t -> t.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.recipe_result.rangedvalue",
                            1,
                            Integer.MAX_VALUE))))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .size(70, 14)
            .marginBottom(4)
            .marginRight(4);
    }

}
