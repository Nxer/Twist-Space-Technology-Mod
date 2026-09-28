package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.machine.UI.NumericTextFieldWidget;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

@IMetaTileEntity.SkipGenerateDescription
public class TST_BottledChaosRecipeTokenHatch extends TST_BottledChaosAuthorizationHatch {

    private int priority = 1;

    // #tr bottled_chaos.recipe_token_hatch.tooltip.0
    // # Holds one Bottled Chaos credential for one processing group.
    // #zh_CN 为一个加工组容纳一个瓶中混沌凭证.

    // #tr bottled_chaos.recipe_token_hatch.tooltip.1
    // # The parallel controller module determines how many credential hatches may be installed.
    // #zh_CN 并行控制器模块决定可安装的凭证仓数量.
    private static final String[] DESCRIPTION = { TSTUtils.tr("bottled_chaos.recipe_token_hatch.tooltip.0"),
        TSTUtils.tr("bottled_chaos.recipe_token_hatch.tooltip.1") };

    public TST_BottledChaosRecipeTokenHatch(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, DESCRIPTION);
    }

    public TST_BottledChaosRecipeTokenHatch(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, description, textures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TST_BottledChaosRecipeTokenHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected boolean isAuthorizationItem(ItemStack stack) {
        return ItemBottledChaosCredential.getCapacity(stack) > 0;
    }

    public int getPriority() {
        return priority;
    }

    private void setPriority(int value) {
        int bounded = Math.max(1, Math.min(15, value));
        if (priority == bounded) return;
        priority = bounded;
        if (getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isServerSide()) markDirty();
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setInteger("bottledChaosPriority", priority);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        priority = Math.max(1, Math.min(15, nbt.getInteger("bottledChaosPriority")));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        // #tr bottled_chaos.recipe_token_hatch.priority
        // # Priority
        // #zh_CN 优先级
        builder.widget(
            TextWidget.localised("bottled_chaos.recipe_token_hatch.priority")
                .setTextAlignment(Alignment.Center)
                .setPos(108, 20)
                .setSize(60, 14))
            .widget(
                new NumericTextFieldWidget().setSetterInt(this::setPriority)
                    .setGetterInt(this::getPriority)
                    .setNumbers(1, 15)
                    .setOnScrollNumbers(1, 1, 1)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                    .setPos(114, 36)
                    .setSize(48, 18));
    }
}
