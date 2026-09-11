package com.Nxer.TwistSpaceTechnology.command;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DSP_TeamName;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DysonSpheres;

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
import com.Nxer.TwistSpaceTechnology.util.PatternConversionWorldSavedData;
import com.Nxer.TwistSpaceTechnology.util.TstSharedLocalization;

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
    public int getRequiredPermissionLevel() {
        return 0;
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
            case "team_join" -> {
                // check
                if (args.length < 3) {
                    // spotless:off
                    sender.addChatMessage(TstSharedLocalization.Command.invalidCommand());
                    sender.addChatMessage(
                        new ChatComponentText(
                            "↓ Use this to join " + EnumChatFormatting.AQUA + "User1" + EnumChatFormatting.RESET + " to " + EnumChatFormatting.AQUA + "User2" + EnumChatFormatting.RESET + " team ↓"));
                    sender.addChatMessage(
                        new ChatComponentText(
                            "/tst team_join " + EnumChatFormatting.AQUA + "User1 " + EnumChatFormatting.AQUA + "User2"));
                    // spotless:on
                    break;
                }

                String waitJoinID = args[1];
                String toJoinID = args[2];
                if (waitJoinID.isEmpty() || toJoinID.isEmpty()) {
                    sender.addChatMessage(TstSharedLocalization.Command.invalidCommand());
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
                sender.addChatMessage(new ChatComponentText("This is an admin command, please try :"));
                sender.addChatMessage(new ChatComponentText("    /tst_admin dsp_setSolarSail"));
            }

            case "dsp_setNode" -> {
                sender.addChatMessage(new ChatComponentText("This is an admin command, please try :"));
                sender.addChatMessage(new ChatComponentText("    /tst_admin dsp_setNode"));
            }

            case "dsp_info" -> {
                TST_CommandMethods.INSTANCE.dsp_info(sender);
            }

            case "get_container_items" -> {
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText("Usage: /tst get_container_items <source|getModItem>"));
                    break;
                }
                ContainerDumpMode mode = ContainerDumpMode.parse(args[1]);
                if (mode == null) {
                    sender.addChatMessage(new ChatComponentText("Usage: /tst get_container_items <source|getModItem>"));
                    break;
                }
                TST_CommandMethods.INSTANCE.dumpContainer(sender, mode);
            }

            case "ae_pattern_conversion" -> {
                processPatternConversion(sender, args, 1);
            }

            default -> {
                sender.addChatMessage(TstSharedLocalization.Command.invalidCommand());
            }
        }
    }

    private final String[] Commands = { "help", "team_join", "dsp_check", "dsp_setSolarSail", "dsp_setNode", "dsp_info",
        "get_container_items", "ae_pattern_conversion" };

    private void processPatternConversion(ICommandSender sender, String[] args, int actionIndex) {
        String playerName = sender.getCommandSenderName();
        if (args.length < actionIndex + 1) {
            sender.addChatMessage(
                new ChatComponentText("Usage: /tst ae_pattern_conversion <status|ampoule <on|off>|crystal <on|off>>"));
            return;
        }

        switch (args[actionIndex]) {
            case "status" -> {
                sender.addChatMessage(
                    new ChatComponentText(
                        "AE2 pattern conversion status:" + " ampoule "
                            + (PatternConversionWorldSavedData.isPatternConversionEnabledFor(playerName) ? "ENABLED"
                                : "DISABLED")
                            + ", crystal essence "
                            + (PatternConversionWorldSavedData.isCrystalConversionEnabledFor(playerName) ? "ENABLED"
                                : "DISABLED")
                            + "."));
            }
            case "ampoule" -> {
                if (args.length < actionIndex + 2) {
                    sender.addChatMessage(new ChatComponentText("Usage: /tst ae_pattern_conversion ampoule <on|off>"));
                    return;
                }
                switch (args[actionIndex + 1]) {
                    case "on" -> {
                        PatternConversionWorldSavedData.setPatternConversionEnabled(playerName, true);
                        sender.addChatMessage(new ChatComponentText("AE2 ampoule conversion is now ENABLED for you."));
                    }
                    case "off" -> {
                        PatternConversionWorldSavedData.setPatternConversionEnabled(playerName, false);
                        sender.addChatMessage(new ChatComponentText("AE2 ampoule conversion is now DISABLED for you."));
                    }
                    default -> {
                        sender.addChatMessage(TstSharedLocalization.Command.invalidCommand());
                        sender.addChatMessage(
                            new ChatComponentText("Usage: /tst ae_pattern_conversion ampoule <on|off>"));
                    }
                }
            }
            case "crystal" -> {
                if (args.length < actionIndex + 2) {
                    sender.addChatMessage(new ChatComponentText("Usage: /tst ae_pattern_conversion crystal <on|off>"));
                    return;
                }
                switch (args[actionIndex + 1]) {
                    case "on" -> {
                        PatternConversionWorldSavedData.setCrystalConversionEnabled(playerName, true);
                        sender.addChatMessage(
                            new ChatComponentText("AE2 crystal essence conversion is now ENABLED for you."));
                    }
                    case "off" -> {
                        PatternConversionWorldSavedData.setCrystalConversionEnabled(playerName, false);
                        sender.addChatMessage(
                            new ChatComponentText("AE2 crystal essence conversion is now DISABLED for you."));
                    }
                    default -> {
                        sender.addChatMessage(TstSharedLocalization.Command.invalidCommand());
                        sender.addChatMessage(
                            new ChatComponentText("Usage: /tst ae_pattern_conversion crystal <on|off>"));
                    }
                }
            }
            default -> {
                sender.addChatMessage(TstSharedLocalization.Command.invalidCommand());
                sender.addChatMessage(
                    new ChatComponentText(
                        "Usage: /tst ae_pattern_conversion <status|ampoule <on|off>|crystal <on|off>>"));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> l = new ArrayList<>();
        // Second level under AE_pattern_conversion: suggest status/ampoule/crystal.
        if (args.length == 2 && "ae_pattern_conversion".equals(args[0])) {
            String sub = args[1].trim();
            String[] subCommands = { "status", "ampoule", "crystal" };
            Stream.of(subCommands)
                .filter(s -> sub.isEmpty() || s.startsWith(sub))
                .forEach(l::add);
            return l;
        }
        // Third level: suggest on|off after ampoule/crystal.
        if (args.length == 3 && "ae_pattern_conversion".equals(args[0])) {
            String sub = args[2].trim();
            String[] subCommands = { "on", "off" };
            Stream.of(subCommands)
                .filter(s -> sub.isEmpty() || s.startsWith(sub))
                .forEach(l::add);
            return l;
        }
        String text = args.length == 0 ? "" : args[0].trim();
        if (args.length == 0 || args.length == 1 && (text.isEmpty() || Stream.of(Commands)
            .anyMatch(s -> s.startsWith(text)))) {
            Stream.of(Commands)
                .filter(s -> text.isEmpty() || s.startsWith(text))
                .forEach(l::add);
        }
        if (args.length == 2 && "get_container_items".equals(args[0])) {
            String mode = args[1].trim();
            Stream.of("source", "getModItem")
                .filter(s -> mode.isEmpty() || s.startsWith(mode))
                .forEach(l::add);
        }
        return l;
    }

    // region Methods

    // endregion

}
