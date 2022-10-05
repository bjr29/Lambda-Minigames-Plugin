package com.bjrushworth29.events;

import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoved implements Listener {
	@EventHandler
	private void handler(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (!PlayerConstraintManager.getAppliedConstraints(player).canMove()) {
			return;
		}

		Game game = GameManager.getPlayerGame(player);

		if (game != null) {
			game.handleMovement(event);
		}
	}
}
