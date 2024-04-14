package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import com.Nxer.TwistSpaceTechnology.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.Audio.Sound;
import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.Entity.EntityMountableBlock;
import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPowerChair extends Block {

    public static Sound sound;

    public BlockPowerChair() {
        super(Material.iron);
        this.setResistance(20f);
        this.setHardness(-1.0f);
        this.setBlockName("tst.PowerChair");
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.55F, 1.0F);
        this.setCreativeTab(GTCMCreativeTabs.TAB_META_BLOCKS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("gtnhcommunitymod:TRANSPARENT");
    }

    @Override
    public String getUnlocalizedName() {
        return "BlockPowerChair";
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        int l = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int i1 = world.getBlockMetadata(x, y, z) & 4;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2 | i1, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 1 | i1, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3 | i1, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, i1, 2);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
        float par8, float par9) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TilePowerChair) {
                int metadata = world.getBlockMetadata(x, y, z);
                metadata %= 4;
                switch (metadata) {
                    case 0 -> {
                        player.rotationYaw = 90.0F;
                        EntityMountableBlock.onBlockActivated(world, x, y, z, player, 0.5F, 0.68F, 0.5F);
                    }
                    case 1 -> {
                        player.rotationYaw = -90.0F;
                        EntityMountableBlock.onBlockActivated(world, x, y, z, player, 0.5F, 0.68F, 0.5F);
                    }
                    case 2 -> {
                        player.rotationYaw = 180.0F;
                        EntityMountableBlock.onBlockActivated(world, x, y, z, player, 0.5F, 0.68F, 0.5F);
                    }
                    case 3 -> {
                        player.rotationYaw = 0.0F;
                        EntityMountableBlock.onBlockActivated(world, x, y, z, player, 0.5F, 0.68F, 0.5F);
                    }
                }
            }
        } else {
            PlaySound(x, y, z);
            return false;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    private void PlaySound(int x, int y, int z) {
        if (Config.Enable_PowerChairBGM){
            sound = new Sound(new ResourceLocation("gtnhcommunitymod:PowerChair"), 0.4f, 1.0f, true, x, y, z);
            Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(sound);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void stopPlaySound() {
        if (Config.Enable_PowerChairBGM){
            Minecraft.getMinecraft()
                    .getSoundHandler()
                    .stopSound(sound);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TilePowerChair();
    }

    @Override
    public int getRenderType() {
        return -1;
    }
}
