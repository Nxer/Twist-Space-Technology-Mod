package com.Nxer.TwistSpaceTechnology.common.item;

import com.Nxer.TwistSpaceTechnology.common.api.IHasVariantAndTooltips;

/**
 * An ItemStack Generator used Meta Item System.
 */
public class ItemAdder01 extends AbstractTstMetaItem implements IHasVariantAndTooltips {

    /**
     * Create the basic item MetaItem01.
     */
    public ItemAdder01() {
        // #tr item.MetaItem01.name
        // # Test Item
        // #zh_CN 测试物品
        super("MetaItem01");
        setTextureName("gtnhcommunitymod:MetaItem01/0");
    }

}
