package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;

import gregtech.api.items.MetaGeneratedItemX32;

public class TSTGeneratedItem extends MetaGeneratedItemX32 {

    public static ItemStack ResearchOnAncientPA;

    public TSTGeneratedItem() {
        super("metaitem.tst");
        setCreativeTab(TstCreativeTabs.TabGeneral);
        registerAllSubTypes();
    }

    private void registerAllSubTypes() {
        ResearchOnAncientPA = addItem(
            0,
            "Research on Ancient Processing Array",
            "People found an ancient machine called Processing Array, and done researches on it, to find a way to replicate it.");
        GTCMItemList.ResearchOnAncientPA.set(ResearchOnAncientPA);
    }
}
