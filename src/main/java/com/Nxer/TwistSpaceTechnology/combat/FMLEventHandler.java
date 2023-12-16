package com.Nxer.TwistSpaceTechnology.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class FMLEventHandler {

    public static final FMLEventHandler INSTANCE = new FMLEventHandler();

    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        EntityPlayer pl = event.player;
        final ItemStack stackBoots = pl.inventory.armorItemInSlot(0);
        final ItemStack stackLegs = pl.inventory.armorItemInSlot(1);
        final ItemStack stackBody = pl.inventory.armorItemInSlot(2);
        final ItemStack stackHelmet = pl.inventory.armorItemInSlot(3);
        final ItemStack weildedItem = pl.getHeldItem();

        final Item boots = stackBoots != null ? stackBoots.getItem() : null;
        final Item legs = stackLegs != null ? stackLegs.getItem() : null;
        final Item body = stackBody != null ? stackBody.getItem() : null;
        final Item helmet = stackHelmet != null ? stackHelmet.getItem() : null;
        final Item sniper = weildedItem != null ? weildedItem.getItem() : null;
        if (weildedItem != null) {
            switch (weildedItem.getUnlocalizedName()) {
                case "swordGold":
                    PlayerExtendedProperties.setBonusPlayerStat(pl, "Strength", 50);
                case "swordDiamond":
                    PlayerExtendedProperties.setBonusPlayerStat(pl, "Strength", 100);
            }
        }
    }
}
