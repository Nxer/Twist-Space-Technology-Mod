package com.Nxer.TwistSpaceTechnology.common.Entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockPowerChair;

public class EntityMountableBlock extends Entity {

    public static final ResourceLocation BGM = new ResourceLocation("gtnhcommunitymod:PowerChair");

    public int orgBlockPosX;
    public int orgBlockPosY;
    public int orgBlockPosZ;
    public Block orgBlock;
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
        this.orgBlock = world.getBlock(x, y, z);
        this.setPosition(mountingX, mountingY, mountingZ);
    }

    public static boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, float hitX,
        float hitY, float hitZ) {
        if (!world.isRemote) {
            double mountingX = (double) x + hitX;
            double mountingY = (double) y + hitY;
            double mountingZ = (double) z + hitZ;
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
            return;
        } else {
            this.setDead();
            if (worldObj.isRemote) {
                BlockPowerChair.stopPlaySound();
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
