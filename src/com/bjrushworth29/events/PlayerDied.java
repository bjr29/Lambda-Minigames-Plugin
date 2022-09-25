package com.bjrushworth29.events;

import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.WorldManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDied implements Listener {
	@EventHandler
	private void handler(PlayerDeathEvent event) {
		event.setDeathMessage("");
	}

	@EventHandler
	private void handler(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		event.setRespawnLocation(WorldManager.getSpawnLocation(player.getWorld()));

		Game game = GameManager.getPlayerGame(player);

		if (game != null) {

		}
	}
}
