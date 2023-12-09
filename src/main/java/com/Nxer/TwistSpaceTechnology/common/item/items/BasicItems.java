package com.Nxer.TwistSpaceTechnology.common.item.items;

import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.client.GTCMCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdder01;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemProofOfHeroes;

public final class BasicItems {

    public static final Item MetaItem01 = new ItemAdder01(
        "MetaItem01Base",
        "MetaItem01",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:MetaItem01/0");

    public static final Item ProofOfHeroes = new ItemProofOfHeroes(
        "英雄の証",
        "ProofOfHeroes",
        GTCMCreativeTabs.tabMetaItem01).setTextureName("gtnhcommunitymod:ProofOfHeroes");

    public static final Item MultiStructuresLinkTool = new ItemMultiStructuresLinkTool(
        "Multi-Structures Link Tool",
        "MultiStructuresLinkTool",
        GTCMCreativeTabs.tabMultiStructures);

}
