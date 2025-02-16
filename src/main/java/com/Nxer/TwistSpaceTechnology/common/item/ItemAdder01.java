package com.Nxer.TwistSpaceTechnology.common.item;

import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;

/**
 * An ItemStack Generator used Meta Item System.
 * <li>Use {@link com.Nxer.TwistSpaceTechnology.util.TstUtils#registerTstMetaItem(AbstractTstMetaItem, int, String[])} to
 * create your Item at ItemList01.
 *
 */
public class ItemAdder01 extends AbstractTstMetaItem implements IHasTooltips {

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
