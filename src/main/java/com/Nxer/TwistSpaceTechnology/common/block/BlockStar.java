package com.Nxer.TwistSpaceTechnology.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStar extends Block {

    public BlockStar() {
        super(Material.iron);
        this.setResistance(20f);
        this.setHardness(-1.0f);
        this.setBlockName("tst.star");
        this.setLightLevel(100.0f);
        this.setCreativeTab(TstCreativeTabs.TabGeneral);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("gtnhcommunitymod:TRANSPARENT");
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
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileStar();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList<>();
    }
}
