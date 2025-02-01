package com.Nxer.TwistSpaceTechnology.common.block.meta;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;

/**
 * A subclass of {@link TstMetaBlock}. Additionally, this block will cause machine updates.
 */
public class TstMetaBlockMachine extends TstMetaBlock {

    public TstMetaBlockMachine(String unlocalizedName) {
        super(unlocalizedName);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }
}
