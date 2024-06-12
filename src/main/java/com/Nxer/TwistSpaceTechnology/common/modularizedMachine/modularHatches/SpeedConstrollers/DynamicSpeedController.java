package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class DynamicSpeedController extends DynamicSpeedControllerBase {

    // region Class Constructor
    public DynamicSpeedController(int aID, String aName, String aNameRegional, int aTier, int maxSpeedMultiplier) {
        super(aID, aName, aNameRegional, aTier);
        this.maxSpeedMultiplier = maxSpeedMultiplier;
    }

    public DynamicSpeedController(String aName, int aTier, int maxSpeedMultiplier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.maxSpeedMultiplier = maxSpeedMultiplier;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new DynamicSpeedController(
            this.mName,
            this.mTier,
            this.maxSpeedMultiplier,
            this.mDescriptionArray,
            this.mTextures);
    }

    // endregion
    protected final int maxSpeedMultiplier;
    protected int speedMultiplier = 1;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("speedMultiplier", speedMultiplier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        speedMultiplier = aNBT.getInteger("speedMultiplier");
    }

    @Override
    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    @Override
    public int getMaxSpeedMultiplier() {
        return maxSpeedMultiplier;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }

        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            // #tr tst.DynamicSpeedController.UI.text.01
            // # Speed Multiplier
            // #zh_CN 速度倍率
            TextWidget.localised("tst.DynamicSpeedController.UI.text.01")
                .setPos(49, 18)
                .setSize(81, 14))
            .widget(
                new TextFieldWidget().setSetterInt(val -> speedMultiplier = val)
                    .setGetterInt(() -> speedMultiplier)
                    .setNumbers(1, maxSpeedMultiplier)
                    .setOnScrollNumbers(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(54, 36)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));
    }

    // region General

    // spotless:off
    protected String[] description;

    @Override
    public String[] getDescription() {
        if (description == null || description.length == 0) {
            description =
                new String[] {
                    // #tr Tooltips.DynamicSpeedController.01
                    // # Speed controller module with adjustable parameters.
                    // #zh_CN 可调参数的速度控制器模块.
                    TextEnums.tr("Tooltips.DynamicSpeedController.01"),
                    // #tr Tooltips.DynamicSpeedController.02
                    // # Provides up to speed x
                    // #zh_CN 最高提供速度增幅
                    TextEnums.tr("Tooltips.DynamicSpeedController.02") + " " + getMaxSpeedMultiplier() + "00%",
                    TextEnums.AddByTwistSpaceTechnology.getText(),
                    TextEnums.ModularizedMachineSystem.getText(),
                };
        }
        return description;
    }
    // spotless:on
}
