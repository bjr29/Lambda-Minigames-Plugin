package com.bjrushworth29.commands;

import com.bjrushworth29.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddGameIdCommand implements CommandExecutor {
	public static int id = 0;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandString, String[] args) {
		if (!Debug.isUsingDebugCommands()) {
			sender.sendMessage(ChatColor.RED + "Debug commands are disabled!");
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(String.format(ChatColor.RED + "Command requires 1 arguments, %s supplied.", args.length));
			return true;
		}

		id = Integer.parseInt(args[0]);

		Debug.info("Number %s will be added on every game id", id);

		return true;
	}
}
