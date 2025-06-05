package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeEnergy;
import tectech.thing.metaTileEntity.pipe.MTEPipeEnergyMirror;
import tectech.util.CommonValues;

public class GT_MetaTileEntity_Pipe_EnergySmart_Reverse extends MTETieredMachineBlock
    implements IConnectsToEnergyTunnel {

    public long Voltage = 0;
    public long Amperes = 0;

    private static Textures.BlockIcons.CustomIcon EMCandyActive, EMpipe;

    public GT_MetaTileEntity_Pipe_EnergySmart_Reverse(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 10, 0, (String) null);
    }

    public GT_MetaTileEntity_Pipe_EnergySmart_Reverse(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMCandyActive = new Textures.BlockIcons.CustomIcon("iconsets/EM_CANDY_ACTIVE");
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_LASER");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new GT_MetaTileEntity_Pipe_EnergySmart_Reverse(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        tag.setLong("Voltage", Voltage);
        tag.setLong("Amperes", Amperes);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        Voltage = tag.getLong("Voltage");
        Amperes = tag.getLong("Amperes");
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM,
            // #tr LaserFocusedSmartNode.description.01
            // # Precise calculation, then, focus!
            // #zh_CN 精密计算, 然后, 聚焦！
            TextEnums.tr("LaserFocusedSmartNode.description.01"),
            // #tr LaserFocusedSmartNode.description.02
            // # {\AQUA}Connects devices in different directions.
            // #zh_CN {\AQUA}连接不同方向的设备.
            TextEnums.tr("LaserFocusedSmartNode.description.02"),
            // #tr LaserFocusedSmartNode.description.03
            // # {\AQUA}Output energy from the front and input from other sides.
            // #zh_CN {\AQUA}从正面输出能量, 从其他面输入.
            TextEnums.tr("LaserFocusedSmartNode.description.03"),
            // #tr LaserFocusedSmartNode.description.04
            // # {\AQUA}As in the original version, please note that if the source voltage is greater than the receiving
            // voltage, it will explode; if it is less, it will not work. Please ensure that the voltages are
            // consistent.
            // #zh_CN {\AQUA}同原始版本一样,请注意,如果源电压大于接受电压会爆炸,小于则不工作,请尽量保持电压一致
            TextEnums.tr("LaserFocusedSmartNode.description.04"), TextEnums.AddByTwistSpaceTechnology.getText() };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
        boolean active, boolean redstone) {
        ITexture pipe = TextureFactory.of(EMpipe);
        ITexture overlay = TextureFactory.of(EMCandyActive, Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA()));
        if (side == facing) {
            return new ITexture[] { pipe, OVERLAYS_ENERGY_OUT_LASER_TT[mTier], overlay };
        }
        return new ITexture[] { pipe, overlay };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public long maxEUInput() {
        return Voltage;
    }

    @Override
    public long maxEUOutput() {
        return Voltage;
    }

    @Override
    public long maxEUStore() {
        return 24 * Voltage * Amperes;
    }

    @Override
    public long maxAmperesOut() {
        return Amperes;
    }

    @Override
    public long maxAmperesIn() {
        return Amperes;
    }

    @Override
    public long getMinimumStoredEU() {
        return Voltage;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer p) {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection f) {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void doExplosion(long power) {}

    @Override
    public void onPostTick(IGregTechTileEntity base, long tick) {
        if (base.isServerSide() && tick % 20 == 0) {
            byte color = base.getColorization();
            if (color < 0) return;

            List<MetaTileEntity> inputs = new ArrayList<>();
            MetaTileEntity output = null;

            ForgeDirection front = base.getFrontFacing();

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (dir == front) {
                    IMetaTileEntity found = findMTE(base, color, dir, false);
                    if (found instanceof MTEHatchEnergyTunnel
                        || found instanceof GT_MetaTileEntity_Pipe_EnergySmart_Reverse) {
                        output = (MetaTileEntity) found;
                    }
                } else {
                    IMetaTileEntity found = findMTE(base, color, dir, true);
                    if (found instanceof MTEHatchDynamoTunnel
                        || found instanceof GT_MetaTileEntity_Pipe_EnergySmart_Reverse) {
                        inputs.add((MetaTileEntity) found);
                    }
                }
            }

            for (MetaTileEntity input : inputs) {
                this.Voltage = input.maxEUOutput();
                this.Amperes = input.maxAmperesOut();
                moveEnergy(input, this);
            }

            if (output != null && base.getStoredEU() > getMinimumStoredEU()) {
                moveEnergy(this, output);
            }
        }
    }

    private void moveEnergy(MetaTileEntity source, MetaTileEntity target) {
        long sourceVoltage = source.maxEUOutput();
        long targetVoltage = target.maxEUInput();

        if (sourceVoltage > targetVoltage) {
            target.doExplosion(sourceVoltage);
            return;
        }
        long voltage = Math.min(sourceVoltage, targetVoltage);
        long amperes = Math.min(source.maxAmperesOut(), target.maxAmperesIn());

        if (voltage <= 0 || amperes <= 0) return;

        long maxTransfer = voltage * amperes * 20;
        long actualTransfer = Math.min(
            maxTransfer,
            Math.min(
                source.getBaseMetaTileEntity()
                    .getStoredEU(),
                target.maxEUStore() - target.getBaseMetaTileEntity()
                    .getStoredEU()));

        if (actualTransfer > 0) {
            source.setEUVar(
                source.getBaseMetaTileEntity()
                    .getStoredEU() - actualTransfer);
            target.setEUVar(
                target.getBaseMetaTileEntity()
                    .getStoredEU() + actualTransfer);
        }
    }

    private IMetaTileEntity findMTE(IGregTechTileEntity base, byte color, ForgeDirection travel, boolean findProvider) {
        ForgeDirection facingSide = travel.getOpposite();

        for (short dist = 1; dist < 1000; dist++) {
            IGregTechTileEntity target = base.getIGregTechTileEntityAtSideAndDistance(travel, dist);
            if (target == null || target.getColorization() != color) break;

            IMetaTileEntity mte = target.getMetaTileEntity();
            if (mte == null) break;

            if (mte instanceof MTEPipeEnergyMirror mirror) {
                ForgeDirection nextTravel = mirror.getBendDirection(facingSide); // 修正方向
                return findMTE(mirror.getBaseMetaTileEntity(), color, nextTravel, findProvider);
            }

            if (findProvider) {
                if (mte instanceof MTEHatchDynamoTunnel && facingSide == target.getFrontFacing()) return mte;
                if (mte instanceof GT_MetaTileEntity_Pipe_EnergySmart_Reverse && facingSide != target.getFrontFacing())
                    return mte;
            } else {
                if (mte instanceof MTEHatchEnergyTunnel && facingSide == target.getFrontFacing()) return mte;
                if (mte instanceof GT_MetaTileEntity_Pipe_EnergySmart_Reverse && facingSide != target.getFrontFacing())
                    return mte;
            }

            if (mte instanceof MTEPipeEnergy pipe) {
                if (pipe.connectionCount < 2) break;
                pipe.markUsed();
            } else {
                break;
            }
        }

        return null;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity te, int i, ForgeDirection d, ItemStack s) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity te, int i, ForgeDirection d, ItemStack s) {
        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return true;
    }
}
