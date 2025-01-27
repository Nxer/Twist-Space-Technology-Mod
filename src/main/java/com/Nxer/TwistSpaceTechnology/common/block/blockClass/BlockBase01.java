package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import static com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01.MetaBlockSet01;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBase01 extends Block {

    // region Constructors
    protected BlockBase01(Material materialIn) {
        super(materialIn);
    }

    public BlockBase01(String unlocalizedName) {
        this(Material.iron);
        this.setCreativeTab(GTCMCreativeTabs.TAB_META_BLOCKS);
        this.setBlockName(unlocalizedName);
    }

    public BlockBase01() {
        // #tr tile.MetaBlock01.name
        // # MetaBlock01
        // #zh_CN MetaBlock01
        this("MetaBlock01");
    }

    // endregion
    // -----------------------
    // region member variables

    // endregion
    // -----------------------
    // region getters

    // endregion
    // -----------------------
    // region setters

    // endregion
    // -----------------------
    // region Overrides
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < BlockStaticDataClientOnly.iconsBlockMap01.size()
            ? BlockStaticDataClientOnly.iconsBlockMap01.get(meta)
            : BlockStaticDataClientOnly.iconsBlockMap01.get(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:MetaBlocks/0");
        for (int Meta : MetaBlockSet01) {
            BlockStaticDataClientOnly.iconsBlockMap01
                .put(Meta, reg.registerIcon("gtnhcommunitymod:MetaBlocks/" + Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        for (int Meta : MetaBlockSet01) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    // endregion
}
