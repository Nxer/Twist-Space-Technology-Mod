package com.Nxer.TwistSpaceTechnology.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.tile.TileEssentiaDiscretizer;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import appeng.block.AEBaseTileBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEssentiaDiscretizer extends AEBaseTileBlock {

    public static final String MODID = "gtnhcommunitymod";
    public static final String NAME = "essentia_discretizer";
    public static final String RNAME = "tst_essentia_discretizer";

    public BlockEssentiaDiscretizer() {
        super(Material.iron);
        this.setTileEntity(TileEssentiaDiscretizer.class);
        this.setBlockName(NAME);
        this.setBlockTextureName(MODID + ":" + NAME);
        this.setCreativeTab(TstCreativeTabs.TabMetaBlocks);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
    }

    public BlockEssentiaDiscretizer register() {
        GameRegistry.registerBlock(this, ItemBlockEssentiaDiscretizer.class, RNAME);
        GameRegistry.registerTileEntity(TileEssentiaDiscretizer.class, RNAME);
        return this;
    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase player, ItemStack is) {
        if (player instanceof EntityPlayer) {
            final TileEntity te = this.getTileEntity(w, x, y, z);
            if (te instanceof TileEssentiaDiscretizer) {
                ((TileEssentiaDiscretizer) te).getProxy()
                    .setOwner((EntityPlayer) player);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(MODID + ":" + NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(this);
    }

    public static ItemStack stack() {
        return new ItemStack(TstBlocks.BlockEssentiaDiscretizer, 1);
    }

    public static ItemStack stack(int size) {
        return new ItemStack(TstBlocks.BlockEssentiaDiscretizer, size);
    }

    public static class ItemBlockEssentiaDiscretizer extends ItemBlock {

        public ItemBlockEssentiaDiscretizer(Block block) {
            super(block);
            setMaxDamage(0);
            setHasSubtypes(false);
            setCreativeTab(TstCreativeTabs.TabMetaBlocks);
        }

        @Override
        public String getItemStackDisplayName(ItemStack stack) {
            // #tr tile.essentia_discretizer.name
            // #en_US Essentia Discretizer
            // #zh_CN 源质离散器
            return TextEnums.tr("tile.essentia_discretizer.name");
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
            // spotless:off
            // #tr tile.essentia.discretizer.tooltip.0
            // #en_US Link the essentia with the CrystalEssence.
            // #zh_CN 将源质与晶化源质相互关联.
            list.add(EnumChatFormatting.GREEN + TextEnums.tr("tile.essentia.discretizer.tooltip.0"));
            // #tr tile.essentia.discretizer.tooltip.1
            // #en_US Ensure essentia cell is sufficient,otherwise,inserted CrystalEssence will be thorough item and will not be recognized by the infusion provider supplier as essentia.
            // #zh_CN 确保插入的源质元件空间充足,否则插入至物品元件的晶化源质将彻底变为物品,无法被注魔供应器识别为源质.
            list.add(TextEnums.tr("tile.essentia.discretizer.tooltip.1"));
            // #tr tile.essentia.discretizer.tooltip.2
            // #en_US This block was inspired by ae2t. We are grateful for its exploration in this area.
            // #zh_CN 本方块灵感来自ae2t,感谢其对此方面做出的探索.
            list.add(TextEnums.tr("tile.essentia.discretizer.tooltip.2"));
            // #tr tile.essentia.discretizer.tooltip.3
            // #en_US With the update of version 2.9.0, this block has lost its significance.Perhaps it can now be used to quickly convert CrystalEssence back into essentia?
            // #zh_CN 伴随着2.9.0版本的更新,此方块已失去了意义,也许现在可以用来快速转化晶化源质为源质?
            list.add(EnumChatFormatting.RED + TextEnums.tr("tile.essentia.discretizer.tooltip.3"));
            // spotless:on
        }
    }
}
