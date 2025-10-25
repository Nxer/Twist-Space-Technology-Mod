package com.Nxer.TwistSpaceTechnology.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.tile.TileEssentiaDiscretizer;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEssentiaDiscretizer extends Block {

    public static final String MODID = "gtnhcommunitymod";
    public static final String NAME = "essentia_discretizer";

    public BlockEssentiaDiscretizer() {
        super(Material.iron);
        this.setBlockName(NAME);
        this.setBlockTextureName(MODID + ":" + NAME);
        this.setCreativeTab(TstCreativeTabs.TabMetaBlocks);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f); // Full block
    }

    public BlockEssentiaDiscretizer register() {
        GameRegistry.registerBlock(this, ItemBlockEssentiaDiscretizer.class, NAME);
        GameRegistry.registerTileEntity(TileEssentiaDiscretizer.class, NAME);
        return this;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEssentiaDiscretizer();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(MODID + ":" + NAME);
    }

    @Override
    public ItemStack getPickBlock(net.minecraft.util.MovingObjectPosition target, World world, int x, int y, int z) {
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
            // #en_US Discretizes essentia into fluid forms.
            // #zh_CN 将源质转为物品.
            list.add(EnumChatFormatting.GREEN + TextEnums.tr("tile.essentia.discretizer.tooltip.0"));
            // #tr tile.essentia.discretizer.tooltip.1
            // #en_US Ensure essentia cell is sufficient,otherwise,inserted CrystalEssence will be thorough item and will not be recognized by the infusion provider supplier as essentia.
            // #zh_CN 确保插入的源质元件空间充足,否则插入至物品元件的晶化源质将彻底变为物品,无法被注魔供应器识别为源质.
            list.add(TextEnums.tr("tile.essentia.discretizer.tooltip.1"));
            // #tr tile.essentia.discretizer.tooltip.2
            // #en_US We still recommend using ae2t as the primary option. It has better completion rate and compatibility.This discretizer is merely an alternative.
            // #zh_CN 我们仍然推荐优先使用ae2t,其完成度以及适配度更好,本离散器仅为替代品.
            list.add(TextEnums.tr("tile.essentia.discretizer.tooltip.2"));
            // spotless:on
        }

    }
}
