package com.Nxer.TwistSpaceTechnology.command;

import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import com.Nxer.TwistSpaceTechnology.combat.ArmorEventHandler;
import com.Nxer.TwistSpaceTechnology.combat.BasicPlayerExtendedProperties;
import com.Nxer.TwistSpaceTechnology.combat.PlayerExtendedProperties;
import com.Nxer.TwistSpaceTechnology.combat.StatsDefination;

public class CombatRework_Info extends CommandBase {

    @Override
    public String getCommandName() {
        return "combatinfo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Twist Space Technology Mod Commands";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP pl = getCommandSenderAsPlayer(sender);
        ArmorEventHandler.INSTANCE.updatePlayerStats(pl);
        Map<String, Integer> BasicStats = BasicPlayerExtendedProperties.getStats(pl);
        Map<String, Integer> Stats = PlayerExtendedProperties.getStats(pl);
        sender.addChatMessage(new ChatComponentText("Player: §e" + pl.getCommandSenderName()));
        sender.addChatMessage(new ChatComponentText("-----------------------------------------"));
        sender.addChatMessage(
            new ChatComponentText(
                "Weapon: §e" + (pl.getHeldItem() == null ? "None"
                    : pl.getHeldItem()
                        .getUnlocalizedName())));
        if (pl.getHeldItem() != null) {
            StatsDefination.printWeaponStats(pl.getHeldItem(), pl);
        }
        sender.addChatMessage(new ChatComponentText("Armors:"));
        sender.addChatMessage(
            new ChatComponentText(
                "Helmet: §e" + (pl.getEquipmentInSlot(1) == null ? "None"
                    : pl.getEquipmentInSlot(1)
                        .getUnlocalizedName())));
        if (pl.getEquipmentInSlot(0) != null) {
            StatsDefination.printArmorStats(pl.getEquipmentInSlot(1), pl);
        }
        sender.addChatMessage(
            new ChatComponentText(
                "Chestplate: §e" + (pl.getEquipmentInSlot(2) == null ? "None"
                    : pl.getEquipmentInSlot(2)
                        .getUnlocalizedName())));
        if (pl.getEquipmentInSlot(1) != null) {
            StatsDefination.printArmorStats(pl.getEquipmentInSlot(2), pl);
        }
        sender.addChatMessage(
            new ChatComponentText(
                "Legging: §e" + (pl.getEquipmentInSlot(3) == null ? "None"
                    : pl.getEquipmentInSlot(3)
                        .getUnlocalizedName())));
        if (pl.getEquipmentInSlot(2) != null) {
            StatsDefination.printArmorStats(pl.getEquipmentInSlot(3), pl);
        }
        sender.addChatMessage(
            new ChatComponentText(
                "Boots: §e" + (pl.getEquipmentInSlot(4) == null ? "None"
                    : pl.getEquipmentInSlot(4)
                        .getUnlocalizedName())));
        if (pl.getEquipmentInSlot(3) != null) {
            StatsDefination.printArmorStats(pl.getEquipmentInSlot(4), pl);
        }

        sender.addChatMessage(new ChatComponentText("-----------------------------------------"));
        sender.addChatMessage(new ChatComponentText("Basic Stats"));
        for (String statName : BasicStats.keySet())
            sender.addChatMessage(new ChatComponentText(statName + ':' + BasicStats.get(statName)));

        sender.addChatMessage(new ChatComponentText("-----------------------------------------"));
        sender.addChatMessage(new ChatComponentText("§eFinal Stats"));
        for (String statName : Stats.keySet())
            sender.addChatMessage(new ChatComponentText("§e" + statName + ':' + Stats.get(statName)));
        return;
    }
}
