package com.Nxer.TwistSpaceTechnology.common.init;

import com.Nxer.TwistSpaceTechnology.common.item.ItemCardigan;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.item.ItemAdder01;
import com.Nxer.TwistSpaceTechnology.common.item.ItemAdderIzumik;
import com.Nxer.TwistSpaceTechnology.common.item.ItemAdderRune;
import com.Nxer.TwistSpaceTechnology.common.item.ItemHatchUpdateTool;
import com.Nxer.TwistSpaceTechnology.common.item.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.item.ItemProofOfHeroes;
import com.Nxer.TwistSpaceTechnology.common.item.ItemYamato;
import com.Nxer.TwistSpaceTechnology.common.item.TSTGeneratedItem;

public class TstItems {

    public static final ItemAdder01 MetaItem01 = new ItemAdder01();
    public static final ItemAdderRune MetaItemRune = new ItemAdderRune();
    public static final ItemAdderIzumik MetaItemIzumik = new ItemAdderIzumik();

    // #tr item.ProofOfHeroes.name
    // # 英雄の証
    // #zh_CN 英雄の証
    public static final Item ProofOfHeroes = new ItemProofOfHeroes(
        "ProofOfHeroes",
        EnumRarity.common,
        TstCreativeTabs.TabGeneral).setTextureName("gtnhcommunitymod:ProofOfHeroes");

    // #tr item.ProofOfGods.name
    // # Twist Token
    // #zh_CN Twist Token
    public static final Item ProofOfGods = new ItemProofOfHeroes(
        "ProofOfGods",
        EnumRarity.epic,
        TstCreativeTabs.TabGeneral).setTextureName("gtnhcommunitymod:ProofOfGods");

    public static final Item MultiStructuresLinkTool = new ItemMultiStructuresLinkTool(
        TstCreativeTabs.TabMultiStructures);

    // public static final Item PowerChair = new ItemPowerChair(TstCreativeTabs.TabGeneral)
    // .setTextureName("gtnhcommunitymod:PowerChair");

    // #tr HatchUpdateTool.name
    // # Hatch Update Tool
    // #zh_CN 仓室升级工具
    public static final Item HatchUpdateTool = new ItemHatchUpdateTool("HatchUpdateTool", TstCreativeTabs.TabGeneral)
        .setTextureName("gtnhcommunitymod:HatchUpdateTool");

    public static final Item Yamato = new ItemYamato(TstCreativeTabs.TabGeneral);

    // don't use this item, but found its stack instance in the class, remember to copy the stacks!
    @SuppressWarnings("unused")
    public static final TSTGeneratedItem Generated = new TSTGeneratedItem();

    public static final ItemCardigan Cardian = new ItemCardigan(0);

}
