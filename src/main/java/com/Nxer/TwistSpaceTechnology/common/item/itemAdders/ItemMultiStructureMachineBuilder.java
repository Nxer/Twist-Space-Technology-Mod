package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import net.minecraft.item.Item;

public class ItemMultiStructureMachineBuilder extends Item {
    public String machineName;

    public ItemMultiStructureMachineBuilder(){
        super();

    }


    @Override
    public Item setHasSubtypes(boolean p_77627_1_) {
        return super.setHasSubtypes(true);
    }
}
