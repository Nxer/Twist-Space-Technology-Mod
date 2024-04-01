package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class MetaBlockBase extends Block implements IMetaBlock {

    public final Set<Integer> usedMetaSet = new HashSet<>(16);
    public final Map<Integer, String[]> tooltipsMap = new HashMap<>(16);
    public final Map<Integer, IIcon> iconMap = new HashMap<>(16);
    public final String unlocalizedName;

    public MetaBlockBase(String unlocalizedName) {
        super(Material.iron);
        this.unlocalizedName = unlocalizedName;
        this.setCreativeTab(GTCMCreativeTabs.TAB_META_BLOCKS);
    }

    // region Abstract

    /**
     * Determines if a specified mob type can spawn on this block, returning false will
     * prevent any mob from spawning on the block.
     *
     * @param type  The Mob Category Type
     * @param world The current world
     * @param x     The X Position
     * @param y     The Y Position
     * @param z     The Z Position
     * @return True to allow a mob of the specified category to spawn, false to prevent it.
     */
    @Override
    public abstract boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z);

    /**
     * Used during tree growth to determine if newly generated leaves can replace this block.
     *
     * @param world The current world
     * @param x     X Position
     * @param y     Y Position
     * @param z     Z Position
     * @return true if this block can be replaced by growing leaves.
     */
    @Override
    public abstract boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z);

    /**
     * Return true if the block is a normal, solid cube. This
     * determines indirect power state, entity ejection from blocks, and a few
     * others.
     *
     * @param world The current world
     * @param x     X Position
     * @param y     Y position
     * @param z     Z position
     * @return True if the block is a full cube
     */
    @Override
    public abstract boolean isNormalCube(IBlockAccess world, int x, int y, int z);

    // endregion
    @Override
    public Set<Integer> getUsedMetaSet() {
        return usedMetaSet;
    }

    @Override
    public Map<Integer, String[]> getTooltipsMap() {
        return tooltipsMap;
    }

    @Override
    public Map<Integer, IIcon> getIconMap() {
        return iconMap;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return getIconMap().get(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        Map<Integer, IIcon> iconMap;
        Set<Integer> usedMetaSet;
        if ((iconMap = getIconMap()) == null || (usedMetaSet = getUsedMetaSet()) == null) {
            throw new NullPointerException("Null in " + this.getUnlocalizedName());
        }
        String root = RESOURCE_ROOT_ID + ":" + getUnlocalizedName() + "/";
        this.blockIcon = reg.registerIcon(root + "0");
        for (int Meta : usedMetaSet) {
            iconMap.put(Meta, reg.registerIcon(root + Meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked" })
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        Set<Integer> usedMetaSet;
        if ((usedMetaSet = getUsedMetaSet()) == null) {
            throw new NullPointerException("Null in " + this.getUnlocalizedName());
        }
        for (int Meta : usedMetaSet) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ);
    }
}
