package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.common.FMLLog;
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
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeEnergy;
import tectech.thing.metaTileEntity.pipe.MTEPipeEnergyMirror;
import tectech.util.CommonValues;

public class GT_MetaTileEntity_Pipe_EnergySmart extends MTETieredMachineBlock implements IConnectsToEnergyTunnel {

    public long Voltage;
    public long Amperes;

    public long actualInputAmperes = 0;
    public long actualOutputAmperes = 0;
    public long lastStoredEU = 0;
    private static Textures.BlockIcons.CustomIcon EMCandyActive, EMpipe;

    public GT_MetaTileEntity_Pipe_EnergySmart(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 10, 0, (String) null);
    }

    public GT_MetaTileEntity_Pipe_EnergySmart(String aName, int aTier, String[] aDescription,
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
    public MetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_EnergySmart(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("Voltage", Voltage);
        aNBT.setLong("Amperes", Amperes);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        Voltage = aNBT.getLong("Voltage");
        Amperes = aNBT.getLong("Amperes");
    }

    @Override
    public String[] getDescription() {
        // spotless:off
        return new String[] { CommonValues.TEC_MARK_EM,
            // #tr LaserSmartNode.getDescription.01
            // # Reflect after precise calculation.
            // #zh_CN 精密计算, 然后, 反射！
            TextEnums.tr("LaserSmartNode.getDescription.01"),
            // #tr LaserSmartNode.getDescription.02
            // # {\AQUA}Connects devices in different directions.
            // #zh_CN {\AQUA}连接不同方向的设备.
            TextEnums.tr("LaserSmartNode.getDescription.02"),
            // #tr LaserSmartNode.getDescription.03
            // # {\AQUA}Input energy from the front and output from other sides.
            // #zh_CN {\AQUA}从正面输入能量, 从其他面输出.
            TextEnums.tr("LaserSmartNode.getDescription.03"),
            // #tr LaserFocusedSmartNode.description.04
            // # {\AQUA}Please note that if the source voltage is greater than the receiving voltage, it will explode; if it is less, it will not work. Please ensure that the voltages are consistent.
            // #zh_CN {\AQUA}请注意,如果源电压大于接受电压会爆炸,小于则不工作,请尽量保持电压一致
            TextEnums.tr("LaserFocusedSmartNode.description.04"),
            // #tr LaserFocusedSmartNode.description.05
            // # {\AQUA}Try not to mix the two types of intelligent nodes. In general, it is allowed, but if a loop occurs in the laser network (that is, the output passes through several nodes and then becomes the input), there may be unpredictable consequences.
            // #zh_CN {\AQUA}尽量不要将两种智能节点混用,在一般情况下是允许的,但是如果激光网络中出现环状(即输出经过若干节点后变成输入)则可能会有不可预测的后果.
            TextEnums.tr("LaserFocusedSmartNode.description.05"),
            TextEnums.AddByTwistSpaceTechnology.getText() };
        // spotless:on
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            return new ITexture[] { TextureFactory.of(EMpipe), OVERLAYS_ENERGY_IN_LASER_TT[mTier],
                TextureFactory.of(EMCandyActive, Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA())) };
        } else {
            return new ITexture[] { TextureFactory.of(EMpipe),
                TextureFactory.of(EMCandyActive, Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA())) };
        }
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
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return !isInputFacing(side);
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        // no-op
    }

    @Override
    public void setEUVar(long newEU) {
        long deltaEU = newEU - this.lastStoredEU;

        if (deltaEU > 0) {
            this.actualInputAmperes += (deltaEU / (Voltage * 20));
        } else if (deltaEU < 0) {
            this.actualOutputAmperes += (-deltaEU / (Voltage * 20));
        }

        this.lastStoredEU = newEU;
        super.setEUVar(newEU);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == 0) {
            this.actualInputAmperes = 0;
            this.actualOutputAmperes = 0;
            byte color = getBaseMetaTileEntity().getColorization();
            if (color < 0) {
                return;
            }
            MetaTileEntity dynamo = null;
            List<MetaTileEntity> energies = new ArrayList<>();
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (side == aBaseMetaTileEntity.getFrontFacing()) {
                    // Search for energy provider
                    IMetaTileEntity aMetaTileEntity = findMTE(aBaseMetaTileEntity, color, side, true);
                    if (aMetaTileEntity instanceof MTEHatchDynamoTunnel dynamoTunnel) {
                        dynamo = dynamoTunnel;
                        Voltage = dynamoTunnel.maxEUOutput();
                        Amperes = dynamoTunnel.Amperes;
                    } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergySmart energySmart) {
                        Voltage = energySmart.maxEUOutput();
                        Amperes = energySmart.Amperes;
                    } else
                        if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergySmart_Focusing energyFocuseSmart) {
                            Voltage = energyFocuseSmart.maxEUOutput();
                            Amperes = energyFocuseSmart.Amperes;
                        }
                } else {
                    // Search for energy receiver
                    IMetaTileEntity aMetaTileEntity = findMTE(aBaseMetaTileEntity, color, side, false);
                    if ((aMetaTileEntity instanceof MTEHatchEnergyTunnel
                        || aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergySmart
                        || aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergySmart_Focusing)) {
                        energies.add((MetaTileEntity) aMetaTileEntity);
                    }
                }
            }
            if (dynamo != null) moveEnergy(dynamo, this);
            for (MetaTileEntity energy : energies) {
                if (aBaseMetaTileEntity.getStoredEU() > getMinimumStoredEU()) moveEnergy(this, energy);
            }
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    private void moveEnergy(MetaTileEntity dynamo, MetaTileEntity energy) {
        if (dynamo.maxEUOutput() > energy.maxEUInput()) {
            energy.doExplosion(dynamo.maxEUOutput());
            dynamo.setEUVar(
                dynamo.getBaseMetaTileEntity()
                    .getStoredEU() - dynamo.maxEUOutput());
        } else if (dynamo.maxEUOutput() == energy.maxEUInput()) {
            long diff = Math.min(
                dynamo.maxAmperesOut() * 20L * dynamo.maxEUOutput(),
                Math.min(
                    energy.maxEUStore() - energy.getBaseMetaTileEntity()
                        .getStoredEU(),
                    dynamo.getBaseMetaTileEntity()
                        .getStoredEU()));

            dynamo.setEUVar(
                dynamo.getBaseMetaTileEntity()
                    .getStoredEU() - diff);

            energy.setEUVar(
                energy.getBaseMetaTileEntity()
                    .getStoredEU() + diff);
        }

    }

    private IMetaTileEntity findMTE(IGregTechTileEntity base, byte color, ForgeDirection travelDirection,
        boolean findProvider) {
        if (travelDirection == null) {
            return null;
        }

        ForgeDirection facingSide = travelDirection.getOpposite();

        for (short dist = 1; dist < 1000; dist++) {
            IGregTechTileEntity tGTTileEntity = base.getIGregTechTileEntityAtSideAndDistance(travelDirection, dist);
            if (tGTTileEntity == null || tGTTileEntity.getColorization() != color) {
                break;
            }

            IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();

            if (aMetaTileEntity instanceof MTEPipeEnergyMirror tMirror) {
                ForgeDirection mirrorFacing = tMirror.getBendDirection(facingSide);
                return findMTE(tMirror.getBaseMetaTileEntity(), color, mirrorFacing, findProvider);
            }

            if (findProvider && aMetaTileEntity instanceof MTEHatchDynamoTunnel
                && facingSide == tGTTileEntity.getFrontFacing()) {
                return aMetaTileEntity;
            }

            if (!findProvider && aMetaTileEntity instanceof MTEHatchEnergyTunnel
                && facingSide == tGTTileEntity.getFrontFacing()) {
                return aMetaTileEntity;
            }

            if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergySmart) {
                if ((findProvider && facingSide != tGTTileEntity.getFrontFacing())
                    || (!findProvider && facingSide == tGTTileEntity.getFrontFacing())) {
                    return aMetaTileEntity;
                }
            }

            if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergySmart_Focusing) {
                FMLLog.info("Successful match");
                if ((findProvider && facingSide == tGTTileEntity.getFrontFacing())
                    || (!findProvider && facingSide != tGTTileEntity.getFrontFacing())) {
                    return aMetaTileEntity;
                }
            }

            if (aMetaTileEntity instanceof MTEPipeEnergy pipe) {
                if (pipe.connectionCount < 2) {
                    break;
                } else {
                    pipe.markUsed();
                    continue;
                }
            }
            break;
        }
        return null;
    }

    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("wailaVoltage", this.Voltage);
        tag.setLong("wailaAmperes", this.Amperes);
        tag.setLong("wailaActualInputAmperes", this.actualInputAmperes);
        tag.setLong("wailaActualOutputAmperes", this.actualOutputAmperes);
        tag.setLong("wailaMaxEuStore", this.maxEUStore());
        tag.setLong(
            "wailaGetStoredEU",
            this.getBaseMetaTileEntity()
                .getStoredEU());
    }

    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(EnumChatFormatting.AQUA + "Voltage: " + EnumChatFormatting.GOLD + tag.getLong("wailaVoltage"));
        currentTip.add(EnumChatFormatting.AQUA + "Amperes: " + EnumChatFormatting.GOLD + tag.getLong("wailaAmperes"));
        currentTip.add(
            EnumChatFormatting.AQUA + "Actual Input Amperes: "
                + EnumChatFormatting.GOLD
                + tag.getLong("wailaActualInputAmperes"));
        currentTip.add(
            EnumChatFormatting.AQUA + "Actual Output Amperes: "
                + EnumChatFormatting.GOLD
                + tag.getLong("wailaActualOutputAmperes"));
        currentTip
            .add(EnumChatFormatting.AQUA + "MaxEuStore: " + EnumChatFormatting.GOLD + tag.getLong("wailaMaxEuStore"));
        currentTip
            .add(EnumChatFormatting.AQUA + "NowEuStore: " + EnumChatFormatting.GOLD + tag.getLong("wailaGetStoredEU"));
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return true;
    }
}
