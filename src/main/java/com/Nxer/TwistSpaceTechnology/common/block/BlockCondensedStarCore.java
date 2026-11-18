package com.Nxer.TwistSpaceTechnology.common.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.block.meta.TstMetaBlock;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;

/** Functional containment anchor; the surrounding ordinary casings use the shared MetaCasing registry. */
public final class BlockCondensedStarCore extends TstMetaBlock {

    public BlockCondensedStarCore() {
        super("CondensedStarCore");
        setHardness(8);
        setResistance(30);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata == 0;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return metadata == 0 ? new TileStarContainment() : null;
    }

    @Override
    public boolean isNotTileEntity() {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileStarContainment receiver)
            receiver.restoreRings(true);
        super.breakBlock(world, x, y, z, block, metadata);
    }
}
