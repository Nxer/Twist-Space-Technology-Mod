package com.Nxer.TwistSpaceTechnology.command;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataCell;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.IDSP_IO;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DSP_TeamName;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataStorageMaps.DysonSpheres;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

public final class TST_Command
	extends CommandBase
	implements IDSP_IO {
	
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
			printHelp(sender);
			return;
		}
		
		switch (args[0]){
			case "help" ->{
				printHelp(sender);
				break;
			}
			
			case "dsp_join" ->{
				String waitJoinID = args[1];
				String toJoinID = args[2];
				if (waitJoinID.isEmpty() || toJoinID.isEmpty()){
					sender.addChatMessage(
						new ChatComponentText(
							EnumChatFormatting.RED
								+ texter("Invalid command.","TST_Command.InvalidCommand")));
					break;
				}
				joinUserTeam(waitJoinID, toJoinID);
				TwistSpaceTechnology.LOG.info("test command dsp_join: " + waitJoinID + " -> " + toJoinID);
				break;
			}
			
			case "dsp_check" ->{
				if (args.length<2){
					sender.addChatMessage(new ChatComponentText(
						"↓ Use this to check "+EnumChatFormatting.AQUA+"User"+EnumChatFormatting.RESET+" Dyson Sphere Program Information. ↓"
					));
					sender.addChatMessage(new ChatComponentText(
						"/tst dsp_check "+EnumChatFormatting.AQUA+"User"
					));
					break;
				}
				
				String checkName = args[1];
				if (DSP_TeamName == null){
					sender.addChatMessage(new ChatComponentText(
						EnumChatFormatting.BOLD+""+EnumChatFormatting.RED+"ERROR! "+EnumChatFormatting.RESET+"internal Map DSP_TeamName is "+EnumChatFormatting.RED+"NULL"
					));
					break;
				}
				if (!DSP_TeamName.containsKey(checkName)){
					sender.addChatMessage(new ChatComponentText(
						"User "+EnumChatFormatting.AQUA+checkName+EnumChatFormatting.RESET+" has not join a team."
					));
					break;
				}
				
				String teamName = DSP_TeamName.get(checkName);
				if (!DysonSpheres.containsKey(teamName)){
					sender.addChatMessage(new ChatComponentText(
						"User "+EnumChatFormatting.AQUA+checkName+EnumChatFormatting.RESET+" is in team "+EnumChatFormatting.GOLD+teamName+EnumChatFormatting.RESET+"."
					));
					sender.addChatMessage(new ChatComponentText(
						"But team "+EnumChatFormatting.GOLD+teamName+EnumChatFormatting.RESET+" has not created a Dyson Sphere in any galaxy."
					));
					break;
				}
				
				HashMap<DSP_Galaxy, DSP_DataCell> teamsDSP = DysonSpheres.get(teamName);
				if (teamsDSP.isEmpty()){
					sender.addChatMessage(new ChatComponentText(
						"User "+EnumChatFormatting.AQUA+checkName+EnumChatFormatting.RESET+" is in team "+EnumChatFormatting.GOLD+teamName+EnumChatFormatting.RESET+"."
					));
					sender.addChatMessage(new ChatComponentText(
						"But team's Dyson Sphere Program is null."
					));
				}
				sender.addChatMessage(new ChatComponentText(
					"User "+EnumChatFormatting.AQUA+checkName+EnumChatFormatting.RESET+" is in team "+EnumChatFormatting.GOLD+teamName+EnumChatFormatting.RESET+"."
				));
				for (Map.Entry<DSP_Galaxy, DSP_DataCell> dataCell : teamsDSP.entrySet()){
					sender.addChatMessage(new ChatComponentText(
						"Galaxy: "+EnumChatFormatting.GOLD+dataCell.getKey()+EnumChatFormatting.RESET+" Dyson Sphere: "+dataCell.getValue()+"."
					));
				}
				break;
			}
			
			default -> {
				sender.addChatMessage(
					new ChatComponentText(
						EnumChatFormatting.RED
							+ texter("Invalid command.","TST_Command.InvalidCommand")));
			}
		}
	
	}
	
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args){
		List<String> l = new ArrayList<>();
		String text = args.length == 0 ? "" : args[0].trim();
		if (args.length == 0 || args.length == 1 && (text.isEmpty()
					           || Stream.of(
								   "dsp_join",
								   "dsp_check").anyMatch(s -> s.startsWith(text))))
		{
			Stream.of(
				"dsp_join",
				"dsp_check")
				.filter(s -> text.isEmpty() || s.startsWith(text))
				.forEach(l::add);
		}
		return l;
	}
	
	private void printHelp(ICommandSender sender){
		sender.addChatMessage(new ChatComponentText(
			EnumChatFormatting.GOLD
				+ " --- "
				+ texter("Twist Space Technology Mod : Dyson Sphere System Controller","TST_Command.printHelp.00")
				+ " --- "
		));
		sender.addChatMessage(new ChatComponentText(
			"↓ Use this to join "+EnumChatFormatting.AQUA+"User1"+EnumChatFormatting.RESET+" to "+EnumChatFormatting.AQUA+"User2"+EnumChatFormatting.RESET+" team ↓"
		));
		sender.addChatMessage(new ChatComponentText(
			"/tst dsp_join "+EnumChatFormatting.AQUA+"User1 "+EnumChatFormatting.AQUA+"User2"
		));
		sender.addChatMessage(new ChatComponentText(
			"↓ Use this to check "+EnumChatFormatting.AQUA+"User1"+EnumChatFormatting.RESET+" Dyson Sphere Program Information. ↓"
		));
		sender.addChatMessage(new ChatComponentText(
			"/tst dsp_check "+EnumChatFormatting.AQUA+"User1"
		));
	}
	
}
