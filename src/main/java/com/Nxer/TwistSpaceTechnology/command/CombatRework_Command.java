package com.Nxer.TwistSpaceTechnology.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import com.Nxer.TwistSpaceTechnology.combat.PlayerExtendedProperties;
import com.Nxer.TwistSpaceTechnology.combat.StatsDefination;

public class CombatRework_Command extends CommandBase {

    @Override
    public String getCommandName() {
        return "combatstats";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Twist Space Technology Mod Commands";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        PlayerExtendedProperties stats = (PlayerExtendedProperties) getCommandSenderAsPlayer(sender)
            .getExtendedProperties("COMBAT_STATS");

        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("Basic Stats"));
            for (String statName : stats.CombatStats.keySet())
                sender.addChatMessage(new ChatComponentText(statName + ':' + stats.CombatStats.get(statName)));
            sender.addChatMessage(new ChatComponentText("§eFinal Stats"));
            sender.addChatMessage(new ChatComponentText("§e" + "Strength" + ':' + stats.getStrength()));
            sender.addChatMessage(new ChatComponentText("§e" + "Defence" + ':' + stats.getDefence()));
            sender.addChatMessage(new ChatComponentText("§e" + "Intelligence" + ':' + stats.getIntelligence()));
            sender.addChatMessage(new ChatComponentText("§e" + "CritChance" + ':' + stats.getCritChance()));
            sender.addChatMessage(new ChatComponentText("§e" + "CritDamage" + ':' + stats.getCritDamage()));
            sender.addChatMessage(new ChatComponentText("§e" + "Resistance" + ':' + stats.getResistance()));
            List<ItemStack> gears = stats.getGears();
            for (ItemStack gear : gears) {
                sender.addChatMessage(
                    new ChatComponentText(
                        gear.getEnchantmentTagList()
                            .toString()));
            }
            return;
        }

        switch (args[0]) {
            case "help": {
                TST_CommandMethods.INSTANCE.printHelp(sender);
                break;
            }
            case "set": {
                if (args.length != 3 || !args[2].matches("[+-]?\\d+") || !StatsDefination.AllStats.contains(args[1])) {
                    TST_CommandMethods.INSTANCE.printHelp(sender);
                    break;
                }
                stats.setPlayerStat(args[1], Integer.parseInt(args[2]));
                break;
            }
            default: {
                TST_CommandMethods.INSTANCE.printHelp(sender);
                break;
            }
        }
    }
}
