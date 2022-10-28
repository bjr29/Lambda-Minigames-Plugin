package com.bjrushworth29.events;

import com.bjrushworth29.commands.ChangeKnockbackCommand;
import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.utils.PlayerGameData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

public class PlayerVelocity implements Listener {
	@EventHandler
	private void handler(PlayerVelocityEvent event) {
		Player player = event.getPlayer();
		Game game = GameManager.getPlayerGame(player);

		if (game == null) {
			return;
		}

		PlayerGameData playerData = game.getPlayerGameConstraints(player);
		Player attacker = playerData.getPreviousAttacker();

		if (playerData.shouldApplyKnockback()) {
			event.setVelocity(
					attacker.getLocation()
							.getDirection()
							.normalize()
							.setY(ChangeKnockbackCommand.knockbackUpward)
							.multiply(ChangeKnockbackCommand.knockbackForward)
			);

			playerData.setApplyKnockback(false);
		}
	}
}
