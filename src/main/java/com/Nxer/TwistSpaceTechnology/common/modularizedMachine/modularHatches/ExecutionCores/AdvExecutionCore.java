package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

// TODO Wireless EU costings
public class AdvExecutionCore extends ExecutionCoreBase {

    public AdvExecutionCore(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public AdvExecutionCore(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AdvExecutionCore(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    private UUID ownerUUID;

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public boolean done() {
        // check wireless EU at this moment
        if (!addEUToGlobalEnergyMap(
            ownerUUID,
            BigInteger.valueOf(eut)
                .multiply(
                    BigInteger.valueOf(maxProgressingTime)
                        .multiply(TstUtils.NEGATIVE_ONE)))) {
            shutDown();
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            TwistSpaceTechnology.LOG.info(
                "Advanced Execution Core shut down because of power at x" + mte
                    .getXCoord() + " y" + mte.getYCoord() + " z" + mte.getZCoord());

            return false;
        }

        trySetActive();
        return true;
    }

    @Override
    public boolean useMainMachinePower() {
        return false;
    }

    // region General

    // spotless:off
    protected String[] description;

    @Override
    public String[] getDescription() {
        if (description == null || description.length == 0) {
            description =
                new String[]{
                    // #tr Tooltips.AdvancedExecutionCore.01
                    // # Add an execution core to your modularized machine that uses direct wireless EU energy.
                    // #zh_CN 为你的模块化机器添加一颗直接使用无线EU能源的执行核心.
                    TextEnums.tr("Tooltips.AdvancedExecutionCore.01"),
                    // #tr Tooltips.AdvancedExecutionCore.02
                    // # This execution core uses the machine's mechanical and energy parameters for recipe matching and execution.
                    // #zh_CN 此执行核心使用机器的机制和能源参数进行配方匹配和执行.
                    TextEnums.tr("Tooltips.AdvancedExecutionCore.02"),
                    // #tr Tooltips.AdvancedExecutionCore.03
                    // # Use the machine's voltage parameters to perform overclocking and timing calculations. Similar to normal machines.
                    // #zh_CN 使用机器的电压参数进行超频和耗时计算. 与一般机器类似.
                    TextEnums.tr("Tooltips.AdvancedExecutionCore.03"),
                    // #tr Tooltips.AdvancedExecutionCore.04
                    // # But it does not consume the energy of the machine, but directly uses the wireless EU energy.
                    // #zh_CN 但不消耗机器的能源, 而是直接使用无线EU能源.
                    TextEnums.tr("Tooltips.AdvancedExecutionCore.04"),
                    // #tr Tooltips.AdvancedExecutionCore.05
                    // # It also accepts boost from machine to speed up the process.
                    // #zh_CN 同时也接受机器的速度增幅用以加快进程.
                    TextEnums.tr("Tooltips.AdvancedExecutionCore.05"),
                    TextEnums.AddByTwistSpaceTechnology.getText(),
                    TextEnums.ModularizedMachineSystem.getText(),
                };
        }
        return description;
    }
    // spotless:on

    // endregion

    // region Texture
    protected static Textures.BlockIcons.CustomIcon ActiveFace;
    protected static Textures.BlockIcons.CustomIcon InactiveFace;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ActiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Adv_on");
        InactiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Adv_off");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(ActiveFace) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(InactiveFace) };
    }

    // endregion
}
