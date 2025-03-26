package com.Nxer.TwistSpaceTechnology.common.item;

public class ItemNamedCircuit extends AbstractTstMetaItem {

    /**
     * To use bart circuit auto modify system, unlocalizedName of item must contain "Circuit"
     * And any other way to add recipe in Circuit Assembly is invalid
     * To prevent similar situations from happening again, register as Meta
     * 
     * @see bartworks.system.material.CircuitGeneration.CircuitImprintLoader
     *      Shit
     */
    public ItemNamedCircuit() {
        // #tr item.MetaItemNamedCircuit.name
        // # Test Item
        // #zh_CN 测试物品
        super("MetaItemNamedCircuit");
        setTextureName("gtnhcommunitymod:MetaItem01/0");
    }
}
