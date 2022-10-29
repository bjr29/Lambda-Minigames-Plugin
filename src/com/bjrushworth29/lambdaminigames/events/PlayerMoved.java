package com.bjrushworth29.lambdaminigames.events;

import com.bjrushworth29.lambdaminigames.games.util.Game;
import com.bjrushworth29.lambdaminigames.managers.GameManager;
import com.bjrushworth29.lambdaminigames.managers.PlayerConstraintManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoved implements Listener {
	@EventHandler
	private void handler(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (!PlayerConstraintManager.getAppliedConstraints(player).canMove()) {
			player.teleport(event.getFrom().setDirection(event.getTo().getDirection()));
			return;
		}

		Game game = GameManager.getPlayerGame(player);

		if (game != null) {
			game.handleMovement(event);
		}
	}
}
