package com.Nxer.TwistSpaceTechnology.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.Teleporter;

import com.Nxer.world.TestUseOnly;
import com.Nxer.world.WorldStats;

public class alftest_Command extends CommandBase {

    @Override
    public String getCommandName() {
        return "alftest";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Twist Space Technology Mod Commands";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            if (getCommandSenderAsPlayer(sender).dimension != WorldStats.dimensionID)
                getCommandSenderAsPlayer(sender).mcServer.getConfigurationManager()
                    .transferPlayerToDimension(
                        getCommandSenderAsPlayer(sender),
                        WorldStats.dimensionID,
                        new Teleporter(
                            getCommandSenderAsPlayer(sender).mcServer.worldServerForDimension(WorldStats.dimensionID)));
            else getCommandSenderAsPlayer(sender).mcServer.getConfigurationManager()
                .transferPlayerToDimension(
                    getCommandSenderAsPlayer(sender),
                    0,
                    new Teleporter(getCommandSenderAsPlayer(sender).mcServer.worldServerForDimension(0)));

        }
        if (args.length != 2) {
            return;
        }
        TestUseOnly.skystats[Integer.parseInt(args[0])] = Integer.parseInt(args[1]) / 100F;
    }
}
