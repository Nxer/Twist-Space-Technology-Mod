package com.Nxer.TwistSpaceTechnology.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.tile.TileArcaneHole;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockArcaneHole extends BlockContainer {

    private IIcon placedIcon;

    // #tr tile.ArcaneHole.name
    // # {\BOLD}{\LIGHT_PURPLE}Arcane Empty Space
    // #zh_CN {\BOLD}{\LIGHT_PURPLE}奥术裂隙
    public BlockArcaneHole() {
        super(Material.rock);
        this.setBlockName("ArcaneHole");
        this.setLightLevel(0.8F);
        this.setHardness(30.0F);
        this.setResistance(6000000.0F);
        this.setStepSound(Block.soundTypeGlass);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setCreativeTab(TstCreativeTabs.TabGeneral);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon("thaumcraft:empty"); // ? thaumcraft:empty
        this.placedIcon = register.registerIcon("thaumcraft:blank"); // ? thaumcraft:blank
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta == 1 ? this.placedIcon : this.blockIcon;
    }

    @Override
    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ,
        int meta) {
        return 1;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(TstBlocks.BlockArcaneHole);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(Item.getItemFromBlock(TstBlocks.BlockArcaneHole), 1, 0);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list,
        Entity collider) {}

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileArcaneHole();
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public int damageDropped(int meta) {
        return super.damageDropped(meta);
    }

}
