package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/** Receiver-owned escrow. Only blocks actually removed by this anchor can later be restored. */
public final class StarRingStorage {

    private final BitSet stored = new BitSet();
    private int rotation;

    public boolean hasBlocks() {
        return !stored.isEmpty();
    }

    public boolean complete() {
        return stored.cardinality() == StarRings.cells()
            .size();
    }

    public int rotation() {
        return rotation;
    }

    public boolean owns(TileEntity anchor, int x, int y, int z) {
        int dx = x - anchor.xCoord, dz = z - anchor.zCoord;
        int localX = rotatedX(dx, dz, (4 - rotation) & 3);
        int localZ = rotatedZ(dx, dz, (4 - rotation) & 3);
        int index = StarRings.indexAt(localX, y - anchor.yCoord + StarStructure.RECEIVER_Y, localZ);
        return index >= 0 && stored.get(index);
    }

    public void collect(TileEntity anchor, int rotation) {
        if (hasBlocks()) return;
        this.rotation = rotation & 3;
        World world = anchor.getWorldObj();
        // Validate the complete ring set and loaded chunks before making any mutation.
        for (StarRings.Cell cell : StarRings.cells()) {
            int x = worldX(anchor, cell), y = worldY(anchor, cell), z = worldZ(anchor, cell);
            if (!world.blockExists(x, y, z) || cell.material.block() == null
                || world.getBlock(x, y, z) != cell.material.block()
                || world.getBlockMetadata(x, y, z) != cell.material.meta
                || world.getTileEntity(x, y, z) != null) return;
        }
        for (int i = 0; i < StarRings.cells()
            .size(); i++) {
            StarRings.Cell cell = StarRings.cells()
                .get(i);
            if (world.setBlock(worldX(anchor, cell), worldY(anchor, cell), worldZ(anchor, cell), Blocks.air, 0, 2))
                stored.set(i);
        }
        anchor.markDirty();
        if (!complete()) restore(anchor, false);
    }

    public void restore(TileEntity anchor, boolean removingAnchor) {
        if (!hasBlocks()) return;
        World world = anchor.getWorldObj();
        Map<Integer, Integer> refunds = new HashMap<>();
        for (int i = stored.nextSetBit(0); i >= 0; i = stored.nextSetBit(i + 1)) {
            StarRings.Cell cell = StarRings.cells()
                .get(i);
            int x = worldX(anchor, cell), y = worldY(anchor, cell), z = worldZ(anchor, cell);
            if (!world.blockExists(x, y, z)) {
                if (!removingAnchor) continue;
                world.getChunkFromBlockCoords(x, z);
            }
            if (world.isAirBlock(x, y, z)) {
                if (!world.setBlock(x, y, z, cell.material.block(), cell.material.meta, 2)) continue;
            } else {
                // A player may have filled a vacated position. Preserve it and return our block as an item.
                refunds.merge(cell.material.id, 1, Integer::sum);
            }
            stored.clear(i);
        }
        for (Map.Entry<Integer, Integer> refund : refunds.entrySet()) {
            StarRings.Material material = StarRings.material(refund.getKey());
            int remaining = refund.getValue();
            while (remaining > 0) {
                ItemStack stack = new ItemStack(material.block(), Math.min(64, remaining), material.meta);
                world.spawnEntityInWorld(
                    new EntityItem(world, anchor.xCoord + .5, anchor.yCoord + 1, anchor.zCoord + .5, stack));
                remaining -= stack.stackSize;
            }
        }
        anchor.markDirty();
    }

    private int worldX(TileEntity anchor, StarRings.Cell cell) {
        return anchor.xCoord + rotatedX(cell.x, cell.z, rotation);
    }

    private int worldY(TileEntity anchor, StarRings.Cell cell) {
        return anchor.yCoord + cell.y - StarStructure.RECEIVER_Y;
    }

    private int worldZ(TileEntity anchor, StarRings.Cell cell) {
        return anchor.zCoord + rotatedZ(cell.x, cell.z, rotation);
    }

    private static int rotatedX(int x, int z, int rotation) {
        return switch (rotation) {
            case 1 -> z;
            case 2 -> -x;
            case 3 -> -z;
            default -> x;
        };
    }

    private static int rotatedZ(int x, int z, int rotation) {
        return switch (rotation) {
            case 1 -> -x;
            case 2 -> -z;
            case 3 -> x;
            default -> z;
        };
    }

    public void write(NBTTagCompound tag) {
        tag.setByteArray("storedRings", stored.toByteArray());
        tag.setInteger("ringRotation", rotation);
    }

    public void read(NBTTagCompound tag) {
        stored.clear();
        stored.or(BitSet.valueOf(tag.getByteArray("storedRings")));
        stored.clear(
            StarRings.cells()
                .size(),
            Math.max(
                StarRings.cells()
                    .size(),
                stored.length()));
        rotation = tag.getInteger("ringRotation") & 3;
    }
}
