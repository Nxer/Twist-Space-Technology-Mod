package com.Nxer.TwistSpaceTechnology.common.block.meta.spaceStation;

import com.Nxer.TwistSpaceTechnology.common.block.meta.TstMetaBlockMachine;

public class SpaceStationStructureCasing extends TstMetaBlockMachine {

    public SpaceStationStructureCasing() {
        // #tr tile.SpaceStationStructureBlock.name
        // # Space Station Structure Block
        // #zh_CN 空间站结构方块
        super("SpaceStationStructureBlock");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
    }

}
