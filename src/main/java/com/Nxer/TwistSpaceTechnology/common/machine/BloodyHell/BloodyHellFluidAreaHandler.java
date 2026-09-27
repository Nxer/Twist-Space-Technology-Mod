package com.Nxer.TwistSpaceTechnology.common.machine.BloodyHell;

import static WayofTime.alchemicalWizardry.ModBlocks.blockLifeEssence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public final class BloodyHellFluidAreaHandler {

    private static final int FLUID_BLOCK_COST = 1000;
    private static final List<int[]> TIER_ONE_POSITIONS = createPositions(TST_BloodyHell.STRUCTURE_BLOOD_1, 20, -4, 20);
    private static final List<int[]> TIER_TWO_POSITIONS = createPositions(TST_BloodyHell.STRUCTURE_BLOOD_2, 20, 19, 20);

    private final TST_BloodyHell machine;

    public BloodyHellFluidAreaHandler(TST_BloodyHell machine) {
        this.machine = machine;
    }

    public int getRequiredAmount(int machineTier) {
        return getPositions(machineTier).size() * FLUID_BLOCK_COST;
    }

    public boolean isAreaLoaded(int machineTier) {
        IGregTechTileEntity base = machine.getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null) return false;
        World world = base.getWorld();
        for (int[] position : getPositions(machineTier)) {
            if (!world.blockExists(
                base.getXCoord() + position[0],
                base.getYCoord() + position[1],
                base.getZCoord() + position[2])) return false;
        }
        return true;
    }

    public boolean isFilled(int machineTier) {
        IGregTechTileEntity base = machine.getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null) return false;
        World world = base.getWorld();
        for (int[] position : getPositions(machineTier)) {
            int x = base.getXCoord() + position[0];
            int y = base.getYCoord() + position[1];
            int z = base.getZCoord() + position[2];
            if (world.getBlock(x, y, z) != blockLifeEssence || world.getBlockMetadata(x, y, z) != 0) return false;
        }
        return true;
    }

    public boolean fill(int machineTier) {
        replaceArea(machineTier, blockLifeEssence, false);
        return isFilled(machineTier);
    }

    public void clear(int machineTier) {
        replaceArea(machineTier, Blocks.air, true);
    }

    private void replaceArea(int machineTier, Block targetBlock, boolean onlyLifeEssence) {
        IGregTechTileEntity base = machine.getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null) return;
        World world = base.getWorld();
        List<int[]> changedPositions = new ArrayList<>();
        for (int[] position : getPositions(machineTier)) {
            int x = base.getXCoord() + position[0];
            int y = base.getYCoord() + position[1];
            int z = base.getZCoord() + position[2];
            if (onlyLifeEssence && world.getBlock(x, y, z) != blockLifeEssence) continue;
            if (world.setBlock(x, y, z, targetBlock, 0, 2)) changedPositions.add(new int[] { x, y, z });
        }
        for (int[] position : changedPositions) {
            world.notifyBlocksOfNeighborChange(position[0], position[1], position[2], targetBlock);
        }
    }

    private static List<int[]> getPositions(int machineTier) {
        return machineTier > 5 ? TIER_TWO_POSITIONS : TIER_ONE_POSITIONS;
    }

    private static List<int[]> createPositions(String[][] structure, int offsetX, int offsetY, int offsetZ) {
        List<int[]> positions = new ArrayList<>();
        for (int x = 0; x < structure.length; x++) {
            for (int y = 0; y < structure[x].length; y++) {
                for (int z = 0; z < structure[x][y].length(); z++) {
                    if (structure[x][y].charAt(z) == 'Z') {
                        positions.add(new int[] { offsetX - x, offsetY - y, offsetZ - z });
                    }
                }
            }
        }
        return Collections.unmodifiableList(positions);
    }
}
