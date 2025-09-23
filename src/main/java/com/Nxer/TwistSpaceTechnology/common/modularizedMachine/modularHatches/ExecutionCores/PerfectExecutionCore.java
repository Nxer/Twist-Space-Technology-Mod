package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

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
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class PerfectExecutionCore extends ExecutionCoreBase {

    public PerfectExecutionCore(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public PerfectExecutionCore(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new PerfectExecutionCore(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    private UUID ownerUUID;
    protected String costEU = "";

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public void resetParameters() {
        super.resetParameters();
        costEU = "";
    }

    @Override
    public boolean done() {
        BigInteger costEU = BigInteger.valueOf(eut)
            .multiply(BigInteger.valueOf(maxProgressingTime));
        // check wireless EU at this moment
        if (!addEUToGlobalEnergyMap(ownerUUID, costEU.multiply(TstUtils.NEGATIVE_ONE))) {
            shutDown();
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            TwistSpaceTechnology.LOG.info(
                "Advanced Execution Core shut down because of power at x" + mte
                    .getXCoord() + " y" + mte.getYCoord() + " z" + mte.getZCoord());

            return false;
        }

        this.costEU = GTUtility.formatNumbers(costEU);
        maxProgressingTime = 20;

        trySetActive();
        return true;
    }

    @Override
    public void processWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("hasBeenSetup")) {
            int maxProgressingTime = tag.getInteger("maxProgressingTime");
            if (maxProgressingTime > 0) {
                // spotless:off
                currentTip.add(
                    // #tr Waila.PerfectExecutionCore.1
                    // # Total progressing time
                    // #zh_CN 总耗时
                    TextEnums.tr("Waila.PerfectExecutionCore.1") + " : "
                        + maxProgressingTime + " tick ("
                        + (maxProgressingTime / 20) + "s)");
                int progressedTime = tag.getInteger("progressedTime");
                currentTip.add(
                    // #tr Waila.ExecutionCore.2
                    // # Progressed time
                    // #zh_CN 已执行时间
                    TextEnums.tr("Waila.ExecutionCore.2") + " : "
                        + progressedTime + " tick ("
                        + (progressedTime / 20) + "s)"
                );
                String costEU = tag.getString("costEU");
                if (costEU != null && !costEU.isEmpty()) {
                    currentTip.add(
                        EnumChatFormatting.AQUA + TextEnums.tr("Waila.TST_MiracleDoor.1")
                            + EnumChatFormatting.RESET
                            + ": "
                            + EnumChatFormatting.GOLD
                            + costEU
                            + EnumChatFormatting.RESET
                            + " EU");
                }
                // spotless:on
            } else {
                // #tr Waila.ExecutionCore.IsIdle
                // # This execution core is idle.
                // #zh_CN 空闲状态
                currentTip.add(TextEnums.tr("Waila.ExecutionCore.IsIdle"));
            }
        } else {
            // #tr Waila.ExecutionCore.HasNotBeenSetup
            // # This execution core has not been setup.
            // #zh_CN 此执行核心未初始化
            currentTip.add(TextEnums.tr("Waila.ExecutionCore.HasNotBeenSetup"));
        }

    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setString("costEU", costEU);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("costEU", costEU);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        costEU = aNBT.getString("costEU");
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
                    // #tr Tooltips.PerfectExecutionCore.01
                    // # Add a second self to your modularized machine, but more powerful.
                    // #zh_CN 为你的模块化机器添加第二个自我, 但更加强大.
                    TextEnums.tr("Tooltips.PerfectExecutionCore.01"),
                    // #tr Tooltips.PerfectExecutionCore.02
                    // # Use the logic parameters of the machine, but without energy constraints, and parallel infinity.
                    // #zh_CN 使用机器的逻辑参数, 但不受能源限制, 并且并行无限.
                    TextEnums.tr("Tooltips.PerfectExecutionCore.02"),
                    // #tr Tooltips.PerfectExecutionCore.03
                    // # Directly use wireless EU energy.
                    // #zh_CN 直接使用无线EU能源.
                    TextEnums.tr("Tooltips.PerfectExecutionCore.03"),
                    // #tr Tooltips.PerfectExecutionCore.04
                    // # Any task is completed within 1 second.
                    // #zh_CN 任何任务都在 1 秒内完成.
                    TextEnums.tr("Tooltips.PerfectExecutionCore.04"),
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
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Per_on");
        InactiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Per_off");
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
