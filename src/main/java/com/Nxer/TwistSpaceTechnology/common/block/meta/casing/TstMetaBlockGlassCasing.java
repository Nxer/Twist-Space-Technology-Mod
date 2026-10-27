package com.Nxer.TwistSpaceTechnology.common.block.meta.casing;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TstMetaBlockGlassCasing extends TstMetaBlockCasing {

    private final Set<Integer> glassVariants = new HashSet<>();
    private final Map<Integer, IIcon> topIcons = new HashMap<>();

    public TstMetaBlockGlassCasing(String unlocalizedName, byte textureIdOffsite) {
        super(unlocalizedName, textureIdOffsite);
        setLightOpacity(255);
    }

    public ItemStack registerGlassVariant(int meta) {
        ItemStack stack = registerVariant(meta);
        glassVariants.add(meta);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        topIcons.clear();
        for (int meta : glassVariants) {
            topIcons.put(meta, register.registerIcon(RESOURCE_ROOT_ID + ":" + unlocalizedName + "/" + meta + "_top"));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side < 2 && glassVariants.contains(meta) ? topIcons.get(meta) : super.getIcon(side, meta);
    }

    @Override
    public boolean isOpaqueCube() {
        // Minecraft 1.7.10 has no metadata-aware opaque-cube hook.
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return !glassVariants.contains(world.getBlockMetadata(x, y, z));
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return glassVariants.contains(world.getBlockMetadata(x, y, z)) ? 0 : super.getLightOpacity(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlock(x, y, z) == this) {
            int neighborMeta = world.getBlockMetadata(x, y, z);
            if (!glassVariants.contains(neighborMeta) || neighborMeta == world.getBlockMetadata(
                x - Facing.offsetsXForSide[side],
                y - Facing.offsetsYForSide[side],
                z - Facing.offsetsZForSide[side])) {
                return false;
            }
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }
}
