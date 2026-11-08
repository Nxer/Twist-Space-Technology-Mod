package com.Nxer.TwistSpaceTechnology.common.block.meta.casing;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TstMetaBlockGlassCasing extends TstMetaBlockCasing {

    private final Map<Integer, IIcon> topIcons = new HashMap<>();

    public TstMetaBlockGlassCasing(String unlocalizedName, byte textureIdOffsite) {
        super(unlocalizedName, textureIdOffsite);
        setLightOpacity(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        topIcons.clear();
        for (int meta : getVariantIds()) {
            topIcons.put(meta, register.registerIcon(RESOURCE_ROOT_ID + ":" + unlocalizedName + "/" + meta + "_top"));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side < 2 ? topIcons.get(meta) : super.getIcon(side, meta);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlock(x, y, z) == this) {
            int neighborMeta = world.getBlockMetadata(x, y, z);
            if (neighborMeta == world.getBlockMetadata(
                x - Facing.offsetsXForSide[side],
                y - Facing.offsetsYForSide[side],
                z - Facing.offsetsZForSide[side])) {
                return false;
            }
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }
}
