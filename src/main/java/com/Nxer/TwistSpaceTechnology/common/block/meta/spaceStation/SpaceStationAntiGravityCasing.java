package com.Nxer.TwistSpaceTechnology.common.block.meta.spaceStation;

import com.Nxer.TwistSpaceTechnology.common.block.meta.TstMetaBlockMachine;

public class SpaceStationAntiGravityCasing extends TstMetaBlockMachine {

    public SpaceStationAntiGravityCasing() {
        // #tr tile.SpaceStationAntiGravityBlock.name
        // # Space Station Anti Gravity Block
        // #zh_CN 空间站反重力方块
        super("SpaceStationAntiGravityBlock");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
    }

}
