package com.bjrushworth29.events;

import com.bjrushworth29.managers.InventoryLoadoutManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import com.bjrushworth29.managers.WorldManager;
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

		WorldManager.teleportToSpawn(player, WorldManager.getWorld("hub"));

		InventoryLoadoutManager.giveInventoryLoadout(player, InventoryLoadoutManager.getDefaultLoadout("hub"));
		PlayerConstraintManager.applyConstraints(player, "hub");

		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Welcome!");
	}
}
