package com.bjrushworth29.commands;

import com.bjrushworth29.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChangeKnockbackCommand implements CommandExecutor {
	public static double knockbackForward = 1;
	public static double knockbackUpward = 1;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
		if (!Debug.isUsingDebugCommands()) {
			sender.sendMessage(ChatColor.RED + "Debug commands are disabled!");
			return true;
		}

		if (args.length != 2) {
			sender.sendMessage(String.format(ChatColor.RED + "Command requires 2 arguments, %s supplied.", args.length));
			return true;
		}

		knockbackForward = Double.parseDouble(args[0]);
		knockbackUpward = Double.parseDouble(args[1]);

		sender.sendMessage(String.format("Knockback is now %s, %s", knockbackForward, knockbackUpward));

		return true;
	}
}
