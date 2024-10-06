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

public final class BasicItems {

    public static final Item MetaItem01 = new ItemAdder01(
        "MetaItem01Base",
        "MetaItem01",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:MetaItem01/0");

    public static final Item MetaItemRune = new ItemAdderRune(
        "MetaItemRuneBase",
        "MetaItemRune",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:MetaItem01/0");

    public static final Item MetaItemIzumik = new ItemAdderIzumik(
        "MetaItemIzumikBase",
        "MetaItemIzumik",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:MetaItem01/0");

    public static final Item ProofOfHeroes = new ItemProofOfHeroes(
        "英雄の証",
        "ProofOfHeroes",
        EnumRarity.common,
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:ProofOfHeroes");

    public static final Item ProofOfGods = new ItemProofOfHeroes(
        "Twist Token",
        "ProofOfGods",
        EnumRarity.epic,
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:ProofOfGods");

    public static final Item MultiStructuresLinkTool = new ItemMultiStructuresLinkTool(
        "Multi-Structures Link Tool",
        "MultiStructuresLinkTool",
        GTCMCreativeTabs.tabMultiStructures);

    public static final Item PowerChair = new ItemPowerChair(
        "Power Chair",
        "PowerChair",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:PowerChair");

    public static final Item HatchUpdateTool = new ItemHatchUpdateTool(
        "Hatch Update Tool",
        "HatchUpdateTool",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:HatchUpdateTool");

    public static final Item Yamato = new ItemYamato("Yamato", "Yamato", GTCMCreativeTabs.tabMetaItem01);

}
