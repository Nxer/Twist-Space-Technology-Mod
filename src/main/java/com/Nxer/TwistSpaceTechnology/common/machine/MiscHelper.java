package com.Nxer.TwistSpaceTechnology.common.machine;

import static gregtech.api.util.GTModHandler.getModItem;
import static tectech.thing.CustomItemList.astralArrayFabricator;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.Mods;

public class MiscHelper {

    public static ItemStack ASTRAL_ARRAY_FABRICATOR;
    public static ItemStack CRITICAL_PHOTON;
    public static Item PickaxeOfTheCore;
    public static Item BoundPickaxe;
    public static Item TerraShatterer;

    public static void initStatics() {
        ASTRAL_ARRAY_FABRICATOR = astralArrayFabricator.get(1);
        CRITICAL_PHOTON = GTCMItemList.CriticalPhoton.get(1);

        ItemStack pickaxeOfTheCore = getModItem(Mods.Thaumcraft.ID, "ItemPickaxeElemental", 1);
        if (pickaxeOfTheCore != null) {
            PickaxeOfTheCore = pickaxeOfTheCore.getItem();
        } else {
            PickaxeOfTheCore = Items.iron_pickaxe;
        }

        ItemStack boundPickaxe = getModItem(Mods.BloodMagic.ID, "boundPickaxe", 1);
        if (boundPickaxe != null) {
            BoundPickaxe = boundPickaxe.getItem();
        } else {
            BoundPickaxe = Items.golden_pickaxe;
        }

        ItemStack terraShatterer = getModItem(Mods.Botania.ID, "terraPick", 1);
        if (terraShatterer != null) {
            TerraShatterer = terraShatterer.getItem();
        } else {
            TerraShatterer = Items.diamond_pickaxe;
        }
    }
}
