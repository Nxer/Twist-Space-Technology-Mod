package com.Nxer.TwistSpaceTechnology.common.block.textures;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class CasingTextureHandler {

    private static final BloodyHellTextures BHTexture = new BloodyHellTextures();

    public static IIcon handleCasingsTST(final IBlockAccess aWorld, final int xCoord, final int yCoord,
        final int zCoord, final ForgeDirection side, final TSTCasingTextures thisBlock) {
        return BHTexture.handleCasingsTST(aWorld, xCoord, yCoord, zCoord, side, thisBlock);
    }
}
