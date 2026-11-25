package com.Nxer.TwistSpaceTechnology.common.init;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.item.ItemActualPattern;
import com.Nxer.TwistSpaceTechnology.common.item.ItemAdder01;
import com.Nxer.TwistSpaceTechnology.common.item.ItemAdderFlask;
import com.Nxer.TwistSpaceTechnology.common.item.ItemAdderIzumik;
import com.Nxer.TwistSpaceTechnology.common.item.ItemAdderRune;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBloodInfusedMiningDrone;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.item.ItemCardigan;
import com.Nxer.TwistSpaceTechnology.common.item.ItemEcoSphereExecutionProtocol;
import com.Nxer.TwistSpaceTechnology.common.item.ItemEcoSphereUpgrade;
import com.Nxer.TwistSpaceTechnology.common.item.ItemFluidSingularity;
import com.Nxer.TwistSpaceTechnology.common.item.ItemHatchUpdateTool;
import com.Nxer.TwistSpaceTechnology.common.item.ItemInfinityFluidStorageCell;
import com.Nxer.TwistSpaceTechnology.common.item.ItemMultiStructuresLinkTool;
import com.Nxer.TwistSpaceTechnology.common.item.ItemNamedCircuit;
import com.Nxer.TwistSpaceTechnology.common.item.ItemProofOfHeroes;
import com.Nxer.TwistSpaceTechnology.common.item.ItemYamato;
import com.Nxer.TwistSpaceTechnology.common.item.TSTGeneratedItem;

public class TstItems {

    public static final ItemAdder01 MetaItem01 = new ItemAdder01();
    public static final ItemBloodInfusedMiningDrone BloodInfusedMiningDrone = new ItemBloodInfusedMiningDrone();

    // #tr item.tst.life_essence_singularity.name
    // # Life Essence Singularity
    // #zh_CN 生命本源奇点
    public static final Item LifeEssenceSingularity = new ItemFluidSingularity(
        "tst.life_essence_singularity",
        0x800000,
        0xC00000);

    // #tr item.tst.distilled_water_singularity.name
    // # Distilled Water Singularity
    // #zh_CN 蒸馏水奇点
    public static final Item DistilledWaterSingularity = new ItemFluidSingularity(
        "tst.distilled_water_singularity",
        0x234B9E,
        0x6DC3F1);

    // #tr item.tst.air_singularity.name
    // # Air Singularity
    // #zh_CN 空气奇点
    public static final Item AirSingularity = new ItemFluidSingularity("tst.air_singularity", 0x638C83, 0xD2F6EB);

    public static final ItemEcoSphereExecutionProtocol EcoSphereExecutionProtocol = new ItemEcoSphereExecutionProtocol();
    public static final ItemEcoSphereUpgrade EcoSphereUpgrade = new ItemEcoSphereUpgrade();
    public static final ItemAdderRune MetaItemRune = new ItemAdderRune();
    public static final ItemAdderIzumik MetaItemIzumik = new ItemAdderIzumik();
    public static final ItemAdderFlask MetaItemFlask = new ItemAdderFlask();
    public static final ItemNamedCircuit MetaItemNamedCircuit = new ItemNamedCircuit();
    public static final ItemBottledChaosCredential BottledChaosCredential = new ItemBottledChaosCredential();

    public static final ItemBottledChaosMachineModule BottledChaosMachineModule = new ItemBottledChaosMachineModule();

    // #tr item.tst.proof_of_heroes.name
    // # 英雄の証
    // #zh_CN 英雄の証
    public static final Item ProofOfHeroes = new ItemProofOfHeroes(
        "ProofOfHeroes",
        EnumRarity.common,
        TstCreativeTabs.TabGeneral).setTextureName("gtnhcommunitymod:ProofOfHeroes");

    // #tr item.tst.proof_of_gods.name
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

    // #tr item.tst.hatch_update_tool.name
    // # Hatch Update Tool
    // #zh_CN 仓室升级工具
    public static final Item HatchUpdateTool = new ItemHatchUpdateTool("HatchUpdateTool", TstCreativeTabs.TabGeneral)
        .setTextureName("gtnhcommunitymod:HatchUpdateTool");

    public static final Item Yamato = new ItemYamato(TstCreativeTabs.TabGeneral);

    // don't use this item, but found its stack instance in the class, remember to copy the stacks!
    @SuppressWarnings("unused")
    public static final TSTGeneratedItem Generated = new TSTGeneratedItem();

    public static final ItemCardigan Cardian = new ItemCardigan();

    public static final Item ActualPattern = new ItemActualPattern();

    // #tr item.tst.infinity_distilled_water_storage_cell.name
    // # ME Infinity Distilled Water Fluid Storage Cell
    // #zh_CN ME无限蒸馏水流体存储元件
    public static final Item InfinityDistilledWaterStorageCell = new ItemInfinityFluidStorageCell(
        "InfinityDistilledWaterStorageCell",
        "item.tst.infinity_distilled_water_storage_cell",
        "ic2distilledwater");

    // #tr item.tst.infinity_life_essence_storage_cell.name
    // # ME Infinity Life Essence Fluid Storage Cell
    // #zh_CN ME无限生命本源流体存储元件
    public static final Item InfinityLifeEssenceStorageCell = new ItemInfinityFluidStorageCell(
        "InfinityLifeEssenceStorageCell",
        "item.tst.infinity_life_essence_storage_cell",
        "lifeessence");

    // #tr item.tst.infinity_air_storage_cell.name
    // # ME Infinity Air Fluid Storage Cell
    // #zh_CN ME无限空气流体存储元件
    public static final Item InfinityAirStorageCell = new ItemInfinityFluidStorageCell(
        "InfinityAirStorageCell",
        "item.tst.infinity_air_storage_cell",
        "air");

}
