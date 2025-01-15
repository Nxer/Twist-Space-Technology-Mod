/*
 * Copyright (c) 2017 Azanor
 * MIT License
 */

package com.Nxer.TwistSpaceTechnology.common.tile;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockArcaneHole;

import net.minecraft.tileentity.TileEntity;

import thaumcraft.common.Thaumcraft;

@SuppressWarnings("unused")
public class TileArcaneHole extends TileEntity {

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) this.surroundWithSparkles();
    }

    private void surroundWithSparkles() {
        boolean yp = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord)
            .isOpaqueCube();
        boolean xp = this.worldObj.getBlock(this.xCoord + 1, this.yCoord, this.zCoord)
            .isOpaqueCube();
        boolean zp = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord + 1)
            .isOpaqueCube();
        boolean yn = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord)
            .isOpaqueCube();
        boolean xn = this.worldObj.getBlock(this.xCoord - 1, this.yCoord, this.zCoord)
            .isOpaqueCube();
        boolean zn = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord - 1)
            .isOpaqueCube();
        boolean b1 = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) != BlockArcaneHole;
        boolean b2 = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) != BlockArcaneHole;
        boolean b3 = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord - 1) != BlockArcaneHole;
        boolean b4 = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord + 1) != BlockArcaneHole;
        boolean b5 = this.worldObj.getBlock(this.xCoord - 1, this.yCoord, this.zCoord) != BlockArcaneHole;
        boolean b6 = this.worldObj.getBlock(this.xCoord + 1, this.yCoord, this.zCoord) != BlockArcaneHole;
        if (!xp && yp && b6) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) (this.yCoord + 1),
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!xn && yp && b5) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) (this.yCoord + 1),
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!zp && yp && b4) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) (this.yCoord + 1),
                (float) (this.zCoord + 1),
                2);
        }

        if (!zn && yp && b3) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) (this.yCoord + 1),
                (float) this.zCoord,
                2);
        }

        if (!xp && yn && b6) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) this.yCoord,
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!xn && yn && b5) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) this.yCoord,
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!zp && yn && b4) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) this.yCoord,
                (float) (this.zCoord + 1),
                2);
        }

        if (!zn && yn && b3) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) this.yCoord,
                (float) this.zCoord,
                2);
        }

        if (!yp && xp && b1) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) (this.yCoord + 1),
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!yn && xp && b2) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) this.yCoord,
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!zp && xp && b4) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) (this.zCoord + 1),
                2);
        }

        if (!zn && xp && b3) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) this.zCoord,
                2);
        }

        if (!yp && xn && b1) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) (this.yCoord + 1),
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!yn && xn && b2) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) this.yCoord,
                (float) this.zCoord + this.worldObj.rand.nextFloat(),
                2);
        }

        if (!zp && xn && b4) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) (this.zCoord + 1),
                2);
        }

        if (!zn && xn && b3) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) this.zCoord,
                2);
        }

        if (!xp && zp && b6) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) (this.zCoord + 1),
                2);
        }

        if (!xn && zp && b5) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) (this.zCoord + 1),
                2);
        }

        if (!yp && zp && b1) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) (this.yCoord + 1),
                (float) (this.zCoord + 1),
                2);
        }

        if (!yn && zp && b2) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) this.yCoord,
                (float) (this.zCoord + 1),
                2);
        }

        if (!xp && zn && b6) {
            Thaumcraft.proxy.sparkle(
                (float) (this.xCoord + 1),
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) this.zCoord,
                2);
        }

        if (!xn && zn && b5) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord,
                (float) this.yCoord + this.worldObj.rand.nextFloat(),
                (float) this.zCoord,
                2);
        }

        if (!yp && zn && b1) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) (this.yCoord + 1),
                (float) this.zCoord,
                2);
        }

        if (!yn && zn && b2) {
            Thaumcraft.proxy.sparkle(
                (float) this.xCoord + this.worldObj.rand.nextFloat(),
                (float) this.yCoord,
                (float) this.zCoord,
                2);
        }

    }

}
