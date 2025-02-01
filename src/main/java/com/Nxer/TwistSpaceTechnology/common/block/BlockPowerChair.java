package com.Nxer.TwistSpaceTechnology.common.block;

import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.client.audio.Sound;
import com.Nxer.TwistSpaceTechnology.common.entity.EntityMountableBlock;
import com.Nxer.TwistSpaceTechnology.common.tile.TilePowerChair;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPowerChair extends Block {

    public static HashMap<ChunkCoordinates, Sound> PowerChair = new HashMap<>();

    public BlockPowerChair() {
        super(Material.iron);
        this.setResistance(20f);
        this.setHardness(5.0f);
        this.setBlockName("tst.PowerChair");
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.55F, 1.0F);
        this.setCreativeTab(TstCreativeTabs.TabGeneral);
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
    public void onBlockDestroyedByPlayer(World worldIn, int x, int y, int z, int meta) {
        if (worldIn.isRemote) {
            EntityMountableBlock.stopPlaySound(
                (int) (x + 0.5F),
                (int) (y + 0.68F),
                (int) (z + 0.5F));/* Stops playing music when the block is broken */
        }
        super.onBlockDestroyedByPlayer(worldIn, x, y, z, meta);
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
            EntityMountableBlock.PlaySound((int) (x + 0.5F), (int) (y + 0.68F), (int) (z + 0.5F));
        }
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TilePowerChair();
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    public static class ItemBlockPowerChair extends ItemBlock {

        public ItemBlockPowerChair(Block p_i45328_1_) {
            super(p_i45328_1_);
            setHasSubtypes(true);
            setMaxDamage(0);
            this.setCreativeTab(TstCreativeTabs.TabMetaBlocks);
        }

        @Override
        public String getUnlocalizedName(ItemStack aStack) {
            return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
        }

        @Override
        public int getMetadata(int aMeta) {
            return aMeta;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List<String> toolTip,
            final boolean advancedToolTips) {
            if (isShiftKeyDown()) {
                // #tr tooltips.PowerChair.page2.line1
                // # {\LIGHT_PURPLE}If you want it, then you'll have to take it.
                // #zh_CN {\LIGHT_PURPLE}If you want it, then you'll have to take it.
                toolTip.add(TextEnums.tr("tooltips.PowerChair.page2.line1"));
            } else {
                // #tr tooltips.PowerChair.page1.line1
                // # Your portal opening day's over.
                // #zh_CN Your portal opening day's over.
                toolTip.add(TextEnums.tr("tooltips.PowerChair.page1.line1"));
            }

        }
    }
}
