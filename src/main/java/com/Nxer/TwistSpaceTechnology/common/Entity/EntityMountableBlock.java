package com.Nxer.TwistSpaceTechnology.common.Entity;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.util.BlockPos;

public class EntityMountableBlock extends Entity {

    public static final HashMap<BlockPos, EntityMountableBlock> OCCUPIED = new HashMap<>();

    public EntityMountableBlock(World worldIn) {
        super(worldIn);
        noClip = true;
        height = 0.0001F;
        width = 0.0001F;
    }

    public EntityMountableBlock(World world, BlockPos pos) {
        super(world);
        setPosition(pos.x + 0.5D, pos.y + 0.68D, pos.z + 0.5D);
        noClip = true;
        height = 0.0001F;
        width = 0.0001F;
        OCCUPIED.put(pos, this);
    }

    @Override
    public void onUpdate() {
        if (worldObj.isRemote) {
            if (this.riddenByEntity == null) {
                this.setDead();
            }
        }
    }

    /**
     *
     */
    @Override
    protected void entityInit() {

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     *
     * @param tagCompund
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     *
     * @param tagCompound
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }
}
