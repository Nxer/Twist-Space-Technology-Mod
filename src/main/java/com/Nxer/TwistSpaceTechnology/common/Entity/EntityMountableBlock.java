package com.Nxer.TwistSpaceTechnology.common.Entity;

import static com.Nxer.TwistSpaceTechnology.client.Sound.SoundLoader.BGM;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.Audio.Sound;
import com.Nxer.TwistSpaceTechnology.common.block.BlockPowerChair;
import com.Nxer.TwistSpaceTechnology.config.Config;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityMountableBlock extends Entity {

    public int orgBlockPosX;
    public int orgBlockPosY;
    public int orgBlockPosZ;
    public EntityPlayer player;

    public EntityMountableBlock(World worldIn) {
        super(worldIn);
        noClip = true;
        height = 0.0001F;
        width = 0.0001F;
    }

    public EntityMountableBlock(World world, EntityPlayer player, int x, int y, int z, double mountingX,
        double mountingY, double mountingZ) {
        super(world);
        this.player = player;
        this.noClip = true;
        this.preventEntitySpawning = true;
        this.width = 0.0F;
        this.height = 0.0F;
        this.orgBlockPosX = x;
        this.orgBlockPosY = y;
        this.orgBlockPosZ = z;
        this.setPosition(mountingX, mountingY, mountingZ);
    }

    public static boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, float hitX,
        float hitY, float hitZ) {
        double mountingX = (double) x + hitX;
        double mountingY = (double) y + hitY;
        double mountingZ = (double) z + hitZ;
        if (!world.isRemote) {
            EntityMountableBlock entity = new EntityMountableBlock(
                world,
                player,
                x,
                y,
                z,
                mountingX,
                mountingY,
                mountingZ);
            world.spawnEntityInWorld(entity);
            entity.interact(player);
            return true;
        } else {
            return false;
        }
    }

    /* Detects if music is playing and plays music at the right time */

    @SideOnly(Side.CLIENT)
    public static void PlaySound(int x, int y, int z) {
        ChunkCoordinates chunkCoordinates = new ChunkCoordinates(x, y, z);
        if (!BlockPowerChair.PowerChair.containsKey(chunkCoordinates)) {
            if (Config.Enable_PowerChairBGM) {
                Sound sound1 = new Sound(
                    BGM,
                    0.4f,
                    1.0f,
                    chunkCoordinates.posX,
                    chunkCoordinates.posY,
                    chunkCoordinates.posZ);
                BlockPowerChair.PowerChair.put(chunkCoordinates, sound1);
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(sound1);
            }
        } else {
            if (Minecraft.getMinecraft()
                .getSoundHandler()
                .isSoundPlaying(BlockPowerChair.PowerChair.get(chunkCoordinates))) {
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .stopSound(BlockPowerChair.PowerChair.get(chunkCoordinates));
                BlockPowerChair.PowerChair.remove(chunkCoordinates);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void stopPlaySound(int x, int y, int z) {
        ChunkCoordinates chunkcoordinates = new ChunkCoordinates(x, y, z);
        if (BlockPowerChair.PowerChair.containsKey(chunkcoordinates)) {
            Minecraft.getMinecraft()
                .getSoundHandler()
                .stopSound(BlockPowerChair.PowerChair.get(chunkcoordinates));
            BlockPowerChair.PowerChair.remove(chunkcoordinates);
        }
    }

    public boolean interact(EntityPlayer entityplayer) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer
            && this.riddenByEntity != entityplayer) {
            return true;
        } else {
            if (!this.worldObj.isRemote) {
                entityplayer.mountEntity(this);
            }

            return false;
        }
    }

    @Override
    public void onEntityUpdate() {
        this.worldObj.theProfiler.startSection("entityBaseTick");
        if (this.riddenByEntity != null && !this.riddenByEntity.isDead) {
            if (!worldObj.isRemote) {
                Block block = worldObj.getBlock(orgBlockPosX, orgBlockPosY, orgBlockPosZ);
                if (block instanceof BlockPowerChair) {
                    return;
                }
                this.setDead();
            }
        } else {
            this.setDead();
            if (worldObj.isRemote) {
                stopPlaySound(
                    (int) this.posX,
                    (int) this.posY,
                    (int) this.posZ);/* Stops playing music when the entity dies */
            }
        }
        ++this.ticksExisted;
        this.worldObj.theProfiler.endSection();
    }

    /**
     *
     */
    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }
}
