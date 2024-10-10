package com.Nxer.TwistSpaceTechnology.common.block.textures;

import static com.Nxer.TwistSpaceTechnology.common.block.textures.GTCasingTextures.BloodyHellIcons;
import static com.Nxer.TwistSpaceTechnology.common.block.textures.GTCasingTextures.BloodyHellIconsActive;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class BloodyHellTextures {

    private static int isControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof TST_BloodyHell && tTile.getFrontFacing() == side)
            return tTile.isActive() ? 1 : 2;
        return 0;
    }

    public IIcon handleCasingsTST(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final ForgeDirection side, final GTCasingTextures thisBlock) {
        final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        final int ordinalSide = side.ordinal();
        if (tMeta != 0) {
            return GTCasingTextures.CasingBloodyHell.getIcon();
        }

        int tInvertLeftRightMod = ordinalSide % 2 * 2 - 1;
        switch (ordinalSide / 2) {
            case 0 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        if (isControllerWithSide(aWorld, xCoord + j, yCoord, zCoord + i, side) != 0) {
                            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aWorld
                                .getTileEntity(xCoord + j, yCoord, zCoord + i)).getMetaTileEntity();
                            return getIconByIndex(tMetaTileEntity, 4 - i * 3 - j);
                        }
                    }
                }
            }
            case 1 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        if (isControllerWithSide(aWorld, xCoord + j, yCoord + i, zCoord, side) != 0) {
                            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aWorld
                                .getTileEntity(xCoord + j, yCoord + i, zCoord)).getMetaTileEntity();
                            return getIconByIndex(tMetaTileEntity, 4 + i * 3 - j * tInvertLeftRightMod);
                        }
                    }
                }
            }
            case 2 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        if (isControllerWithSide(aWorld, xCoord, yCoord + i, zCoord + j, side) != 0) {
                            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aWorld
                                .getTileEntity(xCoord, yCoord + i, zCoord + j)).getMetaTileEntity();
                            return getIconByIndex(tMetaTileEntity, 4 + i * 3 + j * tInvertLeftRightMod);
                        }
                    }
                }
            }
        }
        return GTCasingTextures.CasingBloodyHell.getIcon();
    }

    public boolean isMachineRunning(IMetaTileEntity aTile) {
        if (aTile == null) {
            return false;
        } else {
            return aTile.getBaseMetaTileEntity()
                .isActive();
        }
    }

    public boolean isUsingAnimatedTexture(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity != null) {
            if (aMetaTileEntity instanceof TST_BloodyHell) {
                return ((TST_BloodyHell) aMetaTileEntity).usingAnimations();
            }
        }
        return false;
    }

    public IIcon getIconByIndex(IMetaTileEntity aMetaTileEntity, int aIndex) {
        if (isUsingAnimatedTexture(aMetaTileEntity)) {
            if (isMachineRunning(aMetaTileEntity)) {
                return BloodyHellIconsActive[aIndex].getIcon();
            }
        }
        return BloodyHellIcons[aIndex].getIcon();
    }
}
