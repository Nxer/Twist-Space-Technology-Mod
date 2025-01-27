package com.Nxer.TwistSpaceTechnology.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataCell;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.IDSP_IO;

public class TST_CommandMethods implements IDSP_IO {

    public static final TST_CommandMethods INSTANCE = new TST_CommandMethods();

    public void dsp_info(ICommandSender sender) {
        int dim = sender.getEntityWorld().provider.dimensionId;
        DSP_Galaxy galaxy = DSP_Galaxy.getGalaxyFromDimID(dim);
        DSP_Planet planet = DSP_Planet.getPlanetFromDimID(dim);
        sender.addChatMessage(
            new ChatComponentText(
                "Current Galaxy: " + EnumChatFormatting.GOLD
                    + galaxy
                    + EnumChatFormatting.RESET
                    + " , Stellar Coefficient : "
                    + EnumChatFormatting.GREEN
                    + galaxy.stellarCoefficient));
        sender.addChatMessage(
            new ChatComponentText(
                "Current Planet: " + EnumChatFormatting.GOLD
                    + planet
                    + EnumChatFormatting.RESET
                    + " , Planetary Coefficient : "
                    + EnumChatFormatting.GREEN
                    + planet.planetaryCoefficient));
    }

    public void dsp_setNode(ICommandSender sender, String amount, String dim, String aName) {
        if (amount == null) {
            help_dsp_setSolarSail(sender);
            return;
        }

        long nodeAmount;

        try {
            nodeAmount = Long.parseLong(amount);
        } catch (NumberFormatException e) {
            sendFormatError(sender);
            return;
        }

        String userName = "defaultPlayerWithErrorInformation";
        int dimID = -114;

        if (dim == null) {
            dimID = sender.getEntityWorld().provider.dimensionId;
        } else {
            try {
                dimID = Integer.parseInt(dim);
            } catch (NumberFormatException e) {
                sendFormatError(sender);
                return;
            }
        }

        if (aName == null) {
            userName = sender.getCommandSenderName();
        } else {
            userName = aName;
        }

        DSP_DataCell dataCell = getOrInitDSPData(userName, dimID);
        dataCell.setDSPNode(nodeAmount);
        sender.addChatMessage(
            new ChatComponentText(
                EnumChatFormatting.GOLD + "Succeed to set Node amount to "
                    + EnumChatFormatting.GREEN
                    + nodeAmount
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
    }

    public void help_dsp_setSolarSail(ICommandSender sender) {
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to set Dyson Sphere Node " + EnumChatFormatting.GREEN
                    + "amount"
                    + EnumChatFormatting.RESET
                    + " of you or your "
                    + EnumChatFormatting.AQUA
                    + "team"
                    + EnumChatFormatting.RESET
                    + " in current galaxy or in "
                    + EnumChatFormatting.AQUA
                    + " dimension's galaxy "
                    + EnumChatFormatting.RESET
                    + "↓"));
        sender.addChatMessage(
            new ChatComponentText(
                "/tst_admin dsp_setNode " + EnumChatFormatting.GREEN
                    + "amount "
                    + EnumChatFormatting.AQUA
                    + "<dimID> <team name>"));
    }

    public void sendDSPInfo(ICommandSender sender) {

    }

    public void sendFormatError(ICommandSender sender) {
        sender.addChatMessage(CommandCommonText.formatError());
    }

    public void printHelp(ICommandSender sender) {
        // #tr TST_Command.printHelp.00
        // # {\GOLD} --- Twist Space Technology Mod : Dyson Sphere System Controller ---
        // #zh_CN {\GOLD} --- Twist Space Technology Mod : 戴森球系统控制 ---
        sender.addChatMessage(new ChatComponentTranslation("TST_Command.printHelp.00"));
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to join " + EnumChatFormatting.AQUA
                    + "User1"
                    + EnumChatFormatting.RESET
                    + " to "
                    + EnumChatFormatting.AQUA
                    + "User2"
                    + EnumChatFormatting.RESET
                    + " team ↓"));
        sender.addChatMessage(
            new ChatComponentText(
                "/tst dsp_join " + EnumChatFormatting.AQUA + "User1 " + EnumChatFormatting.AQUA + "User2"));
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to check " + EnumChatFormatting.AQUA
                    + "User1"
                    + EnumChatFormatting.RESET
                    + " Dyson Sphere Program Information. ↓"));
        sender.addChatMessage(new ChatComponentText("/tst dsp_check " + EnumChatFormatting.AQUA + "User1"));
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to set Dyson Sphere Solar Sail " + EnumChatFormatting.GREEN
                    + "amount"
                    + EnumChatFormatting.RESET
                    + " of you or your "
                    + EnumChatFormatting.AQUA
                    + "team"
                    + EnumChatFormatting.RESET
                    + " in current galaxy or in "
                    + EnumChatFormatting.AQUA
                    + " dimension's galaxy "
                    + EnumChatFormatting.RESET
                    + "↓"));
        sender.addChatMessage(
            new ChatComponentText(
                "/tst_admin dsp_setSolarSail " + EnumChatFormatting.GREEN
                    + "amount "
                    + EnumChatFormatting.AQUA
                    + "<dimID> <team name>"));

        help_dsp_setSolarSail(sender);
        sender.addChatMessage(
            new ChatComponentText(EnumChatFormatting.BLUE + "-----------------------------------------------"));
    }

}
