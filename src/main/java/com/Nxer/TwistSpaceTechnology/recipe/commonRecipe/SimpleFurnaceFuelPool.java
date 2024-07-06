package com.Nxer.TwistSpaceTechnology.recipe.commonRecipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Simple furnace fuel handler. Registry simple furnace fuel in {@link #registryFuels()} by using
 * {@link #setFuel(ItemStack, int)}
 *
 * @author Nxer
 */
public class SimpleFurnaceFuelPool implements IRecipePool, IFuelHandler {

    public static final Map<Pair<Item, Integer>, Integer> fuelMap = new HashMap<>();

    /**
     * Registry an item stack to fuel map with its burn time.
     *
     * @param fuel     An item stack to be burnable that contains its {@link net.minecraft.item.Item} and its meta.
     * @param burnTick How much tick every this fuel can burn.
     */
    public static void setFuel(ItemStack fuel, int burnTick) {
        fuelMap.put(Pair.of(fuel.getItem(), fuel.getItemDamage()), burnTick);
    }

    /**
     * Registry your item burnable here.
     */
    public void registryFuels() {
        setFuel(GTCMItemList.EnergyShard.get(1), 365 * 24 * 3600);
    }

    @Override
    public void loadRecipes() {
        registryFuels();
        GameRegistry.registerFuelHandler(this);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel == null || fuel.getItem() == null) return 0;

        return fuelMap.getOrDefault(Pair.of(fuel.getItem(), fuel.getItemDamage()), 0);
    }

}
