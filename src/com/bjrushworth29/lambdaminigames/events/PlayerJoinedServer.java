package com.bjrushworth29.lambdaminigames.events;

import com.bjrushworth29.lambdaminigames.enums.DefaultInventoryLoadout;
import com.bjrushworth29.lambdaminigames.enums.DefaultWorld;
import com.bjrushworth29.lambdaminigames.managers.InventoryManager;
import com.bjrushworth29.lambdaminigames.managers.PlayerConstraintManager;
import com.bjrushworth29.lambdaminigames.managers.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinedServer implements Listener {
	@EventHandler
	private void handler(PlayerJoinEvent event) {
		event.setJoinMessage("");

		Player player = event.getPlayer();

		WorldManager.teleportToSpawn(player, WorldManager.getWorld(DefaultWorld.HUB));

		InventoryManager.give(player, InventoryManager.get(DefaultInventoryLoadout.HUB));
		PlayerConstraintManager.applyConstraints(player, "hub");

		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Welcome!");
	}
}
