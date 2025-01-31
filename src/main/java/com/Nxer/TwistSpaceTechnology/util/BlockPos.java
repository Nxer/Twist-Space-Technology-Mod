package com.Nxer.TwistSpaceTechnology.util;

/**
 * @deprecated see {@link com.gtnewhorizon.gtnhlib.blockpos.BlockPos}
 */
@Deprecated
public class BlockPos {

    public int x;
    public int y;
    public int z;

    public BlockPos(int xPos, int yPos, int zPos) {
        x = xPos;
        y = yPos;
        z = zPos;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockPos && x == ((BlockPos) obj).x && y == ((BlockPos) obj).y && z == ((BlockPos) obj).z;
    }
}
