package com.Nxer.TwistSpaceTechnology.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;

public class BlockPowerChair extends Block {

    public String name;

    protected BlockPowerChair(String name) {
        super(Material.air);
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.name = name;
        this.setCreativeTab(GTCMCreativeTabs.TAB_META_BLOCKS);
    }

    @Override
    public String getUnlocalizedName() {
        return this.name;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TilePowerChair();
    }
}
