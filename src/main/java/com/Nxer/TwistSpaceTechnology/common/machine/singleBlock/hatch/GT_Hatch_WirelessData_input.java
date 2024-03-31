package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Mark_TwistSpaceTechnology_TecTech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.Nxer.TwistSpaceTechnology.system.WirelessDataNetWork.WirelessDataPacket;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT_Hatch_WirelessData_input extends GT_MetaTileEntity_Hatch_InputData
    implements IAddGregtechLogo, IAddUIWidgets {

    public long requiredComputation = 1000000;

    private String clientLocale = "en_US";

    // GT_MetaTileEntity_EM_research

    public GT_Hatch_WirelessData_input(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_Hatch_WirelessData_input(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_WirelessData_input(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        if (!aPlayer.isUsingItem()) {
            GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && q == null) {
            q = WirelessDataPacket.downloadData(aBaseMetaTileEntity.getOwnerName(), requiredComputation);
        }

    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO)
                .setSize(18, 18)
                .setPos(151, 63));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        requiredComputation = aNBT.getLong("requiredComputation");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("requiredComputation", requiredComputation);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            TextWidget.localised("tst.wirelessInputData.config.text")
                .setPos(49, 18)
                .setSize(81, 14))
            .widget(
                new TextFieldWidget().setSetterInt(val -> requiredComputation = val)
                    .setGetterLong(() -> requiredComputation)
                    .setNumbers(1, Integer.MAX_VALUE)
                    .setOnScrollNumbers(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(54, 36)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));
    }

    private static String[] tooltips;

    @Override
    public String[] getDescription() {
        if (tooltips == null) {
            tooltips = new String[] { Mark_TwistSpaceTechnology_TecTech,
                // #tr WirelessDataInput.tooltips.01
                // # Wireless Quantum Data Input for Multiblocks
                // #zh_CN 多方块机器无线数据输入
                TextEnums.tr("WirelessDataInput.tooltips.01") };
        }
        return tooltips;
    }
}
