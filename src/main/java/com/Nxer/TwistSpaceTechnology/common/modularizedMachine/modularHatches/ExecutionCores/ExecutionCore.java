package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

public class ExecutionCore extends ExecutionCoreBase {

    public ExecutionCore(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public ExecutionCore(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ExecutionCore(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean done() {
        // do nothing
        trySetActive();
        return true;
    }

    @Override
    public boolean useMainMachinePower() {
        return true;
    }

    // region General

    // spotless:off
    protected String[] description;

    @Override
    public String[] getDescription() {
        if (description == null || description.length == 0) {
            description =
                new String[]{
                    // #tr Tooltips.ExecutionCore.01
                    // # Add an actual execution core to your modularized machine.
                    // #zh_CN 为你的模块化机器添加一颗实际执行核心.
                    TextEnums.tr("Tooltips.ExecutionCore.01"),
                    // #tr Tooltips.ExecutionCore.02
                    // # Machines that support multiple execution cores distribute actual production tasks to these execution cores.
                    // #zh_CN 支持多执行核心的机器将实际生产任务分配到这些执行核心上.
                    TextEnums.tr("Tooltips.ExecutionCore.02"),
                    // #tr Tooltips.ExecutionCore.03
                    // # Of course, a machine that supports multiple execution cores is also an execution core.
                    // #zh_CN 当然, 支持多执行核心的机器本身也是一颗执行核心.
                    TextEnums.tr("Tooltips.ExecutionCore.03"),
                    // #tr Tooltips.ExecutionCore.04
                    // # Multiple execution cores share the machine's input/output and energy and logics.
                    // #zh_CN 多个执行核心共同使用机器的输入输出和能源和逻辑.
                    TextEnums.tr("Tooltips.ExecutionCore.04"),
                    // #tr Tooltips.ExecutionCore.05
                    // # The machine will use idle power to accelerate the execution cores. Same effect as overclocking.
                    // #zh_CN 机器会用闲置的功率为执行核心加速. 与超频效果相同.
                    TextEnums.tr("Tooltips.ExecutionCore.05"),
                    // #tr Tooltips.ExecutionCore.06
                    // # Finally, the execution cores dynamically share the energy input of the machine.
                    // #zh_CN 最终实现执行核心动态均分机器的能源输入.
                    TextEnums.tr("Tooltips.ExecutionCore.06"),
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
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Pri_on");
        InactiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Pri_off");
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
