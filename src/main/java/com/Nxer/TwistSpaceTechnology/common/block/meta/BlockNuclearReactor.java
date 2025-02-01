package com.Nxer.TwistSpaceTechnology.common.block.meta;

public class BlockNuclearReactor extends TstMetaBlock {

    public BlockNuclearReactor() {
        // #tr tile.MegaNuclearReactor.name
        // # Mega Nuclear Reactor
        // #zh_CN Mega Nuclear Reactor
        super("MegaNuclearReactor");
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.setHarvestLevel("wrench", 1);
    }

}
