package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import gregtech.api.items.MetaGeneratedItemX32;

public class TSTGeneratedItem extends MetaGeneratedItemX32 {

    public static ItemStack ResearchOnAncientPA;
    public static ItemStack ResearchOnAAL;

    public TSTGeneratedItem() {
        super("metaitem.tst");
        setCreativeTab(TstCreativeTabs.TabGeneral);
        registerAllSubTypes();
    }

    private void registerAllSubTypes() {
        ResearchOnAncientPA = addItem(
            0,
            $ -> "Research on Ancient Processing Array",
            $ -> "People found an ancient machine called Processing Array, and done researches on it, to find a way to replicate it.");
        GTCMItemList.ResearchOnAncientPA.set(ResearchOnAncientPA);

        ResearchOnAAL = addItem(
            1,
            // #tr item.metaitem.tst.1.name
            // # AAL modification log
            // #zh_CN 进阶装配线的改造记录
            $ -> TSTUtils.tr("item.metaitem.tst.1.name"),
            // #tr item.metaitem.tst.1.desc
            // # I need a smarter, more efficient way to assemble things…
            // #zh_CN 我需要更加智能、更加高效的组装技术……
            $ -> TSTUtils.tr("item.metaitem.tst.1.desc"));
        GTCMItemList.ResearchOnAAL.set(ResearchOnAAL);
    }
}
