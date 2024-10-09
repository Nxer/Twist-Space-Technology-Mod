package com.Nxer.TwistSpaceTechnology.common.block.textures;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.block.textures.GTCasingTextures.TSTIcon;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class BloodyHellTextures {

    private static TSTIcon BH_1_Active = new TSTIcon("iconsets/BloodHellActive_1");
    private static TSTIcon BH_1 = new TSTIcon("iconsets/BloodHell_1");
    private static TSTIcon BH_2_Active = new TSTIcon("iconsets/BloodHellActive_2");
    private static TSTIcon BH_2 = new TSTIcon("iconsets/BloodHell_2");
    private static TSTIcon BH_3_Active = new TSTIcon("iconsets/BloodHellActive_3");
    private static TSTIcon BH_3 = new TSTIcon("iconsets/BloodHell_3");
    private static TSTIcon BH_4_Active = new TSTIcon("iconsets/BloodHellActive_4");
    private static TSTIcon BH_4 = new TSTIcon("iconsets/BloodHell_4");
    private static TSTIcon BH_5_Active = new TSTIcon("iconsets/BloodHellActive_5");
    private static TSTIcon BH_5 = new TSTIcon("iconsets/BloodHell_5");
    private static TSTIcon BH_6_Active = new TSTIcon("iconsets/BloodHellActive_6");
    private static TSTIcon BH_6 = new TSTIcon("iconsets/BloodHell_6");
    private static TSTIcon BH_7_Active = new TSTIcon("iconsets/BloodHellActive_7");
    private static TSTIcon BH_7 = new TSTIcon("iconsets/BloodHell_7");
    private static TSTIcon BH_8_Active = new TSTIcon("iconsets/BloodHellActive_8");
    private static TSTIcon BH_8 = new TSTIcon("iconsets/BloodHell_8");
    private static TSTIcon BH_9_Active = new TSTIcon("iconsets/BloodHellActive_9");
    private static TSTIcon BH_9 = new TSTIcon("iconsets/BloodHell_9");

    TSTIcon[] Icons = { BH_1, BH_2, BH_3, BH_4, BH_5, BH_6, BH_7, BH_8, BH_9 };
    TSTIcon[] IconsActive = { BH_1_Active, BH_2_Active, BH_3_Active, BH_4_Active, BH_5_Active, BH_6_Active, BH_7_Active,
        BH_8_Active, BH_9_Active };

    private static int isControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof TST_BloodyHell && tTile.getFrontFacing() == side)
            return tTile.isActive() ? 1 : 2;
        return 0;
    }

    public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final ForgeDirection side) {
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
                return this.IconsActive[aIndex].getIcon();
            }
        }
        return this.Icons[aIndex].getIcon();
    }
}
