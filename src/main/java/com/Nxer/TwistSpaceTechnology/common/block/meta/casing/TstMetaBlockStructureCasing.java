package com.Nxer.TwistSpaceTechnology.common.block.meta.casing;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.block.IHasMoreBlockInfo;
import com.Nxer.TwistSpaceTechnology.common.block.meta.AbstractTstMetaBlock;

import gregtech.api.GregTechAPI;

/**
 * A base block class of GregTech multiblock machine structure casings without need for textures.
 * <p>
 * For example, tiered blocks.
 */
public class TstMetaBlockStructureCasing extends AbstractTstMetaBlock implements IHasMoreBlockInfo {

    public TstMetaBlockStructureCasing(String unlocalizedName) {
        super(unlocalizedName);
        this.setHardness(8.0F);
        this.setResistance(5.0F);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 1;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public float getExplosionResistance(Entity aTNT) {
        return Blocks.iron_block.getExplosionResistance(aTNT);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isNoMobSpawn() {
        return true;
    }

    @Override
    public boolean isNotTileEntity() {
        return true;
    }
}
