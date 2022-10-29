package com.bjrushworth29.lambdaminigames.commands;

import com.bjrushworth29.lambdaminigames.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EnableRemoveGameCommand implements CommandExecutor {
	public static boolean state = true;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
		if (!Debug.isUsingDebugCommands()) {
			sender.sendMessage(ChatColor.RED + "Debug commands are disabled!");
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(String.format(ChatColor.RED + "Command requires 2 arguments, %s supplied.", args.length));
			return true;
		}

		state = Boolean.parseBoolean(args[0]);

		sender.sendMessage(String.format("Destroy map: %b", state));

		return true;
	}
}
