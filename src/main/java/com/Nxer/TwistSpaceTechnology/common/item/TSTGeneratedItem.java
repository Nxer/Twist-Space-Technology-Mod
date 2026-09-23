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
        // spotless:off
        ResearchOnAncientPA = addItem(
            0,
            // #tr item.tst.common.MetaItemTST.0.name
            // # Research on Ancient Processing Array
            // #zh_CN 远古处理阵列研究
            $ -> TSTUtils.tr("item.tst.common.MetaItemTST.0.name"),
            // #tr item.tst.common.MetaItemTST.0.tooltip.1
            // # People found an ancient machine called Processing Array, and done researches on it, to find a way to replicate it.
            // #zh_CN 人们发现了一台名为 "处理阵列" 的古老机器, 并对它展开研究, 试图找到复制它的方法.
            $ -> TSTUtils.tr("item.tst.common.MetaItemTST.0.tooltip.1"));
        GTCMItemList.ResearchOnAncientPA.set(ResearchOnAncientPA);

        ResearchOnAAL = addItem(
            1,
            // #tr item.tst.common.MetaItemTST.1.name
            // # AAL modification log
            // #zh_CN 进阶装配线的改造记录
            $ -> TSTUtils.tr("item.tst.common.MetaItemTST.1.name"),
            // #tr item.tst.common.MetaItemTST.1.tooltip.1
            // # I need a smarter, more efficient way to assemble things…
            // #zh_CN 我需要更加智能、更加高效的组装技术……
            $ -> TSTUtils.tr("item.tst.common.MetaItemTST.1.tooltip.1"));
        GTCMItemList.ResearchOnAAL.set(ResearchOnAAL);
        // spotless:on
    }
}
