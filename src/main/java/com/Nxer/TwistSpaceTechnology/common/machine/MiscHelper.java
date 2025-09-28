package com.Nxer.TwistSpaceTechnology.common.machine;

import static gregtech.api.util.GTModHandler.getModItem;
import static tectech.thing.CustomItemList.astralArrayFabricator;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.Mods;
import gregtech.api.util.recipe.Scanning;

public class MiscHelper {

    public static ItemStack ASTRAL_ARRAY_FABRICATOR;
    public static ItemStack CRITICAL_PHOTON;
    public static Item PickaxeOfTheCore;
    public static Item BoundPickaxe;
    public static Item TerraShatterer;

    public static Fluid UnknowWater;
    public static FluidStack water;
    public static FluidStack distilledWater;

    public static void initStatics() {
        ASTRAL_ARRAY_FABRICATOR = astralArrayFabricator.get(1);
        CRITICAL_PHOTON = GTCMItemList.CriticalPhoton.get(1);
        water = new FluidStack(FluidRegistry.WATER, 1);

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

        UnknowWater = FluidRegistry.getFluid("unknowwater");
        if (UnknowWater == null) {
            UnknowWater = FluidRegistry.getFluid("water");
        }

        distilledWater = FluidRegistry.getFluidStack("ic2distilledwater", 1);
        if (distilledWater == null) {
            distilledWater = water;
        }

    }

    public static Scanning scanningLV(int tick) {
        return new Scanning(tick, 30);
    }

}
