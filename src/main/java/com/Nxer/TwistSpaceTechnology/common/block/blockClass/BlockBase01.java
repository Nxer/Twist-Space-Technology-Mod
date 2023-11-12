package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import static com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01.MetaBlockSet01;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.List;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBase01 extends Block {

    // region Constructors
    protected BlockBase01(Material materialIn) {
        super(materialIn);
    }

    public BlockBase01() {
        this(Material.iron);
        this.setCreativeTab(GTCMCreativeTabs.tabMetaBlock01);
    }

    public BlockBase01(String unlocalizedName, String localName) {
        this();
        this.unlocalizedName = unlocalizedName;
        texter(localName, "blockBase01." + unlocalizedName + ".name");
    }

    // endregion
    // -----------------------
    // region member variables

    private String unlocalizedName;

    // endregion
    // -----------------------
    // region getters

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    // endregion
    // -----------------------
    // region setters

    public void setUnlocalizedName(String aUnlocalizedName) {
        this.unlocalizedName = aUnlocalizedName;
    }

    // endregion
    // -----------------------
    // region Overrides
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < BlockStaticDataClientOnly.iconsBlockMap01.size() ? BlockStaticDataClientOnly.iconsBlockMap01.get(meta) : BlockStaticDataClientOnly.iconsBlockMap01.get(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:MetaBlocks/0");
        for (int Meta : MetaBlockSet01) {
            BlockStaticDataClientOnly.iconsBlockMap01.put(Meta, reg.registerIcon("gtnhcommunitymod:MetaBlocks/" + Meta));
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
