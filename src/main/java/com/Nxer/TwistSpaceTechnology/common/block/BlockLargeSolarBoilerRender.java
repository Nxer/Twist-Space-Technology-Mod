package com.Nxer.TwistSpaceTechnology.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.tile.TileLargeSolarBoilerRender;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLargeSolarBoilerRender extends Block {

    public BlockLargeSolarBoilerRender() {
        super(Material.iron);
        this.setResistance(20f);
        this.setHardness(-1.0f);
        this.setCreativeTab(TstCreativeTabs.TabGeneral);
        this.setBlockName("tst.largesolarboilerrender");
        this.setLightLevel(0.0f);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("gtnhcommunitymod:TRANSPARENT");
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canRenderInPass(int a) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileLargeSolarBoilerRender();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public int getRenderType() {
        return -1;
    }
}
