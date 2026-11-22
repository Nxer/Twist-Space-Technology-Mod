package com.Nxer.TwistSpaceTechnology.common.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.TstMetaBlockGlowCasing;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Light-emitting casing that anchors the star containment field. */
public final class BlockCondensedStarCore extends TstMetaBlockGlowCasing {

    /** Reserved for the guide's single render anchor. It has no inventory variant. */
    public static final int GUIDE_PREVIEW_META = 15;

    public BlockCondensedStarCore() {
        super("CondensedStarCore", (byte) 64);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata == 0 || metadata == GUIDE_PREVIEW_META;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return hasTileEntity(metadata) ? new TileStarContainment() : null;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) == GUIDE_PREVIEW_META && world.getBlock(x, y, z) == this) return 0;
        return super.getLightValue(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icons) {
        super.registerBlockIcons(icons);
        iconMap.put(GUIDE_PREVIEW_META, icons.registerIcon("gtnhcommunitymod:TRANSPARENT"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection face = ForgeDirection.getOrientation(side);
        return world.getBlockMetadata(x - face.offsetX, y - face.offsetY, z - face.offsetZ) != GUIDE_PREVIEW_META
            && super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public boolean isNotTileEntity() {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileStarContainment receiver)
            receiver.restoreRings(true);
        // The casing's breakBlock handles GT structure updates, so remove the tile entity here.
        if (hasTileEntity(metadata)) world.removeTileEntity(x, y, z);
        super.breakBlock(world, x, y, z, block, metadata);
    }
}
