package com.Nxer.TwistSpaceTechnology.command;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DSP_TeamName;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DysonSpheres;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataCell;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.IDSP_IO;

public final class TST_Command extends CommandBase implements IDSP_IO {

    @Override
    public String getCommandName() {
        return "tst";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Twist Space Technology Mod Commands";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            TST_CommandMethods.INSTANCE.printHelp(sender);
            return;
        }

        switch (args[0]) {
            case "help" -> {
                TST_CommandMethods.INSTANCE.printHelp(sender);
                break;
            }

            // join team command
            case "dsp_join" -> {
                // check
                if (args.length < 3) {
                    // spotless:off
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + texter("Invalid command.", "TST_Command.InvalidCommand")));
                    sender.addChatMessage(
                        new ChatComponentText(
                            "↓ Use this to join " + EnumChatFormatting.AQUA + "User1" + EnumChatFormatting.RESET + " to " + EnumChatFormatting.AQUA + "User2" + EnumChatFormatting.RESET + " team ↓"));
                    sender.addChatMessage(
                        new ChatComponentText(
                            "/tst dsp_join " + EnumChatFormatting.AQUA + "User1 " + EnumChatFormatting.AQUA + "User2"));
                    // spotless:on
                    break;
                }

                String waitJoinID = args[1];
                String toJoinID = args[2];
                if (waitJoinID.isEmpty() || toJoinID.isEmpty()) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + texter("Invalid command.", "TST_Command.InvalidCommand")));
                    break;
                }
                joinUserTeam(waitJoinID, toJoinID);
                sender.addChatMessage(
                    new ChatComponentText(
                        "Joined " + EnumChatFormatting.AQUA
                            + waitJoinID
                            + EnumChatFormatting.RESET
                            + " to "
                            + EnumChatFormatting.AQUA
                            + toJoinID
                            + " ."));
                break;
            }

            case "dsp_check" -> {
                String checkName;

                if (args.length < 2) {
                    checkName = sender.getCommandSenderName();
                } else {
                    checkName = args[1];
                }

                if (DSP_TeamName == null) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.BOLD + ""
                                + EnumChatFormatting.RED
                                + "ERROR! "
                                + EnumChatFormatting.RESET
                                + "internal Map DSP_TeamName is "
                                + EnumChatFormatting.RED
                                + "NULL"));
                    break;
                }
                if (!DSP_TeamName.containsKey(checkName)) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            "User " + EnumChatFormatting.AQUA
                                + checkName
                                + EnumChatFormatting.RESET
                                + " has not join a team."));
                    break;
                }

                String teamName = DSP_TeamName.get(checkName);
                if (!DysonSpheres.containsKey(teamName)) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            "User " + EnumChatFormatting.AQUA
                                + checkName
                                + EnumChatFormatting.RESET
                                + " is in team "
                                + EnumChatFormatting.GOLD
                                + teamName
                                + EnumChatFormatting.RESET
                                + "."));
                    sender.addChatMessage(
                        new ChatComponentText(
                            "But team " + EnumChatFormatting.GOLD
                                + teamName
                                + EnumChatFormatting.RESET
                                + " has not created a Dyson Sphere in any galaxy."));
                    break;
                }

                HashMap<DSP_Galaxy, DSP_DataCell> teamsDSP = DysonSpheres.get(teamName);
                if (teamsDSP.isEmpty()) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            "User " + EnumChatFormatting.AQUA
                                + checkName
                                + EnumChatFormatting.RESET
                                + " is in team "
                                + EnumChatFormatting.GOLD
                                + teamName
                                + EnumChatFormatting.RESET
                                + "."));
                    sender.addChatMessage(new ChatComponentText("But team's Dyson Sphere Program is null."));
                }
                sender.addChatMessage(
                    new ChatComponentText(
                        "User " + EnumChatFormatting.AQUA
                            + checkName
                            + EnumChatFormatting.RESET
                            + " is in team "
                            + EnumChatFormatting.GOLD
                            + teamName
                            + EnumChatFormatting.RESET
                            + "."));
                for (Map.Entry<DSP_Galaxy, DSP_DataCell> dataCell : teamsDSP.entrySet()) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            "Galaxy: " + EnumChatFormatting.GOLD
                                + dataCell.getKey()
                                + EnumChatFormatting.RESET
                                + " Dyson Sphere: "
                                + dataCell.getValue()
                                + "."));
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.BLUE + "-----------------------------------------------"));
                }
                break;
            }

            case "dsp_setSolarSail" -> {

                // check
                if (args.length < 2) {
                    // spotless:off
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + texter("Invalid command.", "TST_Command.InvalidCommand")));
                    sender.addChatMessage(
                        new ChatComponentText(
                            "↓ Use this to set Dyson Sphere Solar Sail " + EnumChatFormatting.GREEN + "amount" + EnumChatFormatting.RESET + " of you or your " + EnumChatFormatting.AQUA + "team" + EnumChatFormatting.RESET + " in current galaxy or in " +EnumChatFormatting.AQUA + " dimension's galaxy " +EnumChatFormatting.RESET+ "↓"));
                    sender.addChatMessage(
                        new ChatComponentText(
                            "/tst dsp_setSolarSail " + EnumChatFormatting.GREEN + "amount " + EnumChatFormatting.AQUA + "<dimID> <team name>"));
                    sender.addChatMessage(
                        new ChatComponentText(EnumChatFormatting.BLUE +"-----------------------------------------------"));
                    // spotless:on
                    break;
                }
                long solarSail;
                int dimID = -114;
                String teamName = "defaultPlayerWithErrorInformation";

                try {
                    solarSail = Long.parseLong(args[1]);
                } catch (NumberFormatException e) {
                    sender.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + texter(
                                "Input format error, please check your inputs.",
                                "TST_Command.InputFormatError")));
                    break;
                }

                /*
                 * /tst dsp_setSolarSail <amount> <dimID> <teamName>
                 * /tst dsp_setSolarSail <amount> <dimID>
                 * /tst dsp_setSolarSail <amount>
                 */
                if (args.length == 2) {
                    // use default dimID from player now
                    // use default team from player or init team of player
                    dimID = sender.getEntityWorld().provider.dimensionId;
                    teamName = sender.getCommandSenderName();

                } else if (args.length == 3) {
                    try {
                        dimID = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(
                            new ChatComponentText(
                                EnumChatFormatting.RED + texter(
                                    "Input format error, please check your inputs.",
                                    "TST_Command.InputFormatError")));
                        break;
                    }
                } else {
                    try {
                        dimID = Integer.parseInt(args[2]);
                        teamName = args[3];
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(
                            new ChatComponentText(
                                EnumChatFormatting.RED + texter(
                                    "Input format error, please check your inputs.",
                                    "TST_Command.InputFormatError")));
                        break;
                    }
                }
                DSP_DataCell dataCell = getOrInitDSPData(teamName, dimID);
                dataCell.setDSPSolarSail(solarSail);
                sender.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.GOLD + "Succeed to set Solar Sail amount to "
                            + EnumChatFormatting.GREEN
                            + solarSail
                            + EnumChatFormatting.GOLD
                            + " , team "
                            + EnumChatFormatting.RESET
                            + dataCell.getOwnerName()
                            + EnumChatFormatting.GOLD
                            + " in Galaxy "
                            + EnumChatFormatting.RESET
                            + dataCell.getGalaxy()));
                sender.addChatMessage(
                    new ChatComponentText(EnumChatFormatting.BLUE + "-----------------------------------------------"));

                break;
            }

            case "dsp_setNode" -> {
                if (args.length == 2) {
                    TST_CommandMethods.INSTANCE.dsp_setNode(sender, args[1], null, null);
                } else if (args.length == 3) {
                    TST_CommandMethods.INSTANCE.dsp_setNode(sender, args[1], args[2], null);
                } else if (args.length >= 4) {
                    TST_CommandMethods.INSTANCE.dsp_setNode(sender, args[1], args[2], args[3]);
                } else {
                    TST_CommandMethods.INSTANCE.help_dsp_setSolarSail(sender);
                }
            }

            case "dsp_info" -> {
                TST_CommandMethods.INSTANCE.dsp_info(sender);
            }

            default -> {
                sender.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.RED + texter("Invalid command.", "TST_Command.InvalidCommand")));
            }
        }

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> l = new ArrayList<>();
        String text = args.length == 0 ? "" : args[0].trim();
        if (args.length == 0 || args.length == 1
            && (text.isEmpty() || Stream.of("dsp_join", "dsp_check", "dsp_setSolarSail", "dsp_setNode", "dsp_info")
                .anyMatch(s -> s.startsWith(text)))) {
            Stream.of("dsp_join", "dsp_check", "dsp_setSolarSail", "dsp_setNode", "dsp_info")
                .filter(s -> text.isEmpty() || s.startsWith(text))
                .forEach(l::add);
        }
        return l;
    }

    // region Methods

    // endregion

}
