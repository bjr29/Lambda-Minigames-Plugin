package com.bjrushworth29.commands;

import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.managers.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndGameCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandString, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(ChatColor.RED + "Must be a player to use this command.");

			return true;
		}

		Game game = GameManager.getPlayerGame(player);

		if (game == null) {
			player.sendMessage(ChatColor.RED + "That player isn't in a game");
			return true;
		}

		game.end(player);

		return true;
	}
}
