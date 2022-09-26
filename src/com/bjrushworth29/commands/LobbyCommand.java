package com.bjrushworth29.commands;

import com.bjrushworth29.enums.DefaultWorld;
import com.bjrushworth29.managers.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandString, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(ChatColor.RED + "Must be a player to use this command.");

			return true;
		}

		WorldManager.teleportToSpawn(player, WorldManager.getWorld(DefaultWorld.HUB));

		return true;
	}
}
