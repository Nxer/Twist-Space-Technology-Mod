package com.Nxer.TwistSpaceTechnology.common.item.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdder01;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdderIzumik;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdderRune;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemHatchUpdateTool;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemPowerChair;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemProofOfHeroes;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemYamato;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.TSTGeneratedItem;

public final class BasicItems {

    public static final ItemAdder01 MetaItem01 = new ItemAdder01(GTCMCreativeTabs.tabMetaItem01);

    public static final ItemAdderRune MetaItemRune = new ItemAdderRune(GTCMCreativeTabs.tabMetaItem01);

    public static final ItemAdderIzumik MetaItemIzumik = new ItemAdderIzumik(GTCMCreativeTabs.tabMetaItem01);

    // #tr item.ProofOfHeroes.name
    // # 英雄の証
    // #zh_CN 英雄の証
    public static final Item ProofOfHeroes = new ItemProofOfHeroes(
        "ProofOfHeroes",
        EnumRarity.common,
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:ProofOfHeroes");

    // #tr item.ProofOfGods.name
    // # Twist Token
    // #zh_CN Twist Token
    public static final Item ProofOfGods = new ItemProofOfHeroes(
        "ProofOfGods",
        EnumRarity.epic,
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:ProofOfGods");

    public static final Item MultiStructuresLinkTool = new ItemMultiStructuresLinkTool(
        GTCMCreativeTabs.tabMultiStructures);

    public static final Item PowerChair = new ItemPowerChair(GTCMCreativeTabs.tabMetaItem01)
        .setTextureName("gtnhcommunitymod:PowerChair");

    // #tr HatchUpdateTool.name
    // # Hatch Update Tool
    // #zh_CN 仓室升级工具
    public static final Item HatchUpdateTool = new ItemHatchUpdateTool(
        "HatchUpdateTool",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:HatchUpdateTool");

    public static final Item Yamato = new ItemYamato(GTCMCreativeTabs.tabMetaItem01);

    // don't use this item, but found its stack instance in the class, remember to copy the stacks!
    @SuppressWarnings("unused")
    public static final TSTGeneratedItem Generated = new TSTGeneratedItem();

}
